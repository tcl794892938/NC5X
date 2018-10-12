package nc.vo.bxgt.menu;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BxgtStepButton {

	public static int SUO_DAN = 1;

	public static int JIE_SUO = 2;

	public static int QUE_REN = 3;

	public static int TONG_BU = 4;

	public static int QU_XIAO = 5;

	public static int OREDR_SEQ = 16;

	public static int CEL_ORDER = 17;

	public static String DESIGN_A = "design_z";

	public static String DESIGN_B = "";

	public static String DESIGN_C = "design_c";

	public static int CLCK = 6;

	public static int CKPG = 7;

	public static int RKPG = 8;

	/** 同步发票* */
	public static int TBFP = 9;

	/** 发票税率修改* */
	public static int SLXG = 10;

	// 设置数据库清空部分值
	/** 销售订单* */
	public static int N30 = 11;

	/** 采购订单* */
	public static int N21 = 12;

	/** 销售出库单* */
	public static int N12 = 16;

	/** 采购入库单* */
	public static int N13 = 13;

	/** 采购发票* */
	public static int N25 = 14;

	/** 材料出库* */
	public static int N14 = 15;

	// 设置数据源部分
	/** url地址* */
	// public static String URL= "jdbc:oracle:thin:@10.10.84.2:1521:orcl";
	/** 用户名* */
	// public static String USERNAME="ncwz";
	/** 密码* */
	// public static String PASSWORD="minyue";
	public static String[] getConnect() throws Exception {

		String path = BxgtStepButton.class.getResource("./").getFile();
		File file = new File(path + "user.xml");
		SAXReader saxr = new SAXReader();
		Document doc = saxr.read(file);
		Element root = doc.getRootElement();
		String[] str = new String[3];
		str[0] = root.element("url").getStringValue()
				+ root.element("ip").getStringValue()
				+ root.element("url2").getStringValue();
		str[1] = root.element("user").getStringValue();
		str[2] = root.element("password").getStringValue();
		return str;
	}

	/** 销售流程打印* */
	public static int SO_FLOW = 17;

	/** 采购流程打印* */
	public static int PU_FLOW = 18;

	/** 材料出库打印* */
	public static int CL_FLOW = 19;

	/** 广东客户金额打印* */
	public static int GD_FLOW = 20;

}
