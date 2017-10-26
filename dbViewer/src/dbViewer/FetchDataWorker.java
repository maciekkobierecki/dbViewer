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
			JSONObject rowJSON=dataJSON.getJSONObject("Row1");
			DefaultTableModel model=addColumns(rowJSON);
			Object[] row;
			Iterator<String>rowIterator=dataJSON.keys();
			while(rowIterator.hasNext()){
				rowJSON=dataJSON.getJSONObject(rowIterator.next());
				row=new Object[rowJSON.length()];
				for(int i=0; i<rowJSON.length(); i++){
					String columnName=model.getColumnName(i);
					row[i]=rowJSON.get(columnName);
				}
				model.addRow(row);
				
			}
			break;
		
	}
		} catch (JSONException e) {
			DefaultTableModel model=(DefaultTableModel)dataTable.getModel();
			model.addColumn("");
			model.addRow(new Object[]{"tablica pusta"});
		}
		
	}
	public DefaultTableModel addColumns(JSONObject row){
		DefaultTableModel model=(DefaultTableModel)dataTable.getModel();
		Iterator<String>columns=row.keys();
		int counter=0;
		int idColumnPosition=0;
		String columnLabel;
		while(columns.hasNext()){

			columnLabel=columns.next();
			model.addColumn(columnLabel);
			if(columnLabel.equals("id"))
				idColumnPosition=counter;
			counter++;
				
		}
		dataTable.moveColumn(idColumnPosition, 0);
		return model;
		
	}


}
