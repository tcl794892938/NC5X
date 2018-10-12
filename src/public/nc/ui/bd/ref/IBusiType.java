package nc.ui.bd.ref;


/**
 * 参照业务信息定义
 * @deprecated since V5.5
 * 获得所有参照的集合请用nc.ui.bd.ref.RefInfoHelper.getRefinfoVos()
 * 然后通过 nc.ui.bd.ref.RefCall.getUIRefPaneByRefInfo(String pk_refinfo, String pk_corp) 得到一个参照引用
 */
public interface IBusiType {
	public final static int GRID = IRefConst.GRID; // GRID

	public final static int TREE = IRefConst.TREE; // TREE

	public final static int GRIDTREE = IRefConst.GRIDTREE; // GRIDTREE
	
	public final String[] BUSITYPE = { "人员基本档案", "人员档案", "部门档案", "公司目录(集团)S",
			"公司目录", "公司目录(集团)", "客商基本档案", "客商档案", "客户档案", "供应商档案", "客商档案包含冻结",
			"客户档案包含冻结", "供应商档案包含冻结", "客商辅助核算", "客户辅助核算", "供应商辅助核算", "存货基本档案",
			"存货档案", "会计科目", "人员类别", "存货分类", "凭证类别", "收付款协议", "结算方式", "开户银行",
			"开户银行1", "开户银行2", "开户银行3", "仓库档案", "发运方式", "收发类别", "地区分类", "常用摘要",
			"公用自定义项", "公用自定义项(不包含引用基本档案)", "收支项目",
			"计量档案",
			"税目税率",
			"项目类型",
			"项目档案",
			"项目管理档案",
			"项目管理档案表格",// sxj 2003-07-02
			"货位档案", "外币档案", "客商发货地址", "自定义项档案", "成套件", "会计期间", "业务类型", "库存组织",
			"权限操作员", "操作员", "物料档案", "采购组织", "帐龄区间", "单据类型", "帐户", "收入项目",
			"支出项目", "客商档案(并集)", "权限公司目录", "权限公司目录(集团)", "销售组织", "现金流量项目",
			"自定义项档案列表", "票据类型", "票据类型1", "票据类型2", "票据类型3", "票据类型4", "票据类型5",
			"账户档案", "账户档案1", "人员档案HR",
			"基础档案资源列表",// sxj 2003-07-30
			"产品线档案",// sxj 2004-03-15
			"公司类别",// sxj 2004-03-17
			"结算单位",// sxj 2004-03-19
			"地点档案",// sxj 2004-04-01
			"结算中心",// sxj 2004-04-13
			"用户组",// 2004-06-10
			"会计期间方案",// 2004-12-10
			"科目方案",// 2004-12-10
			"核算账簿",// 2004-12-10
			"会计主体",// 2004-12-10
			"主体账簿",// 2004-12-10
			"日历", "计算器", "颜色", "部门档案HR", "集团会计科目", "会计科目多版本", "账簿主体", "结算单位树表",
			"内部账户", "固定资产核算账簿", "固定资产账簿公司", "角色", "仓库档案多公司", "库存组织多公司","银行类别","银行档案","客户分类","供应商分类","影响因素单据类型",
			"可支付开户银行","已支付开户银行","会计年度","公司目录1"};
	public final String[] TABLENAME = { "bd_psnbasdoc", "bd_psndoc",
			"bd_deptdoc", "bd_corp", "bd_corp", "bd_corp", "bd_cubasdoc",
			"bd_cumandoc", "bd_cumandoc", "bd_cumandoc", "bd_cumandoc",
			"bd_cumandoc", "bd_cumandoc", "bd_cumandoc", "bd_cumandoc",
			"bd_cumandoc", "bd_invbasdoc", "bd_invmandoc", "bd_accsubj",
			"bd_psncl", "bd_invcl", "bd_vouchertype", "bd_payterm",
			"bd_balatype", "bd_accbank", "bd_accbank", "bd_accbank",
			"bd_accbank", "bd_stordoc", "bd_sendtype", "bd_rdcl", "bd_areacl",
			"bd_comabstr", "bd_defdef", "bd_defdef", "bd_costsubj",
			"bd_measdoc", "bd_taxitems", "bd_jobtype", "bd_jobbasfil",
			"bd_jobmngfil", "bd_jobmngfil", "bd_cargdoc", "bd_currtype",
			"bd_custaddr", "bd_defdoc", "bd_setpart", "bd_accperiod",
			"bd_busitype", "bd_calbody", "sm_user", "sm_user",
			"bd_produce,bd_invbasdoc", "bd_purorg", "pub_timecontrol",
			"arap_djlx", "bd_accid", "bd_costsubj", "bd_costsubj",
			"bd_cumandoc", "bd_corp", "bd_corp", "bd_salestru", "bd_cashflow",
			"bd_defdoclist", "arap_note_type", "arap_note_type",
			"arap_note_type", "arap_note_type", "arap_note_type",
			"arap_note_type", "bd_accid", "bd_accid", "bd_psndoc",
			"bd_bdinfo",
			"bd_prodline",
			"bd_corpkind",
			"bd_settleunit",
			"bd_address",
			"bd_corp",
			"sm_group",
			"bd_accperiodscheme",// 2004-12-10
			"bd_subjscheme",// 2004-12-10
			"bd_glbook",// 2004-12-10
			"bd_glorg",// 2004-12-10
			"bd_glorgbook",// 2004-12-10
			"bd_deptdoc", "bd_accsubj", "bd_accsubj", "bd_glorgbook",
			"bd_settleunit", "bd_accid", "bd_glbook", "bd_glorgbook",
			"sm_role", "bd_stordoc", "bd_calbody","bd_bank","bd_bank",
			"bd_custclass","bd_supplierclass","bd_billtype",
			"bd_accbank","bd_accbank","bd_accperiod","bd_corp"};

