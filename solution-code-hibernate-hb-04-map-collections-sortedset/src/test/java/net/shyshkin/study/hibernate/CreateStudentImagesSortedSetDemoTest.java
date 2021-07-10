package net.shyshkin.study.hibernate;

import com.github.javafaker.Faker;
import net.shyshkin.study.hibernate.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateStudentImagesSortedSetDemoTest {

    private static Long studentId = null;

    private static final Faker FAKER = Faker.instance(Locale.US);

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
            Student student = new Student(
                    FAKER.name().firstName(),
                    FAKER.name().lastName(),
                    FAKER.bothify("????##@gmail.com"));
            student.getImages().addAll(List.of("image01.jpg", "image02.jpg", "image03.jpg", "image04.jpg", "image04.jpg"));

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Saving the student and images");
            session.persist(student);

            // commit the transaction
            transaction.commit();
            studentId = student.getId();
            System.out.println("Student: " + student);
            System.out.println("Images: " + student.getImages());

        } catch (Exception exception) {
            // clean up code
            if (transaction != null) transaction.rollback();
        }

        assertThat(studentId).isNotNull();
    }
}
