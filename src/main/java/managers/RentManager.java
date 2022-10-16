package managers;

import jakarta.persistence.EntityNotFoundException;
import model.Client;
import model.Address;
import model.EQ.Equipment;
import model.Rent;
import model.UniqueId;
import org.joda.time.LocalDateTime;
import repository.impl.RentRepository;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


public class RentManager {
    private RentRepository rentRepository;

    public RentManager(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    public Rent makeReservation(Client client, Equipment equipment, Address address,
                                LocalDateTime beginTime, LocalDateTime endTime) {
        if (equipment.isMissing() || equipment.isArchive()) {
            return null;
        }
        if (client.isArchive()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        if (beginTime.isEqual(now) || beginTime.isBefore(now)) {
            return null;
        }

        if (beginTime.isAfter(endTime)) {
            return null;
        }

        boolean good = true;
        List<Rent> rentEquipmentList = equipment.getEquipmentRents();

        for (int i = 0; i < rentEquipmentList.size(); i++) {
            Rent curRent = rentEquipmentList.get(i);

            // +----- old rent -----+
            //         +----- new rent -----+
            if (beginTime.isBefore(curRent.getEndTime()) && beginTime.isAfter(curRent.getBeginTime())) {
                good = false;
            }

            // new rent time inside another rent
            // +----- old rent -----+
            //    +-- new rent --+
            if (beginTime.isAfter(curRent.getBeginTime()) && beginTime.isBefore(curRent.getEndTime())) {
                good = false;
            }
            //    +-- old rent -----+
            // +----- new rent --+
            if (endTime.isAfter(curRent.getBeginTime()) && endTime.isBefore(curRent.getEndTime())) {
                good = false;
            }
            //    +-- old rent --+
            // +----- new rent -----+
            if (beginTime.isBefore(curRent.getBeginTime()) && endTime.isAfter(curRent.getEndTime())) {
                good = false;
            }
        }

        if (good) {
            Rent rent = new Rent(beginTime, endTime, equipment, client, address);
            rentRepository.add(rent);
            return rent;
        } else {
            return null;
        }
    }

    public List<Rent> getClientRents(Client client) {
        // todo
//        Predicate<Rent> rentPredicate = (
//                x -> x.getClient() == client && !x.getClient().isArchive()
//                );
//
//        return rentRepository.findBy(rentPredicate);
    }

    public void shipEquipment(Rent rent) {
        rent.setShipped(true);
        rentRepository.update(rent);
    }

    public boolean isAvailable(Equipment equipment) {
        return Objects.equals(whenAvailable(equipment), LocalDateTime.now());
    }


    public LocalDateTime whenAvailable(Equipment equipment) {
        // todo
//        if (equipment.isArchive() || equipment.isMissing()) {
//            return null;
//        }
//        LocalDateTime when = LocalDateTime.now();
//        List<Rent> equipmentRents = getEquipmentRents(equipment);
//
//        for (Rent rent :
//                equipmentRents) {
//            if (when.isAfter(rent.getBeginTime()) && when.isBefore(rent.getEndTime())) {
//                when = rent.getEndTime();
//            }
//        }
//        return when;
    }

    public LocalDateTime untilAvailable(Equipment equipment) {
        // todo
//        LocalDateTime until = null;
//
//        if (equipment.isArchive() || equipment.isMissing()) {
//            return null;
//        }
//
//        LocalDateTime when = whenAvailable(equipment);
//        List<Rent> equipmentRents = getEquipmentRents(equipment);
//
//        for (Rent rent :
//                equipmentRents) {
//            if (when.isBefore(rent.getBeginTime())) {
//                until = rent.getEndTime();
//            }
//        } //FIXME Is there a mistake?
//        return until;
    }

    public void cancelReservation(Rent rent) {
        rentRepository.remove(rent);
    }

    public void returnEquipment(Rent rent, boolean missing) {
        rent.setEqReturned(true);
        rent.getEquipment().setMissing(missing);
        rentRepository.update(rent);
    }

    public double checkClientBalance(Client client) {
        // todo
//        List<Rent> rentList = getClientRents(client);
//        double balance = 0.0;
//        for (Rent rent :
//                rentList) {
//            balance += rent.getRentCost();
//        }
//        return balance;
    }

    public List<Rent> getAllRents() {
        return rentRepository.getAll();
    }

    public Rent getRent(UniqueId id) {
        try {
            return rentRepository.get(id);
        } catch(EntityNotFoundException ex) {
            return null;
        }
    }
}
