/**
 * @description 群组管理
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:56:02
 */
package com.montnets.emp.wxgl.svt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiGroup;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.biz.GroupManageBiz;


/**
 * @description 群组管理模块功能
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:56:02
 */

public class weix_groupManagerSvt extends BaseServlet
{

    /**
     * @description
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午03:56:06
     */
    private static final long   serialVersionUID = 805261869675644019L;

    /**
     * 资源路径
     */
    private static final String PATH             = "/wxgl/group";

    /**
     * 声明一个公用的baseBiz对象
     */
    private final BaseBiz  baseBiz          = new BaseBiz();

    /**
     * 群组管理页面
     * 
     * @description
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午05:43:26
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 默认查询全部公共账号的用户列表信息
        this.findGroupInfoList(request, response);
    }

    /**
     * 分组列表查询
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-30 下午01:08:27
     */
    public void findGroupInfoList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = pageSet(pageInfo, request);
        
        //添加与日志相关
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
        
        try
        {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");

            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            // 获取查询条件
            if(!isFirstEnter)
            {
                // 公众账号ID
                String aid = request.getParameter("aid");
                if(null != aid && !"".equals(aid.trim()))
                {
                    conditionMap.put("aid", aid.trim());
                    request.setAttribute("aid", aid);

                    // 群组名称(此目的在于用户必须选择公共账号才能查看此公共账号的群组)
                    String gname = request.getParameter("gname");
                    if(null != gname && !"".equals(gname.trim()))
                    {
                        conditionMap.put("gname", gname.trim());
                    }
                }
            }

            // 用来存储所有分组信息
            GroupManageBiz groupManageBiz = new GroupManageBiz();
            List<DynaBean> groupbeans = groupManageBiz.getGroupInfoList(lgcorpcode, conditionMap, pageInfo);

            conditionMap.clear();

            // 用来存储所有公众账号
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWeiAccount> otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);

