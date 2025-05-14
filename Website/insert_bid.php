<?php
session_start();
include("includes/connection.php");
include("language/language.php");

if (!isset($_SESSION['user_id'])) {
    echo $auction_lang['notloggedIn'];
    exit;
}

$user_id = $_SESSION['user_id'];
$bidValue = filter_input(INPUT_POST, 'bidValue', FILTER_SANITIZE_NUMBER_FLOAT, FILTER_FLAG_ALLOW_FRACTION);
$formatted_bid_value = number_format(floatval($bidValue), 2, '.', '');
$game_id = filter_input(INPUT_POST, 'gameId', FILTER_SANITIZE_NUMBER_INT);

// Fetch user details
$qry = $mysqli->prepare("SELECT * FROM tbl_users WHERE id = ?");
$qry->bind_param("i", $user_id);
$qry->execute();
$session_row = $qry->get_result()->fetch_assoc();
$user_name = htmlspecialchars($session_row['name'], ENT_QUOTES, 'UTF-8');

$language = htmlspecialchars($session_row['language'], ENT_QUOTES, 'UTF-8');
$current_date = date('Y-m-d');

// Fetch settings
$settingsQuery = "SELECT currency, timezone, admin_email, app_name FROM tbl_settings";
$settingsResult = mysqli_query($mysqli, $settingsQuery);
$settingsRow = mysqli_fetch_assoc($settingsResult);

date_default_timezone_set($settingsRow['timezone']);
$currency = htmlspecialchars($settingsRow['currency'], ENT_QUOTES, 'UTF-8');
$admin_email = htmlspecialchars($settingsRow['admin_email'], ENT_QUOTES, 'UTF-8');
$app_name = htmlspecialchars($settingsRow['app_name'], ENT_QUOTES, 'UTF-8');

// Fetch auction details
$auctionQuery = $mysqli->prepare("SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE o_id = ?");
$auctionQuery->bind_param("i", $game_id);
$auctionQuery->execute();
$auctionRow = $auctionQuery->get_result()->fetch_assoc();

if (!$auctionRow) {
    echo $auction_lang['auctionNotFound'];
    exit;
}

$o_type = htmlspecialchars($auctionRow['o_type'], ENT_QUOTES, 'UTF-8');
$o_amount = htmlspecialchars($auctionRow['o_amount'], ENT_QUOTES, 'UTF-8');
$o_name = htmlspecialchars($auctionRow['o_name'], ENT_QUOTES, 'UTF-8');
$current_winner_id = htmlspecialchars($auctionRow['winner_id'], ENT_QUOTES, 'UTF-8');
$current_winner_bid = htmlspecialchars($auctionRow['winning_value'], ENT_QUOTES, 'UTF-8');
$o_date = htmlspecialchars($auctionRow['o_date'], ENT_QUOTES, 'UTF-8');
$o_stime = htmlspecialchars($auctionRow['o_stime'], ENT_QUOTES, 'UTF-8');
$o_edate = htmlspecialchars($auctionRow['o_edate'], ENT_QUOTES, 'UTF-8');
$o_etime = htmlspecialchars($auctionRow['o_etime'], ENT_QUOTES, 'UTF-8');
$currentBid = htmlspecialchars($auctionRow['o_min'], ENT_QUOTES, 'UTF-8');
$bidIncrement = htmlspecialchars($auctionRow['bid_increment'], ENT_QUOTES, 'UTF-8');
$timeIncrement = htmlspecialchars($auctionRow['time_increment'], ENT_QUOTES, 'UTF-8');

$start = $o_date . " " . $o_stime;
$end = $o_edate . " " . $o_etime;

$datetime = date('Y-m-d H:i:s');

if ($datetime < $start || $datetime > $end) {
    echo $auction_lang['auctionEnded'];
    exit;
}

// Fetch user's wallet balance
$check_balance_query = $mysqli->prepare("SELECT wallet FROM tbl_users WHERE id = ? FOR UPDATE");
$check_balance_query->bind_param("i", $user_id);
$check_balance_query->execute();
$balance_row = $check_balance_query->get_result()->fetch_assoc();

