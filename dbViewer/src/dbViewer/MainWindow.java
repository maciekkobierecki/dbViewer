package dbViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainWindow extends JFrame implements ActionListener {

	private JButton fetchInfoButton;
	private JLabel infoLabel;
	private JTable data;
	//Client client;
	MainWindow(){
		super("dbViewer");
		infoLabel=new JLabel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setPreferredSize(new Dimension(200,100));
		fetchInfoButton=new JButton("fetch info");
		this.setLayout(new BorderLayout());
		data=new JTable();
		data.setPreferredSize(new Dimension(400,300));
		infoLabel.setText("nothing to show");
		fetchInfoButton.addActionListener(this);
		add(infoLabel);
		add(fetchInfoButton, BorderLayout.LINE_END);
		add(data, BorderLayout.PAGE_START);
		pack();
		setVisible(true);
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		FetchDataWorker worker= new FetchDataWorker(FetchDataWorker.METADATA, data, null);
		worker.execute();
		
	}
}
