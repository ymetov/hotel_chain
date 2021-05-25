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

@WebServlet("/room_servlet")
public class room_servlet extends HttpServlet {
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
        String query = "select * from room";
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            String title;
            int price;
            int size;
            int capacity;
            String view;
            String image_url;

            int i = 0;
            while(resultSet.next()) {
                jsonObject[i] = new JSONObject();
                
                title = resultSet.getString(1);
                price = resultSet.getInt(2);
                size = resultSet.getInt(3);
                capacity = resultSet.getInt(4);
                view = resultSet.getString(5);
                image_url = resultSet.getString(6);

                jsonObject[i].put("title", title);
                jsonObject[i].put("price", price);
                jsonObject[i].put("capacity", capacity);
                jsonObject[i].put("size", size);
                jsonObject[i].put("image", image_url);

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
}
