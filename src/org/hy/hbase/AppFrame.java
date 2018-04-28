package org.hy.hbase;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.hy.common.CounterMap;
import org.hy.common.Help;
import org.hy.common.hbase.HBase;
import org.hy.common.hbase.HData;
import org.hy.common.hbase.HPage;
import org.hy.common.ui.JButton;
import org.hy.common.ui.JComboBox;
import org.hy.common.ui.JLabel;
import org.hy.common.ui.JMenuBar;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;





/**
 * 图形界面的主窗口
 * 
 * @author ZhengWei(HY)
 * @create 2014-05-30
 */
@Xjava(XType.XML)
public class AppFrame extends JFrame
{
    
    private static final long serialVersionUID = -1836370997741743657L;
    
    /** 数据库操作类 */
    private HBase                    hbase;
    
    /** 当前选中的数据库表名称 */
    private String                   tableName;
 
    /** 当前选中的数据库表的结构 */
    private CounterMap<String>       structure;       
    
    /** 查询分页信息 */
    private HPage                    hpage;
    
    /** 
     * 当前查询条件。
     * 
     * 用于下一页及最后一页的功能。
     * 当使用上面的功能时，不再读取控件上的信息，即使控件上的内容已有变化。
     */
    private List<HData>              conditions;
    
    /** 左侧数据库表列表的数据模型 */
    private DefaultListModel<String> listModel;
    
    /** 查询结果集列表的数据模型 */
    private DefaultTableModel        tableModel;
    
    /** 逻辑行数。即实际行数 */
    private long                     rowCount;
    
    
    
    public AppFrame()
    {
        this.hpage = new HPage(500);
    }
    
    
    
    /**
     * 初始化主窗口信息
     */
    public void init()
    {
        this.setJMenuBar((JMenuBar)XJava.getObject("xmMainMenuBar"));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add((Component)XJava.getObject("xpMain") ,BorderLayout.CENTER);
        this.setEnabled(false);
        
        JComponent v_JC = ((JComponent)XJava.getObject("xlTables"));
        v_JC.setSize(50 ,v_JC.getHeight());
        
        this.showHintInfo("请稍后，正在访问数据库..." ,Color.BLUE);
        
        this.initListModel();
        this.initTableModel();
        this.initTablesInfo();
        
        this.setEnabled(true);
        this.showHintInfo("欢迎使用！");
    }
    
    
    
    /**
     * 初始化数据库表列表的数据模型
     */
    @SuppressWarnings("unchecked")
    public void initListModel()
    {
        JList<String> v_Tables = (JList<String>)XJava.getObject("xlTables");
        
        this.listModel = new DefaultListModel<String>();
        
        v_Tables.setModel(this.listModel);
        v_Tables.setBackground(this.getBackground());
    }
    
    
    
    /**
     * 初始化查询结果集列表的数据模型
     */
    public void initTableModel()
    {
        List<?> v_Titles = (List<?>)XJava.getObject("titles");
        this.tableModel = new DefaultTableModel();
        
        for (int v_ColIndex=0; v_ColIndex<v_Titles.size(); v_ColIndex++)
        {
            this.tableModel.addColumn(v_Titles.get(v_ColIndex));
        }
        
        
        JTable v_xtDataList = (JTable)XJava.getObject("xtDataList");
        v_xtDataList.setModel(this.tableModel);
        
        
        List<?> v_Titles_Size    = (List<?>)XJava.getObject("titles_Size");
        List<?> v_Titles_MaxSize = (List<?>)XJava.getObject("titles_MaxSize");
        for (int v_ColIndex=0; v_ColIndex<v_Titles_Size.size(); v_ColIndex++)
        {
            int v_Size    = Integer.parseInt(v_Titles_Size   .get(v_ColIndex).toString());
            int v_MaxSize = Integer.parseInt(v_Titles_MaxSize.get(v_ColIndex).toString());
            
            v_xtDataList.getColumnModel().getColumn(v_ColIndex).setMinWidth(v_Size);
            if ( v_MaxSize >= 0 )
            {
                v_xtDataList.getColumnModel().getColumn(v_ColIndex).setPreferredWidth(v_MaxSize);
            }
            
            v_xtDataList.getColumnModel().getColumn(v_ColIndex).setCellRenderer(new ResultCellRenderer());
        }
    }    
    
    
    public void initTablesInfo()
    {
        List<String> v_Tables = this.hbase.getTableNames();
        for (String v_TableName : v_Tables)
        {
            this.addTableName(v_TableName);
        }
    }
    
    
    
