package com.cmq.demo.json2tree;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by chen.ming.qian on 2020/9/12.
 */
@Data
public class NodeInfo {
    private String value;
    private String uuid;
    private String puuid;
    private String nodePath;

    /**
     * @param value
     * @param uuid
     * @param puuid
     * @param nodePath
     * @return
     */
    public static NodeInfo newInstance() {
        NodeInfo instance = new NodeInfo();
        return instance;
    }
}
