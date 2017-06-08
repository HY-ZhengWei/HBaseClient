package org.hy.hbase.event;

import java.awt.event.ActionEvent;
import java.util.Map;

import org.hy.common.JavaHelp;
import org.hy.common.ui.JButtonOnClickTransaction;





/**
 * 工具面板上下一页按钮的点击事件
 *
 * @author   ZhengWei(HY)
 * @version  V1.0  2014-05-31
 */
public class NextPageActionListener extends BaseEvent implements JButtonOnClickTransaction
{
	
	public NextPageActionListener()
	{
		
	}
	

	
	public void transactionBefore(ActionEvent arg0) 
	{
		if ( SubmitActionListener.$MY != null )
		{
		    SubmitActionListener.$MY.setQueryConditionsEnabled(false);
		}
		
		this.getAppFrame().setTables_Tools_Edit_Enabled(false);
	}
	
	
	
	public void onClick(ActionEvent arg0) 
	{
	    Map<String ,Map<String ,Object>> v_Datas = null;
	    
	    if ( this.getHPage().getRowKey() != null )
	    {
    	    if ( JavaHelp.isNull(this.getAppFrame().getConditions()) )
    	    {
    	        v_Datas = this.getHBase().getRows(this.getTableName() ,this.getHPage());
    	    }
    	    else
    	    {
    	        v_Datas = this.getHBase().getRows(this.getTableName() ,this.getHPage() ,this.getAppFrame().getConditions());
    	    }
    	    
    	    SubmitActionListener.$MY.addRows(v_Datas);
	    }
	}
	
	
	
	public void transactionAfter(ActionEvent i_ActionEvent) 
	{
	    this.getAppFrame().setTables_Tools_Edit_Enabled(true);
	    
		if ( SubmitActionListener.$MY != null )
		{
		    SubmitActionListener.$MY.setQueryConditionsEnabled(true);
		    SubmitActionListener.$MY.showResultsCount();
		}
	}
	
}

