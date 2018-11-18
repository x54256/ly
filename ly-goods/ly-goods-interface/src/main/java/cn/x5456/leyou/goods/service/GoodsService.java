package cn.x5456.leyou.goods.service;

import java.util.Map;

public interface GoodsService {
    Map<String,Object> loadModel(Long id);

    void asyncExcute(Long spuId);
}
