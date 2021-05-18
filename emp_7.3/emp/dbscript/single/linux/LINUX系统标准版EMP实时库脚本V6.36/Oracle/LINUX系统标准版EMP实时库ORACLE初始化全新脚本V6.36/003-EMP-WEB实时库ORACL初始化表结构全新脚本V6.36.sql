DECLARE COUNTNUM INT;
        DBVERSIONSTR VARCHAR2(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
    DBVERSIONSTR:='6.36';
    NUMNO:=3;
    TOTALINT:=4;
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

CREATE TABLE SENDQUEUE(
	"ID" NUMBER(11) NOT NULL,
	"PORT_NO" NUMBER(11) DEFAULT -1 NOT NULL,
	"MOBILE" VARCHAR2(20) NOT NULL,
	"MSG_TYPE" NUMBER(11) DEFAULT 0 NOT NULL,
	"SHORT_MESSAGE" VARCHAR2(300) NOT NULL,
	"URL" VARCHAR2(200),
	"SEND_DATE" TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
	"CLIENTMSGID" VARCHAR2(32) DEFAULT '' NOT NULL
) ;

CREATE TABLE RECEIPT(
	"ID" NUMBER(11) NOT NULL,
	"SRCADDR" VARCHAR2(20),
	"DESADDR" VARCHAR2(20),
	"MOTIME" TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
	"CLIENTMSGID" VARCHAR2(32),
	"SPMSGID" VARCHAR2(32),
	"STATUS" VARCHAR2(7) 
) ;

CREATE TABLE HISTQUEUE(
	"ID" NUMBER(11) NOT NULL,
	"PORT_NO" VARCHAR2(20),
	"MOBILE" VARCHAR2(20),
	"SHORT_MESSAGE" VARCHAR2(200),
	"SEND_TIME" TIMESTAMP(6),
	"REPEAT_SEND" NUMBER(11),
	"STATUS" VARCHAR2(10),
	"CLIENTMSGID" VARCHAR2(32) DEFAULT '' NOT NULL,
	"SPMSGID" VARCHAR2(32) DEFAULT '' NOT NULL
) ;

CREATE SEQUENCE SEQ_SENDQUEUE
MINVALUE 1
MAXVALUE 99999999999
START WITH 1
INCREMENT BY 1
CACHE 200
ORDER;


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

CREATE SEQUENCE SEQ_RECEIPT
MINVALUE 1
MAXVALUE 99999999999
START WITH 1
INCREMENT BY 1
CACHE 200
ORDER;

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


CREATE SEQUENCE SEQ_HISTQUEUE
MINVALUE 1
MAXVALUE 99999999999
START WITH 1
INCREMENT BY 1
CACHE 200
ORDER;


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
  );

--
-- Creating table LF_ADDRBOOKS
-- ===========================
--
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
  );
alter table LF_ADDRBOOKS
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
  );

--
-- Creating table LF_ALSMSRECORD
-- =============================
--
create table LF_ALSMSRECORD
(
  ID         NUMBER(38) not null,
  MSGID      NUMBER(22) default 0,
  USERID     VARCHAR2(11) default '' not null,
  SPGATE     VARCHAR2(21) default '0' not null,
  PHONE      VARCHAR2(21) default '0' not null,
  SENDSTATUS NUMBER(11) default 0 not null,
  SENDFLAG   NUMBER(11) default 0 not null,
  ERRORCODE  CHAR(7) default '' not null,
  UNICOM     NUMBER(11) default 0 not null,
  SENDTIME   TIMESTAMP(6) default sysdate not null,
  RECVTIME   TIMESTAMP(6),
  MESSAGE    VARCHAR2(3000) default '' not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_ALSMSRECORD
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
  );
create index IX_ALSMS_MSGID on LF_ALSMSRECORD (MSGID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_APPMAINPAGEHIS
-- ================================
--
create table LF_APPMAINPAGEHIS
(
  ID        NUMBER(38) not null,
  TASKID    NUMBER(38),
  FROMUSER  VARCHAR2(25) not null,
  TOUSER    VARCHAR2(25) not null,
  TOTYPE    NUMBER(20) not null,
  SENDTIME  TIMESTAMP(8) default sysdate,
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
  );
alter table LF_APPMAINPAGEHIS
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
  );

--
-- Creating table LF_APP_ACCOUNT
-- =============================
--
create table LF_APP_ACCOUNT
(
  A_ID         NUMBER(38) not null,
  FAKE_ID      VARCHAR2(32),
  NAME         VARCHAR2(64),
  CODE         VARCHAR2(256) not null,
  OPEN_ID      VARCHAR2(64),
  IMG          VARCHAR2(128),
  TYPE         NUMBER(3),
  IS_APPROVE   NUMBER(3) default 0,
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
  SYNC_STATE   NUMBER(3) default 0,
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
  );
alter table LF_APP_ACCOUNT
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
  );

--
-- Creating table LF_APP_CLIDOWLOAD
-- ================================
--
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
  );
alter table LF_APP_CLIDOWLOAD
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
  );

--
-- Creating table LF_APP_FOMEITEM
-- ==============================
--
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
  );
alter table LF_APP_FOMEITEM
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
  );

--
-- Creating table LF_APP_FOMEVALVE
-- ===============================
--
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
  );
alter table LF_APP_FOMEVALVE
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
  );

--
-- Creating table LF_APP_FOM_INFO
-- ==============================
--
create table LF_APP_FOM_INFO
(
  F_ID          NUMBER(38) not null,
  TYPE_ID       NUMBER(38),
  TITLE         VARCHAR2(512),
  NOTE          VARCHAR2(512),
  PUBLISH_STATE NUMBER(3),
  URL           VARCHAR2(512),
  SUBMIT_COUNT  NUMBER(20) default 0,
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
  );
alter table LF_APP_FOM_INFO
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
  );

--
-- Creating table LF_APP_FOM_QUESN
-- ===============================
--
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
  );
alter table LF_APP_FOM_QUESN
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
  );

--
-- Creating table LF_APP_FOM_TYPE
-- ==============================
--
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
  );
alter table LF_APP_FOM_TYPE
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
  );

--
-- Creating table LF_APP_KEYWORD
-- =============================
--
create table LF_APP_KEYWORD
(
  K_ID       NUMBER(38) not null,
  NAME       VARCHAR2(32) not null,
  TYPE       NUMBER(3) default 0,
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
  );
alter table LF_APP_KEYWORD
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
  );

--
-- Creating table LF_APP_LBS_PIOS
-- ==============================
--
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
  );
alter table LF_APP_LBS_PIOS
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
  );

--
-- Creating table LF_APP_LBS_PUSH
-- ==============================
--
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
  );
alter table LF_APP_LBS_PUSH
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
  );

--
-- Creating table LF_APP_LBS_USER
-- ==============================
--
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
  );
alter table LF_APP_LBS_USER
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
  );

--
-- Creating table LF_APP_MENU
-- ==========================
--
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
  );

--
-- Creating table LF_APP_MOCACHE
-- =============================
--
create table LF_APP_MOCACHE
(
  ID           NUMBER(38) not null,
  MSGID        NUMBER(38),
  READ_STATE   NUMBER(20) not null,
  SEND_STATE   NUMBER(20) default 1 not null,
  SENDED_COUNT NUMBER(20) default 0 not null,
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
  CREATETIME   TIMESTAMP(8) default sysdate not null,
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
  );
alter table LF_APP_MOCACHE
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
  );

--
-- Creating table LF_APP_MSGCACHE
-- ==============================
--
create table LF_APP_MSGCACHE
(
  ID           NUMBER(38) not null,
  MSGID        NUMBER(38),
  READ_STATE   NUMBER(20) not null,
  SEND_STATE   NUMBER(20) default 1 not null,
  SENDED_COUNT NUMBER(20) default 0 not null,
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
  CREATETIME   TIMESTAMP(8) default sysdate not null,
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
  );
alter table LF_APP_MSGCACHE
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
  );

--
-- Creating table LF_APP_MSGCONTENT
-- ================================
--
create table LF_APP_MSGCONTENT
(
  ID       NUMBER(38) not null,
  CACHE_ID NUMBER(38),
  MSG_TYPE NUMBER(20),
  CONTENT  VARCHAR2(4000) default '',
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
  );
alter table LF_APP_MSGCONTENT
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
  );

--
-- Creating table LF_APP_MTMSG
-- ===========================
--
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
  );
alter table LF_APP_MTMSG
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
  );

--
-- Creating table LF_APP_MTTASK
-- ============================
--
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
  BIGINTIME    TIMESTAMP(8) default sysdate,
  ENDTIME      TIMESTAMP(8) default sysdate,
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
  );
alter table LF_APP_MTTASK
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
  );

--
-- Creating table LF_APP_MW_CLIENT
-- ===============================
--
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
  );
alter table LF_APP_MW_CLIENT
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
  );
alter table LF_APP_MW_CLIENT
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
  );

--
-- Creating table LF_APP_MW_GPMEM
-- ==============================
--
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
  );

--
-- Creating table LF_APP_MW_GROUP
-- ==============================
--
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
  );
alter table LF_APP_MW_GROUP
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
  );

--
-- Creating table LF_APP_OL_GPMEM
-- ==============================
--
create table LF_APP_OL_GPMEM
(
  G_ID     NUMBER(38) not null,
  GM_USER  NUMBER(38),
  GM_STATE NUMBER(38) default (1),
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
  );

--
-- Creating table LF_APP_OL_GPMSGID
-- ================================
--
create table LF_APP_OL_GPMSGID
(
  GM_USER     NUMBER(38),
  MSG_ID      NUMBER(38) default (0),
  UPDATE_TIME TIMESTAMP(8) default (sysdate) -- 更新时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_APP_OL_GROUP
-- ==============================
--
create table LF_APP_OL_GROUP
(
  GP_ID       NUMBER(38) not null,
  GP_NAME     VARCHAR2(32),
  CREATE_USER NUMBER(38),
  CREATE_TIME TIMESTAMP(8),
  MOUNTCOUNT  NUMBER(20) default 0,
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
  );
alter table LF_APP_OL_GROUP
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
  );

--
-- Creating table LF_APP_OL_MSG
-- ============================
--
create table LF_APP_OL_MSG
(
  M_ID       NUMBER(38) not null,
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SEND_TIME  TIMESTAMP(8),
  SERVER_NUM VARCHAR2(32),
  MSG_TYPE   VARCHAR2(10),
  MESSAGE    VARCHAR2(512),
  PUSH_TYPE  NUMBER(3) default 0,
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
  );
alter table LF_APP_OL_MSG
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
  );

--
-- Creating table LF_APP_OL_MSGHIS
-- ===============================
--
create table LF_APP_OL_MSGHIS
(
  M_ID       NUMBER(38) not null,
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SEND_TIME  TIMESTAMP(8),
  SERVER_NUM VARCHAR2(32),
  MSG_TYPE   VARCHAR2(10),
  MESSAGE    VARCHAR2(512),
  PUSH_TYPE  NUMBER(3) default 0,
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
  );
alter table LF_APP_OL_MSGHIS
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
  );

--
-- Creating table LF_APP_REVENT
-- ============================
--
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
  );
alter table LF_APP_REVENT
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
  );

--
-- Creating table LF_APP_RIMG
-- ==========================
--
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
  );
alter table LF_APP_RIMG
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
  );

--
-- Creating table LF_APP_RPTCACHE
-- ==============================
--
create table LF_APP_RPTCACHE
(
  ID           NUMBER(38) not null,
  MSGID        NUMBER(38),
  READ_STATE   NUMBER(20),
  SEND_STATE   NUMBER(20) default 1 not null,
  SENDED_COUNT NUMBER(20) default 0 not null,
  SERIAL       VARCHAR2(100) not null,
  ECODE        VARCHAR2(64),
  APPID        NUMBER(38),
  VALIDITY     NUMBER(38),
  ICODE        NUMBER(20),
  BODY         VARCHAR2(4000),
  MSG_TYPE     NUMBER(20),
  CREATETIME   TIMESTAMP(8) default sysdate not null,
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
  );
alter table LF_APP_RPTCACHE
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
  );

--
-- Creating table LF_APP_RTEXT
-- ===========================
--
create table LF_APP_RTEXT
(
  TET_ID     NUMBER(38) not null,
  TET_TYPE   NUMBER(3) default 0,
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
  );
alter table LF_APP_RTEXT
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
  );

--
-- Creating table LF_APP_SIT_INFO
-- ==============================
--
create table LF_APP_SIT_INFO
(
  S_ID        NUMBER(38) not null,
  TYPE_ID     NUMBER(38),
  NAME        VARCHAR2(100),
  URL         VARCHAR2(128),
  IS_SYSTEM   NUMBER(3) default 0,
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
  SENDTIME    TIMESTAMP(8) default sysdate,
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
  );
alter table LF_APP_SIT_INFO
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
  );

--
-- Creating table LF_APP_SIT_PAGE
-- ==============================
--
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
  );
alter table LF_APP_SIT_PAGE
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
  );

--
-- Creating table LF_APP_SIT_PLANT
-- ===============================
--
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
  );
alter table LF_APP_SIT_PLANT
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
  );

--
-- Creating table LF_APP_SIT_TYPE
-- ==============================
--
create table LF_APP_SIT_TYPE
(
  TYPE_ID    NUMBER(38) not null,
  NAME       VARCHAR2(32),
  IS_DEFAULT NUMBER(3),
  PATTERN    NUMBER(20) default 0,
  IMG_URL    VARCHAR2(64),
  IMG_URL0   VARCHAR2(64),
  IMG_URL1   VARCHAR2(64),
  IMG_URL2   VARCHAR2(64),
  IMG_URL3   VARCHAR2(64),
  IS_SYSTEM  NUMBER(3) default 0,
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
  );
alter table LF_APP_SIT_TYPE
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
  );

--
-- Creating table LF_APP_TC_MOMSG
-- ==============================
--
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
  );
alter table LF_APP_TC_MOMSG
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
  );

--
-- Creating table LF_APP_TC_MTMSG
-- ==============================
--
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
  );
alter table LF_APP_TC_MTMSG
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
  );

--
-- Creating table LF_APP_TEMPLATE
-- ==============================
--
create table LF_APP_TEMPLATE
(
  T_ID        NUMBER(38) not null,
  T_NAME      VARCHAR2(64) not null,
  MSG_TYPE    NUMBER(3),
  MSG_XML     VARCHAR2(4000),
  MSG_TEXT    VARCHAR2(4000),
  IS_PUBLIC   NUMBER(3) default 0,
  IS_DRAFT    NUMBER(3) default 0,
  IS_DYNAMIC  NUMBER(3) default 0,
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
  );
alter table LF_APP_TEMPLATE
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
  );

--
-- Creating table LF_APP_TLINK
-- ===========================
--
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
  );
alter table LF_APP_TLINK
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
  );

--
-- Creating table LF_APP_ULINK
-- ===========================
--
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
  );
alter table LF_APP_ULINK
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
  );

--
-- Creating table LF_APP_VERIFY
-- ============================
--
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
  );
alter table LF_APP_VERIFY
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
  );

--
-- Creating table LF_APP_WC_GROUP
-- ==============================
--
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
  );
alter table LF_APP_WC_GROUP
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
  );

--
-- Creating table LF_AUTOREPLY
-- ===========================
--
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
  );
alter table LF_AUTOREPLY
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
  );

--
-- Creating table LF_BILLLOG
-- =========================
--
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
  );
alter table LF_BILLLOG
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
  );

--
-- Creating table LF_BINDFLOW
-- ==========================
--
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
  );

--
-- Creating table LF_BIRTHDAYMEMBER
-- ================================
--
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
  );

--
-- Creating table LF_BIRTHDAYSETUP
-- ===============================
--
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
  );

--
-- Creating table LF_BLACKLIST
-- ===========================
--
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
  );

--
-- Creating table LF_BUSINESS
-- ==========================
--
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
  );
alter table LF_BUSINESS
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
  );

--
-- Creating table LF_BUSMANAGER
-- ============================
--
create table LF_BUSMANAGER
(
  BUS_ID          NUMBER(38) not null,
  BUS_CODE        VARCHAR2(64) not null,
  BUS_NAME        VARCHAR2(32) not null,
  BUS_DESCRIPTION VARCHAR2(2000),
  CLASS_NAME      VARCHAR2(2000),
  CORP_CODE       VARCHAR2(64),
  BUS_TYPE        NUMBER(1) default 0,
  RISELEVEL       NUMBER(2) default -99,
  CREATE_TIME     TIMESTAMP(8) default SYSDATE,
  UPDATE_TIME     TIMESTAMP(8) default SYSDATE,
  DEP_ID          NUMBER(38),
  USER_ID         NUMBER(38),
  STATE           NUMBER(2) default 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_BUSMANAGER
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
  );

--
-- Creating table LF_BUSPKGETAOCAN
-- ===============================
--
create table LF_BUSPKGETAOCAN
(
  ID             NUMBER(38) not null,
  TAOCAN_CODE    VARCHAR2(64),
  PACKAGE_CODE   VARCHAR2(64) not null,
  BUS_CODE       VARCHAR2(64),
  ASSOCIATE_TYPE NUMBER(1) not null,
  CORP_CODE      VARCHAR2(64),
  CREATE_TIME    TIMESTAMP(8) default SYSDATE
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_BUSPKGETAOCAN
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
  );

--
-- Creating table LF_BUSTAIL
-- =========================
--
create table LF_BUSTAIL
(
  BUSTAIL_ID   NUMBER(38) not null,
  BUSTAIL_NAME VARCHAR2(64) not null,
  CORP_CODE    VARCHAR2(64),
  CONTENT      VARCHAR2(2000) not null,
  CREATE_TIME  TIMESTAMP(8) default SYSDATE,
  UPDATE_TIME  TIMESTAMP(8) default SYSDATE,
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
  );
alter table LF_BUSTAIL
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
  );

--
-- Creating table LF_BUS_PACKAGE
-- =============================
--
create table LF_BUS_PACKAGE
(
  PACKAGE_ID    NUMBER(38) not null,
  PACKAGE_CODE  VARCHAR2(64) not null,
  PACKAGE_NAME  VARCHAR2(64) not null,
  PACKAGE_DES   VARCHAR2(2000) default '',
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
  );
alter table LF_BUS_PACKAGE
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
  );
alter table LF_BUS_PACKAGE
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
  );

--
-- Creating table LF_BUS_PROCESS
-- =============================
--
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
  );
alter table LF_BUS_PROCESS
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
  );

--
-- Creating table LF_BUS_TAILTMP
-- =============================
--
create table LF_BUS_TAILTMP
(
  ID             NUMBER(38) not null,
  BUS_ID         NUMBER(38) not null,
  SMSTAIL_ID     NUMBER(38),
  TM_ID          NUMBER(38),
  ASSOCIATE_TYPE NUMBER(1) not null,
  CORP_CODE      VARCHAR2(64),
  CREATE_TIME    TIMESTAMP(8) default SYSDATE,
  UPDATE_TIME    TIMESTAMP(8) default SYSDATE,
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
  );
alter table LF_BUS_TAILTMP
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
  );

--
-- Creating table LF_BUS_TAOCAN
-- ============================
--
create table LF_BUS_TAOCAN
(
  TAOCAN_ID    NUMBER(38) not null,
  TAOCAN_CODE  VARCHAR2(64) not null,
  TAOCAN_NAME  VARCHAR2(64) not null,
  TAOCAN_DES   VARCHAR2(2000) default '',
  STATE        NUMBER(1) not null,
  CORP_CODE    VARCHAR2(64),
  CREATE_TIME  TIMESTAMP(8),
  UPDATE_TIME  TIMESTAMP(8),
  DEP_ID       NUMBER(38),
  USER_ID      NUMBER(38),
  START_DATE   TIMESTAMP(8) default SYSDATE,
  END_DATE     TIMESTAMP(8) default SYSDATE,
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
  );
alter table LF_BUS_TAOCAN
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
  );
alter table LF_BUS_TAOCAN
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
  );

--
-- Creating table LF_CLIDEP_CONN
-- =============================
--
create table LF_CLIDEP_CONN
(
  USER_ID        NUMBER(38) not null,
  DEP_CODE_THIRD VARCHAR2(64) default '0' not null,
  CONN_ID        NUMBER(38),
  DEP_ID         NUMBER(38) default 0 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_CLIDEP_CONN
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
  );

--
-- Creating table LF_CLIENT
-- ========================
--
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
  ISCONTRACT  NUMBER(2) default 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_CLIENT
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
  );
alter table LF_CLIENT
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
  );
create index FK_LF_CLI_RELATIONS_LF_CLI_FK on LF_CLIENT (CC_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index FK_LF_CLI_RELATIONS_LF_DEP_FK on LF_CLIENT (DEP_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_CI_CORP_CODE on LF_CLIENT (CORP_CODE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_CI_NAME on LF_CLIENT (NAME)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_CI_PHONE on LF_CLIENT (PHONE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_CLIENTCLASS
-- =============================
--
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
  );
alter table LF_CLIENTCLASS
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
  );

--
-- Creating table LF_CLIENT_DEP
-- ============================
--
create table LF_CLIENT_DEP
(
  DEP_ID         NUMBER(38) not null,
  DEP_CODE       VARCHAR2(64),
  DEP_NAME       VARCHAR2(64),
  DEP_PCODE      VARCHAR2(64),
  DEP_LEVEL      NUMBER(2) default 0,
  ADD_TYPE       NUMBER(2),
  CORP_CODE      VARCHAR2(64),
  PARENT_ID      NUMBER(38) default 0 not null,
  DEP_CODE_THIRD VARCHAR2(64) default '',
  DEP_PATH       VARCHAR2(160) default ''
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_CLIENT_DEP
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
  );

--
-- Creating table LF_CLIENT_DEP_SP
-- ===============================
--
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
  );
create index INDEX_LF_CI_SP_CLI_ID on LF_CLIENT_DEP_SP (CLIENT_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_CI_SP_DEP_ID on LF_CLIENT_DEP_SP (DEP_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_CONTRACT
-- ==========================
--
create table LF_CONTRACT
(
  CONTRACT_ID       NUMBER(38) not null,
  MOBILE            VARCHAR2(128) not null,
  CUSTOM_NAME       VARCHAR2(120) not null,
  IDENT_TYPE        VARCHAR2(2),
  IDENT_NO          VARCHAR2(20),
  CONTRACT_DEP      NUMBER(38) not null,
  CONTRACT_USER     NUMBER(38) not null,
  CONTRACT_DATE     TIMESTAMP(8) default sysdate not null,
  ADD_ORDER_DATE    TIMESTAMP(8) default sysdate,
  CANCEL_ORDER_DATE TIMESTAMP(8) default sysdate,
  CONTRACT_STATE    NUMBER(2) default 0 not null,
  CONTRACT_TYPE     NUMBER(2) default 0,
  CUSTOM_TYPE       NUMBER(1) default 0,
  ADDRESS           VARCHAR2(256),
  CONTRACT_SOURCE   NUMBER(2),
  CANCEL_CONTYPE    NUMBER(2),
  CANCEL_CONTIME    TIMESTAMP(8),
  CORP_CODE         VARCHAR2(64),
  DEP_ID            NUMBER(38),
  USER_ID           NUMBER(38),
  UPDATE_TIME       TIMESTAMP(8) default sysdate not null,
  ACCT_COMNAME      VARCHAR2(64),
  ACCT_NO           VARCHAR2(25),
  ACCT_NAME         VARCHAR2(120),
  ACCT_IDENTTYPE    VARCHAR2(2),
  ACCT_IDENTNO      VARCHAR2(20),
  ACCT_ADDRESS      VARCHAR2(256),
  IS_VALID          VARCHAR2(1) default 0 not null,
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
  );
alter table LF_CONTRACT
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
  );

--
-- Creating table LF_CONTRACT_TAOCAN
-- =================================
--
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
  CREATE_TIME    TIMESTAMP(8) default SYSDATE,
  UPDATE_TIME    TIMESTAMP(8) default SYSDATE,
  CORP_CODE      VARCHAR2(64),
  BUCKLEFEE_TIME TIMESTAMP(8),
  GUID           NUMBER(38),
  IS_VALID       VARCHAR2(1) default '0' not null,
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
  );
alter table LF_CONTRACT_TAOCAN
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
  );

--
-- Creating table LF_CORP
-- ======================
--
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
  ISBALANCE   NUMBER(1) default 0,
  SUBNO_DIGIT NUMBER(2) default 4,
  CUR_SUBNO   VARCHAR2(20) default '0'
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_CORP_CONF
-- ===========================
--
create table LF_CORP_CONF
(
  CC_ID       NUMBER(38) not null,
  CORP_CODE   VARCHAR2(64),
  PARAM_KEY   VARCHAR2(64),
  PARAM_VALUE VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_CUSTFIELD
-- ===========================
--
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
  );
alter table LF_CUSTFIELD
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
  );

--
-- Creating table LF_CUSTFIELD_VALUE
-- =================================
--
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
  );
alter table LF_CUSTFIELD_VALUE
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
  );

--
-- Creating table LF_DBCONNECT
-- ===========================
--
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
  );
alter table LF_DBCONNECT
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
  );

--
-- Creating table LF_DEDUCTIONS_DISP
-- =================================
--
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
  OPR_TIME         TIMESTAMP(8) default SYSDATE,
  CONTRACT_DEP     NUMBER(38),
  DEP_NAME         VARCHAR2(64),
  CONTRACT_USER    NUMBER(38),
  USER_NAME        VARCHAR2(64),
  DEP_ID           NUMBER(38),
  USER_ID          NUMBER(38),
  CORP_CODE        VARCHAR2(64),
  MSG_ID           VARCHAR2(256) not null,
  UPDATE_TIME      TIMESTAMP(8) default SYSDATE
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_DEDUCTIONS_DISP
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
  );
alter table LF_DEDUCTIONS_DISP
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
  );

--
-- Creating table LF_DEDUCTIONS_LIST
-- =================================
--
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
  CONTRACT_STATE   NUMBER(2) default 0,
  CONTRACT_TYPE    NUMBER(2) default 0,
  DEDUCTIONS_TYPE  NUMBER(2),
  BUCKUP_TIMER     NUMBER(3),
  UDPATE_TIME      TIMESTAMP(8) default SYSDATE,
  BUCKLEFEE_TIME   TIMESTAMP(8) default SYSDATE,
  BUCKLEUPFEE_TIME TIMESTAMP(8) default SYSDATE,
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
  );
alter table LF_DEDUCTIONS_LIST
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
  );
alter table LF_DEDUCTIONS_LIST
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
  );
