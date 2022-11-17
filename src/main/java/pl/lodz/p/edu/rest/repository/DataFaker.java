package pl.lodz.p.edu.rest.repository;

import pl.lodz.p.edu.rest.exception.ObjectNotValidException;
import pl.lodz.p.edu.rest.model.Address;
import pl.lodz.p.edu.rest.model.Equipment;
import pl.lodz.p.edu.rest.model.Rent;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import pl.lodz.p.edu.rest.model.users.Admin;
import pl.lodz.p.edu.rest.model.users.Client;
import pl.lodz.p.edu.rest.model.users.Employee;

public class DataFaker {

    public static Address getAddress() {
        return new Address(randStr(7), randStr(10), randStr(4));
    }

    public static Client getClient(Address a) {
        try {
            return new Client(randStr(7), // idType.values()[(int)(Math.random() * 2) % 2],
                    randStr(10), randStr(10), a);
        } catch(ObjectNotValidException e) {
            return null; // will never happen
        }
    }

    public static Client getClient() {
        try {
            Address a = getAddress();
            return new Client(randStr(7),
                    randStr(10), randStr(10), a);
        } catch(ObjectNotValidException e) {
            return null; // will never happen
        }
    }

    public static Admin getAdmin() {
        try {
            return new Admin(randStr(10), randStr(20));
        } catch (ObjectNotValidException e) {
            return null; // will never happen
        }
    }

    public static Employee getEmployee() {
        try {
            return new Employee(randStr(10), randStr(20));
        } catch (ObjectNotValidException e) {
            return null; // will never happen
        }
    }


    public static Equipment getEquipment() {
        return new Equipment(Math.random() * 100, Math.random() * 200,
                Math.random() * 1000, randStr(10));
    }

    public static Rent getRent(Equipment e, Client c) {
        if(e == null) {
            e = getEquipment();
        }
        if(c == null) {
            c = getClient();
        }
        LocalDateTime nowLocal = LocalDateTime.now();
        DateTime now = nowLocal.toDateTime();
        long sinceEpoch = now.getMillis();
        long beginUnix = sinceEpoch + (long) (Math.random() * 10000000);
        long endUnix = beginUnix + (long) (Math.random() * 10000000);
        LocalDateTime begin = Instant.ofEpochMilli(beginUnix).toDateTime().toLocalDateTime();
        LocalDateTime end = Instant.ofEpochMilli(endUnix).toDateTime().toLocalDateTime();
        return new Rent(begin, end, e, c);
    }

    public static Rent getRent() {
        return getRent(null, null);
    }


    // source: https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
    public static String randStr(int size)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
