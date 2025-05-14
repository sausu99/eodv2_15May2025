<?php include('includes/header.php'); 
    include('includes/connection.php');
    include('includes/function.php');
	include('language/language.php'); 
	
	
	
	$querytime = "SELECT timezone FROM tbl_settings";
    $resulttime = mysqli_query($mysqli, $querytime);
    $rowtime = mysqli_fetch_assoc($resulttime);
 
    date_default_timezone_set($rowtime['timezone']);
	$time = date('H:i:s');
	$date1 = date('Y-m-d');
	$datetime = date('Y-m-d H:i:s');

	
	function getCurrencyFromSettings($mysqli) {
    // Query to fetch the currency from tbl_settings
    $query = "SELECT currency FROM tbl_settings";
    
    // Execute the query
    $result = mysqli_query($mysqli, $query);

    if ($result) {
        // Check if a row is returned
        if (mysqli_num_rows($result) > 0) {
            // Fetch the currency value from the result
            $row = mysqli_fetch_assoc($result);
            return $row['currency'];
        }
    }

    // Return a default currency value or handle errors as needed
    return '$'; // Default currency value (you can change this)
}

	if(isset($_POST['user_search']))
	 {
		 
		
		$user_qry="SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE o_edate <= '".$date1."' and o_type IN ('1', '2', '4','5', '7', '8', '10', '11') and o_status = 1 ORDER BY tbl_offers.o_edate DESC, tbl_offers.o_etime DESC";  
		$users_result=mysqli_query($mysqli,$user_qry);
		
		 
	 }
	 else
	 {

							$tableName="tbl_offers";		
							$targetpage = "winners.php"; 	
							$limit = 25; 
							
							$query = "SELECT COUNT(*) as num FROM $tableName LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE o_edate <= '".$date1."' and o_type IN ('1', '2', '4','5', '7', '8', '10', '11') and o_status = 1  ORDER BY tbl_offers.o_edate DESC, tbl_offers.o_etime DESC";
							$total_pages = mysqli_fetch_array(mysqli_query($mysqli,$query));
							$total_pages = $total_pages['num'];
							
							$stages = 3;
							$page=0;
							if(isset($_GET['page'])){
							$page = mysqli_real_escape_string($mysqli,$_GET['page']);
							}
							if($page){
								$start = ($page - 1) * $limit; 
							}else{
								$start = 0;	
								}	
							
			
						 $users_qry = "SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE CONCAT(o_date, ' ', o_stime) <= '$datetime' AND CONCAT(o_edate, ' ', o_etime) < '$datetime' AND o_type IN ('1', '2', '4', '5', '7', '8', '10', '11') AND o_status = 1  ORDER BY tbl_offers.o_edate DESC, tbl_offers.o_etime DESC LIMIT $start, $limit";
                         $users_result=mysqli_query($mysqli,$users_qry);
							
	 }
	
?>

<style>
    /* Page Title Block */
    .page_title_block {
        margin-bottom: 20px;
    }

    .page_title {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 5px;
    }

    .page_subtitle {
        font-size: 16px;
        color: #777;
    }

    /* Alert Box */
    .alert {
        margin-bottom: 20px;
    }

    /* Table */
    .table {
        width: 100%;
        border-collapse: collapse;
        border-spacing: 0;
    }

    .table th, .table td {
        padding: 8px;
        vertical-align: top;
        border-top: 1px solid #dee2e6;
    }

    .table th {
        background-color: #f2f2f2;
        font-weight: bold;
        text-align: left;
    }

    /* Button */
    .btn {
        display: inline-block;
        padding: 6px 12px;
        margin-bottom: 0;
        font-size: 14px;
        font-weight: normal;
        line-height: 1.42857143;
        text-align: center;
        white-space: nowrap;
        vertical-align: middle;
        cursor: pointer;
        border: 1px solid transparent;
        border-radius: 4px;
        text-decoration: none;
    }

    .btn-success {
        color: #fff;
        background-color: #5cb85c;
        border-color: #4cae4c;
    }

    /* Pagination */
    .pagination_item_block {
        text-align: center;
    }

    .pagination {
        display: inline-block;
        padding-left: 0;
        margin: 20px 0;
        border-radius: 4px;
    }

    .pagination>li {
        display: inline;
    }

    .pagination>li>a, .pagination>li>span {
        position: relative;
        float: left;
        padding: 6px 12px;
        line-height: 1.42857143;
        text-decoration: none;
        color: #337ab7;
        background-color: #fff;
        border: 1px solid #ddd;
        margin-left: -1px;
    }

    .pagination>li:first-child>a, .pagination>li:first-child>span {
        margin-left: 0;
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
    }

    .pagination>li:last-child>a, .pagination>li:last-child>span {
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
    }

    .pagination>li>a:hover, .pagination>li>span:hover, .pagination>li>a:focus, .pagination>li>span:focus {
        color: #23527c;
        background-color: #eee;
        border-color: #ddd;
    }

    .pagination>.active>a, .pagination>.active>span, .pagination>.active>a:hover, .pagination>.active>span:hover, .pagination>.active>a:focus, .pagination>.active>span:focus {
        z-index: 2;
        color: #fff;
        background-color: #337ab7;
        border-color: #337ab7;
        cursor: default;
    }

    .pagination>.disabled>span, .pagination>.disabled>span:hover, .pagination>.disabled>span:focus, .pagination>.disabled>a, .pagination>.disabled>a:hover, .pagination>.disabled>a:focus {
        color: #777;
        background-color: #fff;
        border-color: #ddd;
        cursor: not-allowed;
    }
