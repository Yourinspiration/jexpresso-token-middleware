package de.yourinspiration.jexpresso.token

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.Mongo
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class MongoTokenResolverTest extends Specification {

    MongoTokenResolver mongoTokenResolver

    def mongo
    def db
    def dbCollection

    def databaseName = 'testDb'
    def collectionName = 'tokens'
    def fieldName = 'uuid'
    def dbObject = new BasicDBObject('uuid', '1234')

    def setup() {
        mongo = Mock(Mongo)
        db = Mock(DB)
        dbCollection = Mock(DBCollection)
        mongoTokenResolver = new MongoTokenResolver(mongo, databaseName, collectionName, fieldName)
    }

    def "should query the database"() {
        when:
        mongoTokenResolver.resolve('1234')

        then:
        1 * mongo.getDB(databaseName) >> db
        1 * db.getCollection(collectionName) >> dbCollection
        1 * dbCollection.findOne(new BasicDBObject(fieldName, '1234')) >> dbObject
    }

}
