package br.com.fullcycle.hexagonal.application;

public abstract class UseCase<INPUT, OUTPUT> {

    //1 - Cada caso de uso tem um INPUT e um OUTPUT próprio. Não retorna a entidade, o agregado ou o objeto de valor
    //2 - O caso de uso implementa o padrão Command (cada classe deve ter um único método público chamado execute)

    public abstract OUTPUT execute(INPUT input);
}
