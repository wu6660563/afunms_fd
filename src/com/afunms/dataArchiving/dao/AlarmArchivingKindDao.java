package com.afunms.dataArchiving.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.dataArchiving.model.AlarmArchivingKind;
import com.afunms.event.dao.CheckEventDao;

/**
 * 告警归档类型持久层操作
 * 
 * @author Administrator
 * 
 */
public class AlarmArchivingKindDao extends BaseDao implements DaoInterface {

	/**
	 * 	告警数据system_eventlist数据存留时间为4周
	 */
    private static final String sysEventListRetentionTime = "2419200000";// 1000*60*60*24*7*4;

    private static SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

    private static SysLogger logger = SysLogger.getLogger(AlarmArchivingKindDao.class
                    .getName());

    /**
     * 构造
     */
    public AlarmArchivingKindDao() {
        super("system_alarm_archiving_kinds");
    }

    /**
     * 暂时未提供
     */
    public boolean save(BaseVo vo) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 只更新表中的归档时间
     */
    public boolean update(BaseVo vo) {
        // TODO Auto-generated method stub
        AlarmArchivingKind kind = (AlarmArchivingKind) vo;
        StringBuffer sql = new StringBuffer();
        String latestTime = sdf.format(kind.getLatestArchiveTime().getTime());
        sql.append("update system_alarm_archiving_kinds set ");
        sql.append("latest_archive_time='");
        sql.append(latestTime);
        sql.append("' where id =");
        sql.append(kind.getId());
        logger.info("更新告警归档类型数据: " + sql.toString()); // 输出sql语句
        try{
        	conn.executeUpdate(sql.toString());
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }
       
        return true;
    }

