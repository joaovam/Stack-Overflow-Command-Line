import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;

import ArvoreB.ArvoreBMais_ChaveComposta_Int_Int;
import TabelaHashExtensivel.*;
import TabelaHashExtensivel.aed3.HashExtensivel;
//import aed3.ListaInvertida;
import ListaInvertida.ListaInvertida;

public class Main {

  private static Scanner sc;
  private static int ID; // Usuário Ativo -> Sessão iniciada
  private static Pergunta PS; // Pergunta Selecionada
  private static int ordem = 10;

  /* Inicio Arquivos estaticos */
  private static Arquivo<Voto, pcvVoto> arquivoVoto;
  private static Arquivo<Usuario, pcvUsuario> arquivo;
  private static Arquivo<Pergunta, pcvPergunta> arquivoPergunta;
  private static Arquivo<Resposta, pcvResposta> arquivoResposta;
  private static HashExtensivel<pcvUsuarioEmail> hEmail;
  private static HashExtensivel<pcvVoto> heVoto;
  private static HashExtensivel<pcvPergunta> hePergunta;
  private static HashExtensivel<pcvResposta> heResposta;
  private static HashExtensivel<pcvUsuario> he;
  private static ArvoreBMais_ChaveComposta_Int_Int arvore;
  private static ArvoreBMais_ChaveComposta_Int_Int arvoreUsuarioResposta;
  private static ArvoreBMais_ChaveComposta_Int_Int arvorePerguntaResposta;
  private static ArvoreBMais_ChaveComposta_Int_Int arvoreVotoUsuarioPergunta;
  private static ArvoreBMais_ChaveComposta_Int_Int arvoreVotoUsuarioResposta;
  private static ListaInvertida listaPalavrasChave;

  /* Fim Arquivos estaticos */

  private static ArrayList<Integer> arquivadas = new <Integer>ArrayList();

  private static void Menu() throws Exception {

    int opcao = -1;

    do {
      System.out.print("\033[H\033[2J");
      System.out.println("\nPERGUNTAS CJLR 1.0");
      System.out.println("==================");
      System.out.println("\n Acesso");
      System.out.println("[0] Sair");
      System.out.println("[1] Acesso ao sistema");
      System.out.println("[2] Cadastro (Novo Usuário)");
      System.out.print("Opção: ");
      opcao = sc.nextInt();
      switch (opcao) {
      case 0:
        System.out.println("Sair do sistema");
        break;
      case 1:
        boolean acesso = Acesso();
        System.out.println(((acesso ? "\nAcesso Autorizado!" : "\nAcesso Negado!") + "\n"));
        if (acesso) {
          System.out.println("Entrando no Sistema!");
          System.out.println("Pressione Enter para continuar...");
          sc.nextLine();
          MenuPergunta();
        } else {
          System.out.println("Redirecionando de volta ao menu!\n\nPressione Enter para continuar...");
          sc.nextLine();

        }

        break;
      case 2:
        boolean cadastro = Cadastro();
        System.out.println((cadastro) ? "Usuario Cadastrado com sucesso!" : "Usuario não Cadastrado!");

        System.out.println("Pressione Enter para Continuar!");
        sc.nextLine();
        break;
      case 3:
        sc.nextLine();
        ConsultarPerguntas();

        break;

      default:
        System.out.println("Opção inválida! Tente novamente");
      }

    } while (opcao != 0);
  }

  // Menu de perguntas
  private static void MenuPergunta() throws Exception {
    int opcao = -1;
    while (opcao != 0) {
      System.out.print("\033[H\033[2J");
      System.out.print("PERGUNTAS CJLR 1.0");
      System.out.println("\nINÍCIO");
      System.out.println("\n=============");
      System.out.println("\n1) Criação de perguntas");
      System.out.println("\n2) Consultar/responder perguntas ");
      System.out.println("\n3) Notificações: 0 ");
      System.out.println("\n0) Sair");
      System.out.print("\nOpção: ");
      opcao = sc.nextInt();

      switch (opcao) {
      case 0:
        break;
      case 1:
        CRUDPerguntas();
        break;
      case 2:
        sc.nextLine();
        ConsultarPerguntas();
        break;
      case 3:
        System.out.println("Em criação- notificações");
        break;
      default:
        System.out.println("Opcao Invalida!");
        break;
      }
    }
  }