	public final String[] BUSITYPE_RESID = {
			"UPPref-000514"/* @res "人员基本档案" */,
			"UPPref-000443"/* @res "人员档案" */,
			"UPPref-000005"/* @res "部门档案" */,
			"UPPref-000002"/* @res "公司目录(集团)S" */,
			"UPPref-000004"/* @res "公司目录" */,
			"UPPref-000000"/* @res "公司目录(集团)" */,
			"UC000-0001581"/* @res "客商基本档案" */,
			"UC000-0001584"/* @res "客商档案" */,
			"UPPref-000444"/* @res "客户档案" */,
			"UPPref-000445"/* @res "供应商档案" */,
			"UPPref-000446"/* @res "客商档案包含冻结" */,
			"UPPref-000447"/* @res "客户档案包含冻结" */,
			"UPPref-000448"/* @res "供应商档案包含冻结" */,
			"UC000-0001584"/* @res "客商档案辅助核算" */,
			"UPPref-000444"/* @res "客户档案辅助核算" */,
			"UPPref-000445"/* @res "供应商档案辅助核算" */,
			"UPPref-000367"/* @res "存货基本档案" */,
			"UPPref-000449"/* @res "存货档案" */,
			"UPPref-000006"/* @res "会计科目" */,
			"UC000-0000140"/* @res "人员类别" */,
			"UC000-0001443"/* @res "存货分类" */,
			"UC000-0000479"/* @res "凭证类别" */,
			"UC000-0002200"/* @res "收付款协议" */,
			"UC000-0003249"/* @res "结算方式" */,
			"UC000-0001896"/* @res "开户银行" */,
			"UPPref-000450"/* @res "开户银行1" */,
			"UPPref-000451"/* @res "开户银行2" */,
			"UPPref-000452"/* @res "开户银行3" */,
			"UC000-0000160"/* @res "仓库档案" */,
			"UC000-0001024"/* @res "发运方式" */,
			"UC000-0002209"/* @res "收发类别" */,
			"UC000-0001235"/* @res "地区分类" */,
			"UPPref-000453"/* @res "常用摘要" */,
			"UPPref-000454"/* @res "公用自定义项" */,
			"UPPref-000454"/* @res "公用自定义项(不包含引用基本档案)" */,
			"UC000-0002217"/* @res "收支项目" */,
			"UPPref-000455"/* @res "计量档案" */,
			"UPPref-000456"/* @res "税目税率" */,
			"UPPref-000380"/* @res "项目类型" */,
			"UPPref-000457"/* @res "项目档案" */,
			"UC000-0004175"/* @res "项目管理档案" */,
			// "UPPref-000458"/* @res "项目管理档案表格" */,// sxj 2003-07-02
			"UC000-0004175"/* @res "项目管理档案表格" */,// sxj 2003-07-02
			"UPPref-000459"/* @res "货位档案" */,
			"UPPref-000460"/* @res "外币档案" */,
			"UPPref-000461"/* @res "客商发货地址" */,
			"UPPref-000462"/* @res "自定义项档案" */,
			"UC000-0001953"/* @res "成套件" */,
			"UC000-0000240"/* @res "会计期间" */,
			"UC001-0000003"/* @res "业务类型" */,
			"UC000-0001825"/* @res "库存组织" */,
			"UPPref-000463"/* @res "权限操作员" */,
			"UC000-0002188"/* @res "操作员" */,
			"UPPref-000464"/* @res "物料档案" */,
			"UC000-0004091"/* @res "采购组织" */,
			"UPPref-000465"/* @res "帐龄区间" */,
			"UC000-0000807"/* @res "单据类型" */,
			"UC000-0001766"/* @res "帐户" */,
			"UPPref-000466"/* @res "收入项目" */,
			"UPPref-000467"/* @res "支出项目" */,
			"UPPref-000468"/* @res "客商档案(并集)" */,
			"UPPref-000469"/* @res "权限公司目录" */,
			"UPPref-000469"/* @res ""权限公司目录(集团)" */,
			"UC000-0004128"/* @res "销售组织" */,
			"UC000-0002922"/* @res "现金流量项目" */,
			"UPPref-000470"/* @res "自定义项档案列表" */,
			"UC000-0003020"/* @res "票据类型" */,
			"UPPref-000472"/* @res "票据类型1" */,
			"UPPref-000473"/* @res "票据类型2" */,
			"UPPref-000474"/* @res "票据类型3" */,
			"UPPref-000475"/* @res "票据类型4" */,
			"UPPref-000476"/* @res "票据类型5" */,
			"UPPref-000477"/* @res "账户档案" */,
			"UPPref-000477"/* @res "账户档案1" */,
			"UPPref-000478"/* @res "人员档案HR" */,
			"UPPref-000479"/* @res "基础档案资源列表" */,// sxj 2003-07-30
			"UPPref-000480"/* @res "产品线档案" */,// sxj 2004-03-15
			"UPPref-000481"/* @res "公司类别" */,// sxj 2004-03-17
			"UC000-0003242"/* @res "结算单位" */,// sxj 2004-03-19
			"UPPref-000482"/* @res "地点档案" */,// sxj 2004-04-01
			"UC000-0003234"/* @res "结算中心" */,// sxj 2004-04-13
			"UPPref-000483"/* @res "用户组" */,// 2004-06-10
			"UPPref-000429"/* @res "会计期间方案" */,// 2004-12-10
			"UPPref-000428"/* @res "科目方案" */,// 2004-12-10
			"UPPref-000484"/* @res "核算账簿" */,// 2004-12-10
			"UPPref-000383"/* @res "会计主体" */,// 2004-12-10
			"UPPref-000485"/* @res "主体账簿" */, "UPPref-000486"/* @res "日历" */,
			"UPPref-000487"/* @res "计算器" */, "UPPref-000488"/* @res "颜色" */,
			"UPPref-000005"/* 部门档案HR=部门档案 */, "UPPref-000510"/* 集团会计科目 */,
			"UPPref-000511"/* 会计科目多版本 */,
			"UPPref-000509"/* 账簿主体 */,
			"UPPref-000508"/* @res "结算单位树表" */,// sxj 2004-03-19
			"UPPref-000507"/* 内部账户 */, "UPPref-000512"/* 固定资产核算账簿 */,
			"UPPref-000513"/* 固定资产账簿公司 */, "UPPref-000504"/*
															 * @res "角色" 101612
															 */, "UPPref-000515"/* 仓库档案多公司 */, "UPPref-000516"/*
													 * @res "库存组织多公司" 101612
													 */

	       ,"银行类别","银行档案","客户分类","供应商分类","影响因素单据类型",
	       "UPPref-000518"/* @res 可支付开户银行 */,"UPPref-000519"/* @res 已支付开户银行 */,"会计年度","公司目录1"
			};

	

