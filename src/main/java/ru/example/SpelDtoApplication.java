package ru.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.example.model.Address;
import ru.example.model.Person;
import ru.example.repository.PersonRepository;

import java.time.LocalDate;

@SpringBootApplication
public class SpelDtoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpelDtoApplication.class, args);
    }

    @Bean
    CommandLineRunner init(PersonRepository repo) {
        return args -> {
            repo.save(new Person("Ivan", "Ivanov", LocalDate.of(1985, 3, 10),
                    new Address("Moscow", "Tverskaya")));
            repo.save(new Person("Sofia", "Petrova", LocalDate.of(1992, 11, 2),
                    new Address("Saint Petersburg", "Nevsky")));
        };
    }
}
