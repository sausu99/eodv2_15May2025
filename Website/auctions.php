<?php
include('includes/header.php');
include('includes/function.php');
include('language/language.php');

// Fetch timezone and currency from settings
$querytime = "SELECT timezone, currency FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
$currency = $rowtime['currency'];
date_default_timezone_set($rowtime['timezone']);
$time = date('H:i:s');
$date1 = date('Y-m-d');

// Initialize o_type filter
$o_type_filter = "";
if (isset($_GET['filter_type']) && $_GET['filter_type'] != "") {
    $filter_otype = intval($_GET['filter_type']);
    $o_type_filter = "AND o_type = " . intval($_GET['filter_type']);
}

if (isset($_POST['user_search'])) {
    $user_qry = "SELECT * FROM tbl_offers 
                 LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                 WHERE (o_date <= '$date1' AND o_edate >= '$date1') 
                   AND o_status = 1 
                   $o_type_filter
                   AND tbl_offers.o_name LIKE '%" . addslashes($_POST['search_value']) . "%' 
                 ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";

    $users_result = mysqli_query($mysqli, $user_qry);
} else {
    $tableName = "tbl_offers";
    $targetpage = "auctions.php";
    $limit = isset($_GET['filter_type']) && $_GET['filter_type'] != "" ? "150" : "15";

    // Get total number of active offers
    $query = "SELECT COUNT(*) as num FROM $tableName 
              WHERE (o_date < '$date1' OR (o_date = '$date1' AND o_stime <= '$time')) 
                AND (o_edate > '$date1' OR (o_edate = '$date1' AND o_etime >= '$time')) 
                AND o_status = 1 
                AND o_type IN (1, 2, 7, 8, 10, 11) 
                $o_type_filter";
    $total_pages = mysqli_fetch_array(mysqli_query($mysqli, $query));
    $total_pages = $total_pages['num'];

    $stages = 3;
    $page = 0;
    
    if (isset($_GET['page'])) {
        $page = mysqli_real_escape_string($mysqli, $_GET['page']);
    }
    if ($page) {
        $start = ($page - 1) * $limit;
    } else {
        $start = 0;
    }

    // Fetch offers with pagination
    $users_qry = "SELECT * FROM tbl_offers 
                  LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id 
                  WHERE (o_date < '$date1' OR (o_date = '$date1' AND o_stime <= '$time')) 
                    AND (o_edate > '$date1' OR (o_edate = '$date1' AND o_etime >= '$time')) 
                    AND o_status = 1 
                    AND o_type IN (1, 2, 7, 8, 10, 11) 
                    $o_type_filter 
                  ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC 
                  LIMIT $start, $limit;";

    $users_result = mysqli_query($mysqli, $users_qry);
}
?>

<head>
    <link rel="stylesheet" href="assets/css/home.css">
    <title><?php echo $auction_lang['auctions']; ?></title>
</head>

<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>