create index I_LF_DEDUCTIONS_LIST_D on LF_DEDUCTIONS_LIST (CONTRACT_DEP)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index I_LF_DEDUCTIONS_LIST_M on LF_DEDUCTIONS_LIST (IMONTH)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index I_LF_DEDUCTIONS_LIST_U on LF_DEDUCTIONS_LIST (CONTRACT_USER)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index I_LF_DEDUCTIONS_LIST_Y on LF_DEDUCTIONS_LIST (IYEAR)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_DEP
-- =====================
--
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
  DEP_STATE      NUMBER(1) default 1 not null,
  DEP_PATH       VARCHAR2(160) default '',
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
  );
alter table LF_DEP
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
  );

--
-- Creating table LF_DEPPWDRECEIVER
-- ================================
--
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
  );
alter table LF_DEPPWDRECEIVER
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
  );

--
-- Creating table LF_DEP_RECHARGE_LOG
-- ==================================
--
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
  );
alter table LF_DEP_RECHARGE_LOG
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
  );

--
-- Creating table LF_DEP_TAOCAN
-- ============================
--
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
  );
alter table LF_DEP_TAOCAN
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
  );

--
-- Creating table LF_DEP_USER_BALANCE
-- ==================================
--
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
  HAS_DAYALARM    NUMBER(20)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_DEP_USER_BALANCE
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
  );
alter table LF_DEP_USER_BALANCE
  add constraint TARGETIDUNIQUE unique (TARGET_ID);


--
-- Creating table LF_DFADVANCED
-- ============================
--
create table LF_DFADVANCED
(
  ID            NUMBER(38) not null,
  USER_ID       NUMBER(38) not null,
  BUS_CODE      VARCHAR2(64) default '',
  PRIORITY      VARCHAR2(8) default '0',
  SPUSER_ID     VARCHAR2(11) default ' ',
  REPLY_SET     NUMBER(2) default 0,
  REPEAT_FILTER NUMBER(1) default 2,
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
  );
alter table LF_DFADVANCED
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
  );

--
-- Creating table LF_SYSUSER
-- =========================
--
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
  ON_LINE         NUMBER(1) default 0,
  CORP_CODE       VARCHAR2(64),
  P_ID            NUMBER(38),
  EMPLOYEE_NO     VARCHAR2(64),
  REC_STATE       NUMBER(1) default 0,
  HIDEPH_STATE    NUMBER(1) default 0,
  DEP_CODE        VARCHAR2(64),
  LASTUPDDTIM     TIMESTAMP(8),
  HR_STATUS       VARCHAR2(20) default 'A',
  FAX             VARCHAR2(22),
  ALLOW_LOGIN     NUMBER(1) default 0,
  POST_ID         NUMBER(38),
  PERMISSION_TYPE NUMBER(1) default 2,
  USER_CODE       VARCHAR2(30),
  ISEXISTSUBNO    NUMBER(1),
  UP_GUID         NUMBER(38),
  IS_REVIEWER     NUMBER(2),
  DUTIES          VARCHAR2(200),
  IS_AUDITED      NUMBER(2),
  CONTROLFLAG     VARCHAR2(16) default '0000' not null,
  PWDUPDATETIME   TIMESTAMP(8) default sysdate,
  PWDERRORTIMES   NUMBER(20) default 0,
  WORKNUMBER      VARCHAR2(30),
  FINDPWDTIMES    NUMBER(20),
  FINDPWDDATE     VARCHAR2(30),
  IS_CUSTOME      NUMBER(20),
  LANGUAGE_CODE   VARCHAR2(10)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_SYSUSER
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
  );
alter table LF_SYSUSER
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
  );
alter table LF_SYSUSER
  add constraint FK_LF_SYSUS_FK_LF_SYS_LF_DEP foreign key (DEP_ID)
  references LF_DEP (DEP_ID);
create index FK_LF_SYS_RELATIONS_LF_DEP_FK on LF_SYSUSER (DEP_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_DOMINATION
-- ============================
--
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
  );
alter table LF_DOMINATION
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
  );
alter table LF_DOMINATION
  add constraint FK_LF_DOMIN_LF_DOMINA_LF_DEP foreign key (DEP_ID)
  references LF_DEP (DEP_ID);
alter table LF_DOMINATION
  add constraint FK_LF_DOMIN_LF_DOMINA_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);
create index LF_DOMINATION_FK on LF_DOMINATION (DEP_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_DOMINATION_FK2 on LF_DOMINATION (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_DYNPHONEWORD
-- ==============================
--
create table LF_DYNPHONEWORD
(
  USERID     NUMBER(38) not null,
  PHONEWORD  VARCHAR2(10),
  CREATETIME DATE,
  ISLOGIN    NUMBER(38) default 0 not null,
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
  );

--
-- Creating table LF_SERVICE
-- =========================
--
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
  CPNO          VARCHAR2(21) default ' ',
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
  );
alter table LF_SERVICE
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
  );
alter table LF_SERVICE
  add constraint FK_LF_SERVI_LF_ADDSER_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);
alter table LF_SERVICE
  add constraint FK_SERVICE_BUSMANAGER foreign key (BUS_ID)
  references LF_BUSMANAGER (BUS_ID);
create index LF_ADDSERVICE_FK on LF_SERVICE (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_PROCESS
-- =========================
--
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
  );
alter table LF_PROCESS
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
  );
alter table LF_PROCESS
  add constraint FK_LF_PROCE_FK_LF_PRO_LF_DBCON foreign key (DB_ID)
  references LF_DBCONNECT (DB_ID) on delete set null;
alter table LF_PROCESS
  add constraint FK_LF_PROCE_FK_LF_PRO_LF_SERVI foreign key (SER_ID)
  references LF_SERVICE (SER_ID);
create index FK_LF_PRO_RELATIONS_LF_DBC_FK on LF_PROCESS (DB_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index FK_LF_PRO_RELATIONS_LF_SEN_FK on LF_PROCESS (SG_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index FK_LF_PRO_RELATIONS_LF_SER_FK on LF_PROCESS (SER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_EDIT
-- ======================
--
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
  );
alter table LF_EDIT
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
  );
alter table LF_EDIT
  add constraint FK_LF_EDIT_FK_LF_EDI_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID);
create index FK_LF_EDI_RELATIONS_LF_PRO_FK on LF_EDIT (PR_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_EMPDEP_CONN
-- =============================
--
create table LF_EMPDEP_CONN
(
  USER_ID        NUMBER(38) not null,
  DEP_CODE_THIRD VARCHAR2(64) default '0' not null,
  CONN_ID        NUMBER(38),
  DEP_ID         NUMBER(38) default 0 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_EMPDEP_CONN
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
  );

--
-- Creating table LF_EMPLOYEE
-- ==========================
--
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
  );
alter table LF_EMPLOYEE
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
  );
alter table LF_EMPLOYEE
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
  );
create index FK_LF_LIS_RELATIONS_LF_EMP_FK on LF_EMPLOYEE (DEP_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index FK_LF_SYS_RELATIONS_LF_POS_FK on LF_EMPLOYEE (P_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_EMPLOYEE_DEP
-- ==============================
--
create table LF_EMPLOYEE_DEP
(
  DEP_ID         NUMBER(38) not null,
  DEP_CODE       VARCHAR2(64),
  DEP_NAME       VARCHAR2(64),
  DEP_PCODE      VARCHAR2(64),
  DEP_EFF_STATUS VARCHAR2(20),
  DEP_LEVEL      NUMBER(2) default 0,
  ADD_TYPE       NUMBER(5),
  CORP_CODE      VARCHAR2(64),
  PARENT_ID      NUMBER(38) default 0 not null,
  DEP_CODE_THIRD VARCHAR2(64) default '',
  DEP_PATH       VARCHAR2(160) default '',
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
  );
alter table LF_EMPLOYEE_DEP
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
  );

--
-- Creating table LF_EMPLOYEE_TYPE
-- ===============================
--
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
  );
alter table LF_EMPLOYEE_TYPE
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
  );
alter table LF_EMPLOYEE_TYPE
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
  );

--
-- Creating table LF_ENTERPRISE
-- ============================
--
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
  );
alter table LF_ENTERPRISE
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
  );

--
-- Creating table LF_EXAMINE_SMS
-- =============================
--
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
  );
alter table LF_EXAMINE_SMS
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
  );

--
-- Creating table LF_EXTERNAL_USER
-- ===============================
--
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
  );

--
-- Creating table LF_FLOW
-- ======================
--
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
  );
alter table LF_FLOW
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
  );

--
-- Creating table LF_FLOWBINDOBJ
-- =============================
--
create table LF_FLOWBINDOBJ
(
  ID          NUMBER(38) not null,
  F_ID        NUMBER(38) not null,
  OBJ_TYPE    NUMBER(2),
  OBJ_CODE    VARCHAR2(64),
  INFO_TYPE   NUMBER(2),
  CORP_CODE   VARCHAR2(64),
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
  );

--
-- Creating table LF_FLOWBINDTYPE
-- ==============================
--
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
  );

--
-- Creating table LF_FLOWRECORD
-- ============================
--
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
  SUBMITTIME     TIMESTAMP(8)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_FLOWRECORD
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
  );
create index FK_LF_FLO_RELATIONS_LF_MTT_FK on LF_FLOWRECORD (MT_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_GLOBAL_VARIABLE
-- =================================
--
create table LF_GLOBAL_VARIABLE
(
  GLOBALID       NUMBER(38) not null,
  GLOBALKEY      VARCHAR2(32) not null,
  GLOBALVALUE    NUMBER(38),
  GLOBALSTRVALUE VARCHAR2(2000)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_GLOBAL_VARIABLE
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
  );

--
-- Creating table LF_PRIVILEGE
-- ===========================
--
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
  );
alter table LF_PRIVILEGE
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
  );
create index FK_LF_PRI_RELATIONS_LF_OPE_FK on LF_PRIVILEGE (OPERATE_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index FK_LF_PRI_RELATIONS_LF_RES_FK on LF_PRIVILEGE (RESOURCE_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_ROLES
-- =======================
--
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
  );
alter table LF_ROLES
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
  );

--
-- Creating table LF_IMPOWER
-- =========================
--
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
  );
alter table LF_IMPOWER
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
  );
alter table LF_IMPOWER
  add constraint FK_LF_IMPOW_LF_IMPOWE_LF_PRIVI foreign key (PRIVILEGE_ID)
  references LF_PRIVILEGE (PRIVILEGE_ID);
alter table LF_IMPOWER
  add constraint FK_LF_IMPOW_LF_IMPOWE_LF_ROLES foreign key (ROLE_ID)
  references LF_ROLES (ROLE_ID);
create index LF_IMPOWER_FK on LF_IMPOWER (ROLE_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_IMPOWER_FK2 on LF_IMPOWER (PRIVILEGE_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_IM_CLIENT
-- ===========================
--
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
  );
alter table LF_IM_CLIENT
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
  );

--
-- Creating table LF_IM_GROUP
-- ==========================
--
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
  );
alter table LF_IM_GROUP
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
  );

--
-- Creating table LF_IM_CLIENTGROUP
-- ================================
--
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
  );
alter table LF_IM_CLIENTGROUP
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
  );
alter table LF_IM_CLIENTGROUP
  add constraint FK_CGP_FK_LF_IM_CLIENT foreign key (CLIENTID)
  references LF_IM_CLIENT (CLIENTID);
alter table LF_IM_CLIENTGROUP
  add constraint FK_CGP_FK_LF_IM_GROUP foreign key (GROUPID)
  references LF_IM_GROUP (GROUPID);

--
-- Creating table LF_IM_DIALOG
-- ===========================
--
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
  );
alter table LF_IM_DIALOG
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
  );

--
-- Creating table LF_IM_GPUSERLINK
-- ===============================
--
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
  );
alter table LF_IM_GPUSERLINK
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
  );

--
-- Creating table LF_IM_GROUPMESSAGE
-- =================================
--
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
  );
alter table LF_IM_GROUPMESSAGE
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
  );

--
-- Creating table LF_IM_HISTORY
-- ============================
--
create table LF_IM_HISTORY
(
  HISTORYID     NUMBER(38) not null,
  USERID        NUMBER(38) not null,
  PHONE         VARCHAR2(50),
  REGTIME       TIMESTAMP(6) default SYSDATE,
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
  ISAPPEAR      NUMBER(1) default 1 not null,
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
  );
alter table LF_IM_HISTORY
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
  );

--
-- Creating table LF_IM_OFFICEGROUP
-- ================================
--
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
  );
alter table LF_IM_OFFICEGROUP
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
  );

--
-- Creating table LF_IM_MEMBERGROUP
-- ================================
--
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
  );
alter table LF_IM_MEMBERGROUP
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
  );
alter table LF_IM_MEMBERGROUP
  add constraint FK_LF_MGP_FK_LF_IM_OFGP foreign key (GROUPID)
  references LF_IM_OFFICEGROUP (GROUPID);

--
-- Creating table LF_IM_RECENTCONTACTS
-- ===================================
--
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
  );
alter table LF_IM_RECENTCONTACTS
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
  );

--
-- Creating table LF_IM_SEATGROUP
-- ==============================
--
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
  );
alter table LF_IM_SEATGROUP
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
  );

--
-- Creating table LF_IM_SEATLIMIT
-- ==============================
--
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
  );
alter table LF_IM_SEATLIMIT
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
  );

--
-- Creating table LF_IM_SEATUSER
-- =============================
--
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
  );
alter table LF_IM_SEATUSER
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
  );
alter table LF_IM_SEATUSER
  add constraint FK_LF_SU_FK_LF_IM_SG foreign key (SEATGROUPID)
  references LF_IM_SEATGROUP (SEATGROUPID);

--
-- Creating table LF_IM_SEAT_CGLINK
-- ================================
--
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
  );
alter table LF_IM_SEAT_CGLINK
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
  );

--
-- Creating table LF_IM_SEAT_CLIENT
-- ================================
--
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
  );
alter table LF_IM_SEAT_CLIENT
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
  );

--
-- Creating table LF_IM_SEAT_CLIENTGP
-- ==================================
--
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
  );
alter table LF_IM_SEAT_CLIENTGP
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
  );

--
-- Creating table LF_IM_SHARECLIENT
-- ================================
--
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
  );
alter table LF_IM_SHARECLIENT
  add constraint FK_LF_SC_FK_LF_IM_CLIENT foreign key (CLIENTID)
  references LF_IM_CLIENT (CLIENTID);
alter table LF_IM_SHARECLIENT
  add constraint FK_LF_SC_FK_LF_SYSUSER foreign key (USERID)
  references LF_SYSUSER (USER_ID);

--
-- Creating table LF_IM_USERMESSAGE
-- ================================
--
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
  );
alter table LF_IM_USERMESSAGE
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
  );

--
-- Creating table LF_USEDSUBNO
-- ===========================
--
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
  );
alter table LF_USEDSUBNO
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
  );

--
-- Creating table LF_IM_USERSUBNO
-- ==============================
--
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
  );
alter table LF_IM_USERSUBNO
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
  );
alter table LF_IM_USERSUBNO
  add constraint FK_IM_USERSUBNO_FK_SYSUSER foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);
alter table LF_IM_USERSUBNO
  add constraint FK_IM_USERSUBNO_FK_USEDSUBNO foreign key (SUBNO_ID)
  references LF_USEDSUBNO (SUBNO_ID);

--
-- Creating table LF_KEYWORDS
-- ==========================
--
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
  );
alter table LF_KEYWORDS
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
  );

--
-- Creating table LF_KEY_VALUE
-- ===========================
--
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
  );

--
-- Creating table LF_UDGROUP
-- =========================
--
create table LF_UDGROUP
(
  UDG_ID       NUMBER(38) not null,
  UDG_NAME     VARCHAR2(64),
  USER_ID      NUMBER(38) not null,
  GROUP_TYPE   NUMBER(1) default 2,
  GP_ATTRIBUTE NUMBER(1) default 0,
  SEND_MODE    NUMBER(1) default 1 not null,
  SHARE_TYPE   NUMBER(1),
  GROUP_ID     NUMBER(38),
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
  );
alter table LF_UDGROUP
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
  );
alter table LF_UDGROUP
  add constraint FK_LF_UDGRO_FK_LF_SYS_LF_SYS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);

--
-- Creating table LF_LIST2GRO
-- ==========================
--
create table LF_LIST2GRO
(
  L2G_ID       NUMBER(38) not null,
  L2G_TYPE     NUMBER(4),
  UDG_ID       NUMBER(38),
  GUID         NUMBER(38),
  RECEIVE_FLAG NUMBER(1) default 0,
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
  );
alter table LF_LIST2GRO
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
  );
alter table LF_LIST2GRO
  add constraint FK_LF_UDG_RELATIONS_LF_LIS foreign key (UDG_ID)
  references LF_UDGROUP (UDG_ID);

--
-- Creating table LF_MACIP
-- =======================
--
create table LF_MACIP
(
  LMIID       NUMBER(38) not null,
  GUID        NUMBER(38),
  MAC_ADDR    VARCHAR2(200),
  IP_ADDR     VARCHAR2(200),
  TYPE        NUMBER(4),
  CREATORNAME VARCHAR2(64),
  CREATTIME   DATE,
  DT_PWD      NUMBER(1) default 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_MALIST
-- ========================
--
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
  );
alter table LF_MALIST
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
  );

--
-- Creating table LF_MAPPING
-- =========================
--
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
  );
alter table LF_MAPPING
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
  );

--
-- Creating table LF_MATERIAL_SORT
-- ===============================
--
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
  );
alter table LF_MATERIAL_SORT
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
  );

--
-- Creating table LF_MATERIAL
-- ==========================
--
create table LF_MATERIAL
(
  MTAL_ID      NUMBER(38) not null,
  MTAL_NAME    VARCHAR2(64) not null,
  MTAL_TYPE    VARCHAR2(32) not null,
  MTAL_SIZE    VARCHAR2(20),
  USER_ID      NUMBER(38),
  MTAL_UPTIME  TIMESTAMP(8) default sysdate,
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
  );
alter table LF_MATERIAL
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
  );
alter table LF_MATERIAL
  add constraint FK_MATERIAL_LF_MATERIAL_SORT foreign key (SORT_ID)
  references LF_MATERIAL_SORT (SORT_ID);

--
-- Creating table LF_MMSACCBIND
-- ============================
--
create table LF_MMSACCBIND
(
  ID          NUMBER(38) not null,
  MMS_USER    VARCHAR2(32),
  USER_ID     NUMBER(38),
  CORP_CODE   VARCHAR2(64),
  IS_VALIDATE NUMBER(1),
  CREATETIME  DATE default sysdate,
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
  );
alter table LF_MMSACCBIND
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
  );
create index IND_MMSACCBIND_CODE on LF_MMSACCBIND (CORP_CODE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index IND_MMSACCBIND_USER_ID on LF_MMSACCBIND (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_MMSACCMANAGEMENT
-- ==================================
--
create table LF_MMSACCMANAGEMENT
(
  ID                NUMBER(38) not null,
  MMS_USERID        VARCHAR2(32),
  MMS_NAME          VARCHAR2(64),
  MMS_PASSWORD      VARCHAR2(32),
  MMS_CREATETIME    DATE default sysdate,
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
  );
alter table LF_MMSACCMANAGEMENT
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
  );
create index IND_MMSACCMANAGEMENT_USERID on LF_MMSACCMANAGEMENT (MMS_USERID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_MMSBLIST
-- ==========================
--
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
  );

--
-- Creating table LF_MMSINFO
-- =========================
--
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
  );
alter table LF_MMSINFO
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
  );

--
-- Creating table LF_MMSMTTASK
-- ===========================
--
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
  );
alter table LF_MMSMTTASK
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
  );

--
-- Creating table LF_MMSTASKREPORT
-- ===============================
--
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
  );

--
-- Creating table LF_MMSTEMPLATE
-- =============================
--
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
  );
alter table LF_MMSTEMPLATE
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
  );
create index LF_MMSADDTEMP_FK on LF_MMSTEMPLATE (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_MMS_MTREPORT
-- ==============================
--
create table LF_MMS_MTREPORT
(
  USERID      VARCHAR2(11),
  TASKID      NUMBER(11),
  SPGATE      VARCHAR2(21),
  IYMD        NUMBER(11),
  IHOUR       NUMBER(11),
  PTCODE      CHAR(10) default (' '),
  IMONTH      NUMBER(11),
  ICOUNT      NUMBER(11) default 0,
  SUCC        NUMBER(11) default 0,
  FAIL1       NUMBER(11) default 0,
  FAIL2       NUMBER(11) default 0,
  FAIL3       NUMBER(11) default 0,
  NRET        NUMBER(11) default 0,
  RSUCC       NUMBER(11) default 0,
  RFAIL1      NUMBER(11) default 0,
  RFAIL2      NUMBER(11) default 0,
  RNRET       NUMBER(11) default 0,
  RELEASEFLAG NUMBER(11) default 1,
  STARTTIME   TIMESTAMP(6) default SYSTIMESTAMP,
  ENDTIME     TIMESTAMP(6) default SYSTIMESTAMP,
  ID          NUMBER(11) not null,
  Y           NUMBER(11) default 0,
  SPISUNCM    NUMBER(11) default 0
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
  );
alter table LF_MMS_MTREPORT
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
  );

