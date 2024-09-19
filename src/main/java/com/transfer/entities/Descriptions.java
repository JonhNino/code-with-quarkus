
package com.transfer.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Descriptions {
    @BsonProperty("description1")
    public String description1;

    @BsonProperty("description2")
    public String description2;

    @BsonProperty("description3")
    public String description3;

}