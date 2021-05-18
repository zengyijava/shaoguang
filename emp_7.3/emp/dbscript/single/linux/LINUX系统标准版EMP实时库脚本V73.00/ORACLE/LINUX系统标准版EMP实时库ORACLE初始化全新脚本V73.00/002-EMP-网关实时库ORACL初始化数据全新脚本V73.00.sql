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
VALUES ('WBS00A', 'WBS00A', 11, 1, '�����ʺ�,�Ա�MSGID,�Դ�����');
COMMIT;

INSERT INTO KF_CORPBASE (ECID, CORPACCOUNT, CORPNAME, LICENSE, OPTYPE, STATUS, SPGATE, ECCPNO, ECSIGN, MAXPERDAY, WHITEVER, ORDERTIME, CANCELTIME, MODITIME, EXPANLIMIT, STAFFLIMIT, MAINNOLIMIT, NEEDSIGN, WHTLEVEL, SPEEDLIMIT, ORDERNUM, EXPRIEDDATE, ECMANGER, ECMEMO, ECIDBAK, CORPACCOUNTBAK)
VALUES (1, '200001', '200001', ' ', '0', 0, '                     ', '                     ', '                ', 10000000, 0, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 10, 10, 10, 0, 0, 1000, 0, '          ', '           ', ' ', 0, ' ');
COMMIT;

-------------------�Ŷα��ʼ��
INSERT INTO PB_SERVICETYPE (SPISUNCM, SERVICENO, SERVICEINFO)
VALUES (0, '134,135,136,137,138,139,147,150,151,158,159,157,154,152,188,187,182,183,184,1705,178,1703,1706', '�ƶ�');
INSERT INTO PB_SERVICETYPE (SPISUNCM, SERVICENO, SERVICEINFO)
VALUES (1, '130,131,132,155,156,185,186,145,176,1709,175,1707,1708,1718,1719', '��ͨ');
INSERT INTO PB_SERVICETYPE (SPISUNCM, SERVICENO, SERVICEINFO)
VALUES (21, '133,153,189,180,181,1700,177,173,1701,1702', '����');
COMMIT;

INSERT INTO KF_PARAMS (PARACODE, PARAVAL1, PARAVAL2, PARAINFO)
VALUES (9001, 0, '134,135,136,137,138,139,147,150,151,158,159,157,154,152,187,188,182,183,184,1705,178,1703,1706', '�ƶ�');
INSERT INTO KF_PARAMS (PARACODE, PARAVAL1, PARAVAL2, PARAINFO)
VALUES (9002, 1, '130,131,132,155,156,185,186,145,176,1709,175,1707,1708,1718,1719', '��ͨ');
INSERT INTO KF_PARAMS (PARACODE, PARAVAL1, PARAVAL2, PARAINFO)
VALUES (9021, 21, '133,153,189,180,181,1700,177,173,1701,1702', '����');
COMMIT;

-----��ʼ�������ʺ�
INSERT INTO USERDATA (USERID, LOGINID, STAFFNAME, CORPACCOUNT, USERACCOUNT, MOBILE, ORDERTIME, CANCELTIME, MODITIME, USERTYPE, STATUS, USERPASSWORD, SMSTYPE, SENDTYPE, FEEFLAG, RETFLAG, USERPRIVILEGE, SUBMITCNT, COMPANY, SALEMAN, MEMO, SPEEDLIMIT, RISELEVEL, MTURL, MOURL, RPTURL, LOGINIP, MAXDAYNUM, SENDTMSPAN, FORBIDTMSPAN,ACCOUNTTYPE)
VALUES ('WBS00A', 'WBS00A', 'WBS�����ʺ�', '200001', '200001', '13800138000', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0, 0, '123456', 0, 0, 2, 0, 11, 0, ' ', ' ', ' ', 0, 0, ' ', ' ', ' ', ' ', 100000, '00:00:00-23:59:59', '00:00:00-00:00:00', 1);
------------------------------------------ѹ������------------------------------
INSERT INTO USERDATA (USERID, LOGINID, STAFFNAME, CORPACCOUNT, USERACCOUNT, MOBILE, ORDERTIME, CANCELTIME, MODITIME, USERTYPE, STATUS, USERPASSWORD, SMSTYPE, SENDTYPE, FEEFLAG, RETFLAG, USERPRIVILEGE, SUBMITCNT, COMPANY, SALEMAN, MEMO, SPEEDLIMIT, RISELEVEL, MTURL, MOURL, RPTURL, LOGINIP, MAXDAYNUM, SENDTMSPAN, FORBIDTMSPAN, ACCOUNTTYPE)
VALUES ( 'END000', 'END000', 'ѹ������ͨ���ʺ�(������ʵʹ��)', '200001', '200001', '13800138000', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 1, 0, '123456', 0, 0, 2, 0, 0, 0, ' ', ' ', ' ', 0, 0, ' ', ' ', ' ', ' ', 100000, '00:00:00-23:59:59', '00:00:00-00:00:00',1);
COMMIT;

INSERT INTO USERDATA (USERID, LOGINID, STAFFNAME, CORPACCOUNT, USERACCOUNT, MOBILE, ORDERTIME, CANCELTIME, MODITIME, USERTYPE, STATUS, USERPASSWORD, SMSTYPE, SENDTYPE, FEEFLAG, RETFLAG, USERPRIVILEGE, SUBMITCNT, COMPANY, SALEMAN, MEMO, SPEEDLIMIT, RISELEVEL, MTURL, MOURL, RPTURL, LOGINIP, MAXDAYNUM, SENDTMSPAN, FORBIDTMSPAN, ACCOUNTTYPE)
VALUES ('PRE001', 'PRE001', 'ѹ������ǰ���ʺ�(������ʵʹ��)', '200001', '200001', '13800138000', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0, 0, '123456', 0, 0, 2, 0, 0, 0, ' ', ' ', ' ', 0, 0, ' ', ' ', ' ', ' ', 100000, '00:00:00-23:59:59', '00:00:00-00:00:00',1);
COMMIT;

INSERT INTO A_GWACCOUNT(GWNO,PTACCUID, PTACCID, PTACCPWD, SPACCID, SPACCPWD, SPID, SERVICETYPE, FEEUSERTYPE, SPIP, SPPORT, SPEEDLIMIT, PROTOCOLCODE, PTPORT, PTIP, PTACCNAME, PROTOCOLPARAM)
SELECT 100,  "UID" ,'END000', '123456','END000','123456', 'END000','SMS', 0, '127.0.0.1',7891, 2000, 5, 9980,'127.0.0.1', 'ѹ������ͨ���ʺ�(������ʵʹ��)', 'EXPIREHOUR=72;OUTDBGINFO=3;RETURNMOUDHI=0' FROM USERDATA WHERE USERID='END000' AND ACCOUNTTYPE=1 ;
COMMIT;


INSERT INTO GW_USERPROPERTY(USERID,ECID) SELECT USERID,100001 FROM USERDATA WHERE USERTYPE=0 AND NOT EXISTS (SELECT USERID FROM GW_USERPROPERTY  WHERE USERID=USERDATA.USERID);
INSERT INTO GW_GATECONNINFO(PTACCID,IP,PORT,KEEPCONN,TESTMETHOD)
SELECT PTACCID,SPIP,SPPORT,0,2 FROM A_GWACCOUNT A
WHERE NOT EXISTS (SELECT PTACCID,IP,PORT  FROM GW_GATECONNINFO WHERE A.SPIP=IP AND A.PTACCID=PTACCID AND PORT=A.SPPORT);
COMMIT;


