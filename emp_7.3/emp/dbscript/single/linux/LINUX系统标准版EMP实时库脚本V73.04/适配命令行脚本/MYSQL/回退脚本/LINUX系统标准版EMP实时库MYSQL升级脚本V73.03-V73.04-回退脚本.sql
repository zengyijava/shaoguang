/*****V73.03-V73.04 END*/
DELIMITER $
DROP PROCEDURE IF EXISTS `LF_PVERV0` $

CREATE DEFINER=`root`@`%`  PROCEDURE `LF_PVERV0`(
    DBVERSIONSTR VARCHAR(32),
    NUM INT,
    TOTALSTR INT
)
BEGIN
    IF EXISTS(SELECT * FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR) THEN
      DELETE FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR;
    END IF;
END   $        
  
CALL LF_PVERV0('73.04',1,1) $
DROP PROCEDURE IF EXISTS `LF_PVERV0` $

/*网关升级脚本 START*/
/*网关升级脚本 END*/

/*WEB升级脚本 START*/
/*WEB升级脚本 END*/

DROP PROCEDURE IF EXISTS `LF_PVERV0` $

CREATE DEFINER=`root`@`%` PROCEDURE `LF_PVERV0`(
    VERSIONSTR VARCHAR(32),
    DBVERSIONSTR VARCHAR(32),
    WBSVERSION VARCHAR(32),
    SPGATEVERSION VARCHAR(32),
    VERSIONSTR_OLD VARCHAR(32),
    WBSVERSION_OLD VARCHAR(32),
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
			VALUES(1000,1000,VERSIONSTR,NOW(),NOW(),'EMP-WEB',1,'1、修复企业富信>富信应用>我的场景,富信模板提交审核大小和实际大小不符的BUG。<br />2、修复通信管理 > 网关前端配置 > 短彩SP账户 > 修改短彩SP账号，修改彩信SP账号计费类型后，页面卡死，之后无法进入修改页面的BUG。<br />3、修复企业富信 > 富信应用 > 富信发送，集群环境下，未开启定时服务的节点，发送动态模板定时任务时，会一直处于定时中，无法发送的BUG。<br />4、修复企业富信 > 富信应用 > 富信发送，上传6W号码+1个参数的文件，号码个数显示错误，且上传耗时3分钟的BUG。<br />5、修复企业富信-富信应用-我的场景，模板大小没有匹配到档位时，保存模板提示含有关键字的BUG。<br />6、修复企业富信 > 富信应用 > 我的场景，视频编辑时，使用的编辑器路径在程序中写死，配置文件不生效的BUG。<br />');
	ELSE
			UPDATE LF_VERSION_HIS SET UPDATETIME=NOW(),ISRELEASE=1,VERSIONINFO='1、修复企业富信>富信应用>我的场景,富信模板提交审核大小和实际大小不符的BUG。<br />2、修复通信管理 > 网关前端配置 > 短彩SP账户 > 修改短彩SP账号，修改彩信SP账号计费类型后，页面卡死，之后无法进入修改页面的BUG。<br />3、修复企业富信 > 富信应用 > 富信发送，集群环境下，未开启定时服务的节点，发送动态模板定时任务时，会一直处于定时中，无法发送的BUG。<br />4、修复企业富信 > 富信应用 > 富信发送，上传6W号码+1个参数的文件，号码个数显示错误，且上传耗时3分钟的BUG。<br />5、修复企业富信-富信应用-我的场景，模板大小没有匹配到档位时，保存模板提示含有关键字的BUG。<br />6、修复企业富信 > 富信应用 > 我的场景，视频编辑时，使用的编辑器路径在程序中写死，配置文件不生效的BUG。<br />' WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
	END IF;
    /*删除web旧版本历史*/
    IF EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000) THEN
        DELETE FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    END IF;
    /*删除新增EMP_GATEWAY*/
    IF EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=WBSVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000) THEN
        DELETE FROM LF_VERSION_HIS WHERE VERSION=WBSVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
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
END  $     

CALL LF_PVERV0('7.3.3.560.SP3','73.03','8.6.2.217','6.1.51.343','7.3.4.569.SP4','8.6.3.218',1,1) $
DROP PROCEDURE IF EXISTS `LF_PVERV0` $
/*****V73.02-V73.03 END*/