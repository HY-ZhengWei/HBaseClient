package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import org.hy.common.JavaHelp;
import org.hy.common.hbase.HData;
import org.hy.common.ui.JButton;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.ui.JComboBox;
import org.hy.common.xml.XJava;





/**
 * 结果集编辑面板中的提交按钮事件
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-02
 */
public class EditSubmitActionListener extends BaseEvent implements JButtonOnClickTransaction
{
    private Map<String ,Object>        editDatas;
    
    private JComponent                 myself;
    
    
    
    public EditSubmitActionListener()
    {
        this.editDatas = new Hashtable<String ,Object>();
    }
    
    

    @Override
    public void onClick(ActionEvent arg0)
    {
        HData v_HData = new HData();
        
        v_HData.setRowKey(    ((JTextComponent)XJava.getObject("Edit_RowKey"))     .getText().trim());
        v_HData.setFamilyName(((JComboBox)     XJava.getObject("Edit_FamilyName")) .getSelectedItem().toString().trim());
        v_HData.setColumnName(((JComboBox)     XJava.getObject("Edit_ColumnName")) .getSelectedItem().toString().trim());
        v_HData.setValue(     ((JTextComponent)XJava.getObject("Edit_ColumnValue")).getText().trim());
        
        if ( JavaHelp.isNull(v_HData.getRowKey()) )
        {
            this.getAppFrame().showHintInfo("提交时，行主键不能为空！" ,Color.RED);
            ((JComponent)XJava.getObject("Edit_RowKey")).requestFocus();
            return;
        }
        
        if ( JavaHelp.isNull(v_HData.getFamilyName()) )
        {
            this.getAppFrame().showHintInfo("提交时，列族名不能为空！" ,Color.RED);
            ((JComponent)XJava.getObject("Edit_FamilyName")).requestFocus();
            return;
        }
        
        if ( JavaHelp.isNull(v_HData.getColumnName()) )
        {
            this.getAppFrame().showHintInfo("提交时，字段名不能为空！" ,Color.RED);
            ((JComponent)XJava.getObject("Edit_ColumnName")).requestFocus();
            return;
        }
        
        try
        {
            HData v_OldHData = (HData)this.getHBase().getValue(this.getTableName() ,v_HData);
            
            this.getHBase().update(this.getTableName() ,v_HData);
            
            // 重新从数据库是查询一次，主要想获取时间戳
            HData v_NewHData = (HData)this.getHBase().getValue(this.getTableName() ,v_HData);
            
            JTable  v_JTable  = (JTable)XJava.getObject("xtDataList");
            int     v_RowNo   = v_JTable.getSelectedRow();
            int     v_OptType = 1; // 操作类型(0:修改   1:添加)
            
            if ( v_JTable.getSelectedRowCount() == 1 )
            {
                if ( v_NewHData.getRowKey().equals(v_JTable.getValueAt(v_RowNo ,1)) )
                {
                    if ( v_NewHData.getFamilyName().equals(v_JTable.getValueAt(v_RowNo ,2)) )
                    {
                        if ( v_NewHData.getColumnName().equals(v_JTable.getValueAt(v_RowNo ,3)) )
                        {
                            v_OptType = 0;
                        }
                    }
                }
            }
            
            if ( v_OptType == 0 )
            {
                v_JTable.setValueAt(v_NewHData.getValue().toString()    ,v_RowNo ,4);
                v_JTable.setValueAt(v_NewHData.getTimestamp()           ,v_RowNo ,5);
                v_JTable.setValueAt(v_NewHData.getTime().getFullMilli() ,v_RowNo ,6);
                this.getAppFrame().showHintInfo("修改完成！" ,Color.BLUE);
            }
            else
            {
                this.getAppFrame().setRowCount(this.getAppFrame().getRowCount() + 1);
                this.getAppFrame().getTableModel().addRow(SubmitActionListener.$MY.toObjects(this.getAppFrame().getRowCount() ,v_NewHData));
                
                if ( v_OldHData != null )
                {
                    this.getAppFrame().showHintInfo("修改完成，请刷新查询结果。" ,Color.BLUE);
                }
                else
                {
                    this.getAppFrame().showHintInfo("添加完成！" ,Color.BLUE);
                }
            }
            
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("修改异常：" + exce.getMessage() ,Color.RED);
        }
    }

    
    
    @Override
    public void transactionBefore(ActionEvent i_ActionEvent)
    {
        this.myself = ((JComponent)(i_ActionEvent.getSource()));
        
        // 所有编码控件及按钮本身都失效
        this.setEditDatasEnabled(false);
        
        this.getAppFrame().setTables_Tools_Edit_Enabled(false);
        
        ((JComponent)XJava.getObject("xpResultInfo")).setEnabled(false);
    }

    
    
    @Override
    public void transactionAfter(ActionEvent i_ActionEvent)
    {
        // 所有编码控件及按钮本身都有效
        this.setEditDatasEnabled(true);
        
        this.getAppFrame().setTables_Tools_Edit_Enabled(true);
        
        if ( this.getHPage().getRowKey() == null )
        {
            ((JButton) XJava.getObject("xbNextPage")).setEnabled(false);
            ((JButton) XJava.getObject("xbLastPage")).setEnabled(false);
        }
        
        ((JComponent)XJava.getObject("xpResultInfo")).setEnabled(true);
    }
    
    
    
    /**
     * 设置所有查询条件控件是否有效
     * 
     * @param i_Visible
     */
    public void setEditDatasEnabled(boolean i_Enabled)
    {
        // 先设置事情源本身
        this.myself.setEnabled(i_Enabled);
        
        Iterator<?> v_Iterator = this.editDatas.values().iterator();
        
        while ( v_Iterator.hasNext() )
        {
            Object v_QueryCondition = v_Iterator.next();
            
            if ( v_QueryCondition instanceof JComponent )
            {
                JComponent v_QC = (JComponent)v_QueryCondition;
                
                v_QC.setEnabled(i_Enabled);
            }
        }
    }



    public Map<String ,Object> getEditDatas()
    {
        return editDatas;
    }



    public void setEditDatas(Map<String ,Object> editDatas)
    {
        this.editDatas = editDatas;
    }
    
}
