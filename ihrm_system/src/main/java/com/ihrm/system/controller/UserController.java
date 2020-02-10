package com.ihrm.system.controller;

import com.ihrm.common.Controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;

import com.ihrm.common.utils.JwtUtils;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private DepartmentFeignClient departmentFeignClient;



    @RequestMapping("user/upload/{id}")
    public Result upload(@RequestParam("id")String id,@RequestParam("file")MultipartFile file ){
        String imgUrl = userService.uploadImage(id,file);
        return new Result(ResultCode.SUCCESS,imgUrl)
    }

    @RequestMapping(value = "/user/import",method = RequestMethod.POST)
    public Result importUser(@RequestParam(name = "file") MultipartFile file) throws Exception {
        Workbook wb  = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        List<User> list = new ArrayList<>();
        for(int rowNum= 1;rowNum<=sheet.getLastRowNum();rowNum++) {
            Row row = sheet.getRow(rowNum);
            Object[] values = new Object[row.getLastCellNum()];
            for (int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++) {
                Cell cell = row.getCell(cellNum);
                Object value = getCellValue(cell);
                values[cellNum] = value;
            }
            User user = new User(values);
            list.add(user);
        }
        //批量保存
        userService.saveAll(list,companyId,companyName);
        return new Result(ResultCode.SUCCESS);

    }

    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) {
        String userId= (String)map.get("id");
        List<String> roleIds = (List<String>)map.get("roleIds");
        userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/test/{id}",method = RequestMethod.POST)
    public Result testFeign(@PathVariable(value = "id")String id){
        Result result = departmentFeignClient.findById(id);
        return result;
    }


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //1.设置保存的企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //2.调用service完成保存企业
        userService.save(user);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result findAll(int page,int size,@RequestParam Map map){
        map.put("companyId",companyId);
        Page<User> pageUser = userService.findAll(map,page,size) ;
        PageResult pageResult = new PageResult(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);

    }

    /**
     * 根据ID查询user
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {

        User user = userService.findById(id);
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    /**
     * 修改User
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody User user) {
        //1.设置修改的部门id
        user.setId(id);
        //2.调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap){
        String moblie = loginMap.get("mobile");
        String password = loginMap.get("password");
        password = new Md5Hash(password,moblie,3).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(moblie,password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        String sessionId = (String)subject.getSession().getId();

//        User user = userService.findByMoblie(moblie);
//        if(user == null || !user.getPassword().equals(password))
//            return new Result(ResultCode.MOBLIEORPASSWORDERROR);
//        else {
//            StringBuilder sb = new StringBuilder();
//            for(Role role : user.getRoles()){
//                for(Permission permission : role.getPermissions()){
//                    if(permission.getType() == PermissionConstants.PERMISSION_API){
//                        sb.append(permission.getClass()).append(",");
//                    }
//                }
//            }
//            Map<String,Object> map = new HashMap<>();
//            map.put("apis",sb.toString());
//            map.put("companyId",user.getCompanyId());
//            map.put("companyName",user.getCompanyName());
//            map.put("id",user.getId());
//            map.put("name",user.getUsername());
//            String token = jwtUtils.createJwt(user.getId(),user.getUsername(),map);
        return new Result(ResultCode.SUCCESS,sessionId);

    }

    @RequestMapping(value = "/profile",method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {
//        String userId = (String)claims.get("id");
//
//        User user = userService.findById(userId);
//        ProfileResult profileResult = null;
////所有权限
//        if("user".equals(user.getLevel())){
//            profileResult = new ProfileResult(user);
//        }else {
//            Map map = new HashMap();
//            if ("coAdmin".equals(user.getLevel())){
//                map.put("enVisible","1");
//            }
//            List<Permission> list = permissionService.findAll(map);
//            profileResult = new ProfileResult(user,list);
//        }

        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        //2.获取安全数据
        ProfileResult result = (ProfileResult)principals.getPrimaryPrincipal();

        return new Result(ResultCode.SUCCESS,result);
    }

    public static Object getCellValue(Cell cell){
        CellType cellType = cell.getCellType();
        Object value = null;
        switch (cellType){
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell))
                    value = cell.getDateCellValue();
                else
                    value = cell.getNumericCellValue();
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }
}
