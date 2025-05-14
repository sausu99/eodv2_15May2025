<?php 
include('includes/header.php');
include("includes/session_check.php");
include('includes/function.php');
include('language/language.php');

// Fetch the timezone from tbl_settings
$timezone_query = "SELECT timezone FROM tbl_settings";
$timezone_result = mysqli_query($mysqli, $timezone_query);
$timezone_row = mysqli_fetch_assoc($timezone_result);
$timezone = $timezone_row['timezone'];
date_default_timezone_set($timezone);
$currentDateTime = date('Y-m-d H:i:s');

if(isset($_SESSION['user_id'])) {

    $user_id = isset($_SESSION['user_id']) ? (int)$_SESSION['user_id'] : 0;
    $qry = "SELECT * FROM tbl_users WHERE id='".$user_id."'";
    $result = mysqli_query($mysqli, $qry);
    $row = mysqli_fetch_assoc($result);
}

if(isset($_GET['plan_id'])) {
    
    $plan_id = isset($_GET['plan_id']) ? (int)$_GET['plan_id'] : 0;
    $user_qry = "SELECT * FROM tbl_hyip WHERE plan_id='".$plan_id."'";
    $user_result = mysqli_query($mysqli, $user_qry);
    $user_row = mysqli_fetch_assoc($user_result);
}

