import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.net.ssl.HttpsURLConnection;

import beans.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	static String api = "https://api.vk.com/method/";
	static String method = "users.get";

	/**
	 * Returns comma-separated string with numbers
	 * 
	 * @param from
	 * @param to
	 *            (not included)
	 * @return
	 */
	public static String getNumbers(int from, int to) {
		String result = "" + from;
		while (++from < to)
			result += "," + from;
		return result;
	}

	public static InputStream getJsonUsers(int from, int count)
			throws IOException {
		// Prepare HTTP connection
		URL url = new URL(api + method);
		HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
		c.setRequestMethod("POST");
		c.setDoOutput(true);
		// Write parameters
		String ids = "user_ids=" + getNumbers(from, from + count);
		String fields = "fields=sex,bdate,city,country,photo_50,photo_max_orig,contacts,connections,site,relation";
		DataOutputStream os = new java.io.DataOutputStream(c.getOutputStream());
		os.writeBytes(ids + "&" + fields);
		os.flush();
		os.close();
		// Get response
		/*
		 * BufferedReader br = new BufferedReader(new InputStreamReader(
		 * c.getInputStream(), "utf-8")); String result = "", line; while ((line
		 * = br.readLine()) != null) { result += line; }
		 * System.out.println(c.getHeaderField(0)); return result;
		 */
		return c.getInputStream();
	}

	public static void main(String[] args) throws Exception {
		Class.forName("org.h2.Driver");
		Connection c = DriverManager.getConnection(Response.url, Response.user,
				Response.password);
		PreparedStatement s = c.prepareStatement("SELECT MAX(ID) FROM VK;");
		ResultSet rs = s.executeQuery();
		int id = 0;
		while (rs.next())
			id = rs.getInt(1) + 1;
		int step = 1000;
		for (int i = 0; i < 100; i++) {
			System.out.print(id + "... ");
			InputStream is = getJsonUsers(id, step);
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			Response response = mapper.readValue(is, Response.class);
			response.saveToDb();
			id += step;
		}
	}

}
