var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");

var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

// Desplegar servicio HTTP para la simulación
var httpServer = http.createServer(
	function(request, response) {
      // Seleccionar cliente o sensores 
		var uri = url.parse(request.url).pathname;
      if (uri == "/servidor") 
         uri = "/servidor.html";
      else if (uri == "/cliente")
         uri = "/cliente.html";
      
      // Servir página, controlando errores
		var fname = path.join(process.cwd(), uri);
		fs.exists(fname, function(exists) {
			if (exists) {
				fs.readFile(fname, function(err, data){
					if (!err) {
						var extension = path.extname(fname).split(".")[1];
						var mimeType = mimeTypes[extension];
						response.writeHead(200, mimeType);
						response.write(data);
						response.end();
					}
					else {
						response.writeHead(200, {"Content-Type": "text/plain"});
						response.write('Error de lectura en el fichero: '+uri);
						response.end();
					}
				});
			}
			else{
				console.log("Peticion invalida: "+uri);
				response.writeHead(200, {"Content-Type": "text/plain"});
				response.write('404 Not Found\n');
				response.end();
			}
		});		
	}
);

var t = new Date();
var medidas = {temperatura: 0, luminosidad: 0, tiempo: t};
// MongoDB para almacenar un histórico del cambio en las medidas
MongoClient.connect("mongodb://localhost:27017/", { useUnifiedTopology: true },
function(err, db) {
   httpServer.listen(8080);
   var io = socketio(httpServer);
   console.log("Servicio iniciado");

	var dbo = db.db("P4");
	dbo.createCollection("Ejercicio", function(err, collection){
      // Envío y recepción de mensajes con el servicio
    	io.sockets.on('connection',
		function(client) {
         console.log('Nueva conexión del cliente ' + client.request.connection.remoteAddress + ':' + client.request.connection.remotePort);
         
         // Mostrar medidas e históricos a todos los clientes
         io.sockets.emit('medidas-casa', medidas);
         collection.find().toArray(function(err, results){
            client.emit('obtener-medidas', results);
         });
         
         // Los sensores envían datos, actualizar BD con histórico de medidas
         client.on('sensores', function (data) {
            medidas = data;
            collection.insertOne(data, {safe:true}, function(err, result) {});

            var mensaje = "Las medidas son correctas";

            // AGENTE: Detecta cuando se sobrepasa un cierto umbral
            if (data.temperatura > 50) {
               if (data.luminosidad > 50) {
                  mensaje = "La temperatura y la luminosidad son demasiado altas, por lo que se va a porceder a cerrar la persiana y a encender el aire acondicionado.";
                  data.emit('cerrar-persiana');
               } else if (data.luminosidad < 0) {
                  mensaje = "La temperatura es demasiado alta y la luminosidad demasiado baja, por lo que se va a porceder a abrir la persiana y a encender el aire acondicionado.";
                  data.emit('abrir-persiana');
               } else {
                  mensaje = "La temperatura es demasiado alta, por lo que se va a porceder a encender el aire acondicionado.";
               }

               data.emit('encender-aire');
            } else if (data.temperatura < -50) {
               if (data.luminosidad > 50) {
                  mensaje = "La temperatura demasiado baja y la luminosidad demasiado alta, por lo que se va a porceder a cerrar la persiana y a apagar el aire acondicionado.";
                  data.emit('cerrar-persiana');
               } else if (data.luminosidad < 0) {
                  mensaje = "La temperatura es demasiado y la luminosidad demasiado bajas, por lo que se va a porceder a abrir la persiana y a apagar el aire acondicionado.";
                  data.emit('abrir-persiana');
               } else {
                  mensaje = "La temperatura es demasiado baja, por lo que se va a proceder a apagar el aire.";
               }

               data.emit('apagar-aire');
            } else if (data.luminosidad > 50) {
               mensaje = "La luminosidad es demasiado alta, por lo que se va a proceder a cerrar la persiana.";
               data.emit('cerrar-persiana');
            } else if (data.luminosidad < 0) {
               mensaje = "La luminosidad es demasiado baja, por lo que se va a proceder a abrir la persiana.";
               data.emit('abrir-persiana');
            }

            // Se envía a los clientes el mensaje
            data.emit("mensaje", mensaje, data);

            // Se envía a los clientes las nuevas medidas que nos dan los sensores
            data.emit('medidas-casa', data);
         });

         // Apagar el aire = +5 grados
         client.on('apagar-aire-cliente', function(data) {
            data.emit('apagar-aire');
         });

         // Encender el aire = -5 grados
         client.on('encender-aire-cliente', function(data) {
            data.emit('encender-aire');
         });

         // Cerrar la persiana = -10 luminosidad
         client.on('cerrar-persiana-cliente', function(data) {
            data.emit('cerrar-persiana');
         });

         // Abrir la persiana = +10 luminosidad
         client.on('abrir-persiana-cliente', function(data) {
            data.emit('abrir-persiana');
         });
         
			client.on('obtener-medidas', function (data) {
				data.find().toArray(function(err, results){
					data.emit('obtener-medidas', results);
				});
         });

         client.on('borrar-historico', function () {
            collection.drop();
            client.emit('obtener-medidas', results);
         });
         
         client.on('disconnect', function() {
			   console.log('El cliente ' + client.request.connection.remoteAddress + ' se ha desconectado');
		   });
		});
   });
});

console.log("Servicio MongoDB iniciado");
