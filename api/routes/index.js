const express = require('express');
const router = express.Router();

const todos = require('./todos');
router.use('/todos', todos);

const login = require('./login');
router.use('/login', login);

const register = require('./register');
router.use('/register', register);

const logout = require('./logout');
router.use('/logout', logout);

module.exports = router;