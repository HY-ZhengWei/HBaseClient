package org.hy.hbase.event;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hy.common.JavaHelp;





/**
 * 数据库表列表的点击选择事件
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-01
 */
public class SelectTableInfoListener extends BaseEvent implements ListSelectionListener
{

    @SuppressWarnings("unchecked")
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        JList<String> v_Tables    = (JList<String>)e.getSource();
        String        v_TableName = v_Tables.getSelectedValue();
        
        if ( !JavaHelp.isNull(v_TableName) )
        {
            if ( !v_TableName.equals(this.getTableName()) )
            {
                this.getAppFrame().setTableName(v_TableName);
            }
        }
    }
    
}
