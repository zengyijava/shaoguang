(function () {
  // 模拟json数据
  var context = {
    "music": {
      "loop": true,
      "filename": "飞儿乐团 - Light up the way",
      "active": true,
      "autoPlay": true,
      "src": "http://192.169.6.157/web/upload/20181115/1542260746774.mp3",
      "reused": true
    },
    "pages": [
      {
        "background": {
          "color": "#fff",
          "src": "",
          "crop": {
            "w": 0,
            "h": 0,
            "left": 0,
            "top": 0,
            "url": "",
            "style": {}
          },
          "transparency": 0
        },
        "elements": [
          {
            "locked": false,
            "visible": true,
            "x": 110,
            "y": 38,
            "z": 0,
            "w": 100,
            "h": 32,
            "type": "button",
            "tag": "按钮1",
            "text": "Tel",
            "action": "6",
            "tabsAction": "6",
            "style": {
              "fontSize": "14px",
              "textAlign": "center",
              "color": "#ffffff",
              "backgroundColor": "#2e95ff",
              "borderRadius": 0
            },
            "tel": "13424282150"
          },
          {
            "locked": false,
            "visible": true,
            "x": 110,
            "y": 107,
            "z": 0,
            "w": 100,
            "h": 32,
            "type": "button",
            "tag": "按钮2",
            "text": "Web",
            "action": "2",
            "tabsAction": "2",
            "style": {
              "fontSize": "14px",
              "textAlign": "center",
              "color": "#ffffff",
              "backgroundColor": "#2e95ff",
              "borderRadius": 0
            },
            "webURL": "http://163.com"
          },
          {
            "locked": false,
            "visible": true,
            "x": 110,
            "y": 179,
            "z": 0,
            "w": 100,
            "h": 32,
            "type": "button",
            "tag": "按钮3",
            "text": "App",
            "action": "4",
            "tabsAction": "2",
            "style": {
              "fontSize": "14px",
              "textAlign": "center",
              "color": "#ffffff",
              "backgroundColor": "#2e95ff",
              "borderRadius": 0
            },
            "apkLink": "zhihu://questions/53799426",
            "apkDownloadURL": "https://mobile.baidu.com/item?docid=25300225",
            "ipaLink": "zhihu://oia.zhihu.com/questions/53799426",
            "ipaDownloadURL": "https://itunes.apple.com/cn/app/zhi-hu/id432274380"
          },
          {
            "locked": false,
            "visible": true,
            "x": 110,
            "y": 260,
            "z": 0,
            "w": 100,
            "h": 32,
            "type": "button",
            "tag": "按钮4",
            "text": "Quick",
            "action": "7",
            "tabsAction": "2",
            "style": {
              "fontSize": "14px",
              "textAlign": "center",
              "color": "#ffffff",
              "backgroundColor": "#2e95ff",
              "borderRadius": 0
            },
            "quickAppURL": "hap://app/com.riafan.demo"
          },
          {
            "locked": false,
            "visible": true,
            "x": 110,
            "y": 346,
            "z": 0,
            "w": 100,
            "h": 32,
            "type": "button",
            "tag": "按钮5",
            "text": "Map",
            "action": "5",
            "tabsAction": "2",
            "style": {
              "fontSize": "14px",
              "textAlign": "center",
              "color": "#ffffff",
              "backgroundColor": "#2e95ff",
              "borderRadius": 0
            },
            "lat": 22.615108,
            "lon": 114.035529,
            "title": "深圳北站",
            "addr": "广东省深圳市致远中路28号"
          }, {
            "visible": true,
            "tag": "背景音乐",
            "locked": false,
            "type": "music"
          }
        ]
      }, {
      "background": {
        "transparency": 0,
        "color": "",
        "src": "http://192.169.6.157/web/upload/20181025/1540446712058.jpg",
        "crop": {
          "w": 190,
          "style": {
            "transform": "scale(0.5066666666666667,0.5066666666666667) translate3d(-515.1315789473683px,-294.07894736842104px,0)rotateZ(0deg)",
            "height": "600px",
            "width": "600px"
          },
          "left": 113,
          "h": 303,
          "url": "blob:http://localhost:8080/199675f1-2bf7-4c12-96f4-78fb3158397e",
          "top": 1
        }
      },
      "elements": [{
        "text": "文本：{#参数1#}{#参数2#}",
        "tag": "文本1",
        "type": "text",
        "w": 300,
        "style": {
          "fontWeight": "normal",
          "color": "#e0e0e0",
          "textAlign": "left",
          "fontSize": "14px"
        },
        "z": 0,
        "y": 20,
        "x": 10
      }, {
        "fistFramePath": "http://192.169.6.157/web/upload/20181114/15421660673951.png",
        "tag": "视频1",
        "type": "video",
        "h": 135,
        "w": 240,
        "duration": 10000,
        "src": "http://192.169.6.157/web/upload/20181114/1542166067395.mp4",
        "z": 2,
        "y": 90,
        "x": 43
      }, {
        "duration": 238.994286,
        "filename": "曾惜 - 讲真的 [mqms2]",
        "h": 90,
        "locked": false,
        "src": "http://192.169.6.157/web/upload/20181026/1540525241668.mp3",
        "style": {
          "color": "#ffffff",
          "backgroundColor": "#2196f3",
          "transparency": 0.44
        },
        "tag": "音频1",
        "title": "曾惜 - 讲真的 [mqms2]",
        "type": "audio",
        "visible": true,
        "w": 290,
        "x": 11,
        "y": 207,
        "z": 4
      }, {
        "duration": 56.26775,
        "filename": "飞儿乐团 - Light up the way",
        "h": 90,
        "locked": false,
        "src": "http://192.169.6.157/web/upload/20181115/1542260746774.mp3",
        "style": {
          "color": "#ffffff",
          "backgroundColor": "#2196f3",
          "transparency": 0.44
        },
        "tag": "音频1",
        "title": "飞儿乐团 - Light up the way",
        "type": "audio",
        "visible": true,
        "w": 290,
        "x": 11,
        "y": 50,
        "z": 4
      }, {
        "visible": false,
        "type": "music"
      }]
    }, {
      "background": {
        "transparency": 0,
        "color": "",
        "src": "http://192.169.6.157/web/upload/20181025/1540433118820.jpg",
        "crop": {
          "w": 190,
          "style": {
            "transform": "scale(0.5066666666666667,0.5066666666666667) translate3d(-294.07894736842104px,-294.07894736842104px,0)rotateZ(0deg)",
            "height": "600px",
            "width": "600px"
          },
          "left": 1,
          "h": 303,
          "url": "blob:http://localhost:8081/dc54c811-5770-43d6-a667-2ec340975354",
          "top": 1
        }
      },
      "elements": [{
        "w": 298,
        "text": "文本：{#参数3#}{#参数4#}",
        "style": {
          "fontWeight": "normal",
          "color": "#e0e0e0",
          "backgroundColor": "",
          "textAlign": "left",
          "fontSize": "14px"
        },
        "tag": "文本2",
        "type": "text",
        "z": 0,
        "y": 20,
        "x": 10
      }, {
        "visible": false,
        "type": "music"
      }]
    }],
    "swiper": {
      "effect": "cube",
      "direction": "vertical",
      "loop": true,
      "autoPlay": false,
      "pageAlign": "right"
    }
  }
  // 预编译模板
  var html = template('tpl', context);
  // 输入模板
  document.getElementById('wrap').innerHTML = html;
})();
