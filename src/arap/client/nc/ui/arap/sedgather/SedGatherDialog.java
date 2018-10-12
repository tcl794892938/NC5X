package nc.ui.arap.sedgather;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.vo.arap.sedgather.AggSedGatherVO;
import nc.vo.arap.sedgather.SedGatherDVO;
import nc.vo.arap.sedgather.SedGatherHVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class SedGatherDialog extends UIDialog {

	BillListPanel list;
	SedGatherDialog dlg;
	AggSedGatherVO aggvo;
	SedGatherHVO sghvo;
	SedGatherDVO sgdvo;
	
	public SedGatherDialog(Container arg0) {
		super(arg0);
		this.setModal(false);
	}

	public AggSedGatherVO showSedGatherDialog() throws Exception{
		initialize();
		initDialog();
		initValue();
		aggvo = null;
		dlg = this;
		this.showModal();
		return aggvo;
	}
	
	/**
	 * 初始化对话框面板中数据
	 */
	private void initialize() {
		// 初始化为空
		if (null == this.list) {
			// 初始化卡片单据模板
			this.list = new BillListPanel();
			this.list.loadTemplet("0001AA100000000OIXOS");
//			this.list.setParentMultiSelect(true);
		}
	}
	
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("二次收款");
		// 设置最适合的大小
		this.setSize(new Dimension(800,600));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		UIButton sureBtn = new UIButton("确  定");		
		UIButton notBtn = new UIButton("取  消");
		
		// 加监听
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());
		
		this.list.addHeadEditListener(new BillEditListener(){

			public void afterEdit(BillEditEvent e) {
				
			}

			public void bodyRowChange(BillEditEvent e) {
				try{
					int row = e.getRow();
					if(row>-1){
						BillModel headmodel = list.getHeadBillModel();
						String pk_saleorder = headmodel.getValueAt(row, "pk_saleorder").toString();
						
						sghvo = new SedGatherHVO();
						sghvo.setPk_saleorder(pk_saleorder);
						sghvo.setPk_cust(headmodel.getValueAt(row, "pk_cust").toString());
						sghvo.setSalebillno(headmodel.getValueAt(row, "salebillno").toString());
						sghvo.setSalebilldate(new UFDate(headmodel.getValueAt(row, "salebilldate").toString()));
						sghvo.setSale_nums(new UFDouble(headmodel.getValueAt(row, "sale_nums").toString()));
						sghvo.setSale_amount(new UFDouble(headmodel.getValueAt(row, "sale_amount").toString()));
						
						
						String icsql = "select h.cgeneralhid, h.vbillcode, h.dbilldate,nvl(h.vuserdef16, 0) noutnum,nvl(h.vuserdef16, 0)*sum(nvl(b.nmny,0))/sum(nvl(b.noutnum,0)) nmny " +
								" from ic_general_h h, ic_general_b b where h.cgeneralhid = b.cgeneralhid and nvl(b.dr, 0) = 0 " +
								" and b.cfirstbillhid = '"+pk_saleorder+"' group by h.cgeneralhid, h.vbillcode, h.dbilldate,nvl(h.vuserdef16, 0) ";
						
						String apsql = " select z.vouchid,z.djbh,z.djrq,z.bbje from arap_djzb z, arap_djfb f where z.vouchid = f.vouchid " +
								" and nvl(z.dr,0)=0  and f.ddlx = '"+pk_saleorder+"' ";
					   
					   IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					
					   List<Map<String,Object>> icmaplist = (List<Map<String,Object>>)iQ.executeQuery(icsql, new MapListProcessor());
					   Map<String,Object> icmap = icmaplist.get(0);
					   
					   List<Map<String,Object>> apmaplist = (List<Map<String,Object>>)iQ.executeQuery(apsql, new MapListProcessor());
					   Map<String,Object> apmap = apmaplist.get(0);
					  
					   BillModel bodymodel = list.getBodyBillModel();
					   
					   bodymodel.clearBodyData();
					   
					   bodymodel.addLine();
					   bodymodel.setValueAt(icmap.get("cgeneralhid"), 0, "pk_saleout");
					   bodymodel.setValueAt(icmap.get("vbillcode"), 0, "saleout_no");
					   bodymodel.setValueAt(icmap.get("dbilldate"), 0, "saleout_date");
					   bodymodel.setValueAt(icmap.get("noutnum"), 0, "saleout_nums");
					   bodymodel.setValueAt(icmap.get("nmny"), 0, "saleout_amount");
					 
					   bodymodel.setValueAt(apmap.get("vouchid"), 0, "pk_gathering");
					   bodymodel.setValueAt(apmap.get("djbh"), 0, "gather_no");
					   bodymodel.setValueAt(apmap.get("djrq"), 0, "gather_date");
					   bodymodel.setValueAt(apmap.get("bbje"), 0, "gather_amount");
					   
					   sgdvo = new SedGatherDVO();
					   sgdvo.setPk_saleout(icmap.get("cgeneralhid").toString());
					   sgdvo.setSaleout_no(icmap.get("vbillcode").toString());
					   sgdvo.setSaleout_date(new UFDate(icmap.get("dbilldate").toString()));
					   sgdvo.setSaleout_nums(new UFDouble(icmap.get("noutnum").toString()));
					   sgdvo.setSaleout_amount(new UFDouble(icmap.get("nmny").toString()));
					   
					   sgdvo.setPk_gathering(apmap.get("vouchid").toString());
					   sgdvo.setGather_no(apmap.get("djbh").toString());
					   sgdvo.setGather_date(new UFDate(apmap.get("djrq").toString()));
					   sgdvo.setGather_amount(new UFDouble(apmap.get("bbje").toString()));
					}
				}catch(Exception ep){
					ep.printStackTrace();
				}
			}
		});
		
		
		UIPanel panel = new UIPanel();
		panel.add(sureBtn);
		panel.add(notBtn);
		
		// 将panel加载到对话框中
		this.add(panel, BorderLayout.SOUTH);
		// 将单据面板放入对话框的中间
		this.add(this.list, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initValue() throws Exception{
		String sql = "select v.pk_saleorder,v.salebillno,v.salebilldate,v.pk_cust," +
				"(select b.custname from bd_cubasdoc b,bd_cumandoc m where b.pk_cubasdoc = m.pk_cubasdoc and m.pk_cumandoc = v.pk_cust) custname," +
				"v.sale_nums,v.sale_amount from v_bx_sedgather v";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<Map<String,Object>> maplist = (List<Map<String,Object>>)iQ.executeQuery(sql, new MapListProcessor());
		if(null != maplist && maplist.size()>0){
			BillModel headmodel = this.list.getHeadBillModel();
			for(int i=0;i<maplist.size();i++){
				headmodel.addLine();
				Map<String,Object> map = maplist.get(i);
				for(String keys : map.keySet()){
					headmodel.setValueAt(map.get(keys), i, keys);
				}
			}
		}
	}
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			if(null == sghvo){
				MessageDialog.showHintDlg(list, "提示", "请选择单据");
				return;
			}
			
			if(null == sgdvo){
				MessageDialog.showHintDlg(list, "提示", "单据有误，请重新确认信息");
				return;
			}
			
			aggvo = new AggSedGatherVO();
			aggvo.setParentVO(sghvo);
			aggvo.setChildrenVO(new SedGatherDVO[]{sgdvo});
			
			dlg.setVisible(false);
			dlg.close();
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	class NotMouseLister implements MouseListener{
		public void mouseClicked(MouseEvent arg0) {
			dlg.setVisible(false);
			dlg.close();
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
}