--
-- Creating table LF_MOBFINANCIAL
-- ==============================
--
create table LF_MOBFINANCIAL
(
  MOBFID    NUMBER(38) not null,
  PHONE     VARCHAR2(64),
  MSG       VARCHAR2(200),
  TASK_ID   NUMBER(38),
  SEND_TIME TIMESTAMP(8) default SYSDATE
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_MON_SGTACINFO
-- ===============================
--
create table LF_MON_SGTACINFO
(
  HOSTID      NUMBER(38) not null,
  GATEACCOUNT VARCHAR2(11) not null,
  GATENAME    VARCHAR2(56) default ' ' not null,
  GATEWAYID   NUMBER(38),
  MONSTATUS   NUMBER(3) default 1 not null,
  LINKNUM     NUMBER(20) default 1 not null,
  USERFEE     NUMBER(20) default 0 not null,
  ISARREARAGE NUMBER(20) default 0 not null,
  MTREMAINED  NUMBER(20) default 0 not null,
  MTRECVSPD   NUMBER(20) default 0 not null,
  MOREMAINED  NUMBER(20) default 0 not null,
  MOSNDSPD    NUMBER(20) default 0 not null,
  MOSNDRATIO  NUMBER(20) default 0 not null,
  RPTREMAINED NUMBER(20) default 0 not null,
  RPTSNDSPD   NUMBER(20) default 0 not null,
  RPTSNDRATIO NUMBER(20) default 0 not null,
  CREATETIME  TIMESTAMP(8) default sysdate not null,
  MODIFYTIME  TIMESTAMP(8) default sysdate not null,
  MONPHONE    VARCHAR2(256),
  MONFREQ     NUMBER(20) default 5 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_SGTACINFO
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
  );

--
-- Creating table LF_MON_DGTACINFO
-- ===============================
--
create table LF_MON_DGTACINFO
(
  ID           NUMBER(38) not null,
  GATEACCOUNT  VARCHAR2(11) default ' ' not null,
  EVTTYPE      NUMBER(20) default 0 not null,
  GATEWAYID    NUMBER(38),
  LINKNUM      NUMBER(20) default 1 not null,
  LOGINIP      VARCHAR2(64) default ' ' not null,
  ONLINESTATUS NUMBER(3) default 0 not null,
  USERFEE      NUMBER(20) default 0 not null,
  FEEFLAG      NUMBER(20) default 0 not null,
  MTTOTALSND   NUMBER(20) default 0 not null,
  MTHAVESND    NUMBER(20) default 0 not null,
  MTREMAINED   NUMBER(20) default 0 not null,
  MTRECVSPD    NUMBER(20) default 0 not null,
  MOTOTALRECV  NUMBER(20) default 0 not null,
  MOHAVESND    NUMBER(20) default 0 not null,
  MOREMAINED   NUMBER(20) default 0 not null,
  MOSNDSPD     NUMBER(20) default 0 not null,
  RPTHAVESND   NUMBER(20) default 0 not null,
  RPTREMAINED  NUMBER(20) default 0 not null,
  RPTSNDSPD    NUMBER(20) default 0 not null,
  RPTTOTALRECV NUMBER(20) default 0 not null,
  LOGININTM    TIMESTAMP(8) default sysdate not null,
  LOGINOUTTM   TIMESTAMP(8) default sysdate not null,
  MODIFYTIME   TIMESTAMP(8) default sysdate not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_DGTACINFO
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
  );
alter table LF_MON_DGTACINFO
  add constraint FK_LF_DGTACINFO_LF_SGTACINFO foreign key (GATEACCOUNT)
  references LF_MON_SGTACINFO (GATEACCOUNT);

--
-- Creating table LF_MON_SHOST
-- ===========================
--
create table LF_MON_SHOST
(
  HOSTID        NUMBER(38) not null,
  HOSTNAME      VARCHAR2(64) not null,
  HOSTTYPE      NUMBER(3) not null,
  ADAPTER1      VARCHAR2(72),
  ADAPTER2      VARCHAR2(72) default '',
  OUPIP         VARCHAR2(64) default ' ',
  HOSTMEM       NUMBER(20),
  HOSTHD        NUMBER(20),
  HOSTCPU       VARCHAR2(64) default '',
  OPERSYS       VARCHAR2(128) default '',
  MONSTATUS     NUMBER(3) default 1 not null,
  CREATETIME    TIMESTAMP(8) default sysdate not null,
  MODIFYTIME    TIMESTAMP(8) default sysdate not null,
  DESCR         VARCHAR2(128) default '',
  MONPHONE      VARCHAR2(256),
  CPUUSAGE      NUMBER(20) default 0 not null,
  MEMUSAGE      NUMBER(20) default 0 not null,
  VMEMUSAGE     NUMBER(20) default 0 not null,
  DISKFREESPACE NUMBER(20) default 0 not null,
  CPUSJ         NUMBER(20) default 0,
  CPUBL         NUMBER(20) default 0,
  PROCESSCNT    NUMBER(20) default 0 not null,
  MONFREQ       NUMBER(20) default 5 not null,
  HOSTUSESTATUS NUMBER(20) default 0 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_SHOST
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
  );

--
-- Creating table LF_MON_DHOST
-- ===========================
--
create table LF_MON_DHOST
(
  ID            NUMBER(38) not null,
  HOSTID        NUMBER(38) not null,
  HOSTSTATUS    NUMBER(3) default 0 not null,
  EVTTYPE       NUMBER(20) default 0 not null,
  MEMUSE        NUMBER(20) default 0,
  VMEMUSE       NUMBER(20) default 0,
  DISKSPACE     NUMBER(20) default 0,
  CPUUSAGE      NUMBER(20) default 0 not null,
  MEMUSAGE      NUMBER(20) default 0 not null,
  VMEMUSAGE     NUMBER(20) default 0 not null,
  DISKFREESPACE NUMBER(20) default 0 not null,
  UPDATETIME    TIMESTAMP(8) default sysdate not null,
  PROCESSCNT    NUMBER(20) default 0 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_DHOST
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
  );
alter table LF_MON_DHOST
  add constraint FK_LF_DHOST_FK_LF_SHOST foreign key (HOSTID)
  references LF_MON_SHOST (HOSTID);

--
-- Creating table LF_MON_SPROCE
-- ============================
--
create table LF_MON_SPROCE
(
  PROCEID        NUMBER(38) not null,
  HOSTID         NUMBER(38) not null,
  PROCENAME      VARCHAR2(64) not null,
  VERSION        VARCHAR2(64) default ' ' not null,
  MONFREQ        NUMBER(20) default 5 not null,
  MONSTATUS      NUMBER(3) default 1 not null,
  PROCETYPE      NUMBER(20) not null,
  GATEWAYID      NUMBER(38),
  CREATETIME     TIMESTAMP(8) default sysdate not null,
  MODIFYTIME     TIMESTAMP(8) default sysdate not null,
  MONPHONE       VARCHAR2(256),
  DESCR          VARCHAR2(128),
  CPUBL          NUMBER(20) default 0 not null,
  MEMUSE         NUMBER(20) default 0 not null,
  VMEMUSE        NUMBER(20) default 0 not null,
  HARDDISKSPACE  NUMBER(20) default 0 not null,
  CURTHREADUSE   NUMBER(20) default 0,
  CURSEESIONUSE  NUMBER(20) default 0,
  CURCONUSE      NUMBER(20) default 0,
  HEAPUSE        NUMBER(20) default 0,
  NONHEAPUSE     NUMBER(20) default 0,
  PROCEUSESTATUS NUMBER(20) default 0 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_SPROCE
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
  );

--
-- Creating table LF_MON_DPROCE
-- ============================
--
create table LF_MON_DPROCE
(
  ID            NUMBER(38) not null,
  PROCEID       NUMBER(38) not null,
  GATEWAYID     NUMBER(38),
  EVTTYPE       NUMBER(20) default 0 not null,
  CPUUSAGE      NUMBER(20) default 0 not null,
  MEMUSAGE      NUMBER(20) default 0 not null,
  VMEMUSAGE     NUMBER(20) default 0 not null,
  DISPFREE      NUMBER(20) default 0 not null,
  PROCESTATUS   NUMBER(3) default 0 not null,
  UPDATETIME    TIMESTAMP(8) default sysdate not null,
  STARTTIME     TIMESTAMP(8),
  MAXTHREADNUM  NUMBER(20) default 0,
  CURTHREADNUM  NUMBER(20) default 0,
  BUSYTHREADNUM NUMBER(20) default 0,
  CURSEESIONNUM NUMBER(20) default 0,
  MAXACTIVENUM  NUMBER(20) default 0,
  CURCONNUM     NUMBER(20) default 0,
  HEAPUSE       NUMBER(20) default 0,
  NONHEAPUSE    NUMBER(20) default 0
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_DPROCE
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
  );
alter table LF_MON_DPROCE
  add constraint FK_LF_DPROCE_FK_LF_DPROCE foreign key (PROCEID)
  references LF_MON_SPROCE (PROCEID);

--
-- Creating table LF_MON_SSPACINFO
-- ===============================
--
create table LF_MON_SSPACINFO
(
  HOSTID        NUMBER(38) not null,
  SPACCOUNTID   VARCHAR2(11) not null,
  ACCOUNTNAME   VARCHAR2(56) default ' ' not null,
  SPACCOUNTTYPE NUMBER(3) default 0 not null,
  SENDLEVEL     NUMBER(20),
  MONSTATUS     NUMBER(3) default 1 not null,
  MAXLINKNUM    NUMBER(20) default 1 not null,
  USERFEE       NUMBER(20) default 0 not null,
  MTHAVESND     NUMBER(20) default 0 not null,
  MTREMAINED    NUMBER(20) default 0 not null,
  MTSNDSPD      NUMBER(20) default 0 not null,
  MTISSUEDSPD   NUMBER(20) default 0 not null,
  MOREMAINED    NUMBER(20) default 0 not null,
  MORECVRATIO   NUMBER(20) default 0 not null,
  RPTREMAINED   NUMBER(20) default 0 not null,
  RPTSNDSPD     NUMBER(20) default 0 not null,
  RPTRECVRATIO  NUMBER(20) default 0 not null,
  CREATETIME    TIMESTAMP(8) default sysdate not null,
  MODIFYTIME    TIMESTAMP(8) default sysdate not null,
  MONPHONE      VARCHAR2(256),
  MONFREQ       NUMBER(20) default 5 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_SSPACINFO
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
  );

--
-- Creating table LF_MON_DSPACINFO
-- ===============================
--
create table LF_MON_DSPACINFO
(
  ID           NUMBER(38) not null,
  SPACCOUNTID  VARCHAR2(11) default ' ' not null,
  EVTTYPE      NUMBER(20) default 0 not null,
  LINKNUM      NUMBER(20) default 1 not null,
  LOGINIP      VARCHAR2(64) default ' ' not null,
  ONLINESTATUS NUMBER(3) default 0 not null,
  SENDLEVEL    NUMBER(20) default 5,
  USERFEE      NUMBER(20) default 0 not null,
  FEEFLAG      NUMBER(20) default 0 not null,
  MTTOTALSND   NUMBER(20) default 0 not null,
  MTHAVESND    NUMBER(20) default 0 not null,
  MTREMAINED   NUMBER(20) default 0 not null,
  MTSNDSPD     NUMBER(20) default 0 not null,
  MTISSUEDSPD  NUMBER(20) default 0 not null,
  MOTOTALRECV  NUMBER(20) default 0 not null,
  MOHAVESND    NUMBER(20) default 0 not null,
  MOREMAINED   NUMBER(20) default 0 not null,
  MOSNDSPD     NUMBER(20) default 0 not null,
  RPTTOTALRECV NUMBER(20) default 0 not null,
  RPTREMAINED  NUMBER(20) default 0 not null,
  RPTSNDSPD    NUMBER(20) default 0 not null,
  LOGININTM    TIMESTAMP(8) default sysdate not null,
  LOGINOUTTM   TIMESTAMP(8) default sysdate not null,
  UPDATETIME   TIMESTAMP(8) default sysdate not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_DSPACINFO
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
  );
alter table LF_MON_DSPACINFO
  add constraint FK_LF_DSPACINFO_LF_SSPACINFO foreign key (SPACCOUNTID)
  references LF_MON_SSPACINFO (SPACCOUNTID);

--
-- Creating table LF_MON_ERR
-- =========================
--
create table LF_MON_ERR
(
  ID           NUMBER(38) not null,
  HOSTID       NUMBER(38) not null,
  PROCEID      NUMBER(38) default 0 not null,
  SPACCOUNTID  VARCHAR2(11) default ' ' not null,
  GATEACCOUNT  VARCHAR2(11) default ' ' not null,
  APPTYPE      NUMBER(20) not null,
  EVTID        NUMBER(38),
  WHO          VARCHAR2(16) default '',
  EVTTYPE      NUMBER(3),
  DEALPEOPLE   VARCHAR2(20),
  DEALDESC     VARCHAR2(512),
  MONSTATUS    NUMBER(3) default 1 not null,
  DEALFLAG     NUMBER(3) default 1 not null,
  CRTTIME      TIMESTAMP(8),
  EVTTIME      TIMESTAMP(8) not null,
  RCVTIME      TIMESTAMP(8) not null,
  MSG          VARCHAR2(512) not null,
  MONTHRESHOLD NUMBER(20) default 0,
  MONTIMER     NUMBER(38) default 1
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_ERR
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
  );

--
-- Creating table LF_MON_ERRHIS
-- ============================
--
create table LF_MON_ERRHIS
(
  ID          NUMBER(38) not null,
  HOSTID      NUMBER(38) not null,
  PROCEID     NUMBER(38) default 0 not null,
  SPACCOUNTID VARCHAR2(11) default ' ' not null,
  GATEACCOUNT VARCHAR2(11) default ' ' not null,
  APPTYPE     NUMBER(20) not null,
  WHO         VARCHAR2(16) default '',
  EVTID       NUMBER(38),
  EVTTIME     TIMESTAMP(8) not null,
  EVTTYPE     NUMBER(3),
  DEALPEOPLE  VARCHAR2(20) default ' ' not null,
  DEALDESC    VARCHAR2(512),
  DEALFLAG    NUMBER(3) default 1 not null,
  MONSTATUS   NUMBER(3) default 1 not null,
  RCVTIME     TIMESTAMP(8) not null,
  CRTTIME     TIMESTAMP(8) not null,
  MSG         VARCHAR2(512) not null,
  MONTIMER    NUMBER(38) default 1
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_ERRHIS
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
  );

--
-- Creating table LF_MON_ONLCFG
-- ============================
--
create table LF_MON_ONLCFG
(
  ID         NUMBER(38) not null,
  ONLINENUM  NUMBER(38) default 0 not null,
  MAX_ONLINE NUMBER(38) default 500 not null,
  EVTTYPE    NUMBER(20) default 0 not null,
  MONFREQ    NUMBER(38) default 30 not null,
  MONPHONE   VARCHAR2(256),
  MODIFYTIME TIMESTAMP(8) default sysdate not null,
  MONSTATUS  NUMBER(3) default 1 not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_ONLCFG
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
  );

--
-- Creating table LF_MON_ONLUSER
-- =============================
--
create table LF_MON_ONLUSER
(
  SESSEION_ID VARCHAR2(64) not null,
  CORP_CODE   VARCHAR2(64) not null,
  USER_NAME   VARCHAR2(64) not null,
  NAME        VARCHAR2(64) not null,
  USER_ID     NUMBER(38) not null,
  DEP_ID      NUMBER(38) not null,
  LOGINTIME   TIMESTAMP(8) not null,
  LOGINADDR   VARCHAR2(15) not null,
  SERVERNUM   VARCHAR2(8) not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_ONLUSER
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
  );

--
-- Creating table LF_MON_SPGATEBUF
-- ===============================
--
create table LF_MON_SPGATEBUF
(
  ID           NUMBER(38) not null,
  PROCEID      NUMBER(38) not null,
  GATEWAYID    NUMBER(38) not null,
  MTRVCNT      NUMBER(20) default 0 not null,
  MTSDCNT      NUMBER(20) default 0 not null,
  MTSDBUF      NUMBER(20) default 0 not null,
  MTUPDBUF     NUMBER(20) default 0 not null,
  MTSDWAITBUF  NUMBER(20) default 0 not null,
  MTSPD1       NUMBER(20) default 0 not null,
  MTSPD2       NUMBER(20) default 0 not null,
  MORVCNT      NUMBER(20) default 0 not null,
  RPTRVCNT     NUMBER(20) default 0 not null,
  MORVBUF      NUMBER(20) default 0 not null,
  RPTRVBUF     NUMBER(20) default 0 not null,
  MOSDBUF      NUMBER(20) default 0 not null,
  RPTSDBUF     NUMBER(20) default 0 not null,
  MOSDWAITBUF  NUMBER(20) default 0 not null,
  RPTSDWAITBUF NUMBER(20) default 0 not null,
  MORPTSPD     NUMBER(20) default 0 not null,
  UPDATETIME   TIMESTAMP(8) default sysdate not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_SPGATEBUF
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
  );

--
-- Creating table LF_MON_WBSBUF
-- ============================
--
create table LF_MON_WBSBUF
(
  ID           NUMBER(38) not null,
  PROCEID      NUMBER(38) not null,
  GATEWAYID    NUMBER(38) not null,
  MTNOSD       NUMBER(20) default 0 not null,
  MONOSD       NUMBER(20) default 0 not null,
  RPTNOSD      NUMBER(20) default 0 not null,
  ENDCNT       NUMBER(20) default 0 not null,
  MORVCNT      NUMBER(20) default 0 not null,
  MTSDCNT      NUMBER(20) default 0 not null,
  WRMOBUF      NUMBER(20) default 0 not null,
  UPDMOBUF     NUMBER(20) default 0 not null,
  WRRPTBUF     NUMBER(20) default 0 not null,
  ENDRSPBUF    NUMBER(20) default 0 not null,
  MTSDBUF      NUMBER(20) default 0 not null,
  MTWAITBUF    NUMBER(20) default 0 not null,
  PRECNT       NUMBER(20) default 0 not null,
  MTRVCNT      NUMBER(20) default 0 not null,
  MOSDCNT      NUMBER(20) default 0 not null,
  WRMTBUF      NUMBER(20) default 0 not null,
  WRMTVFYBUF   NUMBER(20) default 0 not null,
  WRMTLVLBUF   NUMBER(20) default 0 not null,
  PRERSPBUF    NUMBER(20) default 0 not null,
  PRERSPTMPBUF NUMBER(20) default 0 not null,
  MORPTSDBUF   NUMBER(20) default 0 not null,
  RPTSDBUF     NUMBER(20) default 0 not null,
  MORPTWAITBUF NUMBER(20) default 0 not null,
  LOGFILENUM   NUMBER(20) default 0 not null,
  LOGBUF       NUMBER(20) default 0 not null,
  RECVBUF      NUMBER(20) default 0 not null,
  RESNDBUF     NUMBER(20) default 0 not null,
  SUPPSDBUF    NUMBER(20) default 0 not null,
  MTRVSPD1     NUMBER(20) default 0 not null,
  MTSDSPD1     NUMBER(20) default 0 not null,
  MTRVSPD2     NUMBER(20) default 0 not null,
  MTSDSPD2     NUMBER(20) default 0 not null,
  MORPTRVSPD1  NUMBER(20) default 0 not null,
  MORPTSDSPD1  NUMBER(20) default 0 not null,
  MORPTRVSPD2  NUMBER(20) default 0 not null,
  MORPTSDSPD2  NUMBER(20) default 0 not null,
  UPDATETIME   TIMESTAMP(8) default sysdate not null
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MON_WBSBUF
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
  );

--
-- Creating table LF_MOTASK
-- ========================
--
create table LF_MOTASK
(
  MO_ID       NUMBER(38) not null,
  PTMSGID     NUMBER(19) default 0,
  UIDS        NUMBER(38) default 0,
  ORGUID      NUMBER(38) default 0,
  ECID        NUMBER(38) default 0,
  SP_USER     VARCHAR2(11) default (''),
  SPNUMBER    VARCHAR2(21) default (' '),
  SERVICEID   CHAR(10) default (' '),
  SPSC        VARCHAR2(32),
  SENDSTATUS  NUMBER(38) default 2,
  MSGFMT      NUMBER(38) default 15,
  TP_PID      NUMBER(38) default 0,
  TP_UDHI     NUMBER(38) default 0,
  DELIVERTIME TIMESTAMP(6) default sysdate not null,
  PHONE       VARCHAR2(21) default (' '),
  MSGCONTENT  VARCHAR2(720) default (' '),
  DONETIME    TIMESTAMP(6) default SYSTIMESTAMP not null,
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
  );
alter table LF_MOTASK
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
  );

--
-- Creating table LF_MO_SERVICE
-- ============================
--
create table LF_MO_SERVICE
(
  MS_ID       NUMBER(38) not null,
  SP_USER     CHAR(6) default (' ') not null,
  SPNUMBER    CHAR(21) default (' ') not null,
  MSGCONTENT  VARCHAR2(720) default (' ') not null,
  DELIVERTIME TIMESTAMP(6) default sysdate not null,
  PHONE       VARCHAR2(32) default (' ') not null,
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
  );
alter table LF_MO_SERVICE
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
  );
alter table LF_MO_SERVICE
  add constraint FK_MO_SERVICE_FK_PROCESS foreign key (PR_ID)
  references LF_PROCESS (PR_ID);
alter table LF_MO_SERVICE
  add constraint FK_MO_SERVICE_FK_SERVICE foreign key (SER_ID)
  references LF_SERVICE (SER_ID);

--
-- Creating table LF_MTRPT_EC
-- ==========================
--
create table LF_MTRPT_EC
(
  ID        NUMBER(11) not null,
  USERID    VARCHAR2(11) not null,
  TASKID    NUMBER(11) default 0 not null,
  SPGATE    VARCHAR2(21) not null,
  ERRORCODE CHAR(7) default '       ' not null,
  Y         NUMBER(11) default 0 not null,
  IMONTH    NUMBER(11) not null,
  ICOUNT    NUMBER(11) default 0 not null,
  IYMD      NUMBER(11) not null,
  SPISUNCM  NUMBER(11) default 0 not null,
  SVRTYPE   VARCHAR2(64) default (' ') not null,
  P1        VARCHAR2(64) default (' ') not null,
  P2        VARCHAR2(64) default (' ') not null,
  P3        VARCHAR2(64) default (' ') not null,
  P4        VARCHAR2(64) default (' ') not null
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
  );
alter table LF_MTRPT_EC
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
  );
create index IX_LF_MTRPT_EC on LF_MTRPT_EC (TASKID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_MTTASK
-- ========================
--
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
  MS_TYPE       NUMBER(1),
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
  ISRETRY       NUMBER(1) default 0,
  RFAIL2        NUMBER,
  RNRET         NUMBER,
  TASKID        NUMBER(38) default 0,
  TEMP_ID       NUMBER(38),
  PARAM_COUNT   NUMBER(10),
  ICOUNT2       NUMBER,
  TASKTYPE      NUMBER(3) default 1 not null,
  BATCHID       NUMBER(38) default 0 not null,
  STARTSENDTIME TIMESTAMP(8),
  ENDSENDTIME   TIMESTAMP(8),
  WYSENDINFO    VARCHAR2(128)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_MTTASK
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
  );
alter table LF_MTTASK
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
  );
alter table LF_MTTASK
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
  );
create index FK_LF_MTT_RELATIONS_LF_SYS_FK on LF_MTTASK (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_CCODE on LF_MTTASK (CORP_CODE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_RESTATE on LF_MTTASK (RE_STATE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_SENDSTATE on LF_MTTASK (SENDSTATE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_SPUSER on LF_MTTASK (SP_USER)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_SUBSTATE on LF_MTTASK (SUB_STATE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_SUBTIME on LF_MTTASK (SUBMITTIME)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_TASKID on LF_MTTASK (TASKID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index INDEX_LF_MTTASK_TASKTYPE on LF_MTTASK (TASKTYPE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_NOTICE
-- ========================
--
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
  );
alter table LF_NOTICE
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
  );

--
-- Creating table LF_ONLINE_USER
-- =============================
--
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
  FLAG             VARCHAR2(12) default '0000000000',
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
  );

--
-- Creating table LF_OPERATE
-- =========================
--
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
  );
alter table LF_OPERATE
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
  );

--
-- Creating table LF_OPRATELOG
-- ===========================
--
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
  );
alter table LF_OPRATELOG
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
  );

--
-- Creating table LF_PAGEFIELD
-- ===========================
--
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
  );

--
-- Creating table LF_PARAM
-- =======================
--
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
  );

--
-- Creating table LF_PERFECT_NOTIC
-- ===============================
--
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
  );
alter table LF_PERFECT_NOTIC
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
  );
alter table LF_PERFECT_NOTIC
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
  );

--
-- Creating table LF_PERSONALCONFIG
-- ================================
--
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
  );
alter table LF_PERSONALCONFIG
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
  );
alter table LF_PERSONALCONFIG
  add constraint FK_LF_PERCONFIG_FK_BUS foreign key (BUS_ID)
  references LF_BUSINESS (BUS_ID);

--
-- Creating table LF_PER_FILEINFO
-- ==============================
--
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
  );

--
-- Creating table LF_POSITION
-- ==========================
--
create table LF_POSITION
(
  P_ID   NUMBER(38) not null,
  P_NAME VARCHAR2(64),
  ISDEL  NUMBER(1) default 0,
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
  );
alter table LF_POSITION
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
  );

--
-- Creating table LF_PRIV_LIST
-- ===========================
--
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
  );
alter table LF_PRIV_LIST
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
  );
alter table LF_PRIV_LIST
  add constraint FK_LF_PRIV__LF_PRIV_L_LF_DEP foreign key (DEP_ID)
  references LF_DEP (DEP_ID);
alter table LF_PRIV_LIST
  add constraint FK_LF_PRIV__LF_PRIV_L_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);
create index LF_PRIV_LIST_FK on LF_PRIV_LIST (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_PRIV_LIST_FK2 on LF_PRIV_LIST (DEP_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_PROCHARGES
-- ============================
--
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
  COMMENTS           VARCHAR2(250) default '',
  CREATE_TIME        TIMESTAMP(8) default SYSDATE,
  UPDATE_TIME        TIMESTAMP(8) default SYSDATE,
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
  );
alter table LF_PROCHARGES
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
  );

--
-- Creating table LF_PRODUCTOR_CONT
-- ================================
--
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
  );

--
-- Creating table LF_PRODUCTS
-- ==========================
--
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
  CREATE_TIME  TIMESTAMP(8) default SYSDATE,
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
  );
alter table LF_PRODUCTS
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
  );

--
-- Creating table LF_PROSENDCOUNT
-- ==============================
--
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
  );
alter table LF_PROSENDCOUNT
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
  );

--
-- Creating table LF_PRO_CON
-- =========================
--
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
  );
alter table LF_PRO_CON
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
  );
alter table LF_PRO_CON
  add constraint FK_LF_PRO_CON_FK_LF_PROCESS foreign key (USEDPRID)
  references LF_PROCESS (PR_ID);
alter table LF_PRO_CON
  add constraint FK_LF_PRO_C_FK_LF_PRO_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID);
create index FK_LF_PRO_RELATIONS_LF_PRO_FK on LF_PRO_CON (PR_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_P_N_UP
-- ========================
--
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
  );
alter table LF_P_N_UP
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
  );

--
-- Creating table LF_RECONCILIATION
-- ================================
--
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
  );
alter table LF_RECONCILIATION
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
  );

--
-- Creating table LF_REPLY
-- =======================
--
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
  );
alter table LF_REPLY
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
  );
alter table LF_REPLY
  add constraint FK_LF_REPLY_LF_PRO_RE_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID);
create index LF_PRO_REPLY_FK on LF_REPLY (PR_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_RESOURCE
-- ==========================
--
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
  );
alter table LF_RESOURCE
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
  );

--
-- Creating table LF_REVIEWER
-- ==========================
--
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
  );
alter table LF_REVIEWER
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
  );
alter table LF_REVIEWER
  add constraint FK_LF_REVIE_LF_REVIEW_LF_FLOW foreign key (F_ID)
  references LF_FLOW (F_ID);
create index LF_REVIEWER_FK on LF_REVIEWER (F_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_REVIEWER_FK2 on LF_REVIEWER (MT_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_REVIEWER2LEVEL
-- ================================
--
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
  );
alter table LF_REVIEWER2LEVEL
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
  );
alter table LF_REVIEWER2LEVEL
  add constraint FK_LF_REVIE_FK_LF_REV_LF_FLOW foreign key (F_ID)
  references LF_FLOW (F_ID);
create index FK_LF_REV_RELATIONS_LF_FLO_FK on LF_REVIEWER2LEVEL (F_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index FK_LF_REV_RELATIONS_LF_SYS_FK on LF_REVIEWER2LEVEL (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_REVIEWSWITCH
-- ==============================
--
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
  );

--
-- Creating table LF_SENDGRO
-- =========================
--
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
  );
alter table LF_SENDGRO
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
  );
alter table LF_SENDGRO
  add constraint FK_LF_SENDG_LF_SER2GR_LF_SERVI foreign key (SER_ID)
  references LF_SERVICE (SER_ID);
create index LF_SER2GRO_FK on LF_SENDGRO (SER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_SERMSG_DETAIL
-- ===============================
--
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
  );
alter table LF_SERMSG_DETAIL
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
  );
create index LF_SL_RELLATION_SMD_FK on LF_SERMSG_DETAIL (SL_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_SERVICELOG
-- ============================
--
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
  );
alter table LF_SERVICELOG
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
  );
alter table LF_SERVICELOG
  add constraint FK_LF_SERVICELOG_FK_SERVICE foreign key (SER_ID)
  references LF_SERVICE (SER_ID);
create index FK_LF_SERVI_RELATIONS_LF_SERVI on LF_SERVICELOG (SER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_SERVICEMSG
-- ============================
--
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
  );
alter table LF_SERVICEMSG
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
  );
alter table LF_SERVICEMSG
  add constraint FK_LF_SERVICEMSG_2_LF_SENDGRO foreign key (SG_ID)
  references LF_SENDGRO (SG_ID);
alter table LF_SERVICEMSG
  add constraint FK_LF_SERVI_LE_SER2MS_LF_SERVI foreign key (SER_ID)
  references LF_SERVICE (SER_ID);
create index LE_SER2MSG_FK on LF_SERVICEMSG (SER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_SKIN
-- ======================
--
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
  );

--
-- Creating table LF_SPEUICFG
-- ==========================
--
create table LF_SPEUICFG
(
  COMPANYNAME VARCHAR2(20),
  COMPANYLOGO VARCHAR2(512),
  BGIMG       VARCHAR2(512),
  LOGINLOGO   VARCHAR2(512),
  DISPCONTENT VARCHAR2(16) default '11100000' not null,
  DISPLAYTYPE NUMBER(3) default 1 not null,
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
  );

--
-- Creating table LF_SPFEE
-- =======================
--
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
  SP_RESULT       VARCHAR2(32)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_SPFEE
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
  );

--
-- Creating table LF_SP_DEP_BIND
-- =============================
--
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
  );
alter table LF_SP_DEP_BIND
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
  );

--
-- Creating table LF_SQLWHERE
-- ==========================
--
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
  );
alter table LF_SQLWHERE
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
  );
alter table LF_SQLWHERE
  add constraint FK_LF_SQLWH_FK_LF_SQL_LF_PROCE foreign key (PR_ID)
  references LF_PROCESS (PR_ID);
create index FK_LF_SQL_RELATIONS_LF_PRO_FK on LF_SQLWHERE (PR_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_SUBNOALLOT
-- ============================
--
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
  ISVALID          NUMBER(2) default 1
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_SUBNOALLOT
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
  );

--
-- Creating table LF_SUBNOALLOT_DETAIL
-- ===================================
--
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
  ISVALID         NUMBER(2) default 1
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_SUBNOALLOT_DETAIL
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
  );

