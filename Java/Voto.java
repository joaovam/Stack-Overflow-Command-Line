import java.util.*;
import java.io.*;
public class Voto implements Registro{

  private int idVoto;
  private int idUsuario;
  private byte tipo; //Pergunta ou resposta
  int idPR; //id da pergunta ou resposta
  boolean voto;  //se foi positivo ou negativo

   public Voto(){    
    this.idVoto= this.idUsuario = this.idPR=-1; 
    this.tipo=(byte)'N';
    this.voto=true;
    
    
  }
  public Voto(int idVoto,int idUsuario,int idPR){    
    this(idVoto, idUsuario,(byte)'N', idPR, true);
  }
  
  public Voto(int idVoto, int idUsuario,byte tipo, int idPR, boolean voto){    
    this.idVoto=idVoto;
    this.idUsuario=idUsuario;
    this.tipo=tipo; 
    this.idPR=idPR;
    this.voto=voto;
    
  }

 public void setId(int id){
    this.idVoto = id;
  }
  
  public int getId(){
    return this.idVoto;
  }
  public void setID_Usuario(int id){
    this.idUsuario=id;
  }
  public int getID_Usuario(){
    return this.idUsuario;
  }
  public void setTipo(byte t){
    this.tipo=t;
  }
  public byte getTipo(){
    return this.tipo;
  }
  public void setIdPR(int id){
    this.idPR=id;
  }
  public int getIdPR(){
    return this.idPR;
  }
  public void setVoto(boolean voto){
    this.voto=voto;
  }
  public boolean getVoto(){
    return this.voto;
  }


  public String toString(){
    return (/*idVoto, idUsuario, tipo, idPR, voto*/
    "Voto: "+ ((voto)  ? "Positivo" : "Negativo") + "\n" + "Para " 
    + ((tipo=='P')  ? "Pergunta" : "Resposta") +", de id = " +idPR+ " criado por usuário de id"
     +idUsuario + "Tem id= " + idVoto);

  }

  public String toString(String nomeUsuario, String PR){

    return (/*idVoto, idUsuario, tipo, idPR, voto*/
    "Voto: "+ ((voto)  ? "Positivo" : "Negativo") + "\n" + "Para a " 
    + ((tipo=='P')  ? "Pergunta" : "Resposta") +" "+ PR + " criada por"
     + nomeUsuario );

  }
  
  public byte[] toByteArray()throws IOException{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(idVoto);
    dos.writeInt(idUsuario);
    dos.writeByte(tipo);
    dos.writeInt(idPR);
    dos.writeBoolean(voto);
    dos.close();
    return baos.toByteArray();
  }
  public void fromByteArray(byte[] ba) throws IOException{
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.idVoto = dis.readInt();
    this.idUsuario = dis.readInt();
    this.tipo  = dis.readByte();
    this.idPR = dis.readInt();
    this.voto = dis.readBoolean();  
    dis.close();
  }
}