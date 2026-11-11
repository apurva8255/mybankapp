package banking;

import java.sql.Connection;
import java.sql.DriverManager;

public class connection {
    static Connection con;

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String password = System.getenv("BANK_DB_PASSWORD");
                if (password == null || password.isEmpty()) {
                    throw new IllegalStateException("BANK_DB_PASSWORD is not set");
                }
                con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bank", // DB name
                    "root",                              // username
                    password
                );
                System.out.println("Database Connected Successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
