<?php

include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
	

if($_POST['title']){
	extract($_POST);
	$m->set_data('title',$title);
	$m->set_data('photo',$photo);
	


	$a=array(
		'title'=> $m->get_data('title'),
		'photo'=> $m->get_data('photo'),
		);
	$q=$d->insert("gallary",$a);
	
	if ($q>0) {
		header('location:gallery.html');
	}
	else{
		echo "error";
	}
}
?>