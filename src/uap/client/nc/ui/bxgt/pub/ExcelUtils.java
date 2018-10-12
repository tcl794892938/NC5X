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
	 * ����Excel
	 * 
	 * @param java.lang.Integer
	 *            sheetNum ��ѯ��sheet
	 * @param java.lang.Integer
	 *            beginCellNum ����һ�п�ʼ��
	 * @param java.lang.Integer
	 *            endCellNum ����һ�н���
	 * @throws java.lang.Exception
	 * @return java.lang.Object[][] ���ض�ά����
	 */
	public static Object[][] doImport(Container parent) throws Exception {
		// ѡ���ļ�
		String fileName = getChooseFileName(parent);
		// �жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(fileName)) {
			// ���Ϊ�գ��Ͳ�����ִ����
			return null;
		}
		
		if(!fileName.endsWith(".xls")){
			MessageDialog.showHintDlg(parent, "��ʾ", "��ʹ��Excel2003��ʽ");
			return null;
		}
		
		// ����Object��ά����
		Object[][] values = null;
		// ������
		InputStream is = new FileInputStream(fileName);
		// ����excel���������
		ExcelTools tool = new ExcelTools(is);
		// �ر���
		is.close();
		// ȡ��ָ��sheet����
		int rowCount = tool.getRowCountOfSheet(0);
		// �ж������Ƿ����0
		if (rowCount > 0) {
			// ���õ�ǰ�е�1��
			tool.setRow(0);
			// ȡ��ָ���еĵ�Ԫ������
			short cellCount = tool.getCellCountofRow();
			// ��ʼ��Object��ά����
			values = new Object[rowCount][cellCount];

			for (int i = 0; i < rowCount; i++) {
				// ���õ�ǰ��
				tool.setRow(i);
				for (short j = 0; j < cellCount; j++) {
						values[i][j] = tool.getValueAt(j);
				}
			}
		}
		
		// ����
		return values;
	}
	
	/**
	 * �ļ�ѡȡ
	 * @return ѡȡ���ļ�·��
	 * */
	public static String getChooseFileName(Container parent) {
		// �½�һ���ļ�ѡ���
		JFileChooser fileChooser = new JFileChooser();
		// �򿪱����
		int retVal = fileChooser.showOpenDialog(parent);
		// ���巵�ر���
		String path = null;
		// �ж��Ƿ��
		if (retVal == JFileChooser.APPROVE_OPTION) {
			// ȷ�ϴ򿪣���ȡѡ���·��
			path = fileChooser.getSelectedFile().getPath();

		}
		// ����·��
		return path;

	}
	
	/**
	 * ����ͷ����excel����
	 * @param defaultFileName
	 * @param list
	 * @param headColsCN
	 * @param beginIndex,��ʼ��
	 * @param endIndex,������
	 * @param parent
	 * @throws Exception 
	 */
/*	public static void exportExcel(String defaultFileName, String[] headColsCN,int beginIndex,int endIndex, Container parent,BillListUI billUI) 
	                               throws Exception{
		BillItem[] items = null;
		int rownum = 0;
//		if(billUI.isListPanelSelected() == true) { // �Ƿ���б�������
			items =billUI.getBillListPanel().getHeadBillModel().getBodyItems();
			rownum = billUI.getBillListPanel().getHeadBillModel().getRowCount();
//		} else { // �ӿ�Ƭ������AggVo
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
	 * @param obj  Ҫ����������
	 * @param headColsCN
	 * @param parent
	 * @throws Exception
	 */
	/*public static void doExport(String defaultFileName, List list, String[] headColsCN, Container parent) throws Exception {
		// �û�ѡ��·��
		String path = getChooseFilePath(parent, defaultFileName);
		// �жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(path)) {
			// ���Ϊ�գ��Ͳ�����ִ����
			return;

		}
		// �жϴ�����ļ����Ƿ���.xls��β
		if (!path.endsWith(".xls")) {
			// ���������.xls��β���͸��ļ�����������.xls��չ��
			path = path + ".xls";

		}

		// ����һ�������
		IOUtils util = new IOUtils(path, false, true);
		// ����excel���������
		ExcelTools excelTools = new ExcelTools();
		// ����һ��sheet
		excelTools.createSheet("��������");
		// �жϲ�ѯ���������Ƿ�Ϊ��
		if (CollectionUtils.isEmpty(list)) {
			// ����һ��
			excelTools.createRow(0);
			// ����һ����Ԫ��
			short ct = 0;
			excelTools.createCell(ct);
			// ���Ϊ�գ���ֱ����excel�ļ���д�롰�����ݣ���
			excelTools.setValue("�����ݣ�");

		} else {
			// ����һ��
			excelTools.createRow(0);
			// �ж���ͷ�Ƿ�Ϊ��
			if (null != headColsCN) {

				for (short i = 0; i < headColsCN.length; i++) {
					// ����һ����Ԫ��
					excelTools.createCell(i);
					// ��ֵд����Ԫ��
					excelTools.setValue(headColsCN[i]);

				}
				// ����Object����
				Object[] array = null;

				for (int i = 0; i < list.size(); i++) {
					// ����list
					array = (Object[]) list.get(i);

					if (null != array) {
						// ����һ��
						excelTools.createRow(i + 1);

						for (short j = 0; j < array.length; j++) {
							// ����һ����Ԫ��
							excelTools.createCell(j);
							// ��ֵд����Ԫ��
							excelTools.setValue(array[j]);

						}
						//array=null;

					}

				}

			}

		}
		// ��excelд��������
		excelTools.writeExcel(util.getOutputStream());
		// �չ���
		util.closeStream();

	}*/
	
	public static void doExportExcel(Container parent,String fileName) throws Exception{
		
		//�û�ѡ��·��
		String path = getChooseFilePath(parent, fileName);
		// �жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(path)) {
			// ���Ϊ�գ��Ͳ�����ִ����
			return;

		}
		// �жϴ�����ļ����Ƿ���.xls��β
		if (!path.endsWith(".xls")) {
			// ���������.xls��β���͸��ļ�����������.xls��չ��
			path = path + ".xls";
		}

		//����excel���������
		OutputStream os=new FileOutputStream(path);
		ExcelTools excelTools = new ExcelTools();
		excelTools.createSheet("���ۿͻ�");
		excelTools.createRow(0);
		String[] colNames=new String[]{"��������","���̱���","�����루�ɲ��","�������ƣ��ɲ��","�������ƣ��ɲ��"};
		for(short i=0;i<colNames.length;i++){
			excelTools.createCell(i);
			excelTools.setValue(colNames[i]);
		}
		excelTools.writeExcel(os);
		os.close();
	}
	
	/**
	 * ����ѡ�����ѡ���·��
	 * @return java.lang.String
	 */
	public static String getChooseFilePath(Container parent, String defaultFileName) {
		// �½�һ���ļ�ѡ���
		JFileChooser fileChooser = new JFileChooser();
		// ����Ĭ���ļ���
		fileChooser.setSelectedFile(new File(defaultFileName));
		// �򿪱����
		int retVal = fileChooser.showSaveDialog(parent);
		// ���巵�ر���
		String path = null;
		// �ж��Ƿ��
		if (retVal == JFileChooser.APPROVE_OPTION) {
			// ȷ�ϴ򿪣���ȡѡ���·��
			path = fileChooser.getSelectedFile().getPath();

		}
		// ����·��
		return path;
	}
	
	
}
