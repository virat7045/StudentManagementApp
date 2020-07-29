<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['_faculty_id']) && isset($_POST['_lab_id']) && isset($_POST['_sem']) && isset($_POST['_batch']) && isset($_POST['_subject']) && isset($_POST['_practical_name']) && isset($_POST['_practical_date']) && isset($_POST['_description'])) 
{

    $user = $db->practical_add($_POST['_faculty_id'],$_POST['_lab_id'],$_POST['_sem'],$_POST['_batch'],$_POST['_subject'],$_POST['_practical_name'],$_POST['_practical_date'],$_POST['_description']);
    if ($user) {
// user is found
       $response["success"] = TRUE;
       $response["success_msg"] = "Practical uploaded Successfully";
       echo json_encode($response);
    	
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Error in uploading practical";
        echo json_encode($response);
    }
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "Input Data is missing";
        echo json_encode($response);}
    
?>