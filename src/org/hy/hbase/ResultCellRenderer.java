package org.hy.hbase;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;





/**
 * 结果集列表的单元格渲染器。
 * 
 * 作于将相同RowKey的多个记录，显示相同的背景色 -- 即"隔行"变色功能
 * 
 * @author ZhengWei(HY)
 * @create 2014-06-04
 */
public class ResultCellRenderer extends DefaultTableCellRenderer
{

    private static final long  serialVersionUID            = -6861189579038172617L;
    
    /** "隔行"变色功能中的奇数行的背景色 */
    private static final Color $COLOR_FIRST_BG             = Color.WHITE;
    
    /** "隔行"变色功能中的偶数行的背景色 */
    private static final Color $COLOR_SECOND_BG            = new Color(206 ,255 ,206);
    
    /** "隔行"变色功能中的偶数行的背景色 */
    private static final Color $COLOR_FIND_NONE_FG         = Color.BLACK;
    
    /** "隔行"变色功能中的偶数行的背景色 */
    private static final Color $COLOR_FIND_FG              = new Color(255 ,136 ,0);
    
    /** 是否对匹配的字段值单独变色 */
    public  static       String findColumnName             = null;
    public  static       String findValue                  = null;
    
    
    
    public Component getTableCellRendererComponent(JTable  i_Table
                                                  ,Object  i_Value
                                                  ,boolean i_IsSelected
                                                  ,boolean i_HasFocus
                                                  ,int     i_Row
                                                  ,int     i_Column)
    {
        try
        {
            long v_RowNo = Long.parseLong(i_Table.getValueAt(i_Row ,0).toString());
            
            if ( v_RowNo % 2 == 1 )
            {
                this.setBackground($COLOR_FIRST_BG);
            }
            else
            {
                this.setBackground($COLOR_SECOND_BG);
            }
            
            if ( findValue != null && i_Column == 4 )
            {
                int v_Index = i_Value.toString().indexOf(findValue);
                
                if ( v_Index >= 0 )
                {
                    if ( findColumnName != null )
                    {
                        String v_ColumName = i_Table.getValueAt(i_Row ,3).toString();
                        
                        if ( v_ColumName.equals(findColumnName) )
                        {
                            this.setForeground($COLOR_FIND_FG);
                        }
                        else
                        {
                            this.setForeground($COLOR_FIND_NONE_FG);
                        }
                    }
                    else
                    {
                        this.setForeground($COLOR_FIND_FG);
                    }
                }
                else
                {
                    this.setForeground($COLOR_FIND_NONE_FG);
                }
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }

        return super.getTableCellRendererComponent(i_Table ,i_Value ,i_IsSelected ,i_HasFocus ,i_Row ,i_Column);
    }
    
}
