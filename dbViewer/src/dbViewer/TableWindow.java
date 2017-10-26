package dbViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class TableWindow extends JFrame {
	private JTable dataTable;
	
	public TableWindow(String tableName, PoolingHttpClientConnectionManager manager){
		this.setTitle(tableName);
		this.setPreferredSize(new Dimension(300,300));
		this.setLayout(new BorderLayout());
		DefaultTableModel tableModel=new DefaultTableModel();
		dataTable=new JTable(tableModel);
		add(dataTable);
		pack();
		setVisible(true);
		FetchDataWorker fetchWorker=new FetchDataWorker(new Client(manager, FetchDataWorker.DATA, tableName), FetchDataWorker.DATA, dataTable, tableName);
		fetchWorker.execute();


	}
	
}
