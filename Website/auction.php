<?php
include ("language/language.php");
include ('includes/function.php');
include ('includes/header.php');
include ("includes/connection.php");

$auction_id = $_GET['id'];
$query = "
    SELECT o.*, i.o_name, i.o_desc, i.o_image, i.o_image1, i.o_image2, i.o_image3, i.o_image4
    FROM tbl_offers o 
    JOIN tbl_items i ON o.item_id = i.item_id 
    WHERE o_id = ?";
$stmt = $mysqli->prepare($query);
$stmt->bind_param("i", $auction_id);
$stmt->execute();
$result = $stmt->get_result();
$auction = $result->fetch_assoc();
$item_id = $auction['item_id'];

if (isset($_SESSION['user_id'])) {
    $current_user = array("user_id" => $_SESSION["user_id"]);
    echo "<script>const currentUser = " . $current_user["user_id"] . ";</script>";
} else {
    echo "<script>const currentUser = '-1';</script>";
    $current_user = array("user_id" => -1);
}

$settingsQuery = "SELECT currency FROM tbl_settings";
$settingsResult = mysqli_query($mysqli, $settingsQuery);
$settingsRow = mysqli_fetch_assoc($settingsResult);

$currency = $settingsRow['currency'];

$bid_query = "
    SELECT b.*, u.name, u.image, o.o_type
    FROM tbl_bid b
    JOIN tbl_users u ON b.u_id = u.id
    JOIN tbl_offers o ON b.o_id = o.o_id
    WHERE b.o_id = ?
    ORDER BY bd_id DESC";
$bid_stmt = $mysqli->prepare($bid_query);
$bid_stmt->bind_param("i", $auction['o_id']);
$bid_stmt->execute();
$bid_result = $bid_stmt->get_result();
$bids = [];
while ($row = $bid_result->fetch_assoc()) {
    $bids[] = $row;
}

// Fetch reviews for the auction
$review_query = "
    SELECT r.*, u.name, u.image 
    FROM tbl_reviews r 
    JOIN tbl_users u ON r.user_id = u.id 
    WHERE r.item_id = ?";
$review_stmt = $mysqli->prepare($review_query);
$review_stmt->bind_param("i", $item_id);
$review_stmt->execute();
$review_result = $review_stmt->get_result();
$reviews = [];
while ($row = $review_result->fetch_assoc()) {
    $reviews[] = $row;
}


function getTimezone()
{
    global $mysqli;
    $stmt = $mysqli->query("SELECT timezone FROM tbl_settings");
    return $stmt->fetch_column();
}

$timezone = getTimezone();
$timezoneObj = new DateTimeZone($timezone);

$start_datetime = new DateTime($auction['o_date'] . ' ' . $auction['o_stime'], $timezoneObj);
$end_datetime = new DateTime($auction['o_edate'] . ' ' . $auction['o_etime'], $timezoneObj);
$current_datetime = new DateTime('now', $timezoneObj);

$status = $current_datetime < $start_datetime ? 'upcoming-offer' : ($current_datetime < $end_datetime ? 'active-offer' : 'ended-offer');

