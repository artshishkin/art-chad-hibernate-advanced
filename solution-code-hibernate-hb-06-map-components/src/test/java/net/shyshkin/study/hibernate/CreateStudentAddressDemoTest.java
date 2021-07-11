package net.shyshkin.study.hibernate;


import com.github.javafaker.Faker;
import net.shyshkin.study.hibernate.entity.Address;
import net.shyshkin.study.hibernate.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateStudentAddressDemoTest {

    private static final Faker FAKER = Faker.instance(Locale.US);
    private static Address savedAddress;
    private static Long studentId = null;

    @Test
    @Order(1)
    void persistStudent() {

        Transaction transaction = null;

        // create session factory
        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // create the object
            Student student = new Student("Art", "Shyshkin", "art@example.com");
            savedAddress = new Address(
                    FAKER.address().streetAddress(),
                    FAKER.address().city(),
                    FAKER.address().zipCode()
            );
            student.setHomeAddress(savedAddress);

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Saving the student and address");
            session.persist(student);
            System.out.println("Saved student: " + student);
            studentId = student.getId();

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            // clean up code
            if (transaction != null) transaction.rollback();
        }
    }

    @Test
    @Order(2)
    void retrieveStudent() {

        Transaction transaction = null;
        Student student = null;

        // create session factory
        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate-update.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // start a transaction
            transaction = session.beginTransaction();

            // retrieve the object
            System.out.println("Retrieving the student and address");

            student = session.get(Student.class, studentId);
            System.out.println("Retrieved student: " + student);

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
                .satisfies(st -> assertThat(st.getHomeAddress()).isEqualTo(savedAddress));
    }
}
