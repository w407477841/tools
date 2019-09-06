package com.honghu.oauth2.controller;

import cn.hutool.json.JSONUtil;
import com.honghu.oauth2.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class GlobalException  implements HandlerExceptionResolver {

    public GlobalException() {
        log.info("全局异常处理");
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception ex) {

        try {
            if (ex instanceof HttpRequestMethodNotSupportedException) {
                response.getWriter().write(JSONUtil.toJsonStr(ResultVO.error("不支持"+((HttpRequestMethodNotSupportedException) ex).getMethod()))           );
                return   new ModelAndView();
            }
            if (ex instanceof HttpMediaTypeNotSupportedException) {
                log.error("HttpMediaTypeNotSupportedException");
            }

            if (ex instanceof HttpMediaTypeNotAcceptableException) {
                log.error("HttpMediaTypeNotAcceptableException");
            }

            if (ex instanceof MissingPathVariableException) {
                log.error("MissingPathVariableException");
            }

            if (ex instanceof MissingServletRequestParameterException) {
                log.error("MissingServletRequestParameterException");
            }

            if (ex instanceof ServletRequestBindingException) {
                log.error("ServletRequestBindingException");
            }

            if (ex instanceof ConversionNotSupportedException) {
                log.error("ConversionNotSupportedException");
            }

            if (ex instanceof TypeMismatchException) {
                log.error("TypeMismatchException");
            }

            if (ex instanceof HttpMessageNotReadableException) {
                log.error("HttpMessageNotReadableException");
            }

            if (ex instanceof HttpMessageNotWritableException) {
                log.error("HttpMessageNotWritableException");
            }

            if (ex instanceof MethodArgumentNotValidException) {
                log.error("MethodArgumentNotValidException");
            }

            if (ex instanceof MissingServletRequestPartException) {
                log.error("MissingServletRequestPartException");
            }

            if (ex instanceof BindException) {
                log.error("BindException");
            }

            if (ex instanceof NoHandlerFoundException) {
                log.error("NoHandlerFoundException");
            }

            if (ex instanceof AsyncRequestTimeoutException) {
                log.error("AsyncRequestTimeoutException");
            }
        } catch (Exception var6) {
            if (log.isWarnEnabled()) {
                log.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", var6);
            }
        }

        return null;
    }
}
