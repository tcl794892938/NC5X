package nc.ui.bfriend.button;

import javax.swing.ImageIcon;

import nc.ui.pub.beans.UIButton;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

public class BFButtonVO extends ValueObject {

	private int no;

	private String name;

	private int mnemonic;

	private String toolTipText;

	private ImageIcon imageIcon;
	
	public BFButtonVO(int no, String name) {
		this(no, name, null, null, -1);
	}

	public BFButtonVO(int no, String name, ImageIcon imageIcon,
			String toolTipText, int mnemonic) {
		this.no = no;
		this.name = name;
		this.imageIcon = imageIcon;
		this.toolTipText = toolTipText;
		this.mnemonic = mnemonic;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	public int getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(int mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	public String getEntityName() {
		return null;
	}

	public void validate() throws ValidationException {
	}

	public UIButton buildButton() {
		UIButton btn = null;
		if (imageIcon != null) {
			btn = new UIButton(name, imageIcon);
		} else {
			btn = new UIButton(name);
		}

		btn.setText(name);
		btn.setMnemonic(mnemonic);
		btn.setToolTipText(toolTipText);
		btn.setActionCommand("" + no);

		return btn;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

}
