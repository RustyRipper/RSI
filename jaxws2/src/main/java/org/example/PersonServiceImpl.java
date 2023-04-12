package org.example;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.List;

@WebService(serviceName = "PersonService",
        endpointInterface = "org.example.PersonService")
public class PersonServiceImpl implements PersonService {
    private PersonRepository dataRepository = new PersonRepositoryImpl();

    @WebMethod
    public Person getPerson(int id) throws PersonNotFoundEx {
        System.out.println("...called getPerson id=" + id);
        return dataRepository.getPerson(id);
    }

    @WebMethod
    public Person updatePerson(int id, String name, int age) throws
            PersonNotFoundEx {
        System.out.println("...called updatePerson");
        return dataRepository.updatePerson(id, name, age);
    }

    @WebMethod
    public boolean deletePerson(int id) throws PersonNotFoundEx {
        System.out.println("delete person");
        return dataRepository.deletePerson(id);

    }

    @WebMethod
    public Person addPerson(int id, String name, int age) throws PersonExistsEx {
        System.out.println("add person");
        return dataRepository.addPerson(id, name, age);
    }

    @WebMethod
    public int countPersons() {
        System.out.println("count person");
        return dataRepository.countPersons();
    }

    @WebMethod
    public List<Person> getAllPersons() {
        System.out.println("get all persons");
        return dataRepository.getAllPersons();
    }
}
