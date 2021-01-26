<?php
include 'db_master.php';
$token = $_POST["token"];
$db = init_db();
$res = array();
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	if (isActiveUser($db, $user_id)) {
		$res["code"] = 1;
		$res["message"] = "_OK_";
		$res["res"] = true;
		echo json_encode($res);
	}else{
		$res["code"] = 1;
		$res["message"] = "_OK_";
		$res["res"] = false;
		echo json_encode($res);
	}
}else{
	$res["code"] = 2;
	$res["message"] = "_invalid_token_";
	$res["res"] = false;
	echo json_encode($res);
}
$db->close();
?>