/*
SQLyog - Free MySQL GUI v5.16 RC1
Host - 5.0.41-community-nt : Database - afunms
*********************************************************************
Server version : 5.0.41-community-nt
*/


SET NAMES utf8;

SET SQL_MODE='';

create database if not exists `afunms`;

USE `afunms`;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';

/*Table structure for table `app_db_node` */

DROP TABLE IF EXISTS `app_db_node`;

CREATE TABLE `app_db_node` (
  `id` int(11) NOT NULL,
  `alias` varchar(30) default NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(11) default NULL,
  `category` tinyint(2) default NULL,
  `db_name` varchar(50) default NULL,
  `port` varchar(5) default NULL,
  `user` varchar(100) default NULL,
  `password` varchar(100) default NULL,
  `dbuse` varchar(100) default NULL,
  `sendmobiles` varchar(200) default NULL,
  `sendemail` varchar(200) default NULL,
  `managed` int(2) default NULL,
  `bid` varchar(100) default NULL,
  `dbtype` int(11) default NULL,
  `sendphone` varchar(200) default NULL,
  `collecttype` int(2) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `app_db_node` */

/*Table structure for table `app_dbtype` */

DROP TABLE IF EXISTS `app_dbtype`;

CREATE TABLE `app_dbtype` (
  `id` bigint(20) NOT NULL auto_increment,
  `dbtype` varchar(50) default NULL,
  `dbdesc` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `app_dbtype` */

insert into `app_dbtype` (`id`,`dbtype`,`dbdesc`) values (1,'Oracle','Oracle');
insert into `app_dbtype` (`id`,`dbtype`,`dbdesc`) values (2,'SQLServer','SQL Server');
insert into `app_dbtype` (`id`,`dbtype`,`dbdesc`) values (4,'MySql','MySql');
insert into `app_dbtype` (`id`,`dbtype`,`dbdesc`) values (5,'DB2','DB2');
insert into `app_dbtype` (`id`,`dbtype`,`dbdesc`) values (6,'Sybase','Sybase');
insert into `app_dbtype` (`id`,`dbtype`,`dbdesc`) values (7,'Informix','Informix');

/*Table structure for table `app_ip_node` */

DROP TABLE IF EXISTS `app_ip_node`;

CREATE TABLE `app_ip_node` (
  `id` int(5) NOT NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(10) default NULL,
  `alias` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `app_ip_node` */

/*Table structure for table `app_tomcat_node` */

DROP TABLE IF EXISTS `app_tomcat_node`;

CREATE TABLE `app_tomcat_node` (
  `id` int(5) NOT NULL,
  `alias` varchar(30) default NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(10) default NULL,
  `port` varchar(5) default NULL,
  `user` varchar(20) default NULL,
  `password` varchar(20) default NULL,
  `monflag` int(2) default NULL,
  `bid` varchar(50) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `version` varchar(100) default NULL,
  `jvmversion` varchar(50) default NULL,
  `jvmvender` varchar(200) default NULL,
  `os` varchar(100) default NULL,
  `osversion` varchar(50) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `app_tomcat_node` */

/*Table structure for table `app_ups_node` */

DROP TABLE IF EXISTS `app_ups_node`;

CREATE TABLE `app_ups_node` (
  `id` int(5) NOT NULL,
  `alias` varchar(30) default NULL,
  `location` varchar(30) default NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(10) default NULL,
  `community` varchar(30) default NULL,
  `sys_name` varchar(30) default NULL,
  `sys_descr` varchar(50) default NULL,
  `sys_oid` varchar(30) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `app_ups_node` */

insert into `app_ups_node` (`id`,`alias`,`location`,`ip_address`,`ip_long`,`community`,`sys_name`,`sys_descr`,`sys_oid`) values (204,'梅兰日兰UPS','一楼机房','10.110.1.126',174981502,'public','','Galaxy PW Single//','1.3.6.1.4.1.705.1.2');

/*Table structure for table `ip_resource` */

DROP TABLE IF EXISTS `ip_resource`;

CREATE TABLE `ip_resource` (
  `id` bigint(10) NOT NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(10) default NULL,
  `mac` varchar(17) default NULL,
  `if_index` varchar(10) default NULL,
  `if_descr` varchar(50) default NULL,
  `node` varchar(30) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `ip_resource` */

/*Table structure for table `ipmac` */

DROP TABLE IF EXISTS `ipmac`;

CREATE TABLE `ipmac` (
  `ID` bigint(11) NOT NULL auto_increment,
  `RELATEIPADDR` varchar(30) default NULL,
  `IFINDEX` varchar(30) default NULL,
  `IPADDRESS` varchar(30) default NULL,
  `MAC` varchar(20) default NULL,
  `IFBAND` varchar(2) default NULL,
  `IFSMS` varchar(2) default NULL,
  `COLLECTTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `BAK` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `ipmac` */

/*Table structure for table `ipmacband` */

DROP TABLE IF EXISTS `ipmacband`;

CREATE TABLE `ipmacband` (
  `ID` bigint(11) NOT NULL auto_increment,
  `RELATEIPADDR` varchar(30) default NULL,
  `IFINDEX` varchar(30) default NULL,
  `IPADDRESS` varchar(30) default NULL,
  `MAC` varchar(20) default NULL,
  `IFBAND` varchar(2) default NULL,
  `IFSMS` varchar(2) default NULL,
  `COLLECTTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `BAK` varchar(100) default NULL,
  `EMPLOYEE_ID` bigint(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `ipmacband` */

/*Table structure for table `ipmacchange` */

DROP TABLE IF EXISTS `ipmacchange`;

CREATE TABLE `ipmacchange` (
  `ID` bigint(11) NOT NULL auto_increment,
  `IPADDRESS` varchar(30) default NULL,
  `MAC` varchar(20) default NULL,
  `CHANGETYPE` varchar(2) default NULL,
  `DETAIL` varchar(200) default NULL,
  `COLLECTTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `BAK` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `ipmacchange` */

/*Table structure for table `iprouter` */

DROP TABLE IF EXISTS `iprouter`;

CREATE TABLE `iprouter` (
  `ID` bigint(11) NOT NULL auto_increment,
  `RELATEIPADDR` varchar(30) default NULL,
  `IFINDEX` varchar(30) default NULL,
  `NEXTHOP` varchar(30) default NULL,
  `TYPE` bigint(10) default NULL,
  `PROTO` bigint(10) default NULL,
  `MASK` varchar(30) default NULL,
  `PHYSADDRESS` varchar(100) default NULL,
  `DEST` varchar(30) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `iprouter` */

/*Table structure for table `nms_alarm_indicators` */

DROP TABLE IF EXISTS `nms_alarm_indicators`;

CREATE TABLE `nms_alarm_indicators` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `subtype` varchar(255) default NULL,
  `datatype` varchar(255) default NULL,
  `moid` varchar(100) default NULL,
  `threshold` varchar(255) default NULL,
  `threshold_unit` varchar(10) default NULL,
  `compare` varchar(10) default '1',
  `compare_type` varchar(10) default '1',
  `alarm_times` varchar(10) default '1',
  `alarm_info` varchar(100) default '',
  `alarm_level` varchar(10) default '1',
  `enabled` varchar(10) default '1',
  `poll_interval` varchar(10) default NULL,
  `interval_unit` varchar(10) default NULL,
  `subentity` varchar(50) default NULL,
  `limenvalue0` bigint(20) default NULL,
  `limenvalue1` bigint(20) default NULL,
  `limenvalue2` bigint(20) default NULL,
  `time0` int(3) default NULL,
  `time1` int(3) default NULL,
  `time2` int(3) default NULL,
  `sms0` int(2) default NULL,
  `sms1` int(2) default NULL,
  `sms2` int(2) default NULL,
  `category` varchar(50) default NULL,
  `descr` varchar(50) default NULL,
  `unit` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_alarm_indicators` */

insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (2,'ping','db','oracle','Number','null','1','%','0','1','null','连通率超过阀值','null','1','null','null','null',50,80,90,3,3,3,1,1,1,'null','连通率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (3,'tablespace','db','oracle','Number','null','1','%','1','1','null','表空间超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','表空间','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (4,'buffercache','db','oracle','Number','null','1','%','1','1','null','缓冲区命中率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','缓冲区命中率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (5,'dictionarycache','db','oracle','Number','null','1','%','1','1','null','数据字典命中率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','数据字典命中率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (6,'pctmemorysorts','db','oracle','Number','null','1','%','1','1','null','内存中的排序超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,0,1,'null','内存中的排序','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (7,'pctbufgets','db','oracle','Number','null','1','%','1','1','null','最浪费内存的前10个语句占全部内存读取量的比例超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','最浪费内存的前10个语句占全部内存读取量的比例','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (8,'tablespaceinc','db','oracle','Number','null','1','%','1','1','null','表空间增长率超过阀值','null','1','null','null','null',10,20,30,3,3,3,1,1,1,'null','表空间增长率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (9,'opencursor','db','oracle','Number','null','1','个','1','1','null','打开的游标数超过阀值','null','1','null','null','null',80,90,100,3,3,3,1,1,1,'null','打开的游标数','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (10,'cpu','host','windows','Number','1.3.6.1.4.1.311.1.1.3.1.1','1','%','1','1',NULL,'cpu利用率超过阀值',NULL,'1','5','m',NULL,80,90,95,3,3,3,1,1,1,NULL,'cpu利用率',NULL);
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (11,'diskperc','host','windows','Number','null','1','%','1','1','null','磁盘利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','磁盘','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (12,'service','host','windows','String','null','1','无','1','1','null','服务丢失','null','1','5','m','null',1,1,1,3,3,3,1,1,1,'null','服务','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (13,'physicalmemory','host','windows','Number','null','1','%','1','1','null','物理内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','物理内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (14,'virtualmemory','host','windows','Number','null','1','%','1','1','null','虚拟内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','虚拟内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (15,'process','host','windows','String','null','1','无','1','1','null','进程丢失','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','进程','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (16,'hardware','host','windows','String','null','1','无','1','1','null','硬件信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','硬件信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (17,'storage','host','windows','String','null','1','无','1','1','null','存储信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','存储信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (18,'ipmac','host','windows','String','null','1','无','1','1','null','IPMAC信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (19,'utilhdx','host','windows','Number','null','1','Kb','1','1','null','网卡流速超过阀值','null','1','5','m','null',12000,24000,36000,3,3,3,1,1,1,'null','网卡流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (20,'software','host','windows','String','null','1','无','1','1','null','安装的软件信息发生变更','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','安装的软件信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (21,'systemgroup','host','windows','String','null','1','无','1','1','null','系统组信息改变','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (22,'ping','host','windows','Number','null','1','%','0','1','null','ping不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (23,'cpu','net','cisco','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (24,'ping','net','cisco','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (25,'memory','net','cisco','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (26,'flash','net','cisco','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (27,'temperature','net','cisco','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (28,'fan','net','cisco','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (29,'power','net','cisco','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (30,'voltage','net','cisco','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (31,'systemgroup','net','cisco','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (32,'ipmac','net','cisco','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (33,'fdb','net','cisco','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (34,'router','net','cisco','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (35,'interface','net','cisco','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (36,'utilhdx','net','cisco','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (37,'packs','net','cisco','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (38,'discardsperc','net','cisco','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (39,'errorsperc','net','cisco','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (40,'inpacks','net','cisco','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (41,'outpacks','net','cisco','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (42,'cpu','net','h3c','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (43,'ping','net','h3c','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (44,'memory','net','h3c','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (45,'flash','net','h3c','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (46,'temperature','net','h3c','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (47,'fan','net','h3c','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (48,'power','net','h3c','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (49,'voltage','net','h3c','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (50,'systemgroup','net','h3c','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (51,'ipmac','net','h3c','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (52,'fdb','net','h3c','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (53,'router','net','h3c','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (54,'interface','net','h3c','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (55,'utilhdx','net','h3c','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (56,'packs','net','h3c','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (57,'discardsperc','net','h3c','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (58,'errorsperc','net','h3c','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (59,'inpacks','net','h3c','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (60,'outpacks','net','h3c','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (75,'cpu','host','linux','Number','1.3.6.1.4.1.311.1.1.3.1.1','1','%','1','1',NULL,'cpu利用率超过阀值',NULL,'1','5','m',NULL,80,90,95,3,3,3,1,1,1,NULL,'cpu利用率',NULL);
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (76,'diskperc','host','linux','Number','null','1','%','1','1','null','文件系统利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','文件系统信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (77,'service','host','linux','String','null','1','无','1','1','null','服务丢失','null','1','5','m','null',1,1,1,3,3,3,1,1,1,'null','服务','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (78,'physicalmemory','host','linux','Number','null','1','%','1','1','null','物理内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','物理内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (79,'virtualmemory','host','linux','Number','null','1','%','1','1','null','交换内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','交换内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (80,'process','host','linux','String','null','1','无','1','1','null','进程丢失','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','进程','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (81,'hardware','host','linux','String','null','1','无','1','1','null','硬件信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','硬件信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (82,'storage','host','linux','String','null','1','无','1','1','null','存储信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','存储信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (83,'ipmac','host','linux','String','null','1','无','1','1','null','IPMAC信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (84,'utilhdx','host','linux','Number','null','1','Kb','1','1','null','网卡流速超过阀值','null','1','5','m','null',12000,24000,36000,3,3,3,1,1,1,'null','网卡流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (85,'software','host','linux','String','null','1','无','1','1','null','安装的软件信息发生变更','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','安装的软件信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (86,'systemgroup','host','linux','String','null','1','无','1','1','null','系统组信息改变','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (87,'ping','host','linux','Number','null','1','%','0','1','null','ping不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (88,'ping','host','aix','Number','null','1','%','0','1','null','PING不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (89,'responsetime','host','aix','Number','null','1','ms','1','1','null','响应时间超过阀值','null','1','null','null','null',1000,2000,3000,3,3,3,1,1,1,'null','响应时间','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (90,'cpu','host','aix','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','CPU信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (91,'physicalmemory','host','aix','Number','null','1','%','1','1','null','物理内存利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','物理内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (92,'swapmemory','host','aix','Number','null','1','%','1','1','null','交换内存利用率超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','交换内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (93,'diskperc','host','aix','Number','null','1','%','1','1','null','文件系统利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','文件系统利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (94,'diskinc','host','aix','Number','null','1','%','1','1','null','磁盘增长率超过阀值','null','1','5','m','null',1,2,3,3,3,3,1,1,1,'null','磁盘增长率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (95,'diskbusy','host','aix','Number','null','1','%','1','1','null','磁盘繁忙程度超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','磁盘繁忙程度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (96,'interface','host','aix','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (97,'utilhdx','host','aix','Number','null','1','kb','1','1','null','端口流速超过阀值','null','1','5','m','null',12000,24000,36000,3,3,3,1,1,1,'null','端口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (98,'process','host','aix','String','null','1','无','1','1','null','进程异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','进程信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (99,'avque','host','aix','Number','null','1','个','1','1','null','I/0平均请求数超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','I/0平均请求数','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (100,'avwait','host','aix','Number','null','1','ms','1','1','null','I/O平均等待时间超过阀值','null','1','5','m','null',5,6,7,3,3,3,1,1,1,'null','I/O平均等待时间','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (101,'avserv','host','aix','Number','null','1','ms','1','1','null','I/O平均完成时间超过阀值','null','1','5','m','null',5,6,7,3,3,3,1,1,1,'null','I/O平均完成时间','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (102,'AllInBandwidthUtilHdx','net','cisco','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (103,'AllOutBandwidthUtilHdx','net','cisco','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (104,'AllInBandwidthUtilHdx','net','h3c','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (105,'AllOutBandwidthUtilHdx','net','h3c','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (106,'AllInBandwidthUtilHdx','host','windows','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',10000,20000,30000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (107,'AllOutBandwidthUtilHdx','host','windows','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','1','5','m','null',10000,20000,30000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (108,'AllInBandwidthUtilHdx','host','linux','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',10000,20000,30000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (109,'AllOutBandwidthUtilHdx','host','linux','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','0','5','m','null',10000,20000,30000,3,3,3,1,1,0,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (110,'AllInBandwidthUtilHdx','host','aix','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',10000,26000,30000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (111,'AllOutBandwidthUtilHdx','host','aix','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','1','5','m','null',10000,26000,30000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (112,'interface','host','windows','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (113,'cpu','net','nortel','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (114,'ping','net','nortel','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (115,'memory','net','nortel','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (116,'flash','net','nortel','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (117,'temperature','net','nortel','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (118,'fan','net','nortel','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (119,'power','net','nortel','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (120,'voltage','net','nortel','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (121,'systemgroup','net','nortel','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (122,'ipmac','net','nortel','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (123,'fdb','net','nortel','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (124,'router','net','nortel','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (125,'interface','net','nortel','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (126,'utilhdx','net','nortel','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (127,'packs','net','nortel','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (128,'discardsperc','net','nortel','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (129,'errorsperc','net','nortel','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (130,'inpacks','net','nortel','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (131,'outpacks','net','nortel','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (132,'cpu','net','maipu','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (133,'ping','net','maipu','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (134,'memory','net','maipu','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (135,'flash','net','maipu','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (136,'temperature','net','maipu','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (137,'fan','net','maipu','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (138,'power','net','maipu','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (139,'voltage','net','maipu','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (140,'systemgroup','net','maipu','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (141,'ipmac','net','maipu','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (142,'fdb','net','maipu','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (143,'router','net','maipu','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (144,'interface','net','maipu','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (145,'utilhdx','net','maipu','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (146,'packs','net','maipu','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (147,'discardsperc','net','maipu','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (148,'errorsperc','net','maipu','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (149,'inpacks','net','maipu','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (150,'outpacks','net','maipu','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (151,'cpu','net','radware','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (152,'ping','net','radware','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (153,'memory','net','radware','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (154,'flash','net','radware','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (155,'temperature','net','radware','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (156,'fan','net','radware','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (157,'power','net','radware','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (158,'voltage','net','radware','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (159,'systemgroup','net','radware','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (160,'ipmac','net','radware','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (161,'fdb','net','radware','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (162,'router','net','radware','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (163,'interface','net','radware','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (164,'utilhdx','net','radware','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (165,'packs','net','radware','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (166,'discardsperc','net','radware','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (167,'errorsperc','net','radware','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (168,'inpacks','net','radware','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (169,'outpacks','net','radware','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (170,'cpu','net','redgiant','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (171,'ping','net','redgiant','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (172,'memory','net','redgiant','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (173,'flash','net','redgiant','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (174,'temperature','net','redgiant','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (175,'fan','net','redgiant','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (176,'power','net','redgiant','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (177,'voltage','net','redgiant','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (178,'systemgroup','net','redgiant','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (179,'ipmac','net','redgiant','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (180,'fdb','net','redgiant','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (181,'router','net','redgiant','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (182,'interface','net','redgiant','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (183,'utilhdx','net','redgiant','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (184,'packs','net','redgiant','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (185,'discardsperc','net','redgiant','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (186,'errorsperc','net','redgiant','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (187,'inpacks','net','redgiant','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (188,'outpacks','net','redgiant','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (189,'cpu','net','zte','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (190,'ping','net','zte','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (191,'memory','net','zte','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (192,'flash','net','zte','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (193,'temperature','net','zte','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (194,'fan','net','zte','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (195,'power','net','zte','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (196,'voltage','net','zte','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (197,'systemgroup','net','zte','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (198,'ipmac','net','zte','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (199,'fdb','net','zte','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (200,'router','net','zte','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (201,'interface','net','zte','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (202,'utilhdx','net','zte','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (203,'packs','net','zte','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (204,'discardsperc','net','zte','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (205,'errorsperc','net','zte','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (206,'inpacks','net','zte','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (207,'outpacks','net','zte','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (208,'cpu','net','bdcom','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (209,'ping','net','bdcom','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (210,'memory','net','bdcom','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (211,'flash','net','bdcom','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (212,'temperature','net','bdcom','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (213,'fan','net','bdcom','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (214,'power','net','bdcom','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (215,'voltage','net','bdcom','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (216,'systemgroup','net','bdcom','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (217,'ipmac','net','bdcom','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (218,'fdb','net','bdcom','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (219,'router','net','bdcom','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (220,'interface','net','bdcom','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (221,'utilhdx','net','bdcom','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (222,'packs','net','bdcom','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (223,'discardsperc','net','bdcom','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (224,'errorsperc','net','bdcom','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (225,'inpacks','net','bdcom','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (226,'outpacks','net','bdcom','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (227,'cpu','net','digitalchina','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (228,'ping','net','digitalchina','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (229,'memory','net','digitalchina','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (230,'flash','net','digitalchina','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (231,'temperature','net','digitalchina','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (232,'fan','net','digitalchina','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (233,'power','net','digitalchina','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (234,'voltage','net','digitalchina','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (235,'systemgroup','net','digitalchina','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (236,'ipmac','net','digitalchina','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (237,'fdb','net','digitalchina','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (238,'router','net','digitalchina','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (239,'interface','net','digitalchina','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (240,'utilhdx','net','digitalchina','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (241,'packs','net','digitalchina','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (242,'discardsperc','net','digitalchina','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (243,'errorsperc','net','digitalchina','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (244,'inpacks','net','digitalchina','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (245,'outpacks','net','digitalchina','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (246,'cpu','net','dlink','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (247,'ping','net','dlink','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (248,'memory','net','dlink','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (249,'flash','net','dlink','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (250,'temperature','net','dlink','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (251,'fan','net','dlink','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (252,'power','net','dlink','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (253,'voltage','net','dlink','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (254,'systemgroup','net','dlink','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (255,'ipmac','net','dlink','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (256,'fdb','net','dlink','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (257,'router','net','dlink','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (258,'interface','net','dlink','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (259,'utilhdx','net','dlink','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (260,'packs','net','dlink','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (261,'discardsperc','net','dlink','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (262,'errorsperc','net','dlink','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (263,'inpacks','net','dlink','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (264,'outpacks','net','dlink','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (265,'cpu','net','enterasys','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (266,'ping','net','enterasys','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (267,'memory','net','enterasys','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (268,'flash','net','enterasys','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (269,'temperature','net','enterasys','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (270,'fan','net','enterasys','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (271,'power','net','enterasys','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (272,'voltage','net','enterasys','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (273,'systemgroup','net','enterasys','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (274,'ipmac','net','enterasys','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (275,'fdb','net','enterasys','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (276,'router','net','enterasys','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (277,'interface','net','enterasys','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (278,'utilhdx','net','enterasys','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (279,'packs','net','enterasys','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (280,'discardsperc','net','enterasys','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (281,'errorsperc','net','enterasys','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (282,'inpacks','net','enterasys','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (283,'outpacks','net','enterasys','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (284,'cpu','net','harbour','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (285,'ping','net','harbour','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (286,'memory','net','harbour','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (287,'flash','net','harbour','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (288,'temperature','net','harbour','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (289,'fan','net','harbour','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (290,'power','net','harbour','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (291,'voltage','net','harbour','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (292,'systemgroup','net','harbour','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (293,'ipmac','net','harbour','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (294,'fdb','net','harbour','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (295,'router','net','harbour','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (296,'interface','net','harbour','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (297,'utilhdx','net','harbour','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (298,'packs','net','harbour','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (299,'discardsperc','net','harbour','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (300,'errorsperc','net','harbour','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (301,'inpacks','net','harbour','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (302,'outpacks','net','harbour','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (303,'cpu','net','huawei','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (304,'ping','net','huawei','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (305,'memory','net','huawei','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (306,'flash','net','huawei','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (307,'temperature','net','huawei','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (308,'fan','net','huawei','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','风扇状态','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (309,'power','net','huawei','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电源模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (310,'voltage','net','huawei','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (311,'systemgroup','net','huawei','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (312,'ipmac','net','huawei','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (313,'fdb','net','huawei','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (314,'router','net','huawei','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (315,'interface','net','huawei','String','null','1','无','1','1','null','接口信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (316,'utilhdx','net','huawei','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (317,'packs','net','huawei','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (318,'discardsperc','net','huawei','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (319,'errorsperc','net','huawei','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (320,'inpacks','net','huawei','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (321,'outpacks','net','huawei','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (322,'AllInBandwidthUtilHdx','net','bdcom','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (323,'AllOutBandwidthUtilHdx','net','bdcom','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (324,'AllInBandwidthUtilHdx','net','digitalchina.java','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (325,'AllOutBandwidthUtilHdx','net','digitalchina','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (326,'AllInBandwidthUtilHdx','net','dlink','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (327,'AllOutBandwidthUtilHdx','net','dlink','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (328,'AllInBandwidthUtilHdx','net','enterasys','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (329,'AllOutBandwidthUtilHdx','net','enterasys','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (330,'AllInBandwidthUtilHdx','net','harbour','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (331,'AllOutBandwidthUtilHdx','net','harbour','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (332,'AllInBandwidthUtilHdx','net','huawei','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (333,'AllOutBandwidthUtilHdx','net','huawei','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (334,'AllInBandwidthUtilHdx','net','maipu','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (335,'AllOutBandwidthUtilHdx','net','maipu','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (336,'AllInBandwidthUtilHdx','net','nortel','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (337,'AllOutBandwidthUtilHdx','net','nortel','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (338,'AllInBandwidthUtilHdx','net','radware','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (339,'AllOutBandwidthUtilHdx','net','radware','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (340,'AllInBandwidthUtilHdx','net','redgiant','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (341,'AllOutBandwidthUtilHdx','net','redgiant','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (342,'AllInBandwidthUtilHdx','net','zte','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (343,'AllOutBandwidthUtilHdx','net','zte','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (344,'ping','db','sqlserver','Number','null','1','%','0','1','null','连通率低于阀值','null','1','null','null','null',30,20,10,3,3,1,1,1,1,'null','连通率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (345,'jobnumber','host','as400','Number','null','1','个','1','1','null','作业数量超过阀值','null','1','null','null','null',100,200,300,3,3,3,1,1,1,'null','作业数量','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (346,'ping','host','as400','Number','null','1','%','0','1','null','PING不通','null','1','null','null','null',90,80,70,3,3,3,1,1,1,'null','可用性','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (347,'cpu','host','as400','Number','null','1','%','1','1','null','CUP利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','CUP利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (348,'diskperc','host','as400','Number','null','1','%','1','1','null','磁盘利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','磁盘利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (349,'ping','host','solaris','Number','null','1','%','0','1','null','PING不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','连通率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (350,'cpu','host','solaris','Number','null','1','%','1','1','null','CPU超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','CPU利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (351,'physicalmemory','host','solaris','Number','null','1','%','1','1','null','物理内存利用率超过阀值','null','1','null','null','null',80,90,95,3,3,3,1,1,1,'null','物理内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (352,'swapmemory','host','solaris','Number','null','1','%','1','1','null','交换内存利用率超过阀值','null','1','null','null','null',80,90,90,3,3,3,1,1,1,'null','交换内存','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (353,'diskperc','host','solaris','Number','null','1','%','1','1','null','磁盘利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','磁盘利用率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (354,'responsetime','host','solaris','Number','null','1','ms','1','1','null','响应时间超过阀值','null','1','null','null','null',1000,2000,3000,3,3,3,1,1,1,'null','响应时间','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (355,'pagingusage','host','aix','Number','null','1','%','1','1','null','换页率超过阀值','null','1','null','null','null',60,70,80,3,3,3,1,1,1,'null','换页率','null');
insert into `nms_alarm_indicators` (`id`,`name`,`type`,`subtype`,`datatype`,`moid`,`threshold`,`threshold_unit`,`compare`,`compare_type`,`alarm_times`,`alarm_info`,`alarm_level`,`enabled`,`poll_interval`,`interval_unit`,`subentity`,`limenvalue0`,`limenvalue1`,`limenvalue2`,`time0`,`time1`,`time2`,`sms0`,`sms1`,`sms2`,`category`,`descr`,`unit`) values (356,'ping','middleware','was','Number','null','1','%','0','1','null','不可用','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性','null');

/*Table structure for table `nms_alarm_indicators_node` */

DROP TABLE IF EXISTS `nms_alarm_indicators_node`;

CREATE TABLE `nms_alarm_indicators_node` (
  `id` int(11) NOT NULL auto_increment,
  `nodeid` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `subtype` varchar(255) default NULL,
  `datatype` varchar(255) default NULL,
  `moid` varchar(100) default NULL,
  `threshold` varchar(255) default NULL,
  `threshold_unit` varchar(10) default NULL,
  `compare` varchar(10) default '1',
  `compare_type` varchar(10) default '1',
  `alarm_times` varchar(10) default '1',
  `alarm_info` varchar(100) default '',
  `alarm_level` varchar(10) default '1',
  `enabled` varchar(10) default '1',
  `poll_interval` varchar(10) default NULL,
  `interval_unit` varchar(10) default NULL,
  `subentity` varchar(10) default NULL,
  `limenvalue0` bigint(20) default NULL,
  `limenvalue1` bigint(20) default NULL,
  `limenvalue2` bigint(20) default NULL,
  `time0` int(3) default NULL,
  `time1` int(3) default NULL,
  `time2` int(3) default NULL,
  `sms0` int(2) default NULL,
  `sms1` int(2) default NULL,
  `sms2` int(2) default NULL,
  `category` varchar(50) default NULL,
  `descr` varchar(50) default NULL,
  `unit` varchar(20) default NULL,
  `way0` varchar(20) default NULL,
  `way1` varchar(20) default NULL,
  `way2` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_alarm_indicators_node` */

/*Table structure for table `nms_alarm_message` */

DROP TABLE IF EXISTS `nms_alarm_message`;

CREATE TABLE `nms_alarm_message` (
  `id` varchar(15) default NULL,
  `ip_address` varchar(15) default NULL,
  `message` varchar(100) default NULL,
  `alarm_level` tinyint(1) default NULL,
  `category` tinyint(3) default NULL,
  `log_time` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_alarm_message` */

/*Table structure for table `nms_alarm_way` */

DROP TABLE IF EXISTS `nms_alarm_way`;

CREATE TABLE `nms_alarm_way` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) character set gb2312 default NULL,
  `description` varchar(255) character set gb2312 default NULL,
  `is_default` varchar(255) character set gb2312 default NULL,
  `is_page_alarm` varchar(255) character set gb2312 default NULL,
  `is_sound_alarm` varchar(255) character set gb2312 default NULL,
  `is_mail_alarm` varchar(255) character set gb2312 default NULL,
  `is_phone_alarm` varchar(255) character set gb2312 default NULL,
  `is_sms_alarm` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_alarm_way` */

insert into `nms_alarm_way` (`id`,`name`,`description`,`is_default`,`is_page_alarm`,`is_sound_alarm`,`is_mail_alarm`,`is_phone_alarm`,`is_sms_alarm`) values (126,'默认2','','1','1','1','1','1','1');

/*Table structure for table `nms_alarm_way_detail` */

DROP TABLE IF EXISTS `nms_alarm_way_detail`;

CREATE TABLE `nms_alarm_way_detail` (
  `id` int(11) NOT NULL auto_increment,
  `alarm_way_id` varchar(255) character set gb2312 default NULL,
  `alarm_category` varchar(255) character set gb2312 default NULL,
  `date_type` varchar(255) character set gb2312 default NULL,
  `send_times` varchar(20) character set gb2312 default NULL,
  `start_date` varchar(255) character set gb2312 default NULL,
  `end_date` varchar(255) character set gb2312 default NULL,
  `start_time` varchar(255) character set gb2312 default NULL,
  `end_time` varchar(255) character set gb2312 default NULL,
  `user_ids` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_alarm_way_detail` */

/*Table structure for table `nms_alarminfo` */

DROP TABLE IF EXISTS `nms_alarminfo`;

CREATE TABLE `nms_alarminfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `content` varchar(1000) default NULL,
  `ipaddress` varchar(100) default NULL,
  `level1` int(5) default NULL,
  `recordtime` timestamp NULL default NULL,
  `type` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_alarminfo` */

/*Table structure for table `nms_apache_history` */

DROP TABLE IF EXISTS `nms_apache_history`;

CREATE TABLE `nms_apache_history` (
  `id` int(20) NOT NULL auto_increment,
  `apache_id` int(10) default NULL,
  `is_canconnected` int(10) default NULL,
  `reason` varchar(255) default NULL,
  `mon_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_apache_history` */

/*Table structure for table `nms_apache_realtime` */

DROP TABLE IF EXISTS `nms_apache_realtime`;

CREATE TABLE `nms_apache_realtime` (
  `id` int(20) NOT NULL auto_increment,
  `apache_id` int(10) default NULL,
  `is_canconnected` int(10) default NULL,
  `reason` varchar(255) default NULL,
  `mon_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `sms_sign` int(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_apache_realtime` */

insert into `nms_apache_realtime` (`id`,`apache_id`,`is_canconnected`,`reason`,`mon_time`,`sms_sign`) values (1,1,0,'Apache服务无效','2010-08-20 09:53:33',1);

/*Table structure for table `nms_apacheconfig` */

DROP TABLE IF EXISTS `nms_apacheconfig`;

CREATE TABLE `nms_apacheconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `alias` varchar(100) default NULL,
  `username` varchar(100) default NULL,
  `password` varchar(100) default NULL,
  `ipaddress` varchar(50) default NULL,
  `port` int(20) default NULL,
  `flag` int(10) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `netid` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_apacheconfig` */

/*Table structure for table `nms_as400_disk` */

DROP TABLE IF EXISTS `nms_as400_disk`;

CREATE TABLE `nms_as400_disk` (
  `id` int(11) default NULL,
  `nodeid` varchar(11) character set gb2312 default NULL,
  `ipaddress` varchar(50) character set gb2312 default NULL,
  `unit` varchar(50) character set gb2312 default NULL,
  `type` varchar(50) character set gb2312 default NULL,
  `sizes` varchar(50) character set gb2312 default NULL,
  `used` varchar(50) character set gb2312 default NULL,
  `io_rqs` varchar(50) character set gb2312 default NULL,
  `request_size` varchar(50) character set gb2312 default NULL,
  `read_rqs` varchar(50) character set gb2312 default NULL,
  `write_rqs` varchar(50) character set gb2312 default NULL,
  `read` varchar(50) character set gb2312 default NULL,
  `write` varchar(50) character set gb2312 default NULL,
  `busy` varchar(50) character set gb2312 default NULL,
  `collect_time` varchar(50) character set gb2312 default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_as400_disk` */

/*Table structure for table `nms_as400_job` */

DROP TABLE IF EXISTS `nms_as400_job`;

CREATE TABLE `nms_as400_job` (
  `nodeid` varchar(11) character set gb2312 default NULL,
  `ipaddress` varchar(30) character set gb2312 default NULL,
  `name` varchar(50) character set gb2312 default NULL,
  `subsystem` varchar(255) character set gb2312 default NULL,
  `status` varchar(20) character set gb2312 default NULL,
  `active_status` varchar(20) character set gb2312 default NULL,
  `type` varchar(20) character set gb2312 default NULL,
  `subtype` varchar(20) character set gb2312 default NULL,
  `cpu_used_time` varchar(50) character set gb2312 default NULL,
  `user` varchar(50) character set gb2312 default NULL,
  `collect_time` varchar(50) character set gbk default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_as400_job` */

/*Table structure for table `nms_as400_job_group` */

DROP TABLE IF EXISTS `nms_as400_job_group`;

CREATE TABLE `nms_as400_job_group` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) character set gb2312 default NULL,
  `nodeid` varchar(255) character set gb2312 default NULL,
  `ipaddress` varchar(255) character set gb2312 default NULL,
  `mon_flag` varchar(255) character set gb2312 default NULL,
  `alarm_level` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_as400_job_group` */

insert into `nms_as400_job_group` (`id`,`name`,`nodeid`,`ipaddress`,`mon_flag`,`alarm_level`) values (138,'测试','126','192.84.45.110','1','1');

/*Table structure for table `nms_as400_job_group_detail` */

DROP TABLE IF EXISTS `nms_as400_job_group_detail`;

CREATE TABLE `nms_as400_job_group_detail` (
  `id` int(11) NOT NULL auto_increment,
  `group_id` varchar(255) character set gb2312 default NULL,
  `num` varchar(10) character set gb2312 default NULL,
  `name` varchar(255) character set gb2312 default NULL,
  `status` varchar(255) character set gb2312 default NULL,
  `active_status` varchar(255) character set gb2312 default NULL,
  `active_status_type` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_as400_job_group_detail` */

insert into `nms_as400_job_group_detail` (`id`,`group_id`,`num`,`name`,`status`,`active_status`,`active_status_type`) values (38,'138','3','333','1',',CNDW','1');

/*Table structure for table `nms_as400_subsystem` */

DROP TABLE IF EXISTS `nms_as400_subsystem`;

CREATE TABLE `nms_as400_subsystem` (
  `id` int(11) default NULL,
  `nodeid` varchar(11) character set gb2312 default NULL,
  `ipaddress` varchar(50) character set gb2312 default NULL,
  `name` varchar(50) character set gb2312 default NULL,
  `current_active_jobs` varchar(50) character set gb2312 default NULL,
  `is_exists` varchar(50) character set gb2312 default NULL,
  `path` varchar(50) character set gb2312 default NULL,
  `object_description` varchar(50) character set gb2312 default NULL,
  `collect_time` varchar(50) character set gb2312 default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_as400_subsystem` */

/*Table structure for table `nms_as400_system_pool` */

DROP TABLE IF EXISTS `nms_as400_system_pool`;

CREATE TABLE `nms_as400_system_pool` (
  `id` int(11) default NULL,
  `nodeid` varchar(11) character set gb2312 default NULL,
  `ipaddress` varchar(50) character set gb2312 default NULL,
  `system_pool` varchar(50) character set gb2312 default NULL,
  `name` varchar(50) character set gb2312 default NULL,
  `size` varchar(50) character set gb2312 default NULL,
  `reserved_size` varchar(50) character set gb2312 default NULL,
  `maximum_active_threads` varchar(50) character set gb2312 default NULL,
  `collect_time` varchar(50) character set gb2312 default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_as400_system_pool` */

/*Table structure for table `nms_as400_system_value` */

DROP TABLE IF EXISTS `nms_as400_system_value`;

CREATE TABLE `nms_as400_system_value` (
  `nodeid` varchar(11) character set gb2312 default NULL,
  `ipaddress` varchar(50) character set gb2312 default NULL,
  `category` varchar(50) character set gb2312 default NULL,
  `value` varchar(50) character set gb2312 default NULL,
  `unit` varchar(50) character set gb2312 default NULL,
  `description` varchar(255) character set gb2312 default NULL,
  `collect_time` varchar(255) character set gb2312 default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_as400_system_value` */

/*Table structure for table `nms_buscolltype` */

DROP TABLE IF EXISTS `nms_buscolltype`;

CREATE TABLE `nms_buscolltype` (
  `id` int(11) NOT NULL auto_increment,
  `collecttype` varchar(50) default NULL,
  `bct_desc` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_buscolltype` */

insert into `nms_buscolltype` (`id`,`collecttype`,`bct_desc`) values (1,'http','HTTP接口数据采集');

/*Table structure for table `nms_businessnode` */

DROP TABLE IF EXISTS `nms_businessnode`;

CREATE TABLE `nms_businessnode` (
  `id` int(11) NOT NULL auto_increment,
  `bid` int(11) default NULL,
  `bn_desc` varchar(50) default NULL,
  `collecttype` int(2) default NULL,
  `method` varchar(200) default NULL,
  `name` varchar(100) default NULL,
  `flag` int(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_businessnode` */

/*Table structure for table `nms_checkevent` */

DROP TABLE IF EXISTS `nms_checkevent`;

CREATE TABLE `nms_checkevent` (
  `name` varchar(200) NOT NULL,
  `alarmlevel` int(2) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_checkevent` */

insert into `nms_checkevent` (`name`,`alarmlevel`) values ('56:net:AllInBandwidthUtilHdx',1);
insert into `nms_checkevent` (`name`,`alarmlevel`) values ('56:net:AllOutBandwidthUtilHdx',1);

/*Table structure for table `nms_cicsconfig` */

DROP TABLE IF EXISTS `nms_cicsconfig`;

CREATE TABLE `nms_cicsconfig` (
  `id` int(11) NOT NULL auto_increment,
  `alias` varchar(50) default NULL,
  `region_name` varchar(50) default NULL,
  `ipaddress` varchar(50) default NULL,
  `port_listener` varchar(50) default NULL,
  `network_protocol` varchar(50) default NULL,
  `conn_timeout` int(10) default NULL,
  `sendemail` varchar(200) default NULL,
  `sendmobiles` varchar(200) default NULL,
  `netid` varchar(100) default NULL,
  `flag` int(10) default NULL,
  `gateway` varchar(100) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_cicsconfig` */

/*Table structure for table `nms_connecttypeconfig` */

DROP TABLE IF EXISTS `nms_connecttypeconfig`;

CREATE TABLE `nms_connecttypeconfig` (
  `id` int(11) NOT NULL auto_increment,
  `node_id` varchar(11) default NULL,
  `connecttype` varchar(20) default NULL,
  `username` varchar(50) default NULL,
  `password` varchar(50) default NULL,
  `login_prompt` varchar(50) default NULL,
  `password_prompt` varchar(50) default NULL,
  `shell_prompt` varchar(50) default NULL,
  `flag` int(2) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_connecttypeconfig` */

insert into `nms_connecttypeconfig` (`id`,`node_id`,`connecttype`,`username`,`password`,`login_prompt`,`password_prompt`,`shell_prompt`,`flag`) values (4,'71','ssh','','','','','',0);

/*Table structure for table `nms_cpu_data_temp` */

DROP TABLE IF EXISTS `nms_cpu_data_temp`;

CREATE TABLE `nms_cpu_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_cpu_data_temp` */

/*Table structure for table `nms_cycle_report_config` */

DROP TABLE IF EXISTS `nms_cycle_report_config`;

CREATE TABLE `nms_cycle_report_config` (
  `id` int(11) default NULL,
  `reciever_id` varchar(20) default NULL,
  `bid` varchar(20) default NULL,
  `collection_of_device_id` varchar(50) default NULL,
  `collection_of_generation_time` varchar(100) character set gb2312 default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_cycle_report_config` */

insert into `nms_cycle_report_config` (`id`,`reciever_id`,`bid`,`collection_of_device_id`,`collection_of_generation_time`) values (1,',1,4,6,7',',2,4,5,6',',128',',6周18时');

/*Table structure for table `nms_db2common` */

DROP TABLE IF EXISTS `nms_db2common`;

CREATE TABLE `nms_db2common` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `host_name` varchar(50) default '',
  `prod_release` varchar(50) default '',
  `total_memory` varchar(50) default '',
  `os_name` varchar(50) default '',
  `configured_cpu` varchar(50) default '',
  `installed_prod` varchar(50) default '',
  `total_cpu` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2common` */

/*Table structure for table `nms_db2conn` */

DROP TABLE IF EXISTS `nms_db2conn`;

CREATE TABLE `nms_db2conn` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `db_name` varchar(50) default '',
  `commitsql` varchar(50) default '',
  `db_location` varchar(50) default '',
  `appls_cur_cons` varchar(50) default '',
  `total_cons` varchar(50) default '',
  `db_conn_time` varchar(50) default '',
  `sqlm_elm_last_backup` varchar(50) default '',
  `db_status` varchar(50) default '',
  `failedsql` varchar(50) default '',
  `connections_top` varchar(50) default '',
  `db_path` varchar(50) default '',
  `dbname` varchar(20) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2conn` */

/*Table structure for table `nms_db2lock` */

DROP TABLE IF EXISTS `nms_db2lock`;

CREATE TABLE `nms_db2lock` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `db_name` varchar(50) default '',
  `total_sorts` varchar(50) default '',
  `lock_waits` varchar(50) default '',
  `lock_escals` varchar(50) default '',
  `lock_wait_time` varchar(50) default '',
  `rows_selected` varchar(50) default '',
  `deadlocks` varchar(50) default '',
  `total_sort_time` varchar(50) default '',
  `rows_read` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2lock` */

/*Table structure for table `nms_db2log` */

DROP TABLE IF EXISTS `nms_db2log`;

CREATE TABLE `nms_db2log` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `maxlogused` varchar(50) default '',
  `logused` varchar(50) default '',
  `pctused` varchar(50) default '',
  `logspacefree` varchar(50) default '',
  `maxsecused` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2log` */

/*Table structure for table `nms_db2pool` */

DROP TABLE IF EXISTS `nms_db2pool`;

CREATE TABLE `nms_db2pool` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `index_hit_ratio` varchar(50) default '',
  `Async_read_pct` varchar(50) default '',
  `bp_name` varchar(50) default '',
  `Direct_RW_Ratio` varchar(50) default '',
  `data_hit_ratio` varchar(50) default '',
  `BP_hit_ratio` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2pool` */

/*Table structure for table `nms_db2read` */

DROP TABLE IF EXISTS `nms_db2read`;

CREATE TABLE `nms_db2read` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `page_reorgs` varchar(50) default '',
  `overflow_accesses` varchar(50) default '',
  `tbname` varchar(50) default '',
  `rows_read` varchar(50) default '',
  `tbschema` varchar(50) default '',
  `rows_written` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2read` */

/*Table structure for table `nms_db2session` */

DROP TABLE IF EXISTS `nms_db2session`;

CREATE TABLE `nms_db2session` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `CLIENT_PLATFORM` varchar(50) default '',
  `APPL_STATUS` varchar(50) default '',
  `APPL_NAME` varchar(50) default '',
  `SNAPSHOT_TIMESTAMP` varchar(50) default '',
  `CLIENT_PROTOCOL` varchar(50) default '',
  `CLIENT_NNAME` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2session` */

/*Table structure for table `nms_db2spaceinfo` */

DROP TABLE IF EXISTS `nms_db2spaceinfo`;

CREATE TABLE `nms_db2spaceinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `usablespac` varchar(50) default '',
  `totalspac` varchar(50) default '',
  `usableper` varchar(50) default '',
  `tablespace_name` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2spaceinfo` */

/*Table structure for table `nms_db2sysinfo` */

DROP TABLE IF EXISTS `nms_db2sysinfo`;

CREATE TABLE `nms_db2sysinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `host_name` varchar(50) default '',
  `prod_release` varchar(50) default '',
  `total_memory` varchar(50) default '',
  `os_name` varchar(50) default '',
  `configured_cpu` varchar(50) default '',
  `installed_prod` varchar(50) default '',
  `total_cpu` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2sysinfo` */

/*Table structure for table `nms_db2tablespace` */

DROP TABLE IF EXISTS `nms_db2tablespace`;

CREATE TABLE `nms_db2tablespace` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `usablespac` varchar(50) default '',
  `totalspac` varchar(50) default '',
  `usableper` varchar(50) default '',
  `tablespace_name` varchar(50) default '',
  `dbname` varchar(20) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2tablespace` */

/*Table structure for table `nms_db2variable` */

DROP TABLE IF EXISTS `nms_db2variable`;

CREATE TABLE `nms_db2variable` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `variable_name` varchar(50) default NULL,
  `value` varchar(50) default NULL,
  `typename` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2variable` */

/*Table structure for table `nms_db2write` */

DROP TABLE IF EXISTS `nms_db2write`;

CREATE TABLE `nms_db2write` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `page_reorgs` varchar(50) default '',
  `overflow_accesses` varchar(50) default '',
  `tbname` varchar(50) default '',
  `rows_read` varchar(50) default '',
  `tbschema` varchar(50) default '',
  `rows_written` varchar(50) default '',
  `dbname` varchar(50) default '',
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_db2write` */

/*Table structure for table `nms_dbbackup` */

DROP TABLE IF EXISTS `nms_dbbackup`;

CREATE TABLE `nms_dbbackup` (
  `id` int(11) NOT NULL auto_increment,
  `filename` varchar(255) default NULL,
  `time` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_dbbackup` */

insert into `nms_dbbackup` (`id`,`filename`,`time`) values (1,'2010-03-22_09-01-04_all.sql','2010-03-22  09:01:04');
insert into `nms_dbbackup` (`id`,`filename`,`time`) values (2,'2010-05-05_14-02-51_all.sql','2010-05-05  14:02:52');

/*Table structure for table `nms_device_data_temp` */

DROP TABLE IF EXISTS `nms_device_data_temp`;

CREATE TABLE `nms_device_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `name` varchar(100) default NULL,
  `deviceindex` varchar(30) default NULL,
  `dtype` varchar(30) default NULL,
  `status` varchar(50) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_device_data_temp` */

/*Table structure for table `nms_device_type` */

DROP TABLE IF EXISTS `nms_device_type`;

CREATE TABLE `nms_device_type` (
  `id` int(11) NOT NULL auto_increment,
  `sys_oid` varchar(100) default NULL,
  `descr` varchar(100) default NULL,
  `image` varchar(50) default NULL,
  `producer` int(11) default NULL,
  `category` int(11) default NULL,
  `locate` varchar(100) default NULL,
  `log_time` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_device_type` */

insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (1,'1.3.6.1.4.1.2011.2.23.31','S8500','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:14:04');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (2,'1.3.6.1.4.1.207.1.14.24','Allied Telesyn AT-SB4211 Control Blade','switch_router.gif',10,2,'齐鲁石化','2006-08-10 15:14:41');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (3,'1.3.6.1.4.1.2011.10.1.43','Quidway S5624P','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:15:09');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (4,'1.3.6.1.4.1.2011.2.23.33','S6506','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:15:41');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (5,'1.3.6.1.4.1.2011.2.23.32','S8505','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:16:13');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (6,'1.3.6.1.4.1.2011.2.23.38','Quidway S3552P','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:17:19');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (7,'1.3.6.1.4.1.207.1.14.22','Allied Telesyn AT-SB4211 Switch Control Card','switch_router.gif',10,2,'齐鲁石化','2006-08-10 15:17:45');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (8,'1.3.6.1.4.1.2011.2.23.67','Quidway S3552F','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:18:28');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (11,'1.3.6.1.4.1.43.10.27.4.1.2.2','3Com SuperStack II','switch_router.gif',8,2,'齐鲁石化','2006-08-10 15:25:13');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (12,'1.3.6.1.4.1.6296.1.2','3Com 3526','router.png',8,1,'齐鲁石化','2006-08-10 15:26:18');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (13,'1.3.6.1.4.1.311.1.1.3.1.3','Windows 2000 Server','win2k.png',1,4,'齐鲁石化','2006-08-10 15:30:32');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (14,'1.3.6.1.4.1.311.1.1.3.1.1','Windows Professional/XP','winxp.png',1,4,'齐鲁石化','2006-08-10 15:33:53');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (15,'1.3.6.1.4.1.311.1.1.3.1.2','Windows 2000 Server','win2k.png',1,4,'齐鲁石化','2006-08-10 15:34:17');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (16,'1.3.6.1.4.1.2021.250.10','Linux','linux.png',0,4,'齐鲁石化','2006-08-10 15:35:33');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (17,'1.3.6.1.4.1.8072.3.2.10','Redhat 9/Linux','linux.png',0,4,'齐鲁石化','2006-08-17 14:52:03');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (18,'1.3.6.1.4.1.2.3.1.2.1.1.2','IBM RS/6000','ibm.gif',3,4,'齐鲁石化','2006-08-10 15:37:34');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (19,'1.3.6.1.4.1.2.3.1.2.1.1.3','IBM PowerPC or RISC System','ibm.gif',3,4,'齐鲁石化','2006-08-10 15:38:49');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (20,'1.3.6.1.4.1.2.3.1.2.1.1.31','IBM','ibm.gif',3,4,'齐鲁石化','2006-08-10 15:39:31');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (21,'1.3.6.1.4.1.11.2.3.9.0','HP Jet-Direct Print Server','printer.png',2,5,'齐鲁石化','2006-08-10 15:39:31');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (22,'1.3.6.1.4.1.11.2.3.9.1','HP Jet-Direct Print Server','printer.png',2,5,'齐鲁石化','2006-08-10 15:43:40');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (23,'1.3.6.1.4.1.11.2.3.9.2','HP Jet-Direct Network Plotter','printer.png',2,5,'齐鲁石化','2006-08-10 15:44:57');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (24,'1.3.6.1.4.1.11.2.3.9.5','HP JetDirect J3113A','printer.png',2,5,'齐鲁石化','2006-08-10 15:45:27');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (25,'1.3.6.1.4.1.11.2.3.9.6','HP Jet-Direct Print Server','printer.png',2,5,'齐鲁石化','2006-08-10 15:45:50');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (26,'1.3.6.1.4.1.52.3.9.20.1.4','Enterasys X-Pedition 8600','switch_router.gif',7,2,'齐鲁石化','2006-08-10 15:47:56');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (28,'1.3.6.1.4.1.274.5.1','PictureTel 330 NetConference Multipoint Server','server.gif',12,0,'齐鲁石化','2006-08-26 10:14:32');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (32,'1.3.6.1.4.1.81.17.1.17','P330 Stackable Switch','switch.png',5,3,'齐鲁石化','2006-09-13 17:25:35');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (33,'1.3.6.1.4.1.81.17.1.18','P130 workgroup switch','switch.png',5,3,'齐鲁石化','2006-09-13 17:26:42');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (37,'1.3.6.1.4.1.5009.1.1.3','未知设备','other.gif',0,0,'齐鲁石化','2006-08-12 09:50:24');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (38,'1.3.7','未知设备','other.gif',0,0,'齐鲁石化','2006-08-12 09:50:48');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (39,'1.3.6.1.4.1.4526.1.10','未知设备','other.gif',0,0,'齐鲁石化','2006-08-12 09:51:16');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (40,'1.3.6.1.4.1.3742.25.1.1.1.1','24 + 2G Ethernet Switch','switch.png',0,3,'齐鲁石化','2006-09-13 17:22:44');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (41,'1.3.6.1.4.1.11.2.3.10.1.2','HP Solaris Sparc agent','other.gif',2,0,'齐鲁石化','2006-08-12 09:52:10');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (42,'1.3.6.1.4.1.11.2.3.10.1','HP SunOS Sparc agent, original','other.gif',2,0,'齐鲁石化','2006-08-12 09:53:30');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (43,'1.3.6.1.4.1.11.2.3.10.1.1','HP SunOS Sparc agent','other.gif',2,0,'齐鲁石化','2006-08-12 09:53:54');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (44,'1.3.6.1.4.1.42.2.1.1','Sun Microsystems SunOS','server.gif',9,4,'齐鲁石化','2006-08-14 14:54:31');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (45,'1.3.6.1.4.1.9.1.186','Cisco 2611','router.png',6,1,'齐鲁石化','2006-08-14 14:54:21');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (46,'1.3.6.1.4.1.442.1.1.1.9.0','PEER Networks, a division of BMC Software, Inc., OptiMaster Release 1.9 on MS-Windows','other.gif',0,0,'齐鲁石化','2006-08-14 17:56:07');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (47,'1.3.6.1.4.1.11.1','未知设备','other.gif',2,0,'齐鲁石化','2006-08-20 17:10:55');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (48,'1.3.6.1.4.1.11.2.3.2.2','HP-UX HP 9000/300','server.gif',2,4,'齐鲁石化','2006-08-20 17:11:34');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (49,'1.3.6.1.4.1.11.2.3.2.3','HP-UX HP 9000/800','server.gif',2,4,'齐鲁石化','2006-08-20 17:11:59');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (50,'1.3.6.1.4.1.11.2.3.2.5','HP-UX HP 9000/700','server.gif',2,4,'齐鲁石化','2006-08-20 17:12:21');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (51,'1.3.6.1.4.1.9.1.324','Cisco Catalyst 2950-24','switch.png',6,3,'齐鲁石化','2006-09-13 17:28:13');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (52,'1.3.6.1.4.1.9.1.323','Cisco Catalyst 2950-12','switch.png',6,3,'齐鲁石化','2006-08-20 22:42:47');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (53,'1.3.6.1.4.1.9.1.325','Cisco Catalyst 2950C-24','switch.png',6,3,'齐鲁石化','2006-08-20 22:43:15');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (54,'1.3.6.1.4.0','INTELLINET Active Networking PS','other.gif',0,0,'齐鲁石化','2006-08-23 22:51:30');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (55,'1.3.6.1.4.1.9.1.27','Cisco 2511','router.png',6,1,'齐鲁石化','2006-08-23 22:52:09');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (56,'1.3.6.1.4.1.9.1.367','Cisco WS-C3550-48-SMI','switch_router.gif',6,2,'东华','2007-02-08 11:05:29');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (57,'1.3.6.1.4.1.2011.2.23.34','Quidway S3026C','switch.png',4,3,'齐鲁石化','2006-09-13 17:20:18');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (58,'1.3.6.1.4.1.2011.10.1.2','Quidway S2016','switch.png',4,3,'齐鲁石化','2006-09-13 17:19:18');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (59,'1.3.6.1.4.1.43.10.27.4.1.2.4','3Com Superstack 3 switch 4400','switch_router.gif',8,1,'齐鲁石化','2006-09-04 20:41:35');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (60,'1.3.6.1.4.1.171.10.48.1','DES-3226S Fast-Ethernet Switch','switch_router.gif',0,2,'齐鲁石化','2006-09-23 18:28:50');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (61,'1.3.6.1.4.1.6889.1.45.2','Avaya Cajun Switch Agent','switch_router.gif',5,2,'齐鲁石化','2006-09-23 18:32:00');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (62,'1.3.6.1.4.1.36.2.15.2.3','AlphaServer ES40 Compaq Tru64 UNIX','ibm.gif',0,4,'齐鲁石化','2006-12-04 11:40:23');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (63,'1.3.6.1.4.1.9.1.209','Cisco 2621','router.png',6,1,'齐鲁石化','2006-12-04 13:39:18');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (64,'1.3.6.1.4.1.9.1.217','Cisco Catalyst 2924R Switch','switch.png',6,3,'齐鲁石化','2006-12-04 13:26:23');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (65,'1.3.6.1.4.1.9.1.218','Cisco Catalyst 2924CR Switch','switch.png',6,3,'齐鲁石化','2006-12-04 13:26:52');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (66,'1.3.6.1.4.1.9.1.219','Catalyst 2912XL','switch.png',6,3,'东华','2007-02-06 10:55:19');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (67,'1.3.6.1.4.1.9.1.222','Cisco 7206','router.png',6,1,'东华','2007-02-06 11:05:18');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (68,'1.3.6.1.4.1.9.1.185','Cisco 2610','router.png',6,1,'东华','2007-02-06 11:08:04');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (69,'1.3.6.1.4.1.2011.2.23.41','huawei','switch_router.gif',4,2,'东华','2007-02-08 11:05:42');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (70,'1.3.6.1.4.1.9.1.110','Cisco 3640 Router','router.png',6,1,'东华合创','2007-03-06 16:24:52');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (71,'1.3.6.1.4.1.9.1.340','Cisco 3662Ac','router.png',6,1,'衡水信用社','2007-03-13 19:26:58');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (72,'1.3.6.1.4.1.9.1.366','Cisco Catalyst 3550-24','switch_router.gif',6,2,'衡水信用社','2007-03-13 19:58:24');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (73,'1.3.6.1.4.1.9.1.467','Cisco 2611 Router','router.png',6,1,'衡水信用社','2007-03-13 19:29:17');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (74,'1.3.6.1.4.1.4.1.2.1','TAINET Terminal Server TS-316','server.gif',0,4,'衡水信用社','2007-03-13 19:38:13');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (75,'1.3.6.1.4.1.1588.2.1.1.1','Fibre Channel-AL Switch','server.png',0,4,'衡水信用社','2007-03-13 19:45:16');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (76,'1.3.6.1.4.1.9.1.359','Cisco Catalyst 2950T-24','switch.png',6,3,'衡水信用社','2007-03-15 18:43:38');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (77,'1.3.6.1.4.1.9.1.283','Cisco Catalyst 6509','switch_router.gif',6,2,'恒源煤电','2007-04-05 07:33:30');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (78,'1.3.6.1.4.1.9.1.516','Cisco Catalyst 3750 series switches','switch.png',6,3,'恒源煤电','2007-04-05 07:36:49');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (79,'1.3.6.1.4.1.2011.2.23.23','huawei s3026E','switch.png',4,3,'恒源煤电','2007-04-05 07:41:07');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (80,'1.3.6.1.4.1.5624.2.1.52','Enterasys Networks, Inc. Matrix N7 Platinum','switch_router.gif',7,2,'潍柴动力','2007-04-19 09:26:19');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (81,'1.3.6.1.4.1.5624.2.1.24','Enterasys Networks Firmware Version: E9.1.3.0','switch_router.gif',7,2,'潍柴动力','2007-04-19 15:05:05');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (82,'1.3.6.1.4.1.9.1.429','Cisco Catalyst 2950G-48','switch.png',6,3,'潍柴动力','2007-04-19 15:08:30');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (83,'1.3.6.1.4.1.5624.2.1.28','Enterasys Networks Matrix E5 5H152-50','switch_router.gif',7,2,'潍柴动力','2007-04-19 15:11:24');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (84,'1.3.6.1.4.1.171.10.32.1.1','D-Link Fast Ethernet Switch','switch_router.gif',0,2,'潍柴动力','2007-04-19 15:15:24');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (85,'1.3.6.1.4.1.388.6.0','Symbol Mobius Wireless AP','switch.png',0,3,'潍柴动力','2007-04-19 15:16:48');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (86,'1.3.6.1.4.1.9.1.392','Cisco的PIX防火墙','firewall.png',6,6,'山西移动','2009-01-14 16:34:00');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (87,'1.3.6.1.4.1.9.1.502','CISCO4506交换机','switch.png',6,3,'齐鲁石化','2009-01-16 13:14:06');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (88,'1.3.6.1.4.1.25506.1.54','H3C路由交换机','switch_router.gif',13,2,'北京公司总部','2009-02-04 12:48:18');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (89,'1.3.6.1.4.1.25506.1.38','交换机','switch.png',13,3,'齐鲁石化','2009-02-04 16:12:22');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (90,'1.3.6.1.4.1.2011.2.39.11','无线接入器','wireless.png',13,7,'东华公司本部','2009-03-30 09:58:00');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (91,'1.3.6.1.4.1.9.1.278','Cisco Catalyst 3548XL','switch_router.gif',14,2,'CTSI','2009-03-12 13:12:07');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (92,'1.3.6.1.4.1.25506.1.224','H3C firewall---','firewall.png',13,6,'太原森林公安','2009-03-18 18:46:25');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (93,'1.3.6.1.4.1.25506.1.221','H3C MSR3011','switch_router.gif',13,2,'山西森林公安','2009-03-19 15:09:48');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (94,'1.3.6.1.4.1.25506.1.77','H3C MSR5060','switch_router.gif',13,2,'山西森林公安','2009-03-19 15:11:53');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (95,'1.3.6.1.4.1.25506.1.194','H3C SR6608','switch_router.gif',13,2,'山西森林公安','2009-03-19 15:13:55');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (96,'1.3.6.1.4.1.25506.1.159','H3C S7510','switch_router.gif',13,2,'山西森林公安','2009-03-19 16:09:19');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (97,'1.3.6.1.4.1.9.1.697','Cisco 2960','switch.png',14,3,'济南烟厂','2009-04-08 21:07:53');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (98,'1.3.6.1.4.1.9.1.428','Cisco 2950','switch.png',14,3,'齐鲁石化','2009-04-08 21:10:21');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (99,'1.3.6.1.4.1.9.1.696','CISCO 2960','switch.png',6,3,'济南烟厂','2009-04-08 22:34:32');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (100,'1.3.6.1.4.1.25506.1.40','交换机','switch.png',13,3,'公司','2009-04-24 10:56:22');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (101,'1.3.6.1.4.1.25506.1.75','H3C MSR 3060','switch_router.gif',13,2,'森林公安','2009-05-15 18:30:04');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (102,'1.3.6.1.4.1.2011.2.23.37','Quidway S3552G','switch_router.gif',13,2,'安徽蚌埠','2009-11-18 18:03:32');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (103,'1.3.6.1.4.1.2011.2.23.39','Quidway S3528G','switch_router.gif',13,2,'安徽蚌埠','2009-11-18 18:04:23');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (104,'1.3.6.1.4.1.3224.1.6','NetScreen-500 Firewall+VPN','firewall.png',15,66,'山西移动IDC','2009-11-26 13:59:44');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (105,'1.3.6.1.4.1.14331.1.4','NGFW4000防火墙','firewall.png',16,66,'','2010-01-12 09:41:10');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (106,'1.3.6.1.4.1.9.1.436','C3745','router.png',6,1,'德阳商行','2010-01-22 10:48:22');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (107,'1.3.6.1.4.1.9.1.444','C1700','router.png',6,1,'德阳商行','2010-01-22 10:49:25');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (108,'1.3.6.1.4.1.5651.1.101.13','MP7500','switch_router.gif',17,2,'吉林中行','2010-02-01 14:23:13');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (109,'1.3.6.1.4.1.9.1.620','C1841','router.png',14,1,'德阳商业银行','2010-03-01 11:50:48');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (110,'1.3.6.1.4.1.9.1.501','cat4000','switch_router.gif',14,2,'德阳商业银行','2010-03-01 11:51:32');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (111,'1.3.6.1.4.1.25506.1.187','SR8800','router.png',13,1,'广核','2010-12-07 11:00:43');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (112,'1.3.6.1.4.1.9.1.617','C3560','switch_router.gif',14,3,'','2010-12-07 14:54:47');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (113,'1.3.6.1.4.1.25506.1.33','S3600 交换机','switch.png',13,3,'公司','2010-12-31 13:36:36');
insert into `nms_device_type` (`id`,`sys_oid`,`descr`,`image`,`producer`,`category`,`locate`,`log_time`) values (114,'1.3.6.1.4.1.9.1.875','CISCO cat4500','switch.png',14,3,'','2011-01-11 18:27:01');

/*Table structure for table `nms_disk_data_temp` */

DROP TABLE IF EXISTS `nms_disk_data_temp`;

CREATE TABLE `nms_disk_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_disk_data_temp` */

/*Table structure for table `nms_diskconfig` */

DROP TABLE IF EXISTS `nms_diskconfig`;

CREATE TABLE `nms_diskconfig` (
  `ID` bigint(20) NOT NULL auto_increment,
  `IPADDRESS` varchar(30) default NULL,
  `DISKINDEX` int(20) default NULL,
  `NAME` varchar(50) default NULL,
  `LINKUSE` varchar(100) default NULL,
  `SMS` int(2) default NULL,
  `BAK` varchar(100) default NULL,
  `REPORTFLAG` int(2) default NULL,
  `LIMENVALUE` int(5) default NULL,
  `LIMENVALUE1` int(5) default NULL,
  `LIMENVALUE2` int(5) default NULL,
  `SMS1` int(2) default NULL,
  `SMS2` int(2) default NULL,
  `SMS3` int(2) default NULL,
  `monflag` int(2) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_diskconfig` */

/*Table structure for table `nms_diskperf_data_temp` */

DROP TABLE IF EXISTS `nms_diskperf_data_temp`;

CREATE TABLE `nms_diskperf_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_diskperf_data_temp` */

/*Table structure for table `nms_district` */

DROP TABLE IF EXISTS `nms_district`;

CREATE TABLE `nms_district` (
  `id` bigint(11) NOT NULL auto_increment,
  `name` varchar(100) default ' ',
  `dis_desc` varchar(1000) default ' ',
  `descolor` varchar(6) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_district` */

insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (1,'呼和浩特','呼和浩特','800080');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (2,'包头','包头','483D8B');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (3,'乌海','乌海','B0C4DE');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (4,'赤峰','赤峰','C0C0C0');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (5,'通辽','通辽','4169E1');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (6,'鄂尔多斯','鄂尔多斯','B0E0E6');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (7,'呼伦贝尔','呼伦贝尔','00CED1');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (8,'巴彦淖尔','巴彦淖尔','008080');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (9,'乌兰察布','乌兰察布','3CB371');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (10,'锡林郭勒','锡林郭勒','00B000');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (11,'阿拉善','阿拉善','B8860B');
insert into `nms_district` (`id`,`name`,`dis_desc`,`descolor`) values (12,'兴安','兴安','CD853F');

/*Table structure for table `nms_dns_history` */

DROP TABLE IF EXISTS `nms_dns_history`;

CREATE TABLE `nms_dns_history` (
  `id` int(20) NOT NULL auto_increment,
  `dns_id` int(10) default NULL,
  `is_canconnected` int(10) default NULL,
  `reason` varchar(255) default NULL,
  `mon_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_dns_history` */

/*Table structure for table `nms_dns_realtime` */

DROP TABLE IF EXISTS `nms_dns_realtime`;

CREATE TABLE `nms_dns_realtime` (
  `id` int(20) NOT NULL auto_increment,
  `dns_id` int(10) default NULL,
  `is_canconnected` int(10) default NULL,
  `reason` varchar(255) default NULL,
  `mon_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `sms_sign` int(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_dns_realtime` */

insert into `nms_dns_realtime` (`id`,`dns_id`,`is_canconnected`,`reason`,`mon_time`,`sms_sign`) values (1,1,1,'DNS服务有效','2010-05-04 19:40:42',1);
insert into `nms_dns_realtime` (`id`,`dns_id`,`is_canconnected`,`reason`,`mon_time`,`sms_sign`) values (2,1,1,'DNS服务有效','2010-05-04 20:06:40',1);

/*Table structure for table `nms_dnsconfig` */

DROP TABLE IF EXISTS `nms_dnsconfig`;

CREATE TABLE `nms_dnsconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `username` varchar(100) default NULL,
  `password` varchar(100) default NULL,
  `hostip` varchar(100) default NULL,
  `hostinter` int(20) default NULL,
  `dns` varchar(500) default NULL,
  `dnsip` varchar(200) default NULL,
  `flag` int(10) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `netid` varchar(50) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_dnsconfig` */

/*Table structure for table `nms_dominoconfig` */

DROP TABLE IF EXISTS `nms_dominoconfig`;

CREATE TABLE `nms_dominoconfig` (
  `id` bigint(11) NOT NULL,
  `name` varchar(100) default NULL,
  `ipaddress` varchar(19) default NULL,
  `community` varchar(100) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `mon_flag` int(11) default NULL,
  `netid` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_dominoconfig` */

/*Table structure for table `nms_email_history` */

DROP TABLE IF EXISTS `nms_email_history`;

CREATE TABLE `nms_email_history` (
  `id` bigint(11) NOT NULL auto_increment,
  `email_id` int(11) default NULL,
  `is_canconnected` int(11) default NULL,
  `reason` varchar(255) character set gb2312 default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_email_history` */

/*Table structure for table `nms_email_realtime` */

DROP TABLE IF EXISTS `nms_email_realtime`;

CREATE TABLE `nms_email_realtime` (
  `id` int(11) NOT NULL auto_increment,
  `email_id` int(11) default NULL,
  `is_canconnected` int(11) default NULL,
  `reason` varchar(255) character set gb2312 default NULL,
  `mon_time` timestamp NULL default NULL,
  `sms_sign` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_email_realtime` */

/*Table structure for table `nms_emailconfig` */

DROP TABLE IF EXISTS `nms_emailconfig`;

CREATE TABLE `nms_emailconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `address` varchar(50) default NULL,
  `sendmail` varchar(100) default NULL,
  `sendpasswd` varchar(30) default NULL,
  `recivemail` varchar(100) default NULL,
  `poll_interval` int(11) default NULL,
  `timeout` int(11) default NULL,
  `flag` int(11) default NULL,
  `mon_flag` int(11) default NULL,
  `sendmobiles` varchar(500) default NULL,
  `netid` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_emailconfig` */

/*Table structure for table `nms_emailmonitorconf` */

DROP TABLE IF EXISTS `nms_emailmonitorconf`;

CREATE TABLE `nms_emailmonitorconf` (
  `id` bigint(11) NOT NULL,
  `name` varchar(50) default NULL,
  `address` varchar(50) default NULL,
  `username` varchar(50) default NULL,
  `password` varchar(50) default NULL,
  `recivemail` varchar(50) default NULL,
  `timeout` bigint(5) default NULL,
  `flag` int(2) default NULL,
  `monflag` int(2) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `bid` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_emailmonitorconf` */

/*Table structure for table `nms_employee` */

DROP TABLE IF EXISTS `nms_employee`;

CREATE TABLE `nms_employee` (
  `id` int(5) NOT NULL auto_increment,
  `name` varchar(30) default NULL,
  `sex` int(1) default NULL,
  `dept_id` int(3) default NULL,
  `position_id` int(3) default NULL,
  `phone` varchar(30) default NULL,
  `email` varchar(30) default NULL,
  `mobile` varchar(30) default NULL,
  `businessids` varchar(200) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_employee` */

insert into `nms_employee` (`id`,`name`,`sex`,`dept_id`,`position_id`,`phone`,`email`,`mobile`,`businessids`) values (2,'胡可磊',1,1,1,'62662421','hukelei@dhcc.com.cn','13811372044','null');

/*Table structure for table `nms_envir_data_temp` */

DROP TABLE IF EXISTS `nms_envir_data_temp`;

CREATE TABLE `nms_envir_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_envir_data_temp` */

/*Table structure for table `nms_errpt_config` */

DROP TABLE IF EXISTS `nms_errpt_config`;

CREATE TABLE `nms_errpt_config` (
  `ID` int(11) NOT NULL auto_increment,
  `NODEID` int(11) default NULL,
  `ERRPTTYPE` varchar(100) default NULL,
  `ERRPTCLASS` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_errpt_config` */

/*Table structure for table `nms_errptlog` */

DROP TABLE IF EXISTS `nms_errptlog`;

CREATE TABLE `nms_errptlog` (
  `ID` int(11) NOT NULL,
  `LABELS` varchar(100) default NULL,
  `IDENTIFIER` varchar(50) default NULL,
  `COLLETTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `SEQNUMBER` int(10) default NULL,
  `NODEID` varchar(20) default NULL,
  `MACHINEID` varchar(20) default NULL,
  `ERRPTCLASS` varchar(5) default NULL,
  `ERRPTTYPE` varchar(5) default NULL,
  `RESOURCENAME` varchar(50) default NULL,
  `RESOURCECLASS` varchar(50) default NULL,
  `RESOURCETYPE` varchar(50) default NULL,
  `LOCATIONS` varchar(100) default NULL,
  `VPD` text,
  `DESCRIPTIONS` text,
  `HOSTID` varchar(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_errptlog` */

/*Table structure for table `nms_fdb_data_temp` */

DROP TABLE IF EXISTS `nms_fdb_data_temp`;

CREATE TABLE `nms_fdb_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `ifindex` varchar(30) default NULL,
  `ipaddress` varchar(30) default NULL,
  `mac` varchar(30) default NULL,
  `ifband` varchar(2) default NULL,
  `ifsms` varchar(2) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_fdb_data_temp` */

/*Table structure for table `nms_fdb_table` */

DROP TABLE IF EXISTS `nms_fdb_table`;

CREATE TABLE `nms_fdb_table` (
  `id` bigint(10) NOT NULL,
  `node_id` int(3) default NULL,
  `mac` varchar(17) default NULL,
  `port` varchar(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_fdb_table` */

/*Table structure for table `nms_fibrecapability_data_temp` */

DROP TABLE IF EXISTS `nms_fibrecapability_data_temp`;

CREATE TABLE `nms_fibrecapability_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_fibrecapability_data_temp` */

/*Table structure for table `nms_fibreconfig_data_temp` */

DROP TABLE IF EXISTS `nms_fibreconfig_data_temp`;

CREATE TABLE `nms_fibreconfig_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_fibreconfig_data_temp` */

/*Table structure for table `nms_firewalltype` */

DROP TABLE IF EXISTS `nms_firewalltype`;

CREATE TABLE `nms_firewalltype` (
  `id` bigint(20) NOT NULL auto_increment,
  `firewalltype` varchar(50) default NULL,
  `firewalldesc` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_firewalltype` */

insert into `nms_firewalltype` (`id`,`firewalltype`,`firewalldesc`) values (10,'netscreen','netscreen');
insert into `nms_firewalltype` (`id`,`firewalltype`,`firewalldesc`) values (11,'pix','pix');

/*Table structure for table `nms_flash_data_temp` */

DROP TABLE IF EXISTS `nms_flash_data_temp`;

CREATE TABLE `nms_flash_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_flash_data_temp` */

/*Table structure for table `nms_ftp_history` */

DROP TABLE IF EXISTS `nms_ftp_history`;

CREATE TABLE `nms_ftp_history` (
  `id` int(11) NOT NULL auto_increment,
  `ftp_id` int(11) default NULL,
  `is_canconnected` int(11) default NULL,
  `reason` varchar(255) character set gb2312 default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_ftp_history` */

/*Table structure for table `nms_ftp_realtime` */

DROP TABLE IF EXISTS `nms_ftp_realtime`;

CREATE TABLE `nms_ftp_realtime` (
  `id` int(11) NOT NULL auto_increment,
  `ftp_id` int(11) default NULL,
  `is_canconnected` int(11) default NULL,
  `reason` varchar(255) character set gb2312 default NULL,
  `mon_time` timestamp NULL default NULL,
  `sms_sign` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_ftp_realtime` */

/*Table structure for table `nms_ftpconfig` */

DROP TABLE IF EXISTS `nms_ftpconfig`;

CREATE TABLE `nms_ftpconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `str` varchar(255) default NULL,
  `user_name` varchar(30) default NULL,
  `user_password` varchar(30) default NULL,
  `availability_string` varchar(255) default NULL,
  `poll_interval` int(11) default NULL,
  `unavailability_string` varchar(255) default NULL,
  `timeout` int(11) default NULL,
  `verify` int(11) default NULL,
  `flag` int(11) default NULL,
  `mon_flag` int(11) default NULL,
  `alias` varchar(50) default NULL,
  `sendmobiles` varchar(500) default NULL,
  `netid` varchar(100) default NULL,
  `filename` varchar(50) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ftpconfig` */

/*Table structure for table `nms_ftpmonitorconfig` */

DROP TABLE IF EXISTS `nms_ftpmonitorconfig`;

CREATE TABLE `nms_ftpmonitorconfig` (
  `id` bigint(11) NOT NULL,
  `name` varchar(100) default NULL,
  `username` varchar(50) default NULL,
  `password` varchar(50) default NULL,
  `timeout` int(10) default NULL,
  `monflag` int(2) default NULL,
  `filename` varchar(200) default NULL,
  `bid` varchar(100) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ftpmonitorconfig` */

/*Table structure for table `nms_func` */

DROP TABLE IF EXISTS `nms_func`;

CREATE TABLE `nms_func` (
  `id` bigint(11) NOT NULL auto_increment,
  `func_desc` varchar(200) character set gb2312 default NULL,
  `ch_desc` varchar(200) character set gb2312 default NULL,
  `level_desc` varchar(200) character set gb2312 default NULL,
  `father_node` varchar(200) character set gb2312 default NULL,
  `url` varchar(200) character set gb2312 default NULL,
  `img_url` varchar(200) character set gb2312 default NULL,
  `is_current_window` varchar(200) character set gb2312 default NULL,
  `width` varchar(5) default NULL,
  `height` varchar(5) default NULL,
  `clientX` varchar(5) default NULL,
  `clientY` varchar(5) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_func` */

insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (1,'0A','首页','1','0','user.do?action=home','images/menu01','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (2,'0A0A','快捷功能','2','1',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (3,'0A0A01','添加设备','3','2','network.do?action=ready_add','resource/image/menu/tjsb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (4,'0A0A02','自动发现','3','2','discover.do?action=config','resource/image/autoDiscovery16.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (5,'0A0A03','用户管理','3','2','user.do?action=list&jp=1','resource/image/menu/yh.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (6,'0A0A04','SNMP设置','3','2','snmp.do?action=list','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (7,'0A0A05','密码设置','3','2','system/user/inputpwd.jsp','resource/image/menu/xgmm.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (8,'0A0A06','工作台历','3','2','userTaskLog.do?action=list','resource/image/calendar_view_month.png','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (9,'0B','资源','1','0',NULL,'images/menu02','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (10,'0B0A','拓扑','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (11,'0B0A01','网络拓扑','3','10','topology/network/index.jsp','resource/image/menu/wltp.gif','1',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (12,'0B0A02','主机服务器','3','10','topology/server/index.jsp','resource/image/menu/zjfwq.gif','1',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (13,'0B0C','设备维护','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (14,'0B0C01','设备列表','3','13','network.do?action=list&jp=1','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (15,'0B0C02','添加设备','3','13','network.do?action=ready_add','resource/image/menu/tjsb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (16,'0B0C03','端口配置','3','13','portconfig.do?action=list&jp=1','resource/image/menu/dkpz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (17,'0B0C04','链路配置','3','13','link.do?action=list&jp=1','resource/image/menu/llxx.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (18,'0B0B','性能监视','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (19,'0B0B01','监视对象一览表','3','18','network.do?action=monitornodelist&jp=1','resource/image/menu/jsxxylb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (21,'0B0D','IP/MAC资源','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (22,'0B0D05','端口-IP-MAC基线','3','21','ipmacbase.do?action=list&jp=1','resource/image/menu/dk-ip-mac-jx.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (23,'0B0D07','当前MAC信息','3','21','ipmac.do?action=list&jp=1','resource/image/menu/dqmacxx.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (24,'0B0D06','MAC变更历史','3','21','ipmacchange.do?action=list&jp=1','resource/image/menu/macbgls.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (25,'0B0E','视图管理','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (26,'0B0E01','视图编辑','3','25','customxml.do?action=list&jp=1','resource/image/menu/stbj.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (27,'0B0E02','视图展示','3','25','topology/view/custom.jsp','resource/image/menu/stzs.gif','1',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (28,'0B0F','设备面板配置管理','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (29,'0B0F01','面板模板编辑','3','28','panel.do?action=panelmodellist&jp=1','resource/image/menu/mbmbbj.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (30,'0B0F02','设备面板编辑','3','28','network.do?action=panelnodelist&jp=1','resource/image/menu/sbmbbj.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (31,'0E','告警','1','0',NULL,'images/menu03','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (32,'0E0A','告警浏览','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (33,'0E0A01','告警列表','3','32','event.do?action=summary&jp=1','resource/image/menu/gjlb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (34,'0E0A02','存在告警的设备','3','32','alarm/event/alarmnodelist.jsp','resource/image/menu/czgjdsb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (35,'0E0B','告警统计','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (36,'0E0B01','按业务分布','3','35','event.do?action=businesslist&jp=1','resource/image/menu/anywfb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (37,'0E0B02','按设备分布','3','35','event.do?action=equipmentlist&jp=1','resource/image/menu/ansbfb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (38,'0E0C','Trap管理','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (39,'0E0C01','浏览Trap','3','38','trap.do?action=list&jp=1','resource/image/menu/lltrap.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (40,'0E0D','Syslog管理','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (41,'0E0D01','浏览Syslog','3','40','netsyslog.do?action=list&jp=1','resource/image/menu/llsyslog.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (42,'0E0D02','过滤规则','3','40','netsyslog.do?action=filter&jp=1','resource/image/menu/glgz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (43,'0F','报表','1','0',NULL,'images/menu04','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (44,'0F0A','报表浏览','2','43',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (45,'0F0A01','网络设备报表','3','44','netreport.do?action=list&jp=1','resource/image/menu/wlsbbb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (46,'0F0A02','服务器报表','3','44','hostreport.do?action=list&jp=1','resource/image/menu/fwqbb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (47,'0D','应用','1','0',NULL,'images/menu05','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (48,'0D0A','数据库管理','2','47',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (49,'0D0A01','数据库监视','3','48','db.do?action=list&jp=1','resource/image/menu/sjkjs.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (50,'0D0A02','数据库类型管理','3','48','dbtype.do?action=list','resource/image/menu/sjklxgl.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (51,'0D0A03','Oracle告警设置','3','48','oraspace.do?action=list&jp=1','resource/image/menu/oracle_gjsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (52,'0D0A04','SQLServer告警设置','3','48','sqldbconfig.do?action=list&jp=1','resource/image/menu/sqlserver_gjsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (53,'0D0A05','DB2告警设置','3','48','db2config.do?action=list&jp=1','resource/image/menu/db2_gjsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (54,'0D0A06','Sybase告警设置','3','48','sybaseconfig.do?action=list&jp=1','resource/image/menu/sybase_gjsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (55,'0D0B','服务管理','2','47',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (56,'0D0B01','FTP服务监视','3','55','FTP.do?action=list&jp=1','resource/image/menu/ftp_fwjs.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (57,'0D0B02','Email服务监视','3','55','mail.do?action=list&jp=1','resource/image/menu/email_fwjs.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (58,'0D0B03','主机进程监视','3','55','process.do?action=list&jp=1','resource/image/menu/zjjcjs.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (59,'0D0B04','WEB访问服务监视','3','55','web.do?action=list&jp=1','resource/image/menu/web_fwfujs.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (63,'0D0C','中间件管理','2','47',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (64,'0D0C01','MQ监视','3','63','mq.do?action=list&jp=1','resource/image/menu/mq_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (65,'0D0C02','MQ告警设置','3','63','mqchannel.do?action=list&jp=1','resource/image/menu/mq_gjsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (66,'0D0C03','Domino监视','3','63','domino.do?action=list&jp=1','resource/image/menu/domino_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (67,'0D0C04','WAS监视','3','63','was.do?action=list&jp=1','resource/image/menu/was_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (68,'0D0C05','Weblogic监视','3','63','weblogic.do?action=list&jp=1','resource/image/menu/weblogic_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (69,'0D0C06','Tomcat监视','3','63','tomcat.do?action=list&jp=1','resource/image/menu/tomcat_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (70,'0H','系统管理','1','0','system.do?action=list','images/menu06','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (71,'0H0A','资源管理','2','70',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (72,'0H0A01','SNNP模板','3','71','snmp.do?action=list','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (73,'0H0A02','设备厂商','3','71','producer.do?action=list&jp=1','resource/image/menu/sbcs.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (74,'0H0A03','设备型号','3','71','devicetype.do?action=list&jp=1','resource/image/menu/sbxh.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (75,'0H0A04','服务','3','71','service.do?action=list&jp=1','resource/image/menu/fw.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (76,'0H0B','用户管理','2','70',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (77,'0H0B01','用户','3','76','user.do?action=list&jp=1','resource/image/menu/yh.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (78,'0H0B02','角色','3','76','role.do?action=list&jp=1','resource/image/menu/js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (79,'0H0B03','部门','3','76','dept.do?action=list&jp=1','resource/image/menu/bm.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (80,'0H0B04','职位','3','76','position.do?action=list&jp=1','resource/image/menu/zw.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (81,'0H0B05','权限设置','3','76','admin.do?action=list&jp=1','resource/image/menu/qxsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (82,'0H0B06','修改密码','3','76','system/user/inputpwd.jsp','resource/image/menu/xgmm.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (83,'0H0B07','菜单设置','3','76','menu.do?action=list','resource/image/menu/cdsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (84,'0H0B08','员工库','3','76','employee.do?action=list&jp=1','resource/image/menu/ygk.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (85,'0H0B09','供应商信息','3','76','supper.do?action=list&jp=1','resource/image/menu/gysxx.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (86,'0H0B10','用户操作审计','3','76','userAudit.do?action=list','resource/image/menu/yhczsj.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (87,'0H0C','系统配置','2','70',NULL,NULL,'0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (88,'0H0C01','业务分类','3','87','business.do?action=list&jp=1','resource/image/menu/ywfl.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (89,'0H0C02','操作日志','3','87','syslog.do?action=list&jp=1','resource/image/menu/czrz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (90,'0H0C03','告警邮箱设置','3','87','alertemail.do?action=list&jp=1','resource/image/menu/gjyxsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (91,'0H0C04','TFTP设置','3','87','tftpserver.do?action=list&jp=1','resource/image/menu/tftp_sz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (92,'0H0C05','磁盘阀值一览表','3','18','disk.do?action=list&jp=1','resource/image/menu/cpfzylb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (95,'0H0A05','防火墙类型','3','71','fwtype.do?action=list','resource/image/menu/fhqlx.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (96,'0H0C07','IIS监视','3','63','iis.do?action=list&jp=1','resource/image/menu/iis_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (97,'0H0B08','端口服务监视','3','55','pstype.do?action=list','resource/image/menu/dkfwjs.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (98,'0H0A07','MySql监视','3','48','mysqlconfig.do?action=list&jp=1','resource/image/menu/mysql_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (99,'0H0A06','中间件管理','3','71','middleware.do?action=list&jp=1','resource/image/menu/zjjgl.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (100,'0H0C08','CICS监视','3','63','cics.do?action=list&jp=1','resource/image/menu/cics_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (101,'0B0D09','MAC地址定位','3','21','maclocate.do?action=readyfind','resource/image/menu/mac_dzdw.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (102,'0H0C09','DNS监视','3','63','dns.do?action=list&jp=1','resource/image/menu/dns_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (103,'0H0D','数据库维护','2','70','','','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (104,'0H0D01','数据倒出','3','103','dbbackup.do?action=list','resource/image/menu/sjdc.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (105,'0H0D02','数据倒入','3','103','dbbackup.do?action=dbbackuplist&jp=1','resource/image/menu/sjdr.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (106,'0F0B','决策支持','2','43','','','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (107,'0F0B02','网络设备','3','106','netreport.do?action=choceDoc&jp=1','resource/image/menu/wlsb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (108,'0F0B04','服务器','3','106','hostreport.do?action=hostchoce&jp=1','resource/image/menu/fwq.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (109,'0F0A03','数据库报表','3','44','dbreport.do?action=list&jp=1','resource/image/menu/sjkbb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (110,'0F0D','业务报表浏览','2','43','','','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (111,'0F0C02','业务报表','3','110','businessReport.do?action=list&jp=1','resource/image/menu/ywbb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (112,'0D0A08','Informix告警设置','3','48','informixspace.do?action=list&jp=1','resource/image/menu/informix_gjsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (113,'0E0E','短信告警','2','31','','','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (114,'0E0E01','短信告警浏览','3','113','smsevent.do?action=list&jp=1','resource/image/menu/dxgjll.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (115,'0H0C05','配置子项类别','3','87','subconfigcat.do?action=list&jp=1','resource/image/menu/pzzxlb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (116,'0D0C0:','IISLog监视','3','63','iislog.do?action=list&jp=1','resource/image/menu/iislog_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (117,'0D0D','环境监控','2','47','','','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (118,'0D0D01','温湿度监测','3','117','temperatureHumidity.do?action=list2&jp=1','resource/image/menu/wsdjc.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (119,'0D0D02','温湿度监测参数配置','3','117','temperatureHumidity.do?action=list&jp=1','resource/image/menu/wsdjccspz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (120,'0B0E03','业务列表','3','25','businessNode.do?action=list&jp=1','resource/image/icon_detail.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (121,'0H0A07','业务采集类型','3','71','buscolltype.do?action=list&jp=1','resource/image/viewmac.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (122,'0B0A05','业务视图','3','10','flex/home.html','resource/image/menu/wltp.gif','1','null','null','null','null');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (123,'0D0C0;','JBOSS监视','3','63','jboss.do?action=list&jp=1','resource/image/jboss.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (124,'0H0A08','端口类型','3','71','porttype.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (125,'0B0C05','远程PING服务器','3','13','remotePing.do?action=list','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (126,'0H0C08','区域管理','3','87','district.do?action=list&jp=1','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (127,'0H0C09','消息服务器设置','3','87','alertinfo.do?action=list&jp=1','resource/image/menu/gjyxsz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (128,'0B0D0:','IP地址段管理','3','21','ipdistrict.do?action=list','resource/image/menu/pzzxlb.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (129,'0F0A04','中间件报表','3','44','midcapreport.do?action=midlist&jp=1','resource/image/viewreport.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (130,'0B0D0;','网段匹配','3','21','ipDistrictMatch.do?action=list&jp=1','resource/image/menu/dxgjll.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (131,'0B0D0<','mac配置','3','21','macconfig.do?action=list&jp=1','resource/image/menu/dqmacxx.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (132,'0B0D0=','网段管理','3','21','ipDistrictMatch.do?action=districtDetails&jp=1','resource/image/menu/czrz.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (133,'0D0C0<','APACHE监视','3','63','apache.do?action=list&jp=1','resource/image/apache.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (134,'0D0C0=','Tuxedo管理','3','63','tuxedo.do?action=list&jp=1','resource/image/menu/weblogic_js.gif','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (135,'0G','工具','1','0','user.do?action=home','images/menu07','0',NULL,NULL,NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (136,'0G0I','Ping','2','135','/tool/ping.jsp','','1','500','400',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (137,'0G0B','路由跟踪','2','135','/tool/tracerouter.jsp','','1','500','400',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (139,'0G0J','远程Ping','2','135','/tool/remote_Ping.jsp','','1','500','420',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (140,'0G0E','Telnet','2','135','network.do?action=telnet','','1','500','400',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (141,'0G0F','Web Telnet','2','135','/webutil/webtelnet.swf','','1','644','555',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (142,'0G0G','Web SSH','2','135','/webutil/WebSSH.swf','','1','644','555',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (143,'0G0H','SNMP测试','2','135','/tool/snmpping.jsp','','1','500','400',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (144,'0D0E','存储管理','2','47','','','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (145,'0D0E01','存储类型管理','3','144','storagetype.do?action=list','resource/image/menu/fhqlx.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (146,'0D0E02','存储管理','3','144','storage.do?action=list&jp=1','resource/image/menu/sbxh.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (147,'0B0C07','连通性检测设置','3','13','connectConfig.do?action=list&jp=1','resource/image/menu/czrz.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (148,'0G0I','链路分析','2','135','/tool/linkAnalytics.jsp','','1','600','400',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (149,'0C','性能','1','0','performance/index.jsp','images/menu08','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (150,'0H0C06','链路性能一览表','3','18','linkperformance.do?action=list&jp=1','resource/image/menu/llxx.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (151,'0G0J','数据库连接工具','2','135','/tool/index.jsp','','1','800','600',NULL,NULL);
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (152,'0H0C07','告警指标配置','3','18','alarmIndicators.do?action=list&jp=1','resource/image/toolbar/jszbfzpz.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (153,'0H0C08','采集指标配置','3','18','gatherIndicators.do?action=list&jp=1','resource/image/menu/pzzxlb.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (154,'0H0C09','采集指标一览表','3','18','nodeGatherIndicators.do?action=showlist&jp=1','resource/image/menu/czrz.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (155,'0H0C0;','指标阀值一览表','3','18','alarmIndicatorsNode.do?action=showlist&jp=1','resource/image/menu/zbqjfzylb.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (156,'0B0C09','远程登陆设置','3','13','vpntelnetconf.do?action=list&jp=1','resource/image/menu/ywfl.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (157,'0H0C0<','进程监视列表','3','18','processgroup.do?action=showlist&jp=1','resource/image/menu/cdsz.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (158,'0H0C0=','主机服务监视','3','18','hostservicegroup.do?action=showlist&jp=1','resource/image/menu/snmp_mb.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (159,'0E0D03','告警设置列表','3','40','netsyslogalarm.do?action=list&jp=1','resource/image/menu/llsyslog.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (160,'0H0C0:','方案管理','3','87','knowledge.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (161,'0H0C0;','知识库管理','3','87','knowledgebase.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (162,'0E0A03','告警方式设置','3','32','alarmWay.do?action=list&jp=1','resource/image/menu/xgmm.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (163,'0F0E','报表订阅','2','43','','','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (164,'0F0E01','订阅设置','3','163','subscribeReportConfig.do?action=list&jp=1','resource/image/menu/xgmm.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (165,'0A0A07','设备配置文件列表','3','2','vpntelnetconf.do?action=deviceList&jp=1','resource/image/menu/ywfl.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (166,'0B0C0:','设备配置文件列表','3','13','vpntelnetconf.do?action=deviceList&jp=1','resource/image/menu/ywfl.gif','0','','','','');
insert into `nms_func` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`,`width`,`height`,`clientX`,`clientY`) values (167,'0H0C0<','系统运行模式','3','87','systemconfig.do?action=collectwebflag','resource/image/menu/xgmm.gif','0','','','','');

/*Table structure for table `nms_gather_indicators` */

DROP TABLE IF EXISTS `nms_gather_indicators`;

CREATE TABLE `nms_gather_indicators` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) character set gb2312 default NULL,
  `type` varchar(255) character set gb2312 default NULL,
  `subtype` varchar(255) character set gb2312 default NULL,
  `alias` varchar(255) character set gb2312 default NULL,
  `description` varchar(255) character set gb2312 default NULL,
  `category` varchar(255) character set gb2312 default NULL,
  `isDefault` varchar(255) character set gb2312 default NULL,
  `isCollection` varchar(255) character set gb2312 default NULL,
  `poll_interval` varchar(255) character set gb2312 default NULL,
  `interval_unit` varchar(5) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_gather_indicators` */

insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1,'ping','net','cisco','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (2,'cpu','net','cisco','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (3,'memory','net','cisco','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (4,'flash','net','cisco','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (5,'temperature','net','cisco','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (6,'fan','net','cisco','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (7,'power','net','cisco','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (8,'voltage','net','cisco','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (9,'systemgroup','net','cisco','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (10,'ipmac','net','cisco','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (11,'fdb','net','cisco','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (12,'router','net','cisco','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (13,'interface','net','cisco','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (14,'ping','net','h3c','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (15,'cpu','net','h3c','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (16,'memory','net','h3c','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (17,'flash','net','h3c','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (18,'temperature','net','h3c','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (19,'fan','net','h3c','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (20,'power','net','h3c','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (21,'voltage','net','h3c','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (22,'systemgroup','net','h3c','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (23,'ipmac','net','h3c','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (24,'fdb','net','h3c','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (25,'router','net','h3c','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (26,'interface','net','h3c','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (27,'ping','net','entrasys','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (28,'cpu','net','entrasys','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (29,'memory','net','entrasys','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (30,'flash','net','entrasys','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (31,'temperature','net','entrasys','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (32,'fan','net','entrasys','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (33,'power','net','entrasys','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (34,'voltage','net','entrasys','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (35,'systemgroup','net','entrasys','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (36,'ipmac','net','entrasys','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (37,'fdb','net','entrasys','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (38,'router','net','entrasys','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (39,'interface','net','entrasys','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (40,'ping','net','radware','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (41,'cpu','net','radware','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (42,'memory','net','radware','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (43,'flash','net','radware','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (44,'temperature','net','radware','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (45,'fan','net','radware','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (46,'power','net','radware','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (47,'voltage','net','radware','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (48,'systemgroup','net','radware','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (49,'ipmac','net','radware','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (50,'fdb','net','radware','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (51,'router','net','radware','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (52,'interface','net','radware','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (53,'ping','net','maipu','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (54,'cpu','net','maipu','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (55,'memory','net','maipu','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (56,'flash','net','maipu','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (57,'temperature','net','maipu','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (58,'fan','net','maipu','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (59,'power','net','maipu','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (60,'voltage','net','maipu','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (61,'systemgroup','net','maipu','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (62,'ipmac','net','maipu','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (63,'fdb','net','maipu','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (64,'router','net','maipu','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (65,'interface','net','maipu','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (66,'ping','net','redgiant','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (67,'cpu','net','redgiant','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (68,'memory','net','redgiant','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (69,'flash','net','redgiant','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (70,'temperature','net','redgiant','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (71,'fan','net','redgiant','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (72,'power','net','redgiant','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (73,'voltage','net','redgiant','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (74,'systemgroup','net','redgiant','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (75,'ipmac','net','redgiant','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (76,'fdb','net','redgiant','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (77,'router','net','redgiant','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (78,'interface','net','redgiant','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (79,'ping','net','northtel','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (80,'cpu','net','northtel','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (81,'memory','net','northtel','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (82,'flash','net','northtel','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (83,'temperature','net','northtel','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (84,'fan','net','northtel','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (85,'power','net','northtel','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (86,'voltage','net','northtel','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (87,'systemgroup','net','northtel','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (88,'ipmac','net','northtel','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (89,'fdb','net','northtel','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (90,'router','net','northtel','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (91,'interface','net','northtel','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (92,'ping','net','dlink','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (93,'cpu','net','dlink','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (94,'memory','net','dlink','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (95,'flash','net','dlink','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (96,'temperature','net','dlink','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (97,'fan','net','dlink','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (98,'power','net','dlink','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (99,'voltage','net','dlink','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (100,'systemgroup','net','dlink','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (101,'ipmac','net','dlink','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (102,'fdb','net','dlink','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (103,'router','net','dlink','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (104,'interface','net','dlink','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (105,'ping','net','bdcom','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (106,'cpu','net','bdcom','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (107,'memory','net','bdcom','memory','内存利用率','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (108,'flash','net','bdcom','flash','FLASH利用率','flash','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (109,'temperature','net','bdcom','temperature','温度','temperature','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (110,'fan','net','bdcom','fan','风扇','fan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (111,'power','net','bdcom','power','电源','power','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (112,'voltage','net','bdcom','voltage','电压','voltage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (113,'systemgroup','net','bdcom','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (114,'ipmac','net','bdcom','ipmac','MAC地址表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (115,'fdb','net','bdcom','fdb','地址转发表','fdb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (116,'router','net','bdcom','router','路由表','router','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (117,'interface','net','bdcom','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (118,'ping','host','windows','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (119,'cpu','host','windows','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (120,'disk','host','windows','disk','磁盘信息','disk','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (121,'service','host','windows','service','服务信息','service','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (122,'physicalmemory','host','windows','physicalmemory','物理内存','physicalmemory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (123,'virtualmemory','host','windows','virtualmemory','虚拟内存','virtualmemory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (124,'process','host','windows','process','进程信息','process','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (125,'hardware','host','windows','hardware','硬件信息','hardware','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (126,'storage','host','windows','storage','存储信息','storage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (127,'ipmac','host','windows','ipmac','MAC信息表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (128,'interface','host','windows','numbererface','接口信息','numbererface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (129,'software','host','windows','software','软件信息','software','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (130,'systemgroup','host','windows','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (131,'session','db','oracle','session','会话信息','session','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (132,'tablespace','db','oracle','tablespace','表空间信息','tablespace','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (133,'rollback','db','oracle','rollback','回滚段信息','rollback','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (134,'sysinfo','db','oracle','sysinfo','系统信息','sysinfo','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (135,'sga','db','oracle','sga','SGA信息','sga','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (136,'pga','db','oracle','pga','PGA信息','pga','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (137,'lock','db','oracle','lock','锁信息','lock','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (138,'buffercache','db','oracle','buffercache','缓存命中率','buffercache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (139,'dictionarycache','db','oracle','dictionarycache','数据字典命中率','dictionarycache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (140,'librarycache','db','oracle','librarycache','库缓存命中率','librarycache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (141,'pctmemorysorts','db','oracle','pctmemorysorts','内存中的排序','pctmemorysorts','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (142,'pctbufgets','db','oracle','pctbufgets','最浪费内存的前10个语句占全部内存读取量的比例','pctbufgets','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (143,'opencur','db','oracle','opencur','打开的游标','opencur','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (144,'curconnect','db','oracle','curconnect','当前连接的游标','curconnect','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (145,'table','db','oracle','table','表信息','table','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (146,'topsql','db','oracle','topsql','最浪费内存的前10个语句','topsql','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (147,'controlfile','db','oracle','controlfile','控制文件','controlfile','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (148,'log','db','oracle','log','日志文件信息','log','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (149,'keepobj','db','oracle','keepobj','固定缓存对象','keepobj','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (150,'openmode','db','oracle','openmode','监听状态','openmode','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (151,'extent','db','oracle','extent','字典管理表空间中的Extent总数','extent','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (152,'dbio','db','oracle','dbio','数据库I/O状况','dbio','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (153,'wait','db','oracle','wait','WAIT状况','wait','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (154,'user','db','oracle','user','用户信息','user','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (155,'ping','db','oracle','ping','连通性','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (156,'ping','db','sqlserver','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (157,'sysvalue','db','sqlserver','sysvalue','系统信息','sysvalue','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (158,'lock','db','sqlserver','lock','锁信息','lock','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (159,'process','db','sqlserver','process','数据库进程信息','process','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (160,'db','db','sqlserver','db','库信息','db','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (161,'bufferhit','db','sqlserver','bufferhit','缓存击中率','bufferhit','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (162,'planhit','db','sqlserver','planhit','plan cache击中率','planhit','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (163,'cursorhit','db','sqlserver','cursor','Cursor Manager by Type击中率','cursor','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (164,'cataloghit','db','sqlserver','cataloghit','Catalog Metadata击中率','cataloghit','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (165,'error','db','sqlserver','error','错误信息','error','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (166,'cursor','db','sqlserver','cursor','指针信息','cursor','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (167,'page','db','sqlserver','page','页信息','page','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (168,'wait','db','sqlserver','wait','等待信息','wait','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (169,'connect','db','sqlserver','connect','连接信息','connect','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (170,'cache','db','sqlserver','cache','缓存信息','cache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (171,'memory','db','sqlserver','memory','内存利用情况','memory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (172,'sql','db','sqlserver','sql','SQL统计','sql','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (173,'scan','db','sqlserver','scan','扫描统计','scan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (174,'sysvalue','db','informix','sysvalue','系统信息','sysvalue','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (175,'log','db','informix','log','日志信息','log','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (176,'space','db','informix','space','表空间信息','space','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (177,'config','db','informix','config','配置信息','config','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (178,'session','db','informix','session','会话信息','session','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (179,'lock','db','informix','lock','锁信息','lock','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (180,'io','db','informix','io','IO信息','io','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (181,'profile','db','informix','profile','概要文件信息','profile','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (182,'ping','db','informix','ping','连通性','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (183,'cpu','db','sybase','cpu','CPU信息','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (184,'ping','db','sybase','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (185,'version','db','sybase','version','版本','version','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (186,'io','db','sybase','io','输入输出','io','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (187,'pack','db','sybase','pack','网络上的数据速率','pack','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (188,'diskpack','db','sybase','diskpack','磁盘速率','diskpack','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (189,'servername','db','sybase','servername','SQL服务器名称','servername','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (190,'sysdevices','db','sybase','sysdevices','转储设备或数据库设备个数','sysdevices','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (191,'lock','db','sybase','lock','锁信息','lock','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (192,'systransactions','db','sybase','systransactions','事务的个数','systransactions','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (193,'totaldatacache','db','sybase','totaldatacache','总数据高速缓存大小','totaldatacache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (194,'totalphysicalmemory','db','sybase','totalphysicalmemory','总物理内存大小','totalphysicalmemory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (195,'metadatacache','db','sybase','metadatacache','Metadata缓存','metadatacache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (196,'procedurecache','db','sybase','procedurecache','存储过程缓存大小','procedurecache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (197,'totallogicalmemory','db','sybase','totallogicalmemory','总逻辑内存大小',NULL,'1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (198,'datahitrate','db','sybase','datahitrate','数据缓存匹配度','datahitrate','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (199,'procedurehitrate','db','sybase','procedurehitrate','存储缓存匹配度','procedurehitrate','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (200,'device','db','sybase','device','转储设备或数据库设备信息','device','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (201,'user','db','sybase','user','当前数据库所有用户的信息','user','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (202,'db','db','sybase','db','数据库大小信息','db','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (203,'servers','db','sybase','servers','远程服务器信息','servers','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1000,'locktotal','db','sqlserver','locktotal','锁汇总信息','locktotal','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1020,'sysvalue','db','db2','sysvalue','系统信息','sysvalue','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1021,'space','db','db2','space','表空间信息','space','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1022,'pool','db','db2','pool','池信息','pool','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1023,'session','db','db2','session','会话信息','session','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1024,'lock','db','db2','lock','锁信息','lock','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1025,'topread','db','db2','topread','读频率高的表','topread','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1026,'topwrite','db','db2','topwrite','写频率高的表','topwrite','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1040,'conn','db','db2','conn','连接信息','conn','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1041,'log','db','db2','log','日志使用信息','log','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1042,'config','db','mysql','config','配置信息','config','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1043,'tablestatus','db','mysql','tablestatus','表信息','tablestatus','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1044,'process','db','mysql','process','进程信息','process','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1045,'maxusedconnect','db','mysql','maxusedconnect','最大连接数','maxusedconnect','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1046,'lock','db','mysql','lock','锁信息','lock','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1047,'keyread','db','mysql','keyread','读索引信息','keyread','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1048,'slow','db','mysql','slow','慢查询','slow','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1049,'thread','db','mysql','thread','线程信息','thread','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1050,'opentable','db','mysql','opentable','打开表信息','opentable','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1051,'handlerread','db','mysql','handlerread','索引扫描信息','handlerread','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1052,'variables','db','mysql','variables','变量信息','variables','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1053,'status','db','mysql','status','状态信息','status','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1054,'scan','db','mysql','scan','扫描信息','scan','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1055,'createdtmp','db','mysql','createdtmp','创建的临时表信息','createdtmp','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1056,'tmptable','db','mysql','tmptable','临时表创建信息','tmptable','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1057,'ping','service','url','ping','可用性','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1058,'responsetime','service','url','responsetime','响应时间','responsetime','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1059,'pagesize','service','url','pagesize','页面大小','pagesize','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1060,'keyword','service','url','keyword','关键字检测','keyword','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1061,'connect','service','socket','connect','SOCKET服务检测','connect','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1062,'send','service','mail','send','发送邮件','send','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1063,'receieve','service','mail','receieve','接收邮件','receieve','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1064,'upload','service','ftp','upload','上载服务','upload','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1065,'download','service','ftp','download','下载服务','download','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1066,'domain','middleware','weblogic','domain','域信息','domain','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1067,'queue','middleware','weblogic','queue','队列信息','queue','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1068,'jdbc','middleware','weblogic','jdbc','JDBC信息','jdbc','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1069,'webapp','middleware','weblogic','webapp','应用信息','webapp','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1070,'heap','middleware','weblogic','heap','堆信息','heap','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1071,'servlet','middleware','weblogic','servlet','SERVLET信息','servlet','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1072,'server','middleware','weblogic','server','服务信息','server','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1073,'system','middleware','was','system','性能信息','system','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1074,'jdbc','middleware','was','jdbc','JDBC信息','jdbc','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1075,'session','middleware','was','session','SERVLET信息','session','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1076,'jvm','middleware','was','jvm','JVM信息','jvm','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1077,'cache','middleware','was','cache','缓存信息','cache','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1078,'thread','middleware','was','thread','线程信息','thread','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1079,'orb','middleware','was','orb','事务信息','orb','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1080,'ping','middleware','was','ping','可用性信息','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1081,'ping','middleware','weblogic','ping','可用性信息','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1082,'service','middleware','was','service','服务信息','service','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1083,'system','middleware','tomcat','system','系统信息','system','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1084,'ping','middleware','tomcat','ping','可用性','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1085,'jvm','middleware','tomcat','jvm','JVM信息','jvm','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1086,'system','host','as400','system','系统信息','system','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1087,'pool','host','as400','pool','池信息','pool','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1088,'disk','host','as400','disk','磁盘信息','disk','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1089,'job','host','as400','job','任务信息','job','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1090,'ping','host','as400','ping','可用性','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1091,'ping','host','linux','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1092,'disk','host','linux','disk','磁盘信息','disk','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1093,'service','host','linux','service','服务信息','service','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1094,'physicalmemory','host','linux','physicalmemory','物理内存','physicalmemory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1095,'virtualmemory','host','linux','virtualmemory','虚拟内存','virtualmemory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1096,'process','host','linux','process','进程信息','process','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1097,'hardware','host','linux','hardware','硬件信息','hardware','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1098,'storage','host','linux','storage','存储信息','storage','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1099,'ipmac','host','linux','ipmac','MAC信息表','ipmac','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1100,'interface','host','linux','interface','接口信息','interface','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1101,'software','host','linux','software','软件信息','software','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1102,'systemgroup','host','linux','systemgroup','系统组属性','systemgroup','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1103,'subsystem','host','as400','subsystem','子系统','subsystem','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1104,'ping','host','aix','ping','可用性','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1105,'cpu','host','aix','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1106,'disk','host','aix','disk','磁盘信息','disk','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1107,'physicalmemory','host','aix','physicalmemory','物理内存信息','physicalmemory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1108,'swapmemory','host','aix','swapmemory','交换内存信息','swapmemory','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1109,'process','host','aix','process','进程信息','process','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1110,'page','host','aix','page','页信息','page','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1111,'ping','host','solaris','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1112,'ping','firewall','tos','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1113,'cpu','net','zte','cpu','CPU利用率','cpu','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1114,'ping','net','zte','ping','连通率','ping','1','1','5','m');
insert into `nms_gather_indicators` (`id`,`name`,`type`,`subtype`,`alias`,`description`,`category`,`isDefault`,`isCollection`,`poll_interval`,`interval_unit`) values (1115,'interface','net','zte','interface','接口信息','interface','1','1','5','m');

/*Table structure for table `nms_gather_indicators_node` */

DROP TABLE IF EXISTS `nms_gather_indicators_node`;

CREATE TABLE `nms_gather_indicators_node` (
  `id` int(11) NOT NULL auto_increment,
  `nodeid` varchar(255) character set gb2312 default NULL,
  `name` varchar(255) character set gb2312 default NULL,
  `type` varchar(255) character set gb2312 default NULL,
  `subtype` varchar(255) character set gb2312 default NULL,
  `alias` varchar(255) character set gb2312 default NULL,
  `description` varchar(255) character set gb2312 default NULL,
  `category` varchar(255) character set gb2312 default NULL,
  `isDefault` varchar(255) character set gb2312 default NULL,
  `isCollection` varchar(255) character set gb2312 default NULL,
  `poll_interval` varchar(255) character set gb2312 default NULL,
  `interval_unit` varchar(5) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_gather_indicators_node` */

/*Table structure for table `nms_grapes` */

DROP TABLE IF EXISTS `nms_grapes`;

CREATE TABLE `nms_grapes` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `thevalue` varchar(2) default NULL,
  `collecttime` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_grapes` */

/*Table structure for table `nms_grapesconfig` */

DROP TABLE IF EXISTS `nms_grapesconfig`;

CREATE TABLE `nms_grapesconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `supperdir` varchar(200) default NULL,
  `subdir` varchar(200) default NULL,
  `subfilesum` varchar(100) default NULL,
  `filesize` int(5) default NULL,
  `sendmobiles` varchar(200) default NULL,
  `sendemail` varchar(200) default NULL,
  `netid` varchar(100) default NULL,
  `mon_flag` int(2) default NULL,
  `name` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_grapesconfig` */

/*Table structure for table `nms_hint_line` */

DROP TABLE IF EXISTS `nms_hint_line`;

CREATE TABLE `nms_hint_line` (
  `id` int(10) NOT NULL auto_increment,
  `line_id` varchar(20) default NULL,
  `father_id` varchar(50) default NULL,
  `child_id` varchar(50) default NULL,
  `xmlfile` varchar(100) default NULL,
  `line_name` varchar(100) default NULL,
  `width` int(10) default NULL,
  `father_xy` varchar(50) default NULL,
  `child_xy` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_hint_line` */

/*Table structure for table `nms_hint_node` */

DROP TABLE IF EXISTS `nms_hint_node`;

CREATE TABLE `nms_hint_node` (
  `id` int(10) NOT NULL auto_increment,
  `node_id` varchar(20) default NULL,
  `xml_file` varchar(100) default NULL,
  `node_type` varchar(100) default NULL,
  `image` varchar(100) default NULL,
  `name` varchar(50) default NULL,
  `alias` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_hint_node` */

insert into `nms_hint_node` (`id`,`node_id`,`xml_file`,`node_type`,`image`,`name`,`alias`) values (12,'hin147','submap1294213842.jsp','null','/afunms/resource/image/topo/jigui/1.png','去','去');

/*Table structure for table `nms_host_service_group` */

DROP TABLE IF EXISTS `nms_host_service_group`;

CREATE TABLE `nms_host_service_group` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) character set gb2312 default NULL,
  `nodeid` varchar(255) character set gb2312 default NULL,
  `ipaddress` varchar(255) character set gb2312 default NULL,
  `mon_flag` varchar(255) character set gb2312 default NULL,
  `alarm_level` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_host_service_group` */

/*Table structure for table `nms_host_service_group_config` */

DROP TABLE IF EXISTS `nms_host_service_group_config`;

CREATE TABLE `nms_host_service_group_config` (
  `id` int(11) NOT NULL auto_increment,
  `group_id` varchar(255) character set gb2312 default NULL,
  `name` varchar(255) character set gb2312 default NULL,
  `status` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_host_service_group_config` */

/*Table structure for table `nms_hua3vpncfg` */

DROP TABLE IF EXISTS `nms_hua3vpncfg`;

CREATE TABLE `nms_hua3vpncfg` (
  `id` int(11) default NULL,
  `ipaddress` varchar(30) default NULL,
  `fileName` varchar(200) default NULL,
  `descri` varchar(200) default NULL,
  `backup_time` datetime default NULL,
  `file_size` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_hua3vpncfg` */

/*Table structure for table `nms_iisconfig` */

DROP TABLE IF EXISTS `nms_iisconfig`;

CREATE TABLE `nms_iisconfig` (
  `id` bigint(11) NOT NULL,
  `name` varchar(100) default NULL,
  `ipaddress` varchar(19) default NULL,
  `community` varchar(100) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `mon_flag` int(11) default NULL,
  `netid` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_iisconfig` */

/*Table structure for table `nms_iislogconfig` */

DROP TABLE IF EXISTS `nms_iislogconfig`;

CREATE TABLE `nms_iislogconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `history_row` int(10) default NULL,
  `flag` int(2) default NULL,
  `name` varchar(100) default NULL,
  `netid` varbinary(50) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_iislogconfig` */

/*Table structure for table `nms_informixabout` */

DROP TABLE IF EXISTS `nms_informixabout`;

CREATE TABLE `nms_informixabout` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `name` varchar(50) default NULL,
  `value` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixabout` */

/*Table structure for table `nms_informixconfig` */

DROP TABLE IF EXISTS `nms_informixconfig`;

CREATE TABLE `nms_informixconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `cf_default` varchar(200) default NULL,
  `cf_original` varchar(200) default NULL,
  `cf_name` varchar(200) default NULL,
  `cf_effective` varchar(200) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixconfig` */

/*Table structure for table `nms_informixdatabase` */

DROP TABLE IF EXISTS `nms_informixdatabase`;

CREATE TABLE `nms_informixdatabase` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `bufflog` varchar(50) default NULL,
  `createtime` varchar(50) default NULL,
  `log` varchar(50) default NULL,
  `dbserver` varchar(50) default NULL,
  `gls` varchar(50) default NULL,
  `createuser` varchar(50) default NULL,
  `ansi` varchar(50) default NULL,
  `dbname` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixdatabase` */

/*Table structure for table `nms_informixio` */

DROP TABLE IF EXISTS `nms_informixio`;

CREATE TABLE `nms_informixio` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `pagesread` varchar(50) default NULL,
  `readsstr` varchar(50) default NULL,
  `writes` varchar(50) default NULL,
  `mwrites` varchar(50) default NULL,
  `chunknum` varchar(50) default NULL,
  `mreads` varchar(50) default NULL,
  `pageswritten` varchar(50) default NULL,
  `mpagesread` varchar(50) default NULL,
  `mpageswritten` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixio` */

/*Table structure for table `nms_informixlock` */

DROP TABLE IF EXISTS `nms_informixlock`;

CREATE TABLE `nms_informixlock` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `username` varchar(50) default NULL,
  `hostname` varchar(50) default NULL,
  `dbsname` varchar(50) default NULL,
  `tabname` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixlock` */

/*Table structure for table `nms_informixlog` */

DROP TABLE IF EXISTS `nms_informixlog`;

CREATE TABLE `nms_informixlog` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `is_backed_up` varchar(50) default NULL,
  `is_current` varchar(50) default NULL,
  `size` varchar(50) default NULL,
  `used` varchar(50) default NULL,
  `is_temp` varchar(50) default NULL,
  `uniqid` varchar(50) default NULL,
  `is_archived` varchar(50) default NULL,
  `is_used` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixlog` */

/*Table structure for table `nms_informixoffset` */

DROP TABLE IF EXISTS `nms_informixoffset`;

CREATE TABLE `nms_informixoffset` (
  `id` bigint(11) NOT NULL auto_increment,
  `dbnodeid` varchar(50) default NULL,
  `lastoffset` varchar(15) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixoffset` */

/*Table structure for table `nms_informixsession` */

DROP TABLE IF EXISTS `nms_informixsession`;

CREATE TABLE `nms_informixsession` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `bufwrites` varchar(50) default NULL,
  `pagwrites` varchar(50) default NULL,
  `pagreads` varchar(50) default NULL,
  `locksheld` varchar(50) default NULL,
  `bufreads` varchar(50) default NULL,
  `access` varchar(50) default NULL,
  `connected` varchar(50) default NULL,
  `username` varchar(50) default NULL,
  `lktouts` varchar(50) default NULL,
  `lockreqs` varchar(50) default NULL,
  `hostname` varchar(50) default NULL,
  `lockwts` varchar(50) default NULL,
  `deadlks` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixsession` */

/*Table structure for table `nms_informixspace` */

DROP TABLE IF EXISTS `nms_informixspace`;

CREATE TABLE `nms_informixspace` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `owner` varchar(50) default NULL,
  `pages_free` varchar(50) default NULL,
  `dbspace` varchar(50) default NULL,
  `pages_size` varchar(50) default NULL,
  `pages_used` varchar(50) default NULL,
  `file_name` varchar(200) default NULL,
  `fname` varchar(200) default NULL,
  `percent_free` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixspace` */

/*Table structure for table `nms_informixstatus` */

DROP TABLE IF EXISTS `nms_informixstatus`;

CREATE TABLE `nms_informixstatus` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `status` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_informixstatus` */

/*Table structure for table `nms_interface_data_temp` */

DROP TABLE IF EXISTS `nms_interface_data_temp`;

CREATE TABLE `nms_interface_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_interface_data_temp` */

/*Table structure for table `nms_ip_change` */

DROP TABLE IF EXISTS `nms_ip_change`;

CREATE TABLE `nms_ip_change` (
  `id` int(10) NOT NULL,
  `address` varchar(20) default NULL,
  `ip_long` bigint(10) default NULL,
  `message` varchar(100) default NULL,
  `tag` tinyint(1) default '0',
  `log_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ip_change` */

/*Table structure for table `nms_ip_district_match` */

DROP TABLE IF EXISTS `nms_ip_district_match`;

CREATE TABLE `nms_ip_district_match` (
  `id` int(11) NOT NULL auto_increment,
  `relateipaddr` varchar(255) character set gb2312 default NULL,
  `node_ip` varchar(255) character set gb2312 default NULL,
  `node_name` varchar(255) character set gb2312 default NULL,
  `is_online` varchar(255) character set gb2312 default NULL,
  `original_district` varchar(255) character set gb2312 default NULL,
  `current_district` varchar(255) character set gb2312 default NULL,
  `is_match` varchar(255) character set gb2312 default NULL,
  `time` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_ip_district_match` */

/*Table structure for table `nms_ip_mac` */

DROP TABLE IF EXISTS `nms_ip_mac`;

CREATE TABLE `nms_ip_mac` (
  `id` int(10) NOT NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(10) default NULL,
  `mac` varchar(20) default NULL,
  `dept` varchar(50) default NULL,
  `room` varchar(10) default NULL,
  `person` varchar(30) default NULL,
  `tel` varchar(30) default NULL,
  `log_time` bigint(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ip_mac` */

/*Table structure for table `nms_ipdistrict` */

DROP TABLE IF EXISTS `nms_ipdistrict`;

CREATE TABLE `nms_ipdistrict` (
  `id` bigint(11) NOT NULL auto_increment,
  `district_id` int(10) default NULL,
  `startip` varchar(50) default ' ',
  `endip` varchar(50) default ' ',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ipdistrict` */

/*Table structure for table `nms_ipmacbase` */

DROP TABLE IF EXISTS `nms_ipmacbase`;

CREATE TABLE `nms_ipmacbase` (
  `id` bigint(11) NOT NULL auto_increment,
  `relateipaddr` varchar(30) default NULL,
  `ifindex` varchar(30) default NULL,
  `ipaddress` varchar(30) default NULL,
  `mac` varchar(20) default NULL,
  `ifband` int(2) default NULL,
  `ifsms` varchar(2) default NULL,
  `iftel` varchar(2) default NULL,
  `ifemail` varchar(2) default NULL,
  `bak` varchar(100) default NULL,
  `employee_id` bigint(11) default NULL,
  `collecttime` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ipmacbase` */

/*Table structure for table `nms_ipmacchange` */

DROP TABLE IF EXISTS `nms_ipmacchange`;

CREATE TABLE `nms_ipmacchange` (
  `ID` bigint(11) NOT NULL auto_increment,
  `RELATEIPADDR` varchar(30) default NULL,
  `IFINDEX` varchar(30) default NULL,
  `IPADDRESS` varchar(30) default NULL,
  `MAC` varchar(20) default NULL,
  `changetype` int(2) default NULL,
  `detail` varchar(100) default NULL,
  `COLLECTTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `BAK` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ipmacchange` */

/*Table structure for table `nms_jboss_history` */

DROP TABLE IF EXISTS `nms_jboss_history`;

CREATE TABLE `nms_jboss_history` (
  `id` int(20) NOT NULL auto_increment,
  `jboss_id` int(10) default NULL,
  `is_canconnected` int(10) default NULL,
  `reason` varchar(255) default NULL,
  `mon_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_jboss_history` */

/*Table structure for table `nms_jboss_realtime` */

DROP TABLE IF EXISTS `nms_jboss_realtime`;

CREATE TABLE `nms_jboss_realtime` (
  `id` int(20) NOT NULL auto_increment,
  `jboss_id` int(10) default NULL,
  `is_canconnected` int(10) default NULL,
  `reason` varchar(255) default NULL,
  `mon_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `sms_sign` int(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_jboss_realtime` */

/*Table structure for table `nms_jbossconfig` */

DROP TABLE IF EXISTS `nms_jbossconfig`;

CREATE TABLE `nms_jbossconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `alias` varchar(100) default NULL,
  `username` varchar(100) default NULL,
  `password` varchar(100) default NULL,
  `ipaddress` varchar(50) default NULL,
  `port` int(20) default NULL,
  `flag` int(10) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `netid` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_jbossconfig` */

/*Table structure for table `nms_lights_data_temp` */

DROP TABLE IF EXISTS `nms_lights_data_temp`;

CREATE TABLE `nms_lights_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_lights_data_temp` */

/*Table structure for table `nms_macconfig` */

DROP TABLE IF EXISTS `nms_macconfig`;

CREATE TABLE `nms_macconfig` (
  `id` int(10) NOT NULL auto_increment,
  `mac` varchar(30) default NULL,
  `discrictid` varchar(100) default NULL,
  `deptid` int(10) default NULL,
  `employeeid` int(10) default NULL,
  `macdesc` varchar(200) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_macconfig` */

/*Table structure for table `nms_machistory` */

DROP TABLE IF EXISTS `nms_machistory`;

CREATE TABLE `nms_machistory` (
  `ID` bigint(11) NOT NULL auto_increment,
  `RELATEIPADDR` varchar(30) default NULL,
  `IFINDEX` varchar(30) default NULL,
  `IPADDRESS` varchar(30) default NULL,
  `MAC` varchar(20) default NULL,
  `thevalue` varchar(1) default '0',
  `COLLECTTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_machistory` */

/*Table structure for table `nms_manage_nodeconfigure` */

DROP TABLE IF EXISTS `nms_manage_nodeconfigure`;

CREATE TABLE `nms_manage_nodeconfigure` (
  `id` int(10) NOT NULL auto_increment,
  `name` varchar(50) character set gb2312 default NULL,
  `text` varchar(100) character set gb2312 default NULL,
  `father_id` int(10) default NULL,
  `descn` varchar(100) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `nms_manage_nodeconfigure` */

insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (1,'document','文档',0,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (2,'hardware','硬件',0,'打');
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (3,'software','软件',0,'');
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (4,'ip','IP',0,'');
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (5,'allmend','所有补丁',0,'');
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (6,'allserial','所有序列号',0,'');
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (7,'doc_manage','管理类',1,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (8,'doc_skill','技术类',1,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (9,'doc_project','工程类',1,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (10,'net_router','路由器',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (11,'net_switch','交换机',2,'DFD');
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (12,'net_server','服务器',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (13,'cabinets','机柜',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (14,'desktop	','台式机',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (15,'notemachine','笔记本',2,'RER');
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (16,'printers','打印机',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (17,'scanners','扫描仪',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (18,'tapelibrary','磁带库',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (19,'raid','磁盘阵列',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (20,'opticalswitch','光纤交换机',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (21,'loadbalancing','负载均衡',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (22,'ids','入侵检测',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (23,'firewall','防火墙',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (24,'kvm','KVM',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (25,'ups','UPS',2,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (26,'soft_independent','自主开发软件',3,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (27,'soft_offshore','外包开发软件',3,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (28,'soft_business','商业软件',3,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (29,'soft_os','操作系统',3,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (30,'soft_db','数据库',3,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (31,'soft_midware','中间件',3,NULL);
insert into `nms_manage_nodeconfigure` (`id`,`name`,`text`,`father_id`,`descn`) values (32,'soft_tools','工具软件',3,NULL);

/*Table structure for table `nms_manage_nodetype` */

DROP TABLE IF EXISTS `nms_manage_nodetype`;

CREATE TABLE `nms_manage_nodetype` (
  `id` int(10) NOT NULL,
  `name` varchar(50) default NULL,
  `text` varchar(100) default NULL,
  `father_id` int(10) default NULL,
  `table_name` varchar(50) default NULL,
  `category` varchar(100) default NULL,
  `node_tag` varchar(20) default NULL,
  `url` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_manage_nodetype` */

insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (1,'net_router','路由器',0,'topo_host_node','1','net','/perform.do?action=monitornodelist&flag=1');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (2,'net_switch','交换机',0,'topo_host_node','2,3,7','net','/perform.do?action=monitornodelist&flag=1');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (3,'middleware','中间件',0,NULL,NULL,NULL,'/middleware.do?action=list&flag=1');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (4,'domino','Domino',3,'nms_dominoconfig','','dom','/middleware.do?action=list&flag=1&category=domino');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (5,'net_server','服务器',0,'topo_host_node','4','net','/perform.do?action=monitornodelist&flag=1');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (6,'dbs','数据库',0,'app_db_node','','dbs','/db.do?action=list&flag=1');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (7,'safeequip','安全设备',0,NULL,NULL,NULL,'/perform.do?action=monitornodelist');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (8,'tomcat','Tomcat',3,'app_tomact_node','','tom','/middleware.do?action=list&flag=1&category=tomcat');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (9,'cics','CICS',3,'nms_cicsconfig','','cic','/middleware.do?action=list&flag=1&category=cics');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (10,'mqs','MQ',3,'nms_mqconfig','','mqs','/middleware.do?action=list&flag=1&category=mq');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (11,'was','WAS',3,'nms_wasconfig','','was','/middleware.do?action=list&flag=1&category=was');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (12,'weblogic','Weblogic',3,'nms_weblogicconfig','','web','/middleware.do?action=list&flag=1&category=weblogic');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (13,'net_firewall','防火墙',7,'topo_host_node','8','net','/perform.do?action=monitornodelist');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (14,'services','服务',0,NULL,NULL,NULL,'/service.do?action=list&flag=1');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (15,'mail','邮件服务',14,'nms_emailmonitorconf','','mai','/service.do?action=list&flag=1&category=mail');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (16,'ftp','FTP服务',14,'nms_ftpconfig','','ftp','/service.do?action=list&flag=1&category=ftp');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (17,'wes','WEB服务',14,'nms_urlconfig','','wes','/service.do?action=list&flag=1&category=web');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (19,'ups','UPS',0,'app_ups_node',NULL,'ups','/perform.do?action=monitornodelist');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (20,'iis','IIS',3,'nms_iisconfig','','iis','/middleware.do?action=list&flag=1&category=iis');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (21,'socket','Socket服务',14,'nms_portservice','','soc','/service.do?action=list&flag=1&category=portservice');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (22,'bussiness','业务',0,'topo_manage_xml',NULL,'bus','/perform.do?action=monitornodelist');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (23,'process','进程',14,'nms_procs',NULL,'pro','/perform.do?action=monitornodelist');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (24,'interface','接口',0,'nms_businessnode',NULL,'int','/perform.do?action=monitornodelist');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (25,'storage','存储',0,'nms_storage',NULL,'sto','/perform.do?action=monitornodelist');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (26,'jboss','JBoss',3,'nms_jbossconfig',NULL,'jbo','/middleware.do?action=list&flag=1&category=jboss');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (27,'apache','Apache',3,NULL,NULL,'apa','/middleware.do?action=list&flag=1&category=apache');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (28,'ggsn','GGSN',0,'topo_other_node','91','ggs','/other.do?action=list&flag=1&category=GGSN');
insert into `nms_manage_nodetype` (`id`,`name`,`text`,`father_id`,`table_name`,`category`,`node_tag`,`url`) values (29,'sgsn','SGSN',0,'topo_other_node','92','sgs','/other.do?action=list&flag=1&category=SGSN');

/*Table structure for table `nms_memory_data_temp` */

DROP TABLE IF EXISTS `nms_memory_data_temp`;

CREATE TABLE `nms_memory_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_memory_data_temp` */

/*Table structure for table `nms_menu` */

DROP TABLE IF EXISTS `nms_menu`;

CREATE TABLE `nms_menu` (
  `id` bigint(11) NOT NULL,
  `func_desc` varchar(200) character set gb2312 default NULL,
  `ch_desc` varchar(200) character set gb2312 default NULL,
  `level_desc` varchar(200) character set gb2312 default NULL,
  `father_node` varchar(200) character set gb2312 default NULL,
  `url` varchar(200) character set gb2312 default NULL,
  `img_url` varchar(200) character set gb2312 default NULL,
  `is_current_window` varchar(200) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_menu` */

insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (1,'0A','资源','1',NULL,NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (2,'0A0A','拓扑','2','0A',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (3,'0A0A01','网络拓扑','3','0A0A','topology/network/index.jsp','resource/image/icon_cloud.gif','1');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (4,'0A0A02','主机服务器','3','0A0A','topology/server/index.jsp','resource/image/bmgl.GIF','1');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (5,'0A0B','设备维护','2','0A',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (6,'0A0B02','添加设备','3','0A0B','network.do?action=ready_add','resource/image/addDevice.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (7,'0A0B01','设备列表','3','0A0B','network.do?action=list&jp=1','resource/image/icon_detail.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (8,'0A0B03','端口配置','3','0A0B','portconfig.do?action=list&jp=1','resource/image/manageDev.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (9,'0A0B04','链路信息','3','0A0B','link.do?action=list&jp=1','resource/image/infopoint.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (10,'0A0C','性能监视','2','0A',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (11,'0A0C01','监视对象一览表','3','0A0C','network.do?action=monitornodelist&jp=1','resource/image/hostl.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (12,'0A0C02','指标全局阀值一览表','3','0A0C','network.do?action=monitornodelist&jp=1','resource/image/desktops.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (13,'0A0D','IP/MAC资源','2','0A',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (14,'0A0D01','端口-IP-MAC基线','3','0A0D','network.do?action=monitornodelist&jp=1','resource/image/mac.jpg','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (15,'0A0D02','当前MAC信息','3','0A0D','moid.do?action=allmoidlist&jp=1','resource/image/maccurrent.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (16,'0A0D03','MAC变更历史','3','0A0D','moid.do?action=allmoidlist&jp=1','resource/image/viewmac.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (17,'0A0E','视图管理','2','0A',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (18,'0A0E01','视图编辑','3','0A0E','customxml.do?action=list&jp=1','resource/image/mkdz.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (19,'0A0E02','视图展示','3','0A0E','topology/view/custom.jsp','resource/image/zcbf.gif','1');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (20,'0A0F','设备面板配置管理','2','0A',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (21,'0A0F01','面板模板编辑','3','0A0F','panel.do?action=showaddpanel&jp=1','resource/image/editicon.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (22,'0A0F02','设备面板编辑','3','0A0F','network.do?action=panelnodelist&jp=1','resource/image/manageDev.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (23,'0B','告警','1',NULL,NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (24,'0B0A','告警浏览','2','0B',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (25,'0B0A01','告警列表','3','0B0A','event.do?action=list&jp=1','resource/image/sysloglist16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (26,'0B0A02','存在告警的设备','3','0B0A','alarm/event/alarmnodelist.jsp','resource/image/cancelMng.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (27,'0B0B','告警统计','2','0B',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (28,'0B0B01','按业务分布','3','0B0B','event.do?action=businesslist&jp=1','resource/image/reportList-16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (29,'0B0B02','按设备分布','3','0B0B','event.do?action=equipmentlist&jp=1','resource/image/reportsyspara-16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (30,'0B0C','Trap管理','2','0B',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (31,'0B0C01','浏览Trap','3','0B0C','trap.do?action=list&jp=1','resource/image/integratedReports-16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (32,'0B0D','Syslog管理','2','0B',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (33,'0B0D01','浏览Syslog','3','0B0D','netsyslog.do?action=list&jp=1','resource/image/syslog.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (34,'0B0D02','过滤规则','3','0B0D','netsyslog.do?action=filter&jp=1','resource/image/filter16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (35,'0C','报表','1',NULL,NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (36,'0C0A','报表浏览','2','0C',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (37,'0C0A01','网络设备报表','3','0C0A','netreport.do?action=list&jp=1','resource/image/graph1.png','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (38,'0C0A02','服务器报表','3','0C0A','hostreport.do?action=list&jp=1','resource/image/viewreport.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (39,'0D','应用','1',NULL,NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (40,'0D0A','数据库管理','2','0D',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (41,'0D0A01','数据库类型管理','3','0D0A','dbtype.do?action=list','resource/image/dbtype.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (42,'0D0A02','数据库监视','3','0D0A','db.do?action=list&jp=1','resource/image/db.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (43,'0D0A03','Oracle告警设置','3','0D0A','oraspace.do?action=list&jp=1','resource/image/oracle.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (44,'0D0A04','SQLServer告警设置','3','0D0A','sqldbconfig.do?action=list&jp=1','resource/image/sqlserver.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (45,'0D0A05','DB2告警设置','3','0D0A','db2config.do?action=list&jp=1','resource/image/db2.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (46,'0D0A06','Sybase告警设置','3','0D0A','sybaseconfig.do?action=list&jp=1','resource/image/sybase.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (47,'0D0B','服务管理','2','0D',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (48,'0D0B01','FTP服务监视','3','0D0B','user.do?action=list&jp=1','resource/image/ftp.jpg','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (49,'0D0B02','Email服务监视','3','0D0B','role.do?action=list&jp=1','resource/image/friend.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (50,'0D0B03','主机进程监视','3','0D0B','process.do?action=list&jp=1','resource/image/add-services.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (51,'0D0B04','WEB访问服务监视','3','0D0B','web.do?action=list&jp=1','/resource/image/www.jpg','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (52,'0D0B05','Grapes监视','3','0D0B','grapes.do?action=list&jp=1','/resource/image/tomcat.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (53,'0D0B06','Radar监视','3','0D0B','radar.do?action=list&jp=1','/resource/image/tomcat.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (54,'0D0B07','Plot监视','3','0D0B','plot.do?action=list&jp=1','/resource/image/tomcat.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (55,'0D0C','中间件管理','2','0D',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (56,'0D0C01','MQ监视','3','0D0C','mq.do?action=list&jp=1','/resource/image/mq.jpg','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (57,'0D0C02','MQ告警设置','3','0D0C','mqchannel.do?action=list&jp=1','/resource/image/setalert.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (58,'0D0C03','Domino监视','3','0D0C','domino.do?action=list&jp=1','/resource/image/domino.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (59,'0D0C04','WAS监视','3','0D0C','was.do?action=list&jp=1','/resource/image/webphere.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (60,'0D0C05','Weblogic监视','3','0D0C','weblogic.do?action=list&jp=1','/resource/image/bea.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (61,'0D0C06','Tomcat监视','3','0D0C','tomcat.do?action=list&jp=1','/resource/image/tomcat.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (62,'0E','系统管理','1',NULL,NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (63,'0E0A','资源管理','2','0E',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (64,'0E0A01','SNNP模板','3','0E0A','snmp.do?action=list','resource/image/editicon.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (65,'0E0A02','设备厂商','3','0E0A','producer.do?action=list&jp=1','resource/image/device_vendor-16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (66,'0E0A03','设备型号','3','0E0A','devicetype.do?action=list&jp=1','resource/image/device_type-16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (67,'0E0A04','服务','3','0E0A','service.do?action=list&jp=1','resource/image/vo_service_16.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (68,'0E0B','用户管理','2','0E',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (69,'0E0B01','用户','3','0E0B','user.do?action=list&jp=1','resource/image/zxry.GIF','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (70,'0E0B02','角色','3','0E0B','role.do?action=list&jp=1','resource/image/jsfp.GIF','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (71,'0E0B03','部门','3','0E0B','dept.do?action=list&jp=1','resource/image/bmgl.GIF','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (72,'0E0B04','职位','3','0E0B','position.do?action=list&jp=1','resource/image/jswh.GIF','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (73,'0E0B05','权限设置','3','0E0B','admin.do?action=list&jp=1','resource/image/sqgl.GIF','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (74,'0E0B06','修改密码','3','0E0B','system/user/inputpwd.jsp','resource/image/xgmm.GIF','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (75,'0E0C','系统配置','2','0E',NULL,NULL,'0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (76,'0E0C01','业务分类','3','0E0C','business.do?action=list&jp=1','resource/image/mkdz.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (77,'0E0C02','操作日志','3','0E0C','syslog.do?action=list&jp=1','resource/image/zcbf.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (78,'0E0C03','告警邮箱设置','3','0E0C','alertemail.do?action=list&jp=1','resource/image/friend.gif','0');
insert into `nms_menu` (`id`,`func_desc`,`ch_desc`,`level_desc`,`father_node`,`url`,`img_url`,`is_current_window`) values (79,'0E0C04','TFTP设置','3','0E0C','tftpserver.do?action=list&jp=1','resource/image/ftp.jpg','0');

/*Table structure for table `nms_mqchannelconfig` */

DROP TABLE IF EXISTS `nms_mqchannelconfig`;

CREATE TABLE `nms_mqchannelconfig` (
  `id` bigint(20) NOT NULL auto_increment,
  `ipaddress` varchar(50) default NULL,
  `chlindex` int(20) default NULL,
  `chlname` varchar(50) default NULL,
  `linkuse` varchar(50) default NULL,
  `sms` int(2) default NULL,
  `bak` varchar(1000) default NULL,
  `reportflag` int(2) default NULL,
  `connipaddress` varchar(50) default NULL,
  `netid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_mqchannelconfig` */

/*Table structure for table `nms_mqconfig` */

DROP TABLE IF EXISTS `nms_mqconfig`;

CREATE TABLE `nms_mqconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `ipaddress` varchar(19) default NULL,
  `managername` varchar(100) default NULL,
  `portnum` int(11) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `mon_flag` int(11) default NULL,
  `netid` varchar(100) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_mqconfig` */

/*Table structure for table `nms_mysqlinfo` */

DROP TABLE IF EXISTS `nms_mysqlinfo`;

CREATE TABLE `nms_mysqlinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `variable_name` varchar(200) default NULL,
  `value` varchar(200) default NULL,
  `dbname` varchar(200) default NULL,
  `typename` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_mysqlinfo` */

/*Table structure for table `nms_netnodecfgfile` */

DROP TABLE IF EXISTS `nms_netnodecfgfile`;

CREATE TABLE `nms_netnodecfgfile` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `name` varchar(50) default NULL,
  `recordtime` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_netnodecfgfile` */

/*Table structure for table `nms_netsyslog` */

DROP TABLE IF EXISTS `nms_netsyslog`;

CREATE TABLE `nms_netsyslog` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `hostname` varchar(100) default NULL,
  `message` varchar(1000) default NULL,
  `facility` int(10) default NULL,
  `priority` int(10) default NULL,
  `facilityname` varchar(50) default NULL,
  `priorityname` varchar(50) default NULL,
  `recordtime` timestamp NULL default NULL,
  `businessid` varchar(50) default NULL,
  `category` int(5) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_netsyslog` */

/*Table structure for table `nms_netsyslogrule` */

DROP TABLE IF EXISTS `nms_netsyslogrule`;

CREATE TABLE `nms_netsyslogrule` (
  `id` bigint(20) NOT NULL auto_increment,
  `facility` varchar(100) default NULL,
  `priority` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_netsyslogrule` */

insert into `nms_netsyslogrule` (`id`,`facility`,`priority`) values (1,'0,1,4,5,','');

/*Table structure for table `nms_netsyslogrule_node` */

DROP TABLE IF EXISTS `nms_netsyslogrule_node`;

CREATE TABLE `nms_netsyslogrule_node` (
  `id` bigint(20) NOT NULL auto_increment,
  `nodeid` varchar(100) default NULL,
  `facility` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_netsyslogrule_node` */

/*Table structure for table `nms_node_depend` */

DROP TABLE IF EXISTS `nms_node_depend`;

CREATE TABLE `nms_node_depend` (
  `id` int(10) NOT NULL auto_increment,
  `node_id` varchar(50) default NULL,
  `location` varchar(50) default NULL,
  `xml` varchar(50) default NULL,
  `alias` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_node_depend` */

/*Table structure for table `nms_nodeconfig` */

DROP TABLE IF EXISTS `nms_nodeconfig`;

CREATE TABLE `nms_nodeconfig` (
  `id` int(11) NOT NULL auto_increment,
  `nodeid` int(11) default NULL,
  `hostname` varchar(255) character set gb2312 default NULL,
  `sysname` varchar(255) character set gb2312 default NULL,
  `serialNumber` varchar(255) character set gb2312 default NULL,
  `cSDVersion` varchar(255) character set gb2312 default NULL,
  `numberOfProcessors` varchar(255) character set gb2312 default NULL,
  `mac` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_nodeconfig` */

/*Table structure for table `nms_nodecpuconfig` */

DROP TABLE IF EXISTS `nms_nodecpuconfig`;

CREATE TABLE `nms_nodecpuconfig` (
  `id` int(11) NOT NULL auto_increment,
  `nodeid` int(11) default NULL,
  `dataWidth` varchar(255) default NULL,
  `processorId` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `l2CacheSize` varchar(255) default NULL,
  `l2CacheSpeed` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_nodecpuconfig` */

/*Table structure for table `nms_nodediskconfig` */

DROP TABLE IF EXISTS `nms_nodediskconfig`;

CREATE TABLE `nms_nodediskconfig` (
  `id` int(11) NOT NULL auto_increment,
  `nodeid` int(11) default NULL,
  `bytesPerSector` varchar(255) default NULL,
  `caption` varchar(255) default NULL,
  `interfaceType` varchar(255) default NULL,
  `size` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_nodediskconfig` */

/*Table structure for table `nms_nodememconfig` */

DROP TABLE IF EXISTS `nms_nodememconfig`;

CREATE TABLE `nms_nodememconfig` (
  `id` int(11) NOT NULL auto_increment,
  `nodeid` int(11) default NULL,
  `totalVisibleMemorySize` varchar(255) default NULL,
  `totalVirtualMemorySize` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_nodememconfig` */

/*Table structure for table `nms_orabanner` */

DROP TABLE IF EXISTS `nms_orabanner`;

CREATE TABLE `nms_orabanner` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `detail` varchar(300) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orabanner` */

/*Table structure for table `nms_oracle_sids` */

DROP TABLE IF EXISTS `nms_oracle_sids`;

CREATE TABLE `nms_oracle_sids` (
  `id` int(11) NOT NULL auto_increment,
  `dbid` int(11) default NULL,
  `sid` varchar(64) character set gb2312 default NULL,
  `user` varchar(64) character set gb2312 default NULL,
  `password` varchar(64) character set gb2312 default NULL,
  `gzerid` varchar(128) character set gb2312 default NULL,
  `collecttype` int(2) default NULL,
  `alias` varchar(32) character set gb2312 default NULL,
  `managed` int(2) default NULL,
  `bid` varchar(100) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_oracle_sids` */

/*Table structure for table `nms_oracontrfile` */

DROP TABLE IF EXISTS `nms_oracontrfile`;

CREATE TABLE `nms_oracontrfile` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `status` varchar(50) default NULL,
  `name` varchar(200) default NULL,
  `is_recovery_dest_file` varchar(50) default NULL,
  `block_size` varchar(50) default NULL,
  `file_size_blks` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oracontrfile` */

/*Table structure for table `nms_oracursors` */

DROP TABLE IF EXISTS `nms_oracursors`;

CREATE TABLE `nms_oracursors` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `opencur` varchar(50) default NULL,
  `curconnect` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oracursors` */

/*Table structure for table `nms_oradbio` */

DROP TABLE IF EXISTS `nms_oradbio`;

CREATE TABLE `nms_oradbio` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `name` varchar(50) default NULL,
  `filename` varchar(200) default NULL,
  `pyr` varchar(50) default NULL,
  `pbr` varchar(50) default NULL,
  `pyw` varchar(50) default NULL,
  `pbw` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oradbio` */

/*Table structure for table `nms_oraextent` */

DROP TABLE IF EXISTS `nms_oraextent`;

CREATE TABLE `nms_oraextent` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `tablespace_name` varchar(200) default NULL,
  `extents` varchar(200) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oraextent` */

/*Table structure for table `nms_oraisarchive` */

DROP TABLE IF EXISTS `nms_oraisarchive`;

CREATE TABLE `nms_oraisarchive` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `created` varchar(50) default NULL,
  `log_mode` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oraisarchive` */

/*Table structure for table `nms_orakeepobj` */

DROP TABLE IF EXISTS `nms_orakeepobj`;

CREATE TABLE `nms_orakeepobj` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `owner` varchar(50) default NULL,
  `name` varchar(200) default NULL,
  `db_link` varchar(50) default NULL,
  `namespace` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `sharable_mem` varchar(50) default NULL,
  `loads` varchar(50) default NULL,
  `executions` varchar(50) default NULL,
  `locks` varchar(50) default NULL,
  `pins` varchar(50) default NULL,
  `kept` varchar(50) default NULL,
  `child_latch` varchar(50) default NULL,
  `invalidations` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orakeepobj` */

/*Table structure for table `nms_oralock` */

DROP TABLE IF EXISTS `nms_oralock`;

CREATE TABLE `nms_oralock` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `username` varchar(15) default NULL,
  `status` varchar(50) default NULL,
  `machine` varchar(50) default NULL,
  `sessiontype` varchar(50) default NULL,
  `logontime` varchar(200) default NULL,
  `program` varchar(50) default NULL,
  `locktype` varchar(50) default NULL,
  `lmode` varchar(50) default NULL,
  `requeststr` varchar(200) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oralock` */

/*Table structure for table `nms_oralogfile` */

DROP TABLE IF EXISTS `nms_oralogfile`;

CREATE TABLE `nms_oralogfile` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `groupstr` varchar(200) default NULL,
  `status` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `member` varchar(200) default NULL,
  `is_recovery_dest_file` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oralogfile` */

/*Table structure for table `nms_oramemperfvalue` */

DROP TABLE IF EXISTS `nms_oramemperfvalue`;

CREATE TABLE `nms_oramemperfvalue` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `pctmemorysorts` varchar(50) default NULL,
  `pctbufgets` varchar(50) default NULL,
  `dictionarycache` varchar(50) default NULL,
  `buffercache` varchar(50) default NULL,
  `librarycache` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oramemperfvalue` */

/*Table structure for table `nms_oramemvalue` */

DROP TABLE IF EXISTS `nms_oramemvalue`;

CREATE TABLE `nms_oramemvalue` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `aggregate_pga_auto_target` varchar(50) default NULL,
  `total_pga_used_for_manual_workareas` varchar(50) default NULL,
  `total_pga_inuse` varchar(50) default NULL,
  `maximum_pga_allocated` varchar(50) default NULL,
  `cache_hit_percentage` varchar(50) default NULL,
  `recycle_buffer_cache` varchar(50) default NULL,
  `keep_buffer_cache` varchar(50) default NULL,
  `process_count` varchar(50) default NULL,
  `total_pga_used_for_auto_workareas` varchar(50) default NULL,
  `asm_buffer_cache` varchar(50) default NULL,
  `over_allocation_count` varchar(50) default NULL,
  `bytes_processed` varchar(50) default NULL,
  `java_pool` varchar(50) default NULL,
  `maximum_pga_used_for_manual_workareas` varchar(50) default NULL,
  `streams_pool` varchar(50) default NULL,
  `default_2k_buffer_cache` varchar(50) default NULL,
  `max_processes_count` varchar(50) default NULL,
  `total_pga_allocated` varchar(50) default NULL,
  `default_4k_buffer_cache` varchar(50) default NULL,
  `shared_pool` varchar(50) default NULL,
  `default_32k_buffer_cache` varchar(50) default NULL,
  `default_buffer_cache` varchar(50) default NULL,
  `large_pool` varchar(50) default NULL,
  `aggregate_pga_target_parameter` varchar(50) default NULL,
  `default_16k_buffer_cache` varchar(50) default NULL,
  `global_memory_bound` varchar(50) default NULL,
  `default_8k_buffer_cache` varchar(50) default NULL,
  `extra_bytes_read_written` varchar(50) default NULL,
  `pga_memory_freed_back_to_os` varchar(50) default NULL,
  `total_freeable_pga_memory` varchar(50) default NULL,
  `recompute_count_total` varchar(50) default NULL,
  `maximum_pga_used_for_auto_workareas` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oramemvalue` */

/*Table structure for table `nms_orarollback` */

DROP TABLE IF EXISTS `nms_orarollback`;

CREATE TABLE `nms_orarollback` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `rollback` varchar(50) default NULL,
  `wraps` varchar(50) default NULL,
  `shrink` varchar(50) default NULL,
  `ashrink` varchar(50) default NULL,
  `extend` varchar(200) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orarollback` */

/*Table structure for table `nms_orasessiondata` */

DROP TABLE IF EXISTS `nms_orasessiondata`;

CREATE TABLE `nms_orasessiondata` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(15) default NULL,
  `dbname` varchar(50) default NULL,
  `machine` varchar(50) default NULL,
  `username` varchar(50) default NULL,
  `program` varchar(200) default NULL,
  `status` varchar(10) default NULL,
  `sessiontype` varchar(10) default NULL,
  `command` varchar(200) default NULL,
  `logontime` timestamp NULL default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orasessiondata` */

/*Table structure for table `nms_oraspaces` */

DROP TABLE IF EXISTS `nms_oraspaces`;

CREATE TABLE `nms_oraspaces` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `tablespace` varchar(15) default NULL,
  `free_mb` varchar(50) default NULL,
  `size_mb` varchar(50) default NULL,
  `percent_free` varchar(50) default NULL,
  `file_name` varchar(200) default NULL,
  `status` varchar(10) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oraspaces` */

/*Table structure for table `nms_orastatus` */

DROP TABLE IF EXISTS `nms_orastatus`;

CREATE TABLE `nms_orastatus` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `lstrnstatu` varchar(50) default NULL,
  `status` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orastatus` */

/*Table structure for table `nms_orasys` */

DROP TABLE IF EXISTS `nms_orasys`;

CREATE TABLE `nms_orasys` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `INSTANCE_NAME` varchar(15) default NULL,
  `HOST_NAME` varchar(50) default NULL,
  `DBNAME` varchar(50) default NULL,
  `VERSION` varchar(50) default NULL,
  `STARTUP_TIME` varchar(200) default NULL,
  `status` varchar(10) default NULL,
  `ARCHIVER` varchar(20) default NULL,
  `BANNER` varchar(20) default NULL,
  `java_pool` varchar(20) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orasys` */

/*Table structure for table `nms_oratables` */

DROP TABLE IF EXISTS `nms_oratables`;

CREATE TABLE `nms_oratables` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `segment_name` varchar(50) default NULL,
  `spaces` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oratables` */

/*Table structure for table `nms_oratopsql` */

DROP TABLE IF EXISTS `nms_oratopsql`;

CREATE TABLE `nms_oratopsql` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `sql_text` varchar(800) default NULL,
  `pct_bufgets` varchar(10) default NULL,
  `username` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_oratopsql` */

/*Table structure for table `nms_orauserinfo` */

DROP TABLE IF EXISTS `nms_orauserinfo`;

CREATE TABLE `nms_orauserinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `parsing_user_id` varchar(50) default NULL,
  `cpu_time` varchar(50) default NULL,
  `sorts` varchar(50) default NULL,
  `buffer_gets` varchar(50) default NULL,
  `runtime_mem` varchar(50) default NULL,
  `version_count` varchar(50) default NULL,
  `disk_reads` varchar(50) default NULL,
  `user` varchar(50) default NULL,
  `username` varchar(50) default NULL,
  `user_id` varchar(50) default NULL,
  `account_status` varchar(50) default NULL,
  `label` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orauserinfo` */

/*Table structure for table `nms_orawait` */

DROP TABLE IF EXISTS `nms_orawait`;

CREATE TABLE `nms_orawait` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `event` varchar(200) default NULL,
  `prev` varchar(50) default NULL,
  `curr` varchar(50) default NULL,
  `tot` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_orawait` */

/*Table structure for table `nms_other_data_temp` */

DROP TABLE IF EXISTS `nms_other_data_temp`;

CREATE TABLE `nms_other_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_other_data_temp` */

/*Table structure for table `nms_ping_data_temp` */

DROP TABLE IF EXISTS `nms_ping_data_temp`;

CREATE TABLE `nms_ping_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_ping_data_temp` */

/*Table structure for table `nms_plot` */

DROP TABLE IF EXISTS `nms_plot`;

CREATE TABLE `nms_plot` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `thevalue` varchar(2) default NULL,
  `collecttime` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_plot` */

/*Table structure for table `nms_plotconfig` */

DROP TABLE IF EXISTS `nms_plotconfig`;

CREATE TABLE `nms_plotconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `supperdir` varchar(200) default NULL,
  `subdir` varchar(200) default NULL,
  `inter` varchar(100) default NULL,
  `filesize` int(5) default NULL,
  `sendmobiles` varchar(200) default NULL,
  `sendemail` varchar(200) default NULL,
  `netid` varchar(100) default NULL,
  `mon_flag` int(2) default NULL,
  `name` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_plotconfig` */

/*Table structure for table `nms_portscan_config` */

DROP TABLE IF EXISTS `nms_portscan_config`;

CREATE TABLE `nms_portscan_config` (
  `id` int(11) NOT NULL auto_increment,
  `ipaddress` varchar(255) character set gb2312 default NULL,
  `port` varchar(255) character set gb2312 default NULL,
  `portName` varchar(255) character set gb2312 default NULL,
  `description` varchar(255) character set gb2312 default NULL,
  `type` varchar(255) character set gb2312 default NULL,
  `timeout` varchar(255) character set gb2312 default NULL,
  `status` varchar(255) character set gb2312 default NULL,
  `isScanned` varchar(255) character set gb2312 default NULL,
  `scantime` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_portscan_config` */

/*Table structure for table `nms_portservice` */

DROP TABLE IF EXISTS `nms_portservice`;

CREATE TABLE `nms_portservice` (
  `id` bigint(20) NOT NULL auto_increment,
  `port` varchar(50) default NULL,
  `portdesc` varchar(50) default NULL,
  `monflag` int(20) default NULL,
  `flag` int(20) default NULL,
  `timeout` int(10) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `bid` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_portservice` */

/*Table structure for table `nms_porttype` */

DROP TABLE IF EXISTS `nms_porttype`;

CREATE TABLE `nms_porttype` (
  `id` bigint(11) NOT NULL auto_increment,
  `typeid` bigint(11) default NULL,
  `chname` varchar(50) default NULL,
  `bak` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_porttype` */

/*Table structure for table `nms_process_data_temp` */

DROP TABLE IF EXISTS `nms_process_data_temp`;

CREATE TABLE `nms_process_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_process_data_temp` */

/*Table structure for table `nms_process_group` */

DROP TABLE IF EXISTS `nms_process_group`;

CREATE TABLE `nms_process_group` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) character set gb2312 default NULL,
  `ipaddress` varchar(255) character set gb2312 default NULL,
  `nodeid` varchar(255) character set gb2312 default NULL,
  `mon_flag` varchar(255) character set gb2312 default NULL,
  `alarm_level` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_process_group` */

/*Table structure for table `nms_process_group_config` */

DROP TABLE IF EXISTS `nms_process_group_config`;

CREATE TABLE `nms_process_group_config` (
  `id` int(11) NOT NULL auto_increment,
  `group_id` varchar(255) character set gb2312 default NULL,
  `name` varchar(255) character set gb2312 default NULL,
  `times` varchar(255) character set gb2312 default NULL,
  `status` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_process_group_config` */

/*Table structure for table `nms_procs` */

DROP TABLE IF EXISTS `nms_procs`;

CREATE TABLE `nms_procs` (
  `id` bigint(11) NOT NULL auto_increment,
  `nodeid` int(11) default NULL,
  `wbstatus` int(2) default NULL,
  `flag` int(2) default NULL,
  `ipaddress` varchar(100) default NULL,
  `procname` varchar(50) default NULL,
  `chname` varchar(50) default NULL,
  `bak` varchar(100) default NULL,
  `collecttime` timestamp NULL default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_procs` */

/*Table structure for table `nms_producer` */

DROP TABLE IF EXISTS `nms_producer`;

CREATE TABLE `nms_producer` (
  `id` int(11) NOT NULL auto_increment,
  `producer` varchar(100) default NULL,
  `enterprise_oid` varchar(20) default '',
  `website` varchar(100) default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_producer` */

insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (1,'微软(Microsoft)','1.3.6.1.4.1.311','www.microsoft.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (2,'惠普(HP)','1.3.6.1.4.1.11','www.hp.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (3,'IBM','1.3.6.1.4.1.2','');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (4,'华为(Huawei)','1.3.6.1.4.1.2011','');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (5,'Avaya','1.3.6.1.4.1.81','www.avaya.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (6,'思科(Cisco)','1.3.6.1.4.1.9','www.cisco.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (7,'凯创(Enterasys)','1.3.6.1.4.1.52','');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (8,'3Com','1.3.6.1.4.1.6296','');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (9,'Sun','1.3.6.1.4.1.42','');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (10,'安奈特(Allied Telesyn)','1.3.6.1.4.1.207','www.alliedtelesis.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (12,'Polycom','1.3.6.1.4.1.274','www.polycom.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (13,'华三(H3C)','1.3.6.1.4.1.25506','');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (14,'Cisco','1.3.6.1.4.1.9.1.278','www.cisco.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (15,'Juniper','1.3.6.1.4.1.3224','');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (16,'天融信','1.3.6.1.4.1.14331','www.topsec.com');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (17,'迈普','1.3.6.1.4.1.5651','http://www.maipu.cn/');
insert into `nms_producer` (`id`,`producer`,`enterprise_oid`,`website`) values (18,'中兴','1.3.6.1.4.1.3902','');

/*Table structure for table `nms_radar` */

DROP TABLE IF EXISTS `nms_radar`;

CREATE TABLE `nms_radar` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `thevalue` varchar(2) default NULL,
  `collecttime` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_radar` */

/*Table structure for table `nms_radarconfig` */

DROP TABLE IF EXISTS `nms_radarconfig`;

CREATE TABLE `nms_radarconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `supperdir` varchar(200) default NULL,
  `subdir` varchar(200) default NULL,
  `inter` varchar(100) default NULL,
  `filesize` int(5) default NULL,
  `sendmobiles` varchar(200) default NULL,
  `sendemail` varchar(200) default NULL,
  `netid` varchar(100) default NULL,
  `mon_flag` int(2) default NULL,
  `name` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_radarconfig` */

/*Table structure for table `nms_remote_ping_host` */

DROP TABLE IF EXISTS `nms_remote_ping_host`;

CREATE TABLE `nms_remote_ping_host` (
  `id` int(11) NOT NULL auto_increment,
  `node_id` varchar(100) default NULL,
  `username` varchar(100) default NULL,
  `password` varchar(100) default NULL,
  `login_prompt` varchar(100) default NULL,
  `password_prompt` varchar(100) default NULL,
  `shell_prompt` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_remote_ping_host` */

/*Table structure for table `nms_remote_ping_node` */

DROP TABLE IF EXISTS `nms_remote_ping_node`;

CREATE TABLE `nms_remote_ping_node` (
  `id` int(11) NOT NULL auto_increment,
  `node_id` varchar(100) character set gb2312 default NULL,
  `child_node_id` varchar(100) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_remote_ping_node` */

/*Table structure for table `nms_role_func` */

DROP TABLE IF EXISTS `nms_role_func`;

CREATE TABLE `nms_role_func` (
  `id` bigint(11) NOT NULL auto_increment,
  `roleid` bigint(11) default NULL,
  `funcid` bigint(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_role_func` */

insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (427,0,1);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (428,0,2);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (429,0,3);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (430,0,4);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (431,0,5);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (432,0,7);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (433,0,6);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (434,0,8);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (435,0,9);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (436,0,10);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (437,0,11);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (438,0,12);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (439,0,13);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (440,0,14);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (441,0,15);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (442,0,16);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (443,0,86);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (444,0,17);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (445,0,18);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (446,0,19);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (448,0,21);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (449,0,22);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (450,0,23);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (451,0,24);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (452,0,25);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (453,0,26);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (454,0,27);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (455,0,28);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (456,0,29);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (457,0,30);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (458,0,31);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (459,0,32);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (460,0,33);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (461,0,34);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (462,0,35);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (463,0,36);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (464,0,37);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (465,0,38);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (466,0,39);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (467,0,40);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (468,0,42);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (469,0,41);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (470,0,43);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (471,0,44);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (472,0,45);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (473,0,46);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (474,0,47);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (475,0,48);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (476,0,49);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (477,0,50);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (478,0,51);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (479,0,52);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (480,0,53);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (481,0,54);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (482,0,55);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (483,0,56);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (484,0,57);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (485,0,58);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (486,0,59);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (487,0,60);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (488,0,61);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (489,0,62);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (490,0,63);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (491,0,64);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (492,0,65);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (493,0,66);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (494,0,67);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (495,0,68);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (496,0,69);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (497,0,70);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (498,0,71);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (499,0,72);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (500,0,73);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (501,0,74);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (502,0,80);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (503,0,81);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (504,0,82);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (505,0,75);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (506,0,76);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (507,0,77);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (508,0,78);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (509,0,79);
insert into `nms_role_func` (`id`,`roleid`,`funcid`) values (510,0,84);

/*Table structure for table `nms_route_data_temp` */

DROP TABLE IF EXISTS `nms_route_data_temp`;

CREATE TABLE `nms_route_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `ifindex` varchar(30) default NULL,
  `nexthop` varchar(30) default NULL,
  `proto` varchar(30) default NULL,
  `rtype` varchar(300) default NULL,
  `mask` varchar(50) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `physaddress` varchar(50) default NULL,
  `dest` varchar(50) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_route_data_temp` */

/*Table structure for table `nms_send_alarm_time` */

DROP TABLE IF EXISTS `nms_send_alarm_time`;

CREATE TABLE `nms_send_alarm_time` (
  `name` varchar(200) character set gb2312 NOT NULL,
  `alarm_way_detail_id` varchar(20) character set gb2312 NOT NULL,
  `send_times` varchar(20) character set gb2312 default NULL,
  `last_send_time` varchar(50) character set gb2312 default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_send_alarm_time` */

/*Table structure for table `nms_sercice_data_temp` */

DROP TABLE IF EXISTS `nms_sercice_data_temp`;

CREATE TABLE `nms_sercice_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `name` varchar(100) default NULL,
  `instate` varchar(30) default NULL,
  `opstate` varchar(30) default NULL,
  `paused` varchar(50) default NULL,
  `uninst` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sercice_data_temp` */

/*Table structure for table `nms_service` */

DROP TABLE IF EXISTS `nms_service`;

CREATE TABLE `nms_service` (
  `id` int(2) NOT NULL,
  `service` varchar(20) default NULL,
  `port` int(5) default NULL,
  `time_out` int(5) default NULL,
  `scan` int(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_service` */

insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (1,'HTTP',80,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (2,'DNS',53,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (3,'SMTP',25,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (4,'FTP',21,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (5,'Telnet',23,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (6,'POP',110,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (7,'Tomcat',8080,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (8,'WebLogic',7001,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (9,'MySQL',3306,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (10,'MSSQL',1433,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (11,'Oracle',1521,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (12,'DB2',8000,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (13,'Sybase',2638,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (14,'Informix',9088,1000,1);
insert into `nms_service` (`id`,`service`,`port`,`time_out`,`scan`) values (15,'SSH',22,1000,1);

/*Table structure for table `nms_smsconfig` */

DROP TABLE IF EXISTS `nms_smsconfig`;

CREATE TABLE `nms_smsconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `objectid` varchar(20) default NULL,
  `objecttype` varchar(20) default NULL,
  `begintime` varchar(50) default NULL,
  `endtime` varchar(50) default NULL,
  `userids` varchar(200) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_smsconfig` */

/*Table structure for table `nms_socket_realtime` */

DROP TABLE IF EXISTS `nms_socket_realtime`;

CREATE TABLE `nms_socket_realtime` (
  `id` bigint(11) NOT NULL auto_increment,
  `is_canconnected` int(2) default NULL,
  `is_valid` int(2) default NULL,
  `is_refresh` int(2) default NULL,
  `reason` varchar(255) default NULL,
  `page_context` mediumtext,
  `mon_time` timestamp NULL default NULL,
  `sms_sign` int(2) default NULL,
  `condelay` int(10) default NULL,
  `socket_id` bigint(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_socket_realtime` */

/*Table structure for table `nms_software_data_temp` */

DROP TABLE IF EXISTS `nms_software_data_temp`;

CREATE TABLE `nms_software_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `insdate` varchar(30) default NULL,
  `name` varchar(100) default NULL,
  `stype` varchar(30) default NULL,
  `swid` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_software_data_temp` */

/*Table structure for table `nms_sqlservercaches` */

DROP TABLE IF EXISTS `nms_sqlservercaches`;

CREATE TABLE `nms_sqlservercaches` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `cacheHitRatio` varchar(50) default NULL,
  `cacheHitRatioBase` varchar(50) default NULL,
  `cacheCount` varchar(50) default NULL,
  `cachePages` varchar(50) default NULL,
  `cacheUsed` varchar(50) default NULL,
  `cacheUsedRate` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlservercaches` */

/*Table structure for table `nms_sqlserverconns` */

DROP TABLE IF EXISTS `nms_sqlserverconns`;

CREATE TABLE `nms_sqlserverconns` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `connections` varchar(50) default NULL,
  `totalLogins` varchar(50) default NULL,
  `totalLoginsRate` varchar(50) default NULL,
  `totalLogouts` varchar(50) default NULL,
  `totalLogoutsRate` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverconns` */

/*Table structure for table `nms_sqlserverdbvalue` */

DROP TABLE IF EXISTS `nms_sqlserverdbvalue`;

CREATE TABLE `nms_sqlserverdbvalue` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `usedperc` varchar(50) default NULL,
  `usedsize` varchar(50) default NULL,
  `size` varchar(50) default NULL,
  `logname` varchar(50) default NULL,
  `dbname` varchar(50) default NULL,
  `instance_name` varchar(50) default NULL,
  `label` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverdbvalue` */

/*Table structure for table `nms_sqlserverinfo_v` */

DROP TABLE IF EXISTS `nms_sqlserverinfo_v`;

CREATE TABLE `nms_sqlserverinfo_v` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `spid` varchar(50) default NULL,
  `waittime` varchar(50) default NULL,
  `lastwaittype` varchar(50) default NULL,
  `waitresource` varchar(50) default NULL,
  `dbname` varchar(50) default NULL,
  `username` varchar(50) default NULL,
  `cpu` varchar(50) default NULL,
  `physical_io` varchar(50) default NULL,
  `memusage` varchar(50) default NULL,
  `login_time` varchar(50) default NULL,
  `last_batch` varchar(50) default NULL,
  `status` varchar(50) default NULL,
  `hostname` varchar(50) default NULL,
  `program_name` varchar(50) default NULL,
  `hostprocess` varchar(50) default NULL,
  `cmd` varchar(50) default NULL,
  `nt_domain` varchar(50) default NULL,
  `nt_username` varchar(50) default NULL,
  `net_library` varchar(50) default NULL,
  `loginame` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverinfo_v` */

/*Table structure for table `nms_sqlserverlockinfo_v` */

DROP TABLE IF EXISTS `nms_sqlserverlockinfo_v`;

CREATE TABLE `nms_sqlserverlockinfo_v` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `rsc_text` varchar(50) default NULL,
  `rsc_dbid` varchar(50) default NULL,
  `dbname` varchar(50) default NULL,
  `rsc_indid` varchar(50) default NULL,
  `rsc_objid` varchar(50) default NULL,
  `rsc_type` varchar(50) default NULL,
  `rsc_flag` varchar(50) default NULL,
  `req_mode` varchar(50) default NULL,
  `req_status` varchar(50) default NULL,
  `req_refcnt` varchar(50) default NULL,
  `req_cryrefcnt` varchar(50) default NULL,
  `req_lifetime` varchar(50) default NULL,
  `req_spid` varchar(50) default NULL,
  `req_ecid` varchar(50) default NULL,
  `req_ownertype` varchar(50) default NULL,
  `req_transactionID` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverlockinfo_v` */

/*Table structure for table `nms_sqlserverlocks` */

DROP TABLE IF EXISTS `nms_sqlserverlocks`;

CREATE TABLE `nms_sqlserverlocks` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `lockRequests` varchar(50) default NULL,
  `lockRequestsRate` varchar(50) default NULL,
  `lockWaits` varchar(50) default NULL,
  `lockWaitsRate` varchar(50) default NULL,
  `lockTimeouts` varchar(50) default NULL,
  `lockTimeoutsRate` varchar(50) default NULL,
  `deadLocks` varchar(50) default NULL,
  `deadLocksRate` varchar(50) default NULL,
  `avgWaitTime` varchar(50) default NULL,
  `avgWaitTimeBase` varchar(50) default NULL,
  `latchWaits` varchar(50) default NULL,
  `latchWaitsRate` varchar(50) default NULL,
  `avgLatchWait` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverlocks` */

/*Table structure for table `nms_sqlservermems` */

DROP TABLE IF EXISTS `nms_sqlservermems`;

CREATE TABLE `nms_sqlservermems` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `totalMemory` varchar(50) default NULL,
  `sqlMem` varchar(50) default NULL,
  `optMemory` varchar(50) default NULL,
  `memGrantPending` varchar(50) default NULL,
  `memGrantSuccess` varchar(50) default NULL,
  `lockMem` varchar(50) default NULL,
  `conMemory` varchar(50) default NULL,
  `grantedWorkspaceMem` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlservermems` */

/*Table structure for table `nms_sqlserverpages` */

DROP TABLE IF EXISTS `nms_sqlserverpages`;

CREATE TABLE `nms_sqlserverpages` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `bufferCacheHitRatio` varchar(50) default NULL,
  `planCacheHitRatio` varchar(50) default NULL,
  `cursorManagerByTypeHitRatio` varchar(50) default NULL,
  `catalogMetadataHitRatio` varchar(50) default NULL,
  `dbOfflineErrors` varchar(50) default NULL,
  `killConnectionErrors` varchar(50) default NULL,
  `userErrors` varchar(50) default NULL,
  `infoErrors` varchar(50) default NULL,
  `sqlServerErrors_total` varchar(50) default NULL,
  `cachedCursorCounts` varchar(50) default NULL,
  `cursorCacheUseCounts` varchar(50) default NULL,
  `cursorRequests_total` varchar(50) default NULL,
  `activeCursors` varchar(50) default NULL,
  `cursorMemoryUsage` varchar(50) default NULL,
  `cursorWorktableUsage` varchar(50) default NULL,
  `activeOfCursorPlans` varchar(50) default NULL,
  `dbPages` varchar(50) default NULL,
  `totalPageLookups` varchar(50) default NULL,
  `totalPageLookupsRate` varchar(50) default NULL,
  `totalPageReads` varchar(50) default NULL,
  `totalPageReadsRate` varchar(50) default NULL,
  `totalPageWrites` varchar(50) default NULL,
  `totalPageWritesRate` varchar(50) default NULL,
  `totalPages` varchar(50) default NULL,
  `freePages` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverpages` */

/*Table structure for table `nms_sqlserverscans` */

DROP TABLE IF EXISTS `nms_sqlserverscans`;

CREATE TABLE `nms_sqlserverscans` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `fullScans` varchar(50) default NULL,
  `fullScansRate` varchar(50) default NULL,
  `rangeScans` varchar(50) default NULL,
  `rangeScansRate` varchar(50) default NULL,
  `probeScans` varchar(50) default NULL,
  `probeScansRate` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverscans` */

/*Table structure for table `nms_sqlserversqls` */

DROP TABLE IF EXISTS `nms_sqlserversqls`;

CREATE TABLE `nms_sqlserversqls` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `batchRequests` varchar(50) default NULL,
  `batchRequestsRate` varchar(50) default NULL,
  `sqlCompilations` varchar(50) default NULL,
  `sqlCompilationsRate` varchar(50) default NULL,
  `sqlRecompilation` varchar(50) default NULL,
  `sqlRecompilationRate` varchar(50) default NULL,
  `autoParams` varchar(50) default NULL,
  `autoParamsRate` varchar(50) default NULL,
  `failedAutoParams` varchar(50) default NULL,
  `failedAutoParamsRate` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserversqls` */

/*Table structure for table `nms_sqlserverstatisticshash` */

DROP TABLE IF EXISTS `nms_sqlserverstatisticshash`;

CREATE TABLE `nms_sqlserverstatisticshash` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `pingjun_lockWaits` varchar(50) default NULL,
  `pingjun_memoryGrantQueueWaits` varchar(50) default NULL,
  `pingjun_threadSafeMemoryObjectWaits` varchar(50) default NULL,
  `pingjun_logWriteWaits` varchar(50) default NULL,
  `pingjun_logBufferWaits` varchar(50) default NULL,
  `pingjun_networkIOWaits` varchar(50) default NULL,
  `pingjun_pageIOLatchWaits` varchar(50) default NULL,
  `pingjun_pageLatchWaits` varchar(50) default NULL,
  `pingjun_nonPageLatchWaits` varchar(50) default NULL,
  `pingjun_waitForTheWorker` varchar(50) default NULL,
  `pingjun_workspaceSynchronizationWaits` varchar(50) default NULL,
  `pingjun_transactionOwnershipWaits` varchar(50) default NULL,
  `jingxing_lockWaits` varchar(50) default NULL,
  `jingxing_memoryGrantQueueWaits` varchar(50) default NULL,
  `jingxing_threadSafeMemoryObjectWaits` varchar(50) default NULL,
  `jingxing_logWriteWaits` varchar(50) default NULL,
  `jingxing_logBufferWaits` varchar(50) default NULL,
  `jingxing_networkIOWaits` varchar(50) default NULL,
  `jingxing_pageIOLatchWaits` varchar(50) default NULL,
  `jingxing_pageLatchWaits` varchar(50) default NULL,
  `jingxing_nonPageLatchWaits` varchar(50) default NULL,
  `jingxing_waitForTheWorker` varchar(50) default NULL,
  `jingxing_workspaceSynchronizationWaits` varchar(50) default NULL,
  `jingxing_transactionOwnershipWaits` varchar(50) default NULL,
  `qidong_lockWaits` varchar(50) default NULL,
  `qidong_memoryGrantQueueWaits` varchar(50) default NULL,
  `qidong_threadSafeMemoryObjectWaits` varchar(50) default NULL,
  `qidong_logWriteWaits` varchar(50) default NULL,
  `qidong_logBufferWaits` varchar(50) default NULL,
  `qidong_networkIOWaits` varchar(50) default NULL,
  `qidong_pageIOLatchWaits` varchar(50) default NULL,
  `qidong_pageLatchWaits` varchar(50) default NULL,
  `qidong_nonPageLatchWaits` varchar(50) default NULL,
  `qidong_waitForTheWorker` varchar(50) default NULL,
  `qidong_workspaceSynchronizationWaits` varchar(50) default NULL,
  `qidong_transactionOwnershipWaits` varchar(50) default NULL,
  `leiji_lockWaits` varchar(50) default NULL,
  `leiji_memoryGrantQueueWaits` varchar(50) default NULL,
  `leiji_threadSafeMemoryObjectWaits` varchar(50) default NULL,
  `leiji_logWriteWaits` varchar(50) default NULL,
  `leiji_logBufferWaits` varchar(50) default NULL,
  `leiji_networkIOWaits` varchar(50) default NULL,
  `leiji_pageIOLatchWaits` varchar(50) default NULL,
  `leiji_pageLatchWaits` varchar(50) default NULL,
  `leiji_nonPageLatchWaits` varchar(50) default NULL,
  `leiji_waitForTheWorker` varchar(50) default NULL,
  `leiji_workspaceSynchronizationWaits` varchar(50) default NULL,
  `leiji_transactionOwnershipWaits` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverstatisticshash` */

/*Table structure for table `nms_sqlserverstatus` */

DROP TABLE IF EXISTS `nms_sqlserverstatus`;

CREATE TABLE `nms_sqlserverstatus` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `status` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserverstatus` */

/*Table structure for table `nms_sqlserversysvalue` */

DROP TABLE IF EXISTS `nms_sqlserversysvalue`;

CREATE TABLE `nms_sqlserversysvalue` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `productlevel` varchar(50) default NULL,
  `version` varchar(200) default NULL,
  `machinename` varchar(50) default NULL,
  `issingleuser` varchar(50) default NULL,
  `processid` varchar(50) default NULL,
  `isintegratedsecurityonly` varchar(50) default NULL,
  `isclustered` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sqlserversysvalue` */

/*Table structure for table `nms_storage` */

DROP TABLE IF EXISTS `nms_storage`;

CREATE TABLE `nms_storage` (
  `id` int(11) NOT NULL auto_increment,
  `ipaddress` varchar(200) character set gb2312 default NULL,
  `name` varchar(200) character set gb2312 default NULL,
  `username` varchar(200) character set gb2312 default NULL,
  `password` varchar(200) character set gb2312 default NULL,
  `status` varchar(200) character set gb2312 default NULL,
  `mon_flag` varchar(200) character set gb2312 default NULL,
  `collecttype` varchar(200) character set gb2312 default NULL,
  `company` varchar(200) character set gb2312 default NULL,
  `type` varchar(200) character set gb2312 default NULL,
  `serial_number` varchar(200) character set gb2312 default NULL,
  `bid` varchar(200) character set gb2312 default NULL,
  `collecttime` varchar(200) character set gb2312 default NULL,
  `supperid` varchar(200) character set gb2312 default NULL,
  `sendemail` varchar(200) character set gb2312 default NULL,
  `sendmobiles` varchar(200) character set gb2312 default NULL,
  `sendphone` varchar(200) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_storage` */

/*Table structure for table `nms_storage_data_temp` */

DROP TABLE IF EXISTS `nms_storage_data_temp`;

CREATE TABLE `nms_storage_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `name` varchar(100) default NULL,
  `stype` varchar(30) default NULL,
  `cap` varchar(30) default NULL,
  `storageindex` varchar(50) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_storage_data_temp` */

/*Table structure for table `nms_storagetype` */

DROP TABLE IF EXISTS `nms_storagetype`;

CREATE TABLE `nms_storagetype` (
  `id` int(11) NOT NULL auto_increment,
  `producer` int(11) default NULL,
  `model` varchar(50) character set gb2312 default NULL,
  `descr` varchar(200) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_storagetype` */

insert into `nms_storagetype` (`id`,`producer`,`model`,`descr`) values (1,3,'DS8100','DS8100');

/*Table structure for table `nms_subconfig_category` */

DROP TABLE IF EXISTS `nms_subconfig_category`;

CREATE TABLE `nms_subconfig_category` (
  `id` bigint(11) NOT NULL auto_increment,
  `name` varchar(100) default ' ',
  `subdesc` varchar(100) default ' ',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_subconfig_category` */

/*Table structure for table `nms_supper_info` */

DROP TABLE IF EXISTS `nms_supper_info`;

CREATE TABLE `nms_supper_info` (
  `id` int(20) NOT NULL auto_increment,
  `su_name` varchar(100) NOT NULL,
  `su_class` varchar(100) NOT NULL,
  `su_area` varchar(100) default NULL,
  `su_desc` varchar(1000) default NULL,
  `su_person` varchar(100) default NULL,
  `su_email` varchar(100) default NULL,
  `su_phone` varchar(100) default NULL,
  `su_address` varchar(200) default NULL,
  `su_dept` varchar(100) default NULL,
  `su_url` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_supper_info` */

insert into `nms_supper_info` (`id`,`su_name`,`su_class`,`su_area`,`su_desc`,`su_person`,`su_email`,`su_phone`,`su_address`,`su_dept`,`su_url`) values (1,'东华软件股份公司','集成商','北京海淀区','北京联银通公司公司竭诚为您服务','胡可磊','hukelei@dhcc.com.cn','13811372044','北京海淀区','网管部','http://www.dhcc.com.cn');
insert into `nms_supper_info` (`id`,`su_name`,`su_class`,`su_area`,`su_desc`,`su_person`,`su_email`,`su_phone`,`su_address`,`su_dept`,`su_url`) values (2,'东华软件股份公司','集成商','北京市海淀区','系统集成部','张自强','zhangzhiqiang@dhcc.com.cn','13811372044','北京市海淀区','系统集成部','www.dhcc.com.cn');

/*Table structure for table `nms_sybasedbinfo` */

DROP TABLE IF EXISTS `nms_sybasedbinfo`;

CREATE TABLE `nms_sybasedbinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `db_created` varchar(50) default NULL,
  `db_freesize` varchar(50) default NULL,
  `db_namer` varchar(50) default NULL,
  `db_owner` varchar(50) default NULL,
  `db_size` varchar(50) default NULL,
  `db_status` varchar(50) default NULL,
  `db_usedperc` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sybasedbinfo` */

/*Table structure for table `nms_sybasedeviceinfo` */

DROP TABLE IF EXISTS `nms_sybasedeviceinfo`;

CREATE TABLE `nms_sybasedeviceinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `device_description` varchar(200) default NULL,
  `device_name` varchar(50) default NULL,
  `device_physical_name` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sybasedeviceinfo` */

/*Table structure for table `nms_sybaseperformance` */

DROP TABLE IF EXISTS `nms_sybaseperformance`;

CREATE TABLE `nms_sybaseperformance` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `cpu_busy` varchar(50) default NULL,
  `idle` varchar(50) default NULL,
  `version` varchar(200) default NULL,
  `io_busy` varchar(50) default NULL,
  `Sent_rate` varchar(50) default NULL,
  `Received_rate` varchar(50) default NULL,
  `Write_rate` varchar(50) default NULL,
  `Read_rate` varchar(50) default NULL,
  `ServerName` varchar(50) default NULL,
  `Cpu_busy_rate` varchar(50) default NULL,
  `Io_busy_rate` varchar(50) default NULL,
  `Disk_count` varchar(50) default NULL,
  `Locks_count` varchar(50) default NULL,
  `Xact_count` varchar(50) default NULL,
  `Total_dataCache` varchar(50) default NULL,
  `Total_physicalMemory` varchar(50) default NULL,
  `Metadata_cache` varchar(50) default NULL,
  `Procedure_cache` varchar(50) default NULL,
  `Total_logicalMemory` varchar(50) default NULL,
  `Data_hitrate` varchar(50) default NULL,
  `Procedure_hitrate` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sybaseperformance` */

/*Table structure for table `nms_sybaseserversinfo` */

DROP TABLE IF EXISTS `nms_sybaseserversinfo`;

CREATE TABLE `nms_sybaseserversinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `Server_class` varchar(50) default NULL,
  `Server_name` varchar(50) default NULL,
  `Server_network_name` varchar(50) default NULL,
  `Server_status` varchar(200) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sybaseserversinfo` */

/*Table structure for table `nms_sybasestatus` */

DROP TABLE IF EXISTS `nms_sybasestatus`;

CREATE TABLE `nms_sybasestatus` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `status` varchar(2) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sybasestatus` */

/*Table structure for table `nms_sybaseuserinfo` */

DROP TABLE IF EXISTS `nms_sybaseuserinfo`;

CREATE TABLE `nms_sybaseuserinfo` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(50) default NULL,
  `Group_name` varchar(50) default NULL,
  `ID_in_db` varchar(50) default NULL,
  `Login_name` varchar(50) default NULL,
  `Users_name` varchar(50) default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_sybaseuserinfo` */

/*Table structure for table `nms_symantec` */

DROP TABLE IF EXISTS `nms_symantec`;

CREATE TABLE `nms_symantec` (
  `begintime` varchar(20) default NULL,
  `machine` varchar(30) default NULL,
  `machine_ip` varchar(15) default NULL,
  `virus` varchar(100) default NULL,
  `virus_file` varchar(200) default NULL,
  `deal_way` varchar(30) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_symantec` */

/*Table structure for table `nms_symantec_log` */

DROP TABLE IF EXISTS `nms_symantec_log`;

CREATE TABLE `nms_symantec_log` (
  `id` int(5) NOT NULL,
  `ip` varchar(15) default NULL,
  `log_file` varchar(15) default NULL,
  `log_row` int(10) default NULL,
  `log_time` varchar(20) default NULL,
  `info` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_symantec_log` */

/*Table structure for table `nms_system_data_temp` */

DROP TABLE IF EXISTS `nms_system_data_temp`;

CREATE TABLE `nms_system_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(30) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_system_data_temp` */

/*Table structure for table `nms_timegratherconfig` */

DROP TABLE IF EXISTS `nms_timegratherconfig`;

CREATE TABLE `nms_timegratherconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `objectid` varchar(20) default NULL,
  `objecttype` varchar(20) default NULL,
  `begintime` varchar(50) default NULL,
  `endtime` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_timegratherconfig` */

/*Table structure for table `nms_timeshareconfig` */

DROP TABLE IF EXISTS `nms_timeshareconfig`;

CREATE TABLE `nms_timeshareconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `objectid` varchar(20) character set gb2312 default NULL,
  `objecttype` varchar(20) character set gb2312 default NULL,
  `timesharetype` varchar(20) character set gb2312 default NULL,
  `begintime` varchar(50) character set gb2312 default NULL,
  `endtime` varchar(50) character set gb2312 default NULL,
  `userids` varchar(200) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_timeshareconfig` */

/*Table structure for table `nms_trapoid` */

DROP TABLE IF EXISTS `nms_trapoid`;

CREATE TABLE `nms_trapoid` (
  `id` bigint(11) NOT NULL auto_increment,
  `enterpriseoid` varchar(100) default NULL,
  `orders` int(2) default NULL,
  `oid` varchar(100) default NULL,
  `desc` varchar(100) default NULL,
  `value1` varchar(100) default NULL,
  `value2` varchar(100) default NULL,
  `transflag` int(1) default NULL,
  `compareflag` int(1) default NULL,
  `transvalue1` varchar(100) default NULL,
  `transvalue2` varchar(100) default NULL,
  `traptype` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_trapoid` */

insert into `nms_trapoid` (`id`,`enterpriseoid`,`orders`,`oid`,`desc`,`value1`,`value2`,`transflag`,`compareflag`,`transvalue1`,`transvalue2`,`traptype`) values (1,'1.3.6.1.4.1.9.1.209',1,'1.3.6.1.2.1.2.2.1.1.1','index','端口号',NULL,0,NULL,NULL,NULL,NULL);
insert into `nms_trapoid` (`id`,`enterpriseoid`,`orders`,`oid`,`desc`,`value1`,`value2`,`transflag`,`compareflag`,`transvalue1`,`transvalue2`,`traptype`) values (2,'1.3.6.1.4.1.9.1.209',2,'1.3.6.1.2.1.2.2.1.2.1','desc','描述',NULL,2,NULL,NULL,NULL,NULL);
insert into `nms_trapoid` (`id`,`enterpriseoid`,`orders`,`oid`,`desc`,`value1`,`value2`,`transflag`,`compareflag`,`transvalue1`,`transvalue2`,`traptype`) values (3,'1.3.6.1.4.1.9.1.209',3,'1.3.6.1.2.1.2.2.1.3.1','type','类型',NULL,0,NULL,NULL,NULL,NULL);
insert into `nms_trapoid` (`id`,`enterpriseoid`,`orders`,`oid`,`desc`,`value1`,`value2`,`transflag`,`compareflag`,`transvalue1`,`transvalue2`,`traptype`) values (4,'1.3.6.1.4.1.9.1.209',4,'1.3.6.1.4.1.9.2.2.1.1.20.1','reasion','Keepalive failed','Keepalive OK',1,NULL,'关闭','启动','port');
insert into `nms_trapoid` (`id`,`enterpriseoid`,`orders`,`oid`,`desc`,`value1`,`value2`,`transflag`,`compareflag`,`transvalue1`,`transvalue2`,`traptype`) values (5,'1.3.6.1.4.1.25506.1.38',1,'1.3.6.1.2.1.2.2.1.1','index','端口号',NULL,2,NULL,NULL,NULL,NULL);
insert into `nms_trapoid` (`id`,`enterpriseoid`,`orders`,`oid`,`desc`,`value1`,`value2`,`transflag`,`compareflag`,`transvalue1`,`transvalue2`,`traptype`) values (6,'1.3.6.1.4.1.25506.1.38',2,'1.3.6.1.2.1.2.2.1.7','adminstatus','2','1',0,NULL,'',NULL,NULL);
insert into `nms_trapoid` (`id`,`enterpriseoid`,`orders`,`oid`,`desc`,`value1`,`value2`,`transflag`,`compareflag`,`transvalue1`,`transvalue2`,`traptype`) values (7,'1.3.6.1.4.1.25506.1.38',3,'1.3.6.1.2.1.2.2.1.8','operstatus','2','1',1,NULL,'关闭','启动','port');

/*Table structure for table `nms_tuxedoconfig` */

DROP TABLE IF EXISTS `nms_tuxedoconfig`;

CREATE TABLE `nms_tuxedoconfig` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) character set gb2312 default NULL,
  `ipaddress` varchar(255) character set gb2312 default NULL,
  `community` varchar(255) character set gb2312 default NULL,
  `port` varchar(255) character set gb2312 default NULL,
  `status` varchar(255) character set gb2312 default NULL,
  `mon_flag` varchar(255) character set gb2312 default NULL,
  `bid` varchar(255) character set gb2312 default NULL,
  `sendphone` varchar(255) character set gb2312 default NULL,
  `sendemail` varchar(255) character set gb2312 default NULL,
  `sendmobiles` varchar(255) character set gb2312 default NULL,
  `reservation1` varchar(255) character set gb2312 default NULL,
  `reservation2` varchar(255) character set gb2312 default NULL,
  `reservation3` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_tuxedoconfig` */

/*Table structure for table `nms_urlconfig` */

DROP TABLE IF EXISTS `nms_urlconfig`;

CREATE TABLE `nms_urlconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `str` varchar(255) default NULL,
  `user_name` varchar(30) default NULL,
  `user_password` varchar(30) default NULL,
  `query_string` varchar(255) default NULL,
  `method` char(1) default NULL,
  `availability_string` varchar(255) default NULL,
  `poll_interval` int(11) default NULL,
  `unavailability_string` varchar(255) default NULL,
  `timeout` int(11) default NULL,
  `verify` int(11) default NULL,
  `flag` int(11) default NULL,
  `mon_flag` int(11) default NULL,
  `alias` varchar(50) default NULL,
  `sendmobiles` varchar(500) default NULL,
  `netid` varchar(100) default NULL,
  `sendemail` varchar(500) default NULL,
  `sendphone` varchar(100) default NULL,
  `ipaddress` varchar(15) default NULL,
  `supperid` int(11) default NULL,
  `keyword` varchar(200) default NULL,
  `pagesize_min` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_urlconfig` */

/*Table structure for table `nms_user_audit` */

DROP TABLE IF EXISTS `nms_user_audit`;

CREATE TABLE `nms_user_audit` (
  `id` int(11) NOT NULL auto_increment,
  `userid` int(11) default NULL,
  `action` varchar(500) character set gb2312 default NULL,
  `time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_user_audit` */

insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (1,4,'删除设备 IP:10.10.152.57 别名:10.10.152.57 类型:Windows Professional/XP','2011-01-27 15:39:51');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (2,4,'删除设备 IP:10.10.1.1 别名:10.10.1.1 类型:H3C路由交换机','2011-01-28 17:41:53');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (3,4,'删除设备 IP:10.10.1.2 别名:10.10.1.2 类型:H3C路由交换机','2011-01-28 17:41:55');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (4,4,'删除设备 IP:10.10.152.57 别名:10.10.152.57 类型:Windows Professional/XP','2011-01-28 17:41:57');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (5,4,'删除设备 IP:10.10.152.58 别名:10.10.152.58 类型:Windows Professional/XP','2011-01-28 17:42:00');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (6,4,'删除设备 IP:10.204.7.20 别名:10.204.7.20 类型:IBM AIX 服务器','2011-01-28 17:42:01');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (7,4,'删除设备 IP:10.10.1.1 别名:10.10.1.1 类型:H3C路由交换机','2011-02-11 16:58:10');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (8,4,'删除设备 IP:172.25.25.240 别名:172.25.25.240 类型:Cisco 2621','2011-02-11 16:58:13');
insert into `nms_user_audit` (`id`,`userid`,`action`,`time`) values (9,4,'删除设备 IP:127.0.0.1 别名:127.0.0.1 类型:Windows Professional/XP','2011-02-11 16:58:15');

/*Table structure for table `nms_user_data_temp` */

DROP TABLE IF EXISTS `nms_user_data_temp`;

CREATE TABLE `nms_user_data_temp` (
  `nodeid` varchar(10) default NULL,
  `ip` varchar(20) default NULL,
  `type` varchar(30) default NULL,
  `subtype` varchar(30) default NULL,
  `entity` varchar(30) default NULL,
  `subentity` varchar(30) default NULL,
  `sindex` varchar(200) default NULL,
  `thevalue` varchar(300) default NULL,
  `chname` varchar(50) default NULL,
  `restype` varchar(20) default NULL,
  `collecttime` timestamp NULL default CURRENT_TIMESTAMP,
  `unit` varchar(10) default NULL,
  `bak` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_user_data_temp` */

/*Table structure for table `nms_user_tasklog` */

DROP TABLE IF EXISTS `nms_user_tasklog`;

CREATE TABLE `nms_user_tasklog` (
  `id` int(11) NOT NULL auto_increment,
  `userid` int(11) default NULL,
  `content` varchar(1000) character set gb2312 default NULL,
  `time` varchar(255) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `nms_user_tasklog` */

insert into `nms_user_tasklog` (`id`,`userid`,`content`,`time`) values (114,4,'测试日志','2009-12-11');
insert into `nms_user_tasklog` (`id`,`userid`,`content`,`time`) values (115,4,'查看日志','2009-12-10');
insert into `nms_user_tasklog` (`id`,`userid`,`content`,`time`) values (116,4,'测试日志','2009-12-09');
insert into `nms_user_tasklog` (`id`,`userid`,`content`,`time`) values (117,4,'测试日志','2009-12-08');
insert into `nms_user_tasklog` (`id`,`userid`,`content`,`time`) values (118,4,'测试','2009-12-07');

/*Table structure for table `nms_wasconfig` */

DROP TABLE IF EXISTS `nms_wasconfig`;

CREATE TABLE `nms_wasconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `nodename` varchar(100) default NULL,
  `ipaddress` varchar(19) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `mon_flag` int(11) default NULL,
  `netid` varchar(100) default NULL,
  `portnum` int(11) default NULL,
  `sendemail` varchar(100) default NULL,
  `servername` varchar(100) default NULL,
  `name` varchar(50) default NULL,
  `supperid` int(11) default NULL,
  `version` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_wasconfig` */

/*Table structure for table `nms_web_history` */

DROP TABLE IF EXISTS `nms_web_history`;

CREATE TABLE `nms_web_history` (
  `id` bigint(11) NOT NULL auto_increment,
  `url_id` int(11) default NULL,
  `is_canconnected` int(11) default NULL,
  `is_valid` int(2) default NULL,
  `is_refresh` int(2) default NULL,
  `reason` varchar(255) default NULL,
  `mon_time` timestamp NULL default NULL,
  `condelay` int(11) default NULL,
  `pagesize` varchar(20) default NULL,
  `key_exist` varchar(200) default NULL,
  `change_rate` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_web_history` */

/*Table structure for table `nms_web_realtime` */

DROP TABLE IF EXISTS `nms_web_realtime`;

CREATE TABLE `nms_web_realtime` (
  `id` bigint(11) NOT NULL auto_increment,
  `is_canconnected` int(2) default NULL,
  `is_valid` int(2) default NULL,
  `is_refresh` int(2) default NULL,
  `reason` varchar(255) default NULL,
  `page_context` mediumtext,
  `mon_time` timestamp NULL default NULL,
  `sms_sign` int(2) default NULL,
  `condelay` int(10) default NULL,
  `url_id` bigint(11) default NULL,
  `pagesize` varchar(20) default NULL,
  `key_exist` varchar(200) default NULL,
  `change_rate` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_web_realtime` */

/*Table structure for table `nms_weblogicconfig` */

DROP TABLE IF EXISTS `nms_weblogicconfig`;

CREATE TABLE `nms_weblogicconfig` (
  `id` bigint(11) NOT NULL,
  `name` varchar(100) default NULL,
  `ipaddress` varchar(19) default NULL,
  `community` varchar(100) default NULL,
  `sendmobiles` varchar(100) default NULL,
  `mon_flag` int(11) default NULL,
  `netid` varchar(100) default NULL,
  `portnum` int(11) default NULL,
  `sendemail` varchar(100) default NULL,
  `sendphone` varchar(100) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `nms_weblogicconfig` */

/*Table structure for table `node_submap_relation` */

DROP TABLE IF EXISTS `node_submap_relation`;

CREATE TABLE `node_submap_relation` (
  `id` int(10) NOT NULL,
  `xml_name` varchar(50) default NULL COMMENT '节点所在的xml',
  `node_id` varchar(10) default NULL COMMENT '节点',
  `category` varchar(50) default NULL COMMENT '节点类型',
  `map_id` varchar(10) default NULL COMMENT '关联的图id',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `node_submap_relation` */

/*Table structure for table `server_telnet_config` */

DROP TABLE IF EXISTS `server_telnet_config`;

CREATE TABLE `server_telnet_config` (
  `id` int(3) NOT NULL,
  `node_id` int(5) default NULL,
  `user` varchar(30) default NULL,
  `password` varchar(30) default NULL,
  `prompt` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `server_telnet_config` */

/*Table structure for table `sms_server` */

DROP TABLE IF EXISTS `sms_server`;

CREATE TABLE `sms_server` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `mobilenum` varchar(100) default NULL,
  `eventlist` varchar(500) default NULL,
  `eventtime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `sms_server` */

/*Table structure for table `storageping` */

DROP TABLE IF EXISTS `storageping`;

CREATE TABLE `storageping` (
  `id` int(11) NOT NULL auto_increment,
  `ipaddress` varchar(200) character set gb2312 default NULL,
  `restype` varchar(200) character set gb2312 default NULL,
  `category` varchar(200) character set gb2312 default NULL,
  `entity` varchar(200) character set gb2312 default NULL,
  `subentity` varchar(200) character set gb2312 default NULL,
  `unit` varchar(200) character set gb2312 default NULL,
  `chname` varchar(200) character set gb2312 default NULL,
  `bak` varchar(200) character set gb2312 default NULL,
  `count` varchar(200) character set gb2312 default NULL,
  `thevalue` varchar(200) character set gb2312 default NULL,
  `collecttime` varchar(200) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `storageping` */

/*Table structure for table `system_alertemail` */

DROP TABLE IF EXISTS `system_alertemail`;

CREATE TABLE `system_alertemail` (
  `id` bigint(11) NOT NULL auto_increment,
  `username` varchar(50) default NULL,
  `password` varchar(50) default NULL,
  `smtp` varchar(50) default NULL,
  `usedflag` int(2) default NULL,
  `mail_address` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_alertemail` */

insert into `system_alertemail` (`id`,`username`,`password`,`smtp`,`usedflag`,`mail_address`) values (1,'songxl','54255425','221.214.12.238',0,NULL);
insert into `system_alertemail` (`id`,`username`,`password`,`smtp`,`usedflag`,`mail_address`) values (2,'donhukelei','hukelei','sohu.com',0,'donghukelei@sohu.com');
insert into `system_alertemail` (`id`,`username`,`password`,`smtp`,`usedflag`,`mail_address`) values (3,'333','333','333',0,NULL);
insert into `system_alertemail` (`id`,`username`,`password`,`smtp`,`usedflag`,`mail_address`) values (4,'hukelei','hukelei','mail.dhcc.com.cn',1,'hukelei@dhcc.com.cn');

/*Table structure for table `system_alertinfoserver` */

DROP TABLE IF EXISTS `system_alertinfoserver`;

CREATE TABLE `system_alertinfoserver` (
  `id` int(11) NOT NULL auto_increment,
  `ipaddress` varchar(50) default NULL,
  `port` varchar(50) default NULL,
  `infodesc` varchar(50) default NULL,
  `flag` int(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_alertinfoserver` */

insert into `system_alertinfoserver` (`id`,`ipaddress`,`port`,`infodesc`,`flag`) values (7,'127.0.0.1','5000','使用客户端进行告警信息提醒的服务器IP设置',1);

/*Table structure for table `system_business` */

DROP TABLE IF EXISTS `system_business`;

CREATE TABLE `system_business` (
  `id` bigint(11) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `descr` varchar(100) default NULL,
  `pid` varchar(12) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_business` */

insert into `system_business` (`id`,`name`,`descr`,`pid`) values (1,'所有业务','所有业务','0');
insert into `system_business` (`id`,`name`,`descr`,`pid`) values (2,'信息中心','信息中心','1');
insert into `system_business` (`id`,`name`,`descr`,`pid`) values (3,'网管业务','网管业务','1');
insert into `system_business` (`id`,`name`,`descr`,`pid`) values (4,'办公网网络业务','办公网网络业务','3');
insert into `system_business` (`id`,`name`,`descr`,`pid`) values (5,'广域网网络业务','广域网网络业务','3');
insert into `system_business` (`id`,`name`,`descr`,`pid`) values (6,'测试业务','测试业务','1');

/*Table structure for table `system_config` */

DROP TABLE IF EXISTS `system_config`;

CREATE TABLE `system_config` (
  `id` bigint(11) NOT NULL auto_increment,
  `variable_name` varchar(20) default NULL,
  `value` varchar(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_config` */

insert into `system_config` (`id`,`variable_name`,`value`) values (1,'collectwebflag','0');

/*Table structure for table `system_db2spaceconf` */

DROP TABLE IF EXISTS `system_db2spaceconf`;

CREATE TABLE `system_db2spaceconf` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `spacename` varchar(100) default NULL,
  `linkuse` varchar(100) default NULL,
  `sms` int(2) default NULL,
  `bak` varchar(100) default NULL,
  `reportflag` int(2) default NULL,
  `alarmvalue` bigint(5) default NULL,
  `dbname` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_db2spaceconf` */

/*Table structure for table `system_department` */

DROP TABLE IF EXISTS `system_department`;

CREATE TABLE `system_department` (
  `id` int(3) NOT NULL,
  `dept` varchar(30) NOT NULL,
  `man` varchar(20) default '',
  `tel` varchar(20) default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_department` */

insert into `system_department` (`id`,`dept`,`man`,`tel`) values (1,'软件事业部','胡可磊','13811372044');
insert into `system_department` (`id`,`dept`,`man`,`tel`) values (2,'信息技术部','林江珧','7586402');
insert into `system_department` (`id`,`dept`,`man`,`tel`) values (3,'系统集成部','王磊','62662421');

/*Table structure for table `system_eventlist` */

DROP TABLE IF EXISTS `system_eventlist`;

CREATE TABLE `system_eventlist` (
  `id` bigint(11) NOT NULL auto_increment,
  `eventtype` varchar(30) default NULL,
  `eventlocation` varchar(200) default NULL,
  `content` text,
  `level1` int(3) default NULL,
  `managesign` int(3) default NULL,
  `bak` varchar(255) default NULL,
  `recordtime` timestamp NULL default NULL,
  `reportman` varchar(30) default NULL,
  `nodeid` int(11) default NULL,
  `businessid` varchar(100) default NULL,
  `oid` int(11) default NULL,
  `subtype` varchar(50) default NULL,
  `managetime` varchar(50) default NULL,
  `subentity` varchar(50) default NULL,
  `reasion` varchar(500) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_eventlist` */

/*Table structure for table `system_eventreport` */

DROP TABLE IF EXISTS `system_eventreport`;

CREATE TABLE `system_eventreport` (
  `id` bigint(11) NOT NULL auto_increment,
  `eventid` bigint(11) default NULL,
  `report_man` varchar(50) default NULL,
  `report_content` text,
  `deal_time` timestamp NULL default NULL,
  `report_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_eventreport` */

/*Table structure for table `system_infomixspaceconf` */

DROP TABLE IF EXISTS `system_infomixspaceconf`;

CREATE TABLE `system_infomixspaceconf` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `spacename` varchar(50) default NULL,
  `linkuse` varchar(200) default NULL,
  `sms` int(11) default NULL,
  `bak` varchar(200) default NULL,
  `reportflag` int(2) default NULL,
  `alarmvalue` int(5) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_infomixspaceconf` */

/*Table structure for table `system_ipaddresspanel` */

DROP TABLE IF EXISTS `system_ipaddresspanel`;

CREATE TABLE `system_ipaddresspanel` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `status` varchar(2) default NULL,
  `imagetype` varchar(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_ipaddresspanel` */

insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (3,'10.3.0.1','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (4,'192.168.1.232','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (5,'10.10.1.4','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (6,'192.168.1.231','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (7,'10.10.3.8','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (8,'10.10.1.2','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (9,'10.10.1.3','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (12,'10.0.4.3','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (13,'10.0.4.2','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (14,'10.10.1.2','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (22,'10.10.1.1','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (24,'10.10.151.176','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (25,'192.168.1.2','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (27,'10.10.1.5','1','1');
insert into `system_ipaddresspanel` (`id`,`ipaddress`,`status`,`imagetype`) values (28,'10.200.230.250','1','1');

/*Table structure for table `system_knowledge` */

DROP TABLE IF EXISTS `system_knowledge`;

CREATE TABLE `system_knowledge` (
  `id` int(11) NOT NULL auto_increment,
  `category` varchar(50) NOT NULL,
  `entity` varchar(50) NOT NULL,
  `subentity` varchar(50) NOT NULL,
  `attachfiles` varchar(200) NOT NULL,
  `bak` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `system_knowledge` */

insert into `system_knowledge` (`id`,`category`,`entity`,`subentity`,`attachfiles`,`bak`) values (5,'net','h3c','cpu','snmp.pdf','test');

/*Table structure for table `system_knowledgebase` */

DROP TABLE IF EXISTS `system_knowledgebase`;

CREATE TABLE `system_knowledgebase` (
  `id` int(11) NOT NULL auto_increment,
  `category` varchar(50) NOT NULL,
  `entity` varchar(50) NOT NULL,
  `subentity` varchar(50) NOT NULL,
  `titles` varchar(50) NOT NULL,
  `contents` varchar(5000) NOT NULL,
  `bak` varchar(100) default NULL,
  `attachfiles` varchar(100) NOT NULL,
  `userid` varchar(30) NOT NULL,
  `ktime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `system_knowledgebase` */

/*Table structure for table `system_menu` */

DROP TABLE IF EXISTS `system_menu`;

CREATE TABLE `system_menu` (
  `id` varchar(10) NOT NULL,
  `title` varchar(30) default NULL,
  `url` varchar(100) default NULL,
  `sort` int(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_menu` */

insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0100','系统管理','top_menu',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0101','用户','user.do?action=list&jp=1',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0102','角色','role.do?action=list&jp=1',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0103','部门','dept.do?action=list&jp=1',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0104','职位','position.do?action=list&jp=1',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0105','菜单','menu.do?action=list_top',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0106','权限','admin.do?action=list',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0107','日志','syslog.do?action=list&jp=1',8);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0200','拓扑管理','top_menu',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0201','网络拓扑','topology/network/index.jsp',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0202','服务器','topology/server/index.jsp',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0203','子网','subnet.do?action=list',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0204','链路信息','link.do?action=list',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0205','发现结果','discover.do?action=list&jp=1',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0206','自动发现','discover.do?action=config',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0207','网络VLAN拓扑','topology/network/vlanindex.jsp',1);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0300','系统设置','top_menu',7);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0301','服务','service.do?action=list',7);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0302','生产商','producer.do?action=list&jp=1',7);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0303','设备类型','devicetype.do?action=list&jp=1',7);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0400','视图定制','top_menu',2);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0402','视图编辑','customxml.do?action=list&jp=1',2);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0403','视图展现','topology/view/custom.jsp',2);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0500','应用','top_menu',6);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0501','数据库','db.do?action=list',6);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0502','Tomcat','tomcat.do?action=list',6);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0503','IP列表','ipnode.do?action=list',6);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0600','告警','top_menu',5);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0601','告警','alarm.do?action=list&jp=1',5);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0602','报表','inform/report/report.jsp',5);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0603','告警浏览','event.do?action=list&jp=1',5);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0700','IP资源','top_menu',3);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0701','IP列表','ipmac.do?action=list&jp=1',3);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0702','IP变更','ipres.do?action=list&jp=1',3);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0703','IP分布','ipres.do?action=detail',3);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0704','IP定位','ipres.do?action=locate',3);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0800','报表','top_menu',4);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0801','门禁日志','security/gate/index.jsp',4);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0802','UPS监控','ups.do?action=list',4);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0900','资源','top_menu',9);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0901','业务分类','business.do?action=list',9);
insert into `system_menu` (`id`,`title`,`url`,`sort`) values ('0902','设备维护','network.do?action=list',9);

/*Table structure for table `system_mysqlspaceconf` */

DROP TABLE IF EXISTS `system_mysqlspaceconf`;

CREATE TABLE `system_mysqlspaceconf` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `dbname` varchar(100) default NULL,
  `linkuse` varchar(100) default NULL,
  `sms` int(2) default NULL,
  `bak` varchar(200) default NULL,
  `reportflag` int(2) default NULL,
  `alarmvalue` bigint(3) default NULL,
  `logflag` int(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_mysqlspaceconf` */

/*Table structure for table `system_nodetobusiness` */

DROP TABLE IF EXISTS `system_nodetobusiness`;

CREATE TABLE `system_nodetobusiness` (
  `id` bigint(11) NOT NULL auto_increment,
  `nodeid` bigint(11) default NULL,
  `businessid` bigint(11) default NULL,
  `elementtype` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_nodetobusiness` */

/*Table structure for table `system_orasessiondata` */

DROP TABLE IF EXISTS `system_orasessiondata`;

CREATE TABLE `system_orasessiondata` (
  `id` bigint(11) NOT NULL auto_increment,
  `serverip` varchar(15) default NULL,
  `dbname` varchar(50) default NULL,
  `machine` varchar(50) default NULL,
  `username` varchar(50) default NULL,
  `program` varchar(200) default NULL,
  `status` varchar(10) default NULL,
  `sessiontype` varchar(10) default NULL,
  `command` varchar(200) default NULL,
  `logontime` timestamp NULL default NULL,
  `mon_time` timestamp NULL default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_orasessiondata` */

/*Table structure for table `system_oraspaceconf` */

DROP TABLE IF EXISTS `system_oraspaceconf`;

CREATE TABLE `system_oraspaceconf` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `spacename` varchar(50) default NULL,
  `linkuse` varchar(200) default NULL,
  `sms` int(11) default NULL,
  `bak` varchar(200) default NULL,
  `reportflag` int(2) default NULL,
  `alarmvalue` int(5) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_oraspaceconf` */

/*Table structure for table `system_ostype` */

DROP TABLE IF EXISTS `system_ostype`;

CREATE TABLE `system_ostype` (
  `ostypeid` bigint(11) NOT NULL auto_increment,
  `ostypename` varchar(50) default NULL,
  `description` varchar(100) default NULL,
  PRIMARY KEY  (`ostypeid`),
  UNIQUE KEY `ostypeid_index` (`ostypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_ostype` */

insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (1,'cisco','cisco');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (2,'h3c','h3c');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (3,'Entrasys','Entrasys');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (4,'Radware','Radware');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (5,'Windows','Windows');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (6,'AIX','AIX');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (7,'HPUNIX','HP UNIX');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (8,'SUNSolaris','SUN Solaris');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (9,'Linux','Linux');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (10,'MaiPu','MaiPu');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (11,'RedGiant','RedGiant');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (12,'NorthTel','NorthTel');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (13,'DLink','DLink');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (14,'BDCom','BDCom');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (15,'AS400','AS400');
insert into `system_ostype` (`ostypeid`,`ostypename`,`description`) values (16,'DigitalChina','DigitalChina');

/*Table structure for table `system_panelmodel` */

DROP TABLE IF EXISTS `system_panelmodel`;

CREATE TABLE `system_panelmodel` (
  `id` bigint(11) NOT NULL auto_increment,
  `oid` varchar(100) default NULL,
  `width` varchar(10) default NULL,
  `height` varchar(10) default NULL,
  `imagetype` varchar(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_panelmodel` */

insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (5,'1.3.6.1.4.1.25506.1.38','1049','166','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (6,'1.3.6.1.4.1.25506.1.40','807','301','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (8,'1.3.6.1.4.1.2011.2.39.11','11','11','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (13,'1.3.6.1.4.1.25506.1.38','1049','166','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (14,'1.3.6.1.4.1.25506.1.40','807','301','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (15,'1.3.6.1.4.1.2011.2.39.11','11','11','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (16,'1.3.6.1.4.1.9.1.516','979','96','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (17,'1.3.6.1.4.1.9.1.367','983','98','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (18,'1.3.6.1.4.1.9.1.502','686','596','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (19,'1.3.6.1.4.1.9.1.283','720','891','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (20,'1.3.6.1.4.1.9.5.46','832','577','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (21,'1.3.6.1.4.1.9.1.617','936','94','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (22,'1.3.6.1.4.1.25506.1.54','808','493','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (23,'1.3.6.1.4.1.2011.2.23.33','803','563','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (24,'1.3.6.1.4.1.25506.1.54','500','500','2');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (25,'1.3.6.1.4.1.25506.1.33','1024','137','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (26,'1.3.6.1.4.1.2011.1.1.1.12811','799','76','1');
insert into `system_panelmodel` (`id`,`oid`,`width`,`height`,`imagetype`) values (27,'1.3.6.1.4.1.25506.1.187','600','400','1');

/*Table structure for table `system_portconfig` */

DROP TABLE IF EXISTS `system_portconfig`;

CREATE TABLE `system_portconfig` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `portindex` bigint(20) default NULL,
  `name` varchar(50) default NULL,
  `linkuse` varchar(100) default NULL,
  `sms` int(2) default NULL,
  `bak` varchar(100) default NULL,
  `reportflag` int(2) default NULL,
  `inportalarm` varchar(50) default NULL,
  `outportalarm` varchar(50) default NULL,
  `speed` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_portconfig` */

/*Table structure for table `system_position` */

DROP TABLE IF EXISTS `system_position`;

CREATE TABLE `system_position` (
  `id` int(5) NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_position` */

insert into `system_position` (`id`,`name`) values (1,'软件工程师');
insert into `system_position` (`id`,`name`) values (2,'信息技术部人员');

/*Table structure for table `system_role` */

DROP TABLE IF EXISTS `system_role`;

CREATE TABLE `system_role` (
  `id` int(5) NOT NULL,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_role` */

insert into `system_role` (`id`,`role`) values (0,'superadmin');
insert into `system_role` (`id`,`role`) values (1,'开发人员');
insert into `system_role` (`id`,`role`) values (2,'管理员');
insert into `system_role` (`id`,`role`) values (4,'一般人员');

/*Table structure for table `system_role_menu` */

DROP TABLE IF EXISTS `system_role_menu`;

CREATE TABLE `system_role_menu` (
  `id` int(5) NOT NULL,
  `role_id` int(5) default NULL,
  `menu_id` varchar(10) default NULL,
  `operate` int(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_role_menu` */

insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (1,0,'0100',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (2,0,'0101',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (3,0,'0102',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (4,0,'0103',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (5,0,'0104',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (6,0,'0105',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (7,0,'0106',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (8,0,'0107',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (9,1,'0100',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (10,1,'0101',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (11,1,'0102',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (12,1,'0103',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (13,1,'0104',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (14,1,'0105',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (15,1,'0106',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (16,1,'0107',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (17,0,'0200',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (18,1,'0200',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (19,0,'0300',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (20,1,'0300',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (21,0,'0201',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (22,1,'0201',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (23,0,'0202',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (24,1,'0202',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (25,0,'0203',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (26,1,'0203',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (27,0,'0204',1);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (28,1,'0204',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (29,0,'0205',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (30,1,'0205',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (31,0,'0301',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (32,1,'0301',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (33,0,'0302',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (34,1,'0302',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (35,0,'0303',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (36,1,'0303',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (39,0,'0400',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (40,1,'0400',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (43,0,'0402',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (44,1,'0402',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (45,0,'0403',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (46,1,'0403',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (47,0,'0500',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (48,1,'0500',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (49,0,'0501',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (50,1,'0501',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (51,0,'0502',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (52,1,'0502',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (53,0,'0503',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (54,1,'0503',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (55,0,'0600',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (56,1,'0600',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (57,0,'0601',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (58,1,'0601',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (59,0,'0602',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (60,1,'0602',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (61,0,'0700',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (62,1,'0700',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (63,0,'0701',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (64,1,'0701',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (65,0,'0702',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (66,1,'0702',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (67,0,'0703',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (68,1,'0703',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (69,0,'0704',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (70,1,'0704',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (71,2,'0100',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (72,2,'0101',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (73,2,'0102',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (74,2,'0103',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (75,2,'0104',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (76,2,'0105',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (77,2,'0106',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (78,2,'0107',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (79,2,'0200',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (80,2,'0300',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (81,2,'0201',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (82,2,'0202',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (83,2,'0203',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (84,2,'0204',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (85,2,'0205',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (86,2,'0301',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (87,2,'0302',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (88,2,'0303',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (89,2,'0400',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (90,2,'0402',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (91,2,'0403',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (92,2,'0500',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (93,2,'0501',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (94,2,'0502',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (95,2,'0503',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (96,2,'0600',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (97,2,'0601',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (98,2,'0602',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (99,2,'0700',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (100,2,'0701',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (101,2,'0702',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (102,2,'0703',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (103,2,'0704',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (104,0,'0800',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (105,1,'0800',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (106,2,'0800',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (107,0,'0801',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (108,1,'0801',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (109,2,'0801',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (110,0,'0802',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (111,1,'0802',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (112,2,'0802',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (113,3,'0100',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (114,3,'0101',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (115,3,'0102',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (116,3,'0103',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (117,3,'0104',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (118,3,'0105',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (119,3,'0106',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (120,3,'0107',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (121,3,'0200',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (122,3,'0300',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (123,3,'0201',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (124,3,'0202',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (125,3,'0203',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (126,3,'0204',1);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (127,3,'0205',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (128,3,'0301',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (129,3,'0302',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (130,3,'0303',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (131,3,'0400',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (132,3,'0402',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (133,3,'0403',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (134,3,'0500',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (135,3,'0501',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (136,3,'0502',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (137,3,'0503',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (138,3,'0600',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (139,3,'0601',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (140,3,'0602',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (141,3,'0700',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (142,3,'0701',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (143,3,'0702',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (144,3,'0703',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (145,3,'0704',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (146,3,'0800',3);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (147,3,'0801',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (148,3,'0802',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (149,4,'0100',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (150,4,'0101',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (151,4,'0102',1);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (152,4,'0103',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (153,4,'0104',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (154,4,'0105',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (155,4,'0106',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (156,4,'0107',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (157,4,'0200',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (158,4,'0300',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (159,4,'0201',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (160,4,'0202',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (161,4,'0203',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (162,4,'0204',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (163,4,'0205',2);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (164,4,'0301',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (165,4,'0302',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (166,4,'0303',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (167,4,'0400',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (168,4,'0402',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (169,4,'0403',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (170,4,'0500',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (171,4,'0501',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (172,4,'0502',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (173,4,'0503',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (174,4,'0600',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (175,4,'0601',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (176,4,'0602',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (177,4,'0700',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (178,4,'0701',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (179,4,'0702',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (180,4,'0703',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (181,4,'0704',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (182,4,'0800',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (183,4,'0801',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (184,4,'0802',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (185,0,'0206',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (186,1,'0206',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (187,2,'0206',0);
insert into `system_role_menu` (`id`,`role_id`,`menu_id`,`operate`) values (188,4,'0206',0);

/*Table structure for table `system_serial_node` */

DROP TABLE IF EXISTS `system_serial_node`;

CREATE TABLE `system_serial_node` (
  `id` int(11) NOT NULL auto_increment,
  `address` varchar(200) character set gb2312 default NULL,
  `name` varchar(200) character set gb2312 default NULL,
  `description` varchar(1000) character set gb2312 default NULL,
  `monflag` varchar(10) character set gb2312 default NULL,
  `serialportid` varchar(10) character set gb2312 default NULL,
  `baudrate` varchar(10) character set gb2312 default NULL,
  `databits` varchar(10) character set gb2312 default NULL,
  `stopbits` varchar(10) character set gb2312 default NULL,
  `parity` varchar(10) character set gb2312 default NULL,
  `bid` varchar(100) character set gb2312 default NULL,
  `sendmail` varchar(100) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `system_serial_node` */

/*Table structure for table `system_snmpconfig` */

DROP TABLE IF EXISTS `system_snmpconfig`;

CREATE TABLE `system_snmpconfig` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `snmpversion` int(5) default NULL,
  `readcommunity` varchar(50) default NULL,
  `writecommunity` varchar(50) default NULL,
  `timeout` int(5) default NULL,
  `trytime` int(5) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_snmpconfig` */

/*Table structure for table `system_sqldbconf` */

DROP TABLE IF EXISTS `system_sqldbconf`;

CREATE TABLE `system_sqldbconf` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `dbname` varchar(100) default NULL,
  `linkuse` varchar(100) default NULL,
  `sms` int(2) default NULL,
  `bak` varchar(200) default NULL,
  `reportflag` int(2) default NULL,
  `alarmvalue` bigint(3) default NULL,
  `logflag` int(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_sqldbconf` */

/*Table structure for table `system_sybspaceconf` */

DROP TABLE IF EXISTS `system_sybspaceconf`;

CREATE TABLE `system_sybspaceconf` (
  `id` bigint(11) NOT NULL auto_increment,
  `ipaddress` varchar(15) default NULL,
  `spacename` varchar(200) default NULL,
  `linkuse` varchar(200) default NULL,
  `sms` int(2) default NULL,
  `bak` varchar(200) default NULL,
  `reportflag` int(2) default NULL,
  `alarmvalue` int(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_sybspaceconf` */

/*Table structure for table `system_sys_log` */

DROP TABLE IF EXISTS `system_sys_log`;

CREATE TABLE `system_sys_log` (
  `id` int(10) NOT NULL,
  `event` varchar(20) default NULL,
  `log_time` varchar(20) default NULL,
  `ip` varchar(15) default '',
  `username` varchar(50) default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_sys_log` */

/*Table structure for table `system_temperature_humidity` */

DROP TABLE IF EXISTS `system_temperature_humidity`;

CREATE TABLE `system_temperature_humidity` (
  `id` int(10) NOT NULL auto_increment,
  `node_id` varchar(10) character set gb2312 default NULL,
  `temperature` varchar(10) character set gb2312 default NULL,
  `humidity` varchar(10) character set gb2312 default NULL,
  `time` varchar(100) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `system_temperature_humidity` */

/*Table structure for table `system_temperature_humidity_threshold` */

DROP TABLE IF EXISTS `system_temperature_humidity_threshold`;

CREATE TABLE `system_temperature_humidity_threshold` (
  `id` int(10) NOT NULL auto_increment,
  `node_id` varchar(255) character set gb2312 default NULL,
  `min_temperature` varchar(10) character set gb2312 default NULL,
  `max_temperature` varchar(10) character set gb2312 default NULL,
  `min_humidity` varchar(10) character set gb2312 default NULL,
  `max_humidity` varchar(10) character set gb2312 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `system_temperature_humidity_threshold` */

/*Table structure for table `system_tftpserver` */

DROP TABLE IF EXISTS `system_tftpserver`;

CREATE TABLE `system_tftpserver` (
  `id` bigint(11) NOT NULL auto_increment,
  `ip` varchar(15) default NULL,
  `usedflag` int(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_tftpserver` */

insert into `system_tftpserver` (`id`,`ip`,`usedflag`) values (1,'10.10.152.30',1);

/*Table structure for table `system_user` */

DROP TABLE IF EXISTS `system_user`;

CREATE TABLE `system_user` (
  `id` int(5) NOT NULL,
  `user_id` varchar(30) default NULL,
  `name` varchar(30) default NULL,
  `password` varchar(40) default NULL,
  `sex` int(1) default NULL,
  `role_id` int(3) default NULL,
  `dept_id` int(3) default NULL,
  `position_id` int(3) default NULL,
  `phone` varchar(30) default NULL,
  `email` varchar(30) default NULL,
  `mobile` varchar(30) default NULL,
  `businessids` varchar(200) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `system_user` */

insert into `system_user` (`id`,`user_id`,`name`,`password`,`sex`,`role_id`,`dept_id`,`position_id`,`phone`,`email`,`mobile`,`businessids`) values (1,'hukelei','hukelei','9173CD7ED6F444A5C1B61CE055CF65FC',1,0,2,2,'','hukelei@dhcc.com.cn','13811372044','');
insert into `system_user` (`id`,`user_id`,`name`,`password`,`sex`,`role_id`,`dept_id`,`position_id`,`phone`,`email`,`mobile`,`businessids`) values (4,'admin','admin','21232F297A57A5A743894A0E4A801FC3',1,0,2,2,'7586402','hukelei@dhcc.com.cn','',',2,4,5,');
insert into `system_user` (`id`,`user_id`,`name`,`password`,`sex`,`role_id`,`dept_id`,`position_id`,`phone`,`email`,`mobile`,`businessids`) values (6,'users','users','9BC65C2ABEC141778FFAA729489F3E87',1,0,2,2,'','hukelei@dhcc.com.cn','15928045542','null');
insert into `system_user` (`id`,`user_id`,`name`,`password`,`sex`,`role_id`,`dept_id`,`position_id`,`phone`,`email`,`mobile`,`businessids`) values (7,'netadmin','netadmin','07DED64F812AFB8DA181014A9D087728',1,0,2,2,'','hukelei@dhcc.com.cn','13981928489','null');

/*Table structure for table `test_status` */

DROP TABLE IF EXISTS `test_status`;

CREATE TABLE `test_status` (
  `id` int(3) NOT NULL,
  `descr` varchar(100) default NULL,
  `status` tinyint(2) default NULL,
  `log_time` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `test_status` */

insert into `test_status` (`id`,`descr`,`status`,`log_time`) values (1,'oa状态',1,'2007-04-24 12:37:29');
insert into `test_status` (`id`,`descr`,`status`,`log_time`) values (2,'internet状态',1,'2006-11-25');

/*Table structure for table `topo_custom_xml` */

DROP TABLE IF EXISTS `topo_custom_xml`;

CREATE TABLE `topo_custom_xml` (
  `id` int(5) NOT NULL,
  `xml_name` varchar(50) default NULL,
  `view_name` varchar(50) default NULL,
  `default_view` tinyint(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_custom_xml` */

/*Table structure for table `topo_discover_config` */

DROP TABLE IF EXISTS `topo_discover_config`;

CREATE TABLE `topo_discover_config` (
  `id` int(3) NOT NULL,
  `address` varchar(15) default NULL,
  `community` varchar(20) default NULL,
  `flag` varchar(10) default NULL,
  `shieldnetstart` varchar(15) default NULL,
  `shieldnetend` varchar(15) default NULL,
  `includenetstart` varchar(15) default NULL,
  `includenetend` varchar(15) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_discover_config` */

insert into `topo_discover_config` (`id`,`address`,`community`,`flag`,`shieldnetstart`,`shieldnetend`,`includenetstart`,`includenetend`) values (1,'10.10.1.1','dhcc_public','core',NULL,NULL,NULL,NULL);

/*Table structure for table `topo_discover_stat` */

DROP TABLE IF EXISTS `topo_discover_stat`;

CREATE TABLE `topo_discover_stat` (
  `id` int(3) NOT NULL,
  `start_time` varchar(20) default NULL,
  `end_time` varchar(20) default NULL,
  `elapse_time` varchar(20) default NULL,
  `host_total` int(3) default NULL,
  `subnet_total` int(3) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_discover_stat` */

insert into `topo_discover_stat` (`id`,`start_time`,`end_time`,`elapse_time`,`host_total`,`subnet_total`) values (1,'2011-01-25 16:30:36','2011-01-25 16:44:33','00:13:57',11,28);
insert into `topo_discover_stat` (`id`,`start_time`,`end_time`,`elapse_time`,`host_total`,`subnet_total`) values (2,'2011-01-25 16:58:17','2011-01-25 17:12:15','00:13:58',11,28);
insert into `topo_discover_stat` (`id`,`start_time`,`end_time`,`elapse_time`,`host_total`,`subnet_total`) values (3,'2011-01-25 17:26:09','2011-01-25 17:39:36','00:13:27',11,28);
insert into `topo_discover_stat` (`id`,`start_time`,`end_time`,`elapse_time`,`host_total`,`subnet_total`) values (4,'2011-01-25 18:03:56','2011-01-25 18:17:23','00:13:27',11,28);
insert into `topo_discover_stat` (`id`,`start_time`,`end_time`,`elapse_time`,`host_total`,`subnet_total`) values (5,'2011-01-25 18:37:13','2011-01-25 18:50:40','00:13:27',11,28);
insert into `topo_discover_stat` (`id`,`start_time`,`end_time`,`elapse_time`,`host_total`,`subnet_total`) values (6,'2011-01-25 19:44:23','2011-01-25 19:58:19','00:13:56',11,28);

/*Table structure for table `topo_equip_pic` */

DROP TABLE IF EXISTS `topo_equip_pic`;

CREATE TABLE `topo_equip_pic` (
  `id` int(10) NOT NULL,
  `category` int(10) default NULL,
  `cn_name` varchar(50) default NULL,
  `en_name` varchar(50) default NULL,
  `topo_image` varchar(50) default NULL,
  `lost_image` varchar(50) default NULL,
  `alarm_image` varchar(50) default NULL,
  `path` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_equip_pic` */

insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (1,1,'路由器','net_router','router.png','router_lost.png','router_alarm3.gif','/afunms/resource/image/topo/router.png');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (2,2,'路由交换机','net_switch_router','switch_with_route.png','switch_with_route_lost.png','switch_with_route_alarm3.gif','/afunms/resource/image/topo/switch_with_route.png');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (3,3,'交换机','net_switch','switch.png','switch_lost.png','switch_alarm3.gif','/afunms/resource/image/topo/switch.png');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (4,4,'主机','net_server','server.gif','server_lost.gif','server_alarm.gif','/afunms/resource/image/topo/server.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (5,5,'打印机','printer','printer.gif','printer_lost.gif','printer_alarm.gif','/afunms/resource/image/topo/printer.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (6,7,'无线路由器','net_wireless','wireless.png','wireless_lost.png','wireless.png','/afunms/resource/image/topo/wireless.png');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (7,50,'IP地址','ip_node','terminal.gif','terminal_lost.gif','terminal_alarm.gif','/afunms/resource/image/topo/terminal.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (8,51,'Tomcat','tomcat','tomcat.gif','tomcat_lost.gif','tomcat_alarm.gif','/afunms/resource/image/topo/tomcat.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (9,52,'MySQL','mysql','mysql.gif','mysql_lost.gif','mysql_alarm.gif','/afunms/resource/image/topo/mysql.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (10,53,'Oracle','oracle','oracle.gif','oracle_lost.gif','oracle_alarm.gif','/afunms/resource/image/topo/oracle.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (11,54,'SQL-Server','sql-server','mssql.gif','mssql_lost.gif','mssql_alarm.gif','/afunms/resource/image/topo/mssql.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (12,55,'Sybase','sybase','sybase.gif','sybase_lost.gif','sybase_alarm.gif','/afunms/resource/image/topo/sybase.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (13,101,'UPS','ups','ups.jpg','ups_lost.jpg','ups_alarm.gif','/afunms/resource/image/topo/ups.jpg');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (18,55,'Sybase','sybase','sybase1.gif','sybase_lost1.gif','sybase_alarm1.gif','/afunms/resource/image/topo/sybase1.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (19,4,'主机','net_server','win_xp.gif','','win_xp_alarm.gif','/afunms/resource/image/topo/win_xp.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (20,4,'主机','net_server','ibm.gif','','ibm_alarm.gif','/afunms/resource/image/topo/ibm.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (21,4,'主机','net_server','win_2000.gif','','win_2000_alarm.gif','/afunms/resource/image/topo/win_2000.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (22,4,'主机','net_server','win_nt.gif','','win_nt_alarm.gif','/afunms/resource/image/topo/win_nt.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (23,4,'主机','net_server','linux.gif','','linux_alarm.gif','/afunms/resource/image/topo/linux.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (24,4,'主机','net_server','solaris.gif','','solaris_alarm.gif','/afunms/resource/image/topo/solaris.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (25,4,'主机','net_server','hp.gif','','hp_alarm.gif','/afunms/resource/image/topo/hp.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (26,4,'主机','net_server','compaq.gif','','compaq_alarm.gif','/afunms/resource/image/topo/compaq.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (27,3,'交换机','net_switch','Switch-B-32.gif',NULL,'switch-alarm.gif','/afunms/resource/image/topo/Switch-B-32.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (28,61,'MQ','mq','mq.jpg','mq_lost.jpg','mq_alarm.gif','/afunms/resource/image/topo/mq.jpg');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (29,62,'Domino','domino','domino.gif','domino_lost.jpg','domino_alarm.gif','/afunms/resource/image/topo/domino.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (30,63,'WAS','was','webphere.gif','was_lost.jpg','was_alarm.gif','/afunms/resource/image/topo/webphere.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (31,64,'Weblogic','weblogic','bea_32.gif','weblogic_lost.jpg','bea_32_alarm.gif','/afunms/resource/image/topo/weblogic.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (32,65,'CICS','cics','cics.jpg','cics_lost.jpg','cics_alarm.gif','/afunms/resource/image/topo/cics.jpg');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (33,66,'防火墙','firewall','firewall.gif','firewall_lost.jpg','firewall_alarm.gif','/afunms/resource/image/topo/firewall.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (34,56,'邮件服务','mail','mail.gif','mail_lost.gif','mail_alarm.gif','/afunms/resource/image/topo/mail.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (35,57,'WEB服务','web','web.gif','web_lost.gif','web_alarm.gif','/afunms/resource/image/topo/web.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (36,67,'IIS','iis','iis.gif','iis_lost.gif','iis_alarm.gif','/afunms/resource/image/topo/iis.gif');
insert into `topo_equip_pic` (`id`,`category`,`cn_name`,`en_name`,`topo_image`,`lost_image`,`alarm_image`,`path`) values (37,68,'Socket','socket','socket.gif','socket_lost.gif','socket_alarm.gif','/afunms/resource/image/topo/socket.gif');

/*Table structure for table `topo_hint_meta` */

DROP TABLE IF EXISTS `topo_hint_meta`;

CREATE TABLE `topo_hint_meta` (
  `icon_id` varchar(255) NOT NULL,
  `icon_path` varchar(255) NOT NULL,
  `sort_name` varchar(255) default NULL,
  `icon_name` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  `web_icon_path` varchar(255) NOT NULL,
  PRIMARY KEY  (`icon_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `topo_hint_meta` */

insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('11111111111111','image\\topo\\up.gif','标题','up','1','/afunms/resource/image/topo/up.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('21312123222222','image\\topo\\wireless\\1.png','无线设备','1','28','/afunms/resource/image/topo/wireless/1.png');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('23231414478788','image\\topo\\unit\\2.gif','Unit集','2','26','/afunms/resource/image/topo/unit/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933552548997','image\\topo\\router\\1.gif','路由器','1','2','/afunms/resource/image/topo/router/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933554569645','image\\topo\\router\\2-1.gif','路由器','2','2','/afunms/resource/image/topo/router/2-1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933556455638','image\\topo\\router\\3-1.gif','路由器','3','2','/afunms/resource/image/topo/router/3-1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933558407842','image\\topo\\router\\1.gif','路由器','4','2','/afunms/resource/image/topo/router/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933560762890','image\\topo\\router\\2-1.gif','路由器','5','2','/afunms/resource/image/topo/router/2-1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933562884388','image\\topo\\router\\3-1.gif','路由器','6','2','/afunms/resource/image/topo/router/3-1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933600171250','image\\topo\\switch\\1.gif','交换机','1','3','/afunms/resource/image/topo/switch/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933601992990','image\\topo\\switch\\2.gif','交换机','2','3','/afunms/resource/image/topo/switch/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933603881778','image\\topo\\switch\\3.gif','交换机','3','3','/afunms/resource/image/topo/switch/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933643840208','image\\topo\\switch3\\1.gif','三层交换机','1','4','/afunms/resource/image/topo/switch3/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933645365262','image\\topo\\switch3\\2.gif','三层交换机','2','4','/afunms/resource/image/topo/switch3/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933646969377','image\\topo\\switch3\\3.gif','三层交换机','3','4','/afunms/resource/image/topo/switch3/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933648611206','image\\topo\\switch3\\4.gif','三层交换机','4','4','/afunms/resource/image/topo/switch3/4.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933690112290','image\\topo\\firewall\\3.gif','防火墙','2','5','/afunms/resource/image/topo/firewall/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933691805243','image\\topo\\firewall\\4.gif','防火墙','3','5','/afunms/resource/image/topo/firewall/4.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933727445806','image\\topo\\firewall\\5.gif','防火墙','4','5','/afunms/resource/image/topo/firewall/5.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933729538251','image\\topo\\firewall\\3.gif','防火墙','5','5','/afunms/resource/image/topo/firewall/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933731611419','image\\topo\\firewall\\4.gif','防火墙','6','5','/afunms/resource/image/topo/firewall/4.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933754277149','image\\topo\\server\\1.gif','服务器','1','6','/afunms/resource/image/topo/server/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933760410331','image\\topo\\server\\22.gif','服务器','2','6','/afunms/resource/image/topo/server/22.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933766741024','image\\topo\\server\\33.gif','服务器','3','6','/afunms/resource/image/topo/server/33.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933772772796','image\\topo\\server\\44.gif','服务器','4','6','/afunms/resource/image/topo/server/44.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933772772797','image\\topo\\server\\55.gif','服务器','5','6','/afunms/resource/image/topo/server/55.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933772772798','image\\topo\\server\\11.gif','服务器','6','6','/afunms/resource/image/topo/server/11.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933881477115','image\\topo\\printer\\1.gif','打印机','1','7','/afunms/resource/image/topo/printer/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933883663705','image\\topo\\printer\\2.gif','打印机','2','7','/afunms/resource/image/topo/printer/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933889403541','image\\topo\\printer\\3.gif','打印机','3','7','/afunms/resource/image/topo/printer/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933891824798','image\\topo\\printer\\4.gif','打印机','4','7','/afunms/resource/image/topo/printer/4.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933906919454','image\\topo\\ups\\1.gif','UPS','1','8','/afunms/resource/image/topo/ups/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933909091797','image\\topo\\ups\\2.gif','UPS','2','8','/afunms/resource/image/topo/ups/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933915659392','image\\topo\\ups\\3.gif','UPS','3','8','/afunms/resource/image/topo/ups/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933994308773','image\\topo\\hub\\1.gif','Hub','1','9','/afunms/resource/image/topo/hub/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27933997093205','image\\topo\\hub\\2.gif','Hub','2','9','/afunms/resource/image/topo/hub/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934106481410','image\\topo\\jigui\\1.png','机柜','1','10','/afunms/resource/image/topo/jigui/1.png');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934256524223','image\\topo\\cpu\\1.gif','CPU','1','11','/afunms/resource/image/topo/cpu/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934258471956','image\\topo\\cpu\\2.gif','CPU','2','11','/afunms/resource/image/topo/cpu/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934416736764','image\\topo\\webservice\\1.gif','网页服务','1','12','/afunms/resource/image/topo/webservice/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934418700980','image\\topo\\webservice\\2.gif','网页服务','2','12','/afunms/resource/image/topo/webservice/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934420497856','image\\topo\\webservice\\3.gif','网页服务','3','12','/afunms/resource/image/topo/webservice/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934428194924','image\\topo\\mail\\1.gif','邮件服务','1','13','/afunms/resource/image/topo/mail/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934428194925','image\\topo\\mail\\1.gif','邮件服务','2','13','/afunms/resource/image/topo/mail/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934450352209','image\\topo\\ftpservice\\1.gif','文件传输服务','1','14','/afunms/resource/image/topo/ftpservice/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934509164712','image\\topo\\mysql\\11.gif','Mysql系列','1','15','/afunms/resource/image/topo/mysql/11.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934510723010','image\\topo\\mysql\\22.gif','Mysql系列','2','15','/afunms/resource/image/topo/mysql/22.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934510723011','image\\topo\\mysql\\33.gif','Mysql系列','3','15','/afunms/resource/image/topo/mysql/33.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934537416906','image\\topo\\sqlserver\\1.gif','Sqlserver系列','1','16','/afunms/resource/image/topo/sqlserver/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934539003979','image\\topo\\sqlserver\\2.gif','Sqlserver系列','2','16','/afunms/resource/image/topo/sqlserver/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934565916897','image\\topo\\oracle\\1.gif','Oracle系列','1','17','/afunms/resource/image/topo/oracle/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934567490840','image\\topo\\oracle\\2.gif','Oracle系列','2','17','/afunms/resource/image/topo/oracle/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934567490841','image\\topo\\oracle\\3.gif','Oracle系列','3','17','/afunms/resource/image/topo/oracle/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934594878119','image\\topo\\sybase\\1.gif','Sybase系列','1','18','/afunms/resource/image/topo/sybase/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934596573586','image\\topo\\sybase\\2.gif','Sybase系列','2','18','/afunms/resource/image/topo/sybase/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934613997868','image\\topo\\weblogic\\11.gif','Weblogic系列','1','19','/afunms/resource/image/topo/weblogic/11.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934615555608','image\\topo\\weblogic\\22.gif','Weblogic系列','2','19','/afunms/resource/image/topo/weblogic/22.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934665503855','image\\topo\\websphere\\1.gif','Websphere系列','1','20','/afunms/resource/image/topo/websphere/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934667372808','image\\topo\\websphere\\2.gif','Websphere系列','2','20','/afunms/resource/image/topo/websphere/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934740825719','image\\topo\\tomcat\\1.gif','Tomcat系列','1','21','/afunms/resource/image/topo/tomcat/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934742709478','image\\topo\\tomcat\\2.gif','Tomcat系列','2','21','/afunms/resource/image/topo/tomcat/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934765043881','image\\topo\\db2\\1.gif','DB2系列','1','22','/afunms/resource/image/topo/db2/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934767097494','image\\topo\\db2\\2.gif','DB2系列','2','22','/afunms/resource/image/topo/db2/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934767097495','image\\topo\\db2\\3.gif','DB2系列','3','22','/afunms/resource/image/topo/db2/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934836760538','image\\topo\\Apache\\1.gif','Apache系列','1','23','/afunms/resource/image/topo/Apache/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934844358989','image\\topo\\Apache\\a_5.gif','Apache系列','2','23','/afunms/resource/image/topo/Apache/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934858976210','image\\topo\\jboss\\1.gif','JBoss系列','1','24','/afunms/resource/image/topo/jboss/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27934860674471','image\\topo\\jboss\\2.gif','JBoss系列','2','24','/afunms/resource/image/topo/jboss/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27935267018294','image\\topo\\unit\\1.png','Unit集','1','26','/afunms/resource/image/topo/unit/1.png');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27935290622970','image\\topo\\area\\1.gif','地域','1','27','/afunms/resource/image/topo/area/1.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('27935292600037','image\\topo\\area\\2.gif','地域','2','27','/afunms/resource/image/topo/area/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('31223231122222','image\\topo\\wireless\\2.png','无线设备','2','28','/afunms/resource/image/topo/wireless/2.png');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('31231232132132','image\\topo\\LAC\\LAC.png','LAC','LAC','29','/afunms/resource/image/topo/LAC/LAC.png');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('32112321312333','image\\topo\\jigui\\3.gif','机柜','3','10','/afunms/resource/image/topo/jigui/3.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('32131231232131','image\\topo\\jigui\\2.gif','机柜','2','10','/afunms/resource/image/topo/jigui/2.gif');
insert into `topo_hint_meta` (`icon_id`,`icon_path`,`sort_name`,`icon_name`,`id`,`web_icon_path`) values ('32334545467777','image\\topo\\WAP\\WAP.png','WAP','WAP','30','/afunms/resource/image/topo/WAP/WAP.png');

/*Table structure for table `topo_host_node` */

DROP TABLE IF EXISTS `topo_host_node`;

CREATE TABLE `topo_host_node` (
  `id` int(5) NOT NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(10) default NULL,
  `sys_name` varchar(50) default NULL,
  `alias` varchar(50) default NULL,
  `net_mask` varchar(15) default NULL,
  `sys_descr` mediumtext,
  `sys_oid` varchar(50) default NULL,
  `community` varchar(20) default NULL,
  `write_community` varchar(20) default NULL,
  `category` int(3) default NULL,
  `managed` tinyint(1) default NULL,
  `type` varchar(100) default NULL,
  `super_node` int(3) default NULL,
  `local_net` int(3) default NULL,
  `layer` int(1) default NULL,
  `bridge_address` varchar(50) default NULL,
  `status` int(3) default NULL,
  `discoverstatus` int(3) default NULL,
  `sys_location` varchar(100) default NULL,
  `sys_contact` varchar(100) default NULL,
  `snmpversion` int(3) default NULL,
  `collecttype` int(3) default NULL,
  `ostype` int(3) default NULL,
  `sendmobiles` varchar(200) default NULL,
  `sendemail` varchar(200) default NULL,
  `bid` varchar(200) default NULL,
  `endpoint` int(2) default '0',
  `sendphone` varchar(200) default NULL,
  `supperid` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_host_node` */

/*Table structure for table `topo_interface` */

DROP TABLE IF EXISTS `topo_interface`;

CREATE TABLE `topo_interface` (
  `id` int(10) NOT NULL,
  `node_id` int(5) default NULL,
  `entity` varchar(30) default NULL,
  `descr` varchar(100) default NULL,
  `port` varchar(10) default NULL,
  `speed` varchar(15) default NULL,
  `alias` varchar(100) default NULL,
  `phys_address` varchar(50) default NULL,
  `ip_address` text,
  `oper_status` tinyint(1) default NULL,
  `type` int(10) default NULL,
  `chassis` int(5) default NULL,
  `slot` int(5) default NULL,
  `uport` int(5) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_interface` */

/*Table structure for table `topo_interface_data` */

DROP TABLE IF EXISTS `topo_interface_data`;

CREATE TABLE `topo_interface_data` (
  `node_id` int(5) default NULL,
  `entity` varchar(50) default NULL,
  `moid` varchar(6) default NULL,
  `value` int(10) default NULL,
  `percentage` float(6,2) default NULL,
  `log_time` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_interface_data` */

/*Table structure for table `topo_interface_threshold` */

DROP TABLE IF EXISTS `topo_interface_threshold`;

CREATE TABLE `topo_interface_threshold` (
  `if_id` int(10) NOT NULL,
  `threshold` int(10) default NULL,
  `compare_type` tinyint(1) default '1',
  `upper_times` tinyint(2) default '1',
  `enable` tinyint(1) default '1',
  PRIMARY KEY  (`if_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_interface_threshold` */

/*Table structure for table `topo_ipalias` */

DROP TABLE IF EXISTS `topo_ipalias`;

CREATE TABLE `topo_ipalias` (
  `id` int(10) NOT NULL auto_increment,
  `ipaddress` varchar(30) default NULL,
  `aliasip` varchar(30) default NULL,
  `indexs` int(11) default NULL,
  `descr` varchar(200) default NULL,
  `speeds` varchar(100) default NULL,
  `types` varchar(50) default NULL,
  `usedflag` tinyint(2) default NULL,
  UNIQUE KEY `ipaliasindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_ipalias` */

/*Table structure for table `topo_manage_xml` */

DROP TABLE IF EXISTS `topo_manage_xml`;

CREATE TABLE `topo_manage_xml` (
  `id` int(5) NOT NULL,
  `xml_name` varchar(50) default NULL COMMENT '生成的xml文件名',
  `topo_name` varchar(100) default NULL COMMENT '拓扑图名称',
  `alias_name` varchar(100) default NULL COMMENT '别名',
  `topo_title` varchar(100) default NULL COMMENT '标题',
  `topo_area` varchar(100) default NULL COMMENT '地域',
  `topo_bg` varchar(50) default NULL COMMENT '背景图片',
  `topo_type` tinyint(1) default '0' COMMENT '拓扑图类型:0.默认 1.业务拓扑图 2.示意拓扑图 3.缩略拓扑图 4.子图',
  `relation_node` varchar(50) default NULL COMMENT ' 关联节点',
  `default_view` tinyint(1) default '0',
  `bid` varchar(50) default NULL,
  `home_view` int(2) default '0',
  `bus_home_view` int(2) default '0',
  `zoom_percent` float default '1',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_manage_xml` */

insert into `topo_manage_xml` (`id`,`xml_name`,`topo_name`,`alias_name`,`topo_title`,`topo_area`,`topo_bg`,`topo_type`,`relation_node`,`default_view`,`bid`,`home_view`,`bus_home_view`,`zoom_percent`) values (1,'network.jsp','物理根图','null','物理根图','null','china_1.gif',0,NULL,1,',2,3,',1,0,0.4);
insert into `topo_manage_xml` (`id`,`xml_name`,`topo_name`,`alias_name`,`topo_title`,`topo_area`,`topo_bg`,`topo_type`,`relation_node`,`default_view`,`bid`,`home_view`,`bus_home_view`,`zoom_percent`) values (2,'submap1295835093.jsp','1','null','1','null','0',4,NULL,0,',2,4,5,6,',0,0,1);
insert into `topo_manage_xml` (`id`,`xml_name`,`topo_name`,`alias_name`,`topo_title`,`topo_area`,`topo_bg`,`topo_type`,`relation_node`,`default_view`,`bid`,`home_view`,`bus_home_view`,`zoom_percent`) values (3,'submap1295835190.jsp','33','null','3','null','0',4,NULL,0,',2,4,5,6,',0,0,1);

/*Table structure for table `topo_network_link` */

DROP TABLE IF EXISTS `topo_network_link`;

CREATE TABLE `topo_network_link` (
  `id` int(3) NOT NULL,
  `start_id` int(3) default NULL,
  `start_index` varchar(15) default '',
  `start_ip` varchar(15) default '',
  `start_descr` varchar(100) default '',
  `start_port` varchar(10) default '',
  `start_mac` varchar(20) default '',
  `end_id` int(3) default NULL,
  `end_ip` varchar(15) default '',
  `end_index` varchar(15) default '',
  `end_descr` varchar(100) default '',
  `end_port` varchar(10) default '',
  `end_mac` varchar(20) default '',
  `assistant` tinyint(1) default '0',
  `type` tinyint(1) default NULL,
  `findtype` int(2) default '-1',
  `linktype` int(2) default '-1',
  `link_name` varchar(100) default NULL,
  `max_speed` varchar(50) default NULL,
  `max_per` varchar(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_network_link` */

/*Table structure for table `topo_node_equip` */

DROP TABLE IF EXISTS `topo_node_equip`;

CREATE TABLE `topo_node_equip` (
  `id` int(10) NOT NULL,
  `xml_name` varchar(100) default NULL,
  `node_id` varchar(50) default NULL,
  `equip_id` int(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_node_equip` */

/*Table structure for table `topo_node_id` */

DROP TABLE IF EXISTS `topo_node_id`;

CREATE TABLE `topo_node_id` (
  `id` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_node_id` */

insert into `topo_node_id` (`id`) values (65);

/*Table structure for table `topo_node_monitor` */

DROP TABLE IF EXISTS `topo_node_monitor`;

CREATE TABLE `topo_node_monitor` (
  `id` int(5) NOT NULL,
  `node_id` int(5) default NULL,
  `moid` varchar(6) default NULL,
  `threshold` int(10) default NULL,
  `threshold_unit` varchar(10) default NULL,
  `compare` tinyint(1) default '1',
  `compare_type` tinyint(1) default '1',
  `upper_times` tinyint(2) default '1',
  `alarm_info` varchar(100) default '',
  `alarm_level` tinyint(1) default '1',
  `enabled` tinyint(1) default '1',
  `poll_interval` int(5) default NULL,
  `interval_unit` char(1) default NULL,
  `nodetype` varchar(10) default NULL,
  `subentity` varchar(50) default NULL,
  `limenvalue0` bigint(20) default NULL,
  `limenvalue1` bigint(20) default NULL,
  `limenvalue2` bigint(20) default NULL,
  `time0` int(3) default NULL,
  `time1` int(3) default NULL,
  `time2` int(3) default NULL,
  `sms0` int(2) default NULL,
  `sms1` int(2) default NULL,
  `sms2` int(2) default NULL,
  `node_ip` varchar(15) default NULL,
  `category` varchar(50) default NULL,
  `descr` varchar(50) default NULL,
  `unit` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_node_monitor` */

/*Table structure for table `topo_node_multi_data` */

DROP TABLE IF EXISTS `topo_node_multi_data`;

CREATE TABLE `topo_node_multi_data` (
  `node_id` int(5) NOT NULL,
  `entity` varchar(50) default NULL,
  `moid` varchar(6) default NULL,
  `value` bigint(10) default NULL,
  `percentage` float(6,2) default NULL,
  `log_time` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_node_multi_data` */

/*Table structure for table `topo_node_single_data` */

DROP TABLE IF EXISTS `topo_node_single_data`;

CREATE TABLE `topo_node_single_data` (
  `node_id` int(5) NOT NULL,
  `moid` varchar(6) default NULL,
  `value` int(10) default NULL,
  `log_time` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_node_single_data` */

/*Table structure for table `topo_node_telnetconfig` */

DROP TABLE IF EXISTS `topo_node_telnetconfig`;

CREATE TABLE `topo_node_telnetconfig` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) DEFAULT '',
  `user` varchar(100) DEFAULT '',
  `password` varchar(100) DEFAULT '',
  `port` int(10) DEFAULT '23',
  `suuser` varchar(100) DEFAULT '',
  `supassword` varchar(100) DEFAULT '',
  `default_promtp` varchar(50) DEFAULT '',
  `enablevpn` int(11) DEFAULT '0',
  `is_synchronized` int(11) DEFAULT NULL,
  `device_render` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=gb2312;

/*Data for the table `topo_node_telnetconfig` */

/*Table structure for table `topo_other_node` */

DROP TABLE IF EXISTS `topo_other_node`;

CREATE TABLE `topo_other_node` (
  `id` int(10) NOT NULL auto_increment,
  `name` varchar(50) character set gb2312 default NULL,
  `ipAddress` varchar(30) character set gb2312 default NULL,
  `alais` varchar(50) character set gb2312 default NULL,
  `category` int(5) default NULL,
  `collecttype` varchar(20) character set gb2312 default '代理',
  `sendmobiles` varchar(100) character set gb2312 default NULL,
  `sendemail` varchar(100) character set gb2312 default NULL,
  `sendphone` varchar(100) character set gb2312 default NULL,
  `bid` varchar(100) character set gb2312 default NULL,
  `managed` int(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `topo_other_node` */

/*Table structure for table `topo_repair_link` */

DROP TABLE IF EXISTS `topo_repair_link`;

CREATE TABLE `topo_repair_link` (
  `id` int(3) NOT NULL,
  `start_index` varchar(15) default '',
  `start_ip` varchar(15) default '',
  `end_ip` varchar(15) default '',
  `end_index` varchar(15) default '',
  `new_start_index` varchar(15) default '',
  `new_end_index` varchar(15) default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_repair_link` */

/*Table structure for table `topo_subnet` */

DROP TABLE IF EXISTS `topo_subnet`;

CREATE TABLE `topo_subnet` (
  `id` int(5) NOT NULL,
  `net_address` varchar(15) default NULL,
  `net_mask` varchar(15) default NULL,
  `net_long` bigint(10) default NULL,
  `managed` tinyint(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Data for the table `topo_subnet` */

SET SQL_MODE=@OLD_SQL_MODE;