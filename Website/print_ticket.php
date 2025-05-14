<?php
include('includes/header.php');
include('includes/function.php');
include('language/language.php');

$ticket_id = isset($_GET['ticket_id']) ? $_GET['ticket_id'] : '';

$query = "SELECT * FROM tbl_ticket
    LEFT JOIN tbl_users ON tbl_users.id = tbl_ticket.u_id
    LEFT JOIN tbl_offers ON tbl_offers.o_id = tbl_ticket.o_id
    LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
    LEFT JOIN tbl_lottery_balls ON tbl_lottery_balls.lottery_balls_id = tbl_offers.lottery_balls_id
    WHERE tbl_ticket.unique_ticket_id = '$ticket_id'";

$result = mysqli_query($mysqli, $query);
$ticket = mysqli_fetch_array($result);
$winner_id = $ticket['u_id'];

$user_id = isset($_SESSION['user_id']) ? intval($_SESSION['user_id']) : 0;
if ($user_id != $winner_id)
{
    $_SESSION['msg'] = "access_denied_ticket";
    header("Location:ticket-purchases");
    exit;
}

$querytime = "SELECT app_logo, app_name, admin_email FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);

$app_logo = $rowtime['app_logo'];
$app_name = $rowtime['app_name'];
$admin_email = $rowtime['admin_email'];
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Print Ticket</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
        }

        .ticket-container {
            max-width: 100%;
            width: 600px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .ticket-header {
            margin-bottom: 20px;
        }

        .ticket-header img {
            max-width: 150px;
            height: auto;
        }

        .ticket-details {
            margin-bottom: 20px;
            text-align: left;
        }

        .ticket-numbers {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            margin-bottom: 20px;
        }

        .ticket-ball {
            margin: 5px;
            position: relative;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .ticket-ball img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .ticket-number {
            font-size: 16px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            font-weight: bold;
            color: #000;
        }

        .ticket-qr {
            text-align: right;
            margin-bottom: 20px;
        }

        .ticket-qr img {
            width: 100px;
            height: 100px;
        }

        .ticket-footer {
            margin-top: 30px;
        }

        .print-button {
            margin-top: 20px;
        }

        .print-button button {
            padding: 10px 20px;
            font-size: 16px;
            background-color: #4CAF50;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .print-button button:hover {
            background-color: #45a049;
        }

        @media (max-width: 600px) {
            .ticket-container {
                width: 100%;
                margin: 10px;
                padding: 15px;
            }

            .ticket-header img {
                max-width: 120px;
            }

            .ticket-qr img {
                width: 80px;
                height: 80px;
            }

            .print-button button {
                width: 100%;
                padding: 15px;
            }
        }

        /* Print Styles */
        @media print {
            body * {
                visibility: hidden;
            }
            .ticket-container, .ticket-container * {
                visibility: visible;
            }
            .ticket-container {
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
            }
            .print-button {
                display: none;
            }
        }
    </style>
    <script>
        function printTicket() {
            window.print();
        }
    </script>
</head>
<body>
    <div class="ticket-container">
        <div class="ticket-header">
            <img src="/images/<?php echo $app_logo; ?>" alt="<?php echo $app_name; ?>">
            <p><?php echo $app_name; ?></p>
        </div>
        <div class="ticket-details">
            <p><strong><?php echo $auction_lang['ticketNumber']; ?>:</strong></p>
            <div class="ticket-numbers">
                <?php
                $blueBalls = $ticket['normal_ball_limit'];
                $goldBalls = $ticket['premium_ball_limit'];
                $totalBalls = $blueBalls + $goldBalls;
                for ($j = 1; $j <= $totalBalls; $j++) {
                    $ballValue = $ticket['ball_'.$j];
                    $ballImage = ($j <= $goldBalls) ? 'admin/images/static/golden_ball.png' : 'admin/images/static/blue_ball.png';
                    echo '<div class="ticket-ball">';
                    echo '<img src="'.$ballImage.'" alt="'.$ballValue.'" title="'.$ballValue.'">';
                    echo '<span class="ticket-number">'.$ballValue.'</span>';
                    echo '</div>';
                }
                ?>
            </div>
            <p><strong><?php echo $client_lang['ticket_purchased_on']; ?>:</strong> <?php echo $ticket['purchase_date']; ?></p>
            <p><strong><?php echo $auction_lang['ticketPrice']; ?>:</strong> <?php echo $ticket['ticket_price'].' '.$auction_lang['coins']; ?></p>
            <p><strong><?php echo $client_lang['ticket_ends_on']; ?>:</strong> <?php echo $ticket['o_edate'].' '.$client_lang['at'].' '.$ticket['o_etime']; ?></p>
            <p><strong><?php echo $client_lang['bought_by']; ?>:</strong> <?php echo $ticket['name']; ?></p>
            <p><strong><?php echo $client_lang['terms']; ?>:</strong><?php echo $client_lang['ticket_terms']; ?></p>
            <p><strong><?php echo $client_lang['ticket_contact']; ?>:</strong> <?php echo $admin_email ?></p>
        </div>
        <div class="ticket-qr">
            <img src="generate_qr.php?text=<?php echo urlencode($ticket['unique_ticket_id']); ?>" alt="QR Code">
        </div>
        <div class="ticket-footer">
            <p><?php echo $client_lang['thank_you_purchase']; ?></p>
            <div class="print-button">
                <button onclick="printTicket()"><?php echo $client_lang['print_ticket']; ?></button>
            </div>
        </div>
    </div>
</body>
</html>