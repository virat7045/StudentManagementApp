<?php
session_start();
$url= $_SESSION['name'];
session_unset('name');
session_destroy();
header("Location:login.php?msg=Logout successfully.");

?>