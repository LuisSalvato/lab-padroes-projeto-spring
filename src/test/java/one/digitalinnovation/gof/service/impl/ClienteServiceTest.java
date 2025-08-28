package test.one.digitalinnovation.gof.service.impl;

import dio.lab.padroes.projeto.spring.model.Cliente;
import dio.lab.padroes.projeto.spring.model.ClienteRepository;
import dio.lab.padroes.projeto.spring.model.Endereco;
import dio.lab.padroes.projeto.spring.model.EnderecoRepository;
import dio.lab.padroes.projeto.spring.service.ViaCepService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ViaCepService viaCepService;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void testSalvarClienteComNovoCep() {
        // Cenário de Teste
        String cep = "01001000";
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        cliente.setEndereco(new Endereco()); // Seta um objeto de Endereco vazio para simular o estado inicial

        Endereco enderecoViaCep = new Endereco();
        enderecoViaCep.setCep(cep);
        enderecoViaCep.setLogradouro("Praça da Sé");
        
        // Comportamento dos Mocks
        when(enderecoRepository.findById(cep)).thenReturn(Optional.empty()); // Simula que o CEP não está no banco
        when(viaCepService.consultarCep(cep)).thenReturn(enderecoViaCep); // Simula a busca na ViaCEP
        
        // Execução do Método a ser Testado
        clienteService.salvar(cliente);
        
        // Verificações (Asserções)
        // Garante que o método save() do enderecoRepository foi chamado com o objeto de endereço retornado pela ViaCEP
        verify(enderecoRepository, times(1)).save(enderecoViaCep);

        // Garante que o método save() do clienteRepository foi chamado
        verify(clienteRepository, times(1)).save(cliente);

        // Garante que o endereço foi setado no objeto cliente antes de ser salvo
        assertNotNull(cliente.getEndereco());
    }
}