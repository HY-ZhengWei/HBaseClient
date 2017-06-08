package org.hy.hbase.event;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.hy.common.ui.JComboBox;
import org.hy.common.xml.XJava;





/**
 * 1. 查询条件面板中，列族名的点击事件。
 * 2. 结果集编辑面板，列族名的点击事件。
 * 
 * 将级联出所属的字段信息
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-01
 */
public class SelectFamilyNameListener extends BaseEvent implements ItemListener
{

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        JComboBox v_FamilyNameObj = (JComboBox)e.getSource();
        JComboBox v_ColumnNameObj = null;
        
        if ( v_FamilyNameObj.getName().equals("Edit_FamilyName") )
        {
            v_ColumnNameObj = (JComboBox)XJava.getObject("Edit_ColumnName");
        }
        else
        {
            v_ColumnNameObj = (JComboBox)XJava.getObject("ColumnName");
            ((JComponent)XJava.getObject("xbSubmitFilter")).setEnabled(false);
        }
        
        this.getAppFrame().initColumnNames(e.getItem().toString() ,v_ColumnNameObj);
    }
    
}
