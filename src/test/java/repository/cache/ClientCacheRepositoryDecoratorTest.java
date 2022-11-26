package repository.cache;

import mgd.*;
import mgd.EQ.EquipmentMgd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.impl.ClientRepository;
import repository.impl.EquipmentRepository;
import repository.impl.RentRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ClientCacheRepositoryDecoratorTest {

    static ClientCacheRepositoryDecorator clientRepository = new ClientCacheRepositoryDecorator(new ClientRepository());

    static AddressMgd address1 = DataFakerMgd.getAddressMgd();
    static AddressMgd address2 = DataFakerMgd.getAddressMgd();
    static AddressMgd address3 = DataFakerMgd.getAddressMgd();
    static ClientMgd client1 = DataFakerMgd.getClientMgd(address1);
    static ClientMgd client2 = DataFakerMgd.getClientMgd(address2);
    static ClientMgd client3 = DataFakerMgd.getClientMgd(address3);

    @BeforeAll
    static void beforeAll() {
        clientRepository.clearCache();

        clientRepository.add(client1);
        clientRepository.add(client2);
        clientRepository.add(client3);

    }

    @Test
    void basicCheck() {
        Repository decoratedRentRepo = new ClientCacheRepositoryDecorator(new ClientRepository());
    }

    @Test
    void count() {
        int startingCount = clientRepository.getAll().size();

        ClientMgd r1 = DataFakerMgd.getClientMgd();
        ClientMgd r2 = DataFakerMgd.getClientMgd();
        ClientMgd r3 = DataFakerMgd.getClientMgd();

        clientRepository.add(r1);
        clientRepository.add(r2);
        clientRepository.add(r3);

        clientRepository.get(r1.getEntityId());

        assertEquals(startingCount + 3, clientRepository.getAll().size());

        clientRepository.remove(r1);

        assertEquals(startingCount + 2, clientRepository.getAll().size());
    }

    @Test
    void addGetDeleteTest() {
        ClientMgd r1 = DataFakerMgd.getClientMgd();
        ClientMgd r2 = DataFakerMgd.getClientMgd();
        ClientMgd r3 = DataFakerMgd.getClientMgd();

        clientRepository.add(r1);
        clientRepository.add(r2);
        clientRepository.add(r3);
        //FIXME Są jakieś problemy z redisem i tym beforeAll, czasem to działa, czasem nie


        assertEquals(clientRepository.getFromMongo(r1.getEntityId()),
                clientRepository.getFromRedis(r1.getEntityId()));

        clientRepository.remove(r3);

        assertThrows(IllegalArgumentException.class, () -> clientRepository.getFromRedis(r3.getEntityId()));
        assertNull(clientRepository.getFromMongo(r3.getEntityId()));
        // Nie pamiętam czy tak powinno być, że to zwraca null

        clientRepository.removeFromRedis(r2);

        assertThrows(IllegalArgumentException.class, () -> clientRepository.getFromRedis(r2.getEntityId()));
        assertEquals(clientRepository.getFromMongo(r2.getEntityId()), r2);

        clientRepository.removeFromMongo(r1);
        assertNull(clientRepository.getFromMongo(r1.getEntityId()));
        assertEquals(r1, clientRepository.getFromRedis(r1.getEntityId()));

        //W sumie to działa ale czy powinno? Jakieś odświeżanie cache?
    }

    @Test
    void clearCacheTest() {
        int startingCount = clientRepository.getAll().size();

        clientRepository.clearCache();

        assertEquals(startingCount, clientRepository.getAll().size());

        assertThrows(IllegalArgumentException.class, () -> clientRepository.getFromRedis(client1.getEntityId()));
        assertThrows(IllegalArgumentException.class, () -> clientRepository.getFromRedis(client2.getEntityId()));
        assertThrows(IllegalArgumentException.class, () -> clientRepository.getFromRedis(client3.getEntityId()));

        //Idk tak samo?
    }

    @Test
//    @Disabled
    void disableRedisGetDataTest() {
        ClientMgd r1 = DataFakerMgd.getClientMgd();
        clientRepository.add(r1);

        assertEquals(clientRepository.get(r1.getEntityId()), r1);
        System.out.println("DISABLE REDIS VIA SERVICES!!!");
        while (ClientCache.checkHealthy()) {}

        assertEquals(clientRepository.get(client1.getEntityId()), client1);
        assertEquals(clientRepository.getFromMongo(client1.getEntityId()), client1);
        assertThrows(IllegalArgumentException.class, () -> clientRepository.getFromRedis(client1.getEntityId()));
    }

}