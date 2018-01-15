package com.andersen.simple_security_mongo.repository;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.List;


@Repository
public class UserRepository {

    @Value("${mongodb.database.name:security}")
    private String mongodbDatabaseName;

    @Value("${mongodb.collection.name:users}")
    private String mongodbCollectionName;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MongoClient mongo;
    private DB mongoDB;
    private DBCollection mongoCollection = null;

    @PostConstruct
    public void initConnection() {
        try {
            mongo = new MongoClient("localhost", 27017);
            mongoDB = mongo.getDB(mongodbDatabaseName);
            mongoCollection = mongoDB.getCollection(mongodbCollectionName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public List getAllUsers() {
        if (mongoCollection == null) {
            this.initConnection();
        }
        return mongoCollection.find().toArray();
    }

    public DBObject createUser(String username, String password, List<String> roles) {
        DBObject newUser = new BasicDBObject();
        newUser.put("username", username);
        newUser.put("password", passwordEncoder.encode(password));
        newUser.put("roles", roles);
        if (mongoCollection == null) {
            this.initConnection();
        }
        mongoCollection.save(newUser);
        return mongoCollection.findOne(newUser);
    }

    public DBObject findUserByUsername(String username) {
        if (mongoCollection == null) {
            this.initConnection();
        }
        return mongoCollection.findOne(new BasicDBObject("username", username));
    }

}