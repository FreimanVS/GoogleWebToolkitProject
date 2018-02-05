package com.mySampleApplication.client.widget;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.mySampleApplication.client.MySampleApplicationServiceAsync;
import com.mySampleApplication.shared.model.Employee;
import com.mySampleApplication.shared.model.Position;
import com.mySampleApplication.shared.model.Role;
import com.mySampleApplication.shared.validator.ValidatorRule;

import java.util.*;

public class Main extends Composite {

    @UiTemplate("Main.ui.xml")
    interface MainUiBinder extends UiBinder<DeckPanel, Main> {
    }

    @UiField
    DeckPanel deckPanel;

    @UiField
    InputElement loginTextField;

    @UiField
    InputElement passwordTextField;

    @UiField
    ButtonElement submit;

    @UiField
    DataGrid<Employee> dataGrid;

    @UiField
    SimplePager pager;

    private static Main.MainUiBinder mainUiBinder = GWT.create(Main.MainUiBinder.class);

    private ListDataProvider<Employee> dataProvider = new ListDataProvider<>();

    public Main(MySampleApplicationServiceAsync service) {

        initWidget(mainUiBinder.createAndBindUi(this));

        Event.sinkEvents(submit, Event.ONCLICK);
        Event.setEventListener(submit, event -> {
            Employee employee = new Employee(loginTextField.getValue(), passwordTextField.getValue());

            if (ValidatorRule.isValid(employee)) {
                service.authorize(employee, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Неверный логин или пароль");
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Window.alert("Вход успешен!");
                        deckPanel.showWidget(5);
                        showDataGrid();
                    }
                });
            }
        });
    }

    int count = 0;
    public void showDataGrid() {
        if (count == 1) {
            return;
        }
        count++;

        dataGrid.setAutoHeaderRefreshDisabled(true);

        dataGrid.setEmptyTableWidget(new Label("EMPTY OR LOADING DATA..."));

        ColumnSortEvent.ListHandler<Employee> sortHandler =
                new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(sortHandler);

        pager.setDisplay(dataGrid);

        initTableColumns(sortHandler);

        dataProvider.addDataDisplay(dataGrid);

        getAllEmployees();
    }

    private void getAllEmployees() {
        final String JSON_url = "/employees";

        RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, JSON_url);
        try {
            rb.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (!(response.getHeader("Content-Type")).startsWith("application/json")
                            || (response.getStatusCode() != 200)) {
                        Window.alert("A wrong content. " + response.getStatusText());
                    } else {
                        //JsArray from JSON
                        JsArray<EmployeeJS> jsArray = JsonUtils.safeEval(response.getText());

                        //make a java List from JsArray
                        List<Employee> employees = listFromJsArray(jsArray);

                        //add the list to DataProvider
                        dataProvider.getList().addAll(employees);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert(exception.getLocalizedMessage());
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    private void initTableColumns(ColumnSortEvent.ListHandler<Employee> sortHandler) {
        //id
        Column<Employee, Number> idColumn =
                new Column<Employee, Number>(new NumberCell()) {
                    @Override
                    public Number getValue(Employee object) {
                        return object.getId();
                    }
                };

        idColumn.setSortable(true);
        sortHandler.setComparator(idColumn, Comparator.comparingLong(Employee::getId));
        dataGrid.addColumn(idColumn, "id");
        dataGrid.setColumnWidth(idColumn, 2, Style.Unit.PCT);

        //login
        Column<Employee, String> loginColumn = new Column<Employee, String>(new EditTextCell()) {
            @Override
            public String getValue(Employee object) {
                return object.getLogin();
            }
        };
        loginColumn.setSortable(true);
        sortHandler.setComparator(loginColumn, (e1, e2) -> {
            if (e1.getLogin().length() == e2.getLogin().length()) {
                return e1.getLogin().compareTo(e2.getLogin());
            } else {
                return Long.compare(e1.getLogin().length(), e2.getLogin().length());
            }
        });
        loginColumn.setFieldUpdater((index, object, value) -> {

            object.setLogin(value);

            if (object.getId() != 0) {
                //put into DB
                putEmployee(object);
            }

            dataProvider.refresh();
        });
        dataGrid.setColumnWidth(loginColumn, 5, Style.Unit.PCT);
        dataGrid.addColumn(loginColumn, "Login");

        //delete a column
        Column<Employee, String> deleteColumn = new Column<Employee, String>(new ButtonCell()) {
            @Override
            public String getValue(Employee object) {
                if (object.getId() == 0) {
                    return "add";
                }
                return "delete";
            }
        };
        deleteColumn.setFieldUpdater((index, object, value) -> {
            if (object.getId() == 0) {

                Employee employee = new Employee();
                employee.setLogin(object.getLogin());
                String newEmployeeJSON = fromEmployeeToJSON(employee);
                postEmployee(newEmployeeJSON);

            } else {
                dataProvider.getList().remove(index);

                //delete from DB
                deleteEmployee(object.getId());
                dataProvider.refresh();

            }
        });
        dataGrid.setColumnWidth(deleteColumn, 2, Style.Unit.PCT);
        dataGrid.addColumn(deleteColumn);

        //add a column
        dataProvider.getList().add(new Employee("INPUT LOGIN HERE"));


    }

    //JS native code
    private static class EmployeeJS extends JavaScriptObject {
        protected EmployeeJS() { }
        public final native int getId() /*-{ return this.id; }-*/;
        public final native String getLogin() /*-{ return this.login; }-*/;
        public final native String getPassword() /*-{ return this.password; }-*/;
        public final native String getFio() /*-{ return this.fio; }-*/;
        public final native String getDepartment() /*-{ return this.department; }-*/;
        public final native String getCity() /*-{ return this.city; }-*/;
        public final native double getSalary() /*-{ return this.salary; }-*/;
        public final native String getPhoneNumber() /*-{ return this.phoneNumber; }-*/;
        public final native String getEmail() /*-{ return this.email; }-*/;
        public final native JsArray<PositionJS> getPositions() /*-{ return this.positions; }-*/;
        public final native JsArray<RoleJS> getRoles() /*-{ return this.roles; }-*/;
    }

    private static class RoleJS extends JavaScriptObject {
        protected RoleJS() { }
        public final native int getId() /*-{ return this.id; }-*/;
        public final native String getName() /*-{ return this.name; }-*/;

    }

    private static class PositionJS extends JavaScriptObject {
        protected PositionJS() { }
        public final native int getId() /*-{ return this.id; }-*/;
        public final native String getName() /*-{ return this.name; }-*/;

    }

    private void postEmployee(String employeeJSON) {

        final String postUrl = "/employees";

        RequestBuilder postJson = new RequestBuilder(RequestBuilder.POST, postUrl);
        postJson.setHeader("Content-Type", "application/json");

        try {
            postJson.sendRequest(employeeJSON, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {

                        Window.alert(response.getText());

                        JSONValue jsonValue = JSONParser.parseStrict(response.getText());
                        Main.EmployeeJS employeeJS = jsonValue.isObject().getJavaScriptObject().cast();
                        Employee employee = fromEmployeeJsToJava(employeeJS);

                        dataProvider.getList().add(employee);
                        dataProvider.refresh();
                    } else {
                        Window.alert(String.valueOf(response.getStatusCode() + " " + response.getStatusText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert(exception.getLocalizedMessage());
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee(long id) {
        final String deleteUrl = "/employees?id=" + id;

        RequestBuilder delete = new RequestBuilder(RequestBuilder.DELETE, deleteUrl);

        try {
            delete.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    Window.alert(String.valueOf(response.getStatusCode()));
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert(exception.getLocalizedMessage());
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    private void putEmployee(Employee employee) {

        final String putJsonUrl = "/employees?id=" + employee.getId();
        String data = fromEmployeeToJSON(employee);

        RequestBuilder putJson = new RequestBuilder(RequestBuilder.PUT, putJsonUrl);
        putJson.setHeader("Content-Type", "application/json");

        try {
            putJson.sendRequest(data, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {

                    Window.alert(response.getText());
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert(exception.getLocalizedMessage());
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    private String fromEmployeeToJSON(Employee employee) {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"id\": ").append(employee.getId())
                .append(", ").append("\"login\": \"").append(employee.getLogin()).append("\"")
                .append("}");
        return sb.toString();
    }

    private Employee fromEmployeeJsToJava(Main.EmployeeJS employeeJS) {
        Employee employeeJava = new Employee();

        employeeJava.setId(employeeJS.getId());
        employeeJava.setLogin(employeeJS.getLogin());
        employeeJava.setPassword(employeeJS.getPassword());
        employeeJava.setFio(employeeJS.getFio());
        employeeJava.setDepartment(employeeJS.getDepartment());
        employeeJava.setCity(employeeJS.getCity());
        employeeJava.setSalary(employeeJS.getSalary());
        employeeJava.setPhoneNumber(employeeJS.getPhoneNumber());
        employeeJava.setEmail(employeeJS.getEmail());

        Set<Role> roles = new HashSet<>();
        JsArray<RoleJS> rolesJsArray = employeeJS.getRoles();
        for (int j = 0; j < rolesJsArray.length(); j++) {
            Role role = new Role();
            role.setId(rolesJsArray.get(j).getId());
            role.setName(rolesJsArray.get(j).getName());
            roles.add(role);
        }
        employeeJava.setRoles(roles);

        Set<Position> positions = new HashSet<>();
        JsArray<PositionJS> positionJSJsArray = employeeJS.getPositions();
        for (int j = 0; j < positionJSJsArray.length(); j++) {
            Position position = new Position();
            position.setId(positionJSJsArray.get(j).getId());
            position.setName(positionJSJsArray.get(j).getName());
            positions.add(position);
        }
        employeeJava.setPositions(positions);

        return employeeJava;
    }

    private List<Employee> listFromJsArray(JsArray<EmployeeJS> jsArray) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < jsArray.length(); i++) {

            Main.EmployeeJS employeeJS = jsArray.get(i);
            Employee employeeJava = fromEmployeeJsToJava(employeeJS);
            employees.add(employeeJava);
        }

        return employees;
    }
}
