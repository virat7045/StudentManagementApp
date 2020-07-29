<?php

include_once('lib/dao.php');
include_once('lib/model.php');

$d = new dao();
$m = new model();
	
	echo($_POST['question']);

if($_POST['question']){
	extract($_POST);
	$m->set_data('question',$question);
	$m->set_data('answer',$answer);
	


	$a=array(
		'question'=> $m->get_data('question'),
		'answer'=> $m->get_data('answer'),
		);
	$q=$d->insert("faq",$a);
	
	if ($q>0) {
		header('location:faq.html');
	}
	else{
		echo "error";
	}
}
?>