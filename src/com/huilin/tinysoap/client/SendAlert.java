package com.huilin.tinysoap.client;

public class SendAlert {

	/**
	 * ����soap���ÿͻ���,����ʾ������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String appId = "101011"; // �ɶ�������ƽ̨ͳһ�����Ӧ�ñ�ʶ���൱����ҵid��

		String title = "aslkdfalskdf"; // wap push ����
		String content = "�������ݲ��Թ������ݲ��Թ����������Թ������ݲ��Թ������ݲ��Թ�������"; // ��������
		///String wapUri = "211.138.109.68/wapoa/rds.jsp?flowId=flow6018601892605466511570_12006444414117812&nodeId=node2042245782870_12006444965172465"; // wap
		String wapUri = "http://211.138.109.68/oatest/acceptvpn.jsp?uid=dujuan&code=1&pwd=123&time=20080312011020";
		// push����
		String uid = "yaoyutian"; // �û�id,����¼poral���û���
		String misId = ""; // Ա�����, uid �� misId �ṩһ������

		//String soapUri = "http://localhost:8080/alert/services/HuilinAlertService";
		String soapUri = "http://211.138.109.69:15699";
		String out = null;
		// �������ѽӿڵ���
		out = AlertClient.sendAlert(soapUri, appId, title, content, wapUri,
				uid, misId);

		// ����ֵ��һ��xml��ʽ���ַ�������ʽ�����ӿڹ淶��
		System.out
				.println("-----------------------���ѽӿڷ���ֵ����--------------------\n");
		System.out.println(out);

		/*// ���Ͷ��Žӿڵ���
		out = "";
		String phoneNo = "13834220402";
		// content = "";
		out = AlertClient.sendSms(soapUri, appId, content, phoneNo);
		System.out
				.println("-----------------------���Žӿڷ���ֵ����--------------------\n");
		System.out.println(out);*/

	}

}
