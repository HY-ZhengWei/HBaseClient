package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JTextArea;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.xml.XJava;





public class CopyActionListener extends ShowDialogAction implements JButtonOnClickTransaction
{

    @Override
    public void onClick(ActionEvent i_Event)
    {
        if ( JavaHelp.isNull(this.getAppFrame().getTableName()) )
        {
            this.getAppFrame().showHintInfo("请先选择要清空的表" ,Color.BLUE);
            return;
        }
        
        if ( this.getAppFrame().getSelectColCount() <= 1 )
        {
            this.getAppFrame().showHintInfo("请选择查询结果列表中的多行数据" ,Color.BLUE);
            return;
        }
        
        
        JTable v_JTable      = (JTable)XJava.getObject("xtDataList");
        int [] v_RowIndexArr = v_JTable.getSelectedRows();
        
        String v_CMDPuts = this.writeContents(v_JTable ,v_RowIndexArr);
        
        ((JTextArea)XJava.getObject("xtPutsInfo")).setText(v_CMDPuts);
        
        super.onClick(i_Event);
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
     * 将查询结果导出到内存中
     * 
     * @param i_JTable
     * @param i_RowIndexArr
     */
    private String writeContents(JTable i_JTable ,int [] i_RowIndexArr)
    {
        StringBuilder v_Buffer = new StringBuilder();
        
        try
        {
            int v_Count = 0;
            int v_RowNo = -1;
            
            v_Count = i_RowIndexArr.length;
            for (int i=0; i<i_RowIndexArr.length; i++)
            {
                this.getAppFrame().showHintInfo("生成中 ... ... " + (i + 1) + " / " + v_Count ,Color.BLUE);
                
                if ( v_RowNo != Integer.parseInt(i_JTable.getValueAt(i_RowIndexArr[i] ,0).toString()) )
                {
                    if ( v_RowNo >= 0 )
                    {
                        v_Buffer.append(JavaHelp.getSysLineSeparator());
                    }
                    
                    v_RowNo = Integer.parseInt(i_JTable.getValueAt(i_RowIndexArr[i] ,0).toString());
                }
                
                v_Buffer.append(ExportActionListener.writePut(i_JTable ,i_RowIndexArr[i] ,this.getTableName()));
            }
            
            this.getAppFrame().showHintInfo("生成完成。共生成 " + v_Count + " 条put命令" ,Color.BLUE);
        }
        catch (Exception exce)
        {
            this.getAppFrame().showHintInfo(exce.getMessage() ,Color.RED);
        }
        
        return v_Buffer.toString();
    }
    
}
