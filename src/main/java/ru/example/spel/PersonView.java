package ru.example.spel;

import org.springframework.beans.factory.annotation.Value;

public interface PersonView {
    Long getId();

    // соединяет имя и фамилию
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();

    // извлекает количество лет между датами
    @Value("#{T(java.time.Period).between(target.birthDate, T(java.time.LocalDate).now()).getYears()}")
    Integer getAge();

    // проверка на null через тернарный оператор для адреса + доступ к вложенному полю адреса через target.address.city
    @Value("#{target.address != null ? target.address.city : null}")
    String getCity();

}
