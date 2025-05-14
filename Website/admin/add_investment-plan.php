<?php include('includes/header.php');

    include('includes/function.php');
	include('language/language.php'); 

 	require_once("thumbnail_images.class.php");

	 
$querytime = "SELECT timezone FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
  	
  	
date_default_timezone_set($rowtime['timezone']);
$date = date('Y-m-d');
$time = date('H:i:s');

if (isset($_POST['submit']) and isset($_GET['add'])) {
    
    
    $plan_name = mysqli_real_escape_string($mysqli, $_POST['plan_name']);
    $plan_short_description = mysqli_real_escape_string($mysqli, $_POST['plan_short_description']);
    $plan_description = mysqli_real_escape_string($mysqli, $_POST['plan_description']);
    $plan_minimum = mysqli_real_escape_string($mysqli, $_POST['plan_minimum']);
    $plan_maximum = mysqli_real_escape_string($mysqli, $_POST['plan_maximum']);
    $plan_interest = mysqli_real_escape_string($mysqli, $_POST['plan_interest']);
    $plan_interest_type = mysqli_real_escape_string($mysqli, $_POST['plan_interest_type']);
    $plan_interest_frequency = mysqli_real_escape_string($mysqli, $_POST['plan_interest_frequency']);
    $plan_repeat_time = mysqli_real_escape_string($mysqli, $_POST['plan_repeat_time']);
    $plan_compound_interest = mysqli_real_escape_string($mysqli, $_POST['plan_compound_interest']);
    $plan_capital_back = mysqli_real_escape_string($mysqli, $_POST['plan_capital_back']);
    $plan_cancelable = mysqli_real_escape_string($mysqli, $_POST['plan_cancelable']);
    $plan_penalty = mysqli_real_escape_string($mysqli, $_POST['plan_penalty']);
    $plan_penalty_type = mysqli_real_escape_string($mysqli, $_POST['plan_penalty_type']);
    $plan_color = ltrim($_POST['plan_color'], '#');

    // Other data fields
    $data = array(
        'plan_name' => $plan_name,
        'plan_short_description' => $plan_short_description,
        'plan_description' => $plan_description,
        'plan_minimum' => $plan_minimum,
        'plan_maximum' => $plan_maximum,
        'plan_interest' => $plan_interest,
        'plan_interest_type' => $plan_interest_type,
        'plan_interest_frequency' => $plan_interest_frequency,
        'plan_repeat_time' => $plan_repeat_time,
        'plan_compound_interest' => $plan_compound_interest,
        'plan_capital_back' => $plan_capital_back,
        'plan_cancelable' => $plan_cancelable,
        'plan_penalty' => $plan_penalty,
        'plan_penalty_type' => $plan_penalty_type,
        'plan_color' => $plan_color,
        'plan_status' => 1
    );

    $qry = Insert('tbl_hyip', $data);

    $_SESSION['msg'] = "30";
    header("location:investment.php");
    exit;
}



	if(isset($_GET['plan_id']))
	{
			 
			$user_qry="SELECT * FROM tbl_hyip where plan_id='".$_GET['plan_id']."'";
			$user_result=mysqli_query($mysqli,$user_qry);
			$user_row=mysqli_fetch_assoc($user_result);
		
	}
	
