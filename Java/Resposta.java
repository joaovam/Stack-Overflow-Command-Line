import java.util.*;
import java.io.*;
public class Resposta implements Registro{

  private int idResposta;
  private int idPergunta;
  private int idUsuario;
  private long criacao;
  private short nota;
  private String resposta;
  private boolean ativa;
   public Resposta(){    
    this.idPergunta=-1; 
    this.idUsuario=-1; 
    this.idResposta=-1;   
    criacao = System.currentTimeMillis();
    this.nota=0;
    this.resposta="";
    this.ativa=true;
    
  }
  public Resposta(int id,int idUsuario,int idPergunta){    
    this(id, idUsuario, idPergunta,(short)0, "", true);
  }

  public Resposta(int id,int idUsuario, int idPergunta, String resposta){
    this(id, idUsuario, idPergunta,(short)0, resposta, true);
  }

  public Resposta(int id,int idUsuario, int idPergunta,String resposta, boolean ativa){
    this(id, idUsuario,idPergunta,(short)0, resposta, ativa);
  }
  
  public Resposta(int id, int idUser, int idP,short nota, String resposta,boolean ativa){    
    this.idPergunta=idP; 
    this.idUsuario=idUser; 
    this.idResposta=id;   
    criacao = System.currentTimeMillis();
    this.nota=nota;
    this.resposta=resposta;
    this.ativa=ativa;
    
  }

 public void setId(int id){
    this.idResposta = id;
  }
  
  public int getId(){
    return this.idResposta;
  }

  public int getID_Usuario(){
    return this.idUsuario;
  }


  public long getCriacao(){    
    
    return this.criacao;

  }
  public int getNota(){
    return this.nota;
  }
  public void atualizaNota(boolean voto){
    if(voto){
      this.nota += (byte) 1;
    }else{      
      this.nota -= (byte) 1;
    }
            
  }

  public void setResposta(String resposta){
    this.resposta=resposta;
  }
  
  public String getResposta(){
    return this.resposta;
  }


  public void setAtiva(boolean ativa){
    this.ativa=ativa;
  }
  public boolean getAtiva(){
    return this.ativa;
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
    + "\n"+ resposta + "\n");

  }

  public String toString(String nomeUsuario){
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-03:00"),new Locale("pt","BR"));
    calendar.setTimeInMillis(this.criacao);


    return (
    ((ativa)  ? " " : "(Arquivada)") +    
    resposta + "\t\n" +
    "\nCriada em " + calendar.get(Calendar.DAY_OF_MONTH) 
    +"/"+(calendar.get(Calendar.MONTH)+1)+"/"+ calendar.get(Calendar.YEAR)
    +" às " + calendar.get(Calendar.HOUR_OF_DAY)
    +":"+calendar.get(Calendar.MINUTE)
    +":"+ calendar.get(Calendar.SECOND)
    +  " Por " + nomeUsuario +"\nNota: " + nota
    + "\n");

  }



  
  public byte[] toByteArray()throws IOException{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(idResposta);
    dos.writeInt(idPergunta);
    dos.writeInt(idUsuario);
    dos.writeLong(criacao);
    dos.writeShort(nota);
    dos.writeUTF(resposta);
    dos.writeBoolean(ativa);
    dos.close();
    return baos.toByteArray();
  }
  public void fromByteArray(byte[] ba) throws IOException{
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.idResposta = dis.readInt();
    this.idPergunta = dis.readInt();
    this.idUsuario  = dis.readInt();
    this.criacao = dis.readLong();
    this.nota = dis.readShort();
    this.resposta= dis.readUTF();
    this.ativa= dis.readBoolean();    
    dis.close();
    
  }
}
  
