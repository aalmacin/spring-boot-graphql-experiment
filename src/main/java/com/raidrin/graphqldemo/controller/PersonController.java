package com.raidrin.graphqldemo.controller;

import com.raidrin.graphqldemo.entity.Person;
import com.raidrin.graphqldemo.entity.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository repository;

    @PostMapping("/addPerson")
    public String addPerson(@RequestBody List<Person> personList) {
        repository.saveAll(personList);
        return "record inserted " + personList.size();
    }

    @GetMapping("/findAllPerson")
    public List<Person> getPersons() {
        return (List<Person>) repository.findAll();
    }
}
