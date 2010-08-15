/*
 * Copyright 2009 Davy Verstappen.
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
package org.wintersleep.codeviz.uml.model;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.*;

import java.util.Iterator;

public class HibernateModelFactory {

    private final Configuration configuration;
    private final String name;

    public HibernateModelFactory(Configuration configuration, String name) {
        this.configuration = configuration;
        this.name = name;
    }

    public CodeModel create() {
        CodeModel model = new CodeModel(name);
        addClasses(model);
        try {
            addRelations(model);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //model.addFieldRelations();


        return model;
    }

    private void addClasses(CodeModel model) {
        Iterator<PersistentClass> pci = configuration.getClassMappings();
        while (pci.hasNext()) {
            PersistentClass persistentClass = pci.next();
            Class clazz = persistentClass.getMappedClass();
            model.add(clazz);
        }
    }

    private void addRelations(CodeModel model) throws ClassNotFoundException {
        Iterator<PersistentClass> pci = configuration.getClassMappings();
        while (pci.hasNext()) {
            PersistentClass persistentClass = pci.next();
            final ModelClass mc = model.findModelClass(persistentClass.getMappedClass());
            Iterator<Property> pi = persistentClass.getPropertyIterator();
            while (pi.hasNext()) {
                Property property = pi.next();
                System.out.println(property);
                Value value = property.getValue();
                if (value instanceof ToOne) {
                    ToOne toOne = (ToOne) value;
                    ModelClass toClass = model.findModelClass(Class.forName(toOne.getReferencedEntityName()));
                    RelationEndpoint fromEndpoint = new RelationEndpoint(mc, property.getName());
                    fromEndpoint.setMinCardinality(toOne.isNullable() ? 0 : 1);
                    RelationEndpoint toEndpoint = new RelationEndpoint(toClass, toOne.getReferencedPropertyName());
                    toEndpoint.setMinCardinality(0);
                    toEndpoint.setMaxCardinality(RelationEndpoint.MANY_CARDINALITY);
                    if (value instanceof OneToOne) {
                        fromEndpoint.setMaxCardinality(1);
                    } else if (value instanceof ManyToOne) {
                        fromEndpoint.setMaxCardinality(1);
                    }
                    mc.addRelationTo(fromEndpoint, toEndpoint);
                } else if (value instanceof OneToMany) {
                    OneToMany oneToMany = (OneToMany) value;
                    oneToMany.getAssociatedClass();
                    ModelClass toClass = model.findModelClass(oneToMany.getAssociatedClass().getMappedClass());
                    RelationEndpoint fromEndpoint = new RelationEndpoint(mc, property.getName());
                    fromEndpoint.setMinCardinality(0);
                    fromEndpoint.setMaxCardinality(RelationEndpoint.MANY_CARDINALITY);
                    RelationEndpoint toEndpoint = new RelationEndpoint(toClass, "TODO other");
                    toEndpoint.setMinCardinality(oneToMany.isNullable() ? 0 : 1);
                    toEndpoint.setMaxCardinality(1);
                    mc.addRelationTo(fromEndpoint, toEndpoint);
                }
            }
        }
    }

}
