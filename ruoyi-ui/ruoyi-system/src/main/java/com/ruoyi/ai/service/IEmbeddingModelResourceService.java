package com.ruoyi.ai.service;

import java.util.List;
import com.ruoyi.ai.domain.EmbeddingModelResource;

/**
 * 嵌入模型资源Service接口
 * 
 * @author ruoyi
 * @date 2025-07-01
 */
public interface IEmbeddingModelResourceService 
{
    /**
     * 查询嵌入模型资源
     * 
     * @param id 嵌入模型资源主键
     * @return 嵌入模型资源
     */
    public EmbeddingModelResource selectEmbeddingModelResourceById(Long id);

    /**
     * 查询嵌入模型资源列表
     * 
     * @param embeddingModelResource 嵌入模型资源
     * @return 嵌入模型资源集合
     */
    public List<EmbeddingModelResource> selectEmbeddingModelResourceList(EmbeddingModelResource embeddingModelResource);

    /**
     * 新增嵌入模型资源
     * 
     * @param embeddingModelResource 嵌入模型资源
     * @return 结果
     */
    public int insertEmbeddingModelResource(EmbeddingModelResource embeddingModelResource);

    /**
     * 修改嵌入模型资源
     * 
     * @param embeddingModelResource 嵌入模型资源
     * @return 结果
     */
    public int updateEmbeddingModelResource(EmbeddingModelResource embeddingModelResource);

    /**
     * 批量删除嵌入模型资源
     * 
     * @param ids 需要删除的嵌入模型资源主键集合
     * @return 结果
     */
    public int deleteEmbeddingModelResourceByIds(Long[] ids);

    /**
     * 删除嵌入模型资源信息
     * 
     * @param id 嵌入模型资源主键
     * @return 结果
     */
    public int deleteEmbeddingModelResourceById(Long id);
}
