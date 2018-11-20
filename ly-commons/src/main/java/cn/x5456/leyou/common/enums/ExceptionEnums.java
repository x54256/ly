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
    SEARCH_CANNOT_BE_FOUND(404,"搜索的商品未找到"),
    PAGE_CONVERT_FAILD(501,"页面转换失败"),
    INVAILD_FAILD(502,"验证类型输入错误！"),
    SMS_SEND_ERROR(503,"短信发送失败！"),
    USER_REGISTER_ERROR(504,"用户注册失败！"),
    CODE_IS_EXPIRED(505,"验证码已过期！"),
    CODE_IS_FAILD(506,"验证码输入错误！"),
    USER_CANNOT_BE_NULL(507,"用户查询为空!"),
    CART_CANNOT_BE_NULL(508,"购物车查询为空!")
    ;

    private int code;
    private String msg;

}
