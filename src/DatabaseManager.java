/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socialpccontroller;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Praveen
 */
public class DatabaseManager {
    private String driver="com.mysql.jdbc.Driver";
    private String dbName="xxxxx";//Your Database name came here
    private String username="xxxxxx";//Your username came here
    private String password="xxxxxx";//Your password came here
    private String port="3306";
    private String url="jdbc:mysql://sql5.freesqldatabase.com:"+port+"/"+dbName;
    private Connection conn;
    
   public DatabaseManager() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException 
   {
        Class.forName(driver).newInstance();
        
        conn = DriverManager.getConnection(url,username,password);
        
        
   }
   public ResultSet selectQuery(String Query) throws SQLException
   {
        Statement pst=conn.createStatement();
        ResultSet temp=pst.executeQuery(Query);
        return temp;
   }
   public int updateQuery(String Query) throws SQLException
   {
       Statement pst=conn.createStatement();
        return pst.executeUpdate(Query);
        
   }
}
