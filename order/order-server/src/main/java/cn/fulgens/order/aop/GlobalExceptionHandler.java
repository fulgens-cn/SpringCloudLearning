package cn.fulgens.order.aop;

import cn.fulgens.order.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleRuntimeException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        RuntimeException exception = (RuntimeException) e;
        return JsonResult.error(exception.getMessage(), null);
    }

    /**
     * 通用的接口映射异常处理方法
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            return new ResponseEntity(JsonResult.build(Integer.valueOf(status.value()),
                    exception.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null), status);
        }
        if (e instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) e;
            log.error("参数转换失败，方法：{}，参数：{}，信息：", exception.getParameter().getMethod().getName(),
                    exception.getParameter(), exception.getLocalizedMessage());
            return new ResponseEntity(JsonResult.build(Integer.valueOf(status.value()), "参数转换失败", null), status);
        }
        return new ResponseEntity(JsonResult.build(Integer.valueOf(status.value()), "参数转换失败", null), status);
    }

}
