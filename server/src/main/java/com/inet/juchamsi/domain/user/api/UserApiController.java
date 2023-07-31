package com.inet.juchamsi.domain.user.api;

import com.inet.juchamsi.domain.user.application.UserService;
import com.inet.juchamsi.global.api.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"사용자 계정"})
@RequestMapping("/user")
public class UserApiController {

    private final UserService userService;


    @ApiOperation(value = "아이디 중복 체크", notes = "사용자는 중복되지 않은 아이디로 회원가입 할 수 있다")
    @GetMapping("/id/{id}")
    public ApiResult<Void> checkId(@ApiParam(value = "user-id") @PathVariable(value = "id") String userId) {
        return null;
    }
}
