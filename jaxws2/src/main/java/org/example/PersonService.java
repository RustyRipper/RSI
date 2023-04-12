package org.example;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface PersonService {
    @WebMethod
    Person getPerson(int id) throws PersonNotFoundEx;

    @WebMethod
    Person updatePerson(int id, String name, int age) throws
            PersonNotFoundEx;

    @WebMethod
    boolean deletePerson(int id) throws PersonNotFoundEx;

    @WebMethod
    Person addPerson(int id, String name, int age) throws PersonExistsEx;

    @WebMethod
    int countPersons();

    @WebMethod
    List<Person> getAllPersons();
}