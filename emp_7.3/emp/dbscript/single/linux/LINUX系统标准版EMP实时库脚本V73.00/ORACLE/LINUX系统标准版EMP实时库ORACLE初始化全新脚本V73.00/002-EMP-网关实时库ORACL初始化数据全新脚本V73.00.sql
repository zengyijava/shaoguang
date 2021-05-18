DELETE FROM USERFEE;
COMMIT;

DELETE FROM USERDATA;
COMMIT;

DELETE FROM PB_SERVICETYPE;
COMMIT;

DELETE FROM KF_PARAMS;
COMMIT;

DELETE FROM KF_CORPBASE;
COMMIT;

DELETE FROM GT_PORT_USED;
COMMIT;

DELETE FROM AGENTACCOUNT;
COMMIT;

DELETE FROM A_GWPARAMCONF;
COMMIT;

DELETE FROM A_PROTOCOLTMPL;
COMMIT;

DELETE FROM A_GWPARAMVALUE;
COMMIT;

DELETE FROM PROCESSINGSTATUS;
COMMIT;

DELETE FROM A_GWACCOUNT;
COMMIT;

DELETE FROM XT_GATE_QUEUE;
COMMIT;

DELETE FROM GT_PORT_USED;
COMMIT;

DELETE FROM A_GWSPBIND;
COMMIT;

DELETE FROM HTTPERRCODE;
COMMIT;

DELETE FROM A_PROVINCECITY;
COMMIT;

DELETE FROM  A_AREACODE;
COMMIT;

DELETE FROM GW_MSGTAIL  ;
COMMIT;

DELETE FROM GW_TAILBIND ;
COMMIT;

DELETE FROM GW_TAILCTRL ;
COMMIT;

DELETE FROM GW_CLUSPBIND ;
COMMIT;

DELETE FROM GW_CLUSTATUS ;
COMMIT;

DELETE FROM GW_CLUDECISION ;
COMMIT;

DELETE FROM GW_TDCMD ;
COMMIT;

INSERT INTO AGENTACCOUNT (LOGINID, DESCRIPTION, PRIVILEGE, USERFEEFLAG, PRIVILEGEINFO)
VALUES ('WBS00A', 'WBS00A', 11, 1, '代理帐号,自编MSGID,自带级别');
COMMIT;

INSERT INTO KF_CORPBASE (ECID, CORPACCOUNT, CORPNAME, LICENSE, OPTYPE, STATUS, SPGATE, ECCPNO, ECSIGN, MAXPERDAY, WHITEVER, ORDERTIME, CANCELTIME, MODITIME, EXPANLIMIT, STAFFLIMIT, MAINNOLIMIT, NEEDSIGN, WHTLEVEL, SPEEDLIMIT, ORDERNUM, EXPRIEDDATE, ECMANGER, ECMEMO, ECIDBAK, CORPACCOUNTBAK)
VALUES (1, '200001', '200001', ' ', '0', 0, '                     ', '                     ', '                ', 10000000, 0, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 10, 10, 10, 0, 0, 1000, 0, '          ', '           ', ' ', 0, ' ');
COMMIT;

-------------------号段表初始化
INSERT INTO PB_SERVICETYPE (SPISUNCM, SERVICENO, SERVICEINFO)
VALUES (0, '134,135,136,137,138,139,147,150,151,158,159,157,154,152,188,187,182,183,184,1705,178,1703,1706', '移动');
INSERT INTO PB_SERVICETYPE (SPISUNCM, SERVICENO, SERVICEINFO)
VALUES (1, '130,131,132,155,156,185,186,145,176,1709,175,1707,1708,1718,1719', '联通');
INSERT INTO PB_SERVICETYPE (SPISUNCM, SERVICENO, SERVICEINFO)
VALUES (21, '133,153,189,180,181,1700,177,173,1701,1702', '电信');
COMMIT;

INSERT INTO KF_PARAMS (PARACODE, PARAVAL1, PARAVAL2, PARAINFO)
VALUES (9001, 0, '134,135,136,137,138,139,147,150,151,158,159,157,154,152,187,188,182,183,184,1705,178,1703,1706', '移动');
INSERT INTO KF_PARAMS (PARACODE, PARAVAL1, PARAVAL2, PARAINFO)
VALUES (9002, 1, '130,131,132,155,156,185,186,145,176,1709,175,1707,1708,1718,1719', '联通');
INSERT INTO KF_PARAMS (PARACODE, PARAVAL1, PARAVAL2, PARAINFO)
VALUES (9021, 21, '133,153,189,180,181,1700,177,173,1701,1702', '电信');
COMMIT;

-----初始化代理帐号
INSERT INTO USERDATA (USERID, LOGINID, STAFFNAME, CORPACCOUNT, USERACCOUNT, MOBILE, ORDERTIME, CANCELTIME, MODITIME, USERTYPE, STATUS, USERPASSWORD, SMSTYPE, SENDTYPE, FEEFLAG, RETFLAG, USERPRIVILEGE, SUBMITCNT, COMPANY, SALEMAN, MEMO, SPEEDLIMIT, RISELEVEL, MTURL, MOURL, RPTURL, LOGINIP, MAXDAYNUM, SENDTMSPAN, FORBIDTMSPAN,ACCOUNTTYPE)
VALUES ('WBS00A', 'WBS00A', 'WBS代理帐号', '200001', '200001', '13800138000', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0, 0, '123456', 0, 0, 2, 0, 11, 0, ' ', ' ', ' ', 0, 0, ' ', ' ', ' ', ' ', 100000, '00:00:00-23:59:59', '00:00:00-00:00:00', 1);
------------------------------------------压力测试------------------------------
INSERT INTO USERDATA (USERID, LOGINID, STAFFNAME, CORPACCOUNT, USERACCOUNT, MOBILE, ORDERTIME, CANCELTIME, MODITIME, USERTYPE, STATUS, USERPASSWORD, SMSTYPE, SENDTYPE, FEEFLAG, RETFLAG, USERPRIVILEGE, SUBMITCNT, COMPANY, SALEMAN, MEMO, SPEEDLIMIT, RISELEVEL, MTURL, MOURL, RPTURL, LOGINIP, MAXDAYNUM, SENDTMSPAN, FORBIDTMSPAN, ACCOUNTTYPE)
VALUES ( 'END000', 'END000', '压力测试通道帐号(勿做真实使用)', '200001', '200001', '13800138000', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 1, 0, '123456', 0, 0, 2, 0, 0, 0, ' ', ' ', ' ', 0, 0, ' ', ' ', ' ', ' ', 100000, '00:00:00-23:59:59', '00:00:00-00:00:00',1);
COMMIT;

INSERT INTO USERDATA (USERID, LOGINID, STAFFNAME, CORPACCOUNT, USERACCOUNT, MOBILE, ORDERTIME, CANCELTIME, MODITIME, USERTYPE, STATUS, USERPASSWORD, SMSTYPE, SENDTYPE, FEEFLAG, RETFLAG, USERPRIVILEGE, SUBMITCNT, COMPANY, SALEMAN, MEMO, SPEEDLIMIT, RISELEVEL, MTURL, MOURL, RPTURL, LOGINIP, MAXDAYNUM, SENDTMSPAN, FORBIDTMSPAN, ACCOUNTTYPE)
VALUES ('PRE001', 'PRE001', '压力测试前端帐号(勿做真实使用)', '200001', '200001', '13800138000', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0, 0, '123456', 0, 0, 2, 0, 0, 0, ' ', ' ', ' ', 0, 0, ' ', ' ', ' ', ' ', 100000, '00:00:00-23:59:59', '00:00:00-00:00:00',1);
COMMIT;

INSERT INTO A_GWACCOUNT(GWNO,PTACCUID, PTACCID, PTACCPWD, SPACCID, SPACCPWD, SPID, SERVICETYPE, FEEUSERTYPE, SPIP, SPPORT, SPEEDLIMIT, PROTOCOLCODE, PTPORT, PTIP, PTACCNAME, PROTOCOLPARAM)
SELECT 100,  "UID" ,'END000', '123456','END000','123456', 'END000','SMS', 0, '127.0.0.1',7891, 2000, 5, 9980,'127.0.0.1', '压力测试通道帐号(勿做真实使用)', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0' FROM USERDATA WHERE USERID='END000' AND ACCOUNTTYPE=1 ;
COMMIT;


INSERT INTO GW_USERPROPERTY(USERID,ECID) SELECT USERID,100001 FROM USERDATA WHERE USERTYPE=0 AND NOT EXISTS (SELECT USERID FROM GW_USERPROPERTY  WHERE USERID=USERDATA.USERID);
INSERT INTO GW_GATECONNINFO(PTACCID,IP,PORT,KEEPCONN,TESTMETHOD)
SELECT PTACCID,SPIP,SPPORT,0,2 FROM A_GWACCOUNT A
WHERE NOT EXISTS (SELECT PTACCID,IP,PORT  FROM GW_GATECONNINFO WHERE A.SPIP=IP AND A.PTACCID=PTACCID AND PORT=A.SPPORT);
COMMIT;


