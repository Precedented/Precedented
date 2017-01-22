//only have to run this class' upload function once
import java.io.*;
import java.net.URLConnection;
import java.net.URI;
import java.net.URL;
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
import java.util.ArrayList;
import java.util.HashSet;

public class UploadCaseData {
    private static final String ALL_CASES_FILE = "all.csv";
    public static void main (String[] args) {
        ArrayList<String> allCaseHTML = getAllCases();
        for (String s : allCaseHTML) {
            uploadData(s);
        }
    }
    private static void uploadData(String body) {
        SolrInputDocument newdoc = new SolrInputDocument();
        newdoc.addField("body", body);

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

    private static ArrayList<String> getAllCases() {
        ArrayList<String> allCases = new ArrayList<>();
        String allCasesJson = curl("https://courtlistener.com/api/rest/v3/opinions");
        //html_lawbox --> length 11
        //</div> --> length 6

        for (int i = 0; i < allCasesJson.length() - 11; i++) {
            if (allCasesJson.substring(i, i+11).equals("html_lawbox")) {
                String nextCase = "";
                int closeIndex;
                for (int j = i + 12; ; j++) {
                    String sub = allCasesJson.substring(j, j + 6);
                    nextCase += sub.substring(0, 1);
                    if (sub.equals("</div>")) {
                        closeIndex = j + 5;
                        break;
                    }
                }
                i = closeIndex + 1;
                allCases.add(nextCase);
            }
        }

        return allCases;
    }
    private static String getCaseName(int caseId) {
        String output = "";
        try {
            Process p = Runtime.getRuntime().exec("curl https://www.courtlistener.com/api/rest/v3/dockets/?id=" + caseId);
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
    private static String curl(String request) {
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
