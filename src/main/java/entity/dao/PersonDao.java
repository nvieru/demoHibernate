package entity.dao;

import entity.Person;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonDao {

    SessionFactory factory;

    public PersonDao(SessionFactory factory) {
        this.factory = factory;
    }

    public void addPerson(String firstName, String lastName, int age) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Person person = new Person(firstName, lastName, age);

            session.save(person);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void saveOrUpdate(Person person) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(person);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Person> findAllPersons() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Person> persons = new ArrayList<Person>();
        try {
            tx = session.beginTransaction();
            persons = session.createQuery("FROM Person ").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return persons;
    }

    public List<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        Session session = factory.openSession();
        Transaction tx = null;

        List<Person> persons = new ArrayList<Person>();
        try {
            tx = session.beginTransaction();
            Map<String, String> values = new HashMap<String, String>();
            values.put("firstName", firstName);
            values.put("lastName", lastName);
            persons =
                    session
                            .createQuery("FROM Person WHERE firstName = :firstName AND lastName = :lastName")
                            .setProperties(values)
                            .getResultList();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return persons;
    }
}
