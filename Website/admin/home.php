<?php include("includes/header.php");
      include("assets/js/app.php");
      include("language/language.php");

$qry_users="SELECT COUNT(*) as num FROM tbl_users";
$total_users= mysqli_fetch_array(mysqli_query($mysqli,$qry_users));
$total_users = $total_users['num'];

$qry_moneyspends="SELECT SUM(tbl_coin_list.c_amount) AS num FROM tbl_wallet_passbook JOIN tbl_coin_list ON tbl_wallet_passbook.wp_package_id = tbl_coin_list.c_id";
$total_moneyspends = mysqli_fetch_array(mysqli_query($mysqli,$qry_moneyspends));
$total_moneyspends = $total_moneyspends['num'];

$qry_redeem="SELECT COUNT(*) as num FROM tbl_offers  WHERE o_type = '3'";
$total_redeem = mysqli_fetch_array(mysqli_query($mysqli,$qry_redeem));
$total_redeem = $total_redeem['num'];

$qry_shop="SELECT COUNT(*) as num FROM tbl_offers  WHERE o_type = '9'";
$total_shop = mysqli_fetch_array(mysqli_query($mysqli,$qry_shop));
$total_shop = $total_shop['num'];

$qry_auction="SELECT COUNT(*) as num FROM tbl_offers  WHERE o_type IN (1,2,7,8)";
$total_auction = mysqli_fetch_array(mysqli_query($mysqli,$qry_auction));
$total_auction = $total_auction['num'];

$qry_lottery="SELECT COUNT(*) as num FROM tbl_offers  WHERE o_type = 5";
$total_lottery = mysqli_fetch_array(mysqli_query($mysqli,$qry_lottery));
$total_lottery = $total_lottery['num'];

$qry_bids="SELECT COUNT(*) as num FROM tbl_bid";
$total_bids = mysqli_fetch_array(mysqli_query($mysqli,$qry_bids));
$total_bids = $total_bids['num'];

$qry_tickets="SELECT COUNT(*) as num FROM tbl_ticket";
$total_tickets = mysqli_fetch_array(mysqli_query($mysqli,$qry_tickets));
$total_tickets = $total_tickets['num'];

$qry_coinspends = "SELECT ROUND(SUM(money)) AS num FROM tbl_transaction";
$total_coinspends = mysqli_fetch_array(mysqli_query($mysqli,$qry_coinspends));
$total_coinspends = $total_coinspends['num'];
 
$qry_network="SELECT COUNT(*) as num FROM tbl_network";
$total_network = mysqli_fetch_array(mysqli_query($mysqli,$qry_network));
$total_network = $total_network['num'];

$qry_vendor="SELECT COUNT(*) as num FROM tbl_vendor";
$total_vendor = mysqli_fetch_array(mysqli_query($mysqli,$qry_vendor));
$total_vendor = $total_vendor['num'];

$qry_orders="SELECT COUNT(*) as num FROM tbl_order";
$total_orders = mysqli_fetch_array(mysqli_query($mysqli,$qry_orders));
$total_orders = $total_orders['num'];

$qry_banner="SELECT COUNT(*) as num FROM tbl_offers  WHERE o_type = '6'";
$total_banner = mysqli_fetch_array(mysqli_query($mysqli,$qry_banner));
$total_banner = $total_banner['num'];

$qry_package="SELECT COUNT(*) as num FROM tbl_coin_list";
$total_package = mysqli_fetch_array(mysqli_query($mysqli,$qry_package));
$total_package = $total_package['num'];

$qry_category="SELECT COUNT(*) as num FROM tbl_cat";
$total_category = mysqli_fetch_array(mysqli_query($mysqli,$qry_category));
$total_category = $total_category['num'];

$qry_items="SELECT COUNT(*) as num FROM tbl_items";
$total_items = mysqli_fetch_array(mysqli_query($mysqli,$qry_items));
$total_items = $total_items['num'];

$qry_currency="SELECT currency FROM tbl_settings";
$get_currency = mysqli_fetch_array(mysqli_query($mysqli,$qry_currency));
$currency = $get_currency['currency'];
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
    gap: 10px;
    margin: 5px;
}

