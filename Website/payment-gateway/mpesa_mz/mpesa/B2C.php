<?php

namespace App\payment\mpesa;
 

/** @author Nelson Flores | nelson.flores@live.com */

class B2C extends MpesaServiceImpl
{
     

	
	public function init() {

        $b2c = $this->mpesa->b2c(
            $this->amount,
            $this->phone_number,
            $this->reference,
            $this->third_party_reference
        );

        $this->response = json_decode($b2c->getResponse());
        
        return $this;

	}
}