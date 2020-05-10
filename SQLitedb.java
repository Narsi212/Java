// Java program to create connection from SQLite database.

import java.sql.*;
import java.util.Scanner;

class SQLitedb implements InterfaceDb
{
	Connection connection;
	public Connection getConnection()
	{
		try
        {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:showroom.db";
            connection = DriverManager.getConnection(dbURL);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        return connection;
	}
}