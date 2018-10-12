package nc.bs.ebank.out;

/**
 * 此处插入类型描述。 创建日期：(2004-4-8 14:36:27)
 * 
 * @author：钟悦
 */

// import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.ebank.log.LogBO;
import nc.bs.ebank.pub.BankTypeConstBO;
import nc.bs.ebank.pub.PublicBO;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.uap.lock.PKLock;
import nc.impl.ebank.BankTypeConstImpl;
import nc.impl.obm.ObmAPIImpl;
import nc.vo.ebank.exception.EbankBusinessException;
import nc.vo.ebank.exception.EbankBusinessRuntimeException;
import nc.vo.ebank.igen.IDGen;
import nc.vo.ebank.interfac3.BankTypeConst;
import nc.vo.ebank.interfac3.EbankAggVO;
import nc.vo.ebank.interfac3.EbankIOException;
import nc.vo.ebank.interfac3.EbankItemVO;
import nc.vo.ebank.interfac3.LogStateConst;
import nc.vo.ebank.interfac3.TranFlagConst;
import nc.vo.ebank.interfac3.XMLVO;
import nc.vo.ebank.interfac3.XmlAttrTool;
import nc.vo.ebank.log.LogVO;
import nc.vo.ebank.pub.BillListAndCardVO;
import nc.vo.ebank.pub.CheckToolVO;
import nc.vo.ebank.pub.EbankBuluReturnVO;
import nc.vo.ebank.pub.FuncTypeConst;
import nc.vo.ebank.pub.ToolVO;
import nc.vo.ebank.xml.HeadAttrConst;
import nc.vo.obm.BalanceItemConstant;
import nc.vo.obm.ObmItemConstant;
import nc.vo.obm.PayItemConstant;
import nc.vo.obm.exception.ObmBizException;
import nc.vo.obm.exception.ObmRuntimeException;
import nc.vo.obm.info.ObmBankInfo;
import nc.vo.obm.log.ObmLog;
import nc.vo.obm.ml.MLString;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class EbankEntranceBuLuBO {

	/**
	 * EbankEntrance 构造子注解。
	 */
	public EbankEntranceBuLuBO() {
		super();
	}

	/**
	 * 返回加锁用户pk
	 * 
	 * @param aggvo
	 * @return
	 * @author wangxy
	 * @since NC5.02
	 */
	private String getPk_user(EbankAggVO aggvo) {
		String pk_user = null;
		if (aggvo != null) {
			CircularlyAccessibleValueObject headvo = aggvo.getParentVO();
			if (headvo != null) {
				Object obj = headvo.getAttributeValue("submit_log_id");
				pk_user = obj == null ? null : obj.toString().trim();
			}
		}
		ObmLog.info(MLString.getString0023(pk_user), this.getClass(), "getPk_user");
		return pk_user;

	}

	private String[] getlockPks(EbankAggVO aggvo) {

		if (aggvo == null) {
			return null;
		}
		CircularlyAccessibleValueObject[] itemvos = aggvo.getChildrenVO();
		Vector<Object> vect = new Vector<Object>();
		if (itemvos != null) {
			for (int i = 0; i < itemvos.length; i++) {
				if (itemvos[i] != null) {
					Object obj = itemvos[i].getAttributeValue("yurref");
					if (obj != null && obj.toString().trim().length() > 0) {
						vect.add("EBANK_" + obj.toString().trim());
						// System.out.println("-------------------加锁pk:"
						// + obj.toString().trim());
					}
				}
			}
		} else {
			return null;
		}
		String[] keys = null;
		if (vect.size() > 0) {
			keys = new String[vect.size()];
			vect.copyInto(keys);
		}
		return keys;

	}

	public CircularlyAccessibleValueObject[] doMultiBankBusiness(
			CircularlyAccessibleValueObject[] cvo, String productcode) {
		//
		EbankAggVO[] eavos = PublicBO.getGroupByEbank(cvo);

		int len = eavos == null ? 0 : eavos.length;
		Vector<BillListAndCardVO> vec = new Vector<BillListAndCardVO>();
		BillListAndCardVO[] bvo = null;
		int len1 = 0;
		int len2 = -1;
		String[] str = null;
		BillListAndCardVO votemp = null;
		for (int i = 0; i < len; i++) {
			try {
				bvo = doBusinessSingle2(eavos[i], productcode);
				len1 = bvo == null ? 0 : bvo.length;
				for (int j = 0; j < len1; j++) {
					vec.add(bvo[i]);
				}
			} catch (Exception e) {
				HashMap<String, Serializable> map = new HashMap<String, Serializable>();
				map.put("tranflag", TranFlagConst.FAIL);
				map.put("errmsg", e.getMessage());
				map.put("exception", e);
				// evo=EbankAggVO.getRetVO((EbankHeadVO)eavos[i].getParentVO(),
				// eavos[i].getChildrenVO(),map);
				len1 = eavos[i].getChildrenVO() == null ? 0 : eavos[i].getChildrenVO().length;
				for (int j = 0; j < len1; j++) {
					str = eavos[i].getChildrenVO()[j].getAttributeNames();
					len2 = str == null ? 0 : str.length;
					votemp = new BillListAndCardVO();
					for (int k = 0; k < len2; k++) {

						votemp.setAttributeValue(str[k], eavos[i].getChildrenVO()[j].getAttributeValue(str[k]));
					}
					votemp.getHashMap().putAll(map);
					vec.addElement(votemp);
				}

			}

		}

		return (CircularlyAccessibleValueObject[]) vec.toArray(new CircularlyAccessibleValueObject[0]);
	}

	public BillListAndCardVO[] doBusinessSingle2(EbankAggVO vo, String productcode)
			throws BusinessException {

		BillListAndCardVO[] bvo = null;
		String func = getFunc(vo);
		boolean islock = false;
		String[] arrKeys = null;
		String userid = null;

		try {
			arrKeys = getlockPks(vo);
			userid = getPk_user(vo);
			Object bank = getBank(vo);

			islock = PKLock.getInstance().acquireBatchLock(arrKeys, userid, null);
			if (!islock) {
				// 此处应作修改，应该返回确切的失败的结果，不应抛出异常
				throw new ObmBizException(MLString.getErr0006());
			}
			// 取业务类型
			ObmLog.info("网银补录：业务类型func=[" + func + "]/银行类别bank=[" + bank + "]", this.getClass(), "doBusinessSingle2");

			ArrayList<BillListAndCardVO> itemvoFromLog = null;

			itemvoFromLog = filterSubVO(vo);

			if (itemvoFromLog != null && itemvoFromLog.size() > 0) {
				// retvo.getAllVOs().addAll(itemvoFromLog);
				bvo = (BillListAndCardVO[]) itemvoFromLog.toArray(new BillListAndCardVO[0]);

				// isAddedLog=true;
			}

		} catch (BusinessException re) {
			throwEbankIOException(re);
		} catch (Exception e) {
			Logger.error("", e);
			throw new EbankBusinessException("", e);
		} finally {
			if (islock == true && arrKeys != null) {
				PKLock.getInstance().releaseBatchLock(arrKeys, userid, null);
			}
		}

		return bvo;
	}

	private void createPayRetVO(ArrayList<BillListAndCardVO> itemVOFromLog, int tranflag,
			String yurref, Object packageid, BillListAndCardVO temp) {
		BillListAndCardVO itemvo = new BillListAndCardVO();
		if (temp != null) {
			itemvo.getHashMap().putAll(temp.getHashMap());
		}
		if (tranflag == 2) {// 多语言问题
			itemvo.setAttributeValue("errmsg", NCLangResOnserver.getInstance().getStrByID("3612", "UPP3612-000040")/*
																													 * @res
																													 * "本张单据有可以支付成功，请立即与银行确认。本次支付不向银行提交。"
																													 */);
			itemvo.setAttributeValue("retcode", "EBANK_02");
		}
		if (tranflag == 20) {// tranflag==20,第一次不明//多语言问题
			tranflag = 2;
			itemvo.setAttributeValue("errmsg", NCLangResOnserver.getInstance().getStrByID("3612", "UPP3612-000041")/*
																													 * @res
																													 * "因为网络故障，银行在网银配置的延时时间内没有返回支付结果。本次支付状态不明。"
																													 */);
			itemvo.setAttributeValue("retcode", "EBANK_20");
		}
		if (tranflag == 10) {// 多语言问题
			tranflag = 1;
			itemvo.setAttributeValue("errmsg", NCLangResOnserver.getInstance().getStrByID("3612", "UPP3612-000042")/*
																													 * @res
																													 * "支付失败，本次支付指令未能发送到银行。请检查网银配置。"
																													 */);
			itemvo.setAttributeValue("retcode", "EBANK_10");
		}
		itemvo.setAttributeValue("tranflag", new Integer(tranflag));
		itemvo.setAttributeValue("yurref", yurref);
		itemvo.setAttributeValue("packageid", packageid);

		itemVOFromLog.add(itemvo);
	}

	/**
	 * public static final String SUCC = "0"; public static final String FAIL =
	 * "1";
	 * 
	 * //从指令日志返回的转帐成功信息，表示该业务参考号的转帐上次已经转帐成功 public static final String SUCC2 =
	 * "2"; //业务组调用网银接口时，如果设定TRANFLAG字段的值为TranFlag.ZANCUN,则表示该条记录暂存在bank_log表
	 * public static final String ZANCUN = "3";
	 * 
	 * 返回从LogVO中得到的已经从银行返回的vo ArrayList，向银行提交新的vo 创建日期：(2004-4-8 14:37:09)
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<BillListAndCardVO> filterSubVO(EbankAggVO vo) throws Exception {
		// 取业务类型
		String func = getFunc(vo);
		if (func != null
				&& !func.trim().equals(FuncTypeConst.ZF)
				&& !func.trim().equals(FuncTypeConst.JTZF)
				&& !func.trim().equals(FuncTypeConst.JTGJ)
				&& !func.trim().equals(FuncTypeConst.DLZF)) {
			return null;
		}

		// 查询指令日志
		CircularlyAccessibleValueObject[] itemvos = (CircularlyAccessibleValueObject[]) vo.getChildrenVO();
		// 在指令日志里返回的vo
		ArrayList itemVOFromLog = new ArrayList();
		if (itemvos == null) {
			throw new ObmBizException(MLString.getErr0008());
		}
		LogBO logbo = new LogBO();
		for (int i = 0; i < itemvos.length; i++) {
			if (itemvos[i] == null) {
				continue;
			}
			String yurref = (String) itemvos[i].getAttributeValue("yurref");
			if (itemvos[i] instanceof BillListAndCardVO) {
				EbankItemVO itemvo = new EbankItemVO();
				itemvo.getHashMap().putAll(((BillListAndCardVO) itemvos[i]).getHashMap());
				itemvo.setAttributeValue("errmsg", MLString.getString0019());

				LogVO logvo = itemvo.changeToLogVO();
				logvo.setState(new Integer("3"));
				logbo.deleteByWhere(" yurref = '" + logvo.getYurref() + "' and state = 3 ");
				logbo.insert(logvo);
				int tranflag_int = 0;
				String packageID = IDGen.getInst().getPackageID();
				createPayRetVO(itemVOFromLog, tranflag_int, yurref, packageID, itemvo);
			}

		}

		return itemVOFromLog;
	}

	/**
	 * @param itemVOFromLog
	 * @param logVO
	 */
	// private void createPayRetVO(java.util.ArrayList itemVOFromLog,int
	// tranflag, String yurref) {
	// PayRetVOImplement itemvo = new PayRetVOImplement();
	// itemvo.setAttributeValue("tranflag", new Integer(tranflag));
	// itemvo.setAttributeValue("yurref", yurref);
	// itemVOFromLog.add(itemvo);
	// }
	/**
	 * 此处插入方法描述。 创建日期：(2004-4-8 14:37:09)
	 */
	private String getFunc(EbankAggVO vo) {
		CircularlyAccessibleValueObject headvo = vo.getParentVO();
		Object obj_func = headvo.getAttributeValue(HeadAttrConst.FUNC);
		if (obj_func == null) {
			ObmLog.warn("错误。vo应该实现func()方法。", this.getClass(), "getFunc");
			return null;
		}
		return obj_func.toString();
	}

	private String getBank(nc.vo.ebank.interfac3.EbankAggVO vo) throws Exception {
		nc.vo.pub.CircularlyAccessibleValueObject headvo = vo.getParentVO();
		Object obj_bank = headvo.getAttributeValue(nc.vo.ebank.xml.HeadAttrConst.BANK);
		if (obj_bank == null) {
			// System.out.println("错误。vo应该实现getBank()方法。");
			return null;
		}
		return obj_bank.toString();
	}

	/**
	 * public static final int TJ_SUCC = 0; //提交成功,等待银行处理。 public static final
	 * int SQ_SUCC = 1; //授权成功，等待银行处理。目前无此情况。 public static final int SQ_WAIT =
	 * 2; //等待授权。目前无此情况 public static final int SQ2_WAIT = 3; //等待二次授权。目前无此情况。
	 * public static final int DF_WAIT = 4; //等待银行答复。该笔转帐交易落地。应联系付款方开户行。 public
	 * static final int CS_FAIL = 5; //超期作废。目前无此情况。 public static final int
	 * TJ_FAIL = 6; //被银行拒绝。拒绝原因见Bank_Rem字段。 public static final int ZF_SUCC =
	 * 7; //处理成功。 public static final int SQ_FAIL = 8; //指令被拒绝授权。目前无此情况。 public
	 * static final int DOING = 9; //银行正在处理。该笔转帐交易可疑。应联系付款方开户行 public static
	 * final int TIMEOUT = -1;//超时，没有得到回应 public static final int INIT = 100;
	 * 初始状态 创建日期：(2004-4-8 14:37:09)
	 */
	public nc.vo.ebank.log.LogVO[] insertToLog(nc.vo.ebank.interfac3.EbankAggVO vo)
			throws BusinessException {
		if (vo == null || vo.getChildrenVO() == null || vo.getChildrenVO().length == 0) {
			return null;
		}
		CircularlyAccessibleValueObject headvo = vo.getParentVO();
		if (headvo == null
				|| !headvo.getAttributeValue(HeadAttrConst.FUNC).equals(FuncTypeConst.ZF)) {
			return null;
		}
		nc.vo.ebank.log.LogVO[] logvos = null;
		try {

			// 传到银行之前,先把传到银行的新指令插入到指令日志
			nc.vo.pub.CircularlyAccessibleValueObject[] itemvos = vo.getChildrenVO();
			logvos = new nc.vo.ebank.log.LogVO[itemvos.length];
			for (int i = 0; i < itemvos.length; i++) {
				logvos[i] = ((EbankItemVO) itemvos[i]).changeToLogVO();
				logvos[i].setState(new Integer(LogStateConst.INIT));
			}
			nc.bs.ebank.log.LogBO logbo = new nc.bs.ebank.log.LogBO();
			String[] keys = logbo.insertArray(logvos);

			for (int i = 0; i < logvos.length; i++) {
				logvos[i].setPk_ebank_log(keys[i]);
			}

		} catch (BusinessException re) {
			throwEbankIOException(re);
		} catch (Exception e) {
			Logger.error("", e);
			throw new EbankBusinessException(e.getMessage());
		}
		return logvos;

	}

	/**
	 * public static final int TJ_SUCC = 0; //提交成功,等待银行处理。 public static final
	 * int SQ_SUCC = 1; //授权成功，等待银行处理。目前无此情况。 public static final int SQ_WAIT =
	 * 2; //等待授权。目前无此情况 public static final int SQ2_WAIT = 3; //等待二次授权。目前无此情况。
	 * public static final int DF_WAIT = 4; //等待银行答复。该笔转帐交易落地。应联系付款方开户行。 public
	 * static final int CS_FAIL = 5; //超期作废。目前无此情况。 public static final int
	 * TJ_FAIL = 6; //被银行拒绝。拒绝原因见Bank_Rem字段。 public static final int ZF_SUCC =
	 * 7; //处理成功。 public static final int SQ_FAIL = 8; //指令被拒绝授权。目前无此情况。 public
	 * static final int DOING = 9; //银行正在处理。该笔转帐交易可疑。应联系付款方开户行 public static
	 * final int TIMEOUT = -1;//超时，没有得到回应 public static final int INIT = 100;
	 * 初始状态 创建日期：(2004-4-8 14:37:09)
	 */
	private void throwEbankIOException(BusinessException re) throws BusinessException {

		Throwable ex = re;
		// ex.printStackTrace();
		if (ex instanceof java.net.NoRouteToHostException /* ip地址错或网络断 */
				|| ex instanceof java.net.ConnectException /* 端口错 */
				|| ex instanceof java.io.FileNotFoundException /* 服务路径错 */
				|| ex instanceof java.io.IOException) { /**/
			EbankIOException ebankioe = new EbankIOException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("36120101", "UPP36120101-000005", null, new String[] { ex.getMessage() })/*
																																				 * @res
																																				 * "网络联接错误:"
																																				 */
			);
			throw new EbankIOException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("36120101", "UPP36120101-000006")/*
																										 * @res
																										 * "网络错误"
																										 */,
					ebankioe);
		} else {
			throw new EbankIOException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("36120101", "UPP36120101-000006")/*
																										 * @res
																										 * "网络错误"
																										 */,
					re);
			// throw re;
		}

	}
	
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * @author zengj
	 * @throws BusinessException 
	 * @since NC5.6
	 */
	public EbankBuluReturnVO dealEbankentranceBuluData(BillListAndCardVO[] bvo,String productcode,String pk_corp) throws BusinessException {
		
		bvo = fillExtInfo(bvo);
		// 检查必要信息是否完整
		String errmsg = ToolVO.checkZf(bvo);
		if (errmsg != null && errmsg.length() > 0) {
			throw new ObmRuntimeException(errmsg);
		}
		// 将NC的banktype转换为网银适配器需要的bank
		bvo = (BillListAndCardVO[]) BankTypeConstBO.setStrBankType(bvo, "banktype", HeadAttrConst.BANK);

		// 得到网银适配器需要的bank
		String bank = getBankFromVO(bvo);
		
		boolean isBankAuthorization = checkIsBankAuthorization(bank);
		if (!isBankAuthorization) {
			String msg = "银行标识为["+bank+"]的银行没有授权！不能进行该业务操作！";
			ObmLog.error(msg, this.getClass(), "dealEbankentranceBuluData");
			throw new ObmBizException(msg);
		}
		
		String func = (String) bvo[0].getAttributeValue(HeadAttrConst.FUNC);

		func = changeFunc(bvo);

		if (FuncTypeConst.DFDK.equals(func)) {
			for (int i = 0; i < bvo.length; i++) {
				bvo[i].setAttributeValue(PayItemConstant.SUMMNY, bvo[i].getAttributeValue(ObmItemConstant.SUM));
			}
		}

		// 读取配置文件
		XMLVO xvo = getHashMaps(productcode, bank, func);
		// 检查配置文件中数据来源为bill(单据)的数据项是否都有值
		errmsg = ToolVO.check(bvo, xvo);
		if (errmsg != null && errmsg.length() > 0) {
			ObmLog.error(errmsg, this.getClass(), "dealEbankentranceBuluData");
			throw new ObmBizException(errmsg);
		}

		bvo = fillFields(bvo, func, productcode, xvo,pk_corp);
		//by tcl 2016-12-09
		if(bvo!=null&&bvo.length>0){
			for(BillListAndCardVO vo : bvo){
				if(vo.getAttributeValue("transfer_type")==null){
					vo.setAttributeValue("transfer_type", 0);
				}
			}
		}

		// 检查付款方限额
		errmsg = checkDbQuota(bvo);
		if (errmsg != null && errmsg.length() > 0) {
			errmsg = errmsg + ",请修改业务单据的支付金额";
			ObmLog.error(errmsg, this.getClass(), "dealEbankentranceBuluData");
			throw new ObmBizException(errmsg);
		}
		errmsg = checkRmb(bvo, true);
		if (errmsg != null && errmsg.length() > 0) {
			throw new ObmBizException(errmsg);
		}
		ToolVO.setDefaultValue(xvo, bvo);
		
		EbankBuluReturnVO returnvo = new EbankBuluReturnVO();
		returnvo.setBvo(bvo);
		returnvo.setXmlvo(xvo);
		return returnvo;
	}
	
	/**
	 * 
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 增加一些附加信息。 该方法的附加信息主要是防止补录时从单据取值，但单据上没有该字段值的情况。
	 * 目前只增加了收方名称(crtnam)、收方开户行(crtbnk)的设置。
	 *  @author zengj
	 * @param bvo
	 * @return
	 * @throws ObmBizException
	 */
	private BillListAndCardVO[] fillExtInfo(BillListAndCardVO[] bvo) throws ObmBizException {
//		String CRTBNK = "crtbnk"; // 收方开户行
//		String CRTNAM = "crtnam"; // 收方名称
		String CRTACC = "crtacc"; // 收方账号
		BillListAndCardVO crtacc_vo;

		String crtbnk_val, crtnam_val, crtacc_val;
		ObmAPIImpl obmapi = new ObmAPIImpl();
		for (int i = 0; i < bvo.length; i++) {
			crtacc_val = bvo[i].getString(CRTACC);
			crtacc_vo = obmapi.getBankaccVO(bvo[i].getString(ObmItemConstant.PK_CRTACC));

			crtbnk_val = bvo[i].getString(PayItemConstant.CRTBNK);
			crtnam_val = bvo[i].getString(PayItemConstant.CRTNAM);
			if (crtacc_vo != null) {
				if (crtbnk_val == null || crtbnk_val.trim().length() == 0) {
					crtbnk_val = crtacc_vo.getString(PayItemConstant.BANKNAME);

					bvo[i].setAttributeValue(PayItemConstant.CRTBNK, crtbnk_val);
				}
				if (crtnam_val == null || crtnam_val.trim().length() == 0) {
					crtnam_val = crtacc_vo.getString(PayItemConstant.UNITNAME);

					bvo[i].setAttributeValue(PayItemConstant.CRTNAM, crtnam_val);
				}
			}
		}
		return bvo;
	}
	
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 取得银行类别信息,并校验付方账号是否为同行
	 * 
	 * @param bvo
	 *            BillListAndCardVO[]
	 * @return
	 * @throws ObmBizException
	 * @author zengj
	 * @since NC5.6
	 */
	private String getBankFromVO(BillListAndCardVO[] bvo) throws ObmBizException {
		String bank = (String) bvo[0].getAttributeValue(HeadAttrConst.BANK);
		String subbank = null;
		for (int i = 0; i < bvo.length; i++) {
			subbank = (String) bvo[i].getAttributeValue(HeadAttrConst.BANK);
			if (bank == null || subbank == null) {
				throw new ObmBizException(MLString.getErr0062());
			} else {
				if (!bank.equals(subbank)) {
					String errmsg = MLString.getErr0063();
					throw new ObmBizException(errmsg);
				}
			}
		}
		return bank;
	}
	
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 由指令字符串解析出银行唯一标识，并判断该银行是否有授权
	 * @param strnet
	 * @return
	 * @author zengj
	 * @since NC5.6
	 */
	private boolean checkIsBankAuthorization(String bank) {
		if (bank.startsWith("rbrs_")) {
			bank = BankTypeConst.RBRS;
		}
		
		ObmAPIImpl obmapiimpl = new ObmAPIImpl();
		boolean isBankAuthorization = obmapiimpl.checkIsBankAuthorization(bank);
		return isBankAuthorization;
	}
	
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 根据banktype.xml配置文件设置，改变支付类型
	 * 
	 * @param bvo
	 *            BillListAndCardVO[]
	 * @return
	 * @author zengj
	 * @throws ObmBizException
	 * @since NC5.6
	 */
	private String changeFunc(BillListAndCardVO[] bvo) throws ObmBizException {
		String bank = bvo[0].getString(HeadAttrConst.BANK);
		String func = bvo[0].getString(HeadAttrConst.FUNC);
		HashMap bankTypeCfg = getBankTypeConfig(bank);
		ObmLog.debug("前台补录单据func=" + func, this.getClass(), "changeFunc(BillListAndCardVO[] bvo)");
		ObmLog.debug("前台补录单据bank=" + bank, this.getClass(), "changeFunc(BillListAndCardVO[] bvo)");

		if (FuncTypeConst.JTGJ.equals(func)) {
			// 判断银行是否支持集团归集
			String jtgj = (String) bankTypeCfg.get("jtgj");
			if ("N".equals(jtgj)) {
				ObmLog.debug("银行接口bank=" + bank + "不支持集团归集指令", this.getClass(), "changeFunc(BillListAndCardVO[] bvo)");
				func = FuncTypeConst.ZF;
			} else {
				func = canUseJTGJ(bvo);
			}
		} else if (FuncTypeConst.JTZF.equals(func)) {
			// 判断是否支持集团支付
			String jtzf = (String) bankTypeCfg.get("jtzf");
			if ("N".equals(jtzf)) {
				ObmLog.debug("银行接口bank=" + bank + "不支持集团支付指令", this.getClass(), "changeFunc(BillListAndCardVO[] bvo)");
				func = FuncTypeConst.ZF;
			}

		}
		for (int i = 0; i < bvo.length; i++) {
			bvo[i].setAttributeValue(HeadAttrConst.FUNC, func);
		}
		ObmLog.debug("前台补录使用func=" + func, this.getClass(), "changeFunc(BillListAndCardVO[] bvo)");
		return func;
	}
	
	/**
	 * 
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 对应网银类型的配置信息
	 * 
	 * @param bank
	 *            网银类别
	 * @return
	 * @author zengj
	 * @since NC5.6
	 */
	private HashMap getBankTypeConfig(String bank) {
		HashMap[] bankTypeCfg = new ObmBankInfo().getBanktypeFile();
		HashMap cfg = null;
		for (int i = 0; i < bankTypeCfg.length; i++) {
			if (bank.equals((String) bankTypeCfg[i].get("sbanktype"))) {
				cfg = bankTypeCfg[i];
			}
		}
		return cfg;
	}
	
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 是否可以使用集团归集指令。 收付方账号为相同银行的单据可以使用集团归集指令
	 * 
	 * @param bvo
	 *            BillListAndCardVO[]
	 * @return
	 * @author zengj
	 * @throws ObmBizException
	 * @since NC5.6
	 */
	private String canUseJTGJ(BillListAndCardVO[] bvo) throws ObmBizException {
		String func = bvo[0].getString("func");
		if (FuncTypeConst.JTGJ.equals(func)) {
			String bankType = bvo[0].getString("banktype");
			ObmLog.debug("前台日志-收款银行类别(banktype)=[" + bankType + "]", this.getClass(), "IsJTGJ()");

			String crtaccPk = bvo[0].getString("pk_crtacc");

			ObmAPIImpl obmapi = new ObmAPIImpl();
			BillListAndCardVO crtaccVO = obmapi.getBankaccVO(crtaccPk);
			if (crtaccVO != null) {
				String crtaccBankType = crtaccVO.getString("netbankinftpcode");
				ObmLog.debug("前台日志-收款银行类别(crtacc_bank)=[" + crtaccBankType + "]", this.getClass(), "IsJTGJ()");
				if (bankType.equals(crtaccBankType)) {
					ObmLog.debug("前台日志-使用集团归集指令支付!", this.getClass(), "IsJTGJ()");
					func = FuncTypeConst.JTGJ;
				} else {
					func = FuncTypeConst.ZF;
				}
			} else {
				func = FuncTypeConst.ZF;
			}
		}
		return func;
	}
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 根据开户银行，指令号查询配置文件信息
	 * 
	 * @param productcode
	 * @param bank
	 * @param func
	 * @return
	 * @throws BusinessException
	 * @author zengj
	 * @since NC5.6
	 */
	private XMLVO getHashMaps(String productcode, String bank, String func)
			throws BusinessException {
		XMLVO xvo = XmlAttrTool.getConfElementFromXml_New(bank, "", productcode, func, "", true);
		return xvo;
	}
	
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 填充帐号相关信息，包括付款帐号的付款限额
	 * 
	 * @param cvo
	 * @param func
	 * @param productcode
	 * @param xvo
	 * @return
	 * @author zengj
	 * @throws ObmBizException
	 * @since NC5.6
	 */
	private BillListAndCardVO[] fillFields(
			CircularlyAccessibleValueObject[] cvo, String func,
			String productcode, XMLVO xvo,String pk_corp) throws ObmBizException {
		int len = cvo == null ? 0 : cvo.length;
		HashMap[] map = null;
		BillListAndCardVO[] bvo = new BillListAndCardVO[len];
		String headpackageid = IDGen.getInst().getPackageID();
		for (int i = 0; i < len; i++) {
			BillListAndCardVO system = ToolVO.createSystemParameter();
			system.setAttributeValue("headpackageid", headpackageid);
			map = xvo.getBodymap();
			bvo[i] = fillFields(cvo[i], map, system, func, pk_corp);

		}
		return bvo;

	}
	
	/**
	 * 
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * 填充帐号相关信息，包括付款帐号的付款限额
	 * 
	 * @param cvo
	 * @param map
	 * @param system
	 * @param func
	 * @return
	 * @author zengj
	 * @throws ObmBizException
	 * @since NC5.6
	 */
	private BillListAndCardVO fillFields(CircularlyAccessibleValueObject cvo, HashMap[] map,
			BillListAndCardVO system, String func,String pk_corp) throws ObmBizException {
		BillListAndCardVO vo = new BillListAndCardVO();
		ObmAPIImpl obmapiimpl = new ObmAPIImpl();
		String[] str = cvo.getAttributeNames();
		int len = str == null ? 0 : str.length;
		for (int i = 0; i < len; i++) {
			vo.setAttributeValue(str[i], cvo.getAttributeValue(str[i]));
		}
		BillListAndCardVO dbtacc = null;
		BillListAndCardVO crtacc = null;
		BillListAndCardVO jtaccount = null;
		BillListAndCardVO account_num = null;
		Object obj = null;
		obj = cvo.getAttributeValue(PayItemConstant.DBTACC);
		Object pk_dbtacc = cvo.getAttributeValue(ObmItemConstant.PK_DBTACC);
		if (obj != null && obj.toString().length() > 0 && pk_dbtacc != null
				&& pk_dbtacc.toString().length() > 0) {
			if (func.equals(FuncTypeConst.ZF)
					|| func.equals(FuncTypeConst.JTGJ)
					|| func.equals(FuncTypeConst.JTZF)) {
				dbtacc = obmapiimpl.getBankaccInfo(pk_dbtacc.toString(), pk_corp, func);
				// 付款银行
				if (dbtacc != null) {

					cvo.setAttributeValue(ObmItemConstant.DBTQUOTA, dbtacc
							.getAttributeValue(PayItemConstant.TRSAMT));
					vo.setAttributeValue(ObmItemConstant.DBTQUOTA, dbtacc
							.getAttributeValue(PayItemConstant.TRSAMT));

					// 设置主办银行编码
					vo.setAttributeValue(ObmItemConstant.BANKOWNERCODE, dbtacc
							.getAttributeValue(ObmItemConstant.BANKOWNERCODE));
				}

			} else {
				dbtacc = obmapiimpl.getBankaccVO(pk_dbtacc.toString());
				// 设置主办银行编码
				vo.setAttributeValue(ObmItemConstant.BANKOWNERCODE, dbtacc
						.getAttributeValue(ObmItemConstant.BANKOWNERCODE));
			}
		}

		obj = cvo.getAttributeValue(PayItemConstant.CRTACC);
		Object pk_crtacc = cvo.getAttributeValue(ObmItemConstant.PK_CRTACC);
		if (obj != null && obj.toString().length() > 0 && pk_crtacc != null
				&& pk_crtacc.toString().length() > 0) {
			crtacc = obmapiimpl.getBankaccVO(pk_crtacc.toString());
		}
		if (dbtacc != null && crtacc != null) {
			system.setAttributeValue(PayItemConstant.ISSAMEBANK, ToolVO
					.isSamebank(dbtacc.getString(ObmItemConstant.BANKOWNERCODE),
							crtacc.getString(ObmItemConstant.BANKOWNERCODE)));
		}
		
		obj = cvo.getAttributeValue(ObmItemConstant.JTACCOUNT);
		if (obj != null && obj.toString().length() > 0) {
			//总账号在业务上不可能重复，所以不加banktype
			jtaccount = obmapiimpl.getBankaccVO(cvo.getAttributeValue(ObmItemConstant.JTACCOUNT).toString(),null);
		}
		
		obj = cvo.getAttributeValue(BalanceItemConstant.ACCOUNT_NUM);

		Object pk_account_num = cvo
				.getAttributeValue(ObmItemConstant.PK_BANKACCBAS);
		if (obj != null && obj.toString().length() > 0
				&& pk_account_num != null
				&& pk_account_num.toString().length() > 0) {
			account_num = obmapiimpl.getBankaccVO(pk_account_num.toString());
			// 设置主办银行编码
			vo.setAttributeValue(ObmItemConstant.BANKOWNERCODE, account_num
					.getAttributeValue(ObmItemConstant.BANKOWNERCODE));
		}
		len = map == null ? 0 : map.length;
		CircularlyAccessibleValueObject temp = null;
		Object key = null;
		Object key1 = null;
		boolean islimit = false;
		for (int i = 0; i < len; i++) {
			obj = map[i].get("source");
			islimit = ToolVO.isLimit((String) map[i].get("itemkey"));
			if (obj != null && obj.toString().length() > 0) {
				if (islimit == true)
					obj = "bill";
				if (obj.toString().equalsIgnoreCase("bill")) {
					temp = cvo;
				} else if (obj.toString().equalsIgnoreCase("dbtacc")) {
					temp = dbtacc;
				} else if (obj.toString().equalsIgnoreCase("crtacc")) {
					temp = crtacc;
				} else if (obj.toString().equalsIgnoreCase("account_num")) {
					temp = account_num;
				} else if (obj.toString().equalsIgnoreCase("system")) {
					temp = system;
				} else if (obj.toString().equalsIgnoreCase("jtaccount")) {
					temp = jtaccount;
				}
				key1 = obj;
				obj = null;
				if (temp != null) {
					key = map[i].get("srcitemkey");
					if (islimit == true
							|| (key == null || key.toString().length() == 0)) {
						key = map[i].get("itemkey");
					}
					obj = temp.getAttributeValue((String) key);
				}
				// 如果在开户银行上取不到，在单据上在取一次
				if ((key1.toString().equalsIgnoreCase("bill")) == false
						&& (obj == null || obj.toString().length() == 0)) {
					key = map[i].get("itemkey");
					obj = cvo.getAttributeValue((String) key);
				}
				cvo.setAttributeValue((String) map[i].get("itemkey"), obj);
				vo.setAttributeValue((String) map[i].get("itemkey"), obj);
			}
		}
		return vo;
	}
	
	/**
	 * 
	 * 网银补录优化前后台交互，处理网银补录数据
	 * @param bvo
	 * @return
	 * @author zengj 2009-8-25
	 * @since NC5.6
	 */
	private String checkDbQuota(BillListAndCardVO[] bvo) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bvo.length; i++) {
			String errmsg = CheckToolVO.checkQuota(bvo[i]);
			if (errmsg != null) {
				sb.append(MLString.getZhangHao() + bvo[i].getString("dbtacc") + errmsg + " ");
			}
		}
		String remsg = sb.length() == 0 ? null : sb.toString();
		return remsg;
	}
	
	/**
	 * 
	 * 网银补录优化前后台交互，处理网银补录数据
	 * @param cvo
	 * @param isbulu
	 * @return
	 * @author zengj 2009-8-25
	 * @since NC5.6
	 */
	private String checkRmb(CircularlyAccessibleValueObject[] cvo,boolean isbulu) {
		BankTypeConstImpl banktypeconstimpl = new BankTypeConstImpl();
		int len = cvo == null ? 0 : cvo.length;
		HashMap map = null;
		for (int i = 0; i < len; i++) {
			String pk_bz = (String) cvo[i].getAttributeValue("c_ccynbr");
			String func = (String) cvo[i].getAttributeValue(HeadAttrConst.FUNC);
			String bank = (String) cvo[i].getAttributeValue(HeadAttrConst.BANK);
			String type = (String) cvo[i].getAttributeValue(HeadAttrConst.TYPE);
			Object ufd = cvo[i].getAttributeValue("trsamt");
			try {
				map = banktypeconstimpl.getHashByBank(bank, type);
			} catch (EbankBusinessException e) {

			} catch (EbankBusinessRuntimeException e) {

			}
			String msg = null;
			if (pk_bz == null || pk_bz.length() == 0) {
				// return getRetEbankItemVO(cvo, len, al, i,1,"支付失败。单据上没有币种。");
				msg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3612",
						"UPP3612-000017")/*
											 * @res "支付失败。单据上没有币种。"
											 */;
				return msg;
			}
			else if (pk_bz != null
					&& !pk_bz.trim().equals("00010000000000000001")) {
				msg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3612",
						"UPP3612-000018")/*
											 * @res
											 * "支付失败。目前网银仅支持人民币业务。本付款指令指定的币种不是人民币。"
											 */;
				return msg;
			}
			else if (func != null && func.trim().equalsIgnoreCase("jtzf")) {
				if (isJtzf(map) == false) {
					return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3612",
							"UC000-0004114")/*
											 * @res "银行"
											 */
							+ bank
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
									"3612", "UPP3612-000019")/*
																 * @res
																 * "不支持集团支付。"
																 */;

				}
			} else if (isbulu == false && ufd != null
					&& new UFDouble(ufd.toString()).doubleValue() <= 0) {
				//
				return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3612",
						"UPP3612-000020")/*
											 * @res "支付失败。目前网银仅支持交易金额为正数的支付指令。"
											 */;
			}
		}
		return null;
	}
	
	/**
	 * 网银补录优化前后台交互，处理网银补录数据
	 * 
	 * @param map
	 * @return
	 * @author zengj 2009-8-25
	 * @since NC5.6
	 */
	private boolean isJtzf(HashMap map) {
		boolean b = false;
		if (map != null) {
			Object obj = map.get("jtzf");
			if (obj != null) {
				b = new UFBoolean(obj.toString()).booleanValue();
			}
		}
		return b;
	}
	
}

