package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;

import org.hy.common.ui.JButtonOnClickTransaction;





/**
 * 菜单点击事件：刷新
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-11
 */
public class MenuRefurbishAction extends BaseEvent implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent arg0)
    {
        try
        {
            this.getAppFrame().initListModel();
            this.getAppFrame().initTablesInfo();
            
            this.getAppFrame().showHintInfo("刷新数据库表的列表完成" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo("刷新异常：" + exce.getMessage() ,Color.RED);
        }
    }

    

    @Override
    public void transactionBefore(ActionEvent arg0)
    {
        this.getAppFrame().setEnabled(false);
        
        this.getAppFrame().showHintInfo("请稍后，正在刷新数据库表的列表..." ,Color.BLUE);
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent arg0)
    {
        this.getAppFrame().setEnabled(true);
        this.getAppFrame().requestFocus();
    }
    
}
