/**
 * 
 */
package com.montnets.emp.wxgl.biz;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiKeyword;
import com.montnets.emp.entity.wxgl.LfWeiMenu;
import com.montnets.emp.entity.wxgl.LfWeiRevent;
import com.montnets.emp.entity.wxgl.LfWeiRimg;
import com.montnets.emp.entity.wxgl.LfWeiRtext;
import com.montnets.emp.entity.wxgl.LfWeiTemplate;
import com.montnets.emp.entity.wxgl.LfWeiTlink;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.wxgl.base.util.DateFormatter;
import com.montnets.emp.wxgl.base.util.TitleImgBean;
/**
 * @author chensj
 */
public class ReplyBiz extends SuperBiz
{
	// 文本
	private final static Integer	TEXT		= 0;

	// 单图文
	private final static Integer	SIGIMG		= 1;

	// 多图文
	private final static Integer	MORIMG		= 2;

	// 图文详细访问链接
	private final static String		imgdetail	= "weix_imgDetail.hts?rimgid=";

	/**
	 * 保存文本回复
	 * 
	 * @param words
	 * @param accuntid
	 * @param replyname
	 * @param replycont
	 * @param lgcorpcode
	 * @return1
	 */
	public String saveTextReply(String words, String accuntid, String replyname, String replycont, String lgcorpcode)
	{
		String remsg = "success";
		LfWeiTemplate temp = new LfWeiTemplate();
		Long aid = 0L;
		if(!GlobalMethods.isInvalidString(accuntid))
		{
			aid = Long.parseLong(accuntid);
		}
		String reply_cont = GlobalMethods.handleSpecialTag(replycont);
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			// 保存回复模板
			temp.setCorpCode(lgcorpcode);
			temp.setCreatetime(DateFormatter.createDate());
			temp.setAId(aid);
			temp.setMsgText(replycont);
			temp.setMsgType(TEXT);
			temp.setMsgXml(WeixBiz.createInitRtextMessage(reply_cont));
			temp.setName(replyname);
			temp.setKeywordsvo(words.replaceAll(":\\d", ""));
			Long tid = empTransDao.saveObjProReturnID(conn, temp);
			// temp保存成功
			if(tid != null)
			{
				String[] keys = words.split(",");
				LfWeiKeyword lfkey = new LfWeiKeyword();
				LfWeiTlink lftink = new LfWeiTlink();
				Long keyid;
				boolean flag = false;
				for (String s : keys)
				{
					// 保存关键字
					// "[名称:匹配类型,...]"
					lfkey.setName(s.split(":")[0]);
					lfkey.setType(Integer.parseInt(s.split(":")[1]));
					lfkey.setCorpCode(lgcorpcode);
					// 公众账号
					lfkey.setAId(aid);
					lfkey.setCreatetime(DateFormatter.createDate());
					keyid = empTransDao.saveObjProReturnID(conn, lfkey);
					// 关键字保存失败 跳出循环
					if(keyid == null)
					{
						remsg = "fail";
						break;
					}

					// 保存关键字和回复模板的关联关系
					lftink.setKId(keyid);
					lftink.setTId(tid);
					flag = empTransDao.save(conn, lftink);
					// 关联关系保存失败 跳出循环
					if(!flag)
					{
						remsg = "fail";
						break;
					}

				}
			}// temp保存失败
			else
			{
				remsg = "fail";
			}
			// 未发生失败的情况则成功保存 提交事务
			if("success".equals(remsg))
			{
				empTransDao.commitTransaction(conn);
			}
			else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			remsg = "fail";
			EmpExecutionContext.error(e, "保存文本回复失败！");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return remsg;
	}

