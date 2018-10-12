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
 * 数据传输UI界面。 创建日期：(2003-2-21 15:08:46)
 * 
 * @author：李充蒲
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

	// 任务执行方式
	private UILabel taskExecuteMode_lb = null;

	private UICheckBox taskExecuteMode_chk = null;

	private TreePanel treePanel = null;

	//
	private nc.ui.pub.ButtonObject m_boAdd = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000002")/*
									 * @res "增加"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000125")/*
										 * @res "增加新任务"
										 */, "增加");

	private nc.ui.pub.ButtonObject m_boUpdate = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000045")/*
									 * @res "修改"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000126")/*
										 * @res "修改选中任务"
										 */, "修改");

	private nc.ui.pub.ButtonObject m_boCopy = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000043")/*
									 * @res "复制"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000127")/*
										 * @res "复制选中任务"
										 */, "复制");

	private nc.ui.pub.ButtonObject m_boDelete = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000039")/*
									 * @res "删除"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000128")/*
										 * @res "删除选中的任务"
										 */, "删除");

	private nc.ui.pub.ButtonObject m_boSave = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000001")/*
									 * @res "保存"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000129")/*
										 * @res "保存任务"
										 */, "保存");

	private nc.ui.pub.ButtonObject m_boCancel = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000008")/*
									 * @res "取消"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000130")/*
										 * @res "取消所作改动"
										 */, "取消");

	private nc.ui.pub.ButtonObject m_boExecute = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000026")/*
									 * @res "执行"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000131")/*
										 * @res "立即执行选中任务"
										 */, "执行");

	private nc.ui.pub.ButtonObject m_boRefresh = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UC001-0000009")/*
									 * @res "刷新"
									 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000132")/*
										 * @res "刷新任务列表"
										 */, "刷新");

	private nc.ui.pub.ButtonObject m_boTemplet = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPT102106-000002")/*
										 * @res "对象"
										 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000133")/*
										 * @res "编辑对象"
										 */, "对象");

	private nc.ui.pub.ButtonObject m_boLog = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000134")/*
										 * @res "日志"
										 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPT102106-000001")/*
										 * @res "查看日志"
										 */, "日志");

	private nc.ui.pub.ButtonObject m_boSwitch = new nc.ui.pub.ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPT102106-000003")/*
										 * @res "任务预警"
										 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000135")/*
										 * @res "切换到预警平台设置界面"
										 */, "任务预警");

	private nc.ui.pub.ButtonObject[] m_boGroup = new nc.ui.pub.ButtonObject[] {
			m_boAdd, m_boUpdate, m_boCopy, m_boDelete, m_boSave, m_boCancel,
			m_boRefresh, m_boTemplet, m_boExecute, m_boLog, m_boSwitch };

	// 条目类型按钮
	private nc.ui.pub.ButtonObject m_boRegistry = null;

	private nc.ui.pub.ButtonObject m_boAddRegistry = null;

	private nc.ui.pub.ButtonObject m_boDelRegistry = null;

	private nc.ui.pub.ButtonObject m_boEditRegistry = null;

	private nc.ui.pub.ButtonObject m_boRefreshRegistrys = null;

	private nc.ui.pub.ButtonObject m_boBack = null;

	//
	private UIButton ivjBtnAdd = null;

	private UIButton ivjBtnDel = null;

	// 状态
	private static final int STATE_BROWSE = 0;

	private static final int STATE_ADDNEW = 1;

	private static final int STATE_MODIFY = 2;

	private static final int STATE_EXECTASK = 3;

	private int m_iState = STATE_BROWSE;

	//
	private Object m_cbbTaskTypePreSel = null;

	// 预警平台界面
	private ConfigToftPanel m_cfgPanel = null; // new

	// nc.ui.pub.pa.ConfigToftPanel("数据传输任务预警");

	/**
	 * TransTaskSetupUI 构造子注解。
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
	 * 根据任务的类型来处理表格中的其它纪录。 创建日期：(2003-3-4 16:07:18)
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
	 * 增加一个新的数据传输任务。 创建日期：(2003-3-4 19:21:19)
	 */
	private void doBOAddTask() {
		m_iState = STATE_ADDNEW;
		setButtonObjectEnable(m_iState);
		setUIEnable(m_iState);
		setRightPanelByTask(null);
	}

	/**
	 * 取消操作。 创建日期：(2003-3-4 19:21:32)
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
	 * 复制一个任务。 创建日期：(2003-3-4 19:21:44)
	 */
	private void doBOCopyTask() {
		TaskVO selVO = getSelectedTaskVO();

		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "提示"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000136")/*
										 * @res "请先选中一条任务再执行复制操作"
										 */);
			return;
		}

		String taskName = selVO.getTaskName();
		String newName = null;
		do {
			newName = (String) MessageDialog.showInputDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000137")/*
												 * @res "复制选中的任务"
												 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000138")/*
												 * @res "请输入新的任务名称"
												 */, taskName + " COPY");
			if (newName != null) {
				newName = newName.trim();
				if (newName.length() <= 0) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "错误"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000139")/*
												 * @res "输入的任务名称不能为空"
												 */);
				} else if (!isOnlyName(newName)) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "错误"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000140")/*
												 * @res "输入的任务名称不唯一，请重新输入"
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
																				 * "错误"
																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
						"UPP102106-000141", null,
						new String[] { e.getMessage() }));// /*@res
				// "复制任务时发生例外：
				// {0}"*/+e.getMessage());
			}
		}

	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong> 设置时间戳是否归零
	 * <p>
	 * <strong>最后修改日期：2006-7-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return TaskVO
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setTs(TaskVO taskVO) {
		boolean isUpdateTs = false;
		isUpdateTs = MessageDialog.showYesNoDlg(this, "提示", "时间戳是否归零?") == MessageDialog.ID_YES;

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
					.getStrByID("102106", "UPP102106-000025")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000092")/* @res "请先选中再执行删除操作" */);
			return;
		}
		if (MessageDialog
				.showOkCancelDlg(this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000093")/* @res "警告" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000094")/* @res "真的要删除吗？" */) == MessageDialog.ID_OK) {
			try {

				// DefaultTreeModel model = (DefaultTreeModel) getUItree()
				// .getModel();

				boolean succ = SFAppServiceUtil.getTransTaskService(null)
						.deleteDirectory(getPath(), Constant.TASK);
				// 更新界面
				if (succ) {
					// model.removeNodeFromParent(selNode);
					doBORefresh();
				} else {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "错误"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000095")/*
												 * @res "删除失败"
												 */);
				}

			} catch (Exception e) {
				Debug.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000017")/* @res "错误" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000096", null,
								new String[] { e.getMessage() }));// /*@res
				// "执行删除操作时发生例外：{0}"*/
				// +
				// e.getMessage());
			}
		}

	}

	/**
	 * 删除一个任务。 创建日期：(2003-3-4 19:21:55)
	 */
	private void delTask() {

		TaskVO selVO = getSelectedTaskVO();
		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "提示"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000142")/*
										 * @res "请先选中一条任务再执行删除操作"
										 */);
			return;
		}

		if (MessageDialog.showOkCancelDlg(this, nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UPP102106-000093")/*
																		 * @res
																		 * "警告"
																		 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UPP102106-000143")/*
									 * @res "真的要删除选中的任务吗？"
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
														 * @res "错误"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000144")/*
												 * @res "删除任务失败"
												 */);
				}
			} catch (Exception e) {
				Debug.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("102106", "UPP102106-000017")/*
																				 * @res
																				 * "错误"
																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
						"UPP102106-000145", null,
						new String[] { e.getMessage() }));// /*@res
				// "执行删除任务操作时发生例外：
				// {0}"*/
				// +
				// e.getMessage());
			}
		}

	}

	/**
	 * 执行一个任务。 创建日期：(2003-3-4 19:22:04)
	 */
	private void doBOExecTask() {
		final TaskVO selVO = getSelectedTaskVO();
		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "提示"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000146")/*
										 * @res "请先选中一条任务再执行"
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
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-9-5</strong>
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
								.getStrByID("102106", "UPT102106-000210")/* 正在后台执行任务请稍候 */);
				try {
					dialog.start();
					// 执行任务
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
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-9-5</strong>
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
			// 显示输入参数的面板
			TransContentVO[] needParaTransContentVOs = selVO
					.getNeedParaContentVOs();
			if (needParaTransContentVOs != null
					&& needParaTransContentVOs.length > 0) {
				ParameterInputDlg paraDlg = new ParameterInputDlg(
						nc.ui.uap.dbtrans.TransTaskSetupUI.this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
								"UPP102106-000147")/*
													 * @res "参数输入对话框"
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
																			 * "执行任务"
																			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000148")/*
										 * @res "执行任务"
										 */
					+ selVO.getTaskName()
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000149")/*
												 * @res "完成，执行情况请查看日志"
												 */);
		} catch (Exception e) {
			MessageDialog.showHintDlg(TransTaskSetupUI.this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("102106", "UPP102106-000148")/*
																			 * @res
																			 * "执行任务"
																			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000150", null,
					new String[] { selVO.getTaskName(), e.getMessage() }));// /*@res
			// "执行任务
			// {0}失败：
			// {1}"*/+selVO.getTaskName()+"失败："+e.getMessage());
			Debug.error(e.getMessage(), e);
		} finally {
			setButtonObjectEnable(TransTaskSetupUI.STATE_BROWSE);
		}
	}

	/**
	 * 查看日志。 创建日期：(2003-3-17 11:11:18)
	 */
	private void doBOLog() {
		
		String str = "";
		try {
			str = SFAppServiceUtil.getTranExecTaskService(null).getLogText()
					.toString();
			LogView v = new LogView(new String[] { str }, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("102106", "UPP102106-000151")/*
																			 * @res
																			 * "日志文件记录"
																			 */);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * 刷新操作。 创建日期：(2003-3-4 19:21:32)
	 */
	private void doBORefresh() {
		refreshTree();
		setRightPanelByTask(null);

	}

	/**
	 * 保存一个任务。 创建日期：(2003-3-4 19:22:15)
	 */
	private void doBOSaveTask() {
		TaskVO newVO = getTaskFromRightPanel();
		// 检查任务的合法性
		try {
			newVO.validate();
		} catch (nc.vo.pub.ValidationException e) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000017")/*
																 * @res "错误"
																 */, e.getMessage());
			return;
		}

		// 保存
		try {
			if (m_iState == STATE_ADDNEW) { // 保存一个新建的任务
				// 检查任务名称是否唯一
				if (!isOnlyName(newVO.getTaskName())) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "错误"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000152")/*
												 * @res "输入的任务名称不唯一，请重新命名。"
												 */);
					return;
				}
				// 保存任务到服务器上
				newVO.setFilePath(getPath());
				SFAppServiceUtil.getTransTaskService(null).addNew(newVO);
				// 增加任务到界面的任务列表中
				refreshTree();
			} else if (m_iState == STATE_MODIFY) { // 保存对一个任务的修改
				// 修改界面上的任务
				if (!isOnlyName(newVO.getTaskName())) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("102106",
									"UPP102106-000017")/*
														 * @res "错误"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000153")/*
												 * @res "任务名称不唯一，请重新命名。"
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
				// 设置时间戳是否归零
				setTs(selVO);
				// 修改服务器上的任务
				SFAppServiceUtil.getTransTaskService(null).update(selVO);
			}
			// 设置界面
			m_iState = STATE_BROWSE;
			setButtonObjectEnable(m_iState);
			setUIEnable(m_iState);
			//
		} catch (Exception e) {
			Debug.debug(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000017")/*
																 * @res "错误"
																 */, e.getMessage());
			return;
		}

	}

	/**
	 * 切换到预警平台界面。 创建日期：(2003-3-19 8:52:13)
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
									 * @res "数据传输任务预警条目设置界面"
									 */);
		this.repaint(this.getVisibleRect());
	}

	/**
	 * 编辑模板。 创建日期：(2003-3-5 14:36:10)
	 */
	private void doBOTemplet() {
		TempletSetupDlg dlg = new TempletSetupDlg(this, nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UPP102106-000155")/*
																		 * @res
																		 * "对象编辑对话框"
																		 */);
		dlg.showModal();
	}

	/**
	 * 修改一个指定任务。 创建日期：(2003-3-4 19:22:33)
	 */
	private void doBOUpdateTask() {
		TaskVO selVO = getSelectedTaskVO();
		if (selVO == null) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "提示"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000156")/*
										 * @res "请先选中一个任务再执行修改操作！"
										 */);
			return;
		}
		m_iState = STATE_MODIFY;
		setButtonObjectEnable(m_iState);
		setUIEnable(m_iState);
	}

	/**
	 * 响应增加按钮的点击事件。增加一条传输内容纪录 创建日期：(2003-2-22 11:05:49)
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
	 * 响应删除按钮的点击事件。删除一条传输内容纪录 创建日期：(2003-2-22 11:05:49)
	 */
	private void doBtnDel() {
		UITable table = getTPContens().getTable();
		int index = table.getSelectedRow();
		if (index == -1) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "提示"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000157")/*
										 * @res "请先选中一条记录再执行删除操作"
										 */);
			return;
		} else {
			if (MessageDialog.showOkCancelDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("102106", "UPP102106-000093")/*
																			 * @res
																			 * "警告"
																			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000083")/*
										 * @res "真的要删除选中的记录吗？"
										 */) == MessageDialog.ID_OK) {
				VOTableModel model = (VOTableModel) table.getModel();
				model.removeVO(index);
			}
		}
	}

	/**
	 * 响应编辑按钮的点击事件。编辑一条传输内容纪录 创建日期：(2003-2-22 11:05:49)
	 */
	private void doBtnEdit() {
		UITable table = getTPContens().getTable();
		int index = table.getSelectedRow();
		if (index == -1) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "提示"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000158")/*
										 * @res "请先选中一条记录再执行编辑操作"
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
	 * 响应察看按钮的点击事件。浏览一条传输内容纪录 创建日期：(2003-2-22 11:06:03)
	 */
	private void doBtnView() {
		UITable table = getTPContens().getTable();
		int index = table.getSelectedRow();
		if (index == -1) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPP102106-000025")/*
																 * @res "提示"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
					"UPP102106-000159")/*
										 * @res "请先选中一条记录再执行浏览操作"
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
	 * 返回 BtnAdd 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBtnAdd() {
		if (ivjBtnAdd == null) {
			try {
				ivjBtnAdd = new nc.ui.pub.beans.UIButton();
				ivjBtnAdd.setName("BtnAdd");
				HotkeyUtil.setHotkeyAndText(ivjBtnAdd, 'A', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000002"));/*
													 * @res "增加"
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
	 * 返回 BtnDel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBtnDel() {
		if (ivjBtnDel == null) {
			try {
				ivjBtnDel = new nc.ui.pub.beans.UIButton();
				ivjBtnDel.setName("BtnDel");
				HotkeyUtil.setHotkeyAndText(ivjBtnDel, 'D', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000039"));/*
													 * @res "删除"
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
	 * 返回 BtnDetail 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBtnEdit() {
		if (ivjBtnEdit == null) {
			try {
				ivjBtnEdit = new nc.ui.pub.beans.UIButton();
				ivjBtnEdit.setName("BtnEdit");
				ivjBtnEdit.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000160")/*
																	 * @res
																	 * "编辑表格内容"
																	 */);
				HotkeyUtil.setHotkeyAndText(ivjBtnEdit, 'E', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000004"));/*
													 * @res "编辑"
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
	 * 返回 BtnView 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBtnView() {
		if (ivjBtnView == null) {
			try {
				ivjBtnView = new nc.ui.pub.beans.UIButton();
				ivjBtnView.setName("BtnView");
				ivjBtnView.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000161")/*
																	 * @res
																	 * "浏览详细情况"
																	 */);
				HotkeyUtil.setHotkeyAndText(ivjBtnView, 'V', nc.ui.ml.NCLangRes
						.getInstance().getStrByID(
								IProductCode.PRODUCTCODE_COMMON,
								"UC001-0000021"));/*
													 * @res "浏览"
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
	 * 返回 CBBTaskType 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 LblTaskDesc 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLblTaskDesc() {
		if (ivjLblTaskDesc == null) {
			try {
				ivjLblTaskDesc = new nc.ui.pub.beans.UILabel();
				ivjLblTaskDesc.setName("LblTaskDesc");
				ivjLblTaskDesc.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000162")/*
																	 * @res
																	 * "任务描述："
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
	 * 返回 LblTaskName 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLblTaskName() {
		if (ivjLblTaskName == null) {
			try {
				ivjLblTaskName = new nc.ui.pub.beans.UILabel();
				ivjLblTaskName.setName("LblTaskName");
				ivjLblTaskName.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000163")/*
																	 * @res
																	 * "任务名称："
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
	 * 返回 LblTaskType 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLblTaskType() {
		if (ivjLblTaskType == null) {
			try {
				ivjLblTaskType = new nc.ui.pub.beans.UILabel();
				ivjLblTaskType.setName("LblTaskType");
				ivjLblTaskType.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("102106", "UPP102106-000164")/*
																	 * @res
																	 * "任务类型："
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
	 * 获得预警平台的界面。 创建日期：(2002-4-5 12:58:57)
	 * 
	 * @return nc.ui.pub.pa.ConfigToftPanel
	 */
	private ConfigToftPanel getPreAlertUI() {
		if (m_cfgPanel == null) {
			// m_cfgPanel = new ConfigToftPanel(nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("102106", "UPP102106-000165")/*
			// * @res
			// * "数据传输任务预警"
			// */);

			// m_cfgPanel = new ConfigToftPanel(nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("prealerttype", "UAP-datatransfer")/*
			// * @res
			// * "数据传输任务预警"
			// */);
			m_cfgPanel = new ConfigToftPanel(
					"nc.bs.uap.dbtrans.TransTaskPAPlugIn");

		}
		return m_cfgPanel;
	}

	/**
	 * 返回 RightPanel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 RightSP 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 SPTaskDesc 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 SPTaskList 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
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
													 * @res "任务列表"
													 */);
				lab.setILabelType(0);// java 自定义
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
	 * 此处插入方法说明。 创建日期：(2003-3-4 19:45:26)
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
	 * 返回 TATaskDesk 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 TFTaskName 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UPP102106-000166")/*
									 * @res "数据传输"
									 */;
	}

	/**
	 * 返回 UITablePane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 UIPanel1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 UIPanel2 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 UIPanel3 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 UISplitPane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// System.out.println("--------- 未捕捉到的异常 ---------");
		// exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
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
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-15</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void initPreAlertButtons() {
		// 初始化条目配置处理按钮
		// m_boRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UPP102106-000167")/*
		// * @res
		// * "条目配置"
		// */);
		// m_boRegistry.setHotKey("R");
		// m_boRegistry.setModifiers(InputEvent.CTRL_MASK
		// | InputEvent.SHIFT_MASK);
		// m_boRegistry.setDisplayHotkey("(Ctrl+Shift+R)");
		// m_boAddRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UC001-0000002")/*
		// * @res
		// * "增加"
		// */);
		// m_boAddRegistry.setHotKey("I");
		// m_boAddRegistry.setModifiers(InputEvent.CTRL_MASK);
		// m_boAddRegistry.setDisplayHotkey("(Ctrl+I)");
		// m_boDelRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UC001-0000039")/*
		// * @res
		// * "删除"
		// */);
		// m_boDelRegistry.setHotKey("D");
		// m_boDelRegistry.setModifiers(InputEvent.CTRL_MASK);
		// m_boDelRegistry.setDisplayHotkey("(Ctrl+D)");
		// m_boEditRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
		// .getInstance().getStrByID("102106", "UC001-0000004")/*
		// * @res
		// * "编辑"
		// */);
		// m_boEditRegistry.setHotKey("F7");
		// m_boEditRegistry.setDisplayHotkey("(F7)");
		// m_boRefreshRegistrys = new nc.ui.pub.ButtonObject(
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
		// "UC001-0000009")/*
		// * @res "刷新"
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
		// * "返回"
		// */);
		// m_boBack.setHotKey("B");
		// m_boBack.setModifiers(InputEvent.CTRL_MASK);
		// m_boBack.setDisplayHotkey("(Ctrl+B)");

		m_boRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UPP102106-000167")/*
																		 * @res
																		 * "条目配置"
																		 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UPP102106-000167")/*
									 * @res "条目配置"
									 */, "条目配置");

		m_boAddRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000002")/*
																	 * @res "增加"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000002")/*
								 * @res "增加"
								 */, "条目增加");

		m_boDelRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000039")/*
																	 * @res "删除"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000039")/*
								 * @res "删除"
								 */, "条目删除");

		m_boEditRegistry = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000004")/*
																	 * @res "编辑"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000004")/*
								 * @res "编辑"
								 */, "条目编辑");

		m_boRefreshRegistrys = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes
				.getInstance().getStrByID("102106", "UC001-0000009")/*
																	 * @res "刷新"
																	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000009")/*
								 * @res "刷新"
								 */, "条目刷新");

		m_boRegistry.addChildButton(m_boAddRegistry);
		m_boRegistry.addChildButton(m_boDelRegistry);
		m_boRegistry.addChildButton(m_boEditRegistry);
		m_boRegistry.addChildButton(m_boRefreshRegistrys);
		m_boBack = new nc.ui.pub.ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("102106", "UC001-0000038")/*
														 * @res "返回"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
				"UC001-0000038")/*
								 * @res "返回"
								 */, "返回");

	}

	/**
	 * 检查新的任务名称是否和已有的任务的名称相同 如果是，返回false,否则返回true。 创建日期：(2003-3-5 10:03:42)
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

	/** 该方法响应任务类型下拉框选择事件 */
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
														 * @res "警告"
														 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000168")/*
												 * @res "你正在改变任务类型，表格中的数据将被清除"
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
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
			System.err.println("nc.ui.pub.ToftPanel 的 main() 中发生异常");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == m_boAdd) { // 新建任务
			doBOAddTask();
		} else if (bo == m_boCancel) { // 取消修改
			doBOCancelTask();
		} else if (bo == m_boCopy) { // 复制任务
			doBOCopyTask();
		} else if (bo == m_boDelete) { // 删除任务
			doBODel();
		} else if (bo == m_boExecute) { // 执行任务
			doBOExecTask();
			this.doUpdateDate();
		} else if (bo == m_boSave) { // 保存任务
			doBOSaveTask();
		} else if (bo == m_boUpdate) { // 修改任务
			doBOUpdateTask();
		} else if (bo == m_boRefresh) { // 刷新任务列表
			doBORefresh();
		} else if (bo == m_boTemplet) { // 编辑任务模板
			doBOTemplet();
		} else if (bo == m_boLog) { // 查看日志
			doBOLog();
		} else if (bo == m_boSwitch) { // 切换到预警界面
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
	 * 设置一个buttonObject 的可用状态
	 */
	private void setButton(nc.ui.pub.ButtonObject bo, boolean bool) {
		bo.setEnabled(bool);
		updateButton(bo);

	}

	/**
	 * m_boAdd, m_boUpdate, m_boCopy, m_boDelete, m_boSave, m_boCancel,
	 * m_boExecute 。 创建日期：(2003-3-4 18:08:34)
	 * 
	 * @param state
	 *            int
	 */
	private void setButtonObjectEnable(int state) {
		if (state == STATE_BROWSE) {
			// 任务是否锁定
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
	 * 用一个任务设置右边面板界面。 创建日期：(2003-3-4 19:38:42)
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
	 * 此处插入方法说明。 创建日期：(2003-2-22 10:20:14)
	 */
	private void setTableModel() {
		UITable table = getTPContens().getTable();
		table.setModel(new VOTableModel(TransContentVO.class) {
			String[] titles = new String[] {
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000170")/*
												 * @res "源数据库"
												 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000171")/*
												 * @res "目标数据库"
												 */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("102106",
							"UPP102106-000172") /*
												 * @res "传输方式"
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
	 * 设置整个界面的可用性。 创建日期：(2003-3-4 20:08:03)
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
	 * @return 返回 taskExecuteMode_chk。
	 */
	private UICheckBox getTaskExecuteMode_chk() {

		if (taskExecuteMode_chk == null) {
			taskExecuteMode_chk = new UICheckBox();
			taskExecuteMode_chk.setSelected(false);
			taskExecuteMode_chk.setName("taskExecuteMode_chk");
			taskExecuteMode_chk.setBounds(48, 153, 150, 22);
			taskExecuteMode_chk.setText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPT102106-000212"/* "调试执行" */));

		}
		return taskExecuteMode_chk;
	}

	/**
	 * @return 返回 taskExecuteMode_lb。
	 */
	private UILabel getTaskExecuteMode_lb() {
		if (taskExecuteMode_lb == null) {
			taskExecuteMode_lb = new UILabel();
			taskExecuteMode_lb.setName("taskExecuteMode_lb");
			taskExecuteMode_lb.setBounds(48, 128, 200, 22);
			taskExecuteMode_lb.setText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("102106", "UPT102106-000211"/* 任务执行方式 */));

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
	 * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
	 * 
	 * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。
	 * 
	 * 创建日期：(2004-6-4 08:30:37)
	 */
	public boolean onClosing() {

		if (m_iState == STATE_ADDNEW || m_iState == STATE_MODIFY) {
			int i = showYesNoMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory", "UPPuifactory-000058")/*
																	 * @res
																	 * "正在编辑状态，未保存的数据将会丢失，是否退出？"
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
		// 参数有缓存
		try {
			value = SysInitBO_Client.getParaBoolean("0001", "SM007");
		} catch (Exception e) {
			Debug.debug("error occur! when getting Parameter SM007. ");
		}

		return value.booleanValue();
	}

	private void doUpdateDate() {

		TaskVO selVO = this.getSelectedTaskVO();
		
		// 执行完任务后期处理时间
		if (selVO.getTaskName().equals("基本档案")) {
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