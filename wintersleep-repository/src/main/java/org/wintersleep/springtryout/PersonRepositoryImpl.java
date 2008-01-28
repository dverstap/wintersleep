package org.wintersleep.springtryout;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.SessionFactory;

@Repository
public class PersonRepositoryImpl extends RepositoryImpl<Person, Long> implements PersonRepository {


/*
    public PersonRepositoryImpl() {
    }
*/

    @Autowired
    public PersonRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    

}
