/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-5 下午02:57:26
 */
package com.montnets.emp.mobilebus.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.ydyw.LfContractTaocan;
import com.montnets.emp.entity.ydyw.LfDeductionsDisp;
import com.montnets.emp.entity.ydyw.LfDeductionsList;
import com.montnets.emp.jfcx.biz.CrmRfMgrBiz;
import com.montnets.emp.mobilebus.constant.MobileBusStaticValue;
import com.montnets.emp.mobilebus.dao.BuckleFeeDAO;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.DynaBean;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chentingsheng <cts314@163.com>
 * @description
 * @project p_ydyw
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-5 下午02:57:26
 */

public class BuckleFeeBiz extends SuperBiz {


    BuckleFeeDAO buckleFeeDAO = new BuckleFeeDAO();

    TxtFileUtil txtfileutil = new TxtFileUtil();

    /**
     * 扣费定时任务
     *
     * @param firstTime
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2014-12-31 下午02:03:22
     */
    public void executeBuckleFeeTask(Date firstTime) {
        // 定时器
        Timer timer = new Timer();

        // 定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //处理未返状态报告
                try {
                    rptWaitOutTime();
                } catch (SQLException e) {
                    EmpExecutionContext.error(e, "处理状态报告超时发生异常！");
                }
                //扣费流程
                try {
                    buckleFee();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "扣费流程发生异常！");
                }

