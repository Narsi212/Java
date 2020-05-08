// Java program to do a Framework on CRUD operations using MySQL database.
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

class CRUD
{
	Scanner scanner = new Scanner(System.in);
	Connection connection;
	ResultSet resultSet;
	Statement stmt;
	int columnCount, choice, counter, rowCount;
	int headerCount = 0;
	ArrayList<String> columnNames = new ArrayList<String>();
	ArrayList<String> fieldList = new ArrayList<String>();
	String query, id;
	String header = "";
	String tableName;
	String activeStatus = "'A'";

	public CRUD()
	{
		String url = "jdbc:mysql://165.22.14.77/dbNarsi?A autoReconnectionect=true&useSSL=false";
		System.out.print("Enter database Username: ");
		String userName = scanner.nextLine();
		System.out.print("Enter your Password: ");
		String password = scanner.next();
		try
		{
			connection = DriverManager.getConnection(url, userName, password); 
			showTables();
			getColumns();
		}
		catch (Exception e)
		{
			printExceptionMessage(e);
		}
	}

	public void printExceptionMessage(Exception e)
	{
		System.err.println("Got an exception!");
		System.err.println(e.getMessage());
	}

	public void printInvalid()
	{
		System.out.println("\n*** Invalid choice! ***\n");
	}

	public void showTables()
	{
		int choice;
		String[] tableNames = new String[3];
		tableNames[0] = "Bank";
		tableNames[1] = "Hospital";
		tableNames[2] = "Travels";
		System.out.println("Choose a database to do CRUD operations.");	
		for (int counter = 0; counter < tableNames.length; counter ++)
		{
			System.out.println(counter + 1 + ". " + tableNames[counter]);
		}
		choice = readChoiceFromUser();
		if (choice >= 1 && choice <= 3)
		{
			tableName = tableNames[choice - 1];
		}
		else
		{
			printInvalid();
			showTables();
		}
	}

	public void getColumns()
	{
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
				fieldList.add(columnNames.get(counter).replace('_', ' '));
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

	public void printMessage(String message)
	{
		System.out.println("\n*** Record " + message + "! ***");
	}

	public void addNewRecord()
	{
		query = "INSERT INTO " + tableName + " VALUES(";
		try
		{
			for (counter = 0; counter < columnCount - 1; counter ++)
			{
				System.out.print("Enter " + fieldList.get(counter) + ": ");
				query += "'" + scanner.nextLine() + "', ";
			}
			query += activeStatus + ")";
			stmt.executeUpdate(query);
			printMessage("Inserted");
		}
		catch(Exception e)
		{
			printExceptionMessage(e);
		}
	}

	public void printHeader()
	{
		for (counter = 0; counter < columnCount - 1; counter ++)
		{
			if (counter != columnCount - 2)
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
				for (counter = 1; counter < columnCount; counter ++)
				{
					System.out.print(String.format("%-20s", resultSet.getString(counter)));
				}
				System.out.println();
			}
		}
		catch(Exception e)
		{
			printExceptionMessage(e);
		}
		if(headerCount == 0)
		{
			printMessage("not found");
		}
	}

	public void readAllRecords()
	{
		headerCount = 0;
		header = "";
		query = "select * from " + tableName + " where " + columnNames.get(columnNames.size() - 1) + " = " + activeStatus;
		printData();
	}
	
	public void readIdFromUser()
	{
		System.out.print("Enter " + fieldList.get(0) + ": ");
        id = scanner.next();
	}

	public void updateRecord()
	{
		String updatedValue;
		readIdFromUser();
		System.out.println("Please choose a field to update");
		for (counter = 1; counter < columnCount - 1; counter ++)
		{
			System.out.println(counter + ". " + fieldList.get(counter));
		}
		choice = readChoiceFromUser();
		if (choice > 0 && choice < columnCount - 1)
		{
			System.out.print("Enter updated " + fieldList.get(choice) + ": ");
			updatedValue = scanner.nextLine();
	        query = "UPDATE " + tableName + " SET " + columnNames.get(choice) + " = '" + updatedValue + "' where " + columnNames.get(0) + " = '" + id + "' and " + columnNames.get(columnNames.size() - 1) + " = " + activeStatus;
	        try
	        {
	        	rowCount = stmt.executeUpdate(query);
	        	if (rowCount == 0)
	        	{
	        		printMessage("not found");
	        	}
	        	else
	        	{
	        		printMessage("Updated");
	        	}
	        }
	        catch(Exception e)
	        {
	        	printExceptionMessage(e);
	        }
	    }
	    else
	    {
	    	printInvalid();
	    }
	}

	public void searchRecord()
	{
		readIdFromUser();
		headerCount = 0;
		header = "";
		query = "select * from " + tableName + " where " + columnNames.get(0) + " = '" + id + "' and " + columnNames.get(columnNames.size() - 1) + " = " + activeStatus;
		printData();
	}
	
	public void deleteRecord()
	{
		readIdFromUser();
        query = "UPDATE " + tableName + " SET " + columnNames.get(columnNames.size() - 1) + " = 'I' where " + columnNames.get(0) + " = '" + id + "' and " + columnNames.get(columnNames.size() - 1) + " = " + activeStatus;
        try
        {
        	rowCount = stmt.executeUpdate(query);
        	if (rowCount == 0)
        	{
        		printMessage("not found");
        	}
        	else
        	{
        		printMessage("Deleted");
        	}
        }
        catch(Exception e)
        {
        	printExceptionMessage(e);
        }
	}
	
	public void exitProgram()
	{
		try
		{
			if (connection != null)
			{
				connection.close();
			}
			System.out.println("Exited Successfully!");
			System.exit(0);
		}
		catch(Exception e)
		{
			printExceptionMessage(e);
		}
	}

	public int readChoiceFromUser()
	{
		try
		{
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();
		}
		catch (Exception e)
		{
			System.out.println("\nPlease choose correct option!\n");
			printExceptionMessage(e);
		}
		return choice;
	}

	public void showMenu()
	{
		while(true)
		{
			String welcome = "Welcome to " + tableName + " database!";
			drawLine(welcome);
			System.out.print("1. Add new record\n2. Display all records\n3. Update record\n4. Search a record\n5. Delete a record\n6. Exit\n");
			switch (readChoiceFromUser())
			{
				case 1: addNewRecord();
						break;
				case 2: readAllRecords();
						break;
				case 3: updateRecord();
						break;
				case 4: searchRecord();
						break;
				case 5: deleteRecord();
						break;
				case 6: exitProgram();
				default: printInvalid();
			}
			System.out.println();
		}
	}
}

class Framework
{
	public static void main(String[] args) 
	{
		CRUD crud = new CRUD();
		if (crud.connection != null)
		{
			crud.showMenu();
		}
		else
		{
			System.out.println("Please check your connection!");
		}
	}
}