<?php
date_default_timezone_set('Asia/Tashkent');

function init_db()
{
	include 'db_config.php';
	$conn = new mysqli($host, $username, $password, $db_name);
	if ($conn->connect_error) {
        echo "ErRoR on Connection to DataBase";
        die("Connection failed: " . $conn->connect_error);
	}
	$conn->set_charset("utf8");
	return $conn;
}

function getTable($db, $table)
{
	$sql = "SELECT * FROM `$table`";
	$q_res = $db->query($sql);
	return $q_res;
}

function getCanBuyGoodsCategorys($db, $shop_category)
{
	$sql = "SELECT * FROM `main_canbuy` WHERE `shopCategory_id` = '$shop_category'";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		$ids = json_decode(str_replace("'", "", $row["goodsCategory"]), true);
		$sql = "SELECT * FROM `main_goodscategory` WHERE";
		$first = true;
		foreach ($ids as $id) {
			if ($first) {
				$sql .= " `id` = $id";
				$first = false;
			}else{
				$sql .= " OR `id` = $id";
			}
		}
		$res = array();
		$q_res = $db->query($sql);
		if ($q_res->num_rows > 0) {
			while ($row = $q_res->fetch_assoc()) {
				$res[] = $row;
			}
		}
		return $res;
	}else{
		return array();
	}
}

function getUserData($db, $user_id)
{
	$sql = "SELECT * FROM `main_shop` WHERE `id` = '$user_id'";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		return $row;
	}else{
		return "_invalid_user_id_";
	}
}

function getDistrict($db, $id)
{
	$sql = "SELECT * FROM `main_district` WHERE `id` = $id";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		return $row["name"];
	}else{
		return "_invalid_district_id_";
	}
}

function getShopCategory($db, $id)
{
	$sql = "SELECT * FROM `main_shopcategory` WHERE `id` = $id";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		return $row["name"];
	}else{
		return "_invalid_shopcategory_id_";
	}
}

function getOrderItemIds($db, $order_id)
{
	$sql = "SELECT * FROM `main_orders` WHERE `id` = $order_id";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		$items = json_decode($row["products"], true);
		$ids = array();
		for ($i=0; $i < count($items); $i++) {
			$ids[] = $items[$i]["id"];
		}
		return $ids;
	}else{
		return "_invalid_order_id";
	}
}

function getGoods($db, $category_id, $district)
{
	$sql = "SELECT * FROM `main_good` WHERE `category_id` = $category_id AND `distribution` LIKE '%\'$district\'%'";
	$q_res = $db->query($sql);
	return $q_res;
}

function getGoodsByBrand($db, $brand_id, $district)
{
	$sql = "SELECT * FROM `main_good` WHERE `brand_id` = $brand_id AND `distribution` LIKE '%\'$district\'%'";
	$q_res = $db->query($sql);
	return $q_res;
}

function searchGoods($db, $category_id, $district, $q){
	$sql = "SELECT * FROM `main_good` WHERE `name` LIKE '%$q%' AND `distribution` LIKE '%\'$district\'%'";
	if ($category_id >= 0) {
		$sql = "SELECT * FROM `main_good` WHERE `name` LIKE '%$q%' AND `category_id` = $category_id AND `distribution` LIKE '%\'$district\'%'";
	}
	$q_res = $db->query($sql);
	return $q_res;
}

function getGood($db, $good_id)
{
	$sql = "SELECT * FROM `main_good` WHERE `id` = $good_id";
	$q_res = $db->query($sql);
	return $q_res;
}

function getOrders($db, $user_id, $approved){
	$sql = "SELECT * FROM `main_orders` WHERE `user_id_id` = $user_id ORDER BY `main_orders`.`date` DESC";
	if ($approved == "true") {
	$sql = "SELECT * FROM `main_orders` WHERE `user_id_id` = $user_id AND `status` = 1 ORDER BY `main_orders`.`date` DESC";
	}
	$q_res = $db->query($sql);
	return $q_res;
}

