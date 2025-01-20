package exercises03;

/* Consider a Person class with attributes: id (long), name (String), zip (int) and address
(String). The Person class has the following functionality:
    • It must be possible to change zip and address together.
    • It is not necessary to be able to change name—but it is not forbidden.
    • The id cannot be changed.
    • It must be possible to get the values of all fields.
    • There must be a constructor for Person that takes no parameters. When calling this constructor, each new
instance of Person gets an id one higher than the previously created person. In case the constructor is used
to create the first instance of Person, then the id for that object is set to 0.
    • There must be a constructor for Person that takes as parameter the initial id value from which future ids
are generated. In case the constructor is used to create the first instance of Person, the initial parameter
must be used. For subsequent instances, the parameter must be ignored and the value of the previously
created person must be used (as stated in the previous requirement). */

public class Person {
    private final long id;
    private String name;
    private int zip;
    private String address;
    private static long idCounter = 0;

    // long idCounter = 0;

    public Person() {
        // class level lock
        synchronized (Person.class) { // Synchronized on the class to manage static counter
            this.id = idCounter++;
        }
    }

    public Person(long initialId) {
        synchronized (Person.class) {
            if (idCounter == 0) {
                idCounter = initialId;
            }
            this.id = idCounter++;
        }
    }

    public synchronized long getId() {
        return id;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized int getZip() {
        return zip;
    }

    public synchronized String getAddress() {
        return address;
    }

    // Method to change zip and address together
    public void setZipAndAddress(int zip, String address) {
        this.zip = zip;
        this.address = address;
    }

    // Optional: Method to change the name if needed
    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        int numberOfThreads = 1000;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Person person = new Person();
                    person.setName("Person-" + person.getId());
                    person.setZipAndAddress(12345, "Address-" + person.getId());
                    System.out.println("Created: " + person.getName() + " with ID: " + person.getId());
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        System.out.println("All threads have finished execution.");
    }
}