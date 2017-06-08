package org.hy.hbase.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import org.hy.common.JavaHelp;
import org.hy.common.hbase.HData;
import org.hy.common.ui.JButtonOnClickTransaction;
import org.hy.common.ui.JComboBox;
import org.hy.common.xml.XJava;
import org.hy.hbase.ResultCellRenderer;





/**
 * 1. 主窗口       查询按钮的点击事件
 * 2. 主窗口过滤查询按钮的点击事件
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-05-31
 */
public class SubmitActionListener extends BaseEvent implements JButtonOnClickTransaction ,KeyListener
{	
    /** 给其它事件调用setQueryConditionsEnabled()方法使用 */
    public static  SubmitActionListener $MY;
    
    private static JComponent           $myself;
    
    
	private Map<String ,Object>         queryConditionMap;
	
	
	
	
	
	public SubmitActionListener()
	{
	    $MY = this;
		this.queryConditionMap = new Hashtable<String ,Object>();
	}
	
	
	
	public void actionPerformed(ActionEvent e) 
	{
		this.submit();
	}
	
	

	public void keyPressed(KeyEvent e) 
	{
		
	}


	public void keyReleased(KeyEvent e)
	{
		if ( e.getKeyChar() == '\n' )
		{
			((JButton)e.getSource()).doClick();
		}
	}
	
	
	public void keyTyped(KeyEvent e) 
	{
		
	}
	
	
	public void transactionBefore(ActionEvent i_ActionEvent) 
	{
		// 所有查询条件控件及按钮本身都失效
	    $myself = ((JComponent)(i_ActionEvent.getSource()));
		
		this.setQueryConditionsEnabled(false);
		
		this.getAppFrame().setTables_Tools_Edit_Enabled(false);
		this.getAppFrame().initTableModel();
		this.getAppFrame().showHintInfo("请稍等，查询中..." ,Color.BLUE);
	}
	
	
	public void onClick(ActionEvent arg0) 
	{
		this.submit();
	}
	
	
	public void transactionAfter(ActionEvent i_ActionEvent) 
	{
		// 所有查询条件控件及按钮本身都有效
		this.setQueryConditionsEnabled(true);
		this.getAppFrame().setTables_Tools_Edit_Enabled(true);
		
		this.showResultsCount();
	}
	
	
	
	/**
	 * 显示查询结果的统计数据
	 */
	public void showResultsCount()
	{
	    if ( JavaHelp.isNull(this.getTableName()) )
        {
            this.getAppFrame().showHintInfo("请先选择要查询的表" ,Color.BLUE);
        }
	    else
	    {
	        this.getAppFrame().showHintInfo("查询到 " + this.getAppFrame().getRowCount() + " 条记录，" + this.getAppFrame().getColCount() + " 条字段数" ,Color.BLUE);
	    }
	}
	
	
	
	/**
	 * 点击操作
	 */
	private void submit()
	{
	    if ( JavaHelp.isNull(this.getTableName()) )
	    {
            return;
	    }
	    
	    List<HData>                      v_Conditions = new ArrayList<HData>();
	    Map<String ,Map<String ,Object>> v_Datas      = null;
		HData                            v_Query      = new HData();
		boolean                          v_IsFilter   = $myself.getName().equals("xbSubmitFilter");
		
		v_Query.setRowKey(    ((JTextComponent)this.queryConditionMap.get("RowKey"))     .getText().trim());
		v_Query.setFamilyName(((JComboBox)     this.queryConditionMap.get("FamilyName")) .getSelectedItem().toString().trim());
		v_Query.setColumnName(((JComboBox)     this.queryConditionMap.get("ColumnName")) .getSelectedItem().toString().trim());
		v_Query.setValue(     ((JTextComponent)this.queryConditionMap.get("ColumnValue")).getText().trim());
		v_Query.setLike(     !((JCheckBox)     this.queryConditionMap.get("IsLike"))     .isSelected());
		
		v_Conditions.add(v_Query);
		
		
		if ( !v_IsFilter && !JavaHelp.isNull(v_Query.getValue().toString()) )
		{
		    ResultCellRenderer.findValue      = v_Query.getValue().toString();
		    ResultCellRenderer.findColumnName = JavaHelp.isNull(v_Query.getColumnName()) ? null : v_Query.getColumnName();
		}
		else
		{
		    ResultCellRenderer.findValue      = null;
		    ResultCellRenderer.findColumnName = null;
		}
		
		
		// 检查查询条件是否有效的，并将无效的HData删除
		for (int i=v_Conditions.size()-1; i>=0; i--)
		{
		    HData v_HData = v_Conditions.get(i);
		    
		    if ( JavaHelp.isNull(v_HData.getRowKey()) 
		      && JavaHelp.isNull(v_HData.getFamilyName())
		      && JavaHelp.isNull(v_HData.getColumnName())
		      && JavaHelp.isNull(v_HData.getValue().toString()) )
		    {
		        v_Conditions.remove(i);
		    }
		    else if ( v_IsFilter )
		    {
		        if ( !JavaHelp.isNull(v_HData.getFamilyName()) && !JavaHelp.isNull(v_HData.getColumnName()) )
		        {
		            if ( JavaHelp.isNull(v_HData.getRowKey()) && JavaHelp.isNull(v_HData.getValue().toString()) )
		            {
		                // 不在条件的过滤查询
		                v_Conditions.remove(i);
		            }
		            
		            HData v_HDataFilter = new HData();
		            
		            v_HDataFilter.setFamilyName(v_HData.getFamilyName());
		            v_HDataFilter.setColumnName(v_HData.getColumnName());
		            
		            v_Conditions.add(v_HDataFilter);
		        }
		    }
		}
		this.getAppFrame().setConditions(v_Conditions);
		
		
		this.getHPage().setRowKey("");
		if ( JavaHelp.isNull(v_Conditions) )
		{
		    v_Datas = this.getHBase().getRows(this.getTableName() ,this.getHPage());
		}
		else
		{
		    v_Datas = this.getHBase().getRows(this.getTableName() ,this.getHPage() ,v_Conditions);
		}
		
		this.getAppFrame().setRowCount(0);
		this.addRows(v_Datas);
	}
	
	
	
