package org.hy.hbase.event;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import org.hy.common.ui.JButton;
import org.hy.common.ui.JComboBox;
import org.hy.common.xml.XJava;





/**
 * 结果集数据行的选择改变事件
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-02
 */
public class ResultsSelectionListener extends BaseEvent implements ListSelectionListener
{
    
	public ResultsSelectionListener()
	{
	    
	}
	
	
	
	public void valueChanged(ListSelectionEvent e) 
	{
	    JTable v_JTable = (JTable)XJava.getObject("xtDataList");
	    
		// 如果事件非快速连续事件之一
		if ( !e.getValueIsAdjusting() )
		{
			int [] v_RowIndexArr = v_JTable.getSelectedRows();
			
			if ( v_RowIndexArr.length == 1 )
			{
			    ((JTextComponent)XJava.getObject("Edit_RowKey"))     .setText(        v_JTable.getValueAt(v_RowIndexArr[0] ,1).toString());
			    ((JComboBox)     XJava.getObject("Edit_ColumnName")) .setSelectedItem(v_JTable.getValueAt(v_RowIndexArr[0] ,3).toString());
			    ((JTextComponent)XJava.getObject("Edit_ColumnValue")).setText(        v_JTable.getValueAt(v_RowIndexArr[0] ,4).toString());
			    ((JButton)       XJava.getObject("xbCopy"))          .setEnabled(false);
			    
		        // 设置列族名下拉列表框
		        String    v_FamilyName    = v_JTable.getValueAt(v_RowIndexArr[0] ,2).toString();
		        JComboBox v_FamilyNameObj = (JComboBox)XJava.getObject("Edit_FamilyName");
		        for (int i=0; i<v_FamilyNameObj.getItemCount(); i++)
		        {
		            if ( v_FamilyNameObj.getItemAt(i).toString().equals(v_FamilyName) )
		            {
		                v_FamilyNameObj.setSelectedIndex(i);
		                return;
		            }
		        }
			}
			else
			{
			    // 多行选择的情况
			    ((JTextComponent)XJava.getObject("Edit_RowKey"))     .setText("");
			    ((JComboBox)     XJava.getObject("Edit_FamilyName")) .setSelectedIndex(0);
                ((JComboBox)     XJava.getObject("Edit_ColumnName")) .setSelectedIndex(0);
                ((JTextComponent)XJava.getObject("Edit_ColumnValue")).setText("");
                ((JButton)       XJava.getObject("xbCopy"))          .setEnabled(this.getAppFrame().getSelectColCount() >= 2);
			}
		}
	}

}