	/**
	 * 编辑文本回复
	 * 
	 * @param words
	 * @param accoutid
	 * @param replyname
	 * @param replycont
	 * @param lgcorpcode
	 * @param accuntnum
	 * @return1
	 */
	public boolean editTextReply(String words, String accoutid, String replyname, String replycont, String lgcorpcode, String accuntnum)
	{
		boolean isSuc = true;
		Connection conn = empTransDao.getConnection();
		BaseBiz baseBiz = new BaseBiz();
		String reply_cont = GlobalMethods.handleSpecialTag(replycont);
		// 先删除关键字 在关联表 模板
		try
		{
			// 参数合法
			if(!GlobalMethods.isInvalidString(accoutid) && accuntnum != null && !GlobalMethods.isInvalidString(words))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("TId", accoutid);
				if("".equals(accuntnum))
				{
					accuntnum = "0";
				}
				Long aid = GlobalMethods.strToLong(accuntnum);
				LfWeiTemplate temp = baseBiz.getById(LfWeiTemplate.class, accoutid);
				List<LfWeiTlink> tlinks = baseBiz.getByCondition(LfWeiTlink.class, conditionMap, null);
				// 执行删除
				empTransDao.beginTransaction(conn);
				int reint;
				if(GlobalMethods.isNotNullList(tlinks))
				{
					reint = empTransDao.delete(conn, LfWeiTlink.class, conditionMap);
					if(reint <= 0)
					{
						isSuc = false;
					}
					else
					{
						for (int i = 0; isSuc && i < tlinks.size(); i++)
						{

							reint = empTransDao.delete(conn, LfWeiKeyword.class, tlinks.get(i).getKId().toString());
							if(reint <= 0)
							{
								isSuc = false;
							}
						}
					}
				}
				else
				{
					isSuc = false;
				}
				// 删除成功的前提下 执行修改
				if(isSuc)
				{
					LfWeiKeyword lfkey = new LfWeiKeyword();
					LfWeiTlink lftink = new LfWeiTlink();
					Long keyid;
					Long accid = GlobalMethods.strToLong(accoutid);
					String[] keys = words.split(",");
					for (String s : keys)
					{
						// 保存关键字
						lfkey.setName(s.split(":")[0]);
						lfkey.setType(Integer.parseInt(s.split(":")[1]));
						lfkey.setCorpCode(lgcorpcode);
						lfkey.setAId(aid);
						lfkey.setCreatetime(DateFormatter.createDate());
						keyid = empTransDao.saveObjProReturnID(conn, lfkey);
						if(keyid == null)
						{
							isSuc = false;
							break;
						}
						// 保存关键字和回复模板关联
						lftink.setKId(keyid);
						lftink.setTId(accid);
						if(!empTransDao.save(conn, lftink))
						{
							isSuc = false;
							break;
						}

					}
				}
				// 删除和修改都成功的前提下 更新template表
				if(isSuc)
				{
					temp.setKeywordsvo(words.replaceAll(":\\d", ""));
					temp.setMsgText(replycont);
					temp.setName(replyname);
					temp.setAId(aid);
					temp.setMsgXml(WeixBiz.createInitRtextMessage(reply_cont));
					if(!empTransDao.update(conn, temp))
					{
						isSuc = false;
					}
				}

			}
			else
			{
				isSuc = false;
			}
			// 未发生失败的情况则成功保存 提交事务
			if(isSuc)
			{
				empTransDao.commitTransaction(conn);
			}
			else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			isSuc = false;
			EmpExecutionContext.error(e, "编辑文本回复失败！");

		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return isSuc;
	}

	public String delTemplebiz(String tempIds)
	{
		String remsg = "";
		// 模板查询条件
		LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		// 图文查询条件
		LinkedHashMap<String, String> rimgMap = new LinkedHashMap<String, String>();
		List<LfWeiTemplate> tempList = null;
		List<LfWeiTlink> linkList = null;
		BaseBiz baseBiz = new BaseBiz();
		try
		{
			if(!GlobalMethods.isInvalidString(tempIds))
			{
				tempMap.put("TId&in", tempIds);
				tempList = baseBiz.getByCondition(LfWeiTemplate.class, tempMap, null);
				if(tempList == null || tempList.size() == 0)
				{
					return "fail";
				}
				// 关键字和模板关联关系List
				linkList = baseBiz.getByCondition(LfWeiTlink.class, tempMap, null);
			}
			else
			{
				return "fail";
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询回复模板失败");
			return "fail";
		}
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			tempMap.clear();
			tempMap.put("TId", tempIds);
			empTransDao.delete(conn, LfWeiTlink.class, tempMap);
			// 删除对应的图文
			for (LfWeiTemplate temp : tempList)
			{
				if(!GlobalMethods.isInvalidString(temp.getRimgids()))
				{
					rimgMap.clear();
					rimgMap.put("rimgId", temp.getRimgids());
					rimgMap.put("corpCode", temp.getCorpCode());
					empTransDao.delete(conn, LfWeiRimg.class, rimgMap);
				}
			}

			// 删除关键字
			if(linkList != null && linkList.size() > 0)
			{
				StringBuffer sb = new StringBuffer();
				for (int a = 0; a < linkList.size(); a++)
				{
					LfWeiTlink wclink = linkList.get(a);
					sb.append(wclink.getKId());
					if(a != linkList.size() - 1)
					{
						sb.append(",");
					}
				}
				if(sb != null && sb.toString().length() > 0)
				{
					empTransDao.delete(conn, LfWeiKeyword.class, sb.toString());
				}
			}
			tempMap.clear();

			// 删除模板
			tempMap.put("TId", tempIds);
			empTransDao.delete(conn, LfWeiTemplate.class, tempMap);
			empTransDao.commitTransaction(conn);
			remsg = "sccuess";
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除 回复记录失败 ！");
			remsg = "fail";
		}
		finally
		{
			if(conn != null)
			{
				empTransDao.closeConnection(conn);
			}
		}
		return remsg;
	}

