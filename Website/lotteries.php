<?php 
include ('includes/header.php');
include ('includes/function.php');
include ('language/language.php');


$querytime = "SELECT timezone, currency FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
$currency = $rowtime['currency'];

date_default_timezone_set($rowtime['timezone']);
$time = date('H:i:s');
$date1 = date('Y-m-d');


if (isset($_POST['user_search'])) {


  $user_qry = "SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE ( o_date <= '" . $date1 . "' and o_edate >= '" . $date1 . "' ) and o_status = 1 and
                             o_type IN (4,5)
						     and tbl_offers.o_name like '%" . addslashes($_POST['search_value']) . "%' ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC";

  $users_result = mysqli_query($mysqli, $user_qry);


} else {

  $tableName = "tbl_offers";
  $targetpage = "lotteries.php";
  $limit = 15;

  $query = "SELECT COUNT(*) as num FROM $tableName WHERE (
                                        (o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                                         AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "')))
                                         AND o_status = 1 AND o_type IN (4, 5)";
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


  $querytime = "SELECT timezone FROM tbl_settings";
  $resulttime = mysqli_query($mysqli, $querytime);
  $rowtime = mysqli_fetch_assoc($resulttime);


  date_default_timezone_set($rowtime['timezone']);
  $time = date('H:i:s');

  $date1 = date('Y-m-d');

  $users_qry = "SELECT *
                                     FROM tbl_offers
                                     LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
                                     WHERE (
                                             (o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                                     AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "'))
                                         )
                                         AND o_status = 1
                                         AND o_type IN (4, 5)
                                     ORDER BY CONCAT(tbl_offers.o_edate, ' ', tbl_offers.o_etime) ASC
                                     LIMIT $start, $limit;
                                     ";

  $users_result = mysqli_query($mysqli, $users_qry);

  if (mysqli_num_rows($users_result) == 0) {
      $title = $client_lang['no_lottery'];
      $description = $client_lang['no_lottery_description'];
      $image = 'no_person.gif';
      include("nodata.php");
      exit;
  }

}


?>


<head>
  <link rel="stylesheet" href="assets/css/home.css">
  <title><?php echo $auction_lang['lottery']?></title>
  <style>
    .auction-fix {
      column-gap: 28px;
      row-gap: 28px;
    }

    @media (max-width: 900px) {
      .auction-fix {
        column-gap: 0;
      }
    }
  </style>
</head>

<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>

<div class="card mrg_bottom card-width-fix">
  <div class="page_title_block">
    <div class="col-md-5 col-xs-12">
      <div class="page_title"><?php echo $auction_lang['lottery']?></div>
      <p><?php echo $auction_lang['auctionDescription']; ?></p>
    </div>
    <div class="col-md-7 col-xs-12">
      <div class="search_list">
        <div class="search_block">
          <form method="post" action="">
            <input class="form-control input-sm" placeholder="<?php echo $auction_lang['searchLottery']?>" aria-controls="DataTables_Table_0"
              type="search" name="search_value" required>
            <button type="submit" name="user_search" class="btn-search"><i class="fa fa-search"></i></button>
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
            <?php while ($row_lottery = mysqli_fetch_assoc($users_result)) { ?>
              <div>
                <div class="auction-item lottery-item">
                  <div class="image flex-lottery-fix">
                    <div class="auction-timer">
                      <p><?php echo $auction_lang['endsIn']; ?>:</p>
                      <div class="countdown" id="timer<?php echo $row_lottery['o_id']; ?>">
                        <?php
                        // Calculate time left for each item
                        $endDateTime = strtotime($row_lottery['o_edate'] . ' ' . $row_lottery['o_etime']);
                        $currentDateTime = strtotime(date('Y-m-d H:i:s'));
                        $timeLeft = $endDateTime - $currentDateTime;
                        ?>
                        <script>
                            // Countdown timer script
                            // Calculate remaining time for this item
                            var remainingTime<?php echo $row_lottery['o_id']; ?> = <?php echo $timeLeft; ?>;
                            
                            function countdown<?php echo $row_lottery['o_id']; ?>() {
                                var timer = document.getElementById('timer<?php echo $row_lottery['o_id']; ?>');
                                
                                var days = Math.floor(remainingTime<?php echo $row_lottery['o_id']; ?> / (60 * 60 * 24));
                                var hours = Math.floor((remainingTime<?php echo $row_lottery['o_id']; ?> % (60 * 60 * 24)) / (60 * 60));
                                var minutes = Math.floor((remainingTime<?php echo $row_lottery['o_id']; ?> % (60 * 60)) / 60);
                                var seconds = Math.floor(remainingTime<?php echo $row_lottery['o_id']; ?> % 60);
                            
                                // Display the remaining time based on the condition
                                if (days > 0) {
                                    timer.innerHTML = '<h4><span id="days<?php echo $row_lottery['o_id']; ?>"></span> Days : <span id="hours<?php echo $row_lottery['o_id']; ?>"></span> Hours</h4>';
                                    document.getElementById('days<?php echo $row_lottery['o_id']; ?>').textContent = days;
                                    document.getElementById('hours<?php echo $row_lottery['o_id']; ?>').textContent = hours;
                                } else if (hours > 0) {
                                    timer.innerHTML = '<h4><span id="hours<?php echo $row_lottery['o_id']; ?>"></span> Hours : <span id="minutes<?php echo $row_lottery['o_id']; ?>"></span> Minutes</h4>';
                                    document.getElementById('hours<?php echo $row_lottery['o_id']; ?>').textContent = hours;
                                    document.getElementById('minutes<?php echo $row_lottery['o_id']; ?>').textContent = minutes;
                                } else if (minutes > 0) {
                                    timer.innerHTML = '<h4><span id="minutes<?php echo $row_lottery['o_id']; ?>"></span> Minutes : <span id="seconds<?php echo $row_lottery['o_id']; ?>"></span> Seconds</h4>';
                                    document.getElementById('minutes<?php echo $row_lottery['o_id']; ?>').textContent = minutes;
                                    document.getElementById('seconds<?php echo $row_lottery['o_id']; ?>').textContent = seconds;
                                } else if (seconds > 0) {
                                    timer.innerHTML = '<h4><span id="seconds<?php echo $row_lottery['o_id']; ?>"></span> Seconds</h4>';
                                    document.getElementById('seconds<?php echo $row_lottery['o_id']; ?>').textContent = seconds;
                                } else {
                                    timer.innerHTML = '<h4>00:00:00</h4>';
                                    // Refresh the page when the countdown ends
                                    location.reload();
                                }
                            
                                // Update the countdown every second
                                if (remainingTime<?php echo $row_lottery['o_id']; ?> > 0) {
                                    remainingTime<?php echo $row_lottery['o_id']; ?>--;
                                    setTimeout(countdown<?php echo $row_lottery['o_id']; ?>, 1000);
                                }
                            }
                            
                            countdown<?php echo $row_lottery['o_id']; ?>(); // Start countdown for this item
                        </script>
                      </div>
                    </div>
                    <a class="lottery-fix-a" href="<?php
                    if ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) {
                      echo 'lottery';
                      $lowercaseString = strtolower($row_lottery["o_name"]);
                      $finalString = str_replace(' ', '-', $lowercaseString);
                      ?>/<?php echo $finalString; ?>/<?php echo $row_lottery['o_id']; ?>">
                        <?php
                    } else {
                      echo 'live.php';
                      ?>?game_id=<?php echo $row_lottery['o_id']; ?>">
                      <?php } ?>
                      <img src="placeholder.jpg" data-src="<?php echo '/seller/images/' . $row_lottery['o_image']; ?>"
                        class="lazyload img-fluid img-thumbnail" alt="<?php echo $row_lottery['o_name']; ?>"
                        style="vertical-align: middle;">
                    </a>
                    <div class="auction-content">
                      <h5 class="lottery-fix-h5"><?php echo $row_lottery['o_name']; ?></h5>
                      <h6 class="description lottery-description-fix"><?php echo $row_lottery['o_desc']; ?>
                      </h6>
                      <?php if ($row_lottery['o_type'] == 1 || $row_lottery['o_type'] == 2) { ?>
                        <div class="current-bid d-flex">
                          <i class="flaticon-hammer"></i>
                          <p class="d-flex flex-column bold-text"><?php echo $auction_lang['startingBid']; ?>:
                            <span><?php echo $rowtime['currency'] . $row_lottery['o_min']; ?></span>
                          </p>
                        </div>
                      <?php } elseif ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) { ?>
                        <div class="current-bid d-flex currency-lottery-fix">
                          <i class="flaticon-hammer"></i>
                          <p class="d-flex flex-column bold-text"><?php echo $auction_lang['ticketPrice']; ?>:
                            <span><?php echo $row_lottery['o_amount']; ?>&nbsp;<i class="fi fi-rs-coins"></i></span>
                          </p>
                        </div>
                      <?php } elseif ($row_lottery['o_type'] == 7 || $row_lottery['o_type'] == 8) { ?>
                        <div class="current-bid d-flex">
                          <i class="flaticon-hammer"></i>
                          <p class="d-flex flex-column bold-text"><?php echo $auction_lang['currentBid']; ?>:
                            <span><?php echo $rowtime['currency'] . $row_lottery['o_min']; ?></span>
                          </p>
                        </div>
                      <?php } ?>
                    </div>


                  </div>

                  <?php

                  $query = "
                        SELECT 
                            tbl_offers.o_id, 
                            tbl_offers.o_qty AS total_tickets,
                            COUNT(tbl_ticket.o_id) AS sold_tickets
                        FROM tbl_offers
                        LEFT JOIN tbl_ticket ON tbl_offers.o_id = tbl_ticket.o_id
                        WHERE tbl_offers.o_id = ?
                        GROUP BY tbl_offers.o_id
                    ";

                  $stmt = $mysqli->prepare($query);
                  $stmt->bind_param("i", $row_lottery['o_id']);
                  $stmt->execute();
                  $result = $stmt->get_result();

                  if ($result->num_rows > 0) {
                    while ($row = $result->fetch_assoc()) {
                      $o_id = $row['o_id'];
                      $totalTickets = $row['total_tickets'];
                      $soldTickets = $row['sold_tickets'];

                      if ($totalTickets > 0) {
                        $percentageSold = ($soldTickets / $totalTickets) * 100;
                      } else {
                        $percentageSold = 0;
                      }
                      echo "<div class='super-parent-progress'>";
                      echo "<div class='progress-parent-container'>";
                      echo "<div style='height: 100%; width: " . $percentageSold . "%; border-radius: 9999px; background-color: #37f5f9;'></div>";
                      echo "</div>";
                      echo "<div style='text-align: center; color: white;'>" . number_format($percentageSold, 2) . "%</div>";
                      echo "</div>";
                    }
                  } else {
                    echo "No data found for the specified o_id.";
                  }

                  $stmt->close();
                  ?>

                  <div class="button text-center">
                    <a class="button-lottery-fix" href="<?php
                    if ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) {
                      echo 'lottery';
                      $lowercaseString = strtolower($row_lottery["o_name"]);
                      $finalString = str_replace(' ', '-', $lowercaseString);
                      ?>/<?php echo $finalString; ?>/<?php echo $row_lottery['o_id']; ?>">
                        <?php
                    } else {
                      if ($row_lottery['o_type'] == 1 || $row_lottery['o_type'] == 2) {
                        echo 'unique.php';
                      } else if ($row_lottery['o_type'] == 7) {
                        echo 'auction.php';
                      } elseif ($row_lottery['o_type'] == 8) {
                        echo 'auctions.php';
                      } else {
                        echo 'live.php';
                      }
                      ?>?game_id=<?php echo $row_lottery['o_id']; ?>">
                      <?php } ?>

                      <?php
                      if ($row_lottery['o_type'] == 1 || $row_lottery['o_type'] == 2 || $row_lottery['o_type'] == 7 || $row_lottery['o_type'] == 8) {
                        echo '<i class="fas fa-gavel"></i> ' . $auction_lang['bidNow'];
                      } elseif ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) {
                        echo '<i class="fas fa-ticket-alt"></i> ' . $auction_lang['buyTicket'];
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
      function AddWishlist(item) {
        const id = item.getAttribute("data");

        $.ajax({
          url: 'insert_wishlist.php',
          type: 'POST',
          data: {
            id: id
          },
          success: function (response) {
            if (response.includes("Inserted")) {
              item.src = "images/static/heart-filled.svg";
            }
            else if (response.includes("Removed")) {
              item.src = "images/static/heart.svg";
            }
            else {
              window.location.href = "/login.php";
            }
          },
          error: function (jqXHR, textStatus, errorThrown) {
            alert("Some issue on the way.");
          }
        });
      }
    </script>


    <?php include ('includes/footer.php'); ?>