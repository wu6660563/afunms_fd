package montnets;

import java.io.File;
import java.io.IOException;

import montnets.mondem;

  public class SmsServer {
	  
	  
	public int sendSMS(String num, String msg) {
		mondem Mytest = new mondem(); // 创建一个 mondem 对象， 这个对象最大可以支持64个端口发送
		int rc=-1;
		rc = Mytest.SetThreadMode(1); // 开启线程模式
		if (rc == 0) {
			System.out.println("设置线程模式成功");
		} else {
			System.out.println("设置线程模式失败");
		}
		// 全都设置成单口猫格式
		Mytest.SetModemType(0, 0);
		Mytest.SetModemType(1, 0);
		Mytest.SetModemType(2, 0);
		Mytest.SetModemType(3, 0);
		Mytest.SetModemType(4, 0);
		Mytest.SetModemType(5, 0);
		Mytest.SetModemType(6, 0);
		Mytest.SetModemType(7, 0);

		if ((rc = (Mytest.InitModem(-1))) == 0)// 初始化短信猫
		{
			System.out.println("初始化成功");
			try {
				rc = Mytest.SendMsg(-1, num, msg); // 发送一条信息
			} catch (Exception ex) {
				ex.printStackTrace();
				rc = -1;
			}
			if (rc >= 0) {
				System.out.println("提交成功, rc=" + rc);
			} else {
				System.out.println("提交错误, rc=" + rc);
			}
		} else {
			System.out.println("初始化错误!" + rc);
		}
		return rc;
	}

	public static void main(String args[]) {
		SmsServer test = new SmsServer();
		//test.sendSMS("15210016034", "测试信息，恭喜发财!");

	}
}