.chart-box, .pie-chart-box {
    flex: 1;
    min-width: 30%; /* Ensure a minimum width for each chart box */
    max-width: 45%; /* Limit the maximum width to prevent stretching */
    box-sizing: border-box;
}

.canvas-wrapper {
    background-color: #fff;
    padding: 10px;
    border-radius: 12px;
    border-color: #fafafa;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

#auctionBidsChart, #auctionBidsPieChart, #auctionBidsPointChart, #topBidderChart, #topTicketChart {
    height: 500px !important; /* Set larger height for charts */
}

/* Responsive adjustments */
@media (max-width: 1200px) {
    .chart-box, .pie-chart-box {
        flex: 1 1 48%; /* Adjust flex basis for medium screens */
        max-width: 48%;
    }

    #auctionBidsChart, #auctionBidsPieChart, #auctionBidsPointChart, #topBidderChart, #topTicketChart {
        height: 450px !important; /* Slightly reduce height for medium screens */
    }
}

@media (max-width: 768px) {
    .chart-box, .pie-chart-box {
        flex: 1 1 100%; /* Full width on smaller screens */
    }

    #auctionBidsChart, #auctionBidsPieChart, #auctionBidsPointChart, #topBidderChart, #topTicketChart {
        height: 400px !important; /* Reduce height for smaller screens */
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
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
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
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
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
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="users.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-user"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_user']; ?></p>
                        <p class="card-value"><?php echo $total_users; ?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="referrals.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-users"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_referral']; ?></p>
                        <p class="card-value"><?php echo $total_network;?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="participations.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-coins"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_spends']; ?></p>
                        <p class="card-value">
                            <?php echo $total_coinspends; ?> <i class="icon fa fa-coins"></i>
                        </p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="coin_purchases.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-money"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_payment']; ?></p>
                        <p class="card-value"><?php echo $get_currency['currency'].$total_moneyspends;?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="vendors.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fi fi-sr-seller"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_seller']; ?></p>
                        <p class="card-value"><?php echo $total_vendor;?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
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
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="shop.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-gift"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_redeem_items']; ?></p>
                        <p class="card-value"><?php echo $total_redeem; ?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
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
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="coin_packages.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-money"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_recharge_pack']; ?></p>
                        <p class="card-value"><?php echo $total_package; ?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="banners.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-image"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_banner']; ?></p>
                        <p class="card-value"><?php echo $total_banner;?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
        
        <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="category.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fa fa-list"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_category']; ?></p>
                        <p class="card-value"><?php echo $total_category;?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
    
    <div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
            <a href="items.php" class="card-link">
                <div class="card">
                    <div class="card-icon">
                        <i class="icon fi fi-sr-boxes"></i>
                    </div>
                    <div class="card-content">
                        <p class="card-title"><?php echo $client_lang['total_items']; ?></p>
                        <p class="card-value"><?php echo $total_items;?></p>
                    </div>
                    <!--<div class="card-arrow">-->
                    <!--    <span>&gt;</span>-->
                    <!--</div>-->
                </div>
            </a>
        </div>
    </div>
    
    <div class="admob_title"><?php echo $client_lang['stats']; ?></div>
    <div class="chart-container">
        <div class="chart-box">
            <div class="canvas-wrapper">
                <canvas id="auctionBidsChart"></canvas>
            </div>
        </div>
        <div class="chart-box">
            <div class="canvas-wrapper">
                <canvas id="topBidderChart"></canvas>
            </div>
        </div>
        <div class="chart-box">
            <div class="canvas-wrapper">
                <canvas id="topTicketChart"></canvas>
            </div>
        </div>
    </div>  
    
    
    <!--<div class="chart-container">-->
    <!--    <div class="pie-chart-box">-->
    <!--        <div class="canvas-wrapper">-->
    <!--            <canvas id="auctionBidsPieChart"></canvas>-->
    <!--        </div>-->
    <!--    </div>-->
    <!--    <div class="chart-box">-->
    <!--        <div class="canvas-wrapper">-->
    <!--            <canvas id="auctionBidsPointChart"></canvas>-->
    <!--        </div>-->
    <!--    </div>-->
    <!--</div>  -->
    
    <?php
// SQL query to get auction data
$sql = "
   SELECT 
    tbl_offers.o_id AS auction_id, 
    tbl_items.o_name AS item_name, 
    COUNT(tbl_bid.bd_id) AS total_bids,
    tbl_offers.o_date,
    tbl_offers.o_edate
FROM 
    tbl_bid
LEFT JOIN 
    tbl_offers ON tbl_bid.o_id = tbl_offers.o_id
LEFT JOIN 
    tbl_items ON tbl_items.item_id = tbl_offers.item_id
WHERE 
    tbl_bid.bd_date >= NOW() - INTERVAL 2 DAY
GROUP BY 
    tbl_offers.o_id, tbl_items.o_name
ORDER BY 
    total_bids DESC;
";

// Execute the query
$result = mysqli_query($mysqli, $sql);

// Initialize arrays to store the data
$auctionData = [];
$auctionNames = [];
$totalBids = 0;

// Fetch the data and store it in arrays
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $auctionData[] = $row['total_bids'];
        $auctionNames[] = $row['item_name'];
        $totalBids += $row['total_bids'];
    }
} 


