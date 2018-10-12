package nc.ui.pub.msg;
/**
 * 登录触发业务流程
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.DapCall;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.msg.let.AbstractMessageLet;
import nc.ui.pub.msg.let.ErrorMessageLet;
import nc.ui.pub.msg.let.IMessageLet;
import nc.ui.pub.msg.let.LetNode;
import nc.ui.pub.msg.let.PfMessageLet;
import nc.vo.pub.msg.MessageLetVO;
import nc.vo.pub.msg.MessageVO;

/**
 * Let化的新消息中心
 *  
 * @author leijun 2008-12
 * @since 5.5
 */
public class MessagePanel extends ToftPanel implements Observer {

	/** 消息接收对象 */
	private MessageReceiveObject m_msgReceiveObject = null;

	/** 消息接收线程 */
	private MessageReceiveThread m_msgReceiveThread = null;

	//默认的背景颜色
	private Color defBackColor = Color.WHITE;

	//消息面板当前的所有消息片
	private HashMap<String, AbstractMessageLet> hmLets = new HashMap<String, AbstractMessageLet>();

	public MessagePanel() {
		super();

		MessagePanelOptions mpo = MessageRepository.getInstance().getMsgPanelOptions();
		if (mpo.getLetInfo() == null) {
			//使用5.02默认的三栏式：公告、待办、预警
			mpo.setLetInfo(createDefaultLetInfo());
		}

		//先清空观察者，再添加进去
		MessageRepository.getInstance().deleteObservers();
		MessageRepository.getInstance().addObserver(this);

		initUI();
	}

	private LetNode createDefaultLetInfo() {
		//使用5.02默认的三栏式：公告、待办、预警
		LetNode bulletinLetNode = new LetNode(MessagePanelOptions.MESSAGE_BULLETIN);
		LetNode worklistLetNode = new LetNode(MessagePanelOptions.MESSAGE_WORKLIST);
		LetNode paLetNode = new LetNode(MessagePanelOptions.MESSAGE_PA);

		LetNode virtualBulletinAndPA = new LetNode(LetNode.VIRTUAL_NODE_CODE);
		virtualBulletinAndPA.add(bulletinLetNode);
		virtualBulletinAndPA.add(paLetNode);
		virtualBulletinAndPA.setSplitPercent(0.5);
		virtualBulletinAndPA.setSplitDirection(JSplitPane.HORIZONTAL_SPLIT);

		LetNode virtualRoot = new LetNode(LetNode.VIRTUAL_NODE_CODE);
		virtualRoot.add(virtualBulletinAndPA);
		virtualRoot.add(worklistLetNode);
		virtualRoot.setSplitPercent(0.33);
		virtualRoot.setSplitDirection(JSplitPane.VERTICAL_SPLIT);

		return virtualRoot;
	}

	private void initUI() {
		setBackground(defBackColor);
		setName("MessagePanel");

		reLayout();

		//使用一个临时线程来异步初次查询消息，并启动 自动刷新线程
		Thread tempThread = new Thread(new Runnable() {
			public void run() {
				try {
					//先等待2s
					Thread.sleep(2000);
					//使用已有的筛选器查询消息
					getMsgReceiveObject().fetchNewMessagesAllFilter();
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}

				//启动 自动刷新线程
				startRefreshThread(MessageRepository.getInstance().getMsgPanelOptions().getFreshSetting());
			}
		});
		Logger.debug("消息面板初始化initialize()被调用，当前线程=" + Thread.currentThread() + "，启动临时线程=" + tempThread);
		tempThread.start();
	}

	/**
	 * 如果设置为自动刷新，则启动刷新线程
	 * @param refreshSetting
	 */
	public void startRefreshThread(RefreshSetting refreshSetting) {
		if (refreshSetting.isAutoRefresh()) {
			//如果设置为自动刷新，则启动 自动刷新线程
			int refreshInterval = refreshSetting.getRefreshInterval();
			int refreshUnit = refreshSetting.getTimeUnit();
			int refreshSecond = refreshInterval * 1000 * (int) Math.pow(60, refreshUnit);
			getMsgReceiveObject().setReceInterval(refreshSecond);

			MessageReceiveThread autoReceiveThread = new MessageReceiveThread(this);
			autoReceiveThread.setMessageObj(getMsgReceiveObject());
			//thread.setMessageReceiver(this);
			m_msgReceiveThread = autoReceiveThread;

			// 保存到客户端内存中
			ClientEnvironment.getInstance().putValue(MessageReceiveThread.class.getName(),
					m_msgReceiveThread);

			autoReceiveThread.start();

			Logger.debug("启动消息刷新线程=" + autoReceiveThread);
		}
	}

