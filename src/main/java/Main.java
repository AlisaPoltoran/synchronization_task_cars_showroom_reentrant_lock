public class Main {
    public static void main(String[] args) {
        Showroom showroom = new Showroom();
        Runnable producer = showroom::produce;
        Runnable customer = showroom::setCustomers;
        Runnable customer2 = showroom::setCustomers;

        Thread thread1 = new Thread(producer);
        Thread thread2 = new Thread(customer);
        Thread thread3 = new Thread(customer2);

        thread1.start();
        thread2.start();
        thread3.start();

    }
}