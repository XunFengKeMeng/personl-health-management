
module.exports = {
  // 关闭保存时的 ESLint 检查。
  lintOnSave: false,
  devServer: {
    host: "localhost",
    port: 21091,
    https: false,
    // 配置代理，用于解决跨域问题。
    proxy: "http://localhost:21090"
  }
}