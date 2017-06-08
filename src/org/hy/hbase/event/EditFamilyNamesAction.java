package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.ui.JTextField;
import org.hy.common.xml.XJava;





/**
 * 编辑表的列族窗口中的修改按钮的点击事件
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-12
 */
public class EditFamilyNamesAction extends CloseDialogAction implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent i_Event)
    {
        JTextField v_TableNameObj   = (JTextField)XJava.getObject("xtEditFamilysTableName");
        JTextField v_FamilyNamesObj = (JTextField)XJava.getObject("xtEditFamilyNames");
        
        if ( JavaHelp.isNull(v_TableNameObj.getText()) )
        {
            this.getAppFrame().showHintInfo("表名称为空异常" ,Color.RED);
            return;
        }
        
        if ( JavaHelp.isNull(v_FamilyNamesObj.getText()) )
        {
            this.getAppFrame().showHintInfo("请输入只少一个列族名" ,Color.BLUE);
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
        
        if ( !this.getHBase().isExistsTable(v_TableNameObj.getText().trim()) )
        {
            this.getAppFrame().showHintInfo("表 [" + v_TableNameObj.getText().trim() + "] 已不存在，无法修改列族名" ,Color.BLUE);
            return;
        }
        
        
        try
        {
            List<String> v_OldFamilys = this.getHBase().getTableFamilyNames(v_TableNameObj.getText().trim());
            List<String> v_DelFamilys = this.findNotExists(v_OldFamilys  ,v_FamilyNames);
            List<String> v_NewFamilys = this.findNotExists(v_FamilyNames ,v_OldFamilys);
            
            if ( JavaHelp.isNull(v_DelFamilys) && JavaHelp.isNull(v_NewFamilys) )
            {
                this.getAppFrame().showHintInfo("您未对表 [" + v_TableNameObj.getText().trim() + "] 做任何改动" ,Color.BLUE);
                return;
            }
            
            for (int i=0; i<v_DelFamilys.size(); i++)
            {
                this.getHBase().dropFamily(v_TableNameObj.getText().trim() ,v_DelFamilys.get(i));
            }
            
            for (int i=0; i<v_NewFamilys.size(); i++)
            {
                this.getHBase().addFamily(v_TableNameObj.getText().trim() ,v_NewFamilys.get(i));
            }
            
            super.onClick(i_Event);
            
            this.getAppFrame().showHintInfo("编辑表 [" + v_TableNameObj.getText().trim() + "] 的列族名成功。请点击表名刷新" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("编辑表的列族名异常：" + exce.getMessage() ,Color.RED);
        }
    }
    
    
    
    /**
     * 找不存在 Olds 集合中的元素
     * 
     * @param i_Olds
     * @param i_News
     * @return
     */
    private List<String> findNotExists(List<String> i_Olds ,String [] i_News)
    {
        List<String> v_NotExists = new ArrayList<String>();
        
        for (int i=0; i<i_Olds.size(); i++)
        {
            int x=0;
                    
            for (; x<i_News.length; x++)
            {
                if ( i_Olds.get(i).equals(i_News[x].trim()) )
                {
                    x = i_News.length + 1;
                }
            }
            
            if ( x ==  i_News.length )
            {
                v_NotExists.add(i_Olds.get(i));
            }
        }
        
        return v_NotExists;
    }
    
    
    
    /**
     * 找不存在 Olds 集合中的元素
     * 
     * @param i_Olds
     * @param i_News
     * @return
     */
    private List<String> findNotExists(String [] i_Olds ,List<String> i_News)
    {
        List<String> v_NotExists = new ArrayList<String>();
        
        for (int i=0; i<i_Olds.length; i++)
        {
            int x=0;
                    
            for (; x<i_News.size(); x++)
            {
                if ( i_Olds[i].equals(i_News.get(x).trim()) )
                {
                    x = i_News.size() + 1;
                }
            }
            
            if ( x ==  i_News.size() )
            {
                v_NotExists.add(i_Olds[i]);
            }
        }
        
        return v_NotExists;
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
