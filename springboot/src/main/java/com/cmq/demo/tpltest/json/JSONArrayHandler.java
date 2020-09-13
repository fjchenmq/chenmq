package com.cmq.demo.tpltest.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.StringUtils;
import com.cmq.demo.tpltest.DefaultContext;
import com.cmq.demo.tpltest.HandlerRegistry;
import com.cmq.demo.tpltest.NodeHandler;
import com.cmq.demo.tpltest.TplContext;
import com.cmq.demo.tpltest.tpl.ForNode;
import com.cmq.demo.tpltest.tpl.MultiNode;
import com.cmq.demo.tpltest.tpl.TplNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JSONArrayHandler implements JSONNodeHandler<JSONArray> {

    @Override
    public TplNode handleNode(JSONArray node, TplContext context) {
        // 对于list节点，需要查找所有的子节点，以获取所有的属性
        // 生成变量，
        // 根据策略进行生成，如果以s结尾，则去掉s；如果以list结尾，则去掉list，如果以array结尾，则去掉array，为了防止变量重复，增加一定的区分机制
        String contextExpr = context.getContext();
        String contextPath = context.getContextPath();
        String contextVariable = context.createContextVariable();

        if (StringUtils.isEmpty(contextExpr)
            || StringUtils.isEmpty(contextPath)) {
            throw new IllegalArgumentException("上下文信息不能为空:" + node.toString());
        }

        if (StringUtils.isEmpty(contextVariable)) {
            throw new IllegalArgumentException("临时变量生成失败:" + node.toString());
        }

        MultiNode content = new MultiNode();
        Set<String> keySet = new HashSet<String>();
        if (node.size() > 0) {
            Iterator<Object> iter = node.iterator();

            while (iter.hasNext()) {
                Object item = iter.next();

                if (item != null) {
                    NodeHandler nodeHandler = getHandlerRegistry().getHandler(item.getClass());
                    if (nodeHandler != null) {
                        // 此时将变量名作为新的上下文
                        TplContext newContext = new DefaultContext(contextVariable);
                        newContext.addParent(context);

                        // FIXME 这次可能类型会转换失败
                        TplNode result = nodeHandler.handleNode((JSON)item, newContext);

                        if (result != null) {
                            if (result instanceof MultiNode) {
                                content.combine((MultiNode) result);
                            }
                            else {
                                content.addNode(result);
                            }

                        }
                    }
                    // 如果为空，说明是简单的值，正常其实不用支持这种，但是蛮支持下吧
                    // TODO 先不处理吧，都测试过在处理
                    else {
                    }
                }
            }
        }

        ForNode forNode = new ForNode(contextVariable, contextPath, content);

        return forNode;
    }

    private HandlerRegistry getHandlerRegistry() {
        return JSONHandlerRegistry.singleton();
    }
}
