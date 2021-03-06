/*****V73.06-V73.07 END*/
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
   
CALL LF_PVERV0('73.07',1,1)$
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
			VALUES(1000,1000,VERSIONSTR,NOW(),NOW(),'EMP-WEB',1,'1.修复查询统计-统计报表，SP账号/机构/操作员/业务类型统计报表，页面查询不出数据<br/>2.修复移动办公-员工通讯录管理-员工通讯录，员工手机号码修改不生效');
	ELSE
			UPDATE LF_VERSION_HIS SET UPDATETIME=NOW(),ISRELEASE=1,VERSIONINFO='1.修复查询统计-统计报表，SP账号/机构/操作员/业务类型统计报表，页面查询不出数据<br/>2.修复移动办公-员工通讯录管理-员工通讯录，员工手机号码修改不生效' WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
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
CALL LF_PVERV0('7.3.7.607.SP7','73.07','8.6.4.219','6.1.52.344',1,1) $
DROP PROCEDURE IF EXISTS `LF_PVERV0` $
/*****V73.06-V73.07 END*/