                //欠费补扣流程
                SupplementDeductFeeBiz sdfBiz = new SupplementDeductFeeBiz();
                try {
                    sdfBiz.supplementDeductFee();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "欠费补扣流程发生异常！");
                }

                //处理扣费返回结果
                reBuckleFeeHandle();
            }
        };
        // 启动定时任务
        timer.schedule(timerTask, firstTime, 86400000L);
    }

    /**
     * 欠费补扣定时任务
     *
     * @param firstTime
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2014-12-31 下午02:03:32
     */
    public void executeBuckleUpFeeTask(Date firstTime) {
        // 定时器
        Timer timer = new Timer();

        // 定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // 欠费补扣流程
                buckleUpFee();
            }
        };
        // 启动定时任务
        timer.schedule(timerTask, firstTime, 86400000L);
    }

    /**
     * 获取定时任务执行时间
     *
     * @return 扣费任务及欠费补扣任务执行时间
     * 一维下标：0为扣费任务；1为欠费补扣任务；二维下标：0为小时；1为分钟
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2014-12-31 下午02:04:44
     */
    public int[][] getTaskExecuteTime() {
        try {
            // 一维下标：0为扣费任务；1为欠费补扣任务；二维下标：0为小时；1为分钟
            int[][] TaskExecuteTime = {{-1, -1}, {-1, -1}};
            List<DynaBean> taskDateList = buckleFeeDAO.getBillingTaskTime();
            int[] time = new int[2];
            if (taskDateList != null && taskDateList.size() > 0) {
                for (DynaBean taskDate : taskDateList) {
                    String globalId = taskDate.get("globalid").toString();
                    String globalStrValue = taskDate.get("globalstrvalue").toString();
                    if (globalId != null && globalStrValue != null) {
                        // 扣费定时任务时间
                        if ("38".equals(globalId)) {
                            time = getexecuteTime(globalId, globalStrValue);
                            TaskExecuteTime[0] = time;
                        }
                        // 欠费补扣定时任务时间
                        else if ("39".equals(globalId)) {
                            time = getexecuteTime(globalId, globalStrValue);
                            TaskExecuteTime[1] = time;
                        }
                    }
                }
            }
            return TaskExecuteTime;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取定时任务执行时间异常！");
            return null;
        }
    }

    /**
     * 时间转换
     *
     * @param globalId
     * @param globalStrValue
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-5 上午11:01:24
     */
    public int[] getexecuteTime(String globalId, String globalStrValue) {
        int hour = -1;
        int minute = -1;
        String hourStr = "";
        String minuteStr = "";

        if (globalStrValue.indexOf(":") > -1) {
            hourStr = globalStrValue.split(":")[0];
            minuteStr = globalStrValue.split(":")[1];
        }
        try {
            hour = Integer.parseInt(hourStr);
            if (hour > 23 || hour < 0) {
                hour = -1;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "转换任务执行时间(小时)异常，hourStr:" + hourStr);
            hour = -1;
        }
        try {
            minute = Integer.parseInt(minuteStr);
            if (minute > 59 || minute < 0) {
                minute = -1;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "转换任务执行时间(分钟)异常，minuteStr:" + minuteStr);
            minute = -1;
        }
        int[] time = new int[2];
        time[0] = hour;
        time[1] = minute;
        return time;
    }

    /**
     * 获取任务执行时间
     *
     * @param hour
     * @param minute
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-5 上午09:56:02
     */
    public Date getTaskExecuteTime(int hour, int minute) {
        Date date = null;
        try {
            // 系统当前时间
            Calendar cal = Calendar.getInstance();
            // 系统当前时间，小时
            int dhour = cal.get(Calendar.HOUR_OF_DAY);
            // 系统当前时间，分钟
            int dminute = cal.get(Calendar.MINUTE);
            // 当前时间大于扣费任务执行时间
            if (dhour > hour || (dhour == hour && dminute > minute)) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hour, minute, 0);
            return date;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取任务执行时间异常！");
            return null;
        }
    }

    /**
     * 扣费流程
     *
     * @throws Exception
     * @throws Exception
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-9 下午12:17:45
     */
    public void buckleFee() {
        //查询扣费时间小于等于当前时间的有效签约套餐记录
        List<DynaBean> buckleFeeACCTList = buckleFeeDAO.getBuckleFeeACCTList();

        if (buckleFeeACCTList == null) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sbBuffer = new StringBuffer("");//扣费信息批量存入文件
        List<LfDeductionsDisp> disps = new ArrayList<LfDeductionsDisp>();//计费流水表
        List<LfDeductionsList> lists = new ArrayList<LfDeductionsList>();//计费统计表
        List<LfContractTaocan> conCts = new ArrayList<LfContractTaocan>();//签约套餐关联表
        for (DynaBean bean : buckleFeeACCTList) {
            int tcType = Integer.valueOf(propVal(bean.get("taocan_type")));
            String lastBucklefee = propVal(bean.get("last_bucklefee"));
            String bucklefeeTime = propVal(bean.get("bucklefee_time"));//扣费时间
            if (bucklefeeTime == null) {
                continue;
            }
            if (getFlag(tcType).equals(lastBucklefee)) {
                continue;
            }//本月已经扣费 则不处理
            String debitAccount = propVal(bean.get("debitaccount"));
            int tcMoney = Integer.valueOf(propVal(bean.get("taocan_money")));
            String tcName = propVal(bean.get("taocan_name"));
            String tcCode = propVal(bean.get("taocan_code"));
            String mobile = propVal(bean.get("mobile"));
            String cusName = propVal(bean.get("custom_name"));
            String acctNo = propVal(bean.get("acct_no"));
            String corpCode = propVal(bean.get("corp_code"));
            String depName = propVal(bean.get("dep_name"));
            int conState = Integer.valueOf(propVal(bean.get("contract_state")));
            int conSource = Integer.valueOf(propVal(bean.get("contract_source")));
            long conDep = Long.valueOf(propVal(bean.get("contract_dep")));
            long conUser = Long.valueOf(propVal(bean.get("contract_user")));
            long conId = Long.valueOf(propVal(bean.get("contract_id")));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long id = Long.valueOf(propVal(bean.get("id")));//签约套餐关系表id
            //套餐状态
            int state = bean.get("state") == null ? 0 : Integer.parseInt(propVal(bean.get("state")));
            //处理签约套餐表 更新下次扣费时间及扣费标识
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(bucklefeeTime));
            } catch (ParseException e) {
                calendar.setTime(new Date());
            }
            if (tcType == 2) {
                calendar.add(Calendar.MONTH, 1);
            } else if (tcType == 3) {
                calendar.add(Calendar.MONTH, 3);
            } else if (tcType == 4) {
                calendar.add(Calendar.YEAR, 1);
            }
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            LfContractTaocan ct = new LfContractTaocan();
            ct.setId(id);
            if (state == 0 && debitAccount != null) {//满足扣费条件则更新提交扣费状态
                ct.setLastbucklefee(getFlag(tcType));
            }
            //不管套餐是否禁用 都需要更新扣费时间  否则一旦重新启用 扣费时间无法及时更新
            ct.setBucklefeetime(new Timestamp(calendar.getTimeInMillis()));
            ct.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            conCts.add(ct);

            //若扣费账号不存在 则不记录流水
            if (state != 0 || debitAccount == null) {
                continue;
            }

            //计费流水表
            LfDeductionsDisp disp = new LfDeductionsDisp();
            //计费表
            LfDeductionsList list = new LfDeductionsList();
            disp.setAcctno(acctNo);
            list.setAcctno(acctNo);
            disp.setContractdep(conDep);
            list.setContractdep(conDep);
            disp.setContractid(conId);
            list.setContractid(conId);
            disp.setContractstate(conState);
            list.setContractstate(conState);
            disp.setCorpcode(corpCode);
            list.setCorpcode(corpCode);
            disp.setCustomname(cusName);
            list.setCustomname(cusName);
            disp.setDebitaccount(debitAccount);
            list.setDebitaccount(debitAccount);
            disp.setDeductionsmoney(tcMoney);
            disp.setDeductionstype(0);
            list.setDeductionstype(0);//扣费
            disp.setDepname(depName);
            list.setDepname(depName);
            disp.setMobile(mobile);
            list.setMobile(mobile);
            disp.setOprstate(0);
            disp.setOprtime(timestamp);
            disp.setTaocancode(tcCode);
            list.setTaocancode(tcCode);
            disp.setTaocanmoney(tcMoney);
            list.setTaocanmoney(tcMoney);
            disp.setTaocantype(tcType);
            list.setTaocantype(tcType);
            disp.setTaocanname(tcName);
            list.setTaocanname(tcName);
            disp.setContractuser(conUser);
            list.setContractuser(conUser);
            disp.setDepid(conDep);
            list.setDepid(conDep);
            disp.setUserid(conUser);
            list.setUserid(conUser);
            disp.setUpdatetime(timestamp);
            list.setUdpatetime(timestamp);
            list.setBucklefeetime(timestamp);
            list.setBupsummoney(0);
            list.setBuckuptimer(0);
            list.setBuptimer(0);
            list.setContracttype(conSource);
            list.setImonth(calendar.get(calendar.MONTH) + 1);
            list.setIyear(calendar.get(calendar.YEAR));
            String msgId = getMsgId(corpCode);
            disp.setMsgid(msgId);//流水号
            list.setMsgid(msgId);
            disps.add(disp);
            lists.add(list);
            int buckupMaxtimer = Integer.valueOf(propVal(bean.get("buckup_maxtimer")));
            //是否最后一次补扣标识
            String lastBucleUpFee = buckupMaxtimer == 0 ? "1" : "0";
            sbBuffer.append(msgId).append("|").append(mobile).append("|").append(debitAccount).append("|")
                    .append(tcCode).append("|").append(tcMoney).append("|0|").append(lastBucleUpFee + "|").append(System.currentTimeMillis()).append("|&");
        }
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            empTransDao.save(conn, disps, LfDeductionsDisp.class);
            empTransDao.save(conn, lists, LfDeductionsList.class);
            buckleFeeDAO.batchUpdate(conn, conCts);
            //批量存入扣费文件
            if (sbBuffer.length() > 0) {
                writeBuckleFeeFile(sbBuffer.toString());
            }
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "扣费处理过程保存流水表或文件出现异常！");
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 欠费补扣流程
     *
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-9 下午12:17:56
     */
    public void buckleUpFee() {

    }

    public String propVal(Object obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    /**
     * @param tryType      试用类型
     * @param buckleDate   扣费时间 如果已经存在扣费时间 则 不用重新计算 不存在 则表示 首次签约
     * @param tryDay       试用天数
     * @param buckleDay    扣费日
     * @param buckleType   扣费类型
     * @param tryStartDate 试用开始日期
     * @param tryEndDate   试用结束日期
     * @return
     * @description 扣费时间计算
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-1-23 上午09:36:39
     */
    public Calendar buckleTime(Date buckleDate, int tryType, int tryDay, Date tryStartDate, Date tryEndDate, int buckleDay, int buckleType) {
        Calendar tryEndTime = Calendar.getInstance();
        Calendar buckleTime = Calendar.getInstance();
        Calendar curTime = Calendar.getInstance();
        curTime.set(Calendar.HOUR_OF_DAY, 0);
        curTime.set(Calendar.MINUTE, 0);
        curTime.set(Calendar.SECOND, 0);
        curTime.set(Calendar.MILLISECOND, 0);
        if (buckleDate == null) {
            //试用期截止时间
            if (tryType == 0) {
                tryEndTime.add(Calendar.DATE, tryDay);
            } else if (tryType == 1) {
                Calendar start = Calendar.getInstance();
                start.setTime(tryStartDate);
                Calendar end = Calendar.getInstance();
                end.setTime(tryEndDate);
                if (tryEndDate != null && tryStartDate != null) {
                    if (!curTime.before(start) && !curTime.after(end)) {//当前时间在试用期间
                        tryEndTime.setTime(tryEndDate);
                    }
                }
            }
            //根据扣费日和扣费类型计算的扣费时间
            switch (buckleType) {
                case 1://订购生效次月
                    buckleTime.add(Calendar.MONTH, 1);
                    buckleTime.set(Calendar.DATE, buckleDay);
                    break;
                case 3://订购生效当月
                    int curDay = buckleTime.get(Calendar.DAY_OF_MONTH);
                    buckleTime.set(Calendar.DATE, buckleDay);
                    if (curDay > buckleDay) {
                        buckleTime.add(Calendar.MONTH, 1);
                    }
                    break;
                case 2://订购生效当天
                default:
                    break;
            }
        } else {
            buckleTime.setTime(buckleDate);
        }
        buckleTime.set(Calendar.HOUR_OF_DAY, 0);
        buckleTime.set(Calendar.MINUTE, 0);
        buckleTime.set(Calendar.SECOND, 0);
        buckleTime.set(Calendar.MILLISECOND, 0);
        tryEndTime.set(Calendar.HOUR_OF_DAY, 0);
        tryEndTime.set(Calendar.MINUTE, 0);
        tryEndTime.set(Calendar.SECOND, 0);
        tryEndTime.set(Calendar.MILLISECOND, 0);
        while (buckleTime.before(tryEndTime)) {
            buckleTime.add(Calendar.MONTH, 1);
        }
        return buckleTime;
    }

    /**
     * @param code
     * @return
     * @throws Exception
     * @description 签约套餐获取扣费时间
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-1-30 下午04:02:03
     */
    public Calendar buckleTime(String code) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DynaBean charge = buckleFeeDAO.findCharge(code);
        if (charge != null) {
            Date tryEndDate = charge.get("tryend_date") == null ? null : sdf.parse(String.valueOf(charge.get("tryend_date")));
            Date tryStartDate = charge.get("trystart_date") == null ? null : sdf.parse(String.valueOf(charge.get("trystart_date")));
            int tryType = charge.get("try_type") == null ? 0 : Integer.valueOf(String.valueOf(charge.get("try_type")));
            int tryDay = charge.get("try_days") == null ? 0 : Integer.valueOf(String.valueOf(charge.get("try_days")));
            int buckleDate = charge.get("buckle_date") == null ? 0 : Integer.valueOf(String.valueOf(charge.get("buckle_date")));
            int buckleType = charge.get("buckle_type") == null ? 0 : Integer.valueOf(String.valueOf(charge.get("buckle_type")));
            return buckleTime(null, tryType, tryDay, tryStartDate, tryEndDate, buckleDate, buckleType);
        }
        return null;
    }

    /**
     * @param type
     * @description 获取当前扣费套餐的标志
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-1-21 下午07:21:32
     */
    public String getFlag(int type) {
        String value = MobileBusStaticValue.getTaoCanType().get(String.valueOf(type));
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        if (value.indexOf("年") != -1) {
            return year;
        }
        int month = cal.get(Calendar.MONTH);
        if (value.indexOf("季") != -1) {
            return year + String.valueOf(month / 3 + 1);
        } else if (value.indexOf("月") != -1) {
            return year + String.valueOf(month + 1);
        }
        return "0";
    }

    /**
     * 保存扣费文件失败
     *
     * @param msg 格式：流水号|手机号码|扣费账号|套餐编码|金额|标识|是否最后一次补扣|扣费时间，参数以"|"分隔，记录以"&"分隔，
     *            标识：0-扣费；1-补扣；2-退费，是否最后一次补扣，标识为1-补费并且是最后一次补扣时为1，其他为0
     *            123456789|13512345678|112233|AAAA|30|0|5|1422425775787&223456789|13212345678|222233|BBBB|10|2|0|1422425775787&
     * @return true:成功;false:失败
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-28 上午10:03:36
     */
    public boolean saveBuckleFeeFile(String msg) {
        boolean result = writeBuckleFeeFile(msg);
        if (!result) {
            EmpExecutionContext.error("保存扣费文件失败！msg:" + msg);
        }
        return result;
    }

    /**
     * 写扣费文件
     *
     * @param msg
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-28 上午10:00:02
     */
    public boolean writeBuckleFeeFile(String msg) {
        FileWriter writer = null;
        try {
            File file = getBuckleFeeFile();
            if (file == null) {
                EmpExecutionContext.error("获取扣费文件异常！");
                return false;
            }
            writer = new FileWriter(file, true);
            //写文件
            writer.write(msg);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "写扣费文件异常!");
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭扣费文件失败！");
                }
            }
        }
        return true;
    }


    /**
     * 生成扣费文件
     *
     * @param flag 0:写文件;1:读文件
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-28 下午04:15:09
     */
    private File getBuckleFeeFile() {
        try {
            //日志文件存放地址
            String dir = new TxtFileUtil().getWebRoot() + "logger" + File.separatorChar;
            dir = dir.replace('\\', '/');
            File dirFile = new File(dir);
            //创建对应文件夹
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            Calendar calendar = Calendar.getInstance();

            //构造扣费文件目录（年月日文件夹）
            String dayPath = (new SimpleDateFormat("yyyy/MM/dd/")).format(calendar.getTime());
            //构造扣费文件目录
            String sbTemp = new StringBuilder(String.valueOf(dir)).append(dayPath).toString();
            //创建扣费文件文件目录
            txtfileutil.makeDir(sbTemp);

            //构造扣费文件名
            String name = "_bucklefee.txt";
            //文件名
            String fname = (new StringBuilder(String.valueOf((new SimpleDateFormat("yyyy_MM_dd")).format(calendar.getTime())))).append(name).toString();

            //生成异常日志文件
            File file = new File((new StringBuilder(String.valueOf(dir))).append(dayPath).append("/").append(fname).toString());
            if (!file.exists()) {
                //文件不存在则创建
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }
            return file;

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取扣费文件异常！");
            return null;
        }
    }

    /**
     * 读扣费文件
     *
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-28 下午07:17:41
     */
    private String readReBuckleFeeFile() {
        File file = getReBuckleFeeFile();
        if (file == null) {
            EmpExecutionContext.error("获取扣费结果返回文件为空！");
            return null;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            StringBuffer sb = new StringBuffer();
            // 读取文件内容
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            if (sb.length() > 0) {
                return sb.toString().trim();
            } else {
                EmpExecutionContext.info("扣费结果返回文件为空！");
                return null;
            }
        } catch (IOException e) {
            EmpExecutionContext.error(e, "读取扣费结果返回文件异常！");
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭扣费结果返回文件异常！");
                }
            }
        }
    }

    /**
     * 获取扣费返回文件
     *
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-28 下午04:56:27
     */
    public File getReBuckleFeeFile() {
        try {
            //日志文件存放地址
            String dir = new TxtFileUtil().getWebRoot() + "logger" + File.separatorChar;
            dir = dir.replace('\\', '/');

            Calendar calendar = Calendar.getInstance();
            //昨天的目录
            calendar.add(Calendar.DATE, -1);
            //构造扣费返回文件目录（年月日文件夹）
            String dayPath = (new SimpleDateFormat("yyyy/MM/dd/")).format(calendar.getTime());
            //构造扣费返回文件名
            String reBuckleFeeFile = new StringBuilder(dir).append(dayPath).append(String.valueOf((new SimpleDateFormat("yyyy_MM_dd")).format(calendar.getTime())))
                    .append("_rebucklefee.txt").toString();

            //生成异常日志文件
            File file = new File(reBuckleFeeFile);
            if (!file.exists()) {
                EmpExecutionContext.error("扣费结果返回文件不存在，文件：" + reBuckleFeeFile);
                return null;
            }
            return file;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取扣费结果返回文件异常！");
            return null;
        }
    }

    /**
     * 处理扣费结果
     *
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-28 下午07:26:59
     */
    public void reBuckleFeeHandle() {
        //扣费结果
        String result = readReBuckleFeeFile();
        //扣费结果集合
        String[] resultClump = null;
        if (result == null || "".equals(result)) {
            EmpExecutionContext.error("读取扣费结果文件为空！");
            return;
        }
        //扣费
        Map<String, BuckleFeeParams> buckleFeeMap = new LinkedHashMap<String, BuckleFeeParams>();
        //补扣
        Map<String, BuckleFeeParams> BuckleUpFeeMap = new LinkedHashMap<String, BuckleFeeParams>();
        //退费
        Map<String, BuckleFeeParams> recedeFeeMap = new LinkedHashMap<String, BuckleFeeParams>();
        //返回格式:流水号|标识|操作状态|是否最后一次补扣|操作时间
        if (result.indexOf("&") > -1) {
            resultClump = result.split("&");
        } else {
            EmpExecutionContext.error("扣费结果文件内容格式错误，缺少分隔符'&'！");
            return;
        }
        String[] buckleFeeClump = null;
        BuckleFeeParams buckleFeeParams = null;
        //流水号
        String msgId;
        //操作状态
        String state;
        //是否最后一次补扣
        String lastBucleUpFee;
        //操作时间
        String time;
        //标识：0-扣费；1-补扣；2-退费
        String flag;
        for (String buckleFeeInfo : resultClump) {
            if (buckleFeeInfo.indexOf("|") > -1) {
                buckleFeeClump = buckleFeeInfo.split("\\|");
                if (buckleFeeClump.length == 5) {
                    msgId = buckleFeeClump[0];
                    flag = buckleFeeClump[1];
                    state = buckleFeeClump[2];
                    lastBucleUpFee = buckleFeeClump[3];
                    time = buckleFeeClump[4];
                    if (msgId != null && !"".equals(msgId) && flag != null && !"".equals(flag) && state != null && !"".equals(state)
                            && lastBucleUpFee != null && !"".equals(lastBucleUpFee) && time != null && !"".equals(time)) {
                        buckleFeeParams = new BuckleFeeParams();
                        buckleFeeParams.setMsgId(msgId);
                        buckleFeeParams.setState(state);
                        buckleFeeParams.setLastBucleUpFee(lastBucleUpFee);
                        buckleFeeParams.setTime(Long.parseLong(time));
                        //扣费
                        if ("0".equals(flag)) {
                            buckleFeeMap.put(msgId, buckleFeeParams);
                        }
                        //补扣
                        else if ("1".equals(flag)) {
                            BuckleUpFeeMap.put(msgId, buckleFeeParams);
                        }
                        //欠费
                        else if ("2".equals(flag)) {
                            recedeFeeMap.put(msgId, buckleFeeParams);
                        }
                    }

                }
            }
        }
        //扣费操作
        if (buckleFeeMap.size() > 0) {
            try {
                buckleFeeDAO.batchUpdate(buckleFeeMap);
            } catch (SQLException e) {
                EmpExecutionContext.error(e, "处理扣费结果更新数据库异常！");
            }
        }
        //补扣操作
        if (BuckleUpFeeMap.size() > 0) {
            SupplementDeductFeeBiz sdfBiz = new SupplementDeductFeeBiz();
            try {
                sdfBiz.dealSupplementDeductFeeResults(BuckleUpFeeMap);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "处理欠费补扣的结果出现异常！");
            }

        }
        //退费操作
        if (recedeFeeMap.size() > 0) {
            CrmRfMgrBiz crmRfMgrBiz = new CrmRfMgrBiz();
            try {
                crmRfMgrBiz.dealRefundResult(recedeFeeMap);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "处理退费结果出现异常！");
            }

        }
    }

    /**
     * 获取流水号
     *
     * @param corpCode
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2015-1-28 上午10:30:46
     */
    public String getMsgId(String corpCode) {
        try {
            Long time = System.currentTimeMillis();
            //获取顺序号单例对象
            GetSxCount sx = GetSxCount.getInstance();
            String sxcount = sx.getCount();
            String msgId = corpCode + time.toString() + sxcount;
            return msgId;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取扣费流水号异常！");
            return null;
        }
    }

    public void rptWaitOutTime() throws SQLException {
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            buckleFeeDAO.batchTimeOutUpdate(conn);
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "处理超时状态报告出现异常！");
        } finally {
            empTransDao.closeConnection(conn);
        }

    }
}