$topBidderChartQuery = "
   SELECT 
    tbl_users.name AS user_name,
    COUNT(tbl_bid.bd_id) AS total_bids
FROM 
    tbl_bid
LEFT JOIN 
    tbl_users ON tbl_bid.u_id = tbl_users.id
WHERE 
    tbl_bid.bd_date >= NOW() - INTERVAL 2 DAY
GROUP BY 
    tbl_users.name
ORDER BY 
    total_bids DESC
LIMIT 10; -- as we want to show the top 10 bidders";

// Execute the query
$topBidderChartResult = mysqli_query($mysqli, $topBidderChartQuery);

// Initialize arrays to store the data
$totalUserBids = [];
$userNames = [];

// Fetch the data and store it in arrays
if (mysqli_num_rows($topBidderChartResult) > 0) {
    while ($topBidderChartRow = mysqli_fetch_assoc($topBidderChartResult)) {
        $totalUserBids[] = $topBidderChartRow['total_bids'];
        $userNames[] = $topBidderChartRow['user_name'];
    }
}


$topTicketChartQuery = "
   SELECT 
    tbl_users.name AS user_name,
    COUNT(tbl_ticket.ticket_id) AS total_bids
FROM 
    tbl_ticket
LEFT JOIN 
    tbl_users ON tbl_ticket.u_id = tbl_users.id
WHERE 
    tbl_ticket.purchase_date >= NOW() - INTERVAL 2 DAY
GROUP BY 
    tbl_users.name
ORDER BY 
    total_bids DESC
LIMIT 10; -- as we want to show the top 10 buyers";

// Execute the query
$topTicketChartResult = mysqli_query($mysqli, $topTicketChartQuery);

// Initialize arrays to store the data
$totalUserTicket = [];
$userNamesTicket = [];

// Fetch the data and store it in arrays
if (mysqli_num_rows($topTicketChartResult) > 0) {
    while ($topTicketChartRow = mysqli_fetch_assoc($topTicketChartResult)) {
        $totalUserTicket[] = $topTicketChartRow['total_bids'];
        $userNamesTicket[] = $topTicketChartRow['user_name'];
    }
}