function displayAuctionDetails($auction)
{
    global $status, $auction_lang, $client_lang, $currency;

    echo '<div class="auction-details">';
    echo '<h2>' . $auction['o_name'] . ' <span class="wishlist-icon"><img src="/images/static/heart.svg" id="wishlist" alt="' . $client_lang['wishlist'] . '" title="' . $client_lang['wishlist'] . '"></span></h2>';
    
    if (in_array($auction['o_type'], ['1', '2','7','8','10'])) {
        echo '<p><span>'.$auction_lang['bidding_fees'].': '. $auction['o_amount'] .' '. '</span><i class="fas fa-coins"></i></p>';
    }
    else if (in_array($auction['o_type'], ['11'])) {
        echo '<p><span>'.$auction_lang['slot_price'].': '. $auction['o_amount'] .' '. '</span><i class="fas fa-coins"></i>'. $auction_lang['per_slot'] .'</p>';
    }

    $startDateTime = new DateTime($auction['o_sdate'] . ' ' . $auction['o_stime']);
    $endDateTime = new DateTime($auction['o_edate'] . ' ' . $auction['o_etime']);
    $currentDateTime = new DateTime();

    if ($status == "upcoming-offer") {
        $startDateTime = new DateTime($auction['o_date'] . ' ' . $auction['o_stime']);
        $interval = $currentDateTime->diff($startDateTime);

        $days = $interval->days;
        $hours = $interval->h;
        $minutes = $interval->i;
        $seconds = $interval->s;

        echo '<p>'.$client_lang['startsIn'].': '.'<span id="countdown-timer">' . $days . " ".$auction_lang['days'].": " . $hours . " ".$auction_lang['hours'].": " . $minutes . " ".$auction_lang['minutes'].": " . $seconds . " ".$auction_lang['seconds'] . '</span></p>';
        echo '<script>
            var startDateTime = new Date("' . $startDateTime->format('Y-m-d H:i:s') . '").getTime();
            var countdownTimer = setInterval(function() {
                var now = new Date().getTime();
                var distance = startDateTime - now;
                var days = Math.floor(distance / (1000 * 60 * 60 * 24));
                var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                var seconds = Math.floor((distance % (1000 * 60)) / 1000);
                document.getElementById("countdown-timer").innerHTML = days + " " + "' . $auction_lang['days'] . '" + " : " + hours + " " + "' . $auction_lang['hours'] . '" + " : " + minutes + " " + "' . $auction_lang['minutes']. '" + " : " + seconds + " " + "' . $auction_lang['seconds'] . '";
            }, 1000);
        </script>';
    } elseif ($status != "ended-offer") {
        $interval = $currentDateTime->diff($endDateTime);

        $days = $interval->days;
        $hours = $interval->h;
        $minutes = $interval->i;
        $seconds = $interval->s;

        echo '<p>'.$auction_lang['auctionEnds'].': '.'<span id="countdown-timer">' . $days . " ".$auction_lang['days'].": " . $hours . " ".$auction_lang['hours'].": " . $minutes . " ".$auction_lang['minutes'].": " . $seconds . " ".$auction_lang['seconds'] . '</span></p>';
        echo '<script>
            var endDateTime = new Date("' . $endDateTime->format('Y-m-d H:i:s') . '").getTime();
            var countdownTimer = setInterval(function() {
                var now = new Date().getTime();
                var distance = endDateTime - now;
                if (distance < 0) {
                    clearInterval(countdownTimer);
                    document.getElementById("countdown-timer").innerHTML = "Ended";
                } else {
                    var days = Math.floor(distance / (1000 * 60 * 60 * 24));
                    var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                    var seconds = Math.floor((distance % (1000 * 60)) / 1000);
                    document.getElementById("countdown-timer").innerHTML = days + " " + "' . $auction_lang['days'] . '" + " : " + hours + " " + "' . $auction_lang['hours'] . '" + " : " + minutes + " " + "' . $auction_lang['minutes']. '" + " : " + seconds + " " + "' . $auction_lang['seconds'] . '";
                }
            }, 1000);
        </script>';
    }

    $o_name = $auction['o_name'];
    $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";
    $current_url = $protocol . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'];

    if ($status != "ended-offer") {
        echo '<div class="social-sharing-buttons">';
        echo '<a href="https://www.facebook.com/sharer/sharer.php?u=' . urlencode($current_url) . '" target="_blank" class="social-btn facebook"><i class="fab fa-facebook-f"></i>' . $client_lang['facebook_share'] . '</a>';
        echo '<a href="https://twitter.com/intent/tweet?url=' . urlencode($current_url) . '&text=' . urlencode($o_name) . '" target="_blank" class="social-btn twitter"><i class="fab fa-twitter"></i>' . $client_lang['twitter_tweet'] . '</a>';
        echo '<a href="https://www.linkedin.com/sharing/share-offsite/?url=' . urlencode($current_url) . '" target="_blank" class="social-btn linkedin"><i class="fab fa-linkedin-in"></i>' . $client_lang['linkedin_share'] . '</a>';
        echo '<a href="mailto:?subject=' . urlencode($o_name) . '&body=' . urlencode($current_url) . '" target="_blank" class="social-btn email"><i class="fas fa-envelope"></i>' . $client_lang['email_share'] . '</a>';
        echo '<a href="https://api.whatsapp.com/send?text=' . urlencode($current_url) . '" target="_blank" class="social-btn whatsapp"><i class="fab fa-whatsapp"></i>' . $client_lang['whatsapp_share'] . '</a>';
        echo '</div>';
    }

    //echo '<p>Actual Price: <span>'.$currency.' '. $auction['o_price'] . '</span></p>';

    // if (in_array($auction['o_type'], ['8'])) {
    //     echo '<p>' . $client_lang['startingBid'] . ': <span>'.$currency.' '. $auction['o_min'] . '</span></p>';
    // }
    
     if (in_array($auction['o_type'], ['1', '2'])) {
        echo '<p>' . $client_lang['minimumBid'] . ': <span>'.$currency.' '. $auction['o_min'] . '</span></p>';
    }

    if (in_array($auction['o_type'], ['1', '2', '7', '8'])) {
        echo '<p>' . $client_lang['maximumBid'] . ': <span>'.$currency.' '. $auction['o_max'] . '</span></p>';
    }

    echo '</div>';
}

