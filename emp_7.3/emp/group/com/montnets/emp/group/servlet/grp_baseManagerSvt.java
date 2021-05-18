package com.montnets.emp.group.servlet;


import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.group.biz.GrpBaseMgrBiz;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 个人群组（客户）
 * @author LINZHIHAN
 *
 */
@SuppressWarnings("serial")
public class grp_baseManagerSvt extends BaseServlet {

	//操作模块
	public final String opModule="个人群组";
    public final String empRoot = "group";
    public final String basePath = "/grpmag";

    //个人群组新增和修改弹出框中成员列表分页每页显示的页数
    public static final Integer SIZE = 50;

    public final BaseBiz baseBiz = new BaseBiz();

    public final GrpBaseMgrBiz grpBaseBiz = new GrpBaseMgrBiz();
    /**
     *   查询 方法
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
        PageInfo pageInfo = new PageInfo();
        List<LfUdgroup> groupList = new ArrayList<LfUdgroup>();
        GrpManagerParams grpParams = getServletParams();
		try {
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);

            if(lguserid == null || "".equals(lguserid.trim())){
                Object obj = request.getSession(false).getAttribute("loginSysuser");
                if(obj == null){
                    EmpExecutionContext.error("登录会话超时，获取登录对象为null");
                    return;
                }
                LfSysuser lfSysuser = (LfSysuser)obj;
                lguserid  = String.valueOf(lfSysuser.getUserId());
            }

            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
            boolean isFirstEnter = pageSet(pageInfo,request);
			if(!isFirstEnter)
			{
				String groupName = request.getParameter("groupName");
				String groupType = request.getParameter("groupType"); 
				//共享状态
				String shareStatus = request.getParameter("shareStatus"); 
				if(groupName!=null && !"".equals(groupName.trim()))
				{
					conditionMap.put("udgName&like", groupName);
					request.setAttribute("groupName", groupName);
				}
				if(groupType!=null && !"".equals(groupType.trim()))
				{
					conditionMap.put("sharetype", groupType);
					request.setAttribute("groupType", groupType);
				}
				if(shareStatus!=null && !"".equals(shareStatus.trim()))
				{
					conditionMap.put("shareStatus", shareStatus);
					request.setAttribute("shareStatus", shareStatus);
				}
			}

			orderbyMap.put("udgId", StaticValue.ASC);
			conditionMap.put("receiver", lguserid);
			conditionMap.put("gpAttribute", grpParams.getGpAttribute());
			groupList = baseBiz.getByConditionNoCount(LfUdgroup.class, null, conditionMap, orderbyMap, pageInfo);
			
			//**********加密处理*********
			//分组ID加密集合	
			HashMap<String,String> encryptmap =new HashMap<String,String>();
			//个人群组ID
			HashMap<String,String> groupEncryptmap =new HashMap<String,String>();
			ParamsEncryptOrDecrypt encryptOrDecrypt=getParamsEncryptOrDecrypt(request);
			//未知机构客户设置标识
			if(groupList != null && groupList.size()>0)
			{
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
						for(LfUdgroup group:groupList)
						{
							//分组ID加密
							String UdgId = encryptOrDecrypt.encrypt(String.valueOf(group.getUdgId()));
							encryptmap.put(group.getUdgId().toString(), UdgId);
							String group_id = encryptOrDecrypt.encrypt(String.valueOf(group.getUdgId()));
							//个人群组ID加密值
							groupEncryptmap.put(group.getGroupid().toString(), group_id);
						}
					}
				else
				{
					EmpExecutionContext.error("查询个人群组列表，从session中获取加密对象为空！");
				}
			}
			request.setAttribute("encryptmap", encryptmap);
			request.setAttribute("groupEncryptmap", groupEncryptmap);
			//增加查询日志
            long end_time=System.currentTimeMillis();
            String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"毫秒，数量："+pageInfo.getTotalRec();
            opSucLog(request, grpParams.getMgrSvtName()+"个人群组查询", opContent, "GET");

		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转"+grpParams.getMgrSvtName()+"群组出现异常 ！");
		}finally {
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("groupList", groupList);
            request.getRequestDispatcher(this.empRoot + basePath +"/"+grpParams.getIndexPage()).forward(request, response);
        }
    }
    


    /**
     *  添加群组信息
     * @param request
     * @param response
     */
	public void addGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        boolean result = false;
        //获取基本参数
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        String lgcorpcode = request.getParameter("lgcorpcode");
        String udgName = request.getParameter("udgName");
        String opUser = "";

