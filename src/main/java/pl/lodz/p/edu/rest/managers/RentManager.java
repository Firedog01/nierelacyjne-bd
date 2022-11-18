package pl.lodz.p.edu.rest.managers;

import jakarta.inject.Inject;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.edu.rest.controllers.AdminController;
import pl.lodz.p.edu.rest.exception.BusinessLogicInterruptException;
import pl.lodz.p.edu.rest.exception.ConflictException;
import pl.lodz.p.edu.rest.exception.IllegalModificationException;
import pl.lodz.p.edu.rest.exception.ObjectNotValidException;
import pl.lodz.p.edu.rest.model.DTO.RentDTO;
import pl.lodz.p.edu.rest.model.Equipment;
import pl.lodz.p.edu.rest.model.Rent;

import pl.lodz.p.edu.rest.model.users.Client;
import pl.lodz.p.edu.rest.repository.impl.EquipmentRepository;
import pl.lodz.p.edu.rest.repository.impl.RentRepository;
import pl.lodz.p.edu.rest.repository.impl.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


@Transactional
public class RentManager {

    Logger logger = Logger.getLogger(RentManager.class.getName());

    @Inject
    private RentRepository rentRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private EquipmentRepository equipmentRepository;

    protected RentManager() {}

    public List<Rent> getRentByEq(Equipment equipment) {
        return rentRepository.getRentByEq(equipment);
    }

    public List<Rent> getRentsByClient(Client client) {
        return rentRepository.getRentByClient(client);
    }

    public Rent get(UUID uuid) {
        return rentRepository.get(uuid);
    }

    public List<Rent> getAll() {
        return rentRepository.getAll();
    }

    public Rent add(RentDTO rentDTO) throws ObjectNotValidException, BusinessLogicInterruptException {
        Client client;
        Equipment equipment;
        LocalDateTime beginTime, endTime = null;
        try {
            beginTime = LocalDateTime.parse(rentDTO.getBeginTime());
            if(rentDTO.getEndTime() != null) {
                endTime = LocalDateTime.parse(rentDTO.getEndTime());
            }
        } catch(DateTimeParseException e) {
            throw new ObjectNotValidException("Given dates were invalid");
        }
//        logger.info("");
        synchronized (userRepository) {
            try {
                logger.info("LOGGGER: " + rentDTO.getClientUUIDFromString().toString());
                client = (Client) userRepository.getOfType("Client", rentDTO.getClientUUIDFromString());
            } catch(NoResultException e) {
                throw new ObjectNotValidException("Client of given uuid does not exist");
            }
            synchronized (equipmentRepository) {
                try {
                    equipment = equipmentRepository.get(rentDTO.getEquipmentUUIDFromString());
                } catch(EntityNotFoundException e) {
                    throw new ObjectNotValidException("Equipment of given uuid does not exist");
                }
                boolean available = this.checkEquipmentAvailable(equipment, beginTime);
                if(available) {
                    Rent rent = new Rent(beginTime, endTime, equipment, client);
                    rentRepository.add(rent);
                    return rent;
                } else {
                    throw new BusinessLogicInterruptException("Equipment not available");
                }
            }
        }
    }

    private boolean checkEquipmentAvailable(Equipment equipment, LocalDateTime beginTime) {
        List<Rent> rentEquipmentList;
        try {
            rentEquipmentList = rentRepository.getEquipmentRents(equipment);
        } catch(NoResultException e) {
            return true;
        }
        for (int i = 0; i < rentEquipmentList.size(); i++) {
            Rent curRent = rentEquipmentList.get(i);
            if (beginTime.isBefore(curRent.getEndTime())) {
                return false;
            }

            // +----- old rent -----+
            //         +----- new rent -----+
//            if (beginTime.isBefore(curRent.getEndTime()) && beginTime.isAfter(curRent.getBeginTime())) {
//                return false;
//            }
            //         +----- old rent -----+
            // +----- new rent -----+
//            if (endTime.isAfter(curRent.getBeginTime()) && endTime.isBefore(curRent.getEndTime())) {
//                return false;
//            }
//            // +----- old rent -----+
//            //    +-- new rent --+
//            if (beginTime.isAfter(curRent.getBeginTime()) && beginTime.isBefore(curRent.getEndTime())) {
//                return false;
//            }
//            //    +-- old rent --+
//            // +----- new rent -----+
//            if (beginTime.isBefore(curRent.getBeginTime()) && endTime.isAfter(curRent.getEndTime())) {
//                return false;
//            }
        }
        return true;
    }

