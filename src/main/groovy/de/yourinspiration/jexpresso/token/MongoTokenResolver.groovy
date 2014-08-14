package de.yourinspiration.jexpresso.token

import com.mongodb.*

/**
 * Created by Marcel on 14.08.2014.
 */
class MongoTokenResolver implements TokenResolver {

    private final Mongo mongo
    private final String databaseName
    private final String collectionName
    private final String fieldName

    MongoTokenResolver(
            final Mongo mongo, final String databaseName, final String collectionName, final String fieldName) {
        this.mongo = mongo
        this.databaseName = databaseName
        this.collectionName = collectionName
        this.fieldName = fieldName
    }

    @Override
    boolean resolve(String token) {
        DB db = mongo.getDB(databaseName)
        DBCollection dbCollection = db.getCollection(collectionName)
        DBObject result = dbCollection.findOne(new BasicDBObject(fieldName, token))
        return result != null
    }
}
