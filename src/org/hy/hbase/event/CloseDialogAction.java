package org.hy.hbase.event;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.xml.XJava;





/**
 * 关闭对话窗口的通用事件
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-11
 */
public class CloseDialogAction extends BaseEvent implements JButtonOnClickTransaction
{
    
    private String dialogName;
    
    

    @Override
    public void onClick(ActionEvent arg0)
    {
        JDialog v_JDialog = this.getDialog();
        
        v_JDialog.setVisible(false);
        v_JDialog.dispose();
    }

    

    @Override
    public void transactionBefore(ActionEvent arg0)
    {
        
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent arg0)
    {
        
    }

    
    
    public void setDialogName(String i_DialogName)
    {
        this.dialogName = i_DialogName;
    }
    
    
    
    public JDialog getDialog()
    {
        if ( !JavaHelp.isNull(this.dialogName) )
        {
            return (JDialog)XJava.getObject(this.dialogName);
        }
        else
        {
            throw new NullPointerException("Dialog name is null.");
        }
    }
    
}
