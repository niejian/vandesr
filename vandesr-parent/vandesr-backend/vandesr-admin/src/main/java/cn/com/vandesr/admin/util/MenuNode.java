package cn.com.vandesr.admin.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: nj
 * @date: 2019/1/28:下午5:07
 */
@Data
public class MenuNode implements Serializable {

    /**链表-前一个节点*/
    private MenuNode pre;
    /**链表-当前节点数据*/
    private String data;
    /**链表-子节点列表*/
    private List<MenuNode> nextList;


    public MenuNode(MenuNode pre, String data, List<MenuNode> nextList) {
        this.pre = pre;
        this.data = data;
        this.nextList = nextList;
    }

    public MenuNode() {
    }


    /**
     * 向指定的节点追加一个元素
     * 因为此链表非常规链表，子节点可能有多个，然，节点中的data必然唯一
     *
     * @param node
     */
    public void setNext(MenuNode node, MenuNode nextNode) {
        node.nextList.add(nextNode);
    }

    /**
     * 向指定位置插入一个节点
     * @param pre
     * @param nextNode
     */
    public void insert(MenuNode pre, MenuNode nextNode) {
        pre.nextList.add(nextNode);
    }

    /**
     * 通过data（唯一）获取指定节点信息
     * @param data
     * @return
     */
    public MenuNode getMenuNode(MenuNode menuNode, String data) {
        if (data.equals(menuNode.data)) {
            return menuNode;
        }

        return menuNode;
    }



}
