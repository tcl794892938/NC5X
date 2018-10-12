package nc.ui.dahuan.stuff;

import java.awt.Container;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import nc.ui.dahuan.exceltools.ExcelTools;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.dahuan.stuff.StuffMXVO;
import nc.vo.dahuan.stuff.StuffVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoSave() throws Exception {
		// 校验必输项
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		
		// 要有明细
		BillModel bmodel = card.getBillModel();
		int rows = bmodel.getRowCount();
		if(rows<1){
			MessageDialog.showHintDlg(card, "提示", "请维护明细");
			return;
		}
		
		card.dataNotNullValidate();
		
		// 统计总额
		UFDouble summny = new UFDouble("0.00");
		
		for(int i=0;i<rows;i++){
			Object jeObj = bmodel.getValueAt(i, "stuff_amount");
			if(null != jeObj && !"".equals(jeObj)){
				summny = summny.add(new UFDouble(jeObj.toString()));
			}
		}
		card.setHeadItem("summny", summny);
		
		super.onBoSave();
	}
	
	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			return;
		}
		StuffVO svo = (StuffVO)aggvo.getParentVO();
		svo.setVbillstatus(1);
		HYPubBO_Client.update(svo);
		
		JobmngfilVO jvo = (JobmngfilVO)HYPubBO_Client.queryByPrimaryKey(JobmngfilVO.class, svo.getPk_jobmngdoc());
		UFDouble xhj = svo.getSummny().add(new UFDouble(jvo.getDef12()));
		jvo.setDef12(xhj.toString());
		HYPubBO_Client.update(jvo);
		
		this.onBoRefresh();
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			return;
		}
		StuffVO svo = (StuffVO)aggvo.getParentVO();
		svo.setVbillstatus(0);
		HYPubBO_Client.update(svo);
		
		JobmngfilVO jvo = (JobmngfilVO)HYPubBO_Client.queryByPrimaryKey(JobmngfilVO.class, svo.getPk_jobmngdoc());
		UFDouble xhj = svo.getSummny().sub(new UFDouble(jvo.getDef12()));
		jvo.setDef12(xhj.toString());
		HYPubBO_Client.update(jvo);
		
		this.onBoRefresh();
	}

	@Override
	protected void onBoImport() throws Exception {
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		// 选择文件
		String fileName = getChooseFileName(card);
		if(null == fileName || "".equals(fileName)){
			return;
		}
		
		if(!fileName.endsWith(".xls")){
			MessageDialog.showHintDlg(card, "提示", "请使用Excel2003格式！");
			return;
		}
		
		Object[][] exlimp = doExcel2003(fileName);
		
		List<StuffMXVO> stufflist = new ArrayList<StuffMXVO>();
		
		int ksrow = -1;
		boolean jsflag = true;
		for(int h=0;h<exlimp.length;h++){
			Object[] exlrow = exlimp[h];
			if(-1 != ksrow){
				if(null == exlrow || null == exlrow[0] || "".equals(exlrow[0])){
					jsflag = false;
				}
			}
			if(null != exlrow && null != exlrow[0] && "货品编码".equals(exlrow[0])){
				ksrow = h+1;
				jsflag = true;
			}
			if(jsflag && ksrow == h){
				
				StuffMXVO mxvo = new StuffMXVO();
				mxvo.setStuff_code(exlrow[0].toString());
				mxvo.setStuff_name(exlrow[8].toString());
				mxvo.setStuff_size(exlrow[13].toString());
				mxvo.setStuff_unit(exlrow[19].toString());
				mxvo.setStuff_price(new UFDouble(exlrow[22].toString()));
				mxvo.setStuff_num(new UFDouble(exlrow[29].toString()));
				mxvo.setStuff_amount(new UFDouble(null!=exlrow[32]&&!"".equals(exlrow[32])?exlrow[32].toString():exlrow[33].toString()));
				mxvo.setVemo(null!=exlrow[38]?exlrow[38].toString():exlrow[39].toString());
				stufflist.add(mxvo);
				
				ksrow++;
			}
		}
		
		if(null != stufflist && stufflist.size()>0){
			BillModel bmodel = card.getBillModel();
			int rows = bmodel.getRowCount();
			for(StuffMXVO mxvo : stufflist){
				bmodel.addLine();
				bmodel.setBodyRowVO(mxvo, rows);
				rows++;
			}
		}
	}
	
//	 导入2003版本的Excel
	public Object[][] doExcel2003(String fileName) throws Exception{
		// 定义Object二维数组
		Object[][] values = null;
		// 输入流
		InputStream is = new FileInputStream(fileName);
		// 创建excel工具类对象
		ExcelTools tool = new ExcelTools(is);
		// 关闭流
		is.close();
		// 取出指定sheet行数
		int rowCount = tool.getRowCountOfSheet(0);
		// 判断行数是否大于0
		if (rowCount > 0) {
			// 设置当前行第1行
			
			tool.setRow(0);
			// 取出指定行的单元格数量
			short cellCount = tool.getCellCountofRow();
			// 初始化Object二维数组
			values = new Object[rowCount][cellCount];

			for (int i = 0; i < rowCount; i++) {
				// 设置当前行
				tool.setRow(i);
				for (short j = 0; j < cellCount; j++) {
						values[i][j] = tool.getValueAt(j);
				}
			}
		}
		
		// 返回
		return values;
	}
	
	/**
	 * 文件选取
	 * @return 选取的文件路径
	 * */
	public static String getChooseFileName(Container parent) {
		// 新建一个文件选择框
		JFileChooser fileChooser = new JFileChooser();
		// 打开保存框
		int retVal = fileChooser.showOpenDialog(parent);
		// 定义返回变量
		String path = null;
		// 判断是否打开
		if (retVal == JFileChooser.APPROVE_OPTION) {
			// 确认打开，获取选择的路径
			path = fileChooser.getSelectedFile().getPath();

		}
		// 返回路径
		return path;

	}
	
}
