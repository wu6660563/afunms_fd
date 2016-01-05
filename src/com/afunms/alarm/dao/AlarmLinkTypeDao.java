package com.afunms.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.afunms.alarm.model.AlarmLinkType;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;

public class AlarmLinkTypeDao extends BaseDao implements DaoInterface{

	public AlarmLinkTypeDao(){
		super("nms_alarm_link_type");
	}
	
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		AlarmLinkType vo = new AlarmLinkType();
		try {
			vo.setId(rs.getInt("id"));
			vo.setLinkName(rs.getString("linkname"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vo;
	}

	public boolean save(BaseVo vo) {
		AlarmLinkType type = (AlarmLinkType)vo;
		StringBuffer sb = new StringBuffer();
		sb.append("insert into nms_alarm_link_type(linkname) values('");
		sb.append(type.getLinkName() + "')");
		return super.saveOrUpdate(sb.toString());
	}

	public boolean update(BaseVo vo) {
		AlarmLinkType type = (AlarmLinkType)vo;
		StringBuffer sb = new StringBuffer();
		sb.append("update nms_alarm_link_type set linkname='");
		sb.append(type.getLinkName() + "' where id=" + type.getId());
		return super.saveOrUpdate(sb.toString());
	}

	public boolean delete(BaseVo vo) {
		AlarmLinkType type = (AlarmLinkType)vo;
		StringBuffer sb = new StringBuffer();
		sb.append("delete from nms_alarm_link_type ");
		sb.append("where id=" + type.getId());
		return super.saveOrUpdate(sb.toString());
	}
}
