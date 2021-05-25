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

@WebServlet("/employee_profile_servlet")
public class employee_profile_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONObject[] employeeProfiles = new JSONObject[100];
        String final_json = "[";
        String json;

        Connection connection;

        String emailQuery = request.getQueryString();
        String email = emailQuery.split("=")[1];

        //System.out.println(email);

        String employeeQuery = "select * from employee where email='" + email + "';";
        System.out.println(employeeQuery);
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(employeeQuery);


            String employee_email;
            String firstname;
            String lastname;
            String gender;
            int salary;
            String address;
            String working_schedule;
            String hotel_name;

            int i = 0;
            while(resultSet.next()) {

                employeeProfiles[i] = new JSONObject();


                employee_email = resultSet.getString(1);
                firstname = resultSet.getString(2);
                lastname = resultSet.getString(3);
                gender = resultSet.getString(4);
                salary = resultSet.getInt(5);
                address = resultSet.getString(6);
                working_schedule = resultSet.getString(7);
                hotel_name = resultSet.getString(8);

                employeeProfiles[i].put("firstname", firstname);
                employeeProfiles[i].put("lastname", lastname);
                employeeProfiles[i].put("employee_email", employee_email);
                employeeProfiles[i].put("gender", gender);
                employeeProfiles[i].put("salary", salary);
                employeeProfiles[i].put("address", address);
                employeeProfiles[i].put("working_schedule", working_schedule);
                employeeProfiles[i].put("hotel_name", hotel_name);

                json = employeeProfiles[i].toJSONString();
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
