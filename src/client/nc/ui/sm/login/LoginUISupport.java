package nc.ui.sm.login;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.metal.MetalComboBoxUI;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ServerTimeProxy;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPasswordField;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.style.Style;
import nc.vo.bd.access.BdinfoManager;
import nc.vo.ml.Language;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.ConfigParameter;
import nc.vo.sm.login.LoginSessBean;

public class LoginUISupport
{
  ArrayList al_listener = new ArrayList();

  private ItemHandler itemHandler = new ItemHandler();

  private TransFocusAction transFocusAction = new TransFocusAction();

  private ConfigParameter m_account = null;

  private UIComboBox cbbLanguage = null;

  private UIComboBox cbbAccount = null;

  private UIRefPane rpCorp = null;

  private UIRefPane rpDate = null;

  private UITextField tfUser = null;

  private UIPasswordField pfUserPWD = null;

  private UILabel lblAccount = null;

  private UILabel lblCorp = null;

  private UILabel lblDate = null;

  private UILabel lblUser = null;

  private UILabel lblUserPWD = null;

  private JButton helpBtn = null;

  private UIButton loginBtn = null;

  private JButton aboutBtn = null;

  private UIButton optionBtn = null;

  private JCheckBox ckbZipRemoteStream = null;

  private JLabel lblLoginFlash = null;

  private JLabel lblLoginResult = null;

  private JPanel topPanel = null;

  private JPanel bottomPanel = null;

  private JPanel centerPanel = null;

  private JPanel loginCompsPanel = null;

  private JPanel optionPanel = null;

  private boolean isShowOptionPanel = false;

  private String m_LangCode = null;

  private Dimension size = new Dimension(160, 20);

  private Border lblBorder = BorderFactory.createEmptyBorder(1, 1, 1, 10);

  private LoginAction loginAction = new LoginAction();

  private String copyRightStr = null;

  private JLabel welComeLbl = null;

  private void setBtnNewString(AbstractButton btn, String newStr, Font font)
  {
    String oldStr = btn.getText();
    int oldW = ClientAssistant.getStringWidth(btn, oldStr);
    int newW = ClientAssistant.getStringWidth(btn, newStr);
    int dw = newW - oldW;
    Rectangle rect = btn.getBounds();
    rect.width += dw;
    btn.setText(newStr);
    Dimension size = new Dimension(rect.width, rect.height);
    btn.setFont(font);
    btn.setSize(size);
    btn.setPreferredSize(size);
    btn.setBounds(rect);
  }

  public LoginUISupport()
  {
    init();
  }
  private void printClientTimeZone() {
    TimeZone tz = TimeZone.getDefault();
    String msg = "Client Time Zone is " + tz.getDisplayName();
    System.out.println(msg);
  }
  private void init() {
    printClientTimeZone();

    boolean isSel = LoginInfoCookie.getInstance().isZipRemoteStream();
    setNetStreamZipProp(isSel);

    NCLangRes.getInstance().setCurrLanguage(NCLangRes.getDefaultLanguage());

    updateCopyRightStr();
    initValuesByCookie();
    updateCorpState();
    new PreLoadClassThread().start();

    if (!hasLoginUIBackGroundIcon()) {
      JApplet applet = ClientAssistant.getApplet();
      applet.getContentPane().addContainerListener(new CustomContainerAdapter(getWelComeLbl()));
      applet.getLayeredPane().add(getWelComeLbl(), new Integer(999));
    }
  }

  protected void updateCorpState() {
    Account selAccount = (Account)getCbbAccount().getSelectedItem();
    if ((selAccount != null) && (selAccount.getDataSourceName().equals("")))
      getRpCorp().setEnabled(false);
    else
      getRpCorp().setEnabled(true);
  }

  private void initValuesByCookie()
  {
    LoginInfoCookie cookie = LoginInfoCookie.getInstance();

    String accountCode = cookie.getAccountCode();
    String pk_corp = cookie.getPk_corp();
    String userCode = cookie.getUserCode();

    this.m_LangCode = cookie.getLangCode();

    if (accountCode != null) {
      Account account = getAccountByCode(accountCode);
      if (account != null) {
        getCbbAccount().setSelectedItem(account);
        System.setProperty("UserDataSource", account.getDataSourceName());
        System.setProperty("DefaultDataSource", account.getDataSourceName());

        if (this.m_LangCode != null) {
          Language lang = NCLangRes.getInstance().getLanguage(this.m_LangCode);
          getCbbLanguage().setSelectedItem(lang);
        }

        System.setProperty("UserPKCorp", pk_corp);
        getRpCorp().setPK(pk_corp);
        getTfUser().setText(userCode);
      }
    } else if (getCbbAccount().getModel().getSize() > 0) {
      getCbbAccount().setSelectedIndex(0);
    }
  }

  private Account getAccountByCode(String accountCode) {
    Account[] accounts = getConfigParameter().getAryAccounts();
    if (accounts != null) {
      for (int i = 0; i < accounts.length; i++) {
        if (accounts[i].getAccountCode().equals(accountCode)) {
          return accounts[i];
        }
      }
    }
    return null;
  }

  protected void updateLoginInfo(LoginSessBean lsb)
    throws Exception
  {
  }

