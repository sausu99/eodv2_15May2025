<?php 
include('includes/header.php'); 
include('includes/function.php');
include('language/language.php');  

$querytime = "SELECT timezone FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
date_default_timezone_set($rowtime['timezone']);
$datetime = date('Y-m-d H:i:s'); // Current datetime
$date = date('Y-m-d'); // Current date
$time = date('H:i:s'); // Current time

if(isset($_POST['user_search'])) {
	$user_qry = "SELECT * FROM tbl_offers LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id WHERE tbl_items.o_name like '%".addslashes($_POST['search_value'])."%' ORDER BY tbl_offers.o_id DESC";  
	$users_result = mysqli_query($mysqli,$user_qry);
}
else {
	$tableName = "tbl_offers";		
	$targetpage = "slot-auction.php"; 	
	$limit = 15; 
	
	$query = "SELECT COUNT(*) as num FROM $tableName WHERE o_type IN ('11')";
	$total_pages = mysqli_fetch_array(mysqli_query($mysqli,$query));
	$total_pages = $total_pages['num'];
	
	$stages = 3;
	$page = 0;
	if(isset($_GET['page'])) {
		$page = mysqli_real_escape_string($mysqli,$_GET['page']);
	}
	if($page) {
		$start = ($page - 1) * $limit; 
	} else {
		$start = 0;	
	}	
	
	$users_qry = "SELECT * FROM tbl_offers
	LEFT JOIN tbl_items ON tbl_items.item_id = tbl_offers.item_id
	WHERE o_type = '11'
	ORDER BY tbl_offers.o_id DESC LIMIT $start, $limit";  
	$users_result = mysqli_query($mysqli,$users_qry);
}

if(isset($_GET['o_id'])) {
	Delete('tbl_offers','o_id='.$_GET['o_id'].'');
	$_SESSION['msg'] = "auction_delete";
	header("Location:slot-auction.php");
	exit;
}

// Active and Deactive status
if(isset($_GET['status_deactive_id'])) {
	$data = array('o_status'  =>  '0');
	$edit_status = Update('tbl_offers', $data, "WHERE o_id = '".$_GET['status_deactive_id']."'");
	$_SESSION['msg'] = "auction_disable";
	header("Location:slot-auction.php");
	exit;
}
if(isset($_GET['status_active_id'])) {
	$data = array('o_status'  =>  '1');
	$edit_status = Update('tbl_offers', $data, "WHERE o_id = '".$_GET['status_active_id']."'");
	$_SESSION['msg'] = "auction_visible";
	header("Location:slot-auction.php");
	exit;
}

$querytime = "SELECT currency FROM tbl_settings";
$resulttime = mysqli_query($mysqli, $querytime);
$rowtime = mysqli_fetch_assoc($resulttime);
$currency = $rowtime['currency'];

?>

<head>
<title><?php echo $client_lang['slot_auction']; ?></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
</head>
<style>
    .btn-download {
        background-color: #6f42c1; /* Purple background color */
        color: #fff; /* White text color */
    }
    
    .btn-download:hover {
        background-color: #5a32a3; /* Darker purple color on hover */
        color: #fff; /* White text color */
    }
