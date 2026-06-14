<!-- ================================================================
     DroneForm.vue — 无人机新增/编辑表单组件（模态框形式）。
     支持：
     1. 手动填写全部无人机属性
     2. AI 智能填充：输入名称和描述后，自动生成技术参数
     提交时通过 /api/drones 的 POST（新增）或 PUT（编辑）保存。
     ================================================================ -->
<template>
  <div class="modal" tabindex="-1" style="display:block; background: rgba(0,0,0,0.5);">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" @click="$emit('cancel')">&times;</button>
          <h4 class="modal-title">{{ isEdit ? '编辑无人机' : '新增无人机' }}</h4>
        </div>
        <div class="modal-body">
          <!-- 错误提示 -->
          <div v-if="error" class="alert alert-danger alert-dismissible" style="margin-bottom:15px;">
            <button type="button" class="close" @click="error = ''">&times;</button>
            {{ error }}
          </div>
          <!-- ===== 基本信息 ===== -->
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <label>名称 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" v-model="form.name" />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label>型号 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" v-model="form.model" />
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <label>序列号</label>
                <input type="text" class="form-control" v-model="form.serialNumber" />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label>制造商</label>
                <input type="text" class="form-control" v-model="form.manufacturer" />
              </div>
            </div>
          </div>
          <!-- ===== 技术参数 ===== -->
          <div class="row">
            <div class="col-md-4">
              <label>类型</label>
              <select class="form-control" v-model="form.type">
                <option value="">请选择</option>
                <option value="多旋翼">多旋翼</option>
                <option value="固定翼">固定翼</option>
                <option value="直升机">直升机</option>
                <option value="垂直起降">垂直起降</option>
              </select>
            </div>
            <div class="col-md-4">
              <label>重量(kg)</label>
              <input type="number" step="0.01" class="form-control" v-model="form.weight" />
            </div>
            <div class="col-md-4">
              <label>最大速度(km/h)</label>
              <input type="number" step="0.1" class="form-control" v-model="form.maxSpeed" />
            </div>
          </div>
          <div class="row" style="margin-top: 10px;">
            <div class="col-md-4">
              <label>最大高度(m)</label>
              <input type="number" step="0.1" class="form-control" v-model="form.maxAltitude" />
            </div>
            <div class="col-md-4">
              <label>续航(min)</label>
              <input type="number" step="0.1" class="form-control" v-model="form.endurance" />
            </div>
            <div class="col-md-4">
              <label>最大航程(km)</label>
              <input type="number" step="0.1" class="form-control" v-model="form.rangeDistance" />
            </div>
          </div>
          <div class="row" style="margin-top: 10px;">
            <div class="col-md-4">
              <label>最大载重(kg)</label>
              <input type="number" step="0.01" class="form-control" v-model="form.maxPayload" />
            </div>
            <div class="col-md-4">
              <label>相机类型</label>
              <input type="text" class="form-control" v-model="form.cameraType" />
            </div>
            <div class="col-md-4">
              <label>电池容量(mAh)</label>
              <input type="number" class="form-control" v-model="form.batteryCapacity" />
            </div>
          </div>
          <div class="row" style="margin-top: 10px;">
            <div class="col-md-4">
              <label>GPS精度(cm)</label>
              <input type="number" step="0.1" class="form-control" v-model="form.gpsAccuracy" />
            </div>
            <div class="col-md-4">
              <label>工作温度</label>
              <input type="text" class="form-control" v-model="form.operatingTemperature" />
            </div>
            <div class="col-md-4">
              <label>防水等级</label>
              <select class="form-control" v-model="form.waterproofLevel">
                <option value="">请选择</option>
                <option value="IP43">IP43</option>
                <option value="IP54">IP54</option>
                <option value="IP55">IP55</option>
                <option value="IP65">IP65</option>
                <option value="IP67">IP67</option>
              </select>
            </div>
          </div>
          <!-- ===== 其他信息 ===== -->
          <div class="row" style="margin-top: 10px;">
            <div class="col-md-6">
              <label>状态</label>
              <select class="form-control" v-model="form.status">
                <option value="正常">正常</option>
                <option value="停用">停用</option>
              </select>
            </div>
            <div class="col-md-6">
              <label>购入日期</label>
              <input type="date" class="form-control" v-model="form.purchaseDate" />
            </div>
          </div>
          <div class="form-group" style="margin-top: 10px;">
            <label>描述 <span class="text-primary">(AI 生成：填好名称和描述后，点击下方按钮自动填充)</span></label>
            <textarea class="form-control" rows="3" v-model="form.description"></textarea>
          </div>
          <!-- AI 智能填充按钮 -->
          <button type="button" class="btn btn-info" @click="aiGenerate" :disabled="generating" style="margin-bottom:10px;">
            <span v-if="generating">AI 生成中...</span>
            <span v-else>AI 智能填充其他字段</span>
          </button>
        </div>
        <div class="modal-footer">
          <button class="btn btn-default" @click="$emit('cancel')">取消</button>
          <button class="btn btn-primary" @click="submit" :disabled="submitting">
            <span v-if="submitting">保存中...</span>
            <span v-else>保存</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/**
 * 表单逻辑：
 * - 编辑模式：父组件传入 drone prop，自动回填到 form
 * - 新增模式：drone 为 null，form 为空
 * - AI 生成：调用 /api/drones/ai-generate 自动填充参数
 * - 提交：根据 isEdit 决定用 PUT 还是 POST
 */
