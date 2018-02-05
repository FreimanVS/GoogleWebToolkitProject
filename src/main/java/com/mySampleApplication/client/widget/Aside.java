package com.mySampleApplication.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.http.client.*;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.mySampleApplication.client.MySampleApplicationServiceAsync;

public class Aside extends Composite {

    @UiTemplate("Aside.ui.xml")
    interface AsideUiBinder extends UiBinder<HTMLPanel, Aside> {
    }

    @UiField
    ParagraphElement curs;

    @UiField
    ParagraphElement news;

    private static Aside.AsideUiBinder asideUiBinder = GWT.create(Aside.AsideUiBinder.class);

    public Aside(MySampleApplicationServiceAsync service) {

        initWidget(asideUiBinder.createAndBindUi(this));

        //xml
        final String XML_url = "/xslt";
        RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, XML_url);
        try {
            rb.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        String xml = response.getText();

                        try {
                            // parse the XML document into a DOM
                            Document messageDom = XMLParser.parse(xml);
                            StringBuilder sb = new StringBuilder("<ul style=\"list-style-type: none;\">");

                            // find the sender's display name in an attribute of the <from> tag
                            NodeList nodeList = messageDom.getElementsByTagName("Valute");
                            for (int i = 0; i < nodeList.getLength(); i++) {
                                sb.append("<li>")
                                        .append(nodeList.item(i).getFirstChild().getFirstChild().getNodeValue())
                                        .append("<span style=\"padding-left: 0.5em\">")
                                        .append(nodeList.item(i).getLastChild().getFirstChild().getNodeValue())
                                        .append("</span></li></br>");
                            }
                            sb.append("</ul>");

                            curs.setInnerHTML(sb.toString());

                        } catch (DOMException e) {
                            Window.alert("Could not parse XML document.");
                        }
                    } else {
                        Window.alert("Failed to send the message: "
                                + response.getStatusText());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert("Failed to send the message: "
                            + exception.getLocalizedMessage());
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }

        //JSON
        final String JSON_url = "/jsoup";

        JsonpRequestBuilder jsonpRequestBuilder = new JsonpRequestBuilder();
        jsonpRequestBuilder.setTimeout(10000);
        jsonpRequestBuilder.requestObject(JSON_url, new AsyncCallback<JavaScriptObject>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(JavaScriptObject result) {
                JsArray<News> jsArray = result.cast();

                StringBuilder sb = new StringBuilder("<ul style=\"list-style-type: none;\">");

                for (int i = 0; i < jsArray.length(); i++) {
                    sb.append("<li>")
                            .append(jsArray.get(i).getName())
                            .append("</li></br>");
                }

                sb.append("</ul>");

                news.setInnerHTML(sb.toString());
            }
        });
    }

    private static class News extends JavaScriptObject {

        protected News() { }

        public final native String getName() /*-{ return this.name; }-*/;
    }
}
