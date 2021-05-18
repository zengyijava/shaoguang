/*文字提示设置js*/
$(document).ready(function() {
	addTipFrame();
	setMtSerTip();
	setSmstmpTip();
});

function addTipFrame()
{
	$('div#rContent').append(
		'<iframe id="ifr" class="ifr"></iframe>' +
		'<div id="id2" class="remindMessage"></div>');

}

//下行业务
function setMtSerTip()
{
	//业务名称
	/*业务名称对应一个业务逻辑。*/
	floatingRemind("spanSerName",getJsLocaleMessage("common","common_js_tipcontent_1"));
	/*业务处理流程步骤的名称。*/
	floatingRemind("spanPrName",getJsLocaleMessage("common","common_js_tipcontent_2"));
	/*业务处理流程步骤的类型。Select类型：该步骤处理的是一个数据库查询语句；Reply类型：该步骤是用来设置回复短信的内容与格式。*/
	floatingRemind("spanPrType",getJsLocaleMessage("common","common_js_tipcontent_3"));
	/*业务处理流程Select类型步骤获取发送短信参数数据的来源。*/
	floatingRemind("spanDBL",getJsLocaleMessage("common","common_js_tipcontent_4"));
	/*业务处理流程Select类型步骤查询数据库获取参数数据的SQL语句模板。*/
	floatingRemind("spanSqlTemp",getJsLocaleMessage("common","common_js_tipcontent_5"));
	/*业务处理流程Select类型步骤查询数据库获取参数数据的SQL语句。*/
	floatingRemind("spanSql",getJsLocaleMessage("common","common_js_tipcontent_6"));
	/*业务处理流程发送短信所需参数的获取和处理，包括手机号和短信内容参数，即Select类型步骤所获取的数据。*/
	floatingRemind("spanDataSource",getJsLocaleMessage("common","common_js_tipcontent_7"));
	/*发送短信的内容。可使用系统定义的插入符编写动态短信内容，其中插入符#P_1#代表第一个参数，#P_2#代表第二个参数，如此类推。*/
	floatingRemind("spanMsgCon",getJsLocaleMessage("common","common_js_tipcontent_8"));
	/*预览第一条短信的发送内容。*/
	floatingRemind("spanPreview",getJsLocaleMessage("common","common_js_tipcontent_9"));
	/*业务开始执行时间。*/
	floatingRemind("spanBeginTime",getJsLocaleMessage("common","common_js_tipcontent_10"));
	/*业务执行的频率。一次性表示该业务只执行一次；每天则表示该业务每天执行一次。*/
	floatingRemind("spanSendCount",getJsLocaleMessage("common","common_js_tipcontent_11"));
	/*到达业务所设定的执行时间而业务未能执行时，允许业务再次执行的时间范围。默认为24小时，即业务到达执行时间而未能执行时，以执行时间起计算的24小时内均会尝试重新执行。*/
	floatingRemind("spanValiddate",getJsLocaleMessage("common","common_js_tipcontent_12"));
	
}

//短信模板
function setSmstmpTip()
{
	/*模板名称长度不能大于20*/
	floatingRemind("spanSmsTmpName",getJsLocaleMessage("common","common_js_tipcontent_13"));

	//floatingRemind("spanSmsTmpCont","如果录入动态模板，参数格式为\"#P_1#\"（如：我们#P_1#去#P_2#。）");
	
	/*
	 *  长度不能大于20
	 */
}


