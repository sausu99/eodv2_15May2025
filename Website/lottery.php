<?php
include ("language/language.php");
include ("includes/connection.php");
include ('includes/function.php');
include ('includes/header.php');

// Get the current page URL
$website_link = "https://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";

$lottery_id = $_GET['id'];
$query = "SELECT tbl_offers.*, tbl_items.*, COUNT(tbl_ticket.ticket_id) AS total_bids FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id LEFT JOIN tbl_ticket ON tbl_ticket.o_id = tbl_offers.o_id WHERE tbl_offers.o_id = $lottery_id AND (tbl_offers.o_type = 4 OR tbl_offers.o_type = 5) GROUP BY tbl_offers.o_id;";
$result = mysqli_query($mysqli, $query);
$lottery = mysqli_fetch_assoc($result);

$querytime = "SELECT timezone,currency, app_name FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
date_default_timezone_set($rowtime['timezone']);
$time = date('H:i:s');
$date1 = date('Y-m-d');
$app_name = $rowtime['app_name'];
$timezone = $rowtime['timezone'];

if (isset($_SESSION['user_id'])) {
    $current_user = array("user_id" => $_SESSION["user_id"]);
    echo "<script>const currentUser = " . $current_user["user_id"] . ";</script>";
} else {
    echo "<script>const currentUser = '-1';</script>";
    $current_user = array("user_id" => -1);
}

$timezoneObj = new DateTimeZone($timezone);

$start_datetime = new DateTime($lottery['o_date'] . ' ' . $lottery['o_stime'], $timezoneObj);
$end_datetime = new DateTime($lottery['o_edate'] . ' ' . $lottery['o_etime'], $timezoneObj);
$current_datetime = new DateTime('now', $timezoneObj);

$status = $current_datetime < $start_datetime ? 'upcoming-offer' : ($current_datetime < $end_datetime ? 'active-offer' : 'ended-offer');

function getLotteryImages($auction)
{
    $images = ['o_image', 'o_image1', 'o_image2', 'o_image3', 'o_image4'];

    $finalImages = array();

    foreach ($images as $image) {
        if (!empty($auction[$image])) {
            array_push($finalImages, '/seller/images/' . $auction[$image]);
        }
    }

    if (empty($finalImages)) {
        array_push($finalImages, "placeholder.jpg");
    }

    return $finalImages;
}

echo '<script>';
echo 'const sliderImages = ' . json_encode(getLotteryImages($lottery)) . ';';
echo '</script>';