    /**
     * 向左侧列表中添加一行新的表记录
     * 
     * @param i_Table
     */
    public void addTableName(String i_Table)
    {
        this.listModel.addElement(i_Table);
    }
    
    
    
    /**
     * 向查询结果集列表中添加一行新的记录
     * 
     * @param i_RowSize
     * @param i_RowNo
     * @param i_ColNo
     * @param i_RowInfo
     */
    public void addRowInfo(long i_RowSize ,long i_RowNo ,int i_ColNo ,Object [] i_RowInfo) 
    {
        this.tableModel.addRow(i_RowInfo);
        
        this.showHintInfo("请稍等，查询中... " + i_ColNo + " / " + i_RowNo + " / " + i_RowSize ,Color.BLUE);
    }
    
    
    
    /**
     * 显示提示信息
     * 
     * @param i_HintInfo
     * @param i_Color
     */
    public void showHintInfo(String i_HintInfo ,Color i_Color)
    {
        JLabel v_XLHintInfo = (JLabel)XJava.getObject("xlHintInfo");
        
        v_XLHintInfo.setText(i_HintInfo);
        v_XLHintInfo.setForeground(i_Color);
        v_XLHintInfo.repaint();
        this.revalidate();
    }
    
    
    
    /**
     * 显示提示信息
     * 
     * @param i_HintInfo
     */
    public void showHintInfo(String i_HintInfo)
    {
        showHintInfo(i_HintInfo ,Color.BLACK);
    }
    
    
    
    /**
     * 设置工具栏所有按钮是否有效
     * 
     * 当 i_Enabled 为真时，对特别控件有业务逻辑判断
     * 
     * @param i_Enabled
     */
    public void setTables_Tools_Edit_Enabled(boolean i_Enabled)
    {
        if ( !i_Enabled )
        {
            ((JButton)   XJava.getObject("xbNextPage"))   .setEnabled(i_Enabled);
            ((JButton)   XJava.getObject("xbLastPage"))   .setEnabled(i_Enabled);
            ((JButton)   XJava.getObject("xbDelete"))     .setEnabled(i_Enabled);
            ((JButton)   XJava.getObject("xbTruncate"))   .setEnabled(i_Enabled);
            ((JButton)   XJava.getObject("xbExport"))     .setEnabled(i_Enabled);
            ((JList<?>)  XJava.getObject("xlTables"))     .setEnabled(i_Enabled);
            ((JComponent)XJava.getObject("xpEditRowInfo")).setEnabled(i_Enabled);
            ((JComponent)XJava.getObject("xmMainMenuBar")).setEnabled(i_Enabled);
        }
        else
        {
            boolean v_HaveData = !(this.getHPage().getRowKey() == null);    // 是否还有分页数据未显示
            boolean v_GetData  = !(this.getRowCount() == 0);
            boolean v_SelData  = this.getSelectColCount() >= 2;
            
            ((JButton)   XJava.getObject("xbNextPage"))   .setEnabled(v_HaveData);
            ((JButton)   XJava.getObject("xbLastPage"))   .setEnabled(v_HaveData);
            ((JButton)   XJava.getObject("xbDelete"))     .setEnabled(v_GetData);
            ((JButton)   XJava.getObject("xbTruncate"))   .setEnabled(v_GetData);
            ((JButton)   XJava.getObject("xbExport"))     .setEnabled(v_GetData);
            ((JButton)   XJava.getObject("xbCopy"))       .setEnabled(v_SelData);
            ((JList<?>)  XJava.getObject("xlTables"))     .setEnabled(i_Enabled);
            ((JComponent)XJava.getObject("xpEditRowInfo")).setEnabled(i_Enabled);
            ((JComponent)XJava.getObject("xmMainMenuBar")).setEnabled(i_Enabled);
        }
    }
    
    
    
    public synchronized String getTableName()
    {
        return tableName;
    }
    
    
    
