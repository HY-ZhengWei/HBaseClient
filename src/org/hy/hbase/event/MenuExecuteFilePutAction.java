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

    @Override
    public void onClick(ActionEvent arg0)
    {
        JFileChooser v_FileChooser = new JFileChooser();
        
        v_FileChooser.setDialogTitle("请选择文件或文件夹");
        v_FileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int v_Result = v_FileChooser.showOpenDialog(this.getAppFrame());
        
        if ( v_Result == JFileChooser.APPROVE_OPTION )
        {
            File v_File = v_FileChooser.getSelectedFile();
            
            if (  !v_File.exists() 
              || (!v_File.isDirectory() && !v_File.isFile()) )
            {
                this.getAppFrame().showHintInfo("请选择有效的文件或文件夹" ,Color.BLUE);
                return;
            }
            
            AppMain.exeutesFile(v_File ,null);
            
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
