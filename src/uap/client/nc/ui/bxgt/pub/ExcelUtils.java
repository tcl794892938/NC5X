package nc.ui.bxgt.pub;

import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.list.BillListUI;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;

public class ExcelUtils {
	
	/**
	 * 导入Excel
	 * 
	 * @param java.lang.Integer
	 *            sheetNum 查询的sheet
	 * @param java.lang.Integer
	 *            beginCellNum 从哪一列开始查
	 * @param java.lang.Integer
	 *            endCellNum 到哪一列结束
	 * @throws java.lang.Exception
	 * @return java.lang.Object[][] 返回二维数组
	 */
	public static Object[][] doImport(Container parent) throws Exception {
		// 选择文件
		String fileName = getChooseFileName(parent);
		// 判断传入的文件名是否为空
		if (StringUtils.isEmpty(fileName)) {
			// 如果为空，就不往下执行了
			return null;
		}
		
		if(!fileName.endsWith(".xls")){
			MessageDialog.showHintDlg(parent, "提示", "请使用Excel2003格式");
			return null;
		}
		
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
	
	/**
	 * 单表头导出excel方法
	 * @param defaultFileName
	 * @param list
	 * @param headColsCN
	 * @param beginIndex,开始列
	 * @param endIndex,结束列
	 * @param parent
	 * @throws Exception 
	 */
/*	public static void exportExcel(String defaultFileName, String[] headColsCN,int beginIndex,int endIndex, Container parent,BillListUI billUI) 
	                               throws Exception{
		BillItem[] items = null;
		int rownum = 0;
//		if(billUI.isListPanelSelected() == true) { // 是否从列表界面进入
			items =billUI.getBillListPanel().getHeadBillModel().getBodyItems();
			rownum = billUI.getBillListPanel().getHeadBillModel().getRowCount();
//		} else { // 从卡片界面获得AggVo
//			items = billUI.getBillCardPanel().getBodyItems();
//			rownum=billUI.getBillCardPanel().getRowCount();
//		}
		
		
		List<Object[]> lists = new ArrayList<Object[]>();
		for (int i = 0; i < rownum; i++)
		{
			Object[] objs = new Object[items.length];
			for (int j =beginIndex; j <endIndex; j++)
			{
				String name = items[j].getKey();
				objs[j-1] = billUI.getBillListPanel().getHeadBillModel().getValueAt(i, name);
			//	System.out.println(objs[j]+"----------");
			}
			lists.add(objs);
			
		}
		doExport(defaultFileName,lists,headColsCN,parent);
	}*/
	
	/**
	 * 
	 * @param defaultFileName
	 * @param obj  要导出的数据
	 * @param headColsCN
	 * @param parent
	 * @throws Exception
	 */
	/*public static void doExport(String defaultFileName, List list, String[] headColsCN, Container parent) throws Exception {
		// 用户选择路径
		String path = getChooseFilePath(parent, defaultFileName);
		// 判断传入的文件名是否为空
		if (StringUtils.isEmpty(path)) {
			// 如果为空，就不往下执行了
			return;

		}
		// 判断传入的文件名是否以.xls结尾
		if (!path.endsWith(".xls")) {
			// 如果不是以.xls结尾，就给文件名变量加上.xls扩展名
			path = path + ".xls";

		}

		// 构造一个输出流
		IOUtils util = new IOUtils(path, false, true);
		// 构造excel工具类对象
		ExcelTools excelTools = new ExcelTools();
		// 创建一个sheet
		excelTools.createSheet("导出数据");
		// 判断查询出的数据是否为空
		if (CollectionUtils.isEmpty(list)) {
			// 创建一行
			excelTools.createRow(0);
			// 创建一个单元格
			short ct = 0;
			excelTools.createCell(ct);
			// 如果为空，就直接向excel文件中写入“无数据！”
			excelTools.setValue("无数据！");

		} else {
			// 创建一行
			excelTools.createRow(0);
			// 判断列头是否为空
			if (null != headColsCN) {

				for (short i = 0; i < headColsCN.length; i++) {
					// 创建一个单元格
					excelTools.createCell(i);
					// 将值写到单元格
					excelTools.setValue(headColsCN[i]);

				}
				// 定义Object数组
				Object[] array = null;

				for (int i = 0; i < list.size(); i++) {
					// 迭代list
					array = (Object[]) list.get(i);

					if (null != array) {
						// 创建一行
						excelTools.createRow(i + 1);

						for (short j = 0; j < array.length; j++) {
							// 创建一个单元格
							excelTools.createCell(j);
							// 将值写到单元格
							excelTools.setValue(array[j]);

						}
						//array=null;

					}

				}

			}

		}
		// 将excel写到磁盘上
		excelTools.writeExcel(util.getOutputStream());
		// 闭关流
		util.closeStream();

	}*/
	
	public static void doExportExcel(Container parent,String fileName) throws Exception{
		
		//用户选择路径
		String path = getChooseFilePath(parent, fileName);
		// 判断传入的文件名是否为空
		if (StringUtils.isEmpty(path)) {
			// 如果为空，就不往下执行了
			return;

		}
		// 判断传入的文件名是否以.xls结尾
		if (!path.endsWith(".xls")) {
			// 如果不是以.xls结尾，就给文件名变量加上.xls扩展名
			path = path + ".xls";
		}

		//构造excel工具类对象
		OutputStream os=new FileOutputStream(path);
		ExcelTools excelTools = new ExcelTools();
		excelTools.createSheet("销售客户");
		excelTools.createRow(0);
		String[] colNames=new String[]{"客商名称","客商编码","助记码（可不填）","客商名称（可不填）","外文名称（可不填）"};
		for(short i=0;i<colNames.length;i++){
			excelTools.createCell(i);
			excelTools.setValue(colNames[i]);
		}
		excelTools.writeExcel(os);
		os.close();
	}
	
	/**
	 * 返回选择框中选择的路径
	 * @return java.lang.String
	 */
	public static String getChooseFilePath(Container parent, String defaultFileName) {
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
	
	
}
