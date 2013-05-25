/*
 * Copyright 2013 Davy Verstappen.
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
package org.wintersleep.wicket.hibernate;

import org.apache.wicket.core.util.lang.WicketObjects;
import org.apache.wicket.model.LoadableDetachableModel;

import java.io.Serializable;

public class HibernateObjectModel<ID extends Serializable, T> extends LoadableDetachableModel<T> {

    private final String className;
    private final ID id;

    public HibernateObjectModel(Class<T> clazz, ID id) {
        this.className = clazz.getName();
        this.id = id;
    }

    public HibernateObjectModel(T object) {
        super(object);
        this.className = object.getClass().getName(); // this won't work in case of a proxy ...
        this.id = (ID) WebAppHibernateUtil.getCurrentSession().getIdentifier(object);
    }

    @Override
    protected T load() {
        return (T) WebAppHibernateUtil.getCurrentSession().load(getPersistentClass(), id);
    }

    private Class<T> getPersistentClass() {
        return WicketObjects.resolveClass(className);
    }

    @Override
    public String toString() {
        return "HibernateObjectModel:" + className + "#" + id;
    }

}
