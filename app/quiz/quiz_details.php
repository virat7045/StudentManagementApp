<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['_faculty_id']) && isset($_POST['_lacture_id']) && isset($_POST['_sem']) && isset($_POST['_classname']) && isset($_POST['_subject'])) 
{
    $faculty_id = $_POST['_faculty_id'];
    $lacture_id = $_POST['_lacture_id'];
    $sem = $_POST['_sem'];
    $classname = $_POST['_classname'];
    $subject = $_POST['_subject'];
    // mysql inserting a new row
                
    $user = $db->quiz_details($faculty_id,$lacture_id,$sem,$classname,$subject);
    if ($user != false) {
// user is found
        while($row = mysqli_fetch_array($user)){
    			array_push($response,array(
    			'error'=>FALSE,
        		'quiz_id'=>$row[0],
        		'quiz_name'=>$row[6],
        		'quiz_date'=>$row[7],
        		'description'=>$row[8],
        		)
    			);  
    		}
    		
    		echo json_encode(array("result"=>$response));
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Quiz Details Not Found";
        echo json_encode($response);
    }
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "Input Data is missing";
        echo json_encode($response);}
    
?>