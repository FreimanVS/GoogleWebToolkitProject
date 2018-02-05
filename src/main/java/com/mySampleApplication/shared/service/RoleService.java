package com.mySampleApplication.shared.service;

import com.mySampleApplication.shared.dao.DAO;
import com.mySampleApplication.shared.dao.RoleDAO;
import com.mySampleApplication.shared.model.Role;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RoleService implements Service<Role> {
    private Session session;

    private DAO<Role> roleDAO;

    public RoleService(Session session) {
        this.session = session;
        roleDAO = new RoleDAO(session);
    }

    @Override
    public long add(Role obj) {
        Transaction transaction = session.beginTransaction();
        try {
            long id = roleDAO.add(obj);
            transaction.commit();
            return id;
        } catch (Exception e) {
            transaction.rollback();
            return -1;
        }
    }

    @Override
    public List<Role> getList() {
        Transaction transaction = session.beginTransaction();
        try {
            List<Role> list = roleDAO.getList();
            transaction.commit();
            return list;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public Role getById(long id) {
        Transaction transaction = session.beginTransaction();
        try {
            Role role = roleDAO.getById(id);
            transaction.commit();
            return role;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        Transaction transaction = session.beginTransaction();
        try {
            roleDAO.deleteById(id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public void updateById(long id, Role obj) {
        Transaction transaction = session.beginTransaction();
        try {
            roleDAO.updateById(id, obj);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
