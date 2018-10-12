package nc.ui.ar.m20060301;

import nc.ui.arap.actions.SearchActionNor;
import nc.ui.ep.dj.DjPflowPanel;
import nc.ui.pub.linkoperate.ILinkType;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.pub.BusinessException;

public class Ar20060306  extends DjPflowPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3787994406728040662L;
	public Ar20060306(){
		 super(0);
	     setM_Node("2006030101");
	     //setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation().getPrimaryKey(),nc.vo.arap.global.ResMessage.$SysCode_AR));
	}
	protected void initialize( ) {
		initialize(true,false,true);
		//initBillListTemplet();
	}
    public void postInit() {
    	 if(ILinkType.NONLINK_TYPE==this.getLinkedType()){
	    	
	        //setDjlxbm("D0");//设置单据类型编码
	        setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(
	                getClientEnvironment().getCorporation().getPrimaryKey(), 0));
	        super.postInit();
 	        
    	}
    }
    public String getNodeCode() {
    	return this.getDjSettingParam().getNodeID();
    }
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000837")/*@res "单据录入"*/;	}
 
}