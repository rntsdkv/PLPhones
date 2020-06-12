package ru.prisonlife.plphones;

import java.sql.*;

public class JDBC {

    public static void main(String[] args) {
        String Url = "jdbc:mysql://localhost:3306:plplugin";
        String Username = "root";
        String Password = "1234";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(Url, Username, Password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM phones");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
