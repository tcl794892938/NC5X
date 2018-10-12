package nc.ui.test;

import nc.vo.framework.rsa.Encode;

public class TestEncode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Encode en=new Encode();
		
		String str=en.decode("hfeamcoohnilaoja");
		
		System.out.println(str);
 
	}

}
