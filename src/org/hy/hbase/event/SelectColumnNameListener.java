package org.hy.hbase.event;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JComboBox;
import org.hy.common.xml.XJava;





/**
 * 1. 查询条件面板中，字段名的点击事件。
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-05
 */
public class SelectColumnNameListener extends BaseEvent implements ItemListener
{

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        boolean v_Enable = false;
        
        try
        {
            String v_FileName   = ((JComboBox)XJava.getObject("FamilyName")).getSelectedItem().toString();
            String v_ColumnName = ((JComboBox)XJava.getObject("ColumnName")).getSelectedItem().toString();
            
            
            if ( !JavaHelp.isNull(v_FileName) && !JavaHelp.isNull(v_ColumnName) )
            {
                v_Enable = true;
            }
        }
        catch (Exception exce)
        {
            // Nothing.
        }
        
        ((JComponent)XJava.getObject("xbSubmitFilter")).setEnabled(v_Enable);
    }
    
}