</style>
<div class="row">
    <div class="col-xs-12">
        <div class="card mrg_bottom">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $client_lang['slot_auction']; ?></div>
                    <p class="control-label-help"><?php echo $client_lang['slot_auction_manage']; ?> <a href="https://documentation.wowcodes.in/guide/slot-auction.html" target="_blank"><?php echo $client_lang['learn_more']; ?> -></a></p>
                </div>
                <div class="col-md-7 col-xs-12">
                    <div class="search_list">
                        <div class="search_block">
                            <form method="post" action="">
                                <input class="form-control input-sm" placeholder="<?php echo $client_lang['search_auction']; ?>" aria-controls="DataTables_Table_0" type="search" name="search_value" required>
                                <button type="submit" name="user_search" class="btn-search"><i class="fa fa-search"></i></button>
                            </form>
                        </div>
                        <div class="add_btn_primary"> <a href="add_slot-auction.php?add"><i class="fa fa-plus"></i>&nbsp;&nbsp;<?php echo $client_lang['add_auction']; ?></a> </div>
                    </div>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])){?> 
                        <div class="alert alert-success alert-dismissible" role="alert"> 
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                            <?php echo $client_lang[$_SESSION['msg']] ; ?> 
                        </div>
                        <?php unset($_SESSION['msg']);}?>    
                    </div>
                </div>
            </div>
            <div class="col-md-12 mrg-top">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <th><?php echo $client_lang['auction_item']; ?></th>  
                            <th><?php echo $client_lang['auction_status']; ?></th>
                            <th class="cat_action_list"><?php echo $client_lang['action']; ?></th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        $i=0;
                        while($users_row = mysqli_fetch_array($users_result)) {
                            // Check if there are bids placed for this auction
                            $bid_count_query = "SELECT COUNT(*) as num FROM tbl_bid WHERE o_id = '".$users_row['o_id']."'";
                            $bid_count_result = mysqli_query($mysqli, $bid_count_query);
                            $bid_count_row = mysqli_fetch_assoc($bid_count_result);
                            $num_bids = $bid_count_row['num'];

                            // Determine auction status based on current time and bid count
                            $current_time = time();
                            $start_time = strtotime($users_row['o_date'] . ' ' . $users_row['o_stime']);
                            $end_time = strtotime($users_row['o_edate'] . ' ' . $users_row['o_etime']);
                            
                            $o_status = $users_row['o_status'];
                            
                            if ($o_status == 0) {
                                $status = "<span class='badge badge-danger badge-icon'><i class='fa fa-eye-slash' aria-hidden='true'></i><span>{$client_lang['auction_is_hidden']}</span></span>";
                            }
                            else if ($current_time < $start_time) {
                                $status = "<span class='badge badge-warning badge-icon'><i class='fa fa-clock-o' aria-hidden='true'></i><span>{$client_lang['not_started']}</span></span><br>{$client_lang['starts_on']}: {$users_row['o_date']} {$client_lang['at']} {$users_row['o_stime']}";
                            } elseif ($current_time > $end_time) {
                                if ($num_bids > 0) {
                                    $status = "<span class='badge badge-success badge-icon'><i class='fa fa-trophy' aria-hidden='true'></i><span>{$client_lang['winner_announced']} | <strong>{$client_lang['winning_bid']}: $currency{$users_row['o_min']}</strong></span></span><br>{$client_lang['ended_on']}: {$users_row['o_edate']} {$client_lang['at']} {$users_row['o_etime']}<br><br>
                                        <center><a href='auction_winner.php?o_id={$users_row['o_id']}' class='btn btn-edit' title='View Winner'>
                                            <i class='fa fa-trophy'></i>&nbsp;{$client_lang['view_winner']}
                                        </a></center>";
                                } else {
                                    $status = "<span class='badge badge-danger badge-icon'><i class='fa fa-close' aria-hidden='true'></i><span>{$client_lang['no_bids']} </span></span><br>{$client_lang['ended_on']}: {$users_row['o_edate']} {$client_lang['at']} {$users_row['o_etime']}";
                                }
                            } else {
                                $status = "<span class='badge badge-info badge-icon'><i class='fa fa-gavel' aria-hidden='true'></i><span>{$client_lang['accepting_bid']} </span></span><br>{$client_lang['ends_on']}: {$users_row['o_edate']} {$client_lang['at']} {$users_row['o_etime']}";
                            }
                        ?>
                        <tr>
                            <td style="background-color: #fff; padding: 10px; border-radius: 4px;">
                                <img src="<?php echo file_exists('../seller/images/'.$users_row['o_image']) ? '../seller/images/'.$users_row['o_image'] : 'placeholder.jpg'; ?>" class="img-fluid img-thumbnail" alt="<?php echo $users_row['o_name']; ?>" style="width: 100px; height: auto;"><br>
                                <?php echo $users_row['o_name'];?><br>
                                <small>
                                    <?php echo $client_lang['sold_by']; ?> 
                                    <?php 
                                        $seller_email = get_vendor_info($users_row['id'], 'email'); 
                                        if (!empty($seller_email)) { 
                                            echo "<a href='view_selleritem.php?id={$users_row['id']}'>{$seller_email}</a>"; 
                                        } else { 
                                            echo "Admin"; 
                                        } 
                                    ?>
                                </small>
                            </td>
                            <td><?php echo $status; ?></td>
                            <td>
                                <div class="button-container">
                                    <?php if ($users_row['o_status'] == 0) { ?>
                                        <a href="slot-auction.php?status_active_id=<?php echo $users_row['o_id'];?>" onclick="return confirm('<?php echo $client_lang['show_auction']; ?>');" class="btn btn-danger" title="Show Auction">
                                            <i class="fa fa-eye-slash"></i>&nbsp;<?php echo $client_lang['hidden']; ?>
                                        </a>
                                    <?php } elseif ($users_row['o_status'] == 1) { ?>
                                        <a href="slot-auction.php?status_deactive_id=<?php echo $users_row['o_id'];?>" onclick="return confirm('<?php echo $client_lang['hide_auction']; ?>');" class="btn btn-success" title="Hide Auction">
                                            <i class="fa fa-check-square"></i>&nbsp;<?php echo $client_lang['visible']; ?>
                                        </a>
                                    <?php } ?>
                                    <a href="auction_details.php?o_id=<?php echo $users_row['o_id'];?>" class="btn btn-view" title="View Details"><i class="fa fa-eye"></i>&nbsp;<?php echo $client_lang['stats']; ?></a>
                                    <a href="add_slot-auction.php?o_id=<?php echo $users_row['o_id'];?>" class="btn btn-edit" title="Edit Auction"><i class="fa fa-pencil"></i>&nbsp;<?php echo $client_lang['edit']; ?></a>
                                    <a href="refund_game.php?o_id=<?php echo $users_row['o_id'];?>" class="btn btn-refund" title="Cancel & Refund"><i class="fa fa-history"></i>&nbsp;<?php echo $client_lang['refund']; ?></a>
                                    <a href="download.php?o_id=<?php echo $users_row['o_id'];?>" class="btn btn-download" title="Download Slots"><i class="fa fa-download"></i>&nbsp;<?php echo $client_lang['download_slot']; ?></a>
                                    <a href="slot-auction.php?o_id=<?php echo $users_row['o_id'];?>" onclick="return confirm('<?php echo $client_lang['delete_auction']; ?>');" class="btn btn-delete" title="Delete Auction"><i class="fa fa-trash"></i>&nbsp;<?php echo $client_lang['delete']; ?></a>
                                </div>
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
                    <nav>
                        <?php if(!isset($_POST["search"])) { include("pagination.php"); } ?>                 
                    </nav>
                </div>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
</div>     

<?php include('includes/footer.php'); ?>
