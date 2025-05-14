<?php
include("includes/connection.php");


// Check if the request contains the necessary data
if (isset($_POST['s_id'])) {
    $s_id = intval($_POST['s_id']);

    // Prepare an update statement
    $sql = "UPDATE tbl_scratch SET s_status = 0 WHERE s_id = ?";
    $stmt = $mysqli->prepare($sql);
    $stmt->bind_param("i", $s_id);

    if ($stmt->execute()) {
        echo "Status updated successfully";
    } else {
        echo "Error updating status: " . $mysqli->error;
    }

    $stmt->close();
} else {
    echo "No scratch card ID provided.";
}
?>
