 <?php

 $app->group('/updateMsn', function () use ($app){

		$app->get('/:id', function ($id) use ($app) {

			date_default_timezone_set('America/Mexico_City');
        	$ts_creacion = date("Y-m-d H:i:s");

			$msn = ORM ::for_table('msn')				 
				->select('msn.*')
				->where('id', $id)
				->find_one();

			$msn->set('estatus',1);
			$msn->set('ts_envio',$ts_creacion);
			$msn->save();


			$response = array (
				'exito' => "SI"
			);

	 	
			/*Respuesta del servicio*/
			$app->response->setBody(json_encode($response));			
			$app->response->setStatus(200);
			$app->stop();

		});

		/*Respuesta del get id*/
		$app->options('/:id', function () use ($app){
		 	$app->response->setStatus(200);
		 	$app->response->setBody(json_encode(array('message' => 'ok')));
		});	

});
?>
