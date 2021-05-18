$(function () {
    const re = /^[0-9]+.?[0-9]*$/;
    const oneDay = 3600 * 1000 * 24;
    new Vue({
        el: "#container",
        data: {
            responseData: [],
            // 菜单变红
            menuFocus: false,
            // 人员树的数据结构
            map: {
                children: 'children',
                id: ['id'],
                label: ['label']
            },
            browserVersion: '',
            permissionType: 0,
            // 菜单项, 为 true 时显示相对应的菜单项, false 时则不显示( key 对应后台页面的相应搜索菜单)
            menuItem: {
                spType: true,
                busType: true,
                messageType: true,
                provinces: true,
                spisuncm: true,
                sendType: true,
                spuserId: true,
                org: true,
                user: true,
                staffname: true,
                mstype: true,
                paramStr: true,
                reportType: true
            },
            /**
             * 表单实体类， --> 后端 reportVo
             */
            request: {
                module: '',  // 模块名称
                page: {
                    pageIndex: 1, // 当前页数
                    totalRec: 0, //数据总数
                    pageSize: 15,   // 每页条数
                    pageSizeOpts: [5, 10, 15, 20, 50],   // 每页条数切换的配置
                    totalPage: 0, // 总页数
                },
                // 这个对象对应后台的 vo 类
                report: {
                    corpCode: '',
                    staffname: '',
                    reportType: 2, // 报表类型 default 日报表
                    provinces: [], // 省市信息 default 全部
                    mstype: '0',
                    orgId: "",
                    spisuncm: '', // 运营商 default 全部
                    sptype: 0,
                    sendtype: 0, // 发送类型 default 全部
                    spUserId: '', // sp 账号 default 全部
                    userIdStr: [],
                    queryTime: "",
                    containSubDep: true,
                    // 自定义参数
                    paramStr: '', // 自定义参数
                    param: '', //参数id之类的吧...
                    paramName: '', //参数名称
                    paramNum: '',  //我也不知道是啥
                    paramValue: '', //参数值
                    busCode: "",
                    spId: "-1",
                },
                details: false,
            },
            orgName: '请选择机构',
            operator: '请选择操作员',
            position: '',
            MessageType: [],
            /**
             * 复选框元素
             */
            isChecked: false,
            moreDimensionsData: [],
            allData: [],
            isIndeterminate: false,
            tmpIsIndeterminate: false,
            tmpCheckData: [],
            tmpCheckAll: false,
            checkAll: false,
            checkedData: [],
            defaultCheckedData: [], // 默认菜单项,也就是说,这个模块本来就显示的内容
            /**
             * 更多维度
             */
            isMoreDimensions: false,
            /**
             * 选择机构页面是否显示
             */
            selectOrg: false,
            orgTree: [],
            operatorTree: [], // 操作员树
            tmpTree: [],
            defaultProps: {},
            /**
             * 选择操作员页面是否显示
             */
            selectOperator: false,
            access: true,  // 这个变量是用来做权限控制的
            loadSucc: false, // 这个变量是确保所有元素加载完毕才显示页面的,提高用户体验--> 加载成功标识, 只有这个和 access 同时为true 页面才能展示;
            isDays: true,
            startTime: "",
            endTime: "",
            dateType: '',   // 日期控件展示类型 default daterange 可选类型 : [ month , year , daterange]
            datePlaceholder: '',
            simpleDateFormat: '',   // 日期控件时间格式--> 主要用于后端传递值需要的形式
            isSearch: true,      // 是否隐藏搜索框
            defaultDate: [],     // 默认的时间
            tableColumns: [],    // 表格的列项, 主要从 json 中配置
            tableData: [],        // 表格的数据, 主要从后台获取
            // 后缀带 label 的都是下拉框
            operatorLabel: [],  // 操作员下拉框
            reportTypeLabel: [],  //报表类型
            sendTypeLabel: [],  // 发送类型
            sendTypeLabelAll: [],  // 发送类型全部, 这个主要是个临时变量
            paramLabel: [],   // 自定义参数下拉框
            clickBack: false,  // 是否点击返回按钮
            isDetails: false,  // 是否查看详情
            loading: false,    // 页面交互的滚动条
            tmpData: [],  // 临时数据, 主要是查看详情的时候继续分页查询用的;
            tmpQueryPage: {}, // 查询页面的分页信息缓存, 当点击详情的时候, 将此分页信息缓存, 如果此时返回的时候, 将此分页信息带回去;
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
                shortcuts: [
                    {
                        text: '清空',
                        onClick(picker) {
                            picker.handleClear();
                        }
                    },
                    {
                        text: '昨天',
                        onClick(picker) {
                            let date = new Date();
                            date.setTime(picker.date.getTime() - oneDay);
                            picker.$emit('pick', date);
                        }
                    },
                    {
                        text: '一周前',
                        onClick(picker) {
                            let date = new Date();
                            date.setTime(picker.date.getTime() - oneDay * 7);
                            picker.$emit('pick', date);
                        }
                    },
                    {
                        text: '一月前',
                        onClick(picker) {
                            let date = new Date();
                            date.setTime(picker.date.getTime() - oneDay * 30);
                            picker.$emit('pick', date);
                        }
                    }
                ]
            },   // 日期控件专用
            pickerOptions2: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
                shortcuts: [
                    {
                        text: '清空',
                        onClick(picker) {
                            picker.handleClear();
                        }
                    }]
            }, // 日期控件专用
            emptyText: "请点击查询获取数据",
        },
        created: function () {
            let _this = this;
            // 获取模块
            _this.request.module = $("#module").val();
            _this.getJsonData();
            _this.getInitData();
        },
        watch: {
            // 更多维度数据过滤
            menuItem(val) {
                if (!val['provinces']) {
                    this.request.report.provinces = [];
                }
                if (!val['user']) {
                    this.operator = '请选择操作员';
                    this.request.report.userIdStr = [];
                }
                if (!val['org']) {
                    this.orgName = '请选择机构';
                    this.request.report.orgId = '';
                }
                if (!val['spuserId']) {
                    this.request.report.spUserId = '';
                }
                if (!val['spisuncm']) {
                    this.request.report.spisuncm = '';
                }
                if (!val['busType']) {
                    this.request.report.busCode = '';
                }
            },
            isDetails(val) {
                this.request.details = !!val;
            },
            checkedData(val) {
                this.isChecked = !!(val && val.length > 0);
            },
            startTime(val) {
                if (val) {
                    if (!re.test(val)) {
                        // 不是數字
                        this.request.report.queryTime = "[\"" + val.Format("yyyy-MM-dd") + "\",\"" + dateFormat(this.endTime) + "\"]";
                    } else {
                        // 是數字
                        this.request.report.queryTime = "[\"" + dateFormat(val) + "\",\"" + dateFormat(this.endTime) + "\"]";
                    }
                }
            },
            endTime(val) {
                if (val) {
                    if (!re.test(val)) {
                        this.request.report.queryTime = "[\"" + dateFormat(this.startTime) + "\",\"" + val.Format("yyyy-MM-dd") + "\"]";
                    } else {
                        this.request.report.queryTime = "[\"" + dateFormat(this.startTime) + "\",\"" + dateFormat(val) + "\"]";
                    }
                }
            }
        },
        //统一采用 _this 去 指向 this;
        methods: {
            getSummaries(param) {
                const { columns, data } = param;
                const sums = [];
                columns.forEach((column, index) => {
                    if (index === 0) {
                    // 标题
                    sums[index] = '合计';
                    return;
                }
                let flag = true;
                data.forEach(item => {
                    let value = item[column.property];
                if (value === '') {
                    flag = false;
                }
            });
                if (flag) {
                    const values = data.map(item => Number(item[column.property]));
                    if (!values.every(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr);
                        if (!isNaN(value)) {
                            return prev + curr;
                        } else {
                            return prev;
                        }
                    }, 0);
                    } else {
                        // 如果不是数字, 则不想加
                        sums[index] = '';
                    }
                }
            });

                return sums;
            },
            // 获取 json 配置文件中的数据
            getJsonData() {
                let _this = this;
                $.get('cxtj/reportform/config/report.json', function (reportJson) {
                    if (typeof(reportJson) === 'string') {
                        reportJson = JSON.parse(reportJson);
                    }
                    _this.reportTypeLabel = reportJson.label.reportTypeLabel;
                    _this.operatorLabel = reportJson.label.operatorLabel;
                    _this.MessageType = reportJson.label.MessageType;
                    _this.defaultProps = reportJson.defaultProps;
                    _this.tableColumns = getTableColumns(reportJson.tableColumns, _this.request.module);
                    _this.menuItem = getMenuItem(reportJson.menuItem, _this.request.module); // 获取应该展示的菜单项
                    _this.moreDimensionsData = reportJson.moreDimensions;
                    _this.allData = reportJson.allData;
                    _this.defaultCheckedData = getDefaultCheckedData(_this.menuItem);
                    _this.loadSucc = true;
                });
            },
            // 获取初始化参数的方法
            getInitData() {
                let _this = this;
                // 请求无论任何模块都是一样的, 所以这里可以不限定基础路径
                $.ajax({
                    type: "POST",
                    url: 'rep_areaReport.htm',
                    data: "method=getInitData&module=" + _this.request.module,
                    dataType: 'json',
                    async: false,
                    success:function(result) {
                        if ("no_service" !== result) {
                            let json = result;
                            _this.responseData = result;
                            _this.menuCode = json['menuCode'];
                            _this.position = json['position'];
                            _this.access = json['access'];
                            _this.sendTypeLabel = json['sendTypeList'];
                            // 备份数据
                            _this.sendTypeLabelAll = _this.sendTypeLabel;
                            _this.orgTree = json['depTreeInfo'];
                            _this.operatorTree = json['userTreeInfo'];
                            // 设置模块名称
                            _this.request.module = json['module'];
                            _this.request.report.corpCode = json['corpCode'];
                            _this.permissionType = json['permissionType'];
                            if (_this.permissionType === 1 && _this.request.module === 'sysDepReport') {
                                _this.access = false;
                            }
                            // 初始化时间控件
                            _this.setDefaultTime();
                            // 获取自定义参数
                            let paramList = json['paramList'];
                            _this.initParamLabel(paramList);
                            // 操作员树转化
                            _this.operatorTree = convertTree(_this.operatorTree, _this.map);
                            // 判断浏览器版本, 做一定的兼容
                            _this.browserVersion = browserData();
                        } else {
                            _this.$message.error('加载页面失败....请重新刷新页面');
                        }
                    },
                    error:function() {
                        _this.$message.error('很抱歉,连接异常!');
                    }
                });
            },
            // 进行查询的方法
            getQuery() {
                let _this = this;
                // 当报表类型为空时,不允许查询
                if (_this.request.module === 'dynParamReport' && _this.request.report.paramStr === '') {
                    _this.$message.error('很抱歉,报表类型不允许为空!');
                    return;
                }
                _this.loading = true;
                _this.emptyText = "暂无数据";
                // 判断是不是详情页面返回
                if (_this.clickBack) {
                    _this.request.page = _this.tmpQueryPage;
                    _this.clickBack = false;
                }
                $.ajax({
                    type: "POST",
                    url: "rep_areaReport.htm?method=query",
                    data: JSON.stringify(_this.request),
                    dataType: 'json',
                    contentType: "application/json",
                    success:function(result) {
                        if ("no_service" !== result) {
                            _this.loading = false;
                            _this.tableData = result['reportList'];  // 查询的数据
                            _this.request.page.totalRec = result['totalRec'];   // 总查询记录
                        } else {
                            _this.$message.error('很抱歉,查询失败....');
                        }
                    },
                    error:function() {
                        _this.$message.error('很抱歉,连接异常!');
                    }
                });
            },
            // 获取详情
            getDetails(index, row) {
                let _this = this;
                _this.emptyText = "暂无数据";
                // 第一次点击详情查询的时候, 我们将分页信息给备份一遍;
                if (index !== -1) {
                    _this.tmpQueryPage = {
                        pageIndex: _this.request.page.pageIndex, // 当前页数
                        totalRec: _this.request.page.totalRec, //数据总数
                        pageSize: _this.request.page.pageSize,   // 每页条数
                        pageSizeOpts: [5, 10, 15, 20, 50],   // 每页条数切换的配置
                        totalPage: 0, // 总页数
                    };
                    _this.request.page = {
                        pageIndex: 1, // 当前页数
                        totalRec: 0, //数据总数
                        pageSize: 15,   // 每页条数
                        pageSizeOpts: [5, 10, 15, 20, 50],   // 每页条数切换的配置
                    };
                }
                let data = {
                    module: _this.request.module,
                    report: row,
                    page: _this.request.page
                };
                // 存放临时的缓存
                _this.tmpData = row;
                _this.loading = true;
                $.ajax({
                    type: "POST",
                    url: 'rep_areaReport.htm?method=details',
                    data: JSON.stringify(data),
                    dataType: 'json',
                    contentType: "application/json",
                    success: function(result) {
                        if ("no_service" !== result) {
                            _this.isDetails = true;  // 隐藏查询条件框
                            _this.isSearch = false;
                            _this.tableData = result['reportList'];
                            _this.request.page.totalRec = result['totalRec'];
                        } else {
                            _this.$message.error('很抱歉,查询失败....');
                        }
                        _this.loading = false;
                    },
                    error:function() {
                        _this.loading = false;
                        _this.$message.error('很抱歉,连接异常!');
                    }
                });
            },
            // 导出
            downloadFile() {
                let _this = this;
                _this.$confirm('确定要导出数据到excel?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (_this.tableData.length === 0) {
                    _this.$message({
                        message: '无数据可导出',
                        type: 'warning'
                    });
                    return;
                }
                if (_this.request.page.totalRec > 500000) {
                    _this.$message.error('很抱歉, 导出数据不得超过50万');
                    return;
                }
                let data = {};
                // 传送不同的对象
                if (_this.request.details) {
                    data = {
                        module: _this.request.module,
                        details: _this.request.details,
                        report: _this.tmpData
                    };
                } else {
                    data = {
                        module: _this.request.module,
                        details: _this.request.details,
                        report: _this.request.report
                    };
                }
                $.ajax({
                    type: "POST",
                    url: 'rep_areaReport.htm?method=downloadFile',
                    data: JSON.stringify(data),
                    dataType: 'json',
                    contentType: "application/json",
                    success: function(result) {
                        if ("fail" === result) {
                            _this.$message.error('很抱歉,导出失败....');
                        } else if ("oversize" === result) {
                            _this.$message.error('导出数量超出最大限制....');
                        } else if ("error" === result) {
                            _this.$message.error('很抱歉,导出错误....');
                        } else {
                            let filePath = result['filePath'];
                            let fileName = result['fileName'];
                            let fileUrl = "rep_areaReport.htm?method=download";
                            let path = fileUrl + "&filePath=" + filePath + "&fileName=" + fileName;
                            download_href(encodeURI(path));
                            _this.$message.success('导出成功!');
                        }
                    },
                    error:function() {
                        _this.$message.error('很抱歉,连接异常!');
                    }
                });
            });
            },
            // 懒加载机构树
            loadDeptNode(node, resolve) {
                let _this = this;
                if (node.level === 0) {
                    return resolve(_this.orgTree);
                }
                let code = node.key;
                _this.getTree(code, "dept");
                setTimeout(() => {
                    if (_this.tmpTree) {
                    return resolve(_this.tmpTree);
                } else {
                    return resolve([]);
                }
            }, 500);
            },
            // 懒加载人员树
            loadUserNode(node, resolve) {
                let _this = this;
                if (node.level === 0) {
                    return resolve(_this.operatorTree);
                }
                let code = node.key;
                if (code.indexOf("mem") !== -1) {
                    return resolve([]);
                }
                _this.getTree(code, "user");
                // 对操作员数据进行一下转化
                setTimeout(() => {
                    if (_this.tmpTree) {
                    return resolve(_this.tmpTree);
                } else {
                    return resolve([]);
                }
            }, 500);
            },
            getTree(code, operation) {
                let _this = this;
                $.ajax({
                    type: "POST",
                    url: 'rep_areaReport.htm?method=getTree&operation=' + operation + '&code=' + code,
                    dataType: 'json',
                    contentType: "application/json",
                    success: function(result) {
                        if ("no_service" !== result) {
                            if (operation === 'user') {
                                // 操作员树转化
                                _this.tmpTree = convertTree(result, _this.map);
                            } else {
                                _this.tmpTree = result;
                            }
                        } else {
                            _this.tmpTree = [];
                            _this.$message.error('很抱歉,加载失败....');
                        }
                    },
                    error:function() {
                        _this.tmpTree = [];
                        _this.$message.error('很抱歉,连接异常!');
                    }
                });
            },
            // 点击返回按钮的时候, 将按钮隐藏, 然后搜索框显示, 再进行一次查询
            isBack() {
                this.isDetails = false;
                this.isSearch = true;
                this.clickBack = true;
                this.getQuery();  // 默认重新查询一遍
            },
            changeDate() {
                let _this = this;
                _this.defaultDate = [];
                _this.request.report.queryTime = '';
                _this.startTime = '';
                _this.endTime = '';
                _this.dateType = '';
                if (1 === _this.request.report.reportType) {
                    _this.isDays = false;
                    _this.dateType = 'year';
                    _this.datePlaceholder = '  请选择年';
                    _this.simpleDateFormat = 'yyyy';
                    _this.request.report.queryTime = new Date().Format("yyyy");  // 设置默认时间格式
                } else if (0 === _this.request.report.reportType) {
                    _this.isDays = false;
                    _this.dateType = "month";
                    _this.datePlaceholder = '  请选择月';
                    _this.simpleDateFormat = 'yyyy-MM';
                    _this.request.report.queryTime = new Date().Format("yyyy-MM");  // 设置默认时间格式
                } else {
                    _this.isDays = true;
                    _this.setDefaultTime();  // 设置默认的时间
                }
            },
            setDefaultTime() {
                let date = new Date();
                this.endTime = date.getTime();
                this.startTime = new Date();
                this.startTime.setTime(this.startTime.getTime() - oneDay * 30);
                this.request.report.queryTime = "[\"" + dateFormat(this.startTime) + "\",\"" + dateFormat(this.endTime) + "\"]";
            },
            // 修改短信类型
            changeMsType() {
                // 置空运营商 ID 属性, 当然仅仅在运营商查询报表的时候有用;
                if (this.request.module === 'spisuncmMtReport') {
                    this.request.report.spId = '-1';
                }
                this.request.report.spUserId = '';
            },
            // 修改发送类型
            changeSendType() {
                let type = this.request.report.sptype;
                this.request.report.sendtype = 0; // 置空属性
                let array = [];
                for (let i = type; i < (1 === type ? 2 : this.sendTypeLabelAll.length); i++) {
                    array.push(this.sendTypeLabelAll[i]);
                }
                this.sendTypeLabel = array;
            },
            // 更改分页
            handlePage(value) {
                this.request.page.pageIndex = value;
                this.isDetails ? this.getDetails(-1, this.tmpData) : this.getQuery();
            },
            handlePageSize(value) {
                this.request.page.pageSize = value;
                this.isDetails ? this.getDetails(-1, this.tmpData) : this.getQuery();
            },
            // 确认选择机构
            confirmOrg() {
                let checkNode = this.$refs.tree.getCurrentNode();
                this.request.report.orgId = checkNode.id;
                this.orgName = checkNode.label;
                this.selectOrg = !this.selectOrg;
            },
            // 清空
            cancelOrg() {
                this.$refs.tree.setCheckedNodes([]);
                this.request.report.orgId = "";
                this.orgName = "请选择机构";
                this.selectOrg = !this.selectOrg;
            },
            // 确认选择操作员
            confirmOperator() {
                let checkNodes = this.$refs.operatorTree.getCheckedNodes();
                this.request.report.userIdStr = this.$refs.operatorTree.getCheckedKeys();
                this.operator = "";
                if (checkNodes) {
                    checkNodes.forEach((item, index) => {
                        this.operator += item['label'];
                    this.operator += (index !== checkNodes.length - 1 ? "," : "");
                });
                }
                // 防止置空数据
                this.operator = this.operator !== "" ? this.operator : "请选择操作员";
                this.selectOperator = !this.selectOperator;
            },
            // 清空操作员
            cancelOperator() {
                this.$refs.operatorTree.setCheckedNodes([]);
                this.request.report.userIdStr = [];
                this.operator = "请选择操作员";
                this.selectOperator = !this.selectOperator;
            },
            // 取消更多维度
            cancelMoreDimensions() {
                this.checkedData = this.tmpCheckData;
                this.isIndeterminate = this.tmpIsIndeterminate;
                this.checkAll = this.tmpCheckAll;
                this.isMoreDimensions = false;
            },
            // 更多维度
            moreDimensions() {
                let _this = this;
                if ((_this.checkedData.length + _this.defaultCheckedData.length) === _this.allData.length) {
                    _this.checkAll = true;
                }
                _this.menuItem = getMoreMenuItem(_this.checkedData, _this.menuItem, _this.defaultCheckedData);
                _this.isMoreDimensions = !_this.isMoreDimensions;
                _this.menuFocus = true;
                setTimeout(function () {
                    _this.menuFocus = false;
                }, 1500);
                _this.tmpCheckData = _this.checkedData;
                _this.tmpIsIndeterminate = _this.isIndeterminate;
                _this.tmpCheckAll = _this.checkAll;
            },
            handleCheckAllChange(val) {
                let tmp = [];
                if (val && this.defaultCheckedData) {
                    this.allData.forEach(item => {
                        if (this.defaultCheckedData.indexOf(item) === -1) {
                        tmp.push(item);
                    }
                });
                }
                this.checkedData = tmp;
                this.isIndeterminate = false;
            },
            handleCheckedData(value) {
                this.isIndeterminate = value.length > 0 && (value.length + this.defaultCheckedData.length) !== this.allData.length;
                this.checkAll = (value.length + this.defaultCheckedData.length) === this.allData.length;
            },
            // 自定义参数表列变化
            paramColumn() {
                let paramStr = this.request.report.paramStr;
                if (paramStr) {
                    let data = paramStr.split(",");
                    this.request.report.param = data[0];
                    this.request.report.paramNum = data[1];
                    this.request.report.paramName = data[2];
                }
            },
            changeStartTime(value) {
                if (value) {
                    if ('' === this.endTime) {
                        this.startTime = value;
                    } else if (value.getTime() > this.endTime) {
                        this.startTime = this.endTime;
                        this.$Message.error("开始日期必须小于等于结束日期");
                    }
                }
            },
            changeEndTime(value) {
                if (value) {
                    if ('' === this.startTime) {
                        this.endTime = value;
                    } else if (value.getTime() < this.startTime) {
                        this.endTime = this.startTime;
                        this.$Message.error("结束日期必须大于等于开始日期");
                    }
                }
            },
            // 自定义参数的下拉框初始化
            initParamLabel(paramList) {
                if (paramList && paramList.length > 0) {
                    paramList.forEach(item => {
                        let tmp = {
                            label: item['paramSubName'],
                            value: item['param'] + ',' + item['paramSubNum'] + ',' + item['paramSubName']
                        };
                    this.paramLabel.push(tmp);
                });
                    let tmp = paramList[0];
                    // 默认选中第一项, 防止空
                    this.request.report['paramStr'] = tmp['param'] + ',' + tmp['paramSubNum'] + ',' + tmp['paramSubName'];
                    this.request.report['paramName'] = tmp['paramSubName'];
                    this.request.report['paramNum'] = tmp['paramSubNum'];
                    this.request.report['param'] = tmp['param'];
                }
            },
        },
    })
    ;
});


