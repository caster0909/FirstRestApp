package ru.grishman.springcourse.FirstRestApp.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.grishman.springcourse.FirstRestApp.dto.PersonDTO;
import ru.grishman.springcourse.FirstRestApp.models.Person;
import ru.grishman.springcourse.FirstRestApp.services.PeopleService;
import ru.grishman.springcourse.FirstRestApp.util.PersonErrorResponce;
import ru.grishman.springcourse.FirstRestApp.util.PersonNotCreatedException;
import ru.grishman.springcourse.FirstRestApp.util.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
//@AllArgsConstructor //Аннотация из библиотеки lombok, позволяющая внедрять ч/з конструктор без конструктора
public class PeopleController {

    private final PeopleService peopleService;
    private final ModelMapper modelMapper;
    @Autowired
    public PeopleController(PeopleService peopleService,
                            ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList()); //Jackson втоматически конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return convertToPersonDTO(peopleService.findOne(id)); //Jackson конвертирует в JSON
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create (@RequestBody @Valid PersonDTO personDTO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
           List<FieldError> errors =  bindingResult.getFieldErrors();
           for (FieldError error : errors) {
               errorMsg.append(error.getField())
                       .append(" - ").append(error.getDefaultMessage())
                       .append(";");
           }

           throw new PersonNotCreatedException(errorMsg.toString());

        }
        peopleService.save(convertToPerson(personDTO));

        // отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponce> handleException(PersonNotFoundException e) {
        PersonErrorResponce responce = new PersonErrorResponce(
                "Person with this id wasn't found",
                System.currentTimeMillis()
        );
        // В НТТР ответе тело ответа  (responce) и статус в заголовке
        return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponce> handleException(PersonNotCreatedException e) {
        PersonErrorResponce responce = new PersonErrorResponce(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В НТТР ответе тело ответа  (responce) и статус в заголовке
        return new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

}
