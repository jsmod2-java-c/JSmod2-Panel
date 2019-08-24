package cn.jsmod2.client.panel;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class JsonRequester {

    private Map<String,String> map = new HashMap<String, String>();

    public JsonRequester append(String key,String value){
        map.put(key,value);
        return this;
    }

    public String toJSON(){
        return JSON.toJSONString(map);
    }
}