  // Segunda Parte do menu de perguntas, Crud para perguntas
  private static void CRUDPerguntas() throws Exception {
    int opcao = -1;
    do {
      System.out.print("\033[H\033[2J");
      System.out.print(
          "PERGUNTAS CJLR 1.0\n  =============\nINÍCIO > CRIAÇÃO DE PERGUNTAS \n\n1) Listar \n\n2) Incluir \n\n3) Alterar \n\n4) Arquivar \n\n0) Retornar ao menu anterior \n\nOpção: ");
      opcao = sc.nextInt();
      sc.nextLine();

      switch (opcao) {

      case 1:
        Buscar();
        break;
      case 2:
        System.out.println((Incluir()) ? "Pergunta criada com sucesso!" : "Pergunta não criada!\n");
        break;
      case 3:
        Alterar();
        break;
      case 4:
        Arquivar();
        break;
      }

    } while (opcao != 0);

  }
  // Consultar perguntas por meio da lista invertida

  String perguntaSelecionada = "";

  public static void ConsultarPerguntas() throws Exception {
    String resposta;
    String[] arrayPalavras;
    boolean opcaoVoltar = false;

    System.out.print("\033[H\033[2J");
    System.out.println("PERGUNTAS CJLR 1.0");
    System.out.println("=============");
    System.out.println("INÍCIO > Consultar/responder perguntas");
    System.out.println("Busque as perguntas por palavra chave separadas por ponto e vírgula");
    System.out.println("Ex: política;Brasil;eleições");
    System.out.println("Palavras chave:");
    String palavrasChave = sc.nextLine();

    System.out.print("Deseja buscar por essa(s) palavra(s) chave? [S/N]:  ");
    resposta = sc.nextLine();
    if (resposta.toLowerCase().compareTo("s") == 0) {
      System.out.println();
      palavrasChave = tratamentoStrings.formatarString(palavrasChave);
      arrayPalavras = tratamentoStrings.separarStrings(palavrasChave);
      int[] idPerguntasFiltradas = buscaPerguntas(arrayPalavras);
      if (idPerguntasFiltradas.length != 0) {
        List<Pergunta> perguntas = new ArrayList<>();// arraylist de perguntas
        for (int i = 0; i < idPerguntasFiltradas.length; i++) {
          perguntas.add(arquivoPergunta.read(idPerguntasFiltradas[i]));
        }
        Collections.sort(perguntas);
        int o = 1;
        for (Pergunta p : perguntas) {
          System.out.println("[" + (o) + "]" + p.getPergunta());
          // print pergunta resumida para selecao do usuario
          o++;
        }
        
          System.out.print("\nDigite qual pergunta gostaria de selecionar: ");
        int opcaoPergunta = sc.nextInt() - 1;
        // printa pergunta completa com nome do usuario dono
        if (opcaoPergunta >= o - 1) {
          System.out.println("Opcao inválida!");
        } else {
          System.out.print("\033[H\033[2J");
          PS = perguntas.get(opcaoPergunta);

          System.out.println(PS.toString(arquivo.read(PS.getIdUsuario()).getNome()));
          int[] ids = arvorePerguntaResposta.read(PS.getId());
          if (ids.length == 0) {
            System.out.println("Ainda não existem respostas para essa pergunta.");
          } else {
            // System.out.println(ids.length);
            System.out.println("RESPOSTAS");
            System.out.println("============================================================");
            o = 1;
            for (int id : ids) {
              if (arquivoResposta.read(id).getAtiva()) {
                System.out.println((o++) + ". " + arquivoResposta.read(id)
                    .toString(arquivo.read(arquivoResposta.read(id).getID_Usuario()).getNome()));
                System.out.println("============================================================");
              }

            }
          }
        }

        switch (MenuOperacaoPergunta()) {
          case 1:
            sc.nextLine();
            MenuRespostas();
            break;
          case 2:
            sc.nextLine();
            opcaoVoltar = MenuAvaliar();
            break;
          case 3:
            sc.nextLine();
            MenuPergunta();
            break;
          case 0:
            sc.nextLine();
            ConsultarPerguntas();
            break;
          default:
            System.out.println("Opção invalida");
        }

      } else {
        System.out.println("Não foram encontradas perguntas com as palavras chave pesquisadas!");
        System.out.println("Pressione Enter para continuar...");
        String oi = sc.nextLine();
      }

    } else {

      System.out.println("Palavras chave não pesquisadas!");
      System.out.println("Pressione Enter para continuar...");
      sc.nextLine();
    }
  }
  private static int MenuOperacaoPergunta() {
    sc.nextLine();

    System.out.println("1) Responder");
    System.out.println("2) Avaliar");
    System.out.println("\n3) Retornar ao menu anterior");
    System.out.println("0) Retornar a busca por palavras chave");
    System.out.print("Opção: ");
    int opcao = sc.nextInt();

    return opcao;
  }

