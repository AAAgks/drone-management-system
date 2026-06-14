<!-- ================================================================
     DroneDetail.vue — 无人机详情弹窗组件。
     以模态框形式展示单条无人机的全部字段。
     打开时禁止背景滚动，关闭时恢复。
     ================================================================ -->
<template>
  <div class="modal" tabindex="-1" ref="modal" style="display:block; background: rgba(0,0,0,0.5);">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" @click="$emit('close')">&times;</button>
          <h4 class="modal-title">无人机详情</h4>
        </div>
        <div class="modal-body">
          <table class="table table-bordered">
            <tr><th width="150">名称</th><td>{{ drone.name }}</td></tr>
            <tr><th>型号</th><td>{{ drone.model }}</td></tr>
            <tr><th>序列号</th><td>{{ drone.serialNumber }}</td></tr>
            <tr><th>制造商</th><td>{{ drone.manufacturer }}</td></tr>
            <tr><th>类型</th><td>{{ drone.type }}</td></tr>
            <tr><th>重量</th><td>{{ drone.weight }} kg</td></tr>
            <tr><th>最大速度</th><td>{{ drone.maxSpeed }} km/h</td></tr>
            <tr><th>最大高度</th><td>{{ drone.maxAltitude }} m</td></tr>
            <tr><th>续航时间</th><td>{{ drone.endurance }} min</td></tr>
            <tr><th>最大航程</th><td>{{ drone.rangeDistance }} km</td></tr>
            <tr><th>最大载重</th><td>{{ drone.maxPayload }} kg</td></tr>
            <tr><th>相机类型</th><td>{{ drone.cameraType }}</td></tr>
            <tr><th>电池容量</th><td>{{ drone.batteryCapacity }} mAh</td></tr>
            <tr><th>GPS精度</th><td>{{ drone.gpsAccuracy }} cm</td></tr>
            <tr><th>工作温度</th><td>{{ drone.operatingTemperature }}</td></tr>
            <tr><th>防水等级</th><td>{{ drone.waterproofLevel }}</td></tr>
            <tr><th>状态</th><td>{{ drone.statusText || (drone.status === 1 ? '正常' : '停用') }}</td></tr>
            <tr><th>购入日期</th><td>{{ drone.purchaseDate }}</td></tr>
            <tr><th>描述</th><td>{{ drone.description }}</td></tr>
          </table>
        </div>
        <div class="modal-footer">
          <button class="btn btn-default" @click="$emit('close')">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DroneDetail',
  props: { drone: Object },  // 父组件传入的无人机数据
  emits: ['close'],
  mounted() {
    document.body.style.overflow = 'hidden'  // 打开弹窗时禁止背景滚动
  },
  beforeUnmount() {
    document.body.style.overflow = ''        // 关闭弹窗时恢复滚动
  }
}
</script>
