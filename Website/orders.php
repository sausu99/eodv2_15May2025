<?php include('includes/header.php'); 
include("includes/session_check.php");

    include('includes/function.php');
	include('language/language.php');  


if(isset($_SESSION['user_id']))
	{
	    $user_id = sanitize($_SESSION['user_id']);
			 
		$qry="select * from tbl_users where id='".$user_id."'";
		 
		$result=mysqli_query($mysqli,$qry);
		$session_row=mysqli_fetch_assoc($result);

	}
	
	
if(isset($_POST['user_search']))
	 {
		 
		$id = sanitize($_SESSION['user_id']);
		
		$user_qry="SELECT *,tbl_order.o_id as o_id FROM tbl_order 
		    left join tbl_users on tbl_users.id = tbl_order.u_id
		    LEFT JOIN tbl_items ON tbl_items.item_id = tbl_order.order_id 
		    WHERE u_id='".$id."' AND tbl_items.o_name like '%".addslashes($_POST['search_value'])."%'
			ORDER BY tbl_order.o_id DESC";  
							 
		$users_result=mysqli_query($mysqli,$user_qry);
		
		 
	 }
	 else
	 {
	     
	                        $user_id = sanitize($_SESSION['user_id']);

							$tableName="tbl_order";		
							$targetpage = "orders.php"; 	
							$limit = 15; 
							
							$query = "SELECT COUNT(*) as num FROM $tableName WHERE u_id='".$user_id."'";
							$total_pages = mysqli_fetch_array(mysqli_query($mysqli,$query));
							$total_pages = $total_pages['num'];
							
							$stages = 3;
							$page=0;
							if(isset($_GET['page'])){
							$page = mysqli_real_escape_string($mysqli,$_GET['page']);
							}
							if($page){
								$start = ($page - 1) * $limit; 
								$id = $user_id;
							}else{
								$start = 0;	
								$id = $user_id;
								}	
							

						 $users_qry="SELECT *,tbl_order.o_id as o_id FROM tbl_order 
		                             left join tbl_users on tbl_users.id = tbl_order.u_id
		                             LEFT JOIN tbl_items ON tbl_items.item_id = tbl_order.offer_id 
		                             WHERE u_id='".$user_id."'
			                         ORDER BY tbl_order.o_id DESC LIMIT $start, $limit";  
			                         				 
			             $users_result=mysqli_query($mysqli,$users_qry);
			             
			             if (mysqli_num_rows($users_result) == 0) {
                            $title = $client_lang['no_orders'];
                            $description = $client_lang['no_orders_description'];
                            $image = 'no_orders.gif';
                            include("nodata.php");
                            exit;
                        }
							
	 }
	
	
?>

<title><?php echo $client_lang['yourOrders']; ?></title>
 <div class="row">
      <div class="col-xs-12">
        <div class="card mrg_bottom">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php echo $client_lang['yourOrders']; ?></div>
            </div>
            <div class="col-md-7 col-xs-12">              
                  <div class="search_list">
                    <div class="search_block">
                      <form  method="post" action="">
                        <input class="form-control input-sm" placeholder="<?php echo $client_lang['searchOrders']; ?>" aria-controls="DataTables_Table_0" type="search" name="search_value" required>
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
          <div class="col-md-12 mrg-top">
            <table class="table table-striped table-bordered table-hover">
              <thead>
                <tr>
				  <th><?php echo $winners_lang['item']; ?></th>
				  <th><?php echo $client_lang['orderedOn']; ?></th>
				  <th><?php echo $winners_lang['status']; ?></th>
                  <th class="cat_action_list"></th>
                </tr>
              </thead>
              <tbody>
              	<?php
						$i=0;
						while($users_row=mysqli_fetch_array($users_result))
						{

				?>
                <tr>
                    <td><?php echo $users_row['o_name'];?></td>
                    <td><?php echo $users_row['order_date'];?></td>
                    <td><?php  if($users_row['order_status'] == 1) { echo $client_lang['orderReceived'];}
							  else if($users_row['order_status'] == 2) { echo $client_lang['orderProcessing'];}
							  else if($users_row['order_status'] == 3) { echo $client_lang['orderShipped'] ;}
							  else if($users_row['order_status'] == 4) {  echo '<span style="color: darkgreen">' . $client_lang['orderDelivered'] . '</span>';}							  
							  else if($users_row['order_status'] == 5) {  echo '<span style="color: darkred">' . $client_lang['orderRejected'] . '</span>';}	
		           ?></td>
                   <td><a href="view_order?o_id=<?php echo $users_row['o_id'];?>" class="btn btn-primary"><?php echo $client_lang['viewOrder']; ?></a>
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