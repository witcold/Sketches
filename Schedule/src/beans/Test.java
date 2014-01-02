package beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;


public class Test {

	static String url = "jdbc:h2:file:D:/Repo/Schedule";
	static SimpleDateFormat sdf = new SimpleDateFormat();

	public static void select() throws SQLException {
		Connection c = DriverManager.getConnection(url);
		if (c != null) {
			PreparedStatement s = c.prepareStatement("SELECT * FROM Tasks;");
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				Task t = new Task();
				t.id = rs.getInt("ID");
				t.name = rs.getString("Name");
				t.started = rs.getTimestamp("Started");
				t.finished = rs.getTimestamp("Finished");
				System.out.println(t.id + " : " + t.name + " [" + sdf.format(t.started)
						+ " , " + t.finished + " ]");
			}
			c.close();
		}
	}

	public static void insert(Task t) throws SQLException {
		Connection c = DriverManager.getConnection(url);
		if (c != null) {
			PreparedStatement p = c.prepareStatement("INSERT INTO Tasks VALUES (?, ?, ?, ?)");
			p.setInt(1, t.id);
			p.setString(2, t.name);
			if (t.started != null) {
				p.setTimestamp(3, new Timestamp(t.started.getTime()));
			}
			else {
				p.setNull(3, java.sql.Types.TIMESTAMP);
			}
			if (t.finished != null) {
				p.setTimestamp(4, new Timestamp(t.finished.getTime()));
			}
			else {
				p.setNull(4, java.sql.Types.TIMESTAMP);
			}
			int i = p.executeUpdate();
			System.out.println(i + " lines inserted");
			c.close();
		}
	}

	public static void delete(int id) throws SQLException {
		Connection c = DriverManager.getConnection(url);
		if (c != null) {
			PreparedStatement p = c.prepareStatement("DELETE FROM Tasks WHERE ID = ?");
			p.setInt(1, id);
			int i = p.executeUpdate();
			System.out.println(i + " lines deleted");
			c.close();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		Class.forName("org.h2.Driver");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print(">: ");
			String s = br.readLine();
			switch (s) {
			case "select":
				select();
				break;
			case "insert":
				Task t = new Task();
				t.id = scanner.nextInt();
				t.name = br.readLine();
				t.started = Calendar.getInstance().getTime();
				insert(t);
				break;
			case "delete":
				int id = scanner.nextInt();
				delete(id);
				break;
			default:
				scanner.close();
				return;
			}
		}
	}

}