  public static void MenuRespostas() throws Exception {
    int opcao = -1;
    do {
      System.out.print("\033[H\033[2J");
      System.out.println("PERGUNTAS 1.0 CJRL");
      System.out.println("==================\n");
      System.out.println("INÍCIO > PERGUNTAS > RESPOSTAS\n");

      System.out.println(PS.toString(arquivo.read(PS.getIdUsuario()).getNome()));

      System.out.print("1) Listar suas respostas\n2) Incluir uma resposta\n3) Alterar uma resposta\n4) Arquivar uma resposta \n\n0)Retornar ao menu anterior\nOpção: ");
      opcao = sc.nextInt();
      sc.nextLine();

      switch (opcao) {

      case 1:
        ListarResposta(PS.getId());
        break;
      case 2:
        System.out.println((IncluirResposta(PS.getId())) ? "Resposta criada com sucesso!" : "Resposta não criada!\n");
        break;
      case 3:
        AlterarResposta(PS.getId());
        break;
      case 4:
        ArquivarResposta(PS.getId());
        break;
      }
    } while (opcao != 0);
  }
  
  public static boolean MenuAvaliar() throws Exception {
    boolean avaliado = true;
    boolean encontrado = false;
    int id = 0;
    String tipoEscolhido="";
    int opcao;
    boolean voto;
    int positivoNeg;
    System.out.println("AVALIAR");
    System.out.println("=============");
   
    System.out.println("\nGostaria de votar em:\n1) Pergunta selecionada anteriormente \n2) Alguma das respostas para a pergunta selecionada\n0) Retornar\nOpção: ");
    opcao = sc.nextInt();
    
    byte tipo = 'N';
    boolean valido = false;
    
    if (opcao == 0) {
      avaliado = true;
    }else {
      if(opcao == 1){
        id = PS.getId();
        tipo = (byte) 'P';  
        tipoEscolhido="pergunta selecionada \"" +PS.getPergunta()+ "\" ";  
        valido = true; 
      }else{
         
        System.out.println("Digite o número da resposta que deseja avaliar. \nDigite 0 para retornar ao menu anterior");
        int[] ids = arvorePerguntaResposta.read(PS.getId());
        int idDigitado = sc.nextInt() ;
        if(idDigitado > ids.length ){
          System.out.println("Número de resposta inválido! Voltando ao menu anterior...\n Pressione Enter para continuar");

          sc.nextLine();
          sc.nextLine();
          //
        }else{
          if (idDigitado == 0) {
          avaliado = false;
          }else {
            
            id = ids[idDigitado - 1];
            tipo = 'R';
            tipoEscolhido="resposta \"" + arquivoResposta.read(ids[idDigitado-1]).getResposta()+"\""+ " da pergunta selecionada" ; 
          }
          valido = true;
        }
        
      }
      if (valido){
        if(tipo == 'P')
          encontrado = buscarPerguntasRespostasVotadas('P', id);
        else if (tipo == 'R')
          encontrado = buscarPerguntasRespostasVotadas('R', id);

        if(encontrado){
          System.out.println("Não é possivel votar novamente!");
          System.out.println("Pressione enter para continuar...");
          sc.nextLine();
          sc.nextLine();
          avaliado = false;
        }else{
          System.out.println("Seu voto é:\n1) Positivo \n2) Negativo");
          positivoNeg = sc.nextInt();
          String avaliacao="";
          if (positivoNeg == 1) {
            voto = true;
            avaliacao="positivo";
          } else {
            voto = false;
            avaliacao="negativo";
          }
          System.out.println("Seu voto é  "+ avaliacao +" para a "+ tipoEscolhido);
          System.out.println("Deseja confirmar o voto? [S/N]");
          sc.nextLine();
          String confirmacao = sc.nextLine();
          if (confirmacao.toUpperCase().compareTo("S") == 0) {

            Voto votoUsuario = new Voto(-1, ID, tipo, id, voto);
            arquivoVoto.create(votoUsuario);
            //System.out.println(votoUsuario);
            if (votoUsuario.getTipo() == 'P') {
              PS.atualizaNota(voto);
              arquivoPergunta.update(PS);
              avaliado = arvoreVotoUsuarioPergunta.create(ID, id);
            } else {
              Resposta resp = arquivoResposta.read(id);
              resp.atualizaNota(voto);
              arquivoResposta.update(resp);
              avaliado = arvoreVotoUsuarioResposta.create(ID, id);
            }
            System.out.println("Voto criado com sucesso!");
            System.out.println("Pressione enter para continuar...");
            sc.nextLine();
          }else{
            System.out.println("Voto não computado!");
            System.out.println("Pressione enter para continuar...");
            sc.nextLine();
          }
        }
      }
    }
    return avaliado;
  }

