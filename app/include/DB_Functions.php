<?php
  
class DB_Functions {
 
    private $conn;
 
 // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
  // destructor
    function __destruct() {
         
    }
 
     // Get user by username and password
     
    public function getUserByUsernameAndPassword($username, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users_tb WHERE username = ?");
 
        $stmt->bind_param("s", $username);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            
            $password_tb = $user['password'];
            if ($password == $password_tb) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    public function lecture_details($faculty_id) {

    $stmt = $this->conn->prepare("SELECT * FROM lecture_details_tb WHERE faculty_id='$faculty_id'");
    $stmt->execute();
    $result = $stmt->get_result();
    $stmt -> close();
    
        //check for success
        if ($result) {
            return $result;
        } else {
            return false;
        }
    }

    public function lab_details($faculty_id) {

    $stmt = $this->conn->prepare("SELECT * FROM lab_details_tb WHERE faculty_id='$faculty_id'");
    $stmt->execute();
    $result = $stmt->get_result();
    $stmt -> close();
    
        //check for success
        if ($result) {
            return $result;
        } else {
            return false;
        }
    }

    public function student_lecture_details($sem,$classname) {
    //Fetch Students From that class,Sem,Subject
    $stmt = $this->conn->prepare("SELECT * FROM student_details_tb WHERE sem='$sem' AND class='$classname'");
    $stmt->execute();
    $result = $stmt->get_result();
    $stmt -> close();                
        //check for success
    if ($result) {
        return $result;
    } else {
            
        return false;            
    }
    }


    public function student_lab_details($sem,$classname) {
    //Fetch Students From that class,Sem,Subject
    $stmt = $this->conn->prepare("SELECT * FROM student_details_tb WHERE sem='$sem' AND batch='$classname'");
    $stmt->execute();
    $result = $stmt->get_result();
    $stmt -> close();                
        //check for success
    if ($result) {
        return $result;
    } else {
            
        return false;            
    }
    }
/*
    public function save_student_lecture_attendance($faculty_id,$lecture_id,$e_no,$name,$sem,$classname,$subject,$status)
    {
        $stmt = $this->conn->prepare("INSERT INTO lecture_attendance_tb(faculty_id,lacture_id,e_no,name,sem,class,subject,_date,status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $date = date('y-m-d',time());
        for ($i=0; $i <count($name) ; $i++) { 
        $stmt->bind_param("sssssssss", $faculty_id,$lecture_id,$e_no[$i],$name[$i],$sem,$classname,$subject,$date,$status[$i]);
        $result = $stmt->execute();
        }
        
        $stmt -> close();
        if ($result) {
            return $result;
        }
        else{ 
            return false;
        }

    }
*/
    public function save_student_lecture_attendance($faculty_id,$lecture_id,$e_no,$name,$sem,$classname,$subject,$status)
    {
        $date = date('y-m-d',time());
        $stmt = $this->conn->prepare("SELECT * FROM lecture_attendance_tb WHERE faculty_id='$faculty_id' AND lacture_id='$lecture_id' AND e_no='$e_no[0]' AND name='$name[0]' AND sem='$sem' AND class='$classname' AND subject='$subject' AND _date='$date'");
        /*$stmt->bind_param("iiisssss", $faculty_id,$lecture_id,$e_no[$i],$name[$i],$sem,$classname,$subject,$date);*/
        $stmt->execute();
        $result = $stmt->get_result()->fetch_assoc();
        $stmt -> close();
       
        if($result['faculty_id'] != $faculty_id)
        {
            $stmt = $this->conn->prepare("INSERT INTO lecture_attendance_tb(faculty_id,lacture_id,e_no,name,sem,class,subject,_date,status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            $date = date('y-m-d',time());
                for ($i=0; $i <count($name) ; $i++) { 
                $stmt->bind_param("sssssssss", $faculty_id,$lecture_id,$e_no[$i],$name[$i],$sem,$classname,$subject,$date,$status[$i]);
                $result = $stmt->execute();
                }
                
                $stmt -> close();
                if ($result) {
                    return $result;
                    }
                else{ 
                    return false;
                    }
        }
        else
        {
            return false;
        }
    }

public function save_student_lab_attendance($faculty_id,$lab_id,$e_no,$name,$sem,$batch,$subject,$status)
    {
     $date = date('y-m-d',time());
        $stmt = $this->conn->prepare("SELECT * FROM lab_attendance_tb WHERE faculty_id='$faculty_id' AND lab_id='$lab_id' AND e_no='$e_no[0]' AND name='$name[0]' AND sem='$sem' AND batch='$batch' AND subject='$subject' AND _date='$date'");
        $stmt->execute();
        $result = $stmt->get_result()->fetch_assoc();
        $stmt -> close();
       
        if($result['faculty_id'] != $faculty_id)
        {
            $stmt = $this->conn->prepare("INSERT INTO lab_attendance_tb(faculty_id,lab_id,e_no,name,sem,batch,subject,_date,status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            $date = date('y-m-d',time());
                for ($i=0; $i <count($name) ; $i++) { 
                $stmt->bind_param("sssssssss", $faculty_id,$lab_id,$e_no[$i],$name[$i],$sem,$batch,$subject,$date,$status[$i]);
                $result = $stmt->execute();
                }
        
            $stmt -> close();
                if ($result) {
                    return $result;
                }
                else{ 
                    return false;
                    }
        }
        else
        {
            return false;
        }   
    }
    /*public function save_student_lab_attendance($faculty_id,$lab_id,$e_no,$name,$sem,$batch,$subject,$status)
    {
        $stmt = $this->conn->prepare("INSERT INTO lab_attendance_tb(faculty_id,lab_id,e_no,name,sem,batch,subject,_date,status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $date = date('y-m-d',time());
        for ($i=0; $i <count($name) ; $i++) { 
        $stmt->bind_param("sssssssss", $faculty_id,$lab_id,$e_no[$i],$name[$i],$sem,$batch,$subject,$date,$status[$i]);
        $result = $stmt->execute();
        }
        
        $stmt -> close();
        if ($result) {
            return $result;
        }
        else{ 
            return false;
        }

    }
*/
    public function update_attendance_details($faculty_id,$lecture_id,$sem,$classname,$subject,$date) {
    //Fetch Students From that class,Sem,Subject
    // $daate=strtotime($date);
       $date_for_database = date ("Y-m-d", strtotime($date));
    $stmt = $this->conn->prepare("SELECT * FROM lecture_attendance_tb WHERE faculty_id='$faculty_id' AND lacture_id='$lecture_id' AND sem='$sem' AND class='$classname' AND subject='$subject' AND _date='$date_for_database'");
    $stmt->execute(); 
    $result = $stmt->get_result();
    $stmt -> close();                
        //check for success
    if ($result) {
        return $result;
    } else {     
        return false;            
    }
    }

    public function update_lab_attendance_details($faculty_id,$lab_id,$sem,$batch,$subject,$date) {
    //Fetch Students From that class,Sem,Subject
    // $daate=strtotime($date);
       $date_for_database = date ("Y-m-d", strtotime($date));
    $stmt = $this->conn->prepare("SELECT * FROM lab_attendance_tb WHERE faculty_id='$faculty_id' AND lab_id='$lab_id' AND sem='$sem' AND batch='$batch' AND subject='$subject' AND _date='$date_for_database'");
    $stmt->execute(); 
    $result = $stmt->get_result();
    $stmt -> close();                
        //check for success
    if ($result) {
        return $result;
    } else {     
        return false;            
    }
    }


    public function update_lecture_attendance($u_id,$status)
    {
        $stmt = $this->conn->prepare("UPDATE lecture_attendance_tb SET status=? WHERE id=? ");
        for ($i=0; $i <count($u_id) ; $i++) { 
        $stmt->bind_param("si",$status[$i],$u_id[$i]);
        $result = $stmt->execute();
        }
        
        $stmt -> close();
        if ($result) {
            return $result;
        }
        else{ 
            return false;
        }

    }

    public function update_lab_attendance($u_id,$status)
    {
        $stmt = $this->conn->prepare("UPDATE lab_attendance_tb SET status=? WHERE id=? ");
        for ($i=0; $i <count($u_id) ; $i++) { 
        $stmt->bind_param("si",$status[$i],$u_id[$i]);
        $result = $stmt->execute();
        }
        
        $stmt -> close();
        if ($result) {
            return $result;
        }
        else{ 
            return false;
        }

    }

    public function generate_fetch_lecture_marks($faculty_id,$lecture_id) {
    //Fetch Students From that class,Sem,Subject
    // $daate=strtotime($date);
    $stmt = $this->conn->prepare("SELECT * FROM lecture_attendance_tb WHERE faculty_id='$faculty_id' AND lacture_id='$lecture_id'");
    $stmt->execute(); 
    $result = $stmt->get_result();
    $stmt -> close();                
        //check for success
    if ($result) {
        return $result;
    } else {     
        return false;            
    }
    }

    public function generate_fetch_lab_marks($faculty_id,$lab_id) {
    //Fetch Students From that class,Sem,Subject
    // $daate=strtotime($date);
    $stmt = $this->conn->prepare("SELECT * FROM lab_attendance_tb WHERE faculty_id='$faculty_id' AND lab_id='$lab_id' ");
    $stmt->execute(); 
    $result = $stmt->get_result();
    $stmt -> close();                
        //check for success
    if ($result) {
        return $result;
    } else {     
        return false;            
    }
    }

//Assignment module
    public function assignment_details($faculty_id,$lecture_id,$sem,$classname,$subject)
    {
        $stmt = $this->conn->prepare("SELECT * FROM assignment_details_tb WHERE faculty_id=? AND lacture_id=? AND sem=? AND classname=? AND subject=?");
        $stmt->bind_param("sssss",$faculty_id,$lecture_id,$sem,$classname,$subject);
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }
    }

    /*public function assignment_add($faculty_id,$lacture_id,$sem,$classname,$subject,$assignment_name,$start_date,$due_date,$description)
    {

        $stmt = $this->conn->prepare("INSERT INTO assignment_details_tb(faculty_id,lacture_id,sem,classname,subject,assignment_name,start_date,due_date,description) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $_date = strtotime($start_date);
        $_start_date=date('Y-m-d',$_date);
        $_date = strtotime($due_date);
        $_due_date=date('Y-m-d',$_date);
        $stmt->bind_param("sssssssss",$faculty_id,$lacture_id,$sem,$classname,$subject,$assignment_name,$_start_date,$_due_date,$description);
                if ($stmt->execute()) {
                    $stmt -> close();
                    return true;
                }
                else{ 
                    return false;
                    }
    }*/   
    public function assignment_add($faculty_id,$lacture_id,$sem,$classname,$subject,$assignment_name,$start_date,$due_date,$description)
    {
        $stmt = $this->conn->prepare("INSERT INTO assignment_details_tb(faculty_id,lacture_id,sem,classname,subject,assignment_name,start_date,due_date,description) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $_date = strtotime($start_date);
        $_start_date=date('Y-m-d',$_date);
        $_date = strtotime($due_date);
        $_due_date=date('Y-m-d',$_date);
        $stmt->bind_param("sssssssss",$faculty_id,$lacture_id,$sem,$classname,$subject,$assignment_name,$_start_date,$_due_date,$description);
        $result=$stmt->execute();
        $stmt -> close();
                if ($result) 
                {
                    $stmtn = $this->conn->prepare("SELECT * FROM assignment_details_tb WHERE faculty_id=? AND lacture_id=? AND sem=? AND classname=? AND subject=? AND assignment_name=? AND start_date=? AND due_date=? AND description=?");
                    $stmtn->bind_param("sssssssss",$faculty_id,$lacture_id,$sem,$classname,$subject,$assignment_name,$_start_date,$_due_date,$description);
                    $assignment_id=0;
                    if ($stmtn->execute()) {
                        $user = $stmtn->get_result()->fetch_assoc();
                        $stmtn->close();
                         $assignment_id=$user['assignment_id'] ;  
                    }
                    if ($result) 
                    {
                                $stmt = $this->conn->prepare("SELECT * FROM student_details_tb WHERE sem='$sem' AND class='$classname'");
                                $stmt->execute();
                                $result2 = $stmt->get_result();
                                $stmt -> close(); 
                                if ($result2) 
                                {
                                    $results1=$result2;
                                    $date = date('y-m-d',time());
                                    $stmt2 = $this->conn->prepare("INSERT INTO assignment_marks_tb(faculty_id,assignment_id,assignment_name,lecture_id,sem,classname,subject,name,e_no,updated_date) VALUES(?,?, ?, ?, ?, ?, ?, ?, ?, ?)");
                                    $results = 0;
                                    while($rows = mysqli_fetch_array($results1)){
                                        $stmt2->bind_param("ssssssssss",$faculty_id,$assignment_id,$assignment_name,$lacture_id,$sem,$classname,$subject,$rows[2],$rows[1],$date);
                                       
                                        if ($stmt2->execute()) {
                                            $results =1;
                                        }                                       
                                    }
                                    $stmt2 -> close();
                                    
                                } 
                                
                    }            
                    return true;   
                }
                else
                { 
                    return false;
                }
    
    }


    public function assignment_fetch_marks($assignment_id)
    {
        $stmt = $this->conn->prepare("SELECT * FROM assignment_marks_tb WHERE assignment_id='$assignment_id'");
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }

    }

    public function assignment_update_marks($assignment_id,$name,$e_no,$marks)
    {   $date = date('y-m-d',time());
        $stmt = $this->conn->prepare("UPDATE assignment_marks_tb SET marks=? , updated_date=? WHERE assignment_id=? AND e_no=? AND name=?");
        for ($i=0; $i <count($e_no) ; $i++) { 
        $stmt->bind_param("sssss",$marks[$i],$date,$assignment_id,$e_no[$i],$name[$i]);
        $result = $stmt->execute();
        }
        
        $stmt -> close();
        if ($result) {
            return $result;
        }
        else{ 
            return false;
        }
    }

    public function generate_fetch_assignment_marks($faculty_id,$lecture_id)
    {
        $stmt = $this->conn->prepare("SELECT * FROM assignment_marks_tb WHERE faculty_id='$faculty_id' AND lecture_id='$lecture_id'");
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }

    }

//Quiz Module

    public function quiz_details($faculty_id,$lecture_id,$sem,$classname,$subject)
    {
        $stmt = $this->conn->prepare("SELECT * FROM quiz_details_tb WHERE faculty_id=? AND lacture_id=? AND sem=? AND classname=? AND subject=?");
        $stmt->bind_param("sssss",$faculty_id,$lecture_id,$sem,$classname,$subject);
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }
    }


