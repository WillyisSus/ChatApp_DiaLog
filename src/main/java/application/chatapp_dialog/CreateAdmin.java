package application.chatapp_dialog;

import application.chatapp_dialog.dal.AdminAccountDAL;
import application.chatapp_dialog.dal.UtilityDAL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CreateAdmin {
    public static void main(String[] args) {
        Connection connection = UtilityDAL.getConnection();
        List<String> username =  new ArrayList<>(List.of("admin1", "admin2", "admin3"));
        List<String> email = new ArrayList<>(List.of("admin1@email.com", "admin2@email.com", "admin3@email.com"));
        List<String> password = new ArrayList<>(List.of("admin1", "admin2", "admin3"));
        for (int i = 0; i < 3; i ++){

            AdminAccountDAL.createNewAdmin(username.get(i), password.get(i), email.get(i),  username.get(i), connection);
        }
    }
}
