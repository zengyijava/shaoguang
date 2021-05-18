package com.montnets.emp.rms.wbs.biz.impl;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.wbs.biz.ISvcContBiz;
import com.montnets.emp.rms.wbs.dao.impl.LfSpDepBindDAOImpl;
import com.montnets.emp.rms.wbs.dao.impl.SvcContDAOImpl;
import com.montnets.emp.rms.wbs.model.SvcCont;

import java.util.List;
import java.util.Map;

/**
 * 业务层实现类，调用数据层完成
 * 
 * @ClassName SvcContBizImpl
 * @Description TODO
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月11日
 */
public class SvcContBizImpl implements ISvcContBiz {

	/**
	 * 业务层实现类，调用数据层执行批量更新<br>
	 * 如果成功，返回true;如果失败，返回false
	 */
	public boolean insertBatch(List<SvcCont> svcConts) throws Exception {
		boolean flag = false;
		try {
			// 调用数层批量插入操作，保存处理结果信息
			flag = new SvcContDAOImpl().doInsertBatch(svcConts);
		} catch (Exception e) {
			// 将错误信息写入日志
			EmpExecutionContext.error(e, "富信同步数据接口调用，批量入库临时表LF_RMS_SYN_TEMP出现异常");
		}
		// 返回保存的结果
		return flag;
	}

	/**
	 * 执行单条数据的插入的业务层处理<br>
	 * 如果成功则返回true,否则返回false
	 */
	public boolean insertSimple(SvcCont svcCont) throws Exception {
		boolean flag = false;
		try {
			// 调用数据层单条数据插入操作
			flag = new SvcContDAOImpl().doInsert(svcCont);
		} catch (Exception e) {
			// 将错误信息写入日志
			EmpExecutionContext.error(e, "入库临时表LF_RMS_SYN_TEMP出现异常");
		}
		// 返回操作标记
		return flag;
	}

	/**
	 * 调用数据层操作将临时表中的数据进行转移处理
	 */

	public void transferDate() throws Exception{
		// 调用数据层的数据转移操作
		new SvcContDAOImpl().doTransferData();
	}

	/**
	 * 获取sp账号和企业编码的关系<br>
	 * 返回Map集合，保存一下内容<br>
	 * <li>key=sp账号,value=企业编码
	 */
	public Map<String, String> findSpAndCorpCode() throws Exception {
		// 声明Map对象
		Map<String, String> map = null;
		try {
			// 调用数据层查找sp账号和企业编码关系操作
			map = new LfSpDepBindDAOImpl().findSpAndCorpCode();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询sp账号和企业编码出现异常");
		}
		return map;
	}
}
