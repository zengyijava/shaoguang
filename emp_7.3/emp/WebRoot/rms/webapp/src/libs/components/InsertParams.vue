<template>
  <div class="insert-params">
    <el-form-item :label="$t('property.params.name')">
      {{ nextParamName }}
    </el-form-item>
    <el-form-item :label="$t('property.params.type')">
      <el-select class="type-select" size="small" v-model="param.type" @change="changeType" @click.native="updateCanInsert(false)">
        <el-option v-for="item in options" :key="item.type" :label="item.label" :value="item.type">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item :label="$t('property.params.lengthRestrict')" v-if="param.hasLength === 1">
      <el-radio-group v-model="param.lengthRestrict" @change="changeRestrict">
        <el-radio size="small" :label="0">{{ $t('property.params.variableLength') }}</el-radio>
        <el-radio size="small" :label="1">{{ $t('property.params.fixedLength') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item :label="$t('property.params.minLength')" v-if="param.hasLength === 1 & param.lengthRestrict === 0">
      <el-input-number size="small" :min="0" :max="maxLength" v-model.number="param.minLength"
        controls-position="right">
      </el-input-number>
    </el-form-item>
    <el-form-item :label="$t('property.params.maxLength')" v-if="param.hasLength === 1 & param.lengthRestrict === 0">
      <el-input-number size="small" :min="1" :max="maxLength" v-model.number="param.maxLength"
        controls-position="right">
      </el-input-number>
    </el-form-item>
    <el-form-item v-if="param.hasLength === 1  && param.lengthRestrict === 1" :label="$t('property.params.fixedLengthT')">
      <el-input-number size="small" :min="1" v-model.number="param.fixLength"
        controls-position="right">
      </el-input-number>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="small" :disabled="!canInsert" @click.stop="handleAddParam">{{ $t('property.params.insertArgument') }}</el-button>
    </el-form-item>
  </div>
</template>
<script>
import {mapGetters, mapMutations} from 'vuex'
import utils from '../utils'

export default {
  name: 'InsertParams',
  data () {
    return {
      disabled: false,
      options: [{
        label: this.$t('property.params.cNumber'),
        type: 1
      }, {
        label: this.$t('property.params.zNumber'),
        type: 2
      }, {
        label: this.$t('property.params.money'),
        type: 3
      }, {
        label: this.$t('property.params.string'),
        type: 4
      }, {
        label: this.$t('property.params.dateOne'),
        type: 5
      }, {
        label: this.$t('property.params.dateTwo'),
        type: 6
      }, {
        label: this.$t('property.params.dateThree'),
        type: 7
      }, {
        label: this.$t('property.params.dateFour'),
        type: 8
      }, {
        label: this.$t('property.params.dateTimeOne'),
        type: 9
      }, {
        label: this.$t('property.params.dateTimeTwo'),
        type: 10
      }, {
        label: this.$t('property.params.dateTimeThree'),
        type: 11
      }, {
        label: this.$t('property.params.dateTimeFour'),
        type: 12
      }, {
        label: this.$t('property.params.time'),
        type: 13
      }]
    }
  },
  computed: {
    ...mapGetters(['params', 'param', 'canInsert']),
    nextParamName () {
      if (this.canInsert) {
        return utils.getMaxName(this.params)
      }
      return this.param.name
    },
    maxLength () {
      switch (this.param.type) {
        case 1:
        case 2:
          return 32
        case 3:
          return 15
        case 4:
          return 5
      }
    }
  },
  methods: {
    ...mapMutations(['addParam', 'updateCanInsert']),
    changeType (type) {
      this.param.type = type
      if (type < 5) {
        this.param.hasLength = 1
        this.$set(this.param, 'lengthRestrict', 0)
        this.changeRestrict(0)
      } else {
        this.param.hasLength = 0
        delete this.param.lengthRestrict
        delete this.param.fixLength
        delete this.param.minLength
        delete this.param.maxLength
      }
    },

    changeRestrict (restrict) {
      this.$set(this.param, 'lengthRestrict', restrict)
      if (restrict === 0) {
        this.$set(this.param, 'minLength', 0)
        this.$set(this.param, 'maxLength', this.maxLength)
        delete this.param.fixLength
      } else if (restrict === 1) {
        this.$set(this.param, 'fixLength', 1)
        delete this.param.minLength
        delete this.param.maxLength
      }
    },

    handleAddParam () {
      if (this.canInsert) {
        const ref = utils.deepClone(this.param)
        ref.name = this.nextParamName
        this.addParam(ref)
      }
    }
  }
}
</script>

<style lang="less">
  .insert-params {
    background-color: #fff;
    .el-radio + .el-radio {
      margin-left: 10px;
    }

    .el-form-item {
      margin-bottom: 0;
    }

    .el-select,
    .el-input-number--small {
      width: 220px;
    }

    .el-form-item:last-child {
      padding-top: 8px;
      padding-bottom: 16px;
      margin-left: 80px;
    }
  }
</style>
