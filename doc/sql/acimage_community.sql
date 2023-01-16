-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: acimage_community
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
-- Table structure for table `bak_tb_image`
--

DROP TABLE IF EXISTS `bak_tb_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bak_tb_image` (
  `id` bigint NOT NULL,
  `topic_id` bigint DEFAULT NULL,
  `size` int NOT NULL,
  `description` varchar(30) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_image_topic_id` (`topic_id`),
  CONSTRAINT `fk_image_topic_id` FOREIGN KEY (`topic_id`) REFERENCES `tb_topic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='此表废弃，迁移到acimage_image';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bak_tb_image`
--

LOCK TABLES `bak_tb_image` WRITE;
/*!40000 ALTER TABLE `bak_tb_image` DISABLE KEYS */;
INSERT INTO `bak_tb_image` VALUES (1572508721903943680,1572508721685839872,3875,'没脸看+','topicImage/2022/12/03/1572508721903943680.jpeg'),(1572508721903943681,1572508721685839872,10254,'愤怒++','topicImage/2022/12/03/1572508721903943681.jpeg'),(1573240094587424768,1573240094281240576,61485,'1589.jpeg','topicImage/2022/12/03/1573240094587424768.jpeg'),(1573240094587424769,1573240094281240576,115251,'bg5552233','topicImage/2022/12/03/1573240094587424769.jpeg'),(1573322637990981632,1573322637529608192,94990,'爱丽丝镇楼','topicImage/2022/12/03/1573322637990981632.jpeg'),(1573324140340367360,1573324140009017344,298951,'login-bg2.jpg','topicImage/2022/12/03/1573324140340367360.jpeg'),(1573953638224220160,1573953638006116352,61485,'1.jpeg','topicImage/2022/12/03/1573953638224220160.jpeg'),(1573953638224220161,1573953638006116352,206308,'2.jpg','topicImage/2022/12/03/1573953638224220161.jpeg'),(1574286298305822720,1574286298175799296,10254,'b5.png','topicImage/2022/12/03/1574286298305822720.jpeg'),(1575129660659408896,1575129660344836096,201835,'次元小镇8','topicImage/2022/12/03/1575129660659408896.jpeg'),(1575868221272621056,1575868221125820416,632860,'大忍.jpg','topicImage/2022/12/03/1575868221272621056.jpeg'),(1575868221272621057,1575868221125820416,548304,'中忍.jpg','topicImage/2022/12/03/1575868221272621057.jpeg'),(1578393575283515392,1578393575279321088,10254,'b5 - 副本.','topicImage/2022/12/03/1578393575283515392.jpeg'),(1578393575283515393,1578393575279321088,10254,'b5','topicImage/2022/12/03/1578393575283515393.jpeg'),(1578709444954001408,1578709444933029888,58013,'水源千鹤.jpeg','topicImage/2022/12/03/1578709444954001408.jpeg'),(1578964177170010112,1578964177039986688,122126,'404.png','topicImage/2022/12/03/1578964177170010112.jpeg'),(1578964177170010113,1578964177039986688,12467,'b4.png','topicImage/2022/12/03/1578964177170010113.jpeg'),(1579019429604798464,1579019429336363008,120195,'次元小镇5.jpg','topicImage/2022/12/03/1579019429604798464.jpeg'),(1579088909441728512,1579088909127155712,23681,'义勇？？？','topicImage/2022/12/03/1579088909441728512.jpeg'),(1583082177125019648,1583082176776892416,71046,'百度图片9.jpeg','topicImage/2022/12/03/1583082177125019648.jpeg'),(1584469610789879808,1584469610554998784,230322,'次元小镇4.jpg','topicImage/2022/12/03/1584469610789879808.jpeg'),(1585529145067569152,1585529145054986240,51912,'藤原千花.jpeg','topicImage/2022/12/03/1585529145067569152.jpeg'),(1585529145067569153,1585529145054986240,44417,'藤原千花1.jpeg','topicImage/2022/12/03/1585529145067569153.jpeg'),(1585529145067569154,1585529145054986240,51626,'藤原千花2.jpeg','topicImage/2022/12/03/1585529145067569154.jpeg'),(1585529145067569155,1585529145054986240,148743,'藤原千花3.jpeg','topicImage/2022/12/03/1585529145067569155.jpeg'),(1585529145067569156,1585529145054986240,73031,'藤原千花4.jpeg','topicImage/2022/12/03/1585529145067569156.jpeg'),(1586281501346553856,1586281501153615872,191680,'login-bg','topicImage/2022/12/03/1586281501346553856.jpeg'),(1586281501346553857,1586281501153615872,191294,'百度图片1','topicImage/2022/12/03/1586281501346553857.jpeg'),(1586281501346553858,1586281501153615872,66104,'百度图片','topicImage/2022/12/03/1586281501346553858.jpeg');
/*!40000 ALTER TABLE `bak_tb_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bak_tb_sp_image`
--

DROP TABLE IF EXISTS `bak_tb_sp_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bak_tb_sp_image` (
  `id` bigint NOT NULL,
  `description` varchar(30) DEFAULT NULL,
  `url` varchar(50) NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `size` int DEFAULT NULL,
  `location` int DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='此表废弃';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bak_tb_sp_image`
--

LOCK TABLES `bak_tb_sp_image` WRITE;
/*!40000 ALTER TABLE `bak_tb_sp_image` DISABLE KEYS */;
INSERT INTO `bak_tb_sp_image` VALUES (1595422558785183744,'三分钟diy虚拟形象改+++9968','homeCarousel/2022/11/23/1595422558785183744','HOME_CAROUSEL',66104,1,'2022-11-23 14:22:34','2022-11-24 14:55:51');
/*!40000 ALTER TABLE `bak_tb_sp_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_comment`
--

DROP TABLE IF EXISTS `tb_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_comment` (
  `id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_comment_user_id` (`user_id`),
  KEY `fk_comment_topic_id` (`topic_id`),
  CONSTRAINT `fk_comment_topic_id` FOREIGN KEY (`topic_id`) REFERENCES `tb_topic` (`id`),
  CONSTRAINT `fk_comment_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user_basic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_comment`
--

LOCK TABLES `tb_comment` WRITE;
/*!40000 ALTER TABLE `tb_comment` DISABLE KEYS */;
INSERT INTO `tb_comment` VALUES (1572851342425731072,1572508721685839872,1572443275490078720,'第一次评论test----','2022-09-22 15:32:36','2022-09-22 15:32:36'),(1572855699737264128,1572508721685839872,1572443275490078720,'再试一次','2022-09-22 15:49:55','2022-09-22 15:49:55'),(1572855798034972672,1572508721685839872,1572443275490078720,'地板555','2022-09-22 15:50:19','2022-09-22 15:50:19'),(1572857549341442048,1572508721685839872,1571804585637974016,'我xlg2也试一下','2022-09-22 15:57:16','2022-09-22 15:57:16'),(1572930863271350272,1572508721685839872,1572517648460795904,'wyn test成功--','2022-09-22 20:48:36','2022-10-07 21:58:11'),(1573953716125028352,1573953638006116352,1572443275490078720,'确实还不错！！！--','2022-09-25 16:33:03','2022-09-25 16:33:03'),(1574394248341057536,1573324140009017344,1572443275490078720,'好看呀','2022-09-26 21:43:34','2022-09-26 21:43:34'),(1575022334250057728,1573953638006116352,1572517648460795904,'哈哈哈哈55','2022-09-28 15:19:21','2022-09-28 15:19:21'),(1575022696075886592,1573953638006116352,1572517648460795904,'sdfsfds+++++++55','2022-09-28 15:20:47','2022-09-30 20:10:27'),(1575026549764780032,1573953638006116352,1572517648460795904,'试试看85876','2022-09-28 15:36:06','2022-09-29 14:00:36'),(1575129723188092928,1575129660344836096,1572517648460795904,'哈哈哈，我是楼主55','2022-09-28 22:26:05','2022-09-28 22:26:22'),(1575868271109341184,1575868221125820416,1572517648460795904,'沙发88','2022-09-30 23:20:48','2022-09-30 23:20:54'),(1576547365828435968,1573240094281240576,1572517648460795904,'牛逼哈哈哈','2022-10-02 20:19:17','2022-10-07 21:28:55'),(1578388149275987968,1573322637529608192,1572517648460795904,'真的牛逼','2022-10-07 22:13:54','2022-10-07 22:13:54'),(1578389197927579648,1574286298175799296,1572517648460795904,'狗头doge','2022-10-07 22:18:04','2022-10-07 22:18:04'),(1578393628375015424,1578393575279321088,1572517648460795904,'双重狗头','2022-10-07 22:35:40','2022-10-07 22:35:40'),(1578604868410134528,1578393575279321088,1572517648460795904,'头头','2022-10-08 12:35:04','2022-10-08 12:35:04'),(1578605891719606272,1578393575279321088,1572517648460795904,'8888','2022-10-08 12:39:08','2022-10-08 12:39:08'),(1578606221970714624,1578393575279321088,1575444479153295360,'你也是狗头？？？','2022-10-08 12:40:27','2022-10-08 12:40:27'),(1578657605663363072,1575868221125820416,1575444479153295360,'小忍yyds','2022-10-08 16:04:37','2022-10-08 16:04:37'),(1578708741988651008,1572508721685839872,1575444479153295360,'评论6\n44','2022-10-08 19:27:49','2022-10-08 19:27:49'),(1578708763522207744,1572508721685839872,1575444479153295360,'评论7','2022-10-08 19:27:54','2022-10-08 19:27:54'),(1578708782283329536,1572508721685839872,1575444479153295360,'评论8','2022-10-08 19:27:59','2022-10-08 19:27:59'),(1578708821575569408,1572508721685839872,1575444479153295360,'评论9','2022-10-08 19:28:08','2022-10-08 19:28:08'),(1578708840655458304,1572508721685839872,1575444479153295360,'评论10','2022-10-08 19:28:13','2022-10-08 19:28:13'),(1578708861375320064,1572508721685839872,1575444479153295360,'评论11','2022-10-08 19:28:18','2022-10-08 19:28:18'),(1578709488805449728,1578709444933029888,1575444479153295360,'谁不服砍我','2022-10-08 19:30:47','2022-10-08 19:30:47'),(1578720891922604032,1578709444933029888,1578720796628017152,'我周哥也来--','2022-10-08 20:16:06','2022-10-08 20:18:41'),(1578721943895990272,1572508721685839872,1578720796628017152,'搞那么多评论干嘛','2022-10-08 20:20:17','2022-10-08 20:20:17'),(1578956782356189184,1573324140009017344,1575444479153295360,'确实','2022-10-09 11:53:27','2022-10-09 11:53:27'),(1580123499950276608,1579088909127155712,1578720796628017152,'沙发','2022-10-12 17:09:34','2022-10-12 17:09:34'),(1581933002085634048,1578964177039986688,1578720796628017152,'猥琐***','2022-10-17 16:59:53','2022-10-17 16:59:59'),(1581943414428626944,1575868221125820416,1578720796628017152,'我也来','2022-10-17 17:41:15','2022-10-17 17:41:15'),(1584449147221061632,1573322637529608192,1571804585637974016,'还是打算看+9','2022-10-24 15:38:08','2022-12-01 23:04:50'),(1584449427845165056,1573322637529608192,1571804585637974016,'牛逼吗','2022-10-24 15:39:15','2022-11-15 11:20:52'),(1584469788133441536,1584469610554998784,1571804585637974016,'楼主竟是我自己','2022-10-24 17:00:10','2022-10-24 17:00:10'),(1586281605595979776,1586281501153615872,1571804585637974016,'阿技术的骄傲和86786\n','2022-10-29 16:59:41','2022-10-29 16:59:46'),(1586281647572574208,1586281501153615872,1571804585637974016,'safasfda886','2022-10-29 16:59:51','2022-11-21 11:34:17'),(1586281665490640896,1586281501153615872,1571804585637974016,'fsdfs','2022-10-29 16:59:55','2022-10-29 16:59:55'),(1586281676790095872,1586281501153615872,1571804585637974016,'sdfsdfds','2022-10-29 16:59:58','2022-10-29 16:59:58'),(1592063300685361152,1572508721685839872,1571804585637974016,'哈哈哈哈哈','2022-11-14 15:54:04','2022-12-01 16:48:10'),(1592064426667241472,1579019429336363008,1571804585637974016,'百合gg','2022-11-14 15:58:33','2022-11-14 15:58:33'),(1592069045388668928,1578393575279321088,1571804585637974016,'嘉实多','2022-11-14 16:16:54','2022-11-14 16:16:54'),(1592069139982807040,1573953638006116352,1571804585637974016,'jk有意思？','2022-11-14 16:17:16','2022-11-14 16:17:16'),(1592349682360217600,1573240094281240576,1571804585637974016,'xlg------','2022-11-15 10:52:03','2022-11-15 10:52:03'),(1592349736324132864,1573324140009017344,1571804585637974016,'背景还行吧','2022-11-15 10:52:16','2022-12-02 16:20:44'),(1592349826493280256,1574286298175799296,1571804585637974016,'你愤怒个鸡儿','2022-11-15 10:52:37','2022-11-15 10:52:37'),(1592349866725044224,1578709444933029888,1571804585637974016,'看啥看','2022-11-15 10:52:47','2022-11-15 10:52:47'),(1592349961650532352,1578964177039986688,1571804585637974016,'404 6666','2022-11-15 10:53:09','2022-11-15 10:53:09'),(1592356962451423232,1573322637529608192,1571804585637974016,'看不看','2022-11-15 11:20:59','2022-11-15 11:20:59'),(1598931851488620544,1575868221125820416,1575444479153295360,'10086','2022-12-03 14:47:14','2022-12-03 14:47:14'),(1600788949604257792,1573322637529608192,1575444479153295360,'我来试试','2022-12-08 17:46:41','2022-12-08 17:46:41'),(1600788989030715392,1573322637529608192,1575444479153295360,'哈哈哈','2022-12-08 17:46:50','2022-12-08 17:46:50'),(1604774538536128512,1575129660344836096,1575444479153295360,'11447','2022-12-19 17:43:59','2022-12-19 17:43:59'),(1605962771487875072,1575129660344836096,1575444479153295360,'xxxxxxxxx','2022-12-23 00:25:36','2022-12-23 00:25:36'),(1605963833229737984,1575129660344836096,1575444479153295360,'ggggggg','2022-12-23 00:29:49','2022-12-23 00:29:49'),(1605968330895237120,1575129660344836096,1575444479153295360,'ggggggggggg','2022-12-23 00:47:42','2022-12-23 00:47:42'),(1605968601507536896,1575129660344836096,1575444479153295360,'xssssss','2022-12-23 00:48:46','2022-12-23 00:48:46'),(1605969456684179456,1575129660344836096,1575444479153295360,'sdfffff','2022-12-23 00:52:10','2022-12-23 00:52:10'),(1605970363962417152,1575129660344836096,1575444479153295360,'85555','2022-12-23 00:55:46','2022-12-23 00:55:46'),(1605970517696241664,1575129660344836096,1575444479153295360,'699999','2022-12-23 00:56:23','2022-12-23 00:56:23'),(1607283241373937664,1575129660344836096,1575444479153295360,'mmm','2022-12-26 15:52:41','2022-12-26 15:52:41'),(1607289328399572992,1575129660344836096,1575444479153295360,'iuggggggg','2022-12-26 16:16:52','2022-12-26 16:16:52'),(1607320152369094656,1578393575279321088,1575444479153295360,'哈哈哈','2022-12-26 18:19:21','2022-12-26 18:19:21'),(1607631079752704000,1578964177039986688,1572443275490078720,'hhhhhhh','2022-12-27 14:54:52','2023-01-03 14:34:24'),(1608491376126291968,1575129660344836096,1575444479153295360,'xxxxxxxx','2022-12-29 23:53:23','2022-12-29 23:53:23'),(1610190322204798976,1610188155725148160,1572443275490078720,'xxfg','2023-01-03 16:24:23','2023-01-03 16:24:23'),(1610283980417425408,1579088909127155712,1572443275490078720,'就是义勇啊66','2023-01-03 22:36:33','2023-01-04 08:27:15'),(1611727257503125504,1611727027223252992,1610563829040152576,'楼主op','2023-01-07 22:11:37','2023-01-10 06:05:32'),(1611727288897490944,1611727027223252992,1610563829040152576,'沙发也是我的','2023-01-07 22:11:44','2023-01-07 22:11:44'),(1612460869299060736,1573322637529608192,1610563829040152576,'testing','2023-01-09 22:46:43','2023-01-09 22:46:43'),(1612693198600785920,1573322637529608192,1610563829040152576,'ugugugh','2023-01-10 14:09:55','2023-01-10 14:09:55');
/*!40000 ALTER TABLE `tb_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_star`
--

DROP TABLE IF EXISTS `tb_star`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_star` (
  `user_id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`topic_id`),
  KEY `index_star_topic_id` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_star`
--

LOCK TABLES `tb_star` WRITE;
/*!40000 ALTER TABLE `tb_star` DISABLE KEYS */;
INSERT INTO `tb_star` VALUES (1571804585637974016,1573322637529608192,'2022-11-08 15:13:52'),(1571804585637974016,1575129660344836096,'2022-12-01 22:47:05'),(1571804585637974016,1578393575279321088,'2022-11-21 11:32:33'),(1571804585637974016,1578709444933029888,'2022-11-14 16:02:02'),(1571804585637974016,1584469610554998784,'2022-10-24 16:59:31'),(1571804585637974016,1586281501153615872,'2022-10-29 17:00:30'),(1572443275490078720,1573322637529608192,'2023-01-07 22:43:45'),(1572443275490078720,1575129660344836096,'2022-12-27 14:54:20'),(1572443275490078720,1578393575279321088,'2022-12-27 14:54:32'),(1572443275490078720,1578709444933029888,'2022-12-27 14:54:27'),(1572443275490078720,1578964177039986688,'2022-12-27 14:54:47'),(1572443275490078720,1579088909127155712,'2023-01-03 21:36:14'),(1572443275490078720,1610188155725148160,'2023-01-03 16:28:25'),(1572517648460795904,1573240094281240576,'2022-10-02 20:19:24'),(1572517648460795904,1575129660344836096,'2022-09-30 15:44:09'),(1575444479153295360,1572508721685839872,'2022-10-09 12:13:28'),(1575444479153295360,1573322637529608192,'2022-10-09 11:49:35'),(1575444479153295360,1573324140009017344,'2022-10-09 11:53:19'),(1575444479153295360,1573953638006116352,'2022-10-09 12:19:09'),(1575444479153295360,1574286298175799296,'2022-10-09 11:49:27'),(1575444479153295360,1575129660344836096,'2022-12-26 16:16:30'),(1575444479153295360,1575868221125820416,'2022-12-08 18:29:33'),(1575444479153295360,1578709444933029888,'2022-12-26 16:45:43'),(1578720796628017152,1572508721685839872,'2022-10-08 20:19:05'),(1578720796628017152,1573322637529608192,'2022-10-09 16:03:39'),(1578720796628017152,1575129660344836096,'2022-10-09 16:03:47'),(1578720796628017152,1575868221125820416,'2022-10-24 15:35:59'),(1578720796628017152,1578709444933029888,'2022-10-17 16:29:30'),(1578720796628017152,1578964177039986688,'2022-10-17 17:00:13'),(1578720796628017152,1579019429336363008,'2022-10-09 16:03:22'),(1578720796628017152,1579088909127155712,'2022-10-12 17:10:30'),(1578720796628017152,1583082176776892416,'2022-10-20 21:12:19'),(1610563829040152576,1573322637529608192,'2023-01-04 17:10:29'),(1610563829040152576,1575129660344836096,'2023-01-09 22:36:28'),(1610563829040152576,1579088909127155712,'2023-01-04 17:09:53');
/*!40000 ALTER TABLE `tb_star` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_test`
--

DROP TABLE IF EXISTS `tb_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_test` (
  `test_id` bigint NOT NULL,
  `test_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_test`
--

LOCK TABLES `tb_test` WRITE;
/*!40000 ALTER TABLE `tb_test` DISABLE KEYS */;
INSERT INTO `tb_test` VALUES (1,'xlg');
/*!40000 ALTER TABLE `tb_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_topic`
--

DROP TABLE IF EXISTS `tb_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_topic` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `title` varchar(30) NOT NULL COMMENT '长度≥4',
  `content` varchar(600) NOT NULL,
  `star_count` int DEFAULT '0',
  `page_view` int DEFAULT '0',
  `comment_count` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `first_image_url` varchar(50) DEFAULT NULL,
  `activity_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_topic_user_id` (`user_id`),
  CONSTRAINT `fk_topic_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user_basic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_topic`
--

LOCK TABLES `tb_topic` WRITE;
/*!40000 ALTER TABLE `tb_topic` DISABLE KEYS */;
INSERT INTO `tb_topic` VALUES (1572508721685839872,1572443275490078720,'测试test---超级长标题超级长标题超级长标题超级长标题超','test-test',2,53,13,'2022-09-21 16:51:09','2022-09-26 20:51:22','topicImage/2022/12/02/1572508721903943680','2022-09-22 15:32:36'),(1573240094281240576,1572443275490078720,'xlg不是变态','牛逼吧！！！',1,41,2,'2022-09-23 17:17:22','2022-09-26 21:42:49','topicImage/2022/12/02/1573240094587424768','2022-10-07 21:28:55'),(1573322637529608192,1572443275490078720,'刀剑爱丽丝','<p style=\"text-align: center;\">刀剑迷冒泡！！</p>\n<p style=\"text-align: center;\"><span style=\"color: rgb(241, 196, 15);\">全体起立</span>🥳</p>\n<p style=\"text-align: center;\"><strong>哈</strong><sup>哈</sup><strong>哈</strong></p>',5,75,8,'2022-09-23 22:45:22','2023-01-07 14:42:27','topicImage/2022/12/02/1573322637990981632','2023-01-10 06:09:56'),(1573324140009017344,1572443275490078720,'背景图一张','这个二次元三次元混合图片如何',1,47,3,'2022-09-23 22:51:20','2022-09-23 22:51:20','topicImage/2022/12/02/1573324140340367360','2022-09-26 21:43:34'),(1573953638006116352,1572443275490078720,'来几张JK','这几张如何，画师牛逼',1,20,5,'2022-09-25 16:32:44','2022-09-25 16:32:44','topicImage/2022/12/02/1573953638224220160','2022-09-25 16:33:03'),(1574286298175799296,1572443275490078720,'愤怒愤怒','愤怒狗头哈哈哈',1,18,2,'2022-09-26 14:34:36','2022-09-26 14:34:36','topicImage/2022/12/02/1574286298305822720','2022-10-07 22:18:04'),(1575129660344836096,1572517648460795904,'这个妹子如何','二次元yyds！！！',6,74,12,'2022-09-28 22:25:50','2022-09-30 15:44:18','topicImage/2022/12/02/1575129660659408896','2022-12-29 15:53:23'),(1575868221125820416,1572517648460795904,'中忍、大忍','物语系列快来看',2,44,4,'2022-09-30 23:20:36','2022-09-30 23:20:36','topicImage/2022/12/02/1575868221272621056','2022-09-30 23:20:54'),(1578393575279321088,1572517648460795904,'双重狗头','999999999999997',2,22,6,'2022-10-07 22:35:28','2022-10-07 22:36:11','topicImage/2022/12/02/1578393575283515392','2022-12-26 10:19:22'),(1578709444933029888,1575444479153295360,'水源千鹤天下第一','我wk就说水源千鹤yyds',4,25,3,'2022-10-08 19:30:37','2022-10-08 19:30:37','topicImage/2022/12/02/1578709444954001408','2022-10-08 19:30:47'),(1578964177039986688,1575444479153295360,'404之神','404哈哈哈',2,23,3,'2022-10-09 12:22:50','2022-10-09 12:22:50','topicImage/2022/12/02/1578964177170010112','2023-01-03 14:34:24'),(1579019429336363008,1578720796628017152,'百合贴贴','我周哥就爱看这个',1,10,1,'2022-10-09 16:02:23','2022-10-09 16:02:23','topicImage/2022/12/02/1579019429604798464','2022-11-14 15:58:33'),(1579088909127155712,1578720796628017152,'请问这个是谁','义勇？？？？',3,10,2,'2022-10-09 20:38:28','2022-10-09 20:38:45','topicImage/2022/12/02/1579088909441728512','2023-01-04 08:27:15'),(1583082176776892416,1578720796628017152,'868767','5465464',1,2,0,'2022-10-20 21:06:17','2022-10-20 21:06:17','topicImage/2022/12/02/1583082177125019648','2022-10-20 21:06:17'),(1584469610554998784,1571804585637974016,'xlg2的分享','小号测试一下',1,1,1,'2022-10-24 16:59:27','2022-10-24 16:59:27','topicImage/2022/12/02/1584469610789879808','2022-10-24 17:00:10'),(1585529145054986240,1571804585637974016,'相似图片测试（藤原千花）','以图搜图看下牛不牛',0,1,0,'2022-10-27 15:09:40','2022-10-27 15:09:40','topicImage/2022/12/02/1585529145067569152','2022-10-27 15:09:40'),(1586281501153615872,1571804585637974016,'测试修改为“话题”后','测试修改为“话题”后sadad',1,5,4,'2022-10-29 16:59:16','2022-11-21 11:34:03','topicImage/2022/12/02/1586281501346553856','2022-10-29 16:59:46'),(1610188155725148160,1572443275490078720,'test 用户-社区-图片 服务拆分','<p style=\"text-align: center;\">😀成功吧</p>\n<p style=\"text-align: left;\">😇</p>\n<p style=\"text-align: center;\">🤑</p>\n<p style=\"text-align: center;\"><s><strong>asdasd</strong></s></p>',1,6,1,'2023-01-03 08:15:47','2023-01-03 16:15:46','topicImage/2023/01/03/1610188153128869888.png','2023-01-03 08:44:32'),(1611727027223252992,1610563829040152576,'这张爱丽丝壁纸666','<p style=\"text-align: center;\">😎好+美+呀🥰</p>',0,3,2,'2023-01-07 14:10:42','2023-01-10 06:09:11','topicImage/2023/01/07/1611727025084055552.jpeg','2023-01-10 06:09:11');
/*!40000 ALTER TABLE `tb_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_basic`
--

DROP TABLE IF EXISTS `tb_user_basic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_basic` (
  `id` bigint NOT NULL,
  `username` varchar(12) NOT NULL,
  `photo_url` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_basic`
--

LOCK TABLES `tb_user_basic` WRITE;
/*!40000 ALTER TABLE `tb_user_basic` DISABLE KEYS */;
INSERT INTO `tb_user_basic` VALUES (1571804585637974016,'xlg2',NULL),(1572443275490078720,'xlg','userPhoto/2023/01/03/1610250118526259200.jpeg'),(1572517648460795904,'wyn',NULL),(1575444479153295360,'wk','userPhoto/2022/12/08/1600794677752725504.jpeg'),(1578720796628017152,'wyk',NULL),(1610563829040152576,'test','userPhoto/2023/01/09/1612457916328574976.jpeg');
/*!40000 ALTER TABLE `tb_user_basic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_community_statistic`
--

DROP TABLE IF EXISTS `tb_user_community_statistic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_community_statistic` (
  `user_id` bigint NOT NULL,
  `topic_count` int DEFAULT '0',
  `star_count` int DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_community_statistic`
--

LOCK TABLES `tb_user_community_statistic` WRITE;
/*!40000 ALTER TABLE `tb_user_community_statistic` DISABLE KEYS */;
INSERT INTO `tb_user_community_statistic` VALUES (1571804585637974016,3,2),(1572443275490078720,7,12),(1572517648460795904,3,10),(1575444479153295360,2,6),(1578720796628017152,3,5),(1610563829040152576,1,0);
/*!40000 ALTER TABLE `tb_user_community_statistic` ENABLE KEYS */;
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
