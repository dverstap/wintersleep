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

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

public class PluginTest extends AbstractDependencyInjectionSpringContextTests {

    private Eater eater;

    public void setEater(Eater eater) {
        this.eater = eater;
    }
    
    @Override
    protected String[] getConfigPaths() {
        return new String[] { "testPluginContext1.xml", "testPluginContext2.xml", "testApplicationContextWithExtensionPoint.xml" };
    }

    public void test() {
        List<Fruit> fruits = (List<Fruit>) eater.getFruits();
        assertEquals(4, fruits.size());
        assertEquals("apple", fruits.get(0).getName());
        assertEquals("banana", fruits.get(1).getName());
        assertEquals("pear", fruits.get(2).getName());
        assertEquals("kiwi", fruits.get(3).getName());
    }
}
