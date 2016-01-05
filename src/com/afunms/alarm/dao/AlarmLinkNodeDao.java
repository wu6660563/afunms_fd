package com.afunms.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.alarm.model.AlarmLinkNode;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.DBManager;

/**
 * 告警联动分析功能数据持久层
 * 
 * 
 * @author yag
 *
 */
public class AlarmLinkNodeDao extends BaseDao implements DaoInterface{

	public AlarmLinkNodeDao(){
		super("nms_alarm_link_node");
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		AlarmLinkNode node = new AlarmLinkNode();
		try{
				node.setId(rs.getInt("id"));
				node.setFid(rs.getInt("fid"));
				node.setLinkid(rs.getInt("linkid"));
				node.setNodeid(rs.getInt("nodeid"));
				node.setAlarmid(rs.getInt("alarmid"));
				node.setName(rs.getString("name"));
				node.setAlarmDescr(rs.getString("alarm_descr"));
				node.setIsCover(rs.getInt("is_cover"));
				node.setIsEnabled(rs.getInt("is_enabled"));
				
				//node.setIsCoverNext(rs.getInt("is_cover_next"));
				//node.setNumber(rs.getInt("number"));
				node.setCompareType(rs.getInt("compare_type"));
				node.setSms0(rs.getInt("sms0"));
				node.setSms1(rs.getInt("sms1"));
				node.setSms2(rs.getInt("sms2"));
				node.setLimenvalue0(rs.getString("limenvalue0"));
				node.setLimenvalue1(rs.getString("limenvalue1"));
				node.setLimenvalue2(rs.getString("limenvalue2"));
				node.setTime0(rs.getInt("time0"));
				node.setTime1(rs.getInt("time1"));
				node.setTime2(rs.getInt("time2"));
				node.setWay0(rs.getString("way0"));
				node.setWay1(rs.getString("way1"));
				node.setWay2(rs.getString("way2"));
				node.setDescr(rs.getString("descr"));

		} catch(Exception e) {
			e.printStackTrace();
		}
		return node;
	}

	public boolean save(BaseVo vo) {
		AlarmLinkNode node = (AlarmLinkNode)vo;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into nms_alarm_link_node (fid,linkid,nodeid,alarmid,name,alarm_descr,is_cover," 
				+ " is_enabled,compare_type,sms0,sms1,sms2,"
				+ " limenvalue0,limenvalue1,limenvalue2,time0,time1,time2,way0,way1,way2,descr)"
				+ " values(");
		sql.append(node.getFid());
		sql.append("," + node.getLinkid());
		sql.append("," + node.getNodeid());
		sql.append("," + node.getAlarmid() + ",");
		sql.append(",'" + node.getName() );
		sql.append("'," + node.getIsCover() + ",");
		sql.append(node.getIsEnabled() + ",");
		sql.append(node.getCompareType() + ",");
		sql.append(node.getSms0() + ",");
		sql.append(node.getSms1() + ",");
		sql.append(node.getSms2() + ",");
		sql.append("'" + node.getLimenvalue0() + "',");
		sql.append("'" + node.getLimenvalue1() + "',");
		sql.append("'" + node.getLimenvalue2() + "',");
		sql.append(node.getTime0() + ",");
		sql.append(node.getTime1() + ",");
		sql.append(node.getTime2() + ",'");
		sql.append(node.getWay0() + "','");
		sql.append(node.getWay1() + "','");
		sql.append(node.getWay2() + "',");
		sql.append("'" + node.getDescr() + "'");
		sql.append(")");
		return super.saveOrUpdate(sql.toString());
	}
	
	
	/**
	 * 更新部分信息
	 * updatePartInfo:
	 * <p>
	 *
	 * @param vo
	 * @return
	 *
	 * @since   v1.01
	 */
	public boolean updatePartInfo(BaseVo vo) {
		AlarmLinkNode node = (AlarmLinkNode)vo;
		StringBuffer sql = new StringBuffer();
		sql.append("update nms_alarm_link_node set "); 
		sql.append(" is_cover=" + node.getIsCover() + ",");
		sql.append(" is_enabled=" + node.getIsEnabled() + ",");
		
		sql.append(" sms0=" + node.getSms0() +",");
		sql.append(" sms1=" + node.getSms1() +",");
		sql.append(" sms2=" + node.getSms2() +",");
		sql.append(" limenvalue0='" + node.getLimenvalue0() + "',");
		sql.append(" limenvalue1='" + node.getLimenvalue1() + "',");
		sql.append(" limenvalue2='" + node.getLimenvalue2() + "',");
		sql.append(" time0=" + node.getTime0() + ",");
		sql.append(" time1=" + node.getTime1() + ",");
		sql.append(" time2=" + node.getTime2() );
		sql.append(" where id=" + node.getId());
		return super.saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		AlarmLinkNode node = (AlarmLinkNode)vo;
		StringBuffer sql = new StringBuffer();
		sql.append("update nms_alarm_link_node set fid="); 
		sql.append(node.getFid());
		sql.append(",linkid=" + node.getLinkid());
		sql.append(",nodeid=" + node.getNodeid());
		sql.append(",alarmid=" + node.getAlarmid());
		sql.append(",name='" + node.getName() + "'");
		sql.append(",alarm_descr='" + node.getAlarmDescr() + "',");
		sql.append(" is_cover=" + node.getIsCover() + ",");
		sql.append(" is_enabled=" + node.getIsEnabled() + ",");
		sql.append(" compare_type=" + node.getCompareType() + ",");
		sql.append(" sms0=" + node.getSms0() +",");
		sql.append(" sms1=" + node.getSms1() +",");
		sql.append(" sms2=" + node.getSms2() +",");
		sql.append(" limenvalue0='" + node.getLimenvalue0() + "',");
		sql.append(" limenvalue1='" + node.getLimenvalue1() + "',");
		sql.append(" limenvalue2='" + node.getLimenvalue2() + "',");
		sql.append(" time0=" + node.getTime0() + ",");
		sql.append(" time1=" + node.getTime1() + ",");
		sql.append(" time2=" + node.getTime2() + ",");
		sql.append(" way0='" + node.getWay0() + "',");
		sql.append(" way1='" + node.getWay1() + "',");
		sql.append(" way2='" + node.getWay2() + "',");
		sql.append(" descr='" + node.getDescr() + "'");
		sql.append(" where id=" + node.getId());
		return super.saveOrUpdate(sql.toString());
	}

