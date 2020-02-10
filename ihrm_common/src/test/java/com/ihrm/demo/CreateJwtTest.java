package com.ihrm.demo;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateJwtTest {
    public static void main(String[] args) {
//        JwtBuilder builder = Jwts.builder().setId("1").setSubject("fufuf")
//                .setIssuedAt(new Date())
//                .signWith(SignatureAlgorithm.HS256,"i4h4r4")
//                .claim("companyId",1);
//        String token = builder.compact();
//        System.out.println(token);

        long now = System.currentTimeMillis();
        long exp = now +3600000;

        JwtBuilder builder = Jwts.builder().setId("1").setSubject("fufuf")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"i4h4r4");
        Map<String,Object> map = new HashMap<>();
        map.put("companyId","111");
        map.put("companyName","斧子");
        map.put("id","1");
        map.put("name","fufuf");
        builder.setClaims(map);
        builder.setExpiration(new Date(exp));
        String token = builder.compact();
        System.out.println(token);
    }
}
