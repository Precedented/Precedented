import java.io.*;
import java.util.Map;
import java.util.Set;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

public class CurlRequestTest {
	public static void main(String[] args) {
		String result = curl("https://www.courtlistener.com/api/rest/v3/dockets/?court__id=scotus");
		//System.out.println(result);
		JsonObject obj = strToJsonObj(result);
		System.out.println(obj);
		Set<Map.Entry<String, JsonElement>> entries = jsonObjToSet(obj);
		for (Map.Entry<String, JsonElement> entry : entries) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		//Map.Entry<String, JsonElement> caseArray = entries.
	}
	public static JsonObject strToJsonObj(String s) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(s);
		JsonObject obj = element.getAsJsonObject();
		return obj;
	}
	public static Set<Map.Entry<String, JsonElement>> jsonObjToSet(JsonObject obj) {
		Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
		return entries;
	}
	public static String curl(String request) {
		String output = "";
		try {
			Process p = Runtime.getRuntime().exec("curl " + request);
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