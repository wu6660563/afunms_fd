package com.afunms.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class CmdTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec("cmd /c dscli");
			System.out.println("=================�������1==================");
			//OutputStream outputStream =process.getOutputStream();
			InputStream inputStream =process.getInputStream();
			String instr = "";
			//OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf8"); 
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf8");// �������������Ϊutf8�����������utf8����������ж����������
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			while((instr = bufferedReader.readLine()) != null){
				System.out.println(instr);
				instr = "";
			}
//			System.out.println("=================�������2==================");
//			writer.write("admin \r");
//			//writer.flush();
//			writer.write("passw0rd \r");
//			//writer.flush();
//			writer.write("help \r");
//			//writer.flush();
//			writer.close();
			
			System.out.println("=================�������3==================");
			
			
			System.out.println("=================�������==================");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
