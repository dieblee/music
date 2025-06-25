package com.xm.controller;

import com.xm.entity.Song;
import com.xm.service.SongService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SongService songService;

    private static final String ALL_SONGS_CLICK_KEY = "song:clicks";
    private static final String SONG_CLICK_KEY_PREFIX = "song:clicks:";

    @PostMapping("/incrementClick")
    public void incrementClick(@RequestBody Map<String, Object> params) {
        Integer songId = convertToInteger(params.get("songId"));
        if (songId == null) {
            throw new IllegalArgumentException("Invalid song ID");
        }

        // 1. 更新有序集合（用于排行榜）
        redisTemplate.opsForZSet().incrementScore(ALL_SONGS_CLICK_KEY, songId, 1);

        // 2. 更新独立歌曲计数（方便单个查询）
    }

    private Integer convertToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @GetMapping("/rankSong")
    public List<Song> rankSong(@RequestParam(defaultValue = "7") int limit) {
        // 1. 从Redis获取排行榜歌曲ID（按点击量降序）
        Set<Object> songIds = redisTemplate.opsForZSet()
                .reverseRange(ALL_SONGS_CLICK_KEY, 0, limit - 1);

        if (songIds == null || songIds.isEmpty()) {
            return null;
        }

        // 2. 逐个获取歌曲信息（使用selectById）
        List<Song> rankedSongs = new ArrayList<>();

        for (Object songId : songIds) {
            // 解析member字符串获取歌曲ID
            if (songId == null) continue;

            // 逐个查询歌曲详情
            Song song = songService.selectById((Integer) songId);
            if (song != null) {
                rankedSongs.add(song);
            }
        }
        System.out.println("排行榜歌曲: " + rankedSongs);
        return rankedSongs;
    }

}
