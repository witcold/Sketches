package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Response {
	public static String url = "jdbc:h2:tcp://localhost/D:/Repo/VK";
	public static String user = "dbadmin";
	public static String password = "dbpassword";
	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");

	public User[] response;

	public void saveToDb() throws SQLException, ParseException {
		Connection c = DriverManager.getConnection(url, user, password);
		if (c != null) {
			int i, result = 0;
			for (i = 0; i < response.length; i++) {
				PreparedStatement s = c
						.prepareStatement("INSERT INTO VK (ID, Firstname, Lastname, Sex, Birthdate, Country, City, Relation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				s.setInt(1, response[i].uid);
				s.setString(2, response[i].first_name);
				s.setString(3, response[i].last_name);
				s.setInt(4, response[i].sex);
				try {
					s.setTimestamp(5, new Timestamp(sdf.parse(response[i].bdate).getTime()));
				} catch (Exception e) {
					s.setNull(5, java.sql.Types.TIMESTAMP);
				}
				s.setInt(6, response[i].country);
				s.setInt(7, response[i].city);
				s.setInt(8, response[i].relation);
				result += s.executeUpdate();
			}
			System.out.println("Processed " + result + " from " + i + " users");
		}
	}
}
