package com.ruoyi.ai.service;

import java.util.List;
import com.ruoyi.ai.domain.FunctionResource;

/**
 * Function资源Service接口
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public interface IFunctionResourceService 
{
    /**
     * 查询Function资源
     * 
     * @param id Function资源主键
     * @return Function资源
     */
    public FunctionResource selectFunctionResourceById(Long id);

    /**
     * 查询Function资源列表
     * 
     * @param functionResource Function资源
     * @return Function资源集合
     */
    public List<FunctionResource> selectFunctionResourceList(FunctionResource functionResource);

    /**
     * 新增Function资源
     * 
     * @param functionResource Function资源
     * @return 结果
     */
    public int insertFunctionResource(FunctionResource functionResource);

    /**
     * 修改Function资源
     * 
     * @param functionResource Function资源
     * @return 结果
     */
    public int updateFunctionResource(FunctionResource functionResource);

    /**
     * 批量删除Function资源
     * 
     * @param ids 需要删除的Function资源主键集合
     * @return 结果
     */
    public int deleteFunctionResourceByIds(Long[] ids);

    /**
     * 删除Function资源信息
     * 
     * @param id Function资源主键
     * @return 结果
     */
    public int deleteFunctionResourceById(Long id);

    /**
     * 检查Function编号是否唯一
     * 
     * @param functionCode Function编号
     * @return 结果
     */
    public String checkFunctionCodeUnique(String functionCode);

    /**
     * 根据编号查询Function资源
     * 
     * @param functionCode Function编号
     * @return Function资源
     */
    public FunctionResource selectFunctionResourceByCode(String functionCode);

    /**
     * 启用/禁用Function资源
     * 
     * @param id Function资源主键
     * @param isEnabled 启用状态
     * @return 结果
     */
    public int changeStatus(Long id, String isEnabled);
}