if ($balance_row['wallet'] < $o_amount) {
    echo $auction_lang['insufficientBalance'];
    exit;
}

function sendEmailNotification($userId, $o_name,$app_name, $new_status, $mysqli, $admin_email) {
    // Fetch user email
    $user_email_query = $mysqli->prepare("SELECT name, email FROM tbl_users WHERE id = ?");
    $user_email_query->bind_param("i", $userId);
    $user_email_query->execute();
    $user_email_row = $user_email_query->get_result()->fetch_assoc();

    $user_name = htmlspecialchars($user_email_row['name'], ENT_QUOTES, 'UTF-8');
    $user_email = htmlspecialchars($user_email_row['email'], ENT_QUOTES, 'UTF-8');

    // Email content
    $to = $user_email;
    $subject = $client_lang['bidStatusUpdate'].' '. htmlspecialchars($o_name, ENT_QUOTES, 'UTF-8');
    $message = '
        <html>
        <head>
            <title>' . htmlspecialchars($client_lang['bidStatusUpdate'], ENT_QUOTES, 'UTF-8') . '</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f4;
                    margin: 0;
                    padding: 0;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: #ffffff;
                    padding: 20px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                .header {
                    text-align: center;
                    padding: 10px 0;
                    border-bottom: 1px solid #dddddd;
                }
                .content {
                    margin: 20px 0;
                    text-align: center;
                }
                .footer {
                    text-align: center;
                    padding: 10px 0;
                    border-top: 1px solid #dddddd;
                    font-size: 12px;
                    color: #aaaaaa;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Bid Status Update</h1>
                </div>
                <div class="content">
                    <p>Hello ' . $user_name . ',</p>
                    <p>The status of your bid for ' . $o_name . ' has been updated to:</p>
                    <h2>' . $new_status . '</h2>
                    <p>Thank you for participating in our auction.</p>
                </div>
                <div class="footer">
                    <p>&copy; ' . date("Y") . ' ' . $app_name . '. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>';
    
    $headers = "From: $admin_email\r\n";
    $headers .= "MIME-Version: 1.0\r\n";
    $headers .= "Content-Type: text/html; charset=UTF-8\r\n";

    // Send email
    mail($to, $subject, $message, $headers);
}


function placeBid($user_id, $game_id, $formatted_bid_value, $o_amount, $current_date, $mysqli) {
    $insert_bid_query = $mysqli->prepare("INSERT INTO tbl_bid (u_id, o_id, bd_value, bd_amount, bd_date, bid_status, bd_status) VALUES (?, ?, ?, ?, ?, ?, ?)");
    $bid_status = 1;
    $bd_status = 1;
    $insert_bid_query->bind_param("iisdsii", $user_id, $game_id, $formatted_bid_value, $o_amount, $current_date, $bid_status, $bd_status);
    $insert_bid_query->execute();
}

function updateWallet($user_id, $new_wallet_balance, $mysqli) {
    $update_wallet_query = $mysqli->prepare("UPDATE tbl_users SET wallet = ? WHERE id = ?");
    $update_wallet_query->bind_param("di", $new_wallet_balance, $user_id);
    $update_wallet_query->execute();
}

function updateWinner($game_id, $new_winner_id, $user_name, $formatted_bid_value, $mysqli) {
    $update_winner_query = $mysqli->prepare("UPDATE tbl_offers SET winner_id = ?, winner_name = ?, winning_value = ? WHERE o_id = ?");
    $update_winner_query->bind_param("issi", $new_winner_id, $user_name, $formatted_bid_value, $game_id);
    $update_winner_query->execute();
}

function updateEnglishWinner($game_id, $new_winner_id, $user_name, $formatted_bid_value, $mysqli) {
    $update_winner_query = $mysqli->prepare("UPDATE tbl_offers SET winner_id = ?, winner_name = ?, winning_value = ?, o_min = ? WHERE o_id = ?");
    $update_winner_query->bind_param("issii", $new_winner_id, $user_name, $formatted_bid_value, $formatted_bid_value, $game_id);
    $update_winner_query->execute();
}

function updateReverseWinner($game_id, $new_winner_id, $user_name, $formatted_bid_value, $mysqli) {
    $update_winner_query = $mysqli->prepare("UPDATE tbl_offers SET winner_id = ?, winner_name = ?, winning_value = ?, o_max = ? WHERE o_id = ?");
    $update_winner_query->bind_param("issii", $new_winner_id, $user_name, $formatted_bid_value, $formatted_bid_value, $game_id);
    $update_winner_query->execute();
}

function updatePennyWinner($game_id, $new_winner_id, $user_name, $newBid, $newTime, $newDate, $mysqli) {
    $update_winner_query = $mysqli->prepare("UPDATE tbl_offers SET winner_id = ?, winner_name = ?, winning_value = ?, o_min = ?, o_etime = ?, o_edate = ? WHERE o_id = ?");
    $update_winner_query->bind_param("issdssi", $new_winner_id, $user_name, $newBid, $newBid, $newTime, $newDate, $game_id);
    $update_winner_query->execute();
}

function updateRandomWinner($game_id, $mysqli) {

    // Fetch a random winner from the bids placed for the specified auction
    $winnerQuery = "SELECT u_id, bd_value FROM tbl_bid WHERE o_id = ? ORDER BY RAND() LIMIT 1";
    $winnerStmt = $mysqli->prepare($winnerQuery);
    $winnerStmt->bind_param("i", $game_id);
    $winnerStmt->execute();
    $winnerResult = $winnerStmt->get_result();
    
        $winnerRow = $winnerResult->fetch_assoc();
        $new_winner_id = $winnerRow['u_id'];
        $newBid = $winnerRow['bd_value'];
        
        // Fetch user name of the new winner
        $userQuery = "SELECT name FROM tbl_users WHERE id = ?";
        $userStmt = $mysqli->prepare($userQuery);
        $userStmt->bind_param("i", $new_winner_id);
        $userStmt->execute();
        $userResult = $userStmt->get_result();
        $userRow = $userResult->fetch_assoc();
        $user_name = $userRow['name'];
        
        // Update the winner details in the auction
        $update_winner_query = $mysqli->prepare("UPDATE tbl_offers SET winner_id = ?, winner_name = ?, winning_value = ? WHERE o_id = ?");
        $update_winner_query->bind_param("issi", $new_winner_id, $user_name, $newBid, $game_id);
        $update_winner_query->execute();
}


function insertTransaction($user_id, $game_id, $current_date, $o_amount, $mysqli) {
    $insert_transaction_query = $mysqli->prepare("INSERT INTO tbl_transaction (user_id, type, type_no, date, money) VALUES (?, ?, ?, ?, ?)");
    $type = 1;
    $insert_transaction_query->bind_param("iiisd", $user_id, $type, $game_id, $current_date, $o_amount);
    $insert_transaction_query->execute();
}

mysqli_begin_transaction($mysqli);

try {
  if ($o_type != 8) {
    // Validate the bid value
    if ($formatted_bid_value < $auctionRow['o_min'] || $formatted_bid_value > $auctionRow['o_max']) {
        echo "{$auction_lang['bidBetween']} {$currency}{$auctionRow['o_min']} {$auction_lang['bidBetweenAnd']} {$currency}{$auctionRow['o_max']}.";
        exit;
    }
  }

    if ($o_type == 1 || $o_type == 2) {
        // Check for duplicate bids
        $check_duplicate_query = $mysqli->prepare("SELECT bd_id FROM tbl_bid WHERE o_id = ? AND bd_value = ?");
        $check_duplicate_query->bind_param("id", $game_id, $formatted_bid_value);
        $check_duplicate_query->execute();
        $duplicate_result = $check_duplicate_query->get_result();

        if ($duplicate_result->num_rows > 0) {
            $bid_status = 2;
            $update_duplicate_query = $mysqli->prepare("UPDATE tbl_bid SET bid_status = 2 WHERE o_id = ? AND bd_value = ?");
            $update_duplicate_query->bind_param("id", $game_id, $formatted_bid_value);
            $update_duplicate_query->execute();
        } else {
            $bid_status = 1;  // Bid is unique
        }

        $new_wallet_balance = $balance_row['wallet'] - $o_amount;
        updateWallet($user_id, $new_wallet_balance, $mysqli);

        placeBid($user_id, $game_id, $formatted_bid_value, $o_amount, $current_date, $mysqli);
        insertTransaction($user_id, $game_id, $current_date, $o_amount, $mysqli);

        if ($o_type == 1) {  // Lowest unique bid
            $unique_lowest_query = $mysqli->prepare("SELECT *, COUNT(*) as num1 FROM tbl_bid LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id WHERE o_id = ? GROUP BY bd_value HAVING num1 = 1 ORDER BY CAST(bd_value AS DECIMAL(18,2)) ASC LIMIT 1");
            $unique_lowest_query->bind_param("i", $game_id);
            $unique_lowest_query->execute();
            $result = $unique_lowest_query->get_result();

            if ($result && $result->num_rows > 0) {
                $row = $result->fetch_assoc();
                $bd_id = $row['bd_id'];
                $new_winner_id = $row['u_id'];
                $new_winner_name = $row['name'];
                $new_winner_bid = $row['bd_value'];

                $update_old_unique_query = $mysqli->prepare("UPDATE tbl_bid SET bid_status = 1 WHERE o_id = ? AND bid_status = 3");
                $update_old_unique_query->bind_param("i", $game_id);
                $update_old_unique_query->execute();

                if ($current_winner_id !== null && $current_winner_id != $new_winner_id) {
                    sendEmailNotification($current_winner_id, $o_name, $app_name, "not winning anymore as someone else outbidded you!", $mysqli, $admin_email);
                }

                $update_new_unique_query = $mysqli->prepare("UPDATE tbl_bid SET bid_status = 3 WHERE bd_id = ?");
                $update_new_unique_query->bind_param("i", $bd_id);
                $update_new_unique_query->execute();

                updateWinner($game_id, $new_winner_id, $new_winner_name, $new_winner_bid, $mysqli);

                if ($new_winner_id !== null && $current_winner_id != $new_winner_id) {
                    sendEmailNotification($new_winner_id, $o_name, $app_name, "now unique and currently you are winning the auction!", $mysqli, $admin_email);
                }
            }

        } elseif ($o_type == 2) {  // Highest unique bid
            $unique_highest_query = $mysqli->prepare("SELECT *, COUNT(*) as num1 FROM tbl_bid LEFT JOIN tbl_users ON tbl_users.id = tbl_bid.u_id WHERE o_id = ? GROUP BY bd_value HAVING num1 = 1 ORDER BY CAST(bd_value AS DECIMAL(18,2)) DESC LIMIT 1");
            $unique_highest_query->bind_param("i", $game_id);
            $unique_highest_query->execute();
            $result1 = $unique_highest_query->get_result();

            if ($result1 && $result1->num_rows > 0) {
                $row1 = $result1->fetch_assoc();
                $bd_id1 = $row1['bd_id'];
                $new_winner_id = $row1['u_id'];
                $new_winner_name = $row1['name'];
                $new_winner_bid = $row1['bd_value'];

                $update_old_unique_query = $mysqli->prepare("UPDATE tbl_bid SET bid_status = 1 WHERE o_id = ? AND bid_status = 3");
                $update_old_unique_query->bind_param("i", $game_id);
                $update_old_unique_query->execute();

                if ($current_winner_id !== null && $current_winner_id != $new_winner_id) {
                    sendEmailNotification($current_winner_id, $o_name, $app_name, "not winning anymore as someone else outbidded you!", $mysqli, $admin_email);
                }

                $update_new_unique_query = $mysqli->prepare("UPDATE tbl_bid SET bid_status = 3 WHERE bd_id = ?");
                $update_new_unique_query->bind_param("i", $bd_id1);
                $update_new_unique_query->execute();

                updateWinner($game_id, $new_winner_id, $new_winner_name, $new_winner_bid, $mysqli);

                if ($new_winner_id !== null && $current_winner_id != $new_winner_id) {
                    sendEmailNotification($new_winner_id, $o_name, $app_name, "now unique and currently you are winning the auction!", $mysqli, $admin_email);
                }
            }
        }

    } elseif ($o_type == 7 || $o_type == 10) {
        // Validate the bid value
        if ($formatted_bid_value < $auctionRow['o_min'] || $formatted_bid_value > $auctionRow['o_max']) {
            echo "{$auction_lang['bidBetween']} {$currency}{$auctionRow['o_min']} {$auction_lang['bidBetweenAnd']} {$currency}{$auctionRow['o_max']}.";
            exit;
        }

        $new_wallet_balance = $balance_row['wallet'] - $o_amount;
        updateWallet($user_id, $new_wallet_balance, $mysqli);

        placeBid($user_id, $game_id, $formatted_bid_value, $o_amount, $current_date, $mysqli);
        insertTransaction($user_id, $game_id, $current_date, $o_amount, $mysqli);

        if ($current_winner_id !== null && $current_winner_id != $user_id) {
            sendEmailNotification($current_winner_id, $o_name, $app_name, "not winning anymore as someone else outbidded you!", $mysqli, $admin_email);
        }

        if ($o_type == 7) {
            updateEnglishWinner($game_id, $user_id, $user_name, $formatted_bid_value, $mysqli);
        } else if ($o_type == 10) {
            updateReverseWinner($game_id, $user_id, $user_name, $formatted_bid_value, $mysqli);
        }

        sendEmailNotification($user_id, $o_name, $app_name, "now winning the auction!", $mysqli, $admin_email);

    } elseif ($o_type == 8) {
        $newBid = $currentBid + $bidIncrement;
        $newTime = date('H:i:s', strtotime($datetime) + $timeIncrement);
        $newDate = date('Y-m-d', strtotime($datetime));

        $new_wallet_balance = $balance_row['wallet'] - $o_amount;
        updateWallet($user_id, $new_wallet_balance, $mysqli);

        placeBid($user_id, $game_id, $newBid, $o_amount, $current_date, $mysqli);
        insertTransaction($user_id, $game_id, $current_date, $o_amount, $mysqli);

        updatePennyWinner($game_id, $user_id, $user_name, $newBid, $newTime, $newDate, $mysqli);

    } elseif ($o_type == 11) {
        $totalEntry = $bidValue;
        $debitAmount = $o_amount * $totalEntry;

        // Fetch user's wallet balance
        $check_balance_query = $mysqli->prepare("SELECT wallet FROM tbl_users WHERE id = ? FOR UPDATE");
        $check_balance_query->bind_param("i", $user_id);
        $check_balance_query->execute();
        $balance_row = $check_balance_query->get_result()->fetch_assoc();

        if ($balance_row['wallet'] < $debitAmount) {
            echo $auction_lang['insufficientBalance'];
            exit;
        }

        $new_wallet_balance = $balance_row['wallet'] - $debitAmount;
        updateWallet($user_id, $new_wallet_balance, $mysqli);

        placeBid($user_id, $game_id, $totalEntry, $debitAmount, $current_date, $mysqli);
        insertTransaction($user_id, $game_id, $current_date, $debitAmount, $mysqli);

        updateRandomWinner($game_id, $mysqli);
    }

    mysqli_commit($mysqli);
    echo $auction_lang['bidPlacedSuccess'];

} catch (Exception $e) {
    mysqli_rollback($mysqli);
    echo "Error: " . $e->getMessage();
}

?>
