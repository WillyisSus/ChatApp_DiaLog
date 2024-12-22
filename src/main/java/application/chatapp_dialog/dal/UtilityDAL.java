package application.chatapp_dialog.dal;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class UtilityDAL {
    private static String username = "postgres";
    private static String password = "123456";
    private static String port = "8010";
    private static String database = "dialog_db";
    public static Connection getConnection(){
        String configPath = "config.properties";
        Properties props;
        props = new Properties();

        try(FileInputStream fi = new FileInputStream(configPath)) {
            props.load(fi);
            username = props.getProperty("username");
            password = props.getProperty("password");
            port = props.getProperty("port");
            database = props.getProperty("database");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        Connection conn = null;
        String url = "jdbc:postgresql://localhost:" + port + "/" + database ;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, username, password);
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
