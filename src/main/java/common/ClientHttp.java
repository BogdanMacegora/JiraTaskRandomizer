package common;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClientHttp {

    public static String sendGetRequest(String url) {
        String output = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(url);
            getRequest.addHeader("Authorization", Config.AUTHORIZATION);
            CloseableHttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200)
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            while ((output = br.readLine()) != null) {
                return output;
            }
        } catch (IOException e) {
            AlertMessage.showErrorMessage("Exception while send GET request to " + url + "\n", e);
        }
        return output;
    }

    public static HttpResponse sendPostRequest(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        StringEntity postEntity = null;
        HttpResponse response = null;
        try {
            postEntity = new StringEntity(data);
            post.addHeader("Authorization", Config.AUTHORIZATION);
            post.addHeader("Content-type", "application/json");
            post.setEntity(postEntity);
            response = httpClient.execute(post);
        } catch (IOException e) {
            AlertMessage.showErrorMessage("Exception while send POST request to " + url + "\n" +
                    "Response content: " + response.getEntity(), e);
        }
        return response;
    }

    public static HttpResponse sendPutRequest(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = null;
        HttpPut put = new HttpPut(url);
        try {
            StringEntity postEntity = new StringEntity(data);
            put.addHeader("Authorization", Config.AUTHORIZATION);
            put.addHeader("Content-type", "application/json");
            put.setEntity(postEntity);
            response = httpClient.execute(put);
        } catch (IOException e) {
            AlertMessage.showErrorMessage("Exception while send PUT request to " + url + "\n" +
                    "Response content: " + response.getEntity(), e);
        }
        return response;
    }
}
