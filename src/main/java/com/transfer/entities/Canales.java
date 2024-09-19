package com.transfer.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

@MongoEntity(collection="canales")
public class Canales extends PanacheMongoEntity {

    @BsonProperty("channel")
    public String channel;

    @BsonProperty("channelId")
    public int channelId;

    @BsonProperty("branchId")
    public int branchId;

    @BsonProperty("tellerId")
    public int tellerId;

    @BsonProperty("overrideCode")
    public String overrideCode;

    @BsonProperty("creationDate")
    public Instant creationDate;

    @BsonProperty("updateDate")
    public Instant updateDate;

}