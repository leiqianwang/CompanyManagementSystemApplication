package com.ruoyi.ai.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI模型资源对象 ai_model_resource
 * 
 * @author ruoyi
 * @date 2025-06-30
 */
public class AiModelResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源ID */
    private Long id;

    /** 资源名称 */
    @Excel(name = "资源名称")
    private String resourceName;

    /** 资源类型 */
    @Excel(name = "资源类型")
    private String resourceType;

    /** 默认模型 */
    @Excel(name = "默认模型")
    private String defaultModel;

    /** API密钥 */
    @Excel(name = "API密钥")
    private String apiKey;

    /** 密钥 */
    @Excel(name = "密钥")
    private String secretKey;

    /** API地址 */
    @Excel(name = "API地址")
    private String apiUrl;

    /** 频率限制(次/分钟) */
    @Excel(name = "频率限制(次/分钟)")
    private Long frequencyLimit;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date creationTime;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String active;

    /** 操作说明 */
    @Excel(name = "操作说明")
    private String operation;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String extraKey;

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
    public void setDefaultModel(String defaultModel) 
    {
        this.defaultModel = defaultModel;
    }

    public String getDefaultModel() 
    {
        return defaultModel;
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
    public void setFrequencyLimit(Long frequencyLimit) 
    {
        this.frequencyLimit = frequencyLimit;
    }

    public Long getFrequencyLimit() 
    {
        return frequencyLimit;
    }
    public void setCreationTime(Date creationTime) 
    {
        this.creationTime = creationTime;
    }

    public Date getCreationTime() 
    {
        return creationTime;
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
    public void setExtraKey(String extraKey) 
    {
        this.extraKey = extraKey;
    }

    public String getExtraKey() 
    {
        return extraKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("resourceName", getResourceName())
            .append("resourceType", getResourceType())
            .append("defaultModel", getDefaultModel())
            .append("apiKey", getApiKey())
            .append("secretKey", getSecretKey())
            .append("apiUrl", getApiUrl())
            .append("frequencyLimit", getFrequencyLimit())
            .append("creationTime", getCreationTime())
            .append("active", getActive())
            .append("operation", getOperation())
            .append("extraKey", getExtraKey())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
