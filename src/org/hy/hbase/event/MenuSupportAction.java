package org.hy.hbase.event;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.hbase.AppMain;





/**
 * 菜单点击事件：支持与源码
 * 
 * @author ZhengWei(HY)
 * @create 2017-06-15
 */
public class MenuSupportAction extends BaseEvent implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent arg0)
    {
        try
        {
            URI     v_URI     = URI.create(AppMain.$SourceCode);
            Desktop v_Desktop = Desktop.getDesktop();
            
            // 判断系统桌面是否支持要执行的功能
            if ( v_Desktop.isSupported(Desktop.Action.BROWSE) )
            {
                // 获取系统默认浏览器打开链接
                v_Desktop.browse(v_URI);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    

    @Override
    public void transactionBefore(ActionEvent arg0)
    {
        this.getAppFrame().setEnabled(false);
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent arg0)
    {
        this.getAppFrame().setEnabled(true);
        this.getAppFrame().requestFocus();
    }
    
}
