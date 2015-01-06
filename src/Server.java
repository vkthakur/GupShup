import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
import java.sql.*;

public class Server {

	private static JFrame frame;
	static Vector ClientSockets;
    static Vector LoginNames;
    final static  JTextArea status = new JTextArea();
    static ServerSocket soc=null;
    Statement stmt;
    Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public Server() throws Exception {
		
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		 String DB_URL = "jdbc:mysql://localhost/chat";

		
		 String USER = "root";
		 String PASS = "";

		Class.forName(JDBC_DRIVER);
	
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		stmt = conn.createStatement();
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		frame = new JFrame("Server");
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.setBounds(100, 100, 662, 511);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		status.setForeground(new Color(0, 128, 0));
		status.setBackground(Color.WHITE);
		status.setFont(new Font("Ubuntu Light", Font.BOLD, 14));
		
		status.setBounds(86, 139, 507, 349);
		JScrollPane sp = new JScrollPane(status);
		sp.setBounds(89, 139, 507, 349);
		frame.getContentPane().add(sp);
		
		JButton btnStartServer = new JButton("START SERVER");
		btnStartServer.setBounds(89, 12, 167, 39);
		btnStartServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				status.append("Server Started\n");
				
				try {
					soc=new ServerSocket(5217);
					//System.out.println("soc1:"+soc);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println("soc:"+soc);
			    ClientSockets=new Vector();
			    LoginNames=new Vector();
			    
			    Thread mythread=new Thread(){
			    	public void run()
			    	{
			    		 while(true)
						    {    
						            Socket CSoc = null;
									try {
										CSoc = soc.accept();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										//e1.printStackTrace();
									}        
						            try {
						            	//System.out.println("Csoc:"+CSoc);
										AcceptClient obClient=new AcceptClient(CSoc);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
						    }
			    		
			    	}
			    	
			    };
			    
			    mythread.start();

			   
				
				
			}
		});
		frame.getContentPane().add(btnStartServer);
		
		
		
		
		JButton btnStopServer = new JButton("STOP SERVER");
		btnStopServer.setBounds(426, 12, 167, 39);
		btnStopServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				
				try {
					soc.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				status.append("Server Stopped\n");
			}
		});
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		frame.getContentPane().add(btnStopServer);
		
		JLabel lblLogDetails = new JLabel("LOG DETAILS");
		lblLogDetails.setBounds(99, 89, 204, 15);
		lblLogDetails.setFont(new Font("Dialog", Font.BOLD, 22));
		frame.getContentPane().add(lblLogDetails);
		
		
	}
	
	class AcceptClient extends Thread{
		
		 	Socket ClientSocket;
		    DataInputStream din;
		    DataOutputStream dout;
		    String LoginName=null;
		    AcceptClient (Socket CSoc) throws Exception
		    {
		        ClientSocket=CSoc;

		        din=new DataInputStream(ClientSocket.getInputStream());
		        dout=new DataOutputStream(ClientSocket.getOutputStream());
		        
		        LoginName=din.readUTF();
		        int stat=0;
		        for(int j=0;j<LoginNames.size();j++)
		        {
		        	if(LoginNames.elementAt(j).equals(LoginName))
		        	{
		        		stat=1;
		        		dout.writeUTF("!@#");
		        		CSoc.close();
		        		ClientSocket.close();
		        	}
	
		        }
		        if (stat==0)
		        {

		      //  System.out.println("User Logged In :" + LoginName);
		        status.append("User Logged In :" + LoginName+"\n");
		        LoginNames.add(LoginName);
		     //   System.out.println("ClientSocket : "+ClientSocket);
		        ClientSockets.add(ClientSocket);    
		        start();
		        }
		    }
		    
		    public void run()
		    {
		        while(true)
		        {
		            
		            try
		            {
		                String msgFromClient=new String();
		                msgFromClient=din.readUTF();
		                StringTokenizer st=new StringTokenizer(msgFromClient);
		                String Sendto=st.nextToken();                
		                String MsgType=st.nextToken();
		                int iCount=0;
		                int temp=0;
		                //System.out.println("MsgType="+MsgType);
		                if(MsgType.equals("LOG"))
		                {
		                	String snd="";
		                	//System.out.println("in server hey!!");
		                	for(int i=0;i<LoginNames.size();i++)
		                	{
		                		snd=snd+LoginNames.elementAt(i)+" ";
		                		if(LoginNames.elementAt(i).equals(Sendto))
		                		{
		                			temp=i;
		                		}
		                			
		                	}
		                
		                	//System.out.println("snd:"+snd);
		                	Socket tSoc=(Socket)ClientSockets.elementAt(temp);                            
                            DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                            tdout.writeUTF("Log "+snd); 
                            
                           // System.out.println("temp:"+temp+tSoc);
		                }
		    
		                else if(MsgType.equals("LOGOUT"))
		                {
		                    for(iCount=0;iCount<LoginNames.size();iCount++)
		                    {
		                        if(LoginNames.elementAt(iCount).equals(Sendto))
		                        {
		                            LoginNames.removeElementAt(iCount);
		                            ClientSockets.removeElementAt(iCount);
		                            status.append("User " + Sendto +" Logged Out ...\n");
		                            break;
		                        }
		                    }
		    
		                }
		                else
		                {
		                    String msg="";
		                    while(st.hasMoreTokens())
		                    {
		                        msg=msg+" " +st.nextToken();
		                    }
		                    for(iCount=0;iCount<LoginNames.size();iCount++)
		                    {
		                        if(LoginNames.elementAt(iCount).equals(Sendto))
		                        {    
		                            String msql="insert into message values('"+LoginName+ "','"+Sendto+"','"+msg+"')";
		                            //String msql="insert into message values("+"'"+LoginName+"'"+ ","+"'"+Sendto+"'"+","+"'"+msg+"'"+")";
		                        	stmt.executeUpdate(msql);
		                        	Socket tSoc=(Socket)ClientSockets.elementAt(iCount);                            
		                            DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
		                            tdout.writeUTF(LoginName+":"+msg);                            
		                            break;
		                        }
		                    }
		                    if(iCount==LoginNames.size())
		                    {
		                        dout.writeUTF(Sendto+":"+"I am offline");
		                    }
		                  
		                
		                }
		                if(MsgType.equals("LOGOUT"))
		                {
		                    break;
		                }

		            }
		            catch(Exception ex)
		            {
		                ex.printStackTrace();
		            }
		            
		            
		            
		        }        
		    }
	}
}
