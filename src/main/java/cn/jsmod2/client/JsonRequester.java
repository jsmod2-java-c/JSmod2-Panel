package cn.jsmod2.client;

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

    @SuppressWarnings("unchecked")
    public Response sendOut(){
        HashMap<String,String> map = JSON.parseObject(Client.getInstance().setMessage(toJSON()),HashMap.class);
        Response response = new Response();
        response.setMessage(map.get("message"));
        response.setValue(map.get("value"));
        response.setStatus(map.get("status"));
        return response;
    }
}

