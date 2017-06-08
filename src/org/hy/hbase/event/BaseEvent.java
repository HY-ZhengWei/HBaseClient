package org.hy.hbase.event;

import java.awt.Toolkit;

import javax.swing.JDialog;

import org.hy.common.hbase.HBase;
import org.hy.common.hbase.HPage;
import org.hy.common.xml.XJava;
import org.hy.hbase.AppFrame;





public class BaseEvent
{
    
    public AppFrame getAppFrame()
    {
        return (AppFrame)XJava.getObject("AppFrame");
    }
    
    
    
    public HBase getHBase()
    {
        return this.getAppFrame().getHBase();
    }
    
    
    
    public String getTableName()
    {
        return this.getAppFrame().getTableName();
    }
    
    
    
    public HPage getHPage()
    {
        return this.getAppFrame().getHPage();
    }
    
    
    
    /**
     * 屏幕中间显示对话窗口
     * 
     * @param i_JDialog
     */
    public void showCreate(JDialog i_JDialog)
    {
        Toolkit v_Toolkit = Toolkit.getDefaultToolkit(); 
        int     v_X       = (v_Toolkit.getScreenSize().height - i_JDialog.getHeight()) / 2;
        int     v_Y       = (v_Toolkit.getScreenSize().width  - i_JDialog.getWidth() ) / 2;
        
        i_JDialog.setLocation(v_Y ,v_X);
        i_JDialog.setVisible(true);
    }
    
}
