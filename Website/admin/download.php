<?php
include('includes/connection.php'); // Make sure this includes your database connection file

// Get the current domain dynamically
$domain = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https" : "http") . "://$_SERVER[HTTP_HOST]";


if(isset($_GET['o_id'])) {
    $o_id = (int)$_GET['o_id']; // Cast to integer to avoid SQL injection
    
    $typeQuery = "SELECT o_type FROM tbl_offers WHERE o_id = $o_id";
    $typeResult = mysqli_query($mysqli, $typeQuery);
    $typeRow = mysqli_fetch_assoc($typeResult);
    $type = $typeRow['o_type'];
    
    if($type == '5') {
        // Fetch tickets for the lottery from the database
        $ticketQuery = "SELECT t.ticket_id, t.unique_ticket_id, t.ball_1, t.ball_2, t.ball_3, t.ball_4, t.ball_5, t.ball_6, t.ball_7, t.ball_8, t.ticket_price, t.purchase_date, u.name, u.email, i.o_name, b.normal_ball_limit, b.premium_ball_limit
                        FROM tbl_ticket AS t 
                        LEFT JOIN tbl_users AS u ON t.u_id = u.id 
                        LEFT JOIN tbl_offers AS o ON t.o_id = o.o_id 
                        LEFT JOIN tbl_items AS i ON o.item_id = i.item_id 
                        LEFT JOIN tbl_lottery_balls AS b ON b.lottery_balls_id = o.lottery_balls_id
                        WHERE t.o_id = $o_id";
        $ticketResult = mysqli_query($mysqli, $ticketQuery);

        // Check if there are tickets for this lottery
        if(mysqli_num_rows($ticketResult) > 0) {
            // Set custom headers for the CSV file
            $filename = "tickets_lottery_$o_id.csv";
            header('Content-Type: text/csv; charset=utf-8'); // Set character encoding
            header('Content-Disposition: attachment; filename="' . $filename . '"');
    
            // Open a file pointer for writing CSV content
            $fp = fopen('php://output', 'w');
    
            // Write custom headers
            fputcsv($fp, array('Ticket ID', 'Buyer', 'Email', 'Purchase Date', 'Balls'));

            // Iterate through ticket data and write to CSV
            while($ticket_row = mysqli_fetch_assoc($ticketResult)) {
                $ticketId = $ticket_row['unique_ticket_id'];
                
                $blueBalls = $ticket_row['normal_ball_limit'];
                $goldBalls = $ticket_row['premium_ball_limit'];
                $totalBalls = $blueBalls + $goldBalls;
                
                // Create image URLs for the balls
                $balls = '';
                for ($i = 1; $i <= $totalBalls; $i++) {
                    $ballNumber = $ticket_row["ball_$i"];
                    $ballImage = ($i <= $goldBalls) ? $domain . '/images/static/golden_ball.png' : $domain . '/images/static/blue_ball.png';
                    
                    // Combine ball image URL with ball number
                    $balls .= " $ballNumber, "; // Adding the ball URL with the number
                }

                // Write the ticket data to CSV
                fputcsv($fp, array(
                    '#'.$ticketId,
                    $ticket_row['name'],
                    $ticket_row['email'],
                    $ticket_row['purchase_date'],
                    $balls // Add image URLs for balls in CSV
                ));
            }

            // Close the file pointer
            fclose($fp);
            exit();
        } else {
            echo "No tickets have been purchased yet for this lottery.";
        }
    } else {
        // Other auction types handling
        $bids_query = "SELECT b.bd_id, b.bd_value, b.bd_amount, b.bd_date, u.name, u.email, i.o_name 
                       FROM tbl_bid AS b 
                       LEFT JOIN tbl_users AS u ON b.u_id = u.id 
                       LEFT JOIN tbl_offers AS o ON b.o_id = o.o_id 
                       LEFT JOIN tbl_items AS i ON o.item_id = i.item_id 
                       WHERE b.o_id = $o_id";
        $bids_result = mysqli_query($mysqli, $bids_query);

        if(mysqli_num_rows($bids_result) > 0) {
            // Set custom headers for the CSV file
            $filename = "bids_auction_$o_id.csv";
            header('Content-Type: text/csv; charset=utf-8');
            header('Content-Disposition: attachment; filename="' . $filename . '"');
    
            // Open a file pointer for writing CSV content
            $fp = fopen('php://output', 'w');

            // Write custom headers
            fputcsv($fp, array('Auction', 'Bid ID', 'Bid', 'Bidder', 'Email', 'Date'));

            while($bid_row = mysqli_fetch_assoc($bids_result)) {
                fputcsv($fp, array(
                    $bid_row['o_name'],
                    $bid_row['bd_id'],
                    $bid_row['bd_value'],
                    $bid_row['name'],
                    $bid_row['email'],
                    $bid_row['bd_date']
                ));
            }

            fclose($fp);
            exit();
        } else {
            echo "No bids have been made yet for this auction.";
        }
    }
}


