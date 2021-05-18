declare

os_type number;--操作系统 ；0-windows 1-linux !!!!!! 若不符合实际情况，请自行修改 !!!!!!
 
file_path varchar2(200);--获取的路径，用于存放表空间

str varchar2(400);

tablespace_count number;

file_name varchar2(200);

tb_filename varchar2(200);--表空间的名称

file_size varchar2(20);

begin
  file_size := '512M';  --创建的数据文件大小，默认512M
  os_type:=1;		--默认为Linux操作系统
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
    dbms_output.put_line( ' 路径为：'||file_path); 
    
  --开始创建表空间................
  --EMPTABLESPACE
  tb_filename := 'emptablespace.dbf';
  file_name := ''''|| file_path||tb_filename||'''';
  str := 'CREATE TABLESPACE "EMP_TABLESPACE" '||
    ' NOLOGGING  '||
    ' DATAFILE '|| file_name||' SIZE '|| file_size||
    ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --设置自动扩展大小属性
  str := 'ALTER DATABASE '||
    ' DATAFILE '||file_name||
    ' AUTOEXTEND '||
    ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  
  --创建完成....................
  
  --TBSSMSACC
  tb_filename := 'tbssmsacc.dbf';
  file_name := ''''|| file_path||tb_filename||'''';
  str := 'CREATE TABLESPACE "TBSSMSACC" '||
    ' NOLOGGING  '||
    ' DATAFILE '|| file_name||' SIZE '|| file_size||
    ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --设置自动扩展大小属性
  str := 'ALTER DATABASE '||
    ' DATAFILE '||file_name||
    ' AUTOEXTEND '||
    ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --创建完成....................
  
  /*
  表空间创建规则：为数据区和索引区各创建一个表空间来存存储；历史库表空间个数和大小与实时库相等，但历史库的表空间初始大小应该小于实时库，
  因为实时库表空间初始值若小的话，每次扩展会影响运行效率，历史库无需过多考虑这一点；暂定为实时库B创建4段表空间文件存放数据，创建2段表空间
  文件来存放索引；为实时库A创建2段表空间文件来存放数据，创建1段表空间文件来存放索引；为实时库C创建2段表空间文件来存放数据，创建1段表空间文
  件来存放索引；为历史库创建4段表空间文件存放数据，创建2段表空间文件来存放索引；每个文件的初始大小阶梯式递减，第一个文件初始值大于后面的文件
  */
  --删除表空间文件并offline   
  --alter database datafile 'F:\ORACLE\PRODUCT\10.2.0\ORADATA\EMPSVR\tbpsvrdata_b03.dbf' offline drop;
  --脱机后需recover
  --alter database recover datafile  'F:\ORACLE\PRODUCT\10.2.0\ORADATA\EMPSVR\tbpsvrdata_b03.dbf'
  --表空间新文件online
  --alter database datafile 'F:\ORACLE\PRODUCT\10.2.0\ORADATA\EMPSVR\tbpsvrdata_b03.dbf' offline
  
  
  --开始创建表空间................
  --历史库表空间(索引独立建):
  --TBSHISDATA
  file_name := ''''|| file_path||'tbshisdata01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBSHISDATA" '||
      ' NOLOGGING '||
      ' DATAFILE '||file_name||' SIZE '||file_size|| 
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --设置自动扩展大小属性
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name || 
      ' AUTOEXTEND '||
      ' ON NEXT  '||file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBSHISDATA添加新文件并直接online
  file_name := ''''|| file_path||'tbshisdata02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSHISDATA" '||
      ' ADD '||
      ' DATAFILE '||file_name||
      ' SIZE '||file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBSHISDATA添加新文件并直接online
  file_name := ''''|| file_path||'tbshisdata03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSHISDATA" '||
      ' ADD '||
      ' DATAFILE '||file_name||
      ' SIZE '||file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBSHISDATA添加新文件并直接online
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
  --设置自动扩展大小属性
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name||
      ' AUTOEXTEND '||
      ' ON NEXT '||  file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBSHISINDEX添加新文件并直接online
  file_name := ''''|| file_path||'tbshisindex02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSHISINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  
  --运行库表空间(索引独立建):
  --TBSSVRDATA
  file_name := ''''|| file_path||'tbssvrdata01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBSSVRDATA" '||
      ' NOLOGGING '||
      ' DATAFILE '||file_name|| ' SIZE '|| file_size ||
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
   dbms_output.put_line(str);
  execute immediate str;
  --设置自动扩展大小属性
  str := 'ALTER DATABASE '|| 
      ' DATAFILE ' ||file_name ||
      ' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --为TBSSVRDATA添加新文件并直接online
  file_name := ''''|| file_path||'tbssvrdata02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRDATA" '||
      ' ADD '||
      ' DATAFILE '|| file_name||
      ' SIZE '|| file_size ||' AUTOEXTEND ' ||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --为TBSSVRDATA添加新文件并直接online
  file_name := ''''|| file_path||'tbssvrdata03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRDATA" '||
      ' ADD '||
      ' DATAFILE '|| file_name||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --为TBSSVRDATA添加新文件并直接online
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
  --设置自动扩展大小属性
  file_name := ''''|| file_path||'tbssvrindex01.dbf'||'''';
  str := 'ALTER DATABASE '|| 
      ' DATAFILE '||file_name||
      ' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --为TBSSVRINDEX添加新文件并直接online
  file_name := ''''|| file_path||'tbssvrindex02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --为TBSSVRINDEX添加新文件并直接online
  file_name := ''''|| file_path||'tbssvrindex03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  --为TBSSVRINDEX添加新文件并直接online
  file_name := ''''|| file_path||'tbssvrindex04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBSSVRINDEX" '||
      ' ADD '||
      ' DATAFILE '|| file_name ||
      ' SIZE '|| file_size ||' AUTOEXTEND '||
      ' ON NEXT '|| file_size;
   dbms_output.put_line(str);
  execute immediate str;
  
 
  --下行任务表空间(TBMTTASKDATA)
  --TBMTTASKDATA
  file_name := ''''|| file_path||'tbmttaskdata01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBMTTASKDATA" '||
      ' NOLOGGING '||
      ' DATAFILE '||file_name||' SIZE ' ||file_size || 
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --设置自动扩展大小属性
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name ||
      ' AUTOEXTEND ' ||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBMTTASKDATA添加新文件并直接online
  file_name := ''''|| file_path||'tbmttaskdata02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTTASKDATA" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBMTTASKDATA添加新文件并直接online
  file_name := ''''|| file_path||'tbmttaskdata03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTTASKDATA" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBMTTASKDATA添加新文件并直接online
  file_name := ''''|| file_path||'tbmttaskdata04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTTASKDATA" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  
  --为MT_TASK_PTMSGID索引单独建一表空间(TBMTINDEX_PTMSGID)
  --TBMTINDEX_PTMSGID
  file_name := ''''|| file_path||'tbmtindex_ptmsgid01.dbf'||'''';
  str := 'CREATE TABLESPACE "TBMTINDEX_PTMSGID" '||
      ' NOLOGGING '||
      ' DATAFILE ' ||file_name||' SIZE '|| file_size ||
      ' EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT  AUTO';
  dbms_output.put_line(str);
  execute immediate str;
  --设置自动扩展大小属性
  str := 'ALTER DATABASE '||
      ' DATAFILE '||file_name ||
      ' AUTOEXTEND ' ||
      ' ON NEXT '|| file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBMTINDEX_PTMSGID添加新文件并直接online
  file_name := ''''|| file_path||'tbmtindex_ptmsgid02.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTINDEX_PTMSGID" '||
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBMTINDEX_PTMSGID添加新文件并直接online
  file_name := ''''|| file_path||'tbmtindex_ptmsgid03.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTINDEX_PTMSGID" '|| 
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  --为TBMTINDEX_PTMSGID添加新文件并直接online
  file_name := ''''|| file_path||'tbmtindex_ptmsgid04.dbf'||'''';
  str := 'ALTER TABLESPACE "TBMTINDEX_PTMSGID" '|| 
      ' ADD '||
      ' DATAFILE ' ||file_name||
      ' SIZE ' || file_size || ' AUTOEXTEND '||
      ' ON NEXT ' || file_size;
  dbms_output.put_line(str);
  execute immediate str;
  
  --创建完成....................
  
end;
