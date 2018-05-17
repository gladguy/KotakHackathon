<?php
		// Bill_ID
		// Vendor_ID
		// Amount
		// Random_ID
		// FirstName
		// LastName
		// AccountNumber
		// DateTime
		// Status
		
		require_once("dbcontroller.php");

		$db_handle = new DBController();

		$vendorid	= $_GET["vendor_id"];
		$randomid	= $_GET["random_id"];
		$amount		= $_GET["amount"];

		$bill_id = $db_handle->kotakPayment($amount,$vendorid,$randomid);
		echo $bill_id;

?>
