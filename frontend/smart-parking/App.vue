<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	@import './common/uni.css';
	@import './static/customicons.css';
	
	uni-page-body {
		background-color: #F5F5F5 !important;
		min-height: 100% !important;
		height: auto !important;
	}
	
	.uni-top-window uni-tabbar .uni-tabbar {
		background-color: #fff !important;
	}
	
	.uni-app--showleftwindow .hideOnPc {
		display: none !important;
	}
</style>
