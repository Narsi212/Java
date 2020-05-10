// Java program to create connection from MySQL database.

import java.sql.*;
import java.util.Scanner;

class MySQLdb implements InterfaceDb
{
	Connection connection;
	Scanner scanner = new Scanner(System.in);
	public Connection getConnection()
	{
		String url = "jdbc:mysql://165.22.14.77/dbNarsi?A autoReconnectionect=true&useSSL=false";
        System.out.print("Enter database Username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();
        try
        {
            connection = DriverManager.getConnection(url, userName, password); 
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        return connection;
	}
}