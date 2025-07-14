package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 嵌入模型资源对象 embedding_model_resource
 * 
 * @author ruoyi
 * @date 2025-07-01
 */
public class EmbeddingModelResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 资源名称 */
    @Excel(name = "资源名称")
    private String resourceName;

    /** 资源类型 */
    @Excel(name = "资源类型")
    private String resourceType;

    /** apiKey */
    @Excel(name = "apiKey")
    private String apiKey;

    /** secretKey */
    @Excel(name = "secretKey")
    private String secretKey;

    /** apiUrl */
    @Excel(name = "apiUrl")
    private String apiUrl;

    /** dimension */
    @Excel(name = "dimension")
    private Integer dimension;

    /** 频率限制(次/分钟) */
    @Excel(name = "频率限制", suffix = "次/分钟")
    private Integer frequencyLimit;

    /** 是否启用（0正常 1停用） */
    @Excel(name = "是否启用", readConverterExp = "0=正常,1=停用")
    private String active;

    /** 操作说明 */
    @Excel(name = "操作说明")
    private String operation;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setResourceName(String resourceName) 
    {
        this.resourceName = resourceName;
    }

    public String getResourceName() 
    {
        return resourceName;
    }
    public void setResourceType(String resourceType) 
    {
        this.resourceType = resourceType;
    }

    public String getResourceType() 
    {
        return resourceType;
    }
    public void setApiKey(String apiKey) 
    {
        this.apiKey = apiKey;
    }

    public String getApiKey() 
    {
        return apiKey;
    }
    public void setSecretKey(String secretKey) 
    {
        this.secretKey = secretKey;
    }

    public String getSecretKey() 
    {
        return secretKey;
    }
    public void setApiUrl(String apiUrl) 
    {
        this.apiUrl = apiUrl;
    }

    public String getApiUrl() 
    {
        return apiUrl;
    }
    public void setDimension(Integer dimension) 
    {
        this.dimension = dimension;
    }

    public Integer getDimension() 
    {
        return dimension;
    }
    public void setFrequencyLimit(Integer frequencyLimit) 
    {
        this.frequencyLimit = frequencyLimit;
    }

    public Integer getFrequencyLimit() 
    {
        return frequencyLimit;
    }
    public void setActive(String active) 
    {
        this.active = active;
    }

    public String getActive() 
    {
        return active;
    }
    public void setOperation(String operation) 
    {
        this.operation = operation;
    }

    public String getOperation() 
    {
        return operation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("resourceName", getResourceName())
            .append("resourceType", getResourceType())
            .append("apiKey", getApiKey())
            .append("secretKey", getSecretKey())
            .append("apiUrl", getApiUrl())
            .append("dimension", getDimension())
            .append("frequencyLimit", getFrequencyLimit())
            .append("active", getActive())
            .append("operation", getOperation())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
