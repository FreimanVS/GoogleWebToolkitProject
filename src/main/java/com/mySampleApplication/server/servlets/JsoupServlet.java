package com.mySampleApplication.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/jsoup")
public class JsoupServlet extends HttpServlet {

    private static final Gson JSON = new GsonBuilder().setPrettyPrinting().create();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Document doc;
        response.setContentType("text/html; charset=UTF8");
        try (PrintWriter pw = response.getWriter()) {

            doc = Jsoup.connect("https://www.rbc.ru").get();
            Elements elems = doc.getElementsByClass("main-feed__item js-main-reload-item");

            List news = new ArrayList<>(elems);

            List<ObjForJson> list = new ArrayList<>();
            news.forEach(n -> list.add(new ObjForJson(n.toString())));
            String jsonString = JSON.toJson(list);
            response.setContentType("application/json");
            pw.println(request.getParameter("callback") + "(" + jsonString + ")");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
