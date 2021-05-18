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
   
CALL LF_PVERV0('73.10',1,1)$
DROP PROCEDURE IF EXISTS `LF_PVERV0`$

/*WEB升级脚本 START*/

DELIMITER $
DROP PROCEDURE IF EXISTS `INSERTPROCEDURE`$
CREATE  DEFINER=`root`@`%`  PROCEDURE `INSERTPROCEDURE`()
BEGIN

-- 添加群发历史查询汇总菜单
IF NOT EXISTS(SELECT * FROM LF_PRIVILEGE WHERE PRIVILEGE_ID=7009)
THEN
INSERT INTO LF_PRIVILEGE ( PRIVILEGE_ID, RESOURCE_ID, operate_id, COMMENTS, PRIV_CODE, MENUNAME, MODNAME, MENUCODE, MENUSITE, ZH_TW_COMMENTS, ZH_HK_COMMENTS, ZH_TW_MENUNAME, ZH_HK_MENUNAME, ZH_TW_MODNAME, ZH_HK_MODNAME)
VALUES
	( 7009, 27, NULL, '汇总', '1520-1700-1', '群发历史查询', '数据查询', '1520-1700', NULL, '汇总', 'Task', '群發歷史查詢', 'Task history view', '數據查詢', 'Data query' );
END IF;

-- 添加群发历史查询汇总权限
IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=7009)
THEN
INSERT INTO LF_IMPOWER (ROLE_ID,PRIVILEGE_ID) VALUES(1,7009);
END IF;

IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=7009)
THEN
INSERT INTO LF_IMPOWER (ROLE_ID,PRIVILEGE_ID) VALUES(2,7009);
END IF;

IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=7009)
THEN
INSERT INTO LF_IMPOWER (ROLE_ID,PRIVILEGE_ID) VALUES(4,7009);
END IF;

-- 添加汇总转移日志菜单
IF NOT EXISTS(SELECT * FROM LF_PRIVILEGE WHERE PRIVILEGE_ID=3594)
THEN
INSERT INTO LF_PRIVILEGE
(PRIVILEGE_ID, RESOURCE_ID, OPERATE_ID, COMMENTS, PRIV_CODE, MENUNAME, MODNAME, MENUCODE, MENUSITE, ZH_TW_COMMENTS,ZH_HK_COMMENTS,ZH_TW_MENUNAME,ZH_HK_MENUNAME,ZH_TW_MODNAME,ZH_HK_MODNAME)
values
(3594, 10, 1, '查看', '1600-2100-0', '汇总转移日志', '操作员管理', '1600-2100', '/transLog.htm', '查看', 'View','匯總轉移日誌','Trans log','操作員管理','Operator');
END IF;

-- 添加汇总转移日志权限
IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=3594)
THEN
INSERT INTO LF_IMPOWER (ROLE_ID,PRIVILEGE_ID) VALUES(1,3594);
END IF;

IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=3594)
THEN
INSERT INTO LF_IMPOWER (ROLE_ID,PRIVILEGE_ID) VALUES(2,3594);
END IF;

IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=3594)
THEN
INSERT INTO LF_IMPOWER (ROLE_ID,PRIVILEGE_ID) VALUES(4,3594);
END IF;

-- 添加WEB运行参数配置页面菜单
IF NOT EXISTS (SELECT * FROM LF_PRIVILEGE WHERE PRIVILEGE_ID = 3595)
THEN
INSERT INTO LF_PRIVILEGE VALUES (3595, 8, 1, '查看', '1400-1900-0', 'WEB运行参数配置', '参数维护', '1400-1900', '/webParamConfig.htm', '查看', 'View', 'WEB運行參數配置', 'Web Parameter', '參數維護', 'Parameter');
END IF;

-- 添加web运行参数页面的查看权限
IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=3595)
THEN
INSERT INTO LF_IMPOWER VALUES(1,3595);
END IF;

IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=3595)
THEN
INSERT INTO LF_IMPOWER VALUES(2,3595);
END IF;

IF NOT EXISTS(SELECT * FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=3595)
THEN
INSERT INTO LF_IMPOWER VALUES(4,3595);
END IF;

-- 增加是否检查运营商余额的配置项
IF NOT EXISTS (SELECT * FROM LF_GLOBAL_VARIABLE WHERE GLOBALID=601 AND GLOBALKEY='GWFEE_CHECK')
THEN
INSERT INTO LF_GLOBAL_VARIABLE(GLOBALID,GLOBALKEY,GLOBALVALUE,MEMO) VALUES(601,'GWFEE_CHECK',1,'是否检查运营商余额 1:是,0:否');
END IF;

-- 添加登录页面是否需要验证码的配置项
IF NOT EXISTS (SELECT * FROM LF_SYS_PARAM WHERE PARAM_ITEM = 'loginYanZhengMa')
THEN
INSERT INTO LF_SYS_PARAM(PARAM_ITEM,PARAM_VALUE,MEMO)
 VALUES('loginYanZhengMa','true','登录页面是否需要验证码,true:需要;false:不需要');
END IF;

-- 添加系统下行记录导出的最大记录条数配置项
IF NOT EXISTS(SELECT * FROM LF_SYS_PARAM WHERE PARAM_ITEM = 'cxtjMtExportLimit')
THEN
INSERT INTO LF_SYS_PARAM(PARAM_ITEM,PARAM_VALUE,MEMO) VALUES
 ('cxtjMtExportLimit','1000000','系统下行记录导出的最大记录条数，默认100万');
END IF;

-- HTTP请求超时时间(毫秒)  发送HTTP请求超时时间，由120秒改成150秒 ------------
UPDATE LF_GLOBAL_VARIABLE SET GLOBALVALUE=150000 WHERE GLOBALID=44 AND GLOBALKEY='HTTP_REQUEST_TIMEOUT';
-- HTTP响应超时时间(毫秒)  发送HTTP请求超时时间，由120秒改成150秒 ------------
UPDATE LF_GLOBAL_VARIABLE SET GLOBALVALUE=150000 WHERE GLOBALID=45 AND GLOBALKEY='HTTP_RESPONSE_TIMEOUT';

END $
CALL `INSERTPROCEDURE`$
DROP PROCEDURE IF EXISTS `INSERTPROCEDURE`$

/*WEB升级脚本 END*/

/*网关升级脚本 START*/
/*网关升级脚本 END*/

DELIMITER $
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
			VALUES(1000,1000,VERSIONSTR,NOW(),NOW(),'EMP-WEB',1,'1.系统管理->参数维护，新增WEB运行参数配置功能；<br>2.查询统计->数据查询->群发历史查询，新增单个任务汇总功能；<br>3.系统管理->操作员管理，新增汇总转移日志查询功能。');
	ELSE
			UPDATE LF_VERSION_HIS SET UPDATETIME=NOW(),ISRELEASE=1,VERSIONINFO='1.系统管理->参数维护，新增WEB运行参数配置功能；<br>2.查询统计->数据查询->群发历史查询，新增单个任务汇总功能；<br>3.系统管理->操作员管理，新增汇总转移日志查询功能。' WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
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
CALL LF_PVERV0('7.3.10.669.SP10','73.10','8.6.5.220','6.1.53.345',1,1) $
DROP PROCEDURE IF EXISTS `LF_PVERV0` $
