  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dahuan.hkjh;
   	
	import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2014-9-25
 * @author author
 * @version Your Project 1.0
 */
     public class HkswVO extends SuperVO {
           
             public UFDate dbilldate;
             public String vbillno;
             public UFDouble hk_amount;
             public String vapproveid;
             public String pk_hksw;
             public String pk_cust;
             public String voperid;
             public String remark;
             public Integer vbillstatus;
             public Integer dr=0;
             public UFDateTime ts;
             public String pk_bizong;
             public UFDouble bzbl;
             public UFDouble rmb_amount;
             public String hkbillno;
             
             public String pk_contract;
             public String xmname;
             public String ctcode;
             public String ctname;
             public String pk_dept;
             public UFDate vapprovedate;
             
             public String vemo;
             public UFDouble give_amount;
             public UFDouble discount_amount;
             public String pk_corp;
            
             public static final String  DBILLDATE="dbilldate";   
             public static final String  VBILLNO="vbillno";   
             public static final String  HK_AMOUNT="hk_amount";   
             public static final String  VAPPROVEID="vapproveid";   
             public static final String  PK_HKDH="pk_hkdh";   
             public static final String  PK_CUST="pk_cust";   
             public static final String  VOPERID="voperid";   
             public static final String  REMARK="remark";   
             public static final String  VBILLSTATUS="vbillstatus";   
      
             
             public String getPk_corp() {
				return pk_corp;
			}

			public void setPk_corp(String pk_corp) {
				this.pk_corp = pk_corp;
			}

			public UFDouble getDiscount_amount() {
				return discount_amount;
			}

			public void setDiscount_amount(UFDouble discount_amount) {
				this.discount_amount = discount_amount;
			}

			public UFDouble getGive_amount() {
				return give_amount;
			}

			public void setGive_amount(UFDouble give_amount) {
				this.give_amount = give_amount;
			}

			public String getVemo() {
				return vemo;
			}

			public void setVemo(String vemo) {
				this.vemo = vemo;
			}

			public UFDate getVapprovedate() {
				return vapprovedate;
			}

			public void setVapprovedate(UFDate vapprovedate) {
				this.vapprovedate = vapprovedate;
			}

			public String getCtcode() {
				return ctcode;
			}

			public void setCtcode(String ctcode) {
				this.ctcode = ctcode;
			}

			public String getCtname() {
				return ctname;
			}

			public void setCtname(String ctname) {
				this.ctname = ctname;
			}

			public String getPk_contract() {
				return pk_contract;
			}

			public void setPk_contract(String pk_contract) {
				this.pk_contract = pk_contract;
			}

			public String getPk_dept() {
				return pk_dept;
			}

			public void setPk_dept(String pk_dept) {
				this.pk_dept = pk_dept;
			}

			public String getXmname() {
				return xmname;
			}

			public void setXmname(String xmname) {
				this.xmname = xmname;
			}

			public String getHkbillno() {
				return hkbillno;
			}

			public void setHkbillno(String hkbillno) {
				this.hkbillno = hkbillno;
			}

			public UFDouble getBzbl() {
				return bzbl;
			}

			public void setBzbl(UFDouble bzbl) {
				this.bzbl = bzbl;
			}

			public Integer getDr() {
				return dr;
			}

			public void setDr(Integer dr) {
				this.dr = dr;
			}

			public String getPk_bizong() {
				return pk_bizong;
			}

			public void setPk_bizong(String pk_bizong) {
				this.pk_bizong = pk_bizong;
			}

			public UFDouble getRmb_amount() {
				return rmb_amount;
			}

			public void setRmb_amount(UFDouble rmb_amount) {
				this.rmb_amount = rmb_amount;
			}

			public UFDateTime getTs() {
				return ts;
			}

			public void setTs(UFDateTime ts) {
				this.ts = ts;
			}

		/**
	   * 属性dbilldate的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return UFDate
	   */
	 public UFDate getDbilldate() {
		 return dbilldate;
	  }   
	  
     /**
	   * 属性dbilldate的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newDbilldate UFDate
	   */
	public void setDbilldate(UFDate newDbilldate) {
		
		dbilldate = newDbilldate;
	 } 	  
       
        /**
	   * 属性vbillno的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return String
	   */
	 public String getVbillno() {
		 return vbillno;
	  }   
	  
     /**
	   * 属性vbillno的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newVbillno String
	   */
	public void setVbillno(String newVbillno) {
		
		vbillno = newVbillno;
	 } 	  
       
        /**
	   * 属性hk_amount的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return UFDouble
	   */
	 public UFDouble getHk_amount() {
		 return hk_amount;
	  }   
	  
     /**
	   * 属性hk_amount的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newHk_amount UFDouble
	   */
	public void setHk_amount(UFDouble newHk_amount) {
		
		hk_amount = newHk_amount;
	 } 	  
       
        /**
	   * 属性vapproveid的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return String
	   */
	 public String getVapproveid() {
		 return vapproveid;
	  }   
	  
     /**
	   * 属性vapproveid的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newVapproveid String
	   */
	public void setVapproveid(String newVapproveid) {
		
		vapproveid = newVapproveid;
	 } 	  
       
        /**
	   * 属性pk_hkdh的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return String
	   */
	 public String getPk_hksw() {
		 return pk_hksw;
	  }   
	  
     /**
	   * 属性pk_hkdh的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newPk_hkdh String
	   */
	public void setPk_hksw(String newPk_hkcd) {
		
		pk_hksw = newPk_hkcd;
	 } 	  
       
        /**
	   * 属性pk_cust的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return String
	   */
	 public String getPk_cust() {
		 return pk_cust;
	  }   
	  
     /**
	   * 属性pk_cust的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newPk_cust String
	   */
	public void setPk_cust(String newPk_cust) {
		
		pk_cust = newPk_cust;
	 } 	  
       
        /**
	   * 属性voperid的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return String
	   */
	 public String getVoperid() {
		 return voperid;
	  }   
	  
     /**
	   * 属性voperid的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newVoperid String
	   */
	public void setVoperid(String newVoperid) {
		
		voperid = newVoperid;
	 } 	  
       
        /**
	   * 属性remark的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return String
	   */
	 public String getRemark() {
		 return remark;
	  }   
	  
     /**
	   * 属性remark的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newRemark String
	   */
	public void setRemark(String newRemark) {
		
		remark = newRemark;
	 } 	  
       
        /**
	   * 属性vbillstatus的Getter方法.
	   *
	   * 创建日期:2014-9-25
	   * @return Integer
	   */
	 public Integer getVbillstatus() {
		 return vbillstatus;
	  }   
	  
     /**
	   * 属性vbillstatus的Setter方法.
	   *
	   * 创建日期:2014-9-25
	   * @param newVbillstatus Integer
	   */
	public void setVbillstatus(Integer newVbillstatus) {
		
		vbillstatus = newVbillstatus;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2014-9-25
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_hksw == null) {
			errFields.add(new String("pk_hksw"));
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
	  * 创建日期:2014-9-25
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2014-9-25
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_hksw";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2014-9-25
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dh_hksw";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2014-9-25
	  */
	public HkswVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2014-9-25
	 * @param newPk_hkdh 主键值
	 */
	 public HkswVO(String newPk_hkcd) {
		
		// 为主键字段赋值:
		 pk_hksw = newPk_hkcd;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2014-9-25
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_hksw;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2014-9-25
	  * @param newPk_hkdh  String    
	  */
	 public void setPrimaryKey(String newPk_hkcd) {
				
				pk_hksw = newPk_hkcd; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2014-9-25
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "dh_hksw"; 
				
	 } 
} 
