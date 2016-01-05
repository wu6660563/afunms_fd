package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.afunms.common.util.CommonUtil;

public class AlertTable {

    public void alert(final String ipaddress, final String where, final Connection conn) {
        
        Thread thread = new Thread(new Runnable() {

            public void run() {
                Statement util2 = null;
                try {
                    util2 = conn.createStatement();
//                if (i<=78){
//                    continue;
//                }
                    String sql = "";
                    
//                sql = "CREATE TABLE if not exists "+"ping"+CommonUtil.doip(ipaddress)
//                + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE if not exists "+"pinghour"+CommonUtil.doip(ipaddress)
//                + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE if not exists "+"pingday"+CommonUtil.doip(ipaddress)
//                + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                
                    String ping = "delete from ping" + CommonUtil.doip(ipaddress) + where;
                    String pinghour = "delete from pinghour" + CommonUtil.doip(ipaddress) + where;
                    String pingday = "delete from pingday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(ping);
                    util2.executeUpdate(ping);
                    System.out.println(pinghour);
                    util2.executeUpdate(pinghour);
                    System.out.println(pingday);
                    util2.executeUpdate(pingday);
                    
                    sql = "CREATE TABLE if not exists "+"cpu"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"cpuhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"cpuday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    String cpu = "delete from cpu" + CommonUtil.doip(ipaddress) + where;
                    String cpuhour = "delete from cpuhour" + CommonUtil.doip(ipaddress) + where;
                    String cpuday = "delete from cpuday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(cpu);
                    util2.executeUpdate(cpu);
                    System.out.println(cpuhour);
                    util2.executeUpdate(cpuhour);
                    System.out.println(cpuday);
                    util2.executeUpdate(cpuday);

                    sql = "CREATE TABLE if not exists "+"memory"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"memoryhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"memoryday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String memory = "delete from memory" + CommonUtil.doip(ipaddress) + where;
                    String memoryhour = "delete from memoryhour" + CommonUtil.doip(ipaddress) + where;
                    String memoryday = "delete from memoryday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(memory);
                    util2.executeUpdate(memory);
                    System.out.println(memoryhour);
                    util2.executeUpdate(memoryhour);
                    System.out.println(memoryday);
                    util2.executeUpdate(memoryday);

                    
                    sql = "CREATE TABLE if not exists "+"flash"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"flashhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"flashday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String flash = "delete from flash" + CommonUtil.doip(ipaddress) + where;
                    String flashhour = "delete from flashhour" + CommonUtil.doip(ipaddress) + where;
                    String flashday = "delete from flashday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(flash);
                    util2.executeUpdate(flash);
                    System.out.println(flashhour);
                    util2.executeUpdate(flashhour);
                    System.out.println(flashday);
                    util2.executeUpdate(flashday);

                    
                    sql = "CREATE TABLE if not exists "+"buffer"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"bufferhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"bufferday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String buffer = "delete from buffer" + CommonUtil.doip(ipaddress) + where;
                    String bufferhour = "delete from bufferhour" + CommonUtil.doip(ipaddress) + where;
                    String bufferday = "delete from bufferday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(buffer);
                    util2.executeUpdate(buffer);
                    System.out.println(bufferhour);
                    util2.executeUpdate(bufferhour);
                    System.out.println(bufferday);
                    util2.executeUpdate(bufferday);
   

                    sql = "CREATE TABLE if not exists "+"temper"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"temperhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"temperday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String temper = "delete from temper" + CommonUtil.doip(ipaddress) + where;
                    String temperhour = "delete from temperhour" + CommonUtil.doip(ipaddress) + where;
                    String temperday = "delete from temperday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(temper);
                    util2.executeUpdate(temper);
                    System.out.println(temperhour);
                    util2.executeUpdate(temperhour);
                    System.out.println(temperday);
                    util2.executeUpdate(temperday);


                    sql = "CREATE TABLE if not exists "+"fan"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"fanhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"fanday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String fan = "delete from fan" + CommonUtil.doip(ipaddress) + where;
                    String fanhour = "delete from fanhour" + CommonUtil.doip(ipaddress) + where;
                    String fanday = "delete from fanday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(fan);
                    util2.executeUpdate(fan);
                    System.out.println(fanhour);
                    util2.executeUpdate(fanhour);
                    System.out.println(fanday);
                    util2.executeUpdate(fanday);


                    sql = "CREATE TABLE if not exists "+"power"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"powerhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"powerday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String power = "delete from power" + CommonUtil.doip(ipaddress) + where;
                    String powerhour = "delete from powerhour" + CommonUtil.doip(ipaddress) + where;
                    String powerday = "delete from powerday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(power);
                    util2.executeUpdate(power);
                    System.out.println(powerhour);
                    util2.executeUpdate(powerhour);
                    System.out.println(powerday);
                    util2.executeUpdate(powerday);


                    sql = "CREATE TABLE if not exists "+"vol"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"volhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"volday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String vol = "delete from vol" + CommonUtil.doip(ipaddress) + where;
                    String volhour = "delete from volhour" + CommonUtil.doip(ipaddress) + where;
                    String volday = "delete from volday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(vol);
                    util2.executeUpdate(vol);
                    System.out.println(volhour);
                    util2.executeUpdate(volhour);
                    System.out.println(volday);
                    util2.executeUpdate(volday);


                    sql = "CREATE TABLE if not exists "+"utilhdx"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"utilhdxhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"utilhdxday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String utilhdx = "delete from utilhdx" + CommonUtil.doip(ipaddress) + where;
                    String utilhdxhour = "delete from utilhdxhour" + CommonUtil.doip(ipaddress) + where;
                    String utilhdxday = "delete from utilhdxday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(utilhdx);
                    util2.executeUpdate(utilhdx);
                    System.out.println(utilhdxhour);
                    util2.executeUpdate(utilhdxhour);
                    System.out.println(utilhdxday);
                    util2.executeUpdate(utilhdxday);


                    sql = "CREATE TABLE if not exists "+"allutilhdx"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"allutilhdxhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"allutilhdxday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String allutilhdx = "delete from allutilhdx" + CommonUtil.doip(ipaddress) + where;
                    String allutilhdxhour = "delete from allutilhdxhour" + CommonUtil.doip(ipaddress) + where;
                    String allutilhdxday = "delete from allutilhdxday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(allutilhdx);
                    util2.executeUpdate(allutilhdx);
                    System.out.println(allutilhdxhour);
                    util2.executeUpdate(allutilhdxhour);
                    System.out.println(allutilhdxday);
                    util2.executeUpdate(allutilhdxday);


                    sql = "CREATE TABLE if not exists "+"utilhdxperc"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"hdxperchour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"hdxpercday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String utilhdxperc = "delete from utilhdxperc" + CommonUtil.doip(ipaddress) + where;
                    String hdxperchour = "delete from hdxperchour" + CommonUtil.doip(ipaddress) + where;
                    String hdxpercday = "delete from hdxpercday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(utilhdxperc);
                    util2.executeUpdate(utilhdxperc);
                    System.out.println(hdxperchour);
                    util2.executeUpdate(hdxperchour);
                    System.out.println(hdxpercday);
                    util2.executeUpdate(hdxpercday);


                    sql = "CREATE TABLE if not exists "+"discardsperc"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"dcardperchour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"dcardpercday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String discardsperc = "delete from discardsperc" + CommonUtil.doip(ipaddress) + where;
                    String dcardperchour = "delete from dcardperchour" + CommonUtil.doip(ipaddress) + where;
                    String dcardpercday = "delete from dcardpercday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(discardsperc);
                    util2.executeUpdate(discardsperc);
                    System.out.println(dcardperchour);
                    util2.executeUpdate(dcardperchour);
                    System.out.println(dcardpercday);
                    util2.executeUpdate(dcardpercday);


                    sql = "CREATE TABLE if not exists "+"errorsperc"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"errperchour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"errpercday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String errorsperc = "delete from errorsperc" + CommonUtil.doip(ipaddress) + where;
                    String errperchour = "delete from errperchour" + CommonUtil.doip(ipaddress) + where;
                    String errpercday = "delete from errpercday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(errorsperc);
                    util2.executeUpdate(errorsperc);
                    System.out.println(errperchour);
                    util2.executeUpdate(errperchour);
                    System.out.println(errpercday);
                    util2.executeUpdate(errpercday);


                    sql = "CREATE TABLE if not exists "+"packs"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"packshour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"packsday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String packs = "delete from packs" + CommonUtil.doip(ipaddress) + where;
                    String packshour = "delete from packshour" + CommonUtil.doip(ipaddress) + where;
                    String packsday = "delete from packsday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(packs);
                    util2.executeUpdate(packs);
                    System.out.println(packshour);
                    util2.executeUpdate(packshour);
                    System.out.println(packsday);
                    util2.executeUpdate(packsday);

                    
                    sql = "CREATE TABLE if not exists "+"inpacks"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"inpackshour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"inpacksday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    

                    String inpacks = "delete from inpacks" + CommonUtil.doip(ipaddress) + where;
                    String inpackshour = "delete from inpackshour" + CommonUtil.doip(ipaddress) + where;
                    String inpacksday = "delete from inpacksday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(inpacks);
                    util2.executeUpdate(inpacks);
                    System.out.println(inpackshour);
                    util2.executeUpdate(inpackshour);
                    System.out.println(inpacksday);
                    util2.executeUpdate(inpacksday);


                    sql = "CREATE TABLE if not exists "+"outpacks"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"outpackshour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"outpacksday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String outpacks = "delete from outpacks" + CommonUtil.doip(ipaddress) + where;
                    String outpackshour = "delete from outpackshour" + CommonUtil.doip(ipaddress) + where;
                    String outpacksday = "delete from outpacksday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(outpacks);
                    util2.executeUpdate(outpacks);
                    System.out.println(outpackshour);
                    util2.executeUpdate(outpackshour);
                    System.out.println(outpacksday);
                    util2.executeUpdate(outpacksday);

                    
                    sql = "CREATE TABLE if not exists "+"pro"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"prohour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"proday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    

                    String pro = "delete from pro" + CommonUtil.doip(ipaddress) + where;
                    String prohour = "delete from prohour" + CommonUtil.doip(ipaddress) + where;
                    String proday = "delete from proday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(pro);
                    util2.executeUpdate(pro);
                    System.out.println(prohour);
                    util2.executeUpdate(prohour);
                    System.out.println(proday);
                    util2.executeUpdate(proday);


                    sql = "CREATE TABLE if not exists "+"cpudtl"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"cpudtlhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"cpudtlday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String cpudtl = "delete from cpudtl" + CommonUtil.doip(ipaddress) + where;
                    String cpudtlhour = "delete from cpudtlhour" + CommonUtil.doip(ipaddress) + where;
                    String cpudtlday = "delete from cpudtlday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(cpudtl);
                    util2.executeUpdate(cpudtl);
                    System.out.println(cpudtlhour);
                    util2.executeUpdate(cpudtlhour);
                    System.out.println(cpudtlday);
                    util2.executeUpdate(cpudtlday);


                    sql = "CREATE TABLE if not exists "+"disk"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"diskhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"diskday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String disk = "delete from disk" + CommonUtil.doip(ipaddress) + where;
                    String diskhour = "delete from diskhour" + CommonUtil.doip(ipaddress) + where;
                    String diskday = "delete from diskday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(disk);
                    util2.executeUpdate(disk);
                    System.out.println(diskhour);
                    util2.executeUpdate(diskhour);
                    System.out.println(diskday);
                    util2.executeUpdate(diskday);


                    sql = "CREATE TABLE if not exists "+"diskincre"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"diskincrehour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"diskincreday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String diskincre = "delete from diskincre" + CommonUtil.doip(ipaddress) + where;
                    String diskincrehour = "delete from diskincrehour" + CommonUtil.doip(ipaddress) + where;
                    String diskincreday = "delete from diskincreday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(diskincre);
                    util2.executeUpdate(diskincre);
                    System.out.println(diskincrehour);
                    util2.executeUpdate(diskincrehour);
                    System.out.println(diskincreday);
                    util2.executeUpdate(diskincreday);


                    sql = "CREATE TABLE if not exists "+"pgused"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"pgusedhour"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    sql = "CREATE TABLE if not exists "+"pgusedday"+CommonUtil.doip(ipaddress)
                    + "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
                    + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
                    + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
                    util2.executeUpdate(sql);
                    
                    
                    String pgused = "delete from pgused" + CommonUtil.doip(ipaddress) + where;
                    String pgusedhour = "delete from pgusedhour" + CommonUtil.doip(ipaddress) + where;
                    String pgusedday = "delete from pgusedday" + CommonUtil.doip(ipaddress) + where;
                    System.out.println(pgused);
                    util2.executeUpdate(pgused);
                    System.out.println(pgusedhour);
                    util2.executeUpdate(pgusedhour);
                    System.out.println(pgusedday);
                    util2.executeUpdate(pgusedday);
                    
//                String ping = "optimize table ping" + CommonUtil.doip(ipaddress);
//                String pinghour = "optimize table pinghour" + CommonUtil.doip(ipaddress);
//                String pingday = "optimize table pingday" + CommonUtil.doip(ipaddress);
//                System.out.println(pinghour);
//                util2.executeUpdate(pinghour);
//                System.out.println(pingday);
//                util2.executeUpdate(pingday);
//                
//                
//                String cpu = "optimize table cpu" + CommonUtil.doip(ipaddress);
//                String cpuhour = "optimize table cpuhour" + CommonUtil.doip(ipaddress);
//                String cpuday = "optimize table cpuday" + CommonUtil.doip(ipaddress);
//                System.out.println(cpuhour);
//                util2.executeUpdate(cpuhour);
//                System.out.println(cpuday);
//                util2.executeUpdate(cpuday);
//
//                
//                String memory = "optimize table memory" + CommonUtil.doip(ipaddress);
//                String memoryhour = "optimize table memoryhour" + CommonUtil.doip(ipaddress);
//                String memoryday = "optimize table memoryday" + CommonUtil.doip(ipaddress);
//                System.out.println(memoryhour);
//                util2.executeUpdate(memoryhour);
//                System.out.println(memoryday);
//                util2.executeUpdate(memoryday);
//
//
//                String flash = "optimize table flash" + CommonUtil.doip(ipaddress);
//                String flashhour = "optimize table flashhour" + CommonUtil.doip(ipaddress);
//                String flashday = "optimize table flashday" + CommonUtil.doip(ipaddress);
//                System.out.println(flashhour);
//                util2.executeUpdate(flashhour);
//                System.out.println(flashday);
//                util2.executeUpdate(flashday);
//
//
//                String buffer = "optimize table buffer" + CommonUtil.doip(ipaddress);
//                String bufferhour = "optimize table bufferhour" + CommonUtil.doip(ipaddress);
//                String bufferday = "optimize table bufferday" + CommonUtil.doip(ipaddress);
//                System.out.println(bufferhour);
//                util2.executeUpdate(bufferhour);
//                System.out.println(bufferday);
//                util2.executeUpdate(bufferday);
//   
//
//                String temper = "optimize table temper" + CommonUtil.doip(ipaddress);
//                String temperhour = "optimize table temperhour" + CommonUtil.doip(ipaddress);
//                String temperday = "optimize table temperday" + CommonUtil.doip(ipaddress);
//                System.out.println(temperhour);
//                util2.executeUpdate(temperhour);
//                System.out.println(temperday);
//                util2.executeUpdate(temperday);
//
//
//                String fan = "optimize table fan" + CommonUtil.doip(ipaddress);
//                String fanhour = "optimize table fanhour" + CommonUtil.doip(ipaddress);
//                String fanday = "optimize table fanday" + CommonUtil.doip(ipaddress);
//                System.out.println(fanhour);
//                util2.executeUpdate(fanhour);
//                System.out.println(fanday);
//                util2.executeUpdate(fanday);
//
//
//                String power = "optimize table power" + CommonUtil.doip(ipaddress);
//                String powerhour = "optimize table powerhour" + CommonUtil.doip(ipaddress);
//                String powerday = "optimize table powerday" + CommonUtil.doip(ipaddress);
//                System.out.println(powerhour);
//                util2.executeUpdate(powerhour);
//                System.out.println(powerday);
//                util2.executeUpdate(powerday);
//
//
//                String vol = "optimize table vol" + CommonUtil.doip(ipaddress);
//                String volhour = "optimize table volhour" + CommonUtil.doip(ipaddress);
//                String volday = "optimize table volday" + CommonUtil.doip(ipaddress);
//                System.out.println(volhour);
//                util2.executeUpdate(volhour);
//                System.out.println(volday);
//                util2.executeUpdate(volday);
//
//
//                String utilhdx = "optimize table utilhdx" + CommonUtil.doip(ipaddress);
//                String utilhdxhour = "optimize table utilhdxhour" + CommonUtil.doip(ipaddress);
//                String utilhdxday = "optimize table utilhdxday" + CommonUtil.doip(ipaddress);
//                System.out.println(utilhdxhour);
//                util2.executeUpdate(utilhdxhour);
//                System.out.println(utilhdxday);
//                util2.executeUpdate(utilhdxday);
//
//
//                String allutilhdx = "optimize table allutilhdx" + CommonUtil.doip(ipaddress);
//                String allutilhdxhour = "optimize table allutilhdxhour" + CommonUtil.doip(ipaddress);
//                String allutilhdxday = "optimize table allutilhdxday" + CommonUtil.doip(ipaddress);
//                System.out.println(allutilhdxhour);
//                util2.executeUpdate(allutilhdxhour);
//                System.out.println(allutilhdxday);
//                util2.executeUpdate(allutilhdxday);
//
//
//                String utilhdxperc = "optimize table utilhdxperc" + CommonUtil.doip(ipaddress);
//                String hdxperchour = "optimize table hdxperchour" + CommonUtil.doip(ipaddress);
//                String hdxpercday = "optimize table hdxpercday" + CommonUtil.doip(ipaddress);
//                System.out.println(hdxperchour);
//                util2.executeUpdate(hdxperchour);
//                System.out.println(hdxpercday);
//                util2.executeUpdate(hdxpercday);
//
//
//                String discardsperc = "optimize table discardsperc" + CommonUtil.doip(ipaddress);
//                String dcardperchour = "optimize table dcardperchour" + CommonUtil.doip(ipaddress);
//                String dcardpercday = "optimize table dcardpercday" + CommonUtil.doip(ipaddress);
//                System.out.println(dcardperchour);
//                util2.executeUpdate(dcardperchour);
//                System.out.println(dcardpercday);
//                util2.executeUpdate(dcardpercday);
//
//
//                String errorsperc = "optimize table errorsperc" + CommonUtil.doip(ipaddress);
//                String errperchour = "optimize table errperchour" + CommonUtil.doip(ipaddress);
//                String errpercday = "optimize table errpercday" + CommonUtil.doip(ipaddress);
//                System.out.println(errperchour);
//                util2.executeUpdate(errperchour);
//                System.out.println(errpercday);
//                util2.executeUpdate(errpercday);
//
//
//                String packs = "optimize table packs" + CommonUtil.doip(ipaddress);
//                String packshour = "optimize table packshour" + CommonUtil.doip(ipaddress);
//                String packsday = "optimize table packsday" + CommonUtil.doip(ipaddress);
//                System.out.println(packshour);
//                util2.executeUpdate(packshour);
//                System.out.println(packsday);
//                util2.executeUpdate(packsday);
//
//
//                String inpacks = "optimize table inpacks" + CommonUtil.doip(ipaddress);
//                String inpackshour = "optimize table inpackshour" + CommonUtil.doip(ipaddress);
//                String inpacksday = "optimize table inpacksday" + CommonUtil.doip(ipaddress);
//                System.out.println(inpackshour);
//                util2.executeUpdate(inpackshour);
//                System.out.println(inpacksday);
//                util2.executeUpdate(inpacksday);
//
//
//                String outpacks = "optimize table outpacks" + CommonUtil.doip(ipaddress);
//                String outpackshour = "optimize table outpackshour" + CommonUtil.doip(ipaddress);
//                String outpacksday = "optimize table outpacksday" + CommonUtil.doip(ipaddress);
//                System.out.println(outpackshour);
//                util2.executeUpdate(outpackshour);
//                System.out.println(outpacksday);
//                util2.executeUpdate(outpacksday);
//
//
//                String pro = "optimize table pro" + CommonUtil.doip(ipaddress);
//                String prohour = "optimize table prohour" + CommonUtil.doip(ipaddress);
//                String proday = "optimize table proday" + CommonUtil.doip(ipaddress);
//                System.out.println(prohour);
//                util2.executeUpdate(prohour);
//                System.out.println(proday);
//                util2.executeUpdate(proday);
//
//
//                String cpudtl = "optimize table cpudtl" + CommonUtil.doip(ipaddress);
//                String cpudtlhour = "optimize table cpudtlhour" + CommonUtil.doip(ipaddress);
//                String cpudtlday = "optimize table cpudtlday" + CommonUtil.doip(ipaddress);
//                System.out.println(cpudtlhour);
//                util2.executeUpdate(cpudtlhour);
//                System.out.println(cpudtlday);
//                util2.executeUpdate(cpudtlday);
//
//
//                String disk = "optimize table disk" + CommonUtil.doip(ipaddress);
//                String diskhour = "optimize table diskhour" + CommonUtil.doip(ipaddress);
//                String diskday = "optimize table diskday" + CommonUtil.doip(ipaddress);
//                System.out.println(diskhour);
//                util2.executeUpdate(diskhour);
//                System.out.println(diskday);
//                util2.executeUpdate(diskday);
//
//
//                String diskincre = "optimize table diskincre" + CommonUtil.doip(ipaddress);
//                String diskincrehour = "optimize table diskincrehour" + CommonUtil.doip(ipaddress);
//                String diskincreday = "optimize table diskincreday" + CommonUtil.doip(ipaddress);
//                System.out.println(diskincrehour);
//                util2.executeUpdate(diskincrehour);
//                System.out.println(diskincreday);
//                util2.executeUpdate(diskincreday);
//
//
//                String pgused = "optimize table pgused" + CommonUtil.doip(ipaddress);
//                String pgusedhour = "optimize table pgusedhour" + CommonUtil.doip(ipaddress);
//                String pgusedday = "optimize table pgusedday" + CommonUtil.doip(ipaddress);
//                System.out.println(pgusedhour);
//                util2.executeUpdate(pgusedhour);
//                System.out.println(pgusedday);
//                util2.executeUpdate(pgusedday);
                    
//                String ping = "truncate ping" + CommonUtil.doip(ipaddress);
//                String pinghour = "truncate pinghour" + CommonUtil.doip(ipaddress);
//                String pingday = "truncate pingday" + CommonUtil.doip(ipaddress);
//                System.out.println(pinghour);
//                util2.executeUpdate(pinghour);
//                System.out.println(pingday);
//                util2.executeUpdate(pingday);
//                
//                
//                String cpu = "truncate cpu" + CommonUtil.doip(ipaddress);
//                String cpuhour = "truncate cpuhour" + CommonUtil.doip(ipaddress);
//                String cpuday = "truncate cpuday" + CommonUtil.doip(ipaddress);
//                System.out.println(cpuhour);
//                util2.executeUpdate(cpuhour);
//                System.out.println(cpuday);
//                util2.executeUpdate(cpuday);
//
//                
//                String memory = "truncate memory" + CommonUtil.doip(ipaddress);
//                String memoryhour = "truncate memoryhour" + CommonUtil.doip(ipaddress);
//                String memoryday = "truncate memoryday" + CommonUtil.doip(ipaddress);
//                System.out.println(memoryhour);
//                util2.executeUpdate(memoryhour);
//                System.out.println(memoryday);
//                util2.executeUpdate(memoryday);
//
//
//                String flash = "truncate flash" + CommonUtil.doip(ipaddress);
//                String flashhour = "truncate flashhour" + CommonUtil.doip(ipaddress);
//                String flashday = "truncate flashday" + CommonUtil.doip(ipaddress);
//                System.out.println(flashhour);
//                util2.executeUpdate(flashhour);
//                System.out.println(flashday);
//                util2.executeUpdate(flashday);
//
//
//                String buffer = "truncate buffer" + CommonUtil.doip(ipaddress);
//                String bufferhour = "truncate bufferhour" + CommonUtil.doip(ipaddress);
//                String bufferday = "truncate bufferday" + CommonUtil.doip(ipaddress);
//                System.out.println(bufferhour);
//                util2.executeUpdate(bufferhour);
//                System.out.println(bufferday);
//                util2.executeUpdate(bufferday);
//   
//
//                String temper = "truncate temper" + CommonUtil.doip(ipaddress);
//                String temperhour = "truncate temperhour" + CommonUtil.doip(ipaddress);
//                String temperday = "truncate temperday" + CommonUtil.doip(ipaddress);
//                System.out.println(temperhour);
//                util2.executeUpdate(temperhour);
//                System.out.println(temperday);
//                util2.executeUpdate(temperday);
//
//
//                String fan = "truncate fan" + CommonUtil.doip(ipaddress);
//                String fanhour = "truncate fanhour" + CommonUtil.doip(ipaddress);
//                String fanday = "truncate fanday" + CommonUtil.doip(ipaddress);
//                System.out.println(fanhour);
//                util2.executeUpdate(fanhour);
//                System.out.println(fanday);
//                util2.executeUpdate(fanday);
//
//
//                String power = "truncate power" + CommonUtil.doip(ipaddress);
//                String powerhour = "truncate powerhour" + CommonUtil.doip(ipaddress);
//                String powerday = "truncate powerday" + CommonUtil.doip(ipaddress);
//                System.out.println(powerhour);
//                util2.executeUpdate(powerhour);
//                System.out.println(powerday);
//                util2.executeUpdate(powerday);
//
//
//                String vol = "truncate vol" + CommonUtil.doip(ipaddress);
//                String volhour = "truncate volhour" + CommonUtil.doip(ipaddress);
//                String volday = "truncate volday" + CommonUtil.doip(ipaddress);
//                System.out.println(volhour);
//                util2.executeUpdate(volhour);
//                System.out.println(volday);
//                util2.executeUpdate(volday);
//
//
//                String utilhdx = "truncate utilhdx" + CommonUtil.doip(ipaddress);
//                String utilhdxhour = "truncate utilhdxhour" + CommonUtil.doip(ipaddress);
//                String utilhdxday = "truncate utilhdxday" + CommonUtil.doip(ipaddress);
//                System.out.println(utilhdxhour);
//                util2.executeUpdate(utilhdxhour);
//                System.out.println(utilhdxday);
//                util2.executeUpdate(utilhdxday);
//
//
//                String allutilhdx = "truncate allutilhdx" + CommonUtil.doip(ipaddress);
//                String allutilhdxhour = "truncate allutilhdxhour" + CommonUtil.doip(ipaddress);
//                String allutilhdxday = "truncate allutilhdxday" + CommonUtil.doip(ipaddress);
//                System.out.println(allutilhdxhour);
//                util2.executeUpdate(allutilhdxhour);
//                System.out.println(allutilhdxday);
//                util2.executeUpdate(allutilhdxday);
//
//
//                String utilhdxperc = "truncate utilhdxperc" + CommonUtil.doip(ipaddress);
//                String hdxperchour = "truncate hdxperchour" + CommonUtil.doip(ipaddress);
//                String hdxpercday = "truncate hdxpercday" + CommonUtil.doip(ipaddress);
//                System.out.println(hdxperchour);
//                util2.executeUpdate(hdxperchour);
//                System.out.println(hdxpercday);
//                util2.executeUpdate(hdxpercday);
//
//
//                String discardsperc = "truncate discardsperc" + CommonUtil.doip(ipaddress);
//                String dcardperchour = "truncate dcardperchour" + CommonUtil.doip(ipaddress);
//                String dcardpercday = "truncate dcardpercday" + CommonUtil.doip(ipaddress);
//                System.out.println(dcardperchour);
//                util2.executeUpdate(dcardperchour);
//                System.out.println(dcardpercday);
//                util2.executeUpdate(dcardpercday);
//
//
//                String errorsperc = "truncate errorsperc" + CommonUtil.doip(ipaddress);
//                String errperchour = "truncate errperchour" + CommonUtil.doip(ipaddress);
//                String errpercday = "truncate errpercday" + CommonUtil.doip(ipaddress);
//                System.out.println(errperchour);
//                util2.executeUpdate(errperchour);
//                System.out.println(errpercday);
//                util2.executeUpdate(errpercday);
//
//
//                String packs = "truncate packs" + CommonUtil.doip(ipaddress);
//                String packshour = "truncate packshour" + CommonUtil.doip(ipaddress);
//                String packsday = "truncate packsday" + CommonUtil.doip(ipaddress);
//                System.out.println(packshour);
//                util2.executeUpdate(packshour);
//                System.out.println(packsday);
//                util2.executeUpdate(packsday);
//
//
//                String inpacks = "truncate inpacks" + CommonUtil.doip(ipaddress);
//                String inpackshour = "truncate inpackshour" + CommonUtil.doip(ipaddress);
//                String inpacksday = "truncate inpacksday" + CommonUtil.doip(ipaddress);
//                System.out.println(inpackshour);
//                util2.executeUpdate(inpackshour);
//                System.out.println(inpacksday);
//                util2.executeUpdate(inpacksday);
//
//
//                String outpacks = "truncate outpacks" + CommonUtil.doip(ipaddress);
//                String outpackshour = "truncate outpackshour" + CommonUtil.doip(ipaddress);
//                String outpacksday = "truncate outpacksday" + CommonUtil.doip(ipaddress);
//                System.out.println(outpackshour);
//                util2.executeUpdate(outpackshour);
//                System.out.println(outpacksday);
//                util2.executeUpdate(outpacksday);
//
//
//                String pro = "truncate pro" + CommonUtil.doip(ipaddress);
//                String prohour = "truncate prohour" + CommonUtil.doip(ipaddress);
//                String proday = "truncate proday" + CommonUtil.doip(ipaddress);
//                System.out.println(prohour);
//                util2.executeUpdate(prohour);
//                System.out.println(proday);
//                util2.executeUpdate(proday);
//
//
//                String cpudtl = "truncate cpudtl" + CommonUtil.doip(ipaddress);
//                String cpudtlhour = "truncate cpudtlhour" + CommonUtil.doip(ipaddress);
//                String cpudtlday = "truncate cpudtlday" + CommonUtil.doip(ipaddress);
//                System.out.println(cpudtlhour);
//                util2.executeUpdate(cpudtlhour);
//                System.out.println(cpudtlday);
//                util2.executeUpdate(cpudtlday);
//
//
//                String disk = "truncate disk" + CommonUtil.doip(ipaddress);
//                String diskhour = "truncate diskhour" + CommonUtil.doip(ipaddress);
//                String diskday = "truncate diskday" + CommonUtil.doip(ipaddress);
//                System.out.println(diskhour);
//                util2.executeUpdate(diskhour);
//                System.out.println(diskday);
//                util2.executeUpdate(diskday);
//
//
//                String diskincre = "truncate diskincre" + CommonUtil.doip(ipaddress);
//                String diskincrehour = "truncate diskincrehour" + CommonUtil.doip(ipaddress);
//                String diskincreday = "truncate diskincreday" + CommonUtil.doip(ipaddress);
//                System.out.println(diskincrehour);
//                util2.executeUpdate(diskincrehour);
//                System.out.println(diskincreday);
//                util2.executeUpdate(diskincreday);
//
//
//                String pgused = "truncate pgused" + CommonUtil.doip(ipaddress);
//                String pgusedhour = "truncate pgusedhour" + CommonUtil.doip(ipaddress);
//                String pgusedday = "truncate pgusedday" + CommonUtil.doip(ipaddress);
//                System.out.println(pgusedhour);
//                util2.executeUpdate(pgusedhour);
//                System.out.println(pgusedday);
//                util2.executeUpdate(pgusedday);
//
//                
//                String ping = "drop table if exists ping" + CommonUtil.doip(ipaddress);
//                String pinghour = "drop table if exists pinghour" + CommonUtil.doip(ipaddress);
//                String pingday = "drop table if exists pingday" + CommonUtil.doip(ipaddress);
//                System.out.println(pinghour);
//                util2.executeUpdate(pinghour);
//                System.out.println(pingday);
//                util2.executeUpdate(pingday);
//                
//                sql = "CREATE TABLE "+"pinghour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"pingday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                System.out.println(sql);
//                
//                String cpu = "drop table if exists cpu" + CommonUtil.doip(ipaddress);
//                String cpuhour = "drop table if exists cpuhour" + CommonUtil.doip(ipaddress);
//                String cpuday = "drop table if exists cpuday" + CommonUtil.doip(ipaddress);
//                System.out.println(cpuhour);
//                util2.executeUpdate(cpuhour);
//                System.out.println(cpuday);
//                util2.executeUpdate(cpuday);
//
//                sql = "CREATE TABLE "+"cpuhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"cpuday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                System.out.println(sql);
//                
//                String memory = "drop table if exists memory" + CommonUtil.doip(ipaddress);
//                String memoryhour = "drop table if exists memoryhour" + CommonUtil.doip(ipaddress);
//                String memoryday = "drop table if exists memoryday" + CommonUtil.doip(ipaddress);
//                System.out.println(memoryhour);
//                util2.executeUpdate(memoryhour);
//                System.out.println(memoryday);
//                util2.executeUpdate(memoryday);
//
//                sql = "CREATE TABLE "+"memoryhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"memoryday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                System.out.println(sql);
//
//                String flash = "drop table if exists flash" + CommonUtil.doip(ipaddress);
//                String flashhour = "drop table if exists flashhour" + CommonUtil.doip(ipaddress);
//                String flashday = "drop table if exists flashday" + CommonUtil.doip(ipaddress);
//                System.out.println(flashhour);
//                util2.executeUpdate(flashhour);
//                System.out.println(flashday);
//                util2.executeUpdate(flashday);
//
//                sql = "CREATE TABLE "+"flashhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"flashday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                System.out.println(sql);
//
//                String buffer = "drop table if exists buffer" + CommonUtil.doip(ipaddress);
//                String bufferhour = "drop table if exists bufferhour" + CommonUtil.doip(ipaddress);
//                String bufferday = "drop table if exists bufferday" + CommonUtil.doip(ipaddress);
//                System.out.println(bufferhour);
//                util2.executeUpdate(bufferhour);
//                System.out.println(bufferday);
//                util2.executeUpdate(bufferday);
//   
//                sql = "CREATE TABLE "+"bufferhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"bufferday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                System.out.println(sql);
//
//                String temper = "drop table if exists temper" + CommonUtil.doip(ipaddress);
//                String temperhour = "drop table if exists temperhour" + CommonUtil.doip(ipaddress);
//                String temperday = "drop table if exists temperday" + CommonUtil.doip(ipaddress);
//                System.out.println(temperhour);
//                util2.executeUpdate(temperhour);
//                System.out.println(temperday);
//                util2.executeUpdate(temperday);
//
//                sql = "CREATE TABLE "+"temperhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"temperday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String fan = "drop table if exists fan" + CommonUtil.doip(ipaddress);
//                String fanhour = "drop table if exists fanhour" + CommonUtil.doip(ipaddress);
//                String fanday = "drop table if exists fanday" + CommonUtil.doip(ipaddress);
//                System.out.println(fanhour);
//                util2.executeUpdate(fanhour);
//                System.out.println(fanday);
//                util2.executeUpdate(fanday);
//
//                sql = "CREATE TABLE "+"fanhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"fanday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String power = "drop table if exists power" + CommonUtil.doip(ipaddress);
//                String powerhour = "drop table if exists powerhour" + CommonUtil.doip(ipaddress);
//                String powerday = "drop table if exists powerday" + CommonUtil.doip(ipaddress);
//                System.out.println(powerhour);
//                util2.executeUpdate(powerhour);
//                System.out.println(powerday);
//                util2.executeUpdate(powerday);
//
//                sql = "CREATE TABLE "+"powerhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"powerday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String vol = "drop table if exists vol" + CommonUtil.doip(ipaddress);
//                String volhour = "drop table if exists volhour" + CommonUtil.doip(ipaddress);
//                String volday = "drop table if exists volday" + CommonUtil.doip(ipaddress);
//                System.out.println(volhour);
//                util2.executeUpdate(volhour);
//                System.out.println(volday);
//                util2.executeUpdate(volday);
//
//                sql = "CREATE TABLE "+"volhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"volday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String utilhdx = "drop table if exists utilhdx" + CommonUtil.doip(ipaddress);
//                String utilhdxhour = "drop table if exists utilhdxhour" + CommonUtil.doip(ipaddress);
//                String utilhdxday = "drop table if exists utilhdxday" + CommonUtil.doip(ipaddress);
//                System.out.println(utilhdxhour);
//                util2.executeUpdate(utilhdxhour);
//                System.out.println(utilhdxday);
//                util2.executeUpdate(utilhdxday);
//
//                sql = "CREATE TABLE "+"utilhdxhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"utilhdxday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String allutilhdx = "drop table if exists allutilhdx" + CommonUtil.doip(ipaddress);
//                String allutilhdxhour = "drop table if exists allutilhdxhour" + CommonUtil.doip(ipaddress);
//                String allutilhdxday = "drop table if exists allutilhdxday" + CommonUtil.doip(ipaddress);
//                System.out.println(allutilhdxhour);
//                util2.executeUpdate(allutilhdxhour);
//                System.out.println(allutilhdxday);
//                util2.executeUpdate(allutilhdxday);
//
//                sql = "CREATE TABLE "+"allutilhdxhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"allutilhdxday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String utilhdxperc = "drop table if exists utilhdxperc" + CommonUtil.doip(ipaddress);
//                String utilhdxperchour = "drop table if exists utilhdxperchour" + CommonUtil.doip(ipaddress);
//                String utilhdxpercday = "drop table if exists utilhdxpercday" + CommonUtil.doip(ipaddress);
//                System.out.println(utilhdxperchour);
//                util2.executeUpdate(utilhdxperchour);
//                System.out.println(utilhdxpercday);
//                util2.executeUpdate(utilhdxpercday);
//
//                sql = "CREATE TABLE "+"utilhdxperchour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"utilhdxpercday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String discardsperc = "drop table if exists discardsperc" + CommonUtil.doip(ipaddress);
//                String discardsperchour = "drop table if exists discardsperchour" + CommonUtil.doip(ipaddress);
//                String discardspercday = "drop table if exists discardspercday" + CommonUtil.doip(ipaddress);
//                System.out.println(discardsperchour);
//                util2.executeUpdate(discardsperchour);
//                System.out.println(discardspercday);
//                util2.executeUpdate(discardspercday);
//
//                sql = "CREATE TABLE "+"discardsperchour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"discardspercday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String errorsperc = "drop table if exists errorsperc" + CommonUtil.doip(ipaddress);
//                String errorsperchour = "drop table if exists errorsperchour" + CommonUtil.doip(ipaddress);
//                String errorspercday = "drop table if exists errorspercday" + CommonUtil.doip(ipaddress);
//                System.out.println(errorsperchour);
//                util2.executeUpdate(errorsperchour);
//                System.out.println(errorspercday);
//                util2.executeUpdate(errorspercday);
//
//                sql = "CREATE TABLE "+"errorsperchour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"errorspercday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String packs = "drop table if exists packs" + CommonUtil.doip(ipaddress);
//                String packshour = "drop table if exists packshour" + CommonUtil.doip(ipaddress);
//                String packsday = "drop table if exists packsday" + CommonUtil.doip(ipaddress);
//                System.out.println(packshour);
//                util2.executeUpdate(packshour);
//                System.out.println(packsday);
//                util2.executeUpdate(packsday);
//
//                sql = "CREATE TABLE "+"packshour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"packsday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String inpacks = "drop table if exists inpacks" + CommonUtil.doip(ipaddress);
//                String inpackshour = "drop table if exists inpackshour" + CommonUtil.doip(ipaddress);
//                String inpacksday = "drop table if exists inpacksday" + CommonUtil.doip(ipaddress);
//                System.out.println(inpackshour);
//                util2.executeUpdate(inpackshour);
//                System.out.println(inpacksday);
//                util2.executeUpdate(inpacksday);
//
//                sql = "CREATE TABLE "+"inpackshour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"inpacksday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String outpacks = "drop table if exists outpacks" + CommonUtil.doip(ipaddress);
//                String outpackshour = "drop table if exists outpackshour" + CommonUtil.doip(ipaddress);
//                String outpacksday = "drop table if exists outpacksday" + CommonUtil.doip(ipaddress);
//                System.out.println(outpackshour);
//                util2.executeUpdate(outpackshour);
//                System.out.println(outpacksday);
//                util2.executeUpdate(outpacksday);
//
//                sql = "CREATE TABLE "+"outpackshour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"outpacksday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String pro = "drop table if exists pro" + CommonUtil.doip(ipaddress);
//                String prohour = "drop table if exists prohour" + CommonUtil.doip(ipaddress);
//                String proday = "drop table if exists proday" + CommonUtil.doip(ipaddress);
//                System.out.println(prohour);
//                util2.executeUpdate(prohour);
//                System.out.println(proday);
//                util2.executeUpdate(proday);
//
//                sql = "CREATE TABLE "+"prohour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"proday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String cpudtl = "drop table if exists cpudtl" + CommonUtil.doip(ipaddress);
//                String cpudtlhour = "drop table if exists cpudtlhour" + CommonUtil.doip(ipaddress);
//                String cpudtlday = "drop table if exists cpudtlday" + CommonUtil.doip(ipaddress);
//                System.out.println(cpudtlhour);
//                util2.executeUpdate(cpudtlhour);
//                System.out.println(cpudtlday);
//                util2.executeUpdate(cpudtlday);
//
//                sql = "CREATE TABLE "+"cpudtlhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"cpudtlday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String disk = "drop table if exists disk" + CommonUtil.doip(ipaddress);
//                String diskhour = "drop table if exists diskhour" + CommonUtil.doip(ipaddress);
//                String diskday = "drop table if exists diskday" + CommonUtil.doip(ipaddress);
//                System.out.println(diskhour);
//                util2.executeUpdate(diskhour);
//                System.out.println(diskday);
//                util2.executeUpdate(diskday);
//
//                sql = "CREATE TABLE "+"diskhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"diskday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String diskincre = "drop table if exists diskincre" + CommonUtil.doip(ipaddress);
//                String diskincrehour = "drop table if exists diskincrehour" + CommonUtil.doip(ipaddress);
//                String diskincreday = "drop table if exists diskincreday" + CommonUtil.doip(ipaddress);
//                System.out.println(diskincrehour);
//                util2.executeUpdate(diskincrehour);
//                System.out.println(diskincreday);
//                util2.executeUpdate(diskincreday);
//
//                sql = "CREATE TABLE "+"diskincrehour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"diskincreday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//
//                String pgused = "drop table if exists pgused" + CommonUtil.doip(ipaddress);
//                String pgusedhour = "drop table if exists pgusedhour" + CommonUtil.doip(ipaddress);
//                String pgusedday = "drop table if exists pgusedday" + CommonUtil.doip(ipaddress);
//                System.out.println(pgusedhour);
//                util2.executeUpdate(pgusedhour);
//                System.out.println(pgusedday);
//                util2.executeUpdate(pgusedday);
//
//                sql = "CREATE TABLE "+"pgusedhour"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                System.out.println(sql);
//                util2.executeUpdate(sql);
//                sql = "CREATE TABLE "+"pgusedday"+CommonUtil.doip(ipaddress)
//                +"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
//                +"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
//                +" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
//                util2.executeUpdate(sql);
//                
//                
//                conn.commit();
                    System.out.println("ÒÑÍê³É£º" + ipaddress);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        if (util2 != null) {
                            util2.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            
            }
            
        });
        thread.start();
    }
    
    public void alertAllTable() {
        String ip = "192.168.201.4";
        String username = "root";
        String password = "root";
        String db = "newafunms";
        String dburl = "jdbc:mysql://" + ip + "/" + db + "?user=" + username + "&password=" + password
                + "&useUnicode=true&&characterEncoding=gbk&autoReconnect = true";
        String sql = "select * from topo_host_node";
        ResultSet rs = null;
        String strDriver = "com.mysql.jdbc.Driver";
        
        Connection conn = null;
        try {
            Class.forName(strDriver).newInstance();
            conn = DriverManager.getConnection(dburl, username, password);
            rs = conn.createStatement().executeQuery(sql);
            String time = "2012-03-01 00:00:00";
            String where = " where collecttime<='" + time + "'";
            while (rs.next()) {
                Connection conn2 = DriverManager.getConnection(dburl, username, password);
                String ipaddress = rs.getString("ip_address");
                alert(ipaddress, where, conn2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
