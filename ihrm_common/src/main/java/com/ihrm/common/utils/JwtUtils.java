package com.ihrm.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    //siyao
    private String key;
    //shi xiao shi jian
    private Long ttl;
    /*she zhi token*/

    public String createJwt(String id, String name, Map<String,Object> map){
        long now = System.currentTimeMillis();
        long exp = now +ttl;

        JwtBuilder builder = Jwts.builder().setId(id).setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,key);
        builder.setClaims(map);
        builder.setExpiration(new Date(exp));
        String token = builder.compact();
        return token;
    }

    /*jie xi token*/
    public Claims parseJWT(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
        }catch (Exception e){
        }
        return claims;
    }
}
