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

	/** ͬ����Ʊ* */
	public static int TBFP = 9;

	/** ��Ʊ˰���޸�* */
	public static int SLXG = 10;

	// �������ݿ���ղ���ֵ
	/** ���۶���* */
	public static int N30 = 11;

	/** �ɹ�����* */
	public static int N21 = 12;

	/** ���۳��ⵥ* */
	public static int N12 = 16;

	/** �ɹ���ⵥ* */
	public static int N13 = 13;

	/** �ɹ���Ʊ* */
	public static int N25 = 14;

	/** ���ϳ���* */
	public static int N14 = 15;

	// ��������Դ����
	/** url��ַ* */
	// public static String URL= "jdbc:oracle:thin:@10.10.84.2:1521:orcl";
	/** �û���* */
	// public static String USERNAME="ncwz";
	/** ����* */
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

	/** �������̴�ӡ* */
	public static int SO_FLOW = 17;

	/** �ɹ����̴�ӡ* */
	public static int PU_FLOW = 18;

	/** ���ϳ����ӡ* */
	public static int CL_FLOW = 19;

	/** �㶫�ͻ�����ӡ* */
	public static int GD_FLOW = 20;

}
