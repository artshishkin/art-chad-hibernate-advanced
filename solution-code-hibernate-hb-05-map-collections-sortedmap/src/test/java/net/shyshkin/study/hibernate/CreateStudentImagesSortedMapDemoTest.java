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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateStudentImagesSortedMapDemoTest {

    private static final Faker FAKER = Faker.instance();
    private static Long studentId;

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

            student.getImages().putAll(Map.of(
                    "image01.jpg", "Image 01 Description",
                    "image01.bmp", "Image 01 BMP Description",
                    "image01.png", "Image 01 PNG Description",
                    "image02.jpg", "Image 02 Description",
                    "image03.jpg", "Image 03 Description",
                    "image03.png", "Image 03 PNG Description",
                    "image04.jpg", "Image 04 Description"
                    )
            );

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Saving the student and images");
            session.persist(student);

            System.out.println("Student: " + student);
            System.out.println("Associated images: " + student.getImages());
            studentId = student.getId();

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            // clean up code
            if (transaction != null) transaction.rollback();
        }

        assertThat(studentId).isNotNull();
    }

    @Test
    @Order(2)
    void retrieveStudent() {

        Student student = null;
        Transaction transaction = null;

        // create session factory
        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Retrieving student and images");

            student = session.get(Student.class, studentId);

            System.out.println("Student: " + student);
            System.out.println("Associated images: " + student.getImages());

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            // clean up code
            if (transaction != null) transaction.rollback();
        }

        assertThat(student)
                .isNotNull()
                .satisfies(st -> assertThat(st.getImages().keySet())
                        .hasSize(7)
                        .containsSequence(
                                "image01.png", "image03.png",
                                "image01.jpg", "image02.jpg", "image03.jpg", "image04.jpg",
                                "image01.bmp"
                        ));
        System.out.println("Class of `images` property: " + student.getImages().getClass());
    }
}
