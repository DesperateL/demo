package com.example.demo.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rainday on 16/6/30.
 */
public class ViewObject implements Serializable {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
