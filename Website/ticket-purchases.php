<?php include('includes/header.php'); 

    include('includes/function.php');
	include('language/language.php'); 
	
	$user_id = isset($_SESSION['user_id']) ? intval($_SESSION['user_id']) : 0;




							$tableName="tbl_ticket";		
							$targetpage = "ticket-purchases.php"; 	
							$limit = 15; 
				
		                    $query = "SELECT COUNT(*) as num FROM $tableName left join tbl_offers on tbl_offers.o_id = tbl_ticket.o_id
		                     LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE tbl_ticket.u_id = '$user_id'";
		                
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
							

                            $users_qry = "SELECT * FROM tbl_ticket
                                LEFT JOIN tbl_users ON tbl_users.id  = $user_id
                                LEFT JOIN tbl_offers ON tbl_offers.o_id = tbl_ticket.o_id
                                LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
                                LEFT JOIN tbl_lottery_balls ON tbl_lottery_balls.lottery_balls_id = tbl_offers.lottery_balls_id
                                WHERE tbl_ticket.u_id = '$user_id'
                                ORDER BY tbl_ticket.ticket_id DESC LIMIT $start, $limit";  
                        
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
							if (mysqli_num_rows($users_result) == 0) {
                                $title = $client_lang['no_ticket'];
                                $description = $client_lang['no_ticket_description'];
                                $image = 'no_ticket.gif';
                                include("nodata.php");
                                exit;
                            }	
?>
<head>
<title><?php echo $client_lang['ticket_purchases']; ?></title>
</head>
<style>
.ticket-numbers {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
}

.ticket-ball {
    margin-right: 10px; /* Add space between balls */
    position: relative;
    width: 30px; /* Set the size of the ball container */
    height: 30px;
}

.ticket-ball img {
    width: 100%; /* Make the image fill the container */
    height: 100%;
}

.ticket-number {
    font-size: 14px; /* Adjust the size of the ticket numbers */
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #000; /* Change the text color to white */
    font-weight: bold;
    text-align: center;
}
</style>

 <div class="row">
      <div class="col-xs-12">
        <div class="card mrg_bottom">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
               <div class="page_title"><?php echo $client_lang['ticket_purchases']; ?></div>
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
                  <th><?php echo $client_lang['ticket']; ?></th>
                  <th><?php echo $client_lang['ticket_purchased_on']; ?></th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
              	<?php
						$i=0;
						while($users_row=mysqli_fetch_array($users_result))
						{
						    $blueBalls = $users_row['normal_ball_limit'];
							$goldBalls = $users_row['premium_ball_limit'];
							$totalBalls = $blueBalls+$goldBalls;

				?>
                <tr> 
                    <td class="ticket-numbers">
                        <?php
                        // Loop through each ball attribute (assuming 8 balls per ticket)
                        for ($j = 1; $j <= $totalBalls; $j++) {
                            $ballValue = $users_row['ball_'.$j];
                            $ballImage = ($j <= $goldBalls) ? 'golden_ball.png' : 'blue_ball.png';
                            echo '<div class="ticket-ball">';
                            echo '<img src="admin/images/static/'.$ballImage.'" alt="'.$ballValue.'" title="'.$ballValue.'" />';
                            echo '<span class="ticket-number">'.$ballValue.'</span>';
                            echo '</div>';
                        }
                        ?>
                    </td>
                    <td><?php echo $users_row['purchase_date'];?><br><?php echo $invest_lang['for'].': '; ?><?php echo $users_row['ticket_price'].' '.$auction_lang['coins'];?></td> 
                    <td>
                        <a href="lottery/seeLottery/<?php echo $users_row['o_id'];?>" class="btn btn-success">
                            <i class="fa fa-eye"></i>&nbsp;<?php echo $client_lang['see_lottery']; ?>
                        </a>
                        <button class="btn blue-btn" onclick="printTicket('<?php echo $users_row['unique_ticket_id']; ?>')">
                            <i class="fa fa-print"></i>&nbsp;Print
                        </button>
                    </td>
                    <script>
                        function printTicket(uniqueTicketId) {
                            window.open('print_ticket.php?ticket_id=' + uniqueTicketId, '_blank');
                        }
                    </script>
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