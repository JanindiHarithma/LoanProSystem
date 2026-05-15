-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: loanpro_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `nic_no` varchar(12) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` text,
  `monthly_income` decimal(12,2) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `nic_no` (`nic_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'200559503720','Janindi Harithma','0711908615','janindiharithma05@gmail.com','375/11, Rathnarama Road, Hokanadra\nNorth, Hokanadara',10000.00,'2026-05-09 19:40:43'),(2,'200659203730','Erandi Alakon','0771636615','erandialakon99@gmail.com','379/14,Kohuwala Road,\n              Nugegoda',80000.00,'2026-05-09 20:23:09');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loan` (
  `loan_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `type_id` int NOT NULL,
  `loan_amount` decimal(12,2) NOT NULL,
  `duration_months` int NOT NULL,
  `emi_amount` decimal(12,2) DEFAULT NULL,
  `status` enum('PENDING','APPROVED','REJECTED','CLOSED') DEFAULT 'PENDING',
  `applied_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `approved_date` datetime DEFAULT NULL,
  `officer_notes` text,
  PRIMARY KEY (`loan_id`),
  KEY `customer_id` (`customer_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `loan_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `loan_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `loan_type` (`type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan`
--

LOCK TABLES `loan` WRITE;
/*!40000 ALTER TABLE `loan` DISABLE KEYS */;
INSERT INTO `loan` VALUES (1,1,1,100000.00,24,4730.73,'APPROVED','2026-05-09 19:42:36','2026-05-09 19:46:23','Loan approved after reviewing customer\'s income and credit history. \nCustomer meets all eligibility criteria.'),(2,2,4,2000000.00,24,96973.30,'PENDING','2026-05-09 20:24:11',NULL,NULL),(3,2,3,2000000.00,24,92289.85,'PENDING','2026-05-09 20:27:20',NULL,NULL);
/*!40000 ALTER TABLE `loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan_type`
--

DROP TABLE IF EXISTS `loan_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loan_type` (
  `type_id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  `interest_rate` decimal(5,2) NOT NULL,
  `max_amount` decimal(12,2) DEFAULT NULL,
  `max_duration_months` int DEFAULT NULL,
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `type_name` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan_type`
--

LOCK TABLES `loan_type` WRITE;
/*!40000 ALTER TABLE `loan_type` DISABLE KEYS */;
INSERT INTO `loan_type` VALUES (1,'Personal Loan',12.50,500000.00,60),(2,'Housing Loan',8.75,5000000.00,240),(3,'Vehicle Loan',10.00,2000000.00,84),(4,'Business Loan',15.00,3000000.00,120);
/*!40000 ALTER TABLE `loan_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repayment`
--

DROP TABLE IF EXISTS `repayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repayment` (
  `repayment_id` int NOT NULL AUTO_INCREMENT,
  `loan_id` int NOT NULL,
  `due_date` date NOT NULL,
  `paid_date` date DEFAULT NULL,
  `amount_due` decimal(12,2) DEFAULT NULL,
  `amount_paid` decimal(12,2) DEFAULT '0.00',
  `status` enum('PENDING','PAID','OVERDUE') DEFAULT 'PENDING',
  `penalty` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`repayment_id`),
  KEY `loan_id` (`loan_id`),
  CONSTRAINT `repayment_ibfk_1` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`loan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repayment`
--

LOCK TABLES `repayment` WRITE;
/*!40000 ALTER TABLE `repayment` DISABLE KEYS */;
INSERT INTO `repayment` VALUES (1,1,'2026-06-09','2026-05-09',4730.73,4730.73,'PAID',0.00),(2,1,'2026-07-09',NULL,4730.73,0.00,'PENDING',0.00),(3,1,'2026-08-09',NULL,4730.73,0.00,'PENDING',0.00),(4,1,'2026-09-09',NULL,4730.73,0.00,'PENDING',0.00),(5,1,'2026-10-09',NULL,4730.73,0.00,'PENDING',0.00),(6,1,'2026-11-09',NULL,4730.73,0.00,'PENDING',0.00),(7,1,'2026-12-09',NULL,4730.73,0.00,'PENDING',0.00),(8,1,'2027-01-09',NULL,4730.73,0.00,'PENDING',0.00),(9,1,'2027-02-09',NULL,4730.73,0.00,'PENDING',0.00),(10,1,'2027-03-09',NULL,4730.73,0.00,'PENDING',0.00),(11,1,'2027-04-09',NULL,4730.73,0.00,'PENDING',0.00),(12,1,'2027-05-09',NULL,4730.73,0.00,'PENDING',0.00),(13,1,'2027-06-09',NULL,4730.73,0.00,'PENDING',0.00),(14,1,'2027-07-09',NULL,4730.73,0.00,'PENDING',0.00),(15,1,'2027-08-09',NULL,4730.73,0.00,'PENDING',0.00),(16,1,'2027-09-09',NULL,4730.73,0.00,'PENDING',0.00),(17,1,'2027-10-09',NULL,4730.73,0.00,'PENDING',0.00),(18,1,'2027-11-09',NULL,4730.73,0.00,'PENDING',0.00),(19,1,'2027-12-09',NULL,4730.73,0.00,'PENDING',0.00),(20,1,'2028-01-09',NULL,4730.73,0.00,'PENDING',0.00),(21,1,'2028-02-09',NULL,4730.73,0.00,'PENDING',0.00),(22,1,'2028-03-09',NULL,4730.73,0.00,'PENDING',0.00),(23,1,'2028-04-09',NULL,4730.73,0.00,'PENDING',0.00),(24,1,'2028-05-09',NULL,4730.73,0.00,'PENDING',0.00);
/*!40000 ALTER TABLE `repayment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-09 21:21:12
