package com.afunms.config.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.afunms.application.dao.ClusterDao;
import com.afunms.application.dao.UpAndDownMachineDao;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.JspPage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.GeneratorKey;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.dao.CheckResultDao;
import com.afunms.config.dao.CompGroupRuleDao;
import com.afunms.config.dao.CompRuleDao;
import com.afunms.config.dao.CompStrategyDao;
import com.afunms.config.dao.DetailCompRuleDao;
import com.afunms.config.dao.StrategyIpDao;
import com.afunms.config.model.CheckResult;
import com.afunms.config.model.CompGroupRule;
import com.afunms.config.model.CompRule;
import com.afunms.config.model.CompStrategy;
import com.afunms.config.model.DetailCompRule;
import com.afunms.config.model.StrategyIp;
import com.afunms.polling.telnet.DevicePolicyEngine;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;
import com.informix.msg.sql1_en_US;

public class NetWorkConfigRuleManager extends BaseManager implements ManagerInterface{

	public String execute(String action) {
		if(action.equals("ruleDetailList")){
			return ruleDetailList();
		}
		if(action.equals("createRule")){
			return createRule();
		}
		if(action.equals("deleteRule")){
			return deleteRule();
		}
		if(action.equals("save")){
			return save();
		}
		if(action.equals("edit")){//�������޸ĵ�����
			return edit();
		}
		if(action.equals("editRule")){//����༭����
			return editRule();
		}
		////////////////������////////////////////
		if(action.equals("groupRuleList")){//�������б����
			return groupRuleList();
		}
		
		if(action.equals("createGroupRule")){//�������������
			return createGroupRule();
		}
		if(action.equals("saveGroupRule")){//����������б����
			return saveGroupRule();
		}
		if(action.equals("deleteGroupRule")){//ɾ���������б����
			return deleteGroupRule();
		}
		if(action.equals("editGroupRule")){//�༭�������б����
			return editGroupRule();
		}
		if(action.equals("updateGroupRule")){//�޸Ĺ������б����
			return updateGroupRule();
		}
		//����
		if(action.equals("strategyList")){//�����б����
			return strategyList();
		}
		
		if(action.equals("createStrategy")){//���������б����
			return createStrategy();
		}
		if(action.equals("saveStrategy")){//��������б����
			return saveStrategy();
		}
		if(action.equals("pureRuleList")){//��ʾ����ĳ�������ϸ����
			return pureRuleList();
		}
		if(action.equals("editStrategy")){//�޸Ĳ���
			return editStrategy();
		}
		if(action.equals("updateStrategy")){//�����޸Ĳ���
			return updateStrategy();
		}
		if(action.equals("deleteStrategy")){//ɾ������
			return deleteStrategy();
		}
		
		if(action.equals("ready_addip")){//��ʾ����ip����
			return ready_addip();
		}
		if(action.equals("showDetailStrategy")){//����ip
			return showDetailStrategy();
		}
		if(action.equals("showAllDevice")){//��ʾ�����豸
			return showAllDevice();
		}
		if(action.equals("saveip")){//����ip
			return saveip();
		}
		if(action.equals("exeRule")){//���й���
			return exeRule();
		}
		if(action.equals("viewDetail")){//���й���
			return viewDetail();
		}
		
		return null;
	}
public String  ruleDetailList() {
	CompRuleDao dao=new CompRuleDao();
	List list=dao.loadAll();
	request.setAttribute("list", list);
	return "/config/vpntelnet/compliance/ruleDetaillist.jsp";
}
public String  createRule() {
	
	return "/config/vpntelnet/compliance/createRule.jsp";
}
public String  editRule() {
	CompRule rule=new CompRule();
	List detailRuleList=new ArrayList();
	String id=getParaValue("id");
	CompRuleDao dao=new CompRuleDao();
	DetailCompRuleDao detailDao=new DetailCompRuleDao();
	try {
		rule=(CompRule) dao.findByID(id);
		 detailRuleList=detailDao.findByCondition(" where RULEID="+id);
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		dao.close();
	}
	request.setAttribute("vo", rule);
	request.setAttribute("detailRuleList", detailRuleList);
	return "/config/vpntelnet/compliance/editRule.jsp";
}

public String  deleteRule() {
	String[] ids = getParaArrayValue("checkbox");
	if (ids != null && ids.length > 0) {
		CompRuleDao dao = new CompRuleDao();
		DetailCompRuleDao detailCompRuleDao=new DetailCompRuleDao();
		try {
			dao.delete(ids);
			detailCompRuleDao.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
			detailCompRuleDao.close();
		}

	 }
	return ruleDetailList();
}
public String  save() {
	
	CompRule rule=loadRule();
	CompRuleDao dao=new CompRuleDao();
	boolean isSucess=dao.save(rule);
	//������ϸ�Ĺ���
	if (isSucess) {
		saveDetailRule(rule.getSelect_type());
	}
	return null;
}
//�޸Ĳ��������
public String  edit() {
	boolean isSucess=false;
	CompRule vo=loadRule();
	int id=getParaIntValue("id");
	vo.setId(id);
	CompRuleDao dao=new CompRuleDao();
	isSucess=dao.update(vo);
	
	DetailCompRuleDao detailDao=new DetailCompRuleDao();
	 
	if (isSucess) {
		
	isSucess=detailDao.deleteDetailRule(vo);
	saveDetailRule(vo.getSelect_type());
	}
	return null;
}
public void saveDetailRule(int standard){

	int key=GeneratorKey.getInstance().getKey();
	
	DetailCompRuleDao detailDao=new DetailCompRuleDao();
	String express="";
	
	//int standard=rule.getSelect_type();//�����򵥣�0�����߼���1�����Զ��壨2��
	int relation=-1;//-1ʾ��Ϊ��
	int isContain=0;
	
	String beginBlock="";
	String endBlock="";
	String extraBlock="";
	int isExtraContain=-1;//��ʾ��
	DetailCompRule vo=new DetailCompRule();
	if(standard==0){
		
		express=getParaValue("content");
		//express=express.replaceAll("\r\n", ";;;;");
		isContain=getParaIntValue("simple_config");
	}else if (standard==1) {
		express=getParaValue("advance_value");
		isContain=getParaIntValue("advance_config");
		
	}else if (standard==2) {
		express=getParaValue("custom_value");
		isContain=getParaIntValue("custom_config");
	}

	
	if (standard==2) {
		 beginBlock=getParaValue("begin");
		 endBlock=getParaValue("end");
		 isExtraContain=getParaIntValue("isExtraContain");
		 extraBlock=getParaValue("extra");
		vo.setBeginBlock(beginBlock);
		vo.setEndBlock(endBlock);
		
		vo.setExtraBlock(extraBlock);
	}
	vo.setRuleId(key);
	vo.setRelation(relation);
	vo.setIsContain(isContain);
	vo.setExpression(express);
	vo.setIsExtraContain(isExtraContain);
	detailDao.save(vo);
	//////////////////////////////////////
	
	
	if (standard==1||standard==2) {
	DBManager db=new DBManager();
	String[] selVals=null;
	String[] textVals=null;
	String selVal=getParaValue("selVal");
	String textVal=getParaValue("textVal");
	System.out.println("########################"+selVal);
	System.out.println("########################"+textVal);
	if(selVal!=null&&textVal!=null){
	 selVals=new String[selVal.split(",").length];
	 textVals=new String[textVal.split(",").length];
		selVals=selVal.split(",");
		textVals=textVal.split(",");
		if(selVals.length==textVals.length*2){
			
		try {
		for (int i = 0; i < selVals.length; i++) {
			
			if(i%2==1){
				vo.setIsContain(Integer.parseInt(selVals[i]));
				vo.setExpression(textVals[i/2]);
				String sql=getSql(vo);
				db.addBatch(sql);
			}else {
				vo=new DetailCompRule();
				if (standard==2) {
					vo.setBeginBlock(beginBlock);
					vo.setEndBlock(endBlock);
					vo.setIsExtraContain(isExtraContain);
					vo.setExtraBlock(extraBlock);
				}
				vo.setRuleId(key);
				vo.setRelation(Integer.parseInt(selVals[i]));
			}
		
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.executeBatch();
		}
		
		}	
	}

	
	}
//return true;
}
public String getSql(DetailCompRule rule){
	StringBuffer sql=new StringBuffer();
	sql.append("insert into nms_comp_detail_rule(RULEID,RELATION,ISCONTAIN,EXPRESSION,BEGINBLOCK,ENDBLOCK,ISEXTRACONTAIN,EXTRABLOCK)values(");
	sql.append(rule.getRuleId());
	sql.append(",");
	sql.append(rule.getRelation());
	sql.append(",");
	sql.append(rule.getIsContain());
	sql.append(",'");
	sql.append(rule.getExpression());
	sql.append("','");
	sql.append(rule.getBeginBlock());
	sql.append("','");
	sql.append(rule.getEndBlock());
	sql.append("',");
	sql.append(rule.getIsExtraContain());
	sql.append(",'");
	sql.append(rule.getExtraBlock());
	sql.append("')");
	return sql.toString();
}

public CompRule loadRule(){

	
	String rule_name=getParaValue("rule_name");
	String des=getParaValue("des");
	int standard=getParaIntValue(("standard"));
	
	
	int level=getParaIntValue("level");
	String add_des=getParaValue("add_des");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	CompRule rule=new CompRule();
	rule.setComprule_name(rule_name);
	rule.setDescription(des);
	rule.setSelect_type(standard);
	//rule.setRule_content(content);
	rule.setCreate_time(sdf.format(date));
	rule.setLast_modified_time(sdf.format(date));
	rule.setCreated_by(user.getName());
	rule.setLast_modified_by(user.getName());//���޸�ʱ����
	rule.setViolation_severity(level);
	rule.setRemediation_descr(add_des);
	return rule;
}
///////////////////////������//////////////////////////////
public String  groupRuleList() {
	CompGroupRuleDao dao=new CompGroupRuleDao();
	List list=dao.loadAll();
	request.setAttribute("list", list);
	return "/config/vpntelnet/compliance/groupRuleList.jsp";
}
//����������
public String createGroupRule(){
	CompRuleDao dao=new CompRuleDao();
	List list=dao.loadAll();
	request.setAttribute("list", list);
	return "/config/vpntelnet/compliance/createGroupRule.jsp";
}
//���湤����
public String saveGroupRule(){
	String name=getParaValue("name");
	String desciption=getParaValue("description");
	String[] id=getParaArrayValue("checkbox");
	String ids="";
	if (ids!=null) {
		for (int i = 0; i < id.length; i++) {
			ids=ids+id[i]+",";
		}
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	CompGroupRule vo=new CompGroupRule();
	vo.setName(name);
	
	vo.setDescription(desciption);
	vo.setRuleId(ids);
	vo.setCreatedBy(user.getName());
	vo.setCreatedTime(sdf.format(date));
	vo.setLastModifiedBy(user.getName());
	vo.setLastModifiedTime(sdf.format(date));
	CompGroupRuleDao dao=new CompGroupRuleDao();
	boolean isSucess=dao.save(vo);
	return null;
}
public String deleteGroupRule(){
	String[] ids = getParaArrayValue("checkbox");
	if (ids != null && ids.length > 0) {
		CompGroupRuleDao dao = new CompGroupRuleDao();
		try {
			dao.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

	 }
return groupRuleList();	
}
//�༭��������Ϣ
public String editGroupRule(){
	String id=getParaValue("id");
	CompRuleDao dao=new CompRuleDao();
	List list=dao.loadAll();
	CompGroupRuleDao groupRuleDao=new CompGroupRuleDao();
	
	CompGroupRule rule=(CompGroupRule) groupRuleDao.findByID(id);
	groupRuleDao.close();
	request.setAttribute("list", list);
	request.setAttribute("compGroupRule", rule);
	return "/config/vpntelnet/compliance/editGroupRule.jsp";
}
//�޸Ĺ�����
public String updateGroupRule(){
    int id=getParaIntValue("id");
	String name=getParaValue("name");
	String desciption=getParaValue("description");
	String[] temp=getParaArrayValue("checkbox");
	
	StringBuffer ids=new StringBuffer();
	if (temp!=null) {
		for (int i = 0; i < temp.length; i++) {
			ids.append(temp[i]+",");
		}
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	CompGroupRule vo=new CompGroupRule();
	vo.setId(id);
	vo.setName(name);
	
	vo.setDescription(desciption);
	vo.setRuleId(ids.toString());
	//vo.setCreatedBy(user.getName());
	//vo.setCreatedTime(sdf.format(date));
	vo.setLastModifiedBy(user.getName());
	vo.setLastModifiedTime(sdf.format(date));
	CompGroupRuleDao dao=new CompGroupRuleDao();
	boolean isSucess=dao.update(vo);
	return null;
	
}
//////////////////////////����////////////////////////////
public String strategyList(){
	CompStrategyDao dao=new CompStrategyDao();
	List list=dao.loadAll();
	
	request.setAttribute("list", list);
	
	return "/config/vpntelnet/compliance/strategyList.jsp";
}
public String createStrategy(){
	CompGroupRuleDao dao=new CompGroupRuleDao();
	List list=dao.loadAll();
	request.setAttribute("list", list);
	return "/config/vpntelnet/compliance/createStrategy.jsp";
}

//��ʾ����ĳ�������ϸ����
public String pureRuleList(){
	String id=getParaValue("id");
	CompGroupRuleDao groupRuleDao=new CompGroupRuleDao();
	CompGroupRule rule=(CompGroupRule) groupRuleDao.findByID(id);
	groupRuleDao.close();
	CompRuleDao dao=new CompRuleDao();
	String temp=rule.getRuleId();
	String ids=temp.substring(0, temp.lastIndexOf(","));
	List list=dao.findByCondition(" where ID in("+ids+")");
	
	request.setAttribute("list", list);
	return "/config/vpntelnet/compliance/pureRuleList.jsp";
}
//�������
public String saveStrategy(){
	String name=getParaValue("name");
	String description=getParaValue("description");
	int type=getParaIntValue("type");
	int violateType=getParaIntValue("violateType");
	String[] temp=getParaArrayValue("checkbox");
	StringBuffer ids=new StringBuffer();
	if (temp!=null) {
		for (int i = 0; i < temp.length; i++) {
			ids.append(temp[i]+",");
		}	
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	CompStrategy vo=new CompStrategy();
	vo.setName(name);
	vo.setDescription(description);
	vo.setType(type);
	vo.setViolateType(violateType);
	vo.setGroupId(ids.toString());
	vo.setCreateBy(user.getName());
	vo.setCreateTime(sdf.format(date));
	vo.setLastModifiedBy(user.getName());
	vo.setLastModifiedTime(sdf.format(date));
	CompStrategyDao dao=new CompStrategyDao();
	boolean isSuccess=dao.save(vo);
	
	return null;
}
//�޸Ĳ���
public String editStrategy(){
	String id=getParaValue("id");
	CompGroupRuleDao dao=new CompGroupRuleDao();
	List list=dao.loadAll();
	 CompStrategyDao strategyDao=new CompStrategyDao();
	CompStrategy vo=(CompStrategy) strategyDao.findByID(id);
	request.setAttribute("list", list);
	request.setAttribute("vo", vo);
	return "/config/vpntelnet/compliance/editStrategy.jsp";	
}
//�����޸Ĳ���
public String updateStrategy(){
	int id=getParaIntValue("id");
	String name=getParaValue("name");
	String description=getParaValue("description");
	int type=getParaIntValue("type");
	int violateType=getParaIntValue("violateType");
	String[] temp=getParaArrayValue("checkbox");
	StringBuffer ids=new StringBuffer();
	if (temp!=null) {
		for (int i = 0; i < temp.length; i++) {
			ids.append(temp[i]+",");
		}	
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	CompStrategy vo=new CompStrategy();
	vo.setId(id);
	vo.setName(name);
	vo.setDescription(description);
	vo.setType(type);
	vo.setViolateType(violateType);
	vo.setGroupId(ids.toString());
	vo.setLastModifiedBy(user.getName());
	vo.setLastModifiedTime(sdf.format(date));
	CompStrategyDao dao=new CompStrategyDao();
	boolean isSuccess=dao.update(vo);	
	return null;
}
//ɾ������
public String deleteStrategy(){
	String[] ids=getParaArrayValue("checkbox");
	if (ids != null && ids.length > 0) {
		CompStrategyDao dao = new CompStrategyDao();
		try {
			dao.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

	 }
	return strategyList();
}

public String ready_addip() {
	String id=getParaValue("id");
	HostNodeDao dao=new HostNodeDao();
	List iplist=dao.loadNetwork(1);
	int listsize=iplist.size();
	StrategyIpDao ipDao=new StrategyIpDao();
	List list=ipDao.findByCondition(" where STRATEGY_ID="+id);
	request.setAttribute("iplist",iplist);
	request.setAttribute("strategyList",list);
	request.setAttribute("id",id);
	HostNodeDao listdao=new HostNodeDao();
	setTarget("/config/vpntelnet/compliance/showip.jsp");
	String page=list(listdao);
	JspPage jp = (JspPage)request.getAttribute("page");
	jp.setTotalRecord(listsize);
	request.setAttribute("page", jp);
	return page;
}
public String showDetailStrategy() {
	String id=getParaValue("id");
	CheckResultDao dao=new CheckResultDao();
	List<CheckResult> list=dao.getResultById(id);
	request.setAttribute("list", list);
	request.setAttribute("id", id);
	StrategyIpDao ipDao=new StrategyIpDao();
	List ipList=ipDao.findByCondition(" where STRATEGY_ID="+id);
	
	request.setAttribute("ipList", ipList);
	return "/config/vpntelnet/compliance/detailStrategyList.jsp";
}
public String showAllDevice() {
	CheckResultDao dao=new CheckResultDao();
	List<CheckResult> list=dao.getAllResult();
	request.setAttribute("list", list);
	StrategyIpDao ipDao=new StrategyIpDao();
	List deviceList=ipDao.findByCondition(" where AVAILABILITY=0");
	request.setAttribute("deviceList", deviceList);
	return "/config/vpntelnet/compliance/showAllDevice.jsp";
}
public String saveip() {
	String[] ips=getParaArrayValue("checkbox");
	int id=getParaIntValue("id");
	List list=null;
	StrategyIpDao dao=new StrategyIpDao();
	  dao.saveBatch(ips,id);
	request.setAttribute("list", list);
	return null;
}
public String exeRule(){
	String id=getParaValue("id");
	DevicePolicyEngine engine=new DevicePolicyEngine();
	engine.executePolicey(id);
	CheckResultDao dao=new CheckResultDao();
	List<CheckResult> list=dao.getResultById(id);
	StrategyIpDao ipDao=new StrategyIpDao();
	List ipList=ipDao.findByCondition(" where STRATEGY_ID="+id);
	
	request.setAttribute("list", list);
	request.setAttribute("ipList", ipList);
	request.setAttribute("id", id);
	return "/config/vpntelnet/compliance/detailStrategyList.jsp";
}
public String viewDetail(){
	String id=getParaValue("id");
	String ip=getParaValue("ip");
	
	CheckResultDao dao=new CheckResultDao();
	List list=dao.getReslutByIdAndIp(id,ip);
	request.setAttribute("list", list);
	request.setAttribute("id", id);
	return "/config/vpntelnet/compliance/viewDetail.jsp";
}
}
