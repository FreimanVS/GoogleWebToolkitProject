package com.mySampleApplication.server.servlets;


import com.mySampleApplication.shared.model.Employee;
import com.mySampleApplication.shared.service.EmployeeService;
import com.mySampleApplication.shared.service.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private Service<Employee> employeeService = new EmployeeService();
    private static final Jsonb JSONB = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        //get all
        if (id == null) {
            List<Employee> employees = employeeService.getList();
            String jsonString = JSONB.toJson(employees);
            resp.setContentType("application/json");
            try (PrintWriter pw = resp.getWriter()) {
                pw.print(jsonString);
                pw.flush();
            }
        }

        //get one by id
        else {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();
        if (!contentType.equals("application/json")) {
            resp.sendError(400, "application/json is required");
        }

        try (InputStream is = req.getInputStream();
             PrintWriter pw = resp.getWriter()) {
            StringBuilder json = new StringBuilder();
            while (is.available() != 0) {
                json.append((char)is.read());
            }
            Employee emp = JSONB.fromJson(json.toString(), Employee.class);

            //temp settings
            emp.setPassword(emp.getLogin());
            emp.setFio(emp.getLogin());
            emp.setDepartment(emp.getLogin());
            emp.setCity(emp.getLogin());
            emp.setSalary(100000.0);
            emp.setPhoneNumber(emp.getLogin());
            emp.setEmail(emp.getLogin());

            long id = employeeService.add(emp);
            Employee empFromDB = employeeService.getById(id);

            pw.println(JSONB.toJson(empFromDB));
        }
    }

    //There is no a doPatch method?
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();
        if (!contentType.startsWith("application/json")) {
            resp.sendError(400, "application/json is required");
        }

        long id_param = Long.valueOf(req.getParameter("id"));
        try (InputStream is = req.getInputStream();
            PrintWriter pw = resp.getWriter()) {
            StringBuilder json = new StringBuilder();
            while (is.available() != 0) {
                json.append((char)is.read());
            }
            Employee emp = JSONB.fromJson(json.toString(), Employee.class);
            employeeService.updateById(id_param, emp);
            pw.println(JSONB.toJson(employeeService.getById(id_param)));
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id_param = Long.valueOf(req.getParameter("id"));
        employeeService.deleteById(id_param);
    }
}
