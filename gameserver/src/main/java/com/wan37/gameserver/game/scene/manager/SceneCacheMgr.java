package com.wan37.gameserver.game.scene.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wan37.gameserver.game.sceneObject.service.GameObjectService;
import com.wan37.gameserver.game.scene.model.GameScene;

import com.wan37.gameserver.game.scene.model.SceneExcelUtil;
import com.wan37.gameserver.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2018/9/27 18:07
 * @version 1.00
 * Description: mmorpg
 */

@Slf4j
@Component
public class SceneCacheMgr  {


    /** 缓存不过期 **/
    private static Cache<Integer, GameScene> sceneCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "场景被移除, 原因是" + notification.getCause())
            ).build();



    @Resource
    private GameObjectService gameObjectService;


    @PostConstruct
    private void init() {
        String path = FileUtil.getStringPath("gameData/gameScene.xlsx");
        SceneExcelUtil sceneExcelUtil = new SceneExcelUtil(path);


        Map<Integer,GameScene> gameSceneMap = sceneExcelUtil.getMap();
        for (GameScene  gameScene: gameSceneMap.values()) {

            // 初始化怪物和NPC
            gameScene = gameObjectService.initSceneObject(gameScene);

            sceneCache.put(gameScene.getId(), gameScene);
        }
        log.info("场景资源加载进缓存完毕");
    }



    public static GameScene getScene(Integer key) {
        return sceneCache.getIfPresent(key);
    }



    public static List<GameScene> list() {
        List<GameScene> gameScenes = new ArrayList<>();
        Map<Integer,GameScene>  sceneCacheMap = sceneCache.asMap();

        for (Map.Entry <Integer,GameScene> gameSceneEntry : sceneCacheMap.entrySet()) {
            gameScenes.add(gameSceneEntry.getValue());
        }
        return gameScenes;
    }



}
