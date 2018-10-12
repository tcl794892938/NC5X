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
 * Excel工具类，主要负责读取，写入excel
 * 
 * @author sun_jian
 * @version 1.0.0
 */
public class ExcelTools implements Cloneable {
	/**
	 * Excel对象
	 */
	private HSSFWorkbook workBook = null;

	/**
	 * Excel工作薄对象
	 */
	private HSSFSheet sheet = null;

	/**
	 * 工作薄中的行对象
	 */
	private HSSFRow row = null;

	/**
	 * 工作薄行中的列对象
	 */
	private HSSFCell cell = null;

	/**
	 * 构造函数，创建一个新的excel用
	 * 
	 */
	public ExcelTools() throws Exception {

		this.workBook = new HSSFWorkbook();

	}

	/**
	 * 构造函数，读取excel用
	 * 
	 * @param java.io.InputStream
	 *            is
	 * @throws java.lang.Exception
	 */
	public ExcelTools(InputStream is) throws Exception {

		this.workBook = new HSSFWorkbook(is);

	}

	/**
	 * 初始化行列对象，当sheet改变时，调用此方法
	 * 
	 */
	private void init() {

		this.row = null;

		this.cell = null;

	}

	/**
	 * 设置当前指定的工作薄
	 * 
	 * @param java.lang.Integer
	 *            i
	 */
	public void setSheet(int i) {

		if (null == this.workBook)

			throw new NullPointerException("workbook对象为空");

		this.sheet = this.workBook.getSheetAt(i);

		this.init();

	}

	/**
	 * 设置当前指定的工作薄
	 * 
	 * @param java.lang.String
	 *            sheetName
	 */
	public void setSheet(String sheetName) {

		if (null == this.workBook)

			throw new NullPointerException("workbook对象为空");

		this.sheet = this.workBook.getSheet(sheetName);

		this.init();

	}

	/**
	 * 设置指定工作薄的行对象
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
	 * 设置指定工作薄的行对象
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
	 * 设置当前工作薄的行对象
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
	 * 设置指定工作薄中指定行的列对象
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
	 * 设置指定工作薄中指定行的列对象
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
	 * 设置当前工作薄中指定行的列对象
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
	 * 设置当前工作薄中当前行的列对象
	 * 
	 * @param java.lang.Integer
	 *            cellNum
	 */
	public void setCell(short cellNum) {

		if (null != this.row)

			this.cell = this.row.getCell(cellNum);

	}

	/**
	 * 获取当前Excel中工作薄的数量
	 * 
	 * @return java.lang.Integer
	 */
	public int getNumberOfSheets() {

		return this.workBook.getNumberOfSheets();

	}

	/**
	 * 获取所有工作薄的行数总和
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
	 * 获取指定工作薄的行数总和
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
	 * 获取指定工作薄的行数总和
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
	 * 获取当前工作薄的行数总和
	 * 
	 * @return java.lang.Integer
	 */
	public int getRowCountOfSheet() {

		return null != this.sheet ? this.sheet.getLastRowNum() + 1 : 0;

	}

	/**
	 * 获取指定工作薄指定行的列数总和
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
	 * 获取指定工作薄指定行的列数总和
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
	 * 获取当前工作薄指定行的列数总和
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
	 * 获取当前工作薄当前行的列数总和
	 * 
	 * @return java.lang.Integer
	 */
	public short getCellCountofRow() {

		return null != this.row ? this.row.getLastCellNum() : 0;

	}

	/**
	 * 根据指定工作薄、指定行和指定列，获取单元格的值
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
	 * 根据指定工作薄、指定行和指定列，获取单元格的值
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
	 * 根据当前工作薄、指定行和指定列，获取单元格的值
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
	 * 根据当前工作薄、当前行和指定列，获取单元格的值
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
	 * 获取当前工作薄、当前行和指定列的单元格的值
	 * 
	 * @return java.lang.String
	 * @throws java.lang.Exception
	 */
	public String getValue() throws Exception {

		return null != this.cell ? this.getCellValue(this.cell) : "";

	}

	/**
	 * 根据传入参数，给指定工作薄、指定行和指定列的单元格赋值
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
	 * 根据传入参数，给指定工作薄、指定行和指定列的单元格赋值
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
	 * 根据传入参数，给当前工作薄、指定行和指定列的单元格赋值
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
	 * 根据传入参数，给当前工作薄、当前行和指定列的单元格赋值
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
	 * 根据传入参数，给当前工作薄、当前行和当前列的单元格赋值
	 * 
	 * @param java.lang.Object
	 *            value
	 */
	public void setValue(Object value) {

		if (null != this.cell)

			this.setCellValue(this.cell, value);

	}

