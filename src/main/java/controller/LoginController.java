package controller;

import io.swagger.annotations.ApiParam;
import model.Msg;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/6/5 10:03
 */
@RestController
public class LoginController {

	@GetMapping("/login")
	public Msg login(HttpServletRequest request,
	                 @ApiParam(defaultValue = "wan2")@RequestParam("account") String account,
	                 @ApiParam(defaultValue = "123")@RequestParam("password") String password) throws Exception {
//		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		Subject subject = SecurityUtils.getSubject();
		System.out.println("喵喵喵");
		UsernamePasswordToken token = new UsernamePasswordToken(account, password);
		subject.login(token);
		return Msg.success("登陆成功");
//		if (exceptionClassName!=null){
//			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
//				//最终会抛给异常处理器
//				throw new IException("账号不存在");
//			} else if (IncorrectCredentialsException.class.getName().equals(
//					exceptionClassName)) {
//				throw new IException("用户名/密码错误");
//			} else if("randomCodeError".equals(exceptionClassName)){
//				throw new IException("验证码错误");
//			} else{
//				throw new Exception();//最终在异常处理器生成未知错误
//			}
//		}
	}

	@GetMapping("/logout")
	public Msg logout(){
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		return Msg.success("成功退出");
	}
}
