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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;

import java.util.List;

/**
 * Based on: http://www.devx.com/Java/Article/31835
 */
public class PluginBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private String extensionBeanName;
    private String propertyName;
    private String pluginBeanName;
    
    public void setExtensionBeanName(String extensionBeanName) {
        this.extensionBeanName = extensionBeanName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setPluginBeanName(String pluginBeanName) {
        this.pluginBeanName = pluginBeanName;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // find the bean definition we wish to modify
        BeanDefinition beanDef = beanFactory.getBeanDefinition(extensionBeanName);

        // within that bean def look for its properties and find
        // the specific property we will modify.
        MutablePropertyValues propValues = beanDef.getPropertyValues();
        if (!propValues.contains(propertyName)) {
            throw new IllegalArgumentException("Cannot find property " +
                    propertyName + " in bean " + extensionBeanName);
        }
        PropertyValue pv = propValues.getPropertyValue(propertyName);

        // pull out the value definition (in our case we only supporting
        // updating of List style properties)
        Object prop = pv.getValue();
        if (!(prop instanceof List)) {
            throw new IllegalArgumentException("Property " + propertyName + " in extension bean " +
                    extensionBeanName + " is not an instanceof List.");
        }
        // add our bean reference to the list, when Spring creates the
        // objects and wires them together our bean is now in place.
        List l = (List) pv.getValue();
        l.add(new RuntimeBeanReference(pluginBeanName));
    }


}
