import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.sql.Date;

public class NewUserDriver 
{

	public static void main(String[] args) 
	{
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter first name: ");
		
		String firstName = input.nextLine();
		
		System.out.println("Enter last name: ");
		
		String lastName = input.nextLine();
		
		System.out.println("Enter SSN: ");
		
		String SSN = input.nextLine();
		
		
		System.out.println("Enter desired username: ");
		
		String username = input.nextLine();
		
		System.out.println("Enter desired password: ");
		
		String password = input.nextLine();
		
//		System.out.println("Enter birthdate: ");
//		
//		SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
//		
//		long birthday = input.nextLong();
//		
//		Date birthDate = new Date(birthday);
		
		
		
		//YYYY-MM-DD
		
		
		addUser(firstName, lastName, null, SSN, username, password);
		
		
		
	}
	
	public static void addUser(String firstName, String lastName, Date birthDate, String SSN,  String username, String password)
	{
		Connection connection = DatabaseConnection.getConnection();
		
		String callProcedure = "CALL DB2.NEW_USER(?, ?, ?, ?, ?, ?)";
		
		try (CallableStatement cs = connection.prepareCall(callProcedure))
		{
			cs.setString(1, firstName);
			cs.setString(2, lastName);
			cs.setDate(3, birthDate);
			cs.setString(4, SSN);
			cs.setString(5, username);
			cs.setString(6, password);
			
			cs.execute();
			System.out.println("User successfully added!");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("User creation unsuccessful");
		} 
	
	}


	
}
