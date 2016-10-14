/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.just.myapplication.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.*;

import jdk.nashorn.internal.parser.JSONParser;

public class MyServlet extends HttpServlet {

    private final static String URLusa = "http://www.nbrb.by/API/ExRates/Currencies/145";
//    private final static String URLeuro = "http://www.nbrb.by/API/ExRates/Currencies/19";


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        URL urlusa = new URL(URLusa);
//        URL urleuro = new URL(URLeuro);

        BufferedReader brusa = new BufferedReader(new InputStreamReader(urlusa.openConnection().getInputStream()));
        StringBuilder textusa = new StringBuilder();
        String lineusa;

//        BufferedReader breuro = new BufferedReader(new InputStreamReader(urleuro.openConnection().getInputStream()));
//        StringBuilder texteoru = new StringBuilder();
//        String lineeuro;

        while (null != (lineusa = brusa.readLine())) {
            textusa.append(lineusa);
        }


        String parsedText;
        parsedText = new Parsers().parseNBRBUrl(new String(textusa));
        resp.setContentType("text/plain");
        resp.getWriter().println(parsedText);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        resp.getWriter().println("Hello " + name);
    }
}
