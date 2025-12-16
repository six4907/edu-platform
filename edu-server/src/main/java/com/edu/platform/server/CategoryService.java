package com.edu.platform.server;

import com.edu.platform.dto.CategoryAddDTO;
import com.edu.platform.dto.CategoryUpdateDTO;
import com.edu.platform.vo.CategoryTreeVO;

import java.util.List;

public interface CategoryService {

    /**
     * 新增分类（仅管理员可用）
     * @param categoryAddDTO 分类信息
     */
    void addCategory(CategoryAddDTO categoryAddDTO);

    /**
     * 修改分类（仅管理员可用）
     * @param id 分类ID
     * @param categoryUpdateDTO 分类修改信息
     */
    void updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO);

    /**
     * 删除分类（仅管理员可用）
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 查询所有分类
     */
    List<CategoryTreeVO> listCategoryTree();

    /**
     * 递归查询子分类
     * @param parentId 父分类ID
     * @return 子分类树形结构
     */
    List<CategoryTreeVO> getChildren(Long parentId);
}