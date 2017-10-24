package dbViewer;

import javax.swing.JTable;
import javax.swing.SwingWorker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

public class FetchDataWorker extends SwingWorker<Integer,String> {
	public static final String METADATA= "metadata";
	public static final String DATA="data";
	private Client client;
	private JTable dataTable;
	private String datatype;
	private String tableName;
	
	//Datatype can be "metadata" or data
	public FetchDataWorker(String dataType, JTable dataTable, String tableName){
		this.dataTable=dataTable;
		this.datatype=dataType;
		this.tableName=tableName;
		client=new Client(datatype, tableName);
	}
	@Override
	protected Integer doInBackground() throws Exception {
		HttpResponse response=client.fetchInfo();
		int responseCode=response.getStatusLine().getStatusCode();
		if(200<=responseCode && responseCode<300){
			ResponseHandler<String>handler=new BasicResponseHandler();
			String body=handler.handleResponse(response);
			System.out.println(body);
		
		}
		
		return null;
	}

}
