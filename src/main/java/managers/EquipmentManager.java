//package managers;
//
//import exception.EquipmentException;
//import jakarta.persistence.EntityNotFoundException;
//import model.EQ.*;
//
//import model.UniqueId;
//import repository.impl.EquipmentRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Predicate;
//
//public class EquipmentManager {
//    EquipmentRepository equipmentRepository;
//
//    public EquipmentManager(EquipmentRepository equipmentRepository) {
//        this.equipmentRepository = equipmentRepository;
//    }
//
//    public void unregisterEquipment(Equipment equipment) {
//        Equipment e = equipmentRepository.get(equipment.getEntityId());
//        equipmentRepository.remove(e);
//    }
//
//    public List<Equipment> getAllEquipment() {
//        return equipmentRepository.getAll();
//    }
//
//    public List<Equipment> getAllAvailableEquipment() {
//        List<Equipment> all = getAllEquipment();
//        List<Equipment> available = new ArrayList<>();
//        for (Equipment e : all) {
//            if(!(e.isArchive() || e.isMissing())) {
//                available.add(e);
//            }
//        }
//        return available;
//    }
//
//    public Equipment registerCamera(double fDayCost, double nDayCost, double bail, String name, String resolution) throws EquipmentException {
//        Camera camera = new Camera(fDayCost, nDayCost, bail, name, resolution);
//        equipmentRepository.add(camera);
//        return camera;
//    }
//
//    public Equipment registerTrivet(double fDayCost, double nDayCost, double bail, String name, double weight) throws EquipmentException {
//        Trivet trivet = new Trivet(fDayCost, nDayCost, bail, name, weight);
//        equipmentRepository.add(trivet);
//        return trivet;
//    }
//
//    public Equipment registerLens(double fDayCost, double nDayCost, double bail, String name, String length) throws EquipmentException {
//        Lens lens = new Lens(fDayCost, nDayCost, bail, name, length);
//        equipmentRepository.add(lens);
//        return lens;
//    }
//
//    public Equipment registerMicrophone(double fDayCost, double nDayCost, double bail, String name, String sensitivity) throws EquipmentException {
//        Microphone microphone = new Microphone(fDayCost, nDayCost, bail, name, sensitivity);
//        equipmentRepository.add(microphone);
//        return microphone;
//    }
//
//    public Equipment registerLighting(double fDayCost, double nDayCost, double bail, String name, String brightness) throws EquipmentException {
//        Lighting lighting = new Lighting(fDayCost, nDayCost, bail, name, brightness);
//        equipmentRepository.add(lighting);
//        return lighting;
//    }
//
//    public Equipment getEquipment(UniqueId id) {
//        try {
//            return equipmentRepository.get(id);
//        } catch(EntityNotFoundException ex) {
//            return null;
//        }
//    }
//}
