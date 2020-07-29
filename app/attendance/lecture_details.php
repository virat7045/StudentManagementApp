<?php
require_once __DIR__.'/../include/DB_Functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
$responses = array();
// check for required fields
if (isset($_POST['faculty_id'])) 
{
 
    $faculty_id = $_POST['faculty_id'];
    // mysql inserting a new row
                
    $user = $db->lecture_details($faculty_id);
    /*$result =array();*/
    if ($user != false) {
// user is found
        while($row = mysqli_fetch_array($user)){
    			array_push($response,array(
    			'error'=>FALSE,
        		'lacture_id'=>$row[0],
        		'faculty_id'=>$row[1],
        		'sem'=>$row[2],
        		'classname'=>$row[3],
        		'subject'=>$row[4],
        		)
    			);  
    		}
    		
    		/*echo json_encode(array("result"=>$response));*/
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Faculty Details Not Found";
        echo json_encode($response);
    }

    $user = $db->lab_details($faculty_id);
    if ($user != false) {
// user is found
        while($row = mysqli_fetch_array($user)){
    			array_push($responses,array(
    			'error'=>FALSE,
        		'lab_id'=>$row[0],
        		'faculty_id'=>$row[1],
        		'sem'=>$row[2],
        		'batch'=>$row[3],
        		'subject'=>$row[4],
        		)
    			);  
    		}
    		
    		/*echo json_encode(array("results"=>$response));*/
    } else {
// user is not found with the credentials
        $responses["error"] = TRUE;
        $responses["error_msg"] = "Faculty Details Not Found";
        echo json_encode($responses);
    }
    	$data = array("result"=>$response,
    					"results"=>$responses);
     echo json_encode($data);
        /*if ($user->num_rows > 0) 
        {
        // user Found successfully
        // output data of each row
        while($row = $user->fetch_assoc()) {
                



                $response["error"] = FALSE;
                $response["user"]["faculty_id"] = $row["faculty_id"];
                $response["user"]["lacture_id"] = $row["lacture_id"];
                $response["user"]["sem"]=$row["sem"];
                $response["user"]["classname"]=$row["classname"];
                $response["user"]["subject"]=$row["subject"];
				echo json_encode($response);
										  }
                        
        } 
        else {
        // No data Found
        $response["error"] = TRUE;
        $response["error_msg"] = "No lacture Details Found";
        echo json_encode($response);
            }*/
}
else{
            // Failed
        $response["error"] = TRUE;
        $response["error_msg"] = "Faculty_id is missing";
        echo json_encode($response);
    }
?>