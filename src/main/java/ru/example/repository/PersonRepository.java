package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
