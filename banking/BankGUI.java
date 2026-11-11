package banking;

import javax.swing.*;
import java.awt.*;

public class BankGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankGUI().createWelcomeFrame());
    }

    // ---------- THEME ----------
    private final Color primary = new Color(0x1E88E5);
    private final Color background = new Color(0xF5F5F5);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
    private final Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

    // ---------- WELCOME SCREEN ----------
    private void createWelcomeFrame() {
        JFrame frame = new JFrame("InBank - Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 220);
        frame.getContentPane().setBackground(background);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Welcome to InBank", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(primary);

        JButton loginBtn = createStyledButton("Login");
        JButton signupBtn = createStyledButton("Sign Up");

        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        btnPanel.setBackground(background);
        btnPanel.add(loginBtn);
        btnPanel.add(signupBtn);

        frame.add(title, BorderLayout.NORTH);
        frame.add(btnPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            frame.dispose();
            createLoginFrame();
        });

        signupBtn.addActionListener(e -> {
            frame.dispose();
            createSignupFrame();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------- LOGIN SCREEN ----------
    public void createLoginFrame() {
        JFrame frame = new JFrame("InBank - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 220);
        frame.getContentPane().setBackground(background);
        frame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = createStyledButton("Login");

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(new JLabel());
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String uname = userField.getText();
            int pass = Integer.parseInt(new String(passField.getPassword()));
            if (bankManagement.loginAccount(uname, pass)) {
                frame.dispose();
                showAccountMenu(uname);
            } else {
                JOptionPane.showMessageDialog(frame, "Login Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------- SIGNUP SCREEN ----------
    private void createSignupFrame() {
        JFrame frame = new JFrame("InBank - Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 220);
        frame.getContentPane().setBackground(background);
        frame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Choose Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Choose Password:");
        JPasswordField passField = new JPasswordField();

        JButton signupBtn = createStyledButton("Create Account");

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(new JLabel());
        frame.add(signupBtn);

        signupBtn.addActionListener(e -> {
            String uname = userField.getText();
            int pass = Integer.parseInt(new String(passField.getPassword()));
            if (bankManagement.createAccount(uname, pass)) {
                JOptionPane.showMessageDialog(frame, "Account Created Successfully!");
                frame.dispose();
                createLoginFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Account Creation Failed!");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------- ACCOUNT MENU ----------
    private void showAccountMenu(String uname) {
        JFrame frame = new JFrame("InBank - Account Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);
        frame.getContentPane().setBackground(background);
        frame.setLayout(new GridLayout(3, 2, 10, 10));

        JButton depositBtn = createStyledButton("Deposit");
        JButton withdrawBtn = createStyledButton("Withdraw");
        JButton balanceBtn = createStyledButton("Check Balance");
        JButton transferBtn = createStyledButton("Transfer Money");
        JButton logoutBtn = createStyledButton("Logout");

        frame.add(depositBtn);
        frame.add(withdrawBtn);
        frame.add(balanceBtn);
        frame.add(transferBtn);
        frame.add(logoutBtn);

        depositBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog("Enter deposit amount:");
            if (amtStr != null) {
                double amt = Double.parseDouble(amtStr);
                if (bankManagement.depositMoney(uname, amt)) {
                    JOptionPane.showMessageDialog(frame, "Deposit Successful!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Deposit Failed!");
                }
            }
        });

        withdrawBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog("Enter withdrawal amount:");
            if (amtStr != null) {
                double amt = Double.parseDouble(amtStr);
                if (bankManagement.withdrawMoney(uname, amt)) {
                    JOptionPane.showMessageDialog(frame, "Withdrawal Successful!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient Balance!");
                }
            }
        });

        balanceBtn.addActionListener(e -> {
            double bal = bankManagement.checkBalance(uname);
            JOptionPane.showMessageDialog(frame, "Your Balance: ₹" + bal);
        });

        transferBtn.addActionListener(e -> {
            String receiver = JOptionPane.showInputDialog("Enter receiver account name:");
            String amtStr = JOptionPane.showInputDialog("Enter amount to transfer:");
            if (receiver != null && amtStr != null) {
                try {
                    double amt = Double.parseDouble(amtStr);
                    if (bankManagement.transferMoney(uname, receiver, amt)) {
                        JOptionPane.showMessageDialog(frame, "✅ ₹" + amt + " transferred to " + receiver);
                    } else {
                        JOptionPane.showMessageDialog(frame, "❌ Transfer failed! Check balance or receiver name.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Enter a valid number!");
                }
            }
        });

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            createWelcomeFrame();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------- HELPER METHOD ----------
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(primary);
        btn.setForeground(Color.WHITE);
        btn.setFont(buttonFont);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
    }
}