        GrpManagerParams grpParams = getServletParams();
        String opType = StaticValue.ADD;
        String opContent = "新建"+grpParams.getMgrSvtName()+"群组（"+udgName+"）";
        try {
            //参数异常返回
            if(StringUtils.isBlank(lguserid) || StringUtils.isBlank(lgcorpcode)){
                Object obj = request.getSession(false).getAttribute("loginSysuser");
                if(obj != null){
                    LfSysuser lfSysuser = (LfSysuser)obj;
                    lguserid = String.valueOf(lfSysuser.getUserId());
                    lgcorpcode = lfSysuser.getCorpCode();
                    opUser = lfSysuser.getUserName();
                }
            }
            if(StringUtils.isBlank(lguserid) || StringUtils.isBlank(lgcorpcode) || StringUtils.isBlank(udgName)){
                out.print("error");
                return;
            }
            //验证名称的唯一性
            String gpAttribute = grpParams.getGpAttribute();
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
            conditionMap.put("userId", lguserid);
            conditionMap.put("udgName", udgName);
            conditionMap.put("gpAttribute", gpAttribute);//表明是客户群组还是员工群组
            List<LfUdgroup> udList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, null);
            if(udList != null && udList.size() > 0)
            {
                out.print("exists");
                return;
            }

            Long userid = Long.valueOf(lguserid);
            LfUdgroup group = new  LfUdgroup();
            group.setUdgName(udgName);
            group.setUserId(userid);
            group.setGpAttribute(Integer.parseInt(gpAttribute));
            group.setSharetype(0);
            group.setSendmode(1);
            //0-非共享群组；1-指定操作员共享群组
            group.setGroupType(0);
            //接收者
            group.setReceiver(userid);
            //共享状态,默认为0,0表示未共享;1:表示已共享
            group.setShareStatus(0);
            //创建时间
            group.setCreateTime(new Timestamp(System.currentTimeMillis()));
            //客户来源或员工来源
            String ygStr = request.getParameter("ygStr");
            //群组来源
            String qzStr = request.getParameter("qzStr");
            //共享来源
            String gxStr = request.getParameter("gxStr");
            //自建
            String zjStr = request.getParameter("zjStr");

