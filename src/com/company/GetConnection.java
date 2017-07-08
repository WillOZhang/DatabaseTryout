package com.company;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by Will on 2017-07-07.
 */
public class GetConnection {
    private static Connection conn;
    private static Statement stmt;

    private static final String databasePassword = "DatabaseTryout";
    private static final String databaseUsername = "root";
    private static final String DATABASE_NAME = "studentInfo";

    public static void main(String[] args) {
        System.out.printf("Press 1 to access all student data \n");
        System.out.printf("Press 2 to add a new student info \n");
        System.out.printf("Press 3 to update a student's info \n");
        System.out.printf("Press 4 to remove a student info from the database \n");

        Scanner scanner = new Scanner(System.in);
        int userEnter = 0;
        userEnter = scanner.nextInt();

        connectToDatabase();

        if (userEnter == 1) {
            displayDatabase();
        } else if (userEnter == 2) {
            String studentName = "noName";
            int studentNumber = -999;
            System.out.printf("What's the student's name?");
            studentName = scanner.next();
            System.out.printf("What about the student number?");
            studentNumber = scanner.nextInt();
            if (!studentName.equals("noName") && studentNumber != -999) {
                String insert = "INSERT INTO " + DATABASE_NAME + "(studentName, studentNumber) VALUES ("
                        + "'" + studentName + "'" + "," +studentNumber + ");";
                try {
                    PreparedStatement pst = conn.prepareStatement(insert);
                    pst.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                displayDatabase();
            } else
                System.out.printf("Invalid Input!");
        } else if (userEnter == 3) {
            displayDatabase();
            String studentName = "noName";
            int studentNumber = -999;
            System.out.printf("What's the student's name?");
            studentName = scanner.next();
            System.out.printf("What about the new student number of this student?");
            studentNumber = scanner.nextInt();

            try {
                String update = "UPDATE " + DATABASE_NAME + " set studentNumber=?  where studentName=?";
                PreparedStatement pst = conn.prepareStatement(update);
                pst.setString(2, studentName);
                pst.setInt(1, studentNumber);
                pst.executeUpdate();
                displayDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (userEnter == 4) {
            displayDatabase();
            int studentNumber = -999;
            System.out.printf("What's the student number of the student you wanna remove?");
            studentNumber = scanner.nextInt();

            try {
                String sql3 = "DELETE FROM " + DATABASE_NAME + " where studentNumber=?";
                PreparedStatement pst = conn.prepareStatement(sql3);
                pst.setInt(1, studentNumber);
                pst.executeUpdate();
                displayDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            System.out.printf("Invalid Input!");

        closeStreams();
    }

    private static void displayDatabase() {
        try {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + DATABASE_NAME + ";");
            System.out.println("Student Name: " + "\t" + "Student Number");
            while (resultSet.next()) {
                System.out.print(resultSet.getString(1) + "\t");
                System.out.print(resultSet.getInt(2) + "\t");
                System.out.println();
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connectToDatabase() {
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/mysql"; //JDBC的URL

            //调用DriverManager对象的getConnection()方法，获得一个Connection对象
            conn = DriverManager.getConnection(url, databaseUsername, databasePassword);

            //创建一个Statement对象
            stmt = conn.createStatement();

            System.out.print("Successfully connected to database！");
            stmt.execute("USE DatabaseTry;");

        } catch (SQLException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e1){
            System.out.println("找不到MySQL驱动!");
        }
    }

    private static void closeStreams() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
