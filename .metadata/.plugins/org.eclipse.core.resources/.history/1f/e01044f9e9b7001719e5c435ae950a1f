package dbViewer;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Client {
	private CloseableHttpClient httpClient;
	private ResponseHandler responseHandler;
	public Client(){
		httpClient=HttpClients.createDefault();
		responseHandler=new ResponseHandler();
	}
	
	public void fetchInfo(){
		HttpPost post=new HttpPost(Config.getURL("fetchInfo"));
		try {
			httpClient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
