package com.huilin.tinysoap.client;

public class VpnClient {

	/**
	 * 慧林soap调用客户端,调用示例代码
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String appId = "101011"; // 由短信提醒平台统一分配的应用标识（相当于企业id）

		String title = "aslkdfalskdf"; // wap push 标题
		String content = "工单内容测试工单内容测试工单内容容试工单内容测试工单内容测试工单内容"; // 短信内容
		///String wapUri = "211.138.109.68/wapoa/rds.jsp?flowId=flow6018601892605466511570_12006444414117812&nodeId=node2042245782870_12006444965172465"; // wap
		String wapUri = "http://211.138.109.68/oatest/acceptvpn.jsp?uid=dujuan&code=1&pwd=YnkR9jganAn9XKuRe9bhuLYJ&time=20080312011020";
		// push链接
		String uid = "zhangsuwen"; // 用户id,即登录poral的用户名
		String misId = ""; // 员工编号, uid 与 misId 提供一个即可

		//String soapUri = "http://localhost:8080/alert/services/HuilinAlertService";
		String soapUri = "http://211.138.109.69:15699";
		String out = null;
		// 发送提醒接口调用
		out = AlertClient.sendAlert(soapUri, appId, title, content, wapUri,
				uid, misId);

		// 返回值是一个xml格式的字符串，格式见《接口规范》
		System.out
				.println("-----------------------提醒接口返回值如下--------------------\n");
		System.out.println(out);

		
	}

}
