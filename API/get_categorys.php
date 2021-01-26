<?php
include 'db_master.php';
$token = $_POST["token"];
$db = init_db();
$res = array();
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	$user_data = getUserData($db, $user_id);
	if ($user_id != "_invalid_user_id_") {
		$shop_category = $user_data["category"];
		$can_buy = getCanBuyGoodsCategorys($db, $shop_category);

		$res["code"] = 1;
		$res["message"] = "_OK_";
		$res["res"] = $can_buy;
		echo json_encode($res);
	}else{
		$res["code"] = 2;
		$res["message"] = "_invalid_user_id_";
		$res["res"] = array();
		echo json_encode($res);
	}
}else{
	$res["code"] = 2;
	$res["message"] = "_invalid_token_";
	$res["res"] = array();
	echo json_encode($res);
}
$db->close();
?>