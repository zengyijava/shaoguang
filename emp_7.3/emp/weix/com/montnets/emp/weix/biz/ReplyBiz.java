/**
 * 
 */
package com.montnets.emp.weix.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.weix.LfWcKeyword;
import com.montnets.emp.entity.weix.LfWcMenu;
import com.montnets.emp.entity.weix.LfWcRevent;
import com.montnets.emp.entity.weix.LfWcRimg;
import com.montnets.emp.entity.weix.LfWcRtext;
import com.montnets.emp.entity.weix.LfWcTemplate;
import com.montnets.emp.entity.weix.LfWcTlink;
import com.montnets.emp.weix.biz.i.IReplyBiz;
import com.montnets.emp.weix.common.util.DateFormatter;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.weix.common.util.TitleImgBean;
import com.montnets.emp.weix.dao.TempleDao;

/**
 * @author chensj
 */
public class ReplyBiz extends SuperBiz implements IReplyBiz
{
	// 文本
	private final static Integer	TEXT		= 0;

	// 单图文
	private final static Integer	SIGIMG		= 1;

	// 多图文
	private final static Integer	MORIMG		= 2;

	// 图文详细访问链接
	private final static String		imgdetail	= "cwc_imgdetail.hts?rimgid=";

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
		LfWcTemplate temp = new LfWcTemplate();
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
				LfWcKeyword lfkey = new LfWcKeyword();
				LfWcTlink lftink = new LfWcTlink();
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
				LfWcTemplate temp = baseBiz.getById(LfWcTemplate.class, accoutid);
				List<LfWcTlink> tlinks = baseBiz.getByCondition(LfWcTlink.class, conditionMap, null);
				// 执行删除
				empTransDao.beginTransaction(conn);
				int reint;
				if(!GlobalMethods.isNullList(tlinks))
				{
					reint = empTransDao.delete(conn, LfWcTlink.class, conditionMap);
					if(reint <= 0)
					{
						isSuc = false;
					}
					else
					{
						for (int i = 0; isSuc && i < tlinks.size(); i++)
						{

							reint = empTransDao.delete(conn, LfWcKeyword.class, tlinks.get(i).getKId().toString());
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
					LfWcKeyword lfkey = new LfWcKeyword();
					LfWcTlink lftink = new LfWcTlink();
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
		List<LfWcTemplate> tempList = null;
		List<LfWcTlink> linkList = null;
		BaseBiz baseBiz = new BaseBiz();
		try
		{
			if(!GlobalMethods.isInvalidString(tempIds))
			{
				tempMap.put("TId&in", tempIds);
				tempList = baseBiz.getByCondition(LfWcTemplate.class, tempMap, null);
				if(tempList == null || tempList.size() == 0)
				{
					return "fail";
				}
				// 关键字和模板关联关系List
				linkList = baseBiz.getByCondition(LfWcTlink.class, tempMap, null);
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
			empTransDao.delete(conn, LfWcTlink.class, tempMap);
			// 删除对应的图文
			for (LfWcTemplate temp : tempList)
			{
				if(!GlobalMethods.isInvalidString(temp.getRimgids()))
				{
					rimgMap.clear();
					rimgMap.put("rimgId", temp.getRimgids());
					rimgMap.put("corpCode", temp.getCorpCode());
					empTransDao.delete(conn, LfWcRimg.class, rimgMap);
				}
			}

			// 删除关键字
			if(linkList != null && linkList.size() > 0)
			{
				StringBuffer sb = new StringBuffer();
				for (int a = 0; a < linkList.size(); a++)
				{
					LfWcTlink wclink = linkList.get(a);
					sb.append(wclink.getKId());
					if(a != linkList.size() - 1)
					{
						sb.append(",");
					}
				}
				if(sb != null && sb.toString().length() > 0)
				{
					empTransDao.delete(conn, LfWcKeyword.class, sb.toString());
				}
			}
			tempMap.clear();

			// 删除模板
			tempMap.put("TId", tempIds);
			empTransDao.delete(conn, LfWcTemplate.class, tempMap);
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

	public boolean saveOrUpdateReply(List<LfWcRimg> vorimgs, String lgcorpcode, String weixPath,String weixFilePath, Long aid, String item_name, String words, String tid)
	{
		// 返回是否成功标识符
		boolean isSuc = true;
		boolean isAdd = true;
		Connection conn = empTransDao.getConnection();
		
		try
		{
			if(vorimgs != null && vorimgs.size() > 0 && aid != null)
			{
				Timestamp time = DateFormatter.createDate();
				LfWcRimg rimg;
				StringBuilder sb = new StringBuilder();
				List<LfWcRimg> lfrimgs = new ArrayList<LfWcRimg>();
				LfWcTemplate temp = null;
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("TId", tid);
				empTransDao.beginTransaction(conn);
				// tid存在则认为是修改操作 删除关联的表信息
				if(!GlobalMethods.isInvalidString(tid))
				{
					isAdd = false;
					temp = new BaseBiz().getById(LfWcTemplate.class, tid);
					temp.setModifytime(time);
					// 删除关联的图文表信息
					TempleDao.ideltesRimgs(tid, empTransDao, conn);
					List<LfWcTlink> tlinks = new BaseBiz().getByCondition(LfWcTlink.class, conditionMap, null);
					if(!GlobalMethods.isNullList(tlinks))
					{
						// 删除关键字关联表信息
						if(empTransDao.delete(conn, LfWcTlink.class, conditionMap) <= 0)
						{
							isSuc = false;
						}
						for (int i = 0; isSuc && i < tlinks.size(); i++)
						{
							// 删除关联的关键字
							if(empTransDao.delete(conn, LfWcKeyword.class, tlinks.get(i).getKId().toString()) <= 0)
							{
								isSuc = false;
							}
						}
					}
				}// 新增操作 初始化temp
				else
				{
					temp = new LfWcTemplate();
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
					rimg = new LfWcRimg();
					rimg.setTitle(vorimgs.get(i).getTitle());
					rimg.setPicurl(vorimgs.get(i).getPicurl());
					rimg.setDescription(vorimgs.get(i).getDescription());
					rimg.setLink(vorimgs.get(i).getLink());
					rimg.setCreatetime(time);
					rimg.setCorpCode(lgcorpcode);
					rimg.setAId(0L);

					// 保存图文中的每一条图文
					Long rid = empTransDao.saveObjProReturnID(conn, rimg);
					
					fileNames[i] = rimg.getPicurl();
					rimg = new LfWcRimg();
					if(rid == null)
					{
						isSuc = false;
						break;
					}
					rimg.setTitle(GlobalMethods.handleSpecialTag(vorimgs.get(i).getTitle()));
					rimg.setPicurl(weixFilePath + vorimgs.get(i).getPicurl());
					rimg.setDescription(cutRimgs(vorimgs.get(i).getDescription()));
					if(GlobalMethods.isInvalidString(vorimgs.get(i).getLink()))
					{
						rimg.setLink(weixPath + imgdetail + rid);
					}
					else
					{
						rimg.setLink(vorimgs.get(i).getLink());
					}
					rimg.setCreatetime(time);
					rimg.setAId(0L);
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
					// 图文的时候，MsgText置空
					temp.setMsgText("");
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
					LfWcKeyword lfkey = new LfWcKeyword();
					LfWcTlink lftink = new LfWcTlink();
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
		List<LfWcRevent> reventList;
		// 默认回复
		List<LfWcRtext> rtextList;
		// 自定义菜单
		List<LfWcMenu> menuList;
		try
		{
			reventList = baseBiz.getByCondition(LfWcRevent.class, conditionMap, null);
			if(reventList != null && reventList.size() > 0)
			{
				return "true";
			}
			rtextList = baseBiz.getByCondition(LfWcRtext.class, conditionMap, null);
			if(rtextList != null && rtextList.size() > 0)
			{
				return "true";
			}
			menuList = baseBiz.getByCondition(LfWcMenu.class, conditionMap, null);
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
		List<LfWcRevent> reventList;
		List<LfWcRtext> rtextList;
		List<LfWcMenu> menuList;
		String[] ids = tempid.split(",");
		String remsg = "k";
		String delid = "k";
		for (String s : ids)
		{
			conditionMap.put("TId", s);
			try
			{
				// 关注时事件
				reventList = baseBiz.getByCondition(LfWcRevent.class, conditionMap, null);
				if(reventList != null && reventList.size() > 0)
				{
					remsg = remsg + s + ",";
					continue;
				}
				// 默认回复
				rtextList = baseBiz.getByCondition(LfWcRtext.class, conditionMap, null);
				if(rtextList != null && rtextList.size() > 0)
				{
					remsg = remsg + s + ",";
					continue;
				}
				// 自定义菜单
				menuList = baseBiz.getByCondition(LfWcMenu.class, conditionMap, null);
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
	public List<TitleImgBean> getImgByList(List<LfWcRimg> echorimgs)
	{
		List<TitleImgBean> imgBeans = new ArrayList<TitleImgBean>();
		if(!GlobalMethods.isNullList(echorimgs))
		{
			try
			{
				TitleImgBean b;
				for (int i = 0; i < echorimgs.size(); i++)
				{
					b = new TitleImgBean();
					String link = "";
					b.setTitle(echorimgs.get(i).getTitle());
					b.setUrl(echorimgs.get(i).getPicurl());
					link = echorimgs.get(i).getLink();
					if(link == null || "".equals(link))
					{
						link = "cwc_imgdetail.hts?rimgid=" + echorimgs.get(i).getRimgId().toString();
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
}
