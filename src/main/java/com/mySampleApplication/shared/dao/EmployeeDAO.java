package com.mySampleApplication.shared.dao;

import com.mySampleApplication.shared.model.Employee;
import com.mySampleApplication.shared.model.Position;
import com.mySampleApplication.shared.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeDAO implements DAO<Employee> {
    private EntityManager em;

    public EmployeeDAO() {
    }

    public EmployeeDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public long add(Employee obj) {
        em.persist(obj);
        return obj.getId();
    }

    @Override
    public List<Employee> getList() {
        TypedQuery<Employee> query = em.createQuery("from Employee", Employee.class);
        return query.getResultList();
    }

    @Override
    public Employee getById(long id) {
        return em.find(Employee.class, id);
    }

    @Override
    public void deleteById(long id) {
        Employee objFromDB = getById(id);
        em.remove(objFromDB);
    }

    @Override
    public void updateById(long id, Employee obj) {
        Employee objFromDB = em.find(Employee.class, id);
        if (obj.getLogin() != null)
            objFromDB.setLogin(obj.getLogin());
        if (obj.getPassword() != null)
            objFromDB.setPassword(obj.getPassword());
        if (obj.getFio() != null)
            objFromDB.setFio(obj.getFio());
        if (obj.getDepartment() != null)
            objFromDB.setDepartment(obj.getDepartment());
        if (obj.getCity() != null)
            objFromDB.setCity(obj.getCity());
        if (obj.getSalary() != 0.0)
            objFromDB.setSalary(obj.getSalary());
        if (obj.getPhoneNumber() != null)
            objFromDB.setPhoneNumber(obj.getPhoneNumber());
        if (obj.getEmail() != null)
            objFromDB.setEmail(obj.getEmail());
        if (obj.getRoles() != null && !obj.getPositions().isEmpty()) {
            DAO<Role> roleDao = new RoleDAO(em);
            Set<Role> roles = obj.getRoles().stream().map(r -> roleDao.getById(r.getId())).collect(Collectors.toSet());
            objFromDB.setRoles(roles);
        }
        if (obj.getPositions() != null && !obj.getPositions().isEmpty()) {
            DAO<Position> positionDao = new PositionDAO(em);
            Set<Position> positions = obj.getPositions().stream()
                    .map(p -> positionDao.getById(p.getId())).collect(Collectors.toSet());
            objFromDB.setPositions(positions);
        }
        em.flush();
    }
}