	public boolean delete(String[] id) {
		return super.delete(id);
	}
	
	public boolean saveList(List<AlarmLinkNode> list) {
		if(list.isEmpty()){
			return false;
		}
		try{
			StringBuffer sql = new StringBuffer();
			AlarmLinkNode tmpNode = new AlarmLinkNode();
			for(int i=0; i<list.size(); i++){
				tmpNode = list.get(i);
				sql.append("insert into nms_alarm_link_node(fid,linkid,nodeid," +
						"alarmid,name,alarm_descr,is_cover,is_enabled,compare_type," +
						"sms0,sms1,sms2,limenvalue0,limenvalue1,limenvalue2,time0,time1," +
						"time2,way0,way1,way2,descr) values(");
				sql.append(tmpNode.getFid());
				sql.append("," + tmpNode.getLinkid());
				sql.append("," + tmpNode.getNodeid());
				sql.append("," + tmpNode.getAlarmid());
				sql.append(",'" + tmpNode.getName()+"'");
				sql.append(",'" + tmpNode.getAlarmDescr()+"'");
				sql.append("," + tmpNode.getIsCover()+"");
				sql.append("," + tmpNode.getIsEnabled()+"");
				sql.append("," + tmpNode.getCompareType()+"");
				sql.append("," + tmpNode.getSms0()+"");
				sql.append("," + tmpNode.getSms1()+"");
				sql.append("," + tmpNode.getSms2()+"");
				sql.append(",'" + tmpNode.getLimenvalue0()+"'");
				sql.append(",'" + tmpNode.getLimenvalue1()+"'");
				sql.append(",'" + tmpNode.getLimenvalue2()+"'");
				sql.append("," + tmpNode.getTime0()+"");
				sql.append("," + tmpNode.getTime1()+"");
				sql.append("," + tmpNode.getTime2()+"");
				sql.append(",'" + tmpNode.getWay0()+"'");
				sql.append(",'" + tmpNode.getWay1()+"'");
				sql.append(",'" + tmpNode.getWay2()+"'");
				sql.append(",'" + tmpNode.getDescr()+"')");
				conn.addBatch(sql.toString());
				sql.delete(0, sql.length());
			}
			conn.executeBatch();
		} catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			conn.close();
		}
		return false;
	}
	
	
	/**
	 * 根据原来的告警id进行查询
	 * 
	 * @param id
	 * 			alarmid
	 * @return
	 * 			List<AlarmLinkNode>
	 */
	public List<AlarmLinkNode> queryByAlarmID(int id) {
		List<AlarmLinkNode> list = new ArrayList<AlarmLinkNode>();
		if (id < 0) {
			return list;
		}
		String condition = " where alarmid=" + id;
		return super.findByCondition(condition);
	}
	
	/**
	 * 根据原来的告警id进行查询
	 * 
	 * @param id
	 * 			alarmid
	 * @return
	 * 			List<AlarmLinkNode>
	 */
	public List<AlarmLinkNode> queryByLinkID(int id) {
		List<AlarmLinkNode> list = new ArrayList<AlarmLinkNode>();
		if (id < 0) {
			return list;
		}
		String condition = " where linkid=" + id;
		return super.findByCondition(condition);
	}
	
	public List<Integer> queryLinkIdByAlarmID(int id) throws SQLException {

		List<Integer> linkIdList = new ArrayList<Integer>();
		DBManager db = new DBManager();
		String sql = "select distinct(linkid) as linkid from "
				+ " nms_alarm_link_node where alarmid=" + id;
		ResultSet rs = null;
		try {
			rs = db.executeQuery(sql);
			while(rs.next()){
				linkIdList.add(rs.getInt("linkid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} finally {
				db.close();
			}
		}
		
		return linkIdList;
	}
	
	/**
	 * 按照告警类型进行查询节点
	 * 
	 * @param type
	 *            类型
	 * @param subtype
	 *            子类型
	 * @return
	 *         
	 */
	public List<AlarmLinkNode> getAlarmLinkNodeByType(String type, String subtype) {
		boolean flag = false;
		List<AlarmLinkNode> list = new ArrayList<AlarmLinkNode>();
		StringBuffer sb  = new StringBuffer();
		
		if(!"-1".equalsIgnoreCase(type.trim())) {
			sb.append(" where alarmid in (select id from nms_alarm_indicators_node");
			
			if(!"-1".equalsIgnoreCase(subtype.trim())) {
			sb.append(" where type='" + type +" and subtype='" + subtype +"')");	
			} else {
				sb.append(" where type='" + type +"'");
			}
		} else {
			if(!"-1".equalsIgnoreCase(subtype.trim())) {
				return list;
			} else {
				sb.append("where alarmid in (select id from nms_alarm_indicators_node)");
			}
			return list;
		}
		return super.findByCondition(sb.toString());
	}
	
	public void test(String bid) {
		List list = new ArrayList();
	    StringBuffer s = new StringBuffer();
		int _flag = 0;
		if (bid != null){
			if(bid !="-1"){
				String[] bids = bid.split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(_flag==0){
								s.append(" and ( bid like '%,"+bids[i].trim()+",%' ");
								_flag = 1;
							}else{
								s.append(" or bid like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}
				
			}	
		}
	}
	
	public static void main(String[] args) {
//		AlarmLinkNode vo = new AlarmLinkNode();
//		//vo.setId(1);
//		vo.setFid(3);
//		vo.setLinkid(1);
//		vo.setAlarmid(12347);
//		vo.setIsCover(1);		// 不覆盖
//		vo.setIsEnabled(-1);		// 可以使用
//		vo.setIsCoverNext(-1);	// 不覆盖下级节点
//		vo.setNumber(1);		// 序号
//		vo.setLimenvalue0("62");
//		vo.setLimenvalue1("73");
//		vo.setLimenvalue2("84");
//		vo.setTime0(12);
//		vo.setTime1(13);
//		vo.setTime2(14);
//		vo.setWay0(16);
//		vo.setWay0(190);
//		vo.setWay0(201);
//		vo.setDescr("告警连接节点测试..2.");
//		AlarmLinkNodeDao dao = new AlarmLinkNodeDao();
//		try{
//			dao.save(vo);
//			//dao.update(vo);
//		} finally {
//			dao.close();
//		}
//		new AlarmLinkNodeDao().test(",2,,4,,5,,6,");
		List list = new AlarmLinkNodeDao().getAlarmLinkNodeByType(null, "oracle");
	}
}
