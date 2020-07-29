<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['_faculty_id']) && isset($_POST['_lecture_id']))
{
 
    $user = $db->generate_fetch_lecture_marks($_POST['_faculty_id'],$_POST['_lecture_id']);
    if ($user != false) {
        while($row = mysqli_fetch_array($user)){
    			array_push($response,array(
    			'error'=>FALSE,
                'u_id'=>$row[0],
        		'e_no'=>$row[3],
        		'name'=>$row[4],
                'date'=>$row[8],
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