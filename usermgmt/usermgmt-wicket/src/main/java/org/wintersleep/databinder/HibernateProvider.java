package org.wintersleep.databinder;

import net.databinder.hib.Databinder;
import net.databinder.models.hib.CriteriaBuilder;
import net.databinder.models.hib.OrderingCriteriaBuilder;
import net.databinder.models.hib.QueryBinder;
import net.databinder.models.hib.QueryBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import java.lang.reflect.Field;

public class HibernateProvider<T> extends net.databinder.models.hib.HibernateProvider<T> {

    public HibernateProvider(Class objectClass) {
        super(objectClass);
    }

    public HibernateProvider(Class objectClass, CriteriaBuilder criteriaBuilder, CriteriaBuilder criteriaOrderer) {
        super(objectClass, criteriaBuilder, criteriaOrderer);
    }

    public HibernateProvider(Class objectClass, OrderingCriteriaBuilder criteriaBuider) {
        super(objectClass, criteriaBuider);
    }

    public HibernateProvider(Class objectClass, CriteriaBuilder criteriaBuilder) {
        super(objectClass, criteriaBuilder);
    }

    public HibernateProvider(String query) {
        super(query);
    }

    public HibernateProvider(String query, String countQuery) {
        super(query, countQuery);
    }

    public HibernateProvider(String query, QueryBinder queryBinder, String countQuery, QueryBinder countQueryBinder) {
        super(query, queryBinder, countQuery, countQueryBinder);
    }

    public HibernateProvider(QueryBuilder queryBuilder, QueryBuilder countQueryBuilder) {
        super(queryBuilder, countQueryBuilder);
    }

    // Hack to deal with the fact that count criteria queries return Long instead of Integer
    // since Hibernate 3.5 (just like HQL queries do since 3.0), but databinder doesn't deal
    // with that yet.
    @Override
    public int size() {
        Class objectClass = getField("objectClass");
        OrderingCriteriaBuilder criteriaBuilder = getField("criteriaBuilder");
        QueryBuilder countQueryBuilder = getField("countQueryBuilder");
        Object factoryKey = getField("factoryKey");

        Session sess = Databinder.getHibernateSession(factoryKey);

        if(countQueryBuilder != null) {
            org.hibernate.Query q = countQueryBuilder.build(sess);
            Object obj = q.uniqueResult();
            return ((Number) obj).intValue();
        }

        Criteria crit = sess.createCriteria(objectClass);

        if (criteriaBuilder != null)
            criteriaBuilder.buildUnordered(crit);
        crit.setProjection(Projections.rowCount());
        Number size = (Number) crit.uniqueResult();
        return size == null ? 0 : size.intValue();
    }

    private <FieldType> FieldType getField(String name) {
        try {
            Field field = net.databinder.models.hib.HibernateProvider.class.getDeclaredField(name);
            field.setAccessible(true);
            return (FieldType) field.get(this);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
