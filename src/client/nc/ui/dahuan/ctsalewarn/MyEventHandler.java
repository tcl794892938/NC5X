package nc.ui.dahuan.ctsalewarn;

import java.awt.Color;
import java.awt.Container;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.dahuan.ctcheck.CtCheckVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MyEventHandler extends ListEventHandler {

	public List<CtCheckVO> ctcklit;

	public MyEventHandler(BillListUI billUI, IListController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoQuery() throws Exception {

		HYQueryConditionDLG querydialog = (HYQueryConditionDLG) getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK) {
			return;
		}

		ConditionVO[] conds = querydialog.getQryCondEditor()
				.getGeneralCondtionVOs();

		// 天数
		int beforeday = 0;
		// 合同号
		String ctcode = "";
		// 合同名称
		String ctname = "";
		// 项目名称
		String xmname = "";
		// 制单部门
		String deptname = "";
		// 销售客户
		String custname = "";
		// 未付款金额
		String syje = "";
		// 公司
		String pkcorp = "";
		// 发货标志
		String isdel = "";
		// 年度
		String jobcode = "";

		if (null != conds && conds.length > 0) {
			for (ConditionVO cd : conds) {
				if ("beforedaynum".equals(cd.getFieldCode())) {
					beforeday = Integer.valueOf(cd.getValue());
				}
				if ("ctcode".equals(cd.getFieldCode())) {
					ctcode = " and v.ctcode " + cd.getOperaCode() + " '"
							+ cd.getValue() + "' ";
				}
				if ("ctname".equals(cd.getFieldCode())) {
					ctname = " and v.ctname " + cd.getOperaCode() + " '"
							+ cd.getValue() + "' ";
				}
				if ("xmname".equals(cd.getFieldCode())) {
					xmname = " and v.jobname " + cd.getOperaCode() + " '"
							+ cd.getValue() + "' ";
				}
				if ("deptname".equals(cd.getFieldCode())) {
					deptname = " and v.deptname " + cd.getOperaCode() + " '"
							+ cd.getValue() + "' ";
				}
				if ("custname".equals(cd.getFieldCode())) {
					custname = " and v.custname " + cd.getOperaCode() + " '"
							+ cd.getValue() + "' ";
				}
				if ("syje".equals(cd.getFieldCode())) {
					syje = " and v.syje " + cd.getOperaCode() + " "
							+ cd.getValue() + " ";
				}
				if ("pkcorp".equals(cd.getFieldCode())) {
					pkcorp = " and v.pk_corp " + cd.getOperaCode() + " "
							+ cd.getValue() + " ";
				}
				if ("isdelivery".equals(cd.getFieldCode())) {
					isdel = " and v.is_delivery " + cd.getOperaCode() + " "
							+ cd.getValue() + " ";
				}
				if ("jobcode".equals(cd.getFieldCode())) {
					jobcode = " and v.jobcode like '"
							+ cd.getValue().substring(2) + "%' ";
				}
			}
		}

		String sql = "select v.ctcode,v.ctname,v.jobname xmname,v.deptname,v.custname,v.prepareddate,v.no,v.syje,v.yszk,"
				+ " v.dctjetotal,v.htrq,v.djhdate from v_concheck v where v.no is not null and v.prepareddate is not null ";

		if (!"".equals(ctcode)) {
			sql += ctcode;
		}
		if (!"".equals(ctname)) {
			sql += ctname;
		}
		if (!"".equals(xmname)) {
			sql += xmname;
		}
		if (!"".equals(deptname)) {
			sql += deptname;
		}
		if (!"".equals(custname)) {
			sql += custname;
		}
		if (!"".equals(syje)) {
			sql += syje;
		}
		if (!"".equals(pkcorp)) {
			sql += pkcorp;
		}
		if (!"".equals(isdel)) {
			sql += isdel;
		}
		if (!"".equals(jobcode)) {
			sql += jobcode;
		}

		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		// 部门权限过滤
		String deptsql = "select count(1) from sm_user_role u where u.pk_corp = '"
				+ _getCorp().getPrimaryKey()
				+ "' and u.cuserid = '"
				+ _getOperator()
				+ "' "
				+ " and u.pk_role = (select r.pk_role from sm_role r where r.role_code = 'DHSX' and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		int retCot = (Integer) iQ.executeQuery(deptsql, new ColumnProcessor());
		if (0 == retCot) {
			sql += " and exists (select 1 from v_deptperonal f where v.deptname = f.dept_name and f.pk_user='"
					+ _getOperator()
					+ "' and f.pk_corp='"
					+ _getCorp().getPrimaryKey() + "') ";
		}
		if (!"1001".equals(_getCorp().getPrimaryKey())) {
			sql += " and v.pk_corp = '" + _getCorp().getPrimaryKey() + "' ";
		}

		List<Map<String, Object>> maplit = (List<Map<String, Object>>) iQ
				.executeQuery(sql, new MapListProcessor());

		if (null != maplit && maplit.size() > 0) {
			Map<String, Map<String, Object>> ckmap = new HashMap<String, Map<String, Object>>();

			for (Map<String, Object> map : maplit) {

				String ctcodef = map.get("ctcode").toString();
				if (ckmap.containsKey(ctcodef)) {
					UFDate ufdateM = new UFDate(ckmap.get(ctcodef).get(
							"prepareddate").toString());
					UFDate ufdateR = new UFDate(map.get("prepareddate")
							.toString());
					if (ufdateR.after(ufdateM)) {
						ckmap.put(ctcodef, map);
					}
				} else {
					ckmap.put(ctcodef, map);
				}
			}

			UFDate nowdate = new UFDate();
			UFDate qxdate = nowdate.getDateBefore(beforeday);

			ctcklit = new ArrayList<CtCheckVO>();
			for (String strKey : ckmap.keySet()) {
				Map<String, Object> objmap = ckmap.get(strKey);

				UFDate ufdateM = new UFDate(objmap.get("prepareddate")
						.toString());
				if (ufdateM.before(qxdate)) {
					CtCheckVO cc = new CtCheckVO();
					for (String objKey : objmap.keySet()) {
						if ("syje".equals(objKey)) {
							UFDouble syjec = objmap.get(objKey) == null ? new UFDouble(
									"0.00")
									: new UFDouble(objmap.get(objKey)
											.toString());
							cc.setSyje(syjec);
						} else if ("yszk".equals(objKey)) {
							UFDouble yszkc = objmap.get(objKey) == null ? new UFDouble(
									"0.00")
									: new UFDouble(objmap.get(objKey)
											.toString());
							cc.setYszk(yszkc);
						} else if ("dctjetotal".equals(objKey)) {
							UFDouble dctjetotal = objmap.get(objKey) == null ? new UFDouble(
									"0.00")
									: new UFDouble(objmap.get(objKey)
											.toString());
							cc.setDctjetotal(dctjetotal);
						} else if ("htrq".equals(objKey)) {
							if (null != objmap.get(objKey)
									&& !"".equals(objmap.get(objKey))) {
								UFDate date = new UFDate(objmap.get(objKey)
										.toString());
								cc.setHtrq(date);
							}
						} else if ("djhdate".equals(objKey)) {
							if (null != objmap.get(objKey)
									&& !"".equals(objmap.get(objKey))) {
								UFDate date = new UFDate(objmap.get(objKey)
										.toString());
								cc.setDjhdate(date);
							}
						} else {
							cc.setAttributeValue(objKey, objmap.get(objKey));
						}
					}
					ctcklit.add(cc);
				}
			}

			getBufferData().clear();
			addDataToBuffer(ctcklit.toArray(new CtCheckVO[0]));
			updateBuffer();

		} else {
			getBufferData().clear();
			updateBuffer();
		}
	}

	@Override
	protected void onBoExport() throws Exception {
		BillModel headmodel = this.getBillListPanelWrapper().getBillListPanel()
				.getHeadBillModel();
		int checkrows = headmodel.getRowCount();
		if (checkrows <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "当前界面无数据导出");
			return;
		}

		String path = getChooseFilePath(this.getBillUI(), "收款时效预警");

		OutputStream os = new FileOutputStream(path + ".xls");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet();

		String[] lmstr = new String[] { "合同编号", "合同名称", "项目名称", "制单部门", "销售客户",
				"合同签约日期", "合同金额", "应收账款", "最后收款日期", "未付款金额" };

		HSSFRow lmrow = sheet.createRow(0);
		for (int i = 0; i < lmstr.length; i++) {
			HSSFCell lmcell = lmrow.createCell((short) i);
			lmcell.setCellValue(lmstr[i]);
		}

		// 明细
		UITable uitable = this.getBillListPanelWrapper().getBillListPanel()
				.getHeadTable();
		int rows = uitable.getRowCount();
		int cols = uitable.getColumnCount();

		for (int i = 0; i < rows; i++) {
			HSSFRow exlrow = sheet.createRow(i + 1);
			for (int j = 0; j < cols; j++) {
				HSSFCell cell = exlrow.createCell((short) j);
				Object uiobj = uitable.getValueAt(i, j);
				if (null != uiobj) {
					if (uiobj instanceof UFDouble) {
						cell.setCellValue(((UFDouble) uiobj).doubleValue());
					} else {
						cell
								.setCellValue(uiobj == null ? "" : uiobj
										.toString());
					}
				}
			}
		}

		// 合计
		DefaultTableModel ttable = headmodel.getTotalTableModel();

		HSSFRow exlrow = sheet.createRow(rows + 1);
		exlrow.createCell((short) 0).setCellValue("合计");

		for (int i = 1; i < lmstr.length; i++) {
			HSSFCell exlcell = exlrow.createCell((short) i);
			Object valueobj = ttable.getValueAt(0, i + 1);
			if (null != valueobj) {
				exlcell.setCellValue(new UFDouble(valueobj.toString())
						.doubleValue());
			}
		}

		hwb.write(os);
		os.close();

		MessageDialog.showHintDlg(this.getBillUI(), "提示", "数据导出完成");
	}

	/**
	 * 文件路径
	 */
	private String getChooseFilePath(Container parent, String defaultFileName) {
		// 新建一个文件选择框
		JFileChooser fileChooser = new JFileChooser();
		// 设置默认文件名
		fileChooser.setSelectedFile(new File(defaultFileName));
		// 打开保存框
		int retVal = fileChooser.showSaveDialog(parent);
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

	@Override
	public void onBillRef() throws Exception {

		if (null == ctcklit || ctcklit.size() < 1) {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请先查询数据");
			return;
		}

		SumDialog sd = new SumDialog(this.getBillUI());
		String[] retStrs = sd.showSumDialog();
		if (null != retStrs && retStrs.length > 0) {

			Map<String, List<CtCheckVO>> ctmap = new HashMap<String, List<CtCheckVO>>();

			Map<String, UFDouble> sumap = new HashMap<String, UFDouble>();// 存放小计合计金额

			for (CtCheckVO cvo : ctcklit) {
				String strKey = "";
				for (String retStr : retStrs) {
					if ("isctcode".equals(retStr)) {
						strKey += cvo.getCtcode();
					}
					if ("isctname".equals(retStr)) {
						strKey += cvo.getCtname();
					}
					if ("isxmname".equals(retStr)) {
						strKey += cvo.getXmname();
					}
					if ("isdept".equals(retStr)) {
						strKey += cvo.getDeptname();
					}
					if ("iscust".equals(retStr)) {
						strKey += cvo.getCustname();
					}
				}
				if (!ctmap.containsKey(strKey)) {
					List<CtCheckVO> lcc = new ArrayList<CtCheckVO>();
					lcc.add(cvo);
					ctmap.put(strKey, lcc);

					UFDouble xjmny = cvo.getSyje();// 设置小计合计
					sumap.put(strKey, xjmny);
				} else {
					List<CtCheckVO> lcc = ctmap.get(strKey);
					lcc.add(cvo);
					ctmap.put(strKey, lcc);

					UFDouble xjmny = sumap.get(strKey);// 设置小计合计
					xjmny = xjmny.add(cvo.getSyje());
					sumap.put(strKey, xjmny);
				}
			}

			// map键排序
			String[] sortMapKey = new String[ctmap.size()];// 排序后的key
			int sortMapNum = 0;
			for (String mapKey : ctmap.keySet()) {
				sortMapKey[sortMapNum] = mapKey;
				sortMapNum++;
			}
			Arrays.sort(sortMapKey);

			// 界面设值
			BillModel hmodel = this.getBillListPanelWrapper()
					.getBillListPanel().getHeadBillModel();
			hmodel.clearBodyData();

			int hrow = 0;
			List<Integer> colorlist = new ArrayList<Integer>();

			Map<Integer, String> inmap = new HashMap<Integer, String>();// 存放序列号
			// 初次放置界面(还需删除)
			for (int i = 0; i < sortMapKey.length; i++) {
				hmodel.addLine();
				hmodel.setValueAt(sumap.get(sortMapKey[i]), i, "syje");
				hmodel.setValueAt(i, i, "xj_no");
				inmap.put(i, sortMapKey[i]);
			}

			CtCheckVO[] hvos = (CtCheckVO[]) hmodel
					.getBodyValueVOs(CtCheckVO.class.getName());

			getBufferData().clear();
			addDataToBuffer(hvos);
			updateBuffer();

			hmodel.sortByColumn("syje", false);// 按照未付款金额降序

			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < hvos.length; i++) {
				Integer ct = (Integer) hmodel.getValueAt(i, "xj_no");
				list.add(ct);
			}

			hmodel.clearBodyData();
			
			//重新获得小计选择项
			boolean[] boostr=new boolean[5];
			for (String retStr : retStrs) {
				if ("isctcode".equals(retStr)) {
					boostr[0]=true;
				}
				if ("isctname".equals(retStr)) {
					boostr[1]=true;
				}
				if ("isxmname".equals(retStr)) {
					boostr[2]=true;
				}
				if ("isdept".equals(retStr)) {
					boostr[3]=true;
				}
				if ("iscust".equals(retStr)) {
					boostr[4]=true;
				}
			}

			//展示画面
			for (int i = 0; i < list.size(); i++) {
				String key = inmap.get(list.get(i));
				List<CtCheckVO> kccv = ctmap.get(key);
				String str1=kccv.get(0).getCtname();
				String str2=kccv.get(0).getXmname();
				String str3=kccv.get(0).getDeptname();
				String str4=kccv.get(0).getCustname();
				// 合计统计
				// UFDouble syjesum = new UFDouble("0.00");
				UFDouble yszksum = new UFDouble("0.00");
				UFDouble dcjtotalsum = new UFDouble("0.00");
				// 合同排序
				String[] ctcodeSort = new String[kccv.size()];
				for (int jt = 0; jt < kccv.size(); jt++) {
					ctcodeSort[jt] = kccv.get(jt).getCtcode();
				}
				Arrays.sort(ctcodeSort);

				for (String ctcodeStr : ctcodeSort) {
					for (int jp = 0; jp < kccv.size(); jp++) {
						CtCheckVO kcct = kccv.get(jp);
						if (ctcodeStr.equals(kcct.getCtcode())) {
							// syjesum = syjesum.add(kcct.getSyje());
							yszksum = yszksum.add(kcct.getYszk());
							dcjtotalsum = dcjtotalsum.add(kcct.getDctjetotal());
							hmodel.addLine();
							hmodel.setBodyRowVO(kcct, hrow);
							hrow++;
						}
					}
				}
				CtCheckVO ptp = new CtCheckVO();
				ptp.setCtcode("--小计--");
				ptp.setSyje(sumap.get(key));
				ptp.setYszk(yszksum);
				ptp.setDctjetotal(dcjtotalsum);
				ptp.setCtname(boostr[1]==true?str1:null);
				ptp.setXmname(boostr[2]==true?str2:null);
				ptp.setDeptname(boostr[3]==true?str3:null);
				ptp.setCustname(boostr[4]==true?str4:null);
				hmodel.addLine();
				hmodel.setBodyRowVO(ptp, hrow);
				colorlist.add(hrow);
				hrow++;
			}

			int[] colorrows = new int[colorlist.size()];
			for (int ci = 0; ci < colorlist.size(); ci++) {
				colorrows[ci] = colorlist.get(ci);
			}
			int[] colorcols = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

			hmodel.setBackground(Color.PINK, colorrows, colorcols);

			DefaultTableModel dtm = hmodel.getTotalTableModel();
			int cols = dtm.getColumnCount();
			for (int r = 0; r < cols; r++) {
				Object value = dtm.getValueAt(0, r);
				if (null != value && value instanceof UFDouble) {
					dtm.setValueAt(((UFDouble) value).div(new UFDouble(2), 2),
							0, r);
				}
			}
		}
	}
}
