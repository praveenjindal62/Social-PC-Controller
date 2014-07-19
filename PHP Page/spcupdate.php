<?php

$email=$_POST['email'];
$token=$_POST['tokenstring'];
$fname=$_POST['fname'];
$lname=$_POST['lname'];
$gender=$_POST['gender'];
$con = mysqli_connect("HOST NAME","USERNAME","PASSWORD","DATABASE NAME");

if($token=="" || $token==null)
{
header('Location: fblogin.php');
}

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
$query1="Select * from fb_user_tb where email='{$email}'";
$query2="insert into fb_user_tb (email,fname,lname,gender,token) values ('{$email}','{$fname}','{$lname}','{$gender}','{$token}')";
$query3="update fb_user_tb set token='{$token}' where email='{$email}'";
$finalResult="Nothing";
echo $query1."<br/>";
echo $query2."<br/>";
echo $query3."<br/>";
$result=mysqli_query($con,$query1);
$row_cnt = mysqli_num_rows($result);
if($row_cnt==1)
{
$result=mysqli_query($con,$query3);
$finalResult="Updated";
}
else if($row_cnt==0)
{
$result=mysqli_query($con,$query2);
$finalResult="Added";
}
echo $finalResult;
// Check connection

?>