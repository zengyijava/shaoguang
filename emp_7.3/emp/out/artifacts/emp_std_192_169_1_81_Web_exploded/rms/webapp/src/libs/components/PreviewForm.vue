<template>
  <el-form class="preview-form" ref="cardForm" :model="card" :rules="rules"  label-position="top">
    <el-form-item :label="$t('public.theme')" prop="tmName">
      <el-input size="small" class="preview-title-input" v-model="card.tmName" :maxlength="15" :placeholder="$t('richText.setRichMedia')"></el-input>
    </el-form-item>
    <el-form-item
      v-if="showCorpSelect"
      :label="$t('public.corpHint')">
      <el-select
        size="small"
        v-model="card.corpId"
        :remote="true"
        :filterable="true"
        :placeholder="$t('public.corpHolder')"
        :remote-method="remoteMethod"
        :loading="loading">
        <el-option
          v-for="item in corpOptions"
          :key="item.corpCode"
          :label="item.corpName"
          :value="item.corpCode">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item v-if="industryAndUse" :label="$t('public.Industry')">
      <el-select size="small" v-model="card.industryId" :placeholder="$t('public.pSelect')">
        <el-option :label="$t('public.pSelect')" value=""></el-option>
        <el-option
          v-for="item in industryIdArr"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <!-- <el-button type="text" size="small" @click="industryActive ? industryActive = false : industryActive = true">
        {{ $t('public.IndustryGm') }}
      </el-button>
      <pop-manage v-if="industryActive" :itemData="industryIdArr" :itemType="'industry'" :tmType="card.tmpType"
        @hidenUsePanel="hidenUsePanel"></pop-manage> -->
    </el-form-item>
    <el-form-item v-if="industryAndUse" :label="$t('public.use')">
      <el-select size="small" v-model="card.useId" :placeholder="$t('public.pSelect')">
        <el-option :label="$t('public.pSelect')" value=""></el-option>
        <el-option
          v-for="item in useIdArr"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <!-- <el-button type="text" size="small" @click="useActive ? useActive = false : useActive = true">{{ $t('public.useGm') }}</el-button>
      <pop-manage v-if="useActive" :itemData="useIdArr" :itemType="'use'" :tmType="card.tmpType"
        @hidenUsePanel="hidenUsePanel"></pop-manage> -->
    </el-form-item>
    <slot></slot>
  </el-form>
</template>

<script>
import useManage from './property/UseManage'
import utils from '../utils'
// import actions from '../api'
import {mapActions, mapGetters} from 'vuex'
import actions from '../api';

export default {
  name: 'PreviewForm',
  props: {
    card: {
      type: Object,
      default: () => {
      }
    }
  },
  computed: {
    ...mapGetters(['userInfo', 'useIds']),
    // 是否公共场景
    isPublic: function () {
      const _public = utils.getUrlParameters('isPublic', false, 'url')

      if (_public && +_public === 1) {
        return true
      } else {
        return false
      }
    },

    // 行业数据
    industryIdArr () {
      let industryArr = []
      let useData = []

      if (this.useIds.length > 0) {
        useData = this.useIds
      } else {
        return industryArr
      }

      for (let i = 0; i < useData.length; i++) {
        let tmpType = useData[i].type

        if (tmpType === 0) {
          industryArr.push(useData[i])
        }
      }

      return industryArr
    },

    useIdArr () {
      let UseArr = []
      let useData

      if (this.useIds.length > 0) {
        useData = this.useIds
      } else {
        return UseArr
      }

      for (let i = 0; i < useData.length; i++) {
        let tmpType = useData[i].type

        if (tmpType === 1) {
          UseArr.push(useData[i])
        }
      }

      return UseArr
    }
  },
  components: {
    'pop-manage': useManage
  },
  created () {
    if (this.userInfo.hasOwnProperty('data')) {
      let _roleArr = this.userInfo.data.loginCorp.roleArr
      let _pageType = this.isPublic ? 'common' : 'my'
      let _self = this

      if (Number(this.userInfo.data.type) === 1) {
        this.showCorpSelect = false
      } else {
        this.showCorpSelect = true
      }
      _roleArr.map(key => {
        if (key.type === _pageType) {
          if (key.industryAndUse === '1') {
            // 获取行业和用途
            this.getIndustryUses({
              name: '',
              type: '',
              tmpType: this.card.tmpType
            })
            _self.industryAndUse = true
          } else {
            _self.industryAndUse = false
          }
        }
      })
    } else {
      this.getUserInfos()
    }
  },
  data () {
    return {
      industryActive: false,
      useActive: false,
      industryAndUse: false,
      // 是否显示企业选择
      showCorpSelect: false,
      corpOptions: [],
      loading: false,
      rules: {
        tmName: [
          { required: true, message: this.$t('public.pThemeName'), trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    // 监听用户信息是否获取成功
    userInfo: function (val) {
      if (val) {
        let _code = val.code
        if (_code === 200) {
          let _roleArr = val.data.loginCorp.roleArr
          let _pageType = this.isPublic ? 'common' : 'my'
          let _self = this

          // 只有在rcos中才有企业选择
          if (Number(this.userInfo.data.type) === 1) {
            this.showCorpSelect = false
          } else {
            this.showCorpSelect = true
          }

          _roleArr.map(key => {
            if (key.type === _pageType) {
              if (key.industryAndUse === '1') {
                // 获取行业和用途
                this.getIndustryUses({
                  name: '',
                  type: '',
                  tmpType: this.card.tmpType
                })
                _self.industryAndUse = true
              } else {
                _self.industryAndUse = false
              }
            }
          })
        }
      }
    }
  },
  methods: {
    ...mapActions(['getIndustryUses', 'getUserInfos']),
    hidenUsePanel: function (type) {
      if (type === 'industry') {
        this.industryActive = false
      } else if (type === 'use') {
        this.useActive = false
      }
    },
    getCorpOptionData (params) {
      let _self = this
      let _params = {
        corpName: params
      }
      this.loading = false
      actions.getCorpInfos(_params, response => {
        _self.corpOptions = response.data.data.corps
      }, errMsg => {
        _self.$message.error(errMsg)
      })
    },
    remoteMethod (params) {
      if (params) {
        this.loading = true
        this.getCorpOptionData(params)
      } else {
        this.corpOptions = []
      }
    }
  }
}
</script>

<style lang="less">
  .preview-form {
    display:inline-block;
    padding: 24px 0 24px 24px;
    vertical-align: top;
    .el-input {
      width: 200px;
    }

    .el-select {
      margin-right: 8px;
    }
    .preview-title-input .el-input__inner{
      padding-left: 8px;
      padding-right: 8px;
    }
  }

</style>
