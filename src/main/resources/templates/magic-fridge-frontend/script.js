document.addEventListener('DOMContentLoaded', () => {
    const foodList = document.getElementById('food-list');
    const addFoodForm = document.getElementById('add-food-form');
    const generateRecipeBtn = document.getElementById('generate-recipe-btn');
    const recipeOutput = document.getElementById('recipe-output');

    // URL base da sua API Spring Boot
    const API_URL = 'http://localhost:8080'; // Ajuste se necessário

    // --- Funções ---

    // Busca e exibe os alimentos
    async function fetchFoods() {
        try {
            const response = await fetch(`${API_URL}/food`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const foods = await response.json();
            renderFoodList(foods);
        } catch (error) {
            console.error('Erro ao buscar alimentos:', error);
            foodList.innerHTML = '<li>Erro ao carregar alimentos. Verifique o console e se o back-end está rodando.</li>';
        }
    }

    // Renderiza a lista de alimentos na tela
    function renderFoodList(foods) {
        foodList.innerHTML = ''; // Limpa a lista atual
        if (foods.length === 0) {
            foodList.innerHTML = '<li>Nenhum alimento na geladeira.</li>';
            return;
        }
        foods.forEach(food => {
            const li = document.createElement('li');
            li.dataset.id = food.id; // Armazena o ID no elemento
            li.innerHTML = `
                <span>
                    <strong>${food.name}</strong> (${food.category || 'Sem categoria'})
                    - Qtd: ${food.quantity || 1}
                    ${food.validity ? `- Val: ${formatDate(food.validity)}` : ''}
                </span>
                <button class="delete-btn" onclick="deleteFood(${food.id})">Remover</button>
            `;
            // Adicionar botão de editar seria aqui (mais complexo)
            foodList.appendChild(li);
        });
    }

    // Formata data (YYYY-MM-DD) para DD/MM/YYYY
    function formatDate(dateString) {
        if (!dateString) return '';
        const [year, month, day] = dateString.split('-');
        return `${day}/${month}/${year}`;
    }

    // Adiciona um novo alimento
    async function addFood(event) {
        event.preventDefault(); // Impede o recarregamento da página

        const formData = new FormData(addFoodForm);
        const foodData = {
            name: formData.get('name'),
            category: formData.get('category'),
            quantity: parseInt(formData.get('quantity'), 10), // Converte para número
            validity: formData.get('validity') || null // Envia null se vazio
        };

        // Validação simples
        if (!foodData.name) {
            alert('O nome do alimento é obrigatório!');
            return;
        }

        try {
            const response = await fetch(`${API_URL}/food`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(foodData),
            });

            if (!response.ok) {
                const errorData = await response.text(); // Tenta ler o erro do corpo
                throw new Error(`HTTP error! status: ${response.status} - ${errorData}`);
            }

            // const newFood = await response.json(); // Opcional: usar o retorno
            addFoodForm.reset(); // Limpa o formulário
            fetchFoods(); // Atualiza a lista

        } catch (error) {
            console.error('Erro ao adicionar alimento:', error);
            alert(`Não foi possível adicionar o alimento: ${error.message}`);
        }
    }

    // Deleta um alimento (função global para ser chamada pelo onclick)
    window.deleteFood = async function(id) {
        if (!confirm('Tem certeza que deseja remover este alimento?')) {
            return;
        }

        try {
            const response = await fetch(`${API_URL}/food/delete/${id}`, {
                method: 'DELETE',
            });

            if (!response.ok) {
                // Se o status for 404 (Not Found), o item já pode ter sido removido
                if (response.status === 404) {
                    console.warn(`Alimento com ID ${id} não encontrado para exclusão.`);
                } else {
                    const errorData = await response.text();
                    throw new Error(`HTTP error! status: ${response.status} - ${errorData}`);
                }
            }

            // Mesmo se 404, atualizamos a lista para refletir o estado atual
            fetchFoods();

        } catch (error) {
            console.error(`Erro ao remover alimento ${id}:`, error);
            alert(`Não foi possível remover o alimento: ${error.message}`);
        }
    }

    // Gera uma receita
    async function generateRecipe() {
        recipeOutput.innerHTML = '<p>Gerando receita...</p>';
        generateRecipeBtn.disabled = true; // Desabilita botão durante a requisição

        try {
            const response = await fetch(`${API_URL}/generate`);
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
            }
            const recipeText = await response.text(); // A API retorna texto puro
            recipeOutput.innerHTML = `<p>${recipeText.replace(/\n/g, '<br>')}</p>`; // Exibe com quebras de linha
        } catch (error) {
            console.error('Erro ao gerar receita:', error);
            recipeOutput.innerHTML = `<p>Erro ao gerar receita: ${error.message}</p>`;
        } finally {
            generateRecipeBtn.disabled = false; // Reabilita o botão
        }
    }

    // --- Event Listeners ---
    addFoodForm.addEventListener('submit', addFood);
    generateRecipeBtn.addEventListener('click', generateRecipe);

    // --- Inicialização ---
    fetchFoods(); // Carrega a lista de alimentos ao iniciar
});