	public Container getParentOfBanner() {
		return this;
	}

	protected void finalize() throws Throwable {
		stopReceiveMessage();
		//getMsgDetailDlg().dispose();
		super.finalize();
	}

	private UISplitPane buildSplit(LetNode virtualLetNode) {
		int iOrientation = virtualLetNode.getSplitDirection();
		UISplitPane sp = createSplitPane(iOrientation);
		virtualLetNode.setSplitPercent(0.33);//by tcl
		sp.setResizeWeight(virtualLetNode.getSplitPercent());

		//二叉树的左孩子
		LetNode leftNode = (LetNode) virtualLetNode.getChildAt(0);
		String strLeftCode = leftNode.getCode();
		if (strLeftCode.equals(LetNode.VIRTUAL_NODE_CODE)) {
			//说明为虚节点
			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
				sp.add(buildSplit(leftNode), JSplitPane.TOP);
			else
				sp.add(buildSplit(leftNode), JSplitPane.LEFT);
		} else {
			AbstractMessageLet aml = createLet(strLeftCode);
			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
				sp.add(aml, JSplitPane.TOP);
			else
				sp.add(aml, JSplitPane.LEFT);
		}

		//二叉树的右孩子
		LetNode rightNode = (LetNode) virtualLetNode.getChildAt(1);
		String strRightCode = rightNode.getCode();
		if (strRightCode.equals(LetNode.VIRTUAL_NODE_CODE)) {
			//说明为虚节点
			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
				sp.add(buildSplit(rightNode), JSplitPane.BOTTOM);
			else
				sp.add(buildSplit(rightNode), JSplitPane.RIGHT);
		} else {
			AbstractMessageLet aml = createLet(strRightCode);
			if (iOrientation == JSplitPane.VERTICAL_SPLIT)
				sp.add(aml, JSplitPane.BOTTOM);
			else
				sp.add(aml, JSplitPane.RIGHT);
		}
		return sp;
	}

	private JComponent createCenterComponent() {
		MessagePanelOptions mpo = MessageRepository.getInstance().getMsgPanelOptions();
		LetNode letInfo = mpo.getLetInfo();
		if (letInfo.getCode().equals(LetNode.VIRTUAL_NODE_CODE))
			return buildSplit(letInfo);
		else {
			//只有一个叶子节点
			return createLet(letInfo.getCode());
		}
	}

	private AbstractMessageLet createLet(String strCode) {
		AbstractMessageLet amlReturn;
		MessageLetVO letVo = PfUIDataCache.getMessageLet(strCode);
		if (letVo == null) {
			amlReturn = new ErrorMessageLet(this, strCode);
		} else {
			//如果已经存在该消息片，则直接使用
			if (hmLets.containsKey(strCode))
				return hmLets.get(strCode);

			//新创建该消息片
			try {
				Constructor construct = Class.forName(letVo.getUiclass()).getConstructor(this.getClass());
				amlReturn = (AbstractMessageLet) construct.newInstance(this);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				amlReturn = new ErrorMessageLet(this, strCode);
			}
		}
		hmLets.put(amlReturn.getCode(), amlReturn);
		return amlReturn;
	}

	/**
	 * 当注销登录时，需要重新计算所有JSplitPane的分隔百分比
	 */
	public void saveSplitPercentWhenLogout() {
		LetNode letInfo = MessageRepository.getInstance().getMsgPanelOptions().getLetInfo();
		Enumeration<LetNode> enumLet = letInfo.depthFirstEnumeration();
		while (enumLet.hasMoreElements()) {
			LetNode lNode = enumLet.nextElement();
			if (!lNode.getCode().equals(LetNode.VIRTUAL_NODE_CODE)) {
				//消息片的父SplitPane
				AbstractMessageLet aml = hmLets.get(lNode.getCode());
				JSplitPane sp = (JSplitPane) aml.getParent();
				double splitPercent = 0.5;
				if (sp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
					splitPercent = (double) sp.getDividerLocation() / (sp.getWidth() - sp.getDividerSize());
				else
					splitPercent = (double) sp.getDividerLocation() / (sp.getHeight() - sp.getDividerSize());
				((LetNode) lNode.getParent()).setSplitPercent(splitPercent);
			}
		}

		//将布局信息保存到文件缓存
		MessageRepository.getInstance().msgPanelOptionsChanged();
	}

	/**
	 * 关闭某个消息片
	 * @param strCode
	 */
	public void closeLet(String strCode) {
		LetNode letInfo = MessageRepository.getInstance().getMsgPanelOptions().getLetInfo();
		LetNode lNode = findLetNode(letInfo, strCode);
		LetNode lParent = (LetNode) lNode.getParent();
		LetNode lParentParent = (LetNode) lParent.getParent();
		if (lParentParent == null) {
			letInfo = getAnotherChild(lNode);
			letInfo.removeFromParent();
			MessageRepository.getInstance().getMsgPanelOptions().setLetInfo(letInfo);
		} else {
			int index = lParentParent.getIndex(lParent);
			lParentParent.remove(lParent);
			lParentParent.insert(getAnotherChild(lNode), index);
		}

		//从消息片缓存中删除该消息片
		hmLets.remove(strCode);
		//将布局信息保存到文件缓存
		MessageRepository.getInstance().msgPanelOptionsChanged();

		//重新布局
		reLayout();
		revalidate();
	}

	/**
	 * 新增某个消息片 
	 * @param strCode 原始消息片
	 * @param strNewCode 新消息片
	 * @param iOrientation
	 */
	public void addLet(String strCode, String strNewCode, int iOrientation) {
		LetNode letInfo = MessageRepository.getInstance().getMsgPanelOptions().getLetInfo();
		LetNode lNode = findLetNode(letInfo, strCode);
		LetNode lParent = (LetNode) lNode.getParent();

		if (lParent == null) {
			LetNode virtualNode = new LetNode(LetNode.VIRTUAL_NODE_CODE);
			virtualNode.add(lNode);
			virtualNode.add(new LetNode(strNewCode));
			virtualNode.setSplitPercent(0.5);
			virtualNode.setSplitDirection(iOrientation);
			MessageRepository.getInstance().getMsgPanelOptions().setLetInfo(virtualNode);
		} else {
			int index = lParent.getIndex(lNode);
			LetNode virtualNode = new LetNode(LetNode.VIRTUAL_NODE_CODE);
			virtualNode.add(lNode);
			virtualNode.add(new LetNode(strNewCode));
			virtualNode.setSplitPercent(0.5);
			virtualNode.setSplitDirection(iOrientation);
			lParent.insert(virtualNode, index);
		}

		//将布局信息保存到文件缓存
		MessageRepository.getInstance().msgPanelOptionsChanged();

		//重新布局
		reLayout();
		revalidate();
	}

	/**
	 * 获得二叉树中某个孩子的相邻孩子
	 * @param childNode
	 * @return
	 */
	private LetNode getAnotherChild(LetNode childNode) {
		LetNode nextNode = (LetNode) childNode.getNextSibling();
		if (nextNode != null)
			return nextNode;
		LetNode previousNode = (LetNode) childNode.getPreviousSibling();
		if (previousNode != null)
			return previousNode;
		throw new IllegalArgumentException("错误的参数：" + childNode.getCode());
	}

	private LetNode findLetNode(LetNode rootNode, String strCode) {
		Enumeration<LetNode> enumLet = rootNode.depthFirstEnumeration();
		while (enumLet.hasMoreElements()) {
			LetNode lNode = enumLet.nextElement();
			if (lNode.getCode().equals(strCode))
				return lNode;
		}
		return null;
	}

	/**
	 * 返回消息接收对象 
	 */
	public MessageReceiveObject getMsgReceiveObject() {
		if (m_msgReceiveObject == null) {
			m_msgReceiveObject = new MessageReceiveObject(DapCall.getOperator());
			m_msgReceiveObject.setCorppk(DapCall.getPkcorp());
		}
		return m_msgReceiveObject;
	}

	/**
	 * 返回消息接收线程
	 */
	public MessageReceiveThread getMsgReceiveThread() {
		return m_msgReceiveThread;
	}

	private UISplitPane createSplitPane(int orientation) {
		UISplitPane sp = new UISplitPane(orientation) {
			public void paint(Graphics g) {
				super.paint(g);
				// 为了使分割条和底色保持一致,这样相当于隐藏
				g.setColor(defBackColor);// this.getBackground());
				if (getOrientation() == JSplitPane.VERTICAL_SPLIT)
					g.fillRect(0, getDividerLocation(), this.getWidth(), getDividerSize());
				else
					g.fillRect(getDividerLocation(), 0, getDividerSize(), this.getWidth());
			}
		};
		sp.setOpaque(false);
		sp.setName("SpMessage");
		sp.setDividerSize(2);
		sp.setBorder(null);
		return sp;
	}

	/**
	 * 将某个UI组件 置中并最大化
	 */
	public void centerMax(Component comp) {
		this.removeAll();
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(comp, gbc);
	}

	public void reLayout() {
		centerMax(createCenterComponent());
	}

	/**
	 * 停止接收消息
	 */
	public void stopReceiveMessage() {
		if (getMsgReceiveThread() != null) {
			getMsgReceiveThread().setStopThread();
		}
	}

	public String getTitle() {
		return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000420")/*@res"消息中心"*/;
	}

	public void onButtonClicked(ButtonObject bo) {
	}

	public boolean isshowMenuPanel() {
		return false;
	}

	public boolean isshowTitlePanel() {
		return false;
	}

	// 因为本Panel没有菜单按钮，不需要初始化热键，故重载其方法，作为空实现．--请勿删除此方法．
	protected void initHotkeys() {
	}

	// The method is critical .It maybe execute when update that login and
	// startRefreshThread.
	public void update(Observable o, Object arg) {
		if (arg != null && arg instanceof MessageVO)
			return;
		MessagePanelOptions mpo = MessageRepository.getInstance().getMsgPanelOptions();

		//遍历消息面板中的所有消息片
		Iterator<AbstractMessageLet> iter = hmLets.values().iterator();
		while (iter.hasNext()) {
			IMessageLet msgLet = iter.next();
			if (msgLet instanceof PfMessageLet) {
				//只有平台消息片才需要过滤
				PfMessageLet mLet = (PfMessageLet) msgLet;
				mLet.doFilter(mpo.getFilterByName(mLet.getCode()), true);
			}
		}

	}

	public void autoRefresh() {
		//XXX:自动刷新线程导致的刷新
		Logger.debug("##autoRefresh() called");
		//遍历消息面板中的所有消息片
		Iterator<AbstractMessageLet> iter = hmLets.values().iterator();
		while (iter.hasNext()) {
			IMessageLet msgLet = iter.next();
			msgLet.autoRefresh();
		}
	}

	/**
	 * 专门为Portal使用的消息处理方法
	 * @param msg
	 * @param strCode 见常量MessagePanelOptions.MESSAGE_PA
	 */
	public void acceptMessageForPortal(MessageVO msg, String strCode) {
		//查询消息面板中是否已有平台消息片
		AbstractMessageLet aml = hmLets.get(strCode);
		if (aml instanceof PfMessageLet) {
			((PfMessageLet) aml).acceptMessage(msg);
		} else {
			MessageLetVO letVo = PfUIDataCache.getMessageLet(strCode);
			if (letVo == null) {
				MessageDialog.showErrorDlg(this, "错误", "找不到该消息片的注册信息：" + strCode);
				return;
			}
			//新创建该消息片，然后处理该消息VO
			try {
				Constructor construct = Class.forName(letVo.getUiclass()).getConstructor(this.getClass());
				PfMessageLet pfLet = (PfMessageLet) construct.newInstance(this);
				pfLet.acceptMessage(msg);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, "错误", "处理消息时出现异常：" + e.getMessage());
			}
		}
	}
}