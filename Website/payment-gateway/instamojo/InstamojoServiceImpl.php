<?php


namespace App\payment\instamojo;



#ini_set('display_errors', 1);#
#ini_set('display_startup_errors', 1);
#error_reporting(E_ALL); 

/** @author Nelson Flores | nelson.flores@live.com */


class InstamojoServiceImpl
{

    private $payment_id;
    private $api_key;
    private $auth_token;
    private $api_endpoint;
    private $purpose = '';
    private $amount;
    private $buyer_name = '';
    private $email = '';
    private $phone_number = '';
    private $redirect_url = '';
    private $send_email = true;
    private $webhook = '';
    private $response = '';

    /**
     * @var bool
     */
    protected $succeeded = false;
    protected $longurl;


    public function __construct($amount = null, $email = null)
    {
        $this->amount = $amount;
        $this->email = $email;

        $this
            ->setApi_endpoint('https://www.instamojo.com/api/1.1/')
            ->setApi_key('37120463c7fa4a51d357c46ba8df4484')
            ->setAuth_token('637094c2b96aab4150d15c5ec47fd99c')

        ;
    }

    public function createPayment()
    {
        $url = $this->api_endpoint . 'payment-requests/';

        $data = [
            'purpose' => $this->getPurpose(),
            'amount' => $this->getAmount(),
            'buyer_name' => $this->getBuyer_name(),
            'email' => $this->getEmail(),
            'phone' => $this->getPhone_number(),
            'redirect_url' => $this->getRedirect_url(),
            'send_email' => $this->send_email,
            'webhook' => $this->getWebhook(),
            'allow_repeated_payments' => 'False',

        ];


        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_HEADER, FALSE);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, TRUE);
        curl_setopt(
            $ch,
            CURLOPT_HTTPHEADER,
            array(
                "X-Api-Key:" . $this->getApi_key(),
                "X-Auth-Token:" . $this->getAuth_token()
            )
        );

        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
        $response = curl_exec($ch);
        curl_close($ch);

        $r = json_decode($response);

        if (!empty($r->payment_request)) {
            $this->setSucceeded($r->success);
            $this->setLongurl($r->payment_request->longurl);
            $this->setPaymentId($r->payment_request->id);
        }

        $this->response = $r;
        return $this;
    }


    public function getPaymentStatus($payment_id)
    {
        $url = $this->api_endpoint . 'payment-requests/' . $payment_id;

        $ch = curl_init($url);

        curl_setopt($ch, CURLOPT_HEADER, FALSE);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, TRUE);


        curl_setopt(
            $ch,
            CURLOPT_HTTPHEADER,
            array(
                'Content-Type: application/json',
                'X-Api-Key: ' . $this->api_key,
                'X-Auth-Token: ' . $this->auth_token
            )
        );

        $response = curl_exec($ch);

        $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        curl_close($ch);

        $r = json_decode($response);

        if (!empty($r->payment_request)) {
            
            $this
                ->setPaymentId($payment_id)
                ->setPhone_number($r->payment_request->phone)
                ->setAmount($r->payment_request->amount)
                ->setWebhook($r->payment_request->webhook)
                ->setRedirect_url($r->payment_request->redirect_url)
                ->setEmail($r->payment_request->email)
                ->setBuyer_name($r->payment_request->buyer_name)
                ->setPurpose($r->payment_request->purpose)
                ->setLongurl($r->payment_request->longurl)
            ;

            if (!empty($r->payment_request->payments)) {
                $this->setSucceeded($r->success);
            }
        }

        $this->response = $r;
        return $this->getPaymentInfo();
    }



    public function setPaymentId($payment_id)
    {
        $this->payment_id = $payment_id;
        return $this;
    }


    public function getPaymentId()
    {
        return $this->payment_id;
    }
    public function setPayment_status($payment_status)
    {
        $this->payment_status = $payment_status;
        return $this;
    }

    public function getPaymentInfo()
    {
        $arr = [
            'success' => $this->isSucceeded(),
            'longurl' => $this->getLongurl(),
            'purpose' => $this->getPurpose(),
            'amount' => $this->getAmount(),
            'buyer_name' => $this->getBuyer_name(),
            'email' => $this->getEmail(),
            'phone' => $this->getPhone_number(),
            'redirect_url' => $this->getRedirect_url(),
            'send_email' => $this->send_email,
            'webhook' => $this->getWebhook(),
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
    public function getApi_endpoint()
    {
        return $this->api_endpoint;
    }

    /**
     * @param mixed $api_endpoint 
     * @return self
     */
    public function setApi_endpoint(string $api_endpoint): self
    {
        $this->api_endpoint = $api_endpoint;
        return $this;
    }



    public function setResponse($response, $status = -1)
    {
        $this->response = $response;
        return $this;
    }


    public function getResponse()
    {
        return $this->response;
    }




    public function setPurpose($purpose, $status = -1)
    {
        $this->purpose = $purpose;
        return $this;
    }


    public function getPurpose()
    {
        return $this->purpose;
    }





    public function setBuyer_name($buyer_name, $status = -1)
    {
        $this->buyer_name = $buyer_name;
        $this->payment_status = $status;
        return $this;
    }


    public function getBuyer_name()
    {
        return $this->buyer_name;
    }








    /**
     * @return mixed
     */
    public function getAuth_token()
    {
        return $this->auth_token;
    }

    /**
     * @param mixed $auth_token 
     * @return self
     */
    public function setAuth_token($auth_token): self
    {
        $this->auth_token = $auth_token;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getApi_key()
    {
        return $this->api_key;
    }

    /**
     * @param mixed $api_key 
     * @return self
     */
    public function setApi_key($api_key): self
    {
        $this->api_key = $api_key;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getPhone_number()
    {
        return $this->phone_number;
    }

    /**
     * @param mixed $phone_number 
     * @return self
     */
    public function setPhone_number($phone_number): self
    {
        $this->phone_number = $phone_number;
        return $this;
    }



    /**
     * @return mixed
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * @param mixed $email 
     * @return self
     */
    public function setEmail($email): self
    {
        $this->email = $email;
        return $this;
    }



    /**
     * @return mixed
     */
    public function getRedirect_url()
    {
        return $this->redirect_url;
    }

    /**
     * @param mixed $redirect_url 
     * @return self
     */
    public function setRedirect_url($redirect_url): self
    {
        $this->redirect_url = $redirect_url;
        return $this;
    }



    /**
     * @return mixed
     */
    public function getWebhook()
    {
        return $this->webhook;
    }

    /**
     * @param mixed $webhook 
     * @return self
     */
    public function setWebhook($webhook): self
    {
        $this->webhook = $webhook;
        return $this;
    }





    /**
     * @return mixed
     */
    public function getLongurl()
    {
        return $this->longurl;
    }

    /**
     * @param mixed $longurl 
     * @return self
     */
    public function setLongurl($longurl): self
    {
        $this->longurl = $longurl;
        return $this;
    }







    /**
     * 
     * @return bool
     */
    public function isSucceeded()
    {
        return ($this->succeeded === true);
    }

    /**
     * 
     * @param bool $succeeded 
     * @return self
     */
    public function setSucceeded($succeeded): self
    {
        $this->succeeded = $succeeded;
        return $this;
    }
}
