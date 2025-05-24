document.addEventListener('DOMContentLoaded', async function () {

    document.getElementById("logoutButton").addEventListener("click", function () {
        localStorage.removeItem("idusuario");
        localStorage.removeItem("idusuario_tipo");
        window.location.href = "login.html";
    });

    await carregarBeneficiarios();

    document.getElementById("inserirButton").addEventListener("click", async function () {
        const beneficiario = {
            horarioDiaVisita: document.getElementById("horarioDiaVisita").value 
                ? new Date(document.getElementById("horarioDiaVisita").value).toISOString() 
                : null,
            bairro: document.getElementById("bairro").value || null,
            cep: document.getElementById("cep").value || null,
            cidade: document.getElementById("cidade").value || null,
            complemento: document.getElementById("complemento").value || null,
            email: document.getElementById("email").value || null,
            endereco: document.getElementById("endereco").value || null,
            nome: document.getElementById("nome").value || null,
            telefone: document.getElementById("telefone").value || null
        };

        const mensagem = document.getElementById("mensagem");
        try {
            const response = await fetch('http://localhost:8080/beneficiarios/inserir', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(beneficiario)
            });

            let data = {};
            const responseText = await response.text(); 
            try {
                data = JSON.parse(responseText); 
            } catch (e) {
                data.message = responseText; 
            }

            mensagem.classList.remove('hidden', 'bg-red-100', 'text-red-700', 'bg-green-100', 'text-green-700');

            if (response.ok) {
                mensagem.classList.add('bg-green-100', 'text-green-700');
                mensagem.textContent = `${data.message || 'Beneficiário inserido com sucesso.'}${data.idbeneficiario ? ` (ID: ${data.idbeneficiario})` : ''}`;
                await carregarBeneficiarios();
                document.querySelectorAll('input').forEach(input => input.value = '');
            } else {
                mensagem.classList.add('bg-red-100', 'text-red-700');
                mensagem.textContent = data.message || 'Erro ao inserir beneficiário.';
            }

        } catch (error) {
            mensagem.classList.remove('hidden', 'bg-green-100', 'text-green-700');
            mensagem.classList.add('bg-red-100', 'text-red-700');
            mensagem.textContent = 'Erro ao inserir beneficiário: ' + error.message;
        }
    });
});

async function carregarBeneficiarios() {
    try {
        const response = await fetch('http://localhost:8080/beneficiarios/listar');
        const tbody = document.getElementById('beneficiariosTabela');
        tbody.innerHTML = '';

        if (response.status === 204) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center p-4 text-gray-500">Nenhum beneficiário encontrado.</td></tr>';
            return;
        }

        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${await response.text()}`);
        }

        const beneficiarios = await response.json();
        beneficiarios.forEach(b => {
            const row = `
                <tr class="border-t">
                    <td class="p-2">${b.idbeneficiario}</td>
                    <td class="p-2">${b.nome || '-'}</td>
                    <td class="p-2">${b.email || '-'}</td>
                    <td class="p-2">${b.telefone || '-'}</td>
                    <td class="p-2">${b.endereco || '-'}, ${b.bairro || '-'}, ${b.cidade || '-'}</td>
                    <td class="p-2">${b.horarioDiaVisita ? new Date(b.horarioDiaVisita).toLocaleString('pt-BR') : '-'}</td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        document.getElementById('beneficiariosTabela').innerHTML = `
            <tr><td colspan="6" class="text-center p-4 text-red-500">Erro ao carregar beneficiários: ${error.message}</td></tr>
        `;
    }
}
