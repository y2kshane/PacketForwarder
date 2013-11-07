package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DefaultCaret;
import packetforwarder.DBClass;
import packetforwarder.SystemStatusReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class MainUI extends javax.swing.JFrame {
  
   public static DefaultTableModel model = new DefaultTableModel(); 
  public static DefaultTableModel model2 = new DefaultTableModel(); 
   public static DefaultTableModel model3 = new DefaultTableModel(); 
   // The server socket.
//  private static ServerSocket serverSocket = null;
//   private static ServerSocket serverSocket_81 = null;
//  // The client socket.
//  private static Socket clientSocket = null;
//  private static Socket clientSocket_81 = null;

  // This chat server can accept up to maxClientsCount clients' connections.

  public static int port;
  public static int delay;
  public static int timeout;
  
  

  public static int wiDelay;
  public static int port_count;
   public static LinkedList<Integer> port_list = new LinkedList<Integer>();
 static packetServer  packetS[];
 //  public static int[] ports=new int[]{1};
  static webINFServer webServer;
  Timer tWebIntData;
  static DBClass obj2=new DBClass(); 
  public static LinkedList<String> waitingList = new LinkedList<String>();
 Timer tMemoryStat;
  Timer tClock;
 // Timer tRestart;
 //public static Timer tPKTRestart;
 Timer tMemoryStatLog;
 
 int m=0;
 int s=0;
 int h=0;
 int d=0;
  
 public static String ForceCloseID; 
public static int FCID;
  
  
  
  
  
// List<MyType> myList = new ArrayList<MyType>();
    /**
     * Creates new form MainUI
     */
    public MainUI() {
        
        initComponents();
      
//               tPKTRestart= new Timer(10*60*1000, new ActionListener() {
//     public void actionPerformed(ActionEvent evt) {
//         
//      
//             try{    
//        
//        restartApplication();
//        
//    }
//    catch(Exception ex)
//    {
//        
//    }
//					
//	
// 
//
//    }    
//    });
    
//        tRestart= new Timer(30*1000, new ActionListener() {
//     public void actionPerformed(ActionEvent evt) {
//         
//         Calendar cal = Calendar.getInstance(); //this is the method you should use, not the Date(), because it is desperated.
// 
//	int hour = cal.get(Calendar.HOUR_OF_DAY);//get the hour number of the day, from 0 to 23
//        int min= cal.get(Calendar.MINUTE);
//       if(hour == 2 && min==0){
//     //  if(hour == 18 && min==16){    
//             try{    
//        
//        restartApplication();
//        
//    }
//    catch(Exception ex)
//    {
//        
//    }
//					
//	}
// 
//
//    }    
//    });
        
       tClock = new Timer(1000, new ActionListener() {
     public void actionPerformed(ActionEvent evt) {
         
         
         
              s++;
        if(s>=60)
        {
           m++;
           s=0;
        }
           
        if(m>=60)
        {
            h++;
            m=0;
        }
            
        if(h>=24)
        {
            d++;
            h=0;
        }
         
        NumberFormat formatter = new DecimalFormat("00");
     //   String s = formatter.format(7);
  
      lblCount2.setText("D:"+formatter.format(d)+" H:"+formatter.format(h)+" M:"+formatter.format(m)+" S:"+formatter.format(s));
    
    
    
    }    
    });
        
        
        
     tMemoryStat= new Timer(5*1000, new ActionListener() {
     public void actionPerformed(ActionEvent evt) {
    
     getMemoryStat();
    }    
    });
     
     
    tMemoryStatLog= new Timer(60*1000, new ActionListener() {
     public void actionPerformed(ActionEvent evt) {
    
     updateMemoryStatLog();
    }    
    });
        
        tClock.start();
       tMemoryStatLog.setInitialDelay(0);  
       tMemoryStat.setInitialDelay(0);  
       tMemoryStatLog.start();
       tMemoryStat.start();
      //  tRestart.start();
     //   tPKTRestart.start();
    //   txtConsole
       DefaultCaret caret = (DefaultCaret)txtConsole.getCaret();
       caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); 
       
       
        tblLog.getTableHeader().setReorderingAllowed(false);
        tblLog.setModel(model);
        tblLog.setAutoCreateRowSorter(true);
        
        tblRMU.setModel(model2);
        tblRMU.setAutoCreateRowSorter(true);
        
        tblPort.setModel(model3);
        tblPort.setAutoCreateRowSorter(true);
        
     
        model3.addColumn("Port");
        model3.addColumn("State");
        
      //  model.addColumn("ID"); 
        model.addColumn("RTU ID"); 
     //   model.addColumn("Req IDs"); 
        model.addColumn("IP");
        model.addColumn("Statues");
        model.addColumn("Connectivity");
        model.addColumn("Last Statues Time");
    //    model.addColumn("Last Communication Time"); 
        model.addColumn("Up Time");
     //   model.addColumn("Thread ID");
        
        
        model2.addColumn("RMU ID");
        model2.addColumn("Requested IDs");
        
        tblRMU.getColumn("RMU ID").setWidth(150);
     //   tblRMU.getColumn("RMU ID").setMinWidth(70);
        tblRMU.getColumn("RMU ID").setMaxWidth(300);
        
//        tblLog.getColumn("Req IDs").setWidth(100);
//        tblLog.getColumn("Req IDs").setMinWidth(100);
//        tblLog.getColumn("Req IDs").setMaxWidth(100);
        
        tblLog.getColumn("RTU ID").setWidth(70);
      //  tblLog.getColumn("RTU ID").setMinWidth(70);
        tblLog.getColumn("RTU ID").setMaxWidth(300);
        
        tblLog.getColumn("Connectivity").setWidth(1);
        tblLog.getColumn("Connectivity").setMinWidth(1);
        tblLog.getColumn("Connectivity").setMaxWidth(1);
        
//        tblLog.getColumn("Statues").setWidth(330);
//        tblLog.getColumn("Statues").setMinWidth(330);
//        tblLog.getColumn("Statues").setMaxWidth(330);
        
//        tblLog.getColumn("Last Communication Time").setWidth(160);
//        tblLog.getColumn("Last Communication Time").setMinWidth(160);
//        tblLog.getColumn("Last Communication Time").setMaxWidth(160);
        
        tblLog.getColumn("IP").setWidth(130);
        tblLog.getColumn("IP").setMinWidth(130);
        tblLog.getColumn("IP").setMaxWidth(300);
        
//        tblLog.getColumn("ID").setWidth(50);
//        tblLog.getColumn("ID").setMinWidth(50);
//        tblLog.getColumn("ID").setMaxWidth(50);
        
        tblLog.getColumn("Up Time").setWidth(200);
        tblLog.getColumn("Up Time").setMinWidth(150);
        tblLog.getColumn("Up Time").setMaxWidth(300);
        
        tblLog.getColumn("Last Statues Time").setWidth(150);
        tblLog.getColumn("Last Statues Time").setMinWidth(150);
        tblLog.getColumn("Last Statues Time").setMaxWidth(300);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLog = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRMU = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtConsole = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblFreeMemory = new javax.swing.JLabel();
        lblAvailableMemory = new javax.swing.JLabel();
        lblUsedMemory = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblCurrentHeap = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtMemorylog = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        bttnSave = new javax.swing.JButton();
        txtPort = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        spDelay = new javax.swing.JSpinner();
        spTimeout = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        bttnSave1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPort = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblCount = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblCount1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblCount2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblCount3 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Packet Forwarder 1.32");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        tblLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblLog.setName("tblLog"); // NOI18N
        tblLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblLogMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblLogMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblLog);
        tblLog.getAccessibleContext().setAccessibleName("tblLog");

        jButton1.setText("Client Count");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setText("Wait List Count");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("Restart");
        jButton3.setEnabled(false);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton4.setText("Logged List");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1285, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(19, 19, 19))
        );

        jTabbedPane1.addTab("RTU Statues", jPanel1);

        tblRMU.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "RMU ID", "Requested Div"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblRMU);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(715, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("RMU", jPanel3);

        txtConsole.setBackground(new java.awt.Color(0, 0, 0));
        txtConsole.setColumns(20);
        txtConsole.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        txtConsole.setForeground(new java.awt.Color(51, 204, 0));
        txtConsole.setRows(5);
        jScrollPane4.setViewportView(txtConsole);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1285, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Console", jPanel4);

        jLabel7.setText("Free Memory ");

        jLabel9.setText("Max Heap Size ");

        jLabel10.setText("Used Memory ");

        lblFreeMemory.setText("0 MB");

        lblAvailableMemory.setText("0 MB");

        lblUsedMemory.setText("0 MB");

        jLabel11.setText("Current Heap Size ");

        lblCurrentHeap.setText("0 MB");

        txtMemorylog.setColumns(20);
        txtMemorylog.setRows(5);
        jScrollPane5.setViewportView(txtMemorylog);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAvailableMemory)
                    .addComponent(lblFreeMemory)
                    .addComponent(lblUsedMemory)
                    .addComponent(lblCurrentHeap))
                .addGap(62, 62, 62)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(727, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(lblCurrentHeap))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblFreeMemory))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(lblUsedMemory))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAvailableMemory)
                            .addComponent(jLabel9))
                        .addGap(0, 465, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Performance Statistic", jPanel5);

        bttnSave.setText("Save");
        bttnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bttnSaveMouseClicked(evt);
            }
        });
        bttnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSaveActionPerformed(evt);
            }
        });

        txtPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPortActionPerformed(evt);
            }
        });

        jLabel2.setText("Port");

        jLabel3.setText("Response Delay");

        jLabel4.setText("TimeOut");

        jLabel5.setText("ms");

        jLabel6.setText("S");

        bttnSave1.setText("Add");
        bttnSave1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bttnSave1MouseClicked(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tblPort.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Port", "State"
            }
        ));
        jScrollPane3.setViewportView(tblPort);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spDelay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spTimeout, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bttnSave, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(18, 18, 18)
                            .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bttnSave1))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(677, 677, 677))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(bttnSave1))
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(bttnSave)
                        .addGap(0, 342, Short.MAX_VALUE))
                    .addComponent(jSeparator1))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bttnSave.getAccessibleContext().setAccessibleName("bttnSave");
        txtPort.getAccessibleContext().setAccessibleName("txtport");

        jTabbedPane1.addTab("Settings", jPanel2);

        jLabel1.setText("Connected Clients");

        lblCount.setText("000");

        jLabel8.setText("Logged Clients");

        lblCount1.setText("000");

        jLabel12.setText("Status Sending Clients");

        lblCount2.setText("DD HH:MM:SS");

        jLabel13.setText("Up Time");

        lblCount3.setText("000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCount2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCount3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCount1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCount)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblCount)
                            .addComponent(jLabel8)
                            .addComponent(lblCount1)
                            .addComponent(jLabel12)
                            .addComponent(lblCount3)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(lblCount2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
public static JPopupMenu jpopup;
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
     getSettings();
     
          jpopup = new JPopupMenu();

    //JMenuItem javaCupMI = new JMenuItem("Example", new ImageIcon("javacup.gif"));
    //jpopup.add(javaCupMI);

 //   jpopup.addSeparator();

    JMenuItem exitMI = new JMenuItem(new AbstractAction("Force Close") {
    public void actionPerformed(ActionEvent ae) {
   
  
        
         for(int z=0;z<packetServer.threads.size();z++)
         { 
             
            if(packetServer.threads.get(z).Div_Name.length()>0)
            { 
             
           // JOptionPane.showMessageDialog(null, threads.size(), "size1" , JOptionPane.INFORMATION_MESSAGE); 
              if(packetServer.threads.get(z).Div_Name.contentEquals(ForceCloseID))
             {  
                 try {
               //  JOptionPane.showMessageDialog(null,packetServer.threads.get(z).Div_Name, "title" , JOptionPane.INFORMATION_MESSAGE);
               
                       packetServer.threads.get(z).Connect=false;  
                          
                     
                    //  packetServer.threads.get(z).is.close();
                  //   packetServer.threads.get(z).os.close();
                     packetServer.threads.get(z).clientSocket.close();
                     packetServer.threads.get(z).DBobj.disconnect();  //v 1.3
                     packetServer.threads.get(z).tDisplaytime.stop();
                     
                 } catch (IOException ex) {
                     Logger.getLogger(packetServer.class.getName()).log(Level.SEVERE, null, ex);
                 }
               //  packetServer.threads.remove(z);
                  MainUI.tblLog.setValueAt("Disconnected", FCID, 3);
                  MainUI.getConnectionCount();
                  packetServer.clearLinkedList();
                  break;
             }
           }
         }
        
        
        
        
        
        
        
    }
});
    jpopup.add(exitMI);
     
     packetS=new packetServer[port_list.size()];
//     jButton1.setEnabled(false);
  //   jButton2.setEnabled(false);
     
     
//        for(int i=0;i<port_list.size();i++)
//       {
//    
//        packetS[i]=new packetServer(port_list.get(i));
//       packetS[i].start();
//       
//            for(int x=0;x<model3.getRowCount() ;x++)
//            {
//                if(port_list.get(i)==(int)model3.getValueAt(x, 0))
//                    model3.setValueAt("Running", x, 1);
//              
//                          
//            }
//       }
//     
       packetS[0]=new packetServer(2900);
       packetS[0].start();
     // webServer=new webINFServer();
     // webServer.start();   
        
        
//      tWebIntData = new Timer(wiDelay*1000, new ActionListener() {
//    public void actionPerformed(ActionEvent evt) {
//       
//     obj2.deleteAllPacketData();   
//for(int i=0;i<	MainUI.tblLog.getRowCount();i++)
//{
//    
//JOptionPane.showMessageDialog(null, "web interface", "title" , JOptionPane.INFORMATION_MESSAGE);  
//       String mSignID=MainUI.tblLog.getValueAt(i, 1).toString();
//       if(mSignID=="LOG")
//       {
//           mSignID=mSignID.substring(12);
//           mSignID.replace("#", "");
//       }
//       String mID=MainUI.tblLog.getValueAt(i, 0).toString();
//       String mIP=MainUI.tblLog.getValueAt(i, 2).toString();
//       String mLastPacket=MainUI.tblLog.getValueAt(i, 3).toString();
//       String mConectivity=MainUI.tblLog.getValueAt(i, 4).toString();
//       String mConnectedtime=MainUI.tblLog.getValueAt(i, 7).toString();
//       String mLastPackettime=MainUI.tblLog.getValueAt(i, 5).toString();
//       String mLastConnectedTime=MainUI.tblLog.getValueAt(i, 6).toString();
//   //     obj2.insertPacketData(mSignID, mID, mIP, mLastPacket, mConectivity, mConnectedtime, mLastPackettime, mLastConnectedTime);
//        
//   }
//    }    
//});
   //   tWebIntData.start();
      obj2.getConnections();
      
//MyTableCellRender objRen=new MyTableCellRender();
//objRen.getTableCellRendererComponent(tblLog, "fsd", true, true, 0, 0);
tblLog.setDefaultRenderer(Object.class, new MyTableCellRender());
    

    }//GEN-LAST:event_formWindowOpened

    
    public static void getConnectionCount()
    {
        int count=0;
        if(packetS.length>0)
           count+=packetS[0].threads.size();
           
            
      
          MainUI.lblCount.setText(String.valueOf(count));
    }
    
    
    
    
    
  public static final String SUN_JAVA_COMMAND = "sun.java.command";  
    
    
    
    
    
    public static void restartApplication() throws IOException {

try {

// java binary

String java = System.getProperty("java.home") + "/bin/java";

// vm arguments

List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

StringBuffer vmArgsOneLine = new StringBuffer();

for (String arg : vmArguments) {

// if it's the agent argument : we ignore it otherwise the

// address of the old application and the new one will be in conflict

if (!arg.contains("-agentlib")) {

vmArgsOneLine.append(arg);

vmArgsOneLine.append(" ");

}

}

// init the command to execute, add the vm args

final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

 

// program main and program arguments

String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");

// program main is a jar

if (mainCommand[0].endsWith(".jar")) {

// if it's a jar, add -jar mainJar

cmd.append("-jar " + new File(mainCommand[0]).getPath());

} else {

// else it's a .class, add the classpath and mainClass

cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);

}

// finally add program arguments

for (int i = 1; i < mainCommand.length; i++) {

cmd.append(" ");

cmd.append(mainCommand[i]);

}

// execute the command in a shutdown hook, to be sure that all the

// resources have been disposed before restarting the application

Runtime.getRuntime().addShutdownHook(new Thread() {

@Override

public void run() {

try {

Runtime.getRuntime().exec(cmd.toString());

} catch (IOException e) {

e.printStackTrace();

}

}

});

// execute some custom code before restarting

//if (runBeforeRestart!= null) {
//
//runBeforeRestart.run();
//
//}

// exit

System.exit(0);

} catch (Exception e) {

// something went wrong

throw new IOException("Error while trying to restart the application", e);

}

}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void bttnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bttnSaveMouseClicked
        
        if(isInteger(txtPort.getText()))
        prefClass.prefs.putInt("port",Integer.parseInt(txtPort.getText()));
        else
         JOptionPane.showMessageDialog(null, "Port must be a numeric value.", "Type Error" , JOptionPane.ERROR_MESSAGE);  
        
        if(Integer.parseInt(spDelay.getValue().toString())>=0)
        prefClass.prefs.putInt("Response Delay",Integer.parseInt(spDelay.getValue().toString()));
        else
         JOptionPane.showMessageDialog(null, "Response Delay can't be a negative value.", "Type Error" , JOptionPane.ERROR_MESSAGE);  
        
        if(Integer.parseInt(spTimeout.getValue().toString())>=0)
        prefClass.prefs.putInt("TimeOut",Integer.parseInt(spTimeout.getValue().toString()));
        else
        JOptionPane.showMessageDialog(null, "Response Delay can't be a negative value.", "Type Error" , JOptionPane.ERROR_MESSAGE); 
        
