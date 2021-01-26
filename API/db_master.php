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

function getTable($db, $table)
{
	$sql = "SELECT * FROM `$table`";
	$q_res = $db->query($sql);
	return $q_res;
}

function getUserId($db, $token)
{
	$sql = "SELECT * FROM `users_session` WHERE `token` = $token";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		require $row["user_id"];
	}else{
		return "_invalid_token_";
	}
}

function isActiveUser($db, $id)
{
	$sql = "SELECT * FROM `users_session` WHERE `id` = $id AND `is_active` = 1";
	$q_res = $db->query($sql);
	return $q_res->num_rows>0;
}

function chekUserAuth($db, $tel, $password)
{
	$sql = "SELECT * FROM `main_shop` WHERE `phone_number` = '$tel' AND `password` = '$password'";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		return $row["id"];
	}else{
		return -1;
	}
}

function newUserAuth($db, $token, $ip, $location, $date, $device, $user_id)
{
	$sql = "INSERT INTO `users_session` (`id`, `user_id`, `token`, `ip`, `date`, `device`) VALUES (NULL, '$user_id', '$token', '$ip', '$date', '$device');";
	$q_res = $db->query($sql);
}


function strtosql($str){
	return str_replace("'", "\'", $str);
}


?>