            request.setAttribute("otWeiAccList", otWeiAccList);
            request.setAttribute("groupbeans", groupbeans);

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "用户管理-关注者列表页面加载出错！");
        }
        finally
        {
        	
        	//添加与日志相关 
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			//增加操作日志 
			Object loginSysuserObj = null;
			try {
				loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e, "session为null");
			}
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
				
				EmpExecutionContext.info("群组管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
        	
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/yhgl_groupManager.jsp").forward(request, response);
        }
    }

    /**
     * 根据分组ID查看分组详情
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午05:58:52
     */
    public void findGroupById(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            // 群组ID
            String gid = request.getParameter("gid");
            if(null != gid && !"".equals(gid.trim()))
            {
                LfWeiGroup otWeiGroup = new LfWeiGroup();
                otWeiGroup = baseBiz.getById(LfWeiGroup.class, gid);
                //查询群组对应的公共账号信息
                LfWeiAccount otWeiAccount=new LfWeiAccount();
                otWeiAccount=baseBiz.getById(LfWeiAccount.class, otWeiGroup.getAId());
                request.setAttribute("otWeiAccount", otWeiAccount);
                request.setAttribute("groupinfo", otWeiGroup);
                // 用来表示用户是查看详情，还是修改信息(1：查看详情，2:修改)
            }
            else
            {
                EmpExecutionContext.error("获取群组信息失败！");
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "用户管理-微信群组管理页面加载出错！");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/yhgl_editGroupManager.jsp").forward(request, response);
        }
    }

    /**
     * 修改分组信息
     * 
     * @description 此处需要同步到微信端，调用微信更新分组接口
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午06:02:12
     */
    public void updateGroupById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 用了标识操作结果
        String result = "";
        // 获取事物连接
        Connection conn = baseBiz.getConnection();
        try
        {
            // 用户备注
            String gid = request.getParameter("gid");
            String gname = request.getParameter("gname");
            if(null == gid || "".equals(gid))
            {
                result = "fail";
                EmpExecutionContext.error("获取群组信息失败！");
            }
            else
            {
                // 得到群组对象
                LfWeiGroup otWeiGroup = baseBiz.getById(LfWeiGroup.class, gid);

                if(null != gname && !"".equals(gname.trim()))
                {
                    // 公众账号ID
                    String aid = request.getParameter("aid");
                    LfWeiAccount otWeiAccount = baseBiz.getById(LfWeiAccount.class, Long.valueOf(aid));
                    String openId = otWeiAccount.getOpenId();
                    // 将信息同步更新到微信端
                    WeixBiz weixBiz = new WeixBiz();
                    String token = weixBiz.getToken(aid);
                    String WGId = String.valueOf(otWeiGroup.getWGId());
                    boolean issynok = weixBiz.updateWeixGroup(token, WGId, gname,openId);
                    if(issynok)
                    {
                        // 开启事物
                        baseBiz.beginTransaction(conn);

                        // 将信息保存到数据库
                        otWeiGroup.setName(gname);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        otWeiGroup.setModifytime(Timestamp.valueOf(df.format(new Date())));
                        boolean isupdate = baseBiz.updateObj(conn, otWeiGroup);
                        if(isupdate)
                        {
                            // 更新成功
                            result = "success";
                            baseBiz.commitTransaction(conn);
                        }
                        else
                        {
                            EmpExecutionContext.error("修改群组名称失败！");
                            // 更新失败
                            result = "fail";
                            baseBiz.rollBackTransaction(conn);
                        }
                    }
                    else
                    {
                        EmpExecutionContext.error("同步信息到微信失败！");
                        // 更新失败
                        result = "fail";
                        baseBiz.rollBackTransaction(conn);
                    }

                }
                else
                {
                    EmpExecutionContext.error("群组名称为空！");
                    baseBiz.rollBackTransaction(conn);
                }
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "用户管理-微信群组管理页面加载出错！");
            baseBiz.rollBackTransaction(conn);
        }
        finally
        {
            baseBiz.closeConnection(conn);
            response.getWriter().print(result);
        }
    }

    /**
     * 新增群组
     * 
     * @description 此处需要同步更新到微信，调用微信新增分组接口
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午06:07:32
     */
    public void addGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 用了标识操作结果
        String result = "";
        String type = "";
        // 获取事物连接
        Connection conn = baseBiz.getConnection();
        try
        {
            // 操作类型
            type = request.getParameter("type");
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            if(null == type || "".equals(type))
            {
                // 群组名称
                String gname = request.getParameter("gname");
                String aid = request.getParameter("aid");
                if(null == aid || "".equals(aid))
                {
                    EmpExecutionContext.error("无法获取公共账号信息！");
                    // 保存成功
                    result = "fail";
                }
                else
                {
                    if(null != gname && !"".equals(gname.trim()))
                    {
                        // 将信息同步更新到微信端
                        WeixBiz weixBiz = new WeixBiz();
                        String token = weixBiz.getToken(aid);
                        LfWeiGroup otWeiGroup = weixBiz.createWeixGroup(token, gname);
                        if(null != otWeiGroup)
                        {
                            // 开启事物
                            baseBiz.beginTransaction(conn);

                            // 将信息保存到数据库
                            LfWeiGroup otWeiGroupinfo = new LfWeiGroup();
                            otWeiGroupinfo.setName(gname);
                            otWeiGroupinfo.setAId(Long.parseLong(aid));
                            otWeiGroupinfo.setCorpCode(lgcorpcode);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            otWeiGroupinfo.setCreatetime(Timestamp.valueOf(df.format(new Date())));
                            otWeiGroupinfo.setWGId(otWeiGroup.getWGId());
                            Long addlLong = baseBiz.addObjProReturnId(conn, otWeiGroupinfo);
                            if(null == addlLong || addlLong == 0L)
                            {
                                EmpExecutionContext.error("新增群组失败！");
                                // 保存失败
                                result = "fail";
                                baseBiz.rollBackTransaction(conn);
                            }
                            else
                            {
                                // 保存成功
                                result = "success";
                                baseBiz.commitTransaction(conn);
                            }
                        }
                    }
                    else
                    {
                        EmpExecutionContext.error("群组名称为空！");
                        baseBiz.rollBackTransaction(conn);
                    }
                }
            }
            else
            {
                // 用来存储所有公众账号
                LinkedHashMap<String, String> actConditionMap = new LinkedHashMap<String, String>();
                actConditionMap.put("corpCode", lgcorpcode);
                List<LfWeiAccount> otWeiAccList = new ArrayList<LfWeiAccount>();
                otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, actConditionMap, null);
                request.setAttribute("otWeiAccList", otWeiAccList);
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "用户管理-微信群组管理页面加载出错！");
            baseBiz.rollBackTransaction(conn);
        }
        finally
        {
            baseBiz.closeConnection(conn);
            if(null == type || "".equals(type))
            {
                response.getWriter().print(result);
            }
            else
            {
                request.getRequestDispatcher(PATH + "/yhgl_addGroupManager.jsp").forward(request, response);
            }
        }
    }

    /**
     * 根据公共账号的AId，判断是否已经同步过
     * 
     * @description 创建群组前先判断此公共账号是否已经进行过同步操作，未同步则必须先进行同步操作
     * @param request
     * @param response
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-9 上午11:19:59
     */
    public void judgeAccIsSynched(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "";
        try
        {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 公众账号aid
            String aid = request.getParameter("aid");
            if(null != aid && !"".equals(aid.trim()))
            {
                // 用来存储所有公共账号信息
                LinkedHashMap<String, String> groupConditionMap = new LinkedHashMap<String, String>();
                groupConditionMap.put("AId", aid);
                groupConditionMap.put("corpCode", lgcorpcode);
                List<LfWeiAccount> otWeiAccountList = baseBiz.getByCondition(LfWeiAccount.class, groupConditionMap, null);

                if(null != otWeiAccountList)
                {
                    LfWeiAccount otWeiAccount = otWeiAccountList.get(0);

                    // 同步状态
                    result = "success@" + String.valueOf(otWeiAccount.getSyncState());
                }
            }
            else
            {
                result = "fail@";
                EmpExecutionContext.error("获取公众账号对应的群组失败！");
            }
        }
        catch (Exception e)
        {
            result = "fail@";
            EmpExecutionContext.error(e, "用户管理-关注者列表页面加载出错！");
        }
        finally
        {
            response.getWriter().print(result);
        }
    }
    
    /**
     * 获取群组的人员信息
     * @description    
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-25 下午01:47:56
     */
    public void getMembers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String gpId = request.getParameter("gpId");
        String aId = request.getParameter("aId");
        LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
        conditionMap.put("AId", aId);
        conditionMap.put("GId", gpId);
        try
        {
            //当前编辑组成员
            List<LfWeiUserInfo> userInfos = baseBiz.getByCondition(LfWeiUserInfo.class, conditionMap, null);
            
            GroupManageBiz groupManageBiz = new GroupManageBiz();
            
            //下拉列表群组
            List<LfWeiGroup> groups  = groupManageBiz.getGroupList(aId,gpId);
            LfWeiGroup otWeiGroup = new LfWeiGroup();
            if(groups!=null&&groups.size()>0){
                //下拉列表中被选中的群组(第一个会被选中)
                otWeiGroup = groups.get(0);
            }
            //下拉列表中被选中的群组的成员
            List<LfWeiUserInfo> unUseInfos = groupManageBiz.getUnGroupMembers(aId,otWeiGroup);
            //下拉列表中被选中的群组的成员json
            String ungroupMemberJson = groupManageBiz.getGroupMemberJson(unUseInfos);
            //当前编辑组成员json
            String groupMemberJson = groupManageBiz.getGroupMemberJson(userInfos);
            //下拉列表群组json
            String groupsJson = groupManageBiz.getGroups(groups);
            request.setAttribute("userInfos", groupMemberJson);
            request.setAttribute("groups",groupsJson);
            request.setAttribute("unUseInfos", ungroupMemberJson);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取群组信息失败");
        }
        request.getRequestDispatcher(PATH + "/yhgl_groupMember.jsp").forward(request, response);
    }
    
    
    public void getGroupUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String gpId = request.getParameter("gpId");
        String addUids = request.getParameter("addUids");
        String delUids = request.getParameter("delUids");
        String wgId = request.getParameter("wgId");
        try
        {
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
            conditionMap.put("GId", gpId);
            if(null != addUids && addUids.length() > 1)
            {
                addUids = addUids.substring(1,addUids.length()-1);
                conditionMap.put("wcId&not in", addUids);
            }
            List<LfWeiUserInfo>  userInfos = baseBiz.getByCondition(LfWeiUserInfo.class, conditionMap, null);
            if("0".equals(wgId) && null != delUids && delUids.length() > 1)
            {
                delUids = delUids.substring(1,delUids.length()-1);
                conditionMap.clear();
                conditionMap.put("wcId&in", delUids);
                userInfos.addAll(baseBiz.getByCondition(LfWeiUserInfo.class, conditionMap, null));
            }
            GroupManageBiz groupManageBiz = new GroupManageBiz();
            response.getWriter().print(groupManageBiz.getGroupMemberJson(userInfos));
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取群组人员信息失败");
        }
    }

    /**
     * 管理群组成员（移动其它组的成员到此组，或者移除本组成员到未分组）
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-2-25 上午10:41:49
     */
    public void managerGroupMember(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // 用了标识操作结果
        String result = "fail";
        // 获取事物连接
        Connection conn = null;
        try
        {
            // 以下取代所有“，”为空格然后去掉前后空格是为了处理没有数据时永远都传了个“，”的问题
            // 需要移入本组的用户id（以","分隔的字符串数组）和本组id（从其它组移动到本组）
            String addUserIds = request.getParameter("addUserIds");
            if(null != addUserIds && !"".equals(addUserIds))
            {
                addUserIds = addUserIds.replaceAll(",", " ").trim();
            }
            String addToGid = request.getParameter("addToGid");
            if(null == addToGid || "".equals(addToGid))
            {
                addToGid = "";
            }

            // 需要从本组移除的用户id（以","分隔的字符串数组）和未分组id（本组移动到未分组）
            String delUserIds = request.getParameter("delUserIds");
            if(null != delUserIds && !"".equals(delUserIds))
            {
                delUserIds = delUserIds.replaceAll(",", " ").trim();
            }
            String delToGid = "";
            
            String aid = request.getParameter("aid");
            if(null == aid || "".equals(aid))
            {
                return;
            }

            // 查询出本公共账号的未分组id
            LinkedHashMap<String, String> groupConditionMap = new LinkedHashMap<String, String>();
            groupConditionMap.put("AId", aid);
            groupConditionMap.put("WGId", "0");
            List<LfWeiGroup> otWeiGroupList = baseBiz.getByCondition(LfWeiGroup.class, groupConditionMap, null);

            if(null != otWeiGroupList)
            {
                LfWeiGroup otWeiGroup = otWeiGroupList.get(0);
                delToGid = String.valueOf(otWeiGroup.getGId());
            }
            
            // 以下操作：只要有群组成员变化，每次都会重新统计“本组”和”未分组“的成员人数
            String gpids = request.getParameter("gpids");
            if(null != gpids && !"".equals(gpids))
            {
                gpids = gpids.replaceAll(",", " ").trim();
            }
            if(!"".equals(addToGid))
            {
                gpids = gpids + " " + addToGid;
            }
            if(!"".equals(delToGid))
            {
                gpids = gpids + " " + delToGid;
            }
            if(null == gpids || "".equals(gpids))
            {
                // 说明没有要修改的群组
                result = "success";
                return;
            }
            
            conn = baseBiz.getConnection();
            
            // 开启事物
            baseBiz.beginTransaction(conn);

            // 一、将其它组的人移动到本组
            if((null != addUserIds && !"".equals(addUserIds.trim())) && (null != addToGid && !"".equals(addToGid.trim())))
            {
                String[] addwcids = addUserIds.split(" ");
                
                for (int i = 0; i < addwcids.length; i++)
                {
                    Long addwcid = Long.valueOf(addwcids[i]);
                    result = updateUserGroupId(conn, result, addwcid, addToGid);
                    if(result.equals("fail"))
                    {
                        break;
                    }
                }
            }
            else
            {
                //表示没有需要移入本组的用户，操作还是成功的
                result = "success";
            }

            // 二、从本组移除用户到未分组
            if(result.equals("success"))
            {
                if((null != delUserIds && !"".equals(delUserIds.trim())) && (null != delToGid && !"".equals(delToGid.trim())))
                {
                    String[] delwcids = delUserIds.split(" ");
                    for (int i = 0; i < delwcids.length; i++)
                    {
                        Long delwcid = Long.valueOf(delwcids[i]);
                        result = updateUserGroupId(conn, result, delwcid, delToGid);
                        if(result.equals("fail"))
                        {
                            break;
                        }
                    }
                }
                else
                {
                    //表示没有需要移除本组的用户，操作还是成功的
                    result = "success";
                }
            }

            if(result.equals("success"))
            {
                //此处修改用户群组信息成功后提交是为了释放otweiinfo的锁，方便下面查询
                baseBiz.commitTransaction(conn);
                gpids = gpids.trim().replaceAll(" ", ",").trim();
                result = updateGroupCount(conn, result, aid, gpids);
            }
            
            if(result.equals("success"))
            {
                baseBiz.commitTransaction(conn);
            }
            else
            {
                baseBiz.rollBackTransaction(conn);
            }
        }
        catch (Exception e)
        {
            result = "fail";
            EmpExecutionContext.error(e, "用户管理-关注者列表页面加载出错！");
            if(null != conn)
            {
                baseBiz.rollBackTransaction(conn);
            }
        }
        finally
        {
            if(null != conn)
            {
                baseBiz.closeConnection(conn); 
            }
            response.getWriter().print(result);
        }
    }

    /**
     * 更新用户群组id
     * 
     * @description
     * @param conn
     * @param result
     * @param wcid
     * @param gid
     * @return
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-2-25 上午11:20:18
     */
    private String updateUserGroupId(Connection conn, String result, Long wcid, String gid) throws Exception
    {
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        
        objectMap.put("GId", gid);
        conditionMap.put("wcId", String.valueOf(wcid));
        boolean isupdateuser = baseBiz.update(conn, LfWeiUserInfo.class, objectMap, conditionMap);
        
        if(isupdateuser)
        {
           // result = "success";
        	  return "success";
        }
        else
        {
           // result = "fail";
        	 return "fail";
        }

      
    }
    
    /**
     * 更新群组数量
     * @description    
     * @param conn
     * @param result
     * @param gpids
     * @return
     * @throws Exception       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-2-25 下午05:51:18
     */
    private String updateGroupCount(Connection conn, String result,String aid, String gpids) throws Exception
    {
        GroupManageBiz groupManageBiz = new GroupManageBiz();
        boolean isupdateuser = groupManageBiz.updateGroupCount(conn, gpids);
        if(isupdateuser)
        {
//            result = "success";
        	return "success";
        }
        else
        {
//            result = "fail";
            return "fail";
        }
      
    }
}
