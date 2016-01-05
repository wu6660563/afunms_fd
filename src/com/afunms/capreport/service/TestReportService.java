/*
 * @(#)TestReportService.java     v1.01, Jun 24, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.afunms.application.util.ReportExport;
import com.afunms.capreport.model.CustomReportVo;
import com.afunms.indicators.util.Constant;
import com.afunms.initialize.ResourceCenter;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.User;

/**
 * ClassName: TestReportService.java
 * <p>
 * ���Ա����Ĺ���
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Jun 24, 2013 4:30:43 PM
 */
public class TestReportService {

	private static Logger log = Logger.getLogger(TestReportService.class);

	public boolean create(CustomReportVo vo, String startdate, String enddate) {
		boolean success = false;

		String startTime = startdate + " 00:00:00";
		String endTime = enddate + " 23:59:59";

		CreateReportUtil util = new CreateReportUtil();
		String dataStr[][] = util.getNodeAlarmReportData(startTime, endTime,
				Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
		if (dataStr == null) {
			return false;
		}

		try {
			String filePath = ResourceCenter.getInstance().getSysPath()
					+ "/reportFile/";
			String filename_xls = "";
			String filename_doc = "";
			String filename_pdf = "";
			CreateReportService service = new CreateReportService();
			filename_xls = service.getFileName(vo, "excel");
			filename_doc = service.getFileName(vo, "word");
			filename_pdf = service.getFileName(vo, "pdf");

			ReportExport export = new ReportExport();
			String type = "node";
			String ids = ""; // �˴�Ϊ���е��豸������Ҫids

			// ����EXCEL
			boolean bln_xls = false;
			try {
				log.info("�ļ�·����" + filePath + filename_xls);
				export.exportCustomReport(ids, type, filePath + filename_xls,
						startTime, endTime, "xls", dataStr); // ����PDF
				bln_xls = true;

				// ������־
				if (bln_xls) {
					util.saveHistory(vo.getId(), filename_xls);
				}
			} catch (Exception e) {
				bln_xls = false;
				e.printStackTrace();
			}

			// ����WORD
			boolean bln_doc = false;
			try {
				export.exportCustomReport(ids, type, filePath + filename_doc,
						startTime, endTime, "doc", dataStr); // ����WORD
				bln_doc = true;

				// ������־
				if (bln_doc) {
					util.saveHistory(vo.getId(), filename_doc);
				}
			} catch (Exception e) {
				bln_doc = false;
				e.printStackTrace();
			}

			// ����PDF
			boolean bln_pdf = false;
			try {
				export.exportCustomReport(ids, type, filePath + filename_pdf,
						startTime, endTime, "pdf", dataStr); // ����PDF
				bln_pdf = true;

				// ������־
				if (bln_pdf) {
					util.saveHistory(vo.getId(), filename_pdf);
				}
			} catch (Exception e) {
				bln_pdf = false;
				e.printStackTrace();
			}

			if (bln_xls && bln_doc && bln_pdf && "1".equals(vo.getIsSend())) {
				success = true;
				// �����ʼ�
				List<User> userList = new ArrayList<User>();
				UserDao userDao = null;
				User user = null;
				String userId = vo.getUserId();
				String[] id = userId.split(",");
				for (int i = 0; i < id.length; i++) {
					if (!"".equals(id[i])) {
						userDao = new UserDao();
						try {
							user = (User) userDao.findByID(id[i]);
							userList.add(user);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							userDao.close();
						}
					}
				}
				String receivemailaddr = "";
				for (int i = 0; i < userList.size(); i++) {
					if (i + 1 != userList.size()) {
						receivemailaddr += userList.get(i).getEmail() + ",";
					} else {
						receivemailaddr += userList.get(i).getEmail();
					}
				}

				if (userList.size() > 0 && !"".equals(receivemailaddr)) {
					String[] fileName = null;
					String[] path = new String[] { filePath };
					if ("excel".equals(vo.getFileType())) {
						fileName = new String[] { filename_xls };
					} else if ("word".equals(vo.getFileType())) {
						fileName = new String[] { filename_doc };
					} else if ("pdf".equals(vo.getFileType())) {
						fileName = new String[] { filename_pdf };
					}

					success = util
							.sendEmail(receivemailaddr, vo.getMailTitle(), vo
									.getMailDesc(), path, fileName);
					if (success) {
						log.info("������:�ʼ�������ϣ�");
					}
				} else {
					log.info("������:�û�δ�����������δѡ���û����޷������ʼ���");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

}
