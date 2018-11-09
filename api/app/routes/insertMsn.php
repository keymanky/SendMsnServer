 <?php

 $app->group('/newMsn', function () use ($app)	{

	$app->post('/', function () use ($app) {

		$rules=array(
			'numero' =>array(false, "string", 1, 10),
			'msn' =>array(false, "string", 1, 120),	 			 		
		);


		 $v = new Validator($app->request->getBody(), $rules);
		 $params = $v->validate();

		 if(count($v->getErrors()) > 0){
		 	foreach ($v->getErrors() as $key => $value) {
		 		$response = array("error" => array($key => "campo incorrecto"));
		 		$app->response->setStatus($v->getCode());
		 		$app->response->setBody(json_encode($response));
		 		$app->stop();
		 	}
		 }

		date_default_timezone_set('America/Mexico_City');
        $ts_creacion = date("Y-m-d H:i:s");
				 

		//Insertamos un nuevo registrO			
	 	$msn = ORM::for_table('msn')->create();
	 	$msn->numero = $params['numero'];		
	 	$msn->msn = $params['msn'];							
	 	$msn->estatus = 0;							
	 	$msn->ts_creacion = $ts_creacion;					
	 	$msn->save();

	 	$response = array(
	 		'id' => $msn->id(),
		    'exito' => "SI"
	 	);

		 $app->response->setStatus(200);
		 $app->response->setBody(json_encode($response));	
	});

	 /*Respuesta del post*/
	$app->options('/', function () use ($app){
	 	$app->response->setStatus(200);
	 	$app->response->setBody(json_encode(array('message' => 'ok')));
	});



});

?>
