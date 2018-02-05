package com.mySampleApplication.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.mySampleApplication.client.widget.Aside;
import com.mySampleApplication.client.widget.Main;
import com.mySampleApplication.client.widget.MainView;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class MySampleApplication implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        MainView mainView = new MainView(MySampleApplicationService.App.getInstance());
        Main main = mainView.getMain();
        Aside aside = new Aside(MySampleApplicationService.App.getInstance());
        RootPanel.get("header").add(mainView);
        RootPanel.get("main").add(main);
        RootPanel.get("aside").add(aside);
    }
}