-- ----------------------------
-- Records of a_gwparamconf
-- ----------------------------
INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1CHKPOINT', '流水号CheckPoint频率 ', 1, '多长时间进行一次checkpoint(单位:小时)', '2', '1-24', 0, '流水號CheckPoint頻率', '多長時間進行一次checkpoint(單位:小時)', 'Serial number CheckPoint frequency', 'How long does it take to have a checkpoint (unit: hour), 2 hours by default, range of value: 1-24');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1DATAFILELPATH', '本地数据文件目录', 1, '定时数据文件输出的本地路径(填写绝对路径)，默认为空，取程序当前路径', ' ', ' ', 0, '本地資料檔案目錄', '定時資料檔案輸出的本地路徑(填寫絕對路徑)，預設為空，取程式當前路徑', 'Local data file directory', 'The local path of timing data file output (absolute path), the default is empty, take the program current path.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1DATAFILERPATH', '共享数据文件目录', 1, '集群网关数据文件共享路径(填写绝对路径)，默认为空，不进行文件共享', ' ', ' ', 0, '共用資料檔案目錄', '集群閘道資料檔案共用路徑(填寫絕對路徑)，預設為空，不進行檔共用', 'Shared data file directory', 'Cluster gateway data file sharing path (absolute path), the default is empty, not for file sharing.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1MSGIDFILESIZE', '流水号文件大小 ', 1, '单个流水号操作日志文件大小(单位:M)', '20', '1-100', 0, '流水號文件大小', '單個流水號操作日誌檔大小(單位:M)', 'Size of serial number file', 'A Single serial number operation log file size (unit: Megabyte), default 20M, range of value: 1-100');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1MSGIDFILET', '流水号文件频率 ', 1, '每隔多少秒产生一个日志文件(单位:秒)', '5', '3-3600', 0, '流水號檔頻率', '每隔多少秒產生一個日誌檔(單位:秒)', 'Frequency of serial number file', 'Log file generation cycle (unit: seconds), default: 5 seconds, value range: 3-3600');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ASYNWINSIZEMON', '滑动窗口大小(1-256)', 2, '滑动窗口大小(异步提交MO)(1-256)', '128', '1-256', 0, '滑動窗口大小(1-256)', '滑動視窗大小(非同步提交MO)(1-256)', 'Sliding-window size(1-256)', 'Sliding Window size (Asynchronous commit MO)(1-256)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ASYNWINSIZEMTN', 'MT滑动窗口大小(1-256)', 1, '滑动窗口大小(异步提交MT)(1-256)', '128', '1-256', 0, 'MT滑動窗口大小(1-256)', '滑動視窗大小(非同步提交MT)(1-256)', 'MT sliding-window size(1-256)', 'Sliding Window size (Asynchronous commit MT)(1-256)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'BEEPORNOT', '出错后是否报警', 1, '平台出错后是否报警 0-否 1-是', '0', '0,1', 1, '出錯後是否報警', '平臺出錯後是否報警 0-否 1-是', 'Error alarm', 'Alarm or not if platform error occurred. 0-NO 1-YES');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE01USEORDB', '启用写文件模式', 1, '文件模式下行先写入文件，后再读取入数据库表(1:文件方式; 0:直接入库, 默认为1)', '1', '0,1', 1, '啟用寫檔模式', '檔模式下行先寫入檔，後再讀取入資料庫表(1:檔方式; 0:直接入庫, 默認為1)', 'Enable file whiting mode', 'File mode first write the file, and then read into the database table (1: file mode; 0: write the database directly, the default is 1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE02DISKFREE', '写数据文件的最小磁盘空间大小', 1, '写数据文件的最小磁盘空间大小,单位G(5~200，默认为10G)', '10', '5~200', 0, '寫資料檔案的最小磁碟空間大小', '寫資料檔案的最小磁碟空間大小,單位G(5~200，默認為10G)', 'Minimum disk space size of the data writing file', 'Write data file minimum disk space size, unit G (5 ~ 200, default is 10G)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE03MSGFILESIZE', '单个数据文件大小', 1, '单个数据文件大小,单位M(20~100，默认为20M)', '20', '20~100', 0, '單個資料檔案大小', '單個資料檔案大小,單位M(20~100，默認為20M)', 'Size of a single data file', 'Size of single data file, M as a unit (20 to 100, default is 20M )');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE04MSGFILETM', '数据文件生成频次', 1, '数据文件生成的频次,单位秒(5~300，默认为20秒)', '20', '5~300', 0, '資料檔案生成頻次', '資料檔案生成的頻次,單位秒(5~300，默認為20秒)', 'Generation frequency of data file', 'Data file generated frequency, the unit seconds (5 to 300, the default is 20 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE05COMPRESSDIRTM', '压缩多少天前的数据文件', 1, '压缩多少天前的数据文件(1~30，默认为3天)', '3', '1~30', 0, '壓縮多少天前的資料檔案', '壓縮多少天前的資料檔案(1~30，默認為3天)', 'Compress data file of how many days ago', 'How many days data files to compressed  (1 ~ 30, the default is 3 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE06DELETEFILETM', '删除多少天前的数据压缩文件', 1, '删除多少天前的数据压缩文件(30~120，默认为90天)', '90', '30~120', 0, '刪除多少天前的資料壓縮檔', '刪除多少天前的資料壓縮檔(30~120，默認為90天)', 'Delete the data compression file of how many days ago', 'The data compress file saved in DB will be deleted when the saving time exceed the setting days. (30 – 120, default is 90 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE07SHAREDIR', '保存数据文件的共享目录', 1, '保存数据文件的共享目录', ' ', ' ', 0, '保存資料檔案的共用目錄', '保存資料檔案的共用目錄', 'Shared directory of data saving files', 'Shared directory of data saving file');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE08STOPINSERTDB', '暂停数据文件入库', 1, '暂停数据文件入库(1:暂停文件入库; 0:开启文件入库, 默认为0)属性：高级参数', '0', '0,1', 1, '暫停資料檔案入庫', '暫停資料檔案入庫(1:暫停檔入庫; 0:開啟檔入庫, 預設為0)屬性：高級參數', 'Pause data file inserting into DB', 'Pause data file inserting into DB （1：Pause; 0: Enable. The default value is 0） Properties: Advanced parameters');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'HOUROUTREPORTN', '状态报告有效期(小时)', 1, '状态报告有效期(小时)(12-360)', '72', '12-360', 0, '狀態報告有效期(小時)', '狀態報告有效期(小時)(12-360)', 'Validity of RPT(hour)', 'The validity of the status report (12-360 hours)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ISCUTSPGATE', '是否仅使用子号下发', 1, '0:完整通道号下发 1:仅使用子号下发(子号包括:绑定表中的CPNO+用户自扩展)', '0', '0,1', 1, '是否僅使用子號下發', '0:完整通道號下發 1:僅使用子號下發(子號包括:綁定表中的CPNO+用戶自擴展)', 'Only use the sub-number to send or not', '0:Use the full channel number to send 1: Use the sub-channel number to send');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ISFORCEFWD', '是否强制中转(0/1)', 2, '0-不强制中转 1-强制中转', '0', '0,1', 0, '是否強制中轉(0/1)', '0-不強制中轉 1-強制中轉', 'Force transfer or not(0/1) ', '0-Unforced transit 1-Force transfer');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ISPACKLONGSMS', '是否长短信MT组包(0/1)', 2, '0-不组包长短信 1-组包长短信', '0', '0,1', 1, '是否長短信MT組包(0/1)', '0-不組包長短信 1-組包長短信', 'Is it a long/short message MT package(0/1) ', '0-Partition long SMS 1-Composition long SMS');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'LOGDAYSN', '日志保存天数', 1, '日志保存天数(7-90)', '15', '7-90', 0, '日誌保存天數', '日誌保存天數(7-90)', 'Number of Log saving days', 'Number of days to save the log(7-90)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'LOGLEVELN', '日志级别', 1, '日志级别(0-5)', '3', '0,1,2,3,4,5', 1, '日誌級別', '日誌級別(0-5)', 'Log level', 'Log level (0-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MAPSIZE', 'MAP流水号队列大小', 1, ' MAP流水号队列大小(20000000~80000000，默认为50000000)', '50000000', '20000000~80000000', 0, 'MAP流水號佇列大小', 'MAP流水號佇列大小(20000000~80000000，默認為50000000)', 'The size of MAP serial number queue', 'MAP serial number queue size (20000000~80000000, default to 50000000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MOBUFSIZEN', 'MO缓冲大小', 1, 'MO缓冲大小(10-10000)', '5000', '10-10000', 0, 'MO緩衝大小', 'MO緩衝大小(10-10000)', 'The size of MO buffer', 'MO buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MONITORMQURL', '监控消息队列地址', 1, '监控消息队列地址(重启生效)', '127.0.0.1:61616', ' ', 0, '監控訊息佇列地址', '監控訊息佇列地址(重啟生效)', 'Address of monitoring message queue ', 'Monitor message queue address (valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MONITORQUENAME', '监控消息队列名', 2, '监控消息队列名(重启生效)', 'MEPMONITORQUEUE', ' ', 0, '監控訊息佇列名', '監控訊息佇列名(重啟生效)', 'Name of monitoring message queue', 'Monitor the message queue name(valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MORESENDCOUNTN', '上行MO失败重发次数', 1, 'MO失败重发次数(1-5)', '3', '1-5', 0, '上行MO失敗重發次數', 'MO失敗重發次數(1-5)', 'Times of failed MO resend', 'Times of failed MO resend (1-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTDBBUFSIZEN', 'MT写库缓冲', 1, '取值范围10#50000', '5000', '10-50000', 0, 'MT寫庫緩衝', '取值範圍10#50000', 'MT DB inserting buffer', 'Value range 10 - 50000');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTPACKSMAXNUMN', '下行MT打包发送数量', 0, 'SGIP/SMGP协议不能打包,打包上限不超过100', '1', '1-100', 0, '下行MT打包發送數量', 'SGIP/SMGP協議不能打包,打包上限不超過100', 'Amount of MT package transmission', 'SGIP / SMGP protocol can not be packaged, the package limit does not exceed 100');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTRESENDCOUNTN', '下行MT失败重发次数', 0, 'MT失败重发次数(1-5)', '3', '1-5', 0, '下行MT失敗重發次數', 'MT失敗重發次數(1-5)', 'Times of MT resend', 'MT failure retry times(1-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTSENDLEVELN', '下行MT提交级别(0-9)', 2, 'MT提交级别(0-9)', '1', '0,1,2,3,4,5,6,7,8,9', 1, '下行MT提交級別(0-9)', 'MT提交級別(0-9)', 'MT submission level(0-9)', 'MT submission level (0-9)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTSENDTHREADNUMN', 'MT提交线程数量(1-15)', 1, 'MT提交线程数量(1-15)', '1', '1-15', 0, 'MT提交執行緒數量(1-15)', 'MT提交執行緒數量(1-15)', 'MT submission thread number(1-15)', 'MT submission thread Number (1-15)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORINSERTNUMN', 'RPT批量写库条数', 1, 'REPORT写库最大包数(1-100)', '100', '1-100', 0, 'RPT批量寫庫條數', 'REPORT寫庫最大包數(1-100)', 'Number of PRT batch DB inserting', 'REPORT Write the maximum number of packets in the database (1-100)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORT1BUFSIZEN', 'REPORT一级缓冲大小(10-10000)', 1, 'REPORT一级缓冲大小(10-10000)', '5000', '10-10000', 0, 'REPORT一級緩衝大小(10-10000)', 'REPORT一級緩衝大小(10-10000)', 'Size of REPORT level1 buffer(10-10000)', 'REPORT level1 buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORT2BUFSIZEN', 'REPORT二级缓冲大小(10-10000)', 1, 'REPORT二级缓冲大小(10-10000)', '5000', '10-10000', 0, 'REPORT二級緩衝大小(10-10000)', 'REPORT二級緩衝大小(10-10000)', 'Size of REPORT level2 buffer(10-10000)', 'REPORT level2 buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORT3BUFSIZEN', 'REPORT三级缓冲大小(10-10000)', 1, 'REPORT三级缓冲大小(10-10000)', '5000', '10-10000', 0, 'REPORT三級緩衝大小(10-10000)', 'REPORT三級緩衝大小(10-10000)', 'Size of REPORT level3 buffer(10-10000)', 'REPORT level3 buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORTPREFIXS', 'RPT错误码前缀', 2, 'RPT错误码前缀', 'E2', 'E2', 0, 'RPT錯誤碼首碼', 'RPT錯誤碼首碼', 'Prefix of RPT error code', 'RPT error code prefix');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORTRESENDCOUNTN', '状态报告RPT失败重发次数', 1, 'RPT失败重发次数(1-5)', '3', '1-5', 0, '狀態報告RPT失敗重發次數', 'RPT失敗重發次數(1-5)', 'Times of failed RPT resend', 'Times of failed RPT resend (1-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORTUPDATENUMN', 'RPT批量更新条数', 1, 'REPORT库更新最大包数(1-100)', '100', '1-100', 0, 'RPT批量更新條數', 'REPORT庫更新最大包數(1-100)', 'Number of RPT batch updating', 'Maximum packages amount of REPORT library updating (1-100)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SENDERRORRPT', '是否推送错误rpt给网关', 1, '是否推送错误rpt给网关 0:不推送，1:推送', '1', '0~1', 0, '是否推送錯誤rpt給閘道', '是否推送錯誤rpt給閘道 0:不推送，1:推送', 'Push error rpt to gateway or not', 'Whether push error rpt to the gateway 0: no push, 1: push');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SPEEDCTRLWAYN', 'MT提交控制速度方式', 1, '3-SPGATE控速 4-提交接口控速', '4', '3,4', 1, 'MT提交控制速度方式', '3-SPGATE控速 4-提交介面控速', 'Method of MT submission speed controlling', '3-SPGATE speed control 4-Submit interface speed control');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SPEEDSIZEN', '下行MT发送速度', 0, 'MT发送速度(1-20000)', '100', '1-20000', 0, '下行MT發送速度', 'MT發送速度(1-20000)', 'MT sending speed', 'MT transmission speed (1-20000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'STOPMONITOR', '暂停监控 ', 1, '是否暂停网关程序监控: 1 暂停 0监控', '0', '0,1', 1, '暫停監控', '是否暫停閘道程式監控: 1 暫停 0監控', 'Pause Monitoring', 'Whether to stop the gateway program monitoring: 1 pause  0 monitor');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SYSMONFREQ', '系统监控频率(S)', 1, '系统监控频率(S)', '5', '3-120', 0, '系統監控頻率(S)', '系統監控頻率(S)', 'System monitoring frequency(S)', 'System monitoring frequency (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, '6CLU1DATAFILELPATH', '本地数据文件目录', 1, '定时数据文件输出的本地路径(填写绝对路径)，默认为空，取程序当前路径', ' ', ' ', 0, '本地資料檔案目錄', '定時資料檔案輸出的本地路徑(填寫絕對路徑)，預設為空，取程式當前路徑', 'Local data file directory', 'The local path of timing data file output (absolute path), the default is empty, take the program current path.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, '6CLU1DATAFILERPATH', '共享数据文件目录', 1, '集群网关数据文件共享路径(填写绝对路径)，默认为空，不进行文件共享', ' ', ' ', 0, '共用資料檔案目錄', '集群閘道資料檔案共用路徑(填寫絕對路徑)，預設為空，不進行檔共用', 'Shared data file directory', 'Cluster gateway data file sharing path (absolute path), the default is empty, not for file sharing.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ADJUSTENDWND', '后端自动调整窗口(0/1)', 2, '是否启用后端滑动窗口自调功能 0-否 1-是', '1', '0,1', 1, '後端自動調整視窗(0/1)', '是否啟用後端滑動視窗自調功能 0-否 1-是', 'Back-end auto size window(0/1)', 'Enable the back-end sliding window self-tuning function 0-Disable 1-Enable ');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ADJUSTPREWND', '前端自动调整窗口', 2, '是否启用前端滑动窗口自调功能0-否 1-是', '1', '0,1', 1, '前端自動調整視窗', '是否啟用前端滑動視窗自調功能0-否 1-是', 'Front-end auto size window', 'Enable the front-end sliding window self-tuning function 0-Disable 1-Enable ');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'APPTITLE', '程序标题', 1, 'MWSMS程序运行时的标题', 'MWSMS短信网关', ' ', 0, '程式標題', 'MWSMS程式運行時的標題', 'Program title', 'The title of the MWSMS program runtime');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'BATCHREAD', '批量读取条数', 1, '批量读取条数', '50', '1-1000', 0, '批量讀取條數', '批量讀取條數', 'Number of batch reading', 'Batch read number');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'BEEPORNOT', '出错后是否报警', 1, '平台出错后是否报警 0-否 1-是', '0', '0,1', 1, '出錯後是否報警', '平臺出錯後是否報警 0-否 1-是', 'Error alarm', 'Alarm or not if platform error occurred. 0-NO 1-YES');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'CHECKNULLLINKFREQ', '空连接检测(S)', 1, '平台检测超时连接的频率(S)', '30', '5-60', 0, '空連接檢測(S)', '平臺檢測超時連接的頻率(S)', 'Null connection detection (S)', 'The platform detects the frequency of the overtime connection (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'CMPPPORT', 'CMPP监听端口', 0, '移动运营商CMPP监听端口', '7890', ' ', 0, 'CMPP監聽埠', '移動運營商CMPP監聽埠', 'CMPP listening port', 'Mobile operator CMPP listening port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'DBCMDDELAY', 'DB超时时间(S)', 1, '连接数据命令执行超时时间（S）', '60', '5-60', 0, 'DB超時時間(S)', '連接資料命令執行超時時間（S）', 'Database timeout (S)', 'Connection data command execution time-out (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'DISCARDNULLMO', '丢弃空上行', 1, '是否丢弃空上行标志', '1', '0,1', 1, '丟棄空上行', '是否丟棄空上行標誌', 'Discard empty Mo', 'Discard empty MO sign or not');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'DISCARDRPT', '是否丢弃RPT(0/1)', 1, '是否丢弃RPT 0-否 1-是', '0', '0,1', 0, '是否丟棄RPT(0/1)', '是否丟棄RPT 0-否 1-是', 'Discard status report (0/1)', 'Whether to discard RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ENABLELOG', '记录运行日志', 1, '是否记录网关运行日志(0/1)', '1', '0,1', 1, '記錄運行日誌', '是否記錄閘道運行日誌(0/1)', 'Record running log', 'Whether to record the gateway run log (0/1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ENDSLIDEWND', '后端窗口初值(1-16)', 2, '后端用户滑动窗口大小初值(1-16)', '5', '1-16', 0, '後端窗口初值(1-16)', '後端使用者滑動視窗大小初值(1-16)', 'Initial value of back-end window', 'Backend User Sliding Window Size Initial Value (1-16)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ERCODEPREFIX', 'RPT错误码前缀', 2, 'RPT错误码前缀(字母和数字的组合，长度不超过3)', 'E1', ' ', 0, 'RPT錯誤碼首碼', 'RPT錯誤碼首碼(字母和數位的組合，長度不超過3)', 'Prefix of RPT error code', 'RPT error code prefix (combination of letters and numbers, no more than 3)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ERMTTODB', '异常MT是否入库标志', 1, '异常MT是否入库标志 0-否 1-是', '1', '0,1', 1, '異常MT是否入庫標誌', '異常MT是否入庫標誌 0-否 1-是', 'Insert MT exception sign into DB or not', 'Exception MT is written to the database flag 0 - No 1 - Yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE01USEORDB', '启用写文件模式', 1, '文件模式下行先写入文件，后再读取入数据库表(1:文件方式; 0:直接入库, 默认为1)', '1', '0,1', 1, '啟用寫檔模式', '檔模式下行先寫入檔，後再讀取入資料庫表(1:檔方式; 0:直接入庫, 默認為1)', 'Enable file whiting mode', 'File mode first write the file, and then read into the database table (1: file mode; 0: write the database directly, the default is 1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE02DISKFREE', '写数据文件的最小磁盘空间大小', 1, '写数据文件的最小磁盘空间大小,单位G(5~200，默认为10G)', '10', '5~200', 0, '寫資料檔案的最小磁碟空間大小', '寫資料檔案的最小磁碟空間大小,單位G(5~200，默認為10G)', 'Minimum disk space size of the data writing file', 'Write data file minimum disk space size, unit G (5 ~ 200, default is 10G)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE03MSGFILESIZE', '单个数据文件大小', 1, '单个数据文件大小,单位M(20~100，默认为20M)', '20', '20~100', 0, '單個資料檔案大小', '單個資料檔案大小,單位M(20~100，默認為20M)', 'Size of a single data file', 'Size of single data file, M as a unit (20 to 100, default is 20M )');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE04MSGFILETM', '数据文件生成频次', 1, '数据文件生成的频次,单位秒(5~300，默认为20秒)', '20', '5~300', 0, '資料檔案生成頻次', '資料檔案生成的頻次,單位秒(5~300，默認為20秒)', 'Generation frequency of data file', 'Data file generated frequency, the unit seconds (5 to 300, the default is 20 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE05COMPRESSDIRTM', '压缩多少天前的数据文件', 1, '压缩多少天前的数据文件(1~30，默认为3天)', '3', '1~30', 0, '壓縮多少天前的資料檔案', '壓縮多少天前的資料檔案(1~30，默認為3天)', 'Compress data file of how many days ago', 'How many days data files to compressed  (1 ~ 30, the default is 3 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE06DELETEFILETM', '删除多少天前的数据压缩文件', 1, '删除多少天前的数据压缩文件(30~120，默认为90天)', '90', '30~120', 0, '刪除多少天前的資料壓縮檔', '刪除多少天前的資料壓縮檔(30~120，默認為90天)', 'Delete the data compression file of how many days ago', 'The data compress file saved in DB will be deleted when the saving time exceed the setting days. (30 – 120, default is 90 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE07SHAREDIR', '保存数据文件的共享目录', 1, '保存数据文件的共享目录', ' ', ' ', 0, '保存資料檔案的共用目錄', '保存資料檔案的共用目錄', 'Shared directory of data saving files', 'Shared directory of data saving file');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE08STOPINSERTDB', '暂停数据文件入库', 1, '暂停数据文件入库(1:暂停文件入库; 0:开启文件入库, 默认为0)属性：高级参数', '0', '0,1', 1, '暫停資料檔案入庫', '暫停資料檔案入庫(1:暫停檔入庫; 0:開啟檔入庫, 預設為0)屬性：高級參數', 'Pause data file inserting into DB', 'Pause data file inserting into DB （1：Pause; 0: Enable. The default value is 0） Properties: Advanced parameters');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILTERB01TDMO', '开启上行退订功能', 1, '是否开启上行回复退订，手机号自动添加黑名单功能 (0不启用、1启用)', '0', '0,1', 1, '開啟上行退訂功能', '是否開啟上行回復退訂，手機號自動添加黑名單功能 (0不啟用、1啟用)', 'Enable MO unsubscribe function', 'Whether to open MO reply unsubscribing. Phone number will be automatically added to blacklist. (0 Not Disable, 1 enable)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILTERBLACKPHONE', '是否过滤黑名单(0/1)', 1, '0-不过滤 1-过滤', '1', '0,1', 0, '是否過濾黑名單(0/1)', '0-不過濾 1-過濾', 'Filter blacklist or not(0/1)', '0 - not filter 1 - filter');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILTERKEYWORDS', '过滤关键字', 1, '是否开启关键字过滤 0-否 1-是', '0', '0,1', 1, '過濾關鍵字', '是否開啟關鍵字過濾 0-否 1-是', 'Filter keyword', 'Whether to enable keyword filtering 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INNERPORT', '内部端口', 0, '网关内部监听端口', '9980', ' ', 0, '內部埠', '閘道內部監聽埠', 'Internal port', 'Interior Gateway listening port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB0TRANSMODE', '数据转移模式', 1, '0-实时转移(实时转移)，1-整点转移(一天转移一次)', '0', '0,1', 1, '資料轉移模式', '0-即時轉移(即時轉移)，1-整點轉移(一天轉移一次)', 'Data transfer mode', '0 - real-time transfer (real-time transfer), 1-The whole point transfer (once a day)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB1INTERVAL', '实时转移时间间隔', 1, '实时从热表转移数据到实时表的时间间隔(单位:秒，默认180秒)', '180', '10-7200', 0, '即時轉移時間間隔', '即時從熱表轉移資料到即時表的時間間隔(單位:秒，默認180秒)', 'Real time transfer interval', 'Real-time transfer from the hot table data to the real-time table of the time interval (unit: seconds, the default 300 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB2PRETIME', '热表数据时长', 1, '热表中保存多长时间的数据(单位:分钟)', '5', '1-1440', 0, '熱表數據時長', '熱表中保存多長時間的資料(單位:分鐘)', 'Hot table data duration', 'How long is the data stored in the hot table (in minutes)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB3INCRESLOAD', '分批次转移数据量', 1, '分批从热表转移到备份表，批次转移数据量。', '500000', '1000-1000000', 0, '分批次轉移資料量', '分批從熱表轉移到備份表，批次轉移資料量。', 'Batch transferred amount of data', 'Batch from the hot table to the backup table,Batch transfer data volume.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB4INCRES', '分批次更新数据量', 1, '下行数据的发送状态分批更新，批次更新数量。', '500000', '1000-1000000', 0, '分批次更新資料量', '下行資料的發送狀態分批更新，批次更新數量。', 'Amount of batch updating data', 'Batch updates transmission status of MT data, the number of batch updating data');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB5TRANSTIME', '整点转移时间', 1, '整点数据转移模式，下一次整点数据转移时间(例如: 00:30:00)', '00:30:00', '00:00:00-23:59:59', 0, '整點轉移時間', '整點數據轉移模式，下一次整點數據轉移時間(例如: 00:30:00)', 'Integral point transfer time', 'The entire data transfer mode, the next point of data transfer time (for example: 00:30:00)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB6STATSTRANS', '调度汇总时间', 1, '网关汇总数据报表及历史数据调度时间(例如: 03:00:00)', '03:00:00', '00:00:00-23:59:59', 0, '調度匯總時間', '閘道匯總資料包表及歷史資料調度時間(例如: 03:00:00)', 'Scheduling summary time', 'Gateway summary data report and historical data scheduling time (for example: 03:00:00)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB7AUTOCTRL', '暂停实时转移阀值', 1, '实时转移模式下，平台短信滞留量超过阀值后，程序自动暂停实时转移(取值范围：1000-20000000，0表示不启用)', '0', '1000-20000000', 0, '暫停即時轉移閥值', '即時轉移模式下，平臺短信滯留量超過閥值後，程式自動暫停即時轉移(取值範圍：1000-20000000，0表示不啟用)', 'Threshold of real time transfer pause', 'Real-time transfer mode, the platform message retention exceeds the threshold, the program automatically suspends the real-time transfer (range: 1000-20000000,0 is not enabled)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGINID', 'WBS身份识别登陆帐号', 2, 'WBS身份识别登陆帐号', 'WBS00A', ' ', 0, 'WBS身份識別登陸帳號', 'WBS身份識別登陸帳號', 'WBS ID login account', 'WBS identification login account');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGINPWD', 'WBS身份识别登陆帐号密码', 2, 'WBS身份识别登陆帐号密码', '123456', ' ', 0, 'WBS身份識別登陸帳號密碼', 'WBS身份識別登陸帳號密碼', 'WBS ID login password', 'WBS identification login password');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGLEVEL', '日志级别(0-5)', 1, '0:错误日志 1:原始网络包(不含命令) 2:与前端交互的网络包(含命令) 3:与后端交互的网络包(含命令) 4:前后两端的网络包(含命令) 5:详细包', '2', '0,1,2,3,4,5', 1, '日誌級別(0-5)', '0:錯誤日誌 1:原始網路包(不含命令) 2:與前端交互的網路包(含命令) 3:與後端交互的網路包(含命令) 4:前後兩端的網路包(含命令) 5:詳細包', 'Log level(0-5)', '0: error log 1: original network packet (without command) 2: network packet with the front end (including command) 3: network packet with the back-end interaction (including command) 4: front and rear network packets (including command) 5: detailed package');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGPATH', '日志路径', 2, '存储日志文件的路径', ' ', ' ', 0, '日誌路徑', '存儲日誌檔的路徑', 'Log path', 'The path to the log file');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGSAVEDAYS', '日志保存天数', 1, '平台记录的日志保存天数，超过将被自动删除', '30', '7-90', 0, '日誌保存天數', '平臺記錄的日誌保存天數，超過將被自動刪除', 'Number of Log saving days', 'Platform records the number of log days to keep,Exceeded date will be automatically deleted');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGSIZE', '单个日志文件大小(M)', 1, '单个日志文件的大小，超过将重命名并新建文件', '30', '10-100', 0, '單個日誌檔大小(M)', '單個日誌檔的大小，超過將重命名並新建檔', 'Size of single log file(M)', 'The size of a single log file, more than will be renamed and new files');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGTHRMODE', '是否开启日志线程模式', 2, '是否开启日志线程模式 0-否 1-是', '0', '0,1', 1, '是否開啟日誌執行緒模式', '是否開啟日誌執行緒模式 0-否 1-是', 'Enable log thread mode or not', 'Whether to open log thread mode 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXDBBUFBUM', '写库缓冲上限', 1, '数据库缓冲大小(1000-10000)', '5000', '1000-10000', 0, '寫庫緩衝上限', '資料庫緩衝大小(1000-10000)', 'Upper limit of inserting DB buffer', 'Database buffer size (1000-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXENDSLIDEWND', '后端窗口上限(16-150)', 2, '后端用户滑动窗口大小最大值(16-150)', '128', '16-150', 0, '後端窗口上限(16-150)', '後端使用者滑動視窗大小最大值(16-150)', 'Upper limit of back-end window(16-150)', 'Back-end user sliding window size maximum (16-150)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXIOCPTHREAD', '完成端口(IOCP)最大线程数', 2, '完成端口(IOCP)开启的最大线程数', '1', '-1-256', 0, '完成埠(IOCP)最大執行緒數', '完成埠(IOCP)開啟的最大執行緒數', 'IOCP greatest number of thread', 'The maximum number of threads that have opened the port (IOCP)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXPRESLIDEWND', '前端窗口上限', 2, '前端用户滑动窗口大小最大值', '128', '16-150', 0, '前端窗口上限', '前端使用者滑動視窗大小最大值', 'Upper limit of front-end window', 'Front-end user sliding window size maximum');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXRESENDCTRLNUM', '最大重发控制短信条数', 2, '重发控制时间范围，进行过滤的最大短信条数', '1000000', '0-1000000', 0, '最大重發控制短信條數', '重發控制時間範圍，進行過濾的最大短信條數', 'Maximum number of SMS for resending control', 'The time range of the retransmission control, The maximum number of messages to filter');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXREWRDBNUM', '失败重写次数', 1, '写库失败时重复写库次数', '3', '0-30', 0, '失敗重寫次數', '寫庫失敗時重複寫庫次數', 'Failed rewrite times', 'The number of retries after write database failed');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXSYSCON', '用户连接上限', 1, '平台运行的最大用户连接数', '1500', '5-32768', 0, '用戶連接上限', '平臺運行的最大用戶連接數', 'Upper limit of user connection', 'The maximum number of user connections when the platform is running');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXUSERCON', '账号最大接口数', 1, '每个帐号设置的最大连接数', '5', '1-50', 0, '帳號最大介面數', '每個帳號設置的最大連接數', 'Max amount of account port', 'The maximum amount of port for each account');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MEMEXCEPTIONVALUE', '内存异常恢复边界值(M)', 2, '内存异常恢复边界值(M)', '1600', '1200-1700', 0, '記憶體異常恢復邊界值(M)', '記憶體異常恢復邊界值(M)', 'Boundary value of memory exception Recovery (M)', 'Memory Exception Recovery Boundary Value (M)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MEMWARNVALUE', '内存报警值(M)', 2, '内存报警值(M)', '1400', '1000-1500', 0, '記憶體報警值(M)', '記憶體報警值(M)', 'Memory alarm value (M)', 'Memory alarm value (M)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MOCMDROUTE', '是否启动上行指令路由(0/1)', 1, '是否启动上行指令路由 1为启动，0为不启动', '1', '0,1', 1, '是否啟動上行指令路由(0/1)', '是否啟動上行指令路由 1為啟動，0為不啟動', 'Enable MO instruction routing or not (0/1)', 'Enable MO instruction routing or not.  1: Enable, 0: Disable');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MON1REMAINMTWARN', '滞留量告警阀值', 1, '当网关滞留量超过阀值后，向监控平台上报告警，取值范围0-50000000，默认值 5000000，0则不进行告警', '5000000', '0-50000000', 0, '滯留量告警閥值', '當閘道滯留量超過閥值後，向監控平臺上報告警，取值範圍0-50000000，預設值 5000000，0則不進行告警', 'Alarm threshold for delay capacity', 'When the gateway retention exceeds the threshold, the alarm is reported to the monitoring platform, the value range is 0-50000000, the default value is 5000000, 0 is not alarm');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MONITORMQURL', '监控消息队列地址', 1, '监控消息队列地址(重启生效)', '127.0.0.1:61616', ' ', 0, '監控訊息佇列地址', '監控訊息佇列地址(重啟生效)', 'Address of monitoring message queue ', 'Monitor message queue address (valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MONITORQUENAME', '监控消息队列名', 2, '监控消息队列名(重启生效)', 'MEPMONITORQUEUE', ' ', 0, '監控訊息佇列名', '監控訊息佇列名(重啟生效)', 'Name of monitoring message queue', 'Monitor the message queue name(valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORESENDCNT', 'MO超时重发次数', 2, 'MO重发次数', '5', '0-100', 0, 'MO超時重發次數', 'MO重發次數', 'Times of MO timeout resend', 'MO number of retransmissions');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORPTDELAYSD', 'MO/RPT延迟推送间隔(分)', 2, 'MO/RPT延迟推送间隔(分)', '0', '0-60', 0, 'MO/RPT延遲推送間隔(分)', 'MO/RPT延遲推送間隔(分)', 'MO/RPT delayed push interval (min)', 'MO / RPT delayed push interval (minutes)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORPTHOLDON', 'WBS状态报告和上行存留内存中的时间(S)', 1, 'WBS状态报告和上行存留内存中的时间 超过该值将被写入数据库', '30', '1-1800', 0, 'WBS狀態報告和上行存留記憶體中的時間(S)', 'WBS狀態報告和上行存留記憶體中的時間 超過該值將被寫入資料庫', 'WBS status report and the duration MO stayed in memory (S)', 'WBS status reports and the amount of time that the uplink remains in memory,Exceeding this value will be written to the database');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORPTPUSHWAY', '推送mo/rpt的方式', 1, '推送mo/rpt的方式(0:IOCPCLIENT推送，1:使用libcurl推送)', '1', '0,1', 1, '推送mo/rpt的方式', '推送mo/rpt的方式(0:IOCPCLIENT推送，1:使用libcurl推送)', 'Push mo/rpt mode', 'Push mo/rpt mode (0:IOCPCLIENT push, 1: use libcurl push)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MSGVALIDTIME', '短信默认有效时长(H)', 1, '短信默认有效时长(1-24*7小时，网关重启生效)', '24', '1-168', 0, '短信默認有效時長(H)', '短信默認有效時長(1-24*7小時，閘道重啟生效)', 'SMS default valid duration (H)', 'SMS default valid length (1-24 * 7 hours, valid after the gateway reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'NULLLINKTM', '空连接超时(S)', 1, '最大空连接超时时间(S)', '120', '10-1200', 0, '空連接逾時(S)', '最大空連接逾時時間(S)', 'Null connection timeout(S)', 'Maximum empty connection timeout (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PACKINITNUM', '打包写库起点', 1, '打包写库起点(缓冲超过该值才开始打包)', '2', '1-10000', 0, '打包寫庫起點', '打包寫庫起點(緩衝超過該值才開始打包)', 'Starting point of database Packing Insert', 'Starting point of database Packing Insert (begin to pack when buffer exceed this value)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PACKUPDATENUM', '批量更新条数', 1, '更新数据库打包条数(1-200)', '100', '1-200', 0, '批量更新條數', '更新資料庫打包條數(1-200)', 'Number of batch updating', 'Update database package number (1-200)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PACKWRITENUM', '批量写库条数', 1, '写数据时打包的条数限制(1-200)', '100', '1-200', 0, '批量寫庫條數', '寫資料時打包的條數限制(1-200)', 'Number of batch database inserting', 'The limit amount of batch data writing (1-200)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PRESLIDEWND', '前端窗口初值(1-16)', 2, '前端用户滑动窗口大小初值(1-16)', '5', '1-16', 0, '前端窗口初值(1-16)', '前端使用者滑動視窗大小初值(1-16)', 'Initial value of front-end window(1-16)', 'Front User Sliding Window Size Initial Value (1-16)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PROTOCOLVERSION', 'CMPP支持的最高协议版本', 2, 'CMPP支持的最高协议版本', '7', '5,7', 1, 'CMPP支援的最高協定版本', 'CMPP支援的最高協定版本', 'The highest protocol version supported by CMPP', 'The latest protocol version supported by CMPP');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PTADDR', '连接平台的IP地址', 2, '连接平台的IP地址', '127.0.0.1', ' ', 0, '連接平臺的IP位址', '連接平臺的IP位址', 'The IP address of the connection platform', 'The IP address of the connected platform');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY01SPDCTRL', '开启查询接口控速', 1, '对频繁调用获取上行、状态报告接口进行控速(0不启用、1启用)', '1', '0,1', 1, '開啟查詢介面控速', '對頻繁調用獲取上行、狀態報告介面進行控速(0不啟用、1啟用)', 'Enable speed control of query interface', 'Speed control on frequently calling MO or RPT interface (0 disable, 1 enable)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY02FREQ', '允许调用查询接口频率', 1, '在无数据获取时，允许连续调用查询接口的频率(1-120/秒/次，默认3秒)', '3', '1-120', 0, '允許調用查詢介面頻率', '在無數據獲取時，允許連續調用查詢介面的頻率(1-120/秒/次，默認3秒)', 'Frequency limit of calling query interface', 'In the absence of data acquisition, the frequency limit of continuous calling query interface (1-120 sec / times, default 3 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY03DELAY', '查询延时回应时长', 1, '当频繁调用查询接口时，网关对查询回应延时回应(1-120/秒)，默认5秒', '5', '1-120', 0, '查詢延時回應時長', '當頻繁調用查詢介面時，閘道對查詢回應延時回應(1-120/秒，默認5秒)', 'Query delay response duration', 'When system frequently calls the query interface, the response delay of the gateway (1-120 / sec, default 5 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY04TIMES', '频繁调用次数', 2, '连续频繁调用次数超过设置值后开启延时回应 (1-100次，默认10次)', '10', '1-100', 0, '頻繁調用次數', '連續頻繁調用次數超過設置值後開啟延時回應 (1-100次，默認10次)', 'Frequently calling times', 'The number of frequent calls more than the set value after the opening delay response (1-100 times, the default 10 times)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESENDCNT', '重发次数', 2, '重发次数，若为0不启用重发功能', '3', '0-100', 0, '重發次數', '重發次數，若為0不啟用重發功能', 'Resend times', 'Number of retransmissions, 0 is not enabled retransmission');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESENDCTRL', '启用重发控制', 1, '是否开启相同短信内容重复提交控制(0/1)', '1', '0,1', 1, '啟用重發控制', '是否開啟相同短信內容重複提交控制(0/1)', 'Enable resend control', 'Whether to open the control of same content message repeat sending (0/1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESNDCHECKFREQ', '重发控制间隔', 1, '重复频率间隔控制(秒)', '3600', '0-86400', 0, '重發控制間隔', '重複頻率間隔控制(秒)', 'Resend control interval', 'Repeat frequency interval control (seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESNDRPTERCODE', '重发路由RPT错误码列表', 2, '需重发的状态报告错误码列表(以逗号(,)隔开)', ' ', ' ', 0, '重發路由RPT錯誤碼清單', '需重發的狀態報告錯誤碼清單(以逗號(,)隔開)', 'Error code list of resend routing RPT', 'Status to be retransmitted Report error list (separated by comma (,))');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RSPDELAY', '延迟回应时间(S)', 1, '缓冲慢时延迟回应时间(S)', '3', '1-15', 0, '延遲回應時間(S)', '緩衝慢時延遲回應時間(S)', 'Delay response time(S)', 'Duration of buffer delay response(S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SENDBUFBUM', '发送缓冲上限', 1, '数据库缓冲大小(1000-10000)', '5000', '1000-10000', 0, '發送緩衝上限', '資料庫緩衝大小(1000-10000)', 'Upper limit of transmission buffer', 'Database buffer size (1000-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SGIPPORT', 'SGIP监听端口', 2, '联通运营商SGIP监听端口', '8801', ' ', 0, 'SGIP監聽埠', '聯通運營商SGIP監聽埠', 'SGIP listening port', 'Unicom operator SGIP listening port');

  INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SMARTVERIFY', '是否开启智能审核(0/1)', 2, '是否开启智能审核(0/1)', '0', '0,1', 1, '是否開啟智慧審核(0/1)', '是否開啟智慧審核(0/1)', 'Turn On the Intelligent audit or not (0/1)', 'Whether to enable intelligent auditing (0/1)');


INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SMGPPORT', 'SMGP监听端口', 2, '电信运营商SMGP监听端口', '8890', ' ', 0, 'SMGP監聽埠', '電信運營商SMGP監聽埠', 'SMGP listening port', 'Telecom operator SMGP listening port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SMPPPORT', 'SMPP监听端口', 2, '移动运营商SMPP监听端口', '8891', ' ', 0, 'SMPP監聽埠', '移動運營商SMPP監聽埠', 'SMPP protocol port', 'International Protocol SMPP Protocol Port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SPLITLONGMSG', '网关是否拆分超长短信', 1, '网关是否对超长短信(超过360个字)进行拆分后发送。1-拆分，0-不拆分，整条发送给SPGATE，由SPGATE控制。', '1', '0,1', 1, '閘道是否拆分超長短信', '閘道是否對超長短信(超過360個字)進行拆分後發送。1-拆分，0-不拆分，整條發送給SPGATE，由SPGATE控制。', 'Split Long Messages by Gateway or not', 'Whether the gateway to split the long message(more than 360 words) before sending.  1 – split; 0 - no split, send entire long message to SPGATE, controlled by SPGATE');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STARTSVRPROTOCOL', '运营商协议配置', 2, '运营商协议，CMPP-移动,SGIP-联通,SMGP-电信,SMPP-国际协议', '1-0-0-0', 'CMPP,SMGP,SGIP,SMPP', 2, '運營商協定配置', '運營商協議，CMPP-移動,SGIP-聯通,SMGP-電信,SMPP-國際協議', 'Mobile operator protocol configuration', 'Carrier Protocol, CMPP-Mobile, SGIP-Unicom, SMGP-Telecom, SMPP-International Agreement');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPCHARGE', '是否暂停计费(0/1)', 2, '是否暂停计费 0-否 1-是', '0', '0,1', 1, '是否暫停計費(0/1)', '是否暫停計費 0-否 1-是', 'Pause Billing or not (0/1)', 'Whether to suspend billing 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMONITOR', '暂停监控 ', 1, '是否暂停网关程序监控: 1 暂停 0监控', '0', '0,1', 1, '暫停監控', '是否暫停閘道程式監控: 1 暫停 0監控', 'Pause Monitoring', 'Whether to stop the gateway program monitoring: 1 pause  0 monitor');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMOREAD', '暂停上行MO读取', 1, '是否暂停MO读取 0-否 1-是', '0', '0,1', 1, '暫停上行MO讀取', '是否暫停MO讀取 0-否 1-是', 'Pause MO reading', 'Whether to suspend MO read 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMOSEND', '暂停上行MO发送', 0, '是否暂停发送MO 0-否 1-是', '0', '0,1', 1, '暫停上行MO發送', '是否暫停發送MO 0-否 1-是', 'Pause MO sending', 'Whether to suspend sending MO 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTLVLRD', '按级别暂停下行MT读取', 1, '按级别暂停MT读取,级别从0-9,按位取值0:不停1:暂停', ' ', '0,1,2,3,4,5,6,7,8,9', 2, '按級別暫停下行MT讀取', '按級別暫停MT讀取,級別從0-9,按位取值0:不停1:暫停', 'Pause MT reading according to the level', 'Pause MT reading according to the level, level 0-9, Bitwise 0: None 1: Pause');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTLVLSD', '按级别暂停下行MT发送', 1, '按级别暂停MT发送,级别从0-9', ' ', '0,1,2,3,4,5,6,7,8,9', 2, '按級別暫停下行MT發送', '按級別暫停MT發送,級別從0-9', 'Pause MT sending according to the level', 'Pause MT sending according to the level, level from 0-9');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTSEND', '暂停下行MT发送', 0, '是否暂停发送MT 0-否 1-是', '0', '0,1', 1, '暫停下行MT發送', '是否暫停發送MT 0-否 1-是', 'Pause MT sending', 'Whether to suspend sending MT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTTMRLVLRD', '按级别暂停下行MT定时表读取', 1, '按级别暂停MT定时发送表读取,级别从0-9', ' ', '0,1,2,3,4,5,6,7,8,9', 2, '按級別暫停下行MT定時表讀取', '按級別暫停MT定時發送表讀取,級別從0-9', 'Pause the MT timer according to the LeveL', 'Pause the MT timer by level to read, level from 0-9');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPRECV', '是否暂停接收MT/MO/RPT(0/1)', 2, '是否暂停接收MT/MO/RPT 0-否 1-是', '0', '0,1', 1, '是否暫停接收MT/MO/RPT(0/1)', '是否暫停接收MT/MO/RPT 0-否 1-是', 'Stop receiving MT/MO/RPT or not(0/1)', 'Whether to suspend reception of MT / MO / RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPRPTREAD', '暂停状态报告RPT读取', 1, '是否暂停RPT读取 0-否 1-是', '0', '0,1', 1, '暫停狀態報告RPT讀取', '是否暫停RPT讀取 0-否 1-是', 'Pause RPT reading', 'Whether to pause RPT read 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPRPTSEND', '暂停状态报告RPT发送', 0, '是否暂停发送状态报告(RPT) 0-否 1-是', '0', '0,1', 1, '暫停狀態報告RPT發送', '是否暫停發送狀態報告(RPT) 0-否 1-是', 'Pause RPT sending', 'Yes No Pause Send Status Report (RPT) 0 - No 1 - Yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SUSPENDRPT', '不转发状态报告RPT', 1, '是否不转发RPT 0-否 1-是', '0', '0,1', 1, '不轉發狀態報告RPT', '是否不轉發RPT 0-否 1-是', 'Not to forward RPT', 'Whether or not to forward RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SYNCBINDINFOINTERVAL', '同步绑定表间隔(M)', 1, '同步端口绑定表时间间隔(M)', '1', '1-60', 0, '同步綁定表間隔(M)', '同步埠綁定表時間間隔(M)', 'Sync binding time interval(M)', 'Synchronize Port Binding Table Interval (M)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SYNCFEEINTERVAL', '同步费率表间隔(S)', 1, '扣费时间间隔(S)', '3', '2-300', 0, '同步費率表間隔(S)', '扣費時間間隔(S)', 'Sync schedule of rates time interval(S)', 'Charge time interval (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SYSMONFREQ', '系统监控频率(S)', 1, '系统监控频率(S)', '5', '3-120', 0, '系統監控頻率(S)', '系統監控頻率(S)', 'System monitoring frequency(S)', 'System monitoring frequency (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'THREADCTRL', '网关线程数控制', 2, '网关程序启动线程数控制(重启生效)', '2-2-2-2-1-5-1', ' ', 0, '閘道執行緒數控制', '閘道程式啟動執行緒數控制(重啟生效)', 'Gateway thread control', 'Gateway program thread number control (restart effective)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ULTRAFLOWDELAY', '超流量延时(S)', 1, '超流量延时值', '1', '1-60', 0, '超流量延時(S)', '超流量延時值', 'Delay of over flow(S)', 'Value of over flow delay');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'VERIFYTMBEGIN', '审核时段起点(24小时制HH:MM:SS)', 2, '审核时段起点(24小时制HH:MM:SS)', '00:00:00', '24小时制HH:MM:SS', 0, '審核時段起點(24小時制HH:MM:SS)', '審核時段起點(24小時制HH:MM:SS)', 'Starting point of Audit period(HH:MM:SS)', 'The starting point of the audit period(24 hours HH: MM: SS)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'VERIFYTMEND', '审核时段终点(24小时制HH:MM:SS)', 2, '审核时段终点(24小时制HH:MM:SS)', '00:00:00', '24小时制HH:MM:SS', 0, '審核時段終點(24小時制HH:MM:SS)', '審核時段終點(24小時制HH:MM:SS)', 'Ending point of Audit period(HH:MM:SS)', 'The end of the audit period(24 hours HH: MM: SS)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WAITRSPDELAY', '回应超时时间', 1, '等待多长时间未收到回应包视为超时', '90', '40-120', 0, '回應超時時間', '等待多長時間未收到回應包視為超時', 'Response timeout', 'How long does it take to receive the response packet as timeout');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSDISCARDRPT', 'WBS是否丢弃RPT', 1, 'WBS是否丢弃RPT 0-否 1-是', '0', '0,1', 1, 'WBS是否丟棄RPT', 'WBS是否丟棄RPT 0-否 1-是', 'WBS discard RPT or not', 'Whether WBS to discard RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSPACKNUM', 'WBS相同内容短信打包手机号个数', 1, 'WBS相同内容短信打包手机号个数', '50', '1-100', 0, 'WBS相同內容短信打包手機號個數', 'WBS相同內容短信打包手機號個數', 'Amount of phone number within WBS same content SMS package', 'Amount of phone number within WBS same content SMS package');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSPORT', '网关WBS监听端口', 0, 'WBS监听的TCP网络端口', '8082', ' ', 0, '閘道WBS監聽埠', 'WBS監聽的TCP網路埠', 'Gateway WBS listening port', 'WBS listens for TCP network ports');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSRESNDCNT', 'WBS主动推送失败重发次数', 1, 'WBS主动推送失败重发次数', '3', '1-32', 0, 'WBS主動推送失敗重發次數', 'WBS主動推送失敗重發次數', 'Times of WBS active push resend', 'Times of WBS failed active push resend');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSSLIDEWND', 'WBS主动推送滑动窗口', 1, 'WBS主动推送滑动窗口', '3', '1-100', 0, 'WBS主動推送滑動窗口', 'WBS主動推送滑動窗口', 'WBS active push sliding-window', 'WBS active push sliding-window');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSSNDBUF', 'WBS发送缓冲上限', 1, 'WBS发送缓冲上限', '5000', '100-10000', 0, 'WBS發送緩衝上限', 'WBS發送緩衝上限', 'Upper limit of WBS sending buffer', 'WBS sends buffer limit');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSSNDSPD', 'WBS发送速度', 1, 'WBS发送速度', '1500', '10-10000', 0, 'WBS發送速度', 'WBS發送速度', 'WBS transmission speed', 'WBS sending speed');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WEBPATH', 'WEB路径', 2, 'WEB路径', '/SMS/MT', ' ', 0, 'WEB路徑', 'WEB路徑', 'WEB path', 'WEB path');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTBUFSIZEN', 'MT接收缓冲', 1, 'MT接收缓冲，取值范围（10-50000）', '50000', '10-50000', 0, 'MT接收緩衝', 'MT接收緩衝，取值範圍（10-50000）', 'MT receive buffer', 'MT receive buffer Value range 10 - 50000');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOADTMPLTIMEINTERVAL', '加载模板时间间隔', 1, '从模板表中加载模板到内存中的时间间隔(单位:秒，默认30秒)', '30', '10-3600', 0, '加載模板時間間隔', '從模板表中加載模板到內存中的時間間隔(單位:秒，默認30秒)', 'Load template interval','The time interval for loading a template from the template table into memory (unit: second, default 30 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO,DEFAULTVALUE, VALUERANGE,CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PUSHCONFIG', '推送字段是否为JSON数组', 1, '推送字段是否为JSON数组(0:字符串,1:JSON数组)','0','0,1',1,'推送字段是否為JSON數組','推送字段是否為JSON數組(0:字符串,1:JSON數組)','Whether the push field is a JSON array','Whether the push field is a JSON array (0: string, 1: JSON array)');

