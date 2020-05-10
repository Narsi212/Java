// Java program to do create, read operations on MySQL, SQLite databases.
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

class CRUDOperations
{
    Scanner scanner = new Scanner(System.in);
    Connection connection = null;
    ResultSet resultSet;
    Statement stmt;
    int columnCount, choice, counter, rowCount;
    int headerCount = 0;
    ArrayList<String> columnNames = new ArrayList<String>();
    String query;
    String header;
    String tableName;

    public CRUDOperations(String className)
    {
        try
        {
        	InterfaceDb crud = (InterfaceDb)Class.forName(className).newInstance();
            connection = crud.getConnection();
            getColumns();
        }
        catch(Exception e)
        {
            printExceptionMessage(e);
        }
    }

    public void printExceptionMessage(Exception e)
    {
        System.err.println("Got an exception!");
        System.err.println(e.getMessage());
    }

    public void getColumns()
    {
    	System.out.print("Enter table name: ");
    	tableName = scanner.nextLine();
        query = "SELECT * FROM " + tableName;
        try
        {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            ResultSetMetaData metadata = resultSet.getMetaData();
            columnCount = metadata.getColumnCount();
            for (counter = 0; counter < columnCount; counter ++)
            {
                columnNames.add(metadata.getColumnName(counter + 1));
            }
        }
        catch (Exception e)
        {
            printExceptionMessage(e);
        }
    }

    public void drawLine(String header)
    {
        System.out.println(header);
        for(counter = 0; counter < header.length(); counter ++)
        {
            System.out.print('-');
        }
        System.out.println();
    }

    public void create()
    {
        query = "INSERT INTO " + tableName + " VALUES(";
        try
        {
            for (counter = 0; counter < columnCount; counter ++)
            {
                System.out.print("Enter " + columnNames.get(counter) + ": ");
                if (counter < columnCount - 1)
                {
                    query += "'" + scanner.nextLine() + "', ";
                }
                else
                {
                    query += "'" + scanner.nextLine() + "')";
                }
            }
            stmt.executeUpdate(query);
            System.out.println("~~~ Record Inserted! ~~~");
        }
        catch(Exception e)
        {
            printExceptionMessage(e);
        }
    }

    public void printHeader()
    {
        for (counter = 0; counter < columnCount; counter ++)
        {
            if (counter != columnCount - 1)
            {
                header += String.format("%-20s", columnNames.get(counter));
            }
            else
            {
                header += String.format(columnNames.get(counter));
            }
        }
        drawLine(header);
    }

    public void printData()
    {
        try
        {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while(resultSet.next())
            {
                if (headerCount == 0)
                {
                    printHeader();
                    headerCount = 1;
                }
                for (counter = 0; counter < columnCount; counter ++)
                {
                    System.out.print(String.format("%-20s", resultSet.getString(counter + 1)));
                }
                System.out.println();
            }
        }
        catch(Exception e)
        {
            printExceptionMessage(e);
        }
    }

    public void read()
    {
        headerCount = 0;
        header = "";
        query = "select * from " + tableName;
        printData();
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String className = null;
        if(args.length == 1)
        {
            className = args[0];
        }
        else
        {
            System.out.print("Enter class name: ");
            className = scanner.next();
        }
        CRUDOperations cr = new CRUDOperations(className);
        cr.read();
    }
}