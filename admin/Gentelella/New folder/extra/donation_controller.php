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
	$m->set_data('mobile',$mobile);
	$m->set_data('address',$address);
	$m->set_data('amount',$amount);



	$a=array(
		'name'=> $m->get_data('name'),
		'email'=> $m->get_data('email'),
		'mobile'=> $m->get_data('mobile'),
		'address'=> $m->get_data('address'),
		'amount'=> $m->get_data('amount'),
		);
	$q=$d->insert("donation",$a);
	
	if ($q>0) {
		header('location:donation.html');
	}
	else{
		echo "error";
	}
}
?>