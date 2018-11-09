<?php
date_default_timezone_set('America/Mexico_City');
require 'app/lib/Slim/Slim.php';
require 'app/vendor/idiorm.php'; 
require 'app/lib/Validator.php';


\Slim\Slim::registerAutoloader();
$config = require 'config.php';

$app = new \Slim\Slim($config["slim"]);

ORM::configure('mysql:host=localhost;dbname=detexis;charset=utf8');
ORM::configure('username', 'root');
ORM::configure('password', 'root');

ORM::configure('return_result_sets', true);
ORM::configure('id_column_overrides', array('id'=> 'msn'));

// routes
require 'app/routes/getMsn.php';
require 'app/routes/updateMsn.php';
require 'app/routes/insertMsn.php';


$app->contentType('application/json');

// CORS headers
$app->response->headers->set('Access-Control-Allow-Origin', '*');
$app->response->headers->set('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS');
$app->response->headers->set('Access-Control-Allow-Headers', 'X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version');

$app->run();