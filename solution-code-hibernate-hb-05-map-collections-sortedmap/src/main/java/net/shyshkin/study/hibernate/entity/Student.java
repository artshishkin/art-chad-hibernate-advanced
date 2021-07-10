package net.shyshkin.study.hibernate.entity;

import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import java.util.*;

@Entity(name = "student")
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @ElementCollection
    @CollectionTable(name = "image")
    @MapKeyColumn(name = "file_name")
    @Column(name = "image_description")
//    @OrderBy //select ... order by image_description asc
//    @OrderBy("file_name") //select ... order by file_name asc
    @SortComparator(ReverseStringComparator.class)
    private SortedMap<String, String> images = new TreeMap<>(); //will be replaced with PersistentSortedMap when retrieving

    public Student() {
    }

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(SortedMap<String, String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static class ReverseStringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            String o1Reversed = new StringBuilder(o1).reverse().toString();
            String o2Reversed = new StringBuilder(o2).reverse().toString();
            return o1Reversed.compareTo(o2Reversed);
        }
    }

}
