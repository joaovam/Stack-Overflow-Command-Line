
import java.util.*;
import java.io.*;
public class Pergunta implements Registro, Comparable<Pergunta>{

  private int idPergunta;
  private int idUsuario;
  private long criacao;
  private short nota;
  private String pergunta;
  private boolean ativa;
  public String palavrasChave;
  
 //	Calendar(TimeZone zone, Locale aLocale)

  public Pergunta(){

    this(0,-1);

  }

 public Pergunta(int id,int idUsuario){    
    this(id, idUsuario,(short)0, "", "", true);
  }

  public Pergunta(int id,int idUsuario, String pergunta, String palavrasChave){
    this(id, idUsuario,(short)0, pergunta, palavrasChave, true);
  }

  public Pergunta(int id,int idUsuario, boolean arquivada){
    this(id, idUsuario,(short)0, "", "", arquivada);
  }
  
  public Pergunta(int idP, int idUser, short nota, String pergunta, String palavrasChave, boolean ativa){    
    this.idPergunta=idP; 
    this.idUsuario=idUser;    
    criacao = System.currentTimeMillis();
    this.nota=nota;
    this.pergunta=pergunta;
    this.ativa=ativa;
    this.palavrasChave = tratamentoStrings.formatarString(palavrasChave);
  }
  
  public String getPalavrasChave(){
    return this.palavrasChave;
  }
 
 @Override
  public int compareTo(Pergunta pergunta){
    return pergunta.getNota() - this.getNota();

  }


 public void setId(int id){
    this.idPergunta = id;
  }
  
  public int getId(){
    return this.idPergunta;
  }


  public long getCriacao(){    
    
    return this.criacao;

  }
  public int getNota(){
    return this.nota;
  }
  


  public void setPergunta(String pergunta){
    this.pergunta=pergunta;
  }
  
  public String getPergunta(){
    return this.pergunta;
  }


  public void setAtiva(boolean ativa){
    this.ativa=ativa;
  }
  public boolean getAtiva(){
    return this.ativa;
  }
  
  public int getIdUsuario(){
    return this.idUsuario;
  }
  
  public void atualizaNota(boolean voto){
    if(voto){
      this.nota += (byte) 1;
    }else{      
      this.nota -= (byte) 1;
    }
            
  }

  public String toString(){
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-03:00"),new Locale("pt","BR"));
    calendar.setTimeInMillis(this.criacao);


    return (/*"ID Usuario: "+idUsuario + 
    "\nID Pergunta: "+idPergunta */
    ((ativa)  ? "" : "(Arquivada)")
    +"\n" + calendar.get(Calendar.DAY_OF_MONTH) 
    +"/"+(calendar.get(Calendar.MONTH)+1)+"/"+ calendar.get(Calendar.YEAR)
    +" " + calendar.get(Calendar.HOUR_OF_DAY)
    +":"+calendar.get(Calendar.MINUTE)
    +":"+ calendar.get(Calendar.SECOND)
    + "\nNota: " + nota
    + "\n"+ pergunta + "\n" 
    + "\nPalavras-chave: "+ palavrasChave + "\n");

  }

  public String toString(String nomeUsuario){
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-03:00"),new Locale("pt","BR"));
    calendar.setTimeInMillis(this.criacao);


    return (
    ((ativa)  ? "" : "(Arquivada)") +
    "---------------------------------------------------------"+
    "\n\t"+ pergunta + "\t\n" + 
    "---------------------------------------------------------"+
    "\nCriada em " + calendar.get(Calendar.DAY_OF_MONTH) 
    +"/"+(calendar.get(Calendar.MONTH)+1)+"/"+ calendar.get(Calendar.YEAR)
    +" às " + calendar.get(Calendar.HOUR_OF_DAY)
    +":"+calendar.get(Calendar.MINUTE)
    +":"+ calendar.get(Calendar.SECOND)
    +  " Por " + nomeUsuario +"\nNota: " + nota
    + "\nPalavras-chave: "+ palavrasChave + "\n");

  }



  
  public byte[] toByteArray()throws IOException{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(idPergunta);
    dos.writeInt(idUsuario);
    dos.writeLong(criacao);
    dos.writeShort(nota);
    dos.writeUTF(pergunta);
    dos.writeUTF(palavrasChave);// adicionado 15/03
    dos.writeBoolean(ativa);
    dos.close();
    return baos.toByteArray();
  }
  public void fromByteArray(byte[] ba) throws IOException{
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    
    this.idPergunta = dis.readInt();
    this.idUsuario  = dis.readInt();
    this.criacao = dis.readLong();
    this.nota = dis.readShort();
    this.pergunta= dis.readUTF();
    this.palavrasChave = dis.readUTF(); // add sabado
    this.ativa= dis.readBoolean();
    
    
    dis.close();
    

  }
}

class SortbyNota implements Comparator<Pergunta>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Pergunta a, Pergunta b)
    {
        return a.getNota() - b.getNota();
    }
}

