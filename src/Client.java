import java.awt.EventQueue;

import javax.swing.JFrame;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;


public class Client{

	private JFrame frame;
	private JTextField username;
	private JTextField chatwith;
	private JTextField status;
	private JTextField msg;
	DataOutputStream dout;
    DataInputStream din;
	static String ser_ip;
	static String login;
	String tochat;
	final JTextArea hist = new JTextArea();
	final JTextArea log = new JTextArea();
	static Socket s=null;
	private JTextField ip;
	
	public void recieve_msg() 
	{
		while(true){
			
			String msg=null;
			int flag=0;
			
			try {
				msg=din.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			if(msg.length()>=3)
			{
				//System.out.println("msg="+msg);
				//System.out.println("length of msg="+msg.length());
			char[] val=msg.toCharArray();
			//System.out.println(msg);
			if(val[0]=='L' && val[1]=='o' && val[2]=='g')
			{	
				flag=1;
				StringTokenizer st=new StringTokenizer(msg);
				st.nextToken();
				log.setText("");
				while(st.hasMoreTokens())
				{
					String pr=st.nextToken();
					log.append(pr+"\n");
							
				}
				
				
			}
			else if(val[0]=='!' && val[1]=='@' && val[2]=='#')
			{
				flag=1;
				//System.out.println("here1");
				status.setText("Username not available");
				
			}

			else
			{
			//System.out.println("here2");
			flag=1;
			
			hist.append(msg + "\n");
			}
			}
			
			if(flag==0 && msg.length()>0)
			{
				//System.out.println("here1");
				//System.out.println("here3");
				hist.append(msg + "\n");
			}
			
		
		
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Client");
		frame.setBackground(new Color(173, 216, 230));
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.getContentPane().setForeground(new Color(0, 0, 128));
		frame.setBounds(100, 100, 969, 555);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(325, 26, 102, 30);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblChatWith = new JLabel("Chat With:");
		lblChatWith.setBounds(611, 26, 102, 30);
		frame.getContentPane().add(lblChatWith);
		
		username = new JTextField();
		username.setFont(new Font("Ubuntu Light", Font.BOLD | Font.ITALIC, 14));
		username.setForeground(new Color(0, 0, 128));
		username.setBounds(409, 23, 168, 37);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		
		status = new JTextField();
		status.setForeground(new Color(0, 0, 128));
		status.setText("Not Connected");
		status.setBounds(241, 77, 212, 37);
		frame.getContentPane().add(status);
		status.setColumns(10);
		
		chatwith = new JTextField();
		chatwith.setFont(new Font("Ubuntu Light", Font.BOLD | Font.ITALIC, 14));
		chatwith.setForeground(new Color(0, 0, 128));
		chatwith.setColumns(10);
		chatwith.setBounds(717, 23, 168, 37);
		frame.getContentPane().add(chatwith);
		log.setFont(new Font("Dialog", Font.BOLD, 16));
		
		
		log.setBounds(674, 211, 245, 254);
		JScrollPane sp1 = new JScrollPane(log);
		sp1.setBounds(674,211,245,254);
		frame.getContentPane().add(sp1);
		
		//frame.getContentPane().add(log);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				login=username.getText();
				tochat=chatwith.getText();
				ser_ip=ip.getText();
				int flag=0;
				
				try {
					s=new Socket(ser_ip,5217);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					status.setText("Server not available");
					flag=1;
					//e1.printStackTrace();
				}
				
			/*	if(flag==0)
				{
				status.setText("CONNECTED");
				}*/
				
				try {
					din=new DataInputStream(s.getInputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				} 
		        try {
					dout=new DataOutputStream(s.getOutputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}        
		        try {
					dout.writeUTF(login);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				//	e1.printStackTrace();
				}
		        
		        if(flag==0)
				{
				status.setText("CONNECTED");
				}
		        
		        Thread trd = new Thread(){
					
					public void run()
					{
						recieve_msg();
					}	
				};
				
				trd.start();
				
			}
			
			
			
		});
		btnNewButton.setBounds(61, 83, 117, 25);
		frame.getContentPane().add(btnNewButton);
	
		hist.setFont(new Font("Ubuntu Light", Font.BOLD, 14));
		
		
		hist.setBounds(309, 209, 303, 254);
		JScrollPane sp = new JScrollPane(hist);
		sp.setBounds(309,209,303,254);
		frame.getContentPane().add(sp);
		
		
		msg = new JTextField();
		msg.setFont(new Font("Ubuntu Light", Font.BOLD, 14));
		msg.setForeground(new Color(0, 0, 128));
		msg.setBounds(41, 268, 212, 110);
		frame.getContentPane().add(msg);
		msg.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
			String mymsg=null;
			mymsg=msg.getText();
			tochat=chatwith.getText();
			try {
				dout.writeUTF(tochat + " " + "DATA" + " " + mymsg);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			hist.append(login + ":" + mymsg + "\n");
			
			msg.setText("");
			}
		});
		btnSend.setBounds(53, 414, 90, 25);
		frame.getContentPane().add(btnSend);
		
		JLabel lblCreateNewMessage = new JLabel("Create New Message");
		lblCreateNewMessage.setBounds(40, 201, 168, 30);
		frame.getContentPane().add(lblCreateNewMessage);
		
		JLabel lblMessageHistory = new JLabel("Message History");
		lblMessageHistory.setBounds(309, 160, 168, 30);
		frame.getContentPane().add(lblMessageHistory);
		
		JButton btnClose = new JButton("Close");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					dout.writeUTF(login + " LOGOUT");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					s.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				System.exit(0);
			}
		});
		btnClose.setBounds(163, 414, 90, 25);
		frame.getContentPane().add(btnClose);
		
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setBounds(61, 26, 102, 30);
		frame.getContentPane().add(lblServerIp);
		
		ip = new JTextField();
		ip.setFont(new Font("Ubuntu Light", Font.BOLD | Font.ITALIC, 14));
		ip.setColumns(10);
		ip.setBounds(145, 23, 168, 37);
		frame.getContentPane().add(ip);
		
		
		
		
		JLabel lblChatRoomLog = new JLabel("Chat Room log");
		lblChatRoomLog.setBounds(674, 160, 168, 30);
		frame.getContentPane().add(lblChatRoomLog);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					dout.writeUTF(login + " " + "LOG" );
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnRefresh.setBounds(787, 163, 117, 25);
		frame.getContentPane().add(btnRefresh);
		
		JButton btnManageMessage = new JButton("Manage Message ");
		btnManageMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				dummy d = null;
				try {
					d=new dummy();
					d.manage();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnManageMessage.setBounds(71, 463, 168, 25);
		frame.getContentPane().add(btnManageMessage);
	}
	
	class dummy extends Manage_msg{

		public dummy() throws ClassNotFoundException, SQLException {
			
			// TODO Auto-generated constructor stub
		}
		void manage() throws ClassNotFoundException, SQLException
		{
			Manage_msg m=new Manage_msg();
		}
		
	}
}
