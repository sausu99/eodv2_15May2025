<?php include('includes/header.php'); ?>

<?php
include('includes/function.php');
include('language/language.php');


$querytime = "SELECT timezone, currency FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
$currency = $rowtime['currency'];

date_default_timezone_set($rowtime['timezone']);
$time = date('H:i:s');
$date1 = date('Y-m-d');

if (isset($_POST['user_search'])) {
    $user_qry = "SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id  WHERE ( o_date <= '" . $date1 . "' and o_edate >= '" . $date1 . "' ) and o_status = 1 and
                             o_type IN (9)
                             and tbl_items.o_name like '%" . addslashes($_POST['search_value']) . "%' ORDER BY tbl_offers.o_date ASC";

    $users_result = mysqli_query($mysqli, $user_qry);
} else {

    $tableName = "tbl_offers";
    //<!--removed .php-->
    $targetpage = "shop";
    $limit = 12;

    $query = "SELECT COUNT(*) as num FROM $tableName LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE (
                                        (o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                                         AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "')))
                                         AND o_status = 1 AND o_type IN (9)";
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
                          WHERE ((o_date < '" . $date1 . "' OR (o_date = '" . $date1 . "' AND o_stime <= '" . $time . "'))
                          AND (o_edate > '" . $date1 . "' OR (o_edate = '" . $date1 . "' AND o_etime >= '" . $time . "')))
                          AND o_status = 1 AND o_type IN (9)
                          ORDER BY tbl_offers.o_edate ASC
                          LIMIT $start, $limit;";

    $users_result = mysqli_query($mysqli, $users_qry);

    if (mysqli_num_rows($users_result) == 0) {
        
        $title = $client_lang['no_shop'];
        $description = $client_lang['no_shop_description'];
        $image = 'no_shop.gif';
        include("nodata.php");
        exit;
    }
}

?>

<head>
    <link rel="stylesheet" href="assets/css/home.css">
    <title><?php echo $header_lang['shop']; ?></title>
</head>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>

        <div class="card mrg_bottom">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $header_lang['shop']; ?></div>
                </div>
                <div class="col-md-7 col-xs-12">
                    <div class="search_list">
                        <div class="search_block">
                            <form method="post" action="">
                                <input class="form-control input-sm" placeholder="<?php echo $client_lang['searchItem']; ?>" aria-controls="DataTables_Table_0" type="search" name="search_value" required>
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
                            <div class="row justify-content-center">
                                <?php while ($users_row = mysqli_fetch_assoc($users_result)) { 
                                    // Calculate discount percentage
                                    $originalPrice = $users_row['o_price'];
                                    $discountedPrice = $users_row['o_amount'];
                                    $discountPercentage = (($originalPrice - $discountedPrice) / $originalPrice) * 100;
                                    
                                    //get seller name
                                    $sellerId = $users_row['id'];
                                    $querySellerName = "SELECT email FROM tbl_vendor WHERE id = $sellerId";
                                    $resultSellerName = mysqli_query($mysqli, $querySellerName);
                                    $rowSellerName = mysqli_fetch_assoc($resultSellerName);
                                    $sellerName = $rowSellerName['email'];
                                ?>
                                    <div class="col-lg-4 col-md-6 col-sm-10">
                                        <div class="auction-item">
                                            <div class="image">
                                                <a href="<?php
                                                            //<!--removed .php-->
                                                                echo 'item_purchase';
                                                            
                                                            ?>?item_id=<?php echo $users_row['o_id']; ?>">
                                                            <img src="placeholder.jpg" data-src="<?php echo '../seller/images/'.$users_row['o_image']; ?>" class="lazyload img-fluid img-thumbnail" alt="<?php echo $users_row['o_name']; ?>" style="vertical-align: middle;">
                                                </a>
                                            </div>
                                            <div class="auction-content">
                                                <span style="color: grey;"><?php echo $auction_lang['soldBy'].' '.$sellerName; ?></span>
                                                <h5 style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"><?php echo $users_row['o_name']; ?></h5>
                                                <span style="color: grey; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; text-overflow: ellipsis;"><?php echo $users_row['o_desc']; ?></span>
                                                <p>
                                                    <strong><?php echo $currency.$discountedPrice.'&nbsp;'; ?></strong>
                                                    <s><?php echo $currency.$originalPrice; ?></s>
                                                    <span style="color: green;"><?php echo round($discountPercentage) . $client_lang['discount']; ?></span>
                                                </p><br>
                                            </div>
                                            <div class="button text-center">
                                                <!--removed .php-->
                                                <a href="item_purchase?item_id=<?php echo $users_row['o_id']; ?>">
                                                    <?php echo $client_lang['buy']; ?>
                                                </a>
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
                                include("pagination.php");
                            } ?>
                        </nav>
                    </div>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>

<?php include('includes/footer.php'); ?>
