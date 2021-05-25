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


@WebServlet("/deskclerk_cancel_servlet")
public class deskclerk_cancel_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String s = request.getQueryString();
        String[] arr = s.split("&");


        String hotel_name = arr[0].split("=")[1];
        String room_title = arr[1].split("=")[1].replace("%20", " ");
        String user_email = arr[2].split("=")[1];

        int hotel_id = 0;

        String[] hotel_names = { "Astana", "Almaty", "Shymkent", "Aqtobe" };
        for(int i = 0; i < hotel_names.length; i++) {
            if(hotel_names[i].equals(hotel_name)) {
                hotel_id = i + 1;
                break;
            }
        }

        String cancelBookingQuery = "update thebooking set booking_status = 'CANCELLED by the desk clerk' where email = '" + user_email +
                "' and hotel_name = '" + hotel_name + "' and room_title = '" + room_title + "';";
        String updateRoomQuery = "update theroom set available = 1 where hotel_id = " + hotel_id + " and title = '" +
                room_title + "';";

        System.out.println(cancelBookingQuery);
        System.out.println(updateRoomQuery);

        Connection connection;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL,USERNAME, PASSWORD);

            Statement cancelBookingStatement = connection.createStatement();
            cancelBookingStatement.execute(cancelBookingQuery); // now this booking is cancelled by the desk clerk

            Statement updateRoomStatement = connection.createStatement();
            updateRoomStatement.execute(updateRoomQuery); // now this room is available

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
