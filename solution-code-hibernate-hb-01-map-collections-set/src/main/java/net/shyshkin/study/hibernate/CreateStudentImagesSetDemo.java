package net.shyshkin.study.hibernate;

import net.shyshkin.study.hibernate.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Set;

/**
 * Hello world!
 */
public class CreateStudentImagesSetDemo {
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
//            Student student = new Student("Art", "Shyshkin", "art@example.com");
            Student student = new Student("Kate", "Shyshkina", "kate@example.com");
            student.getImages().addAll(List.of("image01.jpg", "image02.jpg", "image03.jpg", "image04.jpg", "image04.jpg"));

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Saving the student and images");
            session.persist(student);

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            // clean up code
            if (transaction != null) transaction.rollback();
        }

    }
}
