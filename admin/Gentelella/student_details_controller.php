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
if(isset($_POST['student_details'])){
	$m->set_data('e_no',$e_no);
	$m->set_data('name',$name);
	$m->set_data('sem',$sem);
	$m->set_data('classname',$classname);
	$m->set_data('batch',$batch);

	$a=array(
		'e_no'=> $m->get_data('e_no'),
		'name'=> $m->get_data('name'),
		'sem'=> $m->get_data('sem'),
		'class'=> $m->get_data('classname'),
		'batch'=> $m->get_data('batch'),
		);
	$q=$d->insert("student_details_tb",$a);
	
	if ($q>0) {
		header('location:student_details.php');
	}
	else{
		echo "error";
	}
}
// Delete Student Details..................................................................................................
if(isset($_GET['student_id']))  {
	$id = $_GET['student_id'];
	$q=$d->delete("student_details_tb","student_id='$id'");
	if($q>0) {
		header('location:view_student_details.php');
	}
	else {
		echo "Error";
	}

}

// Update Student Details
if(isset($_POST['update_student_details'])){
	$m->set_data('e_no',$e_no);
	$m->set_data('name',$name);
	$m->set_data('sem',$sem);
	$m->set_data('classname',$classname);
	$m->set_data('batch',$batch);

	$a=array(
		'e_no'=> $m->get_data('e_no'),
		'name'=> $m->get_data('name'),
		'sem'=> $m->get_data('sem'),
		'class'=> $m->get_data('classname'),
		'batch'=> $m->get_data('batch'),
		);
	$q=$d->update("student_details_tb",$a,"student_id='$student_id'");
	
	if ($q>0) {
		header('location:view_student_details.php');
	}
	else{
		echo "error";
	}
}


?>