--
-- Creating table LF_SURVEY
-- ========================
--
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
  MSG_TIME    TIMESTAMP(8) default sysdate,
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
  );
alter table LF_SURVEY
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
  );

--
-- Creating table LF_SURVEYTASK
-- ============================
--
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
  );

--
-- Creating table LF_SYSMTTASK
-- ===========================
--
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
  );
alter table LF_SYSMTTASK
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
  );

--
-- Creating table LF_SYS_PARAM
-- ===========================
--
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
  );
alter table LF_SYS_PARAM
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
  );
alter table LF_SYS_PARAM
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
  );

--
-- Creating table LF_TAOCAN_CMD
-- ============================
--
create table LF_TAOCAN_CMD
(
  ID           NUMBER(38) not null,
  STRUCTCODE   VARCHAR2(20) not null,
  TAOCAN_CODE  VARCHAR2(64),
  TAOCAN_TYPE  NUMBER(2),
  TAOCAN_MONEY NUMBER(20),
  STRUCT_TYPE  NUMBER(2) not null,
  CREATE_TIME  TIMESTAMP(8) default SYSDATE,
  UPDATE_TIME  TIMESTAMP(8) default SYSDATE,
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
  );
alter table LF_TAOCAN_CMD
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
  );

--
-- Creating table LF_TASK
-- ======================
--
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
  SUBMIT_TIME TIMESTAMP(8) default sysdate,
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
  );
alter table LF_TASK
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
  );
alter table LF_TASK
  add constraint FK_LF_TASK_LF_MTMSG__LF_MTTAS foreign key (MT_ID)
  references LF_MTTASK (MT_ID);
create index IX_LF_TASK_USERID on LF_TASK (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_MTMSG_TASK_FK on LF_TASK (MT_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_SERVICE_TASK_FK on LF_TASK (SL_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_TASKREPORT
-- ============================
--
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
  );

--
-- Creating table LF_TEMP
-- ======================
--
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
  );

--
-- Creating table LF_TEMPLATE
-- ==========================
--
create table LF_TEMPLATE
(
  TM_ID        NUMBER(38) not null,
  USER_ID      NUMBER(38),
  TM_NAME      VARCHAR2(64),
  TM_MSG       VARCHAR2(2000),
  DSFLAG       NUMBER(1),
  TM_STATE     NUMBER(1),
  ADDTIME      DATE,
  ISPASS       NUMBER(1),
  TMP_TYPE     NUMBER(1),
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
  TM_CODE      VARCHAR2(64)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table LF_TEMPLATE
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
  );
alter table LF_TEMPLATE
  add constraint FK_LF_TEMPL_LF_ADDTEM_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);
create index LF_ADDTEMPLETE_FK on LF_TEMPLATE (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_THEME
-- =======================
--
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
  );

--
-- Creating table LF_THIR_MENUCONTROL
-- ==================================
--
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
  );
alter table LF_THIR_MENUCONTROL
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
  );

--
-- Creating table LF_TIMER
-- =======================
--
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
  );
alter table LF_TIMER
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
  );

--
-- Creating table LF_TIMER_HISTORY
-- ===============================
--
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
  );
alter table LF_TIMER_HISTORY
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
  );

--
-- Creating table LF_TMPLRELA
-- ==========================
--
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
  );
alter table LF_TMPLRELA
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
  );

--
-- Creating table LF_TRUCTTYPE
-- ===========================
--
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
  );

--
-- Creating table LF_USER2CLIENT
-- =============================
--
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
  );
alter table LF_USER2CLIENT
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
  );
alter table LF_USER2CLIENT
  add constraint FK_LF_USER2_LF_USER2C_LF_CLIEN foreign key (CLIENT_ID)
  references LF_CLIENT (CLIENT_ID);
alter table LF_USER2CLIENT
  add constraint FK_LF_USER2_LF_USER2C_LF_DEP foreign key (DEP_ID)
  references LF_DEP (DEP_ID);
alter table LF_USER2CLIENT
  add constraint FK_LF_USER2_LF_USER2C_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);
create index LF_USER2CLIENT_FK on LF_USER2CLIENT (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_USER2CLIENT_FK2 on LF_USER2CLIENT (CLIENT_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_USER2CLIENT_FK3 on LF_USER2CLIENT (DEP_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_USER2ROLE
-- ===========================
--
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
  );
alter table LF_USER2ROLE
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
  );
alter table LF_USER2ROLE
  add constraint FK_LF_USER2_LF_USER2R_LF_ROLES foreign key (ROLE_ID)
  references LF_ROLES (ROLE_ID);
alter table LF_USER2ROLE
  add constraint FK_LF_USER2_LF_USER2R_LF_SYSUS foreign key (USER_ID)
  references LF_SYSUSER (USER_ID);
create index LF_USER2ROLE_FK on LF_USER2ROLE (ROLE_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_USER2ROLE_FK2 on LF_USER2ROLE (USER_ID)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_USER2SKIN
-- ===========================
--
create table LF_USER2SKIN
(
  USID       NUMBER(38) not null,
  SID        NUMBER(38),
  USERID     NUMBER(38),
  SKIN_CODE  VARCHAR2(32),
  MONVOICE   NUMBER(20) default 0 not null,
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
  );

--
-- Creating table LF_USERPARA
-- ==========================
--
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
  );
alter table LF_USERPARA
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
  );
alter table LF_USERPARA
  add constraint FK_LF_USERPARA_LF_SERVICE foreign key (SER_ID)
  references LF_SERVICE (SER_ID);

--
-- Creating table LF_USER_RPT
-- ==========================
--
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
  );
alter table LF_USER_RPT
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
  );

--
-- Creating table LF_USER_SETTING
-- ==============================
--
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
  );

--
-- Creating table LF_VERSION_EMPDB
-- ===============================
--
create table LF_VERSION_EMPDB
(
  DVID           NUMBER(38) not null,
  EMP_DBVERSION  VARCHAR2(32) not null,
  GATE_DBVERSION VARCHAR2(32) not null,
  UPDATETIME     TIMESTAMP(6) default SYSDATE not null,
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
  );
alter table LF_VERSION_EMPDB
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
  );

--
-- Creating table LF_VODINFO
-- =========================
--
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
  );
alter table LF_VODINFO
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
  );

--
-- Creating table LF_VODMOREC
-- ==========================
--
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
  );
alter table LF_VODMOREC
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
  );

--
-- Creating table LF_VODMTREC
-- ==========================
--
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
  );
alter table LF_VODMTREC
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
  );

--
-- Creating table LF_WC_ACCOUNT
-- ============================
--
create table LF_WC_ACCOUNT
(
  A_ID         NUMBER(38) not null,
  FAKE_ID      VARCHAR2(32),
  NAME         VARCHAR2(64),
  CODE         VARCHAR2(25) not null,
  OPEN_ID      VARCHAR2(64) not null,
  IMG          VARCHAR2(128),
  TYPE         NUMBER(2),
  IS_APPROVE   NUMBER(2) default 0,
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
  );
alter table LF_WC_ACCOUNT
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
  );

--
-- Creating table LF_WC_ALINK
-- ==========================
--
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
  );

--
-- Creating table LF_WC_KEYWORD
-- ============================
--
create table LF_WC_KEYWORD
(
  K_ID       NUMBER(38) not null,
  NAME       VARCHAR2(32) not null,
  TYPE       NUMBER(2) default 0,
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
  );
alter table LF_WC_KEYWORD
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
  );

--
-- Creating table LF_WC_MENU
-- =========================
--
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
  );
alter table LF_WC_MENU
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
  );

--
-- Creating table LF_WC_MSG
-- ========================
--
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
  );
alter table LF_WC_MSG
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
  );

--
-- Creating table LF_WC_REVENT
-- ===========================
--
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
  );
alter table LF_WC_REVENT
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
  );

--
-- Creating table LF_WC_RIMG
-- =========================
--
create table LF_WC_RIMG
(
  RIMG_ID     NUMBER(38) default 1 not null,
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
  );
alter table LF_WC_RIMG
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
  );

--
-- Creating table LF_WC_RTEXT
-- ==========================
--
create table LF_WC_RTEXT
(
  TET_ID     NUMBER(38) not null,
  TET_TYPE   NUMBER(2) default 0,
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
  );
alter table LF_WC_RTEXT
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
  );

--
-- Creating table LF_WC_TEMPLATE
-- =============================
--
create table LF_WC_TEMPLATE
(
  T_ID        NUMBER(38) not null,
  T_NAME      VARCHAR2(64) not null,
  MSG_TYPE    NUMBER(2),
  MSG_XML     VARCHAR2(4000),
  MSG_TEXT    VARCHAR2(4000),
  IS_PUBLIC   NUMBER(2) default 0,
  IS_DRAFT    NUMBER(2) default 0,
  IS_DYNAMIC  NUMBER(2) default 0,
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
  );
alter table LF_WC_TEMPLATE
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
  );

--
-- Creating table LF_WC_TLINK
-- ==========================
--
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
  );
alter table LF_WC_TLINK
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
  );

--
-- Creating table LF_WC_ULINK
-- ==========================
--
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
  );
alter table LF_WC_ULINK
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
  );

--
-- Creating table LF_WC_USERINFO
-- =============================
--
create table LF_WC_USERINFO
(
  WC_ID      NUMBER(38) not null,
  FAKE_ID    VARCHAR2(32),
  NAME       VARCHAR2(20),
  CODE       VARCHAR2(15),
  IMG        VARCHAR2(128),
  TYPE       NUMBER(2) default 0,
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
  );
alter table LF_WC_USERINFO
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
  );

--
-- Creating table LF_WC_VERIFY
-- ===========================
--
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
  );
alter table LF_WC_VERIFY
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
  );

--
-- Creating table LF_WEIX_EMOJI
-- ============================
--
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
  );
alter table LF_WEIX_EMOJI
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
  );

--
-- Creating table LF_WGPARAMCONFIG
-- ===============================
--
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
  );
alter table LF_WGPARAMCONFIG
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
  );
create index LF_WGPARAMCONFIG_I_PARAMSUBNUM on LF_WGPARAMCONFIG (PARAMSUBNUM)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index LF_WGPARAMCONFIG_I_PARAMVALUE on LF_WGPARAMCONFIG (PARAMVALUE)
  tablespace EMP_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table LF_WGPARAMDEFINITION
-- ===================================
--
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
  );
alter table LF_WGPARAMDEFINITION
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
  );

--
-- Creating table LF_WX_BASEINFO
-- =============================
--
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
  OPERAPPSTATUS  NUMBER(20) default 0,
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
  );
alter table LF_WX_BASEINFO
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
  );

--
-- Creating table LF_WX_DATA
-- =========================
--
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
  );
alter table LF_WX_DATA
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
  );

--
-- Creating table LF_WX_DATATYPE
-- =============================
--
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
  );
alter table LF_WX_DATATYPE
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
  );

--
-- Creating table LF_WX_DATA_BIND
-- ==============================
--
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
  );

--
-- Creating table LF_WX_DATA_CHOS
-- ==============================
--
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
  );

--
-- Creating table LF_WX_PAGE
-- =========================
--
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
  );
alter table LF_WX_PAGE
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
  );

--
-- Creating table LF_WX_SENDCOUNT
-- ==============================
--
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
  );
alter table LF_WX_SENDCOUNT
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
  );

--
-- Creating table LF_WX_SORT
-- =========================
--
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
  );
alter table LF_WX_SORT
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
  );

--
-- Creating table LF_WX_UPLOADFILE
-- ===============================
--
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
  );

--
-- Creating table LF_WX_USERFEE
-- ============================
--
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
  );

--
-- Creating table LF_WX_VISITLOG
-- =============================
--
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
  );

--
-- Creating table OT_LBS_PIOS
-- ==========================
--
create table OT_LBS_PIOS
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
  );
alter table OT_LBS_PIOS
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
  );

--
-- Creating table OT_LBS_PUSHSET
-- =============================
--
create table OT_LBS_PUSHSET
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
  );
alter table OT_LBS_PUSHSET
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
  );

--
-- Creating table OT_LBS_USERPIOS
-- ==============================
--
create table OT_LBS_USERPIOS
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
  );
alter table OT_LBS_USERPIOS
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
  );

--
-- Creating table OT_ONL_GPMEM
-- ===========================
--
create table OT_ONL_GPMEM
(
  GP_ID    NUMBER(38) not null,
  GM_USER  NUMBER(38),
  GM_STATE NUMBER(3) default (1)	--是否有效的状态1-有效，0-失效
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table OT_ONL_GPMSGID
-- =============================
--
create table OT_ONL_GPMSGID
(
  GM_USER     NUMBER(38),
  MSG_ID      NUMBER(38) default (0),
  UPDATE_TIME TIMESTAMP(8) default sysdate -- 更新时间
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

--
-- Creating table OT_ONL_GROUP
-- ===========================
--
create table OT_ONL_GROUP
(
  GP_ID       NUMBER(38) not null,
  GP_NAME     VARCHAR2(32),
  CREATE_USER NUMBER(38),
  CREATE_TIME TIMESTAMP(8),
  MEM_COUNT   NUMBER(10) default 0				--群组人数
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table OT_ONL_GROUP
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
  );

--
-- Creating table OT_ONL_MSG
-- =========================
--
create table OT_ONL_MSG
(
  M_ID       NUMBER(38) not null,
  A_ID       NUMBER(38),
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SERVER_NUM VARCHAR2(32),
  MESSAGE    VARCHAR2(2048),
  SEND_TIME  TIMESTAMP(8),
  MSG_TYPE   VARCHAR2(10),
  PUSH_TYPE  NUMBER(3) default 0				--11-手机to客服，2-客服to手机，3-客服to客服，4群组
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table OT_ONL_MSG
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
  );

--
-- Creating table OT_ONL_MSG_HIS
-- =============================
--
create table OT_ONL_MSG_HIS
(
  M_ID       NUMBER(38) not null,
  A_ID       NUMBER(38),
  FROM_USER  VARCHAR2(32),
  TO_USER    VARCHAR2(32),
  SERVER_NUM VARCHAR2(32),
  MESSAGE    VARCHAR2(2048),
  SEND_TIME  TIMESTAMP(8),
  MSG_TYPE   VARCHAR2(10),
  PUSH_TYPE  NUMBER(3) default 0				--11-手机to客服，2-客服to手机，3-客服to客服，4群组
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table OT_ONL_MSG_HIS
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
  );

--
-- Creating table OT_ONL_REMARK
-- ============================
--
create table OT_ONL_REMARK
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
  );

--
-- Creating table OT_ONL_SERVER
-- ============================
--
create table OT_ONL_SERVER
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
  );
alter table OT_ONL_SERVER
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
  );

--
-- Creating table OT_SIT_INFO
-- ==========================
--
create table OT_SIT_INFO
(
  S_ID       NUMBER(38) not null,
  TYPE_ID    NUMBER(38),
  NAME       VARCHAR2(32),
  URL        VARCHAR2(128),
  IS_SYSTEM  NUMBER(3) default 0,
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
  );
alter table OT_SIT_INFO
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
  );

--
-- Creating table OT_SIT_PAGE
-- ==========================
--
create table OT_SIT_PAGE
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
  );
alter table OT_SIT_PAGE
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
  );

--
-- Creating table OT_SIT_PLANT
-- ===========================
--
create table OT_SIT_PLANT
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
  );
alter table OT_SIT_PLANT
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
  );

--
-- Creating table OT_SIT_TYPE
-- ==========================
--
create table OT_SIT_TYPE
(
  TYPE_ID    NUMBER(38) not null,
  NAME       VARCHAR2(32),
  IS_DEFAULT NUMBER(3),
  PATTERN    NUMBER(10) default 0,
  IMG_URL    VARCHAR2(64),
  IMG_URL0   VARCHAR2(64),
  IMG_URL1   VARCHAR2(64),
  IMG_URL2   VARCHAR2(64),
  IMG_URL3   VARCHAR2(64),
  IS_SYSTEM  NUMBER(3) default 0,
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
  );
alter table OT_SIT_TYPE
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
  );

--
-- Creating table OT_SYS_GLOBAL_VARIABLE
-- =====================================
--
create table OT_SYS_GLOBAL_VARIABLE
(
  GLOBALID    NUMBER(38) not null,
  GLOBALKEY   VARCHAR2(32) not null,
  GLOBALVALUE NUMBER(38),
  MEMO        VARCHAR2(50)
)
tablespace EMP_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table OT_SYS_GLOBAL_VARIABLE
  add primary key (GLOBALID)
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
  );

--
-- Creating table OT_WEI_ACCOUNT
-- =============================
--
create table OT_WEI_ACCOUNT
(
  A_ID         NUMBER(38) not null,
  FAKE_ID      VARCHAR2(32),
  NAME         VARCHAR2(64),
  CODE         VARCHAR2(25) not null,
  OPEN_ID      VARCHAR2(64) not null,
  IMG          VARCHAR2(128),
  TYPE         NUMBER(3),
  IS_APPROVE   NUMBER(3) default 0,
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
  SYNC_STATE   NUMBER(3) default 0,
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
  );
alter table OT_WEI_ACCOUNT
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
  );

--
-- Creating table OT_WEI_COUNT
-- ===========================
--
create table OT_WEI_COUNT
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
  );
alter table OT_WEI_COUNT
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
  );

--
-- Creating table OT_WEI_GROUP
-- ===========================
--
create table OT_WEI_GROUP
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
  );
alter table OT_WEI_GROUP
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
  );

--
-- Creating table OT_WEI_KEYWORD
-- =============================
--
create table OT_WEI_KEYWORD
(
  K_ID       NUMBER(38) not null,
  NAME       VARCHAR2(32) not null,
  TYPE       NUMBER(3) default 0,
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
  );
alter table OT_WEI_KEYWORD
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
  );

--
-- Creating table OT_WEI_MENU
-- ==========================
--
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
  );

--
-- Creating table OT_WEI_MSG
-- =========================
--
create table OT_WEI_MSG
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
  );
alter table OT_WEI_MSG
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
  );

--
-- Creating table OT_WEI_REVENT
-- ============================
--
create table OT_WEI_REVENT
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
  );
alter table OT_WEI_REVENT
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
  );

--
-- Creating table OT_WEI_RIMG
-- ==========================
--
create table OT_WEI_RIMG
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
  );
alter table OT_WEI_RIMG
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
  );

--
-- Creating table OT_WEI_RTEXT
-- ===========================
--
create table OT_WEI_RTEXT
(
  TET_ID     NUMBER(38) not null,
  TET_TYPE   NUMBER(3) default 0,
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
  );
alter table OT_WEI_RTEXT
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
  );

--
-- Creating table OT_WEI_SENDLOG
-- =============================
--
create table OT_WEI_SENDLOG
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
  );
alter table OT_WEI_SENDLOG
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
  );

--
-- Creating table OT_WEI_TEMPLATE
-- ==============================
--
create table OT_WEI_TEMPLATE
(
  T_ID        NUMBER(38) not null,
  T_NAME      VARCHAR2(64) not null,
  MSG_TYPE    NUMBER(3),
  MSG_XML     VARCHAR2(4000),
  MSG_TEXT    VARCHAR2(4000),
  IS_PUBLIC   NUMBER(3) default 0,
  IS_DRAFT    NUMBER(3) default 0,
  IS_DYNAMIC  NUMBER(3) default 0,
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
  );
alter table OT_WEI_TEMPLATE
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
  );

--
-- Creating table OT_WEI_TLINK
-- ===========================
--
create table OT_WEI_TLINK
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
  );
alter table OT_WEI_TLINK
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
  );

--
-- Creating table OT_WEI_ULINK
-- ===========================
--
create table OT_WEI_ULINK
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
  );
alter table OT_WEI_ULINK
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
  );

--
-- Creating table OT_WEI_USER2ACC
-- ==============================
--
create table OT_WEI_USER2ACC
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
  );

--
-- Creating table OT_WEI_USERINFO
-- ==============================
--
create table OT_WEI_USERINFO
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
  );
alter table OT_WEI_USERINFO
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
  );


--
-- Creating sequence LF_EMPLOYEE_TYPE_SEQUENCE
-- ===========================================
--
create sequence LF_EMPLOYEE_TYPE_SEQUENCE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;



--
-- Creating sequence S_LF_ACCOUNT_BIND
-- ===================================
--
create sequence S_LF_ACCOUNT_BIND
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_ADDRBOOKS
-- ================================
--
create sequence S_LF_ADDRBOOKS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_ALSMSRECORD
-- ==================================
--
create sequence S_LF_ALSMSRECORD
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APPMAINPAGEHIS
-- =====================================
--
create sequence S_LF_APPMAINPAGEHIS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_CLIDOWLOAD
-- =====================================
--
create sequence S_LF_APP_CLIDOWLOAD
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_FOMEITEM
-- ===================================
--
create sequence S_LF_APP_FOMEITEM
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_FOMEVALVE
-- ====================================
--
create sequence S_LF_APP_FOMEVALVE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_FOM_TYPE
-- ===================================
--
create sequence S_LF_APP_FOM_TYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_LBS_PIOS
-- ===================================
--
create sequence S_LF_APP_LBS_PIOS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_LBS_PUSH
-- ===================================
--
create sequence S_LF_APP_LBS_PUSH
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_LBS_USER
-- ===================================
--
create sequence S_LF_APP_LBS_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_MSGCONTENT
-- =====================================
--
create sequence S_LF_APP_MSGCONTENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_MTMSG
-- ================================
--
create sequence S_LF_APP_MTMSG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_MTTASK
-- =================================
--
create sequence S_LF_APP_MTTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_MW_CLIENT
-- ====================================
--
create sequence S_LF_APP_MW_CLIENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_OL_MSGHIS
-- ====================================
--
create sequence S_LF_APP_OL_MSGHIS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_REVENT
-- =================================
--
create sequence S_LF_APP_REVENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_RTEXT
-- ================================
--
create sequence S_LF_APP_RTEXT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_SIT_PLANT
-- ====================================
--
create sequence S_LF_APP_SIT_PLANT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_SIT_TYPE
-- ===================================
--
create sequence S_LF_APP_SIT_TYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_TC_MOMSG
-- ===================================
--
create sequence S_LF_APP_TC_MOMSG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_APP_VERIFY
-- =================================
--
create sequence S_LF_APP_VERIFY
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_AUTOREPLY
-- ================================
--
create sequence S_LF_AUTOREPLY
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BILLLOG
-- ==============================
--
create sequence S_LF_BILLLOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BINDFLOW
-- ===============================
--
create sequence S_LF_BINDFLOW
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BIRTHDAYMEMBER
-- =====================================
--
create sequence S_LF_BIRTHDAYMEMBER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BIRTHDAYSETUP
-- ====================================
--
create sequence S_LF_BIRTHDAYSETUP
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BLACKLIST
-- ================================
--
create sequence S_LF_BLACKLIST
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUSINESS
-- ===============================
--
create sequence S_LF_BUSINESS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUSMANAGER
-- =================================
--
create sequence S_LF_BUSMANAGER
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUSPKGETAOCAN
-- ====================================
--
create sequence S_LF_BUSPKGETAOCAN
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUSTAIL
-- ==============================
--
create sequence S_LF_BUSTAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUS_PACKAGE
-- ==================================
--
create sequence S_LF_BUS_PACKAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUS_PROCESS
-- ==================================
--
create sequence S_LF_BUS_PROCESS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUS_TAILTMP
-- ==================================
--
create sequence S_LF_BUS_TAILTMP
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_BUS_TAOCAN
-- =================================
--
create sequence S_LF_BUS_TAOCAN
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_CLIDEP_CONN
-- ==================================
--
create sequence S_LF_CLIDEP_CONN
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_CLIENT
-- =============================
--
create sequence S_LF_CLIENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_CLIENTCLASS
-- ==================================
--
create sequence S_LF_CLIENTCLASS
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_CLIENT_DEP
-- =================================
--
create sequence S_LF_CLIENT_DEP
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_CONTRACT
-- ===============================
--
create sequence S_LF_CONTRACT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_CONTRACT_TAOCAN
-- ======================================
--
create sequence S_LF_CONTRACT_TAOCAN
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_CORP
-- ===========================
--
create sequence S_LF_CORP
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_CORP_CONF
-- ================================
--
create sequence S_LF_CORP_CONF
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_CUSTFIELD
-- ================================
--
create sequence S_LF_CUSTFIELD
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_CUSTFIELD_VALUE
-- ======================================
--
create sequence S_LF_CUSTFIELD_VALUE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_DBCONNECT
-- ================================
--
create sequence S_LF_DBCONNECT
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_DEDUCTIONS_DISP
-- ======================================
--
create sequence S_LF_DEDUCTIONS_DISP
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_DEDUCTIONS_LIST
-- ======================================
--
create sequence S_LF_DEDUCTIONS_LIST
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_DEP
-- ==========================
--
create sequence S_LF_DEP
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

