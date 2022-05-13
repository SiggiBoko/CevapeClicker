const cookieParser = require('cookie-parser');
const express = require('express');
const session = require('express-session');
const cors = require('cors');
const config = require('./config.json');
const app = express();

app.use(cors({ credentials: true }));

app.use(session({
    secret: config.SESSION_SECRET,
    saveUninitialized:false
}));


app.use(express.json());
app.use(cookieParser());

const routes = require('./routes/index');
app.use('/', routes);

app.listen(config.PORT, () => {
    console.log("Listening to http://localhost:" + config.PORT);
});