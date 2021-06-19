import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario implements Registro{
  protected int idUsuario;
  protected String nome;
  protected String email;
  protected String senha;

  public Usuario(){
    idUsuario = -1;
    nome = "";
    email = "";
    senha = "";
  }
  public Usuario(int i,String t, String e, String s) throws NoSuchAlgorithmException, IOException{
    setId(i);
    setNome(t);
    setEmail(e);
    setSenha(gerarHash(s));
    
  }

  public void setId(int idUsuario){
    this.idUsuario= idUsuario;
  }
  public int getId(){
    return this.idUsuario;
  }
  public void setNome(String nome){
    this.nome = nome;
  }
  public String getNome(){
    return this.nome;
  }
  public void setEmail(String email){
    this.email = email;
  }
  public String getEmail(){
    return this.email;
  }
  public void setSenha(String senha){
    this.senha = senha;
  }
  public String getSenha(){
    return this.senha;
  }
  


  public String toString(){
    
    return "\n ID: " + idUsuario + 
           "\n Nome: " + nome +
           "\n Email: " + email +
           "\n Senha: " + senha;
  }

  public byte[] toByteArray()throws IOException{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(idUsuario);
    dos.writeUTF(nome);
    dos.writeUTF(email);
    dos.writeUTF(senha);
    dos.close();
    return baos.toByteArray();
  }
  public void fromByteArray(byte[] ba) throws IOException{
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    
    this.idUsuario = dis.readInt();
    this.nome  = dis.readUTF();
    this.email = dis.readUTF();
    this.senha = dis.readUTF();
    
    dis.close();
    

  }

  public static String gerarHash(String s) throws IOException, NoSuchAlgorithmException{
    MessageDigest algorithm = MessageDigest.getInstance("MD5");
    byte messageDigest[] = algorithm.digest(s.getBytes("UTF-8"));

    StringBuilder hexString = new StringBuilder();
    for (byte b : messageDigest) {
        hexString.append(String.format("%02X", 0xFF & b));
    }
    String senhahex = hexString.toString();

    return senhahex;
  }
}




