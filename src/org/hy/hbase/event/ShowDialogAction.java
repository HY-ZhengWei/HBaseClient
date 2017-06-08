package org.hy.hbase.event;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.xml.XJava;





/**
 * 显示对话窗口的通用事件
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-12
 */
public class ShowDialogAction extends BaseEvent implements JButtonOnClickTransaction
{
    
    private String dialogName;
    
    

    @Override
    public void onClick(ActionEvent arg0)
    {
        JDialog v_JDialog = this.getDialog();
        
        if ( v_JDialog == null )
        {
            return;
        }
        
        v_JDialog.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(this.getAppFrame().getClass().getResource("img/HBase.png")));
        
        this.showCreate(v_JDialog);
    }

    

    @Override
    public void transactionBefore(ActionEvent arg0)
    {
        this.getAppFrame().setEnabled(false);
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent arg0)
    {
        this.getAppFrame().setEnabled(true);
        this.getAppFrame().requestFocus();
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
