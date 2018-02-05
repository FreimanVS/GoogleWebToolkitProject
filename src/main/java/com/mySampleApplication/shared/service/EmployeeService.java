package com.mySampleApplication.shared.service;

import com.mySampleApplication.shared.dao.DAO;
import com.mySampleApplication.shared.dao.EmployeeDAO;
import com.mySampleApplication.shared.dao.PositionDAO;
import com.mySampleApplication.shared.dao.RoleDAO;
import com.mySampleApplication.shared.factory.JPAFactory;
import com.mySampleApplication.shared.model.Employee;
import com.mySampleApplication.shared.model.Position;
import com.mySampleApplication.shared.model.Role;
import com.mySampleApplication.shared.security.Encode;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;


public class EmployeeService implements Service<Employee> {
    private EntityManager em = JPAFactory.get().createEntityManager();

    private DAO<Employee> employeeDAO = new EmployeeDAO(em);

    public EmployeeService() {
    }

    @Override
    public long add(Employee obj) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            obj.setPassword(Encode.sha(obj.getPassword()));
            Role user = new RoleDAO(em).getById(1L);
            obj.getRoles().add(user);

            Position worker = new PositionDAO(em).getById(1L);
            obj.getPositions().add(worker);

            long id = employeeDAO.add(obj);
            transaction.commit();
            return id;
        } catch (Exception e) {
            transaction.rollback();
            return -1;
        }
    }

    @Override
    public List<Employee> getList() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            List<Employee> list = employeeDAO.getList();
            transaction.commit();
            return list;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public Employee getById(long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Employee pos = employeeDAO.getById(id);
            transaction.commit();
            return pos;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            employeeDAO.deleteById(id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public void updateById(long id, Employee obj) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            employeeDAO.updateById(id, obj);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