            //保存群组
            result = grpBaseBiz.saveGroup(group, new String[]{lgcorpcode, ygStr, qzStr, gxStr, zjStr});
            out.print(result);
            opSucLog(request, opModule, opContent, opType);
            new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
        } catch (Exception e) {
            new SuperOpLog().logFailureString(opUser, opModule, opType, opContent, e,lgcorpcode);
            EmpExecutionContext.error(e, "添加" + grpParams.getMgrSvtName()+"群组出现异常！");
            out.print(result);
        }
	}
    
    
	
	/**
	 * 获取群组信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getGroupList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		StringBuffer buffer = new StringBuffer("<select name='groupList' id='groupList' " +
					"size='15' style='width: 260px; padding:2px;vertical-align:middle;margin:-4px -6px;color: black;font-size: 12px;'");
		buffer.append(" onchange='grouponChange()'>");
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderByMap = new LinkedHashMap<String,String>();
		BaseBiz baseBiz = new BaseBiz();
        GrpManagerParams grpParams = getServletParams();
		try {
			conditionMap.put("gpAttribute",grpParams.getGpAttribute());
			conditionMap.put("sharetype", "0");
			conditionMap.put("userId", request.getParameter("lguserid"));
			orderByMap.put("udgName", StaticValue.ASC);
			List<LfUdgroup> udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
			if (udgList != null && udgList.size() > 0) 
			{
				
				String udgIds = "";
				for(LfUdgroup udg : udgList)
				{
					udgIds += udg.getUdgId().toString()+",";
				}
				udgIds = udgIds.substring(0,udgIds.length()-1);
				Map<String,String> countMap = grpBaseBiz.getGrpCount(udgIds);
				for(LfUdgroup udg : udgList)
				{
					String mcount = countMap.get(udg.getUdgId().toString());
					mcount = mcount == null ? "0" : mcount;
					buffer.append("<option mcount='").append(mcount).append("' qzlx='1' value='").append(udg.getGroupid()).append("'>");
					buffer.append(udg.getUdgName().replace("<","&lt;").replace(">","&gt;")).append("["+ MessageUtils.extractMessage("group","group_ydbg_xzqz_text_selfbuilt",request)+"]</option>");
				}
			}
			conditionMap.put("sharetype", "1");
			conditionMap.put("receiver", request.getParameter("lguserid"));
			conditionMap.remove("userId");
			List<LfUdgroup> udgList2 = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
			if (udgList2 != null && udgList2.size() > 0) 
			{
				
				String udgIds = "";
				for(LfUdgroup udg : udgList2)
				{
					udgIds += udg.getUdgId().toString()+",";
				}
				udgIds = udgIds.substring(0,udgIds.length()-1);
				Map<String,String> countMap = grpBaseBiz.getGrpCount(udgIds);
				for(LfUdgroup udg : udgList2)
				{
					String mcount = countMap.get(udg.getUdgId().toString());
					mcount = mcount == null ? "0" : mcount;
					buffer.append("<option mcount='").append(mcount).append("' qzlx='2' value='").append(udg.getGroupid()).append("'>");
					buffer.append(udg.getUdgName().replace("<","&lt;").replace(">","&gt;")).append("["+MessageUtils.extractMessage("group","group_ydbg_xzqz_text_shared",request)+"]</option>");
				}
			}
			
			buffer.append("</select>");
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取" + grpParams.getMgrSvtName() + "群组信息出现异常！");
		}
		response.getWriter().print(buffer.toString());
	}
    
    
    
	/**
	 *   通过群组ID查询出员工/客户群组 中的群组人员信息，分页
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void  getGroupUserByGroupId(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
        GrpManagerParams grpParams = getServletParams();
		try
		{
            LfSysuser sysuser = getLoginUser(request);
			//String userId = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String userId = SysuserUtil.strLguserid(request);


			String corpCode = sysuser.getCorpCode();
			String name = request.getParameter("epname");
			StringBuffer sb = new StringBuffer();
			String udgId = request.getParameter("udgId");
			udgId = (udgId != null && udgId.length() > 0) ? udgId.substring(0,udgId.length()-1) : "";
			String pageIndex = request.getParameter("pageIndex");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.parseInt(pageIndex));
			List<GroupInfoVo> groupInfoList = null;
            if("0".equals(grpParams.getGpAttribute())){//员工群组
                groupInfoList = grpBaseBiz.getGroupUser(Long.valueOf(udgId), pageInfo, name,corpCode);
            }else if("1".equals(grpParams.getGpAttribute())){
                groupInfoList = grpBaseBiz.getGroupClient(Long.valueOf(udgId), pageInfo,name,corpCode);
            }

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId);
            conditionMap.put("corpCode", corpCode);
			List<LfMalist> malistList2 = baseBiz.getByCondition(LfMalist.class, conditionMap, null);
			
			String ids = ",";
			if (malistList2 != null && malistList2.size() > 0) 
			{
				for (LfMalist user : malistList2) {
					ids += user.getGuId().toString() + ",";
				}
			}
			
			if (groupInfoList != null && groupInfoList.size() > 0) {
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (GroupInfoVo user : groupInfoList) {
                    if(user.getL2gType() == 0){
                        //客户
                        sb.append("<option value='").append(""+user.getGuId().toString())
                                .append("' etype='1 ' mobile='").append(user.getMobile()).append("'>");
                        sb.append(user.getName().trim()).append("["+MessageUtils.extractMessage("group","group_ydbg_xzqz_text_employee",request)+"]</option>");
                    }else if(user.getL2gType() == 1){
						//客户
						sb.append("<option value='").append(""+user.getGuId().toString())
						.append("' etype='1 ' mobile='").append(user.getMobile()).append("'>");
						sb.append(user.getName().trim()).append("["+MessageUtils.extractMessage("group","group_ydbg_xzqz_text_client",request)+"]</option>");
					}else if(user.getL2gType() == 2){
						//自定义
						if(ids.indexOf(","+user.getGuId().toString()+",") > -1)
						{
							sb.append("<option value='").append(""+user.getGuId().toString())
								.append("' etype='2' mobile='").append(user.getMobile()).append("'>");
							sb.append(user.getName().trim()).append("["+MessageUtils.extractMessage("group","group_ydbg_xzqz_text_selfbuilt",request)+"]</option>");
						}else
						{
							sb.append("<option value='").append(""+user.getGuId().toString())
								.append("' etype='4' mobile='").append(user.getMobile()).append("'>");
							sb.append(user.getName().trim()).append("["+MessageUtils.extractMessage("group","group_ydbg_xzqz_text_shared",request)+"]</option>");
						}
					}
				}
			}
			response.getWriter().print(sb.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, grpParams.getMgrSvtName() + "群组查询群组成员出现异常！");
		}
	}
    
    /**
     * 检查号码是否合法
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkMoblie(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        String mobile = request.getParameter("mobile");
        GrpManagerParams grpParams = getServletParams();
        Integer isValid = 0; // 初始化
        try {
            WgMsgConfigBiz wgMsgConfigBiz = new WgMsgConfigBiz();
            String[] haoduans = wgMsgConfigBiz.getHaoduan();
            //增加国际号码录入
            PhoneUtil util=new PhoneUtil();
            isValid = util.getPhoneType(mobile, haoduans);
            if (isValid != null && isValid != -1) {//-1是非法，0，移动，1，联通，2，电信，3，国际
                isValid = 3; // 正确
            } else {
                isValid = 4; // 不合法
            }
            response.getWriter().print(isValid);
        } catch (Exception e) {
            response.getWriter().print(isValid);
            EmpExecutionContext.error(e,"处理"+grpParams.getMgrSvtName()+"群组验证手机号码合法出现异常 ！");
        }
    }

    public boolean checkRepeat(HashSet<String> aa,String ee)
    {
        if(aa.contains(ee)){
            return false;
        }else{
            aa.add(ee);
        }
        return true;
    }

    public List<LfUdgroup> getSharedUserByGroupId(String groupId){
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("groupid", groupId);
        conditionMap.put("sharetype", "1");
        List<LfUdgroup> udGroupList = null;
        try {
            udGroupList = new BaseBiz().getByCondition(LfUdgroup.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"获取群组共享成员出现异常！");
        }
        return udGroupList;
    }
    /**
     * 根据udgID获取群组的信息
     * @param udgId
     * @return
     */
    public LfUdgroup getLfUdgroupByUdgId(String udgId){
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("udgId", udgId);
        conditionMap.put("sharetype", "0");
        List<LfUdgroup> udGroupList = null;
        try {
            udGroupList = new BaseBiz().getByCondition(LfUdgroup.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"获取群组信息出现异常！");
        }
        return udGroupList==null?null:udGroupList.get(0);
    }

    /**
     *   进入共享界面
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSharedUser(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        String groupId = request.getParameter("groupid");
        String returnStr = "";
        String userIds = "";
        GrpManagerParams grpParams = getServletParams();
        try{
            if(groupId == null || "".equals(groupId.trim())){
                EmpExecutionContext.error("共享"+grpParams.getMgrSvtName()+"群组获取群组id异常！id:"+groupId);
                returnStr = "illegal";
                return;
            }
            //解码处理
            String uid=getDecryptValue( request, groupId);	
            LfUdgroup lfUdgroup = baseBiz.getById(LfUdgroup.class, uid);
            if(lfUdgroup == null){
                EmpExecutionContext.error("共享"+grpParams.getMgrSvtName()+"群组获取群组对象为null！");
                returnStr = "noexist";
                return;
            }
            List<LfUdgroup> udGroupList = this.getSharedUserByGroupId(uid);
            if (udGroupList != null && udGroupList.size() > 0) {
                for (LfUdgroup udgroup : udGroupList) {
                    userIds+=udgroup.getReceiver()+",";
                }
                userIds = userIds.substring(0, userIds.lastIndexOf(","));
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("userId&in", userIds);
                List<LfSysuser> sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
               /* StringBuffer sb = new StringBuffer();
                if (sysuserList != null && sysuserList.size() > 0) {
                    for (LfSysuser user : sysuserList) {
                        sb.append("<option value='").append(user.getUserId())
                                .append("' mobile='").append(user.getMobile()).append("'>");
                        sb.append("["+MessageUtils.extractMessage("group","group_common_operator",request)+"]").append(user.getName().trim()).append("</option>");
                    }
                }
                returnStr = sb.toString();*/
                returnStr = JSONObject.toJSONString(sysuserList);
            }
        } catch (Exception e)
        {
            returnStr = "false";
            EmpExecutionContext.error(e,"获取"+grpParams.getMgrSvtName()+"共享群组界面出现异常！");
        }finally {
            response.getWriter().print(returnStr);
        }
    }
    
	/**
	 * 解密处理
	 * @param request
	 * @param udgId
	 * @return
	 */
	public String getDecryptValue(HttpServletRequest request,String udgId){
				//-----增加解密处理----
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			String uid="";
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				uid = encryptOrDecrypt.decrypt(udgId);
				if(uid == null)
				{
					EmpExecutionContext.error("客户群组参数解密码失败，keyId:"+uid);
					return "";
				}
			}
			else
			{
				EmpExecutionContext.error("客户群组从session中获取加密对象为空！");
				return "";
			}
			return uid;
	}
    
    //点击选择机构按钮的时候如果包含子机构则获取子机构集合
    public void checkDepIsExist(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            String depIdsExist = request.getParameter("depIdsExist");

            String lgcorpcode = request.getParameter("lgcorpcode");// 当前登录企业

            String[] depIds = depIdsExist.split(",");
            StringBuffer depIdsTemp = new StringBuffer();
            for (int i = 0; i < depIds.length; i++) {
                if (depIds[i].indexOf("e") > -1) {
                    depIdsTemp.append(depIds[i].substring(1) + ",");
                } else if (depIds[i].equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                }
            }
            // 判断新添加的机构是不是已经添加的机构的子机构
            boolean result = isDepAcontainsDepB(depIdsTemp.toString(), depId,
                    lgcorpcode);
            if (result) {
                response.getWriter().print("depExist");
                return;
            } else {
                response.getWriter().print("notExist");
                return;
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e,"员工群组处理机构包含出现异常！");
        }
    }

    //判断机构A是否包含机构B
    private boolean isDepAcontainsDepB(String depIdAs, String depIdB,
                                       String corpCode) {
        boolean result = false;
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        String[] depIdAsTemp = depIdAs.split(",");
        try {
            for (int a = 0; a < depIdAsTemp.length; a++) {
                if (depIdAsTemp[a] != null && !"".equals(depIdAsTemp[a])) {
                    String deps = new DepDAO()
                            .getChildUserDepByParentID(null, Long
                                    .valueOf(depIdAsTemp[a]));
                    String depArray[] = deps.split(",");
                    for (int i = 0; i < depArray.length; i++) {
                        depIdSet.add(Long.valueOf(depArray[i]));
                    }
                }
            }
            result = depIdSet.contains(Long.valueOf(depIdB));
        } catch (Exception e) {
            EmpExecutionContext.error(e,"员工群组处理机构包含出现异常！");
        }
        return result;
    }

    public void isDepsContainedByDepB(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
        String ismut = request.getParameter("ismut");
        String depId = request.getParameter("depId");
        DepBiz depBiz = new DepBiz();
        if ("0".equals(ismut)) {
            String countttt = depBiz.getDepCountByDepId(depId);
            response.getWriter().print(countttt);
            return;
        }
        String depIdsExist=request.getParameter("depIdsExist");
        String[] depIds = depIdsExist.split(",");
        //将已经存在的机构id放在list里面(如果前缀有e就去掉e放在depIdExistList里面)
        List<Long> depIdExistList = new ArrayList<Long>();
        for (int j = 0; j < depIds.length; j++) {
            if (depIds[j] != null && !"".equals(depIds[j])) {
                if (depIds[j].indexOf("e") > -1) {
                    if (!"".equals(depIds[j].substring(1))) {
                        depIdExistList.add(Long.valueOf(depIds[j].substring(1)));
                    }
                } else {
                    depIdExistList.add(Long.valueOf(depIds[j]));
                }

            }
        }

        List<Long> depIdListTemp = new ArrayList<Long>();
        String deps = new DepDAO().getChildUserDepByParentID(null, Long.valueOf(depId));
        String depArray[] = deps.split(",");
        //选择机构下所有机构
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        for (String str : depArray) {
            depIdSet.add(Long.valueOf(str));
        }

        //遍历这个set，看看已经存在的机构是否包含在这个机构的子机构里面，如果包含的话，就重新生成option列表的字符串给select控件
        for (int a = 0; a < depIdExistList.size(); a++) {
            if (depIdSet.contains(depIdExistList.get(a))) {
                depIdListTemp.add(depIdExistList.get(a));
            }
        }
        //如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
        String depids = depIdSet.toString();
        depids = depids.substring(1,depids.length()-1);
        //计算机构人数
        String countttt = depBiz.getDepCountByDepId(depids);
        if (depIdListTemp.size() > 0) {
            String tempDeps = depIdListTemp.toString();
            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
            response.getWriter().print(countttt + "," + tempDeps);
            return;
        }
        //如果没有包含关系
        else {
            response.getWriter().print("notContains" + "&" + countttt);
            return;
        }

    }
    /**
     * 查询当前群组是否存在
     * @param request
     * @param response
     * @throws IOException
     */
    public void existGroup(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	
        String groupIdStr = request.getParameter("groupId");
        String uid=getDecryptValue(request,groupIdStr);   
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        boolean result = false;
        try {
            if(uid != null && !"".equals(uid.trim())){
                LfUdgroup lfUdgroup = baseBiz.getById(LfUdgroup.class, uid);
                if(lfUdgroup != null){
                    result = true;
                }
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"查询群组是否存在出现异常！");
        }finally {
            out.print(result);
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
        HttpSession session = request.getSession(false);
        if(session == null) return;
		Object obj = session.getAttribute("loginSysuser");
		if(obj == null) return;
		lfSysuser = (LfSysuser)obj;
		EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
	}

    /**
     * 员工或客户群组 个性化数据信息
     * @return
     */
    public GrpManagerParams getServletParams(){
        String svtName = this.getServletName();
        //区分 是客户群组（1）还是员工群组（0）
        String svtGrpType = "-1";
        String svtGrpInfo = "";
        GrpManagerParams params = new GrpManagerParams();
        if(grp_cliGroupManagerSvt.class.getSimpleName().equals(svtName)){
            params.setGpAttribute("1");
            params.setMgrSvtName("客户");
            params.setIndexPage("grp_cliManage.jsp");
        }else if(grp_groupManagerSvt.class.getSimpleName().equals(svtName)){
            params.setGpAttribute("0");
            params.setMgrSvtName("员工");
            params.setIndexPage("grp_groupManage.jsp");
        }
        return params;
    }

    /**
     *   删除群组
     * @param request
     * @param response
     * @throws IOException
     */
    public void deleteGroup(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        GrpManagerParams grpParams = getServletParams();
        try{
            boolean flag = false;
            //String userid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String userid = SysuserUtil.strLguserid(request);

            String ids = request.getParameter("ids");
            //前端参数非法校验
            if(userid == null || "".equals(userid.trim()) || ids == null || "".equals(ids.trim())){
                response.getWriter().print("illegal");
                EmpExecutionContext.error(grpParams.getMgrSvtName()+"群组，删除群组获取参数异常！");
                return;
            }
            
			//-----增加解密处理----
			String groupSelect="";
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				String userIdStr = "";
				String[] groupArray=null;
				if(ids!=null&&ids.length()>0){
					groupArray= ids.split(",");
				}
				if(groupArray!=null){
					for(int k=0;k<groupArray.length;k++){

						//解密
						userIdStr = encryptOrDecrypt.decrypt(groupArray[k]);
						if(userIdStr == null)
						{
							EmpExecutionContext.error("客户群组参数解密码失败，keyId:"+groupArray[k]);
							response.getWriter().print("error");
							return;
						}
						if(k==groupArray.length-1){
							groupSelect=groupSelect+userIdStr;
						}else{
							groupSelect=groupSelect+userIdStr+",";
						}
						
					}
				}

			}
			else
			{
				EmpExecutionContext.error("客户群组从session中获取加密对象为空！");
				response.getWriter().print("error");
				return;
			}
			//----id解密结束-----
            
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("udgId&in", groupSelect);
            List<LfUdgroup> list = baseBiz.getByCondition(LfUdgroup.class, conditionMap, null);
            flag = grpBaseBiz.delPersonGroup(groupSelect, userid);
            //增加操作日志
            Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
            if(loginSysuserObj!=null)
            {
                LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
                //日志字段
                String field = "[群组id,群组名称，群组类型]";
                String opContent = "";
                String flagStr = flag?"成功":"失败";
                if(list!=null&&list.size()>0)
                {
                    for(int i =0;i<list.size();i++)
                    {
                        opContent = "删除群组"+ flagStr+"。";
                        LfUdgroup udgGroup = list.get(i);
                        opContent += field + "("+udgGroup.getUdgId()+"，"+udgGroup.getUdgName()+"，"+udgGroup.getGroupType()+")";
                        EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, "delete");
                    }
                }
            }
            response.getWriter().print(flag);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"删除"+grpParams.getMgrSvtName()+"群组失败！");
            response.getWriter().print("errer");
        }
    }


    //查找满足条件的操作员列表
    public void getDepAndUserTree(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception
    {
        try{
            String epname = request.getParameter("epname");
            String chooseType = request.getParameter("chooseType");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String depId = request.getParameter("depId");
            StringBuffer sb = new StringBuffer();

            String lgcorpcode = request.getParameter("lgcorpcode");//当前登录企业
            if(depId != null && !"".equals(depId)){
                conditionMap.put("depId", depId);
            }
            conditionMap.put("corpCode", lgcorpcode);
            if("0".equals(chooseType)){
                if(epname != null && !"".equals(epname)){
                    conditionMap.put("name&like", epname);
                }
            }else if("1".equals(chooseType)){
                if(epname != null && !"".equals(epname)){
                    conditionMap.put("mobile&like", epname);
                }
            }

            List<LfSysuser> sysuserList = new BaseBiz().getByCondition(LfSysuser.class, conditionMap, null);
            String str = JSONObject.toJSONString(sysuserList); 
            
            /*if (sysuserList != null && sysuserList.size() > 0) {
                for (LfSysuser user : sysuserList) {
                    sb.append("<option value='").append(user.getUserId())
                            .append("' mobile='").append(user.getMobile()).append("'>");
                    sb.append(user.getName().trim()).append("</option>");
                }
            }
            response.getWriter().print(sb.toString());*/
            response.getWriter().print(str);

        } catch (Exception e)
        {
            EmpExecutionContext.error(e, "员工群组获取操作员出现异常！");
        }
    }

    /**
     * 根据文件路径获取文件头信息
     *
     * @param inputStream
     *            文件流
     * @return 文件头信息
     */
    public String getFileHeader(InputStream inputStream) {
        String value = null;
        try {
            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。 int read(byte[] b) 从此输入流中将最多 b.length
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            inputStream.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }
    
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从session获取加密对象异常。");
			return null;
		}
	}

    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}

//个人群组参数类 区分 员工跟 客户
class GrpManagerParams{
    private String gpAttribute = "-1";
    private String mgrSvtName ;
    private String indexPage;

    public String getGpAttribute() {
        return gpAttribute;
    }

    public void setGpAttribute(String gpAttribute) {
        this.gpAttribute = gpAttribute;
    }

    public String getMgrSvtName() {
        return mgrSvtName;
    }

    public void setMgrSvtName(String mgrSvtName) {
        this.mgrSvtName = mgrSvtName;
    }

    public String getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(String indexPage) {
        this.indexPage = indexPage;
    }
}
