<template>
	<view class="container">
		<uni-forms class="form">
			<uni-forms-item label="用户名" required>
				<uni-icon name="account" slot="media" />
				<uni-easyinput v-model="username" type="text" placeholder="请输入用户名" />
			</uni-forms-item>
			<uni-forms-item label="密码" required>
				<uni-icon name="lock" slot="media" />
				<uni-easyinput v-model="password" type="password" placeholder="请输入密码" />
			</uni-forms-item>
			<uni-button style="margin-bottom: 10px;" @click="onSubmit" type="primary" size="default" :loading="loading" :disabled="loading">登录</uni-button>
			<text style="margin-bottom: 10px;">还没有账号？点击进行注册</text>
			<uni-button @click="onRegister" type="default" size="default">注册</uni-button>
		</uni-forms>
	</view>
</template>

<script>
import ajax from 'common/ajax.js'

export default {
	data() {
		return {
			username: '',
			password: '',
			loading: false
		}
	},
	methods: {
		onSubmit() {
			if (!this.username || !this.password) {
				uni.showToast({
					title: '用户名和密码不能为空',
					icon: 'none'
				})
				return
			}
			this.loading = true
			ajax.post('/user/login', {
				username: this.username,
				password: this.password
			}).then(res => {
				if (res.data.code === 200) {
					uni.showToast({
						title: '登录成功',
						icon: 'success'
					})
					uni.setStorageSync('token', res.data.data)
					uni.reLaunch({
						url: '/pages/index/index'
					})
				} else {
					uni.showToast({
						title: "登录失败",
						icon: 'none'
					})
				}
			}).finally(() => {
				this.loading = false
			})
		},
		onRegister() {
		    uni.navigateTo({
		      url: '/pages/register/register'
		    })
		}
	}
}
</script>

<style scoped>
	.login {
		padding: uni.upx2;
	}
	.form {
		padding: 15px;
		background-color: #fff;
	}
</style>