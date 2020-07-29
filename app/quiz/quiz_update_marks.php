<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array("error"=> FALSE);
// check for required fields
if (isset($_POST['quiz_id']) && isset($_POST['name']) && isset($_POST['e_no']) && isset($_POST['marks'])) 
{           
          
    // mysql inserting a new row
                
    $user = $db->quiz_update_marks($_POST['quiz_id'],$_POST['name'],$_POST['e_no'],$_POST['marks']);
    if ($user) {
        // Data saved Successfully  
        $response["success"] = "Successfully Data Updated";      	
    		echo json_encode($response);
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Error in updating assignmnet marks";
        echo json_encode($response);
    }
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "input data is missing";
        echo json_encode($response);
    }
?>