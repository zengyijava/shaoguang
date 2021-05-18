package com.montnets.emp.rms.detailsend.servlet;



import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.montnets.EMPException;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.degree.biz.DegreeBiz;
import com.montnets.emp.rms.degree.vo.LfDegreeManageVo;
import com.montnets.emp.rms.detailsend.biz.DetailSendBiz;
import com.montnets.emp.rms.report.bean.RptStaticValue;
import com.montnets.emp.rms.rmsapi.biz.impl.IMBOSSApiBiz;
import com.montnets.emp.rms.rmsapi.model.QueryHisRecordParams;
import com.montnets.emp.rms.report.biz.QueryBiz;
import com.montnets.emp.rms.vo.RmsMtRecordVo;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.lang.StringUtils;

public class rms_sendDetailSvt extends BaseServlet {
	private static final long serialVersionUID = 5724494635707579265L;
    private final BaseBiz baseBiz = new BaseBiz();
    private final DetailSendBiz detailSendBiz = new DetailSendBiz();
    private static final String MODNAME = "企业富信";
    private static final String OPNAME = "数据查询-发送明细查询";
    private final TxtFileUtil fileUtil = new TxtFileUtil();

    /**
     * 梦网侧企业编码
     */
    private static final String empCorpCode = "100000";



	/**
	 * 发送明细查询查询
	 * 
	 * @author lvxin
	 * @param request
	 * @param response
	 */

