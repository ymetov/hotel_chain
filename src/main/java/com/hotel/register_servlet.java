package com.hotel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;



@WebServlet("/register_servlet")
public class register_servlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://uppbs7nmhiq8mecx:cZ0Tg5xaB4kMT0ICpotN@bbay1ia6y6wvssmwldks-mysql.services.clever-cloud.com:3306/bbay1ia6y6wvssmwldks";
    private static final String USERNAME = "uppbs7nmhiq8mecx";
    private static final String PASSWORD = "cZ0Tg5xaB4kMT0ICpotN";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = "MD5("+request.getParameter("password") + ")";
        String fname = request.getParameter("firstName");
        String lname = request.getParameter("lastName");
        String email = request.getParameter("email");
        String query = "select * from user";
        boolean check = false;
        Connection connection;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL,USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1));
                if(resultSet.getString(1).equals(email))
                    check = true;
            }

            statement.execute("INSERT INTO user(Name, Surname, Email_ID, Pswd)" + " VALUES('" + fname + "','" + lname+ "','" + email + "'," +  password + ");");
            if(!connection.isClosed()) {
                System.out.println("Connected to database");
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e);
        }

        if(check) {

            request.getRequestDispatcher("errorRegister.html").forward(request, response);
        } else {
            request.getRequestDispatcher("successful_register.html").forward(request, response);
        }


    }


}