    /**
     * 设置当前选择的表对象
     * 
     * 将触发以下多个动作：
     * 1. 获取列族信息  (设置查询条件及编辑面板中的列族名控件)
     * 2. 猜想表结构     (设置查询条件及编辑面板中的字段名控件)
     * 3. 全表查询动作
     * 
     * @param tableName
     */
    public synchronized void setTableName(String i_TableName)
    {
        this.tableName = i_TableName;
        
        if ( Help.isNull(this.tableName) )
        {
            return;
        }
        
        this.getHPage().setRowKey("");
        this.setTables_Tools_Edit_Enabled(true);

        
        ((JTextComponent)XJava.getObject("RowKey"))     .setText("");
        ((JTextComponent)XJava.getObject("ColumnValue")).setText("");
        ((JCheckBox)     XJava.getObject("IsLike"))     .setSelected(true);
        
        
        // 获取列族信息
        JComboBox v_FamilyNameObj = (JComboBox)XJava.getObject("FamilyName");
        JComboBox v_FamilyEditObj = (JComboBox)XJava.getObject("Edit_FamilyName");
        v_FamilyNameObj.removeAllItems();
        v_FamilyEditObj.removeAllItems();
        v_FamilyNameObj.addItem("" ,"");
        v_FamilyEditObj.addItem("" ,"");
        List<String> v_FamilyNames = this.hbase.getTableFamilyNames(this.tableName);
        for (String v_FamilyName : v_FamilyNames)
        {
            v_FamilyNameObj.addItem(v_FamilyName ,v_FamilyName);
            v_FamilyEditObj.addItem(v_FamilyName ,v_FamilyName);
        }
        
        
        // 猜想表结构
        this.structure = this.hbase.getTableStructure(this.tableName);
        this.initColumnNames("" ,(JComboBox)XJava.getObject("ColumnName"));
        this.initColumnNames("" ,(JComboBox)XJava.getObject("Edit_ColumnName"));
        
        
        // 全表查询动作
        ((JButton)XJava.getObject("xbSubmit")).doClick();
    }
    
    
    
    /**
     * 初始化字段名下拉列表框。
     * 
     * 按列族名所属关系初始化。
     * 
     * @param i_FamilyName     当为空时，初始化所有字段
     * @param i_ColumnNameObj  级联的字段名列表
     */
    public void initColumnNames(String i_FamilyName ,JComboBox i_ColumnNameObj)
    {
        i_ColumnNameObj.removeAllItems();
        i_ColumnNameObj.addItem("" ,"");
        List<String> v_Cols = new ArrayList<String>();
        
        if ( !Help.isNull(this.structure) )
        {
            for (String v_Column : this.structure.keySet())
            {
                String v_FamilyName = v_Column.split(":")[0];
                String v_ColumnName = v_Column.split(":")[1];
                
                if ( v_FamilyName.equals(i_FamilyName) || Help.isNull(i_FamilyName) )
                {
                    if ( v_ColumnName.length() <= 32 )
                    {
                        v_Cols.add(v_ColumnName);
                    }
                }
            }
            
            Collections.sort(v_Cols);
            
            for (String v_ColumnName : v_Cols)
            {
                i_ColumnNameObj.addItem(v_ColumnName ,v_ColumnName); 
            }
        }
    }



    public HBase getHBase()
    {
        return hbase;
    }
    
    
    
    public void setHBase(HBase hbase)
    {
        this.hbase = hbase;
    }

    

    public HPage getHPage()
    {
        return hpage;
    }



    public List<HData> getConditions()
    {
        return conditions;
    }


    
    public void setConditions(List<HData> i_Conditions)
    {
        this.conditions = i_Conditions;
    }
    
    
    
    /**
     * 获取总列数(即JTable列表的行数)
     * 
     * @return
     */
    public int getColCount()
    {
        if ( this.tableModel == null )
        {
            return 0;
        }
        else
        {
            return this.tableModel.getRowCount();
        }
    }
    
    
    
    /**
     * 获取选中的总列数(即JTable列表的行数)
     * 
     * @return
     */
    public int getSelectColCount()
    {
        if ( this.tableModel == null )
        {
            return 0;
        }
        else
        {
            return ((JTable)XJava.getObject("xtDataList")).getSelectedRowCount();
        }
    }

    
    
    public DefaultTableModel getTableModel()
    {
        return tableModel;
    }
    

    
    /**
     * 获取行主键不重复的行数据
     * 
     * @return
     */
    public long getRowCount()
    {
        return rowCount;
    }


    
    public void setRowCount(long rowCount)
    {
        this.rowCount = rowCount;
    }
    
}