  private static boolean buscarPerguntasRespostasVotadas(char tipo, int id) throws Exception {
    boolean jaVotou = false;

    int[] pgVotadas = arvoreVotoUsuarioPergunta.read(ID);
    int[] respVotadas = arvoreVotoUsuarioResposta.read(ID);

    if(tipo == 'R'){
      if(respVotadas.length > 0){
        for(int i = 0; i < respVotadas.length && jaVotou == false; i++){
          if(respVotadas[i] == id)
            jaVotou = true;
        }
      }
    }else{
      if(pgVotadas.length > 0){
        for(int i = 0; i < pgVotadas.length && jaVotou == false; i++){
          if(pgVotadas[i] == id)
            jaVotou = true;
        }
      }
    }

    return jaVotou;
  }

  public static void ListarResposta(int idPergunta) throws Exception {
    int[] idResp = arvorePerguntaResposta.read(idPergunta);

    if (idResp.length <= 0) {
      System.out.println("Nenhuma resposta cadastrada ainda!");
    } else {
      int o = 1;
      System.out.println("\nMINHAS RESPOSTAS\n");
      for (int i = 0; i < idResp.length; i++) {
        if (arquivoResposta.read(idResp[i]).getID_Usuario() == ID)
          System.out.println((o++) + "." + arquivoResposta.read(idResp[i]));
      }
    }

    System.out.println("\nPressione enter para continuar...");
    sc.nextLine();
  }

  public static boolean IncluirResposta(int idPergunta) throws Exception {
    boolean incluir;

    System.out.print("\nDigite a resposta para pergunta selecionada: ");
    String resp = sc.nextLine();

    if (resp.isEmpty()) {
      incluir = false;
    } else {
      System.out.println("Deseja postar a resposta? [S/N]");
      String confirmacao = sc.nextLine();
      confirmacao = confirmacao.toUpperCase();

      if (confirmacao.equals("N")) {
        incluir = false; // volta pro menu
        System.out.println("Infelizmente não foi possível registrar sua resposta! Tente novamente...");
      } else if (confirmacao.equals("S")) {
        int idResp = arquivoResposta.create(new Resposta(0, ID, idPergunta, resp));

        incluir = arvoreUsuarioResposta.create(ID, idPergunta);
        arvorePerguntaResposta.create(idPergunta, idResp);
      } else {
        System.out.println("Não consegui compreender! Opção de confirmação inválida");
        incluir = false;
      }
    }

    return incluir;
  }

  private static ArrayList<Integer> idAtivoSelecionado(int idPergunta) throws Exception {
    ArrayList<Integer> respAtivas = new ArrayList<Integer>();
    int[] idResp = arvorePerguntaResposta.read(idPergunta);

    if (idResp.length <= 0) {
      System.out.println("Nenhuma resposta cadastrada ainda!");
    } else {
      for (int i = 0; i < idResp.length; i++) {
        if (arquivoResposta.read(idResp[i]).getID_Usuario() == ID && arquivoResposta.read(idResp[i]).getAtiva())
          respAtivas.add(arquivoResposta.read(idResp[i]).getId());
      }

      System.out.println("\nMINHAS RESPOSTAS\n");
      for (int i = 0; i < respAtivas.size(); i++) {
        System.out.println((i + 1) + "." + arquivoResposta.read(respAtivas.get(i)));
      }
    }

    return respAtivas;
  }

