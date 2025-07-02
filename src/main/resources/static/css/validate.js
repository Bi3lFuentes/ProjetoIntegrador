(function () {
    'use strict';

    // Função para aplicar a máscara de telefone (simples)
    function applyPhoneMask(event) {
        // ... (esta função permanece inalterada)
        const input = event.target;
        let value = input.value.replace(/\D/g, ''); // Remove tudo que não é dígito
        const dddMaxLength = 2;
        const firstPartMaxLength = 5; // Para (XX) XXXXX-...
        const secondPartMaxLength = 4; // Para ...-XXXX

        value = value.substring(0, dddMaxLength + firstPartMaxLength + secondPartMaxLength);
        let formattedValue = '';

        if (value.length > 0) {
            formattedValue = `(${value.substring(0, dddMaxLength)}`;
        }
        if (value.length > dddMaxLength) {
            const numberPart = value.substring(dddMaxLength);
            const isLikelyMobile9Digit = numberPart.length > 8;
            const currentFirstPartMaxLength = isLikelyMobile9Digit ? firstPartMaxLength : 4;

            formattedValue += `) ${numberPart.substring(0, currentFirstPartMaxLength)}`;
            if (numberPart.length > currentFirstPartMaxLength) {
                formattedValue += `-${numberPart.substring(currentFirstPartMaxLength, currentFirstPartMaxLength + secondPartMaxLength)}`;
            }
        }
        input.value = formattedValue;
    }

    // Função para validar o padrão do telefone
    function validatePhoneNumberPattern(inputElement) {
        // ... (esta função permanece inalterada)
        const feedbackElement = inputElement.nextElementSibling;
        const originalFeedbackMessage = "Formato de telefone inválido. Use (XX) XXXXX-XXXX ou (XX) XXXX-XXXX.";
        const requiredFeedbackMessage = "O telefone é obrigatório.";

        const telefonePattern = /^\([1-9]{2}\)\s(?:9[1-9][0-9]{3}|[2-9][0-9]{3})-[0-9]{4}$/;
        const value = inputElement.value.trim();

        inputElement.classList.remove('is-invalid', 'is-valid');
        if (feedbackElement && feedbackElement.classList.contains('invalid-feedback')) {
            feedbackElement.textContent = originalFeedbackMessage;
        }

        if (value === '') {
            if (inputElement.required) {
                inputElement.classList.add('is-invalid');
                if (feedbackElement && feedbackElement.classList.contains('invalid-feedback')) {
                    feedbackElement.textContent = requiredFeedbackMessage;
                }
                return false;
            }
            return true;
        }

        if (!telefonePattern.test(value)) {
            inputElement.classList.add('is-invalid');
            return false;
        }

        inputElement.classList.add('is-valid');
        return true;
    }

    // Função para inicializar a validação em um formulário específico
    // *** ALTERAÇÃO: Agora aceita uma configuração de campos numéricos ***
    function initializeFormValidation(formElement, numberFieldsConfig) {
        if (!formElement) {
            console.warn("Elemento de formulário não encontrado para inicializar validação.");
            return;
        }

        // A validação de telefone só é aplicada se o campo existir no formulário atual
        const telefoneInput = formElement.querySelector('#telefone');
        if (telefoneInput) {
            telefoneInput.addEventListener('input', applyPhoneMask);
            telefoneInput.addEventListener('blur', () => validatePhoneNumberPattern(telefoneInput));
        }

        formElement.addEventListener('submit', function (event) {
            let isFormOverallValid = true;

            // Validação do telefone no submit (só se existir)
            if (telefoneInput && !validatePhoneNumberPattern(telefoneInput)) {
                isFormOverallValid = false;
            }

            // *** ALTERAÇÃO: Usa a configuração de campos fornecida ***
            numberFieldsConfig.forEach(fieldConfig => {
                const input = formElement.querySelector('#' + fieldConfig.id);
                if (input) {
                    const feedback = input.nextElementSibling;
                    input.classList.remove('is-invalid', 'is-valid');

                    if (feedback && feedback.classList.contains('invalid-feedback')) {
                        feedback.textContent = fieldConfig.defaultMsg; // Restaura mensagem padrão
                    }

                    if (input.value !== '') { // Se o campo tem valor
                        if (parseFloat(input.value) < 0) {
                            input.classList.add('is-invalid');
                            if (feedback && feedback.classList.contains('invalid-feedback')) {
                                feedback.textContent = fieldConfig.negativeMsg;
                            }
                            isFormOverallValid = false;
                        } else {
                            if (input.checkValidity()) {
                                input.classList.add('is-valid');
                            } else {
                                input.classList.add('is-invalid');
                                isFormOverallValid = false;
                            }
                        }
                    } else if (input.required) { // Se o campo está vazio mas é obrigatório
                        input.classList.add('is-invalid');
                        isFormOverallValid = false;
                    }
                }
            });

            // Validação geral do Bootstrap
            if (!formElement.checkValidity()) {
                isFormOverallValid = false;
            }

            if (!isFormOverallValid) {
                event.preventDefault();
                event.stopPropagation();
            }

            formElement.classList.add('was-validated');
        }, false);
        console.log("Validação customizada inicializada para o formulário:", formElement.id);
    }

    // *** ALTERAÇÃO: Ponto de entrada que configura cada formulário separadamente ***
    window.addEventListener('load', function () {

        // --- Configuração para o Formulário de PROPRIEDADE ---
        const propFields = [
            { id: 'distancia_municipio', defaultMsg: 'A distância é obrigatória e não pode ser negativa.', negativeMsg: 'A distância não pode ser negativa.' },
            { id: 'numMaoObraFamiliar', defaultMsg: 'Número obrigatório e não pode ser negativo.', negativeMsg: 'O número de pessoas não pode ser negativo.' },
            { id: 'numContratadas', defaultMsg: 'Número obrigatório e não pode ser negativo.', negativeMsg: 'O número de pessoas não pode ser negativo.' },
            { id: 'mediaDiarias', defaultMsg: 'Média obrigatória e não pode ser negativa.', negativeMsg: 'A média de diárias não pode ser negativa.' }
        ];

        const cadastroPropForm = document.getElementById('cadastroPropForm');
        if (cadastroPropForm) {
            initializeFormValidation(cadastroPropForm, propFields);
        }

        const editPropForm = document.getElementById('editPropForm'); // Se houver um formulário de edição de propriedade
        if (editPropForm) {
            initializeFormValidation(editPropForm, propFields);
        }

        // --- Configuração para o Formulário de CULTIVO ---
        const cultivoFields = [
            { id: 'receita', defaultMsg: 'O percentual da receita é obrigatório e não pode ser negativa.', negativeMsg: 'O valor da receita não pode ser negativo.' },
            { id: 'ano_implantacao', defaultMsg: 'O ano é obrigatório.', negativeMsg: 'O ano não pode ser negativo.' },
            { id: 'numero_plantas', defaultMsg: 'O número de plantas é obrigatório.', negativeMsg: 'O número de plantas não pode ser negativo.' },
            { id: 'venda_canal', defaultMsg: 'O valor da venda é obrigatório.', negativeMsg: 'O valor da venda não pode ser negativo.' },
            { id: 'numero_pontos_venda', defaultMsg: 'O número de pontos é obrigatório.', negativeMsg: 'O número de pontos de venda não pode ser negativo.' },
            { id: 'distancia_entrega', defaultMsg: 'A distância é obrigatória.', negativeMsg: 'A distância não pode ser negativa.' }
        ];

        const cadastroCultivoForm = document.getElementById('cadastroCultivoForm');
        if (cadastroCultivoForm) {
            initializeFormValidation(cadastroCultivoForm, cultivoFields);
        }

    }, false);
})();