COMMIT;
/

----协议模板
INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (4, 'SGIP1X', 'LISTENPORT=8074;SRCNODE=0;SPNUMBERCUTLEN=0;SPGATE=10655000;EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (5, 'CMPP2X', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (6, 'SMGP3X', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (7, 'CMPP3X', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (40, 'CMPP2XX', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0;MSGIDON=0');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (41, 'CMPP3XX', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0;MSGIDON=0');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (50, 'OTHER', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (101, 'MWMMS', ' ');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (102, 'MM7', ' ');

INSERT INTO A_PROTOCOLTMPL (PROTOCOLCODE, PROTOCOL, PROTOCOLPARAM)
VALUES (8, 'SMPP3X', 'EXPIREHOUR=24;OUTDBGINFO=3;RETURNMOUDHI=0;SRCTON=1;SRCNPI=1;DESTTON=1;DESTNPI=1;MSGIDENCODE=16');

COMMIT;

INSERT INTO PROCESSINGSTATUS (USEID,CURRINDEX,MAXINDEX,COUNTSTATUS,DISTRACTSTATUS,DELETESTATUS)VALUES(1,0,0,0,0,0);
INSERT INTO PROCESSINGSTATUS (USEID,CURRINDEX,MAXINDEX,COUNTSTATUS,DISTRACTSTATUS,DELETESTATUS)VALUES(2,0,0,0,0,0);


----------------集群表初始化
INSERT INTO GW_CLUSPBIND (PTACCUID,GWNO,GWEIGHT,UPDTIME)
SELECT PTACCUID,GWNO,0,SYSTIMESTAMP FROM A_GWACCOUNT WHERE GWNO NOT IN (SELECT GWNO FROM GW_CLUSPBIND);

MERGE INTO GW_CLUSTATUS A USING(SELECT 4000 AS "GWTYPE",99 AS "GWNO",99 AS "PRIGWNO" ,0 AS "RUNSTATUS",
0 AS "GWEIGHT",0 AS "RUNWEIGHT",SYSTIMESTAMP AS "UPDTIME" FROM DUAL) B ON (A.GWNO=B.GWNO AND A.GWTYPE=B.GWTYPE)
WHEN NOT MATCHED THEN
INSERT (GWTYPE,GWNO,PRIGWNO,RUNSTATUS,GWEIGHT,RUNWEIGHT,UPDTIME)
VALUES (4000,99,99,0,0,0,SYSTIMESTAMP);

INSERT INTO GW_CLUSTATUS(GWTYPE,GWNO,PRIGWNO,RUNSTATUS,GWEIGHT,RUNWEIGHT,UPDTIME)
SELECT 3000,GWNO,GWNO,0,0,0,SYSTIMESTAMP FROM  A_GWACCOUNT WHERE GWNO NOT IN(SELECT GWNO FROM GW_CLUSTATUS);

MERGE INTO GW_CLUDECISION A USING(SELECT 4000 AS "GWTYPE",99 AS "GWNO",0 AS "RUNGWNO",SYSTIMESTAMP AS "RUNUPDTIME",
0 AS "APPLYGWNO",0 AS "APPLYSTATUS"  FROM DUAL) B ON (A.GWNO=B.GWNO AND A.GWTYPE=B.GWTYPE)
WHEN NOT MATCHED THEN
INSERT (GWTYPE,GWNO,RUNGWNO,RUNUPDTIME,APPLYGWNO,APPLYSTATUS)
VALUES (4000,99,0,SYSTIMESTAMP,0,0);

INSERT INTO GW_CLUDECISION(GWTYPE,GWNO,RUNGWNO,RUNUPDTIME,APPLYGWNO,APPLYSTATUS)
SELECT 3000,GWNO,0,SYSTIMESTAMP,0,0 FROM  A_GWACCOUNT WHERE GWNO NOT IN(SELECT GWNO FROM GW_CLUDECISION);
COMMIT;

---通道
INSERT INTO XT_GATE_QUEUE( SPGATE, STATUS, SPISUNCM, GATETYPE, GATENAME, PORTTYPE, SINGLELEN, SIGNSTR, RISELEVEL, SPEED, LONGSMS, MAXWORDS, SUBLEN, FEEFLAG, SIGNLEN, FEE, MULTILEN1, MULTILEN2, SIGNTYPE, SIGNFIXLEN, MAXLONGMSGSEQ, SIGNDROPTYPE, GATESEQ, ENDSPLIT, AREATYPE, GATEAREA, SPLITRULE, SORTID, SHOWFLAG)
VALUES( '106579999', 0, 0, 1, '移动压力测试通道(勿做真实使用)', 0,                70,        '[移动测试]',0,     1000,    1,      1000,      0,      2,        6,       0.00,67,        57,        0,        10,         128,           1,            0,       0,        0,        '全网YL',   1,         1,      1);

INSERT INTO XT_GATE_QUEUE( SPGATE, STATUS, SPISUNCM, GATETYPE, GATENAME, PORTTYPE, SINGLELEN, SIGNSTR, RISELEVEL, SPEED, LONGSMS, MAXWORDS, SUBLEN, FEEFLAG, SIGNLEN, FEE, MULTILEN1, MULTILEN2, SIGNTYPE, SIGNFIXLEN, MAXLONGMSGSEQ, SIGNDROPTYPE, GATESEQ, ENDSPLIT, AREATYPE, GATEAREA, SPLITRULE, SORTID, SHOWFLAG)
VALUES( '106559999', 0, 1, 1, '联通压力测试通道(勿做真实使用)', 1,                70,        '[联通测试]',0,     1000,    1,      1000,      0,      2,        6,       0.00,67,        57,        0,        10,         128,           1,            0,       0,        0,        '全网YL',   1,         1,      1);

INSERT INTO XT_GATE_QUEUE( SPGATE, STATUS, SPISUNCM, GATETYPE, GATENAME, PORTTYPE, SINGLELEN, SIGNSTR, RISELEVEL, SPEED, LONGSMS, MAXWORDS, SUBLEN, FEEFLAG, SIGNLEN, FEE, MULTILEN1, MULTILEN2, SIGNTYPE, SIGNFIXLEN, MAXLONGMSGSEQ, SIGNDROPTYPE, GATESEQ, ENDSPLIT, AREATYPE, GATEAREA, SPLITRULE, SORTID, SHOWFLAG)
VALUES( '106599999', 0, 21, 1, '电信压力测试通道(勿做真实使用)', 21,                70,        '[电信测试]',0,     1000,    1,      1000,      0,      2,        6,       0.00,67,        57,        0,        10,         128,           1,            0,       0,        0,        '全网YL',   1,         1,      1);

SELECT * FROM XT_GATE_QUEUE;

---端口绑定表
INSERT INTO GT_PORT_USED( GATETYPE, SPGATE, CPNO, PORTTYPE, SPISUNCM, ROUTEFLAG, STATUS, USERID, LOGINID,SPNUMBER, FEEFLAG, SIGNSTR, SIGNLEN, MAXWORDS, SINGLELEN, MULTILEN1, MULTILEN2, SENDTMSPAN, FORBIDTMSPAN, GATESEQ, SENDTIMEBEGIN, SENDTIMEEND,MOBIAREA)
VALUES(1, '106579999', '1001', 0,                0,        0,         0,      'PRE001','PRE001','1065799991001',2, '[PRE001]',8,   1000,      70,        67,        57,        '00:00:00-23:59:59','00:00:00-00:00:00', 0, '00:00:00','23:59:59','全网');

INSERT INTO GT_PORT_USED( GATETYPE, SPGATE, CPNO, PORTTYPE, SPISUNCM, ROUTEFLAG, STATUS, USERID, LOGINID,SPNUMBER, FEEFLAG, SIGNSTR, SIGNLEN, MAXWORDS, SINGLELEN, MULTILEN1, MULTILEN2, SENDTMSPAN, FORBIDTMSPAN, GATESEQ, SENDTIMEBEGIN, SENDTIMEEND,MOBIAREA)
VALUES(1, '106559999', '1001', 0,               1,        0,         0,      'PRE001','PRE001','1065599991001',2, '[PRE001]',8,   1000,      70,        67,        57,        '00:00:00-23:59:59','00:00:00-00:00:00', 0, '00:00:00','23:59:59','全网');

INSERT INTO GT_PORT_USED( GATETYPE, SPGATE, CPNO, PORTTYPE, SPISUNCM, ROUTEFLAG, STATUS, USERID, LOGINID,SPNUMBER, FEEFLAG, SIGNSTR, SIGNLEN, MAXWORDS, SINGLELEN, MULTILEN1, MULTILEN2, SENDTMSPAN, FORBIDTMSPAN, GATESEQ, SENDTIMEBEGIN, SENDTIMEEND,MOBIAREA)
VALUES(1, '106599999', '1001', 0,                21,        0,         0,      'PRE001','PRE001','1065999991001',2, '[PRE001]',8,   1000,      70,        67,        57,        '00:00:00-23:59:59','00:00:00-00:00:00', 0, '00:00:00','23:59:59','全网');

INSERT INTO A_GWPARAMVALUE (GWNO, GWTYPE, PARAMITEM, PARAMVALUE)
(SELECT 99, 4000, PARAMITEM, DEFAULTVALUE FROM A_GWPARAMCONF WHERE GWTYPE=4000);

INSERT INTO A_GWPARAMVALUE (GWNO, GWTYPE, PARAMITEM, PARAMVALUE)
(SELECT 100, 3000, PARAMITEM, DEFAULTVALUE FROM A_GWPARAMCONF WHERE GWTYPE=3000);


---协议错误码表
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('DELIVRD','EMPHTTP','000','短信送到手机');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('EXPIRED','EMPHTTP','500','短信过期');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('DELETED','EMPHTTP','500','短信被删除');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('UNDELIV','EMPHTTP','500','无法投递');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('ACCEPTD','EMPHTTP','500','最终用户接收');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('UNKNOWN','EMPHTTP','500','状态不知');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('REJECTD','EMPHTTP','500','短信被拒绝');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0101','EMPHTTP','100','缺少操作命令');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0102','EMPHTTP','100','无效操作命令');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0103','EMPHTTP','100','缺少SP的ID');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0104','EMPHTTP','100','无效SP的ID');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0105','EMPHTTP','100','缺少SP密码');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0106','EMPHTTP','100','无效SP密码');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0107','EMPHTTP','100','下行源地址被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0108','EMPHTTP','100','无效下行源地址');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0109','EMPHTTP','100','缺少下行目的地址');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0110','EMPHTTP','100','无效下行目的地址');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0111','EMPHTTP','100','超过下行目的地址限制');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0112','EMPHTTP','100','ESM_CLASS被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0113','EMPHTTP','100','无效ESM_CLASS');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0114','EMPHTTP','100','PROTOCOL_ID被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0115','EMPHTTP','100','无效PROTOCOL_ID');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0116','EMPHTTP','100','缺少消息编码格式');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0117','EMPHTTP','100','无效消息编码格式');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0118','EMPHTTP','100','缺少消息内容');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0119','EMPHTTP','100','无效消息内容');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0120','EMPHTTP','100','无效消息内容长度');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0121','EMPHTTP','100','优先级被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0122','EMPHTTP','100','无效优先级');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0123','EMPHTTP','100','定时发送时间被禁止（时间格式错误、定时时间小于当前时间、定时时间大于等于存活时间）');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0124','EMPHTTP','100','无效定时发送时间（时间格式错误、定时时间小于当前时间、定时时间大于等于存活时间）');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0125','EMPHTTP','100','短信有效存活时间被禁止（时间格式错误、存活时间小于当前时间、定时时间大于等于存活时间）');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0126','EMPHTTP','100','短信有效存活时间无效（时间格式错误、存活时间小于当前时间、定时时间大于等于存活时间）');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0127','EMPHTTP','100','通道ID被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0128','EMPHTTP','100','无效通道ID');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0131','EMPHTTP','100','缺少批量下行类型');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0132','EMPHTTP','100','无效批量下行类型');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0133','EMPHTTP','100','无效任务ID');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0134','EMPHTTP','100','无效批量下行标题');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0135','EMPHTTP','100','缺少批量下行内容');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0136','EMPHTTP','100','无效批量下行内容');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0137','EMPHTTP','100','缺少批量下行内容URL');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0138','EMPHTTP','100','无效批量下行内容URL');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0139','EMPHTTP','100','SP服务代码不存在');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0140','EMPHTTP','100','无效的不同内容批量下行地址和内容');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0141','EMPHTTP','100','批量下行URL中的文件已经被请求处理过，不能重复请求发送');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0142','EMPHTTP','100','无效随机校验码（长度为8或不为字母、数字、字母数字混合）');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0201','EMPHTTP','200','MSISDN号码段不存在');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0202','EMPHTTP','200','MSISDN号码段停用');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0210','EMPHTTP','200','MSISDN号码被过滤');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0220','EMPHTTP','200','内容被过滤');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0221','EMPHTTP','200','内容被人工过滤');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0230','EMPHTTP','200','下行路由失败');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0240','EMPHTTP','200','上行路由失败');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0250','EMPHTTP','200','配额不足');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0251','EMPHTTP','200','没有配额');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0252','EMPHTTP','200','业务类型错（长度超长或不为字母、数字、字母数字混合）');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0253','EMPHTTP','200','自定义参数错（长度超长）');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0254','EMPHTTP','200','EMP系统认证失败，已停止发送。（可能因为产品序列号过期、长时间无法连接认证服务器等造成）');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0301','EMPHTTP','300','SP被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0302','EMPHTTP','300','SP被锁定');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0303','EMPHTTP','300','非法IP地址');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0304','EMPHTTP','300','超过传输速度限制');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0305','EMPHTTP','300','超过传输连接限制');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0306','EMPHTTP','300','SMS下行被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0307','EMPHTTP','300','SMS批量下行被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0308','EMPHTTP','300','SMS上行被禁止');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0309','EMPHTTP','300','SMS状态报告被禁止');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0401','EMPHTTP','400','源号码与通道号不匹配');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0402','EMPHTTP','400','下发到运行商网关异常');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0403','EMPHTTP','400','下发到运行商网关无反馈');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0500','EMPHTTP','500','运行商网关反馈信息');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('ACCEPTD','EMPHTTP','000','API接口消息被接收');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('REJECTD','EMPHTTP','600','API接口消息被拒绝');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0601','EMPHTTP','600','API接口HTTPEXCEPTION');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0602','EMPHTTP','600','服务器繁忙（服务器繁忙时系统将不处理该请求，需要重新请求）');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0603','EMPHTTP','600','API接口DBEXCEPTION');
COMMIT;
/

