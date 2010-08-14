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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class RegisterExtensionBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class getBeanClass(Element element) {
        return PluginBeanFactoryPostProcessor.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("extensionPointBeanName", element.getAttribute("extension-point"));
        bean.addPropertyValue("extensionPointPropertyName", element.getAttribute("property"));

        List<String> extensionBeanNames = new ArrayList<String>();
        NodeList list = element.getElementsByTagNameNS("http://wintersleep.org/schema/plugin", "extension");
        for (int i = 0; i < list.getLength(); i++) {
            Element extensionElement = (Element) list.item(i);
            extensionBeanNames.add(extensionElement.getAttribute("ref"));
        }
        bean.addPropertyValue("extensionBeanNames", extensionBeanNames);
    }

    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }
}
