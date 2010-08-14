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
package org.wintersleep.repository;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Projections;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.beans.Introspector;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

public class RepositoryImpl<T extends Serializable, ID extends Serializable> implements Repository<T, ID> {

    private static final Logger logger = Logger.getLogger(RepositoryImpl.class);

    protected final SessionFactory sessionFactory;
    protected final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public RepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public T findById(ID id) {
        //return loadById(id, false);
        return getPersistentClass().cast(getSession().get(getPersistentClass(), id));
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public T loadById(ID id, boolean lock) {
        T entity;
        if (lock)
            entity = getPersistentClass().cast(getSession().load(getPersistentClass(), id, LockMode.UPGRADE));
        else
            entity = getPersistentClass().cast(getSession().load(getPersistentClass(), id));

        return entity;
    }

    public List<T> findAll() {
        return findByCriteria();
    }

    public long countAll() {
        return (Integer) getSession().createCriteria(getPersistentClass()).setProjection(Projections.rowCount()).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public T merge(T entity) {
        return getPersistentClass().cast(getSession().merge(entity));
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     * @param criteria The criteria to be filtered on
     * @return The list of entities that are found to match the criteria
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criteria) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criteria) {
            crit.add(c);
        }
        return crit.list();
    }

    protected T uniqueResult(Criterion... criteria) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criteria) {
            crit.add(c);
        }
        return getPersistentClass().cast(crit.uniqueResult());
    }

    @SuppressWarnings("unchecked")
    protected List<T> list(Criteria criteria) {
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    protected List<T> list(String query) {
        return getSession().createQuery(query).list();
    }

    // explicit logging in here, because this executed during class loading,
    // and there's often no good error logging at that point.
    protected static <T> Property findProperty(Class<T> persistentClass, String propertyName) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(persistentClass);
        } catch (IntrospectionException e) {
            String message = "Couldn't get BeanInfo for " + persistentClass + " while looking for property " + propertyName;
            logger.error(message, e);
            throw new RuntimeException(message, e);
        }
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (propertyDescriptor.getName().equals(propertyName)) {
                return Property.forName(propertyName);
            }
        }
        String message = "Couldn't find property " + propertyName + " in " + persistentClass;
        RuntimeException exception = new RuntimeException(message);
        logger.error(message, exception);
        throw exception;
    }

    protected Criteria createCriteria() {
        return getSession().createCriteria(getPersistentClass());
    }
}
