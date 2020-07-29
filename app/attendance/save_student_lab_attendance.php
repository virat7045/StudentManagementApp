<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array("error"=> FALSE);
// check for required fields
if (isset($_POST['faculty_id']) && isset($_POST['lab_id']) && isset($_POST['e_no']) && isset($_POST['name']) && isset($_POST['sem']) && isset($_POST['batch']) && isset($_POST['subject']) && isset($_POST['status'])) 
{           
          
    // mysql inserting a new row
                
    $user = $db->save_student_lab_attendance($_POST['faculty_id'],$_POST['lab_id'],$_POST['e_no'],$_POST['name'],$_POST['sem'],$_POST['batch'],$_POST['subject'],$_POST['status']);
    if ($user) {
        // Data saved Successfully  
        $response["success"] = "Successfully Data Saved";      	
    		echo json_encode($response);
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Attendance Already Saved";
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