// Close the connection
mysqli_close($mysqli);
?>

    <script>
         // Top Auctions Chart
         const ctxBar = document.getElementById('auctionBidsChart').getContext('2d');
        const auctionBidsChart = new Chart(ctxBar, {
            type: 'bar',
            data: {
                labels: <?php echo json_encode($auctionNames); ?>,
                datasets: [{
                    label: 'Total Bids',
                    data: <?php echo json_encode($auctionData); ?>,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        ticks: {
                            display: false, //make it true to see x lable
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Number of Bids'
                        }
                    }
                },
                plugins: {
                    title: {
                        display: true,
                        text: 'Auction Bids in Past 48 Hours'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(tooltipItem) {
                                const auctionName = tooltipItem.label;
                                const bidCount = tooltipItem.raw;
                                return auctionName + ': ' + bidCount + ' Bids';
                            }
                        }
                    }
                }
            }
        });
        
        // Top Bidder Bar Chart
         const topBidderChartBar = document.getElementById('topBidderChart').getContext('2d');
        const topBidderChart = new Chart(topBidderChartBar, {
            type: 'bar',
            data: {
                labels: <?php echo json_encode($userNames); ?>,
                datasets: [{
                    label: 'Total Bids',
                    data: <?php echo json_encode($totalUserBids); ?>,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        ticks: {
                            display: true,
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Number of Bids'
                        }
                    }
                },
                plugins: {
                    title: {
                        display: true,
                        text: 'Top 10 Bidders in last 48 Hours'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(tooltipItem) {
                                const auctionName = tooltipItem.label;
                                const bidCount = tooltipItem.raw;
                                return auctionName + ': ' + bidCount + ' Bids';
                            }
                        }
                    }
                }
            }
        });
        
        // Top Ticket Buyer Bar Chart
         const topTicketChartBar = document.getElementById('topTicketChart').getContext('2d');
        const topTicketChart = new Chart(topTicketChartBar, {
            type: 'bar',
            data: {
                labels: <?php echo json_encode($userNamesTicket); ?>,
                datasets: [{
                    label: 'Total Tickets',
                    data: <?php echo json_encode($totalUserTicket); ?>,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        ticks: {
                            display: true,
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Number of Tickets'
                        }
                    }
                },
                plugins: {
                    title: {
                        display: true,
                        text: 'Top 10 Ticket Buyers in last 48 Hours'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(tooltipItem) {
                                const auctionName = tooltipItem.label;
                                const bidCount = tooltipItem.raw;
                                return auctionName + ': ' + bidCount + ' Bids';
                            }
                        }
                    }
                }
            }
        });

       // Pie Chart
        const ctxPie = document.getElementById('auctionBidsPieChart').getContext('2d');
        const auctionBidsPieChart = new Chart(ctxPie, {
            type: 'pie',
            data: {
                labels: <?php echo json_encode($auctionNames); ?>,
                datasets: [{
                    label: 'Bid Distribution',
                    data: <?php echo json_encode($auctionData); ?>,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)',
                        'rgba(255, 205, 86, 0.2)',
                        'rgba(201, 203, 207, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)',
                        'rgba(255, 205, 86, 1)',
                        'rgba(201, 203, 207, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: 'Bid Distribution by Auction'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(tooltipItem) {
                                const auctionName = tooltipItem.label;
                                const bidCount = tooltipItem.raw;
                                const percentage = ((bidCount / <?php echo $totalBids; ?>) * 100).toFixed(2);
                                return auctionName + ': ' + bidCount + ' Bids (' + percentage + '%)';
                            }
                        }
                    },
                    legend: {
                        display: true // Show pie chart legend
                    }
                }
            }
        });
        // Point Chart
        const ctxPoint = document.getElementById('auctionBidsPointChart').getContext('2d');
        const auctionBidsPointChart = new Chart(ctxPoint, {
            type: 'line',
            data: {
                labels: <?php echo json_encode($auctionNames); ?>,
                datasets: [{
                    label: 'Total Bids',
                    data: <?php
                        $pointData = [];
                        foreach ($auctionNames as $index => $name) {
                            $pointData[] = ['x' => $index + 1, 'y' => $auctionData[$index], 'label' => $name];
                        }
                        echo json_encode($pointData);
                    ?>,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    pointRadius: 5,
                    fill: false,
                    tension: 0.1,
                    showLine: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false, // Allow the chart to resize properly
                scales: {
                    x: {
                        type: 'linear',
                        position: 'bottom',
                        title: {
                            display: true,
                            text: 'Auction Index'
                        },
                        ticks: {
                            callback: function(value, index, values) {
                                return value; // Use the numeric value for x-axis labels
                            }
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Number of Bids'
                        }
                    }
                },
                plugins: {
                    title: {
                        display: true,
                        text: 'Auction Bids Scatter Plot with Line'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                let label = context.raw.label || '';
                                if (label) {
                                    label += ': ';
                                }
                                label += context.raw.y;
                                return label;
                            }
                        }
                    }
                }
            }
        });

    </script>
   
    </script>


        
<?php include("includes/footer.php");?>       
