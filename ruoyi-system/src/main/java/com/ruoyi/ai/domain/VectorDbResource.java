package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 向量数据库资源对象 vector_db_resource
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public class VectorDbResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 数据库名称 */
    @Excel(name = "数据库名称")
    private String dbName;

    /** 数据库类型 */
    @Excel(name = "数据库类型")
    private String dbType;

    /** 连接地址 */
    @Excel(name = "连接地址")
    private String connectionUrl;

    /** API密钥 */
    @Excel(name = "API密钥")
    private String apiKey;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    private String password;

    /** 数据库名 */
    @Excel(name = "数据库名")
    private String databaseName;

    /** 集合名称 */
    @Excel(name = "集合名称")
    private String collectionName;

    /** 向量维度 */
    @Excel(name = "向量维度")
    private Integer dimension;

    /** 最大连接数 */
    @Excel(name = "最大连接数")
    private Integer maxConnections;

    /** 超时时间(毫秒) */
    @Excel(name = "超时时间", readConverterExp = "毫=秒")
    private Integer timeout;

    /** 是否启用（0正常 1停用） */
    @Excel(name = "是否启用", readConverterExp = "0=正常,1=停用")
    private String active;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDbName(String dbName) 
    {
        this.dbName = dbName;
    }

    public String getDbName() 
    {
        return dbName;
    }
    public void setDbType(String dbType) 
    {
        this.dbType = dbType;
    }

    public String getDbType() 
    {
        return dbType;
    }
    public void setConnectionUrl(String connectionUrl) 
    {
        this.connectionUrl = connectionUrl;
    }

    public String getConnectionUrl() 
    {
        return connectionUrl;
    }
    public void setApiKey(String apiKey) 
    {
        this.apiKey = apiKey;
    }

    public String getApiKey() 
    {
        return apiKey;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setDatabaseName(String databaseName) 
    {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() 
    {
        return databaseName;
    }
    public void setCollectionName(String collectionName) 
    {
        this.collectionName = collectionName;
    }

    public String getCollectionName() 
    {
        return collectionName;
    }
    public void setDimension(Integer dimension) 
    {
        this.dimension = dimension;
    }

    public Integer getDimension() 
    {
        return dimension;
    }
    public void setMaxConnections(Integer maxConnections) 
    {
        this.maxConnections = maxConnections;
    }

    public Integer getMaxConnections() 
    {
        return maxConnections;
    }
    public void setTimeout(Integer timeout) 
    {
        this.timeout = timeout;
    }

    public Integer getTimeout() 
    {
        return timeout;
    }
    public void setActive(String active) 
    {
        this.active = active;
    }

    public String getActive() 
    {
        return active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dbName", getDbName())
            .append("dbType", getDbType())
            .append("connectionUrl", getConnectionUrl())
            .append("apiKey", getApiKey())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("databaseName", getDatabaseName())
            .append("collectionName", getCollectionName())
            .append("dimension", getDimension())
            .append("maxConnections", getMaxConnections())
            .append("timeout", getTimeout())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("active", getActive())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
