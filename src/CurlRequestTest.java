import java.io.*;

public class CurlRequestTest {
	public static void main(String[] args) {
		System.out.println(curl("curl https://www.courtlistener.com/api/rest/v3/dockets/?court__id=scotus"));
	}
	public static String curl(String request) {
		String output = "";
		try {
			Process p = Runtime.getRuntime().exec(request);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				output += line + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
}