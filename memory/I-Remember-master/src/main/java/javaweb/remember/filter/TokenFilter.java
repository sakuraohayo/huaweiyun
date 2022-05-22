package javaweb.remember.filter;

import com.alibaba.fastjson.JSON;
import javaweb.remember.entity.User;
import javaweb.remember.enumeration.ResultEnum;
import javaweb.remember.service.RedisService;
import javaweb.remember.service.UserService;
import javaweb.remember.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Configuration
public class TokenFilter implements Filter {
    //设置用户token过期时间为一个小时（3600秒）
    private static final Long USER_TOKEN_EXPIRE_TIME = 3600L;
    //不用过滤的路由
    private static final String[] URIS = {"/login","/sign_up","/verify_code","/password","/randomMemory", "/get-photo"};

    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "token");

        if("OPTIONS".equals(request.getMethod())){
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return;
        }

        String requestURI = request.getRequestURI();
        for(String uri : URIS){
            if(requestURI.startsWith(uri)){
                filterChain.doFilter(request,response);
                return;
            }
        }


        String token = request.getHeader("token");
        //请求头中没有token
        if(token == null){
            //重定向
            response.setHeader("REDIRECT","redirect");
            response.addHeader("Access-Control-Expose-Headers","REDIRECT");
            returnJson(response,new ResultVo(ResultEnum.NO_LOGIN));
            return;
        }
        //通过token获取email
        String email = redisService.get(token);
        //token失效或者是伪造的
        if(email==null){
            //重定向
            response.setHeader("REDIRECT","redirect");
            response.addHeader("Access-Control-Expose-Headers","REDIRECT");
            returnJson(response,new ResultVo(ResultEnum.NO_LOGIN));
            return;
        }
        User user = userService.findByEmail(email);
        if(user == null){
            //重定向
            response.addHeader("REDIRECT","redirect");
            response.addHeader("Access-Control-Expose-Headers","REDIRECT");
            returnJson(response,new ResultVo(ResultEnum.NO_LOGIN));
            return;
        }

        request.setAttribute("id",user.getId());
        //更新token持续时间
        redisService.delete(token);
        redisService.set(token,email);
        redisService.expire(token,USER_TOKEN_EXPIRE_TIME);
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }

    public static void returnJson(HttpServletResponse response, ResultVo resultVo){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSON.toJSONString(resultVo));
        } catch (IOException e) {
            //由于统一拦截了异常所以不需要抛出
        }
    }
}
