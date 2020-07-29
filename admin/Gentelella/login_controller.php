<?php
session_start();
if(isset($_SESSION['name']))
{
    header("location:index.php");
}
include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
extract($_POST);
if(isset($_POST['login'])){

		$username= $_POST['username'];
		$password=$_POST['password'];
	
	$x = "username='$username' AND password='$password'"; 
	$q=$d->select("admin_tb",$x);
	$data=mysqli_fetch_array($q);
	
	if (($data['username']== $username) && ($data['password'] ==$_POST['password']) ) {
		$_SESSION['name'] = $username;
		header('location:index.php');
	}
	else{
		echo "Wrong Username or Password";
	}
}

if(isset($_POST['forgot_password'])){

		$email= $_POST['email'];
	
	$q=$d->select("admin_tb","email='$email'");
	$data=mysqli_fetch_array($q);
	
	$password = $data['password'];

	$to = $data['email'];
	$subject = "Your Recovered Password";
 	
	$message = "Please use this password to login " . $password;
	$headers =  'MIME-Version: 1.0' . "\r\n"; 
	$headers .= 'From: Virat Patel <virat7045@gmail.com>' . "\r\n";
	$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n"; 
	if(mail($to, $subject, $message, $headers)){
		echo "Your Password has been sent to your email id";
	}else{
		echo "Failed to Recover your password, try again";
	}
}
?>