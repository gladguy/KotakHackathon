<?php
	//	checkPayment.php?payment_id=1001
	require_once("dbcontroller.php");
	$db_handle = new DBController();

	$random_id = $_GET["random_id"];

	$result = $db_handle->checkKotakPayment($random_id);
	echo $result;
?>


