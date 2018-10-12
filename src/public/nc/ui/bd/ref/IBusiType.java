package nc.ui.bd.ref;


/**
 * ����ҵ����Ϣ����
 * @deprecated since V5.5
 * ������в��յļ�������nc.ui.bd.ref.RefInfoHelper.getRefinfoVos()
 * Ȼ��ͨ�� nc.ui.bd.ref.RefCall.getUIRefPaneByRefInfo(String pk_refinfo, String pk_corp) �õ�һ����������
 */
public interface IBusiType {
	public final static int GRID = IRefConst.GRID; // GRID

	public final static int TREE = IRefConst.TREE; // TREE

	public final static int GRIDTREE = IRefConst.GRIDTREE; // GRIDTREE
	
	public final String[] BUSITYPE = { "��Ա��������", "��Ա����", "���ŵ���", "��˾Ŀ¼(����)S",
			"��˾Ŀ¼", "��˾Ŀ¼(����)", "���̻�������", "���̵���", "�ͻ�����", "��Ӧ�̵���", "���̵�����������",
			"�ͻ�������������", "��Ӧ�̵�����������", "���̸�������", "�ͻ���������", "��Ӧ�̸�������", "�����������",
			"�������", "��ƿ�Ŀ", "��Ա���", "�������", "ƾ֤���", "�ո���Э��", "���㷽ʽ", "��������",
			"��������1", "��������2", "��������3", "�ֿ⵵��", "���˷�ʽ", "�շ����", "��������", "����ժҪ",
			"�����Զ�����", "�����Զ�����(���������û�������)", "��֧��Ŀ",
			"��������",
			"˰Ŀ˰��",
			"��Ŀ����",
			"��Ŀ����",
			"��Ŀ������",
			"��Ŀ���������",// sxj 2003-07-02
			"��λ����", "��ҵ���", "���̷�����ַ", "�Զ������", "���׼�", "����ڼ�", "ҵ������", "�����֯",
			"Ȩ�޲���Ա", "����Ա", "���ϵ���", "�ɹ���֯", "��������", "��������", "�ʻ�", "������Ŀ",
			"֧����Ŀ", "���̵���(����)", "Ȩ�޹�˾Ŀ¼", "Ȩ�޹�˾Ŀ¼(����)", "������֯", "�ֽ�������Ŀ",
			"�Զ�������б�", "Ʊ������", "Ʊ������1", "Ʊ������2", "Ʊ������3", "Ʊ������4", "Ʊ������5",
			"�˻�����", "�˻�����1", "��Ա����HR",
			"����������Դ�б�",// sxj 2003-07-30
			"��Ʒ�ߵ���",// sxj 2004-03-15
			"��˾���",// sxj 2004-03-17
			"���㵥λ",// sxj 2004-03-19
			"�ص㵵��",// sxj 2004-04-01
			"��������",// sxj 2004-04-13
			"�û���",// 2004-06-10
			"����ڼ䷽��",// 2004-12-10
			"��Ŀ����",// 2004-12-10
			"�����˲�",// 2004-12-10
			"�������",// 2004-12-10
			"�����˲�",// 2004-12-10
			"����", "������", "��ɫ", "���ŵ���HR", "���Ż�ƿ�Ŀ", "��ƿ�Ŀ��汾", "�˲�����", "���㵥λ����",
			"�ڲ��˻�", "�̶��ʲ������˲�", "�̶��ʲ��˲���˾", "��ɫ", "�ֿ⵵���๫˾", "�����֯�๫˾","�������","���е���","�ͻ�����","��Ӧ�̷���","Ӱ�����ص�������",
			"��֧����������","��֧����������","������","��˾Ŀ¼1"};
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
			"UPPref-000514"/* @res "��Ա��������" */,
			"UPPref-000443"/* @res "��Ա����" */,
			"UPPref-000005"/* @res "���ŵ���" */,
			"UPPref-000002"/* @res "��˾Ŀ¼(����)S" */,
			"UPPref-000004"/* @res "��˾Ŀ¼" */,
			"UPPref-000000"/* @res "��˾Ŀ¼(����)" */,
			"UC000-0001581"/* @res "���̻�������" */,
			"UC000-0001584"/* @res "���̵���" */,
			"UPPref-000444"/* @res "�ͻ�����" */,
			"UPPref-000445"/* @res "��Ӧ�̵���" */,
			"UPPref-000446"/* @res "���̵�����������" */,
			"UPPref-000447"/* @res "�ͻ�������������" */,
			"UPPref-000448"/* @res "��Ӧ�̵�����������" */,
			"UC000-0001584"/* @res "���̵�����������" */,
			"UPPref-000444"/* @res "�ͻ�������������" */,
			"UPPref-000445"/* @res "��Ӧ�̵�����������" */,
			"UPPref-000367"/* @res "�����������" */,
			"UPPref-000449"/* @res "�������" */,
			"UPPref-000006"/* @res "��ƿ�Ŀ" */,
			"UC000-0000140"/* @res "��Ա���" */,
			"UC000-0001443"/* @res "�������" */,
			"UC000-0000479"/* @res "ƾ֤���" */,
			"UC000-0002200"/* @res "�ո���Э��" */,
			"UC000-0003249"/* @res "���㷽ʽ" */,
			"UC000-0001896"/* @res "��������" */,
			"UPPref-000450"/* @res "��������1" */,
			"UPPref-000451"/* @res "��������2" */,
			"UPPref-000452"/* @res "��������3" */,
			"UC000-0000160"/* @res "�ֿ⵵��" */,
			"UC000-0001024"/* @res "���˷�ʽ" */,
			"UC000-0002209"/* @res "�շ����" */,
			"UC000-0001235"/* @res "��������" */,
			"UPPref-000453"/* @res "����ժҪ" */,
			"UPPref-000454"/* @res "�����Զ�����" */,
			"UPPref-000454"/* @res "�����Զ�����(���������û�������)" */,
			"UC000-0002217"/* @res "��֧��Ŀ" */,
			"UPPref-000455"/* @res "��������" */,
			"UPPref-000456"/* @res "˰Ŀ˰��" */,
			"UPPref-000380"/* @res "��Ŀ����" */,
			"UPPref-000457"/* @res "��Ŀ����" */,
			"UC000-0004175"/* @res "��Ŀ������" */,
			// "UPPref-000458"/* @res "��Ŀ���������" */,// sxj 2003-07-02
			"UC000-0004175"/* @res "��Ŀ���������" */,// sxj 2003-07-02
			"UPPref-000459"/* @res "��λ����" */,
			"UPPref-000460"/* @res "��ҵ���" */,
			"UPPref-000461"/* @res "���̷�����ַ" */,
			"UPPref-000462"/* @res "�Զ������" */,
			"UC000-0001953"/* @res "���׼�" */,
			"UC000-0000240"/* @res "����ڼ�" */,
			"UC001-0000003"/* @res "ҵ������" */,
			"UC000-0001825"/* @res "�����֯" */,
			"UPPref-000463"/* @res "Ȩ�޲���Ա" */,
			"UC000-0002188"/* @res "����Ա" */,
			"UPPref-000464"/* @res "���ϵ���" */,
			"UC000-0004091"/* @res "�ɹ���֯" */,
			"UPPref-000465"/* @res "��������" */,
			"UC000-0000807"/* @res "��������" */,
			"UC000-0001766"/* @res "�ʻ�" */,
			"UPPref-000466"/* @res "������Ŀ" */,
			"UPPref-000467"/* @res "֧����Ŀ" */,
			"UPPref-000468"/* @res "���̵���(����)" */,
			"UPPref-000469"/* @res "Ȩ�޹�˾Ŀ¼" */,
			"UPPref-000469"/* @res ""Ȩ�޹�˾Ŀ¼(����)" */,
			"UC000-0004128"/* @res "������֯" */,
			"UC000-0002922"/* @res "�ֽ�������Ŀ" */,
			"UPPref-000470"/* @res "�Զ�������б�" */,
			"UC000-0003020"/* @res "Ʊ������" */,
			"UPPref-000472"/* @res "Ʊ������1" */,
			"UPPref-000473"/* @res "Ʊ������2" */,
			"UPPref-000474"/* @res "Ʊ������3" */,
			"UPPref-000475"/* @res "Ʊ������4" */,
			"UPPref-000476"/* @res "Ʊ������5" */,
			"UPPref-000477"/* @res "�˻�����" */,
			"UPPref-000477"/* @res "�˻�����1" */,
			"UPPref-000478"/* @res "��Ա����HR" */,
			"UPPref-000479"/* @res "����������Դ�б�" */,// sxj 2003-07-30
			"UPPref-000480"/* @res "��Ʒ�ߵ���" */,// sxj 2004-03-15
			"UPPref-000481"/* @res "��˾���" */,// sxj 2004-03-17
			"UC000-0003242"/* @res "���㵥λ" */,// sxj 2004-03-19
			"UPPref-000482"/* @res "�ص㵵��" */,// sxj 2004-04-01
			"UC000-0003234"/* @res "��������" */,// sxj 2004-04-13
			"UPPref-000483"/* @res "�û���" */,// 2004-06-10
			"UPPref-000429"/* @res "����ڼ䷽��" */,// 2004-12-10
			"UPPref-000428"/* @res "��Ŀ����" */,// 2004-12-10
			"UPPref-000484"/* @res "�����˲�" */,// 2004-12-10
			"UPPref-000383"/* @res "�������" */,// 2004-12-10
			"UPPref-000485"/* @res "�����˲�" */, "UPPref-000486"/* @res "����" */,
			"UPPref-000487"/* @res "������" */, "UPPref-000488"/* @res "��ɫ" */,
			"UPPref-000005"/* ���ŵ���HR=���ŵ��� */, "UPPref-000510"/* ���Ż�ƿ�Ŀ */,
			"UPPref-000511"/* ��ƿ�Ŀ��汾 */,
			"UPPref-000509"/* �˲����� */,
			"UPPref-000508"/* @res "���㵥λ����" */,// sxj 2004-03-19
			"UPPref-000507"/* �ڲ��˻� */, "UPPref-000512"/* �̶��ʲ������˲� */,
			"UPPref-000513"/* �̶��ʲ��˲���˾ */, "UPPref-000504"/*
															 * @res "��ɫ" 101612
															 */, "UPPref-000515"/* �ֿ⵵���๫˾ */, "UPPref-000516"/*
													 * @res "�����֯�๫˾" 101612
													 */

