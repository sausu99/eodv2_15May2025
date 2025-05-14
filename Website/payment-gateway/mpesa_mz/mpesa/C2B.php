<?php

namespace App\payment\mpesa;
 

/** @author Nelson Flores | nelson.flores@live.com */

class C2B extends MpesaServiceImpl
{
     

	
	public function init() {
        
        $c2b = $this->mpesa->c2b(
            $this->amount,
            $this->phone_number,
            $this->reference,
            $this->third_party_reference
        );

        

 
        $this->response = json_decode($c2b->getResponse());

        return $this;

	}
}