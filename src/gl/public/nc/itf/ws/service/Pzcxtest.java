package nc.itf.ws.service;


public interface Pzcxtest {
	abstract String qryPZ(String s) throws  Exception;
	abstract String doSendVoucherToNCSystem(String s) throws Exception;
	abstract String DeletePZ(String s) throws Exception;
}