    /**
     * 获取归档类型数据
     * 
     * @return List
     */
    public List getArchivingKind() {
        List list = new ArrayList();
        String sql = "select * from system_alarm_archiving_kinds; ";
        try {
            rs = conn.executeQuery(sql);
            while (rs.next()) {
                list.add(loadFromRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
   //     	conn.close();
        }
        logger.info(sql);
        return list;
    }

    /**
     * 获取最新的已经归档时间
     * 
     * 保证同步性
     * 
     * @param archivingId
     * @return
     */
    public Calendar getLatestTime(int archivingId) {
        String sql = "select latest_archive_time from system_alarm_archiving_kinds where "
                        + " id = " + archivingId;
        Calendar latest = Calendar.getInstance();
        try {
            rs = conn.executeQuery(sql);
            while (rs.next()) {
                Calendar cal = Calendar.getInstance();
                Date newdate = new Date();
                newdate.setTime(rs.getTimestamp("latest_archive_time")
                                .getTime());
                latest.setTime(newdate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            latest = null;
        } finally {
  //      	conn.close();
        }
        return latest;
    }

    private int dataIsNull(String sql) {
        try {
            rs = conn.executeQuery(sql);
            while (rs.next()) {
                return 1;
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据归档类型进行归档
     * 
     * @param kind
     * @return
     */
    public boolean archiveDataByKind(AlarmArchivingKind kind) {

        logger.info("******开始归档进行 " + kind.getName() + " 数据归档******");
        Long t1 = System.currentTimeMillis(); // 计算运行时间参数
        try {
            Calendar latest = kind.getLatestArchiveTime();

            String startTime = sdf.format(latest.getTime());// 上次归档时间
            // 新的归档时间 = 上次归档时间 + 归档时间间隔
            String endTime = sdf.format(new Date(latest.getTime().getTime()
                            + Long.parseLong(kind.getArchiveInterval())));

            logger.info("startTime : " + startTime + "  | "
                            + "endTime : " + endTime);
            if ("HOUR".equals(kind.getType())) {
                // 归档小时数据
                archiveByHour(startTime, endTime);
            }
            if ("DAY".equals(kind.getType())) {
                // 归档天数据
                archiveByDay(startTime, endTime);
            }
            try {
                latest.setTime(sdf.parse(endTime));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // 设置并更新最新的归档时间
            kind.setLatestArchiveTime(latest);
            update(kind);

            Long t2 = System.currentTimeMillis();
            logger.info("!!!!!!!!!!!当前归档运行时间： " + (t2 - t1)
                            + " ms    **************");
        } catch (Exception e) {
            logger.info("告警数据归档错误！！！");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
    	String time = "2012-06-29 15:00:00";
    	Date date = null;
    	try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		System.out.println(date.getTime());
		System.out.println(sysEventListRetentionTime);
		
		
		String newtime = sdf.format(date.getTime() - Long.parseLong(sysEventListRetentionTime));
		
		System.out.println(newtime);
		
    }
    
    public boolean deleteDataByKind(AlarmArchivingKind kind) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        if ("HOUR".equals(kind.getType())) {// && now.get(Calendar.HOUR_OF_DAY)
                                            // == 1){
            // 删除源数据和当前的数据
        	logger.info("time: "+kind.getLatestArchiveTime().getTime()
        						+" - retentionTime:" + sysEventListRetentionTime);
            String srcStartTime = sdf.format(kind.getLatestArchiveTime()
                            .getTime().getTime()
                            - Long.parseLong(sysEventListRetentionTime));
            
            logger.info("srcStartTime:" + srcStartTime);
            
            deleteSysEventList(srcStartTime);

            if (getLatestTime(kind.getChildrenId()).getTime().getTime()
                            - (new Date().getTime() - Long.parseLong(kind
                                            .getRetentionTime())) > 0) {
                String hourStartTime = sdf.format(kind.getLatestArchiveTime()
                                .getTime().getTime()
                                - Long.parseLong(kind.getRetentionTime()));
                logger.info("delete hour:" + hourStartTime);
                deleteAlarmHour(hourStartTime);
            }

        }
        if ("DAY".equals(kind.getType())) {// &&
            // Calendar.SUNDAY == now.get(Calendar.DAY_OF_WEEK)){
            // 删除天的数据
            String strtTime = sdf.format(kind.getLatestArchiveTime().getTime()
                            .getTime()
                            - Long.parseLong(kind.getRetentionTime()));
            deleteAlarmDay(strtTime);
        }

        return false;
    }

    private void archiveByHour(String startTime, String endTime) {
        logger.info("archive hour .............");
        String insertSql = "insert into system_alarm_archiving_hour"
                        + "(eventlocation,content,level1,recordtime,nodeid,subtype,count) "
                        + " SELECT eventlocation,content,level1,min(recordtime) as recordtime,nodeid,subtype,count(*) as count "
                        + " FROM system_eventlist as src "
                        + " where src.recordtime>='" + startTime
                        + "' and src.recordtime<'" + endTime
                        + "' group by src.eventlocation,"
                        + " src.subtype, src.nodeid, src.level1 ";
        logger.info(insertSql);
        conn.executeUpdate(insertSql); // 归档并保存
  //      conn.close();
    }

    private void archiveByDay(String startTime, String endTime) {
        logger.info("archive day .............");
        String insertSql = "insert into system_alarm_archiving_day"
                        + "(eventlocation,content,level1,recordtime,nodeid,subtype,count) "
                        + " SELECT eventlocation,content,level1,min(recordtime) as recordtime,nodeid,subtype,sum(count) as count "
                        + " FROM system_alarm_archiving_hour as src "
                        + " where src.recordtime>='" + startTime
                        + "' and src.recordtime<'" + endTime
                        + "' group by src.eventlocation,"
                        + " src.subtype, src.nodeid, src.level1 ";
        logger.info(insertSql);
        conn.executeUpdate(insertSql); // 归档并保存
//        conn.close();
    }

    private void deleteSysEventList(String startTime) {
        String deleteSql = "delete from system_eventlist "
                        + " where recordtime < '" + startTime + "';";
        logger.info(deleteSql);
        conn.executeUpdate(deleteSql);
    }

    private void deleteAlarmHour(String startTime) {
        String deleteSql = "delete from system_alarm_archiving_hour "
                        + " where recordtime < '" + startTime + "';";
        logger.info(deleteSql);
        conn.executeUpdate(deleteSql);
    }

    private void deleteAlarmDay(String startTime) {
        String deleteSql = "delete from system_alarm_archiving_day "
                        + " where recordtime < '" + startTime + "';";
        logger.info(deleteSql);
        conn.executeUpdate(deleteSql);
    }

    /**
     * 数据集转换为VO对象
     */
    @Override
    public BaseVo loadFromRS(ResultSet rs) {

        AlarmArchivingKind kind = null;

        try {
            kind = new AlarmArchivingKind();
            kind.setId(rs.getInt("id"));
            kind.setName(rs.getString("name"));
            kind.setType(rs.getString("type"));
            Calendar cal = Calendar.getInstance();
            Date newdate = new Date();

            newdate.setTime(rs.getTimestamp("latest_archive_time").getTime());
            cal.setTime(newdate);
            kind.setLatestArchiveTime(cal);

            kind.setFatherId(rs.getInt("father_id"));
            kind.setChildrenId(rs.getInt("children_id"));
            kind.setArchiveInterval(rs.getString("archive_interval"));
            kind.setRetentionTime(rs.getString("retention_time"));

        } catch (SQLException e) {
            e.printStackTrace();
            kind = new AlarmArchivingKind();
        }
        logger.info(kind.toString());
        return kind;
    }

//    public static void main(String[] args) {
//        AlarmArchivingKindDao kinddao = new AlarmArchivingKindDao();
//        List<AlarmArchivingKind> list = kinddao.getArchivingKind();
//        for (AlarmArchivingKind kind : list) {
//            logger.info(kind.getType());
//            kinddao.archiveDataByKind(kind);
//        }
//    }
}
