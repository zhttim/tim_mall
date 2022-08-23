package com.tim.gulimall.search.service;

import com.tim.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author tim
 * @date 2022/8/23 11:13
 **/

public interface ProductSaveService {

    Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
