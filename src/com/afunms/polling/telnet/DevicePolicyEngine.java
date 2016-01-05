package com.afunms.polling.telnet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.util.CompareRuleHelper;
import com.afunms.common.util.DBManager;
import com.afunms.config.dao.CompGroupRuleDao;
import com.afunms.config.dao.CompRuleDao;
import com.afunms.config.dao.CompStrategyDao;
import com.afunms.config.dao.DetailCompRuleDao;
import com.afunms.config.dao.Hua3VPNFileConfigDao;
import com.afunms.config.dao.StrategyIpDao;
import com.afunms.config.model.CompCheckResultModel;
import com.afunms.config.model.CompGroupRule;
import com.afunms.config.model.CompRule;
import com.afunms.config.model.CompStrategy;
import com.afunms.config.model.DetailCompRule;
import com.afunms.config.model.Hua3VPNFileConfig;
import com.afunms.config.model.StrategyIp;
import com.afunms.initialize.ResourceCenter;
import com.ibm.websphere.models.config.applicationserver.webcontainer.TuningParams;

public class DevicePolicyEngine {
	public void executePolicey(String strategyId) {
		CompStrategyDao strategyDao = new CompStrategyDao();
		CompStrategy strategy = (CompStrategy) strategyDao.findByID(strategyId);//
		strategyDao.close();
		// �Ȳ�ѯ��Ҫ�����豸IP�������ļ�·��
		List<StrategyIp> noAvaList = new ArrayList<StrategyIp>();// ����豸�����ļ������õļ�¼
		StrategyIpDao ipDao = new StrategyIpDao();
		List<String> ipList = ipDao.findIps(strategy.getId());// �����ѹر�
		Vector<String> ipVec = new Vector<String>();
		List configList = new ArrayList();
		File file = null;
		String type="";
		if (strategy.getType()==0) {
			type="run";
		}else {
			type="startup";
		}
		Hua3VPNFileConfigDao configDao = new Hua3VPNFileConfigDao();
		if (ipList != null && ipList.size() > 0) {
			configList = configDao.getDeviceByIps(ipList,type);
			// ��ʼ�ж��Ƿ���
			if (configList != null && configList.size() > 0) {
				for (int i = 0; i < configList.size(); i++) {
					Hua3VPNFileConfig config = (Hua3VPNFileConfig) configList
							.get(i);
					ipVec.add(config.getIpaddress());
				}
			} else {
				for (int i = 0; i < ipList.size(); i++) {
					StrategyIp strategyIp = new StrategyIp();
					strategyIp.setStrategyId(strategy.getId());
					strategyIp.setIp(ipList.get(i));
					strategyIp.setStrategyName(strategy.getName());
					strategyIp.setAvailability(0);
					noAvaList.add(strategyIp);
				}

			}

			Hua3VPNFileConfig config = null;
			if (ipVec != null && ipVec.size() > 0) {

				for (int j = 0; j < ipList.size(); j++) {
					String ip = ipList.get(j);
					if (!ipVec.contains(ip)) {// �����ҵ������ļ�·�������ݲ����ã�
						StrategyIp strategyIp = new StrategyIp();
						strategyIp.setStrategyId(strategy.getId());
						strategyIp.setIp(ip);
						strategyIp.setStrategyName(strategy.getName());
						strategyIp.setAvailability(0);

						noAvaList.add(strategyIp);
					} else {
						for (int i = 0; i < configList.size(); i++) {
							config = (Hua3VPNFileConfig) configList.get(i);
							if (config.getIpaddress().equals(ip))
								break;
						}
						file = new File(config.getFileName());
						
						
						if (!file.exists()) {
							StrategyIp strategyIp = new StrategyIp();
							strategyIp.setStrategyId(strategy.getId());
							strategyIp.setStrategyName(strategy.getName());
							strategyIp.setIp(ip);
							strategyIp.setAvailability(0);
							noAvaList.add(strategyIp);
						}
						file = null;
					}
				}

			}

		}
		List<CompCheckResultModel> compList = new ArrayList<CompCheckResultModel>();
		CompRuleDao ruleDao = new CompRuleDao();
		CompGroupRuleDao groupRuleDao = new CompGroupRuleDao();
		DetailCompRuleDao detailCompRuleDao = new DetailCompRuleDao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String checkTime = sdf.format(date);

		try {
			CompareRuleHelper helper = new CompareRuleHelper();
			String groupIds = strategy.getGroupId();
			String[] ids = new String[groupIds.split(",").length];
			ids = groupIds.split(",");
			for (int j = 0; j < ids.length; j++) {
				CompGroupRule groupRule = (CompGroupRule) groupRuleDao
						.findByID(ids[j]);
				String tempIds = groupRule.getRuleId();
				// Hashtable<Integer, List<DetailCompRule>> retHashtable =
				// detailCompRuleDao.findByIds(tempIds);
				String[] ruleIds = new String[tempIds.split(",").length];
				ruleIds = tempIds.split(",");
				for (int k = 0; k < ruleIds.length; k++) {
					CompRule compRule = (CompRule) ruleDao.findByID(ruleIds[k]);
					List detailList = (List) detailCompRuleDao.findByCondition(" where RULEID="
									+ compRule.getId());
					//
					boolean isCompared = false;
					int isViolation = 0;
					if (compRule.getSelect_type() == 0) {
						// ��ͨ��׼
						if (detailList != null && detailList.size() > 0) {
							DetailCompRule detailCompRule = (DetailCompRule) detailList
									.get(0);
							String content = detailCompRule.getExpression();
						
							String[] lines = new String[content.split("\r\n").length];
							lines = content.split("\r\n");

							List<String> linesList = new ArrayList<String>();
							for (int i = 0; i < lines.length; i++) {
								linesList.add(lines[i]);
							}
							if (configList != null && configList.size() > 0) {
								for (int i = 0; i < configList.size(); i++) {
									Hua3VPNFileConfig config = (Hua3VPNFileConfig) configList
											.get(i);

									file = new File(config.getFileName());
									if (file.exists()) {
										isCompared = helper.contentSimpleLines(
												file, linesList, detailCompRule
														.getIsContain());
										if (isCompared) {
											isViolation = 1;
										}
										CompCheckResultModel model = new CompCheckResultModel();
										model.setStrategyId(strategy.getId());
										model.setStrategyName(strategy
												.getName());
										model.setIp(config.getIpaddress());
										model.setGroupId(groupRule.getId());
										model.setGroupName(groupRule.getName());

										model.setRuleId(compRule.getId());
										model.setRuleName(compRule
												.getComprule_name());
										model.setDescription(compRule
												.getDescription());
										model.setViolationSeverity(compRule
												.getViolation_severity());
										model.setIsViolation(isViolation);
										model.setCheckTime(checkTime);

										compList.add(model);
									}
								}
							}

						}

					} else if (compRule.getSelect_type() == 1) {
						// �߼���׼
						String[] reg = null;
						int[] relation = null;
						boolean[] isContian = null;
						if (detailList != null && detailList.size() > 0) {
							reg = new String[detailList.size()];
							relation = new int[detailList.size()];
							isContian = new boolean[detailList.size()];
							for (int i = 0; i < detailList.size(); i++) {
								DetailCompRule detailCompRule = (DetailCompRule) detailList
										.get(i);
								reg[i] = detailCompRule.getExpression();
								relation[i] = detailCompRule.getRelation();
								if (detailCompRule.getIsContain() == 0) {
									isContian[i] = false;
								} else {
									isContian[i] = true;
									;
								}
							}
						}
						if (configList != null && configList.size() > 0) {
							for (int i = 0; i < configList.size(); i++) {
								Hua3VPNFileConfig config = (Hua3VPNFileConfig) configList
										.get(i);
								file = new File(config.getFileName());
								if (file.exists()) {

									isCompared = helper.contentSimpleWords(
											file, reg, relation, isContian);
									if (isCompared) {
										isViolation = 1;
									}
									CompCheckResultModel model = new CompCheckResultModel();
									model.setStrategyId(strategy.getId());
									model.setStrategyName(strategy.getName());
									model.setIp(config.getIpaddress());
									model.setGroupId(groupRule.getId());
									model.setGroupName(groupRule.getName());

									model.setRuleId(compRule.getId());
									model.setRuleName(compRule
											.getComprule_name());
									model.setDescription(compRule
											.getDescription());
									model.setViolationSeverity(compRule
											.getViolation_severity());
									model.setIsViolation(isViolation);
									model.setCheckTime(checkTime);
									compList.add(model);
								}
							}
						}
					} else if (compRule.getSelect_type() == 2) {
						// �û��Զ����׼
					}

				}
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			ruleDao.close();
			groupRuleDao.close();
			detailCompRuleDao.close();
			saveCheckResult(compList, ipList, noAvaList,strategy.getId());
		}
	}

	public boolean saveCheckResult(List<CompCheckResultModel> list,
			List<String> ipList, List<StrategyIp> noAvaList,int id) {
		// �����豸�б�
		StringBuffer sql = null;
		DBManager manager = new DBManager();
		
		try {
			sql = new StringBuffer();
			sql.append("update nms_comp_strategy_device set AVAILABILITY=1 where STRATEGY_ID="+id);
			manager.addBatch(sql.toString());
			sql = null;
			
			if (noAvaList != null && noAvaList.size() > 0) {

				for (int i = 0; i < noAvaList.size(); i++) {
					sql = new StringBuffer();
					StrategyIp strategyIp = noAvaList.get(i);
					sql.append("update nms_comp_strategy_device set STRATEGY_NAME='");
					sql.append(strategyIp.getStrategyName());
					sql.append("',AVAILABILITY=");
					sql.append(strategyIp.getAvailability());
					sql.append(" where IP='");
					sql.append(strategyIp.getIp());
					sql.append("' and STRATEGY_ID=");
					sql.append(strategyIp.getStrategyId());
					System.out.println("****" + sql.toString());
					manager.addBatch(sql.toString());
					sql = null;
				}
			}
			if (ipList != null && ipList.size() > 0 ) {
				// ɾ��֮ǰ�ļ�¼
				String sql0 = "";
				for (int j = 0; j < ipList.size(); j++) {
					sql0 = "delete from nms_comp_check_results where IP='"
							+ ipList.get(j) + "' and STRATEGY_ID="+id;
					manager.addBatch(sql0);
				}
			}
			if (list != null&& list.size() > 0) {
				

				

				for (int i = 0; i < list.size(); i++) {
					CompCheckResultModel model = list.get(i);
					sql = new StringBuffer();
					sql
							.append("insert into nms_comp_check_results(STRATEGY_ID,STRATEGY_NAME,IP,GROUP_ID,GROUP_NAME,RULE_ID,RULE_NAME,DESCRIPTION,"
									+ "VIOLATION_SEVERITY,ISVIOLATION,CHECK_TIME) values(");
					sql.append(model.getStrategyId());
					sql.append(",'");
					sql.append(model.getStrategyName());
					sql.append("','");
					sql.append(model.getIp());
					sql.append("',");
					sql.append(model.getGroupId());
					sql.append(",'");
					sql.append(model.getGroupName());
					sql.append("',");
					sql.append(model.getRuleId());
					sql.append(",'");
					sql.append(model.getRuleName());
					sql.append("','");
					sql.append(model.getDescription());
					sql.append("',");
					sql.append(model.getViolationSeverity());
					sql.append(",");
					sql.append(model.getIsViolation());
					sql.append(",'");
					sql.append(model.getCheckTime());
					sql.append("')");
					manager.addBatch(sql.toString());
				}

			}
			manager.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (manager != null) {
				try {
					manager.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				manager = null;
			}
		}
		return false;
	}

	public void name() {
	}

	public static void main(String[] args) {

	}
}
