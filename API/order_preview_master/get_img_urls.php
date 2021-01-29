<?php
	include '../db_master.php';
	$order_id = $_GET["order_id"];
	$db = init_db();
	$ids = getOrderItemIds($db, $order_id);
	$goods = getGoodsByIds($db, $ids);
	$images = array();
	while ($row = $goods->fetch_assoc()) {
		$images[] = $row["photo"];
	}
?>