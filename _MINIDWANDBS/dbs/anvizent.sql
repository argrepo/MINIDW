CREATE DATABASE  IF NOT EXISTS `anvizent` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `anvizent`;
-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: 192.168.0.131    Database: anvizent
-- ------------------------------------------------------
-- Server version	5.6.40

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
-- Table structure for table `AC_CommunicationDetails`
--

DROP TABLE IF EXISTS `AC_CommunicationDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AC_CommunicationDetails` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ReqId` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `CommunicationMode` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Comments` tinytext COLLATE utf8_unicode_ci,
  `Purpose` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Priority` tinyint(4) DEFAULT '4',
  `ComponentName` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `EntityId` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `UserId` int(11) DEFAULT NULL,
  `RequestTime` datetime NOT NULL,
  `SentTime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ReqId` (`ReqId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AC_EmailInfo`
--

DROP TABLE IF EXISTS `AC_EmailInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AC_EmailInfo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `EmailId` varchar(350) COLLATE utf8_unicode_ci NOT NULL,
  `Subject` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `EmailContent` text COLLATE utf8_unicode_ci NOT NULL,
  `ComId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ComId` (`ComId`),
  CONSTRAINT `AC_EmailInfo_ibfk_1` FOREIGN KEY (`ComId`) REFERENCES `AC_CommunicationDetails` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AC_PushNotificationInfo`
--

DROP TABLE IF EXISTS `AC_PushNotificationInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AC_PushNotificationInfo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `DeviceId` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DeviceType` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `NotificationContent` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  `ComId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ComId` (`ComId`),
  CONSTRAINT `AC_PushNotificationInfo_ibfk_1` FOREIGN KEY (`ComId`) REFERENCES `AC_CommunicationDetails` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AC_SMSInfo`
--

DROP TABLE IF EXISTS `AC_SMSInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AC_SMSInfo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `MobileNo` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `SMSText` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  `ComId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ComId` (`ComId`),
  CONSTRAINT `AC_SMSInfo_ibfk_1` FOREIGN KEY (`ComId`) REFERENCES `AC_CommunicationDetails` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CustomerBusinessDetails`
--

