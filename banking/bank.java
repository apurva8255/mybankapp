package banking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class bank {
    public static void main(String args[]) throws IOException {

        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        String name;
        int pass_code;
        int ch;

        while (true) {
            System.out.println("\n ->||    Welcome to InBank    ||<- \n");
            System.out.println("1) Create Account");
            System.out.println("2) Login Account");
            System.out.println("3) Exit");

            try {
                System.out.print("\n    Enter Input: ");
                ch = Integer.parseInt(sc.readLine());

                switch (ch) {
                case 1:
                    try {
                        System.out.print("Enter Unique UserName: ");
                        name = sc.readLine();
                        System.out.print("Enter New Password: ");
                        pass_code = Integer.parseInt(sc.readLine());

                        if (bankManagement.createAccount(name, pass_code)) {
                            System.out.println("MSG : Account Created Successfully!\n");
                        } else {
                            System.out.println("ERR : Account Creation Failed!\n");
                        }
                    }  catch (Exception e) {
                            e.printStackTrace();   
                            
                    }

                    break;

                case 2:
                    try {
                        System.out.print("Enter UserName: ");
                        name = sc.readLine();
                        System.out.print("Enter Password(Numeric Only): ");
                        pass_code = Integer.parseInt(sc.readLine());

                        if (bankManagement.loginAccount(name, pass_code)) {
                            System.out.println("MSG : Login Successful!\n");

                            int choice2;
                            do {
                                System.out.println("1. Deposit Money");
                                System.out.println("2. Withdraw Money");
                                System.out.println("3. Check Balance");
                                System.out.println("4. Transfer Money");
                                System.out.println("5. Exit");

                                System.out.print("Enter choice: ");
                                choice2 = Integer.parseInt(sc.readLine());

                                switch (choice2) {
                                    case 1: // Deposit
                                        System.out.print("Enter amount to deposit: ");
                                        double depAmt = Double.parseDouble(sc.readLine());
                                        if (bankManagement.depositMoney(name, depAmt)) {
                                            System.out.println("Deposit Successful!");
                                        } else {
                                            System.out.println("Deposit Failed!");
                                        }
                                        break;

                                    case 2: // Withdraw
                                        System.out.print("Enter amount to withdraw: ");
                                        double wAmt = Double.parseDouble(sc.readLine());
                                        if (bankManagement.withdrawMoney(name, wAmt)) {
                                            System.out.println("Withdrawal Successful!");
                                        } else {
                                            System.out.println("Withdrawal Failed! (Maybe insufficient balance)");
                                        }
                                        break;

                                    case 3: // Check Balance
                                        double bal = bankManagement.checkBalance(name);
                                        System.out.println("Your Balance: " + bal);
                                        break;

                                    case 4: // Logout
                                        System.out.println("MSG : Logout Successfully!\n");
                                        break;

                                    default:
                                        System.out.println("Invalid choice!");
                                }

                            } while (choice2 != 4);

                        } else {
                            System.out.println("ERR : Login Failed!\n");
                        }
                    } catch (Exception e) {
                        System.out.println(" ERR : Enter Valid Data :: Login Failed!\n");
                    }
                    break;

                case 3:
                    System.out.println("Exited Successfully!\n\n Thank You :)");
                    sc.close();
                    return;
                
                case 4:
                    System.out.print("Enter sender account name: ");
                    String sender = sc.readLine();
                
                    System.out.print("Enter receiver account name: ");
                    String receiver = sc.readLine();
                
                    System.out.print("Enter amount to transfer: ");
                    double amt = Double.parseDouble(sc.readLine());
                
                    bankManagement.transferMoney(sender, receiver, amt);
                    break;
                
                
                default:
                    System.out.println("Invalid Entry!\n");
                }
            } catch (Exception e) {
                System.out.println("Enter Valid Entry!");
            }
        }
    }
}
