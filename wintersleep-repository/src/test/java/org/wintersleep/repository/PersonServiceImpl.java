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

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.hibernate.SessionFactory;
import org.hibernate.FlushMode;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private SessionFactory sessionFactory;
    private PersonRepository personRepository;


    @Autowired
    public PersonServiceImpl(SessionFactory sessionFactory, PersonRepository personRepository) {
        this.sessionFactory = sessionFactory;
        this.personRepository = personRepository;
    }

    public Person find(Long id) {
        System.out.println(personRepository.getClass());
        return personRepository.findById(id);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public FlushMode testContextualSession() {
        return sessionFactory.getCurrentSession().getFlushMode();
    }


}