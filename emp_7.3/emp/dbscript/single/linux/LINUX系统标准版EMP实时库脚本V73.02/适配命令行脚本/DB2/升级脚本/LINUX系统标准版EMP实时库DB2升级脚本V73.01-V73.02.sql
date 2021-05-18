--------------------V73.01-V73.02 START------------------
BEGIN ATOMIC
DECLARE DBVERSIONSTR VARCHAR(32);
DECLARE NUMSTR INT;
DECLARE TOTALSTR INT;
SET DBVERSIONSTR = '73.02';
SET NUMSTR=1;
SET TOTALSTR=1;
IF NOT EXISTS (SELECT * FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR) THEN
--EMP产品数据库版本信息表 
INSERT INTO LF_DB_SCRIPT(VERSION,UPDATETIME,CREATETIME,CURRENT_NO,TOTAL,STATE,MEMO)
VALUES(DBVERSIONSTR,CURRENT TIMESTAMP,CURRENT TIMESTAMP,NUMSTR,TOTALSTR,1,'EMP网关更改');
END IF;
END &&


/*WEB升级脚本 START*/
/*WEB升级脚本 END*/


/*网关升级脚本 START*/
/*网关升级脚本   END*/


CREATE OR REPLACE PROCEDURE "ADDVERSIONEXISTS"
  BEGIN ATOMIC
    DECLARE VERSIONSTR VARCHAR(32);
    DECLARE DBVERSION VARCHAR(32);
    DECLARE WBSVERSION VARCHAR(32);
    DECLARE SPGATEVERSION VARCHAR(32);
    DECLARE NUM INT;
    DECLARE TOTAL INT;
    SET VERSIONSTR = '7.3.2.559.SP2';
    SET DBVERSION = '73.02';
    SET WBSVERSION = '8.6.2.217';
    SET SPGATEVERSION = '6.1.51.343';
    SET NUM =1;
    SET TOTAL=1;
    --EMP产品版本记录
    --EMP-WEB
    IF NOT EXISTS(SELECT * FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000)
    THEN
      INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
      VALUES(1000,1000,VERSIONSTR,CURRENT TIMESTAMP,CURRENT TIMESTAMP,'EMP-WEB');
    ELSE
      UPDATE LF_VERSION SET UPDATETIME=CURRENT TIMESTAMP,VERSION=VERSIONSTR WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
    END IF;
    --EMP_GATEWAY
    IF NOT EXISTS(SELECT * FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=2000)
    THEN
      INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
      VALUES(1000,2000,WBSVERSION,CURRENT TIMESTAMP,CURRENT TIMESTAMP,'EMP_GATEWAY');
    ELSE
      UPDATE LF_VERSION SET UPDATETIME=CURRENT TIMESTAMP,VERSION=WBSVERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=2000;
    END IF;
    --SMT_SPGATE
    IF NOT EXISTS(SELECT * FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=3000)
    THEN
      INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
      VALUES(1000,3000,SPGATEVERSION,CURRENT TIMESTAMP,CURRENT TIMESTAMP,'SMT_SPGATE');
    ELSE
      UPDATE LF_VERSION SET UPDATETIME=CURRENT TIMESTAMP,VERSION=SPGATEVERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=3000;
    END IF;
    --EMP产品版本历史记录
    --EMP-WEB
    IF NOT EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=1000)
    THEN
      INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO,ISRELEASE,VERSIONINFO)
      VALUES(1000,1000,VERSIONSTR,CURRENT TIMESTAMP,CURRENT TIMESTAMP,'EMP-WEB',1,'1、针对tpl_send发送接口支持请求参数tplid。<br />2、重发过滤中增加对模板ID的判断。<br />');
    ELSE
      UPDATE LF_VERSION_HIS SET UPDATETIME=CURRENT TIMESTAMP,ISRELEASE=1,VERSIONINFO='1、针对tpl_send发送接口支持请求参数tplid。<br />2、重发过滤中增加对模板ID的判断。<br />' WHERE VERSION=VERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    END IF;
    --EMP_GATEWAY
    IF NOT EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=WBSVERSION AND PRODUCT_ID=1000 AND PROCESS_ID=2000)
    THEN
      INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
      VALUES(1000,2000,WBSVERSION,CURRENT TIMESTAMP,CURRENT TIMESTAMP,'EMP_GATEWAY');
    ELSE
      UPDATE LF_VERSION_HIS SET UPDATETIME=CURRENT TIMESTAMP WHERE VERSION=WBSVERSION AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
    END IF;
    --SMT_SPGATE
    IF NOT EXISTS(SELECT * FROM LF_VERSION_HIS WHERE VERSION=SPGATEVERSION AND PRODUCT_ID=1000 AND PROCESS_ID=3000)
    THEN
      INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
      VALUES(1000,3000,SPGATEVERSION,CURRENT TIMESTAMP,CURRENT TIMESTAMP,'SMT_SPGATE');
    ELSE
      UPDATE LF_VERSION_HIS SET UPDATETIME=CURRENT TIMESTAMP WHERE VERSION=SPGATEVERSION AND PRODUCT_ID=1000 AND PROCESS_ID=3000;
    END IF;
    IF NOT EXISTS(SELECT * FROM LF_DB_SCRIPT WHERE VERSION=DBVERSION AND CURRENT_NO=NUM AND TOTAL=TOTAL)
    THEN
      --EMP产品数据库版本信息表
      INSERT INTO LF_DB_SCRIPT(VERSION,UPDATETIME,CREATETIME,CURRENT_NO,TOTAL,STATE,MEMO)
      VALUES(DBVERSION,CURRENT TIMESTAMP,CURRENT TIMESTAMP,NUM,TOTAL,2,'版本号更改');
    ELSE
      UPDATE LF_DB_SCRIPT SET STATE=2,UPDATETIME=CURRENT TIMESTAMP,MEMO='版本号更改' WHERE  VERSION=DBVERSION AND CURRENT_NO=NUM AND TOTAL=TOTAL;
    END IF;
  END  &&
CALL ADDVERSIONEXISTS  &&
DROP PROCEDURE ADDVERSIONEXISTS  &&
--------------------V73.01-V73.02 END------------------