DROP TABLE IF EXISTS `CustomerBusinessDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CustomerBusinessDetails` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `LastName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `BusinessEmail` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `PhoneNo` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `OrganizationName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `Address` tinytext COLLATE utf8_unicode_ci,
  `Country` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `State` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `NoOfEmployees` bigint(20) DEFAULT NULL,
  `ERPName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `DatabaseName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `DatabaseVersion` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `TimeZone` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `Connector` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `ConnectorType` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `IpAddress` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `CreatedBy` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UpdatedDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DataModel_Version`
--

DROP TABLE IF EXISTS `DataModel_Version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DataModel_Version` (
  `Current_Version` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Rollout_Date` datetime DEFAULT NULL,
  PRIMARY KEY (`Current_Version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ETL_CNTRL`
--

DROP TABLE IF EXISTS `ETL_CNTRL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ETL_CNTRL` (
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `CTRL_ID` int(11) NOT NULL AUTO_INCREMENT,
  `JOB_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_ORDER_SEQ` int(11) DEFAULT NULL,
  `SOURCE_UPD_DATETIME` datetime DEFAULT NULL,
  `LAST_RUN_DATETIME` datetime DEFAULT NULL,
  `ADDED_DATE` datetime DEFAULT NULL,
  `ADDED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `UPDATED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`CTRL_ID`,`DataSource_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ETL_JOB_ERROR_LOG`
--

DROP TABLE IF EXISTS `ETL_JOB_ERROR_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ETL_JOB_ERROR_LOG` (
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `ERROR_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BATCH_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ERROR_CODE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ERROR_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ERROR_MSG` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ERROR_SYNTAX` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DATA_VALUE_SET` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ADDED_DATETIME` datetime DEFAULT NULL,
  `ADDED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATETIME` datetime DEFAULT NULL,
  `UPDATED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ERROR_ID`,`DataSource_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ETL_JOB_LOAD_SMRY`
--

DROP TABLE IF EXISTS `ETL_JOB_LOAD_SMRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ETL_JOB_LOAD_SMRY` (
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `BATCH_ID` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_PARAMETERS` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SRC_COUNT` int(11) DEFAULT NULL,
  `TGT_INSERT_COUNT` int(11) DEFAULT NULL,
  `TGT_UPDATE_COUNT` int(11) DEFAULT NULL,
  `TGT_DELETE_COUNT` int(11) DEFAULT NULL,
  `ERROR_ROWS_COUNT` int(11) DEFAULT NULL,
  `JOB_START_DATETIME` datetime DEFAULT NULL,
  `JOB_END_DATETIME` datetime DEFAULT NULL,
  `JOB_RUN_STATUS` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_LOG_FILE_LINK` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ADDED_DATETIME` datetime DEFAULT NULL,
  `ADDED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATETIME` datetime DEFAULT NULL,
  `UPDATED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Console_Output` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`BATCH_ID`,`DataSource_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ETL_Jobs`
--

DROP TABLE IF EXISTS `ETL_Jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ETL_Jobs` (
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_ID` int(10) NOT NULL AUTO_INCREMENT,
  `JOB_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_DESCRIPTION` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_TYPE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SOURCE_TBL_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TARGET_TBL_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ADDED_DATETIME` datetime DEFAULT NULL,
  `ADDED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATETIME` datetime DEFAULT NULL,
  `UPDATED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`JOB_ID`,`DataSource_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ETL_QRY_PROCESS`
--

DROP TABLE IF EXISTS `ETL_QRY_PROCESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ETL_QRY_PROCESS` (
  `job_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'JOB',
  `qry_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'QRY',
  `qry` mediumtext COLLATE utf8_unicode_ci,
  `valid` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`job_name`,`qry_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Currency_Rate`
--

DROP TABLE IF EXISTS `IL_Currency_Rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Currency_Rate` (
  `CURR_RATE_ID` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Surrogate Key',
  `From_Currency_Code` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'From Currency Code that Transaction set',
  `To_Currency_Code` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To currency code that need to conversion applied',
  `EXCHANGE_RATE` decimal(24,10) NOT NULL DEFAULT '0.0000000000' COMMENT 'Average Exchange/Financial/Account rate for the Caluclation Period.',
  `CURRENCY_START_PERIOD` datetime NOT NULL COMMENT 'Start Date Period from Currecy  Rate changes',
  `CURRENCY_END_PERIOD` datetime NOT NULL COMMENT 'End Date Period of Currecy  Rate changes valid',
  `RATE_FREQUENCY` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plan to enter rates for the selected Exchange rate.',
  `CALUCLATION_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'Multiply' COMMENT 'Multiply or Divide  Default "Multiply"',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `IS_LOCKED` bit(1) DEFAULT NULL COMMENT 'Currency entry will be locked fo previous period. Not allowed to update the currency record.This may impact DL tables data',
  `ADDED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `ADDED_DATE` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `UPDATED_DATE` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `UPDATED_USER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user',
  PRIMARY KEY (`CURR_RATE_ID`),
  KEY `IL_Currency_Rate_ExchangeRate` (`EXCHANGE_RATE`),
  KEY `IL_Currency_Rate_StrtPerd` (`CURRENCY_START_PERIOD`),
  KEY `IL_Currency_Rate_EndPerd` (`CURRENCY_END_PERIOD`),
  KEY `IL_Currency_Rate_FKIndex1` (`From_Currency_Code`),
  KEY `IL_Currency_Rate_FKIndex2` (`To_Currency_Code`),
  KEY `IL_Currency_Rate_FKIndex3` (`DataSource_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4685 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OrdersBooking_Temp`
--

DROP TABLE IF EXISTS `OrdersBooking_Temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OrdersBooking_Temp` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `Company` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `WO` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `OrderDate` date DEFAULT NULL,
  `PONumber` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `Country` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `CustomerName` varchar(200) CHARACTER SET latin1 DEFAULT NULL,
  `PlantName` varchar(200) CHARACTER SET latin1 DEFAULT NULL,
  `SalesRepName` varchar(245) CHARACTER SET latin1 DEFAULT NULL,
  `OrderNumber` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `Order_Line` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `Product_Code` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `Part_Number` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `Part_Description` varchar(500) CHARACTER SET latin1 DEFAULT NULL,
  `Sector` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `Order_Qty` int(11) DEFAULT NULL,
  `Unit_Price` double DEFAULT NULL,
  `Gross_Amount` double DEFAULT NULL,
  `Discount_Percent` double DEFAULT NULL,
  `BookingValue` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserInfo`
--

DROP TABLE IF EXISTS `UserInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activation_links_log`
--

DROP TABLE IF EXISTS `activation_links_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activation_links_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email_id` varchar(100) NOT NULL,
  `random_id` varchar(45) NOT NULL,
  `client_id` int(10) NOT NULL,
  `type` varchar(45) NOT NULL,
  `sent_date_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `expiry_days` int(3) DEFAULT '1',
  `is_used` char(1) DEFAULT 'N',
  `used_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_EMAIL_RANDOM` (`email_id`,`random_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activation_test123`
--

DROP TABLE IF EXISTS `activation_test123`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activation_test123` (
  `id&` bigint(20) NOT NULL AUTO_INCREMENT,
  `email_id` varchar(100) NOT NULL,
  `random_id` varchar(45) NOT NULL,
  `client_id` int(10) NOT NULL,
  `type` varchar(45) NOT NULL,
  `sent_date_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `expiry_days` int(3) DEFAULT '1',
  `is_used` char(1) DEFAULT 'N',
  `used_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id&`),
  UNIQUE KEY `UNIQUE_EMAIL_RANDOM` (`id&`,`random_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activation_test12356`
--

DROP TABLE IF EXISTS `activation_test12356`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activation_test12356` (
  `id&` bigint(20) NOT NULL AUTO_INCREMENT,
  `random_id` varchar(45) NOT NULL,
  `client_id` int(10) NOT NULL,
  `type` varchar(45) NOT NULL,
  `to` varchar(45) NOT NULL,
  `from` varchar(45) NOT NULL,
  `is_used` char(1) DEFAULT 'N',
  PRIMARY KEY (`id&`),
  UNIQUE KEY `UNIQUE_EMAIL_RANDOM` (`id&`,`random_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activity_groups_mapping`
--

DROP TABLE IF EXISTS `activity_groups_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_groups_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `admin_access` bit(1) DEFAULT b'0',
  `assign_access` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `fk_activity_activity_groups_mapping` (`activity_id`),
  KEY `fk_activity_groups_activity_groups_mapping` (`group_id`),
  CONSTRAINT `fk_activity_activity_groups_mapping` FOREIGN KEY (`activity_id`) REFERENCES `activity_master` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_groups_activity_groups_mapping` FOREIGN KEY (`group_id`) REFERENCES `activity_groups_master` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1563 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activity_groups_master`
--

DROP TABLE IF EXISTS `activity_groups_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_groups_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL,
  `group_creation_date` datetime DEFAULT NULL,
  `group_updation_date` datetime DEFAULT NULL,
  `group_created_by` varchar(45) DEFAULT NULL,
  `group_updated_by` varchar(45) DEFAULT NULL,
  `group_status` int(2) NOT NULL DEFAULT '1',
  `group_description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activity_master`
--

DROP TABLE IF EXISTS `activity_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(255) NOT NULL,
  `activity_url` varchar(255) NOT NULL,
  `activity_type` varchar(255) NOT NULL,
  `activity_creation_date` date DEFAULT NULL,
  `activity_updation_date` date DEFAULT NULL,
  `activity_created_by` varchar(45) DEFAULT NULL,
  `activity_updated_by` varchar(45) DEFAULT NULL,
  `activity_status` int(2) NOT NULL DEFAULT '1',
  `activity_description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `activity_name_UNIQUE` (`activity_name`),
  UNIQUE KEY `activity_url_UNIQUE` (`activity_url`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_servers_details`
--

DROP TABLE IF EXISTS `anv_servers_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_servers_details` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `short_name` varchar(200) DEFAULT NULL,
  `ip_address` varchar(200) DEFAULT NULL,
  `port_number` varchar(200) DEFAULT NULL,
  `schema_name` varchar(200) DEFAULT NULL,
  `user_name` varchar(200) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anvizent_country_formats`
--

DROP TABLE IF EXISTS `anvizent_country_formats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anvizent_country_formats` (
  `id` int(11) NOT NULL DEFAULT '0',
  `region_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `region_code` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `pattern` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `decimal_seperator` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `thousand_seperator` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anvizent_currency_list`
--

DROP TABLE IF EXISTS `anvizent_currency_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anvizent_currency_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `currency_code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `currency_name` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `currency_code_UNIQUE` (`currency_code`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anvizent_internalization`
--

DROP TABLE IF EXISTS `anvizent_internalization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anvizent_internalization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locale_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `locale_code` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `datecreated` datetime DEFAULT NULL,
  `usercreated` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `dateupdated` datetime DEFAULT NULL,
  `userupdated` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=288 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anvizent_partner`
--

DROP TABLE IF EXISTS `anvizent_partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anvizent_partner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `partnername` varchar(255) NOT NULL,
  `partnerdescription` text,
  `partneraddress` varchar(500) DEFAULT NULL,
  `contactpersonemail` varchar(255) DEFAULT NULL,
  `contactpersonname` varchar(500) DEFAULT NULL,
  `datecreated` date DEFAULT NULL,
  `partnerstatus` varchar(100) DEFAULT NULL,
  `partnertype` varchar(100) DEFAULT NULL,
  `activationdate` date DEFAULT NULL,
  `lastmodified` date DEFAULT NULL,
  `partnerlogo` blob,
  `created_by` varchar(45) DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `partnername_UNIQUE` (`partnername`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anvizent_properties`
--

DROP TABLE IF EXISTS `anvizent_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anvizent_properties` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `prop_key` varchar(200) NOT NULL,
  `prop_value` mediumtext NOT NULL,
  `prop_context` varchar(200) NOT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `cloudclientname` varchar(500) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `prop_key` (`prop_key`,`cloudclientid`),
  KEY `fk_client_properties_table_idx` (`cloudclientid`),
  CONSTRAINT `fk_client_properties_table` FOREIGN KEY (`cloudclientid`) REFERENCES `cloud_client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anvizent_timezones`
--

DROP TABLE IF EXISTS `anvizent_timezones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anvizent_timezones` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `zone_offset` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone_name_display` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `zone_name_UNIQUE` (`zone_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1050 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anvizentdb_script`
--

DROP TABLE IF EXISTS `anvizentdb_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anvizentdb_script` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_created` date DEFAULT NULL,
  `anviz_appdbscript` mediumtext COLLATE utf8_unicode_ci,
  `minidw_appdbscript` mediumtext COLLATE utf8_unicode_ci,
  `etl_appdbscript` mediumtext COLLATE utf8_unicode_ci,
  `date_updated` date DEFAULT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `default_db` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `version_UNIQUE` (`version`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_license`
--

DROP TABLE IF EXISTS `app_license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_license` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` int(12) DEFAULT NULL,
  `license_details` mediumtext COLLATE utf8_unicode_ci,
  `client_region` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `license_mode` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientId_UNIQUE` (`clientId`)
) ENGINE=InnoDB AUTO_INCREMENT=266 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_user_proxy`
--

DROP TABLE IF EXISTS `app_user_proxy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_user_proxy` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `proxy_user_name` varchar(45) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  `clientId` int(12) DEFAULT NULL,
  `active_status` varchar(12) DEFAULT NULL,
  `sso_enabled` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `USER_CLIENT_COMPOSITE` (`proxy_user_name`,`clientId`)
) ENGINE=InnoDB AUTO_INCREMENT=1640 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_version_master`
--

DROP TABLE IF EXISTS `app_version_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_version_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anvizent_version` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `minidw_version` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `application_cluster`
--

DROP TABLE IF EXISTS `application_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_cluster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cluster_schema` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'http',
  `cluster_hostname` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cluster_ipaddress` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cluster_username` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cluster_password` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `app_context` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'anvizent',
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `application_user_roles`
--

DROP TABLE IF EXISTS `application_user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_user_roles` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `role` varchar(45) CHARACTER SET utf8 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `datecreated` date DEFAULT NULL,
  `password_expiry_days` int(5) DEFAULT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `cloudclientname` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `clientstatus` varchar(100) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `roleid` bigint(20) DEFAULT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=321 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_druid_details`
--

DROP TABLE IF EXISTS `client_druid_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_druid_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `druid_cluster_name` varchar(100) DEFAULT NULL,
  `zookeeper_host` varchar(255) DEFAULT NULL,
  `zookeeper_portnumber` int(11) DEFAULT '2181',
  `coordinator_host` varchar(255) NOT NULL,
  `coordinator_portnumber` int(11) DEFAULT '8081',
  `broker_host` varchar(255) DEFAULT NULL,
  `broker_portnumber` int(11) DEFAULT '8082',
  `historical_host` varchar(255) NOT NULL,
  `historical_portnumber` int(11) DEFAULT '8083',
  `realtime_host` varchar(255) DEFAULT NULL,
  `realtime_portnumber` int(11) DEFAULT '8084',
  `router_host` varchar(255) DEFAULT NULL,
  `router_portnumber` int(11) DEFAULT '8088',
  `overlord_host` varchar(255) NOT NULL,
  `overlord_portnumber` int(11) DEFAULT '8090',
  `middle_manager_host` varchar(255) NOT NULL,
  `middle_manager_portnumber` int(11) DEFAULT '8091',
  `tranquility_host` varchar(255) DEFAULT NULL,
  `tranquility_portnumber` int(11) DEFAULT '8200',
  `druid_user_name` varchar(255) NOT NULL,
  `druid_ssh_port_number` int(11) DEFAULT '22',
  `is_ppk_file` tinyint(1) DEFAULT '0',
  `ppk_file_path_or_password` varchar(255) NOT NULL,
  `sql_to_json_jar_path` varchar(255) NOT NULL,
  `druid_data_directory` varchar(255) NOT NULL,
  `spark_installation_dir` varchar(255) DEFAULT NULL,
  `hadoop_config_dir` varchar(255) DEFAULT NULL,
  `partition_threshold` int(11) NOT NULL,
  `date_format` varchar(255) NOT NULL,
  `protocol` varchar(45) NOT NULL DEFAULT 'http',
  `status` varchar(45) NOT NULL DEFAULT 'OK',
  `is_default` bit(1) DEFAULT b'0',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_features`
--

DROP TABLE IF EXISTS `client_features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_features` (
  `clientid` bigint(20) NOT NULL,
  `framework` varchar(255) DEFAULT NULL,
  `primaryapi` varchar(255) DEFAULT NULL,
  `internet_enabled` bit(1) DEFAULT b'1',
  `clientcdn_enabled` bit(1) DEFAULT NULL,
  `cdnPath` varchar(255) DEFAULT NULL,
  `enable_org_filter` bit(1) DEFAULT b'0',
  `enable_email_scheduler` bit(1) DEFAULT b'0',
  PRIMARY KEY (`clientid`),
  UNIQUE KEY `clientid_UNIQUE` (`clientid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_launching_details`
--

DROP TABLE IF EXISTS `client_launching_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_launching_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `cloudclientid` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_deatils` mediumtext COLLATE utf8_unicode_ci,
  `clientdetails_status` mediumtext COLLATE utf8_unicode_ci,
  `overall_status` bit(1) NOT NULL DEFAULT b'0',
  `launch_status` bit(1) NOT NULL DEFAULT b'0',
  `client_logo` blob,
  `date_launched` datetime DEFAULT NULL,
  `launched_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `processed_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_processed` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_name_UNIQUE` (`client_name`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_launching_details_old`
--

DROP TABLE IF EXISTS `client_launching_details_old`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_launching_details_old` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clientcode` varchar(50) NOT NULL,
  `clientname` varchar(255) NOT NULL,
  `emaildomainname` varchar(255) DEFAULT NULL,
  `clientdescription` text,
  `address` varchar(500) DEFAULT NULL,
  `contactpersonemergencynumber` varchar(45) DEFAULT NULL,
  `contactpersonemail` varchar(255) DEFAULT NULL,
  `contactpersonname` varchar(500) DEFAULT NULL,
  `datecreated` date DEFAULT NULL,
  `activationdate` date DEFAULT NULL,
  `licenseid` bigint(20) DEFAULT NULL,
  `deployment_type` varchar(50) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  `lastmodified` date DEFAULT NULL,
  `clientlogo` blob,
  `clientdb_schema` varchar(100) DEFAULT NULL,
  `clientdb_username` varchar(100) DEFAULT NULL,
  `clientdb_password` varchar(45) DEFAULT NULL,
  `isdruid_enabled` bit(1) DEFAULT b'0',
  `region_id` bigint(20) DEFAULT NULL,
  `cluster_id` bigint(20) DEFAULT NULL,
  `minidwcluster_id` bigint(20) DEFAULT NULL,
  `partnerid` bigint(20) DEFAULT '100',
  `druid_cluster_id` bigint(20) DEFAULT NULL,
  `sso_enabled` bit(1) DEFAULT b'0',
  `created_by` varchar(45) DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `CLIENT_COMPOSITE` (`clientcode`,`clientname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_launching_templates`
--

DROP TABLE IF EXISTS `client_launching_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_launching_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `template_details` mediumtext COLLATE utf8_unicode_ci,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updayed_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `template_name_UNIQUE` (`template_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clientdbdetails`
--

DROP TABLE IF EXISTS `clientdbdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clientdbdetails` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clientname` varchar(255) NOT NULL,
  `hostname` varchar(255) NOT NULL,
  `portnumber` varchar(8) DEFAULT NULL,
  `database1` varchar(255) NOT NULL,
  `databasevendor` varchar(255) DEFAULT NULL,
  `databaseversion` varchar(255) DEFAULT NULL,
  `user` varchar(255) NOT NULL,
  `password1` varchar(255) NOT NULL,
  `databaseurl` varchar(1000) DEFAULT NULL,
  `readonly_hostname` varchar(255) DEFAULT NULL,
  `clientappdb_name` varchar(100) DEFAULT NULL,
  `clientappdb_username` varchar(45) DEFAULT NULL,
  `clientappdb_password` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `cloudclientname` varchar(500) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientname_database1_hostname_UNIQUE` (`clientname`,`database1`,`hostname`,`cloudclientid`)
) ENGINE=InnoDB AUTO_INCREMENT=444 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clients_contains_tasks_to_kill`
--

DROP TABLE IF EXISTS `clients_contains_tasks_to_kill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients_contains_tasks_to_kill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(20) NOT NULL,
  `contains_tasks_to_kill` bit(1) DEFAULT b'0',
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_clients_contains_tasks_to_kill_created_by_user_id` (`created_by`),
  KEY `fk_clients_contains_tasks_to_kill_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_clients_contains_tasks_to_kill_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_clients_contains_tasks_to_kill_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cloud_client`
--

DROP TABLE IF EXISTS `cloud_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloud_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clientname` varchar(255) NOT NULL,
  `emaildomainname` varchar(255) DEFAULT NULL,
  `clientdescription` text,
  `address` varchar(500) DEFAULT NULL,
  `contactpersonemergencynumber` varchar(45) DEFAULT NULL,
  `contactpersonemail` varchar(255) DEFAULT NULL,
  `contactpersonname` varchar(500) DEFAULT NULL,
  `datecreated` date DEFAULT NULL,
  `activationdate` date DEFAULT NULL,
  `licenseid` bigint(20) DEFAULT NULL,
  `deployment_type` varchar(50) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  `lastmodified` date DEFAULT NULL,
  `clientlogo` blob,
  `clientdb_schema` varchar(100) DEFAULT NULL,
  `clientdb_username` varchar(100) DEFAULT NULL,
  `clientdb_password` varchar(45) DEFAULT NULL,
  `isdruid_enabled` bit(1) DEFAULT b'0',
  `region_id` bigint(20) DEFAULT NULL,
  `cluster_id` bigint(20) DEFAULT NULL,
  `minidwcluster_id` bigint(20) DEFAULT NULL,
  `minidw_service_cluster_id` bigint(20) DEFAULT NULL,
  `partnerid` bigint(20) DEFAULT '100',
  `druid_cluster_id` bigint(20) DEFAULT NULL,
  `etl_template_id` bigint(10) DEFAULT NULL,
  `sso_enabled` bit(1) DEFAULT b'0',
  `default_time_zone` varchar(100) DEFAULT NULL,
  `default_currency` varchar(100) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT '0',
  `enterprise_name` varchar(100) DEFAULT NULL,
  `anvizent_version` varchar(45) DEFAULT NULL,
  `minidw_version` varchar(45) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `isScript_TemplateExecuted` varchar(10) DEFAULT NULL,
  `isLiveClient` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientname_UNIQUE` (`clientname`)
) ENGINE=InnoDB AUTO_INCREMENT=1010441 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `component_parameter_category`
--

DROP TABLE IF EXISTS `component_parameter_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component_parameter_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `display_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `sequence` int(11) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_component_parameter_category_name_sequence` (`name`,`sequence`),
  KEY `fk_component_parameter_category_created_by_user_id` (`created_by`),
  KEY `fk_component_parameter_category_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_component_parameter_category_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_component_parameter_category_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `component_parameter_group`
--

DROP TABLE IF EXISTS `component_parameter_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component_parameter_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `display_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `sequence` int(11) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_component_parameter_group_name_sequence` (`name`,`sequence`),
  KEY `fk_component_parameter_group_created_by_user_id` (`created_by`),
  KEY `fk_component_parameter_group_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_component_parameter_group_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_component_parameter_group_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ddl_script`
--

DROP TABLE IF EXISTS `ddl_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ddl_script` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ddl_name` varchar(145) NOT NULL,
  `ddl_script` mediumtext NOT NULL,
  `created_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ddl_name_constraint` (`ddl_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `druid_data_source_info`
--

DROP TABLE IF EXISTS `druid_data_source_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `druid_data_source_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `druid_data_source_master_id` bigint(20) NOT NULL,
  `table_name` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `current_version_number` bigint(20) DEFAULT '0',
  `time_fields_granularity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `priority` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'IDLE',
  `last_error` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `retry_count` int(5) NOT NULL DEFAULT '0',
  `max_retry_count` int(5) NOT NULL DEFAULT '0',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_druid_data_source_info_druid_data_source_master_id_idx` (`druid_data_source_master_id`),
  CONSTRAINT `fk_druid_data_source_info_druid_data_source_master_id` FOREIGN KEY (`druid_data_source_master_id`) REFERENCES `druid_data_source_master` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `druid_data_source_master`
--

DROP TABLE IF EXISTS `druid_data_source_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `druid_data_source_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `current_version_number` int(5) NOT NULL DEFAULT '0',
  `date_fields_granularity` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'HOUR',
  `time_fields_granularity` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'HOUR',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_druid_data_source_master_table_name` (`table_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `druid_kafka_test_incr_emulator`
--

DROP TABLE IF EXISTS `druid_kafka_test_incr_emulator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `druid_kafka_test_incr_emulator` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `_anviz_copy_of` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `_anviz_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `_anviz_count` int(11) DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elt_il_configuration`
--

DROP TABLE IF EXISTS `elt_il_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elt_il_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `il_id` bigint(20) DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elt_main_key_config`
--

DROP TABLE IF EXISTS `elt_main_key_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elt_main_key_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seq_id` bigint(20) DEFAULT NULL,
  `stg_key` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `stg_value` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elt_spark_submit_details`
--

DROP TABLE IF EXISTS `elt_spark_submit_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elt_spark_submit_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `driver_id` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `worker_id` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `application_id` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `application_start_time` datetime DEFAULT NULL,
  `application_end_time` datetime DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_elt_spark_submit_created_by_user_id_idx` (`created_by`),
  KEY `fk_elt_spark_submit_updated_by_user_id_idx` (`updated_by`),
  CONSTRAINT `fk_elt_sprk_submit_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_elt_sprk_submit_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elt_spark_submit_error_handlers`
--

DROP TABLE IF EXISTS `elt_spark_submit_error_handlers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elt_spark_submit_error_handlers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_details_id` bigint(20) NOT NULL,
  `config_key` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `config_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_elt_spark_submit_error_handlers_job_details_id_id_idx` (`job_details_id`),
  CONSTRAINT `fk_elt_spark_submit_error_handlers_job_details_id_id` FOREIGN KEY (`job_details_id`) REFERENCES `elt_spark_submit_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1351 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elt_spark_submit_executor_details`
--

DROP TABLE IF EXISTS `elt_spark_submit_executor_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elt_spark_submit_executor_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_details_id` bigint(20) NOT NULL,
  `executor_id` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `host` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `cores` int(11) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_elt_spark_submit_executor_details_job_details_id_id_idx` (`job_details_id`),
  KEY `fk_elt_spark_submit_executor_details_created_by_id_idx` (`created_by`),
  KEY `fk_elt_spark_submit_executor_details_updated_by_id_idx` (`updated_by`),
  CONSTRAINT `fk_elt_spark_submit_executor_details_created_by_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_elt_spark_submit_executor_details_job_details_id_id` FOREIGN KEY (`job_details_id`) REFERENCES `elt_spark_submit_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_elt_spark_submit_executor_details_updated_by_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elt_spark_submit_job_settings`
--

DROP TABLE IF EXISTS `elt_spark_submit_job_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elt_spark_submit_job_settings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_details_id` bigint(20) NOT NULL,
  `config_key` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `config_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_elt_spark_submit_job_settings_job_details_id_id_idx` (`job_details_id`),
  CONSTRAINT `fk_elt_spark_submit_job_settings_job_details_id_id` FOREIGN KEY (`job_details_id`) REFERENCES `elt_spark_submit_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1126 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elt_spark_submit_stats_settings`
--

DROP TABLE IF EXISTS `elt_spark_submit_stats_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elt_spark_submit_stats_settings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_details_id` bigint(20) NOT NULL,
  `config_key` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `config_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_elt_spark_submit_stats_settings_job_details_id_id_idx` (`job_details_id`),
  CONSTRAINT `fk_elt_spark_submit_stats_settings_job_details_id_id` FOREIGN KEY (`job_details_id`) REFERENCES `elt_spark_submit_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1201 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_client_migration_mapping`
--

DROP TABLE IF EXISTS `erp_client_migration_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_client_migration_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `erpId` bigint(20) DEFAULT NULL,
  `clientId` bigint(20) DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_date` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_master`
--

DROP TABLE IF EXISTS `erp_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `erp_name` varchar(145) NOT NULL,
  `erp_version` varchar(10) DEFAULT NULL,
  `isactive` bit(1) DEFAULT b'1',
  `description` mediumtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `erp_name_constraint` (`erp_name`),
  UNIQUE KEY `erpname_version` (`erp_name`,`erp_version`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_master_partner_mapping`
--

DROP TABLE IF EXISTS `erp_master_partner_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_master_partner_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `erp_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `partner_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_currency_list`
--

DROP TABLE IF EXISTS `erp_minidwcm_currency_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_currency_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `currency_code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `currency_name` text COLLATE utf8_unicode_ci NOT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `CURRENCY_CODE_UNQ` (`currency_code`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_database_connectors`
--

DROP TABLE IF EXISTS `erp_minidwcm_database_connectors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_database_connectors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `connector_id` int(11) DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `connector_fk_idx` (`connector_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_database_types`
--

DROP TABLE IF EXISTS `erp_minidwcm_database_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_database_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `driver_name` text COLLATE utf8_unicode_ci,
  `protocal` text COLLATE utf8_unicode_ci,
  `connection_string_params` text COLLATE utf8_unicode_ci,
  `url_format` text COLLATE utf8_unicode_ci,
  `connector_jars` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_dl_il_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcm_dl_il_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_dl_il_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dl_id` bigint(20) DEFAULT NULL,
  `il_id` bigint(20) DEFAULT NULL,
  `il_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `IsActive` bit(1) DEFAULT b'1',
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dl_il_UNIQUE` (`dl_id`,`il_id`),
  KEY `fk_il_id_idx` (`il_id`),
  KEY `dl_id_idx` (`dl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4612 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_dl_info`
--

DROP TABLE IF EXISTS `erp_minidwcm_dl_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_dl_info` (
  `DL_id` bigint(45) NOT NULL AUTO_INCREMENT,
  `DL_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dl_table_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `DL_schema` mediumtext COLLATE utf8_unicode_ci,
  `isStandard` bit(1) DEFAULT b'1',
  `industry_id` bigint(45) DEFAULT NULL,
  `DL_tgt_key` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `Src_IL_id` bigint(10) DEFAULT NULL,
  `checkedDl` bit(1) DEFAULT b'0',
  `isActive` bit(1) DEFAULT b'1',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_execution_type` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `elt_job_tag` bigint(20) DEFAULT NULL,
  `elt_load_parameter` bigint(20) DEFAULT NULL,
  `elt_master_id` bigint(20) DEFAULT NULL,
  `dl_info_by_version` text COLLATE utf8_unicode_ci,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`DL_id`),
  KEY `id_idx` (`industry_id`),
  KEY `il_id_idx` (`Src_IL_id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_il_info`
--

DROP TABLE IF EXISTS `erp_minidwcm_il_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_il_info` (
  `IL_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `IL_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `il_type` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `il_table_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `xref_il_table_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `IL_schema` mediumtext COLLATE utf8_unicode_ci,
  `IL_schema_Stg` mediumtext COLLATE utf8_unicode_ci,
  `isStandard` bit(1) DEFAULT b'1',
  `DL_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_execution_type` char(1) COLLATE utf8_unicode_ci DEFAULT 'T',
  `elt_job_tag` bigint(20) DEFAULT NULL,
  `elt_load_parameter` bigint(20) DEFAULT NULL,
  `elt_master_id` bigint(20) DEFAULT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `purge_script` text COLLATE utf8_unicode_ci,
  `src_file_context_param_key` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `target_table_context_param_key` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `il_info_by_version` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`IL_id`),
  UNIQUE KEY `IL_name_UNIQUE` (`IL_name`)
) ENGINE=InnoDB AUTO_INCREMENT=326 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_il_prebuild_queries_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcm_il_prebuild_queries_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_il_prebuild_queries_mapping` (
  `mapping_id` int(11) NOT NULL AUTO_INCREMENT,
  `il_id` bigint(10) DEFAULT NULL,
  `database_type_id` int(11) DEFAULT NULL,
  `il_query` mediumtext COLLATE utf8_unicode_ci,
  `il_incremental_update_query` mediumtext COLLATE utf8_unicode_ci,
  `historical_load` mediumtext COLLATE utf8_unicode_ci,
  `max_date_query` mediumtext COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`mapping_id`),
  UNIQUE KEY `il_id_2` (`il_id`,`database_type_id`),
  KEY `il_id` (`il_id`),
  KEY `database_type_id` (`database_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_kpis`
--

DROP TABLE IF EXISTS `erp_minidwcm_kpis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_kpis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kpi_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `kpi_description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kpi_name_UNIQUE` (`kpi_name`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_kpis_dl_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcm_kpis_dl_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_kpis_dl_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dl_id` bigint(10) NOT NULL,
  `kpi_id` bigint(10) NOT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dl_id_fk_kpi_idx` (`dl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=711 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_ws_api_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcm_ws_api_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_ws_api_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `ws_template_id` bigint(10) NOT NULL,
  `il_id` bigint(10) NOT NULL,
  `api_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `api_url` text COLLATE utf8_unicode_ci NOT NULL,
  `base_url_required` bit(1) DEFAULT b'0',
  `api_method_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `api_path_params` text COLLATE utf8_unicode_ci,
  `api_request_params` text COLLATE utf8_unicode_ci,
  `api_body_params` text COLLATE utf8_unicode_ci,
  `pagination_required` bit(1) DEFAULT b'0',
  `pagination_type` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pagination_request_params` text COLLATE utf8_unicode_ci,
  `incremental_update` bit(1) DEFAULT b'0',
  `incremental_update_params` text COLLATE utf8_unicode_ci,
  `response_column_object_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `response_object_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `soap_body_element` text COLLATE utf8_unicode_ci,
  `webservice_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ssl_disable` bit(1) DEFAULT NULL,
  `default_mapping` text COLLATE utf8_unicode_ci,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ws_template_id` (`ws_template_id`,`api_name`),
  KEY `ws_new_il_id_fk_indx` (`il_id`),
  KEY `ws_new_id_fk` (`ws_template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_ws_authentication_types`
--

DROP TABLE IF EXISTS `erp_minidwcm_ws_authentication_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_ws_authentication_types` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `authentication_type` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `authentication_token` text COLLATE utf8_unicode_ci,
  `authentication_refresh_token` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `authentication_type` (`authentication_type`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_ws_template_auth_request_params`
--

DROP TABLE IF EXISTS `erp_minidwcm_ws_template_auth_request_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_ws_template_auth_request_params` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `ws_template_id` bigint(10) NOT NULL,
  `param_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `is_mandatory` bit(1) DEFAULT b'1',
  `is_passwordtype` bit(1) DEFAULT b'0',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ws_template_id` (`ws_template_id`,`param_name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcm_ws_template_mst`
--

DROP TABLE IF EXISTS `erp_minidwcm_ws_template_mst`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcm_ws_template_mst` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `web_service_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `authentication_type` bigint(10) NOT NULL,
  `date_format` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time_zone` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Asia/Kolkata',
  `base_url` text COLLATE utf8_unicode_ci,
  `authentication_url` text COLLATE utf8_unicode_ci,
  `base_url_required` bit(1) DEFAULT b'0',
  `authentication_method_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `api_auth_request_params` text COLLATE utf8_unicode_ci,
  `api_auth_body_params` text COLLATE utf8_unicode_ci,
  `api_auth_request_body_params` text COLLATE utf8_unicode_ci,
  `api_auth_request_headers` text COLLATE utf8_unicode_ci,
  `callback_url` text COLLATE utf8_unicode_ci,
  `access_token_url` text COLLATE utf8_unicode_ci,
  `response_type` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `grant_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `webservice_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `soap_body_element` text COLLATE utf8_unicode_ci,
  `clientid` text COLLATE utf8_unicode_ci,
  `client_secret` text COLLATE utf8_unicode_ci,
  `ssl_disable` bit(1) DEFAULT NULL,
  `scope` text COLLATE utf8_unicode_ci,
  `state` text COLLATE utf8_unicode_ci,
  `session_auth_urls` text COLLATE utf8_unicode_ci,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `web_service_name` (`web_service_name`),
  KEY `ws_auth_type_fk` (`authentication_type`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_data_validation_script`
--

DROP TABLE IF EXISTS `erp_minidwcs_data_validation_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_data_validation_script` (
  `script_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `validation_id` bigint(20) NOT NULL,
  `database_connector_id` int(11) DEFAULT '0',
  `script_name` varchar(200) CHARACTER SET utf8 NOT NULL,
  `validation_script` mediumtext COLLATE utf8_unicode_ci,
  `prepare_stmt` bit(1) DEFAULT NULL,
  `validation_type_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_name` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `active` bit(1) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`script_id`),
  UNIQUE KEY `script_name_UNIQUE` (`script_name`,`validation_id`),
  KEY `validation_id_idx` (`validation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_data_validation_script_dl_il_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcs_data_validation_script_dl_il_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_data_validation_script_dl_il_mapping` (
  `validation_mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `script_id` int(11) NOT NULL,
  `dl_id` int(11) NOT NULL DEFAULT '0',
  `il_id` int(11) NOT NULL DEFAULT '0',
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`validation_mapping_id`),
  UNIQUE KEY `script_id_UNIQUE` (`script_id`,`dl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_database_connectors_client_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcs_database_connectors_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_database_connectors_client_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(10) NOT NULL,
  `connector_id` int(11) NOT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id_connector_UNIQUE` (`client_id`,`connector_id`),
  KEY `fk_connector_id_idx` (`connector_id`)
) ENGINE=InnoDB AUTO_INCREMENT=747 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_ddls`
--

DROP TABLE IF EXISTS `erp_minidwcs_ddls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_ddls` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userid` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `table_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `table_desc` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `table_structure` text COLLATE utf8_unicode_ci,
  `select_query` text COLLATE utf8_unicode_ci,
  `ddl_tables` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_dl_client_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcs_dl_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_dl_client_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(10) DEFAULT NULL,
  `dl_id` bigint(45) DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `IsActive` bit(1) DEFAULT b'1',
  `isLocked` bit(1) DEFAULT b'0',
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dl_id_idx` (`dl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4239 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_dl_client_specific_jobs`
--

DROP TABLE IF EXISTS `erp_minidwcs_dl_client_specific_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_dl_client_specific_jobs` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(10) NOT NULL,
  `dl_id` bigint(45) NOT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0',
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dlid_clientid_unique` (`client_id`,`dl_id`),
  KEY `fk_dl_id_idx` (`dl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_dl_trailingmonths_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcs_dl_trailingmonths_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_dl_trailingmonths_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dl_id` bigint(20) DEFAULT NULL,
  `trailing_months` bigint(20) DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dl_unique` (`dl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_file_settings`
--

DROP TABLE IF EXISTS `erp_minidwcs_file_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_file_settings` (
  `id` bigint(20) NOT NULL,
  `max_file_size_in_mb` bigint(20) DEFAULT NULL,
  `trail_user_max_file_size_in_mb` bigint(10) DEFAULT '1',
  `multipart_file_enabled` bit(1) DEFAULT b'0',
  `no_of_records_per_file` bigint(40) DEFAULT NULL,
  `file_encryption` bit(1) DEFAULT b'0',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `IsActive` bit(1) DEFAULT b'1',
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_il_client_specific_jobs`
--

DROP TABLE IF EXISTS `erp_minidwcs_il_client_specific_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_il_client_specific_jobs` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(10) NOT NULL,
  `il_id` bigint(10) NOT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0',
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_il_id_idx` (`il_id`),
  KEY `ilid_clientid_unique` (`client_id`,`il_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_package_schedule`
--

DROP TABLE IF EXISTS `erp_minidwcs_package_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_package_schedule` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `package_ID` bigint(10) NOT NULL,
  `dl_id` int(11) DEFAULT '0',
  `schedule_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `schedule_start_date` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `schedule_start_time` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time_zone` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recurrence_pattern` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `minutes_to_run` text COLLATE utf8_unicode_ci,
  `days_to_run` longtext COLLATE utf8_unicode_ci COMMENT 'Recurrence type: Daily\ncoloumns : days_to_run',
  `weeks_to_run` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Recurrence type: weekly\ncoloumns : days_to_run, weeks_to_run',
  `day_of_month` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Recurrence type: monthly\ncoloumns : days_to_run, weeks_to_run',
  `months_to_run` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `day_of_year` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `month_of_year` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `years_to_run` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_no_end_date` bit(1) DEFAULT NULL,
  `end_date` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cron_expression` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `no_of_max_occurences` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `priority` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `ipAddress` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` text CHARACTER SET utf8,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text CHARACTER SET utf8,
  `modified_time` datetime DEFAULT NULL,
  `type_of_hours` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hours_to_run` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `package_dl_unique` (`package_ID`,`dl_id`),
  KEY `pack_id_fk_idx` (`package_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1218 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_package_source_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcs_package_source_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_package_source_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `userid` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Package_id` bigint(10) DEFAULT NULL,
  `DL_id` bigint(10) DEFAULT NULL,
  `IL_id` bigint(10) DEFAULT NULL,
  `il_source_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isIncrementalUpdate` bit(1) DEFAULT b'0',
  `isHavingParentTable` bit(1) DEFAULT NULL,
  `parent_table_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isFlatFile` bit(1) DEFAULT NULL,
  `isDLMapped` bit(1) DEFAULT b'0',
  `target_table_id` bigint(10) DEFAULT NULL,
  `isMapped` bit(1) DEFAULT NULL,
  `fileType` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `source_file_info_id` bigint(20) DEFAULT NULL,
  `storage_type` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT 's3',
  `s3_bucket_id` bigint(20) NOT NULL DEFAULT '0',
  `is_multipart_enabled` bit(1) NOT NULL DEFAULT b'0',
  `filePath` text CHARACTER SET utf8,
  `delimeter` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_row_has_coloumn_names` bit(1) DEFAULT NULL,
  `connection_id` bigint(10) DEFAULT NULL,
  `type_of_command` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Database_Name` text CHARACTER SET utf8,
  `IL_query` text CHARACTER SET utf8,
  `max_date_query` mediumtext COLLATE utf8_unicode_ci,
  `procedure_parameters` text CHARACTER SET utf8,
  `isWebService` bit(1) DEFAULT b'0',
  `webservice_Id` bigint(10) DEFAULT NULL,
  `webservice_mapping_headers` text COLLATE utf8_unicode_ci,
  `is_join_web_service` bit(1) DEFAULT b'0',
  `web_service_join_urls` text COLLATE utf8_unicode_ci,
  `Database_id` bigint(10) DEFAULT NULL,
  `iL_job_status_for_run_now` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_historical_load` bit(1) DEFAULT b'0',
  `historical_from_date` datetime DEFAULT NULL,
  `historical_to_date` datetime DEFAULT NULL,
  `historical_load_interval` bigint(45) DEFAULT NULL,
  `historical_last_updated_time` datetime DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `ipAddress` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` text CHARACTER SET utf8,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text CHARACTER SET utf8,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `connection_id_fk_idx` (`connection_id`),
  KEY `il_id_fk_idx` (`IL_id`),
  KEY `dl_id_fk_idx` (`DL_id`),
  KEY `package_id_fk_idx` (`Package_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3388 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_package_web_service_source_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcs_package_web_service_source_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_package_web_service_source_mapping` (
  `id` bigint(45) NOT NULL AUTO_INCREMENT,
  `fileId` bigint(10) DEFAULT NULL,
  `packageId` bigint(10) DEFAULT NULL,
  `userid` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `il_id` bigint(45) DEFAULT NULL,
  `il_connection_mapping_id` bigint(45) DEFAULT NULL,
  `web_service_url` text COLLATE utf8_unicode_ci,
  `web_service_method_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `web_service_api_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `response_column_object_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `web_service_response_object_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `api_path_params` text COLLATE utf8_unicode_ci,
  `api_request_params` text COLLATE utf8_unicode_ci,
  `api_body_params` text COLLATE utf8_unicode_ci,
  `pagination_required` bit(1) DEFAULT b'0',
  `pagination_type` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pagination_request_params` text COLLATE utf8_unicode_ci,
  `retry_pagination` text COLLATE utf8_unicode_ci,
  `incremental_update` bit(1) DEFAULT b'0',
  `incremental_update_params` text COLLATE utf8_unicode_ci,
  `temp_table_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `temp_table_structure` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `base_url_required` bit(1) DEFAULT b'0',
  `soap_body_element` text COLLATE utf8_unicode_ci,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwcs_ws_client_mapping`
--

DROP TABLE IF EXISTS `erp_minidwcs_ws_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwcs_ws_client_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(10) NOT NULL,
  `ws_template_id` bigint(10) NOT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id` (`client_id`,`ws_template_id`),
  KEY `ws_template_id_client_mapping_fk` (`ws_template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwm_client_currency_mapping`
--

DROP TABLE IF EXISTS `erp_minidwm_client_currency_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwm_client_currency_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `currency_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dashboard_currency` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `accountCurrencyCode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `otherCurrencyCode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `erp_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientId_unique` (`clientId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwm_s3_client_mapping`
--

DROP TABLE IF EXISTS `erp_minidwm_s3_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwm_s3_client_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(10) NOT NULL,
  `s3_bucket_info_id` bigint(45) NOT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `erp_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id_UNIQUE` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_minidwm_scheduler_master_client_mapping`
--

DROP TABLE IF EXISTS `erp_minidwm_scheduler_master_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_minidwm_scheduler_master_client_mapping` (
  `master_id` bigint(20) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `erp_id` bigint(20) NOT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `erp_wishlist`
--

DROP TABLE IF EXISTS `erp_wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `erp_wishlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(145) NOT NULL,
  `datesavedon` varchar(45) DEFAULT NULL,
  `datasourcedev` varchar(145) DEFAULT NULL,
  `devtable` varchar(145) DEFAULT NULL,
  `clientdbdetailsid` bigint(20) DEFAULT NULL,
  `datasourceprod` varchar(145) DEFAULT NULL,
  `externalsourcewithinternalquery` varchar(45) DEFAULT NULL,
  `prodtable` varchar(145) DEFAULT NULL,
  `userid` bigint(20) DEFAULT NULL,
  `category` varchar(145) DEFAULT NULL,
  `description` text,
  `filename` varchar(500) DEFAULT NULL,
  `defaultdata` mediumtext,
  `dashboard_script` mediumtext,
  `htmldashboardfile` mediumtext,
  `thejsfile` mediumtext,
  `isinproduction` varchar(20) DEFAULT NULL,
  `dashboard_version` varchar(100) DEFAULT NULL,
  `isapproved` varchar(20) DEFAULT NULL,
  `approvedbyuserid` bigint(20) DEFAULT NULL,
  `afterwhereclause` text,
  `groupby` text,
  `limitwhere` varchar(255) DEFAULT NULL,
  `thumbnail` longblob,
  `dashbordlogo` blob,
  `status` varchar(45) DEFAULT 'ACTIVE',
  `status_last_modified` date DEFAULT NULL,
  `action_userid` bigint(20) DEFAULT NULL,
  `action_userip` varchar(30) DEFAULT NULL,
  `grouptypeid` bigint(20) DEFAULT NULL,
  `datasource_type` varchar(100) DEFAULT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `cloudclientname` varchar(500) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  `wishlisttype` varchar(45) DEFAULT 'dashboard',
  `erp_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid_name_constraint` (`name`,`userid`),
  KEY `fk_wishlist_userid_idx` (`userid`),
  CONSTRAINT `fk_wishlist_userid` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groups_master`
--

DROP TABLE IF EXISTS `groups_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL,
  `group_creation_date` date DEFAULT NULL,
  `group_updation_date` date DEFAULT NULL,
  `group_description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_name_UNIQUE` (`group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `license_master`
--

DROP TABLE IF EXISTS `license_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `license_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `license_name` varchar(100) NOT NULL,
  `license_type` varchar(50) NOT NULL,
  `license_mode` varchar(50) NOT NULL,
  `license_validate_expiry` int(1) DEFAULT '0',
  `license_validate_active_users` int(1) DEFAULT '0',
  `license_expiry_date` date DEFAULT NULL,
  `license_active_users` int(10) DEFAULT '0',
  `license_expiry_days` varchar(45) DEFAULT NULL,
  `license_expiry_notification_enable` int(2) DEFAULT '0',
  `license_expiry_notification_days` int(2) DEFAULT NULL,
  `license_creation_date` date DEFAULT NULL,
  `license_updation_date` date DEFAULT NULL,
  `license_created_by` varchar(45) DEFAULT NULL,
  `license_updated_by` varchar(45) DEFAULT NULL,
  `license_status` int(2) DEFAULT '1',
  `license_description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_name_UNIQUE` (`license_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logger_details`
--

DROP TABLE IF EXISTS `logger_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logger_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `application_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_id` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `error_code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `severity` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `error_message` longtext COLLATE utf8_unicode_ci,
  `write_drive` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `write_format` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `notify_error_flag` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidw_cluster`
--

DROP TABLE IF EXISTS `minidw_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidw_cluster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cluster_schema` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'http',
  `cluster_hostname` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `app_context` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'minidw',
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidw_erp_tables`
--

DROP TABLE IF EXISTS `minidw_erp_tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidw_erp_tables` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(555) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reference_table` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reference_table_scope` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidw_service_cluster`
--

DROP TABLE IF EXISTS `minidw_service_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidw_service_cluster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cluster_schema` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'http',
  `cluster_hostname` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `app_context` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'minidw',
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_config_tags`
--

DROP TABLE IF EXISTS `minidwcm_config_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_config_tags` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name_UNIQUE` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_config_tags_key_value_pairs`
--

DROP TABLE IF EXISTS `minidwcm_config_tags_key_value_pairs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_config_tags_key_value_pairs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_id` bigint(20) DEFAULT NULL,
  `seq_id` bigint(20) DEFAULT NULL,
  `config_key` longtext COLLATE utf8_unicode_ci,
  `config_value` longtext COLLATE utf8_unicode_ci,
  `config_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2804 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_context_parameters`
--

DROP TABLE IF EXISTS `minidwcm_context_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_context_parameters` (
  `param_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `paramval` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `IsActive` bit(1) DEFAULT b'1',
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`param_id`),
  UNIQUE KEY `param_name_UNIQUE` (`param_name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_context_parameters_data_validation_mapping`
--

DROP TABLE IF EXISTS `minidwcm_context_parameters_data_validation_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_context_parameters_data_validation_mapping` (
  `mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_id` bigint(20) NOT NULL,
  `validation_id` bigint(20) NOT NULL,
  `param_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_date` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`param_id`,`validation_id`),
  UNIQUE KEY `etl_il_context_parameters_mapping_UK` (`mapping_id`),
  KEY `etl_il_mapping_il_fk_idx` (`validation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1152 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_context_parameters_dl_mapping`
--

DROP TABLE IF EXISTS `minidwcm_context_parameters_dl_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_context_parameters_dl_mapping` (
  `mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_id` bigint(20) DEFAULT NULL,
  `dl_id` bigint(20) DEFAULT NULL,
  `param_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_date` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`mapping_id`),
  UNIQUE KEY `mapping_id_UNIQUE` (`mapping_id`),
  KEY `etl_dl_mapping_dl_fk` (`dl_id`),
  KEY `etl_dl_mapping_context_fk` (`param_id`),
  CONSTRAINT `etl_dl_mapping_context_fk` FOREIGN KEY (`param_id`) REFERENCES `minidwcm_context_parameters` (`param_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `etl_dl_mapping_dl_fk` FOREIGN KEY (`dl_id`) REFERENCES `minidwcm_dl_info` (`DL_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13305 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_context_parameters_il_mapping`
--

DROP TABLE IF EXISTS `minidwcm_context_parameters_il_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_context_parameters_il_mapping` (
  `mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_id` bigint(20) NOT NULL,
  `il_id` bigint(20) NOT NULL,
  `param_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_date` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`param_id`,`il_id`),
  UNIQUE KEY `etl_il_context_parameters_mapping_UK` (`mapping_id`),
  KEY `etl_il_mapping_il_fk_idx` (`il_id`),
  CONSTRAINT `etl_il_mapping_context_fk` FOREIGN KEY (`param_id`) REFERENCES `minidwcm_context_parameters` (`param_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `etl_il_mapping_il_fk` FOREIGN KEY (`il_id`) REFERENCES `minidwcm_il_info` (`IL_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31614 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_currency_list`
--

DROP TABLE IF EXISTS `minidwcm_currency_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_currency_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `currency_code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `currency_name` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `CURRENCY_CODE_UNQ` (`currency_code`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_data_validation`
--

DROP TABLE IF EXISTS `minidwcm_data_validation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_data_validation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_data_validation_type`
--

DROP TABLE IF EXISTS `minidwcm_data_validation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_data_validation_type` (
  `validation_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `validation_type_name` text CHARACTER SET utf8 NOT NULL,
  `validation_id` int(11) DEFAULT '0',
  `validation_type_desc` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `job_name` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`validation_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_database_connectors`
--

DROP TABLE IF EXISTS `minidwcm_database_connectors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_database_connectors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `connector_id` int(11) DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `connector_fk_idx` (`connector_id`),
  CONSTRAINT `connector_fk` FOREIGN KEY (`connector_id`) REFERENCES `minidwcm_database_types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_database_types`
--

DROP TABLE IF EXISTS `minidwcm_database_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_database_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `driver_name` text COLLATE utf8_unicode_ci,
  `protocal` text COLLATE utf8_unicode_ci,
  `connection_string_params` text COLLATE utf8_unicode_ci,
  `url_format` text COLLATE utf8_unicode_ci,
  `connector_jars` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_dl_analytical_info`
--

DROP TABLE IF EXISTS `minidwcm_dl_analytical_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_dl_analytical_info` (
  `Analytical_DL_ID` bigint(45) NOT NULL AUTO_INCREMENT,
  `DL_id` bigint(11) DEFAULT NULL,
  `Analytical_DL_Name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Analytical_DL_Table_Name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Description` text COLLATE utf8_unicode_ci,
  `Analytical_DL_Schema` mediumtext COLLATE utf8_unicode_ci,
  `ISStandard` bit(1) DEFAULT b'1',
  `Job_Name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `ISActive` bit(1) DEFAULT b'1',
  `Created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Created_time` datetime DEFAULT NULL,
  `Modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Analytical_DL_ID`),
  KEY `DL_any_ID_idx` (`DL_id`),
  CONSTRAINT `DL_any_ID` FOREIGN KEY (`DL_id`) REFERENCES `minidwcm_dl_info` (`DL_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_dl_il_mapping`
--

DROP TABLE IF EXISTS `minidwcm_dl_il_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_dl_il_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dl_id` bigint(20) DEFAULT NULL,
  `il_id` bigint(20) DEFAULT NULL,
  `il_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `IsActive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dl_il_UNIQUE` (`dl_id`,`il_id`),
  KEY `fk_il_id_idx` (`il_id`),
  KEY `dl_id_idx` (`dl_id`),
  CONSTRAINT `fk_il_id` FOREIGN KEY (`il_id`) REFERENCES `minidwcm_il_info` (`IL_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4751 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_dl_info`
--

DROP TABLE IF EXISTS `minidwcm_dl_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_dl_info` (
  `DL_id` bigint(45) NOT NULL AUTO_INCREMENT,
  `DL_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dl_table_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `DL_schema` mediumtext COLLATE utf8_unicode_ci,
  `isStandard` bit(1) DEFAULT b'1',
  `industry_id` bigint(45) DEFAULT NULL,
  `DL_tgt_key` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `Src_IL_id` bigint(10) DEFAULT NULL,
  `checkedDl` bit(1) DEFAULT b'0',
  `isActive` bit(1) DEFAULT b'1',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_execution_type` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `elt_job_tag` bigint(20) DEFAULT NULL,
  `elt_load_parameter` bigint(20) DEFAULT NULL,
  `elt_master_id` bigint(20) DEFAULT NULL,
  `dl_info_by_version` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`DL_id`),
  KEY `id_idx` (`industry_id`),
  KEY `il_id_idx` (`Src_IL_id`),
  CONSTRAINT `base_il_id` FOREIGN KEY (`Src_IL_id`) REFERENCES `minidwcm_il_info` (`IL_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id` FOREIGN KEY (`industry_id`) REFERENCES `minidwcm_verticals` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_elt_job_tags`
--

DROP TABLE IF EXISTS `minidwcm_elt_job_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_elt_job_tags` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `global_values` bigint(20) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `job_tag_name_UNIQUE` (`tag_name`),
  KEY `fl_job_tag_global_config_tag_idx` (`global_values`),
  CONSTRAINT `fl_job_tag_global_config_tag` FOREIGN KEY (`global_values`) REFERENCES `minidwcm_config_tags` (`tag_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_elt_job_tags_config_derived_component_mapping`
--

DROP TABLE IF EXISTS `minidwcm_elt_job_tags_config_derived_component_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_elt_job_tags_config_derived_component_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_mapping_id` bigint(20) NOT NULL,
  `derived_component_config_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_job_derived_mapping_idx` (`job_mapping_id`),
  KEY `fl_job_tag_mapping_derived_tag_idx` (`derived_component_config_id`),
  CONSTRAINT `fk_job_derived_mapping` FOREIGN KEY (`job_mapping_id`) REFERENCES `minidwcm_elt_job_tags_config_mapping` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fl_job_tag_mapping_derived_tag` FOREIGN KEY (`derived_component_config_id`) REFERENCES `minidwcm_config_tags` (`tag_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_elt_job_tags_config_mapping`
--

DROP TABLE IF EXISTS `minidwcm_elt_job_tags_config_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_elt_job_tags_config_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_tag_id` bigint(20) NOT NULL,
  `job_seq` int(4) NOT NULL,
  `config_prop_tag` bigint(20) DEFAULT NULL,
  `values_prop_tag` bigint(20) DEFAULT NULL,
  `stats_prop_tag` bigint(20) DEFAULT NULL,
  `isactive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_job_tag_mapping_idx` (`job_tag_id`),
  KEY `fl_job_tag_mapping_conf_tag_idx` (`config_prop_tag`),
  KEY `fl_job_tag_mapping_value_tag_idx` (`values_prop_tag`),
  KEY `fl_job_tag_mapping_stats_tag_idx` (`stats_prop_tag`),
  CONSTRAINT `fk_job_tag_mapping` FOREIGN KEY (`job_tag_id`) REFERENCES `minidwcm_elt_job_tags` (`tag_id`) ON UPDATE NO ACTION,
  CONSTRAINT `fl_job_tag_mapping_conf_tag` FOREIGN KEY (`config_prop_tag`) REFERENCES `minidwcm_config_tags` (`tag_id`) ON UPDATE NO ACTION,
  CONSTRAINT `fl_job_tag_mapping_stats_tag` FOREIGN KEY (`stats_prop_tag`) REFERENCES `minidwcm_config_tags` (`tag_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fl_job_tag_mapping_value_tag` FOREIGN KEY (`values_prop_tag`) REFERENCES `minidwcm_config_tags` (`tag_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_elt_load_parameters`
--

DROP TABLE IF EXISTS `minidwcm_elt_load_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_elt_load_parameters` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `no_of_executors` int(4) NOT NULL,
  `executor_memory` int(4) NOT NULL,
  `executor_memory_type` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `executor_cores` int(4) NOT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='contains no of executers, memory and executer cores';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_elt_master_configuration`
--

DROP TABLE IF EXISTS `minidwcm_elt_master_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_elt_master_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `spark_job_path` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `elt_class_path` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `elt_library_path` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `master` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `deploy_mode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spark_master` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `source_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spark_submit_mode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `protocol` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `host` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `port` bigint(20) DEFAULT NULL,
  `user_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_submit_mode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `authentication_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ppk_file` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0',
  `is_active` bit(1) DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='holds the spark job path, master, sparkmaster elt core job path etc';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_elt_master_configuration_variables_mapping`
--

DROP TABLE IF EXISTS `minidwcm_elt_master_configuration_variables_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_elt_master_configuration_variables_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `master_config_id` bigint(20) NOT NULL,
  `key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `value` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_master_config_mapping_id_idx` (`master_config_id`),
  CONSTRAINT `fk_master_config_mapping_id` FOREIGN KEY (`master_config_id`) REFERENCES `minidwcm_elt_master_configuration` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_error_logs`
--

DROP TABLE IF EXISTS `minidwcm_error_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_error_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `error_code` mediumtext COLLATE utf8_unicode_ci,
  `error_message` mediumtext COLLATE utf8_unicode_ci,
  `error_body` text COLLATE utf8_unicode_ci,
  `error_datetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `requested_service` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `received_parameters` text COLLATE utf8_unicode_ci,
  `user_id` bigint(20) DEFAULT NULL,
  `client_details` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=727 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_etl_table_scripts_client_mapping`
--

DROP TABLE IF EXISTS `minidwcm_etl_table_scripts_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_etl_table_scripts_client_mapping` (
  `id` bigint(45) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(45) DEFAULT NULL,
  `script_id` bigint(45) DEFAULT NULL,
  `priority` bigint(45) DEFAULT NULL,
  `isChecked` bit(1) DEFAULT b'0',
  `isError` bit(1) DEFAULT b'0',
  `isExecuted` bit(1) DEFAULT b'0',
  `created_by` text CHARACTER SET utf8,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_by` text CHARACTER SET utf8,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1956 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_il_info`
--

DROP TABLE IF EXISTS `minidwcm_il_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_il_info` (
  `IL_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `IL_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `il_type` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `il_table_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `xref_il_table_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `IL_schema` mediumtext COLLATE utf8_unicode_ci,
  `IL_schema_Stg` mediumtext COLLATE utf8_unicode_ci,
  `isStandard` bit(1) DEFAULT b'1',
  `DL_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_execution_type` char(1) COLLATE utf8_unicode_ci DEFAULT 'T',
  `elt_job_tag` bigint(20) DEFAULT NULL,
  `elt_load_parameter` bigint(20) DEFAULT NULL,
  `elt_master_id` bigint(20) DEFAULT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `purge_script` text COLLATE utf8_unicode_ci,
  `src_file_context_param_key` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `target_table_context_param_key` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `il_info_by_version` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`IL_id`),
  UNIQUE KEY `IL_name_UNIQUE` (`IL_name`)
) ENGINE=InnoDB AUTO_INCREMENT=439 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_il_prebuild_queries_mapping`
--

DROP TABLE IF EXISTS `minidwcm_il_prebuild_queries_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_il_prebuild_queries_mapping` (
  `mapping_id` int(11) NOT NULL AUTO_INCREMENT,
  `il_id` bigint(10) DEFAULT NULL,
  `database_type_id` int(11) DEFAULT NULL,
  `il_query` mediumtext COLLATE utf8_unicode_ci,
  `il_incremental_update_query` mediumtext COLLATE utf8_unicode_ci,
  `historical_load` mediumtext COLLATE utf8_unicode_ci,
  `max_date_query` mediumtext COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `Updated_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`mapping_id`),
  UNIQUE KEY `il_id_2` (`il_id`,`database_type_id`),
  KEY `il_id` (`il_id`),
  KEY `database_type_id` (`database_type_id`),
  CONSTRAINT `minidwcm_il_prebuild_queries_mapping_ibfk_1` FOREIGN KEY (`il_id`) REFERENCES `minidwcm_il_info` (`IL_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `minidwcm_il_prebuild_queries_mapping_ibfk_2` FOREIGN KEY (`database_type_id`) REFERENCES `minidwcm_database_connectors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1015 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_il_templates`
--

DROP TABLE IF EXISTS `minidwcm_il_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_il_templates` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `il_id` bigint(10) DEFAULT NULL,
  `filename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `xref_filename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `isActive` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `il_id_fk_idx` (`il_id`),
  CONSTRAINT `il_id_fk` FOREIGN KEY (`il_id`) REFERENCES `minidwcm_il_info` (`IL_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_kpis`
--

DROP TABLE IF EXISTS `minidwcm_kpis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_kpis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kpi_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `kpi_description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kpi_name_UNIQUE` (`kpi_name`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_kpis_dl_mapping`
--

DROP TABLE IF EXISTS `minidwcm_kpis_dl_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_kpis_dl_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dl_id` bigint(10) NOT NULL,
  `kpi_id` bigint(10) NOT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dl_id_fk_kpi_idx` (`dl_id`),
  CONSTRAINT `dl_id_fk_kpi` FOREIGN KEY (`dl_id`) REFERENCES `minidwcm_dl_info` (`DL_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=841 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_quartz_scheduler_jobs`
--

DROP TABLE IF EXISTS `minidwcm_quartz_scheduler_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_quartz_scheduler_jobs` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduler_id` bigint(20) DEFAULT NULL,
  `job_key_name` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `group_name` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_description` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `end_time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cron_expression` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `next_fire_time` varchar(400) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1839 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_quartz_scheduler_master`
--

DROP TABLE IF EXISTS `minidwcm_quartz_scheduler_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_quartz_scheduler_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `master_id` bigint(20) DEFAULT NULL,
  `name` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `started_time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shutdown_time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `timezone` text COLLATE utf8_unicode_ci,
  `ip_address` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=751 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_quartz_scheduler_triggerfires`
--

DROP TABLE IF EXISTS `minidwcm_quartz_scheduler_triggerfires`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_quartz_scheduler_triggerfires` (
  `trigger_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `description` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fire_time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `end_time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(450) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`trigger_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_verticals`
--

DROP TABLE IF EXISTS `minidwcm_verticals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_verticals` (
  `id` bigint(45) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) NOT NULL DEFAULT b'1',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_ws_api_mapping`
--

DROP TABLE IF EXISTS `minidwcm_ws_api_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_ws_api_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `ws_template_id` bigint(10) NOT NULL,
  `il_id` bigint(10) NOT NULL,
  `api_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `api_url` text COLLATE utf8_unicode_ci NOT NULL,
  `base_url_required` bit(1) DEFAULT b'0',
  `api_method_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `api_path_params` text COLLATE utf8_unicode_ci,
  `api_request_params` text COLLATE utf8_unicode_ci,
  `api_body_params` text COLLATE utf8_unicode_ci,
  `pagination_required` bit(1) DEFAULT b'0',
  `pagination_type` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pagination_request_params` text COLLATE utf8_unicode_ci,
  `incremental_update` bit(1) DEFAULT b'0',
  `incremental_update_params` text COLLATE utf8_unicode_ci,
  `response_column_object_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `response_object_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `soap_body_element` text COLLATE utf8_unicode_ci,
  `webservice_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ssl_disable` bit(1) DEFAULT NULL,
  `default_mapping` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ws_template_id` (`ws_template_id`,`api_name`),
  KEY `ws_new_il_id_fk_indx` (`il_id`),
  KEY `ws_new_id_fk` (`ws_template_id`),
  CONSTRAINT `ws_new_id_fk` FOREIGN KEY (`ws_template_id`) REFERENCES `minidwcm_ws_template_mst` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ws_new_il_id_fk_indx` FOREIGN KEY (`il_id`) REFERENCES `minidwcm_il_info` (`IL_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=360 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_ws_authentication_types`
--

DROP TABLE IF EXISTS `minidwcm_ws_authentication_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_ws_authentication_types` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `authentication_type` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `authentication_token` text COLLATE utf8_unicode_ci,
  `authentication_refresh_token` text COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `authentication_type` (`authentication_type`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_ws_template_auth_request_params`
--

DROP TABLE IF EXISTS `minidwcm_ws_template_auth_request_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_ws_template_auth_request_params` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `ws_template_id` bigint(10) NOT NULL,
  `param_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `is_mandatory` bit(1) DEFAULT b'1',
  `is_passwordtype` bit(1) DEFAULT b'0',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ws_template_id` (`ws_template_id`,`param_name`),
  CONSTRAINT `ws_template_id_auth_params_fk` FOREIGN KEY (`ws_template_id`) REFERENCES `minidwcm_ws_template_mst` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcm_ws_template_mst`
--

DROP TABLE IF EXISTS `minidwcm_ws_template_mst`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcm_ws_template_mst` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `web_service_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `authentication_type` bigint(10) NOT NULL,
  `date_format` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time_zone` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Asia/Kolkata',
  `sleep_time` bigint(200) DEFAULT '5000',
  `retry_limit` bigint(20) DEFAULT '5',
  `base_url` text COLLATE utf8_unicode_ci,
  `authentication_url` text COLLATE utf8_unicode_ci,
  `base_url_required` bit(1) DEFAULT b'0',
  `authentication_method_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `api_auth_request_params` text COLLATE utf8_unicode_ci,
  `api_auth_body_params` text COLLATE utf8_unicode_ci,
  `api_auth_request_body_params` text COLLATE utf8_unicode_ci,
  `api_auth_request_headers` text COLLATE utf8_unicode_ci,
  `callback_url` text COLLATE utf8_unicode_ci,
  `access_token_url` text COLLATE utf8_unicode_ci,
  `response_type` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `grant_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `webservice_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `soap_body_element` text COLLATE utf8_unicode_ci,
  `clientid` text COLLATE utf8_unicode_ci,
  `client_secret` text COLLATE utf8_unicode_ci,
  `ssl_disable` bit(1) DEFAULT NULL,
  `scope` text COLLATE utf8_unicode_ci,
  `state` text COLLATE utf8_unicode_ci,
  `session_auth_urls` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `web_service_name` (`web_service_name`),
  KEY `ws_auth_type_fk` (`authentication_type`),
  CONSTRAINT `ws_auth_type_fk` FOREIGN KEY (`authentication_type`) REFERENCES `minidwcm_ws_authentication_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcs_data_validation_script`
--

DROP TABLE IF EXISTS `minidwcs_data_validation_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcs_data_validation_script` (
  `script_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `validation_id` bigint(20) NOT NULL,
  `database_connector_id` int(11) DEFAULT '0',
  `script_name` varchar(200) CHARACTER SET utf8 NOT NULL,
  `validation_script` mediumtext COLLATE utf8_unicode_ci,
  `prepare_stmt` bit(1) DEFAULT NULL,
  `validation_type_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_name` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dependency_jars` text COLLATE utf8_unicode_ci,
  `active` bit(1) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`script_id`),
  UNIQUE KEY `script_name_UNIQUE` (`script_name`,`validation_id`),
  KEY `validation_id_idx` (`validation_id`),
  CONSTRAINT `validation_id` FOREIGN KEY (`validation_id`) REFERENCES `minidwcm_data_validation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwcs_data_validation_script_dl_il_mapping`
--

DROP TABLE IF EXISTS `minidwcs_data_validation_script_dl_il_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwcs_data_validation_script_dl_il_mapping` (
  `validation_mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `script_id` int(11) NOT NULL,
  `dl_id` int(11) NOT NULL DEFAULT '0',
  `il_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`validation_mapping_id`),
  UNIQUE KEY `script_id_UNIQUE` (`script_id`,`dl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_IL_Client_Currency`
--

DROP TABLE IF EXISTS `minidwm_IL_Client_Currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_IL_Client_Currency` (
  `Client_Currency_Key` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Surrogate Key &nbsp;&nbsp;+ Auto Generated',
  `Client_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Client Id as Defined in Anvizent Data Prep Tool',
  `Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Currency Code details for Base currency or Transactional Currency setup.',
  `Transe_Curr` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Conv_Curr` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cmpny_Currency_Descr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Currency',
  `Currency_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of the Currency',
  `Order_Seq` bigint(20) DEFAULT NULL COMMENT 'Order Seq  that need to be shown in Dashboard',
  `Rpt_Base_Currency_Flag` bit(1) DEFAULT NULL COMMENT 'If Current Country code is a Base Currency the need to Set the Flag',
  `Symbol` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Symbol of the Currency',
  `Decimal_Digit` bigint(20) DEFAULT NULL COMMENT 'Decimal Digit of the currency',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/CREATEd date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user',
  PRIMARY KEY (`Client_Currency_Key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_application_versions`
--

DROP TABLE IF EXISTS `minidwm_application_versions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_application_versions` (
  `version_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `version_number` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `file_path` text COLLATE utf8_unicode_ci,
  `file_size` bigint(20) DEFAULT NULL,
  `number_of_downloads` bigint(20) DEFAULT '0',
  `last_downloaded_date` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `is_latest_version` bit(1) DEFAULT b'0',
  PRIMARY KEY (`version_id`),
  UNIQUE KEY `version_number_UNIQUE` (`version_number`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_aws_credentials`
--

DROP TABLE IF EXISTS `minidwm_aws_credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_aws_credentials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `secret_key` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `access_key` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `region` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_aws_regions`
--

DROP TABLE IF EXISTS `minidwm_aws_regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_aws_regions` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `region_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `region_name_UNIQUE` (`region_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_client_currency_mapping`
--

DROP TABLE IF EXISTS `minidwm_client_currency_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_client_currency_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `currency_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dashboard_currency` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `accountCurrencyCode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `otherCurrencyCode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientId_unique` (`clientId`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_clients_instant_script_execution`
--

DROP TABLE IF EXISTS `minidwm_clients_instant_script_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_clients_instant_script_execution` (
  `clients_instant_script_id` bigint(45) NOT NULL AUTO_INCREMENT,
  `clientIds` text,
  `sqlscript` longtext,
  `executionType` varchar(45) DEFAULT NULL,
  `created_by` text,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `isactive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`clients_instant_script_id`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_common_etl_jobs`
--

DROP TABLE IF EXISTS `minidwm_common_etl_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_common_etl_jobs` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `job_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_file` mediumtext COLLATE utf8_unicode_ci,
  `is_active` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_currency_integration`
--

DROP TABLE IF EXISTS `minidwm_currency_integration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_currency_integration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_url` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `access_key` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `currencies` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `source` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time_hours` char(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time_minutes` char(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time_zone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_file_names` text COLLATE utf8_unicode_ci,
  `client_specific_Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_specific_job_file_names` text COLLATE utf8_unicode_ci,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_data_validation_integration`
--

DROP TABLE IF EXISTS `minidwm_data_validation_integration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_data_validation_integration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time_zone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_file_names` text COLLATE utf8_unicode_ci,
  `client_specific_Job_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_specific_job_file_names` text COLLATE utf8_unicode_ci,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_database_schema_variables`
--

DROP TABLE IF EXISTS `minidwm_database_schema_variables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_database_schema_variables` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `database_connector_id` bigint(10) DEFAULT NULL,
  `database_variable` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `schema_variable` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_default_templates`
--

DROP TABLE IF EXISTS `minidwm_default_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_default_templates` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `trial_template` bit(1) DEFAULT b'1',
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `template_name_UNIQUE` (`template_name`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_default_templates_currency`
--

DROP TABLE IF EXISTS `minidwm_default_templates_currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_default_templates_currency` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) DEFAULT NULL,
  `currency_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dashboard_currency` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `accountCurrencyCode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `otherCurrencyCode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `templateId` (`template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_default_templates_general_settings`
--

DROP TABLE IF EXISTS `minidwm_default_templates_general_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_default_templates_general_settings` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `clientId` bigint(10) NOT NULL,
  `flat_file` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `database_settings` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `webservice` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientId_UNIQUE` (`clientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_default_templates_mapping`
--

DROP TABLE IF EXISTS `minidwm_default_templates_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_default_templates_mapping` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `template_id` bigint(11) NOT NULL,
  `type_of_mapping` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `master_id` bigint(11) DEFAULT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `template_id_idx` (`template_id`),
  CONSTRAINT `template_id` FOREIGN KEY (`template_id`) REFERENCES `minidwm_default_templates` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=779 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_elt_configuration_keys`
--

DROP TABLE IF EXISTS `minidwm_elt_configuration_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_elt_configuration_keys` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_type` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Should contain three type of values 1. staging 2.FK 3. main or 4.DL',
  `key_sequence` int(11) NOT NULL COMMENT 'contain unique value for each type',
  `config_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'hold config key name',
  `config_value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'holds either placeholderor contant value',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_type_seq` (`config_type`,`key_sequence`),
  UNIQUE KEY `unique_type_key_name` (`config_type`,`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='contains the data of main configuation keys with sequence';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_elt_il_configuration`
--

DROP TABLE IF EXISTS `minidwm_elt_il_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_elt_il_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'can be used while configuting il info',
  `il_id` int(10) NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_elt_il_configuration_key_value_mapping`
--

DROP TABLE IF EXISTS `minidwm_elt_il_configuration_key_value_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_elt_il_configuration_key_value_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `il_elt_id` bigint(20) NOT NULL,
  `config_id` bigint(20) NOT NULL,
  `is_skipped` bit(1) NOT NULL,
  `prop_value` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_il_elt_id_idx` (`il_elt_id`),
  KEY `fl_il_config_key_value_mapping_idx` (`config_id`),
  CONSTRAINT `fk_il_elt_id` FOREIGN KEY (`il_elt_id`) REFERENCES `minidwm_elt_il_configuration` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fl_il_config_key_value_mapping` FOREIGN KEY (`config_id`) REFERENCES `minidwm_elt_configuration_keys` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='contaiing the mapping of il and elt key value pairs';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_etl_table_scripts`
--

DROP TABLE IF EXISTS `minidwm_etl_table_scripts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_etl_table_scripts` (
  `id` bigint(45) NOT NULL AUTO_INCREMENT,
  `script_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_id` bigint(45) DEFAULT NULL,
  `target_schema` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `script_description` text COLLATE utf8_unicode_ci,
  `script_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sql_script` longtext COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  `created_by` text CHARACTER SET utf8,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_by` text CHARACTER SET utf8,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_etl_table_scripts_error_logs`
--

DROP TABLE IF EXISTS `minidwm_etl_table_scripts_error_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_etl_table_scripts_error_logs` (
  `id` bigint(40) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(40) DEFAULT NULL,
  `error_msg` text COLLATE utf8_unicode_ci,
  `script_id` bigint(40) DEFAULT NULL,
  `created_by` text CHARACTER SET utf8,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text CHARACTER SET utf8,
  `modified_time` datetime DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_etl_table_scripts_history`
--

DROP TABLE IF EXISTS `minidwm_etl_table_scripts_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_etl_table_scripts_history` (
  `id` bigint(45) NOT NULL AUTO_INCREMENT,
  `script_id` bigint(45) NOT NULL,
  `script_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_id` bigint(45) DEFAULT NULL,
  `target_schema` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `script_description` text COLLATE utf8_unicode_ci,
  `script_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sql_script` longtext COLLATE utf8_unicode_ci,
  `isActive` bit(1) DEFAULT b'1',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  `created_by` text CHARACTER SET utf8,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_by` text CHARACTER SET utf8,
  `modified_time` datetime DEFAULT NULL,
  `history_updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_file_settings`
--

DROP TABLE IF EXISTS `minidwm_file_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_file_settings` (
  `id` bigint(20) NOT NULL,
  `max_file_size_in_mb` bigint(20) DEFAULT NULL,
  `trail_user_max_file_size_in_mb` bigint(10) DEFAULT '1',
  `multipart_file_enabled` bit(1) DEFAULT b'0',
  `no_of_records_per_file` bigint(40) DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `IsActive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_hybrid_clients_grouping_mapping`
--

DROP TABLE IF EXISTS `minidwm_hybrid_clients_grouping_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_hybrid_clients_grouping_mapping` (
  `group_id` bigint(20) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `mapped_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`client_id`),
  KEY `hybrid_client` (`group_id`),
  CONSTRAINT `hybrid_client` FOREIGN KEY (`group_id`) REFERENCES `minidwm_hybrid_clients_grouping_master` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_hybrid_clients_grouping_master`
--

DROP TABLE IF EXISTS `minidwm_hybrid_clients_grouping_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_hybrid_clients_grouping_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `access_key` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `last_modfied_date` datetime NOT NULL,
  `interval_type` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `interval_time` bigint(20) DEFAULT NULL,
  `thread_count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_instant_script_execution_client_auditlog`
--

DROP TABLE IF EXISTS `minidwm_instant_script_execution_client_auditlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_instant_script_execution_client_auditlog` (
  `client_instant_id` bigint(45) NOT NULL AUTO_INCREMENT,
  `client_instant_execution_id` bigint(45) DEFAULT NULL,
  `client_id` bigint(45) DEFAULT NULL,
  `sqlscript` longtext,
  `execution_status` bit(1) DEFAULT NULL,
  `execution_status_msg` longtext,
  `created_by` text,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `isactive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`client_instant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_internalization`
--

DROP TABLE IF EXISTS `minidwm_internalization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_internalization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locale_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `locale_id` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `properties` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_middle_level_manager`
--

DROP TABLE IF EXISTS `minidwm_middle_level_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_middle_level_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `context_path` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `upload_list_end_point` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `write_end_point` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `delete_end_point` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `upgrade_end_point` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_auth_token` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_auth_token` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `encryption_private_key` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `encryption_IV` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_migration_servers_details`
--

DROP TABLE IF EXISTS `minidwm_migration_servers_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_migration_servers_details` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `short_name` varchar(200) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `ip_address` varchar(500) DEFAULT NULL,
  `port_number` varchar(200) DEFAULT NULL,
  `minidw_schema_name` varchar(200) DEFAULT NULL,
  `anvizent_schema_name` varchar(200) DEFAULT NULL,
  `user_name` varchar(200) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_s3_bucket_info`
--

DROP TABLE IF EXISTS `minidwm_s3_bucket_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_s3_bucket_info` (
  `id` bigint(40) NOT NULL AUTO_INCREMENT,
  `aws_bucket_name` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `aws_access_key` text COLLATE utf8_unicode_ci,
  `aws_secret_key` text COLLATE utf8_unicode_ci,
  `created_by` text CHARACTER SET utf8,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text CHARACTER SET utf8,
  `modified_time` datetime DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `aws_bucket_name_UNIQUE` (`aws_bucket_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_s3_client_mapping`
--

DROP TABLE IF EXISTS `minidwm_s3_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_s3_client_mapping` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(10) NOT NULL,
  `s3_bucket_info_id` bigint(45) NOT NULL,
  `created_by` text COLLATE utf8_unicode_ci,
  `created_time` datetime DEFAULT NULL,
  `modified_by` text COLLATE utf8_unicode_ci,
  `modified_time` datetime DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id_UNIQUE` (`client_id`),
  KEY `s3_bucket_info_fk_idx` (`s3_bucket_info_id`),
  CONSTRAINT `s3_bucket_info_fk` FOREIGN KEY (`s3_bucket_info_id`) REFERENCES `minidwm_s3_bucket_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=343 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_client_config`
--

DROP TABLE IF EXISTS `minidwm_scheduler_client_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_client_config` (
  `thread_count` int(11) NOT NULL,
  `thread_priority` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cron_expression` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`thread_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_master`
--

DROP TABLE IF EXISTS `minidwm_scheduler_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(4) NOT NULL,
  `aws_id` int(4) DEFAULT NULL,
  `instance_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip_address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `server_state` bit(1) DEFAULT NULL,
  `scheduler_running_state` bit(1) DEFAULT NULL,
  `is_checking_completed` bit(1) DEFAULT NULL,
  `state_msg` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `package_execution_interval` bigint(20) DEFAULT NULL,
  `package_upload_interval` bigint(20) DEFAULT NULL,
  `history_upload_interval` bigint(20) DEFAULT NULL,
  `history_execution_interval` bigint(20) DEFAULT NULL,
  `request_schema` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `source_upload_interval` bigint(20) DEFAULT NULL,
  `source_execution_interval` bigint(20) DEFAULT NULL,
  `slave_heartbeat_interval` bigint(20) DEFAULT NULL,
  `thread_count` bigint(20) DEFAULT NULL,
  `is_currency_load_required` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `master_ip_unique` (`ip_address`),
  UNIQUE KEY `master_awsinstance_unique` (`instance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_master_client_mapping`
--

DROP TABLE IF EXISTS `minidwm_scheduler_master_client_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_master_client_mapping` (
  `master_id` bigint(20) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_master_slave_mapping`
--

DROP TABLE IF EXISTS `minidwm_scheduler_master_slave_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_master_slave_mapping` (
  `master_id` bigint(20) NOT NULL,
  `slave_id` bigint(20) NOT NULL,
  KEY `master_id` (`master_id`),
  KEY `slave_id` (`slave_id`),
  KEY `slave_id_2` (`master_id`,`slave_id`),
  CONSTRAINT `master_id_fk_mapping` FOREIGN KEY (`master_id`) REFERENCES `minidwm_scheduler_master` (`id`) ON DELETE CASCADE,
  CONSTRAINT `slave_id_fk_mapping` FOREIGN KEY (`slave_id`) REFERENCES `minidwm_scheduler_slave` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_server_type`
--

DROP TABLE IF EXISTS `minidwm_scheduler_server_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_server_type` (
  `id` int(4) NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_slave`
--

DROP TABLE IF EXISTS `minidwm_scheduler_slave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_slave` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(4) NOT NULL,
  `aws_id` int(4) DEFAULT NULL,
  `instance_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip_address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `no_of_package_source_uploads` int(4) NOT NULL DEFAULT '0',
  `no_of_package_executions` int(4) NOT NULL DEFAULT '0',
  `no_of_history_load_source_uploads` int(4) NOT NULL DEFAULT '0',
  `no_of_history_load_executions` int(4) NOT NULL DEFAULT '0',
  `is_active` bit(1) NOT NULL DEFAULT b'1',
  `request_schema` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slave_ip_unique` (`ip_address`),
  UNIQUE KEY `slave_awsinstance_unique` (`instance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_state`
--

DROP TABLE IF EXISTS `minidwm_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `master_id` bigint(20) DEFAULT NULL,
  `server_state` bit(1) DEFAULT NULL,
  `scheduler_running_state` bit(1) DEFAULT NULL,
  `state_msg` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_upload_queue`
--

DROP TABLE IF EXISTS `minidwm_scheduler_upload_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_upload_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `master_id` bigint(20) DEFAULT NULL,
  `total_packages` int(6) DEFAULT NULL,
  `saved_date` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `added_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `upload_master_id_fk_mapping` (`master_id`),
  CONSTRAINT `upload_master_id_fk_mapping` FOREIGN KEY (`master_id`) REFERENCES `minidwm_scheduler_master` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_scheduler_upload_queue_list`
--

DROP TABLE IF EXISTS `minidwm_scheduler_upload_queue_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_scheduler_upload_queue_list` (
  `upload_id` bigint(20) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `package_id` bigint(20) NOT NULL,
  `uploadtime` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  UNIQUE KEY `upload_list_UNIQUE` (`upload_id`,`client_id`,`package_id`),
  CONSTRAINT `upload_list_id_fk_mapping` FOREIGN KEY (`upload_id`) REFERENCES `minidwm_scheduler_upload_queue` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_zones_master`
--

DROP TABLE IF EXISTS `minidwm_zones_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_zones_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `zone_offset` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone_name_display` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `zone_offset_UNIQUE` (`zone_offset`,`zone_name`),
  UNIQUE KEY `zone_name_UNIQUE` (`zone_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1050 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minidwm_zones_master_bak`
--

DROP TABLE IF EXISTS `minidwm_zones_master_bak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minidwm_zones_master_bak` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `zone_offset` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone_name_display` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `display_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `brief_description` text COLLATE utf8_unicode_ci NOT NULL,
  `elaborated_description` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `parent_module_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_module_name` (`name`),
  KEY `fk_module_parent_module_id_module_id` (`parent_module_id`),
  KEY `fk_module_created_by_user_id` (`created_by`),
  KEY `fk_module_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_module_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_module_parent_module_id_module_id` FOREIGN KEY (`parent_module_id`) REFERENCES `module` (`id`),
  CONSTRAINT `fk_module_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mw_inc_update`
--

DROP TABLE IF EXISTS `mw_inc_update`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mw_inc_update` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) DEFAULT NULL,
  `type_of_source` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `il_id` bigint(20) DEFAULT NULL,
  `inc_date_from_source` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orguser_client_status`
--

DROP TABLE IF EXISTS `orguser_client_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orguser_client_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `status_description` text,
  `statusdoc` blob,
  `status_submission_date` date DEFAULT NULL,
  `userid` bigint(20) NOT NULL,
  `userworkflowid` bigint(20) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `created_by` varchar(200) DEFAULT NULL,
  `updated_by` varchar(200) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orguser_client_status_history`
--

DROP TABLE IF EXISTS `orguser_client_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orguser_client_status_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `status_description` text,
  `statusdoc` blob,
  `status_submission_date` date DEFAULT NULL,
  `userid` bigint(20) NOT NULL,
  `userworkflowid` bigint(20) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `created_by` varchar(200) DEFAULT NULL,
  `updated_by` varchar(200) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orguser_role`
--

DROP TABLE IF EXISTS `orguser_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orguser_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(45) NOT NULL,
  `workflowid` bigint(20) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partner_app_license`
--

DROP TABLE IF EXISTS `partner_app_license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partner_app_license` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partnerId` int(12) DEFAULT NULL,
  `license_details` mediumtext COLLATE utf8_unicode_ci,
  `partner_region` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `license_mode` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `partnerId_UNIQUE` (`partnerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partner_erp_clientMapping`
--

DROP TABLE IF EXISTS `partner_erp_clientMapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partner_erp_clientMapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partner_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `client_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `erp_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_invoice`
--

DROP TABLE IF EXISTS `payment_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_invoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `emailId` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `invoice_number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `invoice_amount` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `invoice_date` datetime DEFAULT NULL,
  `invoice_desc` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `invoice_file_name` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `due_date` datetime DEFAULT NULL,
  `payment_status` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `paid_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invoice_number_UNIQUE` (`invoice_number`),
  UNIQUE KEY `invoice_file_name_UNIQUE` (`invoice_file_name`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_transaction`
--

DROP TABLE IF EXISTS `payment_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `amount` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `transaction_status` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `invoice_number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `region_meta_details`
--

DROP TABLE IF EXISTS `region_meta_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `region_meta_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `region_code` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `region_name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `region_hostname` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `region_port` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `region_username` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `region_password` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `region_vendor` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `region_description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_default` bit(1) DEFAULT b'0',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `datecreated` date DEFAULT NULL,
  `password_expiry_days` int(5) DEFAULT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `cloudclientname` varchar(500) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_ldap_map`
--

DROP TABLE IF EXISTS `role_ldap_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_ldap_map` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `roleid` bigint(10) NOT NULL,
  `ldap_group_name` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roleid` (`roleid`,`ldap_group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_status_master`
--

DROP TABLE IF EXISTS `role_status_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_status_master` (
  `rolename` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  UNIQUE KEY `role_status_unique` (`rolename`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sample_template`
--

DROP TABLE IF EXISTS `sample_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample_template` (
  `firstName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `male` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `female` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `passwd` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sample_template_duplicate`
--

DROP TABLE IF EXISTS `sample_template_duplicate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample_template_duplicate` (
  `userName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gender` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `firstName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dob` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_instance_type`
--

DROP TABLE IF EXISTS `scheduler_instance_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_instance_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `instance_type` varchar(63) COLLATE utf8_unicode_ci NOT NULL,
  `no_of_jobs` int(11) NOT NULL DEFAULT '4',
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scheduler_instance_type_instance_type` (`instance_type`),
  KEY `fk_scheduler_instance_type_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_instance_type_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_instance_type_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_instance_type_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_job`
--

DROP TABLE IF EXISTS `scheduler_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(511) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_command` bigint(20) NOT NULL,
  `base_dir` varchar(511) COLLATE utf8_unicode_ci NOT NULL,
  `env_variables` text COLLATE utf8_unicode_ci,
  `log_additional_dir_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `max_retry_count` int(11) DEFAULT '3',
  `parent_job` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scheduler_job_name` (`name`),
  KEY `fk_scheduler_job_job_command_job_command_id` (`job_command`),
  KEY `fk_scheduler_job_parent_job_job_id` (`parent_job`),
  KEY `fk_scheduler_job_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_job_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_job_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_job_job_command_job_command_id` FOREIGN KEY (`job_command`) REFERENCES `scheduler_job_command` (`id`),
  CONSTRAINT `fk_scheduler_job_parent_job_job_id` FOREIGN KEY (`parent_job`) REFERENCES `scheduler_job` (`id`),
  CONSTRAINT `fk_scheduler_job_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_job_command`
--

DROP TABLE IF EXISTS `scheduler_job_command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_job_command` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `initiator` text COLLATE utf8_unicode_ci NOT NULL,
  `options` text COLLATE utf8_unicode_ci,
  `job` text COLLATE utf8_unicode_ci NOT NULL,
  `args` text COLLATE utf8_unicode_ci,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_scheduler_job_command_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_job_command_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_job_command_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_job_command_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_job_run`
--

DROP TABLE IF EXISTS `scheduler_job_run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_job_run` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_schedule` bigint(20) NOT NULL,
  `slave_instance_id` varchar(63) COLLATE utf8_unicode_ci NOT NULL,
  `run_number` int(11) DEFAULT '1',
  `retry_count` int(11) DEFAULT '0',
  `state` varchar(31) COLLATE utf8_unicode_ci NOT NULL,
  `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` datetime DEFAULT NULL,
  `log_state` varchar(31) COLLATE utf8_unicode_ci DEFAULT '0',
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_scheduler_job_run_job_schedule_job_schedule_id` (`job_schedule`),
  KEY `fk_scheduler_job_run_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_job_run_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_job_run_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_job_run_job_schedule_job_schedule_id` FOREIGN KEY (`job_schedule`) REFERENCES `scheduler_job_schedule` (`id`),
  CONSTRAINT `fk_scheduler_job_run_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_job_schedule`
--

DROP TABLE IF EXISTS `scheduler_job_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_job_schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job` bigint(20) NOT NULL,
  `schedule` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `active` bit(1) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scheduler_job_schedule_job_schedule` (`job`,`schedule`),
  KEY `fk_scheduler_job_schedule_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_job_schedule_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_job_schedule_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_job_schedule_job_job_id` FOREIGN KEY (`job`) REFERENCES `scheduler_job` (`id`),
  CONSTRAINT `fk_scheduler_job_schedule_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_master`
--

DROP TABLE IF EXISTS `scheduler_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(511) COLLATE utf8_unicode_ci DEFAULT NULL,
  `host` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `port` int(11) NOT NULL,
  `protocol` varchar(63) COLLATE utf8_unicode_ci NOT NULL,
  `slave_type` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scheduler_master_host` (`host`),
  UNIQUE KEY `uk_scheduler_master_name` (`name`),
  KEY `fk_scheduler_master_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_master_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_master_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_master_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_master_client`
--

DROP TABLE IF EXISTS `scheduler_master_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_master_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `master` bigint(20) NOT NULL,
  `client` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scheduler_master_client_master_client` (`master`,`client`),
  KEY `fk_scheduler_master_client_client_cloud_client_id` (`client`),
  KEY `fk_scheduler_master_client_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_master_client_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_master_client_client_cloud_client_id` FOREIGN KEY (`client`) REFERENCES `cloud_client` (`id`),
  CONSTRAINT `fk_scheduler_master_client_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_master_client_master_scheduler_master_id` FOREIGN KEY (`master`) REFERENCES `scheduler_master` (`id`),
  CONSTRAINT `fk_scheduler_master_client_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_master_slave`
--

DROP TABLE IF EXISTS `scheduler_master_slave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_master_slave` (
  `id` bigint(10) NOT NULL,
  `master` int(11) DEFAULT NULL,
  `slave` bigint(10) DEFAULT NULL,
  `active` bit(1) DEFAULT b'1',
  `created_by` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `updated_date` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_node_type`
--

DROP TABLE IF EXISTS `scheduler_node_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_node_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_slave`
--

DROP TABLE IF EXISTS `scheduler_slave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_slave` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `instance_id` varchar(63) CHARACTER SET utf8 DEFAULT NULL,
  `instance_type` bigint(20) DEFAULT NULL,
  `master` bigint(20) NOT NULL,
  `host` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `base_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `terminatable` bit(1) DEFAULT b'1',
  `active` bit(1) DEFAULT b'1',
  `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` datetime DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_scheduler_slave_master_scheduler_master_id` (`master`),
  KEY `fk_scheduler_slave_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_slave_updated_by_user_id` (`updated_by`),
  KEY `fk_scheduler_slave_instance_type_scheduler_instance_type_id` (`instance_type`),
  CONSTRAINT `fk_scheduler_slave_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_slave_instance_type_scheduler_instance_type_id` FOREIGN KEY (`instance_type`) REFERENCES `scheduler_instance_type` (`id`),
  CONSTRAINT `fk_scheduler_slave_master_scheduler_master_id` FOREIGN KEY (`master`) REFERENCES `scheduler_master` (`id`),
  CONSTRAINT `fk_scheduler_slave_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler_slave_init_config`
--

DROP TABLE IF EXISTS `scheduler_slave_init_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_slave_init_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `no_of_jobs_in_queue` int(11) NOT NULL,
  `instance_type` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(20) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scheduler_s_i_conf_no_of_jobs_in_queue` (`no_of_jobs_in_queue`),
  KEY `fk_scheduler_s_i_conf_instance_type_scheduler_instance_type_id` (`instance_type`),
  KEY `fk_scheduler_s_i_conf_created_by_user_id` (`created_by`),
  KEY `fk_scheduler_s_i_conf_updated_by_user_id` (`updated_by`),
  CONSTRAINT `fk_scheduler_s_i_conf_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_scheduler_s_i_conf_instance_type_scheduler_instance_type_id` FOREIGN KEY (`instance_type`) REFERENCES `scheduler_instance_type` (`id`),
  CONSTRAINT `fk_scheduler_s_i_conf_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sqldatatypes`
--

DROP TABLE IF EXISTS `sqldatatypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sqldatatypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datatypename` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `datatypesize` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `template_scripts`
--

DROP TABLE IF EXISTS `template_scripts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `template_scripts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `script_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `job_details` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `test12333`
--

DROP TABLE IF EXISTS `test12333`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test12333` (
  `col1` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='dsadadsasd';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `pswd` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `roleid` bigint(20) DEFAULT NULL,
  `datecreated` datetime DEFAULT NULL,
  `isactive` bit(1) DEFAULT NULL,
  `lastloggedin` datetime DEFAULT NULL,
  `mobile` bigint(13) DEFAULT NULL,
  `previous_password` varchar(255) DEFAULT '',
  `password_last_modified` date DEFAULT NULL,
  `password_expiry_days` int(5) DEFAULT NULL,
  `authtype` varchar(20) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `cloudclientname` varchar(500) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `user_group` bigint(20) DEFAULT NULL,
  `workingstuff` varchar(255) DEFAULT NULL,
  `workingsalt` varchar(255) DEFAULT NULL,
  `usergroupid` bigint(20) DEFAULT NULL,
  `ahsid` varchar(55) DEFAULT NULL,
  `isagree` int(11) NOT NULL DEFAULT '0',
  `primaryDeviceType` varchar(45) DEFAULT NULL,
  `primaryDeviceId` varchar(255) DEFAULT NULL,
  `is_org_user` bit(1) DEFAULT b'0',
  `dateupdated` datetime DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  `is_email_verified` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `FK_USER__AND_USER_GROUP_idx` (`user_group`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userDetailsforTesting`
--

DROP TABLE IF EXISTS `userDetailsforTesting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userDetailsforTesting` (
  `UserName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `DOB` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Salary` int(11) DEFAULT NULL,
  `Company` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`UserName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_activity_mapping`
--

DROP TABLE IF EXISTS `user_activity_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_activity_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `activity_id` bigint(20) NOT NULL,
  `activity_assignment_type` bit(1) DEFAULT NULL,
  `admin_access` bit(1) DEFAULT b'0',
  `assign_access` bit(1) DEFAULT b'0',
  `assigned_user` bigint(20) DEFAULT NULL,
  `assigned_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_user_activity_mapping` (`userid`),
  KEY `fk_user_activity_mapping` (`activity_id`),
  CONSTRAINT `fk_user_activity_mapping` FOREIGN KEY (`activity_id`) REFERENCES `activity_master` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_user_activity_mapping` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2165 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_partner_mapping`
--

DROP TABLE IF EXISTS `user_partner_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_partner_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `partnerId` int(11) DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wishlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(145) NOT NULL,
  `datesavedon` varchar(45) DEFAULT NULL,
  `datasourcedev` varchar(145) DEFAULT NULL,
  `devtable` varchar(145) DEFAULT NULL,
  `clientdbdetailsid` bigint(20) DEFAULT NULL,
  `datasourceprod` varchar(145) DEFAULT NULL,
  `externalsourcewithinternalquery` varchar(45) DEFAULT NULL,
  `prodtable` varchar(145) DEFAULT NULL,
  `category` varchar(145) DEFAULT NULL,
  `description` text,
  `filename` varchar(500) DEFAULT NULL,
  `defaultdata` mediumtext,
  `dashboard_script` mediumtext,
  `htmldashboardfile` mediumtext,
  `thejsfile` mediumtext,
  `limitwhere` varchar(255) DEFAULT NULL,
  `thumbnail` longblob,
  `dashbordlogo` blob,
  `status` varchar(45) DEFAULT 'ACTIVE',
  `status_last_modified` date DEFAULT NULL,
  `grouptypeid` bigint(20) DEFAULT NULL,
  `datasource_type` varchar(100) DEFAULT NULL,
  `cloudclientid` bigint(20) DEFAULT NULL,
  `cloudclientname` varchar(500) DEFAULT NULL,
  `clientstatus` varchar(100) DEFAULT NULL,
  `wishlisttype` varchar(45) DEFAULT 'dashboard',
  `dashboard_version` varchar(100) DEFAULT NULL,
  `erp_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wishlist_name_erpid_constraint` (`name`,`erp_id`),
  KEY `fk_wishlist_erp` (`erp_id`),
  CONSTRAINT `fk_wishlist_erp` FOREIGN KEY (`erp_id`) REFERENCES `erp_master` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'anvizent'
--

--
-- Dumping routines for database 'anvizent'
--
/*!50003 DROP PROCEDURE IF EXISTS `DeleteClient_IN_Master` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`almadmin`@`%` PROCEDURE `DeleteClient_IN_Master`(IN cid INT)
BEGIN
SET SQL_SAFE_UPDATES = 0;

DELETE FROM `activation_links_log` WHERE `client_id` = cid;
DELETE FROM `anvizent_properties` WHERE `cloudclientid` = cid;
DELETE FROM `app_license` WHERE `clientId` = cid;
DELETE FROM `app_user_proxy` WHERE `clientId` = cid;
DELETE FROM `clientdbdetails` WHERE `cloudclientid` = cid;
DELETE FROM `client_features` WHERE `clientid` = cid;
-- Master Delete
DELETE FROM `cloud_client`WHERE `id` = cid;
SET SQL_SAFE_UPDATES = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-28 13:14:46
