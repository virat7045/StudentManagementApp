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
if(isset($_POST['lecture_details'])){
	$m->set_data('faculty_id',$faculty_id);
	$m->set_data('semester',$semester);
	$m->set_data('classname',$classname);
	$m->set_data('subject',$subject);

	$a=array(
		'faculty_id'=> $m->get_data('faculty_id'),
		'sem'=> $m->get_data('semester'),
		'classname'=> $m->get_data('classname'),
		'subject'=> $m->get_data('subject'),
		);
	$q=$d->insert("lecture_details_tb",$a);
	
	if ($q>0) {
		header('location:lecture_details.php');
	}
	else{
		echo "error";
	}
}

// Delete Lecture Details..................................................................................................
if(isset($_GET['lacture_id']))  {
	$id = $_GET['lacture_id'];
	$q=$d->delete("lecture_details_tb","lacture_id='$id'");
	if($q>0) {
		header('location:view_lecture_details.php');
	}
	else {
		echo "Error";
	}

}

if(isset($_POST['update_lecture_details'])){
	$m->set_data('faculty_id',$faculty_id);
	$m->set_data('semester',$semester);
	$m->set_data('classname',$classname);
	$m->set_data('subject',$subject);

	$a=array(
		'faculty_id'=> $m->get_data('faculty_id'),
		'sem'=> $m->get_data('semester'),
		'classname'=> $m->get_data('classname'),
		'subject'=> $m->get_data('subject'),
		);
	$q=$d->update("lecture_details_tb",$a,"lacture_id='$lacture_id'");
	
	if ($q>0) {
		header('location:view_lecture_details.php');
	}
	else{
		echo "error";
	}
}


?>