	public void find(HttpServletRequest request, HttpServletResponse response){
		//记录耗时
		Long startTime = System.currentTimeMillis();
		// 查询富信历史记录请求参数实体类
		QueryHisRecordParams hisrecord = new QueryHisRecordParams();
		// 是否首次登录
		boolean isFirstEnter = false;
		// 历史记录查询接口
		IMBOSSApiBiz biz = new IMBOSSApiBiz();
		BaseBiz baseBiz = new BaseBiz();
		// sp账号查询结果集
		List<LfSpDepBind>  spList = null;
		// 定义查询结果集
		Map<String, Object> hisRecordMap = null;
		List<Map<String, String>> hisRecordList = null;
		// 日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		//档位查询
		DegreeBiz degreeBiz = new DegreeBiz();
		//历史查询结果是否为空
		Boolean isEmpty=true;
		// 分页信息
		PageInfo pageInfo = new PageInfo();
		//企业编码
		String corpCode = "";
		//操作员Id
		String lguserid = "";
		//SP账号
		String userid = "";
		// 手机号码
		String phone = "";
		// 模板ID
		String tmplid = "";
		// 开始时间
		String starttime = "";
		// 结束时间
		String endtime = "";
		// 运营商
		String operator = "";
		// 富信档位
		String chgrade = "";
		// 发送状态
		String errcode = "";
		try {
			// 是否第一次打开
			isFirstEnter = pageSet(pageInfo, request);

			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			lguserid = lfSysuser.getUserId().toString();

			QueryBiz qbiz = new QueryBiz();
			corpCode = qbiz.getCorpCode(request);

			// 查询sp账号
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			// 如果是梦网则查询所有企业发送账号
			if(!"100000".equals(corpCode)){
				conditionMap.put("corpCode", corpCode);
			}
			
		    spList= baseBiz.getByCondition(LfSpDepBind.class, conditionMap,
					null);
			
			if (!isFirstEnter) {
				// sp账号
				userid = request.getParameter("userid");
				if (userid != null && !"".equals(userid)) {
					hisrecord.setUserid(userid);
				}
				// 手机号码
				phone = request.getParameter("phone").trim();
				if (phone != null && !"".equals(phone)) {
					hisrecord.setPhone(phone);
				}
				// 模板ID
				tmplid = request.getParameter("tmplid").trim();
				if (tmplid != null && !"".equals(tmplid)) {
					hisrecord.setTmplid(Long.parseLong(tmplid));
				}
				// 运营商 0：移动,1：联通,21：电信,5国外，不选则-1：全部
				String mobiletype = request.getParameter("mobiletype");
				if (mobiletype != null && !"".equals(mobiletype)) {
					hisrecord.setMobiletype(Integer.parseInt(mobiletype));
				}
				if("0".equals(mobiletype)){
					operator = "移动";
				}else if("1".equals(mobiletype)){
					operator = "联通";
				}else if("21".equals(mobiletype)){
					operator = "电信";
				}else if("5".equals(mobiletype)){
					operator = "5国外";
				}else {
					operator = "全部";
				}

				// 富信档位
				chgrade = request.getParameter("chgrade").trim();
				if (chgrade != null && !"".equals(chgrade)) {
					hisrecord.setChgrade(Integer.parseInt(chgrade));
				} else {
					hisrecord.setChgrade(-1);
				}
				// 发送状态 0.接收成功(DELIVRD) ，1.失败，-1.全部
				errcode = request.getParameter("errcode").trim();
				if (errcode != null && !"".equals(errcode)) {
					hisrecord.setErrcode(Integer.parseInt(errcode));
				}

				// 开始时间
				starttime = request.getParameter("starttime");
				if (starttime != null && !"".equals(starttime)) {
					hisrecord.setStarttime(starttime);
				}
				// 结束时间
				endtime = request.getParameter("endtime");
				if (endtime != null && !"".equals(endtime)) {
					hisrecord.setEndtime(endtime);
				}
				// 分页条数
				Integer pagesize = pageInfo.getPageSize();
				hisrecord.setPagesize(pagesize);
				// 当前页数
				Integer pageindex = pageInfo.getPageIndex();
				hisrecord.setPageindex(pageindex);
				// 获取查询结果集
				hisRecordMap = biz.queryHisRecord(hisrecord);
				EmpExecutionContext.info("历史查询结果："+hisRecordMap);
				isEmpty=hisRecordMap.isEmpty();
				// 判断调用接口返回状态码 0为成功 
				if(!isEmpty&&(String.valueOf(hisRecordMap.get("rstate"))).equals("0")){
					hisRecordList = (List<Map<String, String>>) hisRecordMap.get("scont");
					if(!hisRecordList.isEmpty()){
						String pagecount=hisRecordList.get(0).get("pagecount");
						if(pagecount!=null&&!pagecount.equals("")){
						pageInfo.setTotalRec(Integer.parseInt(pagecount));
						int totalPage=pageInfo.getTotalRec()/pageInfo.getPageSize();
						int count=pageInfo.getTotalRec()%pageInfo.getPageSize()==0?0:1;
						pageInfo.setTotalPage(totalPage+count);
						}
					}else{
						pageInfo.setTotalRec(0);
						pageInfo.setTotalPage(0);
					}
				}else{
					pageInfo.setTotalRec(0);
					pageInfo.setTotalPage(0);
				}
			}
			
			List<LfDegreeManageVo> chaVoList = null;
			LfDegreeManageVo degreeVo = new LfDegreeManageVo();
			chaVoList = degreeBiz.getDegreeBiz(degreeVo,"");			
			 
			//获取档位层数
			List<Integer> list = new ArrayList<Integer>();
			for(LfDegreeManageVo lfdegree : chaVoList ){
				if(lfdegree.getDegree() != null ){
					   list.add(lfdegree.getDegree());  
				}
			}
			Collections.sort(list);
			Integer[] str = list.toArray(new Integer[list.size()]); 
			list.clear();
			for (int i=0; i<str.length; i++) {    
				if(!list.contains(str[i])) {    
					list.add(str[i]);    
			    }    
			}
			request.setAttribute("chaDegree", list);
			request.setAttribute("isEmpty", isEmpty);
			request.setAttribute("hisRecordList", hisRecordList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("hisrecord", hisrecord);
			request.setAttribute("spList", spList);
			
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "发送明细查询失败！");
		}finally{
			Long endTime = System.currentTimeMillis();
			Integer listSize = hisRecordList == null ? 0 : hisRecordList.size();
			String opContent = "企业富信-数据查询-发送明细查询。耗时：" + (endTime - startTime)/1000 + "s,操作员Id：" + lguserid +
					",企业编码：" + corpCode + ",SP账号：" + userid + ",运营商：" + operator + ",手机号：" + phone + ",模板Id：" + tmplid +
					",查询开始时间：" + starttime + ",查询结束时间：" + endtime + "富信档位：" + chgrade + "查询结果条数：" + listSize;
			EmpExecutionContext.info(opContent);
			//opSucLog(request, "发送明细", opContent, "GET");
			try {
				request.getRequestDispatcher("rms/detailsend/rms_sendDetail.jsp").forward(request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "发送明细查询失败！");
			} 
		}
	}

