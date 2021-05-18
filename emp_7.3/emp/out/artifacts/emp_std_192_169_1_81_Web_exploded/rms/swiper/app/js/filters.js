template.defaults.imports.imgStyle = function(background) {
  var style = background.crop.style;
  return 'width: ' + style.width +
    '; height: ' + style.height +
    '; transform: ' + style.transform;
};

/**
 * 获取content样式
 * @param element
 * @param options
 * @returns {string}
 */
template.defaults.imports.contentStyle = function(element) {
  var str = '';
  for (var p in element.style) {
    // 驼峰转连字符
    var k = p.replace(/([A-Z])/g, function($char) {
      return '-' + $char.toLocaleLowerCase();
    });
    if (element.style[p]) {
      str += k + ': ' + element.style[p] + '; '
    }
  }
  if (element.z) {
    str += 'z-index: ' + element.z + '; '
  }
  if (element.x) {
    str += 'left: ' + element.x + 'px; '
  }
  if (element.y) {
    str += 'top: ' + element.y + 'px; '
  }
  return str
}

/**
 * 将秒数转时分秒并补0
 * @param element
 * @returns {string}
 */
template.defaults.imports.formatTime = function(element) {
  var sec = parseInt(element.duration)
  return formatTime(sec)
}


/**
 * 格式化时间
 * @param sec
 * @returns {string}
 */
function formatTime (sec) {
  var result = '',
  seconds = parseInt(sec % 60),
  minutes = parseInt((sec % 3600) / 60),
  hours = parseInt(sec / 3600);

  seconds < 10 ? seconds = '0' + seconds : seconds = seconds
  minutes < 10 ? minutes = '0' + minutes : minutes = minutes
  hours < 10 ? hours = '0' + hours : hours = hours

  if (hours > 1) {
    result = hours + ':' + minutes + ':' + seconds
  } else {
    result = minutes + ':' + seconds
  }
  return result
}
