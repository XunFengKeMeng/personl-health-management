package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.em.ActiveStatusEnum;
import com.example.health.em.RoleEnum;
import com.example.health.mapper.UserMapper;
import com.example.health.pojo.JwtToken;
import com.example.health.pojo.dto.query.extend.UserQueryDTO;
import com.example.health.pojo.dto.update.UserLoginDTO;
import com.example.health.pojo.dto.update.UserRegisterDTO;
import com.example.health.pojo.dto.update.UserUpdateDTO;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.HealthStatusStatisticsVO;
import com.example.health.pojo.vo.UserVO;
import com.example.health.service.UserService;
import com.example.health.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author huanghaiming
 * @date 2025/3/10 11:05
 * @description: 用户服务
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 持久层接口注入
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 注入JWT
     */
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户注册（用户）
     *
     * @param userRegisterDTO 用户注册信息（账号、密码、用户名、邮箱）
     * @return 注册结果提示信息
     */
    @Override
    public ApiResponse<String> register(UserRegisterDTO userRegisterDTO) {
        // 根据用户账号查询数据库得到对应的用户数据
        UserDO userDO = userMapper.getByCondition(
                UserDO.builder().userAccount(userRegisterDTO.getUserAccount()).build()
        );
        // 判断用户账号是否已存在
        return Optional.ofNullable(userDO)
                .map(user -> {
                    // 显式指定泛型类型
                    return ApiResponse.<String>error("用户已存在");
                })
                .orElseGet(() -> {
                    // 新增用户数据（身份默认为用户，且默认账号未激活）
                    UserDO user = UserDO.builder()
                            .userAccount(userRegisterDTO.getUserAccount())
                            .userName(userRegisterDTO.getUserName())
                            .userPassword(userRegisterDTO.getUserPassword())
                            .userEmail(userRegisterDTO.getUserEmail())
                            .userRole(RoleEnum.USER.getRoleCode())
                            .active(ActiveStatusEnum.INACTIVE.getStatusCode())
                            .sex(userRegisterDTO.getSex())
                            .departmentName(userRegisterDTO.getDepartmentName())
                            .userCreateTime(LocalDateTime.now()).build();
                    userMapper.insertUser(user);
                    return ApiResponse.success("注册成功");
        });
    }

    /**
     * 新增用户（管理员）
     *
     * @param userRegisterDTO 管理员输入注册用户信息
     * @return 用户新增响应结果
     */
    @Override
    public ApiResponse<String> insertUser(UserRegisterDTO userRegisterDTO) {
        return register(userRegisterDTO);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户登录响应结果
     */
    @Override
    public ApiResponse<Object> login(UserLoginDTO userLoginDTO) {
        // 根据用户账号查询数据库得到对应的用户数据
        UserDO userDO = userMapper.getByCondition(
                UserDO.builder().userAccount(userLoginDTO.getUserAccount()).build()
        );
        // 检查用户是否存在
        return Optional.ofNullable(userDO)
                .map(user -> {
                    // 检查密码是否正常
                    if (!user.getUserPassword().equals(userLoginDTO.getUserPassword())) {
                        return ApiResponse.<Object>error("密码错误");
                    }
                    // 检查账号是否已激活
                    if (!user.getActive()) {
                        return ApiResponse.<Object>error("账号未激活，请联系管理员");
                    }
                    // 用户登录成功
                    String token = jwtUtil.toToken(user.getUserId(), user.getUserRole());
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", token);
                    map.put("role", user.getUserRole());
                    // Shiro
                    Subject subject = SecurityUtils.getSubject();
                    JwtToken jwtToken = new JwtToken(token);
                    try {
                        // 进行验证。如果不加，在首次访问Shiro管理的接口时，无法直接认证授权信息。
                        subject.login(jwtToken);
                    } catch (UnknownAccountException e) {
                        return ApiResponse.<Object>error("用户不存在");
                    } catch (AuthenticationException | AuthorizationException e) {
                        return ApiResponse.error("账号密码错误或没有权限");
                    }
                    return ApiResponse.<Object>success("登录成功",map);
                }).orElseGet(() -> ApiResponse.<Object>error("用户不存在"));
    }

    /**
     * 令牌校验，认证成功则返回用户信息
     *
     * @return 用户信息 + 响应结果
     */
    @Override
    public ApiResponse<UserVO> auth(String token) {
        Claims claims = jwtUtil.fromToken(token);
        return Optional.ofNullable(claims)
                .map(c -> {
                    Integer userId = c.get("id", Integer.class);
                    UserDO userDO = UserDO.builder().userId(userId).build();
                    UserDO user = userMapper.getByCondition(userDO);
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return ApiResponse.<UserVO>success(userVO);
                }).orElseGet(()-> ApiResponse.error("令牌校验未通过"));
    }

    /**
     * 批量删除用户信息（管理员）
     *
     * @param ids 若干需删除用户的ID
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteUsers(List<Integer> ids) {
        userMapper.deleteUsers(ids);
        return ApiResponse.success("删除成功");
    }

    /**
     * 修改用户信息（用户）
     *
     * @param userUpdateDTO 修改后的用户数据用户账号、用户名、用户邮箱）
     * @return 修改操作响应结果
     */
    @Override
    public ApiResponse<String> updateSelf(UserUpdateDTO userUpdateDTO) {
        UserDO userDO = userMapper.getByCondition(
                UserDO.builder().userId(userUpdateDTO.getUserId()).build()
        );
        BeanUtils.copyProperties(userUpdateDTO, userDO);
        userMapper.updateUser(userDO);
        return ApiResponse.success("修改成功");
    }

    /**
     * 修改密码（用户）
     *
     * @param map 用户账号及修改后的密码信息
     * @return 修改操作响应结果
     */
    @Override
    public ApiResponse<String> updatePassword(Map<String, String> map) {
        String userAccount = map.get("userAccount");
        String newPassword = map.get("newPassword");
        // 获取用户原数据
        UserDO userDO = userMapper.getByCondition(
                UserDO.builder().userAccount(userAccount).build()
        );
        // 修改数据库数据
        userDO.setUserPassword(newPassword);
        userMapper.updateUser(userDO);
        return ApiResponse.success("密码修改成功");
    }

    /**
     * 修改用户信息（管理员）
     * 比用户修改信息多出用户角色、用户账号激活状态信息
     *
     * @param userUpdateDTO 修改后的用户数据
     * @return 修改操作响应结果
     */
    @Override
    public ApiResponse<String> updateByAdmin(UserUpdateDTO userUpdateDTO) {
        return updateSelf(userUpdateDTO);
    }


    /**
     * 分页+模糊查询用户（管理员）
     *
     * @param userQueryDTO 用户模糊查询参数
     * @return 查询响应结果
     */
    @Override
    public ApiResponse<List<UserVO>> queryUsers(UserQueryDTO userQueryDTO) {
        List<UserVO> userVOList = userMapper.queryUsers(userQueryDTO);
        Integer count = userMapper.queryCount(userQueryDTO);
        return ApiResponse.success(userVOList, count);
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 若用户存在则返回用户信息，若不存在则返回error
     */
    @Override
    public ApiResponse<UserVO> getByUserId(Integer userId) {
        UserDO userDO = userMapper.getByCondition(UserDO.builder().userId(userId).build());
        // Optional判空，并根据判空结果调用相应的响应方法作为返回值。
        return Optional.ofNullable(userDO)
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return ApiResponse.success(userVO);
                }).orElseGet(()-> ApiResponse.error("用户不存在"));
    }

    /**
     * 获取健康状态码及每个健康状态下的用户数
     *
     * @return 健康状态码及每个健康状态下的用户数
     */
    @Override
    public ApiResponse<List<HealthStatusStatisticsVO>> getHealthStatusStatistics() {
        List<HealthStatusStatisticsVO> healthStatusStatisticsVOList = userMapper.queryHealthStatusStatistics();
        return ApiResponse.success(healthStatusStatisticsVOList);
    }

}
