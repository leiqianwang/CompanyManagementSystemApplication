package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Function资源对象 function_resource
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public class FunctionResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号（主键） */
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String functionCode;

    /** 功能描述 */
    @Excel(name = "功能描述")
    private String functionDescription;

    /** 类别类 */
    @Excel(name = "类别类")
    private String categoryClass;

    /** 执行方法 */
    @Excel(name = "执行方法")
    private String executionMethod;

    /** 参数管理 */
    @Excel(name = "参数管理")
    private String parameterManagement;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private String isEnabled;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFunctionCode(String functionCode) 
    {
        this.functionCode = functionCode;
    }

    public String getFunctionCode() 
    {
        return functionCode;
    }
    public void setFunctionDescription(String functionDescription) 
    {
        this.functionDescription = functionDescription;
    }

    public String getFunctionDescription() 
    {
        return functionDescription;
    }
    public void setCategoryClass(String categoryClass) 
    {
        this.categoryClass = categoryClass;
    }

    public String getCategoryClass() 
    {
        return categoryClass;
    }
    public void setExecutionMethod(String executionMethod) 
    {
        this.executionMethod = executionMethod;
    }

    public String getExecutionMethod() 
    {
        return executionMethod;
    }
    public void setParameterManagement(String parameterManagement) 
    {
        this.parameterManagement = parameterManagement;
    }

    public String getParameterManagement() 
    {
        return parameterManagement;
    }
    public void setIsEnabled(String isEnabled) 
    {
        this.isEnabled = isEnabled;
    }

    public String getIsEnabled() 
    {
        return isEnabled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("functionCode", getFunctionCode())
            .append("functionDescription", getFunctionDescription())
            .append("categoryClass", getCategoryClass())
            .append("executionMethod", getExecutionMethod())
            .append("parameterManagement", getParameterManagement())
            .append("isEnabled", getIsEnabled())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
