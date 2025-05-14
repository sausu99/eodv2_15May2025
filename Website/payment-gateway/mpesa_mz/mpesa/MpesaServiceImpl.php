<?php
namespace App\payment\mpesa;
 
use abdulmueid\mpesa\Config;
use abdulmueid\mpesa\Transaction;

/** @author Nelson Flores | nelson.flores@live.com */
/**
 * 
 *  string $msisdn = $phone_number  
 * 
 */
abstract class MpesaServiceImpl
 { 
	protected $receiver_party_code;
	/**
	 * @var \abdulmueid\mpesa\Transaction
	 */
	protected $mpesa;
  
    protected $third_party_reference;
    protected $reference;
    protected $payment_status;
    protected $amount;
    protected $api_url; 
    protected $response;
    protected $currency = "MZN";
    protected $phone_number; // Número de telemóvel do cliente 
    protected $description; 
	protected $public_key;
	protected $api_key;

    /**
     * @var bool
     */
    protected $succeeded;
  


    public function __construct($third_party_reference = null,$amount = null)
    {
        $this->amount = $amount;
        $this->third_party_reference = $third_party_reference;

		$this->setApi_url('api.sandbox.vm.co.mz');
		$this->setReference('T12344C');
		$this->setPublic_key('MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmptSWqV7cGUUJJhUBxsMLonux24u+FoTlrb+4Kgc6092JIszmI1QUoMohaDDXSVueXx6IXwYGsjjWY32HGXj1iQhkALXfObJ4DqXn5h6E8y5/xQYNAyd5bpN5Z8r892B6toGzZQVB7qtebH4apDjmvTi5FGZVjVYxalyyQkj4uQbbRQjgCkubSi45Xl4CGtLqZztsKssWz3mcKncgTnq3DHGYYEYiKq0xIj100LGbnvNz20Sgqmw/cH+Bua4GJsWYLEqf/h/yiMgiBbxFxsnwZl0im5vXDlwKPw+QnO2fscDhxZFAwV06bgG0oEoWm9FnjMsfvwm0rUNYFlZ+TOtCEhmhtFp+Tsx9jPCuOd5h2emGdSKD8A6jtwhNa7oQ8RtLEEqwAn44orENa1ibOkxMiiiFpmmJkwgZPOG/zMCjXIrrhDWTDUOZaPx/lEQoInJoE2i43VN/HTGCCw8dKQAwg0jsEXau5ixD0GUothqvuX3B9taoeoFAIvUPEq35YulprMM7ThdKodSHvhnwKG82dCsodRwY428kg2xM/UjiTENog4B6zzZfPhMxFlOSFX4MnrqkAS+8Jamhy1GgoHkEMrsT5+/ofjCx0HjKbT5NuA2V/lmzgJLl3jIERadLzuTYnKGWxVJcGLkWXlEPYLbiaKzbJb2sYxt+Kt5OxQqC1MCAwEAAQ==');
		$this->setApi_key('0vk1cnfpvfb4c2lug5jhhez4jqhkcm82');
		
    }





	abstract function init();
	public function getStatus()
	{
		return $this->getPaymentInfo();
	}

	public function processPayment() {

        $config = [
            'public_key' => $this->public_key,
            'api_key' => $this->api_key,
            'origin' => '*',
            'service_provider_code' => '171717',
            'initiator_identifier' => '',
            'security_credential' => '',
        ];

    
        $mpesa_config = new \abdulmueid\mpesa\Config(
            $config['public_key'],
            $this->api_url,
            $config['api_key'],
            $config['origin'],
            $config['service_provider_code'],
            $config['initiator_identifier'],
            $config['security_credential']
        );

        $this->mpesa = new Transaction($mpesa_config);
		$this->init();


		return $this;
	}

  

    public function setPaymentId($payment_id)
    {
        $this->payment_id = $payment_id;
        return $this;
    } 
    public function setPayment_status($payment_status)
    {
        $this->payment_status = $payment_status;
        return $this;
    } 

    public function getPaymentInfo()
    {
        $arr = [
            'reference' => $this->reference,
            'payment_status' => $this->payment_status,
            'amount' => $this->amount,
            'phone_number' => $this->phone_number,
            'third_party_reference' => $this->third_party_reference,
            'succeeded'=>$this->succeeded, 
            'response' => $this->getResponse()
        ];

        return json_decode(json_encode($arr));
    }


    /**
     * @return mixed
     */
    public function getAmount()
    {
        return $this->amount;
    }

    /**
     * @param mixed $amount 
     * @return self
     */
    public function setAmount($amount): self
    {
        $this->amount = $amount;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getCurrency()
    {
        return strtoupper($this->currency);
    } 

    /**
     * @param mixed $currency 
     * @return self
     */
    public function setCurrency($currency): self
    {
        $this->currency = strtoupper($currency);
        return $this;
    }

	/**
	 * 
	 * @return bool
	 */
	public function getSucceeded() {
		return $this->succeeded;
	} 
	
	/**
	 * 
	 * @param bool $succeeded 
	 * @return self
	 */
	public function setSucceeded($succeeded = false): self {
		$this->succeeded = $succeeded;
		return $this;
	} 
 
	/**
	 * @return mixed
	 */
	public function getApi_url() {
		return $this->api_url;
	}
	
	/**
	 * @param mixed $api_url 
	 * @return self
	 */
	public function setApi_url(string $api_url): self {
		$this->api_url = $api_url;
		return $this;
	}

	/**
	 * @return mixed
	 */
	public function getReference() {
		return $this->reference;
	}
	
	/**
	 * @param mixed $reference 
	 * @return self
	 */
	public function setReference($reference): self {
		$this->reference = $reference;
		return $this;
	}


    public function setResponse($response, $status = -1)
    {
        $this->response = $response;
        $this->payment_status = $status;
        return $this;
    } 

    
    public function getResponse()
    {
        return $this->response;
    }








	/**
	 * @return mixed
	 */
	public function getPublic_key() {
		return $this->public_key;
	}
	
	/**
	 * @param mixed $public_key 
	 * @return self
	 */
	public function setPublic_key($public_key): self {
		$this->public_key = $public_key;
		return $this;
	}

	/**
	 * @return mixed
	 */
	public function getApi_key() {
		return $this->api_key;
	}
	
	/**
	 * @param mixed $api_key 
	 * @return self
	 */
	public function setApi_key($api_key): self {
		$this->api_key = $api_key;
		return $this;
	}

	/**
	 * @return mixed
	 */
	public function getPhone_number() {
		return $this->phone_number;
	}
	
	/**
	 * @param mixed $phone_number 
	 * @return self
	 */
	public function setPhone_number($phone_number): self {
		$this->phone_number = $phone_number;
		return $this;
	}
}
