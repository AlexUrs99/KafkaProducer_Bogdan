package com.example.kafkademo.generator;

import com.example.kafkademo.person.model.Person;
import com.example.kafkademo.person.model.dto.PersonDto;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonGenerator {

    public static final String FAST_SUPPLIER = "FAST_SUPPLIER";
    public static final String SLOW_SUPPLIER = "SLOW_SUPPLIER";

    public static List<Person> generatePersons(int numberOfElements, String chosenSupplier) {
        Supplier<Person> supplier = getSupplier(chosenSupplier);

        System.out.println("GENERATING " + numberOfElements + " PERSONS.....");

        List<Person> listOfPersons = Stream.generate(supplier)
                .limit(numberOfElements)
                .collect(Collectors.toList());

        return listOfPersons;
    }

    private static Supplier<Person> getSupplier(String supplier) {
        if (supplier.equals(FAST_SUPPLIER)) {
            return PersonGenerator::newPersonUglyButFast;
        }

        return PersonGenerator::newPersonUncleBob;
    }

    private static Person newPersonUncleBob() {
        PersonDto personDto = generateFighterData();

        return Person.builder()
                .id(personDto.getId())
                .age(personDto.getAge())
                .firstName(personDto.getFirstName())
                .lastName(personDto.getLastName())
                .build();
    }

    private static Person newPersonUglyButFast() {
        return Person.builder()
                .id(generateRandomLong())
                .age(generateRandomBoundedInt(40))
                .firstName(generateRandomString())
                .lastName(generateRandomString())
                .build();
    }

    public static long generateRandomLong() {
        return new Random().nextInt(1999);
    }

    public static int generateRandomBoundedInt(int upperBound) {
        return new Random().nextInt(upperBound) + 18;
    }

    public static String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static PersonDto generateFighterData() {
        Faker faker = new Faker();

        Long id = generateRandomLong();
        int age = generateRandomBoundedInt(30);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        PersonDto personDto = PersonDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .build();

        return personDto;
    }
}
