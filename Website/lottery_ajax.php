<?php

include ("includes/connection.php");
include ("language/language.php");



function lotteryPickerCard($index, $normal_ball_start, $normal_ball_end, $premium_ball_start, $premium_ball_end, $normal_ball_limit, $premium_ball_limit)
{
    ?>
    <div class="picker-number-container" id="unique-<?php echo $index; ?>">

        <i class="fa fa-close close-picker-button" onclick="destroyMe(this);"></i>
        <p class="good-status purchase-done">This ticket is purchased</p>

        <div>
            <?php if ((int) $normal_ball_end != 0) { ?>
                <h5>Pick <?php echo $normal_ball_limit; ?> Numbers</h5>

                <div class="button-picker-container" data="<?php echo $index; ?>">

                    <button type="button" class="magic-button-picker" id="picker-<?php echo $index; ?>"
                        onclick="pickMe(this);"><i class="fa fa-magic"></i><?php echo $auction_lang["lotteryPick"]?></button>

                    <button type="button" class="delete-button-picker" onclick="deleteMe(this);"><i
                            class="fa fa-trash"></i>Reset</button>

                </div>

            <?php } ?>

        </div>

        <div class="picker-container">

            <?php

            for ($i = $normal_ball_start; $i <= $normal_ball_end; $i++) {

                echo "<span id='unique-normal-" . $i . "' data='" . $index . "' onclick='selectOnlyMeNormal(this);'>" . $i . "</span>";

            }

            ?>

        </div>

        <?php if ((int) $premium_ball_end != 0) { ?>

            <h6>Pick <?php echo $premium_ball_limit; ?> Numbers</h6>

            <div class="picker-container">

                <?php

                for ($i = $premium_ball_start; $i <= $premium_ball_end; $i++) {

                    echo "<span  id='unique-premium-" . $i . "' data='" . $index . "' onclick='selectOnlyMePremium(this);'>" . $i . "</span>";

                }

                ?>

            </div>

        <?php } ?>

    </div>

    <?php

}





if (isset($_POST["request-type"])) {

    if ($_POST["request-type"] == "get-card") {

        $normal_ball_start = $_POST["normal_ball_start"];

        $normal_ball_end = $_POST["normal_ball_end"];

        $premium_ball_start = $_POST["premium_ball_start"];

        $premium_ball_end = $_POST["premium_ball_end"];

        $normal_ball_limit = $_POST["normal_ball_limit"];

        $premium_ball_limit = $_POST["premium_ball_limit"];

        $index = $_POST["index"];



        lotteryPickerCard($index, $normal_ball_start, $normal_ball_end, $premium_ball_start, $premium_ball_end, $normal_ball_limit, $premium_ball_limit);

    } elseif ($_POST["request-type"] == "insert-ticket") {

        include ("includes/session_check.php");

        $user_id = $_SESSION["user_id"];

        $check_balance_query = "SELECT wallet FROM tbl_users WHERE id = $user_id FOR UPDATE";
        $balance_result = mysqli_query($mysqli, $check_balance_query);
        $balance_row = mysqli_fetch_assoc($balance_result);
        $data = $_POST;
        $current_date = date('Ymd');
        

        $o_amount = (int)$_POST["ticket_price"] * (int)count($data["tickets"]);

        if ($balance_row['wallet'] >= $o_amount) {
            $result = $mysqli->query("SELECT MAX(ticket_id) as max_ticket_id FROM tbl_ticket");

            $row = $result->fetch_assoc();

            $ticket_id = $row['max_ticket_id'] + 1;

            $u_id = $_SESSION["user_id"];

            $game_id = $_POST["o_id"];

            $ticket_price = $_POST["ticket_price"];

            $ticket_status = 1;

            foreach ($data["tickets"] as $ball) {
                $ball1 = $ball[0] ?? null;
                $ball2 = $ball[1] ?? null;
                $ball3 = $ball[2] ?? null;
                $ball4 = $ball[3] ?? null;
                $ball5 = $ball[4] ?? null;
                $ball6 = $ball[5] ?? null;
                $ball7 = $ball[6] ?? null;
                $ball8 = $ball[7] ?? null;

                $unique_ticket_id = $ticket_id . '_' . date('Ymd') . '_' . $data['o_id'];

                $sql = "INSERT INTO tbl_ticket (
    
                    ticket_id, ball_1, ball_2, ball_3, ball_4, ball_5, ball_6, ball_7, ball_8, u_id, o_id, ticket_price, unique_ticket_id, ticket_status
    
                ) VALUES (
    
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
    
                )";

                $stmt = $mysqli->prepare($sql);
                $stmt->bind_param(

                    'iiiiiiiiiiissi',

                    $ticket_id,

                    $ball1,
                    $ball2,
                    $ball3,
                    $ball4,
                    $ball5,
                    $ball6,
                    $ball7,
                    $ball8,
                    $u_id,

                    $data['o_id'],

                    $ticket_price,

                    $unique_ticket_id,

                    $ticket_status
                );

                try {

                    $stmt->execute();

                } catch (Exception) {

                    echo $mysqli->error;
                }

                $ticket_id++;
            }

            $new_wallet_balance = $balance_row['wallet'] - $o_amount;
            $update_wallet_query = "UPDATE tbl_users SET wallet = $new_wallet_balance WHERE id = $user_id";
            mysqli_query($mysqli, $update_wallet_query);

            $transaction_query = "INSERT INTO tbl_transaction (user_id, type, type_no, date, money) 
                                  VALUES ('$user_id', '1', '$game_id', '$current_date', '$o_amount')";
            mysqli_query($mysqli, $transaction_query);

            echo "334455-Done-Response";
            $stmt->close();
            $mysqli->close();
        } else {
            echo "MESSAGE-INSUFFICIENT-BALANCE-3322";
        }
    }

}

?>