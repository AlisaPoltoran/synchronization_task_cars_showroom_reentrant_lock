import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.spi.AbstractResourceBundleProvider;

public class Showroom {
    private List<String> cars = new ArrayList<>();
    private List<String> customers = new ArrayList<>();
    private int carsCounter = 0;
    private int customersCounter = 0;

    Lock lock = new ReentrantLock(true);
    Condition condition = lock.newCondition();

    final int MANUFACTURING_TIME = 3000;
    final int NUMBER_OF_PRODUCTION = 10;
    final int CUSTOMERS_PATIENCE = 3500;

    public void produce() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                lock.lock();
                carsCounter += 1;
                String car = "Новый автомобиль " + carsCounter;
                cars.add(car);
                System.out.println("+++++ Toyota произвела " + car);
                System.out.println(cars);
                condition.signalAll();
                Thread.sleep(MANUFACTURING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            if (carsCounter == NUMBER_OF_PRODUCTION)
                Thread.currentThread().interrupt();
        }
        System.out.println("Производство окончено!");
    }

    public void setCustomers() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                lock.lock();
                customersCounter += 1;
                String customer = "Покупатель " + customersCounter;
                customers.add(customer);
                System.out.println("В шоу рум зашел " + customer);
                System.out.println(customers);
            } finally {
                lock.unlock();
            }
            if (carsCounter == NUMBER_OF_PRODUCTION)
                Thread.currentThread().interrupt();

            this.sellCar();
        }
    }

    public void sellCar() {
        try {
            lock.lock();
            if (this.hasCar()) {
                System.out.println("------" + cars.remove(0) + " забрал довольный " + customers.remove(0));
            } else if (carsCounter < NUMBER_OF_PRODUCTION){
                condition.await();
                System.out.println("..... " + customers.remove(0) + " ждет авто");
            } else {
                System.out.println("Производство окончено и грустный " + customers.remove(0) + " ушел домой");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public boolean hasCar() {
        try {
            lock.lock();
            if (cars.isEmpty()) {
                System.out.println("Машин нет");
                return false;
            } else {
                return true;
            }
        } finally {
            lock.unlock();
        }
    }
}