--
-- Creating sequence S_LF_DEPPWDRECEIVER
-- =====================================
--
create sequence S_LF_DEPPWDRECEIVER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_DEP_RECHARGE_LOG
-- =======================================
--
create sequence S_LF_DEP_RECHARGE_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_DEP_TAOCAN
-- =================================
--
create sequence S_LF_DEP_TAOCAN
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_DEP_USER_BALANCE
-- =======================================
--
create sequence S_LF_DEP_USER_BALANCE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_DFADVANCED
-- =================================
--
create sequence S_LF_DFADVANCED
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_EDIT
-- ===========================
--
create sequence S_LF_EDIT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_EMPDEP_CONN
-- ==================================
--
create sequence S_LF_EMPDEP_CONN
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_EMPLOYEE
-- ===============================
--
create sequence S_LF_EMPLOYEE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_EMPLOYEE_DEP
-- ===================================
--
create sequence S_LF_EMPLOYEE_DEP
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_ENTERPRISE
-- =================================
--
create sequence S_LF_ENTERPRISE
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_EXAMINE_SMS
-- ==================================
--
create sequence S_LF_EXAMINE_SMS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_EXTERNAL_USER
-- ====================================
--
create sequence S_LF_EXTERNAL_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_FLOW
-- ===========================
--
create sequence S_LF_FLOW
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_FLOWBINDOBJ
-- ==================================
--
create sequence S_LF_FLOWBINDOBJ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_FLOWBINDTYPE
-- ===================================
--
create sequence S_LF_FLOWBINDTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_FLOWRECORD
-- =================================
--
create sequence S_LF_FLOWRECORD
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_GLOBAL_VARIABLE
-- ======================================
--
create sequence S_LF_GLOBAL_VARIABLE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_CLIENT
-- ================================
--
create sequence S_LF_IM_CLIENT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_DIALOG
-- ================================
--
create sequence S_LF_IM_DIALOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_GPUSERLINK
-- ====================================
--
create sequence S_LF_IM_GPUSERLINK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_GROUP
-- ===============================
--
create sequence S_LF_IM_GROUP
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_GROUPMESSAGE
-- ======================================
--
create sequence S_LF_IM_GROUPMESSAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_HISTORY
-- =================================
--
create sequence S_LF_IM_HISTORY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_MEMBERGROUP
-- =====================================
--
create sequence S_LF_IM_MEMBERGROUP
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_OFFICEGROUP
-- =====================================
--
create sequence S_LF_IM_OFFICEGROUP
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_RECENTCONTACTS
-- ========================================
--
create sequence S_LF_IM_RECENTCONTACTS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_SEATGROUP
-- ===================================
--
create sequence S_LF_IM_SEATGROUP
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_SEATLIMIT
-- ===================================
--
create sequence S_LF_IM_SEATLIMIT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_SEATUSER
-- ==================================
--
create sequence S_LF_IM_SEATUSER
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_SEAT_CGLINK
-- =====================================
--
create sequence S_LF_IM_SEAT_CGLINK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_SEAT_CLIENT
-- =====================================
--
create sequence S_LF_IM_SEAT_CLIENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_SEAT_CLIENTGP
-- =======================================
--
create sequence S_LF_IM_SEAT_CLIENTGP
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_USERMESSAGE
-- =====================================
--
create sequence S_LF_IM_USERMESSAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_IM_USERSUBNOEMP_TABLESPACE===================================
--
create sequence S_LF_IM_USERSUBNO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_KEYWORDS
-- ===============================
--
create sequence S_LF_KEYWORDS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_KEY_VALUE
-- ================================
--
create sequence S_LF_KEY_VALUE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_LIST2GRO
-- ===============================
--
create sequence S_LF_LIST2GRO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MACIP
-- ============================
--
create sequence S_LF_MACIP
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MALIST
-- =============================
--
create sequence S_LF_MALIST
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MAPPING
-- ==============================
--
create sequence S_LF_MAPPING
minvalue 1
maxvalue 999999999999999999999999999
start with 521
increment by 1
cache 20;

--
-- Creating sequence S_LF_MATERIAL
-- ===============================
--
create sequence S_LF_MATERIAL
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MATERIAL_SORT
-- ====================================
--
create sequence S_LF_MATERIAL_SORT
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_MMSACCBIND
-- =================================
--
create sequence S_LF_MMSACCBIND
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MMSACCMANAGEMENT
-- =======================================
--
create sequence S_LF_MMSACCMANAGEMENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MMSBLIST
-- ===============================
--
create sequence S_LF_MMSBLIST
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MMSINFO
-- ==============================
--
create sequence S_LF_MMSINFO
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MMSMTTASK
-- ================================
--
create sequence S_LF_MMSMTTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MMSTEMPLATE
-- ==================================
--
create sequence S_LF_MMSTEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MMS_MTREPORT
-- ===================================
--
create sequence S_LF_MMS_MTREPORT
minvalue 1
maxvalue 999999999999999999999999999
start with 4
increment by 1
cache 20;

--
-- Creating sequence S_LF_MOBFINANCIAL
-- ===================================
--
create sequence S_LF_MOBFINANCIAL
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_DGTACINFO
-- ====================================
--
create sequence S_LF_MON_DGTACINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_DHOST
-- ================================
--
create sequence S_LF_MON_DHOST
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_DPROCE
-- =================================
--
create sequence S_LF_MON_DPROCE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_DSPACINFO
-- ====================================
--
create sequence S_LF_MON_DSPACINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_ERR
-- ==============================
--
create sequence S_LF_MON_ERR
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_ERRHIS
-- =================================
--
create sequence S_LF_MON_ERRHIS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_ONLCFG
-- =================================
--
create sequence S_LF_MON_ONLCFG
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_SHOST
-- ================================
--
create sequence S_LF_MON_SHOST
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_SPGATEBUF
-- ====================================
--
create sequence S_LF_MON_SPGATEBUF
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_SPROCE
-- =================================
--
create sequence S_LF_MON_SPROCE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MON_WBSBUF
-- =================================
--
create sequence S_LF_MON_WBSBUF
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MOTASK
-- =============================
--
create sequence S_LF_MOTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MO_SERVICE
-- =================================
--
create sequence S_LF_MO_SERVICE
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MTRPT_EC
-- ===============================
--
create sequence S_LF_MTRPT_EC
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_MTTASK
-- =============================
--
create sequence S_LF_MTTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_NOTICE
-- =============================
--
create sequence S_LF_NOTICE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_ONLINE_USER
-- ==================================
--
create sequence S_LF_ONLINE_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_OPERATE
-- ==============================
--
create sequence S_LF_OPERATE
minvalue 1
maxvalue 999999999999999999999999999
start with 32
increment by 1
cache 20;

--
-- Creating sequence S_LF_OPRATELOG
-- ================================
--
create sequence S_LF_OPRATELOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PARAM
-- ============================
--
create sequence S_LF_PARAM
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PERFECT_NOTIC
-- ====================================
--
create sequence S_LF_PERFECT_NOTIC
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PERSONALCONFIG
-- =====================================
--
create sequence S_LF_PERSONALCONFIG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_POSITION
-- ===============================
--
create sequence S_LF_POSITION
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PRIVILEGE
-- ================================
--
create sequence S_LF_PRIVILEGE
minvalue 1
maxvalue 999999999999999999999999999
start with 256
increment by 1
cache 20;

--
-- Creating sequence S_LF_PROCESS
-- ==============================
--
create sequence S_LF_PROCESS
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_PROCHARGES
-- =================================
--
create sequence S_LF_PROCHARGES
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PRODUCTOR_CONT
-- =====================================
--
create sequence S_LF_PRODUCTOR_CONT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PRODUCTS
-- ===============================
--
create sequence S_LF_PRODUCTS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PROSENDCOUNT
-- ===================================
--
create sequence S_LF_PROSENDCOUNT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_PRO_CON
-- ==============================
--
create sequence S_LF_PRO_CON
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_P_N_UP
-- =============================
--
create sequence S_LF_P_N_UP
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_RECONCILIATION
-- =====================================
--
create sequence S_LF_RECONCILIATION
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_REPLY
-- ============================
--
create sequence S_LF_REPLY
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_RESOURCE
-- ===============================
--
create sequence S_LF_RESOURCE
minvalue 1
maxvalue 999999999999999999999999999
start with 18
increment by 1
cache 20;

--
-- Creating sequence S_LF_REVIEWER2LEVEL
-- =====================================
--
create sequence S_LF_REVIEWER2LEVEL
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_REVIEWSWITCH
-- ===================================
--
create sequence S_LF_REVIEWSWITCH
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_ROLES
-- ============================
--
create sequence S_LF_ROLES
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

--
-- Creating sequence S_LF_SENDGRO
-- ==============================
--
create sequence S_LF_SENDGRO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SERVICE
-- ==============================
--
create sequence S_LF_SERVICE
minvalue 1
maxvalue 999999999999999999999999999
start with 24
increment by 1
cache 20;

--
-- Creating sequence S_LF_SERVICELOG
-- =================================
--
create sequence S_LF_SERVICELOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SERVICEMSG
-- =================================
--
create sequence S_LF_SERVICEMSG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SKIN
-- ===========================
--
create sequence S_LF_SKIN
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_SPFEE
-- ============================
--
create sequence S_LF_SPFEE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SP_DEP_BIND
-- ==================================
--
create sequence S_LF_SP_DEP_BIND
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SQLWHERE
-- ===============================
--
create sequence S_LF_SQLWHERE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SUBNOALLOT
-- =================================
--
create sequence S_LF_SUBNOALLOT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SUBNOALLOT_DETAIL
-- ========================================
--
create sequence S_LF_SUBNOALLOT_DETAIL
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SURVEY
-- =============================
--
create sequence S_LF_SURVEY
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SURVEYTASK
-- =================================
--
create sequence S_LF_SURVEYTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SYSMTTASK
-- ================================
--
create sequence S_LF_SYSMTTASK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_SYSUSER
-- ==============================
--
create sequence S_LF_SYSUSER
minvalue 1
maxvalue 999999999999999999999999999
start with 22
increment by 1
cache 20;

--
-- Creating sequence S_LF_SYS_PARAM
-- ================================
--
create sequence S_LF_SYS_PARAM
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_TAOCAN_CMD
-- =================================
--
create sequence S_LF_TAOCAN_CMD
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_TASK
-- ===========================
--
create sequence S_LF_TASK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_TEMPLATE
-- ===============================
--
create sequence S_LF_TEMPLATE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_THEME
-- ============================
--
create sequence S_LF_THEME
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_THIR_MENUCONTROL
-- =======================================
--
create sequence S_LF_THIR_MENUCONTROL
minvalue 1
maxvalue 999999999999999999999999999
start with 101
increment by 1
cache 20;

--
-- Creating sequence S_LF_TIMER
-- ============================
--
create sequence S_LF_TIMER
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_TIMER_HISTORY
-- ====================================
--
create sequence S_LF_TIMER_HISTORY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_TMPLRELA
-- ===============================
--
create sequence S_LF_TMPLRELA
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_TRUCTTYPE
-- ================================
--
create sequence S_LF_TRUCTTYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_UDGROUP
-- ==============================
--
create sequence S_LF_UDGROUP
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_USEDSUBNO
-- ================================
--
create sequence S_LF_USEDSUBNO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_USER2SKIN
-- ================================
--
create sequence S_LF_USER2SKIN
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_USERPARA
-- ===============================
--
create sequence S_LF_USERPARA
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_USER_RPT
-- ===============================
--
create sequence S_LF_USER_RPT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_USER_SETTING
-- ===================================
--
create sequence S_LF_USER_SETTING
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_VERSION_EMPDB
-- ====================================
--
create sequence S_LF_VERSION_EMPDB
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

--
-- Creating sequence S_LF_VODINFO
-- ==============================
--
create sequence S_LF_VODINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_VODMOREC
-- ===============================
--
create sequence S_LF_VODMOREC
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_VODMTREC
-- ===============================
--
create sequence S_LF_VODMTREC
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WC_REVENT
-- ================================
--
create sequence S_LF_WC_REVENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WC_RTEXT
-- ===============================
--
create sequence S_LF_WC_RTEXT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WC_VERIFY
-- ================================
--
create sequence S_LF_WC_VERIFY
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WEIX_EMOJI
-- =================================
--
create sequence S_LF_WEIX_EMOJI
minvalue 1
maxvalue 999999999999999999999999999
start with 500
increment by 1
cache 20;

--
-- Creating sequence S_LF_WGPARAMCONFIG
-- ====================================
--
create sequence S_LF_WGPARAMCONFIG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WGPARAMDEFINITION
-- ========================================
--
create sequence S_LF_WGPARAMDEFINITION
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WX_BASEINFO
-- ==================================
--
create sequence S_LF_WX_BASEINFO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WX_DATATYPE
-- ==================================
--
create sequence S_LF_WX_DATATYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_LF_WX_PAGE
-- ==============================
--
create sequence S_LF_WX_PAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WX_SENDCOUNT
-- ===================================
--
create sequence S_LF_WX_SENDCOUNT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WX_UPLOADFILE
-- ====================================
--
create sequence S_LF_WX_UPLOADFILE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_LF_WX_VISITLOG
-- ==================================
--
create sequence S_LF_WX_VISITLOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_LBS_PIOS
-- ===============================
--
create sequence S_OT_LBS_PIOS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_LBS_PUSHSET
-- ==================================
--
create sequence S_OT_LBS_PUSHSET
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_OT_LBS_USERPIOS
-- ===================================
--
create sequence S_OT_LBS_USERPIOS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_ONL_MSG_HIS
-- ==================================
--
create sequence S_OT_ONL_MSG_HIS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_ONL_SERVER
-- =================================
--
create sequence S_OT_ONL_SERVER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_SIT_PLANT
-- ================================
--
create sequence S_OT_SIT_PLANT
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_OT_SIT_TYPE
-- ===============================
--
create sequence S_OT_SIT_TYPE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

--
-- Creating sequence S_OT_WEI_COUNT
-- ================================
--
create sequence S_OT_WEI_COUNT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_WEI_REVENT
-- =================================
--
create sequence S_OT_WEI_REVENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_WEI_RTEXT
-- ================================
--
create sequence S_OT_WEI_RTEXT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_OT_WEI_SENDLOG
-- ==================================
--
create sequence S_OT_WEI_SENDLOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

--
-- Creating sequence S_RECON_ID
-- ============================
--
create sequence S_RECON_ID
minvalue 1
maxvalue 99999999
start with 1
increment by 1
nocache
order;

--
-- Creating sequence S_S_DEPART
-- ============================
--
create sequence S_S_DEPART
minvalue 1
maxvalue 1000000000000000000000000000
start with 1
increment by 1
nocache;

--
-- Creating package DGPACKAGE
-- ==========================
--
CREATE OR REPLACE PACKAGE DGPACKAGE  AS

 TYPE DG_CURSOR IS REF CURSOR;

end DGPACKAGE;
/

--
-- Creating procedure COMPILE_PROCEDURE
-- ====================================
--
create or replace procedure COMPILE_PROCEDURE
as
v_sql varchar2(2000);
begin

  for v in (SELECT * FROM USER_OBJECTS WHERE OBJECT_TYPE IN ('PROCEDURE','TRIGGER') AND STATUS='INVALID' AND OBJECT_NAME NOT LIKE '%BIN$%')
    loop
      v_sql:= 'alter  '||v.object_type||' '|| v.object_name||' compile';
      execute immediate v_sql;
      dbms_output.put_line(v_sql);
    end loop;
end;
/

--
-- Creating procedure COMPILE_VIEW
-- ===============================
--
create or replace procedure COMPILE_VIEW
IS
v_sql varchar2(2000);
begin

  for v in (SELECT * FROM USER_VIEWS WHERE VIEW_NAME IN ('MTTASK_VIEW','MTTASK_VIEW_2','REALTASKGROUP_VIEW') and view_name not like '%BIN$%' )
    loop
      v_sql:= ' ALTER VIEW '||v.View_Name || '  compile';
      execute immediate v_sql;
      dbms_output.put_line(v_sql);
    end loop;
end;
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
      if var_count = 0 or var_count is null then
        --短信充值/回收数目不能为空
         var_returncount:=-2;
         return;
      end if;
      --判断是否为admin，admin为顶级机构的操作员
      begin
        if var_username<>'admin' then
           var_parentid:=var_sysdepid;
        else
           select superior_id into var_parentid  from lf_dep where dep_id=var_depid;
        end if;
         EXCEPTION WHEN OTHERS THEN
         --获取操作员上级机构失败
         var_returncount:=-3;
         rollback;
         return;
      end;

      if var_username<>'admin' or (var_username='admin' and var_depid<>var_sysdepid) then
         --充值
         if var_costtype=1 then
            update lf_dep_user_balance set sms_balance=sms_balance-var_count where corp_code=var_corpcode and sms_balance>=var_count and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             if var_yxcount=0 then
               begin
                 select count(bl_id) into var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
                end;
                 if var_blcount=0 then
                    --机构下没有可用短信余额
                    var_returncount:=-4;
                 else
                   --充值数据大于短信可分配余额
                    var_returncount:=-5;
                 end if;
                 rollback;
                 return;
             end if;
         else
           --回收
             --回收将回收的加到父及机构上
             update lf_dep_user_balance set sms_balance=sms_balance+var_count where corp_code=var_corpcode and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             if var_yxcount=0 then
               begin
                 select count(bl_id) into var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
               end;
                 if var_blcount=0 then
                    --机构下没有可用短信余额
                    var_returncount:=-4;
                    rollback;
                    return;
                 end if;
             end if;
           end if;
      end if;
      --充值
      if var_costtype=1 then
        update lf_dep_user_balance set sms_balance=sms_balance+var_count where corp_code=var_corpcode and target_id=var_depid;
        var_yxcount:=sql%rowcount;
        if var_yxcount=0 then
          --如果子的机构没有充过值则插入一条充值记录
          insert into lf_dep_user_balance(target_id,sms_balance,sms_count,mms_balance,mms_count,corp_code)
           values(var_depid,var_count,0,0,0,var_corpcode);
        end if;
       else
          update lf_dep_user_balance set sms_balance=sms_balance-var_count where corp_code=var_corpcode and sms_balance>=var_count and target_id=var_depid;
          var_yxcount:=sql%rowcount;
          if var_yxcount=0 then
            begin
              select count(bl_id) into var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_depid;
              EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
            end;
              if var_blcount=0 then
                --机构没有进行充值过
                var_returncount:=-7;
              else
                --回收短信数大于机构可分配数目
                var_returncount:=-5;
              end if;
              rollback;
              return;
          end if;
       end if;
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
CREATE OR REPLACE PROCEDURE FEEDEDUCTION(var_depid number,var_sendcount number,var_returncount out int)
IS
 yxcount number;
 blcount number;
BEGIN
      if var_sendcount = 0 then
         var_returncount:=-5;
      else
          if var_sendcount>0 then
             update LF_DEP_USER_BALANCE set SMS_BALANCE=SMS_BALANCE-var_sendcount,SMS_COUNT=SMS_COUNT+var_sendcount where TARGET_ID=var_depid and SMS_BALANCE>=var_sendcount;
             yxcount:=sql%rowcount;
             if yxcount>0 then
                var_returncount:=0;
             else
                select count(BL_ID) into blcount from  LF_DEP_USER_BALANCE where TARGET_ID=var_depid;
                if blcount=0 then
                     --用户所属机构没有充值
                     var_returncount:=-4;
                else
                      --短信余额不足
                     var_returncount:=-2;
                end if;
             end if;
          else
             update LF_DEP_USER_BALANCE set SMS_BALANCE=SMS_BALANCE-var_sendcount,SMS_COUNT=SMS_COUNT+var_sendcount where TARGET_ID=var_depid and SMS_COUNT+var_sendcount>0;
             yxcount:=sql%rowcount;
             if yxcount>0 then
                --短信回收成功
                var_returncount:=1;
             else
                select count(BL_ID) into blcount from  LF_DEP_USER_BALANCE where TARGET_ID=var_depid;
                if blcount=0 then
                    --用户所属机构没有充值
                    var_returncount:=-4;
                else
                    --短信回收已发送条数异常
                    var_returncount:=-8;
                end if;
             end if;
          end if;
        end if;
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
     IF Lev = 1 then
     insert into LF_TEMP values(PID,0,SJSC,sysdate);
     end if;
     SELECT nvl(count(*),0) into rrowcount FROM LF_CLIENT_DEP  WHERE (PARENT_ID = PID);
     for r in (SELECT DEP_ID FROM LF_CLIENT_DEP  WHERE (PARENT_ID = PID)) loop
           insert into LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
     end loop;
     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT nvl(count(*),0) into rrowcount FROM LF_CLIENT_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC);
         for r in (SELECT A.DEP_ID FROM LF_CLIENT_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC)) loop
          insert into LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
         end loop;
     end loop;

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
      IF Lev = 1 then
      I := 1;
     end if;
     SELECT nvl(count(*),0) into wcount FROM LF_CLIENT_DEP  WHERE (DEP_ID = PID);
     if wcount=0 then
         I:=0;
         rrowcount:=0;
     else
        SELECT PARENT_ID into rrowcount FROM LF_CLIENT_DEP  WHERE (DEP_ID = PID);
     end if;

     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT PARENT_ID into rrowcount FROM LF_CLIENT_DEP  WHERE (DEP_ID = rrowcount);
     end loop;
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
     IF Lev = 1 then
     insert into LF_TEMP values(PID,0,SJSC,sysdate);
     end if;
     SELECT nvl(count(*),0) into rrowcount FROM LF_EMPLOYEE_DEP  WHERE (PARENT_ID = PID);
     for r in (SELECT DEP_ID FROM LF_EMPLOYEE_DEP  WHERE (PARENT_ID = PID)) loop
           insert into LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
     end loop;
     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT nvl(count(*),0) into rrowcount FROM LF_EMPLOYEE_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC);
         for r in (SELECT A.DEP_ID FROM LF_EMPLOYEE_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.PARENT_ID) and (b.sjs=SJSC)) loop
          insert into LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
         end loop;
     end loop;

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
      IF Lev = 1 then
      I := 1;
     end if;
     SELECT nvl(count(*),0) into wcount FROM Lf_Employee_Dep  WHERE (DEP_ID = PID);
     if wcount=0 then
         I:=0;
         rrowcount:=0;
     else
        SELECT PARENT_ID into rrowcount FROM Lf_Employee_Dep  WHERE (DEP_ID = PID);
     end if;

     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT PARENT_ID into rrowcount FROM Lf_Employee_Dep  WHERE (DEP_ID = rrowcount);
     end loop;
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
     IF Lev = 1 then
     insert into LF_TEMP values(PID,0,SJSC,sysdate);
     end if;
     SELECT nvl(count(*),0) into rrowcount FROM LF_DEP  WHERE (SUPERIOR_ID = PID) and (DEP_STATE=1);
     for r in (SELECT DEP_ID FROM LF_DEP  WHERE (SUPERIOR_ID = PID) and (DEP_STATE=1)) loop
           insert into LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
     end loop;
     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT nvl(count(*),0) into rrowcount FROM LF_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.SUPERIOR_ID) and (a.dep_state=1) and (b.sjs=SJSC);
         for r in (SELECT A.DEP_ID FROM LF_DEP a, LF_TEMP b WHERE (b.Lev = I-1) AND (b.DepID = a.SUPERIOR_ID) and (a.dep_state=1) and (b.sjs=SJSC)) loop
          insert into LF_TEMP values(r.DEP_ID,I,SJSC,sysdate);
         end loop;
     end loop;

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
      IF Lev = 1 then
      I := 1;
     end if;
     SELECT nvl(count(*),0) into wcount FROM LF_DEP  WHERE (DEP_ID = PID);
     if wcount=0 then
         I:=0;
         rrowcount:=0;
     else
        SELECT SUPERIOR_ID into rrowcount FROM LF_DEP  WHERE (DEP_ID = PID);
     end if;

     WHILE rrowcount <> 0 loop
         I :=I+1;
         SELECT SUPERIOR_ID into rrowcount FROM LF_DEP  WHERE (DEP_ID = rrowcount);
     end loop;
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
begin
  processflag           := 2;
  currindex             := 0;
  maxindex              := 0;

  --------------------VER 3.2-------------------------------------
