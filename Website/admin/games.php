<?php 
include("includes/header.php");
require("includes/function.php");
require("language/language.php");

$qry = "SELECT * FROM tbl_games where id='1'";
$result = mysqli_query($mysqli, $qry);
$settings_row = mysqli_fetch_assoc($result);


if(isset($_POST['rps_submit']))
  {

        $data = array(
            'rps_min' => $_POST['rps_min'],
            'rps_max' => $_POST['rps_max'],
            'rps_chance' => $_POST['rps_chance'],
            'rps_win' => $_POST['rps_win'],
            'rps_status' => $_POST['rps_status'],
        );

    
    $settings_edit = Update('tbl_games', $data, "WHERE id = '1'");
    $_SESSION['msg'] = "39";
    header("Location: games.php");
    exit;
  
  }
  
if(isset($_POST['gn_submit']))
  {

        $data = array(
            'gn_min' => $_POST['gn_min'],
            'gn_max' => $_POST['gn_max'],
            'gn_chance' => $_POST['gn_chance'],
            'gn_win' => $_POST['gn_win'],
            'gn_status' => $_POST['gn_status'],
        );

    
    $settings_edit = Update('tbl_games', $data, "WHERE id = '1'");
    $_SESSION['msg'] = "39";
    header("Location: games.php");
    exit;
  
  }
  
if(isset($_POST['spin_submit']))
  {

        $data = array(
        'spin_min' => $_POST['spin_min'],
        'spin_max' => $_POST['spin_max'],
        'spin_win_min' => $_POST['spin_win_min'],
        'spin_win_max' => $_POST['spin_win_max'],
        'spin_status' => $_POST['spin_status'],
        );

    
    $settings_edit = Update('tbl_games', $data, "WHERE id = '1'");
    $_SESSION['msg'] = "39";
    header("Location: games.php");
    exit;
  
  }
  
if(isset($_POST['ct_submit']))
  {

        $data = array(
        'ct_min' => $_POST['ct_min'],
        'ct_max' => $_POST['ct_max'],
        'ct_chance' => $_POST['ct_chance'],
        'ct_win' => $_POST['ct_win'],
        'ct_status' => $_POST['ct_status'],
        );

    
    $settings_edit = Update('tbl_games', $data, "WHERE id = '1'");
    $_SESSION['msg'] = "39";
    header("Location: games.php");
    exit;
  
  }  
  
if(isset($_POST['cric_submit']))
  {

        $data = array(
        'cric_min' => $_POST['cric_min'],
        'cric_max' => $_POST['cric_max'],
        'cric_chance' => $_POST['cric_chance'],
        'cric_win' => $_POST['cric_win'],
        'cric_status' => $_POST['cric_status'],
        );

    
    $settings_edit = Update('tbl_games', $data, "WHERE id = '1'");
    $_SESSION['msg'] = "39";
    header("Location: games.php");
    exit;
  
  } 
  
if(isset($_POST['ouc_submit']))
  {

        $data = array(
        'ouc_min' => $_POST['ouc_min'],
        'ouc_max' => $_POST['ouc_max'],
        'ouc_amount' => $_POST['ouc_amount'],
        'ouc_bonus1' => $_POST['ouc_bonus1'],
        'ouc_bonus2' => $_POST['ouc_bonus2'],
        'ouc_bonus3' => $_POST['ouc_bonus3'],
        'ouc_win_min' => $_POST['ouc_win_min'],
        'ouc_win_max' => $_POST['ouc_win_max'],
        'ouc_status' => $_POST['ouc_status'],
        );

    
    $settings_edit = Update('tbl_games', $data, "WHERE id = '1'");
    $_SESSION['msg'] = "39";
    header("Location: games.php");
    exit;
  
  } 

?>
<head>
<title><?php echo $client_lang['game_settings']; ?></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js" async></script>
</head>
<style>
    .nav-tabs > li > a > i {
        font-size: 24px;
        vertical-align: middle;
    }
    .nav-tabs > li > a {
        display: flex;
        align-items: center;
    }
