/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nypl.mss.dgi;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.net.UnknownHostException;

public class MongoConnection {
    
    private Mongo m;
    private DB db;
    private DBCollection coll;

    public MongoConnection() throws UnknownHostException {
        m = new Mongo( "localhost" , 27017 );
        db = m.getDB( "file_content" );
        coll = db.getCollection("content");
        
    }
    
    public void saveContent(String name, String content){
        BasicDBObject dbo = new BasicDBObject();
        dbo.put("filename", name);
        dbo.put("file_content", content);
        insertDoc(dbo);
        
    }   
    
    private void insertDoc(BasicDBObject dbo) {
        coll.insert(dbo);
    }
    
}