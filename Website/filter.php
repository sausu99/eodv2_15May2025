<?php include('includes/header.php'); 
include("includes/session_check.php");

    include('includes/function.php');
	include('language/language.php');  

	
// Get the game_id from the URL parameter
$c_id = isset($_GET['c_id']) ? $_GET['c_id'] : 0;

	if(isset($_POST['user_search']))
	 {
		 
		
		$user_qry="SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id  WHERE ( o_date <= '".$date1."' and o_edate >= '".$date1."' ) and c_id = $c_id and o_status = 1 and
                             o_type IN (1,2,4,5,7,8)
						     and tbl_offers.o_name like '%".addslashes($_POST['search_value'])."%' ORDER BY tbl_offers.o_date ASC";  
							 
		$users_result=mysqli_query($mysqli,$user_qry);
		
		 
	 }
	 else
	 {

							$tableName="tbl_offers";		
							$targetpage = "filter.php"; 	
							$limit = 100; 
							
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
			
						 $users_qry="SELECT *
                                     FROM tbl_offers
                                     LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                                     WHERE (
                                             (o_date < '".$date1."' OR (o_date = '".$date1."' AND o_stime <= '".$time."'))
                                     AND (o_edate > '".$date1."' OR (o_edate = '".$date1."' AND o_etime >= '".$time."'))
                                         ) and c_id = $c_id
                                         AND o_status = 1
                                         AND o_type IN (1, 2, 4, 5, 7, 8)
                                     ORDER BY tbl_offers.o_edate ASC
                                     LIMIT $start, $limit;
                                     ";  
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
													 if(mysqli_num_rows($users_result) == 0)
    {
        include("norefferals.html");
        exit;
    }   
							
	 }
	 
	 
$querycategory = "SELECT * FROM tbl_cat WHERE c_id = $c_id";
$resultcategory = mysqli_query($mysqli, $querycategory);
$rowcategory = mysqli_fetch_assoc($resultcategory);
	
?>


<head>
    <link rel="stylesheet" href="assets/css/home.css">
    <title><?php echo $rowcategory['c_name']; ?></title>
</head>


<div class="row">
  <div class="col-xs-12">
    <div class="card mrg_bottom">
      <div class="page_title_block">
        <div class="col-md-5 col-xs-12">
         <div class="page_title"><?php echo $rowcategory['c_name']; ?></div>
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
  <div class="col-12">
