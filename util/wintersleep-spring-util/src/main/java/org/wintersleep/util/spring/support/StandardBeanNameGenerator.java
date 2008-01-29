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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.beans.Introspector;

public class StandardBeanNameGenerator implements BeanNameGenerator {

    private BeanNameGenerator next;
    private final Set<String> dropPrefixes;
    private final Set<String> dropPostfixes;

    public StandardBeanNameGenerator() {
        this(new String[]{"Dummy", "Mock"}, new String[]{"Impl"});
        next = new AnnotationBeanNameGenerator();
    }

    public StandardBeanNameGenerator(String[] dropPrefixes, String[] dropPostfixes) {
        this.dropPrefixes = new LinkedHashSet<String>(Arrays.asList(dropPrefixes));
        this.dropPostfixes = new LinkedHashSet<String>(Arrays.asList(dropPostfixes));
    }

    public StandardBeanNameGenerator(Set<String> dropPrefixes, Set<String> dropPostfixes) {
        this.dropPrefixes = dropPrefixes;
        this.dropPostfixes = dropPostfixes;
    }

    public BeanNameGenerator getNext() {
        return next;
    }

    public void setNext(BeanNameGenerator next) {
        this.next = next;
    }

    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) {
        String result = next.generateBeanName(beanDefinition, beanDefinitionRegistry);
        return process(result);
    }

    String process(String result) {
        result = dropPrefix(result);
        result = dropPostfix(result);
        return Introspector.decapitalize(result);
    }

    private String dropPrefix(String result) {
        for (String prefix : dropPrefixes) {
            if (result.startsWith(prefix)) {
                return result.substring(prefix.length());
            }
        }
        return result;
    }

    private String dropPostfix(String result) {
        for (String p : dropPostfixes) {
            if (result.endsWith(p)) {
                return result.substring(0, result.length()-p.length());
            }
        }
        return result;
    }

}
