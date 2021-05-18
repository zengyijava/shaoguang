package com.montnets.emp.qyll.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.qyll.biz.DataQueryBIZ;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.utils.ExcelExportUtil;
import com.montnets.emp.qyll.utils.ExcelSimpleUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.qyll.vo.OrderDetailVO;
import com.montnets.emp.qyll.vo.OrderTaskVO;
import com.montnets.emp.qyll.vo.ProductVO;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.PageInfo;

/**
 * @author Jason Huang
 * @date 2017年10月31日 下午2:40:55
 */

public class OrderHistorySVT extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String PATH = "qyll/sjcx";
	final DataQueryBIZ dataQueryBIZ = new DataQueryBIZ();

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
				// 获取查询条件参数
				OrderTaskVO orderTaskVO = getParameter(request);
				List<OrderTaskVO> orderTaskVOList = dataQueryBIZ.getOrderTaskList(orderTaskVO, pageInfo);// 获取list数据
				request.setAttribute("pageInfo", pageInfo);// 分页对象
				request.setAttribute("resultList", orderTaskVOList);// 查询结果
			} else {
				request.setAttribute("isFirstEnter", isFirstEnter);
			}
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "Servlet-根据条件查询套餐订购任务列表异常。");
		} finally {
			request.getRequestDispatcher(PATH + "/ll_orderHistories.jsp").forward(request, response);
		}
	}

	public void updateReSendStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, EMPException {
		String id = request.getParameter("id");
		if (id != null && !id.trim().equals("")) {
			if (dataQueryBIZ.updateReSendById(Integer.parseInt(id))) {
			} else {
				EmpExecutionContext.error("Servlet-Detail数据更新失败");
			}
		}
		find(request, response);
	}

	public void findProductsById(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, EMPException {
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
			List<ProductVO> productVOList = dataQueryBIZ.getProductList(request.getParameter("ids"), null);
			
			if (!isFirstEnter) {
				OrderDetailVO detailVO = new OrderDetailVO();
				LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
				String mobile = request.getParameter("mobile");
				String isp = request.getParameter("isp");
				String state = request.getParameter("state");
				detailVO.setMobile((mobile != null && mobile.trim().length() > 0) ? mobile : null);
				detailVO.setIsp((isp != null && isp.trim().length() > 0) ? isp : null);
				detailVO.setState((state != null && state.trim().length() > 0) ? state : null);
				detailVO.setOrderno(request.getParameter("orderNo"));
				detailVO.setEcid(llCompInfoBiz.getLlCompInfoBean().getCorpCode());
				List<OrderDetailVO> orderDetailVOList = dataQueryBIZ.getOrderDetailList(detailVO, pageInfo);
				request.setAttribute("resultList", orderDetailVOList);
				request.setAttribute("pageInfo", pageInfo);// 分页对象
			} else {
				request.setAttribute("isFirstEnter", isFirstEnter);
			}

			if (isFirstEnter) {// 第一次以ID去查询数据库中的主题,主要为了解决不同容器下乱码问题
				OrderTaskVO orderTask = new OrderTaskVO();
				orderTask.setOrderNo(request.getParameter("orderNo"));
				orderTask.setOrdered(true);
				String topic = dataQueryBIZ.getOrderTaskList(orderTask, null).get(0).getTopic();
				request.setAttribute("topic", topic);
			} else {// 第一次之后以POST方式把中文参数带过来,不需要查询数据库
				request.setAttribute("topic", request.getParameter("topic"));
			}
			request.setAttribute("productList", productVOList);
			request.setAttribute("ids", request.getParameter("ids"));
			request.setAttribute("orderNo", request.getParameter("orderNo"));
			request.setAttribute("orderTM", request.getParameter("orderTM").substring(0, 19));
			request.setAttribute("subCount", request.getParameter("subCount"));
			request.setAttribute("sucCount", request.getParameter("sucCount"));
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			EmpExecutionContext.error(e, "根据ID查询套餐产品信息列表异常。");
		} finally {
			request.getRequestDispatcher(PATH + "/ll_orderDetail.jsp").forward(request, response);
		}
	}

	public void findDetailByIds(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, EMPException {
		String str6 = MessageUtils.extractMessage("qyll", "qyll_common_185", request);// 移动
		String str7 = MessageUtils.extractMessage("qyll", "qyll_common_187", request);// 电信
		String str8 = MessageUtils.extractMessage("qyll", "qyll_common_186", request);// 联通
		PrintWriter pw = null;
		try {
			List<ProductVO> productVOList = dataQueryBIZ.getProductList(null, request.getParameter("orderNo"));
			Map<String, ProductVO> map = new HashMap<String, ProductVO>();
			for (int i = 0; i < productVOList.size(); i++) {
				if (productVOList.get(i).getIsp().equals("1")) {
					productVOList.get(i).setIsp(str6);
				}
				if (productVOList.get(i).getIsp().equals("2")) {
					productVOList.get(i).setIsp(str7);
				}
				if (productVOList.get(i).getIsp().equals("3")) {
					productVOList.get(i).setIsp(str8);
				}
				map.put(Integer.toString(i), productVOList.get(i));
			}
			Gson gson = new Gson();
			String json = gson.toJson(map.values());
			pw = response.getWriter();
			pw.print(json);
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			EmpExecutionContext.error(e, "根据ID查询套餐产品信息列表异常。");
		} finally {
			if (null != pw) {
				pw.flush();
				pw.close();
			}
		}
	}

	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {		
		String str1 = MessageUtils.extractMessage("qyll", "qyll_common_103", request);// 操作员
		String str2 = MessageUtils.extractMessage("qyll", "qyll_common_100", request);// 隶属机构
		String str3 = MessageUtils.extractMessage("qyll", "qyll_common_6", request);// 订购主题
		String str5 = MessageUtils.extractMessage("qyll", "qyll_common_99", request);// 订购编号
		String str4 = MessageUtils.extractMessage("qyll", "qyll_common_94", request);// 订购状态
		String str6 = MessageUtils.extractMessage("qyll", "qyll_common_111", request);// 短信提醒状态
		String str7 = MessageUtils.extractMessage("qyll", "qyll_common_112", request);// 短信内容
		String str8 = MessageUtils.extractMessage("qyll", "qyll_common_19", request);// 订购时间
		String str9 = MessageUtils.extractMessage("qyll", "qyll_common_158", request);// 提交号码数
		String str10 = MessageUtils.extractMessage("qyll", "qyll_common_113", request);// 订购成功数
		String str11 = MessageUtils.extractMessage("qyll", "qyll_common_114", request);// 订购失败数
		String str12 = MessageUtils.extractMessage("qyll", "qyll_common_201", request);// 已提交
		String str13 = MessageUtils.extractMessage("qyll", "qyll_common_118", request);// 未设定
		String str14 = MessageUtils.extractMessage("qyll", "qyll_common_120", request);// 已发送
		String str15 = MessageUtils.extractMessage("qyll", "qyll_common_119", request);//待审核
		String str16 = MessageUtils.extractMessage("qyll", "qyll_common_121", request);// 定时中
		String str17 = MessageUtils.extractMessage("qyll", "qyll_common_110", request);// 已撤销
		String str18 = MessageUtils.extractMessage("qyll", "qyll_common_202", request);// 已冻结
		String str19 = MessageUtils.extractMessage("qyll", "qyll_common_203", request);// 超时未发送
		String str20 = MessageUtils.extractMessage("qyll", "qyll_common_215", request);// 审核未通过
		
		// { "操作员", "隶属机构", "订购主题", "订购编号", "订购状态", "短信提醒状态", "短信内容", "订购时间", "提交号码数", "订购成功数", "订购失败数" }
		String[] title = { str1, str2, str3, str5, str4, str6, str7, str8, str9, str10, str11 };
		List<String[]> list = new ArrayList<String[]>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String datatime = sdf.format(new Date());// 获得当前时间,用于Excel取名记录
		String filePath = request.getSession().getServletContext().getRealPath("/OrderHistoryExcel")
				.replaceAll("%20", " ");
		String excelPath = request.getSession().getServletContext()
				.getRealPath("/OrderHistoryExcel" + "/" + "OrderHistoryExcel-" + datatime + ".xlsx")
				.replaceAll("%20", " ");
		File file = new File(filePath);// 文件夹不存在时创建
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			OrderTaskVO orderTaskVO = getParameter(request);
			List<OrderTaskVO> orderTaskVOList = dataQueryBIZ.getOrderTaskList(orderTaskVO, null);
			for (OrderTaskVO task : orderTaskVOList) {
				String orderState = task.getOrderState();
				String smsState = task.getSmsState();

				if (orderState != null) {
					if (orderState.equals("0")) {
                        orderState = str12;
                    } else if (orderState.equals("1")) {
                        orderState = str15;
                    } else if (orderState.equals("2")) {
                        orderState = str16;
                    } else if (orderState.equals("3")) {
                        orderState = str17;
                    } else if (orderState.equals("4")) {
                        orderState = str18;
                    } else if (orderState.equals("5")) {
                        orderState = str19;
                    } else if (orderState.equals("6")) {
                        orderState = str20;
                    }
				}
				if (smsState != null) {
					if (smsState.equals("-1")) {
                        smsState = str13;
                    } else if (smsState.equals("0")) {
                        smsState = str14;
                    } else if (smsState.equals("1")) {
                        smsState = str15;
                    } else if (smsState.equals("2")) {
                        smsState = str16;
                    } else if (smsState.equals("3")) {
                        smsState = str17;
                    } else if (smsState.equals("4")) {
                        smsState = str18;
                    } else if (smsState.equals("5")) {
                        smsState = str19;
                    } else if (smsState.equals("6")) {
                        smsState = str20;
                    }
				}
				String[] strArr = { task.getOperator(), task.getOrganization(), task.getTopic(), task.getOrderNo(),
						orderState, smsState, task.getMsg(), task.getOrdertm().toString().substring(0, 19),
						task.getSubCount(), task.getSucCount(),
						Integer.toString((Integer.parseInt(task.getSubCount()) - Integer.parseInt(task.getSucCount()))) };
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
			EmpExecutionContext.error(e,"套餐订购历史导出到Excel并压缩失败！");
		}
	}

	public void downFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			String str = "OrderHistoryExcel.zip";
			String serverPath = request.getSession().getServletContext().getRealPath("/"+str).replaceAll("%20", " ");
			String path = serverPath;
			EmpExecutionContext.info("套餐订购历史Excel路径："+path);
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
					response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(str, "GBK"));// 设置头部信息
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					// 开始向网络传输文件流
					while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
						bouts.write(buffer, 0, bytesRead);
					}
				} finally {
					if(null != bins) {
						bins.close();
					}
					if(null != ins) {
                        ins.close();
                    }
					if(null != bouts){
						bouts.flush();// 这里一定要调用flush()方法
						bouts.close();
					}
					if(null != outs) {
                        outs.close();
                    }
				}
					
