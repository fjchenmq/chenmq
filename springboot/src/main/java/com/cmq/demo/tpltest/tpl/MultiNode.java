package com.cmq.demo.tpltest.tpl;

import org.apache.commons.lang.StringUtils;
import com.cmq.demo.tpltest.TplContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放其他节点的容器
 * .
 */
public class MultiNode implements TplNode {

    private List<TplNode> contents = new ArrayList<TplNode>();

    private List<TplNode> getContents() {
        return contents;
    }

    public void addNode(TplNode node) {
        contents.add(node);
    }

    public void addNodes(List<TplNode> nodes) {
        this.contents.addAll(nodes);
    }

    public void combine(MultiNode node) {
        if (contents.size() == 0) {
            this.addNodes(node.getContents());
        }
        else {
            List<TplNode> items = node.getContents();

            for (TplNode item
                    : items) {
                if (!contains(item)) {
                    this.addNode(item);
                }
            }
        }
    }

    private boolean contains(TplNode tplNode) {
        if (tplNode instanceof PlainNode) {
            PlainNode plainNode = (PlainNode) tplNode;

            if (contents.size() > 0) {
                for (TplNode item
                    : contents) {
                    if (item instanceof PlainNode) {
                        PlainNode pn = (PlainNode) item;

                        if (StringUtils.equals(pn.getKeyExpr(), plainNode.getKeyExpr())) {
                            return true;
                        }
                    }
                }
            }
        }
        else if (tplNode instanceof ForNode) {
            ForNode fn1 = (ForNode) tplNode;

            if (contents.size() > 0) {
                for (TplNode item
                        : contents) {
                    if (item instanceof ForNode) {
                        ForNode fn2 = (ForNode) item;

                        if (StringUtils.equals(fn2.getValueExpr(), fn1.getValueExpr())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void apply(TplContext context) {

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        if (contents.size() > 0) {
            sb.append(contents.get(0).toString() + "\n");
        }

        if (contents.size() > 1) {
            for (int i = 1; i < contents.size(); i++) {
                sb.append(", " + contents.get(i).toString() + "\n");
            }
        }

        sb.append("}");

        return sb.toString();
    }
}
