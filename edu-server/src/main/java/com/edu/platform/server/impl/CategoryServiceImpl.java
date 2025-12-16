package com.edu.platform.server.impl;

import com.edu.platform.constant.MessageConstant;
import com.edu.platform.constant.StatusConstant;
import com.edu.platform.dto.CategoryAddDTO;
import com.edu.platform.dto.CategoryUpdateDTO;
import com.edu.platform.entity.Category;
import com.edu.platform.exception.BaseException;
import com.edu.platform.exception.PermissionDeniedException;
import com.edu.platform.mapper.CategoryMapper;
import com.edu.platform.mapper.CourseMapper;
import com.edu.platform.server.CategoryService;
import com.edu.platform.context.BaseContext;
import com.edu.platform.vo.CategoryTreeVO;
import com.edu.platform.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 新增分类（仅管理员可用）
     * @param categoryAddDTO 新增参数
     */
    @Transactional
    public void addCategory(CategoryAddDTO categoryAddDTO) {
        log.info("新增分类：{}", categoryAddDTO);

        // 1. 权限校验（仅管理员可操作，假设3为管理员角色）
        Integer role = BaseContext.getUserRole();
        if (role == null || role != 3) {
            log.warn("新增分类失败：权限不足，角色={}", role);
            throw new PermissionDeniedException(MessageConstant.PERMISSION_DENIED);
        }

        // 2. 校验父分类是否存在（parentId不为0时）
        Long parentId = categoryAddDTO.getParentId();
        if (parentId != 0) {
            Category parentCategory = categoryMapper.selectById(parentId.longValue());
            if (parentCategory == null) {
                log.warn("父分类不存在：parentId={}", parentId);
                throw new BaseException(MessageConstant.CATEGORY_NOT_FOUND);
            }
        }

        // 3. 校验同级分类名称唯一性（同级分类不允许重名）
        String name = categoryAddDTO.getName();
        Category existingCategory = categoryMapper.selectByNameAndParentId(name, parentId);
        if (existingCategory != null) {
            log.warn("分类名称已存在：name={}, parentId={}", name, parentId);
            throw new BaseException(MessageConstant.CATEGORY_ALREADY_EXISTS);
        }

        // 4. 计算排序号（同一父分类下最大排序+1）
        Integer maxSort = categoryMapper.selectMaxSortByParentId(parentId);
        int sort = maxSort == null ? 1 : maxSort + 1;

        // 5. 转换为实体并保存
        Category category = new Category();
        BeanUtils.copyProperties(categoryAddDTO, category);
        category.setSort(sort);
        categoryMapper.insert(category);

        log.info("新增分类成功，分类ID={}", category.getId());
    }

    /**
     * 修改分类（仅管理员可用）
     * @param id 分类ID
     * @param categoryUpdateDTO 修改参数
     */
    @Transactional
    public void updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        log.info("修改分类：id={}, 参数={}", id, categoryUpdateDTO);

        // 1. 权限校验（仅管理员可操作）
        Integer role = BaseContext.getUserRole();
        if (role == null || role != 3) {
            log.warn("修改分类失败：权限不足，角色={}", role);
            throw new PermissionDeniedException(MessageConstant.PERMISSION_DENIED);
        }

        // 2. 校验分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            log.warn("修改分类失败：分类不存在，id={}", id);
            throw new BaseException(MessageConstant.CATEGORY_NOT_FOUND);
        }

        // 3. 校验同级分类名称唯一性（排除当前分类自身）
        String name = categoryUpdateDTO.getName();
        Category existingCategory = categoryMapper.selectByNameAndParentId(name, category.getParentId());
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            log.warn("修改分类失败：同级分类名称已存在，name={}, parentId={}", name, category.getParentId());
            throw new BaseException(MessageConstant.CATEGORY_ALREADY_EXISTS);
        }

        // 4. 更新分类信息
        BeanUtils.copyProperties(categoryUpdateDTO, category);
        categoryMapper.update(category);
        log.info("修改分类成功，分类ID={}", id);
    }

    /**
     * 删除分类（仅管理员可用）
     * @param id 分类ID
     */
    @Transactional
    public void deleteCategory(Long id) {
        log.info("删除分类：id={}", id);

        // 1. 权限校验（仅管理员可操作）
        Integer role = BaseContext.getUserRole();
        if (role == null || role != 3) {
            log.warn("删除分类失败：权限不足，角色={}", role);
            throw new PermissionDeniedException(MessageConstant.PERMISSION_DENIED);
        }

        // 2. 校验分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            log.warn("删除分类失败：分类不存在，id={}", id);
            throw new BaseException(MessageConstant.CATEGORY_NOT_FOUND);
        }

        // 3. 校验是否有关联课程
        int courseCount = courseMapper.countByCategoryId(id);
        if (courseCount > 0) {
            log.warn("删除分类失败：存在关联课程，id={}", id);
            throw new BaseException(MessageConstant.CATEGORY_BE_RELATED_BY_COURSE);
        }

        // 4. 校验是否有子分类
        int childCount = categoryMapper.countByParentId(id);
        if (childCount > 0) {
            log.warn("删除分类失败：存在子分类，id={}", id);
            throw new BaseException(MessageConstant.CATEGORY_HAS_CHILDREN);
        }

        // 5. 执行删除
        categoryMapper.deleteById(id);
        log.info("删除分类成功，id={}", id);
    }

    /**
     * 查询树形结构分类（支持多级）
     * @return 分类树形结构
     */
    public List<CategoryTreeVO> listCategoryTree() {
        log.info("查询所有分类树形结构（多级）");
        // 从根节点开始查询（parentId = 0）
        return getChildren(0L);
    }

    /**
     * 递归查询子分类
     * @param parentId 父分类ID
     * @return 子分类树形结构列表
     */
    @Override
    public List<CategoryTreeVO> getChildren(Long parentId) {
        // 1. 查询当前父节点的所有直接子节点
        List<Category> childCategories = categoryMapper.selectByParentId(parentId);

        // 2. 转换为VO并递归查询子节点
        return childCategories.stream().map(category -> {
            CategoryTreeVO treeVO = new CategoryTreeVO();
            BeanUtils.copyProperties(category, treeVO);

            // 3. 递归查询子分类的子分类
            List<CategoryTreeVO> children = getChildren(category.getId());
            treeVO.setChildren(children);

            return treeVO;
        }).collect(Collectors.toList());
    }
}