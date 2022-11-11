package pl.lodz.p.edu.rest.repository.impl;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.lodz.p.edu.rest.model.Client_;
import pl.lodz.p.edu.rest.model.UniqueId;
import pl.lodz.p.edu.rest.model.EQ.Equipment;
import pl.lodz.p.edu.rest.repository.Repository;

import java.util.List;


public class EquipmentRepository implements Repository<Equipment> {

    private EntityManager em;

    public EquipmentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Equipment get(UniqueId uniqueId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Equipment> cq = cb.createQuery(Equipment.class);
        Root<Equipment> equipment = cq.from(Equipment.class);

        cq.select(equipment);
        cq.where(cb.equal(equipment.get(Client_.ENTITY_ID), uniqueId));

        EntityTransaction et = em.getTransaction();
        et.begin();
        List<Equipment> equipmentList = em.createQuery(cq).setLockMode(LockModeType.OPTIMISTIC).getResultList();
        et.commit();


        if(equipmentList.isEmpty()) {
            throw new EntityNotFoundException("Equipment not found for uniqueId: " + uniqueId);
        }
        return equipmentList.get(0);
    }

    @Override
    public List<Equipment> getAll() {
        EntityTransaction et = em.getTransaction();
        et.begin();
        List<Equipment> equipmentList = em.createQuery("Select eq from Equipment eq", Equipment.class)
                .setLockMode(LockModeType.OPTIMISTIC).getResultList();
        et.commit();
        return equipmentList;
    }

    @Override
    public boolean add(Equipment elem) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            em.persist(elem);
            em.lock(elem, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            et.commit();
        } finally {
            if(et.isActive()) {
                et.rollback();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Equipment elem) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            em.lock(elem, LockModeType.OPTIMISTIC);
            this.em.remove(elem);
            et.commit();
        } finally {
            if(et.isActive()) {
                et.rollback();
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(Equipment elem) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            em.lock(elem, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            this.em.merge(elem);
            et.commit();
        } finally {
            if(et.isActive()) {
                et.rollback();
            }
        }
    }

    @Override
    public Long count() {
        EntityTransaction et = em.getTransaction();
        et.begin();
        Long count =  em.createQuery("Select count(eq) from Equipment eq", Long.class).getSingleResult();
        et.commit();
        return count;
    }
}