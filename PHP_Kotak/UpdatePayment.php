<?php
	//	firstname=Waheed&lastname=Rahuman&random_id=111&account_number=111

	require_once("dbcontroller.php");
	$db_handle = new DBController();

	$random_id		= $_GET["random_id"];
	$firstname		= $_GET["firstname"];
	$lastname		= $_GET["lastname"];
	$account_number	= $_GET["account_number"];

	$result = $db_handle->updateKotakPayment($firstname,$lastname,$random_id,$account_number);

	echo $result;
?>


