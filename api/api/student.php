<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Max-Age: 86400');

require_once 'DbStudent.php';

// Access-Control headers are received during OPTIONS requests
if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {

	if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_METHOD']))
		header("Access-Control-Allow-Methods: GET, POST, OPTIONS");         

	if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']))
		header("Access-Control-Allow-Headers:        
		{$_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']}");

exit(0);
}    

if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$db = new DbStudent();
	
	$tag  = $_POST['tag'];	
	$res  = array("tag" => $tag, "error" => FALSE);
	
	if($tag == "student"){
		$student_no = $_POST['student_no'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		$device_id = $_POST['reg_id']; 
		$course = $_POST['course'];
		$status = $_POST['status'];
		
		$res = $db->addStudent($student_no,$email,$password,$device_id,$course, $status);
		if($res != false){
			if($db-> addDevices($student_no, $device_id)){	
			}
			$stat = $db->updateStatus($student_no,$status);
			if($stat){
				$res["error"] = FALSE;
				$res["error_msg"] = "Student status updated";				
				echo json_encode($res);	;
			}else{
				$res["error"] = TRUE;
				$res["error_msg"] = "Student status Not updated";
				echo json_encode($res);
			}	
		}else{
			$res = $db->getStudent($student_no,$password);		
			if($res != false){
				if($db-> addDevices($student_no, $device_id)){
				}
				$stat = $db->updateStatus($student_no,$status);
				if($stat){
					$res["error"] = FALSE;
					$res["error_msg"] = "Student status updated";				
					echo json_encode($res);	;
				}else{
					$res["error"] = TRUE;
					$res["error_msg"] = "Student status Not updated";
					echo json_encode($res);
				}				
			}
			else{
				$res["error"] = TRUE;
				$res["error_msg"] = "Student Not found";
				echo json_encode($res);
			}
		}		
	}
	else if($tag == "updateStatus"){
		$student_no = $_POST['student_no'];
		$status = $_POST['status'];
		$stat = $db->updateStatus($student_no,$status);
		if($stat){
			$res["error"] = FALSE;
			$res["error_msg"] = "Student status updated";
			echo json_encode($res);
		}else{
			$res["error"] = TRUE;
			$res["error_msg"] = "Student status Not updated";
			echo json_encode($res);
		}
	}
	else if($tag == "updateProfile"){
		$student_no = $_POST['student_no'];
		$firstName = $_POST['first_name'];
		$lastName = $_POST['last_name'];
		$stat = $db->updateProfile($student_no,$firstName, $lastName);
		if($stat != false){
			$res["error"] = FALSE;
			$res["result"] = $stat;
			echo json_encode($res);
		}else{
			$res["error"] = TRUE;
			$res["error_msg"] = "Student status Not updated";
			echo json_encode($res);
		}
	}
	else{
		$response["error"] = TRUE;
		$response["error_msg"] = "Unknown tag. Use a proper tag";
		echo json_encode($response);
	}
    
} 
else if(isset($_GET['tag']) && $_GET['tag'] != ''){
	require_once 'DbStudent.php';
	$db = new DbStudent();
	
	$tag  = $_GET['tag'];	
	$res  = array("tag" => $tag, "error" => FALSE);
	
	if($tag == "student"){
		$student_no = $_GET['student_no'];
		$email = $_GET['email'];
		$password = $_GET['password'];
		$device_id = $_GET['reg_id']; 
		$course = $_GET['course'];
		$status = $_GET['status'];
		
		$res = $db->addStudent($student_no,$email,$password,$device_id,$course,$status);
		if($res != false){
			if($db-> addDevices($student_no, $device_id)){	
			}
			$stat = $db->updateStatus($student_no,$status);
			if($stat){
				$res["error"] = FALSE;
				$res["error_msg"] = "Student status updated";				
				echo json_encode($res);	;
			}else{
				$res["error"] = TRUE;
				$res["error_msg"] = "Student status Not updated";
				echo json_encode($res);
			}		
		}else{
			$res = $db->getStudent($student_no,$password);		
			if($res != false){
				if($db-> addDevices($student_no, $device_id)){
				}
				
				$stat = $db->updateStatus($student_no,$status);
				if($stat){
					$res["error"] = FALSE;
					$res["error_msg"] = "Student status updated";				
					echo json_encode($res);	;
				}else{
					$res["error"] = TRUE;
					$res["error_msg"] = "Student status Not updated";
					echo json_encode($res);
				}
			}
			else{
				$res["error"] = TRUE;
				$res["error_msg"] = "Student Not found";
				echo json_encode($res);
			}
		}		
	}
	else if($tag == "getStudentInfo"){
		$student_no = $_GET['student_no'];
		$result = $db->getStudentById($student_no);
		if($result != false){
			$res["error"] = FALSE;
			$res['result'] = $result;
			echo json_encode($res);
		}else{
			$res["error"] = TRUE;
			$res["error_msg"] = "Failed to retrieve student details";
			echo json_encode($res);
		}
	}
	else if($tag == "getStudents"){
		$result = $db->getStudents();
		if($result != false){
			$res['error'] = false;
			$res['result'] = $result;
			echo json_encode($res);
		}
	}
	else{
		$response["error"] = TRUE;
		$response["error_msg"] = "Unknown tag. Use a proper tag";
		echo json_encode($response);
	}
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>