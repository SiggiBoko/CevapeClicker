const express = require('express');
const router = express.Router();
const dbconnection = require('../db/index');
const { v4: uuidv4 } = require('uuid');

router.post('/', function(req, res) {
    //CHECK IF USER EXISTS
    dbconnection.query('USE todos', function(err, out) {
        if (err) throw err;
    });

    dbconnection.query(`
    SELECT *
    FROM accounts
    WHERE username LIKE "${req.body.username}"
    `, function(err, out) {
        if (err) throw err;
        if (out.length) {
            res.send("Username already exists");
        } else {
            dbconnection.query(`
            INSERT INTO accounts
            VALUES
                ('${uuidv4()}', '${req.body.username}', '${req.body.password}');
            `, function(err, out) {
                if (err) throw err;
                res.send({
                    "success": true
                })
            });
        }
    });
});

module.exports = router;