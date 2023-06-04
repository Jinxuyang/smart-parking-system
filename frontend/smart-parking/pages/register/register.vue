<template>
  <view class="container">
    <uni-forms class="form">
      <uni-forms-item label="用户名" required>
        <uni-icon name="account" slot="media" />
        <uni-easyinput v-model="username" type="text" placeholder="请输入用户名" />
        <view class="tip">用户名长度为6-20个字符</view>
      </uni-forms-item>
      <uni-forms-item label="密码" required>
        <uni-icon name="lock" slot="media" />
        <uni-easyinput v-model="password" type="password" placeholder="请输入密码" />
        <view class="tip">密码长度为6-20个字符</view>
      </uni-forms-item>
      <uni-forms-item label="确认密码" required>
        <uni-icon name="lock" slot="media" />
        <uni-easyinput v-model="confirmPassword" type="password" placeholder="请再次输入密码" />
        <view class="tip">请再次输入密码</view>
      </uni-forms-item>
      <uni-button style="margin-bottom: 10px;" @click="onSubmit" type="primary" size="default" :loading="loading" :disabled="loading">注册</uni-button>
      <uni-button @click="onLogin" type="default" size="default">返回登录</uni-button>
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
      confirmPassword: '',
      loading: false
    }
  },
  methods: {
    onSubmit() {
      if (!this.username || !this.password || !this.confirmPassword) {
        uni.showToast({
          title: '用户名和密码不能为空',
          icon: 'none'
        })
        return
      }
      if (this.password !== this.confirmPassword) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        })
        return
      }
      this.loading = true
      ajax.post('/user/register', {
        username: this.username,
        password: this.password
      }).then(res => {
        if (res.data.code === 200) {
          uni.showToast({
            title: '注册成功',
            icon: 'success'
          })
          uni.reLaunch({
            url: '/pages/login/login'
          })
        } else {
          uni.showToast({
            title: "注册失败",
            icon: 'none'
          })
        }
      }).finally(() => {
        this.loading = false
      })
    },
    onLogin() {
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.container {
  padding: uni.upx2;
}
.tip {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}
.form {
		padding: 15px;
		background-color: #fff;
}
</style>
