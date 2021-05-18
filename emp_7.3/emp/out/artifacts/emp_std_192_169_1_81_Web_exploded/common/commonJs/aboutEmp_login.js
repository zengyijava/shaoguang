var zh_HK_Languages={
    "ver_num_p":"Version number:","version_a":"Version details","DB_script":"•Database script","Copyright_company":"Copyright company:",
    "montnets":"ShenZhen Montnets Technology Development Co.,Ltd.","service_tel":"Service tel:","validity_date":"Period of validity:",
    "status":"Certification status:","speed":"Speed of transmission:","Upgrade_date":"Upgrade date","Version_features":"Version features",
    "ver_num":"Version number",
    "releaseNote_1":"1、[Communications management] - [SMS & MMS SP account] Add a new billing type. \n2、[Communications management] Add \"SP account top-Up/recovery\" and \"Top-up log view\".\n3、SMS sending supports SP account billing function.\n4、The gateway supports newly 64 bits.",
    "releaseNote_2":"1、EMP access required to support international standard SMPP3.4 interface, including client side access and connecting the operator side;\n2、Support that mobile phone up instruction join global blacklist and switch settings;\n3、 File cache optimization;\n4、Limit to the frequency of calling interface;\n5、Installation and deployment for non-root users under LINUX system;\n6、LINUX deployment tools optimization and add process Daemons.",
    "releaseNote_3":"1、EMP multilingual version support"
};
var zh_TW_Languages={
    "ver_num_p":"版本號：","version_a":"版本詳情","DB_script":"&bull;數據庫腳本：","Copyright_company":"版權公司：",
    "montnets":"深圳市夢網科技發展有限公司","service_tel":"客服電話：","validity_date":"有效期：",
    "status":"認證狀態：","speed":"發送速度：","Upgrade_date":"升級時間","Version_features":"版本功能",
    "ver_num":"版本號",
    "releaseNote_1":"1、[通信管理]－[短彩SP賬號]新增計費類型。\n2、[通信管理]新增“SP賬號充值/回收”及“充值日誌查看”。\n3、短信發送支持SP賬號計費功能。\n4、網關新增支持64位。",
    "releaseNote_2":"1、EMP接入需支持SMPP3.4國際標準接口，包括客戶側接入和連接運營商側；\n2、支持手機上行指令加入全局黑名單，同時提供開關設置；\n3、文件緩存優化；\n4、查詢接口調用頻率限制；\n5、LINUX系統下使用非root用戶安裝部署；\n6、LINUX部署工具優化及增加進程守護。",
    "releaseNote_3":"1、EMP多語言版本支持"
};
var source = $("#langName").val()+"_Languages";

document.getElementById("ver_num").textContent=eval(source)["ver_num"];
document.getElementById("ver_num_p").textContent=eval(source)["ver_num_p"];
document.getElementById("version_a").textContent=eval(source)["version_a"];
document.getElementById("version_a1").textContent=eval(source)["version_a"];
document.getElementById("DB_script").textContent=eval(source)["DB_script"];
document.getElementById("Copyright_company").textContent=eval(source)["Copyright_company"];
document.getElementById("montnets").textContent=eval(source)["montnets"];
document.getElementById("service_tel").textContent=eval(source)["service_tel"];
document.getElementById("validity_date").textContent=eval(source)["validity_date"];
document.getElementById("status").textContent=eval(source)["status"];
document.getElementById("speed").textContent=eval(source)["speed"];
document.getElementById("Upgrade_date").textContent=eval(source)["Upgrade_date"];
document.getElementById("Version_features").textContent=eval(source)["Version_features"];


$(document).ready(function() {
    $(".releaseNote").each(function () {
        $(this).css("white-space","pre-wrap");
        var str = $(this).text();
        str = str.replace(/1、[通信管理].*/,eval(source)["releaseNote_1"]).
        replace(/1、EMP接入.*/,eval(source)["releaseNote_2"]).
        replace(/1、EMP多语言版本支持/,eval(source)["releaseNote_3"]);
        $(this).text(str);
    });
});







