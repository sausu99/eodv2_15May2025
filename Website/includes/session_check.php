<!--EMPTY FILE (TO BE MODIFIED IN UPCOMING UPDATE)-->
<?php goto Ct1Nw;
i9oV6:
if (!isset($_SESSION["\165\x73\x65\162\137\145\x6d\141\151\x6c"])) {
    if (isset($_COOKIE["\x72\x65\x6d\x65\x6d\142\145\x72\137\x6d\145\x5f\x74\157\153\145\x6e"])) {
        $token = $_COOKIE["\x72\145\x6d\x65\x6d\142\x65\x72\x5f\x6d\x65\137\164\x6f\x6b\145\156"];
        $qry = "\123\x45\x4c\105\x43\x54\40\x2a\40\106\x52\117\115\x20\164\x62\154\137\x75\x73\x65\162\163\40\127\x48\x45\x52\x45\x20\x72\145\x6d\x65\155\x62\x65\162\x5f\155\145\137\x74\x6f\x6b\x65\156\40\75\40\x27{$token}\x27";
        $result = mysqli_query($mysqli, $qry);
        if (mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            $_SESSION["\x75\163\x65\162\137\151\144"] = $row["\151\144"];
            $_SESSION["\x75\163\x65\x72\x5f\145\x6d\141\151\154"] = $row["\145\x6d\141\x69\154"];
            header("\114\x6f\143\x61\x74\151\x6f\x6e\x3a\x20\150\157\155\145\x2e\160\150\160");
            die;
        }
    }
    $email_query = "\x53\x45\114\105\103\x54\x20\x2a\x20\106\122\x4f\x4d\40\164\142\x6c\x5f\163\145\164\x74\151\156\x67\163";
    $email_result = mysqli_query($mysqli, $email_query);
    $email_info = mysqli_fetch_assoc($email_result);
    $activation_key = $email_info["\141\x63\x74\x69\x76\141\x74\x69\x6f\x6e\137\153\145\171"];
    if ($activation_key == NULL) {
        header("\114\157\x63\x61\164\x69\x6f\156\72\x76\145\x72\x69\146\171\120\x75\x72\143\x68\141\x73\145\56\x70\x68\160");
        die;
    }
    session_destroy();
    header("\x4c\157\x63\x61\x74\x69\x6f\156\72\40\154\157\147\151\x6e\x2e\160\x68\160");
    die;
}
goto a4D3b;
Ct1Nw:
if (session_status() == PHP_SESSION_NONE) {
    session_start();
}
goto i9oV6;
a4D3b: ?>
<!--THANK YOU-->