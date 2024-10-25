package com.example.spring_boot_reflection_demo.service;

import com.example.spring_boot_reflection_demo.dto.PersonDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PersonService {
    private static PersonService instance;  // Singleton instance

    private final Map<String, PersonDTO> personMap = new HashMap<>();  // In-memory data store

    // Private constructor to prevent instantiation from outside
    private PersonService() {}

    // Public method to provide access to the Singleton instance
    public static synchronized PersonService getInstance() {
        if (instance == null) {
            instance = new PersonService();
        }
        return instance;
    }

    // CRUD methods

    // Add a new person
    public PersonDTO addPerson(PersonDTO person) {
        String id = UUID.randomUUID().toString();
        person.setId(id);
        personMap.put(id, person);
        return person;
    }

    // Get a person by ID
    public PersonDTO getPerson(String id) {
        return personMap.get(id);
    }

    // Update an existing person
    public PersonDTO updatePerson(String id, PersonDTO updatedPerson) {
        if (personMap.containsKey(id)) {
            updatedPerson.setId(id);
            personMap.put(id, updatedPerson);
            return updatedPerson;
        }
        return null;
    }

    // Delete a person by ID
    public boolean deletePerson(String id) {
        return personMap.remove(id) != null;
    }

    // Get all persons
    public List<PersonDTO> getAllPersons() {
        return personMap.values().stream().toList();
    }
}
