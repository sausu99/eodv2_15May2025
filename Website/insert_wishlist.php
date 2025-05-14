<?php
include ("includes/connection.php");
include ("includes/function.php");
include ("include/session_check.php");

$item_id = sanitize($_POST["id"]);
$user_id = sanitize($_SESSION["user_id"]);
$created_at = date('Y-m-d H:i:s');

$sSql = "SELECT * FROM tbl_wishlist WHERE item_id = ? AND user_id = ?";
$stmt = $mysqli->prepare($sSql);
$stmt->bind_param("ii", $item_id, $user_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $rSql = "DELETE FROM tbl_wishlist WHERE user_id = ? AND item_id = ?";

    $stmt = $mysqli->prepare($rSql);
    $stmt->bind_param("ii", $user_id, $item_id);

    if ($stmt->execute()) {
        echo "Removed";
    }
} else {
    $iSql = "INSERT INTO tbl_wishlist (user_id, item_id, created_at) VALUES (?, ?, ?)";
    $stmt = $mysqli->prepare($iSql);
    $stmt->bind_param("iis", $user_id, $item_id, $created_at);

    if ($stmt->execute()) {
        echo "Inserted";
    } else {
        echo "Error: " . $sql . "<br>" . $mysqli->error;
    }
}
