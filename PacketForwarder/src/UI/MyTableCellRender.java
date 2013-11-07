/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author User
 */
public class MyTableCellRender extends DefaultTableCellRenderer   
{  
    public MyTableCellRender()   
    {  
        super();  
        setOpaque(true);  
    }   
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,   
            boolean isSelected, boolean hasFocus, int row, int column)   
    {
        
        
         Component comp = super.getTableCellRendererComponent(
                      table,  value, isSelected, hasFocus, row, column);
        
        
        if(table.getValueAt(row, 3) =="Connected")  
       {  
            comp.setForeground(Color.black);          
            comp.setBackground(Color.white);              
       }      
        else  
        {      
            comp.setBackground(Color.yellow);      
            comp.setForeground(Color.black);      
        } 
    //    MainUI.update(); 
     //   repaint();
        setText(value !=null ? value.toString() : "");  
        return this;  
    }  
}  