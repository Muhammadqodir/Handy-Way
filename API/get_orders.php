<?php
include 'db_master.php';
$token = $_POST["token"];
$approved = $_POST["approved"];
$db = init_db();
$res = array();
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	$orders = array();
	$q_res = getOrders($db, $user_id, $approved);
	if ($q_res->num_rows > 0) {
		while ($row = $q_res->fetch_assoc()) {
			$row["isEditable"] = true;
			$after_hour = strtotime($row["date"]) + 3600;
			if ($after_hour<strtotime(date("d-m-Y H:i:s"))) {
				$row["isEditable"] = false;
			}
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