package com.edu.platform.controller;

import com.edu.platform.dto.CategoryAddDTO;
import com.edu.platform.dto.CategoryUpdateDTO;
import com.edu.platform.result.Result;
import com.edu.platform.server.CategoryService;
import com.edu.platform.vo.CategoryTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@Slf4j
@Api(tags = "课程分类管理接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类（仅管理员可用）
     * @param categoryAddDTO 分类信息
     * @return 操作结果
     */
    @PostMapping
    @ApiOperation(value = "新增分类（仅管理员可用）")
    public Result<Void> addCategory(@Valid @RequestBody CategoryAddDTO categoryAddDTO) {
        log.info("接收新增分类请求：{}", categoryAddDTO);
        categoryService.addCategory(categoryAddDTO);
        return Result.success();
    }

    /**
     * 修改分类（仅管理员可用）
     * @param id 分类ID
     * @param categoryUpdateDTO 分类修改信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改分类（仅管理员可用）")
    public Result<Void> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        log.info("接收修改分类请求：id={}, 参数={}", id, categoryUpdateDTO);
        categoryService.updateCategory(id, categoryUpdateDTO);
        return Result.success();
    }

    /**
     * 删除分类（仅管理员可用）
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除分类（仅管理员可用，无课程关联时可删除）")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        log.info("接收删除分类请求：id={}", id);
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 查询所有分类（树形结构：一级分类包含二级）
     * @return 分类树形结构
     */
    @GetMapping("/tree")
    @ApiOperation(value = "查询分类树形结构（一级+二级）")
    public Result<List<CategoryTreeVO>> listCategoryTree() {
        log.info("接收分类树形结构查询请求");
        List<CategoryTreeVO> categoryTree = categoryService.listCategoryTree();
        return Result.success(categoryTree);
    }
}