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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

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
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "category", allEntries = true)
    public void updateCascade(CategoryEntity category) {
        //更新级联数据
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
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

    @Cacheable(value = "category", key = "#root.methodName", sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

//    @Override
//    public Map<String, List<Catelog2Vo>> getCatelogJson() {
//
//        // 给缓存中放 json 字符串、拿出的是 json 字符串，还要逆转为能用的对象类型【序列化和反序列化】
//
//        // 1、加入缓存逻辑，缓存中放的数据是 json 字符串
//        // JSON 跨语言，跨平台兼容
//        String catelogJSON = stringRedisTemplate.opsForValue().get("catelogJSON");
//        if (StringUtils.isEmpty(catelogJSON)) {
//            // 2、缓存没有，从数据库中查询
//            Map<String, List<Catelog2Vo>> catelogJsonFromDb = getCatelogJsonFromDb();
//            // 3、查询到数据，将数据转成 JSON 后放入缓存中
//            stringRedisTemplate.opsForValue().set("catelogJSON", JSON.toJSONString(catelogJsonFromDb));
//            return catelogJsonFromDb;
//        }
//        // 转换为我们指定的对象
//        return JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
//        });
//    }

    @Override
    @Cacheable(value = "category", key = "#root.methodName", sync = true)
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        System.out.println("查询了数据库");

        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        // 1、查询所有1级分类
        List<CategoryEntity> level1Categorys = getParent_cid(categoryEntities, 0L);
        // 2、封装数据封装成 map类型  key为 catId,value List<Catelog2Vo>
        Map<String, List<Catelog2Vo>> categoryList = level1Categorys.stream().collect(Collectors.toMap(
                categoryLevel1 -> categoryLevel1.getCatId().toString(),
                categoryLevel1 -> {

                    // 1、每一个的一级分类，查询到这个一级分类的二级分类
                    List<CategoryEntity> level2Categorys = getParent_cid(categoryEntities, categoryLevel1.getParentCid());

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
                            List<CategoryEntity> level3Categorys = getParent_cid(categoryEntities, catelogLevel2.getParentCid());

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

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntities, Long parent_cid) {
        return categoryEntities.stream().filter(categoryEntity -> categoryEntity.getParentCid().equals(parent_cid)).collect(Collectors.toList());
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