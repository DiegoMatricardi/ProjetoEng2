// server.mjs
import { createServer } from 'node:http';
import fs from 'node:fs';
import path from 'node:path';

const server = createServer((req, res) => {
  let filePath = '.' + req.url;

  if (filePath == './') {
    filePath = './login.html';
  }

  const extname = path.extname(filePath);
  let contentType = 'text/html';

  if (extname === '.css') {
    contentType = 'text/css';
  } else if (extname === '.js') {
    contentType = 'application/javascript';
  }

  // Lê o arquivo e o envia ao navegador
  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('Arquivo não encontrado!');
    } else {
      res.writeHead(200, { 'Content-Type': contentType });
      res.end(data);
    }
  });
});


server.listen(3000, '127.0.0.1', () => {
  console.log('Servidor rodando em http://127.0.0.1:3000');
});
