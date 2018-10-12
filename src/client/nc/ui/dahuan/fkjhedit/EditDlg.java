package nc.ui.dahuan.fkjhedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.dahuan.fkedit.FkjhEditVO;

public class EditDlg extends UIDialog {

	private static final long serialVersionUID = 1L;

	public EditDlg(Container parent) {
		super(parent);
		initialize();
		initDialog();
	}

	private BillListPanel panel;

	private void initialize() {
		if (null == this.panel) {
			this.panel = new BillListPanel();
			this.panel.loadTemplet("0001AA1000000000YXZJ");// ����ģ��
		}
	}

	private void initDialog() {
		// ���öԻ�������
		this.setTitle("�޸ļ�¼");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(800, 600));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ����panel
		this.add(panel, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}

	public BillListPanel getPanel() {
		return panel;
	}

	public void setPanel(BillListPanel panel) {
		this.panel = panel;
	}
	
	public void showEditInfo(FkjhEditVO[] fvos){
		
		panel.getHeadBillModel().setBodyDataVO(fvos);
		panel.getHeadBillModel().loadLoadRelationItemValue();
		panel.getHeadBillModel().execLoadFormula();
		this.showModal();
	}

}
