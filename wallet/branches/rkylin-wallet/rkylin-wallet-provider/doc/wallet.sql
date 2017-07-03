/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50623
Source Host           : localhost:3306
Source Database       : wallet

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2016-08-19 14:58:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `company_main_info`
-- ----------------------------
DROP TABLE IF EXISTS `COMPANY_MAIN_INFO`;
CREATE TABLE `COMPANY_MAIN_INFO` (
  `COMPANY_ID` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `COMPANY_CODE` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '企业编码',
  `COMPANY_NAME` varchar(120) CHARACTER SET utf8 DEFAULT NULL COMMENT '企业名称',
  `COMPANY_SHORT_NAME` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '企业简称',
  `COMPANY_TYPE` tinyint(2) unsigned DEFAULT NULL COMMENT '企业类型',
  `MCC` varchar(120) CHARACTER SET utf8 DEFAULT NULL COMMENT '行业编号',
  `POST` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '邮编',
  `FAX` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '传真',
  `PROVINCE` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '省份',
  `CITY` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '地市',
  `CONTACTS` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '联系人',
  `CONTACTS_INFO` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '联系方式',
  `REGIST_FINACNCE` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '注册资金',
  `MEMBERS` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '员工数',
  `ADDRESS` varchar(120) CHARACTER SET utf8 DEFAULT NULL COMMENT '地址',
  `WEBSITE` varchar(120) CHARACTER SET utf8 DEFAULT NULL COMMENT '网址',
  `BUSLINCE` varchar(60) CHARACTER SET utf8 NOT NULL COMMENT '营业执照号',
  `ACUNT_OPN_LINCE` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '开户许可证',
  `TAXREG_CARD1` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '税务登记证1',
  `TAXREG_CARD2` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '税务登记证2',
  `ORGAN_CERTIFICATE` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '住址机构代码证',
  `CORPORATE_NAME` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '法人姓名',
  `CORPORATE_IDENTITY` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '法人身份证',
  `CORPORATE_TEL` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '法人电话',
  `CORPORATE_MAIL` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '法人邮箱',
  `BUS_PLACE_CTF` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '经营场所实地认证',
  `LOAN_CARD` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '贷款卡',
  `OTHERINFO1` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备用1',
  `OTHERINFO2` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备用2',
  `OTHERINFO3` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备用3',
  `STATUS_ID` tinyint(2) DEFAULT NULL COMMENT '状态:1正常,9删除,2停用',
  `REMARK` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`COMPANY_ID`),
  KEY `UK_USER_CODE` (`COMPANY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='企业信息表';

-- ----------------------------
-- Records of company_main_info
-- ----------------------------

-- ----------------------------
-- Table structure for `pwd_question`
-- ----------------------------
DROP TABLE IF EXISTS `PWD_QUESTION`;
CREATE TABLE `PWD_QUESTION` (
  `QUESTION_ID` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `QUESTION_NAME` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '问题名称',
  `QUESTION_ANSWER` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '问题答案',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`QUESTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='密码问题表答案表';

-- ----------------------------
-- Records of pwd_question
-- ----------------------------

-- ----------------------------
-- Table structure for `pwd_retrieve`
-- ----------------------------
DROP TABLE IF EXISTS `PWD_RETRIEVE`;
CREATE TABLE `PWD_RETRIEVE` (
  `ID` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '登陆ID',
  `QUESTION_ID` bigint(20) DEFAULT NULL COMMENT '问题ID',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='密码找回表';

-- ----------------------------
-- Records of pwd_retrieve
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login`
-- ----------------------------
DROP TABLE IF EXISTS `USER_LOGIN`;
CREATE TABLE `USER_LOGIN` (
  `LOGIN_ID` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '登陆ID',
  `USER_ID` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户编码(个人或企业)',
  `USER_TYPE` tinyint(2) DEFAULT NULL COMMENT '1 个人用户 2 企业用户',
  `PWD_SALT` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '密码盐值',
  `PWD_MD` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `LOGIN_NAME_TEL` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '登陆名(手机)',
  `LOGIN_NAME_EMAIL` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '登陆名(邮箱) 扩展用',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`LOGIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户登陆信息表';

-- ----------------------------
-- Records of user_login
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login_history`
-- ----------------------------
DROP TABLE IF EXISTS `USER_LOGIN_HISTORY`;
CREATE TABLE `USER_LOGIN_HISTORY` (
  `ID` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '登陆ID',
  `LOGIN_IP` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT 'IP',
  `LOGIN_TYPE` tinyint(2) DEFAULT NULL COMMENT '类型 1登陆 2退出',
  `LONG_SOURCE` tinyint(2) NOT NULL COMMENT '用户登陆来源 1 andird 2 ios 3 pc',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='登陆登出历史表';

-- ----------------------------
-- Records of user_login_history
-- ----------------------------

-- ----------------------------
-- Table structure for `user_main_info`
-- ----------------------------
DROP TABLE IF EXISTS `USER_MAIN_INFO`;
CREATE TABLE `USER_MAIN_INFO` (
  `USER_ID` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USER_CODE` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户编码',
  `USER_CNAME` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '用户中文名字',
  `USER_ENAME` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '用户英文名字',
  `USER_TYPE` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '用户类型',
  `GENDER` char(1) CHARACTER SET utf8 NOT NULL COMMENT '用户性别',
  `BIRTHDAY` date DEFAULT NULL COMMENT '用户生日',
  `ID_NUMBER` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '身份证号',
  `ADDRESS` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '用户地址',
  `MOBILE_PHONE` varchar(16) CHARACTER SET utf8 NOT NULL COMMENT '移动电话',
  `FIXED_PHONE` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '固定电话',
  `EMAIL` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '邮箱',
  `HANDLER_FAX` varchar(30) CHARACTER SET utf8 NOT NULL COMMENT '传真号',
  `NATIONAL` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '国家',
  `PROVINCE` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '省',
  `CITY` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '市',
  `DISTRICT` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '区',
  `ZIP_CODE` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT '邮政编码',
  `EDUCATION` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT '学历',
  `POLITICAL_STATUS` varchar(16) CHARACTER SET utf8 DEFAULT NULL COMMENT '政治面貌',
  `INDUSTRY` varchar(8) CHARACTER SET utf8 NOT NULL COMMENT '所属行业',
  `COMPANY_NAME` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '公司名称',
  `COMPANY_ATTRIBUTE` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '公司性质',
  `COMPANY_PHONE` varchar(16) CHARACTER SET utf8 NOT NULL COMMENT '公司电话',
  `JOB_ATTRIBUTE` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '工作性质',
  `OCCUPATION` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '职位',
  `MARRIAGE` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT '是否结婚',
  `FAMILY_SIZE` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '家庭人口数量',
  `PERSON_INCOME` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT '个人收入',
  `FAMILY_INCOME` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT '家庭收入',
  `COME_FROM` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户来源',
  `QQ_NUMBER` varchar(16) CHARACTER SET utf8 DEFAULT NULL COMMENT 'QQ号',
  `WECHAT_NUMBER` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '微信号',
  `WEBO_NUMBER` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '微博号',
  `ISAUTHOR` tinyint(2) DEFAULT NULL COMMENT '实名验证类型',
  `OTHERINFO1` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '备用1',
  `OTHERINFO2` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '备用2',
  `OTHERINFO3` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '备用3',
  `STATUS_ID` tinyint(2) NOT NULL DEFAULT '0' COMMENT '用户状态:1正常,-9删除,-2停用',
  `REMARK` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '用户备注',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`USER_ID`),
  KEY `UK_USER_CODE` (`USER_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户信息表';

-- ----------------------------
-- Records of user_main_info
-- ----------------------------
