package com.wy.flux.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    private Map<String, User> userMap = new HashMap<>();
    private Map<String, User> tokenUserMap = new HashMap<>();

    @PostConstruct
    private void init() {
        User[] users = new User[] { new User("lidan", "123456", "李丹", 26), new User("weiyang", "12345689", "卫杨", 25), };
        for (User user : users) {
            userMap.put(user.getUsername(), user);
        }
    }

    @PostMapping("/login")
    public UserLoginResult login(@RequestBody UserLoginParam param) {
        if (StringUtils.isEmpty(param.getUsername()) || StringUtils.isEmpty(param.getPassword())) {
            return new UserLoginResult(UserLoginResultEnum.USERNAME_PASSWORD_IS_EMPTY);
        }
        User user = userMap.get(param.getUsername());
        if (param.getPassword().equals(user.getPassword())) {
            String token = UUID.randomUUID().toString().replace("-", "");
            tokenUserMap.put(token, user);
            logger.info("login success . username:{},token:{}",param.getUsername(),token);
            return new UserLoginResult(UserLoginResultEnum.SUCCESS, token);
        } else {
            return new UserLoginResult(UserLoginResultEnum.USERNAME_PASSWORD_ERROR);
        }
    }

    @PostMapping("/getCurrentLoginUser")
    public ResultVo<User> getCurrentLoginUser(@RequestHeader("token") String token) {
        User user = tokenUserMap.get(token);
        if (user != null) {
            return ResultVo.success(user);
        } else {
            return ResultVo.loginTimeout();
        }
    }

    @PostMapping("/findUser")
    public ResultVo<User> findUser(@RequestHeader("token") String token, @RequestBody FindUserParam param) {
        if (!checkLogin(token)) {
            return ResultVo.loginTimeout();
        }
        User user = userMap.get(param.getUsername());
        return ResultVo.success(user);
    }

    @PostMapping("/addUser")
    public ResultVo<AddUserResult> addUser(@RequestHeader("token") String token, @RequestBody User user) {
        if (!checkLogin(token)) {
            return ResultVo.loginTimeout();
        }
        if (userMap.get(user.getUsername()) != null) {
            return ResultVo.success(AddUserResult.FAIL);
        }
        userMap.put(user.getUsername(), user);
        return ResultVo.success(AddUserResult.SUCCESS);
    }

    private boolean checkLogin(String token) {
        return tokenUserMap.get(token) != null;
    }
}

class AddUserResult {

    public static final AddUserResult SUCCESS = new AddUserResult(1, "添加成功");
    public static final AddUserResult FAIL = new AddUserResult(2, "用户名已存在");

    private int code;
    private String msg;

    private AddUserResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

class FindUserParam {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

class ResultVo<T> {

    int code;
    String msg;
    T data;

    private ResultVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultVo(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultVo<T> success() {
        return new ResultVo<T>(1, "成功");
    }

    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<T>(1, "成功", data);
    }

    public static <T> ResultVo<T> loginTimeout() {
        return new ResultVo<T>(2, "登录超时");
    }

    public static <T> ResultVo<T> fail() {
        return new ResultVo<T>(3, "失败");
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

class UserLoginParam {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

class UserLoginResult {

    private int code;
    private String msg;
    private String token;

    public UserLoginResult(UserLoginResultEnum userLoginResultEnum) {
        this.code = userLoginResultEnum.getCode();
        this.msg = userLoginResultEnum.getMsg();
    }

    public UserLoginResult(UserLoginResultEnum userLoginResultEnum, String token) {
        this.code = userLoginResultEnum.getCode();
        this.msg = userLoginResultEnum.getMsg();
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

enum UserLoginResultEnum {
    SUCCESS(1, "登录成功"), USERNAME_PASSWORD_IS_EMPTY(2, "用户名密码不能为空"), USERNAME_PASSWORD_ERROR(3, "用户名密码错误");
    private int code;
    private String msg;

    UserLoginResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}