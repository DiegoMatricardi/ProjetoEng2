document.addEventListener('DOMContentLoaded', async function () {
    const idUsuario = localStorage.getItem("idusuario");
    const usuarioTipo = localStorage.getItem("idusuario_tipo");

    if (!idUsuario) {
        alert("Usuário não está logado. Por favor, faça login.");
        window.location.href = "login.html";
        return;
    }

    if (usuarioTipo !== '2') {
        alert("Acesso restrito a colaboradores.");
        window.location.href = "dashboard.html";
        return;
    }

    if (usuarioTipo === '2') {
        const todosDoadoresLink = document.getElementById('todosDoadoresLink');
        const colaboradoresLink = document.getElementById('colaboradoresLink');
        const familiasLink = document.getElementById('familiasLink');
        const buscarLink = document.getElementById('buscarLink');
    if (todosDoadoresLink) {
      todosDoadoresLink.classList.remove('hidden');
      colaboradoresLink.classList.remove('hidden');
      familiasLink.classList.remove('hidden');
      buscarLink.classList.remove('hidden');
    }

    
    document.getElementById("logoutButton").addEventListener("click", function () {
        localStorage.removeItem("idusuario");
        localStorage.removeItem("idusuario_tipo");
        window.location.href = "login.html";
    });

    
    await carregarColaboradores();
}});

async function carregarColaboradores() {
    try {
        const response = await fetch('http://localhost:8080/colaborador/listar');
        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${await response.text()}`);
        }
        const colaboradores = await response.json();
        renderColaboradores(colaboradores);
    } catch (error) {
        console.error('Erro ao carregar colaboradores:', error);
        document.getElementById('colaboradoresContainer').innerHTML = `
            <p class="text-red-500 text-center">Erro ao carregar colaboradores. Tente novamente.</p>
        `;
    }
}

function formatarCPF(cpf) {
    if (!cpf) return 'Não informado';
    cpf = cpf.replace(/\D/g, '');
    // Aplica máscara: 000.000.000-00
    if (cpf.length === 11) {
        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    }
    return cpf;
}

function renderColaboradores(colaboradores) {
    const tbody = document.getElementById('colaboradoresTabela');
    tbody.innerHTML = '';
    console.log(colaboradores.json)
    if (colaboradores.length === 0) {
        tbody.innerHTML = `
            <tr><td colspan="7" class="text-center p-4 text-gray-500">Nenhum colaborador encontrado.</td></tr>
        `;
        return;
    }

    colaboradores.forEach(colaborador => {
        const row = `
            <tr class="border-t">
                <td class="p-2">${colaborador.nome || 'Não informado'}</td>
                <td class="p-2">${formatarCPF(colaborador.cpf)}</td>
                <td class="p-2">${colaborador.emailSecundario || 'Não informado'}</td>
                <td class="p-2">${colaborador.telefone || 'Não informado'}</td>
                <td class="p-2">${colaborador.endereco || 'Não informado'}</td>
                <td class="p-2">${colaborador.bairro || 'Não informado'}</td>
                <td class="p-2">${colaborador.cidade || 'Não informado'}</td>
            </tr>
        `;
        tbody.insertAdjacentHTML('beforeend', row);
    });
}