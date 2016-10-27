<?php
header('Access-Control-Allow-Origin: *');
require_once 'GCM.php';
require_once 'Config.php';

/* header( "Location:http://virtualkitchen.gear.host/api/" ); */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    $tag = $_POST['tag'];
	if($tag == "post_user"){
		
	}else if($tag == "post_user_recipe"){
		
	}
} 
else if(isset($_GET['tag']) && $_GET['tag'] != ''){
	$tag = $_GET['tag'];
	if($tag == "get_user"){
		
	}else if($tag =="get_user_recipes"){
		
	}
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>