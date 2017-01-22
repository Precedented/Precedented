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

    }
    private static ArrayList<String> getAllCases() {
        String allCasesJson = curl("https://courtlistener.com/api/rest/v3/opinions");
        

        return null;
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
