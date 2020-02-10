package com.ihrm.common.Controller;

import com.ihrm.domain.system.response.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    public HttpServletRequest request;
    public HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected Claims claims;
//jwt获取
//    @ModelAttribute
//    public void setResAnReq(HttpServletRequest request,HttpServletResponse response){
//        this.request = request;
//        this.response = response;
//        Object o = request.getAttribute("user_claims");
//        if (o != null){
//            this.claims = (Claims)o;
//            this.companyId = (String)claims.get("companyId");
//            this.companyName=(String)claims.get("companyName");
//        }
//
//        this.companyId = "1";
//        this.companyName="ff";
//    }
    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPreviousPrincipals();
        if (principals!=null && !principals.isEmpty()) {
            ProfileResult profileResult = (ProfileResult) principals.getPrimaryPrincipal();
            this.companyId = "1";
            this.companyName=profileResult.getCompany();
        }

    }
}