if(isset($_POST['submit']) and isset($_POST['plan_id']))
{

    $plan_name = mysqli_real_escape_string($mysqli, $_POST['plan_name']);
    $plan_short_description = mysqli_real_escape_string($mysqli, $_POST['plan_short_description']);
    $plan_description = mysqli_real_escape_string($mysqli, $_POST['plan_description']);
    $plan_minimum = mysqli_real_escape_string($mysqli, $_POST['plan_minimum']);
    $plan_maximum = mysqli_real_escape_string($mysqli, $_POST['plan_maximum']);
    $plan_interest = mysqli_real_escape_string($mysqli, $_POST['plan_interest']);
    $plan_interest_type = mysqli_real_escape_string($mysqli, $_POST['plan_interest_type']);
    $plan_interest_frequency = mysqli_real_escape_string($mysqli, $_POST['plan_interest_frequency']);
    $plan_repeat_time = mysqli_real_escape_string($mysqli, $_POST['plan_repeat_time']);
    $plan_compound_interest = mysqli_real_escape_string($mysqli, $_POST['plan_compound_interest']);
    $plan_capital_back = mysqli_real_escape_string($mysqli, $_POST['plan_capital_back']);
    $plan_cancelable = mysqli_real_escape_string($mysqli, $_POST['plan_cancelable']);
    $plan_penalty = mysqli_real_escape_string($mysqli, $_POST['plan_penalty']);
    $plan_penalty_type = mysqli_real_escape_string($mysqli, $_POST['plan_penalty_type']);
    
    $plan_color = ltrim($_POST['plan_color'], '#');

// Add other data fields to the array
$data = array(
    'plan_name' => $plan_name,
        'plan_short_description' => $plan_short_description,
        'plan_description' => $plan_description,
        'plan_minimum' => $plan_minimum,
        'plan_maximum' => $plan_maximum,
        'plan_interest' => $plan_interest,
        'plan_interest_type' => $plan_interest_type,
        'plan_interest_frequency' => $plan_interest_frequency,
        'plan_repeat_time' => $plan_repeat_time,
        'plan_compound_interest' => $plan_compound_interest,
        'plan_capital_back' => $plan_capital_back,
        'plan_cancelable' => $plan_cancelable,
        'plan_penalty' => $plan_penalty,
        'plan_penalty_type' => $plan_penalty_type,
);
// Add the conditional element separately
if ($plan_color != '000000') {
    $data['plan_color'] = $plan_color;
}

// Update the database with the collected data
$user_edit = Update('tbl_hyip', $data, "WHERE plan_id = '" . $_POST['plan_id'] . "'");


    if ($user_edit > 0){
        $_SESSION['msg'] = "29";
        header("Location:add_investment-plan.php?plan_id=".$_POST['plan_id']);
        exit;
    } 
}

		
$category_qry1 = "SELECT c_name FROM tbl_cat WHERE c_id != 1 AND c_view !=1 ORDER BY RAND() LIMIT 1";
$category_result1 = mysqli_query($mysqli, $category_qry1);      
$category_row1 = mysqli_fetch_assoc($category_result1);

$cat = $category_row1['c_name'];

$category_qry2 = "SELECT city_name FROM tbl_city ORDER BY RAND() LIMIT 1";
$category_result2 = mysqli_query($mysqli, $category_qry2);      
$category_row2 = mysqli_fetch_assoc($category_result2);

$city = $category_row2['city_name'];
	
	
	
?>
 	
<style>
    .block_wallpaper img {
  width: 200px;
  height: 200px;
   /* Keeps aspect ratio while fitting image */
}

