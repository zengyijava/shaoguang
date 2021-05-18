'use strict'

const path = require('path')
const merge = require('webpack-merge')
const baseWebpackConfig = require('./index')

const config = merge(baseWebpackConfig, {
  globModule: '*(card|media|h5-editor|my-template|text-editor)',
  dev: {
    proxyTable: {
      '/': {
        target: 'http://192.169.1.37:8085/rxflow',
        changeOrigin: true
      }
    }
  },
  build: {
    assetsRoot: path.resolve(__dirname, '../dist-rx')
  }
})

module.exports = config
