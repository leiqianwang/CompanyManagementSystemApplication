<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="模型名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入模型名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型类型" prop="modelType">
        <el-select v-model="queryParams.modelType" placeholder="请选择模型类型" clearable>
          <el-option
            v-for="dict in dict.type.embedding_model_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="版本" prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入版本"
          clearable
          style="width: 150px"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['ai:embedding:add']"
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
          v-hasPermi="['ai:embedding:edit']"
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
          v-hasPermi="['ai:embedding:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ai:embedding:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-cpu"
          size="mini"
          @click="handleTest"
          v-hasPermi="['ai:embedding:test']"
        >测试向量化</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="embeddingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="模型名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="模型类型" align="center" prop="modelType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.embedding_model_type" :value="scope.row.modelType"/>
        </template>
      </el-table-column>
      <el-table-column label="版本" align="center" prop="version" width="100" />
      <el-table-column label="组合键" align="center" width="200">
        <template slot-scope="scope">
          <el-tag type="info" size="mini">
            {{scope.row.name}}-{{scope.row.modelType}}-{{scope.row.version}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:embedding:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-cpu"
            @click="handleTest(scope.row)"
            v-hasPermi="['ai:embedding:test']"
          >测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:embedding:remove']"
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
            <el-form-item label="模型名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入模型名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="模型类型" prop="modelType">
              <el-select v-model="form.modelType" placeholder="请选择模型类型" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.embedding_model_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本" prop="version">
              <el-input v-model="form.version" placeholder="请输入版本号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-alert
              title="唯一性约束"
              type="info"
              description="模型名称、模型类型、版本的组合必须唯一"
              :closable="false"
              show-icon>
            </el-alert>
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
      embeddingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示测试对话框
      testOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        modelType: null,
        version: null
      },
      // 表单参数
      form: {},
      // 测试表单参数
      testForm: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "模型名称不能为空", trigger: "blur" }
        ],
        modelType: [
          { required: true, message: "模型类型不能为空", trigger: "change" }
        ],
        version: [
          { required: true, message: "版本不能为空", trigger: "blur" }
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
      listEmbeddingModel(this.queryParams).then(response => {
        this.embeddingList = response.rows;
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
        name: null,
        modelType: null,
        version: null
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
    /** 向量化测试 */
    handleTest(row) {
      this.testForm = {
        id: row ? row.id : this.ids[0],
        modelInfo: row ? `${row.name}-${row.modelType}-${row.version}` : '',
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
      testEmbeddingModel({
        id: this.testForm.id,
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
