import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Showroom {
    private List<String> cars = new ArrayList<>();
    private List<String> customers = new ArrayList<>();
    private int carsCounter = 0;
    private int customersCounter = 0;

    Lock lock = new ReentrantLock(true);

    final int MANUFACTURING_TIME = 3000;
    final int NUMBER_OF_PRODUCTION = 10;

    public void produce() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                lock.lock();
                carsCounter += 1;
                String car = "Новый автомобиль " + carsCounter;
                cars.add(car);
                System.out.println("Toyota произвела " + car);
                System.out.println(cars);
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
            this.sellCar();

            if (carsCounter == NUMBER_OF_PRODUCTION)
                Thread.currentThread().interrupt();
        }
    }

    public void sellCar() {
        try {
            lock.lock();
            if (this.hasCar()) {
                System.out.println(cars.remove(0) + " забрал довольный " + customers.remove(0));
            } else {
                System.out.println("Недовольный " + customers.remove(0) + " ушел домой без авто");
            }
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