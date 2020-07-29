<?php

include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
	
	echo($_POST['title']);

if($_POST['button_send']){
	extract($_POST);
	$m->set_data('title',$title);
	$m->set_data('description',$description);
	


	$a=array(
		'title'=> $m->get_data('title'),
		'description'=> $m->get_data('description'),
		);
	$q=$d->insert("news",$a);
	
	if ($q>0) {
		header('location:news.html');
	}
	else{
		echo "error";
	}
}
?>