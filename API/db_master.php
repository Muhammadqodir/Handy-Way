<?php

function init_db()
{
	include 'db_config.php';
	$conn = new mysqli($host, $username, $password, $db_name);
	if ($conn->connect_error) {
        echo "ErRoR on Connection to DataBase";
        die("Connection failed: " . $conn->connect_error);
	}
	$conn->set_charset("utf8");
	return $conn;
}


?>