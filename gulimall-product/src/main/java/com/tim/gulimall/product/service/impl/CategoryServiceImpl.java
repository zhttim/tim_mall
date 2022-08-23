package com.tim.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tim.common.utils.PageUtils;
import com.tim.common.utils.Query;
import com.tim.gulimall.product.dao.CategoryDao;
import com.tim.gulimall.product.entity.CategoryEntity;
import com.tim.gulimall.product.service.CategoryBrandRelationService;
import com.tim.gulimall.product.service.CategoryService;
import com.tim.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
//      查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
//      创建树形结构
        return entities.stream().filter(categoryEntity ->
                        categoryEntity.getParentCid() == 0
//      找到子菜单
        ).map(categoryEntity -> {
            categoryEntity.setChildren(getChildren(categoryEntity, entities));
            return categoryEntity;
//      菜单排序
        }).sorted(Comparator.comparingInt(
                menu -> menu.getSort() == null ? 0 : menu.getSort()
        )).collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除菜单项是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);

        return parentPath.toArray(new Long[0]);
    }

    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        //更新级联数据
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {

        // 1、查询所有1级分类
        List<CategoryEntity> level1Categorys = getLevel1Categorys();
        // 2、封装数据封装成 map类型  key为 catId,value List<Catelog2Vo>
        Map<String, List<Catelog2Vo>> categoryList = level1Categorys.stream().collect(Collectors.toMap(
                categoryLevel1 -> categoryLevel1.getCatId().toString(),
                categoryLevel1 -> {

                    // 1、每一个的一级分类，查询到这个一级分类的二级分类
                    List<CategoryEntity> level2Categorys = baseMapper
                            .selectList(new QueryWrapper<CategoryEntity>()
                                    .eq("parent_cid", categoryLevel1.getCatId()));

                    // 2、封装上面的结果
                    List<Catelog2Vo> catelog2Vos = null;

                    if (level2Categorys != null) {

                        catelog2Vos = level2Categorys.stream().map(catelogLevel2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(
                                    categoryLevel1.getCatId().toString(),
                                    null,
                                    catelogLevel2.getCatId().toString(),
                                    catelogLevel2.getName());

                            // 1、查询当前二级分类的三级分类vo
                            List<CategoryEntity> level3Categorys = baseMapper
                                    .selectList(new QueryWrapper<CategoryEntity>()
                                            .eq("parent_cid", catelogLevel2.getCatId()));

                            if (level3Categorys != null) {

                                // 2、分装成指定格式
                                List<Catelog2Vo.Catelog3Vo> catelog3VoList = level3Categorys.stream().map(
                                        catelogLevel3 -> new Catelog2Vo.Catelog3Vo(
                                                catelogLevel2.getCatId().toString(),
                                                catelogLevel3.getCatId().toString(),
                                                catelogLevel3.getName())
                                ).collect(Collectors.toList());
                                // 3、设置分类数据
                                catelog2Vo.setCatalog3List(catelog3VoList);
                            }
                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));
        // 2、封装数据
        return categoryList;
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    //  递归找到所有菜单子菜单
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream().filter(categoryEntity ->
                categoryEntity.getParentCid().equals(root.getCatId())
        ).map(categoryEntity -> {
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        }).sorted(Comparator.comparingInt(
                menu -> menu.getSort() == null ? 0 : menu.getSort()
        )).collect(Collectors.toList());
    }

}