else if(isset($_GET['t_id'])) {
    $lottery_id = $_GET['t_id'];

    // Fetch tickets for the selected lottery from the database
    $ticket_query = "SELECT b.bd_id, b.bd_value, b.bd_amount, b.bd_date, u.name, u.email, i.o_name 
                    FROM tbl_bid AS b 
                    LEFT JOIN tbl_users AS u ON b.u_id = u.id 
                    LEFT JOIN tbl_offers AS o ON b.o_id = o.o_id 
                    LEFT JOIN tbl_items AS i ON o.item_id = i.item_id 
                    WHERE b.o_id = $lottery_id";
    $ticket_result = mysqli_query($mysqli, $ticket_query);

    // Check if there are tickets for this lottery
    if(mysqli_num_rows($ticket_result) > 0) {
        // Set custom headers for the CSV file
        $filename = "tickets_$lottery_id.csv";
        header('Content-Type: text/csv; charset=utf-8'); // Set character encoding
        header('Content-Disposition: attachment; filename="' . $filename . '"');

        // Open a file pointer for writing CSV content
        $fp = fopen('php://output', 'w');

        // Set column widths
        $columnWidths = array(20, 10, 15, 20, 30, 20); // Adjust these values as needed

        // Write custom headers
        fputcsv($fp, array('Lottery 1st Prize', 'Ticket ID', 'Ticket', 'User', 'Email', 'Date'));

        // Iterate through ticket data and write to CSV
        while($ticket_row = mysqli_fetch_assoc($ticket_result)) {
            // Write ticket data to CSV
            fputcsv($fp, array(
                $ticket_row['o_name'],
                $ticket_row['bd_id'],
                $ticket_row['bd_value'],
                $ticket_row['name'],
                $ticket_row['email'],
                $ticket_row['bd_date']
            ));
        }

        // Close the file pointer
        fclose($fp);

        exit();
    } else {
        // No tickets found for this lottery
        echo "No Tickets purchased for this lottery.";
    }
}
else if(isset($_GET['s_id'])) {
    $seller_id = $_GET['s_id'];

    // Fetch tickets for the selected lottery from the database
    $seller_query = "SELECT* 
                    FROM tbl_offers 
                    WHERE id = $seller_id";
    $seller_result = mysqli_query($mysqli, $seller_query);

    // Check if there are items for this seller
    if(mysqli_num_rows($seller_result) > 0) {
        // Set custom headers for the CSV file
        $filename = "items_$seller_id.csv";
        header('Content-Type: text/csv; charset=utf-8'); // Set character encoding
        header('Content-Disposition: attachment; filename="' . $filename . '"');

        // Open a file pointer for writing CSV content
        $fp = fopen('php://output', 'w');

        // Set column widths
        $columnWidths = array(20, 10, 15, 20, 30, 20); // Adjust these values as needed

        // Write custom headers
        fputcsv($fp, array('Item ID','Item Name', 'Start Date', 'End Date', 'Type', 'Status'));

        // Iterate through seller data and write to CSV
        while($seller_row = mysqli_fetch_assoc($seller_result)) {
            // Write seller data to CSV
        // Determine the label based on the value of $seller_row['o_type']
            switch ($seller_row['o_type']) {
                case 1:
                case 2:
                case 7:
                case 8:
                    $typeLabel = "Auction";
                    break;
                case 4:
                case 5:
                    $typeLabel = "Lottery";
                    break;
                case 3:
                    $typeLabel = "Gift Item";
                    break;
                case 9:
                    $typeLabel = "Shop Item";
                    break;
                default:
                    $typeLabel = "Unknown";
            }
            
             switch ($seller_row['o_status']) {
                case 1:
                     $statusLabel = "Visible";
                    break;
                case 0:
                    $statusLabel = "Hidden";
                    break;
                default:
                    $statusLabel = "Unknown";
            }
            fputcsv($fp, array(
                $seller_row['o_id'],
                $seller_row['o_name'],
                $seller_row['o_date'],
                $seller_row['o_edate'],
                $typeLabel, // Use the determined type label here
                $statusLabel
            ));
        }

        // Close the file pointer
        fclose($fp);

        exit();
    } else {
        // No items found for this seller
        echo "No Items added by this seller.";
    }
}
else if(isset($_GET['user_participation'])) {
    $participation_id = $_GET['user_participation'];

    // Fetch tickets for the selected lottery from the database
    $participation_query = "SELECT * 
                            FROM tbl_bid
                            LEFT JOIN tbl_offers ON tbl_offers.o_id = tbl_bid.o_id
                            LEFT JOIN tbl_items ON tbl_offers.item_id = tbl_items.item_id 
                            WHERE tbl_bid.u_id = $participation_id";
    $participation_result = mysqli_query($mysqli, $participation_query);

    // Check if there are items for this seller
    if(mysqli_num_rows($participation_result) > 0) {
        // Set custom headers for the CSV file
        $filename = "participation_$participation_id.csv";
        header('Content-Type: text/csv; charset=utf-8'); // Set character encoding
        header('Content-Disposition: attachment; filename="' . $filename . '"');

        // Open a file pointer for writing CSV content
        $fp = fopen('php://output', 'w');

        // Set column widths
        $columnWidths = array(20, 10, 15, 20, 30, 20); // Adjust these values as needed

        // Write custom headers
        fputcsv($fp, array('Item ID','Item Name', 'Participation Value', 'Date', 'Type'));

        // Iterate through seller data and write to CSV
        while($participation_row = mysqli_fetch_assoc($participation_result)) {
            // Write seller data to CSV
        // Determine the label based on the value of $participation_row['o_type']
            switch ($participation_row['o_type']) {
                case 1:
                case 2:
                case 7:
                case 8:
                case 10:
                case 11:
                    $typeLabel = "Auction";
                    break;
                case 4:
                case 5:
                    $typeLabel = "Lottery";
                    break;
                case 3:
                    $typeLabel = "Gift Item";
                    break;
                case 9:
                    $typeLabel = "Shop Item";
                    break;
                default:
                    $typeLabel = "Unknown";
            }
            
             switch ($participation_row['o_type']) {
                case 1:
                case 2:
                case 7:
                case 8:
                    $beforeValue = "Bid Value: ";
                    break;
                case 4:
                case 5:
                    $beforeValue = "Ticket Value: #";
                    break;
                default:
                    $beforeValue = "Other";
            }
            fputcsv($fp, array(
                $participation_row['o_id'],
                $participation_row['o_name'],
                $beforeValue.$participation_row['bd_value'],
                $participation_row['bd_date'],
                $typeLabel, // Use the determined type label here
            ));
        }

        // Close the file pointer
        fclose($fp);

        exit();
    } else {
        // No participation found for this user
        echo "This user has not participated yet.";
    }
}
else if(isset($_GET['user_transactions'])) {
    $transactions_id = $_GET['user_transactions'];

    // Fetch tickets for the selected lottery from the database
    $transactions_query = "SELECT * 
                            FROM tbl_transaction
                            LEFT JOIN tbl_offers ON tbl_offers.o_id = tbl_transaction.type_no
                            LEFT JOIN tbl_items ON tbl_offers.item_id = tbl_items.item_id 
                            WHERE tbl_transaction.user_id = $transactions_id";
    $transactions_result = mysqli_query($mysqli, $transactions_query);

    // Check if there are items for this seller
    if(mysqli_num_rows($transactions_result) > 0) {
        // Set custom headers for the CSV file
        $filename = "transactions_$transactions_id.csv";
        header('Content-Type: text/csv; charset=utf-8'); // Set character encoding
        header('Content-Disposition: attachment; filename="' . $filename . '"');

        // Open a file pointer for writing CSV content
        $fp = fopen('php://output', 'w');

        // Set column widths
        $columnWidths = array(20, 10, 15, 20, 30, 20); // Adjust these values as needed

        // Write custom headers
        fputcsv($fp, array('Item','Coin','Date'));

        // Iterate through seller data and write to CSV
        while($transactions_row = mysqli_fetch_assoc($transactions_result)) {
            // Write seller data to CSV
        // Determine the label based on the value of $transactions_row['o_type']
            
            fputcsv($fp, array(
                $transactions_row['o_name'],
                $transactions_row['money'].' Coins',
                $transactions_row['date'],
            ));
        }

        // Close the file pointer
        fclose($fp);

        exit();
    } else {
        // No participation found for this user
        echo "No transactions found for this user.";
    }
}
else {
    // Redirect if ID is not provided
    header("Location: home.php");
    exit();
}
?>
