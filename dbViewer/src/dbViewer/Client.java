package dbViewer;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;


public class Client {
	private CloseableHttpClient httpClient;
	String dataType;
	String tableName;
	public Client(PoolingHttpClientConnectionManager connectionManager,String dataType, String tableName){
		httpClient=HttpClients.custom().setConnectionManager(connectionManager).build();
		//responseHandler=new ResponseHandlerr(dataType, tableName);
		this.dataType=dataType;
		this.tableName=tableName;
	}
	
	public HttpResponse fetchInfo() throws JSONException{
		String URL=Config.getURL(dataType);
		/*if(tableName!=null){
			URL+="/";
			URL+=tableName;
		}*/
		HttpPost post=new HttpPost(URL);
		
		try {
			if(dataType==FetchDataWorker.DATA){
				JSONObject tableNameJSON=new JSONObject();
				tableNameJSON.put("tableName", tableName);
				post.setEntity(new StringEntity(tableNameJSON.toString()));
			}
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