    public Rent update(UUID entityId, RentDTO rentDTO) throws ObjectNotValidException, BusinessLogicInterruptException {
        Rent rent = rentRepository.get(entityId);
        Client client;
        Equipment equipment;
        LocalDateTime now, beginTime, endTime = null;
        now = LocalDateTime.now();

        // date validation
        try {
            beginTime = LocalDateTime.parse(rentDTO.getBeginTime());
            if(rentDTO.getEndTime() != null) {
                endTime = LocalDateTime.parse(rentDTO.getEndTime());
            }
        } catch(DateTimeParseException e) {
            throw new ObjectNotValidException("Given dates were invalid");
        }
        if(beginTime.isBefore(now)) {
            throw new ObjectNotValidException("Given dates were invalid");
        }

        synchronized (userRepository) {
            client = (Client) userRepository.getOfType("Client", rentDTO.getClientUUIDFromString());
            if (client == null) {
                throw new ObjectNotValidException("Client of given uuid does not exist");
            }
            synchronized (equipmentRepository) {
                equipment = equipmentRepository.get(rentDTO.getEquipmentUUIDFromString());
                if (equipment == null) {
                    throw new ObjectNotValidException("Equipment of given uuid does not exist");
                }
                boolean available = this.checkEquipmentAvailable(equipment, beginTime);
                if(available) {
                    rent.merge(rentDTO, equipment, client);
                    rentRepository.update(rent);
                } else {
                    throw new BusinessLogicInterruptException("Equipment not available");
                }

                return rent;
            }
        }
    }

    public void remove(UUID uuid) {
        rentRepository.remove(uuid);
    }


    public LocalDateTime whenAvailable(Equipment equipment) {
        LocalDateTime when = LocalDateTime.now();
        List<Rent> equipmentRents = getRentByEq(equipment);
//        List<Rent> equipmentRents = equipment.getEquipmentRents();

        for (Rent rent :
                equipmentRents) {
            if (when.isAfter(rent.getBeginTime()) && when.isBefore(rent.getEndTime())) {
                when = rent.getEndTime();
            }
        }
        return when;
    }
//    public boolean validateTime(List<Rent> rentEquipmentList, LocalDateTime beginTime, LocalDateTime endTime) {
//        for (int i = 0; i < rentEquipmentList.size(); i++) {
//            Rent curRent = rentEquipmentList.get(i);
//
//            // +----- old rent -----+
//            //         +----- new rent -----+
//            if (beginTime.isBefore(curRent.getEndTime()) && beginTime.isAfter(curRent.getBeginTime())) {
//                return false;
//            }
//            //         +----- old rent -----+
//            // +----- new rent -----+
//            if (endTime.isAfter(curRent.getBeginTime()) && endTime.isBefore(curRent.getEndTime())) {
//                return false;
//            }
//            // +----- old rent -----+
//            //    +-- new rent --+
//            if (beginTime.isAfter(curRent.getBeginTime()) && beginTime.isBefore(curRent.getEndTime())) {
//                return false;
//            }
//            //    +-- old rent --+
//            // +----- new rent -----+
//            if (beginTime.isBefore(curRent.getBeginTime()) && endTime.isAfter(curRent.getEndTime())) {
//                return false;
//            }
//        }
//        return true;
//    }
}