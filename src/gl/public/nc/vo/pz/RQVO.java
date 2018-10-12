	package nc.vo.pz;
	
	import nc.vo.pub.SuperVO;
	
	public class RQVO extends SuperVO{

		private String pk_rq;
		private String styear;
		private String stmonth;
		private Integer dr;
		private String ts;
		
		
		public Integer getDr() {
			return dr;
		}

		public void setDr(Integer dr) {
			this.dr = dr;
		}

		

		public String getPk_rq() {
			return pk_rq;
		}

		public void setPk_rq(String pk_rq) {
			this.pk_rq = pk_rq;
		}

		public String getTs() {
			return ts;
		}

		public void setTs(String ts) {
			this.ts = ts;
		}

		

		public String getStmonth() {
			return stmonth;
		}

		public void setStmonth(String stmonth) {
			this.stmonth = stmonth;
		}

		public String getStyear() {
			return styear;
		}

		public void setStyear(String styear) {
			this.styear = styear;
		}

		@Override
		public String getPKFieldName() {
			// TODO Auto-generated method stub
			return "pk_rq";
		}

		@Override
		public String getParentPKFieldName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getTableName() {
			// TODO Auto-generated method stub
			return "rq";
		}
	
		 
	     
	

	} 
