package com.cmq.demo.json2tree;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by chen.ming.qian on 2020/9/12.
 */
public class AddUUIDJsonUtil {
    public static final String UUID  = "uuid";
    public static final String PUUID = "puuid";
    
    /**
     * 重构JsonObject 为每个非叶子数据添加关系UUID
     *
     * @param sourceJsonObject
     * @return
     */
    public static JSONObject addUUID(JSONObject jsonObject) {
        Queue<Integer> idQueue = buildIdQueue();
        parseJson(jsonObject, null, idQueue);
        return jsonObject;
    }

    /**
     * 重构JsonObject 为每个非叶子数据添加关系ID
     *
     * @param sourceJsonObject
     * @return
     */
    public static void parseJson(JSONObject sourceJsonObject, Integer parentId,
        Queue<Integer> idQueue) {

        Iterator<String> it = sourceJsonObject.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object object = sourceJsonObject.get(key);
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                jsonObject.put(UUID, idQueue.poll());
                if (parentId != null) {
                    jsonObject.put(PUUID, parentId);
                }
                parseJson(jsonObject, jsonObject.getInteger(UUID), idQueue);
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = ((JSONArray) object);
                processArray(jsonArray, parentId, idQueue);

            }
        }

    }

    /**
     * 重构JsonObject 为每个非叶子数据添加关系ID
     *
     * @param sourceJsonObject
     * @return
     */
    public static void processArray(JSONArray jsonArray, Integer parentId, Queue<Integer> idQueue) {
        jsonArray.stream().forEach(item -> {
            if (item instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) item;
                jsonObject.put(UUID, idQueue.poll());
                if (parentId != null) {
                    jsonObject.put(PUUID, parentId);
                }
                parseJson(jsonObject, jsonObject.getInteger(UUID), idQueue);
            } else if (item instanceof JSONArray) {
                processArray((JSONArray) item, parentId, idQueue);
            }

        });

    }
    public static Queue<Integer> buildIdQueue() {
        Queue<Integer> idQueue = new LinkedList<Integer>();
        for (int i = 1; i < 1000; i++) {
            idQueue.add(i);
        }
        return idQueue;
    }

    public static void main(String[] args) throws Exception {
        String file = "/template/order.json";
        String json = "";
        ClassPathResource resource = new ClassPathResource(file);
        if (resource.exists()) {
            json = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()
                .collect(Collectors.joining(System.lineSeparator()));
        }
        JSONObject result = addUUID(JSONObject.parseObject(json));
        System.out.println(result);
    }
}
