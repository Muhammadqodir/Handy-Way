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

function getGoods($db, $category_id, $district)
{
	$sql = "SELECT * FROM `main_good` WHERE `category_id` = $category_id AND `distribution` LIKE '%\'$district\'%'";
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
	$sql = "SELECT `photo` FROM `main_good` WHERE `category_id` = $category_id ORDER BY RAND() LIMIT $limit;";
	$q_res = $q_res = $db->query($sql);
	return $q_res;
}

function newOrder($db, $date, $products, $user_id)
{
	$sql = "INSERT INTO `main_orders` (`id`, `date`, `products`, `status`, `user_id_id`) VALUES (NULL, '$date', '$products', '0', '$user_id');";
	$q_res = $db->query($sql);
}

function strtosql($str){
	return str_replace("'", "\'", $str);
}


?>