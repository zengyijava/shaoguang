-----------V73.07-V73.08 START------------------------
DECLARE COUNTNUM INT;
        DBVERSIONSTR VARCHAR2(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
  DBVERSIONSTR:='73.10';
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

DECLARE COUNTNUM INT;
BEGIN
-- 删除是否检查运营商余额的配置项
SELECT COUNT(*) INTO COUNTNUM FROM LF_GLOBAL_VARIABLE WHERE GLOBALID=601 AND GLOBALKEY='GWFEE_CHECK';
IF COUNTNUM>0 THEN
DELETE FROM  LF_GLOBAL_VARIABLE WHERE GLOBALID=601 AND GLOBALKEY='GWFEE_CHECK';
END IF;
COMMIT;


-- 删除登录页面是否需要验证码的配置项
SELECT COUNT(*) INTO COUNTNUM FROM LF_SYS_PARAM WHERE PARAM_ITEM = 'loginYanZhengMa';
IF COUNTNUM>0 THEN
DELETE FROM  LF_SYS_PARAM  WHERE PARAM_ITEM = 'loginYanZhengMa';
END IF;
COMMIT;

-- 删除系统下行记录导出的最大记录条数配置项
SELECT COUNT(*) INTO COUNTNUM FROM LF_SYS_PARAM WHERE PARAM_ITEM = 'cxtjMtExportLimit';
IF COUNTNUM>0 THEN
DELETE FROM  LF_SYS_PARAM WHERE PARAM_ITEM = 'cxtjMtExportLimit';
END IF;
COMMIT;

-- 删除web运行参数页面的查看权限
SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=3595;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=3595;
END IF;
COMMIT;

SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=3595;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=3595;
END IF;
COMMIT;

SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=3595;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=3595;
END IF;
COMMIT;

-- 删除WEB运行参数配置页面菜单
SELECT COUNT(*) INTO COUNTNUM FROM LF_PRIVILEGE WHERE PRIVILEGE_ID = 3595;
IF COUNTNUM>0 THEN
DELETE FROM  LF_PRIVILEGE WHERE PRIVILEGE_ID = 3595;
END IF;
COMMIT;

-- 删除群发历史查询汇总权限
SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=7009;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=7009;
END IF;
COMMIT;

SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=7009;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=7009;
END IF;
COMMIT;

SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=7009;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=7009;
END IF;
COMMIT;

-- 删除群发历史查询汇总菜单
SELECT COUNT(*) INTO COUNTNUM FROM LF_PRIVILEGE WHERE PRIVILEGE_ID = 7009;
IF COUNTNUM>0 THEN
DELETE FROM  LF_PRIVILEGE WHERE PRIVILEGE_ID = 7009;
END IF;
COMMIT;

-- 删除汇总转移日志权限
SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=3594;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=1 AND PRIVILEGE_ID=3594;
END IF;
COMMIT;

SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=3594;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=2 AND PRIVILEGE_ID=3594;
END IF;
COMMIT;

SELECT COUNT(*) INTO COUNTNUM FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=3594;
IF COUNTNUM>0 THEN
DELETE FROM LF_IMPOWER WHERE ROLE_ID=4 AND PRIVILEGE_ID=3594;
END IF;
COMMIT;

-- 删除汇总转移日志菜单
SELECT COUNT(*) INTO COUNTNUM FROM LF_PRIVILEGE WHERE PRIVILEGE_ID = 3594;
IF COUNTNUM>0 THEN
DELETE FROM  LF_PRIVILEGE WHERE PRIVILEGE_ID = 3594;
END IF;
COMMIT;

-- HTTP请求超时时间(毫秒)  发送HTTP请求超时时间，由150秒改成120秒 ------------
UPDATE LF_GLOBAL_VARIABLE SET GLOBALVALUE=120000 WHERE GLOBALID=44 AND GLOBALKEY='HTTP_REQUEST_TIMEOUT';
COMMIT;

-- HTTP响应超时时间(毫秒)  发送HTTP请求超时时间，由150秒改成120秒 ------------
UPDATE LF_GLOBAL_VARIABLE SET GLOBALVALUE=120000 WHERE GLOBALID=45 AND GLOBALKEY='HTTP_RESPONSE_TIMEOUT';
COMMIT;

END;
/

/*WEB升级脚本 END*/

--增加版本信息--
DECLARE COUNTNUM INT;
        VERSIONSTR VARCHAR2(32);
        DBVERSIONSTR VARCHAR2(32);
        WBSVERSIONSTR VARCHAR2(32);
        SPGATEVERSIONSTR VARCHAR2(32);
        VERSIONSTR_OLD VARCHAR2(32);
		WBSVERSION_OLD VARCHAR(32);
	    SPGATEVERSION_OLD VARCHAR(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
    VERSIONSTR:='7.3.9.645.SP9';
    DBVERSIONSTR:='73.09';
    WBSVERSIONSTR:='8.6.5.220';
    SPGATEVERSIONSTR:='6.1.53.345';
    VERSIONSTR_OLD:='7.3.10.667.SP10';
	WBSVERSION_OLD:='8.6.5.220';
	SPGATEVERSION_OLD:='6.1.53.345';
	
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
    VALUES(1000,1000,VERSIONSTR,SYSDATE,SYSDATE,'EMP-WEB',1,'1.修复由于百分号转义产生的内容错误问题;<br>2.修复 客服短信-》客户群组群发-》：发送内容输入半角单引号，提交发送之后在群发任务查看内容没有单引号 的问题;<br>3.修复 移动客服-》客户群组群发-》：发送特殊字符，会缺失或替换 的问题；<br>4.修复 参数内容包含中文，在群发任务查看发出的内容是乱码 的问题；<br>5.修复fastjson漏洞；<br>6.修复 操作员在绑定接入账号后，在SP账号统计报表查询不到接入账号数据 的问题；<br>7.解决客户群组群发''''\''字符串转成\的问题；<br>8.解决发送人民币符号变问号的问题。');
  ELSE
    UPDATE LF_VERSION_HIS SET UPDATETIME=SYSDATE,ISRELEASE=1,VERSIONINFO='1.修复由于百分号转义产生的内容错误问题;<br>2.修复 客服短信-》客户群组群发-》：发送内容输入半角单引号，提交发送之后在群发任务查看内容没有单引号 的问题;<br>3.修复 移动客服-》客户群组群发-》：发送特殊字符，会缺失或替换 的问题；<br>4.修复 参数内容包含中文，在群发任务查看发出的内容是乱码 的问题；<br>5.修复fastjson漏洞；<br>6.修复 操作员在绑定接入账号后，在SP账号统计报表查询不到接入账号数据 的问题；<br>7.解决客户群组群发''''\''字符串转成\的问题；<br>8.解决发送人民币符号变问号的问题。' WHERE VERSION=VERSIONSTR AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
  END IF;
  COMMIT;
    --删除EMP旧版本信息
    SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    IF COUNTNUM>0 THEN
        --EMP-WEB
    DELETE FROM LF_VERSION_HIS WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    END IF;
    COMMIT;
	SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    IF COUNTNUM>0 THEN
        --EMP-WEB
    DELETE FROM LF_VERSION WHERE VERSION=VERSIONSTR_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=1000;
    END IF;
    COMMIT;
	
	
	-- 删除旧版本EMP_GATEWAY历史记录
	SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=WBSVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
    IF COUNTNUM>0 THEN
        --EMP_GATEWAY
    DELETE FROM LF_VERSION_HIS WHERE VERSION=WBSVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
    END IF;
    COMMIT;
	SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION WHERE VERSION=WBSVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
    IF COUNTNUM>0 THEN
        --EMP_GATEWAY
    DELETE FROM LF_VERSION WHERE VERSION=WBSVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=2000;
    END IF;
    COMMIT;
	
	
	-- 删除旧版本SMT_SPGATE历史记录
	SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION_HIS WHERE VERSION=SPGATEVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=3000;
    IF COUNTNUM>0 THEN
        --SMT_SPGATE
    DELETE FROM LF_VERSION_HIS WHERE VERSION=SPGATEVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=3000;
    END IF;
    COMMIT;
	SELECT COUNT(*) INTO COUNTNUM FROM LF_VERSION WHERE VERSION=SPGATEVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=3000;
    IF COUNTNUM>0 THEN
        --SMT_SPGATE
    DELETE FROM LF_VERSION WHERE VERSION=SPGATEVERSION_OLD AND PRODUCT_ID=1000 AND PROCESS_ID=3000;
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
    VALUES(DBVERSIONSTR,SYSDATE,SYSDATE,NUMNO,TOTALINT,2,'WEB更改');
  ELSE
    UPDATE LF_DB_SCRIPT SET STATE=2,UPDATETIME=SYSDATE WHERE  VERSION=DBVERSIONSTR AND CURRENT_NO=NUMNO AND TOTAL=TOTALINT;
  END IF;
  COMMIT;
END;
/
--重新编译所有存储过程和触发器
CALL COMPILE_PROCEDURE();
/
