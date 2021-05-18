package com.montnets.emp.query.thread;


import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasgrpbind.LfAccountBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.ReportMtBiz;
import com.montnets.emp.util.CSVUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 异步生成csv格式的下行记录报表
 * @date 20181204
 * @author yangbo
 */
public class MtReportThread implements Runnable {

    private LinkedHashMap<String, String> conditionMap;

    private Map<String, Object> paramterMap;

    /**
     * 导出文件的保存目录
     */
    private static final String BASE_DIR = "cxtj"+ File.separator +"query"+ File.separator +"file"+ File.separator +"report";

    /**
     * 导出数量限额，如果配置文件里面没配置，取这里的默认值
     */
    private static final String MT_REPORT_LIMIT = "1000000";

    private ReportMtBiz iReportMtBiz = new ReportMtBiz();

    private CommonVariables commonVariables = new CommonVariables();

    public MtReportThread(LinkedHashMap<String, String> conditionMap, Map<String, Object> paramterMap){
        this.conditionMap = conditionMap;
        this.paramterMap = paramterMap;
    }

    @Override
    public void run()
    {
        long start=System.currentTimeMillis();
        LfSysuser sysuser = (LfSysuser) paramterMap.get("user");
        //获取当前登录用户
        if(sysuser == null)
        {
            EmpExecutionContext.error("下行记录导出,session中获取当前登录对象为空");
            return;
        }
        int count = 0;
        //获取文件id
        Long fileid = (Long) paramterMap.get("fileid");
        try
        {
            //分页对象
            PageInfo pageInfo = new PageInfo();
            //从配置文件读取导出记录的限制数量
            //String limit = SystemGlobals.getValue("montnets.cxtj.mt.report.limit");
            //从缓存中读取导出记录的限制数量 modify by tanglili 20210201
            String limit=SystemGlobals.getSysParam("cxtjMtExportLimit");
            pageInfo.setPageSize(Integer.parseInt(StringUtils.isEmpty(limit) ? MT_REPORT_LIMIT : limit));
            //查询需要导出的下行记录
            List<DynaBean> mtTaskList = iReportMtBiz.getMtRecords(conditionMap, pageInfo);
            if(mtTaskList == null || mtTaskList.isEmpty())
            {
                EmpExecutionContext.error("下行记录导出,查询的下行记录为空");
                iReportMtBiz.updateRptRecord(fileid.toString(),"2",false,false);
                return;
            }
            //过滤和转换集合中数据
            List<DynaBean> finalMtTasks = filterMtTaskList(sysuser, mtTaskList);
            if(finalMtTasks == null || finalMtTasks.isEmpty()) {
                EmpExecutionContext.error("下行记录导出,转换数据异常。");
                iReportMtBiz.updateRptRecord(fileid.toString(),"2",false,false);
                return;
            }
            count = finalMtTasks.size();

            String filePath = new TxtFileUtil().getWebRoot() + BASE_DIR + File.separator + fileid;
            //生成CSV文件
            File file = CSVUtils.createCSVFile(finalMtTasks, this.getHeadMap(), filePath, conditionMap.get("fileName"));
            if(file.exists() && file.isFile()) {
                if(StaticValue.getISCLUSTER() == 1) {
                    //上传文件到文件服务器
                    CommonBiz comBiz = new CommonBiz();
                    boolean upFileRes = false;
                    //最大尝试次数
                    int retryTime = 3;
                    while (!upFileRes && retryTime-- >0)
                    {
                        upFileRes = comBiz.upFileToFileServer(filePath);
                    }
                }
                //文件生成成功
                iReportMtBiz.updateRptRecord(fileid.toString(),"0",true,false);
            }else
            {
                EmpExecutionContext.error("导出文件写入本地失败。");
                iReportMtBiz.updateRptRecord(fileid.toString(),"2",false,false);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "导出系统下行记录，异常。");
            try
            {
                iReportMtBiz.updateRptRecord(fileid.toString(),"2",false,false);
            } catch (SQLException e1)
            {
                EmpExecutionContext.error(e, "更新导出记录异常。");
            }
        }
        finally
        {
            String conditionstr = "userid=" + conditionMap.get("userid") + ",bindspUserid=" + conditionMap.get("spUsers")
                    + ",spgate=" + conditionMap.get("spgate") + ",buscode=" + conditionMap.get("buscode")
                    + ",spisuncm=" + conditionMap.get("spisuncm") + ",phone=" + conditionMap.get("phone")
                    + ",taskid=" + conditionMap.get("taskid") + ",sendtime=" + conditionMap.get("sendtime")
                    + ",recvtime=" + conditionMap.get("recvtime");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String starthms = timeFormat.format(start);
            String opContent = "导出系统下行记录"+conditionMap.get("recordType")+" totalcount:" + count + "条 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-start)+"ms,条件:"+conditionstr;
            EmpExecutionContext.info("查询统计", sysuser.getCorpCode(), conditionMap.get("userid"), sysuser.getUserName(), opContent, StaticValue.GET);
        }
    }

