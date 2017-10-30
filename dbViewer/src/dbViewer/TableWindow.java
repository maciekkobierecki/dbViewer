package dbViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class TableWindow extends JFrame{
	private JTable dataTable;
	private JButton deleteRowButton, refreshTableButton, filterButton;
	private JPanel menuPanel;
	private JTextField filterTextField;
	public TableWindow(String tableName, PoolingHttpClientConnectionManager manager){
		this.setTitle(tableName);
		//this.setPreferredSize(new Dimension(300,300));
		this.setLayout(new BorderLayout());
		DefaultTableModel tableModel=new DefaultTableModel();
		dataTable=new JTable(tableModel);
		dataTable.setAutoCreateRowSorter(true);
		add(new JScrollPane(dataTable));
		refreshTableButton=new JButton(Config.getProperty("refreshButton"));
		//refreshTableButton.addActionListener(this);
		menuPanel=new JPanel();
		filterTextField=new JTextField("columnName=value");
		filterTextField.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				filterTextField.setText("");
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				
			}
			
		});
		
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
		deleteRowButton=new JButton(Config.getProperty("deleteButton"));
		deleteRowButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model=(DefaultTableModel)dataTable.getModel();
				int selectedRow=dataTable.getSelectedRow();
				if(selectedRow!=-1){
					int rowIndexInModel=dataTable.convertRowIndexToModel(selectedRow);
					model.removeRow(rowIndexInModel);
				}
			}
			
		});
		refreshTableButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model=(DefaultTableModel)dataTable.getModel();
				int rowsNumber=model.getRowCount();
				model.setColumnCount(0);
				model.setRowCount(0);
				FetchDataWorker fetchWorker=new FetchDataWorker(new Client(manager, FetchDataWorker.DATA, tableName), FetchDataWorker.DATA, dataTable, tableName);
				fetchWorker.execute();
				
			}
			
		});
		filterButton=new JButton(Config.getProperty("filterButton"));
		menuPanel.add(deleteRowButton);
		menuPanel.add(Box.createRigidArea(new Dimension(10,0)));
		menuPanel.add(refreshTableButton);
		menuPanel.add(Box.createRigidArea(new Dimension(10,0)));
		menuPanel.add(filterTextField);
		menuPanel.add(Box.createRigidArea(new Dimension(10,0)));
		menuPanel.add(filterButton);
		filterTextField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				filterButton.doClick();
				
			}
			
		});
		filterButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					DefaultTableModel model=(DefaultTableModel)dataTable.getModel();
					int rowsNumber=model.getRowCount();
					String params=filterTextField.getText();
					String[] param=params.split("=");
					String filteredColumn=param[0];
					String filterParam=param[1];
					for(int i=0; i<rowsNumber;i++){
						int column=dataTable.getColumn(filteredColumn).getModelIndex();
						if(!model.getValueAt(i,column).equals(filterParam)){
							model.removeRow(i);
							i--;
							rowsNumber--;
						}
					}
					
				}
				catch(Exception e){
					
				}
			}
			
		});
		add(menuPanel, BorderLayout.AFTER_LAST_LINE);
		pack();
		setVisible(true);
		FetchDataWorker fetchWorker=new FetchDataWorker(new Client(manager, FetchDataWorker.DATA, tableName), FetchDataWorker.DATA, dataTable, tableName);
		fetchWorker.execute();



	}
	
}