import { reactive, ref, watch } from 'vue'
import { createDrone, updateDrone, aiGenerate as apiAiGenerate } from '../api/drones'

export default {
  name: 'DroneForm',
  props: { drone: Object, isEdit: Boolean },
  emits: ['cancel', 'saved'],
  setup(props, { emit }) {
    // 表单数据对象
    const form = reactive({
      name: '', model: '', serialNumber: '', manufacturer: '', type: '',
      weight: null, maxSpeed: null, maxAltitude: null, endurance: null,
      rangeDistance: null, maxPayload: null, cameraType: '', batteryCapacity: null,
      gpsAccuracy: null, operatingTemperature: '', waterproofLevel: '',
      status: '正常', description: '', purchaseDate: ''
    })
    const generating = ref(false)   // AI 生成中
    const error      = ref('')      // 表单级错误提示
    const submitting = ref(false)   // 提交中

    // 监听 drone prop 变化（编辑模式下回填数据）
    watch(() => props.drone, (val) => {
      if (val) {
        Object.assign(form, val)
        // 将数字状态映射回中文文本
        if (val.statusText) {
          form.status = val.statusText
        } else if (val.status === 1 || val.status === '1') {
          form.status = '正常'
        } else if (val.status === 0 || val.status === '0') {
          form.status = '停用'
        }
      }
    }, { immediate: true })

    /**
     * AI 智能填充：调用后端 AI 接口，将返回数据自动填入表单。
     */
    async function aiGenerate() {
      if (!form.name) { error.value = '请先输入无人机名称'; return }
      generating.value = true
      error.value = ''
      try {
        const d = await apiAiGenerate({ name: form.name, description: form.description || '' })
        Object.assign(form, {
          model: d.model || '',
          serialNumber: d.serialNumber || '',
          manufacturer: d.manufacturer || '',
          type: d.type || '',
          weight: d.weight,
          maxSpeed: d.maxSpeed,
          maxAltitude: d.maxAltitude,
          endurance: d.endurance,
          rangeDistance: d.rangeDistance,
          maxPayload: d.maxPayload,
          cameraType: d.cameraType || '',
          batteryCapacity: d.batteryCapacity,
          gpsAccuracy: d.gpsAccuracy,
          operatingTemperature: d.operatingTemperature || '',
          waterproofLevel: d.waterproofLevel || '',
          status: d.statusText || '正常',
          purchaseDate: d.purchaseDate || '',
        })
      } catch(e) {
        error.value = 'AI 生成失败（请确认后端服务已启动）: ' + e.message
      } finally {
        generating.value = false
      }
    }

    /**
     * 提交表单。
     * 新增 → POST /api/drones，编辑 → PUT /api/drones/{id}
     */
    async function submit() {
      if (!props.isEdit) {
        if (!form.name) { error.value = '请输入无人机名称'; return }
        if (!form.model) { error.value = '请先填写型号'; return }
      }
      submitting.value = true
      error.value = ''
      try {
        if (props.isEdit) {
          await updateDrone(form.id, form)
        } else {
          await createDrone(form)
        }
        emit('saved')
      } catch (e) {
        error.value = '保存失败: ' + (e.message || '未知错误')
      } finally {
        submitting.value = false
      }
    }

    return { form, generating, error, submitting, aiGenerate, submit }
  }
}
</script>

<style scoped>
.row { margin-bottom: 0; }
</style>
