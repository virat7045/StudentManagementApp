<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array("error"=> FALSE);
// check for required fields
if (isset($_POST['u_id']) && isset($_POST['status'])) 
{           
          
    // mysql inserting a new row
                
    $user = $db->update_lab_attendance($_POST['u_id'],$_POST['status']);
    if ($user) {
        // Data saved Successfully  
        $response["success"] = "Successfully Data Updated";      	
    		echo json_encode($response);
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Error in Saving Attendance";
        echo json_encode($response);
    }
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "Faculty_id is missing";
        echo json_encode($response);
    }
?>