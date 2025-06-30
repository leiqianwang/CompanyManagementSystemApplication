package com.ruoyi.ai.mapper;

import java.util.List;
import com.ruoyi.ai.domain.AiModelResource;

/**
 * AI模型资源Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-30
 */
public interface AiModelResourceMapper 
{
    /**
     * 查询AI模型资源
     * 
     * @param id AI模型资源主键
     * @return AI模型资源
     */
    public AiModelResource selectAiModelResourceById(Long id);

    /**
     * 查询AI模型资源列表
     * 
     * @param aiModelResource AI模型资源
     * @return AI模型资源集合
     */
    public List<AiModelResource> selectAiModelResourceList(AiModelResource aiModelResource);

    /**
     * 新增AI模型资源
     * 
     * @param aiModelResource AI模型资源
     * @return 结果
     */
    public int insertAiModelResource(AiModelResource aiModelResource);

    /**
     * 修改AI模型资源
     * 
     * @param aiModelResource AI模型资源
     * @return 结果
     */
    public int updateAiModelResource(AiModelResource aiModelResource);

    /**
     * 删除AI模型资源
     * 
     * @param id AI模型资源主键
     * @return 结果
     */
    public int deleteAiModelResourceById(Long id);

    /**
     * 批量删除AI模型资源
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiModelResourceByIds(Long[] ids);
}