  public static boolean AlterarResposta(int idPergunta) throws Exception {
    boolean alterar;
    int idResp_Selecionada = -1;
    ArrayList<Integer> respAtivas = idAtivoSelecionado(idPergunta);

    System.out.print("\nDigite o número da resposta que deseja alterar: ");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao > respAtivas.size())
      opcao = 0;
    else
      idResp_Selecionada = respAtivas.get(opcao - 1);

    if (opcao == 0) {
      alterar = false;
    } else {
      System.out.println(arquivoResposta.read(idResp_Selecionada));
      System.out.print("\nDigite a nova resposta: ");
      String novaResposta = sc.nextLine();

      if (novaResposta.isEmpty()) {
        alterar = false;
      } else {
        System.out.println("Deseja alterar a resposta? [S/N]");
        String confirmacao = sc.nextLine();
        confirmacao = confirmacao.toUpperCase();

        if (confirmacao.equals("N"))
          alterar = false;
        else if (confirmacao.equals("S")) {
          arquivoResposta.update(new Resposta(idResp_Selecionada, ID, idPergunta, novaResposta));
          System.out.println("Resposta alterada com sucesso!");
          System.out.println("--------------------------------");
          System.out.println(arquivoResposta.read(idResp_Selecionada));
          System.out.println("--------------------------------");
          alterar = true;
        } else {
          System.out.println("Não consegui compreender! Opção de confirmação inválida");
          alterar = false;
        }
      }
    }

    System.out.println("\nPressione enter para continuar...");
    sc.nextLine();