	/**
	 * 判断excel单元格的格式
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
			 * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(cell)判断过程中cell
			 * .getNumericCellValue();方法会抛出java.lang.NumberFormatException异常
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
	 * 检测值是否为科学计数法
	 * 
	 * @param value
	 * @throws BusinessException
	 */
	private void checkDouble(String value) throws BusinessException {

		if (null != value && value.indexOf(".") != -1 && value.indexOf("E") != -1) {

			throw new BusinessException("Excel中不可以存在科学计数法！");

		}

	}

	/**
	 * 给不同类型的单元格赋值，支持String,Boolean,Date,Calendar,Double,RichTextString,ArrayList,Array
	 * 别的类型，可以强转，请强转
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
	 * 根据指定工作薄、指定行和指定列，获取单元格的样式
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
	 * 根据指定工作薄、指定行和指定列，获取单元格的样式
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
	 * 根据当前工作薄、指定行和指定列，获取单元格的样式
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
	 * 根据当前工作薄、当前行和指定列，获取单元格的样式
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
	 * 获取单元格的样式
	 * 
	 * @return org.apache.poi.ss.usermodel.CellStyle
	 */
	public HSSFCellStyle getCellStyle() {

		if (null == this.cell)

			throw new NullPointerException("获取单元格样式时，单元格对象为空");

		return this.cell.getCellStyle();

	}

	/**
	 * 根据指定工作薄、指定行和指定列，设置单元格的样式
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
	 * 根据指定工作薄、指定行和指定列，设置单元格的样式
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
	 * 根据当前工作薄、指定行和指定列，设置单元格的样式
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
	 * 根据当前工作薄、当前行和指定列，设置单元格的样式
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
	 * 设置单元格的样式
	 * 
	 * @param org.apache.poi.ss.usermodel.CellStyle
	 *            style
	 */
	public void setCellStyle(HSSFCellStyle style) {

		if (null == this.cell)

			throw new NullPointerException("设置单元格样式时，单元格对象为空");

		this.cell.setCellStyle(style);

	}

	/**
	 * 根据指定工作薄、指定行，获取行高度
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
	 * 根据指定工作薄、指定行，获取行高度
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
	 * 根据当前工作薄、指定行，获取行高度
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
	 * 根据当前工作薄、当前行，获取行高度
	 * 
	 * @param java.lang.Short
	 */
	public short getRowHeight() {

		if (null == this.row)

			throw new NullPointerException("获取行高度时，行对象为空");

		return this.row.getHeight();

	}

	/**
	 * 根据传入参数，给指定工作薄、指定行，设置行高
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
	 * 根据传入参数，给指定工作薄、指定行，设置行高
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
	 * 根据传入参数，给当前工作薄、指定行，设置行高
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
	 * 根据传入参数，给当前工作薄、当前行，设置行高
	 * 
	 * @param java.lang.Short
	 *            h
	 */
	public void setRowHeight(short h) {

		if (null == this.row)

			throw new NullPointerException("设置行高度时，行对象为空");

		this.row.setHeight(h);

	}

	/**
	 * 返回当选单元格
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFCell
	 * @throws java.lang.CloneNotSupportedException
	 */
	public HSSFCell getCell() throws CloneNotSupportedException {

		return this.clone().cell;

	}

	/**
	 * 返回当前行
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFRow
	 * @throws java.lang.CloneNotSupportedException
	 */
	public HSSFRow getRow() throws CloneNotSupportedException {

		return this.clone().row;

	}

	/**
	 * 返回当前工作薄
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFSheet
	 * @throws java.lang.CloneNotSupportedException
	 */
	public HSSFSheet getSheet() throws CloneNotSupportedException {

		return this.clone().sheet;

	}

	/**
	 * 返回Excel的Clone对象
	 * 
	 * @return org.apache.poi.xssf.usermodel.XSSFWorkbook
	 * @throws java.lang.CloneNotSupportedException
	 * 
	 */
	public HSSFWorkbook getWorkBook() throws CloneNotSupportedException {

		return this.clone().workBook;

	}

	/**
	 * 输出Excel
	 * 
	 * @param java.io.OutputStream
	 *            os
	 * @throws java.lang.Exception
	 */
	public void writeExcel(OutputStream os) throws Exception {

		if (null == this.workBook)

			throw new NullPointerException("workbook对象为空");

		this.workBook.write(os);

	}

	/**
	 * 输出Excel
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
	 * 创建一个工作薄
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
	 * 创建一行
	 * 
	 * @param java.lang.Integer
	 *            rowNum
	 */
	public void createRow(int rowNum) {

		this.init();

		this.row = this.sheet.createRow(rowNum);

	}

	/**
	 * 创建一个单元格
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