package com.example.spring_boot_reflection_demo.controller;

import com.example.spring_boot_reflection_demo.annotation.QController;
import com.example.spring_boot_reflection_demo.annotation.QRequestMapping;
import com.example.spring_boot_reflection_demo.dto.PersonDTO;
import com.example.spring_boot_reflection_demo.qenum.QMethod;
import com.example.spring_boot_reflection_demo.service.PersonService;

import java.util.List;

@QController
public class PersonController {
    private final PersonService personService;
    public PersonController() {
        personService = PersonService.getInstance();
    }

    @QRequestMapping(value = "/person", method = QMethod.POST)
    public PersonDTO createPerson(PersonDTO person) {
        return personService.addPerson(person);
    }

    @QRequestMapping(value = "/person", method = QMethod.GET)
    public List<PersonDTO> getAllPersons() {
        return personService.getAllPersons();
    }
}
