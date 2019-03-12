package jpa_ex1;

import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

//@NamedQuery(query = "Select e from Employee e where e.eid = :id", name = "find employee by id")
public class JPA_ex1 {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA_ex1PU");
    static Scanner sc = new Scanner(System.in);
    static boolean loop = true;
 
    public static void main(String[] args) {
       
        while (loop) {
            switchChoice(menu());
        }
    }

    private static int menu() {

        System.out.println("\n**********Menu**********");
        System.out.println(" 1. Add person");
        System.out.println(" 2. List all person");
        System.out.println(" 3. Find person by ID");
        System.out.println(" 4. Find person by name");
        System.out.println(" 5. Update city");
        System.out.println(" 6. Remove person by ID");
        System.out.println(" 7. Max age of all persons");
        System.out.println(" 8. Min age of all persons");
        System.out.println(" 9. Number of persons");
        System.out.println("10. List all persons that are between 20 and 50 years old ");
        System.out.println("11. List all persons in alfabetic order");
        System.out.println(" 0. to exit program");
        System.out.println("\nMake your choice!");

        int choice = sc.nextInt();
        sc.nextLine();

        return choice;
    }

    private static void switchChoice(int choice) {

        switch (choice) {
            case 1:
                addPerson();
                break;
            case 2:
                listAllPerson();
                break;
            case 3:
                findPersonById();
                break;
            case 4:
                findPersonByName();
                break;
            case 5:
                updateCity();
                break;
            case 6:
                removePersonById();
                break;
            case 7:
                maxAge();
                break;
            case 8:
                minAge();
                break;
            case 9:
                numberOfPersons();
                break;
            case 10:
                listAllBetween20And50YearsOld();
                break;
            case 11:
                listAllPersonInAlfabeticOrder();
                break;
            case 0:
                loop = false;
                emf.close();
                sc.close();
                System.out.println("Program exist!");

        }
    }

    private static void updateCity() {
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        System.out.print("Enter ID that you want to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        Person3 x = em.find(Person3.class, id);
        System.out.println(x);

        System.out.print("Enter new city: ");
        String newCity = sc.nextLine();
        x.setCity(newCity);

        em.getTransaction().commit();
        em.close();

        System.out.println("After uppdate");
        System.out.println(x);

    }

    private static void addPerson() {
        EntityManager em = emf.createEntityManager();
        boolean loop = true;
        do {
            em.getTransaction().begin();
            System.out.println("Enter name");
            String name = sc.nextLine();

            System.out.println("Enter city");
            String city = sc.nextLine();

            System.out.println("Enter age");
            int age = sc.nextInt();
            sc.nextLine();

            Person3 p = new Person3(name, city, age);
            em.persist(p);

            em.getTransaction().commit();

            System.out.println("Do you want to add more person?(yes or no)");
            String choice = sc.nextLine();

            if (choice.equalsIgnoreCase("yes")) {
                loop = true;
            } else {
                loop = false;
            }
        } while (loop);
        em.close();
    }

    private static void findPersonById() {
        EntityManager em = emf.createEntityManager();
        boolean loop = true;      

        do {
            System.out.print("Enter ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            Person3 x = em.find(Person3.class, id);
            if (x != null) {
                System.out.println(x);
                em.close();
                loop = false;
            } else {
                System.out.println("No suck ID");
                System.out.println("Try again!");
            }

        } while (loop);
    }

    private static void findPersonByName() {
        EntityManager em = emf.createEntityManager();

        boolean loop = true;
     
        do {
            System.out.print("Enter name: ");
            String name1 = sc.nextLine();
            List<Person3> list = (List<Person3>) em.createQuery("Select e from Person3 e where e.name like :name", Person3.class)
                    .setParameter("name", name1).getResultList();
         
                if(list.size()!=0){
                System.out.println(list);
              
                em.close();
               loop = false;
            }else{
                System.out.println("No such name");
                System.out.println("Try again!");
           }

        } while (loop);
    }

    private static void listAllPerson() {
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        List<Person3> p = em.createQuery("Select e from Person3 e", Person3.class).getResultList();
        for (Person3 person3 : p) {
            System.out.println(person3);
        }
        em.getTransaction().commit();
        em.close();
    }

    private static void removePersonById() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        boolean loop = true;
       

        do {
            System.out.print("\nEnter ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            Person3 x = em.find(Person3.class, id);
            if (x != null) {
                System.out.println("\nPerson with id " + id + " is:");
                System.out.println(x);
                em.remove(em.merge(x));
                
                em.close();
                System.out.println("\nAfter remove:");
                listAllPerson();
                loop = false;
            } else {
                System.out.println("\nNo such user");  
                System.out.println("Try again");
            }

        } while (loop);

    }

    private static void maxAge() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("Select MAX(e.age) from Person3 e");
        int result = (int) query.getSingleResult();
        System.out.println("Oldest Person is: " + result + " years old");
        em.close();
    }

    private static void minAge() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("Select MIN(e.age) from Person3 e");
        int result = (int) query.getSingleResult();
        System.out.println("Youngest person is:  " + result + " years old");
        em.close();
    }

    private static void numberOfPersons() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("Select COUNT(e) from Person3 e");
        long result = (long) query.getSingleResult();
        System.out.println("The number of persons is: " + result);
        em.close();
    }

    private static void listAllBetween20And50YearsOld() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("Select e from Person3 e where e.age Between 20 and 50");

        List<Person3> list = (List<Person3>) query.getResultList();

        for (Person3 e : list) {
            System.out.print("Person ID: " + e.getId());
            System.out.println("\t age: " + e.getAge());
            System.out.println("\t\t name: " + e.getName());

        }
    }

    private static void listAllPersonInAlfabeticOrder() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("Select e from Person3 e ORDER BY e.name ASC");

        List<Person3> list = (List<Person3>) query.getResultList();

        for (Person3 e : list) {
            System.out.println("Person Name: " + e.getName() + ", ID: " + e.getId());
        }
    }

}
