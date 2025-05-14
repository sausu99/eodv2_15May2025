<?php
    include("includes/connection.php");
		include("language/language.php");
    

	if(isset($_SESSION['vendor_name']))
	{
		header("Location:home.php");
		exit;
	}
?>
<!DOCTYPE html>
<html>
<head>
<meta name="author" content="">
<meta name="description" content="">
<meta http-equiv="Content-Type"content="text/html;charset=UTF-8"/>
<meta name="viewport"content="width=device-width, initial-scale=1.0">
<title><?php echo APP_NAME;?></title>
<link rel="stylesheet" type="text/css" href="assets/css/vendor.css">
<link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">

<!-- Theme -->
<link rel="stylesheet" type="text/css" href="assets/css/theme/blue-sky.css">
<link rel="stylesheet" type="text/css" href="assets/css/theme/blue.css">
<link rel="stylesheet" type="text/css" href="assets/css/theme/red.css">
<link rel="stylesheet" type="text/css" href="assets/css/theme/yellow.css">

<script src="assets/js/login.js" type="text/javascript"></script>

<style>
  body {
    background-color: #e4e9f7; /* Path to your background color */
    background-size: cover; /* Adjust the background size to cover the entire screen */
    background-repeat: no-repeat; /* Prevent image repetition */
  }
  
  #email {
    color: #000;
}
 #password {
    color: #000;
}
.btn{
    height: 35px;
    background: rgba(76,68,182,0.808);
    border: 0;
    border-radius: 5px;
    color: #fff;
    font-size: 15px;
    cursor: pointer;
    transition: all .3s;
    margin-top: 10px;
    padding: 0px 10px;
}

.btn:hover { 
  background-color: #2a2a72;  /* Darker blue on hover */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);  /* Subtle shadow effect */
  color: #fff;  /* Change text color to white on hover */

}
.btn-register { /* Replace with your actual button's class */
    height: 35px;
    color: #000;
    border: 0;
    border-radius: 5px;
    background: #fff;
    font-size: 15px;
    cursor: pointer;
    transition: all .3s;
    margin-top: 10px;
    padding: 0px 10px;
} 

.btn-register:hover { 
  background: #fff;  /* Darker blue on hover */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);  /* Subtle shadow effect */
  color: #000;  /* Change text color to white on hover */

}
.box{
    background: #fdfdfd;
    display: flex;
    flex-direction: column;
    padding: 25px 25px;
    box-shadow: 0 0 128px 0 rgba(0,0,0,0.1),
                0 32px 64px -48px rgba(0,0,0,0.5);
}
</style>

</head>
<body>
            <?php               $querytime = "SELECT demo_access FROM tbl_settings";
                                  $resulttime = mysqli_query($mysqli, $querytime);
                                  $rowtime = mysqli_fetch_assoc($resulttime);
                                  $demo_access = $rowtime['demo_access'];
                                  
                                  if ($demo_access == 1) 
                                  {
	                              $queryCred = "SELECT * FROM tbl_vendor WHERE id=2";
                                  $resultCred = mysqli_query($mysqli, $queryCred);
                                  $rowCred = mysqli_fetch_assoc($resultCred);
                                  $emailVendor = $rowCred['username'];
                                  $passwordVendor = $rowCred['password'];
	                              }
              ?>
<div class="app app-default">
  <div class="app-container app-login">
    <div class="flex-center">
      <div class="app-body">
        <div class="app-block">
          <div class="app-form login-form">
            <div class="form-header">
              <div class="app-brand"><img src="../images/<?php echo APP_LOGO;?>" height="100px" width="100px"/></div>
            </div>
            <div class="form-header">
              <div class="app-brand"><?php if ($demo_access == 1) { ?>Merchant Panel (Demo)<?php } else { ?>Merchant Panel<?php } ?></div>
            </div>
			<div class="login_title_lineitem">
	
				  <div class="flipInX-1 blind icon">
					 <span class="icon">
				   </span>
				   </div>
			
			</div>
			<div class="clearfix"></div>
            <form action="login_db.php" method="post">
			  <div class="input-group" style="border:0px;">
                <?php if(isset($_SESSION['msg'])){?>
                <div class="alert alert-danger  alert-dismissible" role="alert"> <?php echo $client_lang[$_SESSION['msg']]; ?> </div>
                <?php unset($_SESSION['msg']);}?>
              </div>
              <div class="input-group"> <span class="input-group-addon" id="basic-addon1"> <i class="fa fa-user" aria-hidden="true"></i></span>
                <input type="text" name="username" id="username" class="form-control" placeholder="email id" <?php if ($demo_access == 1) { ?>value="<?php echo $emailVendor ?>"<?php } else { ?>value=""<?php } ?> aria-describedby="basic-addon1">
              </div>
              <div class="input-group"> <span class="input-group-addon" id="basic-addon2"> <i class="fa fa-lock" aria-hidden="true"></i></span>
                <input type="password" name="password" id="password" class="form-control" placeholder="password" <?php if ($demo_access == 1) { ?>value="<?php echo $passwordVendor ?>"<?php } else { ?>value=""<?php } ?> aria-describedby="basic-addon2">
              </div>
              <div class="text-center">
                <input type="submit" class="btn btn-success" value="Login">
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


</body>
</html>