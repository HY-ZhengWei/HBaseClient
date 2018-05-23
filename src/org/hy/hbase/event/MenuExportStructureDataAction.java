package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import org.hy.common.JavaHelp;
import org.hy.common.hbase.HData;
import org.hy.common.ui.JButtonOnClickTransaction;





/**
 * 菜单点击事件：导出表结构和所有数据 
 *
 * @author      ZhengWei(HY)
 * @createDate  2015-02-11
 * @version     v1.0
 */
public class MenuExportStructureDataAction extends BaseEvent implements JButtonOnClickTransaction
{
    
    private JFileChooser fileChooser;
    
    

    @Override
    public void onClick(ActionEvent arg0)
    {
        List<String> v_TableNames = this.getHBase().getTableNames();
        
        if ( JavaHelp.isNull(v_TableNames) )
        {
            this.getAppFrame().showHintInfo("数据库中不存在表" ,Color.BLUE);
            return;
        }
        
        if ( this.fileChooser == null )
        {
            this.fileChooser = new JFileChooser();
        }
        
        File v_SaveFile = new File(this.getHBase().getHBaseIP() + ".txt");
        this.fileChooser.setSelectedFile(v_SaveFile);
        
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
        
        int v_Result = this.fileChooser.showSaveDialog(this.getAppFrame());
        if ( v_Result == JFileChooser.APPROVE_OPTION )
        {
            v_SaveFile = this.fileChooser.getSelectedFile();
            
            this.writeContents(v_TableNames ,v_SaveFile);
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
    
    
    
    private void writeContents(List<String> i_TableNames ,File i_SaveFile)
    {
        FileOutputStream   v_SaveOutput = null;
        OutputStreamWriter v_SaveWriter = null;
        
        try
        {
            v_SaveOutput = new FileOutputStream(i_SaveFile);
            v_SaveWriter = new OutputStreamWriter(v_SaveOutput ,"UTF-8"); 
            int v_Count  = i_TableNames.size();
            
            for (int i=0; i<v_Count; i++)
            {
                this.getAppFrame().showHintInfo("导出中 ... ... " + (i + 1) + " / " + v_Count ,Color.BLUE);
                
                v_SaveWriter.write("disable '" + i_TableNames.get(i) + "';" + JavaHelp.getSysLineSeparator());
            }
            
            v_SaveWriter.write(JavaHelp.getSysLineSeparator());
            
            for (int i=0; i<v_Count; i++)
            {
                this.getAppFrame().showHintInfo("导出中 ... ... " + (i + 1) + " / " + v_Count ,Color.BLUE);
                
                v_SaveWriter.write("drop '" + i_TableNames.get(i) + "';" + JavaHelp.getSysLineSeparator());
            }
            
            v_SaveWriter.write(JavaHelp.getSysLineSeparator());
            
            for (int i=0; i<v_Count; i++)
            {
                this.getAppFrame().showHintInfo("导出中 ... ... " + (i + 1) + " / " + v_Count ,Color.BLUE);
                
                List<String>  v_FamilyNames = this.getHBase().getTableFamilyNames(i_TableNames.get(i));
                StringBuilder v_Buffer      = new StringBuilder();
                
                v_Buffer.append("create '").append(i_TableNames.get(i)).append("'");
                
                for (int x=0; x<v_FamilyNames.size(); x++)
                {
                    v_Buffer.append(",'").append(v_FamilyNames.get(x)).append("'");
                }
                
                v_Buffer.append(";").append(JavaHelp.getSysLineSeparator());
                
                v_SaveWriter.write(v_Buffer.toString());
            }
            
            v_SaveWriter.write(JavaHelp.getSysLineSeparator());
            
            for (int i=0; i<v_Count; i++)
            {
                this.getAppFrame().showHintInfo("导出数据中 ... ... " + (i + 1) + " / " + v_Count ,Color.BLUE);
                
                Map<String ,Map<String ,Object>> v_Datas  = this.getHBase().getRows(i_TableNames.get(i));
                
                
                for (Entry<String ,Map<String ,Object>> v_RowData : v_Datas.entrySet())
                {
                    StringBuilder v_Buffer = new StringBuilder();
                    
                    for (Object v_ColData : v_RowData.getValue().values())
                    {
                        HData v_HData = (HData)v_ColData;
                        
                        v_Buffer.append(v_HData.toPut(i_TableNames.get(i))).append(JavaHelp.getSysLineSeparator());
                    }
                    
                    v_Buffer.append(JavaHelp.getSysLineSeparator());
                    v_SaveWriter.write(v_Buffer.toString());
                }
            }
            
            
            v_SaveWriter.flush();
            
            this.getAppFrame().showHintInfo("导出中完成。共生成 " + v_Count + " 张表的结构" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo(exce.getMessage() ,Color.RED);
        }
        finally
        {
            if ( v_SaveWriter != null )
            {
                try
                {
                    v_SaveWriter.close();
                }
                catch (Exception exce)
                {
                    // Nothing.
                }
            }
            
            if ( v_SaveOutput != null )
            {
                try
                {
                    v_SaveOutput.close();
                }
                catch (Exception exce)
                {
                    // Nothing.
                }
            }
        }
    }
    
}
