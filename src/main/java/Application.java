import entity.Person;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Application {

    private static SessionFactory factory;

    public static void main(String[] args) {

        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Application app = new Application();

        app.addPerson("Nicolae", "Vieru", 33);

        app.findAllPersons();

    }

    public Integer addPerson(String fname, String lname, int age) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer personId = null;

        try {
            tx = session.beginTransaction();
            Person person = new Person(fname, lname, age);

            personId = (Integer) session.save(person);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return personId;
    }

    public void findAllPersons() {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<Person> persons = session.createQuery("FROM Person ").list();
            for (Person person : persons) {
                System.out.print("First Name: " + person.getFirstName());
                System.out.print("  Last Name: " + person.getLastName());
                System.out.println("  Salary: " + person.getAge());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
