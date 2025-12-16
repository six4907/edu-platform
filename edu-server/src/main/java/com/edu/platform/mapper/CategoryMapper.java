package com.edu.platform.mapper;

import com.edu.platform.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return
     */
    @Select("select * from edu_category where id = #{id}")
    Category selectById(Long id);

    /**
     * 新增分类
     * @param category
     */
    @Insert("insert into edu_category (name, parent_id, sort ) " +
            "values (#{name}, #{parentId}, #{sort} )")
    void insert(Category  category);

    /**
     * 根据名称和父ID查询分类（校验同级分类名称唯一性）
     * @param name 分类名称
     * @param parentId 父分类ID
     */
    @Select("select id from edu_category where name = #{name} and parent_id = #{parentId}")
    Category selectByNameAndParentId( String name, Long parentId);

    /**
     * 查询同一父分类下的最大排序号
     * @param parentId 父分类ID
     */
    @Select("select max(sort) from edu_category where parent_id = #{parentId}")
    Integer selectMaxSortByParentId(Long parentId);

    /**
     * 更新分类信息
     * @param category
     */
    @Update("update edu_category set " +
            "name = #{name}, " +
            "sort = #{sort} " +
            "where id = #{id}")
    void update(Category category);

    /**
     * 根据父ID查询子分类数量
     * @param parentId 父分类ID
     */
    @Select("select count(*) from edu_category where parent_id = #{parentId}")
    int countByParentId(Long parentId);

    /**
     * 根据ID删除分类
     * @param id 分类ID
     */
    @Delete("delete from edu_category where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据父分类ID查询子分类（用于树形结构）
     * @param parentId 父分类ID（0查询一级分类）
     */
    @Select("select id, name, parent_id as parentId, sort from edu_category where parent_id = #{parentId} order by sort asc")
    List<Category> selectByParentId(Long parentId);
}