    return alterar;
  }

  public static boolean ArquivarResposta(int idPergunta) throws Exception {
    boolean arquivar;
    int idResp_Selecionada = -1;
    ArrayList<Integer> respAtivas = idAtivoSelecionado(idPergunta);

    System.out.println("Digite o número da resposta que deseja arquivar: ");
    int opcao = sc.nextInt();
    sc.nextLine();

    if (opcao > respAtivas.size())
      opcao = 0;
    else
      idResp_Selecionada = respAtivas.get(opcao - 1);

    if (opcao == 0) {
      System.out.println("Não foi possível arquivar esta resposta");
      arquivar = false;
    } else {
      System.out.println(arquivoResposta.read(idResp_Selecionada));

      System.out.println("Deseja arquivar a resposta? [S/N]");
      String confirmacao = sc.nextLine();
      confirmacao = confirmacao.toUpperCase();

      if (confirmacao.equals("N"))
        arquivar = false;
      else if (confirmacao.equals("S")) {
        Resposta r = arquivoResposta.read(idResp_Selecionada);
        r.setAtiva(false);
        arquivar = arquivoResposta.update(r);

        System.out.println("Resposta arquivada com sucesso!");
      } else {
        System.out.println("Não consegui compreender! Opção deconfirmação inválida");
        arquivar = false;
      }
    }

    System.out.println("\nPressione enter para continuar...");
    sc.nextLine();

    return arquivar;
  }

  private static int[] buscaPerguntas(String[] arrayPalavras) throws Exception {

    int[] arrayInicial = listaPalavrasChave.read(arrayPalavras[0]);
    Set<Integer> setAUX = new HashSet<>();
    Set<Integer> set = new HashSet<>();
    for (int i = 0; i < arrayInicial.length; i++) {
      set.add(arrayInicial[i]);
    }

    for (int i = 1; i < arrayPalavras.length; i++) {
      arrayInicial = listaPalavrasChave.read(arrayPalavras[i]);

      for (int j = 0; j < arrayInicial.length; j++) {
        setAUX.add(arrayInicial[j]);
      }
      set.retainAll(setAUX);
    }

    int[] retorno = new int[set.size()];
    // System.out.println(set.size());
    int j = 0;
    for (int i : set) {
      // System.out.println(retorno[j]);
      retorno[j] = i;
      j++;

    }

    return retorno;

  }

  // Inclui nova pergunta
  private static boolean Incluir() throws Exception {
    boolean incluir = false;
    System.out.println("Digite sua pergunta: ");
    String pergunta = sc.nextLine();
    System.out.println(
        "Digite as palavras-chave relacionadas à sua pergunta, utilize ponto e vírgula para separar as palavras: ");
    String palavrasChave = sc.nextLine();

    if (pergunta.isEmpty()) {
      System.out.println("Pergunta não informada! Tente novamente...");
      incluir = false;

    } else {
      System.out.println("Deseja postar a pergunta? [S/N]");
      String confirmacao = sc.nextLine();
      confirmacao = confirmacao.toUpperCase();

      if (confirmacao.equals("N")) {
        incluir = false; // volta pro menu
        System.out.println("Infelizmente não foi possível registrar sua pergunta! Tente novamente...");

      } else if (confirmacao.equals("S")) {

        int id_Pergunta = arquivoPergunta.create(new Pergunta(0, ID, pergunta, palavrasChave));

        incluiPC(palavrasChave, id_Pergunta);

        incluir = arvore.create(ID, id_Pergunta);
      } else {
        System.out.println("Não consegui compreender! Opção de confirmação inválida");
        incluir = false;
      }
    }
    return incluir;
  }

  private static void incluiPC(String pc, int idPerg) throws Exception {
    String[] tmp = pc.split(";");
    for (int i = 0; i < tmp.length; i++) {
      tmp[i] = tratamentoStrings.formatarString(tmp[i]);
      listaPalavrasChave.create(tmp[i], idPerg);
    }
  }

  private static void arquivarPC(String pc, int id) throws Exception {
    String[] tmp = pc.split(";");
    for (int i = 0; i < tmp.length; i++) {
      tmp[i] = tratamentoStrings.formatarString(tmp[i]);
      listaPalavrasChave.delete(tmp[i], id);
    }
  }

  // Busca Pergunta
  private static void Buscar() throws Exception {
    int ID_Perguntas[] = arvore.read(ID);

    if (ID_Perguntas.length <= 0) {
      System.out.println("Nenhuma pergunta cadastrada ainda!");
    } else {
      System.out.println("\nMINHAS PERGUNTAS\n");
      for (int i = 0; i < ID_Perguntas.length; i++)
        System.out.println((i + 1) + "." + arquivoPergunta.read(ID_Perguntas[i]));

    }

    // listaPalavrasChave.print();

    System.out.println("\nPressione enter para continuar...");
    sc.nextLine();
  }

  private static void tratarPalavrasChaves(String novaPalavraChave, String antiga, int idPergunta) throws Exception {
    HashSet<String> hsNova = new HashSet<String>();
    HashSet<String> hsAntigo = new HashSet<String>();
    HashSet<String> tmpSet = new HashSet<String>();
    tmpSet = (HashSet) hsAntigo.clone();

    String[] palavras = tratamentoStrings.formatarString(novaPalavraChave).split(";");
    String[] palavrasAntigas = tratamentoStrings.formatarString(antiga).split(";");

    for (int i = 0; i < palavras.length; i++)
      hsNova.add(palavras[i]);

    for (int i = 0; i < palavrasAntigas.length; i++)
      hsAntigo.add(palavrasAntigas[i]);

    hsAntigo.removeAll(hsNova); // Retirar do arquivo de lista invertida de quem sobrou em hsAntigo
    for (String nova : hsAntigo)
      listaPalavrasChave.delete(nova, idPergunta);

    hsNova.removeAll(tmpSet); // Adicionar do arquivo de lista invertida

    for (String nova : hsNova)
      listaPalavrasChave.create(nova, idPergunta);

  }

  // Altera pergunta
  private static boolean Alterar() throws Exception {
    boolean alterar;
    int ID_Perguntas[] = arvore.read(ID);
    ID_Perguntas = tratarVetor(ID_Perguntas);

    System.out.println("\nMINHAS PERGUNTAS\n");
    for (int i = 0; i < ID_Perguntas.length; i++) {
      Pergunta obj = arquivoPergunta.read(ID_Perguntas[i]);
      if (obj.getAtiva()) {
        System.out.println((i + 1) + "." + arquivoPergunta.read(ID_Perguntas[i]));
      }
    }

    System.out.println("Qual pergunta você deseja alterar?");
    int idPergunta = sc.nextInt();
    sc.nextLine();

    if (idPergunta == 0) {
      System.out.println("Não foi possível alterar a pergunta!");
      alterar = false;
    } else {
      System.out.println("\nPERGUNTA SELECIONADA: ");
      System.out.print(arquivoPergunta.read(ID_Perguntas[idPergunta - 1]));

      System.out.print("\nDigite a nova pergunta: ");
      String novaPergunta = sc.nextLine();
      System.out.print("\nDigite as novas palavras-chave: ");
      String novasPalavrasChave = sc.nextLine();

      if (novaPergunta.isEmpty()) {
        System.out.println("Pergunta não informada!");
        alterar = false;
      } else {
        System.out.println("Deseja alterar a pergunta? [S/N]");
        String confirmacao = sc.nextLine();
        confirmacao = confirmacao.toUpperCase();

        if (confirmacao.equals("N")) // ainda tem que checar se a pergunta ja foi respondida
          alterar = false; // volta pro menu
        else {
          tratarPalavrasChaves(novasPalavrasChave,
              arquivoPergunta.read(ID_Perguntas[idPergunta - 1]).getPalavrasChave(), idPergunta - 1); // atualizar o
                                                                                                      // indice
          arquivoPergunta.update(new Pergunta(ID_Perguntas[idPergunta - 1], ID, novaPergunta, novasPalavrasChave));
          System.out.println(arquivoPergunta.read(ID_Perguntas[idPergunta - 1]));
          System.out.println("Pergunta alterada com sucesso!");

          alterar = true;

        }
      }
    }

    System.out.println("\nPressione enter para continuar...");
    sc.nextLine();

    return alterar;
  }

  private static int[] tratarVetor(int[] vetor) throws Exception {
    int cont = 0;

    for (int i = 0; i < vetor.length; i++) {
      Pergunta obj = arquivoPergunta.read(vetor[i]);
      if (obj.getAtiva()) {
        vetor[cont++] = obj.getId();
      }
    }
    int[] retorno = new int[cont];
    for (int i = 0; i < cont; i++)
      retorno[i] = vetor[i];

    return retorno;
  }

  // Arquiva pergunta
  private static boolean Arquivar() throws Exception {
    boolean arquivar;
    int ID_Perguntas[] = arvore.read(ID);
    ID_Perguntas = tratarVetor(ID_Perguntas);

    System.out.println("\nMINHAS PERGUNTAS\n");
    for (int i = 0; i < ID_Perguntas.length; i++) {
      Pergunta obj = arquivoPergunta.read(ID_Perguntas[i]);
      if (obj.getAtiva()) {
        System.out.println((i + 1) + "." + arquivoPergunta.read(ID_Perguntas[i]));
      }
    }

    System.out.println("Qual pergunta voce deseja arquivar?");
    int idSequencial = sc.nextInt();
    sc.nextLine();

    if (idSequencial == 0) {
      System.out.println("Não foi possível arquivar a pergunta!");
      arquivar = false;
    } else {
      System.out.println("\nPERGUNTA SELECIONADA: ");
      System.out.print(arquivoPergunta.read(ID_Perguntas[idSequencial - 1]));

      System.out.println("Deseja arquivar a pergunta? [S/N]");
      String confirmacao = sc.nextLine();
      confirmacao = confirmacao.toUpperCase();

      if (confirmacao.equals("N"))
        arquivar = false; // volta pro menu
      else {
        Pergunta p = arquivoPergunta.read(ID_Perguntas[idSequencial - 1]);
        p.setAtiva(false);
        arquivoPergunta.update(p);
        arquivarPC(p.getPalavrasChave(), ID_Perguntas[idSequencial - 1]);
        System.out.println("Pergunta arquivada com sucesso!");
        arquivar = true;

      }
    }

    System.out.println("\nPressione enter para continuar...");
    sc.nextLine();

    return arquivar;
  }

  // Loga no Sistema
  private static boolean Acesso() throws Exception {
    boolean acesso = false;
    System.out.print("\033[H\033[2J");
    sc.nextLine();
    System.out.print("Digite Email: ");

    String email = sc.nextLine();
    System.out.print("Digite Senha: ");
    String senha = sc.nextLine();

    pcvUsuarioEmail pcvue = hEmail.read(email.hashCode());
    // System.out.println(pcvue);
    if (pcvue != null) {

      Usuario usuario = arquivo.read(pcvue.getId());
      // System.out.println(usuario);

      if (usuario != null) {

        if (Usuario.gerarHash(senha).compareTo(usuario.getSenha()) == 0 && email.compareTo(usuario.getEmail()) == 0) {
          acesso = true;
          ID = usuario.getId();
        }

      }
    }
    return acesso;
  }

  // Cadastra nova pergunta
  private static boolean Cadastro() throws Exception {
    boolean cadastro = false;
    sc.nextLine();
    System.out.print("\033[H\033[2J");
    System.out.println("Digite seu Email: ");

    String email = sc.nextLine();

    if (email.isEmpty()) {

      System.out.println("Email nao informado");

    } else {
      pcvUsuarioEmail pcvue = hEmail.read(email.hashCode());

      if (!(pcvue == null)) {

        System.out.println("Email ja cadastrado!");

      } else {
        System.out.println("Digite seu nome: ");
        String nome = sc.nextLine();

        System.out.println("Digite sua senha: ");
        String senha = sc.nextLine();

        System.out.println("Nome: " + nome + " | E-mail: " + email);
        System.out.println("Os dados estão corretos? [S/N]");
        String confirmacao = sc.nextLine();
        confirmacao = confirmacao.toUpperCase();

        if (confirmacao.equals("N"))
          cadastro = false; // volta pro menu
        else {
          Usuario user = new Usuario(-1, nome, email, senha);
          int idRetornado = arquivo.create(user);
          hEmail.create(new pcvUsuarioEmail(email, idRetornado));
          cadastro = true;

        }

      }

    }
    return cadastro; // volta pro menu

  }

  public static void main(String[] args) throws Exception {

    try {

      sc = new Scanner(System.in);
      he = new HashExtensivel<>(pcvUsuario.class.getConstructor(), 100, "dados/Usuarios.hash_d.db",
          "dados/Usuarios.hash_c.db");

      hEmail = new HashExtensivel<>(pcvUsuarioEmail.class.getConstructor(), 100, "dados/UsuariosEmail.hash_d.db",
          "dados/UsuariosEmail.hash_c.db");
      heVoto = new HashExtensivel<>(pcvVoto.class.getConstructor(), 100, "dados/Votos.hash_d.db",
          "dados/Votos.hash_c.db");

      arquivo = new Arquivo<>(Usuario.class.getConstructor(), pcvUsuario.class.getConstructor(long.class, int.class),
          he, "dados/Usuarios.db");

      hePergunta = new HashExtensivel<>(pcvPergunta.class.getConstructor(), 100, "dados/Perguntas.hash_d.db",
          "dados/Perguntas.hash_c.db");

      arquivoPergunta = new Arquivo(Pergunta.class.getConstructor(),
          pcvPergunta.class.getConstructor(long.class, int.class), hePergunta, "dados/Perguntas.db");
      arquivoVoto = new Arquivo(Voto.class.getConstructor(), pcvVoto.class.getConstructor(long.class, int.class),
          heVoto, "dados/Votos.db");
      arvore = new ArvoreBMais_ChaveComposta_Int_Int(ordem, "dados/arvore.db");
      listaPalavrasChave = new ListaInvertida(100, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");

      heResposta = new HashExtensivel<>(pcvResposta.class.getConstructor(), 100, "dados/Respostas.hash_d.db",
          "dados/Respostas.hash_c.db");

      arquivoResposta = new Arquivo(Resposta.class.getConstructor(),
          pcvResposta.class.getConstructor(long.class, int.class), heResposta, "dados/Respostas.db");

      arvoreUsuarioResposta = new ArvoreBMais_ChaveComposta_Int_Int(ordem, "dados/arvoreUsuarioResposta.db");

      arvorePerguntaResposta = new ArvoreBMais_ChaveComposta_Int_Int(ordem, "dados/arvorePerguntaResposta.db");

      arvoreVotoUsuarioPergunta = new ArvoreBMais_ChaveComposta_Int_Int(ordem, "dados/arvoreVotoUsuarioPergunta.db");

      arvoreVotoUsuarioResposta = new ArvoreBMais_ChaveComposta_Int_Int(ordem, "dados/arvoreVotoUsuarioResposta.db");

      Menu();

    } catch (Exception e) {
      System.out.println("Erro Main: " + e);
    }

  }

}
