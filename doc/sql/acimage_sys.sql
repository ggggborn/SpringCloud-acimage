-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: acimage_sys
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_authorize`
--

DROP TABLE IF EXISTS `tb_authorize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_authorize` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `permission_id` int NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  CONSTRAINT `tb_authorize_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`),
  CONSTRAINT `tb_authorize_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `tb_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_authorize`
--

LOCK TABLES `tb_authorize` WRITE;
/*!40000 ALTER TABLE `tb_authorize` DISABLE KEYS */;
INSERT INTO `tb_authorize` VALUES (2,2,14,'2023-01-16 22:31:31'),(3,1,15,'2023-01-16 22:32:06'),(4,1,7,'2023-01-16 22:32:09'),(5,1,8,'2023-01-16 22:32:10'),(6,1,10,'2023-01-16 22:32:12'),(7,1,11,'2023-01-16 22:32:14'),(8,1,12,'2023-01-16 22:32:15'),(9,1,13,'2023-01-16 22:32:16'),(10,3,18,'2023-01-16 22:37:00'),(11,3,19,'2023-01-16 22:37:01'),(12,3,21,'2023-01-16 22:37:04'),(13,3,22,'2023-01-16 22:37:05'),(14,3,24,'2023-01-16 22:37:07'),(15,2,29,'2023-01-16 23:56:39'),(16,2,30,'2023-01-16 23:56:46'),(17,1,29,'2023-01-16 23:58:10'),(18,1,30,'2023-01-16 23:58:12'),(19,3,31,'2023-01-16 23:58:18'),(20,3,32,'2023-01-16 23:58:21'),(21,3,33,'2023-01-16 23:58:26'),(22,3,26,'2023-01-16 23:58:28'),(23,6,18,'2023-01-16 23:58:55'),(24,6,19,'2023-01-16 23:58:56'),(25,6,31,'2023-01-16 23:58:58'),(26,6,21,'2023-01-16 23:58:59'),(27,6,22,'2023-01-16 23:59:01'),(28,6,32,'2023-01-16 23:59:02'),(29,6,24,'2023-01-16 23:59:04'),(30,6,33,'2023-01-16 23:59:06'),(31,6,26,'2023-01-16 23:59:08'),(32,6,27,'2023-01-16 23:59:10');
/*!40000 ALTER TABLE `tb_authorize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_permission`
--

DROP TABLE IF EXISTS `tb_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `note` varchar(20) DEFAULT NULL,
  `is_module` tinyint NOT NULL COMMENT '是权限模块还是处于叶子节点的权限',
  `label` varchar(20) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_tb_permission_parent_id_idx` (`parent_id`),
  CONSTRAINT `fk_tb_permission_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `tb_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permission`
--

LOCK TABLES `tb_permission` WRITE;
/*!40000 ALTER TABLE `tb_permission` DISABLE KEYS */;
INSERT INTO `tb_permission` VALUES (1,NULL,NULL,'用户社区权限模块',1,'社区','2023-01-16 16:16:53','2023-01-16 15:06:42'),(2,1,NULL,'',1,'用户','2023-01-16 16:36:49','2023-01-16 15:06:59'),(3,1,NULL,'',1,'评论','2023-01-16 16:37:48','2023-01-16 15:07:04'),(4,NULL,NULL,'',1,'管理','2023-01-16 16:43:22','2023-01-16 15:06:53'),(6,1,NULL,'',1,'话题','2023-01-16 16:46:07','2023-01-16 15:07:13'),(7,3,'comment:add','',0,'发表评论','2023-01-16 16:47:16','2023-01-16 15:07:27'),(8,3,'comment:delete','',0,'删除评论','2023-01-16 16:47:55','2023-01-16 15:07:33'),(10,3,'comment:update','',0,'修改评论','2023-01-16 16:53:54','2023-01-16 15:07:39'),(11,6,'topic:add','',0,'发表话题','2023-01-16 17:01:47','2023-01-16 15:07:47'),(12,6,'topic:delete','',0,'删除话题','2023-01-16 17:05:11','2023-01-16 15:07:54'),(13,6,'topic:update','',0,'修改话题','2023-01-16 17:05:46','2023-01-16 15:08:04'),(14,2,'user:register','',0,'注册','2023-01-16 17:13:43','2023-01-16 15:08:17'),(15,2,'user:update','和用户隐私信息修改不同',0,'用户基本信息修改','2023-01-16 17:15:01','2023-01-16 15:08:38'),(16,4,NULL,'',1,'社区管理','2023-01-16 17:16:00','2023-01-16 15:51:03'),(17,16,NULL,'',1,'评论管理','2023-01-16 17:18:39','2023-01-16 15:51:10'),(18,17,'system:comment:update','',0,'评论修改-管理','2023-01-16 17:19:38','2023-01-16 17:19:38'),(19,17,'system:comment:delete','',0,'评论删除-管理','2023-01-16 17:20:27','2023-01-16 17:20:27'),(20,16,NULL,'',1,'话题管理','2023-01-16 17:21:21','2023-01-16 15:51:23'),(21,20,'system:topic:update','',0,'话题修改-管理','2023-01-16 17:22:17','2023-01-16 09:22:32'),(22,20,'system:topic:delete','',0,'话题删除-管理','2023-01-16 17:23:26','2023-01-16 17:23:26'),(23,16,NULL,'',1,'用户管理','2023-01-16 17:25:22','2023-01-16 15:51:46'),(24,23,'system:user:update','',0,'用户信息修改-管理','2023-01-16 17:26:16','2023-01-16 17:26:16'),(25,4,NULL,'',1,'权限管理','2023-01-16 18:18:56','2023-01-16 15:51:35'),(26,25,'auth:query','',0,'权限查看','2023-01-16 22:43:42','2023-01-16 22:43:42'),(27,25,'auth:update','',0,'权限修改','2023-01-16 22:44:32','2023-01-16 22:44:32'),(29,3,'comment:query','',0,'查看评论','2023-01-16 22:47:19','2023-01-16 15:51:55'),(30,6,'topic:query','',0,'查看话题','2023-01-16 22:49:17','2023-01-16 15:57:03'),(31,17,'system:comment:query','',0,'查看评论-管理','2023-01-16 23:53:10','2023-01-16 15:53:25'),(32,20,'system:topic:query','',0,'查看话题-管理','2023-01-16 23:54:00','2023-01-16 15:54:19'),(33,23,'system:user:query','',0,'查看用户-管理','2023-01-16 23:55:02','2023-01-16 23:55:02');
/*!40000 ALTER TABLE `tb_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `note` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES (1,'user','2023-01-15 18:22:25','2023-01-15 10:29:50','社区普通用户'),(2,'visitor','2023-01-15 18:30:03','2023-01-15 18:30:03','游客'),(3,'admin','2023-01-15 18:30:15','2023-01-15 18:30:15','管理员'),(6,'superAdmin','2023-01-16 23:58:44','2023-01-16 23:58:44','超级管理员');
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-17  0:07:18
