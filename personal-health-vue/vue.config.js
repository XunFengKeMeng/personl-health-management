
module.exports = {
  // 关闭保存时的 ESLint 检查。
  lintOnSave: false,
  devServer: {
    host: "localhost",
    port: 21091,
    https: false,
    // 配置代理，用于解决跨域问题。
    // proxy: "http://localhost:21090"
    proxy: {
      // 代理所有以 /api 开头的请求
      "/api": {
        target: "http://localhost:21090", // 后端地址
        changeOrigin: true, // 修改请求头中的Host为目标URL
        pathRewrite: {
          "^/api": "", // 移除路径中的/api前缀（按需配置）
        },
      },
      // 代理图片请求（解决证书问题）
      "/images": {
        target: "https://www.helloimg.com", // 图片服务器地址
        changeOrigin: true,
        secure: false, // 忽略HTTPS证书错误（仅开发环境）
        pathRewrite: {
          "^/images": "/i", // 将/images重写为/i（根据实际路径调整）
        },
      },
    },
  }
}