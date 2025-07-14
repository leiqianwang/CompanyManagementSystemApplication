<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="数据库名称" prop="dbName">
        <el-input
          v-model="queryParams.dbName"
          placeholder="请输入数据库名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据库类型" prop="dbType">
        <el-select v-model="queryParams.dbType" placeholder="请选择数据库类型" clearable>
          <el-option
            v-for="dict in dict.type.vector_db_type"
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
          v-hasPermi="['ai:vectorDatabase:add']"
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
          v-hasPermi="['ai:vectorDatabase:edit']"
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
          v-hasPermi="['ai:vectorDatabase:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ai:vectorDatabase:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-connection"
          size="mini"
          :disabled="single"
          @click="handleTest"
          v-hasPermi="['ai:vectorDatabase:test']"
        >测试连接</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vectorDbList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" width="80" />
      <el-table-column label="数据库名称" align="center" prop="dbName" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="数据库类型" align="center" prop="dbType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.vector_db_type" :value="scope.row.dbType"/>
        </template>
      </el-table-column>
      <el-table-column label="连接地址" align="center" prop="connectionUrl" width="200" :show-overflow-tooltip="true" />
      <el-table-column label="API密钥" align="center" prop="apiKey" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.apiKey">{{ scope.row.apiKey.substring(0, 8) }}****</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="用户名" align="center" prop="username" width="100" />
      <el-table-column label="数据库名" align="center" prop="databaseName" width="120" />
      <el-table-column label="集合名称" align="center" prop="collectionName" width="120" />
      <el-table-column label="向量维度" align="center" prop="dimension" width="100" />
      <el-table-column label="最大连接数" align="center" prop="maxConnections" width="110" />
      <el-table-column label="超时时间" align="center" prop="timeout" width="110">
        <template slot-scope="scope">
          {{ scope.row.timeout }}ms
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="active" width="100">
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:vectorDatabase:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-connection"
            @click="handleTest(scope.row)"
            v-hasPermi="['ai:vectorDatabase:test']"
          >测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:vectorDatabase:remove']"
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

    <!-- 添加或修改向量数据库资源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库名称" prop="dbName">
              <el-input v-model="form.dbName" placeholder="请输入数据库名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="dbType">
              <el-select v-model="form.dbType" placeholder="请选择数据库类型" style="width: 100%">
                <el-option label="Chroma" value="CHROMA" />
                <el-option label="Pinecone" value="PINECONE" />
                <el-option label="Milvus" value="MILVUS" />
                <el-option label="Weaviate" value="WEAVIATE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="连接地址" prop="connectionUrl">
              <el-input v-model="form.connectionUrl" placeholder="请输入连接地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="API密钥" prop="apiKey">
              <el-input v-model="form.apiKey" type="password" placeholder="请输入API密钥（可选）" show-password />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名（可选）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" type="password" placeholder="请输入密码（可选）" show-password />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库名" prop="databaseName">
              <el-input v-model="form.databaseName" placeholder="请输入数据库名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="集合名称" prop="collectionName">
              <el-input v-model="form.collectionName" placeholder="请输入集合名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="向量维度" prop="dimension">
              <el-input-number v-model="form.dimension" :min="1" :max="10000" placeholder="向量维度" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最大连接数" prop="maxConnections">
              <el-input-number v-model="form.maxConnections" :min="1" :max="100" placeholder="最大连接数" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时间(ms)" prop="timeout">
              <el-input-number v-model="form.timeout" :min="1000" :max="300000" placeholder="超时时间" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
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
  </div>
</template>

<script>
import { listVectorDb, getVectorDb, delVectorDb, addVectorDb, updateVectorDb, testVectorDb } from "@/api/ai/vectorDatabase"

export default {
  name: "VectorDatabase",
  dicts: ['vector_db_type', 'sys_normal_disable'],
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
      // 向量数据库资源表格数据
      vectorDbList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dbName: null,
        dbType: null,
        active: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dbName: [
          { required: true, message: "数据库名称不能为空", trigger: "blur" }
        ],
        dbType: [
          { required: true, message: "数据库类型不能为空", trigger: "change" }
        ],
        connectionUrl: [
          { required: true, message: "连接地址不能为空", trigger: "blur" }
        ],
        dimension: [
          { required: true, message: "向量维度不能为空", trigger: "blur" },
          { type: 'number', min: 1, max: 10000, message: '向量维度必须在1-10000之间', trigger: 'blur' }
        ],
        maxConnections: [
          { type: 'number', min: 1, max: 100, message: '最大连接数必须在1-100之间', trigger: 'blur' }
        ],
        timeout: [
          { type: 'number', min: 1000, max: 300000, message: '超时时间必须在1000-300000毫秒之间', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询向量数据库资源列表 */
    getList() {
      this.loading = true;
      listVectorDb(this.queryParams).then(response => {
        this.vectorDbList = response.rows;
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
        dbName: null,
        dbType: null,
        connectionUrl: null,
        apiKey: null,
        username: null,
        password: null,
        databaseName: null,
        collectionName: null,
        dimension: 1536,
        maxConnections: 10,
        timeout: 30000,
        active: "0",
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
      this.title = "添加向量数据库资源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getVectorDb(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改向量数据库资源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateVectorDb(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVectorDb(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除向量数据库资源编号为"' + ids + '"的数据项？').then(function() {
        return delVectorDb(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ai/vectorDb/export', {
        ...this.queryParams
      }, `vectorDb_${new Date().getTime()}.xlsx`)
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.active === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.dbName + '"吗？').then(function() {
        return updateVectorDb(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.active = row.active === "0" ? "1" : "0";
      });
    },
    /** 测试连接 */
    handleTest(row) {
      const id = row ? row.id : this.ids[0];
      if (!id) {
        this.$modal.msgWarning("请选择要测试的数据库");
        return;
      }
      this.$modal.loading("正在测试连接...");
      testVectorDb(id).then(response => {
        this.$modal.closeLoading();
        this.$modal.msgSuccess("连接测试成功");
      }).catch(error => {
        this.$modal.closeLoading();
        this.$modal.msgError("连接测试失败: " + (error.msg || error.message));
      });
    }
  }
};
</script>
