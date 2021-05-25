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


@WebServlet("/booking_servlet")
public class booking_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Cookie[] cookies = request.getCookies();

        String email = "";
        for (Cookie aCookie : cookies) {
            String name = aCookie.getName();
            String value = aCookie.getValue();
            if(name.equals("user_login")) {
                System.out.println(name + " = " + value);
                email = value;
            }
        }

        String s = request.getQueryString();
        String[] arr = s.split("&");

        String hotel_id = arr[0].split("=")[1];
        String check_in = arr[1].split("=")[1];
        String check_out = arr[2].split("=")[1];
        String guestcount = arr[3].split("=")[1];
        String room_title = arr[4].split("=")[1].replace("%20", " ");
        String reservDate = new Date().toString();

        String[] hotel_names = { "Astana", "Almaty", "Shymkent", "Aqtobe" };
        String hotel_name = hotel_names[Integer.parseInt(hotel_id) - 1];

        String updateRoomQuery = "update theroom set available = 0 where hotel_id = " + hotel_id + " and title = '" + room_title + "';";
        String selectRoomQuery = "select price, image, description_room from theroom where title='" +
                room_title + "' and hotel_id=" + hotel_id + ";";

        System.out.println(updateRoomQuery);
        Connection connection;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL,USERNAME, PASSWORD);

            Statement updateRoomStatement = connection.createStatement();
            updateRoomStatement.execute(updateRoomQuery); // now this room is not available

            Statement selectRoomStatement = connection.createStatement();
            ResultSet selectRoomResultSet = selectRoomStatement.executeQuery(selectRoomQuery);

            int room_price = 0;
            String room_image = "";
            String room_description = "";

            while(selectRoomResultSet.next()) {
                room_price = selectRoomResultSet.getInt(1);
                room_image = selectRoomResultSet.getString(2);
                room_description = selectRoomResultSet.getString(3);
            }




            Statement bookingStatement = connection.createStatement();
            String bookingQuery = "INSERT INTO thebooking values('" + email + "','" + check_in + "','" + check_out + "','" + guestcount + "','" +
                    reservDate + "','Awaits for desk clerk to confirm', '" + room_title + "','" + room_price + "','" +
                    room_image + "','" + room_description + "','" + hotel_name + "');";
            System.out.println(bookingQuery);
            bookingStatement.execute(bookingQuery);


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
