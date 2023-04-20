<template>
	<view class="content">
		<view><l-echart ref="chart" @finished="init"></l-echart></view>
	</view>
</template>

<script>
	import * as echarts from 'echarts/core';
	import { TooltipComponent, GeoComponent } from 'echarts/components';
	import { CanvasRenderer } from 'echarts/renderers';
	
	echarts.use([TooltipComponent, GeoComponent, CanvasRenderer]);
	var takenSeatNames = ['a1'];
	export default {
	    data() {
	        return {
				option: {
				    tooltip: {},
				    geo: {
				      map: 'flight-seats',
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
				}
			}
	    },
	    
	    mounted() {},
		methods: {
			init() {
				this.$refs.chart.resize({width: 1000, height: 500})
				this.$refs.chart.init(echarts, chart => {
					uni.request({
						url:"static/flight-seats.svg",
						method:'GET',
						success: (svg) => {
							console.log(svg.data)
							echarts.registerMap('flight-seats', {
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
										
								console.log('selected', selectedNames);
							});
						}
					})
				});
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
