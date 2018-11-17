package cn.x5456.leyou.common.advice;

import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.common.pojo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器；为了能被主启动类扫描到，所以要注意主启动类的位置。
 *      advice:通知
 * @author x5456
 */
@ControllerAdvice   // 作用于所有Controller
public class CommonExceptionHandler {

    /**
     * 当出现了LyException，执行下面代码，返回给前端
     * @param e
     * @return
     */
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handlerException(LyException e){

        return ResponseEntity.status(e.getExceptionEnums().getCode()).body(new ExceptionResult(e.getExceptionEnums()));

    }

}
