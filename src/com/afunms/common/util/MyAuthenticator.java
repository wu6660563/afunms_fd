/**
 * <p>Description:utility class,includes some methods which are often used</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import javax.mail.*;
import javax.mail.internet.*;
//import javax.activation.*;
public class MyAuthenticator extends Authenticator{

  String user = "";
  String pass = "";

  public MyAuthenticator(String usr, String pwd)
  {
      user = usr;
      pass = pwd;
  }

  public PasswordAuthentication getPasswordAutentication(){
    return new PasswordAuthentication( user , pass );
  }
}
