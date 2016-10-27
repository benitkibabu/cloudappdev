<?php
header('Access-Control-Allow-Origin: *');
require_once 'Config.php';

class SessionCon{
    public function connect(){
		$conn = mysqli_connect(dbhost, dbuser,dbpass, dbname);		
		if(!$conn){
			return false;
		}else{
			return $conn;
		}	
	}
}


?>