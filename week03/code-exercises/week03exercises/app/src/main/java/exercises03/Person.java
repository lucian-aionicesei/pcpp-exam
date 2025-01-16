package exercises03;

public class Person {
    private final long id;
    private String name;
    private int zip;
    private String address;
    private static long idCounter = 0;

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