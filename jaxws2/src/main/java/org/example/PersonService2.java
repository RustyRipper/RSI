package org.example;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.jaxws.server_topdown.PersonExistsEx_Exception;
import org.example.jaxws.server_topdown.PersonNotFoundEx_Exception;

import java.util.List;

@WebService
public interface PersonService2 {
    @WebMethod
    org.example.jaxws.server_topdown.Person getPerson(int id) throws PersonNotFoundEx_Exception;

    @WebMethod
    org.example.jaxws.server_topdown.Person updatePerson(int id, String name, int age) throws
            PersonNotFoundEx_Exception;

    @WebMethod
    boolean deletePerson(int id) throws PersonNotFoundEx_Exception;

    @WebMethod
    org.example.jaxws.server_topdown.Person addPerson(int id, String name, int age) throws PersonExistsEx_Exception;

    @WebMethod
    int countPersons();

    @WebMethod
    List<org.example.jaxws.server_topdown.Person> getAllPersons();
}