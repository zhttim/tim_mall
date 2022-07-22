package com.tim.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tim.gulimall.product.entity.BrandEntity;
import com.tim.gulimall.product.service.BrandService;
import com.tim.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("华为");
//        brandService.updateById(brandEntity);
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(System.out::println);

    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径: {}", Arrays.asList(catelogPath));
    }
}
