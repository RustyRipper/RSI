package org.example;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.jaxws.server_topdown.PersonExistsEx_Exception;
import org.example.jaxws.server_topdown.PersonNotFoundEx_Exception;
import org.example.jaxws.server_topdown.PersonService_Service;

import java.util.List;

@WebService(serviceName = "PersonService",
        endpointInterface = "org.example.jaxws.server_topdown.PersonService")
public class PersonService2Impl implements org.example.jaxws.server_topdown.PersonService {
    private PersonRepository2 dataRepository = new PersonRepositoryImpl2();

    @WebMethod
    public org.example.jaxws.server_topdown.Person getPerson(int id) throws PersonNotFoundEx_Exception {
        System.out.println("...called getPerson id=" + id);
        return dataRepository.getPerson(id);
    }

    @WebMethod
    public org.example.jaxws.server_topdown.Person updatePerson(int id, String name, int age) throws
            PersonNotFoundEx_Exception {
        System.out.println("...called updatePerson");
        return dataRepository.updatePerson(id, name, age);
    }

    @WebMethod
    public boolean deletePerson(int id) throws PersonNotFoundEx_Exception {
        System.out.println("delete person");
        return dataRepository.deletePerson(id);

    }

    @WebMethod
    public org.example.jaxws.server_topdown.Person addPerson(int id, String name, int age) throws PersonExistsEx_Exception {
        System.out.println("add person");
        return dataRepository.addPerson(id, name, age);
    }

    @WebMethod
    public int countPersons() {
        System.out.println("count person");
        return dataRepository.countPersons();
    }

    @WebMethod
    public List<org.example.jaxws.server_topdown.Person> getAllPersons() {
        System.out.println("get all persons");
        return dataRepository.getAllPersons();
    }
}
