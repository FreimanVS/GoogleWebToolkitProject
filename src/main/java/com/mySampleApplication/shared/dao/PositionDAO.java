package com.mySampleApplication.shared.dao;

import com.mySampleApplication.shared.model.Position;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PositionDAO implements DAO<Position> {

    private EntityManager em;

    public PositionDAO() {
    }

    public PositionDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public long add(Position obj) {
        em.persist(obj);
        return obj.getId();
    }

    @Override
    public List<Position> getList() {
        TypedQuery<Position> query = em.createQuery("from Position", Position.class);
        return query.getResultList();
    }

    @Override
    public Position getById(long id) {
        return em.find(Position.class, id);
    }

    @Override
    public void deleteById(long id) {
        Position objFromDB = getById(id);
        em.remove(objFromDB);
    }

    @Override
    public void updateById(long id, Position obj) {
        Position objFromDB = em.find(Position.class, id);
        objFromDB.setName(obj.getName());
        objFromDB.setEmpls(obj.getEmpls());
        em.flush();
    }
}
