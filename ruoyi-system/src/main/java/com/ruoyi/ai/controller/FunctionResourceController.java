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
import com.ruoyi.ai.domain.FunctionResource;
import com.ruoyi.ai.service.IFunctionResourceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * Function资源Controller
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@RestController
@RequestMapping("/ai/functionResource")
public class FunctionResourceController extends BaseController
{
    @Autowired
    private IFunctionResourceService functionResourceService;

    /**
     * 查询Function资源列表
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:list')")
    @GetMapping("/list")
    public TableDataInfo list(FunctionResource functionResource)
    {
        startPage();
        List<FunctionResource> list = functionResourceService.selectFunctionResourceList(functionResource);
        return getDataTable(list);
    }

    /**
     * 导出Function资源列表
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:export')")
    @Log(title = "Function资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FunctionResource functionResource)
    {
        List<FunctionResource> list = functionResourceService.selectFunctionResourceList(functionResource);
        ExcelUtil<FunctionResource> util = new ExcelUtil<FunctionResource>(FunctionResource.class);
        util.exportExcel(response, list, "Function资源数据");
    }

    /**
     * 获取Function资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(functionResourceService.selectFunctionResourceById(id));
    }

    /**
     * 新增Function资源
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:add')")
    @Log(title = "Function资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FunctionResource functionResource)
    {
        // 检查编号是否唯一
        if ("1".equals(functionResourceService.checkFunctionCodeUnique(functionResource.getFunctionCode())))
        {
            return error("新增Function资源'" + functionResource.getFunctionCode() + "'失败，Function编号已存在");
        }
        functionResource.setCreateBy(getUsername());
        return toAjax(functionResourceService.insertFunctionResource(functionResource));
    }

    /**
     * 修改Function资源
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:edit')")
    @Log(title = "Function资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FunctionResource functionResource)
    {
        // 检查编号是否唯一（排除当前记录）
        FunctionResource existingResource = functionResourceService.selectFunctionResourceById(functionResource.getId());
        if (existingResource != null && !existingResource.getFunctionCode().equals(functionResource.getFunctionCode()))
        {
            if ("1".equals(functionResourceService.checkFunctionCodeUnique(functionResource.getFunctionCode())))
            {
                return error("修改Function资源'" + functionResource.getFunctionCode() + "'失败，Function编号已存在");
            }
        }
        functionResource.setUpdateBy(getUsername());
        return toAjax(functionResourceService.updateFunctionResource(functionResource));
    }

    /**
     * 删除Function资源
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:remove')")
    @Log(title = "Function资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(functionResourceService.deleteFunctionResourceByIds(ids));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:edit')")
    @Log(title = "Function资源", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody FunctionResource functionResource)
    {
        functionResource.setUpdateBy(getUsername());
        return toAjax(functionResourceService.changeStatus(functionResource.getId(), functionResource.getIsEnabled()));
    }

    /**
     * 根据编号获取Function资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:functionResource:query')")
    @GetMapping(value = "/getByCode/{functionCode}")
    public AjaxResult getInfoByCode(@PathVariable("functionCode") String functionCode)
    {
        return success(functionResourceService.selectFunctionResourceByCode(functionCode));
    }

    /**
     * 检查Function编号是否唯一
     */
    @GetMapping("/checkFunctionCodeUnique")
    public AjaxResult checkFunctionCodeUnique(String functionCode)
    {
        return success(functionResourceService.checkFunctionCodeUnique(functionCode));
    }
}
