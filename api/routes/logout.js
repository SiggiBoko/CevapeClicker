const express = require('express');
const router = express.Router();

router.post('/', function(req, res) {
    //DESTORY SESSION
    req.session.destroy();
    res.end();
});

module.exports = router;