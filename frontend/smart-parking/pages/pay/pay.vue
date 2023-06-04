<template>
	<view>
		<view style="text-align: center;margin-bottom: 10px;">
			<image src="../../static/QRCode.jpg" mode="aspectFit"></image>
		</view>
		
		<button type="primary" @click="pay()">提交</button>
	</view>
</template>

<script>
	import ajax from "../../common/ajax.js"
	
	export default {
		data() {
			return {
				orderId: null
			}
		},
		onLoad: function (option) { 
			console.log(option.orderId); 
			this.orderId = option.orderId
		},
		methods: {
			pay() {
				ajax.put({
					url: "/parkingOrder/pay/" + this.orderId,
				}).then(res => {
					let code = res.data.code
					if (code == "200") {
						uni.showToast({
							title: "支付成功",
							icon: 'none'
						})
						uni.reLaunch({
							url:"/pages/index/index"
						})
					}
				})
			}
		}
	}
</script>

<style>

</style>
