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



