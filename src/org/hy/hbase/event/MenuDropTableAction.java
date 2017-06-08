package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;





/**
 * 菜单点击事件：删除表
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-11
 */
public class MenuDropTableAction extends BaseEvent implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent arg0)
    {
        if ( JavaHelp.isNull(this.getTableName()) )
        {
            this.getAppFrame().showHintInfo("请先选择要删除的表" ,Color.BLUE);
            return;
        }
        
        int v_Ret = JOptionPane.showConfirmDialog(this.getAppFrame() ,"确认要删除表 [" + this.getTableName() + "] ？删除后数据将无法恢复。" ,"确认对话框" , JOptionPane.YES_NO_OPTION);
        
        if ( v_Ret == JOptionPane.YES_OPTION )
        {
            // Nothing.
        }
        else
        {
            this.getAppFrame().showHintInfo("您取消了删除表操作" ,Color.BLUE);
            return;
        }
        
        try
        {
            String v_TableName = this.getTableName();
            this.getHBase().dropTable(this.getTableName());
            
            this.getAppFrame().setTableName(null);
            this.getAppFrame().initListModel();
            this.getAppFrame().initTablesInfo();
            
            this.getAppFrame().showHintInfo("删除表 [" + v_TableName + "] 成功" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("删除表异常：" + exce.getMessage() ,Color.RED);
        }
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
    
}
