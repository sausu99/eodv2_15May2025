<?php
	
	if(!isset($_SESSION['seller_username']))
	{
		session_destroy();
		
		header( "Location:index.php");
		exit;
		 
	}
	 
	
?>