(function() {
  var container = document.querySelector('.swiper-container');
  var music = document.getElementById('music');
  var swiper = new Swiper('.swiper-container', {
    autoplay: container.dataset.delay ? {
      delay: container.dataset.delay,
      disableOnInteraction: false
    } : false,
    direction: container.dataset.direction,
    // initialSlide: container.dataset.pageCount + 1, // 解决循环模式下音视频暂停后不会续约播放的问题
    slidesPerView: 1,
    spaceBetween: 0,
    loop: container.dataset.loop === 'true',
    mousewheel: true,
    allowTouchMove: container.dataset.allowTouchMove === 'true',
    pagination: {
      el: '.swiper-pagination',
      type: 'fraction'
    },
    navigation: {
      nextEl: '.switch-guide',
    },
    speed: 600,
    effect: container.dataset.effect,
    grabCursor: true,
    fadeEffect: {
      crossFade: true
    },
    cubeEffect: {
      shadow: false,
      slideShadows: false
    },
    flipEffect: {
      slideShadows: false,
      limitRotation: false
    },
    on: {
      init: function() {
        if (music) {
          if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
            this.el.classList.remove('swiper-container-3d');
          }
          music.addEventListener('play', function() {
            for (var i = 0; i < musicIcons.length; i++) {
              musicIcons[i].classList.add('rotate');
            }
          });
          music.addEventListener('ended', function() {
            for (var i = 0; i < musicIcons.length; i++) {
              musicIcons[i].classList.remove('rotate');
            }
          });
        }
        if (videos) {
          // 是否UC浏览器(针对苹果的UC做处理)
          if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
            if (/(AliApp)/i.test(navigator.userAgent)) {
              var videoBtns = document.querySelectorAll('.video-btn');
              for (var i = 0; i < videoBtns.length; i++) {
                videoBtns[i].style.display = 'none';
              }
            }
          }
        } else {
          return false;
        }
        var container, containers, i, len;
        containers = document.getElementsByClassName('rich-text');
        len = containers.length;
        for (i = len - 1; i > -1; i--) {
          container = containers[i];
          var values = container.innerText.match(/{#参数\d+#}/g) || [];
          values.forEach(function(value) {
            // 用地址栏参数值替换原文本中的参数值
            var paramValue = getQueryString('p' + value.match(/\d+/g))
            if (paramValue) {
              var str = container.innerText.replace(value, paramValue);
              container.innerText = str;
            }
          })
        }
      },
      slideChangeTransitionEnd: function() {
        videos = document.querySelectorAll('video');
        var myAudio = document.querySelectorAll('.J-audio');
        pauseMedia(videos, 'video');
        pauseMedia(myAudio, 'audio');

        function pauseMedia (media, type) { // 翻页时媒体暂停播放
          for (var i = 0; i < media.length; i++) {
            media[i].pause();
            if (type === 'audio') {
              var audioBtn = media[i].previousElementSibling.previousElementSibling;
              audioBtn.classList.add('paused');
              audioBtn.classList.remove('play');
            }
          }
        }
      }
    }
  });
  var musicIcons = container.querySelectorAll('.music-icon');
  var videos = container.querySelectorAll('video');
  swiper.el.addEventListener('click', function(event) {
    event.stopPropagation()
    var target = event.target;
    if (target.classList.contains('music-icon')) {
      for (var i = 0; i < musicIcons.length; i++) {
        musicIcons[i].classList.toggle('rotate');
      }
      if (music.paused) {
        music.play()
      } else {
        music.pause()
      }
    } else {
      event.preventDefault()
    }
    if (target.classList.contains('J-player-btn')) {
      var audiosEl = target.parentNode.querySelector('.J-audio');
      if (audiosEl.paused) {
        audiosEl.play();
        target.classList.add('play');
        target.classList.remove('paused');
      } else {
        audiosEl.pause()
        target.classList.add('paused');
        target.classList.remove('play');
      }
      audiosEl.addEventListener('timeupdate', function() {
        var parentElement = this.previousElementSibling,
          progressBar = parentElement.querySelector('.J-progress-bg'),
          progressBarW = progressBar.offsetWidth,
          showTime = formatTime(this.currentTime),
          showBarW = progressBarW / (this.duration / this.currentTime);
        parentElement.querySelector('.J-slider-bar').style.left = showBarW + 'px';
        parentElement.querySelector('.J-progress-bar').style.width = showBarW + 'px';
        parentElement.querySelector('.J-show-time').innerHTML = showTime;
      })
      audiosEl.addEventListener('ended', function() {
        target.classList.add('paused')
        target.classList.remove('play')
      })
    } else {
      event.preventDefault()
    }
    if (target.classList.contains('video-btn')) {
      var swiperVideo = target.previousElementSibling;
      if (swiperVideo.paused) {
        swiperVideo.play();
      }
      swiperVideo.addEventListener('timeupdate', function() {
        this.setAttribute('controls', true);
        target.style.display = 'none';
      });
    } else {
      event.preventDefault()
    }
    if (target.classList.contains('text')) {
      var actionData = JSON.parse(target.dataset.element),
          actionNum = actionData.action; //2: webUrl  4: App  5: map  6: tell  7: quick
      switch (actionNum) {
        case '2':
          window.location.href = actionData.webURL;
          break;
        case '4':
          if(navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)){
            window.location.href = actionData.ipaLink;//ios app协议
            window.setTimeout(function() {
                window.location.href = actionData.ipaDownloadURL;
            }, 3000)
          }
          if(navigator.userAgent.match(/android/i)){
            window.location.href = actionData.apkLink;//android app协议
            window.setTimeout(function() {
                window.location.href = actionData.apkDownloadURL;//android 下载地址
            }, 3000)
          }
          break;
        case '5':
          window.location.href = 'http://api.map.baidu.com/marker?location=' + actionData.lat + ',' + actionData.lon + '&title=' + actionData.title + '&content=' + actionData.addr + '&output=html';
          break;
        case '6':
          window.location.href = 'tel:' + actionData.tel;
          break;
        case '7':
          window.location.href = actionData.quickAppURL;
          break;
      }
    } else {
      event.preventDefault()
    }
  })

  /**
   * 获取地址栏参数值
   * @param name
   * @returns {*}
   */
  function getQueryString (name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
      return decodeURIComponent(r[2]);
    }
    return null;
  }
})();
