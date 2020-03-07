import entity.Person;
import entity.dao.PersonDao;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class Application {

  private static SessionFactory factory;
  private static Scanner scanner;
  private static PersonDao personDao;

  public static void main(String[] args) {

    try {
      factory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      System.err.println("Failed to create sessionFactory object." + ex);
      throw new ExceptionInInitializerError(ex);
    }

    scanner = new Scanner(System.in);
    Application app = new Application();
    personDao = new PersonDao(factory);
    app.createUser(app);

    app.findAllPersons();

    app.updateUser(app);
  }

  private void findAllPersons() {
    List<Person> persons = personDao.findAllPersons();

    for (Person person : persons) {
      System.out.print("First Name: " + person.getFirstName());
      System.out.print("  Last Name: " + person.getLastName());
      System.out.println("  age: " + person.getAge());
    }
  }

  private void updateUser(Application app) {
    String firstName;
    String lastName;
    int age;
    scanner.nextLine();
    System.out.println("firstName = ");
    firstName = scanner.nextLine();

    System.out.println("lastName = ");
    lastName = scanner.nextLine();

    List<Person> people = personDao.findByFirstNameAndLastName(firstName, lastName);

    System.out.println("age for update = ");
    age = scanner.nextInt();

    for (Person person : people) {
      person.setAge(age);
      personDao.saveOrUpdate(person);
    }
  }

  private void createUser(Application app) {
    String firstName;
    String lastName;
    int age;

    System.out.println("firstName = ");
    firstName = scanner.nextLine();

    System.out.println("lastName = ");
    lastName = scanner.nextLine();

    System.out.println("age = ");
    age = scanner.nextInt();

    personDao.addPerson(firstName, lastName, age);
  }
}
