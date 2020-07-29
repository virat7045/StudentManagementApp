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
if(isset($_POST['lab_details'])){
	$m->set_data('faculty_id',$faculty_id);
	$m->set_data('semester',$semester);
	$m->set_data('batch',$batch);
	$m->set_data('subject',$subject);

	$a=array(
		'faculty_id'=> $m->get_data('faculty_id'),
		'sem'=> $m->get_data('semester'),
		'batch'=> $m->get_data('batch'),
		'subject'=> $m->get_data('subject'),
		);
	$q=$d->insert("lab_details_tb",$a);
	
	if ($q>0) {
		header('location:lab_details.php');
	}
	else{
		echo "error";
	}
}

// Delete Faculty Info......................................................................................................
if(isset($_GET['lab_id']))  {
	$id = $_GET['lab_id'];
	$q=$d->delete("lab_details_tb","lab_id='$id'");
	if($q>0) {
		header('location:view_lab_details.php');
	}
	else {
		echo "Error";
	}

}

if(isset($_POST['update_lab_details'])){
	$m->set_data('faculty_id',$faculty_id);
	$m->set_data('semester',$semester);
	$m->set_data('batch',$batch);
	$m->set_data('subject',$subject);

	$a=array(
		'faculty_id'=> $m->get_data('faculty_id'),
		'sem'=> $m->get_data('semester'),
		'batch'=> $m->get_data('batch'),
		'subject'=> $m->get_data('subject'),
		);
	$q=$d->update("lab_details_tb",$a,"lab_id='$lab_id'");
	
	if ($q>0) {
		header('location:view_lab_details.php');
	}
	else{
		echo "error";
	}
}


?>