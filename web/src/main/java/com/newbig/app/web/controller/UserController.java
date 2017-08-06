package com.newbig.app.web.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.github.pagehelper.PageInfo;
import com.newbig.app.web.model.User;
import com.newbig.app.web.model.dto.LoginDto;
import com.newbig.app.web.model.dto.UserDto;
import com.newbig.app.web.model.vo.ResponseVO;
import com.newbig.app.web.service.UserService;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "/user")
@Api(value = "用户管理", description = "111",tags = "用户管理")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取用户列表", tags = {"用户"}, notes = "分页 pageNum pageSize", httpMethod = "GET")
    public ResponseVO<PageInfo<User>> list(@ApiParam(value = "页数", required = false) @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                           @ApiParam(value = "每页几条", required = false) @RequestParam(required = false, defaultValue = "29") Integer pageSize) {
        return ResponseVO.success(userService.getUserListByPage(pageNum, pageSize));
    }

    @PostMapping(value = "/doLogin")
    @ApiOperation(
        value = "用户登录", notes = "测试 " +
        "error conditions",
        response = ResponseVO.class,
        authorizations = {
            @Authorization(value = "api_key"),
            @Authorization(value = "petstore_auth", scopes = {
                @AuthorizationScope(scope = "write_pets", description = ""),
                @AuthorizationScope(scope = "read_pets", description = "")
            })})
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found")}
    )    public ResponseVO<String> doLogin(@Valid @RequestBody LoginDto loginDto) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                .withIssuer("auth0")
                .withExpiresAt(new DateTime().minusDays(100).toDate())
                .withSubject("aaaaaa")
                .sign(algorithm);
            return ResponseVO.success(token);
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return ResponseVO.success("---");
    }

    @GetMapping(value = "/get")
    @ApiOperation(value = "用户详情", tags = {"用户"}, notes = "id", httpMethod = "GET")
    public ResponseVO<User> get(@RequestParam(required = false) Long id) {
        if (id == null) {
            return ResponseVO.failure("参数不能为空");
        }
        return ResponseVO.success(userService.getUserById(id));
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除用户", tags = {"用户"}, notes = "id", httpMethod = "GET")
    public ResponseVO<Integer> delete(@RequestBody(required = false) Long id) {
        if (id == null) {
            return ResponseVO.failure("参数不能为空");
        }
        return ResponseVO.success(userService.deleteUserById(id));
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新用户", tags = {"用户"}, notes = "id", httpMethod = "POST")
    public ResponseVO<User> update(@RequestBody(required = false) UserDto userDto) {
        if (userDto == null) {
            return ResponseVO.failure("参数不能为空");
        }
        return ResponseVO.success(userService.updateUser(userDto));
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加用户", tags = {"用户"}, notes = "id", httpMethod = "POST")
    public ResponseVO<User> add(@RequestBody(required = false) UserDto userDto) {
        if (userDto == null) {
            return ResponseVO.failure("参数不能为空");
        }
        return ResponseVO.success(userService.addUser(userDto));
    }
}
