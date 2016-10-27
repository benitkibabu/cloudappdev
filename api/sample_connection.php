<?php

	$dbhost="";
	$dbuser="";
	$dbpass="";
	$dbname="";
	
	$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);		
	if(!$conn){
		echo "No connection made";
	}	
	
	$query = "SELECT * FROM users";
	$res = mysqli_query($conn, $query);
	
	if(mysqli_num_rows($res) > 0){
		$rows = array();
		while($row = mysqli_fetch_array($res,MYSQLI_ASSOC)){
			$rows[] = $row;
		}
		return $rows;
	}
	else{
		echo "Found nothing";
	}
?>



