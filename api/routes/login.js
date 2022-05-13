const express = require('express');
const router = express.Router();
const dbconnection = require('../db/index');

router.post('/', function(req, res) {
    //SELECT DB
    dbconnection.query(`USE todos`, function(err, out) {
        if (err) throw err;
    });

    dbconnection.query(`
    SELECT *
    FROM accounts
    WHERE username LIKE "${req.body.username}"
    `, function(err, out) {
        if (err) throw err;

        if (out.length && out[0].password == req.body.password) {
            //CREATE SESSION
            req.session.user_id = out[0].pk_user_id;
            console.log(req.session);

            res.send({
                "success": true
            });   
        } else {
            res.send({
                "success": false
            });
        }
    });
});

module.exports = router;