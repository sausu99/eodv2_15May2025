<?php 
include('includes/header.php'); 
include('includes/function.php');
include('language/language.php');  


	if(isset($_POST['user_search']))
	 {
		 
		
		$user_qry="SELECT * FROM tbl_hyip WHERE tbl_hyip.plan_name like '%".addslashes($_POST['search_value'])."%' ORDER BY tbl_hyip.plan_id DESC";  
							 
		$users_result=mysqli_query($mysqli,$user_qry);
		
		 
	 }
	 else
	 {

							$tableName="tbl_hyip";		
							$targetpage = "plans.php"; 	
							$limit = 15; 
							
							$query = "SELECT COUNT(*) as num FROM $tableName WHERE plan_status = '1'";
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
							
						 $users_qry="SELECT * FROM tbl_hyip
						 WHERE plan_status = '1'
						 ORDER BY tbl_hyip.plan_id DESC LIMIT $start, $limit";  
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
	 }
?>
<style>
    .package-card {
        background-color: #f9f9f9;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        margin: 10px;
        border: 1px solid #ccc;
        color: white; /* Set text color to white */
    }

    .package-card__title {
        font-size: 24px;
        margin-bottom: 10px;
        color: white; /* Set text color to white */
    }

    .package-card__subtitle {
        font-size: 16px;
        color: #777;
        margin-bottom: 20px;
        color: white; /* Set text color to white */
    }

    .package-card__features {
        list-style: none;
        padding: 0;
        margin-bottom: 20px;
        text-align: center; /* Align list items to the left */
    }

    .package-card__features li {
        font-size: 16px;
        color: #555;
        margin-bottom: 5px;
        color: white; /* Set text color to white */
    }

    .package-card__range {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 20px;
        color: white; /* Set text color to white */
    }

    .btn--base {
        background-color: #007bff;
        color: #fff;
        border: none;
        border-radius: 5px;
        padding: 10px 20px;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .btn--base:hover {
        background-color: #0056b3;
        color: #fff;
    }

    .pagination_item_block {
        margin-top: 20px; /* Added margin to create space between pagination and package cards */
    }

    .badge {
        font-size: 14px;
        padding: 5px 10px;
        border-radius: 5px;
    }

    .badge--success {
        background-color: #28a745;
        color: #fff;
    }

    .badge--warning {
        background-color: #ffc107;
        color: #212529;
    }
</style>


<title><?php echo $invest_lang['avaPlans']; ?></title>

        <div class="card mrg_bottom">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php echo $invest_lang['avaPlans']; ?></div>
            </div>
            <div class="col-md-7 col-xs-12">              
                  <div class="search_list">
                    <div class="search_block">
                      <form  method="post" action="">
                        <input class="form-control input-sm" placeholder="<?php echo $invest_lang['searchPlans']; ?>" aria-controls="DataTables_Table_0" type="search" name="search_value" required>
                        <button type="submit" name="user_search" class="btn-search"><i class="fa fa-search"></i></button>
                      </form>  
                    </div> 
                  </div>
                  
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
          <div class="row gy-4 justify-content-center">
                <?php while ($users_row = mysqli_fetch_array($users_result)) {
                    $plan_color = '#'.$users_row['plan_color']; // Retrieve plan_color from database
                ?>
                    <div class="col-xl-3 col-lg-4 col-md-6">
                        <div class="package-card text-center bg_img" style="background-color: <?php echo $plan_color; ?>;"> <!-- Set background color dynamically -->
                            <h4 class="package-card__title base--color mb-2"><?php echo $users_row['plan_name']; ?></h4><hr>
                            <h6 class="package-card__subtitle"><?php echo $users_row['plan_description']; ?></h6>

                <ul class="package-card__features mt-4">
                <li>
                    <?php echo $invest_lang['return']; ?> 
                    <?php 
                        if ($users_row['plan_interest_type'] == 1) {
                            echo $users_row['plan_interest'].'%';
                        } else {
                            echo $users_row['plan_interest'].' Coins';
                        }
                    ?>
                </li>
                <li><?php echo $users_row['plan_repeat_text'];?></li>
                <li><?php echo $invest_lang['for'].' '; ?> <?php echo $users_row['plan_duration'];?></li>
                 <li><?php echo $invest_lang['capitalBack']; ?>: 
                    <?php 
                        if ($users_row['plan_capital_back'] == 1) {
                            echo '<i class="fi fi-ss-badge-check"></i>';
                        } else {
                            echo '<i class="fi fi-br-octagon-xmark"></i>';
                        }
                    ?>
                </li>
                <li><?php echo $invest_lang['compund']; ?>: 
                    <?php 
                        if ($users_row['plan_compound_interest'] == 1) {
                            echo '<i class="fi fi-ss-badge-check"></i>';
                        } else {
                            echo '<i class="fi fi-br-octagon-xmark"></i>';
                        }
                    ?>
                </li>
                <li><?php echo $invest_lang['lifetime']; ?>: 
                    <?php 
                        if ($users_row['plan_lifetime'] == 1) {
                            echo '<i class="fi fi-ss-badge-check"></i>';
                        } else {
                            echo '<i class="fi fi-br-octagon-xmark"></i>';
                        }
                    ?>
                </li>
            </ul>

            <div class="package-card__range mt-5 base--color">
                <?php 
                    if ($users_row['plan_minimum'] == $users_row['plan_maximum']) {
                        echo $users_row['plan_minimum'].' '.$invest_lang['coins'];
                    } else {
                        echo $users_row['plan_minimum'].' - '.$users_row['plan_maximum'].' '.$invest_lang['coins'];
                    }
                ?>
            </div>

<hr>        <!--removed .php-->
            <a href="invest?plan_id=<?php echo $users_row['plan_id'];?>" class="btn--base btn-md mt-4"><?php echo $invest_lang['investNow']; ?></a>
        </div><!-- package-card end -->
    </div>
    <?php 
    }
    ?>
</div>
          <div class="col-md-12 col-xs-12">
            <div class="pagination_item_block">
              <nav>
              	<?php if(!isset($_POST["search"])){ include("pagination.php");}?>                 
              </nav>
            </div>
          </div>
          <div class="clearfix"></div>
        </div>



<?php include('includes/footer.php');?>                  