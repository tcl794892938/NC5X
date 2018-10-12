package nc.ui.dahuan.conmodify.outside.group;

/**
 * 单据文档管理

 * 创建日期：(2003-3-3 13:44:11)
 * @author：李振
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
	 * 显示文档管理对话框
	 * 
	 * @param parent 父窗体
	 * @param billType 单据类型，也就是要上传的目录名称
	 * @param id 要管理文档的单据ID
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
	 * 显示文档管理对话框
	 * 
	 * @param parent 父窗体
	 * @param billType 单据类型，也就是要上传的目录名称
	 * @param id 要管理文档的单据ID
	 * @since 2008-11-24
	 */
	public static void showDM(Container parent, String billType, String id) {
		showDM(parent, billType, id, null);
	}

	/**
	 * 显示文档管理对话框，由于UAP接口的原因，目前只支持一个单据的文档管理
	 * <p><b>注意：为了跟以前保持一致，保留了传ID数组，但是实际上只能管理数组中
	 * 第一个单据的文档</b>
	 * 
	 * @param parent 父窗体
	 * @param billType 单据类型，也就是要上传的目录名称
	 * @param ids 要管理文档的单据ID数组
	 * @since 2008-11-24
	 */
	public static void showDM(Container parent, String billType, String[] ids) {
		showDM(parent, billType, ids[0]);
	}

	/**
	 * 显示文档管理对话框，由于UAP接口的原因，目前只支持一个单据的文档管理
	 * <p><b>注意：为了跟以前保持一致，保留了传ID数组，但是实际上只能管理数组中
	 * 第一个单据的文档</b>
	 * 
	 * @param parent 父窗体
	 * @param billType 单据类型，也就是要上传的目录名称
	 * @param ids 要管理文档的单据ID数组
	 * @param powerVoMap 单据ID和按钮权限控制VO的映射
	 * @since 2008-11-24
	 */
	public static void showDM(Container parent, String billType, String[] ids, Map<String, BtnPowerVO> powerVoMap) {
		showDM(parent, billType, ids[0], null == powerVoMap ? null : powerVoMap.get(ids[0]));
	}

}