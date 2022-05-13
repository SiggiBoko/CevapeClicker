const express = require('express');
const router = express.Router();
const dbconnection = require('../db/index');

router.get('/', function(req, res) {
    dbconnection.query('USE todos', function(err, out) {
        if (err) throw err;
    });

    dbconnection.query(`
    SELECT *
    FROM tododata
    WHERE fk_pk_user_id LIKE "${req.session.user_id}"
    `, function(err, out) {
        if (err) throw err;
        if (req.session.user_id) {
            res.send(out);
        } else {
            res.send("Please log in!")
        }
    });
});

//ROUTER DELETE TODOS

module.exports = router;