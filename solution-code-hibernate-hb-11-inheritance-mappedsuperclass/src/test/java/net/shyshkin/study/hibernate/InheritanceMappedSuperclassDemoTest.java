package net.shyshkin.study.hibernate;

import net.shyshkin.study.hibernate.entity.Instructor;
import net.shyshkin.study.hibernate.entity.Student;
import net.shyshkin.study.hibernate.entity.User;
import org.assertj.core.api.ThrowableAssert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.UnknownEntityTypeException;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InheritanceMappedSuperclassDemoTest {

    private static Long studentId = null;
    private static Long instructorId = null;

    @Test
    @Order(1)
    void persistUsers() {

        Transaction transaction = null;

        // create session factory
        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Instructor.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // create the object
            Student student = new Student("Art", "Shyshkin", "art@example.com", "Hibernate");
            Instructor instructor = new Instructor("Kate", "Shyshkina", "kate@example.com", 123.45);

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Saving the users");
            session.persist(student);
            System.out.println("Saved user: " + student);
            studentId = student.getId();

            session.persist(instructor);
            System.out.println("Saved user: " + instructor);
            instructorId = instructor.getId();

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            // clean up code
            if (transaction != null) transaction.rollback();
        }
        assertThat(studentId).isNotNull();
        assertThat(instructorId).isNotNull();
    }

    @Test
    @Order(2)
    void retrieveUsers() {

        Transaction transaction = null;
        Student student = null;
        Instructor instructor = null;

        // create session factory
        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate-update.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Instructor.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // start a transaction
            transaction = session.beginTransaction();

            // retrieve the object
            System.out.println("Retrieving users");

            student = session.get(Student.class, studentId);
            System.out.println("Retrieved user: " + student);
            instructor = session.get(Instructor.class, instructorId);
            System.out.println("Retrieved user: " + instructor);

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            // clean up code
            if (transaction != null) transaction.rollback();
        }

        assertThat(student)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("course", "Hibernate");
        assertThat(instructor)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("salary", 123.45);
    }

    @Test
    @Order(3)
    void retrieveUsers_common() {

        // create session factory
        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate-update.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Instructor.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // start a transaction
            Transaction transaction = session.beginTransaction();

            // retrieve the object
            System.out.println("Retrieving users");

            ThrowableAssert.ThrowingCallable execution = () -> {
                User student = session.get(User.class, studentId);
                System.out.println("Retrieved user: " + student);
                User instructor = session.get(User.class, instructorId);
                System.out.println("Retrieved user: " + instructor);

                // commit the transaction
                transaction.commit();

            };

            assertThatThrownBy(execution)
                    .isInstanceOf(UnknownEntityTypeException.class)
                    .hasMessageContaining("Unable to locate persister: net.shyshkin.study.hibernate.entity.User");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
