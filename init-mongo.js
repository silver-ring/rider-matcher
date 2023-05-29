db = db.getSiblingDB('matcher');
db.createCollection('rides');
db.rides.createIndex({ pickupLocation: "2dsphere" })
