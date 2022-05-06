/* ***************************************************************
* Autor............: Gabriel Uzel Fonseca
* Matricula........: 202010028
* Inicio...........: 27/04/2022
* Ultima alteracao.: xx
* Nome.............: Leitor 2
* Funcao...........: Lê da base de dados e reserva uma cadeira
*************************************************************** */

package Model;

public class Leitor2 extends Thread {
    /* ***************************************************************
    * Metodo: run
    * Funcao: Inicializa a thread
    * Parametros: Void
    * Retorno: Void
    *************************************************************** */
    @Override
    public void run() { 
        while(true) {
            try {
                sleep(2000);
                Variaveis.getMutex().acquire(); // Trava a regiao critica da variavel de controle dos leitores

                Variaveis.incrementaLeitoresRC();
                // Caso esse seja o primeiro leitor a comecar a ler, trava a base de dados para que o escritor não a modifique indevidamente
                if(Variaveis.getLeitoresRc() == 1) { 
                    Variaveis.getDados().acquire();  // Trava a regiao critica da base de dados 
                }
                Variaveis.getMutex().release(); // Libera o acesso a variavel de controle

                sleep(500);
                int idCadeira = Variaveis.geraIndexCadeira();
                sleep(500);

                Variaveis.getMutex().acquire(); // Trava a regiao critica da variavel de controle dos leitores 

                Variaveis.decrementaLeitoresRC();
                // Caso esse seja o ultimo leitor a realizar a leitura, libera a base de dados para que o escritor possa modifica-la
                if(Variaveis.getLeitoresRc() == 0) { 
                    Variaveis.getDados().release(); // Libera a regiao critica da base de dados
                }
                
                Variaveis.solicitaCadeira(idCadeira); // Usa o valor lido

                Variaveis.getMutex().release();
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        } 
    } 
}
