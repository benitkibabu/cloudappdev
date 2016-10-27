<?php
header('Access-Control-Allow-Origin: *');
require_once 'Config.php';

class DbStudent{
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
	
	public function getStudents(){
		$query = "SELECT * FROM student";
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
	
	public function getDevices(){
		$conn = $this->connect();
		$result = mysqli_query($conn, "SELECT * FROM devices");
		if(mysqli_num_rows($result) > 0){
			$rows = array();
			while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
				$rows[] = $row;
			}
			return $rows;
		}
		else{
			return false;
		}
		
		$this->close();
	}
	
	public function getStudent($id, $password){
		$query = "SELECT * FROM student WHERE student_no = '$id' AND password='$password'";
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
	
	public function getStudentById($student_no){
		$query = "SELECT * FROM student WHERE student_no='$student_no'";
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
	
	public function addStudent($student_no, $email, $password, $reg_id, $course, $status){
		$query = "INSERT INTO student(student_no, student_email, password, reg_id, course, status, first_name, last_name) VALUES('$student_no','$email','$password','$reg_id','$course', '$status', 'null', 'null')";
		$con  = $this->connect();
		$res = mysqli_query($con, $query);
		if(mysqli_num_rows($res)> 0){				
			$res = mysqli_query($con, "SELECT * FROM student WHERE student_no = '$student_no'");
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
	
	public function updateStatus($student_no, $status){
		$query = "UPDATE student SET status = '$status' WHERE student_no = '$student_no'";
		$con = $this->connect();
		if(mysqli_query($con, $query)){
			return true;
		}else{
			return false;
		}
		
		$this->close();
		
	}
	
	public function updateProfile($student_no,$firstName, $lastName){
		$query = "UPDATE student SET first_name = '$firstName', last_name = '$lastName' WHERE student_no = '$student_no'";
		$con = $this->connect();
		if(mysqli_query($con, $query)){
			$res = mysqli_query($con, "SELECT * FROM student WHERE student_no = '$student_no'");
			$rows = array();
			while($row = mysqli_fetch_array($res,MYSQLI_ASSOC)){
				$rows[] = $row;
			}
			return $rows;
		}else{
			return false;
		}
		
		$this->close();
		
	}
	
	public function addDevices($student_no, $device_id){
		$query = "INSERT INTO devices(student_no, device_id) VALUES('$student_no','$device_id')";
		$mysqli = $this->connect();
		$result = $mysqli->query($query);
		if($result){return true;}
		else{return false;}
		
		$this->close();
	}
}
?>