
print("FOOBAR!");

conn = new Mongo();
db = conn.getDB("test");

db.foo.insert({"foo":"bar"});

cursor = db.foo.find();
while ( cursor.hasNext() ) {
   printjson( cursor.next() );
}



