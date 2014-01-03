import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

	public static String getJsonUsers(int from, int count) throws IOException {
		// Prepare HTTP connection
		URL url = new URL(api + method);
		HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
		c.setRequestMethod("POST");
		c.setDoOutput(true);
		// Write parameters
		String ids = "user_ids=" + getNumbers(from, from + count);
		String fields = "fields=sex,bdate,city,country,photo_50,photo_100,photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,online,online_mobile,lists,has_mobile,contacts,connections,site,education,universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message,status,last_seen,relation,relatives,counters";
		DataOutputStream os = new java.io.DataOutputStream(c.getOutputStream());
		os.writeBytes(ids + "&" + fields);
		os.flush();
		os.close();
		// Get response
		BufferedReader br = new BufferedReader(new InputStreamReader(
				c.getInputStream(), "utf-8"));
		String result = "", line;
		while ((line = br.readLine()) != null) {
			result += line;
		}
		System.out.println(c.getHeaderField(0));
		return result;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getJsonUsers(1, 1));
	}

}
