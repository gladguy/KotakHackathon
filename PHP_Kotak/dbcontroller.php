<?php
class DBController {
	private $host = "localhost";
	private $user = "waheed_water";
	private $password = "rehan123";
	private $database = "waheed_puliyal";

	//private $user = "root";
	//private $database = "test";

	
	function __construct() {
		$conn = $this->connectDB();
		if(!empty($conn)) {
			$this->selectDB($conn);
		}
	}
	
	function connectDB() {
		$conn = mysql_connect($this->host,$this->user,$this->password);
		return $conn;
	}
	
	function selectDB($conn) {
		mysql_select_db($this->database,$conn);
	}

	function updateKotakPayment($firstname,$lastname,$random_id,$account_number)
	{
			$sql = "update kotak_payments set Status = TRUE,FirstName='".$firstname."',LastName='".$lastname."',AccountNumber=".$account_number." where Random_ID=".$random_id;
			if (mysql_query($sql) === TRUE)
			{
				return 1; //Success
			} 
			else
			{
				return 0; //Failed
			}
	}
	function kotakPayment($amount,$vendorid,$randomid)
	{

			// Bill_ID
			// Vendor_ID
			// Amount
			// Random_IDcheckPayment
			// FirstName
			// LastName
			// AccountNumber
			// Bill_DateTime
			// Status

			$sql = "INSERT INTO kotak_payments (Vendor_ID, Random_ID, Amount, Bill_DateTime, FirstName, LastName, AccountNumber, Status ) VALUES(".$vendorid.",".$randomid.", ".$amount.", now(),'FIRSTNAME', 'LASTNAME', 'ACCOUNTNUMBER', FALSE )";

			if (mysql_query($sql) === TRUE)
			{
				$result = mysql_query("SELECT MAX(Bill_ID) FROM kotak_payments");
				$row = mysql_fetch_row($result);
				$highest_id = $row[0];
				return $highest_id;
			} 
			else
			{
				return 0;
			}
	}


	function checkKotakPayment($random_id)
	{
			$cursor = mysql_query("SELECT Status,LastName FROM kotak_payments where Random_ID=".$random_id) or die($query. "<br/><br/>".mysql_error());
			$row = mysql_fetch_row($cursor);
			$highest_id = $row[0];
			return $highest_id;
	}

	function runQuery($query) {
		$result = mysql_query($query);
		while($row=mysql_fetch_assoc($result)) {
			$resultset[] = $row;
		}		
		if(!empty($resultset))
			return $resultset;
	}
	
	function numRows($query) {
		$result  = mysql_query($query);
		$rowcount = mysql_num_rows($result);
		return $rowcount;	
	}
}
?>