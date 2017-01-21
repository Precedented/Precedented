public class WatsonRank {
	public String solrClusterId = "sce46d929b_1d1e_4bd3_bf60_8472e75cb73a";
	public String username = "64e80095-84de-4d28-9e7d-4f92f0c881d2";
	public String password = "VcUAcwnPzwvp";
	private RetrieveAndRank service = new RetrieveAndRank();

public WatsonRank ()
{

}

public boolean uploadJson (String filePath)
{
	
	service.setUsernameAndPassword(username, password);

	CollectionAdminRequest.Create createCollectionRequest =  new CollectionAdminRequest.Create();
	createCollectionRequest.setCollectionName("example_collection");
	createCollectionRequest.setConfigName("example_config");

	System.out.println("Creating collection...");
	CollectionAdminResponse response = createCollectionRequest.process(solrClient);
    if (!response.isSuccess()) {
      System.out.println(response.getErrorMessages());
      throw new IllegalStateException("Failed to create collection: "
          + response.getErrorMessages().toString());
    }
	System.out.println("Collection created.");
	System.out.println(response);
}

public ___ rankWatson()
{


}

public void getClusters()
{
	RetrieveAndRank service = new RetrieveAndRank();
	service.setUsernameAndPassword(username, password);

	SolrClusterList clusters = service.getSolrClusters();
	System.out.println(clusters);
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