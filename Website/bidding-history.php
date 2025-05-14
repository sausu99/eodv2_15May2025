<?php include('includes/header.php'); 

    include('includes/function.php');
	include('language/language.php'); 
	
	$user_id = isset($_SESSION['user_id']) ? intval($_SESSION['user_id']) : 0;




							$tableName="tbl_bid";		
							$targetpage = "bidding-history.php"; 	
							$limit = 15; 
				
		                    $query = "SELECT COUNT(*) as num FROM $tableName left join tbl_offers on tbl_offers.o_id = tbl_bid.o_id
		                     LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE tbl_bid.u_id = '$user_id'";
		                
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
							

                            $users_qry = "SELECT * FROM tbl_bid
                                LEFT JOIN tbl_users ON tbl_users.id  = $user_id
                                LEFT JOIN tbl_offers ON tbl_offers.o_id = tbl_bid.o_id
                                LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
                                WHERE tbl_bid.u_id = '$user_id'
                                ORDER BY tbl_bid.bd_id DESC LIMIT $start, $limit";  
                        
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
							if (mysqli_num_rows($users_result) == 0) {
                                $title = $client_lang['no_bid'];
                                $description = $client_lang['no_bid_description'];
                                $image = 'no_bid.gif';
                                include("nodata.php");
                                exit;
                            }
							
							
$currency_qry = "SELECT currency FROM tbl_settings";
$currency_result = mysqli_query($mysqli, $currency_qry);
$currency_row = mysqli_fetch_assoc($currency_result);
$currency = $currency_row['currency'];	
?>
<head>
<title><?php echo $client_lang['bidding_history']; ?></title>
</head>

 <div class="row">
      <div class="col-xs-12">
        <div class="card mrg_bottom">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
               <div class="page_title"><?php echo $client_lang['bidding_history']; ?></div>
            </div>
            <div class="col-md-7 col-xs-12">              
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
                  <th><?php echo $client_lang['bid']; ?></th>
                  <th><?php echo $client_lang['bid_placed_on']; ?></th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
              	<?php
						while($users_row=mysqli_fetch_array($users_result))
						{
						    
				?>
                <tr> 
                <td><?php echo $currency.$users_row['bd_value'];?></td>
		           <td><?php echo $users_row['bd_date'];?><br><?php echo $invest_lang['for'].': '; ?><?php echo $users_row['bd_amount'].' '.$auction_lang['coins'];?></td> 
                  <td>
                    <a href="auction/seeAuction/<?php echo $users_row['o_id'];?>" class="btn btn-success"><i class="fa fa-eye"></i>&nbsp;<?php echo $client_lang['see_auction']; ?></a></td>
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