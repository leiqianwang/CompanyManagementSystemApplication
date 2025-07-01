<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资源名称" prop="resourceName">
        <el-input
          v-model="queryParams.resourceName"
          placeholder="请输入资源名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源类型" prop="resourceType">
        <el-input
          v-model="queryParams.resourceType"
          placeholder="请输入资源类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="API地址" prop="apiUrl">
        <el-input
          v-model="queryParams.apiUrl"
          placeholder="请输入API地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用" prop="active">
        <el-select v-model="queryParams.active" placeholder="是否启用" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['ai:embeddingModel:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ai:embeddingModel:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ai:embeddingModel:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ai:embeddingModel:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-cpu"
          size="mini"
          :disabled="single"
          @click="handleTest"
          v-hasPermi="['ai:embeddingModel:test']"
        >测试连接</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="embeddingModelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" width="80" />
      <el-table-column label="资源名称" align="center" prop="resourceName" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="资源类型" align="center" prop="resourceType" width="140" />
      <el-table-column label="API地址" align="center" prop="apiUrl" width="220" :show-overflow-tooltip="true" />
      <el-table-column label="API密钥" align="center" prop="apiKey" width="130">
        <template slot-scope="scope">
          <span v-if="scope.row.apiKey">{{ scope.row.apiKey.substring(0, 8) }}****</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="秘密密钥" align="center" prop="secretKey" width="130">
        <template slot-scope="scope">
          <span v-if="scope.row.secretKey">{{ scope.row.secretKey.substring(0, 8) }}****</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="向量维度" align="center" prop="dimension" width="110" />
      <el-table-column label="频率限制" align="center" prop="frequencyLimit" width="130">
        <template slot-scope="scope">
          {{ scope.row.frequencyLimit }}次/分钟
        </template>
      </el-table-column>
      <el-table-column label="是否启用" align="center" prop="active" width="110">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.active"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:embeddingModel:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-cpu"
            @click="handleTest(scope.row)"
            v-hasPermi="['ai:embeddingModel:test']"
          >测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:embeddingModel:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改嵌入模型资源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="资源名称" prop="resourceName">
              <el-input v-model="form.resourceName" placeholder="请输入资源名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="资源类型" prop="resourceType">
              <el-input v-model="form.resourceType" placeholder="请输入资源类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="API密钥" prop="apiKey">
              <el-input v-model="form.apiKey" type="password" placeholder="请输入API密钥" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="秘密密钥" prop="secretKey">
              <el-input v-model="form.secretKey" type="password" placeholder="请输入秘密密钥（可选）" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="API地址" prop="apiUrl">
              <el-input v-model="form.apiUrl" placeholder="请输入API地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="向量维度" prop="dimension">
              <el-input-number v-model="form.dimension" :min="1" :max="10000" placeholder="向量维度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="频率限制" prop="frequencyLimit">
              <el-input-number v-model="form.frequencyLimit" :min="1" :max="1000" placeholder="次/分钟" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="是否启用" prop="active">
              <el-radio-group v-model="form.active">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="操作说明" prop="operation">
              <el-input v-model="form.operation" type="textarea" placeholder="请输入操作说明" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 向量化测试对话框 -->
    <el-dialog title="向量化测试" :visible.sync="testOpen" width="700px" append-to-body>
      <el-form ref="testForm" :model="testForm" label-width="100px">
        <el-form-item label="模型信息">
          <el-input v-model="testForm.modelInfo" disabled />
        </el-form-item>
        <el-form-item label="测试文本">
          <el-input v-model="testForm.testText" type="textarea" placeholder="请输入要进行向量化的测试文本" :rows="4" />
        </el-form-item>
        <el-form-item label="向量结果">
          <el-input v-model="testForm.vectorResult" type="textarea" disabled :rows="8" />
        </el-form-item>
        <el-form-item label="向量维度">
          <el-input v-model="testForm.dimension" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="executeVectorize">执行向量化</el-button>
        <el-button @click="testOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEmbeddingModel, getEmbeddingModel, delEmbeddingModel, addEmbeddingModel, updateEmbeddingModel, testEmbeddingModel } from "@/api/ai/embeddingModel"

export default {
  name: "EmbeddingModel",
  dicts: ['embedding_model_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 嵌入模型资源表格数据
      embeddingModelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示测试对话框
      testOpen: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        resourceName: null,
        resourceType: null,
        apiUrl: null,
        active: null
      },
      // 表单参数
      form: {},
      // 测试表单参数
      testForm: {},
      // 表单校验
      rules: {
        resourceName: [
          { required: true, message: "资源名称不能为空", trigger: "blur" }
        ],
        resourceType: [
          { required: true, message: "资源类型不能为空", trigger: "blur" }
        ],
        apiKey: [
          { required: true, message: "API密钥不能为空", trigger: "blur" }
        ],
        apiUrl: [
          { required: true, message: "API地址不能为空", trigger: "blur" }
        ],
        dimension: [
          { required: true, message: "向量维度不能为空", trigger: "blur" },
          { type: 'number', min: 1, max: 10000, message: '向量维度必须在1-10000之间', trigger: 'blur' }
        ],
        frequencyLimit: [
          { required: true, message: "频率限制不能为空", trigger: "blur" },
          { type: 'number', min: 1, max: 1000, message: '频率限制必须在1-1000之间', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询嵌入模型资源列表 */
    getList() {
      this.loading = true;
      listEmbeddingModel(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.embeddingModelList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        resourceName: null,
        resourceType: null,
        apiKey: null,
        secretKey: null,
        apiUrl: null,
        dimension: null,
        frequencyLimit: null,
        active: "0",
        operation: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加嵌入模型资源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEmbeddingModel(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改嵌入模型资源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEmbeddingModel(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEmbeddingModel(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除嵌入模型资源编号为"' + ids + '"的数据项？').then(function() {
        return delEmbeddingModel(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ai/embeddingModel/export', {
        ...this.queryParams
      }, `embeddingModel_${new Date().getTime()}.xlsx`)
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.active === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.resourceName + '"吗？').then(function() {
        return updateEmbeddingModel(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.active = row.active === "0" ? "1" : "0";
      });
    },
    /** 向量化测试 */
    handleTest(row) {
      this.testForm = {
        id: row ? row.id : this.ids[0],
        modelInfo: row ? `${row.resourceName} (${row.resourceType})` : '',
        testText: '',
        vectorResult: '',
        dimension: ''
      };
      this.testOpen = true;
    },
    /** 执行向量化 */
    executeVectorize() {
      if (!this.testForm.testText) {
        this.$modal.msgWarning("请输入测试文本");
        return;
      }
      testEmbeddingModel(this.testForm.id, {
        text: this.testForm.testText
      }).then(response => {
        this.testForm.vectorResult = JSON.stringify(response.data.vector, null, 2);
        this.testForm.dimension = response.data.dimension;
        this.$modal.msgSuccess("向量化执行成功");
      }).catch(error => {
        this.testForm.vectorResult = "向量化执行失败: " + error.message;
      });
    }
  }
};
</script>
