<template>
	<view class="uni-container">
		<uni-notice-bar single  color="#2979FF" background-color="#EAF2FF" show-close text="红色: 车位已被占用 蓝色: 推荐车位 绿色: 当前选中车位" />
		<view style="width: 100%;">
			<l-echart ref="chart" @finished="init"></l-echart>
		</view>
		<uni-card title="实时车流信息" :extra="trafficStatus">
			<text class="uni-body">近1小时车辆停车数：{{traffic}} 辆</text><br>
			<text class="uni-body">{{trafficMsg}}</text>
		</uni-card>
		<uni-card :is-shadow="true" title="预约信息">
			{{reserveMsg}}
			<view slot="actions" style="margin-left: 10px;margin-bottom: 10px;">
				<view @click="showUnlockDialog()">
					<uni-icons type="checkbox-filled" size="20"></uni-icons>
					<text style="font-size: 15px;margin-left: 5px;">去开/关锁</text>
				</view>
				</br>
				<view @click="toPay()">
					<uni-icons type="wallet-filled" size="20"></uni-icons>
					<text style="font-size: 15px;margin-left: 5px;">去支付</text>
				</view>
			</view>
		</uni-card>
		<uni-card :is-shadow="true">
			<uni-row>
				<uni-col :span="12">
					<uni-icons type="fire" size="20"></uni-icons>
					总车位数：{{totalPlace}}个
				</uni-col>
				<uni-col :span="12">
					<uni-icons type="eye" size="20"></uni-icons>
					空闲车位：{{availablePlace}}个
				</uni-col>
			</uni-row>
			<view style="margin-top: 10px;margin-bottom: 10px;">
				<progress :percent="this.placeProgress" activeColor="#10AEFF" stroke-width="6" show-info="true"/>
				<text>{{progressMsg}}</text>
			</view>
			<uni-row>
				<uni-icons type="checkmarkempty" size="20"></uni-icons>
				数据更新时间：{{dataUpdateTime}}
			</uni-row>
		</uni-card>
		<uni-card @click="toUsage">
			<text class="uni-body">点击查看车位占用率信息>></text>
		</uni-card>
		<uni-card title="热点时间统计">
			<qiun-data-charts 
			    type="column"
			    :opts="colOpt"
			    :chartData="colData"
			    :ontouch="true"
			/>
			{{colMsg}}
		</uni-card>
		<view>
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
			
			<uni-popup ref="alertDialog" type="dialog">
				<uni-popup-dialog type="warn" cancelText="取消" confirmText="确认" title="确认" :content="dialogText" @confirm="dialogConfirm" @close="dialogClose" :loading="loading"></uni-popup-dialog>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	import ajax from 'common/ajax.js'
	
	import * as echarts from 'echarts/core';
	import { TooltipComponent, GeoComponent } from 'echarts/components';
	import { CanvasRenderer } from 'echarts/renderers';
	echarts.use([TooltipComponent, GeoComponent, CanvasRenderer]);
	
	export default {
	    data() {
	        return {
				trafficStatus: "通畅",
				traffic: 0,
				trafficMsg: "",
				orderStatus: null,
				loading: false,
				deviceId: "",
				orderId: null,
				selectedPlace: "",
				totalPlace: 0,
				availablePlace: 0,
				placeProgress: 0,
				reserveMsg: "您还没有预约，快去预约吧！",
				reservePlace: "",
				dialogText: "",
				dialogType: "",
				msgType: 'success',
				messageText: '',
				progressMsg: "目前车位充足",
				dataUpdateTime: "",
				option: {
				    tooltip: {},
				    geo: {
				      map: 'place-map',
				      roam: true,
				      selectedMode: 'single',
				      tooltip: {
				        show: true
				      },
				      itemStyle: {
				        color: '#fff'
				      },
				      emphasis: {
				        itemStyle: {
				          color: null,
				          borderColor: 'green',
				          borderWidth: 2
				        },
				        label: {
				          show: false
				        }
				      },
				      select: {
				        itemStyle: {
				          color: 'green'
				        },
				        label: {
				          show: false,
				          textBorderColor: '#fff',
				          textBorderWidth: 2
				        }
				      },
				      regions: []
				    }
				},
				colOpt: {
					enableScroll: true,
					xAxis: {
					  scrollShow: true,
					  itemCount: 7
					},
					extra: {
					  column: {
						width: 35,
					  }
					}
				},
				colData: {},
				colMsg: "",
			}
	    },
	    mounted() {
			this.getPopluarPeriod()
			this.getReserveInfo()
			this.getPlaceInfo()
			this.getTraffice()
		},
		methods: {
			toUsage() {
				uni.navigateTo({
					url:"/pages/usage/usage"
				})
			},
			getTraffice() {
				ajax.get({
					url: "/parkingOrder/traffic/1"
				}).then(res => {
					let traffic = res.data.data
					this.traffic = traffic
					if (traffic > 50) {
						this.trafficMsg = "当前车流较大，可能发生拥堵"
						this.trafficStatus = "拥堵"
					} else {
						this.trafficMsg = "当前车流较小，道路畅通无阻"
						this.trafficStatus = "通畅"
					}
				})
			},
			toPay() {
				uni.navigateTo({
					url: "/pages/pay/pay" + "?orderId=" + this.orderId
				})
			},
			async init() {
				this.$refs.chart.resize({width: 500, height: 500})
				
				let that = this
				let regions = [];
				
				await ajax.get({
					url: "/parkingPlace/recommend"
				}).then(res => {
					let takenPlaceNames = res.data.data
					console.log(takenPlaceNames)
					for (let i = 0; i < takenPlaceNames.length; i++) {
					  regions.push({
						name: takenPlaceNames[i],
						itemStyle: {
						  color: '#0000ff'
						}
					  });
					}
				})
				
				
				await ajax.get({
					url: "/parkingPlace/status/1",
				}).then(res => {
					let takenPlaceNames = res.data.data
					
					for (let i = 0; i < takenPlaceNames.length; i++) {
					  regions.push({
						name: takenPlaceNames[i],
						itemStyle: {
						  color: '#ff0000'
						},
						select: {
						  itemStyle: {
							color: '#ff0000'
						  }
						}
					  });
					}
				})
				
				that.option.geo.regions = regions
				console.log(regions)
				await this.$refs.chart.init(echarts, chart => {
					uni.request({
						url:"static/place-map.svg",
						method:'GET',
						success: (svg) => {
							echarts.registerMap('place-map', {
								svg: svg.data
							})
							
							chart.setOption(this.option);
							chart.on('geoselectchanged', function (params) {
								let selectedNames = params.allSelected[0].name.slice();
								that.selectedPlace = selectedNames[0]
								that.showReserveDialog(selectedNames)
							});
						}
					})
				});
			},
			showUnlockDialog() {
				if(this.orderStatus == 0) {
					this.dialogText = `您确定要解锁${this.reservePlace}吗?`
					this.dialogType = "unlock"
				} else if (this.orderStatus == 1) {
					this.dialogText = `您确定要关锁${this.reservePlace}吗?`
					this.dialogType = "lock"
				}
				this.$refs.alertDialog.open()
			},
			showReserveDialog(selectedNames) {
				this.dialogType = "reserve"
				this.dialogText = `您确定要预约${selectedNames}, 价格为3元/小时`
				this.$refs.alertDialog.open()
			},
			unlock() {
				ajax.post({
					url: "/parkingOrder/unlock/" + this.orderId
				}).then(res => {
					let code = res.data.code
					if (code == "200") {
						this.msgType = "success"
						this.messageText = "您已成功解锁"
						this.$refs.message.open()
						this.getReserveInfo()
					} else {
						this.msgType = "warn"
						this.messageText = "解锁失败"
						this.$refs.message.open()
					}
				})
				// let that = this
				// uni.openBluetoothAdapter({
				//     success(res) {
				//         console.log('初始化蓝牙成功')
				// 		uni.startBluetoothDevicesDiscovery({
				// 		    success(res) {
				// 		        console.log('开始搜索')
						        
				// 		        // 开启监听回调
				// 		        uni.onBluetoothDeviceFound(device => {
				// 					if (device.devices[0].deviceId === that.deviceId) {
				// 						console.log("找到设备")
										
				// 					}
				// 				})
				// 		    },
				// 		    fail(err) {
				// 		        console.log('搜索失败')
				// 		        console.error(err)
				// 		    }
				// 		})
				//     },
				//     fail(err) {
				//         console.log('初始化蓝牙失败')
				//         console.error(err)
				//     }
				// })
			},
			lock() {
				ajax.post({
					url: "/parkingOrder/lock/" + this.orderId
				}).then(res => {
					let code = res.data.code
					if (code == "200") {
						this.msgType = "success"
						this.messageText = "您已成功关锁"
						this.$refs.message.open()
						this.getReserveInfo()
					} else {
						this.msgType = "warn"
						this.messageText = "关锁失败"
						this.$refs.message.open()
					}
				})
			},
			dialogConfirm() {
				if(this.dialogType == "reserve") {
					ajax.post({
						url: "/parkingOrder/reserve/" + this.selectedPlace,
					}).then(res => {
						let code = res.data.code
						if (code == "200") {
							this.msgType = "success"
							this.messageText = "您已成功预约"
							this.$refs.message.open()
						}
						uni.reLaunch({
							url:"/pages/index/index"
						})
					})	
				} else if (this.dialogType == "unlock") {
					this.msgType = "warn"
					this.messageText = `正在解锁中，请稍后`
					this.$refs.message.open()
					setTimeout(this.unlock(), 3000);
				} else if (this.dialogType == "lock") {
					this.msgType = "warn"
					this.messageText = `正在关锁中，请稍后`
					this.$refs.message.open()
					setTimeout(this.lock(), 3000);
				}
			},
			dialogClose() {
				this.msgType = "warn"
				this.messageText = "您已取消"
				this.$refs.message.open()
			},
			getReserveInfo() {
				ajax.get({
					url: "/parkingOrder/reserve",
				}).then(res => {
					let info = res.data.data
					if (info == null) {
						this.reserveMsg = "您还没有预约，快去预约吧！"
					} else {
						this.reservePlace = info.parkingPlaceNum
						if (info.orderStatus == 0) {
							this.reserveMsg = `您已成功预约${this.reservePlace}，快去停车吧！解锁点击下方按钮。`
						} else if (info.orderStatus == 1) {
							this.reserveMsg = "您的车已停好。"
						} else if (info.orderStatus == 2) {
							this.reserveMsg = "您有一笔订单等待支付。"
						}
						this.orderId = info.orderId
						this.deviceId = info.place.BLEDeviceId
						this.orderStatus = info.orderStatus
					}
				})
			},
			getPlaceInfo() {
				ajax.get({
					url: "/parkingPlace/info"
				}).then(res => {
					let info = res.data.data
					this.totalPlace = info.total
					this.availablePlace = info.available
					this.placeProgress = 100 - ((info.available / info.total) * 100)
					if (this.placeProgress > 80) {
						this.progressMsg = "目前车位紧张，请尽快预定"
					} else {
						this.progressMsg = "目前车位充足"
					}
					let myDate = new Date();
					this.dataUpdateTime = myDate.toLocaleDateString() + " " + myDate.toLocaleTimeString()
				})
			},
			getPopluarPeriod() {
				ajax.get({
					url: "/parkingOrder/popular/time"
				}).then(res => {
					let info = res.data.data
					let max = 0;
					for (let i = 0; i < info.length; i++) {
						if (info[i] > max) {
							max = info[i];
							this.colMsg = `当前的热门时间为${i}点，建议您避开该时段`
						}
					}
					console.log(info)
					this.colData = {
						categories: ["0点","1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点","14点","15点","16点","17点","18点","19点","20点","21点","22点","23点"],
						series: [
						  {
							name: "停车数",
							data: info
						  }
						]
					}
				})
			}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>
