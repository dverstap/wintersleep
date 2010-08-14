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

package org.wintersleep.util.spring.plugin;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;

import java.util.Collections;
import java.util.List;

/**
 * Based on: http://www.devx.com/Java/Article/31835
 */
public class PluginBeanFactoryPostProcessor implements BeanFactoryPostProcessor, InitializingBean {

    private String extensionPointBeanName;
    private String extensionPointPropertyName;
    private String extensionBeanName;
    private List<String> extensionBeanNames;

    public void setExtensionPointBeanName(String extensionPointBeanName) {
        this.extensionPointBeanName = extensionPointBeanName;
    }

    public void setExtensionPointPropertyName(String extensionPointPropertyName) {
        this.extensionPointPropertyName = extensionPointPropertyName;
    }

    public void setExtensionBeanName(String extensionBeanName) {
        this.extensionBeanName = extensionBeanName;
    }

    public void setExtensionBeanNames(List<String> extensionBeanNames) {
        this.extensionBeanNames = extensionBeanNames;
    }

    public void afterPropertiesSet() throws Exception {
        if (extensionBeanName == null && extensionBeanNames == null) {
            throw new IllegalStateException("Either extensionBeanName or extensionBeanNames must be set.");
        }
        if (extensionBeanName != null && extensionBeanNames != null) {
            throw new IllegalStateException("Either extensionBeanName or extensionBeanNames must be set.");
        }
    }

    private List<String> getExtensionBeanNames() {
        if (extensionBeanNames != null) {
            return extensionBeanNames;
        } else {
            return Collections.singletonList(extensionBeanName);
        }
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // find the bean definition we wish to modify
        BeanDefinition beanDef = beanFactory.getBeanDefinition(extensionPointBeanName);

        // within that bean def look for its properties and find
        // the specific property we will modify.
        MutablePropertyValues propValues = beanDef.getPropertyValues();
        if (!propValues.contains(extensionPointPropertyName)) {
            throw new IllegalArgumentException("Cannot find property " +
                    extensionPointPropertyName + " in bean " + extensionPointBeanName);
        }
        PropertyValue pv = propValues.getPropertyValue(extensionPointPropertyName);

        // pull out the value definition (in our case we only supporting
        // updating of List style properties)
        Object prop = pv.getValue();
        List l;
        if (prop instanceof ManagedList) {
            l = (List) prop;
        } else if (prop instanceof BeanDefinitionHolder) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder) prop;
            GenericBeanDefinition extensionPointBeanDefinition = (GenericBeanDefinition) holder.getBeanDefinition();
            PropertyValue sourceListValue = extensionPointBeanDefinition.getPropertyValues().getPropertyValue("sourceList");
            l = (List) sourceListValue.getValue();
        } else {
            throw new IllegalArgumentException("Cannot modify property " + extensionPointPropertyName + " in extension bean " +
                    extensionPointBeanName + " of type: " + prop.getClass());
        }
        for (String name : getExtensionBeanNames()) {
            l.add(new RuntimeBeanReference(name));
        }

    }


}
