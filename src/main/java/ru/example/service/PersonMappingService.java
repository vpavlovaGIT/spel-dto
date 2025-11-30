package ru.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dto.PersonRequest;
import ru.example.model.Person;
import ru.example.spel.PersonView;
import ru.example.spel.SpelContext;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PersonMappingService {
    private final SpelContext spelContext;

    public PersonView mapToPersonView(Person person, PersonRequest request) {
        return new PersonView() {
            @Override
            public Long getId() {
                return person.getId();
            }

            @Override
            public String getFullName() {
                String expr = request.getSearchExpression();
                if (expr != null && !expr.isBlank()) {
                    Map<String, Object> vars = new HashMap<>();
                    vars.put("firstName", person.getFirstName());
                    vars.put("lastName", person.getLastName());
                    vars.put("city", person.getAddress().getCity());  // 🔥 Добавлено для поддержки #city в выражениях
                    // 🔥 Авто-выбор режима: template только если есть ${...}
                    if (expr.contains("${")) {
                        return spelContext.evaluateTemplateExpressionWithVariables(expr, person, vars, String.class);
                    } else {
                        return spelContext.evaluateExpressionWithVariables(expr, person, vars, String.class);
                    }
                }
                return person.getFirstName() + " " + person.getLastName();
            }

            @Override
            public Integer getAge() {
                String expr =
                        "T(java.time.Period).between(#root.birthDate, T(java.time.LocalDate).now()).getYears()";
                return spelContext.evaluateExpression(expr, person, Integer.class);
            }

            @Override
            public String getCity() {
                String expr = "#root.address != null ? #root.address.city : 'Unknown'";
                return spelContext.evaluateExpression(expr, person, String.class);
            }
        };
    }

    public boolean matchesFilters(Person person, PersonRequest request) {
        StringBuilder filterExpr = new StringBuilder();
        Map<String, Object> vars = new HashMap<>();
        Integer age = getAge(person);
        String city = getCity(person);
        vars.put("age", age);
        vars.put("city", city);
        if (request.getMinAge() != null) {
            filterExpr.append("#age >= #minAge");
            vars.put("minAge", request.getMinAge());
        }
        if (request.getMaxAge() != null) {
            if (filterExpr.length() > 0) filterExpr.append(" and ");
            filterExpr.append("#age <= #maxAge");
            vars.put("maxAge", request.getMaxAge());
        }
        if (request.getCity() != null && !request.getCity().isBlank()) {
            if (filterExpr.length() > 0) filterExpr.append(" and ");
            filterExpr.append("#city == #filterCity");
            vars.put("filterCity", request.getCity());
        }
        if (filterExpr.length() == 0) {
            return true;
        }
        return spelContext.evaluateExpressionWithVariables(
                filterExpr.toString(),
                vars,
                vars,
                Boolean.class
        );
    }

    private Integer getAge(Person p) {
        return spelContext.evaluateExpression(
                "T(java.time.Period).between(#root.birthDate, T(java.time.LocalDate).now()).getYears()",
                p,
                Integer.class
        );
    }

    private String getCity(Person p) {
        return spelContext.evaluateExpression(
                "#root.address != null ? #root.address.city : 'Unknown'",
                p,
                String.class
        );
    }
}