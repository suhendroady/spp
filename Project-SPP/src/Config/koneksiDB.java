/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class koneksiDB {
    private static Connection mysqlconfig;
        public static Connection connectDB()throws SQLException{
            try {
                String url="jdbc:mysql://localhost:3306/db_spp"; //url database
                String user="root"; //user database
                String pass=""; //password database
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                mysqlconfig=DriverManager.getConnection(url, user, pass);            
            } catch (SQLException e) {
                System.err.println("koneksi gagal "+e.getMessage()); //perintah menampilkan error pada koneksi
            }
            return mysqlconfig;
        }
}
