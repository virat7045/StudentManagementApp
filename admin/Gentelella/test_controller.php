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

// Add Faculty Info...............................................................................................
/*if (isset($_POST['file'])) {*/
	$filename=$_FILES["file"]["tmp_name"];	
	if($_FILES["file"]["size"] > 0)
		 {
		  	$file = fopen($filename, "r");
	        while (($getData = fgetcsv($file, 10000, ",")) !== FALSE)
	         {
	         	$m->set_data('e_no',$getData[0]);
				$m->set_data('name',$getData[1]);
				$m->set_data('sem',$getData[2]);
				$m->set_data('class',$getData[3]);
				$m->set_data('batch',$getData[4]);


				$a=array(
				'e_no'=> $m->get_data('e_no'),
				'name'=> $m->get_data('name'),
				'sem'=> $m->get_data('sem'),
				'class'=> $m->get_data('class'),
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
	         fclose($file);	
		 }
/*}*/

?>