package com.ihrm.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ParseJwtTest {
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJjb21wYW55SWQiOiIxMTEiLCJjb21wYW55TmFtZSI6IuaWp" +
                "-WtkCIsIm5hbWUiOiJmdWZ1ZiIsImlkIjoiMSIsImV4cCI6MTU4MDgyODMyM30" +
                ".WhJ6fWpV8lMZ2JQRW646yM9Thtq2V75ek0Y4yDn1328";
        Claims claims = Jwts.parser().setSigningKey("i4h4r4").parseClaimsJws(token).getBody();
        System.out.println(claims.get("id"));
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());

    }
}
