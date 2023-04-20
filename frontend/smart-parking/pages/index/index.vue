<template>
	<view class="uni-container">
		<view><l-echart ref="chart" @finished="init"></l-echart></view>
		<uni-card :is-shadow="true" title="预约信息">
			{{reserveMsg}}
			<view slot="actions" style="margin-left: 10px;margin-bottom: 10px;">
				<view @click="showUnlockDialog()">
					<uni-icons type="checkbox-filled" size="20"></uni-icons>
					<text style="font-size: 15px;margin-left: 5px;">去解锁</text>
				</view>
			</view>
		</uni-card>
		
		<uni-card :is-shadow="true">
			<uni-row>
				<uni-col :span="12">
					<uni-icons type="fire" size="20"></uni-icons>
					总车位数： 个
				</uni-col>
				<uni-col :span="12">
					<uni-icons type="eye" size="20"></uni-icons>
					空闲车位： 个
				</uni-col>
			</uni-row>
			<view style="margin-top: 10px;margin-bottom: 10px;">
				<progress :percent="50" activeColor="#10AEFF" stroke-width="6" show-info="true"/>
				<text>{{progressMSsg}}</text>
			</view>
			<uni-row>
				<uni-icons type="checkmarkempty" size="20"></uni-icons>
				数据更新时间：{{}}
			</uni-row>
		</uni-card>
		<uni-card title="空闲车位数量变化">
			<qiun-data-charts 
			    type="line"
			    :opts="lineOpt"
			    :chartData="lineData"
				:ontouch="true"
			/>
		</uni-card>
		<uni-card title="热门车位">
			<qiun-data-charts 
			    type="column"
			    :opts="colOpt"
			    :chartData="colData"
			    :ontouch="true"
			/>
		</uni-card>
		<uni-card title="热点时间统计">
			<qiun-data-charts 
			    type="mount"
			    :opts="mountOpt"
			    :chartData="mountData"
			    :ontouch="true"
			/>
		</uni-card>
		<view>
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
			
			<uni-popup ref="alertDialog" type="dialog">
				<uni-popup-dialog type="warn" cancelText="取消" confirmText="预定" title="确认" :content="dialogText" @confirm="dialogConfirm"
					@close="dialogClose"></uni-popup-dialog>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	import * as echarts from 'echarts/core';
	import { TooltipComponent, GeoComponent } from 'echarts/components';
	import { CanvasRenderer } from 'echarts/renderers';
	
	echarts.use([TooltipComponent, GeoComponent, CanvasRenderer]);
	var takenSeatNames = ['A001'];
	export default {
	    data() {
	        return {
				reserveMsg: "您还没有预约，快去预约吧！",
				dialogText: "",
				dialogType: "",
				msgType: 'success',
				messageText: '',
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
				      regions: this.makeTakenRegions(takenSeatNames)
				    }
				},
				progressMSsg: "目前车位充足",
				lineOpt: {
					enableScroll: true,
					xAxis: {
					  itemCount: 4,
					  scrollShow: true
					},
					extra: {
					  line: {
						type: "curve"
					  }
					}
				},
				lineData: {
					categories: ["2018","2019","2020","2021","2022","2023","2024","2025","2026","2027"],
					series: [
					  {
						name: "成交量A",
						data: [35,8,25,37,4,20,10,10,20,30]
					  }
					]
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
				colData: {
					categories: ["2018","2019","2020","2021","2022","2023","2018","2019","2020","2021","2022","2023"],
					series: [
					  {
						name: "目标值",
						data: [35,36,31,33,13,34,35,36,31,33,13,34]
					  }
					]
				},
				colMsg: "",
				mountOpt: {
					enableScroll: true,
					xAxis: {
					  itemCount: 6,
					  scrollShow: true
					},
					extra: {
					  mount: {
						type: "mount",
						linearType: "opacity",
						linearOpacity: 0.1
					  }
					}
				},
				mountData: {
					series: [
						{
							data: [{"name":"一班","value":55},{"name":"二班","value":63},{"name":"三班","value":86},{"name":"四班","value":65},{"name":"五班","value":40}]
						}
					]
				},
				mountMsg: ""
			}
	    },
	    
	    mounted() {},
		methods: {
			init() {
				this.$refs.chart.resize({width: 1000, height: 500})
				let that = this
				this.$refs.chart.init(echarts, chart => {
					uni.request({
						url:"static/place-map.svg",
						method:'GET',
						success: (svg) => {
							console.log(svg.data)
							echarts.registerMap('place-map', {
								svg: svg.data
							})
							chart.setOption(this.option);
							chart.on('geoselectchanged', function (params) {
								var selectedNames = params.allSelected[0].name.slice();
										
								// Remove taken seats.
								for (var i = selectedNames.length - 1; i >= 0; i--) {
									if (takenSeatNames.indexOf(selectedNames[i]) >= 0) {
										selectedNames.splice(i, 1);
									}
								}
								
								that.showReserveDialog(selectedNames)
							});
						}
					})
				});
			},
			showUnlockDialog() {
				this.dialogText = `您确定要解锁吗`
				this.dialogType = "unlock"
				this.$refs.alertDialog.open()
			},
			showReserveDialog(selectedNames) {
				this.dialogType = "reserve"
				this.dialogText = `您确定要预约${selectedNames}, 价格为3元/小时`
				this.$refs.alertDialog.open()
			},
			unlock() {
				// TODO 解锁
			},
			makeTakenRegions(takenSeatNames) {
				var regions = [];
				for (var i = 0; i < takenSeatNames.length; i++) {
				  regions.push({
					name: takenSeatNames[i],
					silent: true,
					itemStyle: {
					  color: '#bf0e08'
					},
					emphasis: {
					  itemStyle: {
						borderColor: '#aaa',
						borderWidth: 1
					  }
					},
					select: {
					  itemStyle: {
						color: '#bf0e08'
					  }
					}
				  });
				}
				return regions;
			},
			dialogConfirm() {
				if(this.dialogType == "reserve") {
					//TODO
					this.msgType = "success"
					this.messageText = "您已成功预约"
					this.$refs.message.open()
				} else {
					//TODO
					this.msgType = "success"
					this.messageText = "您已成功解锁"
					this.$refs.message.open()
				}
			},
			dialogClose() {
				this.msgType = "warn"
				this.messageText = "您已取消"
				this.$refs.message.open()
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
