<template>
	<view>
		测试蓝牙
	</view>
</template>

<script>
	export default {
		data() {
			return {
				deviceId: "C4:22:08:03:13:BB",
				serviceId: "0000ffe0-0000-1000-8000-00805f9b34fb",
				characteristicId: "0000ffe1-0000-1000-8000-00805f9b34fb"
			}
		},
		onReady() {
			this.initBlue()
			this.discovery()
		},
		methods: {
			initBlue() {
			    uni.openBluetoothAdapter({
			        success(res) {
			            console.log('初始化蓝牙成功')
						this.msg = "初始化蓝牙成功"
			            console.log(res)
			        },
			        fail(err) {
			            console.log('初始化蓝牙失败')
			            console.error(err)
			        }
			    })
			},
			discovery() {
				let that = this
			    uni.startBluetoothDevicesDiscovery({
			        success(res) {
			            console.log('开始搜索')
			            
			            // 开启监听回调
			            uni.onBluetoothDeviceFound(device => {
							if (device.devices[0].deviceId === that.deviceId) {
								console.log("找到设备")
								that.connect(device.devices[0].deviceId)
							}
						})
			        },
			        fail(err) {
			            console.log('搜索失败')
			            console.error(err)
			        }
			    })
			},
			connect(deviceId) {
				var that = this
				console.log("开始连接：" + deviceId)
				uni.createBLEConnection({
					deviceId: deviceId,
					success(res) {
						console.log('连接成功')
						console.log(res)
						// 停止搜索
						that.stopDiscovery()
					},
					fail(err) {
						console.log('连接失败')
						console.error(err)
					}
				})
			},
			stopDiscovery() {
			    uni.stopBluetoothDevicesDiscovery({
			        success(res) {
			            console.log('停止成功')
			            console.log(res)
			        },
			        fail(err) {
			            console.log('停止失败')
			            console.error(err)
			        }
			    })
			}
		}
	}
</script>

<style>

</style>
