import java.io.*;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.RetrieveAndRank;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.*;


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
        final HttpClientBuilder builder = HttpClientBuilder.create()
            .setMaxConnTotal(128)
            .setMaxConnPerRoute(32)
            .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT).setRedirectsEnabled(true).build())
            .setDefaultCredentialsProvider(credentialsProvider)
            .addInterceptorFirst(new PreemptiveAuthInterceptor());
        return builder.build();
    }
    //</construction>

    //<upload>
    public void uploadConfig(String filePath, String newConfigName) {
        File configZip = new File(filePath);
        service.uploadSolrClusterConfigurationZip(SOLR_CLUSTER_ID, newConfigName, configZip);
    }

    public void uploadData(String body, String title, int idNum) {
        SolrInputDocument newdoc = new SolrInputDocument();
        newdoc.addField("id", idNum);
        newdoc.addField("body", body);
        newdoc.addField("title", title);

        try {

            System.out.println("Indexing document...");
            UpdateResponse addResponse = solrClient.add(collectionName, newdoc);
            System.out.println(addResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Commit the document to the index so that it will be available for searching.
        try {
            solrClient.commit(collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Indexed and committed document.");
    }
	//</upload>

    //<functions>
    public QueryResponse queryWatson(String question) {
 		solrClient = getSolrClient(service.getSolrUrl(SOLR_CLUSTER_ID), USERNAME, PASSWORD);
		SolrQuery query = new SolrQuery(question);
		query.setParam("ranker_id", RANKER_ID);
		query.setRequestHandler("/fcselect"); // use if your solrconfig.xml file does not specify fcselect as the default request handler
		QueryResponse response = solrClient.query(collectionName, query);
        return response;
    }
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

    private static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
        public void process(final HttpRequest request, final HttpContext context) throws HttpException {
            final AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);

            if (authState.getAuthScheme() == null) {
                final CredentialsProvider credsProvider = (CredentialsProvider) context
                        .getAttribute(HttpClientContext.CREDS_PROVIDER);
                final HttpHost targetHost = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
                final Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(),
                        targetHost.getPort()));
                if (creds == null) {
                    throw new HttpException("No creds provided for preemptive auth.");
                }
                authState.update(new BasicScheme(), creds);
            }
        }
    }
}