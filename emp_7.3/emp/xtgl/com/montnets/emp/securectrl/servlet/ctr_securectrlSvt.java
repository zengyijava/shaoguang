package com.montnets.emp.securectrl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.securectrl.LfMacIp;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.securectrl.biz.LoginSafeBiz;
import com.montnets.emp.securectrl.vo.LfMacIpVo;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * 高级安全设置
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 */
@SuppressWarnings("serial")
public class ctr_securectrlSvt extends BaseServlet {

	private final LoginSafeBiz loginSafeBiz = new LoginSafeBiz();
	private static final String PATH = "/xtgl/securectrl";
	private final BaseBiz baseBiz=new BaseBiz();
	
	public void find(HttpServletRequest request, HttpServletResponse response) {
		Long guid = null;
		try {
			//操作跳转
			String isOperateRetrun = (String) request.getAttribute("isOperateRetrun");
			if(isOperateRetrun==null)
			{
				isOperateRetrun = request.getParameter("isOperateRetrun");
				request.setAttribute("isOperateRetrun", isOperateRetrun);
			}
			if("true".equals(isOperateRetrun))
			{
				//操作跳转去除上次查询的条件
				request.setAttribute("conditionMap", request.getSession(false).getAttribute("secureConditionMap"));
			}
			
			//获取登录sysuser
			LfSysuser sysuser =loginSafeBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("机构管理,find方法session中获取当前登录对象出现异常");
				return;
			}
			guid=sysuser.getGuId();
			//判断企业编码获取
			if(guid==null){
				EmpExecutionContext.error("机构管理,find方法session中获取企业编码出现异常");
				return;
			}
			
			int permissionType = sysuser.getPermissionType();
			//判断当前用户是否有机构权限
			if(permissionType != 2){
				//没有机构权限，则跳转到提示页面
				request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
						request, response);
				return;
			}
			request.getRequestDispatcher(PATH+"/ctr_securectrl.jsp")
					.forward(request, response);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"高级安全设置页面跳转出现异常！");
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(PATH
								+ "/ctr_securectrl.jsp").forward(request,
						response);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"高级安全设置页面跳转出现异常！");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void getTable(HttpServletRequest request,
			HttpServletResponse response) {
		Long guid = null;
		String depId = request.getParameter("depId");
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		//操作跳转
		String isOperateRetrun = request.getParameter("isOperateRetrun");
		//条件查询
		Map<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//是否第一次打开
			PageInfo pageInfo=new PageInfo();
			//获取登录sysuser
			LfSysuser sysuser =loginSafeBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("高级安全设置,find方法session中获取当前登录对象出现异常");
				return;
			}
			String corpcode=sysuser.getCorpCode();
			if(corpcode==null||"".equals(corpcode)){
				EmpExecutionContext.error("高级安全设置,find方法session中获取企业编码出现异常");
				return;
			}
			guid=sysuser.getGuId();
			//判断企业编码获取
			if(guid==null){
				EmpExecutionContext.error("高级安全设置,find方法session中获取GUID出现异常");
				return;
			}
			if("true".equals(isOperateRetrun))
			{
				//操作跳转取出最后一次分页
				pageInfo = (PageInfo) request.getSession(false).getAttribute("securePageInfo");
				conditionMap =  (LinkedHashMap<String, String>) request.getSession(false).getAttribute("secureConditionMap");
			}
			else
			{
				pageSet(pageInfo, request);
			
				//是否绑定MAC地址
				conditionMap.put("isBindMac", request.getParameter("isBindMac"));
				//是否绑定IP地址
				conditionMap.put("isBindIp", request.getParameter("isBindIp"));
				//是否启用动态口令
				conditionMap.put("isBindPwd", request.getParameter("isBindPwd"));
				//操作员id
				conditionMap.put("userName", request.getParameter("userName"));
				//操作员名称
				conditionMap.put("name", request.getParameter("name"));
				//IP地址
				conditionMap.put("ipaddr", request.getParameter("ipaddr"));
				//MAC地址
				conditionMap.put("macaddr", request.getParameter("macaddr"));
				
				if(depId == null || "".equals(depId)){
					depId = sysuser.getDepId().toString();
				}
				LfDep dep=baseBiz.getById(LfDep.class, depId);
				//判断机构是否在企业内
				if(!corpcode.equals(dep.getCorpCode()))
				{
					String opContent = "该机构不再本企业内，可能已被删除！depId："+depId+",depName："+dep.getDepName();
					EmpExecutionContext.error("模块名称：高级安全配置，企业："+corpcode+",操作员："+sysuser.getUserName()+","+opContent);
					return;
				}
				
				conditionMap.put("corpCode", corpcode);
				conditionMap.put("depId", depId );
				//保存查询条件
				request.getSession(false).setAttribute("secureConditionMap", conditionMap);
				request.getSession(false).setAttribute("securePageInfo", pageInfo);
				
			}
			List<LfMacIpVo> macIpVoList = loginSafeBiz.getMacIpVo(sysuser,conditionMap,
					pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("macIpVoList", macIpVoList);
			request.setAttribute("conditionMap", conditionMap);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "高级安全设置", opContent, "GET");
			}
			request.getRequestDispatcher(PATH+"/ctr_macipTable.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"高级安全设置获取列表信息出现异常！");
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		String guids = request.getParameter("ids");
		String ips = request.getParameter("ipaddrs");
		String macs = request.getParameter("macaddrs");
		//操作类型
		String opType = request.getParameter("opType");
		String lgcorpcode = "";
		LfMacIp lfMacIp = null;
		List<LfMacIp> addList = new ArrayList<LfMacIp>();
		List<LfMacIp> updateList = new ArrayList<LfMacIp>();
		boolean result = false;
		String aftIPs="";
		String aftMAC="";
		try {
			//获取登录sysuser
			LfSysuser sysuser =loginSafeBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("高级安全设置,update方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysuser.getCorpCode();
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("高级安全设置,update方法session中获取企业编码出现异常");
				return;
			}
			
			if ("add".equals(opType)) {
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				List<LfMacIp> tempList = null;
				String guid[] = guids.split(",");
				for (int i = 0; i < guid.length; i++) {
					conditionMap.put("guid", guid[i]);
					tempList = baseBiz.getByCondition(LfMacIp.class, conditionMap, null);
					lfMacIp = tempList != null && tempList.size()>0?tempList.get(0):null;
					if(lfMacIp != null){
						//如果有记录，则修改（追加MAC-IP地址（最多10个），多余的舍弃），否则添加新的记录
						aftIPs=this.getIpAddrs(lfMacIp.getIpaddr(), ips)+","+aftIPs;
						lfMacIp.setIpaddr(this.getIpAddrs(lfMacIp.getIpaddr(), ips));
						aftMAC=this.getMacAddrs(lfMacIp.getMacaddr(), macs)+","+aftMAC;
						lfMacIp.setMacaddr(this.getMacAddrs(lfMacIp.getMacaddr(), macs));
						updateList.add(lfMacIp);
					}else{
						//表中没记录，则产生一个新的对象
						lfMacIp = new LfMacIp();
						lfMacIp.setGuid(Long.parseLong(guid[i]));
						lfMacIp.setIpaddr(ips);
						lfMacIp.setMacaddr(macs);
						addList.add(lfMacIp);
					}
				}
				result = loginSafeBiz.updateMacIpList(addList, updateList);
				if (result) {
					request.setAttribute("result", "true");
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(updateList!=null){
						//增加操作日志
						if(loginSysuserObj!=null){
							LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改MAC-IP地址成功。[IP值|MAC值]（"+aftIPs+"|"+aftMAC+"）->（"+ips+"|"+macs+"）", "UPDATE");
						}
					}
					if(addList!=null){
						//增加操作日志
						if(loginSysuserObj!=null){
							LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "新增MAC-IP地址成功。[IP值|MAC值]（"+ips+"|"+macs+"）", "ADD");
						}
					}

				} else {
					request.setAttribute("result", "false");
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(updateList!=null){
						//增加操作日志
						if(loginSysuserObj!=null){
							LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改MAC-IP地址失败。[IP值|MAC值]（"+aftIPs+"|"+aftMAC+"）->（"+ips+"|"+macs+"）", "UPDATE");
						}
					}
					if(addList!=null){
						//增加操作日志
						if(loginSysuserObj!=null){
							LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "新增MAC-IP地址失败。[IP值|MAC值]（"+ips+"|"+macs+"）", "ADD");
						}
					}

				}
			} else if ("update".equals(opType)) {
				String guid = request.getParameter("guid");
				LfSysuser selUser = baseBiz.getByGuId(LfSysuser.class, Long.valueOf(guid));
				Integer dtpwd = Integer.parseInt(request.getParameter("dtpwd"));
				boolean canUpdate = true;
				String returnMsg = "";
				//启用动态口令
				if(dtpwd == 1)
				{
					List<Userdata> spUserList= this.getAdminSpAccount(lgcorpcode);
					if(spUserList == null || spUserList.size()<1){
						//动态口令发送短信采用的是admin对应的SP账号，如果admin没有
						//可用的SP账号，则不允许绑定动态口令
						canUpdate = false;
						returnMsg = "noSpUser";
					}
					
					if(selUser == null || selUser.getMobile() == null || "".equals(selUser.getMobile().trim()) )
					{
						//未设置手机号则不允许启用动态口令
						canUpdate = false;
						returnMsg = "noPhone";
					}
				}
				if(canUpdate){
					
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("guid", guid);
					List<LfMacIp>	tempList = baseBiz.getByCondition(LfMacIp.class, conditionMap, null);
					lfMacIp = tempList != null && tempList.size()>0?tempList.get(0):null;
					//如果表中已经存在记录，则进行修改，否则进添加
					if(lfMacIp == null){
						lfMacIp = new LfMacIp();
						lfMacIp.setGuid(Long.parseLong(guid));
						lfMacIp.setIpaddr(ips);
						lfMacIp.setMacaddr(macs);
						lfMacIp.setDtpwd(dtpwd);
						result = baseBiz.addObj(lfMacIp);
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if (result) {
							//增加操作日志
							if(loginSysuserObj!=null){
								LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "新增MAC-IP地址成功。[IP值|MAC值]（"+ips+"|"+macs+"）", "ADD");
							}
						} else {
							//增加操作日志
							if(loginSysuserObj!=null){
								LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "新增MAC-IP地址失败。[IP值|MAC值]（"+ips+"|"+macs+"）", "ADD");
							}
						}
					}else{
						String preIP=lfMacIp.getIpaddr();
						String preMAC=lfMacIp.getMacaddr();
						lfMacIp.setIpaddr(ips);
						lfMacIp.setMacaddr(macs);
						lfMacIp.setDtpwd(dtpwd);
						result = baseBiz.updateObj(lfMacIp);
						if (result) {
							//增加操作日志
							Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
							if(loginSysuserObj!=null){
								LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改MAC-IP地址成功。[IP值|MAC值]（"+preIP+"|"+preMAC+"）->（"+ips+"|"+macs+"）", "UPDATE");
							}
						} else {
							EmpExecutionContext.error("操作类型为修改时，企业："+lgcorpcode+"，修改MAC-IP地址失败");
						}
					}

					returnMsg = result?"true":"false";
				}
				request.setAttribute("result",returnMsg);
				
			}
			request.setAttribute("isOperateRetrun", "true");
			find(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"高级安全设置更新操作出现异常！");
		}
	}


	//文件导入绑定
	@SuppressWarnings("unchecked")
	public void upload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<LfMacIp> macIpList = new ArrayList<LfMacIp>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		String resultMsg = "";
		String lgcorpcode = null;
		String lgguid = "";
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException e) {
			EmpExecutionContext.error(e,"高级安全设置解析表单出现异常！");
			resultMsg = "error";
			return ;
		}
		LfSysuser sysuser =null;
		try{
			//获取登录sysuser
			sysuser =loginSafeBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("高级安全设置,update方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysuser.getCorpCode();
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("高级安全设置,update方法session中获取企业编码出现异常");
				return;
			}	
			lgguid=sysuser.getGuId()+"";
			if(lgguid==null||"".equals(lgguid)){
				EmpExecutionContext.error("高级安全设置,update方法session中获取GUID出现异常");
				return;
			}	
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		Iterator<FileItem> it = fileList.iterator();
		while (it.hasNext()) {
			FileItem fileItem = (FileItem) it.next();
			String fileName = fileItem.getFieldName();
			if(fileName.equals("lgcorpcode")){
//				try {
//					//获取企业编码
//					//lgcorpcode = fileItem.getString("UTF-8").toString();
//				} catch (UnsupportedEncodingException e) {
//					EmpExecutionContext.error(e,"高级安全设置获取企业编码出现异常！");
//					resultMsg = "error";
//					return ;
//				}
			}else if(fileName.equals("lgguid")){
//				try {
//					//获取企业编码
//					//lgguid = fileItem.getString("UTF-8").toString();
//				} catch (UnsupportedEncodingException e) {
//					EmpExecutionContext.error(e,"高级安全设置获取lgguid出现异常！");
//					resultMsg = "error";
//					return ;
//				}
			}else if (!fileItem.isFormField() && fileItem.getName().length() > 0) {
				BufferedReader reader = null;
				try {
					String fileCurName = fileItem.getName();
					String fileType = fileCurName.substring(fileCurName
							.lastIndexOf("."));
					//判断是否为xls文件类型
					if (fileType.equals(".xls")) {
						List<LfMacIp> repeatList = new ArrayList<LfMacIp>();
						List<LfMacIp> updateList = new ArrayList<LfMacIp>();
						Workbook workBook = null;
						try {
							workBook = Workbook.getWorkbook(fileItem
									.getInputStream());
						}catch (BiffException e) {
							EmpExecutionContext.error(e,"处理xls文件出现异常！");
							resultMsg = "errorFile";
							return ;
						} catch (IOException e) {
							EmpExecutionContext.error(e,"处理xls文件的输入输出异常！");
							resultMsg = "errorFile";
							return ;
						}
						Sheet sh = workBook.getSheet(0);
						/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Timestamp createTime = new Timestamp(sdf.parse(sdf.format(new Date()))
								.getTime());*/
						boolean isAdd = true;
						for (int k = 1; k < sh.getRows(); k++) {
							Cell[] cells = sh.getRow(k);
							int size = cells.length;
							if (size > 1) {
								isAdd = true;
								//操作员id
								String userName = cells[0].getContents().trim();
								//IP地址
								String ipaddr = cells[1].getContents().trim();
								String macaddr = "";
								if(size >2){
									//如果每行记录超过2个cell,则获取MAC地址
									macaddr = cells[2].getContents().trim();
								}
								ipaddr = ipaddr == null?"":ipaddr;
								macaddr = macaddr == null?"":macaddr;
								boolean isUserNull = userName == null || "".equals(userName);
								if(isUserNull){
									//如果userName为空则不进行下一步的验证，开始验证下一条记录
									continue ;
								}
								LfMacIp macIp = checkUserName(userName,lgcorpcode,updateList);//验证该操作员是否存在
								if(macIp == null || ("".equals(ipaddr) && "".equals(macaddr))){
									//不存在该操作员，或者ip地址跟mac地址都为空
									continue ;
								}
								if(macIp.getLmiid() == null || macIp.getLmiid() == 0){
									//上传时重复的userName,IP-MAC地址累加
									for (LfMacIp obj : repeatList) {
										if(macIp.getGuid().toString().equals(obj.getGuid().toString())){
											macaddr = this.getMacAddrs(obj.getMacaddr(),macaddr);
											ipaddr = this.getIpAddrs(obj.getIpaddr(),ipaddr);
											obj.setMacaddr(macaddr);
											obj.setIpaddr(ipaddr);
											isAdd = false;
										}
									}
								}
								if(isAdd){
									macaddr = this.getMacAddrs(macIp.getMacaddr(),macaddr); 
									ipaddr = this.getIpAddrs(macIp.getIpaddr(),ipaddr);
									macIp.setMacaddr(macaddr);
									macIp.setIpaddr(ipaddr);
								}
								if(macIp.getLmiid() == null || macIp.getLmiid() == 0){
									//macIp.setCreatorName(lgusername);
									//macIp.setCreatTime(createTime);
									repeatList.add(macIp);
									if(isAdd){
										macIpList.add(macIp);
									}
								}
							}	
						}
						if (macIpList.size() > 0 || updateList.size()>0) {
							boolean result = loginSafeBiz.updateMacIpList(macIpList,updateList);
							if(result){
								//增加操作日志
								Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
								if(loginSysuserObj!=null){
									LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
									EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "文件导入绑定MAC-IP成功，修改原来MAC-IP地址数："+updateList.size()+",新增MAC-IP地址数："+macIpList.size(), "OTHER");
								}
								Integer count = macIpList.size()+updateList.size();
								resultMsg = "upload"+ count;
							}else{
								resultMsg = "false";
								EmpExecutionContext.error("企业："+lgcorpcode+"，文件导入绑定MAC-IP失败");
							}
						}else {
							resultMsg = "noRecord";
							EmpExecutionContext.error("企业："+lgcorpcode+"，没有记录导入");
						}
					} 
				} catch (Exception e) {
					EmpExecutionContext.error(e,"高级安全设置读取文件信息出现异常！");
					resultMsg = "error";
				} finally {
					try {
						if (reader != null)
							reader.close();
					} catch (IOException e) {
						EmpExecutionContext.error(e,"高级安全设置关闭文件流出现异常！");
					}
					fileItem.delete();
					String s = request.getRequestURI();
					s = s+"?method=find&lgguid="+lgguid+"&lgcorpcode="+lgcorpcode;
					try {
						request.getSession(false).setAttribute("ctrResult", resultMsg);
						response.sendRedirect(s);
					} catch (IOException e) {
						EmpExecutionContext.error(e,"高级安全设置页面跳转出现异常！");
					}
					
				}
			}
		}
	}
	
	//通过username验证是否存在该操作员，
	//如果该操作员在IP-MAC地址绑定表中有记录则返回该lfmacip对象
	//如果没有则返回一个新的包含guid的lfmacip对象
	private LfMacIp checkUserName(String userName,String corpCode,List<LfMacIp> updateList){
		
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userName", userName);
		conditionMap.put("corpCode", corpCode);
		LfMacIp result = null;
		try {
			List<LfSysuser> lfSysusers = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			LfSysuser sysuser = lfSysusers != null && lfSysusers.size()>0? lfSysusers.get(0):null;
			if(sysuser != null){
				for (LfMacIp lfMacIp : updateList) {
					if(sysuser.getGuId().toString().equals(lfMacIp.getGuid().toString())){
						//如果要修改的集合里面有该操作员的绑定记录则返回
						return lfMacIp;
					}
				}
				conditionMap.clear();
				conditionMap.put("guid", sysuser.getGuId().toString());
				List<LfMacIp> lfMacIps = baseBiz.getByCondition(LfMacIp.class, conditionMap, null);
				if(lfMacIps != null && lfMacIps.size()>0){
					//已有绑定记录，修改记录
					result = lfMacIps.get(0);
					updateList.add(result);
				}else{
					//还没有绑定任何登录验证的操作员，新增记录
					result = new LfMacIp();
					result.setGuid(sysuser.getGuId());
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"高级安全设置验证名称出现异常！");
		}
		return result;
	}
	
	//验证并获取ip地址，ip为原有的ip地址字符串，ipaddrs为新添加的ip地址字符串
	private String getIpAddrs(String ip,String ipaddrs){
		
		ipaddrs = ipaddrs==null?"":ipaddrs;
		if(ip == null || "".equals(ip)){
			ip = ipaddrs;
		}else{
			ip = ip+","+ipaddrs;
		}
		
		String ips[] = ip.split(",");
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = null; 
		String ipaddr = "";
		for (int i=0; i<ips.length;i++) {
			matcher = pattern.matcher(ips[i]);
			//如果IP地址有效，并且该IP地址不存在原有记录中,则追加
			if(matcher.matches() && ipaddr.indexOf(ips[i]) == -1){
				ipaddr +=ips[i]+",";
			}
		}
		ips=ipaddr.split(",");
		//IP地址最多为10个，验证过滤非法重复IP地址后，如果超过10个，则截取前10个
		if(ips.length >10){
			ipaddr = "";
			for(int i=0;i<10;i++){
				ipaddr += ips[i]+",";
			}
		}
		if("".equals(ipaddr)){
			return null;
		}
		return ipaddr.substring(0,ipaddr.lastIndexOf(","));
	}
	
	//验证并获取MAC地址，mac地址为表中原有的mac地址字符串，macaddrs为新填写的mac地址字符串
	private String getMacAddrs(String mac,String macaddrs){
		macaddrs = macaddrs==null?"":macaddrs;
		if(mac == null || "".equals(mac)){
			mac = macaddrs;
		}else{
			mac = mac+","+macaddrs;
		}
		String macs[] = mac.split(",");
		Pattern pattern = Pattern.compile("[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}");
		Matcher matcher = null; 
		String macaddr = "";
		for (int i=0;i<macs.length;i++) {
			matcher = pattern.matcher(macs[i]);
			//如果MAC地址有效，并且该MAC地址不存在原有记录中,则追加
			if(matcher.matches() && macaddr.indexOf(macs[i]) == -1){
				macaddr +=macs[i]+",";
			}
		}
		macs=macaddr.split(",");
		//MAC地址最多为10个，验证过滤非法重复MAC地址后，如果超过10个，则截取前10个
		if(macs.length >10){
			macaddr = "";
			for(int i=0;i<10;i++){
				macaddr += macs[i]+",";
			}
		}
		if("".equals(macaddr)){
			return null;
		}
		return macaddr.substring(0,macaddr.lastIndexOf(","));
	}
	
	//得到admin绑定的SP账号（遵循操作员->本级机构->上级机构->游离SP账号规则）
	private List<Userdata> getAdminSpAccount(String corpCode) throws Exception{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		if("100000".equals(corpCode)){
			conditionMap.put("userName", "sysadmin");
		}else{
			conditionMap.put("userName", "admin");
		}
		conditionMap.put("corpCode", corpCode);
		List<LfSysuser> sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap,null);
		LfSysuser sysuser = sysuserList!= null && sysuserList.size()>0?sysuserList.get(0):null;
		if(sysuser == null){
			//如果查询不到admin账号信息，则抛出异常
			throw new Exception();
		}
		List<Userdata> spUserList = new SmsBiz().getSpUserList(sysuser);
		return spUserList;
	}
	
	//启用动态口令
	public void enablePwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String guid = request.getParameter("guid");
		String lgcorpcode = "";
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//新添数据集合
		List<LfMacIp> addList = new ArrayList<LfMacIp>();
		//修改数据集合
		List<LfMacIp> updateList = new ArrayList<LfMacIp>();
		LfMacIp lfMacIp = null;
		String guids[] = guid.split(",");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<LfMacIp> tempList = null;
		try {
			
			LfSysuser sysuser =loginSafeBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("高级安全设置,update方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysuser.getCorpCode();
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("高级安全设置,update方法session中获取企业编码出现异常");
				return;
			}	
			
			List<Userdata> spUserList = this.getAdminSpAccount(lgcorpcode);
			if(spUserList == null || spUserList.size()<1){
				//动态口令发送短信采用的是admin对应的SP账号，如果admin没有
				//可用的SP账号，则不允许绑定动态口令
				response.getWriter().print("noSpUser");
				return ;
			}
			
			//获取没手机号的操作员guid
			Map<String,LfSysuser> noPhoneUserMap = this.getNoPhoneUser(guid,lgcorpcode);
			//Timestamp time = new Timestamp(sdf.parse(sdf.format(new Date())).getTime());
			//循环guid，如果lfmacip表中存在记录，则进行修改，否则新添一条记录
			String strResult = "";
			for (String str : guids) {

				if(noPhoneUserMap.containsKey(str) )
				{
					//未设置手机号则不允许启用动态口令
					strResult += noPhoneUserMap.get(str).getName()+",";
					continue;
				}
				
				conditionMap.put("guid",str);
				tempList = baseBiz.getByCondition(LfMacIp.class, conditionMap, null);
				lfMacIp = tempList != null && tempList.size()>0?tempList.get(0):null;
				if(lfMacIp != null){
					lfMacIp.setDtpwd(1);
					updateList.add(lfMacIp);
				}else{
					lfMacIp = new LfMacIp();
					lfMacIp.setGuid(Long.parseLong(str));
					lfMacIp.setDtpwd(1);
					//lfMacIp.setCreatorName(lfSysuser.getUserName());
					//lfMacIp.setCreatTime(time);
					//lfMacIp.setType(4);
					addList.add(lfMacIp);
				}
			}
			if(strResult != null && strResult.length() > 0)
			{
				strResult = strResult.substring(0,strResult.length()-1);
			}
			
			if((strResult != null && strResult.length() > 0) && (addList == null || addList.size() == 0)&& (updateList == null || updateList.size() == 0))
			{
				//所有选中的操作员都没手机号码
				response.getWriter().print("false&"+strResult);
				return;
			}
			
			boolean result = loginSafeBiz.updateMacIpList(addList, updateList);
			
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(result){
				//增加操作日志
				if(loginSysuserObj!=null){
					LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "启用动态口令成功，修改原来MAC-IP地址数："+updateList.size()+",新增MAC-IP地址数："+addList.size(), "OTHER");
				}
				
			}else{
				//增加操作日志
				if(loginSysuserObj!=null){
					LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "启用动态口令失败", "OTHER");
				}
			}
			strResult = result +"&"+ strResult;
			
			response.getWriter().print(strResult);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"高级安全设置启动动态口令出现异常！");
			response.getWriter().print("error");
		}
	}
	
	/**
	 * 获取没有手机号码的操作员的guid
	 * @param guId
	 * @return key为guid
	 * @throws Exception
	 */
	private Map<String,LfSysuser> getNoPhoneUser(String guId,String corpCode) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("guId&in", guId);
		conditionMap.put("corpCode", corpCode);
		List<LfSysuser> usersList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
		Map<String,LfSysuser> resultMap = new HashMap<String,LfSysuser>();
		for(int i = 0; i<usersList.size();i++)
		{
			if(usersList.get(i).getMobile() == null || "".equals(usersList.get(i).getMobile().trim()))
			{
				resultMap.put(usersList.get(i).getGuId().toString(), usersList.get(i));
			}
		}
		return resultMap;
	}
	/**
	 * 获取没有手机号码的操作员的guid
	 * @param guId
	 * @return key为guid
	 * @throws Exception
	 */
	private Map<String,LfSysuser> getNoPhoneUser(String guId) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("guId&in", guId);
		List<LfSysuser> usersList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
		Map<String,LfSysuser> resultMap = new HashMap<String,LfSysuser>();
		for(int i = 0; i<usersList.size();i++)
		{
			if(usersList.get(i).getMobile() == null || "".equals(usersList.get(i).getMobile().trim()))
			{
				resultMap.put(usersList.get(i).getGuId().toString(), usersList.get(i));
			}
		}
		return resultMap;
	}
	//禁用动态口令
	/*public void cancelPwd(HttpServletRequest request, HttpServletResponse response) {
		String guid = request.getParameter("guid");
		String guids[] = guid.split(",");
		LfMacIp lfMacIp = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<LfMacIp> tempList = null;
		List<LfMacIp> updateList = new ArrayList<LfMacIp>();
		try {
			//通过guid查询表LFMACIP表中数据，设置dtpwd=0
			for (String str : guids) {
				conditionMap.put("guid",str);
				tempList = baseBiz.getByCondition(LfMacIp.class, conditionMap, null);
				lfMacIp = tempList != null && tempList.size()>0?tempList.get(0):null;
				if(lfMacIp != null){
					lfMacIp.setDtpwd(0);
					updateList.add(lfMacIp);
				}
			}
			boolean result = loginSafeBiz.updateMacIpList(null, updateList);
			writer.print(result);
		} catch (Exception e) {
			EmpExecutionContext.error(e);
			writer.print("error");
		}
	}
	*/
	
	public void cancelBind(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cancelType = request.getParameter("cancelType");
		String guids = request.getParameter("guid");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("guid&in",guids);
		String returnMsg = "";
		try {
			List<LfMacIp> macipList = baseBiz.getByCondition(LfMacIp.class, conditionMap, null);
			if(macipList != null && macipList.size()>0){
				for (LfMacIp lfMacIp : macipList) {
					if(cancelType.contains("1")){
						lfMacIp.setIpaddr("");
					}
					if(cancelType.contains("2")){
						lfMacIp.setMacaddr("");			
					}
					if(cancelType.contains("3")){
						lfMacIp.setDtpwd(0);
					}
				}
				boolean result = loginSafeBiz.updateMacIpList(null,macipList);
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(result){
					//增加操作日志
					if(loginSysuserObj!=null){
						LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "解除绑定成功[ID，解除绑定数]（"+guids+"，"+macipList.size()+")", "OTHER");
					}
				}else{
					//增加操作日志
					if(loginSysuserObj!=null){
						LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("高级安全设置", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "解除绑定失败[ID，解除绑定数]（"+guids+"，"+macipList.size()+")", "OTHER");
					}
				}
				returnMsg = result?"true":"false";
			}else{
				returnMsg = "norecord";
			}
			response.getWriter().print(returnMsg);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"高级安全设置取消绑定出现异常！");
			response.getWriter().print("error");
		}
	}
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常，session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}
