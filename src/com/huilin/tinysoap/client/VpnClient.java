package com.huilin.tinysoap.client;

public class VpnClient {

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
		String wapUri = "http://211.138.109.68/oatest/acceptvpn.jsp?uid=dujuan&code=1&pwd=YnkR9jganAn9XKuRe9bhuLYJ&time=20080312011020";
		// push����
		String uid = "zhangsuwen"; // �û�id,����¼poral���û���
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

		
	}

}
