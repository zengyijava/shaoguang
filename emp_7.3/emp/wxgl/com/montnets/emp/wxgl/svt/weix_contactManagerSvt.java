/**
 * @description 用户管理模块
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:37:13
 */
package com.montnets.emp.wxgl.svt;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiGroup;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.biz.UserManageBiz;
import com.montnets.emp.wxgl.dao.GroupManageDao;

/**
 * @description 用户管理模块
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:37:13
 */

public class weix_contactManagerSvt extends BaseServlet
{

    /**
     * @description serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午03:56:20
     */
    private static final long   serialVersionUID = 6834167819550902714L;

    /**
     * 资源路径
     */
    private static final String PATH  = "/wxgl/user";

    /**
     * 声明一个公用的baseBiz对象
     */
    private final BaseBiz baseBiz   = new BaseBiz();

    /**
     * 用戶管理页面
     *   获取公众账号对应的关注者列表信息（根据用户的查询条件）
     * @description 默认查询方法
     * @param request
     *        HttpServletRequest
     * @param response
     *        HttpServletResponse
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-2 上午11:09:07
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = pageSet(pageInfo, request);
        List<LfWeiGroup> otWeiGroupList = new ArrayList<LfWeiGroup>();
        
        //添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
        
        try
        {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
         // 公众账号ID
            String aid = request.getParameter("a_id");
            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            conditionMap.put("corpCode", lgcorpcode);

            // 用来存储所有公众账号
            List<LfWeiAccount> otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
           
            conditionMap.clear();
            
            // 获取查询条件
            if(!isFirstEnter)
            {
                // 用户昵称
                String nickname = request.getParameter("nickname");
                if(null != nickname && !"".equals(nickname.trim()))
                {
                    conditionMap.put("nickname", nickname.trim());
                }
                
                // 性别
                String sex = request.getParameter("sex");
                if(null != sex && !"".equals(sex.trim()))
                {
                    conditionMap.put("sex", sex.trim());
                }
                
                // 地区
                String place = request.getParameter("place");
                if(null != place && !"".equals(place.trim()))
                {
                    conditionMap.put("place", place.trim());
                }
                
                // 用户状态（是否关注 0：未关注   1：已关注）
                String subscribe = request.getParameter("subscribe");
                if(null != subscribe && !"".equals(subscribe.trim()))
                {
                    conditionMap.put("subscribe", subscribe.trim());
                }

                
                if(null != aid && !"".equals(aid.trim()))
                {
                    conditionMap.put("aid", aid.trim());

                    // 用户组ID(此目的在于用户必须选择公共账号才能查看此公共账号的群组)
                    String gid = request.getParameter("gid");
                    if(null != gid && !"".equals(gid.trim()))
                    {
                         conditionMap.put("gid", gid.trim());
                    }
                }
            }
            else
            {   
            	/*备份p
                if(null != otWeiAccList)
                */
            	if(null != otWeiAccList && otWeiAccList.size()>0)  //新增p
                {
                    aid = String.valueOf(otWeiAccList.get(0).getAId());
                    conditionMap.put("aid", aid);
                }
            }

            // 用来存储集团所有关注者
            UserManageBiz userManageBiz = new UserManageBiz();
            List<DynaBean> userbeans = userManageBiz.getUserInfoList(lgcorpcode, conditionMap, pageInfo);

            conditionMap.clear();
            
            if(null != aid && !"".equals(aid.trim()))
            {                
                conditionMap.put("AId", aid.trim());
                // 用来存储所有分组信息
                otWeiGroupList = baseBiz.getByCondition(LfWeiGroup.class, conditionMap, null);
            }
            int count=0;
            for(int k=0;k<userbeans.size();k++){
            	DynaBean acc=userbeans.get(k);
            	if(acc.get("groupname")==null||"".equals(acc.get("groupname").toString())){
            		count=count+1;
            	}
            }
            for(int s=0;s<otWeiGroupList.size();s++){
            	LfWeiGroup group=otWeiGroupList.get(s);
            	if("未分组".equals(group.getName())){
            		int cou=group.getCount()==null?0:Integer.parseInt(group.getCount());
            		group.setCount(cou+count+"");
            	}
            }
            
            request.setAttribute("userbeans", userbeans);
            request.setAttribute("otWeiAccList", otWeiAccList);
            request.setAttribute("otWeiGroupList", otWeiGroupList);

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
				
				EmpExecutionContext.info("用户列表", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
        	
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/yhgl_contactManager.jsp").forward(request, response);
        }
    }


    /**
     * 根据关注者ID查出用户信息
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-2 上午11:54:51
     */
    public void findFollowerById(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 用户wcid
            String wcid = request.getParameter("wcid");
            // 用户操作类型(1：查看详情，2:修改)
            String type = request.getParameter("type");
            if(null != wcid && !"".equals(wcid.trim()))
            {
                LfWeiUserInfo otWeiUserInfo = new LfWeiUserInfo();
                otWeiUserInfo = baseBiz.getById(LfWeiUserInfo.class, wcid);
                request.setAttribute("userinfo", otWeiUserInfo);

                // 用来存储当前用户所属公共账号的所有分组信息
                LinkedHashMap<String, String> groupConditionMap = new LinkedHashMap<String, String>();
                groupConditionMap.put("corpCode", lgcorpcode);
                groupConditionMap.put("AId", String.valueOf(otWeiUserInfo.getAId()));
                List<LfWeiGroup> otWeiGroupList = new ArrayList<LfWeiGroup>();
                otWeiGroupList = baseBiz.getByCondition(LfWeiGroup.class, groupConditionMap, null);

                request.setAttribute("otWeiGroupList", otWeiGroupList);
                // 用来表示用户是查看详情，还是修改信息(1：查看详情，2:修改)
                request.setAttribute("type", type);
            }
            else
            {
                EmpExecutionContext.error("获取用户信息失败！");
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "用户管理-关注者列表页面加载出错！");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/yhgl_editManager.jsp").forward(request, response);
        }
    }

    /**
     * 修改用户群组和备注信息
     * 
     * @description 根据用户id查找用户信息并修改用户配注
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-2 下午01:43:10
     */
    public void updateFollowerById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	// 用了标识操作结果
        String result = "fail";
        // 获取事物连接
        Connection conn = baseBiz.getConnection();
        // 存放查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        // 存放要更新的字段
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        try
        {
            // 用户备注
            String wcid = request.getParameter("wcid");
            String gid = request.getParameter("gid");
            String descr = request.getParameter("desc");
            if(null == wcid || "".equals(wcid))
            {
                result = "fail";
                EmpExecutionContext.error("获取用户信息失败！");
            }
            else
            {

                if((null != gid && !"".equals(gid.trim())) || (null != descr && !"".equals(descr.trim())))
                {
                	LfWeiUserInfo otWeiUserInfo=baseBiz.getById(LfWeiUserInfo.class,wcid);
                    // 开启事物
                    baseBiz.beginTransaction(conn);
                 
                        conditionMap.clear();
                        conditionMap.put("wcId", wcid);
                        objectMap.put("GId", gid);
                        objectMap.put("descr", descr);
                        String gids=gid+","+otWeiUserInfo.getGId();
                        // 在更新信息之前保存旧群组ID，方便下面对修改数量修改用
                        baseBiz.update(conn, LfWeiUserInfo.class,objectMap,conditionMap); 
                    // 计算群组的数量
                    if(new GroupManageDao().updateGroupCount(conn, gids))
                	{
                    	result="success";
                	}
                
                }
                else
                {
                    EmpExecutionContext.error("用户备注信息/群组信息获取为空！");
                    baseBiz.rollBackTransaction(conn);
                }
                if("success".equals(result))
                {
                	baseBiz.commitTransaction(conn);
                }else
                {
                	baseBiz.rollBackTransaction(conn);
                }
            }

        }
        catch (Exception e)
        {
            result = "fail";
            EmpExecutionContext.error(e, "用户管理-关注者列表页面加载出错！");
            baseBiz.rollBackTransaction(conn);
        }
        finally
        {
            baseBiz.closeConnection(conn);
            response.getWriter().print(result);
        }
    }

    /**
     * 根据公共账号的AId，加载所有公众账号群组
     * 
     * @description 根据公共账号的AId，加载所有公众账号群组
     * @param request
     * @param response
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-9 上午11:19:59
     */
    public void findGroupByAId(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "";
        try
        {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 公众账号aid
            String aid = request.getParameter("a_id");
            if(null != aid && !"".equals(aid.trim()))
            {
                // 用来存储所有分组信息
                LinkedHashMap<String, String> groupConditionMap = new LinkedHashMap<String, String>();
                groupConditionMap.put("corpCode", lgcorpcode);
                groupConditionMap.put("AId", aid);
                List<LfWeiGroup> otWeiGroupList = baseBiz.getByCondition(LfWeiGroup.class, groupConditionMap, null);

                if(null != otWeiGroupList)
                {
                    StringBuffer sb = new StringBuffer();
                    sb.append("[");

                    // 得到集合的长度
                    int size = otWeiGroupList.size();
                    int i = 0;

                    for (LfWeiGroup otWeiGroup : otWeiGroupList)
                    {
                        i++;

                        String gid = String.valueOf(otWeiGroup.getGId());
                        String gname = otWeiGroup.getName();
                        String gcount = otWeiGroup.getCount();
                        if(null == gcount || "".equals(gcount))
                        {
                            gcount = "0";
                        }

                        sb.append("{\"gid\":\"");
                        sb.append(gid);
                        sb.append("\",\"gname\":\"");
                        sb.append(gname);
                        sb.append("\",\"gcount\":\"");
                        sb.append(gcount);
                        sb.append("\"}");

                        // 如果i小于size字符串sb中加","
                        if(i < size)
                        {
                            sb.append(",");
                        }
                    }

                    sb.append("]");

                    // 拼好的字符串赋值给变量
                    result = "success@" + sb.toString();
                }
                else
                {
                    result = "success@[]";
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
     * 批量移动用户群组
     * @description    
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-2-24 下午07:17:32
     */
    public void moveUserGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	// 用了标识操作结果
        String result = "fail";
        // 获取事物连接
        Connection conn = baseBiz.getConnection();
        // 存放查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        // 存放要更新的字段
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        try
        {
            String userIds = request.getParameter("userIds");
            String gid = request.getParameter("usergid");
            String gids = request.getParameter("gids");
            if(null == userIds || "".equals(userIds))
            {
                EmpExecutionContext.error("获取用户信息失败！");
            }
            else
            {

                if(null != gid && !"".equals(gid.trim()))
                {
                    // 开启事物
                    baseBiz.beginTransaction(conn);

                    String[] wcids = userIds.split(",");
                    for (int i = 0; i < wcids.length; i++)
                    {
                        conditionMap.clear();
                        conditionMap.put("wcId", wcids[i]);
                        
                        objectMap.put("GId", gid);
                        // 在更新信息之前保存旧群组ID，方便下面对修改数量修改用
                        baseBiz.update(conn, LfWeiUserInfo.class,objectMap,conditionMap);
                    }  
                    // 计算群组的数量
                    if(new GroupManageDao().updateGroupCount(conn, gids))
                	{
                    	result="success";
                	}
                }
                else
                {
                    EmpExecutionContext.error("用户备注信息/群组信息获取为空！");
                }
                if("success".equals(result))
                {
                	baseBiz.commitTransaction(conn);
                }else
                {
                	baseBiz.rollBackTransaction(conn);
                }
            }
        }
        catch (Exception e)
        {
            result = "fail";
            EmpExecutionContext.error(e, "用户管理-移动到分组功能异常！");
            baseBiz.rollBackTransaction(conn);
        }
        finally
        {
            baseBiz.closeConnection(conn);
            response.getWriter().print(result);
        }
    }
}
