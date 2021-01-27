<?php
include 'db_master.php';
$token = $_POST["token"];
$ids = json_decode($_POST["ids"], true);

$db = init_db();
$res = array();
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	$user_data = getUserData($db, $user_id);
	if ($user_id != "_invalid_user_id_") {
		$q_res = getGoodsByIds($db, $ids);
		$goods = array();

		if ($q_res->num_rows > 0) {
			while ($row = $q_res->fetch_assoc()) {
				$item = array();
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
				$goods[] = $item;
			}
		}

		$res["code"] = 1;
		$res["message"] = "_OK_";
		$res["res"] = $goods;
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