--删除临时
          tablename :='tmep_summary';
          isExist:=0;
          select count(table_name) into isExist from user_tables where table_name=upper(tablename);
          if(isExist>0) then
            begin
                  str := 'drop table '||tablename;
                  execute immediate str;
            end;
          end if;
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



 select nvl(min(ID),0),nvl(max(ID),0) into currindex,maxindex  from MT_TASK  where ( MT_TASK.SENDTIME between  to_date(TO_CHAR(sysdate-processflag,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss '));
 --统计
 str:= 'insert into  '|| tablename ||' (USERID,TASKID,SPGATE,ERRORCODE,Y,IMONTH,ICOUNT,IYMD,SPISUNCM,SVRTYPE,P1,P2,P3,P4)
               select  USERID,TASKID,SPGATE,ERRORCODE,
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
       str:='merge into LF_MTRPT_EC M
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
          when matched then
               update set M.ICOUNT=T.ICOUNT
          when not matched then
               insert (USERID,TASKID,SPGATE,IYMD,IMONTH,Y,ICOUNT,ERRORCODE,SPISUNCM,SVRTYPE,P1,P2,P3,P4)
               values(T.USERID,T.TASKID,T.SPGATE,T.IYMD,T.IMONTH,T.Y,T.ICOUNT,T.ERRORCODE,T.SPISUNCM,T.SVRTYPE,T.P1,T.P2,T.P3,T.P4)';
--dbms_output.put_line(str);
 execute immediate str;


 execute immediate  'drop table '||tablename;
 --execute immediate  'drop table '||tablename;
 commit;
end;
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
      if var_count = 0 or var_count is null then
        --彩信充值/回收数目不能为空
         var_returncount:=-2;
         return;
      end if;
      --判断是否为admin，admin为顶级机构的操作员
      begin
        if var_username<>'admin' then
           var_parentid:=var_sysdepid;
        else
           select superior_id into var_parentid  from lf_dep where dep_id=var_depid;
        end if;
        EXCEPTION WHEN OTHERS THEN
         --获取操作员上级机构失败
         var_returncount:=-3;
         rollback;
         return;
      end;
      if var_username<>'admin' or (var_username='admin' and var_depid<>var_sysdepid) then
         --充值
         if var_costtype=1 then
            update lf_dep_user_balance set mms_balance=mms_balance-var_count where corp_code=var_corpcode and mms_balance>=var_count and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             if var_yxcount=0 then
               begin
                 select count(bl_id) into var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                 --获取用户短信余额记录失败
                 var_returncount:=-6;
                 rollback;
                 return;
                end;
                 if var_blcount=0 then
                    --机构下没有可用彩信余额
                    var_returncount:=-4;
                 else
                   --充值数据大于彩信可分配余额
                    var_returncount:=-5;
                 end if;
                 rollback;
                 return;
             end if;
         else
           --回收
             --回收将回收的加到父及机构上
             update lf_dep_user_balance set mms_balance=mms_balance+var_count where corp_code=var_corpcode and target_id=var_parentid;
             var_yxcount:=sql%rowcount;
             if var_yxcount=0 then
               begin
                 select count(bl_id) into var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_parentid;
                 EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
               end;
                 if var_blcount=0 then
                    --机构下没有可用彩信余额
                    var_returncount:=-4;
                    rollback;
                    return;
                 end if;

             end if;
           end if;
      end if;
      --充值
      if var_costtype=1 then
        update lf_dep_user_balance set mms_balance=mms_balance+var_count where corp_code=var_corpcode and target_id=var_depid;
        var_yxcount:=sql%rowcount;
        if var_yxcount=0 then
          --如果子的机构没有充过值则插入一条充值记录
          insert into lf_dep_user_balance(target_id,sms_balance,sms_count,mms_balance,mms_count,corp_code)
           values(var_depid,0,0,var_count,0,var_corpcode);
        end if;
       else
          update lf_dep_user_balance set mms_balance=mms_balance-var_count where corp_code=var_corpcode and mms_balance>=var_count and target_id=var_depid;
          var_yxcount:=sql%rowcount;
          if var_yxcount=0 then
            begin
             select count(bl_id) into var_blcount from lf_dep_user_balance  where corp_code=var_corpcode and target_id=var_depid;
             EXCEPTION WHEN OTHERS THEN
                   --获取用户短信余额记录失败
                   var_returncount:=-6;
                   rollback;
                   return;
            end;
            if var_blcount=0 then
              --机构没有进行充值过
              var_returncount:=-7;
            else
              --回收彩信数大于机构可分配数目
              var_returncount:=-5;
            end if;
            rollback;
            return;
          end if;
       end if;
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
CREATE OR REPLACE PROCEDURE MMSFEEDEDUCTION(var_depid number,var_sendcount number,var_returncount out int)
IS
 yxcount number;
 blcount number;
BEGIN
      if var_sendcount = 0 then
        --:彩信发送或回收条数不能为空
         var_returncount:=-5;
      else
          if var_sendcount>0 then
             update LF_DEP_USER_BALANCE set MMS_BALANCE=MMS_BALANCE-var_sendcount,MMS_COUNT=MMS_COUNT+var_sendcount where TARGET_ID=var_depid and MMS_BALANCE>=var_sendcount;
             yxcount:=sql%rowcount;
             if yxcount>0 then
                var_returncount:=0;
             else
                select count(BL_ID) into blcount from  LF_DEP_USER_BALANCE ld where TARGET_ID=var_depid;
                if blcount=0 then
                     --彩信用户所属机构没有充值
                     var_returncount:=-4;
                else
                      --彩信余额不足
                     var_returncount:=-2;
                end if;
             end if;
          else
             update LF_DEP_USER_BALANCE set MMS_BALANCE=MMS_BALANCE-var_sendcount,MMS_COUNT=MMS_COUNT+var_sendcount where TARGET_ID=var_depid and MMS_COUNT+var_sendcount>0;
             yxcount:=sql%rowcount;
             if yxcount>0 then
                --彩信回收成功
                var_returncount:=1;
             else
                select count(BL_ID) into blcount from  LF_DEP_USER_BALANCE where TARGET_ID=var_depid;
                if blcount=0 then
                    --彩信用户所属机构没有充值
                    var_returncount:=-4;
                else
                    --彩信回收已发送条数异常
                    var_returncount:=-8;
                end if;
             end if;
          end if;
        end if;
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
begin
  open cur_query for
    select distinct taskid from mt_datareport;
  loop
    fetch cur_query
      into var_taskid;
    exit when cur_query%notfound;
    select sum(ICOUNT),sum(RSUCC),sum(RFAIL1), sum(RFAIL2),sum(RNRET)
      into var_icount,var_succ, var_fail1, var_fail2,var_rnret
      from mt_datareport
     where taskid = var_taskid;
    update LF_MTTASK
       set SUC_COUNT = var_succ ,FAI_COUNT = var_fail1 ,ICOUNT = var_icount,rfail2=var_fail2,rnret=var_rnret
     where MT_ID in (select MT_ID
                       from LF_TASK
                      where TASKID = var_taskid);

  end loop;
  close cur_query;
  commit;
   EXCEPTION WHEN OTHERS THEN
    dbms_output.put_line('汇总报表异常');
end PRC_DATAINSERT;
/

--
-- Creating procedure PRC_MMSSUMM
-- ==============================
--
create or replace procedure PRC_MMSSUMM  is
paramvalue varchar2(64);
hparamvalue     varchar(64);
begin
  select param_value into hparamvalue from lf_sys_param where param_item='isHdataFilish';
  if hparamvalue='0' then
     select param_value into paramvalue from lf_sys_param where param_item='ismSummFilish';
    if paramvalue='0' then
       begin
          update lf_sys_param set param_value='1' where param_item='ismSummFilish';
          commit;
          delete from LF_MMSTASKREPORT;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='ismSummFilish';
           delete from LF_MMSTASKREPORT;
           commit;
           return;
        end;
        begin
            insert into LF_MMSTASKREPORT(R_TASKID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
            select TASKID,0,0,0,0,0 from lf_mttask where TIMER_TIME>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and MS_TYPE=2;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='ismSummFilish';
            delete from  LF_MMSTASKREPORT;
            commit;
            return;
        end;
        begin
            merge into LF_MMSTASKREPORT M
            using (select taskid TASKID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0) RNRET --未返数
              from mms_task  where sendtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by TASKID) T
            on(M.R_TASKID=T.TASKID)
            when matched then
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='ismSummFilish';
            delete from LF_MMSTASKREPORT;
            commit;
            return;
        end;
        begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_MMSTASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Ms_Type=2)
          when matched then
            update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='ismSummFilish';
          delete from LF_MMSTASKREPORT;
          commit;
          return;
        end;
        delete from LF_MMSTASKREPORT;
	update lf_sys_param set param_value='0' where param_item='ismSummFilish';
        commit;
    end if;
  end if;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('汇总异常');
            rollback;
            return;
end;
/

--
-- Creating procedure PRC_MMSSUMMONE
-- =================================
--
create or replace procedure PRC_MMSSUMMONE  is
r_c number;
paramvalue varchar2(64);
paramintvalue int;
begin
  select count(*) into r_c from LF_MMSTASKREPORT;
  if r_c=0 then
     paramintvalue:=4;
     select param_value into paramvalue from lf_sys_param where param_item='MsSummDay';
     if paramvalue is not null  then
       paramintvalue:=TO_NUMBER(paramvalue);
     end if;
     begin
        insert into LF_MMSTASKREPORT(R_TASKID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
        select taskid,nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0),nvl(sum(RFAIL2),0),nvl(sum(RNRET),0) from mms_datareport md
        where md.IYMD>TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by taskid;
        commit;
        EXCEPTION WHEN OTHERS THEN
          rollback;
          delete from LF_MMSTASKREPORT;
          commit;
          return;
     end;

     begin
          merge into LF_MMSTASKREPORT M
          using (select TASKID,nvl(sum(ICOUNT),0) ICOUNT,nvl(sum(RSUCC),0) RSUCC,nvl(sum(RFAIL1),0) RFAIL1,nvl(sum(RFAIL2),0) RFAIL2,nvl(sum(RNRET),0) RNRET from mms_datareport
          where IYMD<=TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by TASKID) T
          on(M.r_Taskid=T.TASKID)
          when matched then
               update set r_Icount=r_Icount+T.ICOUNT,r_Succ=r_Succ+T.RSUCC,r_Fail1=r_Fail1+T.RFAIL1,R_FAIL2=R_FAIL2+T.RFAIL2,R_NRET=R_NRET+T.RNRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from LF_MMSTASKREPORT;
            commit;
            return;
     end;

     begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_MMSTASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Ms_Type=2)
          when matched then
               update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from LF_MMSTASKREPORT;
            commit;
            return;
     end;
     delete from LF_MMSTASKREPORT;
     commit;
  end if;
  EXCEPTION WHEN OTHERS THEN
  dbms_output.put_line('汇总异常');
end;
/

--
-- Creating procedure PRC_SUMM
-- ===========================
--
create or replace procedure PRC_SUMM  is
paramvalue varchar2(64);
hparamvalue     varchar(64);
begin
  select param_value into hparamvalue from lf_sys_param where param_item='isHdataFilish';
  if hparamvalue='0' then
     select param_value into paramvalue from lf_sys_param where param_item='isSummFilish';
    if paramvalue='0' then
       begin
          update lf_sys_param set param_value='1' where param_item='isSummFilish';
          commit;
          delete from LF_TASKREPORT;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='isSummFilish';
           delete from  LF_TASKREPORT;
           commit;
           return;
        end;
        begin
            insert into LF_TASKREPORT(R_TASKID,R_BATCHID,R_TASKTYPE,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
            select TASKID,BATCHID,TASKTYPE,0,0,0,0,0 from lf_mttask where TIMER_TIME>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and MS_TYPE<>2;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        end;
        begin
            merge into LF_TASKREPORT M
            using (select taskid TASKID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0) RNRET --未返数
              from mt_task  where sendtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by TASKID) T
            on(M.r_Taskid=T.TASKID and M.r_Tasktype=1)
            when matched then
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        end;
        begin
            merge into LF_TASKREPORT M
            using (select BATCHID BATCHID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0) RNRET --未返数
              from mt_task  where sendtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by BATCHID) T
            on(M.R_BATCHID=T.BATCHID and M.r_Tasktype=2)
            when matched then
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        end;
        begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and m.ms_type<>2)
          when matched then
            update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        end;

        begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.tasktype=2 and M.Ms_Type<>2)
          when matched then
            update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        end;

        delete from  LF_TASKREPORT;
	update lf_sys_param set param_value='0' where param_item='isSummFilish';
        commit;
    end if;
  end if;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('汇总异常');
            rollback;
            return;
end;
/

