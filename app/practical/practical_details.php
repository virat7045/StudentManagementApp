<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['_faculty_id']) && isset($_POST['_lab_id']) && isset($_POST['_sem']) && isset($_POST['_batch']) && isset($_POST['_subject'])) 
{
    $faculty_id = $_POST['_faculty_id'];
    $lab_id = $_POST['_lab_id'];
    $sem = $_POST['_sem'];
    $batch = $_POST['_batch'];
    $subject = $_POST['_subject'];
    // mysql inserting a new row
                
    $user = $db->practical_details($faculty_id,$lab_id,$sem,$batch,$subject);
    if ($user != false) {
// user is found
        while($row = mysqli_fetch_array($user)){
    			array_push($response,array(
    			'error'=>FALSE,
        		'practical_id'=>$row[0],
        		'practical_name'=>$row[6],
        		'practical_date'=>$row[7],
        		'description'=>$row[8],
        		)
    			);  
    		}
    		
    		echo json_encode(array("result"=>$response));
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "practical Details Not Found";
        echo json_encode($response);
    }
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "Input Data is missing";
        echo json_encode($response);}
    
?>