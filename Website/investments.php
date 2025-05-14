<?php 
include('includes/header.php'); 
include('includes/function.php');
include('language/language.php'); 
	
	
	$user_id = isset($_SESSION['user_id']) ? intval($_SESSION['user_id']) : 0;




							$tableName="tbl_hyip_order";		
							//  <!--removed .php-->
							$targetpage = "investments"; 	
							$limit = 15; 
				
		                    $query = "SELECT COUNT(*) as num FROM $tableName left join tbl_hyip on tbl_hyip.plan_id = tbl_hyip_order.plan_id
		                              WHERE tbl_hyip_order.user_id = $user_id";
		                
							$total_pages = mysqli_fetch_array(mysqli_query($mysqli,$query));
							$total_pages = $total_pages['num'];
							
							$stages = 3;
							$page=0;
							if(isset($_GET['page'])){
							$page = mysqli_real_escape_string($mysqli,$_GET['page']);
							}
							if($page){
								$start = ($page - 1) * $limit; 
							}else{
								$start = 0;	
								}	
							

                            $users_qry = "SELECT * FROM tbl_hyip_order
                                          left join tbl_hyip on tbl_hyip.plan_id = tbl_hyip_order.plan_id
		                                  WHERE tbl_hyip_order.user_id = $user_id
                                          ORDER BY tbl_hyip_order.order_id DESC LIMIT $start, $limit";  
                        
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
							if (mysqli_num_rows($users_result) == 0) {
                                $title = $client_lang['no_investment'];
                                $description = $client_lang['no_investment_description'];
                                $image = 'no_investment.gif';
                                include("nodata.php");
                                exit;
                            }
							
							
	                        if(isset($_GET['cancel_plan']))
	                        {
	                            $user_id = sanitize($_SESSION['user_id']);
	                            $plan_id = sanitize($_GET['cancel_plan']);
	                            
	                            $planDetailsQuery = "SELECT * FROM tbl_hyip_order left join tbl_hyip on tbl_hyip.plan_id = tbl_hyip_order.plan_id WHERE order_id = $plan_id AND user_id= $user_id AND status= 1";
                                $planDetailsResult = mysqli_query($mysqli, $planDetailsQuery);
                                $planDetailsRow = mysqli_fetch_assoc($planDetailsResult);
                                $currentValue = $planDetailsRow['current_value'];
                                $isCancelable = $planDetailsRow['plan_cancelable'];
                                
                                if (mysqli_num_rows($planDetailsResult) > 0) 
                                {
	                            
	                            $checkBalanceQuery = "SELECT wallet FROM tbl_users WHERE id = $user_id";
                                $checkBalanceResult = mysqli_query($mysqli, $checkBalanceQuery);
                                $checkBalanceRow = mysqli_fetch_assoc($checkBalanceResult);
                                $currentBalance = $checkBalanceRow['wallet'];
                                
                                $newwallet = $currentBalance + $currentValue;   
                                $user_edit = "UPDATE tbl_users SET wallet='".$newwallet."' WHERE id = '".$user_id."'"; 
                                $result1 = mysqli_query($mysqli,$user_edit);
                                
                                $qry1 = "INSERT INTO tbl_transaction (`user_id`, `type`, `type_no`, `date`, `money`) 
                                                 VALUES ('$user_id', 11, '11', NOW(), '$currentValue')";
                                $result_insert = mysqli_query($mysqli, $qry1);
	                        
		                        $user_edit= "UPDATE tbl_hyip_order SET status = '2' WHERE order_id= $plan_id AND user_id= $user_id"; 	 
		                        
   		                        $user_res = mysqli_query($mysqli,$user_edit);	
	  	                        
	  	                        $_SESSION['msg']="plan_redeemed";
	  	                          //<!--removed .php-->
		                        header( "Location:investments");
		                        exit;	
	  	                        
                                }
                                else
                                {
                                    $_SESSION['msg']="plan_not_redeemed";
                                    //  <!--removed .php-->
		                            header( "Location:investments");
		                            exit;
                                }
	    
	                        }
							
							
$currency_qry = "SELECT currency FROM tbl_settings";
$currency_result = mysqli_query($mysqli, $currency_qry);
$currency_row = mysqli_fetch_assoc($currency_result);
$currency = $currency_row['currency'];	
?>
<head>
    <title><?php echo $client_lang['my_investment']; ?></title>
    <link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">
    <style>
        .investment-card {
            margin-bottom: 20px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .investment-details {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
        }
        .investment-details div {
            margin: 5px 0;
        }
        .btn-withdraw {
            margin-top: 10px;
        }
    </style>
</head>

<div class="row">
    <div class="col-xs-12">
        <div class="card mrg_bottom">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $client_lang['my_investment']; ?></div>
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
                                <?php echo $client_lang[$_SESSION['msg']] ; ?> 
                            </div>
                        <?php unset($_SESSION['msg']);}?>	
                    </div>
                </div>
            </div>

            <div class="col-md-12 mrg-top">
                <?php
                    while($users_row=mysqli_fetch_array($users_result)) {
                ?>
                <div class="investment-card">
                    <h4><?php echo $users_row['plan_name']; ?></h4>
                    <div class="investment-details">
                        <div><strong><?php echo $invest_lang['investmentAmount']; ?>:</strong> <?php echo $currency.$users_row['investment_amount']; ?></div>
                        <div><strong><?php echo $invest_lang['currentAmount']; ?>:</strong> <?php echo $currency.$users_row['current_value']; ?></div>
                        <div><strong><?php echo $invest_lang['roi']; ?>:</strong> <?php echo $users_row['interest']; ?></div>
                        <div><strong><?php echo $invest_lang['compounding']; ?>:</strong> <?php echo $users_row['plan_repeat_text']; ?></div>
                        <!--<div><strong><?php echo $invest_lang['return_in_rs']; ?>:</strong> <?php echo $currency.$users_row['return_value']; ?></div>-->
                        <!--<div><strong><?php echo $invest_lang['return_in_percent']; ?>:</strong> <?php echo $users_row['return_percent']; ?>%</div>-->
                        <div><strong><?php echo $invest_lang['locked_tenure']; ?>:</strong> <?php echo $users_row['plan_duration']; ?></div>
                        <!--<div><strong><?php echo $invest_lang['matured_on']; ?>:</strong> <?php echo $users_row['maturity_date']; ?></div>-->
                    </div>

                    <?php if ($users_row['status'] == 2): ?>
                        <button class="btn btn-default disabled btn-withdraw" onclick="return false;">
                            <i class="fa fa-close"></i>&nbsp;<?php echo $client_lang['cancelPlanAlready']; ?>
                        </button>
                    <?php elseif ($users_row['plan_cancelable'] == 1): ?>
                        <a href="investments?cancel_plan=<?php echo $users_row['order_id']; ?>" class="btn btn-danger btn-withdraw">
                            <i class="fa fa-close"></i>&nbsp;<?php echo $invest_lang['cancelPlan']; ?>
                        </a>
                    <?php else: ?>
                        <button class="btn btn-default disabled btn-withdraw" onclick="return false;">
                            <i class="fa fa-close"></i>&nbsp;<?php echo $client_lang['cancelPlanNot']; ?>
                        </button>
                    <?php endif; ?>
                </div>
                <?php } ?>
            </div>

            <div class="col-md-12 col-xs-12">
                <div class="pagination_item_block">
                    <nav>
                        <?php if(!isset($_POST["search"])){ include("pagination.php");}?>                 
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>

<?php include('includes/footer.php');?>