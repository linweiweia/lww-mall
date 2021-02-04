package com.ww.mall.tiny.comom.utlis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-14 15:24
 * @describe:
 *  * JwtToken生成的工具类
 *  * JWT token的格式：header.payload.signature
 *  * header的格式（算法、token的类型）：
 *  * {"alg": "HS512","typ": "JWT"}
 *  * payload的格式（用户名、创建时间、生成时间）：
 *  * {"sub":"wang","created":1489079981393,"exp":1489684781}
 *  * signature的生成算法：
 *  * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 *
 *  主要提供三个方法
 *  1.根据用户信息生成token     generateToken(UserDetails userDetails)
 *  2.根据token获取用户信息     getUserNameFromToken(String token)
 *  3.验证token是否还有效       validateToken(String token, UserDetails userDetails)
 */

@Component
public class JwtTokenUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtils.class);
    //声明header.payload.signature中的payload负载因子放用户名称的key
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 1.根据用户信息生成token
     * @param userDetails
     * @return token
     */
    public String generate(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 根据用户信息生成token 用户信息（用户姓名、创建时间）
     * 并且指定过期时间，加密方式和加密秘钥。
     * @param claims   用户信息（用户姓名、创建时间）
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token的过期时间  毫秒锁*1000 token的过期时间一天
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }


    /**
     * 2.根据token获取用户信息
     * @param token  客户端传递的token
     * @return  用户信息
     */
    public String getUserNameFromToken(String token) {
        String userName;
        try {
            Claims claims = getClaimFromToken(token);
            //从负载因子中获取当前用户
            userName = claims.getSubject();
        } catch (Exception e) {
            userName = null;
        }
        return userName;
    }

    /**
     * 根据token获取token中的负载因子
     * 需要传入加入的秘钥
     * @param token
     * @return
     */
    private Claims getClaimFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式校验失败：{}", token);
        }
        return claims;
    }


    /**
     * 判断token是否有效 就是判断用户名是否正确还有token是否过期
     * @param token         客户端传入的token
     * @param userDetails   从数据可中查询出来的用户信息
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }


    /**
     * 判断是否可以刷新，没有过期才可以刷新token
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     * @return true过期  false没有过期
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDareFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从负载因子中获取过期时间
     */
    private Date getExpiredDareFromToken(String token) {
        Claims claim = getClaimFromToken(token);
        return claim.getExpiration();
    }



}