	public boolean saveOrUpdateReply(List<LfWeiRimg> vorimgs, String lgcorpcode, String weixPath,String weixFilePath, Long aid, String item_name, String words, String tid)
	{
		// 返回是否成功标识符
		boolean isSuc = true;
		boolean isAdd = true;
		Connection conn = empTransDao.getConnection();
		// 单图文时存放摘要
		String summary = "";
		
		try
		{
			if(vorimgs != null && vorimgs.size() > 0 && aid != null)
			{
			    // 长度为1时则为单图文
			    if(vorimgs.size() == 1 )
			    {
			        summary = vorimgs.get(0).getSummary();
			    }
			    
				Timestamp time = DateFormatter.createDate();
				LfWeiRimg rimg;
				StringBuilder sb = new StringBuilder();
				List<LfWeiRimg> lfrimgs = new ArrayList<LfWeiRimg>();
				LfWeiTemplate temp = null;
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("TId", tid);
				
				empTransDao.beginTransaction(conn);
				// tid存在则认为是修改操作 删除关联的表信息
				if(!GlobalMethods.isInvalidString(tid))
				{
					isAdd = false;
					temp = new BaseBiz().getById(LfWeiTemplate.class, tid);
					temp.setModifytime(time);
					// 删除关联的图文表信息
					//TempleDao.ideltesRimgs(tid, ottTransDao, conn);
					List<LfWeiTlink> tlinks = new BaseBiz().getByCondition(LfWeiTlink.class, conditionMap, null);
					if(GlobalMethods.isNotNullList(tlinks))
					{
						// 删除关键字关联表信息
						if(empTransDao.delete(conn, LfWeiTlink.class, conditionMap) <= 0)
						{
							isSuc = false;
						}
						for (int i = 0; isSuc && i < tlinks.size(); i++)
						{
							// 删除关联的关键字
							if(empTransDao.delete(conn, LfWeiKeyword.class, tlinks.get(i).getKId().toString()) <= 0)
							{
								isSuc = false;
							}
						}
					}
				}// 新增操作 初始化temp
				else
				{
					temp = new LfWeiTemplate();
					temp.setCreatetime(time);
					temp.setCorpCode(lgcorpcode);
					if(vorimgs.size() > 1)
					{
						// 多图文
						temp.setMsgType(MORIMG);
					}
					else
					{
						// 单图文
						temp.setMsgType(SIGIMG);
					}
				}
				// 需要上传文件服务器的文件数组
				String[] fileNames = new String[vorimgs.size()];
				// 当前面操作正常则保存图文信息
				for (int i = 0; isSuc && i < vorimgs.size(); i++)
				{
					rimg = vorimgs.get(i);
					rimg.setCreatetime(time);
					rimg.setCorpCode(lgcorpcode);
					
					// 保存图文中的每一条图文
					Long rid = empTransDao.saveObjProReturnID(conn, rimg);
					
					fileNames[i] = rimg.getPicurl();
					rimg = new LfWeiRimg();
					if(rid == null)
					{
						isSuc = false;
						break;
					}
					rimg.setTitle(GlobalMethods.handleSpecialTag(vorimgs.get(i).getTitle()));
					rimg.setPicurl(weixFilePath + vorimgs.get(i).getPicurl());
					//rimg.setDescription(cutRimgs(vorimgs.get(i).getDescription()));
					rimg.setDescription(vorimgs.get(i).getSummary());
					rimg.setLink(vorimgs.get(i).getLink());
					rimg.setCreatetime(time);
					rimg.setRimgId(rid);
					rimg.setSourceUrl(vorimgs.get(i).getSourceUrl());
					// 用来生成msgxml
					lfrimgs.add(rimg);
					
					sb.append(rid + ",");
				}
				
				// 保存template信息
				if(isSuc)
				{
					temp.setAId(aid);
					temp.setName(item_name);
					// 回复模板对应的关键字
					temp.setKeywordsvo(words.replaceAll(":\\d", ""));
					// 图文的时候，填入摘要
					temp.setMsgText(summary==null?"":summary);
					// 回复模板关联的图文Ids
					temp.setRimgids(sb.substring(0, sb.length() - 1));
					// 创建回复模板对应的响应初始的msgXml格式数据
					String msgXml = WeixBiz.createInitRimgMessage(lfrimgs);
					temp.setMsgXml(msgXml);
					// 更新或新增图文模板表
					if(!GlobalMethods.isInvalidString(tid))
					{
						if(!empTransDao.update(conn, temp))
						{
							isSuc = false;
						}
					}
					else
					{
						Long id = empTransDao.saveObjProReturnID(conn, temp);
						if(id == null)
						{
							isSuc = false;
						}
						else
						{
							tid = String.valueOf(id);
						}
					}
				}

				// 上述操作成功的情况下 保存关键字信息
				if(isSuc)
				{
					String[] keys = words.split(",");
					LfWeiKeyword lfkey = new LfWeiKeyword();
					LfWeiTlink lftink = new LfWeiTlink();
					Long keyid;
					for (String s : keys)
					{
						// 保存关键字
						lfkey.setName(s.split(":")[0]);
						lfkey.setType(Integer.parseInt(s.split(":")[1]));
						lfkey.setCorpCode(lgcorpcode);
						lfkey.setAId(aid);
						lfkey.setCreatetime(time);
						keyid = empTransDao.saveObjProReturnID(conn, lfkey);
						if(keyid == null)
						{
							isSuc = false;
							break;
						}
						else
						{
							// 保存关键字与模板的关联
							lftink.setKId(keyid);
							lftink.setTId(Long.parseLong(tid));
							if(!empTransDao.save(conn, lftink))
							{
								isSuc = false;
								break;
							}
						}
					}
				}
			}// 存在不合法的参数
			else
			{
				isSuc = false;
			}

			if(isSuc)
			{
				empTransDao.commitTransaction(conn);
			}
			else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}
		catch (Exception e)
		{
			isSuc = false;
			EmpExecutionContext.error(e, isAdd ? "新增" : "修改" + "图文信息失败！");
			empTransDao.rollBackTransaction(conn);
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return isSuc;
	}

	/**
	 * 截取图文正文50个字符
	 * 2013-8-29
	 * 
	 * @return
	 *         String
	 */
	private static String cutRimgs(String des)
	{
		if(!GlobalMethods.isInvalidString(des))
		{
			if(des.length() > 50)
			{
				des = des.substring(0, 50);
			}
		}
		return GlobalMethods.handleSpecialTag(des);
	}

	/**
	 * 处理字串串中的“\"”
	 * 
	 * @param content
	 * @return1
	 */
	private static String handDouquto(String content)
	{
		if(content == null)
			return "";
		if(content.indexOf("\"") != -1)
		{
			content = content.trim().replace("\"", "\\\"");
		}

		return content;
	}

	/**
	 * 判读模板是否被默认回复，关注时回复，自定义菜单关联
	 * 2013-9-5
	 * 
	 * @param tempid
	 * @return
	 *         String
	 */
	public String checkRelated(String tempid)
	{
		BaseBiz baseBiz = new BaseBiz();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("TId", tempid);
		// 关注时回复
		List<LfWeiRevent> reventList;
		// 默认回复
		List<LfWeiRtext> rtextList;
		// 自定义菜单
		List<LfWeiMenu> menuList;
		try
		{
			reventList = baseBiz.getByCondition(LfWeiRevent.class, conditionMap, null);
			if(reventList != null && reventList.size() > 0)
			{
				return "true";
			}
			rtextList = baseBiz.getByCondition(LfWeiRtext.class, conditionMap, null);
			if(rtextList != null && rtextList.size() > 0)
			{
				return "true";
			}
			menuList = baseBiz.getByCondition(LfWeiMenu.class, conditionMap, null);
			if(menuList != null && menuList.size() > 0)
			{
				return "true";
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "判读模板是否关联异常！");
		}
		return "false";
	}

	/**
	 * 找出关联和没有关联的回复模板id
	 * 2013-9-5
	 * 
	 * @param tempid
	 * @return
	 *         String
	 */
	public String getRelatedTemples(String tempid)
	{
		BaseBiz baseBiz = new BaseBiz();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<LfWeiRevent> reventList;
		List<LfWeiRtext> rtextList;
		List<LfWeiMenu> menuList;
		String[] ids = tempid.split(",");
		String remsg = "k";
		String delid = "k";
		for (String s : ids)
		{
			conditionMap.put("TId", s);
			try
			{
				// 关注时事件
				reventList = baseBiz.getByCondition(LfWeiRevent.class, conditionMap, null);
				if(reventList != null && reventList.size() > 0)
				{
					remsg = remsg + s + ",";
					continue;
				}
				// 默认回复
				rtextList = baseBiz.getByCondition(LfWeiRtext.class, conditionMap, null);
				if(rtextList != null && rtextList.size() > 0)
				{
					remsg = remsg + s + ",";
					continue;
				}
				// 自定义菜单
				menuList = baseBiz.getByCondition(LfWeiMenu.class, conditionMap, null);
				if(menuList != null && menuList.size() > 0)
				{
					remsg = remsg + s + ",";
					continue;
				}
				// 没有被关联模板
				delid = delid + s + ",";
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "模板关联ID获取异常！");
			}
		}
		if(!"k".equals(remsg))
		{
			remsg = remsg.substring(0, remsg.length() - 1);
		}
		if(!"k".equals(delid))
		{
			delid = delid.substring(0, delid.length() - 1);
		}
		return remsg + "-" + delid;
	}

