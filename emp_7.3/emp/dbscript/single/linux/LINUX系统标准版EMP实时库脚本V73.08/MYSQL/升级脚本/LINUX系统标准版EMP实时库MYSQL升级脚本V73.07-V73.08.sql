/*****V73.07-V73.08 END*/
DELIMITER $
DROP PROCEDURE IF EXISTS `LF_PVERV0`$

CREATE  DEFINER=`root`@`%`  PROCEDURE `LF_PVERV0`(
    DBVERSIONSTR VARCHAR(32),
    NUM INT,
    TOTALSTR INT
)
BEGIN
    IF NOT EXISTS(SELECT * FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR) THEN
    /*EMP产品数据库版本信息表*/ 
    INSERT INTO LF_DB_SCRIPT(VERSION,UPDATETIME,CREATETIME,CURRENT_NO,TOTAL,STATE,MEMO)
    VALUES(DBVERSIONSTR,NOW(),NOW(),NUM,TOTALSTR,1,'WEB包更改');
    END IF;
END $        
   
CALL LF_PVERV0('73.08',1,1)$
DROP PROCEDURE IF EXISTS `LF_PVERV0`$

/*WEB升级脚本 START*/
/*WEB升级脚本 END*/

/*网关升级脚本 START*/
/*网关升级脚本 END*/

DROP PROCEDURE IF EXISTS `LF_PVERV0`$

CREATE  DEFINER=`root`@`%` PROCEDURE `LF_PVERV0`(
    VERSIONSTR VARCHAR(32),
    DBVERSIONSTR VARCHAR(32),
    WBSVERSION VARCHAR(32),
    SPGATEVERSION VARCHAR(32),
    NUM INT,
    TOTALSTR INT
)
BEGIN
	/*EMP产品版本记录*/
	/*EMP-WEB*/
	IF NOT EXISTS(SELECT * FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000) THEN
		INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
		VALUES(1000,1000,VERSIONSTR,NOW(),NOW(),'EMP-WEB');
	ELSE
		UPDATE LF_VERSION SET UPDATETIME=NOW(),VERSION=VERSIONSTR WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
	END IF;
	/*EMP_GATEWAY*/
	IF NOT EXISTS(SELECT * FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=2000) THEN
		INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
		VALUES(1000,2000,WBSVERSION,NOW(),NOW(),'EMP_GATEWAY');
	ELSE
		UPDATE LF_VERSION SET UPDATETIME=NOW(),VERSION=WBSVERSION WHERE  PRODUCT_ID=1000 AND PROCESS_ID=2000;
	END IF;
	/*SMT_SPGATE*/
	IF NOT EXISTS(SELECT * FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=3000) THEN
		INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
		VALUES(1000,3000,SPGATEVERSION,NOW(),NOW(),'SMT_SPGATE');
	ELSE
		UPDATE LF_VERSION SET UPDATETIME=NOW(),VERSION=SPGATEVERSION WHERE  PRODUCT_ID=1000 AND PROCESS_ID=3000;
	END IF;
	/*EMP产品版本历史记录*/
	/*EMP-WEB*/
	IF NOT EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=1000) THEN
			INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO,ISRELEASE,VERSIONINFO)
			VALUES(1000,1000,VERSIONSTR,NOW(),NOW(),'EMP-WEB',1,'1.修复群发任务查看修改定时任务时，提示发送内容不能为空，导致不能修改定时任务的问题<br/>2.修复状态码导入模板时出错的时候，会闪现多个dialog框的问题<br/>3.查询统计-数据查询-自定参数统计报表，选择时间区间为同一天时间查询，无法查询出数据，实际当天有数据<br/>4.查询统计-统计报表，运营商/SP账号/机构/操作员/业务类型/区域报表，查询统计-数据分析，总体发送趋势，短信类型应为信息类型<br/>5.解决插入操作日志表超时的问题<br/>6.解决V73.00 Oracle全新脚本mt_datareport表缺少recfail字段<br/>7.解决查询统计-统计报表-区域统计报表模块，oracle/db2环境查询报错');
	ELSE
			UPDATE LF_VERSION_HIS SET UPDATETIME=NOW(),ISRELEASE=1,VERSIONINFO='1.修复群发任务查看修改定时任务时，提示发送内容不能为空，导致不能修改定时任务的问题<br/>2.修复状态码导入模板时出错的时候，会闪现多个dialog框的问题<br/>3.查询统计-数据查询-自定参数统计报表，选择时间区间为同一天时间查询，无法查询出数据，实际当天有数据<br/>4.查询统计-统计报表，运营商/SP账号/机构/操作员/业务类型/区域报表，查询统计-数据分析，总体发送趋势，短信类型应为信息类型<br/>5.解决插入操作日志表超时的问题<br/>6.解决V73.00 Oracle全新脚本mt_datareport表缺少recfail字段<br/>7.解决查询统计-统计报表-区域统计报表模块，oracle/db2环境查询报错' WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
	END IF;
	/*EMP_GATEWAY*/
	IF NOT EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=WBSVERSION AND PRODUCT_ID=1000 AND PROCESS_ID=2000) THEN
		INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
		VALUES(1000,2000,WBSVERSION,NOW(),NOW(),'EMP_GATEWAY');
	ELSE
		UPDATE LF_VERSION_HIS SET UPDATETIME=NOW() WHERE  PRODUCT_ID=1000 AND PROCESS_ID=2000;
	END IF;
	/*SMT_SPGATE*/
	IF NOT EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=SPGATEVERSION AND PRODUCT_ID=1000 AND PROCESS_ID=3000) THEN
		INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
		VALUES(1000,3000,SPGATEVERSION,NOW(),NOW(),'SMT_SPGATE');
	ELSE
		UPDATE LF_VERSION_HIS SET UPDATETIME=NOW() WHERE  PRODUCT_ID=1000 AND PROCESS_ID=3000;
	END IF;
	IF NOT EXISTS(SELECT * FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR AND CURRENT_NO=NUM AND TOTAL=TOTALSTR) THEN
		/*EMP产品数据库版本信息表*/ 
		INSERT INTO LF_DB_SCRIPT(VERSION,UPDATETIME,CREATETIME,CURRENT_NO,TOTAL,STATE,MEMO)
		VALUES(DBVERSIONSTR,NOW(),NOW(),NUM,TOTALSTR,2,'1号脚本');
	ELSE
		UPDATE LF_DB_SCRIPT SET STATE=2,UPDATETIME=NOW() WHERE  CURRENT_NO=NUM AND TOTAL=TOTALSTR;
	END IF;
END     $
CALL LF_PVERV0('7.3.8.612.SP8','73.08','8.6.4.219','6.1.52.344',1,1) $
DROP PROCEDURE IF EXISTS `LF_PVERV0` $
/*****V73.07-V73.08 END*/