package com.ruoyi.ai.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.EmbeddingModelResourceMapper;
import com.ruoyi.ai.domain.EmbeddingModelResource;
import com.ruoyi.ai.service.IEmbeddingModelResourceService;

/**
 * 嵌入模型资源Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-01
 */
@Service
public class EmbeddingModelResourceServiceImpl implements IEmbeddingModelResourceService 
{
    @Autowired
    private EmbeddingModelResourceMapper embeddingModelResourceMapper;

    /**
     * 查询嵌入模型资源
     * 
     * @param id 嵌入模型资源主键
     * @return 嵌入模型资源
     */
    @Override
    public EmbeddingModelResource selectEmbeddingModelResourceById(Long id)
    {
        return embeddingModelResourceMapper.selectEmbeddingModelResourceById(id);
    }

    /**
     * 查询嵌入模型资源列表
     * 
     * @param embeddingModelResource 嵌入模型资源
     * @return 嵌入模型资源
     */
    @Override
    public List<EmbeddingModelResource> selectEmbeddingModelResourceList(EmbeddingModelResource embeddingModelResource)
    {
        return embeddingModelResourceMapper.selectEmbeddingModelResourceList(embeddingModelResource);
    }

    /**
     * 新增嵌入模型资源
     * 
     * @param embeddingModelResource 嵌入模型资源
     * @return 结果
     */
    @Override
    public int insertEmbeddingModelResource(EmbeddingModelResource embeddingModelResource)
    {
        embeddingModelResource.setCreateTime(DateUtils.getNowDate());
        return embeddingModelResourceMapper.insertEmbeddingModelResource(embeddingModelResource);
    }

    /**
     * 修改嵌入模型资源
     * 
     * @param embeddingModelResource 嵌入模型资源
     * @return 结果
     */
    @Override
    public int updateEmbeddingModelResource(EmbeddingModelResource embeddingModelResource)
    {
        embeddingModelResource.setUpdateTime(DateUtils.getNowDate());
        return embeddingModelResourceMapper.updateEmbeddingModelResource(embeddingModelResource);
    }

    /**
     * 批量删除嵌入模型资源
     * 
     * @param ids 需要删除的嵌入模型资源主键
     * @return 结果
     */
    @Override
    public int deleteEmbeddingModelResourceByIds(Long[] ids)
    {
        return embeddingModelResourceMapper.deleteEmbeddingModelResourceByIds(ids);
    }

    /**
     * 删除嵌入模型资源信息
     * 
     * @param id 嵌入模型资源主键
     * @return 结果
     */
    @Override
    public int deleteEmbeddingModelResourceById(Long id)
    {
        return embeddingModelResourceMapper.deleteEmbeddingModelResourceById(id);
    }
}
