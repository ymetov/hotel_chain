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
import java.util.Calendar;

import org.json.simple.JSONObject;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

@WebServlet("/available_rooms_servlet")
public class available_rooms_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONObject[] jsonObject = new JSONObject[100];
        String final_json = "[";
        String json;

        Connection connection;

        String s = request.getQueryString();
        String[] arr = s.split("&");

        String hotel_id_str = arr[0].split("=")[1];
        String check_in = arr[1].split("=")[1];
        String check_out = arr[2].split("=")[1];

        //String hotel_id_str = request.getQueryString().split("=")[1];

        int hotel_id = Integer.parseInt(hotel_id_str);
        System.out.println(check_in + " " + check_out);

        String query = "select title, price, image, description_room from theroom where available = 1 and hotel_id=" + hotel_id + ";";
        String seasonQuery = "select * from season";

        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement seasonStatement = connection.createStatement();
            ResultSet seasonResultSet = seasonStatement.executeQuery(seasonQuery);

            String season_name = "";
            String season_start_date;
            String season_end_date;
            String season_coefficient = "";

            while(seasonResultSet.next()) {
                season_start_date = seasonResultSet.getString(2);
                season_end_date = seasonResultSet.getString(3);

                if(isWithRange(check_in, season_start_date, season_end_date)) {
                    season_name = seasonResultSet.getString(1);
                    season_coefficient = seasonResultSet.getString(4);
                }
            }

            System.out.println(season_name + " " + season_coefficient);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String title;
            int price;
            int capacity;
            String image_url;
            String description;

            int i = 0;
            while(resultSet.next()) {
                jsonObject[i] = new JSONObject();

                title = resultSet.getString(1);
                price = resultSet.getInt(2);
                image_url = resultSet.getString(3);
                description = resultSet.getString(4);

                jsonObject[i].put("title", title);
                jsonObject[i].put("price", price);
                jsonObject[i].put("image", image_url);
                jsonObject[i].put("description", description);

                jsonObject[0].put("season_name", season_name);
                jsonObject[0].put("season_coefficient", season_coefficient);


                json = jsonObject[i].toJSONString();
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

        Date testDate = testCal.getTime();
        Date startDate = startCal.getTime();
        Date endDate = endCal.getTime();


        System.out.println(testDate);
        System.out.println(startDate);
        System.out.println(endDate);
        return !(testDate.before(startDate) || testDate.after(endDate));
    }
}
