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
import org.apache.commons.io.input.*;

public class UploadCaseData {
    private static final String ALL_CASES_FILE = "all.csv";
    private static String collectionName = "example_collection";


    public static void main (String[] args) {
        //ArrayList<ArrayList<String>> allCaseData = getAllCases();
        //System.out.println(allCaseData);

        String allCasesJson = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("AllCases.txt"));
            allCasesJson = br.readLine();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Made it");

        //String allCasesJson = curl("https://courtlistener.com/api/rest/v3/opinions", true);
        //System.out.println("here");
        //html_lawbox --> length 11
        //</div> --> length 6
        String [] bodies = new String [4000000];
        String [] urls = new String [4000000];

        int caseCount = 0;
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
                bodies[caseCount]=nextCase;
                caseCount++;
            }
        }

        System.out.println(caseCount);

        //absolute_url --> 12 chars

        int t = 0;
        caseCount = 0;

        for (int i = 0; i < allCasesJson.length() - 12; i++) {
            if (allCasesJson.substring(i, i+12).equals("absolute_url")) {
                String nextURL = "";
                int closeIndex = -1;
                for (int j = i + 12 + 3; j>0; j++) {
                    String sub = allCasesJson.substring(j, j+1);
                    if (sub.equals("\"")) {
                        closeIndex = j;
                        break;
                    }
                    nextURL += sub;
                }
                i = closeIndex + 1;
                urls[caseCount]=nextURL;
                caseCount++;
                t++;
            }
        }

        WatsonRank uploader = new WatsonRank();

        for (int i = 0; i < 100; i++) {
            uploader.uploadData(bodies[i], urls[i]);
        }
    }


    private static ArrayList<ArrayList<String>> getAllCases() {
        ArrayList<ArrayList<String>> allCases = new ArrayList<ArrayList<String>>();
        String allCasesJson = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("AllCases.txt"));
            allCasesJson = br.readLine();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Made it");

        //String allCasesJson = curl("https://courtlistener.com/api/rest/v3/opinions", true);
        //System.out.println("here");
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
                ArrayList<String> newPair = new ArrayList<>();
                newPair.add(nextCase);
                allCases.add(newPair);
            }
        }

        System.out.println(allCases.size());

        //absolute_url --> 12 chars

        int t = 0;

        for (int i = 0; i < allCasesJson.length() - 12; i++) {
            if (allCasesJson.substring(i, i+12).equals("absolute_url")) {
                String nextURL = "";
                int closeIndex;
                for (int j = i + 13 + 3; ; j++) {
                    String sub = allCasesJson.substring(j, j+1);
                    if (sub.equals("\"")) {
                        closeIndex = j;
                        break;
                    }
                    nextURL += sub;
                }
                i = closeIndex + 1;
                allCases.get(t).add(nextURL);
                t++;
            }
        }

        return allCases;
    }
    /*private static String getCaseName(int caseId) {
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
    /*private static String curl(String request, boolean timed) {
        if (!timed) {
            String output = "";
            try {
                Process p = Runtime.getRuntime().exec("curl " + request);
                p.waitFor();
                InputStreamReader r = new InputStreamReader(p.getInputStream());
                String line = null;
                while ((line = r.readLine()) != null) {
                    output += line + "\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        } else {
            String output = "";
            try {
                Process p = Runtime.getRuntime().exec("curl " + request);
                p.waitFor();
                long startTime = System.currentTimeMillis();
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null && (System.currentTimeMillis() - startTime) < 10000) {
                    output += line + "\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }
    }*/
}
