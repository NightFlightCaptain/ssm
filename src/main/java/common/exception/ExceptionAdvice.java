package common.exception;

import auth.UserRealm;
import model.Msg;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * 全局异常处理,所有的异常都放在这里进行处理,无需在每个地方try catch
 *
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/6/1 8:37
 */
@RestControllerAdvice
public class ExceptionAdvice {

	private static final Logger LOGGER = LogManager.getLogger(UserRealm.class);
	/**
	 * 信息无法读取
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Msg handleHttpMessageNotReadableException(Exception e){
		e.printStackTrace();
		return Msg.message(400,"无法读取");
	}

	/**
	 * 处理参数异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Msg handleMethodArgumentNotValidException(Exception e){
		return Msg.message(400,"参数验证失败");
	}

	/**
	 * 处理自定义异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(IException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	public Msg handleIException(IException e){
		return Msg.message(417,"自定义异常");
	}

	/**
	 * 数学计算错误
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ArithmeticException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Msg handleArithmeticException(ArithmeticException e){
		return Msg.message(500,"服务器内部错误");
	}

	/**
	 * 登陆错误
	 * @param e
	 * @return
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Msg handleAuthenticationException(AuthenticationException e){
		LOGGER.error(e);
		return Msg.message(401,"登陆错误");
	}

	@ExceptionHandler(UnknownAccountException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Msg handleUnknownAccountException(UnknownAccountException e){
		LOGGER.error(e);
		return Msg.message(401,"请登录");
	}


	/**
	 * 没有权限——shiro
	 * @param e
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Msg handleUnauthorizedException(UnauthorizedException e){
		return Msg.message(403,"没有权限");
	}
}
