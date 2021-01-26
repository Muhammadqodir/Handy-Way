<?php
	include "db_master.php";

	$db = init_db();
	echo "_OK_";
	$db->close();
?>