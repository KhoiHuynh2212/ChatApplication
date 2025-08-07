
package com.khoihuynh.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author khoih
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;  
    
    private Connection connection; 


    public static DatabaseConnection getInstance() {
        if(instance == null) {
            instance = new DatabaseConnection();
        } 
        return instance;
    } 
    
    public DatabaseConnection() {
        
    } 
    
    public void connectToDatabase() throws SQLException {
        String server = "127.0.0.1"; 
        String port = "3306";
        String database = "chat_application"; 
        String username = "root"; 
        String password = "khoihuynh2202"; 
        
        String url = "jdbc:mysql://" + server + ":" + port + "/" + database;
        connection = java.sql.DriverManager.getConnection(url, username, password);
        
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    } 
    
    
    
}
