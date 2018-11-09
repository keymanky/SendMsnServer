 <?php

 $app->group('/getMsn', function () use ($app)	{

		$app->get('/', function () use ($app) {

		$msn = ORM ::for_table('msn')				 
			->select('msn.*')
			->where('estatus', '0')
			->limit(1)
			->find_many();


			$response = array();

			if($msn){

		 		foreach ($msn as $key => $value) {

					$tmp = array(
						'numero'     => $value->numero,
						'msn'     => $value->msn,
						'id' => $value->id,
						'exito' => "SI",
					);
					$response = $tmp; 
					//$response[] = $tmp; 
		 		}
			}	

			if(empty($response)){
				$response = array (
					'exito' => "NO"
				);
			}

	 	
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
