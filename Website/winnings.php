<?php
include('includes/header.php');
include("includes/session_check.php");
include('includes/function.php');
include('language/language.php');

$user_id = isset($_SESSION['user_id']) ? intval($_SESSION['user_id']) : 0;


// Fetch settings for timezone
$settings_query = "SELECT timezone FROM tbl_settings";
$settings_result = mysqli_query($mysqli, $settings_query);

if (!$settings_result) {
    die("Database query failed: " . mysqli_error($mysqli));
}

$settings_row = mysqli_fetch_assoc($settings_result);
date_default_timezone_set($settings_row['timezone']);

$datetime = date('Y-m-d H:i:s');
$date = date('Y-m-d');
$time = date('H-i-s');

// Query to fetch winnings for the logged-in user
$users_qry = "SELECT p.o_id, p.item_id, o.o_date, o.o_stime, o.o_edate, o.o_etime, i.o_image, i.o_name FROM tbl_winners w
                            LEFT JOIN tbl_prizes p ON w.winner_rank BETWEEN p.rank_start AND p.rank_end
                            LEFT JOIN tbl_offers o ON p.o_id = o.o_id
                            LEFT JOIN tbl_items i ON p.item_id = i.item_id
                            WHERE w.u_id = '$user_id'
                            AND (o.o_edate < '$date' OR (o.o_edate = '$date' AND o.o_etime <= '$time'))";
              
$users_result = mysqli_query($mysqli, $users_qry);

if (mysqli_num_rows($users_result) == 0) {
    $title = $client_lang['no_winnings'];
    $description = $client_lang['no_winnings_description'];
    $image = 'no_winnings.gif';
    include("nodata.php");
    exit;
}

// Check if there are any results
if (mysqli_num_rows($users_result) == 0) {
    $title = $client_lang['no_winnings'];
    $description = $client_lang['no_winnings_description'];
    include('nodata.php');
    exit;
}

if (!$users_result) {
    die("Database query failed: " . mysqli_error($mysqli));
}
?>

<head>
<title><?php echo $client_lang['winningsTitle']; ?></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
</head>
<style>
    .single-line-ellipsis {
        white-space: nowrap; /* Prevent text from wrapping to the next line */
        overflow: hidden;    /* Ensure the overflowing text is hidden */
        text-overflow: ellipsis; /* Add ellipsis when the text overflows */
    }
</style>
<div class="row">
    <div class="col-xs-12">
        <div class="card mrg_bottom">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $client_lang['winningsTitle']; ?></div>
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
                    <?php
                    while ($users_row = mysqli_fetch_array($users_result)) {
                        $offer_id = $users_row['o_id'];
                        $item_id = $users_row['item_id'];
                        $start = $users_row['o_date'] . " " . $users_row['o_stime'];
                        $end = $users_row['o_edate'] . " " . $users_row['o_etime'];
                        
                        if ($end <= $datetime) {
                            $order_check_query = "SELECT * FROM tbl_order WHERE offer_o_id = $offer_id AND offer_id = $item_id AND u_id='" . $_SESSION['user_id'] . "'";
                            $order_check_result = mysqli_query($mysqli, $order_check_query);

                            if (!$order_check_result) {
                                continue;
                            }

                            $order_row = mysqli_fetch_assoc($order_check_result);
                            $order_exists = mysqli_num_rows($order_check_result) > 0;
                    ?>
                        <div class="col-md-4 col-sm-6 col-xs-12">
                            <div class="card">
                                <div class="card-body">
                                    <img src="../seller/images/thumbs/<?php echo htmlspecialchars($users_row['o_image']); ?>" style="width: 250px; height: 250px;" class="img-responsive" alt="Game Image">
                                    <hr>
                                    <h4 class="card-title single-line-ellipsis"><?php echo htmlspecialchars($users_row['o_name']); ?></h4>
                                    <p class="card-text">
                                        <?php echo $winners_lang['wonOn']; ?>
                                        <?php echo date('jS F', strtotime($users_row['o_edate'])); ?>
                                    </p>
                                    <?php if (!$order_exists) { ?>
                                        <a href="claim.php?o=<?php echo $users_row['o_id']; ?>&i=<?php echo $users_row['item_id']; ?>" class="btn btn-primary"><?php echo $client_lang['winningsClaim']; ?></a>
                                    <?php } else { ?>
                                        <a href="view_order.php?o_id=<?php echo $order_row['o_id']; ?>" class="btn btn-success"><?php echo $client_lang['viewOrder']; ?></a>
                                    <?php } ?>
                                </div>
                            </div>
                        </div>
                    <?php
                        }
                    }
                    ?>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="pagination_item_block">
                </div>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
</div>

<?php include('includes/footer.php'); ?>
