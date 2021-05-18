<template>
  <Panel class="property card-style" :title="$t('styleSetting.styleTitle')"
    :class="{'show': styleVisible}" @close="handleClose()">
    <template slot="body">
      <ul class="card-style-tab">
        <li class="active">{{ $t('styleSetting.styleOptions.all') }}</li>
        <li>{{ $t('styleSetting.styleOptions.standard') }}</li>
        <li>{{ $t('styleSetting.styleOptions.business') }}</li>
        <li>{{ $t('styleSetting.styleOptions.cartoon') }}</li>
        <li>{{ $t('styleSetting.styleOptions.chineseStyle') }}</li>
      </ul>
      <ul class="card-style-list">
        <li v-for="(style, index) in cardStyles" :key="index" @click="changeStyle(style.url)">
          <img :src="style.url" />
          <p>{{style.name}}</p>
        </li>
      </ul>
    </template>
  </Panel>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import Panel from '../../libs/components/Panel'

export default {
  name: 'StyleSetting',
  components: {Panel},
  data () {
    return {
    }
  },
  computed: mapGetters(['element', 'styleVisible', 'cardStyles']),
  methods: {
    ...mapMutations(['hideStyle', 'changeStyle']),
    handleClose () {
      this.element.active = false
      this.hideStyle()
    }
  }
}
</script>

<style lang="less">
  @import '../../libs/assets/less/variables';

  .property {
    &.card-style {
      width: 345px;
      right: -345px;
      .card-style-tab {
        width: 100%;
        height: 30px;
        font-size: 12px;
        padding: 10px 16px;
        box-sizing: border-box;
        li {
          float: left;
          margin-right: 15px;
          padding: 1px 5px;
          cursor: pointer;
          &:hover {
            color: @green;
            border-bottom: 1px solid @green;
          }
        }
        .active {
          color: @green;
          border-bottom: 1px solid @green;
        }
      }

      .card-style-list {
        width: 100%;
        height: 755px;
        margin-top: 15px;
        overflow-x: hidden;
        overflow-y: auto;
        padding: 0 0 0 16px;
        box-sizing: border-box;
        li {
          width: 145px;
          height: 123px;
          cursor: pointer;
          box-sizing: border-box;
          float: left;
          margin: 0 15px 12px 0;
          background-color: #fff;
          box-shadow: 0px 1px 1px 0px rgba(225, 225, 225, 0.4);
          border-radius: 2px;
          border: solid 1px #ebebeb;
          position: relative;
          img {
            width: 131px;
            height: 111px;
            position: absolute;
            top: 6px;
            left: 6px;
          }
          p {
            width: 143px;
            line-height: 25px;
            font-size: 12px;
            text-align: center;
            position: absolute;
            top: 35px;
            left: 0;
          }
        }
      }
    }
  }
</style>
