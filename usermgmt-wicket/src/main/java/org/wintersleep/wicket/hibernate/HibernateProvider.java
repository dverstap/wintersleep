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

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import java.io.Serializable;
import java.util.Iterator;

public class HibernateProvider<ID extends Serializable, T> implements IDataProvider<T> {

    private final String persistentClassName;
    private final CriteriaBuilder filterCriteriaBuilder;

    public HibernateProvider(Class<T> clazz) {
        this.persistentClassName = clazz.getName();
        this.filterCriteriaBuilder = null;
    }

    public HibernateProvider(Class<T> clazz, CriteriaBuilder filterCriteriaBuilder) {
        this.persistentClassName = clazz.getName();
        this.filterCriteriaBuilder = filterCriteriaBuilder;
    }

    @Override
    public Iterator<? extends T> iterator(long first, long count) {
        Criteria criteria = createCriteria()
                .setFirstResult((int) first)
                .setFetchSize((int) count);
        return criteria.list().iterator();
    }

    protected Criteria createCriteria() {
        Criteria criteria = WicketHibernateUtil.getCurrentSession().createCriteria(getPersistentClass());
        if (filterCriteriaBuilder != null) {
            filterCriteriaBuilder.build(criteria);
        }
        return criteria;
    }

    @Override
    public long size() {
        Long result = (Long) createCriteria().setProjection(Projections.rowCount()).uniqueResult();
        return result.intValue();
    }

    @Override
    public IModel<T> model(T object) {
        return new HibernateObjectModel<>(object);
    }

    @Override
    public void detach() {
        // nothing to do
    }

    public Class<T> getPersistentClass() {
        try {
            return (Class<T>) Class.forName(persistentClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
