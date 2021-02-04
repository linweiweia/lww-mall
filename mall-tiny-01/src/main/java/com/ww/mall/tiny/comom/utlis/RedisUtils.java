package com.ww.mall.tiny.comom.utlis;

import com.ww.mall.tiny.comom.exception.CatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-12-08 09:59
 * @describe:   Redis工具类
 */

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;


    // ===============================common=========================================

    /**
     * 指定key失效时间
     * @param key   键
     * @param time  时间（秒）
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否存在key
     * @param key
     * @return  truer存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key  key可以是一个也可以是多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }



    // ===============================String是key value形式=========================================

    /**
     * 获取普通缓存
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存设置
     * @param key   键
     * @param value 值
     * @return  true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 设置普通缓存并且设置时间
     * @param key   键
     * @param value 值
     * @param time  过期时间（秒） time要大于0，如果time如果小于0代表无期限
     * @return
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key   键（要为数字）
     * @param delta 递增因子（递增，所以需要大于0）
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * 递减
     * @param key   键（要为数字）
     * @param delta 递减因子（递减，所以要小于0）
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须下于0");
        }
        return redisTemplate.opsForValue().decrement(key, -delta);
    }


    // ===============================HashMap是key Map<Key,Value>形式=========================================

    /**
     * 获取hashmap中map的key为item的value值
     * @param key   键（不能为null）
     * @param item  Map的key（不能为null）
     * @return
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashmap中所map所有key value
     * @param key hashmap的key
     * @return 同一个hashMap中对应多个键值对
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往同一个hashmap，添加多个键值对
     * @param key   hashmap的key
     * @param map   键值对
     * @return
     */
    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 往同一个hashmap，添加多个键值对。 并且添加过期时间
     * @param key   hashmap的key
     * @param map   键值对
     * @return
     */
    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 试验一下？？？？？？
     * 向hashmap中添加map的key为item值为value
     * 有则替换，没有则创建
     * @param key   hashmap的键
     * @param item  map的key
     * @param value map的value
     * @param time  过期时间：注意如果已存在的hash表有时间，就会替换原有时间
     * @return
     */
    public boolean hSet(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hashmap中map的item对
     * @param key   hashmap的键
     * @param item  map中的键可多个
     */
    public void hDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }


    /**
     * 判断hashmap中map是否有键为item的键值对
     * @param key   hashmap中的键
     * @param item  map的键
     * @return
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hashmap递增
     * @param key   键
     * @param item  map中的键
     * @param by    递增步长
     * @return
     */
    public double hIncr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    /**
     * hashmap递减
     * @param key   键
     * @param item  map中的键
     * @param by    递减步长（下面做负号处理）
     * @return
     */
    public double hDecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }



    // ===============================Set是key value形式=========================================

    /**
     * 获取Set中所有成员
     * @param key hashmap的键
     * @return
     */
    public Set<Object> sGets(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断集合key中是否存在value
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 往set中存入数据，可一次性存入多个
     * @param key     键
     * @param values  值（可多个）
     * @return  存入元素个数
     */
    public long sSet(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 往set中添加数据，可一次性添加多个，且可以设置过期时间
     * @param key       键
     * @param time      过期时间(秒)
     * @param values    值（可多个），是多个参数要放最后，这样才能确定最后几个时第三个参数
     * @return  存入元素个数
     */
    public long sSet(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取集合的大小
     * @param key   键
     * @return
     */
    public long sGetSize(String key) {
        try {
            Long size = redisTemplate.opsForSet().size(key);
            return size;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除集合中元素，可以一次删除多个
     * @param key       键
     * @param values    删除值（可多个）
     * @return 删除元素个数
     */
    public long sRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    // ===============================list是key value形式=================================

    /**
     * 获取list中的元素
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 0 -1负数代表最后，及代表所有
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取列表list长度
     * @param key   键
     * @return
     */
    public long lGetSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引获取list中的值
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 往list中添加数据 往右边添加是队列了，往左边添加就是栈了
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lRightPush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 往list中添加数据，并且设置过期时间
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lRightPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 往list中添加list<object>
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lRightPushAll(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 往list中添加list<object>，并且可以设置时间
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lRightPushAll(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的数据
     * @param key   键
     * @param index 索引
     * @param value 修改后的值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key,index,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除count个数的value
     * @param key   键
     * @param count 删除数量
     * @param value 删除值
     * @return
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
