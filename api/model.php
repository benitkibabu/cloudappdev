<?php
header('Access-Control-Allow-Origin: *');
require_once 'Config.php';

class Model{
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
	
	public function getUsersIngredients($userid){
		$query = "SELECT * FROM ingredients WHERE userid='$userid'";
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
	
	public function getUsersRecipes($userid){
		$query = "SELECT * FROM recipes WHERE userid='$userid'";
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
	
	public function getUser($userid, $logintype){
		$query = "SELECT * FROM users WHERE userid = '$userid' AND logintype='$logintype'";
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
	
	public function addUser($logintype, $userid, $name, $email, $imageurl){
		$query = "INSERT INTO users(logintype, userid, name, email, imageurl) VALUES('$logintype','$userid','$name','$email','$imageurl')";
		$con  = $this->connect();
		if(mysqli_query($con, $query)){				
			return true;
		}
		else{
			return false;
		}		
		$this->close();
	}
	
	public function addRecipe($recipe,$userid){
		$query = "INSERT INTO recipes(recipe, userid) VALUES('$recipe','$userid')";
		$con  = $this->connect();
		if(mysqli_query($con, $query)){				
			return true;
		}
		else{
			return false;
		}		
		$this->close();
	}
	
	public function addIngredient($uri,$quantity, $measure, $weight, $food){
		$query = "INSERT INTO ingredients(uri, quantity, measure, weight, food) VALUES('$uri',$quantity,'$measure',$weight, '$food')";
		$con  = $this->connect();
		if(mysqli_query($con, $query)){				
			return true;
		}
		else{
			return false;
		}		
		$this->close();
	}
	
	public function addDevices($userid, $device_id){
		$query = "INSERT INTO devices(userid, device_id) VALUES('$userid','$device_id')";
		$mysqli = $this->connect();
		$result = $mysqli->query($query);
		if($result){return true;}
		else{return false;}
		
		$this->close();
	}
	
	public function removeRecipe(($id,$userid){
		$query = "DELETE FROM recipes WHERE id=$id AND userid='$userid'";
		$con  = $this->connect();
		if(mysqli_query($con, $query)){
			return true;
		}else{
			return false;
		}
	}
	
	public function removeIngredient(($id,$userid){
		$query = "DELETE FROM ingredients WHERE id=$id AND userid='$userid'";
		$con  = $this->connect();
		if(mysqli_query($con, $query)){
			return true;
		}else{
			return false;
		}
	}
}
?>