	public final String[] IMPL_CLASSNAME = {
			"nc.ui.bd.ref.busi.PsnbasdocDefaultRefModel"/* @res "人员基本档案" */,
			"nc.ui.bd.ref.busi.PsndocDefaulRefModel"/* @res "人员档案" */,
			"nc.ui.bd.ref.busi.DeptdocDefaultRefModel"/* @res "部门档案" */,
			"nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel"/*
															 * @res "公司目录(集团)S"
															 * ,表型
															 */,
			"nc.ui.bd.ref.busi.CorpDefaultRefModel"/* @res "公司目录" */,
			"nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel"/* @res "公司目录(集团)" */,
			"nc.ui.bd.ref.busi.CustbasdocDefaulteRefModel"/* @res "客商基本档案" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "客商档案" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "客户档案" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "供应商档案" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "客商档案包含冻结" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "客户档案包含冻结" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "供应商档案包含冻结" */,
			"nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel"/* @res "客商档案辅助核算" */,
			"nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel"/* @res "客户档案辅助核算" */,
			"nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel"/* @res "供应商档案辅助核算" */,
			"nc.ui.bd.ref.busi.InvbasdocDefaultRefModel"/* @res "存货基本档案" */,
			"nc.ui.bd.ref.busi.InvmandocDefaultRefModel"/* @res "存货档案" */,
			"nc.ui.bd.ref.busi.AccsubjDefaultRefModel"/* @res "会计科目" */,
			"nc.ui.bd.ref.busi.PsnclDefaultRefModel"/* @res "人员类别" */,
			"nc.ui.bd.ref.busi.InvclDefaultRefModel"/* @res "存货分类" */,
			"nc.ui.bd.ref.busi.VoucherTypeDefaultRefModel"/* @res "凭证类别" */,
			"nc.ui.bd.ref.busi.PaytermDefaultRefModel"/* @res "收付款协议" */,
			"nc.ui.bd.ref.busi.BalanceTypeDefaultRefModel"/* @res "结算方式" */,
			"nc.ui.bd.ref.busi.AccBankDefaulteRefModel"/* @res "开户银行" */,
			"nc.ui.bd.ref.busi.AccBank123RefModel"/* @res "开户银行1" */,
			"nc.ui.bd.ref.busi.AccBank123RefModel"/* @res "开户银行2" */,
			"nc.ui.bd.ref.busi.AccBank123RefModel"/* @res "开户银行3" */,
			"nc.ui.bd.ref.busi.StorDocDefaulteRefModel"/* @res "仓库档案" */,
			"nc.ui.bd.ref.busi.SendTypeDefaultRefModel"/* @res "发运方式" */,
			"nc.ui.bd.ref.busi.RdclDefaultRefModel"/* @res "收发类别" */,
			"nc.ui.bd.ref.busi.AreaclDefaultRefModel"/* @res "地区分类" */,
			"nc.ui.bd.ref.busi.ComAbstrDefaultRefModel"/* @res "常用摘要" */,
			"nc.ui.bd.ref.busi.DefdefDefaultRefModel"/* @res "公用自定义项" */,
			"nc.ui.bd.ref.busi.DefdefDefaultRefModel"/*
														 * @res
														 * "公用自定义项(不包含引用基本档案)"
														 */,
			"nc.ui.bd.ref.busi.CostsubjDefaultRefModel"/* @res "收支项目" */,
			"nc.ui.bd.ref.busi.MeasdocDefaultRefModel"/* @res "计量档案" */,
			"nc.ui.bd.ref.busi.TaxDefaultRefModel"/* @res "税目税率" */,
			"nc.ui.bd.ref.busi.JobtypeDefaultRefModel"/* @res "项目类型" */,
			"nc.ui.bd.ref.busi.JobbasfilDefaultRefModel"/* @res "项目档案" */,
			"nc.ui.bd.ref.busi.JobmngfilDefaultRefModel"/* @res "项目管理档案" */,
			"nc.ui.bd.ref.busi.JobmngfilgridRefModel"/* @res "项目管理档案表格" */,// sxj
			// 2003-07-02
			"nc.ui.bd.ref.busi.CargdocDefaultRefModel"/* @res "货位档案" */,
			"nc.ui.bd.ref.busi.CurrtypeDefaultRefModel"/* @res "外币档案" */,
			"nc.ui.bd.ref.busi.CustAddrDefaultRefModel"/* @res "客商发货地址" */,
			"nc.ui.bd.ref.busi.DefdocDefaultRefModel"/* @res "自定义项档案" */,
			"nc.ui.bd.ref.busi.SetPartDefaultRefModel"/* @res "成套件" */,
			"nc.ui.bd.ref.busi.AccPeriodDefaultRefModel"/* @res "会计期间" */,
			"nc.ui.bd.ref.busi.BusiTypeDefaultRefModel"/* @res "业务类型" */,
			"nc.ui.bd.ref.busi.CalBodyDefaultRefModel"/* @res "库存组织" */,
			"nc.ui.bd.ref.busi.OperatorDefaultRefModel"/* @res "权限操作员" */,
			"nc.ui.bd.ref.busi.OperatorDefaultRefModel"/* @res "操作员" */,
			"nc.ui.bd.ref.busi.ProduceDefaultRefModel"/* @res "物料档案" */,
			"nc.ui.bd.ref.busi.PurorgDefaultRefModel"/* @res "采购组织" */,
			"nc.ui.bd.ref.busi.PubTimeControlRefModel"/* @res "帐龄区间" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "单据类型" */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* @res "帐户" */,
			"nc.ui.bd.ref.busi.CostSubj_ArapDefaultRefModel"/* @res "收入项目" */,
			"nc.ui.bd.ref.busi.CostSubj_ArapDefaultRefModel"/* @res "支出项目" */,
			"nc.ui.bd.ref.busi.CustmandocUnitDefaultRefModel"/* 客商档案(并集) */,
			"nc.ui.bd.ref.busi.CorpDefaultRefModel"/* @res "权限公司目录" */,
			"nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel"/* @res "权限公司目录(集团)" */,
			"nc.ui.bd.ref.busi.SaleStruDefaultRefModel"/* @res "销售组织" */,
			"nc.ui.bd.ref.busi.CashflowDefaultRefModel"/* @res "现金流量项目" */,
			"nc.ui.bd.ref.busi.DefdoclistDefaultRefModel"/* @res "自定义项档案列表" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "票据类型" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "票据类型1" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "票据类型2" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "票据类型3" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "票据类型4" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "票据类型5" */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* @res "账户档案" */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* @res "账户档案1" */,
			"nc.ui.bd.ref.busi.PsndocDefaulRefModel"/* @res "人员档案HR" */,
			"nc.ui.bd.ref.DefaultRefModel_multiLang"/* @res "基础档案资源列表" */,// sxj
			// 2003-07-30
			"nc.ui.bd.ref.busi.ProdLineDefaultRefModel"/* @res "产品线档案" */,// sxj
			// 2004-03-15
			"nc.ui.bd.ref.busi.CorpKindDefaultRefModel"/* @res "公司类别" */,// sxj
			// 2004-03-17
			"nc.ui.bd.ref.busi.SettleUnitDefaultRefModel"/* @res "结算单位" */,// sxj
			// 2004-03-19
			"nc.ui.bd.ref.busi.AddressDefaultRefModel"/* @res "地点档案" */,// sxj
			// 2004-04-01
			"nc.ui.bd.ref.busi.SettleCenterDefaultRefModel"/* @res "结算中心" */,// sxj
			// 2004-04-13
			"nc.ui.bd.ref.busi.UserGroupDefaultRefModel"/* @res "用户组" */,// 2004-06-10
			"nc.ui.bd.ref.busi.AccPeriodSchemeDefaultRefModel"/* @res "会计期间方案" */,// 2004-12-10
			"nc.ui.bd.ref.busi.AccsubjSchemeDefaultRefModel"/* @res "科目方案" */,// 2004-12-10
			"nc.ui.bd.ref.busi.GlbookDefaultRefModel"/* @res "核算账簿" */,// 2004-12-10
			"nc.ui.bd.ref.busi.GlOrgDefaulRefModel"/* @res "会计主体" */,// 2004-12-10
			"nc.ui.bd.ref.busi.GlorgbookDefaultRefModel"/* @res "主体账簿" */,
			""/* @res "日历" */, ""/* @res "计算器" */, ""/* @res "颜色" */,
			"nc.ui.bd.ref.busi.DeptdocDefaultRefModel"/* 部门档案HR=部门档案 */,
			"nc.ui.bd.ref.busi.AccsubjDefaultRefModel"/* 集团会计科目=会计科目 */,
			"nc.ui.bd.ref.busi.AccsubjDefaultRefModel"/* 会计科目多版本=会计科目 */,
			"nc.ui.bd.ref.busi.GlbookOrgDefaultTreeModel"/* 账簿主体 */,
			"nc.ui.bd.ref.busi.SettleUnitDefaultGridTreeModel"/* 结算单位树表 */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* 内部账户 */,
			"nc.ui.bd.ref.busi.GlbookDefaultRefModel"/* 固定资产核算账簿 */,
			"nc.ui.bd.ref.busi.GlbookcorpDefaultRefModel"/* 固定资产账簿公司 */,
			"nc.ui.bd.ref.busi.RoleDefaultModel",/* 角色 */
			"nc.ui.bd.ref.busi.StorDocMultiCorpDefaultRefModel",/* 仓库档案多公司 */
			"nc.ui.bd.ref.busi.CalBodyMultiCorpDefaultRefModel"/* 库存组织多公司 */,
			"nc.ui.bd.ref.busi.BanktypeDefaultRefModel"/*银行类别*/,
			"nc.ui.bd.ref.busi.BankDefaultRefModel"/*银行档案*/,
			"nc.ui.bd.ref.busi.CustClassDefaultRefModel"/*客户分类*/,
			"nc.ui.bd.ref.busi.SupplierDefaultRefModel" /*供应商分类*/,
			"nc.ui.bd.ref.busi.BillTypeDefaultRefModel" /*影响因素单据类型*/,
			"nc.ui.bd.ref.busi.AccBankPayRefModel", /**可支付开户银行 */
			"nc.ui.bd.ref.busi.AccBankPayRefModel",/**已付开户银行 */
			"nc.ui.bd.ref.busi.AccPeriodYearRefTreeModel" /**会计年度*/,
			"nc.ui.bd.ref.busi.CorpDefaultRefModel1"
			};
}