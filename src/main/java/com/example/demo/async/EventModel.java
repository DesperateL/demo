package com.example.demo.async;

import java.util.HashMap;
import java.util.Map;

//异步队列事件
public class EventModel { //事件现场数据
    private EventType type;
    private int actorId;//触发者
    private int entityType;//触发对象的类型
    private int entityId;//触发对象的id
    private int entityOwnerId;//触发对象拥有者的id

    private Map<String,String> exts = new HashMap<>();

    public String getExt(String key){
        return exts.get(key);
    }

    public EventModel setExt(String key,String value){
        if(exts==null){
            this.exts = new HashMap<>();
        }
        exts.put(key,value);
        return this;
    }

    public EventModel(EventType type){
        this.type = type;
    }
    public EventModel(){}

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