function displayHowItWorksSection($auction) {
    global $client_lang;

    echo '<div class="how-it-works-section">';
    //echo '<h2>' . $client_lang['howItWorks'] . '</h2>';

    // Determine the auction type and display corresponding steps
    switch ($auction['o_type']) {
        
        case '1':
            echo '<ol class="how-it-works-list">';
            echo '<li>' . $client_lang['lub_auction_1'] . $auction['o_amount']. '&nbsp; <i class="fas fa-coins"></i></strong></li>';
            echo '<li>' . $client_lang['lub_auction_2'] . '</li>';
            echo '<li>' . $client_lang['lub_auction_3'] . '</li>';
            echo '<li>' . $client_lang['lub_auction_4'] . '</li>';
            echo '<li>' . $client_lang['lub_auction_5'] . '</li>';
            echo '<li>' . $client_lang['lub_auction_6'] . '</li>';
            echo '<li>' . $client_lang['lub_auction_7'] . '</li>';
            echo '</ol>';
            break;
            
        case '2':
            echo '<ol class="how-it-works-list">';
            echo '<li>' . $client_lang['hub_auction_1'] . $auction['o_amount']. '&nbsp; <i class="fas fa-coins"></i></strong></li>';
            echo '<li>' . $client_lang['hub_auction_2'] . '</li>';
            echo '<li>' . $client_lang['hub_auction_3'] . '</li>';
            echo '<li>' . $client_lang['hub_auction_4'] . '</li>';
            echo '<li>' . $client_lang['hub_auction_5'] . '</li>';
            echo '<li>' . $client_lang['hub_auction_6'] . '</li>';
            echo '<li>' . $client_lang['hub_auction_7'] . '</li>';
            echo '</ol>';
            break;
        
        case '7':
            echo '<ol class="how-it-works-list">';
            echo '<li>' . $client_lang['normal_auction_1'] . $auction['o_amount']. '&nbsp; <i class="fas fa-coins"></i></strong></li>';
            echo '<li>' . $client_lang['normal_auction_2'] . '</li>';
            echo '<li>' . $client_lang['normal_auction_3'] . '</li>';
            echo '<li>' . $client_lang['normal_auction_4'] . '</li>';
            echo '<li>' . $client_lang['normal_auction_5'] . '</li>';
            echo '<li>' . $client_lang['normal_auction_6'] . '</li>';
            echo '</ol>';
            break;
        
        case '8':
            echo '<ol class="how-it-works-list">';
            echo '<li>' . $client_lang['penny_auction_1'] . $auction['o_amount']. '&nbsp; <i class="fas fa-coins"></i></strong></li>';
            echo '<li>' . $client_lang['penny_auction_2'] . '</li>';
            echo '<li>' . $client_lang['penny_auction_3'] . '</li>';
            echo '<li>' . $client_lang['penny_auction_4'] . '</li>';
            echo '<li>' . $client_lang['penny_auction_5'] . '</li>';
            echo '<li>' . $client_lang['penny_auction_6'] . '</li>';
            echo '<li>' . $client_lang['penny_auction_7'] . '</li>';
            echo '</ol>';
            break;
            
        case '10':
            echo '<ol class="how-it-works-list">';
            echo '<li>' . $client_lang['reverse_auction_1'] . $auction['o_amount']. '&nbsp; <i class="fas fa-coins"></i></strong></li>';
            echo '<li>' . $client_lang['reverse_auction_2'] . '</li>';
            echo '<li>' . $client_lang['reverse_auction_3'] . '</li>';
            echo '<li>' . $client_lang['reverse_auction_4'] . '</li>';
            echo '<li>' . $client_lang['reverse_auction_5'] . '</li>';
            echo '<li>' . $client_lang['reverse_auction_6'] . '</li>';
            echo '</ol>';
            break;
            
        case '11':
            echo '<ol class="how-it-works-list">';
            echo '<li>' . $client_lang['slot_auction_1'] . $auction['o_amount']. '&nbsp; <i class="fas fa-coins"></i></strong></li>';
            echo '<li>' . $client_lang['slot_auction_2'] . '</li>';
            echo '<li>' . $client_lang['slot_auction_3'] . '</li>';
            echo '<li>' . $client_lang['slot_auction_4'] . '</li>';
            echo '</ol>';
            break;
    }

    echo '</div>';
}


