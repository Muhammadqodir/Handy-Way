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

		$q_res = getGood($db, $good_id);
		if ($q_res->num_rows > 0) {
			$item = array();
			$row = $q_res->fetch_assoc();
			$item["id"] = $row["id"];
			$item["name"] = $row["name"];
			if ($user_data["is_wholesaler"]) {
				$item["price"] = $row["price_for_one"];
			}else{
				$item["price"] = $row["price_for_wholesaler"];
			}
			$item["min_quantity"] = $row["min_quantity_to_buy"];
			$item["max_quantity"] = $row["quantity"];
			$item["pic"] = $row["photo"];
			$item["description"] = $row["description"];
			$item["payment_method"] = $row["method_of_payment"];
			$res["code"] = 1;
			$res["message"] = "_OK_";
			$res["res"] = $item;
			echo json_encode($res);
		}else{
			$res["code"] = 3;
			$res["message"] = "_invalid_product_id_";
			$res["res"] = array();
			echo json_encode($res);
		}
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