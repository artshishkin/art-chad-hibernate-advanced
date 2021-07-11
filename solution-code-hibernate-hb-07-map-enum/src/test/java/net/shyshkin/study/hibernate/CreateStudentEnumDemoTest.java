package net.shyshkin.study.hibernate;

import net.shyshkin.study.hibernate.entity.Status;
import net.shyshkin.study.hibernate.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateStudentEnumDemoTest {

    private static Long activeStudentId = null;
    private static Long inactiveStudentId = null;

    @Test
    @Order(1)
    void persistStudents() {

        Transaction transaction = null;

        // create session factory
        // create session
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {

            // create the object
            Student studentActive = new Student("Art", "Shyshkin", "art@example.com", Status.ACTIVE);
            Student studentInactive = new Student("Kate", "Shyshkina", "kate@example.com", Status.INACTIVE);

            // start a transaction
            transaction = session.beginTransaction();

            // save the object
            System.out.println("Saving the students");
            session.persist(studentActive);
            System.out.println("Saved student: " + studentActive);
            activeStudentId = studentActive.getId();

            session.persist(studentInactive);
            System.out.println("Saved student: " + studentInactive);
            inactiveStudentId = studentInactive.getId();

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            // clean up code
            if (transaction != null) transaction.rollback();
        }
        assertThat(activeStudentId).isNotNull();
        assertThat(inactiveStudentId).isNotNull();
    }

    @Test
    @Order(2)
    void retrieveStudents() {

        Transaction transaction = null;
        Student activeStudent = null;
        Student inactiveStudent = null;

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

            activeStudent = session.get(Student.class, activeStudentId);
            System.out.println("Retrieved student: " + activeStudent);
            inactiveStudent = session.get(Student.class, inactiveStudentId);
            System.out.println("Retrieved student: " + inactiveStudent);

            // commit the transaction
            transaction.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            // clean up code
            if (transaction != null) transaction.rollback();
        }

        assertThat(activeStudent)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("status", Status.ACTIVE);
        assertThat(inactiveStudent)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("status", Status.INACTIVE);
    }
}