</style>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
<title><?php echo $winners_lang['title']; ?></title>

 <div class="card mrg_bottom">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $winners_lang['title']; ?></div>
                    <h5 class="page_subtitle"><?php echo $winners_lang['description']; ?></h5>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])){ ?>
                            <div class="alert alert-success alert-dismissible" role="alert">
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                                <?php echo $client_lang[$_SESSION['msg']]; ?>
                            </div>
                        <?php unset($_SESSION['msg']);} ?>
                    </div>
                </div>
            </div>
            <div class="col-md-12 mrg-top">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <th><?php echo $winners_lang['item']; ?></th>
                            <!--<th><?php echo $winners_lang['winner']; ?></th>-->
                            <!--<th><?php echo $winners_lang['value']; ?></th>-->
                            <th><?php echo $winners_lang['wonOn']; ?></th>
                            <!--<th><?php echo $winners_lang['type']; ?></th>-->
                            <th><?php echo $winners_lang['status']; ?></th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        $i=0;
                        while($users_row=mysqli_fetch_array($users_result))
                        {
                            $winner_type = $users_row['winner_type'];
                            $winner_name = $users_row['winner_name'];
                            $o_id = $users_row['o_id'];
                            
                            if ($winner_type == 1 & $winner_name != "Winner Announced") {
                                selectAndInsertWinners($mysqli, $o_id);
                            }
                        ?>
                        <tr>
                            <td>
                                <?php 

                                echo '<img src="/seller/images/thumbs/'.$users_row['o_image'].'" alt="'.$users_row['o_name'].'" class="lazyload" style="max-width: 100px; max-height: 100px;" onerror="this.onerror=null;this.src=\'placeholder.jpg\';">';
                                ?><br>
                                <strong><?php echo $users_row['o_name'];?></strong>
                            </td>

                            <!--<td><?php echo empty($users_row['winner_name']) ? $client_lang['no_winners_yet'] : $users_row['winner_name']; ?></td>
                            <td>
                                <?php
                                if (empty($users_row['winning_value'])) {
                                    echo $winners_lang['value'];
                                } else {
                                    if (in_array($users_row['o_type'], [1, 2, 7, 8, 10, 11])) {
                                        $currency = getCurrencyFromSettings($mysqli);
                                        echo $currency . $users_row['winning_value'];
                                    } elseif (in_array($users_row['o_type'], [4, 5])) {
                                        echo '#' . $users_row['winning_value'];
                                    } else {
                                        echo $users_row['winning_value'];
                                    }
                                }
                                ?>
                            </td>-->
                            <td><?php echo $users_row['o_edate'];?><br>
                            
                                <?php 
                                if (in_array($users_row['o_type'], array(1, 2, 7, 8))) {
                                    echo $winners_lang['auction'];
                                } else if ($users_row['o_type'] == 4) {
                                    echo $winners_lang['luckyDraw'];
                                } else if ($users_row['o_type'] == 5) {
                                    echo $winners_lang['lottery'];
                                } else if ($users_row['o_type'] == 3) {
                                    echo $winners_lang['redeem'];
                                }
                                ?>
                            </td>
                            <td>
                                <?php
                                // Define the auction types that should use the 'auction' link
                                $auction_types = [1, 2, 7, 8, 10, 11];
                                
                                // Determine the base URL based on o_type
                                $base_url = in_array($users_row['o_type'], $auction_types) ? 'auction' : 'lottery';
                                
                                // Prepare the rest of the URL
                                $lowercaseString = strtolower($users_row['o_name']);
                                $finalString = str_replace(' ', '-', $lowercaseString);
                                $url = $base_url . '/' . $finalString . '/' . $users_row['o_id'];
                                ?>
                                <a href="<?php echo $url; ?>" class="btn btn-success"><i class="fa fa-eye"></i>&nbsp;<?php echo $client_lang['viewDetails']; ?></a>
                            </td>
                        </tr>
                        <?php
                        $i++;
                            
                        }
                        ?>
                    </tbody>
                </table>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="pagination_item_block">
                    <!-- Pagination controls can be added here -->
                </div>
            </div>
            <div class="clearfix"></div>
        </div>

<?php include('includes/footer.php');?>
