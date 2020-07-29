<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();


$response = array("error" => FALSE);

if (isset($_POST['username']) && isset($_POST['password'])) {

// receiving the post params
    $username = $_POST['username'];
    $password = $_POST['password'];

// get the user by email and password
    $user = $db->getUserByUsernameAndPassword($username, $password);

    if ($user != false) {
// user is found
        $response["error"] = FALSE;
        $response["user"]["faculty_id"] = $user["faculty_id"];
        $response["user"]["username"] = $user["username"];
        $response["user"]["password"] = $user["password"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["profile"] = $user["profile"];
        echo json_encode($response);
    } else {
// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login information are wrong. Please try again!";
        echo json_encode($response);
    }
}
 else {
// Type username & password
    $response["error"] = TRUE;
    $response["error_msg"] = "Type username & password";
    echo json_encode($response);
}
?>