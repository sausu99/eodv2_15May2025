<?php include('includes/header.php');
include("includes/session_check.php");

    include('includes/function.php');
	include('language/language.php'); 

 	require_once("thumbnail_images.class.php");

	if(isset($_GET['o_id']))
	{
	
			
		 $user_qry="SELECT * FROM tbl_offers
		  	where tbl_bid.o_id='".$_GET['o_id']."' ";  
		  
		  
			$user_result=mysqli_query($mysqli,$user_qry);
			$user_row=mysqli_fetch_assoc($user_result);
		
	}
           // INSERT INTO `tbl_order`(`o_id`, `u_id`, `offer_id`, `total_amount`, `dis_amount`, `pay_amount`, `o_address`, `o_date`, `o_status`)	 
	

	
?>


<style>







#table {
  width:100%;
}
#table, #th, #td {
  border: 1px solid #dfd7ca;
  border-collapse: collapse;
}
#th, #td {
  padding: 15px;
  text-align: left;
}
#table#t01 #tr:nth-child(even) {
  background-color: #eee;
}
#table#t01 #tr:nth-child(odd) {
 background-color: #fff;
}
#table#t01 #th {
  background-color: white;
  color: black;
}


table {
  width:100%;
}
table, th, td {
  border: 0px solid #dfd7ca;
  border-collapse: collapse;
      margin-bottom: 10px;
}
th, td {
  padding: 0px;
  text-align: left;
}


.line {
  width:40%;
  
  margin-left: 600px;
}

 th, td {
  border: 0px solid #dfd7ca;
  border-collapse: collapse;
      margin-bottom: 10px;
}




</style>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>



 <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
            <div class="page_title">View Winner</div>
           
            </div>
          </div>
          <div class="clearfix"></div>
          <div class="row mrg-top">
            <div class="col-md-12">
               
              <div class="col-md-12 col-sm-12">
                <?php if(isset($_SESSION['msg'])){?> 
               	 <div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                	<?php echo $client_lang[$_SESSION['msg']] ; ?></a> </div>
                <?php unset($_SESSION['msg']);}?>	
              </div>
            </div>
          </div>
          <div class="card-body mrg_bottom"> 
            <form action="" name="addedituser" method="post" class="form form-horizontal" enctype="multipart/form-data" >
            	<input  type="hidden" name="o_id" value="<?php echo $_GET['o_id'];?>" />
            	<?php
        	date_default_timezone_set("Asia/Calcutta");   //India time (GMT+5:30)
		 $time = date('H:i:s');

		 $date1 = date('Y-m-d');
		 
		 
		  date_default_timezone_set("Asia/Calcutta");   //India time (GMT+5:30)
        $datetime = date('Y-m-d H:i:s');
        
	    $query="SELECT * FROM tbl_offers WHERE o_date <= '".$date1."' and o_status = 1 and tbl_offers.o_id='".$_GET['o_id']."'";
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());

		while($data = mysqli_fetch_assoc($sql))
		{
		     date_default_timezone_set("Asia/Calcutta");   //India time (GMT+5:30)
        $datetime = date('Y-m-d H:i:s');
        
		      $o_date = $data['o_date'];
		    $o_stime = $data['o_stime'];
		    
		    $o_edate = $data['o_edate'];
		    $o_etime =  $data['o_etime'];

		    $start = $o_date." ".$o_stime;
		    $end = $o_edate." ".$o_etime;
		    
		    if( $end <= $datetime )
		  {
            if($data['o_type'] == 1)
            {            
            		      $query1="SELECT *, COUNT(*)
             as num1 FROM tbl_bid 
             left join tbl_users on tbl_users.id = tbl_bid.u_id
             where tbl_bid.o_id='".$data['o_id']."'
             GROUP BY bd_value having num1 = 1  ORDER BY `tbl_bid`.`bd_value` ASC LIMIT 0,1 ";

		    $result1 = mysqli_query($mysqli,$query1)or die(mysqli_error());

		   $row1=mysqli_fetch_assoc($result1);

            }else if($data['o_type'] == 2)
            {
              $query1="SELECT *, COUNT(*)
             as num1 FROM tbl_bid 
             left join tbl_users on tbl_users.id = tbl_bid.u_id
             where tbl_bid.o_id='".$data['o_id']."'
             GROUP BY bd_value having num1 = 1  ORDER BY `tbl_bid`.`bd_value` DESC LIMIT 0,1 ";

		    $result1 = mysqli_query($mysqli,$query1)or die(mysqli_error());

		   $row1=mysqli_fetch_assoc($result1);
            }
		  }
    
		  	?>
		  	 <div class="col-md-4" >
		  	 </div>
		  	 <div class="col-md-4" >
		  	 </div>
                    <div class="col-md-4" >
                    <center><h5> <b>User Details</b> </h5></center>
                       <hr />
                    <div class="form-group">
                        <label class="col-md-12 control-label"><b>User Name:- </b><?php if(isset($_GET['o_id'])){echo $row1['name'];}?> </label>
                     </div>
                  
                    <div class="form-group">
                      <label class="col-md-12 control-label"><b>Email ID:-  </b> <?php if(isset($_GET['o_id'])){echo $row1['email'];}?></label>
                    </div>
                    <div class="form-group">
                      <label class="col-md-12 control-label"><b>Mobile Number:-  </b> <?php if(isset($_GET['o_id'])){echo $row1['phone'];}?></label>
                    </div>
                  </div>
                 <table id='table'>   

                 <div class="col-md-12 mrg-top">
                  <table id="t01" class="table table-striped table-bordered table-hover">
                     <tr id='tr'>
                         <th id='th'>Auction Name</th>
                         <th id='th'>Auction Image</th>
                         <th id='th'>Bid Date</th>
                         <th id='th'>Bid value</th>
                         <th id='th'>Cost Per Bid</th>
                    </tr>
                <tr>
                    <td>
                     <?php if(isset($_GET['o_id'])){echo $data['o_name'];}?>
                    </td>
                    <td>
                        <span class="category_img"><img src="images/thumbs/<?php echo $data['o_image'];?>" /></span>
                    </td>
                    <td>
                     <?php if(isset($_GET['o_id'])){echo $row1['bd_date'];}?>
                    </td>
                    <td>
                     <?php if(isset($_GET['o_id'])){echo $row1['bd_value'];}?>
                    </td>
                    <td>
                     <?php if(isset($_GET['o_id'])){echo $row1['bd_amount'];}?>
                    </td>
                    
                </tr>
    <?php 
		  
            	
            }	   
      ?>

  
   </div>
   </table>
   </table>



      
                </div>
              </div>
            </form>
            
            
          </div>
        </div>
      </div>
    </div>
 
 
<?php include('includes/footer.php');?>                  