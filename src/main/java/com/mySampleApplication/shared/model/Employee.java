package com.mySampleApplication.shared.model;


import com.google.gwt.view.client.ProvidesKey;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee implements Comparable<Employee>, Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotNull
    @Size(min = 5)
    @Column(name="login")
    private String login;

    @NotNull
    @Size(min = 5)
    @Column(name="password")
    private String password;

    @Column(name="fio")
    private String fio;

    @Column(name="department")
    private String department;

    @Column(name="city")
    private String city;

    @Column(name="salary")
    private double salary;

    @Column(name="phoneNumber")
    private String phoneNumber;

    @Column(name="email")
    private String email;

    @ManyToMany()
    @JoinTable(name = "employee_position", joinColumns = {
            @JoinColumn(name = "employeeId")},
            inverseJoinColumns = {@JoinColumn(name = "positionId")})
    private Set<Position> positions = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "employee_role", joinColumns = {
            @JoinColumn(name = "employeeId")},
            inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private Set<Role> roles = new HashSet<>();

    @Transient
    public static final ProvidesKey<Employee> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public Employee() {
    }

    public Employee(String login) {
        this.login = login;
    }

    public Employee(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Employee(long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public Employee(String login, String password, String fio, String department, String city,
                    double salary, String phoneNumber, String email) {
        this.login = login;
        this.password = password;
        this.fio = fio;
        this.department = department;
        this.city = city;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }



    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getFio() {
        return fio;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getDepartment() {
        return department;
    }


    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public double getSalary() {
        return salary;
    }


    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Position> getPositions() {
        return positions;
    }


    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public Set<Role> getRoles() {
        return roles;
    }


    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Employee) {
            Employee o2 = (Employee)obj;
            return o2.getId() == id;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "Employee{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", fio='" + fio + '\'' +
                ", department='" + department + '\'' +
                ", city='" + city + '\'' +
                ", salary=" + salary +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'');

        sb.append(", roles=[");
        if (roles != null && !roles.isEmpty()) {
            for (Role role :
                    roles) {
                sb.append(role.getName() + ", ");
            }
            if (!roles.isEmpty()) {
                sb = sb.delete(sb.length() - 2, sb.length());
            }
        }
        sb.append("]");

        sb.append(", positions=[");
        if (positions != null && !positions.isEmpty()) {
            for (Position pos :
                    positions) {
                sb.append(pos.getName() + ", ");
            }
            sb = sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]}");

        return sb.toString();
    }

    @Override
    public int compareTo(Employee o) {
        return (o == null || o.getPassword() == null) ? -1 : -o.getLogin().compareTo(login);
    }
}
