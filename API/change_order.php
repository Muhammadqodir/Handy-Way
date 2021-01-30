<?php
include 'db_master.php';
$token = $_POST["token"];
$order_id = $_POST["order_id"];
$products = $_POST["products"];
$db = init_db();
$res = array();

$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	if ($products != "[]") {
		$q_res = changeOrder($db, $order_id, $products);
	}else{
		$q_res = removeOrder($db, $order_id);
	}
	$res["code"] = 1;
	$res["message"] = "_OK_";
	$res["res"] = true;
	echo json_encode($res);
}else{
	$res["code"] = 2;
	$res["message"] = "_invalid_token_";
	$res["res"] = false;
	echo json_encode($res);
}
$db->close();
?>