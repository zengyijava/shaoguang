export default {
  obj2List: obj => {
    let list = []
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        obj[key].forEach(element => {
          list.push(element)
        })
      }
    }
    return list
  },

  /*
  ** @fn getUrlParameters获取附加参数
  ** @params {string} paramName参数名
  ** @params {Boolean} decode是否decode
  ** @params {sting} from属性名，url地址栏，iframe父级的iframe窗口
  */
  getUrlParameters: (paramName, decode, from) => {
    let currLocation
    let _from = from || 'url'

    // 判断来源
    if (_from === 'url') {
      currLocation = window.location.search.split('?')[1]
    } else if (_from === 'iframe') {
      currLocation = self.frameElement.getAttribute('data-param')
    }

    if (!currLocation) {
      return ''
    } else {
      let parArr
      let parr
      let returnVal = true
      let valStr = currLocation.indexOf('&')

      if (valStr > 1) {
        parArr = currLocation.split('&')
      } else {
        parArr = currLocation.split(' ')
      }
      for (let i = 0; i < parArr.length; i++) {
        parr = parArr[i].split('=')
        if (parr[0] === paramName) {
          returnVal = true
          return decode ? decodeURIComponent(parr[1]) : parr[1]
        } else {
          returnVal = false
        }
      }

      if (!returnVal) {
        return ''
      }
    }
  },

  /**
   * x轴方向平均布局元素
   * @param elements
   * @param w
   * @param left
   */
  spaceBetween (elements, w = 284) {
    elements.forEach((element, i) => {
      const len = elements.length
      // const gap = left * (len - 1)
      element.w = Math.round(w / len)
      element.x = element.w * i
    })
  },

  /**
   * transformRequest
   * @param data
   * @returns {formdata}
   */
  // 发送请求前处理request的数据
  transformRequest (data) {
    // Do whatever you want to transform the data
    let newData = ''
    for (let k in data) {
      newData += encodeURIComponent(k) + '=' + encodeURIComponent(data[k]) + '&'
    }
    return newData
  },

  /**
   * 返回本地时间
   * @returns {time}
   */
  getCurrentTime () {
    let nowtime = new Date()
    let currentTime = (nowtime.getMonth() + 1) + '月' + nowtime.getDate() + '日 ' + nowtime.getHours() + ':' + nowtime.getMinutes()
    return currentTime
  },

  /*
  ** @fn computeStringSize文字转为字节
  ** @params {String} str字节
  */
  computeStringSize (str) {
    let totalLength = 0
    let i
    let charCode

    for (i = 0; i < str.length; i++) {
      charCode = str.charCodeAt(i)

      if (charCode < 0x007f) {
        totalLength = totalLength + 1
      } else if ((charCode >= 0x0080) && (charCode <= 0x07ff)) {
        totalLength += 2
      } else if ((charCode >= 0x0800) && (charCode <= 0xffff)) {
        totalLength += 3
      } else {
        totalLength += 4
      }
    }
    return totalLength
  },

  /**
   * 自适应计算宽度
   * @param width
   * @param height
   * @returns {number}
   */
  getFitWidth ({width, height}) {
    const MAX_W = 240
    const MAX_H = 240
    const ratio = Math.round(width / height * 100) / 100
    return ratio > 1 ? MAX_W : Math.round(MAX_H * ratio)
  },

  /**
   * 自适应计算高度
   * @param width
   * @param height
   * @returns {number}
   */
  getFitHeight ({width, height}) {
    const MAX_W = 240
    const MAX_H = 240
    const ratio = Math.round(width / height * 100) / 100
    return ratio > 1 ? Math.round(MAX_W / ratio) : MAX_H
  },

  /**
   * 确认框
   * @param context
   * @param message
   * @returns {Promise<MessageBoxData> | *}
   */
  confirm (context, message) {
    return context.$confirm(message, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  },
  /**
   * 获取粘贴时的纯文本
   * @param e
   * @returns {string}
   */
  getPlainText (e) {
    let innerText = ''
    e.preventDefault()
    if (e.clipboardData) {
      innerText = (e.originalEvent || e).clipboardData.getData('text/plain').replace(/\r\n/g, '')
      document.execCommand('insertText', false, innerText)
    } else if (window.clipboardData) {
      innerText = window.clipboardData.getData('Text').replace(/\r\n/g, '')
      if (document.selection) {
        document.selection.createRange().pasteHTML(innerText)
      } else if (window.getSelection) {
        const sel = window.getSelection()
        const range = sel.getRangeAt(0)

        // 创建临时元素，使得TextRange可以移动到正确的位置
        const tempEl = document.createElement('span')
        tempEl.innerHTML = '&#FEFF;'
        range.deleteContents()
        range.insertNode(tempEl)
        const textRange = document.body.createTextRange()
        textRange.moveToElementText(tempEl)
        tempEl.parentNode.removeChild(tempEl)
        textRange.text = innerText
        textRange.collapse(false)
        textRange.select()
      }
    }
    return innerText
  },

  /**
   * 深度复制
   * @param val
   * @returns {any}
   */
  deepClone (val) {
    return JSON.parse(JSON.stringify(val))
  },
  /**
   * 获取最大值参数名称
   * @param params
   * @returns {string}
   */
  getMaxName (params) {
    if (params === undefined || params.length === 0) {
      return '参数1'
    }
    const arr = params.map(item => item.name.substring(2))
    return '参数' + (Math.max(...arr) + 1)
  },

  /**
   * 插入html
   * @param html
   * @param doc
   */
  insertHtml (html, doc = document) {
    let sel, range
    if (doc.getSelection) {
      // IE9 and non-IE
      sel = doc.getSelection()
      if (sel.getRangeAt && sel.rangeCount) {
        range = sel.getRangeAt(0)
        range.deleteContents()

        // Range.createContextualFragment() would be useful here but is
        // only relatively recently standardized and is not supported in
        // some browsers (IE9, for one)
        const el = doc.createElement('span')
        el.innerHTML = html
        let frag, node, lastNode
        frag = doc.createDocumentFragment()
        while ((node = el.firstChild)) {
          lastNode = frag.appendChild(node)
        }
        range.insertNode(frag);

        // Preserve the selection
        if (lastNode) {
          range = range.cloneRange()
          range.setStartAfter(lastNode)
          range.collapse(true)
          sel.removeAllRanges()
          sel.addRange(range)
        }
      }
    } else if ((sel = doc.selection) && sel.type !== 'Control') {
      // IE < 9
      const originalRange = sel.createRange()
      originalRange.collapse(true)
      sel.createRange().pasteHTML(html)
      range = sel.createRange()
      range.setEndPoint('StartToStart', originalRange)
      range.select()
    }
  },

  /**
   * 检测是否当前节点或包含当前节点
   * @param ancestor
   * @param descendant
   * @returns {boolean}
   */
  isOrContainsNode (ancestor, descendant) {
    let node = descendant;
    while (node) {
      if (node === ancestor) {
        return true;
      }
      node = node.parentNode;
    }
    return false;
  },

  /**
   * 跨选区插入节点
   * @param node
   * @param containerNode
   * @param doc
   */
  insertNodeOverSelection (node, containerNode, doc = document) {
    let sel, range, html;
    if (doc.getSelection) {
      sel = doc.getSelection();
      if (sel.getRangeAt && sel.rangeCount) {
        range = sel.getRangeAt(0);
        if (this.isOrContainsNode(containerNode, range.commonAncestorContainer)) {
          range.deleteContents();
          range.insertNode(node);
        } else {
          containerNode.appendChild(node);
        }
        range.setStartAfter(node);
      }
    } else if ((sel = doc.selection) && sel.type !== 'Control') {
      range = sel.createRange();
      if (this.isOrContainsNode(containerNode, range.parentElement())) {
        html = (node.nodeType === 3) ? node.data : node.outerHTML;
        range.pasteHTML(html);
      } else {
        containerNode.appendChild(node);
      }
    }
  },
  /**
   * 参数值转参数名
   * @param value
   * @returns {*}
   */
  paramValue2Name (value) {
    return value.replace('{#', '').replace('#}', '')
  },

  /**
   * 从参数获取参数值
   * @param param
   * @returns {*}
   */
  param2Value (param) {
    return '{#' + param.name + '#}'
  },

  /**
   * 去掉placeholder
   * @param vm
   * @param selector
   * @returns {Element}
   */
  removePlaceholder (vm, selector) {
    // 多个组件实例时根据节点选择当前的富文本组件
    let richText = vm.$el.querySelector(selector)
    if (richText.innerHTML === vm.placeholder) {
      richText.innerText = ''
    }
    return richText
  },

  /**
   * 重新渲染
   * @param element
   * @param richText
   */
  reRender (element, richText) {
    element.text = richText.innerHTML
    element.h = Math.ceil(richText.getBoundingClientRect().height)
  },

  /**
   * 获取要保留的参数值
   * @param textStr
   * @param params
   * @returns {Array}
   */
  getIntersectParams (textStr, params) {
    let values = textStr.match(/{#参数\d+#}/g) || []
    // 差集
    return [...params].filter(param => values.find(value => this.paramValue2Name(value) === param.name))
  },

  /**
   * 获取要删除的参数值
   * @param newValue
   * @param oldValue
   * @returns {Array}
   */
  getDifferentParams (newValue, oldValue) {
    const reg = /{#参数\d+#}/g
    const oldValues = oldValue.match(reg) || []
    const newValues = newValue.match(reg) || []
    // 差集
    return [...oldValues].filter(vlaue => !newValues.find(item => item === vlaue))
  },
  /**
   * 同步删除短信参数
   * @param values
   * @param vm
   */
  removeMsgParam (values, vm) {
    const reg = /{#参数\d+#}/g
    let msgParamValues = vm.msgText.match(reg) || []
    msgParamValues.forEach(item => {
      if (values.find(value => value === item)) {
        const str = vm.msgText.replace(item, '')
        // 重新渲染
        vm.updateMessage(str)
      }
    })
  }
}
