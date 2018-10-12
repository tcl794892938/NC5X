package nc.ui.bxgt.pub;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nc.vo.pub.BusinessException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Excel�����࣬��Ҫ�����ȡ��д��excel
 * 
 * @author sun_jian
 * @version 1.0.0
 */
public class ExcelTools implements Cloneable {
	/**
	 * Excel����
	 */
	private HSSFWorkbook workBook = null;

	/**
	 * Excel����������
	 */
	private HSSFSheet sheet = null;

	/**
	 * �������е��ж���
	 */
	private HSSFRow row = null;

	/**
	 * ���������е��ж���
	 */
	private HSSFCell cell = null;

	/**
	 * ���캯��������һ���µ�excel��
	 * 
	 */
	public ExcelTools() throws Exception {

		this.workBook = new HSSFWorkbook();

	}

	/**
	 * ���캯������ȡexcel��
	 * 
	 * @param java.io.InputStream
	 *            is
	 * @throws java.lang.Exception
	 */
	public ExcelTools(InputStream is) throws Exception {

		this.workBook = new HSSFWorkbook(is);

	}

	/**
	 * ��ʼ�����ж��󣬵�sheet�ı�ʱ�����ô˷���
	 * 
	 */
	private void init() {

		this.row = null;

		this.cell = null;

	}

	/**
	 * ���õ�ǰָ���Ĺ�����
	 * 
	 * @param java.lang.Integer
	 *            i
	 */
	public void setSheet(int i) {

		if (null == this.workBook)

			throw new NullPointerException("workbook����Ϊ��");

		this.sheet = this.workBook.getSheetAt(i);

		this.init();

	}

	/**
	 * ���õ�ǰָ���Ĺ�����
	 * 
	 * @param java.lang.String
	 *            sheetName
	 */
	public void setSheet(String sheetName) {

		if (null == this.workBook)

			throw new NullPointerException("workbook����Ϊ��");

		this.sheet = this.workBook.getSheet(sheetName);

		this.init();

	}

	/**
	 * ����ָ�����������ж���
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 */
	public void setRow(int sheetNum, int rowNum) {

		this.setSheet(sheetNum);

		this.setRow(rowNum);

	}

	/**
	 * ����ָ�����������ж���
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 */
	public void setRow(String sheetName, int rowNum) {

		this.setSheet(sheetName);

		this.setRow(rowNum);

	}

	/**
	 * ���õ�ǰ���������ж���
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 */
	public void setRow(int rowNum) {

		if (null != this.sheet) {

			this.row = this.sheet.getRow(rowNum);

			this.cell = null;

		}

	}

	/**
	 * ����ָ����������ָ���е��ж���
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 */
	public void setCell(int sheetNum, int rowNum, short cellNum) {

		this.setRow(sheetNum, rowNum);

		this.setCell(cellNum);

	}

	/**
	 * ����ָ����������ָ���е��ж���
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 */
	public void setCell(String sheetName, int rowNum, short cellNum) {

		this.setRow(sheetName, rowNum);

		this.setCell(cellNum);

	}

	/**
	 * ���õ�ǰ��������ָ���е��ж���
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 */
	public void setCell(int rowNum, short cellNum) {

		this.setRow(rowNum);

		this.setCell(cellNum);

	}

	/**
	 * ���õ�ǰ�������е�ǰ�е��ж���
	 * 
	 * @param java.lang.Integer
	 *            cellNum
	 */
	public void setCell(short cellNum) {

		if (null != this.row)

			this.cell = this.row.getCell(cellNum);

	}

	/**
	 * ��ȡ��ǰExcel�й�����������
	 * 
	 * @return java.lang.Integer
	 */
	public int getNumberOfSheets() {

		return this.workBook.getNumberOfSheets();

	}

	/**
	 * ��ȡ���й������������ܺ�
	 * 
	 * @return java.lang.Integer
	 */
	public int getAllNumberOfRows() {

		int sheetNum = this.getNumberOfSheets();

		int rowNum = 0;

		for (int i = 0; i < sheetNum; i++) {

			this.setSheet(i);

			rowNum += this.sheet.getLastRowNum() + 1;

		}

		return rowNum;

	}

	/**
	 * ��ȡָ���������������ܺ�
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @return java.lang.Integer
	 */
	public int getRowCountOfSheet(int sheetNum) {

		this.setSheet(sheetNum);

		return getRowCountOfSheet();

	}

