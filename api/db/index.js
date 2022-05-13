const db = require('mysql');
const connection = db.createConnection({
    host     : 'localhost',
    user     : 'root',
    port: '3306'
});

connection.connect();

module.exports = connection;