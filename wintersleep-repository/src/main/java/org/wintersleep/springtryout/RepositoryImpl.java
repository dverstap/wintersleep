package org.wintersleep.springtryout;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class RepositoryImpl<T extends Serializable, ID extends Serializable> implements Repository<T, ID> {

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

    @SuppressWarnings("unchecked")
    public T findById(ID id) {
        //return findById(id, false);
        return (T) getSession().get(getPersistentClass(), id);
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock)
            entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
        else
            entity = (T) getSession().load(getPersistentClass(), id);

        return entity;
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return findByCriteria();
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public T merge(T entity) {
        return (T) getSession().merge(entity);
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


}
