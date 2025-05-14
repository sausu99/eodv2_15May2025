<?php

namespace App\payment\mpesa;
 

/** @author Nelson Flores | nelson.flores@live.com */

class Reversal extends MpesaServiceImpl
{
     

	
	public function init() {

        $reversal = $this->mpesa->reversal(
            $this->amount,
            $this->payment_id,
            $this->reference
        );

        $this->response = $reversal->getResponse();

        return $this;

	}
}