</style>
<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title"><?php echo $client_lang['game_settings']; ?></div>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mrg-top">
                <div class="col-md-12">
                    <div class="col-md-12 col-sm-12">
                        <?php if(isset($_SESSION['msg'])){?> 
                        <div class="alert alert-success alert-dismissible" role="alert"> 
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                            <?php echo $client_lang[$_SESSION['msg']] ; ?> 
                        </div>
                        <?php unset($_SESSION['msg']);}?> 
                    </div>
                </div>
            </div>
            <div class="card-body mrg_bottom">
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active">
                        <a href="#rps" aria-controls="rps" role="tab" data-toggle="tab">
                            <?php echo $client_lang['rps']; ?>&nbsp;<i class="fi fi-rr-hand-scissors"></i>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#gn" aria-controls="gn" role="tab" data-toggle="tab">
                            <?php echo $client_lang['gn']; ?>&nbsp;<i class="fi fi-rr-face-thinking"></i>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#spin" aria-controls="spin" role="tab" data-toggle="tab">
                            <?php echo $client_lang['spin']; ?>&nbsp;<i class="fi fi-rr-dharmachakra"></i>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#ct" aria-controls="ct" role="tab" data-toggle="tab">
                            <?php echo $client_lang['ct']; ?>&nbsp;<i class="fi fi-rr-coins"></i>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#cric" aria-controls="cric" role="tab" data-toggle="tab">
                            <?php echo $client_lang['cric']; ?>&nbsp;<i class="fi fi-rr-cricket"></i>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#ouc" aria-controls="ouc" role="tab" data-toggle="tab">
                            <?php echo $client_lang['ouc']; ?>&nbsp;<i class="fi fi-rr-clover-alt"></i>
                        </a>
                    </li>
                </ul>
                
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="rps">
                        <!-- Rock Paper Scissors form -->
                        <form action="" method="post" class="form form-horizontal" enctype="multipart/form-data">
                            <!-- RPS form fields here -->
                            <!-- Example field -->
                            <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['minimum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="rps_min" id="rps_min" placeholder="eg. 1" value="<?php echo $settings_row['rps_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['maximum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="rps_max" id="rps_max" placeholder="eg. 100" value="<?php echo $settings_row['rps_max'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_chance']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_chance_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="rps_chance" id="rps_chance" placeholder="eg. 50" value="<?php echo $settings_row['rps_chance'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_bonus']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_bonus_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="rps_win" id="rps_win" placeholder="eg. 10" value="<?php echo $settings_row['rps_win'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['game_visibility']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['game_visibility_help']; ?></p></label>
                      <div class="col-md-6">                       
                        <select name="rps_status" id="rps_status" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($settings_row['rps_status']=='1'){?>selected<?php }?>><?php echo $client_lang['visible']; ?></option>
                            <option value="0" <?php if($settings_row['rps_status']=='0'){?>selected<?php }?>><?php echo $client_lang['hidden']; ?></option>
                          
                        </select>
                      </div>
                  </div>
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="rps_submit" class="btn btn-primary"><?php echo $client_lang['save_game']; ?></button>
                    </div>
                  </div>
                        </form>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="gn">
                        <!-- Guess & Win form -->
                        <form action="" method="post" class="form form-horizontal" enctype="multipart/form-data">
                            <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['minimum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="gn_min" id="gn_min" placeholder="eg. 1" value="<?php echo $settings_row['gn_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['maximum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="gn_max" id="gn_max" placeholder="eg. 100" value="<?php echo $settings_row['gn_max'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_chance']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_chance_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="gn_chance" id="gn_chance" placeholder="eg. 50" value="<?php echo $settings_row['gn_chance'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_bonus']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_bonus_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="gn_win" id="gn_win" placeholder="eg. 10" value="<?php echo $settings_row['gn_win'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['game_visibility']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['game_visibility_help']; ?></p></label>
                      <div class="col-md-6">                       
                        <select name="gn_status" id="gn_status" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($settings_row['gn_status']=='1'){?>selected<?php }?>><?php echo $client_lang['visible']; ?></option>
                            <option value="0" <?php if($settings_row['gn_status']=='0'){?>selected<?php }?>><?php echo $client_lang['hidden']; ?></option>
                          
                        </select>
                      </div>
                  </div>
                  <div class="form-group">
                              <div class="col-md-9 col-md-offset-3">
                                <button type="submit" name="gn_submit" class="btn btn-primary"><?php echo $client_lang['save_game']; ?></button>
                              </div>
                            </div>
                        </form>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="spin">
                        <!-- Spin & Win form -->
                        <form action="" method="post" class="form form-horizontal" enctype="multipart/form-data">
                            <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['minimum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="spin_min" id="spin_min" placeholder="eg. 1" value="<?php echo $settings_row['spin_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['maximum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="spin_max" id="spin_max" placeholder="eg. 100" value="<?php echo $settings_row['spin_max'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['minimum_win']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_win_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="spin_win_min" id="spin_win_min" placeholder="eg. 10" value="<?php echo $settings_row['spin_win_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['maximum_win']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_win_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="spin_win_max" id="spin_win_max" placeholder="eg. 50" value="<?php echo $settings_row['spin_win_max'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                 <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['game_visibility']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['game_visibility_help']; ?></p></label>
                      <div class="col-md-6">                       
                        <select name="spin_status" id="spin_status" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($settings_row['spin_status']=='1'){?>selected<?php }?>><?php echo $client_lang['visible']; ?></option>
                            <option value="0" <?php if($settings_row['spin_status']=='0'){?>selected<?php }?>><?php echo $client_lang['hidden']; ?></option>
                          
                        </select>
                      </div>
                  </div>
                            <div class="form-group">
                              <div class="col-md-9 col-md-offset-3">
                                <button type="submit" name="spin_submit" class="btn btn-primary"><?php echo $client_lang['save_game']; ?></button>
                              </div>
                            </div>
                        </form>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="ct">
                        <!-- Coin Toss form -->
                        <form action="" method="post" class="form form-horizontal" enctype="multipart/form-data">
                            <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['minimum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ct_min" id="ct_min" placeholder="eg. 1" value="<?php echo $settings_row['ct_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['maximum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ct_max" id="ct_max" placeholder="eg. 100" value="<?php echo $settings_row['ct_max'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_chance']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_chance_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ct_chance" id="ct_chance" placeholder="eg. 50" value="<?php echo $settings_row['ct_chance'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_bonus']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_bonus_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ct_win" id="ct_win" placeholder="eg. 10" value="<?php echo $settings_row['ct_win'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['game_visibility']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['game_visibility_help']; ?></p></label>
                      <div class="col-md-6">                       
                        <select name="ct_status" id="ct_status" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($settings_row['ct_status']=='1'){?>selected<?php }?>><?php echo $client_lang['visible']; ?></option>
                            <option value="0" <?php if($settings_row['ct_status']=='0'){?>selected<?php }?>><?php echo $client_lang['hidden']; ?></option>
                          
                        </select>
                      </div>
                  </div>
                            <div class="form-group">
                              <div class="col-md-9 col-md-offset-3">
                                <button type="submit" name="ct_submit" class="btn btn-primary"><?php echo $client_lang['save_game']; ?></button>
                              </div>
                            </div>
                        </form>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="cric">
                        <!-- Cricket Mania form -->
                        <form action="" method="post" class="form form-horizontal" enctype="multipart/form-data">
                            <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['minimum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="cric_min" id="cric_min" placeholder="eg. 1" value="<?php echo $settings_row['cric_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['maximum_bet']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_bet_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="cric_max" id="cric_max" placeholder="eg. 100" value="<?php echo $settings_row['cric_max'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_chance']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_chance_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="cric_chance" id="cric_chance" placeholder="eg. 50" value="<?php echo $settings_row['cric_chance'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['winning_bonus']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['winning_bonus_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="cric_win" id="cric_win" placeholder="eg. 10" value="<?php echo $settings_row['cric_win'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['game_visibility']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['game_visibility_help']; ?></p></label>
                      <div class="col-md-6">                       
                        <select name="cric_status" id="cric_status" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($settings_row['cric_status']=='1'){?>selected<?php }?>><?php echo $client_lang['visible']; ?></option>
                            <option value="0" <?php if($settings_row['cric_status']=='0'){?>selected<?php }?>><?php echo $client_lang['hidden']; ?></option>
                          
                        </select>
                      </div>
                  </div>
                            <div class="form-group">
                              <div class="col-md-9 col-md-offset-3">
                                <button type="submit" name="cric_submit" class="btn btn-primary"><?php echo $client_lang['save_game']; ?></button>
                              </div>
                            </div>
                        </form>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="ouc">
                        <!-- Lucky Spinner form -->
                        <form action="" method="post" class="form form-horizontal" enctype="multipart/form-data">
                            <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['spin_amount']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['spin_amount_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_amount" id="ouc_amount" placeholder="eg. 10" value="<?php echo $settings_row['ouc_amount'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['spin_range_minimum']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['spin_range_minimum_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_min" id="ouc_min" placeholder="eg. 1" value="<?php echo $settings_row['ouc_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['spin_range_maximum']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['spin_range_maximum_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_max" id="ouc_max" placeholder="eg. 100" value="<?php echo $settings_row['ouc_max'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['minimum_win']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['minimum_win_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_win_min" id="ouc_win_min" placeholder="eg. 5" value="<?php echo $settings_row['ouc_win_min'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['maximum_win']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['maximum_win_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_win_max" id="ouc_win_max" placeholder="eg. 50" value="<?php echo $settings_row['ouc_win_max'];?>" class="form-control" required>
                      <span class="input-group-addon">%</span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['ouc_extra_10']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['ouc_extra_10_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_bonus1" id="ouc_bonus1" placeholder="eg. 5" value="<?php echo $settings_row['ouc_bonus1'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['ouc_extra_20']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['ouc_extra_20_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_bonus2" id="ouc_bonus2" placeholder="eg. 10" value="<?php echo $settings_row['ouc_bonus2'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['ouc_extra_30']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['ouc_extra_30_help']; ?></p></label>
                    <div class="col-md-6">
                        <div class="input-group">
                      <input type="text" name="ouc_bonus3" id="ouc_bonus3" placeholder="eg. 15" value="<?php echo $settings_row['ouc_bonus3'];?>" class="form-control" required>
                      <span class="input-group-addon"><?php echo $client_lang['coin']; ?></span>
                    </div>
                 </div>
                </div>
                
                <div class="form-group">
                    <label class="col-md-3 control-label"><?php echo $client_lang['game_visibility']; ?>:-<br><p class="control-label-help"><?php echo $client_lang['game_visibility_help']; ?></p></label>
                      <div class="col-md-6">                       
                        <select name="ouc_status" id="ouc_status" style="width:280px; height:25px;" class="select2" required>
                            <option value="1" <?php if($settings_row['ouc_status']=='1'){?>selected<?php }?>><?php echo $client_lang['visible']; ?></option>
                            <option value="0" <?php if($settings_row['ouc_status']=='0'){?>selected<?php }?>><?php echo $client_lang['hidden']; ?></option>
                          
                        </select>
                      </div>
                  </div>
                            <div class="form-group">
                              <div class="col-md-9 col-md-offset-3">
                                <button type="submit" name="ouc_submit" class="btn btn-primary"><?php echo $client_lang['save_game']; ?></button>
                              </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>                        
        </div>
    </div>
</div>

<?php include("includes/footer.php"); ?>
