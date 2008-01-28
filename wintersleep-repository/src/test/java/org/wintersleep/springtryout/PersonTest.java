package org.wintersleep.springtryout;

import org.springframework.test.AbstractTransactionalSpringContextTests;

import java.util.List;

public class PersonTest extends AbstractTransactionalSpringContextTests {

    private PersonRepository personRepository;

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    protected String getConfigPath() {
        return "/testApplicationContext.xml";
    }

    public void testFindById() {
        Person person = personRepository.findById(1L);
        assertNull(person);
    }

    public void testFindAll() {
        List<Person> persons = personRepository.findAll();
        assertEquals(0, persons.size());
    }

}
