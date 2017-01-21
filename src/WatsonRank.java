public class WatsonRank {
	public static String solrClusterId = "sce46d929b_1d1e_4bd3_bf60_8472e75cb73a";
	public static String username = "64e80095-84de-4d28-9e7d-4f92f0c881d2";
	public static String password = "VcUAcwnPzwvp";

public boolean uploadJson (String filePath)
{
String url = "https://gateway.watsonplatform.net/retrieve-and-rank/api/v1/solr_clusters/"+ solrClusterId +"/solr/example_collection/update";
String request = " -X POST -H \"Content-Type: application/json\" -u \"" + username + "\":\"" + password + "\" \"" + url + "\" --data-binary @" + filePath;

curl(request);

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