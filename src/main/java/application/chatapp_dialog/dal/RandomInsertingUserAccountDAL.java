package application.chatapp_dialog.dal;
import application.chatapp_dialog.User;
import application.chatapp_dialog.dummy.UserAccountGenerator;
import application.chatapp_dialog.security.EncryptPassword;

import java.io.FileWriter;
import java.io.IOException;
import java.io.SyncFailedException;
import java.security.spec.ECField;
import java.sql.*;
import java.util.Scanner;

public class RandomInsertingUserAccountDAL {
    public static void insertRandomAccounts(int number){
        Connection conn = UtilityDAL.getConnection();
        PreparedStatement ps = null;
        String insertQuery = "insert into user_accounts (username, password, email, salt) values (?,?,?,?)";
        String inserUserAccountInfo = "insert into user_account_info (account_id, displayname, dob, sex, address) values (?,?,?,?,?)";

        if (conn != null){
            FileWriter fw = null;
            try {
                fw = new FileWriter("src/main/resources/dummy/users.txt", true);
                fw.write("username, password, email, displayname, sex, address, dob \n");
                fw.close();
            } catch (IOException e) {
                System.out.println("Cannot open file");
            }
            create_account:
                for (int i = 0; i < number; i++){
                    String username = UserAccountGenerator.randomUsername();
                    String password = UserAccountGenerator.randomPassword();
                    String email = UserAccountGenerator.randomEmail();
                    String displayname = UserAccountGenerator.randomDisplayname();
                    Boolean sex = UserAccountGenerator.randomSex();
                    String address = UserAccountGenerator.randomAddress();
                    Date dob = UserAccountGenerator.randomCreateDOB();
                    StringBuilder sb = new StringBuilder();
                    sb.append(username + " ");
                    sb.append(password + " ");
                    sb.append(email + " ");
                    sb.append(displayname + " ");
                    sb.append(sex + " ");
                    sb.append(address + " ");
                    sb.append(dob + " ");
                    sb.append("\n");
                    try {
                        fw = new FileWriter("src/main/resources/dummy/users.txt", true);
                        fw.write(sb.toString());
                        fw.close();
                    } catch (IOException e) {
                        System.out.println("Cannot open file");
                        break create_account;
                    }

                    String salt = EncryptPassword.generateRandomSalt();
                    String hashedPass = EncryptPassword.hashPassword(password, salt);
                    try {
                        ps = conn.prepareStatement(insertQuery);
                        ps.setString(1,username);
                        ps.setString(2,hashedPass);
                        ps.setString(3,email);
                        ps.setString(4,salt);
                        int rowAdded = ps.executeUpdate();
                        if (rowAdded > 0){
                            ps = conn.prepareStatement("select id from user_accounts where username = ?");
                            ps.setString(1, username);
                            ResultSet rs = ps.executeQuery();
                            int user_id;
                            if (rs.next()) {
                                user_id = rs.getInt(1);

                                ps = conn.prepareStatement(inserUserAccountInfo);
                                ps.setInt(1, user_id);
                                ps.setString(2, displayname);
                                ps.setDate(3, dob);
                                ps.setBoolean(4, sex);
                                ps.setString(5, address);
                                if (ps.executeUpdate() > 0) {
                                    System.out.println("Add Info sucessfully");
                                }
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }

        }else {
            System.out.println("Cannot connect to database");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numbers = sc.nextInt();
        insertRandomAccounts(numbers);

    }
}
