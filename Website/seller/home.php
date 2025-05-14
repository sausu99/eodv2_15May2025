<?php 
include("includes/header.php");
include("includes/session_check.php");
include('includes/function.php');
include('language/language.php'); 

$id = PROFILE_ID;

$qry_lottery="SELECT COUNT(*) as num FROM tbl_offers WHERE o_type = '5' and tbl_offers.id = '$id'";
$total_lottery = mysqli_fetch_array(mysqli_query($mysqli,$qry_lottery));
$total_lottery = $total_lottery['num'];

$qry_auction="SELECT COUNT(*) as num FROM tbl_offers  WHERE o_type IN (1,2,7,8,10,11) and tbl_offers.id = '$id'";
$total_auction = mysqli_fetch_array(mysqli_query($mysqli,$qry_auction));
$total_auction = $total_auction['num'];

$qry_shop="SELECT COUNT(*) as num FROM tbl_offers  WHERE o_type = '9' and tbl_offers.id = '$id'";
$total_shop = mysqli_fetch_array(mysqli_query($mysqli,$qry_shop));
$total_shop = $total_shop['num'];

$qry_orders="SELECT COUNT(*) as num FROM tbl_order left join tbl_offers on tbl_offers.o_id = tbl_order.offer_id 
		    WHERE tbl_offers.id = '$id'";
$total_orders = mysqli_fetch_array(mysqli_query($mysqli,$qry_orders));
$total_orders = $total_orders['num'];

$qry_bids="SELECT COUNT(*) as num FROM tbl_bid left join tbl_offers on tbl_offers.o_id = tbl_bid.o_id 
		    WHERE tbl_offers.id = '$id' AND tbl_offers.o_type IN (1,2,7,8,10,11)";
$total_bids = mysqli_fetch_array(mysqli_query($mysqli,$qry_bids));
$total_bids = $total_bids['num'];

$qry_tickets="SELECT COUNT(*) as num FROM tbl_ticket left join tbl_offers on tbl_offers.o_id = tbl_ticket.o_id 
		    WHERE tbl_offers.id = '$id'";
$total_tickets = mysqli_fetch_array(mysqli_query($mysqli,$qry_tickets));
$total_tickets = $total_tickets['num'];

$queryPermit = "SELECT * FROM tbl_vendor WHERE id = " . $_SESSION['seller_id'] . "";
$resultPermit = mysqli_query($mysqli, $queryPermit);
$rowPermit = mysqli_fetch_assoc($resultPermit);
?>   
       


    <head>
<title><?php echo $client_lang['home']; ?></title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
<style>
    .chart-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around;
            gap: 20px;
            margin-left: 10px;
            margin-bottom: 10px;
            margin-top:10px;
        }
        .chart-box, .pie-chart-box {
            flex: 1;
            min-width: 25%; /* Ensure a minimum width for each chart box */
            max-width: 33%; /* Limit the maximum width to prevent stretching */
            box-sizing: border-box;
        }
        .canvas-wrapper {
            background-color: #fff;
            padding: 20px;
            border-radius: 18px;
            border-color: #fafafa;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
        #auctionBidsChart, #auctionBidsPieChart, #auctionBidsPointChart, #topBidderChart, #topTicketChart {
            height: 400px !important; /* Set fixed height for charts */
        }
        
        /* Responsive adjustments */
        @media (max-width: 1200px) {
            .chart-box, .pie-chart-box {
                flex: 1 1 45%; /* Adjust flex basis for medium screens */
            }
        }
        @media (max-width: 768px) {
            .chart-box, .pie-chart-box {
                flex: 1 1 100%; /* Full width on smaller screens */
            }
        }
       .card-link {
    text-decoration: none;
    color: inherit;
}

.card {
    display: flex;
    align-items: center;
    padding: 10px 20px;
    border: 1px solid #D3D3D3;
    border-radius: 8px;
    margin: 0px 0;
    background-color: #fff;
    width: 100%;
    max-width: 400px;
    box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease;
}

.card:hover {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
}

.card-icon {
    flex: 0 0 50px;
    width: 50px; /* Ensures the icon background is square */
    height: 50px; /* Ensures the icon background is square */
    background-color: #e0e7ff;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.card-icon i {
    font-size: 24px;
    color: #6366f1; /* Icon color */
}

.card-content {
    flex: 1;
    padding: 0 20px;
}

.card-title {
    margin: 0;
    font-size: 12px; /* Reduced size for single-line display */
    color: #6b7280;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.card-value {
    margin: 0;
    font-size: 24px; /* Default font size */
    font-weight: bold;
    color: #111827;
    white-space: nowrap; /* Prevents the text from wrapping onto a new line */
    overflow: hidden; /* Ensures that any overflow is hidden */
    text-overflow: ellipsis; /* Adds ellipsis (...) to indicate text overflow */
    width: 100%; /* Ensures the text takes the full width of the container */
    display: block; /* Ensures the text behaves as a block element */
}

.card-arrow {
    flex: 0 0 20px;
    text-align: right;
    color: #9ca3af;
    font-size: 18px;
}



</style>
</head>

<!-- Icon -->
<link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.2.0/uicons-regular-rounded/css/uicons-regular-rounded.css'>
<link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.2.0/uicons-regular-straight/css/uicons-regular-straight.css'>


    <div class="row">
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="#" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-gavel"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_auction']; ?></p>
                        <p class="card-value"><?php echo $total_auction; ?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="bidding_history.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-gavel"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_bids']; ?></p>
                        <p class="card-value"><?php echo $total_bids;?></p>
                    </div>
                    <div class="card-arrow">
                        <span>&gt;</span>
                    </div>
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="lottery.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-ticket"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_lottery']; ?></p>
                        <p class="card-value"><?php echo $total_lottery; ?></p>
                    </div>
                    <div class="card-arrow">
                        <span>&gt;</span>
                    </div>
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="ticket_purchases.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-ticket"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_tickets']; ?></p>
                        <p class="card-value"><?php echo $total_tickets;?></p>
                    </div>
                    <div class="card-arrow">
                        <span>&gt;</span>
                    </div>
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="shop.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-shop"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_shop_items']; ?></p>
                        <p class="card-value"><?php echo $total_shop;?></p>
                    </div>
                    <div class="card-arrow">
                        <span>&gt;</span>
                    </div>
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="orders.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-box"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_order']; ?></p>
                        <p class="card-value"><?php echo $total_orders;?></p>
                    </div>
                    <div class="card-arrow">
                        <span>&gt;</span>
                    </div>
                </div>
            </a>
        </div>
    </div>

        
<?php include("includes/footer.php");?>       
