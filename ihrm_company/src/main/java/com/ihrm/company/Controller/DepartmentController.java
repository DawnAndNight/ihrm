package com.ihrm.company.Controller;

import com.ihrm.common.Controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;

import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.DTO.DeptListResult;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CompanyService companyService;
    @RequestMapping(value = "/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department department){
        department.setCompanyId(companyId);
        department.setCreateTime(new Date());
        departmentService.save(department);
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value = "/department",method = RequestMethod.GET)
    public Result findAll(){
        Company company = companyService.findById(companyId);
        List<Department> list = departmentService.findAll(companyId);
        DeptListResult deptListResult = new DeptListResult(company,list);
        return new Result(ResultCode.SUCCESS,deptListResult);
    }
    @RequestMapping(value = "/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id")String id){
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }

    @RequestMapping(value = "/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id")String id,@RequestBody Department department){
        department.setId(id);
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value = "/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id")String id){
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/search",method = RequestMethod.POST)
    public Department findByCode(@RequestParam("code")String code,@RequestParam("companyId")String companyId){
        Department dept = departmentService.findByCode(code,companyId);
        return dept;
    }
}
