package com.wan37.gameserver.game.quest.model;

import com.wan37.gameserver.util.excel.ReadExcelByEntity;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2018/12/25 18:25
 * @version 1.00
 * Description: mmorpg
 */
public class MissionExcelUtil extends ReadExcelByEntity<Quest> {
    /**
     * 构造工具类
     *
     * @param filepath 文件路径
     */
    public MissionExcelUtil(String filepath) {
        super(filepath);
    }
}
