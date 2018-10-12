  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dahuan.fkprintquery;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
	import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2014-2-25
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DhFkbdprintDVO extends SuperVO {
           
             public String htcode;
             public String pk_fkbd_d;
             public String pk_fkbd;
             public UFDouble htamount;
             public String say_content;
             public String say_type;
            
             public static final String  HTCODE="htcode";   
             public static final String  PK_FKBD_D="pk_fkbd_d";   
             public static final String  PK_FKBD="pk_fkbd";   
             public static final String  HTAMOUNT="htamount";  
             public static final String  SAY_CONTENT="say_content";   
             public static final String  SAY_TYPE="say_type"; 
      
             
             
    
        public String getSay_content() {
				return say_content;
			}

			public void setSay_content(String say_content) {
				this.say_content = say_content;
			}

			public String getSay_type() {
				return say_type;
			}

			public void setSay_type(String say_type) {
				this.say_type = say_type;
			}

		/**
	   * 属性htcode的Getter方法.
	   *
	   * 创建日期:2014-2-25
	   * @return String
	   */
	 public String getHtcode() {
		 return htcode;
	  }   
	  
     /**
	   * 属性htcode的Setter方法.
	   *
	   * 创建日期:2014-2-25
	   * @param newHtcode String
	   */
	public void setHtcode(String newHtcode) {
		
		htcode = newHtcode;
	 } 	  
       
        /**
	   * 属性pk_fkbd_d的Getter方法.
	   *
	   * 创建日期:2014-2-25
	   * @return String
	   */
	 public String getPk_fkbd_d() {
		 return pk_fkbd_d;
	  }   
	  
     /**
	   * 属性pk_fkbd_d的Setter方法.
	   *
	   * 创建日期:2014-2-25
	   * @param newPk_fkbd_d String
	   */
	public void setPk_fkbd_d(String newPk_fkbd_d) {
		
		pk_fkbd_d = newPk_fkbd_d;
	 } 	  
       
        /**
	   * 属性pk_fkbd的Getter方法.
	   *
	   * 创建日期:2014-2-25
	   * @return String
	   */
	 public String getPk_fkbd() {
		 return pk_fkbd;
	  }   
	  
     /**
	   * 属性pk_fkbd的Setter方法.
	   *
	   * 创建日期:2014-2-25
	   * @param newPk_fkbd String
	   */
	public void setPk_fkbd(String newPk_fkbd) {
		
		pk_fkbd = newPk_fkbd;
	 } 	  
       
        /**
	   * 属性htamount的Getter方法.
	   *
	   * 创建日期:2014-2-25
	   * @return UFDouble
	   */
	 public UFDouble getHtamount() {
		 return htamount;
	  }   
	  
     /**
	   * 属性htamount的Setter方法.
	   *
	   * 创建日期:2014-2-25
	   * @param newHtamount UFDouble
	   */
	public void setHtamount(UFDouble newHtamount) {
		
		htamount = newHtamount;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2014-2-25
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_fkbd_d == null) {
			errFields.add(new String("pk_fkbd_d"));
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
	  * 创建日期:2014-2-25
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 		return "pk_fkbd";
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2014-2-25
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_fkbd_d";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2014-2-25
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dh_fkbdprint_d";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2014-2-25
	  */
	public DhFkbdprintDVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2014-2-25
	 * @param newPk_fkbd_d 主键值
	 */
	 public DhFkbdprintDVO(String newPk_fkbd_d) {
		
		// 为主键字段赋值:
		 pk_fkbd_d = newPk_fkbd_d;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2014-2-25
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_fkbd_d;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2014-2-25
	  * @param newPk_fkbd_d  String    
	  */
	 public void setPrimaryKey(String newPk_fkbd_d) {
				
				pk_fkbd_d = newPk_fkbd_d; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2014-2-25
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "dh_fkbdprint_d"; 
				
	 } 
} 
