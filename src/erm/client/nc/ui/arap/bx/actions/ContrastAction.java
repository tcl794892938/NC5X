package nc.ui.arap.bx.actions;
/**
 * �����ؿλ by tcl
 */
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.arap.bx.BXBillCardPanel;
import nc.ui.arap.bx.ContrastDialog;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.vo.arap.bx.util.BXConstans;
import nc.vo.arap.bx.util.BxUIControlUtil;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.BxcontrastVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

/**
 * @author twei
 *
 * ����Action
 *
 * nc.ui.arap.bx.actions.ContrastAction
 */
public class ContrastAction extends BXDefaultAction {

	/**
	 * @throws BusinessException
	 *
	 * 1. ��ʼ������Ի���
	 * 2. ȡ��ѡ����Ϣ,�ڽ����ϼ�¼����Ϣ��ˢ�½���
	 */
	public void contrast() throws BusinessException {

		BXVO vo=null;

		vo = getBillValueVO();

		if (vo == null) {
			return;
		}

		if(vo.getChildrenVO()==null){

		}
		if(vo.getParentVO().getPrimaryKey()!=null){
			String primaryKey = vo.getParentVO().getPrimaryKey();
			if(getVoCache().getVOByPk(primaryKey)!=null){
				vo.setContrastVO(getVoCache().getVOByPk(primaryKey).getContrastVO());
			}
		}


		UFDate djrq = vo.getParentVO().getDjrq();
		if(djrq.after(ClientEnvironment.getInstance().getDate())){
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000410")/*@res "�������ĵ������ڲ������ڵ�ǰ��������!"*/);
		}

		ContrastDialog dialog = getMainPanel().getContrastDialog(vo,pk_corp);

		dialog.showModal();

		if (dialog.getResult() == UIDialog.ID_OK) {

			List<BxcontrastVO> contrastsData = dialog.getContrastData();
			
			//У���Ƿ�һ������ by tcl
			this.checkOneDataAndSetData(vo,contrastsData);

			doContrastToUI(getBxBillCardPanel(),vo, contrastsData);

		}
	}
	
	/**
	 * У�����ؿλһ���� by tcl 2016-10-25
	 * @param card
	 * @param vo
	 * @param contrastsData
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private void checkOneDataAndSetData(BXVO vo,List<BxcontrastVO> contrastsData)throws BusinessException{
		
		if(!"264X-01".equals(vo.getParentVO().getDjlxbm())){
			return ;
		}
		
		if(contrastsData==null||contrastsData.size()<=0||contrastsData.size()>1){
			
			throw new BusinessException("��ѡ��һ�����ݣ�");
		}
		
		BxcontrastVO bxvo=contrastsData.get(0);
		String pk=bxvo.getPk_jkd();
		String sql="select szxmid,hbbm,jsfs,fkyhzh from er_jkzb where pk_jkbx='"+pk+"' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Map<String, Object> map=(Map<String, Object>)iQ.executeQuery(sql, new MapProcessor());
		vo.getParentVO().setAttributeValue("szxmid", map.get("szxmid"));//Ԥ����Ŀ
		vo.getParentVO().setAttributeValue("hbbm", map.get("hbbm"));//�տλ
		vo.getParentVO().setAttributeValue("jsfs", map.get("jsfs"));//���㷽ʽ
		vo.getParentVO().setAttributeValue("fkyhzh", map.get("fkyhzh"));//���������ʺ�
	}

	public static void doContrastToUI(BXBillCardPanel card,BXVO vo, List<BxcontrastVO> contrastsData) throws BusinessException {
		if(contrastsData==null || contrastsData.size()==0 ){
			//ȡ�����ĳ���
			setHeadValue(card,BXHeaderVO.CJKYBJE, null);
			setHeadValue(card,BXHeaderVO.CJKBBJE, null);
			setHeadValue(card,BXHeaderVO.HKYBJE, null);
			setHeadValue(card,BXHeaderVO.HKBBJE, null);
			setHeadValue(card,BXHeaderVO.ZFYBJE, getHeadValue(card,BXHeaderVO.YBJE));
			setHeadValue(card,BXHeaderVO.ZFBBJE, getHeadValue(card,BXHeaderVO.BBJE));

			if(card.getBillModel(BXConstans.FIN_PAGE)!=null){
				int rowCount = card.getRowCount(BXConstans.FIN_PAGE);
				for(int i=0;i<rowCount;i++){
					card.setBodyValueAt(null, i, BXHeaderVO.CJKYBJE,BXConstans.FIN_PAGE);
					card.setBodyValueAt(null, i, BXHeaderVO.CJKBBJE,BXConstans.FIN_PAGE);
					card.setBodyValueAt(null, i, BXHeaderVO.HKYBJE,BXConstans.FIN_PAGE);
					card.setBodyValueAt(null, i, BXHeaderVO.HKBBJE,BXConstans.FIN_PAGE);
					card.setBodyValueAt(card.getBillModel(BXConstans.FIN_PAGE).getValueAt(i,BXHeaderVO.YBJE), i, BXHeaderVO.ZFYBJE,BXConstans.FIN_PAGE);
					card.setBodyValueAt(card.getBillModel(BXConstans.FIN_PAGE).getValueAt(i,BXHeaderVO.BBJE), i, BXHeaderVO.ZFBBJE,BXConstans.FIN_PAGE);
				}
			}
		}else{
			BXVO bxvo = new BxUIControlUtil().doContrast(vo, contrastsData);
			card.setBillValueVO(bxvo);
		}

		card.setContrast(true);
		card.setContrasts(contrastsData);
		card.setFinchanged(true);
	}
}