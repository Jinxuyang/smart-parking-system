<template>
	<view >
		<view style="width: 100vw; height: 80vh; ">
			<l-echart ref="chart"></l-echart>
		</view>
		<button @click="save">保存</button>
	</view>		
</template>

<script>
	import * as echarts from '@/uni_modules/lime-echart/static/echarts.min';
	// import * as echarts from 'echarts/core';
	// import {LineChart} from 'echarts/charts';
	// import {TitleComponent,TooltipComponent,GridComponent, TransformComponent, LegendComponent } from 'echarts/components';
	// // 标签自动布局，全局过渡动画等特性
	// import {LabelLayout,UniversalTransition} from 'echarts/features';
	// // 引入 Canvas 渲染器，注意引入 CanvasRenderer 是必须的一步
	// import {CanvasRenderer} from 'echarts/renderers';
	
	// // 按需引入 注册必须的组件
	// echarts.use([
	//     LegendComponent,
	//     TitleComponent,
	//     TooltipComponent,
	//     GridComponent,
	//     TransformComponent,
	//     LineChart,
	//     LabelLayout,
	//     UniversalTransition,
	//     CanvasRenderer
	// ]);
	export default {
		data() {
			return {
				option:  {
					tooltip: {
						trigger: 'axis',
						// shadowBlur: 0,
						textStyle: {
							textShadowBlur : 0
						}
					},
					legend: {
						data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
					},
					yAxis: {
						type: 'value'
					},
					series: [
						{
							name: '邮件营销',
							type: 'line',
							stack: '总量',
							data: [120, 132, 101, 134, 90, 230, 210]
						},
						{
							name: '联盟广告',
							type: 'line',
							stack: '总量',
							data: [220, 182, 191, 234, 290, 330, 310]
						},
						{
							name: '视频广告',
							type: 'line',
							stack: '总量',
							data: [150, 232, 201, 154, 190, 330, 410]
						},
						{
							name: '直接访问',
							type: 'line',
							stack: '总量',
							data: [320, 332, 301, 334, 390, 330, 320]
						},
						{
							name: '搜索引擎',
							type: 'line',
							stack: '总量',
							data: [820, 932, 901, 934, 1290, 1330, 1320]
						}
					]
				}
			}
		},
		mounted() {
			this.$refs.chart.init(echarts, chart => {
				chart.setOption(this.option);
			});
		},
		methods: {
			save() {
				this.$refs.chart.canvasToTempFilePath({
					success(res) {
						console.log('res', res)
					}
				})
			}
		}
	}
</script>
