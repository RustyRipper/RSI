package org.example;

import org.example.jaxws.server_topdown.*;

import java.net.MalformedURLException;
import java.net.URL;


public class ESClient {
    public static void main(String[] args) throws MalformedURLException,
            PersonNotFoundEx_Exception {
        int num = -1;
        URL addr = new URL("http://localhost:8081/personservice?wsdl");
        PersonService_Service pService = new PersonService_Service();
        //PersonService_Service pService = new PersonService_Service();
        org.example.jaxws.server_topdown.PersonService pServiceProxy = pService.getPersonServiceImplPort();
        num = pServiceProxy.countPersons();
        System.out.println("Num of Persons = " + num);
        org.example.jaxws.server_topdown.Person person = pServiceProxy.getPerson(2);
        System.out.println("Person " + person.getFirstName() + ", id = " + person.getId());
    }
}
