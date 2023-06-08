// ajax.js

// 引入 uni-ajax 模块
import ajax from '@/uni_modules/u-ajax'

// 创建请求实例
const instance = ajax.create({
  // 初始配置
  baseURL: 'http://localhost:5678/smart_parking',
})

// 添加请求拦截器
instance.interceptors.request.use(
  config => {
	config.header = {
		"Authorization": uni.getStorageSync("token")
	}
    return config
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 添加响应拦截器
instance.interceptors.response.use(
  response => {
	  
    // 对响应数据做些什么
    return response
  },
  error => {
	 if(error.statusCode == "403") {
	 		  uni.showToast({
	 		  	title: '您还未登录，请登录',
	 		  	icon: 'none',
	 			success() {
	 				uni.navigateTo({
	 				  url: '/pages/login/login'
	 				})
	 			}
	 		  })
	 }
    return Promise.reject(error)
  }
)

// 导出 create 创建后的实例
export default instance
