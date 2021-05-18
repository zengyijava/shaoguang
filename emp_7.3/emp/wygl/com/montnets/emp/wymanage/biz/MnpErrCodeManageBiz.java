/**
 * Program  : MnpErrCodeManageBiz.java
 * Author   : zousy
 * Create   : 2014-3-26 下午03:22:02
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.wymanage.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wy.AMnperrcode;
import com.montnets.emp.table.wy.TableAMnperrcode;
import com.montnets.emp.util.PageInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 携号转网错误码管理
 *
 * @author zousy <zousy999@qq.com>
 * @version 1.0.0
 * @2014-3-26 下午03:22:02
 */
public class MnpErrCodeManageBiz extends SuperBiz{

    /**
     * 携号转网错误码管理查询
     *
     * @param pageInfo
     * @param conditionMap
     * @return
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-3-26 下午03:22:33
     */
    
    public List<AMnperrcode> getMnpErrCodeList(PageInfo pageInfo, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap) {
        List<AMnperrcode> list = null;
        try {
            list = empDao.findPageListBySymbolsConditionNoCount(null, AMnperrcode.class, conditionMap, orderbyMap, pageInfo);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "携号转网错误码管理查询异常！");
            list = null;
        }
        return list;
    }

    /***
     * 得到日志信息
     * @Description: TODO
     * @param @param aMnperrcodes
     * @param @return
     * @return String
     */
    public String getloggerInfoStr(List<AMnperrcode> aMnperrcodes) {
        StringBuffer sb = new StringBuffer();
        if (aMnperrcodes != null && aMnperrcodes.size() > 0) {
            for (int i = 0; i < aMnperrcodes.size(); i++) {
                AMnperrcode aMnperrcode = aMnperrcodes.get(i);
                sb.append("id：").append(aMnperrcode.getId().toString()).append("，类型：").append(aMnperrcode.getType().toString()).append(aMnperrcode.getMnptype().toString()).append("，错误代码：").append(aMnperrcode.getErrorcode()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 携号转网错误码修改
     *
     * @param mnpErrCode
     * @param isAllowed
     * @return
     * @throws Exception
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-3-26 下午04:38:01
     */
    
    public int updateMnpErrCode(AMnperrcode mnpErrCode, boolean isAllowed) throws Exception {

        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            empTransDao.beginTransaction(conn);
            if (isAllowed) {
                Set<String> errCodes = new HashSet<String>();
                ps = conn.prepareStatement("select distinct ERRORCODE from A_MNPERRCODE");
                rs = ps.executeQuery();
                while (rs.next()) {
                    errCodes.add(rs.getString(TableAMnperrcode.ERRORCODE));
                }
                //出现重复
                if (!errCodes.add(mnpErrCode.getErrorcode())) {
                    return 0;
                }
            }
//				mnpErrCode.setCreatetime(new Timestamp(System.currentTimeMillis()));
            empTransDao.update(conn, mnpErrCode);
            empTransDao.commitTransaction(conn);
            return 1;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "携号转网错误码修改异常！");
            throw new Exception("携号转网错误码修改异常！");
        } finally {
            closeConnection(ps, rs);
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 手工添加
     *
     * @param lists
     * @return
     * @throws Exception
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-4-1 上午09:07:47
     */
    
    @SuppressWarnings("unchecked")
    public int saveMnpErrCodeList(List<AMnperrcode> lists) throws Exception {

        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Set<String> codes = new HashSet<String>();
            ps = conn.prepareStatement("select distinct ERRORCODE from A_MNPERRCODE");
            rs = ps.executeQuery();
            while (rs.next()) {
                codes.add(rs.getString(TableAMnperrcode.ERRORCODE));
            }
            Iterator<AMnperrcode> its = lists.iterator();
            while (its.hasNext()) {
                if (!codes.add(its.next().getErrorcode())) {
                    its.remove();
                }
            }
            if (lists.size() <= 0) {
                return 0;
            }
            return empTransDao.save(conn, lists, AMnperrcode.class);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "手工添加数据异常");
            return 0;
        } finally {
            closeConnection(ps, rs);
            empTransDao.closeConnection(conn);
        }

    }

    public void closeConnection(PreparedStatement ps, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

}

