package com.ihrm.system.controller;

import com.ihrm.common.Controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/sys")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/permission", method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        //1.设置保存的企业id

        //2.调用service完成保存企业
        permissionService.save(map);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public Result findAll(@RequestParam Map map){
        List<Permission> list =  permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS,list);

    }

    /**`
     * 根据ID查询user
     */
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) throws Exception {
        Map map = permissionService.findById(id);

        return new Result(ResultCode.SUCCESS, map);
    }

    /**
     * 修改User
     */
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody Map<String,Object> map) throws Exception {
        //1.设置修改的部门id
        map.put("id",id);
        //2.调用service更新
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
