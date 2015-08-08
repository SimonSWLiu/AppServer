/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.6.22 : Database - onemenudb
*********************************************************************
*/

USE `onemenudb`;

/*Table structure for table `config` */

CREATE TABLE `config` (
  `key` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '主键 key',
  `value` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '值',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='配置表（所有配置应写入这个表）';

