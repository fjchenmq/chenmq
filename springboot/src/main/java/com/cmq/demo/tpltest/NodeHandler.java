package com.cmq.demo.tpltest;

import com.cmq.demo.tpltest.tpl.TplNode;

/**
 *
 * 用于处理节点
 * .
 */
public interface NodeHandler<N> {

    TplNode handleNode(N node, TplContext context);
}
