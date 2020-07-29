<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['faculty_id']) && isset($_POST['lab_id']) && isset($_POST['sem']) && isset($_POST['batch']) && isset($_POST['subject']) && isset($_POST['date']))
{
 
    $user = $db->update_lab_attendance_details($_POST['faculty_id'],$_POST['lab_id'],$_POST['sem'],$_POST['batch'],$_POST['subject'],$_POST['date']);

    if ($user != false) {
        while($row = mysqli_fetch_array($user)){
    			array_push($response,array(
    			'error'=>FALSE,
                'u_id'=>$row[0],
        		'e_no'=>$row[3],
        		'name'=>$row[4],
        		'status'=>$row[9],
        		)
    			);  
    		}
    		echo json_encode(array("result"=>$response));
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Attendance Record  Not Found";
        echo json_encode($response);
    }
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "Data is missing";
        echo json_encode($response);
    }
?>