package com.mySampleApplication.client;

import com.mySampleApplication.shared.model.Employee ;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MySampleApplicationServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
    void authorize(Employee employee, AsyncCallback<Void> async);
}