if (isset($_POST['submit']) && isset($user_row['plan_id'])) {
    // Begin transaction
    mysqli_begin_transaction($mysqli);
    
    $topup = sanitize($_POST['topup']);
    $plan_id = $user_row['plan_id'];

    // Validate input
    if ($topup == 0 || !is_numeric($topup)) {
        $_SESSION['msg'] = "24";
        //  <!--removed .php-->
        header("Location: invest?plan_id=" . $user_row['plan_id']);
        exit;
    }
    
    // Check range
    if ($topup < $user_row['plan_minimum'] ||($topup) > $user_row['plan_maximum']) {
        $_SESSION['msg'] = "28";
        //  <!--removed .php-->
        header("Location: invest?plan_id=" . $user_row['plan_id']);
        exit;
    }
    
    $user_id = sanitize($_SESSION['user_id']);

    // Fetch user's wallet balance
    $qryBalance = "SELECT * FROM tbl_users WHERE id='".$user_id."' FOR UPDATE";
    $resultBalance = mysqli_query($mysqli, $qryBalance);
    $rowBalance = mysqli_fetch_assoc($resultBalance);

    // Check if user's wallet balance is sufficient
    if ($rowBalance['wallet'] < $topup) {
        $_SESSION['msg'] = "27";
          //<!--removed .php-->
        header("Location: invest?plan_id=" . $user_row['plan_id']);
        exit;
    }

    // Calculate new wallet value for the user
    $new_wallet = $rowBalance['wallet'] - $topup;

    // Update the user's wallet
    $data = array('wallet' => $new_wallet);
    $user_edit = Update('tbl_users', $data, "WHERE id = '".$user_id."'");

    if ($user_edit > 0) {
        // Calculate the next interest update time
        // Convert the current date and time to a Unix timestamp
        $currentTimestamp = strtotime($currentDateTime);
        
        // Extract hours, minutes, and seconds from plan_repeat_time
        list($hours, $minutes, $seconds) = explode(':', $user_row['plan_repeat_time']);
        
        // Convert plan_repeat_time to total seconds
        $repeatIntervalInSeconds = ($hours * 3600) + ($minutes * 60) + $seconds;
        
        // Calculate the next interest update timestamp
        $nextInterestUpdateTimestamp = $currentTimestamp + $repeatIntervalInSeconds;
        
        // Convert the timestamp to the desired format
        $nextInterestUpdate = date('Y-m-d H:i:s', $nextInterestUpdateTimestamp);
        
        if ($user_row['plan_interest_type'] == 1) {
                $interestType= $user_row['plan_interest'].'%';
            } else {
                $interestType= $user_row['plan_interest'];
            }


        // Insert data into tbl_hyip_order
        $order_data = array(
            'user_id' => sanitize($_SESSION['user_id']),
            'plan_id' => sanitize($user_row['plan_id']),
            'investment_amount' => sanitize($_POST['topup']),
            'current_value' => sanitize($_POST['topup']),
            'last_interest_update' => $currentDateTime,
            'next_interest_update' => $nextInterestUpdate,
            'order_date' => $currentDateTime,
            'interest' => $interestType,
            'status' => 1
        );
        $insert_order = Insert('tbl_hyip_order', $order_data);
        
        $qry1 = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`, `date`, `money`) 
                         VALUES ('$user', 10, '10', 'Now()', '$investment')";
        $result_insert = mysqli_query($mysqli, $qry1);
        
        // Insert transaction
        $transaction_data = array(
            'user_id' => sanitize($_SESSION['user_id']),
            'type' => 10,
            'type_no' => 10,
            'money' => sanitize($_POST['topup']),
            'date' => $currentDateTime,
        );
        $insert_transaction = Insert('tbl_transaction', $transaction_data);

        // Commit transaction
        mysqli_commit($mysqli);
        $_SESSION['msg'] = "25";
          //<!--removed .php-->
        header("Location: investments");
        exit;
        
    } else {
        // Rollback transaction
        mysqli_rollback($mysqli);
        $_SESSION['msg'] = "26";
          //<!--removed .php-->
        header("Location: invest?plan_id=" . $plan_id);
        exit;
    }
}

?>

<title><?php echo $user_row['plan_name']; ?> - <?php echo $invest_lang['confirmInvestment']; ?></title>
<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $invest_lang['confirmInvestment']; ?></div>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])){?> 
                        <div class="alert alert-success alert-dismissible" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                            <?php echo $client_lang[$_SESSION['msg']]; ?>
                        </div>
                        <?php unset($_SESSION['msg']);}?>
                    </div>
                </div>
            </div>
            <div class="card-body mrg_bottom"> 
                <form action="" name="addedituser" method="post" class="form form-horizontal" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="<?php echo $_GET['plan_id']; ?>" />

                    <div class="section">
                        <div class="section-body">
                            <div class="form-group">
                                <label class="col-md-3 control-label"><strong><?php echo $invest_lang['plan']; ?>:</strong> <?php if(isset($_GET['plan_id'])){echo $user_row['plan_name'];}?></label>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label"><strong><?php echo $invest_lang['return']; ?>:</strong> <?php if ($user_row['plan_interest_type'] == 1) {echo $user_row['plan_interest'].'%';} else {echo $user_row['plan_interest'].' Coins';} echo $user_row['plan_repeat_text'];?></label>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label"><strong><?php echo $invest_lang['investment']; ?>:</strong> 
                                    <?php 
                                        if (isset($_GET['plan_id'])) {
                                            if ($user_row['plan_minimum'] == $user_row['plan_maximum']) {
                                                echo $user_row['plan_minimum'] .' '.  $invest_lang['coins'];
                                            } else {
                                                echo $user_row['plan_minimum'] . ' - ' . $user_row['plan_maximum'] .' '. $invest_lang['coins'];
                                            }
                                        }
                                    ?>
                                </label>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label"><strong><?php echo $invest_lang['balance']; ?>:</strong> <?php if(isset($_SESSION['user_id'])){echo $row['wallet'].' '.$invest_lang['coins'];}?></label>
                                <div class="col-md-6"></div>
                            </div>
                            <hr>
                           <div class="form-group">
                               <label class="col-md-3 control-label"><?php echo $invest_lang['investmentAmount']; ?>:-</label>
                               <div class="col-md-6">
                                   <div class="input-group">
                                       <input type="text" name="topup" id="topup" placeholder="<?php echo ($user_row['plan_minimum'] == $user_row['plan_maximum']) ? $user_row['plan_minimum'] : $user_row['plan_minimum'] . ' - ' . $user_row['plan_maximum']; ?>" class="form-control" required>
                                       <span class="input-group-addon"><?php echo $invest_lang['coins']; ?></span>
                                   </div>
                                   <p id="error-message" style="color: red; display: none;"></p>
                               </div>
                           </div>

                            <div class="form-group">
                                <div class="col-md-9 col-md-offset-3">
                                    <button type="submit" name="submit" class="btn btn-primary"><?php echo $invest_lang['invest']; ?></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<?php include('includes/footer.php'); ?>
