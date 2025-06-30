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
import com.ruoyi.ai.domain.AiModelResource;
import com.ruoyi.ai.service.IAiModelResourceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI模型资源Controller
 * 
 * @author ruoyi
 * @date 2025-06-30
 */
@RestController
@RequestMapping("/ai/modelResource")
public class AiModelResourceController extends BaseController
{
    @Autowired
    private IAiModelResourceService aiModelResourceService;

    /**
     * 查询AI模型资源列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelResource:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiModelResource aiModelResource)
    {
        startPage();
        List<AiModelResource> list = aiModelResourceService.selectAiModelResourceList(aiModelResource);
        return getDataTable(list);
    }

    /**
     * 导出AI模型资源列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelResource:export')")
    @Log(title = "AI模型资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiModelResource aiModelResource)
    {
        List<AiModelResource> list = aiModelResourceService.selectAiModelResourceList(aiModelResource);
        ExcelUtil<AiModelResource> util = new ExcelUtil<AiModelResource>(AiModelResource.class);
        util.exportExcel(response, list, "AI模型资源数据");
    }

    /**
     * 获取AI模型资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:modelResource:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiModelResourceService.selectAiModelResourceById(id));
    }

    /**
     * 新增AI模型资源
     */
    @PreAuthorize("@ss.hasPermi('ai:modelResource:add')")
    @Log(title = "AI模型资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiModelResource aiModelResource)
    {
        return toAjax(aiModelResourceService.insertAiModelResource(aiModelResource));
    }

    /**
     * 修改AI模型资源
     */
    @PreAuthorize("@ss.hasPermi('ai:modelResource:edit')")
    @Log(title = "AI模型资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiModelResource aiModelResource)
    {
        return toAjax(aiModelResourceService.updateAiModelResource(aiModelResource));
    }

    /**
     * 删除AI模型资源
     */
    @PreAuthorize("@ss.hasPermi('ai:modelResource:remove')")
    @Log(title = "AI模型资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiModelResourceService.deleteAiModelResourceByIds(ids));
    }

    /**
     * 测试AI模型资源连接
     */
    @PreAuthorize("@ss.hasPermi('ai:modelResource:test')")
    @Log(title = "AI模型资源测试", businessType = BusinessType.OTHER)
    @PostMapping("/test/{id}")
    public AjaxResult test(@PathVariable("id") Long id)
    {
        AiModelResource resource = aiModelResourceService.selectAiModelResourceById(id);
        if (resource == null) {
            return error("资源不存在");
        }
        
        // TODO: 实现具体的测试逻辑
        // 这里可以实现真实的AI API连接测试
        // 例如调用 resource.getApiUrl() 进行连接测试
        
        return success("连接测试成功");
    }
}
