package com.montnets.emp.qyll.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.qyll.biz.DataQueryBIZ;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.biz.OperatorReportBiz;
import com.montnets.emp.qyll.utils.ExcelExportUtil;
import com.montnets.emp.qyll.utils.ExcelSimpleUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.qyll.vo.OrderDetailVO;
import com.montnets.emp.qyll.vo.ProductVO;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.PageInfo;

/**
 * @author Jason Huang
 * @date 2017年10月31日 上午8:51:16
 */

public class OrderDetailSVT extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String PATH = "qyll/sjcx";
	private final DataQueryBIZ dataQueryBIZ = new DataQueryBIZ();

	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,
			EMPException {
		boolean isFirstEnter = false;
		PageInfo pageInfo = new PageInfo();

		// 设置分页
		if (!(request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null)) {
			pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
			pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
		} else {
			isFirstEnter = true;
		}

		try {
			// 不管是否第一次进入页面,总要查询出套餐列表
			LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
			LlCompInfoVo compInfo = llCompInfoBiz.getLlCompInfoBean();
			if (compInfo == null) {
				return;
			}
			List<ProductVO> productList = dataQueryBIZ.getProductList(compInfo.getCorpCode());
			request.setAttribute("productList", productList);
			if (!isFirstEnter) {
				OrderDetailVO orderDetailVO = getParameter(request);
				if (orderDetailVO.getState() == null) {
					orderDetailVO.setState("detail");
				}
				List<OrderDetailVO> orderDetailVOList = dataQueryBIZ.getOrderDetailList(orderDetailVO, pageInfo);// 获取list数据
				request.setAttribute("resultList", orderDetailVOList);// 查询结果集
				request.setAttribute("pageInfo", pageInfo);// 分页对象
			} else {
				request.setAttribute("isFirstEnter", isFirstEnter);
			}
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "Servlet-根据条件查询套餐订购详情列表异常。");
		} finally {
			request.getRequestDispatcher(PATH + "/ll_orderDetails.jsp").forward(request, response);
		}
	}

	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String str1 = MessageUtils.extractMessage("qyll", "qyll_common_93", request);// 手机号码
		String str2 = MessageUtils.extractMessage("qyll", "qyll_common_150", request);// 套餐名称
		String str3 = MessageUtils.extractMessage("qyll", "qyll_common_43", request);// 运营商
		String str4 = MessageUtils.extractMessage("qyll", "qyll_common_144", request);// 状态报告
		String str5 = MessageUtils.extractMessage("qyll", "qyll_common_99", request);// 订购编号
		String str6 = MessageUtils.extractMessage("qyll", "qyll_common_185", request);// 移动
		String str7 = MessageUtils.extractMessage("qyll", "qyll_common_187", request);// 电信
		String str8 = MessageUtils.extractMessage("qyll", "qyll_common_186", request);// 联通
		String str9 = MessageUtils.extractMessage("qyll", "qyll_common_205", request);// 提交成功
		String str10 = MessageUtils.extractMessage("qyll", "qyll_common_206", request);// 提交失败
		String str11 = MessageUtils.extractMessage("qyll", "qyll_common_96", request);// 订购成功
		String str12 = MessageUtils.extractMessage("qyll", "qyll_common_97", request);// 订购失败
		String str13 = MessageUtils.extractMessage("qyll", "qyll_common_103", request);// 操作员
		String str14 = MessageUtils.extractMessage("qyll", "qyll_common_100", request);// 隶属机构
		String str15 = MessageUtils.extractMessage("qyll", "qyll_common_19", request);// 订购时间
		String str16 = MessageUtils.extractMessage("qyll", "qyll_common_106", request);// 状态返回时间
		
		String[] title = { str1, str2, str3, str4, str15, str16, str5, str14, str13 };
		List<String[]> list = new ArrayList<String[]>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String datatime = sdf.format(new Date());// 获得当前时间,用于Excel取名记录
		String filePath = request.getSession().getServletContext().getRealPath("/OrderDetailsExcel")
				.replaceAll("%20", " ");
		String excelPath = request.getSession().getServletContext()
				.getRealPath("/OrderDetailsExcel" + "/" + "OrderDetailsExcel-" + datatime + ".xlsx")
				.replaceAll("%20", " ");
		File file = new File(filePath);// 文件夹不存在时创建
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			OrderDetailVO orderDetailVO = getParameter(request);
			if (orderDetailVO.getState() == null) {
				orderDetailVO.setState("detail");
			}
			List<OrderDetailVO> orderDetailVOList = dataQueryBIZ.getOrderDetailList(orderDetailVO, null);
			for (OrderDetailVO detail : orderDetailVOList) {
				String isp = detail.getIsp();
				if (isp != null) {
					if (isp.equals("1")) {
                        isp = str6;
                    } else if (isp.equals("2")) {
                        isp = str7;
                    } else {
                        isp = str8;
                    }
				}
				String state = detail.getState();
				if (state != null) {
					if (state.equals("0")) {
                        state = str9;
                    } else if (state.equals("4")) {
                        state = str10;
                    } else if (state.equals("1")) {
                        state = str11;
                    } else if (state.equals("2")) {
                        state = str12;
                    }
				}

				String[] strArr = { detail.getMobile(), detail.getTheme(), isp, state,
						detail.getOrdertm().toString().substring(0, 19), detail.getRpttm().toString().substring(0, 19),
						detail.getOrderno(), detail.getOrganization(), detail.getOperator() };
				list.add(strArr);
			}

			ExcelSimpleUtil.export(title, list, excelPath);
			// 将Excel压缩
			ExcelExportUtil excelExportUtil = new ExcelExportUtil();
			List<File> srcFile = new ArrayList<File>();
			File zipFile = new File(filePath + ".zip");
			srcFile.add(new File(excelPath));
			excelExportUtil.zipFiles(srcFile, zipFile);
			response.getWriter().print("true");
		} catch (Exception e) {
			response.getWriter().print("false");
			EmpExecutionContext.error(e, "套餐订购详情导出到Excel并压缩失败！");
		}
	}

	public void downFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			String str = "OrderDetailsExcel.zip";
			String serverPath = request.getSession().getServletContext().getRealPath("/"+str).replaceAll("%20", " ");
			String path = serverPath;
			EmpExecutionContext.info("套餐订购详情Excel路径："+path);
			File file = new File(path);
			if (file.exists()) {
//				InputStream ins = new FileInputStream(path);
//				BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
//				OutputStream outs = response.getOutputStream();// 获取文件输出IO流
//				BufferedOutputStream bouts = new BufferedOutputStream(outs);
				InputStream ins = null;
				BufferedInputStream bins = null;
				OutputStream outs = null;
				BufferedOutputStream bouts = null;
				try {
					ins = new FileInputStream(path);
					bins = new BufferedInputStream(ins);// 放到缓冲流里面
					outs = response.getOutputStream();// 获取文件输出IO流
					bouts = new BufferedOutputStream(outs);

					response.setContentType("application/download");// 设置response内容的类型
					response.setHeader(
							"Content-disposition",
							"attachment;filename="
									+ URLEncoder.encode(str, "GBK"));// 设置头部信息
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					// 开始向网络传输文件流
					while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
						bouts.write(buffer, 0, bytesRead);
					}
				} finally {
					if(null != bouts){
						bouts.flush();// 这里一定要调用flush()方法
					}
					try{
						IOUtils.closeIOs(ins, bins, outs, bouts, getClass());
					}catch(Exception e){
						EmpExecutionContext.error(e,"error:");
					}				}			
