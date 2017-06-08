package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.ui.JTextField;
import org.hy.common.xml.XJava;





/**
 * 创建表窗口中的创建按钮的点击事件
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-11
 */
public class CreateTableAction extends CloseDialogAction implements JButtonOnClickTransaction
{
    
    @Override
    public void onClick(ActionEvent i_Event)
    {
        JTextField v_TableNameObj   = (JTextField)XJava.getObject("xtCreateTableName");
        JTextField v_FamilyNamesObj = (JTextField)XJava.getObject("xtCreateFamilyNames");
        
        if ( JavaHelp.isNull(v_TableNameObj.getText()) )
        {
            this.getAppFrame().showHintInfo("请输入新创建的表名称" ,Color.BLUE);
            return;
        }
        
        if ( JavaHelp.isNull(v_FamilyNamesObj.getText()) )
        {
            this.getAppFrame().showHintInfo("请输入新创建表的列族名" ,Color.BLUE);
            return;
        }
        
        String [] v_FamilyNames = v_FamilyNamesObj.getText().trim().split(",");
        if ( v_FamilyNames.length <= 0 )
        {
            this.getAppFrame().showHintInfo("请输入只少一个列族名" ,Color.BLUE);
            return;
        }
        
        for (int i=0; i<v_FamilyNames.length; i++)
        {
            if ( JavaHelp.isNull(v_FamilyNames[i]) )
            {
                this.getAppFrame().showHintInfo("第 " + (i+1) + " 个列族名为空" ,Color.BLUE);
                return;
            }
        }
        
        if ( this.getHBase().isExistsTable(v_TableNameObj.getText().trim()) )
        {
            this.getAppFrame().showHintInfo("表 [" + v_TableNameObj.getText().trim() + "] 已存在，无法重复创建" ,Color.BLUE);
            return;
        }
        
        
        try
        {
            this.getHBase().createTable(v_TableNameObj.getText().trim() ,v_FamilyNames);
            super.onClick(i_Event);
            
            this.getAppFrame().initListModel();
            this.getAppFrame().initTablesInfo();
            
            this.getAppFrame().showHintInfo("创建表 [" + v_TableNameObj.getText().trim() + "] 成功" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("创建表异常：" + exce.getMessage() ,Color.RED);
        }
    }

    

    @Override
    public void transactionBefore(ActionEvent i_Event)
    {
        
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent i_Event)
    {
        
    }
    
}
