import java.io.File;
import java.net.URI;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.*;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.*;
import java.io.*;

import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.common.*;
import org.apache.solr.client.solrj.request.*;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.client.solrj.*;

public class WatsonRank {
    //<declaration>
    public static final String SOLR_CLUSTER_ID = "sce46d929b_1d1e_4bd3_bf60_8472e75cb73a";
    public static final String RANKER_ID = "76643bx23-rank-3206";
    public static final String USERNAME = "64e80095-84de-4d28-9e7d-4f92f0c881d2";
    public static final String PASSWORD = "VcUAcwnPzwvp";
    public static String configName = "example_config";
    public static String collectionName = "example_collection";
    public static String rankerName = "example_ranker";
    private static RetrieveAndRank service = new RetrieveAndRank();
    private static HttpSolrClient solrClient;
    //</declration>

    //<construction>
    public WatsonRank() {
        service.setUsernameAndPassword(USERNAME, PASSWORD);
    }

    private static HttpSolrClient getSolrClient(String uri, String username, String password) {
        return new HttpSolrClient(service.getSolrUrl(SOLR_CLUSTER_ID), createHttpClient(uri, username, password));
    }

    private static HttpClient createHttpClient(String uri, String username, String password) {
        final URI scopeUri = URI.create(uri);

        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(scopeUri.getHost(), scopeUri.getPort()),
            new UsernamePasswordCredentials("{username}", "{password}"));

        final HttpClientBuilder builder = HttpClientBuilder.create()
            .setMaxConnTotal(128)
            .setMaxConnPerRoute(32)
            .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT).setRedirectsEnabled(true).build())
            .setDefaultCredentialsProvider(credentialsProvider)
            .addInterceptorFirst(new PreemptiveAuthInterceptor());
        return builder.build();
    }
    //</construction>
    /*
    //<create>
    public void createCollection() {
    CollectionAdminRequest.Create createCollectionRequest = new CollectionAdminRequest.Create();
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

    public void createRanker() {
    Ranker ranker = service.createRanker("ranker1", new File("./training_data.csv"));
    System.out.println(ranker);
    } */
    //</create>

    //<upload>
    public void uploadConfig(String filePath, String newConfigName) {
        File configZip = new File(filePath);
        service.uploadSolrClusterConfigurationZip(SOLR_CLUSTER_ID, newConfigName, configZip);
    }

    /*  public void uploadData(String title, String author, String body, String url, int n) {
    SolrInputDocument newdoc = new SolrInputDocument();
    newdoc.addField("id", n);
    newdoc.addField("author", author);
    newdoc.addField("url", url);
    newdoc.addField("body", body);
    newdoc.addField("title", title);

    System.out.println("Indexing document...");
    UpdateResponse addResponse = solrClient.add(collectionName, newdoc);
    System.out.println(addResponse);

    // Commit the document to the index so that it will be available for searching.
    solrClient.commit(collectionName);
    System.out.println("Indexed and committed document.");
    } */
    //</upload>

    //<functions>
    /*  public QueryResponse queryWatson(String question) {
    service = new RetrieveAndRank();
    service.setUsernameAndPassword(USERNAME, PASSWORD);
    solrClient = getSolrClient(service.getSolrUrl(SOLR_CLUSTER_ID), USERNAME, PASSWORD);
    SolrQuery query = new SolrQuery("*:*");
    QueryResponse response = solrClient.query("example_collection", question);
    System.out.println(response);
    }*/
    //</functions>

    //<gets>
    public void getConfigs() {
        System.out.println(service.getSolrClusterConfigurations(SOLR_CLUSTER_ID));
    }
    //</gets>

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