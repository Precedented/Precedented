public class WatsonRank {
	public String solrClusterId = "sce46d929b_1d1e_4bd3_bf60_8472e75cb73a";
	public String username = "64e80095-84de-4d28-9e7d-4f92f0c881d2";
	public String password = "VcUAcwnPzwvp";
	public String configName = "example_config";
	public String collectionName = "example_collection";
	private RetrieveAndRank service = new RetrieveAndRank();

public WatsonRank ()
{
	service.setUsernameAndPassword(username, password);
}

public boolean createCollection()
{
	CollectionAdminRequest.Create createCollectionRequest =  new CollectionAdminRequest.Create();
	createCollectionRequest.setCollectionName("example_collection");
	createCollectionRequest.setConfigName("example_config");

	System.out.println("Creating collection...");
	CollectionAdminResponse response = createCollectionRequest.process(solrClient);
    if (!response.isSuccess()) {
      System.out.println(response.getErrorMessages());
      throw new IllegalStateException("Failed to create collection: " + response.getErrorMessages().toString());
    }
	System.out.println("Collection created.");
	System.out.println(response);
}

public boolean uploadConfig(String filePath, String newConfigName)
{
	File configZip = new File(filePath);
	service.uploadSolrClusterConfigurationZip(solrClusterId, newConfigName, configZip);
}

public boolean uploadData(String title, String author, String body, String url, int n)
{
	SolrInputDocument newdoc = new SolrInputDocument();
	document.addField("id", n);
	document.addField("author", author);
	document.addField("url", url);
	document.addField("body", body);
	document.addField("title", title)

	System.out.println("Indexing document...");
	UpdateResponse addResponse = solrClient.add(collectionName, newdoc);
	System.out.println(addResponse);

	// Commit the document to the index so that it will be available for searching.
	solrClient.commit(collectionName);
	System.out.println("Indexed and committed document.");
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
public void getConfigs()
{
	System.out.println(service.getSolrClusterConfigurations(solrClusterId));
}