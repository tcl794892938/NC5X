/*
 * ������Ƶ�¼�Լ�ע��ʱ�Ľ����л��ȹ��� �������ڣ� 2004-11-4 ���ߣ�licp
 */
package nc.ui.sm.login;
/**
 * ��¼����ҵ������
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLayeredPane;

import nc.bs.framework.common.NCLocator;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.MyFocusManager;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.sm.cmenu.Desktop;
import nc.ui.sm.flowdesktop.FlowBrowsePanel;
import nc.ui.sm.identityverify.DefaultLoginPretreatment;
import nc.ui.sm.identityverify.ILoginPretreatment;
import nc.ui.sm.login.speceffe.AbstractSpecialEffectPanel;
import nc.ui.sm.login.speceffe.SpecialEffectConstant;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.sm.flowdesktop.BusiFlowVO;
import nc.vo.sm.flowdesktop.BusiNavigationVO;
import nc.vo.sm.identityverify.IAConfVO;
import nc.vo.sm.login.LoginFailureInfo;
import nc.vo.sm.login.LoginSessBean;
import nc.vo.sm.login.ViewConstantTool;

public class LoginUIControl {
    private static LoginUIControl instance = null;

    // private ILoginPretreatment pretreatement = null;
    /**
     *
     */
    public LoginUIControl() {
        super();

    }

    public static LoginUIControl getInstance() {
        if (instance == null) {
            instance = new LoginUIControl();
        }
        return instance;
    }

    /**
     * @param parent
     * @param lsb
     * @param serverURL
     * @return object���飬����Ϊ2�� ��һ��Ԫ���Ǳ�ʾ��¼�����id�ţ�Ϊ0��ʾ��¼��֤�ɹ�������ֵ��ʾ�����š� �����¼��֤�ɹ����ڶ���Ԫ�ر�ʾ���ص�LoginSessBean���󣬷���Ϊnull
     * @throws Exception
     *             ����ʱ�䣺2004-11-5 8:58:01
     */
    public Object[] login(LoginSessBean lsb, URL serverURL) throws Exception {
        Object[] objs = new Object[2];
        String langcode = lsb.getLanguage();
        nc.vo.ml.Language selLang = NCLangRes.getInstance().getLanguage(langcode);
        nc.ui.ml.NCLangRes.getInstance().setCurrLanguage(selLang);
        String dsName = lsb.getDataSourceName();
        if (dsName != null) {
            System.setProperty(nc.vo.pub.CommonConstant.USER_DATA_SOURCE, dsName);
            System.setProperty(nc.vo.pub.CommonConstant.DEFAULT_DATA_SOURCE, dsName);
        }
        int resultCode = nc.vo.sm.login.LoginFailureInfo.UNKNOWN_ERROR;
        objs[0] = new Integer(resultCode);
        try {
            IAConfVO conf = SFServiceFacility.getIAModeQueryService().getIAConfByuser(lsb.getUserCode(), lsb.getPk_crop(), lsb.getDataSourceName(), lsb.getAccountId());
            if (conf != null) {
                String LoginPretreatmentClassName = conf.getLoginPretreatmentClassName();
                String LoginResultClassName = conf.getLoginResultClassName();
                String loginClientRunnable = conf.getLoginClientRunnable();
                System.setProperty("_LoginPretreatmentClassName_", LoginPretreatmentClassName == null ? "" : LoginPretreatmentClassName);
                System.setProperty("_LoginResultClassName_", LoginResultClassName == null ? "" : LoginResultClassName);
                System.setProperty("_LoginClientRunnable_", loginClientRunnable == null ? "" : loginClientRunnable);
                   
            } else {
                System.setProperty("_LoginPretreatmentClassName_", "");
                System.setProperty("_LoginResultClassName_", "");
                System.setProperty("_LoginClientRunnable_", "");
                          }
            // ��¼Ԥ����
            loginPretreatment(lsb);
            // ��֤����
            validateLSB(lsb);
            // // �ύ��¼����
            if (serverURL == null) {
                serverURL = getSysURL();
            }
            objs = login0(lsb, serverURL);
            resultCode = ((Integer) objs[0]).intValue();
            if (resultCode == LoginFailureInfo.ALREADY_ONLINE) {
                if (MessageDialog.showOkCancelDlg(ClientAssistant.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000058")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000059")/* @res "���û������ߣ��Ƿ�ǿ�Ƶ�¼��" */) == MessageDialog.ID_OK) {
                    lsb.setForcedLogin(true);
                    loginPretreatment(lsb);
                    objs = login0(lsb, serverURL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return objs;
    }

    private Object[] login0(LoginSessBean lsb, URL serverURL) throws Exception {
        // NetObjectOutputStream netOut = null;
        // NetObjectInputStream netIn = null;
        Object[] objs = new Object[2];
        try {
            // URLConnection connection = null;
            // OutputStream out = null;
            // if (serverURL == null) {
            // serverURL = getSysURL();
            // }
            // connection = serverURL.openConnection();
            // connection.setDoOutput(true);
            // out = connection.getOutputStream();
            // netOut = new NetObjectOutputStream(out);
            // netOut.writeObject(lsb);
            // netOut.finish();
            // netOut.flush();
            // netOut.close();
            // netOut = null;
            // ��ȡ��¼�����
            // netIn = new NetObjectInputStream(connection.getInputStream());
            //
            // Integer result = (Integer) netIn.readObject();
            // objs[0] = result;
            objs = SFServiceFacility.getSMVerifyService().login(lsb);
            int resultCode = ((Integer) objs[0]).intValue();
            if (resultCode == LoginFailureInfo.LOGIN_SUCCESS) {
                // ����ע�ṫ˾
                System.setProperty("UserPKCorp", lsb.getPk_crop());
                // ����ע���û�PK
                System.setProperty("UserCode", lsb.getUserId());
                // ��¼�ɹ�,ˢ�±���cookie��
                refreshCookie((LoginSessBean) objs[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return objs;
    }

    private void refreshCookie(LoginSessBean lsb) {
        String accountCode = lsb.getAccountId();
        String pk_corp = lsb.getPk_crop();
        String userCode = lsb.getUserCode();
        String langCode = lsb.getLanguage();
        LoginInfoCookie.getInstance().setAccountCode(accountCode);
        LoginInfoCookie.getInstance().setPk_corp(pk_corp);
        LoginInfoCookie.getInstance().setLangCode(langCode);
        LoginInfoCookie.getInstance().setUserCode(userCode);
        LoginInfoCookie.getInstance().writeToLocal();
    }

    private ILoginPretreatment getLoginPretreatment() {
        ILoginPretreatment pretreatement = null;
        String loginPretreatementClassName = System.getProperty("_LoginPretreatmentClassName_");// MyAppletStub.getInstance().getParameter("pretreatmentClassName");
        if (loginPretreatementClassName != null && loginPretreatementClassName.trim().length() > 0) {
            try {
                Class<?> pretreatmentClass = Class.forName(loginPretreatementClassName);
                pretreatement = (ILoginPretreatment) pretreatmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pretreatement == null) {
            pretreatement = new DefaultLoginPretreatment();
            System.out.println("ʹ��Ĭ�ϵĵ�¼Ԥ����");
        }
        return pretreatement;
    }

    /**
     * �ڵ�¼ǰ����Ԥ���� �������ڣ�(2003-6-26 10:04:12)
     * 
     * @param lsb
     *            nc.vo.sm.login.LoginSessBean
     */
    private void loginPretreatment(LoginSessBean lsb) throws Exception {
        try {
            ILoginPretreatment pretreat = getLoginPretreatment();
            if (pretreat != null)
                pretreat.pretreatment(lsb);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2002-4-28 9:33:20)
     */
    private void setUpLoginInformtion(LoginSessBean lsb) {
        NCAppletStub appletStub = NCAppletStub.getInstance();
        appletStub.clear();
        appletStub.setParameter("LOGIN_TYPE", lsb.getUserType());
        appletStub.setParameter("USER_ID", lsb.getUserId());
        appletStub.setParameter("USER_CODE", lsb.getUserCode());
        appletStub.setParameter("USER_NAME", lsb.getUserName());
        appletStub.setParameter("ACC_ADM_TYPE", lsb.getAccAdmType() + "");
        appletStub.setParameter("USER_IP", lsb.getUserIp());
        appletStub.setParameter("ACCOUNT_ID", lsb.getAccountId());
        appletStub.setParameter("LANGUAGE", lsb.getLanguage());
        appletStub.setParameter("WORK_DATE", lsb.getWorkDate());
        appletStub.setParameter("CORP_ID", lsb.getPk_crop());
        // appletStub.setParameter("SERVER_IP", lsb.getServerName());
        // appletStub.setParameter("SERVER_PORT", lsb.getServerPort() + "");
        appletStub.setParameter("DS_NAME", lsb.getDataSourceName());
        // appletStub.setParameter("USER_WIDTH", lsb.getUserScreenWidth() + "");
        // appletStub.setParameter("USER_HEIGHT", lsb.getUserScreenHeight() +
        // "");
        int intHeight = ViewConstantTool.getDesktopHeightByUserScreenHeight(lsb.getUserScreenHeight());
        if (System.getProperty("nc.jstarter.start") != null)
            appletStub.setParameter("DESKTOP_HEIGHT", (intHeight + 100) + "");
        else
            appletStub.setParameter("DESKTOP_HEIGHT", intHeight + "");
        appletStub.setParameter("DESKTOP_WIDTH", ViewConstantTool.getDesktopWidthByUserScreenWidth(lsb.getUserScreenWidth()) + "");
        appletStub.setParameter("SID", lsb.getSID()); // 2002-11-13����
    }

    public void showDesktop(LoginSessBean lsb) {
        String langcode = lsb.getLanguage();
        nc.vo.ml.Language selLang = NCLangRes.getInstance().getLanguage(langcode);
        nc.ui.ml.NCLangRes.getInstance().setCurrLanguage(selLang);

        setUpLoginInformtion(lsb);

        nc.ui.sm.cmenu.Desktop deskTop = new nc.ui.sm.cmenu.Desktop();
        deskTop.setLoginSessBean(lsb);
        deskTop.setStubToMySelf(NCAppletStub.getInstance());
        deskTop.init();
        deskTop.start();
        //by tcl 2016-11-01
        //�жϼ��Ż��ǹ�˾ 
        try {
        	 String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
             if("".equals(pk_corp)||"0001".equals(pk_corp)){
             	//this.setOpenFlowBusi();
             }else{
             	this.setOpenFlowBusi();
             }
		} catch (Exception e) {
			Debug.debug("root�û����룡");
		}
       
        JLayeredPane layer = ClientAssistant.getApplet().getLayeredPane();
        
        if (!lsb.isHideLogin()) {
            AbstractSpecialEffectPanel panel = null;
			Container parent = ClientAssistant.getApplet().getContentPane();
			parent.setLayout(new BorderLayout());
			if (AbstractSpecialEffectPanel.getSEID() != SpecialEffectConstant.SE_NOSPECEFFE) {
				Image img = getCurrImage(parent);
				panel = AbstractSpecialEffectPanel.getSEPanel(
						AbstractSpecialEffectPanel.getSEID(), img);
				if (panel != null) {
					layer.add(panel, new Integer(9999));
					layer.repaint();
				}
			}
			parent.removeAll();
			parent.add(deskTop, BorderLayout.CENTER);
			parent.validate();
			MyFocusManager.focusNextCompAsNeed(deskTop);
	        if (panel != null) {
	            new Thread(panel).start();
	        }
	    }else{
			deskTop.setBounds(0,0,0,0);
			
			layer.add(deskTop, JLayeredPane.DEFAULT_LAYER);
			
		}


    }

    private void validateLSB(LoginSessBean lsb) throws Exception {
        // workdate:
        String workDate = lsb.getWorkDate();
        if (workDate.equals("")) {
            throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000227")/* @res "��û������ҵ�����ڣ�" */);
        }
        nc.vo.pub.lang.UFDate date = new nc.vo.pub.lang.UFDate(workDate);
        if (date.toString().equals("")) {
            throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000228")/*
                                                                                                         * @res "�������ҵ�����ڸ�ʽ���Ϸ�����ȷ��ʽΪyyyy-mm-dd��"
                                                                                                         */);
        }
        // ֻ���м��ж�fgj2002-04-04
        // �����ͨ�û��жϵ�¼�������Ƿ���һ������ڼ䣺
        //���ж�ת�Ƶ���ִ̨��
//        String pkCorp = lsb.getPk_crop();
//        if ((pkCorp != null) && (!pkCorp.trim().equals("")) && (!pkCorp.trim().equals(nc.ui.pub.CommonMark.GROUP_CODE))) {
//            try {
//                boolean isValideDate = false;
//                isValideDate = SFServiceFacility.getConfigService().isValidDate(date);
//                if (!isValideDate) {
//                    throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000229")/*
//                                                                                                                 * @res "�������ҵ�����ڲ���ϵͳ����Ļ���ڼ�֮�ڣ�"
//                                                                                                                 */);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new Exception(e.getMessage());
//                        
//            }
//        }

        String userCode = lsb.getUserCode();
        if (userCode == null || userCode.equals("")) {
            throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000231")/* @res "��û�������û����룡" */);
        }

    }

    private URL m_url = null;

    public URL getSysURL() {
        if (m_url == null) {
            String str = ClientAssistant.getSysURLContextString();
            StringBuffer sb = new StringBuffer(str);
            sb.append("service/LoginServlet");
            try {
                m_url = new URL(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return m_url;
    }

    private Image getCurrImage(Container parent) {
        int w = parent.getWidth();
        int h = parent.getHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = null;
        try {
            g2 = img.createGraphics();
            parent.paintAll(g2);
        } catch (Exception e) {
        } finally {
            if (g2 != null)
                g2.dispose();
        }

        return img;
    }
    
    /**
     * ���õ�¼ҳ������������� 2016-11-01 by tcl
     */
    private void setOpenFlowBusi(){
    	
    	 FlowBrowsePanel fpanel=new FlowBrowsePanel(true);
         String sql="select * from sm_busi_navigation where name='����¼��' and pk_busiworkflow is not null";
         IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
         BusiNavigationVO vo=new BusiNavigationVO();
         try {
         	vo=(BusiNavigationVO)iQ.executeQuery(sql, new BeanProcessor(BusiNavigationVO.class));
 		} catch (BusinessException e) {
 			e.printStackTrace();
 		}
 		BusiFlowVO voBizflow=null;
 		try {
 			if (vo.getWfchart() == null) {
 				voBizflow = SFServiceFacility.getBusiFlowDesktopQueryService().findFlowVOByPK(vo.getPk_busiworkflow());
 				vo.setWfchart(voBizflow.getWfchartString());
 			}
 			
 		} catch (BusinessException e) {
 			e.printStackTrace();
 		}
 		
 		 fpanel.browseFlow(vo);
 		 UIPanel mep=Desktop.getApplet().getDesktopPanel().getMessagePanel();
 		 Desktop.getApplet().insertTabToWorkaround("��Ϣ����",	null, mep, "", -1);
         Desktop.getApplet().insertTabToWorkaround("ҵ������",	null, fpanel, "", -1);
         Desktop.getApplet().getDesktopTabbedPane().setSelectedComponent(fpanel);//����ѡ�е�ǰҳǩ
         System.out.println("********");
    }
}