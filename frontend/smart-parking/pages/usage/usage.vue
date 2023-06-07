<template>
	<view>
		<uni-row class="demo-uni-row" style="margin-left: 10%;">
			<uni-col :span="18">
				<uni-forms-item label="时间范围(小时)">
					<uni-easyinput v-model="range"/>
				</uni-forms-item>
			</uni-col>
			<uni-col :span="6">
				<button type="primary" size="mini" @click="getData()" style="margin-left: 10px;margin-top: 3px;">提交</button>
			</uni-col>
		</uni-row>
		
		<view class="uni-container" style="margin-left: 3%;margin-right: 3%;">
			<uni-table ref="table" :loading="loading" border stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="150" align="center">车位</uni-th>
					<uni-th width="150" align="center">使用率</uni-th>
				</uni-tr>
				<uni-tr v-for="(value, key) in tableData" :key="index">
					<uni-td>{{ key }}</uni-td>
					<uni-td>{{ value }}</uni-td>
				</uni-tr>
			</uni-table>
		</view>
	</view>
</template>

<script>
	import ajax from 'common/ajax.js'
	export default {
		data() {
			return {
				range: 24,
				tableData: null,
				loading: true,
			}
		},
		mounted() {
			this.getData()
		},
		methods: {
			getData() {
				ajax.get({
					url: "/parkingOrder/usage/" + this.range
				}).then(res => {
					this.tableData = res.data.data
					this.loading = false
					console.log(usage)
				})
			}
		}
	}
</script>

<style>

</style>
