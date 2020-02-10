package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private IdWorker idWorker;
    /*
    * 1配置IdWork
    * 2注入idwork
    * 3通过idwork生成id
    * 4保存企业
    *
    * */
    public void add(Company company){
        String id = idWorker.nextId()+"";
        company.setId(id);
        company.setAuditState("1");
        company.setState(1);//激活
        companyDao.save(company);
    }

    /*
    * 更新企业
    * 1参数
    * 2根据id查询
    * 3修改属性
    * 4dao进行更新
    * */
    public void update(Company company){
        Company tmp = companyDao.findById(company.getId()).get();
        tmp.setName(company.getName());
        tmp.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(tmp);
    }
    /*
    * 删除企业
    * */
    public void deleteById(String id){
        companyDao.deleteById(id);
    }
    /*
     * 删除企业
     * */
    public Company findById(String id){
        return companyDao.findById(id).get();
    }
    /*
     * 删除企业
     * */
    public List<Company> findAll(){
        return companyDao.findAll();
    }
}
