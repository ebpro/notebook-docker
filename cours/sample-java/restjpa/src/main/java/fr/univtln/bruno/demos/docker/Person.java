package fr.univtln.bruno.demos.docker;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQuery(name = "person.findall",query = "select p from Person p")
public class Person {

    @Id
    @GeneratedValue
    private long id;

    private String email;
    private String firstname;
    private String lastname;
}
