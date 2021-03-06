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

    private final String UNVAILED_USER = "????????????";

    /**
     * ?????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ??????
     */
    public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            //???Session??????????????????????????????
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //???Session??????????????????????????????ID
            String lguserid = String.valueOf(loginSysuser.getUserId());
            //???Session??????????????????????????????????????????
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
            EmpExecutionContext.error(e, "????????????????????????????????????");
        }
    }

    /**
     * ??????????????????????????????,?????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ??????
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
            EmpExecutionContext.error(e, "??????????????????????????????????????????");
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ??????
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
            EmpExecutionContext.error(e, "??????????????????????????????????????????????????????");
        }
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ??????
     */
    public void getClientSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            //???Session??????????????????????????????
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //???Session??????????????????????????????ID
            String lguserid = String.valueOf(loginSysuser.getUserId());
            //???Session??????????????????????????????????????????
            String corpCode = loginSysuser.getCorpCode();
            //??????????????????????????????
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
                    //???????????????????????????id
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
            EmpExecutionContext.error(e, "????????????????????????????????????");
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws Exception ??????
     */
    public void getRmsGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            String corpCode = lfSysuser.getCorpCode();
            //??????ID
            String userId = lfSysuser.getUserId().toString();
            //3.??????(??????+??????) 4.???????????? 5.????????????
            String groupType = request.getParameter("groupType");
            //????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
    }

    /**
     * ?????????ID??????????????????????????????/????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ??????
     */
    public void getGroupUserByGroupId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
            StringBuilder sb = new StringBuilder();
            String groupId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            String type = request.getParameter("type");
            //???????????? 0???????????? 1????????????
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
                        /*??????*/
                        case 0:
                            grpupInfoDto.setIsDep(4);
                            break;
                        /*??????*/
                        case 1:
                            grpupInfoDto.setIsDep(5);
                            break;
                        /*?????? ??? ??????*/
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
            EmpExecutionContext.error(e, "????????????????????????????????????");
        }
    }

    /**
     * ???????????????????????????
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
            //??????????????????  1.???????????????   2.???????????????  3.??????(??????+??????) 4.???????????? 5.???????????? 6.???????????? 7.????????????
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
                //??????html
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
            EmpExecutionContext.error(e, "????????????-????????????????????????-??????????????????????????????????????????");
        }
    }

    /**
     * ???????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ????????????
     */
    public void getClientDepCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //?????????????????? 0?????????   1??????
            String ismut = request.getParameter("ismut");
            //??????ID
            String depId = request.getParameter("depId");
            //??????????????????
            if ("0".equals(ismut)) {
                //??????????????????????????????
                //???????????????????????? ???????????????????????????????????????
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
            EmpExecutionContext.error(e, " ????????????????????????????????????????????????????????????");
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ????????????
     */
    public void isClientDepContaineDeps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //?????????????????? 0?????????   1??????
            String ismut = request.getParameter("ismut");
            //??????ID
            String depId = request.getParameter("depId");
            //??????????????????
            if ("0".equals(ismut)) {
                //???????????????????????? ???????????????????????????????????????
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
            //???????????????????????????
            List<LfClientDep> lfClientDepList = null;
            LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
            //????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //??????????????????
            String cliDepIds = request.getParameter("cliDepIds");
            String[] depIds = cliDepIds.split(",");
            List<Long> depIdExistList = new ArrayList<Long>();
            //??????
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
            //????????????????????????????????????????????????????????????set??????
            LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
            if (dep != null) {
                conditionMap.put("deppath&like", dep.getDeppath());
                conditionMap.put("corpCode", dep.getCorpCode());
                lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
                if (lfClientDepList != null && lfClientDepList.size() > 0) {
                    for (LfClientDep aLfClientDepList : lfClientDepList) {
                        depIdSet.add(aLfClientDepList.getDepId());
                    }
                    //??????????????????????????????ID????????????
                    List<Long> depIdListTemp = new ArrayList<Long>();
                    for (Long aDepIdExistList : depIdExistList) {
                        if (depIdSet.contains(aDepIdExistList)) {
                            depIdListTemp.add(aDepIdExistList);
                        }
                    }

                    //????????????????????????????????????????????????????????????????????????????????????????????????select???html
                    String depids = depIdSet.toString();
                    depids = depids.substring(1, depids.length() - 1);
                    //??????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????????????????");
            response.getWriter().print("errer");
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ????????????
     */
    public void isClientDepContained(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //???????????????ID
            String depId = request.getParameter("depId");
            //????????????????????????ID
            String clientDepIds = request.getParameter("cliDepIds");
            //??????IDS
            String[] depIds = clientDepIds.split(",");
            //????????????
            List<LfClientDep> lfClientDepList = null;
            //????????????????????????ID?????????
            LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
            //??????????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //????????????
            LfClientDep dep = null;
            //??????
            for (String id : depIds) {
                //???????????????????????????????????????????????????
                if (id == null || "".equals(id)) {
                    continue;
                }
                //????????????????????????
                if (id.equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                    //??????????????????????????????????????????
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
                    //?????????????????????????????????
                } else {
                    dep = null;
                    dep = baseBiz.getById(LfClientDep.class, Long.valueOf(id));
                    if (dep != null) {
                        depIdsSet.add(dep.getDepId());
                    }
                }
            }
            boolean isFlag = false;
            //???????????????????????????
            if (depIdsSet.size() > 0) {
                Long tempDepId = Long.valueOf(depId);
                isFlag = depIdsSet.contains(tempDepId);
            }
            //??????
            if (isFlag) {
                response.getWriter().print("depExist");
            } else {
                response.getWriter().print("noExist");
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "?????????????????????????????????????????????????????????");
        }
    }

    /**
     * ???????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ????????????
     */
    public void getEmpDepCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //?????????????????? 0?????????   1??????
            String ismut = request.getParameter("ismut");
            //??????ID
            String depId = request.getParameter("depId");
            //??????????????????
            if ("0".equals(ismut)) {
                //???????????????????????? ???????????????????????????????????????
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
                            //??????????????????
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
            EmpExecutionContext.error(e, "??????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ????????????
     */
    public void isDepContaineDeps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //?????????????????? 0?????????   1??????
            String ismut = request.getParameter("ismut");
            //??????ID
            String depId = request.getParameter("depId");
            //??????????????????
            if ("0".equals(ismut)) {
                //???????????????????????? ???????????????????????????????????????
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
            //???????????????????????????
            List<LfEmployeeDep> lfEmployeeDepList = null;
            LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
            //????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //??????????????????
            String empDepIds = request.getParameter("empDepIds");
            String[] depIds = empDepIds.split(",");
            List<Long> depIdExistList = new ArrayList<Long>();
            //??????
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
            //????????????????????????????????????????????????????????????set??????
            LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(depId));
            if (dep != null) {
                conditionMap.put("deppath&like", dep.getDeppath());
                conditionMap.put("corpCode", dep.getCorpCode());
                lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
                if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0) {
                    for (LfEmployeeDep aLfEmployeeDepList : lfEmployeeDepList) {
                        depIdSet.add(aLfEmployeeDepList.getDepId());
                    }
                    //??????????????????????????????ID????????????
                    List<Long> depIdListTemp = new ArrayList<Long>();
                    for (Long aDepIdExistList : depIdExistList) {
                        if (depIdSet.contains(aDepIdExistList)) {
                            depIdListTemp.add(aDepIdExistList);
                        }
                    }
                    //????????????????????????????????????????????????????????????????????????????????????????????????select???html
                    String depids = depIdSet.toString();
                    depids = depids.substring(1, depids.length() - 1);
                    //??????????????????
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
            EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
            response.getWriter().print("errer");
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ????????????
     */
    public void isEmpDepContained(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //???????????????ID
            String depId = request.getParameter("depId");
            //????????????????????????ID
            String empDepIds = request.getParameter("empDepIds");
            //??????IDS
            String[] depIds = empDepIds.split(",");
            //????????????
            List<LfEmployeeDep> lfEmployeeDepList = null;
            //????????????????????????ID?????????
            LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
            //??????????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //????????????
            LfEmployeeDep dep = null;
            //??????
            for (String id : depIds) {
                //???????????????????????????????????????????????????
                if (id == null || "".equals(id)) {
                    continue;
                }
                //????????????????????????
                if (id.equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                    //??????????????????????????????????????????
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
                    //?????????????????????????????????
                } else {
                    dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(id));
                    if (dep != null) {
                        depIdsSet.add(dep.getDepId());
                    }
                }
            }
            boolean isFlag = false;
            //???????????????????????????
            if (depIdsSet.size() > 0) {
                Long tempDepId = Long.valueOf(depId);
                isFlag = depIdsSet.contains(tempDepId);
            }
            //??????
            if (isFlag) {
                response.getWriter().print("depExist");
            } else {
                response.getWriter().print("noExist");
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param request  request??????
     * @param response response??????
     * @throws IOException ????????????
     */
    public void getClientAttrList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
        LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        //???Session??????????????????????????????????????????
        String corpCode = loginSysuser.getCorpCode();
        // ????????????
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            // ???????????????????????????
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
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        }
    }

    /**
     * ??????????????????fieldId????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getCustFieldMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ????????????
        String corpCode = request.getParameter("corpCode");
        String fieldId = request.getParameter("fieldId");
        // ????????????
        String searchName = request.getParameter("searchName");
        try {
            // ??????????????????????????????
            LfCustFieldValueVo cfvo = new LfCustFieldValueVo();

            // ?????????????????????????????????
            cfvo.setCorp_code(corpCode);
            cfvo.setField_ID(fieldId);
            cfvo.setField_Value(searchName);
            List<LfCustFieldValueVo> proList = userInfoBiz.getCustVos(cfvo);
            //??????html????????????
            response.getWriter().print(JSONObject.toJSONString(proList));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????");
        }
    }

    /**
     * ??????????????????
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
            // ??????????????????????????????
            List<LfBusTaoCan> busTaoCanList = baseBiz.getByCondition(LfBusTaoCan.class, conditionMap, orderByMap);
            if (busTaoCanList != null && busTaoCanList.size() > 0) {
                StringBuilder tcCodes = new StringBuilder();
                for (LfBusTaoCan busTaoCan : busTaoCanList) {
                    tcCodes.append("'").append(busTaoCan.getTaocan_code()).append("',");
                }
                // ????????????id?????????
                tcCodes = new StringBuilder(tcCodes.substring(0, tcCodes.length() - 1));
                Map<String, String> countMap = userInfoBiz.getSignClientMemberCount(tcCodes.toString(), corpCode);
                // ??????html????????????
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
            EmpExecutionContext.error(e, "???????????????????????????");
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSignClientMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
            StringBuilder sb = new StringBuilder();
            //????????????
            String searchName = request.getParameter("searchName");
            //????????????
            String tcCode = request.getParameter("tcCode");
            // ??????
            String pageIndex = request.getParameter("pageIndex");
            if (tcCode == null || "".equals(tcCode.trim())) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????" + tcCode);
                response.getWriter().print("");
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageIndex(Integer.parseInt(pageIndex));
            // ????????????50?????????
            pageInfo.setPageSize(50);
            List<DynaBean> clientBeanList = userInfoBiz.getSignClientMember(searchName, tcCode, pageInfo);
            //??????ID??????
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
            EmpExecutionContext.error(e, "?????????????????????????????????");
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getCustFieldMemberCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ????????????
        String corpCode = request.getParameter("corpCode");
        // ??????????????????
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
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        }
    }

    /**
     * ??????????????????????????????????????????
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
            //???????????????????????????????????????
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
            EmpExecutionContext.error(e, "????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????Z-Tree
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSecondDepJson(HttpServletRequest request, HttpServletResponse response) {
        try {
            LfSysuser sysuser = getLoginUser(request);
            Long depId = null;
            //??????id
            String depStr = request.getParameter("depId");

            if (depStr != null && !"".equals(depStr.trim())) {
                depId = Long.parseLong(depStr);
            }
            //????????????????????????
            String departmentTree = getDepJosnData(depId, sysuser);
            response.getWriter().print(departmentTree);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????????????????????????????????????????");
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
                    LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(sysuser.getUserId(), sysuser.getCorpCode()).get(0);//??????????????????
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
                EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
                tree = new StringBuffer("[]");
            }
        }
        return tree.toString();
    }

    /**
     * ????????????Id?????????????????????
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
            EmpExecutionContext.error(e, "??????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????????????????????????????????????????
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
        //????????????????????????id??????list??????(???????????????e?????????e??????depIdExistList??????)
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
        //???????????????????????????
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        for (String str : depArray) {
            depIdSet.add(Long.valueOf(str));
        }

        //????????????set??????????????????????????????????????????????????????????????????????????????????????????????????????????????????option?????????????????????select??????
        for (Long aDepIdExistList : depIdExistList) {
            if (depIdSet.contains(aDepIdExistList)) {
                depIdListTemp.add(aDepIdExistList);
            }
        }
        //????????????????????????????????????????????????????????????????????????????????????????????????select???html
        String depids = depIdSet.toString();
        depids = depids.substring(1, depids.length() - 1);
        //??????????????????
        String countttt = depBiz.getDepCountByDepId(depids);
        if (depIdListTemp.size() > 0) {
            String tempDeps = depIdListTemp.toString();
            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
            response.getWriter().print(countttt + "," + tempDeps);
        }
        //????????????????????????
        else {
            response.getWriter().print("notContains" + "&" + countttt);
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void checkDepIsExist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            String depIdsExist = request.getParameter("depIdsExist");

            String lgcorpcode = request.getParameter("lgcorpcode");// ??????????????????

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
            // ??????????????????????????????????????????????????????????????????
            boolean result = isDepAcontainsDepB(depIdsTemp.toString(), depId);
            if (result) {
                response.getWriter().print("depExist");
            } else {
                response.getWriter().print("notExist");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
    }

    /**
     * ????????????A??????????????????B
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
        return result;
    }

    /**
     * ??????????????????????????????????????????
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
            //????????????
            while (it.hasNext()) {

                fileItem = it.next();
                //???????????????
                if (!fileItem.isFormField() && fileItem.getName().length() > 0) {
                    String line;
                    String[] haoduan = userInfoBiz.getHaoduan();
                    fileCurName = fileItem.getName();
                    String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
                    if (fileType.equalsIgnoreCase(".xls")) {
                        String fileHeader = getFileHeader(fileItem.getInputStream());//?????????
                        if (!"D0CF11E0".equals(fileHeader)) {//??????????????? xls????????????D0CF11E0
                            continue;
                        }
                        Workbook workBook = Workbook .getWorkbook(fileItem.getInputStream());
                        Sheet sh = workBook.getSheet(0);
                        for (int k = 0; k < sh.getRows(); k++) {
                            LfMalist malist = new LfMalist();
                            Cell[] cells = sh.getRow(k);
                            //??????????????????????????????
                            if(cells.length != 2) continue;
                            String name = cells[0].getContents();
                            String phone = cells[1].getContents();
                            //????????????????????????????????????
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //?????????????????????????????????????????????
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            //????????????????????????????????????
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
                            //????????????????????????????????????
                            line = line.replaceAll("\\s+"," ");
                            String[] nameAndPhone = line.split(" ");
                            if(nameAndPhone.length != 2) continue;
                            String name = nameAndPhone[0];
                            String phone = nameAndPhone[1];
                            //????????????????????????????????????
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //?????????????????????????????????????????????
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            //????????????????????????????????????
                            if (phoneUtil.getPhoneType(phone, haoduan) == -1 || !checkRepeat(repeatList,name+"#HS#"+phone)) {
                                continue;
                            }
                            malist.setName(name);
                            malist.setMobile(phone);
                            lfMalists.add(malist);
                        }
                    } else if (fileType.equalsIgnoreCase(".xlsx")) {
                    	//?????????????????????.xlsx
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
        				//????????????
        				int k=-1;
        				while ((tmp = reader.readLine()) != null && k <= maxNum)
        				{
        					String name="";
        					String phone="";
        					// ?????????????????????
        					if("".equals(tmp)) {
        						continue;
        					}
        					k++;
        					cells = tmp.split(",");
        					int size = cells.length;
        					//?????????
        					if (size >1){
                                 name = cells[1];
        					}
        					//?????????
        					if (size >2){
        						 phone = cells[2];
        					}
        					//??????????????????
        					if (size >3) {
        						continue;
        					}
                            //????????????????????????????????????
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //?????????????????????????????????????????????
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            //????????????????????????????????????
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
            EmpExecutionContext.error(e, "??????????????????????????????????????????????????????");
        }finally {
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                EmpExecutionContext.error(e,"??????????????????????????????????????????");
            }
            if(fileItem != null){
                fileItem.delete();
            }
        }
    }
    /**
     * ???????????????????????????????????????
     *
     * @param inputStream ?????????
     * @return ???????????????
     */
    private String getFileHeader(InputStream inputStream) {
        String value = null;
        try {
            byte[] b = new byte[4];
            /*
             * int read() ????????????????????????????????????????????? int read(byte[] b) ??????????????????????????? b.length
             * ?????????????????????????????? byte ???????????? int read(byte[] b, int off, int len)
             * ??????????????????????????? len ?????????????????????????????? byte ????????????
             */
            inputStream.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"???????????????????????????????????????????????????????????????????????????");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e,"????????????????????????????????????????????????");
                }
            }
        }
        return value;
    }
    /**
     * ???????????????????????????????????????byte???????????????string????????????
     *
     * @param src ????????????????????????????????????byte??????
     * @return ???????????????
     */
    private String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte aSrc : src) {
            // ???????????????????????? 16?????????????????????????????????????????????????????????????????????????????????????????????
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
