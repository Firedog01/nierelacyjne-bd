package managers;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Address;
import model.Client;
import model.EQ.Camera;
import model.EQ.Equipment;
import model.Rent;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.DataFaker;
import repository.RepositoryFactory;
import repository.RepositoryType;
import repository.impl.EquipmentRepository;
import repository.impl.RentRepository;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RentManagerTest {

    static RentRepository rr;
    static RepositoryFactory rf;
    static EntityManagerFactory emf;
    static RentManager rm;


    LocalDateTime t0;
    LocalDateTime t1;
    LocalDateTime t2;
    LocalDateTime t3;
    LocalDateTime t4;
    LocalDateTime t5;
    // t0 =~ now
    // t0 < t1 < t2 < t3 < t4 < t5

    @BeforeAll
    static void beforeAll() {
        emf = Persistence.createEntityManagerFactory("POSTGRES_DB");
        rf = new RepositoryFactory(emf);
        rr = (RentRepository) rf.getRepository(RepositoryType.RentRepository);
        rm = new RentManager(rr);
    }


    RentManagerTest() {
        t0 = LocalDateTime.now();
        t1 = t0.plusDays(1);
        t2 = t0.plusDays(2);
        t3 = t0.plusDays(3);
        t4 = t0.plusDays(4);
        t5 = t0.plusDays(5);
    }

    @Test
    void makeReservation() {
        
    }
//FIXME I'm detached from reality
    @Test
    void concurrentReservationTest() throws BrokenBarrierException, InterruptedException {
        int amount = 5;

        List<RentRepository> rentRepositoryList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            rentRepositoryList.add((RentRepository) rf.getRepository(RepositoryType.RentRepository));
        }

        EquipmentRepository er = (EquipmentRepository) rf.getRepository(RepositoryType.EquipmentRepository);
        Camera camera = DataFaker.getCamera();
        er.add(camera);


        AtomicInteger atomicInteger = new AtomicInteger();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(amount);
        List<Thread> threadList = new ArrayList<Thread>();

        for (int i = 0; i < amount; i++) {
            threadList.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    RentRepository rr = (RentRepository) rf.getRepository(RepositoryType.RentRepository);

                    Rent r = new Rent(t1, t2, camera, DataFaker.getClient(), DataFaker.getAddress());
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (BrokenBarrierException ex) {
                        throw new RuntimeException(ex);
                    }
                    rr.add(r);
                    atomicInteger.getAndIncrement();
                }
            }));
        }

        threadList.forEach(Thread::start);
//        cyclicBarrier.await();
//        threadList.forEach(thread -> {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
        while(atomicInteger.get() < 5) {} // wait


        for (int i = 0; i < amount; i++) {
            List<Rent> rents = rentRepositoryList.get(i).getAll();
            assertEquals(1, rents.size());
            System.out.println(rents.get(0));
        }
    }
}