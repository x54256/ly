package cn.x5456.leyou.common.enums;

import lombok.*;

/**
 * 异常枚举类
 * @author x5456
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {

    PRICE_CANNOT_BE_NULL(400,"价格不能为空!"),
    Brand_CANNOT_BE_NULL(400,"品牌查询为空!"),
    SPEC_GROUP_CANNOT_BE_NULL(400,"规格参数组查询为空!"),
    SPEC_PARAM_CANNOT_BE_NULL(400,"规格查询为空!"),
    SPU_CANNOT_BE_NULL(400,"商品通用信息查询为空!"),
    SKU_CANNOT_BE_NULL(400,"商品具体信息查询为空!"),
    Brand_INSERT_FAILD(500,"品牌插入失败!"),
    GOODS_INSERT_FAILD(500,"商品添加失败!"),
    UPLOAD_FILE_FAILD(402,"文件上传失败!"),
    GOODS_UPDATE_FAILD(501, "商品修改失败"),
    SPU_DETAIL_CANNOT_BE_NULL(400, "商品详情信息查询为空"),
    SEARCH_CANNOT_BE_FOUND(404,"搜索的商品未找到")
    ;

    private int code;
    private String msg;

}