	/**
	 * 将查询结果集，展示在前台列表中
	 * 
	 * @param i_Datas
	 */
	public void addRows(Map<String ,Map<String ,Object>> i_Datas)
	{
	    if ( !JavaHelp.isNull(i_Datas) )
	    {
    	    long v_RowIndex = this.getAppFrame().getRowCount();
            long v_RowSize  = this.getAppFrame().getRowCount() + i_Datas.size();
            
            this.getAppFrame().setRowCount(v_RowSize);
            
            for (String v_RowKey : i_Datas.keySet())
            {
                v_RowIndex++;
                Map<String ,Object> v_RowInfo  = i_Datas.get(v_RowKey);
                int                 v_ColIndex = 0;
                
                for (Object v_ColObj : v_RowInfo.values())
                {
                    HData v_HData = (HData)v_ColObj;
                    this.getAppFrame().addRowInfo(v_RowSize ,v_RowIndex ,++v_ColIndex ,toObjects(v_RowIndex ,v_HData));
                }
            }
	    }
	}
	
	
	
	public Object [] toObjects(long i_RowIndex ,HData i_HData)
	{
	    return new Object[]{i_RowIndex
	                       ,i_HData.getRowKey() 
                           ,i_HData.getFamilyName()
                           ,i_HData.getColumnName()
                           ,i_HData.getValue()
                           ,i_HData.getTimestamp()
                           ,i_HData.getTime().getFullMilli()};
	}

	
	
	public Map<String, Object> getQueryConditions() 
	{
		return queryConditionMap;
	}


	
	public void setQueryConditions(Map<String, Object> queryConditionMap) 
	{
		this.queryConditionMap = queryConditionMap;
	}
	
	
	
	/**
	 * 设置所有查询条件控件是否有效
	 * 
	 * 当 i_Enabled 为真时，对特别控件有业务逻辑判断
	 * 
	 * @param i_Enabled
	 */
	public void setQueryConditionsEnabled(boolean i_Enabled)
	{
		// 先设置事情源本身
	    $myself.setEnabled(i_Enabled);
		
		
		Iterator<?> v_Iterator = this.queryConditionMap.values().iterator();
		while ( v_Iterator.hasNext() )
		{
			Object v_QueryCondition = v_Iterator.next();
			
			if ( v_QueryCondition instanceof JComponent )
			{
				JComponent v_QC = (JComponent)v_QueryCondition;
				
				v_QC.setEnabled(i_Enabled);
			}
		}
		
		
		boolean v_Enable = false;
		if ( i_Enabled )
		{
		    // 设置过滤查询按钮的可用性
	        String  v_FileName   = ((JComboBox)XJava.getObject("FamilyName")).getSelectedItem().toString();
	        String  v_ColumnName = ((JComboBox)XJava.getObject("ColumnName")).getSelectedItem().toString();
	        
	        if ( !JavaHelp.isNull(v_FileName) && !JavaHelp.isNull(v_ColumnName) )
	        {
	            v_Enable = true;
	        }
		}
		
		((JComponent)XJava.getObject("xbSubmit"))      .setEnabled(i_Enabled);
        ((JComponent)XJava.getObject("xbSubmitFilter")).setEnabled(v_Enable);
	}
	
}
