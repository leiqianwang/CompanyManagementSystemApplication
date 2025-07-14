package com.ruoyi.ai.service;

import java.util.List;
import com.ruoyi.ai.domain.VectorDbResource;

/**
 * 向量数据库资源Service接口
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public interface IVectorDbResourceService 
{
    /**
     * 查询向量数据库资源
     * 
     * @param id 向量数据库资源主键
     * @return 向量数据库资源
     */
    public VectorDbResource selectVectorDbResourceById(Long id);

    /**
     * 查询向量数据库资源列表
     * 
     * @param vectorDbResource 向量数据库资源
     * @return 向量数据库资源集合
     */
    public List<VectorDbResource> selectVectorDbResourceList(VectorDbResource vectorDbResource);

    /**
     * 新增向量数据库资源
     * 
     * @param vectorDbResource 向量数据库资源
     * @return 结果
     */
    public int insertVectorDbResource(VectorDbResource vectorDbResource);

    /**
     * 修改向量数据库资源
     * 
     * @param vectorDbResource 向量数据库资源
     * @return 结果
     */
    public int updateVectorDbResource(VectorDbResource vectorDbResource);

    /**
     * 批量删除向量数据库资源
     * 
     * @param ids 需要删除的向量数据库资源主键集合
     * @return 结果
     */
    public int deleteVectorDbResourceByIds(Long[] ids);

    /**
     * 删除向量数据库资源信息
     * 
     * @param id 向量数据库资源主键
     * @return 结果
     */
    public int deleteVectorDbResourceById(Long id);

    /**
     * 测试向量数据库连接
     * 
     * @param id 向量数据库资源主键
     * @return 测试结果
     */
    public boolean testConnection(Long id);
}
