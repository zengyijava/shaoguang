declare

os_type number;--����ϵͳ ��0-windows 1-linux !!!!!! ��������ʵ��������������޸� !!!!!!
 
file_path varchar2(200);--��ȡ��·�������ڴ�ű�ռ�

str varchar2(400);

tablespace_count number;

file_name varchar2(200);

tb_filename varchar2(200);--��ռ������

file_size varchar2(20);

begin
  file_size := '512M';  --�����������ļ���С��Ĭ��512M
  os_type:=1;		--Ĭ��ΪLinux����ϵͳ
  file_path:='';
  
  if os_type=0 then
     select  substr(file_name,0,instr(file_name,'\',-1,1)) into file_path
     from dba_data_files  where tablespace_name = 'SYSTEM'and file_id ='1';
  else
     select  substr(file_name,0,instr(file_name,'/',-1,1)) into file_path
     from dba_data_files where tablespace_name = 'SYSTEM' and file_id ='1';
  end if;
   
  if length(file_path) < 0 then
      file_path:='';
  end if;
    dbms_output.put_line( ' ·��Ϊ��'||file_path); 
    
  --��ʼ������ռ�................
  --EMPTABLESPACE
  tb_filename := 'emptablespace.dbf';
  file_name := ''''|| file_path||tb_filename||'''';
  str := 'CREATE TABLESPACE "EMP_TABLESPACE" '||
    ' NOLOGGING  '||
    ' DATAFILE '|| file_name||' SIZE '|| file_size||
    ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  str := 'ALTER DATABASE '||
    ' DATAFILE '||file_name||
    ' AUTOEXTEND '||
    ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  
  --�������....................
  
  --TBSSMSACC
  tb_filename := 'tbssmsacc.dbf';
  file_name := ''''|| file_path||tb_filename||'''';
  str := 'CREATE TABLESPACE "TBSSMSACC" '||
    ' NOLOGGING  '||
    ' DATAFILE '|| file_name||' SIZE '|| file_size||
    ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  str := 'ALTER DATABASE '||
    ' DATAFILE '||file_name||
    ' AUTOEXTEND '||
    ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --�������....................
  
  /*
  ��ռ䴴������Ϊ��������������������һ����ռ�����洢����ʷ���ռ�����ʹ�С��ʵʱ����ȣ�����ʷ��ı�ռ��ʼ��СӦ��С��ʵʱ�⣬
  ��Ϊʵʱ���ռ��ʼֵ��С�Ļ���ÿ����չ��Ӱ������Ч�ʣ���ʷ��������࿼����һ�㣻�ݶ�Ϊʵʱ��B����4�α�ռ��ļ�������ݣ�����2�α�ռ�
  �ļ������������Ϊʵʱ��A����2�α�ռ��ļ���������ݣ�����1�α�ռ��ļ������������Ϊʵʱ��C����2�α�ռ��ļ���������ݣ�����1�α�ռ���
  �������������Ϊ��ʷ�ⴴ��4�α�ռ��ļ�������ݣ�����2�α�ռ��ļ������������ÿ���ļ��ĳ�ʼ��С����ʽ�ݼ�����һ���ļ���ʼֵ���ں�����ļ�
  */
  --ɾ����ռ��ļ���offline   
  --alter database datafile 'F:\ORACLE\PRODUCT\10.2.0\ORADATA\EMPSVR\tbpsvrdata_b03.dbf' offline drop;
  --�ѻ�����recover
  --alter database recover datafile  'F:\ORACLE\PRODUCT\10.2.0\ORADATA\EMPSVR\tbpsvrdata_b03.dbf'
  --��ռ����ļ�online
  --alter database datafile 'F:\ORACLE\PRODUCT\10.2.0\ORADATA\EMPSVR\tbpsvrdata_b03.dbf' offline
  
  
  --��ʼ������ռ�................
  --��ʷ���ռ�(����������):
  --TBSHISDATA
  file_name := ''''|| file_path||'tbshisdata01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBSHISDATA" '||
      ' NOLOGGING '||
      ' DATAFILE '||file_name||' SIZE '||file_size|| 
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name || 
      ' AUTOEXTEND '||
      ' ON NEXT  '||file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSHISDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbshisdata02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSHISDATA" '||
      ' ADD '||
      ' DATAFILE '||file_name||
      ' SIZE '||file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSHISDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbshisdata03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSHISDATA" '||
      ' ADD '||
      ' DATAFILE '||file_name||
      ' SIZE '||file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSHISDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbshisdata04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSHISDATA" '||
      ' ADD '||
      ' DATAFILE '||file_name||
      ' SIZE '||file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  
  --TBSHISINDEX
  file_name := ''''|| file_path||'tbshisindex01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBSHISINDEX" '||
      ' NOLOGGING '||
      ' DATAFILE '|| file_name ||' SIZE '|| file_size ||
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name||
      ' AUTOEXTEND '||
      ' ON NEXT '||  file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSHISINDEX������ļ���ֱ��online
  file_name := ''''|| file_path||'tbshisindex02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSHISINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  
  --���п��ռ�(����������):
  --TBSSVRDATA
  file_name := ''''|| file_path||'tbssvrdata01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBSSVRDATA" '||
      ' NOLOGGING '||
      ' DATAFILE '||file_name|| ' SIZE '|| file_size ||
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
   dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  str := 'ALTER DATABASE '|| 
      ' DATAFILE ' ||file_name ||
      ' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSSVRDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbssvrdata02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRDATA" '||
      ' ADD '||
      ' DATAFILE '|| file_name||
      ' SIZE '|| file_size ||' AUTOEXTEND ' ||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSSVRDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbssvrdata03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRDATA" '||
      ' ADD '||
      ' DATAFILE '|| file_name||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSSVRDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbssvrdata04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRDATA" '||
      ' ADD '||
      ' DATAFILE '|| file_name||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  
  --TBSSVRINDEX
  file_name := ''''|| file_path||'tbssvrindex01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBSSVRINDEX" '||
      ' NOLOGGING '||
      ' DATAFILE '||file_name ||' SIZE '|| file_size ||
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
   dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  file_name := ''''|| file_path||'tbssvrindex01.dbf'||'''';
  str := 'ALTER DATABASE '|| 
      ' DATAFILE '||file_name||
      ' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSSVRINDEX������ļ���ֱ��online
  file_name := ''''|| file_path||'tbssvrindex02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSSVRINDEX������ļ���ֱ��online
  file_name := ''''|| file_path||'tbssvrindex03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --ΪTBSSVRINDEX������ļ���ֱ��online
  file_name := ''''|| file_path||'tbssvrindex04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  
 
  --���������ռ�(TBMTTASKDATA)
  --TBMTTASKDATA
  file_name := ''''|| file_path||'tbmttaskdata01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBMTTASKDATA" '||
      ' NOLOGGING '||
      ' DATAFILE '||file_name||' SIZE ' ||file_size || 
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name ||
      ' AUTOEXTEND ' ||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBMTTASKDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbmttaskdata02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTTASKDATA" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBMTTASKDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbmttaskdata03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTTASKDATA" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBMTTASKDATA������ļ���ֱ��online
  file_name := ''''|| file_path||'tbmttaskdata04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTTASKDATA" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  
  --ΪMT_TASK_PTMSGID����������һ��ռ�(TBMTINDEX_PTMSGID)
  --TBMTINDEX_PTMSGID
  file_name := ''''|| file_path||'tbmtindex_ptmsgid01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBMTINDEX_PTMSGID" '||
      ' NOLOGGING '||
      ' DATAFILE ' ||file_name||' SIZE '|| file_size ||
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --�����Զ���չ��С����
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name ||
      ' AUTOEXTEND ' ||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBMTINDEX_PTMSGID������ļ���ֱ��online
  file_name := ''''|| file_path||'tbmtindex_ptmsgid02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTINDEX_PTMSGID" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBMTINDEX_PTMSGID������ļ���ֱ��online
  file_name := ''''|| file_path||'tbmtindex_ptmsgid03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTINDEX_PTMSGID" '|| 
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --ΪTBMTINDEX_PTMSGID������ļ���ֱ��online
  file_name := ''''|| file_path||'tbmtindex_ptmsgid04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTINDEX_PTMSGID" '|| 
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  
  --�������....................
  
end;
