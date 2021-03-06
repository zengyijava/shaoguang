var client = {
		// 客户通讯录管理
		// 公共使用部分
		'client_common_confiretodelete':'確認刪除該記錄？',
		'client_common_checknet':'請檢查網絡/數據庫是否正常！',
		'client_common_addsuc':'添加成功！',
		'client_common_addfalse':'添加失敗！',
		'client_common_deletesuc':'刪除成功！',
		'client_common_deletefalse':'刪除失敗！',
		'client_common_delete':'刪除',
		'client_common_personpermission':'對不起，您是個人權限，不能進行綁定操作！',
		'client_common_optsuc':'操作成功！',
		'client_common_optfalse':'操作失敗！',
		'client_common_modifysuc':'修改成功！',
		'client_common_modifyfalse':'修改失敗！',
		'client_common_jumperror':'出現異常,無法跳轉',
		'client_common_nettimeout':'網絡超時，請重新登錄！',
		'client_common_exportexcel':'確定要導出數據到excel?',
		'client_common_exportfalse':'導出失敗！',
		'client_common_datatoobig':'數據量超過導出的範圍50萬，請從數據庫中導出！',
		'client_common_nodatatoexport':'無數據可導出！',
		// 客户通讯录
		// page cli_noPermission.jsp
		// addrbook.js
		'client_js_addrbook_selectrecoed':'請選擇一個或多個記錄進行操作！',
		'client_js_addrbook_deletefalse':'刪除選中記錄失敗！',
		'client_js_addrbook_deletesuc':'刪除選中記錄成功！',
		'client_js_addrbook_noselectbook':'未勾選通訊錄信息！',
		'client_js_addrbook_noselectgroup':'未選擇分組！',
		'client_js_addrbook_synusersuc':'同步員工信息成功！',
		'client_js_addrbook_synuserfalse':'同步員工信息失敗！',
		'client_js_addrbook_synclientsuc':'同步客戶信息成功！',
		'client_js_addrbook_synclientfalse':'同步客戶信息失敗！',
		'client_js_addrbook_userisopt':'該員工已經是操作員！',
		'client_js_addrbook_syndeptsuc':'同步機構信息成功！',
		'client_js_addrbook_syndeptfalse':'同步機構信息失敗！',
		'client_js_addrbook_selectuseropt':'請選擇一個員工進行操作！',
		'client_js_addrbook_pageminnum':'每頁顯示數量必須為一個大於0的整數！',
		'client_js_addrbook_pagemaxnum':'每頁顯示數量不能大於500！',
		'client_js_addrbook_toobig':'輸入頁數大於最大頁數！',
		'client_js_addrbook_jumpnum':'跳轉頁必須為一個大於0的整數！',
		'client_js_addrbook_total':'共',
		'client_js_addrbook_tiaodi':'條，第',
		'client_js_addrbook_page':'頁',
		'client_js_addrbook_tiaoye':'條/頁',
		'client_js_addrbook_totaladd':'共添加',
		'client_js_addrbook_tjldz':'條記錄到組：',
		'client_js_addrbook_di':'第',
		// page cli_clientAddrBook.jsp
		'client_page_clientAddrBook_samename':'名稱重複',
		'client_page_clientAddrBook_entername':'請輸入名稱！',
		'client_page_clientAddrBook_errorname':'名稱中不能包含單引號！',
		'client_page_clientAddrBook_enterorgcode':'請輸入機構編碼！',
		'client_page_clientAddrBook_orgcodeerror':'請合理輸入機構編碼！',
		'client_page_clientAddrBook_entererror':'輸入非法，編碼只能是字母和數字組成！',
		'client_page_clientAddrBook_getorgexp':'獲取機構數異常！',
		'client_page_clientAddrBook_orgisnull':'該機構不存在，可能已被刪除！',
		'client_page_clientAddrBook_orgmax':'機構級數最大限制為',
		'client_page_clientAddrBook_ji':'級！',
		'client_page_clientAddrBook_childorgmax':'子機構最大限制為',
		'client_page_clientAddrBook_ge':'個！',
		'client_page_clientAddrBook_orgmaxnum':'機構總數最大限制為',
		'client_page_clientAddrBook_reorgcode':'機構編碼重複！',
		'client_page_clientAddrBook_reorgname':'機構名稱重複！',
		'client_page_clientAddrBook_addorgsuc':'添加機構成功！',
		'client_page_clientAddrBook_addorgfalse':'新增機構失敗！',
		'client_page_clientAddrBook_selectorgdelete':'請選擇要刪除的機構！',
    	'client_page_clientAddrBook_selectclientdelete':'請選擇要刪除的客戶！',
		'client_page_clientAddrBook_confiredeleteorg':'刪除機構會刪除該機構下成員，確認刪除嗎？',
		'client_page_clientAddrBook_childorgnotdel':'該機構下有子機構不能刪除！',
		'client_page_clientAddrBook_rootorgnotdel':'總機構不能刪除！',
		'client_page_clientAddrBook_parentorgnotdel':'頂級機構不能刪除！',
		'client_page_clientAddrBook_apporgnotdes':'"APP未掛接用戶"機構不允許刪除！',
		// page a_addrbookDepTree.jsp
		'client_page_addrbookDepTree_add':'新增',
		'client_page_addrbookDepTree_edit':'編輯',
		'client_page_addrbookDepTree_selectorg':'請選擇機構！',
		'client_page_addrbookDepTree_selectorgmodify':'請選擇要修改的機構！',
		'client_page_addrbookDepTree_addorg':'新增機構',
		'client_page_addrbookDepTree_appuserinfo':'APP用戶信息',
		'client_page_addrbookDepTree_modifyorgname':'修改機構名稱',
		'client_page_addrbookDepTree_cannotmodifyorgname':'機構名稱不允許修改！',
		'client_page_addrbookDepTree_enterorgname':'請輸入機構名稱！',
		'client_page_addrbookDepTree_nomofifyorgname':'機構名稱沒有做修改！',
		'client_page_addrbookDepTree_reorgname':'機構名稱重複，請檢查後再修改！',
		'client_page_addrbookDepTree_unallowsamename':'不好意思，您所修改的機構名稱在同機構下有相同機構名稱，請重新輸入！',
		//通讯录权限管理
		//permissions.js
		'client_js_permissions_noselect':'未勾選信息！',
		'client_js_permissions_confiredelselect':'確定刪除勾選信息嗎？',
		'client_js_permissions_delsuctotal':'刪除成功,共移除',
		'client_js_permissions_infos':'條信息！',
		'client_js_permissions_cannotdel':'企業管理員頂級部門權限不能被刪除',
		'client_js_permissions_totalremove':'共移除',
		'client_js_permissions_donotdel':'條,其中企業管理員頂級部門權限不能被刪除',
		'client_js_permissions_selectoneorg':'請先選擇一個機構！',
		'client_js_permissions_notbindnullorg':'該機構不存在，可能已被刪除，不能進行綁定操作！',
		// page cli_clientPermissions.jsp
		//客户属性管理
		// custField.js
		'client_js_custField_fieldStr':'<客戶號><姓名><手機><職務><行業><所屬機構><性別><客戶經理><QQ><所屬區域><MSN><客戶描述><E-mail><座機><生日>',
		'client_js_custField_selectattr':'請選擇客戶屬性！',
		'client_js_custField_attrisnotnull':'屬性值不能為空！',
		'client_js_custField_attrtoolong':'屬性值長度過長！',
		'client_js_custField_reattr':'屬性值已經存在，請重新輸入！',
		'client_js_custField_selectattrdel':'請選擇您要刪除的屬性值！',
		'client_js_custField_confiredelattr':'您確定要刪除當前屬性的屬性值？',
		'client_js_custField_confiredelattrrecord':'您確定要刪除當前屬性記錄？',
		'client_js_custField_selectdelattrubites':'請選擇要刪除的屬性！',
		'client_js_custField_confiretodelthisattr':'當前屬性存在屬性值，您確定要刪除當前屬性？',
		'client_js_custField_confiredelcurattr':'您確定要刪除該屬性？',
		'client_js_custField_delattrsuc':'刪除屬性成功！',
		'client_js_custField_attrisnull':'該屬性不存在，可能已被刪除！',
		'client_js_custField_delattrfalse':'刪除屬性失敗！',
		'client_js_custField_attrexcite':'該屬性在客戶基本信息中已存在！',
		'client_js_custField_enterattrname':'請輸入要新增的屬性名稱！',
		'client_js_custField_reattrname':'當前屬性名稱已經存在，請重新輸入！',
		'client_js_custField_addattrsuc':'新建屬性成功！',
		'client_js_custField_addattrfalse':'新建屬性失敗！',
		'client_js_custField_enterattrname':'请输入属性名称！',
		'client_js_custField_atteisexcite':'該屬性在客戶基本信息中已存在！',
		'client_js_custField_enterattrvalue':'請選擇輸入屬性值！',
		'client_js_custField_addattrvaluesuc':'添加屬性值成功！',
		'client_js_custField_attrvalexcite':'添加屬性值失敗！屬性裡已經有這個屬性值了！',
		'client_js_custField_addattrvalfalse':'添加屬性值失敗！',
		'client_js_custField_selectleftattr':'請選擇左邊欄的屬性！',
		// 新建客户通讯录
		// addClient.js
		'client_js_addClient_fileisnull':'文件不存在',
		'client_js_addClient_cannotparexls':'無法解析的xls格式的文件，請上傳正確的xls格式文件！',
		'client_js_addClient_fileattrerror':'上傳文件中屬性列不匹配，請按照模板上傳！',
		'client_js_addClient_filetoobig':'每次2007版本的EXCEL上傳不能超過20萬條！',
		'client_js_addClient_selectorgisnull':'所屬機構不能為空！',
		'client_js_addClient_importexcelfile':'沒有選擇導入文件，請導入Excel文件！',
		'client_js_addClient_nosuporttype':'不支持此種文件類型，請導入後綴名為.xls和.xlsx的文件！',
		'client_js_addClient_waitupload':'上傳處理中，請稍候...',
		'client_js_addClient_clickselectorg':'點擊選擇機構',
		// book.js
		'client_js_book_all':'全部',
		'client_js_book_emailerror':'郵箱格式不正確',
		'client_js_book_nameisnull':'姓名不能為空！',
		'client_js_book_nameformaterror':'姓名含有非法字符！',
		'client_js_book_phoneisnull':'手機不能為空！',
		'client_js_book_phoneformaterror':'手機號碼不正確！',
		'client_js_book_logintimeout':'登錄超時，請重新登錄！',
		'client_js_book_phoneformatill':'手機號碼格式不正確！',
		'client_js_book_phoneisexcite':'該手機號碼已被其他客戶使用,是否繼續添加?',
		'client_js_book_donotmidify':'該機構下已存在姓名、手機號碼相同的客戶，不允許修改！',
		// editClient.js
		'client_js_editClient_confiremofifycurgoup':'您確定要修改當前群組記錄？',
		'client_js_editClient_curgroupnomodify':'當前所屬群組未做修改！',
		'client_js_editClient_grouphaduser':'此群組已存在該成員！',
		'client_js_editClient_grouphadphone':'此群組已存在此手機號碼！',
		'client_js_editClient_queryfalse':'查詢數據失敗！',
		'client_js_editClient_selectdelgroup':'請選擇您要刪除的群組記錄！',
		'client_js_editClient_confiredelcurgroup':'您確定要刪除當前群組記錄？',
		'client_js_editClient_selectdelgroup':'請選擇要刪除的群組！',
		'client_js_editClient_donotdelgroup':'當前群組下存在成員，您確定要刪除當前群組？',
		'client_js_editClient_delthisgroup':'您確定要刪除該群組？',
		'client_js_editClient_delgroupsuc':'刪除群組成功！',
		'client_js_editClient_delgroupfalse':'刪除群組失敗！',
		'client_js_editClient_enterwordill':'請輸入合法字符！',
		'client_js_editClient_entergroupname':'請輸入要新建的群組名稱！',
		'client_js_editClient_regroupname':'當前群組名稱已經存在，請重新輸入！',
		'client_js_editClient_newgroup':'您確定要新建該群組？',
		'client_js_editClient_newgroupsuc':'新建群組成功！',
		'client_js_editClient_newgroupfalse':'新建群組失敗！',
		'client_js_editClient_ranamegroup':'請選擇要重命名的群組！',
		'client_js_editClient_curgroupnamenomodify':'當前群組名稱未做改變！',
		'client_js_editClient_confirerenamegroup':'您確定要重命名該群組？',
		'client_js_editClient_renamesuc':'重命名成功！',
		'client_js_editClient_renamefalse':'重命名失敗！',
		'client_js_editClient_selectleftgroup':'請選擇左邊欄的群組！',
		'client_js_editClient_confire':'確定',
		'client_js_editClient_concel':'取消',
		'client_js_editClient_selectaddmember':'請選擇您要添加的成員！',
		'client_js_editClient_addsuc':'成功添加',
		'client_js_editClient_totalinfo':'條數據 ',
		'client_js_editClient_redata':'群組包含重複號碼數據:',
		'client_js_editClient_addmemberfalse':'添加成員失敗！',
		// unbindCli.js
		'client_js_unbindCli_noselectopt':'未選擇操作員！',
		'client_js_unbindCli_addoptpem':'確認添加該操作員的權限信息嗎？'
		
		
};