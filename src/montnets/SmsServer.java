package montnets;

import java.io.File;
import java.io.IOException;

import montnets.mondem;

  public class SmsServer {
	  
	  
	public int sendSMS(String num, String msg) {
		mondem Mytest = new mondem(); // ����һ�� mondem ���� �������������֧��64���˿ڷ���
		int rc=-1;
		rc = Mytest.SetThreadMode(1); // �����߳�ģʽ
		if (rc == 0) {
			System.out.println("�����߳�ģʽ�ɹ�");
		} else {
			System.out.println("�����߳�ģʽʧ��");
		}
		// ȫ�����óɵ���è��ʽ
		Mytest.SetModemType(0, 0);
		Mytest.SetModemType(1, 0);
		Mytest.SetModemType(2, 0);
		Mytest.SetModemType(3, 0);
		Mytest.SetModemType(4, 0);
		Mytest.SetModemType(5, 0);
		Mytest.SetModemType(6, 0);
		Mytest.SetModemType(7, 0);

		if ((rc = (Mytest.InitModem(-1))) == 0)// ��ʼ������è
		{
			System.out.println("��ʼ���ɹ�");
			try {
				rc = Mytest.SendMsg(-1, num, msg); // ����һ����Ϣ
			} catch (Exception ex) {
				ex.printStackTrace();
				rc = -1;
			}
			if (rc >= 0) {
				System.out.println("�ύ�ɹ�, rc=" + rc);
			} else {
				System.out.println("�ύ����, rc=" + rc);
			}
		} else {
			System.out.println("��ʼ������!" + rc);
		}
		return rc;
	}

	public static void main(String args[]) {
		SmsServer test = new SmsServer();
		//test.sendSMS("15210016034", "������Ϣ����ϲ����!");

	}
}