    /**
     * 发送明细查询---运营商富信
     * @param request
     * @param response
     */
    public void findOperatorRms(HttpServletRequest request, HttpServletResponse response){
        //按钮权限Map
        Map<String,String> btnMap = (Map<String,String>)request.getSession(false).getAttribute("btnMap");
        //记录耗时
        Long startTime = System.currentTimeMillis();
        // 记录类型
        String recordType = request.getParameter("recordType");
        //任务批次
        String taskId = request.getParameter("taskId");
        //手机号码
        String phone = request.getParameter("phone");
        //状态码
        String statusCode = request.getParameter("statusCode");
        //下载状态
        String downStatus = request.getParameter("downStatus");
        //发送主题
        String sendSubject = request.getParameter("sendSubject");
        //富信主题
        String rmsSubject = request.getParameter("rmsSubject");
        //业务类型
        String busType = request.getParameter("busType");
        //SP账号
        String spUser = request.getParameter("spUser");
        //开始时间
        String sendTime = request.getParameter("sendTime");
        //结束时间
        String recvTime = request.getParameter("recvTime");
        //企业编码
        String corpCode = request.getParameter("lgcorpcode");
        //操作员Id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        //操作员名字
        String lgusername = "";
        //分页对象
        PageInfo pageInfo = new PageInfo();
        // sp账号查询结果集
        List<LfSpDepBind>  spUserList = null;
        //业务类型
        List<LfBusManager> busTypeList = null;
        //用于数据回显的Map
        Map<String,String> queryData = new HashMap<String, String>(16);
        //结果集
        List<RmsMtRecordVo> rmsMtRecords = null;
        // 业务类型Map<业务编码，业务名称>
        Map<String, String> busTypeMap = new HashMap<String, String>();
        try {
            //如果页面上传回的corpCode或lguserid为空则从session获取
            LfSysuser loginSysUser = detailSendBiz.getCurrenUser(request);
            lgusername = loginSysUser.getName();
            if(StringUtils.isEmpty(corpCode) || StringUtils.isEmpty(lguserid)){
                if(loginSysUser == null){
                    throw new EMPException(MODNAME + "," + OPNAME + ",从session中获取当前登录对象出现异常");
                }
                corpCode = loginSysUser.getCorpCode();
                lguserid = loginSysUser.getUserId().toString();
            }
            //判断SP账号是否是属于本企业的
            if(spUser != null && !"".equals(spUser.trim())) {
                //多企业且不是10W号才处理
                if(StaticValue.getCORPTYPE() == 1 && !empCorpCode.equals(corpCode)){
                    boolean checkFlag = new CheckUtil().checkSysuserInCorp(loginSysUser, corpCode, spUser, null);
                    if(!checkFlag){
                        throw new EMPException(MODNAME + "," + OPNAME + ",该SP账号" + spUser + "不属于本企业！");
                    }
                }
            }
            // 查询sp账号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 获取业务类型
            if(!"100000".equals(corpCode)) {
                // 只显示自定义业务
                conditionMap.put("corpCode&in", "0," + corpCode);
            }
            else {
                conditionMap.put("corpCode&not in", "1,2");
            }
            busTypeList = baseBiz.getByCondition(LfBusManager.class, conditionMap, null);
            if(busTypeList == null){
                busTypeList = new ArrayList<LfBusManager>();
            }
            for(LfBusManager businessType : busTypeList){
                busTypeMap.put(businessType.getBusCode(), businessType.getBusName());
            }
            conditionMap.clear();

            //如果是梦网侧查询所有企业发送账号
            if(!empCorpCode.equals(corpCode)){
                conditionMap.put("corpCode",corpCode);
            }

            spUserList = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);





            pageSet(pageInfo,request);

            //如果不指定查询类型则默认为实时查询
            recordType = StringUtils.defaultIfEmpty(recordType,"realTime");

            //将查询条件放在一个Map中传回页面用于数据回显
            queryData.put("recordType",recordType);
            queryData.put("taskId",taskId);
            queryData.put("phone",phone);
            queryData.put("statusCode",statusCode);
            queryData.put("downStatus",downStatus);
            queryData.put("sendSubject",sendSubject);
            queryData.put("rmsSubject",rmsSubject);
            queryData.put("busType",busType);
            queryData.put("spUser",spUser);
            queryData.put("sendTime",sendTime);
            queryData.put("recvTime",recvTime);

            conditionMap.clear();
            //当前操作员id
            conditionMap.put("curUserId", lguserid);
            //当前企业编码
            conditionMap.put("curCorpCode", corpCode);
            //当前操作员编码
            conditionMap.put("curUserCode", "'" + loginSysUser.getUserCode() + "'");
            //设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
            String userCode = detailSendBiz.getPermissionUserCode(loginSysUser);
            if(userCode != null) {
                //有权限看的操作员编码
                conditionMap.put("domUserCode", userCode);
            }
            //获取有权限看的SP账号
            String spUserPri = detailSendBiz.getPermissionSpuserMtpri(loginSysUser);
            if(spUserPri != null){
                //有权限看的sp账号
                conditionMap.put("spUserPri", spUserPri);
            }

            // 企业绑定的发送账户，多企业限制查询范围用到
            String spUsers = detailSendBiz.getCorpBindSpusers(corpCode);

            //sp账号条件
            conditionMap.put("spUsers", spUsers);

            //查询下行记录
            conditionMap.putAll(queryData);
            rmsMtRecords = detailSendBiz.getRmsMtRecords(conditionMap, pageInfo);
            //将conditionMap存入cookie中，方便导出报表功能拿到数据
            String rptVoJson = URLEncoder.encode(JSONObject.toJSONString(conditionMap),"utf-8");
            Cookie detailRptVo = new Cookie("sendDetailMap",rptVoJson);
            response.addCookie(detailRptVo);
            //处理结果集
            rmsMtRecords = completeRmsMtRecordVoList(rmsMtRecords, btnMap, busTypeMap);
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业富信-数据查询-发送明细查询，点击查询按钮查询结果出现异常！");
        }finally {
            Integer countSize = rmsMtRecords == null ? 0:rmsMtRecords.size();
            String conditionStr = "查询类型=" + queryData.get("recordType") + ",SP账号=" + queryData.get("spUser")
                    + ",任务Id=" + queryData.get("taskId") + ",业务类型=" + queryData.get("busType")
                    + ",富信主题=" + queryData.get("rmsSubject") + ",手机号码=" + queryData.get("phone")
                    + ",发送主题=" + queryData.get("sendSubject") + ",发送时间=" + queryData.get("sendTime")
                    + ",状态码=" + queryData.get("statusCode") + ",下载状态=" + queryData.get("downStatus")
                    + ",查询结束时间=" + queryData.get("recvTime") + ",查询总数=" + countSize;
            request.setAttribute("spList", spUserList);
            request.setAttribute("busTypeList", busTypeList);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("queryDataMap", queryData);
            request.setAttribute("rmsMtRecords", rmsMtRecords);
            request.setAttribute("countSize", countSize);
            request.setAttribute("isRptFlag", this.isRptFlag(request));
            request.setAttribute("busTypeMap", busTypeMap);
            //记录日志
            String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
            String opContent = OPNAME + "," + recordType + " totalcount:" + countSize + "条 ,查询时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - startTime)+"ms,条件:"+ conditionStr;
            EmpExecutionContext.info(MODNAME, corpCode, lguserid, lgusername, opContent, StaticValue.GET);
            try {
                request.getRequestDispatcher("rms/detailsend/rms_sendDetail.jsp").forward(request, response);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询-跳转页面异常！");
            }
        }
    }

    /**
     * 处理结果集
     * @param rmsMtRecords 原始结果集
     * @return 处理后的结果集
     */
    private List<RmsMtRecordVo> completeRmsMtRecordVoList(List<RmsMtRecordVo> rmsMtRecords, Map<String,String> btnMap, Map<String,String> busTypeMap){
        CommonVariables cv = new CommonVariables();
        if(rmsMtRecords == null || rmsMtRecords.size() == 0){
            return rmsMtRecords;
        }
        //处理结果
        for(RmsMtRecordVo vo:rmsMtRecords){
            //运营商
            String unicomName;
            switch (vo.getUnicom()){
                case 0:unicomName = "移动";break;
                case 21:unicomName = "电信";break;
                case 5:unicomName = "国外";break;
                case 1:unicomName = "联通";break;
                default:unicomName  = "-";
            }
            vo.setUnicomName(unicomName);
            //手机号
            String phoneNum = cv.replacePhoneNumber(btnMap, vo.getPhone());
            vo.setPhone(phoneNum);
            //自定义流水号 优先显示新版的自定义流水号custid，其次再显示旧版的usermsgid
            if(StringUtils.isEmpty(vo.getCustId()) && vo.getUserMsgId() != null && vo.getUserMsgId() != 0){
                vo.setCustId(vo.getUserMsgId().toString());
            }
            vo.setBusTypeName(busTypeMap.get(vo.getSvrtype()));
            //时间处理
            String timeStr = vo.getDownTime();
            if(StringUtils.isNotEmpty(timeStr)){
                vo.setDownTime(timeStr.substring(0,19));
            }
            timeStr = vo.getRecvTime();
            if(StringUtils.isNotEmpty(timeStr)){
                vo.setRecvTime(timeStr.substring(0,19));
            }
            timeStr = vo.getSendTime();
            if(StringUtils.isNotEmpty(timeStr)){
                vo.setSendTime(timeStr.substring(0,19));
            }
            //接收状态
            String errorCode = vo.getErrorCode();
            if(StringUtils.isBlank(errorCode)){
                vo.setReceStatus("未返");
            }else if("DELIVRD".equals(errorCode) || "0".equals(errorCode)){
                vo.setReceStatus("成功");
            }else {
                vo.setReceStatus("失败["+ errorCode +"]");
            }
            //下载状态
            String errorCode2 = vo.getErrorCode2();
            if(StringUtils.isEmpty(errorCode2)){
                vo.setDownStatus("未返");
            }else {
                vo.setDownStatus("DELIVRD".equals(errorCode)?"成功":"失败["+ errorCode2 +"]");
            }
        }
        return rmsMtRecords;
    }
    /**
     * 模板预览
     * @param request
     * @param response
     */
    public void tempPreview(HttpServletRequest request, HttpServletResponse response){
        // 模板Id(网关返回Id)
        String tmId = request.getParameter("tmId");
        BufferedReader br = null;
        try {
            if(StringUtils.isEmpty(tmId)){
                throw new Exception(" 数据查询 > 数据查询 > 发送明细查询 > 模板预览异常 > tmId参数为空");
            }
            //根据mtId查询lf_template
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("sptemplid",tmId);
            List<LfTemplate> list = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
            if(list.size() == 0){
                throw new Exception(" 数据查询 > 数据查询 > 发送明细查询 > 模板预览异常 > 根据此tmId:"+ tmId +"找不到相应模板记录！");
            }
            //获取url
            String url = list.get(0).getTmMsg();
            //获取标题
            String tmName = list.get(0).getTmName();
            if(StringUtils.isEmpty(url)){
                throw new Exception(" 数据查询 > 数据查询 > 发送明细查询 > 模板预览异常 > 根据此tmId:"+ tmId +"找不到相应模板的文件路径！");
            }
            // 地址处理
            String filePath = fileUtil.getWebRoot() + url.replace("fuxin.rms", "fuxinPreview.html");
            File file = new File(filePath);
            StringBuilder htmlStr = new StringBuilder();
            if(!file.exists() || !file.isFile()){
                throw new Exception(" 数据查询 > 数据查询 > 发送明细查询 > 模板预览异常 > 预览文件路径:'"+ filePath +"'不存在或不是文件！");
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                htmlStr.append(lineTxt);
            }
            //插入标题
            htmlStr.insert(0, "<h4 style='margin-bottom: 10px;font-size: 14px;text-align: center;'>"+tmName+"</h4>");
            String finalStr = htmlStr.toString().replace("\r\n", "&lt;BR/&gt;").replace("\n", "&lt;BR/&gt;");
            response.getWriter().print(finalStr);
        }catch (Exception e){
            EmpExecutionContext.error(e,e.getMessage());
            try {
                response.getWriter().print("源文件不存在");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1,"数据查询 > 数据查询 > 发送明细查询 > 模板预览异常 > response.getWriter()方法异常！");
            }
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e,"数据查询 > 数据查询 > 发送明细查询 > 模板预览异常 > 关闭IO对象异常！");
                }
            }
        }
    }
    /**
     * 记录操作成功日志
     * @param request
     * @param modName 模块名称
     * @param opContent 操作详情
     * @param opType 操作类型 ADD UPDATE DELETE GET OTHER
     * @date 2018-1-25上午11:29:50
     */
    public void opSucLog(HttpServletRequest request, String modName, String opContent, String opType) {
        LfSysuser lfSysuser = new LfSysuser();
        try {
            Object obj = request.getSession(false).getAttribute("loginSysuser");
            if (obj == null)
                return;
            lfSysuser = (LfSysuser) obj;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "记录操作日志异常，session为空！");
        }
        EmpExecutionContext.info(modName, lfSysuser.getCorpCode(),
                String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(),
                opContent, opType);
    }
    /**
     * 发送明细详情报表导出
     * @param request
     * @param response
     */
    public void getSendDetailExcel(HttpServletRequest request, HttpServletResponse response){
        //按钮权限Map
        Map<String,String> btnMap = (Map<String,String>)request.getSession(false).getAttribute("btnMap");
        //记录日志
        LfSysuser loginSysUser = detailSendBiz.getCurrenUser(request);
        // 查询条件对象
        LinkedHashMap conditionMap = new LinkedHashMap<String,String>();
        //起始ms数
        long start = System.currentTimeMillis();
        //返回结果
        String result = "false";
        //结果集
        List<RmsMtRecordVo> rmsMtRecords = null;
        try {
            //从cookie中取值
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies){
                if("sendDetailMap".equals(cookie.getName())){
                    String degreeRptVo = cookie.getValue();
                    String rptVoStr = URLDecoder.decode(degreeRptVo,"utf-8");
                    conditionMap = JSONObject.parseObject(rptVoStr, LinkedHashMap.class);
                }
            }
            if(conditionMap == null || conditionMap.size() == 0){
                throw new EMPException(MODNAME + "-" + OPNAME  + "，报表导出功能异常，不存在对应的COOKIE值！");
            }
            rmsMtRecords = detailSendBiz.getRmsMtRecords(conditionMap, null);
            conditionMap.clear();
            // 获取业务类型
            String corpCode = loginSysUser.getCorpCode();
            if(!"100000".equals(corpCode)) {
                // 只显示自定义业务
                conditionMap.put("corpCode&in", "0," + corpCode);
            }
            else {
                conditionMap.put("corpCode&not in", "1,2");
            }
            List<LfBusManager> busTypeList = baseBiz.getByCondition(LfBusManager.class, conditionMap, null);
            if(busTypeList == null){
                busTypeList = new ArrayList<LfBusManager>();
            }
            Map<String, String> busTypeMap = new HashMap<String, String>();
            for(LfBusManager businessType : busTypeList){
                busTypeMap.put(businessType.getBusCode(), businessType.getBusName());
            }
            //处理结果集
            rmsMtRecords = completeRmsMtRecordVoList(rmsMtRecords, btnMap, busTypeMap);
            if(rmsMtRecords.size() > 0 && rmsMtRecords.size() < 500000){

                Map<String, String> languageMap = new HashMap<String, String>();
                languageMap.put("spAccount", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_spaccount", request), "SP账号"));
                languageMap.put("spGate", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_spgate", request), "通道"));
                languageMap.put("sendTopic", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_sendtopic", request), "发送主题"));
                languageMap.put("busType", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_bstype", request), "业务类型"));
                languageMap.put("fxTopic", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_fuxintopic", request), "富信主题"));
                languageMap.put("sceneId", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_sceneid", request), "场景ID"));
                languageMap.put("degree", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_degree", request), "档位"));
                languageMap.put("operator", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_operator", request), "运营商"));
                languageMap.put("phone", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_phonenum", request), "手机号码"));
                languageMap.put("taskbatch", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_taskbatch", request), "任务批次"));
                languageMap.put("recstatus", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_recstatus", request), "接收状态"));
                languageMap.put("sendtime", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_sendtime", request), "发送时间"));
                languageMap.put("recetime", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_rectime", request), "接收时间"));
                languageMap.put("selfseq", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_selfseq", request), "自定义流水号"));
                languageMap.put("optseq", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_operatorseq", request), "运营商流水号"));
                languageMap.put("dlstatus", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_dlstatus", request), "下载状态"));
                languageMap.put("dltime", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_dltime", request), "下载时间"));

                HashMap<String,String> map = detailSendBiz.createRptExcelFile(rmsMtRecords,this.isRptFlag(request), languageMap);
                request.getSession(false).setAttribute("sendDetail_excel_map",map);
                result = "true";
            }
        }catch (EMPException empEx){
            EmpExecutionContext.error(empEx,empEx.getMessage());
        } catch (Exception e){
            EmpExecutionContext.error(e,MODNAME + "-" + OPNAME + ",报表导出功能异常！");
        }finally {
            String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
            String opContent = MODNAME + "-" + MODNAME + ",报表导出,导出时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - start)+"ms";
            EmpExecutionContext.info("报表导出功能", loginSysUser.getCorpCode(), loginSysUser.getUserId().toString(), loginSysUser.getName(), opContent, StaticValue.GET);
            try {
                response.getWriter().write(result);
            } catch (Exception e) {
                EmpExecutionContext.error(e, MODNAME + "," + MODNAME + ",报表导出功能异常,跳转页面异常！");
            }
        }
    }
    /**
     * 发送明细详情报表下载文件
     * @param request
     * @param response
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("sendDetail_excel_map");
        session.removeAttribute("sendDetail_excel_map");
        if(obj != null){
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String filePath = resultMap.get("filePath");
            String fileName = resultMap.get("fileName");
            dfs.downFile(request, response, filePath, fileName);
        }
    }

    /**
     * 判断当前企业是否需要下载状态字段
     */
    public String isRptFlag(HttpServletRequest request){

        //加载排序条件
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        //查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        PageInfo pageInfo=new PageInfo();
        try {

            //登录操作员信息
            LfSysuser loginSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            String code=loginSysuser.getCorpCode();
            if(empCorpCode.equals(code)){
                return "3";//10万号
            }else{
                if(code != null && !"".equals(code))
                {
                    code = URLDecoder.decode(code, "UTF-8");
                    conditionMap.put("corpCode", code);
                }
                List<LfCorp> list = baseBiz.getByConditionNoCount(LfCorp.class, null, conditionMap,
                        orderbyMap, pageInfo);
                if(list.size()>0){
                    // 0:表示不需要;1:需要通知状态报告;2:需要下载状态报告;3:通知、下载状态报告都要;(默认通知状态报告必须)
                    return list.get(0).getRptflag().toString();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e,"error");
        }
        return "";
    }

}
