package com.hotel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Date;


@WebServlet("/change_schedule_servlet")
public class change_schedule_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String s = request.getQueryString();
        String[] arr = s.split("&");


        String email = arr[0].split("=")[1];
        String new_schedule = arr[1].split("=")[1];

        String changeScheduleQuery = "update employee set working_schedule = '" + new_schedule + "' " +
                "where email = '" + email + "';";

        System.out.println(changeScheduleQuery);

        Connection connection;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL,USERNAME, PASSWORD);

            Statement confirmBookingStatement = connection.createStatement();
            confirmBookingStatement.execute(changeScheduleQuery); // now this booking is confirmed by the desk clerk


            request.getRequestDispatcher("index.html").forward(request, response);
            if(!connection.isClosed()) {
                System.out.println("Connected to database");
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }
}