    public function quiz_add($faculty_id,$lacture_id,$sem,$classname,$subject,$quiz_name,$quiz_date,$description)
    {
        $stmt = $this->conn->prepare("INSERT INTO quiz_details_tb(faculty_id,lacture_id,sem,classname,subject,quiz_name,quiz_date,description) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
        $_date = strtotime($quiz_date);
        $_quiz_date=date('Y-m-d',$_date);
        $stmt->bind_param("ssssssss",$faculty_id,$lacture_id,$sem,$classname,$subject,$quiz_name,$_quiz_date,$description);
        $result=$stmt->execute();
        $stmt -> close();
                if ($result) 
                {
                    $stmtn = $this->conn->prepare("SELECT * FROM quiz_details_tb WHERE faculty_id=? AND lacture_id=? AND sem=? AND classname=? AND subject=? AND quiz_name=? AND quiz_date=?  AND description=?");
                    $stmtn->bind_param("ssssssss",$faculty_id,$lacture_id,$sem,$classname,$subject,$quiz_name,$_quiz_date,$description);
                    $quiz_id=0;
                    if ($stmtn->execute()) {
                        $user = $stmtn->get_result()->fetch_assoc();
                        $stmtn->close();
                         $quiz_id=$user['quiz_id'] ;  
                    }
                    if ($result) 
                    {
                                $stmt = $this->conn->prepare("SELECT * FROM student_details_tb WHERE sem='$sem' AND class='$classname'");
                                $stmt->execute();
                                $result2 = $stmt->get_result();
                                $stmt -> close(); 
                                if ($result2) 
                                {
                                    $results1=$result2;
                                    $date = date('y-m-d',time());
                                    $stmt2 = $this->conn->prepare("INSERT INTO quiz_marks_tb(faculty_id,quiz_id,quiz_name,lecture_id,sem,classname,subject,name,e_no,updated_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
                                    $results = 0;
                                    while($rows = mysqli_fetch_array($results1)){
                                        $stmt2->bind_param("ssssssssss",$faculty_id,$quiz_id,$quiz_name,$lacture_id,$sem,$classname,$subject,$rows[2],$rows[1],$date);
                                        
                                        if ($stmt2->execute()) {
                                            $results =1;
                                        }                                       
                                    }
                                    $stmt2 -> close();
                                    
                                } 
                                
                    } 
                    return true;
                }
                else
                { 
                    return false;
                }
    }


    public function quiz_fetch_marks($quiz_id)
    {
        $stmt = $this->conn->prepare("SELECT * FROM quiz_marks_tb WHERE quiz_id='$quiz_id'");
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }

    }

    public function quiz_update_marks($quiz_id,$name,$e_no,$marks)
    {   $date = date('y-m-d',time());
        $stmt = $this->conn->prepare("UPDATE quiz_marks_tb SET marks=? , updated_date=? WHERE quiz_id=? AND e_no=? AND name=?");
        for ($i=0; $i <count($e_no) ; $i++) { 
        $stmt->bind_param("sssss",$marks[$i],$date,$quiz_id,$e_no[$i],$name[$i]);
        $result = $stmt->execute();
        }
        
        $stmt -> close();
        if ($result) {
            return $result;
        }
        else{ 
            return false;
        }
    }

    public function generate_fetch_quiz_marks($faculty_id,$lecture_id)
    {
        $stmt = $this->conn->prepare("SELECT * FROM quiz_marks_tb WHERE faculty_id='$faculty_id' AND lecture_id='$lecture_id'");
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }

    }




//Practicals Module