//         if(Integer.parseInt(spWebDelay.getValue().toString())>=0)
//        prefClass.prefs.putInt("WIDelay",Integer.parseInt(spWebDelay.getValue().toString()));
//        else
//         JOptionPane.showMessageDialog(null, "Web Interface Delay can't be a negative value.", "Type Error" , JOptionPane.ERROR_MESSAGE);  
//        
        
        String tmp_p="";
       // for(int i=0;i<ports.size();i++)
        for(int i=0;i<tblPort.getRowCount();i++)
        {
          if(i==(tblPort.getRowCount()-1))
              tmp_p+=tblPort.getValueAt(i, 0).toString();
          else 
         tmp_p+=tblPort.getValueAt(i, 0).toString()+" ";
            
        }
        
        JOptionPane.showMessageDialog(null, tmp_p, "title" , JOptionPane.INFORMATION_MESSAGE);  
         prefClass.prefs.put("Ports",tmp_p);
         
        prefClass.prefs.putInt("Port Count",tblPort.getRowCount());
        getSettings();
    }//GEN-LAST:event_bttnSaveMouseClicked

    private void txtPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPortActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
  
       
       int confirmed = JOptionPane.showConfirmDialog(null, 
        "Are you sure you want to exit the program?", "Are You Sure?",
        JOptionPane.YES_NO_OPTION);
       
        if (confirmed == JOptionPane.YES_OPTION) {
                 callInsertPackData();
                dispose();
                System.exit(0);
    }
       
       
       
    }//GEN-LAST:event_formWindowClosing

    private void tblLogMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLogMouseEntered
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblLogMouseEntered

    private void bttnSave1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bttnSave1MouseClicked
        // TODO add your handling code here:
        
        boolean match=false;
        for(int i=0;i<tblPort.getRowCount();i++)
        {
            
            if(txtPort.getText().contains(model3.getValueAt(i, 0).toString()))
            {   
             match=true;
             break;
            }
            
 
        }
        
        if(match==true)
            JOptionPane.showMessageDialog(null, "New Port is already in the Port list", "Error" , JOptionPane.ERROR_MESSAGE); 
       else
        model3.addRow(new Object[]{txtPort.getText(), "Not Running"});     
   
    }//GEN-LAST:event_bttnSave1MouseClicked

    private void bttnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bttnSaveActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
       
        
        JOptionPane.showMessageDialog(null, packetServer.threads.size(), "clients" , JOptionPane.INFORMATION_MESSAGE); 
        
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        
       
        
                JOptionPane.showMessageDialog(null, MainUI.waitingList.size(), "wait list" , JOptionPane.INFORMATION_MESSAGE); 
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        
        
        
//    try{    
//        
//      //  restartApplication();
//        
//    }
//    catch(Exception ex)
//    {
//        
//    }
        
        
    }//GEN-LAST:event_jButton3MouseClicked

    private void tblLogMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLogMousePressed
        // TODO add your handling code here:
        
        // Left mouse click
		if ( SwingUtilities.isLeftMouseButton( evt ) )
		{
			// Do something
		}
		// Right mouse click
		else if ( SwingUtilities.isRightMouseButton( evt ))
		{
                    
                     int row = tblLog.rowAtPoint(evt.getPoint());
                     FCID=row;
                    // JOptionPane.showMessageDialog(null, MainUI.tblLog.getValueAt( row, 0), "title" , JOptionPane.INFORMATION_MESSAGE); 
                    //  MainUI.tblLog.setValueAt("Disconnected", rowNumberLS, 3);
                     ForceCloseID= MainUI.tblLog.getValueAt( row, 0).toString();
                   // tblLog.rowAtPoint(evt.getPoint());
                    
                 jpopup.setLocation(evt.getX(), evt.getY()+120);
                jpopup.setInvoker(jpopup);
                jpopup.setVisible(true);
                    
//                    JOptionPane.showMessageDialog(null, "message", "title" , JOptionPane.INFORMATION_MESSAGE); 
//                    jPopupMenu1.show(tblLog, evt.getX(), evt.getY());
                   
			// get the coordinates of the mouse click
			Point p = evt.getPoint();
 
			// get the row index that contains that coordinate
			int rowNumber = tblLog.rowAtPoint( p );
 
			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = tblLog.getSelectionModel();
 
			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			model.setSelectionInterval( rowNumber, rowNumber );
		}
       
        
    }//GEN-LAST:event_tblLogMousePressed

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:
        
        JOptionPane.showMessageDialog(null,  packetServer.LoggedRMUs.size(), "Logged RMU" , JOptionPane.INFORMATION_MESSAGE); 
        
    }//GEN-LAST:event_jButton4MouseClicked

    public void updateMemoryStatLog()
    {
        
        String[] line=txtMemorylog.getText().split("\\n");
        
        
        if(line.length>20)
            txtMemorylog.setText("");
        
         Date today = Calendar.getInstance().getTime();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
         txtMemorylog.setText(txtMemorylog.getText()+"\n"+formatter.format(today)+"  "+getCurrentlyUsingMemory()+" MB");
         
              File myFile = new File(System.getProperty("user.dir")+"/performance log.txt");

       if(!myFile.exists())
       {
            try 
	    {
		 myFile.createNewFile();
	    } catch (IOException e) 
	    {		
		e.printStackTrace();
	    }
	}

       
       		try {
                  //    Date today = Calendar.getInstance().getTime();
                   //  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
         
			FileWriter out = new FileWriter(myFile,true);
			out.write(formatter.format(today)+"  "+lblUsedMemory.getText()+"\n");
			out.close();
	

		} catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "title" , JOptionPane.INFORMATION_MESSAGE); 
		}
    }
    
    public void getMemoryStat()
    {
        
          
        lblFreeMemory.setText(getFreeMemory()+" MB");
        lblAvailableMemory.setText(getMaxMemory()+" MB");
        lblUsedMemory.setText(getCurrentlyUsingMemory()+" MB");
        lblCurrentHeap.setText(getCurrentHeapSize()+" MB");
        
  


    }
    
    
    public long getMegabytes(long byt)
    {
        return (byt/1000)/1000;
    }
    public String getFreeMemory()
    {
        return String.valueOf(getMegabytes(Runtime.getRuntime().freeMemory()));
    }
    
      public  String getCurrentHeapSize()
    {
        return String.valueOf(getMegabytes(Runtime.getRuntime().totalMemory()));
    }
    
     public  String getCurrentlyUsingMemory()
    {
        return String.valueOf(getMegabytes(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
    }
     
      public  String getMaxMemory()
    {
         long maxMemory = Runtime.getRuntime().maxMemory();
       if ( maxMemory == Long.MAX_VALUE) 
         return   "No Limit" ;
        else
         return  String.valueOf(getMegabytes(maxMemory));
        
    }
    
    
    public static void getSettings()
    {
        port= prefClass.prefs.getInt("port", 3000);
        delay=prefClass.prefs.getInt("Response Delay", 2);
        timeout=prefClass.prefs.getInt("TimeOut", 300);
        wiDelay=prefClass.prefs.getInt("WIDelay", 30);
        port_count=prefClass.prefs.getInt("Port Count", 0);
        
       txtPort.setText(String.valueOf(port));
       spDelay.setValue(delay);
       spTimeout.setValue(timeout);
       
       String raw_ports_str=prefClass.prefs.get("Ports", "");
       String[] portss=new String[]{};
       if (raw_ports_str.length()>0)
        portss=raw_ports_str.split(" ");
       
        while (!port_list.isEmpty()) {
        port_list.removeFirst();
    }
    //   port_list.
        // JOptionPane.showMessageDialog(null,raw_ports_str, "title" , JOptionPane.INFORMATION_MESSAGE);          
         for(int i=0;i<portss.length ;i++)
        {
            port_list.add(Integer.parseInt(portss[i]));
            model3.addRow(new Object[]{Integer.parseInt(portss[i]), "Not Running"});
          
          //  JOptionPane.showMessageDialog(null,portss[i], "title" , JOptionPane.INFORMATION_MESSAGE);  
        }
        //JOptionPane.showMessageDialog(null,port_list.size(), "title" , JOptionPane.INFORMATION_MESSAGE);  
  //     spWebDelay.setValue(wiDelay);
       
   //    JOptionPane.showMessageDialog(null, port_count, "title" , JOptionPane.INFORMATION_MESSAGE);  
    }
    
    
    
    
    public static boolean isInteger(String s) {
    try { 
        Integer.parseInt(s); 
    } catch(NumberFormatException e) { 
        return false; 
    }
    // only got here if we didn't return false
    return true;
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
        // This will define a node in which the preferences can be stored
       
        prefClass pfObj=new prefClass();
        pfObj.setup();
     //  webServer=new webINFServer();
     //  webServer.start();
   //     JOptionPane.showMessageDialog(null,port_list.size(), "title" , JOptionPane.INFORMATION_MESSAGE);  
    
        //int portNumber = 3000;
//      try {
//      serverSocket = new ServerSocket(prefClass.prefs.getInt("port", 3000));
//      serverSocket_81=new ServerSocket(81);
//    } catch (IOException e) {
//      System.out.println(e);
//    }
//      
      
      
       /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
    
      
   

    }   
    
    public static void callInsertPackData()
    {
                          MainUI.obj2.deleteAllPacketData();   
            for(int i=0;i<MainUI.tblLog.getRowCount();i++)
            {
    
       String mSignID=MainUI.tblLog.getValueAt(i, 0).toString();

    //   String mID=MainUI.tblLog.getValueAt(i, 0).toString();
       String mIP=MainUI.tblLog.getValueAt(i, 1).toString();
       String mLastPacket=MainUI.tblLog.getValueAt(i, 2).toString();
     //  String mReq=MainUI.tblLog.getValueAt(i, 2).toString();
       String mConectivity=MainUI.tblLog.getValueAt(i, 3).toString();
       String mConnectedtime=MainUI.tblLog.getValueAt(i, 5).toString();
       String mLastPackettime=MainUI.tblLog.getValueAt(i, 4).toString();
     //  String mLastConnectedTime=MainUI.tblLog.getValueAt(i, 7).toString();
        MainUI.obj2.insertPacketData(mSignID,  mIP, mLastPacket, mConectivity, mConnectedtime, mLastPackettime);
        
   }
     
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void webIFSetSettings(Integer Port,Integer RDelay,Integer Timeout)
    {
         
        prefClass.prefs.putInt("port",Port);
      
    
        prefClass.prefs.putInt("Response Delay",RDelay);
    
    
        prefClass.prefs.putInt("TimeOut",Timeout);
    
      
 //       prefClass.prefs.putInt("WIDelay",Integer.parseInt(spWebDelay.getValue().toString()));
     
        
        getSettings();
    }
    
//    public void restartApplication()
//{
//  final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
//  final File currentJar = new File(UpdateReportElements.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//
//  /* is it a jar file? */
//  if(!currentJar.getName().endsWith(".jar"))
//    return;
//
//  /* Build command: java -jar application.jar */
//  final ArrayList<String> command = new ArrayList<String>();
//  command.add(javaBin);
//  command.add("-jar");
//  command.add(currentJar.getPath());
//
//  final ProcessBuilder builder = new ProcessBuilder(command);
//  builder.start();
//  System.exit(0);
//}
    
//    public static void testport81()
//    {
//         DataInputStream is = null;
//         PrintStream os = null;
//       try {
//           //Socket clientSocket ;
//            is = new DataInputStream(clientSocket_81.getInputStream());
//            os = new PrintStream(clientSocket_81.getOutputStream());
//             
//        while (true) {
//        String line=is.readLine();
//        
//        if(line!=null)
//        {
//         //   os.println("ACK+");
//           JOptionPane.showMessageDialog(null, line, "Data" , JOptionPane.INFORMATION_MESSAGE);  
//
//        }
//    
//        }
//             
//       } catch (IOException ex) {
//         ex.printStackTrace();
//       }
//     
//      
//
//    }
    
 
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttnSave;
    private javax.swing.JButton bttnSave1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAvailableMemory;
    public static javax.swing.JLabel lblCount;
    public static javax.swing.JLabel lblCount1;
    public static javax.swing.JLabel lblCount2;
    public static javax.swing.JLabel lblCount3;
    private javax.swing.JLabel lblCurrentHeap;
    private javax.swing.JLabel lblFreeMemory;
    private javax.swing.JLabel lblUsedMemory;
    private static javax.swing.JSpinner spDelay;
    private static javax.swing.JSpinner spTimeout;
    public static javax.swing.JTable tblLog;
    private javax.swing.JTable tblPort;
    public static javax.swing.JTable tblRMU;
    public static javax.swing.JTextArea txtConsole;
    private javax.swing.JTextArea txtMemorylog;
    private static javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables


}







class webINFClientThread extends Thread {

 // public static LinkedList<String> waitingList = new LinkedList<String>();
    
  private DataInputStream is = null;
  private PrintStream os = null;
  private final Socket clientSocket ;

  private int maxClientsCount;
  final int clientCount;
  private DBClass DBobj;
  String serverType;
  String sendData;
  Boolean Connect=true;
 DataOutputStream outToServer ;
                            //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     //       outToServer.writeBytes(json.toString() + '\n');

  private static final String DISPLAY_FORMAT_STR = "%02d";
  

  public webINFClientThread(final Socket clientSocket, final int clientCount) {
    this.clientSocket = clientSocket;
    this.clientCount=clientCount;
    DBobj=new DBClass();
  
    serverType=clientSocket.getRemoteSocketAddress().toString();
    
    
  }

  public void run() {
    int maxClientsCount = this.maxClientsCount;
    
    try {
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
     outToServer = new DataOutputStream(clientSocket.getOutputStream());
      while (true) {
        String line = is.readLine(); //if php script doesnt give string with \r\n this will stuck

        if(line==null)
        {
          Connect = false;
        break;
        }
        else
        { 
             Connect = false;
            // os.println("All the values have to be numeric"+"\0");  
             if(line.contains("+WEB:SET:Port"))
             {
                String[] splitstr=line.split(":");
                String[] port=splitstr[2].split("\\|");
                String[] Rdelay=splitstr[3].split("\\|");
                String[] TimeOut=splitstr[4].split("\\|");
                
                if(MainUI.isInteger(port[1]) || MainUI.isInteger(Rdelay[1]) || MainUI.isInteger(TimeOut[1]))
                {
                    MainUI.webIFSetSettings(Integer.parseInt(port[1]),Integer.parseInt(Rdelay[1]),Integer.parseInt(TimeOut[1]));
                }
            //    else
               //    os.println("All the values have to be numeric");           
            }
            else if(line.contains("+WEB:GET:Connection"))
             {
                 MainUI.callInsertPackData();
      
               if(MainUI.tblLog.getRowCount()>0)
                     os.println("have items"+"\n"); 
                   else
               os.println("no items"+"\n"); 
            
             }
            else
            {
                 
                   try {
                        clientSocket.close();
                       } catch (IOException ex) {
                         JOptionPane.showMessageDialog(null, ex.getMessage(), "Error" , JOptionPane.ERROR_MESSAGE);  
                      }
            }
        }
    //      int u=0;
      //    if(u==0)
          
      //    u++;
        clientSocket.close();
        Connect = false;
        break;
      }
      
      
    } catch (IOException e) {
        
    }
  }
  

  


}


 class prefClass {
    public static Preferences prefs;
    public void setup()
    {
     prefs = Preferences.userRoot().node(this.getClass().getName());
    }
    
}



class PacketClientThread extends Thread {

   //java.util.Date date= new java.util.Date();
 // public DefaultTableModel model ; 
    
     //  clearLinkedList();
  public  DBClass DBobj;
  String Device_Type="RTU";
  String Console_Command_Type="";
 public String Packet_IP;  
    
    
  public  String LoggedDivName;  
  String Div_Name;
  int rowNumberLS;
  int rowNumberRMU;
  boolean gotDivID=false;
  boolean logged=false;
  boolean isFromRequest=false;
 // private packetServer objPS;    
  Boolean Connect=true;
  boolean Initial_Request2=false;  
 // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
 // private DataInputStream is = null;
 public BufferedReader is=null;
  public PrintStream os = null;
  public final Socket clientSocket ;
 //private final clientThread[] threads;
  private int maxClientsCount;
  Timer tResponsDly;
  Timer tRTUACkdlay;
  Timer tDisplaytime;
  Timer tDeadDscnt;
  final long clientCount;

  String serverType;
  String sendData;
  String lastSignId="";
  int rowcnt;
  LinkedList<String> PrivateWaitingList = new LinkedList<String>();
  boolean Initial_Request;
  //String[] PrivateWaitingList;
  
  //time-----------
  Integer d=0;
  Integer h=0;
  Integer m=0;
  Integer s=0;
  private static final String DISPLAY_FORMAT_STR = "%02d";
  

  public PacketClientThread(final Socket clientSocket, final long clientCount) {
      DBobj=new DBClass();
      //  this.objPS = new packetServer();
    this.clientSocket = clientSocket;
    
    Packet_IP=clientSocket.getRemoteSocketAddress().toString();
//    this.threads = threads;
  //  this.model=model;
//    maxClientsCount = threads.length;
    this.clientCount=clientCount;
    rowcnt=(int)clientCount;
    rowcnt--;
 //   JOptionPane.showMessageDialog(null,String.valueOf(rowcnt) , "title" , JOptionPane.INFORMATION_MESSAGE);  
   // DBobj=new DBClass();
    serverType=clientSocket.getRemoteSocketAddress().toString();
  // JOptionPane.showMessageDialog(null, "packet", "port" , JOptionPane.INFORMATION_MESSAGE);  

    tDisplaytime = new Timer(1000, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
	//...Update the progress bar...
        s++;
        if(s>=60)
        {
           m++;
           s=0;
        }
           
        if(m>=60)
        {
            h++;
            m=0;
        }
            
        if(h>=24)
        {
            d++;
            h=0;
        }
         
        NumberFormat formatter = new DecimalFormat("00");
     //   String s = formatter.format(7);
  
        if(gotDivID==true)
         MainUI.tblLog.setValueAt("D:"+formatter.format(d)+" H:"+formatter.format(h)+" M:"+formatter.format(m)+" S:"+formatter.format(s), rowNumberLS,5);
        //  os.println("+ACK");
       //     tDisplaytime.stop();
     
    }    
});
    
    
//   tResponsDly = new Timer(MainUI.delay*1000, new ActionListener() {
//    public void actionPerformed(ActionEvent evt) {
//	//...Update the progress bar...
//
//  //        os.println("+ACK\r\n");
//            os.println(sendData);
//            tResponsDly.stop();
//     
//    }    
//});      
   
   tRTUACkdlay = new Timer(MainUI.delay, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {

     
          
             
             if(Initial_Request2)
           {
                os.println("+ACK"+(char)13+(char)10);
               // JOptionPane.showMessageDialog(null, "1st", "Error" , JOptionPane.ERROR_MESSAGE); 
                Initial_Request2=false;
               
                tRTUACkdlay.stop(); 
                 tRTUACkdlay.start();
                
                
           }
           else
           {//  JOptionPane.showMessageDialog(null, "2nd", "Error" , JOptionPane.ERROR_MESSAGE); 
                 os.println(sendData);
                 tRTUACkdlay.stop(); 
           }
                 
          
     
    }    
});
         
         
          tRTUACkdlay.setInitialDelay(MainUI.delay);   

    tResponsDly = new Timer(MainUI.delay, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {

           if(Initial_Request)
           {
               // os.println("+ACK"+(char)13+(char)10);
           //     os.println(sendData);
          //      Initial_Request=false;
        //        updateConsoleSend("+ACK");
               Initial_Request2=true;
               Initial_Request=false;
                tRTUACkdlay.start();
                tResponsDly.stop(); 
                
           }
           else
           {
                 os.println(sendData);
                tResponsDly.stop(); 
               
           }
             
                 
          
     
    }    
});
       
       
    tResponsDly.setInitialDelay(MainUI.delay);    
    
    
    tDeadDscnt= new Timer(MainUI.timeout*1000, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
         
        try {
            clientSocket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error" , JOptionPane.ERROR_MESSAGE);  
        }

           statUpdateDisconect();
         //  cxvx
            tDeadDscnt.stop();
            Connect=false;
         //   clearLinkedList();  changed this to clear every 1 mins
             removeLoggedMTU(rowcnt);
              if(lastSignId!="")
         {
            DBobj.updateDisconnectedStatues(lastSignId);
         }
     
    }    
});
    
    tDeadDscnt.setInitialDelay(MainUI.timeout*1000);
    
    
  }
  
  public void   removeLoggedMTU(long DivID)
  {//JOptionPane.showMessageDialog(null, DivID, "title" , JOptionPane.INFORMATION_MESSAGE); 
      
       for(int i=0;i<packetServer.LoggedRMUs.size();i++)
     {
            
         String[] tmp=packetServer.LoggedRMUs.get(i).split(":");
      //   JOptionPane.showMessageDialog(null, packetServer.LoggedRMUs.size(), "title" , JOptionPane.INFORMATION_MESSAGE); 
         if(tmp[1].contentEquals(String.valueOf(DivID)))
         {
                   
                  packetServer.LoggedRMUs.remove(i);
               //   JOptionPane.showMessageDialog(null, packetServer.LoggedRMUs.size(), "title in" , JOptionPane.INFORMATION_MESSAGE);
                 break;
         }

     }
  
  }
  
  
  
  
