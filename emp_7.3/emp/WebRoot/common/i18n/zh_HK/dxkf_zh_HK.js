var dxkf = {
		// 短信群发模块
		// common.js
		'dxkf_dxqf_common_spechar':'The input contains non-allowed special characters',
		'dxkf_dxqf_common_specharend':'The input can not end with \"\\\"!',
		'dxkf_dxqf_common_goback':'return',
		'dxkf_dxqf_common_returnback':'Back to previous',
		'dxkf_dxqf_common_namerule':'Step name can only be composed of Chinese characters, English letters, numbers, underlined!',
		'dxkf_dxqf_common_param':'parameter',
		'dxkf_dxqf_common_versionerror':'You are using a browser version that does not support this feature. Please use Google Chrome or IE9 and above!',
		// clientSend.js
		"dxkf_dxqf_client_ylxg":"Preview effect",
		"dxkf_dxqf_client_yebzbfs":"Organization balance is not allowed to send!",
		"dxkf_dxqf_client_spyebzbfs":"SP account balance is insufficient, not allowed to send, please contact the administrator recharge!",
		"dxkf_dxqf_client_sendtimeerror":"The pre-delivery time is less than the server's current time! Please have a reasonable scheduled delivery time!",
		"dxkf_dxqf_client_noywle":"No business type available!",
		"dxkf_dxqf_client_nosp":"There is no available SP account!",
		"dxkf_dxqf_client_addphone":"Please add a send number!",
		"dxkf_dxqf_client_smscontenterror":"Please fill in the correct text message!",
		"dxkf_dxqf_client_noweihao":"There is no extension available for sending!",
		"dxkf_dxqf_client_weihaoerror1":"Mobile, Unicom, the main channel number of telecommunications + extension tail number more than 20, can not send!",
		"dxkf_dxqf_client_weihaoerror2":"Mobile, Unicom main channel number + extension tail number more than 20, can not send!",
		"dxkf_dxqf_client_weihaoerror3":"Mobile, telecommunications main channel number + extension tail number more than 20, can not send!",
		"dxkf_dxqf_client_weihaoerror4":"Unicom, the main channel number of telecommunications + extension tail number more than 20, can not send!",
		"dxkf_dxqf_client_weihaoerror5":"Move the main channel number + extension tail number more than 20, can not send!",
		"dxkf_dxqf_client_weihaoerror6":"Unicom main channel number + extension tail number more than 20, can not send!",
		"dxkf_dxqf_client_weihaoerror7":"Telecom main channel number + extension tail number more than 20, can not send!",
		"dxkf_dxqf_client_nophonebook":"Unselected address book!",
		"dxkf_dxqf_client_nodatasource":"Please select the data source!",
		"dxkf_dxqf_client_nosql":"SQL statement is empty, SMS task can not add!",
		"dxkf_dxqf_client_resubmit":"Submission of information, please do not submit!",
		"dxkf_dxqf_client_cannotsmscontent":"Not sent as a text message",
		"dxkf_dxqf_client_sendtimeerror2":"The pre-delivery time is less than the server's current time! Please have a reasonable scheduled delivery time[EXFV011]",
		"dxkf_dxqf_client_errorphone":"No valid send number could not be submitted.",
		"dxkf_dxqf_client_falsephone":"Invalid number:",
		"dxkf_dxqf_client_inputphone":"Please enter the phone number!",
		"dxkf_dxqf_client_uploadfileerror":"Upload file format is wrong, please select txt format file.",
		"dxkf_dxqf_client_nofileupload":"Please choose to upload the file!",
		"dxkf_dxqf_client_smslength":"SMS sent content is too long, should be less than",
		"dxkf_dxqf_client_gzf":"character!",
		"dxkf_dxqf_client_keyvalueerror":"Check keyword failed!",
		"dxkf_dxqf_client_cannotword":"Sending content contains the following prohibited phrases：\n     ",
		"dxkf_dxqf_client_reinput":"\nPlease check and re-enter.",
		"dxkf_dxqf_client_yloptfalse":"Preview Fail![EKFV242]",
		"dxkf_dxqf_client_ylfalse":"Preview failed：",
		"dxkf_dxqf_client_inputsendtime":"Please fill in the regular time!",
		"dxkf_dxqf_client_ylerror":"Preview exception, Fail![EKFV241]",
		"dxkf_dxqf_client_nocgxfile":"Draft number file does not exist!",
		"dxkf_dxqf_client_codenum":"Numbering",
		"dxkf_dxqf_client_phoenum":"Phone",
		'dxkf_dxqf_client_yys':'Operator',
		"dxkf_dxqf_client_smscontent":"Content",
		"dxkf_dxqf_client_nophonenumatbook":"The address book does not contain a number!",
		"dxkf_dxqf_client_nonomalphone":"No valid number",
		"dxkf_dxqf_client_yebzdqye":"Operator balance is insufficient, current balance:",
		"dxkf_dxqf_client_yebznosend":"Obtain operator balance failed, not allowed to send",
		"dxkf_dxqf_client_spyeerror":"Get sp account balance information exception",
		"dxkf_dxqf_client_getmbfalse":"Get template failed!",
		"dxkf_dxqf_client_getmberror":"Get template exception!",
		"dxkf_dxqf_page_formdept":"From the organization",
		"dxkf_dxqf_page_formuser":"From the operator",
		"dxkf_dxqf_page_nochoosesmsmb":"Not selected SMS template!",
		"dxkf_dxqf_page_getweihaofalse":"Expand tail number failed!",
		"dxkf_dxqf_page_gettongdaofalse":"Channel number failed!",
		"dxkf_dxqf_client_systemnoweihao":"There is no extension tail available for the system!",
		"dxkf_dxqf_client_kftxlfs":"Customer address book sent",
		"dxkf_dxqf_client_grqzfs":"Personal group sent",
		"dxkf_dxqf_page_khsxfs":"Customer attributes are sent",
		"dxkf_dxqf_client_suretodelte":"Are you sure you want to delete it?",
		"dxkf_dxqf_page_pleasechoose":"Please choose",
		"dxkf_dxqf_page_contentorphonenotnull":"Send content and send number can not be empty!",
		"dxkf_dxqf_client_nochoosecgx":"Did not choose drafts!",
		"dxkf_dxqf_page_isconvertcgx":"Does it cover existing drafts?",
		"dxkf_dxqf_page_caogaoxiang":"Draft",
		"dxkf_dxqf_page_savecgxsuucess":"Temporary draft success!",
		"dxkf_dxqf_page_cgxnofile":"Draft number file does not exist!",
		"dxkf_dxqf_page_cgxisdelete":"The draft box may have been deleted, the temporary failure!",
		"dxkf_dxqf_page_savecgxfalse":"Temporary draft box failed!",
		// 页面 kfs_sendClientSMS.jsp
		"dxkf_dxqf_page_ggsg":"Sent to",
		"dxkf_dxqf_page_gkh":"customer",
		'dxkf_dxqf_page_nophonenosend':'No send number, the message will not be sent!',
		'dxkf_dxqf_page_chosepropties':'Please select at least one customer attribute!',
		'dxkf_dxqf_page_neterror':'Load page failed, please check whether the network is normal!',
		'dxkf_dxqf_page_addtaskfalse':'Task creation failed:',
		'dxkf_dxqf_page_addtasksuccess':'Create SMS tasks and timed tasks to add success!',
		'dxkf_dxqf_page_cancelfileupload':'Upload number file failed, cancel task created!',
		'dxkf_dxqf_page_createtaskfalse':'Create a timed task failed to cancel the task creation!',
		'dxkf_dxqf_page_createsmssuccess':'Create SMS task and submit to approval flow success!',
		'dxkf_dxqf_page_savesuccess':'Save draft success!',
		'dxkf_dxqf_page_nophoneinbook':'There is no sendable number in the address book!',
		'dxkf_dxqf_page_createsmsfalse':'Create SMS task failed!',
		'dxkf_dxqf_page_yuebuzu':'Organization balance is insufficient to create SMS task failed!',
		'dxkf_dxqf_page_updatecostfalse':'When creating a text message task, change billing information to fail!',
		'dxkf_dxqf_page_timeout':'The sending time has exceeded the timed time and can not be sent!',
		'dxkf_dxqf_page_noweihao':'Not set tail number!',
		'dxkf_dxqf_page_addweihaofalse':'Expand tail number processing failed!',
		'dxkf_dxqf_page_hqyysyesb':'Get operator balance failed, cancel task creation!',
		'dxkf_dxqf_page_yysyebz':'Operator balance is insufficient to cancel task creation!',
		'dxkf_dxqf_page_spyebz':'SP account balance is insufficient, do not allow to send, please contact the administrator recharge',
		'dxkf_dxqf_page_spyesb':'Query SP account balance failed!',
		'dxkf_dxqf_page_sendfalse':'Failed to send request to gateway!',
		'dxkf_dxqf_page_nofile':'file does not exist',
		'dxkf_dxqf_page_donotjumperror':'An exception can not jump',
		'dxkf_dxqf_page_nopepolechoose':'You did not choose anybody!',
		'dxkf_dxqf_page_setdefault':'Confirm if the current option is set to default? ',
		'dxkf_dxqf_page_setdefaultsuccess':'Current option saved has been set as default successfully! ',
		'dxkf_dxqf_page_setdefaultfalse':'Failed to set current option as default settings! ',

		// clientChooseInfo_new.js
		'dxkf_dxqf_clientchooseinfo_guolvcf':'Choose to repeat records or no members of the records will be automatically filtered!',
		'dxkf_dxqf_clientchooseinfo_sxz':'Attribute value',
		'dxkf_dxqf_clientchooseinfo_wechat':'Wechat',
		'dxkf_dxqf_clientchooseinfo_signuser':'Sign user',
		'dxkf_dxqf_clientchooseinfo_user':'client',
		'dxkf_dxqf_clientchooseinfo_nodatatoremove':'There is no data to remove!',
		'dxkf_dxqf_clientchooseinfo_domyself':'Self-built',
		'dxkf_dxqf_clientchooseinfo_employee':'Employee',
		
		// 页面 kfs_chooseSendInfo.jsp	
		'dxkf_dxqf_page2_choosegroup':'Please select group!',
		'dxkf_dxqf_page2_gqzytj':'The group has been added!',
		'dxkf_dxqf_page2_mouserinthegroup':'No members under this group!',
		'dxkf_dxqf_page2_pepole':' person',
		'dxkf_dxqf_page2_group':'group',
		'dxkf_dxqf_page2_qxzqytc':'Please choose a package!',
		'dxkf_dxqf_page2_gqytcytj':'The signing package has been added!',
		'dxkf_dxqf_page2_gqytcxmycy':'There are no members under this contract!',
		'dxkf_dxqf_page2_qytc':'Sign the package',
		'dxkf_dxqf_page2_qxzkhsx':'Please select customer attributes!',
		'dxkf_dxqf_page2_gkhsxytj':'The client attribute has been added!',
		'dxkf_dxqf_page2_gkhsxxmycy':'There are no members under this client attribute!',
		'dxkf_dxqf_page2_khsx':'Customer attributes',
		'dxkf_dxqf_page2_choosedept':'Please choose the organization!',
		'dxkf_dxqf_page2_jlytj':'Record added!',
		'dxkf_dxqf_page2_gjgybhztjdjgn':'The agency is already included in the added organization!',
		'dxkf_dxqf_page2_ishavechilddept':'Click "OK" to include all sub-organizations, "Cancel" only select the current organization',
		'dxkf_dxqf_page2_nouserindept':'There are no members under this Organization!',
		'dxkf_dxqf_page2_dept':'Organization',
		'dxkf_dxqf_page2_childdept':'Include subunits',
		'dxkf_dxqf_page2_nosignuser':'No search for signed users!',
		'dxkf_dxqf_page2_chooseywtc':'Please choose your business package first!',
		'dxkf_dxqf_page2_noprepage':'First page, no previous page!',
		'dxkf_dxqf_page2_nonextpage':'Has the last page!',
		
		// 页面 kfs_searchPage.jsp
		'dxkf_dxqf_page3_qingxuanze':'Please choose',
    	'dxkf_dxqf_page3_qingxuanzeperson':'Please choose client or organization',

		// 生日祝福模块
		// birManage.js
		'dxkf_srzf_birManage_updatesuccess':'Update status is successful!',
		'dxkf_srzf_birManage_updatefalse':'Update status failed!',
		'dxkf_srzf_birManage_confiretodeletset':'Confirm delete settings?',
		'dxkf_srzf_birManage_deletesuccess':'Successfully deleted!',
		'dxkf_srzf_birManage_deletefalse':'Failed to delete!',
		
		// 页面 bir_birthdaySendManage.jsp
		'dxkf_srzf_page4_setsuccess':'Set up successfully!',
		
		// birSend.js
		'dxkf_srzf_birSend_zjd':'Dear ',
		'dxkf_srzf_birSend_xgcy':'Edit',
		
		// 页面 bir_birthdaySend.jsp
		'dxkf_srzf_page5_isUseTd':'Enabled must take effect before 23:00 every day and the second day',
		'dxkf_srzf_page5_txfdzc':'Please fill in the enclosed name!',
		'dxkf_srzf_page5_wishnotnull':'Blessing language content can not be empty!',
		'dxkf_srzf_page5_zscc':'Word count over limit! please enter again',
		'dxkf_srzf_page5_sendtimenotnull':'Send time can not be empty!',
		'dxkf_srzf_page5_receiversnotnull':'Receivers can not be empty!',
		'dxkf_srzf_page5_accountnotnull':'Send account can not be empty!',
		'dxkf_srzf_page5_glkeyfalse':'Filter keyword failed!',
		'dxkf_srzf_page5_optionfalse':'Fail',
        'dxkf_srzf_page5_sendtimenotlessthancurrenttime':'Send time must than the current time!',
		
		// birInfo.js
		
		// 页面bir_birthSendInfo.jsp	
		'dxkf_srzf_page6_jlytj':'Record added!',
		'dxkf_srzf_page6_bindedchoose':'Gray indicates that the Organization has been bound and can still be selected',
		'dxkf_srzf_page6_khtxl':'Client address book',
		'dxkf_srzf_page6_bncftj':'Can not be added repeatedly!',

    	"dxkf_common_advancedSearch":"Advanced Search"
};