    public function practical_details($faculty_id,$lab_id,$sem,$batch,$subject)
    {
        $stmt = $this->conn->prepare("SELECT * FROM practical_details_tb WHERE faculty_id=? AND lab_id=? AND sem=? AND batch=? AND subject=?");
        $stmt->bind_param("sssss",$faculty_id,$lab_id,$sem,$batch,$subject);
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }
    }


    public function practical_add($faculty_id,$lab_id,$sem,$batch,$subject,$practical_name,$practical_date,$description)
    {
        $stmt = $this->conn->prepare("INSERT INTO practical_details_tb(faculty_id,lab_id,sem,batch,subject,practical_name,practical_date,description) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
        $_date = strtotime($practical_date);
        $_practical_date=date('Y-m-d',$_date);
        $stmt->bind_param("ssssssss",$faculty_id,$lab_id,$sem,$batch,$subject,$practical_name,$_practical_date,$description);
        $result=$stmt->execute();
        $stmt -> close();
                if ($result) 
                {
                    $stmtn = $this->conn->prepare("SELECT * FROM practical_details_tb WHERE faculty_id=? AND lab_id=? AND sem=? AND batch=? AND subject=? AND practical_name=? AND practical_date=?  AND description=?");
                    $stmtn->bind_param("ssssssss",$faculty_id,$lab_id,$sem,$batch,$subject,$practical_name,$_practical_date,$description);
                    $practical_id=0;
                    if ($stmtn->execute()) {
                        $user = $stmtn->get_result()->fetch_assoc();
                        $stmtn->close();
                         $practical_id=$user['practical_id'] ;  
                    }
                    if ($result) 
                    {

                        $stmt = $this->conn->prepare("SELECT * FROM student_details_tb WHERE sem='$sem' AND batch='$batch'");
                        $stmt->execute();
                        $result2 = $stmt->get_result();
                        $stmt -> close(); 
                        if ($result2) 
                        {
                            $results1=$result2;
                            $date = date('y-m-d',time());
                            $stmt2 = $this->conn->prepare("INSERT INTO practical_marks_tb(faculty_id,practical_id,practical_name,lab_id,sem,batch,subject,name,e_no,updated_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
                            $results = 0;
                            while($rows = mysqli_fetch_array($results1)){
                                $stmt2->bind_param("ssssssssss",$faculty_id,$practical_id,$practical_name,$lab_id,$sem,$batch,$subject,$rows[2],$rows[1],$date);
                                
                                if ($stmt2->execute()) {
                                    $results =1;
                                }                                       
                            }
                            $stmt2 -> close();
                            /*if ($results) {
                                return $results;
                            }*/
                            
                        } 
                        
                    }
                    
                return true;              
                }
                else
                { 
                    return false;
                }
    }


    public function practical_fetch_marks($practical_id)
    {
        $stmt = $this->conn->prepare("SELECT * FROM practical_marks_tb WHERE practical_id='$practical_id'");
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }

    }

    public function practical_update_marks($practical_id,$name,$e_no,$marks)
    {   $date = date('y-m-d',time());
        $stmt = $this->conn->prepare("UPDATE practical_marks_tb SET marks=? , updated_date=? WHERE practical_id=? AND e_no=? AND name=?");
        for ($i=0; $i <count($e_no) ; $i++) { 
        $stmt->bind_param("sssss",$marks[$i],$date,$practical_id,$e_no[$i],$name[$i]);
        $result = $stmt->execute();
        }
        
        $stmt -> close();
        if ($result) {
            return $result;
        }
        else{ 
            return false;
        }
    }

    public function generate_fetch_practical_marks($faculty_id,$lab_id)
    {
        $stmt = $this->conn->prepare("SELECT * FROM practical_marks_tb WHERE faculty_id='$faculty_id' AND lab_id='$lab_id'");
        $stmt->execute();
        $result= $stmt->get_result();
        $stmt->close();
        if($result)
        {
            return $result;
        }
        else
        {
            return false;
        }

    }

}
?>  