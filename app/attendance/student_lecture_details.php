<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['sem']) && isset($_POST['classname'])) 
{
 
    $sem = $_POST['sem'];
    $classname = $_POST['classname'];
    // mysql inserting a new row
                
    $user = $db->student_lecture_details($sem,$classname);
    /*$result =array();*/
    if ($user != false) {
// user is found
        while($row = mysqli_fetch_array($user)){
    			array_push($response,array(
    			'error'=>FALSE,
        		'student_id'=>$row[0],
        		'e_no'=>$row[1],
        		'name'=>$row[2],
        		'sem'=>$row[3],
        		'classname'=>$row[4],
        		)
    			);  
    		}
    		
    		echo json_encode(array("result"=>$response));
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Faculty Details Not Found";
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