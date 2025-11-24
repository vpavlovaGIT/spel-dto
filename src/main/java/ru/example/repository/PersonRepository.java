package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.model.Person;
import ru.example.spel.PersonView;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<PersonView> findAllProjectedBy();
}
