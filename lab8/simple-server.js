var http = require('http');
http.createServer(function(req, res) {
	res.writeHead(200, {'Content-Type': 'text/html'});
	res.end('<html><body><h1>Hello, World!</h1></body></html>');
}).listen(1444);
console.log('Simple Server running on port 1444...');
