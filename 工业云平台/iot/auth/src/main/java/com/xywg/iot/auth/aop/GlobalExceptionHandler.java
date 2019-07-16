package com.xywg.iot.auth.aop;

import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 全局异常处理
 * <p>
 * springsecurity 会出现  401 403 异常状态 .  此处将返回封装
 */
@RestController
public class GlobalExceptionHandler extends AbstractErrorController {
    private final ErrorProperties errorProperties;
    public static ThreadLocal<ResultCode> UNAUTHORIZED = new ThreadLocal<>();

    @Autowired
    public GlobalExceptionHandler(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes);
        this.errorProperties = serverProperties.getError();
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }

    @RequestMapping(value = "/error")
    public Object error(HttpServletResponse resp, HttpServletRequest req) {

        Map<String, Object> errorMap = getErrorAttributes(req, isIncludeStackTrace(req, MediaType.ALL));
        resp.setStatus(HttpStatus.OK.value());
        ResultCode resultCode = UNAUTHORIZED.get();
        UNAUTHORIZED.remove();
        if (HttpStatus.FORBIDDEN.value() == (int) errorMap.get("status")) {
            String messgae = (String) errorMap.get("message");
            if ("Forbidden".equals(messgae)) {
                //	return new ResultVO(ResultCode.NO_PERMISSION,null);
                if (resultCode == null) {
                    return new ResultVO(ResultCode.NO_PERMISSION, null);
                }
                return new ResultVO(resultCode, null);
            } else if ("Access Denied".equals(messgae)) {
                if (resultCode != null) {
                    return new ResultVO(resultCode, null);
                } else {
                    return new ResultVO(ResultCode.AUTH_ERROR, null);
                }
            }
        } else {
            if (resultCode != null) {
                return new ResultVO(resultCode, null);
            } else {
                return new ResultVO(ResultCode.SYS_ERROR, null);
            }
        }

        // 403 Forbidden
        //403 Access Denied
        return errorMap;

    }

    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    private ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }


}
