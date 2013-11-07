/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packetforwarder;

import UI.MainUI;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class DBClass {
    Connection con = null; //previously static variable
    ResultSet rs = null;
    
    
  public  DBClass()
    {
        connect();
    }
    
    public void connect() //previously static method 
    {
        
          try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("driver true");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/leecomremote","root", ""
                    + "");
            if (!con.isClosed()) {
                System.out.println("Successfully connected to "
                        + "MySQL server using TCP/IP...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
        } 
        }
    
    
    
        public void disconnect() 
    {
        
          try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
        } 
        }
    
    
    
    
    
    
    
    
    
    
    
    public void deleteAllPacketData() //previously static method 
    {
             
        try {
          //  if(con!=null)
        //    {
                
            
            if(con!=null && !con.isClosed() )
            {
               try {
                    // con = DriverManager.getConnection(url, user, password);
                    Statement st = (Statement) con.createStatement(); 
                    st.executeUpdate("delete from tblpacket");
                   // con.close();
                 
                    }

               catch (SQLException ex) {
                    System.err.println("Exception: " + ex.getMessage());
                    } 
            }
            else
            {
              // connect(); 
            //   deleteAllPacketData();
             
            }
      
        } catch (SQLException ex) {
            Logger.getLogger(DBClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void insertPacketData(String signID ,String IP,String lastpacket,String conectivity,String connectedtime,String lastpackettime) ///previously static method 
    {
              // System.out.println(data.substring(6, 9));
              Date date = new Date();
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
              String datetime = sdf.format(date);

              String mSignID=signID;
            //  String mID=ID;
            //  String mReqID=Req;
              String mIP=IP;
              String mLastPacket=lastpacket;
              String mConectivity=conectivity;
              String mConnectedtime=connectedtime;
              String mLastPackettime=lastpackettime;
            //  String mLastConnectedTime=lastconnectedtime;
           //   String mSignID=lastpackettime;
       
          
       
          //  if(con!=null)
        //    {
          try{      
            if(con==null || con.isClosed())
            {  //   connect(); 
            }
            if(con!=null && !con.isClosed() )
            {
          
               try {
                 //     JOptionPane.showMessageDialog(null, "INSERT INTO tblpacket (DateTime,SignID,IP,LastPacket,Conectivity,ConectedTime,LastPacketTime)  VALUES ('"+datetime+"','"+mSignID+"','"+mIP+"','"+mLastPacket+"','"+mConectivity+"','"+mConnectedtime+"','"+mLastPackettime+"')", "Data" , JOptionPane.INFORMATION_MESSAGE); 
                    // con = DriverManager.getConnection(url, user, password);
                    Statement st = (Statement) con.createStatement(); 
                    st.executeUpdate("INSERT INTO tblpacket (DateTime,SignID,IP,LastPacket,Conectivity,ConectedTime,LastPacketTime)  VALUES ('"+datetime+"','"+mSignID+"','"+mIP+"','"+mLastPacket+"','"+mConectivity+"','"+mConnectedtime+"','"+mLastPackettime+"')   on duplicate key update "
                            + "DateTime='"+datetime+"',IP='"+mIP+"',LastPacket='"+mLastPacket+"',Conectivity='"+mConectivity+"',ConectedTime='"+mConnectedtime+"',LastPacketTime='"+mLastPackettime+"' ");
                   // con.close();
                    }

               catch (SQLException ex) {
                    System.err.println("Exception: " + ex.getMessage());
                    } 
            }
          }
          catch(SQLException ex)
          {
              System.err.println("Exception: " + ex.getMessage());
          }

    }
    
    public void insertRawData(String dt,String unit,String data,String statues)
    {
        String mData=data;
        String mDateTime=dt;
        String mUnit=unit;
        String mStatues=statues;

        try {
          //  if(con!=null)
        //    {
                
            
            if(con!=null && !con.isClosed() )
            {
               try {
                    // con = DriverManager.getConnection(url, user, password);
                    Statement st = (Statement) con.createStatement(); 
                    st.executeUpdate("INSERT INTO tblraw(timestamp,unit,data,status) VALUES ('"+mDateTime+"','"+mUnit+"','"+mData+"','"+mStatues+"')");
                   // con.close();
                    }

               catch (SQLException ex) {
                    System.err.println("Exception: " + ex.getMessage());
                    } 
            }
            else
            {
             //  connect(); 
            //   insertRawData(mDateTime,mUnit,mData,mStatues);
            }
          //  }
         //   else
           // {
           //    connect(); 
            //   insertRawData();
           // }
        } catch (SQLException ex) {
            Logger.getLogger(DBClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public String searchRequestedPacket(String request,String ReqOriginate)
    {
        String signid;
        if(ReqOriginate=="WaitingList")
        signid=request;
        else
        signid=request.substring(6, 9);   //changed from subsequance
       String request1=request;
        try {
          //  if(con!=null)
        //    {
              if(con==null || con.isClosed())
              {  //  connect(); 
              }
            if(con!=null && !con.isClosed() )
            {
               try {
                    // con = DriverManager.getConnection(url, user, password);
                    Statement st = (Statement) con.createStatement(); 
                    rs=st.executeQuery("select data from tblraw where unit='"+signid+"' ORDER BY ID DESC LIMIT 1");
                    //  st.executeUpdate("INSERT INTO tblraw(timestamp,unit,data,status) VALUES ('"+mDateTime+"','"+mUnit+"','"+mData+"','"+mStatues+"')");
                   // con.close();
                    if (!rs.next())
                    return request; 
                    else
                     return rs.getString(1); 
                    }

               catch (SQLException ex) {
                   
                    System.err.println("Exception: " + ex.getMessage());
                     return request; 
                    } 
            }
         //   else
           // {
               
            //   connect(); 
             //  searchRequestedPacket(request1);
               
          //  }
          //  }
         //   else
           // {
           //    connect(); 
            //   insertRawData();
           // }
        } catch (SQLException ex) {
            Logger.getLogger(DBClass.class.getName()).log(Level.SEVERE, null, ex);
            return request; 
        }
       return request; 
    }
    
    public void updateDisconnectedStatues(String SignID)
    {
//         try {
//     
//              if(con==null || con.isClosed())
//                  connect(); 
//            
//            if(con!=null && !con.isClosed() )
//            {
//               try {
//          
//                    Statement st = (Statement) con.createStatement(); 
//                     Statement st2 = (Statement) con.createStatement(); 
//                     rs=st.executeQuery("select * from tblraw where activestat='00' and unit='"+SignID+"' ");
//                     
//                while(rs.next())
//                  {
//                      String tmp[]=new String[2];
//                     tmp[0]= rs.getString(4).substring(0, 22).toString();
//                     tmp[1]= rs.getString(4).substring(24).toString();
//                     
//                     String fixstr=tmp[0]+"FF"+tmp[1];
// 
//                     st2.executeUpdate("update tblraw set data='"+fixstr+"',activestat='FF'  where ID="+rs.getInt(1)+" ");
//                   }
//           
//                  
//                    }
//
//               catch (SQLException ex) {
//                   
//                    System.err.println("Exception: " + ex.getMessage());
//                   
//                    } 
//            }
//      
//        } catch (SQLException ex) {
//            Logger.getLogger(DBClass.class.getName()).log(Level.SEVERE, null, ex);
//            
//        }
    }
    
    public void getConnections()
    {

        try {
       
            
                //if(con==null || con.isClosed())
                //  connect(); 
            
            if(con!=null && !con.isClosed() )
            {
              
               try {
                    // con = DriverManager.getConnection(url, user, password);
                //   Statement st = (Statement) con.createStatement(); 
                    
                    Statement st = (Statement) con.createStatement(); 
                    rs=st.executeQuery("SELECT * FROM tblpacket group by SignID");
                    
                           
                while(rs.next())
                  {
                      //rs.getString(1);
                   //   JOptionPane.showMessageDialog(null, "web interface", "title" , JOptionPane.INFORMATION_MESSAGE); 
                       MainUI.model.addRow(new Object[]{rs.getString(2),rs.getString(3),rs.getString(4),"Disconnected",rs.getString(7),rs.getString(6) }); 
                  }
                    
                   // con.close();
                    }

               catch (SQLException ex) {
                    System.err.println("Exception: " + ex.getMessage());
                    } 
            }
          
    
        } catch (SQLException ex) {
            Logger.getLogger(DBClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