--
-- Creating procedure PRC_SUMMAPP
-- ==============================
--
create or replace procedure PRC_SUMMAPP  is
paramvalue varchar2(64);
begin
    select param_value into paramvalue from lf_sys_param where param_item='isAppSummFilish';
    if paramvalue='0' then
       begin
          update lf_sys_param set param_value='1' where param_item='isAppSummFilish';
          commit;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='isAppSummFilish';
           commit;
           return;
        end;
        begin
            merge into lf_app_mttask M
            using (select taskid TASKID,
              nvl(count(case RPT_STATE when '0' then 1 else null end),0) succount,--成功数
              nvl(count(case RPT_STATE when '1' then 1 else null end),0) faicount,--失败数
              nvl(count(case RPT_STATE when '0' then 1 else null end),0) readcount
              from lf_app_mtmsg  where TASKID is not null and CREATETIME>to_date(TO_CHAR(sysdate-3,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') group by TASKID) T
            on(M.Taskid=T.TASKID )
            when matched then
            update set SUC_COUNT = T.succount ,FAI_COUNT = T.faicount,READ_COUNT = T.readcount,UNREAD_COUNT=0;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isAppSummFilish';
            commit;
            return;
        end;
	      update lf_sys_param set param_value='0' where param_item='isAppSummFilish';
        commit;
    end if;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('汇总异常');
            rollback;
            return;
end;
/

--
-- Creating procedure PRC_SUMMONE
-- ==============================
--
create or replace procedure PRC_SUMMONE  is
r_c number;
paramvalue varchar2(64);
paramintvalue int;
begin
  select count(*) into r_c from LF_TASKREPORT;
  if r_c=0 then
     paramintvalue:=4;
     select param_value into paramvalue from lf_sys_param where param_item='SummDay';
     if paramvalue is not null then
       paramintvalue:=TO_NUMBER(paramvalue);
     end if;
     begin
        insert into LF_TASKREPORT(R_TASKID,R_BATCHID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
        select taskid,BATCHID,nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0),nvl(sum(RFAIL2),0),nvl(sum(RNRET),0) from mt_datareport md
        where md.IYMD>TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by taskid,BATCHID;
        commit;
        EXCEPTION WHEN OTHERS THEN
          rollback;
          delete from  LF_TASKREPORT;
          commit;
          return;
     end;

     begin
          merge into LF_TASKREPORT M
          using (select TASKID,BATCHID,nvl(sum(ICOUNT),0) ICOUNT,nvl(sum(RSUCC),0) RSUCC,nvl(sum(RFAIL1),0) RFAIL1,nvl(sum(RFAIL2),0) RFAIL2,nvl(sum(RNRET),0) RNRET from mt_datareport
          where IYMD<=TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD')) group by TASKID,BATCHID) T
          on(M.r_Taskid=T.TASKID and M.r_Batchid=T.BATCHID)
          when matched then
               update set r_Icount=r_Icount+T.ICOUNT,r_Succ=r_Succ+T.RSUCC,r_Fail1=r_Fail1+T.RFAIL1,R_FAIL2=R_FAIL2+T.RFAIL2,R_NRET=R_NRET+T.RNRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     end;

     begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and M.Ms_Type<>2)
          when matched then
               update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     end;

      begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.Tasktype=2 and M.Ms_Type<>2)
          when matched then
               update set ICOUNT2=T.r_Icount,suc_count=T.r_Succ,fai_count=T.r_Fail1,rfail2=T.R_FAIL2,rnret=T.R_NRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     end;

     delete from  LF_TASKREPORT;
     commit;
  end if;
  EXCEPTION WHEN OTHERS THEN
  dbms_output.put_line('汇总异常');
end;
/

--
-- Creating procedure PRC_SUMMONEWY
-- ================================
--
create or replace procedure PRC_SUMMONEWY  is
r_c number;
paramvalue varchar2(64);
paramintvalue int;
begin
  select count(*) into r_c from LF_TASKREPORT;--查询是否有数据
  if r_c=0 then--没数据则执行汇总
     paramintvalue:=4;--默认晚上汇总4天之内的
     select param_value into paramvalue from lf_sys_param where param_item='SummDay';--获取晚上汇总n天之内的设置
     if paramvalue is not null then--晚上汇总几天之内的设置有值，则使用设置值
       paramintvalue:=TO_NUMBER(paramvalue);
     end if;

	 --先把n天内 且符合条件的记录填到表LF_TASKREPORT
	 begin
        insert into LF_TASKREPORT(R_TASKID,R_BATCHID,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
        select taskid,BATCHID,nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0),nvl(sum(RFAIL2),0),nvl(sum(RNRET),0)
		from mt_datareport md where md.IYMD>TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD'))
		and md.SPGATE in (select spgate from XT_GATE_QUEUE where spgate like '200%')
		group by taskid,BATCHID;
        commit;
        EXCEPTION WHEN OTHERS THEN
          rollback;
          delete from  LF_TASKREPORT;
          commit;
          return;
     end;

	 --把n天前 且符合条件的总数记录更新到表LF_TASKREPORT
     begin
		merge into LF_TASKREPORT M
		using (select TASKID,BATCHID,nvl(sum(ICOUNT),0) ICOUNT,nvl(sum(RSUCC),0) RSUCC,nvl(sum(RFAIL1),0) RFAIL1,nvl(sum(RFAIL2),0) RFAIL2,nvl(sum(RNRET),0) RNRET from mt_datareport
		where IYMD<=TO_NUMBER(TO_CHAR(sysdate-paramintvalue,'YYYYMMDD'))
		and SPGATE in (select spgate from XT_GATE_QUEUE where spgate like '200%')
		group by TASKID,BATCHID) T
          on(M.r_Taskid=T.TASKID and M.r_Batchid=T.BATCHID)
          when matched then
               update set r_Icount=r_Icount+T.ICOUNT,r_Succ=r_Succ+T.RSUCC,r_Fail1=r_Fail1+T.RFAIL1,R_FAIL2=R_FAIL2+T.RFAIL2,R_NRET=R_NRET+T.RNRET;
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     end;

	 --更新信息到lf_mttask表的WYSENDINFO字段，taskid的任务
     begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and M.Ms_Type<>2 and T.r_Icount>0)
          when matched then
               update set m.WYSENDINFO=(T.r_Icount||'/'||T.r_Succ+'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
     end;

	--更新信息到lf_mttask表的WYSENDINFO字段，batchid的任务
	begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.Tasktype=2 and M.Ms_Type<>2 and T.r_Icount>0)
          when matched then
               update set m.WYSENDINFO=(T.r_Icount||'/'||T.r_Succ||'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
            rollback;
            delete from  LF_TASKREPORT;
            commit;
            return;
	end;

     delete from  LF_TASKREPORT;
     commit;
  end if;
  EXCEPTION WHEN OTHERS THEN
  dbms_output.put_line('网优晚上汇总异常');
end;
/

--
-- Creating procedure PRC_SUMMWY
-- =============================
--
create or replace procedure PRC_SUMMWY  is
paramvalue varchar2(64);
hparamvalue     varchar(64);
begin
  select param_value into hparamvalue from lf_sys_param where param_item='isHdataFilish';--获取调度汇总是否正在执行使用的状态
  if hparamvalue='0' then--调度汇总不在执行
     select param_value into paramvalue from lf_sys_param where param_item='isSummFilish';--获取EMP调度汇总是否正在执行使用的状态
    if paramvalue='0' then--EMP调度汇总不在执行

	   begin
          update lf_sys_param set param_value='1' where param_item='isSummFilish';--设置EMP调度汇总为正在执行
          commit;
          delete from LF_TASKREPORT;
           EXCEPTION WHEN OTHERS THEN
           rollback;
           update lf_sys_param set param_value='0' where param_item='isSummFilish';--出现异常，则设置EMP调度汇总不在执行
           delete from  LF_TASKREPORT;
           commit;
           return;
        end;

        begin
            insert into LF_TASKREPORT(R_TASKID,R_BATCHID,R_TASKTYPE,R_ICOUNT,R_SUCC,R_FAIL1,R_FAIL2,R_NRET)
            select TASKID,BATCHID,TASKTYPE,0,0,0,0,0 from lf_mttask where TIMER_TIME>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and MS_TYPE<>2;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        end;

		--符合条件的记录填到表LF_TASKREPORT，taskid的任务
        begin
            merge into LF_TASKREPORT M
            using (select taskid TASKID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0) RNRET --未返数
              from mt_task  where sendtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and SPGATE in (select spgate from XT_GATE_QUEUE where spgate like '200%')
			  group by TASKID) T
            on(M.r_Taskid=T.TASKID and M.r_Tasktype=1)
            when matched then
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        end;

		--符合条件的记录填到表LF_TASKREPORT，batchid的任务
        begin
            merge into LF_TASKREPORT M
            using (select BATCHID BATCHID,count(id) ICOUNT,--发送总数
              nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0) RSUCC,--成功数
              nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0) RFAIL1,--失败数
              (count(id)-nvl(count(case trim(errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0)
              -nvl(count(case substr(errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0)
              -nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0)) RFAIL2,
              nvl(count(case nvl(trim(errorcode),'0') when '0' then 1 else null end),0) RNRET --未返数
              from mt_task  where sendtime>to_date(TO_CHAR(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ') and SPGATE in (select spgate from XT_GATE_QUEUE where spgate like '200%')
			  group by BATCHID) T
            on(M.R_BATCHID=T.BATCHID and M.r_Tasktype=2)
            when matched then
            update set r_Icount=M.r_Icount+T.ICOUNT,r_Succ=M.r_Succ+T.RSUCC,r_Fail1=M.r_Fail1+T.RFAIL1,R_FAIL2=M.R_FAIL2+T.RFAIL2,R_NRET=M.R_NRET+T.RNRET;
            commit;
            EXCEPTION WHEN OTHERS THEN
            rollback;
            update lf_sys_param set param_value='0' where param_item='isSummFilish';
            delete from  LF_TASKREPORT;
            commit;
            return;
        end;

		--更新信息到lf_mttask表的WYSENDINFO字段，taskid的任务
        begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_TASKID from LF_TASKREPORT group by R_TASKID ) T
          on(M.Taskid=T.R_TASKID and M.Tasktype=1 and m.ms_type<>2 and T.r_Icount>0)
          when matched then
            update set WYSENDINFO=(T.r_Icount||'/'||T.r_Succ||'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        end;

		--更新信息到lf_mttask表的WYSENDINFO字段，batchid的任务
        begin
          merge into lf_mttask M
          using (select sum(r_Icount) as r_Icount ,sum(r_Succ) as r_Succ,sum(r_Fail1) as r_Fail1,sum(R_FAIL2) as R_FAIL2,sum(R_NRET) as R_NRET ,R_BATCHID from LF_TASKREPORT group by R_BATCHID ) T
          on(M.Batchid=T.R_BATCHID and M.tasktype=2 and M.Ms_Type<>2 and T.r_Icount>0)
          when matched then
            update set WYSENDINFO=(T.r_Icount||'/'||T.r_Succ||'/'||T.r_Fail1||'/'||T.R_FAIL2);
          commit;
          EXCEPTION WHEN OTHERS THEN
          rollback;
          update lf_sys_param set param_value='0' where param_item='isSummFilish';
          delete from  LF_TASKREPORT;
          commit;
          return;
        end;

        delete from  LF_TASKREPORT;
	update lf_sys_param set param_value='0' where param_item='isSummFilish';
        commit;
    end if;
  end if;
  EXCEPTION WHEN OTHERS THEN
            dbms_output.put_line('网优白天汇总异常');
            rollback;
            return;
end;
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

begin
  r_icount:=0;
  r_icountm:=0;
  --汇总实时信息
   if (task_id <> 0) then

       select count(m.id) ICOUNT,--发送总数
          nvl(count(case trim(m.errorcode) when 'DELIVRD' then 1 when '0' then 1 else null end),0) SUCC,--成功数
          nvl(count(case substr(m.errorcode,1,3) when 'E1:' then 1 when 'E2:' then 1 else null end),0) FAIL,--失败数
          nvl(count(case nvl(trim(m.errorcode),'0') when '0' then 1 else null end),0) NRET --未返数
          into r_icount,r_succ,r_fail1,r_nret
     from mt_task m where m.taskid=task_id group by m.taskid;
     r_fail2:=r_icount-r_succ-r_fail1-r_nret;
      select nvl(sum(ICOUNT),0),nvl(sum(RSUCC),0),nvl(sum(RFAIL1),0), nvl(sum(RFAIL2),0),nvl(sum(RNRET),0)
      into r_icountm,r_succm, r_fail1m, r_fail2m,r_nretm
      from mt_datareport
     where taskid = task_id and to_date(IYMD, 'YYYY-MM-DD')<to_date(TO_CHAR(sysdate-2,'yyyy-mm-dd'),'yyyy-mm-dd hh24:mi:ss ');

     if r_icount+r_icountm<>0 then
         update lf_mttask mttask set mttask.ICOUNT2=r_icount+r_icountm,
         mttask.suc_count=r_succ+r_succm,
          mttask.fai_count=r_fail1+r_fail1m,
          mttask.rfail2=r_fail2+r_fail2m,
          mttask.rnret=r_nret+r_nretm
          where mttask.mt_id in
          ( select  mt_id from LF_TASK
              where mt_id is not null and
              taskid=task_id
          );
   end if;
   end if;
    commit;
     EXCEPTION WHEN OTHERS THEN
    dbms_output.put_line('汇总异常');
end;
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
begin
  open cur_query for
    select MT.SPGATE,
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
      into var_spgate,
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

    select sum(RSUCC)+sum(RNRET),sum(ICOUNT)
      into var_countSucc ,var_icount
      from MT_DATAREPORT MT, LF_TASK LF
     where trim(mt.iymd) = trim(var_date)
       and trim(mt.userid) = trim(var_userid)
       and trim(mt.spgate) = trim(var_spgate)
       and trim(lf.DEP_ID) = trim(var_depId)
       and mt.taskid= lf.taskid;

    var_cost := var_countSucc * var_fee;
    select count(*)
      into j
      from lf_reconciliation lr
     where trim(lr.iymd) = trim(var_date)
       and trim(lr.STAFFNAME) =trim(var_staffname)
       and trim(lr.DEP_NAME) = trim(var_depName)
       and trim(lr.GATENAME) = trim(var_gateName);

    if (j = 0) then
      insert into LF_RECONCILIATION
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
    end if;
  end loop;
  close cur_query;
  open cur_query for
    select distinct taskid from mt_datareport;
  loop
    fetch cur_query
      into var_taskid;
    exit when cur_query%notfound;
    select sum(RSUCC)+sum(RNRET), sum(FAIL1), sum(FAIL2), sum(FAIL3),sum(ICOUNT)
      into var_succ, var_fail1, var_fail2, var_fail3,var_icount
      from mt_datareport
     where taskid = var_taskid;
    var_fail_count := var_fail1 + var_fail2 + var_fail3;
    update LF_MTTASK
       set SUC_COUNT = var_succ ,FAI_COUNT = var_fail_count ,ICOUNT = var_icount
     where MT_ID in (select MT_ID
                       from LF_TASK
                      where TASKID = var_taskid);

  end loop;
  close cur_query;
  commit;
   EXCEPTION WHEN OTHERS THEN
    dbms_output.put_line('汇总报表异常');
end PRO_INSERT;
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
-- Creating trigger TIG_LF_ACCOUNT_BIND_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_ACCOUNT_BIND_QUEUE"
before insert on LF_ACCOUNT_BIND
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_ACCOUNT_BIND.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_ADDRBOOKS_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_ADDRBOOKS_QUEUE"
before insert on LF_ADDRBOOKS
for each row
  
begin
    if(:new.ADDR_ID is null)
    then
      select S_LF_ADDRBOOKS.NEXTVAL into :new.ADDR_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_ALSMSRECORD_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_ALSMSRECORD_QUEUE"
before insert on LF_ALSMSRECORD
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_ALSMSRECORD.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APPMAINPAGEHIS_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_APPMAINPAGEHIS_QUEUE"
before insert on LF_APPMAINPAGEHIS
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_APPMAINPAGEHIS.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_CLIDOWLOAD_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_APP_CLIDOWLOAD_QUEUE"
before insert on LF_APP_CLIDOWLOAD
for each row
  
begin
    if(:new.CLID is null)
    then
      select S_LF_APP_CLIDOWLOAD.NEXTVAL into :new.CLID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_FOMEITEM_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_APP_FOMEITEM_QUEUE"
before insert on LF_APP_FOMEITEM
for each row
  
begin
    if(:new.FIELD_ID is null)
    then
      select S_LF_APP_FOMEITEM.NEXTVAL into :new.FIELD_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_FOMEVALVE_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_APP_FOMEVALVE_QUEUE"
before insert on LF_APP_FOMEVALVE
for each row
  
begin
    if(:new.V_ID is null)
    then
      select S_LF_APP_FOMEVALVE.NEXTVAL into :new.V_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_FOM_TYPE_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_APP_FOM_TYPE_QUEUE"
before insert on LF_APP_FOM_TYPE
for each row
  
begin
    if(:new.TYPE_ID is null)
    then
      select S_LF_APP_FOM_TYPE.NEXTVAL into :new.TYPE_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_LBS_PIOS_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_APP_LBS_PIOS_QUEUE"
before insert on LF_APP_LBS_PIOS
for each row
  
begin
    if(:new.P_ID is null)
    then
      select S_LF_APP_LBS_PIOS.NEXTVAL into :new.P_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_LBS_PUSH_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_APP_LBS_PUSH_QUEUE"
before insert on LF_APP_LBS_PUSH
for each row
  
begin
    if(:new.PUSH_ID is null)
    then
      select S_LF_APP_LBS_PUSH.NEXTVAL into :new.PUSH_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_LBS_USER_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_APP_LBS_USER_QUEUE"
before insert on LF_APP_LBS_USER
for each row
  
begin
    if(:new.UP_ID is null)
    then
      select S_LF_APP_LBS_USER.NEXTVAL into :new.UP_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_MSGCONTENT_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_APP_MSGCONTENT_QUEUE"
before insert on LF_APP_MSGCONTENT
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_APP_MSGCONTENT.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_MTMSG_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_APP_MTMSG_QUEUE"
before insert on LF_APP_MTMSG
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_APP_MTMSG.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_MTTASK_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_APP_MTTASK_QUEUE"
before insert on LF_APP_MTTASK
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_APP_MTTASK.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_MW_CLIENT_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_APP_MW_CLIENT_QUEUE"
before insert on LF_APP_MW_CLIENT
for each row
  
begin
    if(:new.WC_ID is null)
    then
      select S_LF_APP_MW_CLIENT.NEXTVAL into :new.WC_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_OL_MSGHIS_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_APP_OL_MSGHIS_QUEUE"
before insert on LF_APP_OL_MSGHIS
for each row
  
begin
    if(:new.M_ID is null)
    then
      select S_LF_APP_OL_MSGHIS.NEXTVAL into :new.M_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_REVENT_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_APP_REVENT_QUEUE"
before insert on LF_APP_REVENT
for each row
  
begin
    if(:new.EVT_ID is null)
    then
      select S_LF_APP_REVENT.NEXTVAL into :new.EVT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_RTEXT_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_APP_RTEXT_QUEUE"
before insert on LF_APP_RTEXT
for each row
  
begin
    if(:new.TET_ID is null)
    then
      select S_LF_APP_RTEXT.NEXTVAL into :new.TET_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_SIT_PLANT_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_APP_SIT_PLANT_QUEUE"
before insert on LF_APP_SIT_PLANT
for each row
  
begin
    if(:new.PLANT_ID is null)
    then
      select S_LF_APP_SIT_PLANT.NEXTVAL into :new.PLANT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_SIT_TYPE_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_APP_SIT_TYPE_QUEUE"
before insert on LF_APP_SIT_TYPE
for each row
  
begin
    if(:new.TYPE_ID is null)
    then
      select S_LF_APP_SIT_TYPE.NEXTVAL into :new.TYPE_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_TC_MOMSG_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_APP_TC_MOMSG_QUEUE"
before insert on LF_APP_TC_MOMSG
for each row
  
begin
    if(:new.MSG_ID is null)
    then
      select S_LF_APP_TC_MOMSG.NEXTVAL into :new.MSG_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_APP_VERIFY_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_APP_VERIFY_QUEUE"
before insert on LF_APP_VERIFY
for each row
  
begin
    if(:new.VF_ID is null)
    then
      select S_LF_APP_VERIFY.NEXTVAL into :new.VF_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_AUTOREPLY_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_AUTOREPLY_QUEUE"
before insert on LF_AUTOREPLY
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_SYSMTTASK.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_BILLLOG_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_BILLLOG_QUEUE"
before insert on LF_BILLLOG
for each row
  
begin
    if(:new.LOG_ID is null)
    then
      select S_LF_BILLLOG.NEXTVAL into :new.LOG_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_BINDFLOW_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_BINDFLOW_QUEUE"
before insert on LF_BINDFLOW
for each row
  
begin
    if(:new.BFID is null)
    then
      select S_LF_BINDFLOW.NEXTVAL into :new.BFID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_BIRTHDAYMEMBER_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_BIRTHDAYMEMBER_QUEUE"
before insert on LF_BIRTHDAYMEMBER
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_BIRTHDAYMEMBER.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_BIRTHDAYSETUP_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_BIRTHDAYSETUP_QUEUE"
before insert on LF_BIRTHDAYSETUP
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_BIRTHDAYSETUP.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_BLACKLIST_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_BLACKLIST_QUEUE"
before insert on LF_BLACKLIST
for each row
  
begin
    if(:new.BL_ID is null)
    then
      select S_LF_BLACKLIST.NEXTVAL into :new.BL_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_BUSINESS_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_BUSINESS_QUEUE"
before insert on LF_BUSINESS
for each row
 
begin
    if(:new.BUS_ID is null)
    then
    select S_LF_BUSINESS.NEXTVAL into :new.BUS_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_BUSMANAGER_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_BUSMANAGER_QUEUE"
before insert  on LF_BUSMANAGER
for each row
  
begin
    if(:new.BUS_ID is null)
    then
      select S_LF_BUSMANAGER.NEXTVAL into :new.BUS_ID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_BUSPKGETAOCAN_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_BUSPKGETAOCAN_QUEUE"
before insert on LF_BUSPKGETAOCAN
for each row
begin
    if (:new.ID is null)  then
      select S_LF_BUSPKGETAOCAN.NEXTVAL into :new.ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_BUSTAIL_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_BUSTAIL_QUEUE"
before insert on LF_BUSTAIL
for each row
begin
    if (:new.BUSTAIL_ID is null)  then
      select S_LF_BUSTAIL.NEXTVAL into :new.BUSTAIL_ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_BUS_PROCESS_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_BUS_PROCESS_QUEUE"
before insert on LF_BUS_PROCESS
for each row
  
begin
    if(:new.BUSPRO_ID is null)
    then
      select S_LF_BUS_PROCESS.NEXTVAL into :new.BUSPRO_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_BUS_TAILTMP_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_BUS_TAILTMP_QUEUE"
before insert on LF_BUS_TAILTMP
for each row
begin
    if (:new.ID is null)  then
      select S_LF_BUS_TAILTMP.NEXTVAL into :new.ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_BUS_TAOCAN_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_BUS_TAOCAN_QUEUE"
before insert on LF_BUS_TAOCAN
for each row
begin
    if (:new.TAOCAN_ID is null)  then
      select S_LF_BUS_TAOCAN.NEXTVAL into :new.TAOCAN_ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_CLIDEP_CONN_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_CLIDEP_CONN_QUEUE"
before insert on LF_CLIDEP_CONN
for each row
 
begin
    if(:new.CONN_ID is null)
    then
    select S_LF_CLIDEP_CONN.NEXTVAL into :new.CONN_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_CLIENTCLASS_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_CLIENTCLASS_QUEUE"
before insert on LF_CLIENTCLASS
for each row
  
begin
    if(:new.CC_ID is null)
    then
    select S_LF_CLIENTCLASS.nextval into :new.CC_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_CLIENT_DEP_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_CLIENT_DEP_QUEUE"
before insert  on  LF_CLIENT_DEP
for each row
  
begin
    if(:new.DEP_ID is null)
    then
      select S_LF_CLIENT_DEP.NEXTVAL into :new.DEP_ID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_CONTRACT_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_CONTRACT_QUEUE"
before insert on LF_CONTRACT
for each row
begin
    if (:new.CONTRACT_ID is null)  then
      select S_LF_CONTRACT.NEXTVAL into :new.CONTRACT_ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_CONTRACT_TAOCAN_QUEUE
-- =============================================
--
create or replace trigger "TIG_LF_CONTRACT_TAOCAN_QUEUE"
before insert on LF_CONTRACT_TAOCAN
for each row
begin
    if (:new.ID is null)  then
      select S_LF_CONTRACT_TAOCAN.NEXTVAL into :new.ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_CORP_CONF_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_CORP_CONF_QUEUE"
before insert on LF_CORP_CONF
for each row
  
begin
    if(:new.CC_ID is null)
    then
      select S_LF_CORP_CONF.NEXTVAL into :new.CC_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_CORP_QUEUE
-- ==================================
--
create or replace trigger "TIG_LF_CORP_QUEUE"
before insert on LF_CORP
for each row
  
begin
    if(:new.CORP_ID is null)
    then
      select S_LF_CORP.NEXTVAL into :new.CORP_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_CUSTFIELD_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_CUSTFIELD_QUEUE"
before insert on LF_CUSTFIELD
for each row
  
begin
    if(:new.ID is null)
    then
    select S_LF_CUSTFIELD.nextval into :new.ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_CUSTFIELD_VALUE_QUEUE
-- =============================================
--
create or replace trigger "TIG_LF_CUSTFIELD_VALUE_QUEUE"
before insert on LF_CUSTFIELD_VALUE
for each row
  
begin
    if(:new.ID is null)
    then
    select S_LF_CUSTFIELD_VALUE.nextval into :new.ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_DBCONNECT_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_DBCONNECT_QUEUE"
before insert on LF_DBCONNECT
for each row
  
begin
    if(:new.DB_ID is null)
    then
      select S_LF_DBCONNECT.NEXTVAL INTO :new.DB_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_DEDUCTIONS_DISP_QUEUE
-- =============================================
--
create or replace trigger "TIG_LF_DEDUCTIONS_DISP_QUEUE"
before insert on LF_DEDUCTIONS_DISP
for each row
begin
    if (:new.ID is null)  then
      select S_LF_DEDUCTIONS_DISP.NEXTVAL into :new.ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_DEDUCTIONS_LIST_QUEUE
-- =============================================
--
create or replace trigger "TIG_LF_DEDUCTIONS_LIST_QUEUE"
before insert on LF_DEDUCTIONS_LIST
for each row
begin
    if (:new.ID is null)  then
      select S_LF_DEDUCTIONS_LIST.NEXTVAL into :new.ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_DEPPWDRECEIVER_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_DEPPWDRECEIVER_QUEUE"
before insert on LF_DEPPWDRECEIVER
for each row
  
begin
    if(:new.DPR_ID is null)
    then
      select S_LF_DEPPWDRECEIVER.NEXTVAL into :new.DPR_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_DEP_QUEUE
-- =================================
--
create or replace trigger "TIG_LF_DEP_QUEUE"
before insert on LF_DEP
for each row
  
begin
    if(:new.DEP_ID is null)
    then
    select S_LF_DEP.nextval into :new.DEP_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_DEP_RECHARGE_LOG_QUEUE
-- ==============================================
--
create or replace trigger "TIG_LF_DEP_RECHARGE_LOG_QUEUE"
before insert on LF_DEP_RECHARGE_LOG
for each row
  
begin
    if(:new.LOG_ID is null)
    then
      select S_LF_DEP_RECHARGE_LOG.NEXTVAL into :new.LOG_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_DEP_TAOCAN_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_DEP_TAOCAN_QUEUE"
before insert on LF_DEP_TAOCAN
for each row
begin
    if (:new.ID is null)  then
      select S_LF_DEP_TAOCAN.NEXTVAL into :new.ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_DEP_USER_BALANCE_QUEUE
-- ==============================================
--
create or replace trigger "TIG_LF_DEP_USER_BALANCE_QUEUE"
before insert on LF_DEP_USER_BALANCE
for each row
  
begin
    if(:new.BL_ID is null)
    then
      select S_LF_DEP_USER_BALANCE.NEXTVAL into :new.BL_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_DFADVANCED_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_DFADVANCED_QUEUE"
before insert on LF_DFADVANCED
for each row
begin
    if(:new.ID is null)
    then
      select S_LF_DFADVANCED.NEXTVAL into :new.ID from dual;
      end if;
end;
/

--
-- Creating trigger TIG_LF_EDIT_QUEUE
-- ==================================
--
create or replace trigger "TIG_LF_EDIT_QUEUE"
before insert on LF_EDIT
for each row
  
begin
    if(:new.EDIT_ID is null)
    then
      select S_LF_EDIT.NEXTVAL into :new.EDIT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_EMPDEP_CONN_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_EMPDEP_CONN_QUEUE"
before insert on LF_EMPDEP_CONN
for each row
 
begin
    if(:new.CONN_ID is null)
    then
    select S_LF_EMPDEP_CONN.NEXTVAL into :new.CONN_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_EMPLOYEE_DEP_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_EMPLOYEE_DEP_QUEUE"
before insert  on  LF_EMPLOYEE_DEP
for each row
  
begin
    if(:new.DEP_ID is null)
    then
      select S_LF_EMPLOYEE_DEP.NEXTVAL into :new.DEP_ID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_EMPLOYEE_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_EMPLOYEE_QUEUE"
before insert on LF_EMPLOYEE
for each row
 
begin
    if(:new.EMPLOYEEID is null)
    then
    select S_LF_EMPLOYEE.NEXTVAL into :new.EMPLOYEEID from dual;
    end if;
    end;
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
create or replace trigger "TIG_LF_ENTERPRISE_QUEUE"
before insert  on LF_ENTERPRISE
for each row
  
begin
    if(:new.OR_ID is null)
    then
      select S_LF_ENTERPRISE.NEXTVAL into :new.OR_ID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_EXAMINE_SMS_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_EXAMINE_SMS_QUEUE"
before insert on LF_EXAMINE_SMS
for each row
  
begin
    if(:new.ES_ID is null)
    then
      select S_LF_EXAMINE_SMS.NEXTVAL into :new.ES_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_EXTERNAL_USER_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_EXTERNAL_USER_QUEUE"
before insert on LF_EXTERNAL_USER
for each row
  
begin
    if(:new.EXUID is null)
    then
      select S_LF_EXTERNAL_USER.NEXTVAL into :new.EXUID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_FLOWBINDOBJ_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_FLOWBINDOBJ_QUEUE"
before insert on LF_FLOWBINDOBJ
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_FLOWBINDOBJ.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_FLOWBINDTYPE_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_FLOWBINDTYPE_QUEUE"
before insert on LF_FLOWBINDTYPE
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_FLOWBINDTYPE.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_FLOWRECORD
-- ==================================
--
create or replace trigger "TIG_LF_FLOWRECORD"
before insert on LF_FLOWRECORD
for each row

begin
    if(:new.FR_ID is null)
    then
      select S_LF_FLOWRECORD.NEXTVAL into :new.FR_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_GLOBAL_VARIABLE_QUEUE
-- =============================================
--
create or replace trigger "TIG_LF_GLOBAL_VARIABLE_QUEUE"
before insert on LF_GLOBAL_VARIABLE
for each row
 
begin
    if(:new.GLOBALID is null)
    then
    select S_LF_GLOBAL_VARIABLE.NEXTVAL into :new.GLOBALID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_IM_CLIENT_QUEUE
-- =======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_CLIENT_QUEUE" BEFORE
INSERT ON "LF_IM_CLIENT"
    FOR EACH ROW
begin
if (:new.CLIENTID is null) then
select S_LF_IM_CLIENT.nextval into :new.CLIENTID from dual;
end if;
end;
/

--
-- Creating trigger TIG_LF_IM_DIALOG_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_IM_DIALOG_QUEUE"
before insert on LF_IM_DIALOG
for each row
 
begin
    if(:new.DIAID is null)
    then
    select S_LF_IM_DIALOG.NEXTVAL into :new.DIAID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_IM_GPUSERLINK_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_IM_GPUSERLINK_QUEUE"
before insert on LF_IM_GPUSERLINK
for each row
  
begin
    if(:new.LINK_ID is null)
    then
      select S_LF_IM_GPUSERLINK.NEXTVAL into :new.LINK_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_IM_GROUPMESSAGE_QUEUE
-- =============================================
--
create or replace trigger "TIG_LF_IM_GROUPMESSAGE_QUEUE"
before insert on LF_IM_GROUPMESSAGE
for each row
  
begin
    if(:new.MSGID is null)
    then
      select S_LF_IM_GROUPMESSAGE.NEXTVAL into :new.MSGID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_IM_GROUP_QUEUE
-- ======================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_GROUP_QUEUE" BEFORE
INSERT ON "LF_IM_GROUP"
    FOR EACH ROW
begin
if (:new.GROUPID is null) then
select S_LF_IM_GROUP.nextval into :new.GROUPID from dual;
end if;
end;
/

--
-- Creating trigger TIG_LF_IM_HISTORY_QUEUE
-- ========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_HISTORY_QUEUE" BEFORE
INSERT ON "LF_IM_HISTORY"
    FOR EACH ROW
begin
if (:new.HISTORYID is null) then
select S_LF_IM_HISTORY.nextval into :new.HISTORYID from dual;
end if;
end;
/

--
-- Creating trigger TIG_LF_IM_MEMBERGROUP_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_IM_MEMBERGROUP_QUEUE"
before insert on LF_IM_MEMBERGROUP
for each row
 
begin
    if(:new.MEMBERID is null)
    then
    select S_LF_IM_MEMBERGROUP.NEXTVAL into :new.MEMBERID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_IM_OFFICEGROUP_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_IM_OFFICEGROUP_QUEUE"
before insert on LF_IM_OFFICEGROUP
for each row
 
begin
    if(:new.GROUPID is null)
    then
    select S_LF_IM_OFFICEGROUP.NEXTVAL into :new.GROUPID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_IM_RECENTCONTACTS_QUEUE
-- ===============================================
--
create or replace trigger "TIG_LF_IM_RECENTCONTACTS_QUEUE"
before insert on LF_IM_RECENTCONTACTS
for each row
 
begin
    if(:new.RC_ID is null)
    then
    select S_LF_IM_RECENTCONTACTS.NEXTVAL into :new.RC_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_IM_SEATGROUP_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEATGROUP_QUEUE" BEFORE
INSERT ON "LF_IM_SEATGROUP"
    FOR EACH ROW
begin
if (:new.SEATGROUPID is null) then
select S_LF_IM_SEATGROUP.nextval into :new.SEATGROUPID from dual;
end if;
end;
/

--
-- Creating trigger TIG_LF_IM_SEATLIMIT_QUEUE
-- ==========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEATLIMIT_QUEUE" BEFORE
INSERT ON "LF_IM_SEATLIMIT"
    FOR EACH ROW
begin
if (:new.ID is null) then
select S_LF_IM_SEATLIMIT.nextval into :new.ID from dual;
end if;
end;
/

--
-- Creating trigger TIG_LF_IM_SEATUSER_QUEUE
-- =========================================
--
CREATE OR REPLACE TRIGGER "TIG_LF_IM_SEATUSER_QUEUE" BEFORE
INSERT ON "LF_IM_SEATUSER"
    FOR EACH ROW
begin
if (:new.ID is null) then
select S_LF_IM_SEATUSER.nextval into :new.ID from dual;
end if;
end;
/

--
-- Creating trigger TIG_LF_IM_SEAT_CGLINK_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_IM_SEAT_CGLINK_QUEUE"
before insert on LF_IM_SEAT_CGLINK
for each row
  
begin
    if(:new.CGLINK_ID is null)
    then
      select S_LF_IM_SEAT_CGLINK.NEXTVAL into :new.CGLINK_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_IM_SEAT_CLIENTGP_QUEUE
-- ==============================================
--
create or replace trigger "TIG_LF_IM_SEAT_CLIENTGP_QUEUE"
before insert on LF_IM_SEAT_CLIENTGP
for each row
  
begin
    if(:new.CGID is null)
    then
      select S_LF_IM_SEAT_CLIENTGP.NEXTVAL into :new.CGID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_IM_SEAT_CLIENT_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_IM_SEAT_CLIENT_QUEUE"
before insert on LF_IM_SEAT_CLIENT
for each row
  
begin
    if(:new.CLIENT_ID is null)
    then
      select S_LF_IM_SEAT_CLIENT.NEXTVAL into :new.CLIENT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_IM_USERMESSAGE_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_IM_USERMESSAGE_QUEUE"
before insert on LF_IM_USERMESSAGE
for each row
  
begin
    if(:new.MSGID is null)
    then
      select S_LF_IM_USERMESSAGE.NEXTVAL into :new.MSGID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_IM_USERSUBNO_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_IM_USERSUBNO_QUEUE"
before insert on LF_IM_USERSUBNO
for each row
 
begin
    if(:new.IMSUBNO_ID is null)
    then
    select S_LF_IM_USERSUBNO.NEXTVAL into :new.IMSUBNO_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_KEYWORDS_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_KEYWORDS_QUEUE"
before insert on LF_KEYWORDS
for each row
  
begin
    if(:new.KW_ID is null)
    then
      select S_LF_KEYWORDS.NEXTVAL into :new.KW_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_KEY_VALUE_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_KEY_VALUE_QUEUE"
before insert on LF_KEY_VALUE
for each row
  
begin
    if(:new.KV_ID is null)
    then
      select S_LF_KEY_VALUE.NEXTVAL into :new.KV_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_LIST2GRO_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_LIST2GRO_QUEUE"
before insert on LF_LIST2GRO
for each row
  
begin
    if(:new.L2G_ID is null)
    then
      select S_LF_LIST2GRO.NEXTVAL into :new.L2G_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MACIP_QUEUE
-- ===================================
--
create or replace trigger "TIG_LF_MACIP_QUEUE"
before insert on LF_MACIP
for each row
  
begin
    if(:new.LMIID is null)
    then
      select S_LF_MACIP.NEXTVAL into :new.LMIID from dual;
      end if;
end;
/

--
-- Creating trigger TIG_LF_MALIST_QUEUE
-- ====================================
--
create or replace trigger "TIG_LF_MALIST_QUEUE"
before insert on LF_MALIST
for each row
  
begin
    if(:new.MA_ID is null)
    then
      select S_LF_MALIST.NEXTVAL INTO :new.MA_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MAPPING_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_MAPPING_QUEUE"
before insert on LF_MAPPING
for each row
  
begin
    if(:new.MAP_ID is null)
    then
    select S_LF_MAPPING.nextval into :new.MAP_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_MATERIAL_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_MATERIAL_QUEUE"
before insert on LF_MATERIAL
for each row
  
begin
    if(:new.MTAL_ID is null)
    then
      select S_LF_MATERIAL.NEXTVAL into :new.MTAL_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MATERIAL_SORT_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_MATERIAL_SORT_QUEUE"
before insert on LF_MATERIAL_SORT
for each row
  
begin
    if(:new.SORT_ID is null)
    then
      select S_LF_MATERIAL_SORT.NEXTVAL into :new.SORT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MMSACCBIND_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_MMSACCBIND_QUEUE"
before insert on LF_MMSACCBIND
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MMSACCBIND.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MMSACCMANAGEMENT_QUEUE
-- ==============================================
--
create or replace trigger "TIG_LF_MMSACCMANAGEMENT_QUEUE"
before insert on LF_MMSACCMANAGEMENT
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MMSACCMANAGEMENT.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MMSBLIST_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_MMSBLIST_QUEUE"
before insert on LF_MMSBLIST
for each row
  
begin
    if(:new.BL_ID is null)
    then
      select S_LF_MMSBLIST.NEXTVAL into :new.BL_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MMSINFO_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_MMSINFO_QUEUE"
before insert  on LF_MMSINFO
for each row
  
begin
    if(:new.MMSID is null)
    then
      select S_LF_MMSINFO.NEXTVAL into :new.MMSID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_MMSMTTASK_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_MMSMTTASK_QUEUE"
before insert on LF_MMSMTTASK
for each row
  
begin
    if(:new.MMS_ID is null)
    then
      select S_LF_MMSMTTASK.NEXTVAL into :new.MMS_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MMSTEMPLATE_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_MMSTEMPLATE_QUEUE"
before insert on LF_MMSTEMPLATE
for each row
  
begin
    if(:new.MMS_ID is null)
    then
      select S_LF_MMSTEMPLATE.NEXTVAL into :new.MMS_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MMS_MTREPORT_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_MMS_MTREPORT_QUEUE"
before insert on LF_MMS_MTREPORT
for each row
  
begin
  if(:new.ID is null)
  then
    select S_LF_MMS_MTREPORT.NEXTVAL into :new.ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_MOBFINANCIAL_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_MOBFINANCIAL_QUEUE"
before insert on LF_MOBFINANCIAL
for each row
  
begin
    if(:new.MOBFID is null)
    then
      select S_LF_MOBFINANCIAL.NEXTVAL into :new.MOBFID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_DGTACINFO_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_MON_DGTACINFO_QUEUE"
before insert on LF_MON_DGTACINFO
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_DGTACINFO.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_DHOST_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_MON_DHOST_QUEUE"
before insert on LF_MON_DHOST
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_DHOST.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_DPROCE_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_MON_DPROCE_QUEUE"
before insert on LF_MON_DPROCE
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_DPROCE.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_DSPACINFO_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_MON_DSPACINFO_QUEUE"
before insert on LF_MON_DSPACINFO
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_DSPACINFO.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_ERRHIS_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_MON_ERRHIS_QUEUE"
before insert on LF_MON_ERRHIS
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_ERRHIS.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_ERR_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_MON_ERR_QUEUE"
before insert on LF_MON_ERR
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_ERR.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_ONLCFG_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_MON_ONLCFG_QUEUE"
before insert on LF_MON_ONLCFG
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_ONLCFG.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_SHOST_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_MON_SHOST_QUEUE"
before insert on LF_MON_SHOST
for each row
  
begin
    if(:new.HOSTID is null)
    then
      select S_LF_MON_SHOST.NEXTVAL into :new.HOSTID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_SPGATEBUF_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_MON_SPGATEBUF_QUEUE"
before insert on LF_MON_SPGATEBUF
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_SPGATEBUF.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_SPROCE_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_MON_SPROCE_QUEUE"
before insert on LF_MON_SPROCE
for each row
  
begin
    if(:new.ProceID is null)
    then
      select S_LF_MON_SPROCE.NEXTVAL into :new.ProceID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MON_WBSBUF_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_MON_WBSBUF_QUEUE"
before insert on LF_MON_WBSBUF
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MON_WBSBUF.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MOTASK_QUEUE
-- ====================================
--
create or replace trigger TIG_LF_MOTASK_QUEUE
before insert on LF_MOTASK
for each row
  
begin
    if(:new.MO_ID is null)
    then
    select S_LF_MOTASK.NEXTVAL into :new.MO_ID FROM dual;
    end if;
  end;
/

--
-- Creating trigger TIG_LF_MO_SERVICE
-- ==================================
--
create or replace trigger "TIG_LF_MO_SERVICE"
before insert  on LF_MO_SERVICE
for each row
  
begin
    if(:new.MS_ID is null)
    then
      select S_LF_MO_SERVICE.NEXTVAL into :new.MS_ID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_MTRPT_EC_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_MTRPT_EC_QUEUE"
before insert on LF_MTRPT_EC
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_MTRPT_EC.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_MTTASK_QUEUE
-- ====================================
--
create or replace trigger "TIG_LF_MTTASK_QUEUE"
before insert on LF_MTTASK
for each row
  
begin
    if(:new.MT_ID is null)
    then
      select S_LF_MTTASK.NEXTVAL into :new.MT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_NOTICE_QUEUE
-- ====================================
--
create or replace trigger "TIG_LF_NOTICE_QUEUE"
before insert on LF_NOTICE
for each row
  
begin
    if(:new.NOTICE_ID is null)
    then
      select S_LF_NOTICE.NEXTVAL into :new.NOTICE_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_ONLINE_USER_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_ONLINE_USER_QUEUE"
before insert on LF_ONLINE_USER
for each row
  
begin
    if(:new.ONUID is null)
    then
      select S_LF_ONLINE_USER.NEXTVAL into :new.ONUID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_OPERATE_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_OPERATE_QUEUE"
before insert  on LF_OPERATE
for each row
  
begin
    if(:new.OPERATE_ID is null)
    then
      select S_LF_OPERATE.NEXTVAL into :new.OPERATE_ID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_OPRATELOG_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_OPRATELOG_QUEUE"
before insert on LF_OPRATELOG
for each row
  
begin
    if(:new.LOG_ID is null)
    then
      select S_LF_OPRATELOG.NEXTVAL INTO :new.LOG_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PARAM_QUEUE
-- ===================================
--
create or replace trigger "TIG_LF_PARAM_QUEUE"
before insert on LF_PARAM
for each row
  
begin
    if(:new.PARAM_ID is null)
    then
      select S_LF_PARAM.NEXTVAL into :new.PARAM_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PERFECT_NOTIC_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_PERFECT_NOTIC_QUEUE"
before insert on LF_PERFECT_NOTIC
for each row
  
begin
    if(:new.P_NOTIC_ID is null)
    then
      select S_LF_PERFECT_NOTIC.NEXTVAL INTO :new.P_NOTIC_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PERSONALCONFIG_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_PERSONALCONFIG_QUEUE"
before insert on LF_PERSONALCONFIG
for each row
 
begin
    if(:new.PER_ID is null)
    then
    select S_LF_PERSONALCONFIG.NEXTVAL into :new.PER_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_POSITION_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_POSITION_QUEUE"
before insert on LF_POSITION
for each row
  
begin
    if(:new.P_ID is null)
    then
      select S_LF_POSITION.NEXTVAL into :new.P_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PRIVILEGE_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_PRIVILEGE_QUEUE"
before insert on LF_PRIVILEGE
for each row
 
begin
    if(:new.PRIVILEGE_ID is null)
    then
    select S_LF_PRIVILEGE.NEXTVAL into :new.PRIVILEGE_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_PROCESS_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_PROCESS_QUEUE"
before insert on LF_PROCESS
for each row
  
begin
    if(:new.PR_ID is null)
    then
      select S_LF_PROCESS.NEXTVAL into :new.PR_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PROCHARGES_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_PROCHARGES_QUEUE"
before insert on LF_PROCHARGES
for each row
begin
    if (:new.RULE_ID is null)  then
      select S_LF_PROCHARGES.NEXTVAL into :new.RULE_ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_PRODUCTOR_CONT_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_PRODUCTOR_CONT_QUEUE"
before insert on LF_PRODUCTOR_CONT
for each row
  
begin
    if(:new.IDEN_ID is null)
    then
      select S_LF_PRODUCTOR_CONT.NEXTVAL into :new.IDEN_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PRODUCTS_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_PRODUCTS_QUEUE"
before insert on LF_PRODUCTS
for each row
  
begin
    if(:new.PRO_ID is null)
    then
      select S_LF_PRODUCTS.NEXTVAL into :new.PRO_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PROSENDCOUNT_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_PROSENDCOUNT_QUEUE"
before insert on LF_PROSENDCOUNT
for each row
  
begin
    if(:new.CID is null)
    then
      select S_LF_PROSENDCOUNT.NEXTVAL into :new.CID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_PRO_CON_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_PRO_CON_QUEUE"
before insert on LF_PRO_CON
for each row
  
begin
    if(:new.DBCON_ID is null)
    then
      select S_LF_PRO_CON.NEXTVAL into :new.DBCON_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_P_N_UP_QUEUE
-- ====================================
--
create or replace trigger "TIG_LF_P_N_UP_QUEUE"
before insert on LF_P_N_UP
for each row
  
begin
    if(:new.PNUP_ID is null)
    then
      select S_LF_P_N_UP.NEXTVAL into :new.PNUP_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_REPLY_QUEUE
-- ===================================
--
create or replace trigger "TIG_LF_REPLY_QUEUE"
before insert on LF_REPLY
for each row
  
begin
    if(:new.RE_ID is null)
    then
      select S_LF_REPLY.NEXTVAL into :new.RE_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_RESOURCE_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_RESOURCE_QUEUE"
before insert on LF_RESOURCE
for each row
  
begin
    if(:new.RESOURCE_ID  is null)
    then
      select S_LF_RESOURCE.NEXTVAL INTO :new.RESOURCE_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_REVIEWER2LEVEL_QUEUE
-- ============================================
--
create or replace trigger "TIG_LF_REVIEWER2LEVEL_QUEUE"
before insert on LF_REVIEWER2LEVEL
for each row
  
begin
    if(:new.FRL_ID is null)
    then
      select S_LF_REVIEWER2LEVEL.NEXTVAL into :new.FRL_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_REVIEWSWITCH_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_REVIEWSWITCH_QUEUE"
before insert on LF_REVIEWSWITCH
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_REVIEWSWITCH.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_ROLES_QUEUE
-- ===================================
--
create or replace trigger "TIG_LF_ROLES_QUEUE"
before insert on LF_ROLES
for each row
  
begin
    if(:new.ROLE_ID is null)
    then
      select S_LF_ROLES.NEXTVAL into :new.ROLE_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SENDGRO_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_SENDGRO_QUEUE"
before insert on LF_SENDGRO
for each row
  
begin
    if(:new.SG_ID is null)
    then
      select S_LF_SENDGRO.Nextval into :new.SG_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SERVICELOG_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_SERVICELOG_QUEUE"
before insert on LF_SERVICELOG
for each row
  
begin
    if(:new.SL_ID is null)
    then
      select S_LF_SERVICELOG.NEXTVAL INTO :new.SL_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SERVICEMSG_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_SERVICEMSG_QUEUE"
before insert on LF_SERVICEMSG
for each row
  
begin
    if(:new.SERMSGID is null)
    then
      select S_LF_SERVICEMSG.NEXTVAL into :new.SERMSGID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SERVICE_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_SERVICE_QUEUE"
before insert on LF_SERVICE
for each row
  
begin
  if(:new.SER_ID is null)
  then
    select S_LF_SERVICE.NEXTVAL into :new.SER_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_SKIN_QUEUE
-- ==================================
--
create or replace trigger "TIG_LF_SKIN_QUEUE"
before insert on LF_SKIN
for each row
  
begin
    if(:new.SID is null)
    then
      select S_LF_SKIN.NEXTVAL into :new.SID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SPFEE_QUEUE
-- ===================================
--
create or replace trigger "TIG_LF_SPFEE_QUEUE"
before insert on LF_SPFEE
for each row
  
begin
    if(:new.SF_ID is null)
    then
      select S_LF_SPFEE.NEXTVAL into :new.SF_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SP_DEP_BIND_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_SP_DEP_BIND_QUEUE"
before insert on LF_SP_DEP_BIND
for each row
 
begin
    if(:new.DSG_ID is null)
    then
    select S_LF_SP_DEP_BIND.NEXTVAL into :new.DSG_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_SQLWHERE_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_SQLWHERE_QUEUE"
before insert on LF_SQLWHERE
for each row
  
begin
    if(:new.WH_ID is null)
    then
      select S_LF_SQLWHERE.Nextval into :new.WH_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SUBNOALLOT_DETAIL_QUEUE
-- ===============================================
--
create or replace trigger "TIG_LF_SUBNOALLOT_DETAIL_QUEUE"
before insert on LF_SUBNOALLOT_DETAIL
for each row
 
begin
    if(:new.SUDID is null)
    then
    select S_LF_SUBNOALLOT_DETAIL.NEXTVAL into :new.SUDID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_SUBNOALLOT_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_SUBNOALLOT_QUEUE"
before insert on LF_SUBNOALLOT
for each row
 
begin
    if(:new.SUID is null)
    then
    select S_LF_SUBNOALLOT.NEXTVAL into :new.SUID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_SURVEYTASK_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_SURVEYTASK_QUEUE"
before insert on LF_SURVEYTASK
for each row
  
begin
    if(:new.IDEN_ID is null)
    then
      select S_LF_SURVEYTASK.NEXTVAL into :new.IDEN_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SURVEY_QUEUE
-- ====================================
--
create or replace trigger "TIG_LF_SURVEY_QUEUE"
before insert on LF_SURVEY
for each row
  
begin
    if(:new.SUV_ID is null)
    then
      select S_LF_SURVEY.NEXTVAL into :new.SUV_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SYSMTTASK_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_SYSMTTASK_QUEUE"
before insert on LF_SYSMTTASK
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_SYSMTTASK.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_SYS_PARAM_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_SYS_PARAM_QUEUE"
before insert on LF_SYS_PARAM
for each row
  
begin
    if(:new.PARAM_ID is null)
    then
      select S_LF_SYS_PARAM.NEXTVAL into :new.PARAM_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_TAOCAN_CMD_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_TAOCAN_CMD_QUEUE"
before insert on LF_TAOCAN_CMD
for each row
begin
    if (:new.ID is null)  then
      select S_LF_TAOCAN_CMD.NEXTVAL into :new.ID from dual;
    end if;
end;
/

--
-- Creating trigger TIG_LF_TASK_QUEUE
-- ==================================
--
create or replace trigger "TIG_LF_TASK_QUEUE"
before insert on LF_TASK
for each row
  
begin
    if(:new.TAID is null)
    then
      select S_LF_TASK.NEXTVAL into :new.TAID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_TEMPLATE_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_TEMPLATE_QUEUE"
before insert on LF_TEMPLATE
for each row
  
begin
    if(:new.TM_ID is null)
    then
      select S_LF_TEMPLATE.NEXTVAL into :new.TM_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_THEME_QUEUE
-- ===================================
--
create or replace trigger "TIG_LF_THEME_QUEUE"
before insert on LF_THEME
for each row
  
begin
    if(:new.TID is null)
    then
      select S_LF_THEME.NEXTVAL into :new.TID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_THIR_MENUCONTROL_QUEUE
-- ==============================================
--
create or replace trigger "TIG_LF_THIR_MENUCONTROL_QUEUE"
before insert on LF_THIR_MENUCONTROL
for each row
  
begin
    if(:new.TID is null)
    then
      select S_LF_THIR_MENUCONTROL.NEXTVAL into :new.TID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_TIMER_HISTORY_QUEUE
-- ===========================================
--
create or replace trigger TIG_LF_TIMER_HISTORY_QUEUE
before insert on LF_TIMER_HISTORY
for each row
  
begin
    if(:new.TA_ID is null)
    then
      select S_LF_TIMER_HISTORY.NEXTVAL into :new.TA_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_TIMER_QUEUE
-- ===================================
--
create or replace trigger TIG_LF_TIMER_QUEUE
before insert on LF_TIMER
for each row
  
begin
    if(:new.TIMER_TASK_ID is null)
    then
      select S_LF_TIMER.NEXTVAL into :new.TIMER_TASK_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_TMPLRELA_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_TMPLRELA_QUEUE"
before insert on LF_TMPLRELA
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_TMPLRELA.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_TRUCTTYPE_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_TRUCTTYPE_QUEUE"
before insert on LF_TRUCTTYPE
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_TRUCTTYPE.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_UDGROUP_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_UDGROUP_QUEUE"
before insert on LF_UDGROUP
for each row
  
begin
    if(:new.UDG_ID is null)
    then
      select S_LF_UDGROUP.Nextval into :new.UDG_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_USEDSUBNO_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_USEDSUBNO_QUEUE"
before insert on LF_USEDSUBNO
for each row
  
begin
    if(:new.SUBNO_ID is null)
    then
      select S_LF_USEDSUBNO.NEXTVAL into :new.SUBNO_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_USER2SKIN_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_USER2SKIN_QUEUE"
before insert on LF_USER2SKIN
for each row
  
begin
    if(:new.USID is null)
    then
      select S_LF_USER2SKIN.NEXTVAL into :new.USID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_USERPARA_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_USERPARA_QUEUE"
before insert on LF_USERPARA
for each row
  
begin
    if(:new.PARA_ID is null)
    then
      select S_LF_USERPARA.NEXTVAL INTO :new.PARA_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_USER_RPT_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_USER_RPT_QUEUE"
before insert on LF_USER_RPT
for each row
  
begin
    if(:new.IDEN_ID is null)
    then
    select S_LF_USER_RPT.nextval into :new.IDEN_ID from dual;
    end if;
    end;
/

--
-- Creating trigger TIG_LF_USER_SETTING_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_USER_SETTING_QUEUE"
before insert on LF_USER_SETTING
for each row
  
begin
    if(:new.IDEN_ID is null)
    then
      select S_LF_USER_SETTING.NEXTVAL into :new.IDEN_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_VERSION_EMPDB
-- =====================================
--
create or replace trigger "TIG_LF_VERSION_EMPDB"
before insert  on LF_VERSION_EMPDB
for each row
  
begin
    if(:new.DVID is null)
    then
      select S_LF_VERSION_EMPDB.NEXTVAL into :new.DVID from dual;
     end if;
     end;
/

--
-- Creating trigger TIG_LF_VODINFO_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_VODINFO_QUEUE"
before insert on LF_VODINFO
for each row
  
begin
    if(:new.VOD_ID is null)
    then
      select S_LF_VODINFO.NEXTVAL into :new.VOD_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_VODMOREC_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_VODMOREC_QUEUE"
before insert on LF_VODMOREC
for each row
  
begin
    if(:new.REC_ID is null)
    then
      select S_LF_VODMOREC.NEXTVAL into :new.REC_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_VODMTREC_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_VODMTREC_QUEUE"
before insert on LF_VODMTREC
for each row
  
begin
    if(:new.REC_ID is null)
    then
      select S_LF_VODMTREC.NEXTVAL into :new.REC_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WC_REVENT_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_WC_REVENT_QUEUE"
before insert on LF_WC_REVENT
for each row
  
begin
    if(:new.EVT_ID is null)
    then
      select S_LF_WC_REVENT.NEXTVAL into :new.EVT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WC_RTEXT_QUEUE
-- ======================================
--
create or replace trigger "TIG_LF_WC_RTEXT_QUEUE"
before insert on LF_WC_RTEXT
for each row
  
begin
    if(:new.TET_ID is null)
    then
      select S_LF_WC_RTEXT.NEXTVAL into :new.TET_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WC_VERIFY_QUEUE
-- =======================================
--
create or replace trigger "TIG_LF_WC_VERIFY_QUEUE"
before insert on LF_WC_VERIFY
for each row
  
begin
    if(:new.VF_ID is null)
    then
      select S_LF_WC_VERIFY.NEXTVAL into :new.VF_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WEIX_EMOJI_QUEUE
-- ========================================
--
create or replace trigger "TIG_LF_WEIX_EMOJI_QUEUE"
before insert on LF_WEIX_EMOJI
for each row
  
begin
    if(:new.ID is null)
    then
      SELECT S_LF_WEIX_EMOJI.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WGPARAMCONFIG_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_WGPARAMCONFIG_QUEUE"
before insert on LF_WGPARAMCONFIG
for each row
  
begin
    if(:new.PID is null)
    then
      select S_LF_WGPARAMCONFIG.NEXTVAL into :new.PID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WGPARAMDEFINITION_QUEUE
-- ===============================================
--
create or replace trigger "TIG_LF_WGPARAMDEFINITION_QUEUE"
before insert on LF_WGPARAMDEFINITION
for each row
  
begin
    if(:new.PID is null)
    then
      select S_LF_WGPARAMDEFINITION.NEXTVAL into :new.PID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WX_BASEINFO_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_WX_BASEINFO_QUEUE"
before insert on LF_WX_BASEINFO
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_WX_BASEINFO.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WX_DATATYPE_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_WX_DATATYPE_QUEUE"
before insert on LF_WX_DATATYPE
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_WX_DATATYPE.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WX_PAGE_QUEUE
-- =====================================
--
create or replace trigger "TIG_LF_WX_PAGE_QUEUE"
before insert on LF_WX_PAGE
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_WX_PAGE.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WX_SENDCOUNT_QUEUE
-- ==========================================
--
create or replace trigger "TIG_LF_WX_SENDCOUNT_QUEUE"
before insert on LF_WX_SENDCOUNT
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_WX_SENDCOUNT.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WX_UPLOADFILE_QUEUE
-- ===========================================
--
create or replace trigger "TIG_LF_WX_UPLOADFILE_QUEUE"
before insert on LF_WX_UPLOADFILE
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_WX_UPLOADFILE.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_LF_WX_VISITLOG_QUEUE
-- =========================================
--
create or replace trigger "TIG_LF_WX_VISITLOG_QUEUE"
before insert on LF_WX_VISITLOG
for each row
  
begin
    if(:new.ID is null)
    then
      select S_LF_WX_VISITLOG.NEXTVAL into :new.ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_LBS_PIOS_QUEUE
-- ======================================
--
create or replace trigger "TIG_OT_LBS_PIOS_QUEUE"
before insert on OT_LBS_PIOS
for each row
  
begin
    if(:new.P_ID is null)
    then
      select S_OT_LBS_PIOS.NEXTVAL into :new.P_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_LBS_PUSHSET_QUEUE
-- =========================================
--
create or replace trigger "TIG_OT_LBS_PUSHSET_QUEUE"
before insert on OT_LBS_PUSHSET
for each row
  
begin
    if(:new.PUSH_ID is null)
    then
      select S_OT_LBS_PUSHSET.NEXTVAL into :new.PUSH_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_LBS_USERPIOS_QUEUE
-- ==========================================
--
create or replace trigger "TIG_OT_LBS_USERPIOS_QUEUE"
before insert on OT_LBS_USERPIOS
for each row
  
begin
    if(:new.UP_ID is null)
    then
      select S_OT_LBS_USERPIOS.NEXTVAL into :new.UP_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_ONL_MSG_HIS_QUEUE
-- =========================================
--
create or replace trigger "TIG_OT_ONL_MSG_HIS_QUEUE"
before insert on OT_ONL_MSG_HIS
for each row
  
begin
    if(:new.M_ID is null)
    then
      select S_OT_ONL_MSG_HIS.NEXTVAL into :new.M_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_ONL_SERVER_QUEUE
-- ========================================
--
create or replace trigger "TIG_OT_ONL_SERVER_QUEUE"
before insert on OT_ONL_SERVER
for each row
  
begin
    if(:new.SER_ID is null)
    then
      select S_OT_ONL_SERVER.NEXTVAL into :new.SER_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_SIT_PLANT_QUEUE
-- =======================================
--
create or replace trigger "TIG_OT_SIT_PLANT_QUEUE"
before insert on OT_SIT_PLANT
for each row
  
begin
    if(:new.PLANT_ID is null)
    then
      select S_OT_SIT_PLANT.NEXTVAL into :new.PLANT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_SIT_TYPE_QUEUE
-- ======================================
--
create or replace trigger "TIG_OT_SIT_TYPE_QUEUE"
before insert on OT_SIT_TYPE
for each row
  
begin
    if(:new.TYPE_ID is null)
    then
      select S_OT_SIT_TYPE.NEXTVAL into :new.TYPE_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_WEI_COUNT_QUEUE
-- =======================================
--
create or replace trigger "TIG_OT_WEI_COUNT_QUEUE"
before insert on OT_WEI_COUNT
for each row
  
begin
    if(:new.COUNT_ID is null)
    then
      select S_OT_WEI_COUNT.NEXTVAL into :new.COUNT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_WEI_REVENT_QUEUE
-- ========================================
--
create or replace trigger "TIG_OT_WEI_REVENT_QUEUE"
before insert on OT_WEI_REVENT
for each row
  
begin
    if(:new.EVT_ID is null)
    then
      select S_OT_WEI_REVENT.NEXTVAL into :new.EVT_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_WEI_RTEXT_QUEUE
-- =======================================
--
create or replace trigger "TIG_OT_WEI_RTEXT_QUEUE"
before insert on OT_WEI_RTEXT
for each row
  
begin
    if(:new.TET_ID is null)
    then
      select S_OT_WEI_RTEXT.NEXTVAL into :new.TET_ID from dual;
      end if;
      end;
/

--
-- Creating trigger TIG_OT_WEI_SENDLOG_QUEUE
-- =========================================
--
create or replace trigger "TIG_OT_WEI_SENDLOG_QUEUE"
before insert on OT_WEI_SENDLOG
for each row
  
begin
    if(:new.SEND_ID is null)
    then
      select S_OT_WEI_SENDLOG.NEXTVAL into :new.SEND_ID from dual;
      end if;
      end;
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
create or replace trigger "TIG_S_LF_BUS_PACKAGE_QUEUE"
before insert on LF_BUS_PACKAGE
for each row
begin
    if (:new.PACKAGE_ID is null)  then
      select S_LF_BUS_PACKAGE.NEXTVAL into :new.PACKAGE_ID from dual;
    end if;
end;
/

--
-- Creating trigger TRIGGER_RECONCILIATION
-- =======================================
--
create or replace trigger TRIGGER_RECONCILIATION
  before insert on lf_reconciliation
  for each row
declare
  nextid NUMBER;
begin
   IF :new.ID IS NULL or :new.ID=0 THEN --DepartId是列名
    select S_LF_RECONCILIATION.nextval into nextid from sys.dual;
    :new.ID:=nextid;
   END IF;
end TRIGGER_RECONCILIATION;
/



SELECT SEQ_USERDATA.NEXTVAL from dual;



create or replace trigger TIG_MMS_TEMPLATE1
  before update on mms_template
  for each row
declare
begin
  if(:OLD.submitstatus<>:NEW.submitstatus) THEN
              UPDATE LF_TEMPLATE SET submitstatus=:NEW.submitstatus,error_code=:NEW.errcode where EMP_TEMPLID=:NEW.Emp_tmplid;             
  end if;
    IF (:OLD.AUDITSTATUS<>:NEW.AUDITSTATUS)
    THEN
       UPDATE LF_TEMPLATE SET AUDITSTATUS=:NEW.AUDITSTATUS,submitstatus=:NEW.submitstatus,error_code=:NEW.errcode,SP_TEMPLID=:NEW.TmplId
          where EMP_TEMPLID=:NEW.Emp_tmplid;
    END IF;
end;
/

create or replace trigger INSERT_LF_MTTASK_TRIG
  after INSERT on BATCH_MT_REQ
  for each row
declare
  MOBLIETYPE NUMBER;
  UU NUMBER;
  UCOUNT NUMBER;
  TIMESTAMP1 TIMESTAMP;
  MSGLEN NUMBER;--内容长度
  QMLEN NUMBER;--签名长度
  TIMERSTATUS NUMBER;
begin
  TIMESTAMP1:=:NEW.RECVTIME;
  IF :NEW.TASKTYPE = 2  THEN
     if :NEW.SENDTYPE > 1 then
         MOBLIETYPE:=2;--动态短信
     elsif :NEW.SENDTYPE = 1 then
       MSGLEN := nvl(length(:NEW.MSG),0);
       select count(SIGNLEN) into UCOUNT from xt_gate_queue where spgate in (select spgate from gt_port_used where userid =:NEW.userid);
       if UCOUNT=0 then
          QMLEN:=0;
       else
          select nvl(SIGNLEN,0) into QMLEN from xt_gate_queue where spgate in (select spgate from gt_port_used where userid =:NEW.userid) and rownum=1;
          UCOUNT:=0;
       end if;
       
       if MSGLEN + QMLEN >70 then
         MOBLIETYPE := 1;--长短信
       else
         MOBLIETYPE := 0;--短信
       end if;
     end if;

     select count(USER_ID) into UCOUNT from LF_SYSUSER where USER_CODE=:NEW.P1;
     if UCOUNT=0 then
         UU:=0;
     else
        select nvl(USER_ID,0) into UU from LF_SYSUSER where USER_CODE=:NEW.P1;
     end if;

      if :NEW.ATTIME<>'              ' then
         TIMESTAMP1:=to_date(:NEW.ATTIME,'YYYY-MM-DD HH24:MI:SS');
         TIMERSTATUS:=1;
      else
         TIMERSTATUS:=0;
      end if;
       INSERT INTO LF_MTTASK(USER_ID,TASKID,MSG,SP_USER,TITLE,TASKTYPE,BMTTYPE,SENDSTATE,SUB_STATE,RE_STATE,TIMER_TIME,BUS_CODE,SUB_COUNT,EFF_COUNT,MOBILE_URL,SUBMITTIME,MOBILE_TYPE,MS_TYPE,TIMER_STATUS,BATCHID,ISRETRY)
       values(UU,:NEW.TASKID,:NEW.MSG,:NEW.USERID,:NEW.TITLE,:NEW.TASKTYPE,:NEW.SENDTYPE,0,2,1,TIMESTAMP1,:NEW.SVRTYPE,:NEW.TOTALNUM,:NEW.TOTALNUM, :NEW.REMOTEURL,:NEW.RECVTIME,MOBLIETYPE,1,TIMERSTATUS,:NEW.BATCHID,0);
  END IF;
END  INSERT_LF_MTTASK_TRIG;
/


CREATE OR REPLACE TRIGGER UPDATETIME_LF_MTTASK_TRIG
  AFTER INSERT on BATCH_MT_REQ_HIS
  for each row
declare
TIMESTAMP1 TIMESTAMP;
begin
  if :NEW.ERRORCODE = 'ACCEPTD' THEN
     TIMESTAMP1:=:NEW.RECVTIME;
	   if :NEW.ATTIME<>'              ' then
          TIMESTAMP1:=to_date(:NEW.ATTIME,'YYYY-MM-DD HH24:MI:SS');
     end if;
    if :NEW.TASKTYPE=1 then
    UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE TASKID=:NEW.TASKID;
    else
     UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE BATCHID=:NEW.BATCHID;
    end if;
  end if;
end UPDATETIME_LF_MTTASK_TRIG;
/

CREATE OR REPLACE TRIGGER UPDATETIME_LF_MTTASK_TRIG1
  AFTER UPDATE on BATCH_MT_REQ_HIS
  for each row
declare
TIMESTAMP1 TIMESTAMP;
begin
  if :NEW.ERRORCODE = 'ACCEPTD' THEN
     TIMESTAMP1:=:NEW.RECVTIME;
	   if :NEW.ATTIME<>'              ' then
         TIMESTAMP1:=to_date(:NEW.ATTIME,'YYYY-MM-DD HH24:MI:SS');
     end if;
    if :NEW.TASKTYPE=1 then
    UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE TASKID=:NEW.TASKID;
    else
     UPDATE LF_MTTASK SET SENDSTATE=3, STARTSENDTIME=TIMESTAMP1,ENDSENDTIME=:NEW.SENDTIME WHERE BATCHID=:NEW.BATCHID;
    end if;
  end if;
end UPDATETIME_LF_MTTASK_TRIG1;
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



--增加版本信息
DECLARE COUNTNUM INT;
        DBVERSIONSTR VARCHAR2(32);
        NUMNO INT;
        TOTALINT INT;
BEGIN
    DBVERSIONSTR:='6.36';
    NUMNO:=3;
    TOTALINT:=4;
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
