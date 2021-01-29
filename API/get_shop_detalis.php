<?php
include 'db_master.php';
$token = $_POST["token"];
$good_id = $_POST["good_id"];
$db = init_db();
$res = array();
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	$user_data = getUserData($db, $user_id);
	if ($user_data != "_invalid_user_id_") {
		$user_data["district_str"] = getDistrict($db, $user_data["district"]);
		$user_data["category_str"] = getShopCategory($db, $user_data["category"]);
		$res["code"] = 1;
		$res["message"] = "_OK_";
		$res["res"] = $user_data;
		echo json_encode($res);
	}else{
		$res["code"] = 2;
		$res["message"] = "_invalid_user_id_";
		$res["res"] = array(
		    "res" => "empty"
		);
		echo json_encode($res);
	}
}else{
	$res["code"] = 2;
	$res["message"] = "_invalid_token_";
	$res["res"] = array(
		    "res" => "empty"
		);
	echo json_encode($res);
}
$db->close();
?>