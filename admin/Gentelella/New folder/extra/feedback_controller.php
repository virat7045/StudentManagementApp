<?php

include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
	
	echo($_POST['name']);

if($_POST['name']){
	extract($_POST);
	$m->set_data('name',$name);
	$m->set_data('email',$email);
	$m->set_data('subject',$subject);
	$m->set_data('message',$message);
	


	$a=array(
		'name'=> $m->get_data('name'),
		'email'=> $m->get_data('email'),
		'subject'=> $m->get_data('subject'),
		'message'=> $m->get_data('message'),
		);
	$q=$d->insert("feedback",$a);
	
	if ($q>0) {
		header('location:feedback.html');
	}
	else{
		echo "error";
	}
}
?>