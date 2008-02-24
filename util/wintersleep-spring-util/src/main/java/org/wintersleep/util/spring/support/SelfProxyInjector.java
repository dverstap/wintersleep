/*
 * Copyright 2008 Davy Verstappen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wintersleep.util.spring.support;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanWrapper;
import org.springframework.aop.framework.Advised;

public class SelfProxyInjector implements BeanPostProcessor {

    private String selfProxyPropertyName = "selfProxy";

    public String getSelfProxyPropertyName() {
        return selfProxyPropertyName;
    }

    public void setSelfProxyPropertyName(String selfProxyPropertyName) {
        this.selfProxyPropertyName = selfProxyPropertyName;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private Object findTargetBean(Object bean) {
        Object targetBean = bean;
        while (targetBean instanceof Advised) {
            try {
                targetBean = ((Advised)targetBean).getTargetSource().getTarget();
            } catch (Exception e) {
                throw new BeanInitializationException("Unable to resolve target", e);
            }
        }
        return targetBean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Advised) {
            Object targetBean = findTargetBean(bean);
            BeanWrapper beanWrapper = new BeanWrapperImpl(targetBean);
            if (beanWrapper.isWritableProperty(selfProxyPropertyName)) {
                beanWrapper.setPropertyValue(selfProxyPropertyName, bean);
            }
        }
        return bean;
    }
}
