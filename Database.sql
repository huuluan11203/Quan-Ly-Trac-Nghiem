-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tracnghiem
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answers` (
  `awID` int NOT NULL AUTO_INCREMENT,
  `qID` int NOT NULL COMMENT 'id câu hỏi',
  `awContent` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `awPictures` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'url ảnh',
  `isRight` tinyint NOT NULL COMMENT '1: đúng; 0: Sai',
  `awStatus` tinyint NOT NULL COMMENT '1: active; 0: hidden',
  PRIMARY KEY (`awID`),
  KEY `qID` (`qID`),
  CONSTRAINT `answers_ibfk_1` FOREIGN KEY (`qID`) REFERENCES `questions` (`qID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exams`
--

DROP TABLE IF EXISTS `exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exams` (
  `testCode` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `exOrder` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'A;B;C;D;E;F',
  `exCode` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '=testCode + exOrder',
  `ex_quesIDs` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'mảng các id câu hỏi',
  PRIMARY KEY (`testCode`,`exOrder`),
  UNIQUE KEY `testCode_UNIQUE` (`testCode`),
  KEY `testCode` (`testCode`),
  KEY `exCode` (`exCode`),
  CONSTRAINT `exams_ibfk_1` FOREIGN KEY (`exCode`) REFERENCES `result` (`exCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exams`
--

LOCK TABLES `exams` WRITE;
/*!40000 ALTER TABLE `exams` DISABLE KEYS */;
/*!40000 ALTER TABLE `exams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logs` (
  `logID` int NOT NULL AUTO_INCREMENT,
  `logContent` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `logUserID` int NOT NULL,
  `logExCode` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `logDate` datetime NOT NULL,
  PRIMARY KEY (`logID`),
  KEY `logUserID` (`logUserID`),
  CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`logUserID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs`
--

LOCK TABLES `logs` WRITE;
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `qID` int NOT NULL AUTO_INCREMENT,
  `qContent` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'nội dung câu hỏi',
  `qPictures` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'url hình đính kèm',
  `qTopicID` int NOT NULL,
  `qLevel` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'easy, meidum, diff',
  `qStatus` tinyint NOT NULL COMMENT '1: active; 0: hidden',
  PRIMARY KEY (`qID`),
  KEY `qTopicID` (`qTopicID`),
  CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`qTopicID`) REFERENCES `topics` (`tpID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `result`
--

DROP TABLE IF EXISTS `result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `result` (
  `rs_num` tinyint NOT NULL,
  `userID` int NOT NULL,
  `exCode` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rs_anwsers` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'các đáp án đã chọn',
  `rs_mark` decimal(10,0) NOT NULL,
  `rs_date` datetime NOT NULL,
  PRIMARY KEY (`rs_num`,`userID`,`exCode`),
  UNIQUE KEY `exCode_UNIQUE` (`exCode`),
  KEY `userID` (`userID`),
  KEY `exID` (`exCode`),
  KEY `exCode` (`exCode`),
  CONSTRAINT `result_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`),
  CONSTRAINT `result_chk_1` CHECK (json_valid(`rs_anwsers`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `result`
--

LOCK TABLES `result` WRITE;
/*!40000 ALTER TABLE `result` DISABLE KEYS */;
/*!40000 ALTER TABLE `result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
  `testID` int NOT NULL,
  `testCode` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'mã bài thi',
  `testTilte` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `testTime` int NOT NULL COMMENT 'thời gian làm bài (phút)',
  `tpID` int NOT NULL COMMENT 'id của chủ đề/bài học',
  `num_easy` int NOT NULL COMMENT 'số lượng câu dễ',
  `num_medium` int NOT NULL COMMENT 'số lượng câu trung bình',
  `num_diff` int NOT NULL COMMENT 'số lượng câu khó',
  `testLimit` tinyint NOT NULL COMMENT 'số lần thi',
  `testDate` date NOT NULL,
  `testStatus` int NOT NULL,
  PRIMARY KEY (`testID`,`tpID`) USING BTREE,
  KEY `tpID` (`tpID`),
  KEY `testCode` (`testCode`),
  CONSTRAINT `test_ibfk_1` FOREIGN KEY (`tpID`) REFERENCES `topics` (`tpID`),
  CONSTRAINT `test_ibfk_2` FOREIGN KEY (`testCode`) REFERENCES `exams` (`testCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topics` (
  `tpID` int NOT NULL AUTO_INCREMENT,
  `tpTitle` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'tên topic',
  `tpParent` int NOT NULL COMMENT 'id của topic cha',
  `tpStatus` tinyint NOT NULL COMMENT '1: active; 0: hidden',
  PRIMARY KEY (`tpID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics`
--

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;
/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'login = userName',
  `userEmail` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userPassword` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'mã hóa dùng md5',
  `userFullName` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isAdmin` tinyint NOT NULL COMMENT '1: admin; 0: user',
  `userStatus` tinyint NOT NULL DEFAULT 1 COMMENT 'mặc định trạng thái là 1',
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-20 19:13:08
