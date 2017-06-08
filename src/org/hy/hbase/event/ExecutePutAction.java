package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.xml.XJava;
import org.hy.hbase.AppMain;





/**
 * 执行批量put命令窗口中的执行按钮的点击事件
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-12
 */
public class ExecutePutAction extends CloseDialogAction implements JButtonOnClickTransaction
{
    
    @Override
    public void onClick(ActionEvent i_Event)
    {
        String v_CMD = ((JTextArea)XJava.getObject("xtPutsInfo")).getText();
        
        if ( JavaHelp.isNull(v_CMD) )
        {
            this.getAppFrame().showHintInfo("请输入要执行的命令" ,Color.BLUE);
            return;
        }
        
        
        try
        {
            AppMain.executes(v_CMD);
            super.onClick(i_Event);
            
            this.getAppFrame().showHintInfo("执行完成，请查看控制台日志" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("执行命令异常：" + exce.getMessage() ,Color.RED);
        }
    }
    
    
    
    @Override
    public void transactionBefore(ActionEvent i_Event)
    {
        
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent i_Event)
    {
        
    }
    
}
