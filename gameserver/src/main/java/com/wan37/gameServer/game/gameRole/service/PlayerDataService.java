package com.wan37.gameServer.game.gameRole.service;


import com.wan37.gameServer.game.bag.service.BagsService;
import com.wan37.gameServer.game.bag.service.EquipmentBarService;
import com.wan37.gameServer.game.gameRole.model.Buffer;
import com.wan37.gameServer.game.roleProperty.model.RoleProperty;
import com.wan37.gameServer.game.roleProperty.service.RolePropertyService;
import com.wan37.gameServer.game.gameRole.model.Player;

import com.wan37.gameServer.game.gameRole.manager.PlayerCacheMgr;


import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2018/9/30 10:07
 * @version 1.00
 * Description: mmorpg
 */
@Service
@Slf4j
public class PlayerDataService {

    @Resource
    private PlayerCacheMgr playerCacheMgr;

    @Resource
    private BufferService bufferService;

    @Resource
    private RolePropertyService rolePropertyService;

    @Resource
    private BagsService bagsService;

    @Resource
    private EquipmentBarService equipmentBarService;


    public Player getPlayerByCtx(ChannelHandlerContext ctx) {
        return playerCacheMgr.get(ctx.channel().id().asLongText());
    }



    public Player getPlayer(String channelId) {
        return playerCacheMgr.get(channelId);
    }




    public void computeAttack(Player player) {

        Map<Integer, RoleProperty> rolePropertyMap = player.getRolePropertyMap();
        // 基础攻击力
        int basicAtack = Optional.ofNullable(rolePropertyMap.get(4).getValue()).orElse(30);

        // 力量
        int power  = Optional.ofNullable(rolePropertyMap.get(5).getValue()).orElse(100);


        player.setAttack(basicAtack + power);
    }







    /**
     *  初始化角色
     * @param player 角色
     */
    public void initPlayer(Player player) {


        // 加载背包
        bagsService.loadBag(player);

        // 加载武器栏
        equipmentBarService.load(player);

        // 加载属性
        rolePropertyService.loadRoleProperty(player);

        // 计算初始战力
        computeAttack(player);


        // 施放一个回蓝的buffer
        Buffer buffer = bufferService.getTBuffer(105);
        bufferService.startBuffer(player,buffer);


    }



}
