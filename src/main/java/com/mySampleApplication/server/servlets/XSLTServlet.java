package com.mySampleApplication.server.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

@WebServlet("/xslt")
public class XSLTServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String XSL = "/xslt/XML_daily.asp.xsl";
        String XML = "http://www.cbr.ru/scripts/XML_daily.asp";

        StreamSource stylesource = new StreamSource(getServletContext().getResourceAsStream(XSL));
        StreamSource xmlsource = new StreamSource(new URL(XML).openStream());

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter pw = response.getWriter();
            FileInputStream fis = new FileInputStream("./result.txt")) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);

            transformer.transform(xmlsource, new StreamResult(new File("./result.txt")));

            StringBuilder sb = new StringBuilder();
            while (fis.available() != 0) {
                sb.append((char)fis.read());
            }

            pw.println(sb.toString());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
