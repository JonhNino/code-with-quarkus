package com.transfer.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.Instant;

@MongoEntity(collection="parameters_transfer")
public class Parametro extends PanacheMongoEntity {
    @BsonProperty("operationType")
    public String operationType;

    @BsonProperty("transactionCode")
    public String transactionCode;

    @BsonProperty("accountingAccount")
    public String accountingAccount;

    @BsonProperty("descriptions")
    public Descriptions descriptions;

    @BsonProperty("creationDate")
    public Instant creationDate;

    @BsonProperty("updateDate")
    public Instant updateDate;
}
