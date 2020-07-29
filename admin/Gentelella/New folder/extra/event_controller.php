<?php

include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
	
	echo($_POST['title']);

if($_POST['title']){
	extract($_POST);
	$m->set_data('title',$title);
	$m->set_data('phone',$phone);
	$m->set_data('description',$description);
	$m->set_data('place',$place);
	$m->set_data('date',$date);


	$a=array(
		'title'=> $m->get_data('title'),
		'phone'=> $m->get_data('phone'),
		'description'=> $m->get_data('description'),
		'place'=> $m->get_data('place'),
		'date'=> $m->get_data('date'),

		);
	$q=$d->insert("event",$a);
	
	if ($q>0) {
		header('location:event.html');
	}
	else{
		echo "error";
	}
}
?>