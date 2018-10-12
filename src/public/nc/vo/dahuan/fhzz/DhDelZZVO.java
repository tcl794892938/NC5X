  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dahuan.fhzz;
   	
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
 * 创建日期:2014-1-21
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DhDelZZVO extends SuperVO {
           
             public String pk_contract;
             public String httype;
             public String htcode;             
             public String htname;
             public String xmcode;
             public String xmname;
             public UFDouble dey_amount;
             public UFDouble dhjetotal;
             public UFDouble dhsyje;
             public String isdel;
             public String custname;
            
             public static final String  PK_CONTRACT="pk_contract";   
             public static final String  HTTYPE="httype";   
             public static final String  HTCODE="htcode";   
             public static final String  HTNAME="htname";   
             public static final String  XMCODE="xmcode";   
             public static final String  XMNAME="xmname";   
             public static final String  DEY_AMOUNT="dey_amount";   
             public static final String  ISDEL="isdel";   
             
             
        
	public String getCustname() {
				return custname;
			}

			public void setCustname(String custname) {
				this.custname = custname;
			}

	public UFDouble getDhjetotal() {
				return dhjetotal;
			}

			public void setDhjetotal(UFDouble dhjetotal) {
				this.dhjetotal = dhjetotal;
			}

			public UFDouble getDhsyje() {
				return dhsyje;
			}

			public void setDhsyje(UFDouble dhsyje) {
				this.dhsyje = dhsyje;
			}

	public UFDouble getDey_amount() {
				return dey_amount;
			}

			public void setDey_amount(UFDouble dey_amount) {
				this.dey_amount = dey_amount;
			}

			public String getHtcode() {
				return htcode;
			}

			public void setHtcode(String htcode) {
				this.htcode = htcode;
			}

			public String getHtname() {
				return htname;
			}

			public void setHtname(String htname) {
				this.htname = htname;
			}

			public String getHttype() {
				return httype;
			}

			public void setHttype(String httype) {
				this.httype = httype;
			}

			public String getIsdel() {
				return isdel;
			}

			public void setIsdel(String isdel) {
				this.isdel = isdel;
			}

			public String getPk_contract() {
				return pk_contract;
			}

			public void setPk_contract(String pk_contract) {
				this.pk_contract = pk_contract;
			}

			public String getXmcode() {
				return xmcode;
			}

			public void setXmcode(String xmcode) {
				this.xmcode = xmcode;
			}

			public String getXmname() {
				return xmname;
			}

			public void setXmname(String xmname) {
				this.xmname = xmname;
			}

	public String getParentPKFieldName() {
	 	    return null;
	}   
    
	public String getPKFieldName() {
	 	  return "pk_contract";
	}
    
	public String getTableName() {
		return "v_dhdelivery";
	}    

	public DhDelZZVO() {
			   super();	
	}    
    
	public DhDelZZVO(String newPk_contract) {
		 pk_contract = newPk_contract;
    }
    
	 public String getPrimaryKey() {
		 return pk_contract;
   }

	 public void setPrimaryKey(String newPk_contract) {
		 pk_contract = newPk_contract; 
	 } 
           
	 public String getEntityName() {
	   return "v_dhdelivery"; 
	 } 
} 
