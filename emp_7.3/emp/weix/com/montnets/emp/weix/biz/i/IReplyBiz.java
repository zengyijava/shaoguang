package com.montnets.emp.weix.biz.i;


import java.util.List;
import com.montnets.emp.entity.weix.LfWcRimg;
import com.montnets.emp.weix.common.util.TitleImgBean;


/**
 * @author chensj
 */
public interface IReplyBiz 
{

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
	public String saveTextReply(String words, String accuntid, String replyname, String replycont, String lgcorpcode);

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
	public boolean editTextReply(String words, String accoutid, String replyname, String replycont, String lgcorpcode, String accuntnum);

	public String delTemplebiz(String tempIds);

	public boolean saveOrUpdateReply(List<LfWcRimg> vorimgs, String lgcorpcode, String weixPath,String weixFilePath, Long aid, String item_name, String words, String tid);


	/**
	 * 判读模板是否被默认回复，关注时回复，自定义菜单关联
	 * 2013-9-5
	 * 
	 * @param tempid
	 * @return
	 *         String
	 */
	public String checkRelated(String tempid);

	/**
	 * 找出关联和没有关联的回复模板id
	 * 2013-9-5
	 * 
	 * @param tempid
	 * @return
	 *         String
	 */
	public String getRelatedTemples(String tempid);

	/**
	 * 获取图文对象列表
	 * 
	 * @param echorimgs
	 * @param requestXml
	 * @param base_Path
	 * @return
	 */
	public List<TitleImgBean> getImgByList(List<LfWcRimg> echorimgs);
}
