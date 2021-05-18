<template>
  <div class="personalise">
    <div class="tip-box">
      <div class="title">发送不同短信内容给用户</div>
      <ol>
        <li>针对不同手机号码、短信内容生成不同的资源</li>
        <li>可查看具体手机号的下载次数</li>
      </ol>
    </div>
    <div class="brief">
      <div class="cont">个性化资源只支持调用接口进行获取</div>
      <p>您可通过调取 “<i>生成RX富信（URL模式）</i>”接口来获取资源</p>
    </div>
    <el-button @click="$router.push('/')">返回</el-button>
  </div>
</template>

<script>
export default {
  name: 'Personalise'
}
</script>

<style lang="less" scoped>
  @import "../assets/less/default";
  .personalise{
    padding-top: 6px;
    .brief{
      margin: 24px 0 20px;
      .cont{
        font-size: 14px;
      }
      i{
        font-style: normal;
        color: #3b9cff;
      }
    }
  }
</style>
