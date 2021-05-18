(function() {
  var designW = 320,
    actualW = document.documentElement.clientWidth,
    actualH = document.documentElement.clientHeight;

  var containers, i, len;
  containers = document.getElementsByClassName('page-view');
  len = containers.length;
  for (i = 0; i < len; i++) {
    containers[i].style.transform = 'scale(' + actualW / designW + ')';
  }

  containers = document.getElementsByClassName('bg-img-preview');
  len = containers.length;
  for (i = 0; i < len; i++) {
    var container = containers[i],
      cropW = container.dataset.cropw,
      cropH = container.dataset.croph;
    container.style.transform = 'scale(' + actualW / cropW + ', ' + actualH / cropH + ')'
  }
})();
