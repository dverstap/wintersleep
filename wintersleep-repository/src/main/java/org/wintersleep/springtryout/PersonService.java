package org.wintersleep.springtryout;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person find(Long id) {
        System.out.println(personRepository.getClass());
        return personRepository.findById(id);
    }



}