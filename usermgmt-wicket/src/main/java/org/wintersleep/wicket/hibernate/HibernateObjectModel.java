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

import com.google.common.base.Preconditions;
import org.apache.wicket.core.util.lang.WicketObjects;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;
import org.hibernate.TransientObjectException;

import java.io.Serializable;

public class HibernateObjectModel<ID extends Serializable, T> extends LoadableDetachableModel<T> {

    private final String className;
    private ID id;
    private T unsavedObject;

    public HibernateObjectModel(Class<T> clazz, ID id) {
        this.className = clazz.getName();
        this.id = id;
    }

    public HibernateObjectModel(T object) {
        super(object);
        Session session = getCurrentSession();
        this.className = determineClassName(object);
        try {
            this.id = (ID) session.getIdentifier(object);
        } catch (TransientObjectException e) {
            this.unsavedObject = object;
        }
        checkInvariant();
    }

    private String determineClassName(T object) {
        try {
            return getCurrentSession().getEntityName(object);
        } catch (TransientObjectException e) {
            return object.getClass().getName();
        }
    }

    @Override
    protected T load() {
        checkInvariant();
        if (id != null) {
            return (T) getCurrentSession().load(getPersistentClass(), id);
        }
        return unsavedObject;
    }

    private void checkInvariant() {
        Preconditions.checkState((id == null && unsavedObject != null)
                || (id != null && unsavedObject == null));
    }

    protected Session getCurrentSession() {
        return WicketHibernateUtil.getCurrentSession();
    }

    @Override
    protected void onDetach() {
        checkInvariant();
        if (id == null) {
            try {
                this.id = (ID) getCurrentSession().getIdentifier(unsavedObject);
                this.unsavedObject = null;
            } catch (TransientObjectException e) {
                // keep the unsavedObject and serialize it
            }
        }
        checkInvariant();
    }

    private Class<T> getPersistentClass() {
        return WicketObjects.resolveClass(className);
    }

    @Override
    public String toString() {
        return "HibernateObjectModel:" + className + "#" + id;
    }

}
