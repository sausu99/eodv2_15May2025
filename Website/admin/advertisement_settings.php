<?php 
include('includes/header.php');
include('includes/function.php');
include('language/language.php'); 
require_once("thumbnail_images.class.php");

// Secure connection
if (!isset($_SERVER['HTTPS']) || $_SERVER['HTTPS'] !== 'on') {
    header("Location: https://" . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI']);
    exit;
}

$qry="SELECT * FROM tbl_settings where id='1'";
$result=mysqli_query($mysqli,$qry);
$settings_row=mysqli_fetch_assoc($result);


if (isset($_POST['submit'])) {

        $data = array(
                'showad'  =>  sanitize($_POST['showad']),
                'ads_reward'  =>  sanitize($_POST['ads_reward']),
                'vungle_app'  =>  sanitize($_POST['vungle_app']),
                'vungle_placement_rewarded'  =>  sanitize($_POST['vungle_placement_rewarded']),
                'adcolony_app'  =>  sanitize($_POST['adcolony_app']),
                'adcolony_rewarded'  =>  sanitize($_POST['adcolony_rewarded']),
                'unity_game'  =>  sanitize($_POST['unity_game']),
                'unity_rewarded'  =>  sanitize($_POST['unity_rewarded']),
                'admob_banner'  =>  sanitize($_POST['admob_banner']),
                'admob_rewarded'  =>  sanitize($_POST['admob_rewarded']),
                'admob_interstitial'  =>  sanitize($_POST['admob_interstitial']),
                'fb_rewarded'  =>  sanitize($_POST['fb_rewarded']),
                'fb_interstitial'  =>  sanitize($_POST['fb_interstitial']),
                'fb_banner'  =>  sanitize($_POST['fb_banner']),
                'applovin_rewarded'  =>  sanitize($_POST['applovin_rewarded']),
                 'startio_rewarded'  =>  sanitize($_POST['startio_rewarded']),
                 'ironsource_rewarded'  =>  sanitize($_POST['ironsource_rewarded'])
        );

        $updateSettings = Update('tbl_settings', $data, "WHERE id = '1'");

    $_SESSION['msg'] = "23";
    header("location:advertisement_settings.php");
    exit;
}
?>
<head>
<title><?php echo $client_lang['advertisement_settings']; ?></title>
</head>

 <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php echo $client_lang['advertisement_settings']; ?></div>
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
              <div class="section">
                <div class="section-body">
                   
                  <div class="admob_title">Advertisement</div>
                <div class="form-group">
                      <label class="col-md-3 control-label">Banner Ad:-</label>
                      <div class="col-md-9">
                       <select name="showad" id="showad" class="select2">
                                <option value="1" <?php if($settings_row['showad']=='1'){?>selected<?php }?>>Yes, Show Banner ads</option>
                                <option value="0" <?php if($settings_row['showad']=='0'){?>selected<?php }?>>No, Don't show</option>
                        </select>
                      </div>
                    </div>
              
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Coin for each ads watch :-</label>
                      <div class="col-md-9">
                          <div class="input-group">
                      <input type="text" name="ads_reward" id="ads_reward" value="<?php echo $settings_row['ads_reward'];?>" class="form-control">
                       <span class="input-group-addon">Coins</span>
                    </div>
                 </div>
                </div> 
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #ea4235; color: #fff;">
                    <label class="control-label">Admob Ads (Google Ads)</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                    <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Banner ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="admob_banner" id="admob_banner" value="<?php echo $settings_row['admob_banner'];?>" class="form-control">
                      </div>
                    </div> 
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="admob_rewarded" id="admob_rewarded" value="<?php echo $settings_row['admob_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div> 
                
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #594195; color: #fff;">
                    <label class="control-label">Facebook Audience Network</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                    <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Banner ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="fb_banner" id="fb_banner" value="<?php echo $settings_row['fb_banner'];?>" class="form-control">
                      </div>
                    </div> 
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="fb_rewarded" id="fb_rewarded" value="<?php echo $settings_row['fb_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div> 
                
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #222c36; color: #fff;">
                    <label class="control-label">Unity Ads</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                    <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">App ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="unity_game" id="unity_game" value="<?php echo $settings_row['unity_game'];?>" class="form-control">
                      </div>
                    </div> 
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="unity_rewarded" id="unity_rewarded" value="<?php echo $settings_row['unity_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div> 
                
                
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #1d1d1b; color: #fff;">
                    <label class="control-label">Adcolony Ads</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                    <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">App ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="adcolony_app" id="adcolony_app" value="<?php echo $settings_row['adcolony_app'];?>" class="form-control">
                      </div>
                    </div> 
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="adcolony_rewarded" id="adcolony_rewarded" value="<?php echo $settings_row['adcolony_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div>
                
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #1085ab; color: #fff;">
                    <label class="control-label">Applovin Ads</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="applovin_rewarded" id="applovin_rewarded" value="<?php echo $settings_row['applovin_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div>
                
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #02db84; color: #fff;">
                    <label class="control-label">Startio Ads</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="startio_rewarded" id="startio_rewarded" value="<?php echo $settings_row['startio_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div>
                
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #11168e; color: #fff;">
                    <label class="control-label">Ironsource Ads</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="ironsource_rewarded" id="ironsource_rewarded" value="<?php echo $settings_row['ironsource_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div>
                
                <div class="banner_ads_block">
                  <div class="banner_ad_item" style="background-color: #0f2032; color: #fff;">
                    <label class="control-label">Vungle Ads</label>                                  
                  </div>
                  <div class="col-md-12">
                   
                    <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">App ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="vungle_app" id="vungle_app" value="<?php echo $settings_row['vungle_app'];?>" class="form-control">
                      </div>
                    </div> 
                     <div class="form-group">
                      <label class="col-md-3 control-label mr_bottom20">Rewarded Video ID :-</label>
                      <div class="col-md-9">
                      <input type="text" name="vungle_placement_rewarded" id="vungle_placement_rewarded" value="<?php echo $settings_row['vungle_placement_rewarded'];?>" class="form-control">
                      </div>
                    </div> 
                  </div>
                </div> 
                  
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="submit" class="btn btn-primary"><?php echo $client_lang['update'].' '.$client_lang['advertisement_settings']; ?></button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
   

<?php include('includes/footer.php');?>                  