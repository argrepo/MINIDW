CREATE DATABASE  IF NOT EXISTS `mahender_1010009_1010027_staging` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `mahender_1010009_1010027_staging`;
-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: 192.168.0.131    Database: mahender_1010009_1010027_staging
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
-- Table structure for table `AIL_Sales_Forecast_Staging`
--

DROP TABLE IF EXISTS `AIL_Sales_Forecast_Staging`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AIL_Sales_Forecast_Staging` (
  `DIMENSION1` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION2` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION3` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION4` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION5` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `YEAR` bigint(20) NOT NULL,
  `MONTH` bigint(20) NOT NULL,
  `ACTUAL_SALES` double DEFAULT NULL,
  `CREATED_DATETIME` datetime DEFAULT CURRENT_TIMESTAMP,
  `CREATED_BY` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'AI_User',
  `MODIFIED_DATETIME` datetime DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_BY` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'AI_User'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AIL_Sales_Forecast_Staging1`
--

DROP TABLE IF EXISTS `AIL_Sales_Forecast_Staging1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AIL_Sales_Forecast_Staging1` (
  `DIMENSION1` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION2` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION3` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION4` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DIMENSION5` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `YEAR` bigint(20) NOT NULL,
  `MONTH` bigint(20) NOT NULL,
  `ACTUAL_SALES` double DEFAULT NULL,
  `CREATED_DATETIME` datetime DEFAULT CURRENT_TIMESTAMP,
  `CREATED_BY` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'AI_User',
  `MODIFIED_DATETIME` datetime DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_BY` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'AI_User',
  PRIMARY KEY (`DIMENSION1`,`DIMENSION2`,`DIMENSION3`,`DIMENSION4`,`DIMENSION5`,`YEAR`,`MONTH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=43194 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
-- Table structure for table `Emp99`
--

DROP TABLE IF EXISTS `Emp99`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Emp99` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(999) COLLATE utf8_unicode_ci DEFAULT NULL,
  `salary` decimal(10,0) DEFAULT NULL,
  `designation` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_AP_Payment_Detail_Stg`
--

DROP TABLE IF EXISTS `IL_AP_Payment_Detail_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_AP_Payment_Detail_Stg` (
  `Payment_Hdr_Id` bigint(20) NOT NULL,
  `Payment_Dtl_Line_Id` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Mode` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Item_Extension_No` bigint(20) DEFAULT NULL,
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Payment_Amount` decimal(18,2) DEFAULT NULL,
  `Discount_Available_Amount` decimal(18,2) DEFAULT NULL,
  `Discount_Taken_Amount` decimal(18,2) DEFAULT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Discount_Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Curr_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Foreign_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Foreign_Payment_Amount` decimal(18,2) DEFAULT NULL,
  `Foreign_Discount_Available_Amount` decimal(18,2) DEFAULT NULL,
  `Foreign_Discount_Taken_Amount` decimal(18,2) DEFAULT NULL,
  `Remarks` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_AP_Payment_Hdr_Stg`
--

DROP TABLE IF EXISTS `IL_AP_Payment_Hdr_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_AP_Payment_Hdr_Stg` (
  `Payment_Hdr_Id` bigint(20) NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Payment_Mode` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Date` datetime DEFAULT NULL,
  `Payment_Credited_Date` datetime DEFAULT NULL,
  `GL_Account_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Post_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fiscal_Year` int(10) DEFAULT NULL,
  `Batch_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bank_Account_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Amount` decimal(18,2) DEFAULT NULL,
  `Payee_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Trans_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bank_Statement_Ref_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Instrument_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Logged_Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Account_Analysis_Category_Stg`
--

DROP TABLE IF EXISTS `IL_Account_Analysis_Category_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Account_Analysis_Category_Stg` (
  `Analysis_Cat_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Account_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Analysis_code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Update_Count` bigint(20) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Account_Category_Stg`
--

DROP TABLE IF EXISTS `IL_Account_Category_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Account_Category_Stg` (
  `Acct_Cat_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `COA_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Acct_Cat_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Acct_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Acct_Cat_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Transaction_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Group_Code_Lvl1` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Group_Code_Lvl2` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Group_Code_Lvl3` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Group_Code_Lvl4` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `BNumber01` decimal(18,2) DEFAULT '0.00',
  `BNumber02` decimal(18,2) DEFAULT '0.00',
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown',
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'unknown'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Account_Ledger_HOSP_Stg`
--

DROP TABLE IF EXISTS `IL_Account_Ledger_HOSP_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Account_Ledger_HOSP_Stg` (
  `Journal_No` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Journal_Line_No` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Account_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Asset_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Period` int(11) DEFAULT NULL,
  `Transaction_Date` datetime DEFAULT NULL,
  `Due_Date` datetime DEFAULT NULL,
  `Local_Curr_Amount` decimal(18,2) DEFAULT NULL,
  `Journal_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Journal_Source` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Refer` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Journal_Line_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Journal_Entry_date` datetime DEFAULT NULL,
  `Cridit_Debit_Indicator` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Indicator` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Sub_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Currency_Convertion_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Currency_Convertion_Rate` decimal(18,2) DEFAULT NULL,
  `Corporat_Curr_Amount` decimal(18,2) DEFAULT NULL,
  `Other_Amt_Decimal_Points` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Provisional_Posting_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis3` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis4` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis5` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis6` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis7` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis8` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis9` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis10` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Report_Amount` decimal(18,2) DEFAULT NULL,
  `Memo_Amount` decimal(18,2) DEFAULT NULL,
  `Posting_Date` datetime DEFAULT NULL,
  `Exclude_Bal_Flag` bit(1) DEFAULT NULL,
  `Journal_Class_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Originator_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Originated_Date` datetime DEFAULT NULL,
  `Journal_Reversal_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Occupancy_Flag` bit(1) DEFAULT NULL,
  `Occupancy_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Department_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Revenue_flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Account_Ledger_Stg`
--

DROP TABLE IF EXISTS `IL_Account_Ledger_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Account_Ledger_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Date` datetime NOT NULL,
  `Ledger_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Posted_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GL_Posted_Date` datetime DEFAULT NULL,
  `Batch_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_Date` datetime DEFAULT NULL,
  `Account_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Acct_Cat_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COA_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GL_Account_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Current_Account_Flag` bit(1) DEFAULT NULL,
  `Acctount_Mode` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `AP_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GL_Debit_Amount` decimal(18,2) DEFAULT NULL,
  `AR_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GL_Credit_Amount` decimal(18,2) DEFAULT NULL,
  `Subsidiary_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Ledger_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Ledger_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Acc_Ledger_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Acc_Ledger_Remarks` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Offset_GL_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reverse_Void_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fiscal_Year` int(10) DEFAULT NULL,
  `Fiscal_Period` bigint(20) DEFAULT NULL,
  `Amount` decimal(18,2) DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ordered_Qty` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Checked_Date` datetime DEFAULT NULL,
  `Check_Cleared_Date` datetime DEFAULT NULL,
  `Supplier_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GL_Acct_Segment_Val1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GL_Acct_Segment_Val2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GL_Source_Module` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supp_Invoice_Date` datetime DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,12) DEFAULT NULL,
  `Receipt_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Registration_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Account_Payable_Hdr_Stg`
--

DROP TABLE IF EXISTS `IL_Account_Payable_Hdr_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Account_Payable_Hdr_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Date` datetime DEFAULT NULL,
  `Voucher_Due_Date` datetime DEFAULT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Fiscal_Period` int(11) DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vocher_Amount` decimal(24,7) DEFAULT NULL,
  `Discount_Amount` decimal(24,7) DEFAULT NULL,
  `Tax_Amount` decimal(24,7) DEFAULT NULL,
  `Payment_Terms_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Remarks` varchar(8000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Approver_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL,
  `Post_Date` datetime DEFAULT NULL,
  `Pay_Instrument` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Purchase_Order_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Purchase_Order_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Pay_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Non_PO_Flag` bit(1) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Account_Payable_Stg`
--

DROP TABLE IF EXISTS `IL_Account_Payable_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Account_Payable_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Pay_Item_Extension_No` bigint(20) NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Voucher_Date` datetime DEFAULT NULL,
  `Supplier_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Non_Tax_Amount` decimal(18,2) DEFAULT NULL,
  `Pay_Instrument` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Ledger_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Ledger_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Due_Date` datetime DEFAULT NULL,
  `Discount_Due_Date` datetime DEFAULT NULL,
  `Invoice_date` datetime DEFAULT NULL,
  `Post_Date` datetime DEFAULT NULL,
  `Sent_Date` datetime DEFAULT NULL,
  `Match_Date` datetime DEFAULT NULL,
  `Fiscal_Period` int(11) DEFAULT NULL,
  `Misc_Amount` decimal(18,2) DEFAULT NULL,
  `Voucher_Line_Amount` decimal(18,2) DEFAULT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Pay_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Gross_Amount` decimal(18,2) DEFAULT NULL,
  `Open_Amount` decimal(18,2) DEFAULT NULL,
  `Unpaid_Amount` decimal(18,2) DEFAULT NULL,
  `Discount_Available_Flag` bit(1) DEFAULT NULL,
  `Discount_Amount` decimal(24,7) DEFAULT NULL,
  `Tax_Amount` decimal(18,2) DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,12) DEFAULT NULL,
  `Total_Voucher_Amount` decimal(18,2) DEFAULT NULL,
  `Supplier_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Purchase_Order_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Purchase_Order_Line_Number` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Purchase_Order_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Qty` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Non_PO_Flag` bit(1) DEFAULT NULL,
  `Posting_Hold_Flag` bit(1) DEFAULT NULL,
  `Payment_Terms_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Remarks` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Approver_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bank_Transit_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Advance_Inv_Flag` bit(1) DEFAULT NULL,
  `Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Line_Number` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Type` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Account_Stg`
--

DROP TABLE IF EXISTS `IL_Account_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Account_Stg` (
  `Account_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `COA_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Account_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Acc_Abbreviated_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Acc_Full_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Acc_Analysis_Flag` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Activities_Stg`
--

DROP TABLE IF EXISTS `IL_Activities_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Activities_Stg` (
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `ActivityID` bigint(19) NOT NULL,
  `ActivityTypeID` int(3) DEFAULT NULL,
  `Status` int(3) DEFAULT NULL,
  `ReasonCodeID` int(3) DEFAULT NULL,
  `DateActivity` datetime DEFAULT NULL,
  `Description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TouchedBy` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TouchedAt` datetime DEFAULT NULL,
  `CreatedBy` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `walletid` bigint(19) DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`DataSource_Id`,`ActivityID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Activity_Stg`
--

DROP TABLE IF EXISTS `IL_Activity_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Activity_Stg` (
  `Activity_Id` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Id` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Organization_Id` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Person_Id` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Activity_Done_Flag` bit(1) DEFAULT NULL,
  `Activity_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Activity_Due_Date` datetime DEFAULT NULL,
  `Activity_Duration` datetime DEFAULT NULL,
  `Marked_As_Done_Date` datetime DEFAULT NULL,
  `Act_Subject` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Person_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Organization_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Note` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reference_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reference_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Due_Time` datetime DEFAULT NULL,
  `Activity_Add_Time` datetime DEFAULT NULL,
  `Assigned_To_User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Created_By_User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Activity_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Activity_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Activity_Type_Stg` (
  `Activity_Type_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Activity_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Color` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Custom_Flag` bit(1) DEFAULT NULL,
  `Seq_Num` bigint(20) DEFAULT NULL,
  `Key_String` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Icon_Key` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Adjustment_Code_Stg`
--

DROP TABLE IF EXISTS `IL_Adjustment_Code_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Adjustment_Code_Stg` (
  `Adjustment_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Adjustment_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Adjustment_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Alert_Eligible_User_Stg`
--

DROP TABLE IF EXISTS `IL_Alert_Eligible_User_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Alert_Eligible_User_Stg` (
  `Alert_Type_Key` bigint(20) NOT NULL COMMENT 'Relation with IL Alert type table for schedule',
  `User_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Client id that eligible for notification of the Alert',
  `IsActive` bit(1) DEFAULT NULL COMMENT 'If active then 0 else 1',
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Alert_Exception_Stg`
--

DROP TABLE IF EXISTS `IL_Alert_Exception_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Alert_Exception_Stg` (
  `Alert_Schedule_Key` bigint(20) NOT NULL,
  `Exception_Date` datetime DEFAULT NULL COMMENT 'Exception date that user no need to get the Alert',
  `Exception_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Actve/Inactive',
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or Sysdate',
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added User',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or Sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Alert_Schedule_Stg`
--

DROP TABLE IF EXISTS `IL_Alert_Schedule_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Alert_Schedule_Stg` (
  `Alert_Type_Key` bigint(20) NOT NULL COMMENT 'Relation with IL Alert type table for schedule',
  `Schedule_Time` datetime DEFAULT NULL COMMENT 'Time of the Scheduled',
  `WEEKDAY` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'If weekly then which week this need to Run the setup',
  `NEXT_RUN_DATE` datetime DEFAULT NULL COMMENT 'Date of the job next running date',
  `RECURRENCE_TYPE` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Yearly/Quarterly/Monthly/Weekly/Daily/Hourly',
  `JOB_NAME` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Scheduling Job in queue and  pick up by client',
  `SCHEDULE_STATUS` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Active/Inactive',
  `Last_Run_Date` datetime DEFAULT NULL COMMENT 'Last Run date of the Schedule',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Alerts_Stg`
--

DROP TABLE IF EXISTS `IL_Alerts_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Alerts_Stg` (
  `Alert_Type_Key` bigint(20) NOT NULL,
  `Alerts_Run_Datetime` datetime DEFAULT NULL COMMENT 'Alert Schedule run date and time',
  `Alert_Message` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Alert message that User will get in app or Page',
  `Alert_Category` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of Alert',
  `Triggered_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Executed Manually or Automatically',
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Analysis_Category_Stg`
--

DROP TABLE IF EXISTS `IL_Analysis_Category_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Analysis_Category_Stg` (
  `Analysis_Cat_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Analysis_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Label_Length` int(10) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Analysis_Code_Stg`
--

DROP TABLE IF EXISTS `IL_Analysis_Code_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Analysis_Code_Stg` (
  `Analysis_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Analysis_Cat_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Analysis_Cat_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Analysis_Cat_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Analysis_Entity_Defn_Stg`
--

DROP TABLE IF EXISTS `IL_Analysis_Entity_Defn_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Analysis_Entity_Defn_Stg` (
  `Analysis_Entity_Def` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Analysis_Cat_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Update_Count` bigint(20) DEFAULT NULL,
  `Last_Change_User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Change_date` datetime DEFAULT NULL,
  `Entry_Num` int(10) DEFAULT NULL,
  `Validate_indicator` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Abbreviated_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Asset_HOSP_Stg`
--

DROP TABLE IF EXISTS `IL_Asset_HOSP_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Asset_HOSP_Stg` (
  `Asset_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Asset_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Abbr_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Asset_Stg`
--

DROP TABLE IF EXISTS `IL_Asset_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Asset_Stg` (
  `Asset_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `Asset_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Group_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Taken_Date` datetime DEFAULT NULL,
  `Asset_Serial_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Manufacturer` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Model_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Location_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Type_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Status_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Qty` decimal(24,7) DEFAULT NULL,
  `Asset_Unit_Price` decimal(24,7) DEFAULT NULL,
  `Asset_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Amount` decimal(24,7) DEFAULT NULL,
  `Assigned_Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Liability` decimal(18,2) DEFAULT NULL,
  `Asset_Depreciation_Amount` decimal(18,2) DEFAULT NULL,
  `Insurance_Policy_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Insurance_Date` datetime DEFAULT NULL,
  `Insuranced_By` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Insurance_Premium_Amount` decimal(18,2) DEFAULT NULL,
  `Insurance_Renewal_Date` datetime DEFAULT NULL,
  `Insurance_Amount` decimal(18,2) DEFAULT NULL,
  `Insurance_Expiration_Date` datetime DEFAULT NULL,
  `Lease_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Leased_By` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lease_Start_Date` datetime DEFAULT NULL,
  `Lease_End_Date` datetime DEFAULT NULL,
  `Lease_Flag` bit(1) DEFAULT NULL,
  `Lease_Total_Amount` decimal(18,2) DEFAULT NULL,
  `Lease_Mileage_Amount` decimal(18,2) DEFAULT NULL,
  `Overide_Cost` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `Bboolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Bill_Of_Material_Stg`
--

DROP TABLE IF EXISTS `IL_Bill_Of_Material_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Bill_Of_Material_Stg` (
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Primary Product/Item Code for Finished/Semi Finished Product for Manufacturing',
  `Raw_Material_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Required Row Matieral to produce Semi Finished or Finished Product',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company that Product going to Produce',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Plant Details that FP or SFP Product manufacturing',
  `Bill_Planned_Date` datetime NOT NULL COMMENT 'Date That Product Planned to produce',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Material_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Material type either Manufactrured or Puchased',
  `Percent_Of_Scrap` decimal(24,7) DEFAULT NULL COMMENT 'Percentage of Scrap Row material required to Produce the Product',
  `Percent_Of_Yield` decimal(24,7) DEFAULT NULL COMMENT 'Percentage of Row material required to produce the Product',
  `Required_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Required Raw materials to produce the Product',
  `Raw_Material_Unit_Price` decimal(24,12) DEFAULT NULL COMMENT 'Unit Price of the Raw Material',
  `Unit_Of_Measure` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'UOM measurement',
  `Raw_Material_Cost` decimal(18,2) DEFAULT NULL COMMENT 'Total cost of the Raw material to produce the product for each Raw material',
  `LeadTime_Offset_Days` int(10) DEFAULT NULL COMMENT 'Lead time to produce Product',
  `ECO_Number` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Engineering Chane Order Number',
  `Engg_Change_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Engineering Change Reason',
  `Engg_Change_Date` datetime DEFAULT NULL COMMENT 'Date - Engineering Change Date',
  `Effective_Start_Date` datetime DEFAULT NULL COMMENT 'Effective Valid date range of the Raw material  is valid',
  `Effective_End_Date` datetime DEFAULT NULL COMMENT 'Effective Valid date range of the Raw material  is valid',
  `BCharacter01` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Bin_Stg`
--

DROP TABLE IF EXISTS `IL_Bin_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Bin_Stg` (
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Bin Location Id details at the Department',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Branch Details that Bin located',
  `Bin_Name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Bin Location.',
  `Bin_Description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Bin',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant that Bin located',
  `Aisle_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Location_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zone_Location_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Capacity` decimal(24,6) DEFAULT NULL COMMENT 'Capacity of the Bin',
  `Location` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Location of the Bin',
  `Bin_Usable_Length` decimal(18,2) DEFAULT NULL,
  `Bin_Usable_Width` decimal(18,2) DEFAULT NULL,
  `Bin_Usable_Height` decimal(18,2) DEFAULT NULL,
  `Usable_Size_UOM` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Capable_Weight` decimal(18,2) DEFAULT NULL,
  `Capable_Weight_UOM` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Bookings_Goal_Stg`
--

DROP TABLE IF EXISTS `IL_Bookings_Goal_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Bookings_Goal_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company or Org Id of the Plant for the Booking goal Setup',
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Plant or Bussiness unit defined',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Product or Item Code or Short description that Goal for setup',
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Sales representive code that Booking goal set',
  `Start_Date` datetime NOT NULL COMMENT 'Begin period for booking  goal for sales representive',
  `End_Date` datetime NOT NULL COMMENT 'End period for booking  goal for sales representive',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Code of the Currency that Revenue/Price defined',
  `RevenueForecast` decimal(24,7) DEFAULT NULL COMMENT 'Revenue target set for sales representive',
  `MinimumRevenue` decimal(24,7) DEFAULT NULL COMMENT 'Minimum Revenue target set for sales representive',
  `MaximumRevenue` decimal(24,7) DEFAULT NULL COMMENT 'Maximum Revenue target set for sales representive',
  `MinimumUnits` decimal(24,7) DEFAULT NULL COMMENT 'Minimum Units goal for product to sales representive',
  `MaximumUnits` decimal(24,7) DEFAULT NULL COMMENT 'Maximum Units goal for product to sales representive',
  `ProdPlanMTD` decimal(24,7) DEFAULT NULL COMMENT 'Production Planned Make to Order',
  `ProdPlanMTS` decimal(24,7) DEFAULT NULL COMMENT 'Production Planned Make to Stock',
  `BookingForecast` decimal(24,7) DEFAULT NULL COMMENT 'Booking Forecast for Sales person',
  `BookingForecastUnits` decimal(24,7) DEFAULT NULL COMMENT 'Booking Forecast Units for Sales Person',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Budget_Acct_Ledger_HOSP_Stg`
--

DROP TABLE IF EXISTS `IL_Budget_Acct_Ledger_HOSP_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Budget_Acct_Ledger_HOSP_Stg` (
  `Journal_No` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Journal_Line_No` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Account_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Asset_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Period` int(10) DEFAULT NULL,
  `Transaction_Date` datetime DEFAULT NULL,
  `Due_Date` datetime DEFAULT NULL,
  `Local_Curr_Amount` decimal(18,2) DEFAULT NULL,
  `Journal_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Journal_Source` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Refer` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Journal_Line_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Journal_Entry_date` datetime DEFAULT NULL,
  `Cridit_Debit_Indicator` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Indicator` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Asset_Sub_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Currency_Convertion_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Currency_Convertion_Rate` decimal(18,2) DEFAULT NULL,
  `Corporat_Curr_Amount` decimal(18,2) DEFAULT NULL,
  `Other_Amt_Decimal_Points` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Provisional_Flag` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis3` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis4` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis5` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis6` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis7` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis8` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis9` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ledger_Analysis10` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Report_Amount` decimal(18,2) DEFAULT NULL,
  `Memo_Amount` decimal(18,2) DEFAULT NULL,
  `Posting_Date` datetime DEFAULT NULL,
  `Exclude_Bal_Flag` bit(1) DEFAULT NULL,
  `Journal_Class_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Originator_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Originated_Date` datetime DEFAULT NULL,
  `Journal_Reversal_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Occupancy_Flag` bit(1) DEFAULT NULL,
  `Occupancy_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Department_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Revenue_flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Business_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Business_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Business_Type_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Business_Type_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Business_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Business_Type_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Cal_Center_Stg`
--

DROP TABLE IF EXISTS `IL_Cal_Center_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Cal_Center_Stg` (
  `Cal_Center_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Cal_Center_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cal_Center_Version` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cal_Center_Url` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Custom_Settings` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Update_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Campaign_Members_Stg`
--

DROP TABLE IF EXISTS `IL_Campaign_Members_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Campaign_Members_Stg` (
  `Campaign_Members_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Campaign_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Campaign_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Lead_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contact_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First_Responded_Date` datetime DEFAULT NULL,
  `Responded_Flag` bit(1) DEFAULT NULL,
  `Campaign_Member_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDatetime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Campaign_Stg`
--

DROP TABLE IF EXISTS `IL_Campaign_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Campaign_Stg` (
  `Campaign_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Campaign_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Campaign_Date` datetime DEFAULT NULL,
  `Campaign_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Campaign_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Campaign_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Campaign_Owner_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Start_Date` datetime DEFAULT NULL,
  `End_Date` datetime DEFAULT NULL,
  `Expected_Revenue` decimal(24,7) DEFAULT NULL,
  `Budgeted_Amount` decimal(24,7) DEFAULT NULL,
  `Actual_Spent_Amount` decimal(24,7) DEFAULT NULL,
  `Expect_Resp_InPercnt` decimal(24,7) DEFAULT NULL,
  `Campaign_Sent_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `No_Of_Leads` bigint(20) DEFAULT NULL,
  `No_Of_Conv_Leads` bigint(20) DEFAULT NULL,
  `No_Of_Contacts` bigint(20) DEFAULT NULL,
  `No_Of_Responses` bigint(20) DEFAULT NULL,
  `No_Of_Opp` bigint(20) DEFAULT NULL,
  `No_Of_Won_Opp` bigint(20) DEFAULT NULL,
  `Total_Opp_Amount` decimal(18,2) DEFAULT NULL,
  `Opp_Won_Amount` decimal(18,2) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Cards_Stg`
--

DROP TABLE IF EXISTS `IL_Cards_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Cards_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Card_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Card_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Card_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last4_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First6_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Expiration_Date` datetime DEFAULT NULL,
  `Token` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Cash_Events_Stg`
--

DROP TABLE IF EXISTS `IL_Cash_Events_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Cash_Events_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Device_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Note` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Amount_Change` bigint(20) DEFAULT NULL,
  `Event_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Events_Date` datetime DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Client_Currency_Stg`
--

DROP TABLE IF EXISTS `IL_Client_Currency_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Client_Currency_Stg` (
  `Client_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Client Id as Defined in Anvizent Data Prep Tool',
  `Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Currency Code details for Base currency or Transactional Currency setup.',
  `Cmpny_Currency_Descr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Currency',
  `Currency_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of the Currency',
  `Order_Seq` bigint(20) DEFAULT NULL COMMENT 'Order Seq  that need to be shown in Dashboard',
  `Rpt_Base_Currency_Flag` bit(1) DEFAULT NULL COMMENT 'If Current Country code is a Base Currency the need to Set the Flag',
  `Symbol` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Symbol of the Currency',
  `Decimal_Digit` bigint(20) DEFAULT NULL COMMENT 'Decimal Digit of the currency',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Comcast_Stg`
--

DROP TABLE IF EXISTS `IL_Comcast_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Comcast_Stg` (
  `ID` int(11) DEFAULT NULL,
  `ItemNumber` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CustomerName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Qty` float DEFAULT NULL,
  `ExtCost` float DEFAULT NULL,
  `Vendor` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Division` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Month` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ProdType` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ProdCategory` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Year` int(11) DEFAULT NULL,
  KEY `tbl_Comcastind_year` (`Year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Company_CRM_Stg`
--

DROP TABLE IF EXISTS `IL_Company_CRM_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Company_CRM_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Code of the company/Id of the company with Uniquely identified.',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Company_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Company',
  `Address1` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address of the Company',
  `Address2` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address of the Company',
  `City` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'City of the Company',
  `Zipcode` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ZipCode of the Company',
  `Email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Email Address of the Company',
  `State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'State of the Company',
  `Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Country details',
  `Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region of the company',
  `Sub_Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sub Region details',
  `Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base Currency Code details',
  `Hierarchy_Lvl1` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Hierarchy_Lvl2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Hierarchy_Lvl3` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Hierarchy_Lvl4` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Bcharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Bcharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Company_Stg`
--

DROP TABLE IF EXISTS `IL_Company_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Company_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Code of the company/Id of the company with Uniquely identified.',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Company_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Company',
  `Address1` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address of the Company',
  `Address2` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address of the Company',
  `City` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'City of the Company',
  `Zipcode` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ZipCode of the Company',
  `Email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Email Address of the Company',
  `State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'State of the Company',
  `Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Country details',
  `Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region of the company',
  `Sub_Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sub Region details',
  `Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base Currency Code details',
  `Hierarchy_Lvl1` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Hierarchy_Lvl2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Hierarchy_Lvl3` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Hierarchy_Lvl4` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy levles',
  `Bcharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Bcharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Contact_Stg`
--

DROP TABLE IF EXISTS `IL_Contact_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Contact_Stg` (
  `Contact_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Contact_Date` datetime DEFAULT NULL,
  `Contact_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Salutation` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Designation` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Department_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Active_Date` datetime DEFAULT NULL,
  `Email_Bounced_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Bounced_Date` datetime DEFAULT NULL,
  `Mailing_Street` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Mailing_City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Mailing_State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Mailing_Zip_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Mailing_Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Mobile_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fax_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` tinyint(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Update_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Container_Stg`
--

DROP TABLE IF EXISTS `IL_Container_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Container_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Container_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Container_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Container_Descr` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Container_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Container_Length` decimal(18,2) DEFAULT NULL,
  `Container_Width` decimal(18,2) DEFAULT NULL,
  `Container_Height` decimal(18,2) DEFAULT NULL,
  `Size_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Container_Weight` decimal(18,2) DEFAULT NULL,
  `Weight_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Contract_Details_Stg`
--

DROP TABLE IF EXISTS `IL_Contract_Details_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Contract_Details_Stg` (
  `Contract_Detail_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Start_Date` datetime DEFAULT NULL,
  `End_Date` datetime DEFAULT NULL,
  `Renew_State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_Form` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Terms_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contracted_Quantity` decimal(24,7) DEFAULT NULL,
  `Minimum_Quantity` decimal(24,7) DEFAULT NULL,
  `Maximum_Quantity` decimal(24,7) DEFAULT NULL,
  `Price_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Minimum_Price` decimal(18,2) DEFAULT NULL,
  `Maximum_Price` decimal(18,2) DEFAULT NULL,
  `Memo_Price` decimal(18,2) DEFAULT NULL,
  `Memo_Price_Level` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Eligible_For_Advance` bit(1) DEFAULT NULL,
  `Contract_Clause` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Contract_Header_Stg`
--

DROP TABLE IF EXISTS `IL_Contract_Header_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Contract_Header_Stg` (
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Contract_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Amount` decimal(18,2) DEFAULT NULL,
  `Contract_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_terms_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Manager` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Price_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Owener_Ship` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Start_Date` datetime DEFAULT NULL,
  `End_Date` datetime DEFAULT NULL,
  `Renew_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Renewable_End_Date` datetime DEFAULT NULL,
  `Renew_Date` datetime DEFAULT NULL,
  `Renew_Period` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Comments` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Buyer` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_Form` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Contract_Partner_Stg`
--

DROP TABLE IF EXISTS `IL_Contract_Partner_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Contract_Partner_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Contract_Partner_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Contract_Partner_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Partner_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Partner_Title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Partner_First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Partner_Sur_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address3` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Territory_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Partner_Position` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Partner_Contact_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fax_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Currency_CRM_Stg`
--

DROP TABLE IF EXISTS `IL_Currency_CRM_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Currency_CRM_Stg` (
  `Currency_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Currency_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Decimal_Points` bigint(20) DEFAULT NULL,
  `Symbol` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Custom_Flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Currency_Rate_Stg`
--

DROP TABLE IF EXISTS `IL_Currency_Rate_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Currency_Rate_Stg` (
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
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Cust_Shipping_Address_Stg`
--

DROP TABLE IF EXISTS `IL_Cust_Shipping_Address_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Cust_Shipping_Address_Stg` (
  `Cust_Ship_Addr_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source customer Shippment Address Id or ship_customer_Id details',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Id that customer Shipment address Required',
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Primary Customer code that shippement address mapped',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Ship_Customer_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shippment  Customer Name',
  `Ship_Addr1` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shippment  address',
  `Ship_Addr2` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shippment  address',
  `Ship_City` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shippment  City',
  `Ship_State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'State of the Shippment',
  `Ship_Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Country of the shippment',
  `Ship_Zip_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shippment Customer Pincode or zip code',
  `Ship_Email_Addr` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shippment customer Email address',
  `Ship_Phone_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Ship customer work or Office phone number',
  `Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region location of the  Customer shipping address',
  `Sub_Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region location of the customer shipping address',
  `Customer_Latitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Latitude details',
  `Customer_Longitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Latitude details',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Customer_Cards_Stg`
--

DROP TABLE IF EXISTS `IL_Customer_Cards_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Customer_Cards_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Card_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customers_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Customer_Res_Stg`
--

DROP TABLE IF EXISTS `IL_Customer_Res_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Customer_Res_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customers_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Addresses1` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Addresses2` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Addresses3` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EmailAddresses` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_Numbers` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Business_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Date_Of_Birth` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Note` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Marketing_Allowed` bit(1) DEFAULT NULL,
  `Customer_Since` bigint(20) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Customer_Ship_Addr_Stg`
--

DROP TABLE IF EXISTS `IL_Customer_Ship_Addr_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Customer_Ship_Addr_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customer_Ship_Addr_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customers_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Addresses1` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Addresses2` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Addresses3` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Customer_Stg`
--

DROP TABLE IF EXISTS `IL_Customer_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Customer_Stg` (
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source Customer Id or Code',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Id details',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Customer_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer Name',
  `Address1` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer address details',
  `Address2` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer address details',
  `City` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer city details',
  `State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'State of the Customer located',
  `Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Country of the Customer Located',
  `Zip_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer postal code details',
  `Email_Addr` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Email address of the customer',
  `Cust_Category_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer category details',
  `Customer_Latitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Latitude details',
  `Customer_Longitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Longitude details',
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales  Representive code that Customer Allocated',
  `Sales_Territory_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Territory code that Sales Representive Located',
  `Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region location of the company',
  `Sub_Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region location of the company',
  `Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base Currency Code that Customer for Transaction',
  `Hierarchy_Lvl1` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy Lvl defined on client Project',
  `Hierarchy_Lvl2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy Lvl defined on client Project',
  `Hierarchy_Lvl3` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy Lvl defined on client Project',
  `Hierarchy_Lvl4` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Hierarchy Lvl defined on client Project',
  `EDI_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Electronic Device Id',
  `Seed_Cust_Flag` bit(1) DEFAULT NULL COMMENT 'Dummy Customer or valid customer_Stg (0 for valid 1 for dummy)',
  `IsActive` bit(1) DEFAULT NULL COMMENT '0 for active record and 1 for inactive record.',
  `Effective_Start_date` datetime DEFAULT NULL COMMENT 'Applicable for SCD2 effective start date transaction',
  `Effective_End_date` datetime DEFAULT NULL COMMENT 'Applicable for SCD2 effective end date transaction',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Dashboard_Stg`
--

DROP TABLE IF EXISTS `IL_Dashboard_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Dashboard_Stg` (
  `Module_Name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Dashobard Module',
  `KPI_Name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the KPI that need to set the Threshold',
  `Mesure_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of measurement ex: % or $',
  `IsActive` bit(1) DEFAULT NULL COMMENT 'Is active KPI then 0 else 1',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Dashboard_Threshold_Stg`
--

DROP TABLE IF EXISTS `IL_Dashboard_Threshold_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Dashboard_Threshold_Stg` (
  `Dashboard_Key` bigint(20) NOT NULL COMMENT 'Reference Key of the Dashboard module with KPI Name',
  `Level_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of Level - At company lvl, Plant lvl or Customer Lvl threshold Defined',
  `Mapping_Column_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Column Mapping required from Database Table',
  `Mapping_Column_Value` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Level Name / Plant/Company/Customer Name Value for the threshold',
  `Color_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Color Code that need to display at Landing Page',
  `Threshold_Position` bigint(20) DEFAULT NULL COMMENT 'Position of the Threshold',
  `Low_Value` decimal(18,2) DEFAULT NULL COMMENT 'Minimum Range of the Threshold for the KPI',
  `High_Value` decimal(18,2) DEFAULT NULL COMMENT 'Maximum Range of the Threshold for the KPI',
  `Period_ValidFrom` datetime DEFAULT NULL COMMENT 'Valid Period from for the KPI',
  `Period_ValidTo` datetime DEFAULT NULL COMMENT 'Valid Period from for the KPI',
  `IsActive` bit(1) DEFAULT NULL COMMENT 'If it is active then it will show in Landing page',
  `Added_DateTime` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Deals_Stg`
--

DROP TABLE IF EXISTS `IL_Deals_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Deals_Stg` (
  `Deal_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Deal_User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Organization_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Stage_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pipeline_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Person_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Currency` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Value` decimal(24,7) DEFAULT NULL,
  `Deal_Active_Flag` bit(1) DEFAULT NULL,
  `Deal_Delete_Flag` bit(1) DEFAULT NULL,
  `Deal_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Stage_Change_Date` datetime DEFAULT NULL,
  `Deal_Lost_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Date` datetime DEFAULT NULL,
  `Deal_Next_Act_Date` datetime DEFAULT NULL,
  `Next_Activity_Date` datetime DEFAULT NULL,
  `Last_Activity_Date` datetime DEFAULT NULL,
  `Deal_Next_Act_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Closed_Date` datetime DEFAULT NULL,
  `Deal_Won_Date` datetime DEFAULT NULL,
  `Deal_Lost_Date` datetime DEFAULT NULL,
  `Deal_First_Won_Date` datetime DEFAULT NULL,
  `Deal_Exp_Close_Date` datetime DEFAULT NULL,
  `Products_Count` bigint(20) DEFAULT NULL,
  `Files_Count` bigint(20) DEFAULT NULL,
  `Notes_Count` bigint(20) DEFAULT NULL,
  `Followers_Count` bigint(20) DEFAULT NULL,
  `Email_Messages_Count` bigint(20) DEFAULT NULL,
  `Activities_Count` bigint(20) DEFAULT NULL,
  `Done_Activities_Count` bigint(20) DEFAULT NULL,
  `Undone_Activities_Count` bigint(20) DEFAULT NULL,
  `Reference_Activities_Count` bigint(20) DEFAULT NULL,
  `Participants_Count` bigint(20) DEFAULT NULL,
  `Next_Activity_Subject` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Next_Activity_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Next_Activity_Duration` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Next_Activity_Note` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Weighted_Value` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Org_Deal_Email` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Created_User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Incoming_Mail_Date` datetime DEFAULT NULL,
  `Last_Outgoing_Mail_Date` datetime DEFAULT NULL,
  `Rotten_Time` datetime DEFAULT NULL,
  `Org_hidden_Flag` bit(1) DEFAULT NULL,
  `Person_Hidden_Flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Demand_Forecast_Stg`
--

DROP TABLE IF EXISTS `IL_Demand_Forecast_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Demand_Forecast_Stg` (
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Forecast_From_Date` datetime DEFAULT NULL,
  `Forecast_To_Date` datetime DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Forecast_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Qty` decimal(24,7) DEFAULT NULL,
  `Forecast_Qty` decimal(24,7) DEFAULT NULL,
  `Consumed_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Extended_Amount` decimal(24,7) DEFAULT NULL,
  `Forecast_Amount` decimal(24,7) DEFAULT NULL,
  `Comments` varchar(8000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Department_Stg`
--

DROP TABLE IF EXISTS `IL_Department_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Department_Stg` (
  `Department_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Department_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Department_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Device_Stg`
--

DROP TABLE IF EXISTS `IL_Device_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Device_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Device_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Device_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Device_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IMEI_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SIM_ICCID_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SIM_IMSI_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Model_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Offline_Payments_Limit` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Offline_TotalMax_Payments_Limit` bigint(20) DEFAULT NULL,
  `Offline_Payments_MaxLimit` bigint(20) DEFAULT NULL,
  `All_OfflinePayments` bit(1) DEFAULT NULL,
  `Offline_Payments_Flag` bit(1) DEFAULT NULL,
  `Max_Offline_Days` bigint(20) DEFAULT NULL,
  `Device_Certificate` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Prefix` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Serial_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pin_Disabled` bit(1) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Discounts_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Discounts_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Discounts_Type_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Discounts_Type_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Discount_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Discount_Approver` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Std_Discount` decimal(24,7) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Dock_Stg`
--

DROP TABLE IF EXISTS `IL_Dock_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Dock_Stg` (
  `Dock_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Dock_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contact_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_ESTTABLE_Stg`
--

DROP TABLE IF EXISTS `IL_ESTTABLE_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_ESTTABLE_Stg` (
  `TITLE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CONTACT` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CUSTCODE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CUSTNAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CustomerName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COUNTRY` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EMAILADD` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FAX` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PHONE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ESTIMATETYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SALESREP` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PODATE` datetime DEFAULT NULL,
  `DUEDATE` datetime DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `COMPLETEDATE` datetime DEFAULT NULL,
  `MATNAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USER1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EstimatesId` bigint(20) DEFAULT NULL,
  `podate_percent` bigint(20) DEFAULT NULL,
  `ontime_percent` bigint(20) DEFAULT NULL,
  `total_days` bigint(20) DEFAULT NULL,
  `QUOTE_COUNT` bigint(20) DEFAULT NULL,
  `Total_SmallValue` decimal(28,12) DEFAULT NULL,
  `Total_LargeValue` decimal(28,12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Emp_Time_Card_Stg`
--

DROP TABLE IF EXISTS `IL_Emp_Time_Card_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Emp_Time_Card_Stg` (
  `Emp_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Work_Log_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Work_Date` datetime NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Machine_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Department_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Fiscal_Period` int(11) DEFAULT NULL,
  `Work_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Team_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shift_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Labour_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Op_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Op_Seq_No` int(11) DEFAULT NULL,
  `Job_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Proj_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Task_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Check_In_Date` datetime DEFAULT NULL,
  `Check_Out_Date` datetime DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Produced_Qty` decimal(24,7) DEFAULT NULL,
  `Burden_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Burden_Rate` decimal(24,7) DEFAULT NULL,
  `Earned_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Labour_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Labour_Rate` decimal(24,7) DEFAULT NULL,
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Actual_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Rework_Flag` bit(1) DEFAULT NULL,
  `Rework_Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Scrap_Qty` decimal(24,7) DEFAULT NULL,
  `WFH_Flag` bit(1) DEFAULT NULL,
  `Timecard_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Work_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Act_Lunch_InTime` decimal(24,7) DEFAULT NULL,
  `Act_Lunch_OutTime` decimal(24,7) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Emp_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Emp_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Emp_Type_Stg` (
  `Emp_Type_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Type_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Type_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_EmployeeRole_Stg`
--

DROP TABLE IF EXISTS `IL_EmployeeRole_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_EmployeeRole_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Role_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Employee_Master_Stg`
--

DROP TABLE IF EXISTS `IL_Employee_Master_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Employee_Master_Stg` (
  `Emp_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Department_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Emp_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Middle_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Type_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Designation` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Manager_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Birth_Date` datetime DEFAULT NULL,
  `Joining_date` datetime DEFAULT NULL,
  `Emp_Address1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Zip_code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_Country_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Primary_Contact_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Secondary_Contact_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fax_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SSN` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Emp_status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Termination_Date` datetime DEFAULT NULL,
  `Termination_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Employee_Salary_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Employee_Salary_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Employee_Salary_Dtl_Stg` (
  `Emp_Salary_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Fiscal_Period` int(11) DEFAULT NULL,
  `Salary_From_Date` datetime DEFAULT NULL,
  `Salary_To_Date` datetime DEFAULT NULL,
  `Pay_Date` datetime DEFAULT NULL,
  `Pay_Period_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Grade` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Class` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Hours` decimal(24,7) DEFAULT NULL,
  `Hourly_Cost` decimal(24,7) DEFAULT NULL,
  `No_Of_Days` int(11) DEFAULT NULL,
  `Exatra_Days` int(11) DEFAULT NULL,
  `Deduction_Amount` decimal(24,7) DEFAULT NULL,
  `Earning_Amount` decimal(24,7) DEFAULT NULL,
  `Net_Pay_Amount` decimal(24,7) DEFAULT NULL,
  `Emp_Account_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bank_Branch_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Employee_Shift_Stg`
--

DROP TABLE IF EXISTS `IL_Employee_Shift_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Employee_Shift_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Shifts_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Role_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Override_In_Employee_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Override_Out_Employee_id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `In_Time` bigint(20) DEFAULT NULL,
  `Out_Time` bigint(20) DEFAULT NULL,
  `Override_In_Time` bigint(20) DEFAULT NULL,
  `Override_Out_Time` bigint(20) DEFAULT NULL,
  `Cash_Tips_Collected` bigint(20) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Employee_Stg`
--

DROP TABLE IF EXISTS `IL_Employee_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Employee_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Nick_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `InviteSent_Flag` bit(1) DEFAULT NULL,
  `IsOwner_flag` bit(1) DEFAULT NULL,
  `Claimed_Date` datetime DEFAULT NULL,
  `Pin_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unhashed_Pin_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Engg_Change_Order_Stg`
--

DROP TABLE IF EXISTS `IL_Engg_Change_Order_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Engg_Change_Order_Stg` (
  `ECO_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Revision_Num` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Draw_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Engineering_Date` datetime DEFAULT NULL,
  `Revision_Start_Date` datetime DEFAULT NULL,
  `Revision_End_Date` datetime DEFAULT NULL,
  `Required_Qty` decimal(24,7) DEFAULT NULL,
  `Change_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Change_Desc` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Material_Cost` decimal(24,7) DEFAULT NULL,
  `Misc_Cost` decimal(24,7) DEFAULT NULL,
  `Other_Cost` decimal(24,7) DEFAULT NULL,
  `Estimated_Time_Sec` decimal(24,7) DEFAULT NULL,
  `Actual_Time_Sec` decimal(24,7) DEFAULT NULL,
  `Estimated_Scrap_Qty` decimal(24,7) DEFAULT NULL,
  `Estimated_Unit_Price` decimal(24,7) DEFAULT NULL,
  `Crew_Size` bigint(20) DEFAULT NULL,
  `Approved_Date` datetime DEFAULT NULL,
  `Approver_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reason_For_Change` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Food_Inventory_Stg`
--

DROP TABLE IF EXISTS `IL_Food_Inventory_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Food_Inventory_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Inventory_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Qty` decimal(18,2) DEFAULT NULL,
  `Item_Qty` decimal(18,2) DEFAULT NULL,
  `Inventory_Date` datetime DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Food_Production_Stg`
--

DROP TABLE IF EXISTS `IL_Food_Production_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Food_Production_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Production_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Item_Qty` decimal(18,2) DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `Total_Amount` decimal(18,2) DEFAULT NULL,
  `Production_Date` datetime DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Goals_Stg`
--

DROP TABLE IF EXISTS `IL_Goals_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Goals_Stg` (
  `Goal_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Stage_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pipeline_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Goals` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Goal_Period` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Expected_Flag` bit(1) DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Goal_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Expected_Sum` decimal(18,2) DEFAULT NULL,
  `Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Expected_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Master_Expected_Flag` bit(1) DEFAULT NULL,
  `Delivered_Flag` bit(1) DEFAULT NULL,
  `Delivered_Sum` decimal(18,2) DEFAULT NULL,
  `Period_Start_Date` datetime DEFAULT NULL,
  `Period_End_Date` datetime DEFAULT NULL,
  `Created_By_User_Id` bigint(20) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Holiday_Stg`
--

DROP TABLE IF EXISTS `IL_Holiday_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Holiday_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Holiday_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Holiday_Date` datetime DEFAULT NULL,
  `Holiday_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Holiday_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Ingredient_Item_Stg`
--

DROP TABLE IF EXISTS `IL_Ingredient_Item_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Ingredient_Item_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Ingredient_Item_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `Ingredient_Qty` decimal(18,2) DEFAULT NULL,
  `Item_Qty` decimal(18,2) DEFAULT NULL,
  `Created_Date` datetime DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Ingredient_Stg`
--

DROP TABLE IF EXISTS `IL_Ingredient_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Ingredient_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Ingredient_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Ingredient_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `IngredientSpec` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Grp_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Sub_Grp_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Line_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Brand_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Height` decimal(18,2) DEFAULT NULL,
  `Ingredient_Length` decimal(18,2) DEFAULT NULL,
  `Ingredient_Width` decimal(18,2) DEFAULT NULL,
  `Size_UOM` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Weight` decimal(18,2) DEFAULT NULL,
  `Weight_UOM` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Inventory_Adjustment_Stg`
--

DROP TABLE IF EXISTS `IL_Inventory_Adjustment_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Inventory_Adjustment_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Transaction_Date` datetime NOT NULL,
  `Transaction_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Transaction_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Location_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transfer_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Adjustment_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Inv_Adjustment_Qty` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Inv_Unit_Price` decimal(18,2) DEFAULT NULL,
  `Inv_Adjustment_Value` decimal(18,2) DEFAULT NULL,
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Inventory_Stg`
--

DROP TABLE IF EXISTS `IL_Inventory_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Inventory_Stg` (
  `Inventory_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'System Generated Batch Number or Stock number that identifies the Inventory system',
  `Inventory_Date` datetime NOT NULL COMMENT 'Stock date or Inventory Stored date',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Plant company id details that having inventory storage available',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant details of the  storage system available',
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Warehouse /Department/Branch where the product stored',
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Areas/place/Rack within each warehouse/Branch that product got stored.',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product id that stored in stock/Storage location',
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer id that stock produced.',
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Order number that product stored',
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Order line number that need to track agaist the SO',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OnHand_Qty` decimal(24,7) DEFAULT NULL COMMENT 'On hand Quantity',
  `Obsolescence_Flag` bit(1) DEFAULT NULL COMMENT 'Obsolescence details',
  `StockOut` bit(1) DEFAULT NULL COMMENT 'Is stock available or not',
  `Unit_Price` decimal(24,12) DEFAULT NULL COMMENT 'Cost per Unit to identify the inventory Value',
  `UOM_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Unit of measurment(UOM) that need to identify the Case or Box details.',
  `Material_Diverted_Flag` bit(1) DEFAULT NULL COMMENT 'If Product got diverted or',
  `Base_Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,12) DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Inventory_Summary_Stg`
--

DROP TABLE IF EXISTS `IL_Inventory_Summary_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Inventory_Summary_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Inventory_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Inventroy_Process_Date` datetime DEFAULT NULL,
  `Inventory_Qty` decimal(24,7) DEFAULT NULL,
  `On_Hand_Qty` decimal(24,7) DEFAULT NULL,
  `Allocated_Qty` decimal(24,7) DEFAULT NULL,
  `Reserved_Qty` decimal(24,7) DEFAULT NULL,
  `Back_Order_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `Inventory_Value` decimal(18,2) DEFAULT NULL,
  `Last_Ship_Date` datetime DEFAULT NULL,
  `Inventory_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Inventory_Transaction_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Inventory_Transaction_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Inventory_Transaction_Dtl_Stg` (
  `Transaction_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Transaction_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Inventory_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Transaction_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Transaction_Date` datetime DEFAULT NULL,
  `Transaction_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Location_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Fiscal_Period` int(11) DEFAULT NULL,
  `Batch_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Op_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_LineNumber` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Recieve_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Receive_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Packing_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Packing_Line_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vocher_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Item_Extension_No` bigint(20) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Posted_Date` datetime DEFAULT NULL,
  `Posted_Flag` bit(1) DEFAULT NULL,
  `Lot_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Expected_Move_Date` datetime DEFAULT NULL,
  `Plan_Sale_Date` datetime DEFAULT NULL,
  `Expiration_Date` datetime DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Inventory_Qty` decimal(24,7) DEFAULT NULL,
  `On_Hand_Qty` decimal(24,7) DEFAULT NULL,
  `Back_Order_Qty` decimal(24,7) DEFAULT NULL,
  `Allocated_Qty` decimal(24,7) DEFAULT NULL,
  `Reserved_Qty` decimal(24,7) DEFAULT NULL,
  `Inspection_Qty` decimal(24,7) DEFAULT NULL,
  `Outbound_Qty` decimal(24,7) DEFAULT NULL,
  `Inbound_Qty` decimal(24,7) DEFAULT NULL,
  `Safety_Level_Qty` decimal(24,7) DEFAULT NULL,
  `Scrapped_Qty` decimal(24,7) DEFAULT NULL,
  `Extended_Price` decimal(24,7) DEFAULT NULL,
  `Burden_Cost` decimal(24,7) DEFAULT NULL,
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Material_Cost` decimal(24,7) DEFAULT NULL,
  `Misc_Cost` decimal(24,7) DEFAULT NULL,
  `Scrap_Cost` decimal(24,7) DEFAULT NULL,
  `Transaction_Cost` decimal(24,7) DEFAULT NULL,
  `Transaction_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Safety_Level_Flag` bit(1) DEFAULT NULL,
  `Cycle_Count_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Stocking_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Policy` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Planning_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Inventory_Mode` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Action_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Buyer_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Container_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Inventory_Transfer_Stg`
--

DROP TABLE IF EXISTS `IL_Inventory_Transfer_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Inventory_Transfer_Stg` (
  `Transaction_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Transaction_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Transfer_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Transaction_Date` datetime DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `From_Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `From_Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `From_Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `From_Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `From_Location_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `From_Qty` decimal(24,7) DEFAULT NULL,
  `From_Unit_Price` decimal(18,2) DEFAULT NULL,
  `From_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `From_Inv_Transaction_Value` decimal(18,2) DEFAULT NULL,
  `To_Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `To_Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `To_Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `To_Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `To_Location_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `To_Qty` decimal(24,7) DEFAULT NULL,
  `To_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `To_Unit_Price` decimal(18,2) DEFAULT NULL,
  `To_Inv_Transaction_Value` decimal(18,2) DEFAULT NULL,
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transfer_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,12) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Invoice_Hdr_Stg`
--

DROP TABLE IF EXISTS `IL_Invoice_Hdr_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Invoice_Hdr_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipment_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipment_Date` datetime DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL,
  `Due_Date` datetime DEFAULT NULL,
  `Posted_Date` datetime DEFAULT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Fiscal_Period` int(11) DEFAULT NULL,
  `Sent_Date` datetime DEFAULT NULL,
  `GL_Posted_Date` datetime DEFAULT NULL,
  `Paid_Date` datetime DEFAULT NULL,
  `Cancelled_Date` datetime DEFAULT NULL,
  `Recorded_Date` datetime DEFAULT NULL,
  `Invoice_Amount` decimal(24,7) DEFAULT NULL,
  `Misc_Amount` decimal(24,7) DEFAULT NULL,
  `Discount_Amount` decimal(24,7) DEFAULT NULL,
  `Total_Invoice_Amount` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Non_Invoice_Flag` bit(1) DEFAULT NULL,
  `Posted_Flag` bit(1) DEFAULT NULL,
  `Invoice_Comment` varchar(8000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Invoice_Stg`
--

DROP TABLE IF EXISTS `IL_Invoice_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Invoice_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Id details',
  `Invoice_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Invoice number uniquely genereted for Order',
  `Invoice_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Invoice Line number for Each product that invoiced',
  `Invoice_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'unknown' COMMENT 'Type of the invoice',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Invoice_Date` datetime DEFAULT NULL COMMENT 'Date  of invoice generated',
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order number of that invoice processed',
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order Line number that invoice processed',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant or Bussiness unit of the invoice generated',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the product that Order executed',
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer code or Id that Invoice Generated',
  `Order_Rel_Num` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Order Release number details',
  `Order_Quantity` decimal(24,7) DEFAULT NULL COMMENT 'Total Qty orderd by the customer',
  `Shipped_Date` datetime DEFAULT NULL COMMENT 'Date that order got shipped',
  `Shipped_Quantity` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Invoice_Status` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the invoice',
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ware house or branch that order picked up',
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Posted_Date` datetime DEFAULT NULL,
  `Sent_Date` datetime DEFAULT NULL,
  `Recorded_Date` datetime DEFAULT NULL,
  `Due_Date` datetime DEFAULT NULL,
  `Ship_Date` datetime DEFAULT NULL,
  `Paid_Date` datetime DEFAULT NULL,
  `GL_Posted_Date` datetime DEFAULT NULL,
  `Invoice_Cancelled_Date` datetime DEFAULT NULL,
  `Non_Invoice_Flag` bit(1) DEFAULT NULL,
  `Posted_Flag` bit(1) DEFAULT NULL,
  `Posting_Hold_Flag` bit(1) DEFAULT NULL,
  `Fiscal_Period` int(11) DEFAULT NULL,
  `Fiscal_Year` int(11) DEFAULT NULL,
  `Misc_Amount` decimal(18,2) DEFAULT NULL,
  `Invoice_Balance` decimal(18,2) DEFAULT NULL,
  `Unposted_Balance` decimal(18,2) DEFAULT NULL,
  `Invoice_Line_Amount` decimal(18,2) DEFAULT NULL,
  `Invoice_Amount` decimal(24,7) DEFAULT NULL,
  `Tax_Amount` decimal(24,7) DEFAULT NULL,
  `Discount_Amount` decimal(24,7) DEFAULT NULL,
  `Total_Amount` decimal(24,7) DEFAULT NULL,
  `Distribution_channel` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Distribution channel details',
  `Sales_RespCode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Representive Code that order booked',
  `Invoice_Comment` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Any comments for that invoice',
  `Delivery_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_Country_Code` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_State_Code` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Foreign_Curr_Invoice_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base Currency code that Invoice genrated',
  `Transaction_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction currency code that need to convert with base currency',
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Advance_Inv_Flag` bit(1) DEFAULT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Line_Number` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Type` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Item_Stg`
--

DROP TABLE IF EXISTS `IL_Item_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Item_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Bar_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Alternate_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Group_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Tag_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Category_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Price_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Standard_Cost` decimal(24,7) DEFAULT NULL,
  `Stock_Count` bigint(20) DEFAULT NULL,
  `Default_TaxRates_Flag` bit(1) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_JobMtl_Stg`
--

DROP TABLE IF EXISTS `IL_JobMtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_JobMtl_Stg` (
  `Job_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Job or Work Order number',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Id of the plant that Job estimation started',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Plant details.',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Product Id of the Job Material estimated.',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Requested_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Total requested quantity',
  `Estimated_Scrap` decimal(18,2) DEFAULT NULL COMMENT 'Discarded or removed from service product qty',
  `Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Currency Code that Estimation cost defined.',
  `Est_Unit_Cost` decimal(18,2) DEFAULT NULL COMMENT 'Estimated unit cost',
  `Est_Mtl_Unit_Cost` decimal(18,2) DEFAULT NULL COMMENT 'Estimated metirial cost',
  `Est_Bur_Unit_Cost` decimal(18,2) DEFAULT NULL COMMENT 'Estimatied Burnt cost',
  `Est_Labour_Unit_Cost` decimal(18,2) DEFAULT NULL COMMENT 'Estimated Labour Cost',
  `Est_Sub_Unit_Cost` decimal(18,2) DEFAULT NULL COMMENT 'Estimated Sub unit cost',
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Job_Component_Stg`
--

DROP TABLE IF EXISTS `IL_Job_Component_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Job_Component_Stg` (
  `Job_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Job_Description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Part_No` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cust_Order_No` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Commision_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Drawing_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Drawing_Issue_Flag` bit(1) DEFAULT NULL,
  `Group_No` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_No` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Date` datetime DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Delivery_Date` datetime DEFAULT NULL,
  `Completed_Date` datetime DEFAULT NULL,
  `Promised_Date` datetime DEFAULT NULL,
  `Job_Start_Time` datetime DEFAULT NULL,
  `Job_End_Time` datetime DEFAULT NULL,
  `Quality_Inspection_Flag` bit(1) DEFAULT NULL,
  `Est_Cost` decimal(24,7) DEFAULT NULL,
  `Total_Cost` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Rust_Cost` decimal(24,7) DEFAULT NULL,
  `Cust_Material_Info` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cust_Job_No_Info` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cust_Characteristic` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Production_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Ind` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Job_Detail_Stg`
--

DROP TABLE IF EXISTS `IL_Job_Detail_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Job_Detail_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company details that Plant located',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Bussiness Unit or Plant that WO available',
  `Job_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Job Number or WO that Product Manufacturing',
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Shopfloor details that WO.',
  `Machine_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Start_Date` datetime DEFAULT NULL,
  `Completion_Date` datetime DEFAULT NULL,
  `Input_Qty` decimal(24,7) DEFAULT NULL,
  `Produced_Qty` decimal(24,7) DEFAULT NULL,
  `Percent_Of_Scrap` decimal(18,2) DEFAULT NULL,
  `Percent_Of_Yeild` decimal(18,2) DEFAULT NULL,
  `Job_Exce_Seq` bigint(20) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Job_Operation_Log_Stg`
--

DROP TABLE IF EXISTS `IL_Job_Operation_Log_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Job_Operation_Log_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `OP_Log_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Op_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Log_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Machine_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Machine_Log_DateTime` datetime DEFAULT NULL,
  `Actual_Time` decimal(18,2) DEFAULT NULL,
  `Machine_Log_Time_Sec` decimal(24,2) DEFAULT NULL,
  `Clocked_Time_Sec` decimal(24,2) DEFAULT NULL,
  `OP_Log_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Actual_Qty` decimal(24,7) DEFAULT NULL,
  `Booked_Qty` decimal(24,6) DEFAULT NULL,
  `Rejected_Qty` decimal(24,6) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Job_Operation_Stg`
--

DROP TABLE IF EXISTS `IL_Job_Operation_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Job_Operation_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Job_Op_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Sequence_No` bigint(20) DEFAULT NULL,
  `Operation_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Operation_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Planned_Machine_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Actual_Machine_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Operation_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Time_Per_Piece` decimal(18,2) DEFAULT NULL,
  `Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Planned_Time` decimal(18,2) DEFAULT NULL,
  `Schedule_Start_Date` datetime DEFAULT NULL,
  `Schedule_End_Date` datetime DEFAULT NULL,
  `Schedule_Start_Time_Sec` decimal(24,2) DEFAULT NULL,
  `Schedule_End_Time_Sec` decimal(24,2) DEFAULT NULL,
  `Schedule_Start_DateTime` datetime DEFAULT NULL,
  `Schedule_End_DatetTime` datetime DEFAULT NULL,
  `Actual_Qty` decimal(24,7) DEFAULT NULL,
  `Actual_Time` decimal(24,2) DEFAULT NULL,
  `Setup_Time` decimal(24,2) DEFAULT NULL,
  `Waiting_Time` decimal(24,2) DEFAULT NULL,
  `Completed_Date` datetime DEFAULT NULL,
  `Estimated_Time` decimal(24,2) DEFAULT NULL,
  `WIP_Value` decimal(24,6) DEFAULT NULL,
  `Sales_Order_Value` decimal(24,6) DEFAULT NULL,
  `Job_Cost` decimal(24,6) DEFAULT NULL,
  `Live_Op_Flag` bit(1) DEFAULT NULL,
  `Material_Missing_Flag` bit(1) DEFAULT NULL,
  `Material_Missing_Start_Date` datetime DEFAULT NULL,
  `Material_Missing_End_Date` datetime DEFAULT NULL,
  `Material_Missing_Reason` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Preperation_Missing_Flag` bit(1) DEFAULT NULL,
  `Preperation_Missing_Start_Date` datetime DEFAULT NULL,
  `Preperation_Missing_End_Date` datetime DEFAULT NULL,
  `Prep_Missing_Reason` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Tool_Missing_Flag` bit(1) DEFAULT NULL,
  `Tool_Missing_Start_Date` datetime DEFAULT NULL,
  `Tool_Missing_End_Date` datetime DEFAULT NULL,
  `Tool_Missing_Reason` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ext_Compnt_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OP_Info` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OVERLAP_Stage` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OVERLAP_Percentange` decimal(18,2) DEFAULT NULL,
  `Rework_Flag` bit(1) DEFAULT NULL,
  `Rework_Time_InSec` decimal(24,7) DEFAULT NULL,
  `Rework_Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Rework_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Production_Unit_Cost` decimal(24,7) DEFAULT NULL,
  `Estimated_Unit_Cost` decimal(24,7) DEFAULT NULL,
  `Est_Sub_Unit_Cost` decimal(24,7) DEFAULT NULL,
  `Sub_Unit_Cost` decimal(24,7) DEFAULT NULL,
  `Est_Scrap_Qty` decimal(24,7) DEFAULT NULL,
  `Scrapt_Qty` decimal(24,7) DEFAULT NULL,
  `Scrap_Amount` decimal(24,7) DEFAULT NULL,
  `Est_Mtl_Unit_Cost` decimal(24,7) DEFAULT NULL,
  `Est_Bur_Unit_Cost` decimal(24,7) DEFAULT NULL,
  `Est_Labour_Unit_Cost` decimal(24,7) DEFAULT NULL,
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Operate_Cost` decimal(24,7) DEFAULT NULL,
  `Burden_Cost` decimal(24,7) DEFAULT NULL,
  `Material_Cost` decimal(24,7) DEFAULT NULL,
  `Misc_Cost` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Job_Stg`
--

DROP TABLE IF EXISTS `IL_Job_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Job_Stg` (
  `Job_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Uniquely identified  Work Order Code or Job Number',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Code',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Job_Description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of  the job',
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant or Bussiness Unit job allocated',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product Details of the Job /Work Order Allowcated',
  `Sales_Order_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Order Number',
  `Sales_Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Order Line Number that SO created for Product',
  `Quote_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipped_date` datetime DEFAULT NULL,
  `Unit_Of_Measure` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Unit of Measure of the Quantity',
  `Requested_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Total Requested quantity of the product for sales order',
  `Completed_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Total completed quantity of the product for sales order',
  `Start_Date` datetime DEFAULT NULL COMMENT 'Start date of the Job',
  `Job_Requested_Date` datetime DEFAULT NULL COMMENT 'Requested Job due date',
  `Job_Due_Date` datetime DEFAULT NULL COMMENT 'Job due date',
  `Job_Completion_Date` datetime DEFAULT NULL COMMENT 'Job completion date',
  `Job_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of the Job',
  `Job_Status_Code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the Job or WO',
  `Order_Release_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Order Release number details',
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Ware house Id where the Product is stored',
  `Product_Unit_Price` decimal(24,7) DEFAULT NULL,
  `Job_Cost` decimal(24,7) DEFAULT NULL,
  `Base_Curr_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Cur_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Rework_Flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Lead_Account_Stg`
--

DROP TABLE IF EXISTS `IL_Lead_Account_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Lead_Account_Stg` (
  `Account_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Account_Names` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `No_Of_Employees` bigint(20) DEFAULT NULL,
  `Account_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Industry_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Billing_Street` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Billing_City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Billing_State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Billing_Postal_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Billing_Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipping_Street` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipping_City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipping_State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipping_Postal_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipping_Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Website_Link` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` tinyint(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Update_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Lead_Stg`
--

DROP TABLE IF EXISTS `IL_Lead_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Lead_Stg` (
  `Lead_Id` bigint(20) NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Salutation` int(10) DEFAULT NULL,
  `Lead_Full_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Designation` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lead_Source_Link` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Street_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lead_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lead_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Assign_User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lead_Popularity` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `No_Of_Employees` bigint(20) DEFAULT NULL,
  `Converted_Flag` bit(1) DEFAULT NULL,
  `Converted_Date` datetime DEFAULT NULL,
  `Converted_Account_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Converted_Contact_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Converted_Opportunity_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Assigned_Flag` bit(1) DEFAULT NULL,
  `Stage_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Revenue_Size` decimal(24,7) DEFAULT NULL,
  `Exit_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exit_Reason_Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Department_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ERP_Used` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ERP_Version_Used` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Region` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Mobile_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Bounce_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Bounce_Date` datetime DEFAULT NULL,
  `Website_Link` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDatetime01` datetime DEFAULT NULL,
  `Bboolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Logistics_Stg`
--

DROP TABLE IF EXISTS `IL_Logistics_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Logistics_Stg` (
  `Logistic_Id` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Uniquely identifed Logistic Number',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Sales Ordered Company Id',
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'SO line Number',
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'SO Number',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant Id of the SO That recived the Order',
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Warehouse or Product Line  Details where Product picked up.',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product that picked up and delivered to Customer or distributer',
  `Track_Number` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Track Number of Shipping Goods',
  `Primary_Srvc_Provider_Id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Primary Service provider or Supplier details to shipp the product to Customer',
  `Delivery_Status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the Delivery',
  `Shipping_Type` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of Shippement Ex: Regular or Premium',
  `PickedUp_Date` datetime DEFAULT NULL COMMENT 'Date of the good picked up from Warehouse/Branch',
  `Due_Date` datetime DEFAULT NULL COMMENT 'Due Date to deliver it to customer',
  `Delivered_Date` datetime DEFAULT NULL COMMENT 'Date of delivery to Customer',
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Base_Price` decimal(24,7) DEFAULT NULL,
  `PickedUp_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Goods or product picked up Quantity',
  `Delivered_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Goods or product Delivered Quantity',
  `Logistic_Cost` decimal(18,2) DEFAULT NULL COMMENT 'Total logistic cost for the Goods or Product',
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Lost_Sales_Dtl`
--

DROP TABLE IF EXISTS `IL_Lost_Sales_Dtl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Lost_Sales_Dtl` (
  `Lost_Sales_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Lost_Sales_Line_Id` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Lost_Sales_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Lost_Sales_Dtl_Key` bigint(20) NOT NULL AUTO_INCREMENT,
  `Lost_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lost_Sales_Date` datetime DEFAULT NULL,
  `Order_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_LineNumber` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Price` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Planned_Date` datetime DEFAULT NULL,
  `Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Pending_Qty` decimal(24,7) DEFAULT NULL,
  `Lost_Qty` decimal(24,7) DEFAULT NULL,
  `Pending_Amount` decimal(24,7) DEFAULT NULL,
  `Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Comments` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Stock_Out_Flag` bit(1) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Lost_Sales_Id`,`Lost_Sales_Line_Id`,`Lost_Sales_Type`,`Company_Id`,`DataSource_Id`),
  KEY `IL_Lost_Sales_Dtl_idx_key` (`Lost_Sales_Dtl_Key`),
  KEY `IL_Lost_Sales_Dtl_FKIndex1` (`Sales_Rep_Code`,`Company_Id`,`DataSource_Id`),
  KEY `IL_Lost_Sales_Dtl_FKIndex2` (`Company_Id`,`DataSource_Id`),
  KEY `IL_Lost_Sales_Dtl_FKIndex3` (`Warehouse_ID`,`Company_Id`,`DataSource_Id`),
  KEY `IL_Lost_Sales_Dtl_FKIndex4` (`Plant_Id`,`Company_Id`,`DataSource_Id`),
  KEY `IL_Lost_Sales_Dtl_FKIndex5` (`UOM_Id`,`DataSource_Id`),
  KEY `IL_Lost_Sales_Dtl_FKIndex6` (`Product_Id`,`Company_Id`,`DataSource_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_MPS_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_MPS_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_MPS_Dtl_Stg` (
  `Plan_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plan_Order_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `MPS_Order_Seq` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `MPS_Plan_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plan_Date` datetime DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Planned_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Planned_Priority` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MPS_Cost` decimal(24,7) DEFAULT NULL,
  `Quote_Flag` bit(1) DEFAULT NULL,
  `Quote_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Line_Number` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_MRP_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_MRP_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_MRP_Dtl_Stg` (
  `Plan_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plan_Line_Number` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `Plan_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `MRP_Date` datetime DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Machine_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Raw_Material_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Requested_Qty` decimal(24,7) DEFAULT NULL,
  `PO_Qty` decimal(24,7) DEFAULT NULL,
  `Estimated_Scrap` decimal(24,7) DEFAULT NULL,
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Misc_Cost` decimal(24,7) DEFAULT NULL,
  `Raw_Material_Cost` decimal(24,7) DEFAULT NULL,
  `Total_Cost` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Machine_Capacity_Stg`
--

DROP TABLE IF EXISTS `IL_Machine_Capacity_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Machine_Capacity_Stg` (
  `Machine_Cap_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Capacity_Date` datetime DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Machine_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Capacity_InSec` decimal(24,7) DEFAULT NULL,
  `Min_Capacity_InSec` decimal(24,7) DEFAULT NULL,
  `Max_Capacity_InSec` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Machine_Maintenance_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Machine_Maintenance_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Machine_Maintenance_Dtl_Stg` (
  `Machine_Maintenance_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Maintenance_Type` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Shift_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Maintenance_Date` datetime DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Machine_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Breakdown_Start_Date` datetime DEFAULT NULL,
  `Breakdown_End_Date` datetime DEFAULT NULL,
  `Break_Down_Time_InSec` decimal(24,7) DEFAULT NULL,
  `Break_Down_Cost` decimal(24,7) DEFAULT NULL,
  `Shutdown_Start_Date` datetime DEFAULT NULL,
  `Shutdown_End_Date` datetime DEFAULT NULL,
  `Shutdown_Time_InSec` decimal(24,7) DEFAULT NULL,
  `Shutdown_Cost` decimal(24,7) DEFAULT NULL,
  `Maintenance_Cost` decimal(24,7) DEFAULT NULL,
  `Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plan_Flag` bit(1) DEFAULT NULL,
  `Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Machine_Stg`
--

DROP TABLE IF EXISTS `IL_Machine_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Machine_Stg` (
  `Machine_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source Resource Id or resource Code',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company name that machine located',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Machine_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Resource Name from source system',
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Store or Plant id location resource working for',
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shopfloor or Production line that Machine available',
  `Production_Hours` float DEFAULT NULL,
  `Production_Capacity` float DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `IsActive` bit(1) DEFAULT NULL COMMENT '0 for active record and 1 for inactive record.',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Menu_Time_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Menu_Time_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Menu_Time_Type_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Menu_Time_Type_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Time_Type_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Time_Type_Short_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Merchant_Stg`
--

DROP TABLE IF EXISTS `IL_Merchant_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Merchant_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Merchant_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Default_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address1` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address2` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address3` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Website_Link` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IsBillable_Flag` bit(1) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Note_Stg`
--

DROP TABLE IF EXISTS `IL_Note_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Note_Stg` (
  `Note_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Deal_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Person_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Organization_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Note_Content` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Pinned_To_Deal_Flag` bit(1) DEFAULT NULL,
  `Pinned_To_Person_Flag` bit(1) DEFAULT NULL,
  `Pinned_To_Organization_Flag` bit(1) DEFAULT NULL,
  `Organization_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Update_User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Opportunity_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Opportunity_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Opportunity_Dtl_Stg` (
  `Opp_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Opp_Date` datetime DEFAULT NULL,
  `Opp_Due_Date` datetime DEFAULT NULL,
  `Opp_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Opp_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Opp_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Opp_Status_Stage` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Campaign_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Campaign_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Price_Book_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Opp_Amount` decimal(24,7) DEFAULT NULL,
  `Probability_InPerc` decimal(24,7) DEFAULT NULL,
  `Remarks` varchar(8000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lead_Link` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Closed_Flag` bit(1) DEFAULT NULL,
  `Won_Flag` bit(1) DEFAULT NULL,
  `Opp_LineItem_Flag` bit(1) DEFAULT NULL,
  `Budget_Confirmed_Flag` bit(1) DEFAULT NULL,
  `Discovery_Completed_Flag` bit(1) DEFAULT NULL,
  `Forecast_Category` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Forecast_Category_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fiscal_Quarter` bigint(20) DEFAULT NULL,
  `Fiscal_Year` bigint(20) DEFAULT NULL,
  `No_Of_Users` bigint(20) DEFAULT NULL,
  `Exit_Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exit_Reason_Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Used_ERP` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Used_ERP_Version` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Generated_Flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` tinyint(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Order_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Order_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Order_Dtl_Stg` (
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Unique sales order identification number in source.',
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order Serial number for multiple order else default 1',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company of the SO placed',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Type of the Order',
  `Plant_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant id that order got Allocated',
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Warehouse details that stock are available.',
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer Code of the SO.',
  `Cust_Ship_Addr_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Shipment address of the Customer',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product Code / Item code details.',
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ordered_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Quantity ordered per product.',
  `Unit_Price` decimal(18,2) DEFAULT NULL COMMENT 'Selling price of a single product.',
  `Unit_Price_Discount` decimal(24,7) DEFAULT NULL COMMENT 'Discounted cost price per unit for the Product',
  `Order_Line_Amount` decimal(24,7) DEFAULT NULL COMMENT 'Unit Price * Order Qty',
  `Order_Line_Discount_Amount` decimal(24,7) DEFAULT NULL COMMENT 'Unit Price Discount * Order Qty',
  `Order_Line_Total_Amount` decimal(24,7) DEFAULT NULL COMMENT 'Total Order Amount After discount  Order amount - Order discount amount',
  `Order_Line_Status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the Order',
  `Ordered_Date` datetime DEFAULT NULL COMMENT 'Ordered Date',
  `Shipped_Date` datetime DEFAULT NULL COMMENT 'Shippment date for the order',
  `Due_Date` datetime DEFAULT NULL COMMENT 'Expected to delivery date to customer',
  `Quote_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Category_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_Country_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_State_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Misc_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Price` decimal(18,2) DEFAULT NULL,
  `Avg_Price` decimal(18,2) DEFAULT NULL,
  `Last_Price` decimal(18,2) DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL COMMENT 'Requested Date of SO',
  `Shipped_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Shipped quantity for the order',
  `Cancelled_Date` datetime DEFAULT NULL COMMENT 'Order Cancel Date',
  `Cancelled_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Number of Qty Cancelled',
  `Retruned_Date` datetime DEFAULT NULL COMMENT 'Date of the product Returned.',
  `Returned_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Retruned Quantity of the Product',
  `Back_Order_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Back Order Qty',
  `On_Hold_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Hold Qty of the Product',
  `Open_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Order Line Open Quantity of the Product',
  `MTS_Flag` bit(1) DEFAULT NULL COMMENT 'To Identify the MTS and MTO SO details',
  `Transport_Mode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Mode of the Transport',
  `Container_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Container details',
  `Reason_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Reason Code for Hold or Cancel the order.',
  `Transaction_Cur_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tranaction Currency  Code of the SO',
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Base_Curr_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base Currency Code of the SO',
  `Gross_Weight` decimal(18,5) DEFAULT NULL COMMENT 'Weight of the Product in Gross',
  `Measure_Weight` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'UOM of the Weight',
  `EDI_Order_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Electronic Data Interchange Order Number',
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Order_Hdr_Stg`
--

DROP TABLE IF EXISTS `IL_Order_Hdr_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Order_Hdr_Stg` (
  `Order_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Unique sales order identification number in source.',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company code of the Order received',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of the SO',
  `Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of the Currency Transaction done.',
  `Order_Status` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the Order',
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant details that SO is allocated',
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales representive Code of the Order booked',
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer Code of the SO',
  `Delivery_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_Country_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_State_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contact_Date` datetime DEFAULT NULL COMMENT 'Customer Enquired Date.',
  `Requested_Date` datetime DEFAULT NULL COMMENT 'Date of the Order Requested',
  `Order_Date` datetime DEFAULT NULL COMMENT 'Dates the sales order was created.',
  `Due_Date` datetime DEFAULT NULL COMMENT 'Expected to delivery date to customer',
  `Cancelled_Date` datetime DEFAULT NULL COMMENT 'Cancell date of the Order',
  `Total_Order_Amount` decimal(18,2) DEFAULT NULL COMMENT 'Sales order Amount',
  `Total_Discount` decimal(18,2) DEFAULT NULL COMMENT 'Total discount amount',
  `EDI_Order_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Electronic Data Interchange Order number',
  `Online_Order_Flag` bit(1) DEFAULT NULL COMMENT '0 = Order placed by sales person. 1 = Order placed online by customer.',
  `Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Comments made by the sales representative or Customer',
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Order_LineItem_Stg`
--

DROP TABLE IF EXISTS `IL_Order_LineItem_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Order_LineItem_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `LineItem_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `OrderTypes_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Alternate_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Qty` decimal(24,7) DEFAULT NULL,
  `Order_Date` datetime DEFAULT NULL,
  `Cancelled_Date` datetime DEFAULT NULL,
  `Exchange_Date` datetime DEFAULT NULL,
  `Refund_Date` datetime DEFAULT NULL,
  `Last_Payment_Date` datetime DEFAULT NULL,
  `Delivery_date` datetime DEFAULT NULL,
  `Delivery_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `Order_Line_amount` decimal(18,2) DEFAULT NULL,
  `Discount_Amount` decimal(18,2) DEFAULT NULL,
  `Discount_Percentage` bigint(20) DEFAULT NULL,
  `ServiceCharge_Amount` decimal(18,2) DEFAULT NULL,
  `Refund_Amount` decimal(18,2) DEFAULT NULL,
  `Payment_amount` decimal(24,7) DEFAULT NULL,
  `Delivery_Amount` decimal(24,7) DEFAULT NULL,
  `TaxRate` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `IsRevenue_Flag` bit(1) DEFAULT NULL,
  `Exchanged_flag` bit(1) DEFAULT NULL,
  `Refunded_flag` bit(1) DEFAULT NULL,
  `Cancelled_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Void_Created_Employee_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Void_Removed_Employee_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Discount_Type_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Comments` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Order_Refunds_Stg`
--

DROP TABLE IF EXISTS `IL_Order_Refunds_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Order_Refunds_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Refunds_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `LineItem_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ServiceCharge_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Device_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Tender_id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Amount` bigint(20) DEFAULT NULL,
  `Refund_Date` datetime DEFAULT NULL,
  `Tax_Amount` bigint(20) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Order_Release_Stg`
--

DROP TABLE IF EXISTS `IL_Order_Release_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Order_Release_Stg` (
  `Order_Rel_Num` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source system Order release number',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company name that SO released',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order number for release',
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order line number for release',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product that ordered for release',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Release_Date` datetime DEFAULT NULL COMMENT 'Date of release the product',
  `Release_Qty` bigint(20) DEFAULT NULL COMMENT 'Number of quantity released',
  `Release_Status` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Release status',
  `MTS_Flag` bit(1) DEFAULT NULL COMMENT 'To identfiy the SO is for Make To Stock or Make to Order',
  `Insured_Flg` bit(1) DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `Rel_Handling_Amount` decimal(18,2) DEFAULT NULL,
  `Insured_Amt` decimal(18,2) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Order_Status_Stg`
--

DROP TABLE IF EXISTS `IL_Order_Status_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Order_Status_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Sales_Order_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Sales_Order_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Date` datetime DEFAULT NULL,
  `Due_Date` datetime DEFAULT NULL,
  `Order_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Qty` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `Shipped_Date` datetime DEFAULT NULL,
  `Shipped_Qty` decimal(24,7) DEFAULT NULL,
  `Pick_Qty` decimal(24,7) DEFAULT NULL,
  `Staged_Qty` decimal(24,7) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Order_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Order_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Order_Type_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `OrderTypes_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `OrderType_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OrderType_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Distance_Radius` bigint(20) DEFAULT NULL,
  `Taxable_Flag` bit(1) DEFAULT NULL,
  `Misc_Charges` decimal(24,7) DEFAULT NULL,
  `Avg_Order_Time` bigint(20) DEFAULT NULL,
  `MinOrder_Amount` decimal(24,7) DEFAULT NULL,
  `MaxOrder_Amount` decimal(24,7) DEFAULT NULL,
  `IsDeleted_Flag` bit(1) DEFAULT NULL,
  `Availability_Check` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Orders_Item_Stg`
--

DROP TABLE IF EXISTS `IL_Orders_Item_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Orders_Item_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `OrderTypes_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customer_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Device_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Date` datetime DEFAULT NULL,
  `Cancelled_Date` datetime DEFAULT NULL,
  `Total_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Comments` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IsVat_flag` bit(1) DEFAULT NULL,
  `Manual_Transaction_Flag` bit(1) DEFAULT NULL,
  `TestMode_Flag` bit(1) DEFAULT NULL,
  `TaxRemoved_Flag` bit(1) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Organization_Stg`
--

DROP TABLE IF EXISTS `IL_Organization_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Organization_Stg` (
  `Organization_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Organization_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Open_deals_Count` bigint(20) DEFAULT NULL,
  `Related_Open_Deals_Count` bigint(20) DEFAULT NULL,
  `Closed_Deals_Count` bigint(20) DEFAULT NULL,
  `Related_Closed_Deals_Count` bigint(20) DEFAULT NULL,
  `Email_Messages_Count` bigint(20) DEFAULT NULL,
  `People_Count` bigint(20) DEFAULT NULL,
  `Activities_Count` bigint(20) DEFAULT NULL,
  `Undone_Activities_Count` bigint(20) DEFAULT NULL,
  `Reference_Activities_Count` bigint(20) DEFAULT NULL,
  `Files_Count` bigint(20) DEFAULT NULL,
  `Notes_Count` bigint(20) DEFAULT NULL,
  `Followers_Count` bigint(20) DEFAULT NULL,
  `Won_Deals_Count` bigint(20) DEFAULT NULL,
  `Related_Won_Deals_Count` bigint(20) DEFAULT NULL,
  `Lost_Deals_Count` bigint(20) DEFAULT NULL,
  `Related_Lost_Deals_Count` bigint(20) DEFAULT NULL,
  `Next_Activity_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Activity_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Next_Activity_Date` datetime DEFAULT NULL,
  `Next_Activity_Time` datetime DEFAULT NULL,
  `Last_Activity_Date` datetime DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Category_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Address` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address_Subpremise` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Street_No` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address_Route` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address_Admin_Area_Level_1` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address_Admin_Area_Level_2` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address_Formatted_Address` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Locality` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Locality` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country_Code` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_PO_Detail_Stg`
--

DROP TABLE IF EXISTS `IL_PO_Detail_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_PO_Detail_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Line_Descr1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Line_Descr2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Line_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Next_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Last_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Purchase_Date` datetime DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Due_Date` datetime DEFAULT NULL,
  `Order_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `PO_Amount` decimal(24,7) DEFAULT NULL,
  `Cancelled_Date` datetime DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `OnHold_Qty` decimal(24,7) DEFAULT NULL,
  `Open_Qty` decimal(24,7) DEFAULT NULL,
  `Received_Qty` decimal(24,7) DEFAULT NULL,
  `Cancelled_Qty` decimal(24,7) DEFAULT NULL,
  `Buyer_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transport_Mode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Gross_Weight` decimal(24,7) DEFAULT NULL,
  `GrossWeight_UOM` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Reason_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Material_category` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Curr_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,10) DEFAULT NULL,
  `Container_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Detail_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Entry_Person` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Misc_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Price` decimal(18,2) DEFAULT NULL,
  `Avg_Price` decimal(18,2) DEFAULT NULL,
  `Last_Price` decimal(18,2) DEFAULT NULL,
  `Pre_Payment_Flag` bit(1) DEFAULT NULL,
  `Extended_Amount` decimal(24,7) DEFAULT NULL,
  `Discount_Flag` bit(1) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_PO_Header_Stg`
--

DROP TABLE IF EXISTS `IL_PO_Header_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_PO_Header_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company ID that Purchase the Order',
  `PO_Number` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Purchase order number that uniquely identified for PO',
  `PO_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Type of Purchase Order',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant Id or Bussiness unit of the PO',
  `Job_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Job Number or WO Number that PO initiated',
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Supplier of the product to the Plant',
  `Buyer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Buyer id from the source system',
  `Requested_Date` datetime DEFAULT NULL COMMENT 'Requested Date of the Product',
  `Due_Date` datetime DEFAULT NULL COMMENT 'Expected to Deliver Date',
  `PO_Date` datetime DEFAULT NULL COMMENT 'Purchase order Date',
  `Cancelled_Date` datetime DEFAULT NULL COMMENT 'PO Cancel Date',
  `Hold_Order_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'If any PO is on Hold need to provide the Reason Code.',
  `PO_Amount` decimal(18,2) DEFAULT NULL COMMENT 'Purchase order Amount',
  `Base_Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base Currency that need to convert',
  `Trasnsaction_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction currency Code that currency transaction done with Supplier',
  `Exchange_Rate` decimal(24,12) DEFAULT NULL COMMENT 'Exchage rate of the Currency between Base currency and Transaction currency',
  `PO_Approved_Flag` bit(1) DEFAULT NULL COMMENT 'Purchase order Approvall',
  `PO_Status` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the PO',
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_PO_PutAway_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_PO_PutAway_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_PO_PutAway_Dtl_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PutAway_Transaction_No` bigint(20) NOT NULL,
  `PO_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Sequence_Number` bigint(20) DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PutAway_Zone` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Container_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PutAway_Requested_Date` datetime DEFAULT NULL,
  `PutAway_Date` datetime DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PutAway_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Max_PutAway_Qty` decimal(24,7) DEFAULT NULL,
  `PutAway_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PutAway_Qty` decimal(24,7) DEFAULT NULL,
  `Open_Qty` decimal(24,7) DEFAULT NULL,
  `Closed_Qty` decimal(24,7) DEFAULT NULL,
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Resource_Worked_Hours` decimal(18,2) DEFAULT NULL,
  `Labour_Amount` decimal(18,2) DEFAULT NULL,
  `PutAway_Amount` decimal(18,2) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_PO_Receive_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_PO_Receive_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_PO_Receive_Dtl_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Receive_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Receive_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Received_Date` datetime DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipment_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Load_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Container_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Ordered_Date` datetime DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `PO_Due_Date` datetime DEFAULT NULL,
  `Schedule_Pick_Date` datetime DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL,
  `Supplier_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Line_Num` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Item_Extension_No` bigint(20) DEFAULT NULL,
  `Unit_price` decimal(18,2) DEFAULT NULL,
  `Payment_Terms_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Order_Qty` decimal(24,7) DEFAULT NULL,
  `Open_Qty` decimal(24,7) DEFAULT NULL,
  `Received_Qty` decimal(24,7) DEFAULT NULL,
  `Closed_Qty` decimal(24,7) DEFAULT NULL,
  `OnTime_Qty` decimal(24,7) DEFAULT NULL,
  `Stocked_Qty` decimal(24,7) DEFAULT NULL,
  `Returned_Qty` decimal(24,7) DEFAULT NULL,
  `Reworked_Qty` decimal(24,7) DEFAULT NULL,
  `Scrapped_Qty` decimal(24,7) DEFAULT NULL,
  `Rejected_Qty` decimal(24,7) DEFAULT NULL,
  `Adjusted_Qty` decimal(24,7) DEFAULT NULL,
  `PO_Receive_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Received_Amount` decimal(18,2) DEFAULT NULL,
  `Total_Amount` decimal(18,2) DEFAULT NULL,
  `Tax_Amount` decimal(18,2) DEFAULT NULL,
  `Reason_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Open_Amount` decimal(18,2) DEFAULT NULL,
  `Closed_Amount` decimal(18,2) DEFAULT NULL,
  `Landed_Cost` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,12) DEFAULT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Detail_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Remark` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Cost` decimal(18,2) DEFAULT NULL,
  `Last_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Next_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_PO_Receive_Hdr_Stg`
--

DROP TABLE IF EXISTS `IL_PO_Receive_Hdr_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_PO_Receive_Hdr_Stg` (
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Receive_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Receive_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Received_Date` datetime DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `Receive_Due_Date` datetime DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL,
  `Supplier_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Line_Number` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pay_Item_Extension_No` bigint(20) DEFAULT NULL,
  `PO_Receive_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Received_Amount` decimal(24,7) DEFAULT NULL,
  `Landed_Cost` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_PO_Release_Stg`
--

DROP TABLE IF EXISTS `IL_PO_Release_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_PO_Release_Stg` (
  `PO_Rel_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Release_Date` datetime DEFAULT NULL,
  `PO_Due_Date` datetime DEFAULT NULL,
  `PO_Rel_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Seq_Number` bigint(20) DEFAULT NULL,
  `Job_Seq_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Requisition_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Requisition_Line_Number` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shipped_Date` datetime DEFAULT NULL,
  `Shipped_Qty` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `Bboolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Packing_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Packing_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Packing_Dtl_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Packing_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Packing_Line_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Packing_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Packing_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Packing_Code_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Packing_Planed_Date` datetime DEFAULT NULL,
  `Packing_Due_Date` datetime DEFAULT NULL,
  `Packing_Date` datetime DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Packing_Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Packed_Qty` decimal(24,7) DEFAULT NULL,
  `Remaining_Qty` decimal(24,7) DEFAULT NULL,
  `Release_Qty` decimal(24,7) DEFAULT NULL,
  `Rejected_Qty` decimal(24,7) DEFAULT NULL,
  `Rework_Qty` decimal(24,7) DEFAULT NULL,
  `Packing_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Packing_Gross_Weight` decimal(24,7) DEFAULT NULL,
  `Packing_Cost` decimal(24,7) DEFAULT NULL,
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Shipped_Date` datetime DEFAULT NULL,
  `Deliver_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivey_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_Country_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Delivery_State_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Payment_Mode_Stg`
--

DROP TABLE IF EXISTS `IL_Payment_Mode_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Payment_Mode_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Payment_Mode_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Payment_Mode_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Mode_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Instruction_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Visible_Flag` bit(1) DEFAULT NULL,
  `Editable_Flag` bit(1) DEFAULT NULL,
  `OpensCashDrawer_Flag` bit(1) DEFAULT NULL,
  `Supports_Tipping_Flag` bit(1) DEFAULT NULL,
  `Enabled_Flag` bit(1) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Payment_Terms_Code_Stg`
--

DROP TABLE IF EXISTS `IL_Payment_Terms_Code_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Payment_Terms_Code_Stg` (
  `Payment_Terms_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Payment_Terms_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Terms_Descr` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Terms_Percentage` decimal(18,2) DEFAULT NULL,
  `No_Of_Split_Payments` bigint(20) DEFAULT NULL,
  `No_Of_Mnth_Add_To_DueDate` bigint(20) DEFAULT NULL,
  `Discount_Days` bigint(20) DEFAULT NULL,
  `Net_Days_to_Pay` bigint(20) DEFAULT NULL,
  `Advance_Pay_Terms_Flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Payments_Stg`
--

DROP TABLE IF EXISTS `IL_Payments_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Payments_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Payments_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `LineItem_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customers_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Card_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Mode_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Device_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Payment_Date` datetime DEFAULT NULL,
  `Payment_Amount` decimal(24,7) DEFAULT NULL,
  `Cashback_Amount` decimal(24,7) DEFAULT NULL,
  `Tip_Amount` decimal(24,7) DEFAULT NULL,
  `Tax_Amount` decimal(24,7) DEFAULT NULL,
  `ServiceCharge_Amount` decimal(24,7) DEFAULT NULL,
  `Taxable_Amount` decimal(24,7) DEFAULT NULL,
  `Refund_Amount` decimal(24,7) DEFAULT NULL,
  `Tax_Rate` decimal(24,7) DEFAULT NULL,
  `Note` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Void_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Result` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Offline_Flag` bit(1) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Persons_Stg`
--

DROP TABLE IF EXISTS `IL_Persons_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Persons_Stg` (
  `Person_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Organization_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Person_Email_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Person_Phone_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Person_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Primary_Flag` bit(1) DEFAULT NULL,
  `Phone_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_Primary_Flag` bit(1) DEFAULT NULL,
  `Open_Deals_Count` bigint(20) DEFAULT NULL,
  `Related_Open_Deals_Count` bigint(20) DEFAULT NULL,
  `Closed_Deals_Count` bigint(20) DEFAULT NULL,
  `Related_Closed_Deals_Count` bigint(20) DEFAULT NULL,
  `Participant_Open_Deals_Count` bigint(20) DEFAULT NULL,
  `Participant_Closed_Deals_Count` bigint(20) DEFAULT NULL,
  `Email_Messages_Count` bigint(20) DEFAULT NULL,
  `Activities_Count` bigint(20) DEFAULT NULL,
  `Done_Activities_Count` bigint(20) DEFAULT NULL,
  `Undone_Activities_Count` bigint(20) DEFAULT NULL,
  `Reference_Activities_Count` bigint(20) DEFAULT NULL,
  `Files_Count` bigint(20) DEFAULT NULL,
  `Notes_Count` bigint(20) DEFAULT NULL,
  `Followers_Count` bigint(20) DEFAULT NULL,
  `Won_Deals_Count` bigint(20) DEFAULT NULL,
  `Related_Won_Deals_Count` bigint(20) DEFAULT NULL,
  `Lost_Deals_Count` bigint(20) DEFAULT NULL,
  `Related_Lost_Deals_Count` bigint(20) DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Next_Activity_Date` datetime DEFAULT NULL,
  `Next_Activity_Id` bigint(20) DEFAULT NULL,
  `Last_Activity_Id` bigint(20) DEFAULT NULL,
  `Last_Activity_Date` datetime DEFAULT NULL,
  `Last_Incoming_Mail_Time` datetime DEFAULT NULL,
  `Last_Outgoing_Mail_Time` datetime DEFAULT NULL,
  `Next_Activity_Time` datetime DEFAULT NULL,
  `Last_Activity_Time` datetime DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Pick_Details_Stg`
--

DROP TABLE IF EXISTS `IL_Pick_Details_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Pick_Details_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Pick_Slip_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Picked_Date` datetime NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Scheduled_Pick_Date` datetime DEFAULT NULL,
  `Sales_Order_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Batch_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pick_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pickup_From` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Dock_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Orderd_Qty` decimal(24,7) DEFAULT NULL,
  `Planned_Pick_Qty` decimal(24,7) DEFAULT NULL,
  `Picked_Qty` decimal(24,7) DEFAULT NULL,
  `PickUp_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pick_Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Resource_Pick_Hours` decimal(18,2) DEFAULT NULL,
  `Pick_Cost` decimal(18,2) DEFAULT NULL,
  `Stnd_Cost` decimal(18,2) DEFAULT NULL,
  `Pick_Resource_Cost` decimal(18,2) DEFAULT NULL,
  `Planned_Delivery_Date` datetime DEFAULT NULL,
  `Deliver_Date` datetime DEFAULT NULL,
  `Labour_Cost` decimal(18,2) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean01` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_PipeLine_Stg`
--

DROP TABLE IF EXISTS `IL_PipeLine_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_PipeLine_Stg` (
  `Pipeline_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pipeline_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Url_Title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Order_Seq_Num` bigint(20) DEFAULT NULL,
  `selected_Flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Plant_Stg`
--

DROP TABLE IF EXISTS `IL_Plant_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Plant_Stg` (
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source id of the Plant',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Code wher the Plant located.',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Plant_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant name details from source',
  `Plant_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Plant',
  `Address1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address of the Plant Location',
  `Address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address of the Plant Location',
  `City` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'City Details of the Plant',
  `State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'State of the Plant',
  `Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Country of the Plant',
  `Zip_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Postal code of the plant',
  `Email_Addr` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Email address of the Plant',
  `Phone_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Phone number of the plant',
  `Fax_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Fax Details',
  `Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region location of the Plant .. Eg : continent . Asia',
  `Sub_Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Region location of the Plant',
  `Plant_Latitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Latitude details',
  `Plant_Longitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Longitude details',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `IsActive` bit(1) DEFAULT NULL COMMENT '0 for active record and 1 for inactive record.',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Pre_Payment_Transactions_Stg`
--

DROP TABLE IF EXISTS `IL_Pre_Payment_Transactions_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Pre_Payment_Transactions_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Fee_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Fee` decimal(18,2) DEFAULT NULL,
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supp_Bank_Account_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bank_Receipt_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL,
  `Invoice_Qty` decimal(24,7) DEFAULT NULL,
  `Payment_Terms_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pre_Payment_Transaction_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pre_Payment_Error_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cash_Payment_Amount` decimal(18,2) DEFAULT NULL,
  `Check_Payment_Amount` decimal(18,2) DEFAULT NULL,
  `Tax_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,12) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Price_Book_Stg`
--

DROP TABLE IF EXISTS `IL_Price_Book_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Price_Book_Stg` (
  `Price_Book_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Price_Book_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Price_Book_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Price_Book_Short_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Standard_Flag` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_ProductPlant_Stg`
--

DROP TABLE IF EXISTS `IL_ProductPlant_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_ProductPlant_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Capacity` float DEFAULT NULL,
  `Safty_Level` decimal(18,2) DEFAULT NULL,
  `Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Effective_Start_Date` datetime DEFAULT NULL,
  `Effective_End_Date` datetime DEFAULT NULL,
  `Primary_Supplier_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lot_Size` float DEFAULT NULL,
  `Inventory_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Product_CRM_Stg`
--

DROP TABLE IF EXISTS `IL_Product_CRM_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Product_CRM_Stg` (
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Price_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Unit` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Tax_Percent` decimal(18,2) DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Files_Count` bigint(20) DEFAULT NULL,
  `Followers_Count` bigint(20) DEFAULT NULL,
  `Selectable_Flag` bit(1) DEFAULT NULL,
  `Product_Unit_Price` decimal(18,2) DEFAULT NULL,
  `Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Cost` decimal(18,2) DEFAULT NULL,
  `Price_Overhead_Cost` decimal(18,2) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Product_Deal_Stg`
--

DROP TABLE IF EXISTS `IL_Product_Deal_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Product_Deal_Stg` (
  `Deal_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Unit_Price` decimal(18,2) NOT NULL,
  `Product_Qty` decimal(24,6) NOT NULL,
  `Discount_Percentage` decimal(18,2) NOT NULL,
  `Duration` decimal(18,2) DEFAULT NULL,
  `Total_Discount` decimal(18,2) DEFAULT NULL,
  `Product_Value` decimal(18,2) DEFAULT NULL,
  `Currency_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Enabled_Flag` bit(1) NOT NULL,
  `Comments` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Active_Flag` bit(1) NOT NULL,
  `Seq_Num` int(11) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Product_Stg`
--

DROP TABLE IF EXISTS `IL_Product_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Product_Stg` (
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source Product ID details',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company name of the Product',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Product_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Source Product code',
  `Product_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Product',
  `Prod_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Product',
  `ProductSpec` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product Specification details',
  `Prod_Grp_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product Group or Caregory',
  `Prod_Sub_Grp_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product Sub Group or Caregory',
  `Product_Line_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product Line Category',
  `Brand_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'market brand codes to uniquely identify products and traded items',
  `Product_Height` decimal(18,2) DEFAULT NULL,
  `Product_Length` decimal(18,2) DEFAULT NULL,
  `Product_Width` decimal(18,2) DEFAULT NULL,
  `Size_UOM` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Weight` decimal(18,2) DEFAULT NULL,
  `Weight_UOM` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `IsActive` bit(1) DEFAULT NULL COMMENT '0 for active record and 1 for inactive record.',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Production_Planning_Stg`
--

DROP TABLE IF EXISTS `IL_Production_Planning_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Production_Planning_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plan_Date` datetime NOT NULL,
  `Production_Line` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Capacity` decimal(24,6) DEFAULT NULL,
  `Plant_Capacity_Revenue` decimal(18,2) DEFAULT NULL,
  `Prod_Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Prod_Planned_Revenue` decimal(18,2) DEFAULT NULL,
  `Actual_Produced_Qty` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Production_Projection_Stg`
--

DROP TABLE IF EXISTS `IL_Production_Projection_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Production_Projection_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Ingredient_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Projection_Date` datetime NOT NULL,
  `Projection_From_Date` datetime NOT NULL,
  `Projection_To_Date` datetime NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Projection_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Qty` decimal(24,7) DEFAULT NULL,
  `Item_Qty` decimal(24,7) DEFAULT NULL,
  `Projection_Qty` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Extended_Amount` decimal(24,7) DEFAULT NULL,
  `Projection_Amount` decimal(24,7) DEFAULT NULL,
  `Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Production_Stg`
--

DROP TABLE IF EXISTS `IL_Production_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Production_Stg` (
  `Prod_Plan_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Planned_Date` datetime DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Machine_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Prod_Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Prod_Date` datetime DEFAULT NULL,
  `Prod_Actual_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,12) DEFAULT NULL,
  `Planned_Staus_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Prod_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Planned_Prod_Hours` decimal(18,2) DEFAULT NULL,
  `Prod_Hours` decimal(18,2) DEFAULT NULL,
  `HourPerPrice` decimal(24,12) DEFAULT NULL,
  `Scrap_Qty` decimal(24,7) DEFAULT NULL,
  `Est_Scrap_Qty` decimal(24,7) DEFAULT NULL,
  `Production_Revenue` decimal(18,2) DEFAULT NULL,
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Budget_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Budget_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Budget_Type_Stg` (
  `Budget_Type_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Bud_Type_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bud_Type_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Category_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Category_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Category_Stg` (
  `Proj_Category_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Category_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Category_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Dtl_Stg` (
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Date` datetime DEFAULT NULL,
  `Proj_Due_Date` datetime DEFAULT NULL,
  `Proj_Start_date` datetime DEFAULT NULL,
  `Proj_End_Date` datetime DEFAULT NULL,
  `Proj_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Manager` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Staff_Size` decimal(24,7) DEFAULT NULL,
  `Extension_Date` datetime DEFAULT NULL,
  `Scheduling_Calendar` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Customer_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Manager_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Proj_Quantity` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SO_Order_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SO_Order_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SO_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Order_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Order_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Line_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Value` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Group_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Group_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Group_Stg` (
  `Proj_Group_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Group_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Group_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Job_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Job_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Job_Stg` (
  `Job_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Task_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Job_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Proj_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Job_Start_Date` datetime DEFAULT NULL,
  `Job_Requested_Date` datetime DEFAULT NULL,
  `Job_Due_Date` datetime DEFAULT NULL,
  `Job_Completion_Date` datetime DEFAULT NULL,
  `Job_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Team_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Prod_Cost_Per_Piece` decimal(24,7) DEFAULT NULL,
  `Requested_Qty` decimal(24,7) DEFAULT NULL,
  `Completed_Qty` decimal(24,7) DEFAULT NULL,
  `Scrap_Percentage` decimal(24,7) DEFAULT NULL,
  `Scrap_Qty` decimal(24,7) DEFAULT NULL,
  `Actual_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Labour_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Machine_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Other_Time_In_Sec` decimal(24,7) DEFAULT NULL,
  `Mtl_Burden_Cost` decimal(24,7) DEFAULT NULL,
  `Material_Cost` decimal(24,7) DEFAULT NULL,
  `Burden_Cost` decimal(24,7) DEFAULT NULL,
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Sub_Contract_Cost` decimal(24,7) DEFAULT NULL,
  `Job_Cost` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Method_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Method_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Method_Stg` (
  `Proj_Method_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Method_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Method_Short_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Task_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Task_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Task_Dtl_Stg` (
  `Task_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Sub_Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Team_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Task_Due_date` datetime DEFAULT NULL,
  `Task_Start_Date` datetime DEFAULT NULL,
  `Task_End_date` datetime DEFAULT NULL,
  `Task_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Task_Quantity` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Task_Value` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Task_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Task_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Task_Stg` (
  `Task_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Sub_Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Task_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Task_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Proj_Type_Stg`
--

DROP TABLE IF EXISTS `IL_Proj_Type_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Proj_Type_Stg` (
  `Proj_Type_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Short_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Project_Stg`
--

DROP TABLE IF EXISTS `IL_Project_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Project_Stg` (
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Project_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Controller` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Group_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Category_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Type_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Proj_Method_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Comment_Text` varchar(8000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Quote_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Quote_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Quote_Dtl_Stg` (
  `Quote_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Quote_Line_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Quote_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customer_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Draw_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `Quote_Due_Date` datetime DEFAULT NULL,
  `Quote_Date` datetime DEFAULT NULL,
  `Follow_Up_Date` datetime DEFAULT NULL,
  `Quote_Expiry_Date` datetime DEFAULT NULL,
  `Expected_Close_Date` datetime DEFAULT NULL,
  `Quote_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Line_Amount` decimal(24,7) DEFAULT NULL,
  `Quote_Line_Discount` decimal(24,7) DEFAULT NULL,
  `Planned_Qty` decimal(24,7) DEFAULT NULL,
  `Expected_Line_Revenue` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Extended_Price` decimal(24,7) DEFAULT NULL,
  `Ordered_Qty` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Line_Number` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Category` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warranty_info` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Price` decimal(18,2) DEFAULT NULL,
  `Avg_Price` decimal(18,2) DEFAULT NULL,
  `Last_Price` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Quote_Hdr_Stg`
--

DROP TABLE IF EXISTS `IL_Quote_Hdr_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Quote_Hdr_Stg` (
  `Quote_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Quote_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Requested_Date` datetime DEFAULT NULL,
  `Quote_Date` datetime DEFAULT NULL,
  `Quote_Due_Date` datetime DEFAULT NULL,
  `Quote_Expiry_Date` datetime DEFAULT NULL,
  `Follow_Up_Date` datetime DEFAULT NULL,
  `Expected_Close_Date` datetime DEFAULT NULL,
  `Customer_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quote_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Expiration_Flag` bit(1) DEFAULT NULL,
  `Quoted_Flag` bit(1) DEFAULT NULL,
  `Discount_Amount` decimal(24,7) DEFAULT NULL,
  `Misc_Amount` decimal(24,7) DEFAULT NULL,
  `Expected_Revenue` decimal(24,7) DEFAULT NULL,
  `PO_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Comments` varchar(8000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Resource_Stg`
--

DROP TABLE IF EXISTS `IL_Resource_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Resource_Stg` (
  `Resource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Resource_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Resource_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Resource_Category` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supervisor_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shift_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Returns_Stg`
--

DROP TABLE IF EXISTS `IL_Returns_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Returns_Stg` (
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order Number that linked to Sales to track the details',
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order line number that linked from Retruned',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Return_Date` datetime NOT NULL COMMENT 'Date of the product Returned by customer',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company details',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product code/Item Code/Short Descr that Retruned',
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant details',
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Customer that Order retruned',
  `Return_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Quantity Retruned by the customer',
  `UOM_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL COMMENT 'Unit Price of the Product',
  `Retrun_Amount` decimal(18,2) DEFAULT NULL COMMENT 'Total Retruned amount of the Line',
  `Labour_Cost` decimal(24,7) DEFAULT NULL,
  `Misc_Amount` decimal(24,7) DEFAULT NULL,
  `Landed_Cost` decimal(24,7) DEFAULT NULL,
  `Shipping_Cost` decimal(24,7) DEFAULT NULL,
  `Return_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warranty_Period_Flag` bit(1) DEFAULT NULL,
  `Warranty_Start_Date` datetime DEFAULT NULL,
  `Warranty_End_Date` datetime DEFAULT NULL,
  `Retrun_Reason_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of the Retrun reason',
  `Return_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Reason for retruning',
  `Base_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base currency code for Caluclation',
  `Transaction_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction currency details',
  `Exchange_Rate` decimal(24,12) DEFAULT NULL COMMENT 'Exchange rate of the currency between base currency and transaction currency',
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Role_Stg`
--

DROP TABLE IF EXISTS `IL_Role_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Role_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Role_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Role_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Role_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `System_Role` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_SalesRep_Goals_Dtl`
--

DROP TABLE IF EXISTS `IL_SalesRep_Goals_Dtl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_SalesRep_Goals_Dtl` (
  `SalesRep_Goal_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `SalesRep_Goal_Line_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Goal_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Sales_Rep_Goals_Key` bigint(20) NOT NULL AUTO_INCREMENT,
  `Goal_Date` datetime DEFAULT NULL,
  `Goal_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Goal_Seq_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Planned_Start_Date` datetime DEFAULT NULL,
  `Planned_End_Date` datetime DEFAULT NULL,
  `Goal_Status_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Actual_Start_Date` datetime DEFAULT NULL,
  `Actual_End_Date` datetime DEFAULT NULL,
  `Sales_Target_Amount` decimal(24,7) DEFAULT NULL,
  `Goal_Change_Reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Comments` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`SalesRep_Goal_Id`,`SalesRep_Goal_Line_Id`,`Goal_Type`,`Company_Id`,`DataSource_Id`),
  KEY `IL_Sales_Rep_Goals_idx_key` (`Sales_Rep_Goals_Key`),
  KEY `IL_SalesRep_Goals_Dtl_FKIndex1` (`Sales_Rep_Code`,`Company_Id`,`DataSource_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_SalesRep_Stg`
--

DROP TABLE IF EXISTS `IL_SalesRep_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_SalesRep_Stg` (
  `Sales_Rep_Code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Sales person Id or code from the source system',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company of the Sales Representive',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Sales_Rep_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales person Name',
  `First_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Person First name',
  `Last_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Person Last Name',
  `Middle_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales Person Middle Name',
  `Address1` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address details',
  `Address2` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address details',
  `City` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'City detail',
  `State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'State of the Representive',
  `Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Country of the Representive',
  `Zip_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Postal code or zipcode of the Sales representive',
  `Territory_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'territory Details of the Sale Representive',
  `Email_addr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Email address details',
  `Sales_Rep_Designation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Designation of the Sales person',
  `Phone_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Phone number detail',
  `Manager_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Reporting  manager name detail',
  `Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales person Region details',
  `Sub_Region` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sales person Sub Region details',
  `Sale_Rep_Longitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Latitude details',
  `Sale_Rep_Latitude` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Geo Location Longitude details',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `IsActive` bit(1) DEFAULT NULL COMMENT '0 for active record and 1 for inactive record.',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Sales_Projection_Stg`
--

DROP TABLE IF EXISTS `IL_Sales_Projection_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Sales_Projection_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Customers_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Projection_Date` datetime NOT NULL,
  `Projection_From_Date` datetime NOT NULL,
  `Projection_To_Date` datetime NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Projection_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Projection_Qty` decimal(24,7) DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `Extended_Amount` decimal(24,7) DEFAULT NULL,
  `Projection_Amount` decimal(24,7) DEFAULT NULL,
  `Comments` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Service_Provider_Stg`
--

DROP TABLE IF EXISTS `IL_Service_Provider_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Service_Provider_Stg` (
  `Service_Provider_Id` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Uniquely identifed Distributer / Goods Delivery Provider Details',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Service_Provider_Name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Distributor or Provider for Logistic',
  `Service_Provider_Descr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Service provider',
  `IsActive` bit(1) DEFAULT NULL COMMENT 'If he is active 0  inactive 1',
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Shift_Stg`
--

DROP TABLE IF EXISTS `IL_Shift_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Shift_Stg` (
  `Shift_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Shift_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Shift_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Shipments_Goal_Stg`
--

DROP TABLE IF EXISTS `IL_Shipments_Goal_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Shipments_Goal_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Details of the Shipment',
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Plant Id details',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Product details',
  `Start_Date` datetime NOT NULL COMMENT 'Start or begin period for Shipment goal',
  `End_Date` datetime NOT NULL COMMENT 'End period for goal',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Ship_Goal_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Shipment goal quantity',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Shipments_Stg`
--

DROP TABLE IF EXISTS `IL_Shipments_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Shipments_Stg` (
  `Shipment_Number` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Shippment Number Uniquely identifed by the System',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'SO Shipped Company',
  `Plant_ID` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'SO Plant or Bussiness unit',
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Sales order id mapped to Sales oder dtl table',
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Sales order id mapped to Sales oder dtl table',
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Shipped_Date` datetime NOT NULL COMMENT 'Date Shipped the product to customer/Distributor',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Product or Item Code / Short descr of the SO',
  `Job_Number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Job Number Related to Production',
  `Order_Rel_Num` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'SO Release Number.',
  `Customer_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Primary Customer details of the SO',
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Warehouse details',
  `Promise_To_Ship_date` datetime DEFAULT NULL COMMENT 'Date of Shippment promise.',
  `Order_Pickedup_Date` datetime DEFAULT NULL COMMENT 'Data of Picked up for shippment',
  `Shipped_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Number of quantity shipped to customer/Distributor',
  `Unit_Price` decimal(24,7) DEFAULT NULL COMMENT 'Unit Price of the Product',
  `Shipped_Amount` decimal(24,7) DEFAULT NULL COMMENT 'Shipped Order Line Amount',
  `Handling_Cost` decimal(18,2) DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL COMMENT 'Date of bill/Invoice generated',
  `Cancelled_Date` datetime DEFAULT NULL COMMENT 'Cancelled Date of Shippment',
  `Cancelled_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Total number Qty cacelled for shippment of the product',
  `Base_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base Currency code of the Company',
  `Transaction_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction Currency code of the Company',
  `Exchange_Rate` decimal(24,12) DEFAULT NULL COMMENT 'Exchange /Conversion Rate of the Currency',
  `Mode_Of_Transport` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transport mode through Air,Road,Water,Pipeline',
  `Container_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Container Details',
  `Reason_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Reason code for Cancel or on hold',
  `Project_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ship_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_ShopFloor_Stg`
--

DROP TABLE IF EXISTS `IL_ShopFloor_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_ShopFloor_Stg` (
  `ShopFloor_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source Production Line or Shopfloor uniquely identified',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company code that Shopfloor located',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `ShopFloor_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Production Line or Shopfloor',
  `ShopFloor_Descr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Production Line or Shopfloor',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Plant or Bussiness Unit that shopfloor or Production line allocated',
  `No_Of_Employees` bigint(20) DEFAULT NULL COMMENT 'Total number of employee allocated to Shopfloor',
  `Number_Of_Machine` bigint(20) DEFAULT NULL COMMENT 'Total number of Machines allocated to Shopfloor',
  `Stand_Queue_Hours` decimal(24,7) DEFAULT NULL COMMENT 'Standard queue hours requried to produce product',
  `Minimum_Capacity` decimal(24,7) DEFAULT NULL COMMENT 'Minimum Capacity of the Shopfloor',
  `Maximum_Capacity` decimal(24,7) DEFAULT NULL COMMENT 'Maximum Capacity of the Shopfloor',
  `Work_Hours_Per_Day` decimal(24,7) DEFAULT NULL COMMENT 'Number of working hours per day',
  `IsActive` bit(1) DEFAULT NULL COMMENT 'If Shopfloor is active then 0 else 1',
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Source_Activies_Stg`
--

DROP TABLE IF EXISTS `IL_Source_Activies_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Source_Activies_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Source_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Source_Object` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Field_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Activies_Seq_Number` int(11) DEFAULT NULL,
  `New_Value` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Old_Value` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Modified_Date` datetime DEFAULT NULL,
  `Modified_By` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Stages_Stg`
--

DROP TABLE IF EXISTS `IL_Stages_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Stages_Stg` (
  `Stage_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Pipeline_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Stage_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `Deal_Probability` bigint(20) DEFAULT NULL,
  `Rotten_Flag` bit(1) DEFAULT NULL,
  `Rotten_Days` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Stage_Seq_Num` bigint(20) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Sub_Proj_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Sub_Proj_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Sub_Proj_Dtl_Stg` (
  `Sub_Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Proj_Start_Date` datetime DEFAULT NULL,
  `Sub_Proj_End_date` datetime DEFAULT NULL,
  `Sub_Proj_Due_date` datetime DEFAULT NULL,
  `Sub_Proj_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(24,7) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Proj_Quantity` decimal(24,7) DEFAULT NULL,
  `Percent_Complete` decimal(24,7) DEFAULT NULL,
  `Sub_Proj_Value` decimal(24,7) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,7) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Sub_Proj_Stg`
--

DROP TABLE IF EXISTS `IL_Sub_Proj_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Sub_Proj_Stg` (
  `Sub_Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Proj_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Sub_Proj_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sub_Proj_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Parent_Sub_Proj_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Supplier_Invoice_Stg`
--

DROP TABLE IF EXISTS `IL_Supplier_Invoice_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Supplier_Invoice_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Supplier_Invoice_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `PO_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bin_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supp_Invoice_Date` datetime DEFAULT NULL,
  `PO_Requested_Date` datetime DEFAULT NULL,
  `Due_Date` datetime DEFAULT NULL,
  `Contract_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contract_Detail_Id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Supplier_Invoice_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PO_Order_Qty` decimal(24,7) DEFAULT NULL,
  `Accepted_Qty` decimal(24,7) DEFAULT NULL,
  `Rejected_Qty` decimal(24,7) DEFAULT NULL,
  `Total_Amount` decimal(18,2) DEFAULT NULL,
  `Accepted_Amount` decimal(18,2) DEFAULT NULL,
  `Rejected_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(24,12) DEFAULT NULL,
  `Supplier_Invoice_Approver` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Number` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Voucher_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Supplier_Stg`
--

DROP TABLE IF EXISTS `IL_Supplier_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Supplier_Stg` (
  `Supplier_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Supplier/Vendor Id or Code from the source',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Code / Id details',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Supplier_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Supplier name details',
  `Supplier_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name or Description of the Vendor or Supplier',
  `Supplier_Address1` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Supplier address',
  `Supplier_Address2` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Supplier address',
  `Supplier_City` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Supplier city',
  `State` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'State of the Supplier',
  `Country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Country of the Supplier',
  `Supplier_Zipcode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'supplier Postal code or Zipcode',
  `Supplier_Category` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'supply cateogry',
  `Phone_No` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Home or Office Ph number',
  `Fax_Num` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Fax number details of the Supplier',
  `Email_Addr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Email address of the supplier',
  `Home_Page` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Web site page of the Supplier',
  `Base_Currency_Code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Min_Order_Value` decimal(24,7) DEFAULT NULL,
  `Max_Order_Value` decimal(24,7) DEFAULT NULL,
  `Approver_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `IsActive` bit(1) DEFAULT NULL COMMENT '0 for active record and 1 for inactive record.',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Task_Stg`
--

DROP TABLE IF EXISTS `IL_Task_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Task_Stg` (
  `Task_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Task_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Task_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Task_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Task_Date` datetime DEFAULT NULL,
  `Task_Due_Date` datetime DEFAULT NULL,
  `Task_Priority` bigint(20) DEFAULT NULL,
  `Lead_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IsColsed_Flag` bit(1) DEFAULT NULL,
  `Call_Duration_Sec` decimal(24,7) DEFAULT NULL,
  `Call_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Call_Remarks` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Call_Remainder_Date` datetime DEFAULT NULL,
  `Reminder_Set_Flag` bit(1) DEFAULT NULL,
  `Recur_Activity_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Recur_Flag` bit(1) DEFAULT NULL,
  `Recur_Start_Date` datetime DEFAULT NULL,
  `Recur_End_Date` datetime DEFAULT NULL,
  `Recur_Language_Knows` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Recur_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Recur_Interval` decimal(24,7) DEFAULT NULL,
  `Recur_Weekly` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Recur_Monthly` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Recur_Year` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Recur_Instance` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Team_Stg`
--

DROP TABLE IF EXISTS `IL_Team_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Team_Stg` (
  `Team_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Department_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Team_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Lead_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Team_Desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Transfer_Order_Stg`
--

DROP TABLE IF EXISTS `IL_Transfer_Order_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Transfer_Order_Stg` (
  `Company_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'From Company Id that Trasfer Order',
  `From_Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Plant details that Order got transfered',
  `To_Company_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Company that Transfer the Order',
  `To_Plant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Company which order got Received',
  `Transfer_Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Transfer Order Number that generated to track',
  `Transfer_Order_Line_Number` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order line number that generated to track for each product',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Product Id details',
  `Requested_Date` datetime DEFAULT NULL COMMENT 'Date of Order got requested',
  `Requested_Qty` decimal(24,7) DEFAULT NULL,
  `Transfer_Due_Date` datetime DEFAULT NULL COMMENT 'Due date of the Trasfer',
  `Transfered_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Total Trasfer Qty of the Product',
  `Unit_Price` decimal(24,12) DEFAULT NULL COMMENT 'Price of the Product',
  `Transfer_Date` datetime DEFAULT NULL COMMENT 'Date of the Product Transfered',
  `Transfer_Status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the Transfer of the product',
  `Cancelled_Date` datetime DEFAULT NULL COMMENT 'Data of cancellation of the TF Order',
  `Cancelled_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Cancelled Qty of the Product',
  `Base_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Base currency of the Code set',
  `Transaction_Currency_Code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction currency code set',
  `Exchange_Rate` decimal(24,12) DEFAULT NULL COMMENT 'Exchage rate between Base currency and Transaction currency',
  `Mode_of_Transfer` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Mode of Transfer',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Transport_Logistic_Stg`
--

DROP TABLE IF EXISTS `IL_Transport_Logistic_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Transport_Logistic_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Order_LineNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `Order_Type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Transport_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Transport Code details for shipment to customer',
  `Logistic_Id` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Logistic number or tracking system number to track the shipment for each product to customer',
  `PickedUp_Date` datetime NOT NULL COMMENT 'Goods or product picked up Date',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Service_Provider_Id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Distributer Id Detaisl on Logistic Details',
  `Track_Number` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tracking number from Distributer',
  `Delivery_Status` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Status of the Shipment to Customer  or Storage location of distributor',
  `Due_Date` datetime DEFAULT NULL COMMENT 'Due date of delivery',
  `Delivered_Date` datetime DEFAULT NULL COMMENT 'Actual Delivered Date',
  `PickedUp_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Picked up Quantity',
  `Delivered_Qty` decimal(24,7) DEFAULT NULL COMMENT 'Delivered Quantity',
  `FreightCost` decimal(24,7) DEFAULT NULL COMMENT 'Freight Cost involved based on Mode of Transport Air,Pipeline,Water.. etc seaparte cost',
  `Demage_Percentage` decimal(24,7) DEFAULT NULL COMMENT 'Damages in percentage while shipping the goods from one place to another',
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(24,7) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Transportation_Stg`
--

DROP TABLE IF EXISTS `IL_Transportation_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Transportation_Stg` (
  `Transport_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Uniquely identified Transport Code or ID',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Transport_Code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Code of the Transport',
  `Transport_Name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of the Transport',
  `Transport_Descr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Description of the Transport',
  `IsActive` bit(1) DEFAULT NULL COMMENT 'Status of the Transport if active 0 inactive 1',
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Updated_Date',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_UOM_Conversion_Stg`
--

DROP TABLE IF EXISTS `IL_UOM_Conversion_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_UOM_Conversion_Stg` (
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Product_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `From_UOM` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `To_UOM` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Conversion_Factor` float DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Unit_Of_Measurement_Stg`
--

DROP TABLE IF EXISTS `IL_Unit_Of_Measurement_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Unit_Of_Measurement_Stg` (
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `UOM_Type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `UOM_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Short_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Precision` int(10) DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_User_License_Stg`
--

DROP TABLE IF EXISTS `IL_User_License_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_User_License_Stg` (
  `User_License_Id` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `User_License_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_License_Short_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `DateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Update_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_User_Role_Stg`
--

DROP TABLE IF EXISTS `IL_User_Role_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_User_Role_Stg` (
  `User_Role_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Datasource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `User_Role_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Role_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Developer_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Portal_Account_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Portal_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Portal_Account_Owner_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_User_Stg`
--

DROP TABLE IF EXISTS `IL_User_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_User_Stg` (
  `User_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `User_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `First_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Alias_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Manager_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Role_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `User_Profile_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Division_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Dept_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zip_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Mobile_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fax_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Company_Email_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Personal_Email_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Cal_Center_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Account_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Contact_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Language_Knows` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Last_Login_Date` datetime DEFAULT NULL,
  `Company_Owner_Flag` bit(1) DEFAULT NULL,
  `Admin_Flag` bit(1) DEFAULT NULL,
  `Active_Flag` bit(1) DEFAULT NULL,
  `TimeZone_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Url_Link` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Vendor_Invoice_Stg`
--

DROP TABLE IF EXISTS `IL_Vendor_Invoice_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Vendor_Invoice_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Line_No` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Vendor_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Line_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL,
  `Ingredient_Qty` decimal(18,2) DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `Invoice_Amount` decimal(18,2) DEFAULT NULL,
  `Invoice_Line_Amount` decimal(18,2) DEFAULT NULL,
  `Misc_Amount` decimal(18,2) DEFAULT NULL,
  `Tax_Amount` decimal(18,2) DEFAULT NULL,
  `Discount_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` decimal(18,2) DEFAULT NULL,
  `Invoice_Comment` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Vendor_Invoice_Stg1`
--

DROP TABLE IF EXISTS `IL_Vendor_Invoice_Stg1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Vendor_Invoice_Stg1` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Number` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Invoice_Line_No` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Vendor_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Line_Status` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Date` datetime DEFAULT NULL,
  `Ingredient_Qty` decimal(18,2) DEFAULT NULL,
  `Invoice_Amount` decimal(18,2) DEFAULT NULL,
  `Invoice_Line_Amount` decimal(18,2) DEFAULT NULL,
  `Misc_Amount` decimal(18,2) DEFAULT NULL,
  `Tax_Amount` decimal(18,2) DEFAULT NULL,
  `Discount_Amount` decimal(18,2) DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Transaction_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Exchange_Rate` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Invoice_Comment` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Vendor_Stg`
--

DROP TABLE IF EXISTS `IL_Vendor_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Vendor_Stg` (
  `Vendor_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Vendor_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vendor_Descr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vendor_Address1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vendor_Address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vendor_City` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `State` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Country` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vendor_Zipcode` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Vendor_Category` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Phone_No` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Fax_Num` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Email_Addr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Home_Page` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Base_Currency_Code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Min_Order_Value` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Max_Order_Value` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Approver_Name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BCharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Warehouse_Stg`
--

DROP TABLE IF EXISTS `IL_Warehouse_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Warehouse_Stg` (
  `Warehouse_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source Warehouse/Branch Id',
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Company Id of the Ware house or Branch allocated',
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'To Identify the Multiple Source System/Data mangement System that got loaded',
  `Warehouse_Name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Warehouse Name',
  `Plant_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Bussiness Unit or Plant that Branch or Warehouse available',
  `Warehouse_Type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Type of warehouse grouped together',
  `Warehouse_Height` decimal(24,8) DEFAULT NULL,
  `Warehouse_Width` decimal(24,8) DEFAULT NULL,
  `Warehouse_Length` decimal(24,8) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Usable_Width` decimal(18,2) DEFAULT NULL,
  `Warehouse_Usable_Height` decimal(18,2) DEFAULT NULL,
  `Warehouse_Usable_Length` decimal(18,2) DEFAULT NULL,
  `Usable_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Warehouse_Usable_Weight` decimal(18,2) DEFAULT NULL,
  `Usable_Weight_UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Location` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Location details',
  `BCharacter01` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BCharacter02` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber01` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BNumber02` decimal(18,2) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BDateTime01` datetime DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `BBoolean` bit(1) DEFAULT NULL COMMENT 'Extract column for any missing column mapping purpose',
  `Added_Date` datetime DEFAULT NULL COMMENT 'Source Record insert/created date or sysdate',
  `Added_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Added user',
  `Updated_Date` datetime DEFAULT NULL COMMENT 'Source Record Updated date or sysdate',
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Updated user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Warehouse_Test2_Stg`
--

DROP TABLE IF EXISTS `IL_Warehouse_Test2_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Warehouse_Test2_Stg` (
  `Warehouse_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Company_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Warehouse_Code` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Wastage_Stg`
--

DROP TABLE IF EXISTS `IL_Wastage_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Wastage_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Wastage_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Item_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Ingredient_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Item_Qty` decimal(18,2) DEFAULT NULL,
  `Ingredient_Qty` decimal(18,2) DEFAULT NULL,
  `UOM_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Unit_Price` decimal(18,2) DEFAULT NULL,
  `Total_Wastage_Qty` decimal(18,2) DEFAULT NULL,
  `Wastage_Amount` decimal(18,2) DEFAULT NULL,
  `Wastage_Date` datetime DEFAULT NULL,
  `Employee_Id` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Wastage_Reason` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(18,2) DEFAULT NULL,
  `BNumber02` decimal(18,2) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Weather_Forecast_Stg`
--

DROP TABLE IF EXISTS `IL_Weather_Forecast_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Weather_Forecast_Stg` (
  `DT_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `Main_Temp` decimal(19,0) DEFAULT NULL,
  `Main_Temp_Min` decimal(3,0) DEFAULT NULL,
  `Main_Temp_Max` decimal(3,0) DEFAULT NULL,
  `Pressure` decimal(3,0) DEFAULT NULL,
  `Sea_level` decimal(30,0) DEFAULT NULL,
  `Grnd_level` decimal(50,0) DEFAULT NULL,
  `Humidity` bigint(50) DEFAULT NULL,
  `Temp_Kf` decimal(30,0) DEFAULT NULL,
  `Weather_ID` bigint(50) DEFAULT NULL,
  `Weather_Main` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Weather_Description` varchar(19) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Weather_Icon` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Clouds_All` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Wind_Speed` decimal(24,7) DEFAULT NULL,
  `Wind_Deg` decimal(24,7) DEFAULT NULL,
  `Snow` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Sys_Pod` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Date_txt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IL_Working_Time_Dtl_Stg`
--

DROP TABLE IF EXISTS `IL_Working_Time_Dtl_Stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IL_Working_Time_Dtl_Stg` (
  `Merchant_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Working_Time_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Menu_Time_Type_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DataSource_Id` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Open_Date` datetime DEFAULT NULL,
  `Close_Date` datetime DEFAULT NULL,
  `Open_Time` time DEFAULT NULL,
  `Close_Time` time DEFAULT NULL,
  `Bcharacter01` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Bcharacter02` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BNumber01` decimal(24,7) DEFAULT NULL,
  `BNumber02` decimal(24,7) DEFAULT NULL,
  `BDateTime01` datetime DEFAULT NULL,
  `BBoolean` bit(1) DEFAULT NULL,
  `Added_Date` datetime DEFAULT NULL,
  `Added_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Updated_User` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `amws`
--

DROP TABLE IF EXISTS `amws`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amws` (
  `id` decimal(10,0) DEFAULT NULL,
  `title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `brand` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `price` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categorys` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rating` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `review` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weight` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `size` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `groups` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `amws_28032019`
--

DROP TABLE IF EXISTS `amws_28032019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amws_28032019` (
  `title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `brand` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `price` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categorys` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rating` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `review` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weight` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `size` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `groups` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `amws_data`
--

DROP TABLE IF EXISTS `amws_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amws_data` (
  `id` decimal(10,0) DEFAULT NULL,
  `title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `brand` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `price` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categorys` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rating` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `review` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weight` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `size` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `groups` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `amwsid`
--

DROP TABLE IF EXISTS `amwsid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amwsid` (
  `id` decimal(10,0) DEFAULT NULL,
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190315_114508_489`
--

DROP TABLE IF EXISTS `anv_temp_0_20190315_114508_489`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190315_114508_489` (
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190315_115212_771`
--

DROP TABLE IF EXISTS `anv_temp_0_20190315_115212_771`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190315_115212_771` (
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci,
  `Context` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionType` longtext COLLATE utf8mb4_unicode_ci,
  `User` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionNote` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionReason` longtext COLLATE utf8mb4_unicode_ci,
  `ScannedCode` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityAfter` longtext COLLATE utf8mb4_unicode_ci,
  `Sku` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityBefore` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionDate` longtext COLLATE utf8mb4_unicode_ci,
  `Context_ID` longtext COLLATE utf8mb4_unicode_ci,
  `Context_Type` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190315_122154_164`
--

DROP TABLE IF EXISTS `anv_temp_0_20190315_122154_164`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190315_122154_164` (
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci,
  `Context` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionType` longtext COLLATE utf8mb4_unicode_ci,
  `User` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionNote` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionReason` longtext COLLATE utf8mb4_unicode_ci,
  `ScannedCode` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityAfter` longtext COLLATE utf8mb4_unicode_ci,
  `Sku` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityBefore` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionDate` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190315_162731_679`
--

DROP TABLE IF EXISTS `anv_temp_0_20190315_162731_679`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190315_162731_679` (
  `User` longtext COLLATE utf8mb4_unicode_ci,
  `Sku` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `ScannedCode` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityBefore` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityAfter` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionType` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionReason` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionNote` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionDate` longtext COLLATE utf8mb4_unicode_ci,
  `Context` longtext COLLATE utf8mb4_unicode_ci,
  `Context_Type` longtext COLLATE utf8mb4_unicode_ci,
  `Context_ID` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190319_111834_836`
--

DROP TABLE IF EXISTS `anv_temp_0_20190319_111834_836`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190319_111834_836` (
  `User` longtext COLLATE utf8mb4_unicode_ci,
  `Sku` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `ScannedCode` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityBefore` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityAfter` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionType` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionReason` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionNote` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionDate` longtext COLLATE utf8mb4_unicode_ci,
  `Context` longtext COLLATE utf8mb4_unicode_ci,
  `Context_Type` longtext COLLATE utf8mb4_unicode_ci,
  `Context_ID` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190319_180218_600`
--

DROP TABLE IF EXISTS `anv_temp_0_20190319_180218_600`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190319_180218_600` (
  `Account` longtext COLLATE utf8mb4_unicode_ci,
  `AccountCode` longtext COLLATE utf8mb4_unicode_ci,
  `AccountName` longtext COLLATE utf8mb4_unicode_ci,
  `AmountDC` longtext COLLATE utf8mb4_unicode_ci,
  `AmountFC` longtext COLLATE utf8mb4_unicode_ci,
  `AmountVATFC` longtext COLLATE utf8mb4_unicode_ci,
  `AmountVATBaseFC` longtext COLLATE utf8mb4_unicode_ci,
  `Asset` longtext COLLATE utf8mb4_unicode_ci,
  `AssetCode` longtext COLLATE utf8mb4_unicode_ci,
  `AssetDescription` longtext COLLATE utf8mb4_unicode_ci,
  `CostCenter` longtext COLLATE utf8mb4_unicode_ci,
  `CostCenterDescription` longtext COLLATE utf8mb4_unicode_ci,
  `CostUnit` longtext COLLATE utf8mb4_unicode_ci,
  `CostUnitDescription` longtext COLLATE utf8mb4_unicode_ci,
  `Created` longtext COLLATE utf8mb4_unicode_ci,
  `Creator` longtext COLLATE utf8mb4_unicode_ci,
  `CreatorFullName` longtext COLLATE utf8mb4_unicode_ci,
  `Currency` longtext COLLATE utf8mb4_unicode_ci,
  `Date` longtext COLLATE utf8mb4_unicode_ci,
  `Description` longtext COLLATE utf8mb4_unicode_ci,
  `Division` longtext COLLATE utf8mb4_unicode_ci,
  `Document` longtext COLLATE utf8mb4_unicode_ci,
  `DocumentNumber` longtext COLLATE utf8mb4_unicode_ci,
  `DocumentSubject` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate` longtext COLLATE utf8mb4_unicode_ci,
  `EntryID` longtext COLLATE utf8mb4_unicode_ci,
  `EntryNumber` longtext COLLATE utf8mb4_unicode_ci,
  `ExchangeRate` longtext COLLATE utf8mb4_unicode_ci,
  `ExtraDutyAmountFC` longtext COLLATE utf8mb4_unicode_ci,
  `ExtraDutyPercentage` longtext COLLATE utf8mb4_unicode_ci,
  `FinancialPeriod` longtext COLLATE utf8mb4_unicode_ci,
  `FinancialYear` longtext COLLATE utf8mb4_unicode_ci,
  `GLAccount` longtext COLLATE utf8mb4_unicode_ci,
  `GLAccountCode` longtext COLLATE utf8mb4_unicode_ci,
  `GLAccountDescription` longtext COLLATE utf8mb4_unicode_ci,
  `ID` longtext COLLATE utf8mb4_unicode_ci,
  `InvoiceNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Item` longtext COLLATE utf8mb4_unicode_ci,
  `ItemCode` longtext COLLATE utf8mb4_unicode_ci,
  `ItemDescription` longtext COLLATE utf8mb4_unicode_ci,
  `JournalCode` longtext COLLATE utf8mb4_unicode_ci,
  `JournalDescription` longtext COLLATE utf8mb4_unicode_ci,
  `LineNumber` longtext COLLATE utf8mb4_unicode_ci,
  `LineType` longtext COLLATE utf8mb4_unicode_ci,
  `Modified` longtext COLLATE utf8mb4_unicode_ci,
  `Modifier` longtext COLLATE utf8mb4_unicode_ci,
  `ModifierFullName` longtext COLLATE utf8mb4_unicode_ci,
  `Notes` longtext COLLATE utf8mb4_unicode_ci,
  `OffsetID` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNumber` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentDiscountAmount` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentReference` longtext COLLATE utf8mb4_unicode_ci,
  `Project` longtext COLLATE utf8mb4_unicode_ci,
  `ProjectCode` longtext COLLATE utf8mb4_unicode_ci,
  `ProjectDescription` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `SerialNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Status` longtext COLLATE utf8mb4_unicode_ci,
  `Subscription` longtext COLLATE utf8mb4_unicode_ci,
  `SubscriptionDescription` longtext COLLATE utf8mb4_unicode_ci,
  `TrackingNumber` longtext COLLATE utf8mb4_unicode_ci,
  `TrackingNumberDescription` longtext COLLATE utf8mb4_unicode_ci,
  `Type` longtext COLLATE utf8mb4_unicode_ci,
  `VATCode` longtext COLLATE utf8mb4_unicode_ci,
  `VATCodeDescription` longtext COLLATE utf8mb4_unicode_ci,
  `VATPercentage` longtext COLLATE utf8mb4_unicode_ci,
  `VATType` longtext COLLATE utf8mb4_unicode_ci,
  `YourRef` longtext COLLATE utf8mb4_unicode_ci,
  `__metadata_uri` longtext COLLATE utf8mb4_unicode_ci,
  `__metadata_type` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190319_182953_70`
--

DROP TABLE IF EXISTS `anv_temp_0_20190319_182953_70`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190319_182953_70` (
  `cod` longtext COLLATE utf8mb4_unicode_ci,
  `calctime` longtext COLLATE utf8mb4_unicode_ci,
  `cnt` longtext COLLATE utf8mb4_unicode_ci,
  `list_id` longtext COLLATE utf8mb4_unicode_ci,
  `list_name` longtext COLLATE utf8mb4_unicode_ci,
  `list_dt` longtext COLLATE utf8mb4_unicode_ci,
  `list_coord_lon` longtext COLLATE utf8mb4_unicode_ci,
  `list_coord_lat` longtext COLLATE utf8mb4_unicode_ci,
  `list_main_temp` longtext COLLATE utf8mb4_unicode_ci,
  `list_main_temp_min` longtext COLLATE utf8mb4_unicode_ci,
  `list_main_temp_max` longtext COLLATE utf8mb4_unicode_ci,
  `list_main_pressure` longtext COLLATE utf8mb4_unicode_ci,
  `list_main_sea_level` longtext COLLATE utf8mb4_unicode_ci,
  `list_main_grnd_level` longtext COLLATE utf8mb4_unicode_ci,
  `list_main_humidity` longtext COLLATE utf8mb4_unicode_ci,
  `list_wind_speed` longtext COLLATE utf8mb4_unicode_ci,
  `list_wind_deg` longtext COLLATE utf8mb4_unicode_ci,
  `list_rain_3h` longtext COLLATE utf8mb4_unicode_ci,
  `list_clouds_all` longtext COLLATE utf8mb4_unicode_ci,
  `list_weather_id` longtext COLLATE utf8mb4_unicode_ci,
  `list_weather_main` longtext COLLATE utf8mb4_unicode_ci,
  `list_weather_description` longtext COLLATE utf8mb4_unicode_ci,
  `list_weather_icon` longtext COLLATE utf8mb4_unicode_ci,
  `list_wind_var_beg` longtext COLLATE utf8mb4_unicode_ci,
  `list_wind_var_end` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190325_124628_287`
--

DROP TABLE IF EXISTS `anv_temp_0_20190325_124628_287`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190325_124628_287` (
  `Id` text COLLATE utf8mb4_unicode_ci,
  `Code` text COLLATE utf8mb4_unicode_ci,
  `Sku` text COLLATE utf8mb4_unicode_ci,
  `PartNumber` text COLLATE utf8mb4_unicode_ci,
  `Description` text COLLATE utf8mb4_unicode_ci,
  `ShortDescription` text COLLATE utf8mb4_unicode_ci,
  `LongDescription` text COLLATE utf8mb4_unicode_ci,
  `Cost` text COLLATE utf8mb4_unicode_ci,
  `RetailPrice` text COLLATE utf8mb4_unicode_ci,
  `SalePrice` text COLLATE utf8mb4_unicode_ci,
  `WeightValue` text COLLATE utf8mb4_unicode_ci,
  `WeightUnit` text COLLATE utf8mb4_unicode_ci,
  `ReorderPoint` text COLLATE utf8mb4_unicode_ci,
  `Brand` text COLLATE utf8mb4_unicode_ci,
  `Supplier` text COLLATE utf8mb4_unicode_ci,
  `Classification` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHand` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHold` text COLLATE utf8mb4_unicode_ci,
  `QuantityPicked` text COLLATE utf8mb4_unicode_ci,
  `QuantityPending` text COLLATE utf8mb4_unicode_ci,
  `QuantityAvailable` text COLLATE utf8mb4_unicode_ci,
  `QuantityIncoming` text COLLATE utf8mb4_unicode_ci,
  `QuantityInbound` text COLLATE utf8mb4_unicode_ci,
  `QuantityTransfer` text COLLATE utf8mb4_unicode_ci,
  `QuantityInStock` text COLLATE utf8mb4_unicode_ci,
  `QuantityTotalFBA` text COLLATE utf8mb4_unicode_ci,
  `CreatedDateUtc` text COLLATE utf8mb4_unicode_ci,
  `ModifiedDateUtc` text COLLATE utf8mb4_unicode_ci,
  `VariationParentSku` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateSKU` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateCode` text COLLATE utf8mb4_unicode_ci,
  `MOQ` text COLLATE utf8mb4_unicode_ci,
  `MOQInfo` text COLLATE utf8mb4_unicode_ci,
  `IncrementalQuantity` text COLLATE utf8mb4_unicode_ci,
  `DisableQuantitySync` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierName` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierPartNumber` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_Cost` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_LeadTime` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsActive` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsPrimary` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Name` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Value` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190325_124636_980`
--

DROP TABLE IF EXISTS `anv_temp_0_20190325_124636_980`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190325_124636_980` (
  `Id` text COLLATE utf8mb4_unicode_ci,
  `Code` text COLLATE utf8mb4_unicode_ci,
  `Sku` text COLLATE utf8mb4_unicode_ci,
  `PartNumber` text COLLATE utf8mb4_unicode_ci,
  `Description` text COLLATE utf8mb4_unicode_ci,
  `ShortDescription` text COLLATE utf8mb4_unicode_ci,
  `LongDescription` text COLLATE utf8mb4_unicode_ci,
  `Cost` text COLLATE utf8mb4_unicode_ci,
  `RetailPrice` text COLLATE utf8mb4_unicode_ci,
  `SalePrice` text COLLATE utf8mb4_unicode_ci,
  `WeightValue` text COLLATE utf8mb4_unicode_ci,
  `WeightUnit` text COLLATE utf8mb4_unicode_ci,
  `ReorderPoint` text COLLATE utf8mb4_unicode_ci,
  `Brand` text COLLATE utf8mb4_unicode_ci,
  `Supplier` text COLLATE utf8mb4_unicode_ci,
  `Classification` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHand` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHold` text COLLATE utf8mb4_unicode_ci,
  `QuantityPicked` text COLLATE utf8mb4_unicode_ci,
  `QuantityPending` text COLLATE utf8mb4_unicode_ci,
  `QuantityAvailable` text COLLATE utf8mb4_unicode_ci,
  `QuantityIncoming` text COLLATE utf8mb4_unicode_ci,
  `QuantityInbound` text COLLATE utf8mb4_unicode_ci,
  `QuantityTransfer` text COLLATE utf8mb4_unicode_ci,
  `QuantityInStock` text COLLATE utf8mb4_unicode_ci,
  `QuantityTotalFBA` text COLLATE utf8mb4_unicode_ci,
  `CreatedDateUtc` text COLLATE utf8mb4_unicode_ci,
  `ModifiedDateUtc` text COLLATE utf8mb4_unicode_ci,
  `VariationParentSku` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateSKU` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateCode` text COLLATE utf8mb4_unicode_ci,
  `MOQ` text COLLATE utf8mb4_unicode_ci,
  `MOQInfo` text COLLATE utf8mb4_unicode_ci,
  `IncrementalQuantity` text COLLATE utf8mb4_unicode_ci,
  `DisableQuantitySync` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierName` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierPartNumber` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_Cost` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_LeadTime` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsActive` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsPrimary` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Name` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Value` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190325_175312_484`
--

DROP TABLE IF EXISTS `anv_temp_0_20190325_175312_484`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190325_175312_484` (
  `AssimilatedVATBox` text COLLATE utf8mb4_unicode_ci,
  `BalanceSide` text COLLATE utf8mb4_unicode_ci,
  `BalanceType` text COLLATE utf8mb4_unicode_ci,
  `BelcotaxType` text COLLATE utf8mb4_unicode_ci,
  `Code` text COLLATE utf8mb4_unicode_ci,
  `Compress` text COLLATE utf8mb4_unicode_ci,
  `Costcenter` text COLLATE utf8mb4_unicode_ci,
  `CostcenterDescription` text COLLATE utf8mb4_unicode_ci,
  `Costunit` text COLLATE utf8mb4_unicode_ci,
  `CostunitDescription` text COLLATE utf8mb4_unicode_ci,
  `Created` text COLLATE utf8mb4_unicode_ci,
  `Creator` text COLLATE utf8mb4_unicode_ci,
  `CreatorFullName` text COLLATE utf8mb4_unicode_ci,
  `Description` text COLLATE utf8mb4_unicode_ci,
  `Division` text COLLATE utf8mb4_unicode_ci,
  `ExcludeVATListing` text COLLATE utf8mb4_unicode_ci,
  `ExpenseNonDeductiblePercentage` text COLLATE utf8mb4_unicode_ci,
  `ID` text COLLATE utf8mb4_unicode_ci,
  `IsBlocked` text COLLATE utf8mb4_unicode_ci,
  `Matching` text COLLATE utf8mb4_unicode_ci,
  `Modified` text COLLATE utf8mb4_unicode_ci,
  `Modifier` text COLLATE utf8mb4_unicode_ci,
  `ModifierFullName` text COLLATE utf8mb4_unicode_ci,
  `PrivateGLAccount` text COLLATE utf8mb4_unicode_ci,
  `PrivatePercentage` text COLLATE utf8mb4_unicode_ci,
  `ReportingCode` text COLLATE utf8mb4_unicode_ci,
  `RevalueCurrency` text COLLATE utf8mb4_unicode_ci,
  `SearchCode` text COLLATE utf8mb4_unicode_ci,
  `Type` text COLLATE utf8mb4_unicode_ci,
  `TypeDescription` text COLLATE utf8mb4_unicode_ci,
  `UseCostcenter` text COLLATE utf8mb4_unicode_ci,
  `UseCostunit` text COLLATE utf8mb4_unicode_ci,
  `VATCode` text COLLATE utf8mb4_unicode_ci,
  `VATDescription` text COLLATE utf8mb4_unicode_ci,
  `VATGLAccountType` text COLLATE utf8mb4_unicode_ci,
  `VATNonDeductibleGLAccount` text COLLATE utf8mb4_unicode_ci,
  `VATNonDeductiblePercentage` text COLLATE utf8mb4_unicode_ci,
  `VATSystem` text COLLATE utf8mb4_unicode_ci,
  `YearEndCostGLAccount` text COLLATE utf8mb4_unicode_ci,
  `YearEndReflectionGLAccount` text COLLATE utf8mb4_unicode_ci,
  `__metadata_uri` text COLLATE utf8mb4_unicode_ci,
  `__metadata_type` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190327_124303_13`
--

DROP TABLE IF EXISTS `anv_temp_0_20190327_124303_13`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190327_124303_13` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190327_153739_561`
--

DROP TABLE IF EXISTS `anv_temp_0_20190327_153739_561`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190327_153739_561` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190327_173306_673`
--

DROP TABLE IF EXISTS `anv_temp_0_20190327_173306_673`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190327_173306_673` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190327_173336_869`
--

DROP TABLE IF EXISTS `anv_temp_0_20190327_173336_869`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190327_173336_869` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190327_190621_423`
--

DROP TABLE IF EXISTS `anv_temp_0_20190327_190621_423`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190327_190621_423` (
  `Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190328_131045_201`
--

DROP TABLE IF EXISTS `anv_temp_0_20190328_131045_201`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190328_131045_201` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ApprovedForPayment_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReferenceNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Terms_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Vendor_value` longtext COLLATE utf8mb4_unicode_ci,
  `VendorRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ExtendedCost_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Qty_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Subaccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescription_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitCost_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_POReceiptNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_POReceiptLine_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_POLine_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_POOrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_POOrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `Details_files_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_files_href` longtext COLLATE utf8mb4_unicode_ci,
  `Details_files_filename` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190404_163025_945`
--

DROP TABLE IF EXISTS `anv_temp_0_20190404_163025_945`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190404_163025_945` (
  `AmountDC` text COLLATE utf8mb4_unicode_ci,
  `AmountDiscount` text COLLATE utf8mb4_unicode_ci,
  `AmountDiscountExclVat` text COLLATE utf8mb4_unicode_ci,
  `AmountFC` text COLLATE utf8mb4_unicode_ci,
  `AmountFCExclVat` text COLLATE utf8mb4_unicode_ci,
  `ApprovalStatus` text COLLATE utf8mb4_unicode_ci,
  `ApprovalStatusDescription` text COLLATE utf8mb4_unicode_ci,
  `Approved` text COLLATE utf8mb4_unicode_ci,
  `Approver` text COLLATE utf8mb4_unicode_ci,
  `ApproverFullName` text COLLATE utf8mb4_unicode_ci,
  `Created` text COLLATE utf8mb4_unicode_ci,
  `Creator` text COLLATE utf8mb4_unicode_ci,
  `CreatorFullName` text COLLATE utf8mb4_unicode_ci,
  `Currency` text COLLATE utf8mb4_unicode_ci,
  `DeliverTo` text COLLATE utf8mb4_unicode_ci,
  `DeliverToContactPerson` text COLLATE utf8mb4_unicode_ci,
  `DeliverToContactPersonFullName` text COLLATE utf8mb4_unicode_ci,
  `DeliverToName` text COLLATE utf8mb4_unicode_ci,
  `DeliveryDate` text COLLATE utf8mb4_unicode_ci,
  `DeliveryStatus` text COLLATE utf8mb4_unicode_ci,
  `DeliveryStatusDescription` text COLLATE utf8mb4_unicode_ci,
  `DeliveryAddress` text COLLATE utf8mb4_unicode_ci,
  `Description` text COLLATE utf8mb4_unicode_ci,
  `Discount` text COLLATE utf8mb4_unicode_ci,
  `Division` text COLLATE utf8mb4_unicode_ci,
  `Document` text COLLATE utf8mb4_unicode_ci,
  `DocumentNumber` text COLLATE utf8mb4_unicode_ci,
  `DocumentSubject` text COLLATE utf8mb4_unicode_ci,
  `InvoiceStatus` text COLLATE utf8mb4_unicode_ci,
  `InvoiceStatusDescription` text COLLATE utf8mb4_unicode_ci,
  `InvoiceTo` text COLLATE utf8mb4_unicode_ci,
  `InvoiceToContactPerson` text COLLATE utf8mb4_unicode_ci,
  `InvoiceToContactPersonFullName` text COLLATE utf8mb4_unicode_ci,
  `InvoiceToName` text COLLATE utf8mb4_unicode_ci,
  `Modified` text COLLATE utf8mb4_unicode_ci,
  `Modifier` text COLLATE utf8mb4_unicode_ci,
  `ModifierFullName` text COLLATE utf8mb4_unicode_ci,
  `OrderDate` text COLLATE utf8mb4_unicode_ci,
  `OrderedBy` text COLLATE utf8mb4_unicode_ci,
  `OrderedByContactPerson` text COLLATE utf8mb4_unicode_ci,
  `OrderedByContactPersonFullName` text COLLATE utf8mb4_unicode_ci,
  `OrderedByName` text COLLATE utf8mb4_unicode_ci,
  `OrderID` text COLLATE utf8mb4_unicode_ci,
  `OrderNumber` text COLLATE utf8mb4_unicode_ci,
  `PaymentCondition` text COLLATE utf8mb4_unicode_ci,
  `PaymentConditionDescription` text COLLATE utf8mb4_unicode_ci,
  `PaymentReference` text COLLATE utf8mb4_unicode_ci,
  `Remarks` text COLLATE utf8mb4_unicode_ci,
  `Salesperson` text COLLATE utf8mb4_unicode_ci,
  `SalespersonFullName` text COLLATE utf8mb4_unicode_ci,
  `ShippingMethod` text COLLATE utf8mb4_unicode_ci,
  `ShippingMethodDescription` text COLLATE utf8mb4_unicode_ci,
  `Status` text COLLATE utf8mb4_unicode_ci,
  `StatusDescription` text COLLATE utf8mb4_unicode_ci,
  `TaxSchedule` text COLLATE utf8mb4_unicode_ci,
  `TaxScheduleCode` text COLLATE utf8mb4_unicode_ci,
  `TaxScheduleDescription` text COLLATE utf8mb4_unicode_ci,
  `WarehouseID` text COLLATE utf8mb4_unicode_ci,
  `WarehouseCode` text COLLATE utf8mb4_unicode_ci,
  `WarehouseDescription` text COLLATE utf8mb4_unicode_ci,
  `YourRef` text COLLATE utf8mb4_unicode_ci,
  `__metadata_uri` text COLLATE utf8mb4_unicode_ci,
  `__metadata_type` text COLLATE utf8mb4_unicode_ci,
  `SalesOrderLines___deferred_uri` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190404_163027_528`
--

DROP TABLE IF EXISTS `anv_temp_0_20190404_163027_528`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190404_163027_528` (
  `AmountDC` text COLLATE utf8mb4_unicode_ci,
  `AmountDiscount` text COLLATE utf8mb4_unicode_ci,
  `AmountDiscountExclVat` text COLLATE utf8mb4_unicode_ci,
  `AmountFC` text COLLATE utf8mb4_unicode_ci,
  `AmountFCExclVat` text COLLATE utf8mb4_unicode_ci,
  `ApprovalStatus` text COLLATE utf8mb4_unicode_ci,
  `ApprovalStatusDescription` text COLLATE utf8mb4_unicode_ci,
  `Approved` text COLLATE utf8mb4_unicode_ci,
  `Approver` text COLLATE utf8mb4_unicode_ci,
  `ApproverFullName` text COLLATE utf8mb4_unicode_ci,
  `Created` text COLLATE utf8mb4_unicode_ci,
  `Creator` text COLLATE utf8mb4_unicode_ci,
  `CreatorFullName` text COLLATE utf8mb4_unicode_ci,
  `Currency` text COLLATE utf8mb4_unicode_ci,
  `DeliverTo` text COLLATE utf8mb4_unicode_ci,
  `DeliverToContactPerson` text COLLATE utf8mb4_unicode_ci,
  `DeliverToContactPersonFullName` text COLLATE utf8mb4_unicode_ci,
  `DeliverToName` text COLLATE utf8mb4_unicode_ci,
  `DeliveryDate` text COLLATE utf8mb4_unicode_ci,
  `DeliveryStatus` text COLLATE utf8mb4_unicode_ci,
  `DeliveryStatusDescription` text COLLATE utf8mb4_unicode_ci,
  `DeliveryAddress` text COLLATE utf8mb4_unicode_ci,
  `Description` text COLLATE utf8mb4_unicode_ci,
  `Discount` text COLLATE utf8mb4_unicode_ci,
  `Division` text COLLATE utf8mb4_unicode_ci,
  `Document` text COLLATE utf8mb4_unicode_ci,
  `DocumentNumber` text COLLATE utf8mb4_unicode_ci,
  `DocumentSubject` text COLLATE utf8mb4_unicode_ci,
  `InvoiceStatus` text COLLATE utf8mb4_unicode_ci,
  `InvoiceStatusDescription` text COLLATE utf8mb4_unicode_ci,
  `InvoiceTo` text COLLATE utf8mb4_unicode_ci,
  `InvoiceToContactPerson` text COLLATE utf8mb4_unicode_ci,
  `InvoiceToContactPersonFullName` text COLLATE utf8mb4_unicode_ci,
  `InvoiceToName` text COLLATE utf8mb4_unicode_ci,
  `Modified` text COLLATE utf8mb4_unicode_ci,
  `Modifier` text COLLATE utf8mb4_unicode_ci,
  `ModifierFullName` text COLLATE utf8mb4_unicode_ci,
  `OrderDate` text COLLATE utf8mb4_unicode_ci,
  `OrderedBy` text COLLATE utf8mb4_unicode_ci,
  `OrderedByContactPerson` text COLLATE utf8mb4_unicode_ci,
  `OrderedByContactPersonFullName` text COLLATE utf8mb4_unicode_ci,
  `OrderedByName` text COLLATE utf8mb4_unicode_ci,
  `OrderID` text COLLATE utf8mb4_unicode_ci,
  `OrderNumber` text COLLATE utf8mb4_unicode_ci,
  `PaymentCondition` text COLLATE utf8mb4_unicode_ci,
  `PaymentConditionDescription` text COLLATE utf8mb4_unicode_ci,
  `PaymentReference` text COLLATE utf8mb4_unicode_ci,
  `Remarks` text COLLATE utf8mb4_unicode_ci,
  `Salesperson` text COLLATE utf8mb4_unicode_ci,
  `SalespersonFullName` text COLLATE utf8mb4_unicode_ci,
  `ShippingMethod` text COLLATE utf8mb4_unicode_ci,
  `ShippingMethodDescription` text COLLATE utf8mb4_unicode_ci,
  `Status` text COLLATE utf8mb4_unicode_ci,
  `StatusDescription` text COLLATE utf8mb4_unicode_ci,
  `TaxSchedule` text COLLATE utf8mb4_unicode_ci,
  `TaxScheduleCode` text COLLATE utf8mb4_unicode_ci,
  `TaxScheduleDescription` text COLLATE utf8mb4_unicode_ci,
  `WarehouseID` text COLLATE utf8mb4_unicode_ci,
  `WarehouseCode` text COLLATE utf8mb4_unicode_ci,
  `WarehouseDescription` text COLLATE utf8mb4_unicode_ci,
  `YourRef` text COLLATE utf8mb4_unicode_ci,
  `__metadata_uri` text COLLATE utf8mb4_unicode_ci,
  `__metadata_type` text COLLATE utf8mb4_unicode_ci,
  `SalesOrderLines___deferred_uri` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190424_181817_747`
--

DROP TABLE IF EXISTS `anv_temp_0_20190424_181817_747`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190424_181817_747` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190424_184612_394`
--

DROP TABLE IF EXISTS `anv_temp_0_20190424_184612_394`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190424_184612_394` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190425_115715_339`
--

DROP TABLE IF EXISTS `anv_temp_0_20190425_115715_339`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190425_115715_339` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190425_121936_579`
--

DROP TABLE IF EXISTS `anv_temp_0_20190425_121936_579`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190425_121936_579` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190425_152240_361`
--

DROP TABLE IF EXISTS `anv_temp_0_20190425_152240_361`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190425_152240_361` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Approved_value` longtext COLLATE utf8mb4_unicode_ci,
  `BaseCurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ControlTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `EffectiveDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `ExternalRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `IsTaxValid_value` longtext COLLATE utf8mb4_unicode_ci,
  `LastModified_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `NewCard_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderedQty_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentCardIdentifier_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentMethod_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreAuthorizedAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreferredWarehouseID_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReciprocalRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `RequestedOn_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipVia_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190425_153004_629`
--

DROP TABLE IF EXISTS `anv_temp_0_20190425_153004_629`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190425_153004_629` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Approved_value` longtext COLLATE utf8mb4_unicode_ci,
  `BaseCurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ControlTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `EffectiveDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `ExternalRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `IsTaxValid_value` longtext COLLATE utf8mb4_unicode_ci,
  `LastModified_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `NewCard_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderedQty_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentCardIdentifier_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentMethod_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreAuthorizedAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreferredWarehouseID_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReciprocalRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `RequestedOn_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipVia_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190425_175435_64`
--

DROP TABLE IF EXISTS `anv_temp_0_20190425_175435_64`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190425_175435_64` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Approved_value` longtext COLLATE utf8mb4_unicode_ci,
  `BaseCurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ControlTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `EffectiveDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `ExternalRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `IsTaxValid_value` longtext COLLATE utf8mb4_unicode_ci,
  `LastModified_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `NewCard_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderedQty_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentCardIdentifier_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentMethod_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreAuthorizedAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreferredWarehouseID_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReciprocalRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `RequestedOn_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipVia_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_120522_696`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_120522_696`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_120522_696` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_120551_42`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_120551_42`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_120551_42` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Approved_value` longtext COLLATE utf8mb4_unicode_ci,
  `BaseCurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ControlTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `EffectiveDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `ExternalRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `IsTaxValid_value` longtext COLLATE utf8mb4_unicode_ci,
  `LastModified_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `NewCard_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderedQty_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentCardIdentifier_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentMethod_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreAuthorizedAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreferredWarehouseID_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReciprocalRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `RequestedOn_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipVia_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_124011_690`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_124011_690`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_124011_690` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_124018_275`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_124018_275`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_124018_275` (
  `User` longtext COLLATE utf8mb4_unicode_ci,
  `Sku` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `ScannedCode` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityBefore` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityAfter` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionType` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionReason` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionNote` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionDate` longtext COLLATE utf8mb4_unicode_ci,
  `Context` longtext COLLATE utf8mb4_unicode_ci,
  `Context_Type` longtext COLLATE utf8mb4_unicode_ci,
  `Context_ID` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_124030_805`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_124030_805`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_124030_805` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Approved_value` longtext COLLATE utf8mb4_unicode_ci,
  `BaseCurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ControlTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `EffectiveDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `ExternalRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `IsTaxValid_value` longtext COLLATE utf8mb4_unicode_ci,
  `LastModified_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `NewCard_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderedQty_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentCardIdentifier_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentMethod_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreAuthorizedAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreferredWarehouseID_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReciprocalRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `RequestedOn_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipVia_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_125345_156`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_125345_156`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_125345_156` (
  `User` longtext COLLATE utf8mb4_unicode_ci,
  `Sku` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `ScannedCode` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityBefore` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityAfter` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionType` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionReason` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionNote` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionDate` longtext COLLATE utf8mb4_unicode_ci,
  `Context` longtext COLLATE utf8mb4_unicode_ci,
  `Context_Type` longtext COLLATE utf8mb4_unicode_ci,
  `Context_ID` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_130312_205`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_130312_205`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_130312_205` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Balance_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Currency_value` longtext COLLATE utf8mb4_unicode_ci,
  `CuryViewState_value` longtext COLLATE utf8mb4_unicode_ci,
  `Customer_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `DiscountTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `PostPeriod_value` longtext COLLATE utf8mb4_unicode_ci,
  `Type_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_id` longtext COLLATE utf8mb4_unicode_ci,
  `Details_rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Details_note` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Account_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Amount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Branch_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Description_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_DiscountAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_InventoryID_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LineType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Location_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_LotSerialNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ManualDiscount_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvType_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Quantity_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_ShipmentNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TaxCategory_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_TransactionDescr_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UnitPrice_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_UOM_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_Warehouse_value` longtext COLLATE utf8mb4_unicode_ci,
  `Details_OrigInvLineNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `DueDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashDiscountDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `files_filename` longtext COLLATE utf8mb4_unicode_ci,
  `files_href` longtext COLLATE utf8mb4_unicode_ci,
  `files_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_131044_829`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_131044_829`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_131044_829` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Approved_value` longtext COLLATE utf8mb4_unicode_ci,
  `BaseCurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ControlTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `EffectiveDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `ExternalRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `IsTaxValid_value` longtext COLLATE utf8mb4_unicode_ci,
  `LastModified_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `NewCard_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderedQty_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentCardIdentifier_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentMethod_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreAuthorizedAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreferredWarehouseID_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReciprocalRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `RequestedOn_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipVia_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_132258_916`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_132258_916`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_132258_916` (
  `User` longtext COLLATE utf8mb4_unicode_ci,
  `Sku` longtext COLLATE utf8mb4_unicode_ci,
  `Code` longtext COLLATE utf8mb4_unicode_ci,
  `ScannedCode` longtext COLLATE utf8mb4_unicode_ci,
  `LotNumber` longtext COLLATE utf8mb4_unicode_ci,
  `Title` longtext COLLATE utf8mb4_unicode_ci,
  `Quantity` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityBefore` longtext COLLATE utf8mb4_unicode_ci,
  `QuantityAfter` longtext COLLATE utf8mb4_unicode_ci,
  `Location` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionType` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionReason` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionNote` longtext COLLATE utf8mb4_unicode_ci,
  `TransactionDate` longtext COLLATE utf8mb4_unicode_ci,
  `Context` longtext COLLATE utf8mb4_unicode_ci,
  `Context_Type` longtext COLLATE utf8mb4_unicode_ci,
  `Context_ID` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190426_132331_457`
--

DROP TABLE IF EXISTS `anv_temp_0_20190426_132331_457`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190426_132331_457` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `rowNumber` longtext COLLATE utf8mb4_unicode_ci,
  `note` longtext COLLATE utf8mb4_unicode_ci,
  `Approved_value` longtext COLLATE utf8mb4_unicode_ci,
  `BaseCurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `BillToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `CashAccount_value` longtext COLLATE utf8mb4_unicode_ci,
  `ControlTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `CreditHold_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CurrencyRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerID_value` longtext COLLATE utf8mb4_unicode_ci,
  `CustomerOrder_value` longtext COLLATE utf8mb4_unicode_ci,
  `Date_value` longtext COLLATE utf8mb4_unicode_ci,
  `EffectiveDate_value` longtext COLLATE utf8mb4_unicode_ci,
  `ExternalRef_value` longtext COLLATE utf8mb4_unicode_ci,
  `Hold_value` longtext COLLATE utf8mb4_unicode_ci,
  `IsTaxValid_value` longtext COLLATE utf8mb4_unicode_ci,
  `LastModified_value` longtext COLLATE utf8mb4_unicode_ci,
  `LocationID_value` longtext COLLATE utf8mb4_unicode_ci,
  `NewCard_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderedQty_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderNbr_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderTotal_value` longtext COLLATE utf8mb4_unicode_ci,
  `OrderType_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentCardIdentifier_value` longtext COLLATE utf8mb4_unicode_ci,
  `PaymentMethod_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreAuthorizedAmount_value` longtext COLLATE utf8mb4_unicode_ci,
  `PreferredWarehouseID_value` longtext COLLATE utf8mb4_unicode_ci,
  `ReciprocalRate_value` longtext COLLATE utf8mb4_unicode_ci,
  `RequestedOn_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToAddressOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipToContactOverride_value` longtext COLLATE utf8mb4_unicode_ci,
  `ShipVia_value` longtext COLLATE utf8mb4_unicode_ci,
  `Status_value` longtext COLLATE utf8mb4_unicode_ci,
  `TaxTotal_value` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190517_144031_958`
--

DROP TABLE IF EXISTS `anv_temp_0_20190517_144031_958`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190517_144031_958` (
  `562_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `562_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `562_sku` longtext COLLATE utf8mb4_unicode_ci,
  `562_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `562_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `562_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `562_name` longtext COLLATE utf8mb4_unicode_ci,
  `562_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `562_gender` longtext COLLATE utf8mb4_unicode_ci,
  `562_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `562_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `562_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `562_description` longtext COLLATE utf8mb4_unicode_ci,
  `562_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `562_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `562_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `562_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `565_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `565_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `565_sku` longtext COLLATE utf8mb4_unicode_ci,
  `565_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `565_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `565_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `565_name` longtext COLLATE utf8mb4_unicode_ci,
  `565_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `565_gender` longtext COLLATE utf8mb4_unicode_ci,
  `565_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `565_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `565_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `565_description` longtext COLLATE utf8mb4_unicode_ci,
  `565_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `565_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `565_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `565_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `568_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `568_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `568_sku` longtext COLLATE utf8mb4_unicode_ci,
  `568_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `568_name` longtext COLLATE utf8mb4_unicode_ci,
  `568_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `568_gender` longtext COLLATE utf8mb4_unicode_ci,
  `568_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `568_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `568_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `568_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `568_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `568_description` longtext COLLATE utf8mb4_unicode_ci,
  `568_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `568_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `568_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `568_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `569_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `569_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `569_sku` longtext COLLATE utf8mb4_unicode_ci,
  `569_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `569_name` longtext COLLATE utf8mb4_unicode_ci,
  `569_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `569_gender` longtext COLLATE utf8mb4_unicode_ci,
  `569_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `569_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `569_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `569_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `569_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `569_description` longtext COLLATE utf8mb4_unicode_ci,
  `569_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `569_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `569_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `569_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `570_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `570_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `570_sku` longtext COLLATE utf8mb4_unicode_ci,
  `570_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `570_name` longtext COLLATE utf8mb4_unicode_ci,
  `570_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `570_gender` longtext COLLATE utf8mb4_unicode_ci,
  `570_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `570_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `570_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `570_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `570_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `570_description` longtext COLLATE utf8mb4_unicode_ci,
  `570_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `570_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `570_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `570_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `575_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `575_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `575_sku` longtext COLLATE utf8mb4_unicode_ci,
  `575_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `575_name` longtext COLLATE utf8mb4_unicode_ci,
  `575_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `575_gender` longtext COLLATE utf8mb4_unicode_ci,
  `575_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `575_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `575_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `575_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `575_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `575_description` longtext COLLATE utf8mb4_unicode_ci,
  `575_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `575_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `575_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `575_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `578_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `578_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `578_sku` longtext COLLATE utf8mb4_unicode_ci,
  `578_gender` longtext COLLATE utf8mb4_unicode_ci,
  `578_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `578_name` longtext COLLATE utf8mb4_unicode_ci,
  `578_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `578_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `578_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `578_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `578_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `578_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `578_description` longtext COLLATE utf8mb4_unicode_ci,
  `578_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `578_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `578_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `578_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `582_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `582_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `582_sku` longtext COLLATE utf8mb4_unicode_ci,
  `582_gender` longtext COLLATE utf8mb4_unicode_ci,
  `582_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `582_name` longtext COLLATE utf8mb4_unicode_ci,
  `582_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `582_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `582_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `582_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `582_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `582_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `582_description` longtext COLLATE utf8mb4_unicode_ci,
  `582_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `582_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `582_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `582_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `592_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `592_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `592_sku` longtext COLLATE utf8mb4_unicode_ci,
  `592_gender` longtext COLLATE utf8mb4_unicode_ci,
  `592_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `592_name` longtext COLLATE utf8mb4_unicode_ci,
  `592_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `592_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `592_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `592_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `592_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `592_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `592_description` longtext COLLATE utf8mb4_unicode_ci,
  `592_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `592_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `592_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `592_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `593_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `593_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `593_sku` longtext COLLATE utf8mb4_unicode_ci,
  `593_gender` longtext COLLATE utf8mb4_unicode_ci,
  `593_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `593_name` longtext COLLATE utf8mb4_unicode_ci,
  `593_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `593_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `593_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `593_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `593_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `593_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `593_description` longtext COLLATE utf8mb4_unicode_ci,
  `593_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `593_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `593_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `593_image_url` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_0_20190517_145547_287`
--

DROP TABLE IF EXISTS `anv_temp_0_20190517_145547_287`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_0_20190517_145547_287` (
  `562_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `562_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `562_sku` longtext COLLATE utf8mb4_unicode_ci,
  `562_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `562_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `562_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `562_name` longtext COLLATE utf8mb4_unicode_ci,
  `562_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `562_gender` longtext COLLATE utf8mb4_unicode_ci,
  `562_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `562_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `562_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `562_description` longtext COLLATE utf8mb4_unicode_ci,
  `562_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `562_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `562_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `562_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `562_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `565_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `565_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `565_sku` longtext COLLATE utf8mb4_unicode_ci,
  `565_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `565_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `565_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `565_name` longtext COLLATE utf8mb4_unicode_ci,
  `565_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `565_gender` longtext COLLATE utf8mb4_unicode_ci,
  `565_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `565_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `565_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `565_description` longtext COLLATE utf8mb4_unicode_ci,
  `565_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `565_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `565_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `565_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `565_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `568_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `568_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `568_sku` longtext COLLATE utf8mb4_unicode_ci,
  `568_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `568_name` longtext COLLATE utf8mb4_unicode_ci,
  `568_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `568_gender` longtext COLLATE utf8mb4_unicode_ci,
  `568_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `568_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `568_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `568_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `568_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `568_description` longtext COLLATE utf8mb4_unicode_ci,
  `568_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `568_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `568_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `568_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `568_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `569_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `569_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `569_sku` longtext COLLATE utf8mb4_unicode_ci,
  `569_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `569_name` longtext COLLATE utf8mb4_unicode_ci,
  `569_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `569_gender` longtext COLLATE utf8mb4_unicode_ci,
  `569_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `569_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `569_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `569_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `569_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `569_description` longtext COLLATE utf8mb4_unicode_ci,
  `569_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `569_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `569_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `569_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `569_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `570_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `570_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `570_sku` longtext COLLATE utf8mb4_unicode_ci,
  `570_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `570_name` longtext COLLATE utf8mb4_unicode_ci,
  `570_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `570_gender` longtext COLLATE utf8mb4_unicode_ci,
  `570_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `570_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `570_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `570_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `570_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `570_description` longtext COLLATE utf8mb4_unicode_ci,
  `570_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `570_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `570_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `570_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `570_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `575_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `575_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `575_sku` longtext COLLATE utf8mb4_unicode_ci,
  `575_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `575_name` longtext COLLATE utf8mb4_unicode_ci,
  `575_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `575_gender` longtext COLLATE utf8mb4_unicode_ci,
  `575_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `575_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `575_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `575_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `575_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `575_description` longtext COLLATE utf8mb4_unicode_ci,
  `575_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `575_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `575_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `575_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `575_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `578_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `578_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `578_sku` longtext COLLATE utf8mb4_unicode_ci,
  `578_gender` longtext COLLATE utf8mb4_unicode_ci,
  `578_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `578_name` longtext COLLATE utf8mb4_unicode_ci,
  `578_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `578_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `578_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `578_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `578_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `578_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `578_description` longtext COLLATE utf8mb4_unicode_ci,
  `578_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `578_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `578_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `578_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `578_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `582_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `582_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `582_sku` longtext COLLATE utf8mb4_unicode_ci,
  `582_gender` longtext COLLATE utf8mb4_unicode_ci,
  `582_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `582_name` longtext COLLATE utf8mb4_unicode_ci,
  `582_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `582_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `582_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `582_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `582_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `582_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `582_description` longtext COLLATE utf8mb4_unicode_ci,
  `582_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `582_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `582_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `582_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `582_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `592_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `592_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `592_sku` longtext COLLATE utf8mb4_unicode_ci,
  `592_gender` longtext COLLATE utf8mb4_unicode_ci,
  `592_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `592_name` longtext COLLATE utf8mb4_unicode_ci,
  `592_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `592_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `592_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `592_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `592_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `592_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `592_description` longtext COLLATE utf8mb4_unicode_ci,
  `592_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `592_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `592_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `592_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `592_image_url` longtext COLLATE utf8mb4_unicode_ci,
  `593_entity_id` longtext COLLATE utf8mb4_unicode_ci,
  `593_type_id` longtext COLLATE utf8mb4_unicode_ci,
  `593_sku` longtext COLLATE utf8mb4_unicode_ci,
  `593_gender` longtext COLLATE utf8mb4_unicode_ci,
  `593_international_availability` longtext COLLATE utf8mb4_unicode_ci,
  `593_name` longtext COLLATE utf8mb4_unicode_ci,
  `593_ski_pole_basket_type` longtext COLLATE utf8mb4_unicode_ci,
  `593_meta_title` longtext COLLATE utf8mb4_unicode_ci,
  `593_meta_description` longtext COLLATE utf8mb4_unicode_ci,
  `593_model_year` longtext COLLATE utf8mb4_unicode_ci,
  `593_ski_pole_construction` longtext COLLATE utf8mb4_unicode_ci,
  `593_warranty` longtext COLLATE utf8mb4_unicode_ci,
  `593_description` longtext COLLATE utf8mb4_unicode_ci,
  `593_meta_keyword` longtext COLLATE utf8mb4_unicode_ci,
  `593_short_description` longtext COLLATE utf8mb4_unicode_ci,
  `593_regular_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_regular_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_final_price_with_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_final_price_without_tax` longtext COLLATE utf8mb4_unicode_ci,
  `593_is_saleable` longtext COLLATE utf8mb4_unicode_ci,
  `593_image_url` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_202_20190319_175611_625`
--

DROP TABLE IF EXISTS `anv_temp_202_20190319_175611_625`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_202_20190319_175611_625` (
  `base` text COLLATE utf8mb4_unicode_ci,
  `visibility` text COLLATE utf8mb4_unicode_ci,
  `dt` text COLLATE utf8mb4_unicode_ci,
  `id` text COLLATE utf8mb4_unicode_ci,
  `name` text COLLATE utf8mb4_unicode_ci,
  `cod` text COLLATE utf8mb4_unicode_ci,
  `coord_lon` text COLLATE utf8mb4_unicode_ci,
  `coord_lat` text COLLATE utf8mb4_unicode_ci,
  `main_temp` text COLLATE utf8mb4_unicode_ci,
  `main_pressure` text COLLATE utf8mb4_unicode_ci,
  `main_humidity` text COLLATE utf8mb4_unicode_ci,
  `main_temp_min` text COLLATE utf8mb4_unicode_ci,
  `main_temp_max` text COLLATE utf8mb4_unicode_ci,
  `wind_speed` text COLLATE utf8mb4_unicode_ci,
  `wind_deg` text COLLATE utf8mb4_unicode_ci,
  `wind_gust` text COLLATE utf8mb4_unicode_ci,
  `clouds_all` text COLLATE utf8mb4_unicode_ci,
  `sys_type` text COLLATE utf8mb4_unicode_ci,
  `sys_id` text COLLATE utf8mb4_unicode_ci,
  `sys_message` text COLLATE utf8mb4_unicode_ci,
  `sys_country` text COLLATE utf8mb4_unicode_ci,
  `sys_sunrise` text COLLATE utf8mb4_unicode_ci,
  `sys_sunset` text COLLATE utf8mb4_unicode_ci,
  `weather_id` text COLLATE utf8mb4_unicode_ci,
  `weather_main` text COLLATE utf8mb4_unicode_ci,
  `weather_description` text COLLATE utf8mb4_unicode_ci,
  `weather_icon` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_207_20190325_134434_702`
--

DROP TABLE IF EXISTS `anv_temp_207_20190325_134434_702`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_207_20190325_134434_702` (
  `Id` text COLLATE utf8mb4_unicode_ci,
  `Code` text COLLATE utf8mb4_unicode_ci,
  `Sku` text COLLATE utf8mb4_unicode_ci,
  `PartNumber` text COLLATE utf8mb4_unicode_ci,
  `Description` text COLLATE utf8mb4_unicode_ci,
  `ShortDescription` text COLLATE utf8mb4_unicode_ci,
  `LongDescription` text COLLATE utf8mb4_unicode_ci,
  `Cost` text COLLATE utf8mb4_unicode_ci,
  `RetailPrice` text COLLATE utf8mb4_unicode_ci,
  `SalePrice` text COLLATE utf8mb4_unicode_ci,
  `WeightValue` text COLLATE utf8mb4_unicode_ci,
  `WeightUnit` text COLLATE utf8mb4_unicode_ci,
  `ReorderPoint` text COLLATE utf8mb4_unicode_ci,
  `Brand` text COLLATE utf8mb4_unicode_ci,
  `Supplier` text COLLATE utf8mb4_unicode_ci,
  `Classification` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHand` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHold` text COLLATE utf8mb4_unicode_ci,
  `QuantityPicked` text COLLATE utf8mb4_unicode_ci,
  `QuantityPending` text COLLATE utf8mb4_unicode_ci,
  `QuantityAvailable` text COLLATE utf8mb4_unicode_ci,
  `QuantityIncoming` text COLLATE utf8mb4_unicode_ci,
  `QuantityInbound` text COLLATE utf8mb4_unicode_ci,
  `QuantityTransfer` text COLLATE utf8mb4_unicode_ci,
  `QuantityInStock` text COLLATE utf8mb4_unicode_ci,
  `QuantityTotalFBA` text COLLATE utf8mb4_unicode_ci,
  `CreatedDateUtc` text COLLATE utf8mb4_unicode_ci,
  `ModifiedDateUtc` text COLLATE utf8mb4_unicode_ci,
  `VariationParentSku` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateSKU` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateCode` text COLLATE utf8mb4_unicode_ci,
  `MOQ` text COLLATE utf8mb4_unicode_ci,
  `MOQInfo` text COLLATE utf8mb4_unicode_ci,
  `IncrementalQuantity` text COLLATE utf8mb4_unicode_ci,
  `DisableQuantitySync` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierName` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierPartNumber` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_Cost` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_LeadTime` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsActive` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsPrimary` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Name` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Value` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_208_20190326_154603_927`
--

DROP TABLE IF EXISTS `anv_temp_208_20190326_154603_927`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_208_20190326_154603_927` (
  `base` text COLLATE utf8mb4_unicode_ci,
  `visibility` text COLLATE utf8mb4_unicode_ci,
  `dt` text COLLATE utf8mb4_unicode_ci,
  `id` text COLLATE utf8mb4_unicode_ci,
  `name` text COLLATE utf8mb4_unicode_ci,
  `cod` text COLLATE utf8mb4_unicode_ci,
  `coord_lon` text COLLATE utf8mb4_unicode_ci,
  `coord_lat` text COLLATE utf8mb4_unicode_ci,
  `main_temp` text COLLATE utf8mb4_unicode_ci,
  `main_pressure` text COLLATE utf8mb4_unicode_ci,
  `main_humidity` text COLLATE utf8mb4_unicode_ci,
  `main_temp_min` text COLLATE utf8mb4_unicode_ci,
  `main_temp_max` text COLLATE utf8mb4_unicode_ci,
  `wind_speed` text COLLATE utf8mb4_unicode_ci,
  `wind_deg` text COLLATE utf8mb4_unicode_ci,
  `clouds_all` text COLLATE utf8mb4_unicode_ci,
  `sys_type` text COLLATE utf8mb4_unicode_ci,
  `sys_id` text COLLATE utf8mb4_unicode_ci,
  `sys_message` text COLLATE utf8mb4_unicode_ci,
  `sys_country` text COLLATE utf8mb4_unicode_ci,
  `sys_sunrise` text COLLATE utf8mb4_unicode_ci,
  `sys_sunset` text COLLATE utf8mb4_unicode_ci,
  `weather_id` text COLLATE utf8mb4_unicode_ci,
  `weather_main` text COLLATE utf8mb4_unicode_ci,
  `weather_description` text COLLATE utf8mb4_unicode_ci,
  `weather_icon` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_209_20190326_191327_587`
--

DROP TABLE IF EXISTS `anv_temp_209_20190326_191327_587`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_209_20190326_191327_587` (
  `Id_t380` text COLLATE utf8mb4_unicode_ci,
  `Code_t380` text COLLATE utf8mb4_unicode_ci,
  `Sku_t380` text COLLATE utf8mb4_unicode_ci,
  `PartNumber_t380` text COLLATE utf8mb4_unicode_ci,
  `Description_t380` text COLLATE utf8mb4_unicode_ci,
  `ShortDescription_t380` text COLLATE utf8mb4_unicode_ci,
  `LongDescription_t380` text COLLATE utf8mb4_unicode_ci,
  `Cost_t380` text COLLATE utf8mb4_unicode_ci,
  `RetailPrice_t380` text COLLATE utf8mb4_unicode_ci,
  `SalePrice_t380` text COLLATE utf8mb4_unicode_ci,
  `WeightValue_t380` text COLLATE utf8mb4_unicode_ci,
  `WeightUnit_t380` text COLLATE utf8mb4_unicode_ci,
  `ReorderPoint_t380` text COLLATE utf8mb4_unicode_ci,
  `Brand_t380` text COLLATE utf8mb4_unicode_ci,
  `Supplier_t380` text COLLATE utf8mb4_unicode_ci,
  `Classification_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHand_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityOnHold_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityPicked_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityPending_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityAvailable_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityIncoming_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityInbound_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityTransfer_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityInStock_t380` text COLLATE utf8mb4_unicode_ci,
  `QuantityTotalFBA_t380` text COLLATE utf8mb4_unicode_ci,
  `CreatedDateUtc_t380` text COLLATE utf8mb4_unicode_ci,
  `ModifiedDateUtc_t380` text COLLATE utf8mb4_unicode_ci,
  `VariationParentSku_t380` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateSKU_t380` text COLLATE utf8mb4_unicode_ci,
  `IsAlternateCode_t380` text COLLATE utf8mb4_unicode_ci,
  `MOQ_t380` text COLLATE utf8mb4_unicode_ci,
  `MOQInfo_t380` text COLLATE utf8mb4_unicode_ci,
  `IncrementalQuantity_t380` text COLLATE utf8mb4_unicode_ci,
  `DisableQuantitySync_t380` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierName_t380` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_SupplierPartNumber_t380` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_Cost_t380` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_LeadTime_t380` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsActive_t380` text COLLATE utf8mb4_unicode_ci,
  `SupplierInfo_IsPrimary_t380` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Name_t380` text COLLATE utf8mb4_unicode_ci,
  `Attributes_Value_t380` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_212_20190403_113924_19`
--

DROP TABLE IF EXISTS `anv_temp_212_20190403_113924_19`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_212_20190403_113924_19` (
  `From_Currency_Code` text COLLATE utf8mb4_unicode_ci,
  `To_Currency_Code` text COLLATE utf8mb4_unicode_ci,
  `EXCHANGE_RATE` text COLLATE utf8mb4_unicode_ci,
  `CURRENCY_START_PERIOD` text COLLATE utf8mb4_unicode_ci,
  `CURRENCY_END_PERIOD` text COLLATE utf8mb4_unicode_ci,
  `RATE_FREQUENCY` text COLLATE utf8mb4_unicode_ci,
  `CALUCLATION_TYPE` text COLLATE utf8mb4_unicode_ci,
  `DataSource_Id` text COLLATE utf8mb4_unicode_ci,
  `IS_LOCKED` text COLLATE utf8mb4_unicode_ci,
  `ADDED_USER` text COLLATE utf8mb4_unicode_ci,
  `ADDED_DATE` text COLLATE utf8mb4_unicode_ci,
  `UPDATED_DATE` text COLLATE utf8mb4_unicode_ci,
  `Updated_User` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_213_20190402_130435_149`
--

DROP TABLE IF EXISTS `anv_temp_213_20190402_130435_149`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_213_20190402_130435_149` (
  `DataSource_Id` text COLLATE utf8mb4_unicode_ci,
  `ActivityID` text COLLATE utf8mb4_unicode_ci,
  `ActivityTypeID` text COLLATE utf8mb4_unicode_ci,
  `Status` text COLLATE utf8mb4_unicode_ci,
  `ReasonCodeID` text COLLATE utf8mb4_unicode_ci,
  `DateActivity` text COLLATE utf8mb4_unicode_ci,
  `Description` text COLLATE utf8mb4_unicode_ci,
  `TouchedBy` text COLLATE utf8mb4_unicode_ci,
  `TouchedAt` text COLLATE utf8mb4_unicode_ci,
  `CreatedBy` text COLLATE utf8mb4_unicode_ci,
  `CreatedAt` text COLLATE utf8mb4_unicode_ci,
  `walletid` text COLLATE utf8mb4_unicode_ci,
  `BCharacter01` text COLLATE utf8mb4_unicode_ci,
  `BCharacter02` text COLLATE utf8mb4_unicode_ci,
  `BNumber01` text COLLATE utf8mb4_unicode_ci,
  `BNumber02` text COLLATE utf8mb4_unicode_ci,
  `BDateTime01` text COLLATE utf8mb4_unicode_ci,
  `BBoolean` text COLLATE utf8mb4_unicode_ci,
  `Added_Date` text COLLATE utf8mb4_unicode_ci,
  `Added_User` text COLLATE utf8mb4_unicode_ci,
  `Updated_Date` text COLLATE utf8mb4_unicode_ci,
  `Updated_User` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_219_20190403_185403_935`
--

DROP TABLE IF EXISTS `anv_temp_219_20190403_185403_935`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_219_20190403_185403_935` (
  `_ProductID` text COLLATE utf8_unicode_ci,
  `RawMatrial_Id` text COLLATE utf8_unicode_ci,
  `Company_Id` text COLLATE utf8_unicode_ci,
  `PlantId` text COLLATE utf8_unicode_ci,
  `Bill_Planned_Date` text COLLATE utf8_unicode_ci,
  `material_type` text COLLATE utf8_unicode_ci,
  `percent_of_scrap` text COLLATE utf8_unicode_ci,
  `Percent_Yield` text COLLATE utf8_unicode_ci,
  `required_qty` text COLLATE utf8_unicode_ci,
  `RawMaterial_Price` text COLLATE utf8_unicode_ci,
  `Unit_Of_Measure` text COLLATE utf8_unicode_ci,
  `Cost_RAW_Material` text COLLATE utf8_unicode_ci,
  `lead_time` text COLLATE utf8_unicode_ci,
  `Eco_Number` text COLLATE utf8_unicode_ci,
  `Engineering_Change_Reason` text COLLATE utf8_unicode_ci,
  `Engineering_Change_Date` text COLLATE utf8_unicode_ci,
  `Effective_Start_Date` text COLLATE utf8_unicode_ci,
  `Effective_End_date` text COLLATE utf8_unicode_ci,
  `DataSource_Id` text COLLATE utf8_unicode_ci,
  `BCharacter01` text COLLATE utf8_unicode_ci,
  `BCharacter02` text COLLATE utf8_unicode_ci,
  `BNumber01` text COLLATE utf8_unicode_ci,
  `BNumber02` text COLLATE utf8_unicode_ci,
  `BDateTime01` text COLLATE utf8_unicode_ci,
  `BBoolean` text COLLATE utf8_unicode_ci,
  `Added_Date` text COLLATE utf8_unicode_ci,
  `Added_User` text COLLATE utf8_unicode_ci,
  `Updated_Date` text COLLATE utf8_unicode_ci,
  `Updated_User` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_229_20190409_132100_869`
--

DROP TABLE IF EXISTS `anv_temp_229_20190409_132100_869`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_229_20190409_132100_869` (
  `Customer_ID` text COLLATE utf8_unicode_ci,
  `Customer_Name` text COLLATE utf8_unicode_ci,
  `Address1` text COLLATE utf8_unicode_ci,
  `Address2` text COLLATE utf8_unicode_ci,
  `City` text COLLATE utf8_unicode_ci,
  `State` text COLLATE utf8_unicode_ci,
  `Country` text COLLATE utf8_unicode_ci,
  `Zip_Code` text COLLATE utf8_unicode_ci,
  `Email_Addr` text COLLATE utf8_unicode_ci,
  `Cust_Category_id` text COLLATE utf8_unicode_ci,
  `Customer_Latitude` text COLLATE utf8_unicode_ci,
  `Customer_Longitude` text COLLATE utf8_unicode_ci,
  `Sales_Rep_Code` text COLLATE utf8_unicode_ci,
  `Sales_Territory_Id` text COLLATE utf8_unicode_ci,
  `Region` text COLLATE utf8_unicode_ci,
  `Sub_Region` text COLLATE utf8_unicode_ci,
  `Currency_id` text COLLATE utf8_unicode_ci,
  `Hierarchy_Lvl1` text COLLATE utf8_unicode_ci,
  `Hierarchy_Lvl2` text COLLATE utf8_unicode_ci,
  `Hierarchy_Lvl3` text COLLATE utf8_unicode_ci,
  `Hierarchy_Lvl4` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_230_20190418_173233_111`
--

DROP TABLE IF EXISTS `anv_temp_230_20190418_173233_111`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_230_20190418_173233_111` (
  `id_t23` text COLLATE utf8mb4_unicode_ci,
  `cluster_name_t23` text COLLATE utf8mb4_unicode_ci,
  `cluster_schema_t23` text COLLATE utf8mb4_unicode_ci,
  `cluster_hostname_t23` text COLLATE utf8mb4_unicode_ci,
  `app_context_t23` text COLLATE utf8mb4_unicode_ci,
  `description_t23` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_231_20190418_173832_651`
--

DROP TABLE IF EXISTS `anv_temp_231_20190418_173832_651`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_231_20190418_173832_651` (
  `id_t23` text COLLATE utf8mb4_unicode_ci,
  `cluster_name_t23` text COLLATE utf8mb4_unicode_ci,
  `cluster_schema_t23` text COLLATE utf8mb4_unicode_ci,
  `cluster_hostname_t23` text COLLATE utf8mb4_unicode_ci,
  `app_context_t23` text COLLATE utf8mb4_unicode_ci,
  `description_t23` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_234_20190507_171147_641`
--

DROP TABLE IF EXISTS `anv_temp_234_20190507_171147_641`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_234_20190507_171147_641` (
  `id` text COLLATE utf8mb4_unicode_ci,
  `deliverable_id` text COLLATE utf8mb4_unicode_ci,
  `name` text COLLATE utf8mb4_unicode_ci,
  `alias` text COLLATE utf8mb4_unicode_ci,
  `description` text COLLATE utf8mb4_unicode_ci,
  `category` text COLLATE utf8mb4_unicode_ci,
  `dateCreated` text COLLATE utf8mb4_unicode_ci,
  `blueprintDeliverable_id` text COLLATE utf8mb4_unicode_ci,
  `customValues_id` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_235_20190508_145620_857`
--

DROP TABLE IF EXISTS `anv_temp_235_20190508_145620_857`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_235_20190508_145620_857` (
  `id` text COLLATE utf8mb4_unicode_ci,
  `deliverable_id` text COLLATE utf8mb4_unicode_ci,
  `name` text COLLATE utf8mb4_unicode_ci,
  `alias` text COLLATE utf8mb4_unicode_ci,
  `description` text COLLATE utf8mb4_unicode_ci,
  `category` text COLLATE utf8mb4_unicode_ci,
  `dateCreated` text COLLATE utf8mb4_unicode_ci,
  `blueprintDeliverable_id` text COLLATE utf8mb4_unicode_ci,
  `customValues_id` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_236_20190509_152200_992`
--

DROP TABLE IF EXISTS `anv_temp_236_20190509_152200_992`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_236_20190509_152200_992` (
  `id` text COLLATE utf8mb4_unicode_ci,
  `deliverable_id` text COLLATE utf8mb4_unicode_ci,
  `name` text COLLATE utf8mb4_unicode_ci,
  `alias` text COLLATE utf8mb4_unicode_ci,
  `description` text COLLATE utf8mb4_unicode_ci,
  `category` text COLLATE utf8mb4_unicode_ci,
  `dateCreated` text COLLATE utf8mb4_unicode_ci,
  `blueprintDeliverable_id` text COLLATE utf8mb4_unicode_ci,
  `customValues_id` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anv_temp_237_20190513_131123_458`
--

DROP TABLE IF EXISTS `anv_temp_237_20190513_131123_458`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anv_temp_237_20190513_131123_458` (
  `asin` text COLLATE utf8mb4_unicode_ci,
  `title` text COLLATE utf8mb4_unicode_ci,
  `brand` text COLLATE utf8mb4_unicode_ci,
  `weight` text COLLATE utf8mb4_unicode_ci,
  `size` text COLLATE utf8mb4_unicode_ci,
  `category` text COLLATE utf8mb4_unicode_ci,
  `rating` text COLLATE utf8mb4_unicode_ci,
  `reviews` text COLLATE utf8mb4_unicode_ci,
  `SalesRank` text COLLATE utf8mb4_unicode_ci,
  `upclist` text COLLATE utf8mb4_unicode_ci,
  `FulfilledBy` text COLLATE utf8mb4_unicode_ci,
  `fbafee` text COLLATE utf8mb4_unicode_ci,
  `storageFee` text COLLATE utf8mb4_unicode_ci,
  `storageFeeTax` text COLLATE utf8mb4_unicode_ci,
  `pickAndPackFee` text COLLATE utf8mb4_unicode_ci,
  `pickAndPackFeeTax` text COLLATE utf8mb4_unicode_ci,
  `noofsellers` text COLLATE utf8mb4_unicode_ci,
  `buy_box_price` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asins_list`
--

DROP TABLE IF EXISTS `asins_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asins_list` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aws_categories`
--

DROP TABLE IF EXISTS `aws_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aws_categories` (
  `catId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `box`
--

DROP TABLE IF EXISTS `box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `box` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `boxid` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `box_28032019`
--

DROP TABLE IF EXISTS `box_28032019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `box_28032019` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `boxid` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categories` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories_28032019`
--

DROP TABLE IF EXISTS `categories_28032019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories_28032019` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categories` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deals`
--

DROP TABLE IF EXISTS `deals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deals` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categories` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deals_new`
--

DROP TABLE IF EXISTS `deals_new`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deals_new` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categories` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fba_fbm`
--

DROP TABLE IF EXISTS `fba_fbm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fba_fbm` (
  `sellerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerType` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
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
  `package_source_mapping_id` bigint(20) DEFAULT NULL,
  `inc_date_from_source` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_by` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `offer`
--

DROP TABLE IF EXISTS `offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `offer` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isAmazon` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `offerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isFBA` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isSNS` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerName` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `offer_28032019`
--

DROP TABLE IF EXISTS `offer_28032019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `offer_28032019` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isAmazon` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `offerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isFBA` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isSNS` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerName` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_offer`
--

DROP TABLE IF EXISTS `product_offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_offer` (
  `asin` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `offerId` decimal(15,0) DEFAULT NULL,
  `lastSeen` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `offerCSV` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `condition_offer` decimal(15,0) DEFAULT NULL,
  `conditionComment` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isPrime` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isMAP` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isShippable` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isAddonItem` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isPreorder` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isWarehouseDeal` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isScam` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isAmazon` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isFBA` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isPrimeExcl` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `stockCSV` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `review_rating`
--

DROP TABLE IF EXISTS `review_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review_rating` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rating` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sales` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reviews` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `upclist` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fbafee` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `review_rating_28032019`
--

DROP TABLE IF EXISTS `review_rating_28032019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review_rating_28032019` (
  `asin` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rating` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sales` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reviews` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `upclist` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fbafee` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sellername`
--

DROP TABLE IF EXISTS `sellername`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sellername` (
  `sellerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerName` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerType` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sellername_bk`
--

DROP TABLE IF EXISTS `sellername_bk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sellername_bk` (
  `sellerId` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerName` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sellerType` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temptable`
--

DROP TABLE IF EXISTS `temptable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temptable` (
  `modified_by` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'mahender_1010009_1010027_staging'
--

--
-- Dumping routines for database 'mahender_1010009_1010027_staging'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-28 12:14:44
