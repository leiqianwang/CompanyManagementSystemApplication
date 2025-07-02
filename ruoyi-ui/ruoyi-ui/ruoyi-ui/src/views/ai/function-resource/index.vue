<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="函数编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入函数编号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="函数分类" prop="funcCategory">
        <el-select v-model="queryParams.funcCategory" placeholder="请选择函数分类" clearable>
          <el-option
            v-for="dict in dict.type.func_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="实现方式" prop="implMethod">
        <el-select v-model="queryParams.implMethod" placeholder="请选择实现方式" clearable>
          <el-option
            v-for="dict in dict.type.impl_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="active">
        <el-select v-model="queryParams.active" placeholder="状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['ai:function:add']"
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
          v-hasPermi="['ai:function:edit']"
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
          v-hasPermi="['ai:function:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-cpu"
          size="mini"
          @click="handleTest"
          v-hasPermi="['ai:function:test']"
        >测试函数</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="functionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="函数编号" align="center" prop="number" :show-overflow-tooltip="true" />
      <el-table-column label="函数描述" align="center" prop="description" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="函数分类" align="center" prop="funcCategory" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.func_category" :value="scope.row.funcCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="实现方式" align="center" prop="implMethod" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.impl_method" :value="scope.row.implMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="参数管理" align="center" prop="paramManagement" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="状态" align="center" prop="active" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.active"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:function:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-cpu"
            @click="handleTest(scope.row)"
            v-hasPermi="['ai:function:test']"
          >测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:function:remove']"
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

    <!-- 添加或修改函数资源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="函数编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入函数编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="函数分类" prop="funcCategory">
              <el-select v-model="form.funcCategory" placeholder="请选择函数分类" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.func_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="函数描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入函数描述" :rows="3" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="实现方式" prop="implMethod">
              <el-select v-model="form.implMethod" placeholder="请选择实现方式" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.impl_method"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="active">
              <el-radio-group v-model="form.active">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="参数管理" prop="paramManagement">
              <el-input v-model="form.paramManagement" type="textarea" placeholder="请输入参数管理配置(JSON格式)" :rows="4" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 函数测试对话框 -->
    <el-dialog title="函数测试" :visible.sync="testOpen" width="600px" append-to-body>
      <el-form ref="testForm" :model="testForm" label-width="100px">
        <el-form-item label="函数编号">
          <el-input v-model="testForm.number" disabled />
        </el-form-item>
        <el-form-item label="测试参数">
          <el-input v-model="testForm.testParams" type="textarea" placeholder="请输入测试参数(JSON格式)" :rows="4" />
        </el-form-item>
        <el-form-item label="执行结果">
          <el-input v-model="testForm.result" type="textarea" disabled :rows="6" />
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
import { listFunctionResource, getFunctionResource, delFunctionResource, addFunctionResource, updateFunctionResource, testFunctionResource } from "@/api/ai/functionResource"

export default {
  name: "FunctionResource",
  dicts: ['func_category', 'impl_method', 'sys_normal_disable'],
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
      // 函数资源表格数据
      functionList: [],
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
        number: null,
        funcCategory: null,
        implMethod: null,
        active: null
      },
      // 表单参数
      form: {},
      // 测试表单参数
      testForm: {},
      // 表单校验
      rules: {
        number: [
          { required: true, message: "函数编号不能为空", trigger: "blur" }
        ],
        description: [
          { required: true, message: "函数描述不能为空", trigger: "blur" }
        ],
        funcCategory: [
          { required: true, message: "函数分类不能为空", trigger: "change" }
        ],
        implMethod: [
          { required: true, message: "实现方式不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询函数资源列表 */
    getList() {
      this.loading = true;
      listFunctionResource(this.queryParams).then(response => {
        this.functionList = response.rows;
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
        number: null,
        description: null,
        funcCategory: null,
        implMethod: null,
        paramManagement: null,
        active: "0"
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
      this.title = "添加函数资源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFunctionResource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改函数资源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFunctionResource(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFunctionResource(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除函数资源编号为"' + ids + '"的数据项？').then(function() {
        return delFunctionResource(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 测试函数 */
    handleTest(row) {
      this.testForm = {
        id: row ? row.id : this.ids[0],
        number: row ? row.number : '',
        testParams: '',
        result: ''
      };
      this.testOpen = true;
    },
    /** 执行测试 */
    executeTest() {
      if (!this.testForm.testParams) {
        this.$modal.msgWarning("请输入测试参数");
        return;
      }
      testFunctionResource({
        id: this.testForm.id,
        testParams: this.testForm.testParams
      }).then(response => {
        this.testForm.result = JSON.stringify(response.data, null, 2);
        this.$modal.msgSuccess("测试执行成功");
      }).catch(error => {
        this.testForm.result = "测试执行失败: " + error.message;
      });
    }
  }
};
</script>