-- ----------------------------
-- Records of a_gwparamconf
-- ----------------------------
INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1CHKPOINT', '��ˮ��CheckPointƵ�� ', 1, '�೤ʱ�����һ��checkpoint(��λ:Сʱ)', '2', '1-24', 0, '��ˮ̖CheckPoint�l��', '���L�r�g�M��һ��checkpoint(��λ:С�r)', 'Serial number CheckPoint frequency', 'How long does it take to have a checkpoint (unit: hour), 2 hours by default, range of value: 1-24');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1DATAFILELPATH', '���������ļ�Ŀ¼', 1, '��ʱ�����ļ�����ı���·��(��д����·��)��Ĭ��Ϊ�գ�ȡ����ǰ·��', ' ', ' ', 0, '�����Y�ϙn��Ŀ�', '���r�Y�ϙn��ݔ���ı���·��(��^��·��)���A�O��գ�ȡ��ʽ��ǰ·��', 'Local data file directory', 'The local path of timing data file output (absolute path), the default is empty, take the program current path.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1DATAFILERPATH', '���������ļ�Ŀ¼', 1, '��Ⱥ���������ļ�����·��(��д����·��)��Ĭ��Ϊ�գ��������ļ�����', ' ', ' ', 0, '�����Y�ϙn��Ŀ�', '��Ⱥ�l���Y�ϙn������·��(��^��·��)���A�O��գ����M�Йn����', 'Shared data file directory', 'Cluster gateway data file sharing path (absolute path), the default is empty, not for file sharing.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1MSGIDFILESIZE', '��ˮ���ļ���С ', 1, '������ˮ�Ų�����־�ļ���С(��λ:M)', '20', '1-100', 0, '��ˮ̖�ļ���С', '�΂���ˮ̖�������I�n��С(��λ:M)', 'Size of serial number file', 'A Single serial number operation log file size (unit: Megabyte), default 20M, range of value: 1-100');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, '6CLU1MSGIDFILET', '��ˮ���ļ�Ƶ�� ', 1, 'ÿ�����������һ����־�ļ�(��λ:��)', '5', '3-3600', 0, '��ˮ̖�n�l��', 'ÿ��������a��һ�����I�n(��λ:��)', 'Frequency of serial number file', 'Log file generation cycle (unit: seconds), default: 5 seconds, value range: 3-3600');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ASYNWINSIZEMON', '�������ڴ�С(1-256)', 2, '�������ڴ�С(�첽�ύMO)(1-256)', '128', '1-256', 0, '���Ӵ��ڴ�С(1-256)', '����ҕ����С(��ͬ���ύMO)(1-256)', 'Sliding-window size(1-256)', 'Sliding Window size (Asynchronous commit MO)(1-256)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ASYNWINSIZEMTN', 'MT�������ڴ�С(1-256)', 1, '�������ڴ�С(�첽�ύMT)(1-256)', '128', '1-256', 0, 'MT���Ӵ��ڴ�С(1-256)', '����ҕ����С(��ͬ���ύMT)(1-256)', 'MT sliding-window size(1-256)', 'Sliding Window size (Asynchronous commit MT)(1-256)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'BEEPORNOT', '������Ƿ񱨾�', 1, 'ƽ̨������Ƿ񱨾� 0-�� 1-��', '0', '0,1', 1, '���e���Ƿ��', 'ƽ�_���e���Ƿ�� 0-�� 1-��', 'Error alarm', 'Alarm or not if platform error occurred. 0-NO 1-YES');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE01USEORDB', '����д�ļ�ģʽ', 1, '�ļ�ģʽ������д���ļ������ٶ�ȡ�����ݿ��(1:�ļ���ʽ; 0:ֱ�����, Ĭ��Ϊ1)', '1', '0,1', 1, '���Ì��nģʽ', '�nģʽ�����Ȍ���n�������xȡ���Y�ώ��(1:�n��ʽ; 0:ֱ�����, Ĭ�J��1)', 'Enable file whiting mode', 'File mode first write the file, and then read into the database table (1: file mode; 0: write the database directly, the default is 1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE02DISKFREE', 'д�����ļ�����С���̿ռ��С', 1, 'д�����ļ�����С���̿ռ��С,��λG(5~200��Ĭ��Ϊ10G)', '10', '5~200', 0, '���Y�ϙn������С�ŵ����g��С', '���Y�ϙn������С�ŵ����g��С,��λG(5~200��Ĭ�J��10G)', 'Minimum disk space size of the data writing file', 'Write data file minimum disk space size, unit G (5 ~ 200, default is 10G)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE03MSGFILESIZE', '���������ļ���С', 1, '���������ļ���С,��λM(20~100��Ĭ��Ϊ20M)', '20', '20~100', 0, '�΂��Y�ϙn����С', '�΂��Y�ϙn����С,��λM(20~100��Ĭ�J��20M)', 'Size of a single data file', 'Size of single data file, M as a unit (20 to 100, default is 20M )');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE04MSGFILETM', '�����ļ�����Ƶ��', 1, '�����ļ����ɵ�Ƶ��,��λ��(5~300��Ĭ��Ϊ20��)', '20', '5~300', 0, '�Y�ϙn�������l��', '�Y�ϙn�����ɵ��l��,��λ��(5~300��Ĭ�J��20��)', 'Generation frequency of data file', 'Data file generated frequency, the unit seconds (5 to 300, the default is 20 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE05COMPRESSDIRTM', 'ѹ��������ǰ�������ļ�', 1, 'ѹ��������ǰ�������ļ�(1~30��Ĭ��Ϊ3��)', '3', '1~30', 0, '���s������ǰ���Y�ϙn��', '���s������ǰ���Y�ϙn��(1~30��Ĭ�J��3��)', 'Compress data file of how many days ago', 'How many days data files to compressed  (1 ~ 30, the default is 3 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE06DELETEFILETM', 'ɾ��������ǰ������ѹ���ļ�', 1, 'ɾ��������ǰ������ѹ���ļ�(30~120��Ĭ��Ϊ90��)', '90', '30~120', 0, '�h��������ǰ���Y�ω��s�n', '�h��������ǰ���Y�ω��s�n(30~120��Ĭ�J��90��)', 'Delete the data compression file of how many days ago', 'The data compress file saved in DB will be deleted when the saving time exceed the setting days. (30 �C 120, default is 90 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE07SHAREDIR', '���������ļ��Ĺ���Ŀ¼', 1, '���������ļ��Ĺ���Ŀ¼', ' ', ' ', 0, '�����Y�ϙn���Ĺ���Ŀ�', '�����Y�ϙn���Ĺ���Ŀ�', 'Shared directory of data saving files', 'Shared directory of data saving file');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'FILE08STOPINSERTDB', '��ͣ�����ļ����', 1, '��ͣ�����ļ����(1:��ͣ�ļ����; 0:�����ļ����, Ĭ��Ϊ0)���ԣ��߼�����', '0', '0,1', 1, '��ͣ�Y�ϙn�����', '��ͣ�Y�ϙn�����(1:��ͣ�n���; 0:�_���n���, �A�O��0)���ԣ��߼�����', 'Pause data file inserting into DB', 'Pause data file inserting into DB ��1��Pause; 0: Enable. The default value is 0�� Properties: Advanced parameters');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'HOUROUTREPORTN', '״̬������Ч��(Сʱ)', 1, '״̬������Ч��(Сʱ)(12-360)', '72', '12-360', 0, '��B�����Ч��(С�r)', '��B�����Ч��(С�r)(12-360)', 'Validity of RPT(hour)', 'The validity of the status report (12-360 hours)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ISCUTSPGATE', '�Ƿ��ʹ���Ӻ��·�', 1, '0:����ͨ�����·� 1:��ʹ���Ӻ��·�(�ӺŰ���:�󶨱��е�CPNO+�û�����չ)', '0', '0,1', 1, '�Ƿ�Hʹ����̖�°l', '0:����ͨ��̖�°l 1:�Hʹ����̖�°l(��̖����:�������е�CPNO+�Ñ��ԔUչ)', 'Only use the sub-number to send or not', '0:Use the full channel number to send 1: Use the sub-channel number to send');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ISFORCEFWD', '�Ƿ�ǿ����ת(0/1)', 2, '0-��ǿ����ת 1-ǿ����ת', '0', '0,1', 0, '�Ƿ������D(0/1)', '0-���������D 1-�������D', 'Force transfer or not(0/1) ', '0-Unforced transit 1-Force transfer');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'ISPACKLONGSMS', '�Ƿ񳤶���MT���(0/1)', 2, '0-����������� 1-���������', '0', '0,1', 1, '�Ƿ��L����MT�M��(0/1)', '0-���M���L���� 1-�M���L����', 'Is it a long/short message MT package(0/1) ', '0-Partition long SMS 1-Composition long SMS');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'LOGDAYSN', '��־��������', 1, '��־��������(7-90)', '15', '7-90', 0, '���I�����씵', '���I�����씵(7-90)', 'Number of Log saving days', 'Number of days to save the log(7-90)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'LOGLEVELN', '��־����', 1, '��־����(0-5)', '3', '0,1,2,3,4,5', 1, '���I���e', '���I���e(0-5)', 'Log level', 'Log level (0-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MAPSIZE', 'MAP��ˮ�Ŷ��д�С', 1, ' MAP��ˮ�Ŷ��д�С(20000000~80000000��Ĭ��Ϊ50000000)', '50000000', '20000000~80000000', 0, 'MAP��ˮ̖���д�С', 'MAP��ˮ̖���д�С(20000000~80000000��Ĭ�J��50000000)', 'The size of MAP serial number queue', 'MAP serial number queue size (20000000~80000000, default to 50000000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MOBUFSIZEN', 'MO�����С', 1, 'MO�����С(10-10000)', '5000', '10-10000', 0, 'MO���n��С', 'MO���n��С(10-10000)', 'The size of MO buffer', 'MO buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MONITORMQURL', '�����Ϣ���е�ַ', 1, '�����Ϣ���е�ַ(������Ч)', '127.0.0.1:61616', ' ', 0, '�O��ӍϢ���е�ַ', '�O��ӍϢ���е�ַ(�؆���Ч)', 'Address of monitoring message queue ', 'Monitor message queue address (valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MONITORQUENAME', '�����Ϣ������', 2, '�����Ϣ������(������Ч)', 'MEPMONITORQUEUE', ' ', 0, '�O��ӍϢ������', '�O��ӍϢ������(�؆���Ч)', 'Name of monitoring message queue', 'Monitor the message queue name(valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MORESENDCOUNTN', '����MOʧ���ط�����', 1, 'MOʧ���ط�����(1-5)', '3', '1-5', 0, '����MOʧ���ذl�Δ�', 'MOʧ���ذl�Δ�(1-5)', 'Times of failed MO resend', 'Times of failed MO resend (1-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTDBBUFSIZEN', 'MTд�⻺��', 1, 'ȡֵ��Χ10#50000', '5000', '10-50000', 0, 'MT���쾏�n', 'ȡֵ����10#50000', 'MT DB inserting buffer', 'Value range 10 - 50000');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTPACKSMAXNUMN', '����MT�����������', 0, 'SGIP/SMGPЭ�鲻�ܴ��,������޲�����100', '1', '1-100', 0, '����MT����l�͔���', 'SGIP/SMGP�f�h���ܴ��,������޲����^100', 'Amount of MT package transmission', 'SGIP / SMGP protocol can not be packaged, the package limit does not exceed 100');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTRESENDCOUNTN', '����MTʧ���ط�����', 0, 'MTʧ���ط�����(1-5)', '3', '1-5', 0, '����MTʧ���ذl�Δ�', 'MTʧ���ذl�Δ�(1-5)', 'Times of MT resend', 'MT failure retry times(1-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTSENDLEVELN', '����MT�ύ����(0-9)', 2, 'MT�ύ����(0-9)', '1', '0,1,2,3,4,5,6,7,8,9', 1, '����MT�ύ���e(0-9)', 'MT�ύ���e(0-9)', 'MT submission level(0-9)', 'MT submission level (0-9)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTSENDTHREADNUMN', 'MT�ύ�߳�����(1-15)', 1, 'MT�ύ�߳�����(1-15)', '1', '1-15', 0, 'MT�ύ���оw����(1-15)', 'MT�ύ���оw����(1-15)', 'MT submission thread number(1-15)', 'MT submission thread Number (1-15)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORINSERTNUMN', 'RPT����д������', 1, 'REPORTд��������(1-100)', '100', '1-100', 0, 'RPT��������l��', 'REPORT����������(1-100)', 'Number of PRT batch DB inserting', 'REPORT Write the maximum number of packets in the database (1-100)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORT1BUFSIZEN', 'REPORTһ�������С(10-10000)', 1, 'REPORTһ�������С(10-10000)', '5000', '10-10000', 0, 'REPORTһ�����n��С(10-10000)', 'REPORTһ�����n��С(10-10000)', 'Size of REPORT level1 buffer(10-10000)', 'REPORT level1 buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORT2BUFSIZEN', 'REPORT���������С(10-10000)', 1, 'REPORT���������С(10-10000)', '5000', '10-10000', 0, 'REPORT�������n��С(10-10000)', 'REPORT�������n��С(10-10000)', 'Size of REPORT level2 buffer(10-10000)', 'REPORT level2 buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORT3BUFSIZEN', 'REPORT���������С(10-10000)', 1, 'REPORT���������С(10-10000)', '5000', '10-10000', 0, 'REPORT�������n��С(10-10000)', 'REPORT�������n��С(10-10000)', 'Size of REPORT level3 buffer(10-10000)', 'REPORT level3 buffer size (10-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORTPREFIXS', 'RPT������ǰ׺', 2, 'RPT������ǰ׺', 'E2', 'E2', 0, 'RPT�e�`�a�״a', 'RPT�e�`�a�״a', 'Prefix of RPT error code', 'RPT error code prefix');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORTRESENDCOUNTN', '״̬����RPTʧ���ط�����', 1, 'RPTʧ���ط�����(1-5)', '3', '1-5', 0, '��B���RPTʧ���ذl�Δ�', 'RPTʧ���ذl�Δ�(1-5)', 'Times of failed RPT resend', 'Times of failed RPT resend (1-5)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'REPORTUPDATENUMN', 'RPT������������', 1, 'REPORT�����������(1-100)', '100', '1-100', 0, 'RPT�������l��', 'REPORT�����������(1-100)', 'Number of RPT batch updating', 'Maximum packages amount of REPORT library updating (1-100)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SENDERRORRPT', '�Ƿ����ʹ���rpt������', 1, '�Ƿ����ʹ���rpt������ 0:�����ͣ�1:����', '1', '0~1', 0, '�Ƿ������e�`rpt�o�l��', '�Ƿ������e�`rpt�o�l�� 0:�����ͣ�1:����', 'Push error rpt to gateway or not', 'Whether push error rpt to the gateway 0: no push, 1: push');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SPEEDCTRLWAYN', 'MT�ύ�����ٶȷ�ʽ', 1, '3-SPGATE���� 4-�ύ�ӿڿ���', '4', '3,4', 1, 'MT�ύ�����ٶȷ�ʽ', '3-SPGATE���� 4-�ύ�������', 'Method of MT submission speed controlling', '3-SPGATE speed control 4-Submit interface speed control');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SPEEDSIZEN', '����MT�����ٶ�', 0, 'MT�����ٶ�(1-20000)', '100', '1-20000', 0, '����MT�l���ٶ�', 'MT�l���ٶ�(1-20000)', 'MT sending speed', 'MT transmission speed (1-20000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'STOPMONITOR', '��ͣ��� ', 1, '�Ƿ���ͣ���س�����: 1 ��ͣ 0���', '0', '0,1', 1, '��ͣ�O��', '�Ƿ�ͣ�l����ʽ�O��: 1 ��ͣ 0�O��', 'Pause Monitoring', 'Whether to stop the gateway program monitoring: 1 pause  0 monitor');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'SYSMONFREQ', 'ϵͳ���Ƶ��(S)', 1, 'ϵͳ���Ƶ��(S)', '5', '3-120', 0, 'ϵ�y�O���l��(S)', 'ϵ�y�O���l��(S)', 'System monitoring frequency(S)', 'System monitoring frequency (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, '6CLU1DATAFILELPATH', '���������ļ�Ŀ¼', 1, '��ʱ�����ļ�����ı���·��(��д����·��)��Ĭ��Ϊ�գ�ȡ����ǰ·��', ' ', ' ', 0, '�����Y�ϙn��Ŀ�', '���r�Y�ϙn��ݔ���ı���·��(��^��·��)���A�O��գ�ȡ��ʽ��ǰ·��', 'Local data file directory', 'The local path of timing data file output (absolute path), the default is empty, take the program current path.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, '6CLU1DATAFILERPATH', '���������ļ�Ŀ¼', 1, '��Ⱥ���������ļ�����·��(��д����·��)��Ĭ��Ϊ�գ��������ļ�����', ' ', ' ', 0, '�����Y�ϙn��Ŀ�', '��Ⱥ�l���Y�ϙn������·��(��^��·��)���A�O��գ����M�Йn����', 'Shared data file directory', 'Cluster gateway data file sharing path (absolute path), the default is empty, not for file sharing.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ADJUSTENDWND', '����Զ���������(0/1)', 2, '�Ƿ����ú�˻��������Ե����� 0-�� 1-��', '1', '0,1', 1, '����Ԅ��{��ҕ��(0/1)', '�Ƿ�����˻���ҕ�����{���� 0-�� 1-��', 'Back-end auto size window(0/1)', 'Enable the back-end sliding window self-tuning function 0-Disable 1-Enable ');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ADJUSTPREWND', 'ǰ���Զ���������', 2, '�Ƿ�����ǰ�˻��������Ե�����0-�� 1-��', '1', '0,1', 1, 'ǰ���Ԅ��{��ҕ��', '�Ƿ���ǰ�˻���ҕ�����{����0-�� 1-��', 'Front-end auto size window', 'Enable the front-end sliding window self-tuning function 0-Disable 1-Enable ');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'APPTITLE', '�������', 1, 'MWSMS��������ʱ�ı���', 'MWSMS��������', ' ', 0, '��ʽ���}', 'MWSMS��ʽ�\�Еr�Ę��}', 'Program title', 'The title of the MWSMS program runtime');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'BATCHREAD', '������ȡ����', 1, '������ȡ����', '50', '1-1000', 0, '�����xȡ�l��', '�����xȡ�l��', 'Number of batch reading', 'Batch read number');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'BEEPORNOT', '������Ƿ񱨾�', 1, 'ƽ̨������Ƿ񱨾� 0-�� 1-��', '0', '0,1', 1, '���e���Ƿ��', 'ƽ�_���e���Ƿ�� 0-�� 1-��', 'Error alarm', 'Alarm or not if platform error occurred. 0-NO 1-YES');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'CHECKNULLLINKFREQ', '�����Ӽ��(S)', 1, 'ƽ̨��ⳬʱ���ӵ�Ƶ��(S)', '30', '5-60', 0, '���B�әz�y(S)', 'ƽ�_�z�y���r�B�ӵ��l��(S)', 'Null connection detection (S)', 'The platform detects the frequency of the overtime connection (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'CMPPPORT', 'CMPP�����˿�', 0, '�ƶ���Ӫ��CMPP�����˿�', '7890', ' ', 0, 'CMPP�O ��', '�Ƅ��\�I��CMPP�O ��', 'CMPP listening port', 'Mobile operator CMPP listening port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'DBCMDDELAY', 'DB��ʱʱ��(S)', 1, '������������ִ�г�ʱʱ�䣨S��', '60', '5-60', 0, 'DB���r�r�g(S)', '�B���Y��������г��r�r�g��S��', 'Database timeout (S)', 'Connection data command execution time-out (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'DISCARDNULLMO', '����������', 1, '�Ƿ��������б�־', '1', '0,1', 1, '�G��������', '�Ƿ�G�������И��I', 'Discard empty Mo', 'Discard empty MO sign or not');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'DISCARDRPT', '�Ƿ���RPT(0/1)', 1, '�Ƿ���RPT 0-�� 1-��', '0', '0,1', 0, '�Ƿ�G��RPT(0/1)', '�Ƿ�G��RPT 0-�� 1-��', 'Discard status report (0/1)', 'Whether to discard RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ENABLELOG', '��¼������־', 1, '�Ƿ��¼����������־(0/1)', '1', '0,1', 1, 'ӛ��\�����I', '�Ƿ�ӛ��l���\�����I(0/1)', 'Record running log', 'Whether to record the gateway run log (0/1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ENDSLIDEWND', '��˴��ڳ�ֵ(1-16)', 2, '����û��������ڴ�С��ֵ(1-16)', '5', '1-16', 0, '��˴��ڳ�ֵ(1-16)', '���ʹ���߻���ҕ����С��ֵ(1-16)', 'Initial value of back-end window', 'Backend User Sliding Window Size Initial Value (1-16)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ERCODEPREFIX', 'RPT������ǰ׺', 2, 'RPT������ǰ׺(��ĸ�����ֵ���ϣ����Ȳ�����3)', 'E1', ' ', 0, 'RPT�e�`�a�״a', 'RPT�e�`�a�״a(��ĸ�͔�λ�ĽM�ϣ��L�Ȳ����^3)', 'Prefix of RPT error code', 'RPT error code prefix (combination of letters and numbers, no more than 3)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ERMTTODB', '�쳣MT�Ƿ�����־', 1, '�쳣MT�Ƿ�����־ 0-�� 1-��', '1', '0,1', 1, '����MT�Ƿ������I', '����MT�Ƿ������I 0-�� 1-��', 'Insert MT exception sign into DB or not', 'Exception MT is written to the database flag 0 - No 1 - Yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE01USEORDB', '����д�ļ�ģʽ', 1, '�ļ�ģʽ������д���ļ������ٶ�ȡ�����ݿ��(1:�ļ���ʽ; 0:ֱ�����, Ĭ��Ϊ1)', '1', '0,1', 1, '���Ì��nģʽ', '�nģʽ�����Ȍ���n�������xȡ���Y�ώ��(1:�n��ʽ; 0:ֱ�����, Ĭ�J��1)', 'Enable file whiting mode', 'File mode first write the file, and then read into the database table (1: file mode; 0: write the database directly, the default is 1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE02DISKFREE', 'д�����ļ�����С���̿ռ��С', 1, 'д�����ļ�����С���̿ռ��С,��λG(5~200��Ĭ��Ϊ10G)', '10', '5~200', 0, '���Y�ϙn������С�ŵ����g��С', '���Y�ϙn������С�ŵ����g��С,��λG(5~200��Ĭ�J��10G)', 'Minimum disk space size of the data writing file', 'Write data file minimum disk space size, unit G (5 ~ 200, default is 10G)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE03MSGFILESIZE', '���������ļ���С', 1, '���������ļ���С,��λM(20~100��Ĭ��Ϊ20M)', '20', '20~100', 0, '�΂��Y�ϙn����С', '�΂��Y�ϙn����С,��λM(20~100��Ĭ�J��20M)', 'Size of a single data file', 'Size of single data file, M as a unit (20 to 100, default is 20M )');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE04MSGFILETM', '�����ļ�����Ƶ��', 1, '�����ļ����ɵ�Ƶ��,��λ��(5~300��Ĭ��Ϊ20��)', '20', '5~300', 0, '�Y�ϙn�������l��', '�Y�ϙn�����ɵ��l��,��λ��(5~300��Ĭ�J��20��)', 'Generation frequency of data file', 'Data file generated frequency, the unit seconds (5 to 300, the default is 20 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE05COMPRESSDIRTM', 'ѹ��������ǰ�������ļ�', 1, 'ѹ��������ǰ�������ļ�(1~30��Ĭ��Ϊ3��)', '3', '1~30', 0, '���s������ǰ���Y�ϙn��', '���s������ǰ���Y�ϙn��(1~30��Ĭ�J��3��)', 'Compress data file of how many days ago', 'How many days data files to compressed  (1 ~ 30, the default is 3 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE06DELETEFILETM', 'ɾ��������ǰ������ѹ���ļ�', 1, 'ɾ��������ǰ������ѹ���ļ�(30~120��Ĭ��Ϊ90��)', '90', '30~120', 0, '�h��������ǰ���Y�ω��s�n', '�h��������ǰ���Y�ω��s�n(30~120��Ĭ�J��90��)', 'Delete the data compression file of how many days ago', 'The data compress file saved in DB will be deleted when the saving time exceed the setting days. (30 �C 120, default is 90 days)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE07SHAREDIR', '���������ļ��Ĺ���Ŀ¼', 1, '���������ļ��Ĺ���Ŀ¼', ' ', ' ', 0, '�����Y�ϙn���Ĺ���Ŀ�', '�����Y�ϙn���Ĺ���Ŀ�', 'Shared directory of data saving files', 'Shared directory of data saving file');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILE08STOPINSERTDB', '��ͣ�����ļ����', 1, '��ͣ�����ļ����(1:��ͣ�ļ����; 0:�����ļ����, Ĭ��Ϊ0)���ԣ��߼�����', '0', '0,1', 1, '��ͣ�Y�ϙn�����', '��ͣ�Y�ϙn�����(1:��ͣ�n���; 0:�_���n���, �A�O��0)���ԣ��߼�����', 'Pause data file inserting into DB', 'Pause data file inserting into DB ��1��Pause; 0: Enable. The default value is 0�� Properties: Advanced parameters');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILTERB01TDMO', '���������˶�����', 1, '�Ƿ������лظ��˶����ֻ����Զ���Ӻ��������� (0�����á�1����)', '0', '0,1', 1, '�_��������ӆ����', '�Ƿ��_�����л؏���ӆ���֙C̖�Ԅ���Ӻ����ι��� (0�����á�1����)', 'Enable MO unsubscribe function', 'Whether to open MO reply unsubscribing. Phone number will be automatically added to blacklist. (0 Not Disable, 1 enable)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILTERBLACKPHONE', '�Ƿ���˺�����(0/1)', 1, '0-������ 1-����', '1', '0,1', 0, '�Ƿ��^�V������(0/1)', '0-���^�V 1-�^�V', 'Filter blacklist or not(0/1)', '0 - not filter 1 - filter');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'FILTERKEYWORDS', '���˹ؼ���', 1, '�Ƿ����ؼ��ֹ��� 0-�� 1-��', '0', '0,1', 1, '�^�V�P�I��', '�Ƿ��_���P�I���^�V 0-�� 1-��', 'Filter keyword', 'Whether to enable keyword filtering 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INNERPORT', '�ڲ��˿�', 0, '�����ڲ������˿�', '9980', ' ', 0, '�Ȳ���', '�l���Ȳ��O ��', 'Internal port', 'Interior Gateway listening port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB0TRANSMODE', '����ת��ģʽ', 1, '0-ʵʱת��(ʵʱת��)��1-����ת��(һ��ת��һ��)', '0', '0,1', 1, '�Y���D��ģʽ', '0-���r�D��(���r�D��)��1-���c�D��(һ���D��һ��)', 'Data transfer mode', '0 - real-time transfer (real-time transfer), 1-The whole point transfer (once a day)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB1INTERVAL', 'ʵʱת��ʱ����', 1, 'ʵʱ���ȱ�ת�����ݵ�ʵʱ���ʱ����(��λ:�룬Ĭ��180��)', '180', '10-7200', 0, '���r�D�ƕr�g�g��', '���r�ğ���D���Y�ϵ����r��ĕr�g�g��(��λ:�룬Ĭ�J180��)', 'Real time transfer interval', 'Real-time transfer from the hot table data to the real-time table of the time interval (unit: seconds, the default 300 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB2PRETIME', '�ȱ�����ʱ��', 1, '�ȱ��б���೤ʱ�������(��λ:����)', '5', '1-1440', 0, '��픵���r�L', '����б�����L�r�g���Y��(��λ:���)', 'Hot table data duration', 'How long is the data stored in the hot table (in minutes)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB3INCRESLOAD', '������ת��������', 1, '�������ȱ�ת�Ƶ����ݱ�����ת����������', '500000', '1000-1000000', 0, '�������D���Y����', '�����ğ���D�Ƶ���ݱ������D���Y������', 'Batch transferred amount of data', 'Batch from the hot table to the backup table,Batch transfer data volume.');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB4INCRES', '�����θ���������', 1, '�������ݵķ���״̬�������£����θ���������', '500000', '1000-1000000', 0, '�����θ����Y����', '�����Y�ϵİl�͠�B�������£����θ�������', 'Amount of batch updating data', 'Batch updates transmission status of MT data, the number of batch updating data');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB5TRANSTIME', '����ת��ʱ��', 1, '��������ת��ģʽ����һ����������ת��ʱ��(����: 00:30:00)', '00:30:00', '00:00:00-23:59:59', 0, '���c�D�ƕr�g', '���c�����D��ģʽ����һ�����c�����D�ƕr�g(����: 00:30:00)', 'Integral point transfer time', 'The entire data transfer mode, the next point of data transfer time (for example: 00:30:00)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB6STATSTRANS', '���Ȼ���ʱ��', 1, '���ػ������ݱ�����ʷ���ݵ���ʱ��(����: 03:00:00)', '03:00:00', '00:00:00-23:59:59', 0, '�{�ȅR���r�g', '�l���R���Y�ϰ����vʷ�Y���{�ȕr�g(����: 03:00:00)', 'Scheduling summary time', 'Gateway summary data report and historical data scheduling time (for example: 03:00:00)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'INTB7AUTOCTRL', '��ͣʵʱת�Ʒ�ֵ', 1, 'ʵʱת��ģʽ�£�ƽ̨����������������ֵ�󣬳����Զ���ͣʵʱת��(ȡֵ��Χ��1000-20000000��0��ʾ������)', '0', '1000-20000000', 0, '��ͣ���r�D���yֵ', '���r�D��ģʽ�£�ƽ�_���Ŝ��������^�yֵ�ᣬ��ʽ�Ԅӕ�ͣ���r�D��(ȡֵ������1000-20000000��0��ʾ������)', 'Threshold of real time transfer pause', 'Real-time transfer mode, the platform message retention exceeds the threshold, the program automatically suspends the real-time transfer (range: 1000-20000000,0 is not enabled)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGINID', 'WBS���ʶ���½�ʺ�', 2, 'WBS���ʶ���½�ʺ�', 'WBS00A', ' ', 0, 'WBS����R�e��ꑎ�̖', 'WBS����R�e��ꑎ�̖', 'WBS ID login account', 'WBS identification login account');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGINPWD', 'WBS���ʶ���½�ʺ�����', 2, 'WBS���ʶ���½�ʺ�����', '123456', ' ', 0, 'WBS����R�e��ꑎ�̖�ܴa', 'WBS����R�e��ꑎ�̖�ܴa', 'WBS ID login password', 'WBS identification login password');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGLEVEL', '��־����(0-5)', 1, '0:������־ 1:ԭʼ�����(��������) 2:��ǰ�˽����������(������) 3:���˽����������(������) 4:ǰ�����˵������(������) 5:��ϸ��', '2', '0,1,2,3,4,5', 1, '���I���e(0-5)', '0:�e�`���I 1:ԭʼ�W·��(��������) 2:�cǰ�˽����ľW·��(������) 3:�c��˽����ľW·��(������) 4:ǰ��ɶ˵ľW·��(������) 5:Ԕ����', 'Log level(0-5)', '0: error log 1: original network packet (without command) 2: network packet with the front end (including command) 3: network packet with the back-end interaction (including command) 4: front and rear network packets (including command) 5: detailed package');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGPATH', '��־·��', 2, '�洢��־�ļ���·��', ' ', ' ', 0, '���I·��', '�惦���I�n��·��', 'Log path', 'The path to the log file');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGSAVEDAYS', '��־��������', 1, 'ƽ̨��¼����־�������������������Զ�ɾ��', '30', '7-90', 0, '���I�����씵', 'ƽ�_ӛ䛵����I�����씵�����^�����Ԅӄh��', 'Number of Log saving days', 'Platform records the number of log days to keep,Exceeded date will be automatically deleted');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGSIZE', '������־�ļ���С(M)', 1, '������־�ļ��Ĵ�С�����������������½��ļ�', '30', '10-100', 0, '�΂����I�n��С(M)', '�΂����I�n�Ĵ�С�����^���������K�½��n', 'Size of single log file(M)', 'The size of a single log file, more than will be renamed and new files');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOGTHRMODE', '�Ƿ�����־�߳�ģʽ', 2, '�Ƿ�����־�߳�ģʽ 0-�� 1-��', '0', '0,1', 1, '�Ƿ��_�����I���оwģʽ', '�Ƿ��_�����I���оwģʽ 0-�� 1-��', 'Enable log thread mode or not', 'Whether to open log thread mode 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXDBBUFBUM', 'д�⻺������', 1, '���ݿ⻺���С(1000-10000)', '5000', '1000-10000', 0, '���쾏�n����', '�Y�ώ쾏�n��С(1000-10000)', 'Upper limit of inserting DB buffer', 'Database buffer size (1000-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXENDSLIDEWND', '��˴�������(16-150)', 2, '����û��������ڴ�С���ֵ(16-150)', '128', '16-150', 0, '��˴�������(16-150)', '���ʹ���߻���ҕ����С���ֵ(16-150)', 'Upper limit of back-end window(16-150)', 'Back-end user sliding window size maximum (16-150)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXIOCPTHREAD', '��ɶ˿�(IOCP)����߳���', 2, '��ɶ˿�(IOCP)����������߳���', '1', '-1-256', 0, '��ɲ�(IOCP)�����оw��', '��ɲ�(IOCP)�_���������оw��', 'IOCP greatest number of thread', 'The maximum number of threads that have opened the port (IOCP)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXPRESLIDEWND', 'ǰ�˴�������', 2, 'ǰ���û��������ڴ�С���ֵ', '128', '16-150', 0, 'ǰ�˴�������', 'ǰ��ʹ���߻���ҕ����С���ֵ', 'Upper limit of front-end window', 'Front-end user sliding window size maximum');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXRESENDCTRLNUM', '����ط����ƶ�������', 2, '�ط�����ʱ�䷶Χ�����й��˵�����������', '1000000', '0-1000000', 0, '����ذl���ƶ��ŗl��', '�ذl���ƕr�g�������M���^�V�������ŗl��', 'Maximum number of SMS for resending control', 'The time range of the retransmission control, The maximum number of messages to filter');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXREWRDBNUM', 'ʧ����д����', 1, 'д��ʧ��ʱ�ظ�д�����', '3', '0-30', 0, 'ʧ���،��Δ�', '����ʧ���r���}����Δ�', 'Failed rewrite times', 'The number of retries after write database failed');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXSYSCON', '�û���������', 1, 'ƽ̨���е�����û�������', '1500', '5-32768', 0, '�Ñ��B������', 'ƽ�_�\�е�����Ñ��B�Ӕ�', 'Upper limit of user connection', 'The maximum number of user connections when the platform is running');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MAXUSERCON', '�˺����ӿ���', 1, 'ÿ���ʺ����õ����������', '5', '1-50', 0, '��̖�����攵', 'ÿ����̖�O�õ�����B�Ӕ�', 'Max amount of account port', 'The maximum amount of port for each account');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MEMEXCEPTIONVALUE', '�ڴ��쳣�ָ��߽�ֵ(M)', 2, '�ڴ��쳣�ָ��߽�ֵ(M)', '1600', '1200-1700', 0, 'ӛ���w�����֏�߅��ֵ(M)', 'ӛ���w�����֏�߅��ֵ(M)', 'Boundary value of memory exception Recovery (M)', 'Memory Exception Recovery Boundary Value (M)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MEMWARNVALUE', '�ڴ汨��ֵ(M)', 2, '�ڴ汨��ֵ(M)', '1400', '1000-1500', 0, 'ӛ���w��ֵ(M)', 'ӛ���w��ֵ(M)', 'Memory alarm value (M)', 'Memory alarm value (M)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MOCMDROUTE', '�Ƿ���������ָ��·��(0/1)', 1, '�Ƿ���������ָ��·�� 1Ϊ������0Ϊ������', '1', '0,1', 1, '�Ƿ񆢄�����ָ��·��(0/1)', '�Ƿ񆢄�����ָ��·�� 1�醢�ӣ�0�鲻����', 'Enable MO instruction routing or not (0/1)', 'Enable MO instruction routing or not.  1: Enable, 0: Disable');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MON1REMAINMTWARN', '�������澯��ֵ', 1, '������������������ֵ������ƽ̨�ϱ��澯��ȡֵ��Χ0-50000000��Ĭ��ֵ 5000000��0�򲻽��и澯', '5000000', '0-50000000', 0, '�������澯�yֵ', '���l�����������^�yֵ�ᣬ��O��ƽ�_�ψ�澯��ȡֵ����0-50000000���A�Oֵ 5000000��0�t���M�и澯', 'Alarm threshold for delay capacity', 'When the gateway retention exceeds the threshold, the alarm is reported to the monitoring platform, the value range is 0-50000000, the default value is 5000000, 0 is not alarm');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MONITORMQURL', '�����Ϣ���е�ַ', 1, '�����Ϣ���е�ַ(������Ч)', '127.0.0.1:61616', ' ', 0, '�O��ӍϢ���е�ַ', '�O��ӍϢ���е�ַ(�؆���Ч)', 'Address of monitoring message queue ', 'Monitor message queue address (valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MONITORQUENAME', '�����Ϣ������', 2, '�����Ϣ������(������Ч)', 'MEPMONITORQUEUE', ' ', 0, '�O��ӍϢ������', '�O��ӍϢ������(�؆���Ч)', 'Name of monitoring message queue', 'Monitor the message queue name(valid after reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORESENDCNT', 'MO��ʱ�ط�����', 2, 'MO�ط�����', '5', '0-100', 0, 'MO���r�ذl�Δ�', 'MO�ذl�Δ�', 'Times of MO timeout resend', 'MO number of retransmissions');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORPTDELAYSD', 'MO/RPT�ӳ����ͼ��(��)', 2, 'MO/RPT�ӳ����ͼ��(��)', '0', '0-60', 0, 'MO/RPT���t�����g��(��)', 'MO/RPT���t�����g��(��)', 'MO/RPT delayed push interval (min)', 'MO / RPT delayed push interval (minutes)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORPTHOLDON', 'WBS״̬��������д����ڴ��е�ʱ��(S)', 1, 'WBS״̬��������д����ڴ��е�ʱ�� ������ֵ����д�����ݿ�', '30', '1-1800', 0, 'WBS��B�������д���ӛ���w�еĕr�g(S)', 'WBS��B�������д���ӛ���w�еĕr�g ���^ԓֵ���������Y�ώ�', 'WBS status report and the duration MO stayed in memory (S)', 'WBS status reports and the amount of time that the uplink remains in memory,Exceeding this value will be written to the database');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MORPTPUSHWAY', '����mo/rpt�ķ�ʽ', 1, '����mo/rpt�ķ�ʽ(0:IOCPCLIENT���ͣ�1:ʹ��libcurl����)', '1', '0,1', 1, '����mo/rpt�ķ�ʽ', '����mo/rpt�ķ�ʽ(0:IOCPCLIENT���ͣ�1:ʹ��libcurl����)', 'Push mo/rpt mode', 'Push mo/rpt mode (0:IOCPCLIENT push, 1: use libcurl push)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'MSGVALIDTIME', '����Ĭ����Чʱ��(H)', 1, '����Ĭ����Чʱ��(1-24*7Сʱ������������Ч)', '24', '1-168', 0, '����Ĭ�J��Ч�r�L(H)', '����Ĭ�J��Ч�r�L(1-24*7С�r���l���؆���Ч)', 'SMS default valid duration (H)', 'SMS default valid length (1-24 * 7 hours, valid after the gateway reboot)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'NULLLINKTM', '�����ӳ�ʱ(S)', 1, '�������ӳ�ʱʱ��(S)', '120', '10-1200', 0, '���B����r(S)', '�����B����r�r�g(S)', 'Null connection timeout(S)', 'Maximum empty connection timeout (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PACKINITNUM', '���д�����', 1, '���д�����(���峬����ֵ�ſ�ʼ���)', '2', '1-10000', 0, '����������c', '����������c(���n���^ԓֵ���_ʼ���)', 'Starting point of database Packing Insert', 'Starting point of database Packing Insert (begin to pack when buffer exceed this value)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PACKUPDATENUM', '������������', 1, '�������ݿ�������(1-200)', '100', '1-200', 0, '�������l��', '�����Y�ώ����l��(1-200)', 'Number of batch updating', 'Update database package number (1-200)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PACKWRITENUM', '����д������', 1, 'д����ʱ�������������(1-200)', '100', '1-200', 0, '��������l��', '���Y�ϕr����ėl������(1-200)', 'Number of batch database inserting', 'The limit amount of batch data writing (1-200)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PRESLIDEWND', 'ǰ�˴��ڳ�ֵ(1-16)', 2, 'ǰ���û��������ڴ�С��ֵ(1-16)', '5', '1-16', 0, 'ǰ�˴��ڳ�ֵ(1-16)', 'ǰ��ʹ���߻���ҕ����С��ֵ(1-16)', 'Initial value of front-end window(1-16)', 'Front User Sliding Window Size Initial Value (1-16)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PROTOCOLVERSION', 'CMPP֧�ֵ����Э��汾', 2, 'CMPP֧�ֵ����Э��汾', '7', '5,7', 1, 'CMPP֧Ԯ����߅f���汾', 'CMPP֧Ԯ����߅f���汾', 'The highest protocol version supported by CMPP', 'The latest protocol version supported by CMPP');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PTADDR', '����ƽ̨��IP��ַ', 2, '����ƽ̨��IP��ַ', '127.0.0.1', ' ', 0, '�B��ƽ�_��IPλַ', '�B��ƽ�_��IPλַ', 'The IP address of the connection platform', 'The IP address of the connected platform');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY01SPDCTRL', '������ѯ�ӿڿ���', 1, '��Ƶ�����û�ȡ���С�״̬����ӿڽ��п���(0�����á�1����)', '1', '0,1', 1, '�_����ԃ�������', '���l���{�ë@ȡ���С���B�������M�п���(0�����á�1����)', 'Enable speed control of query interface', 'Speed control on frequently calling MO or RPT interface (0 disable, 1 enable)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY02FREQ', '������ò�ѯ�ӿ�Ƶ��', 1, '�������ݻ�ȡʱ�������������ò�ѯ�ӿڵ�Ƶ��(1-120/��/�Σ�Ĭ��3��)', '3', '1-120', 0, '���S�{�ò�ԃ�����l��', '�ڟo�����@ȡ�r�����S�B�m�{�ò�ԃ������l��(1-120/��/�Σ�Ĭ�J3��)', 'Frequency limit of calling query interface', 'In the absence of data acquisition, the frequency limit of continuous calling query interface (1-120 sec / times, default 3 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY03DELAY', '��ѯ��ʱ��Ӧʱ��', 1, '��Ƶ�����ò�ѯ�ӿ�ʱ�����ضԲ�ѯ��Ӧ��ʱ��Ӧ(1-120/��)��Ĭ��5��', '5', '1-120', 0, '��ԃ�ӕr�ؑ��r�L', '���l���{�ò�ԃ����r���l������ԃ�ؑ��ӕr�ؑ�(1-120/�룬Ĭ�J5��)', 'Query delay response duration', 'When system frequently calls the query interface, the response delay of the gateway (1-120 / sec, default 5 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'QUERY04TIMES', 'Ƶ�����ô���', 2, '����Ƶ�����ô�����������ֵ������ʱ��Ӧ (1-100�Σ�Ĭ��10��)', '10', '1-100', 0, '�l���{�ôΔ�', '�B�m�l���{�ôΔ����^�O��ֵ���_���ӕr�ؑ� (1-100�Σ�Ĭ�J10��)', 'Frequently calling times', 'The number of frequent calls more than the set value after the opening delay response (1-100 times, the default 10 times)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESENDCNT', '�ط�����', 2, '�ط���������Ϊ0�������ط�����', '3', '0-100', 0, '�ذl�Δ�', '�ذl�Δ�������0�������ذl����', 'Resend times', 'Number of retransmissions, 0 is not enabled retransmission');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESENDCTRL', '�����ط�����', 1, '�Ƿ�����ͬ���������ظ��ύ����(0/1)', '1', '0,1', 1, '�����ذl����', '�Ƿ��_����ͬ���Ń������}�ύ����(0/1)', 'Enable resend control', 'Whether to open the control of same content message repeat sending (0/1)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESNDCHECKFREQ', '�ط����Ƽ��', 1, '�ظ�Ƶ�ʼ������(��)', '3600', '0-86400', 0, '�ذl�����g��', '���}�l���g������(��)', 'Resend control interval', 'Repeat frequency interval control (seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RESNDRPTERCODE', '�ط�·��RPT�������б�', 2, '���ط���״̬����������б�(�Զ���(,)����)', ' ', ' ', 0, '�ذl·��RPT�e�`�a���', '���ذl�Ġ�B����e�`�a���(�Զ�̖(,)���_)', 'Error code list of resend routing RPT', 'Status to be retransmitted Report error list (separated by comma (,))');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'RSPDELAY', '�ӳٻ�Ӧʱ��(S)', 1, '������ʱ�ӳٻ�Ӧʱ��(S)', '3', '1-15', 0, '���t�ؑ��r�g(S)', '���n���r���t�ؑ��r�g(S)', 'Delay response time(S)', 'Duration of buffer delay response(S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SENDBUFBUM', '���ͻ�������', 1, '���ݿ⻺���С(1000-10000)', '5000', '1000-10000', 0, '�l�;��n����', '�Y�ώ쾏�n��С(1000-10000)', 'Upper limit of transmission buffer', 'Database buffer size (1000-10000)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SGIPPORT', 'SGIP�����˿�', 2, '��ͨ��Ӫ��SGIP�����˿�', '8801', ' ', 0, 'SGIP�O ��', 'ͨ�\�I��SGIP�O ��', 'SGIP listening port', 'Unicom operator SGIP listening port');

  INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SMARTVERIFY', '�Ƿ����������(0/1)', 2, '�Ƿ����������(0/1)', '0', '0,1', 1, '�Ƿ��_���ǻی���(0/1)', '�Ƿ��_���ǻی���(0/1)', 'Turn On the Intelligent audit or not (0/1)', 'Whether to enable intelligent auditing (0/1)');


INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SMGPPORT', 'SMGP�����˿�', 2, '������Ӫ��SMGP�����˿�', '8890', ' ', 0, 'SMGP�O ��', '����\�I��SMGP�O ��', 'SMGP listening port', 'Telecom operator SMGP listening port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SMPPPORT', 'SMPP�����˿�', 2, '�ƶ���Ӫ��SMPP�����˿�', '8891', ' ', 0, 'SMPP�O ��', '�Ƅ��\�I��SMPP�O ��', 'SMPP protocol port', 'International Protocol SMPP Protocol Port');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SPLITLONGMSG', '�����Ƿ��ֳ�������', 1, '�����Ƿ�Գ�������(����360����)���в�ֺ��͡�1-��֣�0-����֣��������͸�SPGATE����SPGATE���ơ�', '1', '0,1', 1, '�l���Ƿ��ֳ��L����', '�l���Ƿ񌦳��L����(���^360����)�M�в����l�͡�1-��֣�0-����֣����l�l�ͽoSPGATE����SPGATE���ơ�', 'Split Long Messages by Gateway or not', 'Whether the gateway to split the long message(more than 360 words) before sending.  1 �C split; 0 - no split, send entire long message to SPGATE, controlled by SPGATE');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STARTSVRPROTOCOL', '��Ӫ��Э������', 2, '��Ӫ��Э�飬CMPP-�ƶ�,SGIP-��ͨ,SMGP-����,SMPP-����Э��', '1-0-0-0', 'CMPP,SMGP,SGIP,SMPP', 2, '�\�I�̅f������', '�\�I�̅f�h��CMPP-�Ƅ�,SGIP-ͨ,SMGP-���,SMPP-���H�f�h', 'Mobile operator protocol configuration', 'Carrier Protocol, CMPP-Mobile, SGIP-Unicom, SMGP-Telecom, SMPP-International Agreement');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPCHARGE', '�Ƿ���ͣ�Ʒ�(0/1)', 2, '�Ƿ���ͣ�Ʒ� 0-�� 1-��', '0', '0,1', 1, '�Ƿ�ͣӋ�M(0/1)', '�Ƿ�ͣӋ�M 0-�� 1-��', 'Pause Billing or not (0/1)', 'Whether to suspend billing 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMONITOR', '��ͣ��� ', 1, '�Ƿ���ͣ���س�����: 1 ��ͣ 0���', '0', '0,1', 1, '��ͣ�O��', '�Ƿ�ͣ�l����ʽ�O��: 1 ��ͣ 0�O��', 'Pause Monitoring', 'Whether to stop the gateway program monitoring: 1 pause  0 monitor');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMOREAD', '��ͣ����MO��ȡ', 1, '�Ƿ���ͣMO��ȡ 0-�� 1-��', '0', '0,1', 1, '��ͣ����MO�xȡ', '�Ƿ�ͣMO�xȡ 0-�� 1-��', 'Pause MO reading', 'Whether to suspend MO read 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMOSEND', '��ͣ����MO����', 0, '�Ƿ���ͣ����MO 0-�� 1-��', '0', '0,1', 1, '��ͣ����MO�l��', '�Ƿ�ͣ�l��MO 0-�� 1-��', 'Pause MO sending', 'Whether to suspend sending MO 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTLVLRD', '��������ͣ����MT��ȡ', 1, '��������ͣMT��ȡ,�����0-9,��λȡֵ0:��ͣ1:��ͣ', ' ', '0,1,2,3,4,5,6,7,8,9', 2, '�����e��ͣ����MT�xȡ', '�����e��ͣMT�xȡ,���e��0-9,��λȡֵ0:��ͣ1:��ͣ', 'Pause MT reading according to the level', 'Pause MT reading according to the level, level 0-9, Bitwise 0: None 1: Pause');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTLVLSD', '��������ͣ����MT����', 1, '��������ͣMT����,�����0-9', ' ', '0,1,2,3,4,5,6,7,8,9', 2, '�����e��ͣ����MT�l��', '�����e��ͣMT�l��,���e��0-9', 'Pause MT sending according to the level', 'Pause MT sending according to the level, level from 0-9');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTSEND', '��ͣ����MT����', 0, '�Ƿ���ͣ����MT 0-�� 1-��', '0', '0,1', 1, '��ͣ����MT�l��', '�Ƿ�ͣ�l��MT 0-�� 1-��', 'Pause MT sending', 'Whether to suspend sending MT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPMTTMRLVLRD', '��������ͣ����MT��ʱ���ȡ', 1, '��������ͣMT��ʱ���ͱ��ȡ,�����0-9', ' ', '0,1,2,3,4,5,6,7,8,9', 2, '�����e��ͣ����MT���r���xȡ', '�����e��ͣMT���r�l�ͱ��xȡ,���e��0-9', 'Pause the MT timer according to the LeveL', 'Pause the MT timer by level to read, level from 0-9');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPRECV', '�Ƿ���ͣ����MT/MO/RPT(0/1)', 2, '�Ƿ���ͣ����MT/MO/RPT 0-�� 1-��', '0', '0,1', 1, '�Ƿ�ͣ����MT/MO/RPT(0/1)', '�Ƿ�ͣ����MT/MO/RPT 0-�� 1-��', 'Stop receiving MT/MO/RPT or not(0/1)', 'Whether to suspend reception of MT / MO / RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPRPTREAD', '��ͣ״̬����RPT��ȡ', 1, '�Ƿ���ͣRPT��ȡ 0-�� 1-��', '0', '0,1', 1, '��ͣ��B���RPT�xȡ', '�Ƿ�ͣRPT�xȡ 0-�� 1-��', 'Pause RPT reading', 'Whether to pause RPT read 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'STOPRPTSEND', '��ͣ״̬����RPT����', 0, '�Ƿ���ͣ����״̬����(RPT) 0-�� 1-��', '0', '0,1', 1, '��ͣ��B���RPT�l��', '�Ƿ�ͣ�l�͠�B���(RPT) 0-�� 1-��', 'Pause RPT sending', 'Yes No Pause Send Status Report (RPT) 0 - No 1 - Yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SUSPENDRPT', '��ת��״̬����RPT', 1, '�Ƿ�ת��RPT 0-�� 1-��', '0', '0,1', 1, '���D�l��B���RPT', '�Ƿ��D�lRPT 0-�� 1-��', 'Not to forward RPT', 'Whether or not to forward RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SYNCBINDINFOINTERVAL', 'ͬ���󶨱���(M)', 1, 'ͬ���˿ڰ󶨱�ʱ����(M)', '1', '1-60', 0, 'ͬ���������g��(M)', 'ͬ����������r�g�g��(M)', 'Sync binding time interval(M)', 'Synchronize Port Binding Table Interval (M)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SYNCFEEINTERVAL', 'ͬ�����ʱ���(S)', 1, '�۷�ʱ����(S)', '3', '2-300', 0, 'ͬ���M�ʱ��g��(S)', '���M�r�g�g��(S)', 'Sync schedule of rates time interval(S)', 'Charge time interval (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'SYSMONFREQ', 'ϵͳ���Ƶ��(S)', 1, 'ϵͳ���Ƶ��(S)', '5', '3-120', 0, 'ϵ�y�O���l��(S)', 'ϵ�y�O���l��(S)', 'System monitoring frequency(S)', 'System monitoring frequency (S)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'THREADCTRL', '�����߳�������', 2, '���س��������߳�������(������Ч)', '2-2-2-2-1-5-1', ' ', 0, '�l�����оw������', '�l����ʽ���ӈ��оw������(�؆���Ч)', 'Gateway thread control', 'Gateway program thread number control (restart effective)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'ULTRAFLOWDELAY', '��������ʱ(S)', 1, '��������ʱֵ', '1', '1-60', 0, '�������ӕr(S)', '�������ӕrֵ', 'Delay of over flow(S)', 'Value of over flow delay');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'VERIFYTMBEGIN', '���ʱ�����(24Сʱ��HH:MM:SS)', 2, '���ʱ�����(24Сʱ��HH:MM:SS)', '00:00:00', '24Сʱ��HH:MM:SS', 0, '���˕r�����c(24С�r��HH:MM:SS)', '���˕r�����c(24С�r��HH:MM:SS)', 'Starting point of Audit period(HH:MM:SS)', 'The starting point of the audit period(24 hours HH: MM: SS)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'VERIFYTMEND', '���ʱ���յ�(24Сʱ��HH:MM:SS)', 2, '���ʱ���յ�(24Сʱ��HH:MM:SS)', '00:00:00', '24Сʱ��HH:MM:SS', 0, '���˕r�νK�c(24С�r��HH:MM:SS)', '���˕r�νK�c(24С�r��HH:MM:SS)', 'Ending point of Audit period(HH:MM:SS)', 'The end of the audit period(24 hours HH: MM: SS)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WAITRSPDELAY', '��Ӧ��ʱʱ��', 1, '�ȴ��೤ʱ��δ�յ���Ӧ����Ϊ��ʱ', '90', '40-120', 0, '�ؑ����r�r�g', '�ȴ����L�r�gδ�յ��ؑ���ҕ�鳬�r', 'Response timeout', 'How long does it take to receive the response packet as timeout');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSDISCARDRPT', 'WBS�Ƿ���RPT', 1, 'WBS�Ƿ���RPT 0-�� 1-��', '0', '0,1', 1, 'WBS�Ƿ�G��RPT', 'WBS�Ƿ�G��RPT 0-�� 1-��', 'WBS discard RPT or not', 'Whether WBS to discard RPT 0 - no 1 - yes');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSPACKNUM', 'WBS��ͬ���ݶ��Ŵ���ֻ��Ÿ���', 1, 'WBS��ͬ���ݶ��Ŵ���ֻ��Ÿ���', '50', '1-100', 0, 'WBS��ͬ���ݶ��Ŵ���֙C̖����', 'WBS��ͬ���ݶ��Ŵ���֙C̖����', 'Amount of phone number within WBS same content SMS package', 'Amount of phone number within WBS same content SMS package');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSPORT', '����WBS�����˿�', 0, 'WBS������TCP����˿�', '8082', ' ', 0, '�l��WBS�O ��', 'WBS�O ��TCP�W·��', 'Gateway WBS listening port', 'WBS listens for TCP network ports');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSRESNDCNT', 'WBS��������ʧ���ط�����', 1, 'WBS��������ʧ���ط�����', '3', '1-32', 0, 'WBS��������ʧ���ذl�Δ�', 'WBS��������ʧ���ذl�Δ�', 'Times of WBS active push resend', 'Times of WBS failed active push resend');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSSLIDEWND', 'WBS�������ͻ�������', 1, 'WBS�������ͻ�������', '3', '1-100', 0, 'WBS�������ͻ��Ӵ���', 'WBS�������ͻ��Ӵ���', 'WBS active push sliding-window', 'WBS active push sliding-window');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSSNDBUF', 'WBS���ͻ�������', 1, 'WBS���ͻ�������', '5000', '100-10000', 0, 'WBS�l�;��n����', 'WBS�l�;��n����', 'Upper limit of WBS sending buffer', 'WBS sends buffer limit');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WBSSNDSPD', 'WBS�����ٶ�', 1, 'WBS�����ٶ�', '1500', '10-10000', 0, 'WBS�l���ٶ�', 'WBS�l���ٶ�', 'WBS transmission speed', 'WBS sending speed');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'WEBPATH', 'WEB·��', 2, 'WEB·��', '/SMS/MT', ' ', 0, 'WEB·��', 'WEB·��', 'WEB path', 'WEB path');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (3000, 'MTBUFSIZEN', 'MT���ջ���', 1, 'MT���ջ��壬ȡֵ��Χ��10-50000��', '50000', '10-50000', 0, 'MT���վ��n', 'MT���վ��n��ȡֵ������10-50000��', 'MT receive buffer', 'MT receive buffer Value range 10 - 50000');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO, DEFAULTVALUE, VALUERANGE, CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'LOADTMPLTIMEINTERVAL', '����ģ��ʱ����', 1, '��ģ����м���ģ�嵽�ڴ��е�ʱ����(��λ:�룬Ĭ��30��)', '30', '10-3600', 0, '���dģ��r�g�g��', '��ģ����м��dģ�嵽�ȴ��еĕr�g�g��(��λ:�룬Ĭ�J30��)', 'Load template interval','The time interval for loading a template from the template table into memory (unit: second, default 30 seconds)');

INSERT INTO a_gwparamconf (GWTYPE, PARAMITEM, PARAMNAME, PARAMATTRIBUTE, PARAMMEMO,DEFAULTVALUE, VALUERANGE,CONTROLTYPE, HKPARAMNAME, HKPARAMMEMO, ENPARAMNAME, ENPARAMMEMO) VALUES (4000, 'PUSHCONFIG', '�����ֶ��Ƿ�ΪJSON����', 1, '�����ֶ��Ƿ�ΪJSON����(0:�ַ���,1:JSON����)','0','0,1',1,'�����ֶ��Ƿ��JSON���M','�����ֶ��Ƿ��JSON���M(0:�ַ���,1:JSON���M)','Whether the push field is a JSON array','Whether the push field is a JSON array (0: string, 1: JSON array)');

COMMIT;
/

----Э��ģ��
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


----------------��Ⱥ���ʼ��
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

---ͨ��
INSERT INTO XT_GATE_QUEUE( SPGATE, STATUS, SPISUNCM, GATETYPE, GATENAME, PORTTYPE, SINGLELEN, SIGNSTR, RISELEVEL, SPEED, LONGSMS, MAXWORDS, SUBLEN, FEEFLAG, SIGNLEN, FEE, MULTILEN1, MULTILEN2, SIGNTYPE, SIGNFIXLEN, MAXLONGMSGSEQ, SIGNDROPTYPE, GATESEQ, ENDSPLIT, AREATYPE, GATEAREA, SPLITRULE, SORTID, SHOWFLAG)
VALUES( '106579999', 0, 0, 1, '�ƶ�ѹ������ͨ��(������ʵʹ��)', 0,                70,        '[�ƶ�����]',0,     1000,    1,      1000,      0,      2,        6,       0.00,67,        57,        0,        10,         128,           1,            0,       0,        0,        'ȫ��YL',   1,         1,      1);

INSERT INTO XT_GATE_QUEUE( SPGATE, STATUS, SPISUNCM, GATETYPE, GATENAME, PORTTYPE, SINGLELEN, SIGNSTR, RISELEVEL, SPEED, LONGSMS, MAXWORDS, SUBLEN, FEEFLAG, SIGNLEN, FEE, MULTILEN1, MULTILEN2, SIGNTYPE, SIGNFIXLEN, MAXLONGMSGSEQ, SIGNDROPTYPE, GATESEQ, ENDSPLIT, AREATYPE, GATEAREA, SPLITRULE, SORTID, SHOWFLAG)
VALUES( '106559999', 0, 1, 1, '��ͨѹ������ͨ��(������ʵʹ��)', 1,                70,        '[��ͨ����]',0,     1000,    1,      1000,      0,      2,        6,       0.00,67,        57,        0,        10,         128,           1,            0,       0,        0,        'ȫ��YL',   1,         1,      1);

INSERT INTO XT_GATE_QUEUE( SPGATE, STATUS, SPISUNCM, GATETYPE, GATENAME, PORTTYPE, SINGLELEN, SIGNSTR, RISELEVEL, SPEED, LONGSMS, MAXWORDS, SUBLEN, FEEFLAG, SIGNLEN, FEE, MULTILEN1, MULTILEN2, SIGNTYPE, SIGNFIXLEN, MAXLONGMSGSEQ, SIGNDROPTYPE, GATESEQ, ENDSPLIT, AREATYPE, GATEAREA, SPLITRULE, SORTID, SHOWFLAG)
VALUES( '106599999', 0, 21, 1, '����ѹ������ͨ��(������ʵʹ��)', 21,                70,        '[���Ų���]',0,     1000,    1,      1000,      0,      2,        6,       0.00,67,        57,        0,        10,         128,           1,            0,       0,        0,        'ȫ��YL',   1,         1,      1);

SELECT * FROM XT_GATE_QUEUE;

---�˿ڰ󶨱�
INSERT INTO GT_PORT_USED( GATETYPE, SPGATE, CPNO, PORTTYPE, SPISUNCM, ROUTEFLAG, STATUS, USERID, LOGINID,SPNUMBER, FEEFLAG, SIGNSTR, SIGNLEN, MAXWORDS, SINGLELEN, MULTILEN1, MULTILEN2, SENDTMSPAN, FORBIDTMSPAN, GATESEQ, SENDTIMEBEGIN, SENDTIMEEND,MOBIAREA)
VALUES(1, '106579999', '1001', 0,                0,        0,         0,      'PRE001','PRE001','1065799991001',2, '[PRE001]',8,   1000,      70,        67,        57,        '00:00:00-23:59:59','00:00:00-00:00:00', 0, '00:00:00','23:59:59','ȫ��');

INSERT INTO GT_PORT_USED( GATETYPE, SPGATE, CPNO, PORTTYPE, SPISUNCM, ROUTEFLAG, STATUS, USERID, LOGINID,SPNUMBER, FEEFLAG, SIGNSTR, SIGNLEN, MAXWORDS, SINGLELEN, MULTILEN1, MULTILEN2, SENDTMSPAN, FORBIDTMSPAN, GATESEQ, SENDTIMEBEGIN, SENDTIMEEND,MOBIAREA)
VALUES(1, '106559999', '1001', 0,               1,        0,         0,      'PRE001','PRE001','1065599991001',2, '[PRE001]',8,   1000,      70,        67,        57,        '00:00:00-23:59:59','00:00:00-00:00:00', 0, '00:00:00','23:59:59','ȫ��');

INSERT INTO GT_PORT_USED( GATETYPE, SPGATE, CPNO, PORTTYPE, SPISUNCM, ROUTEFLAG, STATUS, USERID, LOGINID,SPNUMBER, FEEFLAG, SIGNSTR, SIGNLEN, MAXWORDS, SINGLELEN, MULTILEN1, MULTILEN2, SENDTMSPAN, FORBIDTMSPAN, GATESEQ, SENDTIMEBEGIN, SENDTIMEEND,MOBIAREA)
VALUES(1, '106599999', '1001', 0,                21,        0,         0,      'PRE001','PRE001','1065999991001',2, '[PRE001]',8,   1000,      70,        67,        57,        '00:00:00-23:59:59','00:00:00-00:00:00', 0, '00:00:00','23:59:59','ȫ��');

INSERT INTO A_GWPARAMVALUE (GWNO, GWTYPE, PARAMITEM, PARAMVALUE)
(SELECT 99, 4000, PARAMITEM, DEFAULTVALUE FROM A_GWPARAMCONF WHERE GWTYPE=4000);

INSERT INTO A_GWPARAMVALUE (GWNO, GWTYPE, PARAMITEM, PARAMVALUE)
(SELECT 100, 3000, PARAMITEM, DEFAULTVALUE FROM A_GWPARAMCONF WHERE GWTYPE=3000);


---Э��������
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('DELIVRD','EMPHTTP','000','�����͵��ֻ�');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('EXPIRED','EMPHTTP','500','���Ź���');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('DELETED','EMPHTTP','500','���ű�ɾ��');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('UNDELIV','EMPHTTP','500','�޷�Ͷ��');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('ACCEPTD','EMPHTTP','500','�����û�����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('UNKNOWN','EMPHTTP','500','״̬��֪');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('REJECTD','EMPHTTP','500','���ű��ܾ�');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0101','EMPHTTP','100','ȱ�ٲ�������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0102','EMPHTTP','100','��Ч��������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0103','EMPHTTP','100','ȱ��SP��ID');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0104','EMPHTTP','100','��ЧSP��ID');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0105','EMPHTTP','100','ȱ��SP����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0106','EMPHTTP','100','��ЧSP����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0107','EMPHTTP','100','����Դ��ַ����ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0108','EMPHTTP','100','��Ч����Դ��ַ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0109','EMPHTTP','100','ȱ������Ŀ�ĵ�ַ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0110','EMPHTTP','100','��Ч����Ŀ�ĵ�ַ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0111','EMPHTTP','100','��������Ŀ�ĵ�ַ����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0112','EMPHTTP','100','ESM_CLASS����ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0113','EMPHTTP','100','��ЧESM_CLASS');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0114','EMPHTTP','100','PROTOCOL_ID����ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0115','EMPHTTP','100','��ЧPROTOCOL_ID');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0116','EMPHTTP','100','ȱ����Ϣ�����ʽ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0117','EMPHTTP','100','��Ч��Ϣ�����ʽ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0118','EMPHTTP','100','ȱ����Ϣ����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0119','EMPHTTP','100','��Ч��Ϣ����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0120','EMPHTTP','100','��Ч��Ϣ���ݳ���');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0121','EMPHTTP','100','���ȼ�����ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0122','EMPHTTP','100','��Ч���ȼ�');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0123','EMPHTTP','100','��ʱ����ʱ�䱻��ֹ��ʱ���ʽ���󡢶�ʱʱ��С�ڵ�ǰʱ�䡢��ʱʱ����ڵ��ڴ��ʱ�䣩');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0124','EMPHTTP','100','��Ч��ʱ����ʱ�䣨ʱ���ʽ���󡢶�ʱʱ��С�ڵ�ǰʱ�䡢��ʱʱ����ڵ��ڴ��ʱ�䣩');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0125','EMPHTTP','100','������Ч���ʱ�䱻��ֹ��ʱ���ʽ���󡢴��ʱ��С�ڵ�ǰʱ�䡢��ʱʱ����ڵ��ڴ��ʱ�䣩');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0126','EMPHTTP','100','������Ч���ʱ����Ч��ʱ���ʽ���󡢴��ʱ��С�ڵ�ǰʱ�䡢��ʱʱ����ڵ��ڴ��ʱ�䣩');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0127','EMPHTTP','100','ͨ��ID����ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0128','EMPHTTP','100','��Чͨ��ID');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0131','EMPHTTP','100','ȱ��������������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0132','EMPHTTP','100','��Ч������������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0133','EMPHTTP','100','��Ч����ID');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0134','EMPHTTP','100','��Ч�������б���');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0135','EMPHTTP','100','ȱ��������������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0136','EMPHTTP','100','��Ч������������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0137','EMPHTTP','100','ȱ��������������URL');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0138','EMPHTTP','100','��Ч������������URL');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0139','EMPHTTP','100','SP������벻����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0140','EMPHTTP','100','��Ч�Ĳ�ͬ�����������е�ַ������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0141','EMPHTTP','100','��������URL�е��ļ��Ѿ�����������������ظ�������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0142','EMPHTTP','100','��Ч���У���루����Ϊ8��Ϊ��ĸ�����֡���ĸ���ֻ�ϣ�');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0201','EMPHTTP','200','MSISDN����β�����');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0202','EMPHTTP','200','MSISDN�����ͣ��');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0210','EMPHTTP','200','MSISDN���뱻����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0220','EMPHTTP','200','���ݱ�����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0221','EMPHTTP','200','���ݱ��˹�����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0230','EMPHTTP','200','����·��ʧ��');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0240','EMPHTTP','200','����·��ʧ��');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0250','EMPHTTP','200','����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0251','EMPHTTP','200','û�����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0252','EMPHTTP','200','ҵ�����ʹ����ȳ�����Ϊ��ĸ�����֡���ĸ���ֻ�ϣ�');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0253','EMPHTTP','200','�Զ�����������ȳ�����');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0254','EMPHTTP','200','EMPϵͳ��֤ʧ�ܣ���ֹͣ���͡���������Ϊ��Ʒ���кŹ��ڡ���ʱ���޷�������֤����������ɣ�');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0301','EMPHTTP','300','SP����ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0302','EMPHTTP','300','SP������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0303','EMPHTTP','300','�Ƿ�IP��ַ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0304','EMPHTTP','300','���������ٶ�����');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0305','EMPHTTP','300','����������������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0306','EMPHTTP','300','SMS���б���ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0307','EMPHTTP','300','SMS�������б���ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0308','EMPHTTP','300','SMS���б���ֹ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0309','EMPHTTP','300','SMS״̬���汻��ֹ');

INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0401','EMPHTTP','400','Դ������ͨ���Ų�ƥ��');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0402','EMPHTTP','400','�·��������������쳣');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0403','EMPHTTP','400','�·��������������޷���');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0500','EMPHTTP','500','���������ط�����Ϣ');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('ACCEPTD','EMPHTTP','000','API�ӿ���Ϣ������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('REJECTD','EMPHTTP','600','API�ӿ���Ϣ���ܾ�');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0601','EMPHTTP','600','API�ӿ�HTTPEXCEPTION');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0602','EMPHTTP','600','��������æ����������æʱϵͳ���������������Ҫ��������');
INSERT INTO HTTPERRCODE(ERRSTATUS,PROTOCOLTYPE,ERRCODE,DESCRPTINFO) VALUES('MW:0603','EMPHTTP','600','API�ӿ�DBEXCEPTION');
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

-----���ӳ�������----------
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 10, 10);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���', '���', 22, 22);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '����', 310, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', 'ʯ��ׯ', 311, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '����', 312, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '�żҿ�', 313, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '�е�', 314, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '��ɽ', 315, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '�ȷ�', 316, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '����', 317, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '��ˮ', 318, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '��̨', 319, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ӱ�', '�ػʵ�', 335, 311);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '˷��', 349, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 350, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '̫ԭ', 351, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '��ͬ', 352, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '��Ȫ', 353, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 354, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 355, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 356, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '�ٷ�', 357, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 358, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '�˳�', 359, 351);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '���ױ���', 470, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '���ͺ���', 471, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '��ͷ', 472, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '�ں�', 473, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '�����첼', 474, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', 'ͨ��', 475, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '���', 476, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '������˹', 477, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '�����׶�', 478, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '���ֹ�����', 479, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '�˰���', 482, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('���ɹ�', '��������', 483, 471);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 24, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 24, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 411, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɽ', 412, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��˳', 24, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��Ϫ', 414, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 415, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 416, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'Ӫ��', 417, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 418, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 419, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 421, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�̽�', 427, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��«��', 429, 24);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 431, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 432, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�ӱ߳�����������', 433, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ƽ', 434, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ͨ��', 435, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�׳�', 436, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��Դ', 437, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ԭ', 438, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɽ', 439, 431);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '������', 451, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '�������', 452, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', 'ĵ����', 453, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '��ľ˹', 454, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '�绯', 455, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '�ں�', 456, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '���˰������', 457, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '����', 458, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '����', 459, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '��̨��', 464, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '����', 467, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '�׸�', 468, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('������', '˫Ѽɽ', 469, 451);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ϻ�', '�Ϻ�', 21, 21);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�Ͼ�', 25, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 510, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��', 511, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 512, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ͨ', 513, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 514, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�γ�', 515, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 516, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 517, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���Ƹ�', 518, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 519, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '̩��', 523, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��Ǩ', 527, 25);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '����', 570, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '����', 571, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '����', 572, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '����', 573, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '����', 574, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '����', 575, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '̨��', 576, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '����', 577, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '��ˮ', 578, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '��', 579, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㽭', '��ɽ', 580, 571);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 550, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�Ϸ�', 551, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 552, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�ߺ�', 553, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 554, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɽ', 555, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 556, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 557, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 558, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 558, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɽ', 559, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 561, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ͭ��', 562, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 563, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 564, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 565, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 566, 551);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 591, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 592, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 593, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 594, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'Ȫ��', 595, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 596, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 597, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 598, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ƽ', 599, 591);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ӥ̶', 701, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 790, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�ϲ�', 791, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�Ž�', 792, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 793, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 794, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�˴�', 795, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 796, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 797, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '������', 798, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'Ƽ��', 799, 791);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 530, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 531, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '�ൺ', 532, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '�Ͳ�', 533, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 534, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '��̨', 535, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', 'Ϋ��', 536, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 537, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '̩��', 538, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 539, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 543, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '��Ӫ', 546, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 631, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '��ׯ', 632, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 633, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '����', 634, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('ɽ��', '�ĳ�', 635, 531);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 370, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '֣��', 371, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 372, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 373, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���', 374, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ƽ��ɽ', 375, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 376, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 377, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 378, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 379, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��Դ', 391, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 391, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�ױ�', 392, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���', 393, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�ܿ�', 394, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���', 395, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'פ���', 396, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����Ͽ', 398, 371);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�人', 27, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�差', 710, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 711, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'Т��', 712, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�Ƹ�', 713, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ʯ', 714, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 715, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 716, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�˲�', 717, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ʩ����������������', 718, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ʮ��', 719, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 722, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 724, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '������', 728, 27);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 730, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɳ', 731, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��̶', 731, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 731, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 734, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 735, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 736, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 737, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '¦��', 738, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 739, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��������������������', 743, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�żҽ�', 744, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 745, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 746, 731);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 20, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '��β', 660, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 662, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 663, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', 'ï��', 668, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 750, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '�ع�', 751, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 752, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '÷��', 753, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '��ͷ', 754, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 755, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '�麣', 756, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '��ɽ', 757, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 758, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', 'տ��', 759, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '��ɽ', 760, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '��Դ', 762, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '��Զ', 763, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '�Ƹ�', 766, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '����', 768, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�㶫', '��ݸ', 769, 20);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���Ǹ�', 770, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 771, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 771, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 772, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 772, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 773, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 774, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 774, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���', 775, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 775, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɫ', 776, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 777, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�ӳ�', 778, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 779, 771);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 890, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 898, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 898, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɳ', 898, 898);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 23, 23);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '�ɶ�', 28, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '��֦��', 812, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '�Թ�', 813, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '����', 816, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '�ϳ�', 817, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '����', 818, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '����', 825, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '�㰲', 826, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '����', 827, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '����', 830, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '�˱�', 831, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '�ڽ�', 832, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '����', 28, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '��ɽ', 833, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', 'üɽ', 28, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '��ɽ����������', 834, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '�Ű�', 835, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '���β���������', 836, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '���Ӳ���Ǽ��������', 837, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '����', 838, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�Ĵ�', '��Ԫ', 839, 28);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 851, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 852, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 852, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ǭ�ϲ���������������', 854, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ǭ�������嶱��������', 855, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ͭ�ʵ���', 856, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�Ͻڵ���', 857, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����ˮ', 858, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ǭ���ϲ���������������', 859, 851);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��˫���ɴ���������', 691, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�º���徰����������', 692, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ͨ', 870, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 871, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�������������', 872, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ӹ���������������', 873, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 874, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɽ', 875, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ɽ׳������������', 876, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��������������', 878, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '˼é', 879, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�ٲ�', 883, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ŭ��������������', 886, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�������������', 887, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 888, 871);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 891, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�տ������', 892, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ɽ�ϵ���', 893, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��֥����', 894, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��������', 895, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��������', 896, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�������', 897, 891);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 29, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 29, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '�Ӱ�', 911, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 912, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'μ��', 913, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 914, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 915, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 916, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 917, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ͭ��', 919, 29);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���Ļ���������', 930, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 931, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 932, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ƽ��', 933, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 934, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���', 935, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 935, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��Ҵ', 936, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '������', 937, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��Ȫ', 937, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ˮ', 938, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '¤��', 939, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '���ϲ���������', 941, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 943, 931);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '��������������', 970, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '����', 971, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '��������', 972, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '���ϲ���������', 973, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '���ϲ���������', 974, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '�������������', 975, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '��������������', 976, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '�����ɹ������������', 979, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', 'ã��', 9840, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '���', 9848, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�ຣ', '���', 9849, 971);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 951, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', 'ʯ��ɽ', 952, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 953, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '��ԭ', 954, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('����', '����', 955, 951);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '���ǵ���', 901, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '���ܵ���', 902, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '�������', 903, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '����̩����', 906, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '�������տ¶�����������', 908, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '���������ɹ�������', 909, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '��������', 990, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '��³ľ��', 991, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '��������������', 994, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '��³������', 995, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '���������ɹ�������', 996, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '�����յ���', 997, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '��ʲ����', 998, 991);
INSERT INTO  A_PROVINCECITY (PROVINCE, CITY, AREACODE, PROVINCECODE)VALUES ('�½�', '���������������', 999, 991);
COMMIT;
/

--��36X4����ʷ��
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
  WHILE PICURTYPE<=PITYPE LOOP--������
  PICURNUM:=0;
    <<INNER>>
    WHILE PICURNUM<PINUM LOOP--�����
        TACNT:=0;
        PIYM:=CAST(TO_CHAR(ADD_MONTHS(SYSDATE,PICURNUM),'YYYYMM')AS INT);
        --DBMS_OUTPUT.PUT_LINE(PIYM);

        --Ҫ�����ı���ڣ���������������һ��
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
        IF TACNT=0 THEN--������
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
          --DBMS_OUTPUT.PUT_LINE('������ʷ������쳣�������˳�'||SQLERRM);
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

--������
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'0060'   ,60   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ӡ��������'	                  ,'0062'   ,62   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ɱ�'	                      ,'0063'   ,63   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�¼���'	                      ,'0065'   ,65   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('̩��'	                        ,'0066'   ,66   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00673'  ,673  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ձ�'	                        ,'0081'   ,81   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0082'   ,82   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Խ��'	                        ,'0084'   ,84   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00850'  ,850  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���'	                        ,'00852'  ,852  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00853'  ,853  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����կ'	                      ,'00855'  ,855  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00856'  ,856  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�й�'	                        ,'0086'   ,86   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('̨��'	                        ,'00886'  ,886  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ϼ�����'	                    ,'00880'  ,880  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'0090'   ,90   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ӡ��'	                        ,'0091'   ,91   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ͻ�˹̹'	                    ,'0092'   ,92   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'0093'   ,93   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('˹������'	                    ,'0094'   ,94   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���'	                        ,'0095'   ,95   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�������'	                    ,'00960'  ,960  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����'	                      ,'00961'  ,961  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Լ��'	                        ,'00962'  ,962  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00963'  ,963  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00964'  ,964  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00965'  ,965  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ɳ�ذ�����'	                  ,'00966'  ,966  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00968'  ,968  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��ɫ��'	                      ,'00972'  ,972  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00973'  ,973  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00974'  ,974  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00975'  ,975  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ɹ�'	                        ,'00976'  ,976  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�Ჴ��'	                      ,'00977'  ,977  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0098'   ,98   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����·˹'	                    ,'00357'  ,357  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����˹̹'	                    ,'00970'  ,970  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00971'  ,971  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ҳ��'	                        ,'00967'  ,967  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��Ͳ�Τ'	                    ,'00263'  ,263  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ױ���'	                    ,'00264'  ,264  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����ά'	                      ,'00265'  ,265  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00266'  ,266  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'00267'  ,267  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0020'   ,20   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ϣ��'	                        ,'0030'   ,30   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0031'   ,31   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����ʱ'	                      ,'0032'   ,32   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0033'   ,33   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'0034'   ,34   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ֱ������'	                    ,'00350'  ,350  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00351'  ,351  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('¬ɭ��'	                      ,'00352'  ,352  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00353'  ,353  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00354'  ,354  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����������'	                  ,'00355'  ,355  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����'	                      ,'00356'  ,356  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00376'  ,376  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00358'  ,358  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'00359'  ,359  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'0036'   ,36   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�¹�'	                        ,'0049'   ,49   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��˹����'	                    ,'00381'  ,381  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����'	                      ,'0039'   ,39   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥ����ŵ'	                    ,'00378'  ,378  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��ٸ�'	                      ,'00379'  ,379  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'0040'   ,40   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��ʿ'	                        ,'0041'   ,41   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��֧��ʿ��'	                  ,'00423'  ,423  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�µ���'	                      ,'0043'   ,43   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ӣ��'	                        ,'0044'   ,44   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0045'   ,45   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���'	                        ,'0046'   ,46   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ų��'	                        ,'0047'   ,47   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0048'   ,48   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ݿ�'	                        ,'00420'  ,420  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('˹�工��'                      ,'00421'  ,421  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ħ�ɸ�'	                      ,'00377'  ,377  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����'	                      ,'00389'  ,389  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���޵���'                      ,'00385'  ,385  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('˹��������'	                  ,'00386'  ,386  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00253'  ,253  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00254'  ,254  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('̹ɣ����'	                    ,'00255'  ,255  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ڸɴ�'	                      ,'00256'  ,256  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��¡��'	                      ,'00257'  ,257  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Īɣ�ȿ�'	                    ,'00258'  ,258  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ޱ���'	                      ,'00260'  ,260  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����˹��'                    ,'00261'  ,261  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'00262'  ,262  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00370'  ,370  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����ά��'	                    ,'00371'  ,371  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��ɳ����'	                    ,'00372'  ,372  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ħ������'	                    ,'00373'  ,373  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����ݽ�'	                    ,'00994'  ,994  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('˹��ʿ��'	                    ,'00268'  ,268  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ڿ���'	                      ,'00380'  ,380  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�Ϸ�'	                        ,'0027'   ,27   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥ������'	                    ,'00290'  ,290  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��³�͵�'	                    ,'00297'  ,297  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����Ⱥ��'	                    ,'00298'  ,298  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����������'	                  ,'00291'  ,291  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ϸ������'	                  ,'00246'  ,246  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ɣ���Ͷ�'	                    ,'00259'  ,259  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�϶�����'	                    ,'00590'  ,590  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����/���ô�'	                  ,'001'    ,1    );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ħ���'	                      ,'00212'  ,212  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����������'	                  ,'00213'  ,213  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ͻ��˹'	                      ,'00216'  ,216  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00218'  ,218  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�Ա���'	                      ,'00220'  ,220  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ڼӶ�'	                    ,'00221'  ,221  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ë��������'	                  ,'00222'  ,222  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00223'  ,223  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00224'  ,224  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ص���'	                    ,'00225'  ,225  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����������'	                  ,'00226'  ,226  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ն�'	                      ,'00227'  ,227  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���'	                        ,'00228'  ,228  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00229'  ,229  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ë����˹'	                    ,'00230'  ,230  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'00231'  ,231  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'00232'  ,232  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'                          ,'00233'  ,233  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'00234'  ,234  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('է��'	                        ,'00235'  ,235  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�з�'	                        ,'00236'  ,236  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����¡'	                      ,'00237'  ,237  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��ý�'	                      ,'00238'  ,238  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���������'	                  ,'00240'  ,240  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00241'  ,241  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�չ�'	                        ,'00242'  ,242  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00243'  ,243  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00244'  ,244  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����Ǳ���'	                  ,'00245'  ,245  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��ɭ��'	                      ,'00247'  ,247  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����'	                      ,'00248'  ,248  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�յ�'	                        ,'00249'  ,249  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('¬����'	                      ,'00250'  ,250  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���������'	                  ,'00251'  ,251  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00252'  ,252  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�Ĵ�����'	                    ,'0061'   ,61   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00501'  ,501  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Σ������'	                    ,'00502'  ,502  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����߶�'	                    ,'00503'  ,503  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�鶼��˹'	                    ,'00504'  ,504  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�������'	                    ,'00505'  ,505  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��˹�����'	                  ,'00506'  ,506  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00507'  ,507  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00509'  ,509  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��³'	                        ,'0051'   ,51   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ī����'	                      ,'0052'   ,52   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�Ű�'	                        ,'0053'   ,53   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����͢'	                      ,'0054'   ,54   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0055'   ,55   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'0056'   ,56   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ױ���'	                    ,'0057'   ,57   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ͼ��¬'	                      ,'00688'  ,688  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����ά��'	                    ,'00591'  ,591  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00592'  ,592  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��϶��'	                    ,'00593'  ,593  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����������'	                  ,'00594'  ,594  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00595'  ,595  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�������'	                    ,'00596'  ,596  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00597'  ,597  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'00598'  ,598  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������'	                      ,'0064'   ,64   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ص�'	                        ,'001671' ,1671 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ƿ�˹��'	                    ,'0061891',61891);
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥ����'	                      ,'00618'  ,618  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�³'	                        ,'00674'  ,674  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00676'  ,676  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������Ⱥ��'	                  ,'00677'  ,677  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��Ŭ��ͼ'	                    ,'00678'  ,678  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('쳼�'	                        ,'00679'  ,679  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ƿ�Ⱥ��'	                    ,'00682'  ,682  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ŧ����'	                      ,'00683'  ,683  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����Ħ��'	                    ,'00684'  ,684  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����Ħ��'	                    ,'00685'  ,685  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����˹'	                    ,'00686'  ,686  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������Ⱥ��'	                ,'001670' ,1670 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�¿��������Ⱥ��'	            ,'00687'  ,687  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�Ͳ����¼�����'	              ,'00675'  ,675  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����'	                        ,'00680'  ,680  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�п�³'	                      ,'00690'  ,690  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ܿ���������'	                ,'00691'  ,691  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ܶ�Ⱥ��'	                  ,'00692'  ,692  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������Ⱥ��'	                  ,'00500'  ,500  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��;��/������'	                ,'001808' ,1808 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���˵�'	                      ,'00808'  ,808  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'001264' ,1264 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('Ӣ��ά����Ⱥ��'	              ,'001284' ,1284 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����ά����Ⱥ��'	              ,'001340' ,1340 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥ¬����'	                    ,'001758' ,1758 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����'	                      ,'001876' ,1876 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�͹���'	                      ,'001242' ,1242 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ͰͶ�˹'	                    ,'001246' ,1246 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����˹��'	                    ,'001907' ,1907 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������'	                    ,'00299'  ,299  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����ϺͰͲ���'	              ,'001268' ,1268 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��Ľ��Ⱥ��'	                  ,'001441' ,1441 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����Ⱥ��'	                    ,'001345' ,1345 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����������'	                ,'001767' ,1767 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ί������'	                    ,'0058'   ,58   );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�����ɴ�'	                    ,'001473' ,1473 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���������ص�'	                ,'001664' ,1664 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����������˹Ⱥ��'              ,'00599'  ,599  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�������'	                    ,'001787' ,1787 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����˹����/������˹̹���͹�'	  ,'007'    ,7    );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��˹���Ǻ�����ά��'	          ,'00387'  ,387  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�������ǹ��͹�'	              ,'00374'  ,374  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�׶���˹���͹�'	              ,'00375'  ,375  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��³���ǹ��͹�'	              ,'00995'  ,995  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������˹̹���͹�'	            ,'00996'  ,996  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('���ȱ��˹̹���͹�'	          ,'00998'  ,998  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������˹̹���͹�'	            ,'00992'  ,992  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������˹̹���͹�'	            ,'00993'  ,993  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��Ħ��/��Լ�ص�'	              ,'00269'  ,269  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥƤ�������ܿ�¡��(��)'	      ,'00508'  ,508  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥ����˹�и�����ά˹'	        ,'001869' ,1869 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥ��ɭ�ص�(Ӣ)'	              ,'001784' ,1784 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�������Ͷ�͸�'	            ,'001868' ,1868 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('�ؿ�˹�Ϳ���˹Ⱥ��'	          ,'001649' ,1649 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('����˹�͸�ʿ��Ⱥ��'	          ,'00681'  ,681  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������ӹ��͹�/�������'	      ,'001809' ,1809 );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������Ⱥ��(��)(��˹������˹)'  ,'0034928',34928);
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('������Ⱥ��(��)(ʥ��³˹)'	    ,'0034922',34922);
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('��������������/��ϣ��'	        ,'00689'  ,689  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ʥ����/��������'	              ,'00239'  ,239  );
INSERT INTO A_AREACODE(AREANAME,AREACODE,CODE) VALUES('ŵ���˵�/�ϼ���'	              ,'00672'  ,672  );
COMMIT;
/
DELETE FROM GW_BASEPARA WHERE ID BETWEEN 1 AND 166;

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(1,'single_send',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(2,'single_send',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(3,'single_send',1,'mobile',21,'���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(4,'single_send',1,'content',1000,'�������ݣ����֧��1000����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(5,'single_send',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(6,'single_send',1,'svrtype',32,'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(7,'single_send',1,'exno',6,'��չ��:���Ȳ��ܳ���6λ',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(8,'single_send',1,'custid',64,'�û��Զ�����ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(9,'single_send',1,'exdata',64,'�Զ�����չ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(10,'single_send',1,'param1',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(11,'single_send',1,'param2',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(12,'single_send',1,'param3',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(13,'single_send',1,'param4',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(14,'single_send',1,'moduleid',4,'ģ��ID��4�ֽ�����������0~2^31-1��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(15,'single_send',1,'Attime',14,'���Ŷ�ʱ����ʱ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(16,'single_send',1,'Validtime',14,'������Ч���ʱ�䣨��ѡ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(17,'single_send',1,'Rptflag',4,'�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(18,'single_send',2,'result',4,'���ŷ�����������0���ɹ�,��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(19,'single_send',2,'msgid',64,'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(20,'single_send',2,'custid',64,'�û��Զ�����ˮ��',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(21,'batch_send',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(22,'batch_send',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(23,'batch_send',1,'mobile',21,'���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(24,'batch_send',1,'content',1000,'�������ݣ����֧��1000����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(25,'batch_send',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(26,'batch_send',1,'svrtype',32,'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(27,'batch_send',1,'exno',6,'��չ��:���Ȳ��ܳ���6λ',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(28,'batch_send',1,'custid',64,'�û��Զ�����ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(29,'batch_send',1,'exdata',64,'�Զ�����չ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(30,'batch_send',1,'param1',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(31,'batch_send',1,'param2',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(32,'batch_send',1,'param3',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(33,'batch_send',1,'param4',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(34,'batch_send',1,'moduleid',4,'ģ��ID��4�ֽ�����������0~2^31-1��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(35,'batch_send',1,'Attime',14,'���Ŷ�ʱ����ʱ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(36,'batch_send',1,'Validtime',14,'������Ч���ʱ�䣨��ѡ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(37,'batch_send',1,'Rptflag',4,'�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(38,'batch_send',2,'result',4,'���ŷ�����������0���ɹ�,��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(39,'batch_send',2,'msgid',64,'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(40,'batch_send',2,'custid',64,'�û��Զ�����ˮ��',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(41,'multi_send',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(42,'multi_send',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(43,'multi_send',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(44,'multi_send',1,'multimt',8000,'���Ի���Ϣ����',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(45,'multi_send',1,'mobile',21,'�����ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(46,'multi_send',1,'content',1000,'�������ݣ����֧��1000����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(47,'multi_send',1,'svrtype',32,'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(47,'multi_send',1,'exno',6,'��չ��:���Ȳ��ܳ���6λ',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(49,'multi_send',1,'custid',64,'�û��Զ�����ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(50,'multi_send',1,'exdata',64,'�Զ�����չ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(51,'multi_send',1,'param1',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(52,'multi_send',1,'param2',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(53,'multi_send',1,'param3',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(54,'multi_send',1,'param4',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(55,'multi_send',1,'moduleid',4,'ģ��ID��4�ֽ�����������0~2^31-1��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(56,'multi_send',1,'Attime',14,'���Ŷ�ʱ����ʱ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(57,'multi_send',1,'Validtime',14,'������Ч���ʱ�䣨��ѡ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(58,'multi_send',1,'Rptflag',4,'�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(59,'multi_send',2,'result',4,'���Ի�Ⱥ����������0���ɹ�,��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(60,'multi_send',2,'msgid',64,'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(61,'multi_send',2,'custid',64,'�û��Զ�����ˮ��',1);




INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(62,'template_send',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(63,'template_send',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(64,'template_send',1,'tmplid',20,'����ģ���ţ��������20λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(65,'template_send',1,'mobile',21,'���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(66,'template_send',1,'content',1000,'�������ͱ���ֵ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(67,'template_send',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(68,'template_send',1,'svrtype',32,'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(69,'template_send',1,'exno',6,'��չ��:���Ȳ��ܳ���6λ',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(70,'template_send',1,'custid',64,'�û��Զ�����ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(71,'template_send',1,'exdata',64,'�Զ�����չ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(72,'template_send',1,'msgtype',64,'��Ϣ���ͣ�Ĭ����100',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(73,'template_send',1,'param1',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(74,'template_send',1,'param2',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(75,'template_send',1,'param3',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(76,'template_send',1,'param4',64,'�û��Զ�����������64���ֽ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(77,'template_send',1,'moduleid',4,'ģ��ID��4�ֽ�����������0~2^31-1��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(78,'template_send',1,'Attime',14,'���Ŷ�ʱ����ʱ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(79,'template_send',1,'Validtime',14,'������Ч���ʱ�䣨��ѡ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(80,'template_send',1,'Rptflag',4,'�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(81,'template_send',2,'result',4,'���ŷ�����������0���ɹ�,��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(82,'template_send',2,'msgid',64,'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(83,'template_send',2,'custid',64,'�û��Զ�����ˮ��',1);





INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(84,'get_mo',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(85,'get_mo',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(86,'get_mo',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(87,'get_mo',1,'retsize',4,'ÿ��������Ҫ��ȡ�������������',2);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(88,'get_mo',2,'result',4,'���ŷ�����������0���ɹ�,��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(89,'get_mo',2,'mos',8000,'���ŷ�����������0���ɹ�,��0:ʧ��',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(90,'get_mo',2,'msgid',64,'ƽ̨��ˮ��:������������ͨ��ƽ̨�е�Ψһ���',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(91,'get_mo',2,'mobile',21,'���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(92,'get_mo',2,'spno',21,'������ͨ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(93,'get_mo',2,'exno',6,'��չ��:���Ȳ��ܳ���6λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(94,'get_mo',2,'rtime',14,'���з��ص�ʱ��:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(95,'get_mo',2,'content',1000,'�������ݣ����֧��1000����',1);




INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(96,'get_rpt',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(97,'get_rpt',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(98,'get_rpt',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(99,'get_rpt',1,'retsize',4,'����������Ҫ��ȡ�������������',2);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(100,'get_rpt',2,'result',4,'��ȡ״̬������������0:�ɹ�,��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(101,'get_rpt',2,'rpts',4,'result��0ʱrptsΪ��',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(102,'get_rpt',2,'msgid',64,'ƽ̨��ˮ��:������������ͨ��ƽ̨�е�Ψһ���',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(103,'get_rpt',2,'custid',64,'�û��Զ�����ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(104,'get_rpt',2,'pknum',4,'��ǰ����',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(105,'get_rpt',2,'pktotal',4,'������',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(106,'get_rpt',2,'mobile',21,'���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(107,'get_rpt',2,'spno',21,'������ͨ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(108,'get_rpt',2,'exno',6,'����ʱ��д��exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(109,'get_rpt',2,'stime',19,'״̬�����Ӧ�����з���ʱ��:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(110,'get_rpt',2,'rtime',14,'״̬���淵��ʱ��:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(111,'get_rpt',2,'status',4,'����״̬0:�ɹ� ��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(112,'get_rpt',2,'errcode',7,'״̬����������',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(113,'get_rpt',2,'errdesc',15,'״̬���������������',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(114,'get_rpt',2,'exdata',64,'����ʱ��д��exdata',1);





INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(115,'get_balance',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(116,'get_balance',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(117,'get_balance',1,'timestamp',10,'ʱ���:24Сʱ�Ƹ�ʽ:MMDDHHMMSS,����10λ',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(119,'get_balance',2,'result',4,'��ѯ�����������0:�ɹ�,��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(120,'get_balance',2,'chargetype',4,'�Ʒ�����0:�����Ʒ�1:���Ʒ�',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(121,'get_balance',2,'balance',4,'�������������',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(122,'get_balance',2,'money',32,'result��0ʱrptsΪ��',1);



INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(123,'MO',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(124,'MO',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(125,'MO',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(126,'MO',1,'cmd',32,'����������������:������MO_REQ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(127,'MO',1,'seqid',4,'������Ϣ��ˮ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(128,'MO',1,'mos',8000,'������Ϣ',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(129,'MO',1,'msgid',64,'ƽ̨��ˮ��:��Ӧ�������󷵻ؽ���е�msgid',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(130,'MO',1,'mobile',21,'�ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(131,'MO',1,'spno',21,'����ʱ��д��exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(132,'MO',1,'exno',6,'����ʱ��д��exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(133,'MO',1,'rtime',14,'���з��ص�ʱ��YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(134,'MO',1,'content',1000,'��������:���֧��1000����',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(135,'MO',2,'cmd',32,'������MO_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(136,'MO',2,'seqid',4,'�������е�seqid����һ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(137,'MO',2,'result',4,'���ж���Ϣ��������0:�ɹ�,��0:ʧ��',2);




INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(138,'RPT',1,'userid',6,'�û��˺ţ��������6���ַ���ͳһ��д',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(139,'RPT',1,'pwd',32,'�û����룺����Сд32λ�ַ�',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(140,'RPT',1,'timestamp',10,'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(141,'RPT',1,'cmd',32,'����״̬������������:������RPT_REQ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(142,'RPT',1,'seqid',4,'������Ϣ��ˮ��',2);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(143,'RPT',1,'msgid',64,'ƽ̨��ˮ��:��Ӧ�������󷵻ؽ���е�msgid',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(144,'RPT',1,'custid',64,'�û��Զ�����ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(145,'RPT',1,'pknum',4,'��ǰ����',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(146,'RPT',1,'pktotal',4,'������',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(147,'RPT',1,'mobile',21,'�ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(148,'RPT',1,'spno',21,'������ͨ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(149,'RPT',1,'exno',6,'����ʱ��д��exno',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(150,'RPT',1,'stime',19,'״̬�����Ӧ�����з���ʱ��:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(151,'RPT',1,'rtime',14,'״̬���淵��ʱ��:YYYY-MM-DD HH:MM:SS',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(152,'RPT',1,'status',4,'����״̬0:�ɹ� ��0:ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(153,'RPT',1,'errcode',7,'״̬����������',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(154,'RPT',1,'errdesc',15,'״̬���������������',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(155,'RPT',1,'exdata',64,'����ʱ��д��exdata',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(156,'RPT',1,'rpts',8000,'״̬����',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(157,'RPT',2,'cmd',32,'������RPT_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(158,'RPT',2,'seqid',4,'�������е�seqid����һ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(159,'RPT',2,'result',4,'result',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(160,'single_send',1,'apikey',32,'�û�Ψһ��ʶ:32λ����,�������ṩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(161,'batch_send',1,'apikey',32,'�û�Ψһ��ʶ:32λ����,�������ṩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(162,'multi_send',1,'apikey',32,'�û�Ψһ��ʶ:32λ����,�������ṩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(163,'template_send',1,'apikey',32,'�û�Ψһ��ʶ:32λ����,�������ṩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(164,'get_mo',1,'apikey',32,'�û�Ψһ��ʶ:32λ����,�������ṩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(165,'get_rpt',1,'apikey',32,'�û�Ψһ��ʶ:32λ����,�������ṩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(166,'get_balance',1,'apikey',32,'�û�Ψһ��ʶ:32λ����,�������ṩ',1);
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
            VALUES(170,'get_mo',1,'moreq',64,'XML��moreq����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(171,'get_mo',2,'morsp',64,'morsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(172,'get_mo',2,'mo',64,'mo',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(173,'get_rpt',1,'rptreq',3600,'�����ȡRPT',1);
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
            VALUES(181,'single_send',3,'result',4,'result���ز���',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(182,'single_send',3,'msgid',4,'msgid����ʧ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(183,'single_send',3,'custid',64,'custid����ʧ��',1);
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
            VALUES(189,'batch_send',3,'result',4,'ʧ�ܻ�Ӧ',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(190,'batch_send',3,'msgid',8,'��ˮ��',4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(191,'batch_send',3,'custid',64,'�ͻ�id',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(192,'batch_send',3,'mtrsp',64,'mtrsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(193,'batch_send',4,'failmap',32,'failmap',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(194,'batch_send',4,'succmap',32,'�ɹ���Ӧ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(195,'batch_send',4,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(196,'batch_send',4,'mt',32,'mt',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(197,'batch_send',4,'mobile',32,'�ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(198,'batch_send',4,'msgid',32,'��ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(199,'batch_send',4,'custid',32,'�ͻ�ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(200,'batch_send',4,'mtrsp',32,'mt��Ӧ',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(201,'batch_send',5,'mtrsp',32,'mt��Ӧ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(202,'batch_send',5,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(203,'batch_send',5,'msgid',32,'msgid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(204,'batch_send',5,'custid',32,'�ͻ�ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(205,'batch_send',5,'mobile',32,'�ֻ���',1);
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
            VALUES(212,'multi_send',4,'succmap',32,'�ɹ���Ӧ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(213,'multi_send',4,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(214,'multi_send',4,'mt',32,'mt',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(215,'multi_send',4,'mobile',32,'�ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(216,'multi_send',4,'msgid',32,'��ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(217,'multi_send',4,'custid',32,'�ͻ�ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(218,'multi_send',4,'mtrsp',32,'mt��Ӧ',1);

 INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(219,'multi_send',5,'mtrsp',32,'mt��Ӧ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(220,'multi_send',5,'result',32,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(221,'multi_send',5,'msgid',32,'msgid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(222,'multi_send',5,'custid',32,'�ͻ�ID',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(223,'multi_send',5,'mobile',32,'�ֻ���',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(224,'multi_send',5,'mt',32,'mt',1);



INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(225,'get_mo',3,'result',4,'result����',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(226,'get_mo',3,'mos',64,'mos����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(227,'get_mo',3,'morsp',64,'morsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(228,'get_rpt',3,'result',20,'��ȡ״̬������������',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(229,'get_rpt',3,'rpts',20,'result��0ʱrptΪ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(230,'get_rpt',3,'rptrsp',3600,'rptrsp',1);


INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(231,'get_balance',5,'feersp',200,'feersp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(232,'get_balance',5,'result',200,'result',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(233,'get_balance',5,'balance',200,'balance',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(234,'MO',3,'cmd',64,'ȫʧ��cmd����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(235,'MO',3,'seqid',4,'ȫʧ��seqid����',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(236,'MO',3,'result',4,'ȫʧ��result����',2);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(237,'MO',4,'modetails',64,'��ϸ��Ϣ����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(238,'MO',4,'morsp',64,'morsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(239,'MO',4,'failmap',64,'failmap����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(240,'MO',4,'msgid',64,'msgid',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(241,'MO',4,'succmap',64,'succmap����',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(242,'MO',4,'result',4,'����result����',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(243,'MO',4,'seqid',4,'����seqid����',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(244,'MO',4,'cmd',64,'cmd����',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(245,'RPT',3,'cmd',20,'������RPT_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(246,'RPT',3,'seqid',15,'�������е�seqid����һ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(247,'RPT',3,'result',15,'״̬������������',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(248,'RPT',4,'rptdetails',200,'rptdetails',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(249,'RPT',4,'rptrsp',200,'rptrsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(250,'RPT',4,'failmap',200,'����ʧ�ܱ�ǩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(251,'RPT',4,'msgid',32,'ƽ̨��ˮ��',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(252,'RPT',4,'succmap',200,'���ֳɹ���ǩ',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(253,'RPT',4,'result',4,'״̬������������',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(254,'RPT',4,'seqid',4,'�������е�seqid����һ��',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(255,'RPT',4,'cmd',8,'RPT_RESP',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(256,'get_balance',3,'feersp',200,'feersp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(257,'get_balance',3,'result',4,'����ֵ',2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(258,'get_balance',3,'balance',4,'ʧ�ܲ�ѯ���',2);

DELETE FROM GW_BASEPARA WHERE ID BETWEEN 259 AND 264;
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(259,'RPT',1,'rptreq',3600,'״̬������������',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(260,'RPT',2,'rptrsp',3600,'rptrsp',1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(261,'RPT',3,'rptrsp',3600,'rptrsp',1);

INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)
            VALUES(262,'MO',1,'moreq',64,'XML��moreq����',1);
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
-- API5.5�������ͽӿ�send_single
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(267, 'send_single',   1,  'userid',    6,    '�û��˺ţ��������6���ַ���ͳһ��д',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(268, 'send_single',   1,  'pwd',       32,   '�û����룺����Сд32λ�ַ�',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(269, 'send_single',   1,  'mobile',    21,   '���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(270, 'send_single',   1,  'content',   1000, '�������ݣ����֧��1000����',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(271, 'send_single',   1,  'timestamp', 10,   'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(272, 'send_single',   1,  'svrtype',   32,   'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(273, 'send_single',   1,  'exno',      6,    '��չ��:���Ȳ��ܳ���6λ',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(274, 'send_single',   1,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(275, 'send_single',   1,  'exdata',    64,   '�Զ�����չ����',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(276, 'send_single',   1,  'param1',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(277, 'send_single',   1,  'param2',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(278, 'send_single',   1,  'param3',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(279, 'send_single',   1,  'param4',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(280, 'send_single',   1,  'moduleid',  4,    'ģ��ID��4�ֽ�����������0~2^31-1��',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(281, 'send_single',   1,  'attime',    14,   '���Ŷ�ʱ����ʱ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(282, 'send_single',   1,  'validtime', 14,   '������Ч���ʱ�䣨��ѡ��',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(283, 'send_single',   1,  'rptflag',   4,    '�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(284, 'send_single',   1,  'apikey',    32,   '�û�Ψһ��ʶ:32λ����,�������ṩ',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(285, 'send_single',   1,  'mtreq',     300,  'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(286, 'send_single',   2,  'result',    4,    '���ŷ�����������0���ɹ�,��0:ʧ��',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(287, 'send_single',   2,  'msgid',     64,   'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(288, 'send_single',   2,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(289, 'send_single',   2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(290, 'send_single',   3,  'result',    4,    'result���ز���',                                     2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(291, 'send_single',   3,  'msgid',     4,    'msgid����ʧ��',                                      2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(292, 'send_single',   3,  'custid',    64,   'custid����ʧ��',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(293, 'send_single',   3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(294, 'send_single',   5,  'result',    500,  'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(295, 'send_single',   5,  'msgid',     500,  'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(296, 'send_single',   5,  'custid',    500,  'custid',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(297, 'send_single',   5,  'mtrsp',     500,  'mtrsp',                                              1);

-- API5.5��ͬ����Ⱥ���ӿ�send_batch
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(298, 'send_batch',    1,  'userid',    6,    '�û��˺ţ��������6���ַ���ͳһ��д',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(299, 'send_batch',    1,  'pwd',       32,   '�û����룺����Сд32λ�ַ�',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(300, 'send_batch',    1,  'mobile',    21,   '���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(301, 'send_batch',    1,  'content',   1000, '�������ݣ����֧��1000����',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(302, 'send_batch',    1,  'timestamp', 10,   'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(303, 'send_batch',    1,  'svrtype',   32,   'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(304, 'send_batch',    1,  'exno',      6,    '��չ��:���Ȳ��ܳ���6λ',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(305, 'send_batch',    1,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(306, 'send_batch',    1,  'exdata',    64,   '�Զ�����չ����',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(307, 'send_batch',    1,  'param1',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(308, 'send_batch',    1,  'param2',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(309, 'send_batch',    1,  'param3',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(310, 'send_batch',    1,  'param4',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(311, 'send_batch',    1,  'moduleid',  4,    'ģ��ID��4�ֽ�����������0~2^31-1��',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(312, 'send_batch',    1,  'attime',    14,   '���Ŷ�ʱ����ʱ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(313, 'send_batch',    1,  'validtime', 14,   '������Ч���ʱ�䣨��ѡ��',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(314, 'send_batch',    1,  'rptflag',   4,    '�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(315, 'send_batch',    1,  'apikey',    32,   '�û�Ψһ��ʶ:32λ����,�������ṩ',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(316, 'send_batch',    1,  'mtreq',     16,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(317, 'send_batch',    2,  'result',    4,    '���ŷ�����������0���ɹ�,��0:ʧ��',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(318, 'send_batch',    2,  'msgid',     64,   'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(319, 'send_batch',    2,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(320, 'send_batch',    2,  'mtrsp',     64,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(321, 'send_batch',    3,  'result',    4,    'ʧ�ܻ�Ӧ',                                           2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(322, 'send_batch',    3,  'msgid',     8,    '��ˮ��',                                             4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(323, 'send_batch',    3,  'custid',    64,   '�ͻ�id',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(324, 'send_batch',    3,  'mtrsp',     64,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(325, 'send_batch',    4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(326, 'send_batch',    4,  'succmap',   32,   '�ɹ���Ӧ',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(327, 'send_batch',    4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(328, 'send_batch',    4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(329, 'send_batch',    4,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(330, 'send_batch',    4,  'msgid',     32,   '��ˮ��',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(331, 'send_batch',    4,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(332, 'send_batch',    4,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(333, 'send_batch',    5,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(334, 'send_batch',    5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(335, 'send_batch',    5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(336, 'send_batch',    5,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(337, 'send_batch',    5,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(338, 'send_batch',    5,  'mt',        64,   'mt',                                                 1);

-- API5.5���Ի�Ⱥ���ӿ�send_multi
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(339, 'send_multi',    1,  'userid',    6,    '�û��˺ţ��������6���ַ���ͳһ��д',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(340, 'send_multi',    1,  'pwd',       32,   '�û����룺����Сд32λ�ַ�',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(341, 'send_multi',    1,  'timestamp', 10,   'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(342, 'send_multi',    1,  'multimt',   8000, '���Ի���Ϣ����',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(343, 'send_multi',    1,  'mobile',    21,   '�����ֻ���',                                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(344, 'send_multi',    1,  'content',   1000, '�������ݣ����֧��1000����',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(345, 'send_multi',    1,  'svrtype',   32,   'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(346, 'send_multi',    1,  'exno',      6,    '��չ��:���Ȳ��ܳ���6λ',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(347, 'send_multi',    1,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(348, 'send_multi',    1,  'exdata',    64,   '�Զ�����չ����',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(349, 'send_multi',    1,  'param1',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(350, 'send_multi',    1,  'param2',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(351, 'send_multi',    1,  'param3',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(352, 'send_multi',    1,  'param4',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(353, 'send_multi',    1,  'moduleid',  4,    'ģ��ID��4�ֽ�����������0~2^31-1��',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(354, 'send_multi',    1,  'attime',    14,   '���Ŷ�ʱ����ʱ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(355, 'send_multi',    1,  'validtime', 14,   '������Ч���ʱ�䣨��ѡ��',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(356, 'send_multi',    1,  'rptflag',   4,    '�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(357, 'send_multi',    1,  'apikey',    32,   '�û�Ψһ��ʶ:32λ����,�������ṩ',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(358, 'send_multi',    1,  'mtreq',     32,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(359, 'send_multi',    1,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(360, 'send_multi',    2,  'result',    4,    '���Ի�Ⱥ����������0���ɹ�,��0:ʧ��',             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(361, 'send_multi',    2,  'msgid',     64,   'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(362, 'send_multi',    2,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(363, 'send_multi',    2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(364, 'send_multi',    3,  'result',    4,    'result',                                             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(365, 'send_multi',    3,  'msgid',     8,    'msgid',                                              4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(366, 'send_multi',    3,  'custid',    64,   'custid',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(367, 'send_multi',    3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(368, 'send_multi',    4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(369, 'send_multi',    4,  'succmap',   32,   '�ɹ���Ӧ',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(370, 'send_multi',    4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(371, 'send_multi',    4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(372, 'send_multi',    4,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(373, 'send_multi',    4,  'msgid',     32,   '��ˮ��',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(374, 'send_multi',    4,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(375, 'send_multi',    4,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(376, 'send_multi',    5,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(377, 'send_multi',    5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(378, 'send_multi',    5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(379, 'send_multi',    5,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(380, 'send_multi',    5,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(381, 'send_multi',    5,  'mt',        32,   'mt',                                                 1);

-- API5.5���Ի�Ⱥ���ӿ�send_mixed
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(382, 'send_mixed',    1,  'userid',    6,    '�û��˺ţ��������6���ַ���ͳһ��д',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(383, 'send_mixed',    1,  'pwd',       32,   '�û����룺����Сд32λ�ַ�',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(384, 'send_mixed',    1,  'timestamp', 10,   'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(385, 'send_mixed',    1,  'multimt',   8000, '���Ի���Ϣ����',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(386, 'send_mixed',    1,  'mobile',    21,   '�����ֻ���',                                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(387, 'send_mixed',    1,  'content',   1000, '�������ݣ����֧��1000����',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(388, 'send_mixed',    1,  'svrtype',   32,   'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(389, 'send_mixed',    1,  'exno',      6,    '��չ��:���Ȳ��ܳ���6λ',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(390, 'send_mixed',    1,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(391, 'send_mixed',    1,  'exdata',    64,   '�Զ�����չ����',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(392, 'send_mixed',    1,  'param1',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(393, 'send_mixed',    1,  'param2',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(394, 'send_mixed',    1,  'param3',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(395, 'send_mixed',    1,  'param4',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(396, 'send_mixed',    1,  'moduleid',  4,    'ģ��ID��4�ֽ�����������0~2^31-1��',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(397, 'send_mixed',    1,  'attime',    14,   '���Ŷ�ʱ����ʱ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(398, 'send_mixed',    1,  'validtime', 14,   '������Ч���ʱ�䣨��ѡ��',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(399, 'send_mixed',    1,  'rptflag',   4,    '�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(400, 'send_mixed',    1,  'apikey',    32,   '�û�Ψһ��ʶ:32λ����,�������ṩ',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(401, 'send_mixed',    1,  'mtreq',     32,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(402, 'send_mixed',    2,  'result',    4,    '���Ի�Ⱥ����������0���ɹ�,��0:ʧ��',             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(403, 'send_mixed',    2,  'msgid',     64,   'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(404, 'send_mixed',    2,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(405, 'send_mixed',    2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(406, 'send_mixed',    3,  'result',    4,    'result',                                             2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(407, 'send_mixed',    3,  'msgid',     8,    'msgid',                                              4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(408, 'send_mixed',    3,  'custid',    64,   'custid',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(409, 'send_mixed',    3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(410, 'send_mixed',    4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(411, 'send_mixed',    4,  'succmap',   32,   '�ɹ���Ӧ',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(412, 'send_mixed',    4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(413, 'send_mixed',    4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(414, 'send_mixed',    4,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(415, 'send_mixed',    4,  'msgid',     32,   '��ˮ��',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(416, 'send_mixed',    4,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(417, 'send_mixed',    4,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(418, 'send_mixed',    5,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(419, 'send_mixed',    5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(420, 'send_mixed',    5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(421, 'send_mixed',    5,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(422, 'send_mixed',    5,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(423, 'send_mixed',    5,  'mt',        32,   'mt',                                                 1);

-- API5.5ģ�巢�ͽӿ�send_template
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(424, 'send_template', 1,  'userid',    6,    '�û��˺ţ��������6���ַ���ͳһ��д',                1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(425, 'send_template', 1,  'pwd',       32,   '�û����룺����Сд32λ�ַ�',                         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(426, 'send_template', 1,  'tmplid',    20,   '����ģ���ţ��������20λ�ַ�',                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(427, 'send_template', 1,  'mobile',    21,   '���Ž��յ��ֻ��ţ�ֻ����һ���ֻ���',                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(428, 'send_template', 1,  'content',   1000, '�������ͱ���ֵ',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(429, 'send_template', 1,  'timestamp', 10,   'ʱ�����24Сʱ�Ƹ�ʽ��MMDDHHMMSS������10λ',         1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(430, 'send_template', 1,  'svrtype',   32,   'ҵ�����ͣ�����֧��32�����ȵ�Ӣ��������ϵ��ַ���', 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(431, 'send_template', 1,  'exno',      6,    '��չ��:���Ȳ��ܳ���6λ',                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(432, 'send_template', 1,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(433, 'send_template', 1,  'exdata',    64,   '�Զ�����չ����',                                     1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(434, 'send_template', 1,  'param1',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(435, 'send_template', 1,  'param2',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(436, 'send_template', 1,  'param3',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(437, 'send_template', 1,  'param4',    64,   '�û��Զ�����������64���ֽ�',                       1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(438, 'send_template', 1,  'moduleid',  4,    'ģ��ID��4�ֽ�����������0~2^31-1��',                  2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(439, 'send_template', 1,  'attime',    14,   '���Ŷ�ʱ����ʱ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(440, 'send_template', 1,  'validtime', 14,   '������Ч���ʱ�䣨��ѡ��',                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(441, 'send_template', 1,  'rptflag',   4,    '�Ƿ���Ҫ״̬����,0:��ʾ����Ҫ����0:��ʾ��Ҫ',        2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(442, 'send_template', 1,  'apikey',    32,   '�û�Ψһ��ʶ:32λ����,�������ṩ',                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(443, 'send_template', 1,  'mtreq',     32,   'mtreq',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(444, 'send_template', 2,  'result',    4,    '���ŷ�����������0���ɹ�,��0:ʧ��',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(445, 'send_template', 2,  'msgid',     64,   'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(446, 'send_template', 2,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(447, 'send_template', 2,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(448, 'send_template', 3,  'result',    4,    '���ŷ�����������0���ɹ�,��0:ʧ��',               2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(449, 'send_template', 3,  'msgid',     64,   'ƽ̨��ˮ��:��0,64λ����,��ӦJava��C#��long',         4);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(450, 'send_template', 3,  'custid',    64,   '�û��Զ�����ˮ��',                                   1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(451, 'send_template', 3,  'mtrsp',     32,   'mtrsp',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(454, 'send_template', 4,  'failmap',   32,   'failmap',                                            1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(455, 'send_template', 4,  'succmap',   32,   '�ɹ���Ӧ',                                           1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(456, 'send_template', 4,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(457, 'send_template', 4,  'mt',        32,   'mt',                                                 1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(458, 'send_template', 4,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(459, 'send_template', 4,  'msgid',     32,   '��ˮ��',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(460, 'send_template', 4,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(461, 'send_template', 4,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(462, 'send_template', 5,  'mtrsp',     32,   'mt��Ӧ',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(463, 'send_template', 5,  'result',    32,   'result',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(464, 'send_template', 5,  'msgid',     32,   'msgid',                                              1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(465, 'send_template', 5,  'custid',    32,   '�ͻ�ID',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(466, 'send_template', 5,  'mobile',    32,   '�ֻ���',                                             1);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(467, 'send_template', 5,  'mt',        32,   'mt',                                                 1);

-- API5.5��ѯ���ӿ�get_balance
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(452, 'get_balance',   3,  'chargetype',4,    '�Ʒ�����0:�����Ʒ�1:���Ʒ�',                       2);
INSERT INTO GW_BASEPARA(ID,FUNNAME,CMDTYPE, ARGNAME,ARGVALUELEN,ARGDES,ARGTYPE)  VALUES(453, 'get_balance',   3,  'money',     32,   'result��0ʱrptsΪ��',                                1);


COMMIT;
/
------------------------��ʼ������--------------------
MERGE INTO  GW_MSGTAIL A USING (SELECT 'Ĭ��ȫ����β' AS "TAIL_NAME",'�ظ�TD�˶�' AS "CONTENT",'100001' AS "CORP_CODE",0 AS USER_ID FROM  DUAL) B
ON (A.TAIL_NAME=B.TAIL_NAME AND A.CONTENT=B.CONTENT AND A.CORP_CODE=B.CORP_CODE)
WHEN NOT MATCHED THEN
INSERT (TAIL_NAME, CONTENT, CORP_CODE, USER_ID)VALUES('Ĭ��ȫ����β','�ظ�TD�˶�','100001',0);
/

MERGE INTO  GW_TAILBIND A USING (SELECT 0 AS "TAIL_ID", ' ' AS "BUS_CODE", ' ' AS "SPUSERID",
 0 AS "TAIL_TYPE", '100001' AS "CORP_CODE" , 0 AS "USER_ID" FROM  DUAL) B
ON (A.BUS_CODE=B.BUS_CODE AND A.SPUSERID=B.SPUSERID AND A.TAIL_TYPE=B.TAIL_TYPE AND A.CORP_CODE=B.CORP_CODE)
WHEN NOT MATCHED THEN
INSERT(TAIL_ID,BUS_CODE,SPUSERID,TAIL_TYPE,CORP_CODE,USER_ID)VALUES(0,' ',' ', 0, '100001', 0);
/

UPDATE GW_TAILBIND SET TAIL_ID=(SELECT MM.TAIL_ID FROM GW_MSGTAIL MM INNER JOIN GW_TAILBIND TT ON MM.CORP_CODE=TT.CORP_CODE
WHERE MM.CONTENT='�ظ�TD�˶�' AND MM.TAIL_NAME='Ĭ��ȫ����β' AND MM.CORP_CODE='100001' AND ROWNUM<=1);
/

MERGE INTO  GW_TAILCTRL A USING (SELECT 0 AS "OVERTAILFLAG",0 AS "OTHERTAILFLAG",'100001' AS "CORP_CODE" FROM  DUAL) B
ON (A.OVERTAILFLAG=B.OVERTAILFLAG AND A.OTHERTAILFLAG=B.OTHERTAILFLAG AND A.CORP_CODE=B.CORP_CODE)
WHEN NOT MATCHED THEN
INSERT (OVERTAILFLAG, OTHERTAILFLAG, CORP_CODE)VALUES(0,0,'100001');
/

COMMIT;
/
