package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.ui.JTextField;
import org.hy.common.xml.XJava;





/**
 * 菜单点击事件：编辑表的列族
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-12
 */
public class MenuEditFamilyNamesAction extends ShowDialogAction implements JButtonOnClickTransaction
{
    
    @Override
    public void onClick(ActionEvent i_Event)
    {
        if ( JavaHelp.isNull(this.getTableName()) )
        {
            this.getAppFrame().showHintInfo("请先选择要编辑的表" ,Color.BLUE);
            return;
        }
        
        
        List<String>  v_FamilyNames = this.getHBase().getTableFamilyNames(this.getTableName());
        StringBuilder v_Buffer      = new StringBuilder();
        
        for (String v_FamilyName : v_FamilyNames)
        {
            v_Buffer.append(",").append(v_FamilyName);
        }
        
        ((JTextField)XJava.getObject("xtEditFamilysTableName")).setText(this.getTableName());
        ((JTextField)XJava.getObject("xtEditFamilyNames"))     .setText(v_Buffer.toString().substring(1));
        
        super.onClick(i_Event);
    }
    
}
