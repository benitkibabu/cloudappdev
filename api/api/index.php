<?php
header('Access-Control-Allow-Origin: *');
require_once 'DbUpdate.php';
require_once 'DbStudent.php';
require_once 'GCM.php';
require_once 'Config.php';

/* header( "Location:http://itrackerapp.gear.host/ncigo/manager/" ); */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
	$db = new DbUpdate();
	$dbStudent = new DbStudent();
	
	$tag  = $_POST['tag'];	
	$res  = array("tag" => $tag, "error" => FALSE);
	
	if($tag == "update"){
		$title = $_POST['title'];
		$body  = $_POST['body'];
		$target  = $_POST['target'];
		$source = $_POST['source'];
		
		$list = $dbStudent->getDevices();
		
		if($list != false){
			$devices = array();
			foreach($list as $device){
				$devices[] = $device['device_id'];
			}
			
			$result = $db->addUpdate($title, $body, $target, $source);
						
			$message = "New Update";
			$gcpm = new GCMPushMessage(gcmkey);
			$gcpm->setDevices($devices);
			
			if($result != false){
				$response = $gcpm->send($message, $result[0]);			
				echo json_encode($result[0]);
			}else{
				$result['error'] = TRUE;
				$result['error_msg'] = "Update not added";
				echo json_encode($result);
			}
		}else{
			$response["error"] = TRUE;
			$response["error_msg"] = "List is false";
			echo json_encode($response);
		}		
	}
	else{
		$response["error"] = TRUE;
		$response["error_msg"] = "Unknown tag. Use a proper tag";
		echo json_encode($response);
	}
    
} 
else if(isset($_GET['tag']) && $_GET['tag'] != ''){

	$tag  = $_GET['tag'];
	
	$db = new DbUpdate();
	
	$res = array("tag" => $tag, "error" => FALSE);
	
	if($tag == "getUpdates"){
		
		$result = $db->getUpdates();
		
		if($result != false){			
			$list = array();
			$res["error"] = FALSE;			
			$res['result'] = $result;			
			echo json_encode($res);			
		}
		else{
			$res["error"] = TRUE;
			$res["error_msg"] = "No update found";
			echo json_encode($res);
		}
	}
	else if($tag == "getCourses"){
		
		$result = $db->getCourses();
		
		if($result != false){			
			$list = array();
			$res["error"] = FALSE;			
			$res['result'] = $result;			
			echo json_encode($res);			
		}
		else{
			$res["error"] = TRUE;
			$res["error_msg"] = "No Courses found";
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