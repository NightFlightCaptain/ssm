package auth;

import model.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 *
 * 使用Redis来管理权限信息
 *
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/6/6 11:20
 */
@Component
public class RedisCacheManager implements CacheManager {
	private String cacheKeyPrefix = "shiro:";

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new ShiroRedisCache<K,V>(cacheKeyPrefix+name);
	}

	/**
	 * 为shiro量身定做的一个redis cache,为Authorization cache做了特别优化
	 */
	public class ShiroRedisCache<K, V> implements Cache<K, V> {

		private String cacheKey;

		public ShiroRedisCache(String cacheKey) {
			this.cacheKey=cacheKey;
		}

		@Override
		public V get(K key) throws CacheException {
			BoundHashOperations<String,K,V> hash = redisTemplate.boundHashOps(cacheKey);
			Object k=hashKey(key);
			return hash.get(k);
		}

		@Override
		public V put(K key, V value) throws CacheException {
			BoundHashOperations<String,K,V> hash = redisTemplate.boundHashOps(cacheKey);
			Object k=hashKey(key);
			hash.put((K)k, value);
			return value;
		}

		@Override
		public V remove(K key) throws CacheException {
			BoundHashOperations<String,K,V> hash = redisTemplate.boundHashOps(cacheKey);

			Object k=hashKey(key);
			V value=hash.get(k);
			hash.delete(k);
			return value;
		}

		@Override
		public void clear() throws CacheException {
			redisTemplate.delete(cacheKey);
		}

		@Override
		public int size() {
			BoundHashOperations<String,K,V> hash = redisTemplate.boundHashOps(cacheKey);
			return hash.size().intValue();
		}

		@Override
		public Set<K> keys() {
			BoundHashOperations<String,K,V> hash = redisTemplate.boundHashOps(cacheKey);
			return hash.keys();
		}

		@Override
		public Collection<V> values() {
			BoundHashOperations<String,K,V> hash = redisTemplate.boundHashOps(cacheKey);
			return hash.values();
		}

		protected Object hashKey(K key) {

			if(key instanceof PrincipalCollection) {//此处很重要,如果key是登录凭证,那么这是访问用户的授权缓存;将登录凭证转为user对象,返回user的id属性做为hash key,否则会以user对象做为hash key,这样就不好清除指定用户的缓存了
				PrincipalCollection pc=(PrincipalCollection) key;
				SysUser sysUser=(SysUser) pc.getPrimaryPrincipal();
				return sysUser.getId();
			}
			return key;
		}
	}

}