<div class="featured-container"> 
 <div class="row justify-content-center">
  <?php while ($users_row = mysqli_fetch_assoc($users_result)) { ?>
    <div class="col-lg-4 col-md-6 col-sm-10">
      <div class="auction-item">
        <div class="image">
           <?php
    // Determine ribbon text and information URL based on o_type value
    $ribbon_text = '';
    $info_url = '';
    switch ($users_row['o_type']) {
        case 1:
            $ribbon_text = "&nbsp;&nbsp;" . $auction_lang['lowestUnique'] . "&nbsp;&nbsp;";
            $info_url = 'howitworks.html#lowest-unique-bid'; // Change this URL
            break;
        case 2:
            $ribbon_text = "&nbsp;&nbsp;" . $auction_lang['highestUnique'] . "&nbsp;&nbsp;";
            $info_url = 'howitworks.html#highest-unique-bid'; // Change this URL
            break;
        case 4:
            $ribbon_text = "&nbsp;&nbsp;" . $auction_lang['luckyDraw'] . "&nbsp;&nbsp;";
            $info_url = 'howitworks.html#lottery'; // Change this URL
            break;
        case 5:
            $ribbon_text = "&nbsp;&nbsp;" . $auction_lang['lottery'] . "&nbsp;&nbsp;";
            $info_url = 'howitworks.html#lottery'; // Change this URL
            break;
        case 7:
            $ribbon_text = "&nbsp;&nbsp;" . $auction_lang['englishAuction'] . "&nbsp;&nbsp;";
            $info_url = 'howitworks.html#english-auction'; // Change this URL
            break;
        case 8:
            $ribbon_text = "&nbsp;&nbsp;" . $auction_lang['pennyAuction'] . "&nbsp;&nbsp;";
            $info_url = 'howitworks.html#p'; // Change this URL
            break;
        default:
            $ribbon_text = "&nbsp;&nbsp;" . $auction_lang['featured'] . "&nbsp;&nbsp;";
            $info_url = 'https://example.com/default-info'; // Change this URL
    }
?>
           <span class="item_type"><?php echo $ribbon_text; ?>
                <a href="<?php echo $info_url; ?>" target="_blank"> <!-- Open link in new tab -->
                    <i class="fas fa-info-circle info-icon" style="margin-right: 5px;"></i> <!-- Information icon -->
                </a>
            </span>
          <a href="<?php
              if ($users_row['o_type'] == 1 || $users_row['o_type'] == 2) {
                echo 'unique.php';
              } elseif ($users_row['o_type'] == 4 || $users_row['o_type'] == 5) {
                echo 'raffle.php';
              } elseif ($users_row['o_type'] == 7) {
                echo 'auction.php';
              } elseif ($users_row['o_type'] == 8) {
                echo 'auctions.php';
              } else {
                echo 'live.php';
              }
              ?>?game_id=<?php echo $users_row['o_id']; ?>">
            <img src="<?php echo '../seller/images/'.$users_row['o_image']; ?>" class="img-fluid img-thumbnail" onerror="this.onerror=null;this.src='placeholder.jpg';" vertical-align: middle;" alt="<?php echo $users_row['o_name']; ?>">
          </a>
          <?php if ($users_row['is_featured'] == 1) { ?>
            <a href="#" class="star">
              <i class="far fa-star"></i>
            </a>
          <?php } ?>
          <?php if ($users_row['is_auction'] == 1) { ?>
            <a href="#" class="hammer">
              <i class="flaticon-hammer"></i>
            </a>
          <?php } ?>
          <div class="auction-timer">
                <p><?php echo $auction_lang['endsIn']; ?>:</p>
        <div class="countdown" id="timer<?php echo $users_row['o_id']; ?>">
            <?php
                // Calculate time left for each item
                $endDateTime = strtotime($users_row['o_edate'] . ' ' . $users_row['o_etime']);
                $currentDateTime = strtotime(date('Y-m-d H:i:s'));
                $timeLeft = $endDateTime - $currentDateTime;
                ?>
                
                <script>
    // Countdown timer script
    // Calculate remaining time for this item
    var remainingTime<?php echo $users_row['o_id']; ?> = <?php echo $timeLeft; ?>;

    function countdown<?php echo $users_row['o_id']; ?>() {
        var timer = document.getElementById('timer<?php echo $users_row['o_id']; ?>');
        var days = Math.floor(remainingTime<?php echo $users_row['o_id']; ?> / (60 * 60 * 24));
        var hours = Math.floor((remainingTime<?php echo $users_row['o_id']; ?> % (60 * 60 * 24)) / (60 * 60));
        var minutes = Math.floor((remainingTime<?php echo $users_row['o_id']; ?> % (60 * 60)) / (60));
        var seconds = Math.floor(remainingTime<?php echo $users_row['o_id']; ?> % (60));

        // Display the remaining time
        timer.innerHTML = '<h4><span id="days<?php echo $users_row['o_id']; ?>"></span>D : <span id="hours<?php echo $users_row['o_id']; ?>"></span>H : <span id="minutes<?php echo $users_row['o_id']; ?>"></span>M : <span id="seconds<?php echo $users_row['o_id']; ?>"></span>S</h4>';

        // Update the countdown every second
        if (remainingTime<?php echo $users_row['o_id']; ?> <= 0) {
            timer.innerHTML = '<h4>Expired</h4>';
        } else {
            document.getElementById('days<?php echo $users_row['o_id']; ?>').textContent = days;
            document.getElementById('hours<?php echo $users_row['o_id']; ?>').textContent = hours;
            document.getElementById('minutes<?php echo $users_row['o_id']; ?>').textContent = minutes;
            document.getElementById('seconds<?php echo $users_row['o_id']; ?>').textContent = seconds;
            remainingTime<?php echo $users_row['o_id']; ?>--;
            setTimeout(countdown<?php echo $users_row['o_id']; ?>, 1000);
        }
    }

    countdown<?php echo $users_row['o_id']; ?>(); // Start countdown for this item
</script>
        </div>
    </div>
        </div>
        <div class="auction-content">
          <h5><?php echo $users_row['o_name']; ?></h5>
          <h6 class="description"><?php echo $users_row['o_desc']; ?></h6>
          <?php if ($users_row['o_type'] == 1 || $users_row['o_type'] == 2) { ?>
            <div class="current-bid d-flex">
              <i class="flaticon-hammer"></i>
              <p class="d-flex flex-column bold-text"><?php echo $auction_lang['startingBid']; ?>:
                <span><?php echo $rowtime['currency'].$users_row['o_min']; ?></span>
              </p>
            </div>
          <?php } elseif ($users_row['o_type'] == 4 || $users_row['o_type'] == 5) { ?>
            <div class="current-bid d-flex">
              <i class="flaticon-hammer"></i>
              <p class="d-flex flex-column bold-text"><?php echo $auction_lang['ticketPrice']; ?>:
                <span><?php echo $users_row['o_qty']; ?>&nbsp;<i class="fi fi-rs-coins"></i></span>
              </p>
            </div>
          <?php } elseif ($users_row['o_type'] == 7 || $users_row['o_type'] == 8) { ?>
            <div class="current-bid d-flex">
              <i class="flaticon-hammer"></i>
              <p class="d-flex flex-column bold-text"><?php echo $auction_lang['currentBid']; ?>:
                <span><?php echo $rowtime['currency'].$users_row['o_min']; ?></span>
              </p>
            </div>
          <?php } ?>
        </div>
        <div class="button text-center">
          <a href="<?php
                                if ($users_row['o_type'] == 1 || $users_row['o_type'] == 2) {
                                    echo 'unique.php';
                                } elseif ($users_row['o_type'] == 4 || $users_row['o_type'] == 5) {
                                    echo 'raffle.php';
                                } elseif ($users_row['o_type'] == 7) {
                                    echo 'auction.php';
                                } elseif ($users_row['o_type'] == 8) {
                                    echo 'auctions.php';
                                } else {
                                    echo 'live.php';
                                }
                            ?>?game_id=<?php echo $users_row['o_id']; ?>"><?php 
                                if ($users_row['o_type'] == 1 || $users_row['o_type'] == 2 || $users_row['o_type'] == 7 || $users_row['o_type'] == 8) {
                                    echo '<i class="fas fa-gavel"></i>' .' '. $auction_lang['bidNow'];
                                } elseif ($users_row['o_type'] == 4 || $users_row['o_type'] == 5) {
                                    echo '<i class="fas fa-ticket-alt"></i>' .' '. $auction_lang['buyTicket'];
                                } else {
                                    echo 'Play';
                                }
                                ?></a>
        </div>
      </div>
    </div>
  <?php } ?>
</div>
</div>
</div>
      </div>
      <div class="col-md-12 col-xs-12">
        <div class="pagination_item_block">
          <!-- Pagination controls can be added here -->
        </div>
      </div>
      <div class="clearfix"></div>
    </div>
  </div>
</div>

<?php include('includes/footer.php');?>