    /**
     * 过滤查询到的所有下行记录
     * @param sysuser
     * @param mtTaskList
     */
    private List<DynaBean> filterMtTaskList(LfSysuser sysuser,List<DynaBean> mtTaskList) {
        //获取该用户下绑定的SP账号
        LinkedHashMap<String, String> spAccMap = this.getSpAccMap(sysuser);
        //获取错误码描述的映射关系
        Map<String,String> errCodeDesMap = this.getErrCodeDisMap(sysuser);
        //获取业务类型映射关系
        LinkedHashMap<String, String> busiTypeMap = this.getBusiTypeMap(sysuser.getCorpCode());
        //转换后的下行结果集
        List<DynaBean> finalMtTasks = new ArrayList<DynaBean>();
        DynaBean finalDynaBean;
        String phoneNum;
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String recordType = conditionMap.get("recordType");
        int showNum = sysuser.getShowNum();
        try
        {
            for(DynaBean dynaBean : mtTaskList)
            {
                finalDynaBean = new LazyDynaBean();
                finalDynaBean.set("message",dynaBean.get("message").toString());
                //不显示数字
                if(showNum == 1) {
                    //如果SP账号集合里不存在该sp账号,则数字替换成*
                    if(!spAccMap.containsKey(dynaBean.get("userid").toString().toLowerCase()))
                    {
                        String pat = "\\d+" ;
                        Pattern p = Pattern.compile(pat);
                        Matcher m = p.matcher(dynaBean.get("message").toString());
                        String str = m.replaceAll("*") ;
                        finalDynaBean.set("message",str);
                    }
                }

                finalDynaBean.set("errorcode", dynaBean.get("errorcode"));
                //转换查询结果中的错误码
                if(errCodeDesMap != null){
                    if(errCodeDesMap.containsKey(dynaBean.get("errorcode")))
                    {
                        finalDynaBean.set("errorcode", errCodeDesMap.get(dynaBean.get("errorcode")));
                    }
                    if("DELIVRD".equals(dynaBean.get("errorcode")))
                    {
                        finalDynaBean.set("errorcode", (String) paramterMap.get("cxtj_sjcx_xtxxdc_cg"));
                    }
                }

                finalDynaBean.set("svrtype", dynaBean.get("svrtype").toString());
                //转换结果中的业务类型
                if(busiTypeMap != null){
                    if(busiTypeMap.containsKey(dynaBean.get("svrtype")))
                    {
                        finalDynaBean.set("svrtype", busiTypeMap.get(dynaBean.get("svrtype")));
                    }
                }

                //优先显示新版的自定义流水号custid，其次再显示旧版的usermsgid
                boolean conditionA = dynaBean.get("custid") == null || dynaBean.get("custid").toString().trim().equals("");
                boolean conditionB = dynaBean.get("usermsgid") != null && !"0".equals(dynaBean.get("usermsgid").toString());
                if(conditionA && conditionB){
                    finalDynaBean.set("usermsgid", dynaBean.get("usermsgid").toString());
                }else{
                    if(conditionA){
                        finalDynaBean.set("usermsgid", "-");
                    }else{
                        finalDynaBean.set("usermsgid", dynaBean.get("custid").toString());
                    }
                }

                //转换运营商名称
                finalDynaBean.set("unicom",this.getUnicomName(Integer.valueOf(dynaBean.get("unicom").toString())));

                //转换发送时间格式
                finalDynaBean.set("sendtime", "-");
                if(dynaBean.get("sendtime") != null) {
                    finalDynaBean.set("sendtime", dateTimeFormat.format(dynaBean.get("sendtime")));
                }

                //转换接收时间格式
                finalDynaBean.set("recvtime", "-");
                if(dynaBean.get("recvtime") != null)
                {
                    finalDynaBean.set("recvtime", dateTimeFormat.format(dynaBean.get("recvtime")));
                }

                //组装分条
                String pknumber = dynaBean.get("pknumber") == null ? "-" : dynaBean.get("pknumber").toString();
                String pktotal = dynaBean.get("pktotal") == null ? "-" : dynaBean.get("pktotal").toString();
                finalDynaBean.set("pknumAndPktotal", pknumber + "/" + pktotal);

                //组装spgate
                String spgate = dynaBean.get("spgate") == null ? "-" : dynaBean.get("spgate").toString();
                String cpno = dynaBean.get("cpno") == null ? "" : dynaBean.get("cpno").toString();
                finalDynaBean.set("spgate", spgate + cpno);
                finalDynaBean.set("userid", dynaBean.get("userid"));

                //判断电话号码是否可见
                phoneNum = dynaBean.get("phone") == null ? "-" : commonVariables.replacePhoneNumber((Map<String,String>) paramterMap.get("btnMap"), dynaBean.get("phone").toString());
                finalDynaBean.set("phone", phoneNum);
                finalDynaBean.set("taskid", dynaBean.get("taskid"));
                if("realTime".equals(recordType)){
                    //实时表才有这个值
                    finalDynaBean.set("msgfmt", dynaBean.get("msgfmt"));
                }else{
                    finalDynaBean.set("msgfmt", "-");
                }
                finalMtTasks.add(finalDynaBean);
            }
            return finalMtTasks;
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "导出系统下行记录数据转换，异常。");
            return null;
        }
    }

    /**
     * 转换运营商名称
     * @param unicom
     * @return
     */
    private String getUnicomName(int unicom)
    {
        String unicomName = (String) ((unicom - 0 == 0) ? paramterMap.get("cxtj_sjcx_dxzljl_yd")
                        : (unicom - 1 == 0 ? paramterMap.get("cxtj_sjcx_dxzljl_lt")
                        : (unicom - 21 == 0 ? paramterMap.get("cxtj_sjcx_dxzljl_dx")
                        : (unicom - 5 == 0 ? paramterMap.get("cxtj_sjcx_report_gw") :"-"))));
        return unicomName;
    }

    /**
     * 查询错误码集合转换为map
     * @param sysuser
     * @return
     */
    private LinkedHashMap<String, String> getErrCodeDisMap(LfSysuser sysuser)
    {
        List<DynaBean> beanList = iReportMtBiz.getErrCodeDis(sysuser.getCorpCode());
        LinkedHashMap<String,String> errCodeDisMap = new LinkedHashMap<String,String>();
        if(null != beanList && beanList.size() > 0){
            for(DynaBean bean : beanList) {
                if(bean.get("state_code") == null || bean.get("state_des") == null) {
                    continue;
                }
                errCodeDisMap.put(bean.get("state_code").toString(), bean.get("state_des").toString());
            }
        }
        return errCodeDisMap;
    }

    /**
     * 查询sp账号集合转换为Map
     * @param sysuser
     * @return
     */
    private LinkedHashMap<String, String> getSpAccMap(LfSysuser sysuser)
    {
        //获取该用户下绑定的SP账号
        List<LfAccountBind> accountBindList = iReportMtBiz.getLfAccountBinds(sysuser.getGuId());
        LinkedHashMap<String, String> spAccMap = new LinkedHashMap<String, String>();
        for (LfAccountBind spAcc : accountBindList) {
            spAccMap.put(spAcc.getSpuserId().toLowerCase(),"");
        }
        return spAccMap;
    }

    /**
     * 查询业务类型集合转换为Map
     * @param lgcorpcode 企业编码
     * @return 成功返回true
     */
    private LinkedHashMap<String, String> getBusiTypeMap(String lgcorpcode)
    {
        try
        {
            // 查询条件
            LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
            // 获取业务类型
            if(!"100000".equals(lgcorpcode))
            {
                // 只显示自定义业务
                conditionMMap.put("corpCode&in", "0," + lgcorpcode);
            }
            else
            {
                conditionMMap.put("corpCode&not in", "1,2");
            }

            BaseBiz baseBiz	= new BaseBiz();
            List<LfBusManager> busiList = baseBiz.getByCondition(LfBusManager.class, null, conditionMMap, null);
            LinkedHashMap<String, String> busiMap = new LinkedHashMap<String, String>();
            if(busiList != null && busiList.size() > 0)
            {
                for (LfBusManager bus : busiList)
                {
                    busiMap.put(bus.getBusCode(), bus.getBusName());
                }
            }
            return busiMap;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行记录，获取并设置业务类型，异常。");
            return null;
        }
    }

    /**
     * 封装csv文件的猎头，根据导出选项，选择的表头进行过滤
     * @return
     */
    private LinkedHashMap<String, String> getHeadMap()
    {
        LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
        String [] columns = (String[]) paramterMap.get("columns");
        List<String> columnList = Arrays.asList(columns);
        headMap.put("userid", (String) paramterMap.get("cxtj_sjcx_xtxxdc_spzh"));
        headMap.put("spgate",(String) paramterMap.get("cxtj_sjcx_xtxxdc_tdhm"));
        headMap.put("unicom",(String) paramterMap.get("cxtj_sjcx_xtxxdc_yys"));
        headMap.put("phone",(String) paramterMap.get("cxtj_sjcx_xtxxdc_sjhm"));
        headMap.put("taskid",(String) paramterMap.get("cxtj_sjcx_xtxxdc_rwpc"));
        headMap.put("errorcode",(String) paramterMap.get("cxtj_sjcx_xtxxdc_ztbg"));
        headMap.put("sendtime",(String) paramterMap.get("cxtj_sjcx_xtxxdc_fssj"));
        headMap.put("recvtime",(String) paramterMap.get("cxtj_sjcx_xtxxdc_jssj"));
        headMap.put("svrtype",(String) paramterMap.get("cxtj_sjcx_xtxxdc_ywlx"));
        headMap.put("pknumAndPktotal", (String) paramterMap.get("cxtj_sjcx_xtxxdc_ft"));
        headMap.put("message", (String) paramterMap.get("cxtj_sjcx_xtxxdc_dxnr"));
        headMap.put("usermsgid",(String) paramterMap.get("cxtj_sjcx_xtxxdc_zdylsh"));
        headMap.put("msgfmt", (String) paramterMap.get("cxtj_sjcx_xtxxdc_bm"));

        for (Iterator<Map.Entry<String,String>> it = headMap.entrySet().iterator(); it.hasNext();)
        {
            if(!columnList.contains(it.next().getKey()))
            {
                it.remove();
            }
        }
        return headMap;
    }

}