function displayBidSection($auction)
{
    global $current_user, $status, $auction_lang, $invest_lang, $start_datetime, $currency, $mysqli, $auction_id;

    echo '<div class="bid-section">';

    if ($status == "active-offer") {
        if ($current_user["user_id"] == -1) {
          echo '<p class="social-btn email" style="color: white; cursor: pointer;" onmouseover="this.style.color=\'red\';" onmouseout="this.style.color=\'white\';" onclick="window.location.href=\'/login\';"> ⚠️ ' . $auction_lang['not_logged_in_auction'] . '</p>';
        } else {
            if ($auction['o_status'] == 1) {
                if (in_array($auction['o_type'], ['7', '8'])) {
                    echo '<p>'.$auction_lang['currentBid'].': '.'<span>' .$currency. $auction['o_min'] . '</span></p>';
                }
                if (in_array($auction['o_type'], ['10'])) {
                    echo '<p>'.$auction_lang['currentBid'].': '.'<span>' .$currency. $auction['o_max'] . '</span></p>';
                }

                echo '<p id="place-bid-message"></p>';

                if (in_array($auction['o_type'], ['1', '2'])) {
                    echo "<form id='place-bid-form' method='post' action='/insert_bid.php'>";
                    echo '<span>' . $currency . '</span><input class="place-bid-input" type="number" name="bidValue" step="0.01" min="' . $auction['o_min'] . '" max="' . $auction['o_max'] . '" placeholder="' . $auction['o_min'] . '">';
                    echo '<input type="hidden" name="gameId" value="' . $auction_id . '" />';
                    echo '<button type="submit" class="place-bid-button">'.$auction_lang['placeBid'].'</button>';
                    echo "</form>";
                }
                
                if (in_array($auction['o_type'], ['7', '10'])) {
                    echo "<form id='place-bid-form' method='post' action='/insert_bid.php'>";
                    echo '<span>' . $currency . '</span><input class="place-bid-input" type="number" name="bidValue" step="1" min="' . $auction['o_min'] . '" max="' . $auction['o_max'] . '">';
                    echo '<input type="hidden" name="gameId" value="' . $auction_id . '" />';
                    echo '<button type="submit" class="place-bid-button">'.$auction_lang['placeBid'].'</button>';
                    echo "</form>";
                }

                if ($auction['o_type'] == '8') {
                    echo "<form id='place-bid-form' method='post' action='/insert_bid.php'>";
                    echo '<input type="hidden" name="gameId" value="' . $auction_id . '" />';
                    echo '<button type="submit" class="place-bid-button">'.$auction_lang['placeBid'].'</button>';
                    echo "</form>";
                }

                if ($auction['o_type'] == '11') {
                    echo "<form id='place-bid-form' method='post' action='/insert_bid.php'>";
                    echo '<input type="number" name="bidValue" min="1" max="99999" value="1">';
                    echo '<input type="hidden" name="gameId" value="' . $auction_id . '" />';
                    echo '<button class="place-bid-button">'.$auction_lang['buySlot'].'</button>';
                    echo "</form>";
                }
            }
        }
    } elseif ($status == "upcoming-offer") {
        echo '<button class="social-btn linkedin">'.$auction_lang['not_started_auction'].' '.'&nbsp;<i class="fas fa-clock"></i></button>';
    } elseif ($status == "ended-offer") {
        
        global $currency;
        
        $winner_query = "SELECT u.name AS winner_name, winning_value FROM tbl_offers o JOIN tbl_users u ON o.winner_id = u.id WHERE o.o_id = ? LIMIT 1";
    $winner_stmt = $mysqli->prepare($winner_query);
    $winner_stmt->bind_param("i", $auction['o_id']);
    $winner_stmt->execute();
    $winner_result = $winner_stmt->get_result();
    $winner = $winner_result->fetch_assoc();
    
    if ($winner) {
        echo '<p id="winner">'.$auction_lang['wonBy'].': ' . htmlspecialchars($winner['winner_name']) .' '.$invest_lang['for'].' '. $currency.htmlspecialchars($winner['winning_value']) .'</p>';
    }
    else {
        echo '<p id="winner">'.$auction_lang['no_auction_winner'].'</p>';
    }
    $winner_stmt->close();
    echo '<p class="btn btn-danger" style="color: white">'.$auction_lang['auctionEnded'].'</p>';
    }

    echo '</div>';
}

