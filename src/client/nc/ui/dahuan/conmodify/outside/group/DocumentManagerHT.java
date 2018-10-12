package nc.ui.dahuan.conmodify.outside.group;

/**
 * �����ĵ�����

 * �������ڣ�(2003-3-3 13:44:11)
 * @author������
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Map;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.filesystem.FileManageUI;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.datapower.BtnPowerVO;
public class DocumentManagerHT {
	/**
	 * ��ʾ�ĵ�����Ի���
	 * 
	 * @param parent ������
	 * @param billType �������ͣ�Ҳ����Ҫ�ϴ���Ŀ¼����
	 * @param id Ҫ�����ĵ��ĵ���ID
	 * @since 2008-11-24
	 */
	public static void showDM(Container parent, String billType, String id, BtnPowerVO powerVO) {
		String title = NCLangRes.getInstance().getStrByID("scmcommon",
				"UPPSCMCommon-000278");
		String rootPath = id;
		FileManageUI ui = new FileManageUI(rootPath);
		if (null != powerVO) {
			if (!StringUtil.isEmpty(powerVO.getUploadEnable())) {
				ui.setUploadFileEnable(UFBoolean.valueOf(powerVO.getUploadEnable()).booleanValue());
			}
			if (!StringUtil.isEmpty(powerVO.getShowFileEnable())) {
				ui.setShowFileEnable(UFBoolean.valueOf(powerVO.getShowFileEnable()).booleanValue());
			}
			if (!StringUtil.isEmpty(powerVO.getFileDelEnable())) {
				ui.setDeleteNodeEnable(UFBoolean.valueOf(powerVO.getFileDelEnable()).booleanValue());
			}
		}

		UIDialog dlg = new UIDialog(parent, title);
		dlg.getContentPane().setLayout(new BorderLayout());
		dlg.getContentPane().add(ui, BorderLayout.CENTER);
		dlg.setResizable(true);
		dlg.setSize(600, 400);
		dlg.showModal();
	}

	/**
	 * ��ʾ�ĵ�����Ի���
	 * 
	 * @param parent ������
	 * @param billType �������ͣ�Ҳ����Ҫ�ϴ���Ŀ¼����
	 * @param id Ҫ�����ĵ��ĵ���ID
	 * @since 2008-11-24
	 */
	public static void showDM(Container parent, String billType, String id) {
		showDM(parent, billType, id, null);
	}

	/**
	 * ��ʾ�ĵ�����Ի�������UAP�ӿڵ�ԭ��Ŀǰֻ֧��һ�����ݵ��ĵ�����
	 * <p><b>ע�⣺Ϊ�˸���ǰ����һ�£������˴�ID���飬����ʵ����ֻ�ܹ���������
	 * ��һ�����ݵ��ĵ�</b>
	 * 
	 * @param parent ������
	 * @param billType �������ͣ�Ҳ����Ҫ�ϴ���Ŀ¼����
	 * @param ids Ҫ�����ĵ��ĵ���ID����
	 * @since 2008-11-24
	 */
	public static void showDM(Container parent, String billType, String[] ids) {
		showDM(parent, billType, ids[0]);
	}

	/**
	 * ��ʾ�ĵ�����Ի�������UAP�ӿڵ�ԭ��Ŀǰֻ֧��һ�����ݵ��ĵ�����
	 * <p><b>ע�⣺Ϊ�˸���ǰ����һ�£������˴�ID���飬����ʵ����ֻ�ܹ���������
	 * ��һ�����ݵ��ĵ�</b>
	 * 
	 * @param parent ������
	 * @param billType �������ͣ�Ҳ����Ҫ�ϴ���Ŀ¼����
	 * @param ids Ҫ�����ĵ��ĵ���ID����
	 * @param powerVoMap ����ID�Ͱ�ťȨ�޿���VO��ӳ��
	 * @since 2008-11-24
	 */
	public static void showDM(Container parent, String billType, String[] ids, Map<String, BtnPowerVO> powerVoMap) {
		showDM(parent, billType, ids[0], null == powerVoMap ? null : powerVoMap.get(ids[0]));
	}

}