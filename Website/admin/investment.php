<?php include('includes/header.php'); 

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
							$targetpage = "investment.php"; 	
							$limit = 15; 
							
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
			
						 $users_qry="SELECT * FROM tbl_hyip
						 ORDER BY tbl_hyip.plan_id DESC LIMIT $start, $limit";  
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
	 }
	if(isset($_GET['plan_id']))
	{
		  
		 
		Delete('tbl_hyip','plan_id='.$_GET['plan_id'].'');
		
		$_SESSION['msg']="12";
		header( "Location:investment.php");
		exit;
	}
	
	//Active and Deactive status
	if(isset($_GET['status_deactive_id']))
	{
		$data = array('plan_status'  =>  '0');
		
		$edit_status=Update('tbl_hyip', $data, "WHERE plan_id = '".$_GET['status_deactive_id']."'");
		
		 $_SESSION['msg']="14";
		 header( "Location:investment.php");
		 exit;
	}
	if(isset($_GET['status_active_id']))
	{
		$data = array('plan_status'  =>  '1');
		
		$edit_status=Update('tbl_hyip', $data, "WHERE plan_id = '".$_GET['status_active_id']."'");
		
		$_SESSION['msg']="13";
		 header( "Location:investment.php");
		 exit;
	}
	
	$querytime = "SELECT currency FROM tbl_settings";
    $resulttime = mysqli_query($mysqli, $querytime);
    $rowtime = mysqli_fetch_assoc($resulttime);
    $currency = $rowtime['currency'];
	
	
?>
<style>
    .blue-btn {
        background-color: #007bff; /* Blue background color */
        color: #fff; /* White text color */
        padding: 10px 20px; /* Padding for the button */
        border: none; /* No border */
        border-radius: 4px; /* Rounded corners */
        cursor: pointer; /* Cursor style */
        text-decoration: none; /* Remove default text decoration */
        display: inline-block; /* Make it inline-block to adjust width */
        transition: background-color 0.3s; /* Smooth transition on hover */
    }
    
    .blue-btn:hover {
        color: #fff;
        background-color: #0056b3; /* Darker blue color on hover */
    }
    
</style>
<head>
<title><?php echo $client_lang['manage_investment']; ?></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
</head>

 <div class="row">
      <div class="col-xs-12">
        <div class="card mrg_bottom">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php echo $client_lang['manage_investment']; ?></div>
            </div>
            <div class="col-md-7 col-xs-12">              
                  <div class="search_list">
                    <div class="search_block">
                      <form  method="post" action="">
                        <input class="form-control input-sm" placeholder="<?php echo $client_lang['search_plans']; ?>" aria-controls="DataTables_Table_0" type="search" name="search_value" required>
                        <button type="submit" name="user_search" class="btn-search"><i class="fa fa-search"></i></button>
                      </form>  
                    </div> 
                    <div class="add_btn_primary"> <a href="add_investment-plan.php?add"><i class="fa fa-plus"></i>&nbsp;&nbsp;<?php echo $client_lang['add_investment']; ?></a> </div>
                  </div>
                <div class="add_btn_primary"> <a href="view_investments.php"><?php echo $client_lang['see_order']; ?></a> </div>
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
          <div class="col-md-12 mrg-top">
            <table class="table table-striped table-bordered table-hover">
              <thead>
                <tr>
                  <th><?php echo $client_lang['plan_name']; ?></th>						 
				  <th><?php echo $client_lang['investment_limit']; ?></th>
				  <th><?php echo $client_lang['plan_interest']; ?></th>
				  <th><?php echo $client_lang['visibility']; ?></th>
                  <th class="cat_action_list"><?php echo $client_lang['action']; ?></th>
                </tr>
              </thead>
              <tbody>
              	<?php
						$i=0;
						while($users_row=mysqli_fetch_array($users_result))
						{

	 
				?>
                <tr>
                   <td><?php echo $users_row['plan_name'];?></td>
                   <td><?php echo $users_row['plan_minimum'].' - '.$users_row['plan_minimum'].' '.$client_lang['coin'];?></td>
		           <td>
                       <?php 
                       // Determine the interest type
                       if ($users_row['plan_interest_type'] == 1) {
                           echo $users_row['plan_interest'] . '%';
                       } else {
                           echo $users_row['plan_interest'] .' '.$client_lang['coin'];
                       }
           
                       // Determine the interest frequency
                       switch ($users_row['plan_interest_frequency']) {
                           case 1:
                               echo ' '.$client_lang['per_minute'];
                               break;
                           case 2:
                               echo ' '.$client_lang['per_hour'];
                               break;
                           case 3:
                               echo ' '.$client_lang['per_day'];
                               break;
                           case 4:
                               echo ' '.$client_lang['per_week'];
                               break;
                           case 5:
                               echo ' '.$client_lang['per_month'];
                               break;
                           case 6:
                               echo ' '.$client_lang['per_year'];
                               break;
                           default:
                               echo '';
                       }
                       ?>
                   </td>
                   <td>
		          		<?php if($users_row['plan_status']!="0"){?>
		              <a href="investment.php?status_deactive_id=<?php echo $users_row['plan_id'];?>" title="Change Status"><span class="badge badge-success badge-icon"><i class="fa fa-check" aria-hidden="true"></i><span><?php echo $client_lang['visible']; ?></span></span></a>

		              <?php }else{?>
		              <a href="investment.php?status_active_id=<?php echo $users_row['plan_id'];?>" title="Change Status"><span class="badge badge-danger badge-icon"><i class="fa fa-check" aria-hidden="true"></i><span><?php echo $client_lang['hidden']; ?></span></span></a>
		              <?php }?>
              		</td>
                   <td>
                       <a href="view_investments.php?plan_id=<?php echo $users_row['plan_id'];?>" class="btn btn-view" title="View Details"><i class="fa fa-eye"></i>&nbsp;<?php echo $client_lang['view_investment']; ?></a>
                       <a href="add_investment-plan.php?plan_id=<?php echo $users_row['plan_id'];?>" class="btn btn-edit"><i class="fa fa-pencil"></i>&nbsp;<?php echo $client_lang['edit']; ?></a>
                    </td>
                </tr>
               <?php
						
						$i++;
						}
			   ?>
              </tbody>
            </table>
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
      </div>
    </div>     



<?php include('includes/footer.php');?>                  