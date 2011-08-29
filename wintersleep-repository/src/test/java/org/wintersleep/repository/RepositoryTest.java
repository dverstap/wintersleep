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

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.wintersleep.util.spring.tracer.CallTreeExecutionListener;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/testApplicationContext.xml"})
@Transactional
public class RepositoryTest {

    @Autowired
    private TestDataSource dataSource;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CallTreeExecutionListener performanceLogger;

    private static final int COUNT = 10;

    @BeforeTransaction
    public void startPerformanceLogger() throws Exception {
        performanceLogger.start("RepositoryTest");
    }

    @Before
    public void onSetUpInTransaction() throws Exception {
        dataSource.deleteAllData();
    }

    @AfterTransaction
    public void stopPerformanceLogger() throws Exception {
        performanceLogger.stop();
    }

    @Test
    public void testFindById() {
        Person person = personRepository.findById(1L);
        assertNull(person);
    }

    @Test
    public void testLoadById() {
        Person person = personRepository.loadById(1L, false);
        assertNotNull(person);
        try {
            person.getName();
            fail();
        } catch (ObjectNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testFindAll() {
        List<Person> persons = personRepository.findAll();
        assertEquals(0, persons.size());
    }

    @Test
    public void testInsert() {
        Person person = new Person("ikke");
        personRepository.makePersistent(person);
        assertEquals(1, personRepository.countAll());

        person = new Person("gij");
        personRepository.makePersistent(person);
        assertEquals(2, personRepository.countAll());

        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            person = new Person("jij" + i);
            personRepository.makePersistent(person);
        }
        assertEquals(COUNT + 2, personRepository.countAll());
        System.out.println(System.currentTimeMillis()-start);


    }

}
