<?php
include 'db_master.php';
$token = $_POST["token"];
$db = init_db();
$res = array();
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	$orders = array();
	$q_res = getOrders($db, $user_id);
	if ($q_res->num_rows > 0) {
		while ($row = $q_res->fetch_assoc()) {
			$orders[] = $row;
		}
	}
	$res["code"] = 1;
	$res["message"] = "_OK_";
	$res["res"] = $orders;
	echo json_encode($res);
}else{
	$res["code"] = 2;
	$res["message"] = "_invalid_token_";
	$res["res"] = array();
	echo json_encode($res);
}
$db->close();
?>