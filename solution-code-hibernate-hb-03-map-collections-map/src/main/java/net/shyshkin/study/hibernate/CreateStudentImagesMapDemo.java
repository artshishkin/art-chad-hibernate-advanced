package net.shyshkin.study.hibernate;

import net.shyshkin.study.hibernate.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Map;

/**
 * Hello world!
 */
public class CreateStudentImagesMapDemo {
    public static void main(String[] args) {

        Transaction transaction = null;

        // create session factory

        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // create the object
            Student student1 = new Student("Art", "Shyshkin", "art@example.com");
            student1.getImages().putAll(Map.of(
                    "image01.jpg", "Image 01 Description",
                    "image02.jpg", "Image 02 Description",
                    "image03.jpg", "Image 03 Description",
                    "image04.jpg", "Image 04 Description")
            );

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Saving the students and images");
            session.persist(student1);

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            // clean up code
            if (transaction != null) transaction.rollback();
        }

    }
}
