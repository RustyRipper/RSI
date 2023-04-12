package org.example;

import java.util.List;

public interface PersonRepository2 {
    List<org.example.jaxws.server_topdown.Person> getAllPersons();

    org.example.jaxws.server_topdown.Person getPerson(int id) throws org.example.jaxws.server_topdown.PersonNotFoundEx_Exception;

    org.example.jaxws.server_topdown.Person updatePerson(int id, String name, int age) throws
            org.example.jaxws.server_topdown.PersonNotFoundEx_Exception;

    boolean deletePerson(int id) throws org.example.jaxws.server_topdown.PersonNotFoundEx_Exception;

    org.example.jaxws.server_topdown.Person addPerson(int id, String name, int age) throws org.example.jaxws.server_topdown.PersonExistsEx_Exception;

    int countPersons();
}
