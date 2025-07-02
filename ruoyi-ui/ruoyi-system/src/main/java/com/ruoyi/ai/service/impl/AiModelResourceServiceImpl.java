package com.ruoyi.ai.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.AiModelResourceMapper;
import com.ruoyi.ai.domain.AiModelResource;
import com.ruoyi.ai.service.IAiModelResourceService;

/**
 * AI模型资源Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-30
 */
@Service
public class AiModelResourceServiceImpl implements IAiModelResourceService 
{
    @Autowired
    private AiModelResourceMapper aiModelResourceMapper;

    /**
     * 查询AI模型资源
     * 
     * @param id AI模型资源主键
     * @return AI模型资源
     */
    @Override
    public AiModelResource selectAiModelResourceById(Long id)
    {
        return aiModelResourceMapper.selectAiModelResourceById(id);
    }

    /**
     * 查询AI模型资源列表
     * 
     * @param aiModelResource AI模型资源
     * @return AI模型资源
     */
    @Override
    public List<AiModelResource> selectAiModelResourceList(AiModelResource aiModelResource)
    {
        return aiModelResourceMapper.selectAiModelResourceList(aiModelResource);
    }

    /**
     * 新增AI模型资源
     * 
     * @param aiModelResource AI模型资源
     * @return 结果
     */
    @Override
    public int insertAiModelResource(AiModelResource aiModelResource)
    {
        aiModelResource.setCreateTime(DateUtils.getNowDate());
        return aiModelResourceMapper.insertAiModelResource(aiModelResource);
    }

    /**
     * 修改AI模型资源
     * 
     * @param aiModelResource AI模型资源
     * @return 结果
     */
    @Override
    public int updateAiModelResource(AiModelResource aiModelResource)
    {
        aiModelResource.setUpdateTime(DateUtils.getNowDate());
        return aiModelResourceMapper.updateAiModelResource(aiModelResource);
    }

    /**
     * 批量删除AI模型资源
     * 
     * @param ids 需要删除的AI模型资源主键
     * @return 结果
     */
    @Override
    public int deleteAiModelResourceByIds(Long[] ids)
    {
        return aiModelResourceMapper.deleteAiModelResourceByIds(ids);
    }

    /**
     * 删除AI模型资源信息
     * 
     * @param id AI模型资源主键
     * @return 结果
     */
    @Override
    public int deleteAiModelResourceById(Long id)
    {
        return aiModelResourceMapper.deleteAiModelResourceById(id);
    }
}
