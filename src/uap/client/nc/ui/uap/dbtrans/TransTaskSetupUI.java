package nc.ui.uap.dbtrans;

import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.bs.framework.common.NCLocator;
import nc.bs.uap.sfapp.util.SFAppServiceUtil;
import nc.itf.bxgt.djtc.IbxgtQuerySynchro;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.beans.table.VOTableModel;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.ui.pub.pa.config.ConfigToftPanel;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.uap.dbtrans.ext.ParameterInputDlg;
import nc.vo.logging.Debug;
import nc.vo.ml.IProductCode;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.uap.dbtrans.Constant;
import nc.vo.uap.dbtrans.DBVO;
import nc.vo.uap.dbtrans.TableAndSqlVO;
import nc.vo.uap.dbtrans.TaskVO;
import nc.vo.uap.dbtrans.TransContentVO;

/**
 * ���ݴ���UI���档 �������ڣ�(2003-2-21 15:08:46)
 * 
 * @author�������
 */
public class TransTaskSetupUI extends nc.ui.pub.ToftPanel implements
		java.awt.event.ActionListener, java.awt.event.ItemListener,
		TreeSelectionListener {
	private UIComboBox ivjCBBTaskType = null;

	private UILabel ivjLblTaskDesc = null;

	private UILabel ivjLblTaskName = null;

	private UILabel ivjLblTaskType = null;

	private UIPanel ivjRightPanel = null;

	private UIScrollPane ivjRightSP = null;

	private UIScrollPane ivjSPTaskDesc = null;

	private UIScrollPane ivjSPTaskList = null;

	private UITextField ivjTFTaskName = null;

	private UIPanel ivjUIPanel1 = null;

	private UIPanel ivjUIPanel2 = null;

	private UISplitPane ivjUISplitPane1 = null;

	private UITablePane ivjTPContens = null;

	private UIPanel ivjUIPanel3 = null;

	private UIButton ivjBtnEdit = null;

	private UIButton ivjBtnView = null;

	private UITextArea ivjTATaskDesc = null;

	// ����ִ�з�ʽ
	private UILabel taskExecuteMode_lb = null;

	private UICheckBox taskExecuteMode_chk = null;

	private TreePanel treePanel = null;

	//
	private nc.ui.pub.ButtonObject m_boAdd = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000002")/*
									 * @res "����"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000125")/*
										 * @res "����������"
										 */, "����");

	private nc.ui.pub.ButtonObject m_boUpdate = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000045")/*
									 * @res "�޸�"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000126")/*
										 * @res "�޸�ѡ������"
										 */, "�޸�");

	private nc.ui.pub.ButtonObject m_boCopy = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000043")/*
									 * @res "����"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000127")/*
										 * @res "����ѡ������"
										 */, "����");

	private nc.ui.pub.ButtonObject m_boDelete = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000039")/*
									 * @res "ɾ��"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000128")/*
										 * @res "ɾ��ѡ�е�����"
										 */, "ɾ��");

	private nc.ui.pub.ButtonObject m_boSave = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000001")/*
									 * @res "����"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000129")/*
										 * @res "��������"
										 */, "����");

	private nc.ui.pub.ButtonObject m_boCancel = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000008")/*
									 * @res "ȡ��"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000130")/*
										 * @res "ȡ�������Ķ�"
										 */, "ȡ��");

	private nc.ui.pub.ButtonObject m_boExecute = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000026")/*
									 * @res "ִ��"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000131")/*
										 * @res "����ִ��ѡ������"
										 */, "ִ��");

	private nc.ui.pub.ButtonObject m_boRefresh = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000009")/*
									 * @res "ˢ��"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000132")/*
										 * @res "ˢ�������б�"
										 */, "ˢ��");

	private nc.ui.pub.ButtonObject m_boTemplet = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPT102106-000002")/*
										 * @res "����"
										 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000133")/*
										 * @res "�༭����"
										 */, "����");

	private nc.ui.pub.ButtonObject m_boLog = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000134")/*
										 * @res "��־"
										 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPT102106-000001")/*
										 * @res "�鿴��־"
										 */, "��־");

	private nc.ui.pub.ButtonObject m_boSwitch = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPT102106-000003")/*
										 * @res "����Ԥ��"
										 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000135")/*
										 * @res "�л���Ԥ��ƽ̨���ý���"
										 */, "����Ԥ��");

	private nc.ui.pub.ButtonObject[] m_boGroup = new nc.ui.pub.ButtonObject[] {
			m_boAdd, m_boUpdate, m_boCopy, m_boDelete, m_boSave, m_boCancel,
			m_boRefresh, m_boTemplet, m_boExecute, m_boLog, m_boSwitch };

	// ��Ŀ���Ͱ�ť
	private nc.ui.pub.ButtonObject m_boRegistry = null;

	private nc.ui.pub.ButtonObject m_boAddRegistry = null;

	private nc.ui.pub.ButtonObject m_boDelRegistry = null;

	private nc.ui.pub.ButtonObject m_boEditRegistry = null;

	private nc.ui.pub.ButtonObject m_boRefreshRegistrys = null;

	private nc.ui.pub.ButtonObject m_boBack = null;

	//
	private UIButton ivjBtnAdd = null;

	private UIButton ivjBtnDel = null;

	// ״̬
	private static final int STATE_BROWSE = 0;

	private static final int STATE_ADDNEW = 1;

	private static final int STATE_MODIFY = 2;

	private static final int STATE_EXECTASK = 3;

	private int m_iState = STATE_BROWSE;

	//
	private Object m_cbbTaskTypePreSel = null;

	// Ԥ��ƽ̨����
	private ConfigToftPanel m_cfgPanel = null; // new

	// nc.ui.pub.pa.ConfigToftPanel("���ݴ�������Ԥ��");

	/**
	 * TransTaskSetupUI ������ע�⡣
	 */
	public TransTaskSetupUI() {
		super();
		initialize();
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(getBtnAdd())) {
			doBtnAdd();
		} else if (source.equals(getBtnDel())) {
			doBtnDel();
		} else if (source.equals(getBtnEdit())) {
			doBtnEdit();
		} else if (source.equals(getBtnView())) {
			doBtnView();
		}
	}

	/**
	 * ����������������������е�������¼�� �������ڣ�(2003-3-4 16:07:18)
	 * 
	 * @param vo
	 *            nc.vo.uap.dbtrans.TransContentVO
	 */
	private void dealWithByTaskType(TransContentVO vo) {
		UITable table = getTPContens().getTable();
		VOTableModel model = (VOTableModel) table.getModel();
		int rowCount = model.getRowCount();
		int taskType = getCBBTaskType().getSelectedIndex();
		for (int i = 0; i < rowCount; i++) {
			if (taskType == Constant.TASKTYPE_DISTRIBUTE) {
				DBVO sourDB = (DBVO) vo.getSourDB().clone();
				((TransContentVO) model.getVO(i)).setSourDB(sourDB);
			} else if (taskType == Constant.TASKTYPE_COLLECT) {
				DBVO destDB = (DBVO) vo.getDestDB().clone();
				((TransContentVO) model.getVO(i)).setDestDB(destDB);
			}

		}
	}

	/**
	 * ����һ���µ����ݴ������� �������ڣ�(2003-3-4 19:21:19)
	 */
	private void doBOAddTask() {
		m_iState = STATE_ADDNEW;
		setButtonObjectEnable(m_iState);
		setUIEnable(m_iState);
		setRightPanelByTask(null);
	}

	/**
	 * ȡ�������� �������ڣ�(2003-3-4 19:21:32)
	 */
	private void doBOCancelTask() {
		TaskVO selVO = null;

		selVO = getSelectedTaskVO();
		if (selVO != null) {
			setRightPanelByTask((TaskVO) selVO.clone());
		}

		m_iState = STATE_BROWSE;
		setButtonObjectEnable(m_iState);
		setUIEnable(m_iState);
	}

	/**
	 * ����һ������ �������ڣ�(2003-3-4 19:21:44)
	 */
	private void doBOCopyTask() {
		TaskVO selVO = getSelectedTaskVO();

		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "��ʾ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000136")/*
										 * @res "����ѡ��һ��������ִ�и��Ʋ���"
										 */);
			return;
		}

		String taskName = selVO.getTaskName();
		String newName = null;
		do {
			newName = (String) MessageDialog.showInputDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000137")/*
												 * @res "����ѡ�е�����"
												 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000138")/*
												 * @res "�������µ���������"
												 */, taskName + " COPY");
			if (newName != null) {
				newName = newName.trim();
				if (newName.length() <= 0) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000139")/*
												 * @res "������������Ʋ���Ϊ��"
												 */);
				} else if (!isOnlyName(newName)) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000140")/*
												 * @res "������������Ʋ�Ψһ������������"
												 */);
				} else {
					break;
				}
			}
		} while (newName != null);

		SelectTaskPathDlg pathDlg = new SelectTaskPathDlg(this);
		String taskPath = null;
		if (UIDialog.ID_OK == pathDlg.showModal()) {
			taskPath = pathDlg.getSelecedPath();
		} else {
			taskPath = getPath();
		}

		if (newName != null) {

			TaskVO newVO = (TaskVO) selVO.clone();
			newVO.setFileName(null);
			newVO.setTaskName(newName);

			setTs(newVO);

			try {
				newVO.setFilePath(taskPath);
				SFAppServiceUtil.getTransTaskService(null).addNew(newVO);
				refreshTree();
			} catch (Exception e) {
				Debug.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("102106", "UPP102106-000017")/*
																				 * @res
																				 * "����"
																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
						"UPP102106-000141", null,
						new String[] { e.getMessage() }));// /*@res
				// "��������ʱ�������⣺
				// {0}"*/+e.getMessage());
			}
		}

	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong> ����ʱ����Ƿ����
	 * <p>
	 * <strong>����޸����ڣ�2006-7-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return TaskVO
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setTs(TaskVO taskVO) {
		boolean isUpdateTs = false;
		isUpdateTs = MessageDialog.showYesNoDlg(this, "��ʾ", "ʱ����Ƿ����?") == MessageDialog.ID_YES;

		if (isUpdateTs) {
			TransContentVO[] contents = taskVO.getTransContents();
			int count = contents == null ? 0 : contents.length;
			for (int i = 0; i < count; i++) {
				TableAndSqlVO[] tass = contents[i].getAllTableAndSqlVOs();
				for (int j = 0, n = tass == null ? 0 : tass.length; j < n; j++) {
					tass[j].setLastTS("0");
				}

			}
		}

	}

	private void doBODel() {
		DefaultMutableTreeNode selNode = getSelectedNode();
		if (selNode == null) {
			return;
		}
		if (selNode.getUserObject() instanceof TaskVO) {
			delTask();
		} else {
			delDirectory();
		}
	}

	private void delDirectory() {
		DefaultMutableTreeNode selNode = getSelectedNode();

		if (selNode == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000092")/* @res "����ѡ����ִ��ɾ������" */);
			return;
		}
		if (MessageDialog
				.showOkCancelDlg(this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000093")/* @res "����" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000094")/* @res "���Ҫɾ����" */) == MessageDialog.ID_OK) {
			try {

				// DefaultTreeModel model = (DefaultTreeModel) getUItree()
				// .getModel();

				boolean succ = SFAppServiceUtil.getTransTaskService(null)
						.deleteDirectory(getPath(), Constant.TASK);
				// ���½���
				if (succ) {
					// model.removeNodeFromParent(selNode);
					doBORefresh();
				} else {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000095")/*
												 * @res "ɾ��ʧ��"
												 */);
				}

			} catch (Exception e) {
				Debug.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000017")/* @res "����" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000096", null,
								new String[] { e.getMessage() }));// /*@res
				// "ִ��ɾ������ʱ�������⣺{0}"*/
				// +
				// e.getMessage());
			}
		}

	}

	/**
	 * ɾ��һ������ �������ڣ�(2003-3-4 19:21:55)
	 */
	private void delTask() {

		TaskVO selVO = getSelectedTaskVO();
		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "��ʾ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000142")/*
										 * @res "����ѡ��һ��������ִ��ɾ������"
										 */);
			return;
		}

		if (MessageDialog.showOkCancelDlg(this, nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UPP102106-000093")/*
																		 * @res
																		 * "����"
																		 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UPP102106-000143")/*
									 * @res "���Ҫɾ��ѡ�е�������"
									 */) == MessageDialog.ID_OK) {
			try {
				selVO.setFilePath(getPath());
				boolean succ = SFAppServiceUtil.getTransTaskService(null)
						.delete(selVO);
				if (succ) {
					refreshTree();
					setRightPanelByTask(null);

				} else {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000144")/*
												 * @res "ɾ������ʧ��"
												 */);
				}
			} catch (Exception e) {
				Debug.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("102106", "UPP102106-000017")/*
																				 * @res
																				 * "����"
																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
						"UPP102106-000145", null,
						new String[] { e.getMessage() }));// /*@res
				// "ִ��ɾ���������ʱ�������⣺
				// {0}"*/
				// +
				// e.getMessage());
			}
		}

	}

	/**
	 * ִ��һ������ �������ڣ�(2003-3-4 19:22:04)
	 */
	private void doBOExecTask() {
		final TaskVO selVO = getSelectedTaskVO();
		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "��ʾ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000146")/*
										 * @res "����ѡ��һ��������ִ��"
										 */);
			return;
		}

		new Thread() {
			public void run() {
				executeTaskWithProcessBanner(selVO);
			}

		}.start();

	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-9-5</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void executeTaskWithProcessBanner(final TaskVO selVO) {
		new Thread(new Runnable() {
			public void run() {
				BannerDialog dialog = new BannerDialog(TransTaskSetupUI.this);
				dialog
						.setStartText(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("102106", "UPT102106-000210")/* ���ں�ִ̨���������Ժ� */);
				try {
					dialog.start();
					// ִ������
					executeTask(selVO);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dialog.end();
				}
			}
		}).start();
	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-9-5</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void executeTask(final TaskVO selVO) {
		try {
			setButtonObjectEnable(TransTaskSetupUI.STATE_EXECTASK);
			// ��ʾ������������
			TransContentVO[] needParaTransContentVOs = selVO
					.getNeedParaContentVOs();
			if (needParaTransContentVOs != null
					&& needParaTransContentVOs.length > 0) {
				ParameterInputDlg paraDlg = new ParameterInputDlg(
						nc.ui.uap.dbtrans.TransTaskSetupUI.this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000147")/*
													 * @res "��������Ի���"
													 */, needParaTransContentVOs);
				if (paraDlg.showModal() == UIDialog.ID_CANCEL) {
					return;
				}
			}
			//
			TaskVO retrVO = SFAppServiceUtil.getTranExecTaskService(null)
					.executeTask(selVO);
			selVO.setTransContents(retrVO.getTransContents());
			MessageDialog.showHintDlg(TransTaskSetupUI.this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("102106", "UPP102106-000148")/*
																			 * @res
																			 * "ִ������"
																			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000148")/*
										 * @res "ִ������"
										 */
					+ selVO.getTaskName()
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000149")/*
												 * @res "��ɣ�ִ�������鿴��־"
												 */);
		} catch (Exception e) {
			MessageDialog.showHintDlg(TransTaskSetupUI.this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("102106", "UPP102106-000148")/*
																			 * @res
																			 * "ִ������"
																			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000150", null,
					new String[] { selVO.getTaskName(), e.getMessage() }));// /*@res
			// "ִ������
			// {0}ʧ�ܣ�
			// {1}"*/+selVO.getTaskName()+"ʧ�ܣ�"+e.getMessage());
			Debug.error(e.getMessage(), e);
		} finally {
			setButtonObjectEnable(TransTaskSetupUI.STATE_BROWSE);
		}
	}

	/**
	 * �鿴��־�� �������ڣ�(2003-3-17 11:11:18)
	 */
	private void doBOLog() {
		
		String str = "";
		try {
			str = SFAppServiceUtil.getTranExecTaskService(null).getLogText()
					.toString();
			LogView v = new LogView(new String[] { str }, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("102106", "UPP102106-000151")/*
																			 * @res
																			 * "��־�ļ���¼"
																			 */);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * ˢ�²����� �������ڣ�(2003-3-4 19:21:32)
	 */
	private void doBORefresh() {
		refreshTree();
		setRightPanelByTask(null);

	}

	/**
	 * ����һ������ �������ڣ�(2003-3-4 19:22:15)
	 */
	private void doBOSaveTask() {
		TaskVO newVO = getTaskFromRightPanel();
		// �������ĺϷ���
		try {
			newVO.validate();
		} catch (nc.vo.pub.ValidationException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000017")/*
																 * @res "����"
																 */, e.getMessage());
			return;
		}

		// ����
		try {
			if (m_iState == STATE_ADDNEW) { // ����һ���½�������
				// ������������Ƿ�Ψһ
				if (!isOnlyName(newVO.getTaskName())) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000152")/*
												 * @res "������������Ʋ�Ψһ��������������"
												 */);
					return;
				}
				// �������񵽷�������
				newVO.setFilePath(getPath());
				SFAppServiceUtil.getTransTaskService(null).addNew(newVO);
				// �������񵽽���������б���
				refreshTree();
			} else if (m_iState == STATE_MODIFY) { // �����һ��������޸�
				// �޸Ľ����ϵ�����
				if (!isOnlyName(newVO.getTaskName())) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000153")/*
												 * @res "�������Ʋ�Ψһ��������������"
												 */);
					return;
				}

				TaskVO selVO = getSelectedTaskVO();
				selVO.setTaskName(newVO.getTaskName());
				selVO.setTaskDesc(newVO.getTaskDesc());
				selVO.setTaskType(newVO.getTaskType());
				selVO.setTransContents(newVO.getTransContents());
				selVO.setDebugExecute(newVO.isDebugExecute());
				selVO.setFilePath(getPath());
				// ����ʱ����Ƿ����
				setTs(selVO);
				// �޸ķ������ϵ�����
				SFAppServiceUtil.getTransTaskService(null).update(selVO);
			}
			// ���ý���
			m_iState = STATE_BROWSE;
			setButtonObjectEnable(m_iState);
			setUIEnable(m_iState);
			//
		} catch (Exception e) {
			Debug.debug(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000017")/*
																 * @res "����"
																 */, e.getMessage());
			return;
		}

	}

	/**
	 * �л���Ԥ��ƽ̨���档 �������ڣ�(2003-3-19 8:52:13)
	 */
	private void doBoSwitch() {
		add(getPreAlertUI().getUIPanel3(), "Center");
		remove(getUISplitPane1());
		setButtons(new nc.ui.pub.ButtonObject[] { m_boRegistry, m_boBack });
		updateButton(m_boAddRegistry);
		updateButton(m_boDelRegistry);
		updateButton(m_boEditRegistry);
		updateButton(m_boRefreshRegistrys);
		setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UPP102106-000154")/*
									 * @res "���ݴ�������Ԥ����Ŀ���ý���"
									 */);
		this.repaint(this.getVisibleRect());
	}

	/**
	 * �༭ģ�塣 �������ڣ�(2003-3-5 14:36:10)
	 */
	private void doBOTemplet() {
		TempletSetupDlg dlg = new TempletSetupDlg(this, nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UPP102106-000155")/*
																		 * @res
																		 * "����༭�Ի���"
																		 */);
		dlg.showModal();
	}

	/**
	 * �޸�һ��ָ������ �������ڣ�(2003-3-4 19:22:33)
	 */
	private void doBOUpdateTask() {
		TaskVO selVO = getSelectedTaskVO();
		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "��ʾ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000156")/*
										 * @res "����ѡ��һ��������ִ���޸Ĳ�����"
										 */);
			return;
		}
		m_iState = STATE_MODIFY;
		setButtonObjectEnable(m_iState);
		setUIEnable(m_iState);
	}

	/**
	 * ��Ӧ���Ӱ�ť�ĵ���¼�������һ���������ݼ�¼ �������ڣ�(2003-2-22 11:05:49)
	 */
	private void doBtnAdd() {
		UITable table = getTPContens().getTable();
		VOTableModel model = (VOTableModel) table.getModel();
		//
		TransContentVO newVO = new TransContentVO();
		if (model.getRowCount() > 0) {
			int taskType = getCBBTaskType().getSelectedIndex();
			TransContentVO vo = (TransContentVO) model.getVO(0);
			if (taskType == Constant.TASKTYPE_DISTRIBUTE) {
				DBVO sourDB = vo.getSourDB();
				newVO.setSourDB((DBVO) sourDB.clone());
			} else if (taskType == Constant.TASKTYPE_COLLECT) {
				DBVO destDB = vo.getDestDB();
				newVO.setDestDB((DBVO) destDB.clone());
			}
		}
		TransContentsSetupUI dlg = new TransContentsSetupUI(this, true);
		dlg.setUIData(newVO);
		if (dlg.showModal() == UIDialog.ID_OK) {
			newVO = dlg.getTransContentVO();
			//
			dealWithByTaskType(newVO);
			model.addVO(newVO);
			//
		}
	}

	/**
	 * ��Ӧɾ����ť�ĵ���¼���ɾ��һ���������ݼ�¼ �������ڣ�(2003-2-22 11:05:49)
	 */
	private void doBtnDel() {
		UITable table = getTPContens().getTable();
		int index = table.getSelectedRow();
		if (index == -1) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "��ʾ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000157")/*
										 * @res "����ѡ��һ����¼��ִ��ɾ������"
										 */);
			return;
		} else {
			if (MessageDialog.showOkCancelDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("102106", "UPP102106-000093")/*
																			 * @res
																			 * "����"
																			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000083")/*
										 * @res "���Ҫɾ��ѡ�еļ�¼��"
										 */) == MessageDialog.ID_OK) {
				VOTableModel model = (VOTableModel) table.getModel();
				model.removeVO(index);
			}
		}
	}

	/**
	 * ��Ӧ�༭��ť�ĵ���¼����༭һ���������ݼ�¼ �������ڣ�(2003-2-22 11:05:49)
	 */
	private void doBtnEdit() {
		UITable table = getTPContens().getTable();
		int index = table.getSelectedRow();
		if (index == -1) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "��ʾ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000158")/*
										 * @res "����ѡ��һ����¼��ִ�б༭����"
										 */);
			return;
		}
		VOTableModel model = (VOTableModel) table.getModel();
		TransContentVO selVO = (TransContentVO) model.getVO(index);
		TransContentsSetupUI dlg = new TransContentsSetupUI(this, true);
		dlg.setUIData((TransContentVO) selVO.clone());
		if (dlg.showModal() == UIDialog.ID_OK) {
			TransContentVO newVO = dlg.getTransContentVO();
			//
			selVO.setDestDB(newVO.getDestDB());
			selVO.setSourDB(newVO.getSourDB());
			selVO.setTableAndSqlVOs(newVO.getTableAndSqlVOs());
			selVO.setTransMode(newVO.getTransMode());
			selVO.setTaskTempletVOs(newVO.getTaskTempletVOs());
			selVO.setTransContentExtVO(newVO.getTransContentExtVO());
			// //model.removeVO(index);
			dealWithByTaskType(newVO);
			// //model.getVOs().add(index, newVO);
			// table.getSelectionModel().setSelectionInterval(index, index);
			//
		}

	}

	/**
	 * ��Ӧ�쿴��ť�ĵ���¼������һ���������ݼ�¼ �������ڣ�(2003-2-22 11:06:03)
	 */
	private void doBtnView() {
		UITable table = getTPContens().getTable();
		int index = table.getSelectedRow();
		if (index == -1) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "��ʾ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000159")/*
										 * @res "����ѡ��һ����¼��ִ���������"
										 */);
			return;
		}
		VOTableModel model = (VOTableModel) table.getModel();
		TransContentVO selVO = (TransContentVO) model.getVO(index);
		TransContentsSetupUI dlg = new TransContentsSetupUI(this, true);
		dlg.setUIData((TransContentVO) selVO.clone());
		dlg.showModal();
	}

	/**
	 * ���� BtnAdd ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBtnAdd() {
		if (ivjBtnAdd == null) {
			try {
				ivjBtnAdd = new nc.ui.pub.beans.UIButton();
				ivjBtnAdd.setName("BtnAdd");
				HotkeyUtil.setHotkeyAndText(ivjBtnAdd, 'A', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000002"));/*
													 * @res "����"
													 */
				ivjBtnAdd.setBounds(15, 0, 70, 22);
				// ivjBtnAdd.setLocation(15, 37);
				// user code begin {1}
				ivjBtnAdd.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnAdd;
	}

	/**
	 * ���� BtnDel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBtnDel() {
		if (ivjBtnDel == null) {
			try {
				ivjBtnDel = new nc.ui.pub.beans.UIButton();
				ivjBtnDel.setName("BtnDel");
				HotkeyUtil.setHotkeyAndText(ivjBtnDel, 'D', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000039"));/*
													 * @res "ɾ��"
													 */
				ivjBtnDel.setBounds(15, 70, 70, 22);
				// ivjBtnDel.setLocation(15, 116);
				// user code begin {1}
				ivjBtnDel.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnDel;
	}

	/**
	 * ���� BtnDetail ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBtnEdit() {
		if (ivjBtnEdit == null) {
			try {
				ivjBtnEdit = new nc.ui.pub.beans.UIButton();
				ivjBtnEdit.setName("BtnEdit");
				ivjBtnEdit.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000160")/*
																	 * @res
																	 * "�༭�������"
																	 */);
				HotkeyUtil.setHotkeyAndText(ivjBtnEdit, 'E', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000004"));/*
													 * @res "�༭"
													 */
				ivjBtnEdit.setBounds(15, 35, 70, 22);
				// user code begin {1}
				ivjBtnEdit.addActionListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnEdit;
	}

	/**
	 * ���� BtnView ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBtnView() {
		if (ivjBtnView == null) {
			try {
				ivjBtnView = new nc.ui.pub.beans.UIButton();
				ivjBtnView.setName("BtnView");
				ivjBtnView.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000161")/*
																	 * @res
																	 * "�����ϸ���"
																	 */);
				HotkeyUtil.setHotkeyAndText(ivjBtnView, 'V', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000021"));/*
													 * @res "���"
													 */
				ivjBtnView.setBounds(15, 105, 70, 22);
				// ivjBtnView.setLocation(15, 180);
				// user code begin {1}
				ivjBtnView.addActionListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnView;
	}

	/**
	 * ���� CBBTaskType ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIComboBox getCBBTaskType() {
		if (ivjCBBTaskType == null) {
			try {
				ivjCBBTaskType = new nc.ui.pub.beans.UIComboBox();
				ivjCBBTaskType.setName("CBBTaskType");
				ivjCBBTaskType.setBounds(48, 99, 158, 22);
				// user code begin {1}
				String[] taskTypes = Constant.getTask_mlStrs();
				for (int i = 0, n = taskTypes.length; i < n; i++) {
					ivjCBBTaskType.addItem(taskTypes[i]);
				}
				// ivjCBBTaskType.addItemListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCBBTaskType;
	}

	/**
	 * ���� LblTaskDesc ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLblTaskDesc() {
		if (ivjLblTaskDesc == null) {
			try {
				ivjLblTaskDesc = new nc.ui.pub.beans.UILabel();
				ivjLblTaskDesc.setName("LblTaskDesc");
				ivjLblTaskDesc.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000162")/*
																	 * @res
																	 * "����������"
																	 */);
				ivjLblTaskDesc.setBounds(250, 17, 100, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblTaskDesc;
	}

	/**
	 * ���� LblTaskName ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLblTaskName() {
		if (ivjLblTaskName == null) {
			try {
				ivjLblTaskName = new nc.ui.pub.beans.UILabel();
				ivjLblTaskName.setName("LblTaskName");
				ivjLblTaskName.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000163")/*
																	 * @res
																	 * "�������ƣ�"
																	 */);
				ivjLblTaskName.setBounds(48, 17, 68, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblTaskName;
	}

	/**
	 * ���� LblTaskType ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLblTaskType() {
		if (ivjLblTaskType == null) {
			try {
				ivjLblTaskType = new nc.ui.pub.beans.UILabel();
				ivjLblTaskType.setName("LblTaskType");
				ivjLblTaskType.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000164")/*
																	 * @res
																	 * "�������ͣ�"
																	 */);
				ivjLblTaskType.setBounds(48, 72, 68, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLblTaskType;
	}

	/**
	 * ���Ԥ��ƽ̨�Ľ��档 �������ڣ�(2002-4-5 12:58:57)
	 * 
	 * @return nc.ui.pub.pa.ConfigToftPanel
	 */
	private ConfigToftPanel getPreAlertUI() {
		if (m_cfgPanel == null) {
			// m_cfgPanel = new ConfigToftPanel(nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("102106", "UPP102106-000165")/*
			// * @res
			// * "���ݴ�������Ԥ��"
			// */);

			// m_cfgPanel = new ConfigToftPanel(nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("prealerttype", "UAP-datatransfer")/*
			// * @res
			// * "���ݴ�������Ԥ��"
			// */);
			m_cfgPanel = new ConfigToftPanel(
					"nc.bs.uap.dbtrans.TransTaskPAPlugIn");

		}
		return m_cfgPanel;
	}

	/**
	 * ���� RightPanel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getRightPanel() {
		if (ivjRightPanel == null) {
			try {
				ivjRightPanel = new nc.ui.pub.beans.UIPanel();
				ivjRightPanel.setName("RightPanel");
				ivjRightPanel.setLayout(new java.awt.BorderLayout());
				ivjRightPanel.setLocation(0, 0);
				getRightPanel().add(getUIPanel1(), "North");
				getRightPanel().add(getTPContens(), "Center");
				getRightPanel().add(getUIPanel2(), "East");
				getRightPanel().add(getUIPanel3(), "West");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRightPanel;
	}

	/**
	 * ���� RightSP ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIScrollPane getRightSP() {
		if (ivjRightSP == null) {
			try {
				ivjRightSP = new nc.ui.pub.beans.UIScrollPane();
				ivjRightSP.setName("RightSP");
				getRightSP().setViewportView(getRightPanel());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRightSP;
	}

	/**
	 * ���� SPTaskDesc ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIScrollPane getSPTaskDesc() {
		if (ivjSPTaskDesc == null) {
			try {
				ivjSPTaskDesc = new nc.ui.pub.beans.UIScrollPane();
				ivjSPTaskDesc.setName("SPTaskDesc");
				ivjSPTaskDesc.setBounds(250, 49, 320, 92);
				getSPTaskDesc().setViewportView(getTATaskDesc());
				// user code begin {1}
				ivjSPTaskDesc
						.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSPTaskDesc;
	}

	/**
	 * ���� SPTaskList ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIScrollPane getSPTaskList() {
		if (ivjSPTaskList == null) {
			try {
				ivjSPTaskList = new nc.ui.pub.beans.UIScrollPane();
				ivjSPTaskList.setName("SPTaskList");
				ivjSPTaskList
						.setPreferredSize(new java.awt.Dimension(150, 131));
				getSPTaskList().setViewportView(getTreePanel());
				// user code begin {1}
				nc.ui.pub.beans.UILabel lab = new nc.ui.pub.beans.UILabel(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000048")/*
													 * @res "�����б�"
													 */);
				lab.setILabelType(0);// java �Զ���
				lab.setBorder(javax.swing.BorderFactory
						.createRaisedBevelBorder());
				lab.setHorizontalAlignment(nc.ui.pub.beans.UILabel.CENTER);
				ivjSPTaskList.setColumnHeaderView(lab);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSPTaskList;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-3-4 19:45:26)
	 * 
	 * @return nc.vo.uap.dbtrans.TaskVO
	 */
	private TaskVO getTaskFromRightPanel() {
		TaskVO newVO = new TaskVO();
		newVO.setTaskName(getTFTaskName().getText().trim());
		newVO.setTaskDesc(getTATaskDesc().getText().trim());
		newVO.setTaskType(getCBBTaskType().getSelectedIndex());
		newVO.setDebugExecute(getTaskExecuteMode_chk().isSelected());

		VOTableModel model = (VOTableModel) getTPContens().getTable()
				.getModel();
		TransContentVO[] vos = (TransContentVO[]) model.getVOArray();
		newVO.setTransContents(vos);

		return newVO;
	}

	/**
	 * ���� TATaskDesk ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITextArea getTATaskDesc() {
		if (ivjTATaskDesc == null) {
			try {
				ivjTATaskDesc = new nc.ui.pub.beans.UITextArea();
				ivjTATaskDesc.setName("TATaskDesc");
				ivjTATaskDesc.setBounds(0, 0, 160, 120);
				// user code begin {1}
				ivjTATaskDesc.setLineWrap(true);
				ivjTATaskDesc.setWrapStyleWord(true);
				// ivjTATaskDesc.setDocument(new
				// nc.ui.pub.beans.textfield.UITextDocument() {
				ivjTATaskDesc.setDocument(new javax.swing.text.PlainDocument() {
					public void insertString(int offset, String str,
							javax.swing.text.AttributeSet attr)
							throws javax.swing.text.BadLocationException {
						if (str == null)
							return;
						if (str.indexOf('<') != -1 || str.indexOf('>') != -1
								|| str.indexOf('/') != -1)
							return;
						super.insertString(offset, str, attr);
					}
				}); // user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTATaskDesc;
	}

	/**
	 * ���� TFTaskName ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITextField getTFTaskName() {
		if (ivjTFTaskName == null) {
			try {
				ivjTFTaskName = new nc.ui.pub.beans.UITextField();
				ivjTFTaskName.setName("TFTaskName");
				ivjTFTaskName.setBounds(48, 45, 158, 20);
				// user code begin {1}
				ivjTFTaskName.setDocument(new javax.swing.text.PlainDocument() {
					public void insertString(int offset, String str,
							javax.swing.text.AttributeSet attr)
							throws javax.swing.text.BadLocationException {
						if (str == null)
							return;
						if (str.indexOf('<') != -1 || str.indexOf('>') != -1
								|| str.indexOf('/') != -1)
							return;
						super.insertString(offset, str, attr);
					}
				});
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTFTaskName;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UPP102106-000166")/*
									 * @res "���ݴ���"
									 */;
	}

	/**
	 * ���� UITablePane1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITablePane getTPContens() {
		if (ivjTPContens == null) {
			try {
				ivjTPContens = new nc.ui.pub.beans.UITablePane();
				ivjTPContens.setName("TPContens");
				ivjTPContens.setPreferredSize(new java.awt.Dimension(300, 200));
				// user code begin {1}
				setTableModel();
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTPContens;
	}

	/**
	 * ���� UIPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getUIPanel1() {
		if (ivjUIPanel1 == null) {
			try {
				ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel1.setName("UIPanel1");
				ivjUIPanel1.setPreferredSize(new java.awt.Dimension(150, 190));
				ivjUIPanel1.setLayout(null);
				getUIPanel1().add(getLblTaskName(), getLblTaskName().getName());
				getUIPanel1().add(getLblTaskType(), getLblTaskType().getName());
				getUIPanel1().add(getTFTaskName(), getTFTaskName().getName());
				getUIPanel1().add(getCBBTaskType(), getCBBTaskType().getName());
				getUIPanel1().add(getLblTaskDesc(), getLblTaskDesc().getName());
				getUIPanel1().add(getSPTaskDesc(), getSPTaskDesc().getName());
				getUIPanel1().add(getTaskExecuteMode_lb(),
						getTaskExecuteMode_lb().getName());
				getUIPanel1().add(getTaskExecuteMode_chk(),
						getTaskExecuteMode_chk().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanel1;
	}

	/**
	 * ���� UIPanel2 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getUIPanel2() {
		if (ivjUIPanel2 == null) {
			try {
				ivjUIPanel2 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel2.setName("UIPanel2");
				ivjUIPanel2.setPreferredSize(new java.awt.Dimension(100, 100));
				ivjUIPanel2.setLayout(null);
				getUIPanel2().add(getBtnAdd(), getBtnAdd().getName());
				getUIPanel2().add(getBtnEdit(), getBtnEdit().getName());
				getUIPanel2().add(getBtnDel(), getBtnDel().getName());
				getUIPanel2().add(getBtnView(), getBtnView().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanel2;
	}

	/**
	 * ���� UIPanel3 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getUIPanel3() {
		if (ivjUIPanel3 == null) {
			try {
				ivjUIPanel3 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel3.setName("UIPanel3");
				ivjUIPanel3.setPreferredSize(new java.awt.Dimension(48, 10));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanel3;
	}

	/**
	 * ���� UISplitPane1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UISplitPane getUISplitPane1() {
		if (ivjUISplitPane1 == null) {
			try {
				ivjUISplitPane1 = new nc.ui.pub.beans.UISplitPane(1);
				ivjUISplitPane1.setName("UISplitPane1");
				ivjUISplitPane1.setDividerSize(3);
				ivjUISplitPane1.setDividerLocation(200);
				getUISplitPane1().add(getSPTaskList(), "left");
				getUISplitPane1().add(getRightSP(), "right");
				// user code begin {1}
				getUISplitPane1().setBorder(
						BorderFactory.createEmptyBorder(0, 3, 0, 3));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUISplitPane1;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		// System.out.println("--------- δ��׽�����쳣 ---------");
		// exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			initPreAlertButtons();
			// user code begin {1}
			setButtons(m_boGroup);
			// user code end
			setName("TransTaskSetupUI");
			setSize(774, 419);
			add(getUISplitPane1(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}

		m_iState = STATE_BROWSE;

		setButtonObjectEnable(m_iState);
		setUIEnable(m_iState);

	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-8-15</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void initPreAlertButtons() {
		// ��ʼ����Ŀ���ô���ť
		// m_boRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UPP102106-000167")/*
		// * @res
		// * "��Ŀ����"
		// */);
		// m_boRegistry.setHotKey("R");
		// m_boRegistry.setModifiers(InputEvent.CTRL_MASK
		// | InputEvent.SHIFT_MASK);
		// m_boRegistry.setDisplayHotkey("(Ctrl+Shift+R)");
		// m_boAddRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UC001-0000002")/*
		// * @res
		// * "����"
		// */);
		// m_boAddRegistry.setHotKey("I");
		// m_boAddRegistry.setModifiers(InputEvent.CTRL_MASK);
		// m_boAddRegistry.setDisplayHotkey("(Ctrl+I)");
		// m_boDelRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UC001-0000039")/*
		// * @res
		// * "ɾ��"
		// */);
		// m_boDelRegistry.setHotKey("D");
		// m_boDelRegistry.setModifiers(InputEvent.CTRL_MASK);
		// m_boDelRegistry.setDisplayHotkey("(Ctrl+D)");
		// m_boEditRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UC001-0000004")/*
		// * @res
		// * "�༭"
		// */);
		// m_boEditRegistry.setHotKey("F7");
		// m_boEditRegistry.setDisplayHotkey("(F7)");
		// m_boRefreshRegistrys = new nc.ui.pub.ButtonObject(
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
		// "UC001-0000009")/*
		// * @res "ˢ��"
		// */);
		// m_boRefreshRegistrys.setHotKey("R");
		// m_boRefreshRegistrys.setModifiers(InputEvent.CTRL_MASK);
		// m_boRefreshRegistrys.setDisplayHotkey("(Ctrl+R)");
		// m_boRegistry.addChildButton(m_boAddRegistry);
		// m_boRegistry.addChildButton(m_boDelRegistry);
		// m_boRegistry.addChildButton(m_boEditRegistry);
		// m_boRegistry.addChildButton(m_boRefreshRegistrys);
		// m_boBack = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UC001-0000038")/*
		// * @res
		// * "����"
		// */);
		// m_boBack.setHotKey("B");
		// m_boBack.setModifiers(InputEvent.CTRL_MASK);
		// m_boBack.setDisplayHotkey("(Ctrl+B)");

		m_boRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UPP102106-000167")/*
																		 * @res
																		 * "��Ŀ����"
																		 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UPP102106-000167")/*
									 * @res "��Ŀ����"
									 */, "��Ŀ����");

		m_boAddRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000002")/*
																	 * @res "����"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000002")/*
								 * @res "����"
								 */, "��Ŀ����");

		m_boDelRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000039")/*
																	 * @res "ɾ��"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000039")/*
								 * @res "ɾ��"
								 */, "��Ŀɾ��");

		m_boEditRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000004")/*
																	 * @res "�༭"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000004")/*
								 * @res "�༭"
								 */, "��Ŀ�༭");

		m_boRefreshRegistrys = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000009")/*
																	 * @res "ˢ��"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000009")/*
								 * @res "ˢ��"
								 */, "��Ŀˢ��");

		m_boRegistry.addChildButton(m_boAddRegistry);
		m_boRegistry.addChildButton(m_boDelRegistry);
		m_boRegistry.addChildButton(m_boEditRegistry);
		m_boRegistry.addChildButton(m_boRefreshRegistrys);
		m_boBack = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("102106", "UC001-0000038")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000038")/*
								 * @res "����"
								 */, "����");

	}

	/**
	 * ����µ����������Ƿ�����е������������ͬ ����ǣ�����false,���򷵻�true�� �������ڣ�(2003-3-5 10:03:42)
	 * 
	 * @return boolean
	 * @param taskName
	 *            java.lang.String
	 */
	private boolean isOnlyName(String taskName) {
		taskName = taskName.trim();
		TaskVO taskvo = getSelectedTaskVO();
		if (m_iState == STATE_MODIFY && taskvo.getTaskName().equals(taskName)) {

			return true;

		}
		boolean isOnly = true;
		Map map = getAllTask();
		if (map.get(taskName) != null) {
			isOnly = false;
		}

		return isOnly;
	}

	/** �÷�����Ӧ��������������ѡ���¼� */
	public void itemStateChanged(java.awt.event.ItemEvent e) {
		Object sour = e.getSource();
		if (sour.equals(getCBBTaskType())) {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				UITable table = getTPContens().getTable();
				VOTableModel model = (VOTableModel) table.getModel();
				if (model.getRowCount() > 0) {
					if (MessageDialog.showOkCancelDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000093")/*
														 * @res "����"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000168")/*
												 * @res "�����ڸı��������ͣ�����е����ݽ������"
												 */) == MessageDialog.ID_OK) {
						model.clearTable();
					} else {
						getCBBTaskType().removeItemListener(this);
						getCBBTaskType().setSelectedItem(m_cbbTaskTypePreSel);
						getCBBTaskType().addItemListener(this);
					}
				}
			} else if (e.getStateChange() == java.awt.event.ItemEvent.DESELECTED) {
				m_cbbTaskTypePreSel = e.getItem();
			}
		}
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			TransTaskSetupUI aTransTaskSetupUI;
			aTransTaskSetupUI = new TransTaskSetupUI();
			frame.setContentPane(aTransTaskSetupUI);
			frame.setSize(aTransTaskSetupUI.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.ToftPanel �� main() �з����쳣");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * 
	 * @version (00-6-1 10:32:59)
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == m_boAdd) { // �½�����
			doBOAddTask();
		} else if (bo == m_boCancel) { // ȡ���޸�
			doBOCancelTask();
		} else if (bo == m_boCopy) { // ��������
			doBOCopyTask();
		} else if (bo == m_boDelete) { // ɾ������
			doBODel();
		} else if (bo == m_boExecute) { // ִ������
			doBOExecTask();
			this.doUpdateDate();
		} else if (bo == m_boSave) { // ��������
			doBOSaveTask();
		} else if (bo == m_boUpdate) { // �޸�����
			doBOUpdateTask();
		} else if (bo == m_boRefresh) { // ˢ�������б�
			doBORefresh();
		} else if (bo == m_boTemplet) { // �༭����ģ��
			doBOTemplet();
		} else if (bo == m_boLog) { // �鿴��־
			doBOLog();
		} else if (bo == m_boSwitch) { // �л���Ԥ������
			doBoSwitch();
		} else if (bo == m_boAddRegistry) {
			getPreAlertUI().onAddRegistryButtonClick();
		} else if (bo == m_boDelRegistry) {
			getPreAlertUI().onDelRegistryButtonClick();
		} else if (bo == m_boEditRegistry) {
			getPreAlertUI().onEditRegistryButtonClick();
		} else if (bo == m_boRefreshRegistrys) {
			getPreAlertUI().onRefreshRegistrysButtonClick();
		} else if (bo == m_boBack) {
			remove(getPreAlertUI().getUIPanel3());
			add(getUISplitPane1(), "Center");
			setButtons(m_boGroup);
			setTitleText(getTitle());
			this.repaint(this.getVisibleRect());
		}
	}

	/**
	 * ����һ��buttonObject �Ŀ���״̬
	 */
	private void setButton(nc.ui.pub.ButtonObject bo, boolean bool) {
		bo.setEnabled(bool);
		updateButton(bo);

	}

	/**
	 * m_boAdd, m_boUpdate, m_boCopy, m_boDelete, m_boSave, m_boCancel,
	 * m_boExecute �� �������ڣ�(2003-3-4 18:08:34)
	 * 
	 * @param state
	 *            int
	 */
	private void setButtonObjectEnable(int state) {
		if (state == STATE_BROWSE) {
			// �����Ƿ�����
			if (isTaskLock()) {
				setButton(m_boAdd, false);
				setButton(m_boUpdate, false);
				setButton(m_boCopy, false);
				setButton(m_boDelete, false);
				setButton(m_boSave, false);
				setButton(m_boCancel, false);
				setButton(m_boExecute, true);
				setButton(m_boRefresh, true);
				setButton(m_boTemplet, true);

			} else {
				setButton(m_boAdd, true);
				setButton(m_boUpdate, true);
				setButton(m_boCopy, true);
				setButton(m_boDelete, true);
				setButton(m_boSave, false);
				setButton(m_boCancel, false);
				setButton(m_boExecute, true);
				setButton(m_boRefresh, true);
				setButton(m_boTemplet, true);
			}

		} else if (state == STATE_ADDNEW || state == STATE_MODIFY) {
			setButton(m_boAdd, false);
			setButton(m_boUpdate, false);
			setButton(m_boCopy, false);
			setButton(m_boDelete, false);
			setButton(m_boSave, true);
			setButton(m_boCancel, true);
			setButton(m_boExecute, false);
			setButton(m_boRefresh, false);
			setButton(m_boTemplet, false);
		} else if (state == STATE_EXECTASK) {
			setButton(m_boAdd, false);
			setButton(m_boUpdate, false);
			setButton(m_boCopy, false);
			setButton(m_boDelete, false);
			setButton(m_boSave, false);
			setButton(m_boCancel, false);
			setButton(m_boExecute, false);
			setButton(m_boRefresh, false);
			setButton(m_boTemplet, false);

		}
	}

	/**
	 * ��һ�����������ұ������档 �������ڣ�(2003-3-4 19:38:42)
	 * 
	 * @param task
	 *            nc.vo.uap.dbtrans.TaskVO
	 */
	private void setRightPanelByTask(TaskVO task) {
		if (task == null) {
			getTFTaskName().setText("");
			getTATaskDesc().setText("");
			getCBBTaskType().setSelectedIndex(0);
			getTaskExecuteMode_chk().setSelected(false);
			((VOTableModel) getTPContens().getTable().getModel()).clearTable();
		} else {
			getTFTaskName().setText(task.getTaskName());
			getTATaskDesc().setText(task.getTaskDesc());
			getCBBTaskType().setSelectedIndex(task.getTaskType());
			getTaskExecuteMode_chk().setSelected(task.isDebugExecute());
			((VOTableModel) getTPContens().getTable().getModel()).clearTable();
			((VOTableModel) getTPContens().getTable().getModel()).addVO(task
					.getTransContents());
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-2-22 10:20:14)
	 */
	private void setTableModel() {
		UITable table = getTPContens().getTable();
		table.setModel(new VOTableModel(TransContentVO.class) {
			String[] titles = new String[] {
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000170")/*
												 * @res "Դ���ݿ�"
												 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000171")/*
												 * @res "Ŀ�����ݿ�"
												 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000172") /*
												 * @res "���䷽ʽ"
												 */};

			public String getColumnName(int col) {
				return titles[col];
			}

			public int getColumnCount() {
				return titles.length;
			}

			public Object getValueAt(int row, int col) {
				TransContentVO vo = (TransContentVO) getVO(row);
				switch (col) {
				case 0:
					return vo.getSourDB();
				case 1:
					return vo.getDestDB();
				case 2:
					return vo.getTransMode();
				}
				return null;
			}

		});
	}

	/**
	 * ������������Ŀ����ԡ� �������ڣ�(2003-3-4 20:08:03)
	 * 
	 * @param state
	 *            int
	 */
	private void setUIEnable(int state) {
		if (state == STATE_BROWSE) {
			getUItree().setEnabled(true);
			getTFTaskName().setEnabled(false);
			getTATaskDesc().setEnabled(false);
			getCBBTaskType().setEnabled(false);
			getTaskExecuteMode_chk().setEnabled(false);
			getBtnAdd().setEnabled(false);
			getBtnDel().setEnabled(false);
			getBtnEdit().setEnabled(false);
			getBtnView().setEnabled(true);
			getCBBTaskType().removeItemListener(this);
		} else if (state == STATE_ADDNEW || state == STATE_MODIFY) {
			getUItree().setEnabled(false);
			getTFTaskName().setEnabled(true);
			getTATaskDesc().setEnabled(true);
			getCBBTaskType().setEnabled(true);
			getTaskExecuteMode_chk().setEnabled(true);
			getBtnAdd().setEnabled(true);
			getBtnDel().setEnabled(true);
			getBtnEdit().setEnabled(true);
			getBtnView().setEnabled(false);
			getCBBTaskType().addItemListener(this);
		} else if (state == STATE_EXECTASK) {
			getUItree().setEnabled(false);
			getTFTaskName().setEnabled(false);
			getTATaskDesc().setEnabled(false);
			getCBBTaskType().setEnabled(false);
			getTaskExecuteMode_chk().setEnabled(false);
			getBtnAdd().setEnabled(false);
			getBtnDel().setEnabled(false);
			getBtnEdit().setEnabled(false);
			getBtnView().setEnabled(false);
			getCBBTaskType().removeItemListener(this);
		}
	}

	/**
	 * @return ���� taskExecuteMode_chk��
	 */
	private UICheckBox getTaskExecuteMode_chk() {

		if (taskExecuteMode_chk == null) {
			taskExecuteMode_chk = new UICheckBox();
			taskExecuteMode_chk.setSelected(false);
			taskExecuteMode_chk.setName("taskExecuteMode_chk");
			taskExecuteMode_chk.setBounds(48, 153, 150, 22);
			taskExecuteMode_chk.setText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPT102106-000212"/* "����ִ��" */));

		}
		return taskExecuteMode_chk;
	}

	/**
	 * @return ���� taskExecuteMode_lb��
	 */
	private UILabel getTaskExecuteMode_lb() {
		if (taskExecuteMode_lb == null) {
			taskExecuteMode_lb = new UILabel();
			taskExecuteMode_lb.setName("taskExecuteMode_lb");
			taskExecuteMode_lb.setBounds(48, 128, 200, 22);
			taskExecuteMode_lb.setText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPT102106-000211"/* ����ִ�з�ʽ */));

		}
		return taskExecuteMode_lb;
	}

	private TreePanel getTreePanel() {
		if (treePanel == null) {

			treePanel = new nc.ui.uap.dbtrans.TreePanel(Constant.TASK);
			treePanel.setName("TempletListPanel");
			treePanel.setPreferredSize(new java.awt.Dimension(150, 153));
			treePanel.setBounds(0, 0, 160, 120);

			treePanel.displayBtnAddNewFolder();
			treePanel.getUITree().addTreeSelectionListener(this);

		}
		return treePanel;
	}

	public void valueChanged(TreeSelectionEvent e) {

		TreePath path = e.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		if (node.getUserObject() instanceof TaskVO) {

			TaskVO task = (TaskVO) node.getUserObject();
			setRightPanelByTask((TaskVO) task.clone());

		} else {
			setRightPanelByTask(null);
		}

	}

	private UITree getUItree() {
		return getTreePanel().getUITree();
	}

	private DefaultMutableTreeNode getSelectedNode() {

		TreePath path = getUItree().getSelectionPath();
		if (path == null) {
			return null;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		return node;
	}

	private TaskVO getSelectedTaskVO() {
		TaskVO vo = null;

		DefaultMutableTreeNode node = getSelectedNode();

		if (node != null && node.getUserObject() instanceof TaskVO) {

			vo = (TaskVO) node.getUserObject();

		}
		return vo;

	}

	private String getPath() {
		// TreePath treePath = getUItree().getSelectionPath();
		// if (treePath == null) {
		// return null;
		// }
		// DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) treePath
		// .getLastPathComponent();
		return TransCommons.getPath(getUItree());
	}

	private void refreshTree() {
		getTreePanel().loadTree();
	}

	private Map getAllTask() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getUItree()
				.getModel().getRoot();

		Map map = new HashMap();
		for (Enumeration e = node.breadthFirstEnumeration(); e
				.hasMoreElements();) {
			Object obj = ((DefaultMutableTreeNode) e.nextElement())
					.getUserObject();
			if (obj instanceof TaskVO) {
				TaskVO vo = (TaskVO) obj;
				map.put(vo.getTaskName(), vo);
			}

		}
		return map;
	}

	/**
	 * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
	 * 
	 * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա�
	 * 
	 * �������ڣ�(2004-6-4 08:30:37)
	 */
	public boolean onClosing() {

		if (m_iState == STATE_ADDNEW || m_iState == STATE_MODIFY) {
			int i = showYesNoMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory", "UPPuifactory-000058")/*
																	 * @res
																	 * "���ڱ༭״̬��δ��������ݽ��ᶪʧ���Ƿ��˳���"
																	 */);
			if (i == nc.ui.pub.beans.MessageDialog.ID_YES) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	private boolean isTaskLock() {
		UFBoolean value = UFBoolean.FALSE;
		// �����л���
		try {
			value = SysInitBO_Client.getParaBoolean("0001", "SM007");
		} catch (Exception e) {
			Debug.debug("error occur! when getting Parameter SM007. ");
		}

		return value.booleanValue();
	}

	private void doUpdateDate() {

		TaskVO selVO = this.getSelectedTaskVO();
		
		// ִ����������ڴ���ʱ��
		if (selVO.getTaskName().equals("��������")) {
			String lastts=selVO.getTransContents()[0].getTableAndSqlVOs()[0].getLastTS();
			String currts=ClientEnvironment.getServerTime().toString();
			IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
					IbxgtQuerySynchro.class);
			try {
				ibqs.updateBasedoc(lastts,currts);
			} catch (Exception e) {

			}
		}
	}
}