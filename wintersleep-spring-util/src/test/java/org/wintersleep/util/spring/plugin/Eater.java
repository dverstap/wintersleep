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

import java.util.List;

public class Eater {

    private Iterable<Fruit> fruits;

    public Eater() {
    }

    public Iterable<Fruit> getFruits() {
        return fruits;
    }

    public void setFruits(Iterable<Fruit> fruits) {
        // ensure that the list is complete at the moment it is injected
        List fruitsList = (List) fruits;
        if (fruitsList.size() != 4) {
            throw new IllegalArgumentException("Expected 4 fruits");
        }
        this.fruits = fruits;
    }

    public void doMyThing() {
        for (Fruit fruit : fruits) {
            System.out.println(fruit.getName());
        }
    }

    
}
