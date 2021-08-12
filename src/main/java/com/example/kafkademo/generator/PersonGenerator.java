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
    public static final Integer LIMIT = 99999;

    public static List<Person> generatePersons(int numberOfElements, String chosenSupplier) {
        Supplier<Person> supplier = getSupplier(chosenSupplier);
        List<Long> uniqueIDs = generateUniqueIDs(numberOfElements);

        System.out.println("GENERATING " + numberOfElements + " PERSONS.....");

        List<Person> listOfPersons = Stream.generate(supplier)
                .map(person -> mapUniqueIDToPerson(person, uniqueIDs))
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

    private static Person mapUniqueIDToPerson(Person person, List<Long> uniqueIDs) {
        person.setId(uniqueIDs.get(0));
        uniqueIDs.remove(0);
        return person;
    }

    private static Person newPersonUncleBob() {
        PersonDto personDto = generateFighterData();

        return Person.builder()
                .age(personDto.getAge())
                .firstName(personDto.getFirstName())
                .lastName(personDto.getLastName())
                .build();
    }

    private static Person newPersonUglyButFast() {
        return Person.builder()
                .age(generateRandomBoundedInt(40))
                .firstName(generateRandomString())
                .lastName(generateRandomString())
                .build();
    }

    private static List<Long> generateUniqueIDs(int numberOfIDs) {
        Random random = new Random();
        List<Long> randomLongs = random.longs(1, LIMIT).distinct().limit(numberOfIDs).boxed().collect(Collectors.toList());

        return randomLongs;
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

        int age = generateRandomBoundedInt(30);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        PersonDto personDto = PersonDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .build();

        return personDto;
    }
}
