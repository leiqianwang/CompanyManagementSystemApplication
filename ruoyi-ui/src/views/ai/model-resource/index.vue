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
      <el-form-item label="状态" prop="active">
        <el-select v-model="queryParams.active" placeholder="状态" clearable>
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
          v-hasPermi="['ai:modelResource:add']"
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
          v-hasPermi="['ai:modelResource:edit']"
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
          v-hasPermi="['ai:modelResource:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ai:modelResource:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-cpu"
          size="mini"
          @click="handleTest"
          v-hasPermi="['ai:modelResource:test']"
        >测试连接</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelResourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资源ID" align="center" prop="id" />
      <el-table-column label="资源名称" align="center" prop="resourceName" />
      <el-table-column label="资源类型" align="center" prop="resourceType" />
      <el-table-column label="默认模型" align="center" prop="defaultModel" />
      <el-table-column label="API地址" align="center" prop="apiUrl" />
      <el-table-column label="频率限制" align="center" prop="frequencyLimit" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.frequencyLimit || 0 }} 次/分</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="active">
        <template slot-scope="scope">
          <el-tag :type="scope.row.active === '0' ? 'success' : 'danger'">
            {{ scope.row.active === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="creationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.creationTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:modelResource:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-cpu"
            @click="handleTest(scope.row)"
            v-hasPermi="['ai:modelResource:test']"
          >测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:modelResource:remove']"
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

    <!-- 添加或修改AI模型资源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="资源名称" prop="resourceName">
              <el-input v-model="form.resourceName" placeholder="请输入资源名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源类型" prop="resourceType">
              <el-input v-model="form.resourceType" placeholder="请输入资源类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="默认模型" prop="defaultModel">
              <el-input v-model="form.defaultModel" placeholder="请输入默认模型名称" />
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
            <el-form-item label="API Key" prop="apiKey">
              <el-input v-model="form.apiKey" type="password" placeholder="请输入API Key" show-password />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Secret Key" prop="secretKey">
              <el-input v-model="form.secretKey" type="password" placeholder="请输入Secret Key" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="频率限制" prop="frequencyLimit">
              <el-input-number 
                v-model="form.frequencyLimit" 
                :min="0" 
                :max="10000"
                placeholder="请求次数"
                style="width: 70%" 
              />
              <span style="margin-left: 8px; color: #909399;">次/分钟</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="active">
              <el-radio-group v-model="form.active">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="扩展字段" prop="extraKey">
              <el-input v-model="form.extraKey" type="textarea" placeholder="请输入扩展配置信息(JSON格式)" :rows="3" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="操作说明" prop="operation">
              <el-input v-model="form.operation" type="textarea" placeholder="请输入操作说明" :rows="2" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 测试连接对话框 -->
    <el-dialog title="测试连接" :visible.sync="testOpen" width="500px" append-to-body>
      <el-form ref="testForm" :model="testForm" label-width="100px">
        <el-form-item label="资源名称">
          <el-input v-model="testForm.resourceName" disabled />
        </el-form-item>
        <el-form-item label="API地址">
          <el-input v-model="testForm.apiUrl" disabled />
        </el-form-item>
        <el-form-item label="测试结果">
          <el-input v-model="testForm.result" type="textarea" disabled :rows="4" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="executeTest">执行测试</el-button>
        <el-button @click="testOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listModelResource, getModelResource, delModelResource, addModelResource, updateModelResource, testModelResource } from "@/api/ai/modelResource"
import { addDateRange, parseTime } from "@/utils/ruoyi"

export default {
  name: "ModelResource",
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
      // AI模型资源表格数据
      modelResourceList: [],
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
        apiUrl: [
          { required: true, message: "API地址不能为空", trigger: "blur" },
          {
            pattern: /^https?:\/\/.+/,
            message: "请输入正确的URL地址",
            trigger: "blur"
          }
        ],
        apiKey: [
          { required: true, message: "API Key不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询AI模型资源列表 */
    getList() {
      this.loading = true;
      listModelResource(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.modelResourceList = response.rows;
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
        defaultModel: null,
        apiKey: null,
        secretKey: null,
        apiUrl: null,
        frequencyLimit: 100,
        creationTime: null,
        active: "0",
        operation: null,
        extraKey: null
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
      this.title = "添加AI模型资源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getModelResource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改AI模型资源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateModelResource(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addModelResource(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除AI模型资源编号为"' + ids + '"的数据项？').then(function() {
        return delModelResource(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ai/modelResource/export', {
        ...this.queryParams
      }, `modelResource_${new Date().getTime()}.xlsx`)
    },
    /** 测试连接 */
    handleTest(row) {
      this.testForm = {
        id: row ? row.id : this.ids[0],
        resourceName: row ? row.resourceName : '',
        apiUrl: row ? row.apiUrl : '',
        result: ''
      };
      this.testOpen = true;
    },
    /** 执行测试 */
    executeTest() {
      testModelResource(this.testForm.id).then(response => {
        this.testForm.result = "连接测试成功！\n" + JSON.stringify(response.data, null, 2);
        this.$modal.msgSuccess("连接测试成功");
      }).catch(error => {
        this.testForm.result = "连接测试失败: " + (error.message || "未知错误");
        this.$modal.msgError("连接测试失败");
      });
    }
  }
};
</script>
