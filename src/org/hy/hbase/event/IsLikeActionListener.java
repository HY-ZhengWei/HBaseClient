package org.hy.hbase.event;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;





public class IsLikeActionListener implements ChangeListener
{

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JCheckBox v_IsLike = (JCheckBox)e.getSource();
        
        if ( v_IsLike.isSelected() )
        {
            v_IsLike.setText("精确");
        }
        else
        {
            v_IsLike.setText("模糊");
        }
    }
    
}
