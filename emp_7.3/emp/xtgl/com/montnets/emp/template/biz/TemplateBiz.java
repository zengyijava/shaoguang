package com.montnets.emp.template.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.LfMaterialVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.mmsmate.LfMaterial;
import com.montnets.emp.entity.mmsmate.LfMaterialSort;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfShortTemp;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.entity.template.MmsTemplate;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.template.dao.TemplateDAO;
import com.montnets.emp.template.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

/**
 * @author huangzhibin <307260621@qq.com>
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 下午04:09:25
 * @description
 */
public class TemplateBiz extends SuperBiz{

    /**
     * 根据条件获取该操作员的彩信素材信息
     *
     * @param curLoginedUserId
     * @param lfTemplateVo
     * @param pageInfo
     * @return
     * @throws Exception
     */
    public List<LfTemplateVo> getTemplateByCondition(Long curLoginedUserId,
                                                     LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception {
        //当前登录操作员id
        if (curLoginedUserId == null) {
            //返回
            return null;
        }
        //模板对象集合
        List<LfTemplateVo> templateVosList = null;
        try {
            //分页不为空，需要分页
            if (pageInfo != null) {
                //按条件获取记录，分页
                templateVosList = new TemplateDAO()
                        .findLfTemplateVoList(curLoginedUserId, lfTemplateVo,
                                pageInfo);
            } else {
                //按条件获取所有记录
                templateVosList = new TemplateDAO()
                        .findLfTemplateVoList(curLoginedUserId, lfTemplateVo);
            }
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "根据条件获取该操作员的彩信素材信息biz层异常！");
            throw e;
        }
        //返回结果
        return templateVosList;
    }

