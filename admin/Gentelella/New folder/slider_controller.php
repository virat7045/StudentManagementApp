<?php

include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
	

if($_POST['buttonsubmit']){
	extract($_POST);
	$m->set_data('title',$title);
	$m->set_data('photo',$photo);
	


	$a=array(
		'title'=> $m->get_data('title'),
		'photo'=> $m->get_data('photo'),
		);
	$q=$d->insert("sliders",$a);
	
	if ($q>0) {
		header('location:sliders.html');
	}
	else{
		echo "error";
	}
}
?>