	/**
	 * ��ȡָ���������������ܺ�
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @return java.lang.Integer
	 */
	public int getRowCountOfSheet(String sheetName) {

		this.setSheet(sheetName);

		return getRowCountOfSheet();

	}

	/**
	 * ��ȡ��ǰ�������������ܺ�
	 * 
	 * @return java.lang.Integer
	 */
	public int getRowCountOfSheet() {

		return null != this.sheet ? this.sheet.getLastRowNum() + 1 : 0;

	}

	/**
	 * ��ȡָ��������ָ���е������ܺ�
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @return java.lang.Integer
	 */
	public int getCellCountOfRow(int sheetNum, int rowNum) {

		this.setRow(sheetNum, rowNum);

		return this.getCellCountofRow();

	}

	/**
	 * ��ȡָ��������ָ���е������ܺ�
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @return java.lang.Integer
	 */
	public int getCellCountOfRow(String sheetName, int rowNum) {

		this.setRow(sheetName, rowNum);

		return this.getCellCountofRow();

	}

	/**
	 * ��ȡ��ǰ������ָ���е������ܺ�
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @return java.lang.Integer
	 */
	public int getCellCountOfRow(int rowNum) {

		this.setRow(rowNum);

		return this.getCellCountofRow();

	}

	/**
	 * ��ȡ��ǰ��������ǰ�е������ܺ�
	 * 
	 * @return java.lang.Integer
	 */
	public short getCellCountofRow() {

		return null != this.row ? this.row.getLastCellNum() : 0;

	}

	/**
	 * ����ָ����������ָ���к�ָ���У���ȡ��Ԫ���ֵ
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @return java.lang.String
	 * @throws java.lang.Exception
	 */
	public String getValueAt(String sheetName, int rowNum, short cellNum) throws Exception {

		this.setCell(sheetName, rowNum, cellNum);

		return this.getValue();

	}

	/**
	 * ����ָ����������ָ���к�ָ���У���ȡ��Ԫ���ֵ
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @return java.lang.String
	 * @throws java.lang.Exception
	 */
	public String getValueAt(int sheetNum, int rowNum, short cellNum) throws Exception {

		this.setCell(sheetNum, rowNum, cellNum);

		return this.getValue();

	}

	/**
	 * ���ݵ�ǰ��������ָ���к�ָ���У���ȡ��Ԫ���ֵ
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @return java.lang.String
	 * @throws java.lang.Exception
	 */
	public String getValueAt(int rowNum, short cellNum) throws Exception {

		this.setCell(rowNum, cellNum);

		return this.getValue();

	}

	/**
	 * ���ݵ�ǰ����������ǰ�к�ָ���У���ȡ��Ԫ���ֵ
	 * 
	 * @param java.lang.Integer
	 *            cellNum
	 * @return java.lang.String
	 * @throws java.lang.Exception
	 */
	public String getValueAt(short cellNum) throws Exception {

		this.setCell(cellNum);

		return this.getValue();

	}

	/**
	 * ��ȡ��ǰ����������ǰ�к�ָ���еĵ�Ԫ���ֵ
	 * 
	 * @return java.lang.String
	 * @throws java.lang.Exception
	 */
	public String getValue() throws Exception {

		return null != this.cell ? this.getCellValue(this.cell) : "";

	}

	/**
	 * ���ݴ����������ָ����������ָ���к�ָ���еĵ�Ԫ��ֵ
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @param java.lang.Object
	 *            value
	 */
	public void setValue(String sheetName, int rowNum, short cellNum, Object value) {

		this.setCell(sheetName, rowNum, cellNum);

		this.setValue(value);

	}

	/**
	 * ���ݴ����������ָ����������ָ���к�ָ���еĵ�Ԫ��ֵ
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @param java.lang.Object
	 *            value
	 */
	public void setValue(int sheetNum, int rowNum, short cellNum, Object value) {

		this.setCell(sheetNum, rowNum, cellNum);

		this.setValue(value);

	}

	/**
	 * ���ݴ������������ǰ��������ָ���к�ָ���еĵ�Ԫ��ֵ
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @param java.lang.Object
	 *            value
	 */
	public void setValue(int rowNum, short cellNum, Object value) {

		this.setCell(rowNum, cellNum);

		this.setValue(value);

	}

