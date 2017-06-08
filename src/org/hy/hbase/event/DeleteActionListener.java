package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.hy.common.hbase.HData;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.xml.XJava;





/**
 * 工具面板上删除按钮的点击事件
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-03
 */
public class DeleteActionListener extends BaseEvent implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent i_Event)
    {
        JTable      v_JTable      = (JTable)XJava.getObject("xtDataList");
        int []      v_RowIndexArr = v_JTable.getSelectedRows();
        List<HData> v_HDatas      = new ArrayList<HData>();
        
        if ( v_RowIndexArr.length <= 0 )
        {
            this.getAppFrame().showHintInfo("请先选择要删除行" ,Color.BLUE);
            return;
        }
        
        
        int v_Ret = JOptionPane.showConfirmDialog(this.getAppFrame() ,"确认要删除选中的记录？删除后将无法恢复。" ,"确认对话框" , JOptionPane.YES_NO_OPTION);
        
        if ( v_Ret == JOptionPane.YES_OPTION )
        {
            // Nothing.
        }
        else
        {
            this.getAppFrame().showHintInfo("您取消了删除操作" ,Color.BLUE);
            return;
        }
        
        
        for (int i=0; i<v_RowIndexArr.length; i++)
        {
            HData v_HData = new HData();
            
            v_HData.setRowKey(    v_JTable.getValueAt(v_RowIndexArr[i] ,1).toString());
            v_HData.setFamilyName(v_JTable.getValueAt(v_RowIndexArr[i] ,2).toString());
            v_HData.setColumnName(v_JTable.getValueAt(v_RowIndexArr[i] ,3).toString());
            
            v_HDatas.add(v_HData);
        }
        
        try
        {
            this.getHBase().delete(this.getTableName() ,v_HDatas);
            
            for (int i=v_RowIndexArr.length-1; i>=0; i--)
            {
                this.getAppFrame().getTableModel().removeRow(v_RowIndexArr[i]);
            }
            
            this.getAppFrame().showHintInfo("成功删除 " + v_HDatas.size() + " 条信息" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("删除异常：" + exce.getMessage() ,Color.RED);
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