function displayAuctionDetailsSection($auction)
{
    global $status, $start_datetime, $auction_lang, $end_datetime, $currency;

    if ($status == "active-offer") {
        $statusContent = $auction_lang['auctionLive'];
    } else if ($status == "upcoming-offer") {
        $statusContent = $auction_lang['notStarted'];
    } else {
        $statusContent = $auction_lang['auctionEndedStatus'];
    }

    echo '<div class="auction-details-s">';
    echo '<table class="details-table">';
    echo '<div class="' . $status . '">' . $statusContent . '</div>';
    echo '<tr><th>'.$auction_lang['actualPrice'].': </th> <td> '.$currency . $auction['o_price'] . '</td></tr>';
    
    if (in_array($auction['o_type'], ['1', '2', '7', '8', '10'])) {
    echo '<tr><th>'.$auction_lang['bidRange'].': </th><td>'.$currency . htmlspecialchars($auction['o_min']) . ' - '.$currency . htmlspecialchars($auction['o_max']) . '</td></tr>';
    }
    
    echo '<tr><th>'.$auction_lang['startTime'].': </th><td>' . $start_datetime->format('d F Y H:i:s') . '</td></tr>';
    echo '<tr><th>'.$auction_lang['endTime'].': </th><td>' . $end_datetime->format('d F Y H:i:s') . '</td></tr>';
    echo '</table>';
    echo '</div>';
}

function displayBidHistory($bids, $o_type, $current_user)
{
    echo '<div class="bid-history">';
    echo '<h3>' . $client_lang['bidHistory'] . '</h3>';
    $showNone = true;

    if (in_array($o_type, [1, 2])) {
        foreach ($bids as $bid) {
            if ($bid['u_id'] == $_SESSION['user_id']) {
                displayBidEntryUnique($bid);
                $showNone = false;
            }
        }
    } elseif (in_array($o_type, [7, 8, 10])) {
        foreach ($bids as $bid) {
            displayBidEntry($bid);
            $showNone = false;
        }
    }

    if ($showNone)
    {
        echo '<p>' . $client_lang['noUserBids'] . '</p>';
    }
    echo '</div>';
}

function displayBidEntry($bid)
{
    global $currency;
    
    $image = !empty($bid['image']) ? 'seller/images/'.$bid['image'] : 'placeholder_user.jpg';
    echo '<div class="bid-entry">';
    echo '<img src="/' . htmlspecialchars($image) . '" alt="User Image" class="user-image">';
    echo '<span class="user-name" id="' . $bid["u_id"] . '">' . htmlspecialchars($bid['name']) . '</span>';
    echo '<span class="bid-value">'.$currency . htmlspecialchars($bid['bd_value']) . '</span>';
    echo '<span class="bid-date">' . htmlspecialchars($bid['bd_date']) . '</span>';
    echo '</div>';
}

