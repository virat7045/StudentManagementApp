<?php

class dbconnect
{
    function connect()
    {
        $connection=mysqli_connect("localhost","root","root","student_management");
				return $connection;
    }
}

?>