	/**
	 * ���ݴ������������ǰ����������ǰ�к�ָ���еĵ�Ԫ��ֵ
	 * 
	 * @param java.lang.Integer
	 *            cellNum
	 * @param java.lang.Object
	 *            value
	 */
	public void setValue(short cellNum, Object value) {

		this.setCell(cellNum);

		this.setValue(value);

	}

	/**
	 * ���ݴ������������ǰ����������ǰ�к͵�ǰ�еĵ�Ԫ��ֵ
	 * 
	 * @param java.lang.Object
	 *            value
	 */
	public void setValue(Object value) {

		if (null != this.cell)

			this.setCellValue(this.cell, value);

	}

	/**
	 * �ж�excel��Ԫ��ĸ�ʽ
	 * 
	 * @param org.apache.poi.xssf.usermodel.XSSFCell
	 *            cell
	 * @return java.lang.String
	 * @throws BusinessException
	 */
	private String getCellValue(HSSFCell cell) throws BusinessException {

		String value = null;
		
		Date date = null;

		if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				date = cell.getDateCellValue();
				value = new SimpleDateFormat("yyyy-MM-dd").format(date);
//				value = (date.getYear() + 1900) + "-"
//						+ (date.getMonth() + 1) + "-" + date.getDate();
			}else{
				
				java.text.DecimalFormat formatter = new java.text.DecimalFormat("########.####");
				
				value = formatter.format(cell.getNumericCellValue());
				
				this.checkDouble(value);
				
				BigDecimal b = new BigDecimal(Double.valueOf(value));
				
				value = formatter.format(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}

		} else if (HSSFCell.CELL_TYPE_STRING == cell.getCellType()) {

			value = cell.getStringCellValue();

		} else if (HSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()) {

			value = String.valueOf(cell.getBooleanCellValue());

		} else if (HSSFCell.CELL_TYPE_BLANK == cell.getCellType()) {

			value = null;

		} else if (HSSFCell.CELL_TYPE_FORMULA == cell.getCellType()) {

			/*
			 * �˴��ж�ʹ�ù�ʽ���ɵ��ַ��������⣬��ΪHSSFDateUtil.isCellDateFormatted(cell)�жϹ�����cell
			 * .getNumericCellValue();�������׳�java.lang.NumberFormatException�쳣
			 */
			try {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					date = cell.getDateCellValue();
					value = new SimpleDateFormat("yyyy-MM-dd").format(date);
				} else {
					value = String.valueOf(cell.getNumericCellValue());
				}
			} catch (IllegalStateException e) {
				value = String.valueOf(cell.getRichStringCellValue());
			} catch(NumberFormatException e){
				value = String.valueOf(cell.getRichStringCellValue());
			}

//			value = cell.getCellFormula();
		} else if (HSSFCell.CELL_TYPE_ERROR == cell.getCellType()) {

			value = String.valueOf(cell.getErrorCellValue());

		} else {

			value = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());

		}

		this.checkDouble(value);

		return value;

	}

	/**
	 * ���ֵ�Ƿ�Ϊ��ѧ������
	 * 
	 * @param value
	 * @throws BusinessException
	 */
	private void checkDouble(String value) throws BusinessException {

		if (null != value && value.indexOf(".") != -1 && value.indexOf("E") != -1) {

			throw new BusinessException("Excel�в����Դ��ڿ�ѧ��������");

		}

	}

	/**
	 * ����ͬ���͵ĵ�Ԫ��ֵ��֧��String,Boolean,Date,Calendar,Double,RichTextString,ArrayList,Array
	 * ������ͣ�����ǿת����ǿת
	 * 
	 * @param org.apache.poi.xssf.usermodel.XSSFCell
	 *            cell
	 * @param java.lang.Object
	 *            value
	 */
	private void setCellValue(HSSFCell cell, Object value) {

		if (null != value) {

			if (value instanceof String) {

				cell.setCellValue((String) value);

			} else if (value instanceof Boolean) {

				cell.setCellValue((Boolean) value);

			} else if (value instanceof Date) {

				cell.setCellValue((Date) value);

			} else if (value instanceof Calendar) {

				cell.setCellValue((Calendar) value);

			} else if (value instanceof Double) {

				cell.setCellValue((Double) value);

			} else if (value instanceof BigDecimal) {

				cell.setCellValue(((BigDecimal) value).doubleValue());

			} else if (value instanceof Integer) {

				cell.setCellValue(Double.valueOf(value.toString()));

			} else if (value instanceof HSSFRichTextString){
				cell.setCellValue((HSSFRichTextString)value);
			} else {

				cell.setCellValue(value.toString());
			}

		}

	}

	/**
	 * ����ָ����������ָ���к�ָ���У���ȡ��Ԫ�����ʽ
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @return org.apache.poi.ss.usermodel.CellStyle
	 */
	public HSSFCellStyle getCellStyle(String sheetName, int rowNum, short cellNum) {

		this.setCell(sheetName, rowNum, cellNum);

		return this.getCellStyle();

	}

	/**
	 * ����ָ����������ָ���к�ָ���У���ȡ��Ԫ�����ʽ
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @return org.apache.poi.ss.usermodel.CellStyle
	 */
	public HSSFCellStyle getCellStyle(int sheetNum, int rowNum, short cellNum) {

		this.setCell(sheetNum, rowNum, cellNum);

		return this.getCellStyle();

	}

	/**
	 * ���ݵ�ǰ��������ָ���к�ָ���У���ȡ��Ԫ�����ʽ
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @return org.apache.poi.ss.usermodel.CellStyle
	 */
	public HSSFCellStyle getCellStyle(int rowNum, short cellNum) {

		this.setCell(rowNum, cellNum);

		return this.getCellStyle();

	}

	/**
	 * ���ݵ�ǰ����������ǰ�к�ָ���У���ȡ��Ԫ�����ʽ
	 * 
	 * @param java.lang.Integer
	 *            cellNum
	 * @return org.apache.poi.ss.usermodel.CellStyle
	 */
	public HSSFCellStyle getCellStyle(short cellNum) {

		this.setCell(cellNum);

		return this.getCellStyle();

	}

	/**
	 * ��ȡ��Ԫ�����ʽ
	 * 
	 * @return org.apache.poi.ss.usermodel.CellStyle
	 */
	public HSSFCellStyle getCellStyle() {

		if (null == this.cell)

			throw new NullPointerException("��ȡ��Ԫ����ʽʱ����Ԫ�����Ϊ��");

		return this.cell.getCellStyle();

	}

	/**
	 * ����ָ����������ָ���к�ָ���У����õ�Ԫ�����ʽ
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @param org.apache.poi.ss.usermodel.CellStyle
	 *            style
	 */
	public void setCellStyle(String sheetName, int rowNum, short cellNum, HSSFCellStyle style) {

		this.setCell(sheetName, rowNum, cellNum);

		this.setCellStyle(style);

	}

	/**
	 * ����ָ����������ָ���к�ָ���У����õ�Ԫ�����ʽ
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @param org.apache.poi.ss.usermodel.CellStyle
	 *            style
	 */
	public void setCellStyle(int sheetNum, int rowNum, short cellNum, HSSFCellStyle style) {

		this.setCell(sheetNum, rowNum, cellNum);

		this.setCellStyle(style);

	}

	/**
	 * ���ݵ�ǰ��������ָ���к�ָ���У����õ�Ԫ�����ʽ
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Integer
	 *            cellNum
	 * @param org.apache.poi.ss.usermodel.CellStyle
	 *            style
	 */
	public void setCellStyle(int rowNum, short cellNum, HSSFCellStyle style) {

		this.setCell(rowNum, cellNum);

		this.setCellStyle(style);

	}

	/**
	 * ���ݵ�ǰ����������ǰ�к�ָ���У����õ�Ԫ�����ʽ
	 * 
	 * @param java.lang.Integer
	 *            cellNum
	 * @param org.apache.poi.ss.usermodel.CellStyle
	 *            style
	 */
	public void setCellStyle(short cellNum, HSSFCellStyle style) {

		this.setCell(cellNum);

		this.setCellStyle(style);

	}

	/**
	 * ���õ�Ԫ�����ʽ
	 * 
	 * @param org.apache.poi.ss.usermodel.CellStyle
	 *            style
	 */
	public void setCellStyle(HSSFCellStyle style) {

		if (null == this.cell)

			throw new NullPointerException("���õ�Ԫ����ʽʱ����Ԫ�����Ϊ��");

		this.cell.setCellStyle(style);

	}

	/**
	 * ����ָ����������ָ���У���ȡ�и߶�
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Short
	 */
	public short getRowHeight(String sheetName, int rowNum) {

		this.setRow(sheetName, rowNum);

		return this.getRowHeight();

	}

	/**
	 * ����ָ����������ָ���У���ȡ�и߶�
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Short
	 */
	public short getRowHeight(int sheetNum, int rowNum) {

		this.setRow(sheetNum, rowNum);

		return this.getRowHeight();

	}

	/**
	 * ���ݵ�ǰ��������ָ���У���ȡ�и߶�
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Short
	 */
	public short getRowHeight(int rowNum) {

		this.setRow(rowNum);

		return this.getRowHeight();

	}

	/**
	 * ���ݵ�ǰ����������ǰ�У���ȡ�и߶�
	 * 
	 * @param java.lang.Short
	 */
	public short getRowHeight() {

		if (null == this.row)

			throw new NullPointerException("��ȡ�и߶�ʱ���ж���Ϊ��");

		return this.row.getHeight();

	}

	/**
	 * ���ݴ����������ָ����������ָ���У������и�
	 * 
	 * @param java.lang.String
	 *            sheetName
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Short
	 *            h
	 */
	public void setRowHeight(String sheetName, int rowNum, short h) {

		this.setRow(sheetName, rowNum);

		this.setRowHeight(h);

	}

	/**
	 * ���ݴ����������ָ����������ָ���У������и�
	 * 
	 * @param java.lang.Integer
	 *            sheetNum
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Short
	 *            h
	 */
	public void setRowHeight(int sheetNum, int rowNum, short h) {

		this.setRow(sheetNum, rowNum);

		this.setRowHeight(h);

	}

	/**
	 * ���ݴ������������ǰ��������ָ���У������и�
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 * @param java.lang.Short
	 *            h
	 */
	public void setRowHeight(int rowNum, short h) {

		this.setRow(rowNum);

		this.setRowHeight(h);

	}

	/**
	 * ���ݴ������������ǰ����������ǰ�У������и�
	 * 
	 * @param java.lang.Short
	 *            h
	 */
	public void setRowHeight(short h) {

		if (null == this.row)

			throw new NullPointerException("�����и߶�ʱ���ж���Ϊ��");

		this.row.setHeight(h);

	}

	/**
	 * ���ص�ѡ��Ԫ��
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFCell
	 * @throws java.lang.CloneNotSupportedException
	 */
	public HSSFCell getCell() throws CloneNotSupportedException {

		return this.clone().cell;

	}

	/**
	 * ���ص�ǰ��
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFRow
	 * @throws java.lang.CloneNotSupportedException
	 */
	public HSSFRow getRow() throws CloneNotSupportedException {

		return this.clone().row;

	}

	/**
	 * ���ص�ǰ������
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFSheet
	 * @throws java.lang.CloneNotSupportedException
	 */
	public HSSFSheet getSheet() throws CloneNotSupportedException {

		return this.clone().sheet;

	}

	/**
	 * ����Excel��Clone����
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFWorkbook
	 * @throws java.lang.CloneNotSupportedException
	 * 
	 */
	public HSSFWorkbook getWorkBook() throws CloneNotSupportedException {

		return this.clone().workBook;

	}

	/**
	 * ���Excel
	 * 
	 * @param java.io.OutputStream
	 *            os
	 * @throws java.lang.Exception
	 */
	public void writeExcel(OutputStream os) throws Exception {

		if (null == this.workBook)

			throw new NullPointerException("workbook����Ϊ��");

		this.workBook.write(os);

	}

	/**
	 * ���Excel
	 * 
	 * @param java.io.OutputStream
	 *            os
	 * @throws java.lang.CloneNotSupportedException
	 */
	public ExcelTools clone() throws CloneNotSupportedException {

		ExcelTools excelTools = (ExcelTools) super.clone();

		return excelTools;
	}

	/**
	 * ����һ��������
	 * 
	 * @param java.lang.String
	 *            sheetName
	 */
	public void createSheet(String sheetName) {

		this.init();

		this.sheet = this.workBook.createSheet();

		this.workBook.setSheetName(this.getNumberOfSheets() - 1, sheetName);

	}

	/**
	 * ����һ��
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 */
	public void createRow(int rowNum) {

		this.init();

		this.row = this.sheet.createRow(rowNum);

	}

	/**
	 * ����һ����Ԫ��
	 * 
	 * @param java.lang.Integer
	 *            cellNum
	 */
	public void createCell(short cellNum) {

		this.cell = this.row.createCell(cellNum);

	}

	public void closeWorkBook() {

	}

}