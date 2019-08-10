package cn.edu.njnu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    private String code;    //返回值：10000表示成功，10001表示失败，10002表示异常
    private String msg;
    private T data;

}
