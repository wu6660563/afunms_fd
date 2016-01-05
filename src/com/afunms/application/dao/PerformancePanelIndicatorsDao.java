package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.afunms.application.model.PerformancePanelIndicatorsModel;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 16, 2011 3:36:42 PM
 * ��˵�� �������澯ָ����ĳ־ò�
 */
public class PerformancePanelIndicatorsDao extends BaseDao implements DaoInterface {
	
	public PerformancePanelIndicatorsDao() {
		super("nms_performance_panel_indicators");
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		PerformancePanelIndicatorsModel performancePanel = new PerformancePanelIndicatorsModel();
		try {
			performancePanel.setId(rs.getString("id"));
			performancePanel.setPanelName(rs.getString("panelName"));
			performancePanel.setIndicatorName(rs.getString("indicatorName"));
			performancePanel.setIndicatorDesc(rs.getString("indicatorDesc"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return performancePanel;
	}

	public boolean save(BaseVo vo) {
		PerformancePanelIndicatorsModel performancePanelIndicatorsModel = (PerformancePanelIndicatorsModel)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into nms_performance_panel_indicators (panelName,indicatorName,indicatorDesc) values ('");
		sql.append(performancePanelIndicatorsModel.getPanelName());
		sql.append("','");
		sql.append(performancePanelIndicatorsModel.getIndicatorName());
		sql.append("','");
		sql.append(performancePanelIndicatorsModel.getIndicatorDesc());
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		boolean result=false;
		PerformancePanelIndicatorsModel performancePanelIndicatorsModel = (PerformancePanelIndicatorsModel)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_performance_panel_indicators set deviceId = '");
		sql.append(performancePanelIndicatorsModel.getId());
		sql.append("', panelName = '");
		sql.append(performancePanelIndicatorsModel.getPanelName());
		sql.append("', indicatorsName = '");
		sql.append(performancePanelIndicatorsModel.getIndicatorName());
		sql.append("', indicatorDesc = '");
		sql.append(performancePanelIndicatorsModel.getIndicatorDesc());
		sql.append("' where id = '");
		sql.append(performancePanelIndicatorsModel.getId());
		sql.append("'");
		try {
			conn.executeUpdate(sql.toString());
			result=true;
			
		} catch (Exception e) {
			result=false;
			SysLogger.error("PerformancePanelIndicatorsDao:update", e);
		}
		finally{
			conn.close();
		}
		return result;
	}

	public void savePreformance(List<PerformancePanelIndicatorsModel> performancePanelIndicatorsList) {
		for(PerformancePanelIndicatorsModel p:performancePanelIndicatorsList){
			PerformancePanelIndicatorsModel PerformancePanelIndicatorsModel = (PerformancePanelIndicatorsModel)p;
			StringBuffer sql=new StringBuffer();
			sql.append("insert into nms_performance_panel_indicators (panelName,indicatorsName, indicatorDesc) values ('");
			sql.append(PerformancePanelIndicatorsModel.getPanelName());
			sql.append("','");
			sql.append(PerformancePanelIndicatorsModel.getIndicatorName());
			sql.append("','");
			sql.append(PerformancePanelIndicatorsModel.getIndicatorDesc());
			sql.append("')");
			try {
				conn.addBatch(sql.toString());
			} catch (Exception e) {
				e.printStackTrace();
				SysLogger.error("PerformancePanelIndicatorsDao.savePreformance()", e);
			}
		}
		conn.executeBatch();
	}

	public void savePreformancePanelIndicators(String panelName,String indicatorNames) {
		if(panelName == null){
			return ;
		}
		String[] indicatorNamesArray = indicatorNames.split(",");
		conn.executeUpdate("delete from nms_performance_panel_indicators where panelName = '"+panelName+"'");
		for(int i=0; i<indicatorNamesArray.length; i++){
			if(indicatorNamesArray[i] == null || indicatorNamesArray[i].equals("") || indicatorNamesArray[i].equals("roo,")
					|| indicatorNamesArray[i].equals(":")){
				continue;
			}
			String[] tempStrs = indicatorNamesArray[i].split(":");
			StringBuffer sql=new StringBuffer();
			sql.append("insert into nms_performance_panel_indicators (panelName,indicatorName,indicatorDesc) values ('");
			sql.append(panelName);
			sql.append("','");
			sql.append(tempStrs[0]);
			sql.append("','");
			sql.append(tempStrs[1]);
			sql.append("')");
			try {
				conn.addBatch(sql.toString());
			} catch (Exception e) {
				e.printStackTrace();
				SysLogger.error("PerformancePanelIndicatorsDao.savePreformancePanelIndicators()", e);
			}
		}
		conn.executeBatch();
	}
	
	public void deleteByPanelName(String panelName){
		String sql = "delete from nms_performance_panel_indicators where panelName = '"+panelName+"'";
		try {
			conn.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
	}
	
	/**
	 * ��ϸ����е��豸�ļ���ָ��
	 * @param panelName
	 * @param indicatorNames
	 * @return
	 */
	public boolean updatePanelIndicators(String panelName,String indicatorNames){
		boolean flag = false;
		if(panelName == null || indicatorNames == null){
			return flag;
		}
		String sqlStr = "delete from nms_performance_panel_indicators where panelName = '"+panelName+"'";
		try {
			//ɾ���ɼ�¼
			conn.executeUpdate(sqlStr,true);
			//���Ӽ�¼
			String[] indicatorNamesArray = indicatorNames.split(",");
			for(int i=0; i<indicatorNamesArray.length; i++){
				if(indicatorNamesArray[i] == null || indicatorNamesArray[i].equals("") || indicatorNamesArray[i].equals("roo,")
						|| indicatorNamesArray[i].equals(":")){
					continue;
				}
				String[] tempStrs = indicatorNamesArray[i].split(":");
				StringBuffer sql=new StringBuffer();
				sql.append("insert into nms_performance_panel_indicators (panelName,indicatorName,indicatorDesc) values ('");
				sql.append(panelName);
				sql.append("','");
				sql.append(tempStrs[0]);
				sql.append("','");
				sql.append(tempStrs[1]);
				sql.append("')");
				try {
					conn.addBatch(sql.toString());
				} catch (Exception e) {
					e.printStackTrace();
					SysLogger.error("PerformancePanelIndicatorsDao.updatePanelIndicators()", e);
				}
			}
			conn.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return flag;
	}
}