//				bouts.flush();// 这里一定要调用flush()方法
//				ins.close();
//				bins.close();
//				outs.close();
//				bouts.close();

                boolean flag = file.delete();// 删除Zip文件
                if (!flag) {
                    EmpExecutionContext.error("刪除文件失敗！");
                }
                ExcelExportUtil excelExportUtil = new ExcelExportUtil();// 删除Excel文件夹
				excelExportUtil.deleteTmpExcel(serverPath.substring(0,serverPath.lastIndexOf("/")+1) + "OrderHistoryExcel");
				file = new File(serverPath.substring(0,serverPath.lastIndexOf("/")+1) + "OrderHistoryExcel");
				flag = file.delete();// 删除Zip文件
                if (!flag) {
                    EmpExecutionContext.error("刪除文件失敗！");
                }
			} else {
				EmpExecutionContext.error("套餐订购历史Excel所在文件夹不存在");
			}
		} catch (IOException e) {
			EmpExecutionContext.error("套餐订购历史下载zip包失败");
		}
	}

	public void exportDetailExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException,
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

		// { "手机号码", "套餐名称", "运营商", "状态报告", "订购编号" }
		String[] title = { str1, str2, str3, str4, str5 };
		List<String[]> list = new ArrayList<String[]>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String datatime = sdf.format(new Date());// 获得当前时间,用于Excel取名记录
		String filePath = request.getSession().getServletContext().getRealPath("/OrderDetailExcel")
				.replaceAll("%20", " ");
		String excelPath = request.getSession().getServletContext()
				.getRealPath("/OrderDetailExcel" + "/" + "OrderDetailExcel-" + datatime + ".xlsx")
				.replaceAll("%20", " ");
		File file = new File(filePath);// 文件夹不存在时创建

		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			OrderDetailVO detailVO = getDetailParameter(request);
			detailVO.setOrderno(request.getParameter("orderno"));
			List<OrderDetailVO> orderDetailVOList = dataQueryBIZ.getOrderDetailList(detailVO, null);
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

				String[] strArr = { detail.getMobile(), detail.getTheme(), isp, state, detail.getOrderno() };
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
			EmpExecutionContext.error("套餐订购详情导出到Excel并压缩失败！");
		}
	}

	public void downDetailFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			String str = "OrderDetailExcel.zip";
			String serverPath = request.getSession().getServletContext().getRealPath("/"+str).replaceAll("%20", " ");
			String path = serverPath;
			EmpExecutionContext.error("套餐订购详情Excel所在路径："+path);
			File file = new File(path);
			if (file.exists()) {
//					InputStream ins = new FileInputStream(path);
//					BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
//					OutputStream outs = response.getOutputStream();// 获取文件输出IO流
//					BufferedOutputStream bouts = new BufferedOutputStream(outs);
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
					response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(str, "GBK"));// 设置头部信息
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
				

				boolean flag=file.delete();// 删除Zip文件
				ExcelExportUtil excelExportUtil = new ExcelExportUtil();// 删除Excel文件夹
				excelExportUtil.deleteTmpExcel(serverPath.substring(0,serverPath.lastIndexOf("/")+1) + "OrderDetailExcel");
				file = new File(serverPath.substring(0,serverPath.lastIndexOf("/")+1) + "OrderDetailExcel");
				flag=file.delete();
			} else {
				EmpExecutionContext.error("套餐订购详情Excel所在文件夹不存在:"+path);
			}
		} catch (IOException e) {
			EmpExecutionContext.error(e,"套餐订购详情下载zip包失败");
		}
	}
	
	private OrderDetailVO getDetailParameter(HttpServletRequest request) throws Exception {
		OrderDetailVO orderDetailVO = new OrderDetailVO();
		String mobile = request.getParameter("mobile");
		String isp = request.getParameter("isp");
		String state = request.getParameter("state");
		orderDetailVO.setMobile((mobile != null && mobile.trim().length() > 0) ? mobile : null);
		orderDetailVO.setIsp((isp != null && isp.trim().length() > 0) ? isp : null);
		orderDetailVO.setState((state != null && state.trim().length() > 0) ? state : null);
		return orderDetailVO;
	}

	private OrderTaskVO getParameter(HttpServletRequest request) throws Exception {
		LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
		OrderTaskVO orderTaskVO = new OrderTaskVO();
		String orderNo = request.getParameter("orderNo");
		String theme = request.getParameter("theme");
		String isp = request.getParameter("isp");
		String state = request.getParameter("state");
		String organization = request.getParameter("organization");
		String operator = request.getParameter("operator");
		
		String replaceStr = "("+MessageUtils.extractMessage("qyll","qyll_common_214",request)+")";//已注销
		operator = operator.isEmpty()?null:operator.replace(replaceStr, "");
		
		String topic = request.getParameter("topic");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String str1 = MessageUtils.extractMessage("qyll", "qyll_common_101", request);// 请选择
		
		// 查询状态为'已订购'的订购任务		
		orderTaskVO.setOrdered(true);
		orderTaskVO.setEcid(llCompInfoBiz.getLlCompInfoBean().getCorpCode());
		orderTaskVO.setOrderNo((orderNo != null && orderNo.trim().length() > 0) ? orderNo : null);
		orderTaskVO.setTheme((theme != null && theme.trim().length() > 0) ? theme : null);
		orderTaskVO.setIsp((isp != null && isp.trim().length() > 0) ? isp : null);
		orderTaskVO.setOrderState((state != null && state.trim().length() > 0) ? state : null);
		orderTaskVO.setOrganization((organization != null && organization.trim().length() > 0 && !organization.trim().equals(str1)) ? organization : null);
		orderTaskVO.setOperator((operator != null && operator.trim().length() > 0 && !operator.trim().equals(str1)) ? operator : null);
		orderTaskVO.setTopic((topic != null && topic.trim().length() > 0) ? topic : null);
		orderTaskVO.setStartTime((startTime != null && startTime.trim().length() > 0) ? startTime : null);
		orderTaskVO.setEndTime((endTime != null && endTime.trim().length() > 0) ? endTime : null);
		return orderTaskVO;
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