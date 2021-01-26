<?php
	include '../db_master.php';
	$category_id = $_GET["category_id"];
	$db = init_db();
	$res = getCategoryImages($db, $category_id, 6);
	$images = array();
	for ($i=0; $i < $res->num_rows; $i++) { 
		$row = $res->fetch_assoc();
		$images[] = $row["photo"];
	}
?>