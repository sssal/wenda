package com.niuke.forum.util;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class JedisAdapter {
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));


    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();

        jedis.append("hello", "jedis");
        print(1, jedis.get("hello"));

        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));
        jedis.setex("hello2", 15, "jedis");

        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));

        print(3, jedis.keys("*"));

        //list
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(4, jedis.lrange(listName, 0, 12));
        print(5, jedis.lrange(listName, 0, 3));
        print(6, jedis.llen(listName));
        print(7, jedis.lpop(listName));
        print(8, jedis.llen(listName));
        print(9, jedis.lindex(listName, 3));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "af"));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bef"));
        print(11, jedis.lrange(listName, 0, 12));

        //hash
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "12233242");
        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "pnone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));
        print(16, jedis.hexists(userKey, "name"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "zju");
        jedis.hsetnx(userKey, "name", "ton");
        print(19, jedis.hgetAll(userKey));

        //set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i));
        }
        print(20, jedis.smembers(likeKey1));
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sunion(likeKey1, likeKey2));
        print(23, jedis.sdiff(likeKey1, likeKey2));
        print(24, jedis.sinter(likeKey1, likeKey2));
        print(25, jedis.sismember(likeKey1, "12"));
        print(26, jedis.sismember(likeKey2, "16"));
        jedis.srem(likeKey1, "5");
        print(27, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25");
        print(28, jedis.smembers(likeKey1));
        print(29, jedis.scard(likeKey1));

        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 59, "ben");
        jedis.zadd(rankKey, 64, "mim");
        jedis.zadd(rankKey, 66, "tim");
        jedis.zadd(rankKey, 86, "aim");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 61, 100));
        print(32, jedis.zscore(rankKey, "jim"));
        jedis.zincrby(rankKey, 2, "jim");
        print(33, jedis.zscore(rankKey, "jim"));
        jedis.zincrby(rankKey, 2, "non");
        print(34, jedis.zscore(rankKey, "non"));
        print(35, jedis.zrange(rankKey, 0, 100));
        print(36, jedis.zrange(rankKey, 1, 3));
        print(36, jedis.zrevrange(rankKey, 1, 3));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, 60, 100)) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(38, jedis.zrank(rankKey, "tim"));
        print(39, jedis.zrevrank(rankKey, "tim"));


    }
}