    /**
     * 添加短彩信模板
     *
     * @param template
     * @return
     * @throws Exception
     */
    public long addTemplate(MmsTemplate mmstemplate, LfTemplate template) throws Exception {
        Long id = 0L;
        boolean issuccess = true;
        // 获取连接
        Connection conn = empTransDao.getConnection();
        LfTmplRela shareTemp = new LfTmplRela();
        try {
            // 开启事务
            empTransDao.beginTransaction(conn);
            ReviewBiz reviewBiz = new ReviewBiz();
            // 判断是否需要审核信息
            LfFlow flow = reviewBiz.checkUserFlow(template.getUserId()
                    , template.getCorpCode(), template.getTmpType().toString());
            // 如果是彩信存草稿也不需要审核
            if (flow == null
                    || (template.getTmpType() == 4 && template.getTmState() == 2)) {

                template.setIsPass(0);
            }
            //如果是彩信模板
            if (mmstemplate != null) {
                shareTemp.setTemplType(2);
                //如果当前用户没有审批流且创建的是启用状态的模板，则需要保存网关平台的彩信模板
                if (flow == null && template.getTmState() == 1) {
                    Long mmsId = empTransDao.saveObjectReturnID(conn, mmstemplate);
                    //将网关平台的彩信模板id保存到系统中的关联字段中去。
                    template.setMmstmplid(mmsId);
                }
            }

            //Oracle NULL会报错
            if (StringUtils.isBlank(template.getExlJson()))
                template.setExlJson(" ");
            if (StringUtils.isBlank(template.getVer()))
                template.setVer(" ");

            // 保存数据库
            id = empTransDao.saveObjectReturnID(conn, template);
            //添加模板到lf_all_template
            shareTemp.setCorpCode(template.getCorpCode());
            shareTemp.setCreaterId(template.getUserId());
            shareTemp.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shareTemp.setTemplId(id);
            shareTemp.setToUser(template.getUserId());
            empTransDao.save(conn, shareTemp);
            if (flow != null && template.getTmState() != 2) {
                // 设置审批信息
                issuccess = reviewBiz.addFlowRecordChild(conn, id,
                        template.getTmName(), template.getAddtime(), template.getTmpType(),
                        flow.getFId(), flow.getRLevelAmount(), template.getUserId(),
                        "1", Integer.valueOf(template.getDsflag().toString()), null);

            }
            if (issuccess) {
                empTransDao.commitTransaction(conn);
            } else {
                empTransDao.rollBackTransaction(conn);
            }

        } catch (Exception e) {
            // 回滚
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "添加短彩信模板biz层异常！");
            throw e;
        } finally {
            // 关闭连接
            empTransDao.closeConnection(conn);
            if (issuccess) {
                // 获取配置文件中的信息
                String isre = SystemGlobals.getSysParam("isRemind");
                if ("0".equals(isre)) {
                    LfFlowRecord record = new LfFlowRecord();
                    record.setInfoType(template.getTmpType());
                    record.setMtId(id);
                    record.setProUserCode(template.getUserId());
                    record.setRLevelAmount(1);
                    record.setRLevel(0);
                    ReviewRemindBiz rere = new ReviewRemindBiz();
                    //调用审批提醒方法
                    rere.flowReviewRemind(record);
                    //调用邮件提醒
                    rere.sendMail(record);
                }
            } else {
                id = -1L;
            }
        }
        return id.longValue();
    }

    /**
     * 彩信模板编辑（只有草稿状态的模板才能进行编辑）
     *
     * @param template
     * @return
     * @throws Exception
     */
    public boolean updateTemplate(MmsTemplate mmstemplate, LfTemplate template) throws Exception {
        // 获取连接
        Connection conn = empTransDao.getConnection();
        boolean result = false;
        try {
            // 开启连接
            empTransDao.beginTransaction(conn);
            ReviewBiz reviewBiz = new ReviewBiz();
            // 是否需要审批
            LfFlow flow = reviewBiz.getFlowBySysUserId(template.getUserId());
            // 存草稿也不需要审核
            if (flow == null || template.getTmState() == 2) {
                template.setIsPass(0);
            }
            //编辑成启用状态的彩信模板,并且无审批流则需要将彩信模板保存到网关彩信平台上去
            if (template.getTmState() == 1 && flow == null) {
                Long mmsId = empTransDao.saveObjectReturnID(conn, mmstemplate);
                //将网关平台的彩信模板id保存到系统中的关联字段中去。
                template.setMmstmplid(mmsId);
            }
            // 保存
            result = empTransDao.update(conn, template);
            boolean issuccess = false;
            if (flow != null && template.getTmState() != 2) {
                issuccess = reviewBiz.addFlowRecords(conn, template.getTmid(), template
                        .getTmpType(), flow);
            }
            // 提交事务
            empTransDao.commitTransaction(conn);

            if (issuccess) {
                // 获取配置文件中的信息
                String isre = SystemGlobals.getSysParam("isRemind");
                if ("0".equals(isre)) {
                    new ReviewRemindBiz().reviewRemindTmp(null, template.getTmid(), 2);
                }
            }

        } catch (Exception e) {
            // 回滚
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "彩信模板编辑biz层异常！");
            result = false;
            throw e;
        } finally {
            // 关闭连接
            empTransDao.closeConnection(conn);
        }
        return result;
    }

    /**
     * 检测模板名称是否重复
     *
     * @param tmName
     * @return
     * @throws Exception
     */
	/*public boolean isTemplateNameExists(String tmName, String corpCode)
			throws Exception
	{
		//结果
		boolean result = false;
		// 设置条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			// 设置条件信息
			conditionMap.put("tmName", tmName);
			conditionMap.put("corpCode", corpCode);
			// 数据库查询
			List<LfTemplate> tempList = empDao.findListByCondition(
					LfTemplate.class, conditionMap, null);
			// 判断是否重复
			if (tempList != null && tempList.size() > 0)
			{
				//返回
				result = true;
			}
		} catch (Exception e)
		{
			// 异常
			EmpExecutionContext.error(e);
			throw e;
		}
		//返回结果
		return result;
	}
*/

    /**
     * 删除彩信模板ID
     *
     * @param tmId
     * @return
     * @throws Exception
     */
    public Integer delTempByTmId(String[] tmId) throws Exception {
        Integer delNum = 0;
        Connection conn = empTransDao.getConnection();
        PreparedStatement ps = null;
        try {
            empTransDao.beginTransaction(conn);
            // 循环获取模板ID
            String ids = "";
            //循环处理id
            for (int index = 0; index < tmId.length; index++) {
                ids += tmId[index] + ",";
            }
            //id不为空
            if (ids != null && ids.length() != 0) {
                //截取最后一个逗号
                ids = ids.substring(0, ids.lastIndexOf(","));
            } else {
                //返回
                return null;
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 进行数据库删除
            //delNum = empDao.delete(LfTemplate.class, ids);
            delNum = empTransDao.delete(conn, LfTemplate.class, ids);
//			//删除关联的模板共享表
//			//短信模板type为1
            if (ids.length() > 0) {
                String delSql = "delete from " + TableLfTmplRela.TABLE_NAME + " where " + TableLfTmplRela.templId + " in(" + ids + ") and " + TableLfTmplRela.templType + "= 1";
                ps = conn.prepareStatement(delSql);
                ps.executeUpdate();
            }
            conditionMap.clear();
            LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
            conditionMap.put("mtId", ids);
            conditionMap.put("infoType", "3,4");
            objectMap.put("isComplete", "1");
            //empDao.update(LfFlowRecord.class, objectMap, conditionMap);
            empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            //异常处理
            delNum = 0;
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "删除模板失败！");
        } finally {
            if (ps != null) {
                ps.close();
            }
            empTransDao.closeConnection(conn);
        }
        //返回结果
        return delNum;
    }

    /**
     * 判断名称是否有效
     *
     * @param afTmName
     * @param beTmId
     * @return
     * @throws Exception
     */
    public Long validate(String afTmName, Long beTmId) throws Exception {
        Long validate;
        //id
        Long afTmId = this.getTmIdByTmName(afTmName);
        try {
            //id为空
            if (afTmId == null) {
                //设置1
                validate = new Long(1);
            } else if (afTmId != null && afTmId.equals(beTmId)) {
                //设置2
                validate = new Long(2);
            } else {
                //设置3
                validate = new Long(3);
            }
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "判断名称是否有效异常！");
            throw e;
        }
        //返回结果
        return validate;
    }

    /**
     * 通过模板名称获取模板ID
     *
     * @param tmName
     * @return
     * @throws Exception
     */
    private Long getTmIdByTmName(String tmName) throws Exception {
        Long tmId = null;
        // 设置条件的MAP
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            // 设置其条件
            conditionMap.put("tmName", tmName);
            // 进行查询
            List<LfTemplate> tmNames = empDao.findListByCondition(
                    LfTemplate.class, conditionMap, null);
            LfTemplate template = null;
            // 返回ID
            if (tmNames != null && tmNames.size() != 0) {
                //获取第一条记录
                template = tmNames.get(0);
                //获取id
                tmId = template.getTmid();
            }
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "通过模板名称获取模板IDbiz层异常！");
            throw e;
        }
        //返回结果
        return tmId;
    }

    /**
     * @return
     * @throws Exception
     */
	/*public String visualPrivs(Integer curOperId) throws Exception
	{
		//机构id
		String curOperOwnDepIds = "";
		//id
		String curOperOwnOperIds = "";
		//机构对象集合
		List<LfDep> deps = new ArrayList<LfDep>();
		//条件
		LinkedHashMap<String, String> domConditionMap = new LinkedHashMap<String, String>();
		try
		{
			//操作员id
			domConditionMap.put("userId", curOperId.toString());
			//按条件获取记录
			deps = empDao.findListByCondition(LfDep.class, domConditionMap,
					null);
			//有记录
			if (deps.size() != 1)
			{
				//循环处理记录
				for (int depindex = 0; depindex < deps.size(); depindex++)
				{
					if (depindex != 0)
					{

						curOperOwnDepIds += deps.get(depindex) + ",";
					}
				}
				//截取最后一个逗号
				curOperOwnDepIds = curOperOwnDepIds.substring(0,
						curOperOwnDepIds.lastIndexOf(","));
				//条件
				LinkedHashMap<String, String> sysconditionMap = new LinkedHashMap<String, String>();
				//条件
				LinkedHashMap<String, String> sysorderbyMap = new LinkedHashMap<String, String>();
				sysconditionMap.put("depId", curOperOwnDepIds);
				sysorderbyMap.put("userId", "asc");
				//获取操作员记录
				List<LfSysuser> sysusers = empDao.findListByCondition(
						LfSysuser.class, sysconditionMap, sysorderbyMap);
				//循环处理记录
				for (int sysuindex = 0; sysuindex < sysusers.size(); sysuindex++)
				{
					curOperOwnOperIds += sysusers.get(sysuindex) + ",";
				}
				curOperOwnOperIds += curOperId.toString();

			} else
			{

				curOperOwnOperIds = curOperId.toString();
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e);
			throw e;
		}
		//返回结果
		return curOperOwnOperIds;
	}
*/
//	/**
//	 *
//	 * @param dsflag
//	 * @param curOperId
//	 * @param orderbyMap
//	 * @return
//	 * @throws Exception
//	 */
	/*public List<LfTemplate> getTempByDsflag(Long dsflag, Integer curOperId,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		//短信模板对象集合
		List<LfTemplate> lfTemplates = new ArrayList<LfTemplate>();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//类型
			conditionMap.put("dsflag", dsflag.toString());
			//状态
			conditionMap.put("tmState", "1");
			if (curOperId != 1)
			{
				String userId = visualPrivs(curOperId);
				//有记录
				if (userId != null && userId.length() != 0)
					conditionMap.put("userId", userId);
			}
			//获取记录
			lfTemplates = empDao.findListByCondition(LfTemplate.class,
					conditionMap, orderbyMap);
		} catch (Exception e)
		{
			//异常处理
			lfTemplates = null;
			EmpExecutionContext.error(e);
			throw e;
		}
		//返回结果
		return lfTemplates;
	}*/

