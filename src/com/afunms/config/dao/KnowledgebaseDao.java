package com.afunms.config.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Knowledgebase;

public class KnowledgebaseDao extends BaseDao implements DaoInterface {

	public KnowledgebaseDao() {
		super("system_knowledgebase");
	}

	public BaseVo loadFromRS(ResultSet rs) {
		Knowledgebase vo = new Knowledgebase();

		try {
			vo.setId(rs.getInt("id"));
			vo.setCategory(rs.getString("category"));
			vo.setEntity(rs.getString("entity"));
			vo.setSubentity(rs.getString("subentity"));
			vo.setTitles(rs.getString("titles"));
			vo.setContents(rs.getString("contents"));
			vo.setBak(rs.getString("bak"));
			vo.setAttachfiles(rs.getString("attachfiles"));
			vo.setUserid(rs.getString("userid"));
			vo.setKtime(rs.getString("ktime"));
		} catch (Exception e) {
			SysLogger.error("Error in KnowledgebaseDao.loadFromRS()", e);
			vo = null;
		}
		return vo;
	}

	/**
	*����������ѯ
	*/
	public List find(String con1,String con2,String con3){
			if(con1.equals("ȫ��")&&con2.equals("ȫ��")&&con3.equals("ȫ��")){
				return findByCriteria("select * from system_knowledgebase");
			}
			else if(con1.equals("ȫ��")&&con2.equals("ȫ��")&&!con3.equals("ȫ��")){
				return findByCriteria("select * from system_knowledgebase where subentity='"+con3+"';");
			}
			else if(con1.equals("ȫ��")&&!con2.equals("ȫ��")&&con3.equals("ȫ��")){
				return findByCriteria("select * from system_knowledgebase where entity='"+con2+"';");
			}
			else if(con1.equals("ȫ��")&&!con2.equals("ȫ��")&&!con3.equals("ȫ��")){
				return findByCriteria("select * from system_knowledgebase where entity='"+con2+"' and subentity='"+con3+"';");
			}
			else if(!con1.equals("ȫ��")&&con2.equals("ȫ��")&&con3.equals("ȫ��")){
				return findByCriteria("select * from system_knowledgebase where category='"+con1+"';");
			}
			else if(!con1.equals("ȫ��")&&con2.equals("ȫ��")&&!con3.equals("ȫ��")){
				return findByCriteria("select * from system_knowledgebase where category='"+con1+"' and subentity='"+con3+"';");
			}
			else if(!con1.equals("ȫ��")&&!con2.equals("ȫ��")&&con3.equals("ȫ��")){
				return findByCriteria("select * from system_knowledgebase where category='"+con1+"' and entity='"+con2+"';");
			}
			else{
				return findByCriteria("select * from system_knowledgebase where category='"+con1+"' and entity='"+con2+"' and subentity='"+con3+"';");
			}
	}
	/**
	*
	*/
	public List findforevent(String eventid) {
		String event_category = null;
		String event_entity = null;
		String event_subentity = null;
		List filelist = null;
		int nodeid = 0;
		int ostype =0;
		try {
			rs = conn.executeQuery("select * from system_eventlist where id='"
					+ eventid + "';");
			while (rs.next()) {
				event_category = rs.getString("subtype");
				event_subentity = rs.getString("subentity");
				nodeid = rs.getInt("nodeid");
			}
			rs = conn.executeQuery("select * from topo_host_node where id='"
						+ nodeid + "';");
			while (rs.next()) {
				ostype = rs.getInt("ostype");
			}
			rs=conn.executeQuery("select * from system_ostype where ostypeid='"+ostype+"';");
			while(rs.next()){
				event_entity=rs.getString("ostypename");
			}
		} catch (Exception e) {
			conn.close();
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select * from system_knowledgebase where category='");
		sql.append(event_category);
		sql.append("' and entity='");
		sql.append(event_entity);
		sql.append("' and subentity='");
		sql.append(event_subentity);
		sql.append("';");
		return findByCriteria(sql.toString());
	}

	
	/**
	*�ؼ��ֲ�ѯ
	*/
	public List findByKey(String key){
		return findByCriteria("select * from system_knowledgebase k where k.contents like '%"+key+"%' or k.attachfiles like '%"+key+"%' or k.titles like '%"+key+"%';");
	}
	
	public List loadAll() {
		List list = new ArrayList(8);
		try {
			rs = conn.executeQuery("select * from system_knowledgebase order by id");
			while (rs.next()) {
				list.add(loadFromRS(rs));
			}
		} catch (Exception e) {
			SysLogger.error("KnowledgebaseDao:loadAll()", e);
			list = null;
		} finally {
			conn.close();
		}
		return list;
	}
	
	/**
	*����
	*/
	public boolean save(BaseVo baseVo) {
		Knowledgebase vo=(Knowledgebase)baseVo;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into system_knowledgebase(category,entity,subentity,titles,contents,bak,attachfiles,userid)values(");
		sql.append("'");
		sql.append(vo.getCategory());
		sql.append("','");
		sql.append(vo.getEntity());
		sql.append("','");
		sql.append(vo.getSubentity());
		sql.append("','");
		sql.append(vo.getTitles());
		sql.append("','");
		sql.append(vo.getContents());
		sql.append("','");
		sql.append(vo.getBak());
		sql.append("','");
		sql.append(vo.getAttachfiles());
		sql.append("','");
		sql.append(vo.getUserid());
		sql.append("');");
		return saveOrUpdate(sql.toString());
	}
	/**
	*�޸�
	*/
	public boolean update(BaseVo baseVo) {
		Knowledgebase vo = (Knowledgebase) baseVo;
		StringBuffer sql = new StringBuffer();
		sql.append("update system_knowledgebase set category='");
		sql.append(vo.getCategory());
		sql.append("',entity='");
		sql.append(vo.getEntity());
		sql.append("',subentity='");
		sql.append(vo.getSubentity());
		sql.append("',titles='");
		sql.append(vo.getTitles());
		sql.append("',contents='");
		sql.append(vo.getContents());
		sql.append("',bak='");
		sql.append(vo.getBak());
		sql.append("',attachfiles='");
		sql.append(vo.getAttachfiles());
		sql.append("' where id=");
		sql.append(vo.getId());
		return saveOrUpdate(sql.toString());
	}

	public String selectcontent(){
		ResultSet rs1,rs2,rs3;
		List selectlist1=new ArrayList();
		List selectlist2=new ArrayList();
		List selectlist3=new ArrayList();
		String subtype=null;
		String type=null;
		String name=null;
		String res=null;
		StringBuffer myselect=new StringBuffer();
		myselect.append("ȫ��$ȫ��,ȫ��"+"#");
		rs1=conn.executeQuery("select type from nms_alarm_indicators group by type;");
		try {
			while (rs1.next()) {
				selectlist1.add(rs1.getString(1));
			}
			for(int i=0;i<selectlist1.size();i++){
				type=(String)selectlist1.get(i);
				myselect.append(type+"$"+"ȫ��,ȫ��|");
				rs2=conn.executeQuery("select subtype from nms_alarm_indicators where type='"+type+"' group by subtype");
				while(rs2.next()){
					selectlist2.add(rs2.getString(1));
				}
				for(int j=0;j<selectlist2.size();j++){
					subtype=(String) selectlist2.get(j);
					myselect.append(subtype+","+"ȫ��,");
				rs3=conn.executeQuery("select name from nms_alarm_indicators where subtype='"+subtype+"' group by name");
				while(rs3.next()){
					selectlist3.add(rs3.getString(1));
				}
				for(int n=0;n<selectlist3.size();n++){
					name=(String) selectlist3.get(n);
					myselect.append(name+",");
				}
				myselect.append("|");
				selectlist3.clear();
				}
				myselect.append("#");
				selectlist2.clear();
			}
			String n=myselect.toString();
			String m=n.replace(",|#", "#");
			String p=m.replace(",|", "|");
			int leng=p.length()-1;
			res=p.substring(0, leng);
		} catch (Exception e) {
		}
		return res;
	}
	public String selectcontent2(){
		ResultSet rs1,rs2,rs3;
		List selectlist1=new ArrayList();
		List selectlist2=new ArrayList();
		List selectlist3=new ArrayList();
		String subtype=null;
		String type=null;
		String name=null;
		String res=null;
		StringBuffer myselect=new StringBuffer();
		myselect.append("ȫ��$ȫ��,ȫ��"+"#");
		rs1=conn.executeQuery("select type from nms_gather_indicators group by type;");
		try {
			while (rs1.next()) {
				selectlist1.add(rs1.getString(1));
			}
			for(int i=0;i<selectlist1.size();i++){
				type=(String)selectlist1.get(i);
				myselect.append(type+"$"+"ȫ��,ȫ��|");
				rs2=conn.executeQuery("select subtype from nms_gather_indicators where type='"+type+"' group by subtype");
				while(rs2.next()){
					selectlist2.add(rs2.getString(1));
				}
				for(int j=0;j<selectlist2.size();j++){
					subtype=(String) selectlist2.get(j);
					myselect.append(subtype+","+"ȫ��,");
					rs3=conn.executeQuery("select name from nms_gather_indicators where subtype='"+subtype+"' group by name");
					while(rs3.next()){
						selectlist3.add(rs3.getString(1));
					}
					for(int n=0;n<selectlist3.size();n++){
						name=(String) selectlist3.get(n);
						myselect.append(name+",");
					}
					myselect.append("|");
					selectlist3.clear();
				}
				myselect.append("#");
				selectlist2.clear();
			}
			String n=myselect.toString();
			String m=n.replace(",|#", "#");
			String p=m.replace(",|", "|");
			int leng=p.length()-1;
			res=p.substring(0, leng);
		} catch (Exception e) {
		}
		return res;
	}
	
	/**
	*ɾ��
	*/
	public boolean delete(String id) {
		boolean result = false;

		try {
			conn.addBatch("delete from system_knowledgebase where id=" + id);
			conn.executeBatch();
			result = true;
		} catch (Exception e) {
			SysLogger.error("KnowledgebaseDao.delete()", e);
		} finally {
			conn.close();
		}
		return result;
	}
	
}
