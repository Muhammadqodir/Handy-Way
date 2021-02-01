<?php
	include 'db_master.php';
	$tel = strtosql($_POST["tel"]);
	$password = strtosql($_POST["password"]);
	$device = strtosql($_POST["device"]);
	$ip = $_SERVER['REMOTE_ADDR'];
	$details = json_decode(file_get_contents("http://ipinfo.io/{$ip}/json"));
	$location = strtosql($details->country.", ".$details->city.", ".$details->region);
	$date_time = date("d-m-Y H:i:s");
	$res = array();
	$db = init_db();
	$user_id = chekUserAuth($db, $tel, $password);
	if ($user_id > -1) {
		$token = bin2hex(random_bytes(16));
		newUserAuth($db, $token, $ip, $location, $date_time, $device, $user_id);
		$user_data = getUserData($db, getUserId($db, $token));
		$res["code"] = 1;
		$res["message"] = "_OK_";
		$user_data["token"] = $token;
		$res["user_data"] = $user_data;
		echo json_encode($res);
	}else{
		$res["code"] = 4;
		$res["message"] = "_invalid_pass_";
		$res["user_data"] = array(
		    "res" => "empty"
		);
		echo json_encode($res);
	}
$db->close();
?>