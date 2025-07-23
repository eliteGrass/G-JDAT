package com.itsoku.lesson077;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/22 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class MenuController {

    /**
     * 获取树形菜单
     *
     * @return
     */
    @GetMapping("/menuTree")
    public List<Tree<String>> menuTree() {
        //菜单列表，放在一个menuPOList中，我们用的时候这个数据可以从 db 中去获取
        List<MenuPO> menuPOList = new ArrayList<>();
        menuPOList.add(MenuPO.builder().id("1").name("菜单1").pid(null).theSort(1).url("url1").build());
        menuPOList.add(MenuPO.builder().id("101").name("菜单101").pid("1").theSort(1).url("url101").build());
        menuPOList.add(MenuPO.builder().id("102").name("菜单102").pid("1").theSort(1).url("url102").build());

        menuPOList.add(MenuPO.builder().id("2").name("菜单2").pid(null).theSort(1).url("url2").build());
        menuPOList.add(MenuPO.builder().id("201").name("菜单201").pid("2").theSort(1).url("url201").build());
        menuPOList.add(MenuPO.builder().id("202").name("菜单202").pid("2").theSort(2).url("url202").build());

        /**
         * 下面通过 hutool 提供的工具类 TreeUtil.build，将上面的菜单列表转换为树
         * 3个参数
         * 参数1：数据源，即要被转换为树的原始数据，本案例中就是上面的 menuPOList
         * 参数2：根节点id
         * 参数3：转换器，数据源的数据可以是各种类型的，但是hutool不能识别，所以这里需要一个转换器，
         *   将第一个参数中的元素转换为 hutool可以识别的类型：Tree类（hutool对树节点的一个抽象），
         *   Tree表示树的一个节点，里面包含了这个节点所有的信息
         */
        List<Tree<String>> treeList = TreeUtil.build(menuPOList, null, (MenuPO menuPo, Tree<String> treeNode) -> {
            //树节点id
            treeNode.setId(menuPo.getId());
            //树节点名称
            treeNode.setName(menuPo.getName());
            //父节点id
            treeNode.setParentId(menuPo.getPid());
            //权重，相当于同级节点的顺序，同级会按升序排序
            treeNode.setWeight(menuPo.getTheSort());
            //节点扩展数据，可以通过put方法，放入任意个扩展数据
            treeNode.put("url", menuPo.getUrl());
        });

        return treeList;
    }
}