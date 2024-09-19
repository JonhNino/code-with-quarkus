package com.transfer.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

@MongoEntity(collection = "commission_codes")
public class CommissionCodes extends PanacheMongoEntity {

    @BsonProperty("code")
    public String code;

    @BsonProperty("name")
    public String name;

    @BsonProperty("currency")
    public String currency;

    @BsonProperty("creationDate")
    public Instant creationDate;

    @BsonProperty("updateDate")
    public Instant updateDate;

}