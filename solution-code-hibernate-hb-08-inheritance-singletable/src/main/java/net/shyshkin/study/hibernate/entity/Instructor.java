package net.shyshkin.study.hibernate.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends User {

    private Double salary;

    public Instructor() {
    }

    public Instructor(String firstName, String lastName, String email, Double salary) {
        super(firstName, lastName, email);
        this.salary = salary;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "salary=" + salary +
                "} " + super.toString();
    }
}