<div class="card mrg_bottom card-width-fix">
    <div class="page_title_block">
        <div class="col-md-5 col-xs-12">
            <?php
            if (isset($_GET['filter_type']) && $_GET['filter_type'] != "") {
                $filter_type = intval($_GET['filter_type']);
                switch ($filter_type) {
                    case 1:
                        echo '<div class="page_title">Unique Auction (Lowest Unique Bid)</div>';
                        echo '<p>' . $client_lang['howLub2'] . '</p>';
                        break;
                    case 2:
                        echo '<div class="page_title">Unique Auction (Highest Unique Bid)</div>';
                        echo '<p>' . $client_lang['howHub2'] . '</p>';
                        break;
                    case 7:
                        echo '<div class="page_title">Simple Auction</div>';
                        echo '<p>' . $client_lang['howSimple2'] . '</p>';
                        break;
                    case 8:
                        echo '<div class="page_title">Penny Auction</div>';
                        echo '<p>' . $client_lang['howPenny2'] . '</p>';
                        break;
                    case 10:
                        echo '<div class="page_title">Reverse Auction</div>';
                        break;
                    case 11:
                        echo '<div class="page_title">Slot Auction</div>';
                        break;
                    default:
                        echo '<div class="page_title">Auction</div>';
                        echo '<p>' . $client_lang['defaultDescription'] . '</p>'; // Fallback description
                        break;
                }
            } else {
                echo '<div class="page_title">' . $auction_lang['auctions'] . '</div>';
                echo '<p>' . $client_lang['liveAuctionDesc'] . '</p>';
            }
            ?>
        </div>
        <div class="col-md-7 col-xs-12">
            <div class="search_list">
                <div class="search_block">
                    <!--<form method="post" action="">-->
                    <!--    <input class="form-control input-sm" placeholder="<?php echo $auction_lang['searchAuction']; ?>"-->
                    <!--        aria-controls="DataTables_Table_0" type="search" name="search_value" required>-->
                    <!--    <button type="submit" name="user_search" class="btn-search"><i class="fa fa-search"></i></button>-->
                    <!--</form>-->
                </div>
                <div class="filter_block">
                      <!--removed .php-->
                   <!-- Removed .php extension from action URL -->
                    <form method="get" action="auctions" id="filterForm">
                        <select name="filter_type" class="form-control input-sm" onchange="this.form.submit();">
                            <option value=""><?php echo $client_lang['liveAuctions']; ?></option>
                            <option value="1" <?php echo (isset($_GET['filter_type']) && $_GET['filter_type'] == 1) ? 'selected' : ''; ?>>
                                Lowest Unique Bid Wins
                            </option>
                            <option value="2" <?php echo (isset($_GET['filter_type']) && $_GET['filter_type'] == 2) ? 'selected' : ''; ?>>
                                Highest Unique Bid Wins
                            </option>
                            <option value="7" <?php echo (isset($_GET['filter_type']) && $_GET['filter_type'] == 7) ? 'selected' : ''; ?>>
                                Highest Bidder Wins
                            </option>
                            <option value="8" <?php echo (isset($_GET['filter_type']) && $_GET['filter_type'] == 8) ? 'selected' : ''; ?>>
                                Last Bidder Wins
                            </option>
                            <option value="10" <?php echo (isset($_GET['filter_type']) && $_GET['filter_type'] == 10) ? 'selected' : ''; ?>>
                                Lowest Bidder Wins
                            </option>
                            <option value="11" <?php echo (isset($_GET['filter_type']) && $_GET['filter_type'] == 11) ? 'selected' : ''; ?>>
                                One Lucky Bidder Wins
                            </option>
                        </select>
                    </form>
                </div>
            </div>
        </div>
        <div class="clearfix"></div>
    </div>

    <div class="row mrg-top">
        <div class="col-md-12">
            <div class="col-md-12 col-sm-12">
                <?php if (isset($_SESSION['msg'])) { ?>
                    <div class="alert alert-success alert-dismissible" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">×</span>
                        </button>
                        <?php echo $client_lang[$_SESSION['msg']]; ?>
                    </div>
                    <?php unset($_SESSION['msg']);
                } ?>
            </div>
        </div>
    </div>
  <div class="col-md-12 mrg-top">
    <div class="row">
      <div class="col-12">
        <div class="featured-container">
          <div class="row justify-content-center auction-fix">
              <?php if (mysqli_num_rows($users_result) == 0) {

                $title = $client_lang['no_auctions'];
                $description = $client_lang['no_auctions_description'];
                $image = 'no_person.gif';
                include("nodata.php");
                exit;
    
              }?>
            <?php while ($users_row = mysqli_fetch_assoc($users_result)) { ?>
              <div class="col-lg-4 col-md-6 col-sm-10  auction-width-fix">
                <div class="auction-item">
                  <?php
                    $item_id = $users_row["item_id"];
                    $user_id = $_SESSION["user_id"];
                    
                    $sSql = "SELECT * FROM tbl_wishlist WHERE item_id = ? AND user_id = ?";
                    $stmt = $mysqli->prepare($sSql);
                    $stmt->bind_param("ii", $item_id, $user_id);
                    $stmt->execute();
                    $result = $stmt->get_result();
                    
                    if ($result->num_rows > 0) {
                      $wishlistSrc = "images/static/heart-filled.svg";
                    }
                    else
                    {
                      $wishlistSrc = "images/static/heart.svg";
                    }
                  ?>
                <img src="<?php echo $wishlistSrc; ?>" data="<?php echo $users_row['item_id']; ?>" id="wishlist" alt="wishlist" title="Wishlist" onclick="AddWishlist(this);" />

                  <div class="image flex-lottery-fix">
                    <a href="<?php
                      echo 'auction';
                      $lowercaseString = strtolower($users_row["o_name"]);
                    $finalString = str_replace(' ', '-', $lowercaseString);
                      ?>/<?php echo $finalString; ?>/<?php echo $users_row['o_id']; ?>">
                        
                      <img src="placeholder.jpg" data-src="<?php echo 'seller/images/thumbs/' . $users_row['o_image']; ?>"
                        class="lazyload img-fluid img-thumbnail" alt="<?php echo $users_row['o_name']; ?>"
                        style="vertical-align: middle;">
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
                      <p><?php echo $auction_lang['auctionEnds']; ?>:</p>
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
                                var minutes = Math.floor((remainingTime<?php echo $users_row['o_id']; ?> % (60 * 60)) / 60);
                                var seconds = Math.floor(remainingTime<?php echo $users_row['o_id']; ?> % 60);
                            
                                // Display the remaining time based on the condition
                                if (days > 0) {
                                    timer.innerHTML = '<h4><span id="days<?php echo $users_row['o_id']; ?>"></span> Days : <span id="hours<?php echo $users_row['o_id']; ?>"></span> Hours</h4>';
                                    document.getElementById('days<?php echo $users_row['o_id']; ?>').textContent = days;
                                    document.getElementById('hours<?php echo $users_row['o_id']; ?>').textContent = hours;
                                } else if (hours > 0) {
                                    timer.innerHTML = '<h4><span id="hours<?php echo $users_row['o_id']; ?>"></span> Hours : <span id="minutes<?php echo $users_row['o_id']; ?>"></span> Minutes</h4>';
                                    document.getElementById('hours<?php echo $users_row['o_id']; ?>').textContent = hours;
                                    document.getElementById('minutes<?php echo $users_row['o_id']; ?>').textContent = minutes;
                                } else if (minutes > 0) {
                                    timer.innerHTML = '<h4><span id="minutes<?php echo $users_row['o_id']; ?>"></span> Minutes : <span id="seconds<?php echo $users_row['o_id']; ?>"></span> Seconds</h4>';
                                    document.getElementById('minutes<?php echo $users_row['o_id']; ?>').textContent = minutes;
                                    document.getElementById('seconds<?php echo $users_row['o_id']; ?>').textContent = seconds;
                                } else if (seconds > 0) {
                                    timer.innerHTML = '<h4><span id="seconds<?php echo $users_row['o_id']; ?>"></span> Seconds</h4>';
                                    document.getElementById('seconds<?php echo $users_row['o_id']; ?>').textContent = seconds;
                                } else {
                                    timer.innerHTML = '<h4>00:00:00</h4>';
                                    // Refresh the page when the countdown ends
                                    location.reload();
                                }
                            
                                // Update the countdown every second
                                if (remainingTime<?php echo $users_row['o_id']; ?> > 0) {
                                    remainingTime<?php echo $users_row['o_id']; ?>--;
                                    setTimeout(countdown<?php echo $users_row['o_id']; ?>, 1000);
                                }
                            }
                            
                            countdown<?php echo $users_row['o_id']; ?>(); // Start countdown for this item
                        </script>
                      </div>
                    </div>
                     <!-- Auction Type Information -->
                     <div class="auction-type" style="padding-top: 5px;">
                       <i class="fa fa-info-circle"></i>
                       <span>
                         <?php
                         switch ($users_row['o_type']) {
                           case 1:
                             echo "Lowest Unique Bid Wins";
                             break;
                           case 2:
                             echo "Highest Unique Bid Wins";
                             break;    
                           case 7:
                             echo "Highest Bid Wins";
                             break;
                           case 8:
                             echo "Last Bidder Wins";
                             break;
                           case 10:
                             echo "Lowest Bidder Wins";
                             break;
                           case 11:
                             echo "One Lucky Bidder Wins";
                             break;
                           default:
                             echo "Auction";
                             break;
                         }
                         ?>
                       </span>
                     </div>

                  </div>
                  <div class="auction-content">
                    <h5><?php echo $users_row['o_name']; ?></h5>
                    <h6 class="description"><?php echo $users_row['o_desc']; ?></h6>
                    <?php if ($users_row['o_type'] == 1) { ?>
                      <div class="current-bid d-flex">
                        <i class="flaticon-hammer"></i>
                        <p class="d-flex flex-column bold-text"><?php echo $auction_lang['startingBid']; ?>:
                          <span><?php echo $currency . $users_row['o_min']; ?></span>
                        </p>
                      </div>
                    <?php } else if ($users_row['o_type'] == 2) { ?>
                      <div class="current-bid d-flex">
                        <i class="flaticon-hammer"></i>
                        <p class="d-flex flex-column bold-text"><?php echo $client_lang['maximumBid']; ?>:
                          <span><?php echo $currency . $users_row['o_max']; ?></span>
                        </p>
                      </div>
                    <?php }elseif ($users_row['o_type'] == 7 || $users_row['o_type'] == 8 || $users_row['o_type'] == 10) { ?>
                      <div class="current-bid d-flex">
                        <i class="flaticon-hammer"></i>
                        <p class="d-flex flex-column bold-text"><?php echo $auction_lang['currentBid']; ?>:
                          <span><?php echo $currency . $users_row['o_min']; ?></span>
                        </p>
                      </div>
                    <?php } elseif ($users_row['o_type'] == 11) { ?>
                      <div class="current-bid d-flex">
                        <i class="flaticon-hammer"></i>
                        <p class="d-flex flex-column bold-text"><?php echo $auction_lang['slot_price']; ?>:
                          <span><?php echo $users_row['o_amount'].' '.$auction_row['coins']; ?></span>
                        </p>
                      </div>
                    <?php } ?>
                  </div>
                  <div class="button text-center">
                    <a href="<?php
                    if ($users_row['o_type'] == 1 || $users_row['o_type'] == 2 || $users_row['o_type'] == 7 || $users_row['o_type'] == 8 || $users_row['o_type'] == 10 || $users_row['o_type'] == 11) {
                      echo 'auction';
                      $lowercaseString = strtolower($users_row["o_name"]);
                      $finalString = str_replace(' ', '-', $lowercaseString);
                      ?>/<?php echo $finalString; ?>/<?php echo $users_row['o_id']; ?>">
                    <?php
                    }
                    else {
                          //<!--removed .php-->
                      echo 'auctions';
                    ?>?id=<?php echo $users_row['o_id']; ?>">
                    <?php } ?>
                    <?php
                         echo '<i class="fas fa-gavel"></i>' . ' ' . $auction_lang['bidNow'];
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
        <nav>
          <?php if (!isset($_POST["search"])) {
            include ("pagination.php");
          } ?>
        </nav>
      </div>
    </div>
    <div class="clearfix"></div>
  </div>
</div>

<script>
  function AddWishlist(item)
  {
    const id = item.getAttribute("data");

    $.ajax({
        url: 'insert_wishlist.php',
        type: 'POST',
        data: {
            id: id
        },
        success: function(response) {
          if (response.includes("Inserted"))
          {
            item.src = "images/static/heart-filled.svg";
          }
          else if (response.includes("Removed"))
          {
            item.src = "images/static/heart.svg";
          }
          else
          {
            window.location.href = "/login.php";
          }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("Some issue on the way.");
        }
    });
  }
</script>


<?php include ('includes/footer.php'); ?>