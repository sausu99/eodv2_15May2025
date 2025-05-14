<?php 
include('includes/header.php');
include("includes/session_check.php");
include('includes/function.php');
include('language/language.php'); 

$queryPermit = "SELECT permission_coin FROM tbl_vendor WHERE id = " . $_SESSION['seller_id'] . "";
$resultPermit = mysqli_query($mysqli, $queryPermit);
$rowPermit = mysqli_fetch_assoc($resultPermit);
$permission = $rowPermit['permission_coin'];

if($permission != 1) 
{
    $_SESSION['msg'] = "access_denied";
    header("Location:home.php");
	exit;
}

$id = PROFILE_ID;

 	  if(isset($_SESSION['seller_id']))
   {
        
     $qry="select * from tbl_vendor where id='".sanitize($_SESSION['seller_id'])."'";
      
     $result=mysqli_query($mysqli,$qry);
     $row=mysqli_fetch_assoc($result);
 
   }
	
	if(isset($_GET['id']))
	{
			 
			$user_qry="SELECT * FROM tbl_users where id='".sanitize($_GET['id'])."'";
			$user_result=mysqli_query($mysqli,$user_qry);
			$user_row=mysqli_fetch_assoc($user_result);
		
	}
	
            if(isset($_POST['submit']) and isset($_POST['id']))
            {
                if ($_POST['topup'] == 0 || !is_numeric($_POST['topup'])) {
                    $_SESSION['msg'] = "19";
                    header("Location: topup_wallet.php?id=" . $_POST['id']);
                    exit;
                }
                // Calculate new wallet value for the user
                $data = array(
                    'wallet' => $user_row['wallet'] + $_POST['topup'],
                );
                
                // Update the user's wallet
                $user_edit = Update('tbl_users', $data, "WHERE id = '".$_POST['id']."'");
            
                if ($user_edit > 0) {
                    // Calculate new balance for the seller
                    $data = array(
                        'balance' => $row['balance'] - $_POST['topup'],
                    );
            
                    // Update the seller's balance
                    $seller_edit = Update('tbl_vendor', $data, "WHERE id = '".sanitize($_SESSION['seller_id'])."'");
            
                    if ($seller_edit > 0) {
                        $_SESSION['msg'] = "17";
                        header("Location:topup_wallet.php?id=".$_POST['id']);
                        exit;
                    }
                } 	
            }
            
$querytime = "SELECT demo_access FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
$demo_access = $rowtime['demo_access'];		
	 
	
	
	
?>
 	

 <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php if(isset($_GET['id'])){?><?php }else{?>Add<?php }?> Coins into User Wallet</div>
            </div>
          </div>
          <div class="clearfix"></div>
          <div class="row mrg-top">
            <div class="col-md-12">
               
              <div class="col-md-12 col-sm-12">
                <?php if(isset($_SESSION['msg'])){?> 
               	 <div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                	<?php echo $client_lang[$_SESSION['msg']] ; ?></a> </div>
                <?php unset($_SESSION['msg']);}?>	
              </div>
            </div>
          </div>
          <div class="card-body mrg_bottom"> 
            <form action="" name="addedituser" method="post" class="form form-horizontal" enctype="multipart/form-data" >
            	<input  type="hidden" name="id" value="<?php echo $_GET['id'];?>" />

              <div class="section">
                <div class="section-body">
                    
                    <div class="form-group">
                    <label class="col-md-3 control-label"><h4><u>User Details</u></h4></label>
                  </div>
                  
                    <div class="form-group">
                    <label class="col-md-3 control-label">Name :- <?php if(isset($_GET['id'])){echo $user_row['name'];}?></label>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label">Email :- <?php if ($demo_access == 1) { ?>Hiden@Demo<?php } else { ?><?php if(isset($_GET['id'])){echo $user_row['email'];}?><?php } ?></label>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label">Phone :- <?php if ($demo_access == 1) { ?>Hiden@Demo<?php } else { ?><?php if(isset($_GET['id'])){echo '+'.$user_row['country_code'].' '.$user_row['phone'];}?><?php } ?></label>
                  </div>

                 <div class="form-group">
                    <label class="col-md-3 control-label">Current Balance :- <?php if(isset($_GET['id'])){echo $user_row['wallet'].' Coins';}?></label>
                    <div class="col-md-6">
                    </div>
                  </div>
                  <hr>
<div class="form-group">
    <label class="col-md-3 control-label">Enter Coins to Add:-</label>
    <div class="col-md-6">
        <input type="text" name="topup" id="topup" placeholder="Number of coins you want to add" class="form-control" required>
        <p id="error-message" style="color: red; display: none;"></p>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
    $('#topup').on('input', function() {
        var max = <?php echo $row['balance']; ?>;
        var value = $(this).val();
        if (value < 0) {
            $(this).val('');
            $('#error-message').text('Negative values are not allowed').show();
        } else if (value > max) {
            $(this).val(max);
            $('#error-message').text('You can add upto <?php if(isset($_GET['id'])){echo $row['balance'].' Coins';}?> to add more coins purchase it from admin').show();
        } else {
            $('#error-message').hide();
        }
    });
});
</script>


                  
                  
                 
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="submit" class="btn btn-primary">Add Coins</button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
   

<?php include('includes/footer.php');?>                  