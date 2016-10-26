<?php
header('Access-Control-Allow-Origin: *');
require_once 'GCM.php';
require_once 'Config.php';

/* header( "Location:http://itrackerapp.gear.host/ncigo/manager/" ); */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    
} 
else if(isset($_GET['tag']) && $_GET['tag'] != ''){
	
}
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>