// txseqid=‘交易流水号（建行）’
// reqseqno=’渠道流水号（农行）‘
// clientpatchid=’客户交易号(光大银行)’
// packageid='包序列ID(工行)'
// packetid=”报文号(浦发银行)”
// trnid=”报文号(民生银行)”
// obssid =‘流水号’（中行）>
// private Object getPackageid(CircularlyAccessibleValueObject itemvo){
// if(itemvo == null){
// return null;
// }
// Object ret_packageid = null;
// Object obj = itemvo.getAttributeValue("txseqid");
// Object obj2 = itemvo.getAttributeValue("reqseqno");
// Object obj3 = itemvo.getAttributeValue("clientpatchid");
// Object obj4 = itemvo.getAttributeValue("packageid");
// Object obj5 = itemvo.getAttributeValue("packetid");
// Object obj6 = itemvo.getAttributeValue("trnid");
// Object obj7 = itemvo.getAttributeValue("obssid");
// if(obj != null && obj.toString().trim().length() > 0 ){
// ret_packageid = obj;
// }else if(obj != null && obj.toString().trim().length() > 0 ){
// ret_packageid = obj;
// }else if(obj2 != null && obj2.toString().trim().length() > 0 ){
// ret_packageid = obj2;
// }else if(obj3 != null && obj3.toString().trim().length() > 0 ){
// ret_packageid = obj3;
// }else if(obj4 != null && obj4.toString().trim().length() > 0 ){
// ret_packageid = obj4;
// }else if(obj5 != null && obj5.toString().trim().length() > 0 ){
// ret_packageid = obj5;
// }else if(obj6 != null && obj6.toString().trim().length() > 0 ){
// ret_packageid = obj6;
// }else if(obj7 != null && obj6.toString().trim().length() > 0 ){
// ret_packageid = obj7;
// }
// return ret_packageid;
// }
//
// private CircularlyAccessibleValueObject getItemVO(HashMap hash, String
// packageid) {
// Iterator it= hash.keySet().iterator();
// Object obj=null;
// CircularlyAccessibleValueObject itemvo=null;
// while(it.hasNext()){
// obj=it.next();
// if(packageid.endsWith((String)obj)){
// itemvo=(CircularlyAccessibleValueObject)hash.get(obj);
// break;
// }
// }
// return itemvo;
// }
// }
