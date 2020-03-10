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
            //se creaza conexiunea la baza de date
            //configurarea este in resources/hibernate.cfg.xml
            //baza de date un postgres iar tabelele sunt create automat de catre hibernate
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Application app = new Application();

        //inserare in baza de date
        app.addPerson("Nicolae", "Vieru", 33);

        //afisarea tuturor utilizatorilor din baza de date
        app.findAllPersons();

    }

    public Integer addPerson(String fname, String lname, int age) {
        //se deschide o sesiune
        // sesiunea reprezinta conversatie intre aplicatie si baza de date
        Session session = factory.openSession();
        Transaction tx = null;
        Integer personId = null;

        try {
            //se deschide o tranzatie
            //tranzatia reprezinta unitatea de lucru in care se fac operatiile.
            // Daca o tranzactie arunca o exceptie toate modificarile realizate in interiorul tranzatiei sunt aruncate.
            tx = session.beginTransaction();
            Person person = new Person(fname, lname, age);

            //se face salvarea
            //datele nu sunt inca trimise catre baza de date. Datele sunt inca in cache-ul hibernate
            personId = (Integer) session.save(person);
            //se face excutarea comenzii de salvare pe baza de date
            //aici baza de date poate arunca exceptii iar modificarile facute in metoda nu sunt facute
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            //se inchide sesiunea
            session.close();
        }
        return personId;
    }

    public void findAllPersons() {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            //se poate vedea ca nu mai trebuie sa facem la mana maparea pe obiect
            // aceasta este facuta automat de hibernate folosind adnotarile de pe clasa Person
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
