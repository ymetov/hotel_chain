package com.hotel;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static com.hotel.MD5.getMd5;

@WebServlet("/login_servlet")
public class login_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String password =  getMd5(request.getParameter("password"));
        String email = request.getParameter("email");
        Connection connection;
        String query = "select * from user";
        boolean check = false;
        Cookie cookie;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL,USERNAME, PASSWORD);

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                // index 3 for email, index 4 for password
                System.out.println(resultSet.getString(3) + resultSet.getString(4));
                if(resultSet.getString(3).equals(email) && resultSet.getString(4).equals(password)) {
                    check = true;
                    System.out.println("founds");

                    break;
                }

            }



            if(!connection.isClosed()) {
                System.out.println("Connected to database");
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e);
        }

        if(check) {
            // creating a cookie with name user_login and value as the email
            String name = "user_login";
            String value = email;
            cookie = new Cookie(name, value);
            cookie.setMaxAge(60 * 30); // cookie valid for 30 minutes
            response.addCookie(cookie);
            
            request.getRequestDispatcher("index.html").forward(request, response);
        } else {
            request.getRequestDispatcher("error_login.html").forward(request, response);
        }
    }


}
