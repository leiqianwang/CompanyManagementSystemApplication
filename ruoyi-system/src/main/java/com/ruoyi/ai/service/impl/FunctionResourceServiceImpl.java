package com.ruoyi.ai.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.FunctionResourceMapper;
import com.ruoyi.ai.domain.FunctionResource;
import com.ruoyi.ai.service.IFunctionResourceService;

/**
 * Function资源Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@Service
public class FunctionResourceServiceImpl implements IFunctionResourceService 
{
    @Autowired
    private FunctionResourceMapper functionResourceMapper;

    /**
     * 查询Function资源
     * 
     * @param id Function资源主键
     * @return Function资源
     */
    @Override
    public FunctionResource selectFunctionResourceById(Long id)
    {
        return functionResourceMapper.selectFunctionResourceById(id);
    }

    /**
     * 查询Function资源列表
     * 
     * @param functionResource Function资源
     * @return Function资源
     */
    @Override
    public List<FunctionResource> selectFunctionResourceList(FunctionResource functionResource)
    {
        return functionResourceMapper.selectFunctionResourceList(functionResource);
    }

    /**
     * 新增Function资源
     * 
     * @param functionResource Function资源
     * @return 结果
     */
    @Override
    public int insertFunctionResource(FunctionResource functionResource)
    {
        functionResource.setCreateTime(DateUtils.getNowDate());
        return functionResourceMapper.insertFunctionResource(functionResource);
    }

    /**
     * 修改Function资源
     * 
     * @param functionResource Function资源
     * @return 结果
     */
    @Override
    public int updateFunctionResource(FunctionResource functionResource)
    {
        functionResource.setUpdateTime(DateUtils.getNowDate());
        return functionResourceMapper.updateFunctionResource(functionResource);
    }

    /**
     * 批量删除Function资源
     * 
     * @param ids 需要删除的Function资源主键
     * @return 结果
     */
    @Override
    public int deleteFunctionResourceByIds(Long[] ids)
    {
        return functionResourceMapper.deleteFunctionResourceByIds(ids);
    }

    /**
     * 删除Function资源信息
     * 
     * @param id Function资源主键
     * @return 结果
     */
    @Override
    public int deleteFunctionResourceById(Long id)
    {
        return functionResourceMapper.deleteFunctionResourceById(id);
    }

    /**
     * 检查Function编号是否唯一
     * 
     * @param functionCode Function编号
     * @return 结果
     */
    @Override
    public String checkFunctionCodeUnique(String functionCode)
    {
        int count = functionResourceMapper.checkFunctionCodeUnique(functionCode);
        if (count > 0)
        {
            return "1";
        }
        return "0";
    }

    /**
     * 根据编号查询Function资源
     * 
     * @param functionCode Function编号
     * @return Function资源
     */
    @Override
    public FunctionResource selectFunctionResourceByCode(String functionCode)
    {
        return functionResourceMapper.selectFunctionResourceByCode(functionCode);
    }

    /**
     * 启用/禁用Function资源
     * 
     * @param id Function资源主键
     * @param isEnabled 启用状态
     * @return 结果
     */
    @Override
    public int changeStatus(Long id, String isEnabled)
    {
        FunctionResource functionResource = new FunctionResource();
        functionResource.setId(id);
        functionResource.setIsEnabled(isEnabled);
        functionResource.setUpdateTime(DateUtils.getNowDate());
        return functionResourceMapper.updateFunctionResource(functionResource);
    }
}
