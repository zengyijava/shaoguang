DECLARE COUNTNUM INT;
        DBVERSIONSTR VARCHAR2(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
    DBVERSIONSTR:='73.09';
    NUMNO:=3;
    TOTALINT:=5;
    SELECT COUNT(*) INTO COUNTNUM FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR AND CURRENT_NO=NUMNO AND TOTAL=TOTALINT;
    IF COUNTNUM=0 THEN
      --EMP产品数据库版本信息表
      INSERT INTO LF_DB_SCRIPT(VERSION,UPDATETIME,CREATETIME,CURRENT_NO,TOTAL,STATE,MEMO)
      VALUES(DBVERSIONSTR,SYSDATE,SYSDATE,NUMNO,TOTALINT,1,'3号脚本');
      COMMIT;
    END IF;
END;
/


--------IPCOMM脚本START----------------------------
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='SENDQUEUE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE SENDQUEUE(
	"ID" NUMBER(11) NOT NULL,
	"PORT_NO" NUMBER(11) DEFAULT -1 NOT NULL,
	"MOBILE" VARCHAR2(20) NOT NULL,
	"MSG_TYPE" NUMBER(11) DEFAULT 0 NOT NULL,
	"SHORT_MESSAGE" VARCHAR2(300) NOT NULL,
	"URL" VARCHAR2(200),
	"SEND_DATE" TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
	"CLIENTMSGID" VARCHAR2(32) DEFAULT '''' NOT NULL
)';
  END IF;
END;
/

DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='RECEIPT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE RECEIPT(
	"ID" NUMBER(11) NOT NULL,
	"SRCADDR" VARCHAR2(20),
	"DESADDR" VARCHAR2(20),
	"MOTIME" TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
	"CLIENTMSGID" VARCHAR2(32),
	"SPMSGID" VARCHAR2(32),
	"STATUS" VARCHAR2(7) 
)';
  END IF;
END;
/

DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='HISTQUEUE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE HISTQUEUE(
	"ID" NUMBER(11) NOT NULL,
	"PORT_NO" VARCHAR2(20),
	"MOBILE" VARCHAR2(20),
	"SHORT_MESSAGE" VARCHAR2(200),
	"SEND_TIME" TIMESTAMP(6),
	"REPEAT_SEND" NUMBER(11),
	"STATUS" VARCHAR2(10),
	"CLIENTMSGID" VARCHAR2(32) DEFAULT '''' NOT NULL,
	"SPMSGID" VARCHAR2(32) DEFAULT '''' NOT NULL
)';
  END IF;
END;
/

DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='SEQ_SENDQUEUE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_SENDQUEUE
	MINVALUE 1
	MAXVALUE 99999999999
	START WITH 1
	INCREMENT BY 1
	CACHE 200
	ORDER';
  END IF;
END;
/

CREATE OR REPLACE TRIGGER "TIG_SENDQUEUE" BEFORE
INSERT ON  "SENDQUEUE"
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT SEQ_SENDQUEUE.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='SEQ_RECEIPT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_RECEIPT
	MINVALUE 1
	MAXVALUE 99999999999
	START WITH 1
	INCREMENT BY 1
	CACHE 200
	ORDER';
  END IF;
END;
/

CREATE OR REPLACE TRIGGER "TIG_RECEIPT" BEFORE
INSERT ON  "RECEIPT"
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT SEQ_RECEIPT.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='SEQ_HISTQUEUE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_HISTQUEUE
	MINVALUE 1
	MAXVALUE 99999999999
	START WITH 1
	INCREMENT BY 1
	CACHE 200
	ORDER';
  END IF;
END;
/

CREATE OR REPLACE TRIGGER "TIG_HISTQUEUE" BEFORE
INSERT ON  "HISTQUEUE"
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT SEQ_HISTQUEUE.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

--------IPCOMM脚本END----------------------------



--
-- Creating table LF_ACCOUNT_BIND
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ACCOUNT_BIND';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ACCOUNT_BIND
(
  ID           NUMBER(38) not null,
  SPUSERID     VARCHAR2(32),
  BUS_CODE     VARCHAR2(32),
  SYS_GUID     NUMBER(32),
  DEP_CODE     VARCHAR2(32),
  MENU_CODE    VARCHAR2(32),
  BIND_TYPE    NUMBER(1),
  CREATETIME   DATE,
  CREATER_GUID NUMBER(32),
  DESCRIPTION  VARCHAR2(512),
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_ADDRBOOKS
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ADDRBOOKS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ADDRBOOKS
(
  ADDR_ID   NUMBER(38) not null,
  GUID      NUMBER(38),
  ADDR_TYPE NUMBER(2),
  CORP_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ADDRBOOKS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ADDRBOOKS
	  add constraint PK_LF_ADDRBOOKS primary key (ADDR_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ALSMSRECORD
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ALSMSRECORD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ALSMSRECORD
(
  ID         NUMBER(38) not null,
  MSGID      NUMBER(22) DEFAULT 0,
  USERID     VARCHAR2(11) DEFAULT '''' not null,
  SPGATE     VARCHAR2(21) DEFAULT ''0'' not null,
  PHONE      VARCHAR2(21) DEFAULT ''0'' not null,
  SENDSTATUS NUMBER(11) DEFAULT 0 not null,
  SENDFLAG   NUMBER(11) DEFAULT 0 not null,
  ERRORCODE  CHAR(7) DEFAULT '''' not null,
  UNICOM     NUMBER(11) DEFAULT 0 not null,
  SENDTIME   TIMESTAMP(6) DEFAULT sysdate not null,
  RECVTIME   TIMESTAMP(6),
  MESSAGE    VARCHAR2(3000) DEFAULT '''' not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ALSMSRECORD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ALSMSRECORD
	  add constraint PK_LF_ALSMSRECORD primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_ALSMSRECORD') AND T.INDEX_NAME = UPPER('IX_ALSMS_MSGID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index IX_ALSMS_MSGID on LF_ALSMSRECORD (MSGID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APPMAINPAGEHIS
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APPMAINPAGEHIS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APPMAINPAGEHIS
(
  ID        NUMBER(38) not null,
  TASKID    NUMBER(38),
  FROMUSER  VARCHAR2(25) not null,
  TOUSER    VARCHAR2(25) not null,
  TOTYPE    NUMBER(20) not null,
  SENDTIME  TIMESTAMP(8) DEFAULT sysdate,
  SENDSTATE NUMBER(3),
  USER_ID   NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APPMAINPAGEHIS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APPMAINPAGEHIS
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_ACCOUNT
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_ACCOUNT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_ACCOUNT
(
  A_ID         NUMBER(38) not null,
  FAKE_ID      VARCHAR2(32),
  NAME         VARCHAR2(64),
  CODE         VARCHAR2(256) not null,
  OPEN_ID      VARCHAR2(64),
  IMG          VARCHAR2(128),
  TYPE         NUMBER(3),
  IS_APPROVE   NUMBER(3) DEFAULT 0,
  QRCODE       VARCHAR2(128),
  INFO         VARCHAR2(2048),
  URL          VARCHAR2(64),
  TOKEN        VARCHAR2(32),
  BINDTIME     TIMESTAMP(8),
  CORP_CODE    VARCHAR2(64) not null,
  CREATETIME   TIMESTAMP(8) not null,
  MODIFYTIME   TIMESTAMP(8),
  GRANT_TYPE   VARCHAR2(64),
  APPID        VARCHAR2(64),
  SECRET       VARCHAR2(64),
  ACCESS_TOKEN VARCHAR2(256),
  ACCESS_TIME  TIMESTAMP(8),
  RELEASE_TIME TIMESTAMP(8),
  ACCESS_COUNT VARCHAR2(64),
  SYNC_STATE   NUMBER(3) DEFAULT 0,
  SYNC_TIME    TIMESTAMP(8),
  PORT         NUMBER(20),
  CREATER      NUMBER(38),
  PWD          VARCHAR2(128),
  FILESVR_URL  VARCHAR2(512),
  APP_TYPE     NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_ACCOUNT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_ACCOUNT
	  add primary key (A_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_CLIDOWLOAD
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_CLIDOWLOAD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_CLIDOWLOAD
(
  CLID           NUMBER(38) not null,
  A_ID           NUMBER(38),
  IOSPUSHDATE    TIMESTAMP(8),
  ADPUSHDATE     TIMESTAMP(8),
  IOSFILESIZE    VARCHAR2(30),
  AZFILESIZE     VARCHAR2(30),
  IOSFILEVERSION VARCHAR2(60),
  ADFILEVERSION  VARCHAR2(60),
  IOSFILEURL     VARCHAR2(600),
  ADFILEURL      VARCHAR2(600),
  IOSFILEDESCR   VARCHAR2(90),
  ADFILEDESCR    VARCHAR2(90),
  CORP_CODE      VARCHAR2(64),
  IOSUPDATEUSER  VARCHAR2(64),
  IOSUPDATEDATE  TIMESTAMP(8),
  ADUPDATEUSER   VARCHAR2(64),
  ADUPDATEDATE   TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_CLIDOWLOAD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_CLIDOWLOAD
	  add primary key (CLID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_FOMEITEM
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_FOMEITEM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_FOMEITEM
(
  FIELD_ID    NUMBER(38) not null,
  FILED_TYPE  VARCHAR2(8),
  FIELD_VALUE VARCHAR2(512),
  Q_ID        NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_FOMEITEM') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_FOMEITEM
	  add primary key (FIELD_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_FOMEVALVE
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_FOMEVALVE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_FOMEVALVE
(
  V_ID        NUMBER(38) not null,
  F_ID        NUMBER(38),
  Q_ID        NUMBER(38),
  FIELD_ID    NUMBER(38),
  FIELD_VALUE NUMBER(38),
  FIELD_TYPE  VARCHAR2(8),
  WC_ID       VARCHAR2(64),
  CORP_CODE   VARCHAR2(64),
  CREATETIME  TIMESTAMP(8),
  AID         NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_FOMEVALVE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_FOMEVALVE
	  add primary key (V_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_FOM_INFO
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_FOM_INFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_FOM_INFO
(
  F_ID          NUMBER(38) not null,
  TYPE_ID       NUMBER(38),
  TITLE         VARCHAR2(512),
  NOTE          VARCHAR2(512),
  PUBLISH_STATE NUMBER(3),
  URL           VARCHAR2(512),
  SUBMIT_COUNT  NUMBER(20) DEFAULT 0,
  SEQ_NUM       NUMBER(20),
  IS_SYSTEM     NUMBER(3),
  CORP_CODE     VARCHAR2(64) not null,
  CREATETIME    TIMESTAMP(8),
  MODITYTIME    TIMESTAMP(8),
  PARENT_ID     NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_FOM_INFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_FOM_INFO
	  add primary key (F_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_FOM_QUESN
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_FOM_QUESN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_FOM_QUESN
(
  Q_ID       NUMBER(38) not null,
  F_ID       NUMBER(38),
  FILED_TYPE VARCHAR2(8),
  TITLE      VARCHAR2(512),
  FORM_DATA  VARCHAR2(2048),
  SEQ_NUM    NUMBER(20),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_FOM_QUESN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_FOM_QUESN
	  add primary key (Q_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_FOM_TYPE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_FOM_TYPE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_FOM_TYPE
(
  TYPE_ID    NUMBER(38) not null,
  NAME       VARCHAR2(512),
  PARENT_ID  NUMBER(38),
  SEQ_NUM    NUMBER(20),
  IS_SYSTEM  NUMBER(3),
  CORP_CODE  VARCHAR2(64),
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_FOM_TYPE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_FOM_TYPE
	  add primary key (TYPE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_KEYWORD
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_KEYWORD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_KEYWORD
(
  K_ID       NUMBER(38) not null,
  NAME       VARCHAR2(32) not null,
  TYPE       NUMBER(3) DEFAULT 0,
  A_ID       NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  MODIFYTIME TIMESTAMP(8),
  APP_TYPE   NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_KEYWORD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_KEYWORD
	  add primary key (K_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_LBS_PIOS
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_LBS_PIOS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_LBS_PIOS
(
  P_ID       NUMBER(38) not null,
  CITY_ID    NUMBER(3),
  LAT        VARCHAR2(64) not null,
  LNG        VARCHAR2(64) not null,
  TITLE      VARCHAR2(64),
  KEYWORD    VARCHAR2(64),
  TELEPHONE  VARCHAR2(32),
  ADDRESS    VARCHAR2(128),
  NOTE       VARCHAR2(512),
  A_ID       VARCHAR2(128) not null,
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8),
  DISTANCE   VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_LBS_PIOS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_LBS_PIOS
	  add primary key (P_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_LBS_PUSH
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_LBS_PUSH';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_LBS_PUSH
(
  PUSH_ID    NUMBER(38) not null,
  PUSHTYPE   NUMBER(3),
  IMGURL     VARCHAR2(256),
  NOTE       VARCHAR2(512),
  RADIUS     VARCHAR2(16),
  AUTORADIUS NUMBER(3),
  PUSHCOUNT  NUMBER(3),
  AUTOMORE   NUMBER(3),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_LBS_PUSH') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_LBS_PUSH
	  add primary key (PUSH_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_LBS_USER
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_LBS_USER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_LBS_USER
(
  UP_ID      NUMBER(38) not null,
  LAT        VARCHAR2(64) not null,
  LNG        VARCHAR2(64) not null,
  A_ID       VARCHAR2(128) not null,
  OPENID     VARCHAR2(128) not null,
  CORP_CODE  VARCHAR2(64) not null,
  MODITYTIME TIMESTAMP(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_LBS_USER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_LBS_USER
	  add primary key (UP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MENU
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MENU';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MENU
(
  M_ID       NUMBER(38) not null,
  M_NAME     VARCHAR2(32) not null,
  M_TYPE     NUMBER(10) not null,
  M_KEY      VARCHAR2(64),
  M_URL      VARCHAR2(256),
  M_HIDDEN   NUMBER(10),
  M_ORDER    NUMBER(10) not null,
  P_ID       NUMBER(38),
  T_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000),
  A_ID       NUMBER(38) not null,
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  APP_TYPE   NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_SUB_TEMPLATE
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SUB_TEMPLATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SUB_TEMPLATE(
    ID NUMBER(18) NOT NULL,
    TMP_TYPE NUMBER(11) DEFAULT 1 NOT NULL,
    FRONTJSON VARCHAR2(4000) DEFAULT ''{}'' NOT NULL,
    INDUSTRY_ID NUMBER(11) DEFAULT 0 NOT NULL,
    USE_ID NUMBER(11) DEFAULT 0 NOT NULL,
    STATUS NUMBER(11) DEFAULT 0 NOT NULL,
    ADD_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
    ENDJSON VARCHAR2(4000) DEFAULT ''{}'' NOT NULL,
    PRIORITY NUMBER(15) DEFAULT 0 NOT NULL,
    FILEURL VARCHAR2(255) DEFAULT '' '' NOT NULL,
    CONTENT VARCHAR2(4000) DEFAULT '' '' NOT NULL,
    CARD_HTML NVARCHAR2(2000) DEFAULT '' '' NOT NULL,
    TM_ID NUMBER(11) DEFAULT 0 NOT NULL,
    DEGREE NUMBER(11) DEFAULT 0 NOT NULL,
    DEGREE_SIZE NUMBER(11) DEFAULT 0 NOT NULL,
    H5TYPE NUMBER(11) DEFAULT 0 NOT NULL,
    APP VARCHAR2(3000) DEFAULT '' '' NOT NULL,
    H5URL VARCHAR2(255) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SUB_TEMPLATE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_SUB_TEMPLATE
	  ADD PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_FODDER(素材表)
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_FODDER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_FODDER(
  ID         NUMBER(38) NOT NULL,
  USER_ID   NUMBER(18) DEFAULT 0 NOT NULL,
  URL  VARCHAR2(200) DEFAULT '' '' NOT NULL,
  FO_TYPE NUMBER(11) DEFAULT 0 NOT NULL,
  FO_SIZE NUMBER(11) DEFAULT 0 NOT NULL,
  WIDTH NUMBER(11) DEFAULT 0 NOT NULL,
  HEIGHT NUMBER(11) DEFAULT 0 NOT NULL,
  FO_STATUS NUMBER(11) DEFAULT 0 NOT NULL,
  DURATION NUMBER(11) DEFAULT 0 NOT NULL,
  RADIO  VARCHAR2(50) DEFAULT '' '' NOT NULL,
  ORIGINAL  VARCHAR2(200) DEFAULT '' '' NOT NULL,
  FIRSTFRAMEPATH  VARCHAR2(64) DEFAULT '' '' NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_FODDER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_FODDER
	  ADD CONSTRAINT PK_LF_FODDER PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 2
	  MAXTRANS 255
	  STORAGE
	  (
		INITIAL 64K
		MINEXTENTS 1
		MAXEXTENTS UNLIMITED
	  )';
	END IF;
END;
/

--
-- Creating table LF_HFIVE
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_HFIVE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_HFIVE(
    HID NUMBER(11) NOT NULL,
    TITLE VARCHAR2(50) DEFAULT '' '' NOT NULL,
    AUTHOR VARCHAR2(50) DEFAULT '' '' NOT NULL,
    CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
    COMMITTIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
    URL VARCHAR2(256) DEFAULT '' '' NOT NULL,
    UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
    USETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
    STAUS NUMBER(3) DEFAULT 0 NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_HFIVE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_HFIVE
	  ADD PRIMARY KEY (HID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TEMP_PARAM
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TEMP_PARAM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_TEMP_PARAM(
    ID NUMBER(18) NOT NULL,
    TM_ID NUMBER(18) DEFAULT 0 NOT NULL,
    NAME VARCHAR2(255) DEFAULT '' '' NOT NULL,
    MAX_LENGTH NUMBER(11) DEFAULT 0 NOT NULL,
    MIN_LENGTH NUMBER(11) DEFAULT 0 NOT NULL,
    TYPE NUMBER(3) DEFAULT 1 NOT NULL,
    LENGTH_RESTRICT NUMBER(3) DEFAULT 0 NOT NULL,
    FIX_LENGTH NUMBER(11) DEFAULT 0 NOT NULL,
    HASLENGTH NUMBER(11) DEFAULT 0 NOT NULL,
    REGCONTENT VARCHAR2(50) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TEMP_PARAM') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_TEMP_PARAM
	  ADD PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MOCACHE
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MOCACHE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MOCACHE
(
  ID           NUMBER(38) not null,
  MSGID        NUMBER(38),
  READ_STATE   NUMBER(20) not null,
  SEND_STATE   NUMBER(20) DEFAULT 1 not null,
  SENDED_COUNT NUMBER(20) DEFAULT 0 not null,
  SERIAL       VARCHAR2(100) not null,
  ECODE        VARCHAR2(64) not null,
  APPID        NUMBER(38),
  FROMJID      VARCHAR2(64) not null,
  FNAME        VARCHAR2(64),
  TOJID        VARCHAR2(2048),
  TNAME        VARCHAR2(64),
  VALIDITY     NUMBER(38),
  MSG_SRC      NUMBER(20),
  BODY         VARCHAR2(4000),
  MSG_TYPE     NUMBER(20) not null,
  TOTYPE       NUMBER(20),
  CREATETIME   TIMESTAMP(8) DEFAULT sysdate not null,
  EMPTYPE      NUMBER(20),
  PACKETID     VARCHAR2(32),
  SEND_RESULT  NUMBER(20),
  OUTLINEMSG   NUMBER(20)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_MOCACHE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MOCACHE
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MSGCACHE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MSGCACHE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MSGCACHE
(
  ID           NUMBER(38) not null,
  MSGID        NUMBER(38),
  READ_STATE   NUMBER(20) not null,
  SEND_STATE   NUMBER(20) DEFAULT 1 not null,
  SENDED_COUNT NUMBER(20) DEFAULT 0 not null,
  SERIAL       VARCHAR2(100) not null,
  ECODE        VARCHAR2(64) not null,
  APPID        NUMBER(38),
  FROMJID      VARCHAR2(64) not null,
  FNAME        VARCHAR2(64),
  TOJID        VARCHAR2(2048) not null,
  TNAME        VARCHAR2(64),
  VALIDITY     NUMBER(38),
  MSG_SRC      NUMBER(20),
  BODY         VARCHAR2(4000),
  MSG_TYPE     NUMBER(20) not null,
  TOTYPE       NUMBER(20),
  CREATETIME   TIMESTAMP(8) DEFAULT sysdate not null,
  EMPTYPE      NUMBER(20),
  PACKETID     VARCHAR2(32),
  SEND_RESULT  NUMBER(20),
  HAS_CONTENT  NUMBER(20)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_MSGCACHE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MSGCACHE
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MSGCONTENT
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MSGCONTENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MSGCONTENT
(
  ID       NUMBER(38) not null,
  CACHE_ID NUMBER(38),
  MSG_TYPE NUMBER(20),
  CONTENT  VARCHAR2(4000) DEFAULT '''',
  SORT_NUM NUMBER(20)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_MSGCONTENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MSGCONTENT
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MTMSG
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MTMSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MTMSG
(
  ID             NUMBER(38) not null,
  MSG_ID         VARCHAR2(64) not null,
  APP_MSG_ID     VARCHAR2(64) not null,
  TASKID         NUMBER(38),
  MSG_TYPE       NUMBER(3) not null,
  APPACOUNT      VARCHAR2(25),
  TOUSERNAME     VARCHAR2(1024) not null,
  USERID         NUMBER(38),
  SENDSTATE      NUMBER(3),
  TITLE          VARCHAR2(64),
  CONTENT        VARCHAR2(2048),
  MSG_XML        VARCHAR2(4000),
  MSG_TEXT       VARCHAR2(4000),
  PARENT_ID      NUMBER(38),
  CORP_CODE      VARCHAR2(64),
  CREATETIME     TIMESTAMP(8) not null,
  APPUSERACCOUNT VARCHAR2(256),
  RPT_STATE      VARCHAR2(25),
  SENDMSGTYPE    NUMBER(20) not null,
  RECRPTTIME     TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_MTMSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MTMSG
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MTTASK
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MTTASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MTTASK
(
  ID           NUMBER(38) not null,
  TASKID       NUMBER(38) not null,
  TITLE        VARCHAR2(64),
  MSG          VARCHAR2(2048),
  MSG_URL      VARCHAR2(512),
  MSG_LOCALURL VARCHAR2(512),
  MSG_TYPE     NUMBER(3),
  APPACOUNT    VARCHAR2(25),
  BIGINTIME    TIMESTAMP(8) DEFAULT sysdate,
  ENDTIME      TIMESTAMP(8) DEFAULT sysdate,
  SENDSTATE    NUMBER(3),
  SUB_COUNT    NUMBER(38),
  SUC_COUNT    NUMBER(38),
  FAI_COUNT    NUMBER(38),
  READ_COUNT   NUMBER(38),
  UNREAD_COUNT NUMBER(38),
  USER_ID      NUMBER(38),
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_MTTASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MTTASK
	  add primary key (TASKID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MW_CLIENT
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MW_CLIENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MW_CLIENT
(
  WC_ID          NUMBER(38) not null,
  FAKE_ID        VARCHAR2(32),
  G_ID           NUMBER(38),
  PHONE          VARCHAR2(30),
  UNAME          VARCHAR2(32),
  DESCR          VARCHAR2(512),
  VERIFYTIME     TIMESTAMP(8),
  U_ID           NUMBER(38),
  GUID           NUMBER(38),
  CORP_CODE      VARCHAR2(64),
  CREATETIME     TIMESTAMP(8),
  MODIFYTIME     TIMESTAMP(8),
  SUBSCRIBE      VARCHAR2(11),
  NICK_NAME      VARCHAR2(32),
  SEX            VARCHAR2(11),
  CITY           VARCHAR2(15),
  PROVINCE       VARCHAR2(15),
  COUNTRY        VARCHAR2(15),
  LANGUAGE       VARCHAR2(15),
  HEAD_IMG_URL   VARCHAR2(512),
  SUBSCRIBE_TIME TIMESTAMP(8),
  OPEN_ID        VARCHAR2(64),
  A_ID           NUMBER(38),
  LOCAL_IMG_URL  VARCHAR2(512),
  APP_CODE       VARCHAR2(256) not null,
  AGE            NUMBER(38),
  SYN_ID         NUMBER(20),
  UTYPE          NUMBER(20),
  QQ             VARCHAR2(32),
  EMAIL          VARCHAR2(64),
  ECODE          VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_MW_CLIENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MW_CLIENT
	  add primary key (WC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_APP_MW_CLIENT') AND T.INDEX_NAME = UPPER('APPCODEUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MW_CLIENT
	  add constraint APPCODEUNIQUE unique (APP_CODE)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_MW_GPMEM
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MW_GPMEM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MW_GPMEM
(
  G_ID     NUMBER(20),
  GM_USER  NUMBER(20),
  GM_STATU NUMBER(20),
  APP_TYPE NUMBER(20)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_APP_MW_GROUP
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_MW_GROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_MW_GROUP
(
  G_ID       NUMBER(38) not null,
  WG_ID      NUMBER(38),
  NAME       VARCHAR2(32),
  COUNT      VARCHAR2(11),
  A_ID       NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_MW_GROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_MW_GROUP
	  add primary key (G_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_OL_GPMEM
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_OL_GPMEM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_OL_GPMEM
(
  G_ID     NUMBER(38) not null,
  GM_USER  NUMBER(38),
  GM_STATE NUMBER(38) DEFAULT (1),
  APP_TYPE NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_APP_OL_GPMSGID
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_OL_GPMSGID';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_OL_GPMSGID
(
  GM_USER     NUMBER(38),
  MSG_ID      NUMBER(38) DEFAULT (0),
  UPDATE_TIME TIMESTAMP(8) DEFAULT (sysdate) -- 更新时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_APP_OL_GROUP
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_OL_GROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_OL_GROUP
(
  GP_ID       NUMBER(38) not null,
  GP_NAME     VARCHAR2(32),
  CREATE_USER NUMBER(38),
  CREATE_TIME TIMESTAMP(8),
  MOUNTCOUNT  NUMBER(20) DEFAULT 0,
  APP_TYPE    NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_OL_GROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_OL_GROUP
	  add primary key (GP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_OL_MSG
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_OL_MSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_OL_MSG
(
  M_ID       NUMBER(38) not null,
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SEND_TIME  TIMESTAMP(8),
  SERVER_NUM VARCHAR2(32),
  MSG_TYPE   VARCHAR2(10),
  MESSAGE    VARCHAR2(512),
  PUSH_TYPE  NUMBER(3) DEFAULT 0,
  A_ID       NUMBER(38),
  APP_TYPE   NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_OL_MSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_OL_MSG
	  add primary key (M_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_OL_MSGHIS
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_OL_MSGHIS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_OL_MSGHIS
(
  M_ID       NUMBER(38) not null,
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SEND_TIME  TIMESTAMP(8),
  SERVER_NUM VARCHAR2(32),
  MSG_TYPE   VARCHAR2(10),
  MESSAGE    VARCHAR2(512),
  PUSH_TYPE  NUMBER(3) DEFAULT 0,
  A_ID       NUMBER(38),
  APP_TYPE   NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_OL_MSGHIS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_OL_MSGHIS
	  add primary key (M_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_RMS_SYN_TEMP
--增加富信同步报表数据中间表
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_RMS_SYN_TEMP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_RMS_SYN_TEMP(
   ID NUMBER(18) NOT NULL,--主键
   SPISUNCM VARCHAR2(4) DEFAULT '' '' NOT NULL,--运营商
   USERID VARCHAR2(32) DEFAULT '' '' NOT NULL,--SP账号
   IYMD NUMBER(11) DEFAULT 0 NOT NULL,--年月日
   ICOUNT NUMBER(18) DEFAULT 0 NOT NULL,--提交总数
   RSUCC NUMBER(18) DEFAULT 0 NOT NULL,--发送成功数
   RFAIL NUMBER(18) DEFAULT 0 NOT NULL,--接收失败数
   REQ_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL,--请求时间（入库时间）
   CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,--企业编码
   DEGREE NUMBER(11) DEFAULT 0 NOT NULL,--档位
   FLAG NUMBER(11) DEFAULT 0 NOT NULL,--0未结束，1结束
   PARAM1 varchar2(64) DEFAULT '' '' NOT NULL,
   PARAM2 varchar2(64) DEFAULT '' '' NOT NULL,
   PARAM3 varchar2(64) DEFAULT '' '' NOT NULL,
   PARAM4 varchar2(64) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_RMS_SYN_TEMP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_RMS_SYN_TEMP
	  ADD CONSTRAINT PK_LF_RMS_SYN_TEMP PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_RMS_REPORT
--增加富信统计表（汇总报表）
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_RMS_REPORT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_RMS_REPORT(
   ID NUMBER(18) NOT NULL,--主键
   SPISUNCM VARCHAR2(4) DEFAULT '' '' NOT NULL,--运营商
   USERID VARCHAR2(32) DEFAULT '' '' NOT NULL,--SP账号
   IYMD NUMBER(11) DEFAULT 0 NOT NULL,--年月日
   ICOUNT NUMBER(18) DEFAULT 0 NOT NULL,--提交总数
   RSUCC NUMBER(18) DEFAULT 0 NOT NULL,--发送成功数
   RFAIL NUMBER(18) DEFAULT 0 NOT NULL,--接收失败数
   CREATE_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL,--创建时间
   CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,--企业编码
   SYN_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL,--同步时间
   DEGREE NUMBER(11) DEFAULT 0 NOT NULL,--档位
   PARAM1 varchar2(64) DEFAULT '' '' NOT NULL,
   PARAM2 varchar2(64) DEFAULT '' '' NOT NULL,
   PARAM3 varchar2(64) DEFAULT '' '' NOT NULL,
   PARAM4 varchar2(64) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_RMS_REPORT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_RMS_REPORT
	  ADD CONSTRAINT PK_LF_RMS_REPORT PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_RMS_REPORT') AND T.INDEX_NAME = UPPER('IND_LF_RMS_REP_IYMD');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX IND_LF_RMS_REP_IYMD ON LF_RMS_REPORT(IYMD)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_RMS_REPORT') AND T.INDEX_NAME = UPPER('IND_LF_RMS_REP_DEG');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX IND_LF_RMS_REP_DEG ON LF_RMS_REPORT(DEGREE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_RMS_REPORT') AND T.INDEX_NAME = UPPER('IND_LF_RMS_REP_SPCM');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX IND_LF_RMS_REP_SPCM ON LF_RMS_REPORT(SPISUNCM)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_RMS_REPORT') AND T.INDEX_NAME = UPPER('IND_LF_RMS_REP_UID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX IND_LF_RMS_REP_UID ON LF_RMS_REPORT(USERID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_RMS_REPORT
---档位表 LF_DEGREE
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEGREE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_DEGREE(
   ID NUMBER(18) NOT NULL,--主键
   DEGREE NUMBER(11) DEFAULT 0 NOT NULL,--档位
   DEGREE_BEGIN VARCHAR2(64) DEFAULT '' '' NOT NULL,--档位容量开始
   DEGREE_END VARCHAR2(64) DEFAULT '' '' NOT NULL,--档位容量结束
   VALID_DATE_BEGIN TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--有效开始时间
   VALID_DATE_END TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--有效截止时间
   STATUS NUMBER(11) DEFAULT 0 NOT NULL,--状态,0-启用,1-禁用
   USER_ID NUMBER(18) DEFAULT 0 NOT NULL,--操作员ID
   CREATE_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL --记录创建时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEGREE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_DEGREE
	  ADD CONSTRAINT PK_LF_DEGREE PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_REVENT
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_REVENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_REVENT
(
  EVT_ID     NUMBER(38) not null,
  EVT_TYPE   NUMBER(3),
  T_ID       NUMBER(38),
  A_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000) not null,
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  TITLE      VARCHAR2(64),
  MODIFYTIME TIMESTAMP(8),
  APP_TYPE   NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_REVENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_REVENT
	  add primary key (EVT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_RIMG
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_RIMG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_RIMG
(
  RIMG_ID     NUMBER(38) not null,
  TITLE       VARCHAR2(64) not null,
  DESCRIPTION VARCHAR2(4000),
  PICURL      VARCHAR2(128),
  LINK        VARCHAR2(128),
  A_ID        NUMBER(38),
  CORP_CODE   VARCHAR2(64) not null,
  CREATETIME  TIMESTAMP(8) not null,
  MODIFYTIME  TIMESTAMP(8),
  SOURCE_URL  VARCHAR2(256),
  SUMMARY     VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_RIMG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_RIMG
	  add primary key (RIMG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_RPTCACHE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_RPTCACHE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_RPTCACHE
(
  ID           NUMBER(38) not null,
  MSGID        NUMBER(38),
  READ_STATE   NUMBER(20),
  SEND_STATE   NUMBER(20) DEFAULT 1 not null,
  SENDED_COUNT NUMBER(20) DEFAULT 0 not null,
  SERIAL       VARCHAR2(100) not null,
  ECODE        VARCHAR2(64),
  APPID        NUMBER(38),
  VALIDITY     NUMBER(38),
  ICODE        NUMBER(20),
  BODY         VARCHAR2(4000),
  MSG_TYPE     NUMBER(20),
  CREATETIME   TIMESTAMP(8) DEFAULT sysdate not null,
  EMTYPE       NUMBER(20),
  PACKETID     VARCHAR2(32),
  SEND_RESULT  NUMBER(20),
  ERRCODE      VARCHAR2(64),
  USERNAME     VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_RPTCACHE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_RPTCACHE
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_RTEXT
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_RTEXT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_RTEXT
(
  TET_ID     NUMBER(38) not null,
  TET_TYPE   NUMBER(3) DEFAULT 0,
  T_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000) not null,
  A_ID       NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  TITLE      VARCHAR2(64),
  MODIFYTIME TIMESTAMP(8),
  APP_TYPE   NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_RTEXT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_RTEXT
	  add primary key (TET_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_SIT_INFO
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_SIT_INFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_SIT_INFO
(
  S_ID        NUMBER(38) not null,
  TYPE_ID     NUMBER(38),
  NAME        VARCHAR2(100),
  URL         VARCHAR2(128),
  IS_SYSTEM   NUMBER(3) DEFAULT 0,
  CORP_CODE   VARCHAR2(64) not null,
  CREATETIME  TIMESTAMP(8),
  MODITYTIME  TIMESTAMP(8),
  USERID      NUMBER(38),
  STATUS      NUMBER(20) not null,
  PUBLISHTIME TIMESTAMP(8),
  VALIDITY    NUMBER(38),
  SERIAL      VARCHAR2(64),
  MSG_ID      VARCHAR2(64),
  FROMUSER    VARCHAR2(25),
  TOUSER      VARCHAR2(25),
  SENDTIME    TIMESTAMP(8) DEFAULT sysdate,
  SENDSTATE   NUMBER(3),
  RECTIME     TIMESTAMP(8),
  CANCELTIME  TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_SIT_INFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_SIT_INFO
	  add primary key (S_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_SIT_PAGE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_SIT_PAGE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_SIT_PAGE
(
  PAGE_ID    NUMBER(38) not null,
  S_ID       NUMBER(38),
  PAGE_TYPE  VARCHAR2(32),
  NAME       VARCHAR2(32),
  URL        VARCHAR2(128),
  SEQ_NUM    NUMBER(20),
  CORP_CODE  VARCHAR2(64),
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_SIT_PAGE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_SIT_PAGE
	  add primary key (PAGE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_SIT_PLANT
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_SIT_PLANT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_SIT_PLANT
(
  PLANT_ID     NUMBER(38) not null,
  PLANT_TYPE   VARCHAR2(32),
  PAGE_ID      NUMBER(38),
  NAME         VARCHAR2(100),
  FEILD_NAMES  VARCHAR2(64),
  FEILD_VALUES VARCHAR2(4000),
  CORP_CODE    VARCHAR2(64),
  CREATETIME   TIMESTAMP(8),
  MODITYTIME   TIMESTAMP(8),
  S_ID         NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_SIT_PLANT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_SIT_PLANT
	  add primary key (PLANT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_SIT_TYPE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_SIT_TYPE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_SIT_TYPE
(
  TYPE_ID    NUMBER(38) not null,
  NAME       VARCHAR2(32),
  IS_DEFAULT NUMBER(3),
  PATTERN    NUMBER(20) DEFAULT 0,
  IMG_URL    VARCHAR2(64),
  IMG_URL0   VARCHAR2(64),
  IMG_URL1   VARCHAR2(64),
  IMG_URL2   VARCHAR2(64),
  IMG_URL3   VARCHAR2(64),
  IS_SYSTEM  NUMBER(3) DEFAULT 0,
  SEQ_NUM    NUMBER(20),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_SIT_TYPE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_SIT_TYPE
	  add primary key (TYPE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_TC_MOMSG
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_TC_MOMSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_TC_MOMSG
(
  MSG_ID     NUMBER(38) not null,
  MSG_TYPE   NUMBER(3),
  WC_ID      NUMBER(38),
  A_ID       NUMBER(38),
  TYPE       NUMBER(3),
  MSG_XML    VARCHAR2(4000),
  MSG_TEXT   VARCHAR2(4000),
  PARENT_ID  NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  MSG_JSON   VARCHAR2(4000),
  FROMUSER   VARCHAR2(256) not null,
  TONAME     VARCHAR2(64),
  TOUSER     VARCHAR2(256),
  MSG_SRC    NUMBER(20),
  STATUS     NUMBER(20),
  SERIAL     VARCHAR2(64) not null,
  FROMNAME   VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_TC_MOMSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_TC_MOMSG
	  add primary key (MSG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_TC_MTMSG
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_TC_MTMSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_TC_MTMSG
(
  MSG_ID     NUMBER(38) not null,
  MSG_TYPE   NUMBER(3) not null,
  WC_ID      NUMBER(38) not null,
  A_ID       NUMBER(38) not null,
  TYPE       NUMBER(3) not null,
  MSG_XML    VARCHAR2(4000) not null,
  MSG_TEXT   VARCHAR2(4000) not null,
  PARENT_ID  NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_TC_MTMSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_TC_MTMSG
	  add primary key (MSG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_TEMPLATE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_TEMPLATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_TEMPLATE
(
  T_ID        NUMBER(38) not null,
  T_NAME      VARCHAR2(64) not null,
  MSG_TYPE    NUMBER(3),
  MSG_XML     VARCHAR2(4000),
  MSG_TEXT    VARCHAR2(4000),
  IS_PUBLIC   NUMBER(3) DEFAULT 0,
  IS_DRAFT    NUMBER(3) DEFAULT 0,
  IS_DYNAMIC  NUMBER(3) DEFAULT 0,
  A_ID        NUMBER(38),
  CORP_CODE   VARCHAR2(64) not null,
  RIMG_IDS    VARCHAR2(255),
  KEY_WORDSVO VARCHAR2(255),
  CREATETIME  TIMESTAMP(8) not null,
  MODIFYTIME  TIMESTAMP(8),
  APP_TYPE    NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_TEMPLATE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_TEMPLATE
	  add primary key (T_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_TLINK
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_TLINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_TLINK
(
  K_ID     NUMBER(38) not null,
  T_ID     NUMBER(38) not null,
  APP_TYPE NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_TLINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_TLINK
	  add primary key (K_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_ULINK
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_ULINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_ULINK
(
  U_ID  NUMBER(38) not null,
  WC_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_ULINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_ULINK
	  add primary key (U_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_VERIFY
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_VERIFY';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_VERIFY
(
  VF_ID        NUMBER(38) not null,
  WC_ID        NUMBER(38),
  VERIFYCODE   VARCHAR2(6),
  CODETIME     TIMESTAMP(8),
  PHONE        VARCHAR2(30),
  LIMTTIME     TIMESTAMP(8),
  VERIFY_COUNT NUMBER(3),
  CORP_CODE    VARCHAR2(64) not null,
  APP_TYPE     NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_VERIFY') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_VERIFY
	  add constraint PK_LF_APP_VERIFY primary key (VF_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_APP_WC_GROUP
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_APP_WC_GROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_APP_WC_GROUP
(
  G_ID       NUMBER(38) not null,
  WG_ID      NUMBER(38),
  NAME       VARCHAR2(32),
  COUNT      VARCHAR2(11),
  A_ID       NUMBER(38) not null,
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_APP_WC_GROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_APP_WC_GROUP
	  add primary key (G_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_AUTOREPLY
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_AUTOREPLY';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_AUTOREPLY
(
  ID            NUMBER(38) not null,
  REPLY_CONTENT VARCHAR2(1024),
  STATE         NUMBER(3),
  TYPE          NUMBER(3),
  CORP_CODE     VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_AUTOREPLY') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_AUTOREPLY
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BILLLOG
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BILLLOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BILLLOG
(
  LOG_ID         NUMBER(38) not null,
  PHONE          VARCHAR2(64),
  PRO_TYPE       VARCHAR2(64),
  BILL_TIME      TIMESTAMP(8),
  COST           NUMBER,
  REAL_COST      NUMBER,
  RE_COST_STATUS NUMBER(1),
  RE_COST        NUMBER,
  COMMENTS       VARCHAR2(128),
  PRO_ID         NUMBER(38),
  BILL_STATUS    NUMBER(38),
  PRO_NAME       VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BILLLOG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BILLLOG
	  add constraint PK_LF_BILLLOG primary key (LOG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BINDFLOW
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BINDFLOW';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BINDFLOW
(
  BFID           NUMBER(38) not null,
  FID            NUMBER(38) not null,
  USER_ID        NUMBER(38),
  IS_FLOW_REMIND NUMBER(38),
  MSGACCOUNT     NUMBER(38),
  CORP_CODE      VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_BIRTHDAYMEMBER
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BIRTHDAYMEMBER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BIRTHDAYMEMBER
(
  ID          NUMBER(38) not null,
  BS_ID       NUMBER(38),
  TYPE        NUMBER(4),
  MEMBER_ID   NUMBER(38),
  USER_ID     NUMBER(38),
  MSG         VARCHAR2(2048),
  ADDNAME     VARCHAR2(64),
  CORPCODE    VARCHAR2(32),
  MEMBER_TYPE NUMBER(4)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_BIRTHDAYSETUP
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BIRTHDAYSETUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BIRTHDAYSETUP
(
  ID         NUMBER(38) not null,
  USER_ID    NUMBER(38),
  MSG        VARCHAR2(2048),
  SEND_TIME  DATE,
  IS_ADDNAME NUMBER(4),
  ADDNAME    VARCHAR2(64),
  SP_USER    VARCHAR2(32),
  IS_USE     NUMBER(4),
  TYPE       NUMBER(4),
  CORPCODE   VARCHAR2(32),
  BUS_CODE   VARCHAR2(64),
  TITLE      VARCHAR2(64),
  ISSIGNNAME NUMBER(2),
  SIGNNAME   VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_BLACKLIST
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BLACKLIST';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BLACKLIST
(
  BL_ID     NUMBER(38) not null,
  PHONE     VARCHAR2(64),
  SPGATE    VARCHAR2(30),
  BL_STATE  NUMBER(1),
  COMMENTS  VARCHAR2(512),
  USERID    VARCHAR2(6),
  SPNUMBER  VARCHAR2(30),
  OPTIME    VARCHAR2(6),
  SPISUNCM  NUMBER(2),
  BUS_CODE  VARCHAR2(64),
  CORP_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_BUSINESS
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUSINESS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUSINESS
(
  BUS_ID   NUMBER(38) not null,
  BUS_NAME VARCHAR2(64),
  BUS_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUSINESS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUSINESS
	  add constraint PK_LF_BUSINESS primary key (BUS_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUSMANAGER
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUSMANAGER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUSMANAGER
(
  BUS_ID          NUMBER(38) not null,
  BUS_CODE        VARCHAR2(64) not null,
  BUS_NAME        VARCHAR2(64) not null,
  BUS_DESCRIPTION VARCHAR2(2000),
  CLASS_NAME      VARCHAR2(2000),
  CORP_CODE       VARCHAR2(64),
  BUS_TYPE        NUMBER(1) DEFAULT 0,
  RISELEVEL       NUMBER(2) DEFAULT -99,
  CREATE_TIME     TIMESTAMP(8) DEFAULT SYSDATE,
  UPDATE_TIME     TIMESTAMP(8) DEFAULT SYSDATE,
  DEP_ID          NUMBER(38),
  USER_ID         NUMBER(38),
  STATE           NUMBER(2) DEFAULT 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUSMANAGER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUSMANAGER
	  add constraint PK_LF_BUSMANAGER primary key (BUS_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SHORTTEMP
-- 新增LF_SHORTTEMP(快捷场景)表
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SHORTTEMP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SHORTTEMP(
   ID NUMBER(18) NOT NULL,--主键
   USERID NUMBER(18) DEFAULT 0 NOT NULL,
   CORPCODE VARCHAR2(32) DEFAULT '' '' NOT NULL,
   TEMPID NUMBER(18) DEFAULT 0 NOT NULL,
   TEMPNAME VARCHAR2(32) DEFAULT '' '' NOT NULL,
   ADDTIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SHORTTEMP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_SHORTTEMP
	  ADD CONSTRAINT PK_LF_SHORTTEMP PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUSPKGETAOCAN
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUSPKGETAOCAN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUSPKGETAOCAN
(
  ID             NUMBER(38) not null,
  TAOCAN_CODE    VARCHAR2(64),
  PACKAGE_CODE   VARCHAR2(64) not null,
  BUS_CODE       VARCHAR2(64),
  ASSOCIATE_TYPE NUMBER(1) not null,
  CORP_CODE      VARCHAR2(64),
  CREATE_TIME    TIMESTAMP(8) DEFAULT SYSDATE
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUSPKGETAOCAN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUSPKGETAOCAN
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUSTAIL
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUSTAIL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUSTAIL
(
  BUSTAIL_ID   NUMBER(38) not null,
  BUSTAIL_NAME VARCHAR2(64) not null,
  CORP_CODE    VARCHAR2(64),
  CONTENT      VARCHAR2(2000) not null,
  CREATE_TIME  TIMESTAMP(8) DEFAULT SYSDATE,
  UPDATE_TIME  TIMESTAMP(8) DEFAULT SYSDATE,
  DEP_ID       NUMBER(38),
  USER_ID      NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUSTAIL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUSTAIL
	  add primary key (BUSTAIL_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUS_PACKAGE
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUS_PACKAGE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUS_PACKAGE
(
  PACKAGE_ID    NUMBER(38) not null,
  PACKAGE_CODE  VARCHAR2(64) not null,
  PACKAGE_NAME  VARCHAR2(64) not null,
  PACKAGE_DES   VARCHAR2(2000) DEFAULT '''',
  PACKAGE_STATE NUMBER(1) not null,
  CORP_CODE     VARCHAR2(64),
  CREATE_TIME   TIMESTAMP(8),
  UPDATE_TIME   TIMESTAMP(8),
  DEP_ID        NUMBER(38),
  USER_ID       NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUS_PACKAGE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUS_PACKAGE
	  add primary key (PACKAGE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_BUS_PACKAGE') AND T.INDEX_NAME = UPPER('PACKAGECODEUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUS_PACKAGE
	  add constraint PACKAGECODEUNIQUE unique (PACKAGE_CODE)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUS_PROCESS
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUS_PROCESS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUS_PROCESS
(
  BUSPRO_ID  NUMBER(38) not null,
  BUS_CODE   VARCHAR2(64),
  MENU_CODE  VARCHAR2(64),
  CLASS_NAME VARCHAR2(64),
  REG_TYPE   NUMBER(1),
  CODES      VARCHAR2(64),
  CODE_TYPE  NUMBER(2),
  HTTP_URL   VARCHAR2(1024),
  SEND_TYPE  NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUS_PROCESS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUS_PROCESS
	  add constraint PK_LF_BUS_PROCESS primary key (BUSPRO_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUS_TAILTMP
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUS_TAILTMP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUS_TAILTMP
(
  ID             NUMBER(38) not null,
  BUS_ID         NUMBER(38) not null,
  SMSTAIL_ID     NUMBER(38),
  TM_ID          NUMBER(38),
  ASSOCIATE_TYPE NUMBER(1) not null,
  CORP_CODE      VARCHAR2(64),
  CREATE_TIME    TIMESTAMP(8) DEFAULT SYSDATE,
  UPDATE_TIME    TIMESTAMP(8) DEFAULT SYSDATE,
  DEP_ID         NUMBER(38),
  USER_ID        NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUS_TAILTMP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUS_TAILTMP
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_FILEDATA(文件二进制表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_FILEDATA';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_FILEDATA
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,            --否  自增ID,全局唯一
  FILEURL  VARCHAR2(128) DEFAULT '' '' NOT NULL,    --文件url 唯一
  FSDATA   BLOB DEFAULT EMPTY_BLOB() NOT NULL,    --二进制数据
  NODEID   VARCHAR2(24) DEFAULT '' '' NOT NULL,     --节点编号
  CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL  --创建时间
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_FILEDATA') AND T.INDEX_NAME = UPPER('UQ_FILEURL');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_FILEDATA
	  ADD CONSTRAINT UQ_FILEURL UNIQUE (FILEURL)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUS_TAOCAN
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUS_TAOCAN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_BUS_TAOCAN
(
  TAOCAN_ID    NUMBER(38) not null,
  TAOCAN_CODE  VARCHAR2(64) not null,
  TAOCAN_NAME  VARCHAR2(64) not null,
  TAOCAN_DES   VARCHAR2(2000) DEFAULT '''',
  STATE        NUMBER(1) not null,
  CORP_CODE    VARCHAR2(64),
  CREATE_TIME  TIMESTAMP(8),
  UPDATE_TIME  TIMESTAMP(8),
  DEP_ID       NUMBER(38),
  USER_ID      NUMBER(38),
  START_DATE   TIMESTAMP(8) DEFAULT SYSDATE,
  END_DATE     TIMESTAMP(8) DEFAULT SYSDATE,
  TAOCAN_TYPE  NUMBER(2) not null,
  TAOCAN_MONEY NUMBER(20) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BUS_TAOCAN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUS_TAOCAN
	  add primary key (TAOCAN_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_BUS_TAOCAN') AND T.INDEX_NAME = UPPER('TAOCANCODEUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_BUS_TAOCAN
	  add constraint TAOCANCODEUNIQUE unique (TAOCAN_CODE)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CLIDEP_CONN
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CLIDEP_CONN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CLIDEP_CONN
(
  USER_ID        NUMBER(38) not null,
  DEP_CODE_THIRD VARCHAR2(64) DEFAULT ''0'' not null,
  CONN_ID        NUMBER(38),
  DEP_ID         NUMBER(38) DEFAULT 0 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CLIDEP_CONN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CLIDEP_CONN
	  add primary key (USER_ID, DEP_CODE_THIRD)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CLIENT
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CLIENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CLIENT
(
  CLIENT_ID   NUMBER(38) not null,
  DEP_ID      NUMBER(38) not null,
  CC_ID       NUMBER(38),
  NAME        VARCHAR2(64),
  SEX         NUMBER(1),
  BIRTHDAY    DATE,
  MOBILE      VARCHAR2(200),
  OPH         VARCHAR2(64),
  QQ          VARCHAR2(32),
  E_MAIL      VARCHAR2(64),
  MSN         VARCHAR2(64),
  JOB         VARCHAR2(64),
  PROFESSION  VARCHAR2(64),
  AREA        VARCHAR2(64),
  C_STATE     NUMBER(4),
  SHARE_STATE NUMBER(1),
  COMMENTS    VARCHAR2(512),
  REC_STATE   NUMBER(1),
  HIDE_STATE  NUMBER(1),
  USER_ID     NUMBER(38),
  ENAME       VARCHAR2(64),
  BATCH_NO    VARCHAR2(64),
  PHONE       VARCHAR2(200),
  DEP_CODE    VARCHAR2(64),
  BIZ_ID      VARCHAR2(64),
  CLIENT_CODE VARCHAR2(64),
  GUID        NUMBER(38),
  CORP_CODE   VARCHAR2(64),
  FIELD01     VARCHAR2(64),
  FIELD02     VARCHAR2(64),
  FIELD03     VARCHAR2(64),
  FIELD04     VARCHAR2(64),
  FIELD05     VARCHAR2(64),
  FIELD06     VARCHAR2(64),
  FIELD07     VARCHAR2(64),
  FIELD08     VARCHAR2(64),
  FIELD09     VARCHAR2(64),
  FIELD10     VARCHAR2(64),
  FIELD11     VARCHAR2(64),
  FIELD12     VARCHAR2(64),
  FIELD13     VARCHAR2(64),
  FIELD14     VARCHAR2(64),
  FIELD15     VARCHAR2(64),
  FIELD16     VARCHAR2(64),
  FIELD17     VARCHAR2(64),
  FIELD18     VARCHAR2(64),
  FIELD19     VARCHAR2(64),
  FIELD20     VARCHAR2(64),
  FIELD21     VARCHAR2(64),
  FIELD22     VARCHAR2(64),
  FIELD23     VARCHAR2(64),
  FIELD24     VARCHAR2(64),
  FIELD25     VARCHAR2(64),
  FIELD26     VARCHAR2(64),
  FIELD27     VARCHAR2(64),
  FIELD28     VARCHAR2(64),
  FIELD29     VARCHAR2(64),
  FIELD30     VARCHAR2(64),
  FIELD31     VARCHAR2(64),
  FIELD32     VARCHAR2(64),
  FIELD33     VARCHAR2(64),
  FIELD34     VARCHAR2(64),
  FIELD35     VARCHAR2(64),
  FIELD36     VARCHAR2(64),
  FIELD37     VARCHAR2(64),
  FIELD38     VARCHAR2(64),
  FIELD39     VARCHAR2(64),
  FIELD40     VARCHAR2(64),
  FIELD41     VARCHAR2(64),
  FIELD42     VARCHAR2(64),
  FIELD43     VARCHAR2(64),
  FIELD44     VARCHAR2(64),
  FIELD45     VARCHAR2(64),
  FIELD46     VARCHAR2(64),
  FIELD47     VARCHAR2(64),
  FIELD48     VARCHAR2(64),
  FIELD49     VARCHAR2(64),
  FIELD50     VARCHAR2(64),
  ISCONTRACT  NUMBER(2) DEFAULT 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CLIENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CLIENT
	  add constraint PK_LF_CLIENT primary key (CLIENT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT') AND T.INDEX_NAME = UPPER('C_GUIDUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CLIENT
	  add constraint C_GUIDUNIQUE unique (GUID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT') AND T.INDEX_NAME = UPPER('FK_LF_CLI_RELATIONS_LF_CLI_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_CLI_RELATIONS_LF_CLI_FK on LF_CLIENT (CC_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT') AND T.INDEX_NAME = UPPER('FK_LF_CLI_RELATIONS_LF_DEP_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_CLI_RELATIONS_LF_DEP_FK on LF_CLIENT (DEP_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT') AND T.INDEX_NAME = UPPER('INDEX_LF_CI_CORP_CODE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_CI_CORP_CODE on LF_CLIENT (CORP_CODE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT') AND T.INDEX_NAME = UPPER('INDEX_LF_CI_NAME');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_CI_NAME on LF_CLIENT (NAME)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT') AND T.INDEX_NAME = UPPER('INDEX_LF_CI_PHONE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_CI_PHONE on LF_CLIENT (PHONE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CLIENTCLASS
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CLIENTCLASS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CLIENTCLASS
(
  CC_ID   NUMBER(38) not null,
  CC_NAME VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CLIENTCLASS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CLIENTCLASS
	  add constraint PK_LF_CLIENTCLASS primary key (CC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_STATECODE(状态码管理表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_STATECODE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_STATECODE
(
STATE_ID NUMBER(38) PRIMARY KEY NOT NULL,--  ID，自增
STATE_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,       --状态码
STATE_DES VARCHAR2(256) DEFAULT '' '' NOT NULL,     --解释说明
CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,   --创建时间
UPDATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,   --更新时间
CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,       --企业编码
MAPPING_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,
USER_ID NUMBER(38) DEFAULT 0 NOT NULL           --最后更新的操作员ID
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_CLIENT_DEP
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CLIENT_DEP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CLIENT_DEP
(
  DEP_ID         NUMBER(38) not null,
  DEP_CODE       VARCHAR2(64),
  DEP_NAME       VARCHAR2(64),
  DEP_PCODE      VARCHAR2(64),
  DEP_LEVEL      NUMBER(2) DEFAULT 0,
  ADD_TYPE       NUMBER(2),
  CORP_CODE      VARCHAR2(64),
  PARENT_ID      NUMBER(38) DEFAULT 0 not null,
  DEP_CODE_THIRD VARCHAR2(64) DEFAULT '''',
  DEP_PATH       VARCHAR2(160) DEFAULT ''''
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CLIENT_DEP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CLIENT_DEP
	  add constraint PK_LF_CLIENT_DEPT primary key (DEP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CLIENT_DEP_SP
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CLIENT_DEP_SP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CLIENT_DEP_SP
(
  CLIENT_ID NUMBER(38) not null,
  DEP_ID    NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT_DEP_SP') AND T.INDEX_NAME = UPPER('INDEX_LF_CI_SP_CLI_ID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_CI_SP_CLI_ID on LF_CLIENT_DEP_SP (CLIENT_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CLIENT_DEP_SP') AND T.INDEX_NAME = UPPER('INDEX_LF_CI_SP_DEP_ID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_CI_SP_DEP_ID on LF_CLIENT_DEP_SP (DEP_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CONTRACT
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CONTRACT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CONTRACT
(
  CONTRACT_ID       NUMBER(38) not null,
  MOBILE            VARCHAR2(128) not null,
  CUSTOM_NAME       VARCHAR2(120) not null,
  IDENT_TYPE        VARCHAR2(2),
  IDENT_NO          VARCHAR2(20),
  CONTRACT_DEP      NUMBER(38) not null,
  CONTRACT_USER     NUMBER(38) not null,
  CONTRACT_DATE     TIMESTAMP(8) DEFAULT sysdate not null,
  ADD_ORDER_DATE    TIMESTAMP(8) DEFAULT sysdate,
  CANCEL_ORDER_DATE TIMESTAMP(8) DEFAULT sysdate,
  CONTRACT_STATE    NUMBER(2) DEFAULT 0 not null,
  CONTRACT_TYPE     NUMBER(2) DEFAULT 0,
  CUSTOM_TYPE       NUMBER(1) DEFAULT 0,
  ADDRESS           VARCHAR2(256),
  CONTRACT_SOURCE   NUMBER(2),
  CANCEL_CONTYPE    NUMBER(2),
  CANCEL_CONTIME    TIMESTAMP(8),
  CORP_CODE         VARCHAR2(64),
  DEP_ID            NUMBER(38),
  USER_ID           NUMBER(38),
  UPDATE_TIME       TIMESTAMP(8) DEFAULT sysdate not null,
  ACCT_COMNAME      VARCHAR2(64),
  ACCT_NO           VARCHAR2(25),
  ACCT_NAME         VARCHAR2(120),
  ACCT_IDENTTYPE    VARCHAR2(2),
  ACCT_IDENTNO      VARCHAR2(20),
  ACCT_ADDRESS      VARCHAR2(256),
  IS_VALID          VARCHAR2(1) DEFAULT 0 not null,
  DEBITACCOUNT      VARCHAR2(25),
  DEBITACCT_NAME    VARCHAR2(120),
  DEBITACCT_DEP     NUMBER(38),
  CLIENT_CODE       VARCHAR2(64),
  GUID              NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CONTRACT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CONTRACT
	  add primary key (CONTRACT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CONTRACT_TAOCAN
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CONTRACT_TAOCAN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CONTRACT_TAOCAN
(
  ID             NUMBER(38) not null,
  CONTRACT_ID    NUMBER(38) not null,
  DEBITACCOUNT   VARCHAR2(25),
  TAOCAN_CODE    VARCHAR2(64) not null,
  TAOCAN_NAME    VARCHAR2(64),
  CONTRACT_DEP   NUMBER(38),
  CONTRACT_USER  NUMBER(38),
  TAOCAN_TYPE    NUMBER(2),
  TAOCAN_MONEY   NUMBER(20),
  DEP_ID         NUMBER(38),
  USER_ID        NUMBER(38),
  CREATE_TIME    TIMESTAMP(8) DEFAULT SYSDATE,
  UPDATE_TIME    TIMESTAMP(8) DEFAULT SYSDATE,
  CORP_CODE      VARCHAR2(64),
  BUCKLEFEE_TIME TIMESTAMP(8),
  GUID           NUMBER(38),
  IS_VALID       VARCHAR2(1) DEFAULT ''0'' not null,
  LAST_BUCKLEFEE VARCHAR2(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CONTRACT_TAOCAN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CONTRACT_TAOCAN
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CORP
-- ======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CORP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CORP
(
  CORP_ID     NUMBER(38) not null,
  CORP_CODE   VARCHAR2(64),
  CORP_NAME   VARCHAR2(64),
  MOBILE      VARCHAR2(64),
  PHONE       VARCHAR2(64),
  ADDRESS     VARCHAR2(200),
  LINKMAN     VARCHAR2(64),
  E_MAILS     VARCHAR2(200),
  USER_NAME   VARCHAR2(64),
  LGO_URL     VARCHAR2(200),
  ISBALANCE   NUMBER(1) DEFAULT 0,
  SUBNO_DIGIT NUMBER(2) DEFAULT 4,
  CUR_SUBNO   VARCHAR2(20) DEFAULT ''0'',
  ISOPENTD    INT DEFAULT 0 NOT NULL, --是否开启上行退订功能 默认值为0  0不启动  1是启用
  CORP_STATE NUMBER(2) DEFAULT 1 NOT NULL,--企业状态 (0禁用 1启用) 默认值启用
  RPTFLAG    NUMBER(5) DEFAULT 1 NOT NULL -- 0:表示不需要;1:需要通知状态报告;2:需要下载状态报告;3:通知、下载状态报告都要;(默认通知状态报告必须)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_CORP_CONF
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CORP_CONF';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CORP_CONF
(
  CC_ID       NUMBER(38) not null,
  CORP_CODE   VARCHAR2(64),
  PARAM_KEY   VARCHAR2(64),
  PARAM_VALUE VARCHAR2(256)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_CORP_CONF') AND T.INDEX_NAME = UPPER('UQ_PARAMCORP');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CORP_CONF
	  add constraint UQ_PARAMCORP unique (PARAM_KEY,CORP_CODE)
	  using index
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SPFEE_LOG(SP账号充值/回收日志)
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SPFEE_LOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SPFEE_LOG
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,   --自增ID
  SPUSER VARCHAR2(11) DEFAULT '' '' NOT NULL ,-- SP账号
  ICOUNT NUMBER(20) DEFAULT 0 NOT NULL ,  -- 充值/回收数量
  RESULT NUMBER(20) DEFAULT 0 NOT NULL ,  -- DEFAULT 0 ：成功，1：失败
  USERID NUMBER(38) DEFAULT 0 NOT NULL,  -- 操作员ID
  CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL ,-- 企业编码
  OPR_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, -- 操作时间
  MEMO VARCHAR2(512) DEFAULT '' '' NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_CUSTFIELD
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CUSTFIELD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CUSTFIELD
(
  ID         NUMBER(38) not null,
  FIELD_REF  VARCHAR2(64),
  FIELD_NAME VARCHAR2(64),
  V_TYPE     VARCHAR2(64),
  CORP_CODE  VARCHAR2(64),
  USERID     VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CUSTFIELD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CUSTFIELD
	  add constraint PK_LF_CUSTFIELD primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_CUSTFIELD_VALUE
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_CUSTFIELD_VALUE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_CUSTFIELD_VALUE
(
  ID          NUMBER(38) not null,
  FIELD_ID    NUMBER(38),
  FIELD_VALUE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_CUSTFIELD_VALUE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_CUSTFIELD_VALUE
	  add constraint PK_LF_CUSTFIELD_VALUE primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DBCONNECT
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DBCONNECT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DBCONNECT
(
  DB_ID        NUMBER(38) not null,
  DBCON_NAME   VARCHAR2(64),
  DBCON_IP     VARCHAR2(64),
  DB_TYPE      VARCHAR2(64),
  SERVICE_NAME VARCHAR2(64),
  DB_NAME      VARCHAR2(64),
  PORT         VARCHAR2(64),
  DB_USER      VARCHAR2(64),
  DB_PWD       VARCHAR2(64),
  CON_STR      VARCHAR2(256),
  COMMENTS     VARCHAR2(512),
  DBCONN_TYPE  NUMBER(1),
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DBCONNECT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DBCONNECT
	  add constraint PK_LF_DBCONNECT primary key (DB_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DEDUCTIONS_DISP
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEDUCTIONS_DISP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DEDUCTIONS_DISP
(
  ID               NUMBER(38) not null,
  CONTRACT_ID      NUMBER(38) not null,
  MOBILE           VARCHAR2(64) not null,
  CUSTOM_NAME      VARCHAR2(120) not null,
  ACCT_NO          VARCHAR2(25),
  DEBITACCOUNT     VARCHAR2(25),
  TAOCAN_CODE      VARCHAR2(64),
  TAOCAN_NAME      VARCHAR2(64),
  TAOCAN_TYPE      NUMBER(2),
  TAOCAN_MONEY     NUMBER(20),
  CONTRACT_STATE   NUMBER(2),
  DEDUCTIONS_TYPE  NUMBER(1),
  DEDUCTIONS_MONEY NUMBER(20),
  OPR_STATE        NUMBER(1) not null,
  OPR_TIME         TIMESTAMP(8) DEFAULT SYSDATE,
  CONTRACT_DEP     NUMBER(38),
  DEP_NAME         VARCHAR2(64),
  CONTRACT_USER    NUMBER(38),
  USER_NAME        VARCHAR2(64),
  DEP_ID           NUMBER(38),
  USER_ID          NUMBER(38),
  CORP_CODE        VARCHAR2(64),
  MSG_ID           VARCHAR2(256) not null,
  UPDATE_TIME      TIMESTAMP(8) DEFAULT SYSDATE
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_DISP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEDUCTIONS_DISP
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_DISP') AND T.INDEX_NAME = UPPER('MSGIDDISPUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEDUCTIONS_DISP
	  add constraint MSGIDDISPUNIQUE unique (MSG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DRAFTS(草稿箱管理表)
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DRAFTS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_DRAFTS
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,  --ID，自增
  TITLE VARCHAR2(64) DEFAULT '' '' NOT NULL,        --标题
  MSG VARCHAR2(2048) DEFAULT '' '' NOT NULL,        --短信内容
  MOBILE_URL VARCHAR2(160) DEFAULT '' '' NOT NULL,  --号码文件地址
  DRAFTS_TYPE NUMBER(2) DEFAULT 0 NOT NULL,       --草稿类型，0－相同内容；1－不同内容动态模板；2－不同内容文件发送；3－客户群组群发；4-静态网讯发送；5－动态网讯发送
  CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --创建时间
  UPDATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --更新时间
  CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,     --企业编码
  USER_ID NUMBER(38) DEFAULT 0 NOT NULL         --操作员ID
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DRAFTS') AND T.INDEX_NAME = UPPER('UQ_DRAFTS_MU');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_DRAFTS
	  ADD CONSTRAINT UQ_DRAFTS_MU UNIQUE (MOBILE_URL)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DEDUCTIONS_LIST
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEDUCTIONS_LIST';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DEDUCTIONS_LIST
(
  ID               NUMBER(38) not null,
  CONTRACT_ID      NUMBER(38) not null,
  MOBILE           VARCHAR2(64) not null,
  CUSTOM_NAME      VARCHAR2(120) not null,
  ACCT_NO          VARCHAR2(25),
  DEBITACCOUNT     VARCHAR2(25),
  TAOCAN_CODE      VARCHAR2(64),
  TAOCAN_NAME      VARCHAR2(64),
  TAOCAN_TYPE      NUMBER(2),
  TAOCAN_MONEY     NUMBER(20),
  IYEAR            NUMBER(20),
  IMONTH           NUMBER(20),
  BUPSUM_MONEY     NUMBER(20),
  BUPTIMER         NUMBER(20),
  CONTRACT_STATE   NUMBER(2) DEFAULT 0,
  CONTRACT_TYPE    NUMBER(2) DEFAULT 0,
  DEDUCTIONS_TYPE  NUMBER(2),
  BUCKUP_TIMER     NUMBER(3),
  UDPATE_TIME      TIMESTAMP(8) DEFAULT SYSDATE,
  BUCKLEFEE_TIME   TIMESTAMP(8) DEFAULT SYSDATE,
  BUCKLEUPFEE_TIME TIMESTAMP(8) DEFAULT SYSDATE,
  CONTRACT_DEP     NUMBER(38),
  DEP_NAME         VARCHAR2(64),
  CONTRACT_USER    NUMBER(38),
  DEP_ID           NUMBER(38),
  USER_ID          NUMBER(38),
  CORP_CODE        VARCHAR2(64),
  MSG_ID           VARCHAR2(256) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_LIST') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEDUCTIONS_LIST
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_LIST') AND T.INDEX_NAME = UPPER('MSGIDUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEDUCTIONS_LIST
	  add constraint MSGIDUNIQUE unique (MSG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_LIST') AND T.INDEX_NAME = UPPER('I_LF_DEDUCTIONS_LIST_D');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index I_LF_DEDUCTIONS_LIST_D on LF_DEDUCTIONS_LIST (CONTRACT_DEP)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_LIST') AND T.INDEX_NAME = UPPER('I_LF_DEDUCTIONS_LIST_M');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index I_LF_DEDUCTIONS_LIST_M on LF_DEDUCTIONS_LIST (IMONTH)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_LIST') AND T.INDEX_NAME = UPPER('I_LF_DEDUCTIONS_LIST_U');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index I_LF_DEDUCTIONS_LIST_U on LF_DEDUCTIONS_LIST (CONTRACT_USER)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DEDUCTIONS_LIST') AND T.INDEX_NAME = UPPER('I_LF_DEDUCTIONS_LIST_Y');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index I_LF_DEDUCTIONS_LIST_Y on LF_DEDUCTIONS_LIST (IYEAR)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DEP
-- =====================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DEP
(
  DEP_ID         NUMBER(38) not null,
  DEP_NAME       VARCHAR2(64),
  DEP_CONTACT    VARCHAR2(64),
  DEP_LEVEL      NUMBER(4),
  DEP_CODE       VARCHAR2(64),
  DEP_RESP       VARCHAR2(512),
  SUPERIOR_ID    NUMBER,
  DEP_CODE_THIRD VARCHAR2(64),
  CORP_CODE      VARCHAR2(64),
  DEP_DIRECT     NUMBER(38),
  DEP_STATE      NUMBER(1) DEFAULT 1 not null,
  DEP_PATH       VARCHAR2(160) DEFAULT '''',
  UP_DEPID       VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEP
	  add constraint PK_LF_DEP primary key (DEP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DEPPWDRECEIVER
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEPPWDRECEIVER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DEPPWDRECEIVER
(
  DPR_ID     NUMBER(38) not null,
  DEP_ID     NUMBER(38),
  USER_ID    NUMBER(38),
  CREATETIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEPPWDRECEIVER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEPPWDRECEIVER
	  add primary key (DPR_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DEP_RECHARGE_LOG
-- ==================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEP_RECHARGE_LOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DEP_RECHARGE_LOG
(
  LOG_ID       NUMBER(38) not null,
  OPT_TYPE     NUMBER(3) not null,
  OPT_INFO     VARCHAR2(120) not null,
  SRC_TARGETID NUMBER(38) not null,
  DST_TARGETID NUMBER(38) not null,
  MSG_TYPE     NUMBER(1) not null,
  COUNT        NUMBER(38) not null,
  OPT_ID       NUMBER(38) not null,
  OPT_DATE     DATE not null,
  RESULT       NUMBER(4) not null,
  MEMO         VARCHAR2(512) not null,
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEP_RECHARGE_LOG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEP_RECHARGE_LOG
	  add constraint PK_LF_DEP_RECHARGE_LOG primary key (LOG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DEP_TAOCAN
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEP_TAOCAN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DEP_TAOCAN
(
  ID            NUMBER(38) not null,
  TAOCAN_CODE   VARCHAR2(64) not null,
  CONTRACT_DEP  NUMBER(38) not null,
  CONTRACT_USER NUMBER(38) not null,
  DEP_ID        NUMBER(38),
  USER_ID       NUMBER(38),
  CORP_CODE     VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEP_TAOCAN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEP_TAOCAN
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DEP_USER_BALANCE
-- ==================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DEP_USER_BALANCE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DEP_USER_BALANCE
(
  BL_ID           NUMBER(38) not null,
  TARGET_ID       NUMBER(38) not null,
  SMS_BALANCE     NUMBER(20) not null,
  SMS_COUNT       NUMBER(20) not null,
  MMS_BALANCE     NUMBER(20) not null,
  MMS_COUNT       NUMBER(20) not null,
  CORP_CODE       VARCHAR2(64),
  SMS_ALARM_VALUE NUMBER(20),
  MMS_ALARM_VALUE NUMBER(20),
  ALARM_PHONE     VARCHAR2(256),
  ALARM_NAME      VARCHAR2(12),
  ALARM_COUNT     NUMBER(20),
  ALARMED_COUNT   NUMBER(20),
  HAS_DAYALARM    NUMBER(20),
  MMSALARMEDCOUNT NUMBER(20) DEFAULT 0 NOT NULL --彩信已告警次数
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DEP_USER_BALANCE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEP_USER_BALANCE
	  add constraint PK_LF_DEP_USER_BALANCE primary key (BL_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DEP_USER_BALANCE') AND T.INDEX_NAME = UPPER('TARGETIDUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DEP_USER_BALANCE
  add constraint TARGETIDUNIQUE unique (TARGET_ID)';
	END IF;
END;
/

--
-- Creating table LF_DFADVANCED
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DFADVANCED';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DFADVANCED
(
  ID            NUMBER(38) not null,
  USER_ID       NUMBER(38) not null,
  BUS_CODE      VARCHAR2(64) DEFAULT '''',
  PRIORITY      VARCHAR2(8) DEFAULT ''0'',
  SPUSER_ID     VARCHAR2(11) DEFAULT '' '',
  REPLY_SET     NUMBER(2) DEFAULT 0,
  REPEAT_FILTER NUMBER(1) DEFAULT 2,
  CREATE_TIME   TIMESTAMP(8),
  FLAG          NUMBER(3) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DFADVANCED') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DFADVANCED
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SYSUSER
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SYSUSER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SYSUSER
(
  USER_ID         NUMBER(38) not null,
  DEP_ID          NUMBER(38),
  USER_NAME       VARCHAR2(64),
  NAME            VARCHAR2(64),
  SEX             NUMBER(1),
  BIRTHDAY        DATE,
  MOBILE          VARCHAR2(64),
  OPH             VARCHAR2(64),
  QQ              VARCHAR2(32),
  E_MAIL          VARCHAR2(64),
  MSN             VARCHAR2(64),
  PASSWORD        VARCHAR2(128),
  USER_STATE      NUMBER(1),
  REG_TIME        DATE,
  HOLDER          VARCHAR2(64),
  COMMENTS        VARCHAR2(512),
  WORK_STATE      NUMBER(1),
  MANUAL_INPUT    NUMBER(1),
  USER_TYPE       NUMBER(4),
  UDG_ID          NUMBER(38),
  CLIENT_STATE    NUMBER(1),
  EMPLOYEEID      NUMBER(38),
  GUID            NUMBER(38),
  ON_LINE         NUMBER(1) DEFAULT 0,
  CORP_CODE       VARCHAR2(64),
  P_ID            NUMBER(38),
  EMPLOYEE_NO     VARCHAR2(64),
  REC_STATE       NUMBER(1) DEFAULT 0,
  HIDEPH_STATE    NUMBER(1) DEFAULT 0,
  DEP_CODE        VARCHAR2(64),
  LASTUPDDTIM     TIMESTAMP(8),
  HR_STATUS       VARCHAR2(20) DEFAULT ''A'',
  FAX             VARCHAR2(22),
  ALLOW_LOGIN     NUMBER(1) DEFAULT 0,
  POST_ID         NUMBER(38),
  PERMISSION_TYPE NUMBER(1) DEFAULT 2,
  USER_CODE       VARCHAR2(30),
  ISEXISTSUBNO    NUMBER(1),
  UP_GUID         NUMBER(38),
  IS_REVIEWER     NUMBER(2),
  DUTIES          VARCHAR2(200),
  IS_AUDITED      NUMBER(2),
  CONTROLFLAG     VARCHAR2(16) DEFAULT ''0000'' not null,
  PWDUPDATETIME   TIMESTAMP(8) DEFAULT sysdate,
  PWDERRORTIMES   NUMBER(20) DEFAULT 0,
  WORKNUMBER      VARCHAR2(30),
  FINDPWDTIMES    NUMBER(20),
  FINDPWDDATE     VARCHAR2(30),
  IS_CUSTOME      NUMBER(20),
  LANGUAGE_CODE   VARCHAR2(10),
  SHOWNUM NUMBER(11) DEFAULT 0 NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
	EXECUTE IMMEDIATE 'alter table LF_SYSUSER
	  add constraint FK_LF_SYSUS_FK_LF_SYS_LF_DEP foreign key (DEP_ID)
	  references LF_DEP (DEP_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SYSUSER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SYSUSER
	  add constraint PK_LF_SYSUSER primary key (USER_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SYSUSER') AND T.INDEX_NAME = UPPER('GUIDUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SYSUSER
	  add constraint GUIDUNIQUE unique (GUID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SYSUSER') AND T.INDEX_NAME = UPPER('FK_LF_SYS_RELATIONS_LF_DEP_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_SYS_RELATIONS_LF_DEP_FK on LF_SYSUSER (DEP_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DOMINATION
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DOMINATION';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DOMINATION
(
  DEP_ID  NUMBER(38) not null,
  USER_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_DOMINATION
  add constraint FK_LF_DOMIN_LF_DOMINA_LF_DEP foreign key (DEP_ID)
  references LF_DEP (DEP_ID)';
  EXECUTE IMMEDIATE 'alter table LF_DOMINATION
  add constraint FK_LF_DOMIN_LF_DOMINA_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DOMINATION') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_DOMINATION
	  add constraint PK_LF_DOMINATION primary key (DEP_ID, USER_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DOMINATION') AND T.INDEX_NAME = UPPER('LF_DOMINATION_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_DOMINATION_FK on LF_DOMINATION (DEP_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_DOMINATION') AND T.INDEX_NAME = UPPER('LF_DOMINATION_FK2');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_DOMINATION_FK2 on LF_DOMINATION (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_DYNPHONEWORD
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DYNPHONEWORD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_DYNPHONEWORD
(
  USERID     NUMBER(38) not null,
  PHONEWORD  VARCHAR2(10),
  CREATETIME DATE,
  ISLOGIN    NUMBER(38) DEFAULT 0 not null,
  ERRORCODE  CHAR(7),
  PTMSGID    NUMBER(22)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_SERVICE
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SERVICE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SERVICE
(
  SER_ID        NUMBER(38) not null,
  USER_ID       NUMBER(38),
  SER_NAME      VARCHAR2(64),
  COMMENTS      VARCHAR2(512),
  ORDER_CODE    VARCHAR2(64),
  SUBNO         VARCHAR2(64),
  SP_USER       VARCHAR2(64),
  STAFFNAME     VARCHAR2(32),
  SP_PWD        VARCHAR2(32),
  RUN_STATE     NUMBER(1),
  SER_TYPE      NUMBER(4),
  MSG_SEPARATED VARCHAR2(32),
  OWNER_ID      NUMBER(38),
  CREATE_TIME   TIMESTAMP(8),
  BUS_ID        NUMBER(38),
  CPNO          VARCHAR2(21) DEFAULT '' '',
  BUS_CODE      VARCHAR2(64),
  MENU_CODE     VARCHAR2(64),
  CORP_CODE     VARCHAR2(64),
  IDENTIFY_MODE NUMBER(20),
  ORDER_TYPE    NUMBER(20),
  STRUCTCODE    VARCHAR2(20)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_SERVICE
  add constraint FK_LF_SERVI_LF_ADDSER_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  EXECUTE IMMEDIATE 'alter table LF_SERVICE
  add constraint FK_SERVICE_BUSMANAGER foreign key (BUS_ID)
  references LF_BUSMANAGER (BUS_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SERVICE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SERVICE
	  add constraint PK_LF_SERVICE primary key (SER_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SERVICE') AND T.INDEX_NAME = UPPER('LF_ADDSERVICE_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_ADDSERVICE_FK on LF_SERVICE (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PROCESS
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PROCESS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PROCESS
(
  PR_ID         NUMBER(38) not null,
  DB_ID         NUMBER(38),
  SER_ID        NUMBER(38),
  SG_ID         NUMBER(38),
  PR_NAME       VARCHAR2(64),
  COMMENTS      VARCHAR2(512),
  PR_TYPE       NUMBER(4),
  FINAL_STATE   NUMBER(1),
  GR_STATE      NUMBER(1),
  PR_RESULT     NUMBER,
  TABLE_NAME    VARCHAR2(64),
  SQL_STR       VARCHAR2(2048),
  NEXTPR_ID     NUMBER,
  SYS_ERROR     VARCHAR2(512),
  DB_ERROR      VARCHAR2(512),
  PR_NO         NUMBER(38),
  PREPR_ID      NUMBER(38),
  PRO_CODE      VARCHAR2(64),
  MSG_SEPARATED VARCHAR2(32),
  MSG_TYPE      NUMBER(2),
  MSG_CONTENT   VARCHAR2(1024),
  TEMPLATE_ID   NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE  'alter table LF_PROCESS
  add constraint FK_LF_PROCE_FK_LF_PRO_LF_DBCON foreign key (DB_ID)
  references LF_DBCONNECT (DB_ID) on delete set null';
  EXECUTE IMMEDIATE 'alter table LF_PROCESS
  add constraint FK_LF_PROCE_FK_LF_PRO_LF_SERVI foreign key (SER_ID)
  references LF_SERVICE (SER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PROCESS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PROCESS
	  add constraint PK_LF_PROCESS primary key (PR_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PROCESS') AND T.INDEX_NAME = UPPER('FK_LF_PRO_RELATIONS_LF_DBC_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_PRO_RELATIONS_LF_DBC_FK on LF_PROCESS (DB_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PROCESS') AND T.INDEX_NAME = UPPER('FK_LF_PRO_RELATIONS_LF_SEN_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_PRO_RELATIONS_LF_SEN_FK on LF_PROCESS (SG_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/
  
DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PROCESS') AND T.INDEX_NAME = UPPER('FK_LF_PRO_RELATIONS_LF_SER_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_PRO_RELATIONS_LF_SER_FK on LF_PROCESS (SER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_EDIT
-- ======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_EDIT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_EDIT
(
  EDIT_ID    NUMBER(38) not null,
  PR_ID      NUMBER(38),
  EDIT_NAME  VARCHAR2(64),
  EDIT_VALUE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_EDIT
  add constraint FK_LF_EDIT_FK_LF_EDI_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_EDIT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EDIT
	  add constraint PK_LF_EDIT primary key (EDIT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_EDIT') AND T.INDEX_NAME = UPPER('FK_LF_EDI_RELATIONS_LF_PRO_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_EDI_RELATIONS_LF_PRO_FK on LF_EDIT (PR_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_EMPDEP_CONN
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_EMPDEP_CONN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_EMPDEP_CONN
(
  USER_ID        NUMBER(38) not null,
  DEP_CODE_THIRD VARCHAR2(64) DEFAULT ''0'' not null,
  CONN_ID        NUMBER(38),
  DEP_ID         NUMBER(38) DEFAULT 0 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_EMPDEP_CONN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EMPDEP_CONN
	  add primary key (USER_ID, DEP_CODE_THIRD)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_EMPLOYEE
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_EMPLOYEE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_EMPLOYEE
(
  EMPLOYEEID    NUMBER(38) not null,
  P_ID          NUMBER(38),
  DEP_ID        NUMBER(38),
  EMPLOYEE_NO   VARCHAR2(64),
  E_STATE       NUMBER(1),
  COMMENTS      VARCHAR2(512),
  REC_STATE     NUMBER(1),
  HIDEPH_STATE  NUMBER(1),
  E_MAIL        VARCHAR2(64),
  MSN           VARCHAR2(64),
  QQ            VARCHAR2(32),
  NAME          VARCHAR2(64),
  SEX           NUMBER(1),
  MOBILE        VARCHAR2(200),
  BIRTHDAY      DATE,
  OPH           VARCHAR2(64),
  GUID          NUMBER(38),
  BIZ_ID        VARCHAR2(64),
  EMPLOYEE_CODE VARCHAR2(64),
  DEP_CODE      VARCHAR2(64),
  LASTUPDDTTM   TIMESTAMP(6),
  HR_STATUS     VARCHAR2(20),
  IS_OPERATOR   NUMBER(2),
  CORP_CODE     VARCHAR2(64),
  DUTIES        VARCHAR2(200),
  FAX           VARCHAR2(64),
  UP_GUID       NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_EMPLOYEE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EMPLOYEE
	  add constraint PK_LF_EMPLOYEE primary key (EMPLOYEEID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_EMPLOYEE') AND T.INDEX_NAME = UPPER('E_GUIDUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EMPLOYEE
	  add constraint E_GUIDUNIQUE unique (GUID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_EMPLOYEE') AND T.INDEX_NAME = UPPER('FK_LF_LIS_RELATIONS_LF_EMP_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_LIS_RELATIONS_LF_EMP_FK on LF_EMPLOYEE (DEP_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_EMPLOYEE') AND T.INDEX_NAME = UPPER('FK_LF_SYS_RELATIONS_LF_POS_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_SYS_RELATIONS_LF_POS_FK on LF_EMPLOYEE (P_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_EMPLOYEE_DEP
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_EMPLOYEE_DEP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_EMPLOYEE_DEP
(
  DEP_ID         NUMBER(38) not null,
  DEP_CODE       VARCHAR2(64),
  DEP_NAME       VARCHAR2(64),
  DEP_PCODE      VARCHAR2(64),
  DEP_EFF_STATUS VARCHAR2(20),
  DEP_LEVEL      NUMBER(2) DEFAULT 0,
  ADD_TYPE       NUMBER(5),
  CORP_CODE      VARCHAR2(64),
  PARENT_ID      NUMBER(38) DEFAULT 0 not null,
  DEP_CODE_THIRD VARCHAR2(64) DEFAULT '''',
  DEP_PATH       VARCHAR2(160) DEFAULT '''',
  UP_DEPID       VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_EMPLOYEE_DEP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EMPLOYEE_DEP
	  add constraint PK_LF_EMPLOYEE_DEP primary key (DEP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_EMPLOYEE_TYPE
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_EMPLOYEE_TYPE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_EMPLOYEE_TYPE
(
  ID        NUMBER(38) not null,
  NAME      VARCHAR2(64) not null,
  USER_ID   NUMBER(38) not null,
  CORP_CODE VARCHAR2(64) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_EMPLOYEE_TYPE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EMPLOYEE_TYPE
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_EMPLOYEE_TYPE') AND T.INDEX_NAME = UPPER('LF_EMPLOYEE_TYPE_UNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EMPLOYEE_TYPE
	  add constraint LF_EMPLOYEE_TYPE_UNIQUE unique (NAME, CORP_CODE)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ENTERPRISE
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ENTERPRISE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ENTERPRISE
(
  OR_ID           NUMBER(38) not null,
  SP_USER         VARCHAR2(32),
  ENTERPRISE_CODE NUMBER(38),
  DEP_CODE        VARCHAR2(64) not null,
  DEP_NAME        VARCHAR2(64) not null,
  DEP_ADDR1       VARCHAR2(64),
  DEP_ADDR2       VARCHAR2(64),
  SUPERIOR_ID     NUMBER,
  DEP_LEVEL       NUMBER(4)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ENTERPRISE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ENTERPRISE
	  add constraint PK_LF_ENTERPRISE primary key (OR_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_EXAMINE_SMS
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_EXAMINE_SMS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_EXAMINE_SMS
(
  ES_ID          NUMBER(38) not null,
  FR_ID          NUMBER(38),
  PHONE          VARCHAR2(30),
  MSGCONTENT     VARCHAR2(2000),
  BATCHNUMBER    VARCHAR2(10),
  DISAGREENUMBER VARCHAR2(10),
  RECIVECONTENT  VARCHAR2(2000),
  RECIVETIME     TIMESTAMP(8),
  SPNUMBER       VARCHAR2(21),
  SPUSER         VARCHAR2(11),
  ES_TYPE        NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_EXAMINE_SMS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_EXAMINE_SMS
	  add constraint PK_LF_EXAMINE_SMS primary key (ES_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table A_RPTRECORD(创建导出记录表)
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='A_RPTRECORD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE A_RPTRECORD(
  ID         NUMBER(38) NOT NULL,
  FILENAME  VARCHAR2(256) DEFAULT '' '' NOT NULL,
  USERID  VARCHAR2(256) DEFAULT '' '' NOT NULL,
  STATUS  VARCHAR2(2) DEFAULT '' '' NOT NULL,
  CREATETIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  GENERATETIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  DOWNLOADTIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL
) TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('A_RPTRECORD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE A_RPTRECORD
	  ADD CONSTRAINT PK_A_RPTRECORD PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 2
	  MAXTRANS 255
	  STORAGE
	  (
		INITIAL 64K
		MINEXTENTS 1
		MAXEXTENTS UNLIMITED
	  )';
	END IF;
END;
/

--
-- Creating table LF_EXTERNAL_USER
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_EXTERNAL_USER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_EXTERNAL_USER
(
  EXUID  NUMBER(38) not null,
  GUID   NUMBER(38) not null,
  NAME   VARCHAR2(64),
  MOBILE VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_FLOW
-- ======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_FLOW';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_FLOW
(
  F_ID           NUMBER(38) not null,
  F_TASK         VARCHAR2(64),
  F_TYPE         NUMBER(4),
  R_LEVELAMOUNT  NUMBER(4),
  USERID         VARCHAR2(64),
  MSG_ACCOUNT    NUMBER(8),
  COMMENTS       VARCHAR2(512),
  CORP_CODE      VARCHAR2(64),
  CREATE_USER_ID NUMBER(38),
  FLOWSHOW       VARCHAR2(400),
  REMIND_STATE   NUMBER(2),
  CREATE_TIME    TIMESTAMP(8),
  UPDATE_TIME    TIMESTAMP(8),
  FLOWSTATE      NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_FLOW') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_FLOW
	  add constraint PK_LF_FLOW primary key (F_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SPFEEALARM(SP费用阀值告警信息表)
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SPFEEALARM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SPFEEALARM
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,   --自增ID
  SPUSER VARCHAR2(11) DEFAULT '' '' NOT NULL ,-- SP账号
  NOTICENAME VARCHAR2(32) DEFAULT '' '' NOT NULL ,-- 通知人姓名
  ALARMPHONE VARCHAR2(32) DEFAULT '' '' NOT NULL ,-- 告警手机号码
  MODI_USERID NUMBER(38) DEFAULT 0 NOT NULL ,-- 最后更新记录的操作员ID
  CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL ,-- 企业编码
  CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL ,-- 创建时间
  UPDATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, -- 更新时间
  ALARMEDCOUNT NUMBER(20) DEFAULT 0 NOT NULL --告警次数
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_FLOWBINDOBJ
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_FLOWBINDOBJ';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_FLOWBINDOBJ
(
  ID          NUMBER(38) not null,
  F_ID        NUMBER(38) not null,
  OBJ_TYPE    NUMBER(2),
  OBJ_CODE    VARCHAR2(64),
  INFO_TYPE   NUMBER(2),
  CORP_CODE   VARCHAR2(64),
  UPDATE_TIME TIMESTAMP(8),
  CREATE_TIME TIMESTAMP(8),
  CTSUBDEP  SMALLINT DEFAULT 1 NOT NULL  --是否包含子机构，0－否；1－是
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_FLOWBINDTYPE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_FLOWBINDTYPE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_FLOWBINDTYPE
(
  ID          NUMBER(38) not null,
  F_ID        NUMBER(38) not null,
  MENUCODE    VARCHAR2(64),
  INFO_TYPE   NUMBER(2),
  UPDATE_TIME TIMESTAMP(8),
  CREATE_TIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_BALANCE_DEF(充值默认数据表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BALANCE_DEF';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_BALANCE_DEF
  (
    ID NUMBER(38) PRIMARY KEY  NOT NULL,   --  自增ID
    DEP_ID NUMBER(38) DEFAULT 0  NOT NULL,         --  机构ID
    SMS_COUNT NUMBER(38) DEFAULT 0 NOT NULL,         --  短信充值
    MMS_COUNT NUMBER(38) DEFAULT 0 NOT NULL,       --  彩信充值
    CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,     --  企业编码
    MODI_USERID NUMBER(38) DEFAULT 0 NOT NULL,       -- 最后一次更新记录操作员ID
    MODI_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,   -- 最后一次更新时间
    CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL  --  创建时间
  )
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_FLOWRECORD(审核流程表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_FLOWRECORD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_FLOWRECORD
(
  FR_ID          NUMBER(38) not null,
  MT_ID          NUMBER(38),
  PRE_RV         NUMBER,
  REVIEWER       NUMBER,
  R_LEVEL        NUMBER(4),
  R_LEVELAMOUNT  NUMBER(4),
  R_CONTENT      VARCHAR2(512),
  R_STATE        NUMBER(1),
  COMMENTS       VARCHAR2(800),
  R_TIME         DATE,
  F_ID           NUMBER(38) not null,
  REVIEW_TYPE    NUMBER(2),
  CORP_CODE      VARCHAR2(64),
  BATCHNUMBER    VARCHAR2(10),
  DISAGREENUMBER VARCHAR2(10),
  INFO_TYPE      NUMBER(2),
  PROUSERCODE    NUMBER(38),
  R_TYPE         NUMBER(2),
  USER_CODE      NUMBER(38),
  R_CONDITION    NUMBER(2),
  IS_COMPLETE    NUMBER(2),
  SUBMITTIME     TIMESTAMP(8),
  ISREMIND NUMBER(2) DEFAULT 0 NOT NULL, --是否已催办（0-未催办；1已催办，默认值为0）
  REMINDTIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL --最近催办时间（默认值为系统当前时间）
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_FLOWRECORD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_FLOWRECORD
	  add constraint PK_LF_FLOWRECORD primary key (FR_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_FLOWRECORD') AND T.INDEX_NAME = UPPER('FK_LF_FLO_RELATIONS_LF_MTT_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_FLO_RELATIONS_LF_MTT_FK on LF_FLOWRECORD (MT_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LL_STATUSRPT
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LL_STATUSRPT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LL_STATUSRPT
(
    ID  NUMBER(11) NOT NULL,
    MOBILE  VARCHAR(16) DEFAULT '' '' NOT NULL,
    PRODUCTID  VARCHAR2(32) DEFAULT '' '' NOT NULL,
    LLRPT      VARCHAR2(2) DEFAULT '' '' NOT NULL,
    ORDERTM    VARCHAR2(16) DEFAULT '' '' NOT NULL,
    ERRCODE    VARCHAR2(16) DEFAULT '' '' NOT NULL,
    ORDERNO    VARCHAR2(64) DEFAULT '' '' NOT NULL,
    CREATETM   TIMESTAMP DEFAULT SYSDATE NOT NULL
)
TABLESPACE EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LL_STATUSRPT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LL_STATUSRPT
	  ADD CONSTRAINT PK_LL_STATUSRPT PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LL_DATA_REPORT
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LL_DATA_REPORT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LL_DATA_REPORT
(
  ID  NUMBER(11) NOT NULL,
  PRO_ID VARCHAR2(16) DEFAULT '' '' NOT NULL,
  USER_ID NUMBER(11) DEFAULT 0 NOT NULL,
  ORG_ID  NUMBER(11) DEFAULT 0 NOT NULL,
  ORDERTM TIMESTAMP DEFAULT SYSDATE NOT NULL,
  SUNMITNUM NUMBER(11) DEFAULT 0 NOT NULL,
  SUCCNUM NUMBER(11) DEFAULT 0 NOT NULL,
  FAILDNUM  NUMBER(11) DEFAULT 0 NOT NULL,
  R1  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  R2  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  R3  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  R4  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  CREATETM TIMESTAMP DEFAULT SYSDATE NOT NULL
)
TABLESPACE EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LL_DATA_REPORT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LL_DATA_REPORT
	  ADD CONSTRAINT PK_LL_DATA_REPORT PRIMARY KEY (PRO_ID,USER_ID,ORDERTM)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_INDUSTRY_USE
-- 行业、用途 管理表
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_INDUSTRY_USE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_INDUSTRY_USE(
   ID NUMBER(11) NOT NULL,--主键
   NAME VARCHAR2(100) DEFAULT '' '' NOT NULL,
   OPERATOR NUMBER(18) DEFAULT 0 NOT NULL,
   TYPE NUMBER(5) DEFAULT 0 NOT NULL,
   CREATETM TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
   UPDATETM TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
   TMPTYPE NUMBER(11) DEFAULT 0 NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_INDUSTRY_USE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_INDUSTRY_USE
	  ADD CONSTRAINT PK_LF_INDUSTRY_USE PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LL_COMP_INFO
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LL_COMP_INFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LL_COMP_INFO
(
  ID NUMBER(11) NOT NULL,
  LLACCOUNT VARCHAR2(64),
  PASSWORD VARCHAR2(64) DEFAULT '' '' NOT NULL,
  ECID NUMBER(11) DEFAULT 0 NOT NULL,
  ECNAME VARCHAR2(32) DEFAULT '' '' NOT NULL,
  IP VARCHAR2(32) DEFAULT '' '' NOT NULL,
  PORT NUMBER(11) DEFAULT 0 NOT NULL,
  REMARK VARCHAR2(1024) DEFAULT '' '' NOT NULL,
  UPDATETM TIMESTAMP DEFAULT SYSDATE NOT NULL,
  CREATETM TIMESTAMP DEFAULT SYSDATE NOT NULL,
  CREATEID NUMBER(11) DEFAULT 0 NOT NULL,
  UPDATEID NUMBER(11) DEFAULT 0 NOT NULL,
  PUSHADDR VARCHAR2(128)
)
 tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LL_COMP_INFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LL_COMP_INFO
	  ADD CONSTRAINT PK_LL_COMP_INFO PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BALANCE_PRI(充值回收权限表)
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BALANCE_PRI';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_BALANCE_PRI
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,   --  自增ID
  USER_ID NUMBER(38) DEFAULT 0 NOT NULL,         --  操作员ID
  DEP_ID NUMBER(38) DEFAULT 0  NOT NULL,         --  机构ID
  TYPE NUMBER(2) DEFAULT 0 NOT NULL,           --  充值权限类型：0-操作员机构充值权限；
  CREATE_USERID NUMBER(38) DEFAULT 0 NOT NULL,     --  创建记录的操作员ID
  CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,     --  企业编码
  CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL   --  创建时间
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LL_PRODUCT
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LL_PRODUCT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LL_PRODUCT
  (
    ID NUMBER(11) NOT NULL,
    ECID NUMBER(11) DEFAULT 0 NOT NULL,
    PRODUCTID VARCHAR2(32) DEFAULT '' '' NOT NULL,
    PRODUCTNAME VARCHAR2(128) DEFAULT '' '' NOT NULL,
    ISP VARCHAR2(16) DEFAULT '' '' NOT NULL,
    VOLUME VARCHAR2(16) DEFAULT '' '' NOT NULL,
    PRICE NUMBER(11,2) DEFAULT 0 NOT NULL,
    DISCPRICE NUMBER(11,2)  DEFAULT 0 NOT NULL,
    AREA VARCHAR2(32) DEFAULT '' '' NOT NULL,
    PTYPE NUMBER(11) DEFAULT 0 NOT NULL,
    PMOLD NUMBER(11) DEFAULT 0 NOT NULL,
    RTYPE NUMBER(11) DEFAULT 0 NOT NULL,
    STATUS NUMBER(11) DEFAULT 0 NOT NULL,
    UPDATETM TIMESTAMP DEFAULT SYSDATE NOT NULL,
    CREATETM TIMESTAMP DEFAULT SYSDATE NOT NULL,
    OPERATORID NUMBER(11) DEFAULT 0 NOT NULL
  )
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LL_PRODUCT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LL_PRODUCT
	  ADD CONSTRAINT PK_LL_PRODUCT PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LL_PRODUCT') AND T.INDEX_NAME = UPPER('INDEX_PRO_PRODUCTID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_PRO_PRODUCTID ON LL_PRODUCT (PRODUCTID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LL_PRODUCT') AND T.INDEX_NAME = UPPER('INDEX_PRO_PRODUCTNAME');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_PRO_PRODUCTNAME ON LL_PRODUCT (PRODUCTNAME)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_GLOBAL_VARIABLE
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_GLOBAL_VARIABLE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_GLOBAL_VARIABLE
(
  GLOBALID       NUMBER(38) not null,
  GLOBALKEY      VARCHAR2(32) not null,
  GLOBALVALUE    NUMBER(38),
  GLOBALSTRVALUE VARCHAR2(2000),
  MEMO VARCHAR2(32) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_GLOBAL_VARIABLE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_GLOBAL_VARIABLE
	  add constraint PK_LF_GLOBAL_VARIABLE primary key (GLOBALID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_GLOBAL_VARIABLE') AND T.INDEX_NAME = UPPER('UQ_GKEY');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_GLOBAL_VARIABLE
	  ADD CONSTRAINT UQ_GKEY UNIQUE (GLOBALKEY)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PRIVILEGE
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PRIVILEGE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PRIVILEGE
(
  PRIVILEGE_ID NUMBER(38) not null,
  RESOURCE_ID  NUMBER(38),
  OPERATE_ID   NUMBER(38),
  COMMENTS     VARCHAR2(512),
  PRIV_CODE    VARCHAR2(64),
  MENUNAME     VARCHAR2(64),
  MODNAME      VARCHAR2(64),
  MENUCODE     VARCHAR2(64),
  MENUSITE     VARCHAR2(2048),
  ZH_TW_COMMENTS VARCHAR2(512),
  ZH_HK_COMMENTS VARCHAR2(512),
  ZH_TW_MENUNAME VARCHAR2(64),
  ZH_HK_MENUNAME VARCHAR2(64),
  ZH_TW_MODNAME VARCHAR2(64),
  ZH_HK_MODNAME VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PRIVILEGE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PRIVILEGE
	  add constraint PK_LF_PRIVILEGE primary key (PRIVILEGE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PRIVILEGE') AND T.INDEX_NAME = UPPER('FK_LF_PRI_RELATIONS_LF_OPE_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_PRI_RELATIONS_LF_OPE_FK on LF_PRIVILEGE (OPERATE_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PRIVILEGE') AND T.INDEX_NAME = UPPER('FK_LF_PRI_RELATIONS_LF_RES_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_PRI_RELATIONS_LF_RES_FK on LF_PRIVILEGE (RESOURCE_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ROLES
-- =======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ROLES';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ROLES
(
  ROLE_ID     NUMBER(38) not null,
  ROLE_NAME   VARCHAR2(64),
  COMMENTS    VARCHAR2(512),
  CORP_CODE   VARCHAR2(64),
  CREATE_GUID NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ROLES') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ROLES
	  add constraint PK_LF_ROLES primary key (ROLE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IMPOWER
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IMPOWER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IMPOWER
(
  ROLE_ID      NUMBER(38) not null,
  PRIVILEGE_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_IMPOWER
  add constraint FK_LF_IMPOW_LF_IMPOWE_LF_PRIVI foreign key (PRIVILEGE_ID)
  references LF_PRIVILEGE (PRIVILEGE_ID)';  
  EXECUTE IMMEDIATE 'alter table LF_IMPOWER
  add constraint FK_LF_IMPOW_LF_IMPOWE_LF_ROLES foreign key (ROLE_ID)
  references LF_ROLES (ROLE_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IMPOWER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IMPOWER
	  add constraint PK_LF_IMPOWER primary key (ROLE_ID, PRIVILEGE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_IMPOWER') AND T.INDEX_NAME = UPPER('LF_IMPOWER_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_IMPOWER_FK on LF_IMPOWER (ROLE_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/
  
DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_IMPOWER') AND T.INDEX_NAME = UPPER('LF_IMPOWER_FK2');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_IMPOWER_FK2 on LF_IMPOWER (PRIVILEGE_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_CLIENT
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_CLIENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_CLIENT
(
  CLIENTID       NUMBER(38) not null,
  MOBILE         VARCHAR2(50) not null,
  OWNER_USERID   NUMBER(38),
  GROUPID        NUMBER(38),
  USERTYPE       NUMBER,
  NAME           VARCHAR2(50) not null,
  EMAIL          VARCHAR2(50),
  SEX            NUMBER,
  AGE            NUMBER,
  DEPID          NUMBER(38),
  OWNSHOP        VARCHAR2(50),
  GOODS          VARCHAR2(50),
  MONETARYVALUE  VARCHAR2(50),
  CUSTOMERSOURCE VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_CLIENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_CLIENT
	  add primary key (CLIENTID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_GROUP
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_GROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_GROUP
(
  GROUPID     NUMBER(38) not null,
  OWNERUSERID NUMBER(38) not null,
  GROUPNAME   VARCHAR2(50) not null,
  GP_TYPE     NUMBER(2),
  MAX_NUMBERS NUMBER(12)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_GROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_GROUP
	  add primary key (GROUPID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_CLIENTGROUP
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_CLIENTGROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_CLIENTGROUP
(
  MEMBERID   NUMBER(38) not null,
  GROUPID    NUMBER(38) not null,
  CLIENTID   NUMBER(38) not null,
  MEMBERTYPE NUMBER(1) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_IM_CLIENTGROUP
  add constraint FK_CGP_FK_LF_IM_CLIENT foreign key (CLIENTID)
  references LF_IM_CLIENT (CLIENTID)';
  EXECUTE IMMEDIATE 'alter table LF_IM_CLIENTGROUP
  add constraint FK_CGP_FK_LF_IM_GROUP foreign key (GROUPID)
  references LF_IM_GROUP (GROUPID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_CLIENTGROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_CLIENTGROUP
	  add primary key (MEMBERID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_DIALOG
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_DIALOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_DIALOG
(
  DIAID    NUMBER(38) not null,
  DIALOGID VARCHAR2(100) not null,
  SUDID    NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_DIALOG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_DIALOG
	  add constraint PK_LF_IM_DIALOG primary key (DIAID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LL_ORDER_TASK
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LL_ORDER_TASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LL_ORDER_TASK
(
  ID NUMBER(11) NOT NULL,
  TASKID NUMBER(38) DEFAULT 0 NOT NULL,
  ORDERNO VARCHAR2(36) DEFAULT '' '' NOT NULL,
  ECID VARCHAR2(16) DEFAULT '' '' NOT NULL,
  USER_ID NUMBER(11) DEFAULT 0 NOT NULL,
  ORG_ID NUMBER(11) DEFAULT 0 NOT NULL,
  TOPIC VARCHAR2(64) DEFAULT '' '' NOT NULL,
  PRO_IDS VARCHAR2(32) DEFAULT '' '' NOT NULL,
  MSGTYPE CHAR(1) DEFAULT ''1'' NOT NULL,
  TEMP_ID NUMBER(11) DEFAULT 0 NOT NULL,
  SP_USER VARCHAR2(32) DEFAULT '' '' NOT NULL,
  SP_PWD VARCHAR2(32) DEFAULT '' '' NOT NULL,
  SUBCOUNT NUMBER(11) DEFAULT 0 NOT NULL,
  EFFCOUNT NUMBER(11) DEFAULT 0 NOT NULL,
  SUCCOUNT NUMBER(11) DEFAULT 0 NOT NULL,
  FAICOUNT NUMBER(11) DEFAULT 0 NOT NULL,
  TIMER_STATUS CHAR(1) DEFAULT ''1'' NOT NULL,
  TIMER_TIME TIMESTAMP DEFAULT TO_DATE(''1900-1-1 00:00:00'',''YYYY-MM-DD HH24:MI:SS'') NOT NULL,
  RE_STATUS CHAR(1) DEFAULT ''1'' NOT NULL,
  ORDERSTATUS VARCHAR2(2) DEFAULT ''1'' NOT NULL,
  SMSSTATUS VARCHAR2(2) DEFAULT ''1'' NOT NULL,
  ISRETRY CHAR(1) DEFAULT ''1'' NOT NULL,
  SUBMITTM TIMESTAMP DEFAULT SYSDATE NOT NULL,
  ORDERTM TIMESTAMP DEFAULT SYSDATE NOT NULL,
  UPDATETM TIMESTAMP DEFAULT SYSDATE NOT NULL,
  CREATETM TIMESTAMP DEFAULT SYSDATE NOT NULL,
  SUMMONEY NUMBER(12,2) DEFAULT 0 NOT NULL,
  REPORTDATE VARCHAR2(16) DEFAULT '' '' NOT NULL,
  P_IDS VARCHAR2(64) DEFAULT '' '' NOT NULL,
  SERVERNUM VARCHAR2(32) DEFAULT ''0'' NOT NULL,
  MSG VARCHAR2(2048) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LL_ORDER_TASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LL_ORDER_TASK
	  ADD CONSTRAINT PK_LL_ORDER_TASK PRIMARY KEY (ID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LL_ORDER_TASK') AND T.INDEX_NAME = UPPER('INDEX_TASK_USER_ID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_TASK_USER_ID ON LL_ORDER_TASK (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LL_ORDER_TASK') AND T.INDEX_NAME = UPPER('INDEX_TASK_ORDERTM');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_TASK_ORDERTM ON LL_ORDER_TASK (ORDERTM)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/
  
DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LL_ORDER_TASK') AND T.INDEX_NAME = UPPER('INDEX_TASK_ORG_ID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_TASK_ORG_ID ON LL_ORDER_TASK (ORG_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/
  
--
-- Creating table LF_IM_GPUSERLINK
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_GPUSERLINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_GPUSERLINK
(
  LINK_ID   NUMBER(38) not null,
  GROUPID   NUMBER(38),
  USERID    NUMBER(38),
  USER_TYPE NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_GPUSERLINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_GPUSERLINK
	  add constraint PK_LF_IM_GPUSERLINK primary key (LINK_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_GROUPMESSAGE
-- =================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_GROUPMESSAGE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_GROUPMESSAGE
(
  MSGID             NUMBER(38) not null,
  SENDERID          NUMBER(38),
  CONTENT           VARCHAR2(2048),
  GROUPID           NUMBER(38),
  DIALOGID          VARCHAR2(100),
  M_RECEIVERIDS     VARCHAR2(2000),
  PC_RECEIVERIDS    VARCHAR2(2000),
  M_SUCCRECEIVERIDS VARCHAR2(2048),
  SEND_DATE         TIMESTAMP(8),
  MSG_TYPE          NUMBER(2),
  MO_OR_MT          NUMBER(1),
  MODULES           NUMBER(1),
  TASK_ID           NUMBER(38),
  ONLINE_IDS        VARCHAR2(2048),
  OFFLINE_IDS       VARCHAR2(2048),
  R_ONLINE_IDS      VARCHAR2(2048),
  STYLE             VARCHAR2(200),
  SERIAL_NUM        VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_GROUPMESSAGE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_GROUPMESSAGE
	  add constraint PK_LF_IM_GROUPMESSAGE primary key (MSGID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_HISTORY
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_HISTORY';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_HISTORY
(
  HISTORYID     NUMBER(38) not null,
  USERID        NUMBER(38) not null,
  PHONE         VARCHAR2(50),
  REGTIME       TIMESTAMP(6) DEFAULT SYSDATE,
  MESSAGE       VARCHAR2(1000),
  GROUPID       NUMBER(38),
  HASREAD       NUMBER not null,
  ISSEND        NUMBER not null,
  ISSENDTOGROUP NUMBER not null,
  CLIENTID      NUMBER(38) not null,
  USERTYPE      NUMBER,
  RECUSERTYPE   NUMBER,
  DAYS          CHAR(4),
  MSGTYPE       NUMBER,
  CHATTYPES     NUMBER(3) not null,
  TASKID        NUMBER(38),
  ISAPPEAR      NUMBER(1) DEFAULT 1 not null,
  DIALOGID      VARCHAR2(64) not null,
  SERIALNUMBER  VARCHAR2(64),
  PTMSGID       NUMBER(19),
  ERRORCODE     VARCHAR2(7)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_HISTORY') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_HISTORY
	  add primary key (HISTORYID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_OFFICEGROUP
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_OFFICEGROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_OFFICEGROUP
(
  GROUPID     NUMBER(38) not null,
  OWNERUSERID NUMBER(38) not null,
  GROUPNAME   VARCHAR2(50) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_OFFICEGROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_OFFICEGROUP
	  add primary key (GROUPID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_MEMBERGROUP
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_MEMBERGROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_MEMBERGROUP
(
  MEMBERID   NUMBER(38) not null,
  GROUPID    NUMBER(38) not null,
  USERID     NUMBER(38),
  EMPLOYEEID NUMBER(38),
  MEMBERTYPE NUMBER(1) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_IM_MEMBERGROUP
  add constraint FK_LF_MGP_FK_LF_IM_OFGP foreign key (GROUPID)
  references LF_IM_OFFICEGROUP (GROUPID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_MEMBERGROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_MEMBERGROUP
	  add primary key (MEMBERID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_RECENTCONTACTS
-- ===================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_RECENTCONTACTS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_RECENTCONTACTS
(
  RC_ID         NUMBER(38) not null,
  SENDER_ID     VARCHAR2(64) not null,
  RECEIVER_ID   VARCHAR2(64) not null,
  SEND_DATE     TIMESTAMP(6),
  SENDER_NAME   VARCHAR2(64),
  RECEIVER_NAME VARCHAR2(64),
  DEP_CODE      VARCHAR2(64),
  SOURCE_TYPE   VARCHAR2(64),
  SEND_STATE    NUMBER(1),
  RECEIVE_TYPE  NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_RECENTCONTACTS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_RECENTCONTACTS
	  add constraint PK_LF_IM_RECENTCONTACTS primary key (RC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_SEATGROUP
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_SEATGROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_SEATGROUP
(
  SEATGROUPID   NUMBER(38) not null,
  SEATGROUPNAME VARCHAR2(50) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_SEATGROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_SEATGROUP
	  add primary key (SEATGROUPID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_SEATLIMIT
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_SEATLIMIT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_SEATLIMIT
(
  ID           NUMBER(38) not null,
  USERID       NUMBER(38) not null,
  SMLIMITCOUNT NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_SEATLIMIT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_SEATLIMIT
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_SEATUSER
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_SEATUSER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_SEATUSER
(
  ID          NUMBER(38) not null,
  USERID      NUMBER(38) not null,
  SEATGROUPID NUMBER(38) not null,
  MAX_COUNT   NUMBER(20)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_IM_SEATUSER
  add constraint FK_LF_SU_FK_LF_IM_SG foreign key (SEATGROUPID)
  references LF_IM_SEATGROUP (SEATGROUPID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_SEATUSER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_SEATUSER
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_SEAT_CGLINK
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_SEAT_CGLINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_SEAT_CGLINK
(
  CGLINK_ID NUMBER(38) not null,
  CLIENT_ID NUMBER(38),
  GROUP_ID  NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_SEAT_CGLINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_SEAT_CGLINK
	  add constraint PK_LF_IM_SEAT_CGLINK primary key (CGLINK_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_SEAT_CLIENT
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_SEAT_CLIENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_SEAT_CLIENT
(
  CLIENT_ID NUMBER(38) not null,
  NAME      VARCHAR2(64),
  SEX       NUMBER(1),
  AGE       NUMBER(2),
  SOURCES   VARCHAR2(50),
  PHONE     VARCHAR2(32),
  GUID      NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_SEAT_CLIENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_SEAT_CLIENT
	  add constraint PK_LF_IM_SEAT_CLIENT primary key (CLIENT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_SEAT_CLIENTGP
-- ==================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_SEAT_CLIENTGP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_SEAT_CLIENTGP
(
  CGID   NUMBER(38) not null,
  NAME   VARCHAR2(64),
  REMARK VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_SEAT_CLIENTGP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_SEAT_CLIENTGP
	  add constraint PK_LF_IM_SEAT_CLIENTGP primary key (CGID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_SHARECLIENT
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_SHARECLIENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_SHARECLIENT
(
  USERID   NUMBER(38) not null,
  CLIENTID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_IM_SHARECLIENT
  add constraint FK_LF_SC_FK_LF_IM_CLIENT foreign key (CLIENTID)
  references LF_IM_CLIENT (CLIENTID)';
  EXECUTE IMMEDIATE 'alter table LF_IM_SHARECLIENT
  add constraint FK_LF_SC_FK_LF_SYSUSER foreign key (USERID)
  references LF_SYSUSER (USER_ID)';
  END IF;
END;
/

--
-- Creating table LF_IM_USERMESSAGE
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_USERMESSAGE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_USERMESSAGE
(
  MSGID       NUMBER(38) not null,
  SENDERID    NUMBER(38),
  CONTENT     VARCHAR2(2048),
  HASREAD     NUMBER(2),
  RECEIVERID  NUMBER(38),
  DIALOGID    VARCHAR2(50),
  RECEIVETYPE VARCHAR2(64),
  SENDDATE    TIMESTAMP(8),
  SEND_TYPE   NUMBER(2),
  MO_OR_MT    NUMBER(1),
  MODULES     NUMBER(1),
  TASK_ID     NUMBER(38),
  SEND_STATE  NUMBER(1),
  STYLE       VARCHAR2(200),
  SERIAL_NUM  VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_USERMESSAGE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_USERMESSAGE
	  add constraint PK_LF_IM_USERMESSAGE primary key (MSGID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_USEDSUBNO
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_USEDSUBNO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_USEDSUBNO
(
  SUBNO_ID   NUMBER(38) not null,
  SPUSERID   CHAR(6),
  SPGATE     VARCHAR2(21),
  SPISUNCM   NUMBER(2),
  SUBNO      VARCHAR2(20),
  APPFLAG    NUMBER,
  USER_ID    NUMBER(38),
  COMMENTS   VARCHAR2(512),
  SPNUMBER   VARCHAR2(21),
  SUBNOSTATE NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_USEDSUBNO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_USEDSUBNO
	  add constraint PK_LF_USEDSUBNO primary key (SUBNO_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_IM_USERSUBNO
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_IM_USERSUBNO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_IM_USERSUBNO
(
  IMSUBNO_ID NUMBER(38) not null,
  SUBNO_ID   NUMBER(38) not null,
  USER_ID    NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_IM_USERSUBNO
  add constraint FK_IM_USERSUBNO_FK_SYSUSER foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  EXECUTE IMMEDIATE 'alter table LF_IM_USERSUBNO
  add constraint FK_IM_USERSUBNO_FK_USEDSUBNO foreign key (SUBNO_ID)
  references LF_USEDSUBNO (SUBNO_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_IM_USERSUBNO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_IM_USERSUBNO
	  add constraint PK_LF_IM_USERSUBNO primary key (IMSUBNO_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_KEYWORDS
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_KEYWORDS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_KEYWORDS
(
  KW_ID     NUMBER(38) not null,
  KEYWOED   VARCHAR2(64),
  KW_LEVEL  NUMBER(4),
  KW_STATE  NUMBER(1),
  COMMENTS  VARCHAR2(512),
  CORP_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_KEYWORDS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_KEYWORDS
	  add constraint PK_LF_KEYWORDS primary key (KW_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_KEY_VALUE
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_KEY_VALUE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_KEY_VALUE
(
  KV_ID    NUMBER(38) not null,
  KV_KEY   VARCHAR2(64),
  KV_VALUE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_SUMCTRL(汇总定时集群控制表)
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SUMCTRL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SUMCTRL(
  ID NUMBER(38) PRIMARY KEY NOT NULL,  --ID，自增
  NODEID VARCHAR2(24) DEFAULT '' '' NOT NULL,  --节点ID
  ISMAIN SMALLINT DEFAULT 0 NOT NULL,  --是否主控制记录
  SUMTYPE SMALLINT DEFAULT 0 NOT NULL,  --0代表晚上汇总，1代表白天汇总
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL --更新时间
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_NODEBASEINFO(节点基础信息表)
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_NODEBASEINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_NODEBASEINFO(
  NODEID VARCHAR2(24) PRIMARY KEY NOT NULL ,	--节点ID
  SERVERIP VARCHAR2(160) DEFAULT '' '' NOT NULL,      --主机的IP
  SERVERPORT NUMBER(11) DEFAULT 0 NOT NULL,           --主机端口
  SERLOCALURL VARCHAR2(256) DEFAULT '' '' NOT NULL,      --局域网访问地址
  SERINTERNETURL VARCHAR2(256) DEFAULT '' '' NOT NULL,         --外网访问地址
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL   --更新时间
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_TIMESERSTATE(定时服务状态表)
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TIMESERSTATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_TIMESERSTATE(
  TIMESERVERID VARCHAR2(64) PRIMARY KEY NOT NULL ,	--定时服务ID
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,   --更新时间
  DEALSTATE NUMBER(6) DEFAULT 0 NOT NULL,             --处理状态
  NODEID VARCHAR2(24) DEFAULT '' '' NOT NULL,         --节点ID
  SERVERIP VARCHAR2(160) DEFAULT '' '' NOT NULL,      --定时服务主机的IP
  SERVERPORT NUMBER(11) DEFAULT 0 NOT NULL,           --定时服务主机端口
  SERVERURL VARCHAR2(256) DEFAULT '' '' NOT NULL      --访问地址
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_TIMESERCTRL(定时服务控制表)
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TIMESERCTRL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_TIMESERCTRL(
  TIMESERVERID VARCHAR2(64) PRIMARY KEY NOT NULL ,	--定时服务ID
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,   --更新时间
  NODEID VARCHAR2(24) DEFAULT '' '' NOT NULL,         --节点ID
  SERVERIP VARCHAR2(160) DEFAULT '' '' NOT NULL,      --定时服务主机的IP
  SERVERPORT NUMBER(11) DEFAULT 0 NOT NULL            --定时服务主机端口
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_SPOFFCTRL(SP离线时间段告警控制表)
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SPOFFCTRL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SPOFFCTRL(
  ID NUMBER(38) PRIMARY KEY NOT NULL,  --ID，自增
  SPACCOUNTID VARCHAR2(11) DEFAULT '' '' NOT NULL,  --SP账号
  MONOFFLINEPRD VARCHAR2(32) DEFAULT '' '' NOT NULL,        --开始时间段(小时),结束时间段(小时),时长
  ALARMTIME VARCHAR2(32) DEFAULT ''0'' NOT NULL,  --告警时间
  ALARMFLAG VARCHAR2(32) DEFAULT ''0'' NOT NULL,  --告警标识
  CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --创建时间
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --更新时间
  CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,  --企业编码
  ALARMEMAILFLAG VARCHAR2(32) DEFAULT ''0'' NOT NULL --邮件状态
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table PB_BLACK_DEL(短信黑名单删除记录表)
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='PB_BLACK_DEL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE PB_BLACK_DEL(
  ID         NUMBER(11) NOT NULL,
  OPERATEID  VARCHAR2(21) DEFAULT '' '' NOT NULL,
  PHONE  NUMBER(20) DEFAULT 0 NOT NULL,
  OPTTIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  TASKID VARCHAR2(40) DEFAULT '' '' NOT NULL
) TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('PB_BLACK_DEL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE PB_BLACK_DEL
	  ADD CONSTRAINT PK_PB_BLACK_DEL PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 2
	  MAXTRANS 255
	  STORAGE
	  (
		INITIAL 64K
		MINEXTENTS 1
		MAXEXTENTS UNLIMITED
	  )';
	END IF;
END;
/

--
-- Creating table LF_UDGROUP
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_UDGROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_UDGROUP
(
  UDG_ID       NUMBER(38) not null,
  UDG_NAME     VARCHAR2(64),
  USER_ID      NUMBER(38) not null,
  GROUP_TYPE   NUMBER(1) DEFAULT 2,
  GP_ATTRIBUTE NUMBER(1) DEFAULT 0,
  SEND_MODE    NUMBER(1) DEFAULT 1 not null,
  SHARE_TYPE   NUMBER(1),
  GROUP_ID     NUMBER(38),
  SHARE_STATUS NUMBER(6) DEFAULT 0 NOT NULL,
  CREATETIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  RECEIVER     NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_UDGROUP
  add constraint FK_LF_UDGRO_FK_LF_SYS_LF_SYS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_UDGROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_UDGROUP
	  add constraint PK_LF_UDGROUP primary key (UDG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_LIST2GRO
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_LIST2GRO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_LIST2GRO
(
  L2G_ID       NUMBER(38) not null,
  L2G_TYPE     NUMBER(4),
  UDG_ID       NUMBER(38),
  GUID         NUMBER(38),
  RECEIVE_FLAG NUMBER(1) DEFAULT 0,
  SHARE_TYPE   NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_LIST2GRO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_LIST2GRO
	  add constraint PK_LF_LIST2GRO primary key (L2G_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

  --需要删除该外键
/*
  alter table LF_LIST2GRO
  add constraint FK_LF_UDG_RELATIONS_LF_LIS foreign key (UDG_ID)
  references LF_UDGROUP (UDG_ID);
*/

--
-- Creating table LF_MACIP
-- =======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MACIP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MACIP
(
  LMIID       NUMBER(38) not null,
  GUID        NUMBER(38),
  MAC_ADDR    VARCHAR2(200),
  IP_ADDR     VARCHAR2(200),
  TYPE        NUMBER(4),
  CREATORNAME VARCHAR2(64),
  CREATTIME   DATE,
  DT_PWD      NUMBER(1) DEFAULT 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_MALIST
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MALIST';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MALIST
(
  MA_ID     NUMBER(38) not null,
  NAME      VARCHAR2(64),
  SEX       NUMBER(1),
  BIRTHDAY  DATE,
  MOBILE    VARCHAR2(64),
  OPH       VARCHAR2(64),
  QQ        VARCHAR2(32),
  E_MAIL    VARCHAR2(64),
  MSN       VARCHAR2(64),
  DEP_ID    NUMBER(38),
  USER_ID   NUMBER(38),
  GUID      NUMBER(38),
  CORP_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MALIST') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MALIST
	  add constraint PK_LF_MALIST primary key (MA_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MAPPING
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MAPPING';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MAPPING
(
  MAP_ID   NUMBER(38) not null,
  GUID     NUMBER(38),
  MAP_GUID NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MAPPING') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MAPPING
	  add constraint PK_LF_MAPPING primary key (MAP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MATERIAL_SORT
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MATERIAL_SORT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MATERIAL_SORT
(
  SORT_ID     NUMBER(38) not null,
  SORT_NAME   VARCHAR2(64) not null,
  PARENT_CODE VARCHAR2(64) not null,
  CHILD_CODE  VARCHAR2(64) not null,
  SORT_LEVEL  NUMBER(38) not null,
  CORP_CODE   VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MATERIAL_SORT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MATERIAL_SORT
	  add constraint PK_LF_MATERIAL_SORT primary key (SORT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MATERIAL
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MATERIAL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MATERIAL
(
  MTAL_ID      NUMBER(38) not null,
  MTAL_NAME    VARCHAR2(64) not null,
  MTAL_TYPE    VARCHAR2(32) not null,
  MTAL_SIZE    VARCHAR2(20),
  USER_ID      NUMBER(38),
  MTAL_UPTIME  TIMESTAMP(8) DEFAULT sysdate,
  COMMENTS     VARCHAR2(2000),
  MTAL_ADDRESS VARCHAR2(200) not null,
  SORT_ID      NUMBER(38) not null,
  MTAL_WIDTH   NUMBER(5),
  MTAL_HEIGHT  NUMBER(5),
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_MATERIAL
  add constraint FK_MATERIAL_LF_MATERIAL_SORT foreign key (SORT_ID)
  references LF_MATERIAL_SORT (SORT_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MATERIAL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MATERIAL
	  add constraint PK_LF_MATERIAL primary key (MTAL_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MMSACCBIND
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMSACCBIND';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMSACCBIND
(
  ID          NUMBER(38) not null,
  MMS_USER    VARCHAR2(32),
  USER_ID     NUMBER(38),
  CORP_CODE   VARCHAR2(64),
  IS_VALIDATE NUMBER(1),
  CREATETIME  DATE DEFAULT sysdate,
  DESCRIPTION VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MMSACCBIND') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MMSACCBIND
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MMSACCBIND') AND T.INDEX_NAME = UPPER('IND_MMSACCBIND_CODE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index IND_MMSACCBIND_CODE on LF_MMSACCBIND (CORP_CODE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MMSACCBIND') AND T.INDEX_NAME = UPPER('IND_MMSACCBIND_USER_ID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index IND_MMSACCBIND_USER_ID on LF_MMSACCBIND (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MMSACCMANAGEMENT
-- ==================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMSACCMANAGEMENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMSACCMANAGEMENT
(
  ID                NUMBER(38) not null,
  MMS_USERID        VARCHAR2(32),
  MMS_NAME          VARCHAR2(64),
  MMS_PASSWORD      VARCHAR2(32),
  MMS_CREATETIME    DATE DEFAULT sysdate,
  MMS_CREATEER_GUID NUMBER(38),
  DESCRIPTION       VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MMSACCMANAGEMENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MMSACCMANAGEMENT
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MMSACCMANAGEMENT') AND T.INDEX_NAME = UPPER('IND_MMSACCMANAGEMENT_USERID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index IND_MMSACCMANAGEMENT_USERID on LF_MMSACCMANAGEMENT (MMS_USERID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MMSBLIST
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMSBLIST';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMSBLIST
(
  BL_ID     NUMBER(38) not null,
  PHONE     VARCHAR2(64),
  SPGATE    VARCHAR2(30),
  BL_STATE  NUMBER(1),
  COMMENTS  VARCHAR2(512),
  USERID    VARCHAR2(6),
  SPNUMBER  VARCHAR2(30),
  OPTIME    VARCHAR2(6),
  SPISUNCM  NUMBER(2),
  BUS_CODE  VARCHAR2(64),
  CORP_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_MMSINFO
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMSINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMSINFO
(
  MMSID         NUMBER(38) not null,
  MARKED        VARCHAR2(100) not null,
  FRAMECONTEXT  VARCHAR2(2000),
  MMSTITLE      VARCHAR2(200) not null,
  FILEDIRECTORY VARCHAR2(200) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MMSINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MMSINFO
	  add primary key (MMSID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 16K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_DBSTATE(数据库与程序监控信息表)
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DBSTATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_DBSTATE
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,      --否  自增ID,全局唯一
  DBID NUMBER(38) DEFAULT  0 NOT NULL,       --数据库ID
  DBNAME VARCHAR2(64) DEFAULT '' ''  NOT NULL,--  数据库名称
  PROCENAME VARCHAR2(64) DEFAULT '' ''  NOT NULL,--  程序名称
  PROCENODE NUMBER(38) DEFAULT 0  NOT NULL,--  程序节点
  PROCETYPE NUMBER(20) DEFAULT 0 NOT NULL,--  5000:EMP系统5200：EMP网关WBS5300：EMP网关SPGATE5900:其他应用系统
  CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--  创建时间
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE  NOT NULL,--  记录更新时间
  MONSTATUS NUMBER(2) DEFAULT 1 NOT NULL,--  监控状态：0：未监控1：监控
  EVTTYPE NUMBER(2) DEFAULT 0 NOT NULL,--  0：正常1：告警2：严重
  DBCONNECTSTATE NUMBER(20) DEFAULT 0  NOT NULL,--  0：正常；1：断开
  ADDOPR NUMBER(20) DEFAULT 0  NOT NULL,--  0：正常；1：失败
  DELOPR NUMBER(20) DEFAULT 0  NOT NULL,--  0：正常；1：失败
  MODIOPR NUMBER(20) DEFAULT 0  NOT NULL,--  0：正常；1：失败
  DISPOPR NUMBER(20) DEFAULT 0  NOT NULL,--  0：正常；1：失败
  DBADDOPRDES VARCHAR2(64) DEFAULT '' '' NOT NULL, --新增操作状态描述
  DBDELOPRDES VARCHAR2(64) DEFAULT '' '' NOT NULL, -- 删除操作状态描述
  DBMODIOPRDES VARCHAR2(64) DEFAULT '' '' NOT NULL,--修改操作状态描述
  DBDISPOPRDES VARCHAR2(64) DEFAULT '' '' NOT NULL,--查询操作状态描述
  SMSALFLAG1 NUMBER(38) DEFAULT 0  NOT NULL,--  程序与连接状态短信告警状态记录
  SMSALFLAG2 NUMBER(38) DEFAULT 0  NOT NULL,--  新增操作状态短信告警状态记录
  SMSALFLAG3 NUMBER(38) DEFAULT 0  NOT NULL,--  删除操作状态短信告警状态记录
  SMSALFLAG4 NUMBER(38) DEFAULT 0  NOT NULL,--  修改操作状态短信告警状态记录
  SMSALFLAG5 NUMBER(38) DEFAULT 0  NOT NULL,--  查询操作状态短信告警状态记录
  MAILALFLAG1 NUMBER(38) DEFAULT 0  NOT NULL,--  程序与连接状态邮件告警状态记录
  MAILALFLAG2 NUMBER(38) DEFAULT 0  NOT NULL,--  新增操作状态邮件告警状态记录
  MAILALFLAG3 NUMBER(38) DEFAULT 0  NOT NULL,--  删除操作状态邮件告警状态记录
  MAILALFLAG4 NUMBER(38) DEFAULT 0  NOT NULL,--  修改操作状态邮件告警状态记录
  MAILALFLAG5 NUMBER(38) DEFAULT 0  NOT NULL,    --  查询操作状态邮件告警状态记录
  SERVERNUM  VARCHAR2(8) DEFAULT ''0'' NOT NULL,   --数据分析服务器节点
  DBSERVTIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MON_DBSTATE') AND T.INDEX_NAME = UPPER('UQ_PNODEPTYPE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_MON_DBSTATE
	  ADD CONSTRAINT UQ_PNODEPTYPE UNIQUE (PROCENODE,PROCETYPE)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_BUSDATA(业务监控数据信息表)
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_BUSDATA';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_BUSDATA
(
    ID NUMBER(38) PRIMARY KEY NOT NULL,  --自增ID
    BUSBASE_ID NUMBER(38) DEFAULT 0 NOT NULL,      --业务ID 对应LF_MON_BUSBASE.ID
    BEGIN_HOUR NUMBER(20) DEFAULT 0 NOT NULL,     --开始时间
    END_HOUR NUMBER(20) DEFAULT 0 NOT NULL,       --结束时间
    MTHAVESND NUMBER(20) DEFAULT 0  NOT NULL,        --MT已发告警值
    DEVIAT_HIGH  NUMBER(20) DEFAULT 0 NOT NULL,        --偏离率(高)
    DEVIAT_LOW NUMBER(20) DEFAULT 0 NOT NULL,        --偏离率(低)
    MON_LASTTIME NUMBER(20) DEFAULT 20000101 NOT NULL,--最后一次告警时间YYYYMMDD
    CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    --企业编码
    CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--创建时间
    MODI_USERID  NUMBER(38) DEFAULT 0 NOT NULL        --最后更新记录的操作员ID
)TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MON_BUSBASE(业务监控基础信息表)
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_BUSBASE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_BUSBASE
(
    ID NUMBER(38) PRIMARY KEY NOT NULL,  --自增ID
    BUS_NAME VARCHAR2(32) DEFAULT '' ''  NOT NULL,    --业务名称
    BUS_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    --业务编码
    AREA_CODE VARCHAR2(512) DEFAULT '' '' NOT NULL,    --区域 多个区域以,隔开
    MONPHONE VARCHAR2(256) DEFAULT '' '' NOT NULL,    --告警短信发送手机号
    BEGIN_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,  --监控开始时间
    END_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,  --监控结果时间
    MON_STATE NUMBER(20) DEFAULT 1 NOT NULL,        --监控状态0：关闭1：启用
    MODI_USERID NUMBER(38) DEFAULT 0 NOT NULL,     --  最后更新记录的操作员ID
    CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    --企业编码
    CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--创建时间
    UPDATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,  --更新时间
    MONEMAIL VARCHAR2(256) DEFAULT '' '' NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MMSMTTASK
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMSMTTASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMSMTTASK
(
  MMS_ID      NUMBER(38) not null,
  USER_ID     NUMBER(38),
  PHONE       VARCHAR2(64),
  MMS_MSG     VARCHAR2(200),
  SEND_STATUS NUMBER(2),
  SEND_TIME   TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MMSMTTASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MMSMTTASK
	  add constraint PK_LF_MMSMTTASK primary key (MMS_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MMSTASKREPORT
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMSTASKREPORT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMSTASKREPORT
(
  R_TASKID NUMBER,
  R_ICOUNT NUMBER,
  R_SUCC   NUMBER,
  R_FAIL1  NUMBER,
  R_FAIL2  NUMBER,
  R_NRET   NUMBER
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_MMSTEMPLATE
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMSTEMPLATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMSTEMPLATE
(
  MMS_ID     NUMBER(38) not null,
  USER_ID    NUMBER(38),
  MMS_THEME  VARCHAR2(64),
  MMS_MSG    VARCHAR2(1024),
  DSFLAG     NUMBER(1),
  MMS_STATE  NUMBER(1),
  ADDTIME    DATE,
  UP_PIC     VARCHAR2(2000),
  UP_SOUND   VARCHAR2(2000),
  PIC_INFO   VARCHAR2(100),
  SOUND_INFO VARCHAR2(100)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MMSTEMPLATE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MMSTEMPLATE
	  add constraint PK_LF_MMSTEMPLATE primary key (MMS_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MMSTEMPLATE') AND T.INDEX_NAME = UPPER('LF_MMSADDTEMP_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_MMSADDTEMP_FK on LF_MMSTEMPLATE (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MMS_MTREPORT
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MMS_MTREPORT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MMS_MTREPORT
(
  USERID      VARCHAR2(11),
  TASKID      NUMBER(11),
  SPGATE      VARCHAR2(21),
  IYMD        NUMBER(11),
  IHOUR       NUMBER(11),
  PTCODE      CHAR(10) DEFAULT ('' ''),
  IMONTH      NUMBER(11),
  ICOUNT      NUMBER(11) DEFAULT 0,
  SUCC        NUMBER(11) DEFAULT 0,
  FAIL1       NUMBER(11) DEFAULT 0,
  FAIL2       NUMBER(11) DEFAULT 0,
  FAIL3       NUMBER(11) DEFAULT 0,
  NRET        NUMBER(11) DEFAULT 0,
  RSUCC       NUMBER(11) DEFAULT 0,
  RFAIL1      NUMBER(11) DEFAULT 0,
  RFAIL2      NUMBER(11) DEFAULT 0,
  RNRET       NUMBER(11) DEFAULT 0,
  RELEASEFLAG NUMBER(11) DEFAULT 1,
  STARTTIME   TIMESTAMP(6) DEFAULT SYSTIMESTAMP,
  ENDTIME     TIMESTAMP(6) DEFAULT SYSTIMESTAMP,
  ID          NUMBER(11) not null,
  Y           NUMBER(11) DEFAULT 0,
  SPISUNCM    NUMBER(11) DEFAULT 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MMS_MTREPORT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MMS_MTREPORT
	  add constraint LF_MMS_MTREPORT_PRIMARY primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MOBFINANCIAL
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MOBFINANCIAL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MOBFINANCIAL
(
  MOBFID    NUMBER(38) not null,
  PHONE     VARCHAR2(64),
  MSG       VARCHAR2(200),
  TASK_ID   NUMBER(38),
  SEND_TIME TIMESTAMP(8) DEFAULT SYSDATE
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_MON_SGTACINFO
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_SGTACINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_SGTACINFO
(
  HOSTID      NUMBER(38) not null,
  GATEACCOUNT VARCHAR2(11) not null,
  GATENAME    VARCHAR2(56) DEFAULT '' '' not null,
  GATEWAYID   NUMBER(38),
  MONSTATUS   NUMBER(3) DEFAULT 1 not null,
  LINKNUM     NUMBER(20) DEFAULT 1 not null,
  USERFEE     NUMBER(20) DEFAULT 0 not null,
  ISARREARAGE NUMBER(20) DEFAULT 0 not null,
  MTREMAINED  NUMBER(20) DEFAULT 0 not null,
  MTRECVSPD   NUMBER(20) DEFAULT 0 not null,
  MOREMAINED  NUMBER(20) DEFAULT 0 not null,
  MOSNDSPD    NUMBER(20) DEFAULT 0 not null,
  MOSNDRATIO  NUMBER(20) DEFAULT 0 not null,
  RPTREMAINED NUMBER(20) DEFAULT 0 not null,
  RPTSNDSPD   NUMBER(20) DEFAULT 0 not null,
  RPTSNDRATIO NUMBER(20) DEFAULT 0 not null,
  CREATETIME  TIMESTAMP(8) DEFAULT sysdate not null,
  MODIFYTIME  TIMESTAMP(8) DEFAULT sysdate not null,
  MONPHONE    VARCHAR2(256),
  MONFREQ     NUMBER(20) DEFAULT 5 not null,
  MONEMAIL    VARCHAR2(256) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_SGTACINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_SGTACINFO
	  add primary key (GATEACCOUNT)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_RMSTASK_CTRL(富信发送任务控制表)
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_RMSTASK_CTRL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_RMSTASK_CTRL(
    ID NUMBER(18) NOT NULL,--主键
    TASKID NUMBER(18) DEFAULT 0 NOT NULL,--任务id
    CURRENT_COUNT NUMBER(18) DEFAULT 0 NOT NULL,--当前发送记录数
    UPDATE_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL--修改时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_RMSTASK_CTRL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_RMSTASK_CTRL
	  ADD CONSTRAINT PK_LF_RMSTASK_CTRL PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/
  
DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_RMSTASK_CTRL') AND T.INDEX_NAME = UPPER('INDEX_LF_RMSTASK_CTRL_TASKID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_LF_RMSTASK_CTRL_TASKID ON LF_RMSTASK_CTRL(TASKID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_DGTACINFO
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DGTACINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_DGTACINFO
(
  ID           NUMBER(38) not null,
  GATEACCOUNT  VARCHAR2(11) DEFAULT '' '' not null,
  EVTTYPE      NUMBER(20) DEFAULT 0 not null,
  GATEWAYID    NUMBER(38),
  LINKNUM      NUMBER(20) DEFAULT 1 not null,
  LOGINIP      VARCHAR2(64) DEFAULT '' '' not null,
  ONLINESTATUS NUMBER(3) DEFAULT 0 not null,
  USERFEE      NUMBER(20) DEFAULT 0 not null,
  FEEFLAG      NUMBER(20) DEFAULT 0 not null,
  MTTOTALSND   NUMBER(20) DEFAULT 0 not null,
  MTHAVESND    NUMBER(20) DEFAULT 0 not null,
  MTREMAINED   NUMBER(20) DEFAULT 0 not null,
  MTRECVSPD    NUMBER(20) DEFAULT 0 not null,
  MOTOTALRECV  NUMBER(20) DEFAULT 0 not null,
  MOHAVESND    NUMBER(20) DEFAULT 0 not null,
  MOREMAINED   NUMBER(20) DEFAULT 0 not null,
  MOSNDSPD     NUMBER(20) DEFAULT 0 not null,
  RPTHAVESND   NUMBER(20) DEFAULT 0 not null,
  RPTREMAINED  NUMBER(20) DEFAULT 0 not null,
  RPTSNDSPD    NUMBER(20) DEFAULT 0 not null,
  RPTTOTALRECV NUMBER(20) DEFAULT 0 not null,
  LOGININTM    TIMESTAMP(8) DEFAULT sysdate not null,
  LOGINOUTTM   TIMESTAMP(8) DEFAULT sysdate not null,
  MODIFYTIME   TIMESTAMP(8) DEFAULT sysdate not null,
  DBSERVTIME   TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
  HOSTID NUMBER(38) DEFAULT 0 NOT NULL,
  GATENAME VARCHAR2(64) DEFAULT '' '' NOT NULL,
  SERVERNUM VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  THRESHOLDFLAG1 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG2 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG3 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG4 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG5 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG6 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG7 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG8 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG9 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG10 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG11 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG12 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG13 NUMBER(16) DEFAULT 0 NOT NULL,
  SENDMAILFLAG1 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG2 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG3 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG4 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG5 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG6 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG7 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG8 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG9 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG10 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG11 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG12 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG13 NUMBER(38) DEFAULT 0 NOT NULL,
  GWNO NUMBER(38) DEFAULT 0 NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_DGTACINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_DGTACINFO
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MON_DGTACINFO') AND T.INDEX_NAME = UPPER('GATEACCUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_MON_DGTACINFO
	  ADD CONSTRAINT GATEACCUNIQUE UNIQUE (GATEACCOUNT,GWNO)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_BUSAREASEND(业务区域发送统计表)
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BUSAREASEND';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_BUSAREASEND
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,   --自增ID
  BUS_NAME VARCHAR2(32) DEFAULT '' '' NOT NULL,     --业务名称
  BUS_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,     --业务编码
  AREA_CODE VARCHAR2(21) DEFAULT '' '' NOT NULL,   --区域 多个以逗号隔开
  MTSENDCOUNT VARCHAR2(512) DEFAULT '' '' NOT NULL,  --MT已发（条）
  CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,     --企业编码
  UPDATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --最后一次更新时间
  DATA_DATE NUMBER(20) DEFAULT 20000101 NOT NULL,     --数据时间
  GATEWAYID NUMBER(38) DEFAULT 0 NOT NULL            --网关编号
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_BUSAREASEND') AND T.INDEX_NAME = UPPER('UN_BUSSEND');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_BUSAREASEND
	  ADD CONSTRAINT UN_BUSSEND UNIQUE (GATEWAYID,BUS_CODE,AREA_CODE,DATA_DATE)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_SHOST
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_SHOST';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_SHOST
(
  HOSTID        NUMBER(38) not null,
  HOSTNAME      VARCHAR2(64) not null,
  HOSTTYPE      NUMBER(3) not null,
  ADAPTER1      VARCHAR2(72),
  ADAPTER2      VARCHAR2(72) DEFAULT '''',
  OUPIP         VARCHAR2(64) DEFAULT '' '',
  HOSTMEM       NUMBER(20),
  HOSTHD        NUMBER(20),
  HOSTCPU       VARCHAR2(64) DEFAULT '''',
  OPERSYS       VARCHAR2(128) DEFAULT '''',
  MONSTATUS     NUMBER(3) DEFAULT 1 not null,
  CREATETIME    TIMESTAMP(8) DEFAULT sysdate not null,
  MODIFYTIME    TIMESTAMP(8) DEFAULT sysdate not null,
  DESCR         VARCHAR2(128) DEFAULT '''',
  MONPHONE      VARCHAR2(256),
  CPUUSAGE      NUMBER(20) DEFAULT 0 not null,
  MEMUSAGE      NUMBER(20) DEFAULT 0 not null,
  VMEMUSAGE     NUMBER(20) DEFAULT 0 not null,
  DISKFREESPACE NUMBER(20) DEFAULT 0 not null,
  CPUSJ         NUMBER(20) DEFAULT 0,
  CPUBL         NUMBER(20) DEFAULT 0,
  PROCESSCNT    NUMBER(20) DEFAULT 0 not null,
  MONFREQ       NUMBER(20) DEFAULT 5 not null,
  HOSTUSESTATUS NUMBER(20) DEFAULT 0 not null,
  MONEMAIL      VARCHAR2(256) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_SHOST') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_SHOST
	  add primary key (HOSTID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_DBWARN（数据库与程序监控告警设置表）
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DBWARN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_DBWARN
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,            --否  自增ID,全局唯一
  MONPHONE VARCHAR2(256) DEFAULT '' '' NOT NULL, --接收告警手机号
  MONEMAIL VARCHAR2(256) DEFAULT '' '' NOT NULL, --接收告警邮箱
  CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--  创建时间
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL --  更新时间
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MON_DHOST
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DHOST';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_DHOST
(
  ID            NUMBER(38) not null,
  HOSTID        NUMBER(38) not null,
  HOSTSTATUS    NUMBER(3) DEFAULT 0 not null,
  EVTTYPE       NUMBER(20) DEFAULT 0 not null,
  MEMUSE        NUMBER(20) DEFAULT 0,
  VMEMUSE       NUMBER(20) DEFAULT 0,
  DISKSPACE     NUMBER(20) DEFAULT 0,
  CPUUSAGE      NUMBER(20) DEFAULT 0 not null,
  MEMUSAGE      NUMBER(20) DEFAULT 0 not null,
  VMEMUSAGE     NUMBER(20) DEFAULT 0 not null,
  DISKFREESPACE NUMBER(20) DEFAULT 0 not null,
  UPDATETIME    TIMESTAMP(8) DEFAULT sysdate not null,
  PROCESSCNT    NUMBER(20) DEFAULT 0 not null,
  HOSTNAME      VARCHAR2(64) DEFAULT '' '' NOT NULL,
  ADAPTER1      VARCHAR2(72) DEFAULT '' '' NOT NULL,
  SERVERNUM     VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  THRESHOLDFLAG1 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG2 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG3 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG4 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG5 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG6 NUMBER(16) DEFAULT 0 NOT NULL,
  SENDMAILFLAG1  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG2  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG3  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG4  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG5  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG6  NUMBER(38) DEFAULT 0 NOT NULL,
  IPADDR         VARCHAR2(256) DEFAULT '' '' NOT NULL,
  DBSERVTIME     TIMESTAMP(8) DEFAULT SYSDATE NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_MON_DHOST
  add constraint FK_LF_DHOST_FK_LF_SHOST foreign key (HOSTID)
  references LF_MON_SHOST (HOSTID)';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MON_DHOST') AND T.INDEX_NAME = UPPER('UN_HOSTID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_MON_DHOST
	  ADD CONSTRAINT UN_HOSTID UNIQUE (HOSTID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_DHOST') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_DHOST
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_SPROCE
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_SPROCE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_SPROCE
(
  PROCEID        NUMBER(38) not null,
  HOSTID         NUMBER(38) not null,
  PROCENAME      VARCHAR2(64) not null,
  VERSION        VARCHAR2(64) DEFAULT '' '' not null,
  MONFREQ        NUMBER(20) DEFAULT 5 not null,
  MONSTATUS      NUMBER(3) DEFAULT 1 not null,
  PROCETYPE      NUMBER(20) not null,
  GATEWAYID      NUMBER(38),
  CREATETIME     TIMESTAMP(8) DEFAULT sysdate not null,
  MODIFYTIME     TIMESTAMP(8) DEFAULT sysdate not null,
  MONPHONE       VARCHAR2(256),
  DESCR          VARCHAR2(128),
  CPUBL          NUMBER(20) DEFAULT 0 not null,
  MEMUSE         NUMBER(20) DEFAULT 0 not null,
  VMEMUSE        NUMBER(20) DEFAULT 0 not null,
  HARDDISKSPACE  NUMBER(20) DEFAULT 0 not null,
  CURTHREADUSE   NUMBER(20) DEFAULT 0,
  CURSEESIONUSE  NUMBER(20) DEFAULT 0,
  CURCONUSE      NUMBER(20) DEFAULT 0,
  HEAPUSE        NUMBER(20) DEFAULT 0,
  NONHEAPUSE     NUMBER(20) DEFAULT 0,
  PROCEUSESTATUS NUMBER(20) DEFAULT 0 not null,
  MONEMAIL       VARCHAR2(256) DEFAULT '' '' NOT NULL,
  ISDBCONNECT NUMBER(2) DEFAULT 1 NOT NULL, --数据库网络连接监控0：开启1：关闭
  SERVERNUM VARCHAR2(8) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_SPROCE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_SPROCE
	  add primary key (PROCEID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_DPROCE(程序动态信息表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DPROCE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_DPROCE
(
  ID            NUMBER(38) not null,
  PROCEID       NUMBER(38) not null,
  GATEWAYID     NUMBER(38),
  EVTTYPE       NUMBER(20) DEFAULT 0 not null,
  CPUUSAGE      NUMBER(20) DEFAULT 0 not null,
  MEMUSAGE      NUMBER(20) DEFAULT 0 not null,
  VMEMUSAGE     NUMBER(20) DEFAULT 0 not null,
  DISPFREE      NUMBER(20) DEFAULT 0 not null,
  PROCESTATUS   NUMBER(3) DEFAULT 0 not null,
  UPDATETIME    TIMESTAMP(8) DEFAULT sysdate not null,
  STARTTIME     TIMESTAMP(8),
  MAXTHREADNUM  NUMBER(20) DEFAULT 0,
  CURTHREADNUM  NUMBER(20) DEFAULT 0,
  BUSYTHREADNUM NUMBER(20) DEFAULT 0,
  CURSEESIONNUM NUMBER(20) DEFAULT 0,
  MAXACTIVENUM  NUMBER(20) DEFAULT 0,
  CURCONNUM     NUMBER(20) DEFAULT 0,
  HEAPUSE       NUMBER(20) DEFAULT 0,
  NONHEAPUSE    NUMBER(20) DEFAULT 0,
  DBCONNECTSTATE NUMBER(2) DEFAULT 0 NOT NULL, --数据库网络连接状态0：正常1：断开
  HOSTNAME VARCHAR2(64) DEFAULT '' '' NOT NULL,
  PROCENAME VARCHAR2(64) DEFAULT '' '' NOT NULL,
  HOSTID NUMBER(38) DEFAULT 0 NOT NULL,
  VERSION VARCHAR2(64) DEFAULT '' '' NOT NULL,
  SERVERNUM VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  PROCETYPE NUMBER(20) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG1 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG2 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG3 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG4 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG5 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG6 NUMBER(16) DEFAULT 0 NOT NULL,
  SENDMAILFLAG1 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG2 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG3 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG4 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG5 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG6 NUMBER(38) DEFAULT 0 NOT NULL,
  DBSERVTIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_MON_DPROCE
  add constraint FK_LF_DPROCE_FK_LF_DPROCE foreign key (PROCEID)
  references LF_MON_SPROCE (PROCEID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_DPROCE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_DPROCE
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MON_DPROCE') AND T.INDEX_NAME = UPPER('UN_PROCEID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_MON_DPROCE
	  ADD CONSTRAINT UN_PROCEID UNIQUE (PROCEID)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SPOFFLINEPRD(SP离线时间段告警阀值管理表)
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SPOFFLINEPRD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SPOFFLINEPRD(
  ID NUMBER(38) PRIMARY KEY NOT NULL,  --ID，自增
  SPACCOUNTID VARCHAR2(11) DEFAULT '' '' NOT NULL,  --SP账号
  BEGIN_HOUR NUMBER(8) DEFAULT 0 NOT NULL,        --开始时间段(小时)
  END_HOUR NUMBER(8) DEFAULT 0 NOT NULL,         --结束时间段(小时)
  DURATION NUMBER(6) DEFAULT 0 NOT NULL,         --时长
  CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--创建时间
  UPDATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--更新时间
  CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL, --企业编码
  USER_ID NUMBER(38) DEFAULT 0 NOT NULL           --最后更新的操作员ID
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MON_SSPACINFO(SP账号信息表)
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_SSPACINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_SSPACINFO
(
  HOSTID        NUMBER(38) not null,
  SPACCOUNTID   VARCHAR2(11) not null,
  ACCOUNTNAME   VARCHAR2(56) DEFAULT '' '' not null,
  SPACCOUNTTYPE NUMBER(3) DEFAULT 0 not null,
  SENDLEVEL     NUMBER(20),
  MONSTATUS     NUMBER(3) DEFAULT 1 not null,
  MAXLINKNUM    NUMBER(20) DEFAULT 1 not null,
  USERFEE       NUMBER(20) DEFAULT 0 not null,
  MTHAVESND     NUMBER(20) DEFAULT 0 not null,
  MTREMAINED    NUMBER(20) DEFAULT 0 not null,
  MTSNDSPD      NUMBER(20) DEFAULT 0 not null,
  MTISSUEDSPD   NUMBER(20) DEFAULT 0 not null,
  MOREMAINED    NUMBER(20) DEFAULT 0 not null,
  MORECVRATIO   NUMBER(20) DEFAULT 0 not null,
  RPTREMAINED   NUMBER(20) DEFAULT 0 not null,
  RPTSNDSPD     NUMBER(20) DEFAULT 0 not null,
  RPTRECVRATIO  NUMBER(20) DEFAULT 0 not null,
  CREATETIME    TIMESTAMP(8) DEFAULT sysdate not null,
  MODIFYTIME    TIMESTAMP(8) DEFAULT sysdate not null,
  MONPHONE      VARCHAR2(256),
  MONFREQ       NUMBER(20) DEFAULT 5 not null,
  MONEMAIL      VARCHAR2(256) DEFAULT '' '' NOT NULL,
  LOGIN_TYPE    NUMBER(1) DEFAULT 0 NOT NULL,  --登录类型，0－WBS登录；1－直连登录 2-直连
  OFFLINETHRESHD NUMBER(8) DEFAULT 0 NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_SSPACINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_SSPACINFO
	  add primary key (SPACCOUNTID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_DGATEBUF(SPGATE缓冲数据表)
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DGATEBUF';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_DGATEBUF(
		ID 	NUMBER(38) PRIMARY KEY NOT NULL,
		PROCEID  NUMBER(38) NOT NULL,
		GATEWAYID       NUMBER(38),
		RPTSDWAITBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		MOSDBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		RPTSDBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		MTUPDBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		MTSDBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		MOSDWAITBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		RPTRVCNT		NUMBER(20) DEFAULT 0 NOT NULL,
		MTSDCNT		NUMBER(20) DEFAULT 0 NOT NULL,
		MTSPD1		NUMBER(20) DEFAULT 0 NOT NULL,
		MORVCNT		NUMBER(20) DEFAULT 0 NOT NULL,
		MTSPD2		NUMBER(20) DEFAULT 0 NOT NULL,
		MORPTSPD		NUMBER(20) DEFAULT 0 NOT NULL,
		RPTRVBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		MORVBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		MTRVCNT		NUMBER(20) DEFAULT 0 NOT NULL,
		MTSDWAITBUF		NUMBER(20) DEFAULT 0 NOT NULL,
		EVTTYPE		NUMBER(20) DEFAULT 0 NOT NULL,
		GATENAME	VARCHAR2(64)	DEFAULT '' '' NOT NULL,
		GATEACCOUNT	VARCHAR2(64)	DEFAULT '' '' NOT NULL,
		UPDATETIME	TIMESTAMP(8) DEFAULT SYSDATE	NOT NULL,
		DBSERVTIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL
	)
	TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 1
	  MAXTRANS 255
	  STORAGE
	  (
	    INITIAL 64K
	    MINEXTENTS 1
	    MAXEXTENTS UNLIMITED
	  )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MON_DGATEBUF') AND T.INDEX_NAME = UPPER('UQ_ACCGATE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_DGATEBUF
	  add constraint UQ_ACCGATE unique (GATEACCOUNT,GATEWAYID)
	  using index
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_DSPACINFO
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DSPACINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_DSPACINFO
(
  ID           NUMBER(38) not null,
  SPACCOUNTID  VARCHAR2(11) DEFAULT '' '' not null,
  EVTTYPE      NUMBER(20) DEFAULT 0 not null,
  LINKNUM      NUMBER(20) DEFAULT 1 not null,
  LOGINIP      VARCHAR2(64) DEFAULT '' '' not null,
  ONLINESTATUS NUMBER(3) DEFAULT 0 not null,
  SENDLEVEL    NUMBER(20) DEFAULT 5,
  USERFEE      NUMBER(20) DEFAULT 0 not null,
  FEEFLAG      NUMBER(20) DEFAULT 0 not null,
  MTTOTALSND   NUMBER(20) DEFAULT 0 not null,
  MTHAVESND    NUMBER(20) DEFAULT 0 not null,
  MTREMAINED   NUMBER(20) DEFAULT 0 not null,
  MTSNDSPD     NUMBER(20) DEFAULT 0 not null,
  MTISSUEDSPD  NUMBER(20) DEFAULT 0 not null,
  MOTOTALRECV  NUMBER(20) DEFAULT 0 not null,
  MOHAVESND    NUMBER(20) DEFAULT 0 not null,
  MOREMAINED   NUMBER(20) DEFAULT 0 not null,
  MOSNDSPD     NUMBER(20) DEFAULT 0 not null,
  RPTTOTALRECV NUMBER(20) DEFAULT 0 not null,
  RPTREMAINED  NUMBER(20) DEFAULT 0 not null,
  RPTSNDSPD    NUMBER(20) DEFAULT 0 not null,
  LOGININTM    TIMESTAMP(8) DEFAULT sysdate not null,
  LOGINOUTTM   TIMESTAMP(8) DEFAULT sysdate not null,
  UPDATETIME   TIMESTAMP(8) DEFAULT sysdate not null,
  HOSTID       NUMBER(38) DEFAULT 0 NOT NULL,
  SPACCOUNTTYPE NUMBER(3) DEFAULT 0 NOT NULL,
  ACCOUNTNAME  VARCHAR2(56) DEFAULT '' '' NOT NULL,
  RPTHAVESND  NUMBER(20) DEFAULT 0 NOT NULL,
  OFFLINEDURATION NUMBER(16) DEFAULT 0 NOT NULL,
  NOMTHAVESND NUMBER(16) DEFAULT 0 NOT NULL,
  LOGINTYPE NUMBER(2) DEFAULT 0 NOT NULL,
  ONLINESTATUSSTR VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  SUBMITSTATUSSTR VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  THRESHOLDFLAG1 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG2 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG3 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG4 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG5 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG6 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG7 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG8 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG9 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG10 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG11 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG12 NUMBER(16) DEFAULT 0 NOT NULL,
  THRESHOLDFLAG13 NUMBER(16) DEFAULT 0 NOT NULL,
  GATENAME VARCHAR2(64) DEFAULT '' '' NOT NULL,
  SERVERNUM VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  SENDMAILFLAG1  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG2  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG3  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG4  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG5  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG6  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG7  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG8  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG9  NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG10 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG11 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG12 NUMBER(38) DEFAULT 0 NOT NULL,
  SENDMAILFLAG13 NUMBER(38) DEFAULT 0 NOT NULL,
  GATEWAYID      NUMBER(38) DEFAULT 0 NOT NULL,
  DBSERVTIME     TIMESTAMP(8) DEFAULT SYSDATE NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_DSPACINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_DSPACINFO
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MON_DSPACINFO') AND T.INDEX_NAME = UPPER('SPACCIDNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_DSPACINFO
	  add constraint SPACCIDNIQUE unique (GATEWAYID,SPACCOUNTID)
	  using index
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_ERR
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_ERR';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_ERR
(
  ID           NUMBER(38) not null,
  HOSTID       NUMBER(38) not null,
  PROCEID      NUMBER(38) DEFAULT 0 not null,
  SPACCOUNTID  VARCHAR2(11) DEFAULT '' '' not null,
  GATEACCOUNT  VARCHAR2(11) DEFAULT '' '' not null,
  APPTYPE      NUMBER(20) not null,
  EVTID        NUMBER(38),
  WHO          VARCHAR2(16) DEFAULT '''',
  EVTTYPE      NUMBER(3),
  DEALPEOPLE   VARCHAR2(20),
  DEALDESC     VARCHAR2(512),
  MONSTATUS    NUMBER(3) DEFAULT 1 not null,
  DEALFLAG     NUMBER(3) DEFAULT 1 not null,
  CRTTIME      TIMESTAMP(8),
  EVTTIME      TIMESTAMP(8) not null,
  RCVTIME      TIMESTAMP(8) not null,
  MSG          VARCHAR2(512) not null,
  MONTHRESHOLD NUMBER(20) DEFAULT 0,
  MONTIMER     NUMBER(38) DEFAULT 1,
  SPOFFLINEPRD VARCHAR2(32) DEFAULT '' '' NOT NULL, --SP未提交告警时间段和时长
  PROCENODE NUMBER(38) DEFAULT 0 NOT NULL, --程序节点
  WEBNODE NUMBER(38) DEFAULT 0 NOT NULL --WEB服务器程序的节点
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_ERR') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_ERR
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_BUSINFO(业务监控数据信息表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_BUSINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_BUSINFO
(
   ID NUMBER(38) PRIMARY KEY NOT NULL,  --自增ID
   BUS_NAME VARCHAR2(32) DEFAULT '' '' NOT NULL,    --业务名称
   BUS_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    --业务编码
   AREA_CODE VARCHAR2(512) DEFAULT '' '' NOT NULL,  --区域多个区域以,隔开
   BEGIN_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --监控开始时间
   END_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,  --监控结果时间
   BEGIN_HOUR NUMBER(20) DEFAULT 0 NOT NULL,        --开始时间段(小时)
   END_HOUR NUMBER(20) DEFAULT 0 NOT NULL,        --结束时间段(小时)
   EVTTYPE NUMBER(2) DEFAULT 0 NOT NULL,            --告警级别0：正常1：警告
   MTSENDCOUNT NUMBER(20) DEFAULT 0 NOT NULL,        --MT已发(条)
   MTHAVESND NUMBER(20) DEFAULT 0 NOT NULL,        --MT已发告警值
   MON_DEVIAT VARCHAR2(128) DEFAULT '' '' NOT NULL,  --告警偏离率（告警描述）
   MON_DES VARCHAR(128) DEFAULT '' '' NOT NULL,    --告警说明
   MONPHONE VARCHAR2(256) DEFAULT '' '' NOT NULL,    --告警短信发送手机号
   CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    --企业编码
   CREATE_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL--创建时间
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MON_DBOPR（数据库库操作表）
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_DBOPR';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_DBOPR
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,      --否  自增ID,全局唯一
  CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,--  创建时间
  PROCENODE NUMBER(38) DEFAULT 0 NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MON_ERRHIS
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_ERRHIS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_ERRHIS
(
  ID          NUMBER(38) not null,
  HOSTID      NUMBER(38) not null,
  PROCEID     NUMBER(38) DEFAULT 0 not null,
  SPACCOUNTID VARCHAR2(11) DEFAULT '' '' not null,
  GATEACCOUNT VARCHAR2(11) DEFAULT '' '' not null,
  APPTYPE     NUMBER(20) not null,
  WHO         VARCHAR2(16) DEFAULT '''',
  EVTID       NUMBER(38),
  EVTTIME     TIMESTAMP(8) not null,
  EVTTYPE     NUMBER(3),
  DEALPEOPLE  VARCHAR2(20) DEFAULT '' '' not null,
  DEALDESC    VARCHAR2(512),
  DEALFLAG    NUMBER(3) DEFAULT 1 not null,
  MONSTATUS   NUMBER(3) DEFAULT 1 not null,
  RCVTIME     TIMESTAMP(8) not null,
  CRTTIME     TIMESTAMP(8) not null,
  MSG         VARCHAR2(512) not null,
  MONTIMER    NUMBER(38) DEFAULT 1,
  SPOFFLINEPRD VARCHAR2(32) DEFAULT '' '' NOT NULL,
  PROCENODE NUMBER(38) DEFAULT 0 NOT NULL, --程序节点
  WEBNODE NUMBER(38) DEFAULT 0 NOT NULL --WEB服务器程序的节点
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_ERRHIS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_ERRHIS
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_ONLCFG
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_ONLCFG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_ONLCFG
(
  ID         NUMBER(38) not null,
  ONLINENUM  NUMBER(38) DEFAULT 0 not null,
  MAX_ONLINE NUMBER(38) DEFAULT 500 not null,
  EVTTYPE    NUMBER(20) DEFAULT 0 not null,
  MONFREQ    NUMBER(38) DEFAULT 30 not null,
  MONPHONE   VARCHAR2(256),
  MODIFYTIME TIMESTAMP(8) DEFAULT sysdate not null,
  MONSTATUS  NUMBER(3) DEFAULT 1 not null,
  THRESHOLDFLAG VARCHAR2(64) DEFAULT ''0'' NOT NULL,
  SERVERNUM VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  MONEMAIL VARCHAR2(256) DEFAULT '' '' NOT NULL,
  SENDMAILFLAG VARCHAR2(64) DEFAULT ''0'' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_ONLCFG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_ONLCFG
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_ONLUSER
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_ONLUSER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_ONLUSER
(
  SESSEION_ID VARCHAR2(128) not null,
  CORP_CODE   VARCHAR2(64) not null,
  USER_NAME   VARCHAR2(64) not null,
  NAME        VARCHAR2(64) not null,
  USER_ID     NUMBER(38) not null,
  DEP_ID      NUMBER(38) not null,
  LOGINTIME   TIMESTAMP(8) not null,
  LOGINADDR   VARCHAR2(15) not null,
  SERVERNUM   VARCHAR2(8) not null,
  DEP_NAME VARCHAR2(64) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_ONLUSER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_ONLUSER
	  add primary key (SESSEION_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_SPGATEBUF
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_SPGATEBUF';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_SPGATEBUF
(
  ID           NUMBER(38) not null,
  PROCEID      NUMBER(38) not null,
  GATEWAYID    NUMBER(38) not null,
  MTRVCNT      NUMBER(20) DEFAULT 0 not null,
  MTSDCNT      NUMBER(20) DEFAULT 0 not null,
  MTSDBUF      NUMBER(20) DEFAULT 0 not null,
  MTUPDBUF     NUMBER(20) DEFAULT 0 not null,
  MTSDWAITBUF  NUMBER(20) DEFAULT 0 not null,
  MTSPD1       NUMBER(20) DEFAULT 0 not null,
  MTSPD2       NUMBER(20) DEFAULT 0 not null,
  MORVCNT      NUMBER(20) DEFAULT 0 not null,
  RPTRVCNT     NUMBER(20) DEFAULT 0 not null,
  MORVBUF      NUMBER(20) DEFAULT 0 not null,
  RPTRVBUF     NUMBER(20) DEFAULT 0 not null,
  MOSDBUF      NUMBER(20) DEFAULT 0 not null,
  RPTSDBUF     NUMBER(20) DEFAULT 0 not null,
  MOSDWAITBUF  NUMBER(20) DEFAULT 0 not null,
  RPTSDWAITBUF NUMBER(20) DEFAULT 0 not null,
  MORPTSPD     NUMBER(20) DEFAULT 0 not null,
  UPDATETIME   TIMESTAMP(8) DEFAULT sysdate not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_SPGATEBUF') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_SPGATEBUF
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_HNETWARN(主机网络监控告警设置表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_HNETWARN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_HNETWARN
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,               --否  自增ID,全局唯一
  MONPHONE VARCHAR2(256) DEFAULT '' '' NOT NULL,    --接收告警手机号
  MONEMAIL VARCHAR2(256) DEFAULT '' '' NOT NULL,     --接收告警邮箱
  CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --创建时间
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL  --记录更新时间
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MON_HOSTNET(主机网络监控信息表)
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_HOSTNET';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MON_HOSTNET
(
  ID NUMBER(38) PRIMARY KEY NOT NULL,               --否  自增ID,全局唯一
  WEBNODE NUMBER(38) DEFAULT 0  NOT NULL,           --WEB服务器程序的节点
  WEBNAME VARCHAR2(64) DEFAULT '' '' NOT NULL,      --WEB服务器主机名称
  PROCENODE NUMBER(38) DEFAULT 0 NOT NULL,          --监控主机所属程序的的节点
  PROCETYPE NUMBER(10) DEFAULT 0 NOT NULL,			--程序类型
  HOSTNAME VARCHAR2(64) DEFAULT '' '' NOT NULL,     --监控主机名称
  MONTYPE NUMBER(3) DEFAULT 0  NOT NULL,            --0:WEB服务器主机与其他程序主机网络监控
  IPADDR VARCHAR(256) DEFAULT '' '' NOT NULL,        --多个IP以逗号“,”分隔
  CREATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL, --创建时间
  UPDATETIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL ,--记录更新时间
  MONSTATUS NUMBER(1) DEFAULT 1 NOT NULL,           --监控状态：  0：未监控  1：监控
  EVTTYPE NUMBER(2) DEFAULT 0 NOT NULL,             --0：正常  1：告警  2：严重
  NETSTATE NUMBER(2) DEFAULT 0 NOT NULL,            --主机之间的网络状态  0：正常；1：断开
  SMSALFLAG1 NUMBER(38) DEFAULT 0 NOT NULL,         --网络状态告警短信标识 网络状态短信告警状态记录
  MAILALFLAG1 NUMBER(38) DEFAULT 0 NOT NULL,         --网络状态告警邮件标识 网络状态邮件告警状态记录
  SERVERNUM VARCHAR2(8) DEFAULT ''0'' NOT NULL,
  DBSERVTIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL --数据分析服务器节点
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MON_HOSTNET') AND T.INDEX_NAME = UPPER('UQ_WNODEPNODE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_MON_HOSTNET
	  ADD CONSTRAINT UQ_WNODEPNODE UNIQUE (WEBNODE,PROCENODE,PROCETYPE)
	  USING INDEX
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MON_WBSBUF
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MON_WBSBUF';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MON_WBSBUF
(
  ID           NUMBER(38) not null,
  PROCEID      NUMBER(38) not null,
  GATEWAYID    NUMBER(38) not null,
  MTNOSD       NUMBER(20) DEFAULT 0 not null,
  MONOSD       NUMBER(20) DEFAULT 0 not null,
  RPTNOSD      NUMBER(20) DEFAULT 0 not null,
  ENDCNT       NUMBER(20) DEFAULT 0 not null,
  MORVCNT      NUMBER(20) DEFAULT 0 not null,
  MTSDCNT      NUMBER(20) DEFAULT 0 not null,
  WRMOBUF      NUMBER(20) DEFAULT 0 not null,
  UPDMOBUF     NUMBER(20) DEFAULT 0 not null,
  WRRPTBUF     NUMBER(20) DEFAULT 0 not null,
  ENDRSPBUF    NUMBER(20) DEFAULT 0 not null,
  MTSDBUF      NUMBER(20) DEFAULT 0 not null,
  MTWAITBUF    NUMBER(20) DEFAULT 0 not null,
  PRECNT       NUMBER(20) DEFAULT 0 not null,
  MTRVCNT      NUMBER(20) DEFAULT 0 not null,
  MOSDCNT      NUMBER(20) DEFAULT 0 not null,
  WRMTBUF      NUMBER(20) DEFAULT 0 not null,
  WRMTVFYBUF   NUMBER(20) DEFAULT 0 not null,
  WRMTLVLBUF   NUMBER(20) DEFAULT 0 not null,
  PRERSPBUF    NUMBER(20) DEFAULT 0 not null,
  PRERSPTMPBUF NUMBER(20) DEFAULT 0 not null,
  MORPTSDBUF   NUMBER(20) DEFAULT 0 not null,
  RPTSDBUF     NUMBER(20) DEFAULT 0 not null,
  MORPTWAITBUF NUMBER(20) DEFAULT 0 not null,
  LOGFILENUM   NUMBER(20) DEFAULT 0 not null,
  LOGBUF       NUMBER(20) DEFAULT 0 not null,
  RECVBUF      NUMBER(20) DEFAULT 0 not null,
  RESNDBUF     NUMBER(20) DEFAULT 0 not null,
  SUPPSDBUF    NUMBER(20) DEFAULT 0 not null,
  MTRVSPD1     NUMBER(20) DEFAULT 0 not null,
  MTSDSPD1     NUMBER(20) DEFAULT 0 not null,
  MTRVSPD2     NUMBER(20) DEFAULT 0 not null,
  MTSDSPD2     NUMBER(20) DEFAULT 0 not null,
  MORPTRVSPD1  NUMBER(20) DEFAULT 0 not null,
  MORPTSDSPD1  NUMBER(20) DEFAULT 0 not null,
  MORPTRVSPD2  NUMBER(20) DEFAULT 0 not null,
  MORPTSDSPD2  NUMBER(20) DEFAULT 0 not null,
  UPDATETIME   TIMESTAMP(8) DEFAULT sysdate not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MON_WBSBUF') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MON_WBSBUF
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MOTASK
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MOTASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MOTASK
(
  MO_ID       NUMBER(38) not null,
  PTMSGID     NUMBER(19) DEFAULT 0,
  UIDS        NUMBER(38) DEFAULT 0,
  ORGUID      NUMBER(38) DEFAULT 0,
  ECID        NUMBER(38) DEFAULT 0,
  SP_USER     VARCHAR2(11) DEFAULT (''''),
  SPNUMBER    VARCHAR2(21) DEFAULT ('' ''),
  SERVICEID   CHAR(10) DEFAULT ('' ''),
  SPSC        VARCHAR2(32),
  SENDSTATUS  NUMBER(38) DEFAULT 2,
  MSGFMT      NUMBER(38) DEFAULT 15,
  TP_PID      NUMBER(38) DEFAULT 0,
  TP_UDHI     NUMBER(38) DEFAULT 0,
  DELIVERTIME TIMESTAMP(6) DEFAULT sysdate not null,
  PHONE       VARCHAR2(21) DEFAULT ('' ''),
  MSGCONTENT  VARCHAR2(720) DEFAULT ('' ''),
  DONETIME    TIMESTAMP(6) DEFAULT SYSTIMESTAMP not null,
  USER_GUID   NUMBER(38),
  MENUCODE    VARCHAR2(64),
  DEP_ID      NUMBER(38),
  TASK_ID     NUMBER(38),
  BUS_CODE    VARCHAR2(64),
  CORPCODE    VARCHAR2(32),
  SPISUNCM    NUMBER(11),
  SUBNO       VARCHAR2(20),
  MOORDER     VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MOTASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MOTASK
	  add constraint PK_LF_MOTASK primary key (MO_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MO_SERVICE
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MO_SERVICE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MO_SERVICE
(
  MS_ID       NUMBER(38) not null,
  SP_USER     CHAR(6) DEFAULT ('' '') not null,
  SPNUMBER    CHAR(21) DEFAULT ('' '') not null,
  MSGCONTENT  VARCHAR2(720) DEFAULT ('' '') not null,
  DELIVERTIME TIMESTAMP(6) DEFAULT sysdate not null,
  PHONE       VARCHAR2(32) DEFAULT ('' '') not null,
  PR_ID       NUMBER(38),
  SER_ID      NUMBER(38),
  REPLY_STATE NUMBER(20),
  REPLY_URL   VARCHAR2(512),
  CORP_CODE   VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_MO_SERVICE
  add constraint FK_MO_SERVICE_FK_PROCESS foreign key (PR_ID)
  references LF_PROCESS (PR_ID)';
  EXECUTE IMMEDIATE 'alter table LF_MO_SERVICE
  add constraint FK_MO_SERVICE_FK_SERVICE foreign key (SER_ID)
  references LF_SERVICE (SER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MO_SERVICE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MO_SERVICE
	  add constraint PK_LF_MO_SERVICE primary key (MS_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MTRPT_EC
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MTRPT_EC';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MTRPT_EC
(
  ID        NUMBER(11) not null,
  USERID    VARCHAR2(11) not null,
  TASKID    NUMBER(11) DEFAULT 0 not null,
  SPGATE    VARCHAR2(21) not null,
  ERRORCODE CHAR(7) DEFAULT ''       '' not null,
  Y         NUMBER(11) DEFAULT 0 not null,
  IMONTH    NUMBER(11) not null,
  ICOUNT    NUMBER(11) DEFAULT 0 not null,
  IYMD      NUMBER(11) not null,
  SPISUNCM  NUMBER(11) DEFAULT 0 not null,
  SVRTYPE   VARCHAR2(64) DEFAULT ('' '') not null,
  P1        VARCHAR2(64) DEFAULT ('' '') not null,
  P2        VARCHAR2(64) DEFAULT ('' '') not null,
  P3        VARCHAR2(64) DEFAULT ('' '') not null,
  P4        VARCHAR2(64) DEFAULT ('' '') not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MTRPT_EC') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MTRPT_EC
	  add constraint LF_MTRPT_EC_PRIMARY primary key (USERID, TASKID, SPGATE, IYMD, ERRORCODE, Y, IMONTH, SPISUNCM, SVRTYPE, P1, P2, P3, P4)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTRPT_EC') AND T.INDEX_NAME = UPPER('IX_LF_MTRPT_EC');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index IX_LF_MTRPT_EC on LF_MTRPT_EC (TASKID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_MT_PRI(下行记录查询权限)
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MT_PRI';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_MT_PRI
(
  ID NUMBER(38) PRIMARY KEY NOT NULL, /*标识列ID*/
  SPUSERID VARCHAR2(32) DEFAULT '''' NOT NULL,/*发送账号*/
  USER_ID NUMBER(38) DEFAULT 0 NOT NULL,/*操作员USERID*/
  CREATETIME DATE DEFAULT SYSDATE NOT NULL,/*创建时间*/
  CREATE_USERID NUMBER(38) DEFAULT 0 NOT NULL,/*创建者USERID*/
  CORP_CODE VARCHAR2(64) DEFAULT '''' NOT NULL /*企业编码*/
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
     INITIAL 64K
     MINEXTENTS 1
     MAXEXTENTS UNLIMITED
   )';
  END IF;
END;
/

--
-- Creating table LF_MTTASK
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_MTTASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_MTTASK
(
  MT_ID         NUMBER(38) not null,
  USER_ID       NUMBER(38) not null,
  TITLE         VARCHAR2(64),
  MSG           VARCHAR2(2048),
  MSG_TYPE      NUMBER(4),
  BIGINTIME     DATE,
  ENDTIME       DATE,
  SENDLEVEL     NUMBER(4),
  SUBMITTIME    DATE,
  TASKNAME      VARCHAR2(64),
  SUB_STATE     NUMBER(4),
  RE_STATE      NUMBER(4),
  SENDSTATE     NUMBER(4),
  RE_LEVEL      NUMBER(4),
  SUB_COUNT     NUMBER,
  EFF_COUNT     NUMBER,
  SUC_COUNT     NUMBER,
  FAI_COUNT     NUMBER,
  BMTTYPE       NUMBER(4),
  CONTENT       VARCHAR2(512),
  MOBILE_URL    VARCHAR2(512),
  MOBILE_TYPE   NUMBER(1),
  TXT_TYPE      NUMBER(1),
  COMMENTS      VARCHAR2(512),
  SP_USER       VARCHAR2(32),
  STAFFNAME     VARCHAR2(32),
  SP_PWD        VARCHAR2(32),
  ICOUNT        NUMBER,
  TMPL_PATH     VARCHAR2(512),
  MS_TYPE       NUMBER(2),
  TIMER_TIME    DATE,
  TIMER_STATUS  NUMBER(1),
  BUS_CODE      VARCHAR2(64),
  PARAMS        VARCHAR2(200),
  CORP_CODE     VARCHAR2(64),
  RS_CODE       VARCHAR2(10),
  TOTALNUM      NUMBER,
  SUCSSCOUNT    NUMBER,
  FAICOUNT      NUMBER,
  LASTDATATIME  DATE,
  ERROR_CODES   VARCHAR2(32),
  ISREPLY       NUMBER(1),
  SPNUMBER      VARCHAR2(80),
  SUBNO         VARCHAR2(20),
  ISRETRY       NUMBER(1) DEFAULT 0,
  RFAIL2        NUMBER,
  RNRET         NUMBER,
  TASKID        NUMBER(38) DEFAULT 0,
  TEMP_ID       NUMBER(38),
  PARAM_COUNT   NUMBER(10),
  ICOUNT2       NUMBER,
  TASKTYPE      NUMBER(3) DEFAULT 1 not null,
  BATCHID       NUMBER(38) DEFAULT 0 not null,
  STARTSENDTIME TIMESTAMP(8),
  ENDSENDTIME   TIMESTAMP(8),
  WYSENDINFO    VARCHAR2(128),
  VALIDTM       NUMBER(5) DEFAULT 48 NOT NULL,
  MSGEDCODETYPE NUMBER(2) DEFAULT 15 NOT NULL, --不同内容动态模块使用换行标识，15：不使用换行；35：使用换行
  FILEURI       VARCHAR2(128) DEFAULT '' '' NOT NULL, --文件服务地址
  TEMP_TYPE     NUMBER(11) DEFAULT 0 NOT NULL,
  PHONENUM     NUMBER(11) DEFAULT -1 NOT NULL, -- 下发手机号码数
  SENDNUM     NUMBER(11) DEFAULT -1 NOT NULL,  -- 已下发信息数
  FINISHTIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL  -- 完成时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MTTASK
	  add constraint PK_LF_MTTASK primary key (MT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('MOBILEURLUNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MTTASK
	  add constraint MOBILEURLUNIQUE unique (MOBILE_URL)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('UQ_MTTASK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_MTTASK
	  add constraint UQ_MTTASK unique (TASKID, BATCHID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('FK_LF_MTT_RELATIONS_LF_SYS_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_MTT_RELATIONS_LF_SYS_FK on LF_MTTASK (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_CCODE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_CCODE on LF_MTTASK (CORP_CODE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_RESTATE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_RESTATE on LF_MTTASK (RE_STATE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_SENDSTATE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_SENDSTATE on LF_MTTASK (SENDSTATE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_SPUSER');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_SPUSER on LF_MTTASK (SP_USER)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_SUBSTATE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_SUBSTATE on LF_MTTASK (SUB_STATE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_SUBTIME');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_SUBTIME on LF_MTTASK (SUBMITTIME)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_TASKID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_TASKID on LF_MTTASK (TASKID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_MTTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_MTTASK_TASKTYPE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index INDEX_LF_MTTASK_TASKTYPE on LF_MTTASK (TASKTYPE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_NOTICE
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_NOTICE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_NOTICE
(
  NOTICE_ID    NUMBER(38) not null,
  USER_ID      NUMBER(38),
  TITLE        VARCHAR2(64),
  CONTEXT      VARCHAR2(2000),
  PUBLISH_TIME DATE,
  CORP_CODE    VARCHAR2(64),
  NOTE_TAIL    VARCHAR2(64),
  NOTE_STATE   NUMBER(3),
  NOTE_VALID   NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_NOTICE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_NOTICE
	  add constraint PK_LF_NOTICE primary key (NOTICE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ONLINE_USER
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONLINE_USER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONLINE_USER
(
  ONUID            NUMBER(38) not null,
  USER_ID          NUMBER(38) not null,
  USERNAME         VARCHAR2(64),
  DEP_ID           NUMBER(38),
  DEP_NAME         VARCHAR2(64),
  MOBILE           VARCHAR2(32),
  SEX              NUMBER(1),
  LOGIN_TYPE       NUMBER(1),
  TOKEN            VARCHAR2(40),
  LAST_ACTIVE_TIME NUMBER(20),
  LAST_LOGIN_TIME  TIMESTAMP(8),
  GUID             NUMBER(38),
  FLAG             VARCHAR2(12) DEFAULT ''0000000000'',
  USER_TYPE        NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_OPERATE
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_OPERATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_OPERATE
(
  OPERATE_ID   NUMBER(38) not null,
  OPERATE_NAME VARCHAR2(64),
  COMMENTS     VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_OPERATE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_OPERATE
	  add constraint PK_LF_OPERATE primary key (OPERATE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_OPRATELOG
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_OPRATELOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_OPRATELOG
(
  LOG_ID     NUMBER(38) not null,
  OP_TIME    DATE,
  OP_USER    VARCHAR2(64),
  OP_MODULE  VARCHAR2(64),
  OP_ACTION  VARCHAR2(64),
  OP_RESULT  NUMBER(1),
  OP_CONTENT VARCHAR2(4000),
  CORP_CODE  VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_OPRATELOG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_OPRATELOG
	  add constraint PK_LF_OPRATELOG primary key (LOG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PAGEFIELD
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PAGEFIELD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PAGEFIELD
(
  MODLEID       VARCHAR2(16),
  PAGEID        VARCHAR2(16),
  FIELDID       VARCHAR2(16) not null,
  FIELDNAME     VARCHAR2(32) not null,
  FIELD         VARCHAR2(32),
  FIELDTYPE     NUMBER(2) not null,
  SUBFIELDVALUE VARCHAR2(16),
  SUBFIELDNAME  VARCHAR2(32),
  FILEDSHOW     NUMBER(1) not null,
  SUBFIELD      NUMBER(1) not null,
  DEFAULTVALUE  VARCHAR2(32),
  SORTVALUE     NUMBER(6),
  ISFIELD       NUMBER(1) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_PARAM
-- =======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PARAM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PARAM
(
  PARAM_ID        NUMBER(38) not null,
  PARAM_CODE      VARCHAR2(12),
  PARAM_NAME      VARCHAR2(32),
  PARAM_VALUE     VARCHAR2(32),
  PARAM_OBJECT    VARCHAR2(32),
  PARAM_IDEN_TYPE NUMBER(2),
  COMMENTS        VARCHAR2(400)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_PERFECT_NOTIC
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PERFECT_NOTIC';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PERFECT_NOTIC
(
  P_NOTIC_ID    NUMBER(38) not null,
  SUBMITTIME    TIMESTAMP(8),
  SEND_INTERAL  NUMBER(20),
  MAX_SENDCOUNT NUMBER(20),
  ARY_SENDCOUNT NUMBER(20),
  SENDER_GUID   NUMBER(38),
  CONTENT       VARCHAR2(2000),
  RECEIVER_TYPE NUMBER(1),
  DIALOGID      VARCHAR2(3000),
  RECEVIERID    NUMBER(38),
  TASKID        NUMBER(38),
  NOTIC_COUNT   NUMBER,
  CORP_CODE     VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PERFECT_NOTIC') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PERFECT_NOTIC
	  add constraint PK_LF_PERFECT_NOTIC primary key (P_NOTIC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PERFECT_NOTIC') AND T.INDEX_NAME = UPPER('PN_UNIQUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PERFECT_NOTIC
	  add constraint PN_UNIQUE unique (TASKID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PERSONALCONFIG
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PERSONALCONFIG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PERSONALCONFIG
(
  PER_ID     NUMBER(38) not null,
  BUS_ID     NUMBER(38),
  MENUCODE   VARCHAR2(64),
  ISPERSONAL NUMBER(1),
  VERSION    VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_PERSONALCONFIG
  add constraint FK_LF_PERCONFIG_FK_BUS foreign key (BUS_ID)
  references LF_BUSINESS (BUS_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PERSONALCONFIG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PERSONALCONFIG
	  add constraint PK_LF_PERSONALCONFIG primary key (PER_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PER_FILEINFO
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PER_FILEINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PER_FILEINFO
(
  TASKID       NUMBER(38) not null,
  SENDFILENAME VARCHAR2(512) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_POSITION
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_POSITION';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_POSITION
(
  P_ID   NUMBER(38) not null,
  P_NAME VARCHAR2(64),
  ISDEL  NUMBER(1) DEFAULT 0,
  MEMO   VARCHAR2(500)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_POSITION') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_POSITION
	  add constraint PK_LF_POSITION primary key (P_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PRIV_LIST
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PRIV_LIST';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PRIV_LIST
(
  USER_ID NUMBER(38) not null,
  DEP_ID  NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_PRIV_LIST
  add constraint FK_LF_PRIV__LF_PRIV_L_LF_DEP foreign key (DEP_ID)
  references LF_DEP (DEP_ID)';
  EXECUTE IMMEDIATE 'alter table LF_PRIV_LIST
  add constraint FK_LF_PRIV__LF_PRIV_L_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PRIV_LIST') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PRIV_LIST
	  add constraint PK_LF_PRIV_LIST primary key (USER_ID, DEP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PRIV_LIST') AND T.INDEX_NAME = UPPER('LF_PRIV_LIST_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_PRIV_LIST_FK on LF_PRIV_LIST (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PRIV_LIST') AND T.INDEX_NAME = UPPER('LF_PRIV_LIST_FK2');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_PRIV_LIST_FK2 on LF_PRIV_LIST (DEP_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PROCHARGES
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PROCHARGES';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PROCHARGES
(
  RULE_ID            NUMBER(38) not null,
  TRYSTART_DATE      TIMESTAMP(8),
  TRYEND_DATE        TIMESTAMP(8),
  TRY_DAYS           NUMBER(5),
  TRY_TYPE           NUMBER(1),
  BUCKLE_TYPE        NUMBER(1),
  BUCKLE_DATE        NUMBER(3),
  BUCKUP_MAXTIMER    NUMBER(3),
  BUCKUP_INTERVALDAY NUMBER(3),
  TAOCAN_CODE        VARCHAR2(64) not null,
  COMMENTS           VARCHAR2(250) DEFAULT '''',
  CREATE_TIME        TIMESTAMP(8) DEFAULT SYSDATE,
  UPDATE_TIME        TIMESTAMP(8) DEFAULT SYSDATE,
  DEP_ID             NUMBER(38),
  USER_ID            NUMBER(38),
  CORP_CODE          VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PROCHARGES') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PROCHARGES
	  add primary key (RULE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PRODUCTOR_CONT
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PRODUCTOR_CONT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PRODUCTOR_CONT
(
  IDEN_ID       NUMBER(38) not null,
  PRO_ID        NUMBER(38) not null,
  CONTENT_TITLE VARCHAR2(64),
  CONTENT_BODY  VARCHAR2(250),
  SEND_TIMES    TIMESTAMP(8),
  SEND_STATUS   NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_PRODUCTS
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PRODUCTS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PRODUCTS
(
  PRO_ID       NUMBER(38) not null,
  PRO_NAME     VARCHAR2(64),
  MONTH_ORDER  VARCHAR2(64),
  VOD_ORDER    VARCHAR2(64),
  UNDO_ORDER   VARCHAR2(200),
  MONTH_COST   NUMBER,
  VOD_COST     NUMBER,
  MT_RATE      NUMBER(38),
  PRO_TYPE     NUMBER(38),
  COMMENTS     VARCHAR2(128),
  TITLE        VARCHAR2(64),
  MSG          VARCHAR2(1024),
  MS_TYPE      NUMBER(1),
  SER_ADDRESS  VARCHAR2(1024),
  PARAM_CONFIG VARCHAR2(1024),
  OVER_COST    NUMBER,
  PRO_CODE     VARCHAR2(64),
  CREATE_TIME  TIMESTAMP(8) DEFAULT SYSDATE,
  SEND_TIME    VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PRODUCTS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PRODUCTS
	  add constraint PK_LF_PRODUCTS primary key (PRO_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PROSENDCOUNT
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PROSENDCOUNT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PROSENDCOUNT
(
  CID        NUMBER(38) not null,
  PRO_ID     NUMBER(38) not null,
  SEND_COUNT NUMBER(38),
  YEAR       NUMBER(38),
  MONTH      NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PROSENDCOUNT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PROSENDCOUNT
	  add constraint PK_LF_PROSENDCOUNT primary key (CID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_PRO_CON
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_PRO_CON';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_PRO_CON
(
  DBCON_ID    NUMBER(38) not null,
  PR_ID       NUMBER(38),
  CON_EXPRESS VARCHAR2(64),
  CON_OPERATE VARCHAR2(64),
  CON_VALUE   VARCHAR2(64),
  COMMENTS    VARCHAR2(512),
  USEDPRID    NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_PRO_CON
  add constraint FK_LF_PRO_CON_FK_LF_PROCESS foreign key (USEDPRID)
  references LF_PROCESS (PR_ID)';
  EXECUTE IMMEDIATE 'alter table LF_PRO_CON
  add constraint FK_LF_PRO_C_FK_LF_PRO_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_PRO_CON') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_PRO_CON
	  add constraint PK_LF_PRO_CON primary key (DBCON_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_PRO_CON') AND T.INDEX_NAME = UPPER('FK_LF_PRO_RELATIONS_LF_PRO_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_PRO_RELATIONS_LF_PRO_FK on LF_PRO_CON (PR_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_P_N_UP
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_P_N_UP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_P_N_UP
(
  PNUP_ID       NUMBER(38) not null,
  P_NOTIC       NUMBER(38),
  SENDER_GUID   NUMBER(38),
  CONTENT       VARCHAR2(2000),
  SEND_TIME     TIMESTAMP(8),
  RECEIVERID    NUMBER(38),
  USERTYPE      NUMBER(2),
  NAME          VARCHAR2(64),
  ISREPLY       NUMBER(1),
  ISRECEIVER    NUMBER(1),
  DIALOGIN      VARCHAR2(32),
  TASKID        NUMBER(38),
  MOBILE        VARCHAR2(64),
  RECEIVE_COUNT NUMBER,
  SPNUMBER      VARCHAR2(21),
  CREATE_TIME   TIMESTAMP(8),
  SPUSER        VARCHAR2(32),
  ISATRRED      VARCHAR2(2)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_P_N_UP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_P_N_UP
	  add constraint PK_LF_P_N_UP primary key (PNUP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_RECONCILIATION
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_RECONCILIATION';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_RECONCILIATION
(
  ID        NUMBER(38) not null,
  IMONTH    NUMBER(2),
  Y         NUMBER,
  IYMD      NUMBER(8),
  RSUCC     NUMBER,
  SPISUNCM  NUMBER(2),
  FEE       NUMBER(6,3),
  STAFFNAME VARCHAR2(64),
  DEP_NAME  VARCHAR2(64),
  GATENAME  VARCHAR2(64),
  COST      NUMBER(10,3),
  ICOUNT    NUMBER,
  USERID    VARCHAR2(11),
  DEP_ID    NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_RECONCILIATION') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_RECONCILIATION
	  add constraint PK_LF_RECONCILIATION primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_REPLY
-- =======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_REPLY';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_REPLY
(
  PR_ID       NUMBER(38),
  MSG_HEADER  VARCHAR2(512),
  MSG_MAIN    VARCHAR2(2000),
  MSG_TAIL    VARCHAR2(512),
  MSG_LOOP_ID NUMBER(38),
  RE_ID       NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_REPLY
  add constraint FK_LF_REPLY_LF_PRO_RE_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_REPLY') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_REPLY
	  add primary key (RE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_REPLY') AND T.INDEX_NAME = UPPER('LF_PRO_REPLY_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_PRO_REPLY_FK on LF_REPLY (PR_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_RESOURCE
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_RESOURCE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_RESOURCE
(
  RESOURCE_ID   NUMBER(38) not null,
  RESOURCE_NAME VARCHAR2(64),
  COMMENTS      VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_RESOURCE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_RESOURCE
	  add constraint PK_LF_RESOURCE primary key (RESOURCE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_REVIEWER
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_REVIEWER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_REVIEWER
(
  F_ID    NUMBER(38) not null,
  MT_ID   NUMBER(38) not null,
  RE_ID   NUMBER(38),
  R_LEVEL NUMBER(4)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_REVIEWER
  add constraint FK_LF_REVIE_LF_REVIEW_LF_FLOW foreign key (F_ID)
  references LF_FLOW (F_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_REVIEWER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_REVIEWER
	  add constraint PK_LF_REVIEWER primary key (F_ID, MT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_REVIEWER') AND T.INDEX_NAME = UPPER('LF_REVIEWER_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_REVIEWER_FK on LF_REVIEWER (F_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_REVIEWER') AND T.INDEX_NAME = UPPER('LF_REVIEWER_FK2');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_REVIEWER_FK2 on LF_REVIEWER (MT_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_REVIEWER2LEVEL
-- ================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_REVIEWER2LEVEL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_REVIEWER2LEVEL
(
  FRL_ID        NUMBER(38) not null,
  F_ID          NUMBER(38) not null,
  USER_ID       NUMBER(38) not null,
  R_LEVEL       NUMBER(4),
  IS_REV_REMIND NUMBER(1),
  R_TYPE        NUMBER(2),
  R_CODE        VARCHAR2(64),
  R_CONDITION   NUMBER(2)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_REVIEWER2LEVEL
  add constraint FK_LF_REVIE_FK_LF_REV_LF_FLOW foreign key (F_ID)
  references LF_FLOW (F_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_REVIEWER2LEVEL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_REVIEWER2LEVEL
	  add constraint PK_LF_REVIEWER2LEVEL primary key (FRL_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_REVIEWER2LEVEL') AND T.INDEX_NAME = UPPER('FK_LF_REV_RELATIONS_LF_FLO_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_REV_RELATIONS_LF_FLO_FK on LF_REVIEWER2LEVEL (F_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_REVIEWER2LEVEL') AND T.INDEX_NAME = UPPER('FK_LF_REV_RELATIONS_LF_SYS_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_REV_RELATIONS_LF_SYS_FK on LF_REVIEWER2LEVEL (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_REVIEWSWITCH
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_REVIEWSWITCH';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_REVIEWSWITCH
(
  ID          NUMBER(38) not null,
  MENUCODE    VARCHAR2(64),
  SWITCH_TYPE NUMBER(2),
  MSG_COUNT   NUMBER(10),
  INFO_TYPE   NUMBER(2),
  UPDATE_TIME TIMESTAMP(8),
  CORP_CODE   VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_SENDGRO
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SENDGRO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SENDGRO
(
  SG_ID   NUMBER(38) not null,
  SER_ID  NUMBER(38),
  SG_NAME VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_SENDGRO
  add constraint FK_LF_SENDG_LF_SER2GR_LF_SERVI foreign key (SER_ID)
  references LF_SERVICE (SER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SENDGRO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SENDGRO
	  add constraint PK_LF_SENDGRO primary key (SG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SENDGRO') AND T.INDEX_NAME = UPPER('LF_SER2GRO_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_SER2GRO_FK on LF_SENDGRO (SER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SERMSG_DETAIL
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SERMSG_DETAIL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SERMSG_DETAIL
(
  SMD_ID      NUMBER(38) not null,
  SL_ID       NUMBER(38),
  SMD_MOBILE  VARCHAR2(64),
  SMD_CONTENT VARCHAR2(140),
  COMMENTS    VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SERMSG_DETAIL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SERMSG_DETAIL
	  add constraint PK_LF_SERMSG_DETAIL primary key (SMD_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SERMSG_DETAIL') AND T.INDEX_NAME = UPPER('LF_SL_RELLATION_SMD_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_SL_RELLATION_SMD_FK on LF_SERMSG_DETAIL (SL_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SERVICELOG
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SERVICELOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SERVICELOG
(
  SL_ID    NUMBER(38) not null,
  SER_ID   NUMBER(38),
  RUNTIME  DATE,
  SL_STATE NUMBER(4),
  URL      VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_SERVICELOG
  add constraint FK_LF_SERVICELOG_FK_SERVICE foreign key (SER_ID)
  references LF_SERVICE (SER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SERVICELOG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SERVICELOG
	  add constraint PK_LF_SERVICELOG primary key (SL_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SERVICELOG') AND T.INDEX_NAME = UPPER('FK_LF_SERVI_RELATIONS_LF_SERVI');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_SERVI_RELATIONS_LF_SERVI on LF_SERVICELOG (SER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SERVICEMSG
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SERVICEMSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SERVICEMSG
(
  SERMSGID    NUMBER(38) not null,
  SER_ID      NUMBER(38),
  MSG_TYPE    NUMBER(4),
  SER_MSG     VARCHAR2(512),
  SG_ID       NUMBER(38),
  CREATE_TIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_SERVICEMSG
  add constraint FK_LF_SERVICEMSG_2_LF_SENDGRO foreign key (SG_ID)
  references LF_SENDGRO (SG_ID)';
  EXECUTE IMMEDIATE 'alter table LF_SERVICEMSG
  add constraint FK_LF_SERVI_LE_SER2MS_LF_SERVI foreign key (SER_ID)
  references LF_SERVICE (SER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SERVICEMSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SERVICEMSG
	  add constraint PK_LF_SERVICEMSG primary key (SERMSGID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SERVICEMSG') AND T.INDEX_NAME = UPPER('LE_SER2MSG_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LE_SER2MSG_FK on LF_SERVICEMSG (SER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SKIN
-- ======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SKIN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SKIN
(
  SID        NUMBER(38) not null,
  SKIN_NAMNE VARCHAR2(32),
  SKIN_CODE  VARCHAR2(32),
  SKIN_SRC   VARCHAR2(200),
  THEME_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_BIGFILE(超大文件表)
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_BIGFILE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_BIGFILE (
	ID NUMBER(11) NOT NULL,    -- 主键ID(页面上显示“文件批次”)
	FILE_NAME VARCHAR2(64) DEFAULT '' '' NOT NULL,    -- 文件批次名称
	SUB_COUNT NUMBER(20) DEFAULT 0 NOT NULL,    -- 提交总数
	EFF_COUNT NUMBER(20) DEFAULT 0 NOT NULL,    -- 有效号码数
	BLA_COUNT NUMBER(20) DEFAULT 0 NOT NULL,    -- 黑名单数量
	REP_COUNT NUMBER(20) DEFAULT 0 NOT NULL,    -- 重复号码数
	ERR_COUNT NUMBER(20) DEFAULT 0 NOT NULL,    -- 格式错误号码数
	OPR_NUM VARCHAR2(50) DEFAULT '' '' NOT NULL,    -- 按照移动,联通,电信,国际的顺序用英文逗号分隔
	HANDLE_STATUS NUMBER(5) DEFAULT 0 NOT NULL,    -- 1.上传中 2.上传成功(有效号码、黑名单、重复号码、格式错误号码已处理完成) 3.上传失败
	FILE_STATUS NUMBER(5) DEFAULT 0 NOT NULL,    -- 1.启用 2.禁用
	TASKID VARCHAR2(1024) DEFAULT '' '' NOT NULL,    -- 发送过该超大文件的任务号，用逗号分隔
	UPLOAD_NUM NUMBER(11) DEFAULT 0 NOT NULL,    -- 原始上传文件个数
	EFF_NUM NUMBER(11) DEFAULT 0 NOT NULL,    -- 拆分出来的有效号码文件个数
	HANDLE_NODE VARCHAR2(50) DEFAULT '' '' NOT NULL,    -- 上传WEB节点
	USERID NUMBER(20) DEFAULT 0 NOT NULL,    -- 操作员ID
	USER_NAME VARCHAR2(64) DEFAULT '' '' NOT NULL,    -- 操作员名称
	BUS_ID NUMBER(20) DEFAULT 0 NOT NULL,    -- 业务类型ID
	BUS_NAME VARCHAR2(32) DEFAULT '' '' NOT NULL,    -- 业务名称
	BUS_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    -- 业务编码
	DEP_ID NUMBER(20) DEFAULT 0 NOT NULL,    -- 机构ID
	CREATE_TIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 上传时间
	UPDATE_TIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 修改时间
	FILE_URL VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件1
	FILE_URL2 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件2
	FILE_URL3 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件3
	FILE_URL4 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件4
	FILE_URL5 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件5
	FILE_URL6 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件6
	FILE_URL7 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件7
	FILE_URL8 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件8
	FILE_URL9 VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 有效拆分文件9
	VIEW_URL VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 预览号码文件路径(只预览10个号码)
	BAD_URL VARCHAR2(512) DEFAULT '' '' NOT NULL,    -- 错误号码文件路径（黑名单、重复号码、格式错误）
	CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    -- 企业编码
	REMARK VARCHAR2(512) DEFAULT '' '' NOT NULL
) TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_BIGFILE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_BIGFILE
	  ADD CONSTRAINT PK_LF_BIGFILE PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 2
	  MAXTRANS 255
	  STORAGE
	  (
		INITIAL 64K
		MINEXTENTS 1
		MAXEXTENTS UNLIMITED
	  )';
	END IF;
END;
/
  
DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_BIGFILE') AND T.INDEX_NAME = UPPER('INDEX_LF_BIGFILE_USERID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_LF_BIGFILE_USERID ON LF_BIGFILE (USERID)
    TABLESPACE EMP_TABLESPACE
    PCTFREE 10
    INITRANS 2
    MAXTRANS 255
    STORAGE
    (
      INITIAL 64K
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    )';
	END IF;
END;
/  

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_BIGFILE') AND T.INDEX_NAME = UPPER('INDEX_LF_BIGFILE_DEP_ID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_LF_BIGFILE_DEP_ID ON LF_BIGFILE (DEP_ID)
    TABLESPACE EMP_TABLESPACE
    PCTFREE 10
    INITRANS 2
    MAXTRANS 255
    STORAGE
    (
      INITIAL 64K
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_BIGFILE') AND T.INDEX_NAME = UPPER('INDEX_LF_BIGFILE_CORP_CODE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_LF_BIGFILE_CORP_CODE ON LF_BIGFILE (CORP_CODE)
    TABLESPACE EMP_TABLESPACE
    PCTFREE 10
    INITRANS 2
    MAXTRANS 255
    STORAGE
    (
      INITIAL 64K
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    )';
	END IF;
END;
/
  
--
-- Creating table LF_SPEUICFG
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SPEUICFG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SPEUICFG
(
  COMPANYNAME VARCHAR2(20),
  COMPANYLOGO VARCHAR2(512),
  BGIMG       VARCHAR2(512),
  LOGINLOGO   VARCHAR2(512),
  DISPCONTENT VARCHAR2(16) DEFAULT ''11100000'' not null,
  DISPLAYTYPE NUMBER(3) DEFAULT 1 not null,
  CORPCODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_SPFEE
-- =======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SPFEE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SPFEE
(
  SF_ID           NUMBER(38) not null,
  SP_USER         VARCHAR2(32),
  SP_USERPASSWORD VARCHAR2(32),
  SP_TYPE         NUMBER(2),
  ACCOUNTTYPE     NUMBER(2),
  SPFEE_URL       VARCHAR2(256),
  BALANCE         NUMBER(20),
  BALANCETH       NUMBER(20),
  UPDATETIME      TIMESTAMP(8),
  SPFEE_FLAG      NUMBER(2),
  SP_RESULT       VARCHAR2(32),
  RMS_BALANCE     NUMBER(18) DEFAULT 0 NOT NULL,
  QUERY_TIME TIMESTAMP(8) DEFAULT SYSDATE NOT NULL --判断最后一次查询时间与当前时间是否超过（49:向MBOSS查询运营商余额间隔时间）查询间隔时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SPFEE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SPFEE
	  add primary key (SF_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SP_DEP_BIND
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SP_DEP_BIND';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SP_DEP_BIND
(
  DSG_ID         NUMBER(38) not null,
  DEP_CODE_THIRD VARCHAR2(64),
  SPUSER         VARCHAR2(64),
  SPGATEID       NUMBER(38),
  USER_ID        NUMBER(38),
  BIIND_TYPE     NUMBER(1),
  SHARE_TYPE     NUMBER(1),
  MENUCODE       VARCHAR2(64),
  BUS_CODE       VARCHAR2(64),
  SELBIN_TYPE    NUMBER(2),
  PLATFORM_TYPE  NUMBER(1),
  BUS_ID         NUMBER(38),
  CORP_CODE      VARCHAR2(64),
  IS_VALIDATE    NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SP_DEP_BIND') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SP_DEP_BIND
	  add constraint PK_LF_SP_DEP_BIND primary key (DSG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SQLWHERE
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SQLWHERE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SQLWHERE
(
  WH_ID      NUMBER(38) not null,
  PR_ID      NUMBER(38),
  IF_EXPRESS VARCHAR2(64),
  WH_OPERATE VARCHAR2(64),
  IF_VALUE   VARCHAR2(64),
  WH_JOIN    VARCHAR2(64),
  COMMENTS   VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_SQLWHERE
  add constraint FK_LF_SQLWH_FK_LF_SQL_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SQLWHERE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SQLWHERE
	  add constraint PK_LF_SQLWHERE primary key (WH_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SQLWHERE') AND T.INDEX_NAME = UPPER('FK_LF_SQL_RELATIONS_LF_PRO_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index FK_LF_SQL_RELATIONS_LF_PRO_FK on LF_SQLWHERE (PR_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SUBNOALLOT
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SUBNOALLOT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SUBNOALLOT
(
  SUID             NUMBER(38) not null,
  MENUCODE         VARCHAR2(64),
  LOGINID          VARCHAR2(64),
  SP_USER          VARCHAR2(32),
  SUBNO            VARCHAR2(20),
  ALLOTTYPE        NUMBER(1),
  EXTENDSUBNOBEGIN VARCHAR2(20),
  EXTEMDSUBNOEND   VARCHAR2(20),
  USEDEXTENDSUBNO  VARCHAR2(20),
  SPNUMBER         VARCHAR2(21),
  UPDATETIME       TIMESTAMP(6),
  CREATETIME       TIMESTAMP(6),
  ROUTEID          NUMBER,
  BUS_CODE         VARCHAR2(64),
  DEP_ID           NUMBER(38),
  SHARE_TYPE       NUMBER(2),
  CODES            VARCHAR2(64),
  CODE_TYPE        NUMBER(2),
  CORP_CODE        VARCHAR2(64),
  TASK_ID          NUMBER(38),
  VALIDITY         NUMBER(38),
  ISVALID          NUMBER(2) DEFAULT 1
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SUBNOALLOT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SUBNOALLOT
	  add constraint PK_LF_SUBNOALLOT primary key (SUID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SUBNOALLOT_DETAIL
-- ===================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SUBNOALLOT_DETAIL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SUBNOALLOT_DETAIL
(
  SUDID           NUMBER(38) not null,
  MENUCODE        VARCHAR2(64),
  LOGINID         VARCHAR2(64),
  SP_USER         VARCHAR2(32),
  SPGATE          VARCHAR2(20),
  SUBNO           VARCHAR2(20),
  USEDEXTENDSUBNO VARCHAR2(20),
  SPNUMBER        VARCHAR2(21),
  ALLOTTYPE       NUMBER(1),
  UPDATETIME      TIMESTAMP(6),
  CREATETIME      TIMESTAMP(6),
  BUS_CODE        VARCHAR2(64),
  CODES           VARCHAR2(64),
  CODE_TYPE       NUMBER(2),
  SUID            NUMBER(38),
  CORP_CODE       VARCHAR2(64),
  TASK_ID         NUMBER(38),
  VALIDITY        NUMBER(38),
  DEP_ID          NUMBER(38),
  ISVALID         NUMBER(2) DEFAULT 1
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SUBNOALLOT_DETAIL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SUBNOALLOT_DETAIL
	  add constraint PK_LF_SUBNOALLOT_DETAIL primary key (SUDID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SURVEY
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SURVEY';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SURVEY
(
  SUV_ID      NUMBER(38) not null,
  TASKID      NUMBER(38),
  MSG_TYPE    NUMBER(2),
  PHONE       VARCHAR2(64) not null,
  MSG_CONTENT VARCHAR2(200),
  SUV_SERID   NUMBER(38),
  SUV_PROCEID NUMBER(38) not null,
  PRE_PROCEID NUMBER(38),
  MSG_TIME    TIMESTAMP(8) DEFAULT sysdate,
  SPGATE      VARCHAR2(21),
  SPONSOR     NUMBER(38),
  SEND_STATE  NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SURVEY') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SURVEY
	  add constraint PK_LF_SURVEY primary key (SUV_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SURVEYTASK
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SURVEYTASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SURVEYTASK
(
  IDEN_ID     NUMBER(38),
  TASK_ID     NUMBER(38),
  SER_ID      NUMBER(38),
  MSG_CONTENT VARCHAR2(200),
  PHONE       VARCHAR2(32),
  MSG_TYPE    NUMBER(2),
  MSG_TIME    TIMESTAMP(8),
  SP_USERID   NUMBER(38),
  SPONSOR     NUMBER(38),
  ADD_URL     VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_SYSMTTASK
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SYSMTTASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SYSMTTASK
(
  ID          NUMBER(38) not null,
  USER_ID     NUMBER(38),
  TITLE       VARCHAR2(64),
  MSG         VARCHAR2(1024),
  SEND_TYPE   NUMBER(20),
  SUBMITTIME  TIMESTAMP(8),
  SUB_COUNT   NUMBER(20),
  SUC_COUNT   NUMBER(20),
  FAI_COUNT   NUMBER(20),
  BMTTYPE     NUMBER(20),
  MOBILE_URL  VARCHAR2(512),
  PHONE       VARCHAR2(21),
  SP_USER     VARCHAR2(11),
  ICOUNT      NUMBER(20),
  MS_TYPE     NUMBER(20),
  CORP_CODE   VARCHAR2(64),
  SENDSTATE   NUMBER(20),
  TASKID      NUMBER(38),
  BUS_CODE    VARCHAR2(64),
  ERROR_CODES VARCHAR2(32),
  USER_CODE   VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SYSMTTASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SYSMTTASK
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SYS_PARAM
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SYS_PARAM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SYS_PARAM
(
  PARAM_ID    NUMBER(38) not null,
  PARAM_GROUP VARCHAR2(64),
  PARAM_ITEM  VARCHAR2(64),
  PARAM_VALUE VARCHAR2(64),
  MEMO        VARCHAR2(512)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SYS_PARAM') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SYS_PARAM
	  add constraint PK_LF_SYS_PARAM primary key (PARAM_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_SYS_PARAM') AND T.INDEX_NAME = UPPER('UQ_PARAM_ITEM');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SYS_PARAM
	  add constraint UQ_PARAM_ITEM unique (PARAM_ITEM)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TAOCAN_CMD
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TAOCAN_CMD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TAOCAN_CMD
(
  ID           NUMBER(38) not null,
  STRUCTCODE   VARCHAR2(20) not null,
  TAOCAN_CODE  VARCHAR2(64),
  TAOCAN_TYPE  NUMBER(2),
  TAOCAN_MONEY NUMBER(20),
  STRUCT_TYPE  NUMBER(2) not null,
  CREATE_TIME  TIMESTAMP(8) DEFAULT SYSDATE,
  UPDATE_TIME  TIMESTAMP(8) DEFAULT SYSDATE,
  DEP_ID       NUMBER(38),
  USER_ID      NUMBER(38),
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TAOCAN_CMD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_TAOCAN_CMD
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TASK
-- ======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TASK
(
  TAID        NUMBER(38) not null,
  TASKID      NUMBER(38) not null,
  SL_ID       NUMBER(38),
  MT_ID       NUMBER(38),
  TASKTYPE    NUMBER(2),
  SPUSER      VARCHAR2(64),
  USERNAME    VARCHAR2(64),
  DEP_ID      NUMBER(38),
  USER_ID     NUMBER(38),
  SUBMIT_TIME TIMESTAMP(8) DEFAULT sysdate,
  CORP_CODE   VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_TASK
  add constraint FK_LF_TASK_LF_MTMSG__LF_MTTAS foreign key (MT_ID)
  references LF_MTTASK (MT_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_TASK
	  add constraint PK_LF_TASK primary key (TASKID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_TASK') AND T.INDEX_NAME = UPPER('IX_LF_TASK_USERID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index IX_LF_TASK_USERID on LF_TASK (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_TASK') AND T.INDEX_NAME = UPPER('LF_MTMSG_TASK_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_MTMSG_TASK_FK on LF_TASK (MT_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_TASK') AND T.INDEX_NAME = UPPER('LF_SERVICE_TASK_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_SERVICE_TASK_FK on LF_TASK (SL_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TASKREPORT
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TASKREPORT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TASKREPORT
(
  R_TASKID   NUMBER,
  R_ICOUNT   NUMBER,
  R_SUCC     NUMBER,
  R_FAIL1    NUMBER,
  R_FAIL2    NUMBER,
  R_NRET     NUMBER,
  R_BATCHID  NUMBER(38),
  R_TASKTYPE NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_TEMP
-- ======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TEMP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TEMP
(
  DEPID      NUMBER,
  LEV        NUMBER(38),
  SJS        VARCHAR2(32),
  CURENTDATE TIMESTAMP(6)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_TEMPCONTENT
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TEMPCONTENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_TEMPCONTENT(
    ID NUMBER(18) NOT NULL,
    TMID NUMBER(18) DEFAULT 0 NOT NULL,
    CONTTYPE NUMBER(11) DEFAULT 0 NOT NULL,
    TMPTYPE NUMBER(11) DEFAULT 0 NOT NULL,
    TMPCONTENT VARCHAR2(3000) DEFAULT '' '' NOT NULL
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TEMPCONTENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_TEMPCONTENT
	  ADD CONSTRAINT PK_LF_TEMPCONTENT PRIMARY KEY (ID)
	  USING index
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TEMPLATE
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TEMPLATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TEMPLATE
(
  TM_ID        NUMBER(38) not null,
  USER_ID      NUMBER(38),
  TM_NAME      VARCHAR2(64),
  TM_MSG       VARCHAR2(2000),
  DSFLAG       NUMBER(5),
  TM_STATE     NUMBER(5),
  ADDTIME      TIMESTAMP(8) DEFAULT SYSDATE NOT NULL,
  ISPASS       NUMBER(5),
  TMP_TYPE     NUMBER(3),
  BIZ_CODE     VARCHAR2(64),
  CORP_CODE    VARCHAR2(64),
  SP_TEMPLID   NUMBER(38),
  AUDITSTATUS  NUMBER(2),
  MMS_TMPLID   NUMBER(38),
  TMPLSTATUS   NUMBER(2),
  PARAMCNT     NUMBER(10),
  SUBMITSTATUS NUMBER(10),
  EMP_TEMPLID  VARCHAR2(32),
  ERROR_CODE   NUMBER(38),
  TM_CODE      VARCHAR2(64),
  --新增字段
  USECOUNT     NUMBER(11) DEFAULT 0 NOT NULL,--使用次数
  DEGREE       NUMBER(11) DEFAULT 0 NOT NULL,--档位
  DEGREE_SIZE  NUMBER(18) DEFAULT 0 NOT NULL,--容量大小
  INDUSTRYID   NUMBER(11) DEFAULT -1 NOT NULL,--行业ID
  USEID        NUMBER(11) DEFAULT -2 NOT NULL,--用途ID
  ISSHORTTEMP  NUMBER(11) DEFAULT 0 NOT NULL,--是否为快捷模板
  ISPUBLIC     NUMBER(5) DEFAULT 0 NOT NULL,--是否为公共模板
  EXLJSON      VARCHAR2(4000) DEFAULT ''{}'' NOT NULL,--V2.0模板生成的Excel的表头信息
  VER          VARCHAR2(10) DEFAULT ''V1.0'' NOT NULL,--模板版本 V1.0、V2.0
  CHGRADE      NUMBER(5) DEFAULT 0 NOT NULL,--是否为公共模板
  RMSRESSIZE   NUMBER(11) DEFAULT 0 NOT NULL,
  H5RESSIZE    NUMBER(11) DEFAULT 0 NOT NULL,
  PARAMSNUM    NUMBER(11) DEFAULT 0 NOT NULL,
  SOURCE NUMBER(11) DEFAULT 2 NOT NULL, --富信模板来源
  ISMATERIAL NUMBER(11) DEFAULT 0 NOT NULL, -- RCOS 平台同步来的模板状态 ISMATERIAL 0—否 ，1-是
  RCOS_TMPSTATE NUMBER(11) DEFAULT 1 NOT NULL -- RCOS 平台同步来的公共素材字段 RCOS_TMPSTATE 0—禁用 ，1-启用
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_TEMPLATE
  add constraint FK_LF_TEMPL_LF_ADDTEM_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TEMPLATE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_TEMPLATE
	  add constraint PK_LF_TEMPLATE primary key (TM_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_TEMPLATE') AND T.INDEX_NAME = UPPER('LF_ADDTEMPLETE_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_ADDTEMPLETE_FK on LF_TEMPLATE (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_THEME
-- =======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_THEME';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_THEME
(
  TID        NUMBER(38) not null,
  THEME_NAME VARCHAR2(64),
  THEME_CODE VARCHAR2(64),
  THEME_SRC  VARCHAR2(100)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_THIR_MENUCONTROL
-- ==================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_THIR_MENUCONTROL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_THIR_MENUCONTROL
(
  TID       NUMBER(38) not null,
  MENU_NUM  NUMBER(38),
  TITLE     VARCHAR2(32),
  PRI_MENU  NUMBER(38),
  PRI_ORDER NUMBER(38),
  ZH_TW_TITLE VARCHAR2(32),
  ZH_HK_TITLE VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_THIR_MENUCONTROL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_THIR_MENUCONTROL
	  add constraint PK_LF_THIR_MENUCONTROL primary key (TID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TIMER
-- =======================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TIMER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TIMER
(
  TIMER_TASK_ID    NUMBER(38) not null,
  TIMER_TASK_NAME  VARCHAR2(200),
  RUNSTATE         NUMBER(1),
  START_TIME       DATE,
  PRE_TIME         DATE,
  NEXT_TIME        DATE,
  RESULT           NUMBER(1),
  RUN_INTERVAL     NUMBER(38),
  TASK_EXPRESSION  VARCHAR2(250),
  TASK_ERRORCODE   VARCHAR2(38),
  FAIL_RETRY_TIME  DATE,
  FAIL_RETRY_COUNT NUMBER(38),
  RUNCOUNT         NUMBER(38),
  RUNPERCOUNT      NUMBER(38),
  CLASS_NAME       VARCHAR2(64) not null,
  CUSTOMINTERVAL   NUMBER(38),
  ISRERUN          NUMBER(1),
  RERUNCOUNT       NUMBER(8),
  INTERVAL_UNIT    NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TIMER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_TIMER
	  add constraint PK_LF_TIMER primary key (TIMER_TASK_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TIMER_HISTORY
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TIMER_HISTORY';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TIMER_HISTORY
(
  TA_ID            NUMBER(38) not null,
  TIMER_TASK_ID    NUMBER(38),
  RUN_TIME         TIMESTAMP(6),
  RUN_YEAR         NUMBER(4),
  RUN_MONTH        NUMBER(2),
  RUN_DAY          NUMBER(2),
  RUN_RESULT       NUMBER(1),
  TASK_EXPRESSION  VARCHAR2(250),
  TASK_ERRORCODE   VARCHAR2(38),
  TIMER_TASK_NAME  VARCHAR2(200),
  START_TIME       TIMESTAMP(8),
  PRE_TIME         TIMESTAMP(8),
  RUN_INTERVAL     NUMBER(38),
  FAIL_RETRY_TIME  TIMESTAMP(8),
  FAIL_RETRY_COUNT NUMBER(38),
  RUNCOUNT         NUMBER(20),
  RUNPERCOUNT      NUMBER(20),
  CLASS_NAME       VARCHAR2(64),
  CUSTOMINTERVAL   NUMBER(38),
  ISRERUN          NUMBER(3),
  RERUNCOUNT       NUMBER(20),
  INTERVAL_UNIT    NUMBER(3)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TIMER_HISTORY') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_TIMER_HISTORY
	  add constraint PK_LF_TIMER_HISTORY primary key (TA_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TMPLRELA
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TMPLRELA';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TMPLRELA
(
  ID          NUMBER(38) not null,
  TEMPL_ID    NUMBER(38),
  TOUSER      NUMBER(38),
  TOUSER_TYPE NUMBER(10),
  TEMPL_TYPE  NUMBER(10),
  SHARE_TYPE  NUMBER(10),
  CREATER_ID  NUMBER(38),
  CREATETIME  TIMESTAMP(8),
  CORP_CODE   VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TMPLRELA') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_TMPLRELA
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_TRUCTTYPE
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TRUCTTYPE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_TRUCTTYPE
(
  ID   NUMBER(38) not null,
  TYPE CHAR(2) not null,
  NAME VARCHAR2(20) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_USER2CLIENT
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_USER2CLIENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_USER2CLIENT
(
  USER_ID   NUMBER(38) not null,
  CLIENT_ID NUMBER(38) not null,
  DEP_ID    NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_USER2CLIENT
  add constraint FK_LF_USER2_LF_USER2C_LF_CLIEN foreign key (CLIENT_ID)
  references LF_CLIENT (CLIENT_ID)';
  EXECUTE IMMEDIATE 'alter table LF_USER2CLIENT
  add constraint FK_LF_USER2_LF_USER2C_LF_DEP foreign key (DEP_ID)
  references LF_DEP (DEP_ID)';
  EXECUTE IMMEDIATE 'alter table LF_USER2CLIENT
  add constraint FK_LF_USER2_LF_USER2C_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_USER2CLIENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_USER2CLIENT
	  add constraint PK_LF_USER2CLIENT primary key (USER_ID, CLIENT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_USER2CLIENT') AND T.INDEX_NAME = UPPER('LF_USER2CLIENT_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_USER2CLIENT_FK on LF_USER2CLIENT (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_USER2CLIENT') AND T.INDEX_NAME = UPPER('LF_USER2CLIENT_FK2');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_USER2CLIENT_FK2 on LF_USER2CLIENT (CLIENT_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_USER2CLIENT') AND T.INDEX_NAME = UPPER('LF_USER2CLIENT_FK3');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_USER2CLIENT_FK3 on LF_USER2CLIENT (DEP_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_USER2ROLE
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_USER2ROLE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_USER2ROLE
(
  ROLE_ID NUMBER(38) not null,
  USER_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_USER2ROLE
  add constraint FK_LF_USER2_LF_USER2R_LF_ROLES foreign key (ROLE_ID)
  references LF_ROLES (ROLE_ID)';
  EXECUTE IMMEDIATE 'alter table LF_USER2ROLE
  add constraint FK_LF_USER2_LF_USER2R_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_USER2ROLE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_USER2ROLE
	  add constraint PK_LF_USER2ROLE primary key (ROLE_ID, USER_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_USER2ROLE') AND T.INDEX_NAME = UPPER('LF_USER2ROLE_FK');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_USER2ROLE_FK on LF_USER2ROLE (ROLE_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_USER2ROLE') AND T.INDEX_NAME = UPPER('LF_USER2ROLE_FK2');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_USER2ROLE_FK2 on LF_USER2ROLE (USER_ID)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_USER2SKIN
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_USER2SKIN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_USER2SKIN
(
  USID       NUMBER(38) not null,
  SID        NUMBER(38),
  USERID     NUMBER(38),
  SKIN_CODE  VARCHAR2(32),
  MONVOICE   NUMBER(20) DEFAULT 0 not null,
  THEME_CODE VARCHAR2(64),
  THEME_USE  NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_USERPARA
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_USERPARA';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_USERPARA
(
  PARA_ID      NUMBER(38) not null,
  PARA_NAME    VARCHAR2(64),
  PARA_TYPE    VARCHAR2(64),
  PARA_LEN     NUMBER,
  PARA_DEFAULT VARCHAR2(64),
  PARA_RE      VARCHAR2(512),
  SER_ID       NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE 'alter table LF_USERPARA
  add constraint FK_LF_USERPARA_LF_SERVICE foreign key (SER_ID)
  references LF_SERVICE (SER_ID)';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_USERPARA') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_USERPARA
	  add constraint PK_LF_USERPARA primary key (PARA_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_USER_RPT
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_USER_RPT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_USER_RPT
(
  IDEN_ID    NUMBER(38) not null,
  SID        NUMBER(38),
  RID        NUMBER(38),
  RTIME      TIMESTAMP(8),
  SEQ        VARCHAR2(64),
  TASKID     NUMBER(38),
  STIME      TIMESTAMP(8),
  SEND_STATE NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_USER_RPT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_USER_RPT
	  add constraint PK_LF_USER_RPT primary key (IDEN_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_USER_SETTING
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_USER_SETTING';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_USER_SETTING
(
  GUID    NUMBER(38) not null,
  FW_TYPE NUMBER(2),
  IDEN_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_VERSION_EMPDB
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_VERSION_EMPDB';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_VERSION_EMPDB
(
  DVID           NUMBER(38) not null,
  EMP_DBVERSION  VARCHAR2(32) not null,
  GATE_DBVERSION VARCHAR2(32) not null,
  UPDATETIME     TIMESTAMP(6) DEFAULT SYSDATE not null,
  REMARK         VARCHAR2(4000)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_VERSION_EMPDB') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_VERSION_EMPDB
	  add constraint PK_LF_VERSION_EMPDB primary key (DVID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_VODINFO
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_VODINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_VODINFO
(
  VOD_ID     NUMBER(38) not null,
  PHONE      VARCHAR2(64),
  PRO_ID     NUMBER(38),
  PRO_TYPE   VARCHAR2(64),
  VOD_TIME   TIMESTAMP(8),
  VOD_TYPE   NUMBER(38),
  UNDO_TIME  TIMESTAMP(8),
  VOD_STATUS NUMBER(38),
  TRY_STATUS NUMBER(38),
  TRY_COUNT  NUMBER(38),
  COMMENTS   VARCHAR2(128)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_VODINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_VODINFO
	  add constraint PK_LF_VODINFO primary key (VOD_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_VODMOREC
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_VODMOREC';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_VODMOREC
(
  REC_ID       NUMBER(38) not null,
  PHONE        VARCHAR2(64),
  MSG          VARCHAR2(1024),
  MO_TIME      TIMESTAMP(8),
  REPLY_STATUS NUMBER(2),
  SPGATE       VARCHAR2(21),
  REPLY_MSG    VARCHAR2(128)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_VODMOREC') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_VODMOREC
	  add primary key (REC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_VODMTREC
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_VODMTREC';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_VODMTREC
(
  REC_ID      NUMBER(38) not null,
  PHONE       VARCHAR2(64),
  PRO_NAME    VARCHAR2(64),
  PRO_TYPE    VARCHAR2(64),
  TASK_ID     NUMBER(38),
  SPGATE      VARCHAR2(21),
  SEND_STATUS NUMBER(2),
  SEND_TIME   TIMESTAMP(8),
  TITLE       VARCHAR2(64),
  MSG         VARCHAR2(1024),
  MS_TYPE     NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_VODMTREC') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_VODMTREC
	  add primary key (REC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_ACCOUNT
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_ACCOUNT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_ACCOUNT
(
  A_ID         NUMBER(38) not null,
  FAKE_ID      VARCHAR2(32),
  NAME         VARCHAR2(64),
  CODE         VARCHAR2(25) not null,
  OPEN_ID      VARCHAR2(64) not null,
  IMG          VARCHAR2(128),
  TYPE         NUMBER(2),
  IS_APPROVE   NUMBER(2) DEFAULT 0,
  QRCODE       VARCHAR2(128),
  INFO         VARCHAR2(2048),
  URL          VARCHAR2(64),
  TOKEN        VARCHAR2(32),
  BINDTIME     TIMESTAMP(8),
  CORP_CODE    VARCHAR2(64) not null,
  CREATETIME   TIMESTAMP(8) not null,
  MODIFYTIME   TIMESTAMP(8),
  GRANT_TYPE   VARCHAR2(64),
  APPID        VARCHAR2(64),
  SECRET       VARCHAR2(64),
  ACCESS_TOKEN VARCHAR2(256),
  ACCESS_TIME  TIMESTAMP(8),
  RELEASE_TIME TIMESTAMP(8),
  ACCESS_COUNT VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_ACCOUNT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_ACCOUNT
	  add constraint PK_LF_WC_ACCOUNT primary key (A_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_ALINK
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_ALINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_ALINK
(
  OPEN_ID VARCHAR2(64) not null,
  WC_ID   NUMBER(38) not null,
  A_ID    NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_WC_KEYWORD
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_KEYWORD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_KEYWORD
(
  K_ID       NUMBER(38) not null,
  NAME       VARCHAR2(32) not null,
  TYPE       NUMBER(2) DEFAULT 0,
  A_ID       NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_KEYWORD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_KEYWORD
	  add constraint PK_LF_WC_KEYWORD primary key (K_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_MENU
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_MENU';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_MENU
(
  M_ID       NUMBER(38) not null,
  M_NAME     VARCHAR2(32) not null,
  M_TYPE     NUMBER(10) not null,
  M_KEY      VARCHAR2(64),
  M_URL      VARCHAR2(256),
  M_HIDDEN   NUMBER(10),
  M_ORDER    VARCHAR2(11) not null,
  P_ID       NUMBER(38),
  T_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000),
  A_ID       NUMBER(38) not null,
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_MENU') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_MENU
	  add constraint PK_LF_WC_MENUT primary key (M_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_MSG
-- ========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_MSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_MSG
(
  MSG_ID     NUMBER(38) not null,
  MSG_TYPE   NUMBER(2) not null,
  WC_ID      NUMBER(38) not null,
  A_ID       NUMBER(38) not null,
  TYPE       NUMBER(2) not null,
  MSG_XML    VARCHAR2(4000) not null,
  MSG_TEXT   VARCHAR2(4000) not null,
  PARENT_ID  NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_MSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_MSG
	  add constraint PK_LF_WC_MSG primary key (MSG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_REVENT
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_REVENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_REVENT
(
  EVT_ID     NUMBER(38) not null,
  EVT_TYPE   NUMBER(2),
  T_ID       NUMBER(38),
  A_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000) not null,
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  TITLE      VARCHAR2(64),
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_REVENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_REVENT
	  add constraint PK_LF_WC_REVENT primary key (EVT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_RIMG
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_RIMG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_RIMG
(
  RIMG_ID     NUMBER(38) DEFAULT 1 not null,
  TITLE       VARCHAR2(64) not null,
  DESCRIPTION VARCHAR2(4000),
  PICURL      VARCHAR2(128),
  LINK        VARCHAR2(128),
  A_ID        NUMBER(38),
  CORP_CODE   VARCHAR2(64) not null,
  CREATETIME  TIMESTAMP(8) not null,
  MODIFYTIME  TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_RIMG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_RIMG
	  add constraint PK_LF_WC_RIMG primary key (RIMG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_RTEXT
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_RTEXT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_RTEXT
(
  TET_ID     NUMBER(38) not null,
  TET_TYPE   NUMBER(2) DEFAULT 0,
  T_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000) not null,
  A_ID       NUMBER(38),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  TITLE      VARCHAR2(64),
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_RTEXT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_RTEXT
	  add constraint PK_LF_WC_RTEXT primary key (TET_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_TEMPLATE
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_TEMPLATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_TEMPLATE
(
  T_ID        NUMBER(38) not null,
  T_NAME      VARCHAR2(64) not null,
  MSG_TYPE    NUMBER(2),
  MSG_XML     VARCHAR2(4000),
  MSG_TEXT    VARCHAR2(4000),
  IS_PUBLIC   NUMBER(2) DEFAULT 0,
  IS_DRAFT    NUMBER(2) DEFAULT 0,
  IS_DYNAMIC  NUMBER(2) DEFAULT 0,
  A_ID        NUMBER(38),
  CORP_CODE   VARCHAR2(64) not null,
  RIMG_IDS    VARCHAR2(255),
  KEY_WORDSVO VARCHAR2(255),
  CREATETIME  TIMESTAMP(8) not null,
  MODIFYTIME  TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_TEMPLATE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_TEMPLATE
	  add constraint PK_LF_WC_TEMPLATE primary key (T_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_TLINK
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_TLINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_TLINK
(
  K_ID NUMBER(38) not null,
  T_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_TLINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_TLINK
	  add constraint PK_LF_WC_TLINK primary key (K_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_ULINK
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_ULINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_ULINK
(
  U_ID  NUMBER(38) not null,
  WC_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_ULINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_ULINK
	  add constraint PK_LF_WC_ULINK primary key (U_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_USERINFO
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_USERINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_USERINFO
(
  WC_ID      NUMBER(38) not null,
  FAKE_ID    VARCHAR2(32),
  NAME       VARCHAR2(20),
  CODE       VARCHAR2(15),
  IMG        VARCHAR2(128),
  TYPE       NUMBER(2) DEFAULT 0,
  SIGNATURE  VARCHAR2(256),
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8) not null,
  MODIFYTIME TIMESTAMP(8),
  PHONE      VARCHAR2(30),
  UNAME      VARCHAR2(24),
  DESCR      VARCHAR2(512),
  VERIFYTIME TIMESTAMP(8),
  U_ID       NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_USERINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_USERINFO
	  add constraint PK_LF_WC_USERINFO primary key (WC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WC_VERIFY
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WC_VERIFY';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WC_VERIFY
(
  VF_ID        NUMBER(38) not null,
  WC_ID        NUMBER(38),
  VERIFYCODE   VARCHAR2(6),
  CODETIME     TIMESTAMP(8),
  PHONE        VARCHAR2(30),
  LIMTTIME     TIMESTAMP(8),
  VERIFY_COUNT NUMBER(3),
  CORP_CODE    VARCHAR2(64) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WC_VERIFY') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WC_VERIFY
	  add constraint PK_LF_WC_VERIFY primary key (VF_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEIX_EMOJI
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEIX_EMOJI';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEIX_EMOJI
(
  ID         NUMBER(38) not null,
  SUNICODE   VARCHAR2(100),
  SUTF8      VARCHAR2(100),
  SUTF16     VARCHAR2(100),
  SSBUNICODE VARCHAR2(100),
  FILENAME   VARCHAR2(100)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEIX_EMOJI') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEIX_EMOJI
	  add primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WGPARAMCONFIG
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WGPARAMCONFIG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WGPARAMCONFIG
(
  PID         NUMBER(38) not null,
  PARAM       VARCHAR2(64),
  PARAMVALUE  VARCHAR2(64),
  PARAMNAME   VARCHAR2(64),
  CORPCODE    VARCHAR2(64),
  MEMO        VARCHAR2(200),
  PARAMSUBNUM NUMBER(1)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WGPARAMCONFIG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WGPARAMCONFIG
	  add constraint PK_LF_WGPARAMCONFIG primary key (PID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_WGPARAMCONFIG') AND T.INDEX_NAME = UPPER('LF_WGPARAMCONFIG_I_PARAMSUBNUM');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_WGPARAMCONFIG_I_PARAMSUBNUM on LF_WGPARAMCONFIG (PARAMSUBNUM)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_WGPARAMCONFIG') AND T.INDEX_NAME = UPPER('LF_WGPARAMCONFIG_I_PARAMVALUE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'create index LF_WGPARAMCONFIG_I_PARAMVALUE on LF_WGPARAMCONFIG (PARAMVALUE)
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WGPARAMDEFINITION
-- ===================================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WGPARAMDEFINITION';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WGPARAMDEFINITION
(
  PID          NUMBER(38) not null,
  PARAM        VARCHAR2(64),
  PARAMSUBNUM  NUMBER(1),
  PARAMSUBSIGN VARCHAR2(64),
  PARAMSUBNAME VARCHAR2(64),
  CORPCODE     VARCHAR2(64),
  MEMO         VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WGPARAMDEFINITION') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WGPARAMDEFINITION
	  add constraint PK_LF_WGPARAMDEFINITION primary key (PID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WX_BASEINFO
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_BASEINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_BASEINFO
(
  ID             NUMBER(38) not null,
  NETID          NUMBER(38) not null,
  NAME           VARCHAR2(100) not null,
  SORT           NUMBER(10) not null,
  TIMETYPE       NUMBER(10) not null,
  TIMEOUT        TIMESTAMP(8),
  WXRIGHT        NUMBER(10) not null,
  TIMEEND        TIMESTAMP(8),
  URL            VARCHAR2(100),
  STATUS         NUMBER(10) not null,
  AUDITUSERID    VARCHAR2(20),
  AUDITDATE      TIMESTAMP(8),
  WXTYPE         NUMBER(10) not null,
  IMAGE          VARCHAR2(100),
  MODIFYID       NUMBER(38),
  MODIFYDATE     TIMESTAMP(8),
  CREATID        NUMBER(38) not null,
  CREATDATE      TIMESTAMP(8) not null,
  SMS            VARCHAR2(500),
  WXSHARE        NUMBER(10) not null,
  CORP_CODE      VARCHAR2(64),
  DATA_TABLENAME VARCHAR2(50),
  DYN_TABLENAME  VARCHAR2(50),
  TEMPTYPE       NUMBER(2),
  PARAMS         VARCHAR2(256),
  HASPARAMS      VARCHAR2(256),
  OPERAPPSTATUS  NUMBER(20) DEFAULT 0,
  OPERAPPNOTE    VARCHAR2(400)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WX_BASEINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WX_BASEINFO
	  add constraint PK_LF_WX_BASEINFO primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WX_DATA
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_DATA';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_DATA
(
  DID          NUMBER(38) not null,
  CODE         VARCHAR2(64) not null,
  NAME         VARCHAR2(50) not null,
  DATATYPEID   NUMBER(38) not null,
  REPLYSETTYPE NUMBER(2) not null,
  QUESTYPE     NUMBER(2) not null,
  QUESCONTENT  VARCHAR2(1024) not null,
  COLNAME      VARCHAR2(20) not null,
  USERID       NUMBER(38) not null,
  COLTYPE      NUMBER(2),
  COLSIZE      NUMBER(10),
  STATUS       NUMBER(2),
  MODIFYDATE   TIMESTAMP(8),
  CREATDATE    TIMESTAMP(8),
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WX_DATA') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WX_DATA
	  add constraint PK_LF_WX_DATA primary key (DID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WX_DATATYPE
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_DATATYPE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_DATATYPE
(
  ID         NUMBER(38) not null,
  NAME       VARCHAR2(50) not null,
  UPDATETIME TIMESTAMP(8),
  CREATDATE  TIMESTAMP(8),
  CORP_CODE  VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WX_DATATYPE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WX_DATATYPE
	  add constraint PK_LF_WX_DATATYPE primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WX_DATA_BIND
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_DATA_BIND';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_DATA_BIND
(
  NETID NUMBER(38) not null,
  DID   NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_WX_DATA_CHOS
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_DATA_CHOS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_DATA_CHOS
(
  DID  NUMBER(38) not null,
  NAME VARCHAR2(50) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_WX_PAGE
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_PAGE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_PAGE
(
  ID         NUMBER(38) not null,
  NETID      NUMBER(38) not null,
  NAME       VARCHAR2(50),
  PARENTID   NUMBER(38) not null,
  CHILDID    NUMBER(38),
  CONTENT    VARCHAR2(4000),
  PAGESIZE   NUMBER(10),
  MODIFYID   NUMBER(38),
  MODIFYDATE TIMESTAMP(8),
  CREATID    NUMBER(38) not null,
  CREATDATE  TIMESTAMP(8),
  LINK       VARCHAR2(100)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WX_PAGE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WX_PAGE
	  add constraint PK_LF_WX_PAGE primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WX_SENDCOUNT
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_SENDCOUNT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_SENDCOUNT
(
  ID        NUMBER(38) not null,
  REFID     NUMBER(38) not null,
  MSGID     VARCHAR2(30) not null,
  TYPE      NUMBER(10) not null,
  PHONE     VARCHAR2(30),
  STATUS    NUMBER(10),
  SENDTIME  TIMESTAMP(8),
  USERID    NUMBER(38),
  SUBTOTLE  NUMBER(38) not null,
  NETNAME   VARCHAR2(255),
  CORP_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WX_SENDCOUNT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WX_SENDCOUNT
	  add constraint PK_LF_WX_SENDCOUNT primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WX_SORT
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_SORT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_SORT
(
  ID          NUMBER(38) not null,
  NAME        VARCHAR2(20) not null,
  DESCRIPTION VARCHAR2(100),
  TYPE        NUMBER(10) not null,
  MODIFYID    NUMBER(38),
  MODIFYDATE  TIMESTAMP(8),
  CREATID     NUMBER(38),
  CREATDATE   TIMESTAMP(8),
  PARENTID    NUMBER(38) not null,
  FILETYPE    NUMBER(10),
  CORP_CODE   VARCHAR2(64),
  SORT_PATH   VARCHAR2(160)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WX_SORT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WX_SORT
	  add constraint PK_LF_WX_SORT primary key (ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WX_UPLOADFILE
-- ===============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_UPLOADFILE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_UPLOADFILE
(
  ID           NUMBER(38) not null,
  NETID        NUMBER(38) not null,
  FILENAME     VARCHAR2(100) not null,
  FILEVER      VARCHAR2(20),
  FILEDESC     VARCHAR2(100),
  UPLOADDATE   TIMESTAMP(8) not null,
  UPLOADUSERID NUMBER(38) not null,
  WEBURL       VARCHAR2(200) not null,
  STATUS       NUMBER(10) not null,
  MODIFYID     NUMBER(38),
  MODIFYDATE   TIMESTAMP(8),
  CREATID      NUMBER(38),
  CREATDATE    TIMESTAMP(8),
  SORTID       VARCHAR2(10) not null,
  NAMETEMP     VARCHAR2(50),
  TYPE         VARCHAR2(20),
  FILESIZE     NUMBER(10),
  CORP_CODE    VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_WX_USERFEE
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_USERFEE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_USERFEE
(
  "Uid"       NUMBER(38) not null,
  USERID    NUMBER(38) not null,
  USERNAME  VARCHAR2(64),
  SENDNUM   NUMBER(10) not null,
  SENDEDNUM NUMBER(10) not null,
  ECID      NUMBER(38) not null,
  CORP_CODE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_WX_VISITLOG
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WX_VISITLOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WX_VISITLOG
(
  ID          NUMBER(38) not null,
  REFID       NUMBER(38) not null,
  TYPE        NUMBER(10),
  PHONE       VARCHAR2(30),
  VISITDATE   TIMESTAMP(8) not null,
  VISITSTATUS NUMBER(10) not null,
  MEMO        VARCHAR2(100),
  "uid"         NUMBER(38),
  USERAGENT   VARCHAR2(1024),
  IPADDR      VARCHAR2(25),
  CORP_CODE   VARCHAR2(64),
  TASKID      NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_LBS_PIOS
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_LBS_PIOS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_LBS_PIOS
(
  P_ID       NUMBER(38) not null,
  CITY_ID    NUMBER(3),
  LAT        VARCHAR2(64) not null,
  LNG        VARCHAR2(64) not null,
  TITLE      VARCHAR2(64),
  KEYWORD    VARCHAR2(64),
  TELEPHONE  VARCHAR2(32),
  ADDRESS    VARCHAR2(128),
  NOTE       VARCHAR2(512),
  A_ID       VARCHAR2(128) not null,
  CORP_CODE  VARCHAR2(64) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8),
  DISTANCE   VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_LBS_PIOS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_LBS_PIOS
	  add primary key (P_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_LBS_PUSHSET
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_LBS_PUSHSET';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_LBS_PUSHSET
(
  PUSH_ID    NUMBER(38) not null,
  PUSHTYPE   NUMBER(3),
  IMGURL     VARCHAR2(256),
  NOTE       VARCHAR2(512),
  RADIUS     VARCHAR2(16),
  AUTORADIUS NUMBER(3),
  PUSHCOUNT  NUMBER(3),
  AUTOMORE   NUMBER(3),
  CORP_CODE  VARCHAR2(6) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_LBS_PUSHSET') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_LBS_PUSHSET
	  add primary key (PUSH_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_LBS_USERPIOS
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_LBS_USERPIOS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_LBS_USERPIOS
(
  UP_ID      NUMBER(38) not null,
  LAT        VARCHAR2(64) not null,
  LNG        VARCHAR2(64) not null,
  A_ID       VARCHAR2(128) not null,
  OPENID     VARCHAR2(128) not null,
  CORP_CODE  VARCHAR2(6) not null,
  MODITYTIME TIMESTAMP(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_LBS_USERPIOS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_LBS_USERPIOS
	  add primary key (UP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ONL_GPMEM
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONL_GPMEM';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONL_GPMEM
(
  GP_ID    NUMBER(38) not null,
  GM_USER  NUMBER(38),
  GM_STATE NUMBER(3) DEFAULT (1)	--是否有效的状态1-有效，0-失效
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_ONL_GPMSGID
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONL_GPMSGID';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONL_GPMSGID
(
  GM_USER     NUMBER(38),
  MSG_ID      NUMBER(38) DEFAULT (0),
  UPDATE_TIME TIMESTAMP(8) DEFAULT sysdate -- 更新时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_ONL_GROUP
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONL_GROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONL_GROUP
(
  GP_ID       NUMBER(38) not null,
  GP_NAME     VARCHAR2(32),
  CREATE_USER NUMBER(38),
  CREATE_TIME TIMESTAMP(8),
  MEM_COUNT   NUMBER(10) DEFAULT 0				--群组人数
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ONL_GROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ONL_GROUP
	  add primary key (GP_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ONL_MSG
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONL_MSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONL_MSG
(
  M_ID       NUMBER(38) not null,
  A_ID       NUMBER(38),
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SERVER_NUM VARCHAR2(32),
  MESSAGE    VARCHAR2(2048),
  SEND_TIME  TIMESTAMP(8),
  MSG_TYPE   VARCHAR2(10),
  PUSH_TYPE  NUMBER(3) DEFAULT 0				--11-手机to客服，2-客服to手机，3-客服to客服，4群组
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ONL_MSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ONL_MSG
	  add primary key (M_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ONL_MSG_HIS
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONL_MSG_HIS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONL_MSG_HIS
(
  M_ID       NUMBER(38) not null,
  A_ID       NUMBER(38),
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SERVER_NUM VARCHAR2(32),
  MESSAGE    VARCHAR2(2048),
  SEND_TIME  TIMESTAMP(8),
  MSG_TYPE   VARCHAR2(10),
  PUSH_TYPE  NUMBER(3) DEFAULT 0				--11-手机to客服，2-客服to手机，3-客服to客服，4群组
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ONL_MSG_HIS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ONL_MSG_HIS
	  add primary key (M_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_ONL_REMARK
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONL_REMARK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONL_REMARK
(
  USER_ID   NUMBER(38) not null,
  MARK_NAME VARCHAR2(50),
  MARK_ID   NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_ONL_SERVER
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_ONL_SERVER';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_ONL_SERVER
(
  SER_ID      NUMBER(38) not null,
  SER_NUM     VARCHAR2(32),
  SERTYPE     NUMBER(20),
  CUSTOME_ID  NUMBER(38),
  A_ID        NUMBER(38),
  CREATE_TIME TIMESTAMP(8),
  END_TIME    TIMESTAMP(8),
  FROM_USER   VARCHAR2(50),
  SCORE       NUMBER(10),
  EVALUATE    VARCHAR2(256),
  DURATION    NUMBER(10)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_ONL_SERVER') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_ONL_SERVER
	  add primary key (SER_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SIT_INFO
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SIT_INFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SIT_INFO
(
  S_ID       NUMBER(38) not null,
  TYPE_ID    NUMBER(38),
  NAME       VARCHAR2(32),
  URL        VARCHAR2(128),
  IS_SYSTEM  NUMBER(3) DEFAULT 0,
  CORP_CODE  VARCHAR2(10) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SIT_INFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SIT_INFO
	  add primary key (S_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SIT_PAGE
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SIT_PAGE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SIT_PAGE
(
  PAGE_ID    NUMBER(38) not null,
  S_ID       NUMBER(38),
  PAGE_TYPE  VARCHAR2(32),
  NAME       VARCHAR2(32),
  URL        VARCHAR2(128),
  SEQ_NUM    NUMBER(10),
  CORP_CODE  VARCHAR2(10) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SIT_PAGE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SIT_PAGE
	  add primary key (PAGE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SIT_PLANT
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SIT_PLANT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SIT_PLANT
(
  PLANT_ID     NUMBER(38) not null,
  PLANT_TYPE   VARCHAR2(32),
  PAGE_ID      NUMBER(38),
  NAME         VARCHAR2(32),
  FEILD_NAMES  VARCHAR2(64),
  FEILD_VALUES VARCHAR2(4000),
  CORP_CODE    VARCHAR2(10) not null,
  CREATETIME   TIMESTAMP(8),
  MODITYTIME   TIMESTAMP(8),
  S_ID         NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SIT_PLANT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SIT_PLANT
	  add primary key (PLANT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_SIT_TYPE
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SIT_TYPE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_SIT_TYPE
(
  TYPE_ID    NUMBER(38) not null,
  NAME       VARCHAR2(32),
  IS_DEFAULT NUMBER(3),
  PATTERN    NUMBER(10) DEFAULT 0,
  IMG_URL    VARCHAR2(64),
  IMG_URL0   VARCHAR2(64),
  IMG_URL1   VARCHAR2(64),
  IMG_URL2   VARCHAR2(64),
  IMG_URL3   VARCHAR2(64),
  IS_SYSTEM  NUMBER(3) DEFAULT 0,
  SEQ_NUM    NUMBER(10),
  CORP_CODE  VARCHAR2(10) not null,
  CREATETIME TIMESTAMP(8),
  MODITYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SIT_TYPE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_SIT_TYPE
	  add primary key (TYPE_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_ACCOUNT
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_ACCOUNT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_ACCOUNT
(
  A_ID         NUMBER(38) not null,
  FAKE_ID      VARCHAR2(32),
  NAME         VARCHAR2(64),
  CODE         VARCHAR2(25) not null,
  OPEN_ID      VARCHAR2(64) not null,
  IMG          VARCHAR2(128),
  TYPE         NUMBER(3),
  IS_APPROVE   NUMBER(3) DEFAULT 0,
  QRCODE       VARCHAR2(128),
  INFO         VARCHAR2(2048),
  URL          VARCHAR2(64),
  TOKEN        VARCHAR2(32),
  BINDTIME     TIMESTAMP(8),
  CORP_CODE    VARCHAR2(6) not null,
  CREATETIME   TIMESTAMP(8) not null,
  MODIFYTIME   TIMESTAMP(8),
  GRANT_TYPE   VARCHAR2(64),
  APPID        VARCHAR2(64),
  SECRET       VARCHAR2(64),
  ACCESS_TOKEN VARCHAR2(256),
  ACCESS_TIME  TIMESTAMP(8),
  RELEASE_TIME TIMESTAMP(8),
  ACCESS_COUNT VARCHAR2(64),
  SYNC_STATE   NUMBER(3) DEFAULT 0,
  SYNC_TIME    TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_ACCOUNT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_ACCOUNT
	  add primary key (A_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_COUNT
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_COUNT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_COUNT
(
  COUNT_ID       NUMBER(38) not null,
  UNFOLLOW_COUNT NUMBER(10),
  FOLLOW_COUNT   NUMBER(10),
  INCOME_COUNT   NUMBER(10),
  AMOUNT_COUNT   NUMBER(10),
  DAYTIME        TIMESTAMP(8),
  A_ID           NUMBER(38),
  CORP_CODE      VARCHAR2(64) not null,
  MODIFYTIME     TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_COUNT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_COUNT
	  add primary key (COUNT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_GROUP
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_GROUP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_GROUP
(
  G_ID       NUMBER(38) not null,
  WG_ID      NUMBER(38),
  NAME       VARCHAR2(32),
  COUNT      VARCHAR2(11),
  A_ID       NUMBER(38) not null,
  CORP_CODE  VARCHAR2(6) not null,
  CREATETIME TIMESTAMP(8) not null,
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_GROUP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_GROUP
	  add primary key (G_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_KEYWORD
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_KEYWORD';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_KEYWORD
(
  K_ID       NUMBER(38) not null,
  NAME       VARCHAR2(32) not null,
  TYPE       NUMBER(3) DEFAULT 0,
  A_ID       NUMBER(38),
  CORP_CODE  VARCHAR2(6) not null,
  CREATETIME TIMESTAMP(8) not null,
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_KEYWORD') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_KEYWORD
	  add primary key (K_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_MENU
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='OT_WEI_MENU';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table OT_WEI_MENU
(
  M_ID       NUMBER(38) not null,
  M_NAME     VARCHAR2(32) not null,
  M_TYPE     NUMBER(10) not null,
  M_KEY      VARCHAR2(64),
  M_URL      VARCHAR2(256),
  M_HIDDEN   NUMBER(10),
  M_ORDER    NUMBER(10) not null,
  P_ID       NUMBER(38),
  T_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000),
  A_ID       NUMBER(38) not null,
  CORP_CODE  VARCHAR2(6) not null,
  CREATETIME TIMESTAMP(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_WEI_MSG
-- =========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_MSG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_MSG
(
  MSG_ID     NUMBER(38) not null,
  MSG_TYPE   NUMBER(3) not null,
  WC_ID      NUMBER(38) not null,
  A_ID       NUMBER(38) not null,
  TYPE       NUMBER(3) not null,
  MSG_XML    VARCHAR2(4000) not null,
  MSG_TEXT   VARCHAR2(4000) not null,
  PARENT_ID  NUMBER(38),
  CORP_CODE  VARCHAR2(6) not null,
  CREATETIME TIMESTAMP(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_MSG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_MSG
	  add primary key (MSG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_REVENT
-- ============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_REVENT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_REVENT
(
  EVT_ID     NUMBER(38) not null,
  EVT_TYPE   NUMBER(3),
  T_ID       NUMBER(38),
  A_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000) not null,
  CORP_CODE  VARCHAR2(6) not null,
  CREATETIME TIMESTAMP(8) not null,
  TITLE      VARCHAR2(64),
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_REVENT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_REVENT
	  add primary key (EVT_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_RIMG
-- ==========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_RIMG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_RIMG
(
  RIMG_ID     NUMBER(38) not null,
  TITLE       VARCHAR2(64) not null,
  DESCRIPTION VARCHAR2(4000),
  PICURL      VARCHAR2(128),
  LINK        VARCHAR2(128),
  A_ID        NUMBER(38),
  CORP_CODE   VARCHAR2(6) not null,
  CREATETIME  TIMESTAMP(8) not null,
  MODIFYTIME  TIMESTAMP(8),
  SOURCE_URL  VARCHAR2(256),
  SUMMARY     VARCHAR2(200)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_RIMG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_RIMG
	  add primary key (RIMG_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_RTEXT
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_RTEXT';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_RTEXT
(
  TET_ID     NUMBER(38) not null,
  TET_TYPE   NUMBER(3) DEFAULT 0,
  T_ID       NUMBER(38),
  MSG_TEXT   VARCHAR2(4000),
  MSG_XML    VARCHAR2(4000) not null,
  A_ID       NUMBER(38),
  CORP_CODE  VARCHAR2(6) not null,
  CREATETIME TIMESTAMP(8) not null,
  TITLE      VARCHAR2(64),
  MODIFYTIME TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_RTEXT') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_RTEXT
	  add primary key (TET_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_SENDLOG
-- =============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_SENDLOG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_SENDLOG
(
  SEND_ID      NUMBER(38) not null,
  TP           VARCHAR2(11),
  MSG_TYPE     VARCHAR2(15),
  AREA_VALUE   VARCHAR2(64),
  POST_MSG     VARCHAR2(2048) not null,
  T_ID         NUMBER(38),
  A_ID         NUMBER(38),
  CORP_CODE    VARCHAR2(64) not null,
  CREATETIME   TIMESTAMP(8) not null,
  EVENTDATA    VARCHAR2(2048),
  STATUS       NUMBER(3),
  MSG_ID       VARCHAR2(15),
  RESPONSE_MSG VARCHAR2(1024),
  SEND_CONTENT VARCHAR2(1024)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_SENDLOG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_SENDLOG
	  add primary key (SEND_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_TEMPLATE
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_TEMPLATE';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_TEMPLATE
(
  T_ID        NUMBER(38) not null,
  T_NAME      VARCHAR2(64) not null,
  MSG_TYPE    NUMBER(3),
  MSG_XML     VARCHAR2(4000),
  MSG_TEXT    VARCHAR2(4000),
  IS_PUBLIC   NUMBER(3) DEFAULT 0,
  IS_DRAFT    NUMBER(3) DEFAULT 0,
  IS_DYNAMIC  NUMBER(3) DEFAULT 0,
  A_ID        NUMBER(38),
  CORP_CODE   VARCHAR2(6) not null,
  RIMG_IDS    VARCHAR2(255),
  KEY_WORDSVO VARCHAR2(255),
  CREATETIME  TIMESTAMP(8) not null,
  MODIFYTIME  TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_TEMPLATE') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_TEMPLATE
	  add primary key (T_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_TLINK
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_TLINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_TLINK
(
  K_ID NUMBER(38) not null,
  T_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_TLINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_TLINK
	  add primary key (K_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_ULINK
-- ===========================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_ULINK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_ULINK
(
  U_ID  NUMBER(38) not null,
  WC_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_ULINK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_ULINK
	  add primary key (U_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_WEI_USER2ACC
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_USER2ACC';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_USER2ACC
(
  A_ID    NUMBER(38) not null,
  USER_ID NUMBER(38) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

--
-- Creating table LF_WEI_USERINFO
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEI_USERINFO';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
create table LF_WEI_USERINFO
(
  WC_ID          NUMBER(38) not null,
  FAKE_ID        VARCHAR2(32),
  G_ID           NUMBER(38),
  SUBSCRIBE      VARCHAR2(11),
  NICK_NAME      VARCHAR2(32),
  SEX            VARCHAR2(11),
  CITY           VARCHAR2(15),
  PROVINCE       VARCHAR2(15),
  COUNTRY        VARCHAR2(15),
  LANGUAGE       VARCHAR2(15),
  HEAD_IMG_URL   VARCHAR2(512),
  SUBSCRIBE_TIME TIMESTAMP(8),
  UNAME          VARCHAR2(32),
  PHONE          VARCHAR2(30),
  DESCR          VARCHAR2(512),
  VERIFYTIME     TIMESTAMP(8),
  U_ID           NUMBER(38),
  CORP_CODE      VARCHAR2(6) not null,
  CREATETIME     TIMESTAMP(8) not null,
  MODIFYTIME     TIMESTAMP(8),
  OPEN_ID        VARCHAR2(64),
  A_ID           NUMBER(38),
  LOCAL_IMG_URL  VARCHAR2(512),
  GUID           NUMBER(38)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEI_USERINFO') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'alter table LF_WEI_USERINFO
	  add primary key (WC_ID)
	  using index 
	  tablespace EMP_TABLESPACE
	  pctfree 10
	  initrans 2
	  maxtrans 255
	  storage
	  (
		initial 64K
		minextents 1
		maxextents unlimited
	  )';
	END IF;
END;
/

--
-- Creating table LF_THIR_CORP
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_THIR_CORP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_THIR_CORP(
    ID         NUMBER(38) NOT NULL,
    MENU_NUM   NUMBER(15) DEFAULT 0 NOT NULL,
    CORP_CODE  VARCHAR2(64) DEFAULT '' '' NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_THIR_CORP') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_THIR_CORP
		ADD CONSTRAINT PK_LF_THIR_CORP PRIMARY KEY (ID)
		USING INDEX
		TABLESPACE EMP_TABLESPACE
		PCTFREE 10
		INITRANS 2
		MAXTRANS 255
		STORAGE
		(
		  INITIAL 64K
		  MINEXTENTS 1
		  MAXEXTENTS UNLIMITED
		)';
	END IF;
END;
/

--
-- Creating table LF_WEBCONFIG
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_WEBCONFIG';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_WEBCONFIG(
  ID         NUMBER(38) NOT NULL,
  CONFIG_NAME   VARCHAR2(255) DEFAULT '' '' NOT NULL,
  JSON_CONFIG  VARCHAR2(2000) DEFAULT '' '' NOT NULL,
  CORP_CODE  VARCHAR2(64) DEFAULT '' '' NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_WEBCONFIG') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_WEBCONFIG
		ADD CONSTRAINT PK_LF_WEBCONFIG PRIMARY KEY (ID)
		USING INDEX
		TABLESPACE EMP_TABLESPACE
		PCTFREE 10
		INITRANS 2
		MAXTRANS 255
		STORAGE
		(
		  INITIAL 64K
		  MINEXTENTS 1
		  MAXEXTENTS UNLIMITED
		)';
	END IF;
END;
/

--
-- Creating table LF_TEMP_SYNCH
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TEMP_SYNCH';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_TEMP_SYNCH(
  ID         NUMBER(38) NOT NULL,
  SP_TEMPLID    NUMBER(38) DEFAULT NULL,
  COUNT   NUMBER(5) DEFAULT 0 NOT NULL,
  SYNSTATUS  NUMBER(5) DEFAULT 0 NOT NULL,
  CAUSE  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  ISMATERIAL  NUMBER(11) DEFAULT 0 NOT NULL,-- lf_temp_synch[模板同步状态记录表]添加 ISMATERIAL 字段[是否素材：0-否，1-是]
  UPDATE_TIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TEMP_SYNCH') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_TEMP_SYNCH
		ADD CONSTRAINT PK_LF_TEMP_SYNCH PRIMARY KEY (ID)
		USING INDEX
		TABLESPACE EMP_TABLESPACE
		PCTFREE 10
		INITRANS 2
		MAXTRANS 255
		STORAGE
		(
		  INITIAL 64K
		  MINEXTENTS 1
		  MAXEXTENTS UNLIMITED
		)';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_TEMP_SYNCH') AND T.INDEX_NAME = UPPER('UN_LF_TEMP_SYNCH_SP_TEMPLID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_TEMP_SYNCH
	  ADD CONSTRAINT UN_LF_TEMP_SYNCH_SP_TEMPLID UNIQUE (SP_TEMPLID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 2
	  MAXTRANS 255
	  STORAGE
	  (
		INITIAL 64K
		MINEXTENTS 1
		MAXEXTENTS UNLIMITED
	  )';
	END IF;
END;
/

-- 短链相关建表语句 Start
--
-- Creating table LF_URLTASK(短链接发送任务表)
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_URLTASK';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_URLTASK (
      ID NUMBER(18) NOT NULL,    -- 主键ID
      TASKID NUMBER(18) DEFAULT 0 NOT NULL,    -- 任务批次号
      SPUSER VARCHAR2(11) DEFAULT '' '' NOT NULL, --SP账号
      CORP_CODE VARCHAR2(64) DEFAULT '' '' NOT NULL,    -- 企业编码
      TITLE VARCHAR2(64) DEFAULT '' '' NOT NULL,    -- 发送主题
      MSG VARCHAR2(2048) NOT NULL,    -- 短信内容
      UTYPE NUMBER(5) NOT NULL,    -- 1.相同内容短链接发送 2.不同内容短链接发送
      PLAN_TIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
      SUBMIT_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 提交时间
      SEND_TM TIMESTAMP(6) NOT NULL,    -- 发送时间
      HANDLE_NODE VARCHAR2(50) DEFAULT '' '' NOT NULL,
      HANDLE_LINE NUMBER(18) DEFAULT 0 NOT NULL,
      SUB_COUNT NUMBER(18) DEFAULT 0 NOT NULL,    -- 提交个数
      SRC_FILE VARCHAR2(1024) DEFAULT '' '' NOT NULL,    -- 源文件地址
      URL_FILE VARCHAR2(1024) DEFAULT '' '' NOT NULL,    -- 包含短地址内容文件地址
      STATUS NUMBER(5) DEFAULT 0 NOT NULL,    -- 状态  无定时     10：未处理   11：处理中   12：处理完成   13 : 处理失败  定时发送   20 ：未处理  21：处理中   22：处理完成   23 : 处理失败
      CREATE_UID NUMBER(20) DEFAULT 0 NOT NULL,    -- 创建人员ID
      CREATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 创建时间
      UPDATE_UID NUMBER(20) DEFAULT 0 NOT NULL,    -- 修改人员ID
      UPDATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 修改时间
      NETURL VARCHAR2(1024) DEFAULT '' '',    -- 企业长地址
      NETURL_ID NUMBER(18) DEFAULT 0 NOT NULL,    -- 企业长地址ID
      DOMAIN_ID NUMBER(18) DEFAULT 0 NOT NULL,
      DOMAIN_URL VARCHAR2(256) DEFAULT '' '' NOT NULL,    -- 短地址ID
      DOMAIN_EXTEN NUMBER(11),    -- 短地址扩展部分长度
      DOMAIN_LEN NUMBER(20) DEFAULT 0 NOT NULL,    -- 短地址总长度
      VALID_DAYS NUMBER(5) DEFAULT 0 NOT NULL,    -- 有效期
      REMARK VARCHAR2(256) DEFAULT '' '' NOT NULL,    -- 扩展信息
      REMARK1 VARCHAR2(256) NOT NULL,    -- 扩展信息2
      STR_TASKID VARCHAR2(20) DEFAULT '' '' NOT NULL
    )
    TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 1
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_URLTASK') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_URLTASK
      ADD CONSTRAINT PK_LF_URLTASK PRIMARY KEY (ID)
      USING INDEX
      TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 2
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
	END IF;
END;
/
 
DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_URLTASK') AND T.INDEX_NAME = UPPER('INDEX_LF_URLTASK_TASKID');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_LF_URLTASK_TASKID ON LF_URLTASK (TASKID)
      TABLESPACE EMP_TABLESPACE
          PCTFREE 10
          INITRANS 2
          MAXTRANS 255
          STORAGE
          (
            INITIAL 64K
            MINEXTENTS 1
            MAXEXTENTS UNLIMITED
          )';
	END IF;
END;
/
 
--
-- Creating table LF_NETURL(连接地址维护表)
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_NETURL';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_NETURL (
      ID NUMBER(18) NOT NULL,    -- id主键
      SRC_URL VARCHAR2(1024) DEFAULT '' '',    -- 源地址
      URL_NAME VARCHAR2(200) DEFAULT '' '' NOT NULL,    -- 连接名称
      URL_MSG VARCHAR2(1024) DEFAULT '' '' NOT NULL,    -- 源地址内容描述
      CORP_CODE VARCHAR2(64) DEFAULT 0 NOT NULL,    -- 企业编码
      ISPASS NUMBER(11) DEFAULT 0 NOT NULL,    -- 审批状态   -3 禁用（未调接口）, -2 禁用  -1:删除   0：未审批, 1：无需审批 ,2：审批通过 （运营商侧）
      URLSTATE NUMBER(11) DEFAULT 0 NOT NULL,    -- 长地址状态企业内部维护使用   -2禁用（未调接口）， -1：禁用 0：启用 （用户侧）
      CREATE_UID NUMBER(18) NOT NULL,    -- 创建人
      CREATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 创建时间
      UPDATE_UID NUMBER(18) NOT NULL,  -- 修改人员ID （用户侧）
      UPDATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 修改时间 （用户侧）
      AUDIT_UID NUMBER(18) ,--审核人
      AUDIT_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,--审核时间
      STOP_UID NUMBER(18) , -- 禁用人员ID （运营商测）
      STOP_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,-- 禁用时间
      REMARKS VARCHAR2(256) DEFAULT '' '' NOT NULL,    -- 审核意见
      REMARKS1 VARCHAR2(256) DEFAULT '' '' NOT NULL,--预留字段1
      REMARKS2 VARCHAR2(256) DEFAULT '' '' NOT NULL --预留字段2
    )
     TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 1
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_NETURL') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_NETURL
      ADD CONSTRAINT PK_LF_NETURL PRIMARY KEY (ID)
      USING INDEX
      TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 2
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_NETURL') AND T.INDEX_NAME = UPPER('INDEX_LF_NETURL_URL_NAME');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_LF_NETURL_URL_NAME ON LF_NETURL (URL_NAME)
    TABLESPACE EMP_TABLESPACE
    PCTFREE 10
    INITRANS 2
    MAXTRANS 255
    STORAGE
    (
      INITIAL 64K
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    )';
	END IF;
END;
/

DECLARE 
ICOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO ICOUNT FROM USER_INDEXES T WHERE T.TABLE_NAME = UPPER('LF_NETURL') AND T.INDEX_NAME = UPPER('INDEX_LF_NETURL_CORP_CODE');     
	IF ICOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'CREATE INDEX INDEX_LF_NETURL_CORP_CODE ON LF_NETURL (CORP_CODE)
      TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 2
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
	END IF;
END;
/

--
-- Creating table LF_DOMAIN(短域名信息表)
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DOMAIN';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_DOMAIN (
        ID NUMBER(18) NOT NULL,    -- 短域名id
        DOMAIN VARCHAR2(256) DEFAULT '' '' NOT NULL,    -- 短域名
        LEN_ALL NUMBER(11) NOT NULL,    -- 总长度
        LEN_EXTEN NUMBER(18) DEFAULT 0 NOT NULL,    -- 全局扩展位数
        DTYPE NUMBER(11) DEFAULT 0,    -- 域名类别  0 公用  1专用
        FLAG NUMBER(11) DEFAULT 0 NOT NULL,    -- 域名状态  0  有效    -1 无效
        VALID_DAYS NUMBER(18) DEFAULT 30 NOT NULL,    -- 有效时间，单位天 , 30  ,值不能为0
        CREATE_UID NUMBER(18) NOT NULL,    -- 创建人员
        CREATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 创建时间
        UPDATE_UID NUMBER(18) NOT NULL,    -- 最后修改人员ID
        UPDATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,    -- 最后修改时间
        REMARK VARCHAR2(256) NOT NULL,
        CREATE_USER VARCHAR2(64) DEFAULT '' '' NOT NULL  -- 创建用户
    )
    TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 1
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_DOMAIN') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_DOMAIN
      ADD CONSTRAINT PK_LF_DOMAIN PRIMARY KEY (ID)
      USING INDEX
      TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 2
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
	END IF;
END;
/

--
-- Creating table LF_DOMAIN_CORP(企业域名映射表)
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_DOMAIN_CORP';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_DOMAIN_CORP (
        CORP_CODE VARCHAR2(64) NOT NULL,   --企业编码
        DOMAIN_ID NUMBER(18) NOT NULL,    -- 域名ID
        CREATE_UID NUMBER(18) DEFAULT 0 NOT NULL,    -- 创建人
        CREATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,   -- 创建时间
        UPDATE_UID NUMBER(18) DEFAULT 0 NOT NULL,   -- 修改人
        UPDATE_TM TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,   -- 修改时间
        FLAG NUMBER(11) DEFAULT 0 NOT NULL,   -- 状态 0表示启用,1表示禁用
        CREATE_USER VARCHAR2(64) DEFAULT '' '' NOT NULL   -- 创建用户
      )
      TABLESPACE EMP_TABLESPACE
      PCTFREE 10
      INITRANS 1
      MAXTRANS 255
      STORAGE
      (
        INITIAL 64K
        MINEXTENTS 1
        MAXEXTENTS UNLIMITED
      )';
  END IF;
END;
/

--
-- Creating table LF_SUB_DRAFTS(子草稿箱存储domainid ,neturlid)
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_SUB_DRAFTS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_SUB_DRAFTS(
    ID         NUMBER(38) NOT NULL,
    DRAFTID NUMBER(11) DEFAULT 0 NOT NULL,
    TYPE  NUMBER(3) DEFAULT 0 NOT NULL,
    NETURLID  NUMBER(11) DEFAULT 0 NOT NULL,
    DOMAINID  NUMBER(11) DEFAULT 0 NOT NULL,
    CREATE_TIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
    UPDATE_TIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL
  ) TABLESPACE EMP_TABLESPACE
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 64K
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_SUB_DRAFTS') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_SUB_DRAFTS
	  ADD CONSTRAINT PK_LF_SUB_DRAFTS PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 2
	  MAXTRANS 255
	  STORAGE
	  (
		INITIAL 64K
		MINEXTENTS 1
		MAXEXTENTS UNLIMITED
	  )';
	END IF;
END;
/

-- 序列
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_URLTASK_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_URLTASK_S
        MINVALUE 1
        MAXVALUE 999999999999999999999999999
        START WITH 1
        INCREMENT BY 1
        CACHE 20';
  END IF;
END;
/
  
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_NETURL_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_NETURL_S
        MINVALUE 1
        MAXVALUE 999999999999999999999999999
        START WITH 1
        INCREMENT BY 1
        CACHE 20';
  END IF;
END;
/

DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_DOMAIN_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_DOMAIN_S
        MINVALUE 1
        MAXVALUE 999999999999999999999999999
        START WITH 1
        INCREMENT BY 1
        CACHE 20';
  END IF;
END;
/

DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_SUB_DRAFTS_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_SUB_DRAFTS_S
        MINVALUE 1
        MAXVALUE 999999999999999999999999999
        START WITH 1
        INCREMENT BY 1
        CACHE 20';
  END IF;
END;
/

-- 短链相关建表语句 End

-- 杭州马拉松相关脚本  Start
--
-- Creating table LF_TEMP_IMPORT_DETAILS
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TEMP_IMPORT_DETAILS';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_TEMP_IMPORT_DETAILS(
  ID         NUMBER(38) NOT NULL,
  BATCH   NUMBER(11) DEFAULT 0 NOT NULL,
  TM_NAME  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  CORP_CODE  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  NAME  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  PHONE_NUM  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  SCORE VARCHAR2(64) DEFAULT '' '' NOT NULL,
  SPTEMPLID NUMBER(20),
  SEND_STATUS NUMBER(11) DEFAULT 0 NOT NULL,
  IMPORT_STATUS NUMBER(11) DEFAULT 0 NOT NULL,
  CAUSE VARCHAR2(64) DEFAULT '' '' NOT NULL,
  IMAGE_SRC VARCHAR2(64) DEFAULT '' '' NOT NULL,
  VIDEO_SRC VARCHAR2(64) DEFAULT '' '' NOT NULL,
  ADDTIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL
) TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

--
-- Creating table LF_TEMP_IMPORT_BATCH
-- ==============================
--
DECLARE
  VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_TABLES WHERE TABLE_NAME='LF_TEMP_IMPORT_BATCH';
  IF VERSION = 0 THEN
	EXECUTE IMMEDIATE '
CREATE TABLE LF_TEMP_IMPORT_BATCH(
  ID         NUMBER(38) NOT NULL,
  BATCH   NUMBER(11) DEFAULT 0 NOT NULL,
  CORP_NAME  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  CORP_CODE  VARCHAR2(64) DEFAULT '' '' NOT NULL,
  AMOUNT  NUMBER(20) DEFAULT 0 NOT NULL,
  SUCCESS_AMOUNT  NUMBER(20) DEFAULT 0 NOT NULL,
  FAIL_AMOUNT NUMBER(20) DEFAULT 0 NOT NULL,
  PROCESS_STATUS NUMBER(11) DEFAULT 0 NOT NULL,
  TM_NAME VARCHAR2(64) DEFAULT '' '' NOT NULL,
  ADDTIME TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL
)
TABLESPACE EMP_TABLESPACE
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  )';
  END IF;
END;
/

DECLARE 
PCOUNT NUMBER;  
BEGIN 
	SELECT COUNT(1) INTO PCOUNT FROM USER_CONSTRAINTS T WHERE T.TABLE_NAME = UPPER('LF_TEMP_IMPORT_BATCH') AND T.CONSTRAINT_TYPE = 'P';     
	IF PCOUNT  = 0 THEN 
	EXECUTE IMMEDIATE
	'ALTER TABLE LF_TEMP_IMPORT_BATCH
	  ADD CONSTRAINT PK_LF_TEMP_IMPORT_BATCH PRIMARY KEY (ID)
	  USING INDEX
	  TABLESPACE EMP_TABLESPACE
	  PCTFREE 10
	  INITRANS 2
	  MAXTRANS 255
	  STORAGE
	  (
		INITIAL 64K
		MINEXTENTS 1
		MAXEXTENTS UNLIMITED
	  )';
	END IF;
END;
/

-- 杭州马拉松相关脚本  End

--
-- Creating sequence SEQ_LL_STATUSRPT
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='SEQ_LL_STATUSRPT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_LL_STATUSRPT
      MINVALUE 1
      NOMAXVALUE
      START WITH 1
      INCREMENT BY 1
      NOCYCLE
      NOCACHE';
  END IF;
END;
/

--
-- Creating sequence SEQ_LL_PRODUCT
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='SEQ_LL_PRODUCT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_LL_PRODUCT
      MINVALUE 1
      NOMAXVALUE
      START WITH 1
      INCREMENT BY 1
      NOCYCLE
      NOCACHE';
  END IF;
END;
/

--
-- Creating sequence LF_DRAFTS_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_DRAFTS_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_DRAFTS_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_TEMP_PARAM_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_TEMP_PARAM_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_TEMP_PARAM_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_BIGFILE_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_BIGFILE_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_BIGFILE_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_TEMPCONTENT
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TEMPCONTENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TEMPCONTENT
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_INDUSTRY_USE_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_INDUSTRY_USE_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_INDUSTRY_USE_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_SUB_TEMPLATE_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_SUB_TEMPLATE_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_SUB_TEMPLATE_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_HFIVE_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_HFIVE_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_HFIVE_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

-- Creating sequence LF_RMSTASK_CTRL_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_RMSTASK_CTRL_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_RMSTASK_CTRL_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BL_PRI
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BL_PRI';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BL_PRI
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SPFEEALARM
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SPFEEALARM';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SPFEEALARM
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SHORTTEMP
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SHORTTEMP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SHORTTEMP
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/


--
-- Creating sequence S_LF_MON_DBOPR
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_DBOPR';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_DBOPR
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_DGATEBUF
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_DGATEBUF';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_DGATEBUF
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_SUMCTRL_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_SUMCTRL_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_SUMCTRL_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/


--
-- Creating sequence LF_STATECODE_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_STATECODE_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_STATECODE_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_SPOFFCTRL_S
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_SPOFFCTRL_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_SPOFFCTRL_S
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_EMPLOYEE_TYPE_SEQUENCE
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_EMPLOYEE_TYPE_SEQUENCE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_EMPLOYEE_TYPE_SEQUENCE
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20';
  END IF;
END;
/

--
-- Creating sequence SEQ_LL_ORDER_TASK
-- ===========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='SEQ_LL_ORDER_TASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_LL_ORDER_TASK
	MINVALUE 1
	NOMAXVALUE
	START WITH 1
	INCREMENT BY 1
	NOCYCLE
	NOCACHE';
  END IF;
END;
/

--
-- Creating sequence S_LF_ACCOUNT_BIND
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ACCOUNT_BIND';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ACCOUNT_BIND
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_ADDRBOOKS
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ADDRBOOKS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ADDRBOOKS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_ALSMSRECORD
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ALSMSRECORD';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ALSMSRECORD
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APPMAINPAGEHIS
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APPMAINPAGEHIS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APPMAINPAGEHIS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_CLIDOWLOAD
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_CLIDOWLOAD';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_CLIDOWLOAD
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_FOMEITEM
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_FOMEITEM';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_FOMEITEM
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_FOMEVALVE
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_FOMEVALVE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_FOMEVALVE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_FOM_TYPE
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_FOM_TYPE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_FOM_TYPE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_LBS_PIOS
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_LBS_PIOS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_LBS_PIOS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_LBS_PUSH
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_LBS_PUSH';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_LBS_PUSH
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_LBS_USER
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_LBS_USER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_LBS_USER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DEGREE
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEGREE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEGREE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_MSGCONTENT
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_MSGCONTENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_MSGCONTENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_MTMSG
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_MTMSG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_MTMSG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_MTTASK
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_MTTASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_MTTASK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_MW_CLIENT
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_MW_CLIENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_MW_CLIENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_OL_MSGHIS
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_OL_MSGHIS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_OL_MSGHIS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_REVENT
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_REVENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_REVENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_RTEXT
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_RTEXT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_RTEXT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_SIT_PLANT
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_SIT_PLANT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_SIT_PLANT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_SIT_TYPE
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_SIT_TYPE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_SIT_TYPE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_TC_MOMSG
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_TC_MOMSG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_TC_MOMSG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence LF_SPOFFLPRD_S
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_SPOFFLPRD_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_SPOFFLPRD_S
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_APP_VERIFY
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_APP_VERIFY';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_APP_VERIFY
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_AUTOREPLY
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_AUTOREPLY';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_AUTOREPLY
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BILLLOG
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BILLLOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BILLLOG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BINDFLOW
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BINDFLOW';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BINDFLOW
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BIRTHDAYMEMBER
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BIRTHDAYMEMBER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BIRTHDAYMEMBER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BIRTHDAYSETUP
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BIRTHDAYSETUP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BIRTHDAYSETUP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BLACKLIST
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BLACKLIST';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BLACKLIST
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUSINESS
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUSINESS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUSINESS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUSMANAGER
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUSMANAGER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUSMANAGER
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUSPKGETAOCAN
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUSPKGETAOCAN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUSPKGETAOCAN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUSTAIL
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUSTAIL';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUSTAIL
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUS_PACKAGE
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUS_PACKAGE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUS_PACKAGE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUS_PROCESS
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUS_PROCESS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUS_PROCESS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUS_TAILTMP
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUS_TAILTMP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUS_TAILTMP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_BUS_TAOCAN
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BUS_TAOCAN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BUS_TAOCAN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CLIDEP_CONN
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CLIDEP_CONN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CLIDEP_CONN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CLIENT
-- =============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CLIENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CLIENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CLIENTCLASS
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CLIENTCLASS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CLIENTCLASS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CLIENT_DEP
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CLIENT_DEP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CLIENT_DEP
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CONTRACT
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CONTRACT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CONTRACT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CONTRACT_TAOCAN
-- ======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CONTRACT_TAOCAN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CONTRACT_TAOCAN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CORP
-- ===========================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CORP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CORP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CORP_CONF
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CORP_CONF';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CORP_CONF
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CUSTFIELD
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CUSTFIELD';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CUSTFIELD
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_CUSTFIELD_VALUE
-- ======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_CUSTFIELD_VALUE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_CUSTFIELD_VALUE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DBCONNECT
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DBCONNECT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DBCONNECT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DEDUCTIONS_DISP
-- ======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEDUCTIONS_DISP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEDUCTIONS_DISP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DEDUCTIONS_LIST
-- ======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEDUCTIONS_LIST';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEDUCTIONS_LIST
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DEP
-- ==========================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 22
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DEPPWDRECEIVER
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEPPWDRECEIVER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEPPWDRECEIVER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/
;

--
-- Creating sequence S_LF_DEP_RECHARGE_LOG
-- =======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEP_RECHARGE_LOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEP_RECHARGE_LOG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DEP_TAOCAN
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEP_TAOCAN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEP_TAOCAN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DEP_USER_BALANCE
-- =======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DEP_USER_BALANCE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DEP_USER_BALANCE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_DFADVANCED
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_DFADVANCED';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_DFADVANCED
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/
;

--
-- Creating sequence S_LF_EDIT
-- ===========================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_EDIT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_EDIT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_EMPDEP_CONN
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_EMPDEP_CONN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_EMPDEP_CONN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_EMPLOYEE
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_EMPLOYEE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_EMPLOYEE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_EMPLOYEE_DEP
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_EMPLOYEE_DEP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_EMPLOYEE_DEP
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_ENTERPRISE
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ENTERPRISE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ENTERPRISE
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_EXAMINE_SMS
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_EXAMINE_SMS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_EXAMINE_SMS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_EXTERNAL_USER
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_EXTERNAL_USER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_EXTERNAL_USER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_FLOW
-- ===========================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_FLOW';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_FLOW
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence A_RPTRECORD_S
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='A_RPTRECORD_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE A_RPTRECORD_S
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_FLOWBINDOBJ
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_FLOWBINDOBJ';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_FLOWBINDOBJ
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_FLOWBINDTYPE
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_FLOWBINDTYPE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_FLOWBINDTYPE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_FLOWRECORD
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_FLOWRECORD';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_FLOWRECORD
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_GLOBAL_VARIABLE
-- ======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_GLOBAL_VARIABLE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_GLOBAL_VARIABLE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_CLIENT
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_CLIENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_CLIENT
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_DIALOG
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_DIALOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_DIALOG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_GPUSERLINK
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_GPUSERLINK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_GPUSERLINK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_GROUP
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_GROUP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_GROUP
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_GROUPMESSAGE
-- ======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_GROUPMESSAGE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_GROUPMESSAGE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_HISTORY
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_HISTORY';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_HISTORY
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_MEMBERGROUP
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_MEMBERGROUP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_MEMBERGROUP
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_OFFICEGROUP
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_OFFICEGROUP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_OFFICEGROUP
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_RECENTCONTACTS
-- ========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_RECENTCONTACTS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_RECENTCONTACTS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_SEATGROUP
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_SEATGROUP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_SEATGROUP
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_SEATLIMIT
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_SEATLIMIT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_SEATLIMIT
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_SEATUSER
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_SEATUSER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_SEATUSER
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_SEAT_CGLINK
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_SEAT_CGLINK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_SEAT_CGLINK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_SEAT_CLIENT
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_SEAT_CLIENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_SEAT_CLIENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_SEAT_CLIENTGP
-- =======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_SEAT_CLIENTGP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_SEAT_CLIENTGP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_USERMESSAGE
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_USERMESSAGE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_USERMESSAGE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_IM_USERSUBNO
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_IM_USERSUBNO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_IM_USERSUBNO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_KEYWORDS
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_KEYWORDS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_KEYWORDS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_KEY_VALUE
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_KEY_VALUE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_KEY_VALUE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_LIST2GRO
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_LIST2GRO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_LIST2GRO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MACIP
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MACIP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MACIP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MALIST
-- =============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MALIST';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MALIST
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MAPPING
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MAPPING';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MAPPING
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 521
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MATERIAL
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MATERIAL';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MATERIAL
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MATERIAL_SORT
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MATERIAL_SORT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MATERIAL_SORT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MMSACCBIND
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MMSACCBIND';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MMSACCBIND
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MMSACCMANAGEMENT
-- =======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MMSACCMANAGEMENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MMSACCMANAGEMENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MMSBLIST
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MMSBLIST';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MMSBLIST
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MMSINFO
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MMSINFO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MMSINFO
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MMSMTTASK
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MMSMTTASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MMSMTTASK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MMSTEMPLATE
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MMSTEMPLATE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MMSTEMPLATE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MMS_MTREPORT
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MMS_MTREPORT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MMS_MTREPORT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 4
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MOBFINANCIAL
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MOBFINANCIAL';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MOBFINANCIAL
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_DGTACINFO
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_DGTACINFO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_DGTACINFO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_DHOST
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_DHOST';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_DHOST
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_DPROCE
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_DPROCE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_DPROCE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_DSPACINFO
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_DSPACINFO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_DSPACINFO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_ERR
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_ERR';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_ERR
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_ERRHIS
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_ERRHIS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_ERRHIS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_ONLCFG
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_ONLCFG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_ONLCFG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_SHOST
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_SHOST';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_SHOST
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_SPGATEBUF
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_SPGATEBUF';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_SPGATEBUF
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_SPROCE
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_SPROCE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_SPROCE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MON_WBSBUF
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MON_WBSBUF';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MON_WBSBUF
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MOTASK
-- =============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MOTASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MOTASK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MO_SERVICE
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MO_SERVICE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MO_SERVICE
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MTRPT_EC
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MTRPT_EC';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MTRPT_EC
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_MTTASK
-- =============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MTTASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MTTASK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_NOTICE
-- =============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_NOTICE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_NOTICE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_ONLINE_USER
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ONLINE_USER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ONLINE_USER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_OPERATE
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_OPERATE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_OPERATE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 32
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_OPRATELOG
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_OPRATELOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_OPRATELOG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PARAM
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PARAM';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PARAM
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PERFECT_NOTIC
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PERFECT_NOTIC';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PERFECT_NOTIC
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PERSONALCONFIG
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PERSONALCONFIG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PERSONALCONFIG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_POSITION
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_POSITION';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_POSITION
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PRIVILEGE
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PRIVILEGE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PRIVILEGE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 256
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PROCESS
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PROCESS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PROCESS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PROCHARGES
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PROCHARGES';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PROCHARGES
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PRODUCTOR_CONT
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PRODUCTOR_CONT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PRODUCTOR_CONT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PRODUCTS
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PRODUCTS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PRODUCTS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PROSENDCOUNT
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PROSENDCOUNT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PROSENDCOUNT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_PRO_CON
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_PRO_CON';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_PRO_CON
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_P_N_UP
-- =============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_P_N_UP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_P_N_UP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_RECONCILIATION
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_RECONCILIATION';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_RECONCILIATION
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_REPLY
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_REPLY';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_REPLY
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_RESOURCE
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_RESOURCE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_RESOURCE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 18
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_REVIEWER2LEVEL
-- =====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_REVIEWER2LEVEL';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_REVIEWER2LEVEL
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_REVIEWSWITCH
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_REVIEWSWITCH';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_REVIEWSWITCH
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_ROLES
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ROLES';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ROLES
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 22
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SENDGRO
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SENDGRO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SENDGRO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SERVICE
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SERVICE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SERVICE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 24
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SERVICELOG
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SERVICELOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SERVICELOG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SERVICEMSG
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SERVICEMSG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SERVICEMSG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SKIN
-- ===========================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SKIN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SKIN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SPFEE
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SPFEE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SPFEE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SP_DEP_BIND
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SP_DEP_BIND';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SP_DEP_BIND
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SQLWHERE
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SQLWHERE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SQLWHERE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SUBNOALLOT
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SUBNOALLOT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SUBNOALLOT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SUBNOALLOT_DETAIL
-- ========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SUBNOALLOT_DETAIL';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SUBNOALLOT_DETAIL
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SURVEY
-- =============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SURVEY';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SURVEY
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SURVEYTASK
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SURVEYTASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SURVEYTASK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SYSMTTASK
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SYSMTTASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SYSMTTASK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SYSUSER
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SYSUSER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SYSUSER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 22
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SYS_PARAM
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SYS_PARAM';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SYS_PARAM
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_TAOCAN_CMD
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TAOCAN_CMD';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TAOCAN_CMD
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_TASK
-- ===========================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TASK';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TASK
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_TEMPLATE
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TEMPLATE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TEMPLATE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_THEME
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_THEME';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_THEME
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_THIR_MENUCONTROL
-- =======================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_THIR_MENUCONTROL';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_THIR_MENUCONTROL
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 101
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_TIMER
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TIMER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TIMER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_TIMER_HISTORY
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TIMER_HISTORY';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TIMER_HISTORY
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_TMPLRELA
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TMPLRELA';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TMPLRELA
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/
;

--
-- Creating sequence S_LF_TRUCTTYPE
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_TRUCTTYPE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_TRUCTTYPE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_UDGROUP
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_UDGROUP';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_UDGROUP
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_USEDSUBNO
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_USEDSUBNO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_USEDSUBNO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_USER2SKIN
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_USER2SKIN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_USER2SKIN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_USERPARA
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_USERPARA';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_USERPARA
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_USER_RPT
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_USER_RPT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_USER_RPT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_USER_SETTING
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_USER_SETTING';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_USER_SETTING
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_VERSION_EMPDB
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_VERSION_EMPDB';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_VERSION_EMPDB
	minvalue 1
	maxvalue 9999999999999999999999999999
	start with 41
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_VODINFO
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_VODINFO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_VODINFO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_VODMOREC
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_VODMOREC';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_VODMOREC
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_VODMTREC
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_VODMTREC';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_VODMTREC
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WC_REVENT
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WC_REVENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WC_REVENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WC_RTEXT
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WC_RTEXT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WC_RTEXT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WC_VERIFY
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WC_VERIFY';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WC_VERIFY
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WEIX_EMOJI
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WEIX_EMOJI';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WEIX_EMOJI
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 500
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WGPARAMCONFIG
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WGPARAMCONFIG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WGPARAMCONFIG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WGPARAMDEFINITION
-- ========================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WGPARAMDEFINITION';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WGPARAMDEFINITION
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WX_BASEINFO
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WX_BASEINFO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WX_BASEINFO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WX_DATATYPE
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WX_DATATYPE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WX_DATATYPE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WX_PAGE
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WX_PAGE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WX_PAGE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/


--
-- Creating sequence S_LF_M_BUSDATA
-- ==============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_M_BUSDATA';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_M_BUSDATA
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WX_SENDCOUNT
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WX_SENDCOUNT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WX_SENDCOUNT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WX_UPLOADFILE
-- ====================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WX_UPLOADFILE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WX_UPLOADFILE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WX_VISITLOG
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WX_VISITLOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WX_VISITLOG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_LBS_PIOS
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_LBS_PIOS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_LBS_PIOS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_B_AR_SEND
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_B_AR_SEND';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_B_AR_SEND
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_LBS_PUSHSET
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_LBS_PUSHSET';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_LBS_PUSHSET
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_LBS_USERPIOS
-- ===================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_LBS_USERPIOS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_LBS_USERPIOS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_ONL_MSG_HIS
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ONL_MSG_HIS';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ONL_MSG_HIS
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence PB_BLACK_DEL_S
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='PB_BLACK_DEL_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE PB_BLACK_DEL_S
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_ONL_SERVER
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_ONL_SERVER';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_ONL_SERVER
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SIT_PLANT
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SIT_PLANT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SIT_PLANT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_SIT_TYPE
-- ===============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SIT_TYPE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SIT_TYPE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 21
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WEI_COUNT
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WEI_COUNT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WEI_COUNT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WEI_REVENT
-- =================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WEI_REVENT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WEI_REVENT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WEI_RTEXT
-- ================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WEI_RTEXT';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WEI_RTEXT
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_M_BUSBASE
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_M_BUSBASE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_M_BUSBASE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_WEI_SENDLOG
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_WEI_SENDLOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_WEI_SENDLOG
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_M_DBSTATE
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_M_DBSTATE';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_M_DBSTATE
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_M_BUSINFO
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_M_BUSINFO';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_M_BUSINFO
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_LF_M_HOSTNET
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_M_HOSTNET';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_M_HOSTNET
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/


--
-- Creating sequence S_LF_M_HNETWARN
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_M_HNETWARN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_M_HNETWARN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/


--
-- Creating sequence S_LF_FILEDATA
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_FILEDATA';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_FILEDATA
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/


--
-- Creating sequence S_LF_MT_PRI
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_MT_PRI';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_MT_PRI
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/


--
-- Creating sequence S_LF_BL_DEF
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_BL_DEF';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_BL_DEF
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/


--
-- Creating sequence S_LF_M_DBWARN
-- ==================================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_M_DBWARN';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_M_DBWARN
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20';
  END IF;
END;
/

--
-- Creating sequence S_RECON_ID
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_RECON_ID';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_RECON_ID
	minvalue 1
	maxvalue 99999999
	start with 1
	increment by 1
	nocache
	order';
  END IF;
END;
/

--
-- Creating sequence S_S_DEPART
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_S_DEPART';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_S_DEPART
	minvalue 1
	maxvalue 1000000000000000000000000000
	start with 1
	increment by 1
	nocache';
  END IF;
END;
/

--
-- Creating sequence S_LF_SPFEE_LOG
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='S_LF_SPFEE_LOG';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE S_LF_SPFEE_LOG
	minvalue 1
	maxvalue 1000000000000000000000000000
	start with 1
	increment by 1
	nocache';
  END IF;
END;
/

--
-- Creating sequence LF_THIR_CORP_S
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_THIR_CORP_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_THIR_CORP_S
          MINVALUE 1
          MAXVALUE 999999999999999999999999999
          START WITH 1
          INCREMENT BY 1
          CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_TEMP_SYNCH_S
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_TEMP_SYNCH_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_TEMP_SYNCH_S
      MINVALUE 1
      MAXVALUE 999999999999999999999999999
      START WITH 1
      INCREMENT BY 1
      CACHE 20';
  END IF;
END;
/

--
-- Creating sequence LF_WEBCONFIG_S
-- ============================
--
DECLARE
VERSION INT;
BEGIN
  SELECT COUNT(*) INTO VERSION FROM USER_SEQUENCES WHERE SEQUENCE_NAME='LF_WEBCONFIG_S';
  IF VERSION = 0 THEN
  EXECUTE IMMEDIATE 'CREATE SEQUENCE LF_WEBCONFIG_S
      MINVALUE 1
      MAXVALUE 999999999999999999999999999
      START WITH 1
      INCREMENT BY 1
      CACHE 20';
  END IF;
END;
/

--
-- Creating package DGPACKAGE
-- ==========================
--
CREATE OR REPLACE PACKAGE DGPACKAGE  AS

 TYPE DG_CURSOR IS REF CURSOR;

END DGPACKAGE;
/

--
-- Creating procedure COMPILE_PROCEDURE
-- ====================================
--
create or replace procedure COMPILE_PROCEDURE
as
v_sql varchar2(2000);
BEGIN

  for v in (SELECT * FROM USER_OBJECTS WHERE OBJECT_TYPE IN ('PROCEDURE','TRIGGER') AND STATUS='INVALID' AND OBJECT_NAME NOT LIKE '%BIN$%')
    loop
      v_sql:= 'alter  '||v.object_type||' '|| v.object_name||' compile';
      execute immediate v_sql;
      dbms_output.put_line(v_sql);
    END loop;
END;
/

--
-- Creating procedure COMPILE_VIEW
-- ===============================
--
create or replace procedure COMPILE_VIEW
IS
v_sql varchar2(2000);
BEGIN

  for v in (SELECT * FROM USER_VIEWS WHERE VIEW_NAME IN ('MTTASK_VIEW','MTTASK_VIEW_2','REALTASKGROUP_VIEW') and view_name not like '%BIN$%' )
    loop
      v_sql:= ' ALTER VIEW '||v.View_Name || '  compile';
      execute immediate v_sql;
      dbms_output.put_line(v_sql);
    END loop;
END;
/

--
-- Creating procedure COSTRECOVERY
-- ===============================
--
CREATE OR REPLACE PROCEDURE COSTRECOVERY(var_sysdepid number,var_depid number,var_username varchar2,var_count number,var_corpcode varchar2,var_costtype int,var_returncount out int)
IS
 var_parentid number;
 var_blcount number;
 var_yxcount number;
BEGIN
      IF var_count = 0 or var_count IS NULL THEN
        --短信充值/回收数目不能为空
         var_returncount:=-2;
         return;
      END IF;
      --判断是否为admin，admin为顶级机构的操作员
      BEGIN
        IF var_username<>'admin' THEN
           var_parentid:=var_sysdepid;
        else
           SELECT superior_id INTO var_parentid  from lf_dep where dep_id=var_depid;
        END IF;
         EXCEPTION WHEN OTHERS THEN
         --获取操作员上级机构失败
         var_returncount:=-3;
         rollback;
         return;
      END;

      IF var_username<>'admin' or (var_username='admin' and var_depid<>var_sysdepid) THEN
         --充值
         IF var_costtype=1 THEN
            update lf_dep_user_balance set sms_balance=sms_balance-var_count where corp_code=var_corpcode and sms_balance>=var_count and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             IF var_yxcount=0 THEN
               BEGIN
                 SELECT count(bl_id) INTO var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
                END;
                 IF var_blcount=0 THEN
                    --机构下没有可用短信余额
                    var_returncount:=-4;
                 else
                   --充值数据大于短信可分配余额
                    var_returncount:=-5;
                 END IF;
                 rollback;
                 return;
             END IF;
         else
           --回收
             --回收将回收的加到父及机构上
             update lf_dep_user_balance set sms_balance=sms_balance+var_count where corp_code=var_corpcode and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             IF var_yxcount=0 THEN
               BEGIN
                 SELECT count(bl_id) INTO var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
               END;
                 IF var_blcount=0 THEN
                    --机构下没有可用短信余额
                    var_returncount:=-4;
                    rollback;
                    return;
                 END IF;
             END IF;
           END IF;
      END IF;
      --充值
      IF var_costtype=1 THEN
        update lf_dep_user_balance set sms_balance=sms_balance+var_count where corp_code=var_corpcode and target_id=var_depid;
        var_yxcount:=sql%rowcount;
        IF var_yxcount=0 THEN
          --如果子的机构没有充过值则插入一条充值记录
          insert INTO lf_dep_user_balance(target_id,sms_balance,sms_count,mms_balance,mms_count,corp_code)
           values(var_depid,var_count,0,0,0,var_corpcode);
        END IF;
       else
          update lf_dep_user_balance set sms_balance=sms_balance-var_count where corp_code=var_corpcode and sms_balance>=var_count and target_id=var_depid;
          var_yxcount:=sql%rowcount;
          IF var_yxcount=0 THEN
            BEGIN
              SELECT count(bl_id) INTO var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_depid;
              EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
            END;
              IF var_blcount=0 THEN
                --机构没有进行充值过
                var_returncount:=-7;
              else
                --回收短信数大于机构可分配数目
                var_returncount:=-5;
              END IF;
              rollback;
              return;
          END IF;
       END IF;
      --短信充值/回收成功
      var_returncount:=0;
      commit;
      EXCEPTION WHEN OTHERS THEN
        --短信充值/回收失败
        var_returncount:=-1;
        rollback;
END COSTRECOVERY;
/

--
-- Creating procedure CREATEWXTABLE
-- ================================
--
CREATE OR REPLACE PROCEDURE CREATEWXTABLE(PIYM NUMBER)
IS
PISTR VARCHAR2(4000);
PISTR_1 VARCHAR2(256);
PITABLENAME VARCHAR2(20);
BEGIN
    PITABLENAME:='LFWCMSG'||CAST(PIYM AS CHAR);
    PISTR:='CREATE TABLE '||PITABLENAME||
    '(
        MSG_ID NUMBER(38) not null,
        MSG_TYPE NUMBER(2) not null,
        WC_ID  NUMBER(38) not null,
        A_ID NUMBER(38) not null,
        TYPE NUMBER(2) not null,
        MSG_XML VARCHAR2(4000) not null,
        MSG_TEXT VARCHAR2(4000) not null,
        PARENT_ID NUMBER(38),
        CORP_CODE  VARCHAR2(64) not null,
        CREATETIME TIMESTAMP(8) not null
      )tablespace EMP_TABLESPACE
      pctfree 10
      initrans 1
      maxtrans 255
      storage
      (
        initial 64K
        minextents 1
        maxextents unlimited
      )';
    EXECUTE IMMEDIATE PISTR;
    PISTR_1:='alter table '||PITABLENAME||' add constraint PK_'||PITABLENAME||'  primary key(MSG_ID)';
    EXECUTE IMMEDIATE PISTR_1;

  END;
/

--
-- Creating procedure FEEDEDUCTION
-- ===============================
--
CREATE OR REPLACE PROCEDURE FEEDEDUCTION(var_depid number,var_sENDcount number,var_returncount out int)
IS
 yxcount number;
 blcount number;
BEGIN
      IF var_sENDcount = 0 THEN
         var_returncount:=-5;
      else
          IF var_sENDcount>0 THEN
             update LF_DEP_USER_BALANCE set SMS_BALANCE=SMS_BALANCE-var_sENDcount,SMS_COUNT=SMS_COUNT+var_sENDcount where TARGET_ID=var_depid and SMS_BALANCE>=var_sENDcount;
             yxcount:=sql%rowcount;
             IF yxcount>0 THEN
                var_returncount:=0;
             else
                SELECT count(BL_ID) INTO blcount from  LF_DEP_USER_BALANCE where TARGET_ID=var_depid;
                IF blcount=0 THEN
                     --用户所属机构没有充值
                     var_returncount:=-4;
                else
                      --短信余额不足
                     var_returncount:=-2;
                END IF;
             END IF;
          else
             update LF_DEP_USER_BALANCE set SMS_BALANCE=SMS_BALANCE-var_sENDcount,SMS_COUNT=SMS_COUNT+var_sENDcount where TARGET_ID=var_depid and SMS_COUNT+var_sENDcount>0;
             yxcount:=sql%rowcount;
             IF yxcount>0 THEN
                --短信回收成功
                var_returncount:=1;
             else
                SELECT count(BL_ID) INTO blcount from  LF_DEP_USER_BALANCE where TARGET_ID=var_depid;
                IF blcount=0 THEN
                    --用户所属机构没有充值
                    var_returncount:=-4;
                else
                    --短信回收已发送条数异常
                    var_returncount:=-8;
                END IF;
             END IF;
          END IF;
        END IF;
     commit;
END FEEDEDUCTION;
/

--
-- Creating procedure GETCLIDEPCHILDBYPID
-- ======================================
--
CREATE OR REPLACE PROCEDURE GETCLIDEPCHILDBYPID(Lev INT,PID number,SJSC varchar2,P_CURSOR out DGPACKAGE.DG_CURSOR)
IS
  I INT;
  rrowcount INT;
BEGIN
   I := 1;
     IF Lev = 1 THEN
     insert INTO LF_TEMP values(PID,0,SJSC,sysdate);
     END IF;
     SELECT nvl(count(*),0) INTO rrowcount FROM LF_CLIENT_DEP  WHERE (PARENT_ID = PID);
     for r in (SELECT DEP_ID FROM LF_CLIENT_DEP  WHERE (PARENT_ID = PID)) loop
           insert INTO LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
     END loop;
     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT nvl(count(*),0) INTO rrowcount FROM LF_CLIENT_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC);
         for r in (SELECT A.DEP_ID FROM LF_CLIENT_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC)) loop
          insert INTO LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
         END loop;
     END loop;

    OPEN P_CURSOR FOR SELECT DEPID FROM LF_TEMP where SJS=SJSC;
    delete from LF_TEMP  where SJS=SJSC or curentdate<(sysdate-(1/24));
    commit;
END GETCLIDEPCHILDBYPID;
/

--
-- Creating procedure GETCLIDEPLEVEL
-- =================================
--
CREATE OR REPLACE PROCEDURE GETCLIDEPLEVEL(Lev INT,PID number,MaxLev out int)
IS
  I INT;
  rrowcount number;
  wcount number;
BEGIN
      I:=0;
      IF Lev = 1 THEN
      I := 1;
     END IF;
     SELECT nvl(count(*),0) INTO wcount FROM LF_CLIENT_DEP  WHERE (DEP_ID = PID);
     IF wcount=0 THEN
         I:=0;
         rrowcount:=0;
     else
        SELECT PARENT_ID INTO rrowcount FROM LF_CLIENT_DEP  WHERE (DEP_ID = PID);
     END IF;

     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT PARENT_ID INTO rrowcount FROM LF_CLIENT_DEP  WHERE (DEP_ID = rrowcount);
     END loop;
     MaxLev:=I;
END GETCLIDEPLEVEL;
/

--
-- Creating procedure GETEMPDEPCHILDBYPID
-- ======================================
--
CREATE OR REPLACE PROCEDURE GETEMPDEPCHILDBYPID(Lev INT,PID number,SJSC varchar2,P_CURSOR out DGPACKAGE.DG_CURSOR)
IS
  I INT;
  rrowcount INT;
BEGIN
   I := 1;
     IF Lev = 1 THEN
     insert INTO LF_TEMP values(PID,0,SJSC,sysdate);
     END IF;
     SELECT nvl(count(*),0) INTO rrowcount FROM LF_EMPLOYEE_DEP  WHERE (PARENT_ID = PID);
     for r in (SELECT DEP_ID FROM LF_EMPLOYEE_DEP  WHERE (PARENT_ID = PID)) loop
           insert INTO LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
     END loop;
     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT nvl(count(*),0) INTO rrowcount FROM LF_EMPLOYEE_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC);
         for r in (SELECT A.DEP_ID FROM LF_EMPLOYEE_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC)) loop
          insert INTO LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
         END loop;
     END loop;

    OPEN P_CURSOR FOR SELECT DEPID FROM LF_TEMP where SJS=SJSC;
    delete from LF_TEMP  where SJS=SJSC or curentdate<(sysdate-(1/24));
    commit;
END GETEMPDEPCHILDBYPID;
/

--
-- Creating procedure GETEMPDEPLEVEL
-- =================================
--
CREATE OR REPLACE PROCEDURE GETEMPDEPLEVEL(Lev INT,PID number,MaxLev out int)
IS
  I INT;
  rrowcount number;
  wcount number;
BEGIN
      I:=0;
      IF Lev = 1 THEN
      I := 1;
     END IF;
     SELECT nvl(count(*),0) INTO wcount FROM Lf_Employee_Dep  WHERE (DEP_ID = PID);
     IF wcount=0 THEN
         I:=0;
         rrowcount:=0;
     else
        SELECT PARENT_ID INTO rrowcount FROM Lf_Employee_Dep  WHERE (DEP_ID = PID);
     END IF;

     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT PARENT_ID INTO rrowcount FROM Lf_Employee_Dep  WHERE (DEP_ID = rrowcount);
     END loop;
     MaxLev:=I;
END GETEMPDEPLEVEL;
/

--
-- Creating procedure GETUSERDEPCHILDBYPID
-- =======================================
--
CREATE OR REPLACE PROCEDURE GETUSERDEPCHILDBYPID(Lev INT,PID number,SJSC varchar2,P_CURSOR out DGPACKAGE.DG_CURSOR)
IS
  I INT;
  rrowcount INT;
BEGIN
   I := 1;
     IF Lev = 1 THEN
     insert INTO LF_TEMP values(PID,0,SJSC,sysdate);
     END IF;
     SELECT nvl(count(*),0) INTO rrowcount FROM LF_DEP  WHERE (SUPERIOR_ID = PID) and (DEP_STATE=1);
     for r in (SELECT DEP_ID FROM LF_DEP  WHERE (SUPERIOR_ID = PID) and (DEP_STATE=1)) loop
           insert INTO LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
     END loop;
     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT nvl(count(*),0) INTO rrowcount FROM LF_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.SUPERIOR_ID) and (a.dep_state=1) and (b.sjs=SJSC);
         for r in (SELECT A.DEP_ID FROM LF_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.SUPERIOR_ID) and (a.dep_state=1) and (b.sjs=SJSC)) loop
          insert INTO LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
         END loop;
     END loop;

    OPEN P_CURSOR FOR SELECT DEPID FROM LF_TEMP where SJS=SJSC;
    delete from LF_TEMP  where SJS=SJSC or curentdate<(sysdate-(1/24));
    commit;
END GETUSERDEPCHILDBYPID;
/

--
-- Creating procedure GETUSERDEPLEVEL
-- ==================================
--
CREATE OR REPLACE PROCEDURE GETUSERDEPLEVEL(Lev INT,PID number,MaxLev out int)
IS
  I INT;
  rrowcount number;
  wcount number;
BEGIN
      I:=0;
      IF Lev = 1 THEN
      I := 1;
     END IF;
     SELECT nvl(count(*),0) INTO wcount FROM LF_DEP  WHERE (DEP_ID = PID);
     IF wcount=0 THEN
         I:=0;
         rrowcount:=0;
     else
        SELECT SUPERIOR_ID INTO rrowcount FROM LF_DEP  WHERE (DEP_ID = PID);
     END IF;

     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT SUPERIOR_ID INTO rrowcount FROM LF_DEP  WHERE (DEP_ID = rrowcount);
     END loop;
     MaxLev:=I;
END GETUSERDEPLEVEL;
/

--
-- Creating procedure H_SUMMARY
-- ============================
--
create or replace procedure "H_SUMMARY" as
   str varchar2(4000);
   tablename varchar2(20);
   isExist number;
   processflag           number; --转移方式0:处理今天（含今天)以前的，1为前一天以前的 ,2为前二天以前的...以此类推,
   currindex             number; --当前处理位置
   maxindex              number; --此次执行的最大位置
BEGIN
  processflag           := 2;
  currindex             := 0;
  maxindex              := 0;

  --------------------VER 3.2-------------------------------------
--删除临时
          tablename :='tmep_summary';
          isExist:=0;
          SELECT count(table_name) INTO isExist from user_tables where table_name=upper(tablename);
          IF(isExist>0) THEN
            BEGIN
                  str := 'drop table '||tablename;
                  execute immediate str;
            END;
          END IF;
--创建临时表

      --  SPISUNCM Number(11),
 str :='Create global temporary table '|| tablename ||'(
         USERID      VARCHAR2(11),
         TASKID      NUMBER(11),
         SPGATE      VARCHAR2(21),
         ERRORCODE CHAR(7),
         Y           NUMBER(11),
         IMONTH      NUMBER(11),
         ICOUNT      NUMBER(11),
         IYMD        NUMBER(11),
         SPISUNCM Number(11),
         SVRTYPE VARCHAR2(64),
         P1 VARCHAR2(64),
         P2 VARCHAR2(64),
         P3 VARCHAR2(64),
         P4 VARCHAR2(64)
        )';

--dbms_output.put_line(str);
 execute immediate str;

  --删除前天汇总的数据
str:='delete from lf_mtrpt_ec where iymd=to_number(to_char(sysdate-'||processflag||',''yyyymmdd''))';
dbms_output.put_line(str);
execute immediate str;



 SELECT nvl(min(ID),0),nvl(max(ID),0) INTO currindex,maxindex  from MT_TASK  where ( MT_TASK.SENDTIME between  to_date(TO_CHAR(sysdate-processflag,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss '));
 --统计
 str:= 'insert INTO  '|| tablename ||' (USERID,TASKID,SPGATE,ERRORCODE,Y,IMONTH,ICOUNT,IYMD,SPISUNCM,SVRTYPE,P1,P2,P3,P4)
               SELECT  USERID,TASKID,SPGATE,ERRORCODE,
               to_number(to_char(SENDTIME,''yyyy'')) as Y,
               to_number(to_char(SENDTIME,''mm'')) as mon,
               count(id),
               to_number(to_char(SENDTIME,''yyyymmdd'')) as iymd,
               UNICOM,SVRTYPE,P1,P2,P3,P4
               from MT_TASK
               where  id between  '|| currindex || ' and  '|| maxindex ||'
               group by USERID,TASKID,SPGATE,ERRORCODE,UNICOM,SVRTYPE,P1,P2,P3,P4,
               to_number(to_char(SENDTIME,''yyyy'')),
               to_number(to_char(SENDTIME,''mm'')),
               to_number(to_char(SENDTIME,''yyyymmdd''))';
     dbms_output.put_line(str);
 execute immediate str;
--插入或更新统计表
       str:='merge INTO LF_MTRPT_EC M
          using  '|| tablename ||'  T
          on(M.USERID   = T.USERID and
             M.TASKID   = T.TASKID and
             M.IYMD     = T.IYMD and
             M.ERRORCODE     = T.ERRORCODE and
             M.SPGATE   = T.SPGATE and
             M.Y        = T.Y and
             M.IMONTH   = T.IMONTH and
             M.SPISUNCM = T.SPISUNCM and
             M.SVRTYPE  = T.SVRTYPE and
             M.P1       = T.P1 and
             M.P2       = T.P2 and
             M.P3       = T.P3 and
             M.P4       = T.P4
             )
          when matched THEN
               update set M.ICOUNT=T.ICOUNT
          when not matched THEN
               insert (USERID,TASKID,SPGATE,IYMD,IMONTH,Y,ICOUNT,ERRORCODE,SPISUNCM,SVRTYPE,P1,P2,P3,P4)
               values(T.USERID,T.TASKID,T.SPGATE,T.IYMD,T.IMONTH,T.Y,T.ICOUNT,T.ERRORCODE,T.SPISUNCM,T.SVRTYPE,T.P1,T.P2,T.P3,T.P4)';
--dbms_output.put_line(str);
 execute immediate str;


 execute immediate  'drop table '||tablename;
 --execute immediate  'drop table '||tablename;
 commit;
END;
/

--
-- Creating procedure MMSCOSTRECOVERY
-- ==================================
--
CREATE OR REPLACE PROCEDURE MMSCOSTRECOVERY(var_sysdepid number,var_depid number,var_username varchar2,var_count number,var_corpcode varchar2,var_costtype int,var_returncount out int)
IS
 var_parentid number;
 var_blcount number;
 var_yxcount number;
BEGIN
      IF var_count = 0 or var_count IS NULL THEN
        --彩信充值/回收数目不能为空
         var_returncount:=-2;
         return;
      END IF;
      --判断是否为admin，admin为顶级机构的操作员
      BEGIN
        IF var_username<>'admin' THEN
           var_parentid:=var_sysdepid;
        else
           SELECT superior_id INTO var_parentid  from lf_dep where dep_id=var_depid;
        END IF;
        EXCEPTION WHEN OTHERS THEN
         --获取操作员上级机构失败
         var_returncount:=-3;
         rollback;
         return;
      END;
      IF var_username<>'admin' or (var_username='admin' and var_depid<>var_sysdepid) THEN
         --充值
         IF var_costtype=1 THEN
            update lf_dep_user_balance set mms_balance=mms_balance-var_count where corp_code=var_corpcode and mms_balance>=var_count and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             IF var_yxcount=0 THEN
               BEGIN
                 SELECT count(bl_id) INTO var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                 --获取用户短信余额记录失败
                 var_returncount:=-6;
                 rollback;
                 return;
                END;
                 IF var_blcount=0 THEN
                    --机构下没有可用彩信余额
                    var_returncount:=-4;
                 else
                   --充值数据大于彩信可分配余额
                    var_returncount:=-5;
                 END IF;
                 rollback;
                 return;
             END IF;
         else
           --回收
             --回收将回收的加到父及机构上
             update lf_dep_user_balance set mms_balance=mms_balance+var_count where corp_code=var_corpcode and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             IF var_yxcount=0 THEN
               BEGIN
                 SELECT count(bl_id) INTO var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
               END;
                 IF var_blcount=0 THEN
                    --机构下没有可用彩信余额
                    var_returncount:=-4;
                    rollback;
                    return;
                 END IF;

             END IF;
           END IF;
      END IF;
      --充值
      IF var_costtype=1 THEN
        update lf_dep_user_balance set mms_balance=mms_balance+var_count where corp_code=var_corpcode and target_id=var_depid;
        var_yxcount:=sql%rowcount;
        IF var_yxcount=0 THEN
          --如果子的机构没有充过值则插入一条充值记录
          insert INTO lf_dep_user_balance(target_id,sms_balance,sms_count,mms_balance,mms_count,corp_code)
           values(var_depid,0,0,var_count,0,var_corpcode);
        END IF;
       else
          update lf_dep_user_balance set mms_balance=mms_balance-var_count where corp_code=var_corpcode and mms_balance>=var_count and target_id=var_depid;
          var_yxcount:=sql%rowcount;
          IF var_yxcount=0 THEN
            BEGIN
             SELECT count(bl_id) INTO var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_depid;
             EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
            END;
            IF var_blcount=0 THEN
              --机构没有进行充值过
              var_returncount:=-7;
            else
              --回收彩信数大于机构可分配数目
              var_returncount:=-5;
            END IF;
            rollback;
            return;
          END IF;
       END IF;
      --彩信充值/回收成功
      var_returncount:=0;
      commit;
      EXCEPTION WHEN OTHERS THEN
        --彩信充值/回收失败
        var_returncount:=-1;
        rollback;
END MMSCOSTRECOVERY;
/

--
-- Creating procedure MMSFEEDEDUCTION
-- ==================================
--
CREATE OR REPLACE PROCEDURE MMSFEEDEDUCTION(var_depid number,var_sENDcount number,var_returncount out int)
IS
 yxcount number;
 blcount number;
BEGIN
      IF var_sENDcount = 0 THEN
        --:彩信发送或回收条数不能为空
         var_returncount:=-5;
      else
          IF var_sENDcount>0 THEN
             update LF_DEP_USER_BALANCE set MMS_BALANCE=MMS_BALANCE-var_sENDcount,MMS_COUNT=MMS_COUNT+var_sENDcount where TARGET_ID=var_depid and MMS_BALANCE>=var_sENDcount;
             yxcount:=sql%rowcount;
             IF yxcount>0 THEN
                var_returncount:=0;
             else
                SELECT count(BL_ID) INTO blcount from  LF_DEP_USER_BALANCE ld where TARGET_ID=var_depid;
                IF blcount=0 THEN
                     --彩信用户所属机构没有充值
                     var_returncount:=-4;
                else
                      --彩信余额不足
                     var_returncount:=-2;
                END IF;
             END IF;
          else
             update LF_DEP_USER_BALANCE set MMS_BALANCE=MMS_BALANCE-var_sENDcount,MMS_COUNT=MMS_COUNT+var_sENDcount where TARGET_ID=var_depid and MMS_COUNT+var_sENDcount>0;
             yxcount:=sql%rowcount;
             IF yxcount>0 THEN
                --彩信回收成功
                var_returncount:=1;
             else
                SELECT count(BL_ID) INTO blcount from  LF_DEP_USER_BALANCE where TARGET_ID=var_depid;
                IF blcount=0 THEN
                    --彩信用户所属机构没有充值
                    var_returncount:=-4;
                else
                    --彩信回收已发送条数异常
                    var_returncount:=-8;
                END IF;
             END IF;
          END IF;
        END IF;
     commit;
END MMSFEEDEDUCTION;
/

--
-- Creating procedure PRC_DATAINSERT
-- =================================
--
create or replace procedure PRC_DATAINSERT is
  var_icount     number;
  var_taskid     number;
  var_succ       number;
  var_fail1      number;
  var_fail2      number;
  var_rnret      number;
  type rc is ref cursor;
  cur_query rc;
BEGIN
  open cur_query for
    SELECT distinct taskid from mt_datareport;
  loop
    fetch cur_query
      INTO var_taskid;
    exit when cur_query%notfound;
    SELECT sum(ICOUNT),sum(RSUCC),sum(RFAIL1), sum(RFAIL2),sum(RNRET)
      INTO var_icount,var_succ, var_fail1, var_fail2,var_rnret
      from mt_datareport
     where taskid = var_taskid;
    update LF_MTTASK
       set SUC_COUNT = var_succ ,FAI_COUNT = var_fail1 ,ICOUNT = var_icount,rfail2=var_fail2,rnret=var_rnret
     where MT_ID in (SELECT MT_ID
                       from LF_TASK
                      where TASKID = var_taskid);

  END loop;
  close cur_query;
  commit;
   EXCEPTION WHEN OTHERS THEN
    dbms_output.put_line('汇总报表异常');
END PRC_DATAINSERT;
/

--
-- Creating procedure PRC_MMSSUMM
-- ==============================
--
create or replace procedure PRC_MMSSUMM  is
paramvalue varchar2(64);
hparamvalue     varchar(64);
BEGIN
  SELECT param_value INTO hparamvalue from lf_sys_param where param_item='isHdataFilish';
  IF hparamvalue='0' THEN
     SELECT param_value INTO paramvalue from lf_sys_param where param_item='ismSummFilish';
    IF paramvalue='0' THEN
       BEGIN
          update lf_sys_param set param_value='1' where param_item='ismSummFilish';
          commit;
          delete from LF_MMSTASKREPORT;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='ismSummFilish';
           delete from LF_MMSTASKREPORT;
           commit;
           return;
        END;
        BEGIN
            insert INTO LF_MMSTASKREPORT(R_TASKID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
            SELECT TASKID,0,0,0,0,0 from lf_mttask where TIMER_TIME>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and MS_TYPE=2;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='ismSummFilish';
            delete from  LF_MMSTASKREPORT;
            commit;
            return;
        END;
        BEGIN
            merge INTO LF_MMSTASKREPORT M
            using (SELECT taskid TASKID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0) RNRET --未返数
              from mms_task  where sENDtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by TASKID) T
            on(M.R_TASKID=T.TASKID)
            when matched THEN
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='ismSummFilish';
            delete from LF_MMSTASKREPORT;
            commit;
            return;
        END;
        BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_MMSTASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Ms_Type=2)
          when matched THEN
            update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='ismSummFilish';
          delete from LF_MMSTASKREPORT;
          commit;
          return;
        END;
        delete from LF_MMSTASKREPORT;
	update lf_sys_param set param_value='0' where param_item='ismSummFilish';
        commit;
    END IF;
  END IF;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('汇总异常');
            rollback;
            return;
END;
/

--
-- Creating procedure PRC_MMSSUMMONE
-- =================================
--
create or replace procedure PRC_MMSSUMMONE  is
r_c number;
paramvalue varchar2(64);
paramintvalue int;
BEGIN
  SELECT count(*) INTO r_c from LF_MMSTASKREPORT;
  IF r_c=0 THEN
     paramintvalue:=4;
     SELECT param_value INTO paramvalue from lf_sys_param where param_item='MsSummDay';
     IF paramvalue is not null  THEN
       paramintvalue:=TO_NUMBER(paramvalue);
     END IF;
     BEGIN
        insert INTO LF_MMSTASKREPORT(R_TASKID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
        SELECT taskid,nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0),nvl(sum(RFAIL2),0),nvl(sum(RNRET),0) from mms_datareport md
        where md.IYMD>TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by taskid;
        commit;
        EXCEPTION WHEN OTHERS THEN
          rollback;
          delete from LF_MMSTASKREPORT;
          commit;
          return;
     END;

     BEGIN
          merge INTO LF_MMSTASKREPORT M
          using (SELECT TASKID,nvl(sum(ICOUNT),0) ICOUNT,nvl(sum(RSUCC),0) RSUCC,nvl(sum(RFAIL1),0) RFAIL1,nvl(sum(RFAIL2),0) RFAIL2,nvl(sum(RNRET),0) RNRET from mms_datareport
          where IYMD<=TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by TASKID) T
          on(M.r_Taskid=T.TASKID)
          when matched THEN
               update set r_Icount=r_Icount+T.ICOUNT,r_Succ=r_Succ+T.RSUCC,r_Fail1=r_Fail1+T.RFAIL1,R_FAIL2=R_FAIL2+T.RFAIL2,R_NRET=R_NRET+T.RNRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from LF_MMSTASKREPORT;
            commit;
            return;
     END;

     BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_MMSTASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Ms_Type=2)
          when matched THEN
               update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from LF_MMSTASKREPORT;
            commit;
            return;
     END;
     delete from LF_MMSTASKREPORT;
     commit;
  END IF;
  EXCEPTION WHEN OTHERS THEN
  dbms_output.put_line('汇总异常');
END;
/

--
-- Creating procedure PRC_SUMM
-- ===========================
--
create or replace procedure PRC_SUMM  is
paramvalue varchar2(64);
hparamvalue     varchar(64);
BEGIN
  SELECT param_value INTO hparamvalue from lf_sys_param where param_item='isHdataFilish';
  IF hparamvalue='0' THEN
     SELECT param_value INTO paramvalue from lf_sys_param where param_item='isSummFilish';
    IF paramvalue='0' THEN
       BEGIN
          update lf_sys_param set param_value='1' where param_item='isSummFilish';
          commit;
          delete from LF_TASKREPORT;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='isSummFilish';
           delete from  LF_TASKREPORT;
           commit;
           return;
        END;
        BEGIN
            insert INTO LF_TASKREPORT(R_TASKID,R_BATCHID,R_TASKTYPE,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
            SELECT TASKID,BATCHID,TASKTYPE,0,0,0,0,0 from lf_mttask where TIMER_TIME>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and MS_TYPE<>2;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        END;
        BEGIN
            merge INTO LF_TASKREPORT M
            using (SELECT taskid TASKID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0) RNRET --未返数
              from mt_task  where sENDtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by TASKID) T
            on(M.r_Taskid=T.TASKID and M.r_Tasktype=1)
            when matched THEN
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        END;
        BEGIN
            merge INTO LF_TASKREPORT M
            using (SELECT BATCHID BATCHID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0) RNRET --未返数
              from mt_task  where sENDtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by BATCHID) T
            on(M.R_BATCHID=T.BATCHID and M.r_Tasktype=2)
            when matched THEN
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        END;
        BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and m.ms_type<>2)
          when matched THEN
            update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        END;

        BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.tasktype=2 and M.Ms_Type<>2)
          when matched THEN
            update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        END;

        delete from  LF_TASKREPORT;
	update lf_sys_param set param_value='0' where param_item='isSummFilish';
        commit;
    END IF;
  END IF;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('汇总异常');
            rollback;
            return;
END;
/

--
-- Creating procedure PRC_SUMMAPP
-- ==============================
--
create or replace procedure PRC_SUMMAPP  is
paramvalue varchar2(64);
BEGIN
    SELECT param_value INTO paramvalue from lf_sys_param where param_item='isAppSummFilish';
    IF paramvalue='0' THEN
       BEGIN
          update lf_sys_param set param_value='1' where param_item='isAppSummFilish';
          commit;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='isAppSummFilish';
           commit;
           return;
        END;
        BEGIN
            merge INTO lf_app_mttask M
            using (SELECT taskid TASKID,
              nvl(count(case RPT_STATE when '0' THEN 1 else null END),0) succount,--成功数
              nvl(count(case RPT_STATE when '1' THEN 1 else null END),0) faicount,--失败数
              nvl(count(case RPT_STATE when '0' THEN 1 else null END),0) readcount
              from lf_app_mtmsg  where TASKID is not null and CREATETIME>to_date(TO_CHAR(sysdate-3,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by TASKID) T
            on(M.Taskid=T.TASKID )
            when matched THEN
            update set SUC_COUNT = T.succount ,FAI_COUNT = T.faicount,READ_COUNT = T.readcount,UNREAD_COUNT=0;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isAppSummFilish';
            commit;
            return;
        END;
	      update lf_sys_param set param_value='0' where param_item='isAppSummFilish';
        commit;
    END IF;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('汇总异常');
            rollback;
            return;
END;
/

--
-- Creating procedure PRC_SUMMONE
-- ==============================
--
create or replace procedure PRC_SUMMONE  is
r_c number;
paramvalue varchar2(64);
paramintvalue int;
BEGIN
  SELECT count(*) INTO r_c from LF_TASKREPORT;
  IF r_c=0 THEN
     paramintvalue:=4;
     SELECT param_value INTO paramvalue from lf_sys_param where param_item='SummDay';
     IF paramvalue is not null THEN
       paramintvalue:=TO_NUMBER(paramvalue);
     END IF;
     BEGIN
        insert INTO LF_TASKREPORT(R_TASKID,R_BATCHID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
        SELECT taskid,BATCHID,nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0),nvl(sum(RFAIL2),0),nvl(sum(RNRET),0) from mt_datareport md
        where md.IYMD>TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by taskid,BATCHID;
        commit;
        EXCEPTION WHEN OTHERS THEN
          rollback;
          delete from  LF_TASKREPORT;
          commit;
          return;
     END;

     BEGIN
          merge INTO LF_TASKREPORT M
          using (SELECT TASKID,BATCHID,nvl(sum(ICOUNT),0) ICOUNT,nvl(sum(RSUCC),0) RSUCC,nvl(sum(RFAIL1),0) RFAIL1,nvl(sum(RFAIL2),0) RFAIL2,nvl(sum(RNRET),0) RNRET from mt_datareport
          where IYMD<=TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by TASKID,BATCHID) T
          on(M.r_Taskid=T.TASKID and M.r_Batchid=T.BATCHID)
          when matched THEN
               update set r_Icount=r_Icount+T.ICOUNT,r_Succ=r_Succ+T.RSUCC,r_Fail1=r_Fail1+T.RFAIL1,R_FAIL2=R_FAIL2+T.RFAIL2,R_NRET=R_NRET+T.RNRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     END;

     BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and M.Ms_Type<>2)
          when matched THEN
               update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     END;

      BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.Tasktype=2 and M.Ms_Type<>2)
          when matched THEN
               update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     END;

     delete from  LF_TASKREPORT;
     commit;
  END IF;
  EXCEPTION WHEN OTHERS THEN
  dbms_output.put_line('汇总异常');
END;
/

--
-- Creating procedure PRC_SUMMONEWY
-- ================================
--
create or replace procedure PRC_SUMMONEWY  is
r_c number;
paramvalue varchar2(64);
paramintvalue int;
BEGIN
  SELECT count(*) INTO r_c from LF_TASKREPORT;--查询是否有数据
  IF r_c=0 THEN--没数据则执行汇总
     paramintvalue:=4;--默认晚上汇总4天之内的
     SELECT param_value INTO paramvalue from lf_sys_param where param_item='SummDay';--获取晚上汇总n天之内的设置
     IF paramvalue is not null THEN--晚上汇总几天之内的设置有值，则使用设置值
       paramintvalue:=TO_NUMBER(paramvalue);
     END IF;

	 --先把n天内 且符合条件的记录填到表LF_TASKREPORT
	 BEGIN
        insert INTO LF_TASKREPORT(R_TASKID,R_BATCHID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
        SELECT taskid,BATCHID,nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0),nvl(sum(RFAIL2),0),nvl(sum(RNRET),0)
		from mt_datareport md where md.IYMD>TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD'))
		and md.SPGATE in (SELECT spgate from XT_GATE_QUEUE where spgate like '200%')
		group by taskid,BATCHID;
        commit;
        EXCEPTION WHEN OTHERS THEN
          rollback;
          delete from  LF_TASKREPORT;
          commit;
          return;
     END;

	 --把n天前 且符合条件的总数记录更新到表LF_TASKREPORT
     BEGIN
		merge INTO LF_TASKREPORT M
		using (SELECT TASKID,BATCHID,nvl(sum(ICOUNT),0) ICOUNT,nvl(sum(RSUCC),0) RSUCC,nvl(sum(RFAIL1),0) RFAIL1,nvl(sum(RFAIL2),0) RFAIL2,nvl(sum(RNRET),0) RNRET from mt_datareport
		where IYMD<=TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD'))
		and SPGATE in (SELECT spgate from XT_GATE_QUEUE where spgate like '200%')
		group by TASKID,BATCHID) T
          on(M.r_Taskid=T.TASKID and M.r_Batchid=T.BATCHID)
          when matched THEN
               update set r_Icount=r_Icount+T.ICOUNT,r_Succ=r_Succ+T.RSUCC,r_Fail1=r_Fail1+T.RFAIL1,R_FAIL2=R_FAIL2+T.RFAIL2,R_NRET=R_NRET+T.RNRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     END;

	 --更新信息到lf_mttask表的WYSENDINFO字段，taskid的任务
     BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and M.Ms_Type<>2 and T.r_Icount>0)
          when matched THEN
               update set m.WYSENDINFO=(T.r_Icount||'/'||T.r_Succ+'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     END;

	--更新信息到lf_mttask表的WYSENDINFO字段，batchid的任务
	BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.Tasktype=2 and M.Ms_Type<>2 and T.r_Icount>0)
          when matched THEN
               update set m.WYSENDINFO=(T.r_Icount||'/'||T.r_Succ||'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
	END;

     delete from  LF_TASKREPORT;
     commit;
  END IF;
  EXCEPTION WHEN OTHERS THEN
  dbms_output.put_line('网优晚上汇总异常');
END;
/

--
-- Creating procedure PRC_SUMMWY
-- =============================
--
create or replace procedure PRC_SUMMWY  is
paramvalue varchar2(64);
hparamvalue     varchar(64);
BEGIN
  SELECT param_value INTO hparamvalue from lf_sys_param where param_item='isHdataFilish';--获取调度汇总是否正在执行使用的状态
  IF hparamvalue='0' THEN--调度汇总不在执行
     SELECT param_value INTO paramvalue from lf_sys_param where param_item='isSummFilish';--获取EMP调度汇总是否正在执行使用的状态
    IF paramvalue='0' THEN--EMP调度汇总不在执行

	   BEGIN
          update lf_sys_param set param_value='1' where param_item='isSummFilish';--设置EMP调度汇总为正在执行
          commit;
          delete from LF_TASKREPORT;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='isSummFilish';--出现异常，则设置EMP调度汇总不在执行
           delete from  LF_TASKREPORT;
           commit;
           return;
        END;

        BEGIN
            insert INTO LF_TASKREPORT(R_TASKID,R_BATCHID,R_TASKTYPE,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
            SELECT TASKID,BATCHID,TASKTYPE,0,0,0,0,0 from lf_mttask where TIMER_TIME>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and MS_TYPE<>2;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        END;

		--符合条件的记录填到表LF_TASKREPORT，taskid的任务
        BEGIN
            merge INTO LF_TASKREPORT M
            using (SELECT taskid TASKID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0) RNRET --未返数
              from mt_task  where sENDtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and SPGATE in (SELECT spgate from XT_GATE_QUEUE where spgate like '200%')
			  group by TASKID) T
            on(M.r_Taskid=T.TASKID and M.r_Tasktype=1)
            when matched THEN
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        END;

		--符合条件的记录填到表LF_TASKREPORT，batchid的任务
        BEGIN
            merge INTO LF_TASKREPORT M
            using (SELECT BATCHID BATCHID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' THEN 1 else null END),0) RNRET --未返数
              from mt_task  where sENDtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and SPGATE in (SELECT spgate from XT_GATE_QUEUE where spgate like '200%')
			  group by BATCHID) T
            on(M.R_BATCHID=T.BATCHID and M.r_Tasktype=2)
            when matched THEN
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        END;

		--更新信息到lf_mttask表的WYSENDINFO字段，taskid的任务
        BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and m.ms_type<>2 and T.r_Icount>0)
          when matched THEN
            update set WYSENDINFO=(T.r_Icount||'/'||T.r_Succ||'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        END;

		--更新信息到lf_mttask表的WYSENDINFO字段，batchid的任务
        BEGIN
          merge INTO lf_mttask M
          using (SELECT sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.tasktype=2 and M.Ms_Type<>2 and T.r_Icount>0)
          when matched THEN
            update set WYSENDINFO=(T.r_Icount||'/'||T.r_Succ||'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        END;

        delete from  LF_TASKREPORT;
	update lf_sys_param set param_value='0' where param_item='isSummFilish';
        commit;
    END IF;
  END IF;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('网优白天汇总异常');
            rollback;
            return;
END;
/

--
-- Creating procedure PRC_SUMSH
-- ============================
--
create or replace procedure "PRC_SUMSH"
(
     task_id in number
)
 is

r_icount number;--发送总数
r_succ number;--成功总数
r_fail1 number;--失败总数
r_fail2 number;--失败总数
r_nret number;--未返总数

r_icountm number;--发送总数
r_succm number;--成功总数
r_fail1m number;--失败总数
r_fail2m number;--失败总数
r_nretm number;--未返总数

BEGIN
  r_icount:=0;
  r_icountm:=0;
  --汇总实时信息
   IF (task_id <> 0) THEN

       SELECT count(m.id) ICOUNT,--发送总数
          nvl(count(case trim(m.errorcode) when 'DELIVRD' THEN 1 when '0' THEN 1 else null END),0) SUCC,--成功数
          nvl(count(case substr(m.errorcode,1,3) when 'E1:' THEN 1 when 'E2:' THEN 1 else null END),0) FAIL,--失败数
          nvl(count(case nvl(trim(m.errorcode),'0') when '0' THEN 1 else null END),0) NRET --未返数
          INTO r_icount,r_succ,r_fail1,r_nret
     from mt_task m where m.taskid=task_id group by m.taskid;
     r_fail2:=r_icount-r_succ-r_fail1-r_nret;
      SELECT nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0), nvl(sum(RFAIL2),0),nvl(sum(RNRET),0)
      INTO r_icountm,r_succm, r_fail1m, r_fail2m,r_nretm
      from mt_datareport
     where taskid = task_id and to_date(IYMD, 'YYYY-MM-DD')<to_date(TO_CHAR(sysdate-2,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ');

     IF r_icount+r_icountm<>0 THEN
         update lf_mttask mttask set mttask.ICOUNT2=r_icount+r_icountm,
         mttask.suc_count=r_succ+r_succm,
          mttask.fai_count=r_fail1+r_fail1m,
          mttask.rfail2=r_fail2+r_fail2m,
          mttask.rnret=r_nret+r_nretm
          where mttask.mt_id in
          ( SELECT  mt_id from LF_TASK
              where mt_id is not null and
              taskid=task_id
          );
   END IF;
   END IF;
    commit;
     EXCEPTION WHEN OTHERS THEN
    dbms_output.put_line('汇总异常');
END;
/

--
-- Creating procedure PRO_GET_INT_ID
-- =================================
--
create or replace procedure "PRO_GET_INT_ID"
(
	 --行id。已使用到18
     FILDID in number,
	 GETCOUNT in number:=1,
	 GVALUE OUT NUMBER
)
AS
BEGIN
	GVALUE:=0;
	--没指定行id，则返回0
	IF(FILDID IS NULL OR FILDID = 0) THEN
		GVALUE:=0;
		RETURN;
	END IF;

	--先更新表新值
	UPDATE LF_GLOBAL_VARIABLE SET GLOBALVALUE = GLOBALVALUE + GETCOUNT WHERE GLOBALID = FILDID;

	--查询出当前使用值
	SELECT GLOBALVALUE INTO GVALUE FROM LF_GLOBAL_VARIABLE WHERE GLOBALID = FILDID FOR UPDATE;

	IF(GVALUE IS NULL OR GVALUE = 0) THEN
		--若没初始化数据，则这里插一条
		INSERT INTO LF_GLOBAL_VARIABLE(globalid,globalkey,globalvalue) values(FILDID,'',1000000+GETCOUNT);
	END IF;

	COMMIT;

	EXCEPTION
	WHEN OTHERS THEN
		ROLLBACK;
		GVALUE:=0;
		dbms_output.put_line('PRO_GET_INT_ID获取ID异常');


END;
/

--
-- Creating procedure PRO_INSERT
-- =============================
--
create or replace procedure PRO_INSERT is
  var_date       number;
  var_month      number;
  var_countSucc  number;
  var_icount     number;
  var_year       number;
  var_spisuncm   number;
  var_gateName   varchar2(64);
  var_fee        number(6, 3);
  var_staffName  varchar2(24);
  var_depName    varchar2(64);
  var_depId      number;
  var_cost       number(10, 3);
  var_taskid     number;
  var_succ       number;
  var_fail1      number;
  var_fail2      number;
  var_fail3      number;
  var_fail_count number;
  var_spgate     varchar2(30);
  var_userid     varchar2(20);
  j              number;
  type rc is ref cursor;
  cur_query rc;
  var_id    number;
BEGIN
  open cur_query for
    SELECT MT.SPGATE,
           MT.USERID,
           MT.IYMD,
           MT.IMONTH,
           MT.Y,
           userData.STAFFNAME,
           XT.SPISUNCM,
           XT.GATENAME,
           XT.FEE,
       LD.DEP_NAME,
       LF.DEP_ID
      from MT_DATAREPORT                     MT,
           USERDATA      userData,
           XT_GATE_QUEUE  XT,
           LF_TASK     LF,
       LF_DEP      LD
     where trim(userData.USERID) = trim(MT.USERID)
       and trim(MT.SPGATE) = trim(XT.SPGATE)
       and MT.taskid = LF.taskid
     and LD.dep_id = LF.DEP_ID
       and (to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') -
           TO_date(mt.iymd, 'yyyy-mm-dd')) * 86400 <= 259200;
  loop

    fetch cur_query
      INTO var_spgate,
           var_userid,
           var_date,
           var_month,
           var_year,
           var_staffName,
           var_spisuncm,
           var_gateName,
           var_fee,
           var_depName,
       var_depId;

    exit when cur_query%notfound;

    SELECT sum(RSUCC)+sum(RNRET),sum(ICOUNT)
      INTO var_countSucc ,var_icount
      from MT_DATAREPORT MT, LF_TASK LF
     where trim(mt.iymd) = trim(var_date)
       and trim(mt.userid) = trim(var_userid)
       and trim(mt.spgate) = trim(var_spgate)
       and trim(lf.DEP_ID) = trim(var_depId)
       and mt.taskid= lf.taskid;

    var_cost := var_countSucc * var_fee;
    SELECT count(*)
      INTO j
      from lf_reconciliation lr
     where trim(lr.iymd) = trim(var_date)
       and trim(lr.STAFFNAME) =trim(var_staffname)
       and trim(lr.DEP_NAME) = trim(var_depName)
       and trim(lr.GATENAME) = trim(var_gateName);

    IF (j = 0) THEN
      insert INTO LF_RECONCILIATION
        (ID,
         IMONTH,
         Y,
         IYMD,
         RSUCC,
         SPISUNCM,
         FEE,
         STAFFNAME,
         DEP_NAME,
         GATENAME,
         COST,

     ICOUNT,
     USERID,
     DEP_ID)
      values
        (var_id,
         var_month,
         var_year,
         var_date,
         var_countSucc,
         var_spisuncm,
         var_fee,
         var_staffName,
         var_depName,
         var_gateName,
         var_cost,
     var_icount,
     trim(var_userid),
     trim(var_depId));
    else
      update lf_reconciliation
         set RSUCC = var_countSucc, COST = var_cost,ICOUNT = var_icount
       where trim(iymd) = trim(var_date)
         and trim(STAFFNAME) = trim(var_staffname)
     and trim(USERID) = trim(var_userid)
         and trim(DEP_ID) = trim(var_depId)
         and trim(GATENAME) = trim(var_gateName);
    END IF;
  END loop;
  close cur_query;
  open cur_query for
    SELECT distinct taskid from mt_datareport;
  loop
    fetch cur_query
      INTO var_taskid;
    exit when cur_query%notfound;
    SELECT sum(RSUCC)+sum(RNRET), sum(FAIL1), sum(FAIL2), sum(FAIL3),sum(ICOUNT)
      INTO var_succ, var_fail1, var_fail2, var_fail3,var_icount
      from mt_datareport
     where taskid = var_taskid;
    var_fail_count := var_fail1 + var_fail2 + var_fail3;
    update LF_MTTASK
       set SUC_COUNT = var_succ ,FAI_COUNT = var_fail_count ,ICOUNT = var_icount
     where MT_ID in (SELECT MT_ID
                       from LF_TASK
                      where TASKID = var_taskid);

  END loop;
  close cur_query;
  commit;
   EXCEPTION WHEN OTHERS THEN
    dbms_output.put_line('汇总报表异常');
END PRO_INSERT;
/

--
-- Creating trigger LF_SPFEEALARM_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_SPFEEALARM_Q
BEFORE INSERT ON LF_SPFEEALARM
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_SPFEEALARM.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_TEMP_PARAM
-- ===============================================
--
CREATE OR REPLACE TRIGGER TIG_LF_TEMP_PARAM
BEFORE INSERT ON LF_TEMP_PARAM
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_TEMP_PARAM_S.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_SPFEEALARM_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_SPFEEALARM_Q
BEFORE INSERT ON LF_SPFEEALARM
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_SPFEEALARM.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_TEMPCONTENT_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_TEMPCONTENT_Q
BEFORE INSERT ON LF_TEMPCONTENT
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_TEMPCONTENT.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_SUB_TEMPLATE_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_SUB_TEMPLATE_Q
BEFORE INSERT ON LF_SUB_TEMPLATE
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_SUB_TEMPLATE_S.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_RMSTASK_CTRL_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_RMSTASK_CTRL_Q
BEFORE INSERT ON LF_RMSTASK_CTRL
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_RMSTASK_CTRL_S.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_M_DBWARN_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_M_DBWARN_Q
BEFORE INSERT ON LF_MON_DBWARN
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_M_DBWARN.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_BL_PRI_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_BL_PRI_Q
BEFORE INSERT ON LF_BALANCE_PRI
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_BL_PRI.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_BL_DEF_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_BL_DEF_Q
BEFORE INSERT ON LF_BALANCE_DEF
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_BL_DEF.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/


--
-- Creating trigger LF_MON_DBOPR_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_MON_DBOPR_Q
BEFORE INSERT ON LF_MON_DBOPR
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_MON_DBOPR.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_STATECODE_T
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_STATECODE_T
BEFORE INSERT ON LF_STATECODE
FOR EACH ROW
  BEGIN
    IF(:NEW.STATE_ID IS NULL)
    THEN
      SELECT LF_STATECODE_S.NEXTVAL INTO :NEW.STATE_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_SPFEE_LOG_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_SPFEE_LOG_Q
BEFORE INSERT ON LF_SPFEE_LOG
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_SPFEE_LOG.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_LF_SHORTTEMP
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_SHORTTEMP_Q
BEFORE INSERT ON LF_SHORTTEMP
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_SHORTTEMP.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_MT_PRI_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_MT_PRI_Q
BEFORE INSERT ON LF_MT_PRI
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_MT_PRI.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_FILEDATA_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_FILEDATA_Q
BEFORE INSERT ON LF_FILEDATA
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_FILEDATA.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_M_HNETWARN_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_M_HNETWARN_Q
BEFORE INSERT ON LF_MON_HNETWARN
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_M_HNETWARN.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;

/

--
-- Creating trigger LF_M_HOSTNET_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_M_HOSTNET_Q
BEFORE INSERT ON LF_MON_HOSTNET
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_M_HOSTNET.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_B_AR_SEND_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_M_BUSINFO_Q
BEFORE INSERT ON LF_MON_BUSINFO
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_M_BUSINFO.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_B_AR_SEND_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_B_AR_SEND_Q
BEFORE INSERT ON LF_BUSAREASEND
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_B_AR_SEND.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_M_DBSTATE_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_M_DBSTATE_Q
BEFORE INSERT ON LF_MON_DBSTATE
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_M_DBSTATE.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_M_BUSDATA_Q
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_M_BUSDATA_Q
BEFORE INSERT ON LF_MON_BUSDATA
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_M_BUSDATA.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_SPOFFLPRD_T
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_SPOFFLPRD_T
BEFORE INSERT ON LF_SPOFFLINEPRD
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_SPOFFLPRD_S.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_SUMCTRL_T
-- ===============================================
--
CREATE OR REPLACE TRIGGER LF_SUMCTRL_T
BEFORE INSERT ON LF_SUMCTRL
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_SUMCTRL_S.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger CONTRACTSTATE_LF_CONTRACT_TRIG
-- ===============================================
--
CREATE OR REPLACE TRIGGER CONTRACTSTATE_LF_CONTRACT_TRIG
  AFTER UPDATE ON LF_CONTRACT
  FOR EACH ROW
DECLARE
BEGIN
    IF :NEW.CONTRACT_ID<>0 THEN
       UPDATE LF_DEDUCTIONS_DISP SET CONTRACT_STATE=:NEW.CONTRACT_STATE WHERE CONTRACT_ID=:NEW.CONTRACT_ID;
       UPDATE LF_DEDUCTIONS_LIST SET CONTRACT_STATE=:NEW.CONTRACT_STATE WHERE CONTRACT_ID=:NEW.CONTRACT_ID;
    END IF;
END CONTRACTSTATE_LF_CONTRACT_TRIG;
/

--
-- Creating trigger TIG_HISTQUEUE
-- ==============================
--
CREATE OR REPLACE TRIGGER "TIG_HISTQUEUE" BEFORE
INSERT ON  "HISTQUEUE"
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT SEQ_HISTQUEUE.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger LF_SPOFFCTRL_T
-- ==============================
--
CREATE OR REPLACE TRIGGER LF_SPOFFCTRL_T
BEFORE INSERT ON  LF_SPOFFCTRL
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT LF_SPOFFCTRL_S.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_ACCOUNT_BIND_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ACCOUNT_BIND_QUEUE"
BEFORE INSERT ON LF_ACCOUNT_BIND
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_ACCOUNT_BIND.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_ADDRBOOKS_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ADDRBOOKS_QUEUE"
BEFORE INSERT ON LF_ADDRBOOKS
FOR EACH ROW
  
BEGIN
    IF(:new.ADDR_ID IS NULL)
    THEN
      SELECT S_LF_ADDRBOOKS.NEXTVAL INTO :new.ADDR_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_ALSMSRECORD_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ALSMSRECORD_QUEUE"
BEFORE INSERT ON LF_ALSMSRECORD
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_ALSMSRECORD.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APPMAINPAGEHIS_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APPMAINPAGEHIS_QUEUE"
BEFORE INSERT ON LF_APPMAINPAGEHIS
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_APPMAINPAGEHIS.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_CLIDOWLOAD_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_CLIDOWLOAD_QUEUE"
BEFORE INSERT ON LF_APP_CLIDOWLOAD
FOR EACH ROW
  
BEGIN
    IF(:new.CLID IS NULL)
    THEN
      SELECT S_LF_APP_CLIDOWLOAD.NEXTVAL INTO :new.CLID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_DGATEBUF_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_DGATEBUF_QUEUE"
BEFORE INSERT ON LF_MON_DGATEBUF
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_MON_DGATEBUF.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/
--
-- Creating trigger TIG_LF_APP_FOMEITEM_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_FOMEITEM_QUEUE"
BEFORE INSERT ON LF_APP_FOMEITEM
FOR EACH ROW
  
BEGIN
    IF(:new.FIELD_ID IS NULL)
    THEN
      SELECT S_LF_APP_FOMEITEM.NEXTVAL INTO :new.FIELD_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_FOMEVALVE_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_FOMEVALVE_QUEUE"
BEFORE INSERT ON LF_APP_FOMEVALVE
FOR EACH ROW
  
BEGIN
    IF(:new.V_ID IS NULL)
    THEN
      SELECT S_LF_APP_FOMEVALVE.NEXTVAL INTO :new.V_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_FOM_TYPE_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_FOM_TYPE_QUEUE"
BEFORE INSERT ON LF_APP_FOM_TYPE
FOR EACH ROW
  
BEGIN
    IF(:new.TYPE_ID IS NULL)
    THEN
      SELECT S_LF_APP_FOM_TYPE.NEXTVAL INTO :new.TYPE_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_LBS_PIOS_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_LBS_PIOS_QUEUE"
BEFORE INSERT ON LF_APP_LBS_PIOS
FOR EACH ROW
  
BEGIN
    IF(:new.P_ID IS NULL)
    THEN
      SELECT S_LF_APP_LBS_PIOS.NEXTVAL INTO :new.P_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_LBS_PUSH_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_LBS_PUSH_QUEUE"
BEFORE INSERT ON LF_APP_LBS_PUSH
FOR EACH ROW
  
BEGIN
    IF(:new.PUSH_ID IS NULL)
    THEN
      SELECT S_LF_APP_LBS_PUSH.NEXTVAL INTO :new.PUSH_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_LBS_USER_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_LBS_USER_QUEUE"
BEFORE INSERT ON LF_APP_LBS_USER
FOR EACH ROW
  
BEGIN
    IF(:new.UP_ID IS NULL)
    THEN
      SELECT S_LF_APP_LBS_USER.NEXTVAL INTO :new.UP_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_MSGCONTENT_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_MSGCONTENT_QUEUE"
BEFORE INSERT ON LF_APP_MSGCONTENT
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_APP_MSGCONTENT.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_MTMSG_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_MTMSG_QUEUE"
BEFORE INSERT ON LF_APP_MTMSG
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_APP_MTMSG.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_MTTASK_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_MTTASK_QUEUE"
BEFORE INSERT ON LF_APP_MTTASK
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_APP_MTTASK.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_MW_CLIENT_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_MW_CLIENT_QUEUE"
BEFORE INSERT ON LF_APP_MW_CLIENT
FOR EACH ROW
  
BEGIN
    IF(:new.WC_ID IS NULL)
    THEN
      SELECT S_LF_APP_MW_CLIENT.NEXTVAL INTO :new.WC_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_OL_MSGHIS_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_OL_MSGHIS_QUEUE"
BEFORE INSERT ON LF_APP_OL_MSGHIS
FOR EACH ROW
  
BEGIN
    IF(:new.M_ID IS NULL)
    THEN
      SELECT S_LF_APP_OL_MSGHIS.NEXTVAL INTO :new.M_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_REVENT_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_REVENT_QUEUE"
BEFORE INSERT ON LF_APP_REVENT
FOR EACH ROW
  
BEGIN
    IF(:new.EVT_ID IS NULL)
    THEN
      SELECT S_LF_APP_REVENT.NEXTVAL INTO :new.EVT_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_RTEXT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_RTEXT_QUEUE"
BEFORE INSERT ON LF_APP_RTEXT
FOR EACH ROW
  
BEGIN
    IF(:new.TET_ID IS NULL)
    THEN
      SELECT S_LF_APP_RTEXT.NEXTVAL INTO :new.TET_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger LF_BUSMON_Q
-- ===========================================
--
CREATE OR REPLACE TRIGGER  LF_BUSMON_Q
  AFTER UPDATE ON LF_BUSMANAGER
  FOR EACH ROW
BEGIN
  UPDATE LF_MON_BUSBASE SET BUS_NAME=:NEW.BUS_NAME WHERE BUS_CODE=:NEW.BUS_CODE AND CORP_CODE=:NEW.CORP_CODE;
END LF_BUSMON_Q;
/

--
-- Creating trigger LF_M_BUSBASE_Q
-- ===========================================
--
CREATE OR REPLACE TRIGGER LF_M_BUSBASE_Q
BEFORE INSERT ON LF_MON_BUSBASE
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT S_LF_M_BUSBASE.NEXTVAL INTO :NEW.ID FROM DUAL;
      END IF;
      END;
/


--
-- Creating trigger TIG_LF_APP_SIT_PLANT_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_SIT_PLANT_QUEUE"
BEFORE INSERT ON LF_APP_SIT_PLANT
FOR EACH ROW
  
BEGIN
    IF(:new.PLANT_ID IS NULL)
    THEN
      SELECT S_LF_APP_SIT_PLANT.NEXTVAL INTO :new.PLANT_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_SIT_TYPE_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_SIT_TYPE_QUEUE"
BEFORE INSERT ON LF_APP_SIT_TYPE
FOR EACH ROW
  
BEGIN
    IF(:new.TYPE_ID IS NULL)
    THEN
      SELECT S_LF_APP_SIT_TYPE.NEXTVAL INTO :new.TYPE_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_TC_MOMSG_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_TC_MOMSG_QUEUE"
BEFORE INSERT ON LF_APP_TC_MOMSG
FOR EACH ROW
  
BEGIN
    IF(:new.MSG_ID IS NULL)
    THEN
      SELECT S_LF_APP_TC_MOMSG.NEXTVAL INTO :new.MSG_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_APP_VERIFY_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_APP_VERIFY_QUEUE"
BEFORE INSERT ON LF_APP_VERIFY
FOR EACH ROW
  
BEGIN
    IF(:new.VF_ID IS NULL)
    THEN
      SELECT S_LF_APP_VERIFY.NEXTVAL INTO :new.VF_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_AUTOREPLY_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_AUTOREPLY_QUEUE"
BEFORE INSERT ON LF_AUTOREPLY
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_SYSMTTASK.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_BILLLOG_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BILLLOG_QUEUE"
BEFORE INSERT ON LF_BILLLOG
FOR EACH ROW
  
BEGIN
    IF(:new.LOG_ID IS NULL)
    THEN
      SELECT S_LF_BILLLOG.NEXTVAL INTO :new.LOG_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_BINDFLOW_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BINDFLOW_QUEUE"
BEFORE INSERT ON LF_BINDFLOW
FOR EACH ROW
  
BEGIN
    IF(:new.BFID IS NULL)
    THEN
      SELECT S_LF_BINDFLOW.NEXTVAL INTO :new.BFID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_BIRTHDAYMEMBER_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BIRTHDAYMEMBER_QUEUE"
BEFORE INSERT ON LF_BIRTHDAYMEMBER
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_BIRTHDAYMEMBER.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_BIRTHDAYSETUP_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BIRTHDAYSETUP_QUEUE"
BEFORE INSERT ON LF_BIRTHDAYSETUP
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_BIRTHDAYSETUP.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_BLACKLIST_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BLACKLIST_QUEUE"
BEFORE INSERT ON LF_BLACKLIST
FOR EACH ROW
  
BEGIN
    IF(:new.BL_ID IS NULL)
    THEN
      SELECT S_LF_BLACKLIST.NEXTVAL INTO :new.BL_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_BUSINESS_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BUSINESS_QUEUE"
BEFORE INSERT ON LF_BUSINESS
FOR EACH ROW
 
BEGIN
    IF(:new.BUS_ID IS NULL)
    THEN
    SELECT S_LF_BUSINESS.NEXTVAL INTO :new.BUS_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger LF_DRAFTS_T
-- ========================================
--
CREATE OR REPLACE TRIGGER LF_DRAFTS_T
BEFORE INSERT ON LF_DRAFTS
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_DRAFTS_S.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END;
/

--
-- Creating trigger TIG_LF_URLTASK
-- ========================================
--
CREATE OR REPLACE TRIGGER TIG_LF_URLTASK
BEFORE INSERT ON LF_URLTASK
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_URLTASK_S.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END;
/

--
-- Creating trigger TIG_LF_NETURL
-- ========================================
--
CREATE OR REPLACE TRIGGER TIG_LF_NETURL
BEFORE INSERT ON LF_NETURL
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_NETURL_S.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END;
/

--
-- Creating trigger TIG_LF_DOMAIN
-- ========================================
--
CREATE OR REPLACE TRIGGER TIG_LF_DOMAIN
BEFORE INSERT ON LF_DOMAIN
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_DOMAIN_S.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END;
/

--
-- Creating trigger TIG_LF_SUB_DRAFTS
-- ========================================
--
CREATE OR REPLACE TRIGGER TIG_LF_SUB_DRAFTS
BEFORE INSERT ON LF_SUB_DRAFTS
FOR EACH ROW
  BEGIN
    IF(:NEW.ID IS NULL)
    THEN
      SELECT LF_SUB_DRAFTS_S.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END;
/

--
-- Creating trigger TIG_LF_BUSMANAGER_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BUSMANAGER_QUEUE"
before insert  on LF_BUSMANAGER
FOR EACH ROW
BEGIN
    IF(:new.BUS_ID IS NULL)
    THEN
      SELECT S_LF_BUSMANAGER.NEXTVAL INTO :new.BUS_ID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_BUSPKGETAOCAN_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BUSPKGETAOCAN_QUEUE"
BEFORE INSERT ON LF_BUSPKGETAOCAN
FOR EACH ROW
BEGIN
    IF (:new.ID IS NULL)  THEN
      SELECT S_LF_BUSPKGETAOCAN.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_BUSTAIL_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BUSTAIL_QUEUE"
BEFORE INSERT ON LF_BUSTAIL
FOR EACH ROW
BEGIN
    IF (:new.BUSTAIL_ID IS NULL)  THEN
      SELECT S_LF_BUSTAIL.NEXTVAL INTO :new.BUSTAIL_ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_BUS_PROCESS_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BUS_PROCESS_QUEUE"
BEFORE INSERT ON LF_BUS_PROCESS
FOR EACH ROW
  
BEGIN
    IF(:new.BUSPRO_ID IS NULL)
    THEN
      SELECT S_LF_BUS_PROCESS.NEXTVAL INTO :new.BUSPRO_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_BUS_TAILTMP_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BUS_TAILTMP_QUEUE"
BEFORE INSERT ON LF_BUS_TAILTMP
FOR EACH ROW
BEGIN
    IF (:new.ID IS NULL)  THEN
      SELECT S_LF_BUS_TAILTMP.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_BUS_TAOCAN_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_BUS_TAOCAN_QUEUE"
BEFORE INSERT ON LF_BUS_TAOCAN
FOR EACH ROW
BEGIN
    IF (:new.TAOCAN_ID IS NULL)  THEN
      SELECT S_LF_BUS_TAOCAN.NEXTVAL INTO :new.TAOCAN_ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_CLIDEP_CONN_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CLIDEP_CONN_QUEUE"
BEFORE INSERT ON LF_CLIDEP_CONN
FOR EACH ROW
 
BEGIN
    IF(:new.CONN_ID IS NULL)
    THEN
    SELECT S_LF_CLIDEP_CONN.NEXTVAL INTO :new.CONN_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_CLIENTCLASS_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CLIENTCLASS_QUEUE"
BEFORE INSERT ON LF_CLIENTCLASS
FOR EACH ROW
  
BEGIN
    IF(:new.CC_ID IS NULL)
    THEN
    SELECT S_LF_CLIENTCLASS.nextval INTO :new.CC_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_CLIENT_DEP_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CLIENT_DEP_QUEUE"
before insert  on  LF_CLIENT_DEP
FOR EACH ROW
  
BEGIN
    IF(:new.DEP_ID IS NULL)
    THEN
      SELECT S_LF_CLIENT_DEP.NEXTVAL INTO :new.DEP_ID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_CONTRACT_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CONTRACT_QUEUE"
BEFORE INSERT ON LF_CONTRACT
FOR EACH ROW
BEGIN
    IF (:new.CONTRACT_ID IS NULL)  THEN
      SELECT S_LF_CONTRACT.NEXTVAL INTO :new.CONTRACT_ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_CONTRACT_TAOCAN_QUEUE
-- =============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CONTRACT_TAOCAN_QUEUE"
BEFORE INSERT ON LF_CONTRACT_TAOCAN
FOR EACH ROW
BEGIN
    IF (:new.ID IS NULL)  THEN
      SELECT S_LF_CONTRACT_TAOCAN.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_CORP_CONF_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CORP_CONF_QUEUE"
BEFORE INSERT ON LF_CORP_CONF
FOR EACH ROW
  
BEGIN
    IF(:new.CC_ID IS NULL)
    THEN
      SELECT S_LF_CORP_CONF.NEXTVAL INTO :new.CC_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_CORP_QUEUE
-- ==================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CORP_QUEUE"
BEFORE INSERT ON LF_CORP
FOR EACH ROW
  
BEGIN
    IF(:new.CORP_ID IS NULL)
    THEN
      SELECT S_LF_CORP.NEXTVAL INTO :new.CORP_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_CUSTFIELD_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CUSTFIELD_QUEUE"
BEFORE INSERT ON LF_CUSTFIELD
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
    SELECT S_LF_CUSTFIELD.nextval INTO :new.ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_CUSTFIELD_VALUE_QUEUE
-- =============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_CUSTFIELD_VALUE_QUEUE"
BEFORE INSERT ON LF_CUSTFIELD_VALUE
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
    SELECT S_LF_CUSTFIELD_VALUE.nextval INTO :new.ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_DBCONNECT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DBCONNECT_QUEUE"
BEFORE INSERT ON LF_DBCONNECT
FOR EACH ROW
  
BEGIN
    IF(:new.DB_ID IS NULL)
    THEN
      SELECT S_LF_DBCONNECT.NEXTVAL INTO :new.DB_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_DEDUCTIONS_DISP_QUEUE
-- =============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DEDUCTIONS_DISP_QUEUE"
BEFORE INSERT ON LF_DEDUCTIONS_DISP
FOR EACH ROW
BEGIN
    IF (:new.ID IS NULL)  THEN
      SELECT S_LF_DEDUCTIONS_DISP.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_DEDUCTIONS_LIST_QUEUE
-- =============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DEDUCTIONS_LIST_QUEUE"
BEFORE INSERT ON LF_DEDUCTIONS_LIST
FOR EACH ROW
BEGIN
    IF (:new.ID IS NULL)  THEN
      SELECT S_LF_DEDUCTIONS_LIST.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_DEPPWDRECEIVER_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DEPPWDRECEIVER_QUEUE"
BEFORE INSERT ON LF_DEPPWDRECEIVER
FOR EACH ROW
  
BEGIN
    IF(:new.DPR_ID IS NULL)
    THEN
      SELECT S_LF_DEPPWDRECEIVER.NEXTVAL INTO :new.DPR_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_DEP_QUEUE
-- =================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DEP_QUEUE"
BEFORE INSERT ON LF_DEP
FOR EACH ROW
  
BEGIN
    IF(:new.DEP_ID IS NULL)
    THEN
    SELECT S_LF_DEP.nextval INTO :new.DEP_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_DEP_RECHARGE_LOG_QUEUE
-- ==============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DEP_RECHARGE_LOG_QUEUE"
BEFORE INSERT ON LF_DEP_RECHARGE_LOG
FOR EACH ROW
  
BEGIN
    IF(:new.LOG_ID IS NULL)
    THEN
      SELECT S_LF_DEP_RECHARGE_LOG.NEXTVAL INTO :new.LOG_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_DEP_TAOCAN_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DEP_TAOCAN_QUEUE"
BEFORE INSERT ON LF_DEP_TAOCAN
FOR EACH ROW
BEGIN
    IF (:new.ID IS NULL)  THEN
      SELECT S_LF_DEP_TAOCAN.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_DEP_USER_BALANCE_QUEUE
-- ==============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DEP_USER_BALANCE_QUEUE"
BEFORE INSERT ON LF_DEP_USER_BALANCE
FOR EACH ROW
BEGIN
    IF(:new.BL_ID IS NULL)
    THEN
      SELECT S_LF_DEP_USER_BALANCE.NEXTVAL INTO :new.BL_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_DFADVANCED_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_DFADVANCED_QUEUE"
BEFORE INSERT ON LF_DFADVANCED
FOR EACH ROW
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_DFADVANCED.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_EDIT_QUEUE
-- ==================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_EDIT_QUEUE"
BEFORE INSERT ON LF_EDIT
FOR EACH ROW
  
BEGIN
    IF(:new.EDIT_ID IS NULL)
    THEN
      SELECT S_LF_EDIT.NEXTVAL INTO :new.EDIT_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_EMPDEP_CONN_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_EMPDEP_CONN_QUEUE"
BEFORE INSERT ON LF_EMPDEP_CONN
FOR EACH ROW
 
BEGIN
    IF(:new.CONN_ID IS NULL)
    THEN
    SELECT S_LF_EMPDEP_CONN.NEXTVAL INTO :new.CONN_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_EMPLOYEE_DEP_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_EMPLOYEE_DEP_QUEUE"
before insert  on  LF_EMPLOYEE_DEP
FOR EACH ROW
  
BEGIN
    IF(:new.DEP_ID IS NULL)
    THEN
      SELECT S_LF_EMPLOYEE_DEP.NEXTVAL INTO :new.DEP_ID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_EMPLOYEE_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_EMPLOYEE_QUEUE"
BEFORE INSERT ON LF_EMPLOYEE
FOR EACH ROW
 
BEGIN
    IF(:new.EMPLOYEEID IS NULL)
    THEN
    SELECT S_LF_EMPLOYEE.NEXTVAL INTO :new.EMPLOYEEID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_EMPLOYEE_TYPE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_EMPLOYEE_TYPE" BEFORE
INSERT ON  "LF_EMPLOYEE_TYPE"
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT LF_EMPLOYEE_TYPE_SEQUENCE.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_ENTERPRISE_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ENTERPRISE_QUEUE"
before insert  on LF_ENTERPRISE
FOR EACH ROW
  
BEGIN
    IF(:new.OR_ID IS NULL)
    THEN
      SELECT S_LF_ENTERPRISE.NEXTVAL INTO :new.OR_ID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_EXAMINE_SMS_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_EXAMINE_SMS_QUEUE"
BEFORE INSERT ON LF_EXAMINE_SMS
FOR EACH ROW
  
BEGIN
    IF(:new.ES_ID IS NULL)
    THEN
      SELECT S_LF_EXAMINE_SMS.NEXTVAL INTO :new.ES_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_EXTERNAL_USER_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_EXTERNAL_USER_QUEUE"
BEFORE INSERT ON LF_EXTERNAL_USER
FOR EACH ROW
  
BEGIN
    IF(:new.EXUID IS NULL)
    THEN
      SELECT S_LF_EXTERNAL_USER.NEXTVAL INTO :new.EXUID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_FLOWBINDOBJ_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_FLOWBINDOBJ_QUEUE"
BEFORE INSERT ON LF_FLOWBINDOBJ
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_FLOWBINDOBJ.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_FLOWBINDTYPE_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_FLOWBINDTYPE_QUEUE"
BEFORE INSERT ON LF_FLOWBINDTYPE
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_FLOWBINDTYPE.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_FLOWRECORD
-- ==================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_FLOWRECORD"
BEFORE INSERT ON LF_FLOWRECORD
FOR EACH ROW

BEGIN
    IF(:new.FR_ID IS NULL)
    THEN
      SELECT S_LF_FLOWRECORD.NEXTVAL INTO :new.FR_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_GLOBAL_VARIABLE_QUEUE
-- =============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_GLOBAL_VARIABLE_QUEUE"
BEFORE INSERT ON LF_GLOBAL_VARIABLE
FOR EACH ROW
 
BEGIN
    IF(:new.GLOBALID IS NULL)
    THEN
    SELECT S_LF_GLOBAL_VARIABLE.NEXTVAL INTO :new.GLOBALID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_IM_CLIENT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_CLIENT_QUEUE" BEFORE
INSERT ON "LF_IM_CLIENT"
    FOR EACH ROW
BEGIN
IF (:new.CLIENTID IS NULL) THEN
SELECT S_LF_IM_CLIENT.nextval INTO :new.CLIENTID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_IM_DIALOG_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_DIALOG_QUEUE"
BEFORE INSERT ON LF_IM_DIALOG
FOR EACH ROW
 
BEGIN
    IF(:new.DIAID IS NULL)
    THEN
    SELECT S_LF_IM_DIALOG.NEXTVAL INTO :new.DIAID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_IM_GPUSERLINK_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_GPUSERLINK_QUEUE"
BEFORE INSERT ON LF_IM_GPUSERLINK
FOR EACH ROW
  
BEGIN
    IF(:new.LINK_ID IS NULL)
    THEN
      SELECT S_LF_IM_GPUSERLINK.NEXTVAL INTO :new.LINK_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_IM_GROUPMESSAGE_QUEUE
-- =============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_GROUPMESSAGE_QUEUE"
BEFORE INSERT ON LF_IM_GROUPMESSAGE
FOR EACH ROW
  
BEGIN
    IF(:new.MSGID IS NULL)
    THEN
      SELECT S_LF_IM_GROUPMESSAGE.NEXTVAL INTO :new.MSGID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_IM_GROUP_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_GROUP_QUEUE" BEFORE
INSERT ON "LF_IM_GROUP"
    FOR EACH ROW
BEGIN
IF (:new.GROUPID IS NULL) THEN
SELECT S_LF_IM_GROUP.nextval INTO :new.GROUPID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_IM_HISTORY_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_HISTORY_QUEUE" BEFORE
INSERT ON "LF_IM_HISTORY"
    FOR EACH ROW
BEGIN
IF (:new.HISTORYID IS NULL) THEN
SELECT S_LF_IM_HISTORY.nextval INTO :new.HISTORYID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_IM_MEMBERGROUP_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_MEMBERGROUP_QUEUE"
BEFORE INSERT ON LF_IM_MEMBERGROUP
FOR EACH ROW
 
BEGIN
    IF(:new.MEMBERID IS NULL)
    THEN
    SELECT S_LF_IM_MEMBERGROUP.NEXTVAL INTO :new.MEMBERID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_IM_OFFICEGROUP_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_OFFICEGROUP_QUEUE"
BEFORE INSERT ON LF_IM_OFFICEGROUP
FOR EACH ROW
 
BEGIN
    IF(:new.GROUPID IS NULL)
    THEN
    SELECT S_LF_IM_OFFICEGROUP.NEXTVAL INTO :new.GROUPID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_IM_RECENTCONTACTS_QUEUE
-- ===============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_RECENTCONTACTS_QUEUE"
BEFORE INSERT ON LF_IM_RECENTCONTACTS
FOR EACH ROW
 
BEGIN
    IF(:new.RC_ID IS NULL)
    THEN
    SELECT S_LF_IM_RECENTCONTACTS.NEXTVAL INTO :new.RC_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_IM_SEATGROUP_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEATGROUP_QUEUE" BEFORE
INSERT ON "LF_IM_SEATGROUP"
    FOR EACH ROW
BEGIN
IF (:new.SEATGROUPID IS NULL) THEN
SELECT S_LF_IM_SEATGROUP.nextval INTO :new.SEATGROUPID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_IM_SEATLIMIT_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEATLIMIT_QUEUE" BEFORE
INSERT ON "LF_IM_SEATLIMIT"
    FOR EACH ROW
BEGIN
IF (:new.ID IS NULL) THEN
SELECT S_LF_IM_SEATLIMIT.nextval INTO :new.ID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_IM_SEATUSER_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEATUSER_QUEUE" BEFORE
INSERT ON "LF_IM_SEATUSER"
    FOR EACH ROW
BEGIN
IF (:new.ID IS NULL) THEN
SELECT S_LF_IM_SEATUSER.nextval INTO :new.ID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_LF_IM_SEAT_CGLINK_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEAT_CGLINK_QUEUE"
BEFORE INSERT ON LF_IM_SEAT_CGLINK
FOR EACH ROW
  
BEGIN
    IF(:new.CGLINK_ID IS NULL)
    THEN
      SELECT S_LF_IM_SEAT_CGLINK.NEXTVAL INTO :new.CGLINK_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_IM_SEAT_CLIENTGP_QUEUE
-- ==============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEAT_CLIENTGP_QUEUE"
BEFORE INSERT ON LF_IM_SEAT_CLIENTGP
FOR EACH ROW
  
BEGIN
    IF(:new.CGID IS NULL)
    THEN
      SELECT S_LF_IM_SEAT_CLIENTGP.NEXTVAL INTO :new.CGID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_IM_SEAT_CLIENT_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEAT_CLIENT_QUEUE"
BEFORE INSERT ON LF_IM_SEAT_CLIENT
FOR EACH ROW
  
BEGIN
    IF(:new.CLIENT_ID IS NULL)
    THEN
      SELECT S_LF_IM_SEAT_CLIENT.NEXTVAL INTO :new.CLIENT_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_IM_USERMESSAGE_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_USERMESSAGE_QUEUE"
BEFORE INSERT ON LF_IM_USERMESSAGE
FOR EACH ROW
  
BEGIN
    IF(:new.MSGID IS NULL)
    THEN
      SELECT S_LF_IM_USERMESSAGE.NEXTVAL INTO :new.MSGID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_IM_USERSUBNO_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_USERSUBNO_QUEUE"
BEFORE INSERT ON LF_IM_USERSUBNO
FOR EACH ROW
 
BEGIN
    IF(:new.IMSUBNO_ID IS NULL)
    THEN
    SELECT S_LF_IM_USERSUBNO.NEXTVAL INTO :new.IMSUBNO_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_KEYWORDS_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_KEYWORDS_QUEUE"
BEFORE INSERT ON LF_KEYWORDS
FOR EACH ROW
  
BEGIN
    IF(:new.KW_ID IS NULL)
    THEN
      SELECT S_LF_KEYWORDS.NEXTVAL INTO :new.KW_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_KEY_VALUE_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_KEY_VALUE_QUEUE"
BEFORE INSERT ON LF_KEY_VALUE
FOR EACH ROW
  
BEGIN
    IF(:new.KV_ID IS NULL)
    THEN
      SELECT S_LF_KEY_VALUE.NEXTVAL INTO :new.KV_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_LIST2GRO_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_LIST2GRO_QUEUE"
BEFORE INSERT ON LF_LIST2GRO
FOR EACH ROW
  
BEGIN
    IF(:new.L2G_ID IS NULL)
    THEN
      SELECT S_LF_LIST2GRO.NEXTVAL INTO :new.L2G_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MACIP_QUEUE
-- ===================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MACIP_QUEUE"
BEFORE INSERT ON LF_MACIP
FOR EACH ROW
  
BEGIN
    IF(:new.LMIID IS NULL)
    THEN
      SELECT S_LF_MACIP.NEXTVAL INTO :new.LMIID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_MALIST_QUEUE
-- ====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MALIST_QUEUE"
BEFORE INSERT ON LF_MALIST
FOR EACH ROW
  
BEGIN
    IF(:new.MA_ID IS NULL)
    THEN
      SELECT S_LF_MALIST.NEXTVAL INTO :new.MA_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MAPPING_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MAPPING_QUEUE"
BEFORE INSERT ON LF_MAPPING
FOR EACH ROW
BEGIN
    IF(:new.MAP_ID IS NULL)
    THEN
    SELECT S_LF_MAPPING.nextval INTO :new.MAP_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_MATERIAL_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MATERIAL_QUEUE"
BEFORE INSERT ON LF_MATERIAL
FOR EACH ROW
BEGIN
    IF(:new.MTAL_ID IS NULL)
    THEN
      SELECT S_LF_MATERIAL.NEXTVAL INTO :new.MTAL_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MATERIAL_SORT_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MATERIAL_SORT_QUEUE"
BEFORE INSERT ON LF_MATERIAL_SORT
FOR EACH ROW
BEGIN
    IF(:new.SORT_ID IS NULL)
    THEN
      SELECT S_LF_MATERIAL_SORT.NEXTVAL INTO :new.SORT_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MMSACCBIND_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MMSACCBIND_QUEUE"
BEFORE INSERT ON LF_MMSACCBIND
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MMSACCBIND.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MMSACCMANAGEMENT_QUEUE
-- ==============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MMSACCMANAGEMENT_QUEUE"
BEFORE INSERT ON LF_MMSACCMANAGEMENT
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MMSACCMANAGEMENT.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MMSBLIST_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MMSBLIST_QUEUE"
BEFORE INSERT ON LF_MMSBLIST
FOR EACH ROW
  BEGIN
    IF(:new.BL_ID IS NULL)
    THEN
      SELECT S_LF_MMSBLIST.NEXTVAL INTO :new.BL_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MMSINFO_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MMSINFO_QUEUE"
before insert  on LF_MMSINFO
FOR EACH ROW
  BEGIN
    IF(:new.MMSID IS NULL)
    THEN
      SELECT S_LF_MMSINFO.NEXTVAL INTO :new.MMSID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_MMSMTTASK_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MMSMTTASK_QUEUE"
BEFORE INSERT ON LF_MMSMTTASK
FOR EACH ROW
  BEGIN
    IF(:new.MMS_ID IS NULL)
    THEN
      SELECT S_LF_MMSMTTASK.NEXTVAL INTO :new.MMS_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MMSTEMPLATE_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MMSTEMPLATE_QUEUE"
BEFORE INSERT ON LF_MMSTEMPLATE
FOR EACH ROW
  BEGIN
    IF(:new.MMS_ID IS NULL)
    THEN
      SELECT S_LF_MMSTEMPLATE.NEXTVAL INTO :new.MMS_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MMS_MTREPORT_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MMS_MTREPORT_QUEUE"
BEFORE INSERT ON LF_MMS_MTREPORT
FOR EACH ROW
  BEGIN
  IF(:new.ID IS NULL)
  THEN
    SELECT S_LF_MMS_MTREPORT.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_MOBFINANCIAL_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MOBFINANCIAL_QUEUE"
BEFORE INSERT ON LF_MOBFINANCIAL
FOR EACH ROW
  BEGIN
    IF(:new.MOBFID IS NULL)
    THEN
      SELECT S_LF_MOBFINANCIAL.NEXTVAL INTO :new.MOBFID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_DGTACINFO_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_DGTACINFO_QUEUE"
BEFORE INSERT ON LF_MON_DGTACINFO
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_DGTACINFO.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_DHOST_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_DHOST_QUEUE"
BEFORE INSERT ON LF_MON_DHOST
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_DHOST.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_DPROCE_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_DPROCE_QUEUE"
BEFORE INSERT ON LF_MON_DPROCE
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_DPROCE.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_DSPACINFO_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_DSPACINFO_QUEUE"
BEFORE INSERT ON LF_MON_DSPACINFO
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_DSPACINFO.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_ERRHIS_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_ERRHIS_QUEUE"
BEFORE INSERT ON LF_MON_ERRHIS
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_ERRHIS.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_ERR_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_ERR_QUEUE"
BEFORE INSERT ON LF_MON_ERR
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_ERR.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_ONLCFG_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_ONLCFG_QUEUE"
BEFORE INSERT ON LF_MON_ONLCFG
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_ONLCFG.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_SHOST_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_SHOST_QUEUE"
BEFORE INSERT ON LF_MON_SHOST
FOR EACH ROW
  BEGIN
    IF(:new.HOSTID IS NULL)
    THEN
      SELECT S_LF_MON_SHOST.NEXTVAL INTO :new.HOSTID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_SPGATEBUF_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_SPGATEBUF_QUEUE"
BEFORE INSERT ON LF_MON_SPGATEBUF
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_SPGATEBUF.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_SPROCE_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_SPROCE_QUEUE"
BEFORE INSERT ON LF_MON_SPROCE
FOR EACH ROW
  BEGIN
    IF(:new.ProceID IS NULL)
    THEN
      SELECT S_LF_MON_SPROCE.NEXTVAL INTO :new.ProceID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MON_WBSBUF_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MON_WBSBUF_QUEUE"
BEFORE INSERT ON LF_MON_WBSBUF
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MON_WBSBUF.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MOTASK_QUEUE
-- ====================================
--
CREATE OR REPLACE TRIGGER TIG_LF_MOTASK_QUEUE
BEFORE INSERT ON LF_MOTASK
FOR EACH ROW
  BEGIN
    IF(:new.MO_ID IS NULL)
    THEN
    SELECT S_LF_MOTASK.NEXTVAL INTO :new.MO_ID FROM dual;
    END IF;
  END;
/

--
-- Creating trigger TIG_LF_MO_SERVICE
-- ==================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MO_SERVICE"
before insert  on LF_MO_SERVICE
FOR EACH ROW
  BEGIN
    IF(:new.MS_ID IS NULL)
    THEN
      SELECT S_LF_MO_SERVICE.NEXTVAL INTO :new.MS_ID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_MTRPT_EC_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MTRPT_EC_QUEUE"
BEFORE INSERT ON LF_MTRPT_EC
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_MTRPT_EC.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_MTTASK_QUEUE
-- ====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_MTTASK_QUEUE"
BEFORE INSERT ON LF_MTTASK
FOR EACH ROW
  BEGIN
    IF(:new.MT_ID IS NULL)
    THEN
      SELECT S_LF_MTTASK.NEXTVAL INTO :new.MT_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_NOTICE_QUEUE
-- ====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_NOTICE_QUEUE"
BEFORE INSERT ON LF_NOTICE
FOR EACH ROW
  BEGIN
    IF(:new.NOTICE_ID IS NULL)
    THEN
      SELECT S_LF_NOTICE.NEXTVAL INTO :new.NOTICE_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_ONLINE_USER_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ONLINE_USER_QUEUE"
BEFORE INSERT ON LF_ONLINE_USER
FOR EACH ROW
  BEGIN
    IF(:new.ONUID IS NULL)
    THEN
      SELECT S_LF_ONLINE_USER.NEXTVAL INTO :new.ONUID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_OPERATE_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_OPERATE_QUEUE"
before insert  on LF_OPERATE
FOR EACH ROW
  BEGIN
    IF(:new.OPERATE_ID IS NULL)
    THEN
      SELECT S_LF_OPERATE.NEXTVAL INTO :new.OPERATE_ID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_OPRATELOG_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_OPRATELOG_QUEUE"
BEFORE INSERT ON LF_OPRATELOG
FOR EACH ROW
  BEGIN
    IF(:new.LOG_ID IS NULL)
    THEN
      SELECT S_LF_OPRATELOG.NEXTVAL INTO :new.LOG_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PARAM_QUEUE
-- ===================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PARAM_QUEUE"
BEFORE INSERT ON LF_PARAM
FOR EACH ROW
  BEGIN
    IF(:new.PARAM_ID IS NULL)
    THEN
      SELECT S_LF_PARAM.NEXTVAL INTO :new.PARAM_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PERFECT_NOTIC_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PERFECT_NOTIC_QUEUE"
BEFORE INSERT ON LF_PERFECT_NOTIC
FOR EACH ROW
  BEGIN
    IF(:new.P_NOTIC_ID IS NULL)
    THEN
      SELECT S_LF_PERFECT_NOTIC.NEXTVAL INTO :new.P_NOTIC_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PERSONALCONFIG_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PERSONALCONFIG_QUEUE"
BEFORE INSERT ON LF_PERSONALCONFIG
FOR EACH ROW
 BEGIN
    IF(:new.PER_ID IS NULL)
    THEN
    SELECT S_LF_PERSONALCONFIG.NEXTVAL INTO :new.PER_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_POSITION_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_POSITION_QUEUE"
BEFORE INSERT ON LF_POSITION
FOR EACH ROW
  BEGIN
    IF(:new.P_ID IS NULL)
    THEN
      SELECT S_LF_POSITION.NEXTVAL INTO :new.P_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PRIVILEGE_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PRIVILEGE_QUEUE"
BEFORE INSERT ON LF_PRIVILEGE
FOR EACH ROW
 BEGIN
    IF(:new.PRIVILEGE_ID IS NULL)
    THEN
    SELECT S_LF_PRIVILEGE.NEXTVAL INTO :new.PRIVILEGE_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_PROCESS_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PROCESS_QUEUE"
BEFORE INSERT ON LF_PROCESS
FOR EACH ROW
  BEGIN
    IF(:new.PR_ID IS NULL)
    THEN
      SELECT S_LF_PROCESS.NEXTVAL INTO :new.PR_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PROCHARGES_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PROCHARGES_QUEUE"
BEFORE INSERT ON LF_PROCHARGES
FOR EACH ROW
BEGIN
    IF (:new.RULE_ID IS NULL)  THEN
      SELECT S_LF_PROCHARGES.NEXTVAL INTO :new.RULE_ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_PRODUCTOR_CONT_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PRODUCTOR_CONT_QUEUE"
BEFORE INSERT ON LF_PRODUCTOR_CONT
FOR EACH ROW
  BEGIN
    IF(:new.IDEN_ID IS NULL)
    THEN
      SELECT S_LF_PRODUCTOR_CONT.NEXTVAL INTO :new.IDEN_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PRODUCTS_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PRODUCTS_QUEUE"
BEFORE INSERT ON LF_PRODUCTS
FOR EACH ROW
  BEGIN
    IF(:new.PRO_ID IS NULL)
    THEN
      SELECT S_LF_PRODUCTS.NEXTVAL INTO :new.PRO_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PROSENDCOUNT_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PROSENDCOUNT_QUEUE"
BEFORE INSERT ON LF_PROSENDCOUNT
FOR EACH ROW
  BEGIN
    IF(:new.CID IS NULL)
    THEN
      SELECT S_LF_PROSENDCOUNT.NEXTVAL INTO :new.CID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_PRO_CON_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_PRO_CON_QUEUE"
BEFORE INSERT ON LF_PRO_CON
FOR EACH ROW
  BEGIN
    IF(:new.DBCON_ID IS NULL)
    THEN
      SELECT S_LF_PRO_CON.NEXTVAL INTO :new.DBCON_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_P_N_UP_QUEUE
-- ====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_P_N_UP_QUEUE"
BEFORE INSERT ON LF_P_N_UP
FOR EACH ROW
  BEGIN
    IF(:new.PNUP_ID IS NULL)
    THEN
      SELECT S_LF_P_N_UP.NEXTVAL INTO :new.PNUP_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_REPLY_QUEUE
-- ===================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_REPLY_QUEUE"
BEFORE INSERT ON LF_REPLY
FOR EACH ROW
  BEGIN
    IF(:new.RE_ID IS NULL)
    THEN
      SELECT S_LF_REPLY.NEXTVAL INTO :new.RE_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_RESOURCE_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_RESOURCE_QUEUE"
BEFORE INSERT ON LF_RESOURCE
FOR EACH ROW
  BEGIN
    IF(:new.RESOURCE_ID  IS NULL)
    THEN
      SELECT S_LF_RESOURCE.NEXTVAL INTO :new.RESOURCE_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_REVIEWER2LEVEL_QUEUE
-- ============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_REVIEWER2LEVEL_QUEUE"
BEFORE INSERT ON LF_REVIEWER2LEVEL
FOR EACH ROW
  BEGIN
    IF(:new.FRL_ID IS NULL)
    THEN
      SELECT S_LF_REVIEWER2LEVEL.NEXTVAL INTO :new.FRL_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_REVIEWSWITCH_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_REVIEWSWITCH_QUEUE"
BEFORE INSERT ON LF_REVIEWSWITCH
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_REVIEWSWITCH.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_ROLES_QUEUE
-- ===================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ROLES_QUEUE"
BEFORE INSERT ON LF_ROLES
FOR EACH ROW
  BEGIN
    IF(:new.ROLE_ID IS NULL)
    THEN
      SELECT S_LF_ROLES.NEXTVAL INTO :new.ROLE_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SENDGRO_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SENDGRO_QUEUE"
BEFORE INSERT ON LF_SENDGRO
FOR EACH ROW
  BEGIN
    IF(:new.SG_ID IS NULL)
    THEN
      SELECT S_LF_SENDGRO.Nextval INTO :new.SG_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SERVICELOG_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SERVICELOG_QUEUE"
BEFORE INSERT ON LF_SERVICELOG
FOR EACH ROW
  BEGIN
    IF(:new.SL_ID IS NULL)
    THEN
      SELECT S_LF_SERVICELOG.NEXTVAL INTO :new.SL_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SERVICEMSG_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SERVICEMSG_QUEUE"
BEFORE INSERT ON LF_SERVICEMSG
FOR EACH ROW
  BEGIN
    IF(:new.SERMSGID IS NULL)
    THEN
      SELECT S_LF_SERVICEMSG.NEXTVAL INTO :new.SERMSGID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SERVICE_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SERVICE_QUEUE"
BEFORE INSERT ON LF_SERVICE
FOR EACH ROW
  BEGIN
  IF(:new.SER_ID IS NULL)
  THEN
    SELECT S_LF_SERVICE.NEXTVAL INTO :new.SER_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_SKIN_QUEUE
-- ==================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SKIN_QUEUE"
BEFORE INSERT ON LF_SKIN
FOR EACH ROW
  BEGIN
    IF(:new.SID IS NULL)
    THEN
      SELECT S_LF_SKIN.NEXTVAL INTO :new.SID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SPFEE_QUEUE
-- ===================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SPFEE_QUEUE"
BEFORE INSERT ON LF_SPFEE
FOR EACH ROW
  BEGIN
    IF(:new.SF_ID IS NULL)
    THEN
      SELECT S_LF_SPFEE.NEXTVAL INTO :new.SF_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SP_DEP_BIND_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SP_DEP_BIND_QUEUE"
BEFORE INSERT ON LF_SP_DEP_BIND
FOR EACH ROW
 BEGIN
    IF(:new.DSG_ID IS NULL)
    THEN
    SELECT S_LF_SP_DEP_BIND.NEXTVAL INTO :new.DSG_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_SQLWHERE_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SQLWHERE_QUEUE"
BEFORE INSERT ON LF_SQLWHERE
FOR EACH ROW
  BEGIN
    IF(:new.WH_ID IS NULL)
    THEN
      SELECT S_LF_SQLWHERE.Nextval INTO :new.WH_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SUBNOALLOT_DETAIL_QUEUE
-- ===============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SUBNOALLOT_DETAIL_QUEUE"
BEFORE INSERT ON LF_SUBNOALLOT_DETAIL
FOR EACH ROW
 BEGIN
    IF(:new.SUDID IS NULL)
    THEN
    SELECT S_LF_SUBNOALLOT_DETAIL.NEXTVAL INTO :new.SUDID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_SUBNOALLOT_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SUBNOALLOT_QUEUE"
BEFORE INSERT ON LF_SUBNOALLOT
FOR EACH ROW
 BEGIN
    IF(:new.SUID IS NULL)
    THEN
    SELECT S_LF_SUBNOALLOT.NEXTVAL INTO :new.SUID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_SURVEYTASK_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SURVEYTASK_QUEUE"
BEFORE INSERT ON LF_SURVEYTASK
FOR EACH ROW
  BEGIN
    IF(:new.IDEN_ID IS NULL)
    THEN
      SELECT S_LF_SURVEYTASK.NEXTVAL INTO :new.IDEN_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SURVEY_QUEUE
-- ====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SURVEY_QUEUE"
BEFORE INSERT ON LF_SURVEY
FOR EACH ROW
  BEGIN
    IF(:new.SUV_ID IS NULL)
    THEN
      SELECT S_LF_SURVEY.NEXTVAL INTO :new.SUV_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SYSMTTASK_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SYSMTTASK_QUEUE"
BEFORE INSERT ON LF_SYSMTTASK
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_SYSMTTASK.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SYS_PARAM_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SYS_PARAM_QUEUE"
BEFORE INSERT ON LF_SYS_PARAM
FOR EACH ROW
  BEGIN
    IF(:new.PARAM_ID IS NULL)
    THEN
      SELECT S_LF_SYS_PARAM.NEXTVAL INTO :new.PARAM_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_TAOCAN_CMD_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_TAOCAN_CMD_QUEUE"
BEFORE INSERT ON LF_TAOCAN_CMD
FOR EACH ROW
BEGIN
    IF (:new.ID IS NULL)  THEN
      SELECT S_LF_TAOCAN_CMD.NEXTVAL INTO :new.ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TIG_LF_TASK_QUEUE
-- ==================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_TASK_QUEUE"
BEFORE INSERT ON LF_TASK
FOR EACH ROW
  BEGIN
    IF(:new.TAID IS NULL)
    THEN
      SELECT S_LF_TASK.NEXTVAL INTO :new.TAID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_TEMPLATE_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_TEMPLATE_QUEUE"
BEFORE INSERT ON LF_TEMPLATE
FOR EACH ROW
  BEGIN
    IF(:new.TM_ID IS NULL)
    THEN
      SELECT S_LF_TEMPLATE.NEXTVAL INTO :new.TM_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_THEME_QUEUE
-- ===================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_THEME_QUEUE"
BEFORE INSERT ON LF_THEME
FOR EACH ROW
  BEGIN
    IF(:new.TID IS NULL)
    THEN
      SELECT S_LF_THEME.NEXTVAL INTO :new.TID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_THIR_MENUCONTROL_QUEUE
-- ==============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_THIR_MENUCONTROL_QUEUE"
BEFORE INSERT ON LF_THIR_MENUCONTROL
FOR EACH ROW
  BEGIN
    IF(:new.TID IS NULL)
    THEN
      SELECT S_LF_THIR_MENUCONTROL.NEXTVAL INTO :new.TID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_TIMER_HISTORY_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER TIG_LF_TIMER_HISTORY_QUEUE
BEFORE INSERT ON LF_TIMER_HISTORY
FOR EACH ROW
  BEGIN
    IF(:new.TA_ID IS NULL)
    THEN
      SELECT S_LF_TIMER_HISTORY.NEXTVAL INTO :new.TA_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_TIMER_QUEUE
-- ===================================
--
CREATE OR REPLACE TRIGGER TIG_LF_TIMER_QUEUE
BEFORE INSERT ON LF_TIMER
FOR EACH ROW
  BEGIN
    IF(:new.TIMER_TASK_ID IS NULL)
    THEN
      SELECT S_LF_TIMER.NEXTVAL INTO :new.TIMER_TASK_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_TMPLRELA_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_TMPLRELA_QUEUE"
BEFORE INSERT ON LF_TMPLRELA
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_TMPLRELA.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_PB_BLACK_DEL
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_PB_BLACK_DEL"
BEFORE INSERT ON PB_BLACK_DEL
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT PB_BLACK_DEL_S.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_TRUCTTYPE_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_TRUCTTYPE_QUEUE"
BEFORE INSERT ON LF_TRUCTTYPE
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_TRUCTTYPE.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_UDGROUP_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_UDGROUP_QUEUE"
BEFORE INSERT ON LF_UDGROUP
FOR EACH ROW
  BEGIN
    IF(:new.UDG_ID IS NULL)
    THEN
      SELECT S_LF_UDGROUP.Nextval INTO :new.UDG_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_USEDSUBNO_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_USEDSUBNO_QUEUE"
BEFORE INSERT ON LF_USEDSUBNO
FOR EACH ROW
  BEGIN
    IF(:new.SUBNO_ID IS NULL)
    THEN
      SELECT S_LF_USEDSUBNO.NEXTVAL INTO :new.SUBNO_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_USER2SKIN_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_USER2SKIN_QUEUE"
BEFORE INSERT ON LF_USER2SKIN
FOR EACH ROW
  BEGIN
    IF(:new.USID IS NULL)
    THEN
      SELECT S_LF_USER2SKIN.NEXTVAL INTO :new.USID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_USERPARA_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_USERPARA_QUEUE"
BEFORE INSERT ON LF_USERPARA
FOR EACH ROW
  BEGIN
    IF(:new.PARA_ID IS NULL)
    THEN
      SELECT S_LF_USERPARA.NEXTVAL INTO :new.PARA_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_USER_RPT_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_USER_RPT_QUEUE"
BEFORE INSERT ON LF_USER_RPT
FOR EACH ROW
  BEGIN
    IF(:new.IDEN_ID IS NULL)
    THEN
    SELECT S_LF_USER_RPT.nextval INTO :new.IDEN_ID FROM DUAL;
    END IF;
    END;
/

--
-- Creating trigger TIG_LF_USER_SETTING_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_USER_SETTING_QUEUE"
BEFORE INSERT ON LF_USER_SETTING
FOR EACH ROW
  BEGIN
    IF(:new.IDEN_ID IS NULL)
    THEN
      SELECT S_LF_USER_SETTING.NEXTVAL INTO :new.IDEN_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_VERSION_EMPDB
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_VERSION_EMPDB"
before insert  on LF_VERSION_EMPDB
FOR EACH ROW
  BEGIN
    IF(:new.DVID IS NULL)
    THEN
      SELECT S_LF_VERSION_EMPDB.NEXTVAL INTO :new.DVID FROM DUAL;
     END IF;
     END;
/

--
-- Creating trigger TIG_LF_VODINFO_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_VODINFO_QUEUE"
BEFORE INSERT ON LF_VODINFO
FOR EACH ROW
  BEGIN
    IF(:new.VOD_ID IS NULL)
    THEN
      SELECT S_LF_VODINFO.NEXTVAL INTO :new.VOD_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_VODMOREC_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_VODMOREC_QUEUE"
BEFORE INSERT ON LF_VODMOREC
FOR EACH ROW
  BEGIN
    IF(:new.REC_ID IS NULL)
    THEN
      SELECT S_LF_VODMOREC.NEXTVAL INTO :new.REC_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_VODMTREC_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_VODMTREC_QUEUE"
BEFORE INSERT ON LF_VODMTREC
FOR EACH ROW
  BEGIN
    IF(:new.REC_ID IS NULL)
    THEN
      SELECT S_LF_VODMTREC.NEXTVAL INTO :new.REC_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WC_REVENT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WC_REVENT_QUEUE"
BEFORE INSERT ON LF_WC_REVENT
FOR EACH ROW
  BEGIN
    IF(:new.EVT_ID IS NULL)
    THEN
      SELECT S_LF_WC_REVENT.NEXTVAL INTO :new.EVT_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WC_RTEXT_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WC_RTEXT_QUEUE"
BEFORE INSERT ON LF_WC_RTEXT
FOR EACH ROW
  BEGIN
    IF(:new.TET_ID IS NULL)
    THEN
      SELECT S_LF_WC_RTEXT.NEXTVAL INTO :new.TET_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WC_VERIFY_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WC_VERIFY_QUEUE"
BEFORE INSERT ON LF_WC_VERIFY
FOR EACH ROW
  BEGIN
    IF(:new.VF_ID IS NULL)
    THEN
      SELECT S_LF_WC_VERIFY.NEXTVAL INTO :new.VF_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WEIX_EMOJI_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WEIX_EMOJI_QUEUE"
BEFORE INSERT ON LF_WEIX_EMOJI
FOR EACH ROW
  BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_WEIX_EMOJI.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WGPARAMCONFIG_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WGPARAMCONFIG_QUEUE"
BEFORE INSERT ON LF_WGPARAMCONFIG
FOR EACH ROW
  BEGIN
    IF(:new.PID IS NULL)
    THEN
      SELECT S_LF_WGPARAMCONFIG.NEXTVAL INTO :new.PID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WGPARAMDEFINITION_QUEUE
-- ===============================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WGPARAMDEFINITION_QUEUE"
BEFORE INSERT ON LF_WGPARAMDEFINITION
FOR EACH ROW
  BEGIN
    IF(:new.PID IS NULL)
    THEN
      SELECT S_LF_WGPARAMDEFINITION.NEXTVAL INTO :new.PID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WX_BASEINFO_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WX_BASEINFO_QUEUE"
BEFORE INSERT ON LF_WX_BASEINFO
FOR EACH ROW
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_WX_BASEINFO.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WX_DATATYPE_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WX_DATATYPE_QUEUE"
BEFORE INSERT ON LF_WX_DATATYPE
FOR EACH ROW
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_WX_DATATYPE.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WX_PAGE_QUEUE
-- =====================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WX_PAGE_QUEUE"
BEFORE INSERT ON LF_WX_PAGE
FOR EACH ROW
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_WX_PAGE.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WX_SENDCOUNT_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WX_SENDCOUNT_QUEUE"
BEFORE INSERT ON LF_WX_SENDCOUNT
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_WX_SENDCOUNT.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WX_UPLOADFILE_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WX_UPLOADFILE_QUEUE"
BEFORE INSERT ON LF_WX_UPLOADFILE
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_WX_UPLOADFILE.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_WX_VISITLOG_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WX_VISITLOG_QUEUE"
BEFORE INSERT ON LF_WX_VISITLOG
FOR EACH ROW
  
BEGIN
    IF(:new.ID IS NULL)
    THEN
      SELECT S_LF_WX_VISITLOG.NEXTVAL INTO :new.ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_LBS_PIOS_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_LBS_PIOS_QUEUE"
BEFORE INSERT ON LF_LBS_PIOS
FOR EACH ROW

BEGIN
   IF (:NEW.P_ID IS NULL) THEN
      SELECT S_LF_LBS_PIOS.NEXTVAL INTO :NEW.P_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_LBS_PUSHSET_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_LBS_PUSHSET_QUEUE"
BEFORE INSERT ON LF_LBS_PUSHSET
FOR EACH ROW
BEGIN
    IF(:NEW.PUSH_ID IS NULL)
    THEN
      SELECT S_LF_LBS_PUSHSET.NEXTVAL INTO :NEW.PUSH_ID FROM DUAL;
      END IF;
END;
/
--
-- Creating trigger TIG_LF_LBS_USERPIOS_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_LBS_USERPIOS_QUEUE"
BEFORE INSERT ON LF_LBS_USERPIOS
FOR EACH ROW

BEGIN
    IF(:NEW.UP_ID IS NULL)
    THEN
      SELECT S_LF_LBS_USERPIOS.NEXTVAL INTO :NEW.UP_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_ONL_MSG_HIS_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ONL_MSG_HIS_QUEUE"
BEFORE INSERT ON LF_ONL_MSG_HIS
FOR EACH ROW
BEGIN
    IF(:NEW.M_ID IS NULL)
    THEN
      SELECT S_LF_ONL_MSG_HIS.NEXTVAL INTO :NEW.M_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_OT_ONL_SERVER_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_ONL_SERVER_QUEUE"
BEFORE INSERT ON LF_ONL_SERVER
FOR EACH ROW
  
BEGIN
    IF(:new.SER_ID IS NULL)
    THEN
      SELECT S_LF_ONL_SERVER.NEXTVAL INTO :new.SER_ID FROM DUAL;
      END IF;
      END;
/

--
-- Creating trigger TIG_LF_SIT_PLANT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SIT_PLANT_QUEUE"
BEFORE INSERT ON LF_SIT_PLANT
FOR EACH ROW
BEGIN
    IF(:NEW.PLANT_ID IS NULL)
    THEN
      SELECT S_LF_SIT_PLANT.NEXTVAL INTO :NEW.PLANT_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_SIT_TYPE_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_SIT_TYPE_QUEUE"
BEFORE INSERT ON LF_SIT_TYPE
FOR EACH ROW
BEGIN
 IF(:NEW.TYPE_ID IS NULL)
 THEN
      SELECT S_LF_SIT_TYPE.NEXTVAL INTO :NEW.TYPE_ID FROM DUAL;
 END IF;
END;
/

--
-- Creating trigger TIG_LF_WEI_COUNT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WEI_COUNT_QUEUE"
BEFORE INSERT ON LF_WEI_COUNT
FOR EACH ROW
BEGIN
    IF(:NEW.COUNT_ID IS NULL)
    THEN
      SELECT S_LF_WEI_COUNT.NEXTVAL INTO :NEW.COUNT_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_WEI_REVENT_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WEI_REVENT_QUEUE"
BEFORE INSERT ON LF_WEI_REVENT
FOR EACH ROW
BEGIN
    IF(:NEW.EVT_ID IS NULL)
    THEN
      SELECT S_LF_WEI_REVENT.NEXTVAL INTO :NEW.EVT_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_WEI_RTEXT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WEI_RTEXT_QUEUE"
BEFORE INSERT ON LF_WEI_RTEXT
FOR EACH ROW
BEGIN
    IF(:NEW.TET_ID IS NULL)
    THEN
      SELECT S_LF_WEI_RTEXT.NEXTVAL INTO :NEW.TET_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_LF_WEI_SENDLOG_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_WEI_SENDLOG_QUEUE"
BEFORE INSERT ON LF_WEI_SENDLOG
FOR EACH ROW
BEGIN
    IF(:NEW.SEND_ID IS NULL)
    THEN
      SELECT S_LF_WEI_SENDLOG.NEXTVAL INTO :NEW.SEND_ID FROM DUAL;
      END IF;
END;
/

--
-- Creating trigger TIG_RECEIPT
-- ============================
--
CREATE OR REPLACE TRIGGER "TIG_RECEIPT" BEFORE
INSERT ON  "RECEIPT"
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT SEQ_RECEIPT.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_SENDQUEUE
-- ==============================
--
CREATE OR REPLACE TRIGGER "TIG_SENDQUEUE" BEFORE
INSERT ON  "SENDQUEUE"
    FOR EACH ROW
BEGIN
IF (:NEW.ID IS NULL)
THEN
SELECT SEQ_SENDQUEUE.NEXTVAL INTO :NEW.ID FROM DUAL;
END IF;
END;
/

--
-- Creating trigger TIG_S_LF_BUS_PACKAGE_QUEUE
-- ===========================================
--
CREATE OR REPLACE TRIGGER "TIG_S_LF_BUS_PACKAGE_QUEUE"
BEFORE INSERT ON LF_BUS_PACKAGE
FOR EACH ROW
BEGIN
    IF (:new.PACKAGE_ID IS NULL)  THEN
      SELECT S_LF_BUS_PACKAGE.NEXTVAL INTO :new.PACKAGE_ID FROM DUAL;
    END IF;
END;
/

--
-- Creating trigger TRIGGER_RECONCILIATION
-- =======================================
--
CREATE OR REPLACE TRIGGER TRIGGER_RECONCILIATION
  BEFORE INSERT ON lf_reconciliation
  FOR EACH ROW
declare
  nextid NUMBER;
BEGIN
   IF :new.ID IS NULL or :new.ID=0 THEN --DepartId是列名
    SELECT S_LF_RECONCILIATION.nextval INTO nextid from sys.dual;
    :new.ID:=nextid;
   END IF;
END TRIGGER_RECONCILIATION;
/



SELECT SEQ_USERDATA.NEXTVAL FROM DUAL;



CREATE OR REPLACE TRIGGER TIG_MMS_TEMPLATE1
  before update on mms_template
  FOR EACH ROW
declare
BEGIN
  IF(:OLD.submitstatus<>:NEW.submitstatus) THEN
              UPDATE LF_TEMPLATE SET submitstatus=:NEW.submitstatus,error_code=:NEW.errcode where EMP_TEMPLID=:NEW.Emp_tmplid;             
  END IF;
    IF (:OLD.AUDITSTATUS<>:NEW.AUDITSTATUS)
    THEN
       UPDATE LF_TEMPLATE SET AUDITSTATUS=:NEW.AUDITSTATUS,submitstatus=:NEW.submitstatus,error_code=:NEW.errcode,SP_TEMPLID=:NEW.TmplId
          where EMP_TEMPLID=:NEW.Emp_tmplid;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER INSERT_LF_MTTASK_TRIG
  after INSERT on BATCH_MT_REQ
  FOR EACH ROW
declare
  MOBLIETYPE NUMBER;
  UU NUMBER;
  UCOUNT NUMBER;
  TIMESTAMP1 TIMESTAMP;
  MSGLEN NUMBER;--内容长度
  QMLEN NUMBER;--签名长度
  TIMERSTATUS NUMBER;
BEGIN
  TIMESTAMP1:=:NEW.RECVTIME;
  IF :NEW.TASKTYPE = 2  THEN
     IF :NEW.SENDTYPE > 1 THEN
         MOBLIETYPE:=2;--动态短信
     elsIF :NEW.SENDTYPE = 1 THEN
       MSGLEN := nvl(length(:NEW.MSG),0);
       SELECT count(SIGNLEN) INTO UCOUNT from xt_gate_queue where spgate in (SELECT spgate from gt_port_used where userid =:NEW.userid);
       IF UCOUNT=0 THEN
          QMLEN:=0;
       else
          SELECT nvl(SIGNLEN,0) INTO QMLEN from xt_gate_queue where spgate in (SELECT spgate from gt_port_used where userid =:NEW.userid) and rownum=1;
          UCOUNT:=0;
       END IF;
       
       IF MSGLEN + QMLEN >70 THEN
         MOBLIETYPE := 1;--长短信
       else
         MOBLIETYPE := 0;--短信
       END IF;
     END IF;

     SELECT count(USER_ID) INTO UCOUNT from LF_SYSUSER where USER_CODE=:NEW.P1;
     IF UCOUNT=0 THEN
         UU:=0;
     else
        SELECT nvl(USER_ID,0) INTO UU from LF_SYSUSER where USER_CODE=:NEW.P1;
     END IF;

      IF :NEW.ATTIME<>'              ' THEN
         TIMESTAMP1:=to_date(:NEW.ATTIME,'YYYY-MM-DD HH24:MI:SS');
         TIMERSTATUS:=1;
      else
         TIMERSTATUS:=0;
      END IF;
       INSERT INTO LF_MTTASK(USER_ID,TASKID,MSG,SP_USER,TITLE,TASKTYPE,BMTTYPE,SENDSTATE,SUB_STATE,RE_STATE,TIMER_TIME,BUS_CODE,SUB_COUNT,EFF_COUNT,MOBILE_URL,SUBMITTIME,MOBILE_TYPE,MS_TYPE,TIMER_STATUS,BATCHID,ISRETRY)
       values(UU,:NEW.TASKID,:NEW.MSG,:NEW.USERID,:NEW.TITLE,:NEW.TASKTYPE,:NEW.SENDTYPE,0,2,1,TIMESTAMP1,:NEW.SVRTYPE,:NEW.TOTALNUM,:NEW.TOTALNUM, :NEW.REMOTEURL,:NEW.RECVTIME,MOBLIETYPE,1,TIMERSTATUS,:NEW.BATCHID,0);
  END IF;
END  INSERT_LF_MTTASK_TRIG;
/


CREATE OR REPLACE TRIGGER UPDATETIME_LF_MTTASK_TRIG
  AFTER INSERT on BATCH_MT_REQ_HIS
  FOR EACH ROW
declare
TIMESTAMP1 TIMESTAMP;
BEGIN
  IF :NEW.ERRORCODE = 'ACCEPTD' THEN
     TIMESTAMP1:=:NEW.RECVTIME;
	   IF :NEW.ATTIME<>'              ' THEN
          TIMESTAMP1:=to_date(:NEW.ATTIME,'YYYY-MM-DD HH24:MI:SS');
     END IF;
    IF :NEW.TASKTYPE=1 THEN
    UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE TASKID=:NEW.TASKID;
    else
     UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE BATCHID=:NEW.BATCHID;
    END IF;
  END IF;
END UPDATETIME_LF_MTTASK_TRIG;
/

CREATE OR REPLACE TRIGGER UPDATETIME_LF_MTTASK_TRIG1
  AFTER UPDATE on BATCH_MT_REQ_HIS
  FOR EACH ROW
declare
TIMESTAMP1 TIMESTAMP;
BEGIN
  IF :NEW.ERRORCODE = 'ACCEPTD' THEN
     TIMESTAMP1:=:NEW.RECVTIME;
	   IF :NEW.ATTIME<>'              ' THEN
         TIMESTAMP1:=to_date(:NEW.ATTIME,'YYYY-MM-DD HH24:MI:SS');
     END IF;
    IF :NEW.TASKTYPE=1 THEN
    UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE TASKID=:NEW.TASKID;
    else
     UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE BATCHID=:NEW.BATCHID;
    END IF;
  END IF;
END UPDATETIME_LF_MTTASK_TRIG1;
/



--建表存储过程
CREATE OR REPLACE PROCEDURE CREATEWXTABLE(PIYM NUMBER)
IS
PISTR VARCHAR2(4000);
PISTR_1 VARCHAR2(256);
PITABLENAME VARCHAR2(20);
BEGIN
    PITABLENAME:='LFWCMSG'||CAST(PIYM AS CHAR);
    PISTR:='CREATE TABLE '||PITABLENAME||
    '(
        MSG_ID NUMBER(38) not null,
        MSG_TYPE NUMBER(2) not null,
        WC_ID  NUMBER(38) not null,
        A_ID NUMBER(38) not null,
        TYPE NUMBER(2) not null,
        MSG_XML VARCHAR2(4000) not null,
        MSG_TEXT VARCHAR2(4000) not null,
        PARENT_ID NUMBER(38),
        CORP_CODE  VARCHAR2(64) not null,
        CREATETIME TIMESTAMP(8) not null
      )tablespace EMP_TABLESPACE
      pctfree 10
      initrans 1
      maxtrans 255
      storage
      (
        initial 64K
        minextents 1
        maxextents unlimited
      )';
    EXECUTE IMMEDIATE PISTR;
    PISTR_1:='alter table '||PITABLENAME||' add constraint PK_'||PITABLENAME||'  primary key(MSG_ID)';
    EXECUTE IMMEDIATE PISTR_1;
  END;
/

--建36x4张历史表
DECLARE 
PINUM  INT;
PICURNUM INT;
PIYM INT;
ERRO_NUM INT;
TACNT INT;
PITABLENAME VARCHAR2(20);
BEGIN
  PINUM:=36;
  ERRO_NUM:=0;
  PICURNUM:=0;
    <<INNER>>    
    WHILE PICURNUM<PINUM LOOP--表个数
        TACNT:=0;
        PIYM:=CAST(to_char(add_months(sysdate,PICURNUM),'yyyymm')AS INT);
        PITABLENAME:='LFWCMSG'||CAST(PIYM AS CHAR);
        SELECT COUNT(1) INTO TACNT FROM USER_TABLES T WHERE T.TABLE_NAME=PITABLENAME;
        IF TACNT=0 THEN--表不存在
          <<LABEL_LOCAL_ERROR>>
          BEGIN
          CREATEWXTABLE(PIYM);
          EXCEPTION WHEN OTHERS THEN
          ERRO_NUM:=ERRO_NUM+1;
          IF ERRO_NUM>=5 THEN
            RETURN;
          ELSE
            GOTO LABEL_LOCAL_ERROR;
          END IF;--END OF ERRO_NUM>=5
          END;--END OF EXCEPTION WHEN OTHERS THEN
        END IF;--END OF IF TACNT=0
        PICURNUM:=PICURNUM+1;
    END LOOP INNER;
END;
/

--存储过程 创建富信档位统计信息的年月表(LF_RMS_SYN201807)
CREATE OR REPLACE PROCEDURE CREATETABLEFX(PIYM NUMBER)
IS
STR VARCHAR2(2000);
TABLENAME VARCHAR2(22);
BEGIN
  TABLENAME:='LF_RMS_SYN'||CAST(PIYM AS CHAR);
  STR:= 'CREATE TABLE '||TABLENAME||'(
		   ID NUMBER(18) NOT NULL,
		   SPISUNCM VARCHAR2(4) DEFAULT('''') NOT NULL,
		   USERID VARCHAR2(32) DEFAULT('''') NOT NULL,
		   IYMD NUMBER(11) DEFAULT 0 NOT NULL,
		   ICOUNT NUMBER(18) DEFAULT 0 NOT NULL ,
		   RSUCC NUMBER(18) DEFAULT 0 NOT NULL ,
		   RFAIL NUMBER(18) DEFAULT 0 NOT NULL,
		   REQ_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL,
		   CORP_CODE VARCHAR2(64) DEFAULT('''') NOT NULL,
		   DEGREE NUMBER(11) DEFAULT 0 NOT NULL,
		   SYN_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL,
       PARAM1 varchar2(64) DEFAULT('''') NOT NULL,
       PARAM2 varchar2(64) DEFAULT('''') NOT NULL,
       PARAM3 varchar2(64) DEFAULT('''') NOT NULL,
       PARAM4 varchar2(64) DEFAULT('''') NOT NULL
	)' ||
	 'tablespace EMP_TABLESPACE
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 64K
      minextents 1
      maxextents unlimited
    )';
	EXECUTE IMMEDIATE STR;
	STR:='ALTER TABLE '||TABLENAME||'
  ADD CONSTRAINT PK_'||TABLENAME||' PRIMARY KEY (ID)
  USING INDEX
  TABLESPACE EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  )';
  EXECUTE IMMEDIATE STR;
END;
/
--创建一年的富信档位统计信息年月表(从当前年月开始)
DECLARE
J NUMBER(11);
PIYMFX VARCHAR2(6);
BEGIN
  J:=0;
  WHILE(J < 12) LOOP
      PIYMFX := TO_CHAR(ADD_MONTHS(SYSDATE,J),'yyyymm');
				BEGIN
					CREATETABLEFX(PIYMFX);
					EXCEPTION WHEN OTHERS THEN
            RETURN;
				END;
      J:=J+1;
    END LOOP;
END;
/
--存储过程 从LF_RMS_SYN_201807表(年月表)中按年月转移数据至汇总表(LF_RMS_REPORT)
CREATE OR REPLACE PROCEDURE PRO_RMS_SUM(IYMD NUMBER) AUTHID CURRENT_USER
IS
SQLSTR VARCHAR2(1000);
TABLE_NAME VARCHAR2(25);
IYMDSTR VARCHAR2(8);
BEGIN
  IYMDSTR := TO_CHAR(IYMD);
  IF IYMD > 0 THEN
    BEGIN
      TABLE_NAME:='LF_RMS_SYN'||SUBSTR(IYMDSTR, 0, 6);
      --1.删除老数据
      SQLSTR:='DELETE FROM LF_RMS_REPORT WHERE IYMD='||IYMD;
      EXECUTE IMMEDIATE SQLSTR;
      --2 更新新数据
      SQLSTR:='INSERT INTO LF_RMS_REPORT (ID,SPISUNCM,USERID,IYMD,ICOUNT,RSUCC,RFAIL,SYN_TIME,CORP_CODE,DEGREE,CREATE_TIME,PARAM1,PARAM2,PARAM3,PARAM4) SELECT  ID,SPISUNCM,USERID,IYMD,ICOUNT,RSUCC,RFAIL,SYN_TIME,CORP_CODE,DEGREE,SYSDATE,PARAM1,PARAM2,PARAM3,PARAM4 FROM '||TABLE_NAME||' WHERE IYMD='||IYMD;
      EXECUTE IMMEDIATE SQLSTR;
      --事务提交
      COMMIT;
      EXCEPTION
        WHEN OTHERS THEN
          ROLLBACK;
    END;
  END IF;
END;
/
--从LF_RMS_SYN_TEMP表中按年月日转移数据至年月表(LF_RMS_SYN_201807)
CREATE OR REPLACE PROCEDURE PRO_RMS_TRANSFER AUTHID CURRENT_USER
IS
SQLSTR VARCHAR2(2000);
IYMD NUMBER(11);
ERROR NUMBER(11);
EXCEPTION_NUMBER NUMBER(11);
TABLE_NAME VARCHAR2(25);
--定义游标
CURSOR CURIDX IS SELECT IYMD FROM LF_RMS_SYN_TEMP WHERE IYMD <> 0 GROUP BY IYMD;
BEGIN
	--1.删除重复数据
	DELETE FROM LF_RMS_SYN_TEMP WHERE ID NOT IN(SELECT MAX(T1.ID) FROM LF_RMS_SYN_TEMP T1 GROUP BY T1.SPISUNCM,T1.USERID,T1.IYMD,T1.DEGREE);
	--1.1 删除同步标识数据即IYMD=0
	DELETE FROM LF_RMS_SYN_TEMP WHERE IYMD = 0;
	--2.数据转移
	--打开游标
	IF NOT CURIDX%ISOPEN THEN
		BEGIN
      OPEN CURIDX;
		END;
	END IF;
	--循环开始
	LOOP
		FETCH CURIDX INTO IYMD;
    --退出循环的条件
    EXIT WHEN CURIDX%NOTFOUND OR CURIDX%NOTFOUND IS NULL;
    --处理业务
    IF IYMD > 0 THEN
			BEGIN
				TABLE_NAME := 'LF_RMS_SYN'||SUBSTR(TO_CHAR(IYMD),0,6);
				--2.1删除老数据
				SQLSTR:='DELETE FROM '||TABLE_NAME||' WHERE IYMD='||IYMD;
				EXECUTE IMMEDIATE SQLSTR;
				--2.2 转移临时表数据
				SQLSTR:='INSERT INTO '||TABLE_NAME||'(ID,SPISUNCM,USERID,IYMD,ICOUNT,RSUCC,RFAIL,REQ_TIME,CORP_CODE,DEGREE,SYN_TIME,PARAM1,PARAM2,PARAM3,PARAM4) SELECT ID,SPISUNCM,USERID,IYMD,ICOUNT,RSUCC,RFAIL,REQ_TIME,CORP_CODE,DEGREE,SYSDATE,PARAM1,PARAM2,PARAM3,PARAM4 FROM LF_RMS_SYN_TEMP WHERE IYMD='||IYMD;
				EXECUTE IMMEDIATE SQLSTR;
				--2.3 删除已转移临时表数据
				SQLSTR:='DELETE FROM LF_RMS_SYN_TEMP WHERE IYMD='||IYMD;
				EXECUTE IMMEDIATE SQLSTR;
				--2.4 执行汇总
				BEGIN
				  PRO_RMS_SUM(IYMD);
				END;
				--2.5 提交事务
				COMMIT;
				EXCEPTION
        WHEN OTHERS THEN
        --2.6 异常回滚
          ROLLBACK;
				----2.7 关闭游标
				CLOSE CURIDX;
			END;
		END IF;
	END LOOP;
END;
/

-- V7.3.SP8整包的006脚本开始-------
CREATE OR REPLACE PROCEDURE GW_LOADLFTEMPLATE
 (
  OUT_CURSOR OUT SYS_REFCURSOR
 )
AS
BEGIN
  OPEN OUT_CURSOR FOR
  SELECT TM_ID,USER_ID,TM_NAME,TM_MSG,DSFLAG,TM_STATE,ADDTIME,ISPASS,TMP_TYPE,BIZ_CODE,CORP_CODE,PARAMCNT,TM_CODE,SP_TEMPLID,DEGREE,RMSRESSIZE,H5RESSIZE,ISPUBLIC,PARAMSNUM,VER  FROM LF_TEMPLATE WHERE TM_STATE=1 AND (TMP_TYPE=3 OR TMP_TYPE=11);
END;
/

CREATE OR REPLACE PROCEDURE GW_LOAD_ECERRCODEBIND
 (
  OUT_CURSOR OUT SYS_REFCURSOR
 )
AS
BEGIN
  OPEN OUT_CURSOR FOR
  SELECT CORP_CODE,STATE_CODE,MAPPING_CODE,STATE_DES  FROM LF_STATECODE;
END;
/
-- V7.3.SP8整包的006脚本结束---------

--增加版本信息
DECLARE COUNTNUM INT;
        DBVERSIONSTR VARCHAR2(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
    DBVERSIONSTR:='73.09';
    NUMNO:=3;
    TOTALINT:=5;
    SELECT  COUNT(*) INTO COUNTNUM FROM LF_DB_SCRIPT WHERE VERSION=DBVERSIONSTR AND CURRENT_NO=NUMNO AND TOTAL=TOTALINT;
    IF COUNTNUM=0 THEN
      --EMP产品数据库版本信息表
      INSERT INTO LF_DB_SCRIPT(VERSION,UPDATETIME,CREATETIME,CURRENT_NO,TOTAL,STATE,MEMO)
      VALUES(DBVERSIONSTR,SYSDATE,SYSDATE,NUMNO,TOTALINT,2,'3号脚本');
    ELSE
      UPDATE LF_DB_SCRIPT SET STATE=2,UPDATETIME=SYSDATE WHERE  VERSION=DBVERSIONSTR AND CURRENT_NO=NUMNO AND TOTAL=TOTALINT;
    END IF;
    COMMIT;
END;
/
