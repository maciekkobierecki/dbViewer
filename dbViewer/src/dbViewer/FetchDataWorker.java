package dbViewer;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchDataWorker extends SwingWorker<Integer,String> {
	public static final String METADATA= "metadata";
	public static final String DATA="data";
	public static final Boolean ONLY_TITLES_MODE=true;
	private Client client;
	private JTable dataTable;
	private String datatype;
	private String tableName;
	private Boolean onlyTitles=false;
	
	public FetchDataWorker(Client client, String dataType, JTable dataTable, String tableName){
		this.dataTable=dataTable;
		this.datatype=dataType;
		this.tableName=tableName;
		this.client=client;
	}
	public FetchDataWorker(Client client, String dataType, JTable dataTable, String tableName, Boolean mode){
		this.dataTable=dataTable;
		this.datatype=dataType;
		this.tableName=tableName;
		this.client=client;
		onlyTitles=mode;
	}
	@Override
	protected Integer doInBackground() throws Exception {
		HttpResponse response=client.fetchInfo();
		int responseCode=response.getStatusLine().getStatusCode();
		if(200<=responseCode && responseCode<300){
			ResponseHandler<String>handler=new BasicResponseHandler();
			String body=handler.handleResponse(response);
			System.out.println(body);
			publish(body);
		}
		return null;
	}
	@Override 
	protected void process(List< String> data){
		try {
		JSONObject dataJSON=new JSONObject(data.get(0));
		DefaultTableModel tablemodel=(DefaultTableModel)dataTable.getModel();
		switch(datatype){
		case METADATA:
			Iterator<String> nestedObjectsIterator=dataJSON.keys();
			while(nestedObjectsIterator.hasNext()){
				
					JSONObject tableJSON= dataJSON.getJSONObject(nestedObjectsIterator.next());
					String tableName=tableJSON.getString(Config.getProperty("tableNameJSON"));
					Vector row=new Vector();
					if(!onlyTitles){
						JSONObject columnsJSON=tableJSON.getJSONObject(Config.getProperty("columnsJSON"));

						for(int i=0; i<columnsJSON.length();++i){
							JSONObject columnJSON=columnsJSON.getJSONObject("Column2");
							String tableField=columnJSON.getString("columnName")+"["+columnJSON.getString("columnType")+" ("+columnJSON.getInt("size")+")]";
							row.add(tableField);											
						}
						tablemodel.addRow(row);
					}
					else{
						row.addElement(tableName);
						tablemodel.addRow(row);
					}
					dataTable.repaint();
			}
			
			break;
		case DATA:
			Iterator<String>rowIterator=dataJSON.keys();
			JSONObject rowJSON=dataJSON.getJSONObject(rowIterator.next());
			DefaultTableModel model=addColumns(rowJSON);
			Object[] row;
			while(rowIterator.hasNext()){
				row=new Object[rowJSON.length()];
				for(int i=1; i<=rowJSON.length(); i++){
					String columnName=model.getColumnName(i-1);
					row[i-1]=rowJSON.get(columnName);
				}
				model.addRow(row);
				rowJSON=dataJSON.getJSONObject(rowIterator.next());
			}
			break;
		
	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public DefaultTableModel addColumns(JSONObject row){
		DefaultTableModel model=(DefaultTableModel)dataTable.getModel();
		Iterator<String>columns=row.keys();
		while(columns.hasNext())
			model.addColumn(columns.next());
		return model;
		
	}


}
