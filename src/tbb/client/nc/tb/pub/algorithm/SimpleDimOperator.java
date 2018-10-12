package nc.tb.pub.algorithm;
/**
 * by zqh2016-11-02 ���Ʒ�������
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import nc.tb.cube.DimVector;
import nc.tb.cube.ParaDimVector;
import nc.tb.cube.SheetDef;
import nc.tb.dim.DimDef;
import nc.tb.dim.DimMember;
import nc.tb.dim.FormulaMember;
import nc.tb.dim.data.DimManager;
import nc.tb.form.DimFormulaChain;
import nc.tb.form.FormulaParser;
import nc.tb.pub.IDimConst;
import nc.tb.pub.IDimPkConst;
import nc.tb.pub.exception.NotImplementException;
import nc.tb.pub.util.DimTimeTools;
import nc.tb.pub.util.NtbLogger;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;

public class SimpleDimOperator extends DimOperator {

	private String[] defCode = null;

	private String[] defPK = null;

	private String[] strValue = null;

	private HashSet[] hsValue = null;
	//����dimdef,���Ч�ʣ�tzj
	private DimDef[] dimDefs=null;
	//����SheetDef.getStoreIndex�����Ч�ʣ�tzj
	private int[] storeIdx=null;
	
	private DimFormulaChain m_dfChain = null;
//	public static void main(String[] args) {
//		try {
//			// NtbEnv.loadInvokeProxy();
//			String s = "001;";
//			StringTokenizer sToken = new StringTokenizer(s, ",");
//			ArrayList<String> ar = new ArrayList<String>();
//			while (sToken.hasMoreElements()) {
//				ar.add((String) sToken.nextElement());
//			}
//			if (ar.contains(FormulaMember.FakeDim_Code_Null))
//				System.out.print(true);
//			else
//				System.out.print(false);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public SimpleDimOperator(String originDimStr, SheetDef asd, DimFormulaChain dfChain) throws Exception {
		m_dfChain = dfChain;
		if(originDimStr.indexOf(FormulaMember.DYNAMIC_VIEW) >= 0){//�ж�̬��ͼ��Ա������ʱ��Ҫʵ����Ϊ���ά�ȳ�Ա
			this.originDimStr = FormulaParser.replaceDynView(originDimStr);
		}else{
			this.originDimStr = originDimStr;
		}
		this.sheetDef = asd;
		initialize(asd);
	}

	protected void initialize(SheetDef asd) throws Exception {

		StringTokenizer sToken = new StringTokenizer(originDimStr, ",");
		String sFormulaSeg;
		String sDefExp;
		String sValueExp;
		//String sOper = NtbBaseAlgorithm.EQUAL;
		int index = -1;
		int iBegin;
		int iEnd;
		String sDefcode;
		try {
			// aSD = asd;
			HashMap hmSimpleDimDef = new HashMap();
			while (sToken.hasMoreElements()) {
				sFormulaSeg = sToken.nextElement().toString();
				if (sFormulaSeg.equals(""))
					continue;
				index = sFormulaSeg.indexOf(NtbBaseAlgorithm.EQUAL);
				sDefExp = sFormulaSeg.substring(0, index);
				sDefExp = sDefExp.trim();
				sValueExp = sFormulaSeg.substring(index + 1, sFormulaSeg.length());
				sValueExp = sValueExp.trim();

				// ��ȡdimdef���
				iBegin = sDefExp.indexOf("[");
				iEnd = sDefExp.indexOf("]");
				sDefcode = sDefExp.substring(iBegin + 1, iEnd);

				// ��ȡdimmember���
				if (sValueExp.indexOf("[") == 0 && sValueExp.indexOf("]") == sValueExp.length() - 1)
					sValueExp = sValueExp.substring(1, sValueExp.length() - 1);
				if (!sValueExp.equals(FormulaMember.FakeDim_Code_All))
					hmSimpleDimDef.put(sDefcode, sValueExp);
				//mDimDefMap.put(sDefcode, sValueExp);
			}

			defCode = (String[]) hmSimpleDimDef.keySet().toArray(new String[0]);
			ComparaDef[] cds = new ComparaDef[defCode.length];
			for (int k = 0; k < defCode.length; k++) {
				String strDefPK = DimManager.getDimDefByCode(defCode[k]).getPrimaryKey();
				String memcodes = (String) hmSimpleDimDef.get(defCode[k]);
				Object oValue = null;
				if (memcodes.indexOf(";") > -1) {
					HashSet<String> hsTemp = new HashSet<String>();
					StringTokenizer sTokenParse = new StringTokenizer(memcodes, ";");
					while (sTokenParse.hasMoreElements()) {
						String sSegment = sTokenParse.nextElement().toString();
						hsTemp.add(sSegment);
					}
					oValue = hsTemp;
				} else
					oValue = memcodes;
				cds[k] = new ComparaDef(defCode[k], strDefPK, oValue, asd);
			}
			if (asd != null)
				Arrays.sort(cds, new DimDefComparator());
			defPK = new String[defCode.length];
			strValue = new String[defCode.length];
			hsValue = new HashSet[defCode.length];
			dimDefs=new DimDef[defCode.length];
			storeIdx=new int[defCode.length];
			
			for (int k = 0; k < defCode.length; k++) {
				defCode[k] = cds[k].getDefCode1();
				defPK[k] = cds[k].getDefPK2();
				Object o = cds[k].getOValue();
				if (o instanceof String)
					strValue[k] = (String) o;
				else
					hsValue[k] = (HashSet) o;
				dimDefs[k]=DimManager.getDimDefByPK(defPK[k]);
				storeIdx[k]=sheetDef.getStoreIndex(defPK[k]);
			}
		} catch (Exception e) {
			NtbLogger.error(e);
			String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("pf_pub", "SimpleDimOperator-000000")/*SimpleDimOperator��ʼ��ʧ�ܣ�γ�ȱ��ʽΪ��\n*/;
			msg += originDimStr;
			msg += NCLangRes4VoTransl.getNCLangRes().getStrByID("pf_pub", "SimpleDimOperator-000001", null, new String[]{e.getMessage()})/*����ԭ��\n{0}*/;
			// NtbLogger.print4(e.getMessage());
			throw new BusinessException(msg);
		}

	}

	// �ж�һ��ά�ȳ�Ա�Ƿ��ڱ�ʵ������ά�ȳ�Ա��Χ��
	public boolean isContainDimMember(DimMember dm) throws Exception {
		String memcode = (String) getFormulaSegMap().get(dm.getDimendef().getObjcode());
		if (memcode == null)
			return false;
		if (memcode.equals(FormulaMember.FakeDim_Code_All))
			return true;
		if (memcode.equals(FormulaMember.FakeDim_Code_NotNull)) {
			if (dm.getObjcode().equals(FormulaMember.FakeDim_Code_Null))
				return false;
			else
				return true;
		}
		return NtbBaseAlgorithm.isEqual(dm.getObjcode(), memcode);
	}

	public boolean isMatchDimVector(DimVector dv) throws Exception {
		for (int i = 0; i < defCode.length; i++) {
			
			if(dimDefs[i].getPrimaryKey().equals(IDimPkConst.PK_VERSION)){
				continue;
			}
			DimMember targetDm=dv.getDimMemberByDimDefAndSidx(dimDefs[i],storeIdx[i]);
			if (strValue[i] != null) {

				if (FormulaMember.FakeDim_Code_NotNull.equals(strValue[i])) {
					//begin_ncm_huangjz 201203271042238746 2012-03-31 ����15:16 �������Ʒ���ʱ������Ǹ������������?���⴦���������0��ֵ��Ԥ����
					//���ﲻƥ�両���еĵ�Ԫ������
					if(targetDm!=null&&targetDm.getObjcode().equals("?")){
						return false;
					}
					//end_ncm_huangjz 201203271042238746 2012-03-31 ����15:16 �������Ʒ���ʱ������Ǹ������������?���⴦���������0��ֵ��Ԥ����
					if (targetDm == null)
						return false;
					else
						continue;
				} else if (FormulaMember.FakeDim_Code_Null.equals(strValue[i])) {
					if (targetDm == null)
						continue;
					else
						return false;
				}else if (FormulaMember.Leaf.equals(strValue[i])) {
					if(targetDm != null && DimManager.isLeafNodeByDimMember(targetDm))
						continue;
					else
						return false;
				}else if (FormulaMember.NoneLeaf.equals(strValue[i])) {
					if(targetDm != null && !DimManager.isLeafNodeByDimMember(targetDm))
						continue;
					else
						return false;
				}else if (targetDm == null){
					return false;
				}else if(IDimConst.MONTHCODE.equals(defCode[i])){//��ά�ȵ����⴦����Ƶ��
					//��Ƶ����ά�ȵĴ���
					if(strValue[i].equals(IDimConst.CODE_F)||strValue[i].equals(IDimConst.CODE_S)||strValue[i].equals(IDimConst.CODE_T)){
						if(strValue[i].equals(IDimConst.CODE_F)){
							if(targetDm.getObjcode().equals("001")||targetDm.getObjcode().equals("004")||targetDm.getObjcode().equals("007")||targetDm.getObjcode().equals("010")){
								return true;
							}
						}else if(strValue[i].equals(IDimConst.CODE_S)){
							if(targetDm.getObjcode().equals("002")||targetDm.getObjcode().equals("005")||targetDm.getObjcode().equals("008")||targetDm.getObjcode().equals("011")){
								return true;
							}
						}else if(strValue[i].equals(IDimConst.CODE_T)){
							if(targetDm.getObjcode().equals("003")||targetDm.getObjcode().equals("006")||targetDm.getObjcode().equals("009")||targetDm.getObjcode().equals("012")){
								return true;
							}
						}
						return false;
					}
					//����Ƶ����ά�ȵĴ���
					else{
						if (!targetDm.getObjcode().equals(strValue[i]))
							return false;
					}
				}
				//������ά������ά�ȵĴ���
				else{
					if (!targetDm.getObjcode().equals(strValue[i]))
						return false;
				}
			} 
			//strValue[i]=null
			else {
				if (targetDm == null)
					return false;
				else {
					if(IDimConst.MONTHCODE.equals(defCode[i])){ //��ά�ȵ����⴦����Ƶ��
						//�Ƿ�����Ƶ��ά��
						if(hsValue[i].contains(IDimConst.CODE_F)||hsValue[i].contains(IDimConst.CODE_S)||hsValue[i].contains(IDimConst.CODE_T)){
							if(targetDm.getObjcode().equals("001")||targetDm.getObjcode().equals("004")||targetDm.getObjcode().equals("007")||targetDm.getObjcode().equals("010")){
								if(!hsValue[i].contains(IDimConst.CODE_F)){
									return false;
								}
							}else if(targetDm.getObjcode().equals("002")||targetDm.getObjcode().equals("005")||targetDm.getObjcode().equals("008")||targetDm.getObjcode().equals("011")){
								if(!hsValue[i].contains(IDimConst.CODE_S)){
									return false;
								}
							}else if(targetDm.getObjcode().equals("003")||targetDm.getObjcode().equals("006")||targetDm.getObjcode().equals("009")||targetDm.getObjcode().equals("012")){
								if(!hsValue[i].contains(IDimConst.CODE_T)){
									return false;
								}
							}
						}
						//��ͨ��ά�ȵĴ���
						else if (!hsValue[i].contains(targetDm.getObjcode()))
							return false;
					}
					//������ά������ά�ȵĴ���
					else if (!hsValue[i].contains(targetDm.getObjcode()))
						return false;
				}
			}
		}
		return true;
	}

	public boolean isMatchCommonDimVector(ParaDimVector cdv) throws Exception {
		for (int i = 0; i < defCode.length; i++) {
//			if (!sheetDef.isParaDimDef(DimManager.getDimDefByPK(defPK[i])))
//				continue;
//			DimMember targetDm = cdv.getDimMember(DimManager.getDimDefByPK(defPK[i]));
			if (!sheetDef.isParaDimDef(dimDefs[i]))
				continue;
			DimMember targetDm = cdv.getDimMemberByDimDef(DimManager.getDimDefByPK(defPK[i]));
			
			if (strValue[i] != null) {

				if (FormulaMember.FakeDim_Code_NotNull.equals(strValue[i])) {
					if (targetDm == null)
						return false;
					else
						continue;
				} else if (FormulaMember.FakeDim_Code_Null.equals(strValue[i])) {
					if (targetDm == null)
						continue;
					else
						return false;
				} else if (targetDm == null)
					return false;

				if (!targetDm.getObjcode().equals(strValue[i]))
					return false;
			} else {
				if (targetDm == null)
					return false;
				else {
					if (!hsValue[i].contains(targetDm.getObjcode()))
						return false;
				}
			}
		}
		return true;
	}

	public ArrayList<DimVector> getAllDimVectors(String cubecode) throws Exception {
		throw new NotImplementException("Not Implement!");
	}

	/**
	 * ��һ����򵥵�ά�ȱ��ʽ���������Ӧ������ά�ȳ�Ա�� �磺 [092]=[001;002] => һ�£�����
	 * 
	 * @param defcode
	 * @param memcodes
	 * @return
	 * @throws Exception
	 */
	private ArrayList<DimMember> getAllDimMember(String defcode, String memcodes) throws Exception {
		throw new NotImplementException("Not Implement!");
	}
	public boolean isEnable(){
		return m_dfChain!=null && m_dfChain.isEnable();
	}
	
	// lrx 2010-11-26 ��ֲV5.6�Ĺ�����ʽʵ��������ʱֻ֧�ֹ����º͹�����
	public void shiftRollingDimDefs(ParaDimVector pdv){
		for (int i = 0; i < defCode.length; i++) {
			DimDef ddRollDimDef = null;
			if("070".equals(defCode[i]) || "071".equals(defCode[i])){//������ + //������
				ddRollDimDef = dimDefs[i];
			}
			
			if (ddRollDimDef == null)
				continue;
			
			if (strValue[i] != null) {
				if (FormulaMember.FakeDim_Code_NotNull.equals(strValue[i])) {
						continue ;
				} else if (FormulaMember.FakeDim_Code_Null.equals(strValue[i])) {
						continue ;
				} else{
					strValue[i] = DimTimeTools.shiftRolledDim(strValue[i], ddRollDimDef, pdv);
				}
			} else {
				HashSet anoHsValue = new HashSet();
				for (Iterator iter = hsValue[i].iterator(); iter.hasNext();) {
					String element = (String) iter.next();
					String shiftedElement = DimTimeTools.shiftRolledDim(element, ddRollDimDef, pdv);
					if(shiftedElement != null)
						anoHsValue.add(shiftedElement);
					else
						anoHsValue.add(element);
				}
				hsValue[i] = anoHsValue;
			}
		}
	}
}