//				bouts.flush();// 这里一定要调用flush()方法
//				ins.close();
//				bins.close();
//				outs.close();
//				bouts.close();

				boolean flag =  file.delete();// 删除Zip文件
                if (!flag) {
                    EmpExecutionContext.error("刪除文件失敗！");
                }
				ExcelExportUtil excelExportUtil = new ExcelExportUtil();// 删除Excel文件夹
				excelExportUtil.deleteTmpExcel(serverPath.substring(0,serverPath.lastIndexOf("/")+1) + "OrderDetailsExcel");
				file = new File(serverPath.substring(0,serverPath.lastIndexOf("/")+1) + "OrderDetailsExcel");
                flag =  file.delete();// 删除Zip文件
                if (!flag) {
                    EmpExecutionContext.error("刪除文件失敗！");
                }
			} else {
				EmpExecutionContext.error("套餐订购详情Excel所在文件夹不存在:"+path);
			}
		} catch (IOException e) {
			EmpExecutionContext.error(e, "套餐订购详情下载zip包失败");
		}
	}

	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			// 父级机构ID
			Long superiorDepId = null;
			// 操作员id
			Long userid = null;
			// 部门ID
			String depStr = request.getParameter("depId");
			// 操作员ID
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);

			if (depStr != null && !"".equals(depStr.trim())) {
				superiorDepId = Long.parseLong(depStr);
			}
			if (userStr != null && !"".equals(userStr.trim())) {
				userid = Long.parseLong(userStr);
			}
			// 通过机构id和操作员ID获得机构树字符串
			String departmentTree = getDepUserJosnDataNew(superiorDepId, userid);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取机构树异常");
		}
	}

	private String getDepUserJosnDataNew(Long superiorDepId, Long userid) {
		StringBuffer tree = new StringBuffer();
		LfSysuser user = null;
		BaseBiz baseBiz = new BaseBiz();
		try {
			user = baseBiz.getById(LfSysuser.class, userid);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "获取操作员失败");
		}
		if (user!=null&&user.getPermissionType() == 1) {
			tree = new StringBuffer("[]");
		} else {
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;

			try {
				lfDeps = null;

				if (superiorDepId == null) {
					lfDeps = new ArrayList<LfDep>();
					LfDep lfDep = depBiz.getAllDeps(userid).get(0);
					lfDeps.add(lfDep);
				} else {
					lfDeps = new DepBiz().getDepsByDepSuperId(superiorDepId);
				}
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取操作员失败");
			}
		}
		return tree.toString();
	}

	public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String depId = request.getParameter("depId");
		try {
			String replaceStr = MessageUtils.extractMessage("qyll","qyll_common_214",request);//已注销
			// 获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			OperatorReportBiz operatorReportBiz = new OperatorReportBiz();
			String deptUserTree = operatorReportBiz.getDepUserJosn(depId, curUser,replaceStr);
			if (deptUserTree == null) {
				response.getWriter().print("");
			} else {
				response.getWriter().print(deptUserTree);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询统计，生成机构操作员树字符串，异常。depId=" + depId);
			response.getWriter().print("");
		}
	}

	private LfSysuser getCurUserInSession(HttpServletRequest request) {
		Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
		if (loginSysuserObj == null) {
			return null;
		}
		return (LfSysuser) loginSysuserObj;
	}

	private OrderDetailVO getParameter(HttpServletRequest request) throws Exception {
		LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
		OrderDetailVO orderDetailVO = new OrderDetailVO();
		String mobile = request.getParameter("mobile");
		String theme = request.getParameter("theme");
		String isp = request.getParameter("isp");
		String state = request.getParameter("state");
		String organization = request.getParameter("organization");
		String operator = request.getParameter("operator");
		
		String replaceStr = "("+MessageUtils.extractMessage("qyll","qyll_common_214",request)+")";//已注销
		operator = operator.isEmpty()?null:operator.replace(replaceStr, "");
		
		String orderno = request.getParameter("orderno");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String str1 = MessageUtils.extractMessage("qyll", "qyll_common_101", request);// 请选择
		orderDetailVO.setEcid(llCompInfoBiz.getLlCompInfoBean().getCorpCode());
		orderDetailVO.setMobile((mobile != null && mobile.trim().length() > 0) ? mobile : null);
		orderDetailVO.setTheme((theme != null && theme.trim().length() > 0) ? theme : null);
		orderDetailVO.setIsp((isp != null && isp.trim().length() > 0) ? isp : null);
		orderDetailVO.setState((state != null && state.trim().length() > 0) ? state : null);
		orderDetailVO.setOrganization((organization != null && organization.trim().length() > 0 && !organization.trim().equals(str1)) ? organization : null);
		orderDetailVO.setOperator((operator != null && operator.trim().length() > 0 && !operator.trim().equals(str1)) ? operator : null);
		orderDetailVO.setOrderno((orderno != null && orderno.trim().length() > 0) ? orderno : null);
		orderDetailVO.setStartTime((startTime != null && startTime.trim().length() > 0) ? startTime : null);
		orderDetailVO.setEndTime((endTime != null && endTime.trim().length() > 0) ? endTime : null);
		return orderDetailVO;
	}

	private int getIntParameter(String param, int defaultValue, HttpServletRequest request) {
		try {
			if (request.getParameter(param) != null && !"".equals(request.getParameter(param))) {
				return Integer.parseInt(request.getParameter(param));
			} else {
				return defaultValue;
			}
		} catch (NumberFormatException e) {
			EmpExecutionContext.error(e, "Servlet-获取分页信息异常");
			return defaultValue;
		}
	}
}
