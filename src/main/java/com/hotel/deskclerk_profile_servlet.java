package com.hotel;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import org.json.simple.JSONObject;

@WebServlet("/deskclerk_profile_servlet")
public class deskclerk_profile_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONObject[] bookingHistoryJson = new JSONObject[100];
        String final_json = "[";
        String json;

        Connection connection;

        String emailQuery = request.getQueryString();
        String email = emailQuery.split("=")[1];

        //System.out.println(email);

        String userQuery = "select Name, Surname from user where Email_ID = '" + email + "';";
        String bookingQuery = "select * from thebooking where booking_status = 'Awaits for desk clerk to confirm' or booking_status = 'CANCELLED by the desk clerk';";

        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String firstname = "";
            String lastname = "";

            Statement userStatement = connection.createStatement();
            ResultSet userResultSet = userStatement.executeQuery(userQuery);

            while(userResultSet.next()) {
                firstname = userResultSet.getString(1);
                lastname = userResultSet.getString(2);
            }

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(bookingQuery);


            String user_email;
            String check_in;
            String check_out;
            String guestcount;
            String reservationDate;
            String booking_status;
            String room_title;
            String room_price;
            String room_image;
            String room_description;
            String hotel_name;

            int i = 0;
            while(resultSet.next()) {

                bookingHistoryJson[i] = new JSONObject();


                user_email = resultSet.getString(1);
                check_in = resultSet.getString(2);
                check_out = resultSet.getString(3);
                guestcount = resultSet.getString(4);
                reservationDate = resultSet.getString(5);
                booking_status = resultSet.getString(6);
                room_title = resultSet.getString(7);
                room_price = resultSet.getString(8);
                room_image = resultSet.getString(9);
                room_description = resultSet.getString(10);
                hotel_name = resultSet.getString(11);

                bookingHistoryJson[i].put("user_email", user_email);
                bookingHistoryJson[i].put("check_in", check_in);
                bookingHistoryJson[i].put("check_out", check_out);
                bookingHistoryJson[i].put("guestcount", guestcount);
                bookingHistoryJson[i].put("reservationDate", reservationDate);
                bookingHistoryJson[i].put("booking_status", booking_status);
                bookingHistoryJson[i].put("room_title", room_title);
                bookingHistoryJson[i].put("room_price", room_price);
                bookingHistoryJson[i].put("room_image", room_image);
                bookingHistoryJson[i].put("room_description", room_description);
                bookingHistoryJson[i].put("hotel_name", hotel_name);
                bookingHistoryJson[0].put("firstname", firstname);
                bookingHistoryJson[0].put("lastname", lastname);

                json = bookingHistoryJson[i].toJSONString();
                final_json += json + ",";

                i++;
            }



            if(!connection.isClosed()) {
                System.out.println("Connected to database");
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e);
        }



        final_json = final_json.substring(0, final_json.length() - 1);
        final_json += "]";

        response.getWriter().append(final_json);
    }
}
