package fr.univtln.bruno.demos.docker;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("persons")
public class PersonResource {
    private static List<Person> persons = List.of(
            Person.builder().email("a.b@x.fr").firstname("a").lastname("b").build(),
                    Person.builder().email("c.d@x.fr").firstname("c").lastname("d").build(),
                    Person.builder().email("e.f@x.fr").firstname("e").lastname("f").build()
    );

    @GET
    public List<Person> findAll() {
        EntityManager em = Main.emf.createEntityManager();
        List<Person> persons = em.createNamedQuery("person.findall",Person.class).getResultList();
        return persons;
    }
}
