package com.newbig.app.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newbig.app.util.CustomDateEditor;
import com.newbig.app.util.UserRemindException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
@Slf4j
public class TtControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String, Object> exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ex.printStackTrace();
        log.error(request.getRequestURI(), ex);
        response.setCharacterEncoding("UTF-8"); //避免乱码

        if (ex instanceof UserRemindException) {
            if (ex.getMessage().contains("请退出重新登录")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
            return failure(ex.getMessage());
        } else if (ex instanceof DuplicateKeyException) {
            Pattern pattern = Pattern.compile("Duplicate entry '(.*)' for key '(.*)'");
            Matcher matcher = pattern.matcher(ex.getCause().getMessage());
            if (matcher.matches()) {
                String key = matcher.group(2);
                String value = matcher.group(1);
                return failure(key.replace("_UNIQUE", "") + ":" + value + " 已存在，请修改后重新添加");
            } else {
                return failure("系统有重复数据");
            }
        } else if (ex instanceof DataIntegrityViolationException) {
            if (ex.getCause().getMessage().contains("Data too long for column")) {
                Pattern pattern = Pattern.compile(".*Data too long for column '(.*)' at row.*");
                Matcher matcher = pattern.matcher(ex.getCause().getMessage());
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    return failure("字段[" + key + "]长度超出限制");
                }
                return failure("某些字段长度超出限制");
            } else if (ex.getCause().getMessage().contains("doesn't have a default value")) {
                Pattern pattern = Pattern.compile(".*Field '(.*)' doesn't have a default value.*");
                Matcher matcher = pattern.matcher(ex.getCause().getMessage());
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    return failure("字段[" + key + "]没有默认值");
                }
                return failure("某些字段没有默认值");
            } else {
                return failure(ex.getCause().getMessage());
            }
        } else if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            List<String> errorMesssages = Lists.newArrayList();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMesssages.add(fieldError.getField() + fieldError.getDefaultMessage());
                break;
            }
            //只返回第一条
            return failure(errorMesssages.get(0));
        } else if (ex instanceof MissingServletRequestParameterException) {
            return failure("参数缺失");
        } else if (ex instanceof BindException) {
            BindingResult bindingResult = ((BindException) ex).getBindingResult();
            List<String> errorMesssages = Lists.newArrayList();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMesssages.add(fieldError.getDefaultMessage());
                break;
            }
            return failure(errorMesssages.get(0));
        } else if (ex instanceof RuntimeException) {
            if ("Value for mobile cannot be null".equals(ex.getMessage())) {
                return failure("条件中包含值为null的字段");
            } else {
                return failure("系统运行时异常");
            }
        } else {
            return failure("系统异常");
        }
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        //model.addAttribute("msg", "额外信息");
    }

    //前端传过来的String 转成Date 还未验证
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(true));
    }

    public Map<String, Object> failure(int errorCode, Object message, String exception) {

        Map<String, Object> resultMap = Maps.newHashMap();

        resultMap.put("status", false);
        resultMap.put("code", errorCode);
        if (message != null) {
            resultMap.put("message", message);
        }
        if (exception != null) {
            resultMap.put("exception", exception);
        }

        return resultMap;

    }

    public Map<String, Object> failure(Object message) {
        return failure(500, message, null);
    }
}
