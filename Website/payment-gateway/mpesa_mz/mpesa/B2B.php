<?php

namespace App\payment\mpesa;
 

/** @author Nelson Flores | nelson.flores@live.com */

class B2B extends MpesaServiceImpl
{
      
	public function init() {

        $b2b = $this->mpesa->b2b(
            $this->amount,
            $this->receiver_party_code,
            $this->reference,
            $this->third_party_reference
        );

        $this->response = json_decode($b2b->getResponse());
        
        return $this;

	}
}