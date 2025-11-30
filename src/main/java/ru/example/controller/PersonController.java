package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.PersonRequest;
import ru.example.model.Person;
import ru.example.repository.PersonRepository;
import ru.example.service.PersonMappingService;
import ru.example.spel.PersonView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonRepository repo;
    private final PersonMappingService mappingService;

    @GetMapping
    public List<PersonView> list(@ModelAttribute PersonRequest request) {
        List<Person> allPersons = repo.findAll();
        return allPersons.stream()
                .filter(person -> mappingService.matchesFilters(person, request))
                .map(person -> mappingService.mapToPersonView(person, request))
                .collect(Collectors.toList());
    }

    @GetMapping("/spel-demo")
    public String spelDemo(@ModelAttribute PersonRequest request) {
        // Демонстрация работы SpEL с разными выражениями
        List<Person> persons = repo.findAll();
        if (persons.isEmpty()) {
            return "No persons in database. Add some data first.";
        }
        Person samplePerson = persons.get(0);
        if (request.getSearchExpression() != null && !request.getSearchExpression().isBlank()) {
            // Работа SpEL выражения
            String result = mappingService.mapToPersonView(samplePerson, request).getFullName();
            return "SpEL Result: " + result;
        }
        return "Provide 'searchExpression' parameter to test SpEL";
    }

    @PostMapping("/search")
    public List<PersonView> searchPersons(@RequestBody PersonRequest request) {
        List<Person> allPersons = repo.findAll();
        return allPersons.stream()
                .filter(person -> mappingService.matchesFilters(person, request))
                .map(person -> mappingService.mapToPersonView(person, request))
                .collect(Collectors.toList());
    }
}