function showLotteryCard($row_lottery)
{
    global $auction_lang, $mysqli, $rowtime;
    ?>
    <div class="lottery-card-extra">
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
                            var remainingTime<?php echo $row_lottery['o_id']; ?> = <?php echo $timeLeft; ?>;
                            function countdown<?php echo $row_lottery['o_id']; ?>() {
                                var timer = document.getElementById('timer<?php echo $row_lottery['o_id']; ?>');
                                var days = Math.floor(remainingTime<?php echo $row_lottery['o_id']; ?> / (60 * 60 * 24));
                                var hours = Math.floor((remainingTime<?php echo $row_lottery['o_id']; ?> % (60 * 60 * 24)) / (60 * 60));

                                timer.innerHTML = '<h4><span id="days<?php echo $row_lottery['o_id']; ?>"></span> Days : <span id="hours<?php echo $row_lottery['o_id']; ?>"></span> Hours</h4>';

                                if (remainingTime<?php echo $row_lottery['o_id']; ?> <= 0) {
                                    timer.innerHTML = '<h4>Expired</h4>';
                                } else {
                                    document.getElementById('days<?php echo $row_lottery['o_id']; ?>').textContent = days;
                                    document.getElementById('hours<?php echo $row_lottery['o_id']; ?>').textContent = hours;
                                    remainingTime<?php echo $row_lottery['o_id']; ?>--;
                                    setTimeout(countdown<?php echo $row_lottery['o_id']; ?>, 1000);
                                }
                            }
                            countdown<?php echo $row_lottery['o_id']; ?>();
                        </script>
                    </div>
                </div>
                <a class="lottery-fix-a" href="<?php
                if ($row_lottery['o_type'] == 4 || $row_lottery['o_type'] == 5) {
                    echo 'lottery/seeLottery/';
                } else {
                    echo 'lottery/seeLottery/';
                }
                ?><?php echo $row_lottery['o_id']; ?>">
                    <img src="<?php echo '/seller/images/' . $row_lottery['o_image']; ?>"
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

            $query = "SELECT 
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
                ?>lottery/seeLottery/<?php echo $row_lottery['o_id']; ?>">
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
    <?php
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $lottery["o_name"]; ?></title>
    <base href="/">
    
    <!-- Open Graph Meta Tags -->
    <meta property="og:title" content="<?php echo htmlspecialchars($lottery["o_name"]); ?>" />
    <meta property="og:description" content="<?php echo htmlspecialchars($lottery["o_desc"]); ?>" />
    <meta property="og:image" content="<?php echo 'https://rifando.live/seller/images/' . htmlspecialchars($lottery['o_image']); ?>" />
    <meta property="og:url" content="<?php echo htmlspecialchars($website_link); ?>" />
    <meta property="og:type" content="website" />
    <meta property="og:site_name" content="<?php echo htmlspecialchars($app_name); ?>" />
    
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/vendor.css">
    <link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue-sky.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/red.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/yellow.css">
    <link rel="stylesheet" href="assets/css/picture-preview-slider.css" />
    <link rel="stylesheet" href="assets/css/lottery.css" />

    <link rel="icon" href="images/profile.png" type="image/x-icon">
    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/picture-preview-slider-lottery.js"></script>
    <script src="assets/js/lottery.js" defer></script>
</head>

<body>
    <?php
    $qry_lottery_all = "SELECT *
                        FROM tbl_offers
                        LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
                        WHERE (
                                (o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                        AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "'))
                            )
                            AND o_status = 1
                            AND o_type IN (4, 5)
                            AND tbl_offers.o_id <> " . $lottery_id. " 
                        ORDER BY tbl_offers.o_edate ASC
                        LIMIT 0, 20;
                        ";
    $result_lottery_all = mysqli_query($mysqli, $qry_lottery_all);
    ?>

    <div class="main-layout-handler-main-page">
        <div class="hide-handler">
            <?php $firstCard = mysqli_fetch_assoc($result_lottery_all); ?>
            <?php showLotteryCard($firstCard); ?>
        </div>
        <div class="lottery-main-container">
            <div class="lottery-center-main-container">
                <div class="lottery-timer-container">
                    <p><?php
                    if ($status == "upcoming-offer") {
                        echo $client_lang["lottery_start"].':';
                    } else if ($status == "active-offer") {
                        echo $client_lang["lottery_end"].':';
                    } else if ($status == "ended-offer") {
                        echo $client_lang["lottery_ended"].':';
                    }
                    ?></p>
                    <?php

                    if ($status == "upcoming-offer") {
                        $endDateTime = $start_datetime;
                        $currentDateTime = new DateTime();

                        $interval = $currentDateTime->diff($endDateTime);

                        $days = $interval->days;
                        $hours = $interval->h;
                        $minutes = $interval->i;
                        $seconds = $interval->s;

                        echo '<p><div id="countdown-timer"><div><span>' . $days . '</span> <span>' . $auction_lang['days'] . '</span></div><span>:</span> <div><span>' . $hours . '</span> <span>' . $auction_lang['hours'] . '</span></div> <span>:</span><div><span>' . $minutes . '</span> <span>' . $auction_lang['minutes'] . '</span></div> <span>:</span><div><span>' . $seconds . '</span> <span>' . $auction_lang['seconds'] . '</span></div></div></p>';
                        echo '<script>
                            var endDateTime = new Date("' . $start_datetime->format('Y-m-d H:i:s') . '").getTime();
                            var countdownTimer = setInterval(function() {
                                var now = new Date().getTime();
                                var distance = endDateTime - now;
                                if (distance < 0) {
                                    clearInterval(countdownTimer);
                                    document.getElementById("countdown-timer").innerHTML = "<div><span>Ended</span></div>";
                                } else {
                                    var days = Math.floor(distance / (1000 * 60 * 60 * 24));
                                    var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                                    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                                    var seconds = Math.floor((distance % (1000 * 60)) / 1000);
                                    document.getElementById("countdown-timer").innerHTML = 
                                        "<div><span>" + days + "</span> <span>' . $auction_lang['days'] . '</span></div><span>:</span> " + 
                                        "<div><span>" + hours + "</span> <span>' . $auction_lang['hours'] . '</span></div><span>:</span> " + 
                                        "<div><span>" + minutes + "</span> <span>' . $auction_lang['minutes'] . '</span></div><span>:</span> " + 
                                        "<div><span>" + seconds + "</span> <span>' . $auction_lang['seconds'] . '</span></div>";
                                }
                            }, 1000);
                        </script>';
                    } else if ($status == "active-offer")  {
                        $endDateTime = new DateTime($lottery['o_edate'] . ' ' . $lottery['o_etime']);
                        $currentDateTime = new DateTime();

                        $interval = $currentDateTime->diff($endDateTime);

                        $days = $interval->days;
                        $hours = $interval->h;
                        $minutes = $interval->i;
                        $seconds = $interval->s;

                        echo '<p><div id="countdown-timer"><div><span>' . $days . '</span> <span>' . $auction_lang['days'] . '</span></div><span>:</span> <div><span>' . $hours . '</span> <span>' . $auction_lang['hours'] . '</span></div> <span>:</span><div><span>' . $minutes . '</span> <span>' . $auction_lang['minutes'] . '</span></div> <span>:</span><div><span>' . $seconds . '</span> <span>' . $auction_lang['seconds'] . '</span></div></div></p>';
                        echo '<script>
                            var endDateTime = new Date("' . $endDateTime->format('Y-m-d H:i:s') . '").getTime();
                            var countdownTimer = setInterval(function() {
                                var now = new Date().getTime();
                                var distance = endDateTime - now;
                                if (distance < 0) {
                                    clearInterval(countdownTimer);
                                    document.getElementById("countdown-timer").innerHTML = "<div><span>Ended</span></div>";
                                } else {
                                    var days = Math.floor(distance / (1000 * 60 * 60 * 24));
                                    var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                                    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                                    var seconds = Math.floor((distance % (1000 * 60)) / 1000);
                                    document.getElementById("countdown-timer").innerHTML = 
                                        "<div><span>" + days + "</span> <span>' . $auction_lang['days'] . '</span></div><span>:</span> " + 
                                        "<div><span>" + hours + "</span> <span>' . $auction_lang['hours'] . '</span></div><span>:</span> " + 
                                        "<div><span>" + minutes + "</span> <span>' . $auction_lang['minutes'] . '</span></div><span>:</span> " + 
                                        "<div><span>" + seconds + "</span> <span>' . $auction_lang['seconds'] . '</span></div>";
                                }
                            }, 1000);
                        </script>';
                    }
                    else {
                        $end_date = $lottery['o_edate'];
                        $end_time = $lottery['o_etime'];
                        // Combine date and time into a single datetime string
                        $datetime_str = $end_date . ' ' . $end_time;
                        
                        // Create a DateTime object from the datetime string
                        $datetime = new DateTime($datetime_str);
                        
                        // Format the DateTime object to the desired format
                        $formatted_datetime = $datetime->format('jS F Y \a\t h:i A');
                        
                        // Output the formatted datetime string
                        echo '<h2 style="color: white; text-align: center;">'.$formatted_datetime.'</h2>';
                    }
                    
                    ?>
                </div>
                <div class="lottery-content-container">
                    <div class="lottery-main-content">
                        <div class="image-section">
                            <div id="slider-container"></div>
                            <div class="lower-content">
                                <?php
                                $o_name = $lottery['o_name'];
                                $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";
                                $current_url = $protocol . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'];

                                echo '<div class="social-sharing-buttons">';
                                echo '<a href="https://www.facebook.com/sharer/sharer.php?u=' . urlencode($current_url) . '" target="_blank" class="social-btn facebook"><i class="fab fa-facebook-f"></i>Share</a>';
                                echo '<a href="https://twitter.com/intent/tweet?url=' . urlencode($current_url) . '&text=' . urlencode($o_name) . '" target="_blank" class="social-btn twitter"><i class="fab fa-twitter"></i>Tweet</a>';
                                echo '<a href="mailto:?subject=' . urlencode($o_name) . '&body=' . urlencode($current_url) . '" target="_blank" class="social-btn email"><i class="fas fa-envelope"></i>Email</a>';
                                echo '<a href="https://api.whatsapp.com/send?text=' . urlencode($current_url) . '" target="_blank" class="social-btn whatsapp"><i class="fab fa-whatsapp"></i>WhatsApp</a>';
                                echo '</div>';
                                ?>
                            </div>
                        </div>
                        <!-- ONLY SHOW IF LOTTERY IS LIVE -->
                    <?php if ($status !== "ended-offer") { ?>
                        <div class="content-section">
                            <h4 class="content-section first-line"><?php echo $client_lang["lottery_enter"]?></h4>
                            <h3 class="content-section second-line"><?php echo $lottery["o_name"]; ?><span> <?php echo $client_lang["and_much_more"]?></span></h3>
                            <p class="content-section third-line"><?php echo $client_lang["lottery_limit"]?>
                                <?php echo $lottery["o_qty"]; ?> <?php echo $auction_lang["ticketPurchaseText1"]?>.
                            </p>
                            <h4 class="ticket-solid"><?php echo $client_lang["tickets_sold"]?></h4>
                            <div class="max-min-no">
                                <span><?php echo $lottery["total_bids"]; ?></span><span><?php echo $lottery["o_qty"]; ?></span>
                            </div>
                            <div class="percentage">
                                <div id="animate-slider" data="<?php echo (($lottery["total_bids"] / $lottery["o_qty"]) * 100); ?>"></div>
                            </div>
                            <p class="remaining"><?php echo $client_lang["only"]?>
                                <?php echo (int) $lottery["o_qty"] - (int) $lottery["total_bids"]; ?>
                                <?php echo $client_lang["tickets_left"]?>
                            </p>
                            <h4 class="ticket-solid"><?php echo $auction_lang["buyTicket"]; ?></h4>
                            <div class="price-seperator">
                                <span class="price"><?php echo $lottery["o_amount"];
                                if ($lottery["o_amount"] > 1) {
                                    echo ' '.$auction_lang["coins"];
                                } else {
                                    echo ' '.$client_lang["coin"];
                                }
                                ?>
                                </span>
                                <small class="price-extension"><?php echo $client_lang["per_ticket"]?></small>
                            </div>
                            <?php
                                if ($current_user["user_id"] == -1) {
                                    echo '<button type="button" class="btn btn-danger" onclick="window.location.href=\'login\'">' . $client_lang['not_logged_in_lottery'] . '</button>';
                                } 
                            ?>
                            <?php
                                if ($current_user["user_id"] !== -1) {
                                    ?>
                            <div class="style-helper-button">
                                <div class="style-helper">
                                    <span><?php echo $client_lang["quantity"]?></span>
                                    <div class="quantity-selector">
                                        <i class="fa fa-minus" id="subtract-ticket"></i>
                                        <input type="number" id="ticket-input" min="0"
                                            max="<?php echo (int) $lottery["o_qty"] - (int) $lottery["total_bids"]; ?>"
                                            step="1" readonly value=1>
                                        <span><?php echo $client_lang["ticket"]?></span>
                                        <i class="fa fa-plus" id="add-ticket"></i>
                                    </div>
                                </div>
                                <button type="button" id="buy-ticket-button" <?php if ($status == "upcoming-offer"){ echo "disabled"; }; ?> ><?php if ($status == "upcoming-offer"){ echo $auction_lang["notStarted"]; } else { echo $auction_lang["buyTicket"]; } ?></button>
                            </div>
                               <?php
                                }
                            ?>
                            <div>
                                <h4 class="ticket-solid prizes-text"><?php echo $auction_lang["lotteryPrize"]?></h4>
                                <div class="prizes-style-helper">
                                    <?php
                                    $prizesQ = "SELECT * FROM tbl_prizes LEFT JOIN tbl_items ON tbl_items.item_id = tbl_prizes.item_id  WHERE `o_id` = ?";
                                    $stmt = $mysqli->prepare($prizesQ);
                                    $stmt->bind_param("i", $lottery_id);
                                    $stmt->execute();
                                    $result = mysqli_stmt_get_result($stmt);
                                    $prizes = array();
                                
                                    while ($row = mysqli_fetch_assoc($result)) {
                                        $prizes[] = $row;
                                    }
                                
                                    foreach ($prizes as $prize) {
                                        echo "<div class='rank'>";
                                        echo "<img src='seller/images/" . $prize["o_image"] . "' alt='Lottery' width='50' height='50' />";
                                        echo "<div class='rank-details'>";
                                        echo "<p class='prize-name'>".$prize["o_name"]."</p>";
                                        echo "<p class='rank-info'>Rank ";
                                        if ($prize["rank_start"] == $prize["rank_end"]) {
                                            echo $prize["rank_start"];
                                        } else {
                                            echo $prize["rank_start"] . " - " . $prize["rank_end"];
                                        }
                                        echo "</p>";
                                        echo "</div>";
                                        echo "</div>";
                                    }
                                    ?>
                                </div>
                            </div>
                        </div>
                    <?php } ?>
                    
                    <!-- ONLY SHOW IF LOTTERY HAS ENDED -->
                    <?php if ($status == "ended-offer") { ?>
                        <div class="content-section">
                            <h4 class="content-section first-line"><?php echo $client_lang["winners_won_prizes"]?></h4>
                            <h3 class="content-section second-line"><?php echo $lottery["o_name"]; ?><span> <?php echo $client_lang["and_much_more"]?></span></h3>
                            <p class="content-section third-line"><?php echo $client_lang["top_winners_lottery"]?>
                            </p>
                            <div>
                                <h4 class="ticket-solid prizes-text"><?php echo $winners_lang["title"]?></h4>
                                <div class="prizes-style-helper">
                                <?php
                                $prizesQ = "
                                    SELECT 
                                        tbl_prizes.*, 
                                        tbl_items.o_image, 
                                        tbl_items.item_id, 
                                        tbl_items.price, 
                                        tbl_items.o_name, 
                                        tbl_winners.u_id, 
                                        COALESCE(tbl_winners.winner_rank, tbl_prizes.rank_start) AS winner_rank, 
                                        tbl_winners.winner_name, 
                                        tbl_winners.winning_value, 
                                        tbl_users.image AS user_image
                                    FROM 
                                        tbl_prizes 
                                    LEFT JOIN 
                                        tbl_items ON tbl_items.item_id = tbl_prizes.item_id 
                                    LEFT JOIN 
                                        tbl_winners ON tbl_winners.o_id = tbl_prizes.o_id
                                        AND tbl_winners.winner_rank BETWEEN tbl_prizes.rank_start AND tbl_prizes.rank_end
                                    LEFT JOIN 
                                        tbl_users ON tbl_users.id = tbl_winners.u_id 
                                    WHERE 
                                        tbl_prizes.o_id = ?
                                    ORDER BY 
                                        tbl_prizes.rank_start, tbl_prizes.rank_end";
                                $stmt = $mysqli->prepare($prizesQ);
                                $stmt->bind_param("i", $lottery_id);
                                $stmt->execute();
                                $result = mysqli_stmt_get_result($stmt);
                                $prizes = array();
                            
                                while ($row = mysqli_fetch_assoc($result)) {
                                    $prizes[] = $row;
                                }
                            
                                foreach ($prizes as $prize) {
                                    echo "<div class='rank'>";
                                    echo "<img src='seller/images/" . $prize["o_image"] . "' alt='Lottery' width='50' height='50' />";
                                    echo "<div class='rank-details'>";
                                    echo "<p class='prize-name'>".$prize["o_name"]."</p>";
                            
                                    echo "<p class='rank-info'>".$auction_lang['lotteryRank'].": " . $prize["winner_rank"] . "</p>";
                            
                                    // Display winner info if available
                                    if (!empty($prize["winner_name"])) {
                                        echo "<p class='winner-info'>".$winners_lang['winner'].": " . $prize["winner_name"] . "&nbsp;|&nbsp;".$client_lang["ticket"].": " . $prize["winning_value"] ."</p>";
                                    } else {
                                        echo "<p class='winner-info'>".$client_lang["no_winners_yet"]."</p>";
                                    }
                            
                                    echo "</div>";
                                    echo "</div>";
                                }
                                ?>
                            </div>

                            </div>
                        </div>
                    <?php } ?>
                    </div>
                </div>
            </div>
        </div>
        <div class="wrap-handler">
            <?php showLotteryCard(mysqli_fetch_assoc($result_lottery_all)); ?>
        </div>
    </div>

    <div class="secondary-page-handler">
        <div class="secondary-page-container">
            <div class="secondary-page-center-container">
                <div class="lottery-timer-container secondary-lottery-timer-container">
                    <?php
                    $endDateTime = new DateTime($lottery['o_edate'] . ' ' . $lottery['o_etime']);
                    $currentDateTime = new DateTime();

                    $interval = $currentDateTime->diff($endDateTime);

                    $days = $interval->days;
                    $hours = $interval->h;
                    $minutes = $interval->i;
                    $seconds = $interval->s;

                    echo '<p><div id="countdownsecondary-timer"><div><span>' . $days . '</span> <span>' . $auction_lang['days'] . '</span></div><span>:</span> <div><span>' . $hours . '</span> <span>' . $auction_lang['hours'] . '</span></div> <span>:</span><div><span>' . $minutes . '</span> <span>' . $auction_lang['minutes'] . '</span></div> <span>:</span><div><span>' . $seconds . '</span> <span>' . $auction_lang['seconds'] . '</span></div></div></p>';
                    echo '<script>
                    var endDateTime = new Date("' . $endDateTime->format('Y-m-d H:i:s') . '").getTime();
                    var countdownsecondaryTimer = setInterval(function() {
                        var now = new Date().getTime();
                        var distance = endDateTime - now;
                        if (distance < 0) {
                            clearInterval(countdownsecondaryTimer);
                            document.getElementById("countdownsecondary-timer").innerHTML = "<div><span>Ended</span></div>";
                        } else {
                            var days = Math.floor(distance / (1000 * 60 * 60 * 24));
                            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                            var seconds = Math.floor((distance % (1000 * 60)) / 1000);
                            document.getElementById("countdownsecondary-timer").innerHTML = 
                                "<div><span>" + days + "</span> <span>' . $auction_lang['days'] . '</span></div><span>:</span> " + 
                                "<div><span>" + hours + "</span> <span>' . $auction_lang['hours'] . '</span></div><span>:</span> " + 
                                "<div><span>" + minutes + "</span> <span>' . $auction_lang['minutes'] . '</span></div><span>:</span> " + 
                                "<div><span>" + seconds + "</span> <span>' . $auction_lang['seconds'] . '</span></div>";
                        }
                    }, 1000);
                </script>';
                    ?>
                    <div class="all-button">
                        <button type="button" class="magic-button"><i class="fa fa-magic"></i><?php echo $auction_lang["lotteryPick"]?></button>
                        <button type="button" class="delete-button"><i class="fa fa-trash"></i><?php echo $auction_lang["lotteryClear"]?></button>
                    </div>
                </div>

                <?php
                $pickQuery = "SELECT 
                            o.o_id,
                            lb.normal_ball_start,
                            lb.normal_ball_end,
                            lb.normal_ball_limit,
                            lb.premium_ball_start,
                            lb.premium_ball_end,
                            lb.premium_ball_limit
                        FROM 
                            tbl_offers o
                        JOIN 
                            tbl_lottery_balls lb ON o.lottery_balls_id = lb.lottery_balls_id
                        WHERE 
                            o.o_id = $lottery_id;";

                $result = mysqli_query($mysqli, $pickQuery);
                $picker = mysqli_fetch_assoc($result);

                $normal_ball_start = $picker["normal_ball_start"] ?? 0;
                $normal_ball_end = $picker["normal_ball_end"] ?? 0;
                $premium_ball_start = $picker["premium_ball_start"] ?? 0;
                $premium_ball_end = $picker["premium_ball_end"] ?? 0;
                $normal_ball_limit = $picker["normal_ball_limit"] ?? 0;
                $premium_ball_limit = $picker["premium_ball_limit"] ?? 0;

                echo "<script>
                    const normal_ball_start = $normal_ball_start;
                    const normal_ball_end = $normal_ball_end;
                    const premium_ball_start = $premium_ball_start;
                    const premium_ball_end = $premium_ball_end;
                    const normal_ball_limit = $normal_ball_limit;
                    const premium_ball_limit = $premium_ball_limit;
                    const max_tickets = " . (int) $lottery["o_qty"] - (int) $lottery["total_bids"] . "
                    let index = 0;
                    const successfulyOrderText = " . json_encode($client_lang["ticket_purchased"]) . ";
                    const insufficientBalanceText = " . json_encode($client_lang["insufficient_balance_ticket"]) . ";
                    const selectTickets = " . json_encode($client_lang["please_select_all_numbers"]) . ";
                    const o_id = " . $lottery['o_id'] . "
                    const perTicketPrice = " . $lottery['o_amount'] . "</script>";
                ?>

                <div class="secondary-page-content-container">
                    <?php
                    $normalHeader = "";
 
                    if ((int) $normal_ball_end != 0) {
                    $normalHeader .= $normal_ball_limit . ' ' . $client_lang["numbers"];
                    }

                    $premiumHeader = "";

                    if ((int) $premium_ball_end != 0) {
                    $premiumHeader .= " + " . $premium_ball_limit . ' ' . $client_lang["premium_numbers"];
                    }
                    ?>
                    <h4><?php echo $client_lang["choose_your"]. $normal_ball_limit + $premium_ball_limit.$client_lang["luckiest_number_choose"]; ?>
                        ( <?php echo $normalHeader . $premiumHeader; ?> ).</h4>
                    <div class="picker-style-helper">
                    </div>
                    <button type="button" class="add-more-button" id="add-button"><?php echo $client_lang["add_more"]?></button>
                </div>
            </div>
        </div>
        <div class="summary-container">
            <h2><?php echo $client_lang["summary"]?></h2>
            <p id="status-p-purchase"></p>
            <p><span><?php echo $client_lang["total_ticket"]?>:</span><span><span id="n-tickets-summary">3</span> <?php echo $client_lang["ticket"]?></span></p>
            <p><span><?php echo $client_lang["price_per_ticket"]?>:</span><span><?php echo $lottery["o_amount"]; ?> <?php echo $auction_lang["coins"]?></span></p>
            <p><span><?php echo $client_lang["total_price"]?>:</span><span><span id="total-price-summary"></span> <?php echo $auction_lang["coins"]?> (<span
                        id="calculate-tickets-summary"></span>)</span></p>
            <button type="button" class="add-more-button" id="buy-ticket-submit-button"><?php echo $auction_lang["buyTicket"]?></button>
        </div>
    </div>
</body>

</html>

<?php
include ("includes/footer.php");
?>