package com.montnets.emp.common.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.atom.AddrBookAtom;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SelectUserInfoBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.entity.LfBusTaoCan;
import com.montnets.emp.common.servlet.util.Excel2007Reader;
import com.montnets.emp.common.servlet.util.Excel2007VO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.common.vo.GrpupInfoDto;
import com.montnets.emp.common.vo.LfBusTaoCanDto;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfCustField;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class SelectUserInfoServlet extends BaseServlet {
    private final SelectUserInfoBiz userInfoBiz = new SelectUserInfoBiz();
    private final Integer SIZE = 50;
    private final BaseBiz baseBiz = new BaseBiz();
    private final AddrBookAtom addrBookAtom = new AddrBookAtom();

    private final String UNVAILED_USER = "无效用户";

    /**
     * 获取员工机构树信息，只限查子级
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            //从Session中获取当前登录操作员
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //从Session中获取当前登录操作员ID
            String lguserid = String.valueOf(loginSysuser.getUserId());
            //从Session中获取当前登录操作员企业编码
            String corpCode = loginSysuser.getCorpCode();
            List<LfEmployeeDep> empDepList = userInfoBiz.getEmpSecondDepTreeByUserIdorDepId(lguserid, depId, corpCode);
            LfEmployeeDep dep = null;
            StringBuilder tree = new StringBuilder("");
            if (empDepList != null && empDepList.size() > 0) {
                tree.append("[");
                for (int i = 0; i < empDepList.size(); i++) {
                    dep = empDepList.get(i);
                    tree.append("{");
                    tree.append("id:'").append(dep.getDepId()).append("'");
                    tree.append(",name:'").append(dep.getDepName()).append("'");
                    tree.append(",pId:'").append(dep.getParentId()).append("'");
                    tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
                    //tree.append(",dlevel:").append(dep.getDepLevel());
                    //tree.append(",depCode:'").append(dep.getDepCode()+"'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != empDepList.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            }
            response.getWriter().print(tree.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取员工机构树出现异常！");
        }
    }

    /**
     * 查询员工机构下的员工,只查询当前机构下，不包含子机构
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void getEmployeeByDepId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
            StringBuilder sb = new StringBuilder();
            String depId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(SIZE);
            pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            orderByMap.put("employeeId", StaticValue.ASC);
            conditionMap.put("depId", depId);
            List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, null, conditionMap, orderByMap, pageInfo);
            if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (LfEmployee user : lfEmployeeList) {
                    GrpupInfoDto grpupInfoDto = new GrpupInfoDto();
                    grpupInfoDto.setUserName(user.getName());
                    grpupInfoDto.setMobile(user.getMobile());
                    grpupInfoDto.setUdgId(user.getGuId());
                    grpupInfoDto.setIsDep(4);
                    grpupInfoDtos.add(grpupInfoDto);
                }
            }
            sb.append(JSONObject.toJSONString(grpupInfoDtos));
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取员工机构下员工出现异常！");
        }
    }

    /**
     * 查询客户机构下的客户，只查询当前机构下，不包含子机构
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void getClientByDepId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
            StringBuilder sb = new StringBuilder();
            String depId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(SIZE);
            pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
            List<DynaBean> beanList = userInfoBiz.getClientsByDepId(clientDep, 2, pageInfo);
            if (beanList != null && beanList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (DynaBean bean : beanList) {
                    GrpupInfoDto grpupInfoDto = new GrpupInfoDto();
                    grpupInfoDto.setUserName(String.valueOf(bean.get("name")));
                    grpupInfoDto.setMobile(String.valueOf(bean.get("mobile")));
                    grpupInfoDto.setUdgId(Long.valueOf(String.valueOf(bean.get("guid"))));
                    grpupInfoDto.setIsDep(5);
                    grpupInfoDtos.add(grpupInfoDto);
                }
            }
            sb.append(JSONObject.toJSONString(grpupInfoDtos));
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "静态彩信获取客户机构下客户出现异常！");
        }
    }

    /**
     * 获取客户机构树信息，只限查子级
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void getClientSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            //从Session中获取当前登录操作员
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //从Session中获取当前登录操作员ID
            String lguserid = String.valueOf(loginSysuser.getUserId());
            //从Session中获取当前登录操作员企业编码
            String corpCode = loginSysuser.getCorpCode();
            //此方法只查询两级机构
            List<LfClientDep> clientDepList = userInfoBiz.getCliSecondDepTreeByUserIdorDepId(lguserid, depId, corpCode);
            LfClientDep dep = null;
            StringBuilder tree = new StringBuilder("");
            if (clientDepList != null && clientDepList.size() > 0) {
                tree.append("[");
                for (int i = 0; i < clientDepList.size(); i++) {
                    dep = clientDepList.get(i);
                    tree.append("{");
                    tree.append("id:").append(dep.getDepId());
                    tree.append(",name:'").append(dep.getDepName()).append("'");
                    tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
                    //树数据中加入父机构id
                    if (dep.getParentId() == 0) {
                        tree.append(",pId:").append(0);
                    } else {
                        tree.append(",pId:").append(dep.getParentId());
                    }
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != clientDepList.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            }
            response.getWriter().print(tree.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取客户机构树出现异常！");
        }
    }

    /**
     * 获取富信发送时所对应的群组
     *
     * @param request  request对象
     * @param response response对象
     * @throws Exception 异常
     */
    public void getRmsGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            String corpCode = lfSysuser.getCorpCode();
            //用户ID
            String userId = lfSysuser.getUserId().toString();
            //3.群组(员工+客户) 4.员工群组 5.客户群组
            String groupType = request.getParameter("groupType");
            //存储对象
            List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
            //StringBuilder sb = new StringBuilder();
            if ("4".equals(groupType)) {
                grpupInfoDtos = userInfoBiz.getGroupInfoListByGroupId("0", userId, corpCode);
            } else if ("5".equals(groupType)) {
                grpupInfoDtos = userInfoBiz.getGroupInfoListByGroupId("1", userId, corpCode);
            } else if ("3".equals(groupType)) {
                grpupInfoDtos = userInfoBiz.getGroupInfoListByGroupId("0", userId, corpCode);
                grpupInfoDtos.addAll(userInfoBiz.getGroupInfoListByGroupId("1", userId, corpCode));
            }
            response.getWriter().print(JSONObject.toJSONString(grpupInfoDtos));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "静态彩信获取群组列表出现异常！");
        }
    }

    /**
     * 过群组ID分页查询通查询出员工/客户群组中的群组人员信息
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void getGroupUserByGroupId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
            StringBuilder sb = new StringBuilder();
            String groupId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            String type = request.getParameter("type");
            //共享类型 0代表个人 1代表共享
            String shareType = request.getParameter("shareType");
            Integer shareTypeIntVal = Integer.parseInt(StringUtils.defaultIfEmpty(shareType,"0"));
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(SIZE);
            pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            List<GroupInfoVo> groupInfoList = userInfoBiz.getGroupUser(Long.valueOf(groupId), pageInfo, type);
            if (groupInfoList != null && groupInfoList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (GroupInfoVo user : groupInfoList) {
                    GrpupInfoDto grpupInfoDto = new GrpupInfoDto();
                    switch (user.getL2gType()) {
                        /*员工*/
                        case 0:
                            grpupInfoDto.setIsDep(4);
                            break;
                        /*客户*/
                        case 1:
                            grpupInfoDto.setIsDep(5);
                            break;
                        /*共享 或 自建*/
                        case 2:
                            Integer idDep = shareTypeIntVal == 1 ? 13 : 6;
                            grpupInfoDto.setIsDep(idDep);
                            break;
                        default:grpupInfoDto.setIsDep(0);
                    }
                    grpupInfoDto.setUserName(user.getName());
                    grpupInfoDto.setMobile(user.getMobile());
                    grpupInfoDto.setUdgId(user.getGuId());
                    grpupInfoDtos.add(grpupInfoDto);
                }
            }
            sb.append(JSONObject.toJSONString(grpupInfoDtos));
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取群组下成员出现异常！");
        }
    }

    /**
     * 选择人员的搜索功能
     *
     * @param request
     * @param response
     */
    public void getClientOrEmployeeByName(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = "";
        String chooseType = "";
        String corpCode = "";
        Long lguserid = -1L;
        List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
        try {
            name = request.getParameter("searchName");
            //选择类型查询  1.员工通讯录   2.客户通讯录  3.群组(员工+客户) 4.员工群组 5.客户群组 6.客户属性 7.签约用户
            chooseType = request.getParameter("chooseType");
            String udgId = request.getParameter("udgId");
            String pageIndexStr = request.getParameter("pageIndex");
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            corpCode = loginSysuser.getCorpCode();
            lguserid = loginSysuser.getUserId();
            if ("1".equals(chooseType)) {
                //LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                //LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
                //orderByMap.put("guId", StaticValue.ASC);
                //conditionMap.put("name", name);
                //conditionMap.put("corpCode", corpCode);
                //List<LfEmployee> lfEmployeeList = baseBiz.findListByCondition(LfEmployee.class, conditionMap, orderByMap);
                List<LfEmployee> lfEmployeeList = userInfoBiz.getClientOrEmployeeByName(name, corpCode,pageIndexStr);
                //拼接html
                if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                    for (LfEmployee user : lfEmployeeList) {
                        GrpupInfoDto grpupInfoDto = new GrpupInfoDto();
                        grpupInfoDto.setUserName(user.getName());
                        grpupInfoDto.setUdgId(user.getGuId());
                        grpupInfoDto.setMobile(user.getMobile());
                        grpupInfoDto.setIsDep(4);
                        grpupInfoDtos.add(grpupInfoDto);
                    }
                }
            } else if ("2".equals(chooseType)) {
                List<DynaBean> clientList = userInfoBiz.findClientByName(name, corpCode);
                if (clientList != null && clientList.size() > 0) {
                    for (DynaBean bean : clientList) {
                        GrpupInfoDto grpupInfoDto = new GrpupInfoDto();
                        grpupInfoDto.setUserName(String.valueOf(bean.get("name")));
                        grpupInfoDto.setUdgId(Long.parseLong(String.valueOf(bean.get("guid"))));
                        grpupInfoDto.setMobile(String.valueOf(bean.get("mobile")));
                        grpupInfoDto.setIsDep(5);
                        grpupInfoDtos.add(grpupInfoDto);
                    }
                }
            } else if ("3".equals(chooseType) || "4".equals(chooseType) || "5".equals(chooseType)) {
                List<GroupInfoVo> groupList = userInfoBiz.findGroupByName(name, lguserid, chooseType,udgId);
                if (groupList != null && groupList.size() > 0) {
                    for (GroupInfoVo user : groupList) {
                        GrpupInfoDto grpupInfoDto = new GrpupInfoDto();
                        if (user.getName() != null) {
                            switch (user.getL2gType()) {
                                case 2:
                                    grpupInfoDto.setIsDep(6);
                                    break;
                                case 1:
                                    grpupInfoDto.setIsDep(5);
                                    break;
                                case 0:
                                    grpupInfoDto.setIsDep(4);
                                    break;
                                default:
                                    grpupInfoDto.setIsDep(0);
                            }
                            grpupInfoDto.setUserName(user.getName());
                            grpupInfoDto.setMobile(user.getMobile());
                            grpupInfoDto.setUdgId(user.getGuId());
                            grpupInfoDtos.add(grpupInfoDto);
                        }
                    }
                }
            }
            response.getWriter().print(JSONObject.toJSONString(grpupInfoDtos));
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "企业富信-富信相同内容发送-选择人员，根据名字查询异常！");
        }
    }

    /**
     * 获取客户机构的人员
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void getClientDepCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //是否包含关系 0不包含   1包含
            String ismut = request.getParameter("ismut");
            //机构ID
            String depId = request.getParameter("depId");
            //不包含子机构
            if ("0".equals(ismut)) {
                //这里返回机构的总人数
                //查询出单个机构下 （不包含子机构人员）的个数
                //String number = smsBiz.getClientCountByDepId(depId);
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                if (clientDep == null) {
                    response.getWriter().print("nobody");
                    return;
                }
                String number = userInfoBiz.getDepClientCount(clientDep, 2).toString();
                if (!"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                        return;
                    } else {
                        response.getWriter().print(number);
                        return;
                    }
                } else {
                    response.getWriter().print("nobody");
                    return;
                }
            } else if ("1".equals(ismut)) {
                LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
                if (dep != null) {
                    String number = userInfoBiz.getDepClientCount(dep, 1).toString();
                    response.getWriter().print(number);
                    return;
                }
            }
            response.getWriter().print("nobody");
            return;
        } catch (Exception e) {
            response.getWriter().print("errer");
            EmpExecutionContext.error(e, " 静态彩信发送获取客户机构的人员出现异常！");
        }
    }

    /**
     * 判断是否该客户构包含选择了的子机构，并且把子机构删除掉
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void isClientDepContaineDeps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //是否包含关系 0不包含   1包含
            String ismut = request.getParameter("ismut");
            //机构ID
            String depId = request.getParameter("depId");
            //不包含子机构
            if ("0".equals(ismut)) {
                //查询出单个机构下 （不包含子机构人员）的个数
                //String number = smsBiz.getClientCountByDepId(depId);
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                if (clientDep == null) {
                    response.getWriter().print("nobody");
                    return;
                }
                String number = userInfoBiz.getDepClientCount(clientDep, 2).toString();
                if (!"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                    } else {
                        response.getWriter().print(number);
                    }
                } else {
                    response.getWriter().print("nobody");
                }
                return;
            }
            //该机构的包含子机构
            List<LfClientDep> lfClientDepList = null;
            LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
            //条件查询
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //已选好的机构
            String cliDepIds = request.getParameter("cliDepIds");
            String[] depIds = cliDepIds.split(",");
            List<Long> depIdExistList = new ArrayList<Long>();
            //循环
            for (String id : depIds) {
                if (id != null && !"".equals(id)) {
                    if (id.contains("e")) {
                        if (!"".equals(id.substring(1))) {
                            depIdExistList.add(Long.valueOf(id.substring(1)));
                        }
                    } else {
                        depIdExistList.add(Long.valueOf(id));
                    }
                }
            }
            //查找出要添加的机构的所有子机构，放在一个set里面
            LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
            if (dep != null) {
                conditionMap.put("deppath&like", dep.getDeppath());
                conditionMap.put("corpCode", dep.getCorpCode());
                lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
                if (lfClientDepList != null && lfClientDepList.size() > 0) {
                    for (LfClientDep aLfClientDepList : lfClientDepList) {
                        depIdSet.add(aLfClientDepList.getDepId());
                    }
                    //这里是把包含的机构的ID选择出来
                    List<Long> depIdListTemp = new ArrayList<Long>();
                    for (Long aDepIdExistList : depIdExistList) {
                        if (depIdSet.contains(aDepIdExistList)) {
                            depIdListTemp.add(aDepIdExistList);
                        }
                    }

                    //如果确实有几个已经存在的机构是要添加机构的子机构，那么就生成生成select的html
                    String depids = depIdSet.toString();
                    depids = depids.substring(1, depids.length() - 1);
                    //计算机构人数
                    String countttt = userInfoBiz.getDepClientCount(dep, 1).toString();

                    if (!"".equals(countttt)) {
                        if ("0".equals(countttt)) {
                            response.getWriter().print("nobody");
                        } else if (depIdListTemp.size() > 0) {
                            String tempDeps = depIdListTemp.toString();
                            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
                            response.getWriter().print(countttt + "," + tempDeps);
                        } else {
                            response.getWriter().print("notContains" + "&" + countttt);
                        }
                    } else {
                        response.getWriter().print("nobody");
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "静态彩信发送获取客户机构人数出现异常！");
            response.getWriter().print("errer");
        }
    }

    /**
     * 这里是检测点击客户机构是否被选择了的机构包含
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void isClientDepContained(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //点击机构的ID
            String depId = request.getParameter("depId");
            //已经选择好的机构ID
            String clientDepIds = request.getParameter("cliDepIds");
            //解析IDS
            String[] depIds = clientDepIds.split(",");
            //机构集合
            List<LfClientDep> lfClientDepList = null;
            //处理是否包含机构ID的集合
            LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
            //查询条件集合
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //机构对象
            LfClientDep dep = null;
            //循环
            for (String id : depIds) {
                //如果包含了，则说明该机构包含子机构
                if (id == null || "".equals(id)) {
                    continue;
                }
                //如果相等，则眺出
                if (id.equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                    //遇到包含子机构的机构处理操作
                } else if (id.contains("e")) {
                    Long temp = Long.valueOf(id.substring(1));
                    conditionMap.clear();
                    dep = baseBiz.getById(LfClientDep.class, temp);
                    if (dep != null) {
                        conditionMap.put("corpCode", dep.getCorpCode());
                        conditionMap.put("deppath&like", dep.getDeppath());
                        lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
                        if (lfClientDepList != null && lfClientDepList.size() > 0) {
                            for (LfClientDep aLfClientDepList : lfClientDepList) {
                                depIdsSet.add(aLfClientDepList.getDepId());
                            }
                        }
                    }
                    //单个机构，不包含子机构
                } else {
                    dep = null;
                    dep = baseBiz.getById(LfClientDep.class, Long.valueOf(id));
                    if (dep != null) {
                        depIdsSet.add(dep.getDepId());
                    }
                }
            }
            boolean isFlag = false;
            //判断是否包含该机构
            if (depIdsSet.size() > 0) {
                Long tempDepId = Long.valueOf(depId);
                isFlag = depIdsSet.contains(tempDepId);
            }
            //返回
            if (isFlag) {
                response.getWriter().print("depExist");
            } else {
                response.getWriter().print("noExist");
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "静态彩信发送处理客户机构包含出现异常！");
        }
    }

    /**
     * 获取员工机构的人员
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void getEmpDepCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //是否包含关系 0不包含   1包含
            String ismut = request.getParameter("ismut");
            //机构ID
            String depId = request.getParameter("depId");
            //不包含子机构
            if ("0".equals(ismut)) {
                //查询出单个机构下 （不包含子机构人员）的个数
                String number = addrBookAtom.getEmployeeCountByDepId(depId);
                if (number != null && !"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                    } else {
                        response.getWriter().print(number);
                    }
                } else {
                    response.getWriter().print("nobody");
                }
            } else if ("1".equals(ismut)) {
                LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(depId));
                if (dep != null) {
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    conditionMap.put("deppath&like", dep.getDeppath());
                    conditionMap.put("corpCode", dep.getCorpCode());
                    List<LfEmployeeDep> lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
                    if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0) {
                        StringBuilder idStr = new StringBuilder();
                        for (LfEmployeeDep aLfEmployeeDepList : lfEmployeeDepList) {
                            idStr.append(aLfEmployeeDepList.getDepId().toString()).append(",");
                        }
                        if (!"".equals(idStr.toString()) && idStr.length() > 0) {
                            idStr = new StringBuilder(idStr.substring(0, idStr.length() - 1));
                            //计算机构人数
                            String countttt = addrBookAtom.getEmployeeCountByDepId(idStr.toString());
                            response.getWriter().print(countttt);
                        }
                    }
                }
            } else {
                response.getWriter().print("nobody");
            }
        } catch (Exception e) {
            response.getWriter().print("errer");
            EmpExecutionContext.error(e, "验证员工机构下人数出现异常！");
        }
    }

    /**
     * 判断该员工机构是否包含选择了的子机构，并且把子机构删除掉
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void isDepContaineDeps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //是否包含关系 0不包含   1包含
            String ismut = request.getParameter("ismut");
            //机构ID
            String depId = request.getParameter("depId");
            //不包含子机构
            if ("0".equals(ismut)) {
                //查询出单个机构下 （不包含子机构人员）的个数
                String number = addrBookAtom.getEmployeeCountByDepId(depId);
                if (number != null && !"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                    } else {
                        response.getWriter().print(number);
                    }
                } else {
                    response.getWriter().print("nobody");
                }
                return;
            }
            //该机构的包含子机构
            List<LfEmployeeDep> lfEmployeeDepList = null;
            LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
            //条件查询
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //已选好的机构
            String empDepIds = request.getParameter("empDepIds");
            String[] depIds = empDepIds.split(",");
            List<Long> depIdExistList = new ArrayList<Long>();
            //循环
            for (String id : depIds) {
                if (id != null && !"".equals(id)) {
                    if (id.contains("e")) {
                        if (!"".equals(id.substring(1))) {
                            depIdExistList.add(Long.valueOf(id.substring(1)));
                        }
                    } else {
                        depIdExistList.add(Long.valueOf(id));
                    }
                }
            }
            //查找出要添加的机构的所有子机构，放在一个set里面
            LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(depId));
            if (dep != null) {
                conditionMap.put("deppath&like", dep.getDeppath());
                conditionMap.put("corpCode", dep.getCorpCode());
                lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
                if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0) {
                    for (LfEmployeeDep aLfEmployeeDepList : lfEmployeeDepList) {
                        depIdSet.add(aLfEmployeeDepList.getDepId());
                    }
                    //这里是把包含的机构的ID选择出来
                    List<Long> depIdListTemp = new ArrayList<Long>();
                    for (Long aDepIdExistList : depIdExistList) {
                        if (depIdSet.contains(aDepIdExistList)) {
                            depIdListTemp.add(aDepIdExistList);
                        }
                    }
                    //如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
                    String depids = depIdSet.toString();
                    depids = depids.substring(1, depids.length() - 1);
                    //计算机构人数
                    String countttt = addrBookAtom.getEmployeeCountByDepId(depids);
                    if (countttt != null && !"".equals(countttt)) {
                        if ("0".equals(countttt)) {
                            response.getWriter().print("nobody");
                        } else if (depIdListTemp.size() > 0) {
                            String tempDeps = depIdListTemp.toString();
                            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
                            response.getWriter().print(countttt + "," + tempDeps);
                        } else {
                            response.getWriter().print("notContains" + "&" + countttt);
                        }
                    } else {
                        response.getWriter().print("nobody");
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "静态彩信发送处理机构包含出现异常！");
            response.getWriter().print("errer");
        }
    }

    /**
     * 这里是检测选择的员工机构是否被已经选择了的机构包含
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void isEmpDepContained(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //点击机构的ID
            String depId = request.getParameter("depId");
            //已经选择好的机构ID
            String empDepIds = request.getParameter("empDepIds");
            //解析IDS
            String[] depIds = empDepIds.split(",");
            //机构集合
            List<LfEmployeeDep> lfEmployeeDepList = null;
            //处理是否包含机构ID的集合
            LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
            //查询条件集合
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //机构对象
            LfEmployeeDep dep = null;
            //循环
            for (String id : depIds) {
                //如果包含了，则说明该机构包含子机构
                if (id == null || "".equals(id)) {
                    continue;
                }
                //如果相等，则眺出
                if (id.equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                    //遇到包含子机构的机构处理操作
                } else if (id.contains("e")) {
                    Long temp = Long.valueOf(id.substring(1));
                    conditionMap.clear();
                    dep = baseBiz.getById(LfEmployeeDep.class, temp);
                    if (dep != null) {
                        conditionMap.put("deppath&like", dep.getDeppath());
                        conditionMap.put("corpCode", dep.getCorpCode());
                        lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
                        if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0) {
                            for (LfEmployeeDep aLfEmployeeDepList : lfEmployeeDepList) {
                                depIdsSet.add(aLfEmployeeDepList.getDepId());
                            }
                        }
                    }
                    //单个机构，不包含子机构
                } else {
                    dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(id));
                    if (dep != null) {
                        depIdsSet.add(dep.getDepId());
                    }
                }
            }
            boolean isFlag = false;
            //判断是否包含该机构
            if (depIdsSet.size() > 0) {
                Long tempDepId = Long.valueOf(depId);
                isFlag = depIdsSet.contains(tempDepId);
            }
            //返回
            if (isFlag) {
                response.getWriter().print("depExist");
            } else {
                response.getWriter().print("noExist");
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "静态彩信发送处理机构包含出现异常！");
        }
    }

    /**
     * 获取签约用户列表
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void getClientAttrList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
        LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        //从Session中获取当前登录操作员企业编码
        String corpCode = loginSysuser.getCorpCode();
        // 企业编码
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            // 查询所有的客户属性
            conditionMap.put("corp_code", corpCode);
            orderbyMap.put("id", "asc");
            List<LfCustField> custField = baseBiz.getByCondition(LfCustField.class, conditionMap, orderbyMap);
            for (LfCustField custField2 : custField) {
                GrpupInfoDto dto = new GrpupInfoDto();
                int mcount = userInfoBiz.getClientCountByFieldRef(corpCode, custField2.getField_Ref());
                dto.setClientFieldCount(mcount);
                dto.setClientFieldId(custField2.getId());
                dto.setClientFieldName(custField2.getField_Name());
                dto.setClientFieldRef(custField2.getField_Ref());
                grpupInfoDtos.add(dto);
            }
            writer.print(JSONObject.toJSONString(grpupInfoDtos));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取客户属性人员列表失败！");
        }
    }

    /**
     * 根据名字或者fieldId查找客户
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getCustFieldMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 企业编码
        String corpCode = request.getParameter("corpCode");
        String fieldId = request.getParameter("fieldId");
        // 搜索名称
        String searchName = request.getParameter("searchName");
        try {
            // 查询所有的客户属性值
            LfCustFieldValueVo cfvo = new LfCustFieldValueVo();

            // 根据客户属性查找属性值
            cfvo.setCorp_code(corpCode);
            cfvo.setField_ID(fieldId);
            cfvo.setField_Value(searchName);
            List<LfCustFieldValueVo> proList = userInfoBiz.getCustVos(cfvo);
            //拼成html代码返回
            response.getWriter().print(JSONObject.toJSONString(proList));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取客户属性成员失败！");
        }
    }

    /**
     * 获取套餐列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSignClientList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<LfBusTaoCanDto> lfBusTaoCanDtos = new ArrayList<LfBusTaoCanDto>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
        PrintWriter writer = null;
        String corpCode = "";
        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            corpCode = lfSysuser.getCorpCode();
            writer = response.getWriter();
            conditionMap.put("state", "0");
            conditionMap.put("corp_code", corpCode);
            orderByMap.put("taocan_name", StaticValue.ASC);
            // 根据条件查询所有套餐
            List<LfBusTaoCan> busTaoCanList = baseBiz.getByCondition(LfBusTaoCan.class, conditionMap, orderByMap);
            if (busTaoCanList != null && busTaoCanList.size() > 0) {
                StringBuilder tcCodes = new StringBuilder();
                for (LfBusTaoCan busTaoCan : busTaoCanList) {
                    tcCodes.append("'").append(busTaoCan.getTaocan_code()).append("',");
                }
                // 获取群组id字符串
                tcCodes = new StringBuilder(tcCodes.substring(0, tcCodes.length() - 1));
                Map<String, String> countMap = userInfoBiz.getSignClientMemberCount(tcCodes.toString(), corpCode);
                // 拼成html代码返回
                for (LfBusTaoCan busTaoCan : busTaoCanList) {
                    LfBusTaoCanDto dto = new LfBusTaoCanDto();
                    String mcount = StringUtils.defaultIfEmpty(countMap.get(busTaoCan.getTaocan_code()), "0");
                    dto.setMemberCount(Integer.parseInt(mcount));
                    dto.setTaocanCode(busTaoCan.getTaocan_code());
                    dto.setTaocanName(busTaoCan.getTaocan_name());
                    lfBusTaoCanDtos.add(dto);
                }
            }
            writer.print(JSONObject.toJSONString(lfBusTaoCanDtos));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询套餐信息失败！");
        }
    }

    /**
     * 获取指定签约套餐下的用户列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSignClientMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
            StringBuilder sb = new StringBuilder();
            //搜索名称
            String searchName = request.getParameter("searchName");
            //套餐编码
            String tcCode = request.getParameter("tcCode");
            // 页码
            String pageIndex = request.getParameter("pageIndex");
            if (tcCode == null || "".equals(tcCode.trim())) {
                EmpExecutionContext.error("客户群组群发选择签约人员获取套餐编码为" + tcCode);
                response.getWriter().print("");
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageIndex(Integer.parseInt(pageIndex));
            // 每页显示50条记录
            pageInfo.setPageSize(50);
            List<DynaBean> clientBeanList = userInfoBiz.getSignClientMember(searchName, tcCode, pageInfo);
            //签约ID集合
            StringBuilder contractIds = new StringBuilder("");
            if (clientBeanList != null && clientBeanList.size() > 0) {
                for (DynaBean clientBean : clientBeanList) {
                    contractIds.append(String.valueOf(clientBean.get("contract_id"))).append(",");
                }
                contractIds.deleteCharAt(contractIds.lastIndexOf(","));
            }
            if (clientBeanList != null && clientBeanList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                Map<String, String> contractMap = userInfoBiz.getMemberCountByContractIds(contractIds.toString());
                for (DynaBean clientBean : clientBeanList) {
                    String accountNoStr = contractMap.get(String.valueOf(clientBean.get("contract_id")));
                    if (accountNoStr.length() > 4) {
                        accountNoStr = "(***" + accountNoStr.substring(accountNoStr.length() - 4, accountNoStr.length()) + ")";
                    } else if (accountNoStr.length() > 0) {
                        accountNoStr = "(" + accountNoStr + ")";
                    }
                    GrpupInfoDto dto = new GrpupInfoDto();
                    dto.setUdgId(Long.parseLong(clientBean.get("guid").toString()));
                    dto.setUserName(clientBean.get("name").toString() + accountNoStr);
                    dto.setMobile(clientBean.get("mobile").toString());
                    dto.setIsDep(7);
                    grpupInfoDtos.add(dto);
                }
            }
            sb.append(JSONObject.toJSONString(grpupInfoDtos));
            if (grpupInfoDtos.size() == 0) {
                response.getWriter().print("");
            } else {
                response.getWriter().print(sb.toString());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取套餐人员信息异常！");
        }
    }

    /**
     * 获取指定属性值下的用户数
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getCustFieldMemberCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 企业编码
        String corpCode = request.getParameter("corpCode");
        // 字符转义回来
        String fieldValue = StringEscapeUtils.unescapeHtml(request.getParameter("fieldValue"));
        String[] split = fieldValue.split("&");
        String fieldRef = split[0];
        String fieldValueId = split[1];
        try {
            Integer mcount = 0;
            LfCustFieldValueVo fieldValueVo = new LfCustFieldValueVo();
            fieldValueVo.setField_Ref(fieldRef);
            fieldValueVo.setId(Long.valueOf(fieldValueId));
            mcount = userInfoBiz.getClientCountByCusField(corpCode, fieldValueVo);
            mcount = mcount == null ? 0 : mcount;
            response.getWriter().print(mcount);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据属性值获取客户数失败！");
        }
    }

    /**
     * 根据名字或者手机号查询操作员
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getOperatorByNameOrPhone(HttpServletRequest request, HttpServletResponse response) {
        List<LfSysuser> sysuserList;
        String jsonStr;
        try {
            String nameOrPhone = request.getParameter("nameOrPhone");
            String corpCode = request.getParameter("corpCode");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpCode);
            //如果为全数字则认为查手机号
            if (nameOrPhone.matches("\\d+")) {
                conditionMap.put("mobile&like", nameOrPhone);
            } else {
                conditionMap.put("name&like", nameOrPhone);
            }
            sysuserList = new BaseBiz().getByCondition(LfSysuser.class, conditionMap, null);
            if (sysuserList == null) {
                jsonStr = JSONObject.toJSONString(new ArrayList<LfSysuser>());
            } else {
                jsonStr = JSONObject.toJSONString(sysuserList);
            }
            response.getWriter().print(jsonStr);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据名字或者手机号查询操作员异常");
        }
    }

    /**
     * 获取操作员机构的Z-Tree
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSecondDepJson(HttpServletRequest request, HttpServletResponse response) {
        try {
            LfSysuser sysuser = getLoginUser(request);
            Long depId = null;
            //机构id
            String depStr = request.getParameter("depId");

            if (depStr != null && !"".equals(depStr.trim())) {
                depId = Long.parseLong(depStr);
            }
            //获取机构树字符串
            String departmentTree = getDepJosnData(depId, sysuser);
            response.getWriter().print(departmentTree);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "员工群组获取操作员机构出现异常！");
        }
    }

    private String getDepJosnData(Long depId, LfSysuser sysuser) {
        StringBuffer tree = null;
        if (sysuser.getPermissionType() == 1) {
            tree = new StringBuffer("[]");
        } else {
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;
            try {
                if (depId == null) {
                    lfDeps = new ArrayList<LfDep>();
                    LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(sysuser.getUserId(), sysuser.getCorpCode()).get(0);//这里需要优化
                    lfDeps.add(lfDep);
                } else {
                    lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, sysuser.getCorpCode());
                }
                LfDep lfDep = null;
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId()).append("'");
                    tree.append(",dlevel:'").append(lfDep.getDepLevel()).append("'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != lfDeps.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            } catch (Exception e) {
                EmpExecutionContext.error(e, "员工群组获取操作员机构树出现异常！");
                tree = new StringBuffer("[]");
            }
        }
        return tree.toString();
    }

    /**
     * 根据机构Id获取所属操作员
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getDepMemberByDepId(HttpServletRequest request, HttpServletResponse response) {
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String depId = request.getParameter("depId");
            if (depId != null && !"".equals(depId)) {
                conditionMap.put("depId", depId);
            }
            List<LfSysuser> sysuserList = new BaseBiz().getByCondition(LfSysuser.class, conditionMap, null);
            if (sysuserList != null) {
                Iterator<LfSysuser> iterator = sysuserList.iterator();
                while (iterator.hasNext()) {
                    LfSysuser tmp = iterator.next();
                    if (UNVAILED_USER.equals(tmp.getName())) {
                        iterator.remove();
                        break;
                    }
                }
            }
            String jsonStr = JSONObject.toJSONString(sysuserList);
            response.getWriter().print(jsonStr);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "员工群组获取操作员出现异常！");
        }
    }

    /**
     * 检查要添加的机构是不是包含已经添加的机构
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void isDepsContainedSubDep(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ismut = request.getParameter("ismut");
        String depId = request.getParameter("depId");
        DepBiz depBiz = new DepBiz();
        if ("0".equals(ismut)) {
            String countttt = depBiz.getDepCountByDepId(depId);
            response.getWriter().print(countttt);
            return;
        }
        List<String> depIds = new ArrayList<String>();
        String depIdsExist = request.getParameter("depIdsExist");
        if (StringUtils.isNotEmpty(depIdsExist)) {
            depIds = Arrays.asList(depIdsExist.split(","));
        }
        //将已经存在的机构id放在list里面(如果前缀有e就去掉e放在depIdExistList里面)
        List<Long> depIdExistList = new ArrayList<Long>();
        for (String dep : depIds) {
            if (dep != null && !"".equals(dep)) {
                if (dep.contains("e")) {
                    if (!"".equals(dep.substring(1))) {
                        depIdExistList.add(Long.valueOf(dep.substring(1)));
                    }
                } else {
                    depIdExistList.add(Long.valueOf(dep));
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
        for (Long aDepIdExistList : depIdExistList) {
            if (depIdSet.contains(aDepIdExistList)) {
                depIdListTemp.add(aDepIdExistList);
            }
        }
        //如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
        String depids = depIdSet.toString();
        depids = depids.substring(1, depids.length() - 1);
        //计算机构人数
        String countttt = depBiz.getDepCountByDepId(depids);
        if (depIdListTemp.size() > 0) {
            String tempDeps = depIdListTemp.toString();
            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
            response.getWriter().print(countttt + "," + tempDeps);
        }
        //如果没有包含关系
        else {
            response.getWriter().print("notContains" + "&" + countttt);
        }
    }

    /**
     * 该机构是否已经包含在添加的机构内
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void checkDepIsExist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            String depIdsExist = request.getParameter("depIdsExist");

            String lgcorpcode = request.getParameter("lgcorpcode");// 当前登录企业

            String[] depIds = depIdsExist.split(",");
            StringBuilder depIdsTemp = new StringBuilder();
            for (String depId1 : depIds) {
                if (depId1.contains("e")) {
                    depIdsTemp.append(depId1.substring(1)).append(",");
                } else if (depId1.equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                }
            }
            // 判断新添加的机构是不是已经添加的机构的子机构
            boolean result = isDepAcontainsDepB(depIdsTemp.toString(), depId);
            if (result) {
                response.getWriter().print("depExist");
            } else {
                response.getWriter().print("notExist");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "员工群组处理机构包含出现异常！");
        }
    }

    /**
     * 判断机构A是否包含机构B
     *
     * @param depIdAs
     * @param depIdB
     * @return
     */
    private boolean isDepAcontainsDepB(String depIdAs, String depIdB) {
        boolean result = false;
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        String[] depIdAsTemp = depIdAs.split(",");
        try {
            for (String aDepIdAsTemp : depIdAsTemp) {
                if (aDepIdAsTemp != null && !"".equals(aDepIdAsTemp)) {
                    String deps = new DepDAO()
                            .getChildUserDepByParentID(null, Long
                                    .valueOf(aDepIdAsTemp));
                    String depArray[] = deps.split(",");
                    for (String aDepArray : depArray) {
                        depIdSet.add(Long.valueOf(aDepArray));
                    }
                }
            }
            result = depIdSet.contains(Long.valueOf(depIdB));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "员工群组处理机构包含出现异常！");
        }
        return result;
    }

    /**
     * 通过文件上传形式新增群组成员
     *
     * @param request
     * @param response
     * @return
     */
    public void addGroupMemberByFile(HttpServletRequest request, HttpServletResponse response) {
        BufferedReader reader = null;
        FileItem fileItem = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> fileList = null;
            PhoneUtil phoneUtil = new PhoneUtil();
            StringBuilder sb = new StringBuilder();
            fileList = upload.parseRequest(request);
            Iterator<FileItem> it = fileList.iterator();
            HashSet<String> repeatList = new HashSet<String>();
            List<LfMalist> lfMalists = new ArrayList<LfMalist>();
            String fileCurName = "";
            //遍历文件
            while (it.hasNext()) {

                fileItem = it.next();
                //表明为文件
                if (!fileItem.isFormField() && fileItem.getName().length() > 0) {
                    String line;
                    String[] haoduan = userInfoBiz.getHaoduan();
                    fileCurName = fileItem.getName();
                    String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
                    if (fileType.equalsIgnoreCase(".xls")) {
                        String fileHeader = getFileHeader(fileItem.getInputStream());//文件头
                        if (!"D0CF11E0".equals(fileHeader)) {//文件头判断 xls文件头为D0CF11E0
                            continue;
                        }
                        Workbook workBook = Workbook .getWorkbook(fileItem.getInputStream());
                        Sheet sh = workBook.getSheet(0);
                        for (int k = 0; k < sh.getRows(); k++) {
                            LfMalist malist = new LfMalist();
                            Cell[] cells = sh.getRow(k);
                            //如果不是两个字段跳过
                            if(cells.length != 2) continue;
                            String name = cells[0].getContents();
                            String phone = cells[1].getContents();
                            //如果名字或手机号为空跳过
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //如果名字带有特殊符号则直接跳过
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            //手机号校验不合格直接跳过
                            if (phoneUtil.getPhoneType(phone, haoduan) == -1 || !checkRepeat(repeatList,name+"#HS#"+phone)) {
                                continue;
                            }
                            malist.setName(name);
                            malist.setMobile(phone);
                            lfMalists.add(malist);
                        }
                    } else if (fileType.equalsIgnoreCase(".txt")) {
                    	ChangeCharset charsetUtil = new ChangeCharset();
                    	String charset = charsetUtil.get_charset(fileItem.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
                        while ((line = reader.readLine()) != null) {
                            LfMalist malist = new LfMalist();
                            line = line.trim();
                            //将所有的空格变成一个空格
                            line = line.replaceAll("\\s+"," ");
                            String[] nameAndPhone = line.split(" ");
                            if(nameAndPhone.length != 2) continue;
                            String name = nameAndPhone[0];
                            String phone = nameAndPhone[1];
                            //如果名字或手机号为空跳过
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //如果名字带有特殊符号则直接跳过
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            //手机号校验不合格直接跳过
                            if (phoneUtil.getPhoneType(phone, haoduan) == -1 || !checkRepeat(repeatList,name+"#HS#"+phone)) {
                                continue;
                            }
                            malist.setName(name);
                            malist.setMobile(phone);
                            lfMalists.add(malist);
                        }
                    } else if (fileType.equalsIgnoreCase(".xlsx")) {
                    	//上传文件格式为.xlsx
                    	String uploadPath = StaticValue.FILEDIRNAME;// file/smstxt/
            			String temp = new TxtFileUtil().getWebRoot()+uploadPath;
            			Excel2007VO excel=new Excel2007Reader().fileParset(temp, fileItem.getInputStream());
            			int maxNum = 2000;
            			String[] cells;
        				reader=excel.getReader();
        				String charset = new ChangeCharset().get_charset(fileItem.getInputStream());
        				if(charset.startsWith("UTF-"))
        				{
        					reader.read(new char[1]);
        				}
        				String tmp="";
        				//计算行数
        				int k=-1;
        				while ((tmp = reader.readLine()) != null && k <= maxNum)
        				{
        					String name="";
        					String phone="";
        					// 循环获得单元行
        					if("".equals(tmp)) {
        						continue;
        					}
        					k++;
        					cells = tmp.split(",");
        					int size = cells.length;
        					//用户名
        					if (size >1){
                                 name = cells[1];
        					}
        					//手机号
        					if (size >2){
        						 phone = cells[2];
        					}
        					//不是两个字段
        					if (size >3) {
        						continue;
        					}
                            //如果名字或手机号为空跳过
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //如果名字带有特殊符号则直接跳过
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            //手机号校验不合格直接跳过
                            if (phoneUtil.getPhoneType(phone, haoduan) == -1 || !checkRepeat(repeatList,name+"#HS#"+phone)) {
                                continue;
                            }
                            LfMalist malist = new LfMalist();
                            malist.setName(name);
                            malist.setMobile(phone);
                            lfMalists.add(malist);
        				}
                    }
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(lfMalists));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "通过文件上传形式新增群组成员出现异常");
        }finally {
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                EmpExecutionContext.error(e,"员工群组关闭文件流出现异常！");
            }
            if(fileItem != null){
                fileItem.delete();
            }
        }
    }
    /**
     * 根据文件路径获取文件头信息
     *
     * @param inputStream 文件流
     * @return 文件头信息
     */
    private String getFileHeader(InputStream inputStream) {
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
            EmpExecutionContext.error(e,"群组新增或修改，根据文件路径获取文件头信息出现异常");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e,"群组新增或修改，关闭流出现异常。");
                }
            }
        }
        return value;
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
        for (byte aSrc : src) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(aSrc & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
    private boolean checkRepeat(HashSet<String> original,String target) {
        if(original.contains(target)){
            return false;
        }else{
            original.add(target);
        }
        return true;
    }
}
