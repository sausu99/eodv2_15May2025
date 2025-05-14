<?php include('includes/header.php'); 
include("includes/session_check.php");

    include('includes/function.php');
	include('language/language.php');  

	function getCurrencyFromSettings($mysqli) {
    // Query to fetch the currency from tbl_settings
    $query = "SELECT currency FROM tbl_settings";
    
    // Execute the query
    $result = mysqli_query($mysqli, $query);

    if ($result) {
        // Check if a row is returned
        if (mysqli_num_rows($result) > 0) {
            // Fetch the currency value from the result
            $row = mysqli_fetch_assoc($result);
            return $row['currency'];
        }
    }

    // Return a default currency value or handle errors as needed
    return '$'; // Default currency value (you can change this)
}

	if(isset($_POST['user_search']))
	 {
		 
		
		$user_qry="SELECT * FROM tbl_coin_list WHERE c_status = 1
						     and tbl_coin_list.c_name like '%".addslashes($_POST['search_value'])."%'";  
							 
		$users_result=mysqli_query($mysqli,$user_qry);
		
		 
	 }
	 else
	 {

							$tableName="tbl_coin_list";	
							//<!--removed .php-->
							$targetpage = "recharge"; 	
							$limit = 25; 
							
							$query = "SELECT COUNT(*) as num FROM $tableName";
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
							
					 	    
			                 $querytime = "SELECT timezone FROM tbl_settings";
                             $resulttime = mysqli_query($mysqli, $querytime);
                             $rowtime = mysqli_fetch_assoc($resulttime);
  		                     
  		                     
                             date_default_timezone_set($rowtime['timezone']);
		                     $time = date('H:i:s');

	                    	 $date1 = date('Y-m-d');
			
						 $users_qry="SELECT * FROM tbl_coin_list WHERE c_status = 1";  
							 
						 $users_result=mysqli_query($mysqli,$users_qry);
							
						if (mysqli_num_rows($users_result) == 0) {
                            $title = $client_lang['no_recharge'];
                            $description = $client_lang['no_recharge_description'];
                            $image = 'no_recharge.gif';
                            include("nodata.php");
                            exit;
                        }
							
	 }
	
?>
<link rel="stylesheet" href="assets/css/home.css">
<title><?php echo $recharge_lang['rechargeTitle']; ?></title>

<div class="card mrg_bottom">
      <div class="page_title_block">
        <div class="col-md-5 col-xs-12">
          <div class="page_title"><?php echo $recharge_lang['rechargeTitle']; ?></div>
        </div>
        <div class="clearfix"></div>
      </div>
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
      <div class="col-md-12 mrg-top">
        <div class="row">
          <?php
            $i = 0;
            while($users_row = mysqli_fetch_array($users_result)) {
          ?>
          <div class="col-md-4 col-sm-6 col-xs-12">
              <div class="card">
                  <div class="card-body" style="display: flex; align-items: center;">
                      <img src="../seller/images/<?php echo $users_row['c_image']; ?>" 
                           alt="Shop Image" 
                           onerror="this.onerror=null;this.src='placeholder.jpg';" 
                           style="width: 100px; height: 100px; object-fit: cover; margin-right: 20px;">
                      <div>
                          <h4 class="card-title"><?php echo $users_row['c_name']; ?></h4>
                          <p class="card-text"><?php echo $recharge_lang['rechargeGet'] . $users_row['c_coin'] . ' ' . $invest_lang['coins']; ?></p>
                          <a href="coinpurchase?c_id=<?php echo $users_row['c_id']; ?>" class="btn btn-blue">
                              <?php echo $recharge_lang['rechargePay']; ?> 
                              <?php $currency = getCurrencyFromSettings($mysqli); echo $currency . $users_row['c_amount'];?>
                          </a>
                      </div>
                  </div>
              </div>
          </div>
          <?php
            $i++;
            }
          ?>
        </div>
      </div>
      <div class="col-md-12 col-xs-12">
        <div class="pagination_item_block">
          <!-- Pagination controls can be added here -->
        </div>
      </div>
      <div class="clearfix"></div>
    </div>

<?php include('includes/footer.php');?>