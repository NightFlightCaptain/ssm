package model;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回格式
 *
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/6/1 8:43
 */
public class Msg {

	private int code;

	private String message;

	private Map<String,Object> data = new HashMap<String, Object>();

	private Msg(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static Msg message(int code,String message){
		Msg result = new Msg(code,message);
		return result;
	}
	/**
	 * 处理成功时返回的数据
	 * @return
	 */
	public static Msg success(){
		Msg result = new Msg(200,"处理成功");
		return result;
	}

	public static Msg success(String message){
		Msg result = new Msg(200,message);
		return result;
	}

	public static Msg error(){
		Msg result = new Msg(400,"处理失败");
		return result;
	}
	public static Msg error(String message){
		Msg result = new Msg(400,message);
		return result;
	}

	/**
	 * 添加封装的数据，实现链式编程
	 * @param key
	 * @param value
	 * @return
	 */
	public Msg add(String key,Object value){
		this.data.put(key,value);
		return this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Msg{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
