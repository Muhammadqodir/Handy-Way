<?php
include 'db_master.php';
$token = $_POST["token"];
$category_id = $_POST["category_id"];
$db = init_db();
$res = array();
$user_id = getUserId($db, $token);
if ($user_id != "_invalid_token_") {
	$brands = array();
	$q_res = getBrands($db, $category_id);
	if ($q_res->num_rows > 0) {
		while ($row = $q_res->fetch_assoc()) {
			$image = getBrandImage($db, $row["id"])->fetch_assoc()["photo"];
			$row["logo"] = $image;
			$brands[] = $row;
		}
	}
	$res["code"] = 1;
	$res["message"] = "_OK_";
	$res["res"] = $brands;
	echo json_encode($res);
}else{
	$res["code"] = 2;
	$res["message"] = "_invalid_token_";
	$res["res"] = array();
	echo json_encode($res);
}
$db->close();
?>