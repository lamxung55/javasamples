package com.vcb.database.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.vcb.model.TokenModel;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

//import common.Config;
public class NosqlDB {
    //public MongoClient mongoClient = null;// new MongoClient(uri);
    //public MongoDatabase mongoDB = null;

    public NosqlDB(String url, String dbname) {
        //MongoClientURI uri = new MongoClientURI(url);
        //mongoClient = new MongoClient(uri);
        //mongoDB=mongoClient.getDatabase(dbname);
    }

    /**
     *
     * @param tableName
     * @param doc
     */
    public static void insert(String tableName, Document doc) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        coll.insertOne(doc);
    }

    public static boolean updateSet(String tableName, Bson filter, Document doc) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        UpdateResult rs = coll.updateOne(filter, new Document("$set", doc));
        return rs.getMatchedCount() > 0 ? true : false;
    }

    public static boolean updateInc(String tableName, Bson filter, Document doc) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        UpdateResult rs = coll.updateOne(filter, new Document("$inc", doc));
        return rs.getMatchedCount() > 0 ? true : false;
    }

    public static long findAndInc(String tableName, String key) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        Document doc = new Document();
        doc.append("seq", 1);
        Document rs = coll.findOneAndUpdate(Filters.eq("_id", key), new Document("$inc", doc));
        if (rs == null) {
            doc.append("_id", key);
            doc.put("seq", 100001L);
            insert(tableName, doc);
            return 100000;
        }
        return rs.getLong("seq");
    }

    public static boolean update(String tableName, Bson filter, Document doc) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        UpdateResult rs = coll.replaceOne(filter, doc);
        return rs.getMatchedCount() > 0;
    }

    public static boolean upsert(String tableName, Bson filter, Document doc) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        UpdateResult rs = coll.replaceOne(filter, doc, new UpdateOptions().upsert(true));
        return rs.getMatchedCount() > 0;
    }

    public static List<Map> find(String tableName, Bson filter) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        FindIterable<Document> iter = coll.find(filter);
        List<Map> list = new ArrayList<>();
        for (Document doc : iter) {
            list.add(doc);
        }
        return list;
    }

    public static List<Document> findAll(String tableName) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        FindIterable<Document> iter = coll.find();
        List<Document> list = new ArrayList<>();
        for (Document doc : iter) {
            list.add(doc);
        }
        return list;
    }

    public static Map findOne(String tableName, Bson filter) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        FindIterable<Document> iter = coll.find(filter);
        return iter.first();
    }

    public static boolean delete(String tableName, Bson filter) {
        MongoDatabase client = Config.getMongoDB();
        MongoCollection<Document> coll = client.getCollection(tableName);
        DeleteResult rs = coll.deleteMany(filter);
        return rs.getDeletedCount() > 0;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            try {
                TokenModel token = new TokenModel("token_" + i, "pan_" + i);
                token.set_id(UUID.randomUUID().toString());
                Document doc = Document.parse(token.toJson());
                NosqlDB.insert("tokenization", doc);
                if(i%10000==0){
                    System.out.println("Inserted:" + i + " .Duration:" + (System.currentTimeMillis() - start));
                }
            } catch (Exception ex) {
                Logger.getLogger(NosqlDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Finish.Duration:" + (System.currentTimeMillis() - start) + " ms");

    }

}
