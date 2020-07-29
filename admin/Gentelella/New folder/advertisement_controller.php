<?php

include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
	

if($_POST['title']){
	extract($_POST);
	$m->set_data('title',$title);
	$m->set_data('url',$url);
	$m->set_data('photo',$photo);
	$m->set_data('description',$description);
	


	$a=array(
		'title'=> $m->get_data('title'),
		'url'=> $m->get_data('url'),
		'photo'=> $m->get_data('photo'),
		'description'=> $m->get_data('description'),
		);
	$q=$d->insert("advertisements",$a);
	
	if ($q>0) {
		header('location:advertisement.html');
	}
	else{
		echo "error";
	}
}
?>