//  
//    public void   checkIP(String IP)
//  {
//
//
// //Packet_IP
//     for(int i=0;i<packetServer.threads.size();i++) 
//     {
//         
//      
//         if(packetServer.threads.get(i).Packet_IP.contentEquals(IP)) //check if attempting div is is in the logged list
//         {
//                
//          
//            
//              
//                     
//            
//                        try {
//                                packetServer.threads.get(i).clientSocket.close();
//                            } catch (IOException ex) {
//                                Logger.getLogger(PacketClientThread.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                 
//                        packetServer.threads.get(i).Connect=false;
//                         packetServer.threads.get(i).DBobj.disconnect(); //v 1.34
//                     
//                        packetServer.threads.get(i).statUpdateDisconect(); 
//        
//                        
//                 
//         }
//
//     } 
// 
//
//
//     logged=true;
// 
//     packetServer.LoggedRMUs.add(line.substring(12)+":"+rowcnt);
//
//         
//  }
  
  
  
  
  
  
  public void   checkLoggedMTUs(String ID,String line)
  {

if(packetServer.LoggedRMUs.size()>0)
{
    int loop=packetServer.LoggedRMUs.size();
     for(int i=0;i<loop;i++) //loop the LoggedRMUs list
     {
         
         String[] tmp=packetServer.LoggedRMUs.get(i).split(":");
         if(tmp[0].contentEquals(ID)) //check if attempting div is is in the logged list
         {
                
                  packetServer.LoggedRMUs.remove(i);  //if so remove it 
                  loop--;
                   for(int z=0;z<packetServer.threads.size();z++) //and diconnect (old copy)
                    {
                        if(packetServer.threads.get(z).rowcnt==Integer.parseInt(tmp[1]))
                        {
            
                        try {
                                packetServer.threads.get(z).clientSocket.close();
                            } catch (IOException ex) {
                                Logger.getLogger(PacketClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                 
                        packetServer.threads.get(z).Connect=false;
                         packetServer.threads.get(z).DBobj.disconnect(); //v 1.34
                     
                        packetServer.threads.get(z).statUpdateDisconect(); 
                    //    clearLinkedList(); changed this to clear every 1 mins
                        }
                    }
         }

     } 
 
}

     logged=true;
 
     packetServer.LoggedRMUs.add(line.substring(12)+":"+rowcnt);

         
  }
    
  
  
public void searchInWaitingList(String data)
{
    String divid="";
    String Sdata="";
    for(int i=0;i<MainUI.waitingList.size();i++)
    {
        if(data.substring(6, 9).contentEquals(MainUI.waitingList.get(i).substring(0, 3)))
        {  // Sdata=DBobj.searchRequestedPacket(MainUI.waitingList.get(i).substring(0, 3),"WaitingList");
         Sdata=data;
      //  if(Sdata.length()>10 && !Sdata.contains("?"))
         divid=MainUI.waitingList.get(i).substring(4);
   //     MainUI.waitingList.remove(i);
         
        for(int c=0;c< packetServer.threads.size();c++)
        {
            long tmpe=(long)Integer.parseInt(divid);
            if(packetServer.threads.get(c).clientCount==tmpe)
            {
              //  JOptionPane.showMessageDialog(null, "found", "title" , JOptionPane.INFORMATION_MESSAGE);  
            packetServer.threads.get(c).sendData=Sdata+"\r\n";
            packetServer.threads.get(c). tResponsDly.start();
            }
        }
      
        //break;
        }
  
    }

 
}
  

//public void searchRemoveLoggedMTU(String ID)
//{
//       for(int i=0;i<packetServer.LoggedRMUs.size();i++)
//     {
//         String[] tmp=packetServer.LoggedRMUs.get(i).split(":");
//         if(tmp[0].contentEquals(ID))
//         {
//        
//         }
//   
//     } 
//}

  
public String dtFormated()
    {
          Date date = new Date();
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          String formattedDate = sdf.format(date);
          return formattedDate;
    }
  public void run() {
    int maxClientsCount = this.maxClientsCount;
//    clientThread[] threads = this.threads;
    
    try {
      /*
       * Create input and output streams for this client.
       */
         is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
     // is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
  
   // MainUI.model.addRow(new Object[]{clientCount,"","",clientSocket.getRemoteSocketAddress().toString().replace("/", ""),"", "Connected","",dtFormated() ,""}); 
    tDisplaytime.start();
    tDeadDscnt.start();
   //  JOptionPane.showMessageDialog(null,"new", "titlev2" , JOptionPane.INFORMATION_MESSAGE);
      while (true) {
          
      //  JOptionPane.showMessageDialog(null,"got data", "title" , JOptionPane.INFORMATION_MESSAGE); 
          
        String line = is.readLine();
     //   JOptionPane.showMessageDialog(null,"got data 2", "titlev2" , JOptionPane.INFORMATION_MESSAGE); 
       
        if(line==null)
        {
      
          Connect = false;
         // clearLinkedList();
          removeLoggedMTU(rowcnt);
         statUpdateDisconect();
         
         
      MainUI.lblCount1.setText(String.valueOf(packetServer.LoggedRMUs.size()));
      MainUI.lblCount3.setText(String.valueOf(MainUI.waitingList.size()));

         
    
         
         if(lastSignId!="")
         {
           DBobj.updateDisconnectedStatues(lastSignId);
         }

       
//       if( packetServer.threads[clientCount]==null)
   //       JOptionPane.showMessageDialog(null, "null", "" , JOptionPane.INFORMATION_MESSAGE);

        break;
        }
        else
        {
            
           
           
           //  JOptionPane.showMessageDialog(null,line, "line" , JOptionPane.INFORMATION_MESSAGE); 
            Connect = true;
  // os.println("+ACK"+(char)13+(char)10); 
             if(line.contains("+DATA:")  )            {
                 MainUI.tblLog.repaint();
         //        os.println("+ACK"+(char)13+(char)10);
                // "+ACK"+(char)13+(char)10
              //   JOptionPane.showMessageDialog(null, line, "title" , JOptionPane.INFORMATION_MESSAGE); 
                 isFromRequest=false;
                 tDeadDscnt.restart();
                if(line.length()>12 && line.substring(9, 12).contains(",,,"))
                {
                    Console_Command_Type="HBT";
                  // MainUI.tblLog.setValueAt(dtFormated(), rowcnt, 5);
                   sendData="+ACK"+(char)13+(char)10;
                   statUpdate(line,"Hart"); 
                     
                }
                else if(line.contains("+DATA:") && line.substring(9, 10).contains("?"))
                {
                    Console_Command_Type="REQ";
                    isFromRequest=true;
                    if(logged==true)
                    {
                     //   JOptionPane.showMessageDialog(null, line, "title" , JOptionPane.INFORMATION_MESSAGE);  
                     Initial_Request=true;       
                     sendData= DBobj.searchRequestedPacket(line,"RealTimeReq")+"\r\n";
                     MainUI.waitingList.add(line.substring(6, 9)+":"+String.valueOf(clientCount));
                     PrivateWaitingList.add(line.substring(6, 9)+":"+String.valueOf(clientCount));
                     statUpdate(line,"Req",line.substring(6, 9)); 
                    }
                       
                }
                else if(line.contains("+DATA:LOGIN:"))
                {
                    Console_Command_Type="LOG";
                //    LoggedDivName
                    //login
                  checkLoggedMTUs(line.substring(12),line);
                  LoggedDivName=line.substring(12);
                  
                  statUpdate(line,"Log"); 
                  Device_Type="RMU";
                  sendData="+ACK\r\n";
                }
                else if(line.length()>12 )// better way to authenticate
                {
                    
                   char frstC=line.charAt(6);
                   char secondC=line.charAt(7); 
                   char thirdC=line.charAt(8);
                   
                  // JOptionPane.showMessageDialog(null, frstC + " "+secondC+" "+thirdC, "title" , JOptionPane.INFORMATION_MESSAGE); 
                  if(Character.isLetter(frstC) && Character.isDigit(secondC)  && Character.isDigit(thirdC)) 
                   {  
                    Console_Command_Type="STA";
                 sendData="+ACK"+(char)13+(char)10;
                String statues="PENDING";
                
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String formattedDate = sdf.format(date);
          //    MainUI.tPKTRestart.restart();
               lastSignId=line.substring(6, 9);
              DBobj.insertRawData(formattedDate,line.substring(6, 9),line,statues);
               
               if(! MainUI.waitingList.isEmpty())
               {
                   searchInWaitingList(line);
               }
                statUpdate(line,"Stat"); 
                
                }
                }
              
                   
                  if(isFromRequest==true  )
                  {
                      if(logged==true)
                          tResponsDly.start();
                  }
                  else
                      tResponsDly.start();
                  
            }
            else
            {
                   try {
                        clientSocket.close();
                        Connect = false;
                       } catch (IOException ex) {
                         JOptionPane.showMessageDialog(null, ex.getMessage(), "Error" , JOptionPane.ERROR_MESSAGE);  
                      }
                //   clearLinkedList();
                    statUpdateDisconect();  
            }
            
          updateConsoleReceive(line);
        }
        

      }

      
      MainUI.lblCount1.setText(String.valueOf(packetServer.LoggedRMUs.size()));
      MainUI.lblCount3.setText(String.valueOf(MainUI.waitingList.size()));

    } catch (IOException e) {
    }
  }
  
  public void statUpdateDisconect()
  {
      tDisplaytime.stop();
    //  JOptionPane.showMessageDialog(null, rowNumberLS, "title" , JOptionPane.INFORMATION_MESSAGE);  

    //   MainUI.tblLog.setValueAt(dtFormated(), clientCount, 4);
      if(gotDivID==true)
      {
     // MainUI.tblLog.setValueAt("D:N/A H:N/A M:N/A S:N/A", rowNumberLS, 5);
     
      MainUI.tblLog.setValueAt("Disconnected", rowNumberLS, 3);
    //  MainUI.lblCount.setText(String.valueOf(Integer.parseInt(MainUI.lblCount.getText())-1));
         MainUI.getConnectionCount();
      }
  }

 // public void updateConsoleSend(String str)
 // {
   //   MainUI.txtConsole.setText(MainUI.txtConsole.getText() + "\n"  +Div_Name+"Inf : -> TCP "+Device_Type+" "+Console_Command_Type+"Send >> "+ str);
      
  //}
  
 
   public void updateConsoleReceive(String str)
  {
   if(Device_Type.contentEquals("RMU"))
       MainUI.txtConsole.setText(MainUI.txtConsole.getText() + "\n"  +"INF : --> TCP "+Device_Type+" "+Console_Command_Type+" Received -  "+ Packet_IP+"/"+LoggedDivName); 
    else
    MainUI.txtConsole.setText(MainUI.txtConsole.getText() + "\n"  +"INF : --> TCP "+Device_Type+" "+Console_Command_Type+" Received -  "+ Packet_IP+"/"+Div_Name);
    //  String[] newlines=new String[50];
      String[] lines=MainUI.txtConsole.getText().split("\\n");
    //  JOptionPane.showMessageDialog(null,String.valueOf(lines.length), "title" , JOptionPane.INFORMATION_MESSAGE); 
      
      if(lines.length>50)
      {
          MainUI.txtConsole.setText("");
         // for(int i=0;i<=10;i++)
         //    MainUI.txtConsole.setText(MainUI.txtConsole.getText() + "\n"  +lines[lines.length-i]);
          
          
      }
  
      
      
  }
  
  
  
    public void statUpdate(String data,String ComType)
  {
       
      
      
      if(data.substring(6, 9).contentEquals("LOG"))
      {   
          
 LoggedDivName=data.substring(12).replace("#", "");
     
            boolean found=false;
            for(int i=0;i<MainUI.tblRMU.getRowCount();i++)
            {
              
                
                if(MainUI.tblRMU.getValueAt(i,0).equals(data.substring(13).replace("#", "")))
                {
                    //        gotDivID=true;
                    
                    MainUI.tblRMU.setValueAt("",i,1);
                    rowNumberRMU=i;
                    MainUI.tblRMU.setValueAt(data.substring(13).replace("#", ""), i, 0);

                    found=true;
                   break;
                }
  
            }
            if(found==false)
            {
         //     gotDivID=true;
                MainUI.model2.addRow(new Object[]{data.substring(13).replace("#", ""),""}); 
                rowNumberRMU=MainUI.tblRMU.getRowCount()-1;
       
                    
            }
            
    
       }
      else
      {
          Div_Name=data.substring(6, 9);
      boolean found=false;
      for(int i=0;i<MainUI.tblLog.getRowCount();i++)
      {
           if(MainUI.tblLog.getValueAt(i,0).equals(data.substring(6, 9)))
           {
                gotDivID=true;
                rowNumberLS=i;
       
                MainUI.tblLog.setValueAt(data.substring(6, 9), i, 0);
            //    MainUI.tblLog.setValueAt(dtFormated(), i, 7);
                if(ComType=="Stat")
                MainUI.tblLog.setValueAt(dtFormated(), i, 4);
                if(ComType=="Stat")
                MainUI.tblLog.setValueAt(data, i, 2);
                MainUI.tblLog.setValueAt("Connected", i, 3);
                found=true;
                break;
            }
  
        }
        if(found==false)
        {
            gotDivID=true;
            MainUI.model.addRow(new Object[]{data.substring(6, 9),clientSocket.getRemoteSocketAddress().toString().replace("/", ""),data, "Connected",dtFormated() ,""}); 
          rowNumberLS=MainUI.tblLog.getRowCount()-1;
        }
   

  }
  }
    
        public void statUpdate(String data,String ComType,String reqDiv)
  { //MainUI.model.addRow(new Object[]{clientCount,"","",clientSocket.getRemoteSocketAddress().toString().replace("/", ""),"", "Connected","",dtFormated() ,""}); 

    //  rowNumber


   if(MainUI.tblRMU.getValueAt(rowNumberRMU, 1).toString().length()>0)
       MainUI.tblRMU.setValueAt(reqDiv + "," + MainUI.tblRMU.getValueAt(rowNumberRMU, 1).toString(), rowNumberRMU, 1);
   else
      MainUI.tblRMU.setValueAt(reqDiv, rowNumberRMU, 1);
    //  MainUI.tblRMU.setValueAt(dtFormated(), rowNumberRMU, 1);

     // MainUI.tblLog.setValueAt("Connected", rowNumber, 5);
  }

}























class webINFServer extends Thread {
  private static ServerSocket serverSocket = null;
  private static Socket clientSocket = null;
  // public static  webINFClientThread threads =null; 
 // public static final webINFClientThread[] threads = new webINFClientThread[10]; 
  public static final LinkedList<webINFClientThread> threads = new LinkedList<webINFClientThread>();

  public webINFServer()
  {
//        threads.get()
  }
     public void run() {
      try {    
          serverSocket = new ServerSocket(6000);
      } catch (IOException ex) {
          Logger.getLogger(webINFServer.class.getName()).log(Level.SEVERE, null, ex);
      }
         
               
    while (true) {
          try {
              // try {
                   //  clientSocket_81=serverSocket_81.accept();
                //     testport81();
                 clientSocket = serverSocket.accept();
         //        threads = new webINFClientThread(clientSocket,0)).start();
          } catch (IOException ex) {
              Logger.getLogger(webINFServer.class.getName()).log(Level.SEVERE, null, ex);
          }
        
     //    for (int i = 0; i < 10; i++) {
     //     if (threads.get(i)== null) {
            //  String ip=Socket.getRemoteSocketAddress().toString();
          clearLinkedList();
              threads.add( new webINFClientThread(clientSocket,threads.size()-1));
              threads.get(threads.size()-1).start();
      MainUI.getConnectionCount();
        //    MainUI.lblCount.setText(String.valueOf(Integer.parseInt(MainUI.lblCount.getText())+1));

         
      }
      
    }
         
    
     public void clearLinkedList()
     {//JOptionPane.showMessageDialog(null, threads.size(), "title" , JOptionPane.INFORMATION_MESSAGE);  
         for(int i=0;i<threads.size();i++)
         {
             if(threads.get(i).Connect==false)
             {   //threads.get(i).stop();
                 threads.remove(i);
            //      JOptionPane.showMessageDialog(null, threads.size(), "title" , JOptionPane.INFORMATION_MESSAGE);  
             }
         }
    //    JOptionPane.showMessageDialog(null, threads.size(), "title" , JOptionPane.INFORMATION_MESSAGE);  
     }
         
         
         
         
      //   JOptionPane.showMessageDialog(null, "web server started", "title" , JOptionPane.INFORMATION_MESSAGE);  
     }
    
    
    
//}

class packetServer extends Thread {
  private static ServerSocket serverSocket = null;
  private static Socket clientSocket = null;
  private static final int maxClientsCount = 1000;
  private static Timer tSaveConnectins;
  private int port_local;
 Timer tCleanDeadClients;

 public static long clientCnt = 0;  
  
 public static final LinkedList<PacketClientThread> threads = new LinkedList<PacketClientThread>(); //client count
public static final LinkedList<String> LoggedRMUs = new LinkedList<String>();
 // public static final PacketClientThread[] threads = new PacketClientThread[maxClientsCount]; 
  
  public packetServer(int port)
  {
      port_local=port;
          tSaveConnectins = new Timer(30*1000, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {

      //  MainUI.callInsertPackData();
     
    }    
});
          
              tCleanDeadClients = new Timer(60*1000, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
        clearLinkedList();
        
    }
    });
  
          
          tCleanDeadClients.setInitialDelay(60*1000);      
        //  tSaveConnectins.start();
          tCleanDeadClients.start();
  }
  
  
   public static void   removeLoggedMTU(String DivID)//gets like MT01 so i add #
  {
     // JOptionPane.showMessageDialog(null, DivID, "title" , JOptionPane.INFORMATION_MESSAGE); 
       for(int i=0;i<packetServer.LoggedRMUs.size();i++)
     {
            
         String[] tmp=packetServer.LoggedRMUs.get(i).split(":");

         if(tmp[0].contentEquals(DivID+"#"))
         {
                   
                  packetServer.LoggedRMUs.remove(i);
              
                 break;
         }

     }
  
  }
  
  
  
  
     public void run() {
    
           try {
     // serverSocket = new ServerSocket(prefClass.prefs.getInt("port", 3000));
      serverSocket = new ServerSocket(port_local);
    } catch (IOException e) {
      System.out.println(e);
    }
      
           
       while (true) {
      try {
     
        clientSocket = serverSocket.accept();
      
      
       // for (i = 0; i < maxClientsCount; i++) {
       //  if (threads[i] == null) {
            //  String ip=Socket.getRemoteSocketAddress().toString();
       //    (threads[i] = new PacketClientThread(clientSocket,i)).start();
           clientCnt++;
         
           threads.add( new PacketClientThread(clientSocket,clientCnt));
           threads.get(threads.size()-1).start();
         MainUI.getConnectionCount();
        //    break;
       //   }
       //   else
       //   {
          

       //   }
      //   }

      } catch (IOException e) {
        System.out.println(e);
         
      }
      
    }
         
         
     }
    
     public static void clearLinkedList()
     {//JOptionPane.showMessageDialog(null, threads.size(), "title" , JOptionPane.INFORMATION_MESSAGE);  
         for(int z=0;z<packetServer.threads.size();z++)
         {
           // JOptionPane.showMessageDialog(null, threads.size(), "size1" , JOptionPane.INFORMATION_MESSAGE); 
             if(packetServer.threads.get(z).Connect==false)
             {   
                 try {
                     
                      for(int i=0;i<packetServer.threads.get(z).PrivateWaitingList.size();i++)
                    { //search and remove
             
                         for(int x=0;x<MainUI.waitingList.size();x++)
                         {
                             if(MainUI.waitingList.get(x).contentEquals(packetServer.threads.get(z).PrivateWaitingList.get(i)))
                            {
                                MainUI.waitingList.remove(x);
                                //   break;
                            }
                        }
             
              
                 }
                          
                          
                     
                   //   packetServer.threads.get(z).is.close();
                   //  packetServer.threads.get(z).os.close();
                     packetServer.threads.get(z).clientSocket.close();
                     packetServer.threads.get(z).DBobj.disconnect(); //new disconnect code v 1.3
                     
                 } catch (IOException ex) {
                     Logger.getLogger(packetServer.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 removeLoggedMTU(packetServer.threads.get(z).LoggedDivName);
              //   packetServer.threads.get(z).destroy();
                 packetServer.threads.remove(z);
            
             }
         }
         
          MainUI.getConnectionCount();
          MainUI.lblCount1.setText(String.valueOf(packetServer.LoggedRMUs.size()));
          MainUI.lblCount3.setText(String.valueOf(MainUI.waitingList.size()));
     }
    
}