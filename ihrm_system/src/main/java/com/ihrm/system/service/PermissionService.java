package com.ihrm.system.service;

import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;

import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.Dao.PermissionApiDao;
import com.ihrm.system.Dao.PermissionDao;
import com.ihrm.system.Dao.PermissionMenuDao;
import com.ihrm.system.Dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private PermissionApiDao permissionApiDao;


    @Autowired
    private IdWorker idWorker;

    /**
     * 1.保存用户
     */
    public void save(Map<String,Object> map) throws Exception {
        //设置主键的值
        String id = idWorker.nextId()+"";
        //调用dao保存部门

        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        permission.setId(id);
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                menu.setId(id);
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                point.setId(id);
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi api = BeanMapUtils.mapToBean(map,PermissionApi.class);
                api.setId(id);
                permissionApiDao.save(api);
                break;
            default:
                throw new Exception();
        }
        permissionDao.save(permission);
    }

    /**
     * 2.更新用户
     */
    public void update(Map<String,Object> map) throws Exception {
        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        Permission perm = permissionDao.findById(permission.getId()).get();
        perm.setName(permission.getName());
        perm.setCode(permission.getCode());
        perm.setDescription(permission.getDescription());
        perm.setEnVisible(permission.getEnVisible());

        int type = permission.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                menu.setId(permission.getId());
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                point.setId(permission.getId());
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi api = BeanMapUtils.mapToBean(map,PermissionApi.class);
                api.setId(permission.getId());
                permissionApiDao.save(api);
                break;
            default:
                throw new Exception();
        }
        permissionDao.save(perm);
    }

    /**
     * 3.根据id查询用户
     */
    public Map<String,Object> findById(String id) throws Exception {
        Permission permission = permissionDao.findById(id).get();
        int type = permission.getType();

        Object o = null;
        if(type == PermissionConstants.PERMISSION_MENU){
            o = permissionMenuDao.findById(id).get();
        }else if(type == PermissionConstants.PERMISSION_POINT){
            o = permissionPointDao.findById(id).get();
        }else if(type == PermissionConstants.PERMISSION_API){
            o = permissionApiDao.findById(id).get();
        }else {
            throw new Exception();
        }
        Map<String, Object> map = BeanMapUtils.beanToMap(o);
        map.put("name",permission.getName());
        map.put("type",permission.getType());
        map.put("code",permission.getCode());
        map.put("description",permission.getDescription());
        map.put("pid",permission.getPid());
        map.put("enVisible",permission.getEnVisible());
        return map;
    }

    /**
     * 4.查询全部用户列表
     *      参数：map集合的形式
     *          hasDept
     *          departmentId
     *          companyId
     *
     */
    public List<Permission> findAll(Map<String,Object> map) {
        //1.需要查询条件
        Specification<Permission> spec = new Specification<Permission>() {
            /**
             * 动态拼接查询条件
             * @return
             */
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if(!StringUtils.isEmpty(map.get("pid"))) {
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据请求的部门id构造查询条件
                if(!StringUtils.isEmpty(map.get("enVisible"))) {
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                if(!StringUtils.isEmpty(map.get("type"))) {
                    String ty = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if("0".equals(ty)) {
                        in.value(1).value(2);
                    }else {
                        in.value(Integer.parseInt(ty));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return  permissionDao.findAll(spec);
    }

    /**
     * 5.根据id删除用户
     */
    public void deleteById(String id) {
        permissionMenuDao.deleteById(id);
    }
}
