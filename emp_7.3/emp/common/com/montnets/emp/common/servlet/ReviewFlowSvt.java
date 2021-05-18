package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.ReviewFlowVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;


/**
 * 审核流程查看以及催办的servlet。短信发送，彩信发送，短信模板，彩信模板，网讯发送，网讯模板都会调用此servlet
 * @project emp_std_201508
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-12-2 上午10:41:27
 * @description
 */
public class ReviewFlowSvt extends BaseServlet
{
	final BaseBiz baseBiz = new BaseBiz();
	/**
	 * 获取审核流程中所有审批的操作员(包括已审批的流程和未审批的操作员)
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getReviewFlow(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//未走到的审批级别
		int templateLevel=-1;
		//MAP集合，KEY审核级别，Value是当前审核级别的所有审批人员
		Map<Integer,List<ReviewFlowVo>> reviewFlowVoMap=new HashMap<Integer,List<ReviewFlowVo>>();
		//ID，短信发送，彩信发送为mt_id。短信模板、彩信模板为模板ID。网讯模板为网讯模板ID。
		String mtId = request.getParameter("mtId");
		//操作员用户ID
		String userId = request.getParameter("userId");
		//审核类型 1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；5：网讯发送；6:网讯模板
		String reviewType=request.getParameter("reviewType");
		//流程发起人ID
		String createUserID="";
		//审核流程记录表中的审核ID
		String reviewID="";
		try
		{
			//获取操作员对象
			LfSysuser user = baseBiz.getById(LfSysuser.class, userId);
			//根据审核类型，初始化流程发起人ID和审核流程记录表中的审核ID
			//1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；5：网讯发送；6:网讯模板
			if("1".equals(reviewType)||"2".equals(reviewType)||"5".equals(reviewType)){
				LfMttask mtTask = baseBiz.getById(LfMttask.class, mtId);
				createUserID=String.valueOf(mtTask.getUserId());
				//短信发送和彩信发送，是taskid，不是mt_id
				reviewID=String.valueOf(mtTask.getTaskId());
			}else if("3".equals(reviewType)||"4".equals(reviewType)){
				LfTemplate template = baseBiz.getById(LfTemplate.class, mtId);
				createUserID=String.valueOf(template.getUserId());
				reviewID=mtId;
			}else if("6".equals(reviewType)){
				createUserID=userId;
				reviewID=mtId;
			}
			JSONObject jsonObject = new JSONObject();
			//1.查询已审核的记录
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			//流程发起人ID
			conditionMap.put("ProUserCode",createUserID);
			//审批类型
			conditionMap.put("infoType", reviewType);
			//审核流程记录表中的审核ID
			conditionMap.put("mtId",reviewID);
			//审核状态为同意和不同意 ，已审批只有这两种状态
			conditionMap.put("RState&in","1,2");
			//审批为已经完成，已经审批肯定是已经完成
			conditionMap.put("isComplete","1");
			//按照审核级别升序排序
			orderByMap.put("RLevel", StaticValue.ASC);
			orderByMap.put("preRv", StaticValue.DESC);
			//查询已审核的记录
			List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
			
			
			//2.查询审核中的记录
			conditionMap.clear();
			//流程发起人ID
			conditionMap.put("ProUserCode",createUserID);
			//审批类型
			conditionMap.put("infoType", reviewType);
			//审核流程记录表中的审核ID
			conditionMap.put("mtId",reviewID);
			//审核状态为未审批 ，未审批只有这种状态
			conditionMap.put("RState","-1");
			//审批为未完成
			conditionMap.put("isComplete","2");
			//查询审核中的记录
			List<LfFlowRecord> unflowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
			//审核中的一条记录
			LfFlowRecord lastrecord = null;
			String isshow = "2";
			//审核中的记录不为空
			if(unflowRecords != null && unflowRecords.size()>0){
				isshow = "1";
//				StringBuffer useridstr = new StringBuffer();
//				for(LfFlowRecord temp:unflowRecords){
//					useridstr.append(temp.getUserCode()).append(",");
//				}
				//获取待审批的流程
				if(lastrecord == null){
					lastrecord = unflowRecords.get(0);
				}
//				List<LfSysuser> sysuserList = null;
//				if(useridstr != null && !"".equals(useridstr.toString())){
//					String str = useridstr.toString().substring(0, useridstr.toString().length()-1);
//					conditionMap.clear();
//					conditionMap.put("userId&in", str);
//					conditionMap.put("corpCode", user.getCorpCode());
//					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
//					if(sysuserList != null && sysuserList.size()>0){
//						for(LfSysuser sysuser:sysuserList){
//							if(depId == null){
//								depId = sysuser.getDepId();
//							}
//						}
//					}
//				}
				
				//3.查询审核流程模板中的审核人员
				if(lastrecord != null){
					//审核类型  1操作员  4机构  5逐级审核
					Integer rtype = lastrecord.getRType();
					//审核流程业务层
					ReviewBiz reviewBiz = new ReviewBiz();
					//当前待审批的是逐级审批类型
					if(rtype == 5){
						//判断当前待审批是否是逐级审批的最后一级
						boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lastrecord.getPreRv().intValue(), lastrecord.getProUserCode());
						//当前待审批是逐级审批的最后一级，则查找第二级及以后的审核级别的操作员
						if(isLastLevel){
							//当前待审批是逐级审批的最后一级，则查找第二级及以后的审核级别的操作员
							templateLevel=lastrecord.getRLevel()+1;
							//审核总级别不为1，才查找。因为1肯定是逐级审核。
							if(lastrecord.getRLevelAmount() != 1){
								List<ReviewFlowVo> reviewFlowVos=null;
								//循环查找，从第二级别循环到审核流程最后一级
								for (int level = 1; level <lastrecord.getRLevelAmount() ; level++)
								{
									//调用查询当前审核级别下一级别的所有审批人员方法，查找
									reviewFlowVos=reviewBiz.getNextFlowRecord(level, lastrecord.getRLevelAmount(), String.valueOf(lastrecord.getFId()),user.getCorpCode(), lastrecord.getProUserCode());
									//如果查找不到，Value填null值。查找不到说明该级别没有审核人员。审批界面提示该级别没有审核人员。
									if(reviewFlowVos==null||reviewFlowVos.size()<1){
										reviewFlowVoMap.put(level+1, null);
									}else{
										//将审核人员信息放到map中
										reviewFlowVoMap.put(level+1, reviewFlowVos);
									}
								}
							}
						}else{
							templateLevel=lastrecord.getRLevel();
							//当前待审批的是逐级审批类型，但是不是逐级审批的最后一级。还需要把逐级审批的后续审核人员查找出来，再查找后续级别的审批人员。
							//查找逐级审核当前待审批的后续审核人员
							List<ReviewFlowVo> reviewFlowVos=reviewBiz.getZJAllFlowRecord(lastrecord.getPreRv().intValue(),Long.parseLong(userId), user.getCorpCode(), lastrecord.getRLevelAmount());
							//如果查找不到，Value填空list。这个和填空值不同。审批界面不提示。审核流程可以往下走。
							if(reviewFlowVos==null||reviewFlowVos.size()<1){
								reviewFlowVoMap.put(1, new ArrayList<ReviewFlowVo>());
							}else{
								//将审核人员信息放到map中
								reviewFlowVoMap.put(1, reviewFlowVos);
							}
							//循环查找，从第二级别循环到审核流程最后一级
							if(lastrecord.getRLevelAmount() != 1){
								reviewFlowVos=null;
								for (int level = 1; level <lastrecord.getRLevelAmount() ; level++)
								{
									//调用查询当前审核级别下一级别的所有审批人员方法，查找
									reviewFlowVos=reviewBiz.getNextFlowRecord(level, lastrecord.getRLevelAmount(), String.valueOf(lastrecord.getFId()),user.getCorpCode(), lastrecord.getProUserCode());
									//如果查找不到，Value填null值。查找不到说明该级别没有审核人员。审批界面提示该级别没有审核人员。
									if(reviewFlowVos==null||reviewFlowVos.size()<1){
										reviewFlowVoMap.put(level+1, null);
									}else{
										//将审核人员信息放到map中
										reviewFlowVoMap.put(level+1, reviewFlowVos);
									}
								}
							}
						}
					//当前待审批的不是逐级审批类型
					}else{
						templateLevel=lastrecord.getRLevel()+1;
						//当前待审批的不是逐级审批类型,并且不是最后一级审批
						if(lastrecord.getRLevel() != null && !lastrecord.getRLevel().equals(lastrecord.getRLevelAmount()))
						{
							List<ReviewFlowVo> reviewFlowVos=null;
							//循环查找，从当前审核级别的下一级别查找到审核流程最后一级
							for (int level = lastrecord.getRLevel(); level <lastrecord.getRLevelAmount() ; level++)
							{
								//调用查询当前审核级别下一级别的所有审批人员方法，查找
								reviewFlowVos=reviewBiz.getNextFlowRecord(level, lastrecord.getRLevelAmount(), String.valueOf(lastrecord.getFId()),user.getCorpCode(), lastrecord.getProUserCode());
								//如果查找不到，Value填null值。查找不到说明该级别没有审核人员。审批界面提示该级别没有审核人员。
								if(reviewFlowVos==null||reviewFlowVos.size()<1){
									reviewFlowVoMap.put(level+1, null);
								}else{
									//将审核人员信息放到map中
									reviewFlowVoMap.put(level+1, reviewFlowVos);
								}
							}
						}
					}
				}
			}
			//如果已审核为null，则初始化。
			if(flowRecords==null){
				flowRecords=new ArrayList<LfFlowRecord>();
			}
			//将待审批的集合添加到已审批的集合中去。
			if(unflowRecords!=null&&unflowRecords.size()>0){
				flowRecords.addAll(unflowRecords);
			}
			//循环待审批和已审批的集合
			if(flowRecords != null && flowRecords.size()>0){
				LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
				conditionMap.clear();
				String auditName = "";
				//循环将操作员ID组装成字符串
				for(int j=0;j<flowRecords.size();j++){
					LfFlowRecord temp = flowRecords.get(j);
					auditName = auditName + temp.getUserCode() + ",";
				}
				//根据操作员ID查找操作员对象
				List<LfSysuser> sysuserList = null;
				if(auditName != null && !"".equals(auditName)){
					auditName = auditName.substring(0, auditName.length()-1);
					conditionMap.put("userId&in", auditName);
					conditionMap.put("corpCode", user.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					if(sysuserList != null && sysuserList.size()>0){
						for(LfSysuser sysuser:sysuserList){
							//将操作员ID和操作员姓名生成Map集合
							nameMap.put(sysuser.getUserId(), sysuser.getName());
						}
					}
				}
				
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//新建一个json数组
				JSONArray members = new JSONArray(); 
				//是否有审批信息1有  2 没有
				jsonObject.put("haveRecord","1");
				JSONObject member = null; 
				LfFlowRecord record = null;
				//循环待审批和已审批的集合
				for(int i=0;i<flowRecords.size();i++){
					//新建一个json对象
					member = new JSONObject(); 
					record = flowRecords.get(i);
					//设置是否存在审核人 1是存在，0是不存在
					member.put("existreviewer", "1");
					//审核级别和审核总级别
					member.put("Rlevel", record.getRLevel().toString() + "/" + record.getRLevelAmount().toString());
					//审批人，根据审批人ID到MAP中查找审批人姓名
					if(nameMap != null && nameMap.size()>0 && nameMap.containsKey(record.getUserCode())){
						member.put("Reviname",nameMap.get(record.getUserCode()));
					}else{
						member.put("Reviname","-");
					}
					//审批时间
					if(record.getRTime()== null){
						member.put("rtime", "-");
					}else{
						member.put("rtime", df.format(record.getRTime()));
					}
					//催办时间
					if(record.getIsremind()==1){
						member.put("remindtime", df.format(record.getRemindTime()));
					}else{
						member.put("remindtime","-");
					}
					//是否允许催办 1允许催办，只有待审批才允许催办
					if(record.getRState()==-1){
						member.put("allowremind","1");
					}else{
						member.put("allowremind","-");
					}
					//审核流程ID，催办会用到
					member.put("flowid", record.getFrId());
					//审批结果
					int state = record.getRState();
					switch(state)
					{
						case -1:
							/*未审批*/
							member.put("exstate", MessageUtils.extractMessage("common","common_reviewFlow_text_2",request));
							member.put("exresult","-");
							break;
						case 1:
							/*已审批*/
							member.put("exstate", MessageUtils.extractMessage("common","common_reviewFlow_text_4",request));
							/*审批通过*/
							member.put("exresult",MessageUtils.extractMessage("common","common_reviewFlow_text_6",request));
							break;
						case 2:
							/*已审批*/
							member.put("exstate", MessageUtils.extractMessage("common","common_reviewFlow_text_4",request));
							/*审批不通过*/
							member.put("exresult",MessageUtils.extractMessage("common","common_reviewFlow_text_5",request));
							break;
						default:
							/*无效的标示*/
							member.put("exstate", "["+MessageUtils.extractMessage("common","common_reviewFlow_text_3",request)+"]");
							/*无效的标示*/
							member.put("exresult","["+MessageUtils.extractMessage("common","common_reviewFlow_text_3",request)+"]");
					} 
					//审批意见
					if("".equals(record.getComments()) || record.getComments() == null){
						member.put("Comments","");
					}else{
						member.put("Comments",record.getComments());
					}
					//将审批的json对象加到json集合
					members.add(member);
				}
				//int level=record.getRLevel()+1;
				
				ReviewFlowVo reviewFlowVo=null;
				//reviewFlowVoMap必须有对象
				if(templateLevel!=-1&&reviewFlowVoMap!=null&&reviewFlowVoMap.size()>0){
					//循环从map中查找审批模板
					for (int i = templateLevel; i < (record.getRLevelAmount()+1); i++)
					{
						//查找审批模板
						List<ReviewFlowVo> reviewFlowVos=reviewFlowVoMap.get(i);
						//reviewFlowVo为null，则此审核级别未找到审批人员。在界面给出提示。
						if(reviewFlowVos==null){
							member = new JSONObject(); 
							//设置是否存在审核人 1是存在，0是不存在
							member.put("existreviewer", "0");
							//审核级别和审核总级别
							member.put("Rlevel", i + "/" + record.getRLevelAmount().toString());
							//审批人,审批人员不存在没有姓名
							member.put("Reviname","-");
							//审批时间
							member.put("rtime", "-");
							//审批状态
							/*此审核级别未找到审批人员，请联系管理员处理。*/
							member.put("exstate", MessageUtils.extractMessage("common","common_reviewFlow_text_1",request));
							//审批结果
							member.put("exresult","-");
							//审批意见
							member.put("Comments","");
							//审批流程ID，用于催办
							member.put("flowid", "");
							members.add(member);
							
						}else if(reviewFlowVos.size()==0){
							//逐级审批，未找到审批人员。继续循环。不需要在界面提示未找到审批人员，请联系管理员
							continue;
						}else{
							for (int j = 0; j < reviewFlowVos.size(); j++)
							{
								reviewFlowVo=reviewFlowVos.get(j);
								member = new JSONObject();
								//设置是否存在审核人 1是存在，0是不存在
								member.put("existreviewer", "1");
								//审核级别和审核总级别
								member.put("Rlevel", i + "/" + record.getRLevelAmount().toString());
								//审批人姓名，从VO中获取。
								member.put("Reviname",reviewFlowVo.getReviewerName());
								//审批时间
								member.put("rtime", "-");
								//审批状态 已审批  未审批
								member.put("exstate", MessageUtils.extractMessage("common","common_reviewFlow_text_2",request));
								//审批结果 审批通过  审批不通过
								member.put("exresult","-");
								//审批意见
								member.put("Comments","-");
								//催办时间
								member.put("remindtime","-");
								//是否允许催办 1允许催办，只有待审批才允许催办
								member.put("allowremind","-");
								//审批流程ID，用于催办
								member.put("flowid", "");
								members.add(member);
							}
						}
					}
				}
				jsonObject.put("members",members);
			}else{
				//没有审批记录
				jsonObject.put("haveRecord","2");
			}
			
			//审核级别和审核条件
			jsonObject.put("isshow",isshow);
			//1级
			jsonObject.put("onelevel","");
			jsonObject.put("onecondition","");
			//2级
			jsonObject.put("twolevel","");
			jsonObject.put("twocondition","");
			//3级
			jsonObject.put("threelevel","");
			jsonObject.put("threecondition","");
			//4级
			jsonObject.put("fourlevel","");
			jsonObject.put("fourcondition","");
			//5级
			jsonObject.put("fivelevel","");
			jsonObject.put("fivecondition","");
			//查找审核流程模板
			conditionMap.clear();
			orderByMap.clear();
			conditionMap.put("FId", String.valueOf(lastrecord.getFId()));
			//按照级别升序排序
			orderByMap.put("RLevel", StaticValue.ASC);
			List<LfReviewer2level> lfReviewer2levelList=baseBiz.findListByCondition(LfReviewer2level.class, conditionMap, orderByMap);
			LfReviewer2level lfReviewer2level=null;
			if(lfReviewer2levelList!=null&&lfReviewer2levelList.size()>0){
				for (int i = 0; i <lfReviewer2levelList.size(); i++)
				{
					lfReviewer2level=lfReviewer2levelList.get(i);
					if(lfReviewer2level.getRLevel()==1){
						//1级
						jsonObject.put("onelevel",lfReviewer2level.getRLevel());
						//1全部通过生效;2第一人审核生效
						jsonObject.put("onecondition",lfReviewer2level.getRCondition());
					}else if(lfReviewer2level.getRLevel()==2){
						//2级
						jsonObject.put("twolevel",lfReviewer2level.getRLevel());
						jsonObject.put("twocondition",lfReviewer2level.getRCondition());
					}else if(lfReviewer2level.getRLevel()==3){
						//3级
						jsonObject.put("threelevel",lfReviewer2level.getRLevel());
						jsonObject.put("threecondition",lfReviewer2level.getRCondition());
					}else if(lfReviewer2level.getRLevel()==4){
						//4级
						jsonObject.put("fourlevel",lfReviewer2level.getRLevel());
						jsonObject.put("fourcondition",lfReviewer2level.getRCondition());
					}else if(lfReviewer2level.getRLevel()==5){
						//5级
						jsonObject.put("fivelevel",lfReviewer2level.getRLevel());
						jsonObject.put("fivecondition",lfReviewer2level.getRCondition());
					}
				}
			}
			response.getWriter().print(jsonObject.toString());
		}catch (Exception e){
			response.getWriter().print("fail");
			EmpExecutionContext.error(e,"群发任务 获取信息发送查询里的详细异常！");
		}
	}

	/**
	 * 催办审批
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void cuibanFlow(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String result="sendFail";
		try
		{
			//流程ID
			String frid=request.getParameter("frid");
			//获取审核流程
			LfFlowRecord flowRecord=new BaseBiz().getById(LfFlowRecord.class, frid);
			if(flowRecord!=null){
				//调用催办的方法
				ReviewRemindBiz rrBiz=new ReviewRemindBiz();
				result=rrBiz.cuibanSms(flowRecord);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"催办短信发送异常！");
		}finally{
			response.getWriter().print(result);
		}
		
	}
}