	       ,"�������","���е���","�ͻ�����","��Ӧ�̷���","Ӱ�����ص�������",
	       "UPPref-000518"/* @res ��֧���������� */,"UPPref-000519"/* @res ��֧���������� */,"������","��˾Ŀ¼1"
			};

	

	public final String[] IMPL_CLASSNAME = {
			"nc.ui.bd.ref.busi.PsnbasdocDefaultRefModel"/* @res "��Ա��������" */,
			"nc.ui.bd.ref.busi.PsndocDefaulRefModel"/* @res "��Ա����" */,
			"nc.ui.bd.ref.busi.DeptdocDefaultRefModel"/* @res "���ŵ���" */,
			"nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel"/*
															 * @res "��˾Ŀ¼(����)S"
															 * ,����
															 */,
			"nc.ui.bd.ref.busi.CorpDefaultRefModel"/* @res "��˾Ŀ¼" */,
			"nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel"/* @res "��˾Ŀ¼(����)" */,
			"nc.ui.bd.ref.busi.CustbasdocDefaulteRefModel"/* @res "���̻�������" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "���̵���" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "�ͻ�����" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "��Ӧ�̵���" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "���̵�����������" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "�ͻ�������������" */,
			"nc.ui.bd.ref.busi.CustmandocDefaultRefModel"/* @res "��Ӧ�̵�����������" */,
			"nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel"/* @res "���̵�����������" */,
			"nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel"/* @res "�ͻ�������������" */,
			"nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel"/* @res "��Ӧ�̵�����������" */,
			"nc.ui.bd.ref.busi.InvbasdocDefaultRefModel"/* @res "�����������" */,
			"nc.ui.bd.ref.busi.InvmandocDefaultRefModel"/* @res "�������" */,
			"nc.ui.bd.ref.busi.AccsubjDefaultRefModel"/* @res "��ƿ�Ŀ" */,
			"nc.ui.bd.ref.busi.PsnclDefaultRefModel"/* @res "��Ա���" */,
			"nc.ui.bd.ref.busi.InvclDefaultRefModel"/* @res "�������" */,
			"nc.ui.bd.ref.busi.VoucherTypeDefaultRefModel"/* @res "ƾ֤���" */,
			"nc.ui.bd.ref.busi.PaytermDefaultRefModel"/* @res "�ո���Э��" */,
			"nc.ui.bd.ref.busi.BalanceTypeDefaultRefModel"/* @res "���㷽ʽ" */,
			"nc.ui.bd.ref.busi.AccBankDefaulteRefModel"/* @res "��������" */,
			"nc.ui.bd.ref.busi.AccBank123RefModel"/* @res "��������1" */,
			"nc.ui.bd.ref.busi.AccBank123RefModel"/* @res "��������2" */,
			"nc.ui.bd.ref.busi.AccBank123RefModel"/* @res "��������3" */,
			"nc.ui.bd.ref.busi.StorDocDefaulteRefModel"/* @res "�ֿ⵵��" */,
			"nc.ui.bd.ref.busi.SendTypeDefaultRefModel"/* @res "���˷�ʽ" */,
			"nc.ui.bd.ref.busi.RdclDefaultRefModel"/* @res "�շ����" */,
			"nc.ui.bd.ref.busi.AreaclDefaultRefModel"/* @res "��������" */,
			"nc.ui.bd.ref.busi.ComAbstrDefaultRefModel"/* @res "����ժҪ" */,
			"nc.ui.bd.ref.busi.DefdefDefaultRefModel"/* @res "�����Զ�����" */,
			"nc.ui.bd.ref.busi.DefdefDefaultRefModel"/*
														 * @res
														 * "�����Զ�����(���������û�������)"
														 */,
			"nc.ui.bd.ref.busi.CostsubjDefaultRefModel"/* @res "��֧��Ŀ" */,
			"nc.ui.bd.ref.busi.MeasdocDefaultRefModel"/* @res "��������" */,
			"nc.ui.bd.ref.busi.TaxDefaultRefModel"/* @res "˰Ŀ˰��" */,
			"nc.ui.bd.ref.busi.JobtypeDefaultRefModel"/* @res "��Ŀ����" */,
			"nc.ui.bd.ref.busi.JobbasfilDefaultRefModel"/* @res "��Ŀ����" */,
			"nc.ui.bd.ref.busi.JobmngfilDefaultRefModel"/* @res "��Ŀ������" */,
			"nc.ui.bd.ref.busi.JobmngfilgridRefModel"/* @res "��Ŀ���������" */,// sxj
			// 2003-07-02
			"nc.ui.bd.ref.busi.CargdocDefaultRefModel"/* @res "��λ����" */,
			"nc.ui.bd.ref.busi.CurrtypeDefaultRefModel"/* @res "��ҵ���" */,
			"nc.ui.bd.ref.busi.CustAddrDefaultRefModel"/* @res "���̷�����ַ" */,
			"nc.ui.bd.ref.busi.DefdocDefaultRefModel"/* @res "�Զ������" */,
			"nc.ui.bd.ref.busi.SetPartDefaultRefModel"/* @res "���׼�" */,
			"nc.ui.bd.ref.busi.AccPeriodDefaultRefModel"/* @res "����ڼ�" */,
			"nc.ui.bd.ref.busi.BusiTypeDefaultRefModel"/* @res "ҵ������" */,
			"nc.ui.bd.ref.busi.CalBodyDefaultRefModel"/* @res "�����֯" */,
			"nc.ui.bd.ref.busi.OperatorDefaultRefModel"/* @res "Ȩ�޲���Ա" */,
			"nc.ui.bd.ref.busi.OperatorDefaultRefModel"/* @res "����Ա" */,
			"nc.ui.bd.ref.busi.ProduceDefaultRefModel"/* @res "���ϵ���" */,
			"nc.ui.bd.ref.busi.PurorgDefaultRefModel"/* @res "�ɹ���֯" */,
			"nc.ui.bd.ref.busi.PubTimeControlRefModel"/* @res "��������" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "��������" */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* @res "�ʻ�" */,
			"nc.ui.bd.ref.busi.CostSubj_ArapDefaultRefModel"/* @res "������Ŀ" */,
			"nc.ui.bd.ref.busi.CostSubj_ArapDefaultRefModel"/* @res "֧����Ŀ" */,
			"nc.ui.bd.ref.busi.CustmandocUnitDefaultRefModel"/* ���̵���(����) */,
			"nc.ui.bd.ref.busi.CorpDefaultRefModel"/* @res "Ȩ�޹�˾Ŀ¼" */,
			"nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel"/* @res "Ȩ�޹�˾Ŀ¼(����)" */,
			"nc.ui.bd.ref.busi.SaleStruDefaultRefModel"/* @res "������֯" */,
			"nc.ui.bd.ref.busi.CashflowDefaultRefModel"/* @res "�ֽ�������Ŀ" */,
			"nc.ui.bd.ref.busi.DefdoclistDefaultRefModel"/* @res "�Զ�������б�" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "Ʊ������" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "Ʊ������1" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "Ʊ������2" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "Ʊ������3" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "Ʊ������4" */,
			"nc.ui.bd.ref.busi.DefaultRefModel_ARAP"/* @res "Ʊ������5" */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* @res "�˻�����" */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* @res "�˻�����1" */,
			"nc.ui.bd.ref.busi.PsndocDefaulRefModel"/* @res "��Ա����HR" */,
			"nc.ui.bd.ref.DefaultRefModel_multiLang"/* @res "����������Դ�б�" */,// sxj
			// 2003-07-30
			"nc.ui.bd.ref.busi.ProdLineDefaultRefModel"/* @res "��Ʒ�ߵ���" */,// sxj
			// 2004-03-15
			"nc.ui.bd.ref.busi.CorpKindDefaultRefModel"/* @res "��˾���" */,// sxj
			// 2004-03-17
			"nc.ui.bd.ref.busi.SettleUnitDefaultRefModel"/* @res "���㵥λ" */,// sxj
			// 2004-03-19
			"nc.ui.bd.ref.busi.AddressDefaultRefModel"/* @res "�ص㵵��" */,// sxj
			// 2004-04-01
			"nc.ui.bd.ref.busi.SettleCenterDefaultRefModel"/* @res "��������" */,// sxj
			// 2004-04-13
			"nc.ui.bd.ref.busi.UserGroupDefaultRefModel"/* @res "�û���" */,// 2004-06-10
			"nc.ui.bd.ref.busi.AccPeriodSchemeDefaultRefModel"/* @res "����ڼ䷽��" */,// 2004-12-10
			"nc.ui.bd.ref.busi.AccsubjSchemeDefaultRefModel"/* @res "��Ŀ����" */,// 2004-12-10
			"nc.ui.bd.ref.busi.GlbookDefaultRefModel"/* @res "�����˲�" */,// 2004-12-10
			"nc.ui.bd.ref.busi.GlOrgDefaulRefModel"/* @res "�������" */,// 2004-12-10
			"nc.ui.bd.ref.busi.GlorgbookDefaultRefModel"/* @res "�����˲�" */,
			""/* @res "����" */, ""/* @res "������" */, ""/* @res "��ɫ" */,
			"nc.ui.bd.ref.busi.DeptdocDefaultRefModel"/* ���ŵ���HR=���ŵ��� */,
			"nc.ui.bd.ref.busi.AccsubjDefaultRefModel"/* ���Ż�ƿ�Ŀ=��ƿ�Ŀ */,
			"nc.ui.bd.ref.busi.AccsubjDefaultRefModel"/* ��ƿ�Ŀ��汾=��ƿ�Ŀ */,
			"nc.ui.bd.ref.busi.GlbookOrgDefaultTreeModel"/* �˲����� */,
			"nc.ui.bd.ref.busi.SettleUnitDefaultGridTreeModel"/* ���㵥λ���� */,
			"nc.ui.bd.ref.busi.AccidDefaultRefModel"/* �ڲ��˻� */,
			"nc.ui.bd.ref.busi.GlbookDefaultRefModel"/* �̶��ʲ������˲� */,
			"nc.ui.bd.ref.busi.GlbookcorpDefaultRefModel"/* �̶��ʲ��˲���˾ */,
			"nc.ui.bd.ref.busi.RoleDefaultModel",/* ��ɫ */
			"nc.ui.bd.ref.busi.StorDocMultiCorpDefaultRefModel",/* �ֿ⵵���๫˾ */
			"nc.ui.bd.ref.busi.CalBodyMultiCorpDefaultRefModel"/* �����֯�๫˾ */,
			"nc.ui.bd.ref.busi.BanktypeDefaultRefModel"/*�������*/,
			"nc.ui.bd.ref.busi.BankDefaultRefModel"/*���е���*/,
			"nc.ui.bd.ref.busi.CustClassDefaultRefModel"/*�ͻ�����*/,
			"nc.ui.bd.ref.busi.SupplierDefaultRefModel" /*��Ӧ�̷���*/,
			"nc.ui.bd.ref.busi.BillTypeDefaultRefModel" /*Ӱ�����ص�������*/,
			"nc.ui.bd.ref.busi.AccBankPayRefModel", /**��֧���������� */
			"nc.ui.bd.ref.busi.AccBankPayRefModel",/**�Ѹ��������� */
			"nc.ui.bd.ref.busi.AccPeriodYearRefTreeModel" /**������*/,
			"nc.ui.bd.ref.busi.CorpDefaultRefModel1"
			};
}