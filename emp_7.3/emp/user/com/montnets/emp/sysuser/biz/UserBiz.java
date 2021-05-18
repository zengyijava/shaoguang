package com.montnets.emp.sysuser.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom.Document;

import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepSpecialDAO;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDepUserBalance;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.sysuser.LfUpDep;
import com.montnets.emp.entity.sysuser.LfUpUserInfo;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.sysuser.util.RequestStr;
import com.montnets.emp.sysuser.util.ReturnReq;
import com.montnets.emp.sysuser.util.UpWSTools;
import com.montnets.emp.sysuser.util.XmlParser;
import com.montnets.emp.util.MD5;
/**
 * 处理同步操作员信息
 * @author yejiangmin
 *
 */
public class UserBiz  extends SuperBiz{
	
	private static GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
	//接口编码
	private static String FUNCTION_SYN1001 = "SYN1001";
	private static String FUNCTION_SYN1002 = "SYN1002";
	private UpWSTools upWsTools = new UpWSTools();
	private  SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 	请求IM消息中心同步操作员/机构 数据
	 * @param type	1.获取用户信息  2.获取部门信息
	 * @return
	 * @throws Exception
	 */
	public String syncUserDep(String type,String loginName){
		String returnMsg = "";
		try{
			String webserviceIp = "";
			String empCert = "";
			String empApId = "";
			try{
				//服务URL
				webserviceIp = SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_IP);
				//密钥
				empCert =  SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_CERT);
				//系统标识
				empApId =  SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_APID);
			}catch (Exception e) {
				//这里是未获取配置文件数据失败
				returnMsg = "noValue";
				EmpExecutionContext.error(e,"未获取配置文件数据失败");
			}
			boolean isGetValue = false;
			if(UpWSTools.isNotNull(webserviceIp) && UpWSTools.isNotNull(empCert) && UpWSTools.isNotNull(empApId)){
				isGetValue = true;
			}
			//如果获取到EMP的配置文件数据的话
			if(isGetValue){
				//~~~~~~~~~~~~~~~~~~封装请求XML 并且调用服务端接口
				//组装请求的XML格式文件	
				RequestStr requestStr = new RequestStr();
				requestStr.setBcode(FUNCTION_SYN1001);
				requestStr.setApid(empApId);
				requestStr.setCert(empCert);
				requestStr.setTpe(type);
			
				//定义服务端返回的同步数据XML格式
				String xml = "";
				String inputXMLStr = upWsTools.returnRequestStr(requestStr);
				if(UpWSTools.isNotNull(inputXMLStr)){
					//调用webservice接口，请求数据获取服务端返回的数据
					try{
						xml = UpWSTools.SYNWS(inputXMLStr, webserviceIp);
					}catch (Exception e) {
						//请求webservice接口失败
						returnMsg = "requestFail";
						xml = "";
						EmpExecutionContext.error(e,"请求webservice接口失败");
					}
				}
				//~~~~~~~~~~~~~~~~~~对服务端的返回值做EMP处理操作
				//解吸处理返回的数据
				if(UpWSTools.isNotNull(xml)){
					returnMsg = this.readReqStrXml(xml, type, loginName,empApId,empCert);
					//同步成功
					if(UpWSTools.isNotNull(returnMsg)){
						String backStr = "";
						//~~~~~~~~~~~~~~~~~~~~~~~初始化请求报文对象
						ReturnReq returnReq = new ReturnReq();
						returnReq.setBcode(FUNCTION_SYN1002);
						returnReq.setApid(empApId);
						returnReq.setCert(empCert);
						returnReq.setHaveSer(false);
						boolean isHaveSer = false;
						
						if("fail".equals(returnMsg)){
							//各种失败情况 异常情况
							returnReq.setResultCode("103");
							returnReq.setResultMsg("报文格式不对");
							returnReq.setErr("报文格式不对");
							returnReq.setSer("");
							returnReq.setHaveSer(false);
							isHaveSer = false;
						}else{
							String[] arr = returnMsg.split("&");
							String serialNum = arr[1];
							String optionType = arr[0];
							isHaveSer = upWsTools.returnResponStr(returnReq, optionType, serialNum);
						}
						//~~~~~~~~~~~~~~~~结束
						//~~~~~~~~~~~~~~~~~~~~~~~发送处理结果给服务端
						if(isHaveSer){
							backStr = upWsTools.returnReqStr(returnReq);
							if(UpWSTools.isNotNull(backStr)){
								try{
									UpWSTools.SYNWS(backStr, webserviceIp);
									//完整的一次请求成功操作
									returnMsg = "suceess";
								}catch (Exception e) {
									//再次请求失败	发送回执
									returnMsg = "requestAaginFail";
									EmpExecutionContext.error(e,"请求失败!");
								}
							}
						}
						//~~~~~~~~~~~~~~~~~~~~~~~结束
					}
				}
			}
		}catch (Exception e) {
			//操作失败
			returnMsg = "error";
			EmpExecutionContext.error(e,"请求IM消息中心同步操作员/机构 数据失败！");
		}
		return returnMsg;
	}
	
	
	
	
	
	/**
	 *   发送同步请求返回报文处理
	 * @param xml	报文STR
	 * @param type	类型
	 * @param loginName	登录名
	 * @return
	 * @throws Exception
	 */
	public String readReqStrXml(String xml,String type,String loginName,String empApId,String empCert) throws Exception {
		 String returnMsg = "";
		 try{
			XmlParser xmlParser = new XmlParser();
			Document document = xmlParser.getDocument(xml);
			String bCode = xmlParser.parseXMLStr(document, "BCode");
			String apId = xmlParser.parseXMLStr(document, "ApId");
			String cert = xmlParser.parseXMLStr(document, "Cert");
			String cnxt = xmlParser.parseXMLStr(document, "Cnxt");
			String resultCode = xmlParser.parseXMLStr(document, "RESULTCODE");
		//	String resultMsg = xmlParser.parseXMLStr(document, "RESULTMSG");
			String serialNum = "";
			if(empApId.equals(apId)){
				//判断密钥是否正确
				  if(empCert.equals(cert)){
					  if("0".equals(resultCode)){
						  	String toDecodeStr = "";
							toDecodeStr = UpWSTools.toDecode(cnxt);
							if(UpWSTools.isNotNull(toDecodeStr)){
								document=xmlParser.getDocument("<Cnxt>"+toDecodeStr+"</Cnxt>");
								//操作数据量
								String number=xmlParser.parseXMLStr(document, "NUM");
								//流水号
								serialNum=xmlParser.parseXMLStr(document, "SER");
								String nxt = xmlParser.parseXMLStr(document, "NXT");
								if(UpWSTools.isNotNull(number)){
									Integer count = Integer.valueOf(number);
									if(count > 0){
										//判断是用户信息还是机构信息
										String msg = "";
										if("1".equals(type)){
											msg = this.analysisUser(xmlParser, document, loginName, count);
										}else if("2".equals(type)){
											msg = this.analysisDep(xmlParser, document, loginName, count);
										}
										//返回执行结果   fail / success  以及流水号
										returnMsg = msg +"&" + serialNum;
									}
								}
							}else{
								  //没有报文主体
								  returnMsg = "noContent&noSer";
							}
					  }else{
						  //报文错误
						  returnMsg = "fail";
					  }
				  }else{
					  //密钥错误
					  returnMsg = "certError&noSer";
				  }
			}else{
				//应用编码错误
				returnMsg = "apidError&noSer";
			}
		 }catch (Exception e) {
			EmpExecutionContext.error(e,"发送同步请求返回报文处理失败！");
			returnMsg = "fail";
		}
		 return returnMsg;
	}
	
	/**
	 *   解析操作员的报文主体，并且执行数据库操作
	 * @param xmlParser	解析XML文件对象
	 * @param document	文档对象
	 * @param loginName	登录名称
	 * @param number	获取数据量
	 * @return
	 */
	public String analysisUser(XmlParser xmlParser,Document document,String loginName,Integer count){
		//返回字符
		String returnMsg = "";
		try{
				//存放新增的操作员
				List<LfSysuser> addUserList = new ArrayList<LfSysuser>();
				//存放新增的ADMIN
				List<LfSysuser> addAdminList = new ArrayList<LfSysuser>();
				//存放修改的操作员
				List<LfSysuser> updateUserList = new ArrayList<LfSysuser>();
				
				//存放删除的操作员
				List<LfSysuser> delUserList = new ArrayList<LfSysuser>();
				
				//存放需要禁用的操作员的对应EMP的用户ID
				List<LfSysuser> forbiddenList= new ArrayList<LfSysuser>();
				//存放KEY是统一用户平台的机构ID   VALUE是EMP所对应的机构对象 
                LinkedHashMap<String, LfDep> map = new LinkedHashMap<String, LfDep>();
                //条件查询
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                //新增的EMP操作员对象
				LfSysuser user = null;
				//操作员所对应的EMP机构
				LfDep lfdep = null;
				//操作员对象LIST
				List<LfSysuser> tempList = null;
				//禁用的操作员
				LfSysuser forb_sysuser = null;
				
				for(int i=0;i<count;i++){
					
					//~~~~~~~~~~~~~~~~组装一个操作员对象    BEGIN
					//初始化需要新增的操作员
             	   user = new LfSysuser();
				   //操作
				   String opt = xmlParser.parseXMLStr(document, "UReq/BODY","OPT", i);
				   //部门编号
				   String dpc = xmlParser.parseXMLStr(document, "UReq/BODY","DPC", i);
				   //企业编码
				   String cpc = xmlParser.parseXMLStr(document, "UReq/BODY","CPC", i);
				   //同步平台用户GUID
				   String upGuId = xmlParser.parseXMLStr(document, "UReq/BODY","UCD", i);
				   user.setName(xmlParser.parseXMLStr(document, "UReq/BODY","UNM", i));
                   String sex = xmlParser.parseXMLStr(document, "UReq/BODY","FEX", i);
                   if(UpWSTools.isNotNull(sex)){
                	   user.setSex(Integer.valueOf(sex));
                   }else{
                	   user.setSex(1);
                   }
                   user.setOph(xmlParser.parseXMLStr(document, "UReq/BODY","TEL", i));
                   user.setQq(xmlParser.parseXMLStr(document, "UReq/BODY","QQ", i));
                   user.setEMail(xmlParser.parseXMLStr(document, "UReq/BODY","EML", i));
                   user.setMsn(xmlParser.parseXMLStr(document, "UReq/BODY","MSN", i));
                   //用户名称
                   String userName = xmlParser.parseXMLStr(document, "UReq/BODY","LUM", i);
                   String password = xmlParser.parseXMLStr(document, "UReq/BODY","PWD", i);
                   if(UpWSTools.isNotNull(password)){
                	   user.setPassword(password);
                   }else{
                	   user.setPassword(MD5.getMD5Str(password+userName.toLowerCase()));
                   }
                   user.setMobile(xmlParser.parseXMLStr(document, "UReq/BODY","MBL", i));
                   //企业编码
                   user.setCorpCode(cpc);
                   //对应用户统一管理平台的用户ID
                   user.setUpGuId(Long.valueOf(upGuId));
                   //判断是1普通用户还是2管理员
                   String userTpye = xmlParser.parseXMLStr(document, "UReq/BODY","UTP", i);
                   //状态位
                   String userstate = xmlParser.parseXMLStr(document, "UReq/BODY","STA", i);
                   
                   //~~~~~~~~~~~~~~~~~~~~~~~~~查询该操作员所对应的机构树
				   //获取其操作员所对应的EMP机构对象
				   lfdep = this.getDepByayaDepId(map, cpc, dpc);
				   user.setUserName(userName);
            	   user.setRegTime(new Timestamp(System.currentTimeMillis()));
            	   user.setPermissionType(1);
            	   if(lfdep != null){
            		   user.setDepId(lfdep.getDepId());
            	   }
                   user.setWorkState(1);
                   user.setOnLine(0);
                   user.setClientState(0);
                   user.setUserType(1);
                   user.setHolder(loginName);
                   user.setIsExistSubNo(2);
                   //如果为空的话，则填充1为激活用户
                   if(UpWSTools.isNotNull(userstate)){
                	   user.setUserState(Integer.valueOf(userstate));
                   }else{
                	   user.setUserState(1);
                   }
                   //~~~~~~~~~~~~~~~~组装一个操作员对象    END
                   
                   
				   //暂时不处理    删除操作
                   if("2".equals(opt)){
                	   tempList = null;
                	   conditionMap.clear();
                	   conditionMap.put("upGuId",upGuId);
                	 //查询出该需要删除的操作员
            		   tempList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
            		   //将查询到的需要删除的操作员放到集合中
            		   if(tempList != null && tempList.size()>0){
            			   delUserList.add(tempList.get(0));
            		   }
            	   }else if("4".equals(opt)){
            		   //将所有需要禁用的用户的统一平台GUID对应到EMP系统中的操作员
                       //初始化
            		   tempList = null;
            		   forb_sysuser = null;
            		   //清空
            		   conditionMap.clear();
            		   conditionMap.put("corpCode",cpc);
            		   conditionMap.put("upGuId",upGuId);
            		   //查询出该需要禁用的操作员
            		   tempList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
            		   //将查询到的需要禁用的操作员放到集合中
            		   if(tempList != null && tempList.size()>0){
            			   forb_sysuser = tempList.get(0);
                           //用户状态   0失效   1激活 
                           if(UpWSTools.isNotNull(userstate)){
                        	   forb_sysuser.setUserState(Integer.valueOf(userstate));
                           }else{
                        	   forb_sysuser.setUserState(1);
                           }
            			   forbiddenList.add(forb_sysuser);
            		   }else{
            			   //如果没找到可禁用的操作员，则执行新增操作
            			   Long guid = globalBiz.getValueByKey("guid", 1L);
                           user.setGuId(guid);
                           user.setUserState(0);
                           user.setUserCode(String.valueOf(guid));
            			   addUserList.add(user);
            		   }
                   }else{
                    	   if("1".equals(opt)){
                    		   //新增
                               if("1".equals(userTpye)){
                            	   //验证该用户名在数据库是否存   /  不存在则新增
                            	   tempList = null;
                            	   conditionMap.clear();
                        		   conditionMap.put("corpCode",cpc);
                        		   conditionMap.put("userName",userName);
                        		   tempList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
                        		   if(tempList == null || tempList.size() == 0){
                        			   Long guid = globalBiz.getValueByKey("guid", 1L);
                                       user.setGuId(guid);
                                       user.setUserCode(String.valueOf(guid));
                                       addUserList.add(user);
                        		   }else{
                        			   //存在该用户名则修改
                        			   if("admin".equals(userName)){
                        				   addAdminList.add(user);
                        			   }else{
                        				   updateUserList.add(user);
                        			   }
                        		   }
                               }else if("2".equals(userTpye)){	//这里是处理ADMIN的 
                            	   //新增ADMIN
                            	   addAdminList.add(user);
                               }
                           }else if("3".equals(opt)){
                        	   //修改操作员
                        	   updateUserList.add(user);
                           }
                   }
				}
				returnMsg =  this.OperationUser(addUserList, addAdminList,updateUserList,forbiddenList,delUserList);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"解析操作员的报文主体失败！");
			returnMsg = "fail";
		}	 
		return returnMsg;
	}	
	
	/**
	 *   同步操作 操作员
	 * @param addUserList	新增普通操作员LIST
	 * @param addAdminList	新增管理员LIST
	 * @param updateUserList 需要修改的用户LIST
	 * @param forbiddenList	需要禁用的操作员用户
	 * @param corpCode		企业编码
	 * @return
	 */
	public String OperationUser(List<LfSysuser> addUserList ,List<LfSysuser> addAdminList,List<LfSysuser> updateUserList,
			List<LfSysuser> forbiddenList,List<LfSysuser> delUserList){
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();	
		String returnMsg = "fail"; 
		//获取数据库连接
				Connection conn = empTransDao.getConnection();
				try{
					//开启事务
					boolean flag = false;
					empTransDao.beginTransaction(conn);
					//----------------对ADMIN进行ADD操作
					 if(addAdminList != null && addAdminList.size() > 0){
						 for(int j=0;j<addAdminList.size();j++){
							 //获取操作员对象
							  LfSysuser adminUser = addAdminList.get(j);
							  flag = true;
							  conditionMap.clear();
							  conditionMap.put("userName", adminUser.getUserName());
							  conditionMap.put("corpCode", adminUser.getCorpCode());
							  //查询在EMP中所对象的企业ADMIN对象
							  List<LfSysuser> sysuserList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
							  LfSysuser lfSysuser = null;
							  if(sysuserList != null && sysuserList.size()>0){
								  //获取EMP的管理员信息
								  lfSysuser = sysuserList.get(0);
								  //判断是否有对应的统一用户平台对象
								  lfSysuser.setUpGuId(adminUser.getUpGuId());
								  if(UpWSTools.isNotNull(adminUser.getName())){
									  lfSysuser.setName(adminUser.getName());
								  }
								  if(UpWSTools.isNotNull(adminUser.getSex())){
									  lfSysuser.setSex(adminUser.getSex());
								  }
								  if(UpWSTools.isNotNull(adminUser.getOph())){
									  lfSysuser.setOph(adminUser.getOph());
								  }
								  if(UpWSTools.isNotNull(adminUser.getQq())){
									  lfSysuser.setQq(adminUser.getQq());
								  }
								  if(UpWSTools.isNotNull(adminUser.getEMail())){
									  lfSysuser.setEMail(adminUser.getEMail());
								  }
								  if(UpWSTools.isNotNull(adminUser.getMsn())){
									  lfSysuser.setMsn(adminUser.getMsn());
								  }
								  if(UpWSTools.isNotNull(adminUser.getPassword())){
									  lfSysuser.setPassword(adminUser.getPassword());
								  }
								  if(UpWSTools.isNotNull(adminUser.getCorpCode())){
									  lfSysuser.setCorpCode(adminUser.getCorpCode());
								  }
								  empTransDao.update(conn, lfSysuser);
							  }else{
								  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~如果查询修改的EMP管理员
								  Long guid = globalBiz.getValueByKey("guid", 1L);
								  adminUser.setGuId(guid);
								  adminUser.setUserCode(String.valueOf(guid));
								  empTransDao.save(conn, adminUser);
							  }
						 }
					 }
					 
					 //处理新增普通操作员
					 if(addUserList != null && addUserList.size() > 0){
						 flag = true;
						 empTransDao.save(conn, addUserList, LfSysuser.class);
					 }
					 
					 //处理修改操作员信息
					 if(updateUserList != null && updateUserList.size() > 0){
						 flag = true;
						 //查询对应的操作员
						 List<LfSysuser> empSysUserList = null;
						 //操作员对象
						 LfSysuser empSysUser = null;
						 //装需要更新操作员信息的对象
						 LfSysuser temp = null;
						 for(int i=0;i<updateUserList.size();i++){
							 //初始化
							 temp = null;
							 empSysUser = null;
							 empSysUserList = null;
							 //需要修改的对象内容
							 temp = updateUserList.get(i);
							 Long upGuId = temp.getUpGuId();
							 //对应EMP中的操作员
							 conditionMap.clear();
							 conditionMap.put("upGuId",String.valueOf(upGuId));
							 conditionMap.put("corpCode",temp.getCorpCode());
							 //查询对应的EMP的操作员对象
							 empSysUserList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
							 if(empSysUserList != null && empSysUserList.size() > 0){
								 empSysUser = empSysUserList.get(0);
								 if(UpWSTools.isNotNull(temp.getName())){
									 empSysUser.setName(temp.getName());
								 }
								 if(UpWSTools.isNotNull(temp.getSex())){
									 empSysUser.setSex(temp.getSex());
								 }
								 if(UpWSTools.isNotNull(temp.getOph())){
									 empSysUser.setOph(temp.getOph());
								 }
								 if(UpWSTools.isNotNull(temp.getQq())){
									 empSysUser.setQq(temp.getQq());
								 }
								 if(UpWSTools.isNotNull(temp.getEMail())){
									 empSysUser.setEMail(temp.getEMail());
								 }
								 if(UpWSTools.isNotNull(temp.getMsn())){
									 empSysUser.setMsn(temp.getMsn());
								 }
								 if(UpWSTools.isNotNull(temp.getPassword())){
									 empSysUser.setPassword(temp.getPassword());
								 }
								 if(UpWSTools.isNotNull(temp.getMobile())){
									 empSysUser.setMobile(temp.getMobile());
								 }
								 if(UpWSTools.isNotNull(temp.getDepId())){
									 empSysUser.setDepId(temp.getDepId());
								 }
								 empTransDao.update(conn, empSysUser);
							 }else{
								 //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~如果查询修改的EMP操作员则进行新增
								  Long guid = globalBiz.getValueByKey("guid", 1L);
								  temp.setGuId(guid);
								  temp.setUserCode(String.valueOf(guid));
								  empTransDao.save(conn, temp);
							 }
							 
						 }
					 }
					 //处理操作员禁用/激活的
					 if(forbiddenList != null && forbiddenList.size()>0){
						 flag = true;
						 LfSysuser forbUser = null;
						 for(int i=0;i<forbiddenList.size();i++){
							 //初始化
							 forbUser = null;
							 conditionMap.clear();	
							 orderbyMap.clear();
							 objectMap.clear();
							 forbUser = forbiddenList.get(i);
							 //使禁用
							 if(forbUser.getUserState() == 0){
									//使用户失效的时候，将尾号删除
									if(forbUser.getIsExistSubNo() == 1){
											conditionMap.put("corpCode", forbUser.getCorpCode());
											conditionMap.put("loginId", String.valueOf(forbUser.getGuId()));
											empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);
											empTransDao.delete(conn, LfSubnoAllot.class, conditionMap);
											//将操作员设置为没有尾号状态
											forbUser.setIsExistSubNo(2);								
									}
									//查找此用户是否还有定时中的短信任务
									conditionMap.clear();				
									//发送用户
									conditionMap.put("userId", String.valueOf(forbUser.getUserId()));
									//未发送
									conditionMap.put("sendstate", "0");
									//审批被拒绝的除外
									conditionMap.put("reState&<>", "2");
									//未被撤销的
									conditionMap.put("subState&<>", "3");
									orderbyMap.put("submitTime", StaticValue.DESC);
									List<LfMttask> mtList = empDao.findListBySymbolsCondition(LfMttask.class, conditionMap, orderbyMap);
									if(mtList !=null && mtList.size()>0)
									{
										//冻结所有的这些任务
										String sub = "";
										for(int j=0;j<mtList.size();j++)
										{
											LfMttask mt = mtList.get(j);
											sub+=mt.getMtId()+",";
										}
										sub = sub.substring(0,sub.length()-1);
										//改成已冻结
										objectMap.put("subState", "4");
										conditionMap.clear();
										conditionMap.put("mtId", sub);
										empTransDao.update(conn, LfMttask.class, objectMap, conditionMap);  
									}
									//修改操作员状态
									boolean updateResult = empTransDao.update(conn, forbUser);	
									//提交事务
									//修改成功后移除该操作员的登录状态
									if (updateResult) {
										new SysuserPriBiz().removeLoginInfoMapByUserid(forbUser.getUserId());
									}
							 }else  if(forbUser.getUserState() == 1){
							 //激活
								//查找此用户所以已冻结的任务(所有已冻结的任务变成已提交)
									//发送用户
									conditionMap.put("userId", String.valueOf(forbUser.getUserId()));
									//已冻结的
									conditionMap.put("subState", "4");
									orderbyMap.put("submitTime", StaticValue.DESC);
									List<LfMttask> mtList = empDao.findListBySymbolsCondition(LfMttask.class, conditionMap, orderbyMap);
									if (mtList != null && mtList.size() > 0) {
										//冻结所有的这些任务
										for (int j = 0; j < mtList.size(); j++) {
											LfMttask mt = mtList.get(j);
											//设为已提交状态
											mt.setSubState(2);
											empTransDao.update(conn,mt);
										}
									}
									empTransDao.update(conn, forbUser);	
							 }
						 }
					 }
					//提交事务
					 if(flag){
						empTransDao.commitTransaction(conn);
						returnMsg = "success";
					 }
				}catch (Exception e) {
					EmpExecutionContext.error(e,"同步操作 操作员失败！");
					empTransDao.rollBackTransaction(conn);
					returnMsg = "fail";
				} finally {
					//关闭连接
					empTransDao.closeConnection(conn);
				}
				
				
				//~~~~~~~~~~~~~~~~~~~~~~~~这里处理删除操作员
				if(delUserList != null){
					SysuserPriBiz sysUserBiz = new SysuserPriBiz();
					LfSysuser temp = null;
					for(int i=0;i<delUserList.size();i++){
						try{
							temp = delUserList.get(0);
							sysUserBiz.deleteUser(temp);
						}catch (Exception e) {
							EmpExecutionContext.error(e,"删除操作员失败！");
						}
					}
				}
				 return returnMsg;
	}
	

	/**
	 *   解析操作员机构的报文主体，并且执行数据库操作
	 * @param xmlParser	解析XML文件对象
	 * @param document	文档对象
	 * @param loginName	登录名称
	 * @param number	获取数据量
	 * @return
	 */
	public String analysisDep(XmlParser xmlParser,Document document,String loginName,Integer count){
		String returnMsg = "fail";
		try{
			//新增操作员机构
			List<LfDep> addDepList = new ArrayList<LfDep>();
			//修改操作员机构
			List<LfDep> updateDepList = new ArrayList<LfDep>();
			//修改操作员机构
			List<LfDep> delDepList = new ArrayList<LfDep>();
			//存放所有的同步机构数据
			 LinkedHashMap<String, LfDep> upDepMap = new LinkedHashMap<String, LfDep>(); 
			 
			LfDep dep = null;
			if(count > 0){
				for(int i=0;i<count;i++){
					dep = new LfDep();	
				    //操作类型
					String opt = xmlParser.parseXMLStr(document, "DepReq/BODY","OPT", i);
				    //企业代码
				    String corpCode = xmlParser.parseXMLStr(document, "DepReq/BODY","CRD", i);
	                String depName = xmlParser.parseXMLStr(document, "DepReq/BODY","DNM", i);
	                //部门编号
	                String upDepId = xmlParser.parseXMLStr(document, "DepReq/BODY","DCD", i);
//	            	String utm = xmlParser.parseXMLStr(document, "DepReq/BODY","UTM", i);
					//机构简称
				    dep.setDepCodeThird(xmlParser.parseXMLStr(document, "DepReq/BODY","DSN", i));
                	 //机构级次		无用
				    String depLevel = xmlParser.parseXMLStr(document, "DepReq/BODY","DLV", i);
				   /* //新增的时候判断其机构等级是否有值
	                if(UpWSTools.isNotNull(depLevel)){
	                	dep.setDepLevel(Integer.valueOf(depLevel));
	                }else{
	                	continue;
	                }*/
	                //上级机构代码
	                String pareDepId = xmlParser.parseXMLStr(document, "DepReq/BODY","DPC", i);
	                //判断上级机构的ID是否有值
	                try{
	                	dep.setSuperiorId(Long.valueOf(pareDepId));
	                }catch (Exception e) {
						EmpExecutionContext.error(e,"判断上级机构的ID是否有值出现异常！");
						dep.setSuperiorId(1L);
					}
                	dep.setCorpCode(corpCode);
                	dep.setDepState(1);
                	dep.setUpDepId(upDepId);
                	 //机构名称
	                dep.setDepName(depName);
               	    LfDep empDep = this.getDepByUpDepId(corpCode, upDepId);
	                if("1".equals(opt)){
	                	if(empDep == null){
		                	addDepList.add(dep);
		                	 //保存所有的同步机构
			                upDepMap.put(upDepId, dep);
	                	}
	                }else if("2".equals(opt)){
	                	 //删除	
	                	 if(empDep != null){
	 	                	 delDepList.add(empDep);
	                	 }
	                }else if("3".equals(opt)){
	                	//判断是否存在，不存在对应的机构  则新增
	                	 if(empDep != null){
	 	                	 updateDepList.add(dep);
	                	 }else{
	                		 addDepList.add(dep);
	                		 //保存所有的同步机构
	     	                upDepMap.put(upDepId, dep);
	                	 }
	                }
				}
				returnMsg = this.OperationDep(addDepList, updateDepList,delDepList,upDepMap);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"解析操作员机构的报文主体失败！");
			returnMsg = "fail";
		}	
		return returnMsg;
	}
	/**
	 *   对 操作员机构进行新增/修改/删除的操作
	 * @param addDepList
	 * @param updateDepList
	 * @return
	 */
	public String OperationDep(List<LfDep> addDepList,List<LfDep> updateDepList,List<LfDep> delDepList,LinkedHashMap<String, LfDep> upDepMap){
		//查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>(); 
    	LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
        //存机构的KEY VALUE值的MAP	KEY为用户平台的机构ID   VALUE对应的是EMP平台的机构
        LinkedHashMap<String, LfDep> depMap = new LinkedHashMap<String, LfDep>(); 
        //设置其KEY DEPID VALUE DEPPATH
        LinkedHashMap<Long, String> depPathMap = new LinkedHashMap<Long, String>(); 
        //获取连接
		Connection conn = null;
		String returnMsg = "fail";
		try{
			
 			//处理新增机构
			if(addDepList != null && addDepList.size()>0){
				//用户平台传过来的机构对象
				LfDep addDep = null;
				//初始化EMP的机构对象
				LfDep addEmpDep = null;
				//父级ID
				String parentId = "";
				//企业编码
				String corpCode = "";
				//机构与操作员的关联对象
				LfDomination dom = null;
				//批量处理机构与操作员关联表数据
  				List<LfDomination> dominationList = null;
				for(int i=0;i<addDepList.size();i++){
					//初始化
					addDep = null;
					corpCode = "";
					//获取其用户平台的机构对象
					addDep = addDepList.get(i);
					//机构等级
					//depLevel = String.valueOf(addDep.getDepLevel());
					parentId = String.valueOf(addDep.getSuperiorId());
				  	corpCode = addDep.getCorpCode();
				  	//顶级机构更新
				  	//如果父级机构ID=-1的话，那么则表示是顶级机构
				  	 if("-1".equals(parentId)){
				 	  	 //获取顶级机构的信息
				  		 conditionMap.clear();
				  		 conditionMap.put("depLevel", "1");
						 conditionMap.put("corpCode", corpCode);
						 List<LfDep> lfDeps = empDao.findListByCondition(LfDep.class, conditionMap, null);
	                     if(lfDeps != null && lfDeps.size()>0){
	                    	 //初始化
	                    	 addEmpDep = null;
	                    	 //获取顶级机构
	                    	 addEmpDep = lfDeps.get(0);
	                    	 //判断其用户统一平台的机构ID是否有值 ， 有则不更新，否更新
	                    	 String updepId = addEmpDep.getUpDepId();
	                    	 if(!UpWSTools.isNotNull(updepId)){
	                    		  addEmpDep.setUpDepId(addDep.getUpDepId());
	                    		  addEmpDep.setDepName(addDep.getDepName());
	                    		  empDao.update(addEmpDep);
	                    		  returnMsg = "success";
	                    		  depMap.put(addDep.getUpDepId(), addEmpDep);
	                    	 }
	                     }else{
	                    	 //增加一个超级机构
	                    	 try{
	                    		 addDep.setDepLevel(1);
	                    		 empDao.save(addDep);
	                    	 }catch (Exception e) {
								EmpExecutionContext.error(e,"增加超级机构失败！");
							}
	                    	
	                     }
				  	 }else{
				  		 //子级机构新增
				  		 //初始化EMP对应的父机构对象
				  		addEmpDep = null;
				  		//父机构ID
				  		Long pareDepId = null;
				  		//获取所对应的父机构对象	//先到MAP中查询    再到数据库中查询
				  		addEmpDep = this.getDepByayaDepId(depMap,corpCode,String.valueOf(addDep.getSuperiorId()));
                    	 if(addEmpDep != null){
                    		 pareDepId = addEmpDep.getDepId();
                    		 //把用户统一平台的机构对象进行填充值
                    		 //对应EMP的父机构ID
                    		 addDep.setSuperiorId(pareDepId);
                    		 //先初始化其路径
                    		 addDep.setDeppath(addEmpDep.getDeppath());
                    		 //获取其父机构下的所有机构的用户
                        	 List<LfSysuser> usersList = new DepSpecialDAO().findDomSysuserByDepID(String.valueOf(pareDepId), null);
                        	 conn = null;
                        	 conn = empTransDao.getConnection();
                        	//处理单个操作员机构
                  			try{
                  				 empTransDao.beginTransaction(conn);
                  				 Long depId = empTransDao.saveObjectReturnID(conn, addDep);
	                        	 addDep.setDepId(depId);
	                        	 //将新增的EMP的机构放入到MAP中保存
	                        	 depMap.put(addDep.getUpDepId(), addDep);
	                  			if(UpWSTools.isNotNull(addDep.getDeppath())){
	                  				addDep.setDeppath(addDep.getDeppath()+depId+"/");
	                  			}else{
	                  				addDep.setDeppath(depId+"/");
	                  			}
	                  			//增加机构等级
	                  			if(addEmpDep.getDepLevel() != null){
	                  				addDep.setDepLevel(addEmpDep.getDepLevel()+1);
	            				}
	                  			empTransDao.update(conn, addDep);
	                  			if(usersList != null && usersList.size()>0){
	                  				dominationList = null;
	                  				dominationList = new ArrayList<LfDomination>();
	                  				for (int j = 0; j < usersList.size(); j++) {
	                  					dom = new LfDomination();
	                      				dom.setDepId(addDep.getDepId());
	                      				dom.setUserId(usersList.get(j).getUserId());
	                      				dominationList.add(dom);
	                      			}
	                  				if(dominationList != null && dominationList.size()>0){
	                  					empTransDao.save(conn, dominationList, LfDomination.class);
	                  				}
	                  			}
	                  			empTransDao.commitTransaction(conn);
	                  			returnMsg = "success";
                  			}catch (Exception e) {
								empTransDao.rollBackTransaction(conn);
								EmpExecutionContext.error(e,"同步操作员机构进行新增的操作失败！");
							}finally{
								empTransDao.closeConnection(conn);
							}
                    	 }else{
                    		 //假如找不到父级机构， 当父机机构ID大于子级机构ID的时候
 	                			
 	                			
 	                			
 	                			
 	                		
                    		 
                    		 
                    		 
                    		 
                    		 
                    	 }
				  	 }
				  	 
				  	 
				  	 
				  	 
				  	 
				}
			}
			//处理修改机构
			if(updateDepList != null && updateDepList.size()>0){
				 conn = null;
            	 conn = empTransDao.getConnection();
            	 try{
            		empTransDao.beginTransaction(conn);
            		LfDep dep = null;
        			for(int i=0;i<updateDepList.size();i++){
        				dep = updateDepList.get(i);
        				//查询所对应的机构
        				LfDep empDep = this.getDepByUpDepId(dep.getCorpCode(), dep.getUpDepId());
	                	 if(empDep != null){
	                		 Long depId = empDep.getDepId();
	                		empDep.setDepName(dep.getDepName());
	                		//查询所对应的父机构
	                		LfDep pareDep = this.getDepByUpDepId(dep.getCorpCode(), String.valueOf(dep.getSuperiorId()));
	                		if(pareDep != null){
	                			if(pareDep.getDepId().equals(empDep.getSuperiorId())){
		                			//没有移动机构
	                				empDep.setDepName(dep.getDepName());
	                				empTransDao.update(conn, empDep);
		                		}else{
		                			//移动了机构
		                			//父级的路径
		                			String pareDepPath = pareDep.getDeppath();
		                			conditionMap.clear();
		   				  		    conditionMap.put("deppath&like", empDep.getDeppath());
		   						    conditionMap.put("corpCode", empDep.getCorpCode());
		   						    //显示正常的机构树
		   						    conditionMap.put("depState", "1");
		   						    orderMap.put("depLevel", StaticValue.ASC);
		   						    List<LfDep> moveDepList = empDao.findListBySymbolsCondition(LfDep.class, conditionMap, orderMap);
		   						    depPathMap.put(pareDep.getDepId(), pareDep.getDeppath());
		   						    for(int j = 0;j<moveDepList.size();j++){
		   						    	LfDep temp = moveDepList.get(j);
		   						    	String depPath = depPathMap.get(temp.getSuperiorId());
		   						    	String path = "";
		   						    	if(UpWSTools.isNotNull(depPath)){
		   						    		path = depPath + String.valueOf(depId)+"/";
		   						    	}else{
		   						    		path = pareDepPath + String.valueOf(depId)+"/";
		   						    	}
	   						    		temp.setDeppath(path);
	   						    		depPathMap.put(depId, path);
		   						    	empTransDao.update(conn, temp);
		   						    }
		                		}
	                		}
	                	 }
    				}
        			empTransDao.commitTransaction(conn);
        			returnMsg = "success";
            	 }catch (Exception e) {
 					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error(e,"同步修改机构失败！");
				}finally{
					empTransDao.closeConnection(conn);
				}
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~处理机构删除BEGIN
			if(delDepList != null && delDepList.size()>0){
				for(int j=delDepList.size();j>=1;j--){
					LfDep temp = delDepList.get(j-1);
					Long depId = temp.getDepId();
					String corpCode = temp.getCorpCode();
					conn = null;
					conn = empTransDao.getConnection();
					try{
						empTransDao.beginTransaction(conn);
						conditionMap.clear();
						conditionMap.put("depId", String.valueOf(depId));
						empTransDao.delete(conn, LfDomination.class, conditionMap);
						if(SystemGlobals.isDepBilling(corpCode)){			
							//对改机构的充值回收做处理
							conditionMap.clear();
							conditionMap.put("corpCode", corpCode);
							conditionMap.put("targetId", String.valueOf(depId));
							List<LfDepUserBalance> depUserBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
							if(depUserBalances != null && depUserBalances.size()>0){
								LfDepUserBalance sonBalance = depUserBalances.get(0);
								conditionMap.clear();
								conditionMap.put("corpCode", corpCode);
								conditionMap.put("targetId", String.valueOf(temp.getSuperiorId()));
								List<LfDepUserBalance> balances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
								if(balances!=null && balances.size()>0){
									LfDepUserBalance pareBalance = balances.get(0);
									pareBalance.setMmsBalance(pareBalance.getMmsBalance()+sonBalance.getMmsBalance());//彩信余额
									pareBalance.setMmsCount(pareBalance.getMmsCount()+sonBalance.getMmsCount());
									pareBalance.setSmsBalance(sonBalance.getSmsBalance()+pareBalance.getSmsBalance());//短信余额
									pareBalance.setSmsCount(sonBalance.getSmsCount()+pareBalance.getSmsCount());
									empTransDao.update(conn, pareBalance);
									empTransDao.delete(conn, LfDepUserBalance.class, String.valueOf(sonBalance.getBlId()));
								}
							}
						}
						//设置为隐藏
						temp.setDepState(2);
						empTransDao.update(conn, temp);
						empTransDao.commitTransaction(conn);
					}catch (Exception e) {
						empTransDao.rollBackTransaction(conn);
						EmpExecutionContext.error(e,"同步机构删除失败！");
					}finally{
						empTransDao.closeConnection(conn);
					}
				}
			}
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~删除END
		}catch (Exception e) {
			returnMsg = "fail";
			EmpExecutionContext.error(e,"同步操作员机构进行新增/修改/删除的操作出现异常！");
		}
		return returnMsg;
	}
	
	
	/**
	 *   获取同步机构ID在EMP所对应的机构对象
	 * @param map	存放LFDEP对象LIST
	 * @param cpc	企业编码
	 * @param dpc	同步机构ID
	 * @return
	 */
	public LfDep getDepByayaDepId(LinkedHashMap<String, LfDep> map,String cpc,String dpc){
		LfDep temp = null;
		try{
	    	 if(map.size()>0){
	    		 temp = map.get(dpc);
	    	 }
	    	 if(temp == null){
	 			  LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	    		  List<LfDep> deps = null;
	              conditionMap.put("upDepId", dpc);
	              conditionMap.put("corpCode", cpc);
	              deps = empDao.findListByCondition(LfDep.class, conditionMap, null);
	             if(deps != null && deps.size()>0){
	             	temp = deps.get(0);
	             	map.put(dpc, temp);
	             }
	    	 }
		}catch (Exception e) {
			EmpExecutionContext.error(e," 获取同步机构ID在EMP所对应的机构对象出现异常！");
			temp = null;
		}
		return temp;
	}
	/**
	 *   通过用户统一平台的机构ID以及企业编码查询对应EMP中数据
	 * @param cpc	企业编码
	 * @param dpc	用户统一平台机构ID
	 * @return
	 */
	public LfDep getDepByUpDepId(String cpc,String dpc){
		LfDep temp = null;
		try{
			  LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
    		  List<LfDep> deps = null;
              conditionMap.put("upDepId", dpc);
              conditionMap.put("corpCode", cpc);
              deps = empDao.findListByCondition(LfDep.class, conditionMap, null);
             if(deps != null && deps.size()>0){
             	temp = deps.get(0);
             }
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取通过用户统一平台的机构ID以及企业编码查询对应EMP中数据失败！");
			temp = null;
		}
		return temp;
	}

	/**
	 *   新增一个机构
	 * @param depMap
	 * @param sonDep
	 * @return
	 */
	public boolean addDepByUp( LinkedHashMap<String, LfDep> depMap,LfDep sonDep){
			boolean isFlag = false;
			try{
				//机构与操作员的关联对象
				LfDomination dom = null;
				//批量处理机构与操作员关联表数据
				List<LfDomination> dominationList = null;
				//子级机构新增
		  		 //初始化EMP对应的父机构对象
				LfDep pareDep = null;
		  		//父机构ID
		  		Long pareDepId = null;
		  		//获取所对应的父机构对象	//先到MAP中查询    再到数据库中查询
		  		pareDep = this.getDepByayaDepId(depMap,sonDep.getCorpCode(),String.valueOf(sonDep.getSuperiorId()));
		  		if(pareDep != null){
	       		 pareDepId = pareDep.getDepId();
	       		 //把用户统一平台的机构对象进行填充值
	       		 //对应EMP的父机构ID
	       		sonDep.setSuperiorId(pareDepId);
	       		 //先初始化其路径
	       		sonDep.setDeppath(pareDep.getDeppath());
	       		 //获取其父机构下的所有机构的用户
	           	 List<LfSysuser> usersList = new DepSpecialDAO().findDomSysuserByDepID(String.valueOf(pareDepId), null);
	           	 Connection conn = empTransDao.getConnection();
	           	//处理单个操作员机构
	     			try{
	     				 empTransDao.beginTransaction(conn);
	     				 Long depId = empTransDao.saveObjectReturnID(conn, sonDep);
	     				sonDep.setDepId(depId);
	     				//将新增的EMP的机构放入到MAP中保存
	     				depMap.put(sonDep.getUpDepId(), sonDep);
	         			if(UpWSTools.isNotNull(sonDep.getDeppath())){
	         				sonDep.setDeppath(sonDep.getDeppath()+depId+"/");
	         			}else{
	         				sonDep.setDeppath(depId+"/");
	         			}
	         			//增加机构等级
	         			if(pareDep.getDepLevel() != null){
	         				sonDep.setDepLevel(pareDep.getDepLevel()+1);
	         			}
	         			empTransDao.update(conn, sonDep);
	         			if(usersList != null && usersList.size()>0){
	         				dominationList = null;
	         				dominationList = new ArrayList<LfDomination>();
	         				for (int j = 0; j < usersList.size(); j++) {
	         					dom = new LfDomination();
	             				dom.setDepId(sonDep.getDepId());
	             				dom.setUserId(usersList.get(j).getUserId());
	             				dominationList.add(dom);
	             			}
	         				if(dominationList != null && dominationList.size()>0){
	         					empTransDao.save(conn, dominationList, LfDomination.class);
	         				}
	         			}
	         			empTransDao.commitTransaction(conn);
	         			isFlag = true;
	     			}catch (Exception e) {
						empTransDao.rollBackTransaction(conn);
						isFlag = false;
						EmpExecutionContext.error(e,"同步新增机构出现失败！");
					}finally{
						empTransDao.closeConnection(conn);
					}
	       	 }
		}catch (Exception e) {
			EmpExecutionContext.error(e,"同步新增机构出现异常！");
		}
		return isFlag;
	}

	
	
	/**
	 *    请求数据
	 * @param type
	 * @param loginName
	 * @return
	 */
	public void requestUPWS(){
			String webserviceIp = "";
			String empCert = "";
			String empApId = "";
			boolean isGetValue = false;
			try{
				//服务URL
				webserviceIp = SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_IP);
				//密钥
				empCert =  SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_CERT);
				//系统标识
				empApId =  SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_APID);
				if(UpWSTools.isNotNull(webserviceIp) && UpWSTools.isNotNull(empCert) && UpWSTools.isNotNull(empApId)){
					isGetValue = true;
				}
			}catch (Exception e) {
				isGetValue =false;
				EmpExecutionContext.error(e,"请求 同步配置出现异常！");
			}
			//如果获取到EMP的配置文件数据的话
			if(isGetValue){
				XmlParser xmlParser = new XmlParser();
				//~~~~~~~~~~~~~~~~~~封装请求XML 并且调用服务端接口
				//请求报文
				RequestStr requestStr = new RequestStr();
				requestStr.setBcode(FUNCTION_SYN1001);
				requestStr.setApid(empApId);
				requestStr.setCert(empCert);
				requestStr.setTpe("2");
				
				//返回报文
				ReturnReq returnReq = new ReturnReq();
				returnReq.setBcode(FUNCTION_SYN1002);
				returnReq.setApid(empApId);
				returnReq.setCert(empCert);
				returnReq.setHaveSer(false);
				returnReq.setSta("2");
				returnReq.setErr("失败");
				returnReq.setResultMsg("失败");
				returnReq.setResultCode("103");
				
				//定义服务端返回的同步数据XML格式
				String xml = "";
				String inputXMLStr = upWsTools.returnRequestStr(requestStr);
				String isRequestFlag = "1"; 
				
				//判断需要再发送请求数据 处理机构
				while (UpWSTools.equals(isRequestFlag, "1")) {
					//调用webservice接口，请求数据获取服务端返回的数据
					try{
						xml = UpWSTools.SYNWS(inputXMLStr, webserviceIp);
					}catch (Exception e) {
						xml = "";
						isRequestFlag = "3";
						EmpExecutionContext.error(e,"调用webservice接口出现异常！");
					}
					//处理返回来的数据
					if(UpWSTools.isNotNull(xml)){
						 try{
							Document document = xmlParser.getDocument(xml);
							//String bCode = xmlParser.parseXMLStr(document, "BCode");
							String apid = xmlParser.parseXMLStr(document, "ApId");
							String cert = xmlParser.parseXMLStr(document, "Cert");
							String cnxt = xmlParser.parseXMLStr(document, "Cnxt");
							String resultCode = xmlParser.parseXMLStr(document, "RESULTCODE");
							//String resultMsg = xmlParser.parseXMLStr(document, "RESULTMSG");
							String isRightValue = upWsTools.isRightValue(empCert, empApId, cert, apid, resultCode,returnReq);
							String serialNum = "";
							boolean isFlag = false;
							if("1".equals(isRightValue)){
							  	String toDecodeStr = "";
								toDecodeStr = UpWSTools.toDecode(cnxt);
								if(UpWSTools.isNotNull(toDecodeStr)){
									document = null;
									document=xmlParser.getDocument("<Cnxt>"+toDecodeStr+"</Cnxt>");
									//操作数据量
									String number=xmlParser.parseXMLStr(document, "NUM");
									//流水号
									serialNum=xmlParser.parseXMLStr(document, "SER");
									//1表示还有数据   2表示没有数据
									isRequestFlag = xmlParser.parseXMLStr(document, "NXT");
									//这里处理数据
									Integer count = Integer.valueOf(number);
									//这里处理新增  修改 删除  机构  临时表
									isFlag = this.getDepXmlInfomation(xmlParser, document, count);
								}else{
									isRequestFlag = "2";
								}
							}
							if(isFlag){
								returnReq.setResultCode("0");
								returnReq.setResultMsg("成功");
								returnReq.setSta("1");
								returnReq.setErr("成功");
								returnReq.setSer(serialNum);
								returnReq.setHaveSer(true);
							}
							//处理完数据库操作之后，返回报文
							String returnRespon = upWsTools.returnReqStr(returnReq);
							try{
								UpWSTools.SYNWS(returnRespon, webserviceIp);
								//完整的一次请求成功操作
							}catch (Exception e) {
								//再次请求失败	发送回执
								isRequestFlag = "3";
								EmpExecutionContext.error(e,"同步请求失败！");
							}
						 }catch (Exception e) {
							isRequestFlag = "3";
							EmpExecutionContext.error(e,"同步操作员回执出现异常！");
						}
					}else{
						isRequestFlag = "2";
					}
				}
				//~~~~~~~~~~~~~~END
				
				//这里请求操作员BEGING接口 BEGIN
				isRequestFlag = "1";
				requestStr.setTpe("1");
				xml = "";
				inputXMLStr = "";
				inputXMLStr = upWsTools.returnRequestStr(requestStr);
				while (UpWSTools.equals(isRequestFlag, "1")) {
					//调用webservice接口，请求数据获取服务端返回的数据
					try{
						xml = UpWSTools.SYNWS(inputXMLStr, webserviceIp);
					}catch (Exception e) {
						isRequestFlag = "3";
						xml = "";
						EmpExecutionContext.error(e,"调用webservice接口出现异常！");
					}
					//处理返回来的数据
					if(UpWSTools.isNotNull(xml)){
						 try{
							Document document = xmlParser.getDocument(xml);
							//String bCode = xmlParser.parseXMLStr(document, "BCode");
							String apid = xmlParser.parseXMLStr(document, "ApId");
							String cert = xmlParser.parseXMLStr(document, "Cert");
							String cnxt = xmlParser.parseXMLStr(document, "Cnxt");
							String resultCode = xmlParser.parseXMLStr(document, "RESULTCODE");
							//String resultMsg = xmlParser.parseXMLStr(document, "RESULTMSG");
							String isRightValue = upWsTools.isRightValue(empCert, empApId, cert, apid, resultCode,returnReq);
							String serialNum = "";
							boolean isFlag = false;
							if("1".equals(isRightValue)){
							  	String toDecodeStr = "";
								toDecodeStr = UpWSTools.toDecode(cnxt);
								if(UpWSTools.isNotNull(toDecodeStr)){
									document = null;
									document=xmlParser.getDocument("<Cnxt>"+toDecodeStr+"</Cnxt>");
									//操作数据量
									String number=xmlParser.parseXMLStr(document, "NUM");
									//流水号
									serialNum=xmlParser.parseXMLStr(document, "SER");
									//1表示还有数据   2表示没有数据
									isRequestFlag = xmlParser.parseXMLStr(document, "NXT");
									//这里处理数据
									Integer count = Integer.valueOf(number);
									//这里处理新增  修改 删除  操作员  临时表
									isFlag = this.getUserXmlInfomation(xmlParser, document, count);
								}else{
									isRequestFlag = "2";
								}
							}
							if(isFlag){
								returnReq.setResultCode("0");
								returnReq.setResultMsg("成功");
								returnReq.setSta("1");
								returnReq.setErr("成功");
								returnReq.setSer(serialNum);
								returnReq.setHaveSer(true);
							}
							//处理完数据库操作之后，返回报文
							String returnRespon = upWsTools.returnReqStr(returnReq);
							try{
								UpWSTools.SYNWS(returnRespon, webserviceIp);
								//完整的一次请求成功操作
							}catch (Exception e) {
								//再次请求失败	发送回执
								isRequestFlag = "3";
								EmpExecutionContext.error(e,"同步接口请求失败！");
							}
							
						 }catch (Exception e) {
							EmpExecutionContext.error(e,"同步接口回执失败！");
						}
					}else{
						isRequestFlag = "2";
					}
				}
				
			
			}
	}
	
	/**
	 *  处理机构操作到临时表中
	 * @param xmlParser
	 * @param document
	 * @param count
	 * @return
	 */
	public boolean getDepXmlInfomation(XmlParser xmlParser,Document document,Integer count){
		boolean isFlag = false;
		try{
			LfUpDep upDep = null;
			List<LfUpDep> upDepList = null;
			LinkedHashMap<Long, LfUpDep> keyValueMap = new LinkedHashMap<Long, LfUpDep>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", StaticValue.UPWS_CORPCODE);
			upDepList = empDao.findListByCondition(LfUpDep.class, conditionMap, null);
			if(upDepList != null && upDepList.size() > 0){
				for(int i=0;i<upDepList.size();i++){
					upDep = null;
					upDep = upDepList.get(i);
					keyValueMap.put(upDep.getDepCode(), upDep);
				}
			}
			List<LfUpDep> addDeps = new ArrayList<LfUpDep>();
			List<LfUpDep> updateDeps = new ArrayList<LfUpDep>();
			for(int i=0;i<count;i++){
				try{
					//部门编号
	                String upDepId = xmlParser.parseXMLStr(document, "DepReq/BODY","DCD", i);
	                //操作类型
					String opt = xmlParser.parseXMLStr(document, "DepReq/BODY","OPT", i);
				    //企业代码
				    String corpCode = xmlParser.parseXMLStr(document, "DepReq/BODY","CRD", i);
	                String depName = xmlParser.parseXMLStr(document, "DepReq/BODY","DNM", i);
	                String utm = xmlParser.parseXMLStr(document, "DepReq/BODY","UTM", i);
	            	 //机构级次		无用
				    String depLevel = xmlParser.parseXMLStr(document, "DepReq/BODY","DLV", i);
	                //上级机构代码
	                String pareDepId = xmlParser.parseXMLStr(document, "DepReq/BODY","DPC", i);
	                //机构简称
	                String dsn = xmlParser.parseXMLStr(document, "DepReq/BODY","DSN", i);
	                upDep = null;
	                boolean isValue = false;
					if(keyValueMap != null && keyValueMap.size()>0){
						upDep = keyValueMap.get(Long.parseLong(upDepId));
					}
					if(upDep == null){
						upDep = new LfUpDep();
						upDep.setDepCode(Long.valueOf(upDepId));
						isValue = true;
					}
					upDep.setIsUpdate("1");
					upDep.setOptType(opt);
					//如果不是删除机构操作。那么值全部填充，删除的时候只传OPT以及机构ID过来
					if(!"3".equals(opt)){
					     upDep.setDepName(depName);
			             upDep.setCorpCode(corpCode);
			             upDep.setDepShortName(dsn);
			             upDep.setDepPcode(Long.valueOf(pareDepId));
					}
	                if(isValue){
	                //新增操作
	                	addDeps.add(upDep);
	                }else{
	                //修改操作
	                	updateDeps.add(upDep);
	                }
				}catch (Exception e) {
					EmpExecutionContext.error(e,"处理机构操作出现异常！");
				}
			}
			Connection conn = empTransDao.getConnection();
			try{
				empTransDao.beginTransaction(conn);
				if(addDeps != null && addDeps.size()>0){
					empTransDao.save(conn, addDeps, LfUpDep.class);
				}
				if(updateDeps != null && updateDeps.size()>0){
					for(int i=0;i<updateDeps.size();i++){
						upDep = null;
						upDep = updateDeps.get(i);
						empTransDao.update(conn, upDep);
					}
				}
				empTransDao.commitTransaction(conn);
				isFlag = true;
			}catch (Exception e) {
				isFlag = false;
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"处理机构操作出现异常！");
			}finally{
				empTransDao.closeConnection(conn);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"处理机构操作到临时表出现异常！");
		}
		return isFlag;
	}
	
	
	
	
	
	/**
	 *  处理操作员操作到临时表中
	 * @param xmlParser
	 * @param document
	 * @param count
	 * @return
	 */
	public boolean getUserXmlInfomation(XmlParser xmlParser,Document document,Integer count){
		boolean isFlag = false;
		try{
			LfUpUserInfo upUser = null;
			List<LfUpUserInfo> upUserList = null;
			LinkedHashMap<String, LfUpUserInfo> keyValueMap = new LinkedHashMap<String, LfUpUserInfo>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", StaticValue.UPWS_CORPCODE);
			upUserList = empDao.findListByCondition(LfUpUserInfo.class, conditionMap, null);

			if(upUserList != null && upUserList.size()>0){
				for(int i=0;i<upUserList.size();i++){
					upUser = null;
					upUser = upUserList.get(i);
					keyValueMap.put(upUser.getGuid(), upUser);
				}
			}
			List<LfUpUserInfo> addUsers = new ArrayList<LfUpUserInfo>();
			List<LfUpUserInfo> updateUsers = new ArrayList<LfUpUserInfo>();
			for(int i=0;i<count;i++){
				   String opt = xmlParser.parseXMLStr(document, "UReq/BODY","OPT", i);
				   //部门编号
				   String dpc = xmlParser.parseXMLStr(document, "UReq/BODY","DPC", i);
				   //企业编码
				   String cpc = xmlParser.parseXMLStr(document, "UReq/BODY","CPC", i);
				   //同步平台用户GUID
				   String upGuId = xmlParser.parseXMLStr(document, "UReq/BODY","UCD", i);
				   String name = xmlParser.parseXMLStr(document, "UReq/BODY","UNM", i);
				   String sex = xmlParser.parseXMLStr(document, "UReq/BODY","FEX", i);
				   String tel = xmlParser.parseXMLStr(document, "UReq/BODY","TEL", i);
				   String qq = xmlParser.parseXMLStr(document, "UReq/BODY","QQ", i);
				   String email = xmlParser.parseXMLStr(document, "UReq/BODY","EML", i);
				   String msn = xmlParser.parseXMLStr(document, "UReq/BODY","MSN", i);
				   //用户名称
				   String userName = xmlParser.parseXMLStr(document, "UReq/BODY","LUM", i);
				   String password = xmlParser.parseXMLStr(document, "UReq/BODY","PWD", i);
				   String moblie = xmlParser.parseXMLStr(document, "UReq/BODY","MBL", i);
				   String userType = xmlParser.parseXMLStr(document, "UReq/BODY","UTP", i);
				   //状态位
				   String userstate = xmlParser.parseXMLStr(document, "UReq/BODY","STA", i);
				   //生日
				   String bir = xmlParser.parseXMLStr(document, "UReq/BODY","BIR", i);
				
				    upUser = null;
	                boolean isValue = false;
					if(keyValueMap != null && keyValueMap.size()>0){
						upUser = keyValueMap.get(upGuId);
					}
					if(upUser == null){
						upUser = new LfUpUserInfo();
						upUser.setGuid(upGuId);
						isValue = true;
					}
					//如果是新增和修改加值
					upUser.setIsUpdate("1");
					upUser.setOptType(opt);
					if("1".equals(opt) || "2".equals(opt)){
						upUser.setBirthday(bir);
						upUser.setCorpCode(cpc);
						try{
							upUser.setDepCode(Long.valueOf(dpc));
						}catch (Exception e) {
							EmpExecutionContext.error(e, "字符串转换异常！");
						}
						upUser.setEmail(email);
						upUser.setMobile(moblie);
						upUser.setMsn(msn);
						upUser.setName(name);
						upUser.setPassword(password);
						upUser.setQq(qq);
						upUser.setSex(sex);
						upUser.setTelephone(tel);
						upUser.setUserName(userName);
						upUser.setUserState(userstate);
						upUser.setUserType(userType);
						upUser.setStatus("1");
					}
					if(isValue){
	                //新增操作
						  addUsers.add(upUser);
	                }else{
	                //修改操作
	                	 updateUsers.add(upUser);
	                }
				
			}
			Connection conn = empTransDao.getConnection();
			try{
				empTransDao.beginTransaction(conn);
				if(addUsers != null && addUsers.size()>0){
					empTransDao.save(conn, addUsers, LfUpUserInfo.class);
				}
				if(updateUsers != null && updateUsers.size()>0){
					for(int i=0;i<updateUsers.size();i++){
						upUser = null;
						upUser = updateUsers.get(i);
						empTransDao.update(conn, upUser);
					}
				}
				empTransDao.commitTransaction(conn);
				isFlag = true;
			}catch (Exception e) {
				isFlag = false;
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"同步新增操作员失败！");
			}finally{
				empTransDao.closeConnection(conn);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"同步新增操作员出现异常！");
		}
		return isFlag;
	}
	
	
	
	
	
	public void EmployeeSynchronization() throws Exception{
		
		String corpCode = StaticValue.UPWS_CORPCODE;
		// 查询EMP所有正常的操作员
		List<LfSysuser> lfSysUserList = null;
		// 查询EMP所有员工机构树
		List<LfEmployeeDep> lfEmployeeDepList = null;
		// 查询EMP所有员工
		List<LfEmployee> lfEmployeeList = null;
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//排序条件
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		//更新内容
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		//存放KEY 统一平台GUID  VALUE 为EMP中操作员
		LinkedHashMap<String, LfSysuser> lfSysuserMap = new LinkedHashMap<String, LfSysuser>();
		LinkedHashMap<String, LfEmployeeDep> lfEmployeeDepMap = new LinkedHashMap<String, LfEmployeeDep>();
		LinkedHashMap<String, LfEmployee> lfEmployeeMap = new LinkedHashMap<String, LfEmployee>();


		conditionMap.put("corpCode", corpCode);
		conditionMap.put("userState", "1");
		lfSysUserList = empDao.findListByCondition(LfSysuser.class, conditionMap,null);
		
		// 将EMP所有正常的操作员放到一个map中
		LfSysuser lfsysuser = null;
		if (lfSysUserList != null && lfSysUserList.size()>0) {
			Long userGuid = null;
			for (int i = 0; i < lfSysUserList.size(); i++) {
				lfsysuser = null;
				userGuid = null;
				lfsysuser = lfSysUserList.get(i);
				userGuid = lfsysuser.getUpGuId();
				if (UpWSTools.isNotNull(userGuid)) {
					lfSysuserMap.put(String.valueOf(userGuid),lfsysuser);
				}
			}
		}
		//将EMP所有的员工机构放到一个MAP中
		LfEmployeeDep employeeDep = null;
		conditionMap.clear();
		conditionMap.put("corpCode", corpCode);
		lfEmployeeDepList = empDao.findListByCondition(LfEmployeeDep.class, conditionMap,null);
		if(lfEmployeeDepList != null && lfEmployeeDepList.size()>0){
			String upDepId = "";
			for (int i = 0; i < lfEmployeeDepList.size(); i++) {
				employeeDep = null;
				upDepId = "";
				employeeDep = lfEmployeeDepList.get(i);
				upDepId = employeeDep.getUpdepid();
				if (UpWSTools.isNotNull(upDepId)) {
					lfEmployeeDepMap.put(upDepId,employeeDep);
				}
			}
		}
		
		//将EMP所有的员工放到一个MAP中
		LfEmployee employee = null;
		conditionMap.clear();
		conditionMap.put("corpCode", corpCode);
		lfEmployeeList = empDao.findListByCondition(LfEmployee.class, conditionMap,null);
		if(lfEmployeeList != null && lfEmployeeList.size()>0){
			Long upGuid = null;
			for (int i = 0; i < lfEmployeeList.size(); i++) {
				employee = null;
				upGuid = null;
				employee = lfEmployeeList.get(i);
				upGuid = employee.getUpGuId();
				if (UpWSTools.isNotNull(upGuid)) {
					lfEmployeeMap.put(String.valueOf(upGuid),employee);
				}
			}
		}
		
		
		// 查询所有新增/修改/删除的临时表操作员
		List<LfUpUserInfo> upEmployeeList = null;
		conditionMap.clear();
		orderMap.clear();
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("isUpdate", "2");
		orderMap.put("guid", StaticValue.ASC);
		upEmployeeList = empDao.findListByCondition(LfUpUserInfo.class,
				conditionMap, orderMap);
		
		
		// 将需要新增和需要修改的分别放在两个List中
		LfUpUserInfo upEmployee = null;
		// 需要新增的
		List<LfUpUserInfo> addEmployeeList = new ArrayList<LfUpUserInfo>();
		// 需要修改的
		List<LfUpUserInfo> updateEmployeeList = new ArrayList<LfUpUserInfo>();
		// 需要删除/注销的
		List<LfUpUserInfo> deleteEmployeeList = new ArrayList<LfUpUserInfo>();
		
	
		if (upEmployeeList != null && upEmployeeList.size() > 0) {
			String optType = "";
			for (int i = 0; i < upEmployeeList.size(); i++) {
				employee = null;
				optType = "";
				upEmployee = upEmployeeList.get(i);
				optType = upEmployee.getOptType();
				if ("2".equals(optType) || "4".equals(optType)) {
					deleteEmployeeList.add(upEmployee);
				} else if ("3".equals(optType)|| "1".equals(optType)) {
					employee = lfEmployeeMap.get(upEmployee.getGuid());
					if (employee == null) {
						addEmployeeList.add(upEmployee);
					} else {
						updateEmployeeList.add(upEmployee);
					}
				}
			}
		} 
		
		Connection conn = null;
		employee = null;
		//新增员工
		if (addEmployeeList != null && addEmployeeList.size() > 0) {
			conn = null;
			conn = empTransDao.getConnection();
			try {
				// 开启事务
				empTransDao.beginTransaction(conn);
				for (int i = 0; i < addEmployeeList.size(); i++) {
						try{
							upEmployee = addEmployeeList.get(i);
							if("admin".equals(upEmployee.getUserName())){
								continue;
							}
							// 将临时表的记录保存在LfSysuser对象中
							employee = new LfEmployee();
							LfSysuser user = lfSysuserMap.get(upEmployee.getGuid());
							if(user != null){
								employee.setGuId(user.getGuId());
							}else{
								Long guid= globalBiz.getValueByKey("guid", 1L);
								employee.setGuId(guid);
							}
							try{
								employee.setUpGuId(Long.valueOf(upEmployee.getGuid()));
							}catch (Exception e) {
								// TODO: handle exception
								EmpExecutionContext.error(e, "字符串转换异常！");
							}
							employee.setEmployeeNo(upEmployee.getUserName().toUpperCase());
							conditionMap.clear();
							conditionMap.put("upDepId", String.valueOf(upEmployee.getDepCode()));
							try{
								LfEmployeeDep dep = lfEmployeeDepMap.get(String.valueOf(upEmployee.getDepCode()));
								if(dep != null){
									employee.setDepId(dep.getDepId());
								}
						 	}catch (Exception e) {
								// TODO: handle exception
						 		EmpExecutionContext.error(e, "字符串转换异常！");
							}
							if(UpWSTools.isNotNull(upEmployee.getBirthday())){
								 try{
									Date d = sdformat.parse(upEmployee.getBirthday());
									Timestamp ts = new Timestamp(d.getTime()); 
									employee.setBirthday(ts);
								 }catch (Exception e) {
									// TODO: handle exception
									EmpExecutionContext.error(e, "时间转换异常！"); 
								}
							}
							employee.setSex(Integer.parseInt(upEmployee.getSex()));
							employee.setMobile(upEmployee.getMobile());
							employee.setQq(upEmployee.getQq());
							employee.setMsn(upEmployee.getMsn());
							employee.setEmail(upEmployee.getEmail());
							employee.setOph(upEmployee.getTelephone());
							employee.setCorpCode(upEmployee.getCorpCode());
							employee.setName(upEmployee.getName());
							employee.setIsOperator(1);
							empTransDao.save(conn, employee);
							upEmployee.setIsUpdate("3");
							empTransDao.update(conn, upEmployee);
						}catch (Exception e) {
							// TODO: handle exception
							EmpExecutionContext.error(e,"更新员工信息发生异常！");
						}
					}
					empTransDao.commitTransaction(conn);
				} catch (Exception e) {
					// TODO: handle exception
					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error(e, "同步员工信息发生异常！");
				} finally {
					empTransDao.closeConnection(conn);
				}
		}
		
		
		
		// 修改员工
		if (updateEmployeeList != null && updateEmployeeList.size() > 0) {
			conn = null;
			conn = empTransDao.getConnection();
			// 开启事务
			try {
				empTransDao.beginTransaction(conn);
			for (int i = 0; i < updateEmployeeList.size(); i++) {
					try{
						employee = null;
						upEmployee = updateEmployeeList.get(i);
						employee = lfEmployeeMap.get(upEmployee.getGuid());
						if(employee != null){
							conditionMap.clear();
							conditionMap.put("upDepId", String.valueOf(upEmployee.getDepCode()));
							try{
								LfEmployeeDep dep = lfEmployeeDepMap.get(upEmployee.getDepCode()+"");
								if(dep != null){
									employee.setDepId(dep.getDepId());
								}
						 	}catch (Exception e) {
						 		 EmpExecutionContext.error(e, "获得机构/部门相关信息异常！");
							}
							if(UpWSTools.isNotNull(upEmployee.getBirthday())){
								 try{
									Date d = sdformat.parse(upEmployee.getBirthday());
									Timestamp ts = new Timestamp(d.getTime()); 
									employee.setBirthday(ts);
								 }catch (Exception e) {
									// TODO: handle exception
									 EmpExecutionContext.error(e, "时间转换异常！");
								}
							}
							employee.setSex(Integer.parseInt(upEmployee.getSex()));
							employee.setMobile(upEmployee.getMobile());
							employee.setQq(upEmployee.getQq());
							employee.setMsn(upEmployee.getMsn());
							employee.setEmail(upEmployee.getEmail());
							employee.setOph(upEmployee.getTelephone());
							employee.setName(upEmployee.getName());
							empTransDao.update(conn, employee);
							upEmployee.setIsUpdate("3");
							empTransDao.update(conn, upEmployee);
						}
						
					}catch (Exception e1) {
						EmpExecutionContext.error(e1,"同步处理员工信息失败！");
					}
				}
				empTransDao.commitTransaction(conn);
			} catch (Exception e) {
				// TODO: handle exception
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"同步处理员工信息出现异常！");
			} finally {
				empTransDao.closeConnection(conn);
			}

		}
		
		
		
		// 删除员工
		if (deleteEmployeeList != null && deleteEmployeeList.size() > 0) {
			conn = null;
			conn = empTransDao.getConnection();
			// 开启事务
			try {
				empTransDao.beginTransaction(conn);
				String guIds = "";
				String ids = "";
				for (int i = 0; i < deleteEmployeeList.size(); i++) {
					try{
						employee = null;
						upEmployee = deleteEmployeeList.get(i);
						employee = lfEmployeeMap.get(upEmployee.getGuid());
						if(employee != null){
							guIds += employee.getGuId()+",";
							ids += upEmployee.getGuid()+",";
						}
					/*	upEmployee.setIsUpdate("3");
						empTransDao.update(conn, upEmployee);*/
					}catch (Exception e1) {
						EmpExecutionContext.error(e1,"获取员工ID出现异常 ！");
					}
				}
				if(null != guIds && guIds.contains(",") && !"".equals(guIds))
				{
					guIds = guIds.substring(0,guIds.lastIndexOf(","));
					//删除员工
					conditionMap.clear();
					conditionMap.put("guId", guIds);
					empTransDao.delete(conn, LfEmployee.class, conditionMap);
					//更新操作员表中对用的信息
					objectMap.clear();
					objectMap.put("userType", "0");
					conditionMap.clear();
					conditionMap.put("guId", guIds);
					empTransDao.update(conn, LfSysuser.class, objectMap, conditionMap);
					//删除群组关联表
					conditionMap.clear();
					conditionMap.put("l2gType", "0");
					conditionMap.put("guId", guIds);
					empTransDao.delete(conn, LfList2gro.class, conditionMap);
					//删除生日祝福设置
					conditionMap.clear();
					conditionMap.put("type", "1");
					conditionMap.put("memberId", guIds);
					conditionMap.put("corpCode", corpCode);
					empTransDao.delete(conn, LfBirthdayMember.class, conditionMap);
					conditionMap.clear();
					objectMap.clear();
					conditionMap.put("corpCode", corpCode);
					conditionMap.put("optType", "2,4");
					conditionMap.put("guid", ids);
					objectMap.put("isUpdate", "3");
					empTransDao.update(conn, LfUpUserInfo.class, objectMap, conditionMap);
					
				}
				empTransDao.commitTransaction(conn);
			} catch (Exception e) {
				// TODO: handle exception
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"同步员工操作出现异常！");
			} finally {
				empTransDao.closeConnection(conn);
			}

		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
