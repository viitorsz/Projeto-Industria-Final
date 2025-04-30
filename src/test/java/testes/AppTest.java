package testes;


 



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Objeto {
    private String nome;

    public Objeto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

public class AppTest {
    @Test
    public void Teste() {
        assertEquals(1, 1, "Resultado certo");
    }

    @Test
    public void testeComDoisObjetos() {
        Objeto obj1 = new Objeto("Objeto1");
        Objeto obj2 = new Objeto("Objeto2");

        assertEquals("Objeto1", obj1.getNome(), "O nome do primeiro objeto está correto");
        assertEquals("Objeto2", obj2.getNome(), "O nome do segundo objeto está correto");
    }

    @Test
    public void testeCompararObjetosIguais() {
        Objeto obj1 = new Objeto("MesmoNome");
        Objeto obj2 = new Objeto("MesmoNome");

        assertEquals(obj1.getNome(), obj2.getNome(), "Os nomes dos objetos são iguais");
    }

    @Test
    public void testeObjetoComNomeVazio() {
        Objeto obj = new Objeto("");

        assertEquals("", obj.getNome(), "O nome do objeto está vazio");
    }
}