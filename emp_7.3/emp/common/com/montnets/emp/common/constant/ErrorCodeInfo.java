package com.montnets.emp.common.constant;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodeInfo implements IErrorCode {
	private Map<String,String> infoMap=new HashMap<String,String>();
	 private static ErrorCodeInfo ZH_CN = null;
	 private static ErrorCodeInfo ZH_TW = null;
	 private static ErrorCodeInfo ZH_HK = null;
	 
	synchronized public static ErrorCodeInfo getInstance() {
		return getInstance(StaticValue.ZH_CN);
	}
	
    synchronized public  static ErrorCodeInfo getInstance(String langName) {
		if (StaticValue.ZH_CN.equals(langName)) {
			if (ZH_CN == null) {
				initZHCN();
			}
			return ZH_CN;
		} else if (StaticValue.ZH_TW.equals(langName)) {
			if (ZH_TW == null) {
				initZHTW();
			}
			return ZH_TW;
		} else if (StaticValue.ZH_HK.equals(langName)) {
			if (ZH_HK == null) {
				initZHHK();
			}
			return ZH_HK;
		} else {
			if (ZH_CN == null) {
				initZHCN();
			}
			return ZH_CN;
		}
    }
	private static void initZHHK() {
		ZH_HK = new ErrorCodeInfo();
		ZH_HK.infoMap.put (B20001, "filter blacklist exception");
		ZH_HK.infoMap.put (B20002, "file flow writing exception.");
		ZH_HK.infoMap.put (B20003, "file flow shutdown exception.");
		ZH_HK.infoMap.put (B20004, "parse EXCEL file exception.");
		ZH_HK.infoMap.put (B20005, "reads the file stream exception.");
		ZH_HK.infoMap.put (B20006, "statistical pre sent number of exceptions.");
		ZH_HK.infoMap.put (B20007, "operator and sending account check not pass.");
		ZH_HK.infoMap.put (B20008, "you haven't configured the audit flow yet. Please contact the administrator.");
		ZH_HK.infoMap.put (B20009, "judge operator audit process exception.");
		ZH_HK.infoMap.put (B20010, "institutional chargeback failure).");
		ZH_HK.infoMap.put (B20011, "operator chargeback failed).");
		ZH_HK.infoMap.put (B20012, "failed to save SMS tasks.");
		ZH_HK.infoMap.put (B20013, "failed to upload and send files to the file server.");
		ZH_HK.infoMap.put (B20014, "filter keyword exception.");
		ZH_HK.infoMap.put (B20015, "access to number file storage path exception.");
		ZH_HK.infoMap.put (B20016, "filter phone segment anomaly.");
		ZH_HK.infoMap.put (B20017, "your configured audit stream has been disabled, please contact the administrator.");
		ZH_HK.infoMap.put (B20018, "parse zip file failed.");
		ZH_HK.infoMap.put (A40000, "the number of valid numbers exceeds the maximum allowable number.");
		ZH_HK.infoMap.put (B20019, "check operator audit flow failure.");
		ZH_HK.infoMap.put (B20020, "failed to find the corresponding effective audit process, please contact the administrator.");
		ZH_HK.infoMap.put (B20021, "short message task sending request failed.");
		ZH_HK.infoMap.put (B20022, "SMS task creation parameter acquisition failed.");
		ZH_HK.infoMap.put (B20023, "failed to upload files to the file server.");
		ZH_HK.infoMap.put (B20024, "failed to get the balance of the organization. ");
		/********** 运营 商余额查询错误码 **********/
		ZH_HK.infoMap.put (B20025, "parameter error");
		ZH_HK.infoMap.put (B20026, "request connection failed");
		ZH_HK.infoMap.put (B20027, "request object failed");
		ZH_HK.infoMap.put (B20028, "platform interface call failed");
		ZH_HK.infoMap.put (B20029, "return the response object to null");
		ZH_HK.infoMap.put (B20030, "decryption request response content failed");
		ZH_HK.infoMap.put (B20031, "parse request response content failed");
		ZH_HK.infoMap.put (B20032, "please check whether the operator's balance query address is correct");
		ZH_HK.infoMap.put (B20033, "balance is non numeric format");
		ZH_HK.infoMap.put (B20034, "interface call failed");
		// MBOSS接口返回信息
		ZH_HK.infoMap.put (B20035, "decryption error (illegal encryption) incorrect data");
		ZH_HK.infoMap.put (B20036, "account or password error");
		ZH_HK.infoMap.put (B20037, "internal error");
		/*******************************************/
		ZH_HK.infoMap.put (B20038, "parse EXCEL file failed, \"%s\" is encrypted file, cannot parse. ");
		ZH_HK.infoMap.put (B20039, "parse file failed, \"%s\" file is not the correct zip format. ");
		ZH_HK.infoMap.put (B20040, "send account password is empty");
		ZH_HK.infoMap.put (B20041, "failed to get the sending number file");
		ZH_HK.infoMap.put (B20042, "parse file failed, \"%s\" file does not exist. ");
		ZH_HK.infoMap.put (B20043, "parse EXCEL file exception or file does not exist). ");
		ZH_HK.infoMap.put (B20044, "failed to obtain SP account billing type. ");
		ZH_HK.infoMap.put (B20045, "failed to obtain SP account balance. ");
		ZH_HK.infoMap.put (B20046, "short message task sending request failed.");

		// servlet层抛出的异常
		ZH_HK.infoMap.put (V10002, "get operator number segment exception). ");
		ZH_HK.infoMap.put (V10003, "upload file exception. ");
		ZH_HK.infoMap.put (V10004, "query keyword exception). ");
		ZH_HK.infoMap.put (V10005, "zip file processing word exception). ");
		ZH_HK.infoMap.put (V10006, "file encoding processing exception). ");
		ZH_HK.infoMap.put (V10007, "filter duplicate number exception). ");
		ZH_HK.infoMap.put (V10008, "statistical pre sent number of exceptions). ");
		ZH_HK.infoMap.put (V10009, "getting blacklist exceptions). ");
		ZH_HK.infoMap.put (V10010, "upload file timeout. ");

		ZH_HK.infoMap.put (V10011, "initialization task object failed). ");
		ZH_HK.infoMap.put (V10012, "failed to create SMS tasks. ");
		ZH_HK.infoMap.put (V10013, "failed to obtain the sending file path. ");
		ZH_HK.infoMap.put (V10014, "exceeds the upload file size limit. ");
		ZH_HK.infoMap.put (V10001, "server failed to receive page request parameters. ");
		ZH_HK.infoMap.put (V10000, "undefined exception). ");
		ZH_HK.infoMap.put (V10015, "failed to submit SMS tasks. ");
		// 发送内容存在关键字
		ZH_HK.infoMap.put (V10016, "");
		ZH_HK.infoMap.put (V10017, " gets the current session empty. ");
		ZH_HK.infoMap.put (V10018, "failed to retrieve the current session login object information. ");
		ZH_HK.infoMap.put (V10021, "no subNo can be used. ");
		ZH_HK.infoMap.put (V10022, "failed to get subNo. ");
		
		// 富信发送异常（富信模块暂时只有简体，且因工期紧急，暂未翻译英文）
		ZH_HK.infoMap.put (RM0001, "从本地或集群获得手机号码文件异常。");
		ZH_HK.infoMap.put (RM0002, "从文件服务器下载手机号码文件异常。");
		ZH_HK.infoMap.put (RM0003, "关闭文本流BufferReader异常.");
		ZH_HK.infoMap.put (RM0004, "手机号码文件Url地址为空。");
		ZH_HK.infoMap.put (RM0005, "定时发送时间小于服务器当前时间。");
		ZH_HK.infoMap.put (RM0006, "富信发送调用模板发送接口异常。");
		ZH_HK.infoMap.put (RM0007, "获取模板ID异常。");
		ZH_HK.infoMap.put (RM0008, "富信发送内容有效时长设置错误。");
		ZH_HK.infoMap.put (RM0009, "taskId在Lf_MTTASK表中已经被使用。");
		ZH_HK.infoMap.put (RM00010, "获取模板参数个数异常。");
		ZH_HK.infoMap.put (RM00011, "获取实体类对象出现异常。");
		ZH_HK.infoMap.put (RM00012, "运营商余额不足，扣费金额大于余额。");
		ZH_HK.infoMap.put (RM00013, "运营商扣费查询余额信息异常，前端应用账户无法找到对应的后端账户。");
		ZH_HK.infoMap.put (RM00014, "富信余额查询操作异常。");
		ZH_HK.infoMap.put (RM00015, "更新或增加实体类对象至数据库中异常。");
		ZH_HK.infoMap.put (RM00016, "企业富信内容有效时长设置错误.");
		ZH_HK.infoMap.put (RM00017, "SP賬號余額不足，扣費金額大於余額。");
		ZH_HK.infoMap.put (RM00018, "SP賬號余額查詢操作異常。");
		
	}
	private static void initZHTW() {
		ZH_TW = new ErrorCodeInfo();
		ZH_TW.infoMap.put(B20001, "過濾黑名單異常。");
		ZH_TW.infoMap.put(B20002, "文件流寫入異常。");
		ZH_TW.infoMap.put(B20003, "文件流關閉異常。");
		ZH_TW.infoMap.put(B20004, "解析EXCEL文件異常。");
		ZH_TW.infoMap.put(B20005, "讀取文件流異常。");
		ZH_TW.infoMap.put(B20006, "統計預發送條數異常。");
		ZH_TW.infoMap.put(B20007, "操作員和發送賬號檢查不通過。");
		ZH_TW.infoMap.put(B20008, "您還未配置審核流，請聯繫管理員。");
		ZH_TW.infoMap.put(B20009, "判斷操作員審核流程異常。");
		ZH_TW.infoMap.put(B20010, "機構扣費失敗。");
		ZH_TW.infoMap.put(B20011, "運營商扣費失敗。");
		ZH_TW.infoMap.put(B20012, "保存簡訊任務失敗。");
		ZH_TW.infoMap.put(B20013, "上傳發送文件到文件伺服器失敗。");
		ZH_TW.infoMap.put(B20014, "過濾關鍵字異常。");
		ZH_TW.infoMap.put(B20015, "獲取號碼文件存放路徑異常。");
		ZH_TW.infoMap.put(B20016, "過濾手機號段異常。");
		ZH_TW.infoMap.put(B20017, "您配置的審核流已被禁用，請聯繫管理員。");
		ZH_TW.infoMap.put(B20018, "解析zip文件失敗。");
		ZH_TW.infoMap.put(A40000, "有效號碼數超出最大允許發送數量。");
		ZH_TW.infoMap.put(B20019, "檢查操作員審核流失敗。");
		ZH_TW.infoMap.put(B20020, "未找到相應的有效審核流程，請聯繫管理員。");
		ZH_TW.infoMap.put(B20021, "簡訊任務發送請求失敗。");
		ZH_TW.infoMap.put(B20022, "簡訊任務創建參數獲取失敗。");
		ZH_TW.infoMap.put(B20023, "上傳文件到文件伺服器失敗。");
		ZH_TW.infoMap.put(B20024, "獲取機構餘額失敗。");
		/********** 運營 商餘額查詢錯誤碼 **********/
		ZH_TW.infoMap.put(B20025, "參數錯誤");
		ZH_TW.infoMap.put(B20026, "請求連接失敗");
		ZH_TW.infoMap.put(B20027, "請求對象設置失敗");
		ZH_TW.infoMap.put(B20028, "平台介面調用失敗");
		ZH_TW.infoMap.put(B20029, "返迴響應對象為空");
		ZH_TW.infoMap.put(B20030, "解密請求響應內容失敗");
		ZH_TW.infoMap.put(B20031, "解析請求響應內容失敗");
		ZH_TW.infoMap.put(B20032, "請檢查運營商餘額查詢地址是否正確");
		ZH_TW.infoMap.put(B20033, "餘額為非數字格式");
		ZH_TW.infoMap.put(B20034, "介面調用失敗");
		// MBOSS介面返回信息
		ZH_TW.infoMap.put(B20035, "解密錯誤(非法加密)不正確的數據");
		ZH_TW.infoMap.put(B20036, "賬號或密碼錯誤");
		ZH_TW.infoMap.put(B20037, "內部錯誤");
		/*******************************************/
		ZH_TW.infoMap.put(B20038, "解析EXCEL文件失敗，\"%s\"為加密文件，無法解析。");
		ZH_TW.infoMap.put(B20039, "解析文件失敗，\"%s\"文件不是正確的zip格式。");
		ZH_TW.infoMap.put(B20040, "發送賬號密碼為空");
		ZH_TW.infoMap.put(B20041, "獲取發送號碼文件失敗");
		ZH_TW.infoMap.put(B20042, "解析文件失敗，\"%s\"文件不存在。");
		ZH_TW.infoMap.put(B20043, "解析EXCEL文件異常或文件不存在。");
		ZH_TW.infoMap.put(B20044, "獲取SP賬號計費類型失敗。");
		ZH_TW.infoMap.put(B20045, "獲取SP賬號餘額失敗。");
		ZH_TW.infoMap.put(B20046, "簡訊任務發送請求失敗。");

		// servlet層拋出的異常
		ZH_TW.infoMap.put(V10002, "獲取運營商號碼段異常。");
		ZH_TW.infoMap.put(V10003, "上傳文件異常。");
		ZH_TW.infoMap.put(V10004, "查詢關鍵字異常。");
		ZH_TW.infoMap.put(V10005, "zip文件處理字異常。");
		ZH_TW.infoMap.put(V10006, "文件編碼處理異常。");
		ZH_TW.infoMap.put(V10007, "過濾重複號碼異常。");
		ZH_TW.infoMap.put(V10008, "統計預發送條數異常。");
		ZH_TW.infoMap.put(V10009, "獲取黑名單異常。");
		ZH_TW.infoMap.put(V10010, "上傳文件超時。");

		ZH_TW.infoMap.put(V10011, "初始化任務對象失敗。");
		ZH_TW.infoMap.put(V10012, "創建簡訊任務失敗。");
		ZH_TW.infoMap.put(V10013, "獲取發送文件路徑失敗。");
		ZH_TW.infoMap.put(V10014, "超出上傳文件大小限制。");
		ZH_TW.infoMap.put(V10001, "伺服器接收頁面請求參數失敗。");
		ZH_TW.infoMap.put(V10000, "未定義異常。");
		ZH_TW.infoMap.put(V10015, "提交簡訊任務失敗。");
		// 發送內容存在關鍵字
		ZH_TW.infoMap.put(V10016, "");
		ZH_TW.infoMap.put(V10017, "獲取當前會話為空。");
		ZH_TW.infoMap.put(V10018, "未獲取到當前會話登錄對象信息。");
		ZH_TW.infoMap.put(V10021, "尾号已经用完。");
		ZH_TW.infoMap.put(V10022, "获取尾号失败。");
		
		// 富信发送异常
		ZH_TW.infoMap.put (RM0001, "從本地或集群獲得手機號碼文件異常。");
		ZH_TW.infoMap.put (RM0002, "從文件服務器下載手機號碼文件異常。");
		ZH_TW.infoMap.put (RM0003, "關閉文本流BufferReader異常.");
		ZH_TW.infoMap.put (RM0004, "手機號碼文件Url地址爲空。");
		ZH_TW.infoMap.put (RM0005, "定時發送時間小于服務器當前時間。");
		ZH_TW.infoMap.put (RM0006, "富信發送調用模板發送接口異常。");
		ZH_TW.infoMap.put (RM0007, "獲取模板ID異常。");
		ZH_TW.infoMap.put (RM0008, "富信發送內容有效時長設置錯誤。");
		ZH_TW.infoMap.put (RM0009, "taskId在Lf_MTTASK表中已經被使用。");
		ZH_TW.infoMap.put (RM00010, "獲取模板參數個數異常。");
		ZH_TW.infoMap.put (RM00011, "獲取實體類對象出現異常。");
		ZH_TW.infoMap.put (RM00012, "運營商余額不足，扣費金額大於余額。");
		ZH_TW.infoMap.put (RM00013, "運營商扣費查詢余額信息異常，前端應用賬戶無法找到對應的後端賬戶。");
		ZH_TW.infoMap.put (RM00014, "富信余額查詢操作異常。");
		ZH_TW.infoMap.put (RM00015, "更新或增加實體類對象至數據庫中異常。");
		ZH_TW.infoMap.put (RM00016, "企業富信內容有效時長設置錯誤.");
		ZH_TW.infoMap.put (RM00017, "SP賬號余額不足，扣費金額大於余額。");
		ZH_TW.infoMap.put (RM00018, "SP賬號余額查詢操作異常。");
	}
	private static void initZHCN() {
		ZH_CN = new ErrorCodeInfo();
		ZH_CN.infoMap.put(B20001, "过滤黑名单异常。");
		ZH_CN.infoMap.put(B20002, "文件流写入异常。");
		ZH_CN.infoMap.put(B20003, "文件流关闭异常。");
		ZH_CN.infoMap.put(B20004, "解析EXCEL文件异常。");
		ZH_CN.infoMap.put(B20005, "读取文件流异常。");
		ZH_CN.infoMap.put(B20006, "统计预发送条数异常。");
		ZH_CN.infoMap.put(B20007, "操作员和发送账号检查不通过。");
		ZH_CN.infoMap.put(B20008, "您还未配置审核流，请联系管理员。");
		ZH_CN.infoMap.put(B20009, "判断操作员审核流程异常。");
		ZH_CN.infoMap.put(B20010, "机构扣费失败。");
		ZH_CN.infoMap.put(B20011, "运营商扣费失败。");
		ZH_CN.infoMap.put(B20012, "保存短信任务失败。");
		ZH_CN.infoMap.put(B20013, "上传发送文件到文件服务器失败。");
		ZH_CN.infoMap.put(B20014, "过滤关键字异常。");
		ZH_CN.infoMap.put(B20015, "获取号码文件存放路径异常。");
		ZH_CN.infoMap.put(B20016, "过滤手机号段异常。");
		ZH_CN.infoMap.put(B20017, "您配置的审核流已被禁用，请联系管理员。");
		ZH_CN.infoMap.put(B20018, "解析zip文件失败。");
		ZH_CN.infoMap.put(A40000, "有效号码数超出最大允许发送数量。");
		ZH_CN.infoMap.put(B20019, "检查操作员审核流失败。");
		ZH_CN.infoMap.put(B20020, "未找到相应的有效审核流程，请联系管理员。");
		ZH_CN.infoMap.put(B20021, "短信任务发送请求失败。");
		ZH_CN.infoMap.put(B20022, "短信任务创建参数获取失败。");
		ZH_CN.infoMap.put(B20023, "上传文件到文件服务器失败。");
		ZH_CN.infoMap.put(B20024, "获取机构余额失败。");
		/********** 运营 商余额查询错误码 **********/
		ZH_CN.infoMap.put(B20025, "参数错误");
		ZH_CN.infoMap.put(B20026, "请求连接失败");
		ZH_CN.infoMap.put(B20027, "请求对象设置失败");
		ZH_CN.infoMap.put(B20028, "平台接口调用失败");
		ZH_CN.infoMap.put(B20029, "返回响应对象为空");
		ZH_CN.infoMap.put(B20030, "解密请求响应内容失败");
		ZH_CN.infoMap.put(B20031, "解析请求响应内容失败");
		ZH_CN.infoMap.put(B20032, "请检查运营商余额查询地址是否正确");
		ZH_CN.infoMap.put(B20033, "余额为非数字格式");
		ZH_CN.infoMap.put(B20034, "接口调用失败");
		// MBOSS接口返回信息
		ZH_CN.infoMap.put(B20035, "解密错误(非法加密)不正确的数据");
		ZH_CN.infoMap.put(B20036, "账号或密码错误");
		ZH_CN.infoMap.put(B20037, "内部错误");
		/*******************************************/
		ZH_CN.infoMap.put(B20038, "解析EXCEL文件失败，\"%s\"为加密文件，无法解析。");
		ZH_CN.infoMap.put(B20039, "解析文件失败，\"%s\"文件不是正确的zip格式。");
		ZH_CN.infoMap.put(B20040, "发送账号密码为空");
		ZH_CN.infoMap.put(B20041, "获取发送号码文件失败");
		ZH_CN.infoMap.put(B20042, "解析文件失败，\"%s\"文件不存在。");
		ZH_CN.infoMap.put(B20043, "解析EXCEL文件异常或文件不存在。");
		ZH_CN.infoMap.put(B20044, "获取SP账号计费类型失败。");
		ZH_CN.infoMap.put(B20045, "获取SP账号余额失败。");
		ZH_CN.infoMap.put(B20046, "短信任务发送请求失败。");

		// servlet层抛出的异常
		ZH_CN.infoMap.put(V10002, "获取运营商号码段异常。");
		ZH_CN.infoMap.put(V10003, "上传文件异常。");
		ZH_CN.infoMap.put(V10004, "查询关键字异常。");
		ZH_CN.infoMap.put(V10005, "zip文件处理字异常。");
		ZH_CN.infoMap.put(V10006, "文件编码处理异常。");
		ZH_CN.infoMap.put(V10007, "过滤重复号码异常。");
		ZH_CN.infoMap.put(V10008, "统计预发送条数异常。");
		ZH_CN.infoMap.put(V10009, "获取黑名单异常。");
		ZH_CN.infoMap.put(V10010, "上传文件超时。");

		ZH_CN.infoMap.put(V10011, "初始化任务对象失败。");
		ZH_CN.infoMap.put(V10012, "创建短信任务失败。");
		ZH_CN.infoMap.put(V10013, "获取发送文件路径失败。");
		ZH_CN.infoMap.put(V10014, "超出上传文件大小限制。");
		ZH_CN.infoMap.put(V10001, "服务器接收页面请求参数失败。");
		ZH_CN.infoMap.put(V10000, "未定义异常。");
		ZH_CN.infoMap.put(V10015, "提交短信任务失败。");
		// 发送内容存在关键字
		ZH_CN.infoMap.put(V10016, "");
		ZH_CN.infoMap.put(V10017, "获取当前会话为空。");
		ZH_CN.infoMap.put(V10018, "未获取到当前会话登录对象信息。");
		ZH_CN.infoMap.put(V10021, "尾号已经用完。");
		ZH_CN.infoMap.put(V10022, "获取尾号失败。");
		
		// 富信发送异常
		ZH_CN.infoMap.put (RM0001, "从本地或集群获得手机号码文件异常。");
		ZH_CN.infoMap.put (RM0002, "从文件服务器下载手机号码文件异常。");
		ZH_CN.infoMap.put (RM0003, "关闭文本流BufferReader异常.");
		ZH_CN.infoMap.put (RM0004, "手机号码文件Url地址为空。");
		ZH_CN.infoMap.put (RM0005, "定时发送时间小于服务器当前时间。");
		ZH_CN.infoMap.put (RM0006, "富信发送调用模板发送接口异常。");
		ZH_CN.infoMap.put (RM0007, "获取模板ID异常。");
		ZH_CN.infoMap.put (RM0008, "富信发送内容有效时长设置错误。");
		ZH_CN.infoMap.put (RM0009, "taskId在Lf_MTTASK表中已经被使用。");
		ZH_CN.infoMap.put (RM00010, "获取模板参数个数异常。");
		ZH_CN.infoMap.put (RM00011, "获取实体类对象出现异常。");
		ZH_CN.infoMap.put (RM00012, "运营商余额不足，扣费金额大于余额。");
		ZH_CN.infoMap.put (RM00013, "运营商扣费查询余额信息异常，前端应用账户无法找到对应的后端账户。");
		ZH_CN.infoMap.put (RM00014, "富信余额查询操作异常。");
		ZH_CN.infoMap.put (RM00015, "更新或增加实体类对象至数据库中异常。");
		ZH_CN.infoMap.put (RM00016, "企业富信内容有效时长设置错误。");
		ZH_CN.infoMap.put (RM00017, "SP账号余额不足，扣费金额大于余额。");
		ZH_CN.infoMap.put (RM00018, "SP账号余额查询操作异常。");
	}
	private ErrorCodeInfo()
	{}
	
	public String getErrorInfo(String code) {
		if(infoMap.get(code) == null){
			return null;
		}
		return "empex"+infoMap.get(code) +"["+code+"]";
	}
	
	/**
	 * 返回错误码描述
	 */
	public String getErrorDes(String code)
	{
		if(infoMap.get(code) == null){
			return "["+code+"]";
		}
		return infoMap.get(code) +"["+code+"]";
	}

}