//	/**
//	 *
//	 * 通过用户名、获取彩信模板
//	 *
//	 * @param curOperId
//	 *            用户 ID
//	 *
//	 * @return
//	 * @throws Exception
//	 */
	/*public List<LfTemplate> getTemplateByUserId(Integer curOperId)
			throws Exception
	{
		//模板对象集合
		List<LfTemplate> lfTemplates = new ArrayList<LfTemplate>();
		// 设置条件MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			// 设置条件
			// 进行数据库查询
			String userId = visualPrivs(curOperId);
			//操作员id
			conditionMap.put("userId", userId);
			//获取记录
			lfTemplates = empDao.findListByCondition(LfTemplate.class,
					conditionMap, null);
		} catch (Exception e)
		{
			// 进入异常
			lfTemplates = null;
			EmpExecutionContext.error(e);
			throw e;
		}
		//返回结果
		return lfTemplates;
	}
*/
//	/**
//	 * 获取所有用户的模板
//	 *
//	 * @param dsflag
//	 * @param tmpType
//	 * @param loginUserId
//	 * @return
//	 * @throws Exception
//	 */
/*
	public List<LfTemplate> getAllUsingTemp(Integer dsflag, Integer tmpType,
			Long loginUserId) throws Exception
	{
		//模板对象集合
		List<LfTemplate> templates = new ArrayList<LfTemplate>();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//不为空
			if (dsflag != null)
			{
				conditionMap.put("dsflag", String.valueOf(dsflag));
			}
			//类型不为空
			if (tmpType != null)
			{
				conditionMap.put("tmpType", String.valueOf(tmpType));
			}
			//状态
			conditionMap.put("tmState", "1");
			// 查询模板
			templates = empDao.findListBySymbolsCondition(loginUserId,
					LfTemplate.class, conditionMap, null);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e);
			throw e;
		}
		//返回结果
		return templates;
	}
*/
    public String changeParam(String str) throws Exception {
        String eg = "#[pP]_[1-9][0-9]*#";
        Matcher m = Pattern.compile(eg,
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(str);
        while (m.find()) {
            String rstr = m.group();
            str = str.replace(rstr, rstr.toUpperCase());
        }
        return str;
    }

    public Integer getMsgParamNum(String msg) {
        try {
            int num = 0;
            int numparm = 0;
            while (num < 32) {
                num++;
                String mpam = "#P_" + num + "#";
                if (msg.contains(mpam)) {
                    numparm++;
                }
            }
            return numparm;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "添加模板计算模板参数个数失败msg=" + msg);
            return 0;
        }

    }

    /**
     * 获取素材树数据
     *
     * @param corpCode
     * @return
     */
    public String getMaterialJosnData2(String corpCode) {

        List<LfMaterialSort> lfMaterialSortList;
        StringBuffer tree = null;
        try {
            //设置条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode&in", corpCode + ",100000");
            //设置序列
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("sortId", "asc");
            //查询
            lfMaterialSortList = empDao.findListBySymbolsCondition(LfMaterialSort.class, conditionMap, orderbyMap);
            //构造数
            LfMaterialSort lfMaterialSort = null;
            List<Integer> sortIds = new ArrayList<Integer>();
            tree = new StringBuffer("[");
            for (int i = 0; i < lfMaterialSortList.size(); i++) {
                lfMaterialSort = lfMaterialSortList.get(i);
                sortIds.add(lfMaterialSort.getSortId());
                tree.append("{");
                //tree.append("id:").append(lfMaterialSort.getChildCode());
                tree.append("id:").append(lfMaterialSort.getSortId());
                tree.append(",name:'").append(lfMaterialSort.getSortName()).append("'");
                tree.append(",pId:").append(lfMaterialSort.getParentCode());
                tree.append(",isParent:").append(true);
                tree.append("}");
                if (i != lfMaterialSortList.size() - 1) {
                    tree.append(",");
                }
            }
            //获取素材
            String strSortIds = sortIds.toString();
            strSortIds = strSortIds.substring(1, strSortIds.length() - 1);
            conditionMap.clear();
            conditionMap.put("sortId&in", strSortIds);
            conditionMap.put("corpCode", corpCode);
            List<LfMaterial> lfMaterials = empDao.findListBySymbolsCondition(LfMaterial.class, conditionMap, null);
            LfMaterial lfMaterial = null;
            if (!lfMaterialSortList.isEmpty() && !lfMaterials.isEmpty()) {
                tree.append(",");
            }
            for (int i = 0; i < lfMaterials.size(); i++) {
                lfMaterial = lfMaterials.get(i);


                tree.append("{");
                tree.append("id:'u").append(lfMaterial.getMtalId()).append("'");
                tree.append(",address:'").append(lfMaterial.getMtalAddress()).append("'");
                tree.append(",type:'").append(lfMaterial.getMtalType().toUpperCase()).append("'");
                tree.append(",size:'").append(lfMaterial.getMtalSize()).append("'");
                tree.append(",width:").append(lfMaterial.getMtalWidth());
                tree.append(",height:").append(lfMaterial.getMtalHeight());
                tree.append(",name:'").append(lfMaterial.getMtalName()).append("'");
                tree.append(",pId:").append(lfMaterial.getSortId());
                tree.append(",isParent:").append(false);
                tree.append("}");


                if (i != lfMaterials.size() - 1) {
                    tree.append(",");
                }
            }
            tree.append("]");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "彩信获取素材树数据biz层异常！");
        }
        return tree.toString();
    }


    /**
     * 通过分类编码获取素材分类ID
     *
     * @param childCode
     * @return
     * @throws Exception
     */
    public Integer getSortIdByChildCode(String childCode) throws Exception {
        Integer sortId = null;
        List<LfMaterialSort> lfMaterialSortList = new ArrayList<LfMaterialSort>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            //设置条件
            conditionMap.put("sortId", childCode);
            //数据库查询
            lfMaterialSortList = empDao.findListByCondition(LfMaterialSort.class, conditionMap, null);

            if (null != lfMaterialSortList && 0 != lfMaterialSortList.size()) {
                for (LfMaterialSort lfMaterialSort : lfMaterialSortList) {
                    sortId = lfMaterialSort.getSortId();
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "通过分类编码获取素材分类ID异常。");
            throw e;
        }
        return sortId;
    }


    /**
     * 通过父编码获取子类的IDS
     *
     * @param parentCode
     * @return
     * @throws Exception
     */
    public String getSortIdsByParentCode(String parentCode) throws Exception {
        String sortIds = "";
        boolean doall = false;
        List<LfMaterialSort> lfMaterialSortList = new ArrayList<LfMaterialSort>();
        //设置条件的MAP
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            while (!doall) {
                conditionMap.put("parentCode&in", parentCode);
                parentCode = "";
                lfMaterialSortList = empDao.findListBySymbolsCondition(LfMaterialSort.class, conditionMap, null);
                conditionMap.clear();
                if (null != lfMaterialSortList && 0 != lfMaterialSortList.size()) {
                    for (LfMaterialSort lfMaterialSort : lfMaterialSortList) {
                        sortIds += lfMaterialSort.getSortId() + ",";
                        parentCode += lfMaterialSort.getChildCode() + ",";
                    }

                    if (null != parentCode && !"".equals(parentCode)) {
                        parentCode = parentCode.substring(0, parentCode.lastIndexOf(","));
                    }
                } else {
                    doall = true;
                }
                if (lfMaterialSortList != null) {
                    lfMaterialSortList.clear();
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "通过素材父编码获取子类的IDS异常。");
            throw e;
        }
        return sortIds;
    }


    /**
     * 获取彩信素材信息
     *
     * @param loginUserId  操作员ID
     * @param conditionMap 条件
     * @param pageInfo     分页信息
     * @return
     * @throws Exception
     */
    public List<LfMaterialVo> getMaterialInfos(Long loginUserId, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception {
        List<LfMaterialVo> lfMaterialVoList = new ArrayList<LfMaterialVo>();
        List<LfMaterial> lfMaterialList = null;
        Map<Long, String> userInfoMap = null;
        LfMaterialVo lfMaterialVo = null;

        try {
            //获取所有操作员信息
            LfSysuser curUser = empDao.findObjectByID(LfSysuser.class, loginUserId);
            userInfoMap = this.getUserNameByUserId(curUser.getCorpCode());
            //获取所有彩信模板信息
            lfMaterialList = this.getMaterialInfo(loginUserId, conditionMap, pageInfo);
            //循环获取VO
            for (LfMaterial lfMaterial : lfMaterialList) {
                lfMaterialVo = new LfMaterialVo();
                lfMaterialVo.setMtalId(lfMaterial.getMtalId());
                lfMaterialVo.setMtalName(lfMaterial.getMtalName());
                lfMaterialVo.setMtalSize(lfMaterial.getMtalSize());
                lfMaterialVo.setMtalType(lfMaterial.getMtalType());
                lfMaterialVo.setMtalUptime(lfMaterial.getMtalUptime());
                lfMaterialVo.setSortId(lfMaterial.getSortId());
                lfMaterialVo.setUserId(lfMaterial.getUserId());
                lfMaterialVo.setUserName(userInfoMap.get(lfMaterial.getUserId()));
                //lfMaterialVo.setUserState(user)
                lfMaterialVo.setComments(lfMaterial.getComments());
                lfMaterialVo.setMtalAddress(lfMaterial.getMtalAddress());
                lfMaterialVo.setMtalHeight(lfMaterial.getMtalHeight());
                lfMaterialVo.setMtalWidth(lfMaterial.getMtalWidth());
                lfMaterialVoList.add(lfMaterialVo);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取彩信素材信息异常。");
            throw e;
        }

        return lfMaterialVoList;

    }


    /**
     * 通过用户ID 获取其用户名称
     *
     * @return
     * @throws Exception
     */
    public Map<Long, String> getUserNameByUserId(String corpCode) throws Exception {
        Map<Long, String> userInfoMap = new LinkedHashMap<Long, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        List<LfSysuser> lfSysuserList = null;
        try {
            conditionMap.put("corpCode&in", corpCode + ",100000");
            //查询所有操作员
            lfSysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
            //放入MAP中
            for (LfSysuser lfSysuser : lfSysuserList) {
                userInfoMap.put(lfSysuser.getUserId(), lfSysuser.getUserName());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "通过用户ID 获取其用户名称异常");
            throw e;
        }
        return userInfoMap;
    }

    /**
     * 获取彩信模板信息
     *
     * @param loginUserId  操作员用户ID
     * @param conditionMap
     * @param pageInfo     分页信息
     * @return
     * @throws Exception
     */
    public List<LfMaterial> getMaterialInfo(Long loginUserId, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception {
        //彩信模板列表
        List<LfMaterial> lfMaterialList = null;
        //设置其排序
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        try {
            orderbyMap.put("mtalId", "asc");
            if (null == pageInfo) {
                //查询所有模板列表
                lfMaterialList = empDao.findListBySymbolsCondition(loginUserId, LfMaterial.class, conditionMap, orderbyMap);
                return lfMaterialList;
            }
            //去除彩信素材的权限设置
            lfMaterialList = empDao.findPageListBySymbolsConditionNoCount(null, LfMaterial.class, conditionMap, orderbyMap, pageInfo);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取彩信模板信息异常。");
            throw e;
        }
        return lfMaterialList;
    }

    public String updateShareTemp(String depidstr, String useridstr, String tempId, String InfoType, LfSysuser lfsysuser) {
        String returnmsg = "fail";
        Connection conn = empTransDao.getConnection();
        try {
            if (tempId == null || "".equals(tempId) || InfoType == null || "".equals(InfoType)) {
                return returnmsg;
            }
            empTransDao.beginTransaction(conn);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //先删除原有的共享对象
            conditionMap.put("templId", tempId);
            conditionMap.put("templType", InfoType);
            conditionMap.put("shareType", "1");
            conditionMap.put("corpCode", lfsysuser.getCorpCode());
            empTransDao.delete(conn, LfTmplRela.class, conditionMap);
            List<LfTmplRela> binObjList = new ArrayList<LfTmplRela>();
            LfTmplRela tempObj = null;

            //删除快捷场景
            delShortTemp(depidstr, useridstr, tempId, lfsysuser);

            //添加选中的共享机构
            if (depidstr != null && !"".equals(depidstr)) {
                String[] deparr = depidstr.split(",");
                for (int i = 0; i < deparr.length; i++) {
                    if (deparr[i] != null && !"".equals(deparr[i])) {
                        tempObj = new LfTmplRela();
                        tempObj.setTemplId(Long.valueOf(tempId));
                        tempObj.setToUserType(1);
                        tempObj.setShareType(1);
                        tempObj.setCreaterId(lfsysuser.getUserId());
                        tempObj.setToUser(Long.valueOf(deparr[i]));
                        tempObj.setTemplType(Integer.valueOf(InfoType));
                        tempObj.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        tempObj.setCorpCode(lfsysuser.getCorpCode());
                        binObjList.add(tempObj);
                    }
                }
            }
            //添加共享的操作员
            if (useridstr != null && !"".equals(useridstr)) {
                String[] userarr = useridstr.split(",");
                for (int i = 0; i < userarr.length; i++) {
                    if (userarr[i] != null && !"".equals(userarr[i])) {
                        tempObj = new LfTmplRela();
                        tempObj.setTemplId(Long.valueOf(tempId));
                        tempObj.setToUserType(2);
                        tempObj.setShareType(1);
                        tempObj.setCreaterId(lfsysuser.getUserId());
                        tempObj.setToUser(Long.valueOf(userarr[i]));
                        tempObj.setTemplType(Integer.valueOf(InfoType));
                        tempObj.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        tempObj.setCorpCode(lfsysuser.getCorpCode());
                        binObjList.add(tempObj);
                    }
                }
            }
            if (binObjList != null && binObjList.size() > 0) {
                empTransDao.save(conn, binObjList, LfTmplRela.class);
            }

            empTransDao.commitTransaction(conn);
            returnmsg = "success";
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            returnmsg = "fail";
            EmpExecutionContext.error(e, "更新共享对象出现异常！");
        } finally {
            empTransDao.closeConnection(conn);
        }
        return returnmsg;
    }

    public void delShortTemp(String depidstr, String useridstr, String tempId, LfSysuser loginUser) {

        List<Long> ids = new ArrayList<Long>();
        List<LfSysuser> lfSysusers = null;

        if (StringUtils.isNotBlank(depidstr)) {
            String[] depidstrs = depidstr.split(",");
            for (String depId : depidstrs) {
                LinkedHashMap<String, String> userConditionMap = new LinkedHashMap<String, String>();
                userConditionMap.put("depId", depId);
                try {
                    lfSysusers = empDao.findListByCondition(LfSysuser.class, userConditionMap, null);
                    for (LfSysuser lfSysuser : lfSysusers) {
                        {
                            ids.add(lfSysuser.getUserId());
                        }
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "用户信息查询异常");
                }
            }
        }
        if (StringUtils.isNotBlank(useridstr)) {
            String[] userids = useridstr.split(",");
            for (String userid : userids) {
                ids.add(Long.parseLong(userid));
            }
        }
        LinkedHashMap<String, String> shortTempConditionMap = new LinkedHashMap<String, String>();
        shortTempConditionMap.put("tempId", tempId);
        List<LfShortTemp> lfShortTemps = null;
        try {
            lfShortTemps = empDao.findListByCondition(LfShortTemp.class, shortTempConditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询快捷场景表异常");
        }
        if (null != lfShortTemps) {
            for (LfShortTemp lfShortTemp : lfShortTemps) {
                if (!lfShortTemp.getUserId().equals(loginUser.getUserId())) {//不包含此次操作人员
                    //如果分享用户列表中没有快捷场景表中的id，则删除改快捷场景
                    if (!ids.contains(lfShortTemp.getUserId())) {
                        LinkedHashMap<String, String> delConditionMap = new LinkedHashMap<String, String>();
                        delConditionMap.put("tempId", tempId);
                        delConditionMap.put("userId", String.valueOf(lfShortTemp.getUserId()));
                        try {
                            empDao.delete(LfShortTemp.class, delConditionMap);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "快捷场景删除失败!");
                        }
                    }
                }
            }
        }

    }


}
