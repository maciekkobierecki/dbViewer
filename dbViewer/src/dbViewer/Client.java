package dbViewer;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Client {
	private CloseableHttpClient httpClient;
	String dataType;
	String tableName;
	public Client(String dataType, String tableName){
		httpClient=HttpClients.createDefault();
		//responseHandler=new ResponseHandlerr(dataType, tableName);
		this.dataType=dataType;
		this.tableName=tableName;
	}
	
	public HttpResponse fetchInfo(){
		String URL=Config.getURL(dataType);
		if(tableName!=null){
			URL+="/";
			URL+=tableName;
		}
		HttpPost post=new HttpPost(URL);
		try {
			HttpResponse response=httpClient.execute(post);
			return response;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
