<?php
header('Access-Control-Allow-Origin: *');
require_once 'Config.php';

class Users{
	public function connect(){
		$conn = mysqli_connect(dbhost, dbuser,dbpass, dbname);		
		if(!$conn){
			return false;
		}else{
			return $conn;
		}	
	}
	
	public function close(){
		mysqli_close();
	}
	
	public function getUsers(){
		$query = "SELECT * FROM users";
		$con  = $this->connect();
		$res = mysqli_query($con, $query);
		if(mysqli_num_rows($res) > 0){
			$rows = array();
			while($row = mysqli_fetch_array($res,MYSQLI_ASSOC)){
				$rows[] = $row;
			}
			return $rows;
		}
		else{
			return false;
		}
		$this->close();
	}
	
	public function getUser($id, $password){
		$query = "SELECT * FROM users WHERE id = '$id' AND password='$password'";
		$con  = $this->connect();
		$res = mysqli_query($con, $query);
		if(mysqli_num_rows($res) > 0){			
			$rows = array();
			while($row = mysqli_fetch_array($res,MYSQLI_ASSOC)){
				$rows[] = $row;
			}
			return $rows;
		}
		else{
			return false;
		}
		$this->close();
	}
	
	public function getStudentById($id){
		$query = "SELECT * FROM users WHERE id='$id'";
		$con  = $this->connect();
		$res = mysqli_query($con, $query);
		if(mysqli_num_rows($res) > 0){			
			$rows = array();
			while($row = mysqli_fetch_array($res,MYSQLI_ASSOC)){
				$rows[] = $row;
			}
			return $rows;
		}
		else{
			return false;
		}
		$this->close();
	}
	
	public function addStudent($logintype, $id, $name, $email, $image_url){
		$query = "INSERT INTO users(logintype, id, name, email, image_url) VALUES('$logintype','$id','$name','$email','$image_url')";
		$con  = $this->connect();
		$res = mysqli_query($con, $query);
		if(mysqli_num_rows($res)> 0){				
			$res = mysqli_query($con, "SELECT * FROM users WHERE id = '$id'");
			$rows = array();
			while($row = mysqli_fetch_array($res,MYSQLI_ASSOC)){
				$rows[] = $row;
			}
			return $rows;
		}
		else{
			return false;
		}
		
		$this->close();
	}
	
	public function addDevices($id, $device_id){
		$query = "INSERT INTO devices(id, device_id) VALUES('$id','$device_id')";
		$mysqli = $this->connect();
		$result = $mysqli->query($query);
		if($result){return true;}
		else{return false;}
		
		$this->close();
	}
}
?>