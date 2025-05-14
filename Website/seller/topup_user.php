<?php include('includes/header.php'); 
include("includes/session_check.php");

include('includes/function.php');
include('language/language.php');  

$queryPermit = "SELECT permission_coin, balance FROM tbl_vendor WHERE id = " . $_SESSION['seller_id'] . "";
$resultPermit = mysqli_query($mysqli, $queryPermit);
$rowPermit = mysqli_fetch_assoc($resultPermit);
$permission = $rowPermit['permission_coin'];
$balance = $rowPermit['balance'];

if($permission != 1) 
{
    $_SESSION['msg'] = "access_denied";
    header("Location:home.php");
	exit;
}

$id = PROFILE_ID;


if(isset($_POST['user_search']))
{
    $user_qry="SELECT * FROM tbl_users WHERE tbl_users.email like '%".addslashes($_POST['search_value'])."%' ORDER BY tbl_users.id DESC";  
    $users_result=mysqli_query($mysqli,$user_qry);
}

$querytime = "SELECT demo_access,admin_email FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
$demo_access = $rowtime['demo_access'];
$email = 'mailto:'.$rowtime['admin_email'];
?>
<style>
.search_block {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 20px; /* Adjust as needed */
}

.input-group {
    max-width: 400px; /* Adjust the maximum width as needed */
}

.input-group input {
    border-radius: 5px; /* Rounded corners for the input */
}

.input-group-btn .btn {
    border-radius: 5px; /* Rounded corners for the button */
}

</style>
<div class="row">
    <div class="col-xs-12">
        <div class="card mrg_bottom">
            <div class="page_title_block text-center">
                <h2 class="page_title">Topup User Wallet</h2>
                <p class="description">Your Balance: <?php echo $balance.' Coins!'; ?>. If you want to add more coins into the user account, <a href="<?php echo $email ?>">Contact Admin</a>.</p>
            </div>
            <div class="clearfix"></div>
            <div class="search_list text-center">
                <div class="search_block">
                    <form method="post" action="">
                        <div class="input-group">
                            <input class="form-control input-lg" placeholder="Enter user's email id" aria-controls="DataTables_Table_0" type="search" name="search_value" required>
                            <span class="input-group-btn">
                                <button type="submit" name="user_search" class="btn btn-primary"><i class="fa fa-search"></i>&nbsp;Search</button>
                            </span>
                        </div>
                    </form>  
                </div>
            </div>
            <div class="clearfix"></div>
            <?php if(isset($_POST['user_search'])): ?>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])){?> 
                        <div class="alert alert-success alert-dismissible" role="alert"> 
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                            <?php echo $client_lang[$_SESSION['msg']] ; ?></a> 
                        </div>
                        <?php unset($_SESSION['msg']);}?> 
                    </div>
                </div>
            </div>
            <div class="col-md-12 mrg-top">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>User Name</th>                         
                            <th>Mobile Number</th>
                            <th>Email ID</th>
                            <th>Balance</th>
                            <th class="cat_action_list">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        if(mysqli_num_rows($users_result) > 0){
                            while($users_row=mysqli_fetch_array($users_result))
                            {
                        ?>
                        <tr>
                            <td><?php echo $users_row['name'];?></td>
                            <td><?php if ($demo_access == 1) { ?>Hidden@Demo<?php } else { ?><?php echo '+'.$users_row['country_code'].$users_row['phone'];?><?php } ?> </td>
                            <td><?php if ($demo_access == 1) { ?>Hidden@Demo<?php } else { ?><?php echo $users_row['email'];?><?php } ?></td>
                            <td><?php echo $users_row['wallet'].' Coins';?></td> 
                            <td><a href="topup_wallet.php?id=<?php echo $users_row['id'];?>" class="btn btn-primary">Add Coins</a></td>
                        </tr>
                        <?php
                            }
                        } else {
                        ?>
                        <tr>
                            <td colspan="5">No users found with that email address.</td>
                        </tr>
                        <?php } ?>
                    </tbody>
                </table>
            </div>
            <?php endif; ?>
            <div class="clearfix"></div>
        </div>
    </div>
</div>

<?php include('includes/footer.php');?>  