  protected LoginSessBean getLoginInfo()
    throws Exception
  {
    String accountCode = null;
    String dsName = null;
    String pk_corp = null;
    String corpCode = null;
    String workDate = null;
    String language = null;
    String userCode = null;
    String pwd = null;
    String userScreenWidth = null;
    String userScreenHeight = null;

    Account selAccount = (Account)getCbbAccount().getSelectedItem();
    accountCode = selAccount.getAccountCode();
    dsName = selAccount.getDataSourceName();
    corpCode = getRpCorp().getText().trim();
    if (corpCode.equals(""))
      pk_corp = new String("");
    else {
      pk_corp = getRpCorp().getRefPK();
    }

    if (pk_corp == null) {
      pk_corp = new String("");
    }
    Language selLang = (Language)getCbbLanguage().getSelectedItem();
    language = selLang.getCode();

    workDate = getRpDate().getText();

    userCode = getTfUser().getText();
    pwd = new String(getPfUserPWD().getPassword());

    userScreenWidth = NCAppletStub.getInstance().getParameter("USER_WIDTH");
    userScreenHeight = NCAppletStub.getInstance().getParameter("USER_HEIGHT");

    LoginSessBean lsb = new LoginSessBean();
    lsb.setAccountId(accountCode);
    lsb.setDataSourceName(dsName);
    lsb.setPk_corp(pk_corp);
    lsb.setCorpCode(corpCode);
    lsb.setWorkDate(workDate);
    lsb.setLanguage(language);
    lsb.setUserCode(userCode);
    lsb.setPassword(pwd);
    if (userScreenWidth != null) {
      try {
        lsb.setUserScreenWidth(new Integer(userScreenWidth).intValue());
        lsb.setIsUserResolutionValid(true);
      } catch (NumberFormatException e) {
        lsb.setIsUserResolutionValid(false);
      }
    }
    if (userScreenHeight != null) {
      try {
        lsb.setUserScreenHeight(new Integer(userScreenHeight).intValue());
      } catch (NumberFormatException e) {
        lsb.setIsUserResolutionValid(false);
      }
    }
    return lsb;
  }

  private ConfigParameter getConfigParameter()
  {
    if (this.m_account == null) {
      try {
        this.m_account = new ConfigParameter();
        String strAccoutnInfo = NCAppletStub.getInstance().getParameter("ACCOUNT");
        StringTokenizer st = new StringTokenizer(strAccoutnInfo, ",");
        int intAry = 0;
        Account[] vos = new Account[st.countTokens()];
        String tmpStr = null;
        while (st.hasMoreElements()) {
          Account vo = new Account();
          tmpStr = String.valueOf(st.nextElement());
          String tmpStr2 = "";
          int intBegin = 0;
          int intIndex = 0;
          int intNo = -1;
          int intLast = tmpStr.lastIndexOf(":");
          while (intIndex >= 0) {
            intIndex = tmpStr.indexOf(":", intBegin);
            if (intIndex > -1) {
              tmpStr2 = tmpStr.substring(intBegin, intIndex);
              intBegin = intIndex + 1;
              intNo++;
            }
            else if (intBegin == intLast + 1) {
              tmpStr2 = tmpStr.substring(intBegin);
              vo.setLang(tmpStr2);
              break;
            }

            switch (intNo) {
            case 0:
              vo.setDataSourceName(tmpStr2);
              break;
            case 1:
              vo.setAccountCode(tmpStr2);
              break;
            case 2:
              vo.setAccountName(tmpStr2);
              break;
            case 3:
              vo.setLang(tmpStr2);
            }
          }

          vos[(intAry++)] = vo;
        }
        this.m_account.setAryAccounts(vos);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        ShowDialog.showErrorDlg(ClientAssistant.getApplet(), NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019"), NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000226"));
      }

    }

    return this.m_account;
  }

  public UIComboBox getCbbLanguage() {
    if (this.cbbLanguage == null) {
      try {
        this.cbbLanguage = new LanguageCombobox();
        this.cbbLanguage.setRenderer(new LanguageCellRender());
        this.cbbLanguage.setOpaque(false);
        this.cbbLanguage.setEditable(false);

        this.cbbLanguage.getInputMap().put(KeyStroke.getKeyStroke(10, 0), "enter");
        this.cbbLanguage.getActionMap().put("enter", this.transFocusAction);
        this.cbbLanguage.setFont(new Font(Style.getFontname(), 0, 12));
        Language[] langTypes = NCLangRes.getInstance().getAllLanguages();
        int maxW = 0;
        FontMetrics fm = this.cbbLanguage.getFontMetrics(this.cbbLanguage.getFont());
        int langCount = langTypes == null ? 0 : langTypes.length;
        for (int i = 0; i < langCount; i++) {
          this.cbbLanguage.addItem(langTypes[i]);
          String text = langTypes[i].getDisplayName();
          if (text != null) {
            int width = fm.stringWidth(text);
            if (width > maxW) {
              maxW = width;
            }
          }
        }
        this.cbbLanguage.setPreferredSize(new Dimension(maxW + 20, 23));

        this.cbbLanguage.setSelectedIndex(-1);
        this.cbbLanguage.addItemListener(this.itemHandler);
        if (langCount > 0)
          this.cbbLanguage.setSelectedIndex(0);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.cbbLanguage;
  }

  public UIComboBox getCbbAccount()
  {
    if (this.cbbAccount == null) {
      try {
        this.cbbAccount = new UIComboBox();
        this.cbbAccount.setPreferredSize(this.size);
        this.cbbAccount.setFont(new Font(Style.getFontname(), 0, 12));

        Account[] accounts = getConfigParameter().getAryAccounts();
        ArrayList al = new ArrayList();
        Account sysAccount = null;
        for (int i = 0; i < accounts.length; i++) {
          if (accounts[i].getDataSourceName().equals("")) {
            sysAccount = accounts[i];
          } else {
            if ((!RuntimeEnv.getInstance().isDevelopMode()) && (accounts[i].getAccountCode().equals("design"))) {
              continue;
            }
            al.add(accounts[i]);
          }
        }
        if (sysAccount != null) {
          al.add(sysAccount);
        }
        for (int i = 0; i < al.size(); i++) {
          this.cbbAccount.addItem(al.get(i));
        }

        this.cbbAccount.setSelectedIndex(-1);
        this.cbbAccount.addItemListener(this.itemHandler);
        if (al.size() > 0)
          this.cbbAccount.setSelectedIndex(0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.cbbAccount;
  }

  public UIPasswordField getPfUserPWD() {
    if (this.pfUserPWD == null) {
      try {
        this.pfUserPWD = new UIPasswordField();
        this.pfUserPWD.setFont(new Font(Style.getFontname(), 0, 12));
        this.pfUserPWD.setAllowOtherCharacter(true);
        this.pfUserPWD.setPreferredSize(this.size);
        InputMap input = this.pfUserPWD.getInputMap(0);
        ActionMap action = this.pfUserPWD.getActionMap();
        input.put(KeyStroke.getKeyStroke(10, 0), "login");
        action.put("login", this.loginAction);
        this.pfUserPWD.setVisible(ClientUIConfig.getInstance().getPwdCompVisible().booleanValue());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.pfUserPWD;
  }

  public UIRefPane getRpCorp() {
    if (this.rpCorp == null) {
      try {
        this.rpCorp = new UIRefPane();
        this.rpCorp.setPreferredSize(this.size);

        this.rpCorp.setIsCustomDefined(true);
        this.rpCorp.setRefModel(new Corp_GroupsDefaultRefModel("公司目录(集团)"));
        this.rpCorp.getUITextField().setFont(new Font(Style.getFontname(), 0, 12));
        this.rpCorp.getUITextField().getInputMap().put(KeyStroke.getKeyStroke(10, 0), "enter");
        this.rpCorp.getUITextField().getActionMap().put("enter", new RequestFocusAction(getRpDate()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.rpCorp;
  }

  public UIRefPane getRpDate() {
    if (this.rpDate == null) {
      try {
        this.rpDate = new UIRefPane();
        this.rpDate.getUITextField().setFont(new Font(Style.getFontname(), 0, 12));
        this.rpDate.setPreferredSize(this.size);
        this.rpDate.setRefNodeName("日历");
        this.rpDate.getUITextField().getInputMap().put(KeyStroke.getKeyStroke(10, 0), "enter");
        this.rpDate.getUITextField().getActionMap().put("enter", new RequestFocusAction(getTfUser()));

        UFDateTime datetime = ServerTimeProxy.getInstance().getServerTime();
        this.rpDate.setText(datetime.getDate().toString());
        this.rpDate.setEnabled(ClientUIConfig.getInstance().getDateCompEditable().booleanValue());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.rpDate;
  }

  public UITextField getTfUser() {
    if (this.tfUser == null) {
      try {
        this.tfUser = new UITextField();
        this.tfUser.setFont(new Font(Style.getFontname(), 0, 12));
        this.tfUser.setPreferredSize(this.size);
        this.tfUser.setVisible(ClientUIConfig.getInstance().getUserCompVisible().booleanValue());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.tfUser;
  }

  public JButton getAboutBtn() {
    if (this.aboutBtn == null) {
      try {
        this.aboutBtn = new JButton();
        this.aboutBtn.setAction(new AboutBtnAction());
        this.aboutBtn.setFont(new Font(Style.getFontname(), 0, 12));
        this.aboutBtn.setText(LoginPanelRes.getBtnAbout());
        this.aboutBtn.setMargin(new Insets(0, 0, 0, 0));
        this.aboutBtn.setBorder(BorderFactory.createEmptyBorder());
        this.aboutBtn.setOpaque(false);
        this.aboutBtn.setFocusPainted(false);
        this.aboutBtn.setContentAreaFilled(false);
        this.aboutBtn.setRolloverEnabled(true);
        this.aboutBtn.setIcon(ClientAssistant.loadImageIcon("images/loginicon/about.png"));
        this.aboutBtn.setRolloverIcon(ClientAssistant.loadImageIcon("images/loginicon/about_over.png"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.aboutBtn;
  }

  public JButton getHelpBtn() {
    if (this.helpBtn == null) {
      try {
        this.helpBtn = new JButton();
        this.helpBtn.setAction(new HelpBtnAction());
        this.helpBtn.setFont(new Font(Style.getFontname(), 0, 12));
        this.helpBtn.setText(LoginPanelRes.getBtnHelp());
        this.helpBtn.setMargin(new Insets(0, 0, 0, 0));
        this.helpBtn.setBorder(BorderFactory.createEmptyBorder());
        this.helpBtn.setOpaque(false);
        this.helpBtn.setFocusPainted(false);
        this.helpBtn.setContentAreaFilled(false);
        this.helpBtn.setRolloverEnabled(true);
        this.helpBtn.setIcon(ClientAssistant.loadImageIcon("images/loginicon/help.png"));
        this.helpBtn.setRolloverIcon(ClientAssistant.loadImageIcon("images/loginicon/help_over.png"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.helpBtn;
  }

  public UIButton getLoginBtn() {
    if (this.loginBtn == null) {
      try {
        this.loginBtn = new UIButton();
        this.loginBtn.setAction(this.loginAction);
        this.loginBtn.setFont(new Font(Style.getFontname(), 0, 12));
        this.loginBtn.setText(LoginPanelRes.getBtnLogin());
        this.loginBtn.setToolTipText(LoginPanelRes.getBtnLoginTip());
        this.loginBtn.setPreferredSize(new Dimension(70, 20));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.loginBtn;
  }

  public UIButton getOptionBtn() {
    if (this.optionBtn == null) {
      try {
        this.optionBtn = new UIButton();
        this.optionBtn.setText(NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000060"));

        this.optionBtn.setHorizontalTextPosition(2);
        this.optionBtn.setFont(new Font(Style.getFontname(), 0, 12));
        this.optionBtn.setIcon(ClientAssistant.loadImageIcon("images/loginicon/rightarrow.gif"));
        setShowOptionPanel(false);
        this.optionBtn.setPreferredSize(new Dimension(70, 20));
        this.optionBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            LoginUISupport.this.setShowOptionPanel(!LoginUISupport.this.isShowOptionPanel());
            LoginUISupport.this.fireUIUpdate(new LoginUISupport.LoginUIEvent(LoginUISupport.this.optionBtn));
          } } );
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.optionBtn;
  }

  public JCheckBox getCkbZipRemoteStream() {
    if (this.ckbZipRemoteStream == null) {
      String name = NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000246");
      this.ckbZipRemoteStream = new JCheckBox(name);
      this.ckbZipRemoteStream.setOpaque(false);
      this.ckbZipRemoteStream.setFocusPainted(false);
      this.ckbZipRemoteStream.setBounds(0, 0, 115, 20);
      this.ckbZipRemoteStream.setFont(new Font(Style.getFontname(), 0, 12));
      boolean isSel = LoginInfoCookie.getInstance().isZipRemoteStream();
      this.ckbZipRemoteStream.setSelected(isSel);
      setNetStreamZipProp(isSel);
      this.ckbZipRemoteStream.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          boolean isSel = LoginUISupport.this.ckbZipRemoteStream.isSelected();
          LoginUISupport.this.setNetStreamZipProp(isSel);
          LoginInfoCookie.getInstance().setZipRemoteStream(isSel);
          LoginInfoCookie.getInstance().writeToLocal();
        } } );
    }
    return this.ckbZipRemoteStream;
  }

  private void setNetStreamZipProp(boolean isZip) {
    try {
      nc.bs.framework.comn.NetStreamConstants.STREAM_NEED_COMPRESS = isZip;
      ClassLoader cl = getClass().getClassLoader().getParent();
      if (cl == null) {
        cl = ClassLoader.getSystemClassLoader();
      }

      Class clazz = cl.loadClass("nc.bs.framework.comn.NetStreamConstants");
      Field field = clazz.getDeclaredField("STREAM_NEED_COMPRESS");
      field.set(null, Boolean.valueOf(isZip));
    }
    catch (Throwable thr)
    {
    }
  }

  public UILabel getLblAccount()
  {
    if (this.lblAccount == null) {
      try {
        this.lblAccount = new UILabel();
        this.lblAccount.setFont(new Font(Style.getFontname(), 0, 12));
        this.lblAccount.setText(LoginPanelRes.getAccount2());
        this.lblAccount.setBorder(this.lblBorder);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.lblAccount;
  }

  public UILabel getLblCorp() {
    if (this.lblCorp == null) {
      try {
        this.lblCorp = new UILabel();
        this.lblCorp.setFont(new Font(Style.getFontname(), 0, 12));
        this.lblCorp.setText(LoginPanelRes.getCorporation2());
        this.lblCorp.setBorder(this.lblBorder);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.lblCorp;
  }

  public UILabel getLblDate() {
    if (this.lblDate == null) {
      try {
        this.lblDate = new UILabel();
        this.lblDate.setFont(new Font(Style.getFontname(), 0, 12));
        this.lblDate.setText(LoginPanelRes.getDate2());
        this.lblDate.setBorder(this.lblBorder);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.lblDate;
  }

  public UILabel getLblUser() {
    if (this.lblUser == null) {
      try {
        this.lblUser = new UILabel();
        this.lblUser.setFont(new Font(Style.getFontname(), 0, 12));
        this.lblUser.setText(LoginPanelRes.getUser2());
        this.lblUser.setBorder(this.lblBorder);
        this.lblUser.setVisible(ClientUIConfig.getInstance().getUserCompVisible().booleanValue());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.lblUser;
  }

  public UILabel getLblUserPWD() {
    if (this.lblUserPWD == null) {
      try {
        this.lblUserPWD = new UILabel();
        this.lblUserPWD.setFont(new Font(Style.getFontname(), 0, 12));
        this.lblUserPWD.setText(LoginPanelRes.getPassword2());
        this.lblUserPWD.setBorder(this.lblBorder);
        this.lblUserPWD.setVisible(ClientUIConfig.getInstance().getPwdCompVisible().booleanValue());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.lblUserPWD;
  }

  private boolean hasLoginUIBackGroundIcon() {
    return ClientUIConfig.getInstance().getLoginUIBGIcon() != null;
  }

  public JPanel getTopPanel() {
    if (this.topPanel == null) {
      this.topPanel = new JPanel();
      this.topPanel.setOpaque(false);
      this.topPanel.setLayout(new BorderLayout());
      JPanel p2 = new JPanel()
      {
        private static final long serialVersionUID = 6289212838013767586L;
        ImageIcon bgIcon = ClientAssistant.loadImageIcon("images/loginicon/top_bg.png");

        public void paintComponent(Graphics g) { super.paintComponent(g);
          if (isOpaque()) {
            Graphics2D g2 = (Graphics2D)g;
            Dimension d = getSize();
            if (this.bgIcon != null) {
              int iW = this.bgIcon.getIconWidth();
              int iH = this.bgIcon.getIconHeight();
              int x = (d.width - iW) / 2;
              int y = d.height - iH;
              g2.drawImage(this.bgIcon.getImage(), 0, 0, d.width, d.height, this);
            }
          }
        }
      };
//      p2.setBackground(Color.RED);
      p2.setOpaque(!hasLoginUIBackGroundIcon());
      p2.setPreferredSize(new Dimension(30, 62));
      SpringLayout sl = new SpringLayout();
      p2.setLayout(sl);
      p2.add(getCbbLanguage());
      p2.add(getAboutBtn());
      p2.add(getHelpBtn());

      this.topPanel.add(p2, "Center");
      this.topPanel.setPreferredSize(new Dimension(30, 0));

      sl.putConstraint("East", getHelpBtn(), -20, "East", p2);
      sl.putConstraint("North", getHelpBtn(), (p2.getPreferredSize().height - getHelpBtn().getPreferredSize().height) / 2, "North", p2);
      sl.putConstraint("North", getAboutBtn(), (p2.getPreferredSize().height - getAboutBtn().getPreferredSize().height) / 2, "North", p2);
      sl.putConstraint("East", getAboutBtn(), -5, "West", getHelpBtn());
      sl.putConstraint("North", getCbbLanguage(), (p2.getPreferredSize().height - getCbbLanguage().getPreferredSize().height) / 2 + 2, "North", p2);
      sl.putConstraint("East", getCbbLanguage(), -16, "West", getAboutBtn());
    }
    return this.topPanel;
  }

  private void updateCopyRightStr() {
    this.copyRightStr = NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000061");
  }

  public JPanel getBottomPanel() {
    if (this.bottomPanel == null) {
      this.bottomPanel = new JPanel();
      this.bottomPanel.setOpaque(false);
      this.bottomPanel.setLayout(new BorderLayout());
      this.bottomPanel.setPreferredSize(new Dimension(30, 0));
      JPanel p1 = new JPanel()
      {
        private static final long serialVersionUID = 2958009447658114338L;
        Color c1 = new Color(15331836);
        Color c2 = new Color(16236549);

        public void paintComponent(Graphics g) { super.paintComponent(g);
          if (isOpaque()) {
            Graphics2D g2 = (Graphics2D)g;
            Paint oldPaint = g2.getPaint();
            int w = getSize().width;
            int h = getSize().height;
            GradientPaint gp = new GradientPaint(w - 230, 0.0F, this.c1, w - 115, 0.0F, this.c2);
            g2.setPaint(gp);
            g2.fillRect(w - 230, 0, 115, h);
            gp = new GradientPaint(w - 115, 0.0F, this.c2, w, 0.0F, this.c1);
            g2.setPaint(gp);
            g2.fillRect(w - 115, 0, 115, h);
            g2.setPaint(oldPaint);
          }
        }
      };
      p1.setBackground(new Color(15331836));
      p1.setOpaque(!hasLoginUIBackGroundIcon());
      p1.setPreferredSize(new Dimension(30, 1));

      JPanel p2 = new JPanel()
      {
        private static final long serialVersionUID = -6849133262584714814L;
        ImageIcon bgIcon = ClientAssistant.loadImageIcon("images/loginicon/ufida.gif");
        Color textColor = new Color(9283011);

        public void paintComponent(Graphics g) { super.paintComponent(g);
          if (isOpaque()) {
            Graphics2D g2 = (Graphics2D)g;
            Dimension d = getSize();
            int iconH = 0;
            if (this.bgIcon != null) {
              iconH = this.bgIcon.getIconHeight();
              this.bgIcon.paintIcon(this, g2, d.width - 25 - this.bgIcon.getIconWidth(), 10);
            }
            String text = LoginUISupport.this.copyRightStr;
            if (text == null) {
              text = "";
            }
            Font font = getFont();
            FontMetrics fe = getFontMetrics(font);
            int strW = fe.stringWidth(text);
            Color oldC = g2.getColor();
            g2.setColor(this.textColor);
            g2.drawString(text, d.width - 25 - strW, 10 + iconH + 6 + fe.getAscent());
            g2.setColor(oldC);
          }
        }
      };
      p2.setOpaque(!hasLoginUIBackGroundIcon());
      p2.setBackground(Color.white);
      this.bottomPanel.add(p1, "North");
      this.bottomPanel.add(p2, "Center");
    }
    return this.bottomPanel;
  }

  public JLabel getWelComeLbl() {
    if (this.welComeLbl == null) {
      this.welComeLbl = new JLabel();
      ImageIcon icon = ClientAssistant.loadImageIcon("images/loginicon/mtp.png");
      this.welComeLbl.setBorder(BorderFactory.createEmptyBorder());
      if (icon != null) {
        this.welComeLbl.setIcon(icon);
        this.welComeLbl.setBounds(12, 48, icon.getIconWidth(), icon.getIconHeight());
      }
    }
    return this.welComeLbl;
  }

  public JPanel getCenterPanel() {
    if (this.centerPanel == null) {
      this.centerPanel = new JPanel()
      {
        private static final long serialVersionUID = 5767013748981058923L;
        Color c1 = Color.BLACK;
        Color c2 = Color.RED;
        ImageIcon logo = ClientAssistant.loadImageIcon("images/loginicon/logo.png");
        ImageIcon line = ClientAssistant.loadImageIcon("images/loginicon/line.png");
        
        ImageIcon ncbank = ClientAssistant.loadImageIcon("images/loginicon/nccs_back.png");

        public void paintComponent(Graphics g) { 
        	super.paintComponent(g);
          if (isOpaque()) {
            Graphics2D g2 = (Graphics2D)g;
            Paint oldPaint = g2.getPaint();
            int w = getSize().width;
            int h = getSize().height;
            
            
//            GradientPaint gp = new GradientPaint(0.0F, 0.0F, this.c1, 0.0F, h, this.c2);
//            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
            g2.setPaint(oldPaint);
//            int x = getLeftLocation();
//            if (this.logo != null) {
//              this.logo.paintIcon(this, g2, x, 205);
//              x += this.logo.getIconWidth() + 30;
//            }
//            if (this.line != null){
//              this.line.paintIcon(this, g2, x, 80);
//            }
            g2.drawImage(ncbank.getImage(), 0, 0,w,h, null);
          }
        }

        private int getLeftLocation()
        {
          Dimension d = getSize();
          int width = LoginUISupport.this.getLblAccount().getPreferredSize().width + LoginUISupport.this.size.width + 72;
          if (this.logo != null) {
            width += this.logo.getIconWidth();
          }
          if (this.line != null) {
            width += this.line.getIconWidth();
          }
          return (d.width - width) / 2 - 20;
        }

        public void doLayout()
        {
          int location = getLeftLocation() + 65;
          if (this.logo != null) {
            location += this.logo.getIconWidth();
          }
          if (this.line != null) {
            location += this.line.getIconWidth();
          }

          LoginUISupport.this.getLoginCompsPanel().setLocation(getSize().width*2/5, getSize().height/4);
        }
      };
      this.centerPanel.setOpaque(!hasLoginUIBackGroundIcon());
    }

    return this.centerPanel;
  }

  public JPanel getLoginCompsPanel() {
    if (this.loginCompsPanel == null) {
      this.loginCompsPanel = new JPanel();
      this.loginCompsPanel.setOpaque(false);
      Dimension d = ClientAssistant.getApplet().getSize();
      this.loginCompsPanel.setSize(d.width, d.height);
    }

    return this.loginCompsPanel;
  }

  public JPanel getOptionPanel() {
    if (this.optionPanel == null) {
      this.optionPanel = new JPanel();
      this.optionPanel.setOpaque(false);
      this.optionPanel.setLayout(null);
      this.optionPanel.add(getCkbZipRemoteStream());

      this.optionPanel.setPreferredSize(new Dimension(600, 26));
    }
    return this.optionPanel;
  }

  public void addUIUpdateListener(IUIUpdateListener listener)
  {
    this.al_listener.add(listener);
  }

  protected void fireUIUpdate(LoginUIEvent e) {
    int count = this.al_listener.size();
    for (int i = 0; i < count; i++) {
      IUIUpdateListener listener = (IUIUpdateListener)this.al_listener.get(i);
      listener.uiUpdate(e);
    }
  }

  public boolean isShowOptionPanel() {
    return this.isShowOptionPanel;
  }

  public void setShowOptionPanel(boolean isShowOptionPanel) {
    this.isShowOptionPanel = isShowOptionPanel;
    String name = NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000060");

    if (isShowOptionPanel()) {
      getOptionBtn().setIcon(ClientAssistant.loadImageIcon("images/loginicon/leftarrow.gif"));
    }
    else {
      getOptionBtn().setIcon(ClientAssistant.loadImageIcon("images/loginicon/rightarrow.gif"));
    }

    getOptionBtn().setText(name);
  }

  public JLabel getLblLoginFlash() {
    if (this.lblLoginFlash == null) {
      this.lblLoginFlash = new JLabel();
      ImageIcon icon = getLoginFlashImage();
      this.lblLoginFlash.setIcon(icon);
      this.lblLoginFlash.setOpaque(false);
      this.lblLoginFlash.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      this.lblLoginFlash.setSize(icon.getIconWidth(), icon.getIconHeight());
      this.lblLoginFlash.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }
    return this.lblLoginFlash;
  }

  private ImageIcon getLoginFlashImage() {
    ImageIcon retrIcon = null;
    ImageIcon icon = ClientAssistant.loadImageIcon("images/loginicon/progress.gif");
    if (icon != null) {
      int w = icon.getIconWidth();
      int h = icon.getIconHeight();
      BufferedImage img = new BufferedImage(w - 2, h - 2, 6);
      Graphics2D g = img.createGraphics();
      Image iconImg = icon.getImage();
      g.drawImage(iconImg, 1, 1, w - 1, h - 1, 0, 0, w - 1, h - 1, getLblLoginFlash());
      g.dispose();
      retrIcon = new ImageIcon(iconImg);
    }
    return retrIcon;
  }

  public JLabel getLblLoginResult() {
    if (this.lblLoginResult == null) {
      ImageIcon icon = ClientAssistant.loadImageIcon("images/loginicon/warn.gif");
      this.lblLoginResult = new JLabel(icon) {
        private static final long serialVersionUID = -3544675299514278098L;

        public void setText(String text) { if (text == null)
            text = "null";
          super.setText(text);
          Font f = getFont();
          if (f != null) {
            FontMetrics fm = getFontMetrics(f);
            int w = SwingUtilities.computeStringWidth(fm, text);
            int h = getPreferredSize().height;
            setPreferredSize(new Dimension(w + 30, h));
          } }

        public void paintComponent(Graphics g)
        {
          Insets inset = getInsets();
          Color c = g.getColor();
          Rectangle rect = getBounds();
          g.setColor(getBackground());
          g.fillRect(inset.left, inset.top, rect.width - inset.left - inset.right, rect.height - inset.bottom - inset.top);
          g.setColor(c);
          super.paintComponent(g);
        }
      };
      this.lblLoginResult.setOpaque(false);
      this.lblLoginResult.setHorizontalAlignment(0);

      this.lblLoginResult.setPreferredSize(new Dimension(200, 26));
      this.lblLoginResult.setBorder(new AbstractBorder()
      {
        private static final long serialVersionUID = 7680278292185216656L;
        Color borderColor = new Color(11964932);

        Color shadowColor = new Color(13027527);

        public Insets getBorderInsets(Component c) {
          return new Insets(1, 1, 3, 3);
        }

        public boolean isBorderOpaque() {
          return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
          Color oldColor = g.getColor();
          g.setColor(this.shadowColor);
          Graphics2D g2 = (Graphics2D)g;
          Rectangle rect1 = new Rectangle(x, y, w - 3, h - 3);
          Rectangle rect2 = new Rectangle(x + 3, y + 3, w - 3, h - 3);
          Area a = new Area(rect2);
          a.subtract(new Area(rect1));
          g2.fill(a);
          g.setColor(this.borderColor);
          g2.draw(rect1);
          g.setColor(oldColor);
        }
      });
      this.lblLoginResult.setBackground(new Color(16773314));
    }
    return this.lblLoginResult;
  }

  public void setUIEnable(boolean enable)
  {
    getCbbAccount().setEnabled(enable);
    getCbbLanguage().setEnabled(enable);
    if (((Account)getCbbAccount().getSelectedItem()).getDataSourceName().equals(""))
      getRpCorp().setEnabled(false);
    else {
      getRpCorp().setEnabled(enable);
    }
    getRpDate().setEnabled((enable) && (ClientUIConfig.getInstance().getDateCompEditable().booleanValue()));
    getTfUser().setEnabled(enable);
    getPfUserPWD().setEnabled(enable);
    getLoginBtn().setEnabled(enable);
    getOptionBtn().setEnabled(enable);
    getCkbZipRemoteStream().setEnabled(enable);

    getAboutBtn().setEnabled(enable);
  }

  private static class AboutBtnAction extends AbstractAction
  {
    private static final long serialVersionUID = -8686211649732823588L;

    public void actionPerformed(ActionEvent e)
    {
      AboutDialog2 dlg = new AboutDialog2(ClientAssistant.getApplet());
      Dimension d = ClientAssistant.getApplet().getSize();
      Dimension dlgD = dlg.getSize();
      int x = Math.abs(d.width - dlgD.width) / 2;
      int y = (d.height - dlgD.height) / 2;
      dlg.setLocation(x, y);
      dlg.setVisible(true);
    }
  }

  private static class HelpBtnAction extends AbstractAction
  {
    private static final long serialVersionUID = -4441484147402759648L;

    public void actionPerformed(ActionEvent e)
    {
      URL url = null;
      try {
        String prefix = ClientAssistant.getSysURLContextString();
        url = new URL(prefix + "help/" + ClientEnvironment.getInstance().getLanguage() + "/about.html");
      } catch (MalformedURLException ee) {
        ee.printStackTrace();
        MessageDialog.showErrorDlg(ClientAssistant.getApplet(), NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000232"), NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000062"));
      }

      ClientAssistant.getApplet().getAppletContext().showDocument(url, "_blank");
    }
  }

  private class LoginAction extends AbstractAction
  {
    private static final long serialVersionUID = -6135751854829695365L;

    private LoginAction()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      Runnable run = new Runnable() {
        public void run() {
          LoginUISupport.this.getLblLoginResult().setText("");
          LoginUISupport.this.getLoginBtn().setCursor(new Cursor(3));
          try
          {
            LoginSessBean lsb = LoginUISupport.this.getLoginInfo();
            LoginUISupport.this.updateLoginInfo(lsb);
            if (lsb != null) {
              Object[] objs = LoginUIControl.getInstance().login(lsb, null);
              int resultCode = ((Integer)objs[0]).intValue();
              if (resultCode == 0)
                LoginUIControl.getInstance().showDesktop((LoginSessBean)objs[1]);
              else {
                LoginUISupport.this.getLblLoginResult().setText(LoginResult.getLoginStrResult(resultCode));
              }

            }

          }
          catch (Exception e)
          {
            Logger.error(e.getMessage(), e);
            e.printStackTrace();
            String msg = e.getMessage();
            if ((msg == null) || (msg.trim().length() == 0) || ("null".equals(msg))) {
              msg = e.getClass().getCanonicalName() + ":" + msg;
            }
            LoginUISupport.this.getLblLoginResult().setText(msg);
          }
          LoginUISupport.this.getLoginBtn().setCursor(Cursor.getDefaultCursor());
          LoginUISupport.this.fireUIUpdate(new LoginUISupport.LoginUIEvent(LoginUISupport.this.getLoginBtn(), 1));
        }
      };
      LoginUISupport.this.fireUIUpdate(new LoginUISupport.LoginUIEvent(LoginUISupport.this.getLoginBtn(), 0));
      new Thread(run).start();
    }
  }

  private static class LanguageComboboxUI extends MetalComboBoxUI
  {
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus)
    {
    }

    protected JButton createArrowButton()
    {
      JButton btn = super.createArrowButton();
      btn.setBorderPainted(false);
      btn.setContentAreaFilled(false);
      return btn;
    }

    protected void installComponents() {
      super.installComponents();
      this.comboBox.setBorder(null);
    }
  }

  private static class LanguageCellRender extends JLabel
    implements ListCellRenderer
  {
    private static final long serialVersionUID = -2222264957994327437L;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      setFont(list.getFont());
      if (isSelected) {
        setOpaque(true);
        setBackground(list.getSelectionBackground());
        setForeground(list.getSelectionForeground());
      } else {
        setOpaque(false);
        setBackground(list.getBackground());
        setForeground(list.getForeground());
      }

      setText(value == null ? "" : value.toString());
      return this;
    }
  }

  private class ItemHandler
    implements ItemListener
  {
    private ItemHandler()
    {
    }

    public void itemStateChanged(ItemEvent e)
    {
      Object source = e.getSource();
      if (LoginUISupport.this.getCbbAccount().equals(source)) {
        if (e.getStateChange() == 1)
        {
          Account selAccount = (Account)LoginUISupport.this.getCbbAccount().getSelectedItem();

          System.setProperty("UserDataSource", selAccount.getDataSourceName());
          System.setProperty("DefaultDataSource", selAccount.getDataSourceName());

          LoginUISupport.this.getRpCorp().getRefModel().clearData();
          LoginUISupport.this.getRpCorp().setPK(null);
          try
          {
            if (!"".equals(selAccount.getDataSourceName()))
            {
              BdinfoManager.clear();
            }
          } catch (Exception ee) {
            ee.printStackTrace();
          }
          LoginUISupport.this.fireUIUpdate(new LoginUISupport.LoginUIEvent(LoginUISupport.this.getCbbAccount()));
        }
      } else if ((LoginUISupport.this.getCbbLanguage().equals(source)) && 
        (e.getStateChange() == 1)) {
        Language language = (Language)LoginUISupport.this.getCbbLanguage().getSelectedItem();
        NCLangRes.getInstance().setCurrLanguage(language);
        ClientEnvironment.getInstance().setLanguage(language.getCode());

        System.setProperty("Login_Lang_Code", language.getCode());

        Style.refreshStyle();

        Font font = new Font(language.getFontName(), 0, 12);
        LoginUISupport.this.getCbbAccount().setFont(font);
        LoginUISupport.this.getCbbLanguage().setFont(font);
        LoginUISupport.this.getTfUser().setFont(font);
        LoginUISupport.this.getPfUserPWD().setFont(font);
        LoginUISupport.this.getRpCorp().getUITextField().setFont(font);
        LoginUISupport.this.getRpDate().getUITextField().setFont(font);

        LoginUISupport.this.getLoginBtn().setFont(font);
        LoginUISupport.this.getLoginBtn().setText(LoginPanelRes.getBtnLogin());
        LoginUISupport.this.getLoginBtn().setToolTipText(LoginPanelRes.getBtnLoginTip());
        String optionText = NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000060");
        LoginUISupport.this.getOptionBtn().setFont(font);
        LoginUISupport.this.getOptionBtn().setText(optionText);
        LoginUISupport.this.getOptionBtn().setToolTipText(optionText);
        LoginUISupport.this.getHelpBtn().setFont(font);
        LoginUISupport.this.getHelpBtn().setText(LoginPanelRes.getBtnHelp());
        LoginUISupport.this.getAboutBtn().setFont(font);
        LoginUISupport.this.getAboutBtn().setText(LoginPanelRes.getBtnAbout());
        LoginUISupport.this.getLblAccount().setFont(font);
        LoginUISupport.this.getLblAccount().setText(LoginPanelRes.getAccount2());
        LoginUISupport.this.getLblCorp().setFont(font);
        LoginUISupport.this.getLblCorp().setText(LoginPanelRes.getCorporation2());
        LoginUISupport.this.getLblDate().setFont(font);
        LoginUISupport.this.getLblDate().setText(LoginPanelRes.getDate2());
        LoginUISupport.this.getLblUser().setFont(font);
        LoginUISupport.this.getLblUser().setText(LoginPanelRes.getUser2());
        LoginUISupport.this.getLblUserPWD().setFont(font);
        LoginUISupport.this.getLblUserPWD().setText(LoginPanelRes.getPassword2());

        String str1 = NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000246");
        LoginUISupport.this.setBtnNewString(LoginUISupport.this.getCkbZipRemoteStream(), str1, font);

        LoginUISupport.this.updateCopyRightStr();
        LoginUISupport.this.getBottomPanel().repaint();

        LoginUISupport.this.fireUIUpdate(new LoginUISupport.LoginUIEvent(LoginUISupport.this.getCbbLanguage()));
      }
    }
  }

  private static class CustomUI extends BasicButtonUI
  {
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text)
    {
      super.paintText(g, c, textRect, text);
      AbstractButton b = (AbstractButton)c;
      ButtonModel model = b.getModel();
      if ((model.isArmed()) || (model.isRollover()))
        g.drawLine(textRect.x, textRect.y + textRect.height - 1, textRect.x + textRect.width, textRect.y + textRect.height - 1);
    }
  }

  private static class TransFocusAction extends AbstractAction
  {
    private static final long serialVersionUID = 1976819505587381597L;

    public void actionPerformed(ActionEvent e)
    {
      Object o = e.getSource();
      if ((o instanceof Component))
        ((Component)o).transferFocus();
    }
  }

  public static class LoginUIEvent extends AWTEvent
  {
    private static final long serialVersionUID = -5201877997812643873L;
    public static final int LOGIN_START = 0;
    public static final int LOGIN_END = 1;

    public LoginUIEvent(Object source)
    {
      super(source,-1);
    }

    public LoginUIEvent(Object source, int id) {
      super(source,id);
    }
  }

  public static abstract interface IUIUpdateListener
  {
    public abstract void uiUpdate(LoginUISupport.LoginUIEvent paramLoginUIEvent);
  }

  private static class CustomContainerAdapter extends ContainerAdapter
  {
    private Component comp = null;

    public CustomContainerAdapter(Component comp)
    {
      this.comp = comp;
    }

    public void componentRemoved(ContainerEvent e)
    {
      JApplet applet = ClientAssistant.getApplet();
      applet.getLayeredPane().remove(this.comp);
      applet.getContentPane().removeContainerListener(this);
    }
  }

  private static class LanguageCombobox extends UIComboBox
  {
    private static final long serialVersionUID = -7307027422867254504L;

    public void updateUI()
    {
      setUI(new LoginUISupport.LanguageComboboxUI());
    }
  }

  private static class RequestFocusAction extends AbstractAction
  {
    private static final long serialVersionUID = 3403610494958197404L;
    private Component comp = null;

    public RequestFocusAction(Component comp)
    {
      this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
      this.comp.requestFocus();
    }
  }
}