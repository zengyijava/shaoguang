// 引入 gulp
const {src, dest, series, parallel} = require('gulp');

// 引入组件
const del = require('del');
const cleanCSS = require('gulp-clean-css'); //css压缩
const uglify = require('gulp-uglify'); //js压缩
const useref = require('gulp-useref'); //替换引用
const gulpif = require('gulp-if'); //筛选
const rev = require('gulp-rev'); //文件Md5
const revReplace = require('gulp-rev-replace'); //替换

// 配置路径
const config = {
  app: 'app',
  dist: 'dist'
};

function clean () {
  return del(config.dist + '/*');
}

function copy () {
  return src(config.app + '/image/*')
    .pipe(dest(config.dist + '/image/'));
}

// html中合并css/js/加md5更名并压缩
function html () {
  return src(config.app + '/*.html')
    .pipe(useref())
    .pipe(gulpif('*.js', uglify()))
    .pipe(gulpif('*.css', cleanCSS()))
    .pipe(gulpif('!*.html', rev()))
    .pipe(revReplace())
    .pipe(dest(config.dist));
}

exports.clean = clean;
exports.html = html;
// 默认任务
exports.default = series(clean, parallel(copy, html));
