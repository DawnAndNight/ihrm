package com.ihrm.system.shiro.realm;
//
//import com.ihrm.common.shiro.realm.IhrmRealm;
//import com.ihrm.domain.system.Permission;
//import com.ihrm.domain.system.User;
//import com.ihrm.domain.system.response.ProfileResult;
//import com.ihrm.system.service.PermissionService;
//import com.ihrm.system.service.UserService;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class UserRealm extends IhrmRealm {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private PermissionService permissionService;
//
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
//        String username = upToken.getUsername();
//        String password = upToken.getPassword()
//        return super.doGetAuthenticationInfo(authenticationToken);
//
//        //2.根据用户名查询数据库
//        User byMoblie = userService.findByMoblie(mobile);
//        if(byMoblie != null && byMoblie.getPassword().equals(password)) {
//            //4.如果一致返回安全数据
//            //构造方法：安全数据，密码，realm域名
//            ProfileResult result = null;
//            if("user".equals(byMoblie.getLevel())){
//                result = new ProfileResult(byMoblie);
//            }else {
//                Map map = new HashMap();
//                if ("coAdmin".equals(byMoblie.getLevel())){
//                    map.put("enVisible","1");
//                }
//                List<Permission> list = permissionService.findAll(map);
//                result = new ProfileResult(byMoblie,list);
//            }
//            SimpleAuthenticationInfo info;
//            return info;
//        }
//        return null;
//
//    }
//}

import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 自定义的realm
 */
public class UserRealm extends IhrmRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;


    /**
     * 授权方法
     *      操作的时候，判断用户是否具有响应的权限
     *          先认证 -- 安全数据
     *          再授权 -- 根据安全数据获取用户具有的所有操作权限
     *
     *
     */

    /**
     * 认证方法
     *  参数：传递的用户名密码
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取登录的用户名密码（token）
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String password = new String( upToken.getPassword());
        //2.根据用户名查询数据库
        User user = userService.findByMoblie(username);
        //3.判断用户是否存在或者密码是否一致
        if(user != null && user.getPassword().equals(password)) {
            //4.如果一致返回安全数据
            //构造方法：安全数据，密码，realm域名
            ProfileResult result = null;
            if("user".equals(user.getLevel())){
                result = new ProfileResult(user);
            }else {
                Map map = new HashMap();
                if ("coAdmin".equals(user.getLevel())){
                    map.put("enVisible","1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user,list);
            }
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result,user.getPassword(),this.getName());
            return info;
        }
        //5.不一致，返回null（抛出异常）
        return null;
    }
}
