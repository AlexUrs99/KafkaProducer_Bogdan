package com.example.kafkademo.person.controller;

import com.example.kafkademo.generator.PersonGenerator;
import com.example.kafkademo.person.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private KafkaTemplate<String, Person> template;

    private static final String TOPIC = "persons";

    @PostMapping("/populate")
    public void sendListOfPersons() {
        List<Person> listOfPersons = PersonGenerator.generatePersons(10000, PersonGenerator.FAST_SUPPLIER);

        for (Person person : listOfPersons) {
            template.send(TOPIC, person);
        }

        System.out.println("SENT A REQUEST ON " + TOPIC + " TOPIC WITH " + listOfPersons.size() + " PERSONS!");
//        System.out.println(listOfPersons.stream().map(p -> p.getId()).distinct().count());
    }

    @PostMapping("/add")
    public void sendPerson() {
        Person person = new Person(1L, "Ille", "Dragos", 26);
        template.send(TOPIC, person);

        System.out.println("SENT A REQUEST ON " + TOPIC + " TOPIC WITH DRAGOS!");
    }


}