function getBrands($db, $category_id){
	$sql = "SELECT * FROM `main_brand` WHERE `categories` LIKE '%\'$category_id\'%'";
	$q_res = $db->query($sql);
	return $q_res;
}

function getGoodsByIds($db, $ids)
{
	$sql = "SELECT * FROM `main_good` WHERE";
	$first = true;
	foreach ($ids as $id) {
		if ($first) {
			$sql .= " `id` = $id";
			$first = false;
		}else{
			$sql .= " OR `id` = $id";
		}
	}
	$q_res = $db->query($sql);
	return $q_res;
}

function getUserId($db, $token)
{
	$sql = "SELECT * FROM `users_session` WHERE `token` = '$token'";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		return $row["user_id"];
	}else{
		return "_invalid_token_";
	}
}

function isActiveUser($db, $id)
{
	$sql = "SELECT * FROM `main_shop` WHERE `id` = '$id' AND `is_active` = '1'";
	$q_res = $db->query($sql);
	return $q_res->num_rows>0;
}


function chekUserAuth($db, $tel, $password)
{
	$sql = "SELECT * FROM `main_shop` WHERE `phone_number` = '$tel' AND `password` = '$password'";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		return $row["id"];
	}else{
		return -1;
	}
}

function newUserAuth($db, $token, $ip, $location, $date, $device, $user_id)
{
	$sql = "INSERT INTO `users_session` (`id`, `user_id`, `token`, `ip`, `date`, `device`) VALUES (NULL, '$user_id', '$token', '$ip', '$date', '$device');";
	$q_res = $db->query($sql);
}

function getCategoryImages($db, $category_id, $limit)
{
	$sql = "SELECT `photo` FROM `main_good` WHERE `category_id` = $category_id AND `photo` <> '' ORDER BY RAND() LIMIT $limit;";
	$q_res = $q_res = $db->query($sql);
	return $q_res;
}

function newOrder($db, $date, $products, $user_id)
{
	$sql = "INSERT INTO `main_orders` (`id`, `date`, `products`, `status`, `user_id_id`) VALUES (NULL, '$date', '$products', '0', '$user_id');";
	$q_res = $db->query($sql);
}

function isFirstOrder($db, $date, $user_id)
{
	$res = array();
	$sql = "SELECT * FROM `main_orders` WHERE `date` LIKE '%$date%' AND `user_id_id` = $user_id";
	$q_res = $db->query($sql);
	if ($q_res->num_rows > 0) {
		$row = $q_res->fetch_assoc();
		$res["isFirst"] = false;
		$res["id"] = $row["id"];
	}else{
		$res["isFirst"] = true;
	}
	return $res;
}

function addToExistOrder($db, $date, $order_id, $products)
{
	$sql = "SELECT `products` FROM `main_orders` WHERE `id` = $order_id";
	$q_res = $db->query($sql);
	$e_products = json_decode($q_res->fetch_assoc()["products"], true);
	$final_pr = json_encode(array_merge($e_products, json_decode($products, true)));
	$sql = "UPDATE `main_orders` SET `products` = '$final_pr', `date` = '$date' WHERE `id` = $order_id";
	$q_res = $db->query($sql);
}

function returnOrder($db, $date, $products, $user_id)
{
	$sql = "INSERT INTO `main_returns` (`id`, `date`, `products`, `user_id_id`) VALUES (NULL, '$date', '$products', '$user_id');";
	$q_res = $db->query($sql);
}

function changeOrder($db, $order_id, $products)
{
	$sql = "UPDATE `main_orders` SET `products` = '$products' WHERE `id` = $order_id";
	$q_res = $db->query($sql);
}

function removeOrder($db, $order_id)
{
	$sql = "DELETE FROM `main_orders` WHERE `id` = $order_id";
	$q_res = $db->query($sql);
}

function strtosql($str){
	return str_replace("'", "\'", $str);
}


?>