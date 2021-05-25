package com.hotel;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONObject;

@WebServlet("/profile_servlet")
public class profile_servlet extends HttpServlet {
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
        String bookingQuery = "select * from thebooking where email = '" + email + "';";
        String seasonQuery = "select * from season";
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

            //ResultSet resultSetToViewBookingIsEmpty = statement.executeQuery(bookingQuery);
            ResultSet resultSet = statement.executeQuery(bookingQuery);

            /*if(!resultSetToViewBookingIsEmpty.next()) {
                System.out.println("No Bookings Yet");
                bookingHistoryJson[0] = new JSONObject();
                bookingHistoryJson[0].put("firstname", firstname);
                bookingHistoryJson[0].put("lastname", lastname);
                bookingHistoryJson[0].put("message", "No Booking Created Yet.");
                json = bookingHistoryJson[0].toJSONString();
                final_json += json + ",";
            }*/

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

                String season_name = "";
                String season_start_date;
                String season_end_date;
                String season_coefficient = "";

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


                Statement seasonStatement = connection.createStatement();
                ResultSet seasonResultSet = seasonStatement.executeQuery(seasonQuery);
                while(seasonResultSet.next()) {
                    season_start_date = seasonResultSet.getString(2);
                    season_end_date = seasonResultSet.getString(3);

                    if(isWithRange(check_in, season_start_date, season_end_date)) {
                        season_name = seasonResultSet.getString(1);
                        season_coefficient = seasonResultSet.getString(4);
                    }
                }

                bookingHistoryJson[i].put("season_name", season_name);
                bookingHistoryJson[i].put("season_coefficient", season_coefficient);

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

    public static boolean isWithRange(String test_date, String start_date, String end_date) {
        int test_date_year = Integer.parseInt(test_date.split("-")[0]);
        int test_date_month = Integer.parseInt(test_date.split("-")[1]);
        int test_date_day = Integer.parseInt(test_date.split("-")[2]);

        System.out.println(test_date_year + " " + test_date_month + " " + test_date_day);

        int start_date_year = Integer.parseInt(start_date.split("-")[0]);
        int start_date_month = Integer.parseInt(start_date.split("-")[1]);
        int start_date_day = Integer.parseInt(start_date.split("-")[2]);

        System.out.println(start_date_year + " " + start_date_month + " " + start_date_day);

        int end_date_year = Integer.parseInt(end_date.split("-")[0]);
        int end_date_month = Integer.parseInt(end_date.split("-")[1]);
        int end_date_day = Integer.parseInt(end_date.split("-")[2]);

        System.out.println(end_date_year + " " + end_date_month + " " + end_date_day);


        Calendar testCal = Calendar.getInstance();
        testCal.set(test_date_year, test_date_month-1, test_date_day);


        Calendar startCal = Calendar.getInstance();
        startCal.set(start_date_year, start_date_month-1, start_date_day);


        Calendar endCal = Calendar.getInstance();
        endCal.set(end_date_year, end_date_month-1, end_date_day);

        java.util.Date testDate = testCal.getTime();
        java.util.Date startDate = startCal.getTime();
        Date endDate = endCal.getTime();


        System.out.println(testDate);
        System.out.println(startDate);
        System.out.println(endDate);
        return !(testDate.before(startDate) || testDate.after(endDate));
    }
}
