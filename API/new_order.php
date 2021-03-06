<?php
include 'db_master.php';
$token = $_POST["token"];
$products = $_POST["products"];
$db = init_db();
$res = array();
$date_time = date("d-m-Y H:i:s");
$date = date("d-m-Y");
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	$isFirstOrder = isFirstOrder($db, $date, $user_id);
	if ($isFirstOrder["isFirst"]) {
		$q_res = newOrder($db, $date_time, $products, $user_id);
		$res["code"] = 1;
		$res["message"] = "_OK_";
		$res["res"] = true;
		echo json_encode($res);
	}else{
		$q_res = addToExistOrder($db, $date_time, $isFirstOrder["id"], $products);
		$res["code"] = 1;
		$res["message"] = "_OK_";
		$res["res"] = true;
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