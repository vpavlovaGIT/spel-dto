package ru.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.repository.PersonRepository;
import ru.example.spel.PersonView;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonRepository repo;

    public PersonController(PersonRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<PersonView> list() {
        return repo.findAllProjectedBy();
    }
}
