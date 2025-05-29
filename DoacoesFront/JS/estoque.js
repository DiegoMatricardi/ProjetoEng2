document.addEventListener('DOMContentLoaded', () => {
    fetch('http://localhost:8080/item')
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar itens');
            return response.json();
        })
        .then(itens => {
            const tabela = document.querySelector('#tabelaEstoque');
            if (!tabela) {
                console.error('Elemento #tabelaEstoque não encontrado!');
                return;
            }

            tabela.innerHTML = '';

            itens.forEach((item, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td class="border px-4 py-2">${index + 1}</td>
                    <td class="border px-4 py-2">${item.descricao}</td>
                    <td class="border px-4 py-2">${item.estoque}</td>
                `;
                tabela.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar itens:', error);
            alert('Erro ao carregar itens do estoque.');
        });
});
