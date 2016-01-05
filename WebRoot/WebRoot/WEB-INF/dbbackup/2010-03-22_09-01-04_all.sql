-- MySQL dump 10.11
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.0.41-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app_db_node`
--

DROP TABLE IF EXISTS `app_db_node`;
CREATE TABLE `app_db_node` (
  `id` int(11) NOT NULL,
  `alias` varchar(30) default NULL,
  `ip_address` varchar(15) default NULL,
  `ip_long` bigint(10) default NULL,
  `category` tinyint(2) default NULL,
  `db_name` varchar(30) default NULL,
  `port` varchar(5) default NULL,
  `user` varchar(30) default NULL,
  `password` varchar(30) default NULL,
  `dbuse` varchar(100) default NULL,
  `sendmobiles` varchar(200) default NULL,
  `sendemail` varchar(200) default NULL,
  `managed` int(2) default NULL,
  `bid` varchar(100) default NULL,
  `dbtype` int(11) default NULL,
  `sendphone` varchar(200) default NULL,
  `collecttype` int(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

--
-- Dumping data for table `app_db_node`
--

LOCK TABLES `app_db_node` WRITE;
/*!40000 ALTER TABLE `app_db_node` DISABLE KEYS */;
INSERT INTO `app_db_node` VALUES (110,'ol_ids_1150_1','10.10.152.59',168466491,60,'informix','9088','informix','webnms','','null','',0,',2,3,',7,'null',0),(116,'脚本测试','172.25.25.3',2887325955,53,'itownet','1521','itsm','itsm2008','测试','null','',0,',2,3,',1,'null',2),(117,'SQL SERVER','10.10.152.57',168466489,54,'测试','1433','sa','hukelei','测试数据库','null','',1,',2,3,',2,'null',2),(118,'afunms','10.10.152.59',168466491,52,'afunms','3306','root','root','','null','',0,',2,3,',4,'null',1),(122,'测试SYBASE','10.10.152.57',168466489,55,'model','5000','sa','rvimlagoyjcnr4','','null','',0,',2,3,',6,'null',1),(124,'DB2测试','172.25.25.3',2887325955,59,'DHCC','50000','root','root','','null','',0,',2,3,',5,'null',1);
/*!40000 ALTER TABLE `app_db_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_ups_node`
--

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

--
-- Dumping data for table `app_ups_node`
--

LOCK TABLES `app_ups_node` WRITE;
/*!40000 ALTER TABLE `app_ups_node` DISABLE KEYS */;
INSERT INTO `app_ups_node` VALUES (204,'梅兰日兰UPS','一楼机房','10.110.1.126',174981502,'public','','Galaxy PW Single//','1.3.6.1.4.1.705.1.2');
/*!40000 ALTER TABLE `app_ups_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bnodeday2_138`
--

DROP TABLE IF EXISTS `bnodeday2_138`;
CREATE TABLE `bnodeday2_138` (
  `ID` bigint(20) NOT NULL auto_increment,
  `THEVALUE` varchar(255) default NULL,
  `RESPONSETIME` varchar(100) default NULL,
  `COLLECTTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

--
-- Dumping data for table `bnodeday2_138`
--

LOCK TABLES `bnodeday2_138` WRITE;
/*!40000 ALTER TABLE `bnodeday2_138` DISABLE KEYS */;
/*!40000 ALTER TABLE `bnodeday2_138` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `db2pingday17225253`
--

DROP TABLE IF EXISTS `db2pingday17225253`;
CREATE TABLE `db2pingday17225253` (
  `ID` bigint(20) NOT NULL auto_increment,
  `IPADDRESS` varchar(30) default NULL,
  `RESTYPE` varchar(20) default NULL,
  `CATEGORY` varchar(50) default NULL,
  `ENTITY` varchar(100) default NULL,
  `SUBENTITY` varchar(60) default NULL,
  `THEVALUE` varchar(255) default NULL,
  `COLLECTTIME` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `UNIT` varchar(30) default NULL,
  `COUNT` bigint(20) default NULL,
  `BAK` varchar(100) default NULL,
  `CHNAME` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

--
-- Dumping data for table `db2pingday17225253`
--

LOCK TABLES `db2pingday17225253` WRITE;
/*!40000 ALTER TABLE `db2pingday17225253` DISABLE KEYS */;
/*!40000 ALTER TABLE `db2pingday17225253` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-03-22  1:01:04
