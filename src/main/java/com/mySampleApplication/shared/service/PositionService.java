package com.mySampleApplication.shared.service;

import com.mySampleApplication.shared.dao.DAO;
import com.mySampleApplication.shared.dao.PositionDAO;
import com.mySampleApplication.shared.model.Position;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PositionService implements Service<Position> {

    private Session session;

    private DAO<Position> positionDAO;

    public PositionService(Session session) {
        this.session = session;
        positionDAO = new PositionDAO(session);
    }

    public PositionService() {
    }

    @Override
    public long add(Position obj) {
        Transaction transaction = session.beginTransaction();
        try {
            long id = positionDAO.add(obj);
            transaction.commit();
            return id;
        } catch (Exception e) {
            transaction.rollback();
            return -1;
        }
    }

    @Override
    public List<Position> getList() {
        Transaction transaction = session.beginTransaction();
        try {
            List<Position> list = positionDAO.getList();
            transaction.commit();
            return list;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public Position getById(long id) {
        Transaction transaction = session.beginTransaction();
        try {
            Position pos = positionDAO.getById(id);
            transaction.commit();
            return pos;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        Transaction transaction = session.beginTransaction();
        try {
            positionDAO.deleteById(id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public void updateById(long id, Position obj) {
        Transaction transaction = session.beginTransaction();
        try {
            positionDAO.updateById(id, obj);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
