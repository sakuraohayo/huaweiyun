//Author:刘行
package javaweb.remember.exceptionhandler;

import javaweb.remember.vo.ResultVo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //格式错误异常
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVo handleConstraintViolationException(ConstraintViolationException e){
        return new ResultVo(-100,e.getMessage(),null);
    }

    //数据类型错误异常
    @ExceptionHandler(IllegalStateException.class)
    public ResultVo handleIllegalStateException(IllegalStateException e){
        return new ResultVo(-101, e.getMessage(), null);
    }

    //文件读写异常
    @ExceptionHandler(IOException.class)
    public ResultVo handleIOException(IOException e){
        return new ResultVo(-102, e.getMessage(), null);
    }

    //空指针异常
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResultVo handleNoSuchElementException(NoSuchElementException e){
        return new ResultVo(-103,e.getMessage(),null);
    }
}
