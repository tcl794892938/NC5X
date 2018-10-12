  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dahuan.htinfo.htchange;
   	
	import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2014-3-31
 * @author author
 * @version Your Project 1.0
 */
     public class HtChangeEntity extends SuperVO {
           
             public String pk_contract;
             public String htsaxno;
             public String corp_manager;
             public UFDate change_date;
             public Integer htstatus = 0;
             public String htcust;
             public String htstyle;
             public String httype;
             public String dept_manager;
             public String htcontractor;
             public String htctpe;
             public String htcode;
             public String htbank;
             public String htpduname;
             public String htmanager;
             public String pk_conchange;
             public UFDouble htamount;
             public UFDate sure_date;
             public String htprojname;
             public UFDouble xmaccount;
             public String retvemo;
             public String pk_corp;
             public String voperid;
             
             public static final String  PK_CONTRACT="pk_contract";   
             public static final String  HTSAXNO="htsaxno";   
             public static final String  CORP_MANAGER="corp_manager";   
             public static final String  CHANGE_DATE="change_date";   
             public static final String  HTSTATUS="htstatus";   
             public static final String  HTCUST="htcust";   
             public static final String  HTSTYLE="htstyle";   
             public static final String  HTTYPE="httype";   
             public static final String  DEPT_MANAGER="dept_manager";   
             public static final String  HTCONTRACTOR="htcontractor";   
             public static final String  HTCTPE="htctpe";   
             public static final String  HTCODE="htcode";   
             public static final String  HTBANK="htbank";   
             public static final String  HTPDUNAME="htpduname";   
             public static final String  HTMANAGER="htmanager";   
             public static final String  PK_CONCHANGE="pk_conchange";   
             public static final String  HTAMOUNT="htamount";   
             public static final String  SURE_DATE="sure_date";   
             public static final String  HTPROJNAME="htprojname";   
             public static final String  XMACCOUNT = "xmaccount";
    
             
             
        public String getVoperid() {
				return voperid;
			}

			public void setVoperid(String voperid) {
				this.voperid = voperid;
			}

		public String getPk_corp() {
				return pk_corp;
			}

			public void setPk_corp(String pk_corp) {
				this.pk_corp = pk_corp;
			}

		public String getRetvemo() {
				return retvemo;
			}

			public void setRetvemo(String retvemo) {
				this.retvemo = retvemo;
			}

		public UFDouble getXmaccount() {
				return xmaccount;
			}

			public void setXmaccount(UFDouble xmaccount) {
				this.xmaccount = xmaccount;
			}

		/**
	   * 属性pk_contract的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getPk_contract() {
		 return pk_contract;
	  }   
	  
     /**
	   * 属性pk_contract的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newPk_contract String
	   */
	public void setPk_contract(String newPk_contract) {
		
		pk_contract = newPk_contract;
	 } 	  
       
        /**
	   * 属性htsaxno的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtsaxno() {
		 return htsaxno;
	  }   
	  
     /**
	   * 属性htsaxno的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtsaxno String
	   */
	public void setHtsaxno(String newHtsaxno) {
		
		htsaxno = newHtsaxno;
	 } 	  
       
        /**
	   * 属性corp_manager的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getCorp_manager() {
		 return corp_manager;
	  }   
	  
     /**
	   * 属性corp_manager的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newCorp_manager String
	   */
	public void setCorp_manager(String newCorp_manager) {
		
		corp_manager = newCorp_manager;
	 } 	  
       
        /**
	   * 属性change_date的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return UFDate
	   */
	 public UFDate getChange_date() {
		 return change_date;
	  }   
	  
     /**
	   * 属性change_date的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newChange_date UFDate
	   */
	public void setChange_date(UFDate newChange_date) {
		
		change_date = newChange_date;
	 } 	  
       
        /**
	   * 属性htstatus的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return Integer
	   */
	 public Integer getHtstatus() {
		 return htstatus;
	  }   
	  
     /**
	   * 属性htstatus的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtstatus Integer
	   */
	public void setHtstatus(Integer newHtstatus) {
		
		htstatus = newHtstatus;
	 } 	  
       
        /**
	   * 属性htcust的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtcust() {
		 return htcust;
	  }   
	  
     /**
	   * 属性htcust的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtcust String
	   */
	public void setHtcust(String newHtcust) {
		
		htcust = newHtcust;
	 } 	  
       
        /**
	   * 属性htstyle的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtstyle() {
		 return htstyle;
	  }   
	  
     /**
	   * 属性htstyle的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtstyle String
	   */
	public void setHtstyle(String newHtstyle) {
		
		htstyle = newHtstyle;
	 } 	  
       
        /**
	   * 属性httype的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHttype() {
		 return httype;
	  }   
	  
     /**
	   * 属性httype的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHttype String
	   */
	public void setHttype(String newHttype) {
		
		httype = newHttype;
	 } 	  
       
        /**
	   * 属性dept_manager的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getDept_manager() {
		 return dept_manager;
	  }   
	  
     /**
	   * 属性dept_manager的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newDept_manager String
	   */
	public void setDept_manager(String newDept_manager) {
		
		dept_manager = newDept_manager;
	 } 	  
       
        /**
	   * 属性htcontractor的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtcontractor() {
		 return htcontractor;
	  }   
	  
     /**
	   * 属性htcontractor的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtcontractor String
	   */
	public void setHtcontractor(String newHtcontractor) {
		
		htcontractor = newHtcontractor;
	 } 	  
       
        /**
	   * 属性htctpe的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtctpe() {
		 return htctpe;
	  }   
	  
     /**
	   * 属性htctpe的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtctpe String
	   */
	public void setHtctpe(String newHtctpe) {
		
		htctpe = newHtctpe;
	 } 	  
       
        /**
	   * 属性htcode的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtcode() {
		 return htcode;
	  }   
	  
     /**
	   * 属性htcode的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtcode String
	   */
	public void setHtcode(String newHtcode) {
		
		htcode = newHtcode;
	 } 	  
       
        /**
	   * 属性htbank的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtbank() {
		 return htbank;
	  }   
	  
     /**
	   * 属性htbank的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtbank String
	   */
	public void setHtbank(String newHtbank) {
		
		htbank = newHtbank;
	 } 	  
       
        /**
	   * 属性htpduname的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtpduname() {
		 return htpduname;
	  }   
	  
     /**
	   * 属性htpduname的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtpduname String
	   */
	public void setHtpduname(String newHtpduname) {
		
		htpduname = newHtpduname;
	 } 	  
       
        /**
	   * 属性htmanager的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtmanager() {
		 return htmanager;
	  }   
	  
     /**
	   * 属性htmanager的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtmanager String
	   */
	public void setHtmanager(String newHtmanager) {
		
		htmanager = newHtmanager;
	 } 	  
       
        /**
	   * 属性pk_conchange的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getPk_conchange() {
		 return pk_conchange;
	  }   
	  
     /**
	   * 属性pk_conchange的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newPk_conchange String
	   */
	public void setPk_conchange(String newPk_conchange) {
		
		pk_conchange = newPk_conchange;
	 } 	  
       
        /**
	   * 属性htamount的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return UFDouble
	   */
	 public UFDouble getHtamount() {
		 return htamount;
	  }   
	  
     /**
	   * 属性htamount的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtamount UFDouble
	   */
	public void setHtamount(UFDouble newHtamount) {
		
		htamount = newHtamount;
	 } 	  
       
        /**
	   * 属性sure_date的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return UFDate
	   */
	 public UFDate getSure_date() {
		 return sure_date;
	  }   
	  
     /**
	   * 属性sure_date的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newSure_date UFDate
	   */
	public void setSure_date(UFDate newSure_date) {
		
		sure_date = newSure_date;
	 } 	  
       
        /**
	   * 属性htprojname的Getter方法.
	   *
	   * 创建日期:2014-3-31
	   * @return String
	   */
	 public String getHtprojname() {
		 return htprojname;
	  }   
	  
     /**
	   * 属性htprojname的Setter方法.
	   *
	   * 创建日期:2014-3-31
	   * @param newHtprojname String
	   */
	public void setHtprojname(String newHtprojname) {
		
		htprojname = newHtprojname;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2014-3-31
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_conchange == null) {
			errFields.add(new String("pk_conchange"));
			  }	
	   	
	    StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
		if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append(",");
			message.append(temp[i]);
		}
		throw new NullFieldException(message.toString());
		}
	 }
			   
       
   	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2014-3-31
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2014-3-31
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_conchange";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2014-3-31
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dh_conchange";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2014-3-31
	  */
	public HtChangeEntity() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2014-3-31
	 * @param newPk_conchange 主键值
	 */
	 public HtChangeEntity(String newPk_conchange) {
		
		// 为主键字段赋值:
		 pk_conchange = newPk_conchange;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2014-3-31
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_conchange;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2014-3-31
	  * @param newPk_conchange  String    
	  */
	 public void setPrimaryKey(String newPk_conchange) {
				
				pk_conchange = newPk_conchange; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2014-3-31
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "dh_conchange"; 
				
	 } 
} 