function displayBidEntryUnique($bid)
{
    global  $client_lang, $currency;
    
    $image = !empty($bid['image']) ? 'seller/images/'.$bid['image'] : 'placeholder_user.jpg';
    
    // Determine bid status text
    $bidStatus = '';
    switch ($bid['bid_status']) {
        case 1:
            if ($bid['o_type'] == 1) {
                $bidStatus = '' . $client_lang['lowestNotUniqueBid'] . '';
                $statusColor = 'badge badge-primary'; // Define color in CSS or use a specific hex code
            } elseif ($bid['o_type'] == 2) {
                $bidStatus = '' . $client_lang['highestNotUniqueBid'] . '';
                $statusColor = 'badge badge-primary'; // Define color in CSS or use a specific hex code
            }
            break;
        case 2:
            $bidStatus = '' . $client_lang['notUniqueBid'] . '';
            $statusColor = 'badge badge-danger'; // Define color in CSS or use a specific hex code
            break;
        case 3:
            $bidStatus = '' . $client_lang['uniqueBid'] . '';
            $statusColor = 'badge badge-success'; // Define color in CSS or use a specific hex code
            break;
        default:
            $bidStatus = 'Unknown Status';
            $statusColor = 'black'; // Default color
            break;
    }
    
    echo '<div class="bid-entry">';
    echo '<img src="/' . htmlspecialchars($image) . '" alt="User Image" class="user-image">';
    echo '<span class="user-name" id="' . $bid["u_id"] . '">' . htmlspecialchars($bid['name']) . '</span>';
    echo '<span class="bid-value">' . $currency . htmlspecialchars($bid['bd_value']) . '</span>';
    echo '<span class="' . $statusColor . '">' . htmlspecialchars($bidStatus) . '</span>';
    echo '</div>';
}


function displayLeaderboard($leaderboard)
{
    global $currency;
    
    if
    (!empty($leaderboard)) {

        echo '<div class="leaderboard">';
        echo '<h3>' . $client_lang['leaderboard'] . '</h3>';
        foreach ($leaderboard as $entry) {
            echo '<div class="leaderboard-entry">';
            echo '<span class="user-name">' . htmlspecialchars($entry['name']) . '</span>';
            echo '<span class="bid-value">'.$currency . htmlspecialchars($entry['bid_value']) . '</span>';
            echo '</div>';
        }
        echo '</div>';
    }
}

function displayAuctionTabs($auction, $bids, $leaderboard, $current_user)
{
    echo '<div id="AuctionDetails" class="tabcontent">';
    displayAuctionDetailsSection($auction);
    echo '</div>';
    
    echo '<div id="HowItWorks" class="tabcontent">';
    displayHowItWorksSection($auction);
    echo '</div>';

    echo '<div id="ProductDetails" class="tabcontent">';
    echo '<p>' . htmlspecialchars($auction['o_desc']) . '</p>';
    echo '</div>';

    echo '<div id="BidHistory" class="tabcontent">';
    if (in_array($auction['o_type'], [1, 2])) {
        displayBidHistory($bids, $auction['o_type'], $current_user);
        displayLeaderboard($leaderboard);
    } elseif (in_array($auction['o_type'], [7, 8, 10])) {
        displayBidHistory($bids, $auction['o_type'], $current_user);
    }
    echo '</div>';

    echo '</div>';
    
    echo '<div id="review-box" class="tabcontent">';
    echo '<h3>' . $client_lang['reviews'] . '</h3>';
    if (!empty($reviews)) {
        foreach ($reviews as $review) {
            echo '<div class="review-entry">';
            $user_image = !empty($review['image']) ? 'seller/images/'.$review['image'] : 'placeholder_user.jpg';
            echo '<img src="/' . htmlspecialchars($user_image) . '" alt="User Image" class="user-image">';
            echo '<span class="user-name">' . htmlspecialchars($review['name']) . '</span>';
            echo '<span class="review-rating">' . str_repeat('★', $review['rating']) . '</span>';
            echo '<p class="review-comment">' . htmlspecialchars($review['comment']) . '</p>';
            echo '<span class="review-date">' . htmlspecialchars($review['created_at']) . '</span>';
            echo '</div>';
        }
    } else {
        echo '<p>' . $client_lang['noReviews'] . '</p>';
    }
    echo '</div>';

}

function getAuctionImages($auction)
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

?>


