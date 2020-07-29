<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['_faculty_id']) && isset($_POST['_lecture_id'])) 
{
    // mysql inserting a new row
                
    $user = $db->generate_fetch_assignment_marks($_POST['_faculty_id'],$_POST['_lecture_id']);
    if ($user != false) {
// user is found
        while($row = mysqli_fetch_array($user)){
    			array_push($response,array(
    			'error'=>FALSE,
    			'assignment_id'=> $row[2],
                'assignment_name'=>$row[3],
        		'name'=>$row[8],
        		'e_no'=>$row[9],
        		'marks'=>$row[10],
        		)
    			);  
    		}
    		
    		echo json_encode(array("result"=>$response));
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Students DATA Not Found";
        echo json_encode($response);
    }
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "Quiz_id is missing";
        echo json_encode($response);}
    
?>