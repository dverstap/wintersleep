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
import org.springframework.test.AbstractTransactionalSpringContextTests;

import java.util.List;

public class RepositoryTest extends AbstractTransactionalSpringContextTests {

    private TestDataSource dataSource;
    private PersonRepository personRepository;

    private static final int COUNT = 10 * 1000;

    public void setDataSource(TestDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    protected String getConfigPath() {
        return "/testApplicationContext.xml";
    }

    protected void onSetUpInTransaction() throws Exception {
        dataSource.deleteAllData();
    }

    public void testFindById() {
        Person person = personRepository.findById(1L);
        assertNull(person);
    }

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

    public void testFindAll() {
        List<Person> persons = personRepository.findAll();
        assertEquals(0, persons.size());
    }

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
