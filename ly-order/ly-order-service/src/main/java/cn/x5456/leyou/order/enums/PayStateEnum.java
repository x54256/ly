package cn.x5456.leyou.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayStateEnum {

    NOT_PAY(0),
    SUCCESS(1),
    FAIL(2)
    ;

    int value;

}
