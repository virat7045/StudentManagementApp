<?php

session_start();
if (!isset($_SESSION['name'])) {
header('location:login.php');
} 
include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
extract($_POST);
extract($_GET);

// Add Faculty Info...............................................................................................
if(isset($_POST['name'])){

		$imagetype=$_FILES["profile"]['type'];
		$imagesize=$_FILES["profile"]['size'];
		$image_Arr = $_FILES['profile']; 
 // print_r($image_Arr);
	  move_uploaded_file($image_Arr['tmp_name'],'img/'.$image_Arr['name']);
	  $profile = $image_Arr['name'];

	$m->set_data('name',$name);
	$m->set_data('username',$username);
	$m->set_data('email',$email);
	$m->set_data('password',$password);
	$m->set_data('profile',$profile);

	$a=array(
		'name'=> $m->get_data('name'),
		'username'=> $m->get_data('username'),
		'email'=> $m->get_data('email'),
		'password'=> $m->get_data('password'),
		'profile'=> $m->get_data('profile'),
		);
	$q=$d->insert("users_tb",$a);
	
	if ($q>0) {
		header('location:faculty.php');
	}
	else{
		echo "error";
	}
}

// Delete Faculty Info......................................................................................................
if(isset($_GET['faculty_id']))  {
	$id = $_GET['faculty_id'];
	$q=$d->delete("users_tb","faculty_id='$id'");
	if($q>0) {
		header('location:view_faculty.php');
	}
	else {
		echo "Error";
	}

}

// Update Faculty Info...............................................................................................
if(isset($_POST['namee'])){

		$imagetype=$_FILES["profile"]['type'];
		$imagesize=$_FILES["profile"]['size'];
		$image_Arr = $_FILES['profile']; 
 // print_r($image_Arr);
	  move_uploaded_file($image_Arr['tmp_name'],'img/'.$image_Arr['name']);
	  $profile = $image_Arr['name'];

	$m->set_data('namee',$namee);
	$m->set_data('username',$username);
	$m->set_data('email',$email);
	$m->set_data('password',$password);
	$m->set_data('profile',$profile);

	$a=array(
		'name'=> $m->get_data('namee'),
		'username'=> $m->get_data('username'),
		'email'=> $m->get_data('email'),
		'password'=> $m->get_data('password'),
		'profile'=> $m->get_data('profile'),
		);

	$a1=array(
		'name'=> $m->get_data('namee'),
		'username'=> $m->get_data('username'),
		'email'=> $m->get_data('email'),
		'password'=> $m->get_data('password'),
		);
	
	if($profile == "") {
	$q=$d->update("users_tb",$a1,"faculty_id='$faculty_id'");
	} else {
	$q=$d->update("users_tb",$a,"faculty_id='$faculty_id'");
	}
	if ($q>0) {
		header('location:view_faculty.php');
	}
	else{
		echo "error";
	}
}





?>