# 编译器代码

> webapp

## 权限说明
getUserInfo接口中roleArr对应角色权限，modulePer对应可以拥有哪几个编辑器

## 编译设置

``` bash
# 安装依赖
npm install

# 在localhost:8080上运行热替换服务
npm run dev-rx

# 构建精简的线上版本

## 修改src/libs/config.js中的编辑器地址
TEXT_EDITOR_LINK: 'textEditor.meditorPage',
MEDIA_EDITOR_LINK: 'mediaEditor.meditorPage',
SCENE_EDITOR_LINK: 'cardEditor.meditorPage',
H5_EDITOR_LINK: 'h5Editor.meditorPage'

## 修改获取地址栏参数为
GET_URL_PARAMS: {
  LIST: 'iframe',
  PREVIEW: 'iframe',
  POPLIST: 'iframe'
}

npm run build-rx

# 构建线上版本并查看相关的分析报告
npm run build-rx --report

# 运行单元测试
npm run unit

# 运行自动化测试
npm run e2e

# 运行所有测试
npm test
```

想进一步了解构建工作原理, 请查看[指南](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).

## 前端代码部署
最终分发dist-rx文件夹的文件