INSERT INTO A_GWSPBIND (GATEID)
SELECT XT.ID FROM XT_GATE_QUEUE XT WHERE XT.SPGATE='106579999'AND XT.SPISUNCM=0 AND GATETYPE=1;
INSERT INTO A_GWSPBIND (GATEID)
SELECT XT.ID FROM XT_GATE_QUEUE XT WHERE XT.SPGATE='106559999'AND XT.SPISUNCM=1 AND GATETYPE=1;
INSERT INTO A_GWSPBIND (GATEID)
SELECT XT.ID FROM XT_GATE_QUEUE XT WHERE XT.SPGATE='106599999'AND XT.SPISUNCM=21 AND GATETYPE=1;
UPDATE A_GWSPBIND SET PTACCUID = (SELECT  "UID" FROM USERDATA WHERE USERID='END000' AND ACCOUNTTYPE=1);
COMMIT;
/

-----增加城市区号----------
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('北京', '北京', 10, 10);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('天津', '天津', 22, 22);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '邯郸', 310, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '石家庄', 311, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '保定', 312, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '张家口', 313, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '承德', 314, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '唐山', 315, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '廊坊', 316, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '沧州', 317, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '衡水', 318, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '邢台', 319, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河北', '秦皇岛', 335, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '朔州', 349, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '忻州', 350, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '太原', 351, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '大同', 352, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '阳泉', 353, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '晋中', 354, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '长治', 355, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '晋城', 356, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '临汾', 357, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '吕梁', 358, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山西', '运城', 359, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '呼伦贝尔', 470, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '呼和浩特', 471, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '包头', 472, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '乌海', 473, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '乌兰察布', 474, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '通辽', 475, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '赤峰', 476, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '鄂尔多斯', 477, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '巴彦淖尔', 478, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '锡林郭勒盟', 479, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '兴安盟', 482, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('内蒙古', '阿拉善盟', 483, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '沈阳', 24, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '铁岭', 24, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '大连', 411, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '鞍山', 412, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '抚顺', 24, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '本溪', 414, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '丹东', 415, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '锦州', 416, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '营口', 417, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '阜新', 418, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '辽阳', 419, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '朝阳', 421, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '盘锦', 427, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('辽宁', '葫芦岛', 429, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '长春', 431, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '吉林', 432, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '延边朝鲜族自治州', 433, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '四平', 434, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '通化', 435, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '白城', 436, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '辽源', 437, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '松原', 438, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('吉林', '白山', 439, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '哈尔滨', 451, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '齐齐哈尔', 452, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '牡丹江', 453, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '佳木斯', 454, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '绥化', 455, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '黑河', 456, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '大兴安岭地区', 457, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '伊春', 458, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '大庆', 459, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '七台河', 464, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '鸡西', 467, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '鹤岗', 468, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('黑龙江', '双鸭山', 469, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('上海', '上海', 21, 21);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '南京', 25, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '无锡', 510, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '镇江', 511, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '苏州', 512, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '南通', 513, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '扬州', 514, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '盐城', 515, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '徐州', 516, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '淮安', 517, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '连云港', 518, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '常州', 519, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '泰州', 523, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江苏', '宿迁', 527, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '衢州', 570, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '杭州', 571, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '湖州', 572, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '嘉兴', 573, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '宁波', 574, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '绍兴', 575, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '台州', 576, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '温州', 577, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '丽水', 578, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '金华', 579, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('浙江', '舟山', 580, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '滁州', 550, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '合肥', 551, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '蚌埠', 552, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '芜湖', 553, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '淮南', 554, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '马鞍山', 555, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '安庆', 556, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '宿州', 557, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '亳州', 558, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '阜阳', 558, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '黄山', 559, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '淮北', 561, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '铜陵', 562, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '宣城', 563, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '六安', 564, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '巢湖', 565, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('安徽', '池州', 566, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '福州', 591, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '厦门', 592, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '宁德', 593, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '莆田', 594, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '泉州', 595, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '漳州', 596, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '龙岩', 597, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '三明', 598, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('福建', '南平', 599, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '鹰潭', 701, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '新余', 790, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '南昌', 791, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '九江', 792, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '上饶', 793, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '抚州', 794, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '宜春', 795, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '吉安', 796, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '赣州', 797, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '景德镇', 798, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('江西', '萍乡', 799, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '菏泽', 530, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '济南', 531, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '青岛', 532, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '淄博', 533, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '德州', 534, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '烟台', 535, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '潍坊', 536, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '济宁', 537, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '泰安', 538, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '临沂', 539, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '滨州', 543, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '东营', 546, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '威海', 631, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '枣庄', 632, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '日照', 633, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '莱芜', 634, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('山东', '聊城', 635, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '商丘', 370, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '郑州', 371, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '安阳', 372, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '新乡', 373, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '许昌', 374, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '平顶山', 375, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '信阳', 376, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '南阳', 377, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '开封', 378, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '洛阳', 379, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '济源', 391, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '焦作', 391, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '鹤壁', 392, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '濮阳', 393, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '周口', 394, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '漯河', 395, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '驻马店', 396, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('河南', '三门峡', 398, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '武汉', 27, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '襄樊', 710, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '鄂州', 711, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '孝感', 712, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '黄冈', 713, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '黄石', 714, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '咸宁', 715, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '荆州', 716, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '宜昌', 717, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '恩施土家族苗族自治州', 718, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '十堰', 719, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '随州', 722, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '荆门', 724, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖北', '仙桃市', 728, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '岳阳', 730, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '长沙', 731, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '湘潭', 731, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '株洲', 731, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '衡阳', 734, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '郴州', 735, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '常德', 736, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '益阳', 737, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '娄底', 738, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '邵阳', 739, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '湘西土家族苗族自治州', 743, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '张家界', 744, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '怀化', 745, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('湖南', '永州', 746, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '广州', 20, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '汕尾', 660, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '阳江', 662, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '揭阳', 663, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '茂名', 668, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '江门', 750, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '韶关', 751, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '惠州', 752, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '梅州', 753, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '汕头', 754, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '深圳', 755, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '珠海', 756, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '佛山', 757, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '肇庆', 758, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '湛江', 759, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '中山', 760, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '河源', 762, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '清远', 763, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '云浮', 766, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '潮州', 768, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广东', '东莞', 769, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '防城港', 770, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '崇左', 771, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '南宁', 771, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '来宾', 772, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '柳州', 772, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '桂林', 773, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '贺州', 774, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '梧州', 774, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '贵港', 775, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '玉林', 775, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '百色', 776, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '钦州', 777, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '河池', 778, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('广西', '北海', 779, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('海南', '洋浦', 890, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('海南', '海口', 898, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('海南', '三亚', 898, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('海南', '三沙', 898, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('重庆', '重庆', 23, 23);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '成都', 28, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '攀枝花', 812, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '自贡', 813, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '绵阳', 816, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '南充', 817, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '达州', 818, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '遂宁', 825, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '广安', 826, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '巴中', 827, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '泸州', 830, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '宜宾', 831, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '内江', 832, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '资阳', 28, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '乐山', 833, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '眉山', 28, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '凉山彝族自治州', 834, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '雅安', 835, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '甘孜藏族自治州', 836, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '阿坝藏族羌族自治州', 837, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '德阳', 838, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('四川', '广元', 839, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '贵阳', 851, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '遵义', 852, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '遵义', 852, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '黔南布依族苗族自治州', 854, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '黔东南苗族侗族自治州', 855, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '铜仁地区', 856, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '毕节地区', 857, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '六盘水', 858, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('贵州', '黔西南布依族苗族自治州', 859, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '西双版纳傣族自治州', 691, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '德宏傣族景颇族自治州', 692, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '昭通', 870, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '昆明', 871, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '大理白族自治州', 872, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '红河哈尼族彝族自治州', 873, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '曲靖', 874, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '保山', 875, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '文山壮族苗族自治州', 876, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '楚雄彝族自治州', 878, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '思茅', 879, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '临沧', 883, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '怒江傈僳族自治州', 886, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '迪庆藏族自治州', 887, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('云南', '丽江', 888, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('西藏', '拉萨', 891, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('西藏', '日喀则地区', 892, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('西藏', '山南地区', 893, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('西藏', '林芝地区', 894, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('西藏', '昌都地区', 895, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('西藏', '那曲地区', 896, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('西藏', '阿里地区', 897, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '西安', 29, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '咸阳', 29, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '延安', 911, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '榆林', 912, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '渭南', 913, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '商洛', 914, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '安康', 915, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '汉中', 916, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '宝鸡', 917, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('陕西', '铜川', 919, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '临夏回族自治州', 930, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '兰州', 931, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '定西', 932, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '平凉', 933, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '庆阳', 934, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '金昌', 935, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '武威', 935, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '张掖', 936, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '嘉峪关', 937, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '酒泉', 937, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '天水', 938, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '陇南', 939, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '甘南藏族自治州', 941, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('甘肃', '白银', 943, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '海北藏族自治州', 970, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '西宁', 971, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '海东地区', 972, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '黄南藏族自治州', 973, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '海南藏族自治州', 974, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '果洛藏族自治州', 975, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '玉树藏族自治州', 976, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '海西蒙古族藏族自治州', 979, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '茫崖', 9840, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '大柴旦', 9848, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('青海', '冷湖', 9849, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('宁夏', '银川', 951, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('宁夏', '石嘴山', 952, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('宁夏', '吴忠', 953, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('宁夏', '固原', 954, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('宁夏', '中卫', 955, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '塔城地区', 901, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '哈密地区', 902, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '和田地区', 903, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '阿勒泰地区', 906, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '克孜勒苏柯尔克孜自治州', 908, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '博尔塔拉蒙古自治州', 909, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '克拉玛依', 990, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '乌鲁木齐', 991, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '昌吉回族自治州', 994, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '吐鲁番地区', 995, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '巴音郭楞蒙古自治州', 996, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '阿克苏地区', 997, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '喀什地区', 998, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('新疆', '伊犁哈萨克自治州', 999, 991);
COMMIT;
/

--建36X4张历史表
DECLARE
PITYPE INT;
PICURTYPE INT;
PINUM  INT;
PICURNUM INT;
PIYM INT;
ERRO_NUM INT;
TACNT INT;
PITABLENAME VARCHAR2(20);
BEGIN
  PITYPE:=4;
  PICURTYPE:=0;
  PINUM:=36;
  ERRO_NUM:=0;

  <<OUTER>>
  WHILE PICURTYPE<=PITYPE LOOP--表类型
  PICURNUM:=0;
    <<INNER>>
    WHILE PICURNUM<PINUM LOOP--表个数
        TACNT:=0;
        PIYM:=CAST(TO_CHAR(ADD_MONTHS(SYSDATE,PICURNUM),'YYYYMM')AS INT);
        --DBMS_OUTPUT.PUT_LINE(PIYM);

        --要创建的表存在，则跳过，创建下一张
        IF PICURTYPE=1 THEN
          PITABLENAME:='MTTASK'||CAST(PIYM AS CHAR);
        ELSIF PICURTYPE=2 THEN
          PITABLENAME:='MMSTASK'||CAST(PIYM AS CHAR);
        ELSIF PICURTYPE=3 THEN
          PITABLENAME:='MOTASK'||CAST(PIYM AS CHAR);
        ELSIF PICURTYPE=4 THEN
          PITABLENAME:='MMSMOTASK'||CAST(PIYM AS CHAR);
        ELSE
          PITABLENAME:='GW_MTDRPT'||CAST(PIYM AS CHAR);
        END IF;

        SELECT COUNT(1) INTO TACNT FROM USER_TABLES T WHERE T.TABLE_NAME=PITABLENAME;
        IF TACNT=0 THEN--表不存在
          <<LABEL_LOCAL_ERROR>>
          BEGIN
            IF (PICURTYPE=0)  THEN
               --DBMS_OUTPUT.PUT_LINE(PICURTYPE||' ' ||PITABLENAME);
               GW_CMTDATARPT(PIYM);
            ELSE
               CREATETABLE(PICURTYPE,PIYM);
               --DBMS_OUTPUT.PUT_LINE(PICURTYPE||' ' ||PITABLENAME);
            END IF;
          EXCEPTION WHEN OTHERS THEN
          ERRO_NUM:=ERRO_NUM+1;
          --DBMS_OUTPUT.PUT_LINE('创建历史表出现异常，程序将退出'||SQLERRM);
          IF ERRO_NUM>=6 THEN
            RETURN;
          ELSE
            GOTO LABEL_LOCAL_ERROR;
          END IF;--END OF ERRO_NUM>=5
          END;--END OF EXCEPTION WHEN OTHERS THEN
        END IF;--END OF IF TACNT=0
        PICURNUM:=PICURNUM+1;
    END LOOP INNER;
    PICURTYPE:=PICURTYPE+1;
  END LOOP OUTER;
END;
/

--国际码
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马来西亚'	                    ,'0060'   ,60   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('印度尼西亚'	                  ,'0062'   ,62   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('菲律宾'	                      ,'0063'   ,63   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('新加坡'	                      ,'0065'   ,65   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('泰国'	                        ,'0066'   ,66   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('文莱'	                        ,'00673'  ,673  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('日本'	                        ,'0081'   ,81   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('韩国'	                        ,'0082'   ,82   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('越南'	                        ,'0084'   ,84   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('朝鲜'	                        ,'00850'  ,850  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('香港'	                        ,'00852'  ,852  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('澳门'	                        ,'00853'  ,853  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('柬埔寨'	                      ,'00855'  ,855  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('老挝'	                        ,'00856'  ,856  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('中国'	                        ,'0086'   ,86   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('台湾'	                        ,'00886'  ,886  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('孟加拉国'	                    ,'00880'  ,880  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('土耳其'	                      ,'0090'   ,90   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('印度'	                        ,'0091'   ,91   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴基斯坦'	                    ,'0092'   ,92   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿富汗'	                      ,'0093'   ,93   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('斯里兰卡'	                    ,'0094'   ,94   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('缅甸'	                        ,'0095'   ,95   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马尔代夫'	                    ,'00960'  ,960  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('黎巴嫩'	                      ,'00961'  ,961  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('约旦'	                        ,'00962'  ,962  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('叙利亚'	                      ,'00963'  ,963  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('伊拉克'	                      ,'00964'  ,964  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('科威特'	                      ,'00965'  ,965  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('沙特阿拉伯'	                  ,'00966'  ,966  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿曼'	                        ,'00968'  ,968  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('以色列'	                      ,'00972'  ,972  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴林'	                        ,'00973'  ,973  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('卡塔尔'	                      ,'00974'  ,974  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('不丹'	                        ,'00975'  ,975  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('蒙古'	                        ,'00976'  ,976  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('尼泊尔'	                      ,'00977'  ,977  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('伊朗'	                        ,'0098'   ,98   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('塞浦路斯'	                    ,'00357'  ,357  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴勒斯坦'	                    ,'00970'  ,970  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿联酋'	                      ,'00971'  ,971  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('也门'	                        ,'00967'  ,967  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('津巴布韦'	                    ,'00263'  ,263  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('纳米比亚'	                    ,'00264'  ,264  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马拉维'	                      ,'00265'  ,265  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('莱索托'	                      ,'00266'  ,266  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('博茨瓦纳'	                    ,'00267'  ,267  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('埃及'	                        ,'0020'   ,20   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('希腊'	                        ,'0030'   ,30   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('荷兰'	                        ,'0031'   ,31   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('比利时'	                      ,'0032'   ,32   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('法国'	                        ,'0033'   ,33   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('西班牙'	                      ,'0034'   ,34   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('直布罗陀'	                    ,'00350'  ,350  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('葡萄牙'	                      ,'00351'  ,351  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('卢森堡'	                      ,'00352'  ,352  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('爱尔兰'	                      ,'00353'  ,353  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('冰岛'	                        ,'00354'  ,354  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿尔巴尼亚'	                  ,'00355'  ,355  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马耳他'	                      ,'00356'  ,356  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('安道尔'	                      ,'00376'  ,376  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('芬兰'	                        ,'00358'  ,358  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('保加利亚'	                    ,'00359'  ,359  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('匈牙利'	                      ,'0036'   ,36   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('德国'	                        ,'0049'   ,49   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('南斯拉夫'	                    ,'00381'  ,381  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('意大利'	                      ,'0039'   ,39   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣马力诺'	                    ,'00378'  ,378  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('梵蒂冈'	                      ,'00379'  ,379  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('罗马尼亚'	                    ,'0040'   ,40   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('瑞士'	                        ,'0041'   ,41   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('列支敦士登'	                  ,'00423'  ,423  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('奥地利'	                      ,'0043'   ,43   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('英国'	                        ,'0044'   ,44   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('丹麦'	                        ,'0045'   ,45   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('瑞典'	                        ,'0046'   ,46   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('挪威'	                        ,'0047'   ,47   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('波兰'	                        ,'0048'   ,48   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('捷克'	                        ,'00420'  ,420  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('斯洛伐克'                      ,'00421'  ,421  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('摩纳哥'	                      ,'00377'  ,377  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马其顿'	                      ,'00389'  ,389  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('科罗地亚'                      ,'00385'  ,385  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('斯洛文尼亚'	                  ,'00386'  ,386  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('吉布提'	                      ,'00253'  ,253  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('肯尼亚'	                      ,'00254'  ,254  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('坦桑尼亚'	                    ,'00255'  ,255  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('乌干达'	                      ,'00256'  ,256  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('布隆迪'	                      ,'00257'  ,257  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('莫桑比克'	                    ,'00258'  ,258  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('赞比亚'	                      ,'00260'  ,260  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马达加斯加'                    ,'00261'  ,261  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('留尼旺岛'	                    ,'00262'  ,262  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('立陶宛'	                      ,'00370'  ,370  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('拉脱维亚'	                    ,'00371'  ,371  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('爱沙尼亚'	                    ,'00372'  ,372  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('摩尔多瓦'	                    ,'00373'  ,373  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿塞拜疆'	                    ,'00994'  ,994  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('斯威士兰'	                    ,'00268'  ,268  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('乌克兰'	                      ,'00380'  ,380  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('南非'	                        ,'0027'   ,27   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣赫勒拿'	                    ,'00290'  ,290  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿鲁巴岛'	                    ,'00297'  ,297  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('法罗群岛'	                    ,'00298'  ,298  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('厄里特里亚'	                  ,'00291'  ,291  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('迪戈加西亚'	                  ,'00246'  ,246  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('桑给巴尔'	                    ,'00259'  ,259  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('瓜多罗普'	                    ,'00590'  ,590  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('美国/加拿大'	                  ,'001'    ,1    );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('摩洛哥'	                      ,'00212'  ,212  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿尔及利亚'	                  ,'00213'  ,213  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('突尼斯'	                      ,'00216'  ,216  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('利比亚'	                      ,'00218'  ,218  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('冈比亚'	                      ,'00220'  ,220  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('塞内加尔'	                    ,'00221'  ,221  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('毛里塔尼亚'	                  ,'00222'  ,222  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马里'	                        ,'00223'  ,223  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('几内亚'	                      ,'00224'  ,224  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('科特迪瓦'	                    ,'00225'  ,225  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('布基拉法索'	                  ,'00226'  ,226  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('尼日尔'	                      ,'00227'  ,227  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('多哥'	                        ,'00228'  ,228  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('贝宁'	                        ,'00229'  ,229  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('毛里求斯'	                    ,'00230'  ,230  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('利比里亚'	                    ,'00231'  ,231  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('塞拉利昂'	                    ,'00232'  ,232  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('加纳'                          ,'00233'  ,233  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('尼日利亚'	                    ,'00234'  ,234  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('乍得'	                        ,'00235'  ,235  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('中非'	                        ,'00236'  ,236  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('喀麦隆'	                      ,'00237'  ,237  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('佛得角'	                      ,'00238'  ,238  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('赤道几内亚'	                  ,'00240'  ,240  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('加蓬'	                        ,'00241'  ,241  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('刚果'	                        ,'00242'  ,242  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('扎伊尔'	                      ,'00243'  ,243  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('安哥拉'	                      ,'00244'  ,244  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('几内亚比绍'	                  ,'00245'  ,245  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿森松'	                      ,'00247'  ,247  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('塞舌尔'	                      ,'00248'  ,248  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('苏丹'	                        ,'00249'  ,249  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('卢旺达'	                      ,'00250'  ,250  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('埃塞俄比亚'	                  ,'00251'  ,251  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('索马里'	                      ,'00252'  ,252  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('澳大利亚'	                    ,'0061'   ,61   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('伯利兹'	                      ,'00501'  ,501  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('危地马拉'	                    ,'00502'  ,502  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('萨尔瓦多'	                    ,'00503'  ,503  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('洪都拉斯'	                    ,'00504'  ,504  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('尼加拉瓜'	                    ,'00505'  ,505  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('哥斯达黎加'	                  ,'00506'  ,506  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴拿马'	                      ,'00507'  ,507  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('海地'	                        ,'00509'  ,509  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('秘鲁'	                        ,'0051'   ,51   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('墨西哥'	                      ,'0052'   ,52   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('古巴'	                        ,'0053'   ,53   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿根廷'	                      ,'0054'   ,54   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴西'	                        ,'0055'   ,55   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('智利'	                        ,'0056'   ,56   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('哥伦比亚'	                    ,'0057'   ,57   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('图瓦卢'	                      ,'00688'  ,688  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('玻利维亚'	                    ,'00591'  ,591  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圭亚那'	                      ,'00592'  ,592  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('厄瓜多尔'	                    ,'00593'  ,593  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('法属圭亚那'	                  ,'00594'  ,594  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴拉圭'	                      ,'00595'  ,595  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马提尼克'	                    ,'00596'  ,596  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('苏里南'	                      ,'00597'  ,597  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('乌拉圭'	                      ,'00598'  ,598  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('新西兰'	                      ,'0064'   ,64   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('关岛'	                        ,'001671' ,1671 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('科科斯岛'	                    ,'0061891',61891);
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣诞岛'	                      ,'00618'  ,618  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('瑙鲁'	                        ,'00674'  ,674  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('汤加'	                        ,'00676'  ,676  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('所罗门群岛'	                  ,'00677'  ,677  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('瓦努阿图'	                    ,'00678'  ,678  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('斐济'	                        ,'00679'  ,679  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('科克群岛'	                    ,'00682'  ,682  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('纽埃岛'	                      ,'00683'  ,683  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('东萨摩亚'	                    ,'00684'  ,684  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('西萨摩亚'	                    ,'00685'  ,685  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('基里巴斯'	                    ,'00686'  ,686  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马里亚纳群岛'	                ,'001670' ,1670 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('新咯里多尼亚群岛'	            ,'00687'  ,687  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴布亚新几内亚'	              ,'00675'  ,675  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('帕劳'	                        ,'00680'  ,680  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('托克鲁'	                      ,'00690'  ,690  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('密克罗尼西亚'	                ,'00691'  ,691  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('马绍尔群岛'	                  ,'00692'  ,692  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('福克兰群岛'	                  ,'00500'  ,500  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('中途岛/夏威夷'	                ,'001808' ,1808 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('威克岛'	                      ,'00808'  ,808  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('安圭拉岛'	                    ,'001264' ,1264 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('英属维尔京群岛'	              ,'001284' ,1284 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('美属维尔京群岛'	              ,'001340' ,1340 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣卢西亚'	                    ,'001758' ,1758 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('牙买加'	                      ,'001876' ,1876 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴哈马'	                      ,'001242' ,1242 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('巴巴多斯'	                    ,'001246' ,1246 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('阿拉斯加'	                    ,'001907' ,1907 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('格陵兰岛'	                    ,'00299'  ,299  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('安提瓜和巴布达'	              ,'001268' ,1268 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('百慕大群岛'	                  ,'001441' ,1441 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('开曼群岛'	                    ,'001345' ,1345 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('多米尼加联邦'	                ,'001767' ,1767 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('委内瑞拉'	                    ,'0058'   ,58   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('格林纳达'	                    ,'001473' ,1473 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('蒙特塞拉特岛'	                ,'001664' ,1664 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('荷属安的列斯群岛'              ,'00599'  ,599  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('波多黎哥'	                    ,'001787' ,1787 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('俄罗斯联邦/哈萨克斯坦共和国'	  ,'007'    ,7    );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('波斯尼亚和塞哥维那'	          ,'00387'  ,387  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('亚美尼亚共和国'	              ,'00374'  ,374  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('白俄罗斯共和国'	              ,'00375'  ,375  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('格鲁吉亚共和国'	              ,'00995'  ,995  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('吉尔吉斯坦共和国'	            ,'00996'  ,996  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('乌兹别克斯坦共和国'	          ,'00998'  ,998  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('塔吉克斯坦共和国'	            ,'00992'  ,992  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('土库曼斯坦共和国'	            ,'00993'  ,993  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('科摩罗/马约特岛'	              ,'00269'  ,269  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣皮埃尔岛密克隆岛(法)'	      ,'00508'  ,508  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣克里斯托弗和尼维斯'	        ,'001869' ,1869 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣文森特岛(英)'	              ,'001784' ,1784 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('特立尼达和多巴哥'	            ,'001868' ,1868 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('特克斯和凯科斯群岛'	          ,'001649' ,1649 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('瓦里斯和富士那群岛'	          ,'00681'  ,681  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('多米尼加共和国/波多黎各'	      ,'001809' ,1809 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('加那利群岛(西)(拉斯帕尔马斯)'  ,'0034928',34928);
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('加那利群岛(西)(圣克鲁斯)'	    ,'0034922',34922);
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('法属波里尼西亚/塔希提'	        ,'00689'  ,689  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('圣多美/普林西比'	              ,'00239'  ,239  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('诺福克岛/南极洲'	              ,'00672'  ,672  );
COMMIT;
/
DELETE FROM GW_BASEPARA WHERE ID BETWEEN 1 AND 166;

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(1,'single_send',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(2,'single_send',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(3,'single_send',1,'mobile',21,'短信接收的手机号：只能填一个手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(4,'single_send',1,'content',1000,'短信内容：最大支持1000个字',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(5,'single_send',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(6,'single_send',1,'svrtype',32,'业务类型：最大可支持32个长度的英文数字组合的字符串',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(7,'single_send',1,'exno',6,'扩展号:长度不能超过6位',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(8,'single_send',1,'custid',64,'用户自定义流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(9,'single_send',1,'exdata',64,'自定义扩展数据',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(10,'single_send',1,'param1',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(11,'single_send',1,'param2',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(12,'single_send',1,'param3',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(13,'single_send',1,'param4',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(14,'single_send',1,'moduleid',4,'模块ID，4字节整型正数（0~2^31-1）',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(15,'single_send',1,'Attime',14,'短信定时发送时间',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(16,'single_send',1,'Validtime',14,'短信有效存活时间（可选）',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(17,'single_send',1,'Rptflag',4,'是否需要状态报告,0:表示不需要，非0:表示需要',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(18,'single_send',2,'result',4,'短信发送请求处理结果0：成功,非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(19,'single_send',2,'msgid',64,'平台流水号:非0,64位整型,对应Java和C#的long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(20,'single_send',2,'custid',64,'用户自定义流水号',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(21,'batch_send',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(22,'batch_send',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(23,'batch_send',1,'mobile',21,'短信接收的手机号：只能填一个手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(24,'batch_send',1,'content',1000,'短信内容：最大支持1000个字',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(25,'batch_send',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(26,'batch_send',1,'svrtype',32,'业务类型：最大可支持32个长度的英文数字组合的字符串',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(27,'batch_send',1,'exno',6,'扩展号:长度不能超过6位',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(28,'batch_send',1,'custid',64,'用户自定义流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(29,'batch_send',1,'exdata',64,'自定义扩展数据',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(30,'batch_send',1,'param1',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(31,'batch_send',1,'param2',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(32,'batch_send',1,'param3',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(33,'batch_send',1,'param4',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(34,'batch_send',1,'moduleid',4,'模块ID，4字节整型正数（0~2^31-1）',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(35,'batch_send',1,'Attime',14,'短信定时发送时间',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(36,'batch_send',1,'Validtime',14,'短信有效存活时间（可选）',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(37,'batch_send',1,'Rptflag',4,'是否需要状态报告,0:表示不需要，非0:表示需要',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(38,'batch_send',2,'result',4,'短信发送请求处理结果0：成功,非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(39,'batch_send',2,'msgid',64,'平台流水号:非0,64位整型,对应Java和C#的long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(40,'batch_send',2,'custid',64,'用户自定义流水号',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(41,'multi_send',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(42,'multi_send',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(43,'multi_send',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(44,'multi_send',1,'multimt',8000,'个性化信息详情',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(45,'multi_send',1,'mobile',21,'单个手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(46,'multi_send',1,'content',1000,'短信内容：最大支持1000个字',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(47,'multi_send',1,'svrtype',32,'业务类型：最大可支持32个长度的英文数字组合的字符串',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(47,'multi_send',1,'exno',6,'扩展号:长度不能超过6位',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(49,'multi_send',1,'custid',64,'用户自定义流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(50,'multi_send',1,'exdata',64,'自定义扩展数据',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(51,'multi_send',1,'param1',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(52,'multi_send',1,'param2',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(53,'multi_send',1,'param3',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(54,'multi_send',1,'param4',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(55,'multi_send',1,'moduleid',4,'模块ID，4字节整型正数（0~2^31-1）',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(56,'multi_send',1,'Attime',14,'短信定时发送时间',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(57,'multi_send',1,'Validtime',14,'短信有效存活时间（可选）',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(58,'multi_send',1,'Rptflag',4,'是否需要状态报告,0:表示不需要，非0:表示需要',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(59,'multi_send',2,'result',4,'个性化群发请求处理结果0：成功,非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(60,'multi_send',2,'msgid',64,'平台流水号:非0,64位整型,对应Java和C#的long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(61,'multi_send',2,'custid',64,'用户自定义流水号',1);




INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(62,'template_send',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(63,'template_send',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(64,'template_send',1,'tmplid',20,'短信模版编号：长度最大20位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(65,'template_send',1,'mobile',21,'短信接收的手机号：只能填一个手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(66,'template_send',1,'content',1000,'变量名和变量值',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(67,'template_send',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(68,'template_send',1,'svrtype',32,'业务类型：最大可支持32个长度的英文数字组合的字符串',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(69,'template_send',1,'exno',6,'扩展号:长度不能超过6位',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(70,'template_send',1,'custid',64,'用户自定义流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(71,'template_send',1,'exdata',64,'自定义扩展数据',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(72,'template_send',1,'msgtype',64,'消息类型：默认填100',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(73,'template_send',1,'param1',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(74,'template_send',1,'param2',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(75,'template_send',1,'param3',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(76,'template_send',1,'param4',64,'用户自定义参数，最大64个字节',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(77,'template_send',1,'moduleid',4,'模块ID，4字节整型正数（0~2^31-1）',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(78,'template_send',1,'Attime',14,'短信定时发送时间',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(79,'template_send',1,'Validtime',14,'短信有效存活时间（可选）',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(80,'template_send',1,'Rptflag',4,'是否需要状态报告,0:表示不需要，非0:表示需要',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(81,'template_send',2,'result',4,'短信发送请求处理结果0：成功,非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(82,'template_send',2,'msgid',64,'平台流水号:非0,64位整型,对应Java和C#的long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(83,'template_send',2,'custid',64,'用户自定义流水号',1);





INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(84,'get_mo',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(85,'get_mo',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(86,'get_mo',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(87,'get_mo',1,'retsize',4,'每次请求想要获取的上行最大条数',2);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(88,'get_mo',2,'result',4,'短信发送请求处理结果0：成功,非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(89,'get_mo',2,'mos',8000,'短信发送请求处理结果0：成功,非0:失败',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(90,'get_mo',2,'msgid',64,'平台流水号:上行在梦网云通信平台中的唯一编号',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(91,'get_mo',2,'mobile',21,'短信接收的手机号：只能填一个手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(92,'get_mo',2,'spno',21,'完整的通道号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(93,'get_mo',2,'exno',6,'扩展号:长度不能超过6位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(94,'get_mo',2,'rtime',14,'上行返回的时间:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(95,'get_mo',2,'content',1000,'短信内容：最大支持1000个字',1);




INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(96,'get_rpt',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(97,'get_rpt',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(98,'get_rpt',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(99,'get_rpt',1,'retsize',4,'本次请求想要获取的上行最大条数',2);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(100,'get_rpt',2,'result',4,'获取状态报告请求处理结果0:成功,非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(101,'get_rpt',2,'rpts',4,'result非0时rpts为空',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(102,'get_rpt',2,'msgid',64,'平台流水号:上行在梦网云通信平台中的唯一编号',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(103,'get_rpt',2,'custid',64,'用户自定义流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(104,'get_rpt',2,'pknum',4,'当前条数',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(105,'get_rpt',2,'pktotal',4,'总条数',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(106,'get_rpt',2,'mobile',21,'短信接收的手机号：只能填一个手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(107,'get_rpt',2,'spno',21,'完整的通道号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(108,'get_rpt',2,'exno',6,'下行时填写的exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(109,'get_rpt',2,'stime',19,'状态报告对应的下行发送时间:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(110,'get_rpt',2,'rtime',14,'状态报告返回时间:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(111,'get_rpt',2,'status',4,'接收状态0:成功 非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(112,'get_rpt',2,'errcode',7,'状态报告错误代码',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(113,'get_rpt',2,'errdesc',15,'状态报告错误代码的描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(114,'get_rpt',2,'exdata',64,'下行时填写的exdata',1);





INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(115,'get_balance',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(116,'get_balance',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(117,'get_balance',1,'timestamp',10,'时间戳:24小时制格式:MMDDHHMMSS,定长10位',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(119,'get_balance',2,'result',4,'查询余额请求处理结果0:成功,非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(120,'get_balance',2,'chargetype',4,'计费类型0:条数计费1:金额计费',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(121,'get_balance',2,'balance',4,'短信余额总条数',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(122,'get_balance',2,'money',32,'result非0时rpts为空',1);



INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(123,'MO',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(124,'MO',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(125,'MO',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(126,'MO',1,'cmd',32,'推送上行请求命令:必须填MO_REQ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(127,'MO',1,'seqid',4,'请求消息流水号',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(128,'MO',1,'mos',8000,'上行信息',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(129,'MO',1,'msgid',64,'平台流水号:对应下行请求返回结果中的msgid',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(130,'MO',1,'mobile',21,'手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(131,'MO',1,'spno',21,'下行时填写的exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(132,'MO',1,'exno',6,'下行时填写的exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(133,'MO',1,'rtime',14,'上行返回的时间YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(134,'MO',1,'content',1000,'短信内容:最大支持1000个字',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(135,'MO',2,'cmd',32,'必须填MO_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(136,'MO',2,'seqid',4,'与请求中的seqid保持一致',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(137,'MO',2,'result',4,'上行短消息请求处理结果0:成功,非0:失败',2);




INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(138,'RPT',1,'userid',6,'用户账号：长度最大6个字符，统一大写',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(139,'RPT',1,'pwd',32,'用户密码：定长小写32位字符',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(140,'RPT',1,'timestamp',10,'时间戳：24小时制格式：MMDDHHMMSS，定长10位',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(141,'RPT',1,'cmd',32,'推送状态报告请求命令:必须填RPT_REQ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(142,'RPT',1,'seqid',4,'请求消息流水号',2);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(143,'RPT',1,'msgid',64,'平台流水号:对应下行请求返回结果中的msgid',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(144,'RPT',1,'custid',64,'用户自定义流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(145,'RPT',1,'pknum',4,'当前条数',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(146,'RPT',1,'pktotal',4,'总条数',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(147,'RPT',1,'mobile',21,'手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(148,'RPT',1,'spno',21,'完整的通道号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(149,'RPT',1,'exno',6,'下行时填写的exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(150,'RPT',1,'stime',19,'状态报告对应的下行发送时间:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(151,'RPT',1,'rtime',14,'状态报告返回时间:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(152,'RPT',1,'status',4,'接收状态0:成功 非0:失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(153,'RPT',1,'errcode',7,'状态报告错误代码',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(154,'RPT',1,'errdesc',15,'状态报告错误代码的描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(155,'RPT',1,'exdata',64,'下行时填写的exdata',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(156,'RPT',1,'rpts',8000,'状态报告',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(157,'RPT',2,'cmd',32,'必须填RPT_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(158,'RPT',2,'seqid',4,'与请求中的seqid保持一致',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(159,'RPT',2,'result',4,'result',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(160,'single_send',1,'apikey',32,'用户唯一标识:32位长度,由梦网提供',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(161,'batch_send',1,'apikey',32,'用户唯一标识:32位长度,由梦网提供',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(162,'multi_send',1,'apikey',32,'用户唯一标识:32位长度,由梦网提供',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(163,'template_send',1,'apikey',32,'用户唯一标识:32位长度,由梦网提供',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(164,'get_mo',1,'apikey',32,'用户唯一标识:32位长度,由梦网提供',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(165,'get_rpt',1,'apikey',32,'用户唯一标识:32位长度,由梦网提供',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(166,'get_balance',1,'apikey',32,'用户唯一标识:32位长度,由梦网提供',1);
DELETE FROM GW_BASEPARA WHERE ID BETWEEN 167 AND 180;
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(167,'single_send',1,'mtreq',300,'mtreq',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(168,'batch_send',1,'mtreq',16,'mtreq',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(169,'template_send',1,'mtreq',32,'mtreq',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(170,'multi_send',1,'mtreq',32,'mtreq',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(171,'multi_send',1,'mt',32,'mt',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(171,'multi_send',2,'mtrsp',32,'mtrsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(170,'get_mo',1,'moreq',64,'XML中moreq描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(171,'get_mo',2,'morsp',64,'morsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(172,'get_mo',2,'mo',64,'mo',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(173,'get_rpt',1,'rptreq',3600,'请求获取RPT',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(174,'get_rpt',2,'rptrsp',3600,'rptrsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(175,'get_rpt',2,'rpt',3600,'rpt',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(176,'get_balance',1,'feereq',200,'feereq',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(177,'get_balance',2,'feersp',200,'feersp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(178,'single_send',2,'mtrsp',32,'mtrsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(179,'batch_send',2,'mtrsp',64,'mtrsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(180,'template_send',2,'mtrsp',32,'mtreq',1);
update GW_BASEPARA set ARGNAME=LOWER(ARGNAME)  WHERE ID BETWEEN 0 AND 180;

DELETE FROM GW_BASEPARA WHERE ID BETWEEN 181 AND 258;
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(181,'single_send',3,'result',4,'result返回参数',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(182,'single_send',3,'msgid',4,'msgid返回失败',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(183,'single_send',3,'custid',64,'custid返回失败',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(184,'single_send',3,'mtrsp',32,'mtrsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(185,'single_send',5,'result',500,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(186,'single_send',5,'msgid',500,'msgid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(187,'single_send',5,'custid',500,'custid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(188,'single_send',5,'mtrsp',500,'mtrsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(189,'batch_send',3,'result',4,'失败回应',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(190,'batch_send',3,'msgid',8,'流水号',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(191,'batch_send',3,'custid',64,'客户id',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(192,'batch_send',3,'mtrsp',64,'mtrsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(193,'batch_send',4,'failmap',32,'failmap',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(194,'batch_send',4,'succmap',32,'成功回应',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(195,'batch_send',4,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(196,'batch_send',4,'mt',32,'mt',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(197,'batch_send',4,'mobile',32,'手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(198,'batch_send',4,'msgid',32,'流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(199,'batch_send',4,'custid',32,'客户ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(200,'batch_send',4,'mtrsp',32,'mt回应',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(201,'batch_send',5,'mtrsp',32,'mt回应',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(202,'batch_send',5,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(203,'batch_send',5,'msgid',32,'msgid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(204,'batch_send',5,'custid',32,'客户ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(205,'batch_send',5,'mobile',32,'手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(206,'batch_send',5,'mt',64,'mt',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(207,'multi_send',3,'result',4,'result',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(208,'multi_send',3,'msgid',8,'msgid',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(209,'multi_send',3,'custid',64,'custid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(210,'multi_send',3,'mtrsp',32,'mtrsp',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(211,'multi_send',4,'failmap',32,'failmap',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(212,'multi_send',4,'succmap',32,'成功回应',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(213,'multi_send',4,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(214,'multi_send',4,'mt',32,'mt',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(215,'multi_send',4,'mobile',32,'手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(216,'multi_send',4,'msgid',32,'流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(217,'multi_send',4,'custid',32,'客户ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(218,'multi_send',4,'mtrsp',32,'mt回应',1);

 INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(219,'multi_send',5,'mtrsp',32,'mt回应',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(220,'multi_send',5,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(221,'multi_send',5,'msgid',32,'msgid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(222,'multi_send',5,'custid',32,'客户ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(223,'multi_send',5,'mobile',32,'手机号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(224,'multi_send',5,'mt',32,'mt',1);



INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(225,'get_mo',3,'result',4,'result描述',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(226,'get_mo',3,'mos',64,'mos描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(227,'get_mo',3,'morsp',64,'morsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(228,'get_rpt',3,'result',20,'获取状态报告请求处理结果',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(229,'get_rpt',3,'rpts',20,'result非0时rpt为空',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(230,'get_rpt',3,'rptrsp',3600,'rptrsp',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(231,'get_balance',5,'feersp',200,'feersp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(232,'get_balance',5,'result',200,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(233,'get_balance',5,'balance',200,'balance',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(234,'MO',3,'cmd',64,'全失败cmd描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(235,'MO',3,'seqid',4,'全失败seqid描述',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(236,'MO',3,'result',4,'全失败result描述',2);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(237,'MO',4,'modetails',64,'详细信息描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(238,'MO',4,'morsp',64,'morsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(239,'MO',4,'failmap',64,'failmap描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(240,'MO',4,'msgid',64,'msgid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(241,'MO',4,'succmap',64,'succmap描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(242,'MO',4,'result',4,'部分result描述',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(243,'MO',4,'seqid',4,'部分seqid描述',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(244,'MO',4,'cmd',64,'cmd描述',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(245,'RPT',3,'cmd',20,'必须填RPT_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(246,'RPT',3,'seqid',15,'与请求中的seqid保持一致',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(247,'RPT',3,'result',15,'状态报告请求处理结果',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(248,'RPT',4,'rptdetails',200,'rptdetails',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(249,'RPT',4,'rptrsp',200,'rptrsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(250,'RPT',4,'failmap',200,'部分失败标签',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(251,'RPT',4,'msgid',32,'平台流水号',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(252,'RPT',4,'succmap',200,'部分成功标签',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(253,'RPT',4,'result',4,'状态报告请求处理结果',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(254,'RPT',4,'seqid',4,'与请求中的seqid保持一致',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(255,'RPT',4,'cmd',8,'RPT_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(256,'get_balance',3,'feersp',200,'feersp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(257,'get_balance',3,'result',4,'返回值',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(258,'get_balance',3,'balance',4,'失败查询余额',2);

DELETE FROM GW_BASEPARA WHERE ID BETWEEN 259 AND 264;
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(259,'RPT',1,'rptreq',3600,'状态报告主动推送',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(260,'RPT',2,'rptrsp',3600,'rptrsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(261,'RPT',3,'rptrsp',3600,'rptrsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(262,'MO',1,'moreq',64,'XML中moreq描述',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(263,'MO',2,'morsp',64,'morsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(264,'MO',3,'morsp',64,'morsp',1);
COMMIT;
/

DELETE FROM GW_BASEPARA WHERE ID BETWEEN 265 AND 266;
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(265,'RPT',1,'rpt',3600,'rpt',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(266,'MO',1,'mo',64,'mo',1);

DELETE FROM GW_BASEPARA WHERE ID BETWEEN 267 AND 453;
-- API5.5单条发送接口send_single
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(267, 'send_single',   1,  'userid',    6,    '用户账号：长度最大6个字符，统一大写',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(268, 'send_single',   1,  'pwd',       32,   '用户密码：定长小写32位字符',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(269, 'send_single',   1,  'mobile',    21,   '短信接收的手机号：只能填一个手机号',                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(270, 'send_single',   1,  'content',   1000, '短信内容：最大支持1000个字',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(271, 'send_single',   1,  'timestamp', 10,   '时间戳：24小时制格式：MMDDHHMMSS，定长10位',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(272, 'send_single',   1,  'svrtype',   32,   '业务类型：最大可支持32个长度的英文数字组合的字符串', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(273, 'send_single',   1,  'exno',      6,    '扩展号:长度不能超过6位',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(274, 'send_single',   1,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(275, 'send_single',   1,  'exdata',    64,   '自定义扩展数据',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(276, 'send_single',   1,  'param1',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(277, 'send_single',   1,  'param2',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(278, 'send_single',   1,  'param3',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(279, 'send_single',   1,  'param4',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(280, 'send_single',   1,  'moduleid',  4,    '模块ID，4字节整型正数（0~2^31-1）',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(281, 'send_single',   1,  'attime',    14,   '短信定时发送时间',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(282, 'send_single',   1,  'validtime', 14,   '短信有效存活时间（可选）',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(283, 'send_single',   1,  'rptflag',   4,    '是否需要状态报告,0:表示不需要，非0:表示需要',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(284, 'send_single',   1,  'apikey',    32,   '用户唯一标识:32位长度,由梦网提供',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(285, 'send_single',   1,  'mtreq',     300,  'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(286, 'send_single',   2,  'result',    4,    '短信发送请求处理结果0：成功,非0:失败',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(287, 'send_single',   2,  'msgid',     64,   '平台流水号:非0,64位整型,对应Java和C#的long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(288, 'send_single',   2,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(289, 'send_single',   2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(290, 'send_single',   3,  'result',    4,    'result返回参数',                                     2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(291, 'send_single',   3,  'msgid',     4,    'msgid返回失败',                                      2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(292, 'send_single',   3,  'custid',    64,   'custid返回失败',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(293, 'send_single',   3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(294, 'send_single',   5,  'result',    500,  'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(295, 'send_single',   5,  'msgid',     500,  'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(296, 'send_single',   5,  'custid',    500,  'custid',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(297, 'send_single',   5,  'mtrsp',     500,  'mtrsp',                                              1);

-- API5.5相同内容群发接口send_batch
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(298, 'send_batch',    1,  'userid',    6,    '用户账号：长度最大6个字符，统一大写',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(299, 'send_batch',    1,  'pwd',       32,   '用户密码：定长小写32位字符',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(300, 'send_batch',    1,  'mobile',    21,   '短信接收的手机号：只能填一个手机号',                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(301, 'send_batch',    1,  'content',   1000, '短信内容：最大支持1000个字',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(302, 'send_batch',    1,  'timestamp', 10,   '时间戳：24小时制格式：MMDDHHMMSS，定长10位',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(303, 'send_batch',    1,  'svrtype',   32,   '业务类型：最大可支持32个长度的英文数字组合的字符串', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(304, 'send_batch',    1,  'exno',      6,    '扩展号:长度不能超过6位',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(305, 'send_batch',    1,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(306, 'send_batch',    1,  'exdata',    64,   '自定义扩展数据',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(307, 'send_batch',    1,  'param1',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(308, 'send_batch',    1,  'param2',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(309, 'send_batch',    1,  'param3',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(310, 'send_batch',    1,  'param4',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(311, 'send_batch',    1,  'moduleid',  4,    '模块ID，4字节整型正数（0~2^31-1）',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(312, 'send_batch',    1,  'attime',    14,   '短信定时发送时间',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(313, 'send_batch',    1,  'validtime', 14,   '短信有效存活时间（可选）',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(314, 'send_batch',    1,  'rptflag',   4,    '是否需要状态报告,0:表示不需要，非0:表示需要',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(315, 'send_batch',    1,  'apikey',    32,   '用户唯一标识:32位长度,由梦网提供',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(316, 'send_batch',    1,  'mtreq',     16,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(317, 'send_batch',    2,  'result',    4,    '短信发送请求处理结果0：成功,非0:失败',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(318, 'send_batch',    2,  'msgid',     64,   '平台流水号:非0,64位整型,对应Java和C#的long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(319, 'send_batch',    2,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(320, 'send_batch',    2,  'mtrsp',     64,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(321, 'send_batch',    3,  'result',    4,    '失败回应',                                           2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(322, 'send_batch',    3,  'msgid',     8,    '流水号',                                             4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(323, 'send_batch',    3,  'custid',    64,   '客户id',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(324, 'send_batch',    3,  'mtrsp',     64,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(325, 'send_batch',    4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(326, 'send_batch',    4,  'succmap',   32,   '成功回应',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(327, 'send_batch',    4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(328, 'send_batch',    4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(329, 'send_batch',    4,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(330, 'send_batch',    4,  'msgid',     32,   '流水号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(331, 'send_batch',    4,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(332, 'send_batch',    4,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(333, 'send_batch',    5,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(334, 'send_batch',    5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(335, 'send_batch',    5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(336, 'send_batch',    5,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(337, 'send_batch',    5,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(338, 'send_batch',    5,  'mt',        64,   'mt',                                                 1);

-- API5.5个性化群发接口send_multi
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(339, 'send_multi',    1,  'userid',    6,    '用户账号：长度最大6个字符，统一大写',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(340, 'send_multi',    1,  'pwd',       32,   '用户密码：定长小写32位字符',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(341, 'send_multi',    1,  'timestamp', 10,   '时间戳：24小时制格式：MMDDHHMMSS，定长10位',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(342, 'send_multi',    1,  'multimt',   8000, '个性化信息详情',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(343, 'send_multi',    1,  'mobile',    21,   '单个手机号',                                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(344, 'send_multi',    1,  'content',   1000, '短信内容：最大支持1000个字',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(345, 'send_multi',    1,  'svrtype',   32,   '业务类型：最大可支持32个长度的英文数字组合的字符串', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(346, 'send_multi',    1,  'exno',      6,    '扩展号:长度不能超过6位',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(347, 'send_multi',    1,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(348, 'send_multi',    1,  'exdata',    64,   '自定义扩展数据',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(349, 'send_multi',    1,  'param1',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(350, 'send_multi',    1,  'param2',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(351, 'send_multi',    1,  'param3',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(352, 'send_multi',    1,  'param4',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(353, 'send_multi',    1,  'moduleid',  4,    '模块ID，4字节整型正数（0~2^31-1）',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(354, 'send_multi',    1,  'attime',    14,   '短信定时发送时间',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(355, 'send_multi',    1,  'validtime', 14,   '短信有效存活时间（可选）',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(356, 'send_multi',    1,  'rptflag',   4,    '是否需要状态报告,0:表示不需要，非0:表示需要',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(357, 'send_multi',    1,  'apikey',    32,   '用户唯一标识:32位长度,由梦网提供',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(358, 'send_multi',    1,  'mtreq',     32,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(359, 'send_multi',    1,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(360, 'send_multi',    2,  'result',    4,    '个性化群发请求处理结果0：成功,非0:失败',             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(361, 'send_multi',    2,  'msgid',     64,   '平台流水号:非0,64位整型,对应Java和C#的long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(362, 'send_multi',    2,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(363, 'send_multi',    2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(364, 'send_multi',    3,  'result',    4,    'result',                                             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(365, 'send_multi',    3,  'msgid',     8,    'msgid',                                              4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(366, 'send_multi',    3,  'custid',    64,   'custid',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(367, 'send_multi',    3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(368, 'send_multi',    4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(369, 'send_multi',    4,  'succmap',   32,   '成功回应',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(370, 'send_multi',    4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(371, 'send_multi',    4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(372, 'send_multi',    4,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(373, 'send_multi',    4,  'msgid',     32,   '流水号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(374, 'send_multi',    4,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(375, 'send_multi',    4,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(376, 'send_multi',    5,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(377, 'send_multi',    5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(378, 'send_multi',    5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(379, 'send_multi',    5,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(380, 'send_multi',    5,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(381, 'send_multi',    5,  'mt',        32,   'mt',                                                 1);

-- API5.5个性化群发接口send_mixed
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(382, 'send_mixed',    1,  'userid',    6,    '用户账号：长度最大6个字符，统一大写',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(383, 'send_mixed',    1,  'pwd',       32,   '用户密码：定长小写32位字符',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(384, 'send_mixed',    1,  'timestamp', 10,   '时间戳：24小时制格式：MMDDHHMMSS，定长10位',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(385, 'send_mixed',    1,  'multimt',   8000, '个性化信息详情',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(386, 'send_mixed',    1,  'mobile',    21,   '单个手机号',                                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(387, 'send_mixed',    1,  'content',   1000, '短信内容：最大支持1000个字',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(388, 'send_mixed',    1,  'svrtype',   32,   '业务类型：最大可支持32个长度的英文数字组合的字符串', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(389, 'send_mixed',    1,  'exno',      6,    '扩展号:长度不能超过6位',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(390, 'send_mixed',    1,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(391, 'send_mixed',    1,  'exdata',    64,   '自定义扩展数据',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(392, 'send_mixed',    1,  'param1',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(393, 'send_mixed',    1,  'param2',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(394, 'send_mixed',    1,  'param3',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(395, 'send_mixed',    1,  'param4',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(396, 'send_mixed',    1,  'moduleid',  4,    '模块ID，4字节整型正数（0~2^31-1）',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(397, 'send_mixed',    1,  'attime',    14,   '短信定时发送时间',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(398, 'send_mixed',    1,  'validtime', 14,   '短信有效存活时间（可选）',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(399, 'send_mixed',    1,  'rptflag',   4,    '是否需要状态报告,0:表示不需要，非0:表示需要',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(400, 'send_mixed',    1,  'apikey',    32,   '用户唯一标识:32位长度,由梦网提供',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(401, 'send_mixed',    1,  'mtreq',     32,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(402, 'send_mixed',    2,  'result',    4,    '个性化群发请求处理结果0：成功,非0:失败',             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(403, 'send_mixed',    2,  'msgid',     64,   '平台流水号:非0,64位整型,对应Java和C#的long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(404, 'send_mixed',    2,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(405, 'send_mixed',    2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(406, 'send_mixed',    3,  'result',    4,    'result',                                             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(407, 'send_mixed',    3,  'msgid',     8,    'msgid',                                              4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(408, 'send_mixed',    3,  'custid',    64,   'custid',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(409, 'send_mixed',    3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(410, 'send_mixed',    4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(411, 'send_mixed',    4,  'succmap',   32,   '成功回应',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(412, 'send_mixed',    4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(413, 'send_mixed',    4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(414, 'send_mixed',    4,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(415, 'send_mixed',    4,  'msgid',     32,   '流水号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(416, 'send_mixed',    4,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(417, 'send_mixed',    4,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(418, 'send_mixed',    5,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(419, 'send_mixed',    5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(420, 'send_mixed',    5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(421, 'send_mixed',    5,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(422, 'send_mixed',    5,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(423, 'send_mixed',    5,  'mt',        32,   'mt',                                                 1);

-- API5.5模板发送接口send_template
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(424, 'send_template', 1,  'userid',    6,    '用户账号：长度最大6个字符，统一大写',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(425, 'send_template', 1,  'pwd',       32,   '用户密码：定长小写32位字符',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(426, 'send_template', 1,  'tmplid',    20,   '短信模版编号：长度最大20位字符',                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(427, 'send_template', 1,  'mobile',    21,   '短信接收的手机号：只能填一个手机号',                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(428, 'send_template', 1,  'content',   1000, '变量名和变量值',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(429, 'send_template', 1,  'timestamp', 10,   '时间戳：24小时制格式：MMDDHHMMSS，定长10位',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(430, 'send_template', 1,  'svrtype',   32,   '业务类型：最大可支持32个长度的英文数字组合的字符串', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(431, 'send_template', 1,  'exno',      6,    '扩展号:长度不能超过6位',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(432, 'send_template', 1,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(433, 'send_template', 1,  'exdata',    64,   '自定义扩展数据',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(434, 'send_template', 1,  'param1',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(435, 'send_template', 1,  'param2',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(436, 'send_template', 1,  'param3',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(437, 'send_template', 1,  'param4',    64,   '用户自定义参数，最大64个字节',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(438, 'send_template', 1,  'moduleid',  4,    '模块ID，4字节整型正数（0~2^31-1）',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(439, 'send_template', 1,  'attime',    14,   '短信定时发送时间',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(440, 'send_template', 1,  'validtime', 14,   '短信有效存活时间（可选）',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(441, 'send_template', 1,  'rptflag',   4,    '是否需要状态报告,0:表示不需要，非0:表示需要',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(442, 'send_template', 1,  'apikey',    32,   '用户唯一标识:32位长度,由梦网提供',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(443, 'send_template', 1,  'mtreq',     32,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(444, 'send_template', 2,  'result',    4,    '短信发送请求处理结果0：成功,非0:失败',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(445, 'send_template', 2,  'msgid',     64,   '平台流水号:非0,64位整型,对应Java和C#的long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(446, 'send_template', 2,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(447, 'send_template', 2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(448, 'send_template', 3,  'result',    4,    '短信发送请求处理结果0：成功,非0:失败',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(449, 'send_template', 3,  'msgid',     64,   '平台流水号:非0,64位整型,对应Java和C#的long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(450, 'send_template', 3,  'custid',    64,   '用户自定义流水号',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(451, 'send_template', 3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(454, 'send_template', 4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(455, 'send_template', 4,  'succmap',   32,   '成功回应',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(456, 'send_template', 4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(457, 'send_template', 4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(458, 'send_template', 4,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(459, 'send_template', 4,  'msgid',     32,   '流水号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(460, 'send_template', 4,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(461, 'send_template', 4,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(462, 'send_template', 5,  'mtrsp',     32,   'mt回应',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(463, 'send_template', 5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(464, 'send_template', 5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(465, 'send_template', 5,  'custid',    32,   '客户ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(466, 'send_template', 5,  'mobile',    32,   '手机号',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(467, 'send_template', 5,  'mt',        32,   'mt',                                                 1);

-- API5.5查询余额接口get_balance
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(452, 'get_balance',   3,  'chargetype',4,    '计费类型0:条数计费1:金额计费',                       2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(453, 'get_balance',   3,  'money',     32,   'result非0时rpts为空',                                1);


COMMIT;
/
------------------------初始化数据--------------------
MERGE INTO  GW_MSGTAIL A USING (SELECT '默认全局贴尾' AS "TAIL_NAME",'回复TD退订' AS "CONTENT",'100001' AS "CORP_CODE",0 AS USER_ID FROM  DUAL) B
ON (A.TAIL_NAME=B.TAIL_NAME AND A.CONTENT=B.CONTENT AND A.CORP_CODE=B.CORP_CODE)
WHEN NOT MATCHED THEN
INSERT (TAIL_NAME, CONTENT, CORP_CODE, USER_ID)VALUES('默认全局贴尾','回复TD退订','100001',0);
/

MERGE INTO  GW_TAILBIND A USING (SELECT 0 AS "TAIL_ID", ' ' AS "BUS_CODE", ' ' AS "SPUSERID",
 0 AS "TAIL_TYPE", '100001' AS "CORP_CODE" , 0 AS "USER_ID" FROM  DUAL) B
ON (A.BUS_CODE=B.BUS_CODE AND A.SPUSERID=B.SPUSERID AND A.TAIL_TYPE=B.TAIL_TYPE AND A.CORP_CODE=B.CORP_CODE)
WHEN NOT MATCHED THEN
INSERT(TAIL_ID,BUS_CODE,SPUSERID,TAIL_TYPE,CORP_CODE,USER_ID)VALUES(0,' ',' ', 0, '100001', 0);
/

UPDATE GW_TAILBIND SET TAIL_ID=(SELECT MM.TAIL_ID FROM GW_MSGTAIL MM INNER JOIN GW_TAILBIND TT ON MM.CORP_CODE=TT.CORP_CODE
WHERE MM.CONTENT='回复TD退订' AND MM.TAIL_NAME='默认全局贴尾' AND MM.CORP_CODE='100001' AND ROWNUM<=1);
/

MERGE INTO  GW_TAILCTRL A USING (SELECT 0 AS "OVERTAILFLAG",0 AS "OTHERTAILFLAG",'100001' AS "CORP_CODE" FROM  DUAL) B
ON (A.OVERTAILFLAG=B.OVERTAILFLAG AND A.OTHERTAILFLAG=B.OTHERTAILFLAG AND A.CORP_CODE=B.CORP_CODE)
WHEN NOT MATCHED THEN
INSERT (OVERTAILFLAG, OTHERTAILFLAG, CORP_CODE)VALUES(0,0,'100001');
/

COMMIT;
/
