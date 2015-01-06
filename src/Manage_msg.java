import java.awt.EventQueue;

import javax.swing.JFrame;

import java.sql.*;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Manage_msg extends Client{
	private static final JFrame frame=new JFrame("Manage Messages");
	//private static  JFrame frame;
	Statement stmt;
    Connection conn;
    Client cl;
    private JTextField frnd;
    final JTextArea hist = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Manage_msg window = new Manage_msg();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}

	/**
	 * Create the application.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @wbp.parser.entryPoint
	 */
	public Manage_msg() throws ClassNotFoundException, SQLException {
		System.out.println("I am in constructor");
		cl=new Client();
		 String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		 String DB_URL = "jdbc:mysql://"+cl.ser_ip+"/chat";

		
		 String USER = "root";
		 String PASS = "comp";

		Class.forName(JDBC_DRIVER);
	
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		stmt = conn.createStatement();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//frame = new JFrame();
		frame.setBounds(100, 100, 690, 543);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JLabel lblFreiendsName = new JLabel("Freiend's Name:");
		lblFreiendsName.setBounds(74, 12, 142, 32);
		frame.getContentPane().add(lblFreiendsName);
		
		frnd = new JTextField();
		frnd.setBounds(212, 10, 159, 38);
		frame.getContentPane().add(frnd);
		frnd.setColumns(10);
		hist.setBounds(63, 166, 398, 337);
		frame.getContentPane().add(hist);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String username=cl.login;
				String friend=frnd.getText();
				String msql="select * from message where ( from_='"+username+"'and to_='"+friend+"' ) or ( from_='"+friend+"'and to_='"+username+"')";
				ResultSet rs = null;
				try {
					rs=stmt.executeQuery(msql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					while(rs.next())
					{
						String print="";
						print=print+rs.getString(1)+":"+rs.getString(3);
						hist.append(print+"\n");
						
						
						
						
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnSubmit.setBounds(400, 12, 117, 32);
		frame.getContentPane().add(btnSubmit);
		
		
		
		
		JLabel lblHis = new JLabel("MESSAGE HISTORY");
		lblHis.setBounds(82, 124, 159, 15);
		frame.getContentPane().add(lblHis);
		
		JButton btnClose = new JButton("Close");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				frame.setVisible(false);
			}
		});
		btnClose.setBounds(529, 12, 117, 32);
		frame.getContentPane().add(btnClose);
	}
}