<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $auction['o_name']; ?></title>
    <base href="/">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/vendor.css">
    <link rel="stylesheet" type="text/css" href="assets/css/flat-admin.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue-sky.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/blue.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/red.css">
    <link rel="stylesheet" type="text/css" href="assets/css/theme/yellow.css">
    <link rel="stylesheet" href="assets/css/picture-preview-slider.css" />
    <link rel="stylesheet" type="text/css" href="assets/css/auction.css" />

    <link rel="icon" href="images/profile.png" type="image/x-icon">

    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/picture-preview-slider.js"></script>
    <script src="assets/js/auction.js" defer></script>
</head>

<body>
    <div class="auction-container">
        <div id="slider-container"></div>
        <?php
        if ($auction) {
            echo '<script>';
            echo 'const sliderImages = ' . json_encode(getAuctionImages($auction)) . ';';
            echo '</script>';
            ?>
            <div class="auction-info">
                <?php
                displayAuctionDetails($auction);
                displayBidSection($auction);

                echo '<div class="tabs">
                <button class="tablinks active" onclick="openTab(event, \'how-it-works\')">' . $footer_lang['footerHow'] . '</button>
                <button class="tablinks" onclick="openTab(event, \'auction-details-s\')">' . $client_lang['auctionDetails'] . '</button>
                <button class="tablinks" onclick="openTab(event, \'product-details\')">' . $client_lang['productDetails'] . '</button>
                <button class="tablinks" onclick="openTab(event, \'bid-history\')">' . $client_lang['bidHistory'] . '</button>
                <button class="tablinks" onclick="openTab(event, \'review-box\')">' . $client_lang['reviews'] . '</button>
                </div>';
                
                echo '<div id="how-it-works" class="tabcontent">';
                displayHowItWorksSection($auction);
                echo '</div>';

                echo '<div id="auction-details-s" class="tabcontent">';
                displayAuctionDetailsSection($auction);
                echo '</div>';

                echo '<div id="product-details" class="tabcontent">';
                echo '<p>' . htmlspecialchars($auction['o_desc']) . '</p>';
                echo '</div>';

                echo '<div id="bid-history" class="tabcontent">';
                
                if (!in_array($auction['o_type'], [1, 2])) {
                    echo '<div class="switch-container">
                    <input type="checkbox" id="toggle-switch" class="switch-input" onclick="filterCurrentUserBids(this)">
                    <label for="toggle-switch" class="switch-label"></label>
                    <span class="switch-text">' . $client_lang['enableMyBids'] . '</span>
                    </div>';
                }

                if (in_array($auction['o_type'], [1, 2])) {
                    displayBidHistory($bids, $auction['o_type'], $current_user);
                    displayLeaderboard($leaderboard);
                } elseif (in_array($auction['o_type'], [7, 8, 10])) {
                    displayBidHistory($bids, $auction['o_type'], $current_user);
                }
                echo '</div>';
                
                echo '<div id="review-box" class="tabcontent">';
                echo '<h3>' . $client_lang['reviews'] . '</h3>';
                if (!empty($reviews)) {
                    foreach ($reviews as $review) {
                        echo '<div class="review-entry">';
                        $user_image = !empty($review['image']) ? 'seller/images/'.$review['image'] : 'placeholder_user.jpg';
                        echo '<img src="/' . htmlspecialchars($user_image) . '" alt="User Image" class="user-image">';
                        echo '<span class="user-name">' . htmlspecialchars($review['name']) . '</span>';
                        echo '<span class="review-rating">' . str_repeat('★', $review['rating']) . '</span>';
                        echo '<p class="review-comment">' . htmlspecialchars($review['comment']) . '</p>';
                        echo '<span class="review-date">' . htmlspecialchars($review['created_at']) . '</span>';
                        echo '</div>';
                    }
                } else {
                    echo '<p>' . $client_lang['noReviews'] . '</p>';
                }
                echo '</div>';

                ?>
            </div> 
            <?php
        } else {
            echo '<p>' . $client_lang['auctionNotFound'] . '</p>';
        }
        ?>
    </div>
</body>


</html>

<?php
include ("includes/footer.php");

$stmt->close();
$bid_stmt->close();
$review_stmt->close();
$mysqli->close();
?>