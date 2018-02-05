package com.mySampleApplication.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.mySampleApplication.client.MySampleApplicationServiceAsync;

public class MainView extends Composite {

    @UiTemplate("MainPart.ui.xml")
    interface MainViewUiBinder extends UiBinder<HTMLPanel, MainView> {
    }

    @UiField
    Anchor a1;

    @UiField
    Anchor a2;

    @UiField
    Anchor a3;

    @UiField
    Anchor a4;

    @UiField
    Anchor a5;

    Main main;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    public MainView(MySampleApplicationServiceAsync service) {

        main = new Main(service);
        initWidget(ourUiBinder.createAndBindUi(this));

        a1.addClickHandler(clickEvent -> main.deckPanel.showWidget(0));
        a2.addClickHandler(clickEvent -> main.deckPanel.showWidget(1));
        a3.addClickHandler(clickEvent -> main.deckPanel.showWidget(2));
        a4.addClickHandler(clickEvent -> main.deckPanel.showWidget(3));
        a5.addClickHandler(clickEvent -> main.deckPanel.showWidget(4));
    }

    public Main getMain() {
        return main;
    }
}
