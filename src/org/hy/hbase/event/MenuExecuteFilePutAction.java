package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.hbase.AppMain;





/**
 * 菜单点击事件：文件批量put命令导入
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-11
 */
public class MenuExecuteFilePutAction extends BaseEvent implements JButtonOnClickTransaction
{
    
    private JFileChooser fileChooser;
    
    

    @Override
    public void onClick(ActionEvent arg0)
    {
        if ( this.fileChooser == null )
        {
            this.fileChooser = new JFileChooser();
        }
        
        this.fileChooser.setDialogTitle("请选择文件或文件夹");
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        try
        {
            // 不知道为什么非要sleep一下才再在多次反复打开对话窗口时不出异常，保证每次都能打开对话窗口。
            // 测试环境：Mac 10.12.5、Java 1.6、Eclipse 4.3.2
            Thread.sleep(10);
        }
        catch (Exception exce)
        {
            // Nothing.
        }
        
        int v_Result = this.fileChooser.showOpenDialog(this.getAppFrame());
        if ( v_Result == JFileChooser.APPROVE_OPTION )
        {
            File v_File = this.fileChooser.getSelectedFile();
            
            if (  !v_File.exists() 
              || (!v_File.isDirectory() && !v_File.isFile()) )
            {
                this.getAppFrame().showHintInfo("请选择有效的文件或文件夹" ,Color.BLUE);
                return;
            }
            
            AppMain.exeutesFile(v_File ,"UTF-8");
            
            this.getAppFrame().showHintInfo("执行完成，请查看控制台日志" ,Color.BLUE);
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
