package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import org.hy.common.Date;
import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.ui.JComboBox;
import org.hy.common.xml.XJava;





/**
 * 工具面板上导出按钮的点击事件
 * 
 * 将查询结果导出为put命令，并保存在文件中
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-06-10
 */
public class ExportActionListener extends BaseEvent implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent arg0)
    {
        if ( JavaHelp.isNull(this.getAppFrame().getTableName()) )
        {
            this.getAppFrame().showHintInfo("请先选择要导出的表" ,Color.BLUE);
            return;
        }
        
        if ( this.getAppFrame().getRowCount() == 0 )
        {
            this.getAppFrame().showHintInfo("查询结果无数据，无法导出" ,Color.BLUE);
            return;
        }
        
        
        JTable        v_JTable      = (JTable)XJava.getObject("xtDataList");
        int []        v_RowIndexArr = v_JTable.getSelectedRows();
        StringBuilder v_FileName    = new StringBuilder();
        
        // 生成文件名称
        v_FileName.append(this.getTableName());
        if ( v_RowIndexArr.length <= 0 )
        {
            String v_Text = "";
            
            v_Text = ((JTextComponent)XJava.getObject("RowKey"))     .getText();
            if ( !JavaHelp.isNull(v_Text) )
            {
                v_FileName.append("_R.").append(v_Text.trim());
            }
            
            v_Text = ((JComboBox)     XJava.getObject("FamilyName")) .getSelectedItem().toString();
            if ( !JavaHelp.isNull(v_Text) ) 
            {
                v_FileName.append("_F.").append(v_Text.trim());
            }
            
            v_Text = ((JComboBox)     XJava.getObject("ColumnName")) .getSelectedItem().toString();
            if ( !JavaHelp.isNull(v_Text) ) 
            {
                v_FileName.append("_C.").append(v_Text.trim());
            }
            
            v_Text = ((JTextComponent)XJava.getObject("ColumnValue")).getText();
            if ( !JavaHelp.isNull(v_Text) ) 
            {
                v_FileName.append("_V.").append(v_Text.trim());
            }
        }
        else
        {
            v_FileName.append("_CCount.").append(v_RowIndexArr.length);
            v_FileName.append("_").append(Date.getNowTime().getFull_ID());
        }
        v_FileName.append(".txt");
        
        
        
        File         v_SaveFile    = new File(v_FileName.toString());
        JFileChooser v_FileChooser = new JFileChooser();
        v_FileChooser.setSelectedFile(v_SaveFile);
        
        int v_Result = v_FileChooser.showSaveDialog(this.getAppFrame());
        if ( v_Result == JFileChooser.APPROVE_OPTION )
        {
            v_SaveFile = v_FileChooser.getSelectedFile();
            
            this.writeContents(v_JTable ,v_RowIndexArr ,v_SaveFile);
        }
    }

    

    @Override
    public void transactionBefore(ActionEvent arg0)
    {
        this.getAppFrame().setTables_Tools_Edit_Enabled(false);
        SubmitActionListener.$MY.setQueryConditionsEnabled(false);
    }
    
    
    
    @Override
    public void transactionAfter(ActionEvent arg0)
    {
        this.getAppFrame().setTables_Tools_Edit_Enabled(true);
        SubmitActionListener.$MY.setQueryConditionsEnabled(true);
    }
    
    
    
    /**
     * 将查询结果导出到文件中
     * 
     * @param i_JTable
     * @param i_RowIndexArr
     * @param i_SaveFile
     */
    private void writeContents(JTable i_JTable ,int [] i_RowIndexArr ,File i_SaveFile)
    {
        FileOutputStream   v_SaveOutput = null;
        OutputStreamWriter v_SaveWriter = null;
        
        try
        {
            v_SaveOutput = new FileOutputStream(i_SaveFile);
            v_SaveWriter = new OutputStreamWriter(v_SaveOutput ,"UTF-8"); 
            int v_Count = 0;
            int v_RowNo = -1;
            
            if ( i_RowIndexArr.length <= 0 )
            {
                v_Count = i_JTable.getModel().getRowCount();
                for (int i=0; i<v_Count; i++)
                {
                    this.getAppFrame().showHintInfo("导出中 ... ... " + (i + 1) + " / " + v_Count ,Color.BLUE);
                    
                    if ( v_RowNo != Integer.parseInt(i_JTable.getValueAt(i ,0).toString()) )
                    {
                        if ( v_RowNo >= 0 )
                        {
                            v_SaveWriter.write(JavaHelp.getSysLineSeparator());
                        }
                        
                        v_RowNo = Integer.parseInt(i_JTable.getValueAt(i ,0).toString());
                    }
                    
                    v_SaveWriter.write(writePut(i_JTable ,i ,this.getTableName()));
                }
            }
            else
            {
                v_Count = i_RowIndexArr.length;
                for (int i=0; i<i_RowIndexArr.length; i++)
                {
                    this.getAppFrame().showHintInfo("导出中 ... ... " + (i + 1) + " / " + v_Count ,Color.BLUE);
                    
                    if ( v_RowNo != Integer.parseInt(i_JTable.getValueAt(i_RowIndexArr[i] ,0).toString()) )
                    {
                        if ( v_RowNo >= 0 )
                        {
                            v_SaveWriter.write(JavaHelp.getSysLineSeparator());
                        }
                        
                        v_RowNo = Integer.parseInt(i_JTable.getValueAt(i_RowIndexArr[i] ,0).toString());
                    }
                    
                    v_SaveWriter.write(writePut(i_JTable ,i_RowIndexArr[i] ,this.getTableName()));
                }
            }
            
            v_SaveWriter.flush();
            
            this.getAppFrame().showHintInfo("导出中完成。共生成 " + v_Count + " 条put命令" ,Color.BLUE);
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
    
    
    
    /**
     * 将结果列表中一行数据，变成一条put命令
     * 
     * @param i_JTable
     * @param i_Index
     * @return
     */
    public static String writePut(JTable i_JTable ,int i_Index ,String i_TableName)
    {
        StringBuilder v_Buffer = new StringBuilder();
     
        v_Buffer.append("put '").append(i_TableName).append("'");
        
        // 行主键
        v_Buffer.append(",'");
        v_Buffer.append(i_JTable.getValueAt(i_Index ,1).toString());
        v_Buffer.append("'");
        
        // 列族名:字段名
        v_Buffer.append(",'");
        v_Buffer.append(i_JTable.getValueAt(i_Index ,2).toString());
        v_Buffer.append(":");
        v_Buffer.append(i_JTable.getValueAt(i_Index ,3).toString());
        v_Buffer.append("'");
        
        // 字段值
        v_Buffer.append(",'");
        v_Buffer.append(i_JTable.getValueAt(i_Index ,4).toString());
        v_Buffer.append("'");
        
        
        v_Buffer.append(";").append(JavaHelp.getSysLineSeparator());
        
        return v_Buffer.toString();
    }
    
}
