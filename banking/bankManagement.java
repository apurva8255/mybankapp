
package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class bankManagement {

    // Create Account
    public static boolean createAccount(String name, int pass_code) {
    Connection con = null;
    PreparedStatement ps = null;

    try {
        con = connection.getConnection();
        String sql = "INSERT INTO customer (cname, balance, pass_code) VALUES (?, ?, ?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, "0");   // balance as string since your schema has VARCHAR
        ps.setInt(3, pass_code);

        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();  // see real error
        return false;
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}


    // Login Account
    public static boolean loginAccount(String name, int pass_code) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = connection.getConnection();
        String sql = "SELECT * FROM customer WHERE cname = ? AND pass_code = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, pass_code);

        rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Welcome, " + rs.getString("cname") + "!");
            System.out.println("Account No: " + rs.getInt("ac_no"));
            System.out.println("Balance: " + rs.getString("balance"));
            return true;
        } else {
            return false;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}

   //DEpopsit moneeyy
public static boolean depositMoney(String name, double amount) {
    Connection con = null;
    PreparedStatement ps = null;

    try {
        con = connection.getConnection();

        // increase balance
        String sql = "UPDATE customer SET balance = balance + ? WHERE cname = ?";
        ps = con.prepareStatement(sql);
        ps.setDouble(1, amount);
        ps.setString(2, name);

        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}
// Withdraw Money
public static boolean withdrawMoney(String uname, double amount) {
    try {
        Connection con = connection.getConnection();

        String q1 = "SELECT balance FROM customer WHERE cname = ?";
        PreparedStatement ps1 = con.prepareStatement(q1);
        ps1.setString(1, uname);
        ResultSet rs = ps1.executeQuery();

        if (rs.next()) {
            double currBal = Double.parseDouble(rs.getString("balance"));

            if (currBal >= amount) {
                double newBal = currBal - amount;

                String q2 = "UPDATE customer SET balance = ? WHERE cname = ?";
                PreparedStatement ps2 = con.prepareStatement(q2);
                ps2.setString(1, String.valueOf(newBal));
                ps2.setString(2, uname);

                int rows = ps2.executeUpdate();
                return rows > 0;
            } else {
                System.out.println("ERR : Insufficient Balance!");
                return false;
            }
        }
    } catch (Exception e) {
        System.out.println("ERR : Withdrawal Failed! " + e.getMessage());
    }
    return false;
}

// Check Balance
public static double checkBalance(String uname) {
    try {
        Connection con = connection.getConnection();

        String q1 = "SELECT balance FROM customer WHERE cname = ?";
        PreparedStatement ps1 = con.prepareStatement(q1);
        ps1.setString(1, uname);
        ResultSet rs = ps1.executeQuery();

        if (rs.next()) {
            return Double.parseDouble(rs.getString("balance"));
        }
    } catch (Exception e) {
        System.out.println("ERR : Balance Check Failed! " + e.getMessage());
    }
    return -1; // means error
}
// Transfer money between two accounts (Database version)
public static boolean transferMoney(String fromName, String toName, double amount) {
    Connection con = null;
    PreparedStatement ps1 = null, ps2 = null, ps3 = null;
    ResultSet rs1 = null, rs2 = null;

    try {
        con = connection.getConnection();
        con.setAutoCommit(false); // Start transaction

        // 1. Get sender balance
        String q1 = "SELECT balance FROM customer WHERE cname = ?";
        ps1 = con.prepareStatement(q1);
        ps1.setString(1, fromName);
        rs1 = ps1.executeQuery();
        if (!rs1.next()) {
            System.out.println("❌ Sender account not found!");
            return false;
        }
        double fromBal = Double.parseDouble(rs1.getString("balance"));
        if (fromBal < amount) {
            System.out.println("❌ Insufficient funds!");
            return false;
        }

        // 2. Get receiver balance
        String q2 = "SELECT balance FROM customer WHERE cname = ?";
        ps2 = con.prepareStatement(q2);
        ps2.setString(1, toName);
        rs2 = ps2.executeQuery();
        if (!rs2.next()) {
            System.out.println("❌ Receiver account not found!");
            return false;
        }
        double toBal = Double.parseDouble(rs2.getString("balance"));

        // 3. Update balances
        String q3 = "UPDATE customer SET balance = ? WHERE cname = ?";
        ps3 = con.prepareStatement(q3);

        ps3.setString(1, String.valueOf(fromBal - amount));
        ps3.setString(2, fromName);
        ps3.executeUpdate();

        ps3.setString(1, String.valueOf(toBal + amount));
        ps3.setString(2, toName);
        ps3.executeUpdate();

        con.commit();
        System.out.println("✅ ₹" + amount + " transferred from " + fromName + " to " + toName);
        return true;

    } catch (Exception e) {
        try { if (con != null) con.rollback(); } catch (Exception ex) {}
        System.out.println("❌ Transfer failed! " + e.getMessage());
        return false;
    } finally {
        try { if (rs1 != null) rs1.close(); } catch (Exception e) {}
        try { if (rs2 != null) rs2.close(); } catch (Exception e) {}
        try { if (ps1 != null) ps1.close(); } catch (Exception e) {}
        try { if (ps2 != null) ps2.close(); } catch (Exception e) {}
        try { if (ps3 != null) ps3.close(); } catch (Exception e) {}
        try { if (con != null) con.setAutoCommit(true); con.close(); } catch (Exception e) {}
    }
}





}

