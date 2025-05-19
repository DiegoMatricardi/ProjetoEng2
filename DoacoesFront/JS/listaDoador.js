document.addEventListener('DOMContentLoaded', () => {
  const container = document.getElementById('doadoresContainer');

  fetch('http://localhost:8080/doador/listar')
    .then(response => {
      if (!response.ok) throw new Error('Erro ao buscar doadores');
      return response.json();
    })
    .then(doadores => {
      container.innerHTML = ''; 
      
      doadores.forEach(doador => {
        const card = document.createElement('div');
        card.className = 'bg-white rounded-lg shadow-md p-6 flex flex-col justify-between hover:shadow-xl transition-shadow duration-300';
        console.log(doador)
        const inicial = doador.nome ? doador.nome.charAt(0).toUpperCase() : '?';
        card.innerHTML = `
          <div class="flex items-center space-x-4 mb-4">
            <div class="flex-shrink-0 bg-blue-500 text-white rounded-full h-12 w-12 flex items-center justify-center text-xl font-bold">
              ${inicial}
            </div>
            <h2 class="text-xl font-semibold text-gray-800">${doador.nome}</h2>
          </div>
          <div class="text-gray-600 space-y-1">
            <p><strong>Email:</strong> ${doador.email || '-'}</p>
            <p><strong>Telefone:</strong> ${doador.telefone || '-'}</p>
          </div>
          <div class="mt-6 flex space-x-3">
            <button 
              onclick="excluirDoador(${doador.idDoador})" 
              class="flex-1 bg-red-600 hover:bg-red-700 text-white font-semibold px-4 py-2 rounded-lg shadow-sm transition-colors duration-200"
            >
              Excluir
            </button>
          </div>
        `;
        container.appendChild(card);
      });
    })
    .catch(error => {
      console.error('Erro ao carregar doadores:', error);
      container.innerHTML = `<p class="text-red-600 text-center font-semibold">Erro ao carregar os doadores.</p>`;
    });
});

function editarDoador(id) {
  window.location.href = `editarDoador.html?id=${id}`;
}

function excluirDoador(id) {
  if (confirm('Tem certeza que deseja excluir este doador?')) {
    fetch(`http://localhost:8080/doador/${id}`, {
      method: 'DELETE'
    })
    .then(response => {
      if (response.ok) {
        alert('Doador excluído com sucesso!');
        location.reload();
      } else {
        alert('Erro ao excluir o doador.');
      }
    })
    .catch(error => {
      console.error('Erro na exclusão:', error);
      alert('Erro na exclusão. Veja o console para detalhes.');
    });
  }
}

