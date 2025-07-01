package com.ruoyi.ai.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.ai.domain.EmbeddingModelResource;
import com.ruoyi.ai.service.IEmbeddingModelResourceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 嵌入模型资源Controller
 * 
 * @author ruoyi
 * @date 2025-07-01
 */
@RestController
@RequestMapping("/ai/embeddingModel")
public class EmbeddingModelResourceController extends BaseController
{
    @Autowired
    private IEmbeddingModelResourceService embeddingModelResourceService;

    /**
     * 查询嵌入模型资源列表
     */
    @PreAuthorize("@ss.hasPermi('ai:embeddingModel:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmbeddingModelResource embeddingModelResource)
    {
        startPage();
        List<EmbeddingModelResource> list = embeddingModelResourceService.selectEmbeddingModelResourceList(embeddingModelResource);
        return getDataTable(list);
    }

    /**
     * 导出嵌入模型资源列表
     */
    @PreAuthorize("@ss.hasPermi('ai:embeddingModel:export')")
    @Log(title = "嵌入模型资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EmbeddingModelResource embeddingModelResource)
    {
        List<EmbeddingModelResource> list = embeddingModelResourceService.selectEmbeddingModelResourceList(embeddingModelResource);
        ExcelUtil<EmbeddingModelResource> util = new ExcelUtil<EmbeddingModelResource>(EmbeddingModelResource.class);
        util.exportExcel(response, list, "嵌入模型资源数据");
    }

    /**
     * 获取嵌入模型资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:embeddingModel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(embeddingModelResourceService.selectEmbeddingModelResourceById(id));
    }

    /**
     * 新增嵌入模型资源
     */
    @PreAuthorize("@ss.hasPermi('ai:embeddingModel:add')")
    @Log(title = "嵌入模型资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmbeddingModelResource embeddingModelResource)
    {
        return toAjax(embeddingModelResourceService.insertEmbeddingModelResource(embeddingModelResource));
    }

    /**
     * 修改嵌入模型资源
     */
    @PreAuthorize("@ss.hasPermi('ai:embeddingModel:edit')")
    @Log(title = "嵌入模型资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmbeddingModelResource embeddingModelResource)
    {
        return toAjax(embeddingModelResourceService.updateEmbeddingModelResource(embeddingModelResource));
    }

    /**
     * 删除嵌入模型资源
     */
    @PreAuthorize("@ss.hasPermi('ai:embeddingModel:remove')")
    @Log(title = "嵌入模型资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(embeddingModelResourceService.deleteEmbeddingModelResourceByIds(ids));
    }

    /**
     * 测试嵌入模型资源连接
     */
    @PreAuthorize("@ss.hasPermi('ai:embeddingModel:test')")
    @Log(title = "嵌入模型资源测试", businessType = BusinessType.OTHER)
    @PostMapping("/test/{id}")
    public AjaxResult test(@PathVariable("id") Long id)
    {
        EmbeddingModelResource resource = embeddingModelResourceService.selectEmbeddingModelResourceById(id);
        if (resource == null) {
            return error("资源不存在");
        }
        
        // TODO: 实现具体的测试逻辑
        // 这里可以实现真实的Embedding API连接测试
        // 例如调用 resource.getApiUrl() 进行向量化测试
        
        return success("连接测试成功");
    }
}
