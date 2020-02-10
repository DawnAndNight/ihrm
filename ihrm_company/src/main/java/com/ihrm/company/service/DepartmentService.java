package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService extends BaseService<Department> {
    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Department department){
        String id = idWorker.nextId()+"";
        department.setId(id);
        departmentDao.save(department);
    }

    public void update(Department department){
        Department dept = departmentDao.findById(department.getId()).get();
        dept.setCode(department.getCode());
        dept.setIntroduce(department.getIntroduce());
        dept.setName(department.getName());
        departmentDao.save(dept);
    }

    public Department findById(String id){
        return departmentDao.findById(id).get();
    }

    public List<Department> findAll(String companyId){
        /*
        * 用户构造查询条件
        * root:包含了所有对象的数据
        * cq : 一般不用
        * cb：构造查询条件
        * */
//        Specification<Department> spec = new Specification<Department>() {
//            @Override
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };
        Specification<Department> spec = getSpec(companyId);
        return departmentDao.findAll(spec);
    }
    public void deleteById(String id){
         departmentDao.deleteById(id);
    }

    public Department findByCode(String code, String companyId) {
        return departmentDao.findByCodeAndCompanyId(code,companyId);
    }
}