.fileupload_img.replaced .database-image {
  display: none; /* Hide existing image when replaced class is present */
}
</style>
<head>
<title><?php if(isset($_GET['plan_id'])){?><?php echo $client_lang['edit']; ?><?php }else{?><?php echo $client_lang['add']; ?><?php }?>&nbsp;<?php echo $client_lang['investment_plan']; ?></title>
</head>

 <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php if(isset($_GET['plan_id'])){?><?php echo $client_lang['edit']; ?><?php }else{?><?php echo $client_lang['add']; ?><?php }?>&nbsp;<?php echo $client_lang['investment_plan']; ?></div>
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
            	<input  type="hidden" name="plan_id" value="<?php echo $_GET['plan_id'];?>" />

              <div class="section">
                <div class="section-body">

                   <strong><center><?php echo $client_lang['plan_details']; ?></center></strong>
                   <center><p class="control-label-help"><?php echo $client_lang['plan_details_help']; ?></p></center>
                   <hr>
                   
                 
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['plan_name']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['plan_name_help']; ?></p></label>
                    <div class="col-md-6">
                      <input type="text" name="plan_name" id="plan_name" placeholder="eg. Diamond Plan" value="<?php if(isset($_GET['plan_id'])){echo $user_row['plan_name'];}?>" class="form-control" required>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['plan_short_description']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['plan_short_description_help']; ?></p></label>
                    <div class="col-md-6">
                      <input type="text" name="plan_short_description" id="plan_short_description" placeholder="eg. High Interest" value="<?php if(isset($_GET['plan_id'])){echo $user_row['plan_short_description'];}?>" class="form-control" required>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['plan_description']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['plan_description_help']; ?></p></label>
                    <div class="col-md-6">
                      <input type="text" name="plan_description" id="plan_description" placeholder="eg. This is a plan which offers high interest" value="<?php if(isset($_GET['plan_id'])){echo $user_row['plan_description'];}?>" class="form-control" required>
                    </div>
                  </div>
                  
                  <strong><center><?php echo $client_lang['investment_details']; ?></center></strong>
                  <center><p class="control-label-help"><?php echo $client_lang['investment_details_help']; ?></p></center>
                  <hr>
                  
                  
                  <div class="form-group">
                           <label class="col-md-3 control-label"><?php echo $client_lang['minimum_investment']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_investment_help']; ?></p></label>
                           <div class="col-md-6">
                               <div class="input-group">
                      <input type="text" name="plan_minimum" id="plan_minimum" placeholder="eg. 10"  value="<?php if(isset($_GET['plan_id'])){echo $user_row['plan_minimum'];}?>" class="form-control" required>
                    <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                    </div>
                  </div>
                 
                     <div class="form-group">
                           <label class="col-md-3 control-label"><?php echo $client_lang['maximum_investment']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_investment_help']; ?></p></label>
                           <div class="col-md-6">
                               <div class="input-group">
                      <input type="text" name="plan_maximum" id="plan_maximum" placeholder="eg. 100"  value="<?php if(isset($_GET['plan_id'])){echo $user_row['plan_maximum'];}?>" class="form-control" required>
                    <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                    </div>
                  </div>
                  
                   <strong><center><?php echo $client_lang['interest_details']; ?></center></strong>
                   <center><p class="control-label-help"><?php echo $client_lang['interest_details_help']; ?></p></center>
                   <hr>
                   
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['interest_type']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['interest_type_help']; ?></p></label>
                    <div class="col-md-6">                       
                        <select name="plan_interest_type" id="plan_interest_type" style="width:280px; height:25px;" class="select2" onchange="updateInterestLabel()" required>
                            <option value="">- <?php echo $client_lang['select_interest_type']; ?> -</option>
                            <option value="1" <?php if($user_row['plan_interest_type']=='1'){?>selected<?php }?>><?php echo $client_lang['interest_percentage']; ?></option>
                            <option value="0" <?php if($user_row['plan_interest_type']=='0'){?>selected<?php }?>><?php echo $client_lang['interest_fixed']; ?></option>
                        </select>
                    </div>
                </div>
                
                <script>
                    function updateInterestLabel() {
                        var interestType = document.getElementById('plan_interest_type').value;
                        var interestLabel = document.getElementById('interest-label');
                        
                        if (interestType == '1') {
                            interestLabel.innerHTML = '%';
                        } else {
                            interestLabel.innerHTML = '<?php echo $client_lang['coin']; ?>';
                        }
                    }
                </script>


                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['investment_interest']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['investment_interest_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                            <input type="text" name="plan_interest" id="plan_interest" placeholder="eg. 10"  value="<?php if(isset($_GET['plan_id'])){echo $user_row['plan_interest'];}?>" class="form-control" required>
                            <span class="input-group-addon" id="interest-label">
                                <?php if($user_row['plan_interest_type'] == '1'): ?>
                                    %
                                <?php elseif($user_row['plan_interest_type'] == '0'): ?>
                                    <?php echo $client_lang['coin']; ?>
                                <?php endif; ?>
                            </span>
                        </div>
                    </div>
                </div>

                  
                  <div class="form-group">
                                <label class="col-md-3 control-label"><?php echo $client_lang['investment_interest_frequency']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['investment_interest_frequency_help']; ?></p></label>
                                <div class="col-md-6">
                                    <select name="plan_interest_frequency" id="plan_interest_frequency" style="width:280px; height:25px;" class="select2" onchange="updateRepeatTime()" required>
                                        <option value="">- <?php echo $client_lang['interest_frequency_type']; ?> -</option>
                                        <!--<option value="1" <?php if ($user_row['plan_interest_frequency'] == '1') { ?>selected<?php } ?>>Every Minute</option>-->
                                        <option value="2" <?php if ($user_row['plan_interest_frequency'] == '2') { ?>selected<?php } ?>><?php echo $client_lang['every_hour']; ?></option>
                                        <option value="3" <?php if ($user_row['plan_interest_frequency'] == '3') { ?>selected<?php } ?>><?php echo $client_lang['every_day']; ?></option>
                                        <option value="4" <?php if ($user_row['plan_interest_frequency'] == '4') { ?>selected<?php } ?>><?php echo $client_lang['every_week']; ?></option>
                                        <option value="5" <?php if ($user_row['plan_interest_frequency'] == '5') { ?>selected<?php } ?>><?php echo $client_lang['every_month']; ?></option>
                                        <option value="6" <?php if ($user_row['plan_interest_frequency'] == '6') { ?>selected<?php } ?>><?php echo $client_lang['every_year']; ?></option>
                                    </select>
                                </div>
                            </div>
                            <script>
                                function updateRepeatTime() {
                                    var frequency = document.getElementById('plan_interest_frequency').value;
                                    var repeatTime = document.getElementById('plan_repeat_time');
                                    if (frequency == '1') {
                                        repeatTime.value = '00:01:00'; // Every Minute
                                    }
                                    else if (frequency == '2') {
                                        repeatTime.value = '01:00:00'; // Every Hour
                                    }
                                    else if (frequency == '3') {
                                        repeatTime.value = '24:00:00'; // Every Day
                                    }
                                    else if (frequency == '4') {
                                        repeatTime.value = '168:00:00'; // Every Week (168 hours in a week)
                                    }
                                    else if (frequency == '5') {
                                        repeatTime.value = '672:00:00'; // Every Month (approx. 28 days in a month)
                                    }
                                    else if (frequency == '6') {
                                        repeatTime.value = '8760:00:00'; // Every Year (8760 hours in a year)
                                    }
                                }
                            </script>
                            <div class="form-group">
                                <label class="col-md-3 control-label"><?php echo $client_lang['investment_repeat_time']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['investment_repeat_time_help']; ?></p></label>
                                <div class="col-md-6">
                                    <div class="input-group">
                                        <input type="text" name="plan_repeat_time" id="plan_repeat_time" value="<?php if (isset($_GET['plan_id'])) { echo $user_row['plan_repeat_time']; } ?>" class="form-control" readonly>
                                        <span class="input-group-addon"><?php echo $client_lang['hour']; ?></span>
                                    </div>
                                </div>
                            </div>
                
                   
                  
                  <strong><center><?php echo $client_lang['other_investment_details']; ?></center></strong>
                  <center><p class="control-label-help"><?php echo $client_lang['other_investment_details_help']; ?></p></center>
                   <hr>
                  
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['compound_interest']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['compound_interest_help']; ?></p></label>
                    
                    <div class="col-md-6">                       
                        <select name="plan_compound_interest" id="plan_compound_interest" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($user_row['plan_compound_interest']=='1'){?>selected<?php }?>><?php echo $client_lang['yes']; ?></option>
                            <option value="0" <?php if($user_row['plan_compound_interest']=='0'){?>selected<?php }?>><?php echo $client_lang['no']; ?></option>
                        </select>
                      </div>
                  </div>
                  
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['capital_back']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['capital_back_help']; ?></p></label>
                    <div class="col-md-6">                       
                        <select name="plan_capital_back" id="plan_capital_back" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($user_row['plan_capital_back']=='1'){?>selected<?php }?>><?php echo $client_lang['yes']; ?></option>
                            <option value="0" <?php if($user_row['plan_capital_back']=='0'){?>selected<?php }?>><?php echo $client_lang['no']; ?></option>
                        </select>
                      </div>
                  </div>
                  
                  <strong><center><?php echo $client_lang['early_withdrawl']; ?></center></strong>
                  <center><p class="control-label-help"><?php echo $client_lang['early_withdrawl_help']; ?></p></center>
                   <hr>
                   
                   <div class="form-group">
                        <label class="col-md-3 control-label"><?php echo $client_lang['early_withdrawl']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['early_withdrawl_help']; ?></p></label>
                        <div class="col-md-6">                       
                            <select name="plan_cancelable" id="plan_cancelable" style="width:280px; height:25px;" class="select2" onchange="togglePenaltyFields()" required>
                                <option value="">- <?php echo $client_lang['early_withdrawl_type']; ?> -</option>
                                <option value="1" <?php if($user_row['plan_cancelable']=='1'){?>selected<?php }?>><?php echo $client_lang['allowed']; ?></option>
                                <option value="0" <?php if($user_row['plan_cancelable']=='0'){?>selected<?php }?>><?php echo $client_lang['not_allowed']; ?></option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group penalty-fields" style="display: <?php echo ($user_row['plan_cancelable'] == '1') ? 'block' : 'none'; ?>">
                        <label class="col-md-3 control-label"><?php echo $client_lang['early_withdrawl_penalty_type']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['early_withdrawl_penalty_type_help']; ?></p></label>
                        <div class="col-md-6">                       
                            <select name="plan_penalty_type" id="plan_penalty_type" style="width:280px; height:25px;" class="select2" onchange="updatePenaltyLabel()" required>
                                <option value="">- <?php echo $client_lang['penalty_type_choice']; ?> -</option>
                                <option value="1" <?php if($user_row['plan_penalty_type']=='1'){?>selected<?php }?>><?php echo $client_lang['penalty_percentage']; ?></option>
                                <option value="0" <?php if($user_row['plan_penalty_type']=='0'){?>selected<?php }?>><?php echo $client_lang['penalty_fixed']; ?></option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group penalty-fields" style="display: <?php echo ($user_row['plan_cancelable'] == '1') ? 'block' : 'none'; ?>">
                        <label class="col-md-3 control-label"><?php echo $client_lang['penalty']; ?>:-</label>
                        <div class="col-md-6">
                            <div class="input-group">
                                <input type="text" name="plan_penalty" id="plan_penalty" placeholder="eg. 10"  value="<?php if(isset($_GET['plan_id'])){echo $user_row['plan_penalty'];}?>" class="form-control" required>
                                <span class="input-group-addon" id="penalty-label">
                                    <?php if($user_row['plan_penalty_type'] == '1'): ?>
                                        %
                                    <?php elseif($user_row['plan_penalty_type'] == '0'): ?>
                                        <?php echo $client_lang['coin']; ?>
                                    <?php endif; ?>
                                </span>
                            </div>
                        </div>
                    </div>
                    
                
                
                <script>
                    function updatePenaltyLabel() {
                        var penaltyType = document.getElementById('plan_penalty_type').value;
                        var penaltyLabel = document.getElementById('penalty-label');
                        
                        if (penaltyType == '1') {
                            penaltyLabel.innerHTML = '%';
                        } else {
                            penaltyLabel.innerHTML = '<?php echo $client_lang['coin']; ?>';
                        }
                    }
                </script>
                <script>
                    function togglePenaltyFields() {
                        var cancelable = document.getElementById('plan_cancelable').value;
                        var penaltyFields = document.querySelectorAll('.penalty-fields');
                        
                        if (cancelable == '1') {
                            penaltyFields.forEach(function(field) {
                                field.style.display = 'block';
                            });
                        } else {
                            penaltyFields.forEach(function(field) {
                                field.style.display = 'none';
                            });
                        }
                    }
                </script>

                
                <strong><center><?php echo $client_lang['plan_design_color']; ?></center></strong>
                  <center><p class="control-label-help"><?php echo $client_lang['plan_design_color_help']; ?></p></center>
                <hr>
                    
                 <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['color']; ?>:</label>
                    <div class="col-md-6">
                        <input type="color" name="plan_color" id="plan_color" value="<?php if(isset($_GET['plan_id'])){echo '#' . $user_row['plan_color'];} else { echo '#EA3343'; } ?>" class="form-control" style="height: 34px; padding: 6px;">
                    </div>
                </div>
                  
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="submit" class="btn btn-primary"><?php if(isset($_GET['plan_id'])){?><?php echo $client_lang['edit']; ?><?php }else{?><?php echo $client_lang['add']; ?><?php }?>&nbsp;<?php echo $client_lang['investment_plan']; ?></button>
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