package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButton;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.ui.JComboBox;
import org.hy.common.xml.XJava;





/**
 * 工具面板上删除按钮的点击事件
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-10
 */
public class TruncateActionListener extends BaseEvent implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent arg0)
    {
        String v_TableName = this.getAppFrame().getTableName();
        
        if ( JavaHelp.isNull(v_TableName) )
        {
            this.getAppFrame().showHintInfo("请先选择要清空的表" ,Color.BLUE);
            return;
        }
        
        int v_Ret = JOptionPane.showConfirmDialog(this.getAppFrame() ,"确认要清空表的所有记录？清空后数据将无法恢复。" ,"确认对话框" , JOptionPane.YES_NO_OPTION);
        
        if ( v_Ret == JOptionPane.YES_OPTION )
        {
            // Nothing.
        }
        else
        {
            this.getAppFrame().showHintInfo("您取消了清空表操作" ,Color.BLUE);
            return;
        }
        
        
        try
        {
            this.getHBase().truncate(v_TableName);
            
            ((JTextComponent)XJava.getObject("RowKey"))     .setText("");
            ((JTextComponent)XJava.getObject("ColumnValue")).setText("");
            ((JCheckBox)     XJava.getObject("IsLike"))     .setSelected(true);
            ((JComboBox)     XJava.getObject("FamilyName")) .setSelectedIndex(0);
            ((JComboBox)     XJava.getObject("ColumnName")) .setSelectedIndex(0);
            
            ((JButton)XJava.getObject("xbSubmit")).doClick();
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("清空表异常：" + exce.getMessage() ,Color.RED);
        }
    }


    
    @Override
    public void transactionBefore(ActionEvent arg0)
    {
        
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent arg0)
    {
        
    }
    
}
