package cn.x5456.leyou.common.pojo;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import lombok.Data;


/**
 * 当出现ZyException时，返回前端的内容
 * @author x5456
 */
@Data
public class ExceptionResult {

    private int status;
    private String message;
    private Long timestamp;


    public ExceptionResult(ExceptionEnums em){

        this.status = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();

    }

}