	/**
	 * 获取图文对象列表
	 * 
	 * @param echorimgs
	 * @param requestXml
	 * @param base_Path
	 * @return
	 */
	public List<TitleImgBean> getImgByList(List<LfWeiRimg> echorimgs)
	{
		List<TitleImgBean> imgBeans = new ArrayList<TitleImgBean>();
		if(GlobalMethods.isNotNullList(echorimgs))
		{
			try
			{
				TitleImgBean b;
				for (int i = 0; i < echorimgs.size(); i++)
				{
					b = new TitleImgBean();
					String link = "";
					b.setTitle(echorimgs.get(i).getTitle());
					b.setUrl(downloadFile(echorimgs.get(i).getPicurl()));
					
					link = echorimgs.get(i).getLink();
					if("1".equals(echorimgs.get(i).getSourceUrl())||GlobalMethods.isInvalidString(link))
					{
						link = "weix_imgDetail.hts?rimgid=" + echorimgs.get(i).getRimgId().toString();
						b.setLink(link);
					}
					else
					{
						b.setLink(echorimgs.get(i).getLink());
					}

					imgBeans.add(b);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取图文对象列表失败-[XmlUtil-getImgByList-L90]。");
			}
		}
		return imgBeans;
	}
	
	public String downloadFile(String url){
		if(url!=null&&!"".equals(url.trim())){
			String servletPath=new TxtFileUtil().getWebRoot();
			File f = new File(servletPath+url);
			//文件本地不存在 且为集群时 则 从文件服务器上下载文件
			if(!f.exists()&& StaticValue.getISCLUSTER() ==1){
					CommonBiz comBiz = new CommonBiz();
					String downMsg = comBiz.downloadFileFromFileCenter(url);
					if("error".equals(downMsg)){
					EmpExecutionContext.error("从文件服务器下载文件"+url+"失败！");
					}
			}
			if(!f.exists()){
				url="";
			}
		}else{
			url="";
		}
		return url;
	}
}
