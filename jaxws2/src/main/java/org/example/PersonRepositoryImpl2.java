package org.example;

import org.example.jaxws.server_topdown.PersonExistsEx_Exception;

import java.util.ArrayList;
import java.util.List;

public class PersonRepositoryImpl2 implements PersonRepository2 {
    private List<org.example.jaxws.server_topdown.Person> personList;
    public PersonRepositoryImpl2() {
        personList = new ArrayList<>();
        org.example.jaxws.server_topdown.Person p1 = new org.example.jaxws.server_topdown.Person();
        p1.setId(1);
        p1.setAge(9);
        p1.setFirstName("Mariusz");
        org.example.jaxws.server_topdown.Person p2 = new org.example.jaxws.server_topdown.Person();
        p2.setId(2);
        p2.setAge(10);
        p2.setFirstName("Andrew");
        org.example.jaxws.server_topdown.Person p3 = new org.example.jaxws.server_topdown.Person();
        p3.setId(3);
        p3.setAge(11);
        p3.setFirstName("Thomas");
        personList.add(p1);
        personList.add(p2);
        personList.add(p3);
    }
    public List<org.example.jaxws.server_topdown.Person> getAllPersons() {
        return personList;
    }
    public org.example.jaxws.server_topdown.Person getPerson(int id) throws org.example.jaxws.server_topdown.PersonNotFoundEx_Exception {
        for (org.example.jaxws.server_topdown.Person thePerson : personList) {
            if (thePerson.getId() == id) {
                return thePerson;
            }
        }
        //throw new org.example.jaxws.server_topdown.PersonNotFoundEx();
        return null;
    }
    public org.example.jaxws.server_topdown.Person updatePerson(int id, String name, int age) throws
            org.example.jaxws.server_topdown.PersonNotFoundEx_Exception {
        for (org.example.jaxws.server_topdown.Person thePerson : personList) {
            if (thePerson.getId() == id) {
                thePerson.setFirstName(name);
                thePerson.setAge(age);
                return thePerson;
            }
        }
        //throw new org.example.jaxws.server_topdown.PersonNotFoundEx();
        return null;
    }
    public boolean deletePerson(int id) throws org.example.jaxws.server_topdown.PersonNotFoundEx_Exception{
        for (org.example.jaxws.server_topdown.Person thePerson : personList) {
            if (thePerson.getId() == id) {
                personList.remove(thePerson);
                return true;
            }
        }
        return false;
        //throw new PersonNotFoundEx();
    }
    public org.example.jaxws.server_topdown.Person addPerson(int id, String name, int age) throws org.example.jaxws.server_topdown.PersonExistsEx_Exception {
        for (org.example.jaxws.server_topdown.Person thePerson : personList) {
            if (thePerson.getId() == id) {
                //throw new PersonExistsEx();
            }
        }
        org.example.jaxws.server_topdown.Person person = new org.example.jaxws.server_topdown.Person();
        person.setId(id);
        person.setAge(age);
        person.setFirstName(name);
        personList.add(person);
        return person;
    }
    public int countPersons() {
        return personList.size();
    }
}


