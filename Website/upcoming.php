<?php include('includes/header.php'); 
include("includes/session_check.php");

    include('includes/function.php');
	include('language/language.php');  

$querytime = "SELECT timezone FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
  		                
date_default_timezone_set($rowtime['timezone']);
$time = date('H:i:s');
$date1 = date('Y-m-d');

	if(isset($_POST['user_search']))
	 {
		 
		
		$user_qry="SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE o_date >= '".$date1."' AND (o_date > '".$date1."' OR (o_date = '".$date1."' AND o_stime >= '".$time."')) and o_status = 1 and
                             o_type IN (1,2,4,5,7,8)
						     and tbl_offers.item_name like '%".addslashes($_POST['search_value'])."%' ORDER BY tbl_offers.o_date ASC";  
							 
		$users_result=mysqli_query($mysqli,$user_qry);
		
		 
	 }
	 else
	 {

							$tableName="tbl_offers";		
							$targetpage = "upcoming.php"; 	
							$limit = 12; 
							
							$query = "SELECT COUNT(*) as num FROM $tableName WHERE o_date >= '".$date1."' AND (o_date > '".$date1."' OR (o_date = '".$date1."' AND o_stime >= '".$time."')) AND o_type NOT IN (3,6,9)";
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
							
			
						 $users_qry="SELECT *
                                     FROM tbl_offers
                                     LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                                     WHERE o_date >= '".$date1."' AND (o_date > '".$date1."' OR (o_date = '".$date1."' AND o_stime >= '".$time."'))
                                         AND o_status = 1
                                         AND o_type NOT IN (3,6,9)
                                         ORDER BY tbl_offers.o_date ASC
                                         LIMIT $start, $limit";  
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
							 if(mysqli_num_rows($users_result) == 0)
    {
        include("norefferals.html");
        exit;
    }  
}
?>
<style>
    .btn-blue {
    background-color: #007bff; /* Blue background color */
    color: #fff; /* White text color */
    padding: 10px 20px; /* Padding for the button */
    border: none; /* No border */
    border-radius: 4px; /* Rounded corners */
    cursor: pointer; /* Cursor style */
}

.btn-blue:hover {
    background-color: #0056b3; /* Darker blue color on hover */
}

</style>
<title><?php echo $auction_lang['upcoming']; ?></title>
 <div class="row">
      <div class="col-xs-12">
        <div class="card mrg_bottom">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php echo $auction_lang['upcoming']; ?></div>
            </div>
          <div class="col-md-7 col-xs-12">              
                  <div class="search_list">
                    <div class="search_block">
                      <form  method="post" action="">
                        <input class="form-control input-sm" placeholder="<?php echo $auction_lang['searchItems']; ?>" aria-controls="DataTables_Table_0" type="search" name="search_value" required>
                        <button type="submit" name="user_search" class="btn-search"><i class="fa fa-search"></i></button>
                      </form>  
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
          <div class="col-md-12 mrg-top">
            <table class="table table-striped table-bordered table-hover">
              <thead>
                <tr>
                    <th></th>
				  <th><?php echo $auction_lang['itemName']; ?></th>
				  <th><center><?php echo $auction_lang['upcomingTime']; ?></center></th>
				   <th><?php echo $auction_lang['upcomingCost']; ?></th>
				   <th><center><?php echo $auction_lang['status']; ?></center></th>
                </tr>
              </thead>
              <tbody>
              	<?php
						$i=0;
						while($users_row=mysqli_fetch_array($users_result))
						{

				?>
                <tr>
				   <td style="background-color: white;" width="20%"><?php echo "<img src='../seller/images/" . $users_row['o_image'] . "'>";?></td>
		           <td><?php echo $users_row['o_name'];?></td>   
		           <td>
                        <?php 
                           // Format the 'o_edate' value
                         $formatted_date = date('jS F', strtotime($users_row['o_date']));
                             // Format the 'o_etime' value
                             $formatted_time = date('h:i A', strtotime($users_row['o_stime']));
                          // Concatenate the formatted date and time values
                           echo $formatted_date . ' at ' . $formatted_time;
                        ?>
                    </td>


				   <td>
                       <?php 
                       if ($users_row['o_type'] == 4 || $users_row['o_type'] == 5) {
                           echo $auction_lang['buyTicketFor'] .' '. $users_row['o_amount'] . ' ' . $auction_lang['coins'];
                       } else {
                           echo $auction_lang['bidFor'] .' '. $users_row['o_amount'] . ' ' . $auction_lang['coins'];
                       }
                       ?>
                   </td>
                   <td><center><a href="" style="pointer-events: none; cursor: default;" class="btn btn-blue"><?php echo $auction_lang['upcomingBadge']; ?></a></center>
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
              
            </div>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>
    </div>     



<?php include('includes/footer.php');?>                  