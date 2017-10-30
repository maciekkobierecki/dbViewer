package dbViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class MainWindow extends JFrame implements ActionListener {

	private JButton fetchInfoButton;
	private JTable data;
	PoolingHttpClientConnectionManager  connectionManager=new PoolingHttpClientConnectionManager();
	//Client client;
	MainWindow(){
		super("dbViewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setPreferredSize(new Dimension(200,100));
		fetchInfoButton=new JButton("refresh");
		
		this.setLayout(new BorderLayout());
		DefaultTableModel model=new DefaultTableModel();
		model.addColumn("1");
		data=new JTable(model){
			private static final long serialVersionUID=1L;
			
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		data.setPreferredSize(new Dimension(400,300));
		fetchInfoButton.addActionListener(this);

		add(fetchInfoButton, BorderLayout.LINE_END);
		add(data, BorderLayout.PAGE_START);
		pack();
		setVisible(true);
		data.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent me){
				Point p=me.getPoint();
				int row=data.rowAtPoint(p);
				if(me.getClickCount()== 2){
					new TableWindow(data.getModel().getValueAt(row, 0).toString(), connectionManager);
				}
			}
		});
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		((DefaultTableModel)data.getModel()).setRowCount(0);
		FetchDataWorker worker= new FetchDataWorker(new Client(connectionManager, FetchDataWorker.METADATA, null),FetchDataWorker.METADATA, data, null, FetchDataWorker.ONLY_TITLES_MODE);
		worker.execute();
		
	}
}
