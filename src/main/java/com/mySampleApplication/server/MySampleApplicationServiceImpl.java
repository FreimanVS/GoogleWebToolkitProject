package com.mySampleApplication.server;

import com.mySampleApplication.shared.model.Employee ;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mySampleApplication.client.MySampleApplicationService;
import com.mySampleApplication.shared.security.Encode;
import com.mySampleApplication.shared.service.EmployeeService;
import com.mySampleApplication.shared.service.Service;
import java.util.List;

public class MySampleApplicationServiceImpl extends RemoteServiceServlet implements MySampleApplicationService {

    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }

    Service<Employee> employeeService = new EmployeeService();

    @Override
    public void authorize(Employee employee) throws Exception {
        List<Employee> list = employeeService.getList();
        boolean exists = false;

        if (list != null && !list.isEmpty()) {
            exists = list.stream().anyMatch(emp -> emp.getLogin().equals(employee.getLogin())
                    && emp.getPassword().equals(Encode.sha(employee.getPassword())));
        }

        if (!exists) {
            throw new Exception("Auth error");
        }
    }
}