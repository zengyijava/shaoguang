-----------V73.01-V73.02 START------------------------
DECLARE COUNTNUM INT;
        DBVERSIONSTR VARCHAR2(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
  DBVERSIONSTR:='73.02';
  NUMNO:=1;
  TOTALINT:=1;
  SELECT COUNT(*) INTO COUNTNUM FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR;
  IF COUNTNUM>0 THEN
    DELETE FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR;
    COMMIT;
  END IF;
END;
/

/*网关升级脚本 START*/
/*网关升级脚本 END*/

/*WEB升级脚本 START*/
/*WEB升级脚本 END*/

--增加版本信息--
DECLARE COUNTNUM INT;
        VERSIONSTR VARCHAR2(32);
        DBVERSIONSTR VARCHAR2(32);
        WBSVERSIONSTR VARCHAR2(32);
        SPGATEVERSIONSTR VARCHAR2(32);
        VERSIONSTR_OLD VARCHAR2(32);
        WBSVERSIONSTR_OLD VARCHAR2(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
    VERSIONSTR:='7.3.1.521.SP1';
    DBVERSIONSTR:='73.01';
    WBSVERSIONSTR:='8.6.1.216';
    SPGATEVERSIONSTR:='6.1.50.342';
    VERSIONSTR_OLD:='7.3.2.559.SP2';
    WBSVERSIONSTR_OLD:='8.6.2.217';
  NUMNO:=1;
  TOTALINT:=1;
  --EMP产品版本记录
  SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
  IF COUNTNUM=0 THEN
   --EMP-WEB
   INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
   VALUES(1000,1000,VERSIONSTR,SYSDATE,SYSDATE,'EMP-WEB');
  ELSE
    UPDATE LF_VERSION SET UPDATETIME=SYSDATE,VERSION=VERSIONSTR WHERE PRODUCT_ID=1000 AND PROCESS_ID=1000;
  END IF;
  COMMIT;
  SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=2000;
  IF COUNTNUM=0 THEN
   --EMP_GATEWARY
   INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
   VALUES(1000,2000,WBSVERSIONSTR,SYSDATE,SYSDATE,'EMP_GATEWARY');
  ELSE
    UPDATE LF_VERSION SET UPDATETIME=SYSDATE,VERSION=WBSVERSIONSTR WHERE PRODUCT_ID=1000 AND PROCESS_ID=2000;
  END IF;
  COMMIT;
  SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION WHERE PRODUCT_ID=1000 AND PROCESS_ID=3000;
  IF COUNTNUM=0 THEN
   --SMT_SPGATE
   INSERT INTO LF_VERSION(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
   VALUES(1000,3000,SPGATEVERSIONSTR,SYSDATE,SYSDATE,'SMT_SPGATE');
  ELSE
    UPDATE LF_VERSION SET UPDATETIME=SYSDATE,VERSION=SPGATEVERSIONSTR WHERE PRODUCT_ID=1000 AND PROCESS_ID=3000;
  END IF;
  COMMIT;

  --EMP产品版本历史记录
  SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
  IF COUNTNUM=0 THEN
    --EMP-WEB
    INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO,ISRELEASE,VERSIONINFO)
    VALUES(1000,1000,VERSIONSTR,SYSDATE,SYSDATE,'EMP-WEB',1,'1、后端账号和SP账号短富信区分。');
  ELSE
    UPDATE LF_VERSION_HIS SET UPDATETIME=SYSDATE,ISRELEASE=1,VERSIONINFO='1、后端账号和SP账号短富信区分。' WHERE VERSION=VERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
  END IF;
  COMMIT;
    --删除EMP旧版本信息
    SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    IF COUNTNUM>0 THEN
        --EMP-WEB
    DELETE FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    END IF;
    COMMIT;
    --删除新增EMP_GATEWAY
    SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=WBSVERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
    IF COUNTNUM>0 THEN
            --EMP-WEB
    DELETE FROM LF_VERSION_HIS WHERE VERSION=WBSVERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
    END IF;
    COMMIT;
  SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=WBSVERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
  IF COUNTNUM=0 THEN
    --EMP_GATEWARY
    INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
    VALUES(1000,2000,WBSVERSIONSTR,SYSDATE,SYSDATE,'EMP_GATEWARY');
  ELSE
    UPDATE LF_VERSION_HIS SET UPDATETIME=SYSDATE WHERE VERSION=WBSVERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
  END IF;
  COMMIT;

  SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=SPGATEVERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=3000;
  IF COUNTNUM=0 THEN
    --SMT_SPGATE
    INSERT INTO LF_VERSION_HIS(PRODUCT_ID,PROCESS_ID,VERSION,UPDATETIME,CREATETIME,MEMO)
    VALUES(1000,3000,SPGATEVERSIONSTR,SYSDATE,SYSDATE,'SMT_SPGATE');
  ELSE
    UPDATE LF_VERSION_HIS SET UPDATETIME=SYSDATE WHERE VERSION=SPGATEVERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=3000;
  END IF;
  COMMIT;

  SELECT COUNT(*) INTO COUNTNUM FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR;
  IF COUNTNUM=0 THEN
    --EMP产品数据库版本信息表
    INSERT INTO LF_DB_SCRIPT(VERSION,UPDATETIME,CREATETIME,CURRENT_NO,TOTAL,STATE,MEMO)
    VALUES(DBVERSIONSTR,SYSDATE,SYSDATE,NUMNO,TOTALINT,2,'网关包更改');
  ELSE
    UPDATE LF_DB_SCRIPT SET STATE=2,UPDATETIME=SYSDATE WHERE  VERSION=DBVERSIONSTR AND CURRENT_NO=NUMNO AND TOTAL=TOTALINT;
  END IF;
  COMMIT;
END;
/
--重新编译所有存储过程和触发器
CALL COMPILE_PROCEDURE();
/